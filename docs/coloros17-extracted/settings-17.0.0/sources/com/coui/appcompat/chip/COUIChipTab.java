package com.coui.appcompat.chip;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.ViewParent;
import com.coui.appcompat.COUIMaterialBlurEffect;
import com.coui.appcompat.COUIMaterialEffect;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.COUIMaterialStrokeEffect;
import com.coui.appcompat.OnMaterialStateChangeListener;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.coui.appcompat.spotlight.COUISpotLightEffect;
import com.coui.appcompat.spotlight.COUISpotLightEffectDrawable;
import com.coui.appcompat.textutil.COUIChangeTextUtil;
import com.coui.appcompat.uiutil.ShadowUtils;
import com.coui.appcompat.uiutil.UIUtil;
import com.oplus.graphics.OplusOutlineAdapter;
import com.oplus.view.material.OplusMaterialCornerParams;
import com.support.chip.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIChipTab extends COUIChip implements OnMaterialStateChangeListener {
    private static final float ALPHA_0 = 0.0f;
    private static final float ALPHA_1 = 1.0f;
    private static final boolean DEBUG;
    private static final int DEF_STYLE_RES;
    private static final float ICON_ONLY_CORNER_RADIUS_DP = 16.0f;
    private static final float ICON_ONLY_DIAMETER_DP = 32.0f;
    private static final float SHADOW_CLIP_EXPAND_BOTTOM_DP = 100.0f;
    private static final String TAG = "COUIChipTab";
    private static final String VIEW_CLASS_NAME = "android.view.View";
    private float mCheckedLayerAlpha;
    private boolean mIsBlurEffectEnabled;
    private boolean mIsCausticShadowEffectEnabled;
    private boolean mIsSpotLightEffectEnabled;
    private boolean mIsStrokeEffectEnabled;
    private MaterialEffectType mMaterialEffectType;
    private final Rect mOplusOutLineRect;
    private OplusOutlineAdapter mOplusOutline;
    private boolean mShowIconOnly;
    private COUISpotLightEffectDrawable mSpotLight;
    private int mUseMaterialEffectAttr;

    /* JADX INFO: renamed from: com.coui.appcompat.chip.COUIChipTab$1, reason: invalid class name */
    public class AnonymousClass1 extends ViewOutlineProvider {
        public AnonymousClass1() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            if (RoundCornerUtil.getSmoothStyleType() != 1) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), COUIChipTab.this.getChipCornerRadius());
                return;
            }
            if (COUIChipTab.access$000(COUIChipTab.this) == null) {
                COUIChipTab.access$002(COUIChipTab.this, new OplusOutlineAdapter(outline, 1));
            }
            COUIChipTab.access$100(COUIChipTab.this).set(0, 0, view.getWidth(), view.getHeight());
            COUIChipTab.access$000(COUIChipTab.this).setSmoothRoundRect(COUIChipTab.access$100(COUIChipTab.this), COUIChipTab.this.getChipCornerRadius(), 1.0f);
        }
    }

    public enum MaterialEffectState {
        FORCE_ENABLE(1),
        FORCE_DISABLE(0),
        FOLLOW_SYSTEM(-1);

        private final int mValue;

        MaterialEffectState(int i3) {
            this.mValue = i3;
        }

        public int getValue() {
            return this.mValue;
        }
    }

    public enum MaterialEffectType {
        TYPE_DEFAULT(0),
        TYPE_DEFAULT_NO_BLUR(1);

        private final int mValue;

        MaterialEffectType(int i3) {
            this.mValue = i3;
        }

        public static /* synthetic */ COUIMaterialBlurEffect.BlurEffectType access$200(MaterialEffectType materialEffectType) {
            return materialEffectType.getUnSelectedBlurEffectType();
        }

        public static /* synthetic */ COUIMaterialBlurEffect.BlurEffectType access$300(MaterialEffectType materialEffectType) {
            return materialEffectType.getSelectedBlurEffectType();
        }

        private COUIMaterialBlurEffect.BlurEffectType getSelectedBlurEffectType() {
            return this == TYPE_DEFAULT_NO_BLUR ? COUIMaterialBlurEffect.BlurEffectType.TYPE_CONTENT_CHIP_TAB_SELECTED_NO_BLUR : COUIMaterialBlurEffect.BlurEffectType.TYPE_CONTENT_CHIP_TAB_SELECTED;
        }

        private COUIMaterialBlurEffect.BlurEffectType getUnSelectedBlurEffectType() {
            return this == TYPE_DEFAULT_NO_BLUR ? COUIMaterialBlurEffect.BlurEffectType.TYPE_CONTENT_CHIP_TAB_NO_BLUR : COUIMaterialBlurEffect.BlurEffectType.TYPE_CONTENT_CHIP_TAB;
        }

        public int getValue() {
            return this.mValue;
        }
    }

    static {
        DEBUG = COUILog.LOG_DEBUG || COUILog.isLoggable(TAG, 3);
        DEF_STYLE_RES = R.style.Widget_COUI_Chip;
    }

    public COUIChipTab(Context context) {
        this(context, null);
    }

    public static /* synthetic */ OplusOutlineAdapter access$000(COUIChipTab cOUIChipTab) {
        return cOUIChipTab.mOplusOutline;
    }

    public static /* synthetic */ OplusOutlineAdapter access$002(COUIChipTab cOUIChipTab, OplusOutlineAdapter oplusOutlineAdapter) {
        cOUIChipTab.mOplusOutline = oplusOutlineAdapter;
        return oplusOutlineAdapter;
    }

    public static /* synthetic */ Rect access$100(COUIChipTab cOUIChipTab) {
        return cOUIChipTab.mOplusOutLineRect;
    }

    private void applyMaterialEffect() {
        if (isBlurEffectEnabled()) {
            COUIMaterialEffect.setCornerParams(this, new OplusMaterialCornerParams(80.0f, 1.0f));
            COUIMaterialEffect.setViewBlurEffect(this, COUIMaterialEffect.getBlurParamsByType(getContext(), MaterialEffectType.access$200(this.mMaterialEffectType)), COUIMaterialEffect.getBlurParamsByType(getContext(), MaterialEffectType.access$300(this.mMaterialEffectType)), COUIMaterialEffect.getBlurParamsByType(getContext(), MaterialEffectType.access$300(this.mMaterialEffectType)), this.mCheckedLayerAlpha);
        } else {
            COUIMaterialEffect.clearBlurEffect(this);
        }
        if (!isStrokeEffectEnabled()) {
            COUIMaterialEffect.setViewStrokeEffect(this, COUIMaterialStrokeEffect.StrokeEffectType.TYPE_NO_EFFECT);
            return;
        }
        COUIMaterialStrokeEffect.Companion.StrokeEffectParams strokeParamsByType = COUIMaterialEffect.getStrokeParamsByType(getContext(), COUIMaterialStrokeEffect.StrokeEffectType.TYPE_NO_EFFECT_BASE_1);
        Context context = getContext();
        COUIMaterialStrokeEffect.StrokeEffectType strokeEffectType = COUIMaterialStrokeEffect.StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_2;
        COUIMaterialEffect.setViewStrokeEffect(this, strokeParamsByType, COUIMaterialEffect.getStrokeParamsByType(context, strokeEffectType), COUIMaterialEffect.getStrokeEffectParamsHolder(getContext(), strokeEffectType), this.mCheckedLayerAlpha);
    }

    private void applyShadow(float f3) {
        if (isCausticShadowEffectEnabled() && COUIMaterialEffect.isMaterialVersion()) {
            setCausticShadow(f3);
        } else {
            ShadowUtils.setElevationToView(this, 5, f3);
            COUIMaterialEffect.clearCausticShadow(this);
        }
    }

    public static /* synthetic */ void b(COUIChipTab cOUIChipTab, float f3) {
        cOUIChipTab.applyShadow(f3);
        cOUIChipTab.mCheckedLayerAlpha = f3;
        cOUIChipTab.applyMaterialEffect();
    }

    private int dpToPx(float f3) {
        return Math.round(f3 * getResources().getDisplayMetrics().density);
    }

    private float getOutlineCornerRadius(int i3, int i4) {
        return (!this.mShowIconOnly || i3 <= 0 || i4 <= 0) ? getChipCornerRadius() : Math.min(i3, i4) / 2.0f;
    }

    private boolean isBlurEffectEnabled() {
        if (this.mUseMaterialEffectAttr == MaterialEffectState.FORCE_ENABLE.getValue()) {
            return true;
        }
        if (this.mUseMaterialEffectAttr == MaterialEffectState.FORCE_DISABLE.getValue()) {
            return false;
        }
        return this.mIsBlurEffectEnabled;
    }

    private boolean isCausticShadowEffectEnabled() {
        if (this.mUseMaterialEffectAttr == MaterialEffectState.FORCE_ENABLE.getValue()) {
            return COUIMaterialStateManager.INSTANCE.isFeatureEnabled();
        }
        return this.mUseMaterialEffectAttr != MaterialEffectState.FORCE_DISABLE.getValue() && this.mIsCausticShadowEffectEnabled && COUIMaterialStateManager.INSTANCE.isFeatureEnabled();
    }

    private boolean isSpotLightEffectEnabled() {
        if (this.mUseMaterialEffectAttr == MaterialEffectState.FORCE_ENABLE.getValue()) {
            return COUIMaterialStateManager.INSTANCE.isFeatureEnabled();
        }
        return this.mUseMaterialEffectAttr != MaterialEffectState.FORCE_DISABLE.getValue() && this.mIsSpotLightEffectEnabled && COUIMaterialStateManager.INSTANCE.isFeatureEnabled();
    }

    private boolean isStrokeEffectEnabled() {
        if (this.mUseMaterialEffectAttr == MaterialEffectState.FORCE_ENABLE.getValue()) {
            return true;
        }
        if (this.mUseMaterialEffectAttr == MaterialEffectState.FORCE_DISABLE.getValue()) {
            return false;
        }
        return this.mIsStrokeEffectEnabled;
    }

    private void setCausticShadow(float f3) {
        ShadowUtils.setElevationToView(this, 6, f3);
        int color = getResources().getColor(com.coui.appcompat.effect.R.color.coui_caustic_color_3);
        COUIMaterialEffect.setOutlineCausticShadowColor(this, Color.argb((int) (Color.alpha(color) * f3), Color.red(color), Color.green(color), Color.blue(color)));
        COUIMaterialEffect.setOutlineCausticShadowLayout(this, getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_l_level1), getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_t_level1), getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_r_level1), getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_b_level1));
        COUIMaterialEffect.setOutlineCausticShadowParams(this, getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_sdf_level1));
    }

    private void updateClipExpandForShadow() {
        int width = getWidth();
        int height = getHeight();
        if (width <= 0 || height <= 0 || !isCheckable() || !isChecked()) {
            return;
        }
        int iDpToPx = dpToPx(100.0f);
        UIUtil.setViewClipExpandSize(this, iDpToPx, iDpToPx, iDpToPx, iDpToPx);
        updateClipExpandForShadowOnParents(iDpToPx, iDpToPx);
    }

    private void updateClipExpandForShadowOnParents(int i3, int i4) {
        for (ViewParent parent = getParent(); parent != null; parent = parent.getParent()) {
            if (parent instanceof View) {
                UIUtil.setViewClipExpandSize((View) parent, i3, i4, i3, i4);
            }
        }
    }

    private void updateIconOnlyState() {
        if (this.mShowIconOnly) {
            setText("");
            setCountVisible(false);
            setShowRedDot(false);
            setChipIconVisible(true);
            setMinimumWidth(0);
            setChipMinHeight(0.0f);
            setChipCornerRadius(dpToPx(16.0f));
            float fDpToPx = dpToPx(8.0f);
            setChipStartPadding(fDpToPx);
            setChipEndPadding(fDpToPx);
            if (getLayoutParams() != null) {
                int iDpToPx = dpToPx(32.0f);
                getLayoutParams().width = iDpToPx;
                getLayoutParams().height = iDpToPx;
                requestLayout();
            }
        }
    }

    private void updateShadowByCheckedState(boolean z2) {
        applyShadow(z2 ? 1.0f : 0.0f);
        this.mCheckedLayerAlpha = z2 ? 1.0f : 0.0f;
        applyMaterialEffect();
    }

    @Override // android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (isSpotLightEffectEnabled()) {
            this.mSpotLight.onTouchEvent(motionEvent);
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        if (isSpotLightEffectEnabled()) {
            this.mSpotLight.setSpotLightType(getContext(), COUISpotLightEffect.SpotLightType.TYPE_CHIP_TAB);
            this.mSpotLight.draw(canvas);
        }
        super.draw(canvas);
    }

    @Override // com.coui.appcompat.chip.COUIChip, android.widget.CheckBox, android.widget.CompoundButton, android.widget.Button, android.widget.TextView, android.view.View
    public CharSequence getAccessibilityClassName() {
        return "android.view.View";
    }

    public COUIChipTabDrawable getTabDrawable() {
        COUIChipDrawable chipDrawable = getChipDrawable();
        if (chipDrawable instanceof COUIChipTabDrawable) {
            return (COUIChipTabDrawable) chipDrawable;
        }
        return null;
    }

    public int getUseMaterialEffectAttr() {
        return this.mUseMaterialEffectAttr;
    }

    public boolean isShowIconOnly() {
        return this.mShowIconOnly;
    }

    public boolean isTabSelected() {
        return isCheckable() && isChecked();
    }

    @Override // android.widget.TextView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isSpotLightEffectEnabled()) {
            this.mSpotLight.setCallback(this);
        }
        COUIMaterialStateManager.INSTANCE.addOnMaterialStateChangedListener(this);
        COUIChipTabDrawable tabDrawable = getTabDrawable();
        if (tabDrawable != null) {
            tabDrawable.registerObservers();
        }
    }

    @Override // android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (isSpotLightEffectEnabled()) {
            this.mSpotLight.setCallback(null);
            this.mSpotLight.cancel();
        }
        COUIMaterialStateManager.INSTANCE.removeOnMaterialStateChangedListener(this);
        COUIChipTabDrawable tabDrawable = getTabDrawable();
        if (tabDrawable != null) {
            tabDrawable.unregisterObservers();
        }
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public CharSequence onDump() {
        return toString();
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public void onMaterialStateChange(int i3, boolean z2) {
        if (i3 == 1) {
            this.mIsStrokeEffectEnabled = z2;
        } else if (i3 == 2) {
            this.mIsBlurEffectEnabled = z2;
        } else if (i3 == 4) {
            this.mIsSpotLightEffectEnabled = z2;
        } else if (i3 == 8) {
            this.mIsCausticShadowEffectEnabled = z2;
        }
        applyMaterialEffect();
        applyShadow((isCheckable() && isChecked()) ? 1.0f : 0.0f);
    }

    @Override // com.coui.appcompat.chip.COUIChip, android.widget.TextView, android.view.View
    public void onMeasure(int i3, int i4) {
        super.onMeasure(i3, i4);
        if (this.mShowIconOnly) {
            int iDpToPx = dpToPx(32.0f);
            int iMin = Math.min(View.resolveSize(iDpToPx, i3), View.resolveSize(iDpToPx, i4));
            setMeasuredDimension(iMin, iMin);
            setChipCornerRadius(dpToPx(16.0f));
        }
    }

    @Override // android.view.View
    public void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLight;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setBounds(0, 0, i3, i4);
        }
        updateClipExpandForShadow();
    }

    @Override // com.coui.appcompat.chip.COUIChip
    public void refresh() {
        COUIChipTabDrawable tabDrawable = getTabDrawable();
        if (tabDrawable != null) {
            tabDrawable.refresh(getContext());
        }
        int attrColor = COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorLabelPrimary);
        setUncheckedTextColor(attrColor);
        setCheckedTextColor(attrColor);
        int attrColor2 = COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorLabelTertiary);
        setUncheckedDisabledTextColor(attrColor2);
        setCheckedDisabledTextColor(attrColor2);
        setUncheckedDisabledChipIconTint(attrColor2);
        setCheckedDisabledChipIconTint(attrColor2);
        applyMaterialEffect();
        invalidate();
    }

    @Override // com.coui.appcompat.chip.COUIChip, android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean z2) {
        super.setChecked(z2);
        updateClipExpandForShadow();
    }

    public void setMaterialEffectType(MaterialEffectType materialEffectType) {
        if (this.mMaterialEffectType == materialEffectType) {
            return;
        }
        this.mMaterialEffectType = materialEffectType;
        applyMaterialEffect();
    }

    public void setShowIconOnly(boolean z2) {
        if (this.mShowIconOnly == z2) {
            return;
        }
        this.mShowIconOnly = z2;
        updateIconOnlyState();
    }

    public void setTabSelected(boolean z2) {
        if (isCheckable()) {
            setChecked(z2);
        }
    }

    @Override // com.coui.appcompat.chip.COUIChip, android.widget.TextView
    public void setTextSize(float f3) {
        super.setTextSize(f3);
        COUIChangeTextUtil.adaptFontSize(this, 5);
        if (getChipDrawable() != null) {
            getChipDrawable().setTextSize(getTextSize());
        }
    }

    public void setUseMaterialEffectAttr(MaterialEffectState materialEffectState) {
        if (UIUtil.confirmLevelAnim(UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN)) {
            COUIMaterialStateManager cOUIMaterialStateManager = COUIMaterialStateManager.INSTANCE;
            if (cOUIMaterialStateManager.materialVersionCheck() && cOUIMaterialStateManager.checkThemeEnable(getContext()) && this.mUseMaterialEffectAttr != materialEffectState.getValue()) {
                this.mUseMaterialEffectAttr = materialEffectState.getValue();
                COUIChipTabDrawable tabDrawable = getTabDrawable();
                if (tabDrawable != null) {
                    tabDrawable.setUseMaterialEffectAttr(this.mUseMaterialEffectAttr);
                }
                applyMaterialEffect();
                applyShadow((isCheckable() && isChecked()) ? 1.0f : 0.0f);
                invalidate();
            }
        }
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void toggle() {
        if (!isChecked()) {
            super.toggle();
        } else if (DEBUG) {
            Log.d(TAG, "已选中状态，阻止 toggle 操作");
        }
    }

    public COUIChipTab(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiChipStyle);
    }

    public COUIChipTab(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, DEF_STYLE_RES);
    }

    public COUIChipTab(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mOplusOutLineRect = new Rect();
        boolean z2 = false;
        this.mShowIconOnly = false;
        this.mSpotLight = null;
        this.mUseMaterialEffectAttr = MaterialEffectState.FOLLOW_SYSTEM.getValue();
        this.mMaterialEffectType = MaterialEffectType.TYPE_DEFAULT;
        this.mIsBlurEffectEnabled = false;
        this.mIsStrokeEffectEnabled = false;
        this.mIsSpotLightEffectEnabled = false;
        this.mIsCausticShadowEffectEnabled = false;
        this.mCheckedLayerAlpha = 0.0f;
        getPaint().setLinearText(true);
        getPaint().setSubpixelText(true);
        COUIChipTabDrawable cOUIChipTabDrawableCreateFromAttributes = COUIChipTabDrawable.createFromAttributes(context, attributeSet, i3, i4);
        this.mUseMaterialEffectAttr = cOUIChipTabDrawableCreateFromAttributes.getUseMaterialEffectAttr();
        setChipDrawable(cOUIChipTabDrawableCreateFromAttributes);
        cOUIChipTabDrawableCreateFromAttributes.setUncheckedBackgroundColorFromTheme(context);
        int attrColor = COUIContextUtil.getAttrColor(context, com.support.appcompat.R.attr.couiColorLabelPrimary);
        setUncheckedTextColor(attrColor);
        setCheckedTextColor(attrColor);
        int attrColor2 = COUIContextUtil.getAttrColor(context, com.support.appcompat.R.attr.couiColorLabelTertiary);
        setUncheckedDisabledTextColor(attrColor2);
        setCheckedDisabledTextColor(attrColor2);
        setUncheckedDisabledChipIconTint(attrColor2);
        setCheckedDisabledChipIconTint(attrColor2);
        setChipCornerRadius(dpToPx(16.0f));
        setOutlineProvider(new AnonymousClass1());
        setClipToOutline(true);
        cOUIChipTabDrawableCreateFromAttributes.setOnCheckedLayerAlphaChangeListener(new k(this));
        if (isCheckable() && isChecked()) {
            z2 = true;
        }
        updateShadowByCheckedState(z2);
        setElevation(0.0f);
        this.mSpotLight = new COUISpotLightEffectDrawable();
        UIUtil.setViewShadowClippingEnabled(this, true);
        updateClipExpandForShadow();
    }
}