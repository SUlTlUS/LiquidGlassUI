package com.coui.appcompat.seekbar;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.AbsSeekBar;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.coui.appcompat.animation.COUIEaseInterpolator;
import com.coui.appcompat.animation.COUIMoveEaseInterpolator;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.coui.appcompat.version.COUIVersionUtil;
import com.coui.appcompat.vibrateutil.VibrateUtils;
import com.coui.appcompat.view.ViewUtil;
import com.decouple.injector.adaptor.BaseConfig;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import com.oplus.graphics.OplusCanvas;
import com.oplus.graphics.OplusPath;
import com.oplus.graphics.OplusPathAdapter;
import com.oplus.os.LinearmotorVibrator;
import com.oplus.physicsengine.common.Compat;
import com.oplus.physicsengine.engine.AnimationListener;
import com.oplus.physicsengine.engine.AnimationUpdateListener;
import com.oplus.physicsengine.engine.BaseBehavior;
import com.oplus.physicsengine.engine.FlingBehavior;
import com.oplus.physicsengine.engine.FloatValueHolder;
import com.oplus.physicsengine.engine.PhysicalAnimator;
import com.support.seekbar.R;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/* JADX INFO: loaded from: classes7.dex */
public class COUIVerticalSeekBar extends AbsSeekBar implements AnimationListener, AnimationUpdateListener {
    private static final float BACKGROUND_RADIUS_SCALE = 6.0f;
    private static final long BUTTON_DEFORMATION_ANIM_DURATION = 350;
    private static final int DAMPING_DISTANCE = 20;
    private static final double DEFORMATION_FORMULA_CONSTANT = -11.5d;
    private static final int DURATION_150 = 150;
    private static final int DURATION_483 = 483;
    private static final int FAST_MOVE_VELOCITY = 95;
    public static final float FLOAT_PRECISION = 0.001f;
    private static final String HOLDER_SCALE = "HOLDER_SCALE";
    public static final int LOG_QUEUE_MAX_SIZE = 20;
    private static final float MAX_FAST_MOVE_PERCENT = 0.95f;
    private static final float MAX_MOVE_DAMPING = 0.4f;
    private static final int MAX_VELOCITY = 8000;
    private static final float MIN_FAST_MOVE_PERCENT = 0.05f;
    public static final int MOVE_BY_DEFAULT = 0;
    public static final int MOVE_BY_DISTANCE = 2;
    public static final int MOVE_BY_FINGER = 1;
    private static final int ONE_SECOND_UNITS = 1000;
    private static final int PHYSICAL_VELOCITY_LIMIT = 100;
    private static final float PROGRESS_RADIUS_SCALE = 4.0f;
    protected static final int RELEASE_ANIM_DURATION = 183;
    private static final float SCALE_DEFORMATION_MAX = 2.0f;
    private static final float SCALE_DEFORMATION_MIN = -1.0f;
    private static final int SCALE_DEFORMATION_TIMES = 5;
    private static final float SCALE_MAX = 1.0f;
    private static final float SCALE_MIN = 0.0f;
    private static final String TAG = "COUIVerticalSeekBar";
    private static final float TEXT_SHADOW_DX = 0.0f;
    private static final float TEXT_SHADOW_DY = 8.0f;
    private static final float TEXT_SHADOW_RADIUS = 25.0f;
    private static final float TEXT_SIZE = 12.0f;
    private static final int TOUCH_ANIMATION_ENLARGE_DURATION = 183;
    public static final int TYPE_ENLARGE = 1;
    public static final int TYPE_NORMAL = 0;
    private static final int VELOCITY_COMPUTE_TIME = 100;
    protected int mBackgroundColor;
    protected ColorStateList mBackgroundColorStateList;
    protected float mBackgroundEnlargeScale;
    protected Paint mBackgroundPaint;
    private Path mBackgroundPath;
    private OplusPathAdapter mBackgroundPathAdapter;
    protected float mBackgroundRadius;
    protected float mBackgroundRadiusEnlargeScale;
    protected Rect mBackgroundRect;
    protected float mBackgroundRoundCornerWeight;
    protected float mBackgroundWidth;
    private ValueAnimator mButtonDeformationAnimator;
    private Locale mCachedLocale;
    protected ValueAnimator mClickAnimator;
    protected Path mClipProgressPath;
    protected Rect mClipProgressRect;
    protected float mCurBackgroundRadius;
    protected float mCurBackgroundWidth;
    protected float mCurBottomDeformationValue;
    protected float mCurPaddingVertical;
    protected float mCurProgressRadius;
    protected float mCurProgressWidth;
    protected float mCurTopDeformationValue;
    protected float mCustomProgressAnimDuration;
    private Interpolator mCustomProgressAnimInterpolator;
    private float mDamping;
    private float mDefaultPaddingVertical;
    protected boolean mEnableAdaptiveVibrator;
    private boolean mEnableCustomEnlarge;
    protected boolean mEnableVibrator;
    private ValueAnimator mEnlargeAnimator;
    private float mFastMoveScaleOffsetY;
    private Spring mFastMoveSpring;
    private SpringConfig mFastMoveSpringConfig;
    private FlingBehavior mFlingBehavior;
    private float mFlingDampingRatio;
    private float mFlingFrequency;
    private float mFlingLinearDamping;
    private FloatValueHolder mFlingValueHolder;
    private float mFlingVelocity;
    private Paint.FontMetricsInt mFmi;
    private boolean mHasEnlarge;
    protected boolean mHasMotorVibrator;
    protected float mHeightBottomDeformedDownValue;
    protected float mHeightBottomDeformedUpValue;
    protected float mHeightTopDeformedDownValue;
    protected float mHeightTopDeformedUpValue;
    private boolean mHistoryLogEnable;
    private final Queue<String> mHistoryLogQueue;
    private float mHistorySeekBarHeight;
    private Drawable mInactiveTrackDrawable;
    private int mIncrement;
    private Interpolator mInterpolator;
    protected boolean mIsDragging;
    private boolean mIsPhysicsEnable;
    private boolean mIsProgressFull;
    private boolean mIsSupportDeformation;
    protected boolean mIsSupportSmoothRoundCorner;
    private float mLastPixelPhysicalSize;
    protected int mLastProgress;
    protected float mLastY;
    protected Object mLinearMotorVibrator;
    protected int mMax;
    private float mMaxBottomHeightDeformedValue;
    private int mMaxBottomMovingDistance;
    private int mMaxHeight;
    private float mMaxTopHeightDeformedValue;
    private int mMaxTopMovingDistance;
    private float mMaxWidthDeformedValue;
    protected int mMin;
    private int mMoveType;
    protected int mOldProgress;
    private OnDeformedListener mOnDeformedListener;
    private OnSeekBarChangeListener mOnSeekBarChangeListener;
    protected float mPaddingVertical;
    private NumberFormat mPercentFormat;
    private PhysicalAnimator mPhysicalAnimator;
    private float mPixPerProgress;
    protected int mProgress;
    protected int mProgressColor;
    protected ColorStateList mProgressColorStateList;
    private String mProgressContentDescription;
    protected float mProgressEnlargeScale;
    private OplusPathAdapter mProgressInnerPathAdapter;
    private final Path mProgressInnerSmoothPath;
    protected Paint mProgressPaint;
    private OplusPathAdapter mProgressPathAdapter;
    protected float mProgressRadius;
    protected float mProgressRadiusEnlargeScale;
    protected Rect mProgressRect;
    protected float mProgressRoundCornerWeight;
    protected float mProgressWidth;
    private int mRealProgress;
    private int mRefreshStyle;
    private ValueAnimator mReleaseAnimator;
    protected float mScale;
    private final String mSeekBarRoleDescription;
    private int mSeekbarMinWidth;
    protected boolean mShowProgress;
    private boolean mShowText;
    private boolean mShowTextShadow;
    protected boolean mShowThumb;
    protected int mSmoothStyleType;
    private boolean mStartDragging;
    protected Rect mTempRect;
    private int mTextColor;
    private String mTextContent;
    private TextDrawable mTextDrawable;
    protected float mTextMarginTop;
    private TextPaint mTextPaint;
    private Bitmap mThumbBitmap;
    protected int mThumbColor;
    protected ColorStateList mThumbColorStateList;
    protected float mThumbOutRadius;
    protected float mThumbOutRoundCornerWeight;
    protected float mThumbOutWidth;
    protected Paint mThumbPaint;
    private OplusPathAdapter mThumbPathAdapter;
    private float mThumbPosition;
    private final Path mThumbSmoothPath;
    private final RectF mTmpRectF;
    protected float mTouchDownY;
    protected ValueAnimator mTouchEnlargeAnimator;
    protected ValueAnimator mTouchReleaseAnimator;
    protected int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    protected float mVerticalPaddingScale;
    private ExecutorService mVibratorExecutor;
    private float mWidthDeformedValue;
    private static final Interpolator THUMB_ANIMATE_INTERPOLATOR = new COUIMoveEaseInterpolator();
    private static final Interpolator PROGRESS_SCALE_INTERPOLATOR = new COUIEaseInterpolator();

    /* JADX INFO: renamed from: com.coui.appcompat.seekbar.COUIVerticalSeekBar$1, reason: invalid class name */
    public class AnonymousClass1 extends AccessibilityDelegateCompat {
        public AnonymousClass1() {
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            accessibilityNodeInfoCompat.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SET_PROGRESS);
            accessibilityNodeInfoCompat.setRangeInfo(AccessibilityNodeInfoCompat.RangeInfoCompat.obtain(1, COUIVerticalSeekBar.this.getMin(), COUIVerticalSeekBar.this.getMax(), COUIVerticalSeekBar.this.getProgress()));
            accessibilityNodeInfoCompat.setRoleDescription(COUIVerticalSeekBar.access$000(COUIVerticalSeekBar.this));
            COUIVerticalSeekBar cOUIVerticalSeekBar = COUIVerticalSeekBar.this;
            accessibilityNodeInfoCompat.setStateDescription(COUIVerticalSeekBar.access$100(cOUIVerticalSeekBar, cOUIVerticalSeekBar.getProgress()));
            if (COUIVerticalSeekBar.this.isEnabled()) {
                int progress = COUIVerticalSeekBar.this.getProgress();
                if (progress > COUIVerticalSeekBar.this.getMin()) {
                    accessibilityNodeInfoCompat.addAction(8192);
                }
                if (progress < COUIVerticalSeekBar.this.getMax()) {
                    accessibilityNodeInfoCompat.addAction(4096);
                }
            }
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public boolean performAccessibilityAction(View view, int i3, Bundle bundle) {
            if (!COUIVerticalSeekBar.this.isEnabled()) {
                return false;
            }
            if (i3 == 4096) {
                COUIVerticalSeekBar cOUIVerticalSeekBar = COUIVerticalSeekBar.this;
                cOUIVerticalSeekBar.setProgress(cOUIVerticalSeekBar.getProgress() + COUIVerticalSeekBar.access$200(COUIVerticalSeekBar.this), false, true);
                COUIVerticalSeekBar cOUIVerticalSeekBar2 = COUIVerticalSeekBar.this;
                cOUIVerticalSeekBar2.announceForAccessibility(COUIVerticalSeekBar.access$100(cOUIVerticalSeekBar2, cOUIVerticalSeekBar2.getProgress()));
                return true;
            }
            if (i3 != 8192) {
                return super.performAccessibilityAction(view, i3, bundle);
            }
            COUIVerticalSeekBar cOUIVerticalSeekBar3 = COUIVerticalSeekBar.this;
            cOUIVerticalSeekBar3.setProgress(cOUIVerticalSeekBar3.getProgress() - COUIVerticalSeekBar.access$200(COUIVerticalSeekBar.this), false, true);
            COUIVerticalSeekBar cOUIVerticalSeekBar4 = COUIVerticalSeekBar.this;
            cOUIVerticalSeekBar4.announceForAccessibility(COUIVerticalSeekBar.access$100(cOUIVerticalSeekBar4, cOUIVerticalSeekBar4.getProgress()));
            return true;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.seekbar.COUIVerticalSeekBar$2, reason: invalid class name */
    public class AnonymousClass2 implements SpringListener {
        public AnonymousClass2() {
        }

        @Override // com.facebook.rebound.SpringListener
        public void onSpringActivate(Spring spring) {
        }

        @Override // com.facebook.rebound.SpringListener
        public void onSpringAtRest(Spring spring) {
        }

        @Override // com.facebook.rebound.SpringListener
        public void onSpringEndStateChange(Spring spring) {
        }

        @Override // com.facebook.rebound.SpringListener
        public void onSpringUpdate(Spring spring) {
            if (COUIVerticalSeekBar.access$300(COUIVerticalSeekBar.this) != spring.getEndValue()) {
                if (COUIVerticalSeekBar.this.isEnabled()) {
                    COUIVerticalSeekBar.access$302(COUIVerticalSeekBar.this, (float) spring.getCurrentValue());
                } else {
                    COUIVerticalSeekBar.access$302(COUIVerticalSeekBar.this, 0.0f);
                }
                COUIVerticalSeekBar.this.invalidate();
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.seekbar.COUIVerticalSeekBar$3, reason: invalid class name */
    public class AnonymousClass3 implements Animator.AnimatorListener {
        final /* synthetic */ boolean val$isFromUser;

        public AnonymousClass3(boolean z2) {
            this.val$isFromUser = z2;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (COUIVerticalSeekBar.access$400(COUIVerticalSeekBar.this) != null) {
                OnSeekBarChangeListener onSeekBarChangeListenerAccess$400 = COUIVerticalSeekBar.access$400(COUIVerticalSeekBar.this);
                COUIVerticalSeekBar cOUIVerticalSeekBar = COUIVerticalSeekBar.this;
                onSeekBarChangeListenerAccess$400.onProgressChanged(cOUIVerticalSeekBar, COUIVerticalSeekBar.access$500(cOUIVerticalSeekBar), this.val$isFromUser);
            }
            COUIVerticalSeekBar.this.onStopTrackingTouch(this.val$isFromUser);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUIVerticalSeekBar.access$400(COUIVerticalSeekBar.this) != null) {
                OnSeekBarChangeListener onSeekBarChangeListenerAccess$400 = COUIVerticalSeekBar.access$400(COUIVerticalSeekBar.this);
                COUIVerticalSeekBar cOUIVerticalSeekBar = COUIVerticalSeekBar.this;
                onSeekBarChangeListenerAccess$400.onProgressChanged(cOUIVerticalSeekBar, COUIVerticalSeekBar.access$500(cOUIVerticalSeekBar), this.val$isFromUser);
            }
            COUIVerticalSeekBar.this.onStopTrackingTouch(this.val$isFromUser);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUIVerticalSeekBar.this.onStartTrackingTouch(this.val$isFromUser);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.seekbar.COUIVerticalSeekBar$4, reason: invalid class name */
    public class AnonymousClass4 implements Animator.AnimatorListener {
        public AnonymousClass4() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUIVerticalSeekBar.access$602(COUIVerticalSeekBar.this, true);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.seekbar.COUIVerticalSeekBar$5, reason: invalid class name */
    public class AnonymousClass5 implements Runnable {
        public AnonymousClass5() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUIVerticalSeekBar cOUIVerticalSeekBar = COUIVerticalSeekBar.this;
            if (cOUIVerticalSeekBar.mIsDragging) {
                cOUIVerticalSeekBar.performHapticFeedback(305, 0);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.seekbar.COUIVerticalSeekBar$6, reason: invalid class name */
    public class AnonymousClass6 implements Runnable {
        public AnonymousClass6() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUIVerticalSeekBar cOUIVerticalSeekBar = COUIVerticalSeekBar.this;
            if (cOUIVerticalSeekBar.mIsDragging) {
                LinearmotorVibrator linearmotorVibrator = (LinearmotorVibrator) cOUIVerticalSeekBar.mLinearMotorVibrator;
                int i3 = cOUIVerticalSeekBar.mProgress;
                int i4 = cOUIVerticalSeekBar.mMin;
                VibrateUtils.setLinearMotorVibratorStrength(linearmotorVibrator, 152, i3 - i4, cOUIVerticalSeekBar.mMax - i4, 200, 2000);
            }
        }
    }

    public interface OnDeformedListener {
        default void onHeightDeformedChanged(float f3, float f4) {
        }

        default void onScaleChanged(DeformedValueBean deformedValueBean) {
        }
    }

    public interface OnSeekBarChangeListener {
        void onProgressChanged(COUIVerticalSeekBar cOUIVerticalSeekBar, int i3, boolean z2);

        void onStartTrackingTouch(COUIVerticalSeekBar cOUIVerticalSeekBar);

        void onStopTrackingTouch(COUIVerticalSeekBar cOUIVerticalSeekBar);
    }

    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new AnonymousClass1();
        int mSaveProgress;

        /* JADX INFO: renamed from: com.coui.appcompat.seekbar.COUIVerticalSeekBar$SavedState$1, reason: invalid class name */
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

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i3) {
            super.writeToParcel(parcel, i3);
            parcel.writeInt(this.mSaveProgress);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.mSaveProgress = parcel.readInt();
        }
    }

    public COUIVerticalSeekBar(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void a(COUIVerticalSeekBar cOUIVerticalSeekBar, ValueAnimator valueAnimator) {
        cOUIVerticalSeekBar.getClass();
        cOUIVerticalSeekBar.setTouchScale(((Float) valueAnimator.getAnimatedValue(HOLDER_SCALE)).floatValue());
        cOUIVerticalSeekBar.setLocalProgress(cOUIVerticalSeekBar.getProgressLimit(Math.round((cOUIVerticalSeekBar.mMax - cOUIVerticalSeekBar.mMin) * cOUIVerticalSeekBar.mScale) + cOUIVerticalSeekBar.mMin));
        cOUIVerticalSeekBar.invalidate();
    }

    public static /* synthetic */ String access$000(COUIVerticalSeekBar cOUIVerticalSeekBar) {
        return cOUIVerticalSeekBar.mSeekBarRoleDescription;
    }

    public static /* synthetic */ String access$100(COUIVerticalSeekBar cOUIVerticalSeekBar, int i3) {
        return cOUIVerticalSeekBar.formatStateDescription(i3);
    }

    public static /* synthetic */ int access$200(COUIVerticalSeekBar cOUIVerticalSeekBar) {
        return cOUIVerticalSeekBar.mIncrement;
    }

    public static /* synthetic */ float access$300(COUIVerticalSeekBar cOUIVerticalSeekBar) {
        return cOUIVerticalSeekBar.mFastMoveScaleOffsetY;
    }

    public static /* synthetic */ float access$302(COUIVerticalSeekBar cOUIVerticalSeekBar, float f3) {
        cOUIVerticalSeekBar.mFastMoveScaleOffsetY = f3;
        return f3;
    }

    public static /* synthetic */ OnSeekBarChangeListener access$400(COUIVerticalSeekBar cOUIVerticalSeekBar) {
        return cOUIVerticalSeekBar.mOnSeekBarChangeListener;
    }

    public static /* synthetic */ int access$500(COUIVerticalSeekBar cOUIVerticalSeekBar) {
        return cOUIVerticalSeekBar.mRealProgress;
    }

    public static /* synthetic */ boolean access$602(COUIVerticalSeekBar cOUIVerticalSeekBar, boolean z2) {
        cOUIVerticalSeekBar.mHasEnlarge = z2;
        return z2;
    }

    private void addOs16SmoothRoundRectToPath(OplusPathAdapter oplusPathAdapter, Rect rect, float f3, float f4, float f5) {
        this.mTmpRectF.set(rect);
        addOs16SmoothRoundRectToPath(oplusPathAdapter, this.mTmpRectF, f3, f4, f5);
    }

    private void attemptClaimDrag() {
        if (getParent() instanceof ViewGroup) {
            ((ViewGroup) getParent()).requestDisallowInterceptTouchEvent(true);
        }
    }

    public static /* synthetic */ void b(COUIVerticalSeekBar cOUIVerticalSeekBar, ValueAnimator valueAnimator) {
        if (cOUIVerticalSeekBar.mPixPerProgress > 0.0f) {
            float fFloatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            cOUIVerticalSeekBar.setLocalProgress((int) (fFloatValue / cOUIVerticalSeekBar.mPixPerProgress));
            cOUIVerticalSeekBar.mScale = cOUIVerticalSeekBar.getSeekBarHeightFloat() > 0.0f ? (fFloatValue - (cOUIVerticalSeekBar.mMin * cOUIVerticalSeekBar.mPixPerProgress)) / cOUIVerticalSeekBar.getSeekBarHeightFloat() : 0.0f;
            cOUIVerticalSeekBar.invalidate();
        }
    }

    private boolean bottomDeformedChange() {
        if (!this.mIsSupportDeformation) {
            return false;
        }
        float heightBottomDeformedValue = getHeightBottomDeformedValue();
        if (this.mCurBottomDeformationValue == heightBottomDeformedValue) {
            return false;
        }
        this.mCurBottomDeformationValue = heightBottomDeformedValue;
        return true;
    }

    public static /* synthetic */ void c(COUIVerticalSeekBar cOUIVerticalSeekBar, ValueAnimator valueAnimator) {
        cOUIVerticalSeekBar.getCurAnimatorValues(valueAnimator);
        cOUIVerticalSeekBar.invalidate();
    }

    private float calculateDamping() {
        float f3 = this.mDamping;
        if (f3 != 0.0f) {
            return f3;
        }
        return 0.4f;
    }

    private void calculateFlingDeformationValue(float f3) {
        if (f3 > 1.0f) {
            double d3 = f3 - 1.0f;
            this.mHeightBottomDeformedUpValue = computeValue(d3, this.mMaxBottomMovingDistance);
            this.mHeightTopDeformedUpValue = computeValue(d3, this.mMaxTopMovingDistance + this.mMaxTopHeightDeformedValue);
            this.mWidthDeformedValue = computeValue(d3, this.mMaxWidthDeformedValue);
            heightDeformedChanged();
            return;
        }
        if (f3 >= 0.0f) {
            resetDeformationValue();
            return;
        }
        double dAbs = Math.abs(f3);
        this.mHeightTopDeformedDownValue = computeValue(dAbs, this.mMaxTopMovingDistance);
        this.mHeightBottomDeformedDownValue = computeValue(dAbs, this.mMaxBottomMovingDistance + this.mMaxBottomHeightDeformedValue);
        this.mWidthDeformedValue = computeValue(dAbs, this.mMaxWidthDeformedValue);
        heightDeformedChanged();
    }

    private void calculateTouchDeformationValue() {
        float f3 = this.mScale;
        if (f3 >= 1.0f) {
            double d3 = (f3 - 1.0f) / 5.0f;
            this.mHeightBottomDeformedUpValue = computeValue(d3, this.mMaxBottomMovingDistance);
            this.mHeightTopDeformedUpValue = computeValue(d3, this.mMaxTopMovingDistance + this.mMaxTopHeightDeformedValue);
            this.mWidthDeformedValue = computeValue(d3, this.mMaxWidthDeformedValue);
            heightDeformedChanged();
            return;
        }
        if (f3 <= 0.0f) {
            double dAbs = Math.abs(f3) / 5.0f;
            this.mHeightTopDeformedDownValue = computeValue(dAbs, this.mMaxTopMovingDistance);
            this.mHeightBottomDeformedDownValue = computeValue(dAbs, this.mMaxBottomMovingDistance + this.mMaxBottomHeightDeformedValue);
            this.mWidthDeformedValue = computeValue(dAbs, this.mMaxWidthDeformedValue);
            heightDeformedChanged();
        }
    }

    private void cancelAnim(ValueAnimator valueAnimator) {
        if (valueAnimator == null || !valueAnimator.isRunning()) {
            return;
        }
        valueAnimator.cancel();
    }

    private void checkPhysicsAnimator() {
        if (this.mIsPhysicsEnable) {
            if (this.mPhysicalAnimator == null || Math.abs(this.mLastPixelPhysicalSize - Compat.physicalSizeToPixels(100.0f)) > 0.001f) {
                stopPhysicsMove();
                initPhysicsAnimator(getContext());
            }
        }
    }

    private void clearDeformationValue() {
        int i3 = this.mProgress;
        if (i3 <= this.mMin || i3 >= this.mMax) {
            return;
        }
        resetDeformationValue();
    }

    private void clearVibratorPendingTasks() {
        ExecutorService executorService = this.mVibratorExecutor;
        if (executorService instanceof ThreadPoolExecutor) {
            ((ThreadPoolExecutor) executorService).getQueue().clear();
        }
    }

    private float computeValue(double d3, float f3) {
        return (int) (((double) f3) * (1.0d - Math.exp(d3 * DEFORMATION_FORMULA_CONSTANT)));
    }

    private ValueAnimator createClickAnimator(boolean z2) {
        Interpolator interpolator;
        ValueAnimator valueAnimator = new ValueAnimator();
        if (z2 || (interpolator = this.mCustomProgressAnimInterpolator) == null) {
            valueAnimator.setInterpolator(THUMB_ANIMATE_INTERPOLATOR);
        } else {
            valueAnimator.setInterpolator(interpolator);
        }
        valueAnimator.addUpdateListener(new c(this));
        valueAnimator.addListener(new AnonymousClass3(z2));
        return valueAnimator;
    }

    private Paint createPaint() {
        Paint paint = new Paint(1);
        paint.setDither(true);
        return paint;
    }

    public static /* synthetic */ void d(COUIVerticalSeekBar cOUIVerticalSeekBar, ValueAnimator valueAnimator) {
        cOUIVerticalSeekBar.getCurAnimatorValues(valueAnimator);
        cOUIVerticalSeekBar.invalidate();
    }

    private void drawBackgroundByClip(Canvas canvas, boolean z2) {
        this.mBackgroundPath.reset();
        if (z2) {
            int i3 = this.mSmoothStyleType;
            if (i3 == 0) {
                OplusPath oplusPath = new OplusPath(this.mBackgroundPath);
                Rect rect = this.mBackgroundRect;
                float f3 = rect.left;
                float f4 = rect.top;
                float f5 = rect.right;
                float f6 = rect.bottom;
                float f7 = this.mCurBackgroundRadius;
                oplusPath.addSmoothRoundRect(f3, f4, f5, f6, f7, f7, this.mBackgroundRoundCornerWeight, Path.Direction.CCW);
            } else if (i3 == 1) {
                if (this.mBackgroundPathAdapter == null) {
                    this.mBackgroundPathAdapter = new OplusPathAdapter(this.mBackgroundPath, this.mSmoothStyleType);
                }
                OplusPathAdapter oplusPathAdapter = this.mBackgroundPathAdapter;
                Rect rect2 = this.mBackgroundRect;
                float f8 = this.mCurBackgroundRadius;
                addOs16SmoothRoundRectToPath(oplusPathAdapter, rect2, f8, f8, this.mBackgroundRoundCornerWeight);
            } else {
                Path path = this.mBackgroundPath;
                Rect rect3 = this.mBackgroundRect;
                float f9 = rect3.left;
                float f10 = rect3.top;
                float f11 = rect3.right;
                float f12 = rect3.bottom;
                float f13 = this.mCurBackgroundRadius;
                path.addRoundRect(f9, f10, f11, f12, f13, f13, Path.Direction.CCW);
            }
        } else {
            Path path2 = this.mBackgroundPath;
            Rect rect4 = this.mBackgroundRect;
            float f14 = rect4.left;
            float f15 = rect4.top;
            float f16 = rect4.right;
            float f17 = rect4.bottom;
            float f18 = this.mCurBackgroundRadius;
            path2.addRoundRect(f14, f15, f16, f17, f18, f18, Path.Direction.CCW);
        }
        canvas.save();
        canvas.clipPath(this.mBackgroundPath);
        this.mInactiveTrackDrawable.draw(canvas);
        canvas.restore();
    }

    private void drawBackgroundByPaint(Canvas canvas, boolean z2) {
        this.mBackgroundPaint.setColor(this.mBackgroundColor);
        if (!z2) {
            Rect rect = this.mBackgroundRect;
            float f3 = rect.left;
            float f4 = rect.top;
            float f5 = rect.right;
            float f6 = rect.bottom;
            float f7 = this.mCurBackgroundRadius;
            canvas.drawRoundRect(f3, f4, f5, f6, f7, f7, this.mBackgroundPaint);
            return;
        }
        int i3 = this.mSmoothStyleType;
        if (i3 == 0) {
            OplusCanvas oplusCanvas = new OplusCanvas(canvas);
            Rect rect2 = this.mBackgroundRect;
            float f8 = rect2.left;
            float f9 = rect2.top;
            float f10 = rect2.right;
            float f11 = rect2.bottom;
            float f12 = this.mCurBackgroundRadius;
            oplusCanvas.drawSmoothRoundRect(f8, f9, f10, f11, f12, f12, this.mBackgroundPaint, this.mBackgroundRoundCornerWeight);
            return;
        }
        if (i3 != 1) {
            Rect rect3 = this.mBackgroundRect;
            float f13 = rect3.left;
            float f14 = rect3.top;
            float f15 = rect3.right;
            float f16 = rect3.bottom;
            float f17 = this.mCurBackgroundRadius;
            canvas.drawRoundRect(f13, f14, f15, f16, f17, f17, this.mBackgroundPaint);
            return;
        }
        if (this.mBackgroundPathAdapter == null) {
            this.mBackgroundPathAdapter = new OplusPathAdapter(this.mBackgroundPath, this.mSmoothStyleType);
        }
        this.mBackgroundPath.reset();
        OplusPathAdapter oplusPathAdapter = this.mBackgroundPathAdapter;
        Rect rect4 = this.mBackgroundRect;
        float f18 = this.mCurBackgroundRadius;
        addOs16SmoothRoundRectToPath(oplusPathAdapter, rect4, f18, f18, this.mBackgroundRoundCornerWeight);
        canvas.drawPath(this.mBackgroundPath, this.mBackgroundPaint);
    }

    private void drawProgress(Canvas canvas) {
        boolean z2 = this.mIsSupportSmoothRoundCorner && this.mProgressRoundCornerWeight != 0.0f;
        this.mProgressPaint.setColor(this.mProgressColor);
        this.mClipProgressPath.reset();
        if (z2) {
            int i3 = this.mSmoothStyleType;
            if (i3 == 0) {
                OplusPath oplusPath = new OplusPath(this.mClipProgressPath);
                Rect rect = this.mClipProgressRect;
                float f3 = rect.left;
                float f4 = rect.top;
                float f5 = rect.right;
                float f6 = rect.bottom;
                float f7 = this.mCurProgressRadius;
                oplusPath.addSmoothRoundRect(f3, f4, f5, f6, f7, f7, this.mProgressRoundCornerWeight, Path.Direction.CCW);
            } else if (i3 == 1) {
                if (this.mProgressPathAdapter == null) {
                    this.mProgressPathAdapter = new OplusPathAdapter(this.mClipProgressPath, this.mSmoothStyleType);
                }
                OplusPathAdapter oplusPathAdapter = this.mProgressPathAdapter;
                Rect rect2 = this.mClipProgressRect;
                float f8 = this.mCurProgressRadius;
                addOs16SmoothRoundRectToPath(oplusPathAdapter, rect2, f8, f8, this.mProgressRoundCornerWeight);
            } else {
                Path path = this.mClipProgressPath;
                Rect rect3 = this.mClipProgressRect;
                float f9 = rect3.left;
                float f10 = rect3.top;
                float f11 = rect3.right;
                float f12 = rect3.bottom;
                float f13 = this.mCurProgressRadius;
                path.addRoundRect(f9, f10, f11, f12, f13, f13, Path.Direction.CCW);
            }
        } else {
            Path path2 = this.mClipProgressPath;
            Rect rect4 = this.mClipProgressRect;
            float f14 = rect4.left;
            float f15 = rect4.top;
            float f16 = rect4.right;
            float f17 = rect4.bottom;
            float f18 = this.mCurProgressRadius;
            path2.addRoundRect(f14, f15, f16, f17, f18, f18, Path.Direction.CCW);
        }
        canvas.save();
        canvas.clipPath(this.mClipProgressPath);
        if (this.mShowThumb) {
            Rect rect5 = this.mProgressRect;
            float f19 = rect5.top;
            float f20 = this.mThumbOutWidth;
            rect5.top = (int) (f19 - (f20 / 2.0f));
            rect5.bottom = (int) (rect5.bottom + (f20 / 2.0f));
            this.mTmpRectF.set(rect5);
            if (z2) {
                int i4 = this.mSmoothStyleType;
                if (i4 == 0) {
                    OplusCanvas oplusCanvas = new OplusCanvas(canvas);
                    RectF rectF = this.mTmpRectF;
                    float f21 = this.mCurProgressRadius;
                    oplusCanvas.drawSmoothRoundRect(rectF, f21, f21, this.mProgressPaint, this.mProgressRoundCornerWeight);
                } else if (i4 == 1) {
                    if (this.mProgressInnerPathAdapter == null) {
                        this.mProgressInnerPathAdapter = new OplusPathAdapter(this.mProgressInnerSmoothPath, this.mSmoothStyleType);
                    }
                    this.mProgressInnerSmoothPath.reset();
                    OplusPathAdapter oplusPathAdapter2 = this.mProgressInnerPathAdapter;
                    RectF rectF2 = this.mTmpRectF;
                    float f22 = this.mCurProgressRadius;
                    addOs16SmoothRoundRectToPath(oplusPathAdapter2, rectF2, f22, f22, this.mProgressRoundCornerWeight);
                    canvas.drawPath(this.mProgressInnerSmoothPath, this.mProgressPaint);
                } else {
                    RectF rectF3 = this.mTmpRectF;
                    float f23 = this.mCurProgressRadius;
                    canvas.drawRoundRect(rectF3, f23, f23, this.mProgressPaint);
                }
            } else {
                RectF rectF4 = this.mTmpRectF;
                float f24 = this.mCurProgressRadius;
                canvas.drawRoundRect(rectF4, f24, f24, this.mProgressPaint);
            }
        } else {
            canvas.drawRect(this.mProgressRect, this.mProgressPaint);
        }
        canvas.restore();
    }

    private void drawText(Canvas canvas) {
        if (TextUtils.isEmpty(this.mTextContent)) {
            return;
        }
        canvas.drawText(this.mTextContent, (getWidth() - this.mTextPaint.measureText(this.mTextContent)) / 2.0f, ((this.mTextMarginTop - this.mFmi.ascent) + this.mHeightTopDeformedDownValue) - this.mHeightTopDeformedUpValue, this.mTextPaint);
    }

    private void drawThumb(Canvas canvas) {
        Bitmap bitmap;
        float f3 = this.mThumbPosition;
        float f4 = this.mThumbOutWidth;
        float f5 = f3 - (f4 / 2.0f);
        float f6 = f3 + (f4 / 2.0f);
        int seekBarCenterX = getSeekBarCenterX();
        if (getThumb() != null && (bitmap = this.mThumbBitmap) != null) {
            canvas.drawBitmap(bitmap, seekBarCenterX - (this.mThumbOutWidth / 2.0f), f5, this.mThumbPaint);
            return;
        }
        RectF rectF = this.mTmpRectF;
        float f7 = seekBarCenterX;
        float f8 = this.mThumbOutWidth;
        rectF.set(f7 - (f8 / 2.0f), f5, f7 + (f8 / 2.0f), f6);
        this.mThumbPaint.setColor(this.mThumbColor);
        if (!this.mIsSupportSmoothRoundCorner || this.mThumbOutRoundCornerWeight == 0.0f) {
            RectF rectF2 = this.mTmpRectF;
            float f9 = this.mThumbOutRadius;
            canvas.drawRoundRect(rectF2, f9, f9, this.mThumbPaint);
            return;
        }
        int i3 = this.mSmoothStyleType;
        if (i3 == 0) {
            OplusCanvas oplusCanvas = new OplusCanvas(canvas);
            RectF rectF3 = this.mTmpRectF;
            float f10 = this.mThumbOutRadius;
            oplusCanvas.drawSmoothRoundRect(rectF3, f10, f10, this.mThumbPaint, this.mThumbOutRoundCornerWeight);
            return;
        }
        if (i3 != 1) {
            RectF rectF4 = this.mTmpRectF;
            float f11 = this.mThumbOutRadius;
            canvas.drawRoundRect(rectF4, f11, f11, this.mThumbPaint);
            return;
        }
        if (this.mThumbPathAdapter == null) {
            this.mThumbPathAdapter = new OplusPathAdapter(this.mThumbSmoothPath, this.mSmoothStyleType);
        }
        this.mThumbSmoothPath.reset();
        OplusPathAdapter oplusPathAdapter = this.mThumbPathAdapter;
        RectF rectF5 = this.mTmpRectF;
        float f12 = this.mThumbOutRadius;
        addOs16SmoothRoundRectToPath(oplusPathAdapter, rectF5, f12, f12, this.mThumbOutRoundCornerWeight);
        canvas.drawPath(this.mThumbSmoothPath, this.mThumbPaint);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int iMax = Math.max(1, drawable.getIntrinsicHeight());
        int iMax2 = Math.max(1, drawable.getIntrinsicWidth());
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(iMax2, iMax, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        drawable.setBounds(0, 0, iMax2, iMax);
        drawable.draw(canvas);
        return bitmapCreateBitmap;
    }

    private void ensureSize() {
        resetProgressSize();
        this.mVerticalPaddingScale = this.mBackgroundEnlargeScale != 1.0f ? (getResources().getDimensionPixelSize(R.dimen.coui_vertical_seekbar_progress_pressed_padding_vertical) + (this.mBackgroundRadius * this.mBackgroundRadiusEnlargeScale)) / this.mPaddingVertical : 1.0f;
        float f3 = this.mProgressRadius;
        this.mCurProgressRadius = f3;
        this.mCurBackgroundRadius = this.mBackgroundRadius;
        this.mThumbOutRadius = f3 * this.mProgressRadiusEnlargeScale;
        this.mThumbOutRoundCornerWeight = this.mProgressRoundCornerWeight;
        float f4 = this.mProgressWidth;
        this.mCurProgressWidth = f4;
        this.mCurBackgroundWidth = this.mBackgroundWidth;
        this.mThumbOutWidth = f4 * this.mProgressEnlargeScale;
        this.mCurPaddingVertical = this.mPaddingVertical;
        COUILog.i(TAG, "COUIVerticalSeekBar ensureSize : mIsProgressFull:" + this.mIsProgressFull + ",mBackgroundRadius:" + this.mBackgroundRadius + ",mBackgroundWidth:" + this.mBackgroundWidth + ",mBackgroundEnlargeScale" + this.mBackgroundEnlargeScale + ",mProgressRadius:" + this.mProgressRadius + ",mProgressWidth:" + this.mProgressWidth + ",mProgressEnlargeScale" + this.mProgressEnlargeScale + ",mPaddingVertical" + this.mPaddingVertical);
        updateBehavior();
        updatePixPerProgress();
        initAnimation();
    }

    private void executeEnlargeAnim(long j3, Interpolator interpolator) {
        if (this.mEnlargeAnimator == null) {
            ValueAnimator enlargeAnimator = getEnlargeAnimator(j3, interpolator);
            this.mEnlargeAnimator = enlargeAnimator;
            enlargeAnimator.addListener(new AnonymousClass4());
        }
        setEnlargeAnimatorValues(this.mEnlargeAnimator);
        this.mEnlargeAnimator.start();
    }

    private void executeReleaseAnim(long j3, Interpolator interpolator) {
        if (this.mReleaseAnimator == null) {
            this.mReleaseAnimator = getReleaseAnimator(j3, interpolator);
        }
        setReleaseAnimatorValues(this.mReleaseAnimator);
        this.mHasEnlarge = false;
        this.mReleaseAnimator.start();
    }

    private void flingBehaviorAfterDeformationDrag() {
        if (this.mFlingValueHolder == null || this.mFlingBehavior == null || !this.mIsSupportDeformation) {
            return;
        }
        float f3 = this.mScale;
        if (f3 > 1.0f || f3 < 0.0f) {
            this.mLastProgress = 0;
            float normalSeekBarHeightFloat = getNormalSeekBarHeightFloat();
            int i3 = this.mMax - this.mMin;
            float f4 = i3 > 0 ? normalSeekBarHeightFloat / i3 : 0.0f;
            float deformationFlingScale = getDeformationFlingScale();
            float f5 = i3 * deformationFlingScale * f4;
            this.mFlingValueHolder.setStartValue(f5);
            this.mFlingBehavior.start();
            COUILog.i(TAG, "flingBehaviorAfterDeformationDrag ** range = " + i3 + " height = " + normalSeekBarHeightFloat + " pixPerProgress = " + f4 + " scale = " + deformationFlingScale + " startValue = " + f5);
        }
    }

    private void flingBehaviorAfterEndDrag(float f3) {
        if (this.mFlingValueHolder == null || this.mFlingBehavior == null) {
            return;
        }
        this.mLastProgress = 0;
        float normalSeekBarHeightFloat = getNormalSeekBarHeightFloat();
        int i3 = this.mMax;
        int i4 = this.mMin;
        int i5 = i3 - i4;
        float f4 = i5 > 0 ? normalSeekBarHeightFloat / i5 : 0.0f;
        if (this.mIsSupportDeformation) {
            float deformationFlingScale = getDeformationFlingScale();
            float f5 = i5 * deformationFlingScale * f4;
            this.mFlingValueHolder.setStartValue(f5);
            COUILog.i(TAG, "flingBehaviorAfterEndDrag ** range = " + i5 + " height = " + normalSeekBarHeightFloat + " pixPerProgress = " + f4 + " scale = " + deformationFlingScale + " startValue = " + f5 + " velocity = " + f3 + " mMax = " + this.mMax + " mMin = " + this.mMin);
        } else {
            float f6 = (this.mProgress - i4) * f4;
            this.mFlingValueHolder.setStartValue(f6);
            COUILog.i(TAG, "flingBehaviorAfterEndDrag ** range = " + i5 + " height = " + normalSeekBarHeightFloat + " pixPerProgress = " + f4 + " startValue = " + f6 + " velocity = " + f3 + " mMax = " + this.mMax + " mMin = " + this.mMin);
        }
        this.mFlingBehavior.start(-f3);
    }

    private String formatStateDescription(int i3) {
        Locale locale = getResources().getConfiguration().getLocales().get(0);
        if (locale != null && !locale.equals(this.mCachedLocale)) {
            this.mCachedLocale = locale;
            this.mPercentFormat = NumberFormat.getPercentInstance(locale);
        }
        NumberFormat numberFormat = this.mPercentFormat;
        return numberFormat != null ? numberFormat.format(getPercent(i3)) : Integer.toString(i3);
    }

    private ValueAnimator getButtonDeformationAnimator() {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.addUpdateListener(new b(this));
        return valueAnimator;
    }

    private void getCurAnimatorValues(ValueAnimator valueAnimator) {
        this.mCurProgressRadius = (int) ((Float) valueAnimator.getAnimatedValue("progressRadius")).floatValue();
        this.mCurBackgroundRadius = (int) ((Float) valueAnimator.getAnimatedValue("backgroundRadius")).floatValue();
        this.mCurProgressWidth = (int) ((Float) valueAnimator.getAnimatedValue("progressWidth")).floatValue();
        this.mCurBackgroundWidth = (int) ((Float) valueAnimator.getAnimatedValue("backgroundWidth")).floatValue();
        this.mCurPaddingVertical = ((Float) valueAnimator.getAnimatedValue("animatePadding")).floatValue();
    }

    private float getDeformationFlingScale() {
        float f3 = this.mScale;
        if (f3 > 1.0f) {
            return ((f3 - 1.0f) / 5.0f) + 1.0f;
        }
        return f3 < 0.0f ? f3 / 5.0f : f3;
    }

    private ValueAnimator getEnlargeAnimator(long j3, Interpolator interpolator) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(j3);
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.addUpdateListener(new d(this));
        return valueAnimator;
    }

    private Spring getFastMoveSpring() {
        if (this.mFastMoveSpring == null) {
            initFastMoveAnimation();
        }
        return this.mFastMoveSpring;
    }

    private float getHeightBottomDeformedValue() {
        return this.mHeightBottomDeformedDownValue - this.mHeightBottomDeformedUpValue;
    }

    private float getHeightTopDeformedValue() {
        return this.mHeightTopDeformedDownValue - this.mHeightTopDeformedUpValue;
    }

    private int getNormalSeekBarHeight() {
        return (int) getNormalSeekBarHeightFloat();
    }

    private float getNormalSeekBarHeightFloat() {
        return ((getHeight() - getPaddingTop()) - getPaddingBottom()) - (this.mPaddingVertical * 2.0f);
    }

    private float getPercent(int i3) {
        float max = getMax();
        float min = getMin();
        float f3 = i3;
        float f4 = max - min;
        if (f4 <= 0.0f) {
            return 0.0f;
        }
        return Math.max(0.0f, Math.min(1.0f, (f3 - min) / f4));
    }

    private int getProgressLimit(int i3) {
        int i4 = this.mMax;
        int i5 = this.mMin;
        int i6 = i4 - i5;
        return Math.max(i5 - i6, Math.min(i3, i4 + i6));
    }

    private int getRealProgress(int i3) {
        return Math.max(this.mMin, Math.min(i3, this.mMax));
    }

    private float getRealScale(float f3) {
        return Math.max(0.0f, Math.min(f3, 1.0f));
    }

    private ValueAnimator getReleaseAnimator(long j3, Interpolator interpolator) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(j3);
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.addUpdateListener(new e(this));
        return valueAnimator;
    }

    private void heightDeformedChanged() {
        if (this.mOnDeformedListener != null) {
            boolean z2 = topDeformedChange();
            boolean zBottomDeformedChange = bottomDeformedChange();
            if (z2 || zBottomDeformedChange) {
                this.mOnDeformedListener.onHeightDeformedChanged(this.mCurTopDeformationValue, this.mCurBottomDeformationValue);
            }
        }
    }

    private void initAnimation() {
        ValueAnimator valueAnimator = this.mTouchEnlargeAnimator;
        if (valueAnimator == null) {
            this.mTouchEnlargeAnimator = getEnlargeAnimator(183L, PROGRESS_SCALE_INTERPOLATOR);
        } else {
            cancelAnim(valueAnimator);
        }
        setEnlargeAnimatorValues(this.mTouchEnlargeAnimator);
    }

    private void initFastMoveAnimation() {
        if (this.mFastMoveSpring != null) {
            return;
        }
        Spring springCreateSpring = SpringSystem.create().createSpring();
        this.mFastMoveSpring = springCreateSpring;
        springCreateSpring.setSpringConfig(this.mFastMoveSpringConfig);
        this.mFastMoveSpring.addListener(new AnonymousClass2());
    }

    private void initOrResetVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
    }

    private void initPhysicsAnimator(Context context) {
        this.mPhysicalAnimator = PhysicalAnimator.create(context);
        this.mFlingValueHolder = new FloatValueHolder(0.0f);
        float normalSeekBarHeightFloat = getNormalSeekBarHeightFloat();
        this.mHistorySeekBarHeight = normalSeekBarHeightFloat;
        String str = "COUIVerticalSeekBar initPhysicsAnimator : setActiveFrame:" + normalSeekBarHeightFloat + ",mPaddingVertical = " + this.mPaddingVertical + ",Thread_name = " + Thread.currentThread().getName() + ",hashCode = " + hashCode() + ",time = " + System.currentTimeMillis();
        COUILog.i(TAG, str);
        updateParameter(str);
        FlingBehavior flingBehavior = (FlingBehavior) ((FlingBehavior) new FlingBehavior(4, 0.0f, normalSeekBarHeightFloat).withProperty(this.mFlingValueHolder)).setSpringProperty(this.mFlingFrequency, this.mFlingDampingRatio).applyTo(null);
        this.mFlingBehavior = flingBehavior;
        flingBehavior.setLinearDamping(this.mFlingLinearDamping);
        this.mPhysicalAnimator.addBehavior(this.mFlingBehavior);
        this.mPhysicalAnimator.addAnimationListener(this.mFlingBehavior, this);
        this.mPhysicalAnimator.addAnimationUpdateListener(this.mFlingBehavior, this);
        this.mLastPixelPhysicalSize = Compat.physicalSizeToPixels(100.0f);
    }

    private void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void initView() {
        this.mBackgroundColor = getColor(this, this.mBackgroundColorStateList, COUIContextUtil.getColor(getContext(), R.color.coui_seekbar_background_color_normal));
        ColorStateList colorStateList = this.mProgressColorStateList;
        Context context = getContext();
        int i3 = R.color.coui_seekbar_progress_color_normal;
        this.mProgressColor = getColor(this, colorStateList, COUIContextUtil.getColor(context, i3));
        this.mThumbColor = getColor(this, this.mThumbColorStateList, COUIContextUtil.getColor(getContext(), i3));
        this.mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        ViewCompat.setAccessibilityDelegate(this, new AnonymousClass1());
        this.mBackgroundPaint = createPaint();
        this.mProgressPaint = createPaint();
        this.mThumbPaint = createPaint();
        TextPaint textPaint = new TextPaint(1);
        this.mTextPaint = textPaint;
        textPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.coui_seekbar_text_size));
        refreshTextColor();
        this.mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        this.mFmi = this.mTextPaint.getFontMetricsInt();
    }

    private void invalidateProgress(MotionEvent motionEvent) {
        float y2 = motionEvent.getY();
        float seekBarHeightFloat = getSeekBarHeightFloat();
        float f3 = this.mCurProgressRadius;
        float f4 = seekBarHeightFloat + (2.0f * f3);
        this.mScale = Math.max(0.0f, Math.min(f4 > 0.0f ? (((getHeight() - getPaddingBottom()) - (this.mCurPaddingVertical - f3)) - y2) / f4 : 0.0f, 1.0f));
        int progressLimit = getProgressLimit(Math.round((this.mScale * (getMax() - getMin())) + getMin()));
        int i3 = this.mRealProgress;
        setLocalProgress(progressLimit);
        invalidate();
        int i4 = this.mRealProgress;
        if (i3 != i4) {
            OnSeekBarChangeListener onSeekBarChangeListener = this.mOnSeekBarChangeListener;
            if (onSeekBarChangeListener != null) {
                onSeekBarChangeListener.onProgressChanged(this, i4, true);
            }
            performFeedback();
        }
    }

    private boolean isDeformationFling() {
        PhysicalAnimator physicalAnimator;
        if (!this.mIsSupportDeformation) {
            return false;
        }
        float f3 = this.mScale;
        return (f3 > 1.0f || f3 < 0.0f) && (physicalAnimator = this.mPhysicalAnimator) != null && physicalAnimator.isAnimatorRunning();
    }

    private boolean isMoveFollowHand() {
        return this.mMoveType != 2;
    }

    private void logOnAnimationEnd(BaseBehavior baseBehavior) {
        Object animatedValue = baseBehavior.getAnimatedValue();
        if (animatedValue == null) {
            return;
        }
        float fFloatValue = ((Float) animatedValue).floatValue();
        float normalSeekBarHeightFloat = getNormalSeekBarHeightFloat();
        COUILog.i(TAG, "logOnAnimationEnd ** flingValue = " + fFloatValue + " height = " + normalSeekBarHeightFloat + " scale = " + (normalSeekBarHeightFloat > 0.0f ? fFloatValue / normalSeekBarHeightFloat : 0.0f) + " mScale = " + this.mScale + " mClipProgressRect = " + this.mClipProgressRect.toString() + " mBackgroundRect = " + this.mBackgroundRect.toString() + " mProgressRect = " + this.mProgressRect.toString() + " mLastProgress = " + this.mLastProgress + " Behavior = " + baseBehavior);
        printLogHistory();
    }

    private void printLogHistory() {
        if (this.mHistoryLogQueue.isEmpty()) {
            Log.d(TAG, "No parameter history available.");
            return;
        }
        StringBuilder sb = new StringBuilder("Parameter Log History:\n");
        for (String str : this.mHistoryLogQueue) {
            sb.append("-> ");
            sb.append((Object) str);
            sb.append('\n');
        }
        COUILog.i(TAG, sb.toString());
    }

    private void recycleVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private void refreshTextColor() {
        this.mTextPaint.setColor(this.mTextColor);
        if (this.mShowTextShadow) {
            this.mTextPaint.setShadowLayer(25.0f, 0.0f, 8.0f, this.mTextColor);
        } else {
            this.mTextPaint.clearShadowLayer();
        }
    }

    private void resetDeformationValue() {
        if (this.mIsSupportDeformation) {
            this.mHeightTopDeformedUpValue = 0.0f;
            this.mHeightBottomDeformedUpValue = 0.0f;
            this.mWidthDeformedValue = 0.0f;
            this.mHeightTopDeformedDownValue = 0.0f;
            this.mHeightBottomDeformedDownValue = 0.0f;
            heightDeformedChanged();
        }
    }

    private void resetProgressSize() {
        if (this.mIsProgressFull) {
            this.mProgressRadius = this.mBackgroundRadius;
            this.mProgressRoundCornerWeight = this.mBackgroundRoundCornerWeight;
            this.mProgressWidth = this.mBackgroundWidth;
            this.mProgressEnlargeScale = this.mBackgroundEnlargeScale;
            this.mProgressRadiusEnlargeScale = this.mBackgroundRadiusEnlargeScale;
        }
    }

    private void setBackgroundRect(int i3) {
        float paddingTop = (getPaddingTop() + this.mCurPaddingVertical) - this.mCurBackgroundRadius;
        float height = ((getHeight() - getPaddingBottom()) - this.mCurPaddingVertical) + this.mCurBackgroundRadius;
        Rect rect = this.mBackgroundRect;
        float f3 = i3;
        float f4 = this.mCurBackgroundWidth;
        float f5 = this.mWidthDeformedValue;
        rect.set((int) (f3 - ((f4 / 2.0f) - f5)), (int) ((paddingTop - this.mHeightTopDeformedUpValue) + this.mHeightTopDeformedDownValue), (int) (f3 + ((f4 / 2.0f) - f5)), (int) ((height - this.mHeightBottomDeformedUpValue) + this.mHeightBottomDeformedDownValue));
    }

    private void setDeformationScale(float f3) {
        if (f3 > 1.0f) {
            f3 = ((f3 - 1.0f) * 5.0f) + 1.0f;
        } else if (f3 < 0.0f) {
            f3 *= 5.0f;
        }
        this.mScale = Math.max(-1.0f, Math.min(f3, 2.0f));
    }

    private void setDrawableBounds(Drawable drawable) {
        if (drawable != null) {
            if (!this.mIsSupportDeformation) {
                drawable.setBounds(0, 0, getWidth(), getHeight());
                return;
            }
            drawable.setBounds(0, -(this.mMaxTopMovingDistance + ((int) Math.ceil(this.mMaxTopHeightDeformedValue))), getWidth(), getHeight() + this.mMaxBottomMovingDistance + ((int) Math.ceil(this.mMaxBottomHeightDeformedValue)));
        }
    }

    private void setEnlargeAnimatorValues(ValueAnimator valueAnimator) {
        valueAnimator.setValues(PropertyValuesHolder.ofFloat("progressRadius", this.mCurProgressRadius, this.mProgressRadius * this.mProgressRadiusEnlargeScale), PropertyValuesHolder.ofFloat("backgroundRadius", this.mCurBackgroundRadius, this.mBackgroundRadius * this.mBackgroundRadiusEnlargeScale), PropertyValuesHolder.ofFloat("progressWidth", this.mCurProgressWidth, this.mProgressWidth * this.mProgressEnlargeScale), PropertyValuesHolder.ofFloat("backgroundWidth", this.mCurBackgroundWidth, this.mBackgroundWidth * this.mBackgroundEnlargeScale), PropertyValuesHolder.ofFloat("animatePadding", this.mCurPaddingVertical, this.mPaddingVertical * this.mVerticalPaddingScale));
    }

    private void setFlingScale(float f3) {
        if (!this.mIsSupportDeformation) {
            this.mScale = Math.max(0.0f, Math.min(f3, 1.0f));
            return;
        }
        calculateFlingDeformationValue(f3);
        setDeformationScale(f3);
        if (this.mOnDeformedListener != null) {
            DeformedValueBean deformedValueBean = new DeformedValueBean(this.mHeightBottomDeformedUpValue, this.mHeightTopDeformedUpValue, this.mWidthDeformedValue, this.mHeightBottomDeformedDownValue, this.mHeightTopDeformedDownValue, this.mProgress);
            deformedValueBean.setScale(this.mScale);
            this.mOnDeformedListener.onScaleChanged(deformedValueBean);
        }
    }

    private void setProgressRect(int i3, float f3, float f4) {
        Rect rect = this.mProgressRect;
        float f5 = i3;
        float f6 = this.mCurProgressWidth;
        float f7 = this.mWidthDeformedValue;
        float f8 = f3 - this.mHeightTopDeformedUpValue;
        float f9 = this.mHeightBottomDeformedDownValue;
        rect.set((int) (f5 - ((f6 / 2.0f) - f7)), (int) (f8 + f9), (int) (f5 + ((f6 / 2.0f) - f7)), (int) ((f4 - this.mHeightBottomDeformedUpValue) + f9));
    }

    private void setReleaseAnimatorValues(ValueAnimator valueAnimator) {
        valueAnimator.setValues(PropertyValuesHolder.ofFloat("progressRadius", this.mCurProgressRadius, this.mProgressRadius), PropertyValuesHolder.ofFloat("backgroundRadius", this.mCurBackgroundRadius, this.mBackgroundRadius), PropertyValuesHolder.ofFloat("progressWidth", this.mCurProgressWidth, this.mProgressWidth), PropertyValuesHolder.ofFloat("backgroundWidth", this.mCurBackgroundWidth, this.mBackgroundWidth), PropertyValuesHolder.ofFloat("animatePadding", this.mCurPaddingVertical, this.mPaddingVertical));
    }

    private void setThumbBitmap() {
        if (getThumb() != null) {
            this.mThumbBitmap = drawableToBitmap(getThumb());
        }
    }

    private void setTouchScale(float f3) {
        if (!this.mIsSupportDeformation) {
            this.mScale = Math.max(0.0f, Math.min(f3, 1.0f));
            return;
        }
        this.mScale = Math.max(-1.0f, Math.min(f3, 2.0f));
        calculateTouchDeformationValue();
        if (this.mOnDeformedListener != null) {
            DeformedValueBean deformedValueBean = new DeformedValueBean(this.mHeightBottomDeformedUpValue, this.mHeightTopDeformedUpValue, this.mWidthDeformedValue, this.mHeightBottomDeformedDownValue, this.mHeightTopDeformedDownValue, this.mProgress);
            deformedValueBean.setScale(this.mScale);
            this.mOnDeformedListener.onScaleChanged(deformedValueBean);
        }
    }

    private void startFastMoveAnimation(float f3) {
        Spring fastMoveSpring = getFastMoveSpring();
        if (fastMoveSpring.getCurrentValue() == fastMoveSpring.getEndValue()) {
            int i3 = this.mMax - this.mMin;
            if (f3 <= -95.0f) {
                int i4 = this.mProgress;
                float f4 = i3;
                if (i4 > 0.95f * f4 || i4 < f4 * MIN_FAST_MOVE_PERCENT) {
                    return;
                }
                fastMoveSpring.setEndValue(1.0d);
                return;
            }
            if (f3 < 95.0f) {
                fastMoveSpring.setEndValue(0.0d);
                return;
            }
            int i5 = this.mProgress;
            float f5 = i3;
            if (i5 > 0.95f * f5 || i5 < f5 * MIN_FAST_MOVE_PERCENT) {
                return;
            }
            fastMoveSpring.setEndValue(-1.0d);
        }
    }

    private void stopDeformationFling() {
        if (isDeformationFling()) {
            stopPhysicsMove();
        }
    }

    private boolean topDeformedChange() {
        if (!this.mIsSupportDeformation) {
            return false;
        }
        float heightTopDeformedValue = getHeightTopDeformedValue();
        if (this.mCurTopDeformationValue == heightTopDeformedValue) {
            return false;
        }
        this.mCurTopDeformationValue = heightTopDeformedValue;
        return true;
    }

    private void trackTouchEvent(MotionEvent motionEvent) {
        float y2 = motionEvent.getY();
        float f3 = this.mLastY - y2;
        int i3 = this.mMax - this.mMin;
        float fCalculateDamping = f3 * calculateDamping();
        float seekBarHeightFloat = getSeekBarHeightFloat();
        float f4 = 0.0f;
        if (i3 > 0 && seekBarHeightFloat > 0.0f) {
            f4 = (this.mProgress / i3) + (fCalculateDamping / seekBarHeightFloat);
        }
        setTouchScale(f4);
        int progressLimit = getProgressLimit(Math.round((this.mScale * i3) + getMin()));
        int i4 = this.mProgress;
        int i5 = this.mRealProgress;
        setLocalProgress(progressLimit);
        invalidate();
        if (i4 != this.mProgress) {
            this.mLastY = y2;
            int i6 = this.mRealProgress;
            if (i5 != i6) {
                OnSeekBarChangeListener onSeekBarChangeListener = this.mOnSeekBarChangeListener;
                if (onSeekBarChangeListener != null) {
                    onSeekBarChangeListener.onProgressChanged(this, i6, true);
                }
                performFeedback();
            }
        }
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.computeCurrentVelocity(100);
            startFastMoveAnimation(this.mVelocityTracker.getYVelocity());
        }
    }

    /* JADX WARN: Code duplicated, block: B:4:0x002e  */
    private void trackTouchEventByFinger(MotionEvent motionEvent) {
        float height;
        float f3 = this.mLastY;
        int iRound = Math.round(f3 - ((f3 - motionEvent.getY()) * calculateDamping()));
        int height2 = (getHeight() - getPaddingTop()) - getPaddingBottom();
        if (iRound > getHeight() - getPaddingBottom()) {
            height = 0.0f;
        } else if (iRound < getPaddingTop()) {
            height = 1.0f;
        } else if (height2 > 0) {
            height = ((getHeight() - getPaddingBottom()) - iRound) / height2;
        } else {
            height = 0.0f;
        }
        this.mScale = Math.max(0.0f, Math.min(height, 1.0f));
        int progressLimit = getProgressLimit(Math.round((this.mScale * (getMax() - getMin())) + getMin()));
        int i3 = this.mProgress;
        int i4 = this.mRealProgress;
        setLocalProgress(progressLimit);
        invalidate();
        if (i3 != this.mProgress) {
            this.mLastY = iRound;
            int i5 = this.mRealProgress;
            if (i4 != i5) {
                OnSeekBarChangeListener onSeekBarChangeListener = this.mOnSeekBarChangeListener;
                if (onSeekBarChangeListener != null) {
                    onSeekBarChangeListener.onProgressChanged(this, i5, true);
                }
                performFeedback();
            }
        }
    }

    private void updateBehavior() {
        FlingBehavior flingBehavior;
        float normalSeekBarHeightFloat = getNormalSeekBarHeightFloat();
        String str = "updateBehavior * normalSeekBarHeight = " + normalSeekBarHeightFloat + ",mPaddingVertical = " + this.mPaddingVertical + ",mIsPhysicsEnable = " + this.mIsPhysicsEnable + ",mPhysicalAnimator = " + this.mPhysicalAnimator + ",mFlingBehavior = " + this.mFlingBehavior + ",Thread_name = " + Thread.currentThread().getName() + ",hashCode = " + hashCode() + ",time = " + System.currentTimeMillis();
        COUILog.i(TAG, str);
        updateParameter(str);
        if (!this.mIsPhysicsEnable || this.mPhysicalAnimator == null || (flingBehavior = this.mFlingBehavior) == null) {
            return;
        }
        this.mHistorySeekBarHeight = normalSeekBarHeightFloat;
        flingBehavior.setActiveFrame(0.0f, normalSeekBarHeightFloat);
    }

    private void updateParameter(String str) {
        if (this.mHistoryLogEnable) {
            this.mHistoryLogQueue.offer(str);
            while (this.mHistoryLogQueue.size() > 20) {
                this.mHistoryLogQueue.poll();
            }
        }
    }

    private void updatePixPerProgress() {
        float seekBarHeightFloat = getSeekBarHeightFloat();
        int i3 = this.mMax - this.mMin;
        this.mPixPerProgress = i3 > 0 ? seekBarHeightFloat / i3 : 0.0f;
    }

    private void updateScale() {
        int i3 = this.mMax;
        int i4 = this.mMin;
        int i5 = i3 - i4;
        this.mScale = i5 > 0 ? (this.mProgress - i4) / i5 : 0.0f;
    }

    public void animForClick(float f3) {
        float seekBarHeightFloat = getSeekBarHeightFloat();
        float f4 = this.mCurProgressRadius;
        float f5 = seekBarHeightFloat + (2.0f * f4);
        startTransitionAnim(getProgressLimit(Math.round(((f5 > 0.0f ? (((getHeight() - getPaddingBottom()) - (this.mCurPaddingVertical - f4)) - f3) / f5 : 0.0f) * (getMax() - getMin())) + getMin())), true);
    }

    @Override // android.view.View
    public boolean dispatchHoverEvent(MotionEvent motionEvent) {
        return super.dispatchHoverEvent(motionEvent);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        float f3;
        float f4;
        float f5;
        float f6;
        int seekBarCenterX = getSeekBarCenterX();
        float seekBarHeightFloat = getSeekBarHeightFloat();
        if (this.mShowThumb) {
            float f7 = this.mCurPaddingVertical;
            float f8 = this.mThumbOutWidth;
            float f9 = this.mThumbOutRadius;
            f3 = ((f8 / 2.0f) - f9) + f7;
            float f10 = seekBarHeightFloat - (f8 - (f9 * 2.0f));
            float f11 = this.mCurProgressRadius;
            float f12 = f7 - f11;
            f4 = seekBarHeightFloat + (f11 * 2.0f);
            f5 = f10;
            f6 = f12;
        } else {
            float f13 = this.mCurPaddingVertical;
            float f14 = this.mCurProgressRadius;
            f5 = seekBarHeightFloat + (f14 * 2.0f);
            f6 = f13 - f14;
            f3 = f6;
            f4 = f5;
        }
        Rect rect = this.mClipProgressRect;
        float f15 = seekBarCenterX;
        float f16 = this.mCurProgressWidth;
        float f17 = this.mWidthDeformedValue;
        rect.left = (int) ((f15 - (f16 / 2.0f)) + f17);
        rect.right = (int) ((f15 + (f16 / 2.0f)) - f17);
        float paddingTop = getPaddingTop() + f3 + f5;
        float realScale = paddingTop - (getRealScale(this.mScale) * f5);
        this.mThumbPosition = realScale;
        float paddingTop2 = getPaddingTop() + f6;
        float f18 = this.mHeightTopDeformedDownValue;
        float f19 = this.mHeightTopDeformedUpValue;
        float f20 = (paddingTop2 + f18) - f19;
        Rect rect2 = this.mClipProgressRect;
        rect2.top = (int) f20;
        rect2.bottom = (int) (((((f20 + f4) + f19) - this.mHeightBottomDeformedUpValue) + this.mHeightBottomDeformedDownValue) - f18);
        setBackgroundRect(seekBarCenterX);
        setProgressRect(seekBarCenterX, realScale, paddingTop);
        super.draw(canvas);
    }

    public void drawActiveTrack(Canvas canvas) {
        if (this.mShowProgress) {
            drawProgress(canvas);
        }
        if (this.mShowThumb) {
            drawThumb(canvas);
        }
        if (this.mShowText) {
            drawText(canvas);
        }
    }

    public void drawInactiveTrack(Canvas canvas) {
        boolean z2 = this.mIsSupportSmoothRoundCorner && this.mBackgroundRoundCornerWeight != 0.0f;
        if (this.mInactiveTrackDrawable != null) {
            drawBackgroundByClip(canvas, z2);
        } else {
            drawBackgroundByPaint(canvas, z2);
        }
    }

    public void endCustomDeformation() {
        cancelAnim(this.mButtonDeformationAnimator);
        setTouchScale(Math.max(0.0f, Math.min(this.mScale, 1.0f)));
        setLocalProgress(getProgressLimit(Math.round((this.mMax - this.mMin) * this.mScale) + this.mMin));
        invalidate();
    }

    public void endEnlargeAnim() {
        ValueAnimator valueAnimator = this.mEnlargeAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mEnlargeAnimator.end();
        }
        ValueAnimator valueAnimator2 = this.mReleaseAnimator;
        if (valueAnimator2 == null || !valueAnimator2.isRunning()) {
            return;
        }
        this.mReleaseAnimator.end();
    }

    public void ensureLabelsRemoved() {
        if (ViewUtil.getContentViewOverlay(this) != null) {
            ViewUtil.getContentViewOverlay(this).remove(this.mTextDrawable);
        }
    }

    public Paint getBackgroundPaint() {
        return this.mBackgroundPaint;
    }

    public int getColor(View view, ColorStateList colorStateList, int i3) {
        return colorStateList == null ? i3 : colorStateList.getColorForState(view.getDrawableState(), i3);
    }

    public boolean getHasEnlarge() {
        return this.mHasEnlarge;
    }

    public int getLabelHeight() {
        return this.mTextDrawable.getIntrinsicHeight();
    }

    @Override // android.widget.ProgressBar
    public int getMax() {
        return this.mMax;
    }

    @Override // android.widget.ProgressBar
    public int getMin() {
        return this.mMin;
    }

    public float getMoveDamping() {
        return this.mDamping;
    }

    public int getMoveType() {
        return this.mMoveType;
    }

    @Override // android.widget.ProgressBar
    public int getProgress() {
        return this.mRealProgress;
    }

    public Paint getProgressPaint() {
        return this.mProgressPaint;
    }

    public int getSeekBarCenterX() {
        return getPaddingStart() + (((getWidth() - getPaddingStart()) - getPaddingEnd()) >> 1);
    }

    public int getSeekBarHeight() {
        return (int) getSeekBarHeightFloat();
    }

    public float getSeekBarHeightFloat() {
        return ((getHeight() - getPaddingTop()) - getPaddingBottom()) - (this.mCurPaddingVertical * 2.0f);
    }

    public Paint getThumbPaint() {
        return this.mThumbPaint;
    }

    public void handleMotionEventDown(MotionEvent motionEvent) {
        this.mTouchDownY = motionEvent.getY();
        this.mLastY = motionEvent.getY();
    }

    public void handleMotionEventMove(MotionEvent motionEvent) {
        if (this.mIsDragging && this.mStartDragging) {
            cancelAnim(this.mButtonDeformationAnimator);
            int i3 = this.mMoveType;
            if (i3 != 0) {
                if (i3 == 1) {
                    trackTouchEventByFinger(motionEvent);
                    return;
                } else if (i3 != 2) {
                    return;
                }
            }
            trackTouchEvent(motionEvent);
            return;
        }
        float y2 = motionEvent.getY();
        if (Math.abs(y2 - this.mTouchDownY) > this.mTouchSlop) {
            if (!isMoveFollowHand()) {
                cancelAnim(this.mClickAnimator);
            }
            stopDeformationFling();
            startDrag();
            touchAnim();
            this.mLastY = y2;
            if (isMoveFollowHand()) {
                invalidateProgress(motionEvent);
            }
        }
    }

    public void handleMotionEventUp(MotionEvent motionEvent) {
        OnSeekBarChangeListener onSeekBarChangeListener;
        getFastMoveSpring().setEndValue(0.0d);
        if (!this.mIsDragging) {
            if (isEnabled() && touchInSeekBar(motionEvent, this) && isMoveFollowHand()) {
                animForClick(motionEvent.getY());
                return;
            }
            return;
        }
        this.mIsDragging = false;
        this.mStartDragging = false;
        COUILog.i(TAG, "handleMotionEventUp mFlingVelocity = " + this.mFlingVelocity);
        if (!this.mIsPhysicsEnable || Math.abs(this.mFlingVelocity) < 100.0f) {
            float f3 = this.mScale;
            if (f3 >= 0.0f && f3 <= 1.0f && (onSeekBarChangeListener = this.mOnSeekBarChangeListener) != null) {
                onSeekBarChangeListener.onStopTrackingTouch(this);
            }
            flingBehaviorAfterDeformationDrag();
        } else {
            flingBehaviorAfterEndDrag(this.mFlingVelocity);
        }
        setPressed(false);
        releaseAnim();
    }

    @Override // android.widget.ProgressBar, android.view.View, android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        if (this.mInactiveTrackDrawable != null) {
            Rect dirtyBounds = drawable.getDirtyBounds();
            invalidate(dirtyBounds.left, dirtyBounds.top, dirtyBounds.right, dirtyBounds.bottom);
        }
    }

    public boolean isLayoutRtl() {
        return getLayoutDirection() == 1;
    }

    @Override // com.oplus.physicsengine.engine.AnimationListener
    public void onAnimationCancel(BaseBehavior baseBehavior) {
        onStopTrackingTouch(true);
    }

    @Override // com.oplus.physicsengine.engine.AnimationListener
    public void onAnimationEnd(BaseBehavior baseBehavior) {
        logOnAnimationEnd(baseBehavior);
        OnSeekBarChangeListener onSeekBarChangeListener = this.mOnSeekBarChangeListener;
        if (onSeekBarChangeListener != null) {
            onSeekBarChangeListener.onStopTrackingTouch(this);
        }
    }

    @Override // com.oplus.physicsengine.engine.AnimationUpdateListener
    public void onAnimationUpdate(BaseBehavior baseBehavior) {
        int i3;
        Object animatedValue = baseBehavior.getAnimatedValue();
        if (animatedValue == null) {
            return;
        }
        float fFloatValue = ((Float) animatedValue).floatValue();
        float normalSeekBarHeightFloat = getNormalSeekBarHeightFloat();
        setFlingScale(normalSeekBarHeightFloat > 0.0f ? fFloatValue / normalSeekBarHeightFloat : 0.0f);
        float f3 = this.mProgress;
        int i4 = this.mRealProgress;
        setLocalProgress(getProgressLimit(Math.round((this.mMax - this.mMin) * this.mScale) + this.mMin));
        invalidate();
        if (f3 != this.mProgress) {
            this.mLastY = (getHeight() - getPaddingBottom()) - fFloatValue;
            OnSeekBarChangeListener onSeekBarChangeListener = this.mOnSeekBarChangeListener;
            if (onSeekBarChangeListener == null || i4 == (i3 = this.mRealProgress)) {
                return;
            }
            onSeekBarChangeListener.onProgressChanged(this, i3, true);
        }
    }

    @Override // android.widget.ProgressBar, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        VibrateUtils.registerHapticObserver(getContext());
    }

    @Override // android.widget.ProgressBar, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopPhysicsMove();
        VibrateUtils.unRegisterHapticObserver();
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    public void onDraw(Canvas canvas) {
        drawInactiveTrack(canvas);
        drawActiveTrack(canvas);
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    public void onMeasure(int i3, int i4) {
        int mode = View.MeasureSpec.getMode(i3);
        int size = View.MeasureSpec.getSize(i3);
        int size2 = View.MeasureSpec.getSize(i4);
        int paddingStart = this.mSeekbarMinWidth + getPaddingStart() + getPaddingEnd();
        if (1073741824 != mode || size < paddingStart) {
            size = paddingStart;
        }
        int i5 = this.mMaxHeight;
        if (i5 > 0 && size2 > i5) {
            size2 = i5;
        }
        setMeasuredDimension(size, size2);
    }

    @Override // android.widget.ProgressBar, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            SavedState savedState = (SavedState) parcelable;
            super.onRestoreInstanceState(savedState.getSuperState());
            setProgress(savedState.mSaveProgress);
        }
    }

    @Override // android.widget.ProgressBar, android.view.View
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mSaveProgress = this.mProgress;
        return savedState;
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    public void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        this.mStartDragging = false;
        setDrawableBounds(this.mInactiveTrackDrawable);
        stopPhysicsMove();
        updateBehavior();
        updatePixPerProgress();
    }

    public void onStartTrackingTouch() {
        onStartTrackingTouch(true);
    }

    public void onStopTrackingTouch() {
        onStopTrackingTouch(true);
    }

    /* JADX WARN: Code duplicated, block: B:20:0x0039  */
    /* JADX WARN: Code duplicated, block: B:22:0x003d  */
    /* JADX WARN: Instruction removed from duplicated block: B:22:0x003d, please report this as an issue */
    @Override // android.widget.AbsSeekBar, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        VelocityTracker velocityTracker;
        if (!isEnabled()) {
            if (motionEvent.getAction() != 1 && motionEvent.getAction() != 3) {
                return false;
            }
            handleMotionEventUp(motionEvent);
            return true;
        }
        int action = motionEvent.getAction();
        if (action == 0) {
            if (isMoveFollowHand()) {
                cancelAnim(this.mClickAnimator);
            }
            if (!isDeformationFling()) {
                stopPhysicsMove();
            }
            checkPhysicsAnimator();
            initOrResetVelocityTracker();
            this.mVelocityTracker.addMovement(motionEvent);
            this.mIsDragging = false;
            this.mStartDragging = false;
            if (this.mHistorySeekBarHeight != getNormalSeekBarHeightFloat()) {
                this.mHistoryLogEnable = false;
            }
            handleMotionEventDown(motionEvent);
        } else if (action == 1) {
            velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.computeCurrentVelocity(1000, 8000.0f);
                this.mFlingVelocity = this.mVelocityTracker.getYVelocity();
                COUILog.i(TAG, "onTouchEvent ACTION_UP mFlingVelocity = " + this.mFlingVelocity);
            }
            recycleVelocityTracker();
            clearVibratorPendingTasks();
            handleMotionEventUp(motionEvent);
        } else if (action == 2) {
            clearDeformationValue();
            initVelocityTrackerIfNotExists();
            this.mVelocityTracker.addMovement(motionEvent);
            handleMotionEventMove(motionEvent);
        } else if (action == 3) {
            velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.computeCurrentVelocity(1000, 8000.0f);
                this.mFlingVelocity = this.mVelocityTracker.getYVelocity();
                COUILog.i(TAG, "onTouchEvent ACTION_UP mFlingVelocity = " + this.mFlingVelocity);
            }
            recycleVelocityTracker();
            clearVibratorPendingTasks();
            handleMotionEventUp(motionEvent);
        }
        return true;
    }

    public boolean performAdaptiveFeedback() {
        if (this.mLinearMotorVibrator == null) {
            LinearmotorVibrator linearMotorVibrator = VibrateUtils.getLinearMotorVibrator(getContext());
            this.mLinearMotorVibrator = linearMotorVibrator;
            this.mHasMotorVibrator = linearMotorVibrator != null;
        }
        if (this.mLinearMotorVibrator == null) {
            return false;
        }
        if (this.mRealProgress == getMax() || this.mRealProgress == getMin()) {
            LinearmotorVibrator linearmotorVibrator = (LinearmotorVibrator) this.mLinearMotorVibrator;
            int i3 = this.mRealProgress;
            int i4 = this.mMin;
            VibrateUtils.setLinearMotorVibratorStrength(linearmotorVibrator, 154, i3 - i4, this.mMax - i4, 800, 1200);
        } else {
            if (this.mVibratorExecutor == null) {
                this.mVibratorExecutor = Executors.newSingleThreadExecutor();
            }
            this.mVibratorExecutor.execute(new AnonymousClass6());
        }
        return true;
    }

    public void performFeedback() {
        if (this.mEnableVibrator) {
            if (this.mHasMotorVibrator && this.mEnableAdaptiveVibrator && performAdaptiveFeedback()) {
                return;
            }
            if (this.mRealProgress == getMax() || this.mRealProgress == getMin()) {
                performHapticFeedback(306, 0);
                return;
            }
            if (this.mVibratorExecutor == null) {
                this.mVibratorExecutor = Executors.newSingleThreadExecutor();
            }
            this.mVibratorExecutor.execute(new AnonymousClass5());
        }
    }

    public void preCalcClipPath() {
        int seekBarCenterX = getSeekBarCenterX();
        float paddingTop = (getPaddingTop() + this.mCurPaddingVertical) - this.mCurBackgroundRadius;
        float height = ((getHeight() - getPaddingBottom()) - this.mCurPaddingVertical) + this.mCurBackgroundRadius;
        Rect rect = this.mBackgroundRect;
        float f3 = seekBarCenterX;
        float f4 = this.mCurBackgroundWidth;
        float f5 = this.mWidthDeformedValue;
        rect.set((int) (f3 - ((f4 / 2.0f) - f5)), (int) ((paddingTop - this.mHeightTopDeformedUpValue) + this.mHeightTopDeformedDownValue), (int) (f3 + ((f4 / 2.0f) - f5)), (int) ((height - this.mHeightBottomDeformedUpValue) + this.mHeightBottomDeformedDownValue));
    }

    public void refresh() {
        String resourceTypeName = getResources().getResourceTypeName(this.mRefreshStyle);
        TypedArray typedArrayObtainStyledAttributes = null;
        if (TextUtils.equals(resourceTypeName, "attr")) {
            typedArrayObtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(null, R.styleable.COUIVerticalSeekBar, this.mRefreshStyle, 0);
        } else if (TextUtils.equals(resourceTypeName, BaseConfig.KEY_STYLE)) {
            typedArrayObtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(null, R.styleable.COUIVerticalSeekBar, 0, this.mRefreshStyle);
        }
        if (typedArrayObtainStyledAttributes != null) {
            this.mProgressColor = getColor(this, typedArrayObtainStyledAttributes.getColorStateList(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarProgressColor), COUIContextUtil.getColor(getContext(), R.color.coui_seekbar_progress_color_normal));
            this.mBackgroundColor = getColor(this, typedArrayObtainStyledAttributes.getColorStateList(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarBackgroundColor), COUIContextUtil.getColor(getContext(), R.color.coui_seekbar_background_color_normal));
            this.mTextColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarTextColor, getResources().getColor(R.color.coui_seekbar_text_color));
            invalidate();
            typedArrayObtainStyledAttributes.recycle();
        }
    }

    public void releaseAnim() {
        if (this.mEnableCustomEnlarge) {
            return;
        }
        cancelAnim(this.mTouchEnlargeAnimator);
        ValueAnimator valueAnimator = this.mTouchReleaseAnimator;
        if (valueAnimator == null) {
            this.mTouchReleaseAnimator = getReleaseAnimator(183L, PROGRESS_SCALE_INTERPOLATOR);
        } else {
            cancelAnim(valueAnimator);
        }
        setReleaseAnimatorValues(this.mTouchReleaseAnimator);
        this.mTouchReleaseAnimator.start();
    }

    public void setBackgroundEnlargeScale(float f3) {
        this.mBackgroundEnlargeScale = f3;
        ensureSize();
        invalidate();
    }

    public void setBackgroundRadius(float f3) {
        this.mBackgroundRadius = f3;
        ensureSize();
        invalidate();
    }

    public void setBackgroundRoundCornerWeight(float f3) {
        this.mBackgroundRoundCornerWeight = f3;
        invalidate();
    }

    public void setBackgroundWidth(float f3) {
        this.mBackgroundWidth = f3;
        ensureSize();
        invalidate();
    }

    public void setCustomProgressAnimDuration(float f3) {
        if (f3 <= 0.0f) {
            return;
        }
        this.mCustomProgressAnimDuration = f3;
    }

    public void setCustomProgressAnimInterpolator(Interpolator interpolator) {
        this.mCustomProgressAnimInterpolator = interpolator;
    }

    public void setDeformedListener(OnDeformedListener onDeformedListener) {
        this.mOnDeformedListener = onDeformedListener;
    }

    public void setDeformedParams(DeformedValueBean deformedValueBean) {
        this.mScale = deformedValueBean.getScale();
        this.mProgress = deformedValueBean.getProgress();
        this.mHeightBottomDeformedUpValue = deformedValueBean.getHeightBottomDeformedUpValue();
        this.mHeightTopDeformedUpValue = deformedValueBean.getHeightTopDeformedUpValue();
        this.mWidthDeformedValue = deformedValueBean.getWidthDeformedValue();
        this.mHeightBottomDeformedDownValue = deformedValueBean.getHeightBottomDeformedDownValue();
        this.mHeightTopDeformedDownValue = deformedValueBean.getHeightTopDeformedDownValue();
        invalidate();
    }

    public void setEnableAdaptiveVibrator(boolean z2) {
        this.mEnableAdaptiveVibrator = z2;
    }

    public void setEnableCustomEnlarge(boolean z2) {
        this.mEnableCustomEnlarge = z2;
    }

    public void setEnableVibrator(boolean z2) {
        this.mEnableVibrator = z2;
    }

    @Override // android.view.View
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        ColorStateList colorStateList = this.mProgressColorStateList;
        Context context = getContext();
        int i3 = R.color.coui_seekbar_progress_color_normal;
        this.mProgressColor = getColor(this, colorStateList, COUIContextUtil.getColor(context, i3));
        this.mBackgroundColor = getColor(this, this.mBackgroundColorStateList, COUIContextUtil.getColor(getContext(), R.color.coui_seekbar_background_color_normal));
        this.mThumbColor = getColor(this, this.mThumbColorStateList, COUIContextUtil.getColor(getContext(), i3));
    }

    public void setFlingLinearDamping(float f3) {
        FlingBehavior flingBehavior;
        if (this.mIsPhysicsEnable) {
            this.mFlingLinearDamping = f3;
            if (this.mPhysicalAnimator == null || (flingBehavior = this.mFlingBehavior) == null) {
                return;
            }
            flingBehavior.setLinearDamping(f3);
        }
    }

    public void setFlingProperty(float f3, float f4) {
        FlingBehavior flingBehavior;
        if (this.mIsPhysicsEnable) {
            this.mFlingFrequency = f3;
            this.mFlingDampingRatio = f4;
            if (this.mPhysicalAnimator == null || (flingBehavior = this.mFlingBehavior) == null) {
                return;
            }
            flingBehavior.setSpringProperty(f3, f4);
        }
    }

    public void setInactiveTrackDrawable(Drawable drawable) {
        Drawable drawable2 = this.mInactiveTrackDrawable;
        if (drawable2 != null) {
            drawable2.setCallback(null);
        }
        this.mInactiveTrackDrawable = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
            setDrawableBounds(this.mInactiveTrackDrawable);
        }
        invalidate();
    }

    public void setIncrement(int i3) {
        this.mIncrement = Math.abs(i3);
    }

    public void setLocalMax(int i3) {
        this.mMax = i3;
        updatePixPerProgress();
        updateScale();
        super.setMax(i3);
    }

    public void setLocalMin(int i3) {
        this.mMin = i3;
        updatePixPerProgress();
        updateScale();
        super.setMin(i3);
    }

    public void setLocalProgress(int i3) {
        this.mProgress = i3;
        this.mRealProgress = getRealProgress(i3);
        super.setProgress(i3);
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar
    public void setMax(int i3) {
        if (i3 < getMin()) {
            int min = getMin();
            Log.e(TAG, "setMax : the input params is lower than min. (inputMax:" + i3 + ",mMin:" + this.mMin + ")");
            i3 = min;
        }
        if (i3 != this.mMax) {
            setLocalMax(i3);
            if (this.mProgress > i3) {
                setProgress(i3);
            }
        }
        invalidate();
    }

    public void setMaxHeightDeformed(float f3) {
        setMaxHeightDeformed(f3, f3);
    }

    public void setMaxMovingDistance(int i3) {
        setMaxMovingDistance(i3, i3);
    }

    public void setMaxWidthDeformed(float f3) {
        this.mMaxWidthDeformedValue = f3;
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar
    public void setMin(int i3) {
        int max = i3 < 0 ? 0 : i3;
        if (i3 > getMax()) {
            max = getMax();
            Log.e(TAG, "setMin : the input params is greater than max. (inputMin:" + i3 + ",mMax:" + this.mMax + ")");
        }
        if (max != this.mMin) {
            setLocalMin(max);
            if (this.mProgress < max) {
                setProgress(max);
            }
        }
        invalidate();
    }

    public void setMoveDamping(float f3) {
        this.mDamping = f3;
    }

    public void setMoveType(int i3) {
        this.mMoveType = i3;
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener onSeekBarChangeListener) {
        this.mOnSeekBarChangeListener = onSeekBarChangeListener;
    }

    public void setPaddingVertical(float f3) {
        String str = "setPaddingVertical * paddingVertical = " + f3 + ",hashCode = " + hashCode() + ",time = " + System.currentTimeMillis();
        COUILog.i(TAG, str);
        updateParameter(str);
        if (f3 == 0.0f) {
            this.mPaddingVertical = this.mDefaultPaddingVertical;
        } else {
            this.mPaddingVertical = f3;
        }
        ensureSize();
        invalidate();
    }

    public void setPhysicalEnabled(boolean z2) {
        if (z2 == this.mIsPhysicsEnable) {
            return;
        }
        COUILog.i(TAG, "setPhysicalEnabled * isEnable = " + z2 + ",hashCode = " + hashCode() + ",time = " + System.currentTimeMillis());
        if (z2) {
            this.mIsPhysicsEnable = true;
            updateBehavior();
        } else {
            stopPhysicsMove();
            this.mIsPhysicsEnable = false;
        }
    }

    @Override // android.widget.ProgressBar
    public void setProgress(int i3) {
        setProgress(i3, false);
    }

    public void setProgressColor(ColorStateList colorStateList) {
        if (colorStateList != null) {
            this.mProgressColorStateList = colorStateList;
            this.mProgressColor = getColor(this, colorStateList, COUIContextUtil.getColor(getContext(), R.color.coui_seekbar_progress_color_normal));
            invalidate();
        }
    }

    public void setProgressContentDescription(String str) {
        this.mProgressContentDescription = str;
    }

    public void setProgressEnlargeScale(float f3) {
        this.mProgressEnlargeScale = f3;
        ensureSize();
        invalidate();
    }

    public void setProgressFull() {
        this.mIsProgressFull = true;
        ensureSize();
    }

    public void setProgressRadius(float f3) {
        this.mProgressRadius = f3;
        ensureSize();
        invalidate();
    }

    public void setProgressRoundCornerWeight(float f3) {
        this.mProgressRoundCornerWeight = f3;
        ensureSize();
        invalidate();
    }

    public void setProgressWidth(float f3) {
        this.mProgressWidth = f3;
        ensureSize();
        invalidate();
    }

    public void setSeekBarBackgroundColor(ColorStateList colorStateList) {
        if (colorStateList != null) {
            this.mBackgroundColorStateList = colorStateList;
            this.mBackgroundColor = getColor(this, colorStateList, COUIContextUtil.getColor(getContext(), R.color.coui_seekbar_background_color_normal));
            invalidate();
        }
    }

    public void setSupportDeformation(boolean z2) {
        this.mIsSupportDeformation = z2;
    }

    public void setText(String str) {
        this.mTextContent = str;
        invalidate();
    }

    public void setTextColor(int i3) {
        if (this.mTextColor != i3) {
            this.mTextColor = i3;
            refreshTextColor();
            invalidate();
        }
    }

    public void setTextMarginTop(float f3) {
        if (this.mTextMarginTop != f3) {
            this.mTextMarginTop = f3;
            invalidate();
        }
    }

    public void setTextShadowEnabled(boolean z2) {
        if (this.mShowTextShadow != z2) {
            this.mShowTextShadow = z2;
            refreshTextColor();
            invalidate();
        }
    }

    public void setTextTypeface(Typeface typeface) {
        if (typeface == null || typeface.equals(this.mTextPaint.getTypeface())) {
            return;
        }
        this.mTextPaint.setTypeface(typeface);
        invalidate();
    }

    @Override // android.widget.AbsSeekBar
    public void setThumb(Drawable drawable) {
        super.setThumb(drawable);
        setThumbBitmap();
    }

    public void setThumbColor(ColorStateList colorStateList) {
        if (colorStateList != null) {
            this.mThumbColorStateList = colorStateList;
            this.mThumbColor = getColor(this, colorStateList, COUIContextUtil.getColor(getContext(), R.color.coui_seekbar_progress_color_normal));
            invalidate();
        }
    }

    public void showText(boolean z2) {
        this.mShowText = z2;
        invalidate();
    }

    public void startCustomDeformation(float f3) {
        if (this.mIsSupportDeformation) {
            if (this.mInterpolator == null) {
                this.mInterpolator = new COUIMoveEaseInterpolator();
            }
            startCustomDeformation(f3, this.mInterpolator, BUTTON_DEFORMATION_ANIM_DURATION);
        }
    }

    public void startDrag() {
        setPressed(true);
        onStartTrackingTouch(true);
        attemptClaimDrag();
    }

    public void startEnlargeAnim(int i3, long j3, Interpolator interpolator) {
        cancelAnim(this.mReleaseAnimator);
        cancelAnim(this.mEnlargeAnimator);
        if (i3 == 1) {
            executeEnlargeAnim(j3, interpolator);
        } else if (i3 == 0) {
            executeReleaseAnim(j3, interpolator);
        }
    }

    /* JADX WARN: Code duplicated, block: B:10:0x0021  */
    /* JADX WARN: Code duplicated, block: B:12:0x0028  */
    /* JADX WARN: Code duplicated, block: B:13:0x0032  */
    /* JADX WARN: Code duplicated, block: B:16:0x003e  */
    public void startTransitionAnim(int i3, boolean z2) {
        int i4;
        float fAbs;
        long j3;
        int i5 = this.mProgress;
        ValueAnimator valueAnimator = this.mClickAnimator;
        if (valueAnimator == null) {
            this.mClickAnimator = createClickAnimator(z2);
        } else {
            cancelAnim(valueAnimator);
        }
        if (z2) {
            i4 = this.mMax - this.mMin;
            if (i4 > 0) {
                fAbs = Math.abs(i3 - i5) / i4;
            } else {
                fAbs = 0.0f;
            }
            j3 = (long) (fAbs * 483.0f);
            if (j3 < 150) {
                j3 = 150;
            }
            this.mClickAnimator.setDuration(j3);
        } else {
            float f3 = this.mCustomProgressAnimDuration;
            if (f3 != -1.0f) {
                this.mClickAnimator.setDuration((long) f3);
            } else {
                i4 = this.mMax - this.mMin;
                if (i4 > 0) {
                    fAbs = Math.abs(i3 - i5) / i4;
                } else {
                    fAbs = 0.0f;
                }
                j3 = (long) (fAbs * 483.0f);
                if (j3 < 150) {
                    j3 = 150;
                }
                this.mClickAnimator.setDuration(j3);
            }
        }
        float f4 = i5;
        float f5 = this.mPixPerProgress;
        this.mClickAnimator.setValues(PropertyValuesHolder.ofFloat("progressLength", f4 * f5, i3 * f5));
        this.mClickAnimator.start();
    }

    public void stopPhysicsMove() {
        FlingBehavior flingBehavior;
        if (!this.mIsPhysicsEnable || this.mPhysicalAnimator == null || (flingBehavior = this.mFlingBehavior) == null) {
            return;
        }
        flingBehavior.stop();
    }

    public float subtract(float f3, float f4) {
        return new BigDecimal(Float.toString(f3)).subtract(new BigDecimal(Float.toString(f4))).floatValue();
    }

    public void touchAnim() {
        if (this.mEnableCustomEnlarge) {
            return;
        }
        cancelAnim(this.mTouchEnlargeAnimator);
        this.mTouchEnlargeAnimator.start();
    }

    public boolean touchInSeekBar(MotionEvent motionEvent, View view) {
        float x2 = motionEvent.getX();
        float y2 = motionEvent.getY();
        return x2 >= 0.0f && x2 <= ((float) view.getWidth()) && y2 >= ((float) view.getPaddingTop()) && y2 <= ((float) (view.getHeight() - view.getPaddingBottom()));
    }

    public COUIVerticalSeekBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiVerticalSeekBarStyle);
    }

    public void onStartTrackingTouch(boolean z2) {
        if (z2) {
            this.mIsDragging = true;
            this.mStartDragging = true;
            OnSeekBarChangeListener onSeekBarChangeListener = this.mOnSeekBarChangeListener;
            if (onSeekBarChangeListener != null) {
                onSeekBarChangeListener.onStartTrackingTouch(this);
            }
        }
    }

    public void onStopTrackingTouch(boolean z2) {
        if (z2) {
            this.mIsDragging = false;
            this.mStartDragging = false;
            OnSeekBarChangeListener onSeekBarChangeListener = this.mOnSeekBarChangeListener;
            if (onSeekBarChangeListener != null) {
                onSeekBarChangeListener.onStopTrackingTouch(this);
            }
        }
    }

    public void setMaxHeightDeformed(float f3, float f4) {
        this.mMaxTopHeightDeformedValue = f3;
        this.mMaxBottomHeightDeformedValue = f4;
    }

    public void setMaxMovingDistance(int i3, int i4) {
        this.mMaxTopMovingDistance = i3;
        this.mMaxBottomMovingDistance = i4;
    }

    @Override // android.widget.ProgressBar
    public void setProgress(int i3, boolean z2) {
        setProgress(i3, z2, false);
    }

    public COUIVerticalSeekBar(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, COUIContextUtil.isCOUIDarkTheme(context) ? R.style.COUIVerticalSeekBar_Dark : R.style.COUIVerticalSeekBar);
    }

    private void addOs16SmoothRoundRectToPath(OplusPathAdapter oplusPathAdapter, RectF rectF, float f3, float f4, float f5) {
        if (COUIVersionUtil.getOSVersionCode() > 37) {
            oplusPathAdapter.addSmoothRoundRect(rectF, f3, f3, f5, Path.Direction.CCW);
        } else {
            oplusPathAdapter.addSmoothRoundRect(rectF, f3, f4, Path.Direction.CCW);
        }
    }

    public void setProgress(int i3, boolean z2, boolean z3) {
        this.mOldProgress = this.mProgress;
        int iMax = Math.max(this.mMin, Math.min(i3, this.mMax));
        this.mLastProgress = iMax;
        if (this.mOldProgress != iMax) {
            if (z2) {
                startTransitionAnim(iMax, z3);
            } else {
                setLocalProgress(iMax);
                this.mOldProgress = iMax;
                updateScale();
                OnSeekBarChangeListener onSeekBarChangeListener = this.mOnSeekBarChangeListener;
                if (onSeekBarChangeListener != null) {
                    onSeekBarChangeListener.onProgressChanged(this, this.mRealProgress, z3);
                }
                invalidate();
            }
            resetDeformationValue();
        }
    }

    public COUIVerticalSeekBar(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mSeekBarRoleDescription = getResources().getString(R.string.coui_seek_bar_role_description);
        this.mMax = 100;
        this.mMin = 0;
        this.mOldProgress = 0;
        this.mLastProgress = 0;
        this.mProgress = 0;
        this.mTouchSlop = 0;
        this.mCustomProgressAnimDuration = -1.0f;
        this.mScale = 0.0f;
        this.mShowProgress = false;
        this.mShowThumb = false;
        this.mEnableAdaptiveVibrator = true;
        this.mEnableVibrator = true;
        this.mHasMotorVibrator = true;
        this.mIsDragging = false;
        this.mIsSupportSmoothRoundCorner = false;
        this.mSmoothStyleType = RoundCornerUtil.getSmoothStyleType();
        this.mBackgroundColorStateList = null;
        this.mProgressColorStateList = null;
        this.mThumbColorStateList = null;
        this.mLinearMotorVibrator = null;
        this.mClipProgressPath = new Path();
        this.mClipProgressRect = new Rect();
        this.mProgressRect = new Rect();
        this.mTempRect = new Rect();
        this.mBackgroundRect = new Rect();
        this.mHasEnlarge = false;
        this.mIsProgressFull = false;
        this.mShowTextShadow = true;
        this.mCustomProgressAnimInterpolator = null;
        this.mIncrement = 1;
        this.mStartDragging = false;
        this.mMoveType = 1;
        this.mFastMoveSpringConfig = SpringConfig.fromOrigamiTensionAndFriction(500.0d, 30.0d);
        this.mDamping = 0.0f;
        this.mThumbPosition = 0.0f;
        this.mIsPhysicsEnable = false;
        this.mFlingVelocity = 0.0f;
        this.mFlingFrequency = 2.8f;
        this.mFlingDampingRatio = 1.0f;
        this.mFlingLinearDamping = 15.0f;
        this.mMaxTopMovingDistance = 30;
        this.mMaxBottomMovingDistance = 30;
        this.mMaxTopHeightDeformedValue = 28.5f;
        this.mMaxBottomHeightDeformedValue = 28.5f;
        this.mMaxWidthDeformedValue = 4.7f;
        this.mBackgroundPath = new Path();
        this.mTmpRectF = new RectF();
        this.mThumbSmoothPath = new Path();
        this.mProgressInnerSmoothPath = new Path();
        this.mHistoryLogQueue = new LinkedList();
        this.mHistoryLogEnable = true;
        if (attributeSet != null) {
            this.mRefreshStyle = attributeSet.getStyleAttribute();
        }
        if (this.mRefreshStyle == 0) {
            this.mRefreshStyle = i3;
        }
        COUIDarkModeUtil.setForceDarkAllow(this, false);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUIVerticalSeekBar, i3, i4);
        this.mEnableVibrator = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarEnableVibrator, true);
        this.mEnableAdaptiveVibrator = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarAdaptiveVibrator, false);
        this.mIsPhysicsEnable = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarPhysicsEnable, true);
        this.mShowProgress = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarShowProgress, true);
        this.mShowThumb = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarShowThumb, true);
        this.mIsProgressFull = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarProgressFull, false);
        this.mShowText = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarShowText, false);
        this.mIsSupportDeformation = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarDeformation, false);
        this.mBackgroundColorStateList = typedArrayObtainStyledAttributes.getColorStateList(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarBackgroundColor);
        this.mProgressColorStateList = typedArrayObtainStyledAttributes.getColorStateList(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarProgressColor);
        this.mThumbColorStateList = typedArrayObtainStyledAttributes.getColorStateList(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarThumbColor);
        this.mTextColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarTextColor, getResources().getColor(R.color.coui_seekbar_text_color));
        this.mBackgroundRadius = typedArrayObtainStyledAttributes.getDimension(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarBackgroundRadius, getResources().getDimension(R.dimen.coui_seekbar_background_radius));
        this.mProgressRadius = typedArrayObtainStyledAttributes.getDimension(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarProgressRadius, getResources().getDimension(R.dimen.coui_seekbar_progress_radius));
        this.mBackgroundRoundCornerWeight = typedArrayObtainStyledAttributes.getFloat(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarBackgroundRoundCornerWeight, 0.0f);
        this.mProgressRoundCornerWeight = typedArrayObtainStyledAttributes.getFloat(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarProgressRoundCornerWeight, 0.0f);
        float dimension = getResources().getDimension(R.dimen.coui_vertical_seekbar_progress_padding_vertical);
        this.mDefaultPaddingVertical = dimension;
        float dimension2 = typedArrayObtainStyledAttributes.getDimension(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarProgressPaddingVertical, dimension);
        this.mPaddingVertical = dimension2;
        if (dimension2 == 0.0f) {
            this.mPaddingVertical = this.mDefaultPaddingVertical;
        }
        this.mBackgroundWidth = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarBackgroundWidth, (int) (this.mBackgroundRadius * 2.0f));
        this.mProgressWidth = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarProgressWidth, (int) (this.mProgressRadius * 2.0f));
        this.mSeekbarMinWidth = typedArrayObtainStyledAttributes.getDimensionPixelOffset(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarMinWidth, getResources().getDimensionPixelSize(R.dimen.coui_vertical_seekbar_view_min_width));
        this.mMaxHeight = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarMaxHeight, 0);
        this.mBackgroundEnlargeScale = typedArrayObtainStyledAttributes.getFloat(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarBackGroundEnlargeScale, 6.0f);
        this.mProgressEnlargeScale = typedArrayObtainStyledAttributes.getFloat(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarProgressEnlargeScale, 4.0f);
        this.mBackgroundRadiusEnlargeScale = typedArrayObtainStyledAttributes.getFloat(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarBackGroundRadiusEnlargeScale, this.mBackgroundEnlargeScale);
        this.mProgressRadiusEnlargeScale = typedArrayObtainStyledAttributes.getFloat(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarProgressRadiusEnlargeScale, this.mProgressEnlargeScale);
        this.mTextContent = typedArrayObtainStyledAttributes.getString(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarText);
        this.mTextMarginTop = typedArrayObtainStyledAttributes.getDimension(R.styleable.COUIVerticalSeekBar_couiVerticalSeekBarTextMarginTop, getResources().getDimension(R.dimen.coui_vertical_seekbar_text_padding_top));
        typedArrayObtainStyledAttributes.recycle();
        this.mTextDrawable = new TextDrawable(getContext());
        this.mHasMotorVibrator = VibrateUtils.isLinearMotorVersion(context);
        this.mIsSupportSmoothRoundCorner = RoundCornerUtil.isVersionSupport();
        initView();
        setThumbBitmap();
        ensureSize();
    }

    public void startCustomDeformation(float f3, Interpolator interpolator, long j3) {
        if (this.mIsSupportDeformation) {
            float f4 = this.mScale;
            if ((f4 < 1.0f || f3 < 1.0f) && (f4 > 0.0f || f3 > 0.0f)) {
                return;
            }
            ValueAnimator valueAnimator = this.mButtonDeformationAnimator;
            if (valueAnimator == null) {
                this.mButtonDeformationAnimator = getButtonDeformationAnimator();
            } else {
                cancelAnim(valueAnimator);
            }
            this.mButtonDeformationAnimator.setValues(PropertyValuesHolder.ofFloat(HOLDER_SCALE, this.mScale, f3));
            this.mButtonDeformationAnimator.setInterpolator(interpolator);
            this.mButtonDeformationAnimator.setDuration(j3);
            this.mButtonDeformationAnimator.start();
        }
    }
}