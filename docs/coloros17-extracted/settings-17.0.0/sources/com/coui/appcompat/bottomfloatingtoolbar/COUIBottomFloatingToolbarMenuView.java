package com.coui.appcompat.bottomfloatingtoolbar;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.TextView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.ViewCompat;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.COUIMaterialBlurEffect;
import com.coui.appcompat.COUIMaterialEffect;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.COUIMaterialStrokeEffect;
import com.coui.appcompat.OnMaterialStateChangeListener;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.animation.touchanimation.COUIIComposeMotionUnit;
import com.coui.appcompat.animation.touchanimation.COUITouchMotionAnimator;
import com.coui.appcompat.animation.touchanimation.OnAnimationEndListener;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.shadowedge.COUIShadowEdgeDrawable;
import com.coui.appcompat.shadowedge.IShadowEdgeEffect;
import com.coui.appcompat.spotlight.COUISpotLightEffect;
import com.coui.appcompat.spotlight.COUISpotLightEffectDrawable;
import com.coui.appcompat.uiutil.ShadowUtils;
import com.coui.appcompat.uiutil.UIUtil;
import com.oplus.graphics.OplusPathAdapter;
import com.oplus.view.material.OplusMaterialBaseParams;
import com.oplus.view.material.OplusMaterialCornerParams;
import com.support.appcompat.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/* JADX INFO: loaded from: classes7.dex */
class COUIBottomFloatingToolbarMenuView extends ViewGroup implements IBottomFloatingToolbarMenuViewAnimator, IBottomFloatingToolbarItemViewAnimator, OnMaterialStateChangeListener {
    private static final float COLOR_CHANNEL_MAX = 255.0f;
    private static final int MAX_ITEM = 7;
    private static final int PANEL_FALLBACK_OVERLAY_COLOR_DARK = -13421773;
    private static final int PANEL_FALLBACK_OVERLAY_COLOR_LIGHT = -986896;
    private static final boolean SHADER_TOUCH_FEEDBACK_ENABLED = false;
    private static final int SHADOW_EDGE_CORNER_TYPE_G2 = 2;
    private static final float SINGLE_ITEM_CORNER_WEIGHT = 2.0f;
    private static final float SMOOTH_CORNER_FACTOR = 1.0f;
    private static final String TAG = "COUIBottomFloatingToolbarMenuView";
    private static final float TOUCH_EFFECT_ALPHA_OPAQUE = 255.0f;
    private static final float TOUCH_EFFECT_ALPHA_SPRING_BOUNCE = 0.0f;
    private static final float TOUCH_EFFECT_ALPHA_SPRING_RESPONSE = 0.2f;
    private static final int TOUCH_EFFECT_CENTER_ALPHA = 637534208;
    private static final int TOUCH_EFFECT_LIGHT_COLOR = -1;
    private final RectF mBackgroundBounds;
    private final Path mBackgroundPath;
    private final Rect mBackgroundRect;
    private final COUIBottomFloatingToolbar.SmoothRoundCornerHelper mBackgroundSmoothRoundCornerHelper;
    final ArrayList<View> mChildList;
    private COUIBottomFloatingToolbar.ItemClickCallback mClickCallBack;
    private final View.OnClickListener mClickListener;
    private List<COUIBottomFloatingToolbarItem> mContentReplaceItemsList;
    private Context mContext;
    boolean mDoAnimation;
    boolean mDoItemAnimation;
    protected int mGroupId;
    private final ArrayList<View> mIncomingItemList;
    private Map<COUIBottomFloatingToolbarItem, Integer> mInsertItemsMap;
    private boolean mIsDrawableMaterialEffect;
    private List<COUIBottomFloatingToolbarItem> mItemBeanList;
    private final ArrayList<View> mItemRemoving;
    protected ItemViewAnimatorImpl mItemViewAnimatorImpl;
    private ArrayList<View> mItemViewList;
    int mItemViewSpace;
    final ArrayList<View> mLastChild;
    private int mLastOrientation;
    private int mLastScreenWidthDp;
    boolean mLayoutAnim;
    private OplusMaterialBaseParams mMaterialBaseParams;
    private MaterialEffectManager mMaterialEffectManager;
    final ArrayList<View> mMeasuredChild;
    protected MenuViewAnimatorImpl mMenuViewAnimatorImpl;
    private final int mMenuViewHeight;
    int mMenuViewItemBgInsetHorizontal;
    private final int mMenuViewItemBgInsetHorizontalDefault;
    private boolean mMenuViewMaterial;
    private String mMoreOptionsDescription;
    private int mOldWidth;
    private OplusMaterialCornerParams mOplusMaterialCornerParams;
    COUIBottomFloatingToolbarItemView mOverflowItem;
    private ColorDrawable mPanelFallbackOverlayDrawable;
    boolean mPendingRollback;
    private float mRadius;
    private List<COUIBottomFloatingToolbarItem> mRemoveItemsList;
    private boolean mScreenSwitch;
    private COUIShadowEdgeDrawable mShadowEdgeDrawable;
    private COUISpotLightEffectDrawable mSpotLightEffectDrawable;
    private boolean mStrokeAndInnerShadowEnabled;
    private COUISpringAnimation mTouchEffectAlphaAnim;
    private Matrix mTouchEffectMatrix;
    private Paint mTouchEffectPaint;
    private float mTouchEffectRadius;
    private Shader mTouchEffectShader;
    private COUITouchMotionAnimator mTouchMotionAnimator;
    private float mTouchX;
    private float mTouchY;
    private ArrayList<View> mTransitioningViews;
    private ColorDrawable mTransparentBackgroundDrawable;
    private int mUseMaterialEffectAttr;

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingtoolbar.COUIBottomFloatingToolbarMenuView$1, reason: invalid class name */
    public class AnonymousClass1 extends ViewOutlineProvider {
        public AnonymousClass1() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setPath(COUIBottomFloatingToolbarMenuView.access$000(COUIBottomFloatingToolbarMenuView.this));
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingtoolbar.COUIBottomFloatingToolbarMenuView$2, reason: invalid class name */
    public class AnonymousClass2 implements COUIDynamicAnimation.OnAnimationUpdateListener {
        public AnonymousClass2() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
        public void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
            if (COUIBottomFloatingToolbarMenuView.access$400(COUIBottomFloatingToolbarMenuView.this) != null) {
                COUIBottomFloatingToolbarMenuView.access$400(COUIBottomFloatingToolbarMenuView.this).setAlpha((int) f3);
                COUIBottomFloatingToolbarMenuView.this.invalidate();
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingtoolbar.COUIBottomFloatingToolbarMenuView$3, reason: invalid class name */
    public class AnonymousClass3 implements OnAnimationEndListener {
        public AnonymousClass3() {
        }

        @Override // com.coui.appcompat.animation.touchanimation.OnAnimationEndListener
        public void onAnimationEnd(COUIIComposeMotionUnit cOUIIComposeMotionUnit, boolean z2, float f3, float f4) {
            COUIBottomFloatingToolbarMenuView.access$500(COUIBottomFloatingToolbarMenuView.this, false);
        }
    }

    public COUIBottomFloatingToolbarMenuView(Context context) {
        this(context, (AttributeSet) null);
    }

    public static /* synthetic */ void a(COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView, COUIIComposeMotionUnit.ContributeSink contributeSink) {
        cOUIBottomFloatingToolbarMenuView.getClass();
        cOUIBottomFloatingToolbarMenuView.setTranslationX(contributeSink.getTranslateX());
        cOUIBottomFloatingToolbarMenuView.setTranslationY(contributeSink.getTranslateY());
        cOUIBottomFloatingToolbarMenuView.setScaleX(contributeSink.getScaleX());
        cOUIBottomFloatingToolbarMenuView.setScaleY(contributeSink.getScaleY());
        cOUIBottomFloatingToolbarMenuView.invalidate();
    }

    public static /* synthetic */ Path access$000(COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView) {
        return cOUIBottomFloatingToolbarMenuView.mBackgroundPath;
    }

    public static /* synthetic */ Rect access$100(COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView) {
        return cOUIBottomFloatingToolbarMenuView.mBackgroundRect;
    }

    public static /* synthetic */ RectF access$200(COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView) {
        return cOUIBottomFloatingToolbarMenuView.mBackgroundBounds;
    }

    public static /* synthetic */ float access$300(COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView) {
        return cOUIBottomFloatingToolbarMenuView.mRadius;
    }

    public static /* synthetic */ Paint access$400(COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView) {
        return cOUIBottomFloatingToolbarMenuView.mTouchEffectPaint;
    }

    public static /* synthetic */ void access$500(COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView, boolean z2) {
        cOUIBottomFloatingToolbarMenuView.setItemTextPreventsShakeForAllChildren(z2);
    }

    private void applyPanelBackground() {
        int i3 = COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(getContext()) ? PANEL_FALLBACK_OVERLAY_COLOR_DARK : PANEL_FALLBACK_OVERLAY_COLOR_LIGHT;
        ColorDrawable colorDrawable = this.mPanelFallbackOverlayDrawable;
        if (colorDrawable == null) {
            this.mPanelFallbackOverlayDrawable = new ColorDrawable(i3);
        } else {
            colorDrawable.setColor(i3);
        }
        setBackground(this.mPanelFallbackOverlayDrawable);
    }

    private void applyPanelMaterialEffectState() {
        updateBlurEffect();
        updateStrokeEffect();
        updateOuterShadow();
        applySmoothCornerOutlineState();
        updateTouchEffect();
        invalidate();
    }

    private void applySmoothCornerOutlineState() {
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable;
        if (BottomFloatingToolbarUtil.isOs17OrAbove()) {
            if (this.mIsDrawableMaterialEffect) {
                setClipToOutline(true);
                if (!isStrokeEffectEnabled() || (cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable) == null) {
                    return;
                }
                cOUIShadowEdgeDrawable.setWeight(computeMaterialCornerWeight());
                return;
            }
            if (!isMaterialQualityEnable()) {
                setClipToOutline(true);
                this.mOplusMaterialCornerParams = null;
                return;
            }
            setClipToOutline(false);
            OplusMaterialCornerParams oplusMaterialCornerParams = this.mOplusMaterialCornerParams;
            if (oplusMaterialCornerParams == null) {
                this.mOplusMaterialCornerParams = new OplusMaterialCornerParams(this.mRadius, computeMaterialCornerWeight());
            } else {
                oplusMaterialCornerParams.setRadius(this.mRadius);
                this.mOplusMaterialCornerParams.setWeight(computeMaterialCornerWeight());
            }
            COUIMaterialEffect.setCornerParams(this, this.mOplusMaterialCornerParams);
        }
    }

    private void applySpotLightColor(COUISpotLightEffectDrawable cOUISpotLightEffectDrawable, int i3) {
        cOUISpotLightEffectDrawable.setLightColor(Color.red(i3) / 255.0f, Color.green(i3) / 255.0f, Color.blue(i3) / 255.0f);
    }

    private void applyTextPreventsShakeRecursively(View view, boolean z2) {
        if (view instanceof TextView) {
            applyTextViewPreventsShake((TextView) view, z2);
            return;
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                applyTextPreventsShakeRecursively(viewGroup.getChildAt(i3), z2);
            }
        }
    }

    private void applyTextViewPreventsShake(TextView textView, boolean z2) {
        textView.setLayerType(z2 ? 2 : 0, null);
        textView.getPaint().setLinearText(z2);
        textView.getPaint().setSubpixelText(z2);
    }

    private void applyTouchEffect(MotionEvent motionEvent) {
        if (COUIMaterialStateManager.INSTANCE.isFeatureEnabled() && isSpotLightEffectEnable()) {
            handleTouchEffectDrawableEvent(motionEvent);
        }
    }

    public static /* synthetic */ void b(COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView, View view) {
        COUIBottomFloatingToolbar.ItemClickCallback itemClickCallback;
        cOUIBottomFloatingToolbarMenuView.getClass();
        if (!(view instanceof COUIBottomFloatingToolbarItemView) || (itemClickCallback = cOUIBottomFloatingToolbarMenuView.mClickCallBack) == null) {
            return;
        }
        itemClickCallback.onItemViewClick((COUIBottomFloatingToolbarItemView) view);
    }

    private void changeBackground() {
        MaterialEffectManager materialEffectManager;
        if (BottomFloatingToolbarUtil.isOs17OrAbove()) {
            return;
        }
        if (!this.mMenuViewMaterial || (materialEffectManager = this.mMaterialEffectManager) == null) {
            setBackgroundColor(COUIContextUtil.getAttrColor(this.mContext, R.attr.couiColorBar));
        } else {
            materialEffectManager.applyMaterialBackground(this);
        }
    }

    private boolean checkItemExist(int i3) {
        Iterator<COUIBottomFloatingToolbarItem> it = this.mItemBeanList.iterator();
        while (it.hasNext()) {
            if (it.next().getId() == i3) {
                return true;
            }
        }
        return false;
    }

    private int computeIncomingVisibleCount() {
        int i3 = 0;
        for (int i4 = 0; i4 < this.mIncomingItemList.size(); i4++) {
            if (this.mIncomingItemList.get(i4).getVisibility() != 8) {
                i3++;
            }
        }
        return i3;
    }

    private float computeMaterialCornerWeight() {
        ArrayList<View> arrayList = !this.mMeasuredChild.isEmpty() ? this.mMeasuredChild : this.mIncomingItemList;
        View view = null;
        int i3 = 0;
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            View view2 = arrayList.get(i4);
            if (view2.getVisibility() != 8) {
                i3++;
                view = view2;
            }
        }
        return (i3 == 1 && (view instanceof COUIBottomFloatingToolbarItemView)) ? 2.0f : 1.0f;
    }

    private View createItemView(COUIBottomFloatingToolbarItem cOUIBottomFloatingToolbarItem) {
        if (cOUIBottomFloatingToolbarItem.getView() != null) {
            return cOUIBottomFloatingToolbarItem.getView();
        }
        COUIBottomFloatingToolbarItemView cOUIBottomFloatingToolbarItemView = new COUIBottomFloatingToolbarItemView(this.mContext);
        cOUIBottomFloatingToolbarItemView.setOnClickListener(this.mClickListener);
        cOUIBottomFloatingToolbarItemView.apply(cOUIBottomFloatingToolbarItem);
        return cOUIBottomFloatingToolbarItemView;
    }

    private void createOverflowItem() {
        this.mOverflowItem = new COUIBottomFloatingToolbarItemView(this.mContext);
        this.mOverflowItem.setLayoutParams(new ViewGroup.LayoutParams(getResources().getDimensionPixelSize(com.support.bottomnavigation.R.dimen.coui_floating_toolbar_item_width), getResources().getDimensionPixelSize(com.support.bottomnavigation.R.dimen.coui_floating_toolbar_item_height)));
        this.mOverflowItem.setId(com.support.bottomnavigation.R.id.floating_toolbar_group_more);
        this.mOverflowItem.setIcon(AppCompatResources.getDrawable(this.mContext, com.support.bottomnavigation.R.drawable.coui_floating_toolbar_more));
        this.mOverflowItem.setTitle(this.mMoreOptionsDescription);
        this.mOverflowItem.setOnClickListener(this.mClickListener);
    }

    private RadialGradient createTouchEffectRadialGradient(int i3) {
        int i4 = i3 & 16777215;
        return new RadialGradient(0.0f, 0.0f, this.mTouchEffectRadius, i4 | TOUCH_EFFECT_CENTER_ALPHA, i4, Shader.TileMode.CLAMP);
    }

    private void drawPanelStroke(Canvas canvas) {
        if (isStrokeEffectEnabled() && this.mIsDrawableMaterialEffect && this.mShadowEdgeDrawable != null) {
            float fWidth = this.mBackgroundBounds.width();
            float fHeight = this.mBackgroundBounds.height();
            if (fWidth <= 0.0f || fHeight <= 0.0f) {
                return;
            }
            this.mShadowEdgeDrawable.setCornerRadius(fHeight / 2.0f);
            this.mShadowEdgeDrawable.setBounds(0, 0, Math.round(fWidth), Math.round(fHeight));
            int iSave = canvas.save();
            canvas.translate(getScrollX() + this.mBackgroundBounds.left, getScrollY() + this.mBackgroundBounds.top);
            this.mShadowEdgeDrawable.draw(canvas);
            canvas.restoreToCount(iSave);
        }
    }

    private void drawTouchEffectDrawable(Canvas canvas) {
        if (BottomFloatingToolbarUtil.isOs17OrAbove() && this.mSpotLightEffectDrawable != null && isSpotLightPressEffectEnabled()) {
            int iSave = canvas.save();
            canvas.clipPath(this.mBackgroundPath);
            this.mSpotLightEffectDrawable.draw(canvas);
            canvas.restoreToCount(iSave);
        }
    }

    private void ensureMenuViewAnimatorImpl() {
        if (this.mMenuViewAnimatorImpl == null) {
            this.mMenuViewAnimatorImpl = new MenuViewAnimatorImpl(this);
        }
    }

    private void handleTouchEffectDrawableEvent(MotionEvent motionEvent) {
        this.mSpotLightEffectDrawable.onTouchEvent(motionEvent);
    }

    private void handleTouchEffectShaderEvent(MotionEvent motionEvent) {
        Paint paint;
        COUISpringAnimation cOUISpringAnimation;
        int action = motionEvent.getAction();
        float x2 = motionEvent.getX();
        float y2 = motionEvent.getY();
        boolean zIsTouchInBounds = isTouchInBounds(x2, y2);
        if (action != 0) {
            if (action == 2) {
                updateTouchPosition(x2, y2);
            }
        } else {
            if (!zIsTouchInBounds || (paint = this.mTouchEffectPaint) == null || (cOUISpringAnimation = this.mTouchEffectAlphaAnim) == null) {
                return;
            }
            cOUISpringAnimation.setStartValue(paint.getAlpha());
            this.mTouchEffectAlphaAnim.animateToFinalPosition(255.0f);
            updateTouchPosition(x2, y2);
        }
    }

    private void handleTouchRelease() {
        isSpotLightEffectEnable();
    }

    private void initConfigInfo() {
        Configuration configuration = getResources().getConfiguration();
        this.mLastOrientation = configuration.orientation;
        this.mLastScreenWidthDp = configuration.screenWidthDp;
    }

    private void initOs17SystemMaterial() {
        updateOuterShadow();
        updateStrokeEffect();
        updateBlurEffect();
    }

    private void initPressTouchEffectIfSupported() {
        if (BottomFloatingToolbarUtil.isOs17OrAbove()) {
            this.mTouchEffectRadius = getResources().getDimensionPixelSize(com.support.bottomnavigation.R.dimen.coui_floating_toolbar_menu_touch_effect_radius);
            initTouchEffectAlphaAnim();
            updateTouchEffect();
        }
    }

    private void initShadowEdgeDrawableIfNeeded() {
        if (COUIMaterialEffect.isMaterialVersion() && !COUIMaterialStateManager.INSTANCE.isLowAnimationLevel() && this.mShadowEdgeDrawable == null) {
            this.mShadowEdgeDrawable = new COUIShadowEdgeDrawable();
            Context context = getContext();
            boolean zIsCOUIDarkThemeOrNightMode = COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(context);
            COUIMaterialStrokeEffect.Companion.StrokeEffectParams strokeParamsByType = COUIMaterialEffect.getStrokeParamsByType(context, COUIMaterialStrokeEffect.StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_13);
            this.mShadowEdgeDrawable.setEdgeEnable(true).setShadowEnable(true).setEdgeStyle(IShadowEdgeEffect.EdgeStyle.EDGE_STYLE_0).setShadowStyle(IShadowEdgeEffect.ShadowStyle.SHADOW_STYLE_0).setLineAlpha(strokeParamsByType.getEdgeAlpha()).setLineWidth(strokeParamsByType.getEdgeWidth(context)).setLineAngle(strokeParamsByType.getEdgeAngle()).setFadeAlpha(strokeParamsByType.getShadowFadeIn(), strokeParamsByType.getShadowFadeOut()).setFadeWidthScale(strokeParamsByType.getShadowFadeScale()).setIsDarkMode(zIsCOUIDarkThemeOrNightMode).setCornerType(2).setPaintBlendMode(BlendMode.SRC_OVER);
        }
    }

    private void initTouchEffectAlphaAnim() {
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(0.0f);
        cOUISpringForce.setResponse(0.2f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mTouchEffectAlphaAnim = spring;
        spring.addUpdateListener(new AnonymousClass2());
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

    private void initTouchMotionAnimIfSupported() {
        if (BottomFloatingToolbarUtil.isOs17OrAbove()) {
            COUITouchMotionAnimator cOUITouchMotionAnimator = new COUITouchMotionAnimator(getContext());
            this.mTouchMotionAnimator = cOUITouchMotionAnimator;
            cOUITouchMotionAnimator.setDeformationEnable(true).setRubberEnable(true).setPressScaleEnable(true).addOnAnimationUpdateCallback(new e(this));
            this.mTouchMotionAnimator.addEndListener(new AnonymousClass3());
        }
    }

    private boolean isBlurEffectEnabled() {
        if (!this.mIsDrawableMaterialEffect) {
            return COUIMaterialStateManager.INSTANCE.isBlurEffectEnable();
        }
        if (this.mUseMaterialEffectAttr == COUIBottomFloatingToolbar.MaterialEffectState.FORCE_ENABLE.getValue()) {
            return true;
        }
        if (this.mUseMaterialEffectAttr == COUIBottomFloatingToolbar.MaterialEffectState.FORCE_DISABLE.getValue()) {
            return false;
        }
        return COUIMaterialStateManager.INSTANCE.isBlurEffectEnable();
    }

    private boolean isCausticShadowEffectEnabled() {
        if (!this.mIsDrawableMaterialEffect) {
            return COUIMaterialStateManager.INSTANCE.isCausticShadowEffectEnable();
        }
        if (this.mUseMaterialEffectAttr == COUIBottomFloatingToolbar.MaterialEffectState.FORCE_ENABLE.getValue()) {
            return COUIMaterialStateManager.INSTANCE.isFeatureEnabled();
        }
        if (this.mUseMaterialEffectAttr == COUIBottomFloatingToolbar.MaterialEffectState.FORCE_DISABLE.getValue()) {
            return false;
        }
        return COUIMaterialStateManager.INSTANCE.isCausticShadowEffectEnable();
    }

    private boolean isIdle() {
        ItemViewAnimatorImpl itemViewAnimatorImpl = this.mItemViewAnimatorImpl;
        return itemViewAnimatorImpl == null || itemViewAnimatorImpl.getCurState() == 0;
    }

    private boolean isMaterialQualityEnable() {
        return isBlurEffectEnabled();
    }

    private boolean isPending() {
        ItemViewAnimatorImpl itemViewAnimatorImpl = this.mItemViewAnimatorImpl;
        return itemViewAnimatorImpl != null && itemViewAnimatorImpl.getCurState() == 1;
    }

    private boolean isPendingOrRunningWithHorizontal() {
        if (this.mMenuViewAnimatorImpl.getNotHorizontalAnim()) {
            return isPending() || isRunning();
        }
        return false;
    }

    private boolean isPendingWithHorizontal() {
        return !this.mMenuViewAnimatorImpl.getNotHorizontalAnim() && isPending();
    }

    private boolean isRunning() {
        ItemViewAnimatorImpl itemViewAnimatorImpl = this.mItemViewAnimatorImpl;
        return itemViewAnimatorImpl != null && itemViewAnimatorImpl.getCurState() == 2;
    }

    private boolean isSpotLightEffectEnable() {
        if (!this.mIsDrawableMaterialEffect) {
            return COUIMaterialStateManager.INSTANCE.isSpotLightEffectEnable();
        }
        if (this.mUseMaterialEffectAttr == COUIBottomFloatingToolbar.MaterialEffectState.FORCE_ENABLE.getValue()) {
            return COUIMaterialStateManager.INSTANCE.isFeatureEnabled();
        }
        if (this.mUseMaterialEffectAttr == COUIBottomFloatingToolbar.MaterialEffectState.FORCE_DISABLE.getValue()) {
            return false;
        }
        return COUIMaterialStateManager.INSTANCE.isSpotLightEffectEnable();
    }

    private boolean isSpotLightPressEffectEnabled() {
        return isSpotLightEffectEnable();
    }

    private boolean isStrokeEffectEnabled() {
        if (!this.mIsDrawableMaterialEffect) {
            return COUIMaterialStateManager.INSTANCE.isStrokeEffectEnable();
        }
        if (this.mUseMaterialEffectAttr == COUIBottomFloatingToolbar.MaterialEffectState.FORCE_ENABLE.getValue()) {
            return true;
        }
        if (this.mUseMaterialEffectAttr == COUIBottomFloatingToolbar.MaterialEffectState.FORCE_DISABLE.getValue()) {
            return false;
        }
        return COUIMaterialStateManager.INSTANCE.isStrokeEffectEnable();
    }

    private boolean isTouchInBounds(float f3, float f4) {
        int width = getWidth();
        int height = getHeight();
        return width > 0 && height > 0 && f3 >= 0.0f && f3 <= ((float) width) && f4 >= 0.0f && f4 <= ((float) height);
    }

    private int layoutChildHorizontally(View view, int i3, int i4, float f3) {
        int iMax = Math.max(getPaddingTop(), getPaddingTop() + ((i4 - view.getMeasuredHeight()) / 2));
        view.layout(i3, iMax, view.getMeasuredWidth() + i3, view.getMeasuredHeight() + iMax);
        return (int) (i3 + view.getMeasuredWidth() + f3);
    }

    private void onLayoutMenuItems(int i3, int i4, int i5, int i6) {
        int paddingLeft = getPaddingLeft() + this.mMenuViewItemBgInsetHorizontal;
        int i7 = isRtl() ? -1 : 1;
        float f3 = this.mItemViewSpace;
        for (int size = isRtl() ? this.mMeasuredChild.size() - 1 : 0; size >= 0 && size < this.mMeasuredChild.size(); size += i7) {
            paddingLeft = layoutChildHorizontally(this.mMeasuredChild.get(size), paddingLeft, ((i6 - i4) - getPaddingTop()) - getPaddingBottom(), f3);
        }
    }

    private void refreshSpotLightPressEffect() {
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLightEffectDrawable;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setEnabled(isSpotLightPressEffectEnabled());
        }
    }

    private int resolveTouchEffectLightColor() {
        return -1;
    }

    private void setCausticShadowEffect() {
        ShadowUtils.setElevationToView(this, 7);
        COUIMaterialEffect.setOutlineCausticShadowColor(this, getResources().getColor(com.coui.appcompat.effect.R.color.coui_caustic_color_2));
        COUIMaterialEffect.setOutlineCausticShadowLayout(this, getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_l_level3), getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_t_level3), getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_r_level3), getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_b_level3));
        COUIMaterialEffect.setOutlineCausticShadowParams(this, getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_sdf_level3));
    }

    private void setItemTextPreventsShakeForAllChildren(boolean z2) {
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            if (childAt instanceof COUIBottomFloatingToolbarItemView) {
                ((COUIBottomFloatingToolbarItemView) childAt).setItemTextPreventsShake(z2);
            } else {
                applyTextPreventsShakeRecursively(childAt, z2);
            }
        }
    }

    private void setTouchMotionAnim(MotionEvent motionEvent) {
        COUITouchMotionAnimator cOUITouchMotionAnimator = this.mTouchMotionAnimator;
        if (cOUITouchMotionAnimator != null) {
            cOUITouchMotionAnimator.handleTouchEvent(motionEvent);
        }
    }

    private boolean shouldPreserveAnimDuringLayout() {
        ItemViewAnimatorImpl itemViewAnimatorImpl;
        return this.mDoAnimation && (itemViewAnimatorImpl = this.mItemViewAnimatorImpl) != null && itemViewAnimatorImpl.isSessionActive() && this.mItemViewAnimatorImpl.matchesSessionTarget(this.mMeasuredChild);
    }

    private boolean shouldSyncOffDraw() {
        return (isShown() && getWindowVisibility() == 0) ? false : true;
    }

    private void startMenuItemViewTransition(View view) {
        if (this.mTransitioningViews == null) {
            this.mTransitioningViews = new ArrayList<>();
        }
        if (this.mTransitioningViews.contains(view)) {
            return;
        }
        this.mTransitioningViews.add(view);
        startViewTransition(view);
    }

    private void updateAndDrawBackgroundRect() {
        this.mBackgroundBounds.setEmpty();
        List<View> animatingViews = getAnimatingViews();
        View childAt = isRtl() ? getChildAt(getChildCount() - 1) : getChildAt(0);
        View childAt2 = isRtl() ? getChildAt(0) : getChildAt(getChildCount() - 1);
        if (!animatingViews.isEmpty()) {
            childAt = isRtl() ? animatingViews.get(animatingViews.size() - 1) : animatingViews.get(0);
            childAt2 = isRtl() ? animatingViews.get(0) : animatingViews.get(animatingViews.size() - 1);
        }
        if (childAt == null) {
            return;
        }
        this.mBackgroundBounds.top = getPaddingTop();
        this.mBackgroundBounds.bottom = getHeight() - getPaddingBottom();
        this.mBackgroundBounds.left = (childAt.getX() + ((childAt.getMeasuredWidth() * Math.max(0.0f, 1.0f - childAt.getScaleX())) / 2.0f)) - this.mMenuViewItemBgInsetHorizontal;
        float measuredWidth = (childAt2.getMeasuredWidth() * (1.0f - childAt2.getScaleX())) / 2.0f;
        if (childAt2.getScaleX() <= 0.0f) {
            this.mBackgroundBounds.right = childAt2.getX() + measuredWidth + this.mMenuViewItemBgInsetHorizontal;
        } else {
            this.mBackgroundBounds.right = ((childAt2.getX() + childAt2.getMeasuredWidth()) - measuredWidth) + this.mMenuViewItemBgInsetHorizontal;
        }
        ViewGroup viewGroup = (ViewGroup) getParent();
        if (viewGroup instanceof COUIBottomFloatingToolbar) {
            COUIBottomFloatingToolbar cOUIBottomFloatingToolbar = (COUIBottomFloatingToolbar) viewGroup;
            boolean zIsPendingOrRunningWithHorizontal = isPendingOrRunningWithHorizontal();
            boolean zIsPendingWithHorizontal = isPendingWithHorizontal();
            boolean zShouldMenuViewAlignRight = cOUIBottomFloatingToolbar.shouldMenuViewAlignRight(this);
            boolean zShouldMenuViewAlignCenter = cOUIBottomFloatingToolbar.shouldMenuViewAlignCenter(this);
            boolean zShouldMenuViewAlignLeft = cOUIBottomFloatingToolbar.shouldMenuViewAlignLeft(this);
            boolean z2 = zIsPendingOrRunningWithHorizontal || zIsPendingWithHorizontal;
            if (z2 && zShouldMenuViewAlignRight) {
                this.mMenuViewAnimatorImpl.getDrawingBounds().right = viewGroup.getWidth() - viewGroup.getPaddingRight();
                this.mMenuViewAnimatorImpl.getDrawingBounds().left = this.mMenuViewAnimatorImpl.getDrawingBounds().right - this.mBackgroundBounds.right;
                setX(this.mMenuViewAnimatorImpl.getDrawingBounds().left);
            }
            if (z2 && zShouldMenuViewAlignCenter) {
                this.mMenuViewAnimatorImpl.getDrawingBounds().left = ((viewGroup.getWidth() / 2.0f) - (this.mBackgroundBounds.width() / 2.0f)) - this.mBackgroundBounds.left;
                this.mMenuViewAnimatorImpl.getDrawingBounds().right = this.mMenuViewAnimatorImpl.getDrawingBounds().left + this.mMenuViewAnimatorImpl.getRealBounds().width();
                setX(this.mMenuViewAnimatorImpl.getDrawingBounds().left);
            }
            if (z2 && zShouldMenuViewAlignLeft) {
                this.mMenuViewAnimatorImpl.getDrawingBounds().left = viewGroup.getPaddingLeft() - this.mBackgroundBounds.left;
                this.mMenuViewAnimatorImpl.getDrawingBounds().right = this.mMenuViewAnimatorImpl.getDrawingBounds().left + this.mBackgroundBounds.width();
                setX(this.mMenuViewAnimatorImpl.getDrawingBounds().left);
            }
        }
    }

    private void updateBlurEffect() {
        if (BottomFloatingToolbarUtil.isOs17OrAbove()) {
            if (this.mIsDrawableMaterialEffect) {
                COUIMaterialEffect.setViewBlurEffect(this, COUIMaterialBlurEffect.BlurEffectType.TYPE_NO_EFFECT);
                applyPanelBackground();
            } else if (!isMaterialQualityEnable()) {
                if (COUIMaterialStateManager.INSTANCE.materialVersionCheck()) {
                    COUIMaterialEffect.setViewBlurEffect(this, COUIMaterialBlurEffect.BlurEffectType.TYPE_NO_EFFECT);
                }
                setBackground(AppCompatResources.getDrawable(getContext(), com.support.bottomnavigation.R.drawable.coui_tab_navigation_view_bg));
            } else {
                COUIMaterialEffect.setViewBlurEffect(this, COUIMaterialBlurEffect.BlurEffectType.TYPE_FRAMEWORK_BOTTOM_BAR);
                if (this.mTransparentBackgroundDrawable == null) {
                    this.mTransparentBackgroundDrawable = new ColorDrawable(0);
                }
                setBackground(this.mTransparentBackgroundDrawable);
            }
        }
    }

    private void updateEdgeBounds() {
        if (BottomFloatingToolbarUtil.isOs17OrAbove() && COUIMaterialStateManager.INSTANCE.isBlurEffectEnable() && !this.mBackgroundBounds.isEmpty()) {
            Rect rect = this.mBackgroundRect;
            RectF rectF = this.mBackgroundBounds;
            rect.set((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
            if (this.mMaterialBaseParams == null) {
                this.mMaterialBaseParams = new OplusMaterialBaseParams(0, (Rect) null);
            }
            this.mMaterialBaseParams.setInnerBounds(this.mBackgroundRect);
            COUIMaterialEffect.setBaseParams(this, this.mMaterialBaseParams);
        }
    }

    private void updateEdgeDrawable() {
        COUIMaterialEffect.setViewStrokeEffect(this, COUIMaterialStrokeEffect.StrokeEffectType.TYPE_NO_EFFECT_BASE_1);
        if (this.mShadowEdgeDrawable == null) {
            initShadowEdgeDrawableIfNeeded();
        }
        invalidate();
    }

    private void updateFloatingToolbarMaskOutsetForChildren() {
        boolean z2 = computeIncomingVisibleCount() <= 1;
        List<View> itemViewList = getItemViewList();
        for (int i3 = 0; i3 < itemViewList.size(); i3++) {
            View view = itemViewList.get(i3);
            if (view instanceof COUIBottomFloatingToolbarItemView) {
                COUIBottomFloatingToolbarItemView cOUIBottomFloatingToolbarItemView = (COUIBottomFloatingToolbarItemView) view;
                if (cOUIBottomFloatingToolbarItemView.getItemStyle() == 0) {
                    cOUIBottomFloatingToolbarItemView.setSingleVisibleItemInMenu(z2);
                }
            }
        }
    }

    private void updateMaterialRoundedCorners() {
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable;
        if (BottomFloatingToolbarUtil.isOs17OrAbove()) {
            this.mRadius = ((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom()) / 2.0f;
            if (isStrokeEffectEnabled() && this.mIsDrawableMaterialEffect && (cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable) != null) {
                cOUIShadowEdgeDrawable.setCornerRadius(this.mRadius);
                this.mShadowEdgeDrawable.setBounds(0, 0, getWidth(), getHeight());
            } else if (isMaterialQualityEnable()) {
                if (this.mOplusMaterialCornerParams == null) {
                    this.mOplusMaterialCornerParams = new OplusMaterialCornerParams(this.mRadius, 1.0f);
                }
                this.mOplusMaterialCornerParams.setRadius(this.mRadius);
                COUIMaterialEffect.setCornerParams(this, this.mOplusMaterialCornerParams);
            }
        }
    }

    private void updateOuterShadow() {
        if (BottomFloatingToolbarUtil.isOs17OrAbove()) {
            UIUtil.setViewShadowClippingEnabled(this, isMaterialQualityEnable());
            if (isCausticShadowEffectEnabled()) {
                setCausticShadowEffect();
            } else {
                ShadowUtils.setElevationToView(this, 2, getContext().getResources().getDimensionPixelOffset(R.dimen.support_shadow_size_level_three), COUIContextUtil.getColor(getContext(), com.support.bottomnavigation.R.color.coui_bottom_floating_navigation_bar_menu_shadow_color));
                COUIMaterialEffect.clearCausticShadow(this);
            }
        }
    }

    private void updateSmoothRoundPath() {
        this.mRadius = ((getHeight() - getPaddingTop()) - getPaddingBottom()) / 2.0f;
        this.mBackgroundPath.reset();
        if (this.mBackgroundSmoothRoundCornerHelper.getSmoothStyleType() == 1) {
            OplusPathAdapter pathAdapter = this.mBackgroundSmoothRoundCornerHelper.getPathAdapter();
            RectF rectF = this.mBackgroundBounds;
            float f3 = this.mRadius;
            pathAdapter.addSmoothRoundRect(rectF, f3, f3, Path.Direction.CCW);
        } else {
            Path path = this.mBackgroundPath;
            RectF rectF2 = this.mBackgroundBounds;
            float f4 = this.mRadius;
            path.addRoundRect(rectF2, f4, f4, Path.Direction.CCW);
        }
        invalidateOutline();
    }

    private void updateStrokeEffect() {
        if (BottomFloatingToolbarUtil.isOs17OrAbove() && COUIMaterialStateManager.INSTANCE.materialVersionCheck()) {
            if (this.mIsDrawableMaterialEffect) {
                updateEdgeDrawable();
            } else if (isStrokeEffectEnabled()) {
                COUIMaterialEffect.setViewStrokeEffect(this, COUIMaterialStrokeEffect.StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_5);
            } else {
                COUIMaterialEffect.setViewStrokeEffect(this, COUIMaterialStrokeEffect.StrokeEffectType.TYPE_NO_EFFECT);
            }
        }
    }

    private void updateTouchEffect() {
        if (BottomFloatingToolbarUtil.isOs17OrAbove() && COUIMaterialStateManager.INSTANCE.isFeatureEnabled()) {
            isSpotLightEffectEnable();
            initTouchEffectDrawable();
            refreshSpotLightPressEffect();
        }
    }

    private void updateTouchPosition(float f3, float f4) {
        this.mTouchX = f3;
        this.mTouchY = f4;
        invalidate();
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarItemViewAnimator
    public void animationRollBack() {
        ensureItemViewAnimatorImpl();
        this.mItemViewAnimatorImpl.animationRollBack();
    }

    public void applyMaterialCornerWeight() {
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable;
        if (BottomFloatingToolbarUtil.isOs17OrAbove() && isMaterialQualityEnable()) {
            if (isStrokeEffectEnabled() && this.mIsDrawableMaterialEffect && (cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable) != null) {
                cOUIShadowEdgeDrawable.setWeight(computeMaterialCornerWeight());
            }
            OplusMaterialCornerParams oplusMaterialCornerParams = this.mOplusMaterialCornerParams;
            if (oplusMaterialCornerParams == null) {
                return;
            }
            oplusMaterialCornerParams.setWeight(computeMaterialCornerWeight());
            COUIMaterialEffect.setCornerParams(this, this.mOplusMaterialCornerParams);
        }
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarMenuViewAnimator
    public void bindController() {
        ensureMenuViewAnimatorImpl();
    }

    public boolean checkContentReplaceItemsData(COUIBottomFloatingToolbarItem cOUIBottomFloatingToolbarItem) {
        if (!checkItemExist(cOUIBottomFloatingToolbarItem.getId())) {
            return false;
        }
        this.mContentReplaceItemsList.add(cOUIBottomFloatingToolbarItem);
        return true;
    }

    public boolean checkInsertItemsData(COUIBottomFloatingToolbarItem cOUIBottomFloatingToolbarItem, int i3) {
        if (cOUIBottomFloatingToolbarItem.getGroupId() != this.mGroupId || checkItemExist(cOUIBottomFloatingToolbarItem.getId()) || i3 < 0) {
            return false;
        }
        this.mInsertItemsMap.put(cOUIBottomFloatingToolbarItem, Integer.valueOf(i3));
        return true;
    }

    public boolean checkRemoveItemsData(COUIBottomFloatingToolbarItem cOUIBottomFloatingToolbarItem) {
        if (this.mItemBeanList.size() <= 1 || !checkItemExist(cOUIBottomFloatingToolbarItem.getId())) {
            return false;
        }
        this.mRemoveItemsList.add(cOUIBottomFloatingToolbarItem);
        return true;
    }

    public void clearContentReplaceItemsData() {
        this.mContentReplaceItemsList.clear();
    }

    public void clearInsertItemsData() {
        this.mInsertItemsMap.clear();
    }

    public void clearRemoveItemsData() {
        this.mRemoveItemsList.clear();
    }

    public int computeMenuViewItemBgInsetHorizontal() {
        if (computeIncomingVisibleCount() <= 1) {
            return 0;
        }
        return this.mMenuViewItemBgInsetHorizontalDefault;
    }

    public void disableTouchMotionForHorizontalAnim() {
        COUITouchMotionAnimator cOUITouchMotionAnimator = this.mTouchMotionAnimator;
        if (cOUITouchMotionAnimator != null) {
            cOUITouchMotionAnimator.disable();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        isSpotLightEffectEnable();
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        setTouchMotionAnim(motionEvent);
        if (this.mSpotLightEffectDrawable != null) {
            applyTouchEffect(motionEvent);
        }
        int action = motionEvent.getAction();
        boolean zIsTouchInBounds = isTouchInBounds(motionEvent.getX(), motionEvent.getY());
        if (this.mTouchMotionAnimator != null && action == 0 && zIsTouchInBounds) {
            setItemTextPreventsShakeForAllChildren(true);
        }
        if (this.mSpotLightEffectDrawable != null && (action == 1 || action == 3)) {
            handleTouchRelease();
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public void enableTouchMotionAfterHorizontalAnim() {
        COUITouchMotionAnimator cOUITouchMotionAnimator = this.mTouchMotionAnimator;
        if (cOUITouchMotionAnimator != null) {
            cOUITouchMotionAnimator.enable();
        }
    }

    public void endMenuItemViewTransition(View view) {
        if (this.mTransitioningViews == null) {
            this.mTransitioningViews = new ArrayList<>();
        }
        this.mTransitioningViews.remove(view);
        endViewTransition(view);
    }

    public void ensureItemViewAnimatorImpl() {
        if (this.mItemViewAnimatorImpl == null) {
            this.mItemViewAnimatorImpl = new ItemViewAnimatorImpl(this);
        }
    }

    public int findIndexById(int i3) {
        for (int i4 = 0; i4 < this.mItemBeanList.size(); i4++) {
            if (this.mItemBeanList.get(i4).getId() == i3) {
                return i4;
            }
        }
        return 0;
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarItemViewAnimator
    public List<View> getAnimatingViews() {
        ItemViewAnimatorImpl itemViewAnimatorImpl = this.mItemViewAnimatorImpl;
        if (itemViewAnimatorImpl == null) {
            return this.mChildList;
        }
        List<View> animatingViews = itemViewAnimatorImpl.getAnimatingViews();
        return animatingViews.isEmpty() ? this.mChildList : animatingViews;
    }

    public RectF getBounds() {
        return this.mBackgroundBounds;
    }

    public List<COUIBottomFloatingToolbarItem> getItemBenList() {
        return this.mItemBeanList;
    }

    public List<View> getItemViewList() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.mMeasuredChild);
        arrayList.addAll(this.mItemRemoving);
        return arrayList;
    }

    public int getVisibleViewsSize() {
        return this.mChildList.contains(this.mOverflowItem) ? this.mChildList.size() - 1 : this.mChildList.size();
    }

    public void handleAnimation(boolean z2) {
        this.mDoAnimation = z2;
    }

    public void handleContentReplaceAction() {
        if (this.mContentReplaceItemsList.isEmpty()) {
            return;
        }
        for (COUIBottomFloatingToolbarItem cOUIBottomFloatingToolbarItem : this.mContentReplaceItemsList) {
            int id = cOUIBottomFloatingToolbarItem.getId();
            for (COUIBottomFloatingToolbarItem cOUIBottomFloatingToolbarItem2 : this.mItemBeanList) {
                if (id == cOUIBottomFloatingToolbarItem2.getId()) {
                    cOUIBottomFloatingToolbarItem2.setIcon(cOUIBottomFloatingToolbarItem.getIcon());
                    cOUIBottomFloatingToolbarItem2.setTitle(cOUIBottomFloatingToolbarItem.getTitle());
                }
            }
            for (View view : this.mItemViewList) {
                if (id == view.getId() && (view instanceof COUIBottomFloatingToolbarItemView)) {
                    COUIBottomFloatingToolbarItemView cOUIBottomFloatingToolbarItemView = (COUIBottomFloatingToolbarItemView) view;
                    cOUIBottomFloatingToolbarItemView.setIcon(cOUIBottomFloatingToolbarItem.getIcon());
                    cOUIBottomFloatingToolbarItemView.setTitle(cOUIBottomFloatingToolbarItem.getTitle());
                }
            }
        }
    }

    public void handleInsertItemsAction() {
        View next;
        ArrayList<View> arrayList;
        if (this.mInsertItemsMap.isEmpty()) {
            return;
        }
        for (Map.Entry<COUIBottomFloatingToolbarItem, Integer> entry : this.mInsertItemsMap.entrySet()) {
            COUIBottomFloatingToolbarItem key = entry.getKey();
            int iIntValue = entry.getValue().intValue();
            if (iIntValue >= this.mItemBeanList.size()) {
                iIntValue = this.mItemBeanList.size();
            }
            this.mItemBeanList.add(iIntValue, key);
            if (!this.mDoAnimation || (arrayList = this.mTransitioningViews) == null) {
                next = null;
                break;
            }
            Iterator<View> it = arrayList.iterator();
            do {
                if (!it.hasNext()) {
                    next = null;
                    break;
                }
                next = it.next();
            } while (key.getId() != next.getId());
            if (next != null) {
                ArrayList<View> arrayList2 = this.mTransitioningViews;
                if (arrayList2 != null && arrayList2.contains(next)) {
                    endMenuItemViewTransition(next);
                    this.mItemViewList.add(iIntValue, next);
                }
            } else {
                this.mItemViewList.add(iIntValue, createItemView(key));
            }
        }
        setItemList(this.mItemViewList);
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarItemViewAnimator
    public void handleOverflowItem(COUIBottomFloatingToolbarItemView cOUIBottomFloatingToolbarItemView, boolean z2) {
        ItemViewAnimatorImpl itemViewAnimatorImpl = this.mItemViewAnimatorImpl;
        if (itemViewAnimatorImpl != null) {
            itemViewAnimatorImpl.handleOverflowItem(cOUIBottomFloatingToolbarItemView, z2);
        }
    }

    public void handleRemoveItemsAction() {
        if (this.mRemoveItemsList.isEmpty()) {
            return;
        }
        Iterator<COUIBottomFloatingToolbarItem> it = this.mRemoveItemsList.iterator();
        while (it.hasNext()) {
            int id = it.next().getId();
            for (int i3 = 0; i3 < this.mItemBeanList.size(); i3++) {
                if (id == this.mItemBeanList.get(i3).getId()) {
                    this.mItemBeanList.remove(i3);
                    break;
                }
            }
            for (int i4 = 0; i4 < this.mItemViewList.size(); i4++) {
                if (id == this.mItemViewList.get(i4).getId()) {
                    this.mItemViewList.remove(i4);
                    break;
                }
            }
        }
        setItemList(this.mItemViewList);
    }

    public boolean isRtl() {
        return ViewCompat.getLayoutDirection(this) == 1;
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarItemViewAnimator
    public boolean isRunningAnimation() {
        ItemViewAnimatorImpl itemViewAnimatorImpl = this.mItemViewAnimatorImpl;
        if (itemViewAnimatorImpl != null) {
            return itemViewAnimatorImpl.isRunningAnimation();
        }
        return false;
    }

    public void layoutMenuItemsForCurrentBounds() {
        if (getWidth() <= 0 || getHeight() <= 0 || this.mMeasuredChild.isEmpty()) {
            return;
        }
        onLayoutMenuItems(0, 0, getWidth(), getHeight());
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (BottomFloatingToolbarUtil.isOs17OrAbove()) {
            COUIMaterialStateManager.INSTANCE.addOnMaterialStateChangedListener(this);
            COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable;
            if (cOUIShadowEdgeDrawable != null) {
                cOUIShadowEdgeDrawable.setCallback(this);
            }
            COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLightEffectDrawable;
            if (cOUISpotLightEffectDrawable != null) {
                cOUISpotLightEffectDrawable.setCallback(this);
            }
        }
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        boolean z2 = this.mLastOrientation != configuration.orientation;
        boolean z3 = this.mLastScreenWidthDp != configuration.screenWidthDp;
        if (z2 || z3) {
            ItemViewAnimatorImpl itemViewAnimatorImpl = this.mItemViewAnimatorImpl;
            if (itemViewAnimatorImpl != null) {
                itemViewAnimatorImpl.cancelItemAnim();
            }
            MenuViewAnimatorImpl menuViewAnimatorImpl = this.mMenuViewAnimatorImpl;
            if (menuViewAnimatorImpl != null) {
                menuViewAnimatorImpl.cancelMenuAnim();
            }
            setTranslationX(0.0f);
            this.mLayoutAnim = false;
        }
        this.mLastOrientation = configuration.orientation;
        this.mLastScreenWidthDp = configuration.screenWidthDp;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (BottomFloatingToolbarUtil.isOs17OrAbove()) {
            COUIMaterialStateManager.INSTANCE.removeOnMaterialStateChangedListener(this);
            COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable;
            if (cOUIShadowEdgeDrawable != null) {
                cOUIShadowEdgeDrawable.setCallback(null);
            }
            COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLightEffectDrawable;
            if (cOUISpotLightEffectDrawable != null) {
                cOUISpotLightEffectDrawable.setCallback(null);
                this.mSpotLightEffectDrawable.cancel();
            }
        }
        COUISpringAnimation cOUISpringAnimation = this.mTouchEffectAlphaAnim;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.cancel();
        }
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        Canvas canvas2;
        super.onDraw(canvas);
        syncVisualState();
        drawPanelStroke(canvas);
        MaterialEffectManager materialEffectManager = this.mMaterialEffectManager;
        if (materialEffectManager != null && this.mMenuViewMaterial && this.mStrokeAndInnerShadowEnabled) {
            RectF rectF = this.mBackgroundBounds;
            canvas2 = canvas;
            materialEffectManager.drawMaterialEffects(canvas2, rectF, this.mBackgroundPath, rectF.left, getPaddingTop());
        } else {
            canvas2 = canvas;
        }
        drawTouchEffectDrawable(canvas2);
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public CharSequence onDump() {
        return toString();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        boolean z3;
        if (this.mDoAnimation) {
            if (this.mMeasuredChild.size() == this.mChildList.size()) {
                int i7 = 0;
                while (true) {
                    if (i7 < this.mMeasuredChild.size()) {
                        if (this.mMeasuredChild.get(i7) == this.mChildList.get(i7)) {
                            i7++;
                        }
                    }
                }
            } else {
                z3 = !this.mChildList.isEmpty();
            }
            this.mPendingRollback = true;
            if (isIdle() || this.mMeasuredChild.size() != this.mLastChild.size()) {
                this.mPendingRollback = false;
            } else {
                for (int i8 = 0; i8 < this.mMeasuredChild.size(); i8++) {
                    if (this.mMeasuredChild.get(i8) != this.mLastChild.get(i8)) {
                        this.mPendingRollback = false;
                        break;
                    }
                }
            }
            this.mDoItemAnimation = (z3 | this.mPendingRollback) & this.mScreenSwitch;
        }
        boolean zShouldPreserveAnimDuringLayout = shouldPreserveAnimDuringLayout();
        if (this.mDoItemAnimation) {
            for (int i9 = 0; i9 < this.mItemRemoving.size(); i9++) {
                startMenuItemViewTransition(this.mItemRemoving.get(i9));
            }
        }
        for (int i10 = 0; i10 < getChildCount(); i10++) {
            if (!this.mMeasuredChild.contains(getChildAt(i10)) && this.mDoItemAnimation) {
                startMenuItemViewTransition(getChildAt(i10));
            }
        }
        if (this.mMeasuredChild.size() != 1 || (this.mMeasuredChild.get(0) instanceof COUIBottomFloatingToolbarItemView)) {
            if (!zShouldPreserveAnimDuringLayout) {
                removeAllViewsInLayout();
                for (int i11 = 0; i11 < this.mMeasuredChild.size(); i11++) {
                    if (this.mMeasuredChild.get(i11).getParent() != null) {
                        ((ViewGroup) this.mMeasuredChild.get(i11).getParent()).removeView(this.mMeasuredChild.get(i11));
                    }
                    addView(this.mMeasuredChild.get(i11), getChildCount());
                    this.mMeasuredChild.get(i11).setScaleY(1.0f);
                    this.mMeasuredChild.get(i11).setScaleX(1.0f);
                    this.mMeasuredChild.get(i11).setAlpha(1.0f);
                    this.mMeasuredChild.get(i11).setTranslationX(0.0f);
                }
            }
        } else if (getChildCount() == 0) {
            if (this.mMeasuredChild.get(0).getParent() != null) {
                ((ViewGroup) this.mMeasuredChild.get(0).getParent()).removeView(this.mMeasuredChild.get(0));
            }
            addView(this.mMeasuredChild.get(0), getChildCount());
        }
        if (this.mDoItemAnimation && !zShouldPreserveAnimDuringLayout) {
            ensureItemViewAnimatorImpl();
            this.mItemViewAnimatorImpl.setItemList(this.mMeasuredChild);
        } else if (!zShouldPreserveAnimDuringLayout) {
            ItemViewAnimatorImpl itemViewAnimatorImpl = this.mItemViewAnimatorImpl;
            if (itemViewAnimatorImpl != null && itemViewAnimatorImpl.isRunningAnimation() && !this.mDoAnimation) {
                this.mItemViewAnimatorImpl.skipAnimation();
            }
            this.mMenuViewItemBgInsetHorizontal = computeMenuViewItemBgInsetHorizontal();
            this.mChildList.clear();
            this.mChildList.addAll(this.mMeasuredChild);
            this.mLastChild.clear();
            this.mLastChild.addAll(this.mMeasuredChild);
        }
        updateFloatingToolbarMaskOutsetForChildren();
        onLayoutMenuItems(i3, i4, i5, i6);
        if (zShouldPreserveAnimDuringLayout) {
            ensureItemViewAnimatorImpl();
            this.mItemViewAnimatorImpl.reconcileVisualStateAfterLayout();
        }
        onMenuViewWidthChanged(this.mOldWidth, getMeasuredWidth());
        this.mOldWidth = getMeasuredWidth();
        updateMaterialRoundedCorners();
        if (zShouldPreserveAnimDuringLayout || this.mDoItemAnimation) {
            return;
        }
        applyMaterialCornerWeight();
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public void onMaterialStateChange(int i3, boolean z2) {
        if (BottomFloatingToolbarUtil.isOs17OrAbove()) {
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
    }

    @Override // android.view.View
    public void onMeasure(int i3, int i4) {
        View view;
        for (int size = this.mTransitioningViews.size() - 1; size >= 0; size--) {
            endMenuItemViewTransition(this.mTransitioningViews.get(size));
        }
        int size2 = ((View.MeasureSpec.getSize(i3) - getPaddingLeft()) - getPaddingRight()) - (computeMenuViewItemBgInsetHorizontal() * 2);
        this.mMeasuredChild.clear();
        this.mItemRemoving.clear();
        measureChild(this.mOverflowItem, i3, i4);
        int measuredWidth = this.mOverflowItem.getMeasuredWidth();
        int size3 = this.mIncomingItemList.size() - 1;
        while (true) {
            if (size3 < 0) {
                view = null;
                break;
            }
            view = this.mIncomingItemList.get(size3);
            if (view.getVisibility() != 8) {
                break;
            } else {
                size3--;
            }
        }
        boolean z2 = false;
        int iMax = 0;
        for (int i5 = 0; i5 < this.mIncomingItemList.size(); i5++) {
            View view2 = this.mIncomingItemList.get(i5);
            int i6 = this.mMeasuredChild.isEmpty() ? 0 : this.mItemViewSpace;
            if (view2.getVisibility() != 8) {
                boolean z3 = view2 instanceof COUIBottomFloatingToolbarItemView;
                if (!z3) {
                    this.mMeasuredChild.clear();
                    this.mItemRemoving.clear();
                }
                if (!z2) {
                    if (view2.getLayoutParams() == null) {
                        view2.setLayoutParams(generateDefaultLayoutParams());
                    }
                    measureChild(view2, i3, i4);
                    iMax = Math.max(iMax, view2.getMeasuredHeight());
                    if (view2 != view && view2.getMeasuredWidth() + this.mItemViewSpace + i6 + measuredWidth <= size2 && this.mMeasuredChild.size() < 6) {
                        size2 -= view2.getMeasuredWidth() + i6;
                        this.mMeasuredChild.add(view2);
                        endMenuItemViewTransition(view2);
                    } else if (view2 != view || view2.getMeasuredWidth() + i6 > size2) {
                        measureChild(this.mOverflowItem, i3, i4);
                        size2 -= this.mOverflowItem.getMeasuredWidth() + i6;
                        this.mMeasuredChild.add(this.mOverflowItem);
                        this.mItemRemoving.add(view2);
                        z2 = true;
                    } else {
                        size2 -= view2.getMeasuredWidth() + i6;
                        this.mMeasuredChild.add(view2);
                        endMenuItemViewTransition(view2);
                    }
                    if (!z3) {
                        break;
                    }
                } else {
                    this.mItemRemoving.add(view2);
                }
            }
        }
        setMeasuredDimension(View.MeasureSpec.getSize(i3) - size2, Math.max(iMax, this.mMenuViewHeight));
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarItemViewAnimator
    public void onMenuViewWidthChanged(int i3, int i4) {
        ItemViewAnimatorImpl itemViewAnimatorImpl = this.mItemViewAnimatorImpl;
        if (itemViewAnimatorImpl != null) {
            itemViewAnimatorImpl.onMenuViewWidthChanged(i3, i4);
        }
    }

    @Override // android.view.View
    public void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        COUITouchMotionAnimator cOUITouchMotionAnimator = this.mTouchMotionAnimator;
        if (cOUITouchMotionAnimator != null) {
            cOUITouchMotionAnimator.onSizeChange(i3, i4);
        }
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLightEffectDrawable;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setBounds(0, 0, i3, i4);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return true;
    }

    @Override // android.view.View
    public void onWindowVisibilityChanged(int i3) {
        super.onWindowVisibilityChanged(i3);
        if (i3 == 0) {
            ItemViewAnimatorImpl itemViewAnimatorImpl = this.mItemViewAnimatorImpl;
            if (itemViewAnimatorImpl != null) {
                itemViewAnimatorImpl.flushAnimatingStateIfNeeded();
            }
            syncVisualState();
            invalidate();
        }
    }

    public void registerItemClick(COUIBottomFloatingToolbar.ItemClickCallback itemClickCallback) {
        this.mClickCallBack = itemClickCallback;
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarItemViewAnimator
    public void setAttachAnimation(Runnable runnable) {
        ItemViewAnimatorImpl itemViewAnimatorImpl = this.mItemViewAnimatorImpl;
        if (itemViewAnimatorImpl != null) {
            itemViewAnimatorImpl.setAttachAnimation(runnable);
        } else {
            runnable.run();
        }
    }

    public void setIsDrawableMaterialEffect(boolean z2) {
        if (this.mIsDrawableMaterialEffect == z2) {
            return;
        }
        this.mIsDrawableMaterialEffect = z2;
        applyPanelMaterialEffectState();
    }

    public void setItemList(List<COUIBottomFloatingToolbarItem> list) {
        Objects.requireNonNull(list);
        for (COUIBottomFloatingToolbarItem cOUIBottomFloatingToolbarItem : list) {
            View viewCreateItemView = createItemView(cOUIBottomFloatingToolbarItem);
            if (viewCreateItemView == null) {
                return;
            }
            this.mItemBeanList.add(cOUIBottomFloatingToolbarItem);
            this.mItemViewList.add(viewCreateItemView);
            this.mGroupId = cOUIBottomFloatingToolbarItem.getGroupId();
        }
        setItemList(this.mItemViewList);
        COUIBottomFloatingToolbarItemView cOUIBottomFloatingToolbarItemView = this.mOverflowItem;
        if (cOUIBottomFloatingToolbarItemView != null) {
            cOUIBottomFloatingToolbarItemView.setGroupId(this.mGroupId);
        }
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarItemViewAnimator
    public void setLayoutAnimation(Runnable runnable) {
        ItemViewAnimatorImpl itemViewAnimatorImpl = this.mItemViewAnimatorImpl;
        if (itemViewAnimatorImpl != null) {
            itemViewAnimatorImpl.setLayoutAnimation(runnable);
        } else if (runnable != null) {
            runnable.run();
        }
    }

    public final void setMenuViewMaterial(boolean z2) {
        this.mMenuViewMaterial = z2;
        changeBackground();
    }

    public void setScreenSwitch(boolean z2) {
        this.mScreenSwitch = z2;
    }

    public void setStrokeAndInnerShadowEnabled(boolean z2) {
        this.mStrokeAndInnerShadowEnabled = z2;
        invalidate();
    }

    public void setUseMaterialEffectAttr(COUIBottomFloatingToolbar.MaterialEffectState materialEffectState) {
        int value = materialEffectState.getValue();
        if (this.mUseMaterialEffectAttr != value && UIUtil.confirmLevelAnim(UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN)) {
            COUIMaterialStateManager cOUIMaterialStateManager = COUIMaterialStateManager.INSTANCE;
            if (cOUIMaterialStateManager.materialVersionCheck() && cOUIMaterialStateManager.checkThemeEnable(getContext())) {
                this.mUseMaterialEffectAttr = value;
                if (this.mIsDrawableMaterialEffect) {
                    applyPanelMaterialEffectState();
                }
            }
        }
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarItemViewAnimator
    public void skipAnimation() {
        ItemViewAnimatorImpl itemViewAnimatorImpl = this.mItemViewAnimatorImpl;
        if (itemViewAnimatorImpl != null) {
            itemViewAnimatorImpl.skipAnimation();
        }
    }

    public void syncVisualState() {
        updateAllAnimatingViews();
        updateAndDrawBackgroundRect();
        updateSmoothRoundPath();
        updateEdgeBounds();
    }

    public void syncVisualStateIfNeeded() {
        if (shouldSyncOffDraw()) {
            syncVisualState();
        }
    }

    public void unRegisterItemClick() {
        this.mClickCallBack = null;
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarMenuViewAnimator
    public void unbindController() {
        ensureMenuViewAnimatorImpl();
        this.mMenuViewAnimatorImpl.unbindController();
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarItemViewAnimator
    public void updateAllAnimatingViews() {
        ItemViewAnimatorImpl itemViewAnimatorImpl = this.mItemViewAnimatorImpl;
        if (itemViewAnimatorImpl != null) {
            itemViewAnimatorImpl.updateAllAnimatingViews();
        }
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarMenuViewAnimator
    public void updateAttachState(boolean z2, boolean z3) {
        ensureMenuViewAnimatorImpl();
        this.mMenuViewAnimatorImpl.updateAttachState(z2, z3);
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarMenuViewAnimator
    public void updateRealBounds(int i3, int i4, int i5, int i6, boolean z2) {
        ensureMenuViewAnimatorImpl();
        this.mMenuViewAnimatorImpl.updateRealBounds(i3, i4, i5, i6, z2);
    }

    public COUIBottomFloatingToolbarMenuView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIBottomFloatingToolbarMenuView(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0);
    }

    public COUIBottomFloatingToolbarMenuView(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mLastChild = new ArrayList<>();
        this.mMeasuredChild = new ArrayList<>();
        this.mChildList = new ArrayList<>();
        Path path = new Path();
        this.mBackgroundPath = path;
        this.mItemRemoving = new ArrayList<>();
        this.mIncomingItemList = new ArrayList<>();
        this.mBackgroundBounds = new RectF();
        this.mBackgroundRect = new Rect();
        this.mClickListener = new d(this);
        this.mMenuViewAnimatorImpl = null;
        this.mItemViewAnimatorImpl = null;
        this.mGroupId = -1;
        this.mLayoutAnim = true;
        this.mItemBeanList = new ArrayList();
        this.mItemViewList = new ArrayList<>();
        this.mMoreOptionsDescription = getResources().getString(com.support.bottomnavigation.R.string.floating_toolbar_item_more);
        this.mInsertItemsMap = new LinkedHashMap();
        this.mRemoveItemsList = new ArrayList();
        this.mContentReplaceItemsList = new ArrayList();
        this.mOldWidth = 0;
        this.mTransitioningViews = new ArrayList<>();
        this.mLastOrientation = 0;
        this.mLastScreenWidthDp = 0;
        this.mStrokeAndInnerShadowEnabled = true;
        this.mUseMaterialEffectAttr = COUIBottomFloatingToolbar.MaterialEffectState.FOLLOW_SYSTEM.getValue();
        this.mIsDrawableMaterialEffect = false;
        this.mContext = context;
        if (BottomFloatingToolbarUtil.isOs17OrAbove()) {
            initOs17SystemMaterial();
            initShadowEdgeDrawableIfNeeded();
        } else {
            this.mMaterialEffectManager = new MaterialEffectManager(context);
        }
        this.mMenuViewHeight = getResources().getDimensionPixelSize(com.support.bottomnavigation.R.dimen.coui_floating_toolbar_menu_view_height);
        this.mItemViewSpace = getResources().getDimensionPixelSize(com.support.bottomnavigation.R.dimen.coui_floating_toolbar_item_view_space);
        this.mMenuViewItemBgInsetHorizontalDefault = getResources().getDimensionPixelSize(com.support.bottomnavigation.R.dimen.coui_floating_toolbar_menu_view_item_bg_inset_horizontal);
        this.mMenuViewItemBgInsetHorizontal = 0;
        setOutlineProvider(new AnonymousClass1());
        setClipToOutline(true);
        this.mBackgroundSmoothRoundCornerHelper = new COUIBottomFloatingToolbar.SmoothRoundCornerHelper(path);
        setWillNotDraw(false);
        setClipChildren(false);
        setClipToPadding(false);
        createOverflowItem();
        initTouchMotionAnimIfSupported();
        initPressTouchEffectIfSupported();
        applySmoothCornerOutlineState();
        initConfigInfo();
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarItemViewAnimator
    public void setItemList(ArrayList<View> arrayList) {
        this.mIncomingItemList.clear();
        this.mIncomingItemList.addAll(arrayList);
        requestLayout();
    }

    public COUIBottomFloatingToolbarMenuView(Context context, boolean z2) {
        this(context);
        setMenuViewMaterial(z2);
    }
}