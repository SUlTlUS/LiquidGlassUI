package com.coui.appcompat.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import androidx.core.graphics.ColorUtils;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.decouple.injector.adaptor.BaseConfig;
import com.oplus.settings.romupdate.RomUpdateListParser;
import com.support.progressbar.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes7.dex */
public class COUICircularProgressBar extends View {
    private static final int FOLLOW_THEME_PROGRESS_BAR_TRACK_ALPHA = 89;
    public static final int LARGE_SIZE = 1;
    public static final int MEDIUM_SIZE = 0;
    private static final String TAG = "COUICircularProgressBar";
    private static final int TIMEOUT_SEND_ACCESSIBILITY_EVENT = 10;
    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_FOLLOW_THEME = 2;
    public static final int TYPE_ON_IMAGE = 1;
    private AccessibilityEventSender mAccessibilityEventSender;
    private float mCenterX;
    private float mCenterY;
    private Context mContext;
    private final COUICircularProgressDrawable mDrawable;
    private boolean mError;
    private int mErrorDrawableTint;
    private int mHeight;
    private int mInnerPadding;
    private AccessibilityManager mManager;
    private int mMax;
    private int mPauseDrawableTint;
    private boolean mPaused;
    private int mProgress;
    private int mProgressBarColor;
    private int mProgressBarSize;
    private int mProgressBarTrackColor;
    private int mProgressBarType;
    private int mStrokeLargeWidth;
    private int mStrokeMediumWidth;
    private int mStrokeWidth;
    private int mStyle;
    private int mWidth;

    public class AccessibilityEventSender implements Runnable {
        private AccessibilityEventSender() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUICircularProgressBar.this.sendAccessibilityEvent(4);
        }

        public /* synthetic */ AccessibilityEventSender(COUICircularProgressBar cOUICircularProgressBar, AnonymousClass1 anonymousClass1) {
            this();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ProgressBarSize {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ProgressBarType {
    }

    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new AnonymousClass1();
        int mMax;
        int mProgress;

        /* JADX INFO: renamed from: com.coui.appcompat.progressbar.COUICircularProgressBar$SavedState$1, reason: invalid class name */
        public class AnonymousClass1 implements Parcelable.Creator<SavedState> {
            @Override // android.os.Parcelable.Creator
            public /* bridge */ /* synthetic */ SavedState createFromParcel(Parcel parcel) {
                return createFromParcel(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public /* bridge */ /* synthetic */ SavedState[] newArray(int i3) {
                return newArray(i3);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int i3) {
                return new SavedState[i3];
            }
        }

        public /* synthetic */ SavedState(Parcel parcel, AnonymousClass1 anonymousClass1) {
            this(parcel);
        }

        public String toString() {
            return "COUICircularProgressBar.SavedState { " + Integer.toHexString(System.identityHashCode(this)) + " mProgress = " + this.mProgress + " mMax = " + this.mMax + " }";
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i3) {
            super.writeToParcel(parcel, i3);
            parcel.writeValue(Integer.valueOf(this.mProgress));
            parcel.writeValue(Integer.valueOf(this.mMax));
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.mProgress = ((Integer) parcel.readValue(getClass().getClassLoader())).intValue();
            this.mMax = ((Integer) parcel.readValue(getClass().getClassLoader())).intValue();
        }
    }

    public COUICircularProgressBar(Context context) {
        this(context, null);
    }

    private void configDrawable() {
        if (2 == this.mProgressBarType) {
            this.mDrawable.setTrackColor(ColorUtils.setAlphaComponent(this.mProgressBarTrackColor, 89));
        } else {
            this.mDrawable.setTrackColor(this.mProgressBarTrackColor);
        }
        this.mDrawable.setIsDrawShadow(1 == this.mProgressBarType);
        this.mDrawable.setProgressColor(this.mProgressBarColor);
        this.mDrawable.setPauseIconColor(this.mPauseDrawableTint);
        this.mDrawable.setErrorIconColor(this.mErrorDrawableTint);
        COUICircularProgressDrawable cOUICircularProgressDrawable = this.mDrawable;
        float f3 = this.mCenterX;
        int i3 = this.mInnerPadding;
        cOUICircularProgressDrawable.setProperties(f3 + i3, this.mCenterY + i3, this.mWidth - (i3 * 2), this.mStrokeWidth);
        this.mDrawable.setProgressBarErrorSize(this.mContext.getResources().getDimensionPixelSize(R.dimen.coui_circular_progress_error_diameter), this.mContext.getResources().getDimensionPixelSize(R.dimen.coui_circular_progress_error_stroke_width));
        this.mDrawable.invalidateSelf();
        invalidate();
    }

    private void init() {
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
        this.mManager = (AccessibilityManager) this.mContext.getSystemService(RomUpdateListParser.ACCESSIBILITY_TAG);
        setProgress(this.mProgress);
        setMax(this.mMax);
        configDrawable();
    }

    private void scheduleAccessibilityEventSender() {
        AccessibilityEventSender accessibilityEventSender = this.mAccessibilityEventSender;
        if (accessibilityEventSender == null) {
            this.mAccessibilityEventSender = new AccessibilityEventSender(this, null);
        } else {
            removeCallbacks(accessibilityEventSender);
        }
        postDelayed(this.mAccessibilityEventSender, 10L);
    }

    public void error() {
        if (this.mDrawable.isAnimating() || this.mPaused || this.mError) {
            return;
        }
        this.mDrawable.error();
        this.mError = true;
    }

    public int getMax() {
        return this.mMax;
    }

    public int getProgress() {
        return this.mProgress;
    }

    public float getVisualProgress() {
        return this.mDrawable.getVisualProgress();
    }

    public boolean isError() {
        return this.mError;
    }

    public boolean isPaused() {
        return this.mPaused;
    }

    @Override // android.view.View
    public void onAttachedToWindow() {
        this.mDrawable.setHostView(this);
        super.onAttachedToWindow();
    }

    @Override // android.view.View
    public void onDetachedFromWindow() {
        COUICircularProgressDrawable cOUICircularProgressDrawable = this.mDrawable;
        if (cOUICircularProgressDrawable != null) {
            cOUICircularProgressDrawable.recycle();
        }
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        this.mDrawable.draw(canvas);
    }

    @Override // android.view.View
    public void onMeasure(int i3, int i4) {
        int i5 = this.mWidth;
        int i6 = this.mInnerPadding;
        setMeasuredDimension(i5 + (i6 * 2), this.mHeight + (i6 * 2));
    }

    public void onProgressRefresh() {
        AccessibilityManager accessibilityManager = this.mManager;
        if (accessibilityManager != null && accessibilityManager.isEnabled() && this.mManager.isTouchExplorationEnabled()) {
            scheduleAccessibilityEventSender();
        }
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setProgress(savedState.mProgress, false);
        requestLayout();
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mProgress = this.mProgress;
        return savedState;
    }

    public void pause() {
        int i3 = this.mProgressBarType;
        if (i3 == 0 || 2 == i3) {
            Log.w(TAG, "Default type circular progress bar can not pause!");
        } else {
            if (this.mDrawable.isAnimating() || this.mError || this.mPaused) {
                return;
            }
            this.mDrawable.pause();
            this.mPaused = true;
        }
    }

    public void recover() {
        if (this.mDrawable.isAnimating() || this.mPaused || !this.mError) {
            return;
        }
        this.mDrawable.recover();
        this.mError = false;
    }

    public void refresh() {
        String resourceTypeName = getResources().getResourceTypeName(this.mStyle);
        TypedArray typedArrayObtainStyledAttributes = null;
        if ("attr".equals(resourceTypeName)) {
            typedArrayObtainStyledAttributes = this.mContext.obtainStyledAttributes(null, R.styleable.COUICircularProgressBar, this.mStyle, 0);
        } else if (BaseConfig.KEY_STYLE.equals(resourceTypeName)) {
            typedArrayObtainStyledAttributes = this.mContext.obtainStyledAttributes(null, R.styleable.COUICircularProgressBar, 0, this.mStyle);
        }
        if (typedArrayObtainStyledAttributes != null) {
            this.mProgressBarColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUICircularProgressBar_couiCircularProgressBarColor, 0);
            this.mProgressBarTrackColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUICircularProgressBar_couiCircularProgressBarTrackColor, 0);
            this.mPauseDrawableTint = typedArrayObtainStyledAttributes.getColor(R.styleable.COUICircularProgressBar_couiCircularPauseDrawableTint, 0);
            this.mErrorDrawableTint = typedArrayObtainStyledAttributes.getColor(R.styleable.COUICircularProgressBar_couiCircularErrorDrawableTint, 0);
            typedArrayObtainStyledAttributes.recycle();
        }
    }

    public void resume() {
        if (this.mDrawable.isAnimating() || this.mError || !this.mPaused) {
            return;
        }
        this.mDrawable.resume();
        this.mPaused = false;
    }

    public void setMax(int i3) {
        if (i3 < 0) {
            i3 = 0;
        }
        if (i3 != this.mMax) {
            this.mMax = i3;
            this.mDrawable.setMax(i3);
            int i4 = this.mProgress;
            int i5 = this.mMax;
            if (i4 > i5) {
                this.mProgress = i5;
            }
        }
    }

    public void setOnProgressChangedListener(COUICircularProgressDrawable.OnProgressChangedListener onProgressChangedListener) {
        COUICircularProgressDrawable cOUICircularProgressDrawable = this.mDrawable;
        if (cOUICircularProgressDrawable != null) {
            cOUICircularProgressDrawable.setOnProgressChangedListener(onProgressChangedListener);
        }
    }

    public void setOnProgressStateAnimationListener(COUICircularProgressDrawable.OnProgressStateAnimatorListener onProgressStateAnimatorListener) {
        COUICircularProgressDrawable cOUICircularProgressDrawable = this.mDrawable;
        if (cOUICircularProgressDrawable != null) {
            cOUICircularProgressDrawable.setOnProgressStateAnimatorListener(onProgressStateAnimatorListener);
        }
    }

    public void setProgress(int i3) {
        setProgress(i3, true);
    }

    public void setProgressBarType(int i3) {
        this.mProgressBarType = i3;
        configDrawable();
    }

    public void setProgressSize(int i3) {
        this.mProgressBarSize = i3;
        if (i3 == 0) {
            int dimensionPixelOffset = this.mContext.getResources().getDimensionPixelOffset(R.dimen.coui_circular_progress_medium_length);
            this.mWidth = dimensionPixelOffset;
            this.mHeight = dimensionPixelOffset;
            this.mStrokeWidth = this.mStrokeMediumWidth;
        } else if (1 == i3) {
            int dimensionPixelOffset2 = this.mContext.getResources().getDimensionPixelOffset(R.dimen.coui_circular_progress_large_length);
            this.mWidth = dimensionPixelOffset2;
            this.mHeight = dimensionPixelOffset2;
            this.mStrokeWidth = this.mStrokeLargeWidth;
        }
        this.mCenterX = this.mWidth >> 1;
        this.mCenterY = this.mHeight >> 1;
        configDrawable();
        requestLayout();
    }

    public COUICircularProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiCircularProgressBarStyle);
    }

    public void setProgress(int i3, boolean z2) {
        if (i3 < 0) {
            i3 = 0;
        }
        int i4 = this.mMax;
        if (i3 > i4) {
            i3 = i4;
        }
        if (i3 != this.mProgress) {
            this.mProgress = i3;
            this.mDrawable.setProgress(i3, z2);
        }
        onProgressRefresh();
    }

    public COUICircularProgressBar(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, R.style.Widget_COUI_COUICircularProgressBar);
    }

    public COUICircularProgressBar(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mProgressBarSize = 0;
        this.mProgressBarType = 0;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mInnerPadding = 0;
        this.mStrokeWidth = 0;
        this.mMax = 100;
        this.mProgress = 0;
        this.mPaused = false;
        this.mError = false;
        COUIDarkModeUtil.setForceDarkAllow(this, false);
        this.mContext = context;
        if (attributeSet != null && attributeSet.getStyleAttribute() != 0) {
            this.mStyle = attributeSet.getStyleAttribute();
        } else {
            this.mStyle = i3;
        }
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.coui_circular_progress_large_length);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUICircularProgressBar, i3, i4);
        this.mWidth = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUICircularProgressBar_couiCircularProgressBarWidth, dimensionPixelSize);
        this.mHeight = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUICircularProgressBar_couiCircularProgressBarHeight, dimensionPixelSize);
        this.mProgressBarType = typedArrayObtainStyledAttributes.getInteger(R.styleable.COUICircularProgressBar_couiCircularProgressBarType, 0);
        this.mProgressBarSize = typedArrayObtainStyledAttributes.getInteger(R.styleable.COUICircularProgressBar_couiCircularProgressBarSize, 1);
        this.mProgressBarColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUICircularProgressBar_couiCircularProgressBarColor, 0);
        this.mProgressBarTrackColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUICircularProgressBar_couiCircularProgressBarTrackColor, 0);
        this.mPauseDrawableTint = typedArrayObtainStyledAttributes.getColor(R.styleable.COUICircularProgressBar_couiCircularPauseDrawableTint, 0);
        this.mErrorDrawableTint = typedArrayObtainStyledAttributes.getColor(R.styleable.COUICircularProgressBar_couiCircularErrorDrawableTint, 0);
        this.mProgress = typedArrayObtainStyledAttributes.getInteger(R.styleable.COUICircularProgressBar_couiCircularProgress, this.mProgress);
        this.mMax = typedArrayObtainStyledAttributes.getInteger(R.styleable.COUICircularProgressBar_couiCircularMax, this.mMax);
        typedArrayObtainStyledAttributes.recycle();
        this.mInnerPadding = context.getResources().getDimensionPixelSize(R.dimen.coui_circular_progress_default_padding);
        this.mStrokeMediumWidth = context.getResources().getDimensionPixelSize(R.dimen.coui_circular_progress_medium_stroke_width);
        int dimensionPixelSize2 = context.getResources().getDimensionPixelSize(R.dimen.coui_circular_progress_large_stroke_width);
        this.mStrokeLargeWidth = dimensionPixelSize2;
        int i5 = this.mProgressBarSize;
        if (i5 == 0) {
            this.mStrokeWidth = this.mStrokeMediumWidth;
        } else if (1 == i5) {
            this.mStrokeWidth = dimensionPixelSize2;
        }
        this.mCenterX = this.mWidth >> 1;
        this.mCenterY = this.mHeight >> 1;
        this.mDrawable = new COUICircularProgressDrawable(context);
        init();
    }
}