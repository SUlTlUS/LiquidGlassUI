package com.coui.appcompat.seekbar.effect;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.app.NotificationCompat;
import com.coui.appcompat.animation.touchanimation.COUIIComposeMotionUnit;
import com.coui.appcompat.animation.touchanimation.COUITouchMotionAnimator;
import com.coui.appcompat.animation.touchanimation.TouchMotionParamConfig;
import com.coui.appcompat.seekbar.core.SeekBarState;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\t\u0018\u0000 &2\u00020\u0001:\u0001&B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J \u0010\u000f\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u000bH\u0016J\b\u0010\u0015\u001a\u00020\u000eH\u0016J\u0006\u0010\u0016\u001a\u00020\bJ\u000e\u0010\u0017\u001a\u00020\f2\u0006\u0010\u0018\u001a\u00020\u0019J\b\u0010\u001a\u001a\u00020\u001bH\u0016J\u0016\u0010\u001c\u001a\u00020\f2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001eJ\u0006\u0010 \u001a\u00020\fJ\b\u0010!\u001a\u00020\fH\u0002J\u0010\u0010\"\u001a\u00020\f2\u0006\u0010#\u001a\u00020\u000eH\u0016J\u001c\u0010$\u001a\u00020\f2\u0014\u0010%\u001a\u0010\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\f\u0018\u00010\nR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\t\u001a\u0010\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\f\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006'"}, d2 = {"Lcom/coui/appcompat/seekbar/effect/TouchMotionEffectRenderer;", "Lcom/coui/appcompat/seekbar/effect/IEffectRenderer;", "context", "Landroid/content/Context;", "hostView", "Landroid/view/View;", "(Landroid/content/Context;Landroid/view/View;)V", "mAnimator", "Lcom/coui/appcompat/animation/touchanimation/COUITouchMotionAnimator;", "mOnTranslationXChangeListener", "Lkotlin/Function1;", "", "", "mParams", "Lcom/coui/appcompat/seekbar/effect/EffectParams;", "drawEffect", "canvas", "Landroid/graphics/Canvas;", "state", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "progressPosition", "getEffectParams", "getTouchMotionAnimator", "handleTouchEvent", NotificationCompat.CATEGORY_EVENT, "Landroid/view/MotionEvent;", "isEnabled", "", "onViewSizeChanged", "width", "", "height", "release", "resetHostTransforms", "setEffectParams", "params", "setOnTranslationXChangeListener", "listener", "Companion", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class TouchMotionEffectRenderer implements IEffectRenderer {
    private static final float DEFAULT_MOVEMENT_STRENGTH = 0.3f;
    private final View hostView;
    private final COUITouchMotionAnimator mAnimator;
    private Function1<? super Float, Unit> mOnTranslationXChangeListener;
    private final EffectParams mParams;

    public TouchMotionEffectRenderer(Context context, View hostView) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(hostView, "hostView");
        this.hostView = hostView;
        this.mParams = new EffectParams();
        COUITouchMotionAnimator cOUITouchMotionAnimator = new COUITouchMotionAnimator(context);
        cOUITouchMotionAnimator.addOnAnimationUpdateCallback(new l(this));
        cOUITouchMotionAnimator.setDeformationEnable(true).setRubberEnable(true).setPressScaleEnable(true).config(new TouchMotionParamConfig().setAnimationParamsType(COUITouchMotionAnimator.AnimationParamsType.SLIDE).setMovementStrength(0.3f));
        this.mAnimator = cOUITouchMotionAnimator;
    }

    public static /* synthetic */ void a(TouchMotionEffectRenderer touchMotionEffectRenderer, COUIIComposeMotionUnit.ContributeSink contributeSink) {
        mAnimator$lambda$1$lambda$0(touchMotionEffectRenderer, contributeSink);
    }

    private static final void mAnimator$lambda$1$lambda$0(TouchMotionEffectRenderer this$0, COUIIComposeMotionUnit.ContributeSink contributeSink) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.hostView.setTranslationX(contributeSink.getTranslateX());
        Function1<? super Float, Unit> function1 = this$0.mOnTranslationXChangeListener;
        if (function1 != null) {
            function1.invoke(Float.valueOf(contributeSink.getTranslateX()));
        }
    }

    private final void resetHostTransforms() {
        this.hostView.setTranslationX(0.0f);
        this.hostView.setTranslationY(0.0f);
        this.hostView.setScaleX(1.0f);
        this.hostView.setScaleY(1.0f);
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    public void drawEffect(Canvas canvas, SeekBarState state, float progressPosition) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        Intrinsics.checkNotNullParameter(state, "state");
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    public DrawPhase getDrawPhase() {
        return IEffectRenderer.DefaultImpls.getDrawPhase(this);
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    /* JADX INFO: renamed from: getEffectParams, reason: from getter */
    public EffectParams getMParams() {
        return this.mParams;
    }

    /* JADX INFO: renamed from: getTouchMotionAnimator, reason: from getter */
    public final COUITouchMotionAnimator getMAnimator() {
        return this.mAnimator;
    }

    public final void handleTouchEvent(MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (getMEnabled()) {
            this.mAnimator.handleTouchEvent(event);
        }
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    /* JADX INFO: renamed from: isEnabled */
    public boolean getMEnabled() {
        return this.mParams.getEnabled();
    }

    public final void onViewSizeChanged(int width, int height) {
        if (getMEnabled()) {
            this.mAnimator.onSizeChange(width, height);
        }
    }

    public final void release() {
        this.mAnimator.cancel();
        this.mAnimator.reset();
        resetHostTransforms();
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    public void setEffectParams(EffectParams params) {
        Intrinsics.checkNotNullParameter(params, "params");
        this.mParams.setEnabled(params.getEnabled());
    }

    public final void setOnTranslationXChangeListener(Function1<? super Float, Unit> listener) {
        this.mOnTranslationXChangeListener = listener;
    }
}