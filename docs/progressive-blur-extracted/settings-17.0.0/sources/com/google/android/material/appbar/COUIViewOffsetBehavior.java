package com.google.android.material.appbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.coui.appcompat.log.COUILog;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
class COUIViewOffsetBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
    private static final boolean DEBUG;
    private static final String TAG = "COUIViewOffsetBehavior";
    COUIViewOffsetHelper mViewOffsetHelper;
    private int tempLeftRightOffset;
    private int tempTopBottomOffset;

    static {
        DEBUG = COUILog.LOG_DEBUG || COUILog.isLoggable(TAG, 3);
    }

    public COUIViewOffsetBehavior() {
        this.tempTopBottomOffset = 0;
        this.tempLeftRightOffset = 0;
    }

    private View findAppBarDependency(CoordinatorLayout coordinatorLayout, View view) {
        List<View> dependencies = coordinatorLayout.getDependencies(view);
        int size = dependencies.size();
        for (int i3 = 0; i3 < size; i3++) {
            View view2 = dependencies.get(i3);
            if ((view2 instanceof COUIAppBarLayout) || (view2 instanceof AppBarLayout)) {
                return view2;
            }
        }
        return null;
    }

    public boolean animateTopAndBottomOffset(int i3, float f3, float f4, boolean z2) {
        COUIViewOffsetHelper cOUIViewOffsetHelper = this.mViewOffsetHelper;
        if (cOUIViewOffsetHelper != null) {
            return cOUIViewOffsetHelper.animateTopAndBottomOffset(i3, f3, f4, z2);
        }
        this.tempTopBottomOffset = i3;
        return false;
    }

    public void cancelAnimateOffset() {
        COUIViewOffsetHelper cOUIViewOffsetHelper = this.mViewOffsetHelper;
        if (cOUIViewOffsetHelper != null) {
            cOUIViewOffsetHelper.cancelAnimateOffset();
        }
    }

    public int getAnimateTopAndBottomOffset() {
        COUIViewOffsetHelper cOUIViewOffsetHelper = this.mViewOffsetHelper;
        if (cOUIViewOffsetHelper != null) {
            return cOUIViewOffsetHelper.getAnimateTopAndBottomOffset();
        }
        return 0;
    }

    public int getLeftAndRightOffset() {
        COUIViewOffsetHelper cOUIViewOffsetHelper = this.mViewOffsetHelper;
        if (cOUIViewOffsetHelper != null) {
            return cOUIViewOffsetHelper.getLeftAndRightOffset();
        }
        return 0;
    }

    public int getTempLeftRightOffset() {
        return this.tempLeftRightOffset;
    }

    public int getTempTopBottomOffset() {
        return this.tempTopBottomOffset;
    }

    public int getTopAndBottomOffset() {
        COUIViewOffsetHelper cOUIViewOffsetHelper = this.mViewOffsetHelper;
        if (cOUIViewOffsetHelper != null) {
            return cOUIViewOffsetHelper.getTopAndBottomOffset();
        }
        return 0;
    }

    public boolean isAnimatingOffset() {
        COUIViewOffsetHelper cOUIViewOffsetHelper = this.mViewOffsetHelper;
        return cOUIViewOffsetHelper != null && cOUIViewOffsetHelper.isAnimatingOffset();
    }

    public boolean isHorizontalOffsetEnabled() {
        COUIViewOffsetHelper cOUIViewOffsetHelper = this.mViewOffsetHelper;
        return cOUIViewOffsetHelper != null && cOUIViewOffsetHelper.isHorizontalOffsetEnabled();
    }

    public boolean isVerticalOffsetEnabled() {
        COUIViewOffsetHelper cOUIViewOffsetHelper = this.mViewOffsetHelper;
        return cOUIViewOffsetHelper != null && cOUIViewOffsetHelper.isVerticalOffsetEnabled();
    }

    public void layoutChild(CoordinatorLayout coordinatorLayout, V v2, int i3) {
        coordinatorLayout.onLayoutChild(v2, i3);
    }

    public boolean layoutRequestTopAndBottomOffset(int i3) {
        COUIViewOffsetHelper cOUIViewOffsetHelper = this.mViewOffsetHelper;
        if (cOUIViewOffsetHelper != null) {
            return cOUIViewOffsetHelper.layoutRequestTopAndBottomOffset(i3);
        }
        this.tempTopBottomOffset = i3;
        return false;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, V v2, int i3) {
        boolean z2 = DEBUG;
        if (z2) {
            COUILog.d(TAG, "COUIViewOffsetBehavior.onLayoutChild child Bottom = " + v2.getBottom() + " SimpleName = " + v2.getClass().getSimpleName());
        }
        layoutChild(coordinatorLayout, v2, i3);
        if (this.mViewOffsetHelper == null) {
            this.mViewOffsetHelper = new COUIViewOffsetHelper(v2);
        }
        this.mViewOffsetHelper.onViewLayout();
        if (this.mViewOffsetHelper.isAnimatingOffset()) {
            COUIViewOffsetHelper cOUIViewOffsetHelper = this.mViewOffsetHelper;
            cOUIViewOffsetHelper.applyOffsets(cOUIViewOffsetHelper.getAnimateTopAndBottomOffset());
        } else {
            this.mViewOffsetHelper.applyOffsets();
        }
        int i4 = this.tempTopBottomOffset;
        if (i4 != 0) {
            this.mViewOffsetHelper.setTopAndBottomOffset(i4);
            this.tempTopBottomOffset = 0;
        }
        int i5 = this.tempLeftRightOffset;
        if (i5 != 0) {
            this.mViewOffsetHelper.setLeftAndRightOffset(i5);
            this.tempLeftRightOffset = 0;
        }
        if (!z2) {
            return true;
        }
        COUILog.d(TAG, "after COUIViewOffsetBehavior.onLayoutChild child Bottom = " + v2.getBottom() + " SimpleName = " + v2.getClass().getSimpleName());
        return true;
    }

    public void setHorizontalOffsetEnabled(boolean z2) {
        COUIViewOffsetHelper cOUIViewOffsetHelper = this.mViewOffsetHelper;
        if (cOUIViewOffsetHelper != null) {
            cOUIViewOffsetHelper.setHorizontalOffsetEnabled(z2);
        }
    }

    public boolean setLeftAndRightOffset(int i3) {
        COUIViewOffsetHelper cOUIViewOffsetHelper = this.mViewOffsetHelper;
        if (cOUIViewOffsetHelper != null) {
            return cOUIViewOffsetHelper.setLeftAndRightOffset(i3);
        }
        this.tempLeftRightOffset = i3;
        return false;
    }

    public void setOnAnimateUpdateListener(COUIViewOffsetHelper.OnAnimateUpdateListener onAnimateUpdateListener) {
        COUIViewOffsetHelper cOUIViewOffsetHelper = this.mViewOffsetHelper;
        if (cOUIViewOffsetHelper != null) {
            cOUIViewOffsetHelper.setOnAnimateUpdateListener(onAnimateUpdateListener);
        }
    }

    public void setTempLeftRightOffset(int i3) {
        this.tempLeftRightOffset = i3;
    }

    public void setTempTopBottomOffset(int i3) {
        this.tempTopBottomOffset = i3;
    }

    public boolean setTopAndBottomOffset(int i3) {
        COUIViewOffsetHelper cOUIViewOffsetHelper = this.mViewOffsetHelper;
        if (cOUIViewOffsetHelper != null) {
            return cOUIViewOffsetHelper.setTopAndBottomOffset(i3);
        }
        this.tempTopBottomOffset = i3;
        return false;
    }

    public void setVerticalOffsetEnabled(boolean z2) {
        COUIViewOffsetHelper cOUIViewOffsetHelper = this.mViewOffsetHelper;
        if (cOUIViewOffsetHelper != null) {
            cOUIViewOffsetHelper.setVerticalOffsetEnabled(z2);
        }
    }

    public COUIViewOffsetBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.tempTopBottomOffset = 0;
        this.tempLeftRightOffset = 0;
    }
}