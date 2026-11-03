package com.coui.appcompat.seekbar.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.View;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.seekbar.core.SeekBarState;
import com.oplus.drs.statistics.comm.provider.ProviderIpcClient;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import n0.c;
import n0.d;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0012\u0018\u0000 =2\u00020\u0001:\u0001=B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010\"\u001a\u00020#J\u0018\u0010$\u001a\u00020#2\u0006\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020\bH\u0002J\u0018\u0010(\u001a\u00020#2\u0006\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020\bH\u0002J\u0018\u0010)\u001a\u00020#2\u0006\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020\bH\u0002J\u0018\u0010*\u001a\u00020#2\u0006\u0010%\u001a\u00020&2\u0006\u0010+\u001a\u00020,H\u0002J\u0018\u0010-\u001a\u00020#2\u0006\u0010%\u001a\u00020&2\u0006\u0010+\u001a\u00020,H\u0016J\b\u0010.\u001a\u00020#H\u0002J\b\u0010/\u001a\u00020#H\u0002J\u0010\u00100\u001a\u00020#2\u0006\u0010+\u001a\u00020,H\u0002J\u001a\u00101\u001a\u0004\u0018\u00010\u00112\u0006\u00102\u001a\u00020\b2\u0006\u0010'\u001a\u00020\bH\u0002J\u0006\u00103\u001a\u00020\u0006J\u0006\u00104\u001a\u00020#J\u0006\u00105\u001a\u00020#J\u000e\u00106\u001a\u00020#2\u0006\u00107\u001a\u00020\u0006J \u00108\u001a\u00020#2\u0006\u00109\u001a\u00020\b2\u0006\u0010:\u001a\u00020\b2\u0006\u0010;\u001a\u00020\bH\u0002J\u0010\u0010<\u001a\u00020#2\u0006\u0010:\u001a\u00020\bH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u001cX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u001eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010!\u001a\u0004\u0018\u00010\u001cX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006>"}, d2 = {"Lcom/coui/appcompat/seekbar/renderer/TexturedThumbRenderer;", "Lcom/coui/appcompat/seekbar/renderer/StandardThumbRenderer;", "hostView", "Landroid/view/View;", "(Landroid/view/View;)V", "mIsEnabled", "", "mLayerAGradientHeight", "", "mLayerAPaint", "Landroid/graphics/Paint;", "mLayerAShader", "Landroid/graphics/LinearGradient;", "mLayerAShaderMatrix", "Landroid/graphics/Matrix;", "mLayerBBitmapPaint", "mLayerBCacheBitmap", "Landroid/graphics/Bitmap;", "mLayerBCacheDiameter", "mLayerBClipPath", "Landroid/graphics/Path;", "mLayerBDstRect", "Landroid/graphics/RectF;", "mLayerBFillPaint", "mLayerBShadowPaint", "mLayerCAlpha", "mLayerCPaint", "mLayerCSpringAnim", "Lcom/coui/appcompat/animation/dynamicanimation/COUISpringAnimation;", "mLayerCSpringForce", "Lcom/coui/appcompat/animation/dynamicanimation/COUISpringForce;", "mTextureAlpha", "mTextureRect", "mTextureSpringAnim", "cancelAnimations", "", "drawLayerA", "canvas", "Landroid/graphics/Canvas;", "r", "drawLayerB", "drawLayerC", "drawTexturedThumb", "state", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "drawThumb", "ensureLayerCSpringAnim", "ensureTextureSpringAnim", "fillTextureRect", "getOrCreateLayerBBitmap", "diameter", "isEnabled", "onPressDown", "onPressUp", "setEnabled", "enabled", "startLayerCAlphaAnim", "fromAlpha", "targetAlpha", ProviderIpcClient.BUNDLE_KEY_RESPONSE, "startTextureAlphaAnim", "Companion", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class TexturedThumbRenderer extends StandardThumbRenderer {
    private static final int COLOR_CHANNEL_MAX = 255;
    private static final int LAYER_A_ALPHA_BOTTOM = 140;
    private static final int LAYER_A_ALPHA_TOP = 230;
    private static final float LAYER_B_SHADOW_DX = 0.0f;
    private static final float LAYER_B_SHADOW_DY = 0.4f;
    private static final float LAYER_B_SHADOW_RADIUS = 5.0f;
    private static final float LAYER_B_SHADOW_STROKE_WIDTH = 10.0f;
    private static final float LAYER_C_SPRING_RESPONSE_IN = 0.5f;
    private static final float LAYER_C_SPRING_RESPONSE_OUT = 0.3f;
    private static final float TEXTURE_SPRING_BOUNCE = 0.0f;
    private static final float TEXTURE_SPRING_FINAL = 100.0f;
    private static final float TEXTURE_SPRING_RESPONSE = 0.3f;
    private final View hostView;
    private boolean mIsEnabled;
    private float mLayerAGradientHeight;
    private final Paint mLayerAPaint;
    private LinearGradient mLayerAShader;
    private final Matrix mLayerAShaderMatrix;
    private final Paint mLayerBBitmapPaint;
    private Bitmap mLayerBCacheBitmap;
    private float mLayerBCacheDiameter;
    private final Path mLayerBClipPath;
    private final RectF mLayerBDstRect;
    private final Paint mLayerBFillPaint;
    private final Paint mLayerBShadowPaint;
    private float mLayerCAlpha;
    private final Paint mLayerCPaint;
    private COUISpringAnimation mLayerCSpringAnim;
    private COUISpringForce mLayerCSpringForce;
    private float mTextureAlpha;
    private final RectF mTextureRect;
    private COUISpringAnimation mTextureSpringAnim;

    public TexturedThumbRenderer(View hostView) {
        Intrinsics.checkNotNullParameter(hostView, "hostView");
        this.hostView = hostView;
        this.mTextureRect = new RectF();
        this.mLayerAPaint = new Paint(1);
        this.mLayerAShaderMatrix = new Matrix();
        Paint paint = new Paint(1);
        paint.setColor(Color.parseColor("#77FFFFFF"));
        this.mLayerBFillPaint = paint;
        Paint paint2 = new Paint(1);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth(10.0f);
        paint2.setColor(0);
        paint2.setShadowLayer(LAYER_B_SHADOW_RADIUS, 0.0f, 0.4f, -1);
        this.mLayerBShadowPaint = paint2;
        this.mLayerBClipPath = new Path();
        this.mLayerBBitmapPaint = new Paint(3);
        this.mLayerBCacheDiameter = -1.0f;
        this.mLayerBDstRect = new RectF();
        Paint paint3 = new Paint(1);
        paint3.setColor(-1);
        this.mLayerCPaint = paint3;
    }

    public static /* synthetic */ void a(TexturedThumbRenderer texturedThumbRenderer, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        ensureTextureSpringAnim$lambda$4(texturedThumbRenderer, cOUIDynamicAnimation, f3, f4);
    }

    public static /* synthetic */ void b(TexturedThumbRenderer texturedThumbRenderer, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        ensureLayerCSpringAnim$lambda$6(texturedThumbRenderer, cOUIDynamicAnimation, f3, f4);
    }

    private final void drawLayerA(Canvas canvas, float r2) {
        float fHeight = this.mTextureRect.height();
        if (fHeight <= 0.0f) {
            return;
        }
        if (fHeight != this.mLayerAGradientHeight || this.mLayerAShader == null) {
            this.mLayerAGradientHeight = fHeight;
            this.mLayerAShader = new LinearGradient(0.0f, fHeight, 0.0f, 0.0f, new int[]{Color.argb(230, 255, 255, 255), Color.argb(230, 255, 255, 255), Color.argb(LAYER_A_ALPHA_BOTTOM, 255, 255, 255)}, new float[]{0.0f, 0.2f, 1.0f}, Shader.TileMode.CLAMP);
        }
        LinearGradient linearGradient = this.mLayerAShader;
        if (linearGradient == null) {
            return;
        }
        Matrix matrix = this.mLayerAShaderMatrix;
        RectF rectF = this.mTextureRect;
        matrix.setTranslate(rectF.left, rectF.top);
        linearGradient.setLocalMatrix(this.mLayerAShaderMatrix);
        this.mLayerAPaint.setShader(linearGradient);
        canvas.drawRoundRect(this.mTextureRect, r2, r2, this.mLayerAPaint);
    }

    private final void drawLayerB(Canvas canvas, float r2) {
        Bitmap orCreateLayerBBitmap = getOrCreateLayerBBitmap(this.mTextureRect.width(), r2);
        if (orCreateLayerBBitmap == null) {
            return;
        }
        RectF rectF = this.mLayerBDstRect;
        RectF rectF2 = this.mTextureRect;
        rectF.set(rectF2.left - 12.0f, rectF2.top - 12.0f, rectF2.right + 12.0f, rectF2.bottom + 12.0f);
        canvas.drawBitmap(orCreateLayerBBitmap, (Rect) null, this.mLayerBDstRect, this.mLayerBBitmapPaint);
    }

    private final void drawLayerC(Canvas canvas, float r2) {
        int iCoerceIn = RangesKt.coerceIn((int) (this.mLayerCAlpha * 255), 0, 255);
        if (iCoerceIn <= 0) {
            return;
        }
        this.mLayerCPaint.setAlpha(iCoerceIn);
        canvas.drawRoundRect(this.mTextureRect, r2, r2, this.mLayerCPaint);
    }

    private final void drawTexturedThumb(Canvas canvas, SeekBarState state) {
        Canvas canvas2;
        fillTextureRect(state);
        float curThumbRadius = state.getCurThumbRadius();
        int iCoerceIn = RangesKt.coerceIn((int) (this.mTextureAlpha * 255), 0, 255);
        if (iCoerceIn > 0) {
            RectF rectF = this.mTextureRect;
            canvas2 = canvas;
            canvas2.saveLayerAlpha(rectF.left, rectF.top, rectF.right, rectF.bottom, iCoerceIn);
            drawLayerA(canvas2, curThumbRadius);
            drawLayerB(canvas2, curThumbRadius);
            canvas2.restore();
        } else {
            canvas2 = canvas;
        }
        drawLayerC(canvas2, curThumbRadius);
    }

    private final void ensureLayerCSpringAnim() {
        if (this.mLayerCSpringAnim != null) {
            return;
        }
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(0.0f);
        this.mLayerCSpringForce = cOUISpringForce;
        COUISpringAnimation spring = new COUISpringAnimation(new FloatValueHolder(0.0f)).setSpring(cOUISpringForce);
        this.mLayerCSpringAnim = spring;
        if (spring != null) {
            spring.addUpdateListener(new c(this));
        }
    }

    private static final void ensureLayerCSpringAnim$lambda$6(TexturedThumbRenderer this$0, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.mLayerCAlpha = RangesKt.coerceIn(f3 / 100.0f, 0.0f, 1.0f);
        this$0.hostView.invalidate();
    }

    private final void ensureTextureSpringAnim() {
        if (this.mTextureSpringAnim != null) {
            return;
        }
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setResponse(0.3f);
        cOUISpringForce.setBounce(0.0f);
        COUISpringAnimation spring = new COUISpringAnimation(new FloatValueHolder(0.0f)).setSpring(cOUISpringForce);
        this.mTextureSpringAnim = spring;
        if (spring != null) {
            spring.addUpdateListener(new d(this));
        }
    }

    private static final void ensureTextureSpringAnim$lambda$4(TexturedThumbRenderer this$0, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.mTextureAlpha = RangesKt.coerceIn(f3 / 100.0f, 0.0f, 1.0f);
        this$0.hostView.invalidate();
    }

    private final void fillTextureRect(SeekBarState state) {
        float thumbPosition = state.getThumbPosition();
        float curThumbRadius = state.getCurThumbRadius();
        if (state.getIsVertical()) {
            this.mTextureRect.set(state.getCenterX() - curThumbRadius, thumbPosition - curThumbRadius, state.getCenterX() + curThumbRadius, thumbPosition + curThumbRadius);
        } else {
            this.mTextureRect.set(thumbPosition - curThumbRadius, state.getCenterY() - curThumbRadius, thumbPosition + curThumbRadius, state.getCenterY() + curThumbRadius);
        }
    }

    private final Bitmap getOrCreateLayerBBitmap(float diameter, float r2) {
        if (diameter <= 0.0f) {
            return null;
        }
        Bitmap bitmap = this.mLayerBCacheBitmap;
        if (bitmap != null) {
            return bitmap;
        }
        int iCeil = (int) Math.ceil((2 * 12.0f) + diameter);
        if (iCeil <= 0) {
            return null;
        }
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(iCeil, iCeil, Bitmap.Config.ARGB_8888);
        Intrinsics.checkNotNullExpressionValue(bitmapCreateBitmap, "createBitmap(...)");
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        float f3 = diameter + 12.0f;
        RectF rectF = new RectF(12.0f, 12.0f, f3, f3);
        canvas.drawRoundRect(rectF, r2, r2, this.mLayerBFillPaint);
        this.mLayerBClipPath.reset();
        this.mLayerBClipPath.addOval(rectF, Path.Direction.CCW);
        canvas.save();
        canvas.clipPath(this.mLayerBClipPath);
        canvas.drawOval(rectF, this.mLayerBShadowPaint);
        canvas.restore();
        this.mLayerBCacheBitmap = bitmapCreateBitmap;
        this.mLayerBCacheDiameter = diameter;
        return bitmapCreateBitmap;
    }

    private final void startLayerCAlphaAnim(float fromAlpha, float targetAlpha, float response) {
        ensureLayerCSpringAnim();
        COUISpringAnimation cOUISpringAnimation = this.mLayerCSpringAnim;
        if (cOUISpringAnimation == null) {
            return;
        }
        if (cOUISpringAnimation.isRunning()) {
            cOUISpringAnimation.cancel();
        }
        COUISpringForce cOUISpringForce = this.mLayerCSpringForce;
        if (cOUISpringForce != null) {
            cOUISpringForce.setResponse(response);
        }
        cOUISpringAnimation.setStartValue(fromAlpha * 100.0f);
        cOUISpringAnimation.animateToFinalPosition(targetAlpha * 100.0f);
    }

    private final void startTextureAlphaAnim(float targetAlpha) {
        ensureTextureSpringAnim();
        COUISpringAnimation cOUISpringAnimation = this.mTextureSpringAnim;
        if (cOUISpringAnimation == null) {
            return;
        }
        if (cOUISpringAnimation.isRunning()) {
            cOUISpringAnimation.cancel();
        }
        cOUISpringAnimation.setStartValue(this.mTextureAlpha * 100.0f);
        cOUISpringAnimation.animateToFinalPosition(targetAlpha * 100.0f);
    }

    public final void cancelAnimations() {
        COUISpringAnimation cOUISpringAnimation = this.mTextureSpringAnim;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.cancel();
        }
        this.mTextureSpringAnim = null;
        COUISpringAnimation cOUISpringAnimation2 = this.mLayerCSpringAnim;
        if (cOUISpringAnimation2 != null) {
            cOUISpringAnimation2.cancel();
        }
        this.mLayerCSpringAnim = null;
        this.mLayerCSpringForce = null;
        this.mLayerAShader = null;
        Bitmap bitmap = this.mLayerBCacheBitmap;
        if (bitmap != null) {
            bitmap.recycle();
        }
        this.mLayerBCacheBitmap = null;
        this.mLayerBCacheDiameter = -1.0f;
    }

    @Override // com.coui.appcompat.seekbar.renderer.StandardThumbRenderer, com.coui.appcompat.seekbar.renderer.IThumbRenderer
    public void drawThumb(Canvas canvas, SeekBarState state) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        Intrinsics.checkNotNullParameter(state, "state");
        if (state.getShowThumb()) {
            if (!this.mIsEnabled || this.mTextureAlpha <= 0.0f) {
                super.drawThumb(canvas, state);
            } else {
                drawTexturedThumb(canvas, state);
            }
        }
    }

    /* JADX INFO: renamed from: isEnabled, reason: from getter */
    public final boolean getMIsEnabled() {
        return this.mIsEnabled;
    }

    public final void onPressDown() {
        if (this.mIsEnabled) {
            startTextureAlphaAnim(1.0f);
            startLayerCAlphaAnim(1.0f, 0.0f, 0.5f);
        }
    }

    public final void onPressUp() {
        if (this.mIsEnabled) {
            startTextureAlphaAnim(0.0f);
            startLayerCAlphaAnim(this.mLayerCAlpha, 1.0f, 0.3f);
        }
    }

    public final void setEnabled(boolean enabled) {
        if (this.mIsEnabled == enabled) {
            return;
        }
        this.mIsEnabled = enabled;
        if (enabled) {
            return;
        }
        COUISpringAnimation cOUISpringAnimation = this.mTextureSpringAnim;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.cancel();
        }
        COUISpringAnimation cOUISpringAnimation2 = this.mLayerCSpringAnim;
        if (cOUISpringAnimation2 != null) {
            cOUISpringAnimation2.cancel();
        }
        this.mTextureAlpha = 0.0f;
        this.mLayerCAlpha = 0.0f;
        this.hostView.invalidate();
    }
}