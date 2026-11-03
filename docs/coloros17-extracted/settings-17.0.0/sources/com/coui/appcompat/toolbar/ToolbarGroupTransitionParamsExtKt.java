package com.coui.appcompat.toolbar;

import com.coui.iola.SpringData;
import com.oplus.settings.statistics.SettingsStatisticsConstant;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000.\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\n\u0010\u0002\u001a\u00020\u0001*\u00020\u0003\u001a\u001e\u0010\u0002\u001a\u00020\u0001*\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u001a5\u0010\u0007\u001a\u00020\b*\u00020\u00012\u0006\u0010\t\u001a\u00020\n2!\u0010\u000b\u001a\u001d\u0012\u0013\u0012\u00110\u0001¢\u0006\f\b\r\u0012\b\b\u000e\u0012\u0004\b\b(\u000f\u0012\u0004\u0012\u00020\b0\f\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"toolbarGroupTransitionParamsDefault", "Lcom/coui/appcompat/toolbar/ToolbarGroupTransitionParams;", SettingsStatisticsConstant.METHOD_GET, "Lcom/coui/appcompat/toolbar/ToolbarGroupTransitionParams$Companion;", "instanceKey", "", "customServerName", "observe", "", "owner", "", "listener", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "params", "coui-support-toolbar_release"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ToolbarGroupTransitionParamsExtKt {
    private static final ToolbarGroupTransitionParams toolbarGroupTransitionParamsDefault = new ToolbarGroupTransitionParams(new SpringData(0.0f, 0.2f, 0.0f), new SpringData(0.35f, 0.3f, 0.0f), new SpringData(0.0f, 0.2f, 0.0f), 0.8f, 10.0f, new SpringData(0.0f, 0.2f, 0.0f), new SpringData(0.0f, 0.2f, 0.0f), new SpringData(0.0f, 0.2f, 0.0f), 0.8f, 10.0f, 0.00390625f, 0.001f, 1.0f, new SpringData(0.0f, 0.2f, 0.0f), new SpringData(0.0f, 0.2f, 0.0f), 0.00390625f);

    public static final ToolbarGroupTransitionParams get(ToolbarGroupTransitionParams.Companion companion) {
        Intrinsics.checkNotNullParameter(companion, "<this>");
        return toolbarGroupTransitionParamsDefault;
    }

    public static /* synthetic */ ToolbarGroupTransitionParams get$default(ToolbarGroupTransitionParams.Companion companion, String str, String str2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            str2 = null;
        }
        return get(companion, str, str2);
    }

    public static final void observe(ToolbarGroupTransitionParams toolbarGroupTransitionParams, Object owner, Function1<? super ToolbarGroupTransitionParams, Unit> listener) {
        Intrinsics.checkNotNullParameter(toolbarGroupTransitionParams, "<this>");
        Intrinsics.checkNotNullParameter(owner, "owner");
        Intrinsics.checkNotNullParameter(listener, "listener");
    }

    public static final ToolbarGroupTransitionParams get(ToolbarGroupTransitionParams.Companion companion, String instanceKey, String str) {
        Intrinsics.checkNotNullParameter(companion, "<this>");
        Intrinsics.checkNotNullParameter(instanceKey, "instanceKey");
        return toolbarGroupTransitionParamsDefault;
    }
}