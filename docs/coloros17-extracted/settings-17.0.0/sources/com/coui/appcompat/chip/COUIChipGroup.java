package com.coui.appcompat.chip;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.edittext.COUIEditText;
import com.coui.appcompat.log.COUILog;
import com.support.chip.R;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: classes7.dex */
public class COUIChipGroup extends ViewGroup {
    private static final boolean DEBUG;
    private static final float DEFAULT_CHIP_GROUP_HEIGHT_CHANG_ANIMATION_BOUNCE = 0.0f;
    private static final float DEFAULT_CHIP_GROUP_HEIGHT_CHANG_ANIMATION_RESPONSE = 0.35f;
    private static final int DEFAULT_MINIMUM_COLLAPSED_MAX_ROWS = 3;
    private static final int DEF_STYLE_RES;
    private static final int INVALID_COLOR = Integer.MAX_VALUE;
    private static final int INVALID_TINT = Integer.MAX_VALUE;
    private static final String TAG = "COUIChipGroup";
    private int mAnimatingHeight;
    private int mAnimatingWidth;
    private final COUICheckableGroup<COUIChip> mCheckableGroup;
    private ChipGroupLayoutAnimationCallback mChipGroupLayoutAnimationCallback;
    private int mChipSpacingHorizontal;
    private int mChipSpacingVertical;
    private final ArrayList<COUIChip> mChips;
    private final ArrayList<COUIChip> mChipsPendingToHide;
    private final ArrayList<COUIChip> mChipsPendingToShow;
    private int mCollapsableButtonBackgroundColor;
    private int mCollapsableButtonIconTint;
    private final int mCollapsableButtonSize;
    private COUIChip mCollapseChip;
    private int mCollapsedMaxRows;
    private int mDefaultCheckedId;
    private final int[] mDrawableSize;
    private COUIEditText mEdittext;
    private int mEdittextHeight;
    private boolean mEdittextNewLine;
    private boolean mEnableChipsAnimations;
    private COUIChip mExpandChip;
    private int mFinalHeight;
    private int mFinalItemIndexWhenCollapsed;
    private int mFinalWidth;
    private COUISpringAnimation mGroupHeightChangeAnimation;
    private COUISpringAnimation mGroupWidthChangeAnimation;
    private final HashSet<COUIChip> mHiddenChips;
    private boolean mIsCollapsable;
    private boolean mIsCollapseChipAttached;
    private boolean mIsCollapsed;
    private boolean mIsExpandChipAttached;
    private Configuration mOldConfiguration;
    private OnCheckedStateChangeListener mOnCheckedStateChangeListener;
    private OnChipGroupCollapsableButtonClickListener mOnChipGroupCollapsableButtonClickListener;
    private final PassThroughHierarchyChangeListener mPassThroughListener;
    private int mRowCount;
    private boolean mShowEdittext;
    private boolean mSingleLine;
    private boolean mSkipAnimationAfterConfigurationChanged;
    private boolean mSuppressHeightChange;
    private boolean mSuppressWidthChange;
    private ArrayList<View> mTransitioningViews;

    /* JADX INFO: renamed from: com.coui.appcompat.chip.COUIChipGroup$1, reason: invalid class name */
    public class AnonymousClass1 extends FloatPropertyCompat<COUIChipGroup> {
        public AnonymousClass1(String str) {
            super(str);
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public /* bridge */ /* synthetic */ float getValue(COUIChipGroup cOUIChipGroup) {
            return getValue2(cOUIChipGroup);
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public /* bridge */ /* synthetic */ void setValue(COUIChipGroup cOUIChipGroup, float f3) {
            setValue2(cOUIChipGroup, f3);
        }

        /* JADX INFO: renamed from: getValue, reason: avoid collision after fix types in other method */
        public float getValue2(COUIChipGroup cOUIChipGroup) {
            return COUIChipGroup.access$100(cOUIChipGroup);
        }

        /* JADX INFO: renamed from: setValue, reason: avoid collision after fix types in other method */
        public void setValue2(COUIChipGroup cOUIChipGroup, float f3) {
            COUIChipGroup.access$200(cOUIChipGroup, f3);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.chip.COUIChipGroup$2, reason: invalid class name */
    public class AnonymousClass2 extends FloatPropertyCompat<COUIChipGroup> {
        public AnonymousClass2(String str) {
            super(str);
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public /* bridge */ /* synthetic */ float getValue(COUIChipGroup cOUIChipGroup) {
            return getValue2(cOUIChipGroup);
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public /* bridge */ /* synthetic */ void setValue(COUIChipGroup cOUIChipGroup, float f3) {
            setValue2(cOUIChipGroup, f3);
        }

        /* JADX INFO: renamed from: getValue, reason: avoid collision after fix types in other method */
        public float getValue2(COUIChipGroup cOUIChipGroup) {
            return COUIChipGroup.access$300(cOUIChipGroup);
        }

        /* JADX INFO: renamed from: setValue, reason: avoid collision after fix types in other method */
        public void setValue2(COUIChipGroup cOUIChipGroup, float f3) {
            COUIChipGroup.access$400(cOUIChipGroup, f3);
        }
    }

    public interface ChipGroupLayoutAnimationCallback {
        default void onAnimatingHeightUpdate(int i3, int i4, int i5) {
        }

        default void onAnimatingWidthUpdate(int i3, int i4, int i5) {
        }
    }

    /* JADX INFO: loaded from: classes.dex */
    public interface IChipGroupAnimator {
        public static final float DEFAULT_CHIP_ALPHA_SPRING_ANIMATION_RESPONSE = 0.2f;
        public static final float DEFAULT_CHIP_SCALE_START = 0.8f;
        public static final float DEFAULT_CHIP_SPRING_ANIMATION_BOUNCE = 0.0f;
        public static final float DEFAULT_CHIP_SPRING_ANIMATION_RANGE = 10000.0f;
        public static final float DEFAULT_CHIP_SPRING_ANIMATION_RESPONSE = 0.3f;

        void bindController(COUIChipGroup cOUIChipGroup);

        void forceFinishAllAnimation();

        void getDrawingBounds(RectF rectF);

        boolean isChipAnimationRunning();

        void resetChipGroupAnimations();

        void unbindController();

        void updateAttachState(boolean z2, boolean z3);

        void updateChipRealBounds(int i3, int i4, int i5, int i6, boolean z2);
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(int i3, int i4) {
            super(i3, i4);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }
    }

    public interface OnCheckedStateChangeListener {
        void onCheckedChanged(COUIChipGroup cOUIChipGroup, List<Integer> list);
    }

    public interface OnChipGroupCollapsableButtonClickListener {
        default void onCollapseButtonClicked() {
        }

        default void onExpandButtonClicked() {
        }
    }

    public class PassThroughHierarchyChangeListener implements ViewGroup.OnHierarchyChangeListener {
        private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;

        private PassThroughHierarchyChangeListener() {
        }

        public static /* synthetic */ ViewGroup.OnHierarchyChangeListener access$502(PassThroughHierarchyChangeListener passThroughHierarchyChangeListener, ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener) {
            passThroughHierarchyChangeListener.mOnHierarchyChangeListener = onHierarchyChangeListener;
            return onHierarchyChangeListener;
        }

        @Override // android.view.ViewGroup.OnHierarchyChangeListener
        public void onChildViewAdded(View view, View view2) {
            COUIChipGroup cOUIChipGroup = COUIChipGroup.this;
            if (view == cOUIChipGroup && (view2 instanceof COUIChip) && view2 != COUIChipGroup.access$600(cOUIChipGroup) && view2 != COUIChipGroup.access$700(COUIChipGroup.this)) {
                if (view2.getId() == -1) {
                    view2.setId(ViewCompat.generateViewId());
                }
                COUIChipGroup.access$800(COUIChipGroup.this).addCheckable((COUIChip) view2);
            }
            ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener = this.mOnHierarchyChangeListener;
            if (onHierarchyChangeListener != null) {
                onHierarchyChangeListener.onChildViewAdded(view, view2);
            }
        }

        @Override // android.view.ViewGroup.OnHierarchyChangeListener
        public void onChildViewRemoved(View view, View view2) {
            COUIChipGroup cOUIChipGroup = COUIChipGroup.this;
            if (view == cOUIChipGroup && (view2 instanceof COUIChip) && view2 != COUIChipGroup.access$600(cOUIChipGroup) && view2 != COUIChipGroup.access$700(COUIChipGroup.this)) {
                COUIChipGroup.access$800(COUIChipGroup.this).removeCheckable((COUIChip) view2);
            }
            ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener = this.mOnHierarchyChangeListener;
            if (onHierarchyChangeListener != null) {
                onHierarchyChangeListener.onChildViewRemoved(view, view2);
            }
        }

        public /* synthetic */ PassThroughHierarchyChangeListener(COUIChipGroup cOUIChipGroup, AnonymousClass1 anonymousClass1) {
            this();
        }
    }

    static {
        DEBUG = COUILog.LOG_DEBUG || COUILog.isLoggable(TAG, 3);
        DEF_STYLE_RES = R.style.Widget_COUI_COUIChipGroup;
    }

    public COUIChipGroup(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void a(COUIChipGroup cOUIChipGroup, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        if (cOUIChipGroup.getAnimatingWidth() != cOUIChipGroup.getWidth()) {
            cOUIChipGroup.mSuppressWidthChange = true;
            ChipGroupLayoutAnimationCallback chipGroupLayoutAnimationCallback = cOUIChipGroup.mChipGroupLayoutAnimationCallback;
            if (chipGroupLayoutAnimationCallback != null) {
                chipGroupLayoutAnimationCallback.onAnimatingWidthUpdate(cOUIChipGroup.mAnimatingWidth, cOUIChipGroup.mFinalWidth, 0);
            }
            cOUIChipGroup.requestLayout();
        }
    }

    public static /* synthetic */ float access$100(COUIChipGroup cOUIChipGroup) {
        return cOUIChipGroup.getAnimatingHeight();
    }

    public static /* synthetic */ void access$200(COUIChipGroup cOUIChipGroup, float f3) {
        cOUIChipGroup.setAnimatingHeight(f3);
    }

    public static /* synthetic */ float access$300(COUIChipGroup cOUIChipGroup) {
        return cOUIChipGroup.getAnimatingWidth();
    }

    public static /* synthetic */ void access$400(COUIChipGroup cOUIChipGroup, float f3) {
        cOUIChipGroup.setAnimatingWidth(f3);
    }

    public static /* synthetic */ COUIChip access$600(COUIChipGroup cOUIChipGroup) {
        return cOUIChipGroup.mCollapseChip;
    }

    public static /* synthetic */ COUIChip access$700(COUIChipGroup cOUIChipGroup) {
        return cOUIChipGroup.mExpandChip;
    }

    public static /* synthetic */ COUICheckableGroup access$800(COUIChipGroup cOUIChipGroup) {
        return cOUIChipGroup.mCheckableGroup;
    }

    public static /* synthetic */ void b(COUIChipGroup cOUIChipGroup, Set set) {
        OnCheckedStateChangeListener onCheckedStateChangeListener = cOUIChipGroup.mOnCheckedStateChangeListener;
        if (onCheckedStateChangeListener != null) {
            onCheckedStateChangeListener.onCheckedChanged(cOUIChipGroup, cOUIChipGroup.mCheckableGroup.getCheckedIdsSortedByChildOrder(cOUIChipGroup));
        }
    }

    public static /* synthetic */ void c(COUIChipGroup cOUIChipGroup, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        if (cOUIChipGroup.getAnimatingHeight() != cOUIChipGroup.getHeight()) {
            cOUIChipGroup.mSuppressHeightChange = true;
            ChipGroupLayoutAnimationCallback chipGroupLayoutAnimationCallback = cOUIChipGroup.mChipGroupLayoutAnimationCallback;
            if (chipGroupLayoutAnimationCallback != null) {
                chipGroupLayoutAnimationCallback.onAnimatingHeightUpdate(cOUIChipGroup.mAnimatingHeight, cOUIChipGroup.mFinalHeight, 0);
            }
            cOUIChipGroup.requestLayout();
        }
    }

    private void collapse() {
        if (this.mIsCollapsable && !this.mIsCollapsed) {
            performCollapseButtonClick();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("ChipGroup can not collapse: ");
        sb.append(this.mIsCollapsable ? "Already collapse!" : "Not collapsable!");
        COUILog.w(TAG, sb.toString());
    }

    private COUIChip createCollapsableChip(boolean z2) {
        int i3;
        COUIChip cOUIChip = new COUIChip(getContext(), null, R.attr.couiChipStyle, R.style.Widget_COUI_Chip_Suggestion_Collapsable);
        cOUIChip.setId(z2 ? R.id.coui_chip_group_collapse_button : R.id.coui_chip_group_expand_button);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), z2 ? R.drawable.ic_chip_collapse : R.drawable.ic_chip_expand, getContext().getTheme());
        if (drawable != null && (i3 = this.mCollapsableButtonIconTint) != Integer.MAX_VALUE) {
            drawable.setColorFilter(i3, PorterDuff.Mode.SRC_IN);
        }
        int i4 = this.mCollapsableButtonBackgroundColor;
        if (i4 != Integer.MAX_VALUE) {
            cOUIChip.setUncheckedBackgroundColor(i4);
        }
        cOUIChip.setChipIcon(drawable);
        cOUIChip.setChipIconVisible(true);
        cOUIChip.bindController(this);
        cOUIChip.setOnClickListener(z2 ? new h(this) : new i(this));
        return cOUIChip;
    }

    public static /* synthetic */ void d(COUIChipGroup cOUIChipGroup, View view) {
        cOUIChipGroup.performExpandButtonClick();
    }

    public static /* synthetic */ void e(COUIChipGroup cOUIChipGroup, View view) {
        cOUIChipGroup.performCollapseButtonClick();
    }

    private void endChipTransition(COUIChip cOUIChip) {
        if (this.mTransitioningViews == null) {
            this.mTransitioningViews = new ArrayList<>();
        }
        this.mTransitioningViews.remove(cOUIChip);
        endViewTransition(cOUIChip);
    }

    private void expand() {
        if (this.mIsCollapsable && this.mIsCollapsed) {
            performExpandButtonClick();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("ChipGroup can not expand: ");
        sb.append(this.mIsCollapsable ? "Already expanded!" : "Not collapsable!");
        COUILog.w(TAG, sb.toString());
    }

    private float getAnimatingHeight() {
        return this.mAnimatingHeight;
    }

    private float getAnimatingWidth() {
        return this.mAnimatingWidth;
    }

    private int getChildMeasuredWidth(View view) {
        if (!(view instanceof COUIChip)) {
            return view.getMeasuredWidth();
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null && layoutParams.width >= 0) {
            return view.getMeasuredWidth();
        }
        COUIChipDrawable chipDrawable = ((COUIChip) view).getChipDrawable();
        if (chipDrawable != null) {
            chipDrawable.getDeterminateChipSize(this.mDrawableSize);
        }
        return this.mDrawableSize[0];
    }

    private static int getMeasuredDimension(int i3, int i4, int i5) {
        if (i4 != Integer.MIN_VALUE) {
            return i4 != 1073741824 ? i5 : i3;
        }
        return Math.min(i5, i3);
    }

    private int getRtlCoefficient() {
        return isRtl() ? -1 : 1;
    }

    private boolean hideChip(COUIChip cOUIChip) {
        if ((cOUIChip != this.mCollapseChip || !this.mIsCollapseChipAttached) && ((cOUIChip != this.mExpandChip || !this.mIsExpandChipAttached) && !this.mHiddenChips.add(cOUIChip))) {
            return false;
        }
        startChipTransition(cOUIChip);
        removeViewInLayout(cOUIChip);
        return true;
    }

    private int indexInChildren(int i3) {
        if (!this.mIsCollapsable || !this.mIsCollapsed || this.mHiddenChips.isEmpty()) {
            return i3;
        }
        int i4 = 0;
        for (int i5 = 0; i5 < getChildCount(); i5++) {
            while (i4 < this.mChips.size() && this.mChips.get(i4) != getChildAt(i5)) {
                i4++;
            }
            if (i4 == this.mChips.size()) {
                return i4;
            }
            if (i4 >= i3) {
                break;
            }
        }
        if (i4 != i3) {
            COUILog.w(TAG, "addView index changed from " + i3 + " to " + i4);
        }
        return i4;
    }

    private void initAnimation() {
        AnonymousClass1 anonymousClass1 = new AnonymousClass1("ChipGroupAnimatorImpl");
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setResponse(0.35f);
        cOUISpringForce.setBounce(0.0f);
        COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(this, anonymousClass1);
        this.mGroupHeightChangeAnimation = cOUISpringAnimation;
        cOUISpringAnimation.setSpring(cOUISpringForce);
        this.mGroupHeightChangeAnimation.addEndListener(new e(this));
        AnonymousClass2 anonymousClass2 = new AnonymousClass2("ChipGroupAnimatorImpl");
        COUISpringForce cOUISpringForce2 = new COUISpringForce();
        cOUISpringForce2.setResponse(0.35f);
        cOUISpringForce2.setBounce(0.0f);
        COUISpringAnimation cOUISpringAnimation2 = new COUISpringAnimation(this, anonymousClass2);
        this.mGroupWidthChangeAnimation = cOUISpringAnimation2;
        cOUISpringAnimation2.setSpring(cOUISpringForce2);
        this.mGroupWidthChangeAnimation.addEndListener(new f(this));
    }

    private boolean isChipExceedCurrentLine(int i3, int i4, int i5, View view, View view2) {
        if (view == view2) {
            return i3 + getChildMeasuredWidth(view) <= i5;
        }
        return (((i3 + getChildMeasuredWidth(view)) + i4) + this.mChipSpacingHorizontal) + this.mCollapsableButtonSize <= i5;
    }

    private boolean isRtl() {
        return ViewCompat.getLayoutDirection(this) == 1;
    }

    private void loadFromAttributes(Context context, AttributeSet attributeSet, int i3, int i4) {
        TypedArray typedArrayObtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.COUIChipGroup, i3, i4);
        int dimensionPixelSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIChipGroup_chipSpacing, 0);
        setChipSpacingVertical(typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIChipGroup_chipSpacingVertical, dimensionPixelSize));
        setChipSpacingHorizontal(typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIChipGroup_chipSpacingHorizontal, dimensionPixelSize));
        setSingleLine(typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIChipGroup_singleLine, false));
        setSingleSelection(typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIChipGroup_singleSelection, false));
        setSelectionRequired(typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIChipGroup_selectionRequired, false));
        this.mDefaultCheckedId = typedArrayObtainStyledAttributes.getResourceId(R.styleable.COUIChipGroup_checkedChip, -1);
        setIsCollapsable(typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIChipGroup_isCollapsable, false));
        setIsCollapsed(typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIChipGroup_isCollapsed, false));
        setCollapsedMaxRows(typedArrayObtainStyledAttributes.getInteger(R.styleable.COUIChipGroup_collapsedMaxRows, 3));
        typedArrayObtainStyledAttributes.recycle();
    }

    private void performCollapseButtonClick() {
        this.mIsCollapsed = true;
        OnChipGroupCollapsableButtonClickListener onChipGroupCollapsableButtonClickListener = this.mOnChipGroupCollapsableButtonClickListener;
        if (onChipGroupCollapsableButtonClickListener != null) {
            onChipGroupCollapsableButtonClickListener.onCollapseButtonClicked();
        }
        requestLayout();
    }

    private void performExpandButtonClick() {
        this.mIsCollapsed = false;
        OnChipGroupCollapsableButtonClickListener onChipGroupCollapsableButtonClickListener = this.mOnChipGroupCollapsableButtonClickListener;
        if (onChipGroupCollapsableButtonClickListener != null) {
            onChipGroupCollapsableButtonClickListener.onExpandButtonClicked();
        }
        requestLayout();
    }

    private void removeChipInternal(COUIChip cOUIChip, boolean z2) {
        if (cOUIChip == null) {
            return;
        }
        this.mChips.remove(cOUIChip);
        if (this.mHiddenChips.contains(cOUIChip)) {
            this.mHiddenChips.remove(cOUIChip);
            return;
        }
        if (z2) {
            startChipTransition(cOUIChip);
        }
        cOUIChip.updateAttachState(false, z2);
        removeView(cOUIChip);
    }

    private void setAnimatingHeight(float f3) {
        int i3 = (int) f3;
        this.mAnimatingHeight = i3;
        ChipGroupLayoutAnimationCallback chipGroupLayoutAnimationCallback = this.mChipGroupLayoutAnimationCallback;
        if (chipGroupLayoutAnimationCallback != null) {
            chipGroupLayoutAnimationCallback.onAnimatingHeightUpdate(i3, this.mFinalHeight, i3 - Math.max(i3, getHeight()));
        }
        invalidate();
    }

    private void setAnimatingWidth(float f3) {
        int i3 = (int) f3;
        this.mAnimatingWidth = i3;
        ChipGroupLayoutAnimationCallback chipGroupLayoutAnimationCallback = this.mChipGroupLayoutAnimationCallback;
        if (chipGroupLayoutAnimationCallback != null) {
            int i4 = this.mFinalWidth;
            int rtlCoefficient = getRtlCoefficient();
            int i5 = this.mAnimatingWidth;
            chipGroupLayoutAnimationCallback.onAnimatingWidthUpdate(i3, i4, rtlCoefficient * (i5 - Math.max(i5, getWidth())));
        }
        invalidate();
    }

    private boolean showChip(COUIChip cOUIChip, int i3, ViewGroup.LayoutParams layoutParams) {
        if ((cOUIChip != this.mCollapseChip || this.mIsCollapseChipAttached) && ((cOUIChip != this.mExpandChip || this.mIsExpandChipAttached) && !this.mHiddenChips.remove(cOUIChip))) {
            return false;
        }
        endChipTransition(cOUIChip);
        addViewInLayout(cOUIChip, i3, layoutParams, true);
        return true;
    }

    private void startChipTransition(COUIChip cOUIChip) {
        if (this.mTransitioningViews == null) {
            this.mTransitioningViews = new ArrayList<>();
        }
        if (this.mTransitioningViews.contains(cOUIChip)) {
            return;
        }
        this.mTransitioningViews.add(cOUIChip);
        startViewTransition(cOUIChip);
    }

    private void updateBounds(COUIChip cOUIChip) {
        if (cOUIChip != null) {
            cOUIChip.updateChipRealBounds(cOUIChip.getLeft(), cOUIChip.getTop(), cOUIChip.getRight(), cOUIChip.getBottom(), useAnimation());
        }
    }

    private boolean useAnimation() {
        if (this.mSkipAnimationAfterConfigurationChanged) {
            return false;
        }
        return this.mEnableChipsAnimations;
    }

    public void addChip(COUIChip cOUIChip) {
        addChip(cOUIChip, true);
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i3, ViewGroup.LayoutParams layoutParams) {
        COUIEditText cOUIEditText;
        if (this.mShowEdittext && view == (cOUIEditText = this.mEdittext)) {
            super.addView(cOUIEditText, i3, layoutParams);
        }
        if (!(view instanceof COUIChip)) {
            COUILog.e(TAG, "Child must be a COUIChip!");
            return;
        }
        if (this.mChips.contains(view)) {
            COUILog.e(TAG, "addView: chip already exists!");
            return;
        }
        if (i3 > this.mChips.size() || i3 < 0) {
            i3 = this.mChips.size();
        }
        COUIChip cOUIChip = (COUIChip) view;
        this.mChips.add(i3, cOUIChip);
        cOUIChip.bindController(this);
        if (this.mHiddenChips.contains(view)) {
            i3 = indexInChildren(i3);
        }
        int i4 = this.mFinalItemIndexWhenCollapsed;
        if (i4 != -1 && i3 > i4) {
            this.mHiddenChips.add(cOUIChip);
            view.setLayoutParams(layoutParams);
        } else {
            super.addView(view, i3, layoutParams);
            if (this.mShowEdittext) {
                bringChildToFront(this.mEdittext);
            }
        }
    }

    public void check(int i3) {
        this.mCheckableGroup.check(i3);
    }

    @Override // android.view.ViewGroup
    public boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.checkLayoutParams(layoutParams) && (layoutParams instanceof LayoutParams);
    }

    public void clearCheck() {
        this.mCheckableGroup.clearCheck();
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.mSkipAnimationAfterConfigurationChanged = false;
    }

    public ViewGroup.LayoutParams generateCollapsableChipLayoutParams() {
        int i3 = this.mCollapsableButtonSize;
        return new LayoutParams(i3, i3);
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    public int getCheckedChipId() {
        return this.mCheckableGroup.getSingleCheckedId();
    }

    public List<Integer> getCheckedChipIds() {
        return this.mCheckableGroup.getCheckedIdsSortedByChildOrder(this);
    }

    public COUIChip getChipAt(int i3) {
        return this.mChips.get(i3);
    }

    public int getChipCount() {
        return this.mChips.size();
    }

    public int getChipSpacingHorizontal() {
        return this.mChipSpacingHorizontal;
    }

    public int getChipSpacingVertical() {
        return this.mChipSpacingVertical;
    }

    public int getCollapsedMaxRows() {
        return this.mCollapsedMaxRows;
    }

    public EditText getEdittext() {
        return this.mEdittext;
    }

    public int getRowCount() {
        return this.mRowCount;
    }

    public int getRowIndex(View view) {
        Object tag = view.getTag(R.id.row_index_key);
        if (tag instanceof Integer) {
            return ((Integer) tag).intValue();
        }
        return -1;
    }

    public int indexOfChips(COUIChip cOUIChip) {
        return this.mChips.indexOf(cOUIChip);
    }

    public boolean isChipGroupAnimationsEnabled() {
        return this.mEnableChipsAnimations;
    }

    public boolean isChipHidden(COUIChip cOUIChip) {
        if (this.mChips.contains(cOUIChip)) {
            return this.mHiddenChips.contains(cOUIChip);
        }
        return false;
    }

    public boolean isCollapsable() {
        return this.mIsCollapsable;
    }

    public boolean isCollapsed() {
        return this.mIsCollapsable && this.mIsCollapsed;
    }

    public boolean isSelectionRequired() {
        return this.mCheckableGroup.isSelectionRequired();
    }

    public boolean isSingleLine() {
        return this.mSingleLine;
    }

    public boolean isSingleSelection() {
        return this.mCheckableGroup.isSingleSelection();
    }

    public void onChipHideAnimationEnd(IChipGroupAnimator iChipGroupAnimator) {
        if (iChipGroupAnimator instanceof COUIChip) {
            endChipTransition((COUIChip) iChipGroupAnimator);
            iChipGroupAnimator.forceFinishAllAnimation();
            if (this.mChips.contains(iChipGroupAnimator)) {
                return;
            }
            iChipGroupAnimator.unbindController();
        }
    }

    public void onChipShowAnimationEnd(IChipGroupAnimator iChipGroupAnimator) {
        if (iChipGroupAnimator.isChipAnimationRunning() || !(iChipGroupAnimator instanceof COUIChip)) {
            return;
        }
        endChipTransition((COUIChip) iChipGroupAnimator);
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        Configuration configuration2 = this.mOldConfiguration;
        int iDiff = configuration2 != null ? configuration2.diff(configuration) : 0;
        if ((iDiff & 128) != 0 || (iDiff & 1024) != 0) {
            this.mSkipAnimationAfterConfigurationChanged = true;
        }
        super.onConfigurationChanged(configuration);
        this.mOldConfiguration = configuration;
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        int i3 = this.mDefaultCheckedId;
        if (i3 != -1) {
            this.mCheckableGroup.check(i3);
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        AccessibilityNodeInfoCompat.wrap(accessibilityNodeInfo).setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(getRowCount(), isSingleLine() ? getChipCount() : -1, false, isSingleSelection() ? 1 : 2));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        int marginEnd;
        int marginStart;
        int i7 = 0;
        this.mSuppressHeightChange = false;
        this.mSuppressWidthChange = false;
        int i8 = 1;
        if (getChildCount() == 0) {
            this.mRowCount = 0;
        } else {
            this.mRowCount = 1;
        }
        boolean z3 = ViewCompat.getLayoutDirection(this) == 1;
        ArrayList<COUIChip> arrayList = this.mChipsPendingToShow;
        if (arrayList != null && !arrayList.isEmpty()) {
            Iterator<COUIChip> it = this.mChipsPendingToShow.iterator();
            while (it.hasNext()) {
                COUIChip next = it.next();
                if (!showChip(next, indexOfChips(next), next.getLayoutParams())) {
                    it.remove();
                }
            }
        }
        ArrayList<COUIChip> arrayList2 = this.mChipsPendingToHide;
        if (arrayList2 != null && !arrayList2.isEmpty()) {
            Iterator<COUIChip> it2 = this.mChipsPendingToHide.iterator();
            while (it2.hasNext()) {
                COUIChip next2 = it2.next();
                if (z3) {
                    next2.layout(next2.getRight() - getChildMeasuredWidth(next2), next2.getTop(), next2.getRight(), next2.getBottom());
                } else {
                    next2.layout(next2.getLeft(), next2.getTop(), next2.getLeft() + getChildMeasuredWidth(next2), next2.getBottom());
                }
                if (!hideChip(next2)) {
                    it2.remove();
                }
            }
        }
        int paddingRight = z3 ? getPaddingRight() : getPaddingLeft();
        int paddingLeft = z3 ? getPaddingLeft() : getPaddingRight();
        int paddingTop = getPaddingTop();
        int i9 = i5 - i3;
        int i10 = i9 - paddingLeft;
        int childCount = getChildCount();
        int i11 = 0;
        int childMeasuredWidth = paddingRight;
        int i12 = paddingTop;
        while (i11 < childCount) {
            View childAt = getChildAt(i11);
            if (childAt != this.mEdittext) {
                if (childAt.getVisibility() == 8) {
                    childAt.setTag(R.id.row_index_key, -1);
                } else {
                    ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                    if (layoutParams instanceof LayoutParams) {
                        LayoutParams layoutParams2 = (LayoutParams) layoutParams;
                        marginStart = MarginLayoutParamsCompat.getMarginStart(layoutParams2);
                        marginEnd = MarginLayoutParamsCompat.getMarginEnd(layoutParams2);
                    } else {
                        marginEnd = i7;
                        marginStart = marginEnd;
                    }
                    int childMeasuredWidth2 = childMeasuredWidth + marginStart + getChildMeasuredWidth(childAt);
                    if (!isSingleLine() && childMeasuredWidth2 > i10) {
                        paddingTop = i12 + this.mChipSpacingVertical;
                        this.mRowCount += i8;
                        childMeasuredWidth = paddingRight;
                    }
                    childAt.setTag(R.id.row_index_key, Integer.valueOf(this.mRowCount - i8));
                    int i13 = childMeasuredWidth + marginStart;
                    int measuredWidth = childAt.getMeasuredWidth() + i13;
                    int measuredHeight = paddingTop + childAt.getMeasuredHeight();
                    if (z3) {
                        childAt.layout(i9 - measuredWidth, paddingTop, (i9 - childMeasuredWidth) - marginStart, measuredHeight);
                    } else {
                        childAt.layout(i13, paddingTop, measuredWidth, measuredHeight);
                    }
                    if (childAt instanceof COUIChip) {
                        updateBounds((COUIChip) childAt);
                    }
                    childMeasuredWidth += marginStart + marginEnd + getChildMeasuredWidth(childAt) + this.mChipSpacingHorizontal;
                    i12 = measuredHeight;
                }
            }
            i11++;
            i8 = i8;
            i7 = 0;
        }
        boolean z4 = i8;
        if (this.mIsCollapsable && this.mCollapsableButtonSize + childMeasuredWidth > i10) {
            this.mRowCount++;
        }
        if (this.mShowEdittext) {
            if (!z3) {
                if (this.mEdittextNewLine) {
                    COUIEditText cOUIEditText = this.mEdittext;
                    cOUIEditText.layout(paddingRight, this.mChipSpacingVertical + i12, cOUIEditText.getMeasuredWidth() + paddingRight, i12 + this.mChipSpacingVertical + this.mEdittext.getMeasuredHeight());
                } else {
                    COUIEditText cOUIEditText2 = this.mEdittext;
                    cOUIEditText2.layout(childMeasuredWidth, paddingTop, cOUIEditText2.getMeasuredWidth() + childMeasuredWidth, this.mEdittext.getMeasuredHeight() + paddingTop);
                }
                COUIEditText cOUIEditText3 = this.mEdittext;
                ((COUIChipGroupEditText) cOUIEditText3).updateChipRealBounds(cOUIEditText3.getLeft(), this.mEdittext.getTop(), this.mEdittext.getRight(), this.mEdittext.getBottom(), useAnimation());
            } else if (this.mEdittextNewLine) {
                COUIEditText cOUIEditText4 = this.mEdittext;
                cOUIEditText4.layout(paddingLeft, i12 + this.mChipSpacingVertical, paddingLeft - cOUIEditText4.getMeasuredWidth(), paddingTop + this.mEdittext.getMeasuredHeight());
            } else {
                COUIEditText cOUIEditText5 = this.mEdittext;
                int i14 = i10 - childMeasuredWidth;
                cOUIEditText5.layout(i14, paddingTop, cOUIEditText5.getMeasuredWidth() + i14, this.mEdittext.getMeasuredHeight() + paddingTop);
            }
        }
        ArrayList<COUIChip> arrayList3 = this.mChipsPendingToShow;
        if (arrayList3 != null && !arrayList3.isEmpty()) {
            Iterator<COUIChip> it3 = this.mChipsPendingToShow.iterator();
            while (it3.hasNext()) {
                it3.next().updateAttachState(z4, useAnimation());
            }
            this.mChipsPendingToShow.clear();
        }
        ArrayList<COUIChip> arrayList4 = this.mChipsPendingToHide;
        if (arrayList4 == null || arrayList4.isEmpty()) {
            return;
        }
        Iterator<COUIChip> it4 = this.mChipsPendingToHide.iterator();
        while (it4.hasNext()) {
            it4.next().updateAttachState(false, useAnimation());
        }
        this.mChipsPendingToHide.clear();
    }

    /* JADX WARN: Code duplicated, block: B:146:0x0360 A[PHI: r1 r3
  0x0360: PHI (r1v31 int) = (r1v30 int), (r1v30 int), (r1v35 int) binds: [B:125:0x0271, B:127:0x0279, B:144:0x035c] A[DONT_GENERATE, DONT_INLINE]
  0x0360: PHI (r3v3 int) = (r3v2 int), (r3v2 int), (r3v34 int) binds: [B:125:0x0271, B:127:0x0279, B:144:0x035c] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r24v0 */
    /* JADX WARN: Type inference failed for: r2v30 */
    /* JADX WARN: Type inference failed for: r2v31 */
    /* JADX WARN: Type inference failed for: r2v39 */
    @Override // android.view.View
    public void onMeasure(int i3, int i4) {
        int i5;
        boolean z2;
        COUIChip chipAt;
        int i6;
        int i7;
        boolean z3;
        int i8;
        int i9;
        int i10;
        COUIChip cOUIChip;
        int i11;
        ?? r2;
        int i12;
        int size = View.MeasureSpec.getSize(i3);
        int mode = View.MeasureSpec.getMode(i3);
        int size2 = View.MeasureSpec.getSize(i4);
        int mode2 = View.MeasureSpec.getMode(i4);
        int i13 = (mode == Integer.MIN_VALUE || mode == 1073741824) ? size : Integer.MAX_VALUE;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = i13 - getPaddingRight();
        this.mFinalItemIndexWhenCollapsed = -1;
        boolean z4 = true;
        int chipCount = getChipCount() - 1;
        while (true) {
            i5 = 8;
            if (chipCount < 0) {
                z2 = z4;
                chipAt = null;
                break;
            }
            chipAt = getChipAt(chipCount);
            z2 = z4;
            if (chipAt.getVisibility() != 8) {
                break;
            }
            chipCount--;
            z4 = z2;
        }
        int childMeasuredWidth = paddingLeft;
        int i14 = paddingTop;
        boolean z5 = z2;
        int i15 = 0;
        int i16 = 0;
        int i17 = 0;
        boolean z6 = false;
        while (true) {
            if (i15 >= getChipCount()) {
                i6 = paddingTop;
                i7 = i15;
                break;
            }
            int i18 = i15;
            COUIChip chipAt2 = getChipAt(i18);
            if (chipAt2.getVisibility() == i5) {
                int i19 = paddingTop;
                i11 = paddingRight;
                i12 = i19;
                i7 = i18;
                cOUIChip = chipAt;
            } else {
                chipAt2.forceLayout();
                measureChild(chipAt2, i3, i4);
                ViewGroup.LayoutParams layoutParams = chipAt2.getLayoutParams();
                if (layoutParams instanceof LayoutParams) {
                    LayoutParams layoutParams2 = (LayoutParams) layoutParams;
                    int i20 = ((ViewGroup.MarginLayoutParams) layoutParams2).leftMargin;
                    int i21 = ((ViewGroup.MarginLayoutParams) layoutParams2).rightMargin;
                    i9 = i20;
                    i8 = i21;
                } else {
                    i8 = 0;
                    i9 = 0;
                }
                int i22 = paddingRight;
                if (this.mIsCollapsable && this.mIsCollapsed && z5 == this.mCollapsedMaxRows) {
                    int i23 = childMeasuredWidth + i9;
                    i10 = i8;
                    cOUIChip = chipAt;
                    i7 = i18;
                    i6 = paddingTop;
                    i11 = i22;
                    if (!isChipExceedCurrentLine(i23, i10, i11, chipAt2, cOUIChip)) {
                        childMeasuredWidth += this.mCollapsableButtonSize + this.mChipSpacingHorizontal;
                        z6 = z2;
                        break;
                    } else {
                        this.mFinalItemIndexWhenCollapsed = i7;
                        i17 = childMeasuredWidth + this.mCollapsableButtonSize;
                    }
                } else {
                    i10 = i8;
                    cOUIChip = chipAt;
                    i7 = i18;
                    i6 = paddingTop;
                    i11 = i22;
                }
                if (childMeasuredWidth + i9 + getChildMeasuredWidth(chipAt2) <= i11 || isSingleLine()) {
                    r2 = z5;
                } else {
                    int i24 = (z5 ? 1 : 0) + 1;
                    if (i24 > this.mCollapsedMaxRows) {
                        z6 = z2;
                    }
                    childMeasuredWidth = getPaddingLeft();
                    i14 = i6 + this.mChipSpacingVertical;
                    r2 = i24;
                }
                int childMeasuredWidth2 = childMeasuredWidth + i9 + getChildMeasuredWidth(chipAt2);
                ?? r24 = r2;
                int iMax = Math.max(i14 + chipAt2.getMeasuredHeight(), i6);
                if (childMeasuredWidth2 > i16) {
                    i16 = childMeasuredWidth2;
                }
                childMeasuredWidth += i9 + i10 + getChildMeasuredWidth(chipAt2) + this.mChipSpacingHorizontal;
                if (i7 == getChipCount() - 1) {
                    if (this.mIsCollapsable && !this.mIsCollapsed && z6) {
                        int i25 = this.mCollapsableButtonSize;
                        if (childMeasuredWidth + i25 <= i11) {
                            i16 = childMeasuredWidth + i25;
                            childMeasuredWidth += this.mChipSpacingHorizontal + i25;
                        } else {
                            int iMax2 = Math.max(i16 + i10, i25);
                            iMax += this.mChipSpacingVertical + this.mCollapsableButtonSize;
                            childMeasuredWidth = getPaddingLeft() + this.mCollapsableButtonSize + this.mChipSpacingHorizontal;
                            i16 = iMax2;
                        }
                    } else {
                        i16 += i10;
                    }
                }
                if (indexOfChild(chipAt2) == -1 && this.mHiddenChips.contains(chipAt2) && !this.mChipsPendingToShow.contains(chipAt2) && this.mIsCollapsable) {
                    this.mChipsPendingToShow.add(chipAt2);
                }
                i12 = iMax;
                z5 = r24 == true ? 1 : 0;
            }
            i15 = i7 + 1;
            int i26 = i11;
            paddingTop = i12;
            paddingRight = i26;
            chipAt = cOUIChip;
            i5 = 8;
        }
        if (isCollapsable() && !isCollapsed() && z6) {
            if (this.mCollapseChip.getLayoutParams() == null) {
                this.mCollapseChip.setLayoutParams(generateCollapsableChipLayoutParams());
            }
            measureChild(this.mCollapseChip, i3, i4);
            if (this.mIsExpandChipAttached && !this.mChipsPendingToHide.contains(this.mExpandChip)) {
                this.mChipsPendingToHide.add(this.mExpandChip);
            }
            if (!this.mIsCollapseChipAttached && !this.mChipsPendingToShow.contains(this.mCollapseChip)) {
                this.mChipsPendingToShow.add(this.mCollapseChip);
            }
        } else if (isCollapsable() && isCollapsed() && z6) {
            if (this.mExpandChip.getLayoutParams() == null) {
                this.mExpandChip.setLayoutParams(generateCollapsableChipLayoutParams());
            }
            measureChild(this.mExpandChip, i3, i4);
            if (!this.mIsExpandChipAttached && !this.mChipsPendingToShow.contains(this.mExpandChip)) {
                this.mChipsPendingToShow.add(this.mExpandChip);
            }
            if (this.mIsCollapseChipAttached && !this.mChipsPendingToHide.contains(this.mCollapseChip)) {
                this.mChipsPendingToHide.add(this.mCollapseChip);
            }
        } else {
            if (this.mIsExpandChipAttached && !this.mChipsPendingToHide.contains(this.mExpandChip)) {
                this.mChipsPendingToHide.add(this.mExpandChip);
            }
            if (this.mIsCollapseChipAttached && !this.mChipsPendingToHide.contains(this.mCollapseChip)) {
                this.mChipsPendingToHide.add(this.mCollapseChip);
            }
        }
        int i27 = this.mFinalItemIndexWhenCollapsed;
        int i28 = i27 == -1 ? i7 : i27 + 1;
        int iMax3 = Math.max(i16, i17) + getPaddingRight();
        int paddingBottom = i6 + getPaddingBottom();
        int measuredDimension = getMeasuredDimension(size, mode, iMax3);
        if (this.mShowEdittext && this.mEdittext.isAttachedToWindow()) {
            int width = this.mEdittext.getWidth();
            this.mEdittextNewLine = false;
            ((COUIChipGroupEditText) this.mEdittext).ensureMinWidth();
            this.mEdittext.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i3), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i4), Integer.MIN_VALUE));
            if (isSingleLine()) {
                measuredDimension += this.mChipSpacingHorizontal + this.mEdittext.getMeasuredWidth();
                if (this.mChips.isEmpty()) {
                    paddingBottom += this.mEdittext.getMeasuredHeight();
                }
            } else if (this.mEdittext.getMeasuredWidth() > ((measuredDimension - getPaddingRight()) - childMeasuredWidth) - ((COUIChipGroupEditText) this.mEdittext).getEdittextMinWidth()) {
                if (this.mEdittext.isSingleLine()) {
                    this.mEdittext.setSingleLine(false);
                    this.mEdittext.setMinHeight(this.mEdittextHeight);
                }
                this.mEdittext.measure(View.MeasureSpec.makeMeasureSpec((View.MeasureSpec.getSize(i3) - getPaddingLeft()) - getPaddingRight(), 1073741824), View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i4), Integer.MIN_VALUE));
                this.mEdittextNewLine = z2;
                paddingBottom += this.mEdittext.getMeasuredHeight() + this.mChipSpacingVertical;
            } else {
                boolean z7 = z2;
                if (!this.mEdittext.isSingleLine()) {
                    this.mEdittext.setSingleLine(z7);
                    this.mEdittext.setMinHeight(this.mEdittextHeight);
                }
                this.mEdittext.measure(View.MeasureSpec.makeMeasureSpec((measuredDimension - getPaddingRight()) - childMeasuredWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i4), Integer.MIN_VALUE));
            }
            if (width != this.mEdittext.getMeasuredWidth()) {
                z3 = true;
            } else {
                z3 = false;
            }
        } else {
            z3 = false;
        }
        int measuredDimension2 = getMeasuredDimension(size2, mode2, paddingBottom);
        if (useAnimation()) {
            if (getAnimatingHeight() == -1.0f) {
                setAnimatingHeight(measuredDimension2);
            } else if (this.mFinalHeight != measuredDimension2) {
                float f3 = measuredDimension2;
                this.mGroupHeightChangeAnimation.animateToFinalPosition(f3);
                if (this.mChipGroupLayoutAnimationCallback != null && f3 > getAnimatingHeight()) {
                    ChipGroupLayoutAnimationCallback chipGroupLayoutAnimationCallback = this.mChipGroupLayoutAnimationCallback;
                    int i29 = this.mAnimatingHeight;
                    chipGroupLayoutAnimationCallback.onAnimatingHeightUpdate(i29, measuredDimension2, i29 - Math.max(i29, measuredDimension2));
                }
            }
            this.mFinalHeight = measuredDimension2;
            if (getAnimatingWidth() == -1.0f || z3) {
                setAnimatingWidth(measuredDimension);
            } else if (this.mFinalWidth != measuredDimension) {
                float f4 = measuredDimension;
                this.mGroupWidthChangeAnimation.animateToFinalPosition(f4);
                if (this.mChipGroupLayoutAnimationCallback != null && f4 > getAnimatingWidth()) {
                    ChipGroupLayoutAnimationCallback chipGroupLayoutAnimationCallback2 = this.mChipGroupLayoutAnimationCallback;
                    int i30 = this.mAnimatingWidth;
                    int rtlCoefficient = getRtlCoefficient();
                    int i31 = this.mAnimatingWidth;
                    chipGroupLayoutAnimationCallback2.onAnimatingWidthUpdate(i30, measuredDimension, rtlCoefficient * (i31 - Math.max(i31, measuredDimension)));
                }
            }
            this.mFinalWidth = measuredDimension;
        }
        if (this.mSuppressHeightChange || this.mSuppressWidthChange) {
            setMeasuredDimension(measuredDimension, measuredDimension2);
            return;
        }
        for (int chipCount2 = getChipCount() - 1; chipCount2 >= i28; chipCount2--) {
            COUIChip chipAt3 = getChipAt(chipCount2);
            if (chipAt3.getVisibility() != 8 && !this.mHiddenChips.contains(chipAt3)) {
                this.mChipsPendingToHide.add(0, chipAt3);
            }
        }
        if (!useAnimation()) {
            setMeasuredDimension(measuredDimension, measuredDimension2);
            return;
        }
        if (measuredDimension2 < getHeight()) {
            measuredDimension2 = getHeight();
        }
        if (measuredDimension < getWidth()) {
            measuredDimension = getWidth();
        }
        setMeasuredDimension(measuredDimension, measuredDimension2);
    }

    @Override // android.view.ViewGroup
    public void onViewAdded(View view) {
        super.onViewAdded(view);
        if (view == this.mCollapseChip) {
            this.mIsCollapseChipAttached = true;
        }
        if (view == this.mExpandChip) {
            this.mIsExpandChipAttached = true;
        }
    }

    @Override // android.view.ViewGroup
    public void onViewRemoved(View view) {
        super.onViewRemoved(view);
        if ((view instanceof COUIChip) && !this.mHiddenChips.contains(view)) {
            this.mChips.remove(view);
        }
        if (view == this.mCollapseChip) {
            this.mIsCollapseChipAttached = false;
        }
        if (view == this.mExpandChip) {
            this.mIsExpandChipAttached = false;
        }
    }

    public void removeAllChips() {
        removeAllChips(true);
    }

    public void removeChip(COUIChip cOUIChip) {
        removeChip(cOUIChip, true);
    }

    public void setChipGroupAnimationsEnabled(boolean z2) {
        this.mEnableChipsAnimations = z2;
    }

    public void setChipGroupLayoutAnimationCallback(ChipGroupLayoutAnimationCallback chipGroupLayoutAnimationCallback) {
        this.mChipGroupLayoutAnimationCallback = chipGroupLayoutAnimationCallback;
    }

    public void setChipSpacing(int i3) {
        setChipSpacingHorizontal(i3);
        setChipSpacingVertical(i3);
    }

    public void setChipSpacingHorizontal(int i3) {
        if (this.mChipSpacingHorizontal != i3) {
            this.mChipSpacingHorizontal = i3;
            requestLayout();
        }
    }

    public void setChipSpacingHorizontalResource(int i3) {
        setChipSpacingHorizontal(getResources().getDimensionPixelOffset(i3));
    }

    public void setChipSpacingResource(int i3) {
        setChipSpacing(getResources().getDimensionPixelOffset(i3));
    }

    public void setChipSpacingVertical(int i3) {
        if (this.mChipSpacingVertical != i3) {
            this.mChipSpacingVertical = i3;
            requestLayout();
        }
    }

    public void setChipSpacingVerticalResource(int i3) {
        setChipSpacingVertical(getResources().getDimensionPixelOffset(i3));
    }

    public void setCollapsableButtonBackgroundColor(int i3) {
        this.mCollapsableButtonBackgroundColor = i3;
        COUIChip cOUIChip = this.mExpandChip;
        if (cOUIChip != null) {
            cOUIChip.setUncheckedBackgroundColor(i3);
        }
        COUIChip cOUIChip2 = this.mCollapseChip;
        if (cOUIChip2 != null) {
            cOUIChip2.setUncheckedBackgroundColor(this.mCollapsableButtonBackgroundColor);
        }
    }

    public void setCollapsableButtonIconTint(int i3) {
        this.mCollapsableButtonIconTint = i3;
        COUIChip cOUIChip = this.mExpandChip;
        if (cOUIChip != null && cOUIChip.getChipIcon() != null) {
            this.mExpandChip.getChipIcon().setColorFilter(this.mCollapsableButtonIconTint, PorterDuff.Mode.SRC_IN);
        }
        COUIChip cOUIChip2 = this.mCollapseChip;
        if (cOUIChip2 == null || cOUIChip2.getChipIcon() == null) {
            return;
        }
        this.mCollapseChip.getChipIcon().setColorFilter(this.mCollapsableButtonIconTint, PorterDuff.Mode.SRC_IN);
    }

    public void setCollapsedMaxRows(int i3) {
        if (i3 <= 0) {
            throw new IllegalArgumentException("collapsedMaxRows must be greater than 0");
        }
        if (this.mCollapsedMaxRows != i3) {
            this.mCollapsedMaxRows = i3;
            if (this.mIsCollapsable) {
                requestLayout();
            }
        }
    }

    public void setIsCollapsable(boolean z2) {
        if (isSingleLine()) {
            COUILog.e(TAG, "Single line mode! Cannot set collapsable mode!");
            return;
        }
        if (this.mIsCollapsable != z2) {
            this.mIsCollapsable = z2;
            if (z2) {
                if (this.mExpandChip == null) {
                    this.mExpandChip = createCollapsableChip(false);
                }
                if (this.mCollapseChip == null) {
                    this.mCollapseChip = createCollapsableChip(true);
                }
            }
            requestLayout();
        }
    }

    public void setIsCollapsed(boolean z2) {
        if (z2 != this.mIsCollapsed) {
            if (z2) {
                collapse();
                return;
            } else {
                expand();
                return;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("ChipGroup already ");
        sb.append(this.mIsCollapsed ? "collapsed!" : "expanded");
        COUILog.w(TAG, sb.toString());
    }

    public void setOnCheckedStateChangeListener(OnCheckedStateChangeListener onCheckedStateChangeListener) {
        this.mOnCheckedStateChangeListener = onCheckedStateChangeListener;
    }

    public void setOnChipGroupCollapsableButtonClickListener(OnChipGroupCollapsableButtonClickListener onChipGroupCollapsableButtonClickListener) {
        this.mOnChipGroupCollapsableButtonClickListener = onChipGroupCollapsableButtonClickListener;
    }

    @Override // android.view.ViewGroup
    public void setOnHierarchyChangeListener(ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener) {
        PassThroughHierarchyChangeListener.access$502(this.mPassThroughListener, onHierarchyChangeListener);
    }

    public void setSelectionRequired(boolean z2) {
        this.mCheckableGroup.setSelectionRequired(z2);
    }

    public void setSingleLine(boolean z2) {
        if (isCollapsable()) {
            COUILog.e(TAG, "collapsable mode!");
        } else if (this.mSingleLine != z2) {
            this.mSingleLine = z2;
            requestLayout();
        }
    }

    public void setSingleSelection(boolean z2) {
        this.mCheckableGroup.setSingleSelection(z2);
    }

    public COUIChipGroup(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiChipGroupStyle);
    }

    public void addChip(COUIChip cOUIChip, boolean z2) {
        addChip(cOUIChip, -1, z2);
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    public void removeAllChips(boolean z2) {
        for (COUIChip cOUIChip : this.mChips) {
            if (this.mHiddenChips.contains(cOUIChip)) {
                this.mHiddenChips.remove(cOUIChip);
            } else {
                if (z2) {
                    startChipTransition(cOUIChip);
                }
                cOUIChip.updateAttachState(false, z2);
            }
        }
        if (this.mIsCollapseChipAttached || this.mIsExpandChipAttached) {
            removeViews(0, getChildCount() - 1);
        } else {
            removeAllViews();
        }
        this.mChips.clear();
    }

    public void removeChip(int i3) {
        removeChip(i3, true);
    }

    public COUIChipGroup(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, DEF_STYLE_RES);
    }

    public void addChip(COUIChip cOUIChip, int i3, boolean z2) {
        cOUIChip.bindController(this);
        cOUIChip.updateAttachState(true, z2);
        addView(cOUIChip, i3);
    }

    public void removeChip(int i3, boolean z2) {
        if (i3 < 0 || i3 > this.mChips.size()) {
            return;
        }
        removeChipInternal(this.mChips.get(i3), z2);
    }

    public COUIChipGroup(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        PassThroughHierarchyChangeListener passThroughHierarchyChangeListener = new PassThroughHierarchyChangeListener(this, null);
        this.mPassThroughListener = passThroughHierarchyChangeListener;
        this.mDrawableSize = new int[2];
        this.mChips = new ArrayList<>();
        this.mHiddenChips = new HashSet<>();
        this.mChipsPendingToHide = new ArrayList<>();
        this.mChipsPendingToShow = new ArrayList<>();
        COUICheckableGroup<COUIChip> cOUICheckableGroup = new COUICheckableGroup<>();
        this.mCheckableGroup = cOUICheckableGroup;
        this.mAnimatingHeight = -1;
        this.mAnimatingWidth = -1;
        this.mFinalItemIndexWhenCollapsed = -1;
        this.mEdittextHeight = 0;
        this.mCollapsableButtonIconTint = Integer.MAX_VALUE;
        this.mCollapsableButtonBackgroundColor = Integer.MAX_VALUE;
        this.mSuppressWidthChange = false;
        this.mSuppressHeightChange = false;
        this.mIsCollapseChipAttached = false;
        this.mIsExpandChipAttached = false;
        this.mEdittextNewLine = false;
        this.mShowEdittext = false;
        this.mEnableChipsAnimations = true;
        this.mSkipAnimationAfterConfigurationChanged = false;
        this.mEdittext = null;
        this.mOldConfiguration = null;
        loadFromAttributes(context, attributeSet, i3, i4);
        initAnimation();
        this.mCollapsableButtonSize = getResources().getDimensionPixelSize(R.dimen.coui_chip_group_collapsable_button_size);
        cOUICheckableGroup.setOnCheckedStateChangeListener(new g(this));
        super.setOnHierarchyChangeListener(passThroughHierarchyChangeListener);
        ViewCompat.setImportantForAccessibility(this, 1);
        COUIDarkModeUtil.setForceDarkAllow(this, false);
        setWillNotDraw(false);
        setClipChildren(false);
        setClipToPadding(false);
        if (this.mShowEdittext) {
            this.mEdittext = new COUIChipGroupEditText(context);
            int dimensionPixelOffset = context.getResources().getDimensionPixelOffset(R.dimen.coui_chip_suggest_style_height);
            this.mEdittextHeight = dimensionPixelOffset;
            this.mEdittext.setMinHeight(dimensionPixelOffset);
            addView(this.mEdittext);
        }
        this.mOldConfiguration = getResources().getConfiguration();
    }

    public void removeChip(COUIChip cOUIChip, boolean z2) {
        removeChipInternal(cOUIChip, z2);
    }
}