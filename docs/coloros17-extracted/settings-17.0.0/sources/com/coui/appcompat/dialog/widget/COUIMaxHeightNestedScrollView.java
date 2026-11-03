package com.coui.appcompat.dialog.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.coui.appcompat.scrollview.COUINestedScrollView;
import com.support.scrollview.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIMaxHeightNestedScrollView extends COUINestedScrollView {
    private ConfigChangeListener mConfigChangeListener;
    private final int mInitialMaxHeightFromAttrs;
    private final int mInitialMinHeightFromAttrs;
    private boolean mInterceptWhenCannotScroll;
    private int mMinHeight;
    private int mScrollViewMaxHeightAttrResId;
    private int mScrollViewMinHeightAttrResId;
    private int maxHeight;

    /* JADX INFO: renamed from: com.coui.appcompat.dialog.widget.COUIMaxHeightNestedScrollView$1, reason: invalid class name */
    public class AnonymousClass1 implements Runnable {
        public AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUIMaxHeightNestedScrollView.this.requestLayout();
        }
    }

    public interface ConfigChangeListener {
        void onChange();
    }

    public COUIMaxHeightNestedScrollView(Context context) {
        this(context, null);
    }

    private void refreshScrollViewDimensionsFromResources() {
        if (this.mScrollViewMaxHeightAttrResId != 0) {
            this.maxHeight = getContext().getResources().getDimensionPixelSize(this.mScrollViewMaxHeightAttrResId);
        } else {
            this.maxHeight = this.mInitialMaxHeightFromAttrs;
        }
        if (this.mScrollViewMinHeightAttrResId != 0) {
            this.mMinHeight = getContext().getResources().getDimensionPixelSize(this.mScrollViewMinHeightAttrResId);
        } else {
            this.mMinHeight = this.mInitialMinHeightFromAttrs;
        }
    }

    public boolean isInterceptWhenCannotScroll() {
        return this.mInterceptWhenCannotScroll;
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        refreshScrollViewDimensionsFromResources();
        ConfigChangeListener configChangeListener = this.mConfigChangeListener;
        if (configChangeListener != null) {
            configChangeListener.onChange();
        }
    }

    @Override // com.coui.appcompat.scrollview.COUINestedScrollView, androidx.core.widget.NestedScrollView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!this.mInterceptWhenCannotScroll || canScrollVertically(-1) || canScrollVertically(1)) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        return false;
    }

    @Override // androidx.core.widget.NestedScrollView, android.widget.FrameLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        super.onMeasure(i3, i4);
        int measuredHeight = getMeasuredHeight();
        int i5 = this.mMinHeight;
        if (i5 > 0) {
            measuredHeight = Math.max(measuredHeight, i5);
        }
        int i6 = this.maxHeight;
        if (i6 > 0) {
            measuredHeight = Math.min(i6, measuredHeight);
        }
        if (measuredHeight != getMeasuredHeight()) {
            super.onMeasure(i3, View.MeasureSpec.makeMeasureSpec(Math.min(this.maxHeight, measuredHeight), 1073741824));
        }
    }

    public void setConfigChangeListener(ConfigChangeListener configChangeListener) {
        this.mConfigChangeListener = configChangeListener;
    }

    public void setInterceptWhenCannotScroll(boolean z2) {
        this.mInterceptWhenCannotScroll = z2;
    }

    public void setMaxHeight(int i3) {
        this.maxHeight = i3;
        if (isInLayout()) {
            post(new AnonymousClass1());
        } else {
            requestLayout();
        }
    }

    public void setMinHeight(int i3) {
        this.mMinHeight = i3;
        requestLayout();
    }

    public COUIMaxHeightNestedScrollView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIMaxHeightNestedScrollView(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.mInterceptWhenCannotScroll = false;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUIMaxHeightScrollView);
        int i4 = R.styleable.COUIMaxHeightScrollView_scrollViewMaxHeight;
        this.mScrollViewMaxHeightAttrResId = typedArrayObtainStyledAttributes.getResourceId(i4, 0);
        int i5 = R.styleable.COUIMaxHeightScrollView_scrollViewMinHeight;
        this.mScrollViewMinHeightAttrResId = typedArrayObtainStyledAttributes.getResourceId(i5, 0);
        if (this.mScrollViewMaxHeightAttrResId != 0) {
            this.maxHeight = context.getResources().getDimensionPixelSize(this.mScrollViewMaxHeightAttrResId);
        } else {
            this.maxHeight = typedArrayObtainStyledAttributes.getDimensionPixelOffset(i4, 0);
        }
        if (this.mScrollViewMinHeightAttrResId != 0) {
            this.mMinHeight = context.getResources().getDimensionPixelSize(this.mScrollViewMinHeightAttrResId);
        } else {
            this.mMinHeight = typedArrayObtainStyledAttributes.getDimensionPixelOffset(i5, 0);
        }
        this.mInterceptWhenCannotScroll = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIMaxHeightScrollView_scrollViewInterceptWhenCannotScroll, false);
        typedArrayObtainStyledAttributes.recycle();
        this.mInitialMaxHeightFromAttrs = this.maxHeight;
        this.mInitialMinHeightFromAttrs = this.mMinHeight;
    }
}