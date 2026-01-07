import clsx from 'classnames';

const variants = {
  primary:
    'bg-primary-500 text-white hover:bg-primary-400 focus-visible:outline-primary-300 disabled:bg-primary-500/40 disabled:text-white/70',
  ghost:
    'bg-white/0 text-white/80 hover:bg-white/10 focus-visible:outline-white disabled:text-white/40 disabled:hover:bg-white/0',
  danger:
    'bg-red-500/80 text-white hover:bg-red-500 focus-visible:outline-red-400 disabled:bg-red-500/30',
};

function Button({ type = 'button', variant = 'primary', className, children, ...props }) {
  return (
    <button
      type={type}
      className={clsx(
        'inline-flex items-center justify-center gap-2 rounded-xl px-4 py-2 text-sm font-medium transition focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2',
        variants[variant],
        className
      )}
      {...props}
    >
      {children}
    </button>
  );
}

export default Button;
