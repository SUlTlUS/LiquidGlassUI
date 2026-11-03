package com.coui.appcompat.searchview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.appcompat.view.CollapsibleActionView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import com.coui.appcompat.animation.COUIEaseInterpolator;
import com.coui.appcompat.animation.COUIMoveEaseInterpolator;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.roundRect.COUIShapePath;
import com.coui.appcompat.textutil.COUIChangeTextUtil;
import com.coui.appcompat.textviewcompatutil.COUITextViewCompatUtil;
import com.coui.appcompat.toolbar.COUIToolbar;
import com.decouple.injector.adaptor.BaseConfig;
import com.google.android.material.resources.MaterialResources;
import com.oplus.settings.appfunctions.SystemUpdateScreenStateConfig;
import com.support.toolbar.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class COUISearchViewAnimate extends LinearLayout implements CollapsibleActionView, ImeInsetsAnimationCallback.OnImeAnimationListener {
    private static final float CLAMP_ANIMATION_PERCENT = 0.3f;
    private static final boolean DEBUG = true;
    private static final long DEFAULT_BUTTON_ALPHA_CHANGE_DURATION = 350;
    private static final long DEFAULT_BUTTON_MOVE_DURATION = 450;
    private static final long DEFAULT_FADE_DURATION = 150;
    private static final long DEFAULT_PRESS_FEEDBACK_DURATION = 150;
    private static final long DEFAULT_SEARCH_VIEW_OFFSET_CHANGE_DURATION = 450;
    private static final long DEFAULT_SEARCH_VIEW_SCALE_CHANGE_DURATION = 450;
    private static final float FLOAT_POINT_FIVE = 0.5f;
    private static final float FLOAT_TWO = 2.0f;
    private static final long NON_INSTANT_SEARCH_BUTTON_ALPHA_CHANGE_DURATION = 100;
    public static final int STATE_EDIT = 1;
    public static final int STATE_NORMAL = 0;
    private static final String TAG = "COUISearchViewAnimate";
    public static final int TYPE_INSTANT_SEARCH = 0;
    public static final int TYPE_NON_INSTANT_SEARCH = 1;
    private static final int WAY_AT_BEHIND = 1;
    private static final int WAY_AT_FRONT = 2;
    private static final int WAY_NONE = 0;
    private int mAddToToolbarWay;
    private volatile AnimatorHelper mAnimatorHelper;
    private OnStateChangeListener mAtToolbarFrontStateChangeListener;
    private int mBackgroundEndGap;
    private int mBackgroundStartGap;
    private ValueAnimator mButtonAlphaEnterAnimator;
    private ValueAnimator mButtonAlphaExitAnimator;
    private ImageView mButtonDivider;
    private final RectF mButtonHitRect;
    private final int[] mButtonLocation;
    private int mCancelButtonLargeStartMargin;
    private int mCancelButtonSmallStartMargin;
    private ImageView mCloseBtn;
    private int mCollapsedMinHeight;
    private float mCollapsingHeightPercent;
    private Context mContext;
    private int mCurrentBackgroundColor;
    private boolean mDivideBackground;
    private final ArgbEvaluator mEvaluator;
    private int mExtraY;
    private SearchFunctionalButton mFunctionalButton;
    private int mGravity;
    private int mGravityInToolBar;
    private boolean mHasAddedToToolbar;
    private int mHorizontalDividerColor;
    private int mInitSearchViewAnimateHeight;
    private int mInitSearchViewWrapperHeight;
    private boolean mInputMethodAnimationEnabled;
    private boolean mIsAtLeastR;
    private final int[] mLocation;
    private ImageView mMainIcon;
    private Rect mMainIconRect;
    private MenuItem mMenuItem;
    private boolean mNeedUpdateNormalRectPath;
    private boolean mNeedUpdatePressFeedbackRectPath;
    private int mNormalBackgroundColor;
    private final Path mNormalBackgroundPath;
    private RectF mNormalBackgroundRect;
    private final Paint mNormalPaint;
    private OnAnimationListener mOnAnimationListener;
    private List<OnCancelButtonClickListener> mOnCancelButtonClickListeners;
    private OnDispatchKeyEventPreImeListener mOnDispatchKeyEventPreImeListener;
    private List<OnStateChangeListener> mOnStateChangeListeners;
    private ObjectAnimator mPressFeedbackAnimator;
    private final Path mPressFeedbackBackgroundPath;
    private RectF mPressFeedbackBackgroundRect;
    private final Paint mPressFeedbackPaint;
    private boolean mPressed;
    private int mPressedBackgroundColor;
    private Rect mRect;
    private ImageView mSearchIcon;
    private boolean mSearchIconCanAnimate;
    private COUISearchView mSearchView;
    private ValueAnimator mSearchViewOffsetEnterAnimator;
    private ValueAnimator mSearchViewOffsetExitAnimator;
    private ValueAnimator mSearchViewScaleEnterAnimator;
    private ValueAnimator mSearchViewScaleExitAnimator;
    private int mSearchViewShrinkWidth;
    private AnimatorSet mSearchViewSmoothEnterAnimatorSet;
    private AnimatorSet mSearchViewSmoothExitAnimatorSet;
    private int mSearchViewType;
    private ConstraintLayout mSearchViewWrapper;
    private boolean mShouldClearFocus;
    private int mShowImeAnimDuration;
    private Interpolator mShowImeInterpolator;
    private int mStartY;
    public AtomicInteger mState;
    private int mStyle;
    private ImageView mSubIcon;
    private Rect mSubIconRect;
    private COUIToolbar mToolBar;
    private boolean mToolBarAnimationRunning;
    private int mTopOffset;
    private final Rect mWrapperBounds;
    private static final String[] TYPE_NAME = {"TYPE_INSTANT_SEARCH", "TYPE_NON_INSTANT_SEARCH"};
    private static final Interpolator DEFAULT_SEARCH_VIEW_OFFSET_CHANGE_INTERPOLATOR = new COUIMoveEaseInterpolator();
    private static final Interpolator DEFAULT_SEARCH_VIEW_SCALE_CHANGE_INTERPOLATOR = new COUIMoveEaseInterpolator();
    private static final Interpolator DEFAULT_BUTTON_ALPHA_CHANGE_INTERPOLATOR = new COUIMoveEaseInterpolator();
    private static final Interpolator DEFAULT_PRESS_FEEDBACK_INTERPOLATOR = new COUIEaseInterpolator();
    private static final ArgbEvaluator DEFAULT_PRESS_FEEDBACK_EVALUATOR = new ArgbEvaluator();

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$1, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass1 implements OnStateChangeListener {
        public AnonymousClass1() {
        }

        @Override // com.coui.appcompat.searchview.COUISearchViewAnimate.OnStateChangeListener
        public void onStateChange(int i3, int i4) {
            if (i4 == 1) {
                COUISearchViewAnimate.this.showInToolBar();
            } else if (i4 == 0) {
                COUISearchViewAnimate.this.hideInToolBar();
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$10, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass10 extends AnimatorListenerAdapter {
        public AnonymousClass10() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            COUISearchViewAnimate.access$1002(COUISearchViewAnimate.this, false);
            COUISearchViewAnimate.access$1200(COUISearchViewAnimate.this, 0);
            COUISearchViewAnimate.access$1300(COUISearchViewAnimate.this);
            COUISearchViewAnimate.access$300(COUISearchViewAnimate.this).setVisibility(4);
            COUISearchViewAnimate.access$1400(COUISearchViewAnimate.this).setVisibility(4);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            super.onAnimationStart(animator);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$11, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass11 implements Animator.AnimatorListener {
        public AnonymousClass11() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUISearchViewAnimate.access$3202(COUISearchViewAnimate.this, 0);
            COUISearchViewAnimate cOUISearchViewAnimate = COUISearchViewAnimate.this;
            COUISearchViewAnimate.access$3302(cOUISearchViewAnimate, cOUISearchViewAnimate.getTop());
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$12, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass12 implements Animator.AnimatorListener {
        public AnonymousClass12() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            COUISearchViewAnimate.access$3400(COUISearchViewAnimate.this);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUISearchViewAnimate.access$3400(COUISearchViewAnimate.this);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$13, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass13 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass13() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float fFloatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            if (COUISearchViewAnimate.access$3000(COUISearchViewAnimate.this) == 0) {
                COUISearchViewAnimate.access$300(COUISearchViewAnimate.this).setAlpha(fFloatValue);
            } else if (COUISearchViewAnimate.access$3000(COUISearchViewAnimate.this) == 1) {
                COUISearchViewAnimate.access$1400(COUISearchViewAnimate.this).setAlpha(fFloatValue);
                COUISearchViewAnimate.access$300(COUISearchViewAnimate.this).setAlpha(fFloatValue);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$14, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass14 implements Animator.AnimatorListener {
        public AnonymousClass14() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUISearchViewAnimate.access$3000(COUISearchViewAnimate.this) == 1) {
                COUISearchViewAnimate.access$3502(COUISearchViewAnimate.this, true);
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (COUISearchViewAnimate.access$3000(COUISearchViewAnimate.this) == 1) {
                COUISearchViewAnimate.access$1400(COUISearchViewAnimate.this).setVisibility(0);
            }
            COUISearchViewAnimate.access$300(COUISearchViewAnimate.this).setVisibility(0);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$15, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass15 implements Animator.AnimatorListener {
        public AnonymousClass15() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            COUISearchViewAnimate.access$3400(COUISearchViewAnimate.this);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUISearchViewAnimate.access$3202(COUISearchViewAnimate.this, 0);
            if (COUISearchViewAnimate.access$3000(COUISearchViewAnimate.this) == 1) {
                COUISearchViewAnimate.access$3502(COUISearchViewAnimate.this, true);
            }
            COUISearchViewAnimate.access$3400(COUISearchViewAnimate.this);
            COUISearchViewAnimate.access$1100(COUISearchViewAnimate.this);
            AnimatorHelper.access$2200(COUISearchViewAnimate.access$3600(COUISearchViewAnimate.this)).set(false);
            if (COUISearchViewAnimate.access$2000(COUISearchViewAnimate.this) != null) {
                COUISearchViewAnimate.access$2000(COUISearchViewAnimate.this).onAnimationEnd(1);
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUISearchViewAnimate.access$3202(COUISearchViewAnimate.this, 0);
            if (COUISearchViewAnimate.access$3000(COUISearchViewAnimate.this) == 1) {
                COUISearchViewAnimate.access$1400(COUISearchViewAnimate.this).setVisibility(0);
            }
            COUISearchViewAnimate.access$300(COUISearchViewAnimate.this).setVisibility(0);
            COUISearchViewAnimate.this.mState.set(1);
            if (!COUISearchViewAnimate.access$2300(COUISearchViewAnimate.this) || COUISearchViewAnimate.access$2400(COUISearchViewAnimate.this) == 0 || COUISearchViewAnimate.access$2500(COUISearchViewAnimate.this)) {
                COUISearchViewAnimate.access$1300(COUISearchViewAnimate.this);
                COUISearchViewAnimate.this.openSoftInput(true);
            }
            COUISearchViewAnimate.access$2100(COUISearchViewAnimate.this, 0, 1);
            COUISearchViewAnimate cOUISearchViewAnimate = COUISearchViewAnimate.this;
            COUISearchViewAnimate.access$3302(cOUISearchViewAnimate, cOUISearchViewAnimate.getTop());
            if (COUISearchViewAnimate.access$2000(COUISearchViewAnimate.this) != null) {
                COUISearchViewAnimate.access$2000(COUISearchViewAnimate.this).onAnimationStart(1);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$16, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass16 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass16() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float fFloatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            if (COUISearchViewAnimate.access$3000(COUISearchViewAnimate.this) == 0) {
                int iAccess$3300 = (int) (fFloatValue * (COUISearchViewAnimate.access$3300(COUISearchViewAnimate.this) - COUISearchViewAnimate.access$3700(COUISearchViewAnimate.this)));
                ((ViewGroup.MarginLayoutParams) COUISearchViewAnimate.this.getLayoutParams()).topMargin += iAccess$3300 - COUISearchViewAnimate.access$3200(COUISearchViewAnimate.this);
                COUISearchViewAnimate.this.requestLayout();
                COUISearchViewAnimate.access$3202(COUISearchViewAnimate.this, iAccess$3300);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$17, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass17 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass17() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float fFloatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            if (COUISearchViewAnimate.access$3000(COUISearchViewAnimate.this) == 0) {
                COUISearchViewAnimate cOUISearchViewAnimate = COUISearchViewAnimate.this;
                COUISearchViewAnimate.access$3102(cOUISearchViewAnimate, (int) ((1.0f - fFloatValue) * (COUISearchViewAnimate.access$700(cOUISearchViewAnimate) - COUISearchViewAnimate.access$800(COUISearchViewAnimate.this))));
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) COUISearchViewAnimate.access$600(COUISearchViewAnimate.this).getLayoutParams();
                marginLayoutParams.setMarginEnd(COUISearchViewAnimate.access$3100(COUISearchViewAnimate.this));
                COUISearchViewAnimate.access$600(COUISearchViewAnimate.this).setLayoutParams(marginLayoutParams);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$18, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass18 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass18() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float fFloatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            if (COUISearchViewAnimate.access$3000(COUISearchViewAnimate.this) == 0) {
                COUISearchViewAnimate.access$300(COUISearchViewAnimate.this).setAlpha(1.0f - fFloatValue);
            } else if (COUISearchViewAnimate.access$3000(COUISearchViewAnimate.this) == 1) {
                float f3 = 1.0f - fFloatValue;
                COUISearchViewAnimate.access$1400(COUISearchViewAnimate.this).setAlpha(f3);
                COUISearchViewAnimate.access$300(COUISearchViewAnimate.this).setAlpha(f3);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$19, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass19 implements Animator.AnimatorListener {
        public AnonymousClass19() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            COUISearchViewAnimate.access$3400(COUISearchViewAnimate.this);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUISearchViewAnimate.access$3202(COUISearchViewAnimate.this, 0);
            if (COUISearchViewAnimate.access$3000(COUISearchViewAnimate.this) == 1) {
                COUISearchViewAnimate.access$3502(COUISearchViewAnimate.this, false);
                COUISearchViewAnimate.access$1400(COUISearchViewAnimate.this).setVisibility(8);
                COUISearchViewAnimate.access$300(COUISearchViewAnimate.this).setVisibility(8);
            } else if (COUISearchViewAnimate.access$3000(COUISearchViewAnimate.this) == 0) {
                COUISearchViewAnimate.access$300(COUISearchViewAnimate.this).setVisibility(4);
            }
            COUISearchViewAnimate.access$3400(COUISearchViewAnimate.this);
            COUISearchViewAnimate.access$1300(COUISearchViewAnimate.this);
            AnimatorHelper.access$2200(COUISearchViewAnimate.access$3600(COUISearchViewAnimate.this)).set(false);
            COUISearchViewAnimate.access$600(COUISearchViewAnimate.this).setQuery("", false);
            if (COUISearchViewAnimate.access$2000(COUISearchViewAnimate.this) != null) {
                COUISearchViewAnimate.access$2000(COUISearchViewAnimate.this).onAnimationEnd(0);
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUISearchViewAnimate.access$3202(COUISearchViewAnimate.this, 0);
            COUISearchViewAnimate.access$600(COUISearchViewAnimate.this).getSearchAutoComplete().setText((CharSequence) null);
            COUISearchViewAnimate.access$600(COUISearchViewAnimate.this).getSearchAutoComplete().clearFocus();
            COUISearchViewAnimate.this.openSoftInput(false);
            COUISearchViewAnimate.this.mState.set(0);
            COUISearchViewAnimate.access$2100(COUISearchViewAnimate.this, 1, 0);
            if (COUISearchViewAnimate.access$2000(COUISearchViewAnimate.this) != null) {
                COUISearchViewAnimate.access$2000(COUISearchViewAnimate.this).onAnimationStart(0);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$2, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass2 implements TextWatcher {
        public AnonymousClass2() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            if ("".contentEquals(editable)) {
                COUISearchViewAnimate.access$000(COUISearchViewAnimate.this).setVisibility(0);
                COUISearchViewAnimate.access$100(COUISearchViewAnimate.this).setVisibility(0);
            } else {
                COUISearchViewAnimate.access$000(COUISearchViewAnimate.this).setVisibility(8);
                COUISearchViewAnimate.access$100(COUISearchViewAnimate.this).setVisibility(8);
            }
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$3, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass3 implements Runnable {
        public AnonymousClass3() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUISearchViewAnimate.access$300(COUISearchViewAnimate.this).getHitRect(COUISearchViewAnimate.access$200(COUISearchViewAnimate.this));
            COUISearchViewAnimate.access$200(COUISearchViewAnimate.this).right += COUISearchViewAnimate.this.getPaddingStart();
            COUISearchViewAnimate.access$200(COUISearchViewAnimate.this).left += COUISearchViewAnimate.this.getPaddingStart();
            COUISearchViewAnimate.access$200(COUISearchViewAnimate.this).top += COUISearchViewAnimate.access$400(COUISearchViewAnimate.this).getTop();
            COUISearchViewAnimate.access$200(COUISearchViewAnimate.this).bottom += COUISearchViewAnimate.access$400(COUISearchViewAnimate.this).getTop();
            float fMax = Float.max(0.0f, COUISearchViewAnimate.access$400(COUISearchViewAnimate.this).getMeasuredHeight() - COUISearchViewAnimate.access$200(COUISearchViewAnimate.this).height());
            Rect rectAccess$200 = COUISearchViewAnimate.access$200(COUISearchViewAnimate.this);
            float f3 = fMax / 2.0f;
            rectAccess$200.top = (int) (rectAccess$200.top - f3);
            Rect rectAccess$201 = COUISearchViewAnimate.access$200(COUISearchViewAnimate.this);
            rectAccess$201.bottom = (int) (rectAccess$201.bottom + f3);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$4, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass4 implements Runnable {
        final /* synthetic */ int val$targetState;

        public AnonymousClass4(int i3) {
            this.val$targetState = i3;
        }

        @Override // java.lang.Runnable
        public void run() {
            COUISearchViewAnimate.access$500(COUISearchViewAnimate.this, this.val$targetState);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$5, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass5 implements Runnable {
        public AnonymousClass5() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) COUISearchViewAnimate.access$600(COUISearchViewAnimate.this).getLayoutParams();
            marginLayoutParams.setMarginEnd(COUISearchViewAnimate.access$700(COUISearchViewAnimate.this) - COUISearchViewAnimate.access$800(COUISearchViewAnimate.this));
            COUISearchViewAnimate.access$600(COUISearchViewAnimate.this).setLayoutParams(marginLayoutParams);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$6, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass6 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass6() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUISearchViewAnimate.access$900(COUISearchViewAnimate.this, ((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$7, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass7 extends AnimatorListenerAdapter {
        public AnonymousClass7() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            COUISearchViewAnimate.access$1002(COUISearchViewAnimate.this, false);
            COUISearchViewAnimate.access$1100(COUISearchViewAnimate.this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$8, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass8 extends AnimatorListenerAdapter {
        public AnonymousClass8() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            COUISearchViewAnimate.this.setVisibility(8);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$9, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass9 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass9() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUISearchViewAnimate.access$900(COUISearchViewAnimate.this, ((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    /* JADX INFO: loaded from: classes7.dex */
    @Retention(RetentionPolicy.SOURCE)
    public @interface AddToolBarWay {
    }

    /* JADX INFO: loaded from: classes7.dex */
    public class AnimatorHelper {
        private int mIconTranslation;
        private volatile AtomicBoolean mAnimatingAtomic = new AtomicBoolean(false);
        private Runnable mToEditStartRunnable = new AnonymousClass1();
        private Runnable mToEditEndRunnable = new AnonymousClass2();
        private Runnable mToNormalStartRunnable = new AnonymousClass3();
        private Runnable mToNormalEndRunnable = new AnonymousClass4();

        /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$AnimatorHelper$1, reason: invalid class name */
        public class AnonymousClass1 implements Runnable {
            public AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() {
                if (COUISearchViewAnimate.access$1900(COUISearchViewAnimate.this)) {
                    COUISearchViewAnimate.access$1300(COUISearchViewAnimate.this);
                    COUISearchViewAnimate.this.openSoftInput(true);
                }
                COUISearchViewAnimate.access$1902(COUISearchViewAnimate.this, true);
                if (COUISearchViewAnimate.access$2000(COUISearchViewAnimate.this) != null) {
                    COUISearchViewAnimate.access$2000(COUISearchViewAnimate.this).onAnimationStart(1);
                }
                COUISearchViewAnimate.access$2100(COUISearchViewAnimate.this, 0, 1);
            }
        }

        /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$AnimatorHelper$10, reason: invalid class name */
        public class AnonymousClass10 extends AnimatorListenerAdapter {
            public AnonymousClass10() {
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                if (COUISearchViewAnimate.access$3000(COUISearchViewAnimate.this) == 1) {
                    COUISearchViewAnimate.access$1400(COUISearchViewAnimate.this).setVisibility(8);
                    COUISearchViewAnimate.access$300(COUISearchViewAnimate.this).setVisibility(8);
                } else if (COUISearchViewAnimate.access$3000(COUISearchViewAnimate.this) == 0) {
                    COUISearchViewAnimate.access$300(COUISearchViewAnimate.this).setVisibility(4);
                }
                AnimatorHelper.access$1800(AnimatorHelper.this).run();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                super.onAnimationStart(animator);
                AnimatorHelper.access$1700(AnimatorHelper.this).run();
            }
        }

        /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$AnimatorHelper$11, reason: invalid class name */
        public class AnonymousClass11 implements ValueAnimator.AnimatorUpdateListener {
            public AnonymousClass11() {
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fFloatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                if (COUISearchViewAnimate.access$3000(COUISearchViewAnimate.this) == 0) {
                    COUISearchViewAnimate.access$300(COUISearchViewAnimate.this).setAlpha(fFloatValue);
                    COUISearchViewAnimate cOUISearchViewAnimate = COUISearchViewAnimate.this;
                    COUISearchViewAnimate.access$3102(cOUISearchViewAnimate, (int) (fFloatValue * (COUISearchViewAnimate.access$700(cOUISearchViewAnimate) - COUISearchViewAnimate.access$800(COUISearchViewAnimate.this))));
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) COUISearchViewAnimate.access$600(COUISearchViewAnimate.this).getLayoutParams();
                    marginLayoutParams.setMarginEnd(COUISearchViewAnimate.access$3100(COUISearchViewAnimate.this));
                    COUISearchViewAnimate.access$600(COUISearchViewAnimate.this).setLayoutParams(marginLayoutParams);
                } else if (COUISearchViewAnimate.access$3000(COUISearchViewAnimate.this) == 1) {
                    COUISearchViewAnimate.access$1400(COUISearchViewAnimate.this).setAlpha(fFloatValue);
                    COUISearchViewAnimate.access$300(COUISearchViewAnimate.this).setAlpha(fFloatValue);
                }
                if (COUISearchViewAnimate.access$2000(COUISearchViewAnimate.this) != null) {
                    COUISearchViewAnimate.access$2000(COUISearchViewAnimate.this).onUpdate(1, valueAnimator);
                }
            }
        }

        /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$AnimatorHelper$12, reason: invalid class name */
        public class AnonymousClass12 extends AnimatorListenerAdapter {
            public AnonymousClass12() {
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                AnimatorHelper.access$1600(AnimatorHelper.this).run();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                super.onAnimationStart(animator);
                AnimatorHelper.access$1500(AnimatorHelper.this).run();
            }
        }

        /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$AnimatorHelper$2, reason: invalid class name */
        public class AnonymousClass2 implements Runnable {
            public AnonymousClass2() {
            }

            @Override // java.lang.Runnable
            public void run() {
                COUISearchViewAnimate.access$1100(COUISearchViewAnimate.this);
                AnimatorHelper.access$2200(AnimatorHelper.this).set(false);
                if (COUISearchViewAnimate.access$2000(COUISearchViewAnimate.this) != null) {
                    COUISearchViewAnimate.access$2000(COUISearchViewAnimate.this).onAnimationEnd(1);
                }
            }
        }

        /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$AnimatorHelper$3, reason: invalid class name */
        public class AnonymousClass3 implements Runnable {
            public AnonymousClass3() {
            }

            @Override // java.lang.Runnable
            public void run() {
                COUISearchViewAnimate.access$1100(COUISearchViewAnimate.this);
                COUISearchViewAnimate.this.openSoftInput(false);
                if (COUISearchViewAnimate.access$2000(COUISearchViewAnimate.this) != null) {
                    COUISearchViewAnimate.access$2000(COUISearchViewAnimate.this).onAnimationStart(0);
                }
                COUISearchViewAnimate.access$2100(COUISearchViewAnimate.this, 1, 0);
            }
        }

        /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$AnimatorHelper$4, reason: invalid class name */
        public class AnonymousClass4 implements Runnable {
            public AnonymousClass4() {
            }

            @Override // java.lang.Runnable
            public void run() {
                COUISearchViewAnimate.access$1300(COUISearchViewAnimate.this);
                AnimatorHelper.access$2200(AnimatorHelper.this).set(false);
                COUISearchViewAnimate.access$600(COUISearchViewAnimate.this).setQuery("", false);
                if (COUISearchViewAnimate.access$2000(COUISearchViewAnimate.this) != null) {
                    COUISearchViewAnimate.access$2000(COUISearchViewAnimate.this).onAnimationEnd(0);
                }
            }
        }

        /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$AnimatorHelper$5, reason: invalid class name */
        public class AnonymousClass5 extends AnimatorListenerAdapter {
            public AnonymousClass5() {
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this).setVisibility(4);
            }
        }

        /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$AnimatorHelper$6, reason: invalid class name */
        public class AnonymousClass6 extends AnimatorListenerAdapter {
            public AnonymousClass6() {
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this).setVisibility(0);
            }
        }

        /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$AnimatorHelper$7, reason: invalid class name */
        public class AnonymousClass7 extends AnimatorListenerAdapter {
            public AnonymousClass7() {
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this).setVisibility(8);
            }
        }

        /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$AnimatorHelper$8, reason: invalid class name */
        public class AnonymousClass8 extends AnimatorListenerAdapter {
            public AnonymousClass8() {
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this).setRotationY(0.0f);
            }
        }

        /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$AnimatorHelper$9, reason: invalid class name */
        public class AnonymousClass9 implements ValueAnimator.AnimatorUpdateListener {
            public AnonymousClass9() {
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fFloatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                if (COUISearchViewAnimate.access$3000(COUISearchViewAnimate.this) == 0) {
                    float f3 = 1.0f - fFloatValue;
                    COUISearchViewAnimate.access$300(COUISearchViewAnimate.this).setAlpha(f3);
                    COUISearchViewAnimate cOUISearchViewAnimate = COUISearchViewAnimate.this;
                    COUISearchViewAnimate.access$3102(cOUISearchViewAnimate, (int) (f3 * (COUISearchViewAnimate.access$700(cOUISearchViewAnimate) - COUISearchViewAnimate.access$800(COUISearchViewAnimate.this))));
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) COUISearchViewAnimate.access$600(COUISearchViewAnimate.this).getLayoutParams();
                    marginLayoutParams.setMarginEnd(COUISearchViewAnimate.access$3100(COUISearchViewAnimate.this));
                    COUISearchViewAnimate.access$600(COUISearchViewAnimate.this).setLayoutParams(marginLayoutParams);
                } else if (COUISearchViewAnimate.access$3000(COUISearchViewAnimate.this) == 1) {
                    float f4 = 1.0f - fFloatValue;
                    COUISearchViewAnimate.access$1400(COUISearchViewAnimate.this).setAlpha(f4);
                    COUISearchViewAnimate.access$300(COUISearchViewAnimate.this).setAlpha(f4);
                }
                if (COUISearchViewAnimate.access$2000(COUISearchViewAnimate.this) != null) {
                    COUISearchViewAnimate.access$2000(COUISearchViewAnimate.this).onUpdate(0, valueAnimator);
                }
            }
        }

        public AnimatorHelper() {
        }

        public static /* synthetic */ Runnable access$1500(AnimatorHelper animatorHelper) {
            return animatorHelper.mToEditStartRunnable;
        }

        public static /* synthetic */ Runnable access$1600(AnimatorHelper animatorHelper) {
            return animatorHelper.mToEditEndRunnable;
        }

        public static /* synthetic */ Runnable access$1700(AnimatorHelper animatorHelper) {
            return animatorHelper.mToNormalStartRunnable;
        }

        public static /* synthetic */ Runnable access$1800(AnimatorHelper animatorHelper) {
            return animatorHelper.mToNormalEndRunnable;
        }

        public static /* synthetic */ AtomicBoolean access$2200(AnimatorHelper animatorHelper) {
            return animatorHelper.mAnimatingAtomic;
        }

        private void startCancelButtonEnterValueAnimator() {
            COUISearchViewAnimate.access$300(COUISearchViewAnimate.this).setAlpha(0.0f);
            COUISearchViewAnimate.access$300(COUISearchViewAnimate.this).setVisibility(0);
            ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            valueAnimatorOfFloat.setDuration(450L);
            valueAnimatorOfFloat.addUpdateListener(new AnonymousClass11());
            valueAnimatorOfFloat.addListener(new AnonymousClass12());
            valueAnimatorOfFloat.start();
        }

        private void startCancelButtonExitValueAnimator() {
            ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            valueAnimatorOfFloat.setDuration(450L);
            valueAnimatorOfFloat.addUpdateListener(new AnonymousClass9());
            valueAnimatorOfFloat.addListener(new AnonymousClass10());
            valueAnimatorOfFloat.start();
        }

        private void startSearchIconFadeInAnimator() {
            if (COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this) != null) {
                COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this).setPivotX(0.0f);
                COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this).setRotationY(0.0f);
                COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this).setVisibility(0);
                COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this).animate().setDuration(150L).alpha(1.0f).setListener(new AnonymousClass6()).start();
            }
        }

        private void startSearchIconFadeOutAnimator() {
            if (COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this) != null) {
                COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this).setPivotX(0.0f);
                COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this).setRotationY(0.0f);
                COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this).animate().setDuration(150L).alpha(0.0f).setListener(new AnonymousClass5()).start();
            }
        }

        public void runStateChangeAnimation(int i3) {
            if (COUISearchViewAnimate.this.mState.get() == i3) {
                Log.d(COUISearchViewAnimate.TAG, "runStateChangeAnimation: same state , return. targetState = " + i3);
                return;
            }
            if (i3 == 1) {
                startToEditAnimator();
            } else if (i3 == 0) {
                startToNormalAnimator();
            }
        }

        public void startCancelButtonEnterAnimator() {
            if (COUISearchViewAnimate.access$300(COUISearchViewAnimate.this) != null) {
                COUISearchViewAnimate.access$300(COUISearchViewAnimate.this).setAlpha(0.0f);
                if (COUISearchViewAnimate.access$3000(COUISearchViewAnimate.this) == 1) {
                    COUISearchViewAnimate.access$1400(COUISearchViewAnimate.this).setAlpha(0.0f);
                    COUISearchViewAnimate.access$1400(COUISearchViewAnimate.this).setVisibility(0);
                }
                COUISearchViewAnimate.access$300(COUISearchViewAnimate.this).setVisibility(0);
                startCancelButtonEnterValueAnimator();
            }
        }

        public void startCancelButtonExitAnimator() {
            if (COUISearchViewAnimate.access$300(COUISearchViewAnimate.this) != null) {
                COUISearchViewAnimate.access$300(COUISearchViewAnimate.this).setAlpha(1.0f);
                if (COUISearchViewAnimate.access$3000(COUISearchViewAnimate.this) == 1) {
                    COUISearchViewAnimate.access$1400(COUISearchViewAnimate.this).setAlpha(1.0f);
                }
                if (COUISearchViewAnimate.access$300(COUISearchViewAnimate.this).isPerformClicked()) {
                    COUISearchViewAnimate.access$300(COUISearchViewAnimate.this).setPerformClicked(false);
                } else {
                    COUISearchViewAnimate.access$300(COUISearchViewAnimate.this).setVisibility(0);
                    if (COUISearchViewAnimate.access$3000(COUISearchViewAnimate.this) == 1) {
                        COUISearchViewAnimate.access$1400(COUISearchViewAnimate.this).setVisibility(0);
                    }
                }
                startCancelButtonExitValueAnimator();
            }
        }

        public void startSearchIconEnterAnimator() {
            if (COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this) != null) {
                if (this.mIconTranslation == 0) {
                    if (COUISearchViewAnimate.access$2900(COUISearchViewAnimate.this)) {
                        this.mIconTranslation = (COUISearchViewAnimate.this.getWidth() - COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this).getRight()) + COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this).getWidth();
                    } else {
                        this.mIconTranslation = -COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this).getLeft();
                    }
                }
                COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this).setVisibility(0);
                COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this).setPivotX(this.mIconTranslation);
                COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this).setRotationY(80.0f);
                COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this).animate().setDuration(150L).rotationY(0.0f).setListener(new AnonymousClass8()).start();
            }
        }

        public void startSearchIconExitAnimator() {
            if (COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this) != null) {
                if (this.mIconTranslation == 0) {
                    if (COUISearchViewAnimate.access$2900(COUISearchViewAnimate.this)) {
                        this.mIconTranslation = (COUISearchViewAnimate.this.getWidth() - COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this).getRight()) + COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this).getWidth();
                    } else {
                        this.mIconTranslation = -COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this).getLeft();
                    }
                }
                COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this).setPivotX(this.mIconTranslation);
                COUISearchViewAnimate.access$2800(COUISearchViewAnimate.this).animate().setDuration(150L).rotationY(80.0f).setListener(new AnonymousClass7()).start();
            }
        }

        public void startToEditAnimator() {
            synchronized (this) {
                try {
                    if (this.mAnimatingAtomic.compareAndSet(false, true)) {
                        if (!COUISearchViewAnimate.access$2300(COUISearchViewAnimate.this) || COUISearchViewAnimate.access$2400(COUISearchViewAnimate.this) == 0 || COUISearchViewAnimate.access$2500(COUISearchViewAnimate.this)) {
                            COUISearchViewAnimate.this.mState.set(1);
                            COUISearchViewAnimate.access$2600(COUISearchViewAnimate.this).start();
                        } else {
                            COUISearchViewAnimate.access$1300(COUISearchViewAnimate.this);
                            COUISearchViewAnimate.this.openSoftInput(true);
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        public void startToNormalAnimator() {
            synchronized (this) {
                try {
                    if (this.mAnimatingAtomic.compareAndSet(false, true)) {
                        COUISearchViewAnimate.access$2700(COUISearchViewAnimate.this).start();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    /* JADX INFO: loaded from: classes7.dex */
    public static class COUISavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<COUISavedState> CREATOR = new AnonymousClass1();
        float mCollapsingHeightPercent;

        /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchViewAnimate$COUISavedState$1, reason: invalid class name */
        public class AnonymousClass1 implements Parcelable.Creator<COUISavedState> {
            @Override // android.os.Parcelable.Creator
            public /* bridge */ /* synthetic */ COUISavedState createFromParcel(Parcel parcel) {
                return createFromParcel(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public /* bridge */ /* synthetic */ COUISavedState[] newArray(int i3) {
                return newArray(i3);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public COUISavedState createFromParcel(Parcel parcel) {
                return new COUISavedState(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public COUISavedState[] newArray(int i3) {
                return new COUISavedState[i3];
            }
        }

        public COUISavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override // android.view.AbsSavedState, android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public void readFromParcel(Parcel parcel) {
            this.mCollapsingHeightPercent = parcel.readFloat();
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i3) {
            super.writeToParcel(parcel, i3);
            parcel.writeFloat(this.mCollapsingHeightPercent);
        }

        public COUISavedState(Parcel parcel) {
            super(parcel);
            this.mCollapsingHeightPercent = parcel.readFloat();
        }
    }

    /* JADX INFO: loaded from: classes7.dex */
    public interface OnAnimationListener {
        void onAnimationEnd(int i3);

        void onAnimationStart(int i3);

        void onUpdate(int i3, ValueAnimator valueAnimator);
    }

    /* JADX INFO: loaded from: classes7.dex */
    public interface OnCancelButtonClickListener {
        boolean onClickCancel();
    }

    /* JADX INFO: loaded from: classes7.dex */
    public interface OnDispatchKeyEventPreImeListener {
        void onDispatchKeyEventPreIme(KeyEvent keyEvent);
    }

    /* JADX INFO: loaded from: classes7.dex */
    public interface OnStateChangeListener {
        void onStateChange(int i3, int i4);
    }

    /* JADX INFO: loaded from: classes7.dex */
    public static class SearchFunctionalButton extends AppCompatButton {
        private static final int DEFAULT_MAX_LINES = 1;
        volatile boolean mIsPerformClicked;
        PerformClickCallback mPerformClickCallback;

        public interface PerformClickCallback {
            void performClick();
        }

        public SearchFunctionalButton(Context context) {
            this(context, null);
        }

        public boolean isPerformClicked() {
            return this.mIsPerformClicked;
        }

        @Override // android.view.View
        public boolean performClick() {
            if (this.mPerformClickCallback != null) {
                this.mIsPerformClicked = true;
                this.mPerformClickCallback.performClick();
            }
            return super.performClick();
        }

        public void setPerformClickCallback(PerformClickCallback performClickCallback) {
            this.mPerformClickCallback = performClickCallback;
        }

        public void setPerformClicked(boolean z2) {
            this.mIsPerformClicked = z2;
        }

        public SearchFunctionalButton(Context context, AttributeSet attributeSet) {
            this(context, attributeSet, 0);
        }

        public SearchFunctionalButton(Context context, AttributeSet attributeSet, int i3) {
            super(context, attributeSet, i3);
            this.mPerformClickCallback = null;
            this.mIsPerformClicked = false;
            setMaxLines(1);
            setMaxWidth(context.getResources().getDimensionPixelOffset(R.dimen.coui_search_function_button_max_width));
            setEllipsize(TextUtils.TruncateAt.END);
        }
    }

    /* JADX INFO: loaded from: classes7.dex */
    @Retention(RetentionPolicy.SOURCE)
    public @interface SearchViewState {
    }

    /* JADX INFO: loaded from: classes7.dex */
    @Retention(RetentionPolicy.SOURCE)
    public @interface SearchViewType {
    }

    public COUISearchViewAnimate(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void a(COUISearchViewAnimate cOUISearchViewAnimate, ValueAnimator valueAnimator) {
        cOUISearchViewAnimate.getClass();
        float fFloatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        if (cOUISearchViewAnimate.mSearchViewType == 0) {
            int i3 = (int) (fFloatValue * (cOUISearchViewAnimate.mStartY - cOUISearchViewAnimate.mExtraY));
            ((ViewGroup.MarginLayoutParams) cOUISearchViewAnimate.getLayoutParams()).topMargin -= i3 - cOUISearchViewAnimate.mTopOffset;
            cOUISearchViewAnimate.requestLayout();
            cOUISearchViewAnimate.mTopOffset = i3;
        }
    }

    public static /* synthetic */ ImageView access$000(COUISearchViewAnimate cOUISearchViewAnimate) {
        return cOUISearchViewAnimate.mMainIcon;
    }

    public static /* synthetic */ ImageView access$100(COUISearchViewAnimate cOUISearchViewAnimate) {
        return cOUISearchViewAnimate.mSubIcon;
    }

    public static /* synthetic */ boolean access$1002(COUISearchViewAnimate cOUISearchViewAnimate, boolean z2) {
        cOUISearchViewAnimate.mToolBarAnimationRunning = z2;
        return z2;
    }

    public static /* synthetic */ void access$1100(COUISearchViewAnimate cOUISearchViewAnimate) {
        cOUISearchViewAnimate.setSearchAutoCompleteFocus();
    }

    public static /* synthetic */ void access$1200(COUISearchViewAnimate cOUISearchViewAnimate, int i3) {
        cOUISearchViewAnimate.setToolBarChildVisibility(i3);
    }

    public static /* synthetic */ void access$1300(COUISearchViewAnimate cOUISearchViewAnimate) {
        cOUISearchViewAnimate.setSearchAutoCompleteUnFocus();
    }

    public static /* synthetic */ ImageView access$1400(COUISearchViewAnimate cOUISearchViewAnimate) {
        return cOUISearchViewAnimate.mButtonDivider;
    }

    public static /* synthetic */ boolean access$1900(COUISearchViewAnimate cOUISearchViewAnimate) {
        return cOUISearchViewAnimate.mShouldClearFocus;
    }

    public static /* synthetic */ boolean access$1902(COUISearchViewAnimate cOUISearchViewAnimate, boolean z2) {
        cOUISearchViewAnimate.mShouldClearFocus = z2;
        return z2;
    }

    public static /* synthetic */ Rect access$200(COUISearchViewAnimate cOUISearchViewAnimate) {
        return cOUISearchViewAnimate.mWrapperBounds;
    }

    public static /* synthetic */ OnAnimationListener access$2000(COUISearchViewAnimate cOUISearchViewAnimate) {
        return cOUISearchViewAnimate.mOnAnimationListener;
    }

    public static /* synthetic */ void access$2100(COUISearchViewAnimate cOUISearchViewAnimate, int i3, int i4) {
        cOUISearchViewAnimate.notifyOnStateChange(i3, i4);
    }

    public static /* synthetic */ boolean access$2300(COUISearchViewAnimate cOUISearchViewAnimate) {
        return cOUISearchViewAnimate.mIsAtLeastR;
    }

    public static /* synthetic */ int access$2400(COUISearchViewAnimate cOUISearchViewAnimate) {
        return cOUISearchViewAnimate.mShowImeAnimDuration;
    }

    public static /* synthetic */ boolean access$2500(COUISearchViewAnimate cOUISearchViewAnimate) {
        return cOUISearchViewAnimate.isInMultiWindowMode();
    }

    public static /* synthetic */ AnimatorSet access$2600(COUISearchViewAnimate cOUISearchViewAnimate) {
        return cOUISearchViewAnimate.mSearchViewSmoothEnterAnimatorSet;
    }

    public static /* synthetic */ AnimatorSet access$2700(COUISearchViewAnimate cOUISearchViewAnimate) {
        return cOUISearchViewAnimate.mSearchViewSmoothExitAnimatorSet;
    }

    public static /* synthetic */ ImageView access$2800(COUISearchViewAnimate cOUISearchViewAnimate) {
        return cOUISearchViewAnimate.mSearchIcon;
    }

    public static /* synthetic */ boolean access$2900(COUISearchViewAnimate cOUISearchViewAnimate) {
        return cOUISearchViewAnimate.isRtl();
    }

    public static /* synthetic */ SearchFunctionalButton access$300(COUISearchViewAnimate cOUISearchViewAnimate) {
        return cOUISearchViewAnimate.mFunctionalButton;
    }

    public static /* synthetic */ int access$3000(COUISearchViewAnimate cOUISearchViewAnimate) {
        return cOUISearchViewAnimate.mSearchViewType;
    }

    public static /* synthetic */ int access$3100(COUISearchViewAnimate cOUISearchViewAnimate) {
        return cOUISearchViewAnimate.mSearchViewShrinkWidth;
    }

    public static /* synthetic */ int access$3102(COUISearchViewAnimate cOUISearchViewAnimate, int i3) {
        cOUISearchViewAnimate.mSearchViewShrinkWidth = i3;
        return i3;
    }

    public static /* synthetic */ int access$3200(COUISearchViewAnimate cOUISearchViewAnimate) {
        return cOUISearchViewAnimate.mTopOffset;
    }

    public static /* synthetic */ int access$3202(COUISearchViewAnimate cOUISearchViewAnimate, int i3) {
        cOUISearchViewAnimate.mTopOffset = i3;
        return i3;
    }

    public static /* synthetic */ int access$3300(COUISearchViewAnimate cOUISearchViewAnimate) {
        return cOUISearchViewAnimate.mStartY;
    }

    public static /* synthetic */ int access$3302(COUISearchViewAnimate cOUISearchViewAnimate, int i3) {
        cOUISearchViewAnimate.mStartY = i3;
        return i3;
    }

    public static /* synthetic */ void access$3400(COUISearchViewAnimate cOUISearchViewAnimate) {
        cOUISearchViewAnimate.updateBackgroundRect();
    }

    public static /* synthetic */ boolean access$3502(COUISearchViewAnimate cOUISearchViewAnimate, boolean z2) {
        cOUISearchViewAnimate.mDivideBackground = z2;
        return z2;
    }

    public static /* synthetic */ AnimatorHelper access$3600(COUISearchViewAnimate cOUISearchViewAnimate) {
        return cOUISearchViewAnimate.getAnimatorHelper();
    }

    public static /* synthetic */ int access$3700(COUISearchViewAnimate cOUISearchViewAnimate) {
        return cOUISearchViewAnimate.mExtraY;
    }

    public static /* synthetic */ ConstraintLayout access$400(COUISearchViewAnimate cOUISearchViewAnimate) {
        return cOUISearchViewAnimate.mSearchViewWrapper;
    }

    public static /* synthetic */ void access$500(COUISearchViewAnimate cOUISearchViewAnimate, int i3) {
        cOUISearchViewAnimate.changeStateWithOutAnimation(i3);
    }

    public static /* synthetic */ COUISearchView access$600(COUISearchViewAnimate cOUISearchViewAnimate) {
        return cOUISearchViewAnimate.mSearchView;
    }

    public static /* synthetic */ int access$700(COUISearchViewAnimate cOUISearchViewAnimate) {
        return cOUISearchViewAnimate.getOriginWidth();
    }

    public static /* synthetic */ int access$800(COUISearchViewAnimate cOUISearchViewAnimate) {
        return cOUISearchViewAnimate.getShrinkWidth();
    }

    public static /* synthetic */ void access$900(COUISearchViewAnimate cOUISearchViewAnimate, float f3) {
        cOUISearchViewAnimate.setToolBarAlpha(f3);
    }

    public static /* synthetic */ void b(COUISearchViewAnimate cOUISearchViewAnimate, ValueAnimator valueAnimator) {
        cOUISearchViewAnimate.getClass();
        float fFloatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        if (cOUISearchViewAnimate.mSearchViewType == 0) {
            cOUISearchViewAnimate.mSearchViewShrinkWidth = (int) (fFloatValue * (cOUISearchViewAnimate.getOriginWidth() - cOUISearchViewAnimate.getShrinkWidth()));
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) cOUISearchViewAnimate.mSearchView.getLayoutParams();
            marginLayoutParams.setMarginEnd(cOUISearchViewAnimate.mSearchViewShrinkWidth);
            cOUISearchViewAnimate.mSearchView.setLayoutParams(marginLayoutParams);
        }
    }

    public static /* synthetic */ void c(COUISearchViewAnimate cOUISearchViewAnimate, ImeInsetsAnimationCallback imeInsetsAnimationCallback) {
        ViewCompat.setWindowInsetsAnimationCallback(cOUISearchViewAnimate.mSearchView.getRootView(), imeInsetsAnimationCallback);
    }

    private void changeStateWithOutAnimation(int i3) {
        if (this.mState.get() == i3) {
            Log.d(TAG, "changeStateWithOutAnimation: same state , return. targetState = " + i3);
            return;
        }
        this.mState.set(i3);
        Log.d(TAG, "changeStateWithOutAnimation: " + i3);
        if (i3 == 1) {
            this.mSearchView.setAlpha(1.0f);
            this.mFunctionalButton.setAlpha(1.0f);
            this.mSearchView.setVisibility(0);
            this.mFunctionalButton.setVisibility(0);
            this.mSearchIcon.setVisibility(0);
            int i4 = this.mSearchViewType;
            if (i4 == 1) {
                this.mButtonDivider.setAlpha(1.0f);
            } else if (i4 == 0) {
                int originWidth = getOriginWidth() - getShrinkWidth();
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mSearchView.getLayoutParams();
                marginLayoutParams.setMarginEnd(originWidth);
                this.mSearchView.setLayoutParams(marginLayoutParams);
            }
            AnimatorHelper.access$1500(getAnimatorHelper()).run();
            AnimatorHelper.access$1600(getAnimatorHelper()).run();
        } else {
            this.mSearchIcon.setAlpha(1.0f);
            this.mSearchIcon.setRotationY(0.0f);
            this.mSearchView.setQuery("", false);
            int i5 = this.mSearchViewType;
            if (i5 == 1) {
                this.mButtonDivider.setAlpha(0.0f);
                this.mFunctionalButton.setVisibility(8);
            } else if (i5 == 0) {
                ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) this.mSearchView.getLayoutParams();
                marginLayoutParams2.setMarginEnd(0);
                this.mSearchView.setLayoutParams(marginLayoutParams2);
                this.mFunctionalButton.setVisibility(4);
            }
            this.mSearchIcon.setVisibility(0);
            AnimatorHelper.access$1700(getAnimatorHelper()).run();
            AnimatorHelper.access$1800(getAnimatorHelper()).run();
        }
        requestLayout();
    }

    private float clampMarginValue(float f3) {
        return Math.max(0.0f, Math.min(1.0f, f3 / 0.3f));
    }

    private float clampProgress(float f3, float f4, float f5) {
        return Float.max(0.0f, Float.min((f5 / (f4 - f3)) + (f3 / (f3 - f4)), 1.0f));
    }

    private float clampSearchViewHeight(float f3) {
        return (f3 / 0.7f) - 0.42857146f;
    }

    private void ensureAddedToToolBar() {
        if (this.mHasAddedToToolbar) {
            return;
        }
        this.mHasAddedToToolbar = true;
        if (this.mToolBar != null) {
            removeLast();
            COUIToolbar.LayoutParams layoutParams = new COUIToolbar.LayoutParams(-1, this.mToolBar.getHeight() - this.mToolBar.getPaddingTop());
            layoutParams.gravity = this.mGravityInToolBar;
            this.mToolBar.setSearchView(this, layoutParams);
        }
    }

    private List ensureList(List list) {
        return list == null ? new ArrayList() : list;
    }

    private AnimatorHelper getAnimatorHelper() {
        if (this.mAnimatorHelper == null) {
            synchronized (this) {
                try {
                    if (this.mAnimatorHelper == null) {
                        this.mAnimatorHelper = new AnimatorHelper();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return this.mAnimatorHelper;
    }

    private int getOriginWidth() {
        return (((getMeasuredWidth() - getPaddingStart()) - getPaddingEnd()) - this.mBackgroundStartGap) - this.mBackgroundEndGap;
    }

    private int getShrinkWidth() {
        int i3 = this.mSearchViewType;
        if (i3 == 0) {
            return ((getOriginWidth() - this.mCancelButtonLargeStartMargin) - this.mFunctionalButton.getMeasuredWidth()) + this.mFunctionalButton.getPaddingEnd();
        }
        return i3 == 1 ? (((getOriginWidth() - this.mCancelButtonSmallStartMargin) - this.mBackgroundEndGap) - this.mFunctionalButton.getMeasuredWidth()) - this.mButtonDivider.getMeasuredWidth() : getOriginWidth();
    }

    private void inflateView(Context context, AttributeSet attributeSet) {
        View.inflate(context, R.layout.coui_search_view_animate_layout, this);
        this.mSearchIcon = (ImageView) findViewById(R.id.animated_search_icon);
        this.mSearchView = (COUISearchView) findViewById(R.id.animated_search_view);
        this.mFunctionalButton = (SearchFunctionalButton) findViewById(R.id.animated_cancel_button);
        this.mButtonDivider = (ImageView) findViewById(R.id.button_divider);
        this.mSearchViewWrapper = (ConstraintLayout) findViewById(R.id.coui_search_view_wrapper);
    }

    private void initAnimator() {
        initSearchViewEnterAnimator();
        initSearchViewExitAnimator();
        initButtonEnterAnimator();
        initButtonExitAnimator();
        initSmoothEnterAnimatorSet();
        initSmoothExitAnimatorSet();
    }

    private void initButtonEnterAnimator() {
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mButtonAlphaEnterAnimator = valueAnimatorOfFloat;
        valueAnimatorOfFloat.setDuration(this.mSearchViewType == 0 ? DEFAULT_BUTTON_ALPHA_CHANGE_DURATION : 100L);
        this.mButtonAlphaEnterAnimator.setInterpolator(DEFAULT_BUTTON_ALPHA_CHANGE_INTERPOLATOR);
        this.mButtonAlphaEnterAnimator.setStartDelay(this.mSearchViewType != 0 ? 0L : 100L);
        this.mButtonAlphaEnterAnimator.addUpdateListener(new AnonymousClass13());
        this.mButtonAlphaEnterAnimator.addListener(new AnonymousClass14());
    }

    private void initButtonExitAnimator() {
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mButtonAlphaExitAnimator = valueAnimatorOfFloat;
        valueAnimatorOfFloat.setDuration(this.mSearchViewType == 0 ? DEFAULT_BUTTON_ALPHA_CHANGE_DURATION : 100L);
        this.mButtonAlphaExitAnimator.setInterpolator(DEFAULT_BUTTON_ALPHA_CHANGE_INTERPOLATOR);
        this.mButtonAlphaExitAnimator.addUpdateListener(new AnonymousClass18());
    }

    private void initSearchViewEnterAnimator() {
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mSearchViewOffsetEnterAnimator = valueAnimatorOfFloat;
        valueAnimatorOfFloat.setDuration(450L);
        this.mSearchViewOffsetEnterAnimator.setInterpolator(DEFAULT_SEARCH_VIEW_OFFSET_CHANGE_INTERPOLATOR);
        this.mSearchViewOffsetEnterAnimator.addUpdateListener(new n(this));
        this.mSearchViewOffsetEnterAnimator.addListener(new AnonymousClass11());
        ValueAnimator valueAnimatorOfFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mSearchViewScaleEnterAnimator = valueAnimatorOfFloat2;
        valueAnimatorOfFloat2.setDuration(450L);
        this.mSearchViewScaleEnterAnimator.setInterpolator(DEFAULT_SEARCH_VIEW_SCALE_CHANGE_INTERPOLATOR);
        this.mSearchViewScaleEnterAnimator.addUpdateListener(new o(this));
        this.mSearchViewScaleEnterAnimator.addListener(new AnonymousClass12());
    }

    private void initSearchViewExitAnimator() {
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mSearchViewOffsetExitAnimator = valueAnimatorOfFloat;
        valueAnimatorOfFloat.setDuration(450L);
        ValueAnimator valueAnimator = this.mSearchViewOffsetExitAnimator;
        Interpolator interpolator = DEFAULT_SEARCH_VIEW_OFFSET_CHANGE_INTERPOLATOR;
        valueAnimator.setInterpolator(interpolator);
        this.mSearchViewOffsetExitAnimator.addUpdateListener(new AnonymousClass16());
        ValueAnimator valueAnimatorOfFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mSearchViewScaleExitAnimator = valueAnimatorOfFloat2;
        valueAnimatorOfFloat2.setDuration(450L);
        this.mSearchViewScaleExitAnimator.setInterpolator(interpolator);
        this.mSearchViewScaleExitAnimator.addUpdateListener(new AnonymousClass17());
    }

    private void initSmoothEnterAnimatorSet() {
        AnimatorSet animatorSet = new AnimatorSet();
        this.mSearchViewSmoothEnterAnimatorSet = animatorSet;
        animatorSet.addListener(new AnonymousClass15());
        this.mSearchViewSmoothEnterAnimatorSet.playTogether(this.mSearchViewOffsetEnterAnimator, this.mSearchViewScaleEnterAnimator, this.mButtonAlphaEnterAnimator);
    }

    private void initSmoothExitAnimatorSet() {
        AnimatorSet animatorSet = new AnimatorSet();
        this.mSearchViewSmoothExitAnimatorSet = animatorSet;
        animatorSet.addListener(new AnonymousClass19());
        this.mSearchViewSmoothExitAnimatorSet.playTogether(this.mSearchViewOffsetExitAnimator, this.mSearchViewScaleExitAnimator, this.mButtonAlphaExitAnimator);
    }

    private boolean isInButton(float f3, float f4) {
        return this.mButtonHitRect.contains(f3, f4);
    }

    private boolean isInIcon(float f3, float f4) {
        getGlobalVisibleRect(this.mRect);
        this.mMainIcon.getGlobalVisibleRect(this.mMainIconRect);
        this.mSubIcon.getGlobalVisibleRect(this.mSubIconRect);
        this.mMainIconRect.offset(0, -this.mRect.top);
        this.mSubIconRect.offset(0, -this.mRect.top);
        int i3 = (int) f3;
        int i4 = (int) f4;
        return this.mMainIconRect.contains(i3, i4) || this.mSubIconRect.contains(i3, i4);
    }

    private boolean isInMultiWindowMode() {
        Context context = this.mContext;
        if (context instanceof Activity) {
            return ((Activity) context).isInMultiWindowMode();
        }
        return false;
    }

    private boolean isInView(float f3, float f4) {
        float f5 = (int) f3;
        float f6 = (int) f4;
        return this.mPressFeedbackBackgroundRect.contains(f5, f6) || this.mNormalBackgroundRect.contains(f5, f6);
    }

    private boolean isRtl() {
        return getLayoutDirection() == 1;
    }

    private void loadAttr(Context context, AttributeSet attributeSet, int i3, int i4) {
        Drawable drawable;
        if (attributeSet != null) {
            int styleAttribute = attributeSet.getStyleAttribute();
            this.mStyle = styleAttribute;
            if (styleAttribute == 0) {
                this.mStyle = i3;
            }
        } else {
            this.mStyle = i3;
        }
        this.mBackgroundEndGap = context.getResources().getDimensionPixelOffset(R.dimen.coui_search_view_background_end_gap);
        this.mBackgroundStartGap = context.getResources().getDimensionPixelOffset(R.dimen.coui_search_view_background_start_gap);
        this.mCancelButtonSmallStartMargin = context.getResources().getDimensionPixelOffset(R.dimen.coui_search_view_cancel_margin_small);
        this.mCancelButtonLargeStartMargin = context.getResources().getDimensionPixelOffset(R.dimen.coui_search_view_cancel_margin_large);
        this.mCollapsedMinHeight = context.getResources().getDimensionPixelSize(R.dimen.coui_search_view_collapsed_min_height);
        this.mInitSearchViewWrapperHeight = context.getResources().getDimensionPixelOffset(R.dimen.coui_search_view_wrapper_height);
        this.mInitSearchViewAnimateHeight = context.getResources().getDimensionPixelOffset(R.dimen.coui_search_view_animate_height);
        if (this.mPressFeedbackBackgroundRect == null) {
            this.mPressFeedbackBackgroundRect = new RectF();
        }
        if (this.mNormalBackgroundRect == null) {
            this.mNormalBackgroundRect = new RectF();
        }
        if (this.mRect == null) {
            this.mRect = new Rect();
        }
        if (this.mMainIconRect == null) {
            this.mMainIconRect = new Rect();
        }
        if (this.mSubIconRect == null) {
            this.mSubIconRect = new Rect();
        }
        this.mNormalBackgroundColor = context.getResources().getColor(R.color.coui_search_view_selector_color_normal);
        this.mPressedBackgroundColor = context.getResources().getColor(R.color.coui_search_view_selector_color_pressed);
        this.mCurrentBackgroundColor = this.mNormalBackgroundColor;
        this.mHorizontalDividerColor = context.getResources().getColor(com.support.appcompat.R.color.coui_color_divider);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUISearchViewAnimate, i3, i4);
        float f3 = context.getResources().getConfiguration().fontScale;
        this.mSearchView.getSearchAutoComplete().setTextSize(0, typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUISearchViewAnimate_inputTextSize, getResources().getDimensionPixelSize(R.dimen.coui_search_view_input_text_size)));
        SearchView.SearchAutoComplete searchAutoComplete = this.mSearchView.getSearchAutoComplete();
        searchAutoComplete.setPaddingRelative(0, 0, getResources().getDimensionPixelSize(R.dimen.coui_search_view_auto_complete_padding_end), 0);
        searchAutoComplete.setTextColor(typedArrayObtainStyledAttributes.getColor(R.styleable.COUISearchViewAnimate_inputTextColor, 0));
        this.mSearchIcon.setImageDrawable(MaterialResources.getDrawable(context, typedArrayObtainStyledAttributes, R.styleable.COUISearchViewAnimate_couiSearchIcon));
        this.mSearchView.getSearchAutoComplete().setHintTextColor(typedArrayObtainStyledAttributes.getColorStateList(R.styleable.COUISearchViewAnimate_normalHintColor));
        this.mSearchViewType = typedArrayObtainStyledAttributes.getInt(R.styleable.COUISearchViewAnimate_couiSearchViewAnimateType, 0);
        int i5 = R.styleable.COUISearchViewAnimate_searchHint;
        if (typedArrayObtainStyledAttributes.hasValue(i5)) {
            setQueryHint(typedArrayObtainStyledAttributes.getString(i5));
        }
        int i6 = R.styleable.COUISearchViewAnimate_functionalButtonTextColor;
        if (typedArrayObtainStyledAttributes.hasValue(i6)) {
            this.mFunctionalButton.setTextColor(typedArrayObtainStyledAttributes.getColor(i6, 0));
        }
        int i7 = R.styleable.COUISearchViewAnimate_functionalButtonText;
        if (typedArrayObtainStyledAttributes.hasValue(i7)) {
            this.mFunctionalButton.setText(typedArrayObtainStyledAttributes.getString(i7));
        } else {
            this.mFunctionalButton.setText(R.string.coui_search_view_cancel);
        }
        this.mFunctionalButton.setTextSize(0, COUIChangeTextUtil.getSuitableFontSize(this.mFunctionalButton.getTextSize(), f3, 2));
        COUITextViewCompatUtil.setPressRippleDrawable(this.mFunctionalButton);
        int i8 = R.styleable.COUISearchViewAnimate_buttonDivider;
        if (typedArrayObtainStyledAttributes.hasValue(i8) && (drawable = typedArrayObtainStyledAttributes.getDrawable(i8)) != null) {
            this.mButtonDivider.setImageDrawable(drawable);
        }
        this.mSearchView.setBackgroundColor(typedArrayObtainStyledAttributes.getColor(R.styleable.COUISearchViewAnimate_searchBackground, 0));
        this.mMainIcon = (ImageView) this.mSearchView.findViewById(R.id.search_main_icon_btn);
        this.mSubIcon = (ImageView) this.mSearchView.findViewById(R.id.search_sub_icon_btn);
        this.mMainIcon.setImageDrawable(MaterialResources.getDrawable(context, typedArrayObtainStyledAttributes, R.styleable.COUISearchViewAnimate_couiSearchViewMainIcon));
        this.mSubIcon.setImageDrawable(MaterialResources.getDrawable(context, typedArrayObtainStyledAttributes, R.styleable.COUISearchViewAnimate_couiSearchViewSubIcon));
        this.mCloseBtn = (ImageView) this.mSearchView.findViewById(R.id.search_close_btn);
        int resourceId = typedArrayObtainStyledAttributes.getResourceId(R.styleable.COUISearchViewAnimate_couiSearchClearSelector, 0);
        ImageView imageView = this.mCloseBtn;
        if (imageView != null) {
            imageView.setImageResource(resourceId);
        }
        int i9 = typedArrayObtainStyledAttributes.getInt(R.styleable.COUISearchViewAnimate_android_gravity, 16);
        Log.i(TAG, "gravity " + i9);
        setGravity(i9);
        typedArrayObtainStyledAttributes.recycle();
    }

    private boolean notifyCancelButton() {
        List<OnCancelButtonClickListener> list = this.mOnCancelButtonClickListeners;
        boolean zOnClickCancel = false;
        if (list != null) {
            for (OnCancelButtonClickListener onCancelButtonClickListener : list) {
                if (onCancelButtonClickListener != null) {
                    zOnClickCancel |= onCancelButtonClickListener.onClickCancel();
                }
            }
        }
        return zOnClickCancel;
    }

    private void notifyOnStateChange(int i3, int i4) {
        List<OnStateChangeListener> list = this.mOnStateChangeListeners;
        if (list != null) {
            for (OnStateChangeListener onStateChangeListener : list) {
                if (onStateChangeListener != null) {
                    onStateChangeListener.onStateChange(i3, i4);
                }
            }
        }
    }

    private void onClickStateEdit() {
        getAnimatorHelper().runStateChangeAnimation(0);
    }

    private void onClickStateNormal() {
        getAnimatorHelper().runStateChangeAnimation(1);
    }

    private void playPressAnimation() {
        ObjectAnimator objectAnimator = this.mPressFeedbackAnimator;
        if (objectAnimator != null && objectAnimator.isRunning()) {
            this.mPressFeedbackAnimator.cancel();
        }
        ObjectAnimator objectAnimatorOfInt = ObjectAnimator.ofInt(this, "currentBackgroundColor", this.mCurrentBackgroundColor, this.mPressedBackgroundColor);
        this.mPressFeedbackAnimator = objectAnimatorOfInt;
        objectAnimatorOfInt.setDuration(150L);
        this.mPressFeedbackAnimator.setInterpolator(DEFAULT_PRESS_FEEDBACK_INTERPOLATOR);
        this.mPressFeedbackAnimator.setEvaluator(DEFAULT_PRESS_FEEDBACK_EVALUATOR);
        this.mPressFeedbackAnimator.start();
    }

    private void playReleaseAnimation() {
        ObjectAnimator objectAnimator = this.mPressFeedbackAnimator;
        if (objectAnimator != null && objectAnimator.isRunning()) {
            this.mPressFeedbackAnimator.cancel();
        }
        ObjectAnimator objectAnimatorOfInt = ObjectAnimator.ofInt(this, "currentBackgroundColor", this.mCurrentBackgroundColor, this.mNormalBackgroundColor);
        this.mPressFeedbackAnimator = objectAnimatorOfInt;
        objectAnimatorOfInt.setDuration(150L);
        this.mPressFeedbackAnimator.setInterpolator(DEFAULT_PRESS_FEEDBACK_INTERPOLATOR);
        this.mPressFeedbackAnimator.setEvaluator(DEFAULT_PRESS_FEEDBACK_EVALUATOR);
        this.mPressFeedbackAnimator.start();
    }

    private void removeLast() {
        int childCount = this.mToolBar.getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            if (getClass().isInstance(this.mToolBar.getChildAt(i3))) {
                this.mToolBar.removeViewAt(i3);
                return;
            }
        }
    }

    private void setCurrentBackgroundColor(int i3) {
        this.mCurrentBackgroundColor = i3;
        invalidate();
    }

    private void setMenuItem(MenuItem menuItem) {
        this.mMenuItem = menuItem;
        if (menuItem == null || menuItem.getActionView() != this) {
            return;
        }
        this.mMenuItem.setActionView((View) null);
    }

    private void setRelativeVerticalGravity(View view, int i3) {
        ViewGroup.LayoutParams layoutParams;
        if (view == null || (layoutParams = view.getLayoutParams()) == null || !(layoutParams instanceof RelativeLayout.LayoutParams)) {
            return;
        }
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) layoutParams;
        Arrays.fill(layoutParams2.getRules(), 0);
        layoutParams2.alignWithParent = true;
        int i4 = i3 & 112;
        int i5 = 15;
        if (i4 != 16) {
            if (i4 == 48) {
                i5 = 10;
            } else if (i4 == 80) {
                i5 = 12;
            }
        }
        layoutParams2.addRule(i5);
        view.requestLayout();
    }

    private void setSearchAutoCompleteFocus() {
        SearchView.SearchAutoComplete searchAutoComplete;
        COUISearchView cOUISearchView = this.mSearchView;
        if (cOUISearchView == null || (searchAutoComplete = cOUISearchView.getSearchAutoComplete()) == null) {
            return;
        }
        searchAutoComplete.setFocusable(true);
        searchAutoComplete.setFocusableInTouchMode(true);
        searchAutoComplete.requestFocus();
    }

    private void setSearchAutoCompleteUnFocus() {
        COUISearchView cOUISearchView = this.mSearchView;
        if (cOUISearchView != null) {
            cOUISearchView.clearFocus();
            this.mSearchView.setFocusable(false);
            this.mSearchView.onWindowFocusChanged(false);
            SearchView.SearchAutoComplete searchAutoComplete = this.mSearchView.getSearchAutoComplete();
            if (searchAutoComplete != null) {
                searchAutoComplete.setFocusable(false);
            }
        }
    }

    private void setToolBarAlpha(float f3) {
        COUIToolbar cOUIToolbar = this.mToolBar;
        if (cOUIToolbar != null) {
            int childCount = cOUIToolbar.getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = this.mToolBar.getChildAt(i3);
                if (childAt != this) {
                    childAt.setAlpha(f3);
                }
            }
        }
    }

    private void setToolBarChildVisibility(int i3) {
        COUIToolbar cOUIToolbar = this.mToolBar;
        if (cOUIToolbar != null) {
            int childCount = cOUIToolbar.getChildCount();
            for (int i4 = 0; i4 < childCount; i4++) {
                View childAt = this.mToolBar.getChildAt(i4);
                if (childAt != this) {
                    childAt.setVisibility(i3);
                }
            }
        }
    }

    private void showSoftInput() {
        CustomWindowInsetsAnimationControlListener customWindowInsetsAnimationControlListener = new CustomWindowInsetsAnimationControlListener(true, this.mShowImeAnimDuration, this.mShowImeInterpolator);
        COUISearchView cOUISearchView = this.mSearchView;
        if (cOUISearchView == null || cOUISearchView.getWindowInsetsController() == null) {
            return;
        }
        this.mSearchView.getWindowInsetsController().controlWindowInsetsAnimation(WindowInsets.Type.ime(), this.mShowImeAnimDuration, this.mShowImeInterpolator, null, customWindowInsetsAnimationControlListener);
    }

    private static String state2String(int i3) {
        if (i3 != 0) {
            return i3 != 1 ? String.valueOf(i3) : "state edit";
        }
        return "state normal";
    }

    private void updateBackgroundRect() {
        if (!this.mDivideBackground) {
            if (isRtl()) {
                this.mPressFeedbackBackgroundRect.right = this.mSearchViewWrapper.getRight();
                int i3 = this.mSearchViewType;
                if (i3 == 0) {
                    this.mPressFeedbackBackgroundRect.left = this.mSearchView.getLeft() + getPaddingEnd();
                } else if (i3 == 1) {
                    this.mPressFeedbackBackgroundRect.left = this.mSearchViewWrapper.getLeft();
                }
            } else {
                this.mPressFeedbackBackgroundRect.left = this.mSearchViewWrapper.getLeft();
                int i4 = this.mSearchViewType;
                if (i4 == 0) {
                    this.mPressFeedbackBackgroundRect.right = this.mSearchView.getRight() + getPaddingStart();
                } else if (i4 == 1) {
                    this.mPressFeedbackBackgroundRect.right = this.mSearchViewWrapper.getRight();
                }
            }
            this.mPressFeedbackBackgroundRect.top = this.mSearchViewWrapper.getTop();
            this.mPressFeedbackBackgroundRect.bottom = this.mSearchViewWrapper.getBottom();
            this.mNeedUpdatePressFeedbackRectPath = true;
            return;
        }
        if (isRtl()) {
            this.mPressFeedbackBackgroundRect.right = this.mSearchViewWrapper.getRight();
            this.mPressFeedbackBackgroundRect.left = this.mButtonDivider.getRight() + this.mSearchViewWrapper.getLeft();
        } else {
            this.mPressFeedbackBackgroundRect.left = this.mSearchViewWrapper.getLeft();
            this.mPressFeedbackBackgroundRect.right = this.mButtonDivider.getLeft() + this.mPressFeedbackBackgroundRect.left;
        }
        this.mPressFeedbackBackgroundRect.top = this.mSearchViewWrapper.getTop();
        this.mPressFeedbackBackgroundRect.bottom = this.mSearchViewWrapper.getBottom();
        this.mNeedUpdatePressFeedbackRectPath = true;
        if (isRtl()) {
            RectF rectF = this.mNormalBackgroundRect;
            rectF.right = this.mPressFeedbackBackgroundRect.left;
            rectF.left = this.mSearchViewWrapper.getLeft();
        } else {
            RectF rectF2 = this.mNormalBackgroundRect;
            rectF2.left = this.mPressFeedbackBackgroundRect.right;
            rectF2.right = this.mSearchViewWrapper.getRight();
        }
        RectF rectF3 = this.mNormalBackgroundRect;
        RectF rectF4 = this.mPressFeedbackBackgroundRect;
        rectF3.top = rectF4.top;
        rectF3.bottom = rectF4.bottom;
        this.mNeedUpdateNormalRectPath = true;
    }

    private void updateBounds() {
        this.mFunctionalButton.getLocationOnScreen(this.mButtonLocation);
        getLocationOnScreen(this.mLocation);
        RectF rectF = this.mButtonHitRect;
        int[] iArr = this.mButtonLocation;
        int i3 = iArr[0];
        rectF.set(i3, iArr[1] - this.mLocation[1], i3 + this.mFunctionalButton.getWidth(), (this.mButtonLocation[1] - this.mLocation[1]) + this.mFunctionalButton.getHeight());
        this.mFunctionalButton.post(new AnonymousClass3());
    }

    private void updatePath() {
        RectF rectF = this.mPressFeedbackBackgroundRect;
        float f3 = (rectF.bottom - rectF.top) / 2.0f;
        boolean zIsRtl = isRtl();
        if (this.mNeedUpdateNormalRectPath) {
            COUIShapePath.getRoundRectPath(this.mNormalBackgroundPath, this.mNormalBackgroundRect, f3, zIsRtl, !zIsRtl, zIsRtl, !zIsRtl);
            this.mNeedUpdateNormalRectPath = false;
        }
        if (this.mNeedUpdatePressFeedbackRectPath) {
            if (this.mDivideBackground) {
                COUIShapePath.getRoundRectPath(this.mPressFeedbackBackgroundPath, this.mPressFeedbackBackgroundRect, f3, !zIsRtl, zIsRtl, !zIsRtl, zIsRtl);
            } else {
                COUIShapePath.getRoundRectPath(this.mPressFeedbackBackgroundPath, this.mPressFeedbackBackgroundRect, f3, true, true, true, true);
            }
            this.mNeedUpdatePressFeedbackRectPath = false;
        }
    }

    @Deprecated
    public void addOnCancelButtonClickListener(OnCancelButtonClickListener onCancelButtonClickListener) {
        List<OnCancelButtonClickListener> listEnsureList = ensureList(this.mOnCancelButtonClickListeners);
        this.mOnCancelButtonClickListeners = listEnsureList;
        listEnsureList.add(onCancelButtonClickListener);
    }

    public void addOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        List<OnStateChangeListener> listEnsureList = ensureList(this.mOnStateChangeListeners);
        this.mOnStateChangeListeners = listEnsureList;
        listEnsureList.add(onStateChangeListener);
    }

    @Override // android.view.ViewGroup
    public void addView(View view) {
        super.addView(view);
    }

    public void changeStateImmediately(int i3) {
        Log.d(TAG, "changeStateImmediately: " + state2String(i3));
        post(new AnonymousClass4(i3));
    }

    public void changeStateWithAnimation(int i3) {
        if (this.mState.get() == i3) {
            Log.d(TAG, "changeStateWithAnimation: same state , return. targetState = " + i3);
            return;
        }
        if (this.mState.get() == 1) {
            onClickStateEdit();
        } else if (this.mState.get() == 0) {
            onClickStateNormal();
        }
    }

    public void controlImeShowAnim(int i3, Interpolator interpolator) {
        this.mShowImeAnimDuration = i3;
        this.mShowImeInterpolator = interpolator;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEventPreIme(KeyEvent keyEvent) {
        OnDispatchKeyEventPreImeListener onDispatchKeyEventPreImeListener = this.mOnDispatchKeyEventPreImeListener;
        if (onDispatchKeyEventPreImeListener != null) {
            onDispatchKeyEventPreImeListener.onDispatchKeyEventPreIme(keyEvent);
        }
        return super.dispatchKeyEventPreIme(keyEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return super.dispatchTouchEvent(motionEvent);
        }
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action == 1 || action == 3) {
                if (isInView(motionEvent.getX(), motionEvent.getY()) || this.mPressed) {
                    this.mPressed = false;
                    playReleaseAnimation();
                }
            } else if (!isInView(motionEvent.getX(), motionEvent.getY()) && this.mPressed) {
                this.mPressed = false;
                playReleaseAnimation();
            }
        } else {
            if (motionEvent.getY() < this.mSearchViewWrapper.getTop() || motionEvent.getY() > this.mSearchViewWrapper.getBottom()) {
                return false;
            }
            if (isInView(motionEvent.getX(), motionEvent.getY()) && !isInButton(motionEvent.getX(), motionEvent.getY())) {
                this.mPressed = true;
                playPressAnimation();
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public long getAnimatorDuration() {
        return 150L;
    }

    public boolean getCancelIconAnimating() {
        return this.mToolBarAnimationRunning;
    }

    public SearchFunctionalButton getFunctionalButton() {
        return this.mFunctionalButton;
    }

    @Override // android.widget.LinearLayout
    public int getGravity() {
        return this.mGravity;
    }

    public boolean getInputMethodAnimationEnabled() {
        return this.mInputMethodAnimationEnabled;
    }

    public ImageView getMainIconView() {
        return this.mMainIcon;
    }

    public int getSearchState() {
        return this.mState.get();
    }

    public COUISearchView getSearchView() {
        return this.mSearchView;
    }

    public float getSearchViewAnimateHeightPercent() {
        return this.mCollapsingHeightPercent;
    }

    public ImageView getSubIconView() {
        return this.mSubIcon;
    }

    public void hideInToolBar() {
        if (this.mToolBarAnimationRunning) {
            return;
        }
        this.mToolBarAnimationRunning = true;
        ensureAddedToToolBar();
        if (this.mAddToToolbarWay == 1) {
            animate().alpha(0.0f).setDuration(150L).setListener(new AnonymousClass8()).start();
        }
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimatorOfFloat.setDuration(150L);
        valueAnimatorOfFloat.addUpdateListener(new AnonymousClass9());
        valueAnimatorOfFloat.addListener(new AnonymousClass10());
        valueAnimatorOfFloat.start();
        if (this.mInputMethodAnimationEnabled) {
            openSoftInput(false);
        }
    }

    @Deprecated
    public boolean isIconCanAnimate() {
        return this.mSearchIconCanAnimate;
    }

    @Override // androidx.appcompat.view.CollapsibleActionView
    public void onActionViewCollapsed() {
    }

    @Override // androidx.appcompat.view.CollapsibleActionView
    public void onActionViewExpanded() {
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onDraw(Canvas canvas) {
        if (this.mNeedUpdateNormalRectPath || this.mNeedUpdatePressFeedbackRectPath) {
            updatePath();
        }
        Paint paint = this.mPressFeedbackPaint;
        Paint.Style style = Paint.Style.FILL;
        paint.setStyle(style);
        this.mNormalPaint.setStyle(style);
        int iSave = canvas.save();
        if (this.mDivideBackground) {
            this.mNormalPaint.setColor(this.mCurrentBackgroundColor);
            canvas.drawPath(this.mNormalBackgroundPath, this.mNormalPaint);
        }
        this.mPressFeedbackPaint.setColor(this.mCurrentBackgroundColor);
        canvas.drawPath(this.mPressFeedbackBackgroundPath, this.mPressFeedbackPaint);
        canvas.restoreToCount(iSave);
        super.onDraw(canvas);
    }

    @Override // com.coui.appcompat.searchview.ImeInsetsAnimationCallback.OnImeAnimationListener
    public void onImeAnimStart() {
        if (this.mSearchView.getRootWindowInsets() != null && this.mSearchView.getRootWindowInsets().isVisible(WindowInsets.Type.ime()) && this.mState.get() == 0) {
            this.mState.set(1);
            this.mSearchViewSmoothEnterAnimatorSet.start();
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (isInIcon(motionEvent.getX(), motionEvent.getY())) {
            return false;
        }
        if (this.mState.get() != 0 || isInButton(motionEvent.getX(), motionEvent.getY())) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        return true;
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        super.onLayout(z2, i3, i4, i5, i6);
        updateBackgroundRect();
        updateBounds();
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        super.onMeasure(i3, i4);
        if (this.mSearchViewType == 1) {
            int measuredWidth = (this.mBackgroundEndGap * 2) + this.mFunctionalButton.getMeasuredWidth() + this.mButtonDivider.getMeasuredWidth();
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mSearchView.getLayoutParams();
            if (marginLayoutParams.getMarginEnd() != measuredWidth) {
                marginLayoutParams.setMarginEnd(measuredWidth);
                this.mSearchView.setLayoutParams(marginLayoutParams);
            }
        }
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof COUISavedState) {
            setSearchViewAnimateHeightPercent(((COUISavedState) parcelable).mCollapsingHeightPercent);
        }
        super.onRestoreInstanceState(parcelable);
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        COUISavedState cOUISavedState = new COUISavedState(super.onSaveInstanceState());
        cOUISavedState.mCollapsingHeightPercent = this.mCollapsingHeightPercent;
        return cOUISavedState;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean zOnTouchEvent = super.onTouchEvent(motionEvent);
        if (this.mPressFeedbackBackgroundRect.contains(motionEvent.getX(), motionEvent.getY())) {
            return true;
        }
        return zOnTouchEvent;
    }

    public void openSoftInput(boolean z2) {
        COUISearchView cOUISearchView = this.mSearchView;
        if (cOUISearchView == null || cOUISearchView.getSearchAutoComplete() == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(SystemUpdateScreenStateConfig.KEY_INPUT_METHOD);
        Log.d(TAG, "openSoftInput: " + z2);
        if (!z2) {
            if (inputMethodManager == null || !inputMethodManager.isActive()) {
                return;
            }
            inputMethodManager.hideSoftInputFromWindow(this.mSearchView.getSearchAutoComplete().getWindowToken(), 0);
            return;
        }
        setSearchAutoCompleteFocus();
        if (inputMethodManager != null) {
            if (!this.mIsAtLeastR || this.mShowImeAnimDuration == 0) {
                inputMethodManager.showSoftInput(this.mSearchView.getSearchAutoComplete(), 0);
            } else {
                showSoftInput();
            }
        }
    }

    public void refresh() {
        TypedArray typedArrayObtainStyledAttributes = null;
        String resourceTypeName = this.mStyle != 0 ? getResources().getResourceTypeName(this.mStyle) : null;
        if ("attr".equals(resourceTypeName)) {
            typedArrayObtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(null, R.styleable.COUISearchViewAnimate, this.mStyle, 0);
        } else if (BaseConfig.KEY_STYLE.equals(resourceTypeName)) {
            typedArrayObtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(null, R.styleable.COUISearchViewAnimate, 0, this.mStyle);
        }
        if (typedArrayObtainStyledAttributes != null) {
            this.mNormalBackgroundColor = getContext().getResources().getColor(R.color.coui_search_view_selector_color_normal);
            this.mPressedBackgroundColor = getContext().getResources().getColor(R.color.coui_search_view_selector_color_pressed);
            this.mCurrentBackgroundColor = this.mNormalBackgroundColor;
            this.mSearchView.getSearchAutoComplete().setTextColor(typedArrayObtainStyledAttributes.getColor(R.styleable.COUISearchViewAnimate_inputTextColor, 0));
            int i3 = R.styleable.COUISearchViewAnimate_couiSearchIcon;
            Drawable drawable = typedArrayObtainStyledAttributes.getDrawable(i3);
            if (typedArrayObtainStyledAttributes.hasValue(i3)) {
                this.mSearchIcon.setImageDrawable(drawable);
            } else {
                this.mSearchIcon.setImageDrawable(drawable);
            }
            this.mSearchView.getSearchAutoComplete().setHintTextColor(typedArrayObtainStyledAttributes.getColorStateList(R.styleable.COUISearchViewAnimate_normalHintColor));
            this.mSearchView.setBackgroundColor(typedArrayObtainStyledAttributes.getColor(R.styleable.COUISearchViewAnimate_searchBackground, 0));
            this.mCloseBtn = (ImageView) this.mSearchView.findViewById(R.id.search_close_btn);
            Drawable drawable2 = typedArrayObtainStyledAttributes.getDrawable(R.styleable.COUISearchViewAnimate_couiSearchClearSelector);
            ImageView imageView = this.mCloseBtn;
            if (imageView != null) {
                imageView.setImageDrawable(drawable2);
            }
            Drawable drawable3 = typedArrayObtainStyledAttributes.getDrawable(R.styleable.COUISearchViewAnimate_buttonDivider);
            if (drawable3 != null) {
                this.mButtonDivider.setImageDrawable(drawable3);
            }
            COUITextViewCompatUtil.setPressRippleDrawable(this.mFunctionalButton);
            typedArrayObtainStyledAttributes.recycle();
        }
        if (this.mSearchViewShrinkWidth != 0) {
            this.mSearchViewShrinkWidth = getOriginWidth() - getShrinkWidth();
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mSearchView.getLayoutParams();
            marginLayoutParams.setMarginEnd(this.mSearchViewShrinkWidth);
            this.mSearchView.setLayoutParams(marginLayoutParams);
        }
    }

    @Deprecated
    public void removeHintViewLayoutOnClickListener() {
    }

    public void setAtBehindToolBar(COUIToolbar cOUIToolbar, int i3, MenuItem menuItem) {
        this.mToolBar = cOUIToolbar;
        this.mGravityInToolBar = i3;
        this.mAddToToolbarWay = 1;
        setMenuItem(menuItem);
        this.mShouldClearFocus = false;
        changeStateImmediately(1);
        setVisibility(8);
    }

    public void setAtFrontToolBar(COUIToolbar cOUIToolbar, int i3, MenuItem menuItem) {
        this.mToolBar = cOUIToolbar;
        this.mGravityInToolBar = i3;
        this.mAddToToolbarWay = 2;
        setMenuItem(menuItem);
        ensureAddedToToolBar();
        menuItem.setVisible(false);
        addOnStateChangeListener(this.mAtToolbarFrontStateChangeListener);
    }

    public void setCancelButtonBackground(Drawable drawable) {
        this.mFunctionalButton.setBackground(drawable);
    }

    public void setCancelDividerImageDrawable(Drawable drawable) {
        this.mButtonDivider.setImageDrawable(drawable);
    }

    public void setCloseBtnBackground(Drawable drawable) {
        this.mCloseBtn.setBackground(drawable);
    }

    public void setCloseBtnImageDrawable(Drawable drawable) {
        this.mCloseBtn.setImageDrawable(drawable);
    }

    @Override // android.view.View
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        ImageView imageView = this.mSearchIcon;
        if (imageView != null) {
            imageView.setEnabled(z2);
        }
        COUISearchView cOUISearchView = this.mSearchView;
        if (cOUISearchView != null) {
            cOUISearchView.setEnabled(z2);
        }
        SearchFunctionalButton searchFunctionalButton = this.mFunctionalButton;
        if (searchFunctionalButton != null) {
            searchFunctionalButton.setEnabled(z2);
        }
    }

    public void setExtraActivateMarginTop(int i3) {
        this.mExtraY = i3;
    }

    public void setFunctionalButtonText(String str) {
        this.mFunctionalButton.setText(str);
    }

    @Override // android.widget.LinearLayout
    public void setGravity(int i3) {
        if (this.mGravity != i3) {
            if ((8388615 & i3) == 0) {
                i3 |= 8388611;
            }
            if ((i3 & 112) == 0) {
                i3 |= 16;
            }
            this.mGravity = i3;
        }
    }

    @Deprecated
    public void setHintTextViewHintTextColor(int i3) {
    }

    @Deprecated
    public void setHintTextViewTextColor(int i3) {
    }

    @Deprecated
    public void setHintViewBackground(Drawable drawable) {
    }

    @Deprecated
    public void setHintViewLayoutOnClickListener(View.OnClickListener onClickListener) {
    }

    @Deprecated
    public void setIconCanAnimate(boolean z2) {
        this.mSearchIconCanAnimate = z2;
    }

    public void setImeInsetsAnimationCallback() {
        if (this.mIsAtLeastR) {
            this.mShowImeAnimDuration = 450;
            this.mShowImeInterpolator = DEFAULT_SEARCH_VIEW_OFFSET_CHANGE_INTERPOLATOR;
            ImeInsetsAnimationCallback imeInsetsAnimationCallback = new ImeInsetsAnimationCallback();
            imeInsetsAnimationCallback.setImeAnimationListener(this);
            this.mSearchView.post(new p(this, imeInsetsAnimationCallback));
        }
    }

    public void setInputHintTextColor(int i3) {
        this.mSearchView.getSearchAutoComplete().setHintTextColor(i3);
    }

    public void setInputMethodAnimationEnabled(boolean z2) {
        this.mInputMethodAnimationEnabled = z2;
    }

    public void setInputTextColor(int i3) {
        this.mSearchView.getSearchAutoComplete().setTextColor(i3);
    }

    public void setMainIconDrawable(Drawable drawable) {
        this.mMainIcon.setImageDrawable(drawable);
    }

    public void setOnAnimationListener(OnAnimationListener onAnimationListener) {
        this.mOnAnimationListener = onAnimationListener;
    }

    public void setOnDispatchKeyEventPreImeListener(OnDispatchKeyEventPreImeListener onDispatchKeyEventPreImeListener) {
        this.mOnDispatchKeyEventPreImeListener = onDispatchKeyEventPreImeListener;
    }

    public void setQueryHint(CharSequence charSequence) {
        COUISearchView cOUISearchView = this.mSearchView;
        if (cOUISearchView != null) {
            cOUISearchView.setQueryHint(charSequence);
        }
    }

    public void setSearchAnimateType(int i3) {
        if (this.mState.get() == 1) {
            Log.d(TAG, "setSearchAnimateType to " + TYPE_NAME[i3] + " is not allowed in STATE_EDIT");
            return;
        }
        this.mSearchViewType = i3;
        if (i3 == 0) {
            this.mButtonDivider.setVisibility(8);
            this.mFunctionalButton.setVisibility(4);
            this.mFunctionalButton.setAlpha(0.0f);
            ((ViewGroup.MarginLayoutParams) this.mFunctionalButton.getLayoutParams()).setMarginStart(this.mCancelButtonLargeStartMargin);
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mSearchView.getLayoutParams();
            marginLayoutParams.setMarginEnd(0);
            this.mSearchView.setLayoutParams(marginLayoutParams);
            return;
        }
        if (i3 == 1) {
            this.mButtonDivider.setVisibility(8);
            this.mFunctionalButton.setVisibility(8);
            ((ViewGroup.MarginLayoutParams) this.mFunctionalButton.getLayoutParams()).setMarginStart(this.mCancelButtonSmallStartMargin);
            ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) this.mSearchView.getLayoutParams();
            marginLayoutParams2.setMarginEnd(getOriginWidth() - getShrinkWidth());
            this.mSearchView.setLayoutParams(marginLayoutParams2);
        }
    }

    public void setSearchBackgroundColor(ColorStateList colorStateList) {
        if (colorStateList != null) {
            int i3 = this.mNormalBackgroundColor;
            int defaultColor = colorStateList.getDefaultColor();
            this.mNormalBackgroundColor = defaultColor;
            this.mPressedBackgroundColor = colorStateList.getColorForState(new int[]{16842919}, defaultColor);
            if (this.mCurrentBackgroundColor == i3) {
                this.mCurrentBackgroundColor = this.mNormalBackgroundColor;
            }
            invalidate();
        }
    }

    public void setSearchViewAnimateHeightPercent(float f3) {
        this.mCollapsingHeightPercent = f3;
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) getChildAt(0).getLayoutParams();
        marginLayoutParams.height = (int) Float.max(this.mCollapsedMinHeight, this.mInitSearchViewWrapperHeight * clampSearchViewHeight(f3));
        marginLayoutParams.setMarginStart((int) (getPaddingStart() * (1.0f - clampMarginValue(f3)) * (-1.0f)));
        marginLayoutParams.setMarginEnd((int) (getPaddingEnd() * (1.0f - clampMarginValue(f3)) * (-1.0f)));
        getChildAt(0).setLayoutParams(marginLayoutParams);
        setTranslationY((this.mInitSearchViewAnimateHeight / 2.0f) * (1.0f - f3));
        float f4 = (f3 - 0.5f) * 2.0f;
        this.mSearchView.setAlpha(f4);
        this.mSearchIcon.setAlpha(f4);
        this.mCurrentBackgroundColor = ((Integer) this.mEvaluator.evaluate(clampMarginValue(f3), Integer.valueOf(this.mHorizontalDividerColor), Integer.valueOf(this.mNormalBackgroundColor))).intValue();
    }

    public void setSearchViewBackgroundColor(int i3) {
        this.mSearchView.setBackgroundColor(i3);
    }

    public void setSearchViewIcon(Drawable drawable) {
        this.mSearchIcon.setImageDrawable(drawable);
    }

    public void setSubIconDrawable(Drawable drawable) {
        this.mSubIcon.setImageDrawable(drawable);
    }

    public void showInToolBar() {
        if (this.mToolBarAnimationRunning) {
            return;
        }
        this.mToolBarAnimationRunning = true;
        ensureAddedToToolBar();
        if (this.mAddToToolbarWay == 1) {
            setVisibility(0);
            setAlpha(0.0f);
            int i3 = this.mSearchViewType;
            if (i3 == 0) {
                this.mFunctionalButton.setVisibility(0);
                this.mButtonDivider.setVisibility(8);
            } else if (i3 == 1) {
                this.mFunctionalButton.setVisibility(0);
                this.mButtonDivider.setVisibility(0);
            }
            post(new AnonymousClass5());
            animate().alpha(1.0f).setDuration(150L).setListener(null).start();
        }
        setToolBarChildVisibility(8);
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
        valueAnimatorOfFloat.setDuration(150L);
        valueAnimatorOfFloat.addUpdateListener(new AnonymousClass6());
        valueAnimatorOfFloat.addListener(new AnonymousClass7());
        valueAnimatorOfFloat.start();
        setSearchAutoCompleteUnFocus();
        if (this.mInputMethodAnimationEnabled) {
            openSoftInput(true);
        }
    }

    public COUISearchViewAnimate(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiSearchViewAnimateStyle);
    }

    public COUISearchViewAnimate(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, COUIContextUtil.isCOUIDarkTheme(context) ? R.style.Widget_COUI_COUISearchViewAnimate_Dark : R.style.Widget_COUI_COUISearchViewAnimate);
    }

    public COUISearchViewAnimate(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mPressFeedbackBackgroundPath = new Path();
        this.mNormalBackgroundPath = new Path();
        this.mPressFeedbackPaint = new Paint(1);
        this.mNormalPaint = new Paint(1);
        this.mButtonLocation = new int[2];
        this.mLocation = new int[2];
        this.mEvaluator = new ArgbEvaluator();
        this.mButtonHitRect = new RectF();
        Rect rect = new Rect();
        this.mWrapperBounds = rect;
        this.mState = new AtomicInteger(0);
        this.mGravityInToolBar = 48;
        this.mAddToToolbarWay = 0;
        this.mSearchIconCanAnimate = true;
        this.mShouldClearFocus = true;
        this.mInputMethodAnimationEnabled = true;
        this.mTopOffset = 0;
        this.mExtraY = 0;
        this.mSearchViewType = 1;
        this.mCollapsingHeightPercent = 1.0f;
        this.mDivideBackground = false;
        this.mNeedUpdatePressFeedbackRectPath = true;
        this.mNeedUpdateNormalRectPath = true;
        this.mShowImeAnimDuration = 0;
        this.mShowImeInterpolator = null;
        this.mPressed = false;
        this.mAtToolbarFrontStateChangeListener = new AnonymousClass1();
        this.mGravity = 16;
        this.mContext = context;
        COUIDarkModeUtil.setForceDarkAllow(this, false);
        this.mIsAtLeastR = true;
        inflateView(context, attributeSet);
        loadAttr(context, attributeSet, i3, i4);
        setClipToPadding(false);
        setClipChildren(false);
        setWillNotDraw(false);
        initAnimator();
        setLayoutDirection(3);
        setSearchAnimateType(this.mSearchViewType);
        setTouchDelegate(new TouchDelegate(rect, this.mFunctionalButton));
        this.mSearchView.getSearchAutoComplete().addTextChangedListener(new AnonymousClass2());
    }
}