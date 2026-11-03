package com.coui.appcompat.bottomnavigation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.coui.appcompat.animation.COUIInEaseInterpolator;
import com.coui.appcompat.animation.COUILinearInterpolator;
import com.coui.appcompat.animation.COUIOutEaseInterpolator;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.coui.appcompat.material.bottomnavigation.BottomNavigationView;
import com.coui.appcompat.material.navigation.NavigationBarMenuView;
import com.coui.appcompat.material.navigation.NavigationBarView;
import com.coui.appcompat.poplist.PopupMenuConfigRule;
import com.coui.appcompat.textutil.COUIChangeTextUtil;
import com.google.android.material.internal.ViewUtils;
import com.support.appcompat.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUINavigationView extends BottomNavigationView implements PopupMenuConfigRule {
    private static final long DEFAULT_ENTER_TRANSLATE_ANIMATION_DURATION = 350;
    private static final long DEFAULT_HIDE_TRANSLATE_ANIMATION_DURATION = 200;
    public static final int DEFAULT_ITEM_LAYOUT_TYPE = 0;
    private static final long DEFAULT_SHOW_TRANSLATE_ANIMATION_DURATION = 250;
    private static final float END_ALPHA = 1.0f;
    public static final int ENTER_ANIMATION_TYPE = 1;
    public static final int EXIT_ANIMATION_TYPE = 2;
    public static final int NAVIGATION_TYPE_ENLARGE = 2;
    public static final int NAVIGATION_TYPE_TAB = 1;
    public static final int NAVIGATION_TYPE_TOOL = 0;
    private static final float START_ALPHA = 0.0f;
    public static final int VERTICAL_ITEM_LAYOUT_TYPE = 1;
    private COUINavigationMenuView mCOUINavigationMenuView;
    private OnConfigChangedListener mConfigChangedListener;
    private Rect mDisplayFrame;
    private Rect mDisplayFrameOutsets;
    private View mDividerView;
    private OnNavigationEnterExitListener mEndListener;
    private FrameLayout mEnlargeBgView;
    private int mEnlargeNavigationViewBgId;
    private OnEnlargeSelectListener mEnlargeSelectListener;
    private Animator mEnterAnimation;
    private ValueAnimator mEnterTranslateAnimation;
    private Animator mExitAnimation;
    private ValueAnimator mHideTranslateAnimation;
    private boolean mIsCheckEnlarge;
    private boolean mIsEnlarge;
    private int mLayoutType;
    private int mNavigationType;
    private int mNextMenuResId;
    private boolean mPopupRuleEnable;
    private OnNavigationShowHideListener mShowHideListener;
    private ValueAnimator mShowTranslateAnimation;
    private int mTextSizeResId;
    private Rect mWindowFrame;

    /* JADX INFO: renamed from: com.coui.appcompat.bottomnavigation.COUINavigationView$1, reason: invalid class name */
    public class AnonymousClass1 implements ViewUtils.OnApplyWindowInsetsListener {
        public AnonymousClass1() {
        }

        @Override // com.google.android.material.internal.ViewUtils.OnApplyWindowInsetsListener
        public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat, ViewUtils.RelativePadding relativePadding) {
            boolean z2 = ViewCompat.getLayoutDirection(view) == 1;
            int i3 = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars()).left;
            int i4 = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars()).right;
            relativePadding.start += z2 ? i4 : i3;
            int i5 = relativePadding.end;
            if (!z2) {
                i3 = i4;
            }
            relativePadding.end = i5 + i3;
            relativePadding.applyToView(view);
            return windowInsetsCompat;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomnavigation.COUINavigationView$2, reason: invalid class name */
    public class AnonymousClass2 implements NavigationBarView.OnItemSelectedListener {
        public AnonymousClass2() {
        }

        @Override // com.coui.appcompat.material.navigation.NavigationBarView.OnItemSelectedListener
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            COUINavigationView cOUINavigationView = COUINavigationView.this;
            COUINavigationView.access$002(cOUINavigationView, COUINavigationView.access$100(cOUINavigationView).getEnlargeId() == menuItem.getItemId());
            COUINavigationView.access$200(COUINavigationView.this).onEnlargeSelect(COUINavigationView.access$000(COUINavigationView.this), menuItem);
            COUINavigationView.access$300(COUINavigationView.this);
            return true;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomnavigation.COUINavigationView$3, reason: invalid class name */
    public class AnonymousClass3 implements Animator.AnimatorListener {
        public AnonymousClass3() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUINavigationView.access$500(COUINavigationView.this) != null) {
                COUINavigationView.access$500(COUINavigationView.this).onAnimationEnterEnd();
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (COUINavigationView.access$400(COUINavigationView.this) != 0) {
                COUINavigationView cOUINavigationView = COUINavigationView.this;
                cOUINavigationView.inflateMenu(COUINavigationView.access$400(cOUINavigationView));
                COUINavigationView.access$402(COUINavigationView.this, 0);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomnavigation.COUINavigationView$4, reason: invalid class name */
    public class AnonymousClass4 implements Animator.AnimatorListener {
        final /* synthetic */ AnimatorSet val$animatorSet;

        public AnonymousClass4(AnimatorSet animatorSet) {
            this.val$animatorSet = animatorSet;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUINavigationView.access$400(COUINavigationView.this) != 0) {
                COUINavigationView.access$100(COUINavigationView.this).setTranslationY(-COUINavigationView.this.getHeight());
                this.val$animatorSet.start();
            }
            if (COUINavigationView.access$500(COUINavigationView.this) != null) {
                COUINavigationView.access$500(COUINavigationView.this).onAnimationExitEnd();
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomnavigation.COUINavigationView$5, reason: invalid class name */
    public class AnonymousClass5 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass5() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUINavigationView.access$100(COUINavigationView.this).setTranslationY(((Float) valueAnimator.getAnimatedValue()).floatValue() * COUINavigationView.this.getMeasuredHeight());
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomnavigation.COUINavigationView$6, reason: invalid class name */
    public class AnonymousClass6 implements Animator.AnimatorListener {
        public AnonymousClass6() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUINavigationView.access$600(COUINavigationView.this) != null) {
                COUINavigationView.access$600(COUINavigationView.this).onAnimationHideEnd();
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (COUINavigationView.access$600(COUINavigationView.this) != null) {
                COUINavigationView.access$600(COUINavigationView.this).onAnimationHideStart();
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomnavigation.COUINavigationView$7, reason: invalid class name */
    public class AnonymousClass7 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass7() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) COUINavigationView.this.getLayoutParams();
            marginLayoutParams.bottomMargin = (int) (((Float) valueAnimator.getAnimatedValue()).floatValue() * COUINavigationView.this.getMeasuredHeight() * (-1.0f));
            COUINavigationView.this.setLayoutParams(marginLayoutParams);
            if (COUINavigationView.access$600(COUINavigationView.this) != null) {
                COUINavigationView.access$600(COUINavigationView.this).onAnimationHideUpdate(marginLayoutParams.bottomMargin);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomnavigation.COUINavigationView$8, reason: invalid class name */
    public class AnonymousClass8 implements Animator.AnimatorListener {
        public AnonymousClass8() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUINavigationView.access$600(COUINavigationView.this) != null) {
                COUINavigationView.access$600(COUINavigationView.this).onAnimationShowEnd();
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (COUINavigationView.access$600(COUINavigationView.this) != null) {
                COUINavigationView.access$600(COUINavigationView.this).onAnimationShowStart();
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomnavigation.COUINavigationView$9, reason: invalid class name */
    public class AnonymousClass9 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass9() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) COUINavigationView.this.getLayoutParams();
            marginLayoutParams.bottomMargin = (int) (((Float) valueAnimator.getAnimatedValue()).floatValue() * COUINavigationView.this.getMeasuredHeight() * (-1.0f));
            COUINavigationView.this.setLayoutParams(marginLayoutParams);
            if (COUINavigationView.access$600(COUINavigationView.this) != null) {
                COUINavigationView.access$600(COUINavigationView.this).onAnimationShowUpdate(marginLayoutParams.bottomMargin);
            }
        }
    }

    public interface OnConfigChangedListener {
        void onConfigChanged(Configuration configuration);
    }

    public interface OnEnlargeSelectListener {
        void onEnlargeSelect(boolean z2, MenuItem menuItem);
    }

    public interface OnNavigationEnterExitListener {
        void onAnimationEnterEnd();

        void onAnimationExitEnd();
    }

    public interface OnNavigationShowHideListener {
        void onAnimationHideEnd();

        void onAnimationHideStart();

        void onAnimationHideUpdate(int i3);

        void onAnimationShowEnd();

        void onAnimationShowStart();

        void onAnimationShowUpdate(int i3);
    }

    public COUINavigationView(Context context) {
        this(context, null);
    }

    public static /* synthetic */ boolean access$000(COUINavigationView cOUINavigationView) {
        return cOUINavigationView.mIsCheckEnlarge;
    }

    public static /* synthetic */ boolean access$002(COUINavigationView cOUINavigationView, boolean z2) {
        cOUINavigationView.mIsCheckEnlarge = z2;
        return z2;
    }

    public static /* synthetic */ COUINavigationMenuView access$100(COUINavigationView cOUINavigationView) {
        return cOUINavigationView.mCOUINavigationMenuView;
    }

    public static /* synthetic */ OnEnlargeSelectListener access$200(COUINavigationView cOUINavigationView) {
        return cOUINavigationView.mEnlargeSelectListener;
    }

    public static /* synthetic */ void access$300(COUINavigationView cOUINavigationView) {
        cOUINavigationView.checkEnlargeState();
    }

    public static /* synthetic */ int access$400(COUINavigationView cOUINavigationView) {
        return cOUINavigationView.mNextMenuResId;
    }

    public static /* synthetic */ int access$402(COUINavigationView cOUINavigationView, int i3) {
        cOUINavigationView.mNextMenuResId = i3;
        return i3;
    }

    public static /* synthetic */ OnNavigationEnterExitListener access$500(COUINavigationView cOUINavigationView) {
        return cOUINavigationView.mEndListener;
    }

    public static /* synthetic */ OnNavigationShowHideListener access$600(COUINavigationView cOUINavigationView) {
        return cOUINavigationView.mShowHideListener;
    }

    private void addCompatibilityTopDivider(Context context) {
        View view = new View(context);
        this.mDividerView = view;
        COUIDarkModeUtil.setForceDarkAllow(view, false);
        this.mDividerView.setBackgroundColor(COUIContextUtil.getAttrColor(context, R.attr.couiColorDivider));
        this.mDividerView.setLayoutParams(new FrameLayout.LayoutParams(-1, getResources().getDimensionPixelSize(com.support.bottomnavigation.R.dimen.coui_navigation_shadow_height)));
        if (this.mIsEnlarge) {
            addView(this.mDividerView, 0);
        } else {
            addView(this.mDividerView);
            this.mCOUINavigationMenuView.setTop(0);
        }
    }

    private void addEnlargeBackgroundView() {
        this.mEnlargeBgView = new FrameLayout(getContext());
        this.mEnlargeBgView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        addView(this.mEnlargeBgView, 0);
        ViewCompat.setBackground(this.mEnlargeBgView, new ColorDrawable(ContextCompat.getColor(getContext(), com.support.bottomnavigation.R.color.coui_navigation_enlarge_default_bg)));
    }

    private void adjustMenuItemTextSize(Context context) {
        int dimensionPixelOffset = context.getResources().getDimensionPixelOffset(com.support.bottomnavigation.R.dimen.coui_navigation_item_text_size);
        if (this.mTextSizeResId != 0) {
            dimensionPixelOffset = getContext().getResources().getDimensionPixelOffset(this.mTextSizeResId);
        } else if (this.mLayoutType == 1) {
            dimensionPixelOffset = context.getResources().getDimensionPixelOffset(com.support.bottomnavigation.R.dimen.coui_navigation_item_small_text_size);
        }
        this.mCOUINavigationMenuView.setTextSize(dimensionPixelOffset);
    }

    private void applyWindowInsets() {
        ViewUtils.doOnApplyWindowInsets(this, new AnonymousClass1());
    }

    private void checkEnlargeState() {
        if (this.mEnlargeNavigationViewBgId == 0) {
            return;
        }
        if (this.mIsCheckEnlarge) {
            setItemForEnlargeColor();
            this.mEnlargeBgView.setBackgroundResource(this.mEnlargeNavigationViewBgId);
        } else {
            this.mCOUINavigationMenuView.clearColorFilter();
            this.mCOUINavigationMenuView.setItemTextColor(getItemTextColor());
            this.mEnlargeBgView.setBackgroundColor(getResources().getColor(com.support.bottomnavigation.R.color.coui_navigation_enlarge_default_bg));
        }
    }

    private void initAnimation() {
        AnimatorSet animatorSet = new AnimatorSet();
        Property property = View.ALPHA;
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(this.mCOUINavigationMenuView, (Property<COUINavigationMenuView, Float>) property, 0.0f, 1.0f);
        this.mEnterAnimation = objectAnimatorOfFloat;
        objectAnimatorOfFloat.setInterpolator(new COUIInEaseInterpolator());
        this.mEnterAnimation.setDuration(100L);
        this.mEnterAnimation.addListener(new AnonymousClass3());
        ObjectAnimator objectAnimatorOfFloat2 = ObjectAnimator.ofFloat(this.mCOUINavigationMenuView, (Property<COUINavigationMenuView, Float>) property, 1.0f, 0.0f);
        this.mExitAnimation = objectAnimatorOfFloat2;
        objectAnimatorOfFloat2.setInterpolator(new COUILinearInterpolator());
        this.mExitAnimation.setDuration(100L);
        this.mExitAnimation.addListener(new AnonymousClass4(animatorSet));
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
        this.mEnterTranslateAnimation = valueAnimatorOfFloat;
        valueAnimatorOfFloat.setInterpolator(new COUIInEaseInterpolator());
        this.mEnterTranslateAnimation.setDuration(DEFAULT_ENTER_TRANSLATE_ANIMATION_DURATION);
        this.mEnterTranslateAnimation.addUpdateListener(new AnonymousClass5());
        animatorSet.playTogether(this.mEnterAnimation, this.mEnterTranslateAnimation);
        ValueAnimator valueAnimatorOfFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mHideTranslateAnimation = valueAnimatorOfFloat2;
        valueAnimatorOfFloat2.setInterpolator(new COUIOutEaseInterpolator());
        this.mHideTranslateAnimation.setDuration(200L);
        this.mHideTranslateAnimation.addListener(new AnonymousClass6());
        this.mHideTranslateAnimation.addUpdateListener(new AnonymousClass7());
        ValueAnimator valueAnimatorOfFloat3 = ValueAnimator.ofFloat(1.0f, 0.0f);
        this.mShowTranslateAnimation = valueAnimatorOfFloat3;
        valueAnimatorOfFloat3.setInterpolator(new COUIInEaseInterpolator());
        this.mShowTranslateAnimation.setDuration(250L);
        this.mShowTranslateAnimation.addListener(new AnonymousClass8());
        this.mShowTranslateAnimation.addUpdateListener(new AnonymousClass9());
    }

    private static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException unused) {
            return false;
        }
    }

    private void setItemForEnlargeColor() {
        this.mCOUINavigationMenuView.setItemForEnlargeColor();
    }

    public void adjustNavigationItem() {
        if (COUIResponsiveUtils.isSmallScreen(getContext(), getMeasuredWidth())) {
            setItemLayoutType(1);
            this.mCOUINavigationMenuView.setTextSize(getContext().getResources().getDimensionPixelOffset(com.support.bottomnavigation.R.dimen.coui_navigation_item_text_size));
        } else {
            setItemLayoutType(0);
            this.mCOUINavigationMenuView.setTextSize(getContext().getResources().getDimensionPixelOffset(com.support.bottomnavigation.R.dimen.coui_navigation_item_large_text_size));
        }
    }

    @Override // com.coui.appcompat.material.bottomnavigation.BottomNavigationView, com.coui.appcompat.material.navigation.NavigationBarView
    public NavigationBarMenuView createNavigationBarMenuView(Context context) {
        return new COUINavigationMenuView(new ContextThemeWrapper(context, COUIContextUtil.getAttrId(context, com.support.bottomnavigation.R.attr.COUINavigationViewItemStyle, com.support.bottomnavigation.R.style.COUINavigationView_NoAnimation)));
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
        return this.mWindowFrame.height() <= getContext().getResources().getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_min_window_height_to_apply_vertical_barrier) ? -1 : 3;
    }

    public COUINavigationMenuView getCOUINavigationMenuView() {
        return this.mCOUINavigationMenuView;
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public Rect getDisplayFrame() {
        if (this.mDisplayFrame == null) {
            this.mDisplayFrame = new Rect();
        }
        getGlobalVisibleRect(this.mDisplayFrame);
        return this.mDisplayFrame;
    }

    public View getDividerView() {
        return this.mDividerView;
    }

    public FrameLayout getEnlargeBgView() {
        return this.mEnlargeBgView;
    }

    @Override // com.coui.appcompat.material.bottomnavigation.BottomNavigationView, com.coui.appcompat.material.navigation.NavigationBarView
    public int getMaxItemCount() {
        return 10;
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

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public int getType() {
        return 2;
    }

    public void hide() {
        hide(true);
    }

    @Override // com.coui.appcompat.material.navigation.NavigationBarView
    public void inflateMenu(int i3) {
        if (getMenu().size() > 0) {
            getMenu().clear();
        }
        super.inflateMenu(i3);
        if (this.mNavigationType == 0) {
            this.mCOUINavigationMenuView.setShowPressShadow(true);
        }
    }

    @Override // com.coui.appcompat.material.navigation.NavigationBarView, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mIsEnlarge) {
            addEnlargeBackgroundView();
        }
        adjustMenuItemTextSize(getContext());
        this.mCOUINavigationMenuView.setItemLayoutType(this.mLayoutType);
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        OnConfigChangedListener onConfigChangedListener = this.mConfigChangedListener;
        if (onConfigChangedListener != null) {
            onConfigChangedListener.onConfigChanged(configuration);
        }
        adjustMenuItemTextSize(getContext().createConfigurationContext(configuration));
    }

    @Override // com.coui.appcompat.material.bottomnavigation.BottomNavigationView, android.widget.FrameLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        int mode = View.MeasureSpec.getMode(i4);
        int size = View.MeasureSpec.getSize(i4);
        int dimensionPixelSize = getResources().getDimensionPixelSize(com.support.bottomnavigation.R.dimen.coui_tool_navigation_item_height);
        if (mode == Integer.MIN_VALUE) {
            i4 = View.MeasureSpec.makeMeasureSpec(dimensionPixelSize, 1073741824);
        } else if (mode == 1073741824) {
            i4 = View.MeasureSpec.makeMeasureSpec(size, 1073741824);
        }
        super.onMeasure(i3, i4);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        return true;
    }

    public void refresh() {
        TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(null, com.support.bottomnavigation.R.styleable.COUINavigationMenuView, com.support.bottomnavigation.R.attr.couiNavigationViewStyle, COUIContextUtil.isCOUIDarkTheme(getContext()) ? com.support.bottomnavigation.R.style.Widget_COUI_COUINavigationView_Dark : com.support.bottomnavigation.R.style.Widget_COUI_COUINavigationView);
        this.mCOUINavigationMenuView.setIconTintList(typedArrayObtainStyledAttributes.getColorStateList(com.support.bottomnavigation.R.styleable.COUINavigationMenuView_couiNaviIconTint));
        this.mCOUINavigationMenuView.setItemTextColor(typedArrayObtainStyledAttributes.getColorStateList(com.support.bottomnavigation.R.styleable.COUINavigationMenuView_couiNaviTextColor));
        this.mDividerView.setBackgroundColor(COUIContextUtil.getAttrColor(getContext(), R.attr.couiColorDivider));
        int resourceId = typedArrayObtainStyledAttributes.getResourceId(com.support.bottomnavigation.R.styleable.COUINavigationMenuView_couiToolNavigationViewBg, 0);
        int resourceId2 = typedArrayObtainStyledAttributes.getResourceId(com.support.bottomnavigation.R.styleable.COUINavigationMenuView_couiTabNavigationViewBg, 0);
        int i3 = this.mNavigationType;
        if (i3 == 2) {
            setBackgroundColor(0);
        } else if (i3 == 0) {
            setBackground(ContextCompat.getDrawable(getContext(), resourceId));
        } else {
            setBackground(ContextCompat.getDrawable(getContext(), resourceId2));
        }
        for (int i4 = 0; i4 < getChildCount(); i4++) {
            View childAt = getChildAt(i4);
            if (childAt instanceof COUINavigationItemView) {
                ((COUINavigationItemView) childAt).refresh();
            }
        }
        typedArrayObtainStyledAttributes.recycle();
    }

    public void setAnimationType(int i3) {
        if (i3 == 1) {
            this.mEnterAnimation.start();
        } else if (i3 == 2) {
            this.mExitAnimation.start();
        }
    }

    public void setEnlargeIndex(int i3) {
        this.mCOUINavigationMenuView.setEnlarge(this.mIsEnlarge, i3);
    }

    public void setItemLayoutType(int i3) {
        this.mLayoutType = i3;
        adjustMenuItemTextSize(getContext());
        this.mCOUINavigationMenuView.setItemLayoutType(this.mLayoutType);
    }

    @Deprecated
    public void setNeedTextAnim(boolean z2) {
    }

    public void setOnAnimatorListener(OnNavigationEnterExitListener onNavigationEnterExitListener) {
        this.mEndListener = onNavigationEnterExitListener;
    }

    public void setOnAnimatorShowHideListener(OnNavigationShowHideListener onNavigationShowHideListener) {
        this.mShowHideListener = onNavigationShowHideListener;
    }

    public void setOnConfigChangedListener(OnConfigChangedListener onConfigChangedListener) {
        this.mConfigChangedListener = onConfigChangedListener;
    }

    public void setOnEnlargeSelectListener(OnEnlargeSelectListener onEnlargeSelectListener) {
        this.mEnlargeSelectListener = onEnlargeSelectListener;
        setOnItemSelectedListener(new AnonymousClass2());
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public void setPopupMenuRuleEnabled(boolean z2) {
        this.mPopupRuleEnable = z2;
    }

    public void setTipsView(int i3, int i4, int i5) {
        if (i3 >= this.mCOUINavigationMenuView.getVisibleItems().size()) {
            return;
        }
        setTipsView(i3, String.valueOf(i4), i5);
    }

    public void setTipsViewByItemId(int i3, int i4, int i5) {
        setTipsView((COUINavigationItemView) this.mCOUINavigationMenuView.findItemView(i3), String.valueOf(i4), i5);
    }

    public void show() {
        show(true);
    }

    public void switchToMenu(int i3) {
        switchToMenu(i3, true);
    }

    public COUINavigationView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, com.support.bottomnavigation.R.attr.couiNavigationViewStyle);
    }

    @Deprecated
    public int getMaxItemCount(int i3) {
        if (COUIResponsiveUtils.isLargeScreen(getContext(), i3)) {
            return 7;
        }
        return COUIResponsiveUtils.isMediumScreen(getContext(), i3) ? 6 : 5;
    }

    public void hide(boolean z2) {
        if (z2) {
            this.mHideTranslateAnimation.start();
            return;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        marginLayoutParams.bottomMargin = getMeasuredHeight() * (-1);
        setLayoutParams(marginLayoutParams);
    }

    public void show(boolean z2) {
        if (z2) {
            this.mShowTranslateAnimation.start();
            return;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        marginLayoutParams.bottomMargin = 0;
        setLayoutParams(marginLayoutParams);
    }

    public void switchToMenu(int i3, boolean z2) {
        if (!z2) {
            inflateMenu(i3);
        } else {
            this.mNextMenuResId = i3;
            this.mExitAnimation.start();
        }
    }

    public COUINavigationView(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, com.support.bottomnavigation.R.style.Widget_COUI_COUINavigationView);
    }

    public void setTipsView(int i3, String str, int i4) {
        if (i3 >= this.mCOUINavigationMenuView.getVisibleItems().size()) {
            return;
        }
        setTipsView((COUINavigationItemView) this.mCOUINavigationMenuView.findItemView(getCOUINavigationMenuView().getVisibleItem(i3).getItemId()), str, i4);
    }

    public void setTipsViewByItemId(int i3, String str, int i4) {
        setTipsView((COUINavigationItemView) this.mCOUINavigationMenuView.findItemView(i3), str, i4);
    }

    public COUINavigationView(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mNextMenuResId = 0;
        this.mTextSizeResId = 0;
        this.mIsEnlarge = false;
        this.mIsCheckEnlarge = false;
        this.mPopupRuleEnable = true;
        this.mDisplayFrame = null;
        this.mWindowFrame = null;
        this.mDisplayFrameOutsets = null;
        TintTypedArray tintTypedArrayObtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, com.support.bottomnavigation.R.styleable.COUINavigationMenuView, i3, i4);
        this.mCOUINavigationMenuView = (COUINavigationMenuView) getMenuView();
        int i5 = com.support.bottomnavigation.R.styleable.COUINavigationMenuView_couiNaviTextColor;
        if (tintTypedArrayObtainStyledAttributes.hasValue(i5)) {
            setItemTextColor(tintTypedArrayObtainStyledAttributes.getColorStateList(i5));
        } else {
            setItemTextColor(getResources().getColorStateList(com.support.bottomnavigation.R.color.coui_bottom_tool_navigation_item_selector));
        }
        this.mCOUINavigationMenuView.setIconTintList(tintTypedArrayObtainStyledAttributes.getColorStateList(com.support.bottomnavigation.R.styleable.COUINavigationMenuView_couiNaviIconTint));
        this.mNavigationType = tintTypedArrayObtainStyledAttributes.getInt(com.support.bottomnavigation.R.styleable.COUINavigationMenuView_navigationType, 0);
        int dimensionPixelSize = getResources().getDimensionPixelSize(com.support.bottomnavigation.R.dimen.coui_navigation_item_text_size);
        int i6 = com.support.bottomnavigation.R.styleable.COUINavigationMenuView_couiNaviTextSize;
        int dimensionPixelSize2 = tintTypedArrayObtainStyledAttributes.getDimensionPixelSize(i6, dimensionPixelSize);
        this.mTextSizeResId = tintTypedArrayObtainStyledAttributes.getResourceId(i6, 0);
        this.mCOUINavigationMenuView.setTextSize((int) COUIChangeTextUtil.getSuitableFontSize(dimensionPixelSize2, getResources().getConfiguration().fontScale, 2));
        int integer = tintTypedArrayObtainStyledAttributes.getInteger(com.support.bottomnavigation.R.styleable.COUINavigationMenuView_couiNaviTipsType, -1);
        int integer2 = tintTypedArrayObtainStyledAttributes.getInteger(com.support.bottomnavigation.R.styleable.COUINavigationMenuView_couiNaviTipsNumber, 0);
        int i7 = com.support.bottomnavigation.R.styleable.COUINavigationMenuView_couiNaviMenu;
        if (tintTypedArrayObtainStyledAttributes.hasValue(i7)) {
            inflateMenu(tintTypedArrayObtainStyledAttributes.getResourceId(i7, 0));
            setTipsView(0, integer2, integer);
        }
        int resourceId = tintTypedArrayObtainStyledAttributes.getResourceId(com.support.bottomnavigation.R.styleable.COUINavigationMenuView_couiToolNavigationViewBg, 0);
        int resourceId2 = tintTypedArrayObtainStyledAttributes.getResourceId(com.support.bottomnavigation.R.styleable.COUINavigationMenuView_couiTabNavigationViewBg, 0);
        this.mEnlargeNavigationViewBgId = tintTypedArrayObtainStyledAttributes.getResourceId(com.support.bottomnavigation.R.styleable.COUINavigationMenuView_couiEnlargeNavigationViewBg, 0);
        int i8 = this.mNavigationType;
        if (i8 == 2) {
            this.mIsEnlarge = true;
            setBackgroundColor(0);
            this.mCOUINavigationMenuView.initEnlargeParams();
        } else if (i8 == 0) {
            setBackgroundResource(resourceId);
        } else {
            setBackgroundResource(resourceId2);
        }
        this.mCOUINavigationMenuView.setItemNavigationType(this.mNavigationType);
        int i9 = com.support.bottomnavigation.R.styleable.COUINavigationMenuView_couiItemLayoutType;
        if (tintTypedArrayObtainStyledAttributes.hasValue(i9)) {
            setItemLayoutType(tintTypedArrayObtainStyledAttributes.getInteger(i9, 0));
        }
        setLabelVisibilityMode(1);
        setClipChildren(false);
        setClipToPadding(false);
        addCompatibilityTopDivider(context);
        setElevation(0.0f);
        tintTypedArrayObtainStyledAttributes.recycle();
        initAnimation();
        applyWindowInsets();
        COUIDarkModeUtil.setForceDarkAllow(this, false);
    }

    public int getMaxItemCount(int i3, int i4) {
        if (COUIResponsiveUtils.isLargeScreen(getContext(), i3, i4)) {
            return 7;
        }
        return COUIResponsiveUtils.isMediumScreen(getContext(), i3, i4) ? 6 : 5;
    }

    private void setTipsView(COUINavigationItemView cOUINavigationItemView, String str, int i3) {
        if (cOUINavigationItemView != null) {
            if (i3 == 1) {
                cOUINavigationItemView.getCOUIHintRedDot().setVisibility(0);
                cOUINavigationItemView.getCOUIHintRedDot().setPointMode(1);
            } else {
                if (i3 != 2) {
                    cOUINavigationItemView.getCOUIHintRedDot().setVisibility(4);
                    return;
                }
                cOUINavigationItemView.getCOUIHintRedDot().setVisibility(0);
                if (isNumeric(str)) {
                    cOUINavigationItemView.getCOUIHintRedDot().setPointMode(2);
                    cOUINavigationItemView.getCOUIHintRedDot().setPointNumber(Integer.parseInt(str));
                } else {
                    cOUINavigationItemView.getCOUIHintRedDot().setPointMode(3);
                    cOUINavigationItemView.getCOUIHintRedDot().setPointText(str);
                }
            }
        }
    }
}