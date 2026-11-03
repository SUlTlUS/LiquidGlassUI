package com.coui.appcompat.bottomfloatingnavigationbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.COUIMaterialEffect;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.bottomfloatingtoolbar.COUIBottomFloatingToolbarItemView;
import com.google.android.material.R;
import com.oplus.view.material.OplusMaterialBaseParams;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes7.dex */
public class COUIBottomFloatingNavigationBarMenuView extends BaseBottomFloatingMenuView {
    private static final float ALPHA_SPRING_BOUNCE = 0.0f;
    private static final float BLUR_RADIUS_SPRING_BOUNCE = 0.0f;
    private static final float BLUR_RADIUS_SPRING_RESPONSE = 0.3f;
    private static final float DEFAULT_SPRING_ANIMATION_FACTOR = 10000.0f;
    private static final int GAP_COUNT_OFFSET = 1;
    private static final float HIGH_END_ALPHA_SPRING_RESPONSE = 0.2f;
    private static final float HIGH_END_HORIZONTAL_MOVE_SPRING_BOUNCE = 0.35f;
    private static final float HIGH_END_HORIZONTAL_MOVE_SPRING_RESPONSE = 0.55f;
    private static final float HIGH_END_SCALE_SPRING_BOUNCE = 0.35f;
    private static final float HIGH_END_SCALE_SPRING_RESPONSE = 0.35f;
    private static final float HIGH_END_WIDTH_CHANGE_SPRING_BOUNCE = 0.35f;
    private static final float HIGH_END_WIDTH_CHANGE_SPRING_RESPONSE = 0.55f;
    private static final float ITEM_CENTER_HALF_FACTOR = 0.5f;
    private static final float LOW_END_ALPHA_SPRING_RESPONSE = 0.25f;
    private static final float LOW_END_HORIZONTAL_MOVE_SPRING_BOUNCE = 0.3f;
    private static final float LOW_END_HORIZONTAL_MOVE_SPRING_RESPONSE = 0.5f;
    private static final float LOW_END_SCALE_SPRING_BOUNCE = 0.3f;
    private static final float LOW_END_SCALE_SPRING_RESPONSE = 0.45f;
    private static final float LOW_END_WIDTH_CHANGE_SPRING_BOUNCE = 0.3f;
    private static final float LOW_END_WIDTH_CHANGE_SPRING_RESPONSE = 0.5f;
    private static final float MENU_VIEW_DEFAULT_MIN_VISIBLE_CHANGE = 0.001f;
    private static final float MENU_VIEW_MAX_BLUR_RADIUS = 30.0f;
    private static final float MENU_VIEW_MIN_SCALE = 0.8f;
    private COUISpringAnimation mAlphaAnim;
    private boolean mDispatchItemClickWhenAlreadySelected;
    private float mForegroundBlurRadius;
    private COUISpringAnimation mForegroundBlurRadiusAnim;
    private boolean mHasSlidOffTouchDownItem;
    private COUISpringAnimation mHorizontalMoveAnim;
    private boolean mIsMenuVisible;
    private OplusMaterialBaseParams mMaterialBaseParams;
    private final ArrayList<BaseBottomFloatingItem> mNavigationBarItems;
    private Runnable mOnBoundsAnimationsEndAction;
    private OnMenuViewShowHideAnimListener mOnMenuViewShowHideAnimListener;
    private int mPendingSlideSelectItemId;
    private final RectF mRealBounds;
    private COUISpringAnimation mScaleAnim;
    private int mSelectedItemId;
    private final ArrayList<Float> mTargetItemCenterPointX;
    private int mTouchDownItemId;
    private COUISpringAnimation mWidthChangeAnim;

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingnavigationbar.COUIBottomFloatingNavigationBarMenuView$1, reason: invalid class name */
    public class AnonymousClass1 implements COUIDynamicAnimation.OnAnimationUpdateListener {
        public AnonymousClass1() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
        public void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
            float f5 = f3 / 10000.0f;
            COUIBottomFloatingNavigationBarMenuView.this.setScaleX(f5);
            COUIBottomFloatingNavigationBarMenuView.this.setScaleY(f5);
            BaseBottomFloatingMenuView.OnAnimationSyncListener onAnimationSyncListener = COUIBottomFloatingNavigationBarMenuView.this.mAnimationSyncListener;
            if (onAnimationSyncListener != null) {
                onAnimationSyncListener.onScaleAnimUpdate(f5);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingnavigationbar.COUIBottomFloatingNavigationBarMenuView$10, reason: invalid class name */
    public class AnonymousClass10 extends AccessibilityDelegateCompat {
        final /* synthetic */ int val$position;

        public AnonymousClass10(int i3) {
            this.val$position = i3;
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(0, 1, this.val$position, 1, false, view.isSelected()));
            accessibilityNodeInfoCompat.setRoleDescription(view.getResources().getString(R.string.item_view_role_description));
            if (view.isSelected()) {
                accessibilityNodeInfoCompat.setClickable(false);
                accessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingnavigationbar.COUIBottomFloatingNavigationBarMenuView$2, reason: invalid class name */
    public class AnonymousClass2 implements COUIDynamicAnimation.OnAnimationUpdateListener {
        public AnonymousClass2() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
        public void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
            float f5 = f3 / 10000.0f;
            COUIBottomFloatingNavigationBarMenuView.this.setAlpha(f5);
            BaseBottomFloatingMenuView.OnAnimationSyncListener onAnimationSyncListener = COUIBottomFloatingNavigationBarMenuView.this.mAnimationSyncListener;
            if (onAnimationSyncListener != null) {
                onAnimationSyncListener.onAlphaAnimUpdate(f5);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingnavigationbar.COUIBottomFloatingNavigationBarMenuView$3, reason: invalid class name */
    public class AnonymousClass3 implements COUIDynamicAnimation.OnAnimationUpdateListener {
        public AnonymousClass3() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
        public void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
            COUIBottomFloatingNavigationBarMenuView.access$002(COUIBottomFloatingNavigationBarMenuView.this, f3 / 10000.0f);
            COUIBottomFloatingNavigationBarMenuView cOUIBottomFloatingNavigationBarMenuView = COUIBottomFloatingNavigationBarMenuView.this;
            cOUIBottomFloatingNavigationBarMenuView.setRenderEffect(RenderEffect.createBlurEffect(COUIBottomFloatingNavigationBarMenuView.access$000(cOUIBottomFloatingNavigationBarMenuView), COUIBottomFloatingNavigationBarMenuView.access$000(COUIBottomFloatingNavigationBarMenuView.this), Shader.TileMode.CLAMP));
            COUIBottomFloatingNavigationBarMenuView cOUIBottomFloatingNavigationBarMenuView2 = COUIBottomFloatingNavigationBarMenuView.this;
            BaseBottomFloatingMenuView.OnAnimationSyncListener onAnimationSyncListener = cOUIBottomFloatingNavigationBarMenuView2.mAnimationSyncListener;
            if (onAnimationSyncListener != null) {
                onAnimationSyncListener.onForegroundBlurRadiusAnimUpdate(COUIBottomFloatingNavigationBarMenuView.access$000(cOUIBottomFloatingNavigationBarMenuView2));
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingnavigationbar.COUIBottomFloatingNavigationBarMenuView$4, reason: invalid class name */
    public class AnonymousClass4 implements COUIDynamicAnimation.OnAnimationEndListener {
        public AnonymousClass4() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
        public void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
            COUIBottomFloatingNavigationBarMenuView.this.setRenderEffect(null);
            COUIBottomFloatingNavigationBarMenuView.access$100(COUIBottomFloatingNavigationBarMenuView.this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingnavigationbar.COUIBottomFloatingNavigationBarMenuView$5, reason: invalid class name */
    public class AnonymousClass5 implements COUIDynamicAnimation.OnAnimationUpdateListener {
        public AnonymousClass5() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
        public void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
            if (COUIBottomFloatingNavigationBarMenuView.this.isLayoutRtl()) {
                RectF rectF = COUIBottomFloatingNavigationBarMenuView.this.mDrawingBounds;
                rectF.left = rectF.right - f3;
            } else {
                RectF rectF2 = COUIBottomFloatingNavigationBarMenuView.this.mDrawingBounds;
                rectF2.right = rectF2.left + f3;
            }
            COUIBottomFloatingNavigationBarMenuView.this.notifyDrawingBoundsChanged();
            int childCount = COUIBottomFloatingNavigationBarMenuView.this.getChildCount();
            if (childCount <= 0 || COUIBottomFloatingNavigationBarMenuView.access$200(COUIBottomFloatingNavigationBarMenuView.this).size() != childCount) {
                return;
            }
            float totalItemSpace = ((f3 - (COUIBottomFloatingNavigationBarMenuView.this.mInnerPadding * 2)) - COUIBottomFloatingNavigationBarMenuView.this.getTotalItemSpace()) / childCount;
            for (int i3 = 0; i3 < childCount; i3++) {
                COUIBottomFloatingNavigationBarMenuView.this.getChildAt(i3).setTranslationX(COUIBottomFloatingNavigationBarMenuView.access$300(COUIBottomFloatingNavigationBarMenuView.this, totalItemSpace, i3) - ((Float) COUIBottomFloatingNavigationBarMenuView.access$200(COUIBottomFloatingNavigationBarMenuView.this).get(i3)).floatValue());
            }
            COUIBottomFloatingNavigationBarMenuView.this.invalidate();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingnavigationbar.COUIBottomFloatingNavigationBarMenuView$6, reason: invalid class name */
    public class AnonymousClass6 implements COUIDynamicAnimation.OnAnimationEndListener {
        public AnonymousClass6() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
        public void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
            for (int i3 = 0; i3 < COUIBottomFloatingNavigationBarMenuView.this.getChildCount(); i3++) {
                COUIBottomFloatingNavigationBarMenuView.this.getChildAt(i3).setTranslationX(0.0f);
            }
            COUIBottomFloatingNavigationBarMenuView.access$400(COUIBottomFloatingNavigationBarMenuView.this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingnavigationbar.COUIBottomFloatingNavigationBarMenuView$7, reason: invalid class name */
    public class AnonymousClass7 implements COUIDynamicAnimation.OnAnimationUpdateListener {
        public AnonymousClass7() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
        public void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
            if (COUIBottomFloatingNavigationBarMenuView.this.isLayoutRtl()) {
                float fWidth = COUIBottomFloatingNavigationBarMenuView.this.mDrawingBounds.width();
                COUIBottomFloatingNavigationBarMenuView cOUIBottomFloatingNavigationBarMenuView = COUIBottomFloatingNavigationBarMenuView.this;
                RectF rectF = cOUIBottomFloatingNavigationBarMenuView.mDrawingBounds;
                rectF.right = f3;
                rectF.left = f3 - fWidth;
                cOUIBottomFloatingNavigationBarMenuView.setX(f3 - cOUIBottomFloatingNavigationBarMenuView.getWidth());
            } else {
                RectF rectF2 = COUIBottomFloatingNavigationBarMenuView.this.mDrawingBounds;
                rectF2.offsetTo(f3, rectF2.top);
                COUIBottomFloatingNavigationBarMenuView cOUIBottomFloatingNavigationBarMenuView2 = COUIBottomFloatingNavigationBarMenuView.this;
                cOUIBottomFloatingNavigationBarMenuView2.setX(cOUIBottomFloatingNavigationBarMenuView2.mDrawingBounds.left);
            }
            COUIBottomFloatingNavigationBarMenuView.this.notifyDrawingBoundsChanged();
            COUIBottomFloatingNavigationBarMenuView.this.invalidate();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingnavigationbar.COUIBottomFloatingNavigationBarMenuView$8, reason: invalid class name */
    public class AnonymousClass8 implements COUIDynamicAnimation.OnAnimationEndListener {
        public AnonymousClass8() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
        public void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
            COUIBottomFloatingNavigationBarMenuView.this.mTouchMotionAnimator.enable();
            COUIBottomFloatingNavigationBarMenuView.access$400(COUIBottomFloatingNavigationBarMenuView.this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingnavigationbar.COUIBottomFloatingNavigationBarMenuView$9, reason: invalid class name */
    public class AnonymousClass9 implements View.OnClickListener {
        final /* synthetic */ COUIBottomFloatingNavigationBarItem val$item;
        final /* synthetic */ int val$position;

        public AnonymousClass9(COUIBottomFloatingNavigationBarItem cOUIBottomFloatingNavigationBarItem, int i3) {
            this.val$item = cOUIBottomFloatingNavigationBarItem;
            this.val$position = i3;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int id = this.val$item.getId();
            if (id != COUIBottomFloatingNavigationBarMenuView.access$500(COUIBottomFloatingNavigationBarMenuView.this) || COUIBottomFloatingNavigationBarMenuView.access$600(COUIBottomFloatingNavigationBarMenuView.this)) {
                COUIBottomFloatingNavigationBarMenuView.this.setSelectedItem(id);
                BaseBottomFloatingMenuView.OnItemClickListener onItemClickListener = COUIBottomFloatingNavigationBarMenuView.this.mOnItemClickListener;
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, this.val$item, this.val$position);
                }
            }
        }
    }

    public COUIBottomFloatingNavigationBarMenuView(Context context) {
        this(context, null);
    }

    public static /* synthetic */ float access$000(COUIBottomFloatingNavigationBarMenuView cOUIBottomFloatingNavigationBarMenuView) {
        return cOUIBottomFloatingNavigationBarMenuView.mForegroundBlurRadius;
    }

    public static /* synthetic */ float access$002(COUIBottomFloatingNavigationBarMenuView cOUIBottomFloatingNavigationBarMenuView, float f3) {
        cOUIBottomFloatingNavigationBarMenuView.mForegroundBlurRadius = f3;
        return f3;
    }

    public static /* synthetic */ void access$100(COUIBottomFloatingNavigationBarMenuView cOUIBottomFloatingNavigationBarMenuView) {
        cOUIBottomFloatingNavigationBarMenuView.checkAllAnimationsEnded();
    }

    public static /* synthetic */ ArrayList access$200(COUIBottomFloatingNavigationBarMenuView cOUIBottomFloatingNavigationBarMenuView) {
        return cOUIBottomFloatingNavigationBarMenuView.mTargetItemCenterPointX;
    }

    public static /* synthetic */ float access$300(COUIBottomFloatingNavigationBarMenuView cOUIBottomFloatingNavigationBarMenuView, float f3, int i3) {
        return cOUIBottomFloatingNavigationBarMenuView.calculateItemCenterPointX(f3, i3);
    }

    public static /* synthetic */ void access$400(COUIBottomFloatingNavigationBarMenuView cOUIBottomFloatingNavigationBarMenuView) {
        cOUIBottomFloatingNavigationBarMenuView.checkAndLayoutAfterAnimations();
    }

    public static /* synthetic */ int access$500(COUIBottomFloatingNavigationBarMenuView cOUIBottomFloatingNavigationBarMenuView) {
        return cOUIBottomFloatingNavigationBarMenuView.mSelectedItemId;
    }

    public static /* synthetic */ boolean access$600(COUIBottomFloatingNavigationBarMenuView cOUIBottomFloatingNavigationBarMenuView) {
        return cOUIBottomFloatingNavigationBarMenuView.mDispatchItemClickWhenAlreadySelected;
    }

    private void addItem(COUIBottomFloatingNavigationBarItem cOUIBottomFloatingNavigationBarItem, int i3) {
        COUIBottomFloatingToolbarItemView cOUIBottomFloatingToolbarItemView = new COUIBottomFloatingToolbarItemView(getContext());
        if (cOUIBottomFloatingNavigationBarItem.isLargeIcon()) {
            cOUIBottomFloatingToolbarItemView.setItemStyle(2);
        } else {
            cOUIBottomFloatingToolbarItemView.setItemStyle(1);
        }
        cOUIBottomFloatingToolbarItemView.setId(cOUIBottomFloatingNavigationBarItem.getId());
        cOUIBottomFloatingToolbarItemView.setContentDescription(cOUIBottomFloatingNavigationBarItem.getDescription());
        cOUIBottomFloatingToolbarItemView.setTitle(cOUIBottomFloatingNavigationBarItem.getTitle());
        int iconResId = cOUIBottomFloatingNavigationBarItem.getIconResId();
        if (iconResId != 0) {
            cOUIBottomFloatingToolbarItemView.setIconResourceId(iconResId);
        } else {
            cOUIBottomFloatingToolbarItemView.setIcon(cOUIBottomFloatingNavigationBarItem.getIcon());
        }
        cOUIBottomFloatingToolbarItemView.setRedDot(cOUIBottomFloatingNavigationBarItem.getRedDotType(), cOUIBottomFloatingNavigationBarItem.getRedDotNumber());
        cOUIBottomFloatingToolbarItemView.setOnClickListener(new AnonymousClass9(cOUIBottomFloatingNavigationBarItem, i3));
        setupItemAccessibility(cOUIBottomFloatingToolbarItemView, i3);
        addView(cOUIBottomFloatingToolbarItemView);
        this.mNavigationBarItems.add(cOUIBottomFloatingNavigationBarItem);
    }

    private boolean areMenuViewShowHideAnimationsIdle() {
        COUISpringAnimation cOUISpringAnimation = this.mAlphaAnim;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            return false;
        }
        COUISpringAnimation cOUISpringAnimation2 = this.mScaleAnim;
        if (cOUISpringAnimation2 != null && cOUISpringAnimation2.isRunning()) {
            return false;
        }
        COUISpringAnimation cOUISpringAnimation3 = this.mForegroundBlurRadiusAnim;
        return cOUISpringAnimation3 == null || !cOUISpringAnimation3.isRunning();
    }

    public static /* synthetic */ void b(COUIBottomFloatingNavigationBarMenuView cOUIBottomFloatingNavigationBarMenuView, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        cOUIBottomFloatingNavigationBarMenuView.checkAllAnimationsEnded();
    }

    public static /* synthetic */ void c(COUIBottomFloatingNavigationBarMenuView cOUIBottomFloatingNavigationBarMenuView, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        cOUIBottomFloatingNavigationBarMenuView.checkAllAnimationsEnded();
    }

    private float calculateItemCenterPointX(float f3, int i3) {
        if (isLayoutRtl()) {
            return ((getWidth() - this.mInnerPadding) - (f3 * (i3 + 0.5f))) - (((getChildCount() - i3) - 1) * this.mItemSpace);
        }
        return this.mInnerPadding + (f3 * (i3 + 0.5f)) + (this.mItemSpace * i3);
    }

    private void checkAllAnimationsEnded() {
        if (areMenuViewShowHideAnimationsIdle()) {
            if (!this.mIsMenuVisible) {
                setVisibility(4);
            }
            OnMenuViewShowHideAnimListener onMenuViewShowHideAnimListener = this.mOnMenuViewShowHideAnimListener;
            if (onMenuViewShowHideAnimListener != null) {
                onMenuViewShowHideAnimListener.onMenuViewShowHideAnimEnd();
            }
        }
    }

    private void checkAndLayoutAfterAnimations() {
        if (this.mRealBounds.isEmpty() || isBoundsAnimationRunning()) {
            return;
        }
        setTranslationX(0.0f);
        RectF rectF = this.mRealBounds;
        layout((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
        Runnable runnable = this.mOnBoundsAnimationsEndAction;
        if (runnable != null) {
            runnable.run();
        }
    }

    private void dispatchPendingSlideItemClickIfNeeded(float f3, float f4, boolean z2) {
        int i3;
        View viewFindViewById;
        int iIndexOfChild;
        if (this.mOnItemClickListener == null || (i3 = this.mPendingSlideSelectItemId) == -1 || this.mSelectedItemId != i3) {
            return;
        }
        if ((z2 || !shouldSkipSlideDispatch(f3, f4)) && (viewFindViewById = findViewById(this.mSelectedItemId)) != null && viewFindViewById.isEnabled() && (iIndexOfChild = indexOfChild(viewFindViewById)) >= 0 && iIndexOfChild < this.mNavigationBarItems.size()) {
            this.mOnItemClickListener.onItemClick(viewFindViewById, this.mNavigationBarItems.get(iIndexOfChild), iIndexOfChild);
        }
    }

    private void doHorizontalMoveAnim(int i3, int i4) {
        if (isLayoutRtl()) {
            float f3 = i4;
            if (this.mRealBounds.right != f3) {
                this.mTouchMotionAnimator.disable();
                setX(this.mDrawingBounds.right - getWidth());
                this.mHorizontalMoveAnim.setStartValue(this.mDrawingBounds.right);
                this.mHorizontalMoveAnim.animateToFinalPosition(f3);
                return;
            }
            return;
        }
        float f4 = i3;
        if (this.mRealBounds.left != f4) {
            this.mTouchMotionAnimator.disable();
            setX(this.mDrawingBounds.left);
            this.mHorizontalMoveAnim.setStartValue(this.mDrawingBounds.left);
            this.mHorizontalMoveAnim.animateToFinalPosition(f4);
        }
    }

    private void doWidthChangeAnim(int i3) {
        float f3 = i3;
        if (this.mRealBounds.width() != f3) {
            this.mTargetItemCenterPointX.clear();
            float fWidth = this.mDrawingBounds.width();
            int totalItemSpace = getTotalItemSpace();
            float childCount = ((fWidth - (this.mInnerPadding * 2)) - totalItemSpace) / getChildCount();
            float childCount2 = ((i3 - (this.mInnerPadding * 2)) - totalItemSpace) / getChildCount();
            int iMax = (int) Math.max(childCount, childCount2);
            int iMin = (int) Math.min(childCount, childCount2);
            for (int i4 = 0; i4 < getChildCount(); i4++) {
                View childAt = getChildAt(i4);
                float fCalculateItemCenterPointX = calculateItemCenterPointX(childCount, i4);
                float fCalculateItemCenterPointX2 = calculateItemCenterPointX(childCount2, i4);
                childAt.setTranslationX(fCalculateItemCenterPointX - fCalculateItemCenterPointX2);
                this.mTargetItemCenterPointX.add(Float.valueOf(fCalculateItemCenterPointX2));
                if (childAt instanceof COUIBottomFloatingToolbarItemView) {
                    ((COUIBottomFloatingToolbarItemView) childAt).startWidthAnimation(iMax, iMin, f3 < fWidth);
                }
            }
            this.mWidthChangeAnim.setStartValue(fWidth);
            this.mWidthChangeAnim.animateToFinalPosition(f3);
        }
    }

    private void initAlphaAnim() {
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(0.0f);
        cOUISpringForce.setResponse(isSupportMeltBall() ? 0.2f : 0.25f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mAlphaAnim = spring;
        spring.addUpdateListener(new AnonymousClass2());
        this.mAlphaAnim.addEndListener(new j(this));
    }

    private void initForegroundBlurRadiusAnim() {
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(0.0f);
        cOUISpringForce.setResponse(0.3f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mForegroundBlurRadiusAnim = spring;
        spring.addUpdateListener(new AnonymousClass3());
        this.mForegroundBlurRadiusAnim.addEndListener(new AnonymousClass4());
    }

    private void initHorizontalMoveAnim() {
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(isSupportMeltBall() ? 0.35f : 0.3f);
        cOUISpringForce.setResponse(isSupportMeltBall() ? 0.55f : 0.5f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mHorizontalMoveAnim = spring;
        spring.setMinimumVisibleChange(0.001f);
        this.mHorizontalMoveAnim.addUpdateListener(new AnonymousClass7());
        this.mHorizontalMoveAnim.addEndListener(new AnonymousClass8());
    }

    private void initNaviMenuAnim() {
        initHorizontalMoveAnim();
        initWidthChangeAnim();
        initScaleAnim();
        initAlphaAnim();
        initForegroundBlurRadiusAnim();
    }

    private void initScaleAnim() {
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(isSupportMeltBall() ? 0.35f : 0.3f);
        cOUISpringForce.setResponse(isSupportMeltBall() ? 0.35f : 0.45f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mScaleAnim = spring;
        spring.addUpdateListener(new AnonymousClass1());
        this.mScaleAnim.addEndListener(new i(this));
    }

    private void initWidthChangeAnim() {
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(isSupportMeltBall() ? 0.35f : 0.3f);
        cOUISpringForce.setResponse(isSupportMeltBall() ? 0.55f : 0.5f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mWidthChangeAnim = spring;
        spring.setMinimumVisibleChange(0.001f);
        this.mWidthChangeAnim.addUpdateListener(new AnonymousClass5());
        this.mWidthChangeAnim.addEndListener(new AnonymousClass6());
    }

    private boolean shouldSkipSlideDispatch(float f3, float f4) {
        View viewFindChildViewUnder;
        if (!this.mDispatchItemClickWhenAlreadySelected || (viewFindChildViewUnder = findChildViewUnder(f3, f4)) == null || !viewFindChildViewUnder.isEnabled() || viewFindChildViewUnder.getId() != this.mSelectedItemId) {
            return false;
        }
        int i3 = this.mTouchDownItemId;
        if (i3 == -1 || i3 == viewFindChildViewUnder.getId()) {
            return !this.mHasSlidOffTouchDownItem;
        }
        return true;
    }

    private void updateEdgeBounds() {
        if (COUIMaterialStateManager.INSTANCE.isBlurEffectEnable()) {
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

    @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView
    public void addItems(List<? extends BaseBottomFloatingItem> list) {
        int i3 = this.mSelectedItemId;
        COUISpringAnimation cOUISpringAnimation = this.mWidthChangeAnim;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            this.mWidthChangeAnim.cancel();
        }
        removeAllViews();
        this.mSelectedItemId = -1;
        this.mNavigationBarItems.clear();
        if (list != null && !list.isEmpty()) {
            for (int i4 = 0; i4 < list.size(); i4++) {
                BaseBottomFloatingItem baseBottomFloatingItem = list.get(i4);
                if (baseBottomFloatingItem instanceof COUIBottomFloatingNavigationBarItem) {
                    addItem((COUIBottomFloatingNavigationBarItem) baseBottomFloatingItem, i4);
                }
            }
            if (i3 != -1) {
                Iterator<? extends BaseBottomFloatingItem> it = list.iterator();
                while (it.hasNext()) {
                    if (it.next().getId() == i3) {
                        setSelectedItem(i3);
                    }
                }
                BaseBottomFloatingItem baseBottomFloatingItem2 = list.get(0);
                if (baseBottomFloatingItem2 instanceof COUIBottomFloatingNavigationBarItem) {
                    setSelectedItem(baseBottomFloatingItem2.getId());
                }
            } else {
                BaseBottomFloatingItem baseBottomFloatingItem3 = list.get(0);
                if (baseBottomFloatingItem3 instanceof COUIBottomFloatingNavigationBarItem) {
                    setSelectedItem(baseBottomFloatingItem3.getId());
                }
            }
        }
        applyUniformItemTitleTextColors();
    }

    @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView, android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (!this.mIsMenuVisible) {
            return false;
        }
        float x2 = motionEvent.getX();
        float y2 = motionEvent.getY();
        int action = motionEvent.getAction();
        if (action == 0 && !isTouchInBounds(x2, y2)) {
            return false;
        }
        if (action == 0) {
            this.mPendingSlideSelectItemId = -1;
            View viewFindChildViewUnder = findChildViewUnder(x2, y2);
            this.mTouchDownItemId = viewFindChildViewUnder != null ? viewFindChildViewUnder.getId() : -1;
            this.mHasSlidOffTouchDownItem = false;
        }
        boolean zDispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        if (action != 1 && action != 3) {
            return zDispatchTouchEvent;
        }
        dispatchPendingSlideItemClickIfNeeded(x2, y2, action == 3);
        this.mPendingSlideSelectItemId = -1;
        this.mTouchDownItemId = -1;
        this.mHasSlidOffTouchDownItem = false;
        return zDispatchTouchEvent;
    }

    public void doMenuViewShowOrHideAnim(boolean z2) {
        this.mIsMenuVisible = z2;
        if (z2) {
            setVisibility(0);
        }
        this.mAlphaAnim.setStartValue(getAlpha() * 10000.0f);
        this.mAlphaAnim.animateToFinalPosition(z2 ? 10000.0f : 0.0f);
        this.mScaleAnim.setStartValue(getScaleX() * 10000.0f);
        this.mScaleAnim.animateToFinalPosition(z2 ? 10000.0f : 8000.0f);
        if (COUIMaterialStateManager.INSTANCE.isBlurEffectEnable()) {
            this.mForegroundBlurRadiusAnim.setStartValue(this.mForegroundBlurRadius * 10000.0f);
            this.mForegroundBlurRadiusAnim.animateToFinalPosition(z2 ? 0.0f : 300000.0f);
        }
        OnMenuViewShowHideAnimListener onMenuViewShowHideAnimListener = this.mOnMenuViewShowHideAnimListener;
        if (onMenuViewShowHideAnimListener != null) {
            onMenuViewShowHideAnimListener.onMenuViewShowHideAnimStart(z2);
        }
    }

    public COUIBottomFloatingToolbarItemView getLargeIconItemView() {
        for (int i3 = 0; i3 < getChildCount(); i3++) {
            View childAt = getChildAt(i3);
            if (childAt instanceof COUIBottomFloatingToolbarItemView) {
                COUIBottomFloatingToolbarItemView cOUIBottomFloatingToolbarItemView = (COUIBottomFloatingToolbarItemView) childAt;
                if (cOUIBottomFloatingToolbarItemView.getItemStyle() == 2) {
                    return cOUIBottomFloatingToolbarItemView;
                }
            }
        }
        return null;
    }

    public int getSelectedItemId() {
        return this.mSelectedItemId;
    }

    public boolean isBoundsAnimationRunning() {
        COUISpringAnimation cOUISpringAnimation = this.mHorizontalMoveAnim;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            return true;
        }
        COUISpringAnimation cOUISpringAnimation2 = this.mWidthChangeAnim;
        return cOUISpringAnimation2 != null && cOUISpringAnimation2.isRunning();
    }

    public boolean isSupportMeltBall() {
        COUIMaterialStateManager cOUIMaterialStateManager = COUIMaterialStateManager.INSTANCE;
        return !cOUIMaterialStateManager.isLowAnimationLevel() && cOUIMaterialStateManager.materialVersionCheck() && cOUIMaterialStateManager.checkThemeEnable(getContext()) && !this.mIsDrawableMaterialEffect;
    }

    @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        updateEdgeBounds();
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        AccessibilityNodeInfoCompat.wrap(accessibilityNodeInfo).setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(1, getVisibleChildCount(), false, 1));
    }

    @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView
    public void onMenuTouchMove(float f3, float f4, boolean z2) {
        View viewFindChildViewUnder;
        if (z2 && (viewFindChildViewUnder = findChildViewUnder(f3, f4)) != null && viewFindChildViewUnder.isEnabled()) {
            if (this.mTouchDownItemId != -1 && viewFindChildViewUnder.getId() != this.mTouchDownItemId) {
                this.mHasSlidOffTouchDownItem = true;
            }
            int id = viewFindChildViewUnder.getId();
            if (id == this.mSelectedItemId) {
                return;
            }
            setSelectedItem(id);
            this.mPendingSlideSelectItemId = id;
        }
    }

    @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView
    public void refreshThemeAppearance() {
        super.refreshThemeAppearance();
        for (int i3 = 0; i3 < getChildCount(); i3++) {
            View childAt = getChildAt(i3);
            if (childAt instanceof COUIBottomFloatingToolbarItemView) {
                ((COUIBottomFloatingToolbarItemView) childAt).refreshThemeAppearance();
            }
        }
    }

    public void setDispatchItemClickWhenAlreadySelected(boolean z2) {
        this.mDispatchItemClickWhenAlreadySelected = z2;
    }

    public void setOnBoundsAnimationsEndAction(Runnable runnable) {
        this.mOnBoundsAnimationsEndAction = runnable;
    }

    public void setOnMenuViewShowHideAnimListener(OnMenuViewShowHideAnimListener onMenuViewShowHideAnimListener) {
        this.mOnMenuViewShowHideAnimListener = onMenuViewShowHideAnimListener;
    }

    public void setSelectedItem(int i3) {
        if (this.mSelectedItemId == i3) {
            return;
        }
        this.mSelectedItemId = i3;
        int childCount = getChildCount();
        for (int i4 = 0; i4 < childCount; i4++) {
            View childAt = getChildAt(i4);
            if (childAt instanceof COUIBottomFloatingToolbarItemView) {
                childAt.setSelected(childAt.getId() == i3);
            }
        }
    }

    @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView
    public void setupItemAccessibility(View view, int i3) {
        ViewCompat.setAccessibilityDelegate(view, new AnonymousClass10(i3));
    }

    public void updateRealBounds(int i3, int i4, int i5, int i6, boolean z2) {
        RectF rectF = this.mRealBounds;
        float f3 = i3;
        if (rectF.left == f3 && rectF.top == i4 && rectF.right == i5 && rectF.bottom == i6) {
            return;
        }
        if (!rectF.isEmpty() && z2) {
            doHorizontalMoveAnim(i3, i5);
            doWidthChangeAnim(i5 - i3);
            this.mRealBounds.set(f3, i4, i5, i6);
            return;
        }
        float f4 = i4;
        float f5 = i5;
        float f6 = i6;
        this.mRealBounds.set(f3, f4, f5, f6);
        COUISpringAnimation cOUISpringAnimation = this.mHorizontalMoveAnim;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            this.mHorizontalMoveAnim.cancel();
        }
        COUISpringAnimation cOUISpringAnimation2 = this.mWidthChangeAnim;
        if (cOUISpringAnimation2 != null && cOUISpringAnimation2.isRunning()) {
            this.mWidthChangeAnim.cancel();
        }
        for (int i7 = 0; i7 < getChildCount(); i7++) {
            getChildAt(i7).setTranslationX(0.0f);
        }
        this.mDrawingBounds.set(f3, f4, f5, f6);
        if (isLayoutRtl()) {
            setX(this.mDrawingBounds.right - getWidth());
        } else {
            setX(this.mDrawingBounds.left);
        }
        notifyDrawingBoundsChanged();
        invalidate();
    }

    public COUIBottomFloatingNavigationBarMenuView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIBottomFloatingNavigationBarMenuView(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0);
    }

    public COUIBottomFloatingNavigationBarMenuView(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mTargetItemCenterPointX = new ArrayList<>();
        this.mRealBounds = new RectF();
        this.mSelectedItemId = -1;
        this.mNavigationBarItems = new ArrayList<>();
        this.mPendingSlideSelectItemId = -1;
        this.mTouchDownItemId = -1;
        this.mIsMenuVisible = true;
        this.mForegroundBlurRadius = 0.0f;
        initNaviMenuAnim();
    }
}