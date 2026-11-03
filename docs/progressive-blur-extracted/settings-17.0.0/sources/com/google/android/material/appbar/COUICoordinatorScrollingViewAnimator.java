package com.google.android.material.appbar;

import android.view.View;
import com.coui.appcompat.log.COUILog;

/* JADX INFO: loaded from: classes8.dex */
class COUICoordinatorScrollingViewAnimator {
    private static final boolean DEBUG;
    private static final int DEFAULT_BASE_OFFSET = 0;
    private static final String TAG = "COUICoordinatorAnim";
    private int mBaseOffset;
    private final View mChild;
    private final COUIViewOffsetHelper mChildHelper;
    private final View mDependency;
    private final COUIViewOffsetHelper mDependencyHelper;
    private final COUIViewOffsetHelper.OnOffsetChangedListener mDependencyOffsetListener = new AnonymousClass1();
    private boolean mInitialized;
    private final OffsetParamsProvider mProvider;

    /* JADX INFO: renamed from: com.google.android.material.appbar.COUICoordinatorScrollingViewAnimator$1, reason: invalid class name */
    public class AnonymousClass1 implements COUIViewOffsetHelper.OnOffsetChangedListener {
        public AnonymousClass1() {
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetHelper.OnOffsetChangedListener
        public void onOffsetAnimating(COUIViewOffsetHelper cOUIViewOffsetHelper, int i3, float f3, float f4) {
            COUICoordinatorScrollingViewAnimator.this.animateChildOffset(f3, f4);
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetHelper.OnOffsetChangedListener
        public void onOffsetChanged(COUIViewOffsetHelper cOUIViewOffsetHelper, int i3, boolean z2) {
            if (!z2 && COUICoordinatorScrollingViewAnimator.access$000(COUICoordinatorScrollingViewAnimator.this).isAnimatingOffset()) {
                COUICoordinatorScrollingViewAnimator.access$000(COUICoordinatorScrollingViewAnimator.this).cancelAnimateOffset();
            }
            COUICoordinatorScrollingViewAnimator.access$100(COUICoordinatorScrollingViewAnimator.this);
        }
    }

    public interface OffsetParamsProvider {
        int getOffsetDelta();

        int getOverlapPixels();

        int getVerticalLayoutGap();
    }

    static {
        DEBUG = COUILog.LOG_DEBUG || COUILog.isLoggable(TAG, 3);
    }

    public COUICoordinatorScrollingViewAnimator(View view, View view2, COUIViewOffsetHelper cOUIViewOffsetHelper, COUIViewOffsetHelper cOUIViewOffsetHelper2, OffsetParamsProvider offsetParamsProvider) {
        this.mDependency = view;
        this.mChild = view2;
        this.mDependencyHelper = cOUIViewOffsetHelper;
        this.mChildHelper = cOUIViewOffsetHelper2;
        this.mProvider = offsetParamsProvider;
    }

    public static /* synthetic */ COUIViewOffsetHelper access$000(COUICoordinatorScrollingViewAnimator cOUICoordinatorScrollingViewAnimator) {
        return cOUICoordinatorScrollingViewAnimator.mChildHelper;
    }

    public static /* synthetic */ void access$100(COUICoordinatorScrollingViewAnimator cOUICoordinatorScrollingViewAnimator) {
        cOUICoordinatorScrollingViewAnimator.applyChildOffsetUnchecked();
    }

    private void applyChildOffsetUnchecked() {
        int iComputeChildTarget = computeChildTarget();
        this.mChildHelper.setTopAndBottomOffset(iComputeChildTarget);
        if (DEBUG) {
            COUILog.d(TAG, "applyChildOffsetUnchecked target=" + iComputeChildTarget + ", child.getTop()=" + this.mChild.getTop() + ", child.getBottom()=" + this.mChild.getBottom());
        }
    }

    private int computeChildTarget() {
        if (DEBUG) {
            COUILog.d(TAG, "computeChildTarget mBaseOffset=" + this.mBaseOffset + ", dependencyOffsetTop=" + this.mDependencyHelper.getTopAndBottomOffset() + ", getOffsetDelta=" + this.mProvider.getOffsetDelta() + ", getVerticalLayoutGap=" + this.mProvider.getVerticalLayoutGap() + ", getOverlapPixels=" + this.mProvider.getOverlapPixels());
        }
        return (((this.mBaseOffset + this.mDependencyHelper.getTopAndBottomOffset()) + this.mProvider.getOffsetDelta()) + this.mProvider.getVerticalLayoutGap()) - this.mProvider.getOverlapPixels();
    }

    public void animateChildOffset(float f3, float f4) {
        if (this.mInitialized) {
            int iComputeChildTarget = computeChildTarget();
            this.mChildHelper.animateTopAndBottomOffset(iComputeChildTarget, f3, f4, false);
            if (DEBUG) {
                COUILog.d(TAG, "animateChildOffset target=" + iComputeChildTarget + ", bounce=" + f3 + ", response=" + f4 + ", child.getTop()=" + this.mChild.getTop());
            }
        }
    }

    public void applyChildOffset() {
        if (this.mInitialized && !this.mChildHelper.isAnimatingOffset()) {
            int iComputeChildTarget = computeChildTarget();
            this.mChildHelper.setOffsetTopValue(iComputeChildTarget);
            this.mChildHelper.applyOffsets();
            if (DEBUG) {
                COUILog.d(TAG, "applyChildOffset target=" + iComputeChildTarget + ", child.getTop()=" + this.mChild.getTop() + ", child.getBottom()=" + this.mChild.getBottom());
            }
        }
    }

    public void initialize() {
        if (this.mInitialized) {
            return;
        }
        this.mBaseOffset = 0;
        if (DEBUG) {
            COUILog.d(TAG, "initialize baseOffset=" + this.mBaseOffset + ", dependency.getBottom()=" + this.mDependency.getBottom() + ", dependencyOffsetTop=" + this.mDependencyHelper.getTopAndBottomOffset() + ", childLayoutTop=" + this.mChildHelper.getLayoutTop());
        }
        updateChildLayoutTop(this.mDependency);
        this.mDependencyHelper.addOnOffsetChangedListener(this.mDependencyOffsetListener);
        this.mInitialized = true;
        applyChildOffset();
    }

    public boolean isInitialized() {
        return this.mInitialized;
    }

    public void release() {
        if (this.mInitialized) {
            this.mDependencyHelper.removeOnOffsetChangedListener(this.mDependencyOffsetListener);
            this.mChildHelper.cancelAnimateOffset();
            this.mInitialized = false;
        }
    }

    public void updateChildLayoutTop(View view) {
        this.mChildHelper.onUpdateViewLayout(view, this.mDependencyHelper.getLayoutTop());
    }
}