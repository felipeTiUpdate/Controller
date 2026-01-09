import { useMemo, useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useNavigate, useParams } from 'react-router-dom';
import dayjs from 'dayjs';
import { getDeviceUsage, deleteDevice } from '../api/devices.js';
import SectionHeader from '../components/SectionHeader.jsx';
import Button from '../components/Button.jsx';
import RangeSelector from '../components/RangeSelector.jsx';
import LoadingState from '../components/LoadingState.jsx';
import ErrorState from '../components/ErrorState.jsx';
import Modal from '../components/Modal.jsx';
import { formatDateTime, formatMegabytes } from '../utils/formatting.js';

function DeviceDetailPage() {
  const { deviceId } = useParams();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [range, setRange] = useState({
    startDate: dayjs().startOf('month').format('YYYY-MM-DD'),
    endDate: dayjs().format('YYYY-MM-DD'),
  });
  const [isDeleteModalOpen, setDeleteModalOpen] = useState(false);

  const usageQuery = useQuery({
    queryKey: ['devices', deviceId, 'usage', range],
    queryFn: () => getDeviceUsage(deviceId, range),
    enabled: Boolean(deviceId),
  });

  const deleteMutation = useMutation({
    mutationFn: () => deleteDevice(deviceId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['devices'] });
      queryClient.invalidateQueries({ queryKey: ['reports'] });
      navigate('/devices');
    },
  });

  const deviceUsage = usageQuery.data;

  const usageSummary = useMemo(() => {
    if (!deviceUsage?.usage?.entries?.length) {
      return {
        total: 0,
        byNetwork: {},
      };
    }

    const total = deviceUsage.usage.entries.reduce((sum, entry) => sum + Number(entry.megabytes || 0), 0);
    const byNetwork = deviceUsage.usage.entries.reduce((acc, entry) => {
      const key = entry.networkType || 'DESCONHECIDA';
      acc[key] = (acc[key] || 0) + Number(entry.megabytes || 0);
      return acc;
    }, {});

    return {
      total,
      byNetwork,
    };
  }, [deviceUsage]);

  if (usageQuery.isLoading) {
    return <LoadingState message="Carregando detalhes do dispositivo..." />;
  }

  if (usageQuery.isError) {
    return (
      <ErrorState
        message="Não foi possível carregar os dados do dispositivo."
        onRetry={() => usageQuery.refetch()}
      />
    );
  }

  const device = deviceUsage?.device;
  if (!device) {
    return <ErrorState message="Dispositivo não encontrado." onRetry={() => navigate('/devices')} />;
  }

  const usageEntries = deviceUsage?.usage?.entries || [];

  return (
    <div className="space-y-10">
      <div className="flex flex-col gap-4 xl:flex-row xl:items-center xl:justify-between">
        <SectionHeader
          title={device.name}
          subtitle={
            device.simNumber
              ? `SIM ${device.simNumber} — monitorando consumo desde ${dayjs(device.createdAt).format('DD/MM/YYYY')}`
              : `Monitorando consumo desde ${dayjs(device.createdAt).format('DD/MM/YYYY')}`
          }
          action={
            <div className="flex flex-wrap gap-3">
              <Button variant="ghost" onClick={() => setDeleteModalOpen(true)}>
                Remover dispositivo
              </Button>
            </div>
          }
        />
        <RangeSelector value={range} onChange={setRange} />
      </div>

      <div className="grid gap-6 lg:grid-cols-3">
        <div className="rounded-2xl border border-white/5 bg-white/5 p-6 shadow-card backdrop-blur">
          <p className="text-xs uppercase tracking-[0.3em] text-white/40">Franquia</p>
          <p className="mt-2 font-display text-3xl font-semibold text-white">
            {device.dataLimitMb ? formatMegabytes(device.dataLimitMb) : 'Sem limite'}
          </p>
          <p className="mt-2 text-sm text-white/60">
            {device.dataLimitMb
              ? `Disponível até ${formatMegabytes(Math.max(device.dataLimitMb - usageSummary.total, 0))}`
              : 'Sem limite configurado'}
          </p>
        </div>
        <div className="rounded-2xl border border-white/5 bg-white/5 p-6 shadow-card backdrop-blur">
          <p className="text-xs uppercase tracking-[0.3em] text-white/40">Consumo total no período</p>
          <p className="mt-2 font-display text-3xl font-semibold text-white">
            {formatMegabytes(deviceUsage?.usage?.totalMb || 0)}
          </p>
          <p className="mt-2 text-sm text-white/60">
            {dayjs(range.startDate).format('DD/MM')} até {dayjs(range.endDate).format('DD/MM')}
          </p>
        </div>
        <div className="rounded-2xl border border-white/5 bg-white/5 p-6 shadow-card backdrop-blur">
          <p className="text-xs uppercase tracking-[0.3em] text-white/40">Distribuição por rede</p>
          <div className="mt-3 space-y-2 text-sm text-white/70">
            {Object.keys(usageSummary.byNetwork).length ? (
              Object.entries(usageSummary.byNetwork)
                .sort(([, a], [, b]) => b - a)
                .map(([network, total]) => (
                  <div key={network} className="flex items-center justify-between">
                    <span className="text-white">{network}</span>
                    <span>{formatMegabytes(total)}</span>
                  </div>
                ))
            ) : (
              <p className="text-white/40">Sem registros no período selecionado</p>
            )}
          </div>
        </div>
      </div>

      <div className="rounded-2xl border border-white/5 bg-white/5 p-6 shadow-card backdrop-blur">
        <div className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
          <div>
            <h2 className="font-display text-lg font-semibold text-white">Registros de consumo</h2>
            <p className="text-sm text-white/60">Entradas detalhadas sincronizadas a partir do aplicativo</p>
          </div>
        </div>

        <div className="mt-6 overflow-x-auto">
          <table className="min-w-full text-left text-sm">
            <thead className="text-white/60">
              <tr className="border-b border-white/10">
                <th className="py-2 font-medium">Data</th>
                <th className="py-2 font-medium">Rede</th>
                <th className="py-2 font-medium">Consumo (MB)</th>
                <th className="py-2 font-medium">Descrição</th>
              </tr>
            </thead>
            <tbody>
              {usageEntries.length ? (
                usageEntries.map((entry) => (
                  <tr key={entry.id} className="border-b border-white/5 last:border-none">
                    <td className="py-3 text-white">{formatDateTime(entry.recordedAt)}</td>
                    <td className="py-3 text-white/70">{entry.networkType || '—'}</td>
                    <td className="py-3 text-white">{formatMegabytes(entry.megabytes)}</td>
                    <td className="py-3 text-white/60">{entry.description || '—'}</td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="4" className="py-8 text-center text-white/50">
                    Nenhum registro encontrado no período selecionado.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      <Modal open={isDeleteModalOpen} onClose={() => setDeleteModalOpen(false)} title="Remover dispositivo">
        <div className="space-y-6 text-white/80">
          <p>
            Tem certeza que deseja remover <strong>{device.name}</strong>? Todos os dados associados serão excluídos.
          </p>
          <div className="flex justify-end gap-3">
            <Button variant="ghost" onClick={() => setDeleteModalOpen(false)} disabled={deleteMutation.isLoading}>
              Cancelar
            </Button>
            <Button variant="danger" onClick={() => deleteMutation.mutate()} disabled={deleteMutation.isLoading}>
              {deleteMutation.isLoading ? 'Removendo...' : 'Remover'}
            </Button>
          </div>
        </div>
      </Modal>
    </div>
  );
}

export default DeviceDetailPage;
