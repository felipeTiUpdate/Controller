function StatCard({ title, value, subtitle, highlight }) {
  return (
    <div className="rounded-2xl border border-white/5 bg-white/5 p-6 shadow-card backdrop-blur-md">
      <div className="flex items-center justify-between">
        <p className="text-sm uppercase tracking-[0.25em] text-white/50">{title}</p>
        {highlight ? <span className="text-xs text-accent uppercase tracking-[0.2em]">{highlight}</span> : null}
      </div>
      <p className="mt-4 font-display text-3xl font-semibold text-white">{value}</p>
      {subtitle ? <p className="mt-2 text-sm text-white/60">{subtitle}</p> : null}
    </div>
  );
}

export default StatCard;
