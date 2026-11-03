package com.coui.appcompat.toolbar;

import android.view.View;
import com.coui.appcompat.COUIMaterialStateManager;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0007"}, d2 = {"com/coui/appcompat/toolbar/AppBarBlurHelper$MaterialBlurWatcher$attachListener$1", "Landroid/view/View$OnAttachStateChangeListener;", "onViewAttachedToWindow", "", "v", "Landroid/view/View;", "onViewDetachedFromWindow", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class AppBarBlurHelper$MaterialBlurWatcher$attachListener$1 implements View.OnAttachStateChangeListener {
    final /* synthetic */ AppBarBlurHelper.MaterialBlurWatcher this$0;

    public AppBarBlurHelper$MaterialBlurWatcher$attachListener$1(AppBarBlurHelper.MaterialBlurWatcher materialBlurWatcher) {
        this.this$0 = materialBlurWatcher;
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewAttachedToWindow(View v2) {
        Intrinsics.checkNotNullParameter(v2, "v");
        AppBarBlurHelper.MaterialBlurWatcher.access$setLastProcessedBlurState$p(this.this$0, COUIMaterialStateManager.INSTANCE.isBlurEffectEnable());
        AppBarBlurHelper.MaterialBlurWatcher.access$getOnMaterialBlurChanged$p(this.this$0).invoke(v2);
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewDetachedFromWindow(View v2) {
        Intrinsics.checkNotNullParameter(v2, "v");
        v2.removeOnAttachStateChangeListener(this);
        COUIMaterialStateManager.INSTANCE.removeOnMaterialStateChangedListener(this.this$0);
        AppBarBlurHelper.access$removeBlurWatcherIfCurrent(AppBarBlurHelper.INSTANCE, v2, this.this$0);
    }
}