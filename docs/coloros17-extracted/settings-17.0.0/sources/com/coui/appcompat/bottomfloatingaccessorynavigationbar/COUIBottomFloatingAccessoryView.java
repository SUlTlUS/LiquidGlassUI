package com.coui.appcompat.bottomfloatingaccessorynavigationbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.COUIMaterialBlurEffect;
import com.coui.appcompat.COUIMaterialEffect;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.COUIMaterialStrokeEffect;
import com.coui.appcompat.OnMaterialStateChangeListener;
import com.coui.appcompat.animation.IExpansionViewTouchMotionAnimHost;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.animation.touchanimation.COUIIComposeMotionUnit;
import com.coui.appcompat.animation.touchanimation.COUITouchMotionAnimator;
import com.coui.appcompat.animation.touchanimation.OnAnimationEndListener;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.spotlight.COUISpotLightEffect;
import com.coui.appcompat.spotlight.COUISpotLightEffectDrawable;
import com.coui.appcompat.uiutil.ShadowUtils;
import com.coui.appcompat.uiutil.UIUtil;
import com.oplus.view.material.OplusMaterialCornerParams;
import com.support.appcompat.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIBottomFloatingAccessoryView extends View implements OnMaterialStateChangeListener, IExpansionViewTouchMotionAnimHost {
    private static final float COLOR_CHANNEL_MAX = 255.0f;
    private static final float DEFAULT_SPRING_ANIMATION_FACTOR = 10000.0f;
    private static final int DRAWABLE_ALPHA_OPAQUE = 255;
    private static final float ICON_ALPHA_OPAQUE = 1.0f;
    private static final float ICON_ALPHA_TRANSPARENT = 0.0f;
    private static final float ICON_BLANK_WEIGHT_THRESHOLD = 0.05f;
    private static final float ICON_INCOMING_ALPHA_SPRING_BOUNCE = 0.0f;
    private static final float ICON_INCOMING_ALPHA_SPRING_RESPONSE = 0.2f;
    private static final float ICON_LIGHT_INCOMING_ALPHA_SPRING_BOUNCE = 0.0f;
    private static final float ICON_LIGHT_INCOMING_ALPHA_SPRING_RESPONSE = 0.15f;
    private static final float ICON_LIGHT_OUTGOING_ALPHA_SPRING_BOUNCE = 0.0f;
    private static final float ICON_LIGHT_OUTGOING_ALPHA_SPRING_RESPONSE = 0.1f;
    private static final float ICON_OUTGOING_ALPHA_SPRING_BOUNCE = 0.0f;
    private static final float ICON_OUTGOING_ALPHA_SPRING_RESPONSE = 0.15f;
    private static final float ICON_SCALE_FULL = 1.0f;
    private static final float ICON_SCALE_GONE = 0.0f;
    private static final float ICON_SCALE_SPRING_BOUNCE = 0.4f;
    private static final float ICON_SCALE_SPRING_RESPONSE = 0.5f;
    private static final boolean SHADER_TOUCH_FEEDBACK_ENABLED = false;
    private static final float SMOOTH_CORNER_FACTOR = 2.0f;
    private static final float TOUCH_EFFECT_ALPHA_OPAQUE = 255.0f;
    private static final float TOUCH_EFFECT_ALPHA_SPRING_BOUNCE = 0.0f;
    private static final float TOUCH_EFFECT_ALPHA_SPRING_RESPONSE = 0.2f;
    private static final int TOUCH_EFFECT_CENTER_ALPHA = 637534208;
    private static final int TOUCH_EFFECT_LIGHT_COLOR = -1;
    private final int mAccessorySize;
    private boolean mApplyingTouchMotionTranslation;
    private final Path mBackgroundPath;
    private int mCustomFallbackBackgroundColor;
    private EffectCapabilityHost mEffectCapabilityHost;
    private ShapeDrawable mFallbackBackground;
    private float mIconAlpha;
    private Drawable mIconDrawable;
    private boolean mIconReplaceAnimsInitialized;
    private float mIconScale;
    private COUISpringAnimation mIncomingIconAlphaAnim;
    private COUISpringAnimation mIncomingIconScaleAnim;
    private boolean mIsTouchMotionAnimDrive;
    private OplusMaterialCornerParams mOplusMaterialCornerParams;
    private float mOriginTranslationX;
    private float mOutgoingIconAlpha;
    private COUISpringAnimation mOutgoingIconAlphaAnim;
    private Drawable mOutgoingIconDrawable;
    private float mOutgoingIconScale;
    private COUISpringAnimation mOutgoingIconScaleAnim;
    private float mRadius;
    private COUISpotLightEffectDrawable mSpotLightEffectDrawable;
    private COUISpringAnimation mTouchEffectAlphaAnim;
    private Matrix mTouchEffectMatrix;
    private Paint mTouchEffectPaint;
    private final float mTouchEffectRadius;
    private Shader mTouchEffectShader;
    private COUITouchMotionAnimator mTouchMotionAnimator;
    private float mTouchX;
    private float mTouchY;
    private ColorDrawable mTransparentBackgroundDrawable;

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingaccessorynavigationbar.COUIBottomFloatingAccessoryView$1, reason: invalid class name */
    public class AnonymousClass1 extends ViewOutlineProvider {
        public AnonymousClass1() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, view.getWidth(), view.getHeight());
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingaccessorynavigationbar.COUIBottomFloatingAccessoryView$2, reason: invalid class name */
    public class AnonymousClass2 implements OnAnimationEndListener {
        public AnonymousClass2() {
        }

        @Override // com.coui.appcompat.animation.touchanimation.OnAnimationEndListener
        public void onAnimationEnd(COUIIComposeMotionUnit cOUIIComposeMotionUnit, boolean z2, float f3, float f4) {
            COUIBottomFloatingAccessoryView.access$000(COUIBottomFloatingAccessoryView.this);
        }
    }

    public interface EffectCapabilityHost {
        boolean isFullMotionEffect();
    }

    public COUIBottomFloatingAccessoryView(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void a(COUIBottomFloatingAccessoryView cOUIBottomFloatingAccessoryView, COUIIComposeMotionUnit.ContributeSink contributeSink) {
        cOUIBottomFloatingAccessoryView.mApplyingTouchMotionTranslation = true;
        try {
            cOUIBottomFloatingAccessoryView.setTranslationX(cOUIBottomFloatingAccessoryView.mOriginTranslationX + contributeSink.getTranslateX());
            cOUIBottomFloatingAccessoryView.setTranslationY(contributeSink.getTranslateY());
            if (cOUIBottomFloatingAccessoryView.mIsTouchMotionAnimDrive) {
                cOUIBottomFloatingAccessoryView.setScaleX(contributeSink.getScaleX());
                cOUIBottomFloatingAccessoryView.setScaleY(contributeSink.getScaleY());
            }
        } finally {
            cOUIBottomFloatingAccessoryView.mApplyingTouchMotionTranslation = false;
        }
    }

    public static /* synthetic */ void access$000(COUIBottomFloatingAccessoryView cOUIBottomFloatingAccessoryView) {
        cOUIBottomFloatingAccessoryView.resetTouchMotionTranslationX();
    }

    private void applyDrawableAlpha(Drawable drawable, float f3) {
        if (drawable == null) {
            return;
        }
        int iRound = Math.round(f3 * 255.0f);
        if (iRound < 0) {
            iRound = 0;
        } else if (iRound > 255) {
            iRound = 255;
        }
        drawable.setAlpha(iRound);
    }

    private void applyFallbackBackgroundColor() {
        if (this.mFallbackBackground == null || isMaterialQualityEnable()) {
            return;
        }
        int attrColor = this.mCustomFallbackBackgroundColor;
        if (attrColor == 0) {
            attrColor = COUIContextUtil.getAttrColor(getContext(), R.attr.couiColorBar);
        }
        this.mFallbackBackground.getPaint().setColor(attrColor);
        invalidate();
    }

    private void applyIconDrawableInstant(Drawable drawable) {
        cancelAnimIfRunning(this.mIncomingIconScaleAnim);
        cancelAnimIfRunning(this.mIncomingIconAlphaAnim);
        cancelAnimIfRunning(this.mOutgoingIconScaleAnim);
        cancelAnimIfRunning(this.mOutgoingIconAlphaAnim);
        Drawable drawable2 = this.mOutgoingIconDrawable;
        if (drawable2 != null && drawable2 != this.mIconDrawable) {
            unbindIconDrawable(drawable2);
        }
        this.mOutgoingIconDrawable = null;
        this.mOutgoingIconAlpha = 0.0f;
        this.mOutgoingIconScale = 0.0f;
        Drawable drawable3 = this.mIconDrawable;
        if (drawable3 != drawable) {
            unbindIconDrawable(drawable3);
            this.mIconDrawable = drawable;
            bindIconDrawable(drawable);
        }
        this.mIconAlpha = 1.0f;
        this.mIconScale = 1.0f;
        applyDrawableAlpha(this.mIconDrawable, 1.0f);
        invalidate();
    }

    private void applyIconReplaceSpringParams(boolean z2) {
        if (!z2) {
            applySpringBounceResponse(this.mIncomingIconAlphaAnim, 0.0f, 0.15f);
            applySpringBounceResponse(this.mOutgoingIconAlphaAnim, 0.0f, 0.1f);
        } else {
            applySpringBounceResponse(this.mIncomingIconScaleAnim, 0.4f, 0.5f);
            applySpringBounceResponse(this.mOutgoingIconScaleAnim, 0.4f, 0.5f);
            applySpringBounceResponse(this.mIncomingIconAlphaAnim, 0.0f, 0.2f);
            applySpringBounceResponse(this.mOutgoingIconAlphaAnim, 0.0f, 0.15f);
        }
    }

    private void applySmoothCornerOutlineState() {
        if (!isMaterialQualityEnable()) {
            setClipToOutline(true);
            this.mOplusMaterialCornerParams = null;
            return;
        }
        setClipToOutline(false);
        OplusMaterialCornerParams oplusMaterialCornerParams = this.mOplusMaterialCornerParams;
        if (oplusMaterialCornerParams == null) {
            this.mOplusMaterialCornerParams = new OplusMaterialCornerParams(this.mRadius, 2.0f);
        } else {
            oplusMaterialCornerParams.setRadius(this.mRadius);
        }
        COUIMaterialEffect.setCornerParams(this, this.mOplusMaterialCornerParams);
    }

    private void applySpotLightColor(COUISpotLightEffectDrawable cOUISpotLightEffectDrawable, int i3) {
        cOUISpotLightEffectDrawable.setLightColor(Color.red(i3) / 255.0f, Color.green(i3) / 255.0f, Color.blue(i3) / 255.0f);
    }

    private void applySpringBounceResponse(COUISpringAnimation cOUISpringAnimation, float f3, float f4) {
        if (cOUISpringAnimation == null || cOUISpringAnimation.getSpring() == null) {
            return;
        }
        cOUISpringAnimation.getSpring().setBounce(f3);
        cOUISpringAnimation.getSpring().setResponse(f4);
    }

    private void applyTouchEffect(MotionEvent motionEvent) {
        if (isSpotLightEffectEnable()) {
            handleTouchEffectDrawableEvent(motionEvent);
        }
    }

    public static /* synthetic */ void b(COUIBottomFloatingAccessoryView cOUIBottomFloatingAccessoryView, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        float f5 = f3 / 10000.0f;
        cOUIBottomFloatingAccessoryView.mIconAlpha = f5;
        cOUIBottomFloatingAccessoryView.applyDrawableAlpha(cOUIBottomFloatingAccessoryView.mIconDrawable, f5);
        cOUIBottomFloatingAccessoryView.invalidate();
    }

    private void bindIconDrawable(Drawable drawable) {
        if (drawable == null) {
            return;
        }
        drawable.mutate();
        drawable.setCallback(this);
        drawable.setState(getDrawableState());
        drawable.jumpToCurrentState();
    }

    public static /* synthetic */ void c(COUIBottomFloatingAccessoryView cOUIBottomFloatingAccessoryView, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        float f5 = f3 / 10000.0f;
        cOUIBottomFloatingAccessoryView.mOutgoingIconAlpha = f5;
        cOUIBottomFloatingAccessoryView.applyDrawableAlpha(cOUIBottomFloatingAccessoryView.mOutgoingIconDrawable, f5);
        cOUIBottomFloatingAccessoryView.invalidate();
    }

    private void cancelAnimIfRunning(COUISpringAnimation cOUISpringAnimation) {
        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) {
            return;
        }
        cOUISpringAnimation.cancel();
    }

    private COUISpringAnimation createIconSpringAnim(COUIDynamicAnimation.OnAnimationUpdateListener onAnimationUpdateListener) {
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(new COUISpringForce());
        spring.addUpdateListener(onAnimationUpdateListener);
        return spring;
    }

    private RadialGradient createTouchEffectRadialGradient(int i3) {
        int i4 = i3 & 16777215;
        return new RadialGradient(0.0f, 0.0f, this.mTouchEffectRadius, i4 | TOUCH_EFFECT_CENTER_ALPHA, i4, Shader.TileMode.CLAMP);
    }

    public static /* synthetic */ void d(COUIBottomFloatingAccessoryView cOUIBottomFloatingAccessoryView, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        cOUIBottomFloatingAccessoryView.mIconScale = f3 / 10000.0f;
        cOUIBottomFloatingAccessoryView.invalidate();
    }

    private void drawIcon(Canvas canvas) {
        drawIconLayer(canvas, this.mOutgoingIconDrawable, this.mOutgoingIconScale);
        drawIconLayer(canvas, this.mIconDrawable, this.mIconScale);
    }

    private void drawIconLayer(Canvas canvas, Drawable drawable, float f3) {
        if (drawable == null || f3 <= 0.0f) {
            return;
        }
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
            Rect bounds = drawable.getBounds();
            int iWidth = bounds.width();
            intrinsicHeight = bounds.height();
            intrinsicWidth = iWidth;
        }
        if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
            return;
        }
        int width = (getWidth() - intrinsicWidth) / 2;
        int height = (getHeight() - intrinsicHeight) / 2;
        drawable.setBounds(width, height, intrinsicWidth + width, intrinsicHeight + height);
        int iSave = canvas.save();
        canvas.scale(f3, f3, getWidth() / 2.0f, getHeight() / 2.0f);
        drawable.draw(canvas);
        canvas.restoreToCount(iSave);
    }

    private void drawTouchEffectDrawable(Canvas canvas) {
        if (!isSpotLightEffectEnable() || this.mSpotLightEffectDrawable == null) {
            return;
        }
        this.mBackgroundPath.reset();
        this.mBackgroundPath.addCircle(getMeasuredWidth() / 2.0f, getMeasuredHeight() / 2.0f, getMeasuredHeight() / 2.0f, Path.Direction.CCW);
        int iSave = canvas.save();
        canvas.clipPath(this.mBackgroundPath);
        this.mSpotLightEffectDrawable.draw(canvas);
        canvas.restoreToCount(iSave);
    }

    private void drawTouchEffectShader(Canvas canvas) {
        isSpotLightEffectEnable();
    }

    public static /* synthetic */ void e(COUIBottomFloatingAccessoryView cOUIBottomFloatingAccessoryView, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        Paint paint = cOUIBottomFloatingAccessoryView.mTouchEffectPaint;
        if (paint != null) {
            paint.setAlpha((int) f3);
            cOUIBottomFloatingAccessoryView.invalidate();
        }
    }

    public static /* synthetic */ void f(COUIBottomFloatingAccessoryView cOUIBottomFloatingAccessoryView, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        cOUIBottomFloatingAccessoryView.tryFinishOutgoingIcon();
    }

    public static /* synthetic */ void g(COUIBottomFloatingAccessoryView cOUIBottomFloatingAccessoryView, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        cOUIBottomFloatingAccessoryView.mOutgoingIconScale = f3 / 10000.0f;
        cOUIBottomFloatingAccessoryView.invalidate();
    }

    private void handleTouchEffectDrawableEvent(MotionEvent motionEvent) {
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLightEffectDrawable;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.onTouchEvent(motionEvent);
        }
    }

    private void handleTouchEffectShaderEvent(MotionEvent motionEvent) {
        if (this.mTouchEffectPaint == null || this.mTouchEffectAlphaAnim == null) {
            return;
        }
        int action = motionEvent.getAction();
        float x2 = motionEvent.getX();
        float y2 = motionEvent.getY();
        boolean z2 = x2 >= 0.0f && x2 <= ((float) getWidth()) && y2 >= 0.0f && y2 <= ((float) getHeight());
        if (action == 0) {
            if (z2) {
                this.mTouchEffectAlphaAnim.setStartValue(this.mTouchEffectPaint.getAlpha());
                this.mTouchEffectAlphaAnim.animateToFinalPosition(255.0f);
                updateTouchPosition(x2, y2);
                return;
            }
            return;
        }
        if (action == 2) {
            updateTouchPosition(x2, y2);
        } else if (action == 1 || action == 3) {
            isSpotLightEffectEnable();
        }
    }

    private float iconVisualWeight(float f3, float f4, boolean z2) {
        float fMax = Math.max(f3, 0.0f);
        return !z2 ? fMax : fMax * Math.max(f4, 0.0f);
    }

    private void initIconReplaceAnims() {
        if (this.mIconReplaceAnimsInitialized) {
            return;
        }
        this.mIncomingIconScaleAnim = createIconSpringAnim(new n(this));
        this.mIncomingIconAlphaAnim = createIconSpringAnim(new o(this));
        this.mOutgoingIconScaleAnim = createIconSpringAnim(new p(this));
        this.mOutgoingIconAlphaAnim = createIconSpringAnim(new q(this));
        r rVar = new r(this);
        COUISpringAnimation cOUISpringAnimation = this.mOutgoingIconScaleAnim;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.addEndListener(rVar);
        }
        COUISpringAnimation cOUISpringAnimation2 = this.mOutgoingIconAlphaAnim;
        if (cOUISpringAnimation2 != null) {
            cOUISpringAnimation2.addEndListener(rVar);
        }
        this.mIconReplaceAnimsInitialized = true;
    }

    private void initMaterialAppearance() {
        applySmoothCornerOutlineState();
        updateOuterShadow();
        updateStrokeEffect();
        updateBlurEffect();
        updateTouchEffect();
    }

    private void initTouchEffectAlphaAnim() {
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(0.0f);
        cOUISpringForce.setResponse(0.2f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mTouchEffectAlphaAnim = spring;
        spring.addUpdateListener(new m(this));
    }

    private void initTouchEffectDrawable() {
        if (this.mSpotLightEffectDrawable != null) {
            return;
        }
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = new COUISpotLightEffectDrawable();
        this.mSpotLightEffectDrawable = cOUISpotLightEffectDrawable;
        cOUISpotLightEffectDrawable.setSpotLightType(getContext(), COUISpotLightEffect.SpotLightType.TYPE_BOTTOM_NAVIGATION);
        applySpotLightColor(this.mSpotLightEffectDrawable, resolveTouchEffectLightColor());
    }

    private void initTouchEffectShader() {
        if (this.mTouchEffectPaint != null) {
            return;
        }
        this.mTouchEffectPaint = new Paint(1);
        this.mTouchEffectMatrix = new Matrix();
        RadialGradient radialGradientCreateTouchEffectRadialGradient = createTouchEffectRadialGradient(resolveTouchEffectLightColor());
        this.mTouchEffectShader = radialGradientCreateTouchEffectRadialGradient;
        this.mTouchEffectPaint.setShader(radialGradientCreateTouchEffectRadialGradient);
        this.mTouchEffectPaint.setAlpha(0);
    }

    private void initTouchMotionAnim() {
        COUITouchMotionAnimator cOUITouchMotionAnimator = new COUITouchMotionAnimator(getContext());
        this.mTouchMotionAnimator = cOUITouchMotionAnimator;
        cOUITouchMotionAnimator.setDeformationEnable(true).setRubberEnable(true).setPressScaleEnable(true).addOnAnimationUpdateCallback(new s(this));
        this.mTouchMotionAnimator.addEndListener(new AnonymousClass2());
    }

    private boolean isFullIconReplaceEffect() {
        EffectCapabilityHost effectCapabilityHost = this.mEffectCapabilityHost;
        return effectCapabilityHost != null && effectCapabilityHost.isFullMotionEffect();
    }

    private boolean isMaterialQualityEnable() {
        return COUIMaterialStateManager.INSTANCE.isBlurEffectEnable();
    }

    private boolean isSpotLightEffectEnable() {
        return COUIMaterialStateManager.INSTANCE.isSpotLightEffectEnable();
    }

    private boolean isSpotLightPressEffectEnabled() {
        return isSpotLightEffectEnable();
    }

    private void keepOutgoingDiscardIncoming(boolean z2) {
        Drawable drawable = this.mIconDrawable;
        if (drawable != null && drawable != this.mOutgoingIconDrawable) {
            unbindIconDrawable(drawable);
        }
        if (z2) {
            this.mOutgoingIconScale = Math.max(this.mOutgoingIconScale, 0.0f);
        }
        this.mOutgoingIconAlpha = Math.max(this.mOutgoingIconAlpha, 0.0f);
    }

    private boolean prepareOutgoingLayerForInterruptReplace(boolean z2, Drawable drawable) {
        selectVisibleLayerAsOutgoing(z2);
        if ((this.mOutgoingIconDrawable == null ? 0.0f : iconVisualWeight(this.mOutgoingIconAlpha, this.mOutgoingIconScale, z2)) >= ICON_BLANK_WEIGHT_THRESHOLD) {
            return true;
        }
        applyIconDrawableInstant(drawable);
        return false;
    }

    private void promoteIncomingToOutgoing(boolean z2) {
        Drawable drawable = this.mOutgoingIconDrawable;
        if (drawable != null && drawable != this.mIconDrawable) {
            unbindIconDrawable(drawable);
        }
        this.mOutgoingIconDrawable = this.mIconDrawable;
        this.mOutgoingIconAlpha = Math.max(this.mIconAlpha, 0.0f);
        this.mOutgoingIconScale = z2 ? Math.max(this.mIconScale, 0.0f) : 1.0f;
    }

    private void refreshSpotLightPressEffect() {
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLightEffectDrawable;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setEnabled(isSpotLightPressEffectEnabled());
        }
    }

    private void resetTouchMotionTranslationX() {
        this.mApplyingTouchMotionTranslation = true;
        try {
            setTranslationX(this.mOriginTranslationX);
        } finally {
            this.mApplyingTouchMotionTranslation = false;
        }
    }

    private int resolveTouchEffectLightColor() {
        return -1;
    }

    private void selectVisibleLayerAsOutgoing(boolean z2) {
        float fIconVisualWeight = iconVisualWeight(this.mIconAlpha, this.mIconScale, z2);
        float fIconVisualWeight2 = this.mOutgoingIconDrawable == null ? 0.0f : iconVisualWeight(this.mOutgoingIconAlpha, this.mOutgoingIconScale, z2);
        if (this.mOutgoingIconDrawable == null || fIconVisualWeight2 < fIconVisualWeight) {
            promoteIncomingToOutgoing(z2);
        } else {
            keepOutgoingDiscardIncoming(z2);
        }
    }

    private void setCausticShadowEffect() {
        ShadowUtils.setElevationToView(this, 7);
        COUIMaterialEffect.setOutlineCausticShadowColor(this, getResources().getColor(com.coui.appcompat.effect.R.color.coui_caustic_color_2));
        COUIMaterialEffect.setOutlineCausticShadowLayout(this, getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_l_level5), getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_t_level5), getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_r_level5), getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_b_level5));
        COUIMaterialEffect.setOutlineCausticShadowParams(this, getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_sdf_level5));
    }

    private void startIconReplaceAnimation(Drawable drawable) {
        COUISpringAnimation cOUISpringAnimation;
        COUISpringAnimation cOUISpringAnimation2;
        initIconReplaceAnims();
        if (drawable == this.mIconDrawable) {
            applyIconDrawableInstant(drawable);
            return;
        }
        boolean zIsFullIconReplaceEffect = isFullIconReplaceEffect();
        if (prepareOutgoingLayerForInterruptReplace(zIsFullIconReplaceEffect, drawable)) {
            this.mIconDrawable = drawable;
            bindIconDrawable(drawable);
            this.mIconAlpha = 0.0f;
            this.mIconScale = zIsFullIconReplaceEffect ? 0.0f : 1.0f;
            applyDrawableAlpha(this.mIconDrawable, 0.0f);
            applyIconReplaceSpringParams(zIsFullIconReplaceEffect);
            if (!zIsFullIconReplaceEffect) {
                cancelAnimIfRunning(this.mIncomingIconScaleAnim);
                cancelAnimIfRunning(this.mOutgoingIconScaleAnim);
            }
            if (this.mOutgoingIconDrawable != null) {
                if (zIsFullIconReplaceEffect && (cOUISpringAnimation2 = this.mOutgoingIconScaleAnim) != null) {
                    cOUISpringAnimation2.setStartValue(this.mOutgoingIconScale * 10000.0f);
                    this.mOutgoingIconScaleAnim.animateToFinalPosition(0.0f);
                }
                COUISpringAnimation cOUISpringAnimation3 = this.mOutgoingIconAlphaAnim;
                if (cOUISpringAnimation3 != null) {
                    cOUISpringAnimation3.setStartValue(this.mOutgoingIconAlpha * 10000.0f);
                    this.mOutgoingIconAlphaAnim.animateToFinalPosition(0.0f);
                }
            }
            if (zIsFullIconReplaceEffect && (cOUISpringAnimation = this.mIncomingIconScaleAnim) != null) {
                cOUISpringAnimation.setStartValue(this.mIconScale * 10000.0f);
                this.mIncomingIconScaleAnim.animateToFinalPosition(10000.0f);
            }
            COUISpringAnimation cOUISpringAnimation4 = this.mIncomingIconAlphaAnim;
            if (cOUISpringAnimation4 != null) {
                cOUISpringAnimation4.setStartValue(this.mIconAlpha * 10000.0f);
                this.mIncomingIconAlphaAnim.animateToFinalPosition(10000.0f);
            }
            invalidate();
        }
    }

    private void tryFinishOutgoingIcon() {
        if (this.mOutgoingIconDrawable == null) {
            return;
        }
        COUISpringAnimation cOUISpringAnimation = this.mOutgoingIconScaleAnim;
        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) {
            COUISpringAnimation cOUISpringAnimation2 = this.mOutgoingIconAlphaAnim;
            if (cOUISpringAnimation2 == null || !cOUISpringAnimation2.isRunning()) {
                unbindIconDrawable(this.mOutgoingIconDrawable);
                this.mOutgoingIconDrawable = null;
                this.mOutgoingIconAlpha = 0.0f;
                this.mOutgoingIconScale = 0.0f;
                invalidate();
            }
        }
    }

    private void unbindIconDrawable(Drawable drawable) {
        if (drawable == null) {
            return;
        }
        drawable.setCallback(null);
        unscheduleDrawable(drawable);
    }

    private void updateBlurEffect() {
        UIUtil.setViewShadowClippingEnabled(this, isMaterialQualityEnable());
        if (isMaterialQualityEnable()) {
            COUIMaterialEffect.setViewBlurEffect(this, COUIMaterialBlurEffect.BlurEffectType.TYPE_FRAMEWORK_BOTTOM_BAR);
            if (this.mTransparentBackgroundDrawable == null) {
                this.mTransparentBackgroundDrawable = new ColorDrawable(0);
            }
            setBackground(this.mTransparentBackgroundDrawable);
            return;
        }
        if (COUIMaterialStateManager.INSTANCE.materialVersionCheck()) {
            COUIMaterialEffect.setViewBlurEffect(this, COUIMaterialBlurEffect.BlurEffectType.TYPE_NO_EFFECT);
        }
        if (this.mFallbackBackground == null) {
            this.mFallbackBackground = new ShapeDrawable(new OvalShape());
        }
        setBackground(this.mFallbackBackground);
        applyFallbackBackgroundColor();
    }

    private void updateMaterialCornerRadius() {
        if (this.mOplusMaterialCornerParams == null || !isMaterialQualityEnable()) {
            return;
        }
        this.mOplusMaterialCornerParams.setRadius(this.mRadius);
        COUIMaterialEffect.setCornerParams(this, this.mOplusMaterialCornerParams);
    }

    private void updateOuterShadow() {
        if (COUIMaterialStateManager.INSTANCE.isCausticShadowEffectEnable()) {
            setCausticShadowEffect();
        } else {
            ShadowUtils.setElevationToView(this, 2, getContext().getResources().getDimensionPixelOffset(R.dimen.support_shadow_size_level_three), COUIContextUtil.getColor(getContext(), com.support.bottomnavigation.R.color.coui_bottom_floating_navigation_bar_menu_shadow_color));
            COUIMaterialEffect.clearCausticShadow(this);
        }
        setOutlineProvider(new AnonymousClass1());
    }

    private void updateStrokeEffect() {
        COUIMaterialStateManager cOUIMaterialStateManager = COUIMaterialStateManager.INSTANCE;
        if (cOUIMaterialStateManager.materialVersionCheck()) {
            if (cOUIMaterialStateManager.isStrokeEffectEnable()) {
                COUIMaterialEffect.setViewStrokeEffect(this, COUIMaterialStrokeEffect.StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_3);
            } else {
                COUIMaterialEffect.setViewStrokeEffect(this, COUIMaterialStrokeEffect.StrokeEffectType.TYPE_NO_EFFECT);
            }
        }
    }

    private void updateTouchEffect() {
        isSpotLightEffectEnable();
        initTouchEffectDrawable();
        refreshSpotLightPressEffect();
    }

    private void updateTouchPosition(float f3, float f4) {
        this.mTouchX = f3;
        this.mTouchY = f4;
        invalidate();
    }

    @Override // android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        COUITouchMotionAnimator cOUITouchMotionAnimator = this.mTouchMotionAnimator;
        if (cOUITouchMotionAnimator != null) {
            cOUITouchMotionAnimator.handleTouchEvent(motionEvent);
        }
        applyTouchEffect(motionEvent);
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        Drawable drawable = this.mIconDrawable;
        boolean z2 = drawable != null && drawable.setState(drawableState);
        Drawable drawable2 = this.mOutgoingIconDrawable;
        if ((drawable2 == null || !drawable2.setState(drawableState)) ? z2 : true) {
            invalidate();
        }
    }

    public Drawable getIconDrawable() {
        return this.mIconDrawable;
    }

    @Override // android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        COUIMaterialStateManager.INSTANCE.addOnMaterialStateChangedListener(this);
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLightEffectDrawable;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setCallback(this);
        }
        Drawable drawable = this.mIconDrawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
    }

    @Override // android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        COUIMaterialStateManager.INSTANCE.removeOnMaterialStateChangedListener(this);
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLightEffectDrawable;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setCallback(null);
            this.mSpotLightEffectDrawable.cancel();
        }
        cancelAnimIfRunning(this.mIncomingIconScaleAnim);
        cancelAnimIfRunning(this.mIncomingIconAlphaAnim);
        cancelAnimIfRunning(this.mOutgoingIconScaleAnim);
        cancelAnimIfRunning(this.mOutgoingIconAlphaAnim);
        Drawable drawable = this.mOutgoingIconDrawable;
        if (drawable != null) {
            unbindIconDrawable(drawable);
            this.mOutgoingIconDrawable = null;
            this.mOutgoingIconAlpha = 0.0f;
            this.mOutgoingIconScale = 0.0f;
        }
        this.mIconAlpha = 1.0f;
        this.mIconScale = 1.0f;
        applyDrawableAlpha(this.mIconDrawable, 1.0f);
        unbindIconDrawable(this.mIconDrawable);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTouchEffectDrawable(canvas);
        drawTouchEffectShader(canvas);
        drawIcon(canvas);
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public CharSequence onDump() {
        return toString();
    }

    @Override // android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        super.onLayout(z2, i3, i4, i5, i6);
        this.mRadius = ((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom()) / 2.0f;
        updateMaterialCornerRadius();
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public void onMaterialStateChange(int i3, boolean z2) {
        if (i3 == 1) {
            updateStrokeEffect();
            return;
        }
        if (i3 == 2) {
            updateBlurEffect();
            applySmoothCornerOutlineState();
        } else if (i3 == 4) {
            updateTouchEffect();
        } else if (i3 == 8) {
            updateOuterShadow();
        }
    }

    @Override // android.view.View
    public void onMeasure(int i3, int i4) {
        int i5 = this.mAccessorySize;
        setMeasuredDimension(i5, i5);
    }

    @Override // android.view.View
    public void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        COUITouchMotionAnimator cOUITouchMotionAnimator = this.mTouchMotionAnimator;
        if (cOUITouchMotionAnimator != null) {
            cOUITouchMotionAnimator.onSizeChange(i3, i4);
        }
        ShapeDrawable shapeDrawable = this.mFallbackBackground;
        if (shapeDrawable != null && i3 > 0 && i4 > 0) {
            shapeDrawable.setBounds(0, 0, i3, i4);
        }
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLightEffectDrawable;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setBounds(0, 0, i3, i4);
        }
    }

    public void setAccessoryBackgroundColor(int i3) {
        this.mCustomFallbackBackgroundColor = i3;
        applyFallbackBackgroundColor();
    }

    public void setEffectCapabilityHost(EffectCapabilityHost effectCapabilityHost) {
        this.mEffectCapabilityHost = effectCapabilityHost;
    }

    @Override // com.coui.appcompat.animation.IExpansionViewTouchMotionAnimHost
    public void setExpansionViewTouchMotionAnimEnabled(boolean z2) {
        COUITouchMotionAnimator cOUITouchMotionAnimator = this.mTouchMotionAnimator;
        if (cOUITouchMotionAnimator == null) {
            return;
        }
        this.mIsTouchMotionAnimDrive = z2;
        if (z2) {
            cOUITouchMotionAnimator.enable();
        } else {
            cOUITouchMotionAnimator.disable();
            this.mTouchMotionAnimator.reset(true);
        }
    }

    @Deprecated
    public void setIconDrawable(Drawable drawable) {
        setIconDrawable(drawable, false);
    }

    @Override // android.view.View
    public void setTranslationX(float f3) {
        super.setTranslationX(f3);
        if (this.mApplyingTouchMotionTranslation) {
            return;
        }
        this.mOriginTranslationX = f3;
    }

    @Override // android.view.View
    public boolean verifyDrawable(Drawable drawable) {
        return drawable == this.mIconDrawable || drawable == this.mOutgoingIconDrawable || super.verifyDrawable(drawable);
    }

    public COUIBottomFloatingAccessoryView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public void setIconDrawable(Drawable drawable, boolean z2) {
        if (drawable == this.mIconDrawable && this.mOutgoingIconDrawable == null) {
            return;
        }
        if (!z2 || drawable == null) {
            applyIconDrawableInstant(drawable);
        } else {
            startIconReplaceAnimation(drawable);
        }
    }

    public COUIBottomFloatingAccessoryView(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.mIconAlpha = 1.0f;
        this.mIconScale = 1.0f;
        this.mOutgoingIconAlpha = 0.0f;
        this.mOutgoingIconScale = 0.0f;
        this.mBackgroundPath = new Path();
        this.mIsTouchMotionAnimDrive = true;
        this.mAccessorySize = getResources().getDimensionPixelSize(com.support.bottomnavigation.R.dimen.coui_bottom_floating_navigation_bar_height);
        this.mTouchEffectRadius = getResources().getDimensionPixelSize(com.support.bottomnavigation.R.dimen.coui_bottom_floating_navigation_bar_touch_effect_radius);
        setClickable(true);
        setFocusable(true);
        setWillNotDraw(false);
        initMaterialAppearance();
        initTouchMotionAnim();
        initTouchEffectAlphaAnim();
    }
}