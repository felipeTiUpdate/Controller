import { Link } from 'react-router-dom';
import { FiChevronRight } from 'react-icons/fi';
import { formatMegabytes } from '../utils/formatting.js';

function UsageBar({ value, limit }) {
  const percentage = limit ? Math.min((value / limit) * 100, 100) : 0;
  return (
    <div className="mt-4 h-2 w-full rounded-full bg-white/10">
      <div
        className="h-full rounded-full bg-gradient-to-r from-primary-500 via-primary-400 to-accent"
        style={{ width: `${percentage}%` }}
      />
    </div>
  );
}

function DeviceCard({ device, onEdit, onDelete }) {
  const percent = device.dataLimitMb ? device.usage?.percentage : null;

  return (
    <div className="group flex h-full flex-col rounded-3xl border border-white/5 bg-white/5 p-6 shadow-card backdrop-blur">
      <div className="flex items-start justify-between">
        <div>
          <p className="text-xs uppercase tracking-[0.3em] text-white/40">Dispositivo</p>
          <h3 className="mt-2 font-display text-xl font-semibold text-white">{device.name}</h3>
        </div>
        <button
          type="button"
          onClick={() => onDelete(device)}
          className="rounded-full border border-white/10 px-3 py-1 text-xs font-semibold uppercase tracking-[0.3em] text-white/40 transition hover:bg-red-500/20 hover:text-red-100"
        >
          remover
        </button>
      </div>
      <dl className="mt-4 space-y-1 text-sm text-white/60">
        <div className="flex justify-between">
          <dt>SIM</dt>
          <dd className="text-white">{device.simNumber || '—'}</dd>
        </div>
        <div className="flex justify-between">
          <dt>Franquia</dt>
          <dd className="text-white">
            {device.dataLimitMb ? formatMegabytes(device.dataLimitMb) : 'Sem limite'}
          </dd>
        </div>
        <div className="flex justify-between">
          <dt>Consumo</dt>
          <dd className="text-white">{formatMegabytes(device.usage?.totalMb || 0)}</dd>
        </div>
      </dl>
      {device.dataLimitMb ? <UsageBar value={device.usage?.totalMb || 0} limit={device.dataLimitMb} /> : null}
      <div className="mt-6 flex items-center justify-between text-sm text-white/60">
        <div>
          {percent ? (
            <p>
              <span className="text-white">{percent}%</span> da franquia consumida
            </p>
          ) : (
            <p>Consumo monitorado em tempo real</p>
          )}
        </div>
        <div className="flex gap-3">
          <button
            type="button"
            onClick={() => onEdit(device)}
            className="rounded-full border border-white/10 px-3 py-1 text-xs font-semibold uppercase tracking-[0.3em] text-white/60 transition hover:bg-white/10 hover:text-white"
          >
            editar
          </button>
          <Link
            to={`/devices/${device.id}`}
            className="inline-flex items-center gap-2 rounded-full border border-primary-500/40 px-4 py-1 text-xs font-semibold uppercase tracking-[0.3em] text-primary-200 transition hover:bg-primary-500/20"
          >
            detalhes <FiChevronRight size={16} />
          </Link>
        </div>
      </div>
    </div>
  );
}

export default DeviceCard;
