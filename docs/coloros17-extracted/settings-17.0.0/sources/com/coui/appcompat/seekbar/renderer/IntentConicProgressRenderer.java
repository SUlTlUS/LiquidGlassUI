package com.coui.appcompat.seekbar.renderer;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.seekbar.COUIIntentSeekBar;
import com.coui.appcompat.seekbar.PlaybackState;
import com.coui.appcompat.seekbar.core.COUISeekBarCore;
import com.coui.appcompat.seekbar.core.SeekBarState;
import com.coui.appcompat.seekbar.handler.IOrientationHandler;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import n0.a;
import n0.b;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\u0018\u0000 32\u00020\u0001:\u00013B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J \u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020'2\u0006\u0010(\u001a\u00020)H\u0002J\u0018\u0010*\u001a\u00020#2\u0006\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020'H\u0016J\b\u0010+\u001a\u00020\u0006H\u0007J\b\u0010,\u001a\u00020#H\u0002J\u0006\u0010-\u001a\u00020#J\u0010\u0010.\u001a\u00020#2\b\b\u0001\u0010/\u001a\u00020\u0006J\u0018\u00100\u001a\u00020#2\u0006\u00101\u001a\u00020\u000e2\u0006\u00102\u001a\u00020\u000eH\u0002R\u0012\u0010\u0005\u001a\u00020\u00068\u0002@\u0002X\u0083\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082D¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082D¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0019X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001cX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u001fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u001fX\u0082\u0004¢\u0006\u0002\n\u0000¨\u00064"}, d2 = {"Lcom/coui/appcompat/seekbar/renderer/IntentConicProgressRenderer;", "Lcom/coui/appcompat/seekbar/renderer/StandardProgressRenderer;", "intentSeekBar", "Lcom/coui/appcompat/seekbar/COUIIntentSeekBar;", "(Lcom/coui/appcompat/seekbar/COUIIntentSeekBar;)V", "colorA", "", "colorBEnd", "colorBStart", "mBShaderMatrix", "Landroid/graphics/Matrix;", "mCachedBGradient", "Landroid/graphics/LinearGradient;", "mGradientAlpha", "", "mGradientAlphaAnim", "Lcom/coui/appcompat/animation/dynamicanimation/COUISpringAnimation;", "mGradientAlphaFrom", "mGradientAlphaTo", "mLastBGradientLen", "mLastBGradientRtl", "", "mLastGradientLenB", "mWasGradientVisible", "paintA", "Landroid/graphics/Paint;", "paintB", "pathA", "Landroid/graphics/Path;", "pathB", "rectA", "Landroid/graphics/RectF;", "rectB", "tempProgressRect", "drawConicStyleProgress", "", "canvas", "Landroid/graphics/Canvas;", "state", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "seekBar", "Lcom/coui/appcompat/seekbar/core/COUISeekBarCore;", "drawProgress", "getProgressColorA", "initGradientAlphaAnim", "release", "setProgressColorA", TypedValues.Custom.S_COLOR, "startGradientAlphaAnim", "from", TypedValues.TransitionType.S_TO, "Companion", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class IntentConicProgressRenderer extends StandardProgressRenderer {
    private static final int COLOR_CHANNEL_MAX = 255;
    private static final float GRADIENT_ALPHA_ANIM_FINAL = 10000.0f;
    private static final float GRADIENT_ALPHA_SPRING_BOUNCE = 0.0f;
    private static final float GRADIENT_ALPHA_SPRING_RESPONSE = 0.5f;
    private int colorA;
    private final int colorBEnd;
    private final int colorBStart;
    private final COUIIntentSeekBar intentSeekBar;
    private final Matrix mBShaderMatrix;
    private LinearGradient mCachedBGradient;
    private float mGradientAlpha;
    private COUISpringAnimation mGradientAlphaAnim;
    private float mGradientAlphaFrom;
    private float mGradientAlphaTo;
    private float mLastBGradientLen;
    private boolean mLastBGradientRtl;
    private float mLastGradientLenB;
    private boolean mWasGradientVisible;
    private final Paint paintA;
    private final Paint paintB;
    private final Path pathA;
    private final Path pathB;
    private final RectF rectA;
    private final RectF rectB;
    private final RectF tempProgressRect;

    public IntentConicProgressRenderer(COUIIntentSeekBar intentSeekBar) {
        Intrinsics.checkNotNullParameter(intentSeekBar, "intentSeekBar");
        this.intentSeekBar = intentSeekBar;
        this.colorA = COUIIntentSeekBar.DEFAULT_CONIC_LIGHT_COLOR;
        this.colorBStart = 16777215;
        this.colorBEnd = -1;
        Paint paint = new Paint(1);
        Paint.Style style = Paint.Style.FILL;
        paint.setStyle(style);
        paint.setColor(this.colorA);
        this.paintA = paint;
        Paint paint2 = new Paint(1);
        paint2.setStyle(style);
        this.paintB = paint2;
        this.rectA = new RectF();
        this.rectB = new RectF();
        this.pathA = new Path();
        this.pathB = new Path();
        this.tempProgressRect = new RectF();
        this.mBShaderMatrix = new Matrix();
    }

    public static /* synthetic */ void a(IntentConicProgressRenderer intentConicProgressRenderer, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        initGradientAlphaAnim$lambda$2(intentConicProgressRenderer, cOUIDynamicAnimation, f3, f4);
    }

    public static /* synthetic */ void b(IntentConicProgressRenderer intentConicProgressRenderer, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        initGradientAlphaAnim$lambda$3(intentConicProgressRenderer, cOUIDynamicAnimation, z2, f3, f4);
    }

    /* JADX WARN: Code duplicated, block: B:26:0x0080  */
    private final void drawConicStyleProgress(Canvas canvas, SeekBarState state, COUISeekBarCore seekBar) {
        float f3;
        float f4;
        float heightBottomDeformedDownValue;
        float heightTopDeformedUpValue;
        float heightTopDeformedUpValue2;
        IOrientationHandler mOrientationHandler = seekBar.getMOrientationHandler();
        if (mOrientationHandler == null) {
            return;
        }
        mOrientationHandler.calculateProgressRect(this.tempProgressRect, state);
        float seekBarLength = mOrientationHandler.getSeekBarLength(seekBar);
        float progressStart = mOrientationHandler.getProgressStart(seekBar);
        float centerCoordinate = mOrientationHandler.getCenterCoordinate(seekBar);
        float f5 = 2;
        float progressRectHeight = seekBar.getProgressRectHeight(state) / f5;
        float widthDeformedValue = progressRectHeight - (state.getIsSupportDeformation() ? state.getWidthDeformedValue() : 0.0f);
        float f6 = centerCoordinate - widthDeformedValue;
        float f7 = centerCoordinate + widthDeformedValue;
        float fCoerceIn = RangesKt.coerceIn(seekBar.getRealScale(state.getDrawProgressScale()), 0.0f, 1.0f);
        float f8 = seekBarLength * fCoerceIn;
        if (f8 <= 0.0f) {
            return;
        }
        boolean z2 = this.intentSeekBar.getMPlaybackState() == PlaybackState.PLAYING;
        boolean zIsDraggingState = this.intentSeekBar.isDraggingState();
        if (z2) {
            if (fCoerceIn <= 0.2f) {
                f3 = f8;
            } else {
                f4 = 5.0f;
                f3 = seekBarLength / f4;
            }
        } else if (!zIsDraggingState) {
            f3 = 0.0f;
        } else if (fCoerceIn <= 0.1f) {
            f3 = f8;
        } else {
            f4 = 10.0f;
            f3 = seekBarLength / f4;
        }
        boolean z3 = f3 > 0.0f;
        if (z3 != this.mWasGradientVisible) {
            this.mWasGradientVisible = z3;
            startGradientAlphaAnim(this.mGradientAlpha, z3 ? 1.0f : 0.0f);
        }
        if (f3 > 0.0f) {
            this.mLastGradientLenB = f3;
        }
        if (f3 <= 0.0f) {
            f3 = this.mLastGradientLenB;
        }
        float heightBottomDeformedDownValue2 = state.getIsRtl() ? (progressStart + seekBarLength) - f8 : progressStart + f8;
        float progressHeight = state.getExtendProgressRectForRoundCorner() ? state.getProgressHeight() / f5 : 0.0f;
        if (state.getIsRtl()) {
            heightTopDeformedUpValue = progressStart + seekBarLength;
            heightBottomDeformedDownValue = heightBottomDeformedDownValue2;
        } else {
            heightBottomDeformedDownValue = heightBottomDeformedDownValue2 - f8;
            heightTopDeformedUpValue = heightBottomDeformedDownValue2;
        }
        if (progressHeight > 0.0f) {
            heightBottomDeformedDownValue -= progressHeight;
            heightTopDeformedUpValue += progressHeight;
        }
        if (state.getIsSupportDeformation()) {
            if (state.getIsRtl()) {
                heightBottomDeformedDownValue = (heightBottomDeformedDownValue - state.getHeightTopDeformedUpValue()) + state.getHeightBottomDeformedDownValue();
                heightTopDeformedUpValue = (heightTopDeformedUpValue - state.getHeightBottomDeformedUpValue()) + state.getHeightBottomDeformedDownValue();
            } else {
                heightBottomDeformedDownValue = (heightBottomDeformedDownValue - state.getHeightBottomDeformedDownValue()) + state.getHeightBottomDeformedUpValue();
                heightTopDeformedUpValue = (heightTopDeformedUpValue + state.getHeightTopDeformedUpValue()) - state.getHeightBottomDeformedDownValue();
            }
        }
        this.rectA.set(heightBottomDeformedDownValue, f6, heightTopDeformedUpValue, f7);
        this.pathA.reset();
        Path path = this.pathA;
        RectF rectF = this.rectA;
        Path.Direction direction = Path.Direction.CW;
        path.addRoundRect(rectF, progressRectHeight, progressRectHeight, direction);
        canvas.drawPath(this.pathA, this.paintA);
        if (f3 <= 0.0f || this.mGradientAlpha <= 0.0f) {
            return;
        }
        if (state.getIsRtl()) {
            heightTopDeformedUpValue2 = f3 + heightBottomDeformedDownValue2;
        } else {
            float f9 = heightBottomDeformedDownValue2;
            heightBottomDeformedDownValue2 -= f3;
            heightTopDeformedUpValue2 = f9;
        }
        if (progressHeight > 0.0f) {
            heightBottomDeformedDownValue2 -= progressHeight;
            heightTopDeformedUpValue2 += progressHeight;
        }
        if (state.getIsSupportDeformation()) {
            if (state.getIsRtl()) {
                heightBottomDeformedDownValue2 = (heightBottomDeformedDownValue2 - state.getHeightTopDeformedUpValue()) + state.getHeightBottomDeformedDownValue();
                heightTopDeformedUpValue2 = (heightTopDeformedUpValue2 - state.getHeightBottomDeformedUpValue()) + state.getHeightBottomDeformedDownValue();
            } else {
                heightBottomDeformedDownValue2 = (heightBottomDeformedDownValue2 - state.getHeightBottomDeformedDownValue()) + state.getHeightBottomDeformedUpValue();
                heightTopDeformedUpValue2 = (heightTopDeformedUpValue2 + state.getHeightTopDeformedUpValue()) - state.getHeightBottomDeformedDownValue();
            }
        }
        this.rectB.set(heightBottomDeformedDownValue2, f6, heightTopDeformedUpValue2, f7);
        this.pathB.reset();
        this.pathB.addRoundRect(this.rectB, progressRectHeight, progressRectHeight, direction);
        RectF rectF2 = this.rectB;
        float f10 = rectF2.right - rectF2.left;
        if (this.mCachedBGradient == null || f10 != this.mLastBGradientLen || state.getIsRtl() != this.mLastBGradientRtl) {
            this.mCachedBGradient = new LinearGradient(0.0f, 0.0f, f10, 0.0f, state.getIsRtl() ? this.colorBEnd : this.colorBStart, state.getIsRtl() ? this.colorBStart : this.colorBEnd, Shader.TileMode.CLAMP);
            this.mLastBGradientLen = f10;
            this.mLastBGradientRtl = state.getIsRtl();
        }
        this.mBShaderMatrix.setTranslate(this.rectB.left, 0.0f);
        LinearGradient linearGradient = this.mCachedBGradient;
        Intrinsics.checkNotNull(linearGradient);
        linearGradient.setLocalMatrix(this.mBShaderMatrix);
        this.paintB.setShader(this.mCachedBGradient);
        this.paintB.setAlpha((int) (this.mGradientAlpha * 255));
        canvas.drawPath(this.pathB, this.paintB);
        this.paintB.setShader(null);
        this.paintB.setAlpha(255);
    }

    private final void initGradientAlphaAnim() {
        if (this.mGradientAlphaAnim != null) {
            return;
        }
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setResponse(0.5f);
        cOUISpringForce.setBounce(0.0f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mGradientAlphaAnim = spring;
        if (spring != null) {
            spring.addUpdateListener(new a(this));
        }
        COUISpringAnimation cOUISpringAnimation = this.mGradientAlphaAnim;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.addEndListener(new b(this));
        }
    }

    private static final void initGradientAlphaAnim$lambda$2(IntentConicProgressRenderer this$0, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        float fCoerceIn = RangesKt.coerceIn(f3 / 10000.0f, 0.0f, 1.0f);
        float f5 = this$0.mGradientAlphaFrom;
        this$0.mGradientAlpha = RangesKt.coerceIn(f5 + ((this$0.mGradientAlphaTo - f5) * fCoerceIn), 0.0f, 1.0f);
        this$0.intentSeekBar.invalidate();
    }

    private static final void initGradientAlphaAnim$lambda$3(IntentConicProgressRenderer this$0, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (z2) {
            return;
        }
        this$0.mGradientAlpha = this$0.mGradientAlphaTo;
        this$0.intentSeekBar.invalidate();
    }

    private final void startGradientAlphaAnim(float from, float to) {
        initGradientAlphaAnim();
        COUISpringAnimation cOUISpringAnimation = this.mGradientAlphaAnim;
        if (cOUISpringAnimation == null) {
            return;
        }
        if (cOUISpringAnimation.isRunning()) {
            cOUISpringAnimation.cancel();
        }
        this.mGradientAlphaFrom = from;
        this.mGradientAlphaTo = to;
        cOUISpringAnimation.setStartValue(0.0f);
        cOUISpringAnimation.animateToFinalPosition(10000.0f);
    }

    @Override // com.coui.appcompat.seekbar.renderer.StandardProgressRenderer, com.coui.appcompat.seekbar.renderer.IProgressRenderer
    public void drawProgress(Canvas canvas, SeekBarState state) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        Intrinsics.checkNotNullParameter(state, "state");
        if (state.getShowProgress()) {
            COUISeekBarCore view = state.getView();
            if (view == null) {
                view = null;
            }
            if (view == null) {
                return;
            }
            if (!(view instanceof COUIIntentSeekBar) || !this.intentSeekBar.isConicLightProgressStyle()) {
                super.drawProgress(canvas, state);
            } else if (view.isVertical()) {
                super.drawProgress(canvas, state);
            } else {
                drawConicStyleProgress(canvas, state, view);
            }
        }
    }

    /* JADX INFO: renamed from: getProgressColorA, reason: from getter */
    public final int getColorA() {
        return this.colorA;
    }

    public final void release() {
        COUISpringAnimation cOUISpringAnimation = this.mGradientAlphaAnim;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.cancel();
        }
        this.mGradientAlphaAnim = null;
        this.mCachedBGradient = null;
        this.mLastBGradientRtl = false;
    }

    public final void setProgressColorA(int color) {
        if (this.colorA == color) {
            return;
        }
        this.colorA = color;
        this.paintA.setColor(color);
    }
}