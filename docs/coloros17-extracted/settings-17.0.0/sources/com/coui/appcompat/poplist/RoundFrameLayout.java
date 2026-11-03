package com.coui.appcompat.poplist;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import androidx.core.content.ContextCompat;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.OnMaterialStateChangeListener;
import com.coui.appcompat.animation.rt.BlurCornerRadiusScaleSync;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.coui.appcompat.shadowedge.COUIShadowEdgeDrawable;
import com.coui.appcompat.shadowedge.IShadowEdgeEffect;
import com.coui.appcompat.spotlight.COUISpotLightEffect;
import com.coui.appcompat.spotlight.COUISpotLightEffectDrawable;
import com.coui.appcompat.uiutil.AnimLevel;
import com.coui.appcompat.uiutil.COUIBackgroundBlurBuilder;
import com.coui.appcompat.uiutil.ShadowUtils;
import com.coui.appcompat.uiutil.UIUtil;
import com.coui.appcompat.version.COUIVersionUtil;
import com.oplus.graphics.OplusOutline;
import com.oplus.graphics.OplusOutlineAdapter;
import com.oplus.graphics.OplusPath;
import com.oplus.graphics.OplusPathAdapter;
import com.support.appcompat.R;

/* JADX INFO: loaded from: classes7.dex */
public class RoundFrameLayout extends FrameLayout implements OnMaterialStateChangeListener, BlurCornerRadiusScaleSync {
    public static final int CANVAS_CLIP = 0;
    private static final int DEFAULT_BLUR_RADIUS_PX = 160;
    private static final float DEFAULT_HDR_SDR_RATIO = 1.0f;
    private static final float MATERIAL_LINE_WIDTH_DP_DARK = 2.0f;
    private static final float MATERIAL_LINE_WIDTH_DP_LIGHT = 4.0f;
    private static final int MAX_COLOR = 255;
    public static final int OUTLINE_CLIP = 1;
    private static final int SHADOW_EDGE_CORNER_TYPE_POPUP = 2;
    private static final float SHADOW_EDGE_FADE_ALPHA_IN_DARK = 0.1f;
    private static final float SHADOW_EDGE_FADE_ALPHA_IN_LIGHT = 0.9f;
    private static final float SHADOW_EDGE_LINE_ALPHA_DARK = 0.4f;
    private static final float SHADOW_EDGE_LINE_ALPHA_LIGHT = 0.9f;
    private static final String TAG = "RoundFrameLayout";
    private boolean mAllowDispatchEvent;
    private COUIBackgroundBlurBuilder mBackgroundBlurBuilder;
    private final float mBaseRoundCornerRadius;
    private int mClipMode;
    private Boolean mIsThemeBitmapBackground;
    private OnDispatchTouchEventListener mOnDispatchTouchEventListener;
    private OplusPathAdapter mOplusPathAdapter;
    private float mOutlineAlpha;
    private final Rect mOutlineRect;
    private final Rect mOverrideRect;
    private Paint mPaint;
    private Path mPath;
    private float mRadius;
    private RectF mRectF;
    private float mRoundCornerRadius;
    private float mRoundCornerWeight;
    private COUIShadowEdgeDrawable mShadowEdgeDrawable;
    private boolean mShadowEdgeEnabled;
    private COUISpotLightEffectDrawable mSpotLight;
    private boolean mSpotLightEnabled;
    private boolean mSpotLightTouchEnabled;

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.RoundFrameLayout$1, reason: invalid class name */
    public class AnonymousClass1 extends ViewOutlineProvider {
        public AnonymousClass1() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            if (RoundFrameLayout.e(RoundFrameLayout.this).isEmpty()) {
                RoundFrameLayout.d(RoundFrameLayout.this).set((int) RoundFrameLayout.g(RoundFrameLayout.this).left, (int) RoundFrameLayout.g(RoundFrameLayout.this).top, (int) RoundFrameLayout.g(RoundFrameLayout.this).right, (int) RoundFrameLayout.g(RoundFrameLayout.this).bottom);
            } else {
                outline.setAlpha(RoundFrameLayout.c(RoundFrameLayout.this));
                RoundFrameLayout.d(RoundFrameLayout.this).set(RoundFrameLayout.e(RoundFrameLayout.this));
            }
            float fH = RoundFrameLayout.h(RoundFrameLayout.this) != 0.0f ? RoundFrameLayout.h(RoundFrameLayout.this) : RoundFrameLayout.f(RoundFrameLayout.this);
            if (!RoundCornerUtil.supportSRCCompatibleBlur(RoundFrameLayout.a(RoundFrameLayout.this).useBackgroundBlur()) || COUIMaterialStateManager.INSTANCE.isLowAnimationLevel()) {
                outline.setRoundRect(RoundFrameLayout.d(RoundFrameLayout.this), fH);
                return;
            }
            if (RoundFrameLayout.j(RoundFrameLayout.this)) {
                new OplusOutline(outline).setSmoothRoundRect(RoundFrameLayout.d(RoundFrameLayout.this), fH, RoundFrameLayout.i(RoundFrameLayout.this));
                return;
            }
            if (!RoundFrameLayout.k(RoundFrameLayout.this)) {
                outline.setRoundRect(RoundFrameLayout.d(RoundFrameLayout.this), fH);
                return;
            }
            OplusOutlineAdapter oplusOutlineAdapter = new OplusOutlineAdapter(outline, 1);
            if (COUIVersionUtil.getOSVersionCode() > 37) {
                oplusOutlineAdapter.setSmoothRoundRect(RoundFrameLayout.d(RoundFrameLayout.this), fH, RoundFrameLayout.i(RoundFrameLayout.this));
                return;
            }
            if (!RoundFrameLayout.a(RoundFrameLayout.this).useBackgroundBlur()) {
                fH = RoundFrameLayout.b(RoundFrameLayout.this);
            }
            oplusOutlineAdapter.setSmoothRoundRect(RoundFrameLayout.d(RoundFrameLayout.this), fH);
        }
    }

    public interface OnDispatchTouchEventListener {
        boolean dispatchTouchEvent(MotionEvent motionEvent);
    }

    public RoundFrameLayout(Context context) {
        this(context, null);
    }

    public static /* bridge */ /* synthetic */ COUIBackgroundBlurBuilder a(RoundFrameLayout roundFrameLayout) {
        return roundFrameLayout.mBackgroundBlurBuilder;
    }

    private void applyBlurColorParams() {
        if (useHdrBlurParams()) {
            this.mBackgroundBlurBuilder.setMixColorLight(UIUtil.colorToFloats(COUIContextUtil.getColor(getContext(), R.color.coui_popup_list_mix_blur_light_hdr)));
            this.mBackgroundBlurBuilder.setMixColorDark(UIUtil.colorToFloats(COUIContextUtil.getColor(getContext(), R.color.coui_popup_list_mix_blur_dark_hdr)));
            this.mBackgroundBlurBuilder.setBlendColorLight(UIUtil.colorToFloats(COUIContextUtil.getColor(getContext(), R.color.coui_popup_list_blend_blur_light_hdr)));
            this.mBackgroundBlurBuilder.setBlendColorDark(UIUtil.colorToFloats(COUIContextUtil.getColor(getContext(), R.color.coui_popup_list_blend_blur_dark_hdr)));
            this.mBackgroundBlurBuilder.setMode(COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(getContext()) ? 2 : 3);
        } else {
            this.mBackgroundBlurBuilder.setMixColorLight(UIUtil.colorToFloats(COUIContextUtil.getColor(getContext(), R.color.coui_popup_list_mix_blur_light)));
            this.mBackgroundBlurBuilder.setMixColorDark(UIUtil.colorToFloats(COUIContextUtil.getColor(getContext(), R.color.coui_popup_list_mix_blur_dark)));
            this.mBackgroundBlurBuilder.setBlendColorLight(UIUtil.colorToFloats(COUIContextUtil.getColor(getContext(), R.color.coui_popup_list_blend_blur_light)));
            this.mBackgroundBlurBuilder.setBlendColorDark(UIUtil.colorToFloats(COUIContextUtil.getColor(getContext(), R.color.coui_popup_list_blend_blur_dark)));
            this.mBackgroundBlurBuilder.setMode(4);
        }
        this.mBackgroundBlurBuilder.setBlurRadius(DEFAULT_BLUR_RADIUS_PX);
    }

    public static /* bridge */ /* synthetic */ float b(RoundFrameLayout roundFrameLayout) {
        return roundFrameLayout.mBaseRoundCornerRadius;
    }

    public static /* bridge */ /* synthetic */ float c(RoundFrameLayout roundFrameLayout) {
        return roundFrameLayout.mOutlineAlpha;
    }

    public static /* bridge */ /* synthetic */ Rect d(RoundFrameLayout roundFrameLayout) {
        return roundFrameLayout.mOutlineRect;
    }

    private void dispatchDraw27(Canvas canvas) {
        canvas.saveLayer(this.mRectF, null, 31);
        super.dispatchDraw(canvas);
        canvas.drawPath(genPath(), this.mPaint);
        canvas.restore();
    }

    private void dispatchDraw28(Canvas canvas) {
        canvas.save();
        canvas.clipPath(genPath());
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    private float dp2px(float f3) {
        return TypedValue.applyDimension(1, f3, getContext().getResources().getDisplayMetrics());
    }

    public static /* bridge */ /* synthetic */ Rect e(RoundFrameLayout roundFrameLayout) {
        return roundFrameLayout.mOverrideRect;
    }

    private boolean execute15SRC() {
        if (RoundCornerUtil.getSmoothStyleType() != 0 || this.mRoundCornerWeight <= 0.0f) {
            return this.mBackgroundBlurBuilder.useBackgroundBlur() && COUIVersionUtil.getOSVersionCode() == 37;
        }
        return true;
    }

    private boolean execute16SRC() {
        return RoundCornerUtil.getSmoothStyleType() == 1;
    }

    public static /* bridge */ /* synthetic */ float f(RoundFrameLayout roundFrameLayout) {
        return roundFrameLayout.mRadius;
    }

    public static /* bridge */ /* synthetic */ RectF g(RoundFrameLayout roundFrameLayout) {
        return roundFrameLayout.mRectF;
    }

    private Path genPath() {
        this.mPath.reset();
        float f3 = this.mRoundCornerRadius;
        if (f3 == 0.0f) {
            f3 = this.mRadius;
        }
        float f4 = f3;
        if (!RoundCornerUtil.supportSRCCompatibleBlur(this.mBackgroundBlurBuilder.useBackgroundBlur())) {
            this.mPath.addRoundRect(this.mRectF, f4, f4, Path.Direction.CW);
        } else if (execute15SRC()) {
            new OplusPath(this.mPath).addSmoothRoundRect(this.mRectF, f4, this.mRoundCornerWeight, Path.Direction.CW);
        } else if (execute16SRC()) {
            if (this.mOplusPathAdapter == null) {
                this.mOplusPathAdapter = new OplusPathAdapter(this.mPath, 1);
            }
            if (COUIVersionUtil.getOSVersionCode() > 37) {
                this.mOplusPathAdapter.addSmoothRoundRect(this.mRectF, f4, f4, this.mRoundCornerWeight, Path.Direction.CCW);
            } else {
                if (!this.mBackgroundBlurBuilder.useBackgroundBlur()) {
                    f4 = this.mBaseRoundCornerRadius;
                }
                this.mOplusPathAdapter.addSmoothRoundRect(this.mRectF, f4, f4, Path.Direction.CCW);
            }
        } else {
            this.mPath.addRoundRect(this.mRectF, f4, f4, Path.Direction.CW);
        }
        return this.mPath;
    }

    private float[] getSmoothRadiusAndWeight() {
        float[] fArr = new float[2];
        int oSVersionCode = COUIVersionUtil.getOSVersionCode();
        if (COUIMaterialStateManager.INSTANCE.isLowAnimationLevel()) {
            fArr[0] = getResources().getDimension(R.dimen.coui_round_corner_xxl_radius);
            fArr[1] = COUIContextUtil.getFloat(getContext(), R.dimen.coui_round_corner_xxl_weight);
            return fArr;
        }
        if (oSVersionCode > 37) {
            fArr[0] = getResources().getDimension(com.support.poplist.R.dimen.coui_popup_round_frame_layout_corner_radius);
            fArr[1] = COUIContextUtil.getFloat(getContext(), R.dimen.weight_16_1_normal);
            return fArr;
        }
        fArr[0] = getResources().getDimension(R.dimen.coui_round_corner_xxl_radius);
        fArr[1] = COUIContextUtil.getFloat(getContext(), R.dimen.coui_round_corner_xxl_weight);
        return fArr;
    }

    public static /* bridge */ /* synthetic */ float h(RoundFrameLayout roundFrameLayout) {
        return roundFrameLayout.mRoundCornerRadius;
    }

    public static /* bridge */ /* synthetic */ float i(RoundFrameLayout roundFrameLayout) {
        return roundFrameLayout.mRoundCornerWeight;
    }

    private void initMaterialEffects() {
        boolean zIsCOUIDarkThemeOrNightMode = COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(getContext());
        if (this.mSpotLight == null) {
            this.mSpotLight = new COUISpotLightEffectDrawable();
        }
        this.mSpotLight.setSpotLightType(getContext(), COUISpotLightEffect.SpotLightType.TYPE_TRANSLUCENT_LARGE_1);
        if (this.mShadowEdgeDrawable == null) {
            this.mShadowEdgeDrawable = new COUIShadowEdgeDrawable();
        }
        this.mShadowEdgeDrawable.setShadowStyle(IShadowEdgeEffect.ShadowStyle.SHADOW_STYLE_2);
        this.mShadowEdgeDrawable.setEdgeStyle(IShadowEdgeEffect.EdgeStyle.EDGE_STYLE_2);
        float[] smoothRadiusAndWeight = getSmoothRadiusAndWeight();
        this.mShadowEdgeDrawable.setCornerRadius(smoothRadiusAndWeight[0]);
        this.mShadowEdgeDrawable.setWeight(smoothRadiusAndWeight[1]);
        this.mShadowEdgeDrawable.setCornerType(2);
        this.mShadowEdgeDrawable.setIsDarkMode(zIsCOUIDarkThemeOrNightMode);
        if (zIsCOUIDarkThemeOrNightMode) {
            this.mShadowEdgeDrawable.setLineAlpha(0.4f);
            this.mShadowEdgeDrawable.setLineAngle(0.0f);
            this.mShadowEdgeDrawable.setLineWidth(dp2px(2.0f));
            this.mShadowEdgeDrawable.setFadeWidthScale(1.0f);
            this.mShadowEdgeDrawable.setFadeAlpha(0.1f, 0.0f);
            this.mShadowEdgeDrawable.setPaintBlendMode(BlendMode.SRC_OVER);
        } else {
            this.mShadowEdgeDrawable.setLineAlpha(0.9f);
            this.mShadowEdgeDrawable.setLineAngle(0.0f);
            this.mShadowEdgeDrawable.setLineWidth(dp2px(4.0f));
            this.mShadowEdgeDrawable.setFadeWidthScale(1.0f);
            this.mShadowEdgeDrawable.setFadeAlpha(0.9f, 0.0f);
            this.mShadowEdgeDrawable.setPaintBlendMode(BlendMode.SRC_OVER);
        }
        this.mSpotLight.setCallback(this);
    }

    public static /* bridge */ /* synthetic */ boolean j(RoundFrameLayout roundFrameLayout) {
        return roundFrameLayout.execute15SRC();
    }

    public static /* bridge */ /* synthetic */ boolean k(RoundFrameLayout roundFrameLayout) {
        return roundFrameLayout.execute16SRC();
    }

    private boolean shouldSkipOutlineClipForTheme() {
        if (this.mIsThemeBitmapBackground == null) {
            Drawable drawable = getContext().getDrawable(com.support.poplist.R.drawable.coui_popup_window_background);
            this.mIsThemeBitmapBackground = Boolean.valueOf((drawable instanceof NinePatchDrawable) || (drawable instanceof BitmapDrawable));
        }
        return this.mIsThemeBitmapBackground.booleanValue();
    }

    private void updateBlurCornerRadius() {
        updateBlurCornerRadiusInternal(false, Float.NaN, Float.NaN);
    }

    private void updateBlurCornerRadiusFromRt(float f3, float f4) {
        updateBlurCornerRadiusInternal(true, f3, f4);
    }

    private void updateBlurCornerRadiusInternal(boolean z2, float f3, float f4) {
        COUIBackgroundBlurBuilder cOUIBackgroundBlurBuilder = this.mBackgroundBlurBuilder;
        if (cOUIBackgroundBlurBuilder != null && cOUIBackgroundBlurBuilder.useBackgroundBlur() && getWindowVisibility() == 0) {
            if (z2 || hasWindowFocus()) {
                if (Float.isNaN(f3)) {
                    f3 = getScaleX();
                }
                if (Float.isNaN(f4)) {
                    f4 = getScaleY();
                }
                this.mBackgroundBlurBuilder.setCornerRadius(Math.max(f3, f4) * this.mRoundCornerRadius);
                this.mBackgroundBlurBuilder.refreshCornerRadius();
            }
        }
    }

    private void updateMaterialEffectsState() {
        COUIMaterialStateManager cOUIMaterialStateManager = COUIMaterialStateManager.INSTANCE;
        if (!this.mBackgroundBlurBuilder.useBackgroundBlur() || !cOUIMaterialStateManager.checkThemeEnable(getContext()) || cOUIMaterialStateManager.isLowAnimationLevel() || cOUIMaterialStateManager.isLowPowerMode()) {
            this.mSpotLightEnabled = false;
            this.mShadowEdgeEnabled = false;
            COUILog.d(TAG, "Material effects disabled by common condition");
            invalidate();
            return;
        }
        boolean zIsSpotLightEffectEnable = cOUIMaterialStateManager.isSpotLightEffectEnable();
        boolean zIsStrokeEffectEnable = cOUIMaterialStateManager.isStrokeEffectEnable();
        boolean z2 = this.mSpotLightEnabled;
        boolean z3 = this.mShadowEdgeEnabled;
        this.mSpotLightEnabled = zIsSpotLightEffectEnable;
        this.mShadowEdgeEnabled = zIsStrokeEffectEnable;
        if (z2 == zIsSpotLightEffectEnable && z3 == zIsStrokeEffectEnable) {
            return;
        }
        COUILog.d(TAG, "Material effects state updated: spotlight=" + this.mSpotLightEnabled + ", shadowEdge=" + this.mShadowEdgeEnabled);
        invalidate();
    }

    private boolean useHdrBlurParams() {
        float fA;
        if (Build.VERSION.SDK_INT >= 34) {
            try {
                fA = e0.a(getContext().getDisplay());
            } catch (UnsupportedOperationException e3) {
                COUILog.e(TAG, String.valueOf(e3));
                fA = 1.0f;
            }
        } else {
            fA = 1.0f;
        }
        COUILog.d(TAG, "hdrsdr ratio=" + fA);
        return fA > 1.0f;
    }

    public void addDispatchTouchEventListener(OnDispatchTouchEventListener onDispatchTouchEventListener) {
        this.mOnDispatchTouchEventListener = onDispatchTouchEventListener;
    }

    public void clearOverrideOutline() {
        this.mOverrideRect.setEmpty();
        this.mOutlineAlpha = 1.0f;
        invalidateOutline();
    }

    public void clearSpotLightState() {
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable;
        if (!this.mBackgroundBlurBuilder.useBackgroundBlur() || (cOUISpotLightEffectDrawable = this.mSpotLight) == null) {
            return;
        }
        cOUISpotLightEffectDrawable.extinguish();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        if (!this.mOverrideRect.isEmpty()) {
            getBackground().setBounds(this.mOverrideRect);
        }
        dispatchDraw28(canvas);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable;
        OnDispatchTouchEventListener onDispatchTouchEventListener;
        if (!this.mAllowDispatchEvent) {
            return false;
        }
        if (hasWindowFocus() && isShown() && (onDispatchTouchEventListener = this.mOnDispatchTouchEventListener) != null) {
            onDispatchTouchEventListener.dispatchTouchEvent(motionEvent);
        }
        if (!this.mOverrideRect.isEmpty() && !this.mOverrideRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
            return false;
        }
        if (this.mSpotLightEnabled && this.mSpotLightTouchEnabled && (cOUISpotLightEffectDrawable = this.mSpotLight) != null) {
            cOUISpotLightEffectDrawable.onTouchEvent(motionEvent);
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        int iSave = 0;
        boolean z2 = !this.mOverrideRect.isEmpty() && shouldSkipOutlineClipForTheme();
        if (z2) {
            iSave = canvas.save();
            canvas.clipRect(this.mOverrideRect);
        }
        if (!this.mOverrideRect.isEmpty()) {
            getBackground().setBounds(this.mOverrideRect);
        }
        super.draw(canvas);
        if (z2) {
            canvas.restoreToCount(iSave);
        }
    }

    public boolean getUseBackgroundBlur() {
        return this.mBackgroundBlurBuilder.useBackgroundBlur();
    }

    public void initUseBackgroundBlur(boolean z2) {
        initUseBackgroundBlur(z2, UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isHardwareAccelerated()) {
            COUILog.e(TAG, "Hardware accelerate is disabled! Set background blur failed.");
            return;
        }
        COUIMaterialStateManager.INSTANCE.addOnMaterialStateChangedListener(this);
        if (this.mBackgroundBlurBuilder.useBackgroundBlur()) {
            this.mBackgroundBlurBuilder.setTargetView(this);
            float f3 = this.mRoundCornerRadius;
            if (f3 == 0.0f) {
                f3 = this.mRadius;
            }
            float f4 = this.mRoundCornerWeight;
            this.mBackgroundBlurBuilder.setCornerRadius(f3);
            if (RoundCornerUtil.isSupportRoundCornerWhenBlur()) {
                COUILog.i(TAG, "current version support roundCorner when use blur");
                this.mBackgroundBlurBuilder.setSmoothWeight(f4);
            }
            applyBlurColorParams();
            this.mBackgroundBlurBuilder.applyBlurBackground();
            initMaterialEffects();
        }
        updateMaterialEffectsState();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        COUIMaterialStateManager.INSTANCE.removeOnMaterialStateChangedListener(this);
        this.mBackgroundBlurBuilder.release();
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLight;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setCallback(null);
            this.mSpotLight.cancel();
        }
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable;
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable;
        super.onDraw(canvas);
        if (this.mShadowEdgeEnabled && (cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable) != null) {
            cOUIShadowEdgeDrawable.draw(canvas);
        }
        if (!this.mSpotLightEnabled || (cOUISpotLightEffectDrawable = this.mSpotLight) == null) {
            return;
        }
        cOUISpotLightEffectDrawable.draw(canvas);
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public CharSequence onDump() {
        return "RoundFrameLayout: spotlightEnabled=" + this.mSpotLightEnabled + ", shadowEdgeEnabled=" + this.mShadowEdgeEnabled + ", useBackgroundBlur=" + this.mBackgroundBlurBuilder.useBackgroundBlur();
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public void onMaterialStateChange(int i3, boolean z2) {
        if (i3 == 2 || i3 == 1 || i3 == 4) {
            updateMaterialEffectsState();
        }
    }

    @Override // android.view.View
    public void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        this.mRectF.set(getPaddingLeft(), getPaddingTop(), i3 - getPaddingRight(), i4 - getPaddingBottom());
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLight;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setBounds(0, 0, i3, i4);
        }
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable;
        if (cOUIShadowEdgeDrawable != null) {
            cOUIShadowEdgeDrawable.setBounds(0, 0, i3, i4);
        }
    }

    @Override // android.view.View
    public void onWindowFocusChanged(boolean z2) {
        super.onWindowFocusChanged(z2);
        updateBlurCornerRadius();
    }

    @Override // android.view.View
    public void onWindowVisibilityChanged(int i3) {
        super.onWindowVisibilityChanged(i3);
        updateBlurCornerRadius();
    }

    public void setAllowDispatchEvent(boolean z2) {
        this.mAllowDispatchEvent = z2;
    }

    @Override // android.view.View
    public void setAlpha(float f3) {
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable;
        super.setAlpha(f3);
        COUIBackgroundBlurBuilder cOUIBackgroundBlurBuilder = this.mBackgroundBlurBuilder;
        if (cOUIBackgroundBlurBuilder != null) {
            cOUIBackgroundBlurBuilder.onTargetViewAlphaChanged();
        }
        if (!this.mSpotLightEnabled || (cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable) == null) {
            return;
        }
        cOUIShadowEdgeDrawable.setAlpha((int) (f3 * 255.0f));
    }

    public void setClipMode(int i3) {
        this.mClipMode = i3;
        if (i3 == 0) {
            setClipToOutline(false);
            setElevation(0.0f);
            setBackgroundColor(0);
        } else if (i3 == 1) {
            setClipToOutline(!shouldSkipOutlineClipForTheme());
            if (ShadowUtils.checkOPlusViewElevationSDK()) {
                ShadowUtils.setElevationToView(this, 3);
            } else {
                setElevation(getContext().getResources().getDimensionPixelSize(R.dimen.support_shadow_size_level_five));
                setOutlineSpotShadowColor(ContextCompat.getColor(getContext(), com.support.poplist.R.color.coui_popup_outline_spot_shadow_color));
            }
            setBackgroundColor(-1);
        }
    }

    public void setOverrideOutline(int i3, int i4, int i5, int i6, float f3) {
        this.mOutlineAlpha = f3;
        this.mOverrideRect.set(i3, i4, i5, i6);
        if (getBackground() != null) {
            getBackground().setBounds(this.mOverrideRect);
        }
        invalidateOutline();
    }

    public void setRadius(float f3) {
        this.mRadius = f3;
        postInvalidate();
    }

    public void setRoundCornerRadius(float f3) {
        this.mRoundCornerRadius = f3;
        postInvalidate();
    }

    @Override // android.view.View
    public void setScaleX(float f3) {
        super.setScaleX(f3);
        updateBlurCornerRadius();
    }

    @Override // android.view.View
    public void setScaleY(float f3) {
        super.setScaleY(f3);
        updateBlurCornerRadius();
    }

    public void setSpotLightTouchEnabled(boolean z2) {
        this.mSpotLightTouchEnabled = z2;
    }

    @Override // com.coui.appcompat.animation.rt.BlurCornerRadiusScaleSync
    public void syncBlurCornerRadiusWithScale(float f3, float f4) {
        updateBlurCornerRadiusFromRt(f3, f4);
    }

    public RoundFrameLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public void initUseBackgroundBlur(boolean z2, AnimLevel animLevel) {
        this.mBackgroundBlurBuilder.setUseBackgroundBlur(z2, animLevel);
    }

    @Override // com.coui.appcompat.animation.rt.BlurCornerRadiusScaleSync
    public void syncBlurCornerRadiusWithScale() {
        updateBlurCornerRadiusFromRt(Float.NaN, Float.NaN);
    }

    public RoundFrameLayout(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.mOutlineRect = new Rect();
        this.mOverrideRect = new Rect();
        this.mOutlineAlpha = 1.0f;
        this.mAllowDispatchEvent = true;
        this.mSpotLightEnabled = false;
        this.mShadowEdgeEnabled = false;
        this.mSpotLightTouchEnabled = true;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, com.support.poplist.R.styleable.RoundFrameLayout);
        this.mRoundCornerRadius = typedArrayObtainStyledAttributes.getDimension(com.support.poplist.R.styleable.RoundFrameLayout_couiRoundCornerRadius, 0.0f);
        this.mRadius = typedArrayObtainStyledAttributes.getDimension(com.support.poplist.R.styleable.RoundFrameLayout_rfRadius, 0.0f);
        this.mClipMode = typedArrayObtainStyledAttributes.getInt(com.support.poplist.R.styleable.RoundFrameLayout_couiClipType, 0);
        this.mRoundCornerWeight = typedArrayObtainStyledAttributes.getFloat(com.support.poplist.R.styleable.RoundFrameLayout_couirfRoundCornerWeight, 0.0f);
        typedArrayObtainStyledAttributes.recycle();
        this.mPath = new Path();
        this.mPaint = new Paint(1);
        this.mRectF = new RectF();
        this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        this.mRoundCornerRadius = getSmoothRadiusAndWeight()[0];
        this.mRoundCornerWeight = getSmoothRadiusAndWeight()[1];
        this.mBaseRoundCornerRadius = getResources().getDimension(R.dimen.coui_round_corner_xxl);
        setOutlineProvider(new AnonymousClass1());
        setClipMode(this.mClipMode);
        setDefaultFocusHighlightEnabled(false);
        this.mBackgroundBlurBuilder = new COUIBackgroundBlurBuilder(getContext());
    }
}