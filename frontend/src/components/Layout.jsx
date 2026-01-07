import { NavLink, Outlet } from 'react-router-dom';
import { FiActivity, FiBarChart2, FiSmartphone } from 'react-icons/fi';
import clsx from 'classnames';

const navItems = [
  { to: '/', label: 'Visão Geral', icon: FiBarChart2, end: true },
  { to: '/devices', label: 'Dispositivos', icon: FiSmartphone },
];

function Layout() {
  return (
    <div className="min-h-screen bg-slate-950 text-white flex">
      <aside className="w-72 hidden lg:flex flex-col border-r border-white/5 bg-gradient-to-b from-white/10 via-white/5 to-transparent backdrop-blur-md">
        <div className="px-8 py-10">
          <div className="flex items-center gap-3">
            <div className="h-12 w-12 rounded-2xl bg-primary-500/20 flex items-center justify-center shadow-card">
              <FiActivity className="text-primary-400" size={26} />
            </div>
            <div>
              <p className="font-display text-xl font-bold tracking-tight">Pulse</p>
              <p className="text-xs uppercase tracking-[0.3em] text-white/60">Dados móveis</p>
            </div>
          </div>
        </div>
        <nav className="mt-4 flex-1 px-6 space-y-2">
          {navItems.map(({ to, label, icon: Icon, end }) => (
            <NavLink
              key={to}
              to={to}
              end={end}
              className={({ isActive }) =>
                clsx(
                  'flex items-center gap-3 rounded-xl px-4 py-3 text-sm font-medium transition-all duration-200',
                  isActive
                    ? 'bg-primary-500/20 text-white shadow-card'
                    : 'text-white/70 hover:bg-white/5 hover:text-white'
                )
              }
            >
              <Icon size={18} />
              {label}
            </NavLink>
          ))}
        </nav>
        <div className="px-6 py-10 text-xs text-white/40">
          <p>Monitoramento em tempo real de consumo de dados móveis, com alertas e tendências inteligentes.</p>
        </div>
      </aside>

      <div className="flex-1 flex flex-col overflow-hidden">
        <header className="sticky top-0 z-20 backdrop-blur-xl border-b border-white/5 bg-slate-950/70">
          <div className="mx-auto max-w-6xl px-6">
            <div className="flex items-center justify-between py-6">
              <div>
                <h1 className="font-display text-2xl md:text-3xl font-semibold tracking-tight">Monitor de Dados</h1>
                <p className="text-sm text-white/60">Acompanhe o uso de franquias e redes móveis em todas as linhas.</p>
              </div>
              <div className="hidden md:flex items-center gap-3">
                <div className="rounded-full border border-white/10 bg-white/5 px-5 py-2 text-xs uppercase tracking-[0.3em] text-white/60">
                  Atualizado em tempo real
                </div>
              </div>
            </div>
          </div>
        </header>
        <main className="flex-1 overflow-y-auto">
          <div className="mx-auto max-w-6xl px-6 py-10">
            <Outlet />
          </div>
        </main>
      </div>
    </div>
  );
}

export default Layout;
