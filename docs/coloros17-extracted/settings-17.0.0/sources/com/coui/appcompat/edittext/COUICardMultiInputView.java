package com.coui.appcompat.edittext;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.oplus.settings.appfunctions.SystemUpdateScreenStateConfig;
import com.support.appcompat.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUICardMultiInputView extends ConstraintLayout implements View.OnLayoutChangeListener {
    private static final int ERROR_COUNT_COLOR_ANIMATOR_TIME = 250;
    private static final int ERROR_COUNT_COLOR_DELAY_TIME = 1000;
    private static final int MAX_HINT_LINE = 3;
    private static final int MAX_LINE = 5;
    private boolean isErrorColor;
    private int mCountTextColor;
    private ValueAnimator mCountTextColorAnimator;
    private TextView mCountTextView;
    private COUIEditText mEditText;
    private int mEditTextMaxHeight;
    private LinearLayout mEdittextContainer;
    private Rect mEdittextContainerRect;
    private boolean mEnableInputCount;
    private CharSequence mHint;
    private TextWatcher mHintTextWatcher;
    private InputMethodManager mInputMethodManager;
    private int mMaxCount;
    private Runnable setCountColorRunnable;

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUICardMultiInputView$1, reason: invalid class name */
    public class AnonymousClass1 implements View.OnTouchListener {
        final /* synthetic */ int val$padding;

        public AnonymousClass1(int i3) {
            this.val$padding = i3;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            if ((action == 1 || action == 3) && motionEvent.getX() > this.val$padding && motionEvent.getX() < COUICardMultiInputView.this.getWidth() - this.val$padding) {
                if (COUICardMultiInputView.access$000(COUICardMultiInputView.this) == null) {
                    COUICardMultiInputView cOUICardMultiInputView = COUICardMultiInputView.this;
                    COUICardMultiInputView.access$002(cOUICardMultiInputView, (InputMethodManager) cOUICardMultiInputView.getContext().getSystemService(SystemUpdateScreenStateConfig.KEY_INPUT_METHOD));
                }
                COUICardMultiInputView.access$100(COUICardMultiInputView.this).setFocusable(true);
                COUICardMultiInputView.access$100(COUICardMultiInputView.this).requestFocus();
                COUICardMultiInputView.access$000(COUICardMultiInputView.this).showSoftInput(COUICardMultiInputView.access$100(COUICardMultiInputView.this), 0);
            }
            return true;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUICardMultiInputView$2, reason: invalid class name */
    public class AnonymousClass2 implements TextWatcher {
        public AnonymousClass2() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
            if (TextUtils.isEmpty(charSequence)) {
                COUICardMultiInputView.access$100(COUICardMultiInputView.this).setMaxLines(3);
                COUICardMultiInputView.access$100(COUICardMultiInputView.this).setEllipsize(TextUtils.TruncateAt.END);
            } else {
                COUICardMultiInputView.access$100(COUICardMultiInputView.this).setMaxLines(5);
                COUICardMultiInputView.access$100(COUICardMultiInputView.this).setEllipsize(null);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUICardMultiInputView$3, reason: invalid class name */
    public class AnonymousClass3 implements Runnable {
        public AnonymousClass3() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUICardMultiInputView.access$100(COUICardMultiInputView.this).setPadding(COUICardMultiInputView.access$100(COUICardMultiInputView.this).getPaddingLeft(), COUICardMultiInputView.access$100(COUICardMultiInputView.this).getPaddingTop(), COUICardMultiInputView.access$100(COUICardMultiInputView.this).getPaddingRight(), COUICardMultiInputView.access$200(COUICardMultiInputView.this).getMeasuredHeight());
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUICardMultiInputView$4, reason: invalid class name */
    public class AnonymousClass4 implements TextWatcher {
        public AnonymousClass4() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            int length = editable.length();
            if (length < COUICardMultiInputView.access$300(COUICardMultiInputView.this)) {
                COUICardMultiInputView.access$200(COUICardMultiInputView.this).setText(length + "/" + COUICardMultiInputView.access$300(COUICardMultiInputView.this));
                if (COUICardMultiInputView.access$400(COUICardMultiInputView.this)) {
                    COUICardMultiInputView cOUICardMultiInputView = COUICardMultiInputView.this;
                    COUICardMultiInputView.access$600(cOUICardMultiInputView, COUICardMultiInputView.access$500(cOUICardMultiInputView), COUIContextUtil.getAttrColor(COUICardMultiInputView.this.getContext(), R.attr.couiColorHintNeutral));
                    COUICardMultiInputView.access$100(COUICardMultiInputView.this).removeCallbacks(COUICardMultiInputView.access$700(COUICardMultiInputView.this));
                }
                COUICardMultiInputView.access$402(COUICardMultiInputView.this, false);
                return;
            }
            COUICardMultiInputView.access$200(COUICardMultiInputView.this).setText(COUICardMultiInputView.access$300(COUICardMultiInputView.this) + "/" + COUICardMultiInputView.access$300(COUICardMultiInputView.this));
            if (length > COUICardMultiInputView.access$300(COUICardMultiInputView.this)) {
                COUICardMultiInputView.access$100(COUICardMultiInputView.this).setText(editable.subSequence(0, COUICardMultiInputView.access$300(COUICardMultiInputView.this)));
            }
            COUICardMultiInputView cOUICardMultiInputView2 = COUICardMultiInputView.this;
            COUICardMultiInputView.access$600(cOUICardMultiInputView2, COUICardMultiInputView.access$500(cOUICardMultiInputView2), COUIContextUtil.getAttrColor(COUICardMultiInputView.this.getContext(), R.attr.couiColorError));
            COUICardMultiInputView.access$100(COUICardMultiInputView.this).removeCallbacks(COUICardMultiInputView.access$700(COUICardMultiInputView.this));
            COUICardMultiInputView.access$100(COUICardMultiInputView.this).postDelayed(COUICardMultiInputView.access$700(COUICardMultiInputView.this), 1000L);
            COUICardMultiInputView.access$402(COUICardMultiInputView.this, true);
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUICardMultiInputView$5, reason: invalid class name */
    public class AnonymousClass5 implements Runnable {
        public AnonymousClass5() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (COUICardMultiInputView.access$400(COUICardMultiInputView.this)) {
                COUICardMultiInputView cOUICardMultiInputView = COUICardMultiInputView.this;
                COUICardMultiInputView.access$600(cOUICardMultiInputView, COUICardMultiInputView.access$500(cOUICardMultiInputView), COUIContextUtil.getAttrColor(COUICardMultiInputView.this.getContext(), R.attr.couiColorHintNeutral));
                COUICardMultiInputView.access$402(COUICardMultiInputView.this, false);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUICardMultiInputView$6, reason: invalid class name */
    public class AnonymousClass6 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass6() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUICardMultiInputView.access$502(COUICardMultiInputView.this, ((Integer) valueAnimator.getAnimatedValue()).intValue());
            COUICardMultiInputView.access$200(COUICardMultiInputView.this).setTextColor(COUICardMultiInputView.access$500(COUICardMultiInputView.this));
        }
    }

    public COUICardMultiInputView(Context context) {
        this(context, null);
    }

    public static /* synthetic */ InputMethodManager access$000(COUICardMultiInputView cOUICardMultiInputView) {
        return cOUICardMultiInputView.mInputMethodManager;
    }

    public static /* synthetic */ InputMethodManager access$002(COUICardMultiInputView cOUICardMultiInputView, InputMethodManager inputMethodManager) {
        cOUICardMultiInputView.mInputMethodManager = inputMethodManager;
        return inputMethodManager;
    }

    public static /* synthetic */ COUIEditText access$100(COUICardMultiInputView cOUICardMultiInputView) {
        return cOUICardMultiInputView.mEditText;
    }

    public static /* synthetic */ TextView access$200(COUICardMultiInputView cOUICardMultiInputView) {
        return cOUICardMultiInputView.mCountTextView;
    }

    public static /* synthetic */ int access$300(COUICardMultiInputView cOUICardMultiInputView) {
        return cOUICardMultiInputView.mMaxCount;
    }

    public static /* synthetic */ boolean access$400(COUICardMultiInputView cOUICardMultiInputView) {
        return cOUICardMultiInputView.isErrorColor;
    }

    public static /* synthetic */ boolean access$402(COUICardMultiInputView cOUICardMultiInputView, boolean z2) {
        cOUICardMultiInputView.isErrorColor = z2;
        return z2;
    }

    public static /* synthetic */ int access$500(COUICardMultiInputView cOUICardMultiInputView) {
        return cOUICardMultiInputView.mCountTextColor;
    }

    public static /* synthetic */ int access$502(COUICardMultiInputView cOUICardMultiInputView, int i3) {
        cOUICardMultiInputView.mCountTextColor = i3;
        return i3;
    }

    public static /* synthetic */ void access$600(COUICardMultiInputView cOUICardMultiInputView, int i3, int i4) {
        cOUICardMultiInputView.executeColorAnimator(i3, i4);
    }

    public static /* synthetic */ Runnable access$700(COUICardMultiInputView cOUICardMultiInputView) {
        return cOUICardMultiInputView.setCountColorRunnable;
    }

    private void cancelColorAnimator() {
        ValueAnimator valueAnimator = this.mCountTextColorAnimator;
        if (valueAnimator == null || !valueAnimator.isRunning()) {
            return;
        }
        this.mCountTextColorAnimator.cancel();
    }

    private void executeColorAnimator(int i3, int i4) {
        cancelColorAnimator();
        ValueAnimator valueAnimatorOfInt = ValueAnimator.ofInt(i3, i4);
        this.mCountTextColorAnimator = valueAnimatorOfInt;
        valueAnimatorOfInt.addUpdateListener(new AnonymousClass6());
        this.mCountTextColorAnimator.setDuration(250L);
        this.mCountTextColorAnimator.setEvaluator(new ArgbEvaluator());
        this.mCountTextColorAnimator.start();
    }

    private void handleWithCount() {
        if (!this.mEnableInputCount || this.mMaxCount <= 0) {
            this.mCountTextView.setVisibility(8);
            COUIEditText cOUIEditText = this.mEditText;
            cOUIEditText.setPadding(cOUIEditText.getPaddingLeft(), this.mEditText.getPaddingTop(), this.mEditText.getPaddingRight(), this.mEditText.getPaddingTop());
            return;
        }
        this.mCountTextView.setVisibility(0);
        this.mCountTextView.setText(this.mEditText.getText().length() + "/" + this.mMaxCount);
        this.mEditText.post(new AnonymousClass3());
        this.mEditText.addTextChangedListener(new AnonymousClass4());
    }

    private void init() {
        if (this.mHintTextWatcher == null) {
            this.mHintTextWatcher = new AnonymousClass2();
        }
        this.mEditText.addTextChangedListener(this.mHintTextWatcher);
        this.mEditText.setTopHint(this.mHint);
        handleWithCount();
    }

    private void releaseCountColorRunnable() {
        Runnable runnable;
        COUIEditText cOUIEditText = this.mEditText;
        if (cOUIEditText == null || (runnable = this.setCountColorRunnable) == null) {
            return;
        }
        cOUIEditText.removeCallbacks(runnable);
    }

    public COUIEditText getEditText() {
        return this.mEditText;
    }

    public CharSequence getHint() {
        return this.mHint;
    }

    public int getLayoutResId() {
        return com.support.input.R.layout.coui_multi_input_card_view;
    }

    public COUIEditText instanceCOUIEditText(Context context, AttributeSet attributeSet) {
        context.getTheme().applyStyle(com.support.input.R.style.COUIMultiInputViewStyle, true);
        return new COUIEditText(context, attributeSet, com.support.input.R.attr.couiCardMultiInputEditTextStyle);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releaseCountColorRunnable();
        cancelColorAnimator();
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.mEditTextMaxHeight = (((this.mEdittextContainer.getMeasuredHeight() - this.mEdittextContainer.getPaddingTop()) - this.mEdittextContainer.getPaddingBottom()) - this.mEditText.getPaddingTop()) - this.mEditText.getPaddingBottom();
            boolean z2 = this.mEditText.getLineCount() * this.mEditText.getLineHeight() > this.mEditTextMaxHeight;
            if (this.mEdittextContainerRect.contains((int) motionEvent.getX(), (int) motionEvent.getY()) && z2 && this.mEditText.getLineCount() >= 1) {
                this.mEdittextContainer.requestDisallowInterceptTouchEvent(true);
            }
        }
        return false;
    }

    @Override // android.view.View.OnLayoutChangeListener
    public void onLayoutChange(View view, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        Rect rect = this.mEdittextContainerRect;
        rect.left = 0;
        rect.top = 0;
        rect.right = getMeasuredWidth();
        this.mEdittextContainerRect.bottom = getMeasuredHeight() - this.mEdittextContainer.getPaddingBottom();
    }

    public void setHint(CharSequence charSequence) {
        this.mHint = charSequence;
        this.mEditText.setTopHint(charSequence);
    }

    public void setMaxCount(int i3) {
        this.mMaxCount = i3;
        handleWithCount();
    }

    public COUICardMultiInputView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUICardMultiInputView(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.mHintTextWatcher = null;
        this.mEdittextContainerRect = new Rect();
        this.isErrorColor = false;
        this.setCountColorRunnable = new AnonymousClass5();
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, com.support.input.R.styleable.COUIInputView, i3, 0);
        this.mHint = typedArrayObtainStyledAttributes.getText(com.support.input.R.styleable.COUIInputView_couiHint);
        this.mMaxCount = typedArrayObtainStyledAttributes.getInt(com.support.input.R.styleable.COUIInputView_couiInputMaxCount, 0);
        this.mEnableInputCount = typedArrayObtainStyledAttributes.getBoolean(com.support.input.R.styleable.COUIInputView_couiEnableInputCount, false);
        typedArrayObtainStyledAttributes.recycle();
        LayoutInflater.from(getContext()).inflate(getLayoutResId(), (ViewGroup) this, true);
        this.mEdittextContainer = (LinearLayout) findViewById(com.support.input.R.id.edittext_container);
        COUIEditText cOUIEditTextInstanceCOUIEditText = instanceCOUIEditText(context, attributeSet);
        this.mEditText = cOUIEditTextInstanceCOUIEditText;
        cOUIEditTextInstanceCOUIEditText.setMaxLines(3);
        this.mEditText.setEllipsize(TextUtils.TruncateAt.END);
        this.mEditText.setGravity(8388659);
        this.mEdittextContainer.addView(this.mEditText, -1, -1);
        this.mEdittextContainer.addOnLayoutChangeListener(this);
        this.mCountTextView = (TextView) findViewById(com.support.input.R.id.input_count);
        int dimensionPixelSize = getResources().getDimensionPixelSize(com.support.input.R.dimen.support_preference_category_layout_title_margin_start);
        this.mCountTextColor = COUIContextUtil.getAttrColor(getContext(), R.attr.couiColorHintNeutral);
        findViewById(com.support.input.R.id.single_card).setOnTouchListener(new AnonymousClass1(dimensionPixelSize));
        init();
    }
}