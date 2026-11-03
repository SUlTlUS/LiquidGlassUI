package com.coui.appcompat.bottomfloatingtoolbar;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.LinearInterpolator;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes7.dex */
public class ItemViewAnimatorImpl implements IBottomFloatingToolbarItemViewAnimator {
    private static final float DEFAULT_ANIMATION_RANGE = 10000.0f;
    private static final long DEFAULT_PENDING_ANIMATION_DURATION = 200;
    private static final float DEFAULT_SPRING_BOUNCE = 0.4f;
    private static final float DEFAULT_SPRING_RESPONSE = 0.5f;
    private static final float GAP_ANIMATION_MULTIPLIER = 5.0f;
    private static final float GAP_MAXIMUM_EXTRA_SCALE_RANGE = 7500.0f;
    static final int STATE_IDLE = 0;
    static final int STATE_PENDING = 1;
    static final int STATE_RUNNING = 2;
    private static final String TAG = "ItemViewAnimatorImpl";
    private boolean mAnimateChanged;
    private HashMap<View, Integer> mAnimatingState;
    private ArrayList<View> mAnimatingViews;
    private View mAppearingFirstItem;
    private View mAppearingLastItem;
    private Runnable mAttachAnimation;
    private boolean mClearAnimatingViews;
    private View mDisappearingFirstItem;
    private View mDisappearingLastItem;
    private final ArrayList<View> mFinalItemList;
    private float mGapScale;
    private final ValueAnimator mGapScaleAnimator;
    private COUIBottomFloatingToolbarMenuView mHost;
    private int mInsetFromPx;
    private int mInsetToPx;
    private boolean mIsGapEnlarging;
    private boolean mIsReverseRunning;
    private int mItemCountIdle;
    private Runnable mLayoutAnimation;
    private float mProgress;
    private int mState;
    private float mTargetProgress;
    private COUISpringAnimation mTransitionAnimation;

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingtoolbar.ItemViewAnimatorImpl$1, reason: invalid class name */
    public class AnonymousClass1 extends FloatPropertyCompat<ItemViewAnimatorImpl> {
        public AnonymousClass1(String str) {
            super(str);
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public /* bridge */ /* synthetic */ float getValue(ItemViewAnimatorImpl itemViewAnimatorImpl) {
            return getValue2(itemViewAnimatorImpl);
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public /* bridge */ /* synthetic */ void setValue(ItemViewAnimatorImpl itemViewAnimatorImpl, float f3) {
            setValue2(itemViewAnimatorImpl, f3);
        }

        /* JADX INFO: renamed from: getValue, reason: avoid collision after fix types in other method */
        public float getValue2(ItemViewAnimatorImpl itemViewAnimatorImpl) {
            return ItemViewAnimatorImpl.access$000(ItemViewAnimatorImpl.this);
        }

        /* JADX INFO: renamed from: setValue, reason: avoid collision after fix types in other method */
        public void setValue2(ItemViewAnimatorImpl itemViewAnimatorImpl, float f3) {
            ItemViewAnimatorImpl.access$100(ItemViewAnimatorImpl.this, f3);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingtoolbar.ItemViewAnimatorImpl$2, reason: invalid class name */
    public class AnonymousClass2 implements Animator.AnimatorListener {
        public AnonymousClass2() {
        }

        private void executeGapScaleAnimBack() {
            ItemViewAnimatorImpl.access$800(ItemViewAnimatorImpl.this).setFloatValues(ItemViewAnimatorImpl.GAP_MAXIMUM_EXTRA_SCALE_RANGE, 0.0f);
            ItemViewAnimatorImpl.access$800(ItemViewAnimatorImpl.this).start();
        }

        private void gapScaleAnimBack() {
            ItemViewAnimatorImpl.access$1100(ItemViewAnimatorImpl.this, false, "GapScaleAnimator from gapScaleAnimBack");
            executeGapScaleAnimBack();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            ItemViewAnimatorImpl.access$202(ItemViewAnimatorImpl.this, 0.0f);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (ItemViewAnimatorImpl.access$200(ItemViewAnimatorImpl.this) == 0.75f) {
                if (ItemViewAnimatorImpl.access$300(ItemViewAnimatorImpl.this) == 1) {
                    ItemViewAnimatorImpl.access$302(ItemViewAnimatorImpl.this, 2);
                    ViewParent parent = ItemViewAnimatorImpl.access$400(ItemViewAnimatorImpl.this).getParent();
                    if (parent instanceof COUIBottomFloatingToolbar) {
                        ((COUIBottomFloatingToolbar) parent).getAnimationCounter().setPendingAnimDecrement();
                    }
                }
                if (!ItemViewAnimatorImpl.access$400(ItemViewAnimatorImpl.this).mPendingRollback) {
                    ItemViewAnimatorImpl.access$500(ItemViewAnimatorImpl.this);
                }
                gapScaleAnimBack();
            } else if (ItemViewAnimatorImpl.access$200(ItemViewAnimatorImpl.this) == 0.0f) {
                ItemViewAnimatorImpl.access$400(ItemViewAnimatorImpl.this).mPendingRollback = false;
                if (!ItemViewAnimatorImpl.access$600(ItemViewAnimatorImpl.this).isRunning()) {
                    ItemViewAnimatorImpl.access$702(ItemViewAnimatorImpl.this, true);
                    ItemViewAnimatorImpl.access$302(ItemViewAnimatorImpl.this, 0);
                }
            }
            ItemViewAnimatorImpl itemViewAnimatorImpl = ItemViewAnimatorImpl.this;
            ItemViewAnimatorImpl.access$900(itemViewAnimatorImpl, ((Float) ItemViewAnimatorImpl.access$800(itemViewAnimatorImpl).getAnimatedValue()).floatValue());
            ItemViewAnimatorImpl.access$1000(ItemViewAnimatorImpl.this, false, "GapScaleAnimator");
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingtoolbar.ItemViewAnimatorImpl$3, reason: invalid class name */
    public class AnonymousClass3 implements Map.Entry<View, View> {
        final /* synthetic */ View val$key;
        final /* synthetic */ View val$value;

        public AnonymousClass3(View view, View view2) {
            this.val$key = view;
            this.val$value = view2;
        }

        @Override // java.util.Map.Entry
        public /* bridge */ /* synthetic */ View getKey() {
            return getKey();
        }

        @Override // java.util.Map.Entry
        public /* bridge */ /* synthetic */ View getValue() {
            return getValue();
        }

        @Override // java.util.Map.Entry
        public /* bridge */ /* synthetic */ View setValue(View view) {
            return setValue2(view);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map.Entry
        public View getKey() {
            return this.val$key;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map.Entry
        public View getValue() {
            return this.val$value;
        }

        /* JADX INFO: renamed from: setValue, reason: avoid collision after fix types in other method */
        public View setValue2(View view) {
            throw new UnsupportedOperationException("This entry is immutable");
        }
    }

    public ItemViewAnimatorImpl(COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView) {
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, GAP_MAXIMUM_EXTRA_SCALE_RANGE);
        this.mGapScaleAnimator = valueAnimatorOfFloat;
        this.mFinalItemList = new ArrayList<>();
        this.mAnimatingViews = new ArrayList<>();
        this.mLayoutAnimation = null;
        this.mAttachAnimation = null;
        this.mState = 0;
        this.mIsReverseRunning = false;
        this.mAnimateChanged = false;
        this.mAnimatingState = new HashMap<>();
        this.mItemCountIdle = 0;
        this.mGapScale = 0.0f;
        this.mIsGapEnlarging = false;
        this.mClearAnimatingViews = false;
        this.mDisappearingFirstItem = null;
        this.mAppearingFirstItem = null;
        this.mDisappearingLastItem = null;
        this.mAppearingLastItem = null;
        this.mInsetFromPx = 0;
        this.mInsetToPx = 0;
        this.mHost = cOUIBottomFloatingToolbarMenuView;
        AnonymousClass1 anonymousClass1 = new AnonymousClass1("progress");
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setResponse(0.5f);
        cOUISpringForce.setBounce(0.4f);
        COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(this, anonymousClass1);
        this.mTransitionAnimation = cOUISpringAnimation;
        cOUISpringAnimation.setSpring(cOUISpringForce);
        this.mTransitionAnimation.addEndListener(new h(this));
        valueAnimatorOfFloat.setInterpolator(new LinearInterpolator());
        valueAnimatorOfFloat.setDuration(200L);
        valueAnimatorOfFloat.addUpdateListener(new i(this));
        valueAnimatorOfFloat.addListener(new AnonymousClass2());
    }

    public static /* synthetic */ void a(ItemViewAnimatorImpl itemViewAnimatorImpl, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        itemViewAnimatorImpl.handleEnd(f3);
        itemViewAnimatorImpl.setAnimDecrement(true, "TransitionAnimation");
    }

    public static /* synthetic */ float access$000(ItemViewAnimatorImpl itemViewAnimatorImpl) {
        return itemViewAnimatorImpl.mProgress;
    }

    public static /* synthetic */ void access$100(ItemViewAnimatorImpl itemViewAnimatorImpl, float f3) {
        itemViewAnimatorImpl.setProgress(f3);
    }

    public static /* synthetic */ void access$1000(ItemViewAnimatorImpl itemViewAnimatorImpl, boolean z2, String str) {
        itemViewAnimatorImpl.setAnimDecrement(z2, str);
    }

    public static /* synthetic */ void access$1100(ItemViewAnimatorImpl itemViewAnimatorImpl, boolean z2, String str) {
        itemViewAnimatorImpl.setAnimIncrement(z2, str);
    }

    public static /* synthetic */ float access$200(ItemViewAnimatorImpl itemViewAnimatorImpl) {
        return itemViewAnimatorImpl.mGapScale;
    }

    public static /* synthetic */ float access$202(ItemViewAnimatorImpl itemViewAnimatorImpl, float f3) {
        itemViewAnimatorImpl.mGapScale = f3;
        return f3;
    }

    public static /* synthetic */ int access$300(ItemViewAnimatorImpl itemViewAnimatorImpl) {
        return itemViewAnimatorImpl.mState;
    }

    public static /* synthetic */ int access$302(ItemViewAnimatorImpl itemViewAnimatorImpl, int i3) {
        itemViewAnimatorImpl.mState = i3;
        return i3;
    }

    public static /* synthetic */ COUIBottomFloatingToolbarMenuView access$400(ItemViewAnimatorImpl itemViewAnimatorImpl) {
        return itemViewAnimatorImpl.mHost;
    }

    public static /* synthetic */ void access$500(ItemViewAnimatorImpl itemViewAnimatorImpl) {
        itemViewAnimatorImpl.onPendingAnimationFinished();
    }

    public static /* synthetic */ COUISpringAnimation access$600(ItemViewAnimatorImpl itemViewAnimatorImpl) {
        return itemViewAnimatorImpl.mTransitionAnimation;
    }

    public static /* synthetic */ boolean access$702(ItemViewAnimatorImpl itemViewAnimatorImpl, boolean z2) {
        itemViewAnimatorImpl.mClearAnimatingViews = z2;
        return z2;
    }

    public static /* synthetic */ ValueAnimator access$800(ItemViewAnimatorImpl itemViewAnimatorImpl) {
        return itemViewAnimatorImpl.mGapScaleAnimator;
    }

    public static /* synthetic */ void access$900(ItemViewAnimatorImpl itemViewAnimatorImpl, float f3) {
        itemViewAnimatorImpl.setGapScale(f3);
    }

    private void addAnimatingView(int i3, View view, int i4) {
        if (!this.mAnimatingViews.contains(view)) {
            this.mAnimatingViews.add(i3, view);
        }
        this.mAnimatingState.put(view, Integer.valueOf(i4));
    }

    private void applyAnimatingTransforms() {
        for (Map.Entry<View, Integer> entry : this.mAnimatingState.entrySet()) {
            if (entry.getValue().intValue() == 1) {
                entry.getKey().setScaleX(0.0f);
                entry.getKey().setScaleY(0.0f);
            }
        }
    }

    public static /* synthetic */ void b(ItemViewAnimatorImpl itemViewAnimatorImpl, Runnable runnable) {
        itemViewAnimatorImpl.getClass();
        runnable.run();
        itemViewAnimatorImpl.mAttachAnimation = null;
    }

    public static /* synthetic */ void c(ItemViewAnimatorImpl itemViewAnimatorImpl, ValueAnimator valueAnimator) {
        itemViewAnimatorImpl.getClass();
        itemViewAnimatorImpl.setGapScale(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    private void clearAnimatingViews() {
        this.mAnimatingState.clear();
        this.mAnimatingViews.clear();
    }

    public static /* synthetic */ void d(ItemViewAnimatorImpl itemViewAnimatorImpl, Runnable runnable) {
        itemViewAnimatorImpl.getClass();
        runnable.run();
        itemViewAnimatorImpl.mAttachAnimation = null;
    }

    private void executeDecreasingPendingAnimation() {
        this.mState = 1;
        this.mIsReverseRunning = false;
        this.mIsGapEnlarging = true;
        this.mGapScaleAnimator.setFloatValues(0.0f, GAP_MAXIMUM_EXTRA_SCALE_RANGE);
        this.mGapScaleAnimator.start();
    }

    private void executeIncreasingPendingAnimation() {
        this.mState = 1;
        this.mIsReverseRunning = false;
        this.mIsGapEnlarging = false;
        this.mGapScaleAnimator.setFloatValues(0.0f, GAP_MAXIMUM_EXTRA_SCALE_RANGE);
        this.mGapScaleAnimator.start();
    }

    private void handleEnd(float f3) {
        COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView = this.mHost;
        cOUIBottomFloatingToolbarMenuView.mDoItemAnimation = false;
        cOUIBottomFloatingToolbarMenuView.mDoAnimation = false;
        if ((f3 == 10000.0f || f3 == 0.0f) && !this.mGapScaleAnimator.isRunning()) {
            this.mClearAnimatingViews = true;
        }
        if (f3 == 10000.0f) {
            onRunningAnimationFinished();
        } else if (f3 == 0.0f) {
            onRollBackAnimationFinished();
        }
        flushAnimatingStateIfNeeded();
        this.mHost.syncVisualState();
    }

    private float handleItemState(View view, float f3, float f4) {
        if (this.mState == 2) {
            Integer num = this.mAnimatingState.get(view);
            if (num != null && num.intValue() == 0) {
                view.setScaleX(1.0f);
                view.setScaleY(1.0f);
                view.setAlpha(1.0f);
                return f3;
            }
            if (num != null) {
                if (num.intValue() != 1) {
                    f4 = 1.0f - f4;
                }
                float fMax = Math.max(0.0f, f4);
                view.setScaleX(fMax);
                view.setScaleY(fMax);
                view.setAlpha(fMax);
                return f4;
            }
        }
        return f3;
    }

    private void invalidateParent() {
        if (this.mHost.getParent() instanceof View) {
            ((View) this.mHost.getParent()).invalidate();
        }
    }

    private void onPendingAnimationFinished() {
        this.mProgress = 0.0f;
        this.mInsetToPx = this.mHost.computeMenuViewItemBgInsetHorizontal();
        if (!this.mTransitionAnimation.isRunning()) {
            setAnimIncrement(true, "TransitionAnimation from onPendingAnimationFinished");
        }
        this.mTargetProgress = 10000.0f;
        this.mTransitionAnimation.setStartValue(this.mProgress);
        this.mTransitionAnimation.animateToFinalPosition(10000.0f);
        this.mHost.mChildList.clear();
        COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView = this.mHost;
        cOUIBottomFloatingToolbarMenuView.mChildList.addAll(cOUIBottomFloatingToolbarMenuView.mMeasuredChild);
    }

    private void onRollBackAnimationFinished() {
        this.mState = 0;
        this.mHost.mMenuViewItemBgInsetHorizontal = this.mInsetFromPx;
        for (Map.Entry<View, Integer> entry : this.mAnimatingState.entrySet()) {
            if ((entry.getValue().intValue() == 1 && this.mIsReverseRunning) || (entry.getValue().intValue() == -1 && !this.mIsReverseRunning)) {
                this.mHost.endMenuItemViewTransition(entry.getKey());
            }
        }
        if (this.mHost.getRight() != this.mHost.mMenuViewAnimatorImpl.getRealBounds().right) {
            COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView = this.mHost;
            cOUIBottomFloatingToolbarMenuView.setRight((int) cOUIBottomFloatingToolbarMenuView.mMenuViewAnimatorImpl.getRealBounds().right);
        }
        this.mHost.mLastChild.clear();
        COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView2 = this.mHost;
        cOUIBottomFloatingToolbarMenuView2.mLastChild.addAll(cOUIBottomFloatingToolbarMenuView2.mChildList);
    }

    private void onRunningAnimationFinished() {
        this.mState = 0;
        this.mHost.mMenuViewItemBgInsetHorizontal = this.mInsetToPx;
        for (Map.Entry<View, Integer> entry : this.mAnimatingState.entrySet()) {
            if ((entry.getValue().intValue() == 1 && this.mIsReverseRunning) || (entry.getValue().intValue() == -1 && !this.mIsReverseRunning)) {
                this.mHost.endMenuItemViewTransition(entry.getKey());
            }
        }
        if (this.mHost.getRight() != this.mHost.mMenuViewAnimatorImpl.getRealBounds().right) {
            COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView = this.mHost;
            cOUIBottomFloatingToolbarMenuView.setRight((int) cOUIBottomFloatingToolbarMenuView.mMenuViewAnimatorImpl.getRealBounds().right);
        }
        this.mHost.mLastChild.clear();
        COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView2 = this.mHost;
        cOUIBottomFloatingToolbarMenuView2.mLastChild.addAll(cOUIBottomFloatingToolbarMenuView2.mChildList);
        this.mHost.applyMaterialCornerWeight();
    }

    private void resetItemAnimState() {
        if (this.mState == 1) {
            ViewParent parent = this.mHost.getParent();
            if (parent instanceof COUIBottomFloatingToolbar) {
                ((COUIBottomFloatingToolbar) parent).getAnimationCounter().abandonPendingSlot();
            }
        }
        this.mLayoutAnimation = null;
        this.mAttachAnimation = null;
        this.mAnimateChanged = false;
        this.mState = 0;
        this.mAnimatingViews.clear();
    }

    private void resetItemScale() {
        int i3 = this.mHost.isRtl() ? -1 : 1;
        for (int size = this.mHost.isRtl() ? this.mAnimatingViews.size() - 1 : 0; size >= 0 && size < this.mAnimatingViews.size(); size += i3) {
            View view = this.mAnimatingViews.get(size);
            view.setPivotX(view.getWidth() / 2.0f);
            view.setPivotY(view.getHeight() / 2.0f);
            if (this.mState == 2) {
                Integer num = this.mAnimatingState.get(view);
                if (this.mHost.mChildList.contains(view) && num != null && num.intValue() == 0) {
                    view.setScaleX(1.0f);
                    view.setScaleY(1.0f);
                } else if (num != null) {
                    float f3 = num.intValue() == 1 ? 1 : 0;
                    view.setScaleX(f3);
                    view.setScaleY(f3);
                }
            }
        }
    }

    private void resetItemTransformsToLayout() {
        this.mHost.layoutMenuItemsForCurrentBounds();
        for (int i3 = 0; i3 < this.mHost.mChildList.size(); i3++) {
            View view = this.mHost.mChildList.get(i3);
            view.setScaleX(1.0f);
            view.setScaleY(1.0f);
            view.setAlpha(1.0f);
            view.setTranslationX(0.0f);
        }
    }

    private boolean rollbackCheck(ArrayList<View> arrayList) {
        if (this.mAnimatingViews.isEmpty()) {
            return false;
        }
        int i3 = this.mState;
        if (i3 == 2) {
            animationRollBack();
            this.mFinalItemList.clear();
            this.mFinalItemList.addAll(arrayList);
            this.mHost.mChildList.clear();
            COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView = this.mHost;
            cOUIBottomFloatingToolbarMenuView.mChildList.addAll(cOUIBottomFloatingToolbarMenuView.mMeasuredChild);
        } else if (i3 == 1) {
            this.mFinalItemList.clear();
            this.mFinalItemList.addAll(arrayList);
            this.mDisappearingFirstItem = this.mHost.getChildAt(0);
            COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView2 = this.mHost;
            this.mDisappearingLastItem = cOUIBottomFloatingToolbarMenuView2.getChildAt(cOUIBottomFloatingToolbarMenuView2.getChildCount() - 1);
            if (!arrayList.isEmpty()) {
                this.mAppearingFirstItem = arrayList.get(0);
                this.mAppearingLastItem = arrayList.get(arrayList.size() - 1);
            }
            if (this.mDisappearingFirstItem == this.mAppearingFirstItem) {
                this.mDisappearingFirstItem = null;
                this.mAppearingFirstItem = null;
            }
            if (this.mDisappearingLastItem == this.mAppearingLastItem) {
                this.mDisappearingLastItem = null;
                this.mAppearingLastItem = null;
            }
            applyAnimatingTransforms();
        }
        this.mIsReverseRunning = !this.mIsReverseRunning;
        return true;
    }

    private void setAnimDecrement(boolean z2, String str) {
        ViewParent parent = this.mHost.getParent();
        if (parent instanceof COUIBottomFloatingToolbar) {
            ((COUIBottomFloatingToolbar) parent).getAnimationCounter().setAnimDecrement(z2, str);
        }
    }

    private void setAnimIncrement(boolean z2, String str) {
        ViewParent parent = this.mHost.getParent();
        if (parent instanceof COUIBottomFloatingToolbar) {
            ((COUIBottomFloatingToolbar) parent).getAnimationCounter().setAnimIncrement(z2, str);
        }
    }

    private void setGapScale(float f3) {
        this.mGapScale = f3 / 10000.0f;
        this.mHost.syncVisualStateIfNeeded();
        this.mHost.invalidate();
        invalidateParent();
    }

    private void setItemData(ArrayList<View> arrayList) {
        this.mProgress = 0.0f;
        this.mItemCountIdle = this.mHost.getChildCount();
        this.mFinalItemList.clear();
        this.mFinalItemList.addAll(arrayList);
        clearAnimatingViews();
        if (this.mHost.mChildList.isEmpty()) {
            this.mDisappearingFirstItem = null;
            this.mDisappearingLastItem = null;
        } else {
            this.mDisappearingFirstItem = this.mHost.mChildList.get(0);
            ArrayList<View> arrayList2 = this.mHost.mChildList;
            this.mDisappearingLastItem = arrayList2.get(arrayList2.size() - 1);
        }
        if (!arrayList.isEmpty()) {
            this.mAppearingFirstItem = arrayList.get(0);
            this.mAppearingLastItem = arrayList.get(arrayList.size() - 1);
        }
        if (this.mDisappearingFirstItem == this.mAppearingFirstItem) {
            this.mDisappearingFirstItem = null;
            this.mAppearingFirstItem = null;
        }
        if (this.mDisappearingLastItem == this.mAppearingLastItem) {
            this.mDisappearingLastItem = null;
            this.mAppearingLastItem = null;
        }
    }

    private void setProgress(float f3) {
        this.mProgress = f3;
        if (this.mState == 2 && this.mInsetFromPx != this.mInsetToPx) {
            float fMax = Math.max(0.0f, Math.min(f3 / 10000.0f, 1.0f));
            COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView = this.mHost;
            int i3 = this.mInsetFromPx;
            cOUIBottomFloatingToolbarMenuView.mMenuViewItemBgInsetHorizontal = Math.round(i3 + ((this.mInsetToPx - i3) * fMax));
        }
        this.mHost.syncVisualStateIfNeeded();
        this.mHost.invalidate();
        invalidateParent();
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarItemViewAnimator
    public void animationRollBack() {
        if (!this.mTransitionAnimation.isRunning()) {
            setAnimIncrement(true, "TransitionAnimation from animationRollBack");
        }
        if (this.mIsReverseRunning) {
            this.mTargetProgress = 10000.0f;
            this.mTransitionAnimation.animateToFinalPosition(10000.0f);
        } else {
            this.mTargetProgress = 0.0f;
            this.mTransitionAnimation.animateToFinalPosition(0.0f);
        }
    }

    public void cancelGapScaleAnim() {
        if (this.mGapScaleAnimator.isRunning()) {
            this.mGapScaleAnimator.cancel();
            setGapScale(0.0f);
        }
    }

    public void cancelItemAnim() {
        cancelGapScaleAnim();
        resetItemScale();
        cancelTransitionAnim();
        resetItemAnimState();
    }

    public void cancelTransitionAnim() {
        if (this.mTransitionAnimation.isRunning()) {
            this.mTransitionAnimation.cancel();
            setProgress(this.mTargetProgress);
            handleEnd(this.mTargetProgress);
        }
    }

    public void executeAttachAnimRunnable() {
        Runnable runnable = this.mAttachAnimation;
        if (runnable != null) {
            runnable.run();
            this.mAttachAnimation = null;
        }
    }

    public void executeLayoutAnimRunnable() {
        Runnable runnable = this.mLayoutAnimation;
        if (runnable != null) {
            runnable.run();
            this.mLayoutAnimation = null;
        }
    }

    public void flushAnimatingStateIfNeeded() {
        if (this.mClearAnimatingViews) {
            this.mAnimatingViews.clear();
            this.mClearAnimatingViews = false;
            resetItemTransformsToLayout();
        }
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarItemViewAnimator
    public List<View> getAnimatingViews() {
        return this.mAnimatingViews;
    }

    public int getCurState() {
        return this.mState;
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarItemViewAnimator
    public void handleOverflowItem(COUIBottomFloatingToolbarItemView cOUIBottomFloatingToolbarItemView, boolean z2) {
        int i3 = !this.mHost.mChildList.contains(cOUIBottomFloatingToolbarItemView) ? 1 : 0;
        int size = this.mAnimatingViews.size();
        if (!z2) {
            i3 = -1;
        }
        addAnimatingView(size, cOUIBottomFloatingToolbarItemView, i3);
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarItemViewAnimator
    public boolean isRunningAnimation() {
        COUISpringAnimation cOUISpringAnimation = this.mTransitionAnimation;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            return true;
        }
        ValueAnimator valueAnimator = this.mGapScaleAnimator;
        return valueAnimator != null && valueAnimator.isRunning();
    }

    public boolean isSessionActive() {
        return this.mState != 0;
    }

    public boolean matchesSessionTarget(ArrayList<View> arrayList) {
        if (this.mFinalItemList.isEmpty() || arrayList.size() != this.mFinalItemList.size()) {
            return false;
        }
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            if (arrayList.get(i3) != this.mFinalItemList.get(i3)) {
                return false;
            }
        }
        return true;
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarItemViewAnimator
    public void onMenuViewWidthChanged(int i3, int i4) {
        if (this.mState == 0 && this.mAnimateChanged) {
            this.mAnimateChanged = false;
            ViewParent parent = this.mHost.getParent();
            if (parent instanceof COUIBottomFloatingToolbar) {
                ((COUIBottomFloatingToolbar) parent).getAnimationCounter().setPendingAnimIncrement();
            }
            setAnimIncrement(false, "GapScaleAnimator from onMenuViewWidthChanged");
            if (i4 >= i3) {
                executeIncreasingPendingAnimation();
            } else {
                executeDecreasingPendingAnimation();
            }
        }
    }

    public void reconcileVisualStateAfterLayout() {
        applyAnimatingTransforms();
        if (this.mState == 2) {
            updateAllAnimatingViews();
        }
        this.mHost.syncVisualStateIfNeeded();
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarItemViewAnimator
    public void setAttachAnimation(Runnable runnable) {
        int i3 = this.mState;
        if (i3 == 1) {
            if (this.mIsReverseRunning) {
                this.mAttachAnimation = new f(this, runnable);
                return;
            } else {
                this.mHost.removeCallbacks(this.mAttachAnimation);
                this.mAttachAnimation = null;
                return;
            }
        }
        if (i3 == 2) {
            runnable.run();
        } else if (i3 == 0) {
            this.mAttachAnimation = new g(this, runnable);
        }
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarItemViewAnimator
    public void setItemList(ArrayList<View> arrayList) {
        if (rollbackCheck(arrayList)) {
            return;
        }
        this.mInsetFromPx = this.mHost.mMenuViewItemBgInsetHorizontal;
        setItemData(arrayList);
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            addAnimatingView(i3, arrayList.get(i3), 1);
        }
        LinkedList linkedList = new LinkedList();
        View view = null;
        for (int i4 = 0; i4 < this.mHost.mChildList.size(); i4++) {
            View view2 = this.mHost.mChildList.get(i4);
            if (arrayList.contains(view2)) {
                addAnimatingView(-1, view2, 0);
                view = view2;
            } else {
                linkedList.addLast(new AnonymousClass3(this.mHost.mChildList.get(i4), view));
            }
        }
        for (int size = linkedList.size() - 1; size >= 0; size--) {
            Map.Entry entry = (Map.Entry) linkedList.get(size);
            addAnimatingView(this.mAnimatingViews.indexOf(entry.getValue()) + 1, (View) entry.getKey(), -1);
        }
        if (this.mAnimatingViews.remove(this.mHost.mOverflowItem)) {
            this.mAnimatingViews.add(this.mHost.mOverflowItem);
        }
        applyAnimatingTransforms();
        if (!this.mHost.isAttachedToWindow() || this.mAnimatingViews.isEmpty()) {
            return;
        }
        this.mAnimateChanged = true;
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarItemViewAnimator
    public void setLayoutAnimation(Runnable runnable) {
        this.mLayoutAnimation = runnable;
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarItemViewAnimator
    public void skipAnimation() {
        if (this.mTransitionAnimation.canSkipToEnd()) {
            this.mTransitionAnimation.skipToEnd();
        }
        if (this.mGapScaleAnimator.isRunning()) {
            this.mGapScaleAnimator.cancel();
        }
    }

    /* JADX WARN: Code duplicated, block: B:49:0x00dd  */
    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarItemViewAnimator
    public void updateAllAnimatingViews() {
        float f3;
        flushAnimatingStateIfNeeded();
        boolean zIsRtl = this.mHost.isRtl();
        View view = zIsRtl ? this.mAppearingLastItem : this.mAppearingFirstItem;
        View view2 = zIsRtl ? this.mDisappearingLastItem : this.mDisappearingFirstItem;
        View view3 = zIsRtl ? this.mAppearingFirstItem : this.mAppearingLastItem;
        View view4 = zIsRtl ? this.mDisappearingFirstItem : this.mDisappearingLastItem;
        float f4 = (this.mIsGapEnlarging ? 1 : -1) * this.mGapScale;
        COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView = this.mHost;
        float f5 = cOUIBottomFloatingToolbarMenuView.mItemViewSpace * (f4 + 1.0f);
        int paddingLeft = cOUIBottomFloatingToolbarMenuView.getPaddingLeft();
        COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView2 = this.mHost;
        float f6 = paddingLeft + cOUIBottomFloatingToolbarMenuView2.mMenuViewItemBgInsetHorizontal;
        float f7 = cOUIBottomFloatingToolbarMenuView2.mItemViewSpace * f4 * (this.mItemCountIdle - 1);
        float f8 = 2.0f;
        float measuredWidth = f6 - (f7 / 2.0f);
        int size = zIsRtl ? this.mAnimatingViews.size() - 1 : 0;
        int i3 = zIsRtl ? -1 : 1;
        int size2 = zIsRtl ? 0 : this.mAnimatingViews.size() - 1;
        int i4 = size;
        while (i4 >= 0 && i4 < this.mAnimatingViews.size()) {
            View view5 = this.mAnimatingViews.get(i4);
            view5.setPivotX(view5.getWidth() / f8);
            view5.setPivotY(view5.getHeight() / f8);
            float f9 = this.mProgress / 10000.0f;
            float fHandleItemState = handleItemState(view5, 1.0f, f9);
            float scaleX = (view5.getScaleX() * f5) / f8;
            float scaleX2 = (view5.getScaleX() * f5) / f8;
            float f10 = f8;
            if (fHandleItemState < 0.0f) {
                scaleX = GAP_ANIMATION_MULTIPLIER * fHandleItemState * f5;
                scaleX2 = scaleX;
            }
            float fMax = Math.max(0.0f, Math.min(f9, 1.0f));
            if (view5 == view) {
                fMax = 1.0f - fMax;
            } else {
                if (view5 != view2) {
                    if (view5 == view3) {
                        fMax = 1.0f - fMax;
                    } else if (view5 == view4) {
                    }
                    scaleX2 *= fMax;
                }
                f3 = measuredWidth + scaleX;
                if (i4 == size) {
                    f3 -= scaleX;
                }
                float measuredWidth2 = f3 - ((view5.getMeasuredWidth() * (1.0f - view5.getScaleX())) / f10);
                view5.setX(measuredWidth2);
                measuredWidth = f3 + (view5.getMeasuredWidth() * view5.getScaleX()) + scaleX2;
                if (i4 != size2 && fHandleItemState < 0.0f) {
                    view5.setX(measuredWidth2 + scaleX2);
                }
                i4 += i3;
                f8 = f10;
            }
            scaleX *= fMax;
            f3 = measuredWidth + scaleX;
            if (i4 == size) {
                f3 -= scaleX;
            }
            float measuredWidth3 = f3 - ((view5.getMeasuredWidth() * (1.0f - view5.getScaleX())) / f10);
            view5.setX(measuredWidth3);
            measuredWidth = f3 + (view5.getMeasuredWidth() * view5.getScaleX()) + scaleX2;
            if (i4 != size2) {
            }
            i4 += i3;
            f8 = f10;
        }
    }
}