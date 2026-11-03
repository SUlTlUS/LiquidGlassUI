package com.coui.appcompat.toolbar;

import android.util.Log;
import com.coui.appcompat.log.COUILog;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u001f\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005H\u0082\b¨\u0006\u0006"}, d2 = {"logD", "", "tag", "", "lazyMessage", "Lkotlin/Function0;", "coui-support-toolbar_release"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class AppBarBlurHelperKt {
    private static final void logD(String str, Function0<String> function0) {
        if (COUILog.LOG_DEBUG || Log.isLoggable(str, 3)) {
            Log.d(str, function0.invoke());
        }
    }
}