package com.coui.appcompat.edittext;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.text.method.TransformationMethod;
import android.view.animation.Interpolator;
import android.widget.EditText;
import androidx.core.view.GravityCompat;
import com.coui.appcompat.animation.COUIEaseInterpolator;
import com.coui.appcompat.animation.COUILinearInterpolator;
import com.support.appcompat.R;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes7.dex */
class COUIErrorEditTextHelper {
    private static final int DELAY_MASK_ANIMATOR = 80;
    private static final int DURATION_HINT_ANIMATOR = 217;
    private static final int DURATION_MASK_ANIMATOR = 133;
    private static final int MAX_COLOR_VALUE = 255;
    private static final float SELECTION_MASK_ALPHA_MAX = 0.3f;
    private static final Rect tmpRect = new Rect();
    private boolean mAnimating;
    private COUICutoutDrawable mBoxBackground;
    private final COUICutoutDrawable.COUICollapseTextHelper mCOUICollapseTextHelper;
    private ColorStateList mCollapsedTextColor;
    private final EditText mEditText;
    private int mErrorColor;
    private Paint mErrorPaint;
    private boolean mErrorState;
    private AnimatorSet mErrorTrueAnimatorSet;
    private ColorStateList mExpandedTextColor;
    private float mHintColorChangeProgress;
    private boolean mIsFocusedAtAnimateBeginning;
    private ArrayList<COUIEditText.OnErrorStateChangedListener> mOnErrorStateChangedListeners;
    private int mOriginalHighlightColor;
    private ColorStateList mOriginalTextColors;
    private float mSelectionMaskAlpha;
    private Paint mSelectionMaskPaint;
    private float mSingleCOUIEditTextHeight;
    private int mStrokeWidth;
    private float mTextShakeOffset;
    private float mTextWidth;

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUIErrorEditTextHelper$1, reason: invalid class name */
    public class AnonymousClass1 implements TextWatcher {
        public AnonymousClass1() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            COUIErrorEditTextHelper.access$200(COUIErrorEditTextHelper.this, false, false, false);
            Editable text = COUIErrorEditTextHelper.access$100(COUIErrorEditTextHelper.this).getText();
            int length = text.length();
            COUIErrorEditTextHelper cOUIErrorEditTextHelper = COUIErrorEditTextHelper.this;
            COUIErrorEditTextHelper.access$302(cOUIErrorEditTextHelper, COUIErrorEditTextHelper.access$100(cOUIErrorEditTextHelper).getPaint().measureText(text, 0, length));
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
            if (COUIErrorEditTextHelper.access$000(COUIErrorEditTextHelper.this) <= 0.0f) {
                COUIErrorEditTextHelper cOUIErrorEditTextHelper = COUIErrorEditTextHelper.this;
                COUIErrorEditTextHelper.access$002(cOUIErrorEditTextHelper, COUIErrorEditTextHelper.access$100(cOUIErrorEditTextHelper).getHeight());
            }
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUIErrorEditTextHelper$2, reason: invalid class name */
    public class AnonymousClass2 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass2() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIErrorEditTextHelper.access$402(COUIErrorEditTextHelper.this, ((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUIErrorEditTextHelper$3, reason: invalid class name */
    public class AnonymousClass3 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass3() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (COUIErrorEditTextHelper.access$600(COUIErrorEditTextHelper.this)) {
                COUIErrorEditTextHelper.access$702(COUIErrorEditTextHelper.this, ((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
            COUIErrorEditTextHelper.access$100(COUIErrorEditTextHelper.this).invalidate();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUIErrorEditTextHelper$4, reason: invalid class name */
    public class AnonymousClass4 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass4() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (COUIErrorEditTextHelper.access$600(COUIErrorEditTextHelper.this)) {
                COUIErrorEditTextHelper.access$802(COUIErrorEditTextHelper.this, ((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUIErrorEditTextHelper$5, reason: invalid class name */
    public class AnonymousClass5 implements Animator.AnimatorListener {

        /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUIErrorEditTextHelper$5$1, reason: invalid class name */
        public class AnonymousClass1 implements Runnable {
            public AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() {
                COUIErrorEditTextHelper cOUIErrorEditTextHelper = COUIErrorEditTextHelper.this;
                COUIErrorEditTextHelper.access$002(cOUIErrorEditTextHelper, COUIErrorEditTextHelper.access$100(cOUIErrorEditTextHelper).getHeight());
            }
        }

        public AnonymousClass5() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUIErrorEditTextHelper.access$900(COUIErrorEditTextHelper.this, true, true, true);
            COUIErrorEditTextHelper.access$1000(COUIErrorEditTextHelper.this, true);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUIErrorEditTextHelper.access$100(COUIErrorEditTextHelper.this).setSelection(COUIErrorEditTextHelper.access$100(COUIErrorEditTextHelper.this).length());
            if (COUIErrorEditTextHelper.access$000(COUIErrorEditTextHelper.this) <= 0.0f) {
                COUIErrorEditTextHelper.access$100(COUIErrorEditTextHelper.this).post(new AnonymousClass1());
            }
        }
    }

    public static class ShakeInterpolator implements Interpolator {
        private static final int[] DURATIONS;
        private static final float[] OFFSETS = {0.0f, -1.0f, 0.5f, -0.5f, 0.0f};
        static final int TOTAL_DURATION = 450;
        private static final float[] progresses;
        private final Interpolator mBetweenInterpolator;

        static {
            int[] iArr = {83, 133, 117, 117};
            DURATIONS = iArr;
            progresses = new float[iArr.length + 1];
            int i3 = 0;
            int i4 = 0;
            while (true) {
                int[] iArr2 = DURATIONS;
                if (i3 >= iArr2.length) {
                    return;
                }
                i4 += iArr2[i3];
                i3++;
                progresses[i3] = i4 / 450.0f;
            }
        }

        public /* synthetic */ ShakeInterpolator(AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f3) {
            int i3 = 1;
            while (true) {
                float[] fArr = progresses;
                if (i3 >= fArr.length) {
                    return 0.0f;
                }
                float f4 = fArr[i3];
                if (f3 <= f4) {
                    int i4 = i3 - 1;
                    float f5 = fArr[i4];
                    float interpolation = this.mBetweenInterpolator.getInterpolation((f3 - f5) / (f4 - f5));
                    float[] fArr2 = OFFSETS;
                    return (fArr2[i4] * (1.0f - interpolation)) + (fArr2[i3] * interpolation);
                }
                i3++;
            }
        }

        private ShakeInterpolator() {
            this.mBetweenInterpolator = new COUIEaseInterpolator();
        }
    }

    public COUIErrorEditTextHelper(EditText editText) {
        this(editText, 1);
    }

    public static /* synthetic */ float access$000(COUIErrorEditTextHelper cOUIErrorEditTextHelper) {
        return cOUIErrorEditTextHelper.mSingleCOUIEditTextHeight;
    }

    public static /* synthetic */ float access$002(COUIErrorEditTextHelper cOUIErrorEditTextHelper, float f3) {
        cOUIErrorEditTextHelper.mSingleCOUIEditTextHeight = f3;
        return f3;
    }

    public static /* synthetic */ EditText access$100(COUIErrorEditTextHelper cOUIErrorEditTextHelper) {
        return cOUIErrorEditTextHelper.mEditText;
    }

    public static /* synthetic */ void access$1000(COUIErrorEditTextHelper cOUIErrorEditTextHelper, boolean z2) {
        cOUIErrorEditTextHelper.performOnErrorStateChangeAnimationEnd(z2);
    }

    public static /* synthetic */ void access$200(COUIErrorEditTextHelper cOUIErrorEditTextHelper, boolean z2, boolean z3, boolean z4) {
        cOUIErrorEditTextHelper.setErrorState(z2, z3, z4);
    }

    public static /* synthetic */ float access$302(COUIErrorEditTextHelper cOUIErrorEditTextHelper, float f3) {
        cOUIErrorEditTextHelper.mTextWidth = f3;
        return f3;
    }

    public static /* synthetic */ float access$402(COUIErrorEditTextHelper cOUIErrorEditTextHelper, float f3) {
        cOUIErrorEditTextHelper.mHintColorChangeProgress = f3;
        return f3;
    }

    public static /* synthetic */ boolean access$600(COUIErrorEditTextHelper cOUIErrorEditTextHelper) {
        return cOUIErrorEditTextHelper.mIsFocusedAtAnimateBeginning;
    }

    public static /* synthetic */ float access$702(COUIErrorEditTextHelper cOUIErrorEditTextHelper, float f3) {
        cOUIErrorEditTextHelper.mTextShakeOffset = f3;
        return f3;
    }

    public static /* synthetic */ float access$802(COUIErrorEditTextHelper cOUIErrorEditTextHelper, float f3) {
        cOUIErrorEditTextHelper.mSelectionMaskAlpha = f3;
        return f3;
    }

    public static /* synthetic */ void access$900(COUIErrorEditTextHelper cOUIErrorEditTextHelper, boolean z2, boolean z3, boolean z4) {
        cOUIErrorEditTextHelper.setErrorStateEnd(z2, z3, z4);
    }

    private void cancelAnimation() {
        if (this.mErrorTrueAnimatorSet.isStarted()) {
            this.mErrorTrueAnimatorSet.cancel();
        }
    }

    private Layout.Alignment getAlignment() {
        switch (this.mEditText.getTextAlignment()) {
            case 1:
                int gravity = this.mEditText.getGravity() & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
                if (gravity == 1) {
                    return Layout.Alignment.ALIGN_CENTER;
                }
                if (gravity == 3) {
                    return isRtlMode() ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_NORMAL;
                }
                if (gravity == 5) {
                    return isRtlMode() ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
                }
                if (gravity != 8388611 && gravity == 8388613) {
                    return Layout.Alignment.ALIGN_OPPOSITE;
                }
                return Layout.Alignment.ALIGN_NORMAL;
            case 2:
                return Layout.Alignment.ALIGN_NORMAL;
            case 3:
                return Layout.Alignment.ALIGN_OPPOSITE;
            case 4:
                return Layout.Alignment.ALIGN_CENTER;
            case 5:
                return Layout.Alignment.ALIGN_NORMAL;
            case 6:
                return Layout.Alignment.ALIGN_OPPOSITE;
            default:
                return Layout.Alignment.ALIGN_NORMAL;
        }
    }

    private CharSequence getFullText() {
        return !isPassword() ? this.mEditText.getText() : getMaskChars();
    }

    private int getGradientColor(int i3, int i4, float f3) {
        if (f3 <= 0.0f) {
            return i3;
        }
        if (f3 >= 1.0f) {
            return i4;
        }
        float f4 = 1.0f - f3;
        int iAlpha = (int) ((Color.alpha(i3) * f4) + (Color.alpha(i4) * f3));
        int iRed = (int) ((Color.red(i3) * f4) + (Color.red(i4) * f3));
        int iGreen = (int) ((Color.green(i3) * f4) + (Color.green(i4) * f3));
        int iBlue = (int) ((Color.blue(i3) * f4) + (Color.blue(i4) * f3));
        if (iAlpha > 255) {
            iAlpha = 255;
        }
        if (iRed > 255) {
            iRed = 255;
        }
        if (iGreen > 255) {
            iGreen = 255;
        }
        if (iBlue > 255) {
            iBlue = 255;
        }
        return Color.argb(iAlpha, iRed, iGreen, iBlue);
    }

    private CharSequence getMaskChars() {
        TransformationMethod transformationMethod = this.mEditText.getTransformationMethod();
        return transformationMethod != null ? transformationMethod.getTransformation(this.mEditText.getText(), this.mEditText) : this.mEditText.getText();
    }

    private int getSelectionMaskColor(float f3) {
        return Color.argb((int) (f3 * 255.0f), Color.red(this.mErrorColor), Color.green(this.mErrorColor), Color.blue(this.mErrorColor));
    }

    private void initAnimator() {
        float dimension = this.mEditText.getResources().getDimension(R.dimen.coui_edit_text_shake_amplitude);
        COUIEaseInterpolator cOUIEaseInterpolator = new COUIEaseInterpolator();
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimatorOfFloat.setInterpolator(cOUIEaseInterpolator);
        valueAnimatorOfFloat.setDuration(217L);
        valueAnimatorOfFloat.addUpdateListener(new AnonymousClass2());
        ValueAnimator valueAnimatorOfFloat2 = ValueAnimator.ofFloat(0.0f, dimension);
        valueAnimatorOfFloat2.setInterpolator(new ShakeInterpolator(null));
        valueAnimatorOfFloat2.setDuration(450L);
        valueAnimatorOfFloat2.addUpdateListener(new AnonymousClass3());
        ValueAnimator valueAnimatorOfFloat3 = ValueAnimator.ofFloat(0.0f, 0.3f);
        valueAnimatorOfFloat3.setInterpolator(cOUIEaseInterpolator);
        valueAnimatorOfFloat3.setDuration(133L);
        valueAnimatorOfFloat3.setStartDelay(80L);
        valueAnimatorOfFloat3.addUpdateListener(new AnonymousClass4());
        AnimatorSet animatorSet = new AnimatorSet();
        this.mErrorTrueAnimatorSet = animatorSet;
        animatorSet.playTogether(valueAnimatorOfFloat, valueAnimatorOfFloat2, valueAnimatorOfFloat3);
        this.mErrorTrueAnimatorSet.addListener(new AnonymousClass5());
    }

    private boolean isPassword() {
        return (this.mEditText.getInputType() & 128) == 128 || (this.mEditText.getInputType() & 16) == 16;
    }

    private boolean isRtlMode() {
        return this.mEditText.getLayoutDirection() == 1;
    }

    private void performOnErrorStateChangeAnimationEnd(boolean z2) {
        if (this.mOnErrorStateChangedListeners != null) {
            for (int i3 = 0; i3 < this.mOnErrorStateChangedListeners.size(); i3++) {
                this.mOnErrorStateChangedListeners.get(i3).onErrorStateChangeAnimationEnd(z2);
            }
        }
    }

    private void performOnErrorStateChanged(boolean z2) {
        if (this.mOnErrorStateChangedListeners != null) {
            for (int i3 = 0; i3 < this.mOnErrorStateChangedListeners.size(); i3++) {
                this.mOnErrorStateChangedListeners.get(i3).onErrorStateChanged(z2);
            }
        }
    }

    private void setErrorStateEnd(boolean z2, boolean z3, boolean z4) {
        this.mAnimating = false;
        if (!z2) {
            this.mEditText.setTextColor(this.mOriginalTextColors);
            this.mEditText.setHighlightColor(this.mOriginalHighlightColor);
            return;
        }
        if (z3) {
            this.mEditText.setTextColor(this.mOriginalTextColors);
        }
        this.mEditText.setHighlightColor(getSelectionMaskColor(0.3f));
        if (z4) {
            EditText editText = this.mEditText;
            editText.setSelection(0, editText.getText().length());
        }
    }

    private void setErrorStateWithAnimation(boolean z2, boolean z3) {
        if (!z2) {
            cancelAnimation();
            setErrorStateEnd(false, false, z3);
            return;
        }
        cancelAnimation();
        this.mEditText.setTextColor(0);
        this.mEditText.setHighlightColor(0);
        this.mHintColorChangeProgress = 0.0f;
        this.mTextShakeOffset = 0.0f;
        this.mSelectionMaskAlpha = 0.0f;
        this.mAnimating = true;
        this.mIsFocusedAtAnimateBeginning = this.mEditText.isFocused();
        this.mErrorTrueAnimatorSet.start();
    }

    private void setErrorStateWithoutAnimation(boolean z2, boolean z3) {
        if (!z2) {
            setErrorStateEnd(false, false, z3);
            return;
        }
        this.mHintColorChangeProgress = 1.0f;
        this.mTextShakeOffset = 0.0f;
        this.mSelectionMaskAlpha = 0.0f;
        setErrorStateEnd(true, false, z3);
    }

    public void addOnErrorStateChangedListener(COUIEditText.OnErrorStateChangedListener onErrorStateChangedListener) {
        if (this.mOnErrorStateChangedListeners == null) {
            this.mOnErrorStateChangedListeners = new ArrayList<>();
        }
        if (this.mOnErrorStateChangedListeners.contains(onErrorStateChangedListener)) {
            return;
        }
        this.mOnErrorStateChangedListeners.add(onErrorStateChangedListener);
    }

    public void drawCollapseText(Canvas canvas, COUICutoutDrawable.COUICollapseTextHelper cOUICollapseTextHelper) {
        this.mCOUICollapseTextHelper.setCollapsedTextColor(ColorStateList.valueOf(getGradientColor(this.mCollapsedTextColor.getDefaultColor(), this.mErrorColor, this.mHintColorChangeProgress)));
        this.mCOUICollapseTextHelper.setExpandedTextColor(ColorStateList.valueOf(getGradientColor(this.mExpandedTextColor.getDefaultColor(), this.mErrorColor, this.mHintColorChangeProgress)));
        this.mCOUICollapseTextHelper.setExpansionFraction(cOUICollapseTextHelper.getExpandedFraction());
        this.mCOUICollapseTextHelper.draw(canvas);
    }

    public void drawModeBackgroundLine(Canvas canvas, int i3, int i4, int i5, Paint paint, Paint paint2) {
        this.mErrorPaint.setColor(getGradientColor(paint.getColor(), this.mErrorColor, this.mHintColorChangeProgress));
        float f3 = i3;
        canvas.drawRect(0.0f, i3 - this.mStrokeWidth, i4, f3, this.mErrorPaint);
        this.mErrorPaint.setColor(getGradientColor(paint2.getColor(), this.mErrorColor, this.mHintColorChangeProgress));
        canvas.drawRect(0.0f, i3 - this.mStrokeWidth, i5, f3, this.mErrorPaint);
    }

    public void drawModeBackgroundRect(Canvas canvas, GradientDrawable gradientDrawable, int i3) {
        this.mBoxBackground.setBounds(gradientDrawable.getBounds());
        if (gradientDrawable instanceof COUICutoutDrawable) {
            this.mBoxBackground.setCutout(((COUICutoutDrawable) gradientDrawable).getCutout());
        }
        this.mBoxBackground.setStroke(this.mStrokeWidth, getGradientColor(i3, this.mErrorColor, this.mHintColorChangeProgress));
        this.mBoxBackground.draw(canvas);
    }

    public void drawableStateChanged(int[] iArr) {
        this.mCOUICollapseTextHelper.setState(iArr);
    }

    public void init(int i3, int i4, int i5, float[] fArr, COUICutoutDrawable.COUICollapseTextHelper cOUICollapseTextHelper) {
        this.mOriginalTextColors = this.mEditText.getTextColors();
        this.mOriginalHighlightColor = this.mEditText.getHighlightColor();
        this.mErrorColor = i3;
        this.mStrokeWidth = i4;
        if (i5 == 2) {
            this.mCOUICollapseTextHelper.setTypefaces(Typeface.create("sans-serif-medium", 0));
        }
        this.mCOUICollapseTextHelper.setExpandedTextSize(cOUICollapseTextHelper.getExpandedTextSize());
        this.mCOUICollapseTextHelper.setCollapsedTextGravity(cOUICollapseTextHelper.getCollapsedTextGravity());
        this.mCOUICollapseTextHelper.setExpandedTextGravity(cOUICollapseTextHelper.getExpandedTextGravity());
        COUICutoutDrawable cOUICutoutDrawable = new COUICutoutDrawable();
        this.mBoxBackground = cOUICutoutDrawable;
        cOUICutoutDrawable.setCornerRadii(fArr);
        this.mErrorPaint = new Paint();
        this.mSelectionMaskPaint = new Paint();
        initAnimator();
        this.mEditText.addTextChangedListener(new AnonymousClass1());
        setHintInternal(cOUICollapseTextHelper);
        updateLabelState(cOUICollapseTextHelper);
    }

    public boolean isErrorState() {
        return this.mErrorState;
    }

    public void onDetachedFromWindow() {
        AnimatorSet animatorSet = this.mErrorTrueAnimatorSet;
        if (animatorSet != null) {
            if (animatorSet.isStarted()) {
                this.mErrorTrueAnimatorSet.cancel();
            }
            for (Animator animator : this.mErrorTrueAnimatorSet.getChildAnimations()) {
                if (animator instanceof ValueAnimator) {
                    ((ValueAnimator) animator).removeAllUpdateListeners();
                }
                animator.removeAllListeners();
            }
            this.mErrorTrueAnimatorSet.removeAllListeners();
        }
    }

    public void onDraw(Canvas canvas) {
        Layout.Alignment alignment;
        float f3;
        float f4;
        if (this.mAnimating && this.mErrorState) {
            int iSave = canvas.save();
            if (isRtlMode()) {
                canvas.translate(-this.mTextShakeOffset, 0.0f);
            } else {
                canvas.translate(this.mTextShakeOffset, 0.0f);
            }
            int compoundPaddingStart = this.mEditText.getCompoundPaddingStart();
            int compoundPaddingEnd = this.mEditText.getCompoundPaddingEnd();
            int width = this.mEditText.getWidth();
            int i3 = width - compoundPaddingEnd;
            int i4 = i3 - compoundPaddingStart;
            float x2 = i3 + this.mEditText.getX() + this.mEditText.getScrollX();
            float f5 = i4;
            float scrollX = (this.mTextWidth - this.mEditText.getScrollX()) - f5;
            EditText editText = this.mEditText;
            Rect rect = tmpRect;
            editText.getLineBounds(0, rect);
            int iSave2 = canvas.save();
            if (isRtlMode()) {
                canvas.translate(compoundPaddingEnd, rect.top);
            } else {
                canvas.translate(compoundPaddingStart, rect.top);
            }
            int iSave3 = canvas.save();
            if (this.mEditText.getBottom() - this.mEditText.getTop() == this.mSingleCOUIEditTextHeight && this.mTextWidth > f5) {
                if (isRtlMode()) {
                    canvas.clipRect(this.mEditText.getScrollX() + i4, 0.0f, this.mEditText.getScrollX(), this.mSingleCOUIEditTextHeight);
                } else {
                    canvas.translate(-scrollX, 0.0f);
                    canvas.clipRect(this.mEditText.getScrollX(), 0.0f, x2, this.mSingleCOUIEditTextHeight);
                }
            }
            Layout layout = this.mEditText.getLayout();
            layout.getPaint().setColor(this.mOriginalTextColors.getDefaultColor());
            layout.draw(canvas);
            canvas.restoreToCount(iSave3);
            canvas.restoreToCount(iSave2);
            Layout.Alignment alignment2 = getAlignment();
            this.mSelectionMaskPaint.setColor(getSelectionMaskColor(this.mSelectionMaskAlpha));
            Layout.Alignment alignment3 = Layout.Alignment.ALIGN_NORMAL;
            if ((alignment2 != alignment3 || isRtlMode()) && (!(alignment2 == (alignment = Layout.Alignment.ALIGN_OPPOSITE) && isRtlMode()) && (!(alignment2 == alignment3 && isRtlMode()) && (alignment2 != alignment || isRtlMode())))) {
                float f6 = ((compoundPaddingStart + width) - compoundPaddingEnd) / 2.0f;
                float f7 = this.mTextWidth;
                float f8 = f6 - (f7 / 2.0f);
                f3 = f8;
                f4 = f8 + f7;
            } else {
                f3 = compoundPaddingStart;
                f4 = f3;
            }
            canvas.drawRect(f3, rect.top, f4, rect.bottom, this.mSelectionMaskPaint);
            canvas.restoreToCount(iSave);
        }
    }

    public void onLayout(COUICutoutDrawable.COUICollapseTextHelper cOUICollapseTextHelper) {
        Rect expandedBounds = cOUICollapseTextHelper.getExpandedBounds();
        Rect collapsedBounds = cOUICollapseTextHelper.getCollapsedBounds();
        this.mCOUICollapseTextHelper.setExpandedBounds(expandedBounds.left, expandedBounds.top, expandedBounds.right, expandedBounds.bottom);
        this.mCOUICollapseTextHelper.setCollapsedBounds(collapsedBounds.left, collapsedBounds.top, collapsedBounds.right, collapsedBounds.bottom);
        this.mCOUICollapseTextHelper.recalculate();
    }

    public void removeOnErrorStateChangedListener(COUIEditText.OnErrorStateChangedListener onErrorStateChangedListener) {
        ArrayList<COUIEditText.OnErrorStateChangedListener> arrayList = this.mOnErrorStateChangedListeners;
        if (arrayList == null) {
            return;
        }
        arrayList.remove(onErrorStateChangedListener);
    }

    public void setCollapsedTextAppearance(int i3, ColorStateList colorStateList) {
        this.mCOUICollapseTextHelper.setCollapsedTextAppearance(i3, colorStateList);
    }

    public void setErrorColor(int i3) {
        this.mErrorColor = i3;
    }

    public void setErrorState(boolean z2) {
        setErrorState(z2, true);
    }

    public void setHintInternal(COUICutoutDrawable.COUICollapseTextHelper cOUICollapseTextHelper) {
        this.mCOUICollapseTextHelper.setText(cOUICollapseTextHelper.getText());
    }

    public void setOriginalTextColors(ColorStateList colorStateList) {
        this.mOriginalTextColors = colorStateList;
    }

    public void updateLabelState(COUICutoutDrawable.COUICollapseTextHelper cOUICollapseTextHelper) {
        this.mCollapsedTextColor = cOUICollapseTextHelper.getCollapsedTextColor();
        this.mExpandedTextColor = cOUICollapseTextHelper.getExpandedTextColor();
        this.mCOUICollapseTextHelper.setCollapsedTextColor(this.mCollapsedTextColor);
        this.mCOUICollapseTextHelper.setExpandedTextColor(this.mExpandedTextColor);
    }

    public COUIErrorEditTextHelper(EditText editText, int i3) {
        this.mEditText = editText;
        COUICutoutDrawable.COUICollapseTextHelper cOUICollapseTextHelper = new COUICutoutDrawable.COUICollapseTextHelper(editText);
        this.mCOUICollapseTextHelper = cOUICollapseTextHelper;
        cOUICollapseTextHelper.setHintLines(i3);
        cOUICollapseTextHelper.setTextSizeInterpolator(new COUILinearInterpolator());
        cOUICollapseTextHelper.setPositionInterpolator(new COUILinearInterpolator());
        cOUICollapseTextHelper.setCollapsedTextGravity(8388659);
    }

    private void setErrorState(boolean z2, boolean z3) {
        setErrorState(z2, z3, true);
    }

    private void setErrorState(boolean z2, boolean z3, boolean z4) {
        if (this.mErrorState == z2) {
            return;
        }
        this.mErrorState = z2;
        performOnErrorStateChanged(z2);
        if (z3) {
            setErrorStateWithAnimation(z2, z4);
        } else {
            setErrorStateWithoutAnimation(z2, z4);
        }
    }
}