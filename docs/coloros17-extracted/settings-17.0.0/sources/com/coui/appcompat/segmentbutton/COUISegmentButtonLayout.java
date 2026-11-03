package com.coui.appcompat.segmentbutton;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.TextViewCompat;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.COUIMaterialBlurEffect;
import com.coui.appcompat.COUIMaterialEffect;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.COUIMaterialStrokeEffect;
import com.coui.appcompat.OnMaterialStateChangeListener;
import com.coui.appcompat.accessibilityutil.COUIAccessibilityUtil;
import com.coui.appcompat.animation.COUIMoveEaseInterpolator;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.animation.touchanimation.COUIViewTouchAnimator;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.pressfeedback.COUIPressFeedbackHelper;
import com.coui.appcompat.roundRect.COUIShapePath;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.coui.appcompat.shadowedge.COUIShadowEdgeDrawable;
import com.coui.appcompat.shadowedge.IShadowEdgeEffect;
import com.coui.appcompat.spotlight.COUISpotLightEffect;
import com.coui.appcompat.spotlight.COUISpotLightEffectDrawable;
import com.coui.appcompat.state.COUIMaskEffectDrawable;
import com.coui.appcompat.state.COUIStateEffectDrawable;
import com.coui.appcompat.state.COUIStrokeDrawable;
import com.coui.appcompat.textutil.COUIChangeTextUtil;
import com.coui.appcompat.uiutil.ShadowUtils;
import com.coui.appcompat.uiutil.UIUtil;
import com.coui.component.responsiveui.ResponsiveUIModel;
import com.coui.component.responsiveui.layoutgrid.MarginType;
import com.decouple.injector.adaptor.BaseConfig;
import com.oplus.graphics.OplusPathAdapter;
import com.oplus.view.material.OplusMaterialCornerParams;
import com.support.appcompat.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/* JADX INFO: loaded from: classes7.dex */
public class COUISegmentButtonLayout extends LinearLayout implements ISegmentButtonLayout, OnMaterialStateChangeListener {
    private static final int BLUR_WINDOW_VIEW_BG_COLOR_DARK = 268435455;
    private static final int BLUR_WINDOW_VIEW_BG_COLOR_LIGHT = 788529151;
    private static final int COLOR_ANIMATOR_TIME = 300;
    private static final boolean COUI_DEBUG;
    private static final float DEFAULT_OVER_STIFFNESS = 0.12f;
    private static final int DEFAULT_SELECT_POSITION = 0;
    private static final float DEFAULT_SPRING_BOUNCE = 0.0f;
    private static final float DEFAULT_SPRING_RESPONSE = 0.6f;
    private static final int GRID_NUMBER_LARGE = 8;
    private static final int GRID_NUMBER_MEDIUM = 6;
    private static final int GRID_NUMBER_SMALL = 4;
    private static final float HEIGHT_SPRING_BOUNCE = 0.5f;
    private static final float HEIGHT_SPRING_RESPONSE = 0.6f;
    private static final float HEIGHT_SPRING_VELOCITY = -500.0f;
    private static final float INDICATOR_STROKE_CORNER_WEIGHT = 2.0f;
    private static final float MATERIAL_CORNER_WEIGHT = 1.0f;
    private static final float MIN_HEIGHT_PERCENT = 0.9f;
    public static final int PADDING_LARGE = 1;
    public static final int PADDING_SMALL = 0;
    private static final float POS_DIFF_CURVE_EXPONENT = 1.0f;
    private static final float POS_SPRING_BOUNCE = 0.1f;
    private static final float POS_SPRING_BOUNCE_MIN = 0.03f;
    private static final float POS_SPRING_RESPONSE = 0.4f;
    private static final float POS_SPRING_VELOCITY = 0.0f;
    private static final float SHADOW_DX = 0.0f;
    private static final String TAG = "COUISegmentButtonLayout";
    private static final float WIDTH_DIFF_CURVE_EXPONENT = 2.0f;
    private static final float WIDTH_SPRING_BOUNCE = 0.5f;
    private static final float WIDTH_SPRING_BOUNCE_MIN = 0.1f;
    private static final float WIDTH_SPRING_RESPONSE = 0.6f;
    private static final float WIDTH_SPRING_RESPONSE_MIN = 0.4f;
    private static final float WIDTH_SPRING_VELOCITY = 1000.0f;
    private AttributeSet mAttrs;
    private int mBackgroundColor;
    private final Path mBackgroundPath;
    private COUIShadowEdgeDrawable mBackgroundShadowEdgeDrawable;
    private COUIMaterialStrokeEffect.StrokeEffectType mBackgroundStrokeType;
    private boolean mBlurEffectEnabled;
    private final ArrayList<TextView> mButtons;
    private boolean mCausticShadowEffectEnabled;
    private int mCausticShadowLevel;
    private final ArrayList<Integer> mChildWidths;
    private final Paint mContainerPaint;
    private OplusMaterialCornerParams mCornerParams;
    private int mCustomButtonPaddingHorizontal;
    private float mCustomHeightPx;
    private int mCustomPaddingType;
    private float mCustomTextSizePx;
    private boolean mDragAnimationEnabled;
    private SegmentButtonDrawDelegate mDrawDelegate;
    private boolean mDrawMaterialEffect;
    private boolean mEnableRubberEffect;
    private boolean mEnableTextScale;
    private float mIndicatorCenterLastX;
    private float mIndicatorCenterX;
    private float mIndicatorCenterY;
    private int mIndicatorColor;
    private SmoothRoundCornerHelper mIndicatorCornerHelper;
    private float mIndicatorHeight;
    private final Paint mIndicatorPaint;
    private final Path mIndicatorPath;
    private final RectF mIndicatorRectF;
    private float mIndicatorScale;
    private COUIPressFeedbackHelper mIndicatorScaleHelper;
    private final int mIndicatorShadowOffset;
    private float mIndicatorWidth;
    private int mInitialPaddingBottom;
    private int mInitialPaddingLeft;
    private int mInitialPaddingRight;
    private int mInitialPaddingTop;
    private boolean mInnerShadowStrokeEnabled;
    private boolean mIsDragging;
    private boolean mIsNeedVibration;
    private boolean mIsNightMode;
    private boolean mIsResponsiveWidthEnabled;
    private boolean mIsSizeChanged;
    private int mLastMaterialUpdateHeight;
    private int mLastMaterialUpdateWidth;
    private int mLastSelectedPosition;
    private float mLastShadowEdgeHeight;
    private float mLastShadowEdgeRadius;
    private float mLastShadowEdgeWidth;
    private int mLastSpotLightHeight;
    private int mLastSpotLightWidth;
    private int mLastTouchPosition;
    private float mLastX;
    private boolean mMaterialEffectEnabled;
    private int mMaterialRubberMaxDistance;
    private final int mMinHeightPx;
    private float mMinSegmentHeight;
    private OnSegmentSelectedListener mOnSegmentSelectedListener;
    private OnSelectedSegmentChangeListener mOnSelectedSegmentChangeListener;
    private int mOuterShadowLevel;
    private int mOverDistance;
    private float mOverStiffness;
    private final ArrayList<COUIPressFeedbackHelper> mPressScaleHelper;
    private final ResponsiveUIModel mResponsiveUIModel;
    private final int mSegmentExtraWidth;
    private final int mSegmentExtraWidthSmall;
    private final int mSegmentMinWidth;
    private final int mSegmentPaddingHorizontal;
    private final int mSegmentPaddingRegular;
    private final int mSegmentPaddingSmall;
    private String[] mSegmentStrings;
    private int mSelectedPosition;
    private int mSelectedTextColor;
    private int mShadowColor;
    private final RectF mShadowEdgeClipRect;
    private COUIShadowEdgeDrawable mShadowEdgeDrawable;
    private SmoothRoundCornerHelper mShadowEdgeHelper;
    private final int[] mShadowGradientColors;
    private int mShadowGradientEndColor;
    private final Paint mShadowGradientPaint;
    private final float[] mShadowGradientStops;
    private final int mShadowRadius;
    private final RectF mShadowRectF;
    private SmoothRoundCornerHelper mSmoothRoundCornerHelper;
    private COUISpotLightEffectDrawable mSpotLightDrawable;
    private boolean mSpotLightEnabled;
    private COUISpotLightEffect.SpotLightType mSpotLightType;
    private boolean mSpotlightEffectEnabled;
    private COUISpringAnimation mSpringAnimationHeight;
    private COUISpringAnimation mSpringAnimationPos;
    private COUISpringAnimation mSpringAnimationRubber;
    private COUISpringAnimation mSpringAnimationWidth;
    private boolean mStrokeEffectEnabled;
    private int mStyleAttr;
    private int mStyleRes;
    private int mSwitchDistance;
    private final ArrayList<SegmentButtonTextColorChangeHelper> mTextColorChangeHelper;
    private TextPaint mTextPaint;
    private final TextView mTextView;
    private final Rect mTmpRect;
    private final RectF mTmpRectF;
    private COUIViewTouchAnimator mTouchAnimator;
    private int mTouchPosition;
    private int mTouchSlop;
    private int mUnselectedTextColor;
    private boolean mViewBlurEffectEnabled;

    /* JADX INFO: renamed from: com.coui.appcompat.segmentbutton.COUISegmentButtonLayout$1, reason: invalid class name */
    public class AnonymousClass1 extends AccessibilityDelegateCompat {
        final /* synthetic */ TextView val$button;
        final /* synthetic */ int val$position;

        public AnonymousClass1(int i3, TextView textView) {
            this.val$position = i3;
            this.val$button = textView;
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            accessibilityNodeInfoCompat.setClassName(COUIAccessibilityUtil.BUTTON_CLASS_NAME);
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(0, 1, this.val$position, 1, false, this.val$button.isSelected()));
            if (!this.val$button.isSelected()) {
                accessibilityNodeInfoCompat.setStateDescription(COUISegmentButtonLayout.this.getContext().getResources().getString(R.string.coui_accessibility_unselected));
            } else {
                accessibilityNodeInfoCompat.setClickable(false);
                accessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK);
            }
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public boolean performAccessibilityAction(View view, int i3, Bundle bundle) {
            if (i3 != AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK.getId()) {
                return super.performAccessibilityAction(view, i3, bundle);
            }
            COUISegmentButtonLayout.this.selectSegmentAt(this.val$button);
            return true;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.segmentbutton.COUISegmentButtonLayout$2, reason: invalid class name */
    public class AnonymousClass2 implements Comparator<IndexLengthPair> {
        public AnonymousClass2() {
        }

        @Override // java.util.Comparator
        public /* bridge */ /* synthetic */ int compare(IndexLengthPair indexLengthPair, IndexLengthPair indexLengthPair2) {
            return compare2(indexLengthPair, indexLengthPair2);
        }

        /* JADX INFO: renamed from: compare, reason: avoid collision after fix types in other method */
        public int compare2(IndexLengthPair indexLengthPair, IndexLengthPair indexLengthPair2) {
            return Integer.compare(indexLengthPair.mLength, indexLengthPair2.mLength);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.segmentbutton.COUISegmentButtonLayout$3, reason: invalid class name */
    public class AnonymousClass3 implements COUIDynamicAnimation.OnAnimationEndListener {
        public AnonymousClass3() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
        public void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
            COUISegmentButtonLayout.access$002(COUISegmentButtonLayout.this, true);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.segmentbutton.COUISegmentButtonLayout$4, reason: invalid class name */
    public class AnonymousClass4 implements COUIPressFeedbackHelper.COUIPressFeedbackHelperCallback {
        public AnonymousClass4() {
        }

        @Override // com.coui.appcompat.pressfeedback.COUIPressFeedbackHelper.COUIPressFeedbackHelperCallback
        public int getTargetHeight() {
            return (int) COUISegmentButtonLayout.access$400(COUISegmentButtonLayout.this);
        }

        @Override // com.coui.appcompat.pressfeedback.COUIPressFeedbackHelper.COUIPressFeedbackHelperCallback
        public int getTargetWidth() {
            return (int) COUISegmentButtonLayout.access$300(COUISegmentButtonLayout.this);
        }

        @Override // com.coui.appcompat.pressfeedback.COUIPressFeedbackHelper.COUIPressFeedbackHelperCallback
        public void onScaleUpdate(float f3) {
            COUISegmentButtonLayout.access$102(COUISegmentButtonLayout.this, f3);
            COUISegmentButtonLayout.access$200(COUISegmentButtonLayout.this);
        }
    }

    public static class IndexLengthPair {
        int mIndex;
        int mLength;

        public IndexLengthPair(int i3, int i4) {
            this.mIndex = i3;
            this.mLength = i4;
        }
    }

    @FunctionalInterface
    public interface OnSegmentSelectedListener {
        void onSegmentSelected(int i3);
    }

    public interface OnSelectedSegmentChangeListener {
        void onSelectedSegmentChange(int i3, int i4, float f3);
    }

    public interface SegmentButtonDrawDelegate {
        default Paint[] getCustomBackgroundPaint() {
            Paint customBackrgoundPaint = setCustomBackrgoundPaint();
            if (customBackrgoundPaint != null) {
                return new Paint[]{customBackrgoundPaint};
            }
            return null;
        }

        default Paint[] getCustomIndicatorPaint() {
            Paint customIndicatorPaint = setCustomIndicatorPaint();
            if (customIndicatorPaint != null) {
                return new Paint[]{customIndicatorPaint};
            }
            return null;
        }

        @Deprecated
        default Boolean needProxy() {
            return Boolean.TRUE;
        }

        @Deprecated
        default Paint setCustomBackrgoundPaint() {
            return null;
        }

        @Deprecated
        default Paint setCustomIndicatorPaint() {
            return null;
        }
    }

    public class SegmentButtonTextColorChangeHelper {
        private final ArgbEvaluator mColorEvaluator = new ArgbEvaluator();
        private float mCurrentFraction;
        private TextView mSegment;
        private final ValueAnimator mTextColorSelectAnimator;
        private final ValueAnimator mTextColorUnSelectAnimator;

        /* JADX INFO: renamed from: com.coui.appcompat.segmentbutton.COUISegmentButtonLayout$SegmentButtonTextColorChangeHelper$1, reason: invalid class name */
        public class AnonymousClass1 implements ValueAnimator.AnimatorUpdateListener {
            final /* synthetic */ COUISegmentButtonLayout val$this$0;

            public AnonymousClass1(COUISegmentButtonLayout cOUISegmentButtonLayout) {
                this.val$this$0 = cOUISegmentButtonLayout;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                SegmentButtonTextColorChangeHelper.access$502(SegmentButtonTextColorChangeHelper.this, valueAnimator.getAnimatedFraction());
                SegmentButtonTextColorChangeHelper.access$900(SegmentButtonTextColorChangeHelper.this).setTextColor(((Integer) SegmentButtonTextColorChangeHelper.access$800(SegmentButtonTextColorChangeHelper.this).evaluate(SegmentButtonTextColorChangeHelper.access$500(SegmentButtonTextColorChangeHelper.this), Integer.valueOf(COUISegmentButtonLayout.access$600(COUISegmentButtonLayout.this)), Integer.valueOf(COUISegmentButtonLayout.access$700(COUISegmentButtonLayout.this)))).intValue());
            }
        }

        /* JADX INFO: renamed from: com.coui.appcompat.segmentbutton.COUISegmentButtonLayout$SegmentButtonTextColorChangeHelper$2, reason: invalid class name */
        public class AnonymousClass2 implements ValueAnimator.AnimatorUpdateListener {
            final /* synthetic */ COUISegmentButtonLayout val$this$0;

            public AnonymousClass2(COUISegmentButtonLayout cOUISegmentButtonLayout) {
                this.val$this$0 = cOUISegmentButtonLayout;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                SegmentButtonTextColorChangeHelper.access$502(SegmentButtonTextColorChangeHelper.this, 1.0f - valueAnimator.getAnimatedFraction());
                SegmentButtonTextColorChangeHelper.access$900(SegmentButtonTextColorChangeHelper.this).setTextColor(((Integer) SegmentButtonTextColorChangeHelper.access$800(SegmentButtonTextColorChangeHelper.this).evaluate(SegmentButtonTextColorChangeHelper.access$500(SegmentButtonTextColorChangeHelper.this), Integer.valueOf(COUISegmentButtonLayout.access$600(COUISegmentButtonLayout.this)), Integer.valueOf(COUISegmentButtonLayout.access$700(COUISegmentButtonLayout.this)))).intValue());
            }
        }

        public SegmentButtonTextColorChangeHelper(TextView textView) {
            ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.mTextColorSelectAnimator = valueAnimatorOfFloat;
            ValueAnimator valueAnimatorOfFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.mTextColorUnSelectAnimator = valueAnimatorOfFloat2;
            this.mCurrentFraction = 0.0f;
            this.mSegment = textView;
            valueAnimatorOfFloat.addUpdateListener(new AnonymousClass1(COUISegmentButtonLayout.this));
            valueAnimatorOfFloat.setDuration(300L);
            valueAnimatorOfFloat.setInterpolator(new COUIMoveEaseInterpolator());
            valueAnimatorOfFloat2.addUpdateListener(new AnonymousClass2(COUISegmentButtonLayout.this));
            valueAnimatorOfFloat2.setDuration(300L);
            valueAnimatorOfFloat2.setInterpolator(new COUIMoveEaseInterpolator());
        }

        public static /* synthetic */ float access$500(SegmentButtonTextColorChangeHelper segmentButtonTextColorChangeHelper) {
            return segmentButtonTextColorChangeHelper.mCurrentFraction;
        }

        public static /* synthetic */ float access$502(SegmentButtonTextColorChangeHelper segmentButtonTextColorChangeHelper, float f3) {
            segmentButtonTextColorChangeHelper.mCurrentFraction = f3;
            return f3;
        }

        public static /* synthetic */ ArgbEvaluator access$800(SegmentButtonTextColorChangeHelper segmentButtonTextColorChangeHelper) {
            return segmentButtonTextColorChangeHelper.mColorEvaluator;
        }

        public static /* synthetic */ TextView access$900(SegmentButtonTextColorChangeHelper segmentButtonTextColorChangeHelper) {
            return segmentButtonTextColorChangeHelper.mSegment;
        }

        public void startAnimation(boolean z2) {
            if (z2) {
                this.mTextColorUnSelectAnimator.cancel();
                this.mTextColorSelectAnimator.setCurrentFraction(this.mCurrentFraction);
                this.mTextColorSelectAnimator.start();
            } else {
                this.mTextColorSelectAnimator.cancel();
                this.mTextColorUnSelectAnimator.setCurrentFraction(1.0f - this.mCurrentFraction);
                this.mTextColorUnSelectAnimator.start();
            }
        }
    }

    public static final class SmoothRoundCornerHelper {
        private final Path mPath;
        private OplusPathAdapter mPathAdapter;
        private final int mSmoothType;

        public SmoothRoundCornerHelper(Path path) {
            this.mPathAdapter = null;
            this.mPath = path;
            int smoothStyleType = RoundCornerUtil.getSmoothStyleType();
            this.mSmoothType = smoothStyleType;
            if (smoothStyleType == 1) {
                this.mPathAdapter = new OplusPathAdapter(path, smoothStyleType);
            }
        }

        public Path getPath() {
            return this.mPath;
        }

        public OplusPathAdapter getPathAdapter() {
            return this.mPathAdapter;
        }

        public int getSmoothType() {
            return this.mSmoothType;
        }

        public void updatePath(RectF rectF, float f3) {
            this.mPath.reset();
            COUIShapePath.getRoundRectPath(this.mPath, rectF, f3);
        }
    }

    static {
        COUI_DEBUG = COUILog.LOG_DEBUG || COUILog.isLoggable(TAG, 3);
    }

    public COUISegmentButtonLayout(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void a(COUISegmentButtonLayout cOUISegmentButtonLayout, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        cOUISegmentButtonLayout.mIndicatorCenterX = f3;
        cOUISegmentButtonLayout.updateIndicatorRect();
    }

    public static /* synthetic */ boolean access$002(COUISegmentButtonLayout cOUISegmentButtonLayout, boolean z2) {
        cOUISegmentButtonLayout.mEnableRubberEffect = z2;
        return z2;
    }

    public static /* synthetic */ float access$102(COUISegmentButtonLayout cOUISegmentButtonLayout, float f3) {
        cOUISegmentButtonLayout.mIndicatorScale = f3;
        return f3;
    }

    public static /* synthetic */ void access$200(COUISegmentButtonLayout cOUISegmentButtonLayout) {
        cOUISegmentButtonLayout.updateIndicatorRect();
    }

    public static /* synthetic */ float access$300(COUISegmentButtonLayout cOUISegmentButtonLayout) {
        return cOUISegmentButtonLayout.mIndicatorWidth;
    }

    public static /* synthetic */ float access$400(COUISegmentButtonLayout cOUISegmentButtonLayout) {
        return cOUISegmentButtonLayout.mIndicatorHeight;
    }

    public static /* synthetic */ int access$600(COUISegmentButtonLayout cOUISegmentButtonLayout) {
        return cOUISegmentButtonLayout.mUnselectedTextColor;
    }

    public static /* synthetic */ int access$700(COUISegmentButtonLayout cOUISegmentButtonLayout) {
        return cOUISegmentButtonLayout.mSelectedTextColor;
    }

    private void animateIndicatorToPosition(int i3) {
        COUISegmentButtonLayout cOUISegmentButtonLayout;
        float fCalculateDynamicWidthParam;
        COUISpringAnimation cOUISpringAnimation = this.mSpringAnimationRubber;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            this.mSpringAnimationRubber.cancel();
        }
        TextView segmentAt = getSegmentAt(i3);
        if (segmentAt == null) {
            COUILog.w(TAG, i3 + "out of range of segment button layout");
            return;
        }
        float left = segmentAt.getLeft();
        float right = segmentAt.getRight();
        segmentAt.getTop();
        segmentAt.getBottom();
        float f3 = (left + right) / 2.0f;
        float f4 = right - left;
        this.mIndicatorCenterLastX = f3;
        ensureSpringAnimation();
        boolean z2 = f4 > this.mIndicatorWidth;
        COUISpringForce spring = this.mSpringAnimationPos.getSpring();
        if (spring != null) {
            if (z2) {
                spring.setBounce(getEffectiveTranslationBounceForShortToLong(this.mIndicatorWidth, f4));
            } else {
                spring.setBounce(0.1f);
            }
        }
        float width = getWidth();
        float fMax = 0.0f;
        if (this.mButtons.size() > 0 && width > 0.0f) {
            TextView textView = this.mButtons.get(0);
            ArrayList<TextView> arrayList = this.mButtons;
            TextView textView2 = arrayList.get(arrayList.size() - 1);
            if (textView != null && textView2 != null && textView.getRight() > textView.getLeft() && textView2.getRight() > textView2.getLeft()) {
                float left2 = (textView.getLeft() + textView.getRight()) / 2.0f;
                float left3 = (textView2.getLeft() + textView2.getRight()) / 2.0f;
                float fMin = Math.min(left2, left3);
                float fMax2 = Math.max(left2, left3);
                fMax = Math.max(0.0f, fMin);
                width = Math.min(getWidth(), fMax2);
            }
        }
        float fMax3 = Math.max(fMax, Math.min(width, f3));
        this.mSpringAnimationPos.setMinValue(fMax);
        this.mSpringAnimationPos.setMaxValue(width);
        this.mSpringAnimationPos.setStartValue(Math.max(fMax, Math.min(width, this.mIndicatorCenterX)));
        this.mSpringAnimationPos.animateToFinalPosition(fMax3);
        if (z2) {
            this.mSpringAnimationWidth.setMaxValue(getEffectiveMaxExtraWidthForShortToLong() + f4);
            fCalculateDynamicWidthParam = 0.5f;
            cOUISegmentButtonLayout = this;
        } else {
            cOUISegmentButtonLayout = this;
            fCalculateDynamicWidthParam = cOUISegmentButtonLayout.calculateDynamicWidthParam(this.mIndicatorWidth, f4, 0.5f, 0.1f, 2.0f);
            cOUISegmentButtonLayout.mSpringAnimationWidth.setMaxValue(2.1474836E9f);
        }
        float fCalculateDynamicWidthParam2 = cOUISegmentButtonLayout.calculateDynamicWidthParam(cOUISegmentButtonLayout.mIndicatorWidth, f4, 0.6f, 0.4f, 2.0f);
        COUISpringForce spring2 = cOUISegmentButtonLayout.mSpringAnimationWidth.getSpring();
        if (spring2 != null) {
            spring2.setResponse(fCalculateDynamicWidthParam2);
            spring2.setBounce(fCalculateDynamicWidthParam);
            if (COUI_DEBUG) {
                COUILog.d(TAG, "dynamicBounce: " + fCalculateDynamicWidthParam + ", dynamicResponse: " + fCalculateDynamicWidthParam2);
            }
        }
        cOUISegmentButtonLayout.mSpringAnimationWidth.setStartValue(cOUISegmentButtonLayout.mIndicatorWidth);
        cOUISegmentButtonLayout.mSpringAnimationWidth.setStartVelocity(1000.0f);
        cOUISegmentButtonLayout.mSpringAnimationWidth.animateToFinalPosition(f4);
        float height = (cOUISegmentButtonLayout.getHeight() - cOUISegmentButtonLayout.getPaddingTop()) - cOUISegmentButtonLayout.getPaddingBottom();
        float f5 = 0.9f * height;
        float fMax4 = Math.max(f5, height);
        cOUISegmentButtonLayout.mSpringAnimationHeight.setMinValue(f5);
        cOUISegmentButtonLayout.mSpringAnimationHeight.setStartValue(Math.max(f5, cOUISegmentButtonLayout.mIndicatorHeight));
        cOUISegmentButtonLayout.mSpringAnimationHeight.setStartVelocity(HEIGHT_SPRING_VELOCITY);
        cOUISegmentButtonLayout.mSpringAnimationHeight.animateToFinalPosition(fMax4);
        OnSegmentSelectedListener onSegmentSelectedListener = cOUISegmentButtonLayout.mOnSegmentSelectedListener;
        if (onSegmentSelectedListener != null) {
            onSegmentSelectedListener.onSegmentSelected(cOUISegmentButtonLayout.mSelectedPosition);
        }
    }

    private void applyCausticShadowEffect() {
        ShadowUtils.setElevationToView(this, this.mCausticShadowLevel);
        COUIMaterialEffect.setOutlineCausticShadowColor(this, getResources().getColor(com.coui.appcompat.effect.R.color.coui_caustic_color_1));
        int i3 = this.mCausticShadowLevel;
        int i4 = i3 == 6 ? com.coui.appcompat.effect.R.dimen.coui_caustic_layout_l_level3 : com.coui.appcompat.effect.R.dimen.coui_caustic_layout_l_level5;
        int i5 = i3 == 6 ? com.coui.appcompat.effect.R.dimen.coui_caustic_layout_t_level3 : com.coui.appcompat.effect.R.dimen.coui_caustic_layout_t_level5;
        int i6 = i3 == 6 ? com.coui.appcompat.effect.R.dimen.coui_caustic_layout_r_level3 : com.coui.appcompat.effect.R.dimen.coui_caustic_layout_r_level5;
        int i7 = i3 == 6 ? com.coui.appcompat.effect.R.dimen.coui_caustic_layout_b_level3 : com.coui.appcompat.effect.R.dimen.coui_caustic_layout_b_level5;
        int i8 = i3 == 6 ? com.coui.appcompat.effect.R.dimen.coui_caustic_layout_sdf_level3 : com.coui.appcompat.effect.R.dimen.coui_caustic_layout_sdf_level5;
        COUIMaterialEffect.setOutlineCausticShadowLayout(this, getResources().getDimensionPixelOffset(i4), getResources().getDimensionPixelOffset(i5), getResources().getDimensionPixelOffset(i6), getResources().getDimensionPixelOffset(i7));
        COUIMaterialEffect.setOutlineCausticShadowParams(this, getResources().getDimensionPixelOffset(i8));
    }

    private void applyCustomPadding() {
        int i3 = this.mCustomPaddingType;
        if (i3 == 0) {
            int i4 = this.mSegmentPaddingSmall;
            setPadding(i4, i4, i4, i4);
            this.mCustomButtonPaddingHorizontal = this.mSegmentPaddingSmall;
            updateAllButtonsPadding();
        } else if (i3 == 1) {
            int i5 = this.mSegmentPaddingRegular;
            setPadding(i5, i5, i5, i5);
            this.mCustomButtonPaddingHorizontal = this.mSegmentPaddingRegular;
            updateAllButtonsPadding();
        } else {
            setPadding(this.mInitialPaddingLeft, this.mInitialPaddingTop, this.mInitialPaddingRight, this.mInitialPaddingBottom);
            this.mCustomButtonPaddingHorizontal = -1;
            updateAllButtonsPadding();
        }
        updateMaterialRubberMaxDistance();
    }

    private void applyInnerShadowStrokeParams() {
        if (isMaterialEffectSupported()) {
            if (this.mDrawMaterialEffect) {
                COUIMaterialEffect.setViewStrokeEffect(this, COUIMaterialStrokeEffect.StrokeEffectType.TYPE_NO_EFFECT);
            } else if (this.mInnerShadowStrokeEnabled && this.mStrokeEffectEnabled) {
                COUIMaterialEffect.setViewStrokeEffect(this, COUIMaterialStrokeEffect.StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_6);
            } else {
                COUIMaterialEffect.setViewStrokeEffect(this, COUIMaterialStrokeEffect.StrokeEffectType.TYPE_NO_EFFECT);
            }
        }
    }

    private void applyMaterialEffect() {
        int width = getWidth();
        int height = getHeight();
        if (width <= 0 || height <= 0) {
            COUILog.d(TAG, "[MaterialEffect] Apply skipped: invalid dimensions " + width + "x" + height);
            return;
        }
        this.mLastMaterialUpdateWidth = width;
        this.mLastMaterialUpdateHeight = height;
        float f3 = height / 2.0f;
        OplusMaterialCornerParams oplusMaterialCornerParams = this.mCornerParams;
        if (oplusMaterialCornerParams != null) {
            oplusMaterialCornerParams.setRadius(f3);
            this.mCornerParams.setWeight(1.0f);
            COUIMaterialEffect.setCornerParams(this, this.mCornerParams);
        }
        applyInnerShadowStrokeParams();
        applyShadow();
        applyViewBlurEffect();
        COUILog.d(TAG, "[MaterialEffect] Applied: size=" + width + "x" + height + ", blur=" + this.mViewBlurEffectEnabled + ", stroke=" + this.mInnerShadowStrokeEnabled);
    }

    private void applyOuterShadow() {
        int i3 = this.mOuterShadowLevel;
        if (i3 == -1) {
            ShadowUtils.clearShadow(this);
        } else {
            ShadowUtils.setElevationToView(this, i3);
        }
    }

    private void applyShadow() {
        if (this.mCausticShadowEffectEnabled && this.mCausticShadowLevel != -1) {
            applyCausticShadowEffect();
        } else {
            applyOuterShadow();
            COUIMaterialEffect.clearCausticShadow(this);
        }
    }

    private void applyViewBlurEffect() {
        if (isMaterialEffectSupported()) {
            if (this.mDrawMaterialEffect) {
                COUIMaterialEffect.setViewBlurEffect(this, COUIMaterialBlurEffect.BlurEffectType.TYPE_NO_EFFECT);
                UIUtil.setViewShadowClippingEnabled(this, false);
            } else if (this.mViewBlurEffectEnabled && this.mBlurEffectEnabled) {
                COUIMaterialEffect.setViewBlurEffect(this, COUIMaterialBlurEffect.BlurEffectType.TYPE_CONTENT_SEGMENT_BUTTON);
                UIUtil.setViewShadowClippingEnabled(this, true);
            } else {
                COUIMaterialEffect.setViewBlurEffect(this, COUIMaterialBlurEffect.BlurEffectType.TYPE_NO_EFFECT);
                UIUtil.setViewShadowClippingEnabled(this, false);
            }
        }
    }

    private void attemptClaimDrag() {
        if (getParent() instanceof ViewGroup) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
    }

    public static /* synthetic */ void b(COUISegmentButtonLayout cOUISegmentButtonLayout, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        cOUISegmentButtonLayout.mIndicatorCenterX = f3;
        cOUISegmentButtonLayout.updateIndicatorRect();
        if (cOUISegmentButtonLayout.mOnSelectedSegmentChangeListener != null) {
            cOUISegmentButtonLayout.mOnSelectedSegmentChangeListener.onSelectedSegmentChange(cOUISegmentButtonLayout.mLastSelectedPosition, cOUISegmentButtonLayout.mSelectedPosition, cOUISegmentButtonLayout.getPositionChangeProgress());
        }
    }

    public static /* synthetic */ void c(COUISegmentButtonLayout cOUISegmentButtonLayout, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        cOUISegmentButtonLayout.getClass();
        cOUISegmentButtonLayout.mIndicatorHeight = Math.abs(f3);
        cOUISegmentButtonLayout.updateIndicatorRect();
    }

    private void calculateChildrenWidths(float f3) {
        int size = this.mButtons.size();
        if (size == 0) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        int i3 = 0;
        for (int i4 = 0; i4 < size; i4++) {
            arrayList.add(new IndexLengthPair(i4, getSegmentTextWidth(this.mButtons.get(i4))));
        }
        Collections.sort(arrayList, new AnonymousClass2());
        float f4 = f3 / size;
        float[] fArr = new float[size];
        int i5 = 0;
        float f5 = f4;
        int i6 = -1;
        while (i5 < size) {
            float f6 = ((IndexLengthPair) arrayList.get(i5)).mLength;
            if (f6 < f5) {
                fArr[((IndexLengthPair) arrayList.get(i5)).mIndex] = f6;
                f3 -= f6;
                i5++;
            } else {
                if (i6 == i5) {
                    while (i5 < size) {
                        fArr[((IndexLengthPair) arrayList.get(i5)).mIndex] = f5;
                        i5++;
                    }
                    f3 = 0.0f;
                    break;
                }
                f5 = f3 / (size - i5);
                i6 = i5;
            }
        }
        if (i6 == -1) {
            this.mChildWidths.clear();
            while (i3 < size) {
                this.mChildWidths.add(Integer.valueOf(Math.round(f4)));
                i3++;
            }
            return;
        }
        if (f3 > 0.0f) {
            int i7 = 0;
            while (true) {
                int i8 = size - 1;
                if (i7 >= i8) {
                    break;
                }
                int i9 = ((IndexLengthPair) arrayList.get(i7)).mIndex;
                fArr[i9] = fArr[i9] + (f3 / i8);
                i7++;
            }
        }
        this.mChildWidths.clear();
        while (i3 < size) {
            this.mChildWidths.add(Integer.valueOf(Math.round(fArr[i3])));
            i3++;
        }
    }

    private float calculateDynamicWidthParam(float f3, float f4, float f5, float f6, float f7) {
        if (f3 <= 0.0f || f4 <= 0.0f) {
            return f5;
        }
        return Math.max(0.0f, Math.min(1.0f, f5 - ((f5 - f6) * ((float) Math.pow(Math.abs(f3 - f4) / Math.max(f3, f4), f7)))));
    }

    private static double[] calculateRubberBand(double d3, double d4, double[] dArr) {
        double[] dArr2 = new double[dArr.length];
        for (int i3 = 0; i3 < dArr.length; i3++) {
            if (d4 == 0.0d || d3 == 0.0d) {
                dArr2[i3] = 0.0d;
            } else {
                dArr2[i3] = (1.0d - (1.0d / (((Math.abs(dArr[i3]) * d3) / d4) + 1.0d))) * d4 * Math.signum(dArr[i3]);
            }
        }
        return dArr2;
    }

    private void clampIndicatorRectToContainer() {
        float width = getWidth();
        if (width <= 0.0f) {
            return;
        }
        int i3 = this.mMaterialRubberMaxDistance;
        float f3 = i3 + 0.0f;
        float f4 = width - i3;
        RectF rectF = this.mIndicatorRectF;
        if (rectF.left < f3) {
            rectF.left = f3;
        }
        if (rectF.right > f4) {
            rectF.right = f4;
        }
    }

    private void clearViewBlurEffect() {
        COUIMaterialEffect.setBackgroundRenderEffect(null, this);
        COUILog.d(TAG, "[MaterialEffect] View blur effect cleared");
    }

    private void clearViewStrokeEffect() {
        if (isMaterialEffectSupported()) {
            COUIMaterialEffect.setCornerParams(this, null);
            COUIMaterialEffect.setViewStrokeEffect(this, COUIMaterialStrokeEffect.StrokeEffectType.TYPE_NO_EFFECT);
            COUILog.d(TAG, "[MaterialEffect] View stroke effect cleared");
        }
    }

    public static /* synthetic */ void d(COUISegmentButtonLayout cOUISegmentButtonLayout, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        cOUISegmentButtonLayout.mIndicatorWidth = f3;
        if (COUI_DEBUG) {
            COUILog.d(TAG, "update width: " + cOUISegmentButtonLayout.mIndicatorWidth);
        }
        cOUISegmentButtonLayout.updateIndicatorRect();
    }

    private void drawBackgroundShadowEdge(Canvas canvas) {
        if (this.mDrawMaterialEffect && this.mBackgroundShadowEdgeDrawable != null && this.mMaterialEffectEnabled && this.mInnerShadowStrokeEnabled && this.mStrokeEffectEnabled) {
            float drawableRadius = getDrawableRadius(this.mTmpRectF);
            this.mBackgroundShadowEdgeDrawable.setBounds(this.mTmpRect);
            this.mBackgroundShadowEdgeDrawable.setCornerRadius(drawableRadius);
            this.mBackgroundShadowEdgeDrawable.draw(canvas);
        }
    }

    private void drawButtonBackground(Canvas canvas, Paint paint) {
        drawSmoothRoundRect(this.mSmoothRoundCornerHelper, canvas, this.mTmpRectF, paint);
    }

    private void drawIndicator(Canvas canvas, Boolean bool, Paint paint) {
        if (bool.booleanValue()) {
            drawShadowWithGradient(canvas);
            return;
        }
        canvas.save();
        drawSmoothRoundRect(this.mIndicatorCornerHelper, canvas, this.mIndicatorRectF, paint);
        canvas.restore();
    }

    private void drawIndicatorShadowEdge(Canvas canvas) {
        if (this.mShadowEdgeDrawable != null && this.mMaterialEffectEnabled && this.mInnerShadowStrokeEnabled && this.mStrokeEffectEnabled && this.mIndicatorRectF.width() > 0.0f && this.mIndicatorRectF.height() > 0.0f) {
            int iSave = canvas.save();
            RectF rectF = this.mIndicatorRectF;
            canvas.translate(rectF.left, rectF.top);
            float fWidth = this.mIndicatorRectF.width();
            float fHeight = this.mIndicatorRectF.height();
            float drawableRadius = getDrawableRadius(this.mIndicatorRectF);
            if (fWidth != this.mLastShadowEdgeWidth || fHeight != this.mLastShadowEdgeHeight || drawableRadius != this.mLastShadowEdgeRadius) {
                this.mShadowEdgeClipRect.set(0.0f, 0.0f, fWidth, fHeight);
                updatePath(this.mShadowEdgeHelper, this.mShadowEdgeClipRect);
                this.mLastShadowEdgeWidth = fWidth;
                this.mLastShadowEdgeHeight = fHeight;
                this.mLastShadowEdgeRadius = drawableRadius;
            }
            canvas.clipPath(this.mShadowEdgeHelper.getPath());
            this.mShadowEdgeDrawable.setBounds(0, 0, (int) fWidth, (int) fHeight);
            this.mShadowEdgeDrawable.setCornerRadius(drawableRadius);
            this.mShadowEdgeDrawable.setIsDarkMode(this.mIsNightMode);
            this.mShadowEdgeDrawable.draw(canvas);
            canvas.restoreToCount(iSave);
        }
    }

    private void drawShadowWithGradient(Canvas canvas) {
        if (this.mIndicatorRectF.width() <= 0.0f || this.mIndicatorRectF.height() <= 0.0f) {
            return;
        }
        int iSave = canvas.save();
        canvas.translate(getScrollX(), getScrollY());
        float drawableRadius = getDrawableRadius(this.mIndicatorRectF);
        updatePath(this.mIndicatorCornerHelper, this.mIndicatorRectF);
        canvas.clipOutPath(this.mIndicatorCornerHelper.getPath());
        RectF rectF = this.mShadowRectF;
        RectF rectF2 = this.mIndicatorRectF;
        rectF.left = rectF2.left;
        rectF.right = rectF2.right;
        rectF.top = rectF2.top + this.mIndicatorShadowOffset;
        rectF.bottom = this.mTmpRectF.bottom;
        if (this.mIsNightMode) {
            this.mShadowGradientPaint.setStyle(Paint.Style.FILL);
            this.mShadowGradientPaint.setShader(null);
            this.mShadowGradientPaint.setColor(this.mShadowColor);
        } else {
            RadialGradient radialGradient = new RadialGradient(rectF2.centerX(), this.mIndicatorRectF.centerY(), this.mShadowRectF.width() / 2.0f, this.mShadowGradientColors, this.mShadowGradientStops, Shader.TileMode.CLAMP);
            this.mShadowGradientPaint.setStyle(Paint.Style.FILL);
            this.mShadowGradientPaint.setShader(radialGradient);
        }
        canvas.drawRoundRect(this.mShadowRectF, drawableRadius, drawableRadius, this.mShadowGradientPaint);
        canvas.restoreToCount(iSave);
    }

    private void drawSmoothRoundRect(SmoothRoundCornerHelper smoothRoundCornerHelper, Canvas canvas, RectF rectF, Paint paint) {
        float drawableRadius = getDrawableRadius(rectF);
        int smoothType = smoothRoundCornerHelper.getSmoothType();
        if (smoothType != 0 && smoothType != 1) {
            canvas.drawRoundRect(rectF, drawableRadius, drawableRadius, paint);
        } else {
            canvas.clipPath(smoothRoundCornerHelper.getPath());
            canvas.drawRect(rectF, paint);
        }
    }

    private void drawSpotLight(Canvas canvas) {
        if (this.mSpotLightDrawable != null && this.mSpotLightEnabled && this.mSpotlightEffectEnabled) {
            int iSave = canvas.save();
            canvas.translate(getScrollX(), getScrollY());
            canvas.clipPath(this.mBackgroundPath);
            this.mSpotLightDrawable.draw(canvas);
            canvas.restoreToCount(iSave);
        }
    }

    private void ensureIndicatorScaleAnimation() {
        if (this.mIndicatorScaleHelper == null) {
            COUIPressFeedbackHelper cOUIPressFeedbackHelper = new COUIPressFeedbackHelper(getContext());
            this.mIndicatorScaleHelper = cOUIPressFeedbackHelper;
            cOUIPressFeedbackHelper.setCallback(new AnonymousClass4());
        }
    }

    private void ensureSpringAnimation() {
        if (this.mSpringAnimationPos == null) {
            COUISpringForce cOUISpringForce = new COUISpringForce();
            cOUISpringForce.setBounce(0.1f);
            cOUISpringForce.setResponse(0.4f);
            COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(new FloatValueHolder(this.mIndicatorCenterX));
            this.mSpringAnimationPos = cOUISpringAnimation;
            cOUISpringAnimation.setSpring(cOUISpringForce);
            this.mSpringAnimationPos.setStartVelocity(0.0f);
            this.mSpringAnimationPos.addUpdateListener(new a(this));
            this.mSpringAnimationPos.addEndListener(new AnonymousClass3());
        }
        if (this.mSpringAnimationWidth == null) {
            COUISpringForce cOUISpringForce2 = new COUISpringForce();
            cOUISpringForce2.setBounce(0.5f);
            cOUISpringForce2.setResponse(0.6f);
            COUISpringAnimation cOUISpringAnimation2 = new COUISpringAnimation(new FloatValueHolder(this.mIndicatorWidth));
            this.mSpringAnimationWidth = cOUISpringAnimation2;
            cOUISpringAnimation2.setSpring(cOUISpringForce2);
            this.mSpringAnimationWidth.addUpdateListener(new b(this));
        }
        if (this.mSpringAnimationHeight == null) {
            COUISpringForce cOUISpringForce3 = new COUISpringForce();
            cOUISpringForce3.setBounce(0.5f);
            cOUISpringForce3.setResponse(0.6f);
            COUISpringAnimation cOUISpringAnimation3 = new COUISpringAnimation(new FloatValueHolder(this.mIndicatorHeight));
            this.mSpringAnimationHeight = cOUISpringAnimation3;
            cOUISpringAnimation3.setStartVelocity(HEIGHT_SPRING_VELOCITY);
            this.mSpringAnimationHeight.setSpring(cOUISpringForce3);
            this.mSpringAnimationHeight.addUpdateListener(new c(this));
        }
        if (this.mSpringAnimationRubber == null) {
            COUISpringForce cOUISpringForce4 = new COUISpringForce();
            cOUISpringForce4.setBounce(0.0f);
            cOUISpringForce4.setResponse(0.6f);
            COUISpringAnimation cOUISpringAnimation4 = new COUISpringAnimation(new FloatValueHolder(this.mIndicatorCenterX));
            this.mSpringAnimationRubber = cOUISpringAnimation4;
            cOUISpringAnimation4.setSpring(cOUISpringForce4);
            this.mSpringAnimationRubber.addUpdateListener(new d(this));
        }
        ensureIndicatorScaleAnimation();
    }

    private void executeScaleAnimatorAt(boolean z2, int i3) {
        if (i3 < 0 || i3 >= this.mPressScaleHelper.size()) {
            return;
        }
        this.mPressScaleHelper.get(i3).executeFeedbackAnimator(z2);
    }

    private void executeTextColorChangeAnimatorAt(boolean z2, int i3) {
        if (i3 < 0 || i3 >= this.mTextColorChangeHelper.size()) {
            return;
        }
        this.mTextColorChangeHelper.get(i3).startAnimation(z2);
    }

    private float getDrawableRadius(RectF rectF) {
        return (rectF.bottom - rectF.top) / 2.0f;
    }

    private float getEffectiveMaxExtraWidthForShortToLong() {
        return isSmallPadding() ? this.mSegmentExtraWidthSmall : this.mSegmentExtraWidth;
    }

    private float getEffectiveTranslationBounceForShortToLong(float f3, float f4) {
        if (!isSmallPadding() || f3 <= 0.0f || f4 <= 0.0f) {
            return 0.1f;
        }
        return calculateDynamicWidthParam(f3, f4, 0.1f, 0.03f, 1.0f);
    }

    private float getPositionChangeProgress() {
        int size = this.mButtons.size();
        if (size <= 1) {
            return 0.0f;
        }
        float f3 = this.mIndicatorCenterX;
        float left = (this.mButtons.get(0).getLeft() + this.mButtons.get(0).getRight()) / 2.0f;
        int i3 = 1;
        float f4 = left;
        while (i3 < size) {
            float left2 = (this.mButtons.get(i3).getLeft() + this.mButtons.get(i3).getRight()) / 2.0f;
            float fMin = Math.min(f4, left2);
            float fMax = Math.max(f4, left2);
            if (f3 >= fMin && f3 <= fMax) {
                return (i3 - 1) + Math.max(0.0f, Math.min(1.0f, left2 != f4 ? (f3 - f4) / (left2 - f4) : 0.0f));
            }
            i3++;
            f4 = left2;
        }
        if (Math.abs(f3 - left) <= Math.abs(f3 - f4)) {
            return 0.0f;
        }
        return size - 1;
    }

    private int getResponsiveWidth() {
        int screenWidthMetrics = UIUtil.getScreenWidthMetrics(getContext());
        int screenHeightMetrics = UIUtil.getScreenHeightMetrics(getContext());
        this.mResponsiveUIModel.rebuild(screenWidthMetrics, screenHeightMetrics).chooseMargin(MarginType.MARGIN_SMALL);
        if (COUIResponsiveUtils.isLargeScreen(getContext(), screenWidthMetrics, screenHeightMetrics)) {
            return this.mResponsiveUIModel.calculateGridWidth(8);
        }
        return COUIResponsiveUtils.isMediumScreen(getContext(), screenWidthMetrics, screenHeightMetrics) ? this.mResponsiveUIModel.calculateGridWidth(6) : this.mResponsiveUIModel.calculateGridWidth(4);
    }

    private int getSegmentTextWidth(TextView textView) {
        TextPaint paint = this.mTextPaint;
        if (paint == null) {
            paint = textView.getPaint();
        }
        int iMeasureText = (int) paint.measureText(textView.getText().toString());
        int i3 = this.mCustomButtonPaddingHorizontal;
        if (i3 < 0) {
            i3 = this.mSegmentPaddingHorizontal;
        }
        return Math.max(iMeasureText + (i3 * 2), this.mSegmentMinWidth);
    }

    private int getSelectedIndexFromX(MotionEvent motionEvent) {
        for (int i3 = 0; i3 < this.mButtons.size(); i3++) {
            if (isInsideView(this.mButtons.get(i3), motionEvent.getX())) {
                return i3;
            }
        }
        return -1;
    }

    private void handleActionCancel() {
        resetTouchEffect();
        this.mIsDragging = false;
    }

    private void handleRubberBandEffect(float f3) {
        if (this.mEnableRubberEffect) {
            this.mLastX = f3;
            this.mEnableRubberEffect = false;
        }
        float f4 = f3 - this.mLastX;
        if (f4 != 0.0f) {
            double[] dArrCalculateRubberBand = calculateRubberBand(this.mOverStiffness, this.mOverDistance, new double[]{f4});
            ensureSpringAnimation();
            this.mSpringAnimationRubber.setStartValue(this.mIndicatorCenterX);
            this.mSpringAnimationRubber.animateToFinalPosition(this.mIndicatorCenterLastX + ((float) dArrCalculateRubberBand[0]));
        }
    }

    private void handleSpotLight(MotionEvent motionEvent) {
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLightDrawable;
        if (cOUISpotLightEffectDrawable != null && this.mSpotLightEnabled && this.mSpotlightEffectEnabled) {
            cOUISpotLightEffectDrawable.onTouchEvent(motionEvent);
        }
    }

    private void initBackgroundShadowEdgeDrawable() {
        if (isMaterialEffectSupported()) {
            COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = new COUIShadowEdgeDrawable();
            this.mBackgroundShadowEdgeDrawable = cOUIShadowEdgeDrawable;
            cOUIShadowEdgeDrawable.setCallback(this);
            this.mBackgroundShadowEdgeDrawable.setCornerType(2);
            this.mBackgroundShadowEdgeDrawable.setWeight(2.0f);
            this.mBackgroundShadowEdgeDrawable.setEdgeStyle(IShadowEdgeEffect.EdgeStyle.EDGE_STYLE_0);
            setStrokeEffectType(this.mBackgroundStrokeType, null);
        }
    }

    private void initPaint() {
        Paint paint = this.mContainerPaint;
        Paint.Style style = Paint.Style.FILL;
        paint.setStyle(style);
        this.mContainerPaint.setAntiAlias(true);
        this.mIndicatorPaint.setStyle(style);
        this.mIndicatorPaint.setAntiAlias(true);
        initShadowGradient();
        TextViewCompat.setTextAppearance(this.mTextView, R.style.couiTextButtonM);
        refreshTextPaint();
    }

    private void initShadowEdgeDrawable() {
        if (isMaterialEffectSupported()) {
            COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = new COUIShadowEdgeDrawable();
            this.mShadowEdgeDrawable = cOUIShadowEdgeDrawable;
            cOUIShadowEdgeDrawable.setCallback(this);
            COUIMaterialStrokeEffect.Companion.StrokeEffectParams strokeParamsByType = COUIMaterialEffect.getStrokeParamsByType(getContext(), COUIMaterialStrokeEffect.StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_10);
            this.mShadowEdgeDrawable.setCornerType(2).setWeight(2.0f).setEdgeStyle(IShadowEdgeEffect.EdgeStyle.EDGE_STYLE_0).setShadowEnable(false).setEdgeEnable(true).setLineAlpha(strokeParamsByType.getEdgeAlpha()).setLineWidth(strokeParamsByType.getEdgeWidth(getContext())).setLineAngle(strokeParamsByType.getEdgeAngle()).setFadeAlpha(strokeParamsByType.getShadowFadeIn(), strokeParamsByType.getShadowFadeOut()).setFadeWidthScale(strokeParamsByType.getShadowFadeScale()).setIsDarkMode(this.mIsNightMode);
            COUILog.d(TAG, "Indicator shadow edge drawable initialized");
        }
    }

    private void initShadowGradient() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setGradientType(1);
        updateShadowGradientColors();
    }

    private void initSpotLight() {
        if (isMaterialEffectSupported()) {
            COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = new COUISpotLightEffectDrawable();
            this.mSpotLightDrawable = cOUISpotLightEffectDrawable;
            cOUISpotLightEffectDrawable.setCallback(this);
            setSpotLightEffectType(this.mSpotLightType, null);
        }
    }

    private void initStateEffectBackground(View view) {
        Drawable background = view.getBackground();
        if (view instanceof TextView) {
            COUIStrokeDrawable cOUIStrokeDrawable = new COUIStrokeDrawable(getContext());
            cOUIStrokeDrawable.setStrokeType(1);
            view.setDefaultFocusHighlightEnabled(false);
            cOUIStrokeDrawable.setCallback(view);
            COUIMaskEffectDrawable cOUIMaskEffectDrawable = new COUIMaskEffectDrawable(getContext(), 0);
            if (background == null) {
                background = new ColorDrawable(0);
            }
            view.setBackground(new COUIStateEffectDrawable(new Drawable[]{background, cOUIMaskEffectDrawable, cOUIStrokeDrawable}));
        }
    }

    private void initTouchMotionAnimator() {
        if (!COUIMaterialStateManager.INSTANCE.materialVersionCheck()) {
            COUILog.w(TAG, "mTouchAnimator version < 37");
            return;
        }
        COUIViewTouchAnimator cOUIViewTouchAnimator = new COUIViewTouchAnimator(getContext(), this);
        this.mTouchAnimator = cOUIViewTouchAnimator;
        cOUIViewTouchAnimator.setDeformationEnable(true).setRubberEnable(true).setPressScaleEnable(true);
    }

    private void initializeSegmentButton(TextView textView, int i3, String str) {
        textView.setGravity(17);
        textView.setTextAlignment(4);
        textView.setIncludeFontPadding(false);
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        int i4 = this.mCustomButtonPaddingHorizontal;
        if (i4 < 0) {
            i4 = this.mSegmentPaddingHorizontal;
        }
        ViewCompat.setPaddingRelative(textView, i4, 0, i4, 0);
        textView.setClickable(true);
        textView.setFocusable(true);
        textView.setMinWidth(this.mSegmentMinWidth);
        if (i3 == this.mSelectedPosition) {
            textView.setSelected(true);
        }
        ViewCompat.setAccessibilityDelegate(textView, new AnonymousClass1(i3, textView));
        initStateEffectBackground(textView);
        this.mPressScaleHelper.add(new COUIPressFeedbackHelper(textView));
        this.mTextColorChangeHelper.add(new SegmentButtonTextColorChangeHelper(textView));
        textView.setText(str);
        refreshSegmentTextConfig(textView, i3);
    }

    private boolean isInsideView(View view, float f3) {
        if (view == null || view.getVisibility() == 8) {
            return false;
        }
        float f4 = this.mIsDragging ? this.mSwitchDistance : 0.0f;
        return f3 >= ((float) view.getLeft()) + f4 && f3 <= ((float) view.getRight()) - f4;
    }

    private boolean isMaterialEffectSupported() {
        COUIMaterialStateManager cOUIMaterialStateManager = COUIMaterialStateManager.INSTANCE;
        if (cOUIMaterialStateManager.isLowAnimationLevel()) {
            COUILog.d(TAG, "[MaterialEffect] Material effect not supported: low-end device (B+ or B)");
            return false;
        }
        if (!cOUIMaterialStateManager.materialVersionCheck()) {
            COUILog.d(TAG, "[MaterialEffect] Material effect not supported: version < 37");
            return false;
        }
        if (cOUIMaterialStateManager.checkThemeEnable(getContext())) {
            return true;
        }
        COUILog.d(TAG, "[MaterialEffect] Material effect not supported: global theme enabled");
        return false;
    }

    private boolean isSmallPadding() {
        int paddingStart = getPaddingStart();
        return Math.abs(paddingStart - this.mSegmentPaddingSmall) < Math.abs(paddingStart - this.mSegmentPaddingRegular);
    }

    private boolean isTouchInIndicator(float f3, float f4) {
        RectF rectF = this.mIndicatorRectF;
        if (rectF != null && rectF.width() > 0.0f && this.mIndicatorRectF.height() > 0.0f) {
            RectF rectF2 = this.mIndicatorRectF;
            if (f3 >= rectF2.left - 10.0f && f3 <= rectF2.right + 10.0f && f4 >= rectF2.top - 10.0f && f4 <= rectF2.bottom + 10.0f) {
                return true;
            }
        }
        return false;
    }

    private void loadAttr(Context context, AttributeSet attributeSet, int i3, int i4) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, com.support.segmentbutton.R.styleable.COUISegmentButtonLayout, i3, i4);
        int i5 = com.support.segmentbutton.R.styleable.COUISegmentButtonLayout_UnSelectedTextColor;
        int i6 = R.attr.couiColorLabelPrimary;
        this.mUnselectedTextColor = typedArrayObtainStyledAttributes.getColor(i5, COUIContextUtil.getAttrColor(context, i6));
        this.mSelectedTextColor = typedArrayObtainStyledAttributes.getColor(com.support.segmentbutton.R.styleable.COUISegmentButtonLayout_selectedTextColor, COUIContextUtil.getAttrColor(context, i6));
        this.mCustomTextSizePx = typedArrayObtainStyledAttributes.getDimensionPixelSize(com.support.segmentbutton.R.styleable.COUISegmentButtonLayout_segmentButtonTextSize, 0);
        this.mEnableTextScale = typedArrayObtainStyledAttributes.getBoolean(com.support.segmentbutton.R.styleable.COUISegmentButtonLayout_enableTextScale, true);
        this.mIsResponsiveWidthEnabled = typedArrayObtainStyledAttributes.getBoolean(com.support.segmentbutton.R.styleable.COUISegmentButtonLayout_enableResponsiveWidth, true);
        typedArrayObtainStyledAttributes.recycle();
        this.mIndicatorColor = COUIContextUtil.getColor(context, com.support.segmentbutton.R.color.coui_color_segment_button_indicator);
        this.mBackgroundColor = COUIContextUtil.getAttrColor(context, com.support.segmentbutton.R.attr.couiColorSegmentButtonBackground, COUIContextUtil.getColor(context, com.support.segmentbutton.R.color.coui_color_segment_button_background));
        this.mShadowColor = COUIContextUtil.getColor(context, com.support.segmentbutton.R.color.coui_segment_button_shadow_color);
        this.mShadowGradientEndColor = COUIContextUtil.getColor(context, com.support.segmentbutton.R.color.coui_segment_button_shadow_end_color);
        this.mIsNightMode = COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(context);
    }

    private void performHapticFeedback() {
        if (this.mIsNeedVibration) {
            performHapticFeedback(302);
        }
    }

    private void refreshSegmentTextConfig(TextView textView, int i3) {
        if (textView == null || i3 < 0 || i3 > this.mButtons.size()) {
            return;
        }
        TextViewCompat.setTextAppearance(textView, R.style.couiTextButtonM);
        if (this.mCustomTextSizePx > 0.0f) {
            textView.getPaint().setTextSize(this.mCustomTextSizePx);
        }
        if (this.mEnableTextScale) {
            COUIChangeTextUtil.adaptFontSize(textView, 4);
        }
        if (i3 == this.mSelectedPosition) {
            textView.setTextColor(this.mSelectedTextColor);
        } else {
            textView.setTextColor(this.mUnselectedTextColor);
        }
    }

    private void refreshTextPaint() {
        if (this.mCustomTextSizePx > 0.0f) {
            this.mTextView.getPaint().setTextSize(this.mCustomTextSizePx);
        }
        if (this.mEnableTextScale) {
            COUIChangeTextUtil.adaptFontSize(this.mTextView, 4);
        }
        this.mTextPaint = this.mTextView.getPaint();
    }

    private void removeMaterialEffect() {
        clearViewStrokeEffect();
        clearViewBlurEffect();
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLightDrawable;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.extinguish();
        }
        ShadowUtils.clearShadow(this);
        this.mLastMaterialUpdateWidth = -1;
        this.mLastMaterialUpdateHeight = -1;
        invalidate();
        COUILog.d(TAG, "[MaterialEffect] Material effect removed, all sub-effects cleared");
    }

    private void resetAnimation(COUISpringAnimation cOUISpringAnimation) {
        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) {
            return;
        }
        cOUISpringAnimation.skipToEnd();
    }

    private void resetIndicatorRect() {
        if (this.mButtons.isEmpty()) {
            return;
        }
        COUISpringAnimation cOUISpringAnimation = this.mSpringAnimationPos;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            this.mSpringAnimationPos.cancel();
        }
        COUISpringAnimation cOUISpringAnimation2 = this.mSpringAnimationRubber;
        if (cOUISpringAnimation2 != null && cOUISpringAnimation2.isRunning()) {
            this.mSpringAnimationRubber.cancel();
        }
        COUISpringAnimation cOUISpringAnimation3 = this.mSpringAnimationWidth;
        if (cOUISpringAnimation3 != null && cOUISpringAnimation3.isRunning()) {
            this.mSpringAnimationWidth.cancel();
        }
        COUISpringAnimation cOUISpringAnimation4 = this.mSpringAnimationHeight;
        if (cOUISpringAnimation4 != null && cOUISpringAnimation4.isRunning()) {
            this.mSpringAnimationHeight.cancel();
        }
        this.mIndicatorRectF.right = this.mButtons.get(this.mSelectedPosition).getRight();
        this.mIndicatorRectF.left = this.mButtons.get(this.mSelectedPosition).getLeft();
        this.mIndicatorRectF.top = getPaddingTop();
        this.mIndicatorRectF.bottom = getHeight() - getPaddingBottom();
        updatePath(this.mIndicatorCornerHelper, this.mIndicatorRectF);
        this.mIndicatorHeight = this.mIndicatorRectF.height();
        this.mIndicatorCenterY = this.mIndicatorRectF.centerY();
        this.mIndicatorCenterX = this.mIndicatorRectF.centerX();
        this.mIndicatorWidth = this.mIndicatorRectF.width();
        this.mMinSegmentHeight = this.mIndicatorRectF.height() * 0.9f;
    }

    private void resetRubberBandEffect(int i3) {
        TextView segmentAt = getSegmentAt(i3);
        if (segmentAt != null) {
            float left = (segmentAt.getLeft() + segmentAt.getRight()) / 2.0f;
            this.mSpringAnimationRubber.setStartValue(this.mIndicatorCenterX);
            this.mSpringAnimationRubber.animateToFinalPosition(left);
        } else {
            COUILog.w(TAG, i3 + " out of range of segment button layout");
        }
    }

    private void resetTouchEffect() {
        ensureSpringAnimation();
        int i3 = this.mLastTouchPosition;
        if (i3 < 0) {
            i3 = this.mSelectedPosition;
        }
        executeScaleAnimatorAt(false, i3);
        if (this.mIsDragging) {
            this.mIndicatorScaleHelper.executeFeedbackAnimator(false);
            if (this.mSpringAnimationPos.isRunning()) {
                return;
            }
            resetRubberBandEffect(this.mSelectedPosition);
        }
    }

    private void setSelectedAt(int i3, boolean z2) {
        if (i3 < 0 || i3 >= this.mButtons.size()) {
            return;
        }
        TextView textView = this.mButtons.get(i3);
        textView.setSelected(z2);
        textView.setTextColor(z2 ? this.mSelectedTextColor : this.mUnselectedTextColor);
    }

    private void updateAllButtonsPadding() {
        int i3 = this.mCustomButtonPaddingHorizontal;
        if (i3 < 0) {
            i3 = this.mSegmentPaddingHorizontal;
        }
        Iterator<TextView> it = this.mButtons.iterator();
        while (it.hasNext()) {
            ViewCompat.setPaddingRelative(it.next(), i3, 0, i3, 0);
        }
        this.mIsSizeChanged = true;
    }

    private void updateBackgroundShadowEdgeParams(Context context) {
        COUIMaterialStrokeEffect.StrokeEffectType strokeEffectType;
        if (context == null) {
            context = getContext();
        }
        if (this.mBackgroundShadowEdgeDrawable == null || !COUIMaterialEffect.isMaterialVersion() || (strokeEffectType = this.mBackgroundStrokeType) == null || context == null) {
            return;
        }
        COUIMaterialStrokeEffect.Companion.StrokeEffectParams strokeParamsByType = COUIMaterialEffect.getStrokeParamsByType(context, strokeEffectType);
        this.mBackgroundShadowEdgeDrawable.setLineWidth(strokeParamsByType.getEdgeWidth(context));
        this.mBackgroundShadowEdgeDrawable.setLineAlpha(strokeParamsByType.getEdgeAlpha());
        this.mBackgroundShadowEdgeDrawable.setLineAngle(strokeParamsByType.getEdgeAngle());
        this.mBackgroundShadowEdgeDrawable.setFadeAlpha(strokeParamsByType.getShadowFadeIn(), strokeParamsByType.getShadowFadeOut());
        this.mBackgroundShadowEdgeDrawable.setFadeWidthScale(strokeParamsByType.getShadowFadeScale());
        this.mBackgroundShadowEdgeDrawable.setIsDarkMode(this.mIsNightMode);
    }

    private void updateIndicatorRect() {
        RectF rectF = this.mIndicatorRectF;
        float f3 = this.mIndicatorCenterX;
        float f4 = this.mIndicatorScale;
        float f5 = this.mIndicatorWidth;
        rectF.left = f3 - ((f4 * f5) / 2.0f);
        rectF.right = f3 + ((f5 * f4) / 2.0f);
        float f6 = this.mIndicatorCenterY;
        float f7 = this.mIndicatorHeight;
        rectF.top = f6 - ((f4 * f7) / 2.0f);
        rectF.bottom = f6 + ((f4 * f7) / 2.0f);
        clampIndicatorRectToContainer();
        updatePath(this.mIndicatorCornerHelper, this.mIndicatorRectF);
        this.mOverDistance = (int) (((1.0f - this.mIndicatorScale) * this.mIndicatorWidth) / 2.0f);
        int i3 = this.mSelectedPosition;
        if (i3 == 0 || i3 == this.mButtons.size() - 1) {
            this.mOverDistance += getPaddingStart();
        } else {
            this.mOverDistance += this.mSwitchDistance;
        }
        int i4 = this.mSelectedPosition;
        if (i4 == 0 || i4 == this.mButtons.size() - 1) {
            this.mOverDistance = Math.min(this.mOverDistance, ((int) (((1.0f - this.mIndicatorScale) * this.mIndicatorWidth) / 2.0f)) + Math.max(0, getPaddingStart() - this.mMaterialRubberMaxDistance));
        }
        invalidate();
    }

    private void updateMaterialEffectParams() {
        if (isMaterialEffectSupported()) {
            int width = getWidth();
            int height = getHeight();
            if (width <= 0 || height <= 0) {
                return;
            }
            if (width == this.mLastMaterialUpdateWidth && height == this.mLastMaterialUpdateHeight) {
                COUILog.d(TAG, "[MaterialEffect] Params update skipped: size unchanged " + width + "x" + height);
                return;
            }
            float f3 = height / 2.0f;
            OplusMaterialCornerParams oplusMaterialCornerParams = this.mCornerParams;
            if (oplusMaterialCornerParams != null) {
                oplusMaterialCornerParams.setRadius(f3);
                this.mCornerParams.setWeight(1.0f);
                if (!this.mDrawMaterialEffect) {
                    COUIMaterialEffect.setCornerParams(this, this.mCornerParams);
                }
            }
            this.mLastMaterialUpdateWidth = width;
            this.mLastMaterialUpdateHeight = height;
            applyInnerShadowStrokeParams();
            applyShadow();
            applyViewBlurEffect();
            COUILog.d(TAG, "[MaterialEffect] Params updated for new size: " + width + "x" + height);
        }
    }

    private void updateMaterialRubberMaxDistance() {
        this.mMaterialRubberMaxDistance = isSmallPadding() ? this.mSegmentPaddingSmall : this.mSegmentPaddingRegular;
    }

    private void updatePath(SmoothRoundCornerHelper smoothRoundCornerHelper, RectF rectF) {
        float drawableRadius = getDrawableRadius(rectF);
        int smoothType = smoothRoundCornerHelper.getSmoothType();
        if (smoothType == 0) {
            smoothRoundCornerHelper.updatePath(rectF, drawableRadius);
            return;
        }
        if (smoothType != 1) {
            Path path = smoothRoundCornerHelper.getPath();
            path.reset();
            path.addRoundRect(rectF, drawableRadius, drawableRadius, Path.Direction.CCW);
        } else {
            OplusPathAdapter pathAdapter = smoothRoundCornerHelper.getPathAdapter();
            smoothRoundCornerHelper.getPath().reset();
            pathAdapter.addSmoothRoundRect(rectF.left, rectF.top, rectF.right, rectF.bottom, drawableRadius, drawableRadius, Path.Direction.CCW);
        }
    }

    private void updateShadowGradientColors() {
        int[] iArr = this.mShadowGradientColors;
        iArr[0] = this.mShadowColor;
        iArr[1] = this.mShadowGradientEndColor;
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i3, ViewGroup.LayoutParams layoutParams) {
        if (!(view instanceof TextView)) {
            COUILog.e(TAG, "Child views must be of type TextView.");
            return;
        }
        TextView textView = (TextView) view;
        initializeSegmentButton(textView, this.mButtons.size(), textView.getText().toString());
        this.mButtons.add(textView);
        super.addView(textView, -1, new ViewGroup.LayoutParams(-2, -1));
        this.mIsSizeChanged = true;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        int segmentIndex;
        if (!isEnabled()) {
            return false;
        }
        int keyCode = keyEvent.getKeyCode();
        if ((keyCode == 23 || keyCode == 66) && keyEvent.getAction() == 1 && (getFocusedChild() instanceof TextView) && (segmentIndex = getSegmentIndex((TextView) getFocusedChild())) != this.mSelectedPosition) {
            selectSegmentAt(segmentIndex);
        }
        return super.dispatchKeyEvent(keyEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        COUIViewTouchAnimator cOUIViewTouchAnimator;
        if (!isEnabled()) {
            return false;
        }
        if (this.mDragAnimationEnabled && (cOUIViewTouchAnimator = this.mTouchAnimator) != null) {
            cOUIViewTouchAnimator.handleTouchEvent(motionEvent);
        }
        this.mLastTouchPosition = this.mTouchPosition;
        this.mTouchPosition = getSelectedIndexFromX(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            ensureSpringAnimation();
            executeScaleAnimatorAt(true, this.mTouchPosition);
            if (!this.mIsDragging) {
                boolean z2 = this.mSelectedPosition == this.mTouchPosition || isTouchInIndicator(motionEvent.getX(), motionEvent.getY());
                this.mIsDragging = z2;
                if (z2) {
                    this.mLastX = motionEvent.getX();
                    this.mIndicatorCenterLastX = this.mIndicatorCenterX;
                    this.mIndicatorScaleHelper.executeFeedbackAnimator(true);
                }
            }
        } else if (actionMasked == 1) {
            resetTouchEffect();
            this.mIsDragging = false;
            int i3 = this.mTouchPosition;
            if (i3 != this.mSelectedPosition && i3 >= 0) {
                selectSegmentAt(i3);
            }
        } else if (actionMasked != 2) {
            if (actionMasked == 3) {
                handleActionCancel();
            }
        } else if (this.mIsDragging) {
            if (Math.abs(motionEvent.getX() - this.mLastX) > this.mTouchSlop) {
                attemptClaimDrag();
            }
            int i4 = this.mTouchPosition;
            if (i4 != this.mSelectedPosition && i4 >= 0) {
                selectSegmentAt(i4);
                performHapticFeedback();
                executeScaleAnimatorAt(false, this.mLastSelectedPosition);
                executeScaleAnimatorAt(true, this.mSelectedPosition);
            } else if (!this.mSpringAnimationPos.isRunning()) {
                handleRubberBandEffect(motionEvent.getX());
            }
        } else {
            int i5 = this.mTouchPosition;
            int i6 = this.mLastTouchPosition;
            if (i5 != i6) {
                if (i6 < 0) {
                    i6 = this.mLastSelectedPosition;
                }
                executeScaleAnimatorAt(false, i6);
            }
        }
        handleSpotLight(motionEvent);
        return super.dispatchTouchEvent(motionEvent);
    }

    public TextView getSegmentAt(int i3) {
        if (i3 < 0 || i3 >= getSegmentCount()) {
            return null;
        }
        return this.mButtons.get(i3);
    }

    public int getSegmentCount() {
        return this.mButtons.size();
    }

    public int getSegmentIndex(View view) {
        return this.mButtons.indexOf(view);
    }

    public int getSelectedSegmentPosition() {
        return this.mSelectedPosition;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLightDrawable;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setCallback(this);
        }
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable;
        if (cOUIShadowEdgeDrawable != null) {
            cOUIShadowEdgeDrawable.setCallback(this);
        }
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable2 = this.mBackgroundShadowEdgeDrawable;
        if (cOUIShadowEdgeDrawable2 != null) {
            cOUIShadowEdgeDrawable2.setCallback(this);
        }
        COUIMaterialStateManager.INSTANCE.addOnMaterialStateChangedListener(this);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLightDrawable;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setCallback(null);
            this.mSpotLightDrawable.cancel();
        }
        COUIViewTouchAnimator cOUIViewTouchAnimator = this.mTouchAnimator;
        if (cOUIViewTouchAnimator != null) {
            cOUIViewTouchAnimator.reset();
        }
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable;
        if (cOUIShadowEdgeDrawable != null) {
            cOUIShadowEdgeDrawable.setCallback(null);
        }
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable2 = this.mBackgroundShadowEdgeDrawable;
        if (cOUIShadowEdgeDrawable2 != null) {
            cOUIShadowEdgeDrawable2.setCallback(null);
        }
        COUIMaterialStateManager.INSTANCE.removeOnMaterialStateChangedListener(this);
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onDraw(Canvas canvas) {
        Paint[] customBackgroundPaint;
        Paint[] customIndicatorPaint;
        int iSave = canvas.save();
        canvas.translate(getScrollX(), getScrollY());
        SegmentButtonDrawDelegate segmentButtonDrawDelegate = this.mDrawDelegate;
        if (segmentButtonDrawDelegate != null) {
            customBackgroundPaint = segmentButtonDrawDelegate.getCustomBackgroundPaint();
            customIndicatorPaint = this.mDrawDelegate.getCustomIndicatorPaint();
        } else {
            customBackgroundPaint = null;
            customIndicatorPaint = null;
        }
        if (!this.mMaterialEffectEnabled || !this.mBlurEffectEnabled || this.mDrawMaterialEffect) {
            if (customBackgroundPaint == null || customBackgroundPaint.length == 0) {
                if (this.mDrawMaterialEffect) {
                    this.mBackgroundColor = COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(getContext()) ? BLUR_WINDOW_VIEW_BG_COLOR_DARK : BLUR_WINDOW_VIEW_BG_COLOR_LIGHT;
                }
                this.mContainerPaint.setColor(this.mBackgroundColor);
                drawButtonBackground(canvas, this.mContainerPaint);
            } else {
                for (Paint paint : customBackgroundPaint) {
                    if (paint != null) {
                        drawButtonBackground(canvas, paint);
                    }
                }
            }
        }
        drawBackgroundShadowEdge(canvas);
        if (customIndicatorPaint == null || customIndicatorPaint.length == 0) {
            drawIndicator(canvas, Boolean.TRUE, this.mIndicatorPaint);
            this.mIndicatorPaint.setColor(this.mIndicatorColor);
            drawIndicator(canvas, Boolean.FALSE, this.mIndicatorPaint);
        } else {
            for (Paint paint2 : customIndicatorPaint) {
                if (paint2 != null) {
                    drawIndicator(canvas, Boolean.FALSE, paint2);
                }
            }
        }
        drawIndicatorShadowEdge(canvas);
        canvas.restoreToCount(iSave);
        super.onDraw(canvas);
        drawSpotLight(canvas);
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public CharSequence onDump() {
        return "COUISegmentButtonLayout[materialEffectEnabled=" + this.mMaterialEffectEnabled + ", innerShadowStrokeEnabled=" + this.mInnerShadowStrokeEnabled + ", viewBlurEffectEnabled=" + this.mViewBlurEffectEnabled + ", spotLightEnabled=" + this.mSpotLightEnabled + "]";
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        AccessibilityNodeInfoCompat.wrap(accessibilityNodeInfo).setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(1, getSegmentCount(), false, 0));
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        super.onLayout(z2, i3, i4, i5, i6);
        this.mTmpRect.right = getWidth();
        this.mTmpRect.bottom = getHeight();
        this.mTmpRectF.set(this.mTmpRect);
        updatePath(this.mSmoothRoundCornerHelper, this.mTmpRectF);
        if (this.mIsSizeChanged) {
            resetIndicatorRect();
            this.mIsSizeChanged = false;
        }
        if (z2 && this.mSpotLightDrawable != null) {
            int width = getWidth();
            int height = getHeight();
            if (width != this.mLastSpotLightWidth || height != this.mLastSpotLightHeight) {
                this.mLastSpotLightWidth = width;
                this.mLastSpotLightHeight = height;
                this.mSpotLightDrawable.setResolution(width, height);
            }
        }
        if (this.mMaterialEffectEnabled && z2) {
            updateMaterialEffectParams();
        }
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public void onMaterialStateChange(int i3, boolean z2) {
        if (i3 == 1) {
            COUILog.d(TAG, "[MaterialEffect] Stroke effect state changed: " + z2);
            this.mStrokeEffectEnabled = z2;
            if (this.mMaterialEffectEnabled && isMaterialEffectSupported()) {
                applyInnerShadowStrokeParams();
            }
            invalidate();
            return;
        }
        if (i3 == 2) {
            COUILog.d(TAG, "[MaterialEffect] Blur effect state changed: " + z2);
            this.mBlurEffectEnabled = z2;
            if (this.mMaterialEffectEnabled && isMaterialEffectSupported()) {
                applyViewBlurEffect();
            }
            invalidate();
            return;
        }
        if (i3 == 4) {
            COUILog.d(TAG, "[MaterialEffect] Spotlight effect state changed: " + z2);
            this.mSpotlightEffectEnabled = z2;
            return;
        }
        if (i3 == 8) {
            COUILog.d(TAG, "[MaterialEffect] Caustic shadow effect state changed: " + z2);
            this.mCausticShadowEffectEnabled = z2;
            applyShadow();
        }
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        int mode = View.MeasureSpec.getMode(i3);
        int size = View.MeasureSpec.getSize(i3);
        this.mChildWidths.clear();
        int i5 = 0;
        if (size > 0) {
            int i6 = 0;
            for (int i7 = 0; i7 < getChildCount(); i7++) {
                View childAt = getChildAt(i7);
                if (childAt instanceof TextView) {
                    ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                    int segmentTextWidth = getSegmentTextWidth((TextView) childAt);
                    layoutParams.width = segmentTextWidth;
                    this.mChildWidths.add(Integer.valueOf(segmentTextWidth));
                    i6 += layoutParams.width;
                }
            }
            int paddingStart = i6 + getPaddingStart() + getPaddingEnd();
            if (mode == 1073741824) {
                if (this.mIsResponsiveWidthEnabled) {
                    size = Math.min(getResponsiveWidth(), size);
                }
                calculateChildrenWidths((size - getPaddingStart()) - getPaddingEnd());
                while (i5 < getChildCount()) {
                    getChildAt(i5).getLayoutParams().width = this.mChildWidths.get(i5).intValue();
                    i5++;
                }
            } else if (paddingStart > size) {
                calculateChildrenWidths((size - getPaddingStart()) - getPaddingEnd());
                while (i5 < getChildCount()) {
                    getChildAt(i5).getLayoutParams().width = this.mChildWidths.get(i5).intValue();
                    i5++;
                }
            } else {
                size = paddingStart;
            }
        } else {
            for (int i8 = 0; i8 < getChildCount(); i8++) {
                getChildAt(i8).getLayoutParams().width = 0;
            }
        }
        super.onMeasure(i3, i4);
        int size2 = View.MeasureSpec.getSize(i4);
        float f3 = this.mCustomHeightPx;
        if (f3 > 0.0f) {
            size2 = Math.max((int) f3, this.mMinHeightPx);
        }
        setMeasuredDimension(size, size2);
    }

    @Override // com.coui.appcompat.segmentbutton.ISegmentButtonLayout
    public void onPageScrollStateChanged(int i3) {
    }

    @Override // com.coui.appcompat.segmentbutton.ISegmentButtonLayout
    public void onPageScrolled(int i3, float f3, int i4) {
    }

    @Override // com.coui.appcompat.segmentbutton.ISegmentButtonLayout
    public void onPageSelected(int i3) {
        if (i3 < 0 || i3 >= this.mButtons.size() || i3 == this.mSelectedPosition) {
            return;
        }
        selectSegmentAt(i3);
    }

    @Override // android.view.View
    public void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        this.mIsSizeChanged = true;
        COUIViewTouchAnimator cOUIViewTouchAnimator = this.mTouchAnimator;
        if (cOUIViewTouchAnimator != null) {
            cOUIViewTouchAnimator.onSizeChange(i3, i4);
        }
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLightDrawable;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setBounds(0, 0, i3, i4);
        }
    }

    @Override // android.view.ViewGroup
    public void onViewRemoved(View view) {
        if (!(view instanceof TextView)) {
            COUILog.e(TAG, "Child views must be of type TextView.");
            return;
        }
        this.mSelectedPosition = 0;
        this.mLastSelectedPosition = -1;
        int segmentIndex = getSegmentIndex((TextView) view);
        if (segmentIndex == -1) {
            COUILog.e(TAG, "Child view is not a direct child of this ViewGroup: " + view + "\nParent: " + this + "\nMake sure the view was properly added with addView()");
            return;
        }
        this.mButtons.remove(segmentIndex);
        this.mPressScaleHelper.remove(segmentIndex);
        this.mTextColorChangeHelper.remove(segmentIndex);
        resetAnimation(this.mSpringAnimationPos);
        resetAnimation(this.mSpringAnimationWidth);
        resetAnimation(this.mSpringAnimationHeight);
        resetAnimation(this.mSpringAnimationRubber);
        this.mSpringAnimationPos = null;
        this.mSpringAnimationWidth = null;
        this.mSpringAnimationHeight = null;
        this.mSpringAnimationRubber = null;
        this.mIndicatorScaleHelper = null;
    }

    public void refresh() {
        int i3;
        int i4;
        String resourceTypeName = getContext().getResources().getResourceTypeName(this.mStyleAttr);
        if (TextUtils.equals(resourceTypeName, "attr")) {
            i3 = this.mStyleAttr;
            i4 = 0;
        } else if (TextUtils.equals(resourceTypeName, BaseConfig.KEY_STYLE)) {
            i4 = this.mStyleAttr;
            i3 = 0;
        } else {
            i3 = 0;
            i4 = 0;
        }
        loadAttr(getContext(), null, i3, i4);
        updateShadowGradientColors();
        this.mIsNightMode = COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(getContext());
        setStrokeEffectType(this.mBackgroundStrokeType, getContext());
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable;
        if (cOUIShadowEdgeDrawable != null) {
            cOUIShadowEdgeDrawable.setIsDarkMode(this.mIsNightMode);
            COUILog.d(TAG, "Indicator shadow edge dark mode updated: " + this.mIsNightMode);
        }
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable2 = this.mBackgroundShadowEdgeDrawable;
        if (cOUIShadowEdgeDrawable2 != null) {
            cOUIShadowEdgeDrawable2.setIsDarkMode(this.mIsNightMode);
        }
        if (this.mViewBlurEffectEnabled && this.mMaterialEffectEnabled && isMaterialEffectSupported()) {
            applyViewBlurEffect();
        }
        for (int i5 = 0; i5 < this.mButtons.size(); i5++) {
            refreshSegmentTextConfig(this.mButtons.get(i5), i5);
        }
    }

    public void resetSegmentPadding() {
        if (this.mCustomPaddingType != -1) {
            this.mCustomPaddingType = -1;
            applyCustomPadding();
            requestLayout();
        }
    }

    public void selectSegmentAt(int i3) {
        int i4;
        if (i3 < 0 || i3 >= this.mButtons.size() || i3 == (i4 = this.mSelectedPosition)) {
            return;
        }
        this.mLastSelectedPosition = i4;
        this.mSelectedPosition = i3;
        setSelectedAt(i3, true);
        setSelectedAt(this.mLastSelectedPosition, false);
        if (this.mSelectedTextColor != this.mUnselectedTextColor) {
            executeTextColorChangeAnimatorAt(true, this.mSelectedPosition);
            executeTextColorChangeAnimatorAt(false, this.mLastSelectedPosition);
        }
        animateIndicatorToPosition(this.mSelectedPosition);
    }

    public void setCausticShadowLevel(int i3) {
        if (i3 == 6 || i3 == 7) {
            this.mCausticShadowLevel = i3;
        } else {
            this.mCausticShadowLevel = -1;
        }
        applyShadow();
    }

    public void setDragAnimationEnabled(boolean z2) {
        this.mDragAnimationEnabled = z2;
        COUILog.d(TAG, "[AnimationEffect] Drag animation enabled: " + z2);
    }

    @Override // android.view.View
    public void setEnabled(boolean z2) {
        if (isEnabled() != z2 && !z2) {
            handleActionCancel();
        }
        super.setEnabled(z2);
    }

    @Deprecated
    public void setIndicatorResponse(float f3) {
    }

    public void setInnerShadowStrokeEnabled(boolean z2) {
        this.mInnerShadowStrokeEnabled = z2;
        if (this.mMaterialEffectEnabled) {
            applyInnerShadowStrokeParams();
            invalidate();
        }
    }

    public void setIsDrawMaterialEffect(boolean z2) {
        if (isMaterialEffectSupported()) {
            this.mDrawMaterialEffect = z2;
            if (z2) {
                if (this.mBackgroundShadowEdgeDrawable == null) {
                    initBackgroundShadowEdgeDrawable();
                }
                clearViewBlurEffect();
                clearViewStrokeEffect();
            } else {
                applyMaterialEffect();
            }
            invalidate();
        }
    }

    public void setMaterialEffectEnabled(boolean z2) {
        if (!isMaterialEffectSupported()) {
            COUILog.w(TAG, "[MaterialEffect] Material effect enabled but not supported, disabling all effects");
            return;
        }
        this.mMaterialEffectEnabled = z2;
        this.mSpotLightEnabled = z2;
        this.mInnerShadowStrokeEnabled = z2;
        this.mViewBlurEffectEnabled = z2;
        if (z2) {
            applyMaterialEffect();
        } else {
            removeMaterialEffect();
        }
    }

    public void setOnSegmentSelectedListener(OnSegmentSelectedListener onSegmentSelectedListener) {
        this.mOnSegmentSelectedListener = onSegmentSelectedListener;
    }

    public void setOnSelectedSegmentChangeListener(OnSelectedSegmentChangeListener onSelectedSegmentChangeListener) {
        this.mOnSelectedSegmentChangeListener = onSelectedSegmentChangeListener;
    }

    public void setOuterShadowLevel(int i3) {
        this.mOuterShadowLevel = i3;
        applyShadow();
    }

    public void setResponsiveWidthEnabled(boolean z2) {
        this.mIsResponsiveWidthEnabled = z2;
        requestLayout();
    }

    public void setSegmentButtonDrawDelegate(SegmentButtonDrawDelegate segmentButtonDrawDelegate) {
        this.mDrawDelegate = segmentButtonDrawDelegate;
    }

    public void setSegmentButtons(String[] strArr) {
        if (Arrays.equals(this.mSegmentStrings, strArr)) {
            return;
        }
        this.mSegmentStrings = strArr;
        removeAllViews();
        for (int i3 = 0; i3 < this.mSegmentStrings.length; i3++) {
            TextView textView = new TextView(getContext());
            textView.setText(strArr[i3]);
            addView(textView, new ViewGroup.LayoutParams(-2, -1));
        }
    }

    public void setSegmentHeight(float f3) {
        float fApplyDimension = TypedValue.applyDimension(1, f3, getResources().getDisplayMetrics());
        if (this.mCustomHeightPx != fApplyDimension) {
            this.mCustomHeightPx = fApplyDimension;
            requestLayout();
        }
    }

    public void setSegmentPadding(int i3) {
        if (i3 == 0 || i3 == 1) {
            if (this.mCustomPaddingType != i3) {
                this.mCustomPaddingType = i3;
                applyCustomPadding();
                requestLayout();
                return;
            }
            return;
        }
        COUILog.w(TAG, "Invalid padding type: " + i3 + ". Use PADDING_SMALL or PADDING_LARGE.");
    }

    public void setSegmentSelectedTextColor(int i3) {
        if (this.mButtons.isEmpty()) {
            COUILog.e(TAG, "Cannot set text color: COUISegmentButtonLayout has no children.");
        } else {
            this.mSelectedTextColor = i3;
            this.mButtons.get(this.mSelectedPosition).setTextColor(this.mSelectedTextColor);
        }
    }

    public void setSegmentTextAt(String str, int i3) {
        TextView segmentAt = getSegmentAt(i3);
        if (segmentAt != null) {
            segmentAt.setText(str);
            this.mIsSizeChanged = true;
            requestLayout();
        } else {
            COUILog.w(TAG, "Segment at index " + i3 + "does not exist");
        }
    }

    public void setSegmentTextSize(float f3) {
        setSegmentTextSize(2, f3);
    }

    public void setSegmentUnselectedTextColor(int i3) {
        this.mUnselectedTextColor = i3;
        for (int i4 = 0; i4 < this.mButtons.size(); i4++) {
            if (i4 != this.mSelectedPosition) {
                this.mButtons.get(i4).setTextColor(this.mUnselectedTextColor);
            }
        }
    }

    public void setSpotLightEffectType(COUISpotLightEffect.SpotLightType spotLightType, Context context) {
        if (spotLightType == null) {
            this.mSpotLightType = COUISpotLightEffect.SpotLightType.TYPE_CAPSULE_2;
        } else {
            this.mSpotLightType = spotLightType;
        }
        if (context == null) {
            context = getContext();
        }
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLightDrawable;
        if (cOUISpotLightEffectDrawable == null || context == null) {
            return;
        }
        cOUISpotLightEffectDrawable.setSpotLightType(context, this.mSpotLightType);
    }

    public void setSpotLightEnabled(boolean z2) {
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable;
        this.mSpotLightEnabled = z2;
        if (!z2 && (cOUISpotLightEffectDrawable = this.mSpotLightDrawable) != null) {
            cOUISpotLightEffectDrawable.extinguish();
            invalidate();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[MaterialEffect] SpotLight user switch: ");
        sb.append(z2);
        sb.append(", drawable initialized: ");
        sb.append(this.mSpotLightDrawable != null);
        sb.append(", blur hw enabled: ");
        sb.append(this.mBlurEffectEnabled);
        COUILog.d(TAG, sb.toString());
    }

    public void setStrokeEffectType(COUIMaterialStrokeEffect.StrokeEffectType strokeEffectType, Context context) {
        if (strokeEffectType == null) {
            this.mBackgroundStrokeType = COUIMaterialStrokeEffect.StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_11;
        } else {
            this.mBackgroundStrokeType = strokeEffectType;
        }
        updateBackgroundShadowEdgeParams(context);
    }

    public void setTextScaleEnabled(boolean z2) {
        this.mEnableTextScale = z2;
        requestLayout();
    }

    public void setViewBlurEffectEnabled(boolean z2) {
        this.mViewBlurEffectEnabled = z2;
        if (this.mMaterialEffectEnabled) {
            applyViewBlurEffect();
            invalidate();
        }
    }

    public COUISegmentButtonLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, com.support.segmentbutton.R.attr.couiSegmentButtonStyle);
    }

    public void setSegmentTextSize(int i3, float f3) {
        if (f3 == this.mTextPaint.getTextSize()) {
            return;
        }
        this.mCustomTextSizePx = TypedValue.applyDimension(i3, f3, getResources().getDisplayMetrics());
        for (int i4 = 0; i4 < this.mButtons.size(); i4++) {
            this.mButtons.get(i4).setTextSize(i3, f3);
            if (this.mEnableTextScale) {
                COUIChangeTextUtil.adaptFontSize(this.mButtons.get(i4), 4);
            }
        }
        refreshTextPaint();
        this.mIsSizeChanged = true;
    }

    public COUISegmentButtonLayout(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, com.support.segmentbutton.R.style.SegmentButton);
    }

    public COUISegmentButtonLayout(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mContainerPaint = new Paint(1);
        this.mIndicatorPaint = new Paint(1);
        this.mShadowGradientPaint = new Paint(1);
        this.mShadowGradientColors = new int[2];
        this.mShadowGradientStops = new float[]{0.0f, 1.0f};
        this.mTmpRect = new Rect();
        this.mTmpRectF = new RectF();
        this.mIndicatorRectF = new RectF();
        this.mShadowRectF = new RectF();
        Path path = new Path();
        this.mBackgroundPath = path;
        Path path2 = new Path();
        this.mIndicatorPath = path2;
        this.mResponsiveUIModel = new ResponsiveUIModel(getContext(), 0, 0);
        this.mPressScaleHelper = new ArrayList<>();
        this.mTextColorChangeHelper = new ArrayList<>();
        this.mButtons = new ArrayList<>();
        this.mChildWidths = new ArrayList<>();
        this.mTextView = new TextView(getContext());
        this.mShadowEdgeClipRect = new RectF();
        this.mCustomButtonPaddingHorizontal = -1;
        this.mTextPaint = null;
        this.mCustomHeightPx = 0.0f;
        this.mCustomPaddingType = -1;
        this.mInitialPaddingLeft = 0;
        this.mInitialPaddingTop = 0;
        this.mInitialPaddingRight = 0;
        this.mInitialPaddingBottom = 0;
        this.mOnSelectedSegmentChangeListener = null;
        this.mOnSegmentSelectedListener = null;
        this.mDrawDelegate = null;
        this.mSmoothRoundCornerHelper = null;
        this.mIndicatorCornerHelper = null;
        this.mShadowEdgeHelper = null;
        this.mIndicatorScale = 1.0f;
        this.mIsDragging = false;
        this.mEnableRubberEffect = false;
        this.mIsNeedVibration = true;
        this.mIsSizeChanged = false;
        this.mIsResponsiveWidthEnabled = true;
        this.mDrawMaterialEffect = false;
        this.mTouchPosition = -1;
        this.mLastTouchPosition = -1;
        this.mSelectedPosition = 0;
        this.mLastSelectedPosition = -1;
        this.mAttrs = null;
        this.mStyleAttr = 0;
        this.mStyleRes = 0;
        this.mTouchSlop = 0;
        this.mMinSegmentHeight = 0.0f;
        this.mTouchAnimator = null;
        this.mDragAnimationEnabled = true;
        this.mMaterialEffectEnabled = false;
        this.mOuterShadowLevel = -1;
        this.mCausticShadowLevel = -1;
        this.mSpotLightDrawable = null;
        this.mSpotLightEnabled = false;
        this.mInnerShadowStrokeEnabled = false;
        this.mViewBlurEffectEnabled = false;
        this.mStrokeEffectEnabled = false;
        this.mBlurEffectEnabled = false;
        this.mSpotlightEffectEnabled = false;
        this.mCausticShadowEffectEnabled = false;
        this.mShadowEdgeDrawable = null;
        this.mBackgroundShadowEdgeDrawable = null;
        this.mLastSpotLightWidth = -1;
        this.mLastSpotLightHeight = -1;
        this.mLastMaterialUpdateWidth = -1;
        this.mLastMaterialUpdateHeight = -1;
        this.mLastShadowEdgeRadius = -1.0f;
        this.mLastShadowEdgeWidth = -1.0f;
        this.mLastShadowEdgeHeight = -1.0f;
        this.mBackgroundStrokeType = null;
        this.mSpotLightType = null;
        COUIDarkModeUtil.setForceDarkAllow(this, false);
        this.mAttrs = attributeSet;
        if (attributeSet != null) {
            this.mStyleAttr = attributeSet.getStyleAttribute();
        }
        if (this.mStyleAttr == 0) {
            this.mStyleAttr = i3;
        }
        this.mStyleRes = i4;
        loadAttr(context, attributeSet, i3, i4);
        this.mSegmentMinWidth = getResources().getDimensionPixelOffset(com.support.segmentbutton.R.dimen.coui_segment_min_width);
        this.mOverStiffness = 0.12f;
        this.mOverDistance = getResources().getDimensionPixelOffset(com.support.segmentbutton.R.dimen.coui_segment_over_distance);
        this.mSwitchDistance = getResources().getDimensionPixelOffset(com.support.segmentbutton.R.dimen.coui_segment_switch_distance);
        this.mSegmentPaddingHorizontal = getResources().getDimensionPixelOffset(com.support.segmentbutton.R.dimen.coui_segment_btn_padding_horizontal);
        this.mSegmentPaddingRegular = getResources().getDimensionPixelOffset(com.support.segmentbutton.R.dimen.coui_segment_padding);
        this.mSegmentPaddingSmall = getResources().getDimensionPixelOffset(com.support.segmentbutton.R.dimen.coui_segment_padding_tiny);
        this.mSegmentExtraWidth = getResources().getDimensionPixelOffset(com.support.segmentbutton.R.dimen.coui_segment_regular_extra_width);
        this.mSegmentExtraWidthSmall = getResources().getDimensionPixelOffset(com.support.segmentbutton.R.dimen.coui_segment_small_extra_width);
        this.mIndicatorShadowOffset = getResources().getDimensionPixelOffset(com.support.segmentbutton.R.dimen.coui_segment_shadow_offset);
        this.mShadowRadius = getResources().getDimensionPixelOffset(com.support.segmentbutton.R.dimen.coui_segment_shadow_radius);
        this.mMinHeightPx = getResources().getDimensionPixelOffset(com.support.segmentbutton.R.dimen.coui_segment_min_height);
        if (COUIMaterialEffect.isMaterialVersion()) {
            this.mCornerParams = new OplusMaterialCornerParams(0.0f, 1.0f);
        }
        initPaint();
        setWillNotDraw(false);
        setGravity(16);
        setOrientation(0);
        setBackgroundColor(0);
        setClipChildren(false);
        setClipToPadding(false);
        setImportantForAccessibility(1);
        this.mInitialPaddingLeft = getPaddingLeft();
        this.mInitialPaddingTop = getPaddingTop();
        this.mInitialPaddingRight = getPaddingRight();
        this.mInitialPaddingBottom = getPaddingBottom();
        updateMaterialRubberMaxDistance();
        this.mSmoothRoundCornerHelper = new SmoothRoundCornerHelper(path);
        this.mIndicatorCornerHelper = new SmoothRoundCornerHelper(path2);
        this.mShadowEdgeHelper = new SmoothRoundCornerHelper(new Path());
        this.mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        initTouchMotionAnimator();
        initSpotLight();
        initShadowEdgeDrawable();
    }

    public void selectSegmentAt(View view) {
        selectSegmentAt(getSegmentIndex(view));
    }
}