package com.example.mobiledatamonitor.worker;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u0000 \u00132\u00020\u0001:\u0001\u0013B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u000b\u001a\u00020\fH\u0002J\b\u0010\r\u001a\u00020\fH\u0002J\u000e\u0010\u000e\u001a\u00020\u000fH\u0096@\u00a2\u0006\u0002\u0010\u0010J\b\u0010\u0011\u001a\u00020\u0012H\u0002R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/example/mobiledatamonitor/worker/DataUsageMonitorWorker;", "Landroidx/work/CoroutineWorker;", "context", "Landroid/content/Context;", "params", "Landroidx/work/WorkerParameters;", "(Landroid/content/Context;Landroidx/work/WorkerParameters;)V", "prefs", "Landroid/content/SharedPreferences;", "repository", "Lcom/example/mobiledatamonitor/data/DataUsageRepository;", "checkAppUsage", "", "checkPlanUsage", "doWork", "Landroidx/work/ListenableWorker$Result;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTodayKey", "", "Companion", "app_debug"})
public final class DataUsageMonitorWorker extends androidx.work.CoroutineWorker {
    @org.jetbrains.annotations.NotNull()
    private final com.example.mobiledatamonitor.data.DataUsageRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.SharedPreferences prefs = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String WORK_NAME = "data_usage_monitor";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREFS_NAME = "data_monitor_prefs";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_LAST_PLAN_WARNING = "last_plan_warning";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_LAST_PLAN_EXCEEDED = "last_plan_exceeded";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_NOTIFIED_APPS_PREFIX = "notified_app_";
    public static final long APP_HIGH_USAGE_THRESHOLD_MB = 50L;
    public static final float PLAN_WARNING_PERCENTAGE = 70.0F;
    public static final float PLAN_CRITICAL_PERCENTAGE = 90.0F;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.mobiledatamonitor.worker.DataUsageMonitorWorker.Companion Companion = null;
    
    public DataUsageMonitorWorker(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    androidx.work.WorkerParameters params) {
        super(null, null);
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object doWork(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super androidx.work.ListenableWorker.Result> $completion) {
        return null;
    }
    
    private final void checkAppUsage() {
    }
    
    private final void checkPlanUsage() {
    }
    
    private final java.lang.String getTodayKey() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/example/mobiledatamonitor/worker/DataUsageMonitorWorker$Companion;", "", "()V", "APP_HIGH_USAGE_THRESHOLD_MB", "", "KEY_LAST_PLAN_EXCEEDED", "", "KEY_LAST_PLAN_WARNING", "KEY_NOTIFIED_APPS_PREFIX", "PLAN_CRITICAL_PERCENTAGE", "", "PLAN_WARNING_PERCENTAGE", "PREFS_NAME", "WORK_NAME", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}