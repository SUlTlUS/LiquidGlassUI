package com.google.android.material.appbar;

import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.log.COUILog;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
class COUIViewOffsetHelper {
    private static final boolean DEBUG;
    private static final float DEFAULT_SPRING_BOUNCE = 0.0f;
    private static final float DEFAULT_SPRING_RESPONSE = 0.25f;
    private static final String TAG = "COUIViewOffsetHelper";
    private OnAnimateUpdateListener mAnimateUpdateListener;
    private List<OnOffsetChangedListener> mOffsetChangedListeners;
    private COUISpringAnimation mScrollSpringAnimation;
    private FloatValueHolder mScrollValueHolder;
    private final View view;
    private boolean verticalOffsetEnabled = true;
    private boolean horizontalOffsetEnabled = true;
    private int offsetTop = 0;
    private int offsetLeft = 0;
    private int layoutTop = 0;
    private int layoutLeft = 0;
    private int mAnimateOffsetTop = 0;

    /* JADX INFO: loaded from: classes8.dex */
    public interface OnAnimateUpdateListener {
        void onAnimateUpdate(COUIViewOffsetHelper cOUIViewOffsetHelper, int i3);
    }

    /* JADX INFO: loaded from: classes8.dex */
    public interface OnOffsetChangedListener {
        void onOffsetAnimating(COUIViewOffsetHelper cOUIViewOffsetHelper, int i3, float f3, float f4);

        void onOffsetChanged(COUIViewOffsetHelper cOUIViewOffsetHelper, int i3, boolean z2);
    }

    static {
        DEBUG = COUILog.LOG_DEBUG || COUILog.isLoggable(TAG, 3);
    }

    public COUIViewOffsetHelper(View view) {
        this.view = view;
    }

    public static /* synthetic */ void a(COUIViewOffsetHelper cOUIViewOffsetHelper, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        cOUIViewOffsetHelper.getClass();
        int iRound = Math.round(f3);
        if (DEBUG) {
            COUILog.d(TAG, "spring frameOffset: " + iRound + ", SimpleName: " + cOUIViewOffsetHelper.view.getClass().getSimpleName());
        }
        cOUIViewOffsetHelper.applyOffsets(iRound);
        OnAnimateUpdateListener onAnimateUpdateListener = cOUIViewOffsetHelper.mAnimateUpdateListener;
        if (onAnimateUpdateListener != null) {
            onAnimateUpdateListener.onAnimateUpdate(cOUIViewOffsetHelper, iRound);
        }
    }

    public static /* synthetic */ void b(COUIViewOffsetHelper cOUIViewOffsetHelper, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        if (z2) {
            cOUIViewOffsetHelper.getClass();
        } else {
            cOUIViewOffsetHelper.applyOffsets(cOUIViewOffsetHelper.offsetTop);
        }
    }

    private void dispatchOffsetAnimating(int i3, float f3, float f4) {
        List<OnOffsetChangedListener> list = this.mOffsetChangedListeners;
        if (list == null) {
            return;
        }
        int size = list.size();
        for (int i4 = 0; i4 < size; i4++) {
            OnOffsetChangedListener onOffsetChangedListener = this.mOffsetChangedListeners.get(i4);
            if (onOffsetChangedListener != null) {
                onOffsetChangedListener.onOffsetAnimating(this, i3, f3, f4);
            }
        }
    }

    private void dispatchOffsetChanged(int i3, boolean z2) {
        List<OnOffsetChangedListener> list = this.mOffsetChangedListeners;
        if (list == null) {
            return;
        }
        int size = list.size();
        for (int i4 = 0; i4 < size; i4++) {
            OnOffsetChangedListener onOffsetChangedListener = this.mOffsetChangedListeners.get(i4);
            if (onOffsetChangedListener != null) {
                onOffsetChangedListener.onOffsetChanged(this, i3, z2);
            }
        }
    }

    private void ensureSpringAnimation() {
        if (this.mScrollSpringAnimation != null) {
            return;
        }
        if (this.mScrollValueHolder == null) {
            this.mScrollValueHolder = new FloatValueHolder();
        }
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setResponse(0.25f);
        cOUISpringForce.setBounce(0.0f);
        COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(this.mScrollValueHolder);
        this.mScrollSpringAnimation = cOUISpringAnimation;
        cOUISpringAnimation.setSpring(cOUISpringForce);
        this.mScrollSpringAnimation.addEndListener(new n(this));
        this.mScrollSpringAnimation.addUpdateListener(new o(this));
    }

    public void addOnOffsetChangedListener(OnOffsetChangedListener onOffsetChangedListener) {
        if (this.mOffsetChangedListeners == null) {
            this.mOffsetChangedListeners = new ArrayList();
        }
        if (onOffsetChangedListener == null || this.mOffsetChangedListeners.contains(onOffsetChangedListener)) {
            return;
        }
        this.mOffsetChangedListeners.add(onOffsetChangedListener);
    }

    public boolean animateTopAndBottomOffset(int i3, float f3, float f4, boolean z2) {
        if (DEBUG) {
            COUILog.d(TAG, "animateTopAndBottomOffset: offset=" + i3 + ", bounce=" + f3 + ", response=" + f4 + ", dispatchAnimating=" + z2 + ", verticalOffsetEnabled=" + this.verticalOffsetEnabled + ", offsetTop=" + this.offsetTop + ", animateOffsetTop=" + this.mAnimateOffsetTop + ", SimpleName: " + this.view.getClass().getSimpleName());
        }
        if (!this.verticalOffsetEnabled || this.offsetTop == i3) {
            return false;
        }
        ensureSpringAnimation();
        this.mScrollSpringAnimation.setStartValue(this.mAnimateOffsetTop);
        this.mScrollSpringAnimation.getSpring().setBounce(f3).setResponse(f4);
        this.mScrollSpringAnimation.animateToFinalPosition(i3);
        this.offsetTop = i3;
        if (z2) {
            dispatchOffsetAnimating(i3, f3, f4);
            return true;
        }
        dispatchOffsetChanged(i3, false);
        return true;
    }

    public void applyOffsets() {
        applyOffsets(this.offsetTop);
    }

    public void cancelAnimateOffset() {
        if (DEBUG) {
            COUILog.d(TAG, "cancelAnimateOffset");
        }
        COUISpringAnimation cOUISpringAnimation = this.mScrollSpringAnimation;
        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) {
            return;
        }
        this.mScrollSpringAnimation.cancel();
    }

    public int getAnimateTopAndBottomOffset() {
        return this.mAnimateOffsetTop;
    }

    public int getLayoutLeft() {
        return this.layoutLeft;
    }

    public int getLayoutTop() {
        return this.layoutTop;
    }

    public int getLeftAndRightOffset() {
        return this.offsetLeft;
    }

    public int getTopAndBottomOffset() {
        return this.offsetTop;
    }

    public boolean isAnimatingOffset() {
        COUISpringAnimation cOUISpringAnimation = this.mScrollSpringAnimation;
        return cOUISpringAnimation != null && cOUISpringAnimation.isRunning();
    }

    public boolean isHorizontalOffsetEnabled() {
        return this.horizontalOffsetEnabled;
    }

    public boolean isVerticalOffsetEnabled() {
        return this.verticalOffsetEnabled;
    }

    public boolean layoutRequestTopAndBottomOffset(int i3) {
        if (DEBUG) {
            COUILog.d(TAG, "layoutRequestTopAndBottomOffset offset  : " + i3 + ", SimpleName: " + this.view.getClass().getSimpleName());
        }
        if (!this.verticalOffsetEnabled || this.offsetTop == i3) {
            dispatchOffsetChanged(i3, true);
            return false;
        }
        this.offsetTop = i3;
        applyOffsets();
        dispatchOffsetChanged(i3, true);
        return true;
    }

    public final void onUpdateViewLayout(View view, int i3) {
        int height = view.getHeight() + i3;
        if (height != this.layoutTop) {
            this.layoutTop = height;
        }
        if (DEBUG) {
            COUILog.d(TAG, "onUpdateViewLayout dependencyHeader.getHeight(): " + view.getHeight() + " headerLayoutTop: " + i3 + " layoutTop: " + height + " layoutLeft: " + this.layoutLeft + " SimpleName = " + this.view.getClass().getSimpleName());
        }
    }

    public final void onViewLayout(COUIAppBarLayout cOUIAppBarLayout, int i3) {
        this.layoutTop = cOUIAppBarLayout.getHeight() + i3;
        if (DEBUG) {
            COUILog.d(TAG, "onViewLayout dependencyHeader.getHeight(): " + cOUIAppBarLayout.getHeight() + " headerLayoutTop: " + i3 + " getTop: " + this.view.getTop() + " SimpleName = " + this.view.getClass().getSimpleName());
        }
        this.layoutLeft = this.view.getLeft();
    }

    public void removeOnOffsetChangedListener(OnOffsetChangedListener onOffsetChangedListener) {
        List<OnOffsetChangedListener> list = this.mOffsetChangedListeners;
        if (list == null || onOffsetChangedListener == null) {
            return;
        }
        list.remove(onOffsetChangedListener);
    }

    public void setHorizontalOffsetEnabled(boolean z2) {
        this.horizontalOffsetEnabled = z2;
    }

    public boolean setLeftAndRightOffset(int i3) {
        if (!this.horizontalOffsetEnabled || this.offsetLeft == i3) {
            return false;
        }
        this.offsetLeft = i3;
        applyOffsets();
        return true;
    }

    public void setOffsetTopValue(int i3) {
        if (DEBUG) {
            COUILog.d(TAG, "setOffsetTopValue offsetTop : " + i3 + " animateOffsetTop : " + this.mAnimateOffsetTop);
        }
        this.offsetTop = i3;
    }

    public void setOnAnimateUpdateListener(OnAnimateUpdateListener onAnimateUpdateListener) {
        this.mAnimateUpdateListener = onAnimateUpdateListener;
    }

    @Deprecated
    public void setOnOffsetChangedListener(OnOffsetChangedListener onOffsetChangedListener) {
        if (this.mOffsetChangedListeners == null) {
            this.mOffsetChangedListeners = new ArrayList();
        }
        if (onOffsetChangedListener == null || this.mOffsetChangedListeners.contains(onOffsetChangedListener)) {
            return;
        }
        this.mOffsetChangedListeners.add(onOffsetChangedListener);
    }

    public boolean setTopAndBottomOffset(int i3) {
        if (DEBUG) {
            COUILog.d(TAG, "setTopAndBottomOffset offset  : " + i3 + ", SimpleName: " + this.view.getClass().getSimpleName());
        }
        if (!this.verticalOffsetEnabled || this.offsetTop == i3) {
            return false;
        }
        this.offsetTop = i3;
        applyOffsets();
        dispatchOffsetChanged(i3, false);
        return true;
    }

    public void setTopAndBottomOffsetDirectly() {
        if (DEBUG) {
            COUILog.d(TAG, "setTopAndBottomOffsetDirectly offsetTop : " + this.offsetTop + " animateOffsetTop : " + this.mAnimateOffsetTop);
        }
        applyOffsets();
        dispatchOffsetChanged(this.offsetTop, false);
    }

    public void setVerticalOffsetEnabled(boolean z2) {
        this.verticalOffsetEnabled = z2;
    }

    public void applyOffsets(int i3) {
        boolean z2 = DEBUG;
        if (z2) {
            COUILog.d(TAG, "before applyOffsets layoutTop: " + this.layoutTop + " frameOffsetTop: " + i3 + " getBottom: " + this.view.getBottom() + " getTop: " + this.view.getTop() + " SimpleName = " + this.view.getClass().getSimpleName());
        }
        View view = this.view;
        ViewCompat.offsetTopAndBottom(view, i3 - (view.getTop() - this.layoutTop));
        View view2 = this.view;
        ViewCompat.offsetLeftAndRight(view2, this.offsetLeft - (view2.getLeft() - this.layoutLeft));
        this.mAnimateOffsetTop = i3;
        if (z2) {
            COUILog.d(TAG, "after applyOffsets getBottom: " + this.view.getBottom() + " getTop: " + this.view.getTop() + " SimpleName = " + this.view.getClass().getSimpleName());
        }
    }

    public void onViewLayout() {
        this.layoutTop = this.view.getTop();
        this.layoutLeft = this.view.getLeft();
        if (DEBUG) {
            COUILog.d(TAG, "onViewLayout layoutTop: " + this.layoutTop + " SimpleName = " + this.view.getClass().getSimpleName());
        }
    }
}