package com.coui.appcompat.progressbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.core.view.ViewCompat;
import com.coui.appcompat.animation.COUILinearInterpolator;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.touchhelper.COUIViewExplorerByTouchHelper;
import com.decouple.injector.adaptor.BaseConfig;
import com.support.progressbar.R;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes7.dex */
public class COUILoadingView extends View {
    private static final boolean DEBUG = false;
    public static final int DEFAULT_TYPE = 1;
    private static final float LARGE_POINT_END_ALPHA = 1.0f;
    private static final float LARGE_POINT_START_ALPHA = 0.215f;
    public static final int LARGE_TYPE = 2;
    private static final float MEDIUM_POINT_END_ALPHA = 0.4f;
    private static final float MEDIUM_POINT_START_ALPHA = 0.1f;
    public static final int MEDIUM_TYPE = 1;
    private static final int ONE_CIRCLE_DEGREE = 360;
    private static final int ONE_CYCLE_DURATION = 480;
    private static final int ONE_THOUSAND_INT = 1000;
    private static final float ONE_THOUSAND_MILLISECOND = 1000.0f;
    public static final int ORIGINAL_ANGLE = -90;
    private static final int PROGRESS_POINT_COUNT = 6;
    private static final int SEMI_CIRCLE = 180;
    public static final int SMALL_TYPE = 0;
    public static final int SWIPT_ANGEL = 60;
    private static final String TAG = "COUILoadingView";
    private static final int TWO = 2;
    private String mAccessDescription;
    private ValueAnimator.AnimatorUpdateListener mAnimListener;
    private float mArcRadius;
    private RectF mArcRect;
    private Paint mBackGroundPaint;
    private COUIViewExplorerByTouchHelper.COUIViewTalkBalkInteraction mCOUIViewTalkBalkInteraction;
    private float mCenterX;
    private float mCenterY;
    private Context mContext;
    private float mCurrentDegree;
    private float mCurrentStepProgress;
    private float mEndAlpha;
    private float mHalfHeight;
    private float mHalfStrokeWidth;
    private float mHalfWidth;
    private int mHeight;
    private boolean mIsAnimationCreated;
    private boolean mIsAnimationStarted;
    private int mLoadingType;
    private int mLoadingViewBgCircleColor;
    private int mLoadingViewColor;
    private float[] mPointsAlpha;
    private ValueAnimator mProgressAnimator;
    private Paint mProgressPaint;
    private float mStartAlpha;
    private float mStepDegree;
    private int mStrokeDefaultWidth;
    private int mStrokeLargeWidth;
    private int mStrokeMediumWidth;
    private float mStrokeWidth;
    private int mStyle;
    private COUIViewExplorerByTouchHelper mTouchHelper;
    private int mWidth;

    /* JADX INFO: renamed from: com.coui.appcompat.progressbar.COUILoadingView$1, reason: invalid class name */
    public class AnonymousClass1 implements COUIViewExplorerByTouchHelper.COUIViewTalkBalkInteraction {
        private int mVirtualViewId = -1;

        public AnonymousClass1() {
        }

        @Override // com.coui.appcompat.touchhelper.COUIViewExplorerByTouchHelper.COUIViewTalkBalkInteraction
        public CharSequence getClassName() {
            return null;
        }

        @Override // com.coui.appcompat.touchhelper.COUIViewExplorerByTouchHelper.COUIViewTalkBalkInteraction
        public int getCurrentPosition() {
            return -1;
        }

        @Override // com.coui.appcompat.touchhelper.COUIViewExplorerByTouchHelper.COUIViewTalkBalkInteraction
        public int getDisablePosition() {
            return -1;
        }

        @Override // com.coui.appcompat.touchhelper.COUIViewExplorerByTouchHelper.COUIViewTalkBalkInteraction
        public void getItemBounds(int i3, Rect rect) {
            if (i3 == 0) {
                rect.set(0, 0, COUILoadingView.access$200(COUILoadingView.this), COUILoadingView.access$300(COUILoadingView.this));
            }
        }

        @Override // com.coui.appcompat.touchhelper.COUIViewExplorerByTouchHelper.COUIViewTalkBalkInteraction
        public int getItemCounts() {
            return 1;
        }

        @Override // com.coui.appcompat.touchhelper.COUIViewExplorerByTouchHelper.COUIViewTalkBalkInteraction
        public CharSequence getItemDescription(int i3) {
            return COUILoadingView.access$400(COUILoadingView.this) != null ? COUILoadingView.access$400(COUILoadingView.this) : getClass().getSimpleName();
        }

        @Override // com.coui.appcompat.touchhelper.COUIViewExplorerByTouchHelper.COUIViewTalkBalkInteraction
        public int getVirtualViewAt(float f3, float f4) {
            return (f3 < 0.0f || f3 > ((float) COUILoadingView.access$200(COUILoadingView.this)) || f4 < 0.0f || f4 > ((float) COUILoadingView.access$300(COUILoadingView.this))) ? -1 : 0;
        }

        @Override // com.coui.appcompat.touchhelper.COUIViewExplorerByTouchHelper.COUIViewTalkBalkInteraction
        public void performAction(int i3, int i4, boolean z2) {
        }
    }

    public static class LoadingAnimUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        WeakReference<COUILoadingView> mWeakRef;

        public LoadingAnimUpdateListener(COUILoadingView cOUILoadingView) {
            this.mWeakRef = new WeakReference<>(cOUILoadingView);
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            valueAnimator.getAnimatedFraction();
            COUILoadingView cOUILoadingView = this.mWeakRef.get();
            if (cOUILoadingView != null) {
                if (cOUILoadingView.isAttachedToWindow() && cOUILoadingView.getVisibility() == 0) {
                    cOUILoadingView.invalidate();
                } else {
                    Log.e(COUILoadingView.access$000(), "LoadingView state error,cancelAnimations");
                    COUILoadingView.access$100(cOUILoadingView);
                }
            }
        }
    }

    public COUILoadingView(Context context) {
        this(context, null);
    }

    public static /* synthetic */ String access$000() {
        return TAG;
    }

    public static /* synthetic */ void access$100(COUILoadingView cOUILoadingView) {
        cOUILoadingView.cancelAnimations();
    }

    public static /* synthetic */ int access$200(COUILoadingView cOUILoadingView) {
        return cOUILoadingView.mWidth;
    }

    public static /* synthetic */ int access$300(COUILoadingView cOUILoadingView) {
        return cOUILoadingView.mHeight;
    }

    public static /* synthetic */ String access$400(COUILoadingView cOUILoadingView) {
        return cOUILoadingView.mAccessDescription;
    }

    private void cancelAnimations() {
        ValueAnimator valueAnimator = this.mProgressAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    private void createAnimator() {
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mProgressAnimator = valueAnimatorOfFloat;
        valueAnimatorOfFloat.setDuration(480L);
        this.mProgressAnimator.setInterpolator(new COUILinearInterpolator());
        this.mProgressAnimator.addUpdateListener(new LoadingAnimUpdateListener(this));
        this.mProgressAnimator.setRepeatMode(1);
        this.mProgressAnimator.setRepeatCount(-1);
        this.mProgressAnimator.setInterpolator(new COUILinearInterpolator());
    }

    private void destroyAnimator() {
        ValueAnimator valueAnimator = this.mProgressAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.mProgressAnimator.removeAllListeners();
            this.mProgressAnimator.removeAllUpdateListeners();
            this.mProgressAnimator = null;
        }
    }

    private void drawBackgroundCircle(Canvas canvas) {
        float f3 = this.mHalfWidth;
        canvas.drawCircle(f3, f3, this.mArcRadius, this.mBackGroundPaint);
    }

    private void initArcRect() {
        this.mHalfStrokeWidth = this.mStrokeWidth / 2.0f;
        this.mHalfWidth = getWidth() / 2;
        this.mHalfHeight = getHeight() / 2;
        this.mArcRadius = this.mHalfWidth - this.mHalfStrokeWidth;
        float f3 = this.mHalfWidth;
        float f4 = this.mArcRadius;
        this.mArcRect = new RectF(f3 - f4, f3 - f4, f3 + f4, f3 + f4);
    }

    private void initBackgroundPaint() {
        Paint paint = new Paint(1);
        this.mBackGroundPaint = paint;
        paint.setColor(this.mLoadingViewBgCircleColor);
        this.mBackGroundPaint.setStyle(Paint.Style.STROKE);
        this.mBackGroundPaint.setStrokeWidth(this.mStrokeWidth);
    }

    private void initProgressPaint() {
        Paint paint = new Paint(1);
        this.mProgressPaint = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.mProgressPaint.setColor(this.mLoadingViewColor);
        this.mProgressPaint.setStrokeWidth(this.mStrokeWidth);
        this.mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    private void startAnimations() {
        ValueAnimator valueAnimator = this.mProgressAnimator;
        if (valueAnimator != null) {
            if (valueAnimator.isRunning()) {
                this.mProgressAnimator.cancel();
            }
            this.mProgressAnimator.start();
        }
    }

    @Override // android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!this.mIsAnimationCreated) {
            createAnimator();
            this.mIsAnimationCreated = true;
        }
        if (this.mIsAnimationStarted || getVisibility() != 0) {
            return;
        }
        startAnimations();
        this.mIsAnimationStarted = true;
    }

    @Override // android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        destroyAnimator();
        this.mIsAnimationCreated = false;
        this.mIsAnimationStarted = false;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        this.mCurrentStepProgress = ((SystemClock.uptimeMillis() % 1000) * 360) / 1000.0f;
        drawBackgroundCircle(canvas);
        canvas.save();
        canvas.rotate(-90.0f, this.mHalfWidth, this.mHalfHeight);
        if (this.mArcRect == null) {
            initArcRect();
        }
        RectF rectF = this.mArcRect;
        float f3 = this.mCurrentStepProgress;
        canvas.drawArc(rectF, f3 - 30.0f, (2.0f - Math.abs((180.0f - f3) / 180.0f)) * 60.0f, false, this.mProgressPaint);
        canvas.restore();
    }

    @Override // android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        super.onLayout(z2, i3, i4, i5, i6);
        if (this.mArcRect == null) {
            initArcRect();
        }
    }

    @Override // android.view.View
    public void onMeasure(int i3, int i4) {
        setMeasuredDimension(this.mWidth, this.mHeight);
    }

    @Override // android.view.View
    public void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        initArcRect();
    }

    @Override // android.view.View
    public void onVisibilityChanged(View view, int i3) {
        super.onVisibilityChanged(view, i3);
        if (getVisibility() != 0 || !isAttachedToWindow()) {
            cancelAnimations();
            this.mIsAnimationStarted = false;
            return;
        }
        if (!this.mIsAnimationCreated) {
            createAnimator();
            this.mIsAnimationCreated = true;
        }
        if (this.mIsAnimationStarted) {
            return;
        }
        startAnimations();
        this.mIsAnimationStarted = true;
    }

    @Override // android.view.View
    public void onWindowVisibilityChanged(int i3) {
        super.onWindowVisibilityChanged(i3);
        if (i3 == 0 && isAttachedToWindow() && getVisibility() == 0 && getWindowVisibility() == 0) {
            startAnimations();
        } else {
            cancelAnimations();
        }
    }

    public void refresh() {
        String resourceTypeName = getResources().getResourceTypeName(this.mStyle);
        TypedArray typedArrayObtainStyledAttributes = null;
        if ("attr".equals(resourceTypeName)) {
            typedArrayObtainStyledAttributes = this.mContext.obtainStyledAttributes(null, R.styleable.COUILoadingView, this.mStyle, 0);
        } else if (BaseConfig.KEY_STYLE.equals(resourceTypeName)) {
            typedArrayObtainStyledAttributes = this.mContext.obtainStyledAttributes(null, R.styleable.COUILoadingView, 0, this.mStyle);
        }
        if (typedArrayObtainStyledAttributes != null) {
            this.mLoadingViewColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUILoadingView_couiLoadingViewColor, 0);
            this.mLoadingViewBgCircleColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUILoadingView_couiLoadingViewBgCircleColor, 0);
            typedArrayObtainStyledAttributes.recycle();
        }
        initProgressPaint();
        initBackgroundPaint();
        invalidate();
    }

    public void setHeight(int i3) {
        this.mHeight = i3;
    }

    public void setLoadingType(int i3) {
        this.mLoadingType = i3;
    }

    public void setLoadingViewBgCircleColor(int i3) {
        this.mLoadingViewBgCircleColor = i3;
        initBackgroundPaint();
    }

    public void setLoadingViewColor(int i3) {
        this.mLoadingViewColor = i3;
        initProgressPaint();
    }

    public void setWidth(int i3) {
        this.mWidth = i3;
    }

    public COUILoadingView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiLoadingViewStyle);
    }

    public COUILoadingView(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, R.attr.couiLoadingViewStyle, R.style.Widget_COUI_COUILoadingView);
    }

    public COUILoadingView(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mPointsAlpha = new float[6];
        this.mWidth = 0;
        this.mHeight = 0;
        this.mLoadingType = 1;
        this.mStepDegree = 60.0f;
        this.mAccessDescription = null;
        this.mStartAlpha = 0.1f;
        this.mEndAlpha = 0.4f;
        this.mIsAnimationCreated = false;
        this.mIsAnimationStarted = false;
        this.mCOUIViewTalkBalkInteraction = new AnonymousClass1();
        if (attributeSet != null && attributeSet.getStyleAttribute() != 0) {
            this.mStyle = attributeSet.getStyleAttribute();
        } else {
            this.mStyle = i3;
        }
        this.mContext = context;
        COUIDarkModeUtil.setForceDarkAllow(this, false);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUILoadingView, i3, i4);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.coui_loading_view_default_length);
        this.mWidth = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUILoadingView_couiLoadingViewWidth, dimensionPixelSize);
        this.mHeight = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUILoadingView_couiLoadingViewHeight, dimensionPixelSize);
        this.mLoadingType = typedArrayObtainStyledAttributes.getInteger(R.styleable.COUILoadingView_couiLoadingViewType, 1);
        this.mLoadingViewColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUILoadingView_couiLoadingViewColor, 0);
        this.mLoadingViewBgCircleColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUILoadingView_couiLoadingViewBgCircleColor, 0);
        typedArrayObtainStyledAttributes.recycle();
        this.mStrokeDefaultWidth = context.getResources().getDimensionPixelSize(R.dimen.coui_circle_loading_strokewidth);
        this.mStrokeMediumWidth = context.getResources().getDimensionPixelSize(R.dimen.coui_circle_loading_medium_strokewidth);
        int dimensionPixelSize2 = context.getResources().getDimensionPixelSize(R.dimen.coui_circle_loading_large_strokewidth);
        this.mStrokeLargeWidth = dimensionPixelSize2;
        this.mStrokeWidth = this.mStrokeDefaultWidth;
        int i5 = this.mLoadingType;
        if (1 == i5) {
            this.mStrokeWidth = this.mStrokeMediumWidth;
            this.mStartAlpha = 0.1f;
            this.mEndAlpha = 0.4f;
        } else if (2 == i5) {
            this.mStrokeWidth = dimensionPixelSize2;
            this.mStartAlpha = LARGE_POINT_START_ALPHA;
            this.mEndAlpha = 1.0f;
        }
        this.mCenterX = this.mWidth >> 1;
        this.mCenterY = this.mHeight >> 1;
        COUIViewExplorerByTouchHelper cOUIViewExplorerByTouchHelper = new COUIViewExplorerByTouchHelper(this);
        this.mTouchHelper = cOUIViewExplorerByTouchHelper;
        cOUIViewExplorerByTouchHelper.setCOUIViewTalkBalkInteraction(this.mCOUIViewTalkBalkInteraction);
        ViewCompat.setAccessibilityDelegate(this, this.mTouchHelper);
        ViewCompat.setImportantForAccessibility(this, 1);
        this.mAccessDescription = context.getString(R.string.coui_loading_view_access_string);
        initProgressPaint();
        initBackgroundPaint();
    }
}