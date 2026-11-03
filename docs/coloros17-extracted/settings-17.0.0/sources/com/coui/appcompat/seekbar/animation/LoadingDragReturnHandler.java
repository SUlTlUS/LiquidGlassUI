package com.coui.appcompat.seekbar.animation;

import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.heytap.cloud.sdk.utils.Constants;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\u0018\u0000 \u00172\u00020\u0001:\u0002\u0016\u0017B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010\u0011\u001a\u00020\u0012J\b\u0010\u0013\u001a\u00020\u0012H\u0002J\b\u0010\u0014\u001a\u00020\u0012H\u0002J\u0006\u0010\u0015\u001a\u00020\u0012R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\n\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0007\"\u0004\b\u000b\u0010\tR\u0011\u0010\f\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\f\u0010\u0007R\u001a\u0010\r\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u0007\"\u0004\b\u000e\u0010\tR\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0018"}, d2 = {"Lcom/coui/appcompat/seekbar/animation/LoadingDragReturnHandler;", "", "callback", "Lcom/coui/appcompat/seekbar/animation/LoadingDragReturnHandler$Callback;", "(Lcom/coui/appcompat/seekbar/animation/LoadingDragReturnHandler$Callback;)V", "isDraggingDuringLoading", "", "()Z", "setDraggingDuringLoading", "(Z)V", "isEnabled", "setEnabled", "isInProgress", "isReturning", "setReturning", "returnAnim", "Lcom/coui/appcompat/animation/dynamicanimation/COUISpringAnimation;", Constants.ResultMessage.RESULT_CANCEL, "", "ensureReturnAnim", "onReturnComplete", "startReturnAnimation", "Callback", "Companion", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class LoadingDragReturnHandler {
    private static final float SPRING_BOUNCE = 0.0f;
    private static final float SPRING_RESPONSE = 0.3f;
    private final Callback callback;
    private boolean isDraggingDuringLoading;
    private boolean isEnabled;
    private boolean isReturning;
    private COUISpringAnimation returnAnim;

    @Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\b\u0010\u0004\u001a\u00020\u0003H&J\b\u0010\u0005\u001a\u00020\u0003H&J\b\u0010\u0006\u001a\u00020\u0007H&J\u0010\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u0003H&J\b\u0010\n\u001a\u00020\u0007H&¨\u0006\u000b"}, d2 = {"Lcom/coui/appcompat/seekbar/animation/LoadingDragReturnHandler$Callback;", "", "getCurrentProgress", "", "getMax", "getMin", "onReturnAnimComplete", "", "setProgress", "progress", "stopPhysicsMove", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public interface Callback {
        int getCurrentProgress();

        int getMax();

        int getMin();

        void onReturnAnimComplete();

        void setProgress(int progress);

        void stopPhysicsMove();
    }

    public LoadingDragReturnHandler(Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        this.callback = callback;
    }

    public static /* synthetic */ void a(LoadingDragReturnHandler loadingDragReturnHandler, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        ensureReturnAnim$lambda$2$lambda$0(loadingDragReturnHandler, cOUIDynamicAnimation, f3, f4);
    }

    public static /* synthetic */ void b(LoadingDragReturnHandler loadingDragReturnHandler, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        ensureReturnAnim$lambda$2$lambda$1(loadingDragReturnHandler, cOUIDynamicAnimation, z2, f3, f4);
    }

    private final void ensureReturnAnim() {
        if (this.returnAnim != null) {
            return;
        }
        COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(new FloatValueHolder(0.0f));
        cOUISpringAnimation.addUpdateListener(new c(this));
        cOUISpringAnimation.addEndListener(new d(this));
        this.returnAnim = cOUISpringAnimation;
    }

    private static final void ensureReturnAnim$lambda$2$lambda$0(LoadingDragReturnHandler this$0, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.callback.setProgress(RangesKt.coerceIn((int) f3, this$0.callback.getMin(), this$0.callback.getMax()));
    }

    private static final void ensureReturnAnim$lambda$2$lambda$1(LoadingDragReturnHandler this$0, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (z2) {
            return;
        }
        Callback callback = this$0.callback;
        callback.setProgress(callback.getMin());
        this$0.onReturnComplete();
    }

    private final void onReturnComplete() {
        this.isReturning = false;
        this.callback.onReturnAnimComplete();
    }

    public final void cancel() {
        COUISpringAnimation cOUISpringAnimation = this.returnAnim;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.cancel();
        }
        if (this.isReturning) {
            this.isReturning = false;
        }
        this.isDraggingDuringLoading = false;
    }

    /* JADX INFO: renamed from: isDraggingDuringLoading, reason: from getter */
    public final boolean getIsDraggingDuringLoading() {
        return this.isDraggingDuringLoading;
    }

    /* JADX INFO: renamed from: isEnabled, reason: from getter */
    public final boolean getIsEnabled() {
        return this.isEnabled;
    }

    public final boolean isInProgress() {
        return this.isDraggingDuringLoading || this.isReturning;
    }

    /* JADX INFO: renamed from: isReturning, reason: from getter */
    public final boolean getIsReturning() {
        return this.isReturning;
    }

    public final void setDraggingDuringLoading(boolean z2) {
        this.isDraggingDuringLoading = z2;
    }

    public final void setEnabled(boolean z2) {
        this.isEnabled = z2;
    }

    public final void setReturning(boolean z2) {
        this.isReturning = z2;
    }

    public final void startReturnAnimation() {
        this.callback.stopPhysicsMove();
        COUISpringAnimation cOUISpringAnimation = this.returnAnim;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.cancel();
        }
        this.isReturning = true;
        int currentProgress = this.callback.getCurrentProgress();
        int min = this.callback.getMin();
        if (currentProgress <= min) {
            onReturnComplete();
            return;
        }
        ensureReturnAnim();
        COUISpringAnimation cOUISpringAnimation2 = this.returnAnim;
        if (cOUISpringAnimation2 == null) {
            return;
        }
        float f3 = min;
        cOUISpringAnimation2.setSpring(new COUISpringForce(f3).setBounce(0.0f).setResponse(0.3f));
        cOUISpringAnimation2.setStartValue(currentProgress);
        cOUISpringAnimation2.animateToFinalPosition(f3);
    }
}