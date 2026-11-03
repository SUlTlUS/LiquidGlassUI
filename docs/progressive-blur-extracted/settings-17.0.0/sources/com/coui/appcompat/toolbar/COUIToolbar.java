package com.coui.appcompat.toolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.graphics.drawable.AnimatedStateListDrawableCompat;
import androidx.appcompat.view.CollapsibleActionView;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ToolbarWidgetWrapper;
import androidx.appcompat.widget.ViewUtils;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import com.coui.appcompat.COUIMaterialBlurEffect;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.coui.appcompat.poplist.COUIPopupListWindow;
import com.coui.appcompat.poplist.COUISubMenuClickListener;
import com.coui.appcompat.poplist.PopupListItem;
import com.coui.appcompat.poplist.PopupMenuConfigRule;
import com.coui.appcompat.searchview.COUISearchBar;
import com.coui.appcompat.state.COUIMaskRippleDrawable;
import com.coui.appcompat.textutil.COUIChangeTextUtil;
import com.coui.appcompat.textviewcompatutil.COUITextViewCompatUtil;
import com.coui.appcompat.uiutil.UIUtil;
import com.google.android.material.navigation.NavigationBarView;
import com.support.toolbar.R;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class COUIToolbar extends Toolbar implements PopupMenuConfigRule {
    private static final int DEFAULT_TEXT_MAX = 24;
    private static final int DEFAULT_TEXT_MIN = 16;
    private static final int SHADOW_CLIP_EXPAND_SIZE_PX = 120;
    private static final String TAG = "Toolbar";
    public static final int TITLE_TYPE_HEAD = 0;
    public static final int TITLE_TYPE_SECONDARY = 1;
    private static final Rect TOOLBAR_OUTSETS = new Rect();
    private MenuPresenter.Callback mActionMenuPresenterCallback;
    private int mButtonGravity;
    private ImageButton mCollapseButtonView;
    private CharSequence mCollapseDescription;
    private Drawable mCollapseIcon;
    private boolean mCollapsible;
    private final COUIRtlSpacingHelper mContentInsets;
    private int mCustomNonBlurFallbackColor;
    private Rect mDisplayFrame;
    private View mDummyView;
    private boolean mEatingHover;
    private boolean mEatingTouch;
    private View mExpandedActionView;
    private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
    private final int mGapBeforeMenuView;
    private int mGapBetweenNavigationAndTitle;
    private int mGapBetweenSearchViewAndMenu;
    private final int mGapBetweenTextButtonAndMenuView;
    private int mGravity;
    private ToolbarGroupTransitionController mGroupTransitionController;
    private ViewTreeObserver.OnPreDrawListener mGroupTransitionPreDraw;
    private boolean mHasCustomViewBeforeTitle;
    private boolean mHasSearchViewFlag;
    private boolean mIsDrawableMaterialEffect;
    boolean mIsInsidePanel;
    private boolean mIsInsideSideNavigationBar;
    private boolean mIsTiny;
    private boolean mIsTitleCenterStyle;
    private ImageView mLogoView;
    private COUIMaskRippleDrawable mMaskRippleDrawable;
    private int mMaterialType;
    private int mMaxButtonHeight;
    private MenuBuilder.Callback mMenuBuilderCallback;
    private COUIActionMenuView mMenuView;
    private final ActionMenuView.OnMenuItemClickListener mMenuViewItemClickListener;
    private int mMinHeight;
    private ToolbarMaterialEffectDelegate mNavButtonEffectDelegate;
    private ImageButton mNavButtonView;
    private Toolbar.OnMenuItemClickListener mOnMenuItemClickListener;
    private Context mPopupContext;
    private boolean mPopupRuleEnable;
    private int mPopupTheme;
    private int mResId;
    private final int[] mSearchCollapsingMargins;
    private boolean mSearchFullWidthFlag;
    private int mSectionGap;
    private int mSectionGapMediumLarge;
    private final int mSectionGapSmall;
    private View mSegmentButton;
    private final int mSegmentButtonHeight;
    private final int mSegmentButtonMaxWidth;
    private final int mSegmentButtonMinWidth;
    private final Runnable mShowOverflowMenuRunnable;
    private int mStyle;
    private CharSequence mSubtitleText;
    private int mSubtitleTextAppearance;
    private int mSubtitleTextColor;
    private TextView mSubtitleTextView;
    private final int[] mTempMargins;
    private final ArrayList<View> mTempViews;
    private View mTextButton;
    private ToolbarMaterialEffectDelegate mTextButtonEffectDelegate;
    private float mTextMaxSize;
    private float mTextMinSize;
    private ToolbarTextTransitionController mTextTransitionController;
    private ViewTreeObserver.OnPreDrawListener mTextTransitionPreDraw;
    private final RectF mTitleExitOverlayOldRect;
    private CharSequence mTitleExitOverlayOldTitle;
    private boolean mTitleExitOverlayPrepared;
    private TextView mTitleExitOverlayView;
    private int mTitleMarginBottom;
    private int mTitleMarginEnd;
    private int mTitleMarginStart;
    private int mTitleMarginTop;
    private int mTitleMinWidth;
    private int mTitlePaddingBottom;
    private int mTitlePaddingTop;
    private int[] mTitlePosition;
    private CharSequence mTitleText;
    private int mTitleTextAppearance;
    private int mTitleTextColor;
    private final int mTitleTextMinWidth;
    private float mTitleTextSize;
    private TextView mTitleTextView;
    private int mTitleType;
    private int mToolbarCenterTitlePaddingLeft;
    private int mToolbarCenterTitlePaddingRight;
    private int mToolbarHeight;
    private int mToolbarNormalPaddingLeft;
    private int mToolbarNormalPaddingRight;
    private int mToolbarOverFlowPadding;
    private int mUseMaterialEffectAttr;
    private boolean mUseResponsivePadding;
    private Rect mWindowFrame;
    private ToolbarWidgetWrapper mWrapper;

    /* JADX INFO: renamed from: com.coui.appcompat.toolbar.COUIToolbar$1, reason: invalid class name */
    public class AnonymousClass1 implements ActionMenuView.OnMenuItemClickListener {
        public AnonymousClass1() {
        }

        @Override // androidx.appcompat.widget.ActionMenuView.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (COUIToolbar.access$000(COUIToolbar.this) != null) {
                return COUIToolbar.access$000(COUIToolbar.this).onMenuItemClick(menuItem);
            }
            return false;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.toolbar.COUIToolbar$2, reason: invalid class name */
    public class AnonymousClass2 implements Runnable {
        public AnonymousClass2() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUIToolbar.this.showOverflowMenu();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.toolbar.COUIToolbar$3, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass3 implements View.OnClickListener {
        public AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            COUIToolbar.this.collapseActionView();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.toolbar.COUIToolbar$4, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass4 implements ViewTreeObserver.OnPreDrawListener {
        final /* synthetic */ boolean val$enterOnlyAfterParallelTitleExit;
        final /* synthetic */ CharSequence val$targetTitle;

        public AnonymousClass4(CharSequence charSequence, boolean z2) {
            this.val$targetTitle = charSequence;
            this.val$enterOnlyAfterParallelTitleExit = z2;
        }

        public static /* synthetic */ void a(AnonymousClass4 anonymousClass4) {
            COUIToolbar.access$1200(COUIToolbar.this);
        }

        public static /* synthetic */ void b(AnonymousClass4 anonymousClass4) {
            COUIToolbar.access$1200(COUIToolbar.this);
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            ViewTreeObserver viewTreeObserver = COUIToolbar.this.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeOnPreDrawListener(this);
            }
            COUIToolbar.access$802(COUIToolbar.this, null);
            CharSequence charSequence = this.val$targetTitle;
            if (charSequence != null) {
                COUIToolbar.this.setTitle(charSequence);
            }
            ToolbarTextTransitionController toolbarTextTransitionControllerAccess$900 = COUIToolbar.access$900(COUIToolbar.this);
            if (!this.val$enterOnlyAfterParallelTitleExit) {
                toolbarTextTransitionControllerAccess$900.startCrossFade(COUIToolbar.access$1100(COUIToolbar.this), COUIToolbar.access$1000(COUIToolbar.this), new o(this));
                return true;
            }
            if (COUIToolbar.access$1000(COUIToolbar.this) != null) {
                COUIToolbar.access$1000(COUIToolbar.this).setAlpha(0.0f);
            }
            toolbarTextTransitionControllerAccess$900.startCrossFade(null, COUIToolbar.access$1000(COUIToolbar.this), new n(this));
            return true;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.toolbar.COUIToolbar$5, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass5 implements ViewTreeObserver.OnPreDrawListener {
        final /* synthetic */ Runnable val$action;

        public AnonymousClass5(Runnable runnable) {
            this.val$action = runnable;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            ViewTreeObserver viewTreeObserver = COUIToolbar.this.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeOnPreDrawListener(this);
            }
            COUIToolbar.access$702(COUIToolbar.this, null);
            this.val$action.run();
            return true;
        }
    }

    /* JADX INFO: loaded from: classes7.dex */
    @FunctionalInterface
    public interface EditModeTitleExitStarter {
        void startTitleExit(Runnable runnable);
    }

    /* JADX INFO: loaded from: classes7.dex */
    public class ExpandedActionViewMenuPresenter implements MenuPresenter {
        MenuItemImpl mCurrentExpandedItem;
        MenuBuilder mMenu;

        private ExpandedActionViewMenuPresenter() {
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
            if (COUIToolbar.access$400(COUIToolbar.this) instanceof CollapsibleActionView) {
                ((CollapsibleActionView) COUIToolbar.access$400(COUIToolbar.this)).onActionViewCollapsed();
            }
            COUIToolbar cOUIToolbar = COUIToolbar.this;
            cOUIToolbar.removeView(COUIToolbar.access$400(cOUIToolbar));
            COUIToolbar cOUIToolbar2 = COUIToolbar.this;
            cOUIToolbar2.removeView(COUIToolbar.access$300(cOUIToolbar2));
            COUIToolbar.access$402(COUIToolbar.this, null);
            COUIToolbar.access$600(COUIToolbar.this, false);
            this.mCurrentExpandedItem = null;
            COUIToolbar.this.requestLayout();
            menuItemImpl.setActionViewExpanded(false);
            return true;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
            COUIToolbar.access$200(COUIToolbar.this);
            ViewParent parent = COUIToolbar.access$300(COUIToolbar.this).getParent();
            COUIToolbar cOUIToolbar = COUIToolbar.this;
            if (parent != cOUIToolbar) {
                cOUIToolbar.addView(COUIToolbar.access$300(cOUIToolbar));
            }
            COUIToolbar.access$402(COUIToolbar.this, menuItemImpl.getActionView());
            this.mCurrentExpandedItem = menuItemImpl;
            ViewParent parent2 = COUIToolbar.access$400(COUIToolbar.this).getParent();
            COUIToolbar cOUIToolbar2 = COUIToolbar.this;
            if (parent2 != cOUIToolbar2) {
                LayoutParams layoutParamsGenerateDefaultLayoutParams = cOUIToolbar2.generateDefaultLayoutParams();
                layoutParamsGenerateDefaultLayoutParams.gravity = (COUIToolbar.access$500(COUIToolbar.this) & 112) | 8388611;
                layoutParamsGenerateDefaultLayoutParams.mViewType = 2;
                COUIToolbar.access$400(COUIToolbar.this).setLayoutParams(layoutParamsGenerateDefaultLayoutParams);
                COUIToolbar cOUIToolbar3 = COUIToolbar.this;
                cOUIToolbar3.addView(COUIToolbar.access$400(cOUIToolbar3));
            }
            COUIToolbar.access$600(COUIToolbar.this, true);
            COUIToolbar.this.requestLayout();
            menuItemImpl.setActionViewExpanded(true);
            if (COUIToolbar.access$400(COUIToolbar.this) instanceof CollapsibleActionView) {
                ((CollapsibleActionView) COUIToolbar.access$400(COUIToolbar.this)).onActionViewExpanded();
            }
            return true;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public boolean flagActionItems() {
            return false;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public int getId() {
            return 0;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public MenuView getMenuView(ViewGroup viewGroup) {
            return null;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public void initForMenu(Context context, MenuBuilder menuBuilder) {
            MenuItemImpl menuItemImpl;
            MenuBuilder menuBuilder2 = this.mMenu;
            if (menuBuilder2 != null && (menuItemImpl = this.mCurrentExpandedItem) != null) {
                menuBuilder2.collapseItemActionView(menuItemImpl);
            }
            this.mMenu = menuBuilder;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public void onCloseMenu(MenuBuilder menuBuilder, boolean z2) {
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public void onRestoreInstanceState(Parcelable parcelable) {
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public Parcelable onSaveInstanceState() {
            return null;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
            return subMenuBuilder != null && subMenuBuilder.size() > 0;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public void setCallback(MenuPresenter.Callback callback) {
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public void updateMenuView(boolean z2) {
            if (this.mCurrentExpandedItem != null) {
                MenuBuilder menuBuilder = this.mMenu;
                if (menuBuilder != null) {
                    int size = menuBuilder.size();
                    for (int i3 = 0; i3 < size; i3++) {
                        if (this.mMenu.getItem(i3) == this.mCurrentExpandedItem) {
                            return;
                        }
                    }
                }
                collapseItemActionView(this.mMenu, this.mCurrentExpandedItem);
            }
        }

        public /* synthetic */ ExpandedActionViewMenuPresenter(COUIToolbar cOUIToolbar, AnonymousClass1 anonymousClass1) {
            this();
        }
    }

    /* JADX INFO: loaded from: classes7.dex */
    public enum MaterialEffectState {
        FORCE_ENABLE(1),
        FORCE_DISABLE(-1),
        FOLLOW_SYSTEM(0);

        private final int mValue;

        MaterialEffectState(int i3) {
            this.mValue = i3;
        }

        public int getValue() {
            return this.mValue;
        }
    }

    public COUIToolbar(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void a(COUIToolbar cOUIToolbar, boolean z2, CharSequence charSequence, boolean z3) {
        if (!z2) {
            cOUIToolbar.getClass();
            return;
        }
        cOUIToolbar.removeTextTransitionPreDraw();
        cOUIToolbar.mTextTransitionPreDraw = cOUIToolbar.new AnonymousClass4(charSequence, z3);
        cOUIToolbar.getViewTreeObserver().addOnPreDrawListener(cOUIToolbar.mTextTransitionPreDraw);
    }

    public static /* synthetic */ Toolbar.OnMenuItemClickListener access$000(COUIToolbar cOUIToolbar) {
        return cOUIToolbar.mOnMenuItemClickListener;
    }

    public static /* synthetic */ TextView access$1000(COUIToolbar cOUIToolbar) {
        return cOUIToolbar.mTitleTextView;
    }

    public static /* synthetic */ View access$1100(COUIToolbar cOUIToolbar) {
        return cOUIToolbar.getPreparedTitleExitOverlayForAnimation();
    }

    public static /* synthetic */ void access$1200(COUIToolbar cOUIToolbar) {
        cOUIToolbar.resetTitleExitOverlayState();
    }

    public static /* synthetic */ void access$200(COUIToolbar cOUIToolbar) {
        cOUIToolbar.ensureCollapseButtonView();
    }

    public static /* synthetic */ ImageButton access$300(COUIToolbar cOUIToolbar) {
        return cOUIToolbar.mCollapseButtonView;
    }

    public static /* synthetic */ View access$400(COUIToolbar cOUIToolbar) {
        return cOUIToolbar.mExpandedActionView;
    }

    public static /* synthetic */ View access$402(COUIToolbar cOUIToolbar, View view) {
        cOUIToolbar.mExpandedActionView = view;
        return view;
    }

    public static /* synthetic */ int access$500(COUIToolbar cOUIToolbar) {
        return cOUIToolbar.mButtonGravity;
    }

    public static /* synthetic */ void access$600(COUIToolbar cOUIToolbar, boolean z2) {
        cOUIToolbar.setChildVisibilityForExpandedActionView(z2);
    }

    public static /* synthetic */ ViewTreeObserver.OnPreDrawListener access$702(COUIToolbar cOUIToolbar, ViewTreeObserver.OnPreDrawListener onPreDrawListener) {
        cOUIToolbar.mGroupTransitionPreDraw = onPreDrawListener;
        return onPreDrawListener;
    }

    public static /* synthetic */ ViewTreeObserver.OnPreDrawListener access$802(COUIToolbar cOUIToolbar, ViewTreeObserver.OnPreDrawListener onPreDrawListener) {
        cOUIToolbar.mTextTransitionPreDraw = onPreDrawListener;
        return onPreDrawListener;
    }

    public static /* synthetic */ ToolbarTextTransitionController access$900(COUIToolbar cOUIToolbar) {
        return cOUIToolbar.ensureTextTransitionController();
    }

    private void addCustomCenterViews(List<View> list) {
        int childCount = getChildCount();
        int absoluteGravity = GravityCompat.getAbsoluteGravity(1, ViewCompat.getLayoutDirection(this));
        list.clear();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            if (layoutParams.mViewType == 0 && shouldLayout(childAt) && getChildHorizontalGravity(layoutParams.gravity) == absoluteGravity) {
                list.add(childAt);
            }
        }
    }

    private void addCustomViewsWithGravity(List<View> list, int i3) {
        boolean z2 = ViewCompat.getLayoutDirection(this) == 1;
        int childCount = getChildCount();
        int absoluteGravity = GravityCompat.getAbsoluteGravity(i3, ViewCompat.getLayoutDirection(this));
        list.clear();
        if (!z2) {
            for (int i4 = 0; i4 < childCount; i4++) {
                View childAt = getChildAt(i4);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.mViewType == 0 && shouldLayout(childAt) && getChildHorizontalGravity(layoutParams.gravity) == absoluteGravity) {
                    list.add(childAt);
                }
            }
            return;
        }
        for (int i5 = childCount - 1; i5 >= 0; i5--) {
            View childAt2 = getChildAt(i5);
            LayoutParams layoutParams2 = (LayoutParams) childAt2.getLayoutParams();
            if (layoutParams2.mViewType == 0 && shouldLayout(childAt2) && getChildHorizontalGravity(layoutParams2.gravity) == absoluteGravity) {
                list.add(childAt2);
            }
        }
    }

    private void addSystemView(View view) {
        LayoutParams layoutParamsGenerateLayoutParams;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParamsGenerateLayoutParams = generateDefaultLayoutParams();
        } else {
            layoutParamsGenerateLayoutParams = !checkLayoutParams(layoutParams) ? generateLayoutParams(layoutParams) : (LayoutParams) layoutParams;
        }
        layoutParamsGenerateLayoutParams.mViewType = 1;
        addView(view, layoutParamsGenerateLayoutParams);
    }

    public static void attachTitleOverlayForCrossFade(ViewGroup viewGroup, View view, RectF rectF, int i3) {
        ViewCompat.setLayoutDirection(view, ViewCompat.getLayoutDirection(viewGroup));
        int iMax = Math.max(Math.round(rectF.width()), 1);
        int iMax2 = Math.max(Math.round(rectF.height()), 1);
        view.measure(View.MeasureSpec.makeMeasureSpec(iMax, 1073741824), View.MeasureSpec.makeMeasureSpec(iMax2, 1073741824));
        view.layout(0, 0, iMax, iMax2);
        view.setX(rectF.left);
        view.setY(rectF.top);
        view.setAlpha(1.0f);
        view.setVisibility(i3);
        viewGroup.getOverlay().remove(view);
        viewGroup.getOverlay().add(view);
    }

    public static /* synthetic */ void b(COUIToolbar cOUIToolbar, Runnable runnable, Runnable runnable2) {
        if (runnable != null) {
            cOUIToolbar.getClass();
            runnable.run();
        }
        cOUIToolbar.schedulePhaseInIncomingMenuTransitionAfterStructure(runnable2);
    }

    private EditModeTitleExitStarter buildEditModeTitleExitStarter(boolean z2) {
        if (z2 && this.mTitleExitOverlayPrepared) {
            return new g(this);
        }
        return null;
    }

    private Runnable buildRegisterTitleCrossFadePreDrawRunnable(boolean z2, CharSequence charSequence, boolean z3) {
        return new i(this, z2, charSequence, z3);
    }

    public static /* synthetic */ void c(COUIToolbar cOUIToolbar, Runnable runnable) {
        cOUIToolbar.schedulePhaseInIncomingMenuTransition(runnable);
    }

    private void calculateTitlePosition(int[] iArr) {
        int measuredWidth;
        int measuredWidth2;
        boolean z2 = ViewCompat.getLayoutDirection(this) == 1;
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.coui_actionbar_menuitemview_item_spacing);
        iArr[0] = Math.max(this.mContentInsets.getLeft(), getPaddingLeft());
        iArr[1] = getMeasuredWidth() - Math.max(this.mContentInsets.getRight(), getPaddingRight());
        if (!shouldLayout(this.mMenuView) || this.mMenuView.getChildCount() == 0) {
            return;
        }
        if (this.mMenuView.getChildCount() == 1) {
            measuredWidth2 = this.mMenuView.getChildAt(0).getMeasuredWidth() + dimensionPixelSize;
            measuredWidth = 0;
        } else {
            measuredWidth = this.mMenuView.getChildAt(0).getMeasuredWidth() + dimensionPixelSize;
            measuredWidth2 = 0;
            for (int i3 = 1; i3 < this.mMenuView.getChildCount(); i3++) {
                measuredWidth2 += this.mMenuView.getChildAt(i3).getMeasuredWidth() + dimensionPixelSize;
            }
        }
        if (z2) {
            iArr[0] = iArr[0] + measuredWidth2;
            iArr[1] = iArr[1] - measuredWidth;
        } else {
            iArr[0] = iArr[0] + measuredWidth;
            iArr[1] = iArr[1] - measuredWidth2;
        }
        int iMax = Math.max(iArr[0], getMeasuredWidth() - iArr[1]) + getResources().getDimensionPixelSize(R.dimen.coui_toolbar_action_menu_inner_padding);
        if (this.mIsInsidePanel || COUIChangeTextUtil.measureTextLineCount(this.mTitleTextView, getMeasuredWidth(), iMax * 2) > 1) {
            return;
        }
        iArr[0] = iMax;
        iArr[1] = getMeasuredWidth() - iMax;
    }

    private void calculateToolbarPadding(MenuBuilder menuBuilder, int i3, boolean z2) {
        boolean z3 = true;
        boolean z4 = this.mHasCustomViewBeforeTitle || shouldLayout(this.mNavButtonView);
        if (!hasMenuItem(menuBuilder) && !z2) {
            z3 = false;
        }
        boolean zIsMaterialEffectOsSupported = ToolbarMaterialHelper.isMaterialEffectOsSupported();
        if (COUIResponsiveUtils.isSmallScreen(getContext(), View.MeasureSpec.getSize(i3))) {
            if (zIsMaterialEffectOsSupported) {
                this.mToolbarNormalPaddingLeft = getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_normal_padding_left_compat);
                this.mToolbarNormalPaddingRight = getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_normal_padding_right_compat);
            } else {
                this.mToolbarNormalPaddingLeft = getContext().getResources().getDimensionPixelOffset(z4 ? R.dimen.toolbar_normal_menu_padding_left_compat : R.dimen.toolbar_normal_padding_left_compat);
                this.mToolbarNormalPaddingRight = z3 ? getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_normal_menu_padding_right_compat) : getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_normal_padding_right_compat);
            }
            this.mToolbarCenterTitlePaddingLeft = getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_center_menu_padding_horizontal_compat);
        } else if (COUIResponsiveUtils.isMediumScreen(getContext(), View.MeasureSpec.getSize(i3), UIUtil.getScreenHeightMetrics(getContext()))) {
            if (zIsMaterialEffectOsSupported) {
                this.mToolbarNormalPaddingLeft = getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_normal_padding_left_medium);
                this.mToolbarNormalPaddingRight = getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_normal_padding_right_medium);
            } else {
                this.mToolbarNormalPaddingLeft = getContext().getResources().getDimensionPixelOffset(z4 ? R.dimen.toolbar_normal_menu_padding_left_medium : R.dimen.toolbar_normal_padding_left_medium);
                this.mToolbarNormalPaddingRight = z3 ? getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_normal_menu_padding_right_medium) : getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_normal_padding_right_medium);
            }
            this.mToolbarCenterTitlePaddingLeft = getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_center_menu_padding_horizontal_medium);
        } else if (COUIResponsiveUtils.isLargeScreen(getContext(), View.MeasureSpec.getSize(i3), UIUtil.getScreenHeightMetrics(getContext()))) {
            if (zIsMaterialEffectOsSupported) {
                this.mToolbarNormalPaddingLeft = getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_normal_padding_left_expanded);
                this.mToolbarNormalPaddingRight = getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_normal_padding_right_expanded);
            } else {
                this.mToolbarNormalPaddingLeft = getContext().getResources().getDimensionPixelOffset(z4 ? R.dimen.toolbar_normal_menu_padding_left_expanded : R.dimen.toolbar_normal_padding_left_expanded);
                this.mToolbarNormalPaddingRight = z3 ? getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_normal_menu_padding_right_expanded) : getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_normal_padding_right_expanded);
            }
            this.mToolbarCenterTitlePaddingLeft = getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_center_menu_padding_horizontal_expanded);
        }
        if (this.mIsInsideSideNavigationBar) {
            this.mToolbarCenterTitlePaddingLeft = getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_center_menu_padding_horizontal_medium);
        }
        this.mToolbarCenterTitlePaddingRight = this.mToolbarCenterTitlePaddingLeft;
        if (this.mIsTiny) {
            this.mToolbarNormalPaddingLeft = z4 ? 0 : getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_normal_menu_padding_tiny_left);
            this.mToolbarNormalPaddingRight = getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_normal_menu_padding_tiny_right);
        }
    }

    private void captureTitleExitOldPosition() {
        TextView textView = this.mTitleTextView;
        if (textView == null) {
            this.mTitleExitOverlayOldRect.setEmpty();
            return;
        }
        this.mTitleExitOverlayOldTitle = textView.getText();
        int left = this.mTitleTextView.getLeft();
        int top = this.mTitleTextView.getTop();
        this.mTitleExitOverlayOldRect.set(left, top, left + Math.max(this.mTitleTextView.getWidth(), 1), top + Math.max(this.mTitleTextView.getHeight(), 1));
    }

    private void changeBackViewParams() {
        ImageButton imageButton = this.mNavButtonView;
        if (imageButton == null || !this.mIsTiny) {
            return;
        }
        LayoutParams layoutParams = (LayoutParams) imageButton.getLayoutParams();
        ((ViewGroup.MarginLayoutParams) layoutParams).width = getContext().getResources().getDimensionPixelOffset(R.dimen.coui_toolbar_back_view_tiny_width);
        this.mNavButtonView.setLayoutParams(layoutParams);
        this.mNavButtonView.setPadding(0, 0, 0, 0);
    }

    private void changeToolbarPadding(MenuBuilder menuBuilder, ImageButton imageButton, boolean z2, int i3, boolean z3) {
        int i4;
        if (menuBuilder == null && imageButton == null && !z3) {
            return;
        }
        calculateToolbarPadding(menuBuilder, i3, z3);
        if (this.mUseResponsivePadding) {
            boolean z4 = hasMenuItem(menuBuilder) || z3;
            boolean z5 = this.mIsTitleCenterStyle;
            int i5 = z5 ? this.mToolbarCenterTitlePaddingLeft : this.mToolbarNormalPaddingLeft;
            if (z4) {
                i4 = z5 ? this.mToolbarCenterTitlePaddingLeft : this.mToolbarNormalPaddingLeft;
            } else {
                i4 = useTextMenuItemPaddingEnd() ? this.mToolbarCenterTitlePaddingRight : this.mToolbarNormalPaddingRight;
            }
            if (z2) {
                setPadding(i4, getPaddingTop(), i5, getPaddingBottom());
            } else {
                setPadding(i5, getPaddingTop(), i4, getPaddingBottom());
            }
        }
    }

    private void clampTitleWidthIfNeeded(TextView textView, int i3) {
        if (textView == null || i3 < 0 || textView.getMeasuredWidth() <= i3) {
            return;
        }
        textView.measure(View.MeasureSpec.makeMeasureSpec(i3, 1073741824), View.MeasureSpec.makeMeasureSpec(textView.getMeasuredHeight(), 1073741824));
    }

    private List<View> collectToolbarActionViewsForEditTransition() {
        ArrayList arrayList = new ArrayList();
        if (shouldLayout(this.mNavButtonView)) {
            arrayList.add(this.mNavButtonView);
        }
        arrayList.addAll(resolveVisibleMenuItemViews());
        return arrayList;
    }

    private void configNavigationButtonBackground() {
        COUIMaskRippleDrawable cOUIMaskRippleDrawable = new COUIMaskRippleDrawable(getContext());
        this.mMaskRippleDrawable = cOUIMaskRippleDrawable;
        cOUIMaskRippleDrawable.setCircleRippleMask(COUIMaskRippleDrawable.getMaskRippleRadiusByType(getContext(), 0));
        this.mNavButtonView.setBackground(this.mMaskRippleDrawable);
    }

    private Runnable createStructureSwitchOnceRunnable(Runnable runnable) {
        return new h(new boolean[]{false}, runnable);
    }

    public static /* synthetic */ void d(COUIToolbar cOUIToolbar, Runnable runnable) {
        cOUIToolbar.resetTitleExitOverlayState();
        runnable.run();
    }

    public static /* synthetic */ void e(COUIToolbar cOUIToolbar, Runnable runnable, Runnable runnable2, EditModeTitleExitStarter editModeTitleExitStarter, ToolbarGroupTransitionController toolbarGroupTransitionController) {
        List<View> listCollectToolbarActionViewsForEditTransition = cOUIToolbar.collectToolbarActionViewsForEditTransition();
        j jVar = new j(cOUIToolbar, runnable, runnable2);
        int i3 = !listCollectToolbarActionViewsForEditTransition.isEmpty() ? 1 : 0;
        if (editModeTitleExitStarter != null) {
            i3++;
        }
        if (i3 == 0) {
            jVar.run();
            return;
        }
        k kVar = new k(new int[]{i3}, jVar);
        if (!listCollectToolbarActionViewsForEditTransition.isEmpty()) {
            toolbarGroupTransitionController.startTransition(listCollectToolbarActionViewsForEditTransition, false, kVar);
        }
        if (editModeTitleExitStarter != null) {
            editModeTitleExitStarter.startTitleExit(kVar);
        }
    }

    private void ensureCollapseButtonView() {
        if (this.mCollapseButtonView == null) {
            ImageButton imageButton = new ImageButton(getContext(), null, R.attr.couiToolbarNavigationButtonStyle, R.style.Widget_COUI_Toolbar_Button_Navigation);
            this.mCollapseButtonView = imageButton;
            imageButton.setImageDrawable(this.mCollapseIcon);
            this.mCollapseButtonView.setContentDescription(this.mCollapseDescription);
            LayoutParams layoutParamsGenerateDefaultLayoutParams = generateDefaultLayoutParams();
            layoutParamsGenerateDefaultLayoutParams.gravity = (this.mButtonGravity & 112) | 8388611;
            layoutParamsGenerateDefaultLayoutParams.mViewType = 2;
            this.mCollapseButtonView.setLayoutParams(layoutParamsGenerateDefaultLayoutParams);
            this.mCollapseButtonView.setOnClickListener(new AnonymousClass3());
        }
    }

    private ToolbarGroupTransitionController ensureGroupTransitionController() {
        if (this.mGroupTransitionController == null) {
            this.mGroupTransitionController = new ToolbarGroupTransitionController();
        }
        return this.mGroupTransitionController;
    }

    private void ensureLogoView() {
        if (this.mLogoView == null) {
            this.mLogoView = new ImageView(getContext());
        }
    }

    private void ensureMenu() {
        ensureMenuView();
        if (this.mMenuView.peekMenu() == null) {
            MenuBuilder menuBuilder = (MenuBuilder) this.mMenuView.getMenu();
            if (this.mExpandedMenuPresenter == null) {
                this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter(this, null);
            }
            this.mMenuView.setExpandedActionViewsExclusive(true);
            menuBuilder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
        }
    }

    private void ensureMenuView() {
        if (this.mMenuView == null) {
            COUIActionMenuView cOUIActionMenuView = new COUIActionMenuView(getContext());
            this.mMenuView = cOUIActionMenuView;
            cOUIActionMenuView.setMenuItemUseMaterialEffectAttr(this.mUseMaterialEffectAttr);
            if (this.mIsDrawableMaterialEffect) {
                this.mMenuView.setMenuItemIsDrawableMaterialEffect(true);
            }
            int i3 = this.mCustomNonBlurFallbackColor;
            if (i3 != 0) {
                this.mMenuView.setMenuItemNonBlurFallbackColor(i3);
            }
            this.mMenuView.setMenuItemMaterialEffectType(this.mMaterialType);
            this.mMenuView.setId(R.id.coui_toolbar_more_view);
            this.mMenuView.setPopupTheme(this.mPopupTheme);
            this.mMenuView.setOnMenuItemClickListener(this.mMenuViewItemClickListener);
            this.mMenuView.setMenuCallbacks(this.mActionMenuPresenterCallback, this.mMenuBuilderCallback);
            LayoutParams layoutParamsGenerateDefaultLayoutParams = generateDefaultLayoutParams();
            if (this.mIsTitleCenterStyle) {
                ((ViewGroup.MarginLayoutParams) layoutParamsGenerateDefaultLayoutParams).width = -1;
            } else {
                ((ViewGroup.MarginLayoutParams) layoutParamsGenerateDefaultLayoutParams).width = -2;
            }
            layoutParamsGenerateDefaultLayoutParams.gravity = (this.mButtonGravity & 112) | GravityCompat.END;
            this.mMenuView.setLayoutParams(layoutParamsGenerateDefaultLayoutParams);
            addSystemView(this.mMenuView);
        }
    }

    private void ensureNavButtonView() {
        if (this.mNavButtonView == null) {
            ImageButton imageButton = new ImageButton(getContext(), null, R.attr.couiToolbarNavigationButtonStyle, R.style.Widget_COUI_Toolbar_Button_Navigation);
            this.mNavButtonView = imageButton;
            imageButton.setId(R.id.coui_toolbar_back_view);
            COUIDarkModeUtil.setForceDarkAllow(this.mNavButtonView, false);
            LayoutParams layoutParamsGenerateDefaultLayoutParams = generateDefaultLayoutParams();
            layoutParamsGenerateDefaultLayoutParams.gravity = (this.mButtonGravity & 112) | 8388611;
            this.mNavButtonView.setLayoutParams(layoutParamsGenerateDefaultLayoutParams);
            if (ToolbarMaterialHelper.isMaterialEffectOsSupported()) {
                ToolbarMaterialHelper.applyIconButtonSize(this.mNavButtonView);
                this.mNavButtonEffectDelegate = ToolbarMaterialEffectDelegate.forToolbarButton(this.mNavButtonView);
                if (this.mUseMaterialEffectAttr != MaterialEffectState.FOLLOW_SYSTEM.getValue()) {
                    this.mNavButtonEffectDelegate.setUseMaterialEffectAttr(this.mUseMaterialEffectAttr);
                }
                if (this.mIsDrawableMaterialEffect) {
                    this.mNavButtonEffectDelegate.setIsDrawableMaterialEffect(true);
                }
                int i3 = this.mCustomNonBlurFallbackColor;
                if (i3 != 0) {
                    this.mNavButtonEffectDelegate.setCustomNonBlurFallbackColor(i3);
                }
                this.mNavButtonEffectDelegate.setMaterialEffectType(this.mMaterialType);
            } else {
                configNavigationButtonBackground();
            }
            changeBackViewParams();
        }
    }

    private ToolbarTextTransitionController ensureTextTransitionController() {
        if (this.mTextTransitionController == null) {
            this.mTextTransitionController = new ToolbarTextTransitionController();
        }
        return this.mTextTransitionController;
    }

    private void ensureTitleExitOverlayView() {
        if (this.mTitleExitOverlayView == null) {
            TextView textView = new TextView(getContext());
            this.mTitleExitOverlayView = textView;
            textView.setSingleLine();
            this.mTitleExitOverlayView.setEllipsize(TextUtils.TruncateAt.END);
            this.mTitleExitOverlayView.setVisibility(8);
            this.mTitleExitOverlayView.setIncludeFontPadding(false);
            this.mTitleExitOverlayView.setImportantForAccessibility(2);
        }
    }

    private void ensureTitleTextView() {
        if (this.mTitleTextView == null) {
            Context context = getContext();
            TextView textView = new TextView(context);
            this.mTitleTextView = textView;
            textView.setPaddingRelative(0, this.mTitlePaddingTop, 0, this.mTitlePaddingBottom);
            LayoutParams layoutParamsGenerateDefaultLayoutParams = generateDefaultLayoutParams();
            layoutParamsGenerateDefaultLayoutParams.mTypeTitle = true;
            ((ViewGroup.MarginLayoutParams) layoutParamsGenerateDefaultLayoutParams).bottomMargin = this.mIsTiny ? 0 : this.mTitleMarginBottom;
            layoutParamsGenerateDefaultLayoutParams.gravity = (this.mButtonGravity & 112) | GravityCompat.END;
            this.mTitleTextView.setLayoutParams(layoutParamsGenerateDefaultLayoutParams);
            this.mTitleTextView.setSingleLine();
            this.mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
            int i3 = this.mTitleTextAppearance;
            if (i3 != 0) {
                setTitleTextAppearance(context, i3);
            }
            int i4 = this.mTitleTextColor;
            if (i4 != 0) {
                this.mTitleTextView.setTextColor(i4);
            }
            this.mTitleTextView.setTextAlignment(this.mIsTitleCenterStyle ? 4 : 5);
            this.mTitleTextView.setTextDirection(5);
            if (this.mTitleType == 1) {
                this.mTitleTextView.setTextSize(0, COUIChangeTextUtil.getSuitableFontSize(this.mTitleTextView.getTextSize(), getContext().getResources().getConfiguration().fontScale, 2));
            }
        }
    }

    public static /* synthetic */ void f(COUIToolbar cOUIToolbar) {
        cOUIToolbar.ensureGroupTransitionController().startTransition(cOUIToolbar.collectToolbarActionViewsForEditTransition(), true, null);
    }

    public static /* synthetic */ void g(boolean[] zArr, Runnable runnable) {
        if (zArr[0]) {
            return;
        }
        zArr[0] = true;
        if (runnable != null) {
            runnable.run();
        }
    }

    private int getChildHorizontalGravity(int i3) {
        int layoutDirection = ViewCompat.getLayoutDirection(this);
        int absoluteGravity = GravityCompat.getAbsoluteGravity(i3, layoutDirection) & 7;
        if (absoluteGravity == 1 || absoluteGravity == 3 || absoluteGravity == 5) {
            return absoluteGravity;
        }
        return layoutDirection == 1 ? 5 : 3;
    }

    private int getChildTop(View view, int i3) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int measuredHeight = view.getMeasuredHeight();
        int i4 = i3 > 0 ? (measuredHeight - i3) / 2 : 0;
        int childVerticalGravity = getChildVerticalGravity(layoutParams.gravity);
        if (childVerticalGravity == 48) {
            return getPaddingTop() - i4;
        }
        if (childVerticalGravity == 80) {
            return (((getHeight() - getPaddingBottom()) - measuredHeight) - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin) - i4;
        }
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int height = getHeight();
        int iMax = (((height - paddingTop) - paddingBottom) - measuredHeight) / 2;
        int i5 = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
        if (iMax < i5) {
            iMax = i5;
        } else {
            int i6 = (((height - paddingBottom) - measuredHeight) - iMax) - paddingTop;
            int i7 = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
            if (i6 < i7) {
                iMax = Math.max(0, iMax - (i7 - i6));
            }
        }
        return paddingTop + iMax;
    }

    private int getChildVerticalGravity(int i3) {
        int i4 = i3 & 112;
        return (i4 == 16 || i4 == 48 || i4 == 80) ? i4 : this.mGravity & 112;
    }

    private int getHorizontalMargins(View view) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return MarginLayoutParamsCompat.getMarginStart(marginLayoutParams) + MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams);
    }

    private int getMinimumHeightCompat() {
        return ViewCompat.getMinimumHeight(this);
    }

    private View getPreparedTitleExitOverlayForAnimation() {
        if (!this.mTitleExitOverlayPrepared || this.mTitleExitOverlayView == null || this.mTitleExitOverlayOldRect.isEmpty()) {
            return null;
        }
        attachTitleOverlayForCrossFade(this, this.mTitleExitOverlayView, this.mTitleExitOverlayOldRect, 0);
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setAlpha(0.0f);
        }
        return this.mTitleExitOverlayView;
    }

    private int getVerticalMargins(View view) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
    }

    private int getViewListMeasuredWidth(List<View> list, int[] iArr) {
        int i3 = iArr[0];
        int i4 = iArr[1];
        int size = list.size();
        int i5 = 0;
        int measuredWidth = 0;
        while (i5 < size) {
            View view = list.get(i5);
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            int i6 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin - i3;
            int i7 = ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin - i4;
            int iMax = Math.max(0, i6);
            int iMax2 = Math.max(0, i7);
            int iMax3 = Math.max(0, -i6);
            int iMax4 = Math.max(0, -i7);
            measuredWidth += iMax + view.getMeasuredWidth() + iMax2;
            i5++;
            i4 = iMax4;
            i3 = iMax3;
        }
        return measuredWidth;
    }

    public static /* synthetic */ void h(COUIToolbar cOUIToolbar, Runnable runnable) {
        View preparedTitleExitOverlayForAnimation = cOUIToolbar.getPreparedTitleExitOverlayForAnimation();
        if (preparedTitleExitOverlayForAnimation == null) {
            runnable.run();
        } else {
            cOUIToolbar.ensureTextTransitionController().startCrossFade(preparedTitleExitOverlayForAnimation, null, new l(cOUIToolbar, runnable));
        }
    }

    private boolean hasMenuItem(MenuBuilder menuBuilder) {
        if (menuBuilder != null) {
            return (menuBuilder.getNonActionItems().isEmpty() && menuBuilder.getActionItems().isEmpty()) ? false : true;
        }
        return false;
    }

    public static /* synthetic */ void i(int[] iArr, Runnable runnable) {
        int i3 = iArr[0] - 1;
        iArr[0] = i3;
        if (i3 == 0) {
            runnable.run();
        }
    }

    private boolean isDummyView(View view, LayoutParams layoutParams) {
        if (view == null || view.getClass() != View.class) {
            return false;
        }
        this.mDummyView = view;
        return true;
    }

    private int layoutChildLeft(View view, int i3, int i4, int[] iArr, int i5) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int i6 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin - iArr[0];
        int iMax = i3 + Math.max(0, i6);
        iArr[0] = Math.max(0, -i6);
        int childTop = getChildTop(view, i5);
        int measuredWidth = view.getMeasuredWidth();
        view.layout(iMax, childTop, Math.min(i4, iMax + measuredWidth), view.getMeasuredHeight() + childTop);
        return iMax + measuredWidth + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
    }

    private int layoutChildRight(View view, int i3, int i4, int[] iArr, int i5) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int i6 = ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin - iArr[1];
        int iMax = i4 - Math.max(0, i6);
        iArr[1] = Math.max(0, -i6);
        int childTop = getChildTop(view, i5);
        int measuredWidth = view.getMeasuredWidth();
        view.layout(Math.max(i3, iMax - measuredWidth), childTop, iMax, view.getMeasuredHeight() + childTop);
        return iMax - (measuredWidth + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin);
    }

    private int measureChildCollapseMargins(View view, int i3, int i4, int i5, int i6, int[] iArr) {
        return measureChildCollapseMargins(view, i3, i4, Integer.MAX_VALUE, i5, i6, iArr);
    }

    private void measureChildConstrained(View view, int i3, int i4, int i5, int i6, int i7) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        int childMeasureSpec = ViewGroup.getChildMeasureSpec(i3, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + i4, marginLayoutParams.width);
        int childMeasureSpec2 = ViewGroup.getChildMeasureSpec(i5, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + i6, marginLayoutParams.height);
        int mode = View.MeasureSpec.getMode(childMeasureSpec2);
        if (mode != 1073741824 && i7 >= 0) {
            if (mode != 0) {
                i7 = Math.min(View.MeasureSpec.getSize(childMeasureSpec2), i7);
            }
            childMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(i7, 1073741824);
        }
        view.measure(childMeasureSpec, childMeasureSpec2);
    }

    private void measureChildMaxWidthConstrained(View view, int i3, int i4, int i5, int i6, int i7) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        int childMeasureSpec = ViewGroup.getChildMeasureSpec(i3, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + i4, marginLayoutParams.width);
        int childMeasureSpec2 = ViewGroup.getChildMeasureSpec(i6, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin, marginLayoutParams.height);
        int mode = View.MeasureSpec.getMode(childMeasureSpec2);
        if (mode != 1073741824 && i7 >= 0) {
            if (mode != 0) {
                i7 = Math.min(View.MeasureSpec.getSize(childMeasureSpec2), i7);
            }
            childMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(i7, 1073741824);
        }
        view.measure(childMeasureSpec, childMeasureSpec2);
        if (i5 <= 0 || view.getMeasuredWidth() <= i5) {
            return;
        }
        view.measure(View.MeasureSpec.makeMeasureSpec(i5, 1073741824), childMeasureSpec2);
    }

    private void prepareAttachedSearchBarForStructureChange() {
        for (int i3 = 0; i3 < getChildCount(); i3++) {
            View childAt = getChildAt(i3);
            if (childAt instanceof COUISearchBar) {
                ((COUISearchBar) childAt).prepareForToolbarStructureChange();
                return;
            }
        }
    }

    private void prepareExitOverlayBeforeStructureSwitch() {
        this.mTitleExitOverlayPrepared = false;
        if (TextUtils.isEmpty(this.mTitleExitOverlayOldTitle) || this.mTitleTextView == null || this.mTitleExitOverlayOldRect.isEmpty()) {
            return;
        }
        ensureTitleExitOverlayView();
        TextView textView = this.mTitleExitOverlayView;
        if (textView == null) {
            return;
        }
        textView.setText(this.mTitleExitOverlayOldTitle);
        copyTitleAppearance(this.mTitleExitOverlayView, this.mTitleTextView);
        attachTitleOverlayForCrossFade(this, this.mTitleExitOverlayView, this.mTitleExitOverlayOldRect, 4);
        this.mTitleExitOverlayPrepared = true;
    }

    private void prepareTitleExitOverlayForEditTransitionIfNeeded(boolean z2) {
        if (z2) {
            captureTitleExitOldPosition();
            prepareExitOverlayBeforeStructureSwitch();
        }
    }

    private void refreshWidthLimits(int i3) {
        if (COUIResponsiveUtils.isSmallScreen(getContext(), i3)) {
            this.mSectionGap = this.mSectionGapSmall;
        } else {
            this.mSectionGap = this.mSectionGapMediumLarge;
        }
    }

    private void releaseTransitionControllers() {
        ToolbarTextTransitionController toolbarTextTransitionController = this.mTextTransitionController;
        if (toolbarTextTransitionController != null) {
            toolbarTextTransitionController.release();
            this.mTextTransitionController = null;
        }
        ToolbarGroupTransitionController toolbarGroupTransitionController = this.mGroupTransitionController;
        if (toolbarGroupTransitionController != null) {
            toolbarGroupTransitionController.release();
            this.mGroupTransitionController = null;
        }
    }

    private void removeGroupTransitionPreDraw() {
        if (this.mGroupTransitionPreDraw != null) {
            ViewTreeObserver viewTreeObserver = getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeOnPreDrawListener(this.mGroupTransitionPreDraw);
            }
            this.mGroupTransitionPreDraw = null;
        }
    }

    private void removeTextTransitionPreDraw() {
        if (this.mTextTransitionPreDraw != null) {
            ViewTreeObserver viewTreeObserver = getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeOnPreDrawListener(this.mTextTransitionPreDraw);
            }
            this.mTextTransitionPreDraw = null;
        }
    }

    private void resetTitleExitOverlayState() {
        this.mTitleExitOverlayOldTitle = null;
        this.mTitleExitOverlayPrepared = false;
        this.mTitleExitOverlayOldRect.setEmpty();
        if (this.mTitleExitOverlayView != null) {
            getOverlay().remove(this.mTitleExitOverlayView);
            this.mTitleExitOverlayView.setVisibility(8);
            this.mTitleExitOverlayView.setAlpha(1.0f);
        }
    }

    private List<View> resolveVisibleMenuItemViews() {
        ArrayList arrayList = new ArrayList();
        COUIActionMenuView cOUIActionMenuView = this.mMenuView;
        if (cOUIActionMenuView != null && cOUIActionMenuView.getVisibility() == 0) {
            int childCount = this.mMenuView.getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = this.mMenuView.getChildAt(i3);
                if (childAt != null && childAt.getVisibility() == 0) {
                    arrayList.add(childAt);
                }
            }
        }
        return arrayList;
    }

    private void scheduleEditModeButtonGroupSequence(Runnable runnable, EditModeTitleExitStarter editModeTitleExitStarter, Runnable runnable2) {
        m mVar = new m(this, runnable, runnable2, editModeTitleExitStarter, ensureGroupTransitionController());
        if (isAttachedToWindow()) {
            mVar.run();
        } else {
            post(mVar);
        }
    }

    private void scheduleGroupTransitionPreDraw(Runnable runnable) {
        removeGroupTransitionPreDraw();
        this.mGroupTransitionPreDraw = new AnonymousClass5(runnable);
        getViewTreeObserver().addOnPreDrawListener(this.mGroupTransitionPreDraw);
    }

    private void schedulePhaseInIncomingMenuTransition(Runnable runnable) {
        if (runnable != null) {
            runnable.run();
        }
        scheduleGroupTransitionPreDraw(new e(this));
    }

    private void schedulePhaseInIncomingMenuTransitionAfterStructure(Runnable runnable) {
        if (isAttachedToWindow()) {
            schedulePhaseInIncomingMenuTransition(runnable);
        } else {
            post(new f(this, runnable));
        }
    }

    private void setChildVisibilityForExpandedActionView(boolean z2) {
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            if (((LayoutParams) childAt.getLayoutParams()).mViewType != 2 && childAt != this.mMenuView) {
                childAt.setVisibility(z2 ? 8 : 0);
            }
        }
    }

    private boolean shouldCollapse() {
        if (!this.mCollapsible) {
            return false;
        }
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            if (shouldLayout(childAt) && childAt.getMeasuredWidth() > 0 && childAt.getMeasuredHeight() > 0) {
                return false;
            }
        }
        return true;
    }

    private boolean shouldLayout(View view) {
        return (view == null || view.getParent() != this || view.getVisibility() == 8) ? false : true;
    }

    private void softCancelTitleContentTransition() {
        removeTextTransitionPreDraw();
        resetTitleExitOverlayState();
        ToolbarTextTransitionController toolbarTextTransitionController = this.mTextTransitionController;
        if (toolbarTextTransitionController != null) {
            toolbarTextTransitionController.cancelAll();
        }
    }

    private void updateChildVisibilityForExpandedActionView(View view) {
        if (((LayoutParams) view.getLayoutParams()).mViewType == 2 || view == this.mMenuView) {
            return;
        }
        view.setVisibility(this.mExpandedActionView != null ? 8 : 0);
    }

    private boolean useTextMenuItemPaddingEnd() {
        COUIActionMenuView cOUIActionMenuView = this.mMenuView;
        return this.mIsTitleCenterStyle || ((cOUIActionMenuView == null || cOUIActionMenuView.getChildCount() != 1 || !(this.mMenuView.getChildAt(0) instanceof COUIActionMenuItemView)) ? false : ((COUIActionMenuItemView) this.mMenuView.getChildAt(0)).isTextMenuItem());
    }

    public void cancelTransition() {
        softCancelTitleContentTransition();
        removeGroupTransitionPreDraw();
        ToolbarGroupTransitionController toolbarGroupTransitionController = this.mGroupTransitionController;
        if (toolbarGroupTransitionController != null) {
            toolbarGroupTransitionController.cancelAll();
        }
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup
    public boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.checkLayoutParams(layoutParams) && (layoutParams instanceof LayoutParams);
    }

    public void clearMenu() {
        this.mResId = 0;
        getMenu().clear();
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void collapseActionView() {
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        MenuItemImpl menuItemImpl = expandedActionViewMenuPresenter == null ? null : expandedActionViewMenuPresenter.mCurrentExpandedItem;
        if (menuItemImpl != null) {
            menuItemImpl.collapseActionView();
        }
    }

    public void copyTitleAppearance(TextView textView, TextView textView2) {
        if (textView2 == null) {
            return;
        }
        textView.setTextSize(0, textView2.getTextSize());
        textView.setTypeface(textView2.getTypeface());
        textView.setTextColor(textView2.getCurrentTextColor());
        textView.setGravity(textView2.getGravity());
        textView.setIncludeFontPadding(textView2.getIncludeFontPadding());
        textView.setPadding(textView2.getPaddingLeft(), textView2.getPaddingTop(), textView2.getPaddingRight(), textView2.getPaddingBottom());
        textView.setTextAlignment(textView2.getTextAlignment());
        textView.setTextDirection(textView2.getTextDirection());
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void dismissPopupMenus() {
        COUIActionMenuView cOUIActionMenuView = this.mMenuView;
        if (cOUIActionMenuView != null) {
            cOUIActionMenuView.dismissPopupMenus();
        }
    }

    public void editModeGroupTransition(Runnable runnable, EditModeTitleExitStarter editModeTitleExitStarter, Runnable runnable2) {
        prepareAttachedSearchBarForStructureChange();
        scheduleEditModeButtonGroupSequence(runnable, editModeTitleExitStarter, runnable2);
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return generateDefaultLayoutParams();
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return generateLayoutParams(attributeSet);
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public int getBarrierDirection() {
        if (getDisplayFrame().isEmpty()) {
            return -1;
        }
        if (this.mWindowFrame == null) {
            this.mWindowFrame = new Rect();
        }
        int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_min_window_height_to_apply_vertical_barrier);
        getRootView().getGlobalVisibleRect(this.mWindowFrame);
        return (this.mWindowFrame.height() > dimensionPixelSize && this.mWindowFrame.bottom - this.mDisplayFrame.bottom > dimensionPixelSize) ? 1 : -1;
    }

    public TextView getCOUITitleTextView() {
        ensureTitleTextView();
        return this.mTitleTextView;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public int getContentInsetEnd() {
        return this.mContentInsets.getEnd();
    }

    @Override // androidx.appcompat.widget.Toolbar
    public int getContentInsetLeft() {
        return this.mContentInsets.getLeft();
    }

    @Override // androidx.appcompat.widget.Toolbar
    public int getContentInsetRight() {
        return this.mContentInsets.getRight();
    }

    @Override // androidx.appcompat.widget.Toolbar
    public int getContentInsetStart() {
        return this.mContentInsets.getStart();
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public Rect getDisplayFrame() {
        if (this.mDisplayFrame == null) {
            this.mDisplayFrame = new Rect();
        }
        getGlobalVisibleRect(this.mDisplayFrame);
        return this.mDisplayFrame;
    }

    public boolean getIsTitleCenterStyle() {
        return this.mIsTitleCenterStyle;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public Drawable getLogo() {
        ImageView imageView = this.mLogoView;
        if (imageView != null) {
            return imageView.getDrawable();
        }
        return null;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public CharSequence getLogoDescription() {
        ImageView imageView = this.mLogoView;
        if (imageView != null) {
            return imageView.getContentDescription();
        }
        return null;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public Menu getMenu() {
        ensureMenu();
        return this.mMenuView.getMenu();
    }

    public COUIActionMenuView getMenuView() {
        ensureMenuView();
        return this.mMenuView;
    }

    public ToolbarMaterialEffectDelegate getNavButtonEffectDelegate() {
        return this.mNavButtonEffectDelegate;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public CharSequence getNavigationContentDescription() {
        ImageButton imageButton = this.mNavButtonView;
        if (imageButton != null) {
            return imageButton.getContentDescription();
        }
        return null;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public Drawable getNavigationIcon() {
        ImageButton imageButton = this.mNavButtonView;
        if (imageButton != null) {
            return imageButton.getDrawable();
        }
        return null;
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public Rect getOutsets() {
        return TOOLBAR_OUTSETS;
    }

    public View getOverFlowMenuButton() {
        COUIActionMenuView cOUIActionMenuView = this.mMenuView;
        if (cOUIActionMenuView != null) {
            return cOUIActionMenuView.getOverFlowMenuButton();
        }
        return null;
    }

    public ToolbarMaterialEffectDelegate getOverflowButtonEffectDelegate() {
        COUIActionMenuView cOUIActionMenuView = this.mMenuView;
        if (cOUIActionMenuView != null) {
            return cOUIActionMenuView.getOverflowButtonEffectDelegate();
        }
        return null;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public Drawable getOverflowIcon() {
        ensureMenu();
        return this.mMenuView.getOverflowIcon();
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public boolean getPopupMenuRuleEnabled() {
        return this.mPopupRuleEnable;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public int getPopupTheme() {
        return this.mPopupTheme;
    }

    public int getSectionGapMediumLarge() {
        return this.mSectionGapMediumLarge;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public CharSequence getSubtitle() {
        return this.mSubtitleText;
    }

    public ToolbarMaterialEffectDelegate getTextButtonEffectDelegate() {
        return this.mTextButtonEffectDelegate;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public CharSequence getTitle() {
        return this.mTitleText;
    }

    public View getTitleView() {
        return this.mTitleTextView;
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public int getType() {
        return 2;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void inflateMenu(int i3) {
        super.inflateMenu(i3);
        this.mResId = i3;
        if (this.mMenuView != null) {
            this.mMenuView.clearRedDotInfo();
        }
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this.mShowOverflowMenuRunnable);
        releaseTransitionControllers();
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        if (actionMasked == 9) {
            this.mEatingHover = false;
        }
        if (!this.mEatingHover) {
            boolean zOnHoverEvent = super.onHoverEvent(motionEvent);
            if (actionMasked == 9 && !zOnHoverEvent) {
                this.mEatingHover = true;
            }
        }
        if (actionMasked == 10 || actionMasked == 3) {
            this.mEatingHover = false;
        }
        return true;
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        int iLayoutChildRight;
        int[] iArr;
        int i7;
        int iLayoutChildRight2;
        int[] iArr2;
        int i8;
        int iLayoutChildLeft;
        int[] iArr3;
        int i9;
        int iMax;
        int measuredHeight;
        int paddingTop;
        int i10;
        int i11;
        int i12;
        int iMax2;
        int i13;
        int i14;
        int i15;
        int[] iArr4;
        int i16;
        int i17;
        int[] iArr5;
        int i18;
        int i19;
        int i20 = 1;
        boolean z3 = ViewCompat.getLayoutDirection(this) == 1;
        int width = getWidth();
        int height = getHeight();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop2 = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int iLayoutChildLeft2 = width - paddingRight;
        int[] iArr6 = this.mTempMargins;
        iArr6[0] = 0;
        iArr6[1] = 0;
        int minimumHeightCompat = getMinimumHeightCompat();
        if (shouldLayout(this.mNavButtonView)) {
            if (z3) {
                iLayoutChildLeft2 = layoutChildRight(this.mNavButtonView, paddingLeft, iLayoutChildLeft2, iArr6, minimumHeightCompat);
            } else {
                paddingLeft = layoutChildLeft(this.mNavButtonView, paddingLeft, iLayoutChildLeft2, iArr6, minimumHeightCompat);
            }
        }
        if (shouldLayout(this.mCollapseButtonView)) {
            if (z3) {
                iLayoutChildLeft2 = layoutChildRight(this.mCollapseButtonView, paddingLeft, iLayoutChildLeft2, iArr6, minimumHeightCompat);
            } else {
                paddingLeft = layoutChildLeft(this.mCollapseButtonView, paddingLeft, iLayoutChildLeft2, iArr6, minimumHeightCompat);
            }
        }
        if (shouldLayout(this.mTextButton)) {
            if (z3) {
                paddingLeft = layoutChildLeft(this.mTextButton, paddingLeft, iLayoutChildLeft2, iArr6, minimumHeightCompat);
                if (shouldLayout(this.mMenuView)) {
                    paddingLeft += this.mGapBetweenTextButtonAndMenuView;
                }
            } else {
                iLayoutChildLeft2 = layoutChildRight(this.mTextButton, paddingLeft, iLayoutChildLeft2, iArr6, minimumHeightCompat);
                if (shouldLayout(this.mMenuView)) {
                    iLayoutChildLeft2 -= this.mGapBetweenTextButtonAndMenuView;
                }
            }
        }
        boolean zIsSmallScreen = COUIResponsiveUtils.isSmallScreen(getContext(), getMeasuredWidth());
        if (shouldLayout(this.mMenuView)) {
            if (z3) {
                paddingLeft = layoutChildLeft(this.mMenuView, paddingLeft, iLayoutChildLeft2, iArr6, minimumHeightCompat);
                if (!zIsSmallScreen) {
                    paddingLeft += this.mGapBeforeMenuView;
                }
            } else {
                iLayoutChildLeft2 = layoutChildRight(this.mMenuView, paddingLeft, iLayoutChildLeft2, iArr6, minimumHeightCompat);
                if (!zIsSmallScreen) {
                    iLayoutChildLeft2 -= this.mGapBeforeMenuView;
                }
            }
        }
        int iLayoutChildLeft3 = paddingLeft;
        if (shouldLayout(this.mSegmentButton) && !this.mIsTitleCenterStyle) {
            int measuredWidth = this.mSegmentButton.getMeasuredWidth();
            int i21 = (width / 2) - (measuredWidth / 2);
            int i22 = measuredWidth + i21;
            if (i21 < iLayoutChildLeft3) {
                i21 = iLayoutChildLeft3;
            } else if (i22 > iLayoutChildLeft2) {
                i21 -= i22 - iLayoutChildLeft2;
            }
            LayoutParams layoutParams = (LayoutParams) this.mSegmentButton.getLayoutParams();
            if (z3) {
                iLayoutChildLeft3 = layoutChildLeft(this.mSegmentButton, i21, iLayoutChildLeft2, iArr6, minimumHeightCompat);
            } else {
                iLayoutChildLeft2 = layoutChildLeft(this.mSegmentButton, i21, iLayoutChildLeft2, iArr6, minimumHeightCompat) - (this.mSegmentButton.getMeasuredWidth() + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin);
            }
        }
        if (shouldLayout(this.mTitleTextView)) {
            addCustomViewsWithGravity(this.mTempViews, 1);
            int viewListMeasuredWidth = getViewListMeasuredWidth(this.mTempViews, iArr6);
            int iLayoutChildLeft4 = (width / 2) - (viewListMeasuredWidth / 2);
            int i23 = viewListMeasuredWidth + iLayoutChildLeft4;
            if (iLayoutChildLeft4 < iLayoutChildLeft3) {
                iLayoutChildLeft4 = iLayoutChildLeft3;
            } else if (i23 > iLayoutChildLeft2) {
                iLayoutChildLeft4 -= i23 - iLayoutChildLeft2;
            }
            int size = this.mTempViews.size();
            int i24 = 0;
            while (i24 < size) {
                int i25 = i20;
                ViewGroup.LayoutParams layoutParams2 = this.mTempViews.get(i24).getLayoutParams();
                if (layoutParams2 == null || !(layoutParams2 instanceof LayoutParams) || !((LayoutParams) layoutParams2).mTypeSegmentButton) {
                    iLayoutChildLeft4 = layoutChildLeft(this.mTempViews.get(i24), iLayoutChildLeft4, iLayoutChildLeft2, iArr6, minimumHeightCompat);
                }
                i24++;
                i20 = i25;
            }
        }
        int i26 = i20;
        iArr6[0] = Math.max(0, getContentInsetLeft() - iLayoutChildLeft3);
        iArr6[i26] = Math.max(0, getContentInsetRight() - (iLayoutChildLeft2 - iLayoutChildLeft2));
        int iMax3 = Math.max(iLayoutChildLeft3, getContentInsetLeft());
        int iMin = Math.min(iLayoutChildLeft2, width - getContentInsetRight());
        if (shouldLayout(this.mExpandedActionView)) {
            if (z3) {
                iMin = layoutChildRight(this.mExpandedActionView, iMax3, iMin, iArr6, minimumHeightCompat);
            } else {
                iMax3 = layoutChildLeft(this.mExpandedActionView, iMax3, iMin, iArr6, minimumHeightCompat);
            }
        }
        if (!shouldLayout(this.mLogoView)) {
            iLayoutChildRight = iMin;
        } else if (z3) {
            iLayoutChildRight = layoutChildRight(this.mLogoView, iMax3, iMin, iArr6, minimumHeightCompat);
        } else {
            iMax3 = layoutChildLeft(this.mLogoView, iMax3, iMin, iArr6, minimumHeightCompat);
            iLayoutChildRight = iMin;
        }
        addCustomViewsWithGravity(this.mTempViews, 3);
        int size2 = this.mTempViews.size();
        if (this.mHasSearchViewFlag) {
            iLayoutChildLeft = iMax3;
            int i27 = 0;
            while (i27 < size2) {
                ViewGroup.LayoutParams layoutParams3 = this.mTempViews.get(i27).getLayoutParams();
                if (isDummyView(this.mTempViews.get(i27), (LayoutParams) layoutParams3)) {
                    iArr5 = iArr6;
                    i18 = minimumHeightCompat;
                    i19 = iLayoutChildRight;
                } else if (layoutParams3 != null && (layoutParams3 instanceof LayoutParams) && ((LayoutParams) layoutParams3).mTypeSearch) {
                    layoutChildLeft(this.mTempViews.get(i27), 0, width, this.mSearchCollapsingMargins, 0);
                    iArr5 = iArr6;
                    i18 = minimumHeightCompat;
                    i19 = iLayoutChildRight;
                } else {
                    iArr5 = iArr6;
                    i18 = minimumHeightCompat;
                    i19 = iLayoutChildRight;
                    iLayoutChildLeft = layoutChildLeft(this.mTempViews.get(i27), iLayoutChildLeft, i19, iArr5, i18);
                }
                i27++;
                iLayoutChildRight = i19;
                iArr6 = iArr5;
                minimumHeightCompat = i18;
            }
            iLayoutChildRight2 = iLayoutChildRight;
            i8 = minimumHeightCompat;
            iArr2 = iArr6;
        } else {
            iLayoutChildRight2 = iLayoutChildRight;
            int i28 = 0;
            while (i28 < size2) {
                View view = this.mTempViews.get(i28);
                LayoutParams layoutParams4 = (LayoutParams) view.getLayoutParams();
                if (layoutParams4.mTypeTextButton || isDummyView(view, layoutParams4)) {
                    iArr = iArr6;
                    i7 = minimumHeightCompat;
                    iArr = iArr6;
                    i7 = minimumHeightCompat;
                } else {
                    iArr = iArr6;
                    i7 = minimumHeightCompat;
                    iMax3 = layoutChildLeft(view, iMax3, iLayoutChildRight2, iArr, i7);
                }
                i28++;
                iArr = iArr;
                i7 = i7;
            }
            iArr = iArr6;
            i7 = minimumHeightCompat;
            iArr2 = iArr;
            i8 = i7;
            iLayoutChildLeft = iMax3;
        }
        addCustomViewsWithGravity(this.mTempViews, 5);
        int size3 = this.mTempViews.size();
        if (this.mHasSearchViewFlag) {
            int iLayoutChildRight3 = iLayoutChildRight2;
            int i29 = 0;
            while (i29 < size3) {
                ViewGroup.LayoutParams layoutParams5 = this.mTempViews.get(i29).getLayoutParams();
                if (isDummyView(this.mTempViews.get(i29), (LayoutParams) layoutParams5)) {
                    iArr4 = iArr2;
                    i16 = i8;
                    i17 = iLayoutChildLeft;
                } else if (layoutParams5 != null && (layoutParams5 instanceof LayoutParams) && ((LayoutParams) layoutParams5).mTypeSearch) {
                    layoutChildRight(this.mTempViews.get(i29), 0, width, this.mSearchCollapsingMargins, 0);
                    iArr4 = iArr2;
                    i16 = i8;
                    i17 = iLayoutChildLeft;
                } else {
                    iArr4 = iArr2;
                    i16 = i8;
                    i17 = iLayoutChildLeft;
                    iLayoutChildRight3 = layoutChildRight(this.mTempViews.get(i29), i17, iLayoutChildRight3, iArr4, i16);
                }
                i29++;
                iLayoutChildLeft = i17;
                iArr2 = iArr4;
                i8 = i16;
            }
            iLayoutChildRight2 = iLayoutChildRight3;
            iArr3 = iArr2;
            i9 = i8;
            iMax = iLayoutChildLeft;
        } else {
            iArr3 = iArr2;
            i9 = i8;
            int i30 = iLayoutChildLeft;
            if (z3) {
                for (int i31 = size3 - 1; i31 >= 0; i31--) {
                    View view2 = this.mTempViews.get(i31);
                    LayoutParams layoutParams6 = (LayoutParams) view2.getLayoutParams();
                    if (!layoutParams6.mTypeTextButton && !isDummyView(view2, layoutParams6)) {
                        iLayoutChildRight2 = layoutChildRight(view2, i30, iLayoutChildRight2, iArr3, i9);
                    }
                }
            } else {
                int i32 = 0;
                while (i32 < size3) {
                    View view3 = this.mTempViews.get(i32);
                    LayoutParams layoutParams7 = (LayoutParams) view3.getLayoutParams();
                    if (!layoutParams7.mTypeTextButton && !isDummyView(view3, layoutParams7)) {
                        iLayoutChildRight2 = layoutChildRight(view3, i30, iLayoutChildRight2, iArr3, i9);
                    }
                    i32++;
                    i30 = i30;
                }
            }
            iMax = i30;
        }
        if (!shouldLayout(this.mTitleTextView)) {
            addCustomViewsWithGravity(this.mTempViews, i26);
            int viewListMeasuredWidth2 = getViewListMeasuredWidth(this.mTempViews, iArr3);
            int i33 = (width / 2) - (viewListMeasuredWidth2 / 2);
            int i34 = viewListMeasuredWidth2 + i33;
            int i35 = this.mSectionGap;
            if (i33 < iMax + i35) {
                i33 = iMax + i35;
            } else if (i34 > iLayoutChildRight2 - i35) {
                i33 -= i34 - (iLayoutChildRight2 - i35);
            }
            int size4 = this.mTempViews.size();
            int iLayoutChildLeft5 = i33;
            for (int i36 = 0; i36 < size4; i36++) {
                ViewGroup.LayoutParams layoutParams8 = this.mTempViews.get(i36).getLayoutParams();
                if (layoutParams8 == null || !(layoutParams8 instanceof LayoutParams) || !((LayoutParams) layoutParams8).mTypeSegmentButton) {
                    iLayoutChildLeft5 = layoutChildLeft(this.mTempViews.get(i36), iLayoutChildLeft5, iLayoutChildRight2, iArr3, i9);
                }
            }
        }
        this.mTempViews.clear();
        boolean zShouldLayout = shouldLayout(this.mTitleTextView);
        boolean zShouldLayout2 = shouldLayout(this.mSubtitleTextView);
        if (zShouldLayout) {
            LayoutParams layoutParams9 = (LayoutParams) this.mTitleTextView.getLayoutParams();
            measuredHeight = ((ViewGroup.MarginLayoutParams) layoutParams9).bottomMargin + ((ViewGroup.MarginLayoutParams) layoutParams9).topMargin + this.mTitleTextView.getMeasuredHeight();
        } else {
            measuredHeight = 0;
        }
        if (zShouldLayout2) {
            LayoutParams layoutParams10 = (LayoutParams) this.mSubtitleTextView.getLayoutParams();
            measuredHeight += ((ViewGroup.MarginLayoutParams) layoutParams10).topMargin + this.mSubtitleTextView.getMeasuredHeight() + ((ViewGroup.MarginLayoutParams) layoutParams10).bottomMargin;
        }
        if (zShouldLayout || zShouldLayout2) {
            TextView textView = zShouldLayout ? this.mTitleTextView : this.mSubtitleTextView;
            TextView textView2 = zShouldLayout2 ? this.mSubtitleTextView : this.mTitleTextView;
            LayoutParams layoutParams11 = (LayoutParams) textView.getLayoutParams();
            LayoutParams layoutParams12 = (LayoutParams) textView2.getLayoutParams();
            boolean z4 = (zShouldLayout && this.mTitleTextView.getMeasuredWidth() > 0) || (zShouldLayout2 && this.mSubtitleTextView.getMeasuredWidth() > 0);
            int i37 = this.mGravity & 112;
            if (i37 == 48) {
                paddingTop = getPaddingTop() + ((ViewGroup.MarginLayoutParams) layoutParams11).topMargin + this.mTitleMarginTop;
            } else if (i37 != 80) {
                int iMax4 = (((height - paddingTop2) - paddingBottom) - measuredHeight) / 2;
                int i38 = ((ViewGroup.MarginLayoutParams) layoutParams11).topMargin;
                int i39 = this.mTitleMarginTop;
                if (iMax4 < i38 + i39) {
                    iMax4 = i38 + i39;
                } else {
                    int i40 = (((height - paddingBottom) - measuredHeight) - iMax4) - paddingTop2;
                    int i41 = ((ViewGroup.MarginLayoutParams) layoutParams11).bottomMargin;
                    int i42 = this.mTitleMarginBottom;
                    if (i40 < i41 + i42) {
                        iMax4 = Math.max(0, iMax4 - ((((ViewGroup.MarginLayoutParams) layoutParams12).bottomMargin + i42) - i40));
                    }
                }
                paddingTop = paddingTop2 + iMax4;
            } else {
                paddingTop = (((height - paddingBottom) - ((ViewGroup.MarginLayoutParams) layoutParams12).bottomMargin) - this.mTitleMarginBottom) - measuredHeight;
            }
            if (this.mIsTitleCenterStyle) {
                int iMax5 = Math.max(zShouldLayout ? this.mTitleTextView.getMeasuredWidth() : 0, zShouldLayout2 != 0 ? this.mSubtitleTextView.getMeasuredWidth() : 0);
                int width2 = getWidth() - (Math.max(this.mTitlePosition[0], getWidth() - this.mTitlePosition[1]) * 2);
                int[] iArr7 = this.mTitlePosition;
                int i43 = iArr7[1] - iArr7[0];
                if (zShouldLayout) {
                    LayoutParams layoutParams13 = (LayoutParams) this.mTitleTextView.getLayoutParams();
                    int measuredWidth2 = this.mTitleTextView.getMeasuredWidth();
                    int width3 = (getWidth() - measuredWidth2) / 2;
                    int i44 = width3 + measuredWidth2;
                    int measuredHeight2 = this.mTitleTextView.getMeasuredHeight() + paddingTop;
                    if (width2 < iMax5) {
                        if (measuredWidth2 >= i43) {
                            int[] iArr8 = this.mTitlePosition;
                            width3 = iArr8[0];
                            i44 = iArr8[1];
                        } else {
                            width3 = this.mTitlePosition[0] + ((i43 - measuredWidth2) / 2);
                            i44 = width3 + measuredWidth2;
                        }
                    }
                    this.mTitleTextView.layout(width3, paddingTop, i44, measuredHeight2);
                    paddingTop = measuredHeight2 + ((ViewGroup.MarginLayoutParams) layoutParams13).bottomMargin;
                }
                if (zShouldLayout2) {
                    int i45 = paddingTop + ((ViewGroup.MarginLayoutParams) ((LayoutParams) this.mSubtitleTextView.getLayoutParams())).topMargin;
                    int measuredWidth3 = this.mSubtitleTextView.getMeasuredWidth();
                    int width4 = (getWidth() - measuredWidth3) / 2;
                    int i46 = width4 + measuredWidth3;
                    int measuredHeight3 = this.mSubtitleTextView.getMeasuredHeight() + i45;
                    if (width2 < iMax5) {
                        if (measuredWidth3 >= i43) {
                            int[] iArr9 = this.mTitlePosition;
                            width4 = iArr9[0];
                            i46 = iArr9[1];
                        } else {
                            width4 = this.mTitlePosition[0] + ((i43 - measuredWidth3) / 2);
                            i46 = width4 + measuredWidth3;
                        }
                    }
                    this.mSubtitleTextView.layout(width4, i45, i46, measuredHeight3);
                }
            } else if (z3) {
                int i47 = ((z4 ? this.mTitleMarginStart : 0) - iArr3[1]) + ((this.mHasCustomViewBeforeTitle || shouldLayout(this.mNavButtonView)) ? this.mGapBetweenNavigationAndTitle : 0);
                if (this.mIsTiny) {
                    i13 = 0;
                } else {
                    i13 = 0;
                    iLayoutChildRight2 -= Math.max(0, i47);
                }
                iArr3[1] = Math.max(i13, -i47);
                if (zShouldLayout) {
                    LayoutParams layoutParams14 = (LayoutParams) this.mTitleTextView.getLayoutParams();
                    clampTitleWidthIfNeeded(this.mTitleTextView, Math.max(i13, iLayoutChildRight2 - iMax));
                    int iMax6 = Math.max(iMax, iLayoutChildRight2 - this.mTitleTextView.getMeasuredWidth());
                    int measuredHeight4 = this.mTitleTextView.getMeasuredHeight() + paddingTop;
                    this.mTitleTextView.layout(iMax6, paddingTop, iLayoutChildRight2, measuredHeight4);
                    i14 = iMax6 - this.mTitleMarginEnd;
                    paddingTop = measuredHeight4 + ((ViewGroup.MarginLayoutParams) layoutParams14).bottomMargin;
                } else {
                    i14 = iLayoutChildRight2;
                }
                if (zShouldLayout2 != 0) {
                    int i48 = paddingTop + ((ViewGroup.MarginLayoutParams) ((LayoutParams) this.mSubtitleTextView.getLayoutParams())).topMargin;
                    clampTitleWidthIfNeeded(this.mSubtitleTextView, Math.max(0, iLayoutChildRight2 - iMax));
                    this.mSubtitleTextView.layout(iLayoutChildRight2 - this.mSubtitleTextView.getMeasuredWidth(), i48, iLayoutChildRight2, this.mSubtitleTextView.getMeasuredHeight() + i48);
                    i15 = iLayoutChildRight2 - this.mTitleMarginEnd;
                } else {
                    i15 = iLayoutChildRight2;
                }
                if (z4) {
                    iLayoutChildRight2 = Math.min(i14, i15);
                }
            } else {
                int i49 = ((z4 ? this.mTitleMarginStart : 0) - iArr3[0]) + ((this.mHasCustomViewBeforeTitle || shouldLayout(this.mNavButtonView)) ? this.mGapBetweenNavigationAndTitle : 0);
                if (this.mIsTiny) {
                    i10 = 0;
                } else {
                    i10 = 0;
                    iMax += Math.max(0, i49);
                }
                iArr3[i10] = Math.max(i10, -i49);
                if (zShouldLayout) {
                    LayoutParams layoutParams15 = (LayoutParams) this.mTitleTextView.getLayoutParams();
                    clampTitleWidthIfNeeded(this.mTitleTextView, Math.max(i10, iLayoutChildRight2 - iMax));
                    int iMin2 = Math.min(this.mTitleTextView.getMeasuredWidth() + iMax, iLayoutChildRight2);
                    int measuredHeight5 = this.mTitleTextView.getMeasuredHeight() + paddingTop;
                    this.mTitleTextView.layout(iMax, paddingTop, iMin2, measuredHeight5);
                    i11 = iMin2 + this.mTitleMarginEnd;
                    paddingTop = measuredHeight5 + ((ViewGroup.MarginLayoutParams) layoutParams15).bottomMargin;
                } else {
                    i11 = iMax;
                }
                if (zShouldLayout2 != 0) {
                    int i50 = paddingTop + ((ViewGroup.MarginLayoutParams) ((LayoutParams) this.mSubtitleTextView.getLayoutParams())).topMargin;
                    clampTitleWidthIfNeeded(this.mSubtitleTextView, Math.max(0, iLayoutChildRight2 - iMax));
                    int measuredWidth4 = this.mSubtitleTextView.getMeasuredWidth() + iMax;
                    this.mSubtitleTextView.layout(iMax, i50, measuredWidth4, this.mSubtitleTextView.getMeasuredHeight() + i50);
                    i12 = measuredWidth4 + this.mTitleMarginEnd;
                } else {
                    i12 = iMax;
                }
                if (z4) {
                    iMax2 = Math.max(i11, i12);
                }
            }
            iMax2 = iMax;
        } else {
            iMax2 = iMax;
        }
        if (shouldLayout(this.mDummyView)) {
            if (z3) {
                layoutChildRight(this.mDummyView, iMax2, iLayoutChildRight2, iArr3, i9);
            } else {
                layoutChildLeft(this.mDummyView, iMax2, iLayoutChildRight2, iArr3, i9);
            }
        }
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.View
    public void onMeasure(int i3, int i4) {
        int i5;
        int measuredWidth;
        int iMax;
        int iCombineMeasuredStates;
        int iCombineMeasuredStates2;
        int iMax2;
        int iMax3;
        int iCombineMeasuredStates3;
        int measuredWidth2;
        boolean z2;
        MenuBuilder menuBuilder;
        int[] iArr;
        int i6;
        int iMax4;
        int measuredWidth3;
        int i7;
        int iMax5;
        int i8;
        int iCombineMeasuredStates4;
        int iMax6;
        int measuredHeight;
        int i9;
        int i10;
        int i11;
        int[] iArr2;
        int iMeasureText;
        int i12;
        boolean z3;
        int measuredWidth4;
        int iMax7;
        int iCombineMeasuredStates5;
        int[] iArr3;
        int iMax8;
        int iCombineMeasuredStates6;
        int i13;
        int i14;
        int i15;
        int i16;
        int size = View.MeasureSpec.getSize(i3);
        refreshWidthLimits(size);
        boolean z4 = ViewCompat.getLayoutDirection(this) == 1;
        if (this.mIsTitleCenterStyle) {
            int[] iArr4 = this.mTempMargins;
            boolean zIsLayoutRtl = ViewUtils.isLayoutRtl(this);
            int i17 = !zIsLayoutRtl ? 1 : 0;
            int contentInsetStart = getContentInsetStart();
            int iMax9 = Math.max(contentInsetStart, 0);
            iArr4[zIsLayoutRtl ? 1 : 0] = Math.max(0, contentInsetStart);
            if (shouldLayout(this.mMenuView)) {
                changeToolbarPadding((MenuBuilder) this.mMenuView.getMenu(), null, z4, i3, false);
                measureChildConstrained(this.mMenuView, i3, 0, i4, 0, this.mMaxButtonHeight);
                measuredWidth4 = this.mMenuView.getMeasuredWidth() + getHorizontalMargins(this.mMenuView);
                iMax7 = Math.max(0, this.mMenuView.getMeasuredHeight() + getVerticalMargins(this.mMenuView));
                iCombineMeasuredStates5 = View.combineMeasuredStates(0, ViewCompat.getMeasuredState(this.mMenuView));
            } else {
                measuredWidth4 = 0;
                iMax7 = 0;
                iCombineMeasuredStates5 = 0;
            }
            int contentInsetEnd = getContentInsetEnd();
            int iMax10 = Math.max(contentInsetEnd, measuredWidth4) + iMax9;
            iArr4[i17] = Math.max(0, contentInsetEnd - measuredWidth4);
            if (shouldLayout(this.mExpandedActionView)) {
                iArr3 = iArr4;
                iMax10 += measureChildCollapseMargins(this.mExpandedActionView, i3, iMax10, i4, 0, iArr3);
                iMax7 = Math.max(iMax7, this.mExpandedActionView.getMeasuredHeight() + getVerticalMargins(this.mExpandedActionView));
                iCombineMeasuredStates5 = View.combineMeasuredStates(iCombineMeasuredStates5, ViewCompat.getMeasuredState(this.mExpandedActionView));
            } else {
                iArr3 = iArr4;
            }
            int childCount = getChildCount();
            for (int i18 = 0; i18 < childCount; i18++) {
                View childAt = getChildAt(i18);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.mViewType == 0 && shouldLayout(childAt) && !layoutParams.mTypeSegmentButton) {
                    iMax10 += measureChildCollapseMargins(childAt, i3, iMax10, i4, 0, iArr3);
                    int iMax11 = Math.max(iMax7, childAt.getMeasuredHeight() + getVerticalMargins(childAt));
                    iCombineMeasuredStates5 = View.combineMeasuredStates(iCombineMeasuredStates5, ViewCompat.getMeasuredState(childAt));
                    iMax7 = iMax11;
                } else {
                    iMax10 = iMax10;
                }
            }
            int i19 = iMax10;
            int i20 = this.mTitleMarginTop + this.mTitleMarginBottom;
            if (shouldLayout(this.mTitleTextView)) {
                this.mTitleTextView.getLayoutParams().width = -2;
                this.mTitleTextView.setTextSize(0, this.mTitleTextSize);
                measureChildCollapseMargins(this.mTitleTextView, i3, 0, i4, i20, iArr3);
                int measuredWidth5 = this.mTitleTextView.getMeasuredWidth() + getHorizontalMargins(this.mTitleTextView);
                int measuredHeight2 = this.mTitleTextView.getMeasuredHeight() + getVerticalMargins(this.mTitleTextView);
                iMax8 = measuredWidth5;
                iCombineMeasuredStates6 = View.combineMeasuredStates(iCombineMeasuredStates5, ViewCompat.getMeasuredState(this.mTitleTextView));
                i13 = measuredHeight2;
            } else {
                iMax8 = 0;
                iCombineMeasuredStates6 = iCombineMeasuredStates5;
                i13 = 0;
            }
            if (shouldLayout(this.mSubtitleTextView)) {
                this.mSubtitleTextView.getLayoutParams().width = -2;
                i15 = i4;
                i14 = i3;
                iMax8 = Math.max(iMax8, measureChildCollapseMargins(this.mSubtitleTextView, i14, 0, i15, i13 + i20, iArr3));
                iCombineMeasuredStates6 = View.combineMeasuredStates(iCombineMeasuredStates6, ViewCompat.getMeasuredState(this.mSubtitleTextView));
            } else {
                i14 = i3;
                i15 = i4;
            }
            int iMax12 = Math.max(iMax7, i13);
            int paddingLeft = i19 + iMax8 + getPaddingLeft() + getPaddingRight();
            int paddingTop = iMax12 + getPaddingTop() + getPaddingBottom();
            int iResolveSizeAndState = ViewCompat.resolveSizeAndState(Math.max(paddingLeft, getSuggestedMinimumWidth()), i14, iCombineMeasuredStates6 & (-16777216));
            int iResolveSizeAndState2 = ViewCompat.resolveSizeAndState(Math.max(paddingTop, getSuggestedMinimumHeight()), i15, iCombineMeasuredStates6 << 16);
            if (shouldCollapse()) {
                iResolveSizeAndState2 = 0;
            }
            setMeasuredDimension(iResolveSizeAndState, iResolveSizeAndState2);
            calculateTitlePosition(this.mTitlePosition);
            int[] iArr5 = this.mTitlePosition;
            int i21 = iArr5[1] - iArr5[0];
            if (shouldLayout(this.mTitleTextView)) {
                this.mTitleTextView.setMaxWidth(i21);
                i16 = i20;
                measureChildCollapseMargins(this.mTitleTextView, View.MeasureSpec.makeMeasureSpec(i21, Integer.MIN_VALUE), 0, i15, i16, iArr3);
            } else {
                i16 = i20;
            }
            if (shouldLayout(this.mSubtitleTextView)) {
                this.mSubtitleTextView.setMaxWidth(i21);
                measureChildCollapseMargins(this.mSubtitleTextView, View.MeasureSpec.makeMeasureSpec(i21, Integer.MIN_VALUE), 0, i4, i16 + i13, iArr3);
                return;
            }
            return;
        }
        int[] iArr6 = this.mTempMargins;
        boolean zIsLayoutRtl2 = ViewUtils.isLayoutRtl(this);
        int i22 = !zIsLayoutRtl2 ? 1 : 0;
        if (shouldLayout(this.mNavButtonView)) {
            changeToolbarPadding(null, this.mNavButtonView, z4, i3, false);
            i5 = 0;
            measureChildConstrained(this.mNavButtonView, i3, 0, i4, 0, -1);
            measuredWidth = this.mNavButtonView.getMeasuredWidth() + getHorizontalMargins(this.mNavButtonView);
            iMax = Math.max(0, this.mNavButtonView.getMeasuredHeight() + getVerticalMargins(this.mNavButtonView));
            iCombineMeasuredStates = View.combineMeasuredStates(0, ViewCompat.getMeasuredState(this.mNavButtonView));
        } else {
            i5 = 0;
            measuredWidth = 0;
            iMax = 0;
            iCombineMeasuredStates = 0;
        }
        if (shouldLayout(this.mCollapseButtonView)) {
            measureChildConstrained(this.mCollapseButtonView, i3, i5, i4, 0, this.mMaxButtonHeight);
            measuredWidth = this.mCollapseButtonView.getMeasuredWidth() + getHorizontalMargins(this.mCollapseButtonView);
            iMax2 = Math.max(iMax, this.mCollapseButtonView.getMeasuredHeight() + getVerticalMargins(this.mCollapseButtonView));
            iCombineMeasuredStates2 = View.combineMeasuredStates(iCombineMeasuredStates, ViewCompat.getMeasuredState(this.mCollapseButtonView));
        } else {
            iCombineMeasuredStates2 = iCombineMeasuredStates;
            iMax2 = iMax;
        }
        int contentInsetStart2 = getContentInsetStart();
        int iMax13 = Math.max(contentInsetStart2, measuredWidth);
        iArr6[zIsLayoutRtl2 ? 1 : 0] = Math.max(0, contentInsetStart2 - measuredWidth);
        if (shouldLayout(this.mTextButton)) {
            measureChildCollapseMargins(this.mTextButton, i3, iMax13, i4, 0, iArr6);
            int measuredWidth6 = this.mTextButton.getMeasuredWidth() + getHorizontalMargins(this.mTextButton);
            iMax3 = Math.max(iMax2, this.mTextButton.getMeasuredHeight() + getVerticalMargins(this.mTextButton));
            iCombineMeasuredStates3 = View.combineMeasuredStates(iCombineMeasuredStates2, ViewCompat.getMeasuredState(this.mTextButton));
            measuredWidth2 = measuredWidth6;
        } else {
            iMax3 = iMax2;
            iCombineMeasuredStates3 = iCombineMeasuredStates2;
            measuredWidth2 = 0;
        }
        boolean zIsSmallScreen = COUIResponsiveUtils.isSmallScreen(getContext(), View.MeasureSpec.getSize(i3));
        if (shouldLayout(this.mMenuView)) {
            MenuBuilder menuBuilder2 = (MenuBuilder) this.mMenuView.getMenu();
            changeToolbarPadding(menuBuilder2, this.mNavButtonView, z3, i3, false);
            menuBuilder = menuBuilder2;
            z2 = z3;
            measureChildConstrained(this.mMenuView, i3, iMax13, i4, 0, this.mMaxButtonHeight);
            iMax13 = iMax13;
            measuredWidth2 += this.mMenuView.getMeasuredWidth() + getHorizontalMargins(this.mMenuView);
            iMax3 = Math.max(iMax3, this.mMenuView.getMeasuredHeight() + getVerticalMargins(this.mMenuView));
            iCombineMeasuredStates3 = View.combineMeasuredStates(iCombineMeasuredStates3, ViewCompat.getMeasuredState(this.mMenuView));
            if (shouldLayout(this.mTextButton)) {
                z3 = z4;
                measuredWidth2 += this.mGapBetweenTextButtonAndMenuView;
            }
        } else {
            z2 = z4;
            menuBuilder = null;
        }
        if (measuredWidth2 > 0) {
            changeToolbarPadding(null, this.mNavButtonView, z2, i3, true);
            if (!zIsSmallScreen) {
                measuredWidth2 += this.mGapBeforeMenuView;
            }
        }
        addCustomViewsWithGravity(this.mTempViews, 8388611);
        this.mHasCustomViewBeforeTitle = false;
        int size2 = this.mTempViews.size();
        int i23 = 0;
        int iMax14 = iMax3;
        int iCombineMeasuredStates7 = iCombineMeasuredStates3;
        int iMeasureChildCollapseMargins = iMax13;
        while (true) {
            if (i23 >= size2) {
                iArr = iArr6;
                i6 = iMax14;
                break;
            }
            View view = this.mTempViews.get(i23);
            LayoutParams layoutParams2 = (LayoutParams) view.getLayoutParams();
            if (layoutParams2.mViewType != 0 || layoutParams2.mTypeTextButton || layoutParams2.mTypeSegmentButton || isDummyView(view, layoutParams2) || !shouldLayout(view)) {
                i12 = i23;
                iArr = iArr6;
                i6 = iMax14;
                if (isDummyView(view, layoutParams2) && this.mTempViews.size() == 1) {
                    this.mHasCustomViewBeforeTitle = false;
                    break;
                }
                iMax14 = i6;
            } else {
                i12 = i23;
                int[] iArr7 = iArr6;
                iArr = iArr7;
                iMeasureChildCollapseMargins += measureChildCollapseMargins(view, i3, iMeasureChildCollapseMargins + measuredWidth2, i4, 0, iArr7);
                iMax14 = Math.max(iMax14, view.getMeasuredHeight() + getVerticalMargins(view));
                int iCombineMeasuredStates8 = View.combineMeasuredStates(iCombineMeasuredStates7, ViewCompat.getMeasuredState(view));
                if (!layoutParams2.mTypeSearch && !zIsSmallScreen) {
                    this.mHasCustomViewBeforeTitle = true;
                }
                iCombineMeasuredStates7 = iCombineMeasuredStates8;
            }
            i23 = i12 + 1;
            size2 = size2;
            iArr6 = iArr;
        }
        boolean z5 = z2;
        changeToolbarPadding(menuBuilder, this.mNavButtonView, z5, i3, this.mHasCustomViewBeforeTitle || measuredWidth2 > 0);
        int i24 = this.mTitleMarginTop + this.mTitleMarginBottom;
        int i25 = this.mTitleMarginStart + this.mTitleMarginEnd;
        if (!shouldLayout(this.mSegmentButton) || this.mIsTitleCenterStyle) {
            iMax4 = i6;
            measuredWidth3 = 0;
        } else {
            if (shouldLayout(this.mTitleTextView)) {
                this.mTitleTextView.getLayoutParams().width = -2;
                this.mTitleTextView.setTextSize(0, this.mTitleTextSize);
                TextPaint paint = this.mTitleTextView.getPaint();
                CharSequence charSequence = this.mTitleText;
                iMeasureText = (int) paint.measureText(charSequence, 0, charSequence.length());
            } else {
                iMeasureText = 0;
            }
            int i26 = (size / 2) - iMeasureChildCollapseMargins;
            if (iMeasureText <= ((i26 - (this.mSegmentButtonMinWidth / 2)) - this.mSectionGap) - getPaddingStart()) {
                int paddingStart = ((i26 - iMeasureText) - this.mSectionGap) - getPaddingStart();
                measureChildMaxWidthConstrained(this.mSegmentButton, i3, iMeasureChildCollapseMargins, zIsSmallScreen ? Math.min(paddingStart * 2, this.mSegmentButtonMaxWidth) : paddingStart * 2, i4, this.mSegmentButtonHeight);
            } else {
                measureChildMaxWidthConstrained(this.mSegmentButton, i3, iMeasureChildCollapseMargins, (((i26 - getPaddingStart()) - this.mSectionGap) / 2) * 2, i4, this.mSegmentButtonHeight);
            }
            measuredWidth3 = this.mSegmentButton.getMeasuredWidth() + getHorizontalMargins(this.mSegmentButton);
            iMax4 = Math.max(i6, this.mSegmentButton.getMeasuredHeight() + getVerticalMargins(this.mSegmentButton));
            iCombineMeasuredStates7 = View.combineMeasuredStates(iCombineMeasuredStates7, ViewCompat.getMeasuredState(this.mSegmentButton));
        }
        int iMax15 = iMeasureChildCollapseMargins == 0 ? this.mSectionGap + measuredWidth2 : iMeasureChildCollapseMargins + measuredWidth2 + (this.mSectionGap * 2);
        if (shouldLayout(this.mTitleTextView)) {
            this.mTitleTextView.getLayoutParams().width = -2;
            this.mTitleTextView.setTextSize(0, this.mTitleTextSize);
            TextPaint paint2 = this.mTitleTextView.getPaint();
            CharSequence charSequence2 = this.mTitleText;
            int iMin = Math.min((int) paint2.measureText(charSequence2, 0, charSequence2.length()), this.mTitleTextMinWidth) + ((this.mHasCustomViewBeforeTitle || shouldLayout(this.mNavButtonView)) ? this.mGapBetweenNavigationAndTitle : 0);
            int i27 = this.mSectionGap;
            i7 = iMin;
            iMax15 = Math.max((iMeasureChildCollapseMargins + iMin + i27) * 2, (i27 + measuredWidth2) * 2);
        } else {
            i7 = 0;
        }
        addCustomCenterViews(this.mTempViews);
        int i28 = iCombineMeasuredStates7;
        int iMeasureChildCollapseMargins2 = measuredWidth3;
        int iCombineMeasuredStates9 = i28;
        int i29 = 0;
        for (int size3 = this.mTempViews.size(); i29 < size3; size3 = size3) {
            View view2 = this.mTempViews.get(i29);
            LayoutParams layoutParams3 = (LayoutParams) view2.getLayoutParams();
            int i30 = iCombineMeasuredStates9;
            if (layoutParams3.mViewType != 0 || layoutParams3.mTypeSegmentButton || !shouldLayout(view2) || isDummyView(view2, layoutParams3)) {
                i11 = i29;
                iArr2 = iArr;
                iMax4 = iMax4;
                iCombineMeasuredStates9 = i30;
            } else {
                i11 = i29;
                iArr2 = iArr;
                iMeasureChildCollapseMargins2 += measureChildCollapseMargins(view2, i3, iMax15, i4, 0, iArr2);
                iMax15 += iMeasureChildCollapseMargins2;
                iMax4 = Math.max(iMax4, view2.getMeasuredHeight() + getVerticalMargins(view2));
                iCombineMeasuredStates9 = View.combineMeasuredStates(i30, ViewCompat.getMeasuredState(view2));
            }
            i29 = i11 + 1;
            iArr = iArr2;
            size = size;
        }
        int iCombineMeasuredStates10 = iCombineMeasuredStates9;
        int i31 = size;
        int[] iArr8 = iArr;
        int iMax16 = iMax4;
        int i32 = i31 / 2;
        int i33 = i32 + (iMeasureChildCollapseMargins2 / 2);
        int paddingStart2 = (this.mSectionGap + i33) - getPaddingStart();
        int paddingEnd = (i33 + this.mSectionGap) - getPaddingEnd();
        int contentInsetEnd2 = getContentInsetEnd();
        boolean z6 = measuredWidth2 > 0 || shouldLayout(this.mExpandedActionView) || shouldLayout(this.mLogoView);
        if (iMeasureChildCollapseMargins2 > 0) {
            if (!z5) {
                paddingStart2 = paddingEnd;
            }
            iMax5 = iMeasureChildCollapseMargins + paddingStart2;
        } else {
            iMax5 = iMeasureChildCollapseMargins + Math.max(contentInsetEnd2, measuredWidth2);
        }
        iArr8[i22] = Math.max(0, contentInsetEnd2 - measuredWidth2);
        if (shouldLayout(this.mExpandedActionView)) {
            int i34 = iMax5;
            iMax5 = i34 + measureChildCollapseMargins(this.mExpandedActionView, i3, i34, i4, 0, iArr8);
            int iMax17 = Math.max(iMax16, this.mExpandedActionView.getMeasuredHeight() + getVerticalMargins(this.mExpandedActionView));
            iCombineMeasuredStates10 = View.combineMeasuredStates(iCombineMeasuredStates10, ViewCompat.getMeasuredState(this.mExpandedActionView));
            iMax16 = iMax17;
        }
        int iMeasureChildCollapseMargins3 = iMax5;
        if (shouldLayout(this.mLogoView)) {
            iMeasureChildCollapseMargins3 += measureChildCollapseMargins(this.mLogoView, i3, iMeasureChildCollapseMargins3, i4, 0, iArr8);
            iMax16 = Math.max(iMax16, this.mLogoView.getMeasuredHeight() + getVerticalMargins(this.mLogoView));
            iCombineMeasuredStates10 = View.combineMeasuredStates(iCombineMeasuredStates10, ViewCompat.getMeasuredState(this.mLogoView));
        }
        addCustomViewsWithGravity(this.mTempViews, GravityCompat.END);
        int size4 = this.mTempViews.size();
        int paddingEnd2 = iMeasureChildCollapseMargins2 > 0 ? ((i32 - (((this.mSectionGap * 2) + iMeasureChildCollapseMargins2) / 2)) - getPaddingEnd()) - measuredWidth2 : (((i31 - iMeasureChildCollapseMargins3) - i7) - getPaddingEnd()) - getPaddingStart();
        int i35 = iMax16;
        int iCombineMeasuredStates11 = iCombineMeasuredStates10;
        int measuredWidth7 = iMeasureChildCollapseMargins3;
        for (int i36 = 0; i36 < size4; i36++) {
            View view3 = this.mTempViews.get(i36);
            LayoutParams layoutParams4 = (LayoutParams) view3.getLayoutParams();
            if (layoutParams4.mViewType != 0 || layoutParams4.mTypeSegmentButton || layoutParams4.mTypeTextButton || !shouldLayout(view3) || isDummyView(view3, layoutParams4)) {
                iCombineMeasuredStates11 = iCombineMeasuredStates11;
            } else {
                int[] iArr9 = iArr8;
                int i37 = iCombineMeasuredStates11;
                int iMeasureChildCollapseMargins4 = measureChildCollapseMargins(view3, i3, measuredWidth7 + i7, paddingEnd2, i4, 0, iArr9);
                iArr8 = iArr9;
                if (iMeasureChildCollapseMargins2 == 0) {
                    measuredWidth7 += iMeasureChildCollapseMargins4;
                    z6 = true;
                }
                int iMax18 = Math.max(i35, view3.getMeasuredHeight() + getVerticalMargins(view3));
                iCombineMeasuredStates11 = View.combineMeasuredStates(i37, ViewCompat.getMeasuredState(view3));
                i35 = iMax18;
            }
        }
        int i38 = iCombineMeasuredStates11;
        if (shouldLayout(this.mDummyView)) {
            int i39 = measuredWidth7;
            measureChildCollapseMargins(this.mDummyView, i3, i39, i4, 0, iArr8);
            measuredWidth7 = i39 + this.mDummyView.getMeasuredWidth() + getHorizontalMargins(this.mDummyView);
        }
        if (iMeasureChildCollapseMargins2 == 0 && z6) {
            measuredWidth7 += this.mSectionGap;
        }
        int i40 = (this.mHasCustomViewBeforeTitle || shouldLayout(this.mNavButtonView)) ? this.mGapBetweenNavigationAndTitle : 0;
        int iMax19 = Math.max(0, ((((i31 - getPaddingLeft()) - getPaddingRight()) - measuredWidth7) - i25) - i40);
        if (shouldLayout(this.mTitleTextView)) {
            this.mTitleTextView.getLayoutParams().width = -1;
            this.mTitleTextView.setTextSize(0, this.mTitleTextSize);
            this.mTitleTextView.setMaxWidth(iMax19);
            i8 = i24;
            measureChildCollapseMargins(this.mTitleTextView, i3, measuredWidth7 + i25 + i40, i4, i8, iArr8);
            int measuredWidth8 = this.mTitleTextView.getMeasuredWidth() + getHorizontalMargins(this.mTitleTextView);
            int measuredHeight3 = this.mTitleTextView.getMeasuredHeight() + getVerticalMargins(this.mTitleTextView);
            int iCombineMeasuredStates12 = View.combineMeasuredStates(i38, ViewCompat.getMeasuredState(this.mTitleTextView));
            iMax6 = measuredWidth8;
            measuredHeight = measuredHeight3;
            iCombineMeasuredStates4 = iCombineMeasuredStates12;
        } else {
            i8 = i24;
            iCombineMeasuredStates4 = i38;
            iMax6 = 0;
            measuredHeight = 0;
        }
        if (shouldLayout(this.mSubtitleTextView)) {
            this.mSubtitleTextView.getLayoutParams().width = -1;
            this.mSubtitleTextView.setMaxWidth(iMax19);
            i9 = i3;
            i10 = i4;
            iMax6 = Math.max(iMax6, measureChildCollapseMargins(this.mSubtitleTextView, i9, i25 + measuredWidth7 + i40, i10, i8 + measuredHeight, iArr8));
            measuredHeight += this.mSubtitleTextView.getMeasuredHeight() + getVerticalMargins(this.mSubtitleTextView);
            iCombineMeasuredStates4 = View.combineMeasuredStates(iCombineMeasuredStates4, ViewCompat.getMeasuredState(this.mSubtitleTextView));
        } else {
            i9 = i3;
            i10 = i4;
        }
        setMeasuredDimension(ViewCompat.resolveSizeAndState(Math.max(measuredWidth7 + iMax6 + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), i9, iCombineMeasuredStates4 & (-16777216)), shouldCollapse() ? 0 : ViewCompat.resolveSizeAndState(Math.max(Math.max(i35, measuredHeight) + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), i10, iCombineMeasuredStates4 << 16));
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.View
    public void onRtlPropertiesChanged(int i3) {
        super.onRtlPropertiesChanged(i3);
        COUIRtlSpacingHelper cOUIRtlSpacingHelper = this.mContentInsets;
        if (cOUIRtlSpacingHelper != null) {
            cOUIRtlSpacingHelper.setDirection(i3 == 1);
        }
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        if (actionMasked == 0) {
            this.mEatingTouch = false;
        }
        if (!this.mEatingTouch) {
            boolean zOnTouchEvent = super.onTouchEvent(motionEvent);
            if (actionMasked == 0 && !zOnTouchEvent) {
                this.mEatingTouch = true;
            }
        }
        if (actionMasked == 1 || actionMasked == 3) {
            this.mEatingTouch = false;
        }
        return true;
    }

    public void performEditModeTransition(CharSequence charSequence, Runnable runnable) {
        cancelTransition();
        boolean zShouldLayout = shouldLayout(this.mTitleTextView);
        Runnable runnableCreateStructureSwitchOnceRunnable = createStructureSwitchOnceRunnable(runnable);
        prepareTitleExitOverlayForEditTransitionIfNeeded(zShouldLayout);
        EditModeTitleExitStarter editModeTitleExitStarterBuildEditModeTitleExitStarter = buildEditModeTitleExitStarter(zShouldLayout);
        editModeGroupTransition(runnableCreateStructureSwitchOnceRunnable, editModeTitleExitStarterBuildEditModeTitleExitStarter, buildRegisterTitleCrossFadePreDrawRunnable(zShouldLayout, charSequence, editModeTitleExitStarterBuildEditModeTitleExitStarter != null));
    }

    public void refresh() {
        int i3;
        int i4;
        TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(null, androidx.appcompat.R.styleable.ActionBar, androidx.appcompat.R.attr.actionBarStyle, 0);
        setOverflowIcon(getResources().getDrawable(com.support.appcompat.R.drawable.coui_toolbar_menu_icon_more, getContext().getTheme()));
        Drawable drawable = typedArrayObtainStyledAttributes.getDrawable(androidx.appcompat.R.styleable.ActionBar_homeAsUpIndicator);
        if (drawable != null) {
            setNavigationIcon(drawable);
        }
        COUIMaskRippleDrawable cOUIMaskRippleDrawable = this.mMaskRippleDrawable;
        if (cOUIMaskRippleDrawable != null) {
            cOUIMaskRippleDrawable.refresh(getContext());
        }
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate = this.mNavButtonEffectDelegate;
        if (toolbarMaterialEffectDelegate != null) {
            toolbarMaterialEffectDelegate.refreshRipple(getContext());
        }
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate2 = this.mTextButtonEffectDelegate;
        if (toolbarMaterialEffectDelegate2 != null) {
            toolbarMaterialEffectDelegate2.refreshRipple(getContext());
        }
        if (this.mMenuView != null) {
            COUIActionMenuView cOUIActionMenuView = this.mMenuView;
            cOUIActionMenuView.refresh();
            COUIPopupListWindow cOUIPopupListWindow = cOUIActionMenuView.mOverflowPopup;
            if (cOUIPopupListWindow != null && cOUIPopupListWindow.isShowing()) {
                cOUIActionMenuView.mOverflowPopup.dismiss();
            }
            cOUIActionMenuView.mOverflowPopup = null;
        }
        if (this.mTitleTextView != null && this.mTitleTextAppearance != 0) {
            setTitleTextAppearance(getContext(), this.mTitleTextAppearance);
        }
        TextView textView = this.mTitleTextView;
        if (textView != null && (i4 = this.mTitleTextColor) != 0) {
            textView.setTextColor(i4);
        }
        TextView textView2 = this.mSubtitleTextView;
        if (textView2 != null && this.mSubtitleTextAppearance != 0) {
            textView2.setTextAppearance(getContext(), this.mSubtitleTextAppearance);
        }
        TextView textView3 = this.mSubtitleTextView;
        if (textView3 != null && (i3 = this.mSubtitleTextColor) != 0) {
            textView3.setTextColor(i3);
        }
        if (this.mResId != 0) {
            getMenu().clear();
            inflateMenu(this.mResId);
        }
        typedArrayObtainStyledAttributes.recycle();
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setCollapsible(boolean z2) {
        this.mCollapsible = z2;
        requestLayout();
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setContentInsetsAbsolute(int i3, int i4) {
        this.mContentInsets.setAbsolute(i3, i4);
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setContentInsetsRelative(int i3, int i4) {
        this.mContentInsets.setRelative(i3, i4);
    }

    public void setCustomNonBlurFallbackColor(int i3) {
        if (this.mCustomNonBlurFallbackColor == i3) {
            return;
        }
        this.mCustomNonBlurFallbackColor = i3;
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate = this.mNavButtonEffectDelegate;
        if (toolbarMaterialEffectDelegate != null) {
            toolbarMaterialEffectDelegate.setCustomNonBlurFallbackColor(i3);
        }
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate2 = this.mTextButtonEffectDelegate;
        if (toolbarMaterialEffectDelegate2 != null) {
            toolbarMaterialEffectDelegate2.setCustomNonBlurFallbackColor(i3);
        }
        COUIActionMenuView cOUIActionMenuView = this.mMenuView;
        if (cOUIActionMenuView != null) {
            cOUIActionMenuView.setMenuItemNonBlurFallbackColor(i3);
        }
    }

    @Deprecated
    public void setEnableAddExtraWidth(boolean z2) {
    }

    @Deprecated
    public void setIfInBlurWindow(boolean z2) {
        setIsDrawableMaterialEffect(true);
        setUseMaterialEffectAttr(z2 ? MaterialEffectState.FORCE_ENABLE : MaterialEffectState.FORCE_DISABLE);
    }

    public void setIsDrawableMaterialEffect(boolean z2) {
        if (this.mIsDrawableMaterialEffect == z2) {
            return;
        }
        this.mIsDrawableMaterialEffect = z2;
        COUIActionMenuView cOUIActionMenuView = this.mMenuView;
        if (cOUIActionMenuView != null) {
            cOUIActionMenuView.setMenuItemIsDrawableMaterialEffect(z2);
        }
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate = this.mNavButtonEffectDelegate;
        if (toolbarMaterialEffectDelegate != null) {
            toolbarMaterialEffectDelegate.setIsDrawableMaterialEffect(z2);
        }
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate2 = this.mTextButtonEffectDelegate;
        if (toolbarMaterialEffectDelegate2 != null) {
            toolbarMaterialEffectDelegate2.setIsDrawableMaterialEffect(z2);
        }
    }

    @Deprecated
    public void setIsFixTitleFontSize(boolean z2) {
        COUIActionMenuView cOUIActionMenuView = this.mMenuView;
        if (cOUIActionMenuView != null) {
            cOUIActionMenuView.setIsFixTitleFontSize(z2);
        } else {
            Log.e(TAG, "setIsFixTitleFontSize when mMenuView is null");
        }
    }

    public void setIsInsideSideNavigationBar(boolean z2) {
        if (this.mIsInsideSideNavigationBar != z2) {
            this.mIsInsideSideNavigationBar = z2;
            requestLayout();
        }
    }

    public void setIsTitleCenterStyle(boolean z2) {
        ensureMenuView();
        this.mIsTitleCenterStyle = z2;
        LayoutParams layoutParams = (LayoutParams) this.mMenuView.getLayoutParams();
        boolean z3 = this.mIsTitleCenterStyle;
        if (z3) {
            ((ViewGroup.MarginLayoutParams) layoutParams).width = -1;
        } else {
            ((ViewGroup.MarginLayoutParams) layoutParams).width = -2;
        }
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setTextAlignment(z3 ? 4 : 5);
        }
        this.mMenuView.setLayoutParams(layoutParams);
        requestLayout();
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setLogo(int i3) {
        setLogo(AppCompatResources.getDrawable(getContext(), i3));
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setLogoDescription(int i3) {
        setLogoDescription(getContext().getText(i3));
    }

    public void setMaterialEffectType(int i3) {
        this.mMaterialType = i3;
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate = this.mNavButtonEffectDelegate;
        if (toolbarMaterialEffectDelegate != null) {
            toolbarMaterialEffectDelegate.setMaterialEffectType(i3);
        }
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate2 = this.mTextButtonEffectDelegate;
        if (toolbarMaterialEffectDelegate2 != null) {
            toolbarMaterialEffectDelegate2.setMaterialEffectType(i3);
        }
        COUIActionMenuView cOUIActionMenuView = this.mMenuView;
        if (cOUIActionMenuView != null) {
            cOUIActionMenuView.setMenuItemMaterialEffectType(i3);
        }
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setMenuCallbacks(MenuPresenter.Callback callback, MenuBuilder.Callback callback2) {
        this.mActionMenuPresenterCallback = callback;
        this.mMenuBuilderCallback = callback2;
    }

    public void setMenuItemBlurParams(COUIMaterialBlurEffect.BaseBlurEffectParams baseBlurEffectParams) {
        if (this.mMenuView == null || !ToolbarMaterialHelper.isMaterialEffectOsSupported()) {
            return;
        }
        this.mMenuView.setMenuItemBlurParams(baseBlurEffectParams);
    }

    public void setMenuViewColor(int i3) {
        Drawable overflowIcon;
        COUIActionMenuView cOUIActionMenuView = this.mMenuView;
        if (cOUIActionMenuView == null || (overflowIcon = cOUIActionMenuView.getOverflowIcon()) == null || (overflowIcon instanceof AnimatedStateListDrawableCompat)) {
            return;
        }
        DrawableCompat.setTint(overflowIcon, i3);
        this.mMenuView.setOverflowIcon(overflowIcon);
    }

    public void setMinTitleTextSize(float f3) {
        float f4 = this.mTextMaxSize;
        if (f3 > f4) {
            f3 = f4;
        }
        this.mTextMinSize = f3;
    }

    @Override // android.view.View
    public void setMinimumHeight(int i3) {
        this.mMinHeight = i3;
        super.setMinimumHeight(i3);
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setNavigationContentDescription(int i3) {
        setNavigationContentDescription(i3 != 0 ? getContext().getText(i3) : null);
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setNavigationIcon(int i3) {
        setNavigationIcon(AppCompatResources.getDrawable(getContext(), i3));
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setNavigationOnClickListener(View.OnClickListener onClickListener) {
        ensureNavButtonView();
        this.mNavButtonView.setOnClickListener(onClickListener);
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener onMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setOverflowIcon(Drawable drawable) {
        ensureMenu();
        this.mMenuView.setOverflowIcon(drawable);
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public void setPopupMenuRuleEnabled(boolean z2) {
        this.mPopupRuleEnable = z2;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setPopupTheme(int i3) {
        if (this.mPopupTheme != i3) {
            this.mPopupTheme = i3;
            if (i3 == 0) {
                this.mPopupContext = getContext();
            } else {
                this.mPopupContext = new ContextThemeWrapper(getContext(), i3);
            }
        }
    }

    public void setPopupWindowOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        if (this.mMenuView != null) {
            this.mMenuView.setPopupWindowOnDismissListener(onDismissListener);
        }
    }

    public void setRedDot(int i3, int i4) {
        COUIActionMenuView cOUIActionMenuView = this.mMenuView;
        if (cOUIActionMenuView == null) {
            Log.e(TAG, "The COUIActionMenuView is null");
        } else {
            cOUIActionMenuView.setRedDot(i3, i4);
        }
    }

    public void setSearchFullWidthFlag(boolean z2) {
        this.mSearchFullWidthFlag = z2;
    }

    public void setSearchView(View view) {
        LayoutParams layoutParams;
        if (view != null) {
            layoutParams = view.getLayoutParams() == null ? new LayoutParams(new LayoutParams(-1, this.mToolbarHeight)) : new LayoutParams(view.getLayoutParams());
        } else {
            layoutParams = null;
        }
        setSearchView(view, layoutParams);
    }

    public void setSectionGapMediumLarge(int i3) {
        if (this.mSectionGapMediumLarge != i3) {
            this.mSectionGapMediumLarge = i3;
            requestLayout();
        }
    }

    public void setSegmentButtons(View view) {
        View view2 = this.mSegmentButton;
        if (view2 != null) {
            removeView(view2);
        }
        if (view != null) {
            setSegmentButtons(view, view.getLayoutParams() == null ? new LayoutParams(new LayoutParams(-2, this.mSegmentButtonHeight)) : new LayoutParams(view.getLayoutParams()));
        } else {
            this.mSegmentButton = null;
        }
    }

    @Deprecated
    public void setSubMenuList(ArrayList<PopupListItem> arrayList, int i3, COUISubMenuClickListener cOUISubMenuClickListener) {
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setSubtitle(int i3) {
        setSubtitle(getContext().getText(i3));
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setSubtitleTextAppearance(Context context, int i3) {
        this.mSubtitleTextAppearance = i3;
        TextView textView = this.mSubtitleTextView;
        if (textView != null) {
            textView.setTextAppearance(context, i3);
        }
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setSubtitleTextColor(int i3) {
        this.mSubtitleTextColor = i3;
        TextView textView = this.mSubtitleTextView;
        if (textView != null) {
            textView.setTextColor(i3);
        }
    }

    public void setTextButton(View view) {
        View view2 = this.mTextButton;
        if (view2 != null) {
            removeView(view2);
            ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate = this.mTextButtonEffectDelegate;
            if (toolbarMaterialEffectDelegate != null) {
                toolbarMaterialEffectDelegate.detach();
                this.mTextButtonEffectDelegate = null;
            }
        }
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(view);
            }
            setTextButton(view, view.getLayoutParams() == null ? new LayoutParams(new LayoutParams(-2, -2)) : new LayoutParams(view.getLayoutParams()));
            return;
        }
        this.mTextButton = null;
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate2 = this.mTextButtonEffectDelegate;
        if (toolbarMaterialEffectDelegate2 != null) {
            toolbarMaterialEffectDelegate2.detach();
            this.mTextButtonEffectDelegate = null;
        }
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setTitle(int i3) {
        setTitle(getContext().getText(i3));
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setTitleMarginStart(int i3) {
        this.mTitleMarginStart = i3;
        requestLayout();
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setTitleTextAppearance(Context context, int i3) {
        this.mTitleTextAppearance = i3;
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setTextAppearance(context, i3);
            if (this.mTitleType == 1) {
                this.mTitleTextView.setTextSize(0, COUIChangeTextUtil.getSuitableFontSize(this.mTitleTextView.getTextSize(), getContext().getResources().getConfiguration().fontScale, 2));
            }
            TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(this.mTitleTextAppearance, new int[]{android.R.attr.minHeight});
            if (typedArrayObtainStyledAttributes != null) {
                this.mTitleTextView.setMinHeight(typedArrayObtainStyledAttributes.getDimensionPixelSize(0, 0));
                typedArrayObtainStyledAttributes.recycle();
            }
            TypedArray typedArrayObtainStyledAttributes2 = context.obtainStyledAttributes(this.mTitleTextAppearance, new int[]{android.R.attr.lineSpacingMultiplier});
            if (typedArrayObtainStyledAttributes2 != null) {
                float f3 = typedArrayObtainStyledAttributes2.getFloat(0, 1.4f);
                TextView textView2 = this.mTitleTextView;
                textView2.setLineSpacing(textView2.getLineSpacingExtra(), f3);
                typedArrayObtainStyledAttributes2.recycle();
            }
            TypedArray typedArrayObtainStyledAttributes3 = context.obtainStyledAttributes(this.mTitleTextAppearance, new int[]{android.R.attr.textAlignment});
            if (typedArrayObtainStyledAttributes3 != null) {
                int integer = typedArrayObtainStyledAttributes3.getInteger(0, 5);
                if (integer >= 0) {
                    this.mTitleTextView.setTextAlignment(integer);
                }
                typedArrayObtainStyledAttributes3.recycle();
            }
            TypedArray typedArrayObtainStyledAttributes4 = context.obtainStyledAttributes(this.mTitleTextAppearance, new int[]{android.R.attr.maxLines});
            if (typedArrayObtainStyledAttributes4 != null) {
                int integer2 = typedArrayObtainStyledAttributes4.getInteger(0, 1);
                if (integer2 >= 1) {
                    this.mTitleTextView.setSingleLine(false);
                    this.mTitleTextView.setMaxLines(integer2);
                }
                typedArrayObtainStyledAttributes4.recycle();
            }
            this.mTextMaxSize = this.mTitleTextView.getTextSize();
            this.mTitleTextSize = this.mTitleTextView.getTextSize();
        }
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setTitleTextColor(int i3) {
        this.mTitleTextColor = i3;
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setTextColor(i3);
        }
    }

    public void setTitleTextSize(float f3) {
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setTextSize(f3);
            this.mTitleTextSize = TypedValue.applyDimension(1, f3, getResources().getDisplayMetrics());
        }
    }

    public void setTitleTextViewTypeface(Typeface typeface) {
        ensureTitleTextView();
        this.mTitleTextView.setTypeface(typeface);
    }

    public void setUseMaterialEffectAttr(MaterialEffectState materialEffectState) {
        int value = materialEffectState.getValue();
        if (this.mUseMaterialEffectAttr == value) {
            return;
        }
        this.mUseMaterialEffectAttr = value;
        COUIActionMenuView cOUIActionMenuView = this.mMenuView;
        if (cOUIActionMenuView != null) {
            cOUIActionMenuView.setMenuItemUseMaterialEffectAttr(value);
        }
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate = this.mNavButtonEffectDelegate;
        if (toolbarMaterialEffectDelegate != null) {
            toolbarMaterialEffectDelegate.setUseMaterialEffectAttr(value);
        }
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate2 = this.mTextButtonEffectDelegate;
        if (toolbarMaterialEffectDelegate2 != null) {
            toolbarMaterialEffectDelegate2.setUseMaterialEffectAttr(value);
        }
    }

    public void setUseResponsivePadding(boolean z2) {
        this.mUseResponsivePadding = z2;
        requestLayout();
    }

    @Override // androidx.appcompat.widget.Toolbar
    public boolean showOverflowMenu() {
        return (this.mMenuView == null || this.mMenuView.getWindowToken() == null) ? super.showOverflowMenu() : this.mMenuView.showOverflowMenu();
    }

    public void tintNavigationIconDrawable(int i3) {
        Drawable drawable;
        ImageButton imageButton = this.mNavButtonView;
        if (imageButton == null || (drawable = imageButton.getDrawable()) == null || (drawable instanceof AnimatedStateListDrawableCompat)) {
            return;
        }
        DrawableCompat.setTint(drawable, i3);
    }

    public COUIToolbar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, androidx.appcompat.R.attr.toolbarStyle);
    }

    private int measureChildCollapseMargins(View view, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
        boolean z2;
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        int i8 = marginLayoutParams.leftMargin - iArr[0];
        boolean z3 = true;
        int i9 = marginLayoutParams.rightMargin - iArr[1];
        int iMax = Math.max(0, i8) + Math.max(0, i9);
        iArr[0] = Math.max(0, -i8);
        iArr[1] = Math.max(0, -i9);
        if (marginLayoutParams instanceof LayoutParams) {
            LayoutParams layoutParams = (LayoutParams) marginLayoutParams;
            z2 = layoutParams.mTypeSearch && this.mHasSearchViewFlag;
            if (!layoutParams.mTypeTitle || !this.mIsTitleCenterStyle) {
                z3 = false;
            }
        } else {
            z2 = false;
            z3 = false;
        }
        int childMeasureSpec = (z2 || z3) ? ViewGroup.getChildMeasureSpec(i3, iMax, marginLayoutParams.width) : ViewGroup.getChildMeasureSpec(i3, getPaddingLeft() + getPaddingRight() + iMax + i4, marginLayoutParams.width);
        int childMeasureSpec2 = ViewGroup.getChildMeasureSpec(i6, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + i7, marginLayoutParams.height);
        int minimumWidth = marginLayoutParams.width;
        if (minimumWidth <= 0) {
            minimumWidth = -1;
        }
        int i10 = marginLayoutParams.height;
        int minimumHeight = i10 > 0 ? i10 : -1;
        if (minimumWidth <= 0 && minimumHeight <= 0 && view.getMinimumWidth() > 0 && view.getMinimumHeight() > 0 && (marginLayoutParams.width == -2 || marginLayoutParams.height == -2)) {
            minimumWidth = view.getMinimumWidth();
            minimumHeight = view.getMinimumHeight();
        }
        if (minimumWidth <= 0 || minimumHeight <= 0) {
            view.measure(childMeasureSpec, childMeasureSpec2);
        } else {
            view.measure(View.MeasureSpec.makeMeasureSpec(minimumWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(minimumHeight, 1073741824));
        }
        if (!z2) {
            if (view.getMeasuredWidth() > i5) {
                view.measure(View.MeasureSpec.makeMeasureSpec(Math.max(i5, 0), 1073741824), childMeasureSpec2);
            }
            return view.getMeasuredWidth() + iMax;
        }
        COUIActionMenuView cOUIActionMenuView = this.mMenuView;
        if (cOUIActionMenuView != null && cOUIActionMenuView.getVisibility() != 8 && !this.mSearchFullWidthFlag) {
            view.measure(ViewGroup.getChildMeasureSpec(i3, iMax, ((view.getMeasuredWidth() - this.mMenuView.getMeasuredWidth()) - (this.mMenuView.getMeasuredWidth() != 0 ? getPaddingEnd() : 0)) - this.mGapBetweenSearchViewAndMenu), childMeasureSpec2);
        }
        return iMax;
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup
    public /* bridge */ /* synthetic */ Toolbar.LayoutParams generateDefaultLayoutParams() {
        return generateDefaultLayoutParams();
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return generateLayoutParams(layoutParams);
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setLogo(Drawable drawable) {
        if (drawable != null) {
            ensureLogoView();
            if (this.mLogoView.getParent() == null) {
                addSystemView(this.mLogoView);
                updateChildVisibilityForExpandedActionView(this.mLogoView);
            }
        } else {
            ImageView imageView = this.mLogoView;
            if (imageView != null && imageView.getParent() != null) {
                removeView(this.mLogoView);
            }
        }
        ImageView imageView2 = this.mLogoView;
        if (imageView2 != null) {
            imageView2.setImageDrawable(drawable);
        }
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setLogoDescription(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            ensureLogoView();
        }
        ImageView imageView = this.mLogoView;
        if (imageView != null) {
            imageView.setContentDescription(charSequence);
        }
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setNavigationContentDescription(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            ensureNavButtonView();
        }
        ImageButton imageButton = this.mNavButtonView;
        if (imageButton != null) {
            imageButton.setContentDescription(charSequence);
        }
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setNavigationIcon(Drawable drawable) {
        if (drawable != null) {
            ensureNavButtonView();
            if (this.mNavButtonView.getParent() == null) {
                addSystemView(this.mNavButtonView);
                updateChildVisibilityForExpandedActionView(this.mNavButtonView);
            }
        } else {
            ImageButton imageButton = this.mNavButtonView;
            if (imageButton != null && imageButton.getParent() != null) {
                removeView(this.mNavButtonView);
            }
        }
        ImageButton imageButton2 = this.mNavButtonView;
        if (imageButton2 != null) {
            imageButton2.setImageDrawable(drawable);
        }
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setSubtitle(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            TextView textView = this.mSubtitleTextView;
            if (textView != null && textView.getParent() != null) {
                removeView(this.mSubtitleTextView);
            }
        } else {
            if (this.mSubtitleTextView == null) {
                Context context = getContext();
                this.mSubtitleTextView = new TextView(context);
                LayoutParams layoutParamsGenerateDefaultLayoutParams = generateDefaultLayoutParams();
                layoutParamsGenerateDefaultLayoutParams.mTypeTitle = true;
                this.mSubtitleTextView.setLayoutParams(layoutParamsGenerateDefaultLayoutParams);
                this.mSubtitleTextView.setSingleLine();
                this.mSubtitleTextView.setEllipsize(TextUtils.TruncateAt.END);
                int i3 = this.mSubtitleTextAppearance;
                if (i3 != 0) {
                    this.mSubtitleTextView.setTextAppearance(context, i3);
                }
                int i4 = this.mSubtitleTextColor;
                if (i4 != 0) {
                    this.mSubtitleTextView.setTextColor(i4);
                }
            }
            if (this.mSubtitleTextView.getParent() == null) {
                addSystemView(this.mSubtitleTextView);
                updateChildVisibilityForExpandedActionView(this.mSubtitleTextView);
            }
        }
        TextView textView2 = this.mSubtitleTextView;
        if (textView2 != null) {
            textView2.setTextAlignment(5);
            this.mSubtitleTextView.setTextDirection(5);
            this.mSubtitleTextView.setText(charSequence);
        }
        this.mSubtitleText = charSequence;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setTitle(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            TextView textView = this.mTitleTextView;
            if (textView != null && textView.getParent() != null) {
                removeView(this.mTitleTextView);
            }
        } else {
            ensureTitleTextView();
            if (this.mTitleTextView.getParent() == null) {
                addSystemView(this.mTitleTextView);
                updateChildVisibilityForExpandedActionView(this.mTitleTextView);
            }
        }
        TextView textView2 = this.mTitleTextView;
        if (textView2 != null) {
            textView2.setText(charSequence);
            this.mTitleTextSize = this.mTitleTextView.getTextSize();
        }
        this.mTitleText = charSequence;
    }

    public COUIToolbar(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, R.style.Widget_COUI_Toolbar);
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup
    public /* bridge */ /* synthetic */ Toolbar.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return generateLayoutParams(attributeSet);
    }

    public COUIToolbar(Context context, AttributeSet attributeSet, int i3, int i4) {
        int i5;
        super(context, attributeSet, i3);
        COUIRtlSpacingHelper cOUIRtlSpacingHelper = new COUIRtlSpacingHelper();
        this.mContentInsets = cOUIRtlSpacingHelper;
        this.mTempViews = new ArrayList<>();
        this.mTempMargins = new int[2];
        this.mMenuViewItemClickListener = new AnonymousClass1();
        this.mSearchCollapsingMargins = new int[2];
        this.mShowOverflowMenuRunnable = new AnonymousClass2();
        this.mTitleExitOverlayOldRect = new RectF();
        this.mTitleExitOverlayPrepared = false;
        this.mDisplayFrame = null;
        this.mWindowFrame = null;
        this.mHasCustomViewBeforeTitle = false;
        this.mGravity = NavigationBarView.ITEM_GRAVITY_START_CENTER;
        this.mIsTitleCenterStyle = false;
        this.mTitlePosition = new int[2];
        this.mTitleTextSize = 0.0f;
        this.mHasSearchViewFlag = false;
        this.mSearchFullWidthFlag = false;
        this.mIsInsideSideNavigationBar = false;
        this.mPopupRuleEnable = true;
        this.mUseResponsivePadding = true;
        this.mDummyView = null;
        this.mUseMaterialEffectAttr = MaterialEffectState.FOLLOW_SYSTEM.getValue();
        this.mIsDrawableMaterialEffect = false;
        this.mCustomNonBlurFallbackColor = 0;
        this.mMaterialType = 0;
        setClipToPadding(false);
        setClipChildren(false);
        if (attributeSet != null) {
            int styleAttribute = attributeSet.getStyleAttribute();
            this.mStyle = styleAttribute;
            if (styleAttribute == 0) {
                this.mStyle = i3;
            }
        } else {
            this.mStyle = 0;
        }
        TintTypedArray tintTypedArrayObtainStyledAttributes = TintTypedArray.obtainStyledAttributes(getContext(), attributeSet, R.styleable.COUIToolbar, R.attr.couiToolbarStyle, i4);
        int i6 = R.styleable.COUIToolbar_titleType;
        if (tintTypedArrayObtainStyledAttributes.hasValue(i6)) {
            this.mTitleType = tintTypedArrayObtainStyledAttributes.getInt(i6, 0);
        }
        this.mTitleTextAppearance = tintTypedArrayObtainStyledAttributes.getResourceId(R.styleable.COUIToolbar_supportTitleTextAppearance, 0);
        this.mSubtitleTextAppearance = tintTypedArrayObtainStyledAttributes.getResourceId(R.styleable.COUIToolbar_supportSubtitleTextAppearance, 0);
        this.mGravity = tintTypedArrayObtainStyledAttributes.getInteger(R.styleable.COUIToolbar_android_gravity, this.mGravity);
        this.mButtonGravity = tintTypedArrayObtainStyledAttributes.getInteger(R.styleable.COUIToolbar_supportButtonGravity, 48);
        this.mTitleMarginStart = tintTypedArrayObtainStyledAttributes.getDimensionPixelOffset(R.styleable.COUIToolbar_supportTitleMargins, 0);
        this.mIsTiny = tintTypedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIToolbar_supportIsTiny, false);
        this.mIsInsidePanel = tintTypedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIToolbar_supportPanelStyle, false);
        int i7 = this.mTitleMarginStart;
        this.mTitleMarginEnd = i7;
        this.mTitleMarginTop = i7;
        this.mTitleMarginBottom = i7;
        int dimensionPixelOffset = tintTypedArrayObtainStyledAttributes.getDimensionPixelOffset(R.styleable.COUIToolbar_supportTitleMarginStart, getContext().getResources().getDimensionPixelSize(R.dimen.coui_toolbar_support_margin_start));
        if (dimensionPixelOffset >= 0) {
            this.mTitleMarginStart = dimensionPixelOffset;
        }
        int dimensionPixelOffset2 = tintTypedArrayObtainStyledAttributes.getDimensionPixelOffset(R.styleable.COUIToolbar_supportTitleMarginEnd, -1);
        if (dimensionPixelOffset2 >= 0) {
            this.mTitleMarginEnd = dimensionPixelOffset2;
        }
        int dimensionPixelOffset3 = tintTypedArrayObtainStyledAttributes.getDimensionPixelOffset(R.styleable.COUIToolbar_supportTitleMarginTop, -1);
        if (dimensionPixelOffset3 >= 0) {
            this.mTitleMarginTop = dimensionPixelOffset3;
        }
        int dimensionPixelOffset4 = tintTypedArrayObtainStyledAttributes.getDimensionPixelOffset(R.styleable.COUIToolbar_supportTitleMarginBottom, -1);
        if (dimensionPixelOffset4 >= 0) {
            this.mTitleMarginBottom = dimensionPixelOffset4;
        }
        this.mTitlePaddingTop = tintTypedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIToolbar_supportTitlePaddingTop, 0);
        this.mTitlePaddingBottom = tintTypedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIToolbar_supportTitlePaddingBottom, 0);
        this.mMaxButtonHeight = tintTypedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIToolbar_supportMaxButtonHeight, -1);
        int dimensionPixelOffset5 = tintTypedArrayObtainStyledAttributes.getDimensionPixelOffset(R.styleable.COUIToolbar_supportContentInsetStart, Integer.MIN_VALUE);
        int dimensionPixelOffset6 = tintTypedArrayObtainStyledAttributes.getDimensionPixelOffset(R.styleable.COUIToolbar_supportContentInsetEnd, Integer.MIN_VALUE);
        cOUIRtlSpacingHelper.setAbsolute(tintTypedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIToolbar_supportContentInsetLeft, 0), tintTypedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIToolbar_supportContentInsetRight, 0));
        if (dimensionPixelOffset5 != Integer.MIN_VALUE || dimensionPixelOffset6 != Integer.MIN_VALUE) {
            cOUIRtlSpacingHelper.setRelative(dimensionPixelOffset5, dimensionPixelOffset6);
        }
        this.mCollapseIcon = tintTypedArrayObtainStyledAttributes.getDrawable(R.styleable.COUIToolbar_supportCollapseIcon);
        this.mCollapseDescription = tintTypedArrayObtainStyledAttributes.getText(R.styleable.COUIToolbar_supportCollapseContentDescription);
        CharSequence text = tintTypedArrayObtainStyledAttributes.getText(R.styleable.COUIToolbar_supportTitle);
        if (!TextUtils.isEmpty(text)) {
            setTitle(text);
        }
        CharSequence text2 = tintTypedArrayObtainStyledAttributes.getText(R.styleable.COUIToolbar_supportSubtitle);
        if (!TextUtils.isEmpty(text2)) {
            setSubtitle(text2);
        }
        this.mPopupContext = getContext();
        setPopupTheme(tintTypedArrayObtainStyledAttributes.getResourceId(R.styleable.COUIToolbar_supportPopupTheme, 0));
        Drawable drawable = tintTypedArrayObtainStyledAttributes.getDrawable(R.styleable.COUIToolbar_supportNavigationIcon);
        if (drawable != null) {
            setNavigationIcon(drawable);
        }
        CharSequence text3 = tintTypedArrayObtainStyledAttributes.getText(R.styleable.COUIToolbar_supportNavigationContentDescription);
        if (!TextUtils.isEmpty(text3)) {
            setNavigationContentDescription(text3);
        }
        this.mMinHeight = tintTypedArrayObtainStyledAttributes.getDimensionPixelSize(androidx.appcompat.R.styleable.Toolbar_android_minHeight, 0);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int i8 = R.styleable.COUIToolbar_minTitleTextSize;
        if (tintTypedArrayObtainStyledAttributes.hasValue(i8)) {
            this.mTextMinSize = tintTypedArrayObtainStyledAttributes.getDimensionPixelSize(i8, (int) (displayMetrics.scaledDensity * 16.0f));
        } else {
            this.mTextMinSize = displayMetrics.scaledDensity * 16.0f;
        }
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(this.mTitleTextAppearance, new int[]{android.R.attr.textSize});
        if (typedArrayObtainStyledAttributes != null) {
            this.mTextMaxSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(0, (int) (displayMetrics.scaledDensity * 24.0f));
            typedArrayObtainStyledAttributes.recycle();
        }
        if (this.mTitleType == 1) {
            this.mTextMaxSize = COUIChangeTextUtil.getSuitableFontSize(this.mTextMaxSize, getResources().getConfiguration().fontScale, 2);
            this.mTextMinSize = COUIChangeTextUtil.getSuitableFontSize(this.mTextMinSize, getResources().getConfiguration().fontScale, 2);
        }
        this.mSegmentButtonMaxWidth = getContext().getResources().getDimensionPixelSize(R.dimen.coui_toolbar_segment_button_max_width);
        this.mSegmentButtonMinWidth = getContext().getResources().getDimensionPixelSize(R.dimen.coui_toolbar_segment_button_min_width);
        this.mSectionGapMediumLarge = getContext().getResources().getDimensionPixelSize(R.dimen.coui_toolbar_section_gap);
        this.mSectionGapSmall = getContext().getResources().getDimensionPixelSize(R.dimen.coui_toolbar_section_gap_small);
        this.mTitleTextMinWidth = getContext().getResources().getDimensionPixelSize(R.dimen.coui_toolbar_title_text_min_width);
        this.mToolbarHeight = getContext().getResources().getDimensionPixelSize(R.dimen.toolbar_min_height);
        this.mSegmentButtonHeight = getContext().getResources().getDimensionPixelSize(R.dimen.segment_button_height);
        this.mToolbarNormalPaddingLeft = getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_normal_menu_padding_left);
        this.mGapBeforeMenuView = getContext().getResources().getDimensionPixelSize(R.dimen.coui_toolbar_gap_before_menu);
        this.mGapBetweenTextButtonAndMenuView = getContext().getResources().getDimensionPixelSize(R.dimen.coui_actionbar_menuitemview_item_spacing);
        if (this.mIsTiny) {
            this.mToolbarNormalPaddingRight = getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_normal_menu_padding_tiny_right);
            changeBackViewParams();
        } else {
            this.mToolbarNormalPaddingRight = getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_normal_menu_padding_right);
        }
        this.mToolbarCenterTitlePaddingLeft = getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_center_title_padding_left);
        this.mToolbarCenterTitlePaddingRight = getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_center_title_padding_right);
        this.mToolbarOverFlowPadding = getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_overflow_menu_padding);
        this.mTitleMinWidth = getContext().getResources().getDimensionPixelOffset(R.dimen.coui_toolbar_title_min_width);
        this.mGapBetweenSearchViewAndMenu = getContext().getResources().getDimensionPixelOffset(R.dimen.coui_toolbar_gap_between_search_and_menu);
        this.mGapBetweenNavigationAndTitle = getContext().getResources().getDimensionPixelOffset(R.dimen.coui_toolbar_gap_between_navigation_and_title);
        int i9 = R.styleable.COUIToolbar_titleCenter;
        if (tintTypedArrayObtainStyledAttributes.hasValue(i9)) {
            this.mIsTitleCenterStyle = tintTypedArrayObtainStyledAttributes.getBoolean(i9, false);
        }
        TextView textView = this.mSubtitleTextView;
        if (textView != null && (i5 = this.mSubtitleTextAppearance) != 0) {
            textView.setTextAppearance(context, i5);
        }
        int i10 = R.styleable.COUIToolbar_supportMaterialType;
        if (tintTypedArrayObtainStyledAttributes.hasValue(i10)) {
            int integer = tintTypedArrayObtainStyledAttributes.getInteger(i10, 0);
            this.mMaterialType = integer;
            setMaterialEffectType(integer);
        }
        setWillNotDraw(false);
        tintTypedArrayObtainStyledAttributes.recycle();
        UIUtil.setViewClipExpandSize(this, 120, 120, 120, 120);
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup
    public /* bridge */ /* synthetic */ Toolbar.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return generateLayoutParams(layoutParams);
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    public static class LayoutParams extends Toolbar.LayoutParams {
        static final int CUSTOM = 0;
        static final int EXPANDED = 2;
        static final int SYSTEM = 1;
        boolean mTypeSearch;
        boolean mTypeSegmentButton;
        boolean mTypeTextButton;
        boolean mTypeTitle;
        int mViewType;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.mViewType = 0;
            this.mTypeSearch = false;
            this.mTypeTitle = false;
            this.mTypeSegmentButton = false;
            this.mTypeTextButton = false;
        }

        public void copyMarginsFromCompat(ViewGroup.MarginLayoutParams marginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) this).leftMargin = marginLayoutParams.leftMargin;
            ((ViewGroup.MarginLayoutParams) this).topMargin = marginLayoutParams.topMargin;
            ((ViewGroup.MarginLayoutParams) this).rightMargin = marginLayoutParams.rightMargin;
            ((ViewGroup.MarginLayoutParams) this).bottomMargin = marginLayoutParams.bottomMargin;
        }

        public LayoutParams(int i3, int i4) {
            super(i3, i4);
            this.mViewType = 0;
            this.mTypeSearch = false;
            this.mTypeTitle = false;
            this.mTypeSegmentButton = false;
            this.mTypeTextButton = false;
            this.gravity = NavigationBarView.ITEM_GRAVITY_START_CENTER;
        }

        public LayoutParams(int i3, int i4, int i5) {
            super(i3, i4);
            this.mViewType = 0;
            this.mTypeSearch = false;
            this.mTypeTitle = false;
            this.mTypeSegmentButton = false;
            this.mTypeTextButton = false;
            this.gravity = i5;
        }

        public LayoutParams(int i3) {
            this(-2, -1, i3);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((Toolbar.LayoutParams) layoutParams);
            this.mViewType = 0;
            this.mTypeSearch = false;
            this.mTypeTitle = false;
            this.mTypeSegmentButton = false;
            this.mTypeTextButton = false;
            this.mViewType = layoutParams.mViewType;
        }

        public LayoutParams(ActionBar.LayoutParams layoutParams) {
            super(layoutParams);
            this.mViewType = 0;
            this.mTypeSearch = false;
            this.mTypeTitle = false;
            this.mTypeSegmentButton = false;
            this.mTypeTextButton = false;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.mViewType = 0;
            this.mTypeSearch = false;
            this.mTypeTitle = false;
            this.mTypeSegmentButton = false;
            this.mTypeTextButton = false;
            copyMarginsFromCompat(marginLayoutParams);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.mViewType = 0;
            this.mTypeSearch = false;
            this.mTypeTitle = false;
            this.mTypeSegmentButton = false;
            this.mTypeTextButton = false;
        }
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LayoutParams) {
            return new LayoutParams((LayoutParams) layoutParams);
        }
        if (layoutParams instanceof ActionBar.LayoutParams) {
            return new LayoutParams((ActionBar.LayoutParams) layoutParams);
        }
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams) layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    public void setSearchView(View view, LayoutParams layoutParams) {
        if (view == null) {
            this.mHasSearchViewFlag = false;
            return;
        }
        this.mHasSearchViewFlag = true;
        LayoutParams layoutParams2 = new LayoutParams(layoutParams);
        layoutParams2.mTypeSearch = true;
        layoutParams2.mViewType = 0;
        addView(view, 0, layoutParams2);
    }

    private void setSegmentButtons(View view, LayoutParams layoutParams) {
        this.mSegmentButton = view;
        LayoutParams layoutParams2 = new LayoutParams(layoutParams);
        layoutParams2.mViewType = 0;
        layoutParams2.mTypeSegmentButton = true;
        layoutParams2.gravity = 1;
        addView(view, 0, layoutParams2);
    }

    private void setTextButton(View view, LayoutParams layoutParams) {
        this.mTextButton = view;
        LayoutParams layoutParams2 = new LayoutParams(layoutParams);
        layoutParams2.mViewType = 0;
        layoutParams2.mTypeTextButton = true;
        layoutParams2.gravity = GravityCompat.END;
        if (ToolbarMaterialHelper.isMaterialEffectOsSupported()) {
            ((ViewGroup.MarginLayoutParams) layoutParams2).width = -2;
            ((ViewGroup.MarginLayoutParams) layoutParams2).height = -2;
            if (view instanceof TextView) {
                ((TextView) this.mTextButton).setGravity(17);
            }
            ToolbarMaterialHelper.applyTextButtonSize(view);
            this.mTextButtonEffectDelegate = ToolbarMaterialEffectDelegate.forToolbarButton(view);
            if (this.mUseMaterialEffectAttr != MaterialEffectState.FOLLOW_SYSTEM.getValue()) {
                this.mTextButtonEffectDelegate.setUseMaterialEffectAttr(this.mUseMaterialEffectAttr);
            }
            if (this.mIsDrawableMaterialEffect) {
                this.mTextButtonEffectDelegate.setIsDrawableMaterialEffect(true);
            }
            int i3 = this.mCustomNonBlurFallbackColor;
            if (i3 != 0) {
                this.mTextButtonEffectDelegate.setCustomNonBlurFallbackColor(i3);
            }
            this.mTextButtonEffectDelegate.setMaterialEffectType(this.mMaterialType);
        } else {
            View view2 = this.mTextButton;
            if (view2 instanceof TextView) {
                COUITextViewCompatUtil.setPressRippleDrawable((TextView) view2);
            }
        }
        addView(view, layoutParams2);
    }
}