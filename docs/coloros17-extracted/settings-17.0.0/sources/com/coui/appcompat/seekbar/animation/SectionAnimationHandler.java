package com.coui.appcompat.seekbar.animation;

import android.animation.ValueAnimator;
import android.view.animation.Interpolator;
import androidx.core.view.animation.PathInterpolatorCompat;
import com.coui.appcompat.seekbar.core.SeekBarState;
import com.oplus.dmp.sdk.search.SearchProtocol;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import kotlin.ranges.RangesKt;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0018\u0018\u0000 ?2\u00020\u0001:\u0002>?B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010'\u001a\u00020(2\u0006\u0010)\u001a\u00020\fH\u0002J\u0006\u0010*\u001a\u00020(J\u0010\u0010+\u001a\u00020(2\u0006\u0010)\u001a\u00020\fH\u0002J\u001e\u0010,\u001a\u00020(2\u0006\u0010-\u001a\u00020\f2\u0006\u0010)\u001a\u00020\f2\u0006\u0010.\u001a\u00020\fJ\u0016\u0010/\u001a\u00020(2\u0006\u00100\u001a\u00020\b2\u0006\u0010)\u001a\u00020\fJ\u0006\u00101\u001a\u00020(J\u000e\u00102\u001a\u00020(2\u0006\u00103\u001a\u00020\bJ\u0016\u00104\u001a\u00020(2\u0006\u00105\u001a\u00020\b2\u0006\u0010)\u001a\u00020\fJ&\u00106\u001a\u00020(2\u0006\u00107\u001a\u00020\f2\u0006\u00108\u001a\u00020\f2\u0006\u00109\u001a\u00020\f2\u0006\u0010)\u001a\u00020\fJ&\u0010:\u001a\u00020(2\u0006\u0010;\u001a\u00020\b2\u0006\u0010<\u001a\u00020\u001d2\u0006\u0010)\u001a\u00020\f2\u0006\u0010=\u001a\u00020\u001dR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\t\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\b@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u001e\u0010\r\u001a\u00020\f2\u0006\u0010\u0007\u001a\u00020\f@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u001a\u0010\u0010\u001a\u00020\fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u000f\"\u0004\b\u0012\u0010\u0013R\u001e\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\b@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000bR\u000e\u0010\u0016\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u0018\u001a\u00020\f2\u0006\u0010\u0007\u001a\u00020\f@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u000fR\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u001bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u001c\u001a\u00020\u001d8F¢\u0006\u0006\u001a\u0004\b\u001c\u0010\u001eR\u001a\u0010\u001f\u001a\u00020\u001dX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u001e\"\u0004\b \u0010!R\u0011\u0010\"\u001a\u00020\u001d8F¢\u0006\u0006\u001a\u0004\b\"\u0010\u001eR\u001a\u0010#\u001a\u00020\fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b$\u0010\u000f\"\u0004\b%\u0010\u0013R\u0010\u0010&\u001a\u0004\u0018\u00010\u001bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006@"}, d2 = {"Lcom/coui/appcompat/seekbar/animation/SectionAnimationHandler;", "", "state", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "callback", "Lcom/coui/appcompat/seekbar/animation/SectionAnimationHandler$Callback;", "(Lcom/coui/appcompat/seekbar/core/SeekBarState;Lcom/coui/appcompat/seekbar/animation/SectionAnimationHandler$Callback;)V", "<set-?>", "", "dragAnchorSection", "getDragAnchorSection", "()I", "", "dragAnchorThumbX", "getDragAnchorThumbX", "()F", "dragCurrentOffset", "getDragCurrentOffset", "setDragCurrentOffset", "(F)V", "dragDirection", "getDragDirection", "dragMoveAnimEndX", "dragMoveAnimStartX", "dragMoveAnimValue", "getDragMoveAnimValue", "dragMoveAnimator", "Landroid/animation/ValueAnimator;", "isDragSnapRunning", "", "()Z", "isFastMoving", "setFastMoving", "(Z)V", "isTransitionRunning", "lastFingerTrackX", "getLastFingerTrackX", "setLastFingerTrackX", "sectionTransitionAnimator", "calculateDragProgress", "", SearchProtocol.QUERY_LENGTH, "cancelAll", "handleFastMoveSnap", "initDragAnchor", "fingerTrackX", "touchDownTrackX", "onDirectionReversed", "newDir", "resetDragState", "setDragDirection", "dir", "snapToNearestSection", "targetSection", "startDragSnapAnimation", "startThumbX", "targetThumbX", "startOffset", "startProgressTransitionAnim", "targetProgress", "fromUser", "enableVibrator", "Callback", "Companion", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class SectionAnimationHandler {
    public static final float DRAG_MOVE_RATIO = 0.4f;
    private static final long DRAG_SNAP_DURATION = 100;
    private static final Interpolator DRAG_SNAP_INTERPOLATOR;
    private static final float PATH_INTERPOLATOR_CONTROL_X = 0.25f;
    private static final long PROGRESS_TRANSITION_DURATION = 200;
    private static final long RELEASE_SNAP_DURATION = 150;
    private static final float SNAP_POSITION_THRESHOLD = 0.5f;
    private final Callback callback;
    private int dragAnchorSection;
    private float dragAnchorThumbX;
    private float dragCurrentOffset;
    private int dragDirection;
    private float dragMoveAnimEndX;
    private float dragMoveAnimStartX;
    private float dragMoveAnimValue;
    private ValueAnimator dragMoveAnimator;
    private boolean isFastMoving;
    private float lastFingerTrackX;
    private ValueAnimator sectionTransitionAnimator;
    private final SeekBarState state;

    @Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\b\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&J\b\u0010\b\u001a\u00020\u0003H&J\u0010\u0010\t\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\b\u0010\n\u001a\u00020\u0003H&J\u0010\u0010\u000b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0018\u0010\f\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0007H&J\u0010\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&¨\u0006\u000f"}, d2 = {"Lcom/coui/appcompat/seekbar/animation/SectionAnimationHandler$Callback;", "", "notifyProgressChanged", "", "progress", "", "fromUser", "", "performFeedback", "performVibrationForSection", "requestInvalidate", "setInternalProgress", "stopTrackingTouch", "isTouching", "syncProgressToParent", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public interface Callback {
        void notifyProgressChanged(int progress, boolean fromUser);

        void performFeedback();

        void performVibrationForSection(int progress);

        void requestInvalidate();

        void setInternalProgress(int progress);

        void stopTrackingTouch(boolean fromUser, boolean isTouching);

        void syncProgressToParent(int progress);
    }

    static {
        Interpolator interpolatorCreate = PathInterpolatorCompat.create(0.0f, 0.0f, 0.25f, 1.0f);
        Intrinsics.checkNotNullExpressionValue(interpolatorCreate, "create(...)");
        DRAG_SNAP_INTERPOLATOR = interpolatorCreate;
    }

    public SectionAnimationHandler(SeekBarState state, Callback callback) {
        Intrinsics.checkNotNullParameter(state, "state");
        Intrinsics.checkNotNullParameter(callback, "callback");
        this.state = state;
        this.callback = callback;
        this.dragAnchorSection = Integer.MIN_VALUE;
        this.dragMoveAnimEndX = Float.NaN;
    }

    public static /* synthetic */ void a(SectionAnimationHandler sectionAnimationHandler, float f3, ValueAnimator valueAnimator) {
        startDragSnapAnimation$lambda$1$lambda$0(sectionAnimationHandler, f3, valueAnimator);
    }

    public static final /* synthetic */ Callback access$getCallback$p(SectionAnimationHandler sectionAnimationHandler) {
        return sectionAnimationHandler.callback;
    }

    public static final /* synthetic */ SeekBarState access$getState$p(SectionAnimationHandler sectionAnimationHandler) {
        return sectionAnimationHandler.state;
    }

    public static final /* synthetic */ void access$handleFastMoveSnap(SectionAnimationHandler sectionAnimationHandler, float f3) {
        sectionAnimationHandler.handleFastMoveSnap(f3);
    }

    public static /* synthetic */ void b(SectionAnimationHandler sectionAnimationHandler, float f3, ValueAnimator valueAnimator) {
        snapToNearestSection$lambda$3$lambda$2(sectionAnimationHandler, f3, valueAnimator);
    }

    public static /* synthetic */ void c(SectionAnimationHandler sectionAnimationHandler, float f3, ValueAnimator valueAnimator) {
        startProgressTransitionAnim$lambda$5$lambda$4(sectionAnimationHandler, f3, valueAnimator);
    }

    private final void calculateDragProgress(float length) {
        int iCeil;
        int min;
        int max = this.state.getMax() - this.state.getMin();
        if (max <= 0) {
            return;
        }
        float f3 = length / max;
        float f4 = this.dragMoveAnimEndX;
        float f5 = this.dragMoveAnimStartX;
        if (f4 > f5) {
            iCeil = MathKt.roundToInt(this.state.getSectionThumbX() / f3);
            min = this.state.getMin();
        } else {
            if (f4 >= f5) {
                return;
            }
            iCeil = (int) Math.ceil(this.state.getSectionThumbX() / f3);
            min = this.state.getMin();
        }
        int iCoerceIn = RangesKt.coerceIn(iCeil + min, this.state.getMin(), this.state.getMax());
        if (iCoerceIn != this.state.getProgress()) {
            this.state.setProgress(iCoerceIn);
            this.callback.syncProgressToParent(iCoerceIn);
            this.callback.setInternalProgress(iCoerceIn);
            this.callback.notifyProgressChanged(iCoerceIn, true);
            this.callback.performFeedback();
        }
    }

    private final void handleFastMoveSnap(float length) {
        int max = this.state.getMax() - this.state.getMin();
        float f3 = this.lastFingerTrackX;
        resetDragState();
        if (length <= 0.0f || max <= 0) {
            this.callback.stopTrackingTouch(true, true);
        } else {
            snapToNearestSection(RangesKt.coerceIn(MathKt.roundToInt((f3 / length) * max) + this.state.getMin(), this.state.getMin(), this.state.getMax()), length);
            this.callback.stopTrackingTouch(true, true);
        }
    }

    private static final void snapToNearestSection$lambda$3$lambda$2(SectionAnimationHandler this$0, float f3, ValueAnimator animation) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(animation, "animation");
        Object animatedValue = animation.getAnimatedValue();
        Intrinsics.checkNotNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
        float fFloatValue = ((Float) animatedValue).floatValue();
        this$0.state.setSectionThumbX(fFloatValue);
        this$0.state.setDrawProgressScale(RangesKt.coerceIn(f3 > 0.0f ? fFloatValue / f3 : 0.0f, 0.0f, 1.0f));
        this$0.callback.requestInvalidate();
    }

    private static final void startDragSnapAnimation$lambda$1$lambda$0(SectionAnimationHandler this$0, float f3, ValueAnimator animation) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(animation, "animation");
        Object animatedValue = animation.getAnimatedValue();
        Intrinsics.checkNotNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
        float fFloatValue = ((Float) animatedValue).floatValue();
        this$0.dragMoveAnimValue = fFloatValue;
        float fCoerceIn = RangesKt.coerceIn(this$0.dragMoveAnimStartX + (fFloatValue * 0.4f) + (this$0.dragCurrentOffset * 0.6f), 0.0f, f3);
        this$0.state.setSectionThumbX(fCoerceIn);
        this$0.state.setDrawProgressScale(RangesKt.coerceIn(fCoerceIn / f3, 0.0f, 1.0f));
        this$0.callback.requestInvalidate();
        this$0.calculateDragProgress(f3);
    }

    private static final void startProgressTransitionAnim$lambda$5$lambda$4(SectionAnimationHandler this$0, float f3, ValueAnimator animation) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(animation, "animation");
        Object animatedValue = animation.getAnimatedValue();
        Intrinsics.checkNotNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
        float fFloatValue = ((Float) animatedValue).floatValue();
        this$0.state.setSectionThumbX(fFloatValue);
        this$0.state.setDrawProgressScale(RangesKt.coerceIn(f3 > 0.0f ? fFloatValue / f3 : 0.0f, 0.0f, 1.0f));
        this$0.callback.requestInvalidate();
    }

    public final void cancelAll() {
        ValueAnimator valueAnimator;
        ValueAnimator valueAnimator2;
        ValueAnimator valueAnimator3 = this.sectionTransitionAnimator;
        if (valueAnimator3 != null && valueAnimator3.isRunning() && (valueAnimator2 = this.sectionTransitionAnimator) != null) {
            valueAnimator2.cancel();
        }
        ValueAnimator valueAnimator4 = this.dragMoveAnimator;
        if (valueAnimator4 == null || !valueAnimator4.isRunning() || (valueAnimator = this.dragMoveAnimator) == null) {
            return;
        }
        valueAnimator.cancel();
    }

    public final int getDragAnchorSection() {
        return this.dragAnchorSection;
    }

    public final float getDragAnchorThumbX() {
        return this.dragAnchorThumbX;
    }

    public final float getDragCurrentOffset() {
        return this.dragCurrentOffset;
    }

    public final int getDragDirection() {
        return this.dragDirection;
    }

    public final float getDragMoveAnimValue() {
        return this.dragMoveAnimValue;
    }

    public final float getLastFingerTrackX() {
        return this.lastFingerTrackX;
    }

    public final void initDragAnchor(float fingerTrackX, float length, float touchDownTrackX) {
        int max = this.state.getMax() - this.state.getMin();
        if (max <= 0) {
            return;
        }
        int iCoerceIn = RangesKt.coerceIn(MathKt.roundToInt(touchDownTrackX / (length / max)) + this.state.getMin(), this.state.getMin(), this.state.getMax());
        this.dragAnchorSection = iCoerceIn;
        this.dragAnchorThumbX = this.state.progressToScale(iCoerceIn) * length;
        this.dragMoveAnimValue = 0.0f;
        int progress = this.state.getProgress();
        this.state.setProgress(this.dragAnchorSection);
        this.callback.syncProgressToParent(this.dragAnchorSection);
        this.callback.setInternalProgress(this.dragAnchorSection);
        this.state.setSectionThumbX(this.dragAnchorThumbX);
        SeekBarState seekBarState = this.state;
        seekBarState.setDrawProgressScale(seekBarState.progressToScale(this.dragAnchorSection));
        int i3 = this.dragAnchorSection;
        if (progress != i3) {
            this.callback.notifyProgressChanged(i3, true);
            this.callback.performFeedback();
        }
        this.dragDirection = fingerTrackX < this.dragAnchorThumbX ? -1 : 1;
        this.lastFingerTrackX = fingerTrackX;
        this.callback.requestInvalidate();
    }

    public final boolean isDragSnapRunning() {
        ValueAnimator valueAnimator = this.dragMoveAnimator;
        return valueAnimator != null && valueAnimator.isRunning();
    }

    /* JADX INFO: renamed from: isFastMoving, reason: from getter */
    public final boolean getIsFastMoving() {
        return this.isFastMoving;
    }

    public final boolean isTransitionRunning() {
        ValueAnimator valueAnimator = this.sectionTransitionAnimator;
        return valueAnimator != null && valueAnimator.isRunning();
    }

    public final void onDirectionReversed(int newDir, float length) {
        this.dragDirection = newDir;
        if (this.dragAnchorSection != this.state.getProgress()) {
            this.dragAnchorSection = this.state.getProgress();
            SeekBarState seekBarState = this.state;
            this.dragAnchorThumbX = seekBarState.progressToScale(seekBarState.getProgress()) * length;
            this.dragMoveAnimValue = 0.0f;
        }
        ValueAnimator valueAnimator = this.dragMoveAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    public final void resetDragState() {
        ValueAnimator valueAnimator;
        this.dragAnchorSection = Integer.MIN_VALUE;
        this.dragAnchorThumbX = 0.0f;
        this.dragDirection = 0;
        this.dragCurrentOffset = 0.0f;
        this.dragMoveAnimValue = 0.0f;
        this.dragMoveAnimEndX = Float.NaN;
        this.dragMoveAnimStartX = 0.0f;
        this.lastFingerTrackX = 0.0f;
        this.isFastMoving = false;
        ValueAnimator valueAnimator2 = this.dragMoveAnimator;
        if (valueAnimator2 == null || !valueAnimator2.isRunning() || (valueAnimator = this.dragMoveAnimator) == null) {
            return;
        }
        valueAnimator.cancel();
    }

    public final void setDragCurrentOffset(float f3) {
        this.dragCurrentOffset = f3;
    }

    public final void setDragDirection(int dir) {
        this.dragDirection = dir;
    }

    public final void setFastMoving(boolean z2) {
        this.isFastMoving = z2;
    }

    public final void setLastFingerTrackX(float f3) {
        this.lastFingerTrackX = f3;
    }

    public final void snapToNearestSection(int targetSection, float length) {
        ValueAnimator valueAnimator;
        float sectionThumbX = this.state.getSectionThumbX();
        ValueAnimator valueAnimator2 = this.sectionTransitionAnimator;
        if (valueAnimator2 != null && valueAnimator2.isRunning() && (valueAnimator = this.sectionTransitionAnimator) != null) {
            valueAnimator.cancel();
        }
        float fProgressToScale = this.state.progressToScale(targetSection);
        float f3 = fProgressToScale * length;
        if (this.state.getProgress() != targetSection) {
            this.state.setProgress(targetSection);
            this.callback.syncProgressToParent(targetSection);
            this.callback.setInternalProgress(targetSection);
            this.callback.notifyProgressChanged(targetSection, true);
        }
        if (Math.abs(sectionThumbX - f3) < 0.5f) {
            SeekBarState seekBarState = this.state;
            seekBarState.setOldProgress(seekBarState.getProgress());
            this.state.setScale(fProgressToScale);
            this.state.setDrawProgressScale(fProgressToScale);
            this.state.setSectionThumbX(f3);
            this.callback.requestInvalidate();
            return;
        }
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(sectionThumbX, f3);
        valueAnimatorOfFloat.setDuration(150L);
        valueAnimatorOfFloat.addUpdateListener(new f(this, length));
        valueAnimatorOfFloat.addListener(new SectionAnimationHandler$snapToNearestSection$1$2(this, fProgressToScale, length));
        valueAnimatorOfFloat.start();
        this.sectionTransitionAnimator = valueAnimatorOfFloat;
    }

    public final void startDragSnapAnimation(float startThumbX, float targetThumbX, float startOffset, float length) {
        if (this.state.getSectionThumbX() == targetThumbX) {
            return;
        }
        ValueAnimator valueAnimator = this.dragMoveAnimator;
        if (valueAnimator != null && valueAnimator.isRunning() && this.dragMoveAnimEndX == targetThumbX) {
            return;
        }
        this.dragMoveAnimEndX = targetThumbX;
        this.dragMoveAnimStartX = startThumbX;
        ValueAnimator valueAnimator2 = this.dragMoveAnimator;
        if (valueAnimator2 != null) {
            valueAnimator2.cancel();
        }
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(startOffset, targetThumbX - startThumbX);
        valueAnimatorOfFloat.setDuration(100L);
        valueAnimatorOfFloat.setInterpolator(DRAG_SNAP_INTERPOLATOR);
        valueAnimatorOfFloat.addUpdateListener(new g(this, length));
        valueAnimatorOfFloat.addListener(new SectionAnimationHandler$startDragSnapAnimation$1$2(this, length));
        valueAnimatorOfFloat.start();
        this.dragMoveAnimator = valueAnimatorOfFloat;
    }

    public final void startProgressTransitionAnim(int targetProgress, boolean fromUser, float length, boolean enableVibrator) {
        ValueAnimator valueAnimator;
        int progress = this.state.getProgress();
        float sectionThumbX = this.state.getSectionThumbX();
        ValueAnimator valueAnimator2 = this.sectionTransitionAnimator;
        if (valueAnimator2 != null && valueAnimator2.isRunning() && (valueAnimator = this.sectionTransitionAnimator) != null) {
            valueAnimator.cancel();
        }
        if (fromUser && enableVibrator) {
            this.callback.performVibrationForSection(targetProgress);
        }
        this.state.setProgress(targetProgress);
        this.callback.syncProgressToParent(targetProgress);
        this.callback.setInternalProgress(targetProgress);
        this.callback.notifyProgressChanged(targetProgress, fromUser);
        if (length <= 0.0f) {
            SeekBarState seekBarState = this.state;
            seekBarState.setOldProgress(seekBarState.getProgress());
            float fProgressToScale = this.state.progressToScale(targetProgress);
            this.state.setScale(fProgressToScale);
            this.state.setDrawProgressScale(fProgressToScale);
            this.state.setSectionThumbX(fProgressToScale * length);
            this.callback.stopTrackingTouch(fromUser, false);
            this.callback.requestInvalidate();
            return;
        }
        float fProgressToScale2 = this.state.progressToScale(targetProgress) * length;
        if (sectionThumbX < 0.0f) {
            sectionThumbX = this.state.progressToScale(progress) * length;
        }
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(sectionThumbX, fProgressToScale2);
        valueAnimatorOfFloat.setDuration(200L);
        valueAnimatorOfFloat.addUpdateListener(new e(this, length));
        valueAnimatorOfFloat.addListener(new SectionAnimationHandler$startProgressTransitionAnim$1$2(this, targetProgress, length, fromUser));
        valueAnimatorOfFloat.start();
        this.sectionTransitionAnimator = valueAnimatorOfFloat;
    }
}