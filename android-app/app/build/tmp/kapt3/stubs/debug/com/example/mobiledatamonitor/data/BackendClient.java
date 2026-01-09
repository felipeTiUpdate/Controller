package com.example.mobiledatamonitor.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \u001b2\u00020\u0001:\u0001\u001bB\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\u000b\u001a\u0004\u0018\u00010\f2\u0006\u0010\r\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u000fJ\u001a\u0010\u0010\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u0012\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\u0011H\u0002J(\u0010\u0014\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u0015\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0086@\u00a2\u0006\u0002\u0010\u001aR\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/example/mobiledatamonitor/data/BackendClient;", "", "context", "Landroid/content/Context;", "prefs", "Landroid/content/SharedPreferences;", "(Landroid/content/Context;Landroid/content/SharedPreferences;)V", "baseUrl", "", "userManager", "Lcom/example/mobiledatamonitor/data/UserManager;", "ensureDeviceId", "", "settings", "Lcom/example/mobiledatamonitor/data/DataPlanSettings;", "(Lcom/example/mobiledatamonitor/data/DataPlanSettings;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "postJson", "Lorg/json/JSONObject;", "path", "body", "sendUsage", "deviceId", "megabytes", "", "recordedAt", "Ljava/time/Instant;", "(IDLjava/time/Instant;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public final class BackendClient {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.SharedPreferences prefs = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREF_DEVICE_ID = "backend_device_id_v2";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String EMULATOR_URL = "http://10.0.2.2:4000";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String NETWORK_URL = "http://192.168.2.78:4000";
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String baseUrl = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.mobiledatamonitor.data.UserManager userManager = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.mobiledatamonitor.data.BackendClient.Companion Companion = null;
    
    public BackendClient(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.content.SharedPreferences prefs) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object ensureDeviceId(@org.jetbrains.annotations.NotNull()
    com.example.mobiledatamonitor.data.DataPlanSettings settings, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object sendUsage(int deviceId, double megabytes, @org.jetbrains.annotations.NotNull()
    java.time.Instant recordedAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super org.json.JSONObject> $completion) {
        return null;
    }
    
    private final org.json.JSONObject postJson(java.lang.String path, org.json.JSONObject body) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/example/mobiledatamonitor/data/BackendClient$Companion;", "", "()V", "EMULATOR_URL", "", "NETWORK_URL", "PREF_DEVICE_ID", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}