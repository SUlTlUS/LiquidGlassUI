package com.coui.appcompat.couiswitch;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewOutlineProvider;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.widget.Switch;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.animation.PathInterpolatorCompat;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.OnMaterialStateChangeListener;
import com.coui.appcompat.animation.COUILinearInterpolator;
import com.coui.appcompat.animation.COUISpringDragHelper;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.couiswitch.olivelink.COUISwitchParams;
import com.coui.appcompat.couiswitch.olivelink.COUISwitchParamsExtKt;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.coui.appcompat.soundloadutil.COUIAsyncSoundUtil;
import com.coui.appcompat.spotlight.COUISpotLightEffect;
import com.coui.appcompat.spotlight.COUISpotLightEffectDrawable;
import com.coui.appcompat.state.COUIStateEffectDrawable;
import com.coui.appcompat.state.COUIStrokeDrawable;
import com.coui.appcompat.state.StateEffectAnimator;
import com.coui.appcompat.uiutil.ShadowUtils;
import com.decouple.injector.adaptor.BaseConfig;
import com.oplus.graphics.OplusOutlineAdapter;
import com.oplus.settings.romupdate.RomUpdateListParser;
import com.support.appcompat.R;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class COUISwitch extends SwitchCompat implements OnMaterialStateChangeListener {
    private static final int ALPHA_VALUE_30 = 1308622847;
    private static final float CIRCLE_CLICK_ENLARGE_PHASE_DENOM = 383.0f;
    private static final float CIRCLE_CLICK_ENLARGE_PHASE_NUM = 133.0f;
    private static final float CIRCLE_CLICK_MAX_SCALE = 1.3f;
    private static final float CIRCLE_CLICK_MIN_SCALE = 1.0f;
    private static final float DEFAULT_STATE_SPRING_BOUNCE = 0.0f;
    private static final float DEFAULT_STATE_SPRING_RESPONSE = 0.3f;
    private static final boolean ENABLE_SPOTLIGHT = false;
    public static final int FOLLOW_SYSTEM = -1;
    public static final int FORCE_DISABLED = 0;
    public static final int FORCE_ENABLE = 1;
    private static final float HALF = 2.0f;
    private static final float LIGHT_HEIGHT = 100.0f;
    private static final float MATERIAL_EFFECT_REFERENCE_DENSITY = 3.0f;
    private static final float MATERIAL_EFFECT_SHADOW_DX_DP = -0.6666667f;
    private static final float MATERIAL_EFFECT_SHADOW_DY_DP = -0.6666667f;
    private static final float MATERIAL_EFFECT_SHADOW_PADDING_DP = 8.0f;
    private static final float MATERIAL_EFFECT_SHADOW_RADIUS_DP = 6.6666665f;
    private static final float MATERIAL_EFFECT_STROKE_WIDTH_DP = 13.333333f;
    private static final float SPRING_CURVERATIO = 0.55f;
    private static final String TAG = "COUISwitch";
    private static final float TOGGLE_MIN_CHANGE = 0.1f;
    private int mBarCheckedColor;
    private int mBarCheckedDisabledColor;
    private int mBarHeight;
    private int mBarTrackCurrentColor;
    private int mBarUnCheckedColor;
    private int mBarUncheckedDisabledColor;
    private Drawable mCheckedDrawable;
    private int mCirclePadding;
    private float mCircleScale;
    private float mCircleScaleX;
    private int mCircleTranslation;
    private FloatPropertyCompat<COUISwitch> mCircleTranslationProperty;
    private final Path mClipPath;
    private int mDefaultTranslation;
    private boolean mEnableHapticFeedback;
    private StateEffectAnimator mHoverAnimator;
    private boolean mInitialCheckedState;
    private float mInitialTouchX;
    private float mInitialTouchY;
    private float mInnerCircleAlpha;
    private int mInnerCircleCheckedDisabledColor;
    private int mInnerCircleColor;
    private Paint mInnerCirclePaint;
    private RectF mInnerCircleRectF;
    private int mInnerCircleUncheckedDisabledColor;
    private int mInnerCircleWidth;
    private boolean mIsActionDownCanDrag;
    private boolean mIsAttachAnimRunning;
    private boolean mIsAttachedToWindow;
    private boolean mIsDelegateCall;
    private boolean mIsDraggingStarted;
    private boolean mIsLoading;
    private boolean mIsLoadingStyle;
    private boolean mIsMeasured;
    private boolean mIsSupportMaterialEffect;
    private boolean mIsThemedEnabled;
    private boolean mIsUserClickInProgress;
    private float mLoadingAlpha;
    private Drawable mLoadingDrawable;
    private float mLoadingRotation;
    private float mLoadingScale;
    private AccessibilityManager mManager;
    private OnLoadingStateChangedListener mOnLoadingStateChangedListener;
    private int mOuterCircleCheckedDisabledColor;
    private int mOuterCircleColor;
    private Bitmap mOuterCircleMaterialBitmap;
    private float mOuterCircleMaterialBitmapDensity;
    private int mOuterCircleMaterialBitmapSize;
    private Paint mOuterCirclePaint;
    private RectF mOuterCircleRectF;
    private int mOuterCircleStrokeWidth;
    private int mOuterCircleUnCheckedColor;
    private int mOuterCircleUncheckedDisabledColor;
    private int mOuterCircleWidth;
    private int mPadding;
    private boolean mPendingStateChange;
    private StateEffectAnimator mPressAnimator;
    private float mProgressScope;
    private boolean mShouldDrawSpotlight;
    private boolean mShouldPlaySound;
    private COUISpotLightEffectDrawable mSpotLight;
    private COUISpringDragHelper mSpringDragHelper;
    private AnimatorSet mStartLoadingAnimator;
    private COUIStateEffectDrawable mStateEffectBackground;
    private AnimatorSet mStopLoadingAnimator;
    private COUIStrokeDrawable mStrokeDrawable;
    private int mStyle;
    private String mSwitchLoadingStr;
    private String mSwitchOffStr;
    private String mSwitchOnStr;
    private final RectF mSwitchRect;
    private AnimatorSet mThemedLoadingAnimator;
    private Drawable mThemedLoadingCheckedBackground;
    private Drawable mThemedLoadingDrawable;
    private Drawable mThemedLoadingUncheckedBackground;
    private COUISpringAnimation mToggleSpringAnim;
    private int mTouchSlop;
    private Drawable mUncheckedDrawable;
    private int mUseMaterialEffect;

    /* JADX INFO: renamed from: com.coui.appcompat.couiswitch.COUISwitch$1, reason: invalid class name */
    public class AnonymousClass1 extends ViewOutlineProvider {
        private final Rect mOplusOutLineRect = new Rect();
        private OplusOutlineAdapter mOplusOutline;

        public AnonymousClass1() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            this.mOplusOutline = new OplusOutlineAdapter(outline, 1);
            this.mOplusOutLineRect.left = (int) COUISwitch.access$000(COUISwitch.this).left;
            this.mOplusOutLineRect.top = (int) COUISwitch.access$000(COUISwitch.this).top;
            this.mOplusOutLineRect.right = (int) COUISwitch.access$000(COUISwitch.this).right;
            this.mOplusOutLineRect.bottom = (int) COUISwitch.access$000(COUISwitch.this).bottom;
            this.mOplusOutline.setSmoothRoundRect(this.mOplusOutLineRect, (this.mOplusOutLineRect.height() * COUISwitch.this.getScaleY()) / 2.0f);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.couiswitch.COUISwitch$2, reason: invalid class name */
    public class AnonymousClass2 extends FloatPropertyCompat<COUISwitch> {
        public AnonymousClass2(String str) {
            super(str);
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public /* bridge */ /* synthetic */ float getValue(COUISwitch cOUISwitch) {
            return getValue2(cOUISwitch);
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public /* bridge */ /* synthetic */ void setValue(COUISwitch cOUISwitch, float f3) {
            setValue2(cOUISwitch, f3);
        }

        /* JADX INFO: renamed from: getValue, reason: avoid collision after fix types in other method */
        public float getValue2(COUISwitch cOUISwitch) {
            return COUISwitch.access$100(cOUISwitch);
        }

        /* JADX INFO: renamed from: setValue, reason: avoid collision after fix types in other method */
        public void setValue2(COUISwitch cOUISwitch, float f3) {
            float f4;
            cOUISwitch.setCircleTranslation((int) f3);
            int iAccess$200 = COUISwitch.access$200(cOUISwitch);
            if (iAccess$200 > 0) {
                f4 = f3 / iAccess$200;
                if (COUISwitch.access$300(cOUISwitch)) {
                    f4 = 1.0f - f4;
                }
            } else {
                f4 = 0.0f;
            }
            cOUISwitch.setCircleScaleX(f4 < 0.34725848f ? ((f4 / 0.34725848f) * 0.29999995f) + 1.0f : 1.3f - (((f4 - 0.34725848f) / 0.65274155f) * 0.29999995f));
            if (iAccess$200 > 0) {
                float fMax = Math.max(0.0f, Math.min(1.0f, f4));
                cOUISwitch.setInnerCircleAlpha(1.0f - fMax);
                COUISwitch.access$600(cOUISwitch, ColorUtils.blendARGB(COUISwitch.access$400(cOUISwitch), COUISwitch.access$500(cOUISwitch), fMax));
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.couiswitch.COUISwitch$3, reason: invalid class name */
    public class AnonymousClass3 implements COUISpringDragHelper.SpringChangeObserver {
        public AnonymousClass3() {
        }

        @Override // com.coui.appcompat.animation.COUISpringDragHelper.SpringChangeObserver
        public void onSizeChange(float f3, float f4) {
            int i3 = (int) f3;
            COUISwitch.this.setCircleTranslation(i3);
            if (COUISwitch.access$200(COUISwitch.this) > 0) {
                float fMax = Math.max(0, Math.min(COUISwitch.access$200(COUISwitch.this), i3));
                float fAccess$200 = COUISwitch.access$300(COUISwitch.this) ? 1.0f - (fMax / COUISwitch.access$200(COUISwitch.this)) : fMax / COUISwitch.access$200(COUISwitch.this);
                COUISwitch.this.setInnerCircleAlpha(1.0f - fAccess$200);
                COUISwitch.access$600(COUISwitch.this, ColorUtils.blendARGB(COUISwitch.access$400(COUISwitch.this), COUISwitch.access$500(COUISwitch.this), fAccess$200));
            }
        }

        @Override // com.coui.appcompat.animation.COUISpringDragHelper.SpringChangeObserver
        public void onStateChange(int i3) {
            if (i3 == 0) {
                COUISwitch.access$702(COUISwitch.this, false);
                if (COUISwitch.access$800(COUISwitch.this)) {
                    COUISwitch.this.setChecked(COUISwitch.access$900(COUISwitch.this), false);
                    COUISwitch.access$802(COUISwitch.this, false);
                }
            }
        }
    }

    /* JADX INFO: loaded from: classes7.dex */
    public interface OnLoadingStateChangedListener {
        void onStartLoading();

        void onStopLoading();
    }

    public COUISwitch(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void a(COUISwitch cOUISwitch, float f3) {
        cOUISwitch.mProgressScope = f3;
    }

    public static /* synthetic */ RectF access$000(COUISwitch cOUISwitch) {
        return cOUISwitch.mSwitchRect;
    }

    public static /* synthetic */ int access$100(COUISwitch cOUISwitch) {
        return cOUISwitch.mCircleTranslation;
    }

    public static /* synthetic */ int access$200(COUISwitch cOUISwitch) {
        return cOUISwitch.mDefaultTranslation;
    }

    public static /* synthetic */ boolean access$300(COUISwitch cOUISwitch) {
        return cOUISwitch.isRtlMode();
    }

    public static /* synthetic */ int access$400(COUISwitch cOUISwitch) {
        return cOUISwitch.mBarUnCheckedColor;
    }

    public static /* synthetic */ int access$500(COUISwitch cOUISwitch) {
        return cOUISwitch.mBarCheckedColor;
    }

    public static /* synthetic */ void access$600(COUISwitch cOUISwitch, int i3) {
        cOUISwitch.setBarColor(i3);
    }

    public static /* synthetic */ boolean access$702(COUISwitch cOUISwitch, boolean z2) {
        cOUISwitch.mIsAttachAnimRunning = z2;
        return z2;
    }

    public static /* synthetic */ boolean access$800(COUISwitch cOUISwitch) {
        return cOUISwitch.mPendingStateChange;
    }

    public static /* synthetic */ boolean access$802(COUISwitch cOUISwitch, boolean z2) {
        cOUISwitch.mPendingStateChange = z2;
        return z2;
    }

    public static /* synthetic */ boolean access$900(COUISwitch cOUISwitch) {
        return cOUISwitch.calculateTargetCheckedState();
    }

    private void animateWhenStateChanged(boolean z2) {
        int i3 = 0;
        if (this.mToggleSpringAnim == null || this.mCircleTranslationProperty == null) {
            if (isRtlMode()) {
                setCircleTranslation(z2 ? 0 : this.mDefaultTranslation);
            } else {
                setCircleTranslation(z2 ? this.mDefaultTranslation : 0);
            }
            setInnerCircleAlpha(z2 ? 0.0f : 1.0f);
            setBarColor(z2 ? this.mBarCheckedColor : this.mBarUnCheckedColor);
            setCircleScaleX(1.0f);
            return;
        }
        int i4 = this.mCircleTranslation;
        if (isRtlMode()) {
            if (!z2) {
                i3 = this.mDefaultTranslation;
            }
        } else if (z2) {
            i3 = this.mDefaultTranslation;
        }
        if (this.mToggleSpringAnim.isRunning()) {
            this.mToggleSpringAnim.animateToFinalPosition(i3);
            return;
        }
        this.mToggleSpringAnim.setStartValue(i4);
        COUISpringForce spring = this.mToggleSpringAnim.getSpring();
        if (spring != null) {
            applyToggleSpringParams(spring);
            spring.setFinalPosition(i3);
        }
        this.mToggleSpringAnim.start();
    }

    private void applySpotLightParams() {
        if (this.mSpotLight == null && this.mShouldDrawSpotlight) {
            COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = new COUISpotLightEffectDrawable();
            this.mSpotLight = cOUISpotLightEffectDrawable;
            cOUISpotLightEffectDrawable.setLightHeight(100.0f);
            this.mSpotLight.setSpotLightType(getContext(), COUISpotLightEffect.SpotLightType.TYPE_SWITCH);
            this.mSpotLight.setSpotLightProgressScopeListener(new a(this));
            this.mSpotLight.setCallback(this);
            this.mSpotLight.setBounds(0, 0, getWidth(), getHeight());
        }
    }

    private void applyToggleSpringParams(COUISpringForce cOUISpringForce) {
        COUISwitchParams cOUISwitchParams = COUISwitchParamsExtKt.get(COUISwitchParams.INSTANCE);
        cOUISpringForce.setResponse(cOUISwitchParams.getToggleSpring().getResponse());
        cOUISpringForce.setBounce(cOUISwitchParams.getToggleSpring().getBounce());
    }

    private Drawable backgroundDrawable() {
        if (isLoading()) {
            return isChecked() ? this.mThemedLoadingCheckedBackground : this.mThemedLoadingUncheckedBackground;
        }
        return isChecked() ? this.mCheckedDrawable : this.mUncheckedDrawable;
    }

    private boolean calculateTargetCheckedState() {
        int i3 = this.mDefaultTranslation / 2;
        int i4 = this.mCircleTranslation;
        boolean zIsRtlMode = isRtlMode();
        if (i4 >= i3) {
            return !zIsRtlMode;
        }
        return zIsRtlMode;
    }

    private float calculateTouchDistance(float f3, float f4) {
        float f5 = f3 - this.mInitialTouchX;
        float f6 = f4 - this.mInitialTouchY;
        return (float) Math.sqrt((f5 * f5) + (f6 * f6));
    }

    private boolean canClick() {
        return isEnabled();
    }

    private boolean canDrag() {
        if (!canClick() || this.mIsLoadingStyle || this.mIsThemedEnabled) {
            return false;
        }
        COUISpringAnimation cOUISpringAnimation = this.mToggleSpringAnim;
        return cOUISpringAnimation == null || !cOUISpringAnimation.isRunning();
    }

    private boolean canDrawBar() {
        return true;
    }

    private void cancelDrag() {
        COUISpringDragHelper cOUISpringDragHelper = this.mSpringDragHelper;
        if (cOUISpringDragHelper != null) {
            cOUISpringDragHelper.cancelDrag();
        }
    }

    private void configStateEffectAnimator() {
        StateEffectAnimator stateEffectAnimator = new StateEffectAnimator(this, "hover", 0, COUIContextUtil.getAttrColor(getContext(), R.attr.couiColorHover));
        this.mHoverAnimator = stateEffectAnimator;
        stateEffectAnimator.setSpringResponse(0.3f);
        this.mHoverAnimator.setSpringBounce(0.0f);
        StateEffectAnimator stateEffectAnimator2 = new StateEffectAnimator(this, "press", 0, COUIContextUtil.getAttrColor(getContext(), R.attr.couiColorPress));
        this.mPressAnimator = stateEffectAnimator2;
        stateEffectAnimator2.setSpringResponse(0.3f);
        this.mPressAnimator.setSpringBounce(0.0f);
    }

    private MotionEvent convertToTranslationCoordinates(MotionEvent motionEvent) {
        float x2 = motionEvent.getX();
        float width = getWidth();
        float fMax = Math.max(0.0f, Math.min(this.mDefaultTranslation, width > 0.0f ? (x2 / width) * this.mDefaultTranslation : 0.0f));
        MotionEvent motionEventObtainNoHistory = MotionEvent.obtainNoHistory(motionEvent);
        motionEventObtainNoHistory.setLocation(fMax, 0.0f);
        return motionEventObtainNoHistory;
    }

    private void drawBar() {
        Drawable trackDrawable;
        if (canDrawBar() && (trackDrawable = getTrackDrawable()) != null) {
            if (!isEnabled()) {
                trackDrawable.setTint(isChecked() ? this.mBarCheckedDisabledColor : this.mBarUncheckedDisabledColor);
                return;
            }
            int iCompositeColors = ColorUtils.compositeColors(this.mHoverAnimator.getCurrentMaskColor(), this.mBarTrackCurrentColor);
            if (!this.mShouldDrawSpotlight) {
                iCompositeColors = ColorUtils.compositeColors(this.mPressAnimator.getCurrentMaskColor(), iCompositeColors);
            }
            trackDrawable.setTint(iCompositeColors);
        }
    }

    private void drawLoading(Canvas canvas) {
        if (this.mIsLoading) {
            canvas.save();
            float f3 = this.mLoadingScale;
            canvas.scale(f3, f3, this.mOuterCircleRectF.centerX(), this.mOuterCircleRectF.centerY());
            canvas.rotate(this.mLoadingRotation, this.mOuterCircleRectF.centerX(), this.mOuterCircleRectF.centerY());
            Drawable drawable = this.mLoadingDrawable;
            if (drawable != null) {
                RectF rectF = this.mOuterCircleRectF;
                drawable.setBounds((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
                this.mLoadingDrawable.setAlpha((int) (this.mLoadingAlpha * 255.0f));
                this.mLoadingDrawable.draw(canvas);
            }
            canvas.restore();
        }
    }

    private void drawOuterCircle(Canvas canvas) {
        canvas.save();
        float f3 = this.mCircleScale;
        canvas.scale(f3, f3, this.mOuterCircleRectF.centerX(), this.mOuterCircleRectF.centerY());
        this.mOuterCirclePaint.setColor(isChecked() ? this.mOuterCircleColor : this.mOuterCircleUnCheckedColor);
        if (!isEnabled()) {
            this.mOuterCirclePaint.setColor(isChecked() ? this.mOuterCircleCheckedDisabledColor : this.mOuterCircleUncheckedDisabledColor);
        }
        if (isEnabled()) {
            this.mOuterCirclePaint.setAlpha((!this.mShouldDrawSpotlight || this.mSpotLight == null) ? 255 : (int) ((1.0f - this.mProgressScope) * 255.0f));
        }
        float f4 = this.mOuterCircleWidth / 2.0f;
        canvas.drawRoundRect(this.mOuterCircleRectF, f4, f4, this.mOuterCirclePaint);
        if (isEnabled() && this.mShouldDrawSpotlight) {
            ensureOuterCircleMaterialBitmap();
            if (this.mOuterCircleMaterialBitmap != null) {
                this.mClipPath.reset();
                this.mClipPath.addOval(this.mOuterCircleRectF, Path.Direction.CCW);
                canvas.clipPath(this.mClipPath);
                canvas.drawBitmap(this.mOuterCircleMaterialBitmap, this.mOuterCircleRectF.centerX() - (this.mOuterCircleMaterialBitmap.getWidth() / 2.0f), this.mOuterCircleRectF.centerY() - (this.mOuterCircleMaterialBitmap.getHeight() / 2.0f), (Paint) null);
            }
        }
        canvas.restore();
    }

    private void drawThemedBackground(Canvas canvas) {
        canvas.save();
        Drawable drawableBackgroundDrawable = backgroundDrawable();
        drawableBackgroundDrawable.setAlpha(drawableAlpha());
        int i3 = this.mPadding;
        int switchMinWidth = getSwitchMinWidth();
        int i4 = this.mPadding;
        drawableBackgroundDrawable.setBounds(i3, i3, switchMinWidth + i4, this.mBarHeight + i4);
        backgroundDrawable().draw(canvas);
        canvas.restore();
    }

    private void drawThemedLoading(Canvas canvas) {
        if (this.mIsLoading) {
            int width = (getWidth() - this.mOuterCircleWidth) / 2;
            int width2 = (getWidth() + this.mOuterCircleWidth) / 2;
            int height = (getHeight() - this.mOuterCircleWidth) / 2;
            int height2 = (getHeight() + this.mOuterCircleWidth) / 2;
            int width3 = getWidth() / 2;
            int height3 = getHeight() / 2;
            canvas.save();
            canvas.rotate(this.mLoadingRotation, width3, height3);
            this.mThemedLoadingDrawable.setBounds(width, height, width2, height2);
            this.mThemedLoadingDrawable.draw(canvas);
            canvas.restore();
        }
    }

    private int drawableAlpha() {
        return (int) ((isEnabled() ? 1.0f : 0.5f) * 255.0f);
    }

    private void ensureOuterCircleMaterialBitmap() {
        if (this.mOuterCircleWidth <= 0) {
            return;
        }
        float f3 = getResources().getDisplayMetrics().density;
        int iRound = Math.round(materialEffectDpToPx(8.0f));
        int i3 = this.mOuterCircleWidth;
        int i4 = (iRound * 2) + i3;
        Bitmap bitmap = this.mOuterCircleMaterialBitmap;
        if (bitmap != null && this.mOuterCircleMaterialBitmapSize == i3 && this.mOuterCircleMaterialBitmapDensity == f3) {
            return;
        }
        if (bitmap != null) {
            bitmap.recycle();
            this.mOuterCircleMaterialBitmap = null;
        }
        this.mOuterCircleMaterialBitmapSize = this.mOuterCircleWidth;
        this.mOuterCircleMaterialBitmapDensity = f3;
        this.mOuterCircleMaterialBitmap = Bitmap.createBitmap(i4, i4, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(this.mOuterCircleMaterialBitmap);
        Paint paint = new Paint(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(materialEffectDpToPx(MATERIAL_EFFECT_STROKE_WIDTH_DP));
        paint.setColor(0);
        paint.setShadowLayer(materialEffectDpToPx(MATERIAL_EFFECT_SHADOW_RADIUS_DP), materialEffectDpToPx(-0.6666667f), materialEffectDpToPx(-0.6666667f), -1);
        float f4 = iRound;
        int i5 = this.mOuterCircleWidth;
        canvas.drawOval(new RectF(f4, f4, i5 + f4, i5 + f4), paint);
    }

    private int getBarColor() {
        return this.mBarTrackCurrentColor;
    }

    private boolean handleActionCancel(MotionEvent motionEvent) {
        if (!this.mShouldDrawSpotlight) {
            this.mPressAnimator.animateToProgress(0.0f, true);
        }
        if (!this.mIsActionDownCanDrag) {
            this.mIsDraggingStarted = false;
            return false;
        }
        this.mIsAttachAnimRunning = true;
        this.mPendingStateChange = true;
        handleSpringDrag(motionEvent);
        getParent().requestDisallowInterceptTouchEvent(false);
        this.mIsDraggingStarted = false;
        this.mIsActionDownCanDrag = false;
        return true;
    }

    private boolean handleActionDown(MotionEvent motionEvent, float f3, float f4) {
        if (!this.mShouldDrawSpotlight) {
            this.mPressAnimator.animateToProgress(10000.0f, true);
        }
        if (this.mIsAttachAnimRunning) {
            COUILog.d(TAG, "handleActionDown: attachAnim running, return false");
            return false;
        }
        if (!isTouchInBounds(f3, f4) || !canClick()) {
            COUILog.d(TAG, "handleActionDown: out of bounds or !canClick, return false");
            return false;
        }
        this.mInitialTouchX = f3;
        this.mInitialTouchY = f4;
        if (!canDrag()) {
            this.mInitialCheckedState = isChecked();
            this.mIsActionDownCanDrag = false;
            return true;
        }
        this.mIsActionDownCanDrag = true;
        initDragState();
        if (!handleSpringDrag(motionEvent)) {
            return false;
        }
        getParent().requestDisallowInterceptTouchEvent(true);
        return true;
    }

    private boolean handleActionMove(MotionEvent motionEvent, float f3, float f4) {
        if (this.mSpringDragHelper != null && calculateTouchDistance(f3, f4) > this.mTouchSlop) {
            if (canDrag() && this.mIsActionDownCanDrag) {
                this.mIsDraggingStarted = true;
                return handleSpringDrag(motionEvent);
            }
            COUILog.d(TAG, "handleActionMove: over slop but !canDrag, return false");
        }
        return false;
    }

    private boolean handleActionUp(MotionEvent motionEvent, float f3, float f4) {
        this.mShouldPlaySound = true;
        this.mEnableHapticFeedback = true;
        if (!this.mShouldDrawSpotlight) {
            this.mPressAnimator.animateToProgress(0.0f, true);
        }
        if (this.mSpringDragHelper == null || !canDrag() || !this.mIsActionDownCanDrag) {
            getParent().requestDisallowInterceptTouchEvent(false);
            this.mIsDraggingStarted = false;
            this.mIsActionDownCanDrag = false;
            return handleClickToggle();
        }
        if (!shouldHandleAsDrag(f3, f4)) {
            getParent().requestDisallowInterceptTouchEvent(false);
            this.mIsDraggingStarted = false;
            this.mIsActionDownCanDrag = false;
            return handleClickToggle();
        }
        this.mIsAttachAnimRunning = true;
        this.mPendingStateChange = true;
        handleSpringDrag(motionEvent);
        getParent().requestDisallowInterceptTouchEvent(false);
        this.mIsDraggingStarted = false;
        this.mIsActionDownCanDrag = false;
        return true;
    }

    private boolean handleClickToggle() {
        if (this.mIsLoadingStyle && isEnabled()) {
            startLoading();
            return false;
        }
        this.mIsUserClickInProgress = true;
        try {
            setChecked(!isChecked(), true);
            return true;
        } finally {
            this.mIsUserClickInProgress = false;
        }
    }

    private boolean handleSpringDrag(MotionEvent motionEvent) {
        if (this.mSpringDragHelper == null) {
            return false;
        }
        MotionEvent motionEventConvertToTranslationCoordinates = convertToTranslationCoordinates(motionEvent);
        try {
            this.mSpringDragHelper.springDrag(motionEventConvertToTranslationCoordinates);
            return true;
        } finally {
            motionEventConvertToTranslationCoordinates.recycle();
        }
    }

    private void initAnimator() {
        initStartLoadingAnimator();
        initStopLoadingAnimator();
        initThemedLoadingAnimator();
        initSpringDragHelper();
        initToggleSpringAnim();
    }

    private void initAttr(TypedArray typedArray, Context context) {
        this.mLoadingDrawable = typedArray.getDrawable(R.styleable.COUISwitch_loadingDrawable);
        this.mBarHeight = typedArray.getDimensionPixelSize(R.styleable.COUISwitch_barHeight, 0);
        this.mOuterCircleStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.COUISwitch_outerCircleStrokeWidth, 0);
        this.mOuterCircleWidth = typedArray.getDimensionPixelOffset(R.styleable.COUISwitch_outerCircleWidth, 0);
        this.mInnerCircleWidth = typedArray.getDimensionPixelSize(R.styleable.COUISwitch_innerCircleWidth, 0);
        this.mCirclePadding = typedArray.getDimensionPixelSize(R.styleable.COUISwitch_circlePadding, 0);
        this.mInnerCircleColor = typedArray.getColor(R.styleable.COUISwitch_innerCircleColor, 0);
        this.mOuterCircleColor = typedArray.getColor(R.styleable.COUISwitch_outerCircleColor, 0);
        this.mInnerCircleUncheckedDisabledColor = typedArray.getColor(R.styleable.COUISwitch_innerCircleUncheckedDisabledColor, 0);
        this.mOuterCircleUnCheckedColor = typedArray.getColor(R.styleable.COUISwitch_outerUnCheckedCircleColor, 0);
        this.mInnerCircleCheckedDisabledColor = typedArray.getColor(R.styleable.COUISwitch_innerCircleCheckedDisabledColor, 0);
        this.mOuterCircleUncheckedDisabledColor = typedArray.getColor(R.styleable.COUISwitch_outerCircleUncheckedDisabledColor, 0);
        this.mOuterCircleCheckedDisabledColor = typedArray.getColor(R.styleable.COUISwitch_outerCircleCheckedDisabledColor, 0);
        this.mBarCheckedDisabledColor = typedArray.getColor(R.styleable.COUISwitch_barUncheckedDisabledColor, COUIContextUtil.getAttrColor(context, R.attr.couiColorPrimary) & ALPHA_VALUE_30);
        boolean z2 = getContext().getResources().getBoolean(R.bool.coui_switch_theme_enable);
        this.mIsThemedEnabled = z2;
        if (z2) {
            this.mThemedLoadingDrawable = typedArray.getDrawable(R.styleable.COUISwitch_themedLoadingDrawable);
            this.mThemedLoadingCheckedBackground = typedArray.getDrawable(R.styleable.COUISwitch_themedLoadingCheckedBackground);
            this.mThemedLoadingUncheckedBackground = typedArray.getDrawable(R.styleable.COUISwitch_themedLoadingUncheckedBackground);
            this.mCheckedDrawable = typedArray.getDrawable(R.styleable.COUISwitch_themedCheckedDrawable);
            this.mUncheckedDrawable = typedArray.getDrawable(R.styleable.COUISwitch_themedUncheckedDrawable);
            setSwitchMinWidth(getResources().getDimensionPixelSize(R.dimen.theme_style_bar_width));
        }
        setUseMaterialEffect(typedArray.getInt(R.styleable.COUISwitch_useMaterialEffect, -1));
    }

    private void initDragState() {
        this.mIsDraggingStarted = false;
        this.mInitialCheckedState = isChecked();
        if (this.mSpringDragHelper == null) {
            initSpringDragHelper();
        }
        this.mSpringDragHelper.setStartValue(this.mCircleTranslation, 0.0f);
    }

    private void initOutLine() {
        if (!isOs16() || this.mIsThemedEnabled) {
            return;
        }
        setOutlineProvider(new AnonymousClass1());
        setClipToOutline(true);
        ShadowUtils.clearShadow(this);
    }

    private void initPaint() {
        this.mOuterCirclePaint = new Paint(1);
        setPaintShadowLayer();
        this.mInnerCirclePaint = new Paint(1);
    }

    private void initResValue(Context context) {
        this.mPadding = context.getResources().getDimensionPixelSize(R.dimen.coui_switch_padding);
        this.mSwitchOnStr = getResources().getString(R.string.switch_on);
        this.mSwitchOffStr = getResources().getString(R.string.switch_off);
        this.mSwitchLoadingStr = getResources().getString(R.string.switch_loading);
        this.mDefaultTranslation = Math.max((getSwitchMinWidth() - (this.mCirclePadding * 2)) - this.mOuterCircleWidth, 0);
        this.mBarCheckedColor = COUIContextUtil.getAttrColor(context, R.attr.couiColorPrimary);
        this.mBarUnCheckedColor = COUIContextUtil.getAttrColor(context, R.attr.couiColorControls);
        this.mBarTrackCurrentColor = isChecked() ? this.mBarCheckedColor : this.mBarUnCheckedColor;
        this.mBarUncheckedDisabledColor = COUIContextUtil.getAttrColor(context, R.attr.couiColorPressBackground);
        setTrackTintMode(PorterDuff.Mode.SRC);
    }

    private void initSpringDragHelper() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new COUISpringDragHelper.Vec2(0.0f, 0.0f));
        arrayList.add(new COUISpringDragHelper.Vec2(this.mDefaultTranslation, 0.0f));
        AnonymousClass3 anonymousClass3 = new AnonymousClass3();
        COUISwitchParams cOUISwitchParams = COUISwitchParamsExtKt.get(COUISwitchParams.INSTANCE);
        COUISpringDragHelper cOUISpringDragHelper = new COUISpringDragHelper(anonymousClass3, arrayList, cOUISwitchParams.getResponseDrag(), cOUISwitchParams.getBounceDrag(), cOUISwitchParams.getResponseAttach(), cOUISwitchParams.getBounceAttach(), 0.55f, 0.0f, 0.0f, cOUISwitchParams.getTransformVelocity(), cOUISwitchParams.getTransformDistance());
        this.mSpringDragHelper = cOUISpringDragHelper;
        if (this.mDefaultTranslation > 0) {
            cOUISpringDragHelper.setStartValue(this.mCircleTranslation, 0.0f);
        }
    }

    private void initStartLoadingAnimator() {
        Interpolator interpolatorCreate = PathInterpolatorCompat.create(0.3f, 0.0f, 0.1f, 1.0f);
        this.mStartLoadingAnimator = new AnimatorSet();
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(this, "circleScale", 1.0f, 0.0f);
        objectAnimatorOfFloat.setInterpolator(interpolatorCreate);
        objectAnimatorOfFloat.setDuration(433L);
        ObjectAnimator objectAnimatorOfFloat2 = ObjectAnimator.ofFloat(this, "loadingScale", 0.5f, 1.0f);
        objectAnimatorOfFloat2.setInterpolator(interpolatorCreate);
        objectAnimatorOfFloat2.setDuration(550L);
        ObjectAnimator objectAnimatorOfFloat3 = ObjectAnimator.ofFloat(this, "loadingAlpha", 0.0f, 1.0f);
        objectAnimatorOfFloat3.setInterpolator(interpolatorCreate);
        objectAnimatorOfFloat3.setDuration(550L);
        ObjectAnimator objectAnimatorOfFloat4 = ObjectAnimator.ofFloat(this, "loadingRotation", 0.0f, 360.0f);
        objectAnimatorOfFloat4.setRepeatCount(-1);
        objectAnimatorOfFloat4.setDuration(800L);
        objectAnimatorOfFloat4.setInterpolator(new COUILinearInterpolator());
        this.mStartLoadingAnimator.play(objectAnimatorOfFloat).with(objectAnimatorOfFloat3).with(objectAnimatorOfFloat2).with(objectAnimatorOfFloat4);
    }

    private void initStateEffectBackground() {
        Drawable background = getBackground();
        COUIStrokeDrawable cOUIStrokeDrawable = new COUIStrokeDrawable(getContext());
        this.mStrokeDrawable = cOUIStrokeDrawable;
        RectF rectF = this.mSwitchRect;
        Resources resources = getContext().getResources();
        int i3 = R.dimen.bar_radius;
        cOUIStrokeDrawable.setStrokeRect(rectF, resources.getDimensionPixelOffset(i3), getContext().getResources().getDimensionPixelOffset(i3));
        if (background == null) {
            background = new ColorDrawable(0);
        }
        Drawable[] drawableArr = {background, this.mStrokeDrawable};
        setDefaultFocusHighlightEnabled(false);
        COUIStateEffectDrawable cOUIStateEffectDrawable = new COUIStateEffectDrawable(drawableArr);
        this.mStateEffectBackground = cOUIStateEffectDrawable;
        super.setBackground(cOUIStateEffectDrawable);
    }

    private void initStopLoadingAnimator() {
        Interpolator interpolatorCreate = PathInterpolatorCompat.create(0.3f, 0.0f, 0.1f, 1.0f);
        this.mStopLoadingAnimator = new AnimatorSet();
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(this, "loadingAlpha", 1.0f, 0.0f);
        objectAnimatorOfFloat.setInterpolator(interpolatorCreate);
        objectAnimatorOfFloat.setDuration(100L);
        this.mStopLoadingAnimator.play(objectAnimatorOfFloat);
    }

    private void initThemedLoadingAnimator() {
        this.mThemedLoadingAnimator = new AnimatorSet();
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(this, "loadingRotation", 0.0f, 360.0f);
        objectAnimatorOfFloat.setRepeatCount(-1);
        objectAnimatorOfFloat.setDuration(800L);
        objectAnimatorOfFloat.setInterpolator(new COUILinearInterpolator());
        this.mThemedLoadingAnimator.play(objectAnimatorOfFloat);
    }

    private void initToggleSpringAnim() {
        this.mCircleTranslationProperty = new AnonymousClass2("circleTranslation");
        this.mToggleSpringAnim = new COUISpringAnimation(this, this.mCircleTranslationProperty);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        applyToggleSpringParams(cOUISpringForce);
        this.mToggleSpringAnim.setMinimumVisibleChange(0.1f);
        this.mToggleSpringAnim.setSpring(cOUISpringForce);
    }

    private boolean isOs16() {
        return RoundCornerUtil.getSmoothStyleType() == 1;
    }

    private boolean isRtlMode() {
        return getLayoutDirection() == 1;
    }

    private boolean isTouchInBounds(float f3, float f4) {
        return f3 >= 0.0f && f3 <= ((float) getWidth()) && f4 >= 0.0f && f4 <= ((float) getHeight());
    }

    private float materialEffectDpToPx(float f3) {
        return TypedValue.applyDimension(1, f3, getResources().getDisplayMetrics());
    }

    private void performFeedBack() {
        if (isTactileFeedbackEnabled()) {
            setTactileFeedbackEnabled(false);
            if (this.mIsAttachedToWindow) {
                performHapticFeedback(302);
            }
        }
    }

    private void playSoundEffect(boolean z2) {
        COUIAsyncSoundUtil.play(getContext(), z2 ? R.raw.coui_switch_sound_on : R.raw.coui_switch_sound_off, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    private void refreshIsSupportMaterialEffect() {
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable;
        this.mIsSupportMaterialEffect = COUIMaterialStateManager.INSTANCE.isSpotLightEffectEnable();
        updateShouldDrawSpotlight();
        if (!this.mShouldDrawSpotlight && (cOUISpotLightEffectDrawable = this.mSpotLight) != null) {
            cOUISpotLightEffectDrawable.cancel();
        }
        applySpotLightParams();
    }

    private void setBarColor(int i3) {
        this.mBarTrackCurrentColor = i3;
        invalidate();
    }

    private void setInnerCircleRectF() {
        RectF rectF = this.mOuterCircleRectF;
        float f3 = rectF.left;
        int i3 = this.mOuterCircleStrokeWidth;
        this.mInnerCircleRectF.set(f3 + i3, rectF.top + i3, rectF.right - i3, rectF.bottom - i3);
    }

    private void setOuterCircleRectF() {
        int i3 = this.mOuterCircleWidth;
        float f3 = i3 * this.mCircleScaleX;
        int i4 = this.mCirclePadding + this.mCircleTranslation;
        int i5 = this.mPadding;
        float f4 = i4 + i5;
        float f5 = ((this.mBarHeight - i3) / 2.0f) + i5;
        this.mOuterCircleRectF.set(f4, f5, f3 + f4, i3 + f5);
    }

    private void setPaintShadowLayer() {
        this.mOuterCirclePaint.setShadowLayer(8.0f, 0.0f, 4.0f, Color.argb(25, 0, 0, 0));
    }

    private boolean shouldHandleAsDrag(float f3, float f4) {
        return this.mIsDraggingStarted || calculateTouchDistance(f3, f4) > ((float) this.mTouchSlop);
    }

    private void updateShouldDrawSpotlight() {
        this.mShouldDrawSpotlight = false;
    }

    public void disableThemed() {
        this.mIsThemedEnabled = false;
    }

    public void enableThemed() {
        this.mIsThemedEnabled = true;
    }

    @Override // android.widget.CompoundButton, android.widget.Button, android.widget.TextView, android.view.View
    public CharSequence getAccessibilityClassName() {
        return Switch.class.getName();
    }

    public final int getOuterCircleUncheckedColor() {
        return this.mOuterCircleUnCheckedColor;
    }

    public boolean isDraggingOrAttach() {
        return this.mIsDraggingStarted || this.mIsAttachAnimRunning;
    }

    public boolean isLoading() {
        return this.mIsLoading;
    }

    public boolean isTactileFeedbackEnabled() {
        return this.mEnableHapticFeedback;
    }

    public final boolean isUserClickInProgress() {
        return this.mIsUserClickInProgress;
    }

    @Override // android.widget.TextView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        COUIMaterialStateManager.INSTANCE.addOnMaterialStateChangedListener(this);
        this.mIsAttachedToWindow = true;
        COUIAsyncSoundUtil.register(getContext(), R.raw.coui_switch_sound_on, R.raw.coui_switch_sound_off);
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLight;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setCallback(this);
        }
    }

    @Override // android.view.View
    public void onDetachedFromWindow() {
        this.mIsAttachedToWindow = false;
        super.onDetachedFromWindow();
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLight;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setCallback(null);
            this.mSpotLight.cancel();
        }
        Bitmap bitmap = this.mOuterCircleMaterialBitmap;
        if (bitmap != null) {
            bitmap.recycle();
            this.mOuterCircleMaterialBitmap = null;
            this.mOuterCircleMaterialBitmapSize = -1;
        }
        COUIMaterialStateManager.INSTANCE.removeOnMaterialStateChangedListener(this);
    }

    @Override // androidx.appcompat.widget.SwitchCompat, android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable;
        if (this.mIsThemedEnabled) {
            drawThemedBackground(canvas);
            drawThemedLoading(canvas);
            return;
        }
        drawBar();
        setOuterCircleRectF();
        setInnerCircleRectF();
        super.onDraw(canvas);
        if (this.mShouldDrawSpotlight && (cOUISpotLightEffectDrawable = this.mSpotLight) != null) {
            cOUISpotLightEffectDrawable.draw(canvas);
        }
        drawOuterCircle(canvas);
        drawLoading(canvas);
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public CharSequence onDump() {
        return toString();
    }

    @Override // androidx.appcompat.widget.SwitchCompat, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (!this.mIsLoadingStyle) {
            accessibilityNodeInfo.setText(isChecked() ? this.mSwitchOnStr : this.mSwitchOffStr);
        } else {
            accessibilityNodeInfo.setCheckable(false);
            accessibilityNodeInfo.setText(isChecked() ? this.mSwitchOnStr : this.mSwitchOffStr);
        }
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public void onMaterialStateChange(int i3, boolean z2) {
        if (i3 == 4) {
            refreshIsSupportMaterialEffect();
        }
    }

    @Override // androidx.appcompat.widget.SwitchCompat, android.widget.TextView, android.view.View
    public void onMeasure(int i3, int i4) {
        super.onMeasure(i3, i4);
        int switchMinWidth = getSwitchMinWidth();
        int i5 = this.mPadding;
        setMeasuredDimension(switchMinWidth + (i5 * 2), this.mBarHeight + (i5 * 2));
        if (this.mIsMeasured) {
            return;
        }
        this.mIsMeasured = true;
        if (isRtlMode()) {
            this.mCircleTranslation = isChecked() ? 0 : this.mDefaultTranslation;
        } else {
            this.mCircleTranslation = isChecked() ? this.mDefaultTranslation : 0;
        }
        this.mInnerCircleAlpha = isChecked() ? 0.0f : 1.0f;
    }

    @Override // android.view.View
    public void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        this.mSwitchRect.set(0.0f, 0.0f, i3, i4);
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLight;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setBounds(0, 0, i3, i4);
        }
        if (isOs16()) {
            invalidateOutline();
        }
    }

    @Override // androidx.appcompat.widget.SwitchCompat, android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable;
        if (!isEnabled()) {
            if (motionEvent.getActionMasked() == 1 || motionEvent.getActionMasked() == 3) {
                return handleActionCancel(motionEvent);
            }
            return false;
        }
        if (this.mIsLoading) {
            COUILog.d(TAG, "onTouchEvent: loading, return false");
            return false;
        }
        float x2 = motionEvent.getX();
        float y2 = motionEvent.getY();
        if (!isClickable() && !isFocusable() && !this.mIsDelegateCall) {
            COUILog.d(TAG, "onTouchEvent: not clickable/focusable, return super");
            return super.onTouchEvent(motionEvent);
        }
        if (this.mIsAttachAnimRunning) {
            return super.onTouchEvent(motionEvent);
        }
        if (this.mShouldDrawSpotlight && (cOUISpotLightEffectDrawable = this.mSpotLight) != null) {
            cOUISpotLightEffectDrawable.onTouchEvent(motionEvent);
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            return handleActionDown(motionEvent, x2, y2);
        }
        if (actionMasked == 1) {
            return handleActionUp(motionEvent, x2, y2);
        }
        if (actionMasked != 2) {
            return actionMasked != 3 ? super.onTouchEvent(motionEvent) : handleActionCancel(motionEvent);
        }
        return handleActionMove(motionEvent, x2, y2);
    }

    @Override // android.widget.TextView, android.view.View
    public void onVisibilityChanged(View view, int i3) {
        AnimatorSet animatorSet;
        AnimatorSet animatorSet2;
        AnimatorSet animatorSet3;
        AnimatorSet animatorSet4;
        super.onVisibilityChanged(view, i3);
        if (i3 == 0) {
            if (this.mIsThemedEnabled && (animatorSet4 = this.mThemedLoadingAnimator) != null && animatorSet4.isPaused()) {
                this.mThemedLoadingAnimator.resume();
                return;
            } else {
                if (this.mIsThemedEnabled || (animatorSet3 = this.mStartLoadingAnimator) == null || !animatorSet3.isPaused()) {
                    return;
                }
                this.mStartLoadingAnimator.resume();
                return;
            }
        }
        if (this.mIsThemedEnabled && (animatorSet2 = this.mThemedLoadingAnimator) != null && animatorSet2.isRunning()) {
            this.mThemedLoadingAnimator.pause();
        } else {
            if (this.mIsThemedEnabled || (animatorSet = this.mStartLoadingAnimator) == null || !animatorSet.isRunning()) {
                return;
            }
            this.mStartLoadingAnimator.pause();
        }
    }

    public void refresh() {
        StateEffectAnimator stateEffectAnimator;
        String resourceTypeName = getResources().getResourceTypeName(this.mStyle);
        TypedArray typedArrayObtainStyledAttributes = null;
        if ("attr".equals(resourceTypeName)) {
            typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(null, R.styleable.COUISwitch, this.mStyle, 0);
        } else if (BaseConfig.KEY_STYLE.equals(resourceTypeName)) {
            typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(null, R.styleable.COUISwitch, 0, this.mStyle);
        }
        if (typedArrayObtainStyledAttributes != null) {
            initAttr(typedArrayObtainStyledAttributes, getContext());
            typedArrayObtainStyledAttributes.recycle();
            initResValue(getContext());
        }
        if (!this.mShouldDrawSpotlight && (stateEffectAnimator = this.mPressAnimator) != null) {
            stateEffectAnimator.setEndMaskColor(COUIContextUtil.getAttrColor(getContext(), R.attr.couiColorPress));
        }
        StateEffectAnimator stateEffectAnimator2 = this.mHoverAnimator;
        if (stateEffectAnimator2 != null) {
            stateEffectAnimator2.setEndMaskColor(COUIContextUtil.getAttrColor(getContext(), R.attr.couiColorHover));
        }
        COUIStateEffectDrawable cOUIStateEffectDrawable = this.mStateEffectBackground;
        if (cOUIStateEffectDrawable != null) {
            cOUIStateEffectDrawable.refresh(getContext());
        }
        invalidate();
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

    public final void setBarCheckedColor(int i3) {
        this.mBarCheckedColor = i3;
        if (isChecked()) {
            this.mBarTrackCurrentColor = this.mBarCheckedColor;
        }
        setBarStateListDrawable();
        invalidate();
    }

    public final void setBarCheckedDisabledColor(int i3) {
        this.mBarCheckedDisabledColor = i3;
        setBarStateListDrawable();
        invalidate();
    }

    public void setBarStateListDrawable() {
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.switch_custom_track_on);
        Drawable drawable2 = ContextCompat.getDrawable(getContext(), R.drawable.switch_custom_track_off);
        Drawable drawable3 = ContextCompat.getDrawable(getContext(), R.drawable.switch_custom_track_on_disable);
        Drawable drawable4 = ContextCompat.getDrawable(getContext(), R.drawable.switch_custom_track_off_disable);
        StateListDrawable stateListDrawable = new StateListDrawable();
        COUIMaterialStateManager cOUIMaterialStateManager = COUIMaterialStateManager.INSTANCE;
        boolean z2 = cOUIMaterialStateManager.materialVersionCheck() && !cOUIMaterialStateManager.isLowAnimationLevel();
        if (this.mBarCheckedColor != 0) {
            GradientDrawable gradientDrawable = (GradientDrawable) drawable.mutate();
            gradientDrawable.setColor(this.mBarCheckedColor);
            if (z2) {
                gradientDrawable.setCornerRadius(0.0f);
            }
            stateListDrawable.addState(new int[]{android.R.attr.state_checked, 16842910}, gradientDrawable);
        } else {
            stateListDrawable.addState(new int[]{android.R.attr.state_checked, 16842910}, drawable);
        }
        if (this.mBarUnCheckedColor != 0) {
            GradientDrawable gradientDrawable2 = (GradientDrawable) drawable2.mutate();
            gradientDrawable2.setColor(this.mBarUnCheckedColor);
            if (z2) {
                gradientDrawable2.setCornerRadius(0.0f);
            }
            stateListDrawable.addState(new int[]{-16842912, 16842910}, gradientDrawable2);
        } else {
            stateListDrawable.addState(new int[]{-16842912, 16842910}, drawable2);
        }
        if (this.mBarCheckedDisabledColor != 0) {
            GradientDrawable gradientDrawable3 = (GradientDrawable) drawable3.mutate();
            gradientDrawable3.setColor(this.mBarCheckedDisabledColor);
            if (z2) {
                gradientDrawable3.setCornerRadius(0.0f);
            }
            stateListDrawable.addState(new int[]{-16842910, android.R.attr.state_checked}, gradientDrawable3);
        } else {
            stateListDrawable.addState(new int[]{-16842910, android.R.attr.state_checked}, drawable3);
        }
        if (this.mBarUncheckedDisabledColor != 0) {
            GradientDrawable gradientDrawable4 = (GradientDrawable) drawable4.mutate();
            gradientDrawable4.setColor(this.mBarUncheckedDisabledColor);
            if (z2) {
                gradientDrawable4.setCornerRadius(0.0f);
            }
            stateListDrawable.addState(new int[]{-16842910, -16842912}, gradientDrawable4);
        } else {
            stateListDrawable.addState(new int[]{-16842910, -16842912}, drawable4);
        }
        setTrackDrawable(stateListDrawable);
        setTrackTintMode(PorterDuff.Mode.SRC);
    }

    public final void setBarUnCheckedColor(int i3) {
        this.mBarUnCheckedColor = i3;
        if (!isChecked()) {
            this.mBarTrackCurrentColor = this.mBarUnCheckedColor;
        }
        setBarStateListDrawable();
        invalidate();
    }

    public final void setBarUncheckedDisabledColor(int i3) {
        this.mBarUncheckedDisabledColor = i3;
        setBarStateListDrawable();
        invalidate();
    }

    @Override // androidx.appcompat.widget.SwitchCompat, android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean z2) {
        setChecked(z2, true);
    }

    public void setCheckedDrawable(Drawable drawable) {
        this.mCheckedDrawable = drawable;
    }

    public void setCircleScale(float f3) {
        this.mCircleScale = f3;
        invalidate();
    }

    public void setCircleScaleX(float f3) {
        this.mCircleScaleX = f3;
        invalidate();
    }

    public void setCircleTranslation(int i3) {
        this.mCircleTranslation = i3;
        invalidate();
    }

    public void setDelegateCall(boolean z2) {
        this.mIsDelegateCall = z2;
    }

    @Override // android.widget.TextView, android.view.View
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        if (this.mOuterCirclePaint == null) {
            this.mOuterCirclePaint = new Paint(1);
        }
        if (z2) {
            setPaintShadowLayer();
        } else {
            this.mOuterCirclePaint.clearShadowLayer();
        }
    }

    @Override // android.view.View
    public void setHovered(boolean z2) {
        super.setHovered(z2);
        if (isEnabled()) {
            this.mHoverAnimator.animateToProgress(z2 ? 10000.0f : 0.0f, true);
        }
    }

    public void setInnerCircleAlpha(float f3) {
        this.mInnerCircleAlpha = f3;
        invalidate();
    }

    public void setInnerCircleColor(int i3) {
        this.mInnerCircleColor = i3;
    }

    public void setLoadingAlpha(float f3) {
        this.mLoadingAlpha = f3;
        invalidate();
    }

    public void setLoadingDrawable(Drawable drawable) {
        this.mLoadingDrawable = drawable;
    }

    public void setLoadingRotation(float f3) {
        this.mLoadingRotation = f3;
        invalidate();
    }

    public void setLoadingScale(float f3) {
        this.mLoadingScale = f3;
        invalidate();
    }

    public void setLoadingStyle(boolean z2) {
        this.mIsLoadingStyle = z2;
    }

    public void setOnLoadingStateChangedListener(OnLoadingStateChangedListener onLoadingStateChangedListener) {
        this.mOnLoadingStateChangedListener = onLoadingStateChangedListener;
    }

    public void setOuterCircleColor(int i3) {
        this.mOuterCircleColor = i3;
    }

    public void setOuterCircleStrokeWidth(int i3) {
        this.mOuterCircleStrokeWidth = i3;
    }

    public final void setOuterCircleUncheckedColor(int i3) {
        this.mOuterCircleUnCheckedColor = i3;
        invalidate();
    }

    public void setShouldPlaySound(boolean z2) {
        this.mShouldPlaySound = z2;
    }

    public void setTactileFeedbackEnabled(boolean z2) {
        this.mEnableHapticFeedback = z2;
    }

    public void setThemedLoadingCheckedBackground(Drawable drawable) {
        this.mThemedLoadingCheckedBackground = drawable;
    }

    public void setThemedLoadingUncheckedBackground(Drawable drawable) {
        this.mThemedLoadingUncheckedBackground = drawable;
    }

    public void setUncheckedDrawable(Drawable drawable) {
        this.mUncheckedDrawable = drawable;
    }

    public void setUseMaterialEffect(int i3) {
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable;
        this.mUseMaterialEffect = i3;
        if (i3 != -1 && i3 != 0 && i3 != 1) {
            this.mUseMaterialEffect = -1;
        }
        updateShouldDrawSpotlight();
        if (!this.mShouldDrawSpotlight && (cOUISpotLightEffectDrawable = this.mSpotLight) != null) {
            cOUISpotLightEffectDrawable.cancel();
        }
        applySpotLightParams();
        invalidate();
    }

    public void startLoading() {
        if (this.mIsLoading) {
            return;
        }
        AccessibilityManager accessibilityManager = this.mManager;
        if (accessibilityManager != null && accessibilityManager.isEnabled()) {
            announceForAccessibility(this.mSwitchLoadingStr);
        }
        this.mIsLoading = true;
        if (this.mIsThemedEnabled) {
            this.mThemedLoadingAnimator.start();
        } else {
            this.mStartLoadingAnimator.start();
        }
        OnLoadingStateChangedListener onLoadingStateChangedListener = this.mOnLoadingStateChangedListener;
        if (onLoadingStateChangedListener != null) {
            onLoadingStateChangedListener.onStartLoading();
        }
        invalidate();
    }

    public void stopLoading() {
        AccessibilityManager accessibilityManager;
        if (this.mIsLoadingStyle && (accessibilityManager = this.mManager) != null && accessibilityManager.isEnabled()) {
            announceForAccessibility(isChecked() ? this.mSwitchOffStr : this.mSwitchOnStr);
        }
        AnimatorSet animatorSet = this.mStartLoadingAnimator;
        if (animatorSet != null && animatorSet.isRunning()) {
            this.mStartLoadingAnimator.cancel();
        }
        AnimatorSet animatorSet2 = this.mThemedLoadingAnimator;
        if (animatorSet2 != null && animatorSet2.isRunning()) {
            this.mThemedLoadingAnimator.cancel();
        }
        if (this.mIsLoading) {
            if (!this.mIsThemedEnabled) {
                this.mStopLoadingAnimator.start();
            }
            setCircleScale(1.0f);
            this.mIsLoading = false;
            toggle();
            OnLoadingStateChangedListener onLoadingStateChangedListener = this.mOnLoadingStateChangedListener;
            if (onLoadingStateChangedListener != null) {
                onLoadingStateChangedListener.onStopLoading();
            }
        }
    }

    public COUISwitch(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiSwitchStyle);
    }

    public void setChecked(boolean z2, boolean z3) {
        if (z2 == isChecked()) {
            return;
        }
        cancelDrag();
        super.setChecked(z2);
        if (!this.mIsThemedEnabled) {
            z2 = isChecked();
            if (!this.mIsAttachedToWindow || !z3 || getHeight() <= 0 || getWidth() <= 0) {
                COUISpringAnimation cOUISpringAnimation = this.mToggleSpringAnim;
                if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
                    this.mToggleSpringAnim.cancelComplete();
                }
                if (isRtlMode()) {
                    setCircleTranslation(z2 ? 0 : this.mDefaultTranslation);
                } else {
                    setCircleTranslation(z2 ? this.mDefaultTranslation : 0);
                }
                setInnerCircleAlpha(z2 ? 0.0f : 1.0f);
                setBarColor(z2 ? this.mBarCheckedColor : this.mBarUnCheckedColor);
                setCircleScaleX(1.0f);
            } else {
                animateWhenStateChanged(z2);
            }
        }
        if (this.mShouldPlaySound && this.mIsAttachedToWindow) {
            playSoundEffect(z2);
            this.mShouldPlaySound = false;
        }
        performFeedBack();
        invalidate();
    }

    public COUISwitch(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.mSwitchRect = new RectF();
        this.mClipPath = new Path();
        this.mIsLoading = false;
        this.mIsLoadingStyle = false;
        this.mOuterCircleRectF = new RectF();
        this.mInnerCircleRectF = new RectF();
        this.mCircleScaleX = 1.0f;
        this.mCircleScale = 1.0f;
        this.mIsMeasured = false;
        this.mIsDelegateCall = false;
        this.mPendingStateChange = false;
        this.mIsDraggingStarted = false;
        this.mIsAttachAnimRunning = false;
        this.mIsActionDownCanDrag = false;
        this.mOuterCircleMaterialBitmapSize = -1;
        this.mOuterCircleMaterialBitmapDensity = 3.0f;
        this.mUseMaterialEffect = -1;
        this.mProgressScope = 0.0f;
        this.mIsSupportMaterialEffect = false;
        setSoundEffectsEnabled(false);
        COUIDarkModeUtil.setForceDarkAllow(this, false);
        this.mManager = (AccessibilityManager) getContext().getSystemService(RomUpdateListParser.ACCESSIBILITY_TAG);
        if (attributeSet != null && attributeSet.getStyleAttribute() != 0) {
            this.mStyle = attributeSet.getStyleAttribute();
        } else {
            this.mStyle = i3;
        }
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUISwitch, i3, 0);
        initAttr(typedArrayObtainStyledAttributes, context);
        typedArrayObtainStyledAttributes.recycle();
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        initPaint();
        initResValue(context);
        initAnimator();
        initStateEffectBackground();
        configStateEffectAnimator();
        initOutLine();
        updateShouldDrawSpotlight();
        applySpotLightParams();
    }
}