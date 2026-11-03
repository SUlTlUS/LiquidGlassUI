package com.coui.appcompat.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.decouple.injector.adaptor.BaseConfig;
import com.oplus.settings.romupdate.RomUpdateListParser;
import com.support.progressbar.R;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes7.dex */
@Deprecated
public class COUICircleProgressBar extends View {
    public static final int ACCURACY = 2;
    private static final int ALPHA_SHOW_DURATION = 360;
    private static final float BASE_PROGRESS_POINT_ALPHA = 0.215f;
    public static final int DEFAULT_TYPE = 0;
    public static final int LARGE_TYPE = 2;
    public static final int MEDIUM_TYPE = 1;
    private static final long ONE_CIRCLE_DEGREE = 360;
    public static final int ORIGINAL_ANGLE = -90;
    private static final int PROGRESS_POINT_COUNT = 360;
    private static final String TAG = "COUICircleProgressBar";
    private static final int TIMEOUT_SEND_ACCESSIBILITY_EVENT = 10;
    private AccessibilityEventSender mAccessibilityEventSender;
    private float mArcRadius;
    private RectF mArcRect;
    private Paint mBackGroundPaint;
    private float mCenterX;
    private float mCenterY;
    private Context mContext;
    private int mCurrentStepProgress;
    private int mHalfStrokeWidth;
    private int mHalfWidth;
    private int mHeight;
    private AccessibilityManager mManager;
    private int mMax;
    private ArrayList<ProgressPoint> mPointList;
    private int mPointRadius;
    private int mPreStepProgress;
    private int mProgress;
    private int mProgressBarBgCircleColor;
    private int mProgressBarColor;
    private int mProgressBarType;
    private Paint mProgressPaint;
    private float mStepDegree;
    private int mStrokeDefaultWidth;
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
            COUICircleProgressBar.this.sendAccessibilityEvent(4);
        }

        public /* synthetic */ AccessibilityEventSender(COUICircleProgressBar cOUICircleProgressBar, AnonymousClass1 anonymousClass1) {
            this();
        }
    }

    public class ProgressPoint {
        float mCurrentAlpha;

        public ProgressPoint() {
        }

        public float getCurrentAlpha() {
            return this.mCurrentAlpha;
        }

        public void setCurrentAlpha(float f3) {
            this.mCurrentAlpha = f3;
        }
    }

    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new AnonymousClass1();
        int mProgress;

        /* JADX INFO: renamed from: com.coui.appcompat.progressbar.COUICircleProgressBar$SavedState$1, reason: invalid class name */
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
            return "COUICircleProgressBar.SavedState { " + Integer.toHexString(System.identityHashCode(this)) + " mProgress = " + this.mProgress + " }";
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i3) {
            super.writeToParcel(parcel, i3);
            parcel.writeValue(Integer.valueOf(this.mProgress));
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.mProgress = ((Integer) parcel.readValue(null)).intValue();
        }
    }

    public COUICircleProgressBar(Context context) {
        this(context, null);
    }

    private void drawBackgroudCicle(Canvas canvas) {
        this.mBackGroundPaint.setStrokeWidth(this.mStrokeWidth);
        int i3 = this.mHalfWidth;
        canvas.drawCircle(i3, i3, this.mArcRadius, this.mBackGroundPaint);
    }

    private void init() {
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
        for (int i3 = 0; i3 < 360; i3++) {
            this.mPointList.add(new ProgressPoint());
        }
        initBackgroundPaint();
        initProgressPaint();
        setProgress(this.mProgress);
        setMax(this.mMax);
        this.mManager = (AccessibilityManager) this.mContext.getSystemService(RomUpdateListParser.ACCESSIBILITY_TAG);
    }

    private void initBackgroundPaint() {
        Paint paint = new Paint(1);
        this.mBackGroundPaint = paint;
        paint.setColor(this.mProgressBarBgCircleColor);
        this.mBackGroundPaint.setStyle(Paint.Style.STROKE);
    }

    private void initProgressPaint() {
        Paint paint = new Paint(1);
        this.mProgressPaint = paint;
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.mProgressPaint.setColor(this.mProgressBarColor);
        this.mProgressPaint.setStyle(Paint.Style.STROKE);
        this.mProgressPaint.setStrokeWidth(this.mStrokeWidth);
        this.mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
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

    private void verifyProgress() {
        int i3 = this.mMax;
        if (i3 > 0) {
            int i4 = (int) (this.mProgress / (i3 / 360.0f));
            this.mCurrentStepProgress = i4;
            if (360 - i4 < 2) {
                this.mCurrentStepProgress = 360;
            }
            this.mPreStepProgress = this.mCurrentStepProgress;
        } else {
            this.mPreStepProgress = 0;
            this.mCurrentStepProgress = 0;
        }
        invalidate();
    }

    public int getMax() {
        return this.mMax;
    }

    public int getProgress() {
        return this.mProgress;
    }

    @Override // android.view.View
    public void onDetachedFromWindow() {
        AccessibilityEventSender accessibilityEventSender = this.mAccessibilityEventSender;
        if (accessibilityEventSender != null) {
            removeCallbacks(accessibilityEventSender);
        }
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        drawBackgroudCicle(canvas);
        canvas.save();
        int i3 = this.mHalfWidth;
        canvas.rotate(-90.0f, i3, i3);
        canvas.drawArc(this.mArcRect, 0.0f, this.mCurrentStepProgress, false, this.mProgressPaint);
        canvas.restore();
    }

    @Override // android.view.View
    public void onMeasure(int i3, int i4) {
        setMeasuredDimension(this.mWidth, this.mHeight);
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
        setProgress(savedState.mProgress);
        requestLayout();
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mProgress = this.mProgress;
        return savedState;
    }

    @Override // android.view.View
    public void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        this.mHalfStrokeWidth = this.mStrokeWidth / 2;
        int width = getWidth() / 2;
        this.mHalfWidth = width;
        this.mArcRadius = width - this.mHalfStrokeWidth;
        int i7 = this.mHalfWidth;
        float f3 = this.mArcRadius;
        this.mArcRect = new RectF(i7 - f3, i7 - f3, i7 + f3, i7 + f3);
    }

    public void refresh() {
        String resourceTypeName = getResources().getResourceTypeName(this.mStyle);
        TypedArray typedArrayObtainStyledAttributes = null;
        if ("attr".equals(resourceTypeName)) {
            typedArrayObtainStyledAttributes = this.mContext.obtainStyledAttributes(null, R.styleable.COUICircleProgressBar, this.mStyle, 0);
        } else if (BaseConfig.KEY_STYLE.equals(resourceTypeName)) {
            typedArrayObtainStyledAttributes = this.mContext.obtainStyledAttributes(null, R.styleable.COUICircleProgressBar, 0, this.mStyle);
        }
        if (typedArrayObtainStyledAttributes != null) {
            this.mProgressBarColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUICircleProgressBar_couiCircleProgressBarColor, 0);
            this.mProgressBarBgCircleColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUICircleProgressBar_couiCircleProgressBarBgCircleColor, 0);
            typedArrayObtainStyledAttributes.recycle();
        }
    }

    public void setHeight(int i3) {
        this.mHeight = i3;
    }

    public void setMax(int i3) {
        if (i3 < 0) {
            i3 = 0;
        }
        if (i3 != this.mMax) {
            this.mMax = i3;
            if (this.mProgress > i3) {
                this.mProgress = i3;
            }
        }
        verifyProgress();
    }

    public void setProgress(int i3) {
        Log.i(TAG, "setProgress: " + i3);
        if (i3 < 0) {
            i3 = 0;
        }
        int i4 = this.mMax;
        if (i3 > i4) {
            i3 = i4;
        }
        if (i3 != this.mProgress) {
            this.mProgress = i3;
        }
        verifyProgress();
        onProgressRefresh();
    }

    public void setProgressBarBgCircleColor(int i3) {
        this.mProgressBarBgCircleColor = i3;
        initBackgroundPaint();
    }

    public void setProgressBarColor(int i3) {
        this.mProgressBarColor = i3;
        initProgressPaint();
    }

    public void setProgressBarType(int i3) {
        this.mProgressBarType = i3;
    }

    public void setWidth(int i3) {
        this.mWidth = i3;
    }

    public COUICircleProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiCircleProgressBarStyle);
    }

    public COUICircleProgressBar(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, R.style.Widget_COUI_COUICircleProgressBar);
    }

    public COUICircleProgressBar(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mWidth = 0;
        this.mHeight = 0;
        this.mProgressBarType = 0;
        this.mStrokeWidth = 0;
        this.mPointRadius = 0;
        this.mMax = 100;
        this.mProgress = 0;
        this.mCurrentStepProgress = 0;
        this.mPreStepProgress = -1;
        this.mStepDegree = 1.0f;
        this.mPointList = new ArrayList<>();
        COUIDarkModeUtil.setForceDarkAllow(this, false);
        this.mContext = context;
        if (attributeSet != null && attributeSet.getStyleAttribute() != 0) {
            this.mStyle = attributeSet.getStyleAttribute();
        } else {
            this.mStyle = i3;
        }
        this.mContext = context;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUICircleProgressBar, i3, i4);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.coui_loading_view_default_length);
        this.mWidth = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUICircleProgressBar_couiCircleProgressBarWidth, dimensionPixelSize);
        this.mHeight = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUICircleProgressBar_couiCircleProgressBarHeight, dimensionPixelSize);
        this.mProgressBarType = typedArrayObtainStyledAttributes.getInteger(R.styleable.COUICircleProgressBar_couiCircleProgressBarType, 0);
        this.mProgressBarColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUICircleProgressBar_couiCircleProgressBarColor, 0);
        this.mProgressBarBgCircleColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUICircleProgressBar_couiCircleProgressBarBgCircleColor, 0);
        this.mProgress = typedArrayObtainStyledAttributes.getInteger(R.styleable.COUICircleProgressBar_couiCircleProgress, this.mProgress);
        this.mMax = typedArrayObtainStyledAttributes.getInteger(R.styleable.COUICircleProgressBar_couiCircleMax, this.mMax);
        typedArrayObtainStyledAttributes.recycle();
        this.mStrokeDefaultWidth = context.getResources().getDimensionPixelSize(R.dimen.coui_circle_loading_strokewidth);
        this.mStrokeMediumWidth = context.getResources().getDimensionPixelSize(R.dimen.coui_circle_loading_medium_strokewidth);
        int dimensionPixelSize2 = context.getResources().getDimensionPixelSize(R.dimen.coui_circle_loading_large_strokewidth);
        this.mStrokeLargeWidth = dimensionPixelSize2;
        this.mStrokeWidth = this.mStrokeDefaultWidth;
        int i5 = this.mProgressBarType;
        if (1 == i5) {
            this.mStrokeWidth = this.mStrokeMediumWidth;
        } else if (2 == i5) {
            this.mStrokeWidth = dimensionPixelSize2;
        }
        this.mPointRadius = this.mStrokeWidth >> 1;
        this.mCenterX = this.mWidth >> 1;
        this.mCenterY = this.mHeight >> 1;
        init();
    }
}