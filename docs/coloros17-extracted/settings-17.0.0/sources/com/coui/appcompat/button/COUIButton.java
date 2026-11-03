package com.coui.appcompat.button;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewParent;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.ViewCompat;
import com.coui.appcompat.COUIMaterialBlurEffect;
import com.coui.appcompat.COUIMaterialEffect;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.COUIMaterialStrokeEffect;
import com.coui.appcompat.OnMaterialStateChangeListener;
import com.coui.appcompat.animation.touchanimation.COUIIComposeMotionUnit;
import com.coui.appcompat.animation.touchanimation.COUITouchMotionAnimator;
import com.coui.appcompat.animation.touchanimation.COUIViewTouchAnimator;
import com.coui.appcompat.button.listener.OnSizeChangeListener;
import com.coui.appcompat.button.listener.OnTextChangeListener;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.effect.COUIMaterialThemeBlurEffect;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.roundRect.COUIShapePath;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.coui.appcompat.shadowedge.COUIShadowEdgeDrawable;
import com.coui.appcompat.shadowedge.IShadowEdgeEffect;
import com.coui.appcompat.spotlight.COUISpotLightEffect;
import com.coui.appcompat.spotlight.COUISpotLightEffectDrawable;
import com.coui.appcompat.state.COUIMaskEffectDrawable;
import com.coui.appcompat.state.COUIMaskRippleDrawable;
import com.coui.appcompat.state.COUIStateEffectDrawable;
import com.coui.appcompat.state.COUIStrokeDrawable;
import com.coui.appcompat.textutil.COUIChangeTextUtil;
import com.coui.appcompat.uiutil.ShadowUtils;
import com.coui.appcompat.uiutil.UIUtil;
import com.coui.appcompat.version.COUIVersionUtil;
import com.decouple.injector.adaptor.BaseConfig;
import com.oplus.graphics.OplusOutlineAdapter;
import com.oplus.view.material.OplusMaterialCornerParams;
import com.support.appcompat.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIButton extends AppCompatButton implements OnMaterialStateChangeListener {
    private static final int ALERT_DIALOG_CORNER_TYPE = 2;
    private static final float ALERT_DIALOG_WEIGHT = 2.0f;
    private static final int ALPHA_COLOR_153 = 153;
    private static final int ALPHA_COLOR_166 = 166;
    private static final int ALPHA_COLOR_204 = 204;
    private static final int ALPHA_COLOR_220 = 220;
    private static final int ALPHA_COLOR_25 = 25;
    private static final int ALPHA_COLOR_30 = 30;
    private static final int ALPHA_COLOR_51 = 51;
    private static final int ALPHA_MAX = 255;
    public static final int BORDERLESS_BUTTON_ANIM = 0;
    private static final int CAUSTIC_SHADOW_COLOR_ALPHA = 153;
    private static final float CIRCLE_CORNER_WEIGHT = 2.0f;
    public static final int COMMON_ROUND = 1;
    private static final float DEFAULT_BRIGHTNESS_MAX_VALUE = 0.8f;
    public static final float DEFAULT_RADIUS = -1.0f;
    public static final int DIALOG_BORDERLESS_BUTTON_ANIM = 2;
    public static final int FILL_BUTTON_ANIM = 1;
    private static final float HALF_HEIGHT = 2.0f;
    public static final int RADIUS_HALF_HEIGHT = -1;
    private static final float SHADOW_EDGE_CORNER_WEIGHT = 1.0f;
    public static final int SMOOTH_ROUND = 0;
    private static String TAG = "COUIButton";
    private boolean isLimitHeight;
    private boolean mAnimEnable;
    private int mAnimType;
    private final Path mBackgroundPath;
    private boolean mBlurEffectEnabled;
    private int mButtonHeight;
    private int mButtonMaxHeight;
    private int mButtonMaxWidth;
    private int mButtonType;
    private int mButtonWidth;
    private boolean mCausticShadowEffectEnabled;
    private float[] mColorHsl;
    private OplusMaterialCornerParams mCornerParams;
    private float mCurrentBrightness;
    private float mCurrentScale;
    private String mDescText;
    private int mDisabledColor;
    private int mDrawableColor;
    private final Paint mFillPaint;
    private float mFocusedStrokeRadius;
    private boolean mHasSecondaryElevatedColor;
    private boolean mIsDescType;
    private boolean mIsDrawMaterialEffect;
    private boolean mIsNeedVibrate;
    private int mLowLevelDrawableColor;
    private COUIMaskEffectDrawable mMaskDrawable;
    private boolean mMaterialEffectEnabled;
    private float mMaxBrightness;
    private int mMaxSingleLargeWidth;
    private boolean mNeedLimitMaxWidth;
    private OnSizeChangeListener mOnSizeChangeListener;
    private OnTextChangeListener mOnTextChangeListener;
    private Rect mOplusOutLineRect;
    private OplusOutlineAdapter mOplusOutline;
    private final PopupGradientBackgroundHelper.GradientParams mPopupGradientParams;
    private int mPressColor;
    private float mRadius;
    private float mRadiusOffset;
    private COUIMaskRippleDrawable mRippleBackgroundDrawable;
    private int mRoundType;
    private boolean mScaleEnable;
    private int mSecondaryElevatedColor;
    private COUIShadowEdgeDrawable mShadowEdgeDrawable;
    private boolean mShadowEdgeEffectEnabled;
    private int mShadowLevel;
    private COUISpotLightEffectDrawable mSpotLightDrawable;
    private boolean mSpotLightEffectEnabled;
    private COUIStateEffectDrawable mStateEffectBackground;
    private int mStrokeColor;
    private COUIStrokeDrawable mStrokeDrawable;
    private float mStrokeWidth;
    private int mStyle;
    private String mText;
    private Rect mTmpRect;
    private RectF mTmpRectF;
    private final COUITouchMotionAnimator mTouchMotionAnimator;
    private int mUseMaterialEffectAttr;
    private COUIViewTouchAnimator mViewTouchAnimator;
    private float mWeight;
    private static final StrokeEffectCustomParams PRIMARY_LIGHT_ENABLED = new StrokeEffectCustomParams(0.2f, 3.0f, 0.0f, 0.4f, 0.5f, 1.0f);
    private static final StrokeEffectCustomParams PRIMARY_DARK_ENABLED = new StrokeEffectCustomParams(0.6f, 3.0f, 0.0f, 0.5f, 0.5f, 1.0f);
    private static final StrokeEffectCustomParams PRIMARY_DISABLED = new StrokeEffectCustomParams(0.2f, 3.0f, 0.0f, 0.2f, 0.2f, 1.0f);
    private static final StrokeEffectCustomParams SECONDARY_LIGHT = new StrokeEffectCustomParams(1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    private static final StrokeEffectCustomParams SECONDARY_DARK = new StrokeEffectCustomParams(1.0f, 0.3f, 0.0f, 0.2f, 1.0f, 1.0f);
    private static final StrokeEffectCustomParams TRANSPARENT_LIGHT = new StrokeEffectCustomParams(0.3f, 0.5f, 0.0f, 0.5f, 1.0f, 1.0f);
    private static final StrokeEffectCustomParams TRANSPARENT_DARK = new StrokeEffectCustomParams(0.3f, 0.5f, 0.0f, 0.5f, 1.0f, 1.0f);

    /* JADX INFO: renamed from: com.coui.appcompat.button.COUIButton$1, reason: invalid class name */
    public class AnonymousClass1 extends ViewOutlineProvider {
        public AnonymousClass1() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            COUIButton.access$002(COUIButton.this, new OplusOutlineAdapter(outline, 1));
            COUIButton.access$100(COUIButton.this).left = (int) COUIButton.access$200(COUIButton.this).left;
            COUIButton.access$100(COUIButton.this).top = (int) COUIButton.access$200(COUIButton.this).top;
            COUIButton.access$100(COUIButton.this).right = (int) COUIButton.access$200(COUIButton.this).right;
            COUIButton.access$100(COUIButton.this).bottom = (int) COUIButton.access$200(COUIButton.this).bottom;
            if (COUIVersionUtil.getOSVersionCode() <= 37) {
                COUIButton.access$000(COUIButton.this).setSmoothRoundRect(COUIButton.access$100(COUIButton.this), COUIButton.this.getDrawableRadius());
            } else if (COUIButton.access$300(COUIButton.this) > 0.0f) {
                COUIButton.access$000(COUIButton.this).setSmoothRoundRect(COUIButton.access$100(COUIButton.this), RoundCornerUtil.getRoundCornerForOS17(COUIButton.this.getContext(), COUIButton.access$300(COUIButton.this)), 3.0f);
            } else {
                COUIButton.access$000(COUIButton.this).setSmoothRoundRect(COUIButton.access$100(COUIButton.this), COUIButton.this.getDrawableRadius(), 1.0f);
            }
        }
    }

    public enum ButtonType {
        PRIMARY(1),
        SECONDARY(2),
        TRANSPARENT(3),
        AI_PAYMENT(4),
        AI_PAYMENT_SECONDARY(5),
        UNKNOWN(-1);

        private final int mValue;

        ButtonType(int i3) {
            this.mValue = i3;
        }

        public static ButtonType fromInt(int i3) {
            for (ButtonType buttonType : values()) {
                if (buttonType.mValue == i3) {
                    return buttonType;
                }
            }
            return UNKNOWN;
        }

        public int getValue() {
            return this.mValue;
        }
    }

    public enum MaterialEffectState {
        FORCE_ENABLE(1),
        FORCE_DISABLE(0),
        FOLLOW_SYSTEM(-1);

        private final int value;

        MaterialEffectState(int i3) {
            this.value = i3;
        }

        public int getValue() {
            return this.value;
        }
    }

    public COUIButton(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void a(COUIButton cOUIButton, CharSequence charSequence, TextView.BufferType bufferType) {
        cOUIButton.getClass();
        super.setText(cOUIButton.getDescTypeText(charSequence.toString()), bufferType);
    }

    public static /* synthetic */ OplusOutlineAdapter access$000(COUIButton cOUIButton) {
        return cOUIButton.mOplusOutline;
    }

    public static /* synthetic */ OplusOutlineAdapter access$002(COUIButton cOUIButton, OplusOutlineAdapter oplusOutlineAdapter) {
        cOUIButton.mOplusOutline = oplusOutlineAdapter;
        return oplusOutlineAdapter;
    }

    public static /* synthetic */ Rect access$100(COUIButton cOUIButton) {
        return cOUIButton.mOplusOutLineRect;
    }

    public static /* synthetic */ RectF access$200(COUIButton cOUIButton) {
        return cOUIButton.mTmpRectF;
    }

    public static /* synthetic */ float access$300(COUIButton cOUIButton) {
        return cOUIButton.mRadius;
    }

    private void adapterDisableColor(TypedArray typedArray) {
        if (getButtonTypeFromStyle() == ButtonType.AI_PAYMENT) {
            this.mDisabledColor = shouldUsePopupGradientRadial() ? COUIContextUtil.getColor(getContext(), R.color.coui_transparence) : COUIContextUtil.getAttrColor(getContext(), R.attr.couiColorAiPaymentPrimaryDisabled);
            return;
        }
        if (getButtonTypeFromStyle() == ButtonType.AI_PAYMENT_SECONDARY) {
            this.mDisabledColor = COUIContextUtil.getAttrColor(getContext(), R.attr.couiColorAiPaymentSecondaryDisabled);
            return;
        }
        if (typedArray == null) {
            return;
        }
        Context context = getContext();
        int i3 = R.attr.couiColorDisable;
        int attrId = COUIContextUtil.getAttrId(context, i3, 0);
        int i4 = R.styleable.COUIButton_disabledColor;
        int resourceId = typedArray.getResourceId(i4, 0);
        if (attrId == 0 || attrId != resourceId) {
            this.mDisabledColor = typedArray.getColor(i4, 0);
        } else {
            this.mDisabledColor = COUIContextUtil.getAttrColor(getContext(), i3);
        }
    }

    private void applyBlurEffect() {
        COUIMaskEffectDrawable cOUIMaskEffectDrawable = this.mMaskDrawable;
        if (cOUIMaskEffectDrawable != null) {
            cOUIMaskEffectDrawable.enablePressedState(!isBlurEffectEnabled());
        }
        if (!COUIMaterialEffect.isMaterialVersion() || this.mStyle == 0) {
            return;
        }
        ButtonType buttonTypeFromStyle = getButtonTypeFromStyle();
        if (this.mIsDrawMaterialEffect) {
            COUIMaterialEffect.setViewBlurEffect(this, COUIMaterialBlurEffect.BlurEffectType.TYPE_NO_EFFECT);
            changeButtonAlphaColor(isBlurEffectEnabled(), buttonTypeFromStyle);
        } else {
            if (buttonTypeFromStyle == ButtonType.UNKNOWN) {
                COUIMaterialEffect.setViewBlurEffect(this, COUIMaterialBlurEffect.BlurEffectType.TYPE_NO_EFFECT);
                return;
            }
            if (!isBlurEffectEnabled()) {
                COUIMaterialEffect.setViewBlurEffect(this, COUIMaterialBlurEffect.BlurEffectType.TYPE_NO_EFFECT);
            } else if (buttonTypeFromStyle == ButtonType.PRIMARY) {
                COUIMaterialThemeBlurEffect.INSTANCE.setViewBlurEffect(this, isEnabled() ? COUIMaterialThemeBlurEffect.ThemeBlurEffectType.TYPE_CONTENT_PRIMARY_BUTTON : COUIMaterialThemeBlurEffect.ThemeBlurEffectType.TYPE_CONTENT_DISABLED_PRIMARY_BUTTON);
            } else if (buttonTypeFromStyle == ButtonType.SECONDARY) {
                COUIMaterialEffect.setViewBlurEffect(this, COUIMaterialBlurEffect.BlurEffectType.TYPE_CONTENT_SECONDARY_BUTTON);
            } else if (buttonTypeFromStyle == ButtonType.TRANSPARENT) {
                COUIMaterialEffect.setViewBlurEffect(this, COUIMaterialBlurEffect.BlurEffectType.TYPE_CONTENT_TRANSPARENT_BUTTON);
            }
        }
        invalidate();
    }

    private void applyCausticShadowEffect() {
        int iArgb;
        if (!isCausticShadowEffectEnabled()) {
            int i3 = this.mShadowLevel;
            if (i3 == 6 || i3 == 7 || i3 == -1) {
                ShadowUtils.clearShadow(this);
            } else {
                ShadowUtils.setElevationToView(this, i3);
            }
            COUIMaterialEffect.clearCausticShadow(this);
            return;
        }
        ShadowUtils.setElevationToView(this, this.mShadowLevel);
        if (getButtonTypeFromStyle() == ButtonType.PRIMARY || getButtonTypeFromStyle() == ButtonType.AI_PAYMENT) {
            int iConvertCausticShadowColor = COUIMaterialEffect.convertCausticShadowColor(COUIContextUtil.getAttrColor(getContext(), R.attr.couiColorContainerTheme));
            iArgb = Color.argb(153, Color.red(iConvertCausticShadowColor), Color.green(iConvertCausticShadowColor), Color.blue(iConvertCausticShadowColor));
        } else {
            iArgb = getResources().getColor(com.coui.appcompat.effect.R.color.coui_caustic_color_1);
        }
        COUIMaterialEffect.setOutlineCausticShadowColor(this, iArgb);
        COUIMaterialEffect.setOutlineCausticShadowLayout(this, getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_l_level3), getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_t_level3), getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_r_level3), getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_b_level3));
        COUIMaterialEffect.setOutlineCausticShadowParams(this, getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_sdf_level3));
    }

    private void applyLightLevelDrawableColor() {
        int lightLevelDrawableColor = getLightLevelDrawableColor(getContext());
        this.mLowLevelDrawableColor = lightLevelDrawableColor;
        if (this.mAnimEnable) {
            this.mDrawableColor = lightLevelDrawableColor;
        }
    }

    private void applySpotlightEffect() {
        ButtonType buttonTypeFromStyle = getButtonTypeFromStyle();
        if (buttonTypeFromStyle == ButtonType.UNKNOWN) {
            return;
        }
        setSpotLightEffectAttrs(buttonTypeFromStyle == ButtonType.PRIMARY || buttonTypeFromStyle == ButtonType.AI_PAYMENT);
    }

    private void applyStrokeEffect() {
        if (!COUIMaterialEffect.isMaterialVersion() || this.mStyle == 0) {
            return;
        }
        updateStrokeCorner(getWidth(), getHeight());
        ButtonType buttonTypeFromStyle = getButtonTypeFromStyle();
        if (this.mIsDrawMaterialEffect) {
            if (isDrawableShadowEdgeEnabled()) {
                initShadowEdgeParamsWithoutBlur(buttonTypeFromStyle, isEnabled());
            } else {
                COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable;
                if (cOUIShadowEdgeDrawable != null) {
                    cOUIShadowEdgeDrawable.setEdgeEnable(false).setShadowEnable(false);
                    this.mShadowEdgeDrawable.setDrawableEnabled(false);
                }
            }
            clearMaterialShadowEdgeCornerAndStroke();
        } else if (buttonTypeFromStyle == ButtonType.UNKNOWN) {
            clearMaterialShadowEdgeCornerAndStroke();
            return;
        } else if (isStrokeEffectEnabled()) {
            COUIMaterialEffect.setViewStrokeEffect(this, getStrokeEffectType(buttonTypeFromStyle));
        } else {
            clearMaterialShadowEdgeCornerAndStroke();
        }
        invalidate();
    }

    public static /* synthetic */ void b(COUIButton cOUIButton, COUIIComposeMotionUnit.ContributeSink contributeSink) {
        cOUIButton.getClass();
        cOUIButton.setTranslationX(contributeSink.getTranslateX());
        cOUIButton.setTranslationY(contributeSink.getTranslateY());
        cOUIButton.setScaleX(contributeSink.getScaleX());
        cOUIButton.setScaleY(contributeSink.getScaleY());
    }

    private void changeButtonAlphaColor(boolean z2, ButtonType buttonType) {
        if (isAiPaymentButton()) {
            applyLightLevelDrawableColor();
            adapterDisableColor(null);
            return;
        }
        boolean zIsCOUIDarkThemeOrNightMode = COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(getContext());
        if (z2) {
            if (buttonType == ButtonType.PRIMARY) {
                int attrColor = COUIContextUtil.getAttrColor(getContext(), R.attr.couiColorContainerTheme, 0);
                setDrawableColor(Color.argb(255, Color.red(attrColor), Color.green(attrColor), Color.blue(attrColor)));
            } else {
                if (buttonType == ButtonType.SECONDARY || buttonType == ButtonType.AI_PAYMENT_SECONDARY) {
                    setDrawableColor(Color.argb(zIsCOUIDarkThemeOrNightMode ? 51 : ALPHA_COLOR_166, Color.red(-1), Color.green(-1), Color.blue(-1)));
                } else if (buttonType == ButtonType.TRANSPARENT) {
                    setDrawableColor(Color.argb(zIsCOUIDarkThemeOrNightMode ? 25 : 51, Color.red(-1), Color.green(-1), Color.blue(-1)));
                }
            }
        } else if (buttonType == ButtonType.PRIMARY) {
            setDrawableColor(COUIContextUtil.getAttrColor(getContext(), R.attr.couiColorContainerTheme, 0));
        } else if (buttonType == ButtonType.SECONDARY) {
            setDrawableColor(getSecondaryCompositeColors(getContext()));
        } else if (buttonType == ButtonType.AI_PAYMENT_SECONDARY) {
            applyLightLevelDrawableColor();
        } else if (buttonType == ButtonType.TRANSPARENT) {
            setDrawableColor(getContext().getResources().getColor(R.color.coui_button_translate_color));
        }
        if (buttonType == ButtonType.AI_PAYMENT_SECONDARY) {
            adapterDisableColor(null);
        }
    }

    private void clearMaterialShadowEdgeCornerAndStroke() {
        if (COUIMaterialEffect.isMaterialVersion() && this.mIsDrawMaterialEffect) {
            COUIMaterialEffect.setCornerParams(this, null);
            COUIMaterialEffect.setViewStrokeEffect(this, COUIMaterialStrokeEffect.StrokeEffectType.TYPE_NO_EFFECT);
        }
    }

    private float dp2px(float f3) {
        return TypedValue.applyDimension(1, f3, getContext().getResources().getDisplayMetrics());
    }

    private void drawBorderLess(Canvas canvas) {
        this.mFillPaint.setColor(isEnabled() ? this.mStrokeColor : this.mDisabledColor);
        this.mFillPaint.setStrokeWidth(this.mStrokeWidth * 2.0f);
        this.mFillPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(this.mBackgroundPath, this.mFillPaint);
    }

    private void drawButtonBackground(Canvas canvas) {
        Canvas canvas2;
        if (this.mAnimEnable) {
            int iSave = canvas.save();
            canvas.translate(getScrollX(), getScrollY());
            this.mFillPaint.setStyle(Paint.Style.FILL);
            this.mFillPaint.setAntiAlias(true);
            canvas.clipPath(this.mBackgroundPath);
            if (isAiPaymentButton() && shouldUsePopupGradientRadial()) {
                canvas2 = canvas;
                PopupGradientBackgroundHelper.drawRadialGradient(canvas2, this.mTmpRectF, this.mFillPaint, getContext(), isEnabled(), this.mPopupGradientParams);
            } else {
                canvas2 = canvas;
                if (this.mAnimType == 1) {
                    this.mFillPaint.setColor(isEnabled() ? this.mDrawableColor : this.mDisabledColor);
                    canvas2.drawRect(this.mTmpRectF, this.mFillPaint);
                } else {
                    this.mFillPaint.setColor(getStrokeButtonAnimatorColor(this.mDrawableColor));
                    canvas2.drawRect(this.mTmpRectF, this.mFillPaint);
                }
            }
            if (this.mAnimType != 1 && !isAiPaymentButton()) {
                drawBorderLess(canvas2);
            }
            canvas2.restoreToCount(iSave);
        }
    }

    private void drawMaskAndStroke(Canvas canvas) {
        int iSave = canvas.save();
        canvas.translate(getScrollX(), getScrollY());
        this.mMaskDrawable.draw(canvas);
        this.mStrokeDrawable.draw(canvas);
        canvas.restoreToCount(iSave);
    }

    private int getAnimatorColor() {
        return !isEnabled() ? this.mDisabledColor : ColorUtils.compositeColors(this.mMaskDrawable.getCompositeMaskColor(), this.mDrawableColor);
    }

    private ButtonType getButtonTypeFromStyle() {
        return ButtonType.fromInt(this.mButtonType);
    }

    private OplusMaterialCornerParams getCornerParams() {
        OplusMaterialCornerParams oplusMaterialCornerParams = this.mCornerParams;
        if (oplusMaterialCornerParams != null) {
            return oplusMaterialCornerParams;
        }
        if (!COUIMaterialEffect.isMaterialVersion()) {
            return null;
        }
        OplusMaterialCornerParams oplusMaterialCornerParams2 = new OplusMaterialCornerParams(0.0f, 0.0f);
        this.mCornerParams = oplusMaterialCornerParams2;
        return oplusMaterialCornerParams2;
    }

    private SpannableString getDescTypeText(String str) {
        DescButtonTextSpan descButtonTextSpan = new DescButtonTextSpan(getContext(), str, this.mDescText, (this.mButtonMaxWidth - getPaddingStart()) - getPaddingRight(), (this.mButtonWidth - getPaddingStart()) - getPaddingRight(), (this.mButtonHeight - getPaddingBottom()) - getPaddingTop(), getCurrentTextColor(), getPaint(), isLayoutRTL());
        SpannableString spannableString = new SpannableString("  ");
        spannableString.setSpan(descButtonTextSpan, spannableString.length() - 1, spannableString.length(), 33);
        return spannableString;
    }

    private int getLightLevelDrawableColor(Context context) {
        int iOrdinal = getButtonTypeFromStyle().ordinal();
        if (iOrdinal == 0) {
            return COUIContextUtil.getAttrColor(context, R.attr.couiColorContainerTheme);
        }
        if (iOrdinal == 1) {
            return getSecondaryCompositeColors(context);
        }
        if (iOrdinal == 2) {
            return COUIContextUtil.getAttrColor(context, R.color.coui_button_translate_color_lightOS);
        }
        if (iOrdinal != 3) {
            if (iOrdinal != 4) {
                return 0;
            }
            return COUIContextUtil.getAttrColor(context, R.attr.couiColorAiPaymentSecondary);
        }
        if (shouldUsePopupGradientRadial()) {
            return 0;
        }
        return COUIContextUtil.getAttrColor(context, R.attr.couiColorAiPaymentPrimary);
    }

    private int getStrokeButtonAnimatorColor(int i3) {
        return 0;
    }

    private COUIMaterialStrokeEffect.StrokeEffectType getStrokeEffectType(ButtonType buttonType) {
        if (buttonType == ButtonType.PRIMARY || buttonType == ButtonType.AI_PAYMENT) {
            return isEnabled() ? COUIMaterialStrokeEffect.StrokeEffectType.TYPE_CONTENT_CAPSULE_1 : COUIMaterialStrokeEffect.StrokeEffectType.TYPE_CONTENT_CAPSULE_DISABLED_1;
        }
        if (buttonType == ButtonType.SECONDARY || buttonType == ButtonType.AI_PAYMENT_SECONDARY) {
            return isEnabled() ? COUIMaterialStrokeEffect.StrokeEffectType.TYPE_CONTENT_CAPSULE_2 : COUIMaterialStrokeEffect.StrokeEffectType.TYPE_CONTENT_CAPSULE_DISABLED_2;
        }
        return isEnabled() ? COUIMaterialStrokeEffect.StrokeEffectType.TYPE_CONTENT_CAPSULE_3 : COUIMaterialStrokeEffect.StrokeEffectType.TYPE_CONTENT_CAPSULE_DISABLED_3;
    }

    private COUIMaterialStrokeEffect.Companion.StrokeEffectParams getStrokeParams(ButtonType buttonType, boolean z2) {
        if (buttonType == ButtonType.PRIMARY || buttonType == ButtonType.AI_PAYMENT) {
            return !z2 ? COUIMaterialEffect.getStrokeParamsByType(getContext(), COUIMaterialStrokeEffect.StrokeEffectType.TYPE_CONTENT_CAPSULE_DISABLED_4) : COUIMaterialEffect.getStrokeParamsByType(getContext(), COUIMaterialStrokeEffect.StrokeEffectType.TYPE_CONTENT_CAPSULE_4);
        }
        if (buttonType == ButtonType.SECONDARY || buttonType == ButtonType.AI_PAYMENT_SECONDARY) {
            return !z2 ? COUIMaterialEffect.getStrokeParamsByType(getContext(), COUIMaterialStrokeEffect.StrokeEffectType.TYPE_CONTENT_CAPSULE_DISABLED_5) : COUIMaterialEffect.getStrokeParamsByType(getContext(), COUIMaterialStrokeEffect.StrokeEffectType.TYPE_CONTENT_CAPSULE_5);
        }
        if (buttonType == ButtonType.TRANSPARENT) {
            return !z2 ? COUIMaterialEffect.getStrokeParamsByType(getContext(), COUIMaterialStrokeEffect.StrokeEffectType.TYPE_CONTENT_CAPSULE_DISABLED_6) : COUIMaterialEffect.getStrokeParamsByType(getContext(), COUIMaterialStrokeEffect.StrokeEffectType.TYPE_CONTENT_CAPSULE_6);
        }
        return null;
    }

    private void initShadowEdgeDrawableIfNeeded() {
        if (COUIMaterialStateManager.INSTANCE.isLowAnimationLevel() || this.mShadowEdgeDrawable != null) {
            return;
        }
        this.mShadowEdgeDrawable = new COUIShadowEdgeDrawable();
    }

    private void initShadowEdgeParamsWithoutBlur(ButtonType buttonType, boolean z2) {
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable;
        COUIMaterialStrokeEffect.Companion.StrokeEffectParams strokeParams = getStrokeParams(buttonType, z2);
        boolean zIsCOUIDarkThemeOrNightMode = COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(getContext());
        if (strokeParams == null || (cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable) == null) {
            return;
        }
        cOUIShadowEdgeDrawable.setDrawableEnabled(true).setEdgeEnable(true).setShadowEnable(true).setEdgeStyle(IShadowEdgeEffect.EdgeStyle.EDGE_STYLE_0).setShadowStyle(IShadowEdgeEffect.ShadowStyle.SHADOW_STYLE_0).setLineAlpha(strokeParams.getEdgeAlpha()).setLineAngle(strokeParams.getEdgeAngle()).setLineWidth(strokeParams.getEdgeWidth(getContext())).setFadeAlpha(strokeParams.getShadowFadeIn(), strokeParams.getShadowFadeOut()).setFadeWidthScale(strokeParams.getShadowFadeScale()).setIsDarkMode(zIsCOUIDarkThemeOrNightMode).setCornerType(2).setWeight(2.0f);
    }

    private void initSpotLightEffects() {
        if (COUIMaterialStateManager.INSTANCE.isLowAnimationLevel() || this.mSpotLightDrawable != null) {
            return;
        }
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = new COUISpotLightEffectDrawable();
        this.mSpotLightDrawable = cOUISpotLightEffectDrawable;
        cOUISpotLightEffectDrawable.setCallback(this);
        this.mSpotLightDrawable.setEnabled(isEnabled());
    }

    private void initStatefulBackground(Context context) {
        this.mFocusedStrokeRadius = context.getResources().getDimension(R.dimen.default_focus_stroke_radius);
        Drawable background = getBackground();
        COUIMaskEffectDrawable cOUIMaskEffectDrawable = new COUIMaskEffectDrawable(context, 0);
        this.mMaskDrawable = cOUIMaskEffectDrawable;
        cOUIMaskEffectDrawable.setMaskPath(this.mBackgroundPath);
        this.mMaskDrawable.setCallback(this);
        COUIStrokeDrawable cOUIStrokeDrawable = new COUIStrokeDrawable(context);
        this.mStrokeDrawable = cOUIStrokeDrawable;
        cOUIStrokeDrawable.setStrokePath(this.mBackgroundPath);
        this.mStrokeDrawable.setCallback(this);
        COUIMaskRippleDrawable cOUIMaskRippleDrawable = new COUIMaskRippleDrawable(context);
        this.mRippleBackgroundDrawable = cOUIMaskRippleDrawable;
        cOUIMaskRippleDrawable.setCustomRippleMask();
        this.mRippleBackgroundDrawable.setMaskPath(this.mBackgroundPath);
        if (background == null) {
            background = new ColorDrawable(0);
        }
        COUIStateEffectDrawable cOUIStateEffectDrawable = new COUIStateEffectDrawable(new Drawable[]{background, this.mRippleBackgroundDrawable});
        this.mStateEffectBackground = cOUIStateEffectDrawable;
        super.setBackground(cOUIStateEffectDrawable);
        this.mStateEffectBackground.disableScaleEffect();
        setAnimType(this.mAnimType);
    }

    private boolean isAiPaymentButton() {
        return getButtonTypeFromStyle() == ButtonType.AI_PAYMENT;
    }

    private boolean isBlurEffectEnabled() {
        if (this.mUseMaterialEffectAttr == MaterialEffectState.FORCE_ENABLE.getValue()) {
            return true;
        }
        return (this.mUseMaterialEffectAttr == MaterialEffectState.FORCE_DISABLE.getValue() || !this.mBlurEffectEnabled || getButtonTypeFromStyle() == ButtonType.UNKNOWN) ? false : true;
    }

    private boolean isBlurWindowButtonType(int i3) {
        return i3 == ButtonType.PRIMARY.getValue() || i3 == ButtonType.SECONDARY.getValue() || i3 == ButtonType.AI_PAYMENT_SECONDARY.getValue();
    }

    private boolean isCausticShadowEffectEnabled() {
        if (this.mUseMaterialEffectAttr == MaterialEffectState.FORCE_ENABLE.getValue()) {
            return COUIMaterialStateManager.INSTANCE.isFeatureEnabled() && isCausticShadowLevel(this.mShadowLevel);
        }
        return this.mUseMaterialEffectAttr != MaterialEffectState.FORCE_DISABLE.getValue() && this.mCausticShadowEffectEnabled && getButtonTypeFromStyle() != ButtonType.UNKNOWN && isCausticShadowLevel(this.mShadowLevel) && COUIMaterialStateManager.INSTANCE.isFeatureEnabled();
    }

    private boolean isCausticShadowLevel(int i3) {
        return i3 == 7 || i3 == 6;
    }

    private boolean isDrawableShadowEdgeEnabled() {
        return this.mIsDrawMaterialEffect && isStrokeEffectEnabled();
    }

    private boolean isLayoutRTL() {
        return ViewCompat.getLayoutDirection(this) == 1;
    }

    private boolean isLowAnimationLevel() {
        return COUIMaterialStateManager.INSTANCE.isLowAnimationLevel();
    }

    private boolean isOs16() {
        return RoundCornerUtil.getSmoothStyleType() == 1;
    }

    private boolean isSpotlightEffectEnabled() {
        if (this.mUseMaterialEffectAttr == MaterialEffectState.FORCE_ENABLE.getValue()) {
            return COUIMaterialStateManager.INSTANCE.isFeatureEnabled();
        }
        return this.mUseMaterialEffectAttr != MaterialEffectState.FORCE_DISABLE.getValue() && this.mSpotLightEffectEnabled && getButtonTypeFromStyle() != ButtonType.UNKNOWN && COUIMaterialStateManager.INSTANCE.isFeatureEnabled();
    }

    private boolean isStrokeEffectEnabled() {
        if (this.mUseMaterialEffectAttr == MaterialEffectState.FORCE_ENABLE.getValue()) {
            return true;
        }
        return (this.mUseMaterialEffectAttr == MaterialEffectState.FORCE_DISABLE.getValue() || !this.mShadowEdgeEffectEnabled || getButtonTypeFromStyle() == ButtonType.UNKNOWN) ? false : true;
    }

    private int limitLargeButtonMaxWidth(int i3) {
        if (!this.mNeedLimitMaxWidth || i3 == 0 || getLayoutParams() == null) {
            return 0;
        }
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        int i4 = this.mButtonMaxWidth;
        int i5 = this.mMaxSingleLargeWidth;
        if (i4 <= i5) {
            return 0;
        }
        layoutParams.width = i5;
        return View.MeasureSpec.makeMeasureSpec(i5, 1073741824);
    }

    private void performHapticFeedback() {
        if (this.mIsNeedVibrate) {
            performHapticFeedback(302);
        }
    }

    private void setIsDescTypeStyle() {
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.coui_btn_desc_padding_horizontal);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R.dimen.coui_btn_desc_padding_vertical);
        setPaddingRelative(dimensionPixelSize, dimensionPixelSize2, dimensionPixelSize, dimensionPixelSize2);
        setGravity(17);
        int dpG2Size = (int) COUIChangeTextUtil.getDpG2Size(getResources().getDimensionPixelSize(R.dimen.coui_btn_desc_height_min), getResources().getConfiguration().fontScale);
        setMinHeight(dpG2Size);
        setMinimumHeight(dpG2Size);
        setMinWidth(0);
        setMinimumWidth(0);
        requestLayout();
    }

    private void setSpotLightEffectAttrs(boolean z2) {
        if (!isSpotlightEffectEnabled() || this.mSpotLightDrawable == null) {
            return;
        }
        updateSpotLightEffectBounds(getWidth(), getHeight());
        if (getButtonTypeFromStyle() == ButtonType.SECONDARY) {
            this.mSpotLightDrawable.setSpotLightType(getContext(), COUISpotLightEffect.SpotLightType.TYPE_OPAQUE_MEDIUM_2);
        } else if (getButtonTypeFromStyle() == ButtonType.TRANSPARENT) {
            this.mSpotLightDrawable.setSpotLightType(getContext(), COUISpotLightEffect.SpotLightType.TYPE_TRANSPARENT_MEDIUM_1);
        } else {
            this.mSpotLightDrawable.setSpotLightType(getContext(), COUISpotLightEffect.SpotLightType.TYPE_OPAQUE_MEDIUM_1);
        }
    }

    private boolean shouldUsePopupGradientRadial() {
        return isAiPaymentButton() && isStrokeEffectEnabled() && COUIMaterialEffect.isMaterialVersion() && COUIMaterialStateManager.INSTANCE.isFeatureEnabled();
    }

    private void startAnimMode() {
        if (this.mAnimType == 1) {
            setBackgroundDrawable(null);
        }
    }

    private boolean supportAddOnSmoothRound() {
        return isOs16() && this.mRoundType == 0;
    }

    private void updateRoundRectPath() {
        if (this.mRoundType == 1) {
            this.mBackgroundPath.reset();
            Path path = this.mBackgroundPath;
            RectF rectF = this.mTmpRectF;
            path.addRoundRect(rectF, getDrawableRadius(rectF), getDrawableRadius(this.mTmpRectF), Path.Direction.CCW);
            return;
        }
        if (!supportAddOnSmoothRound()) {
            COUIShapePath.getRoundRectPath(this.mBackgroundPath, this.mTmpRectF, getDrawableRadius());
            return;
        }
        if (COUIVersionUtil.getOSVersionCode() <= 37) {
            COUIShapePath.getSmoothRoundRectPath(this.mBackgroundPath, this.mTmpRectF, getDrawableRadius(), 0.0f);
        } else if (this.mRadius > 0.0f) {
            COUIShapePath.getSmoothRoundRectPath(this.mBackgroundPath, this.mTmpRectF, RoundCornerUtil.getRoundCornerForOS17(getContext(), this.mRadius), 3.0f);
        } else {
            COUIShapePath.getSmoothRoundRectPath(this.mBackgroundPath, this.mTmpRectF, getDrawableRadius(), 1.0f);
        }
    }

    private void updateSpotLightEffectBounds(int i3, int i4) {
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLightDrawable;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setBounds(0, 0, i3, i4);
        }
    }

    private void updateStrokeCorner(int i3, int i4) {
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable;
        this.mTmpRect.set(0, 0, i3, i4);
        float f3 = this.mRadius;
        float f4 = 2.0f;
        if ((f3 >= 0.0f && f3 != i4 / 2.0f) || i3 != i4) {
            f4 = (!this.mAnimEnable || f3 < 0.0f || f3 == ((float) i4) / 2.0f) ? 1.0f : 3.0f;
        }
        if (isDrawableShadowEdgeEnabled() && (cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable) != null) {
            cOUIShadowEdgeDrawable.setCornerRadius(getDrawableRadius());
            this.mShadowEdgeDrawable.setWeight(f4);
            this.mShadowEdgeDrawable.setBounds(0, 0, i3, i4);
        }
        if (COUIMaterialEffect.isMaterialVersion()) {
            if (this.mIsDrawMaterialEffect) {
                COUIMaterialEffect.setCornerParams(this, null);
                return;
            }
            OplusMaterialCornerParams cornerParams = getCornerParams();
            if (cornerParams != null) {
                cornerParams.setRadius(getDrawableRadius());
                cornerParams.setWeight(f4);
                COUIMaterialEffect.setCornerParams(this, cornerParams);
            }
        }
    }

    @Override // android.view.View
    public boolean dispatchHoverEvent(MotionEvent motionEvent) {
        if (isEnabled() && motionEvent.getActionMasked() == 9) {
            this.mMaskDrawable.setHoverEntered();
        }
        if (motionEvent.getActionMasked() == 10) {
            this.mMaskDrawable.setHoverExited();
        }
        return super.dispatchHoverEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        COUITouchMotionAnimator cOUITouchMotionAnimator;
        boolean zDispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        if (zDispatchTouchEvent && (cOUITouchMotionAnimator = this.mTouchMotionAnimator) != null) {
            cOUITouchMotionAnimator.handleTouchEvent(motionEvent);
        }
        return zDispatchTouchEvent;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        updateRoundRectPath();
        super.draw(canvas);
        if (!isSpotlightEffectEnabled() || this.mSpotLightDrawable == null) {
            return;
        }
        canvas.save();
        canvas.translate(getScrollX(), getScrollY());
        canvas.clipPath(this.mBackgroundPath);
        this.mSpotLightDrawable.draw(canvas);
        canvas.translate(-getScrollX(), -getScrollY());
        canvas.restore();
    }

    @Override // androidx.appcompat.widget.AppCompatButton, android.widget.TextView, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        COUIMaskEffectDrawable cOUIMaskEffectDrawable = this.mMaskDrawable;
        if (cOUIMaskEffectDrawable != null) {
            cOUIMaskEffectDrawable.setState(getDrawableState());
        }
        COUIStrokeDrawable cOUIStrokeDrawable = this.mStrokeDrawable;
        if (cOUIStrokeDrawable != null) {
            cOUIStrokeDrawable.setState(getDrawableState());
        }
    }

    public String getDescText() {
        return this.mDescText;
    }

    public int getDrawableColor() {
        return this.mDrawableColor;
    }

    public float getDrawableRadius() {
        return getDrawableRadius(this.mTmpRect);
    }

    public int getMeasureMaxHeight() {
        return this.mButtonMaxHeight;
    }

    public int getMeasureMaxWidth() {
        return this.mButtonMaxWidth;
    }

    public int getRoundType() {
        return this.mRoundType;
    }

    public int getSecondaryCompositeColors(Context context) {
        int attrColor = COUIContextUtil.getAttrColor(context, R.attr.couiColorContainer8);
        if (COUIMaterialEffect.isMaterialVersion() || !this.mIsDrawMaterialEffect) {
            return ColorUtils.compositeColors(attrColor, this.mHasSecondaryElevatedColor ? this.mSecondaryElevatedColor : COUIContextUtil.getAttrColor(context, R.attr.couiColorBackgroundElevated));
        }
        return attrColor;
    }

    @Override // android.view.View
    public int getSolidColor() {
        return (this.mAnimEnable && this.mAnimType == 1) ? getAnimatorColor() : super.getSolidColor();
    }

    public float getStrokeWidth() {
        return this.mStrokeWidth;
    }

    @Override // android.widget.TextView
    public CharSequence getText() {
        return isDescType() ? this.mText : super.getText();
    }

    @Override // android.widget.TextView, android.view.View, android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        super.invalidateDrawable(drawable);
        invalidate();
    }

    public boolean isDescType() {
        return (!this.mIsDescType || TextUtils.isEmpty(this.mText) || TextUtils.isEmpty(this.mDescText)) ? false : true;
    }

    public boolean isLimitHeight() {
        return this.isLimitHeight;
    }

    @Override // android.widget.TextView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        COUIMaterialStateManager.INSTANCE.addOnMaterialStateChangedListener(this);
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable;
        if (cOUIShadowEdgeDrawable != null) {
            cOUIShadowEdgeDrawable.setCallback(this);
        }
        if (!isSpotlightEffectEnabled() || this.mSpotLightDrawable == null) {
            return;
        }
        updateSpotLightEffectBounds(getWidth(), getHeight());
        this.mSpotLightDrawable.setCallback(this);
    }

    @Override // android.widget.TextView, android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        applyBlurEffect();
        applyStrokeEffect();
        applySpotlightEffect();
    }

    @Override // android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        COUIMaterialStateManager.INSTANCE.removeOnMaterialStateChangedListener(this);
        COUITouchMotionAnimator cOUITouchMotionAnimator = this.mTouchMotionAnimator;
        if (cOUITouchMotionAnimator != null) {
            cOUITouchMotionAnimator.reset();
        }
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable;
        if (cOUIShadowEdgeDrawable != null) {
            cOUIShadowEdgeDrawable.setCallback(null);
        }
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLightDrawable;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setCallback(null);
            this.mSpotLightDrawable.cancel();
        }
    }

    @Override // android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        if (!isBlurEffectEnabled() || this.mIsDrawMaterialEffect) {
            drawButtonBackground(canvas);
        }
        drawMaskAndStroke(canvas);
        super.onDraw(canvas);
        if (!isDrawableShadowEdgeEnabled() || this.mShadowEdgeDrawable == null) {
            return;
        }
        canvas.translate(getScrollX(), getScrollY());
        this.mShadowEdgeDrawable.draw(canvas);
        canvas.translate(-getScrollX(), -getScrollY());
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public CharSequence onDump() {
        return toString();
    }

    @Override // android.widget.TextView, android.view.View
    public void onFocusChanged(boolean z2, int i3, Rect rect) {
        super.onFocusChanged(z2, i3, rect);
        if (z2) {
            this.mStrokeDrawable.setFocusEntered();
            this.mMaskDrawable.setFocusEntered();
        } else {
            this.mStrokeDrawable.setFocusExited();
            this.mMaskDrawable.setFocusExited();
        }
        ViewParent parent = getParent();
        if (this.mAnimType == 1 && (parent instanceof ViewGroup) && !((ViewGroup) parent).getClipChildren()) {
            COUILog.w(TAG, "Button parent view should set clip children false to make drawing focused stroke effect works.");
        }
    }

    @Override // androidx.appcompat.widget.AppCompatButton, android.widget.TextView, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        super.onLayout(z2, i3, i4, i5, i6);
        this.mTmpRect.right = getWidth();
        this.mTmpRect.bottom = getHeight();
        this.mTmpRectF.set(this.mTmpRect);
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public void onMaterialStateChange(int i3, boolean z2) {
        if (i3 == 1) {
            this.mShadowEdgeEffectEnabled = z2;
            applyStrokeEffect();
            return;
        }
        if (i3 == 2) {
            this.mBlurEffectEnabled = z2;
            applyBlurEffect();
        } else if (i3 == 4) {
            this.mSpotLightEffectEnabled = z2;
            applySpotlightEffect();
        } else if (i3 == 8) {
            this.mCausticShadowEffectEnabled = z2;
            applyCausticShadowEffect();
        }
    }

    @Override // android.widget.TextView, android.view.View
    public void onMeasure(int i3, int i4) {
        this.mButtonMaxWidth = View.MeasureSpec.getSize(i3);
        this.mButtonMaxHeight = View.MeasureSpec.getSize(i4);
        int mode = View.MeasureSpec.getMode(i4);
        int mode2 = View.MeasureSpec.getMode(i3);
        if (mode2 == 1073741824) {
            this.mButtonWidth = this.mButtonMaxWidth;
        } else {
            this.mButtonWidth = 0;
        }
        if (mode == 1073741824) {
            this.mButtonHeight = this.mButtonMaxHeight;
        } else {
            this.mButtonHeight = 0;
        }
        int iLimitLargeButtonMaxWidth = limitLargeButtonMaxWidth(mode2);
        if (iLimitLargeButtonMaxWidth != 0) {
            i3 = iLimitLargeButtonMaxWidth;
        }
        super.onMeasure(i3, i4);
    }

    @Override // android.view.View
    public void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        OnSizeChangeListener onSizeChangeListener = this.mOnSizeChangeListener;
        if (onSizeChangeListener != null) {
            onSizeChangeListener.onSizeChanged(this, i3, i4, i5, i6);
        }
        if (isDescType()) {
            setText(this.mText);
        }
        COUITouchMotionAnimator cOUITouchMotionAnimator = this.mTouchMotionAnimator;
        if (cOUITouchMotionAnimator != null) {
            cOUITouchMotionAnimator.onSizeChange(i3, i4);
        }
        updateStrokeCorner(i3, i4);
        updateSpotLightEffectBounds(i3, i4);
    }

    @Override // androidx.appcompat.widget.AppCompatButton, android.widget.TextView
    public void onTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
        super.onTextChanged(charSequence, i3, i4, i5);
        OnTextChangeListener onTextChangeListener = this.mOnTextChangeListener;
        if (onTextChangeListener != null) {
            onTextChangeListener.onTextChanged(this, charSequence, i3, i4, i5);
        }
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable;
        if (isSpotlightEffectEnabled() && (cOUISpotLightEffectDrawable = this.mSpotLightDrawable) != null) {
            cOUISpotLightEffectDrawable.onTouchEvent(motionEvent);
        }
        if (isEnabled() && this.mAnimEnable) {
            int action = motionEvent.getAction();
            if (action == 0) {
                performHapticFeedback();
                this.mMaskDrawable.setTouchEntered();
                this.mStateEffectBackground.setTouched(true);
            } else if (action == 1 || action == 3) {
                performHapticFeedback();
                this.mMaskDrawable.setTouchExited();
                this.mStateEffectBackground.setTouched(false);
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    public void refresh() {
        this.mLowLevelDrawableColor = getLightLevelDrawableColor(getContext());
        String resourceTypeName = getResources().getResourceTypeName(this.mStyle);
        TypedArray typedArrayObtainStyledAttributes = null;
        if ("attr".equals(resourceTypeName)) {
            typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(null, R.styleable.COUIButton, this.mStyle, 0);
        } else if (BaseConfig.KEY_STYLE.equals(resourceTypeName)) {
            typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(null, R.styleable.COUIButton, 0, this.mStyle);
        }
        if (typedArrayObtainStyledAttributes != null) {
            this.mLowLevelDrawableColor = getLightLevelDrawableColor(getContext());
            adapterDisableColor(typedArrayObtainStyledAttributes);
            int color = this.mLowLevelDrawableColor;
            if (color == 0) {
                color = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIButton_drawableColor, 0);
            }
            this.mDrawableColor = color;
            this.mStrokeColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIButton_strokeColor, 0);
            setTextColor(typedArrayObtainStyledAttributes.getColorStateList(R.styleable.COUIButton_android_textColor));
            typedArrayObtainStyledAttributes.recycle();
        }
        COUIStateEffectDrawable cOUIStateEffectDrawable = this.mStateEffectBackground;
        if (cOUIStateEffectDrawable != null) {
            cOUIStateEffectDrawable.refresh(getContext());
        }
        applyBlurEffect();
        applyStrokeEffect();
        applySpotlightEffect();
        applyCausticShadowEffect();
    }

    public void setAnimEnable(boolean z2) {
        this.mAnimEnable = z2;
    }

    public void setAnimType(int i3) {
        this.mAnimType = i3;
        if (i3 == 1 || i3 == 0) {
            this.mMaskDrawable.setDrawableEnabled(true);
            this.mMaskDrawable.setMaskType(0);
            this.mStrokeDrawable.setDrawableEnabled(true);
            this.mRippleBackgroundDrawable.setDrawableEnabled(false);
        } else if (i3 == 2) {
            this.mMaskDrawable.setDrawableEnabled(true);
            this.mMaskDrawable.setMaskType(1);
            this.mStrokeDrawable.setDrawableEnabled(false);
            this.mRippleBackgroundDrawable.setDrawableEnabled(false);
        }
        updateRoundRectPath();
    }

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        COUIStateEffectDrawable cOUIStateEffectDrawable = this.mStateEffectBackground;
        if (cOUIStateEffectDrawable == null) {
            super.setBackground(drawable);
        } else if (drawable == null) {
            cOUIStateEffectDrawable.setViewBackground(new ColorDrawable(0));
        } else {
            cOUIStateEffectDrawable.setViewBackground(drawable);
        }
    }

    public void setButtonType(ButtonType buttonType) {
        this.mButtonType = buttonType.getValue();
        applyLightLevelDrawableColor();
        adapterDisableColor(null);
        applyBlurEffect();
        applyStrokeEffect();
        applySpotlightEffect();
        applyCausticShadowEffect();
    }

    public void setCausticShadow(int i3) {
        this.mShadowLevel = i3;
        applyCausticShadowEffect();
    }

    public void setDescText(String str) {
        this.mDescText = str;
        if (isDescType()) {
            setText(getText());
        }
    }

    public void setDescType(boolean z2, String str) {
        if (!z2 || TextUtils.isEmpty(getText()) || TextUtils.isEmpty(str)) {
            return;
        }
        this.mIsDescType = true;
        this.mDescText = str;
        setIsDescTypeStyle();
        setText(getText());
    }

    public void setDisabledColor(int i3) {
        this.mDisabledColor = i3;
    }

    public void setDrawableColor(int i3) {
        this.mDrawableColor = i3;
    }

    public void setDrawableRadius(int i3) {
        this.mRadius = i3;
    }

    @Override // android.widget.TextView, android.view.View
    public void setEnabled(boolean z2) {
        boolean z3 = false;
        boolean z4 = z2 != isEnabled();
        if (z4 && isDescType()) {
            setText(this.mText);
        }
        COUITouchMotionAnimator cOUITouchMotionAnimator = this.mTouchMotionAnimator;
        if (cOUITouchMotionAnimator != null && z4) {
            if (z2) {
                cOUITouchMotionAnimator.enable();
                COUITouchMotionAnimator rubberEnable = this.mTouchMotionAnimator.setDeformationEnable(z2).setRubberEnable(z2);
                if (z2 && this.mScaleEnable) {
                    z3 = true;
                }
                rubberEnable.setPressScaleEnable(z3);
            } else {
                cOUITouchMotionAnimator.disable();
                this.mTouchMotionAnimator.reset(true);
            }
        }
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLightDrawable;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setEnabled(z2);
        }
        super.setEnabled(z2);
        applyBlurEffect();
        applyStrokeEffect();
    }

    public void setIsDrawableMaterialEffect(boolean z2) {
        if (this.mIsDrawMaterialEffect == z2) {
            return;
        }
        this.mIsDrawMaterialEffect = z2;
        applyBlurEffect();
        applyStrokeEffect();
        if (getButtonTypeFromStyle() == ButtonType.AI_PAYMENT || getButtonTypeFromStyle() == ButtonType.AI_PAYMENT_SECONDARY) {
            return;
        }
        int lightLevelDrawableColor = getLightLevelDrawableColor(getContext());
        this.mLowLevelDrawableColor = lightLevelDrawableColor;
        if (this.mAnimEnable && lightLevelDrawableColor != 0) {
            this.mDrawableColor = lightLevelDrawableColor;
        }
        adapterDisableColor(null);
    }

    public void setIsNeedVibrate(boolean z2) {
        this.mIsNeedVibrate = z2;
    }

    public void setLimitHeight(boolean z2) {
        this.isLimitHeight = z2;
    }

    public void setMaxBrightness(int i3) {
        this.mMaxBrightness = i3;
    }

    @Override // android.widget.TextView
    public void setMinHeight(int i3) {
        int dimensionPixelSize;
        if (isDescType() && i3 < (dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.coui_btn_large_height_min))) {
            i3 = dimensionPixelSize;
        }
        super.setMinHeight(i3);
    }

    public void setNeedLimitMaxWidth(boolean z2) {
        this.mNeedLimitMaxWidth = z2;
    }

    public void setOnSizeChangeListener(OnSizeChangeListener onSizeChangeListener) {
        this.mOnSizeChangeListener = onSizeChangeListener;
    }

    public void setOnTextChangeListener(OnTextChangeListener onTextChangeListener) {
        this.mOnTextChangeListener = onTextChangeListener;
    }

    public void setRoundType(int i3) {
        if (i3 != 0 && i3 != 1) {
            throw new IllegalArgumentException("Invalid roundType" + i3);
        }
        if (this.mRoundType != i3) {
            this.mRoundType = i3;
            invalidate();
        }
    }

    public void setScaleEnable(boolean z2) {
        this.mScaleEnable = z2;
        COUITouchMotionAnimator cOUITouchMotionAnimator = this.mTouchMotionAnimator;
        if (cOUITouchMotionAnimator != null) {
            cOUITouchMotionAnimator.setPressScaleEnable(isEnabled() && z2);
        }
    }

    public void setStrokeColor(int i3) {
        this.mStrokeColor = i3;
    }

    public void setStrokeWidth(float f3) {
        this.mStrokeWidth = f3;
    }

    @Override // android.widget.TextView
    public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
        if (!this.mIsDescType || TextUtils.isEmpty(charSequence) || TextUtils.isEmpty(this.mDescText)) {
            super.setText(charSequence, bufferType);
        } else {
            post(new b(this, charSequence, bufferType));
        }
        this.mText = charSequence == null ? null : charSequence.toString();
    }

    public void setUseMaterialEffectAttr(MaterialEffectState materialEffectState) {
        setUseMaterialEffectAttr(materialEffectState, ButtonType.UNKNOWN);
    }

    public COUIButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, androidx.appcompat.R.attr.buttonStyle);
    }

    private float getDrawableRadius(RectF rectF) {
        if (this.mRadius < 0.0f && supportAddOnSmoothRound()) {
            return rectF.height() / 2.0f;
        }
        float f3 = this.mRadius;
        return f3 < 0.0f ? (rectF.height() / 2.0f) - this.mRadiusOffset : f3;
    }

    public void setUseMaterialEffectAttr(MaterialEffectState materialEffectState, ButtonType buttonType) {
        this.mButtonType = buttonType.getValue();
        if (UIUtil.confirmLevelAnim(UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN)) {
            COUIMaterialStateManager cOUIMaterialStateManager = COUIMaterialStateManager.INSTANCE;
            if (cOUIMaterialStateManager.materialVersionCheck() && cOUIMaterialStateManager.checkThemeEnable(getContext())) {
                this.mUseMaterialEffectAttr = materialEffectState.getValue();
                applyBlurEffect();
                applyStrokeEffect();
                applySpotlightEffect();
                applyCausticShadowEffect();
            }
        }
    }

    public COUIButton(Context context, AttributeSet attributeSet, int i3) {
        boolean z2;
        super(context, attributeSet, i3);
        this.mBackgroundPath = new Path();
        this.mScaleEnable = true;
        this.mUseMaterialEffectAttr = MaterialEffectState.FOLLOW_SYSTEM.getValue();
        this.mBlurEffectEnabled = false;
        this.mMaterialEffectEnabled = false;
        this.mShadowEdgeEffectEnabled = false;
        this.mIsDrawMaterialEffect = false;
        this.mSpotLightEffectEnabled = false;
        this.mCausticShadowEffectEnabled = false;
        this.mFillPaint = new Paint(1);
        this.mPopupGradientParams = new PopupGradientBackgroundHelper.GradientParams();
        this.mRadius = 21.0f;
        this.mCurrentBrightness = 1.0f;
        this.mCurrentScale = 1.0f;
        this.mPressColor = 0;
        this.mButtonType = 0;
        this.mTmpRect = new Rect();
        this.mTmpRectF = new RectF();
        this.mColorHsl = new float[3];
        this.isLimitHeight = true;
        this.mNeedLimitMaxWidth = false;
        this.mOplusOutLineRect = new Rect();
        this.mShadowLevel = -1;
        if (attributeSet != null && attributeSet.getStyleAttribute() != 0) {
            this.mStyle = attributeSet.getStyleAttribute();
        } else {
            this.mStyle = i3;
        }
        COUIDarkModeUtil.setForceDarkAllow(this, false);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUIButton, i3, 0);
        this.mAnimEnable = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIButton_animEnable, false);
        this.mAnimType = typedArrayObtainStyledAttributes.getInteger(R.styleable.COUIButton_animType, 1);
        this.mRoundType = typedArrayObtainStyledAttributes.getInteger(R.styleable.COUIButton_couiRoundType, 0);
        this.mIsNeedVibrate = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIButton_needVibrate, true);
        this.mScaleEnable = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIButton_scaleEnable, this.mScaleEnable);
        this.mButtonType = typedArrayObtainStyledAttributes.getInteger(R.styleable.COUIButton_buttonType, 0);
        if (UIUtil.confirmLevelAnim(UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN)) {
            COUIMaterialStateManager cOUIMaterialStateManager = COUIMaterialStateManager.INSTANCE;
            if (cOUIMaterialStateManager.materialVersionCheck() && cOUIMaterialStateManager.checkThemeEnable(getContext())) {
                this.mUseMaterialEffectAttr = typedArrayObtainStyledAttributes.getInteger(R.styleable.COUIButton_useMaterialEffect, -1);
            }
        }
        this.mIsDrawMaterialEffect = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIButton_drawableMaterialEffect, false);
        int i4 = R.styleable.COUIButton_secondaryElevatedColor;
        boolean zHasValue = typedArrayObtainStyledAttributes.hasValue(i4);
        this.mHasSecondaryElevatedColor = zHasValue;
        if (zHasValue) {
            this.mSecondaryElevatedColor = typedArrayObtainStyledAttributes.getColor(i4, 0);
        }
        this.mLowLevelDrawableColor = getLightLevelDrawableColor(context);
        if (this.mAnimEnable) {
            this.mMaxBrightness = typedArrayObtainStyledAttributes.getFloat(R.styleable.COUIButton_brightness, 0.8f);
            this.mRadius = typedArrayObtainStyledAttributes.getDimension(R.styleable.COUIButton_drawableRadius, -1.0f);
            adapterDisableColor(typedArrayObtainStyledAttributes);
            int i5 = this.mLowLevelDrawableColor;
            this.mDrawableColor = i5 == 0 ? typedArrayObtainStyledAttributes.getColor(R.styleable.COUIButton_drawableColor, 0) : i5;
            this.mStrokeColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIButton_strokeColor, 0);
            this.mPressColor = typedArrayObtainStyledAttributes.getInteger(R.styleable.COUIButton_pressColor, 0);
            z2 = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIButton_closeLimitTextSize, false);
            startAnimMode();
        } else {
            z2 = false;
        }
        this.mStrokeWidth = typedArrayObtainStyledAttributes.getDimension(R.styleable.COUIButton_strokeWidth, context.getResources().getDimension(R.dimen.coui_bordless_btn_stroke_width));
        this.mMaxSingleLargeWidth = getResources().getDimensionPixelSize(R.dimen.coui_single_larger_btn_width);
        boolean z3 = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIButton_isDescType, false);
        this.mIsDescType = z3;
        if (z3 && !TextUtils.isEmpty(getText())) {
            this.mDescText = typedArrayObtainStyledAttributes.getString(R.styleable.COUIButton_descText);
            this.mText = getText().toString();
            if (isDescType()) {
                setDescType(this.mIsDescType, this.mDescText);
            }
        }
        typedArrayObtainStyledAttributes.recycle();
        this.mRadiusOffset = getResources().getDimension(R.dimen.coui_button_radius_offset);
        if (!z2) {
            COUIChangeTextUtil.adaptFontSize(this, 4);
        }
        initStatefulBackground(context);
        initSpotLightEffects();
        initShadowEdgeDrawableIfNeeded();
        if (supportAddOnSmoothRound()) {
            setOutlineProvider(new AnonymousClass1());
            setClipToOutline(false);
            ShadowUtils.clearShadow(this);
        }
        COUITouchMotionAnimator cOUITouchMotionAnimator = new COUITouchMotionAnimator(context);
        this.mTouchMotionAnimator = cOUITouchMotionAnimator;
        cOUITouchMotionAnimator.setDeformationEnable(isEnabled()).setRubberEnable(isEnabled()).setPressScaleEnable(isEnabled() && this.mScaleEnable).addOnAnimationUpdateCallback(new a(this));
    }

    private float getDrawableRadius(Rect rect) {
        if (this.mRadius < 0.0f && supportAddOnSmoothRound()) {
            return rect.height() / 2.0f;
        }
        float f3 = this.mRadius;
        return f3 < 0.0f ? (rect.height() / 2.0f) - this.mRadiusOffset : f3;
    }
}