package com.example.mobiledatamonitor.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\t\n\u0002\b\t\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u0003R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\tj\u0002\b\nj\u0002\b\u000b\u00a8\u0006\f"}, d2 = {"Lcom/example/mobiledatamonitor/data/UsageRange;", "", "daysBack", "", "(Ljava/lang/String;IJ)V", "getDaysBack", "()J", "computeStartTimestamp", "nowMillis", "TODAY", "LAST_7_DAYS", "LAST_30_DAYS", "app_debug"})
public enum UsageRange {
    /*public static final*/ TODAY /* = new TODAY(0L) */,
    /*public static final*/ LAST_7_DAYS /* = new LAST_7_DAYS(0L) */,
    /*public static final*/ LAST_30_DAYS /* = new LAST_30_DAYS(0L) */;
    private final long daysBack = 0L;
    
    UsageRange(long daysBack) {
    }
    
    public final long getDaysBack() {
        return 0L;
    }
    
    public final long computeStartTimestamp(long nowMillis) {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.example.mobiledatamonitor.data.UsageRange> getEntries() {
        return null;
    }
}