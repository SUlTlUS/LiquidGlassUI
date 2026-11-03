package com.coui.appcompat.bottomfloatingnavigationbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.COUIMaterialEffect;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.IMeltBallView;
import com.coui.appcompat.animation.IExpansionViewTouchMotionAnimHost;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.bottomfloatingtoolbar.COUIBottomFloatingToolbarItemView;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.coui.appcompat.poplist.PopupMenuConfigRule;
import com.coui.appcompat.uiutil.AnimLevel;
import com.coui.appcompat.uiutil.UIUtil;
import com.oplus.view.material.OplusMaterialMetaBallParams;
import com.support.poplist.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes7.dex */
public class COUIBottomFloatingNavigationBar extends ViewGroup implements PopupMenuConfigRule {
    private static final float DEFAULT_SPRING_ANIMATION_FACTOR = 10000.0f;
    private static final float EXPANSION_VIEW_ALPHA_SPRING_BOUNCE = 0.0f;
    private static final float EXPANSION_VIEW_DEFAULT_MIN_VISIBLE_CHANGE = 0.001f;
    private static final float EXPANSION_VIEW_MIN_SCALE = 0.5f;
    private static final float HIGH_END_EXPANSION_VIEW_ALPHA_SPRING_RESPONSE = 0.35f;
    private static final float HIGH_END_EXPANSION_VIEW_SCALE_SPRING_BOUNCE = 0.35f;
    private static final float HIGH_END_EXPANSION_VIEW_SCALE_SPRING_RESPONSE = 0.55f;
    private static final float HIGH_END_EXPANSION_VIEW_TRANSLATION_X_SPRING_BOUNCE = 0.35f;
    private static final float HIGH_END_EXPANSION_VIEW_TRANSLATION_X_SPRING_RESPONSE = 0.55f;
    private static final float LOW_END_EXPANSION_VIEW_ALPHA_SPRING_RESPONSE = 0.2f;
    private static final float LOW_END_EXPANSION_VIEW_SCALE_SPRING_BOUNCE = 0.3f;
    private static final float LOW_END_EXPANSION_VIEW_SCALE_SPRING_RESPONSE = 0.5f;
    private static final float LOW_END_EXPANSION_VIEW_TRANSLATION_X_SPRING_BOUNCE = 0.3f;
    private static final float LOW_END_EXPANSION_VIEW_TRANSLATION_X_SPRING_RESPONSE = 0.5f;
    private static final float MELT_BALL_DRAW_ORDER_ELEVATION = 10000.0f;
    private static final int MELT_BALL_EXPANSION_VIEW_INDEX = 2;
    private static final float MELT_BALL_EXTENSION_FACTOR_X = 1.5f;
    private static final float MELT_BALL_EXTENSION_FACTOR_Y = 2.0f;
    private static final int MELT_BALL_MENU_VIEW_INDEX = 1;
    private static final int MELT_BALL_MODE = 0;
    private static final int MENU_VIEW_CENTER_ITEM_COUNT = 2;
    private static final int SHADOW_CLIP_EXPAND_SIZE_PX = 120;
    private Rect mDisplayFrame;
    private Rect mDisplayFrameOutsets;
    private View mExpansionView;
    private COUISpringAnimation mExpansionViewAlphaAnim;
    private final View.OnLayoutChangeListener mExpansionViewLayoutChangeListener;
    private COUISpringAnimation mExpansionViewScaleAnim;
    private COUISpringAnimation mExpansionViewTranslationXAnim;
    private float mExpansionViewTranslationXAnimTarget;
    private final Rect mFakeBounds;
    private final int mFakeViewSize;
    private ImageView mFloatingIconView;
    private boolean mIsExpansionViewActive;
    private boolean mIsMenuViewVisible;
    private COUIBottomFloatingToolbarItemView mLargeIconItemView;
    private final int mLargeIconOverflow;
    private final int mLargeIconSize;
    private int mLastFakeBoundsLeft;
    private int mLastOrientation;
    private int mLastScreenWidthDp;
    private View mMainView;
    private ArraySet<View> mMeltBallChainViews;
    private MeltBallHideTransitionState mMeltBallHideTransitionState;
    private final int mMeltBallParamExtension;
    private final MenuBuilder mMenuBuilder;
    private MenuInflater mMenuInflater;
    private final COUIBottomFloatingNavigationBarMenuView mMenuView;
    private boolean mMenuViewCentered;
    private boolean mMenuViewEverRemoveExpansionView;
    private final int mMinHeightWithoutPaddingVertical;
    private final ViewOutlineProvider mNoDropShadowOutline;
    private Runnable mOverlapAlphaGeometryCallback;
    private final int mPaddingHorizontal;
    private boolean mPopupRuleEnable;
    private final int mSideViewSpacing;
    private boolean mSkipNextLayoutAnimation;
    private final int[] mTmpExpansionHorizontalPositions;
    private final int[] mTmpLocationOnScreen;
    private Rect mWindowFrame;

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingnavigationbar.COUIBottomFloatingNavigationBar$1, reason: invalid class name */
    public class AnonymousClass1 extends ViewOutlineProvider {
        public AnonymousClass1() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setEmpty();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingnavigationbar.COUIBottomFloatingNavigationBar$2, reason: invalid class name */
    public class AnonymousClass2 implements COUIDynamicAnimation.OnAnimationUpdateListener {
        public AnonymousClass2() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
        public void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
            if (COUIBottomFloatingNavigationBar.access$000(COUIBottomFloatingNavigationBar.this) != null) {
                COUIBottomFloatingNavigationBar.access$000(COUIBottomFloatingNavigationBar.this).setX(Math.round(f3));
                COUIBottomFloatingNavigationBar.access$100(COUIBottomFloatingNavigationBar.this);
                COUIBottomFloatingNavigationBar.access$200(COUIBottomFloatingNavigationBar.this);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingnavigationbar.COUIBottomFloatingNavigationBar$3, reason: invalid class name */
    public class AnonymousClass3 implements COUIDynamicAnimation.OnAnimationEndListener {
        public AnonymousClass3() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
        public void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
            if (COUIBottomFloatingNavigationBar.access$300(COUIBottomFloatingNavigationBar.this)) {
                COUIBottomFloatingNavigationBar.access$400(COUIBottomFloatingNavigationBar.this, true);
            } else {
                COUIBottomFloatingNavigationBar.access$500(COUIBottomFloatingNavigationBar.this);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingnavigationbar.COUIBottomFloatingNavigationBar$4, reason: invalid class name */
    public class AnonymousClass4 implements COUIDynamicAnimation.OnAnimationUpdateListener {
        public AnonymousClass4() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
        public void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
            float f5 = f3 / 10000.0f;
            if (COUIBottomFloatingNavigationBar.access$600(COUIBottomFloatingNavigationBar.this) != null) {
                COUIBottomFloatingNavigationBar cOUIBottomFloatingNavigationBar = COUIBottomFloatingNavigationBar.this;
                COUIBottomFloatingNavigationBar.access$700(cOUIBottomFloatingNavigationBar, COUIBottomFloatingNavigationBar.access$600(cOUIBottomFloatingNavigationBar), f5);
                COUIBottomFloatingNavigationBar.access$100(COUIBottomFloatingNavigationBar.this);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingnavigationbar.COUIBottomFloatingNavigationBar$5, reason: invalid class name */
    public class AnonymousClass5 implements COUIDynamicAnimation.OnAnimationUpdateListener {
        public AnonymousClass5() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
        public void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
            float f5 = f3 / 10000.0f;
            if (COUIBottomFloatingNavigationBar.access$600(COUIBottomFloatingNavigationBar.this) != null) {
                COUIBottomFloatingNavigationBar cOUIBottomFloatingNavigationBar = COUIBottomFloatingNavigationBar.this;
                cOUIBottomFloatingNavigationBar.setExpansionViewAlpha(COUIBottomFloatingNavigationBar.access$600(cOUIBottomFloatingNavigationBar), f5);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingnavigationbar.COUIBottomFloatingNavigationBar$6, reason: invalid class name */
    public class AnonymousClass6 implements BaseBottomFloatingMenuView.OnItemClickListener {
        final /* synthetic */ OnItemSelectedListener val$listener;

        public AnonymousClass6(OnItemSelectedListener onItemSelectedListener) {
            this.val$listener = onItemSelectedListener;
        }

        @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView.OnItemClickListener
        public void onItemClick(View view, BaseBottomFloatingItem baseBottomFloatingItem, int i3) {
            OnItemSelectedListener onItemSelectedListener = this.val$listener;
            if (onItemSelectedListener == null || !(baseBottomFloatingItem instanceof COUIBottomFloatingNavigationBarItem)) {
                return;
            }
            onItemSelectedListener.onNavigationItemSelected(view, (COUIBottomFloatingNavigationBarItem) baseBottomFloatingItem, i3);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingnavigationbar.COUIBottomFloatingNavigationBar$7, reason: invalid class name */
    public class AnonymousClass7 implements BaseBottomFloatingMenuView.OnAnimationSyncListener {
        public AnonymousClass7() {
        }

        @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView.OnAnimationSyncListener
        public void onAlphaAnimUpdate(float f3) {
            if (COUIBottomFloatingNavigationBar.access$800(COUIBottomFloatingNavigationBar.this)) {
                COUIBottomFloatingNavigationBar.access$900(COUIBottomFloatingNavigationBar.this).setAlpha(f3);
            }
        }

        @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView.OnAnimationSyncListener
        public void onForegroundBlurRadiusAnimUpdate(float f3) {
            if (COUIBottomFloatingNavigationBar.access$800(COUIBottomFloatingNavigationBar.this)) {
                COUIBottomFloatingNavigationBar.access$900(COUIBottomFloatingNavigationBar.this).setRenderEffect(RenderEffect.createBlurEffect(f3, f3, Shader.TileMode.CLAMP));
            }
        }

        @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView.OnAnimationSyncListener
        public void onScaleAnimUpdate(float f3) {
            if (COUIBottomFloatingNavigationBar.access$800(COUIBottomFloatingNavigationBar.this)) {
                COUIBottomFloatingNavigationBar.access$900(COUIBottomFloatingNavigationBar.this).setScaleX(f3);
                COUIBottomFloatingNavigationBar.access$900(COUIBottomFloatingNavigationBar.this).setScaleY(f3);
            }
        }

        @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView.OnAnimationSyncListener
        public void onTouchAnimUpdate(float f3, float f4, float f5, float f6) {
            if (COUIBottomFloatingNavigationBar.access$800(COUIBottomFloatingNavigationBar.this)) {
                COUIBottomFloatingNavigationBar.access$900(COUIBottomFloatingNavigationBar.this).setTranslationX(f3);
                COUIBottomFloatingNavigationBar.access$900(COUIBottomFloatingNavigationBar.this).setTranslationY(f4);
                COUIBottomFloatingNavigationBar.access$900(COUIBottomFloatingNavigationBar.this).setScaleX(f5);
                COUIBottomFloatingNavigationBar.access$900(COUIBottomFloatingNavigationBar.this).setScaleY(f6);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingnavigationbar.COUIBottomFloatingNavigationBar$8, reason: invalid class name */
    public class AnonymousClass8 implements Runnable {
        public AnonymousClass8() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUIBottomFloatingNavigationBar.access$1000(COUIBottomFloatingNavigationBar.this);
        }
    }

    public enum MeltBallHideTransitionState {
        IDLE,
        WAITING_FOR_LAYOUT,
        WAITING_FOR_ANIMATIONS
    }

    public interface OnItemSelectedListener {
        void onNavigationItemSelected(View view, COUIBottomFloatingNavigationBarItem cOUIBottomFloatingNavigationBarItem, int i3);
    }

    public COUIBottomFloatingNavigationBar(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void a(COUIBottomFloatingNavigationBar cOUIBottomFloatingNavigationBar) {
        cOUIBottomFloatingNavigationBar.tryFinishMeltBallHideTransition();
    }

    public static /* synthetic */ View access$000(COUIBottomFloatingNavigationBar cOUIBottomFloatingNavigationBar) {
        return cOUIBottomFloatingNavigationBar.mExpansionView;
    }

    public static /* synthetic */ void access$100(COUIBottomFloatingNavigationBar cOUIBottomFloatingNavigationBar) {
        cOUIBottomFloatingNavigationBar.onExpansionViewGeometryChangedForAlpha();
    }

    public static /* synthetic */ void access$1000(COUIBottomFloatingNavigationBar cOUIBottomFloatingNavigationBar) {
        cOUIBottomFloatingNavigationBar.updateTranslationXViewPosition();
    }

    public static /* synthetic */ void access$200(COUIBottomFloatingNavigationBar cOUIBottomFloatingNavigationBar) {
        cOUIBottomFloatingNavigationBar.finishCollapseTranslationIfFullyOverlapped();
    }

    public static /* synthetic */ boolean access$300(COUIBottomFloatingNavigationBar cOUIBottomFloatingNavigationBar) {
        return cOUIBottomFloatingNavigationBar.mIsExpansionViewActive;
    }

    public static /* synthetic */ void access$400(COUIBottomFloatingNavigationBar cOUIBottomFloatingNavigationBar, boolean z2) {
        cOUIBottomFloatingNavigationBar.setExpansionViewTouchMotionAnimEnabled(z2);
    }

    public static /* synthetic */ void access$500(COUIBottomFloatingNavigationBar cOUIBottomFloatingNavigationBar) {
        cOUIBottomFloatingNavigationBar.tryFinishMeltBallHideTransition();
    }

    public static /* synthetic */ View access$600(COUIBottomFloatingNavigationBar cOUIBottomFloatingNavigationBar) {
        return cOUIBottomFloatingNavigationBar.mMainView;
    }

    public static /* synthetic */ void access$700(COUIBottomFloatingNavigationBar cOUIBottomFloatingNavigationBar, View view, float f3) {
        cOUIBottomFloatingNavigationBar.setScaleSnapped(view, f3);
    }

    public static /* synthetic */ boolean access$800(COUIBottomFloatingNavigationBar cOUIBottomFloatingNavigationBar) {
        return cOUIBottomFloatingNavigationBar.isFloatingIconVisible();
    }

    public static /* synthetic */ ImageView access$900(COUIBottomFloatingNavigationBar cOUIBottomFloatingNavigationBar) {
        return cOUIBottomFloatingNavigationBar.mFloatingIconView;
    }

    private void applyExpansionViewAlphaScaleState(boolean z2, boolean z3) {
        if (this.mExpansionView == null || this.mMainView == null) {
            return;
        }
        if (z3) {
            if (z2) {
                prepareExpansionViewForShowAnim();
            }
            doExpansionViewAlphaScaleAnim(z2);
            return;
        }
        clearExpansionViewAlphaPolicy();
        cancelExpansionViewAlphaScaleAnimIfRunning();
        if (z2) {
            this.mMainView.setScaleX(1.0f);
            this.mMainView.setScaleY(1.0f);
            setExpansionViewAlpha(this.mMainView, 1.0f);
        } else {
            this.mMainView.setScaleX(0.5f);
            this.mMainView.setScaleY(0.5f);
            setExpansionViewAlpha(this.mMainView, 0.0f);
            this.mExpansionView.setVisibility(4);
        }
    }

    private void applyExpansionViewMeltBallState(boolean z2) {
        if (shouldDoTranslationAnim() && (this.mIsExpansionViewActive || z2)) {
            setMeltBallDrawOrder();
            setMeltBallParams();
        } else {
            clearMeltBallParams();
            resetMeltBallDrawOrder();
            this.mMeltBallHideTransitionState = MeltBallHideTransitionState.IDLE;
        }
    }

    private void applyFakeBoundsOrTranslationAnim(int i3, int i4, int i5, int i6) {
        if (shouldDoTranslationAnim() && !this.mSkipNextLayoutAnimation) {
            handleExpansionViewTranslationXAnim(i3, i4, i5, i6);
            return;
        }
        cancelExpansionViewSetXAnimIfRunning();
        this.mFakeBounds.set(i3, i4, i5, i6);
        updateTranslationXViewPosition();
    }

    private boolean areExpansionShowHideAnimIdle() {
        COUISpringAnimation cOUISpringAnimation = this.mExpansionViewAlphaAnim;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            return false;
        }
        COUISpringAnimation cOUISpringAnimation2 = this.mExpansionViewScaleAnim;
        return cOUISpringAnimation2 == null || !cOUISpringAnimation2.isRunning();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void assignExpansionViews(View view) {
        detachExpansionViewLayoutListener();
        if (view instanceof IMeltBallView) {
            IMeltBallView iMeltBallView = (IMeltBallView) view;
            this.mExpansionView = iMeltBallView.getExpansionView();
            this.mMainView = iMeltBallView.getMainView();
        } else {
            this.mExpansionView = view;
            this.mMainView = view;
        }
        View view2 = this.mExpansionView;
        if (view2 != null) {
            view2.addOnLayoutChangeListener(this.mExpansionViewLayoutChangeListener);
        }
    }

    private void attachOverlapAlphaPolicy() {
        COUISpringAnimation cOUISpringAnimation = this.mExpansionViewAlphaAnim;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            this.mExpansionViewAlphaAnim.cancel();
        }
        this.mOverlapAlphaGeometryCallback = new g(this);
    }

    private void attachSpringAlphaPolicy(boolean z2) {
        this.mOverlapAlphaGeometryCallback = null;
        initExpansionViewAlphaAnim();
        this.mExpansionViewAlphaAnim.setStartValue(getExpansionViewAlpha(this.mMainView) * 10000.0f);
        this.mExpansionViewAlphaAnim.animateToFinalPosition(z2 ? 10000.0f : 0.0f);
    }

    public static /* synthetic */ void b(COUIBottomFloatingNavigationBar cOUIBottomFloatingNavigationBar) {
        cOUIBottomFloatingNavigationBar.onExpansionViewGeometryChangedForAlpha();
    }

    public static /* synthetic */ void c(COUIBottomFloatingNavigationBar cOUIBottomFloatingNavigationBar) {
        cOUIBottomFloatingNavigationBar.updateExpansionViewAlphaByOverlap();
    }

    private void cancelExpansionViewAlphaScaleAnimIfRunning() {
        COUISpringAnimation cOUISpringAnimation = this.mExpansionViewAlphaAnim;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            this.mExpansionViewAlphaAnim.cancel();
        }
        COUISpringAnimation cOUISpringAnimation2 = this.mExpansionViewScaleAnim;
        if (cOUISpringAnimation2 == null || !cOUISpringAnimation2.isRunning()) {
            return;
        }
        this.mExpansionViewScaleAnim.cancel();
    }

    private void cancelExpansionViewSetXAnimIfRunning() {
        COUISpringAnimation cOUISpringAnimation = this.mExpansionViewTranslationXAnim;
        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) {
            return;
        }
        this.mExpansionViewTranslationXAnim.cancel();
    }

    private void changePadding(int i3) {
        int i4 = COUIResponsiveUtils.isSmallScreen(getContext(), i3) ? this.mPaddingHorizontal : 0;
        if (i4 == getPaddingLeft() && i4 == getPaddingRight()) {
            return;
        }
        setPadding(i4, getPaddingTop(), i4, getPaddingBottom());
    }

    private void clearExpansionViewAlphaPolicy() {
        this.mOverlapAlphaGeometryCallback = null;
    }

    private void clearMeltBallParams() {
        View view = this.mMainView;
        if (view == null || this.mMenuView == null) {
            return;
        }
        COUIMaterialEffect.setMetaBallParams(view, null);
        COUIMaterialEffect.setMetaBallParams(this.mMenuView, null);
    }

    /* JADX WARN: Code duplicated, block: B:6:0x0009 A[PHI: r4
  0x0009: PHI (r4v9 int) = (r4v6 int), (r4v12 int) binds: [B:11:0x0014, B:4:0x0006] A[DONT_GENERATE, DONT_INLINE]] */
    private void computeExpansionActiveHorizontalPositions(int i3, int i4, int i5, int i6, int i7, boolean z2, boolean z3, boolean z4) {
        int i8;
        if (z4) {
            i8 = ((i5 - i6) / 2) + i3;
            if (!z3) {
                i3 = i4 - i7;
            }
        } else if (z2) {
            i8 = z3 ? i4 - i6 : i3;
            if (!z3) {
                i3 = i4 - i7;
            }
        } else {
            int i9 = this.mSideViewSpacing;
            i8 = ((i5 - ((i6 + i9) + i7)) / 2) + i3;
            if (z3) {
                i8 = i7 + i8 + i9;
                i3 = i8;
            } else {
                i3 = i6 + i8 + i9;
            }
        }
        int[] iArr = this.mTmpExpansionHorizontalPositions;
        iArr[0] = i8;
        iArr[1] = i3;
    }

    private float computeExpansionOverlapRatioWithMenu() {
        return BottomFloatingOverlapHelper.computeScaledViewOverlapRatio(this.mMainView, this, this.mMenuView.getDrawingBounds(), this.mTmpLocationOnScreen);
    }

    private BaseBottomFloatingMenuView.OnAnimationSyncListener createFloatingIconAnimationSyncListener() {
        return new AnonymousClass7();
    }

    private void createFloatingIconView(Drawable drawable) {
        ImageView imageView = new ImageView(getContext());
        this.mFloatingIconView = imageView;
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        this.mFloatingIconView.setImageDrawable(drawable);
        this.mFloatingIconView.setOnClickListener(new f(this));
    }

    public static /* synthetic */ void d(COUIBottomFloatingNavigationBar cOUIBottomFloatingNavigationBar, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        cOUIBottomFloatingNavigationBar.onExpansionShowHideAnimEnded();
    }

    private void detachExpansionViewLayoutListener() {
        View view = this.mExpansionView;
        if (view != null) {
            view.removeOnLayoutChangeListener(this.mExpansionViewLayoutChangeListener);
        }
    }

    private void doExpansionViewAlphaScaleAnim(boolean z2) {
        if (this.mMainView == null) {
            return;
        }
        initExpansionViewScaleAnim();
        this.mExpansionViewScaleAnim.setStartValue(this.mMainView.getScaleX() * 10000.0f);
        this.mExpansionViewScaleAnim.animateToFinalPosition(z2 ? 10000.0f : 5000.0f);
        if (willRunExpansionViewTranslationAnim(z2)) {
            attachOverlapAlphaPolicy();
        } else {
            attachSpringAlphaPolicy(z2);
        }
    }

    public static /* synthetic */ void e(COUIBottomFloatingNavigationBar cOUIBottomFloatingNavigationBar, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        cOUIBottomFloatingNavigationBar.onExpansionShowHideAnimEnded();
    }

    public static /* synthetic */ void f(COUIBottomFloatingNavigationBar cOUIBottomFloatingNavigationBar, View view) {
        COUIBottomFloatingToolbarItemView cOUIBottomFloatingToolbarItemView = cOUIBottomFloatingNavigationBar.mLargeIconItemView;
        if (cOUIBottomFloatingToolbarItemView != null) {
            cOUIBottomFloatingToolbarItemView.performClick();
        }
    }

    private void finishCollapseTranslationIfFullyOverlapped() {
        COUISpringAnimation cOUISpringAnimation;
        if (this.mIsExpansionViewActive || this.mExpansionView == null || (cOUISpringAnimation = this.mExpansionViewTranslationXAnim) == null || !cOUISpringAnimation.isRunning() || computeExpansionOverlapRatioWithMenu() < 1.0f) {
            return;
        }
        float f3 = this.mExpansionViewTranslationXAnimTarget;
        this.mExpansionViewTranslationXAnim.cancel();
        this.mExpansionView.setX(Math.round(f3));
        onExpansionViewGeometryChangedForAlpha();
    }

    public static /* synthetic */ void g(COUIBottomFloatingNavigationBar cOUIBottomFloatingNavigationBar, View view, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        cOUIBottomFloatingNavigationBar.getClass();
        if ((i3 == i7 && i4 == i8 && i5 == i9 && i6 == i10) || cOUIBottomFloatingNavigationBar.mFakeBounds.isEmpty()) {
            return;
        }
        COUISpringAnimation cOUISpringAnimation = cOUIBottomFloatingNavigationBar.mExpansionViewTranslationXAnim;
        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) {
            cOUIBottomFloatingNavigationBar.updateTranslationXViewPosition();
        }
    }

    private MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            this.mMenuInflater = new MenuInflater(getContext());
        }
        return this.mMenuInflater;
    }

    private void handleExpansionViewTranslationXAnim(int i3, int i4, int i5, int i6) {
        float x2;
        if (this.mExpansionView == null) {
            return;
        }
        initExpansionViewTranslationXAnim();
        if (this.mLastFakeBoundsLeft == Integer.MIN_VALUE) {
            x2 = this.mFakeBounds.left;
            this.mExpansionView.setX(x2);
        } else {
            x2 = this.mExpansionView.getX();
        }
        this.mFakeBounds.set(i3, i4, i5, i6);
        this.mLastFakeBoundsLeft = this.mFakeBounds.left;
        this.mExpansionViewTranslationXAnim.setStartValue(x2);
        int i7 = this.mFakeBounds.left;
        this.mExpansionViewTranslationXAnimTarget = i7;
        this.mExpansionViewTranslationXAnim.animateToFinalPosition(i7);
    }

    private void initConfigInfo() {
        Configuration configuration = getResources().getConfiguration();
        this.mLastOrientation = configuration.orientation;
        this.mLastScreenWidthDp = configuration.screenWidthDp;
    }

    private void initExpansionViewAlphaAnim() {
        if (this.mExpansionViewAlphaAnim != null) {
            return;
        }
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(0.0f);
        cOUISpringForce.setResponse(isSupportMeltBall() ? 0.35f : 0.2f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mExpansionViewAlphaAnim = spring;
        spring.addUpdateListener(new AnonymousClass5());
        this.mExpansionViewAlphaAnim.addEndListener(new h(this));
    }

    private void initExpansionViewScaleAnim() {
        if (this.mExpansionViewScaleAnim != null) {
            return;
        }
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(isSupportMeltBall() ? 0.35f : 0.3f);
        cOUISpringForce.setResponse(isSupportMeltBall() ? 0.55f : 0.5f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mExpansionViewScaleAnim = spring;
        spring.addUpdateListener(new AnonymousClass4());
        this.mExpansionViewScaleAnim.addEndListener(new e(this));
    }

    private void initExpansionViewTranslationXAnim() {
        if (this.mExpansionViewTranslationXAnim != null) {
            return;
        }
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(isSupportMeltBall() ? 0.35f : 0.3f);
        cOUISpringForce.setResponse(isSupportMeltBall() ? 0.55f : 0.5f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mExpansionViewTranslationXAnim = spring;
        spring.setMinimumVisibleChange(0.001f);
        this.mExpansionViewTranslationXAnim.addUpdateListener(new AnonymousClass2());
        this.mExpansionViewTranslationXAnim.addEndListener(new AnonymousClass3());
    }

    private boolean isExpansionViewTranslationAnimRunning() {
        COUISpringAnimation cOUISpringAnimation = this.mExpansionViewTranslationXAnim;
        return cOUISpringAnimation != null && cOUISpringAnimation.isRunning();
    }

    private boolean isFloatingIconVisible() {
        ImageView imageView = this.mFloatingIconView;
        return imageView != null && imageView.getVisibility() == 0;
    }

    private void layoutMenuViewForExpansionActive(int i3, int i4, int i5, int i6, boolean z2, boolean z3) {
        int iMax = (z3 || this.mSkipNextLayoutAnimation) ? i5 : Math.max(i5, this.mMenuView.getWidth());
        if (!z2) {
            this.mMenuView.layout(i3, i4, iMax + i3, i6 + i4);
        } else {
            int i7 = i3 + i5;
            this.mMenuView.layout(i7 - iMax, i4, i7, i6 + i4);
        }
    }

    private void layoutWhenExpansionViewActive(int i3, int i4, int i5, int i6, boolean z2, int i7) {
        int measuredWidth = this.mMenuView.getMeasuredWidth();
        int measuredHeight = this.mMenuView.getMeasuredHeight();
        int i8 = this.mFakeViewSize;
        boolean zIsSmallScreen = COUIResponsiveUtils.isSmallScreen(getContext(), i7);
        boolean zShouldLayoutMenuViewCentered = shouldLayoutMenuViewCentered(i7);
        int i9 = i4 + ((i6 - measuredHeight) / 2);
        int i10 = i4 + ((i6 - i8) / 2);
        computeExpansionActiveHorizontalPositions(i3, i5, i5 - i3, measuredWidth, i8, zIsSmallScreen, z2, zShouldLayoutMenuViewCentered);
        int[] iArr = this.mTmpExpansionHorizontalPositions;
        boolean z3 = false;
        int i11 = iArr[0];
        int i12 = iArr[1];
        layoutMenuViewForExpansionActive(i11, i9, measuredWidth, measuredHeight, z2, zShouldLayoutMenuViewCentered);
        if (!this.mSkipNextLayoutAnimation && !zShouldLayoutMenuViewCentered) {
            z3 = true;
        }
        this.mMenuView.updateRealBounds(i11, i9, i11 + measuredWidth, measuredHeight + i9, z3);
        applyFakeBoundsOrTranslationAnim(i12, i10, i12 + i8, i10 + i8);
    }

    private void layoutWhenExpansionViewInactive(int i3, int i4, int i5, int i6, boolean z2, int i7) {
        if (this.mMenuView.getVisibility() == 8) {
            return;
        }
        int measuredWidth = this.mMenuView.getMeasuredWidth();
        int measuredHeight = this.mMenuView.getMeasuredHeight();
        boolean zShouldLayoutMenuViewCentered = shouldLayoutMenuViewCentered(i7);
        int i8 = i3 + ((i5 - measuredWidth) / 2);
        int i9 = i4 + ((i6 - measuredHeight) / 2);
        boolean z3 = (zShouldLayoutMenuViewCentered || !this.mMenuViewEverRemoveExpansionView || this.mSkipNextLayoutAnimation) ? false : true;
        int i10 = measuredHeight + i9;
        this.mMenuView.layout((z2 && z3) ? getLeft() : i8, i9, (z2 || !z3) ? i8 + measuredWidth : getRight(), i10);
        int i11 = i8 + measuredWidth;
        this.mMenuView.updateRealBounds(i8, i9, i11, i10, (this.mSkipNextLayoutAnimation || zShouldLayoutMenuViewCentered) ? false : true);
        if (shouldDoTranslationAnim()) {
            int i12 = this.mFakeViewSize;
            int i13 = i4 + ((i6 - i12) / 2);
            if (!z2) {
                i8 = i11 - i12;
            }
            int i14 = i8 + i12;
            int i15 = i12 + i13;
            if (!this.mFakeBounds.isEmpty() && !this.mSkipNextLayoutAnimation) {
                setExpansionViewTouchMotionAnimEnabled(false);
                handleExpansionViewTranslationXAnim(i8, i13, i14, i15);
            } else {
                cancelExpansionViewSetXAnimIfRunning();
                this.mFakeBounds.set(i8, i13, i14, i15);
                updateTranslationXViewPosition();
            }
        }
    }

    private void measureMenuViewWithCenteredExpansionReserve(int i3, int i4) {
        if (this.mMenuView.getVisibility() == 8) {
            return;
        }
        int size = (View.MeasureSpec.getSize(i3) - getPaddingLeft()) - getPaddingRight();
        int i5 = this.mFakeViewSize + this.mSideViewSpacing;
        this.mMenuView.measure(View.MeasureSpec.makeMeasureSpec(Math.max(0, (size - i5) - i5), Integer.MIN_VALUE), i4);
    }

    private void onExpansionShowHideAnimEnded() {
        View view;
        if (!areExpansionShowHideAnimIdle() || (view = this.mExpansionView) == null || this.mIsExpansionViewActive) {
            return;
        }
        view.setEnabled(false);
    }

    private void onExpansionViewGeometryChangedForAlpha() {
        Runnable runnable = this.mOverlapAlphaGeometryCallback;
        if (runnable != null) {
            runnable.run();
        }
    }

    private void prepareExpansionViewForShowAnim() {
        COUISpringAnimation cOUISpringAnimation = this.mExpansionViewAlphaAnim;
        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) {
            COUISpringAnimation cOUISpringAnimation2 = this.mExpansionViewScaleAnim;
            if (cOUISpringAnimation2 == null || !cOUISpringAnimation2.isRunning()) {
                this.mMainView.setScaleX(0.5f);
                this.mMainView.setScaleY(0.5f);
                setExpansionViewAlpha(this.mMainView, 0.0f);
            }
        }
    }

    private void prepareMeltBallHideTransition(boolean z2) {
        this.mMeltBallHideTransitionState = (z2 && shouldDoTranslationAnim()) ? MeltBallHideTransitionState.WAITING_FOR_LAYOUT : MeltBallHideTransitionState.IDLE;
    }

    private void removeFloatingIcon() {
        if (this.mFloatingIconView != null) {
            this.mMenuView.setOnAnimationSyncListener(null);
            getOverlay().remove(this.mFloatingIconView);
            this.mFloatingIconView = null;
        }
        this.mLargeIconItemView = null;
    }

    private void resetMeltBallDrawOrder() {
        setElevation(0.0f);
        setOutlineProvider(ViewOutlineProvider.BACKGROUND);
    }

    private void setExpansionViewTouchMotionAnimEnabled(boolean z2) {
        KeyEvent.Callback callback = this.mExpansionView;
        if (callback instanceof IExpansionViewTouchMotionAnimHost) {
            ((IExpansionViewTouchMotionAnimHost) callback).setExpansionViewTouchMotionAnimEnabled(z2);
        }
    }

    private void setMeltBallDrawOrder() {
        setElevation(10000.0f);
        setOutlineProvider(this.mNoDropShadowOutline);
    }

    private void setMeltBallParams() {
        if (this.mMainView == null || this.mMenuView == null || !COUIMaterialStateManager.INSTANCE.materialVersionCheck()) {
            return;
        }
        COUIMaterialEffect.setMetaBallParams(this.mMainView, new OplusMaterialMetaBallParams(2, 0, this.mMeltBallParamExtension, 1.5f, 2.0f));
        COUIMaterialEffect.setMetaBallParams(this.mMenuView, new OplusMaterialMetaBallParams(1, 0, this.mMeltBallParamExtension, 1.5f, 2.0f));
        if (this.mMeltBallChainViews == null) {
            ArraySet<View> arraySet = new ArraySet<>(2);
            this.mMeltBallChainViews = arraySet;
            arraySet.add(this.mMainView);
            this.mMeltBallChainViews.add(this.mMenuView);
        }
        COUIMaterialEffect.setMetaBallChain(this.mMeltBallChainViews);
    }

    private void setScaleSnapped(View view, float f3) {
        float fRound;
        int width = view.getWidth();
        int height = view.getHeight();
        if (width > 0) {
            float f4 = width;
            fRound = Math.round(f4 * f3) / f4;
        } else {
            fRound = f3;
        }
        view.setScaleX(fRound);
        if (height > 0) {
            float f5 = height;
            f3 = Math.round(f3 * f5) / f5;
        }
        view.setScaleY(f3);
    }

    private void setupFloatingIcon() {
        Drawable iconDrawable;
        removeFloatingIcon();
        COUIBottomFloatingToolbarItemView largeIconItemView = this.mMenuView.getLargeIconItemView();
        this.mLargeIconItemView = largeIconItemView;
        if (largeIconItemView == null || (iconDrawable = largeIconItemView.getIconDrawable()) == null) {
            return;
        }
        createFloatingIconView(iconDrawable);
        this.mMenuView.setOnAnimationSyncListener(createFloatingIconAnimationSyncListener());
    }

    private boolean shouldDoTranslationAnim() {
        return isSupportMeltBall() || UIUtil.confirmLevelAnim(AnimLevel.LOW_END);
    }

    private boolean shouldLayoutMenuViewCentered(int i3) {
        return this.mMenuViewCentered && this.mMenuView.getVisibleChildCount() == 2 && COUIResponsiveUtils.isSmallScreen(getContext(), i3);
    }

    private void tryFinishMeltBallHideTransition() {
        if (this.mMeltBallHideTransitionState != MeltBallHideTransitionState.WAITING_FOR_ANIMATIONS || this.mIsExpansionViewActive || isExpansionViewTranslationAnimRunning() || this.mMenuView.isBoundsAnimationRunning()) {
            return;
        }
        this.mMeltBallHideTransitionState = MeltBallHideTransitionState.IDLE;
        clearMeltBallParams();
        resetMeltBallDrawOrder();
    }

    private void updateExpansionViewAlphaByOverlap() {
        if (this.mMainView == null) {
            return;
        }
        setExpansionViewAlpha(this.mMainView, BottomFloatingOverlapHelper.alphaFromOverlapRatio(computeExpansionOverlapRatioWithMenu(), 50.0f));
    }

    private void updateFloatingIconPosition() {
        if (this.mFloatingIconView == null || this.mLargeIconItemView == null) {
            return;
        }
        ViewGroupOverlay overlay = getOverlay();
        overlay.remove(this.mFloatingIconView);
        overlay.add(this.mFloatingIconView);
        int left = this.mLargeIconItemView.getLeft();
        int top = this.mLargeIconItemView.getTop();
        int width = left + (this.mLargeIconItemView.getWidth() / 2);
        int left2 = this.mMenuView.getLeft() + width;
        int top2 = (this.mMenuView.getTop() + top) - this.mLargeIconOverflow;
        int i3 = this.mLargeIconSize;
        int i4 = left2 - (i3 / 2);
        this.mFloatingIconView.layout(i4, top2, i4 + i3, i3 + top2);
    }

    private void updateTranslationXViewPosition() {
        if (this.mExpansionView == null || this.mFakeBounds.isEmpty()) {
            return;
        }
        int width = this.mExpansionView.getWidth();
        int height = this.mExpansionView.getHeight();
        if (width == 0 || height == 0) {
            post(new AnonymousClass8());
            return;
        }
        int[] iArr = new int[2];
        getLocationOnScreen(iArr);
        int iCenterX = iArr[0] + this.mFakeBounds.centerX();
        int left = this.mExpansionView.getLeft();
        View view = (View) this.mExpansionView.getParent();
        int[] iArr2 = new int[2];
        if (view != null) {
            view.getLocationOnScreen(iArr2);
        }
        this.mExpansionView.setTranslationX(iCenterX - ((iArr2[0] + left) + (width / 2)));
        onExpansionViewGeometryChangedForAlpha();
    }

    private boolean willRunExpansionViewTranslationAnim(boolean z2) {
        if (!shouldDoTranslationAnim() || this.mSkipNextLayoutAnimation) {
            return false;
        }
        return z2 || !this.mFakeBounds.isEmpty();
    }

    @Deprecated
    public void addExpansionView(View view) {
        bindExpansionView(view);
        showExpansionView(true);
    }

    public void addItems(List<COUIBottomFloatingNavigationBarItem> list) {
        this.mMenuView.addItems(list);
        setupFloatingIcon();
    }

    public void bindExpansionView(View view) {
        assignExpansionViews(view);
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public int getBarrierDirection() {
        if (getDisplayFrame().isEmpty()) {
            return -1;
        }
        if (this.mWindowFrame == null) {
            this.mWindowFrame = new Rect();
        }
        getRootView().getGlobalVisibleRect(this.mWindowFrame);
        return this.mWindowFrame.height() <= getContext().getResources().getDimensionPixelSize(R.dimen.coui_popup_list_window_min_window_height_to_apply_vertical_barrier) ? -1 : 3;
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public Rect getDisplayFrame() {
        if (this.mDisplayFrame == null) {
            this.mDisplayFrame = new Rect();
        }
        if (this.mMenuView.getVisibility() == 0 && this.mMenuView.getGlobalVisibleRect(this.mDisplayFrame) && !this.mDisplayFrame.isEmpty()) {
            return this.mDisplayFrame;
        }
        getGlobalVisibleRect(this.mDisplayFrame);
        return this.mDisplayFrame;
    }

    public float getExpansionViewAlpha(View view) {
        if (view != null) {
            return view.getAlpha();
        }
        return 1.0f;
    }

    public ImageView getFloatingIconView() {
        return this.mFloatingIconView;
    }

    public COUIBottomFloatingToolbarItemView getLargeIconItemView() {
        return this.mLargeIconItemView;
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public Rect getOutsets() {
        if (this.mDisplayFrameOutsets == null) {
            this.mDisplayFrameOutsets = new Rect(0, getContext().getResources().getDimensionPixelOffset(com.support.bottomnavigation.R.dimen.coui_popup_list_window_gap_to_navigation_view), 0, 0);
        }
        return this.mDisplayFrameOutsets;
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public boolean getPopupMenuRuleEnabled() {
        return this.mPopupRuleEnable;
    }

    public int getSelectedItemId() {
        return this.mMenuView.getSelectedItemId();
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public int getType() {
        return 2;
    }

    public void hideExpansionView(boolean z2) {
        if (this.mIsExpansionViewActive) {
            if (!z2) {
                this.mSkipNextLayoutAnimation = true;
            }
            this.mIsExpansionViewActive = false;
            prepareMeltBallHideTransition(z2);
            applyExpansionViewMeltBallState(z2);
            this.mMenuViewEverRemoveExpansionView = true;
            requestLayout();
            applyExpansionViewAlphaScaleState(false, z2);
        }
    }

    public void hideMenuView() {
        if (this.mIsMenuViewVisible) {
            this.mIsMenuViewVisible = false;
            this.mMenuView.doMenuViewShowOrHideAnim(false);
        }
    }

    public final void inflateMenu(int i3) {
        if (i3 == 0) {
            return;
        }
        Map<Integer, MenuXmlParserUtils.MenuItemAttrs> menuItemAttrs = MenuXmlParserUtils.parseMenuItemAttrs(getContext(), i3);
        this.mMenuBuilder.clear();
        getMenuInflater().inflate(i3, this.mMenuBuilder);
        if (this.mMenuBuilder.getNonActionItems().isEmpty()) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        COUIBottomFloatingNavigationBarItem.Builder builder = new COUIBottomFloatingNavigationBarItem.Builder();
        for (MenuItemImpl menuItemImpl : this.mMenuBuilder.getNonActionItems()) {
            int itemId = menuItemImpl.getItemId();
            MenuXmlParserUtils.MenuItemAttrs menuItemAttrs2 = menuItemAttrs.get(Integer.valueOf(itemId));
            ((COUIBottomFloatingNavigationBarItem.Builder) ((COUIBottomFloatingNavigationBarItem.Builder) ((COUIBottomFloatingNavigationBarItem.Builder) builder.reset().setId(itemId)).setTitle(menuItemImpl.getTitle() == null ? "" : menuItemImpl.getTitle().toString())).setIcon(menuItemImpl.getIcon())).setDescription(menuItemImpl.getContentDescription());
            if (menuItemAttrs2 != null) {
                ((COUIBottomFloatingNavigationBarItem.Builder) ((COUIBottomFloatingNavigationBarItem.Builder) builder.setIconResId(menuItemAttrs2.iconResId)).setLargeIcon(menuItemAttrs2.isLargeIcon).setRedDotType(menuItemAttrs2.redDotType)).setRedDotNumber(menuItemAttrs2.redDotNumber);
            }
            arrayList.add(builder.build());
        }
        addItems(arrayList);
    }

    public boolean isSupportMeltBall() {
        return this.mMenuView.isSupportMeltBall();
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        boolean z2 = this.mLastOrientation != configuration.orientation;
        boolean z3 = this.mLastScreenWidthDp != configuration.screenWidthDp;
        if (z2 || z3) {
            this.mSkipNextLayoutAnimation = true;
            if (this.mIsExpansionViewActive) {
                cancelExpansionViewSetXAnimIfRunning();
            }
        }
        this.mLastOrientation = configuration.orientation;
        this.mLastScreenWidthDp = configuration.screenWidthDp;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        COUIBottomFloatingNavigationBar cOUIBottomFloatingNavigationBar;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int i7 = i5 - i3;
        int paddingRight = i7 - getPaddingRight();
        int i8 = paddingRight - paddingLeft;
        int paddingBottom = ((i6 - i4) - getPaddingBottom()) - paddingTop;
        boolean z3 = getLayoutDirection() == 1;
        if (this.mIsExpansionViewActive) {
            cOUIBottomFloatingNavigationBar = this;
            cOUIBottomFloatingNavigationBar.layoutWhenExpansionViewActive(paddingLeft, paddingTop, paddingRight, paddingBottom, z3, i7);
        } else {
            cOUIBottomFloatingNavigationBar = this;
            cOUIBottomFloatingNavigationBar.layoutWhenExpansionViewInactive(paddingLeft, paddingTop, i8, paddingBottom, z3, i7);
        }
        cOUIBottomFloatingNavigationBar.mSkipNextLayoutAnimation = false;
        cOUIBottomFloatingNavigationBar.updateFloatingIconPosition();
        if (cOUIBottomFloatingNavigationBar.mIsExpansionViewActive || cOUIBottomFloatingNavigationBar.mMeltBallHideTransitionState != MeltBallHideTransitionState.WAITING_FOR_LAYOUT) {
            return;
        }
        cOUIBottomFloatingNavigationBar.mMeltBallHideTransitionState = MeltBallHideTransitionState.WAITING_FOR_ANIMATIONS;
        cOUIBottomFloatingNavigationBar.tryFinishMeltBallHideTransition();
    }

    @Override // android.view.View
    public void onMeasure(int i3, int i4) {
        int i5;
        int iMax;
        int iMax2;
        int iCombineMeasuredStates;
        int size = View.MeasureSpec.getSize(i3);
        changePadding(size);
        int i6 = 0;
        if (shouldLayoutMenuViewCentered(size)) {
            measureMenuViewWithCenteredExpansionReserve(i3, i4);
            iMax2 = this.mMenuView.getMeasuredWidth();
            iMax = this.mMenuView.getMeasuredHeight();
            iCombineMeasuredStates = View.combineMeasuredStates(0, this.mMenuView.getMeasuredState());
            if (this.mIsExpansionViewActive) {
                iMax2 = Math.max(iMax2, this.mMenuView.getMeasuredWidth() + this.mSideViewSpacing + this.mFakeViewSize);
                iMax = Math.max(iMax, this.mFakeViewSize);
            }
        } else {
            if (!this.mIsExpansionViewActive) {
                if (this.mMenuView.getVisibility() != 8) {
                    measureChild(this.mMenuView, i3, i4);
                    iMax2 = Math.max(0, this.mMenuView.getMeasuredWidth());
                    iMax = Math.max(0, this.mMenuView.getMeasuredHeight());
                    iCombineMeasuredStates = View.combineMeasuredStates(0, this.mMenuView.getMeasuredState());
                } else {
                    i5 = 0;
                    iMax = 0;
                }
                setMeasuredDimension(View.resolveSizeAndState(Math.max(i6 + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), i3, i5), View.resolveSizeAndState(Math.max(Math.max(iMax + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), this.mMinHeightWithoutPaddingVertical + getPaddingTop() + getPaddingBottom()), i4, i5 << 16));
            }
            int i7 = this.mFakeViewSize;
            int iMax3 = Math.max(0, i7);
            this.mMenuView.measure(View.MeasureSpec.makeMeasureSpec(Math.max(0, (((size - getPaddingLeft()) - getPaddingRight()) - i7) - this.mSideViewSpacing), Integer.MIN_VALUE), i4);
            iMax2 = Math.max(0, this.mMenuView.getMeasuredWidth() + this.mSideViewSpacing + i7);
            iMax = Math.max(iMax3, this.mMenuView.getMeasuredHeight());
            iCombineMeasuredStates = View.combineMeasuredStates(0, this.mMenuView.getMeasuredState());
        }
        int i8 = iCombineMeasuredStates;
        i6 = iMax2;
        i5 = i8;
        setMeasuredDimension(View.resolveSizeAndState(Math.max(i6 + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), i3, i5), View.resolveSizeAndState(Math.max(Math.max(iMax + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), this.mMinHeightWithoutPaddingVertical + getPaddingTop() + getPaddingBottom()), i4, i5 << 16));
    }

    public void refresh() {
        this.mMenuView.refreshThemeAppearance();
        setupFloatingIcon();
        invalidate();
    }

    @Deprecated
    public void removeExpansionView() {
        hideExpansionView(true);
    }

    public void setDispatchItemClickWhenAlreadySelected(boolean z2) {
        this.mMenuView.setDispatchItemClickWhenAlreadySelected(z2);
    }

    public void setExpansionViewAlpha(View view, float f3) {
        if (view != null) {
            view.setAlpha(f3);
        }
    }

    public void setIsDrawableMaterialEffect(boolean z2) {
        this.mMenuView.setIsDrawableMaterialEffect(z2);
    }

    public void setItemsTitleTextColor(ColorStateList colorStateList) {
        this.mMenuView.setItemsTitleTextColor(colorStateList);
    }

    public void setMenuViewCentered(boolean z2) {
        if (this.mMenuViewCentered == z2) {
            return;
        }
        this.mMenuViewCentered = z2;
        requestLayout();
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.mMenuView.setOnItemClickListener(new AnonymousClass6(onItemSelectedListener));
    }

    public void setOnMenuViewShowHideAnimListener(OnMenuViewShowHideAnimListener onMenuViewShowHideAnimListener) {
        this.mMenuView.setOnMenuViewShowHideAnimListener(onMenuViewShowHideAnimListener);
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public void setPopupMenuRuleEnabled(boolean z2) {
        this.mPopupRuleEnable = z2;
    }

    public void setRedDot(int i3, int i4, int i5) {
        this.mMenuView.setRedDot(i3, i4, i5);
    }

    public void setSelectedItemId(int i3) {
        this.mMenuView.setSelectedItem(i3);
    }

    public void setUseMaterialEffectAttr(BaseBottomFloatingMenuView.MaterialEffectState materialEffectState) {
        this.mMenuView.setUseMaterialEffectAttr(materialEffectState);
    }

    public void showExpansionView(boolean z2) {
        if (this.mIsExpansionViewActive || this.mExpansionView == null || this.mMainView == null) {
            return;
        }
        this.mMeltBallHideTransitionState = MeltBallHideTransitionState.IDLE;
        if (!z2) {
            this.mSkipNextLayoutAnimation = true;
        }
        this.mIsExpansionViewActive = true;
        applyExpansionViewMeltBallState(z2);
        this.mExpansionView.setVisibility(0);
        this.mExpansionView.setEnabled(true);
        requestLayout();
        applyExpansionViewAlphaScaleState(true, z2);
    }

    public void showMenuView() {
        if (this.mIsMenuViewVisible) {
            return;
        }
        this.mIsMenuViewVisible = true;
        this.mMenuView.doMenuViewShowOrHideAnim(true);
    }

    public void unbindExpansionView() {
        this.mMeltBallHideTransitionState = MeltBallHideTransitionState.IDLE;
        cancelExpansionViewAlphaScaleAnimIfRunning();
        cancelExpansionViewSetXAnimIfRunning();
        clearExpansionViewAlphaPolicy();
        clearMeltBallParams();
        resetMeltBallDrawOrder();
        detachExpansionViewLayoutListener();
        this.mExpansionView = null;
        this.mMainView = null;
        this.mMeltBallChainViews = null;
        this.mLastFakeBoundsLeft = Integer.MIN_VALUE;
    }

    public COUIBottomFloatingNavigationBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, com.support.bottomnavigation.R.attr.couiBottomFloatingNavigationBarStyle);
    }

    public COUIBottomFloatingNavigationBar(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0);
    }

    public COUIBottomFloatingNavigationBar(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mNoDropShadowOutline = new AnonymousClass1();
        this.mFakeBounds = new Rect();
        this.mTmpExpansionHorizontalPositions = new int[2];
        this.mTmpLocationOnScreen = new int[2];
        this.mIsExpansionViewActive = false;
        this.mSkipNextLayoutAnimation = false;
        this.mMenuViewEverRemoveExpansionView = false;
        this.mLastOrientation = 0;
        this.mLastScreenWidthDp = 0;
        this.mLastFakeBoundsLeft = Integer.MIN_VALUE;
        this.mMeltBallHideTransitionState = MeltBallHideTransitionState.IDLE;
        this.mExpansionViewLayoutChangeListener = new b(this);
        this.mIsMenuViewVisible = true;
        this.mMenuViewCentered = false;
        this.mPopupRuleEnable = true;
        this.mDisplayFrame = null;
        this.mWindowFrame = null;
        this.mDisplayFrameOutsets = null;
        this.mMenuBuilder = new MenuBuilder(context);
        this.mMinHeightWithoutPaddingVertical = getResources().getDimensionPixelSize(com.support.bottomnavigation.R.dimen.coui_bottom_floating_navigation_bar_min_height_without_padding_vertical);
        this.mPaddingHorizontal = getResources().getDimensionPixelSize(com.support.bottomnavigation.R.dimen.coui_bottom_floating_navigation_bar_padding_horizontal);
        this.mSideViewSpacing = getResources().getDimensionPixelSize(com.support.bottomnavigation.R.dimen.coui_bottom_floating_navigation_bar_Minimum_spacing_with_slide_view);
        this.mFakeViewSize = getResources().getDimensionPixelSize(com.support.bottomnavigation.R.dimen.coui_bottom_floating_navigation_bar_height);
        this.mLargeIconSize = getResources().getDimensionPixelSize(com.support.bottomnavigation.R.dimen.coui_bottom_floating_navigation_bar_large_icon_size);
        this.mLargeIconOverflow = getResources().getDimensionPixelSize(com.support.bottomnavigation.R.dimen.coui_bottom_floating_navigation_bar_large_icon_overflow);
        this.mMeltBallParamExtension = getResources().getDimensionPixelSize(com.support.bottomnavigation.R.dimen.coui_bottom_floating_navigation_bar_melt_ball_param_extension);
        COUIBottomFloatingNavigationBarMenuView cOUIBottomFloatingNavigationBarMenuView = new COUIBottomFloatingNavigationBarMenuView(context);
        this.mMenuView = cOUIBottomFloatingNavigationBarMenuView;
        addView(cOUIBottomFloatingNavigationBarMenuView);
        cOUIBottomFloatingNavigationBarMenuView.setOnDrawingBoundsChangedListener(new c(this));
        cOUIBottomFloatingNavigationBarMenuView.setOnBoundsAnimationsEndAction(new d(this));
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, com.support.bottomnavigation.R.styleable.COUIBottomFloatingNavigationBar, i3, i4);
        int i5 = com.support.bottomnavigation.R.styleable.COUIBottomFloatingNavigationBar_couiNavigationBarMenu;
        if (typedArrayObtainStyledAttributes.hasValue(i5)) {
            inflateMenu(typedArrayObtainStyledAttributes.getResourceId(i5, 0));
        }
        this.mMenuViewCentered = typedArrayObtainStyledAttributes.getBoolean(com.support.bottomnavigation.R.styleable.COUIBottomFloatingNavigationBar_couiMenuViewCentered, false);
        typedArrayObtainStyledAttributes.recycle();
        setClipChildren(false);
        setClipToPadding(false);
        UIUtil.setViewClipExpandSize(this, 0, 120, 0, 0);
        COUIDarkModeUtil.setForceDarkAllow(this, false);
        initConfigInfo();
    }
}