package com.example.mobiledatamonitor;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0010\u001a\u00020\u0011H\u0014J\u000e\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u0017J\b\u0010\u0018\u001a\u00020\u0011H\u0002J\u0006\u0010\u0019\u001a\u00020\u0011J\u0010\u0010\u001a\u001a\u00020\u00112\b\b\u0002\u0010\u001b\u001a\u00020\u001cJ\b\u0010\u001d\u001a\u00020\u0011H\u0002J\u000e\u0010\u001e\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020 R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00070\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006!"}, d2 = {"Lcom/example/mobiledatamonitor/UsageMonitorViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_state", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/example/mobiledatamonitor/UsageMonitorState;", "autoRefreshJob", "Lkotlinx/coroutines/Job;", "repository", "Lcom/example/mobiledatamonitor/data/DataUsageRepository;", "state", "Lkotlinx/coroutines/flow/StateFlow;", "getState", "()Lkotlinx/coroutines/flow/StateFlow;", "onCleared", "", "onRangeSelected", "range", "Lcom/example/mobiledatamonitor/data/UsageRange;", "onTabSelected", "tabIndex", "", "refreshDataPlanStatus", "refreshPermissions", "refreshUsage", "force", "", "scheduleAutoRefresh", "updateDataPlanLimit", "limitGB", "", "app_debug"})
public final class UsageMonitorViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.mobiledatamonitor.data.DataUsageRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.example.mobiledatamonitor.UsageMonitorState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.example.mobiledatamonitor.UsageMonitorState> state = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job autoRefreshJob;
    
    public UsageMonitorViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.example.mobiledatamonitor.UsageMonitorState> getState() {
        return null;
    }
    
    public final void onRangeSelected(@org.jetbrains.annotations.NotNull()
    com.example.mobiledatamonitor.data.UsageRange range) {
    }
    
    public final void onTabSelected(int tabIndex) {
    }
    
    public final void updateDataPlanLimit(float limitGB) {
    }
    
    public final void refreshUsage(boolean force) {
    }
    
    private final void refreshDataPlanStatus() {
    }
    
    public final void refreshPermissions() {
    }
    
    private final void scheduleAutoRefresh() {
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
}