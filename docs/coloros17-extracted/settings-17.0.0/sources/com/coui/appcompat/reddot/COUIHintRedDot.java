package com.coui.appcompat.reddot;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import com.coui.appcompat.animation.COUIMoveEaseInterpolator;
import com.support.reddot.R;

/* JADX INFO: loaded from: classes.dex */
public class COUIHintRedDot extends View {
    public static final int CONSTANT_VALUE_3 = 3;
    public static final int CONSTANT_VALUE_4 = 4;
    public static final int MAX_ALPHA_VALUE = 255;
    public static final int MIN_ALPHA_VALUE = 0;
    public static final int NO_POINT_MODE = 0;
    public static final long NUM_CHANGE_ALPHA_ANIM_DURATION = 150;
    public static final long NUM_CHANGE_WIDTH_ANIM_DURATION = 517;
    public static final Interpolator NUM_CHANGE_WIDTH_ANIM_INTERPOLATOR = new COUIMoveEaseInterpolator();
    public static final int POINT_NAVI_WITH_NUM = 3;
    public static final int POINT_NUM_MODE_STROKE = 5;
    public static final int POINT_ONLY_MODE = 1;
    public static final int POINT_ONLY_MODE_STROKE = 4;
    public static final int POINT_WITH_NUM_MODE = 2;
    public static final long RED_POINT_ANIM_DURATION = 520;
    public static final int TYPE_BIG_RECT_RADIUS = 2;
    public static final int TYPE_SMALL_RECT_RADIUS = 1;
    private ValueAnimator mAlphaAnim;
    private COUIHintRedDotHelper mCOUIHintRedDotHelper;
    private boolean mIsExecutingAlphaAnim;
    private boolean mIsExecutingWidthAnim;
    private boolean mIsLaidOut;
    private String mMoreText;
    private int mPointMode;
    private int mPointNumber;
    private String mPointText;
    private RectF mRectF;
    private String mRedDotDescription;
    private int mRedDotWithNumberDescriptionId;
    private Drawable mStrokeBackground;
    private int mTempPointNumber;
    private int mTempWidth;
    private int mTextPaintAlpha;
    private ValueAnimator mWidthAnim;

    /* JADX INFO: renamed from: com.coui.appcompat.reddot.COUIHintRedDot$1, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass1 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass1() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIHintRedDot.access$002(COUIHintRedDot.this, ((Integer) valueAnimator.getAnimatedValue()).intValue());
            COUIHintRedDot.this.requestLayout();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.reddot.COUIHintRedDot$2, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass2 extends AnimatorListenerAdapter {
        public AnonymousClass2() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            COUIHintRedDot.access$102(COUIHintRedDot.this, false);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUIHintRedDot.access$102(COUIHintRedDot.this, false);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUIHintRedDot.access$102(COUIHintRedDot.this, true);
            COUIHintRedDot.access$200(COUIHintRedDot.this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.reddot.COUIHintRedDot$3, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass3 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass3() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIHintRedDot.access$302(COUIHintRedDot.this, ((Integer) valueAnimator.getAnimatedValue()).intValue());
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.reddot.COUIHintRedDot$4, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass4 extends AnimatorListenerAdapter {
        public AnonymousClass4() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            COUIHintRedDot.access$402(COUIHintRedDot.this, false);
            COUIHintRedDot cOUIHintRedDot = COUIHintRedDot.this;
            COUIHintRedDot.access$502(cOUIHintRedDot, COUIHintRedDot.access$600(cOUIHintRedDot));
            COUIHintRedDot cOUIHintRedDot2 = COUIHintRedDot.this;
            COUIHintRedDot.access$702(cOUIHintRedDot2, String.valueOf(COUIHintRedDot.access$500(cOUIHintRedDot2)));
            COUIHintRedDot.access$602(COUIHintRedDot.this, 0);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUIHintRedDot.access$402(COUIHintRedDot.this, false);
            COUIHintRedDot cOUIHintRedDot = COUIHintRedDot.this;
            COUIHintRedDot.access$502(cOUIHintRedDot, COUIHintRedDot.access$600(cOUIHintRedDot));
            COUIHintRedDot cOUIHintRedDot2 = COUIHintRedDot.this;
            COUIHintRedDot.access$702(cOUIHintRedDot2, String.valueOf(COUIHintRedDot.access$500(cOUIHintRedDot2)));
            COUIHintRedDot.access$602(COUIHintRedDot.this, 0);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUIHintRedDot.access$402(COUIHintRedDot.this, true);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.reddot.COUIHintRedDot$5, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass5 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass5() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            Float f3 = (Float) valueAnimator.getAnimatedValue();
            if (COUIHintRedDot.this.getVisibility() != 8) {
                COUIHintRedDot.this.setScaleX(f3.floatValue());
                COUIHintRedDot.this.setScaleY(f3.floatValue());
                COUIHintRedDot.this.invalidate();
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.reddot.COUIHintRedDot$6, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass6 implements Animator.AnimatorListener {
        final /* synthetic */ boolean val$isShow;

        public AnonymousClass6(boolean z2) {
            this.val$isShow = z2;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (this.val$isShow) {
                return;
            }
            COUIHintRedDot.this.setPointMode(0);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (this.val$isShow) {
                return;
            }
            COUIHintRedDot.this.setVisibility(8);
            COUIHintRedDot.this.setPointMode(0);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (this.val$isShow) {
                COUIHintRedDot.this.setVisibility(0);
                COUIHintRedDot.this.requestLayout();
            }
        }
    }

    public COUIHintRedDot(Context context) {
        this(context, null);
    }

    public static /* synthetic */ int access$002(COUIHintRedDot cOUIHintRedDot, int i3) {
        cOUIHintRedDot.mTempWidth = i3;
        return i3;
    }

    public static /* synthetic */ boolean access$102(COUIHintRedDot cOUIHintRedDot, boolean z2) {
        cOUIHintRedDot.mIsExecutingWidthAnim = z2;
        return z2;
    }

    public static /* synthetic */ void access$200(COUIHintRedDot cOUIHintRedDot) {
        cOUIHintRedDot.executeAlphaAnim();
    }

    public static /* synthetic */ int access$302(COUIHintRedDot cOUIHintRedDot, int i3) {
        cOUIHintRedDot.mTextPaintAlpha = i3;
        return i3;
    }

    public static /* synthetic */ boolean access$402(COUIHintRedDot cOUIHintRedDot, boolean z2) {
        cOUIHintRedDot.mIsExecutingAlphaAnim = z2;
        return z2;
    }

    public static /* synthetic */ int access$500(COUIHintRedDot cOUIHintRedDot) {
        return cOUIHintRedDot.mPointNumber;
    }

    public static /* synthetic */ int access$502(COUIHintRedDot cOUIHintRedDot, int i3) {
        cOUIHintRedDot.mPointNumber = i3;
        return i3;
    }

    public static /* synthetic */ int access$600(COUIHintRedDot cOUIHintRedDot) {
        return cOUIHintRedDot.mTempPointNumber;
    }

    public static /* synthetic */ int access$602(COUIHintRedDot cOUIHintRedDot, int i3) {
        cOUIHintRedDot.mTempPointNumber = i3;
        return i3;
    }

    public static /* synthetic */ String access$702(COUIHintRedDot cOUIHintRedDot, String str) {
        cOUIHintRedDot.mPointText = str;
        return str;
    }

    private void cancelAnim() {
        ValueAnimator valueAnimator = this.mWidthAnim;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mWidthAnim.end();
        }
        ValueAnimator valueAnimator2 = this.mAlphaAnim;
        if (valueAnimator2 == null || !valueAnimator2.isRunning()) {
            return;
        }
        this.mAlphaAnim.end();
    }

    private void executeAlphaAnim() {
        if (this.mAlphaAnim == null) {
            ValueAnimator valueAnimatorOfInt = ValueAnimator.ofInt(255, 0);
            this.mAlphaAnim = valueAnimatorOfInt;
            valueAnimatorOfInt.setDuration(150L);
            this.mAlphaAnim.addUpdateListener(new AnonymousClass3());
            this.mAlphaAnim.addListener(new AnonymousClass4());
        }
        this.mAlphaAnim.start();
    }

    private void executeWidthAnim(int i3, int i4) {
        ValueAnimator valueAnimatorOfInt = ValueAnimator.ofInt(this.mCOUIHintRedDotHelper.getViewWidth(this.mPointMode, i3), this.mCOUIHintRedDotHelper.getViewWidth(this.mPointMode, i4));
        this.mWidthAnim = valueAnimatorOfInt;
        valueAnimatorOfInt.setDuration(517L);
        this.mWidthAnim.setInterpolator(NUM_CHANGE_WIDTH_ANIM_INTERPOLATOR);
        this.mWidthAnim.addUpdateListener(new AnonymousClass1());
        this.mWidthAnim.addListener(new AnonymousClass2());
        this.mWidthAnim.start();
    }

    public void changePointNumber(int i3) {
        int i4;
        if (getVisibility() == 8 || (i4 = this.mPointMode) == 0 || i4 == 1 || i4 == 4 || i4 == 5 || this.mPointNumber == i3 || i3 <= 0 || this.mCOUIHintRedDotHelper == null) {
            return;
        }
        cancelAnim();
        if (!this.mIsLaidOut) {
            setPointNumber(i3);
        } else {
            this.mTempPointNumber = i3;
            executeWidthAnim(this.mPointNumber, i3);
        }
    }

    public void executeScaleAnim(boolean z2) {
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(z2 ? 0.0f : 1.0f, z2 ? 1.0f : 0.0f);
        valueAnimatorOfFloat.setDuration(520L);
        valueAnimatorOfFloat.setInterpolator(NUM_CHANGE_WIDTH_ANIM_INTERPOLATOR);
        valueAnimatorOfFloat.addUpdateListener(new AnonymousClass5());
        valueAnimatorOfFloat.addListener(new AnonymousClass6(z2));
        valueAnimatorOfFloat.start();
    }

    public boolean getIsLaidOut() {
        return this.mIsLaidOut;
    }

    public int getPointMode() {
        return this.mPointMode;
    }

    public int getPointNumber() {
        return this.mPointNumber;
    }

    public String getPointText() {
        return this.mPointText;
    }

    @Override // android.view.View
    public void onDetachedFromWindow() {
        cancelAnim();
        super.onDetachedFromWindow();
        this.mIsLaidOut = false;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        int i3;
        RectF rectF = this.mRectF;
        rectF.left = 0.0f;
        rectF.top = 0.0f;
        rectF.right = getWidth();
        this.mRectF.bottom = getHeight();
        if (this.mIsExecutingAlphaAnim && ((i3 = this.mPointNumber) < 1000 || this.mTempPointNumber < 1000)) {
            COUIHintRedDotHelper cOUIHintRedDotHelper = this.mCOUIHintRedDotHelper;
            int i4 = this.mTextPaintAlpha;
            cOUIHintRedDotHelper.drawPointWithFadeNumber(canvas, i3, i4, this.mTempPointNumber, 255 - i4, this.mRectF);
        } else {
            int i5 = this.mPointNumber;
            if (i5 == 0 || i5 < 1000) {
                this.mCOUIHintRedDotHelper.drawRedPoint(canvas, this.mPointMode, this.mPointText, this.mRectF);
            } else {
                this.mCOUIHintRedDotHelper.drawRedPoint(canvas, this.mPointMode, this.mMoreText, this.mRectF);
            }
        }
    }

    @Override // android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        super.onLayout(z2, i3, i4, i5, i6);
        this.mIsLaidOut = true;
    }

    @Override // android.view.View
    public void onMeasure(int i3, int i4) {
        setMeasuredDimension(this.mIsExecutingWidthAnim ? this.mTempWidth : this.mCOUIHintRedDotHelper.getViewWidth(this.mPointMode, this.mPointText), this.mCOUIHintRedDotHelper.getViewHeight(this.mPointMode));
    }

    public COUIHintRedDotMemento saveMemento() {
        COUIHintRedDotMemento cOUIHintRedDotMemento = new COUIHintRedDotMemento();
        cOUIHintRedDotMemento.setPointMode(getPointMode());
        cOUIHintRedDotMemento.setPointNumber(getPointNumber());
        cOUIHintRedDotMemento.setPointText(getPointText());
        return cOUIHintRedDotMemento;
    }

    public void setBgColor(int i3) {
        this.mCOUIHintRedDotHelper.setBgColor(i3);
    }

    public void setCornerRadius(int i3) {
        this.mCOUIHintRedDotHelper.setCornerRadius(i3);
    }

    public void setDotDiameter(int i3) {
        this.mCOUIHintRedDotHelper.setDotDiameter(i3);
    }

    public void setEllipsisDiameter(int i3) {
        this.mCOUIHintRedDotHelper.setEllipsisDiameter(i3);
    }

    public void setLaidOut() {
        this.mIsLaidOut = true;
    }

    public void setLargeWidth(int i3) {
        this.mCOUIHintRedDotHelper.setLargeWidth(i3);
    }

    public void setMediumWidth(int i3) {
        this.mCOUIHintRedDotHelper.setMediumWidth(i3);
    }

    public void setPointMode(int i3) {
        if (this.mPointMode != i3) {
            this.mPointMode = i3;
            if (i3 == 4) {
                setBackground(this.mStrokeBackground);
            }
            requestLayout();
            int i4 = this.mPointMode;
            if (i4 == 1 || i4 == 4) {
                setContentDescription(this.mRedDotDescription);
            } else if (i4 == 0) {
                setContentDescription("");
            }
        }
    }

    public void setPointNumber(int i3) {
        this.mPointNumber = i3;
        if (i3 != 0) {
            setPointText(String.valueOf(i3));
        } else {
            setPointText("");
        }
        if (i3 > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(",");
            Resources resources = getResources();
            int i4 = this.mRedDotWithNumberDescriptionId;
            int i5 = this.mPointNumber;
            sb.append(resources.getQuantityString(i4, i5, Integer.valueOf(i5)));
            setContentDescription(sb.toString());
        }
    }

    public void setPointText(String str) {
        this.mPointText = str;
        requestLayout();
    }

    public void setSmallWidth(int i3) {
        this.mCOUIHintRedDotHelper.setSmallWidth(i3);
    }

    public void setTextColor(int i3) {
        this.mCOUIHintRedDotHelper.setTextColor(i3);
    }

    public void setTextSize(int i3) {
        this.mCOUIHintRedDotHelper.setTextSize(i3);
    }

    public void setViewHeight(int i3) {
        this.mCOUIHintRedDotHelper.setViewHeight(i3);
    }

    public COUIHintRedDot(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiHintRedDotStyle);
    }

    public COUIHintRedDot(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, R.style.Widget_COUI_COUIHintRedDot);
    }

    public COUIHintRedDot(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mPointMode = 0;
        this.mPointNumber = 0;
        this.mPointText = "";
        this.mTempPointNumber = 0;
        this.mTextPaintAlpha = 255;
        int[] iArr = R.styleable.COUIHintRedDot;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr, i3, i4);
        this.mPointMode = typedArrayObtainStyledAttributes.getInteger(R.styleable.COUIHintRedDot_couiHintRedPointMode, 0);
        setPointNumber(typedArrayObtainStyledAttributes.getInteger(R.styleable.COUIHintRedDot_couiHintRedPointNum, 0));
        this.mPointText = typedArrayObtainStyledAttributes.getString(R.styleable.COUIHintRedDot_couiHintRedPointText);
        typedArrayObtainStyledAttributes.recycle();
        this.mCOUIHintRedDotHelper = new COUIHintRedDotHelper(context, attributeSet, iArr, i3, i4);
        this.mRectF = new RectF();
        this.mRedDotDescription = getResources().getString(R.string.red_dot_description);
        this.mRedDotWithNumberDescriptionId = R.plurals.red_dot_with_number_description;
        Drawable drawable = context.getResources().getDrawable(R.drawable.red_dot_stroke_circle);
        this.mStrokeBackground = drawable;
        if (this.mPointMode == 4) {
            setBackground(drawable);
        }
        this.mMoreText = context.getString(R.string.red_dot_more);
    }
}