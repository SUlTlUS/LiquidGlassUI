package com.coui.appcompat.button;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.AttributeSet;
import androidx.appcompat.R;
import com.coui.appcompat.animation.COUILinearInterpolator;

/* JADX INFO: loaded from: classes7.dex */
public class COUILoadingButton extends COUIButton {
    public static final int DEFAULT_STATE = 0;
    private static final float DOT_END_ALPHA = 255.0f;
    private static final float DOT_MID_ALPHA = 127.5f;
    private static final float DOT_START_ALPHA = 51.0f;
    public static final int LOADING_STATE = 1;
    private int mButtonState;
    private final String mDots;
    private int mFirstLoadingDotAlpha;
    private AnimatorSet mLoadingAnim;
    private final float mLoadingCircleRadius;
    private final float mLoadingCircleSpacing;
    private final float mLoadingCircleTotalWidth;
    private String mLoadingText;
    private final Rect mLoadingTextBounds;
    private OnLoadingStateChangeListener mOnLoadingStateChangeListener;
    private String mOriginalText;
    private int mSecondLoadingDotAlpha;
    private boolean mShowLoadingText;
    private int mThirdLoadingDotAlpha;

    /* JADX INFO: renamed from: com.coui.appcompat.button.COUILoadingButton$1, reason: invalid class name */
    public class AnonymousClass1 implements TextWatcher {
        public AnonymousClass1() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
            if (COUILoadingButton.access$000(COUILoadingButton.this) != 1 || charSequence.toString().equals("")) {
                return;
            }
            COUILoadingButton.access$102(COUILoadingButton.this, charSequence.toString());
            COUILoadingButton.this.setText("");
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.button.COUILoadingButton$2, reason: invalid class name */
    public class AnonymousClass2 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass2() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUILoadingButton.access$202(COUILoadingButton.this, (int) ((Float) valueAnimator.getAnimatedValue()).floatValue());
            COUILoadingButton.this.invalidate();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.button.COUILoadingButton$3, reason: invalid class name */
    public class AnonymousClass3 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass3() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUILoadingButton.access$302(COUILoadingButton.this, (int) ((Float) valueAnimator.getAnimatedValue()).floatValue());
            COUILoadingButton.this.invalidate();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.button.COUILoadingButton$4, reason: invalid class name */
    public class AnonymousClass4 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass4() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUILoadingButton.access$402(COUILoadingButton.this, (int) ((Float) valueAnimator.getAnimatedValue()).floatValue());
            COUILoadingButton.this.invalidate();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.button.COUILoadingButton$5, reason: invalid class name */
    public class AnonymousClass5 extends AnimatorListenerAdapter {

        /* JADX INFO: renamed from: com.coui.appcompat.button.COUILoadingButton$5$1, reason: invalid class name */
        public class AnonymousClass1 implements Runnable {
            public AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() {
                COUILoadingButton.access$500(COUILoadingButton.this).start();
            }
        }

        public AnonymousClass5() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUILoadingButton.access$500(COUILoadingButton.this) == null || COUILoadingButton.access$000(COUILoadingButton.this) != 1) {
                return;
            }
            COUILoadingButton.this.post(new AnonymousClass1());
        }
    }

    public interface OnLoadingStateChangeListener {
        void OnLoadingStateChanged(int i3);
    }

    public COUILoadingButton(Context context) {
        this(context, null);
    }

    public static /* synthetic */ int access$000(COUILoadingButton cOUILoadingButton) {
        return cOUILoadingButton.mButtonState;
    }

    public static /* synthetic */ String access$102(COUILoadingButton cOUILoadingButton, String str) {
        cOUILoadingButton.mOriginalText = str;
        return str;
    }

    public static /* synthetic */ int access$202(COUILoadingButton cOUILoadingButton, int i3) {
        cOUILoadingButton.mFirstLoadingDotAlpha = i3;
        return i3;
    }

    public static /* synthetic */ int access$302(COUILoadingButton cOUILoadingButton, int i3) {
        cOUILoadingButton.mSecondLoadingDotAlpha = i3;
        return i3;
    }

    public static /* synthetic */ int access$402(COUILoadingButton cOUILoadingButton, int i3) {
        cOUILoadingButton.mThirdLoadingDotAlpha = i3;
        return i3;
    }

    public static /* synthetic */ AnimatorSet access$500(COUILoadingButton cOUILoadingButton) {
        return cOUILoadingButton.mLoadingAnim;
    }

    private void drawClipDot(Canvas canvas, float f3, float f4, float f5, float f6, TextPaint textPaint, int i3) {
        textPaint.setAlpha(i3);
        int iSave = canvas.save();
        canvas.clipRect(f3, 0.0f, f4, getHeight());
        canvas.drawText(this.mDots, f5, f6, textPaint);
        canvas.restoreToCount(iSave);
    }

    private void drawLoadingCircles(Canvas canvas, TextPaint textPaint) {
        int i3;
        int i4;
        int i5 = this.mSecondLoadingDotAlpha;
        if (isRtlMode()) {
            i3 = this.mThirdLoadingDotAlpha;
            i4 = this.mFirstLoadingDotAlpha;
        } else {
            i3 = this.mFirstLoadingDotAlpha;
            i4 = this.mThirdLoadingDotAlpha;
        }
        float measuredHeight = getMeasuredHeight() / 2.0f;
        float measuredWidth = ((getMeasuredWidth() - this.mLoadingCircleTotalWidth) / 2.0f) + this.mLoadingCircleRadius;
        textPaint.setAlpha(i3);
        canvas.drawCircle(measuredWidth, measuredHeight, this.mLoadingCircleRadius, textPaint);
        float f3 = measuredWidth + (this.mLoadingCircleRadius * 2.0f) + this.mLoadingCircleSpacing;
        textPaint.setAlpha(i5);
        canvas.drawCircle(f3, measuredHeight, this.mLoadingCircleRadius, textPaint);
        float f4 = f3 + (this.mLoadingCircleRadius * 2.0f) + this.mLoadingCircleSpacing;
        textPaint.setAlpha(i4);
        canvas.drawCircle(f4, measuredHeight, this.mLoadingCircleRadius, textPaint);
    }

    private ValueAnimator getAlphaAnimator(float f3, float f4, long j3, long j4, ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(f3, f4);
        valueAnimatorOfFloat.setDuration(j3);
        valueAnimatorOfFloat.setStartDelay(j4);
        valueAnimatorOfFloat.addUpdateListener(animatorUpdateListener);
        return valueAnimatorOfFloat;
    }

    private void initAnim() {
        AnonymousClass2 anonymousClass2 = new AnonymousClass2();
        ValueAnimator alphaAnimator = getAlphaAnimator(DOT_START_ALPHA, DOT_MID_ALPHA, 133L, 0L, anonymousClass2);
        ValueAnimator alphaAnimator2 = getAlphaAnimator(DOT_MID_ALPHA, 255.0f, 67L, 133L, anonymousClass2);
        ValueAnimator alphaAnimator3 = getAlphaAnimator(255.0f, DOT_MID_ALPHA, 67L, 467L, anonymousClass2);
        ValueAnimator alphaAnimator4 = getAlphaAnimator(DOT_MID_ALPHA, DOT_START_ALPHA, 133L, 533L, anonymousClass2);
        AnonymousClass3 anonymousClass3 = new AnonymousClass3();
        ValueAnimator alphaAnimator5 = getAlphaAnimator(DOT_START_ALPHA, DOT_MID_ALPHA, 133L, 333L, anonymousClass3);
        ValueAnimator alphaAnimator6 = getAlphaAnimator(DOT_MID_ALPHA, 255.0f, 67L, 466L, anonymousClass3);
        ValueAnimator alphaAnimator7 = getAlphaAnimator(255.0f, DOT_MID_ALPHA, 67L, 800L, anonymousClass3);
        ValueAnimator alphaAnimator8 = getAlphaAnimator(DOT_MID_ALPHA, DOT_START_ALPHA, 133L, 866L, anonymousClass3);
        AnonymousClass4 anonymousClass4 = new AnonymousClass4();
        ValueAnimator alphaAnimator9 = getAlphaAnimator(DOT_START_ALPHA, DOT_MID_ALPHA, 133L, 666L, anonymousClass4);
        ValueAnimator alphaAnimator10 = getAlphaAnimator(DOT_MID_ALPHA, 255.0f, 67L, 799L, anonymousClass4);
        ValueAnimator alphaAnimator11 = getAlphaAnimator(255.0f, DOT_MID_ALPHA, 67L, 1133L, anonymousClass4);
        ValueAnimator alphaAnimator12 = getAlphaAnimator(DOT_MID_ALPHA, DOT_START_ALPHA, 133L, 1199L, anonymousClass4);
        AnimatorSet animatorSet = new AnimatorSet();
        this.mLoadingAnim = animatorSet;
        animatorSet.playTogether(alphaAnimator, alphaAnimator2, alphaAnimator3, alphaAnimator4, alphaAnimator5, alphaAnimator6, alphaAnimator7, alphaAnimator8, alphaAnimator9, alphaAnimator10, alphaAnimator11, alphaAnimator12);
        this.mLoadingAnim.setInterpolator(new COUILinearInterpolator());
        this.mLoadingAnim.addListener(new AnonymousClass5());
    }

    private void initTextChangeListener() {
        addTextChangedListener(new AnonymousClass1());
    }

    private boolean isRtlMode() {
        return getLayoutDirection() == 1;
    }

    public int getButtonState() {
        return this.mButtonState;
    }

    public String getLoadingText() {
        return this.mLoadingText;
    }

    public OnLoadingStateChangeListener getOnLoadingStateChangeListener(OnLoadingStateChangeListener onLoadingStateChangeListener) {
        return this.mOnLoadingStateChangeListener;
    }

    public boolean getShowLoadingText() {
        return this.mShowLoadingText;
    }

    @Override // com.coui.appcompat.button.COUIButton, android.widget.TextView, android.view.View
    public void onAttachedToWindow() {
        AnimatorSet animatorSet;
        super.onAttachedToWindow();
        if (this.mButtonState != 1 || (animatorSet = this.mLoadingAnim) == null || animatorSet.isRunning()) {
            return;
        }
        this.mLoadingAnim.start();
    }

    @Override // com.coui.appcompat.button.COUIButton, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mButtonState == 1) {
            this.mLoadingAnim.cancel();
        }
    }

    @Override // com.coui.appcompat.button.COUIButton, android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        float measuredWidth;
        float measuredWidth2;
        int i3;
        int i4;
        Canvas canvas2 = canvas;
        super.onDraw(canvas);
        if (this.mButtonState != 1 || getPaint() == null) {
            return;
        }
        TextPaint paint = getPaint();
        int alpha = paint.getAlpha();
        int iSave = canvas2.save();
        canvas2.translate(getScrollX(), getScrollY());
        if (this.mShowLoadingText) {
            float fMeasureText = paint.measureText(this.mLoadingText);
            float fMeasureText2 = paint.measureText(this.mDots);
            if (fMeasureText + fMeasureText2 > (getMeasuredWidth() - getPaddingStart()) - getPaddingEnd()) {
                drawLoadingCircles(canvas2, paint);
            } else {
                Paint.FontMetrics fontMetrics = paint.getFontMetrics();
                float measuredHeight = (((getMeasuredHeight() + (fontMetrics.bottom - fontMetrics.top)) / 2.0f) - fontMetrics.bottom) - ((getPaddingBottom() - getPaddingTop()) / 2);
                int i5 = this.mSecondLoadingDotAlpha;
                if (isRtlMode()) {
                    measuredWidth = (((getMeasuredWidth() - fMeasureText) - fMeasureText2) / 2.0f) + fMeasureText2;
                    measuredWidth2 = ((getMeasuredWidth() - fMeasureText) - fMeasureText2) / 2.0f;
                    i3 = this.mThirdLoadingDotAlpha;
                    i4 = this.mFirstLoadingDotAlpha;
                } else {
                    measuredWidth = ((getMeasuredWidth() - fMeasureText) - fMeasureText2) / 2.0f;
                    measuredWidth2 = measuredWidth + fMeasureText;
                    i3 = this.mFirstLoadingDotAlpha;
                    i4 = this.mThirdLoadingDotAlpha;
                }
                float f3 = measuredWidth2;
                int i6 = i4;
                canvas2.drawText(this.mLoadingText, measuredWidth - ((getPaddingEnd() - getPaddingStart()) / 2), measuredHeight, paint);
                paint.getTextBounds(this.mDots, 0, 1, this.mLoadingTextBounds);
                drawClipDot(canvas2, f3, this.mLoadingTextBounds.right + f3, f3, measuredHeight, paint, i3);
                Rect rect = this.mLoadingTextBounds;
                float f4 = rect.right + f3;
                paint.getTextBounds(this.mDots, 0, 2, rect);
                drawClipDot(canvas, f4, this.mLoadingTextBounds.right + f3, f3, measuredHeight, paint, i5);
                canvas2 = canvas;
                drawClipDot(canvas2, this.mLoadingTextBounds.right + f3, f3 + fMeasureText2, f3, measuredHeight, paint, i6);
            }
        } else {
            drawLoadingCircles(canvas2, paint);
        }
        paint.setAlpha(alpha);
        canvas2.restoreToCount(iSave);
    }

    public void resetButtonState() {
        if (this.mButtonState == 1) {
            this.mButtonState = 0;
            setText(this.mOriginalText);
            this.mLoadingAnim.cancel();
            this.mFirstLoadingDotAlpha = 51;
            this.mSecondLoadingDotAlpha = 51;
            this.mThirdLoadingDotAlpha = 51;
            OnLoadingStateChangeListener onLoadingStateChangeListener = this.mOnLoadingStateChangeListener;
            if (onLoadingStateChangeListener != null) {
                onLoadingStateChangeListener.OnLoadingStateChanged(this.mButtonState);
            }
        }
    }

    public void setLoadingText(String str) {
        if (str == null || !this.mShowLoadingText) {
            return;
        }
        this.mLoadingText = str;
    }

    public void setOnLoadingStateChangeListener(OnLoadingStateChangeListener onLoadingStateChangeListener) {
        this.mOnLoadingStateChangeListener = onLoadingStateChangeListener;
    }

    public void setOriginalText(String str) {
        this.mOriginalText = str;
    }

    public void setShowLoadingText(boolean z2) {
        this.mShowLoadingText = z2;
    }

    public void switchToLoadingState() {
        if (this.mLoadingAnim == null) {
            initTextChangeListener();
            initAnim();
        }
        if (this.mButtonState == 0) {
            this.mButtonState = 1;
            setText("");
            this.mLoadingAnim.start();
            OnLoadingStateChangeListener onLoadingStateChangeListener = this.mOnLoadingStateChangeListener;
            if (onLoadingStateChangeListener != null) {
                onLoadingStateChangeListener.OnLoadingStateChanged(this.mButtonState);
            }
        }
    }

    public COUILoadingButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.buttonStyle);
    }

    public COUILoadingButton(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.mButtonState = 0;
        this.mLoadingText = "";
        this.mLoadingTextBounds = new Rect();
        this.mFirstLoadingDotAlpha = 51;
        this.mSecondLoadingDotAlpha = 51;
        this.mThirdLoadingDotAlpha = 51;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, com.support.appcompat.R.styleable.COUIButton, i3, 0);
        boolean z2 = typedArrayObtainStyledAttributes.getBoolean(com.support.appcompat.R.styleable.COUIButton_isShowLoadingText, false);
        this.mShowLoadingText = z2;
        if (z2) {
            String string = typedArrayObtainStyledAttributes.getString(com.support.appcompat.R.styleable.COUIButton_loadingText);
            this.mLoadingText = string;
            if (string == null) {
                this.mLoadingText = "";
            }
        }
        typedArrayObtainStyledAttributes.recycle();
        this.mOriginalText = getText().toString();
        this.mDots = context.getString(com.support.button.R.string.loading_button_dots);
        float dimensionPixelOffset = context.getResources().getDimensionPixelOffset(com.support.button.R.dimen.coui_loading_btn_circle_radius);
        this.mLoadingCircleRadius = dimensionPixelOffset;
        float dimensionPixelOffset2 = context.getResources().getDimensionPixelOffset(com.support.button.R.dimen.coui_loading_btn_circle_spacing);
        this.mLoadingCircleSpacing = dimensionPixelOffset2;
        this.mLoadingCircleTotalWidth = (dimensionPixelOffset * 6.0f) + (dimensionPixelOffset2 * 2.0f);
    }
}