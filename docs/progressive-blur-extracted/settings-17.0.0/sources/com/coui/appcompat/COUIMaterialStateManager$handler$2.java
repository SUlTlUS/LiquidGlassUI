package com.coui.appcompat;

import android.os.Handler;
import android.os.Looper;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "Landroid/os/Handler;", "invoke"}, k = 3, mv = {1, 9, 0}, xi = 48)
public final class COUIMaterialStateManager$handler$2 extends Lambda implements Function0<Handler> {
    public static final COUIMaterialStateManager$handler$2 INSTANCE = new COUIMaterialStateManager$handler$2();

    public COUIMaterialStateManager$handler$2() {
        super(0);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    public final Handler invoke() {
        return new Handler(Looper.getMainLooper());
    }

    @Override // kotlin.jvm.functions.Function0
    public /* bridge */ /* synthetic */ Handler invoke() {
        return invoke();
    }
}