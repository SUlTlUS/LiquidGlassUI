package com.coui.appcompat.seekbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import androidx.core.app.NotificationCompat;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.seekbar.animation.PhysicsAnimationHandler;
import com.coui.appcompat.seekbar.animation.SectionAnimationHandler;
import com.coui.appcompat.seekbar.core.COUISeekBarCore;
import com.coui.appcompat.seekbar.core.SeekBarState;
import com.coui.appcompat.seekbar.feedback.VibrationFeedbackHandler;
import com.coui.appcompat.seekbar.renderer.IProgressRenderer;
import com.coui.appcompat.seekbar.renderer.SectionProgressRenderer;
import com.coui.appcompat.statelistutil.COUIStateListUtil;
import com.oplus.dmp.sdk.search.SearchProtocol;
import com.support.seekbar.R;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import kotlin.ranges.RangesKt;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000W\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\t*\u0001\r\b\u0017\u0018\u0000 52\u00020\u0001:\u00015B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007¢\u0006\u0002\u0010\tJ\b\u0010\u0016\u001a\u00020\u0017H\u0014J\u0018\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u00192\u0006\u0010\u001b\u001a\u00020\u0019H\u0002J\u0010\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fH\u0014J*\u0010 \u001a\u00020\u001d2\u0006\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u0014J\u0010\u0010!\u001a\u00020\u001d2\u0006\u0010\u0002\u001a\u00020\u0003H\u0014J\b\u0010\"\u001a\u00020\u001dH\u0014J(\u0010#\u001a\u00020\u001d2\u0006\u0010$\u001a\u00020\u00072\u0006\u0010%\u001a\u00020\u00072\u0006\u0010&\u001a\u00020\u00072\u0006\u0010'\u001a\u00020\u0007H\u0014J\u0010\u0010(\u001a\u00020\u001d2\u0006\u0010)\u001a\u00020\u0007H\u0016J \u0010*\u001a\u00020\u001d2\u0006\u0010+\u001a\u00020\u00072\u0006\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020-H\u0016J\u0010\u0010/\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fH\u0014J\b\u00100\u001a\u00020\u001dH\u0002J\u0010\u00101\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fH\u0014J\u0010\u00102\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fH\u0014J\b\u00103\u001a\u00020\u001dH\u0014J\b\u00104\u001a\u00020\u001dH\u0014R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u00020\rX\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u000eR\u001b\u0010\u000f\u001a\u00020\u00108BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0011\u0010\u0012R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u000bX\u0082\u000e¢\u0006\u0002\n\u0000¨\u00066"}, d2 = {"Lcom/coui/appcompat/seekbar/COUISectionSeekBar;", "Lcom/coui/appcompat/seekbar/COUISeekBar;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "defStyleRes", "(Landroid/content/Context;Landroid/util/AttributeSet;II)V", "mSectionActiveMarkColorStateList", "Landroid/content/res/ColorStateList;", "mSectionAnimCallback", "com/coui/appcompat/seekbar/COUISectionSeekBar$mSectionAnimCallback$1", "Lcom/coui/appcompat/seekbar/COUISectionSeekBar$mSectionAnimCallback$1;", "mSectionAnimHandler", "Lcom/coui/appcompat/seekbar/animation/SectionAnimationHandler;", "getMSectionAnimHandler", "()Lcom/coui/appcompat/seekbar/animation/SectionAnimationHandler;", "mSectionAnimHandler$delegate", "Lkotlin/Lazy;", "mSectionInactiveMarkColorStateList", "createProgressRenderer", "Lcom/coui/appcompat/seekbar/renderer/IProgressRenderer;", "getFingerTrackX", "", "primary", SearchProtocol.QUERY_LENGTH, "handleMotionEventUp", "", NotificationCompat.CATEGORY_EVENT, "Landroid/view/MotionEvent;", "initState", "initVibrationFeedback", "onDetachedFromWindow", "onSizeChanged", "w", "h", "oldw", "oldh", "setMax", "max", "setProgress", "progress", "animate", "", "fromUser", "startClickTransitionAnim", "syncSectionThumbXFromProgress", "trackTouchEvent", "trackTouchEventByFinger", "updateColors", "updateState", "Companion", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class COUISectionSeekBar extends COUISeekBar {
    private static final float DRAG_DIRECTION_THRESHOLD = 0.5f;
    private static final float POSITION_BIAS = 0.1f;
    private static final int VELOCITY_COMPUTE_TIME = 100;
    private ColorStateList mSectionActiveMarkColorStateList;
    private final COUISectionSeekBar$mSectionAnimCallback$1 mSectionAnimCallback;

    /* JADX INFO: renamed from: mSectionAnimHandler$delegate, reason: from kotlin metadata */
    private final Lazy mSectionAnimHandler;
    private ColorStateList mSectionInactiveMarkColorStateList;

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUISectionSeekBar(Context context) {
        this(context, null, 0, 0, 14, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public static final /* synthetic */ COUISeekBarCore.OnSeekBarChangeListener access$getMOnSeekBarChangeListener(COUISectionSeekBar cOUISectionSeekBar) {
        return cOUISectionSeekBar.getMOnSeekBarChangeListener();
    }

    public static final /* synthetic */ COUISectionSeekBar$mSectionAnimCallback$1 access$getMSectionAnimCallback$p(COUISectionSeekBar cOUISectionSeekBar) {
        return cOUISectionSeekBar.mSectionAnimCallback;
    }

    public static final /* synthetic */ VibrationFeedbackHandler access$getMVibrationHandler(COUISectionSeekBar cOUISectionSeekBar) {
        return cOUISectionSeekBar.getMVibrationHandler();
    }

    public static final /* synthetic */ void access$performFeedback(COUISectionSeekBar cOUISectionSeekBar) {
        cOUISectionSeekBar.performFeedback();
    }

    public static final /* synthetic */ void access$setMInternalProgress(COUISectionSeekBar cOUISectionSeekBar, int i3) {
        cOUISectionSeekBar.setMInternalProgress(i3);
    }

    public static final /* synthetic */ void access$stopTrackingTouch(COUISectionSeekBar cOUISectionSeekBar, boolean z2, boolean z3) {
        cOUISectionSeekBar.stopTrackingTouch(z2, z3);
    }

    private final float getFingerTrackX(float primary, float length) {
        return RangesKt.coerceIn(primaryToHorizontalAbsoluteScale(primary, length) * length, 0.0f, length);
    }

    private final SectionAnimationHandler getMSectionAnimHandler() {
        return (SectionAnimationHandler) this.mSectionAnimHandler.getValue();
    }

    private final void syncSectionThumbXFromProgress() {
        SeekBarState state = getState();
        float seekBarLength = getSeekBarLength();
        if (seekBarLength <= 0.0f) {
            return;
        }
        state.setSectionThumbX(state.progressToScale(state.getProgress()) * seekBarLength);
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public IProgressRenderer createProgressRenderer() {
        return new SectionProgressRenderer();
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarAdvanced, com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void handleMotionEventUp(MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        releaseThumbScaleAnim();
        if (getMStartDragging()) {
            SeekBarState state = getState();
            float scale = state.getScale();
            if (0.0f > scale || scale > 1.0f) {
                float f3 = state.getScale() > 1.0f ? 1.0f : 0.0f;
                state.setScale(f3);
                state.setDrawProgressScale(f3);
                startDeformationRelease();
                if (getMSectionAnimHandler().isDragSnapRunning()) {
                    getMSectionAnimHandler().setFastMoving(true);
                }
            } else if (getMSectionAnimHandler().isDragSnapRunning()) {
                getMSectionAnimHandler().setFastMoving(true);
            } else {
                float touchPrimaryCoordinate = getTouchPrimaryCoordinate(event);
                float seekBarLength = getSeekBarLength();
                if (seekBarLength > 0.0f) {
                    int max = state.getMax() - state.getMin();
                    getMSectionAnimHandler().snapToNearestSection(RangesKt.coerceIn(state.getProgress() + ((int) (((RangesKt.coerceIn(primaryToHorizontalAbsoluteScale(touchPrimaryCoordinate, seekBarLength), 0.0f, 1.0f) * seekBarLength) - (state.progressToScale(state.getProgress()) * seekBarLength)) / (max > 0 ? seekBarLength / max : seekBarLength))), state.getMin(), state.getMax()), seekBarLength);
                }
            }
            if (!getMSectionAnimHandler().getIsFastMoving()) {
                onStopTrackingTouch(true);
            }
            setPressed(false);
        }
        if (!getMSectionAnimHandler().getIsFastMoving()) {
            getMSectionAnimHandler().resetDragState();
        }
        PhysicsAnimationHandler mPhysicsHandler = getMPhysicsHandler();
        if (mPhysicsHandler != null) {
            mPhysicsHandler.recycleVelocityTracker();
        }
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void initState(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) throws Throwable {
        Intrinsics.checkNotNullParameter(context, "context");
        super.initState(context, attrs, defStyleAttr, defStyleRes);
        SeekBarState state = getState();
        state.setSectionMarkRadius(context.getResources().getDimensionPixelSize(R.dimen.coui_section_seekbar_tick_mark_radius));
        int i3 = R.color.coui_seekbar_active_mark_selector;
        ColorStateList colorStateListCreateColorStateList = COUIStateListUtil.createColorStateList(COUIContextUtil.getColor(context, i3), COUIContextUtil.getColor(context, R.color.coui_seekbar_active_mark_disable_color));
        this.mSectionActiveMarkColorStateList = colorStateListCreateColorStateList;
        int i4 = R.color.coui_seekbar_inactive_mark_selector;
        ColorStateList colorStateListCreateColorStateList2 = COUIStateListUtil.createColorStateList(COUIContextUtil.getColor(context, i4), COUIContextUtil.getColor(context, R.color.coui_seekbar_inactive_mark_disable_color));
        this.mSectionInactiveMarkColorStateList = colorStateListCreateColorStateList2;
        state.setSectionActiveMarkColor(getColor(this, colorStateListCreateColorStateList, COUIContextUtil.getColor(context, i3)));
        state.setSectionInactiveMarkColor(getColor(this, colorStateListCreateColorStateList2, COUIContextUtil.getColor(context, i4)));
        syncSectionThumbXFromProgress();
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarAdvanced
    public void initVibrationFeedback(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        super.initVibrationFeedback(context);
        VibrationFeedbackHandler mVibrationHandler = getMVibrationHandler();
        if (mVibrationHandler == null) {
            return;
        }
        mVibrationHandler.setUseSectionStyleFeedback(true);
    }

    @Override // com.coui.appcompat.seekbar.COUISeekBar, com.coui.appcompat.seekbar.core.COUISeekBarAdvanced, com.coui.appcompat.seekbar.core.COUISeekBarCore, android.widget.ProgressBar, android.view.View
    public void onDetachedFromWindow() {
        getMSectionAnimHandler().cancelAll();
        cancelDeformationRelease();
        super.onDetachedFromWindow();
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarAdvanced, com.coui.appcompat.seekbar.core.COUISeekBarCore, android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    public void onSizeChanged(int w2, int h3, int oldw, int oldh) {
        super.onSizeChanged(w2, h3, oldw, oldh);
        syncSectionThumbXFromProgress();
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore, android.widget.AbsSeekBar, android.widget.ProgressBar, com.coui.appcompat.seekbar.core.ICOUISeekBar
    public void setMax(int max) {
        int iCoerceAtLeast = RangesKt.coerceAtLeast(max, getMin());
        super.setMax(iCoerceAtLeast);
        if (getState().getProgress() > iCoerceAtLeast) {
            setProgress(iCoerceAtLeast, false, false);
        } else {
            syncSectionThumbXFromProgress();
        }
        invalidate();
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void setProgress(int progress, boolean animate, boolean fromUser) {
        SeekBarState state = getState();
        cancelFlexibleFollowHandAnim();
        int iCoerceIn = RangesKt.coerceIn(progress, state.getMin(), state.getMax());
        if (state.getProgress() == iCoerceIn) {
            return;
        }
        if (animate) {
            getMSectionAnimHandler().startProgressTransitionAnim(iCoerceIn, fromUser, getSeekBarLength(), state.getEnableVibrator());
        } else {
            state.setProgress(iCoerceIn);
            setMInternalProgress(iCoerceIn);
            state.setOldProgress(state.getProgress());
            float seekBarLength = getSeekBarLength();
            float fProgressToScale = state.progressToScale(iCoerceIn);
            state.setScale(fProgressToScale);
            state.setDrawProgressScale(fProgressToScale);
            state.setSectionThumbX(seekBarLength > 0.0f ? fProgressToScale * seekBarLength : 0.0f);
            syncProgressToParent(iCoerceIn);
            COUISeekBarCore.OnSeekBarChangeListener mOnSeekBarChangeListener = getMOnSeekBarChangeListener();
            if (mOnSeekBarChangeListener != null) {
                mOnSeekBarChangeListener.onProgressChanged(this, iCoerceIn, fromUser);
            }
            invalidate();
        }
        resetDeformationValue();
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarAdvanced
    public void startClickTransitionAnim(MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        float touchPrimaryCoordinate = getTouchPrimaryCoordinate(event);
        float seekBarLength = getSeekBarLength();
        if (seekBarLength <= 0.0f) {
            return;
        }
        SeekBarState state = getState();
        int max = state.getMax() - state.getMin();
        setProgress(RangesKt.coerceIn(max > 0 ? MathKt.roundToInt((RangesKt.coerceIn(primaryToHorizontalAbsoluteScale(touchPrimaryCoordinate, seekBarLength), 0.0f, 1.0f) * max) + state.getMin()) : state.getMin(), state.getMin(), state.getMax()), true, true);
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarAdvanced, com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void trackTouchEvent(MotionEvent event) {
        VelocityTracker velocityTracker;
        VelocityTracker velocityTracker2;
        Intrinsics.checkNotNullParameter(event, "event");
        cancelDeformationRelease();
        float touchPrimaryCoordinate = getTouchPrimaryCoordinate(event);
        float seekBarLength = getSeekBarLength();
        if (seekBarLength <= 0.0f) {
            return;
        }
        SeekBarState state = getState();
        int max = state.getMax() - state.getMin();
        if (max <= 0) {
            return;
        }
        float fPrimaryToHorizontalAbsoluteScale = primaryToHorizontalAbsoluteScale(touchPrimaryCoordinate, seekBarLength);
        int i3 = 0;
        setTouchScale(fPrimaryToHorizontalAbsoluteScale, false);
        float fCoerceIn = RangesKt.coerceIn(fPrimaryToHorizontalAbsoluteScale * seekBarLength, 0.0f, seekBarLength);
        float f3 = seekBarLength / max;
        if (getMSectionAnimHandler().getDragAnchorSection() < state.getMin()) {
            getMSectionAnimHandler().initDragAnchor(fCoerceIn, seekBarLength, getFingerTrackX(getMTouchDownX(), seekBarLength));
            setMLastX(touchPrimaryCoordinate);
            executeTouchGlitterEffectAnim();
            PhysicsAnimationHandler mPhysicsHandler = getMPhysicsHandler();
            if (mPhysicsHandler == null || (velocityTracker2 = mPhysicsHandler.getVelocityTracker()) == null) {
                return;
            }
            velocityTracker2.computeCurrentVelocity(100);
            return;
        }
        if (fCoerceIn > getMSectionAnimHandler().getLastFingerTrackX() + 0.5f) {
            i3 = 1;
        } else if (fCoerceIn < getMSectionAnimHandler().getLastFingerTrackX() - 0.5f) {
            i3 = -1;
        }
        if (i3 != 0 && getMSectionAnimHandler().getDragDirection() == 0) {
            getMSectionAnimHandler().setDragDirection(i3);
        } else if (i3 != 0 && i3 == (-getMSectionAnimHandler().getDragDirection())) {
            getMSectionAnimHandler().onDirectionReversed(i3, seekBarLength);
        }
        float dragAnchorThumbX = fCoerceIn - getMSectionAnimHandler().getDragAnchorThumbX();
        int i4 = (int) ((dragAnchorThumbX >= 0.0f ? 0.1f + dragAnchorThumbX : dragAnchorThumbX - 0.1f) / f3);
        float f4 = i4 * f3;
        getMSectionAnimHandler().setDragCurrentOffset(dragAnchorThumbX);
        if (RangesKt.coerceIn(getMSectionAnimHandler().getDragAnchorSection() + i4, state.getMin(), state.getMax()) != state.getProgress()) {
            getMSectionAnimHandler().startDragSnapAnimation(getMSectionAnimHandler().getDragAnchorThumbX(), getMSectionAnimHandler().getDragAnchorThumbX() + f4, getMSectionAnimHandler().getDragMoveAnimValue(), seekBarLength);
        } else if (!getMSectionAnimHandler().isDragSnapRunning()) {
            float fCoerceIn2 = RangesKt.coerceIn(getMSectionAnimHandler().getDragAnchorThumbX() + f4 + ((dragAnchorThumbX - f4) * 0.6f), 0.0f, seekBarLength);
            state.setSectionThumbX(fCoerceIn2);
            state.setDrawProgressScale(RangesKt.coerceIn(fCoerceIn2 / seekBarLength, 0.0f, 1.0f));
            invalidate();
        }
        getMSectionAnimHandler().setLastFingerTrackX(fCoerceIn);
        setMLastX(touchPrimaryCoordinate);
        executeTouchGlitterEffectAnim();
        PhysicsAnimationHandler mPhysicsHandler2 = getMPhysicsHandler();
        if (mPhysicsHandler2 == null || (velocityTracker = mPhysicsHandler2.getVelocityTracker()) == null) {
            return;
        }
        velocityTracker.computeCurrentVelocity(100);
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarAdvanced, com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void trackTouchEventByFinger(MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        trackTouchEvent(event);
    }

    @Override // com.coui.appcompat.seekbar.COUISeekBar, com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void updateColors() {
        super.updateColors();
        SeekBarState state = getState();
        ColorStateList colorStateList = this.mSectionActiveMarkColorStateList;
        if (colorStateList != null) {
            state.setSectionActiveMarkColor(getColor(this, colorStateList, COUIContextUtil.getColor(getContext(), R.color.coui_seekbar_active_mark_selector)));
        }
        ColorStateList colorStateList2 = this.mSectionInactiveMarkColorStateList;
        if (colorStateList2 != null) {
            state.setSectionInactiveMarkColor(getColor(this, colorStateList2, COUIContextUtil.getColor(getContext(), R.color.coui_seekbar_inactive_mark_selector)));
        }
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void updateState() {
        SeekBarState state = getState();
        super.updateState();
        if (state.getSectionThumbX() >= 0.0f) {
            float progressStart = getProgressStart();
            float seekBarLength = state.getIsRtl() ? (progressStart + getSeekBarLength()) - state.getSectionThumbX() : progressStart + state.getSectionThumbX();
            if (state.getIsSupportDeformation()) {
                float heightTopDeformedUpValue = state.getHeightTopDeformedUpValue() - state.getHeightBottomDeformedDownValue();
                if (state.getIsRtl()) {
                    heightTopDeformedUpValue = -heightTopDeformedUpValue;
                }
                seekBarLength += heightTopDeformedUpValue;
            }
            state.setThumbPosition(seekBarLength);
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUISectionSeekBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUISectionSeekBar(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0, 8, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public /* synthetic */ COUISectionSeekBar(Context context, AttributeSet attributeSet, int i3, int i4, int i5, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i5 & 2) != 0 ? null : attributeSet, (i5 & 4) != 0 ? R.attr.couiSectionSeekBarStyle : i3, (i5 & 8) != 0 ? R.style.COUISectionSeekBar : i4);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUISectionSeekBar(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        Intrinsics.checkNotNullParameter(context, "context");
        this.mSectionAnimHandler = LazyKt.lazy(new COUISectionSeekBar$mSectionAnimHandler$2(this));
        this.mSectionAnimCallback = new COUISectionSeekBar$mSectionAnimCallback$1(this);
    }
}