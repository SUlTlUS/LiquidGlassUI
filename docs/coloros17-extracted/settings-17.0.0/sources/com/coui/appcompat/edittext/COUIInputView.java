package com.coui.appcompat.edittext;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.PathInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.settings.security.SecuritySettings;
import com.coui.appcompat.animation.COUIEaseInterpolator;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.uiutil.UIUtil;
import com.oplus.settings.feature.display.atmospherelight.AtmosphereLightConstants;
import com.support.appcompat.R;
import java.util.Locale;

/* JADX INFO: loaded from: classes7.dex */
public class COUIInputView extends ConstraintLayout {
    private static final int APPEAR_DURATION = 217;
    private static final int BUTTON_LAYOUT_MORE_PADDING = 3;
    private static final int COUNT_TEXTVIEW_MORE_PADDING = 10;
    private static final int COUNT_VIEW_PADDING = 8;
    private static final int DISAPPEAR_DURATION = 283;
    public static final int INPUT_TYPE_NUMBER = 1;
    public static final int INPUT_TYPE_NUMBER_PASSWORD = 2;
    public static final int INPUT_TYPE_TEXT = 0;
    private static final int MAX_BUTTON_ICON_COUNT = 2;
    private static final int MAX_LINE = 5;
    private static final int PASSWORD_STATUES_TYPE_CLOSE = 1;
    private static final int PASSWORD_STATUES_TYPE_OPEN = 0;
    private static final int SPACE_STEP = 4;
    protected View mButtonLayout;
    private ErrorStateChangeCallback mCallback;
    private Paint mCountPaint;
    protected TextView mCountTextView;
    private boolean mCustomFormat;
    private ImageButton mDeleteButton;
    private int mDeleteIconMarginEndWithPsd;
    private boolean mEditLineColor;
    protected COUIEditText mEditText;
    private LinearLayout mEdittextContainer;
    private boolean mEnableError;
    protected boolean mEnableInputCount;
    private boolean mEnablePassword;
    private TextView mErrorText;
    private ValueAnimator mHideErrorTextAnimator;
    private CharSequence mHint;
    protected int mInputType;
    protected int mMaxCount;
    protected OnEditTextChangeListener mOnEditTextChangeListener;
    private View.OnFocusChangeListener mOnFocusChangeListener;
    CheckBox mPasswordButton;
    private int mPasswordType;
    private PathInterpolator mPathInterpolator;
    private ValueAnimator mShowErrorTextAnimator;
    private String mSpaceString;
    private int mTextMinHeightInInputView;
    private TextWatcher mTextWatcher;
    protected CharSequence mTitle;
    protected TextView mTitleTextView;
    private Runnable mUpdateRunnable;
    private String replaceString;

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUIInputView$1, reason: invalid class name */
    public class AnonymousClass1 implements Runnable {
        public AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUIInputView cOUIInputView = COUIInputView.this;
            cOUIInputView.mEditText.setPaddingRelative(0, cOUIInputView.getEdittextPaddingTop(), COUIInputView.this.getEdittextPaddingEnd(), COUIInputView.this.getEdittextPaddingBottom());
            TextView textView = COUIInputView.this.mTitleTextView;
            textView.setPaddingRelative(textView.getPaddingStart(), COUIInputView.this.getTitlePaddingTop(), COUIInputView.this.mTitleTextView.getPaddingEnd(), COUIInputView.this.mTitleTextView.getPaddingBottom());
            UIUtil.setMargin(COUIInputView.this.mButtonLayout, 1, (COUIInputView.this.getEdittextPaddingTop() - COUIInputView.this.getEdittextPaddingBottom()) / 2);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUIInputView$10, reason: invalid class name */
    public class AnonymousClass10 extends AnimatorListenerAdapter {
        public AnonymousClass10() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            COUIInputView.access$800(COUIInputView.this).setVisibility(8);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUIInputView.access$800(COUIInputView.this).setVisibility(8);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUIInputView$2, reason: invalid class name */
    public class AnonymousClass2 implements View.OnClickListener {
        public AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (COUIInputView.this.mEditText.getTextDeleteListener() == null || !COUIInputView.this.mEditText.getTextDeleteListener().onTextDeleted()) {
                COUIInputView.this.mEditText.onFastDelete();
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUIInputView$3, reason: invalid class name */
    public class AnonymousClass3 implements COUIEditText.OnErrorStateChangedListener {
        public AnonymousClass3() {
        }

        @Override // com.coui.appcompat.edittext.COUIEditText.OnErrorStateChangedListener
        public void onErrorStateChangeAnimationEnd(boolean z2) {
        }

        @Override // com.coui.appcompat.edittext.COUIEditText.OnErrorStateChangedListener
        public void onErrorStateChanged(boolean z2) {
            COUIInputView.this.mEditText.setSelectAllOnFocus(z2);
            if (z2) {
                COUIInputView.access$000(COUIInputView.this);
            } else {
                COUIInputView.access$100(COUIInputView.this);
            }
            if (COUIInputView.access$200(COUIInputView.this) != null) {
                COUIInputView.access$200(COUIInputView.this).callback(z2);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUIInputView$4, reason: invalid class name */
    public class AnonymousClass4 implements TextWatcher {
        public AnonymousClass4() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            COUIInputView cOUIInputView = COUIInputView.this;
            if (cOUIInputView.mEnableInputCount && cOUIInputView.mMaxCount > 0) {
                OnEditTextChangeListener onEditTextChangeListener = cOUIInputView.mOnEditTextChangeListener;
                if (onEditTextChangeListener != null) {
                    onEditTextChangeListener.afterTextChange(editable);
                } else {
                    int length = editable.length();
                    COUIInputView cOUIInputView2 = COUIInputView.this;
                    if (length < cOUIInputView2.mMaxCount) {
                        cOUIInputView2.mCountTextView.setText(length + "/" + COUIInputView.this.mMaxCount);
                        COUIInputView cOUIInputView3 = COUIInputView.this;
                        cOUIInputView3.mCountTextView.setTextColor(COUIContextUtil.getAttrColor(cOUIInputView3.getContext(), R.attr.couiColorHintNeutral));
                    } else {
                        cOUIInputView2.mCountTextView.setText(COUIInputView.this.mMaxCount + "/" + COUIInputView.this.mMaxCount);
                        COUIInputView cOUIInputView4 = COUIInputView.this;
                        cOUIInputView4.mCountTextView.setTextColor(COUIContextUtil.getAttrColor(cOUIInputView4.getContext(), R.attr.couiColorError));
                        COUIInputView cOUIInputView5 = COUIInputView.this;
                        int i3 = cOUIInputView5.mMaxCount;
                        if (length > i3) {
                            cOUIInputView5.mEditText.setText(editable.subSequence(0, i3));
                        }
                    }
                }
            }
            COUIInputView cOUIInputView6 = COUIInputView.this;
            COUIInputView.access$500(cOUIInputView6, cOUIInputView6.hasFocus());
            COUIInputView.access$600(COUIInputView.this, true);
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
            if (COUIInputView.this.isIsCardSingleInput() && COUIInputView.access$300(COUIInputView.this)) {
                COUIInputView.access$400(COUIInputView.this, charSequence);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUIInputView$5, reason: invalid class name */
    public class AnonymousClass5 implements View.OnFocusChangeListener {
        public AnonymousClass5() {
        }

        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View view, boolean z2) {
            COUIInputView.access$500(COUIInputView.this, z2);
            COUIInputView.access$600(COUIInputView.this, true);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUIInputView$6, reason: invalid class name */
    public class AnonymousClass6 implements Runnable {
        public AnonymousClass6() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUIInputView.access$700(COUIInputView.this).setVisibility(0);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUIInputView$7, reason: invalid class name */
    public class AnonymousClass7 implements CompoundButton.OnCheckedChangeListener {
        public AnonymousClass7() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
            if (z2) {
                COUIInputView cOUIInputView = COUIInputView.this;
                int i3 = cOUIInputView.mInputType;
                if (i3 == 1 || i3 == 2) {
                    cOUIInputView.mEditText.setInputType(2);
                    return;
                } else {
                    cOUIInputView.mEditText.setInputType(AtmosphereLightConstants.EffectId.MUSIC);
                    return;
                }
            }
            COUIInputView cOUIInputView2 = COUIInputView.this;
            int i4 = cOUIInputView2.mInputType;
            if (i4 == 1 || i4 == 2) {
                cOUIInputView2.mEditText.setInputType(18);
            } else {
                cOUIInputView2.mEditText.setInputType(SecuritySettings.UNIFY_LOCK_CONFIRM_PROFILE_REQUEST);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUIInputView$8, reason: invalid class name */
    public class AnonymousClass8 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass8() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIInputView.access$800(COUIInputView.this).setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUIInputView$9, reason: invalid class name */
    public class AnonymousClass9 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass9() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIInputView.access$800(COUIInputView.this).setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    public interface ErrorStateChangeCallback {
        void callback(boolean z2);
    }

    public interface OnEditTextChangeListener {
        void afterTextChange(Editable editable);
    }

    public COUIInputView(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void a(COUIInputView cOUIInputView, View view, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        if (i9 - i7 != i5 - i3) {
            cOUIInputView.updatePadding(true);
        } else {
            cOUIInputView.getClass();
        }
    }

    public static /* synthetic */ void access$000(COUIInputView cOUIInputView) {
        cOUIInputView.showErrorMsgAnim();
    }

    public static /* synthetic */ void access$100(COUIInputView cOUIInputView) {
        cOUIInputView.hideErrorMsgAnim();
    }

    public static /* synthetic */ ErrorStateChangeCallback access$200(COUIInputView cOUIInputView) {
        return cOUIInputView.mCallback;
    }

    public static /* synthetic */ boolean access$300(COUIInputView cOUIInputView) {
        return cOUIInputView.mCustomFormat;
    }

    public static /* synthetic */ void access$400(COUIInputView cOUIInputView, CharSequence charSequence) {
        cOUIInputView.handleCustomStyleText(charSequence);
    }

    public static /* synthetic */ void access$500(COUIInputView cOUIInputView, boolean z2) {
        cOUIInputView.updateDeleteButton(z2);
    }

    public static /* synthetic */ void access$600(COUIInputView cOUIInputView, boolean z2) {
        cOUIInputView.updatePadding(z2);
    }

    public static /* synthetic */ ImageButton access$700(COUIInputView cOUIInputView) {
        return cOUIInputView.mDeleteButton;
    }

    public static /* synthetic */ TextView access$800(COUIInputView cOUIInputView) {
        return cOUIInputView.mErrorText;
    }

    private int getCountTextWidth() {
        if (!this.mEnableInputCount) {
            return 0;
        }
        if (this.mCountPaint == null) {
            Paint paint = new Paint();
            this.mCountPaint = paint;
            paint.setTextSize(this.mCountTextView.getTextSize());
        }
        return ((int) this.mCountPaint.measureText((String) this.mCountTextView.getText())) + 8;
    }

    private int getCustomButtonShowNum() {
        TextView textView;
        View view = this.mButtonLayout;
        if (!(view instanceof ViewGroup)) {
            return 0;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int i3 = 0;
        for (int i4 = 0; i4 < viewGroup.getChildCount(); i4++) {
            View childAt = viewGroup.getChildAt(i4);
            if (childAt.getVisibility() == 0 && (textView = this.mCountTextView) != null && textView.getId() != childAt.getId()) {
                i3++;
            }
        }
        return i3;
    }

    private void handleCustomStyleText(CharSequence charSequence) {
        if (Locale.getDefault().getLanguage().equals("zh")) {
            String str = this.mSpaceString;
            if (str == null || !str.equals(charSequence.toString())) {
                this.mSpaceString = charSequence.toString();
                boolean zIsCnMobileExact = RegexUtils.isCnMobileExact(charSequence);
                boolean zIsCnBankCardId = RegexUtils.isCnBankCardId(charSequence);
                if (!zIsCnMobileExact && !zIsCnBankCardId) {
                    resetCustomStyleText(charSequence);
                    return;
                }
                this.replaceString = charSequence.toString();
                SpannableString spannableString = new SpannableString(charSequence);
                int length = spannableString.length() / 4;
                for (int i3 = 0; i3 < length; i3++) {
                    if (zIsCnMobileExact) {
                        int i4 = (i3 + 1) * 4;
                        spannableString.setSpan(new CustomEditTextSpan(), i4 - 2, i4 - 1, 17);
                    } else {
                        int i5 = (i3 + 1) * 4;
                        spannableString.setSpan(new CustomEditTextSpan(), i5 - 1, i5, 17);
                    }
                }
                int selectionStart = this.mEditText.getSelectionStart();
                this.mEditText.setText(spannableString);
                COUIEditText cOUIEditText = this.mEditText;
                cOUIEditText.setSelection(Math.min(selectionStart, cOUIEditText.getText().length()));
            }
        }
    }

    private void handleWithError() {
        if (!this.mEnableError) {
            this.mErrorText.setVisibility(8);
            return;
        }
        if (!TextUtils.isEmpty(this.mErrorText.getText())) {
            this.mErrorText.setVisibility(0);
        }
        this.mEditText.addOnErrorStateChangedListener(new AnonymousClass3());
    }

    private void handleWithTitle() {
        if (TextUtils.isEmpty(this.mTitle)) {
            return;
        }
        this.mTitleTextView.setText(this.mTitle);
        this.mTitleTextView.setVisibility(0);
    }

    private void hideErrorMsgAnim() {
        ValueAnimator valueAnimator = this.mShowErrorTextAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mShowErrorTextAnimator.cancel();
        }
        if (this.mHideErrorTextAnimator == null) {
            ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
            this.mHideErrorTextAnimator = valueAnimatorOfFloat;
            valueAnimatorOfFloat.setDuration(283L).setInterpolator(this.mPathInterpolator);
            this.mHideErrorTextAnimator.addUpdateListener(new AnonymousClass9());
            this.mHideErrorTextAnimator.addListener(new AnonymousClass10());
        }
        if (this.mHideErrorTextAnimator.isStarted()) {
            this.mHideErrorTextAnimator.cancel();
        }
        this.mHideErrorTextAnimator.start();
    }

    private void init() {
        handleWithTitle();
        this.mEditText.setTopHint(this.mHint);
        if (this.mEditLineColor) {
            this.mEditText.setDefaultStrokeColor(COUIContextUtil.getAttrColor(getContext(), R.attr.couiColorPrimary));
        }
        handleWithCount();
        handleWithPassword();
        handleWithError();
        initDeleteButton();
        updatePadding(false);
    }

    private void initDeleteButton() {
        if (this.mDeleteButton == null || this.mEditText.isShowDeleteIcon()) {
            return;
        }
        this.mDeleteButton.setOnClickListener(new AnonymousClass2());
    }

    private void initListener() {
        View view = this.mButtonLayout;
        if (view != null) {
            view.addOnLayoutChangeListener(new a(this));
        }
    }

    private boolean isEnablePassword() {
        if (this.mPasswordButton.getVisibility() == 0) {
            return this.mEnablePassword;
        }
        return this.mEnablePassword && getCustomButtonShowNum() < 2;
    }

    private boolean isShowDeleteButton() {
        if (this.mDeleteButton.getVisibility() == 0) {
            return this.mEditText.isFastDeletable();
        }
        return this.mEditText.isFastDeletable() && getCustomButtonShowNum() < 2;
    }

    private void resetCustomStyleText(CharSequence charSequence) {
        if (this.replaceString != null) {
            String strValueOf = String.valueOf(charSequence);
            int selectionStart = this.mEditText.getSelectionStart();
            this.mEditText.setText(strValueOf);
            COUIEditText cOUIEditText = this.mEditText;
            cOUIEditText.setSelection(Math.min(selectionStart, cOUIEditText.getText().length()));
            this.replaceString = null;
        }
    }

    private void setInputType() {
        int i3 = this.mInputType;
        if (i3 == -1) {
            return;
        }
        if (i3 == 0) {
            this.mEditText.setInputType(1);
            return;
        }
        if (i3 == 1) {
            this.mEditText.setInputType(2);
        } else if (i3 != 2) {
            this.mEditText.setInputType(0);
        } else {
            this.mEditText.setInputType(18);
        }
    }

    private void showErrorMsgAnim() {
        ValueAnimator valueAnimator = this.mHideErrorTextAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mHideErrorTextAnimator.cancel();
        }
        this.mErrorText.setVisibility(0);
        if (this.mShowErrorTextAnimator == null) {
            ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.mShowErrorTextAnimator = valueAnimatorOfFloat;
            valueAnimatorOfFloat.setDuration(217L).setInterpolator(this.mPathInterpolator);
            this.mShowErrorTextAnimator.addUpdateListener(new AnonymousClass8());
        }
        if (this.mShowErrorTextAnimator.isStarted()) {
            this.mShowErrorTextAnimator.cancel();
        }
        this.mShowErrorTextAnimator.start();
    }

    private void updateDeleteButton(boolean z2) {
        if (this.mDeleteButton != null) {
            if (!isShowDeleteButton() || !z2 || TextUtils.isEmpty(this.mEditText.getText().toString())) {
                this.mDeleteButton.setVisibility(8);
            } else {
                if (UIUtil.isInVisibleRect(this.mDeleteButton)) {
                    return;
                }
                this.mDeleteButton.setVisibility(4);
                post(new AnonymousClass6());
            }
        }
    }

    private void updatePadding(boolean z2) {
        if (!z2) {
            this.mUpdateRunnable.run();
        } else {
            this.mEditText.removeCallbacks(this.mUpdateRunnable);
            this.mEditText.post(this.mUpdateRunnable);
        }
    }

    public void addCustomButton(View view) {
        View view2 = this.mButtonLayout;
        if (view2 == null || view == null || !(view2 instanceof ViewGroup)) {
            return;
        }
        ViewGroup viewGroup = (ViewGroup) view2;
        if (getCustomButtonShowNum() < 2) {
            int dimensionPixelSize = getResources().getDimensionPixelSize(com.support.input.R.dimen.coui_inputview_custom_button_size);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize);
            layoutParams.setMarginStart(this.mDeleteIconMarginEndWithPsd);
            layoutParams.setMarginEnd(0);
            viewGroup.addView(view, layoutParams);
            updatePadding(true);
        }
    }

    public TextView getCountTextView() {
        return this.mCountTextView;
    }

    public COUIEditText getEditText() {
        return this.mEditText;
    }

    public int getEdittextPaddingBottom() {
        return !TextUtils.isEmpty(this.mTitle) ? getResources().getDimensionPixelSize(com.support.input.R.dimen.coui_input_edit_error_text_has_title_padding_bottom) : (int) getResources().getDimension(R.dimen.coui_input_edit_text_no_title_padding_bottom);
    }

    public int getEdittextPaddingEnd() {
        return this.mButtonLayout.getWidth();
    }

    public int getEdittextPaddingTop() {
        return !TextUtils.isEmpty(this.mTitle) ? getResources().getDimensionPixelSize(com.support.input.R.dimen.coui_input_edit_text_has_title_padding_top) : (int) getResources().getDimension(R.dimen.coui_input_edit_text_no_title_padding_top);
    }

    public CharSequence getHint() {
        return this.mHint;
    }

    public int getLayoutResId() {
        return com.support.input.R.layout.coui_input_view;
    }

    public int getMaxCount() {
        return this.mMaxCount;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public int getTitlePaddingTop() {
        return getResources().getDimensionPixelSize(com.support.input.R.dimen.coui_input_preference_title_padding_top);
    }

    public void handleWithCount() {
        handleWithCountTextView();
        if (this.mTextWatcher == null) {
            AnonymousClass4 anonymousClass4 = new AnonymousClass4();
            this.mTextWatcher = anonymousClass4;
            this.mEditText.addTextChangedListener(anonymousClass4);
        }
        if (this.mOnFocusChangeListener == null) {
            AnonymousClass5 anonymousClass5 = new AnonymousClass5();
            this.mOnFocusChangeListener = anonymousClass5;
            this.mEditText.setOnFocusChangeListener(anonymousClass5);
        }
    }

    public void handleWithCountTextView() {
        if (!this.mEnableInputCount || this.mMaxCount <= 0) {
            this.mCountTextView.setVisibility(8);
            return;
        }
        this.mCountTextView.setVisibility(0);
        this.mCountTextView.setText(this.mEditText.getText().length() + "/" + this.mMaxCount);
    }

    public void handleWithPassword() {
        if (!isEnablePassword()) {
            this.mPasswordButton.setVisibility(8);
            setInputType();
            return;
        }
        this.mPasswordButton.setVisibility(0);
        if (this.mPasswordType == 1) {
            this.mPasswordButton.setChecked(false);
            int i3 = this.mInputType;
            if (i3 == 1 || i3 == 2) {
                this.mEditText.setInputType(18);
            } else {
                this.mEditText.setInputType(SecuritySettings.UNIFY_LOCK_CONFIRM_PROFILE_REQUEST);
            }
        } else {
            this.mPasswordButton.setChecked(true);
            int i4 = this.mInputType;
            if (i4 == 1 || i4 == 2) {
                this.mEditText.setInputType(2);
            } else {
                this.mEditText.setInputType(AtmosphereLightConstants.EffectId.MUSIC);
            }
        }
        this.mPasswordButton.setOnCheckedChangeListener(new AnonymousClass7());
    }

    public COUIEditText instanceCOUIEditText(Context context, AttributeSet attributeSet) {
        context.getTheme().applyStyle(com.support.input.R.style.COUIInputViewStyle, true);
        COUIEditText cOUIEditText = new COUIEditText(context, attributeSet, com.support.input.R.attr.couiInputPreferenceEditTextStyle);
        cOUIEditText.setShowDeleteIcon(false);
        cOUIEditText.setVerticalScrollBarEnabled(false);
        cOUIEditText.setMinHeight(this.mTextMinHeightInInputView);
        return cOUIEditText;
    }

    public boolean isEnableInputCount() {
        return this.mEnableInputCount;
    }

    public boolean isIsCardSingleInput() {
        return false;
    }

    public void lazyInit(Context context, AttributeSet attributeSet) {
        COUIEditText cOUIEditTextInstanceCOUIEditText = instanceCOUIEditText(context, attributeSet);
        this.mEditText = cOUIEditTextInstanceCOUIEditText;
        cOUIEditTextInstanceCOUIEditText.setMaxLines(5);
        this.mEdittextContainer.addView(this.mEditText, -1, -2);
        init();
    }

    public void nowInit(Context context, AttributeSet attributeSet) {
        lazyInit(context, attributeSet);
    }

    public void removeCustomButton(View view) {
        View view2 = this.mButtonLayout;
        if (view2 == null || view == null || !(view2 instanceof ViewGroup)) {
            return;
        }
        ((ViewGroup) view2).removeView(view);
        updatePadding(true);
    }

    public void setCustomFormat(Boolean bool) {
        this.mCustomFormat = bool.booleanValue();
        if (this.mEditText.getText() == null) {
            return;
        }
        if (isIsCardSingleInput() && this.mCustomFormat) {
            handleCustomStyleText(this.mEditText.getText());
        } else {
            resetCustomStyleText(this.mEditText.getText());
        }
    }

    public void setEnableError(boolean z2) {
        if (this.mEnableError != z2) {
            this.mEnableError = z2;
            handleWithError();
            updatePadding(false);
        }
    }

    public void setEnableInputCount(boolean z2) {
        this.mEnableInputCount = z2;
        handleWithCount();
        updatePadding(true);
    }

    public void setEnablePassword(boolean z2) {
        if (this.mEnablePassword != z2) {
            this.mEnablePassword = z2;
            handleWithPassword();
            updatePadding(true);
        }
    }

    @Override // android.view.View
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        this.mEditText.setEnabled(z2);
        this.mTitleTextView.setEnabled(z2);
        this.mButtonLayout.setEnabled(z2);
        this.mPasswordButton.setEnabled(z2);
        this.mCountTextView.setEnabled(z2);
    }

    public void setErrorStateChangeCallBack(ErrorStateChangeCallback errorStateChangeCallback) {
        this.mCallback = errorStateChangeCallback;
    }

    public void setHint(CharSequence charSequence) {
        this.mHint = charSequence;
        this.mEditText.setTopHint(charSequence);
    }

    public void setMaxCount(int i3) {
        this.mMaxCount = i3;
        handleWithCount();
    }

    public void setOnEditTextChangeListener(OnEditTextChangeListener onEditTextChangeListener) {
        this.mOnEditTextChangeListener = onEditTextChangeListener;
    }

    public void setPasswordType(int i3) {
        if (this.mPasswordType != i3) {
            this.mPasswordType = i3;
            handleWithPassword();
            updatePadding(true);
        }
    }

    public void setTitle(CharSequence charSequence) {
        if (charSequence == null || charSequence.equals(this.mTitle)) {
            return;
        }
        this.mTitle = charSequence;
        handleWithTitle();
        updatePadding(false);
    }

    public void showError(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            this.mEditText.setErrorState(false);
        } else {
            this.mEditText.setErrorState(true);
            if (this.mEnableError) {
                this.mErrorText.setVisibility(0);
            }
        }
        this.mErrorText.setText(charSequence);
    }

    public COUIInputView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIInputView(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.mOnEditTextChangeListener = null;
        this.mPathInterpolator = new COUIEaseInterpolator();
        this.mCountPaint = null;
        this.mEditLineColor = false;
        this.mCustomFormat = true;
        this.mUpdateRunnable = new AnonymousClass1();
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, com.support.input.R.styleable.COUIInputView, i3, 0);
        this.mTitle = typedArrayObtainStyledAttributes.getText(com.support.input.R.styleable.COUIInputView_couiTitle);
        this.mHint = typedArrayObtainStyledAttributes.getText(com.support.input.R.styleable.COUIInputView_couiHint);
        this.mEnablePassword = typedArrayObtainStyledAttributes.getBoolean(com.support.input.R.styleable.COUIInputView_couiEnablePassword, false);
        this.mPasswordType = typedArrayObtainStyledAttributes.getInt(com.support.input.R.styleable.COUIInputView_couiPasswordType, 0);
        this.mEnableError = typedArrayObtainStyledAttributes.getBoolean(com.support.input.R.styleable.COUIInputView_couiEnableError, false);
        this.mMaxCount = typedArrayObtainStyledAttributes.getInt(com.support.input.R.styleable.COUIInputView_couiInputMaxCount, 0);
        this.mEnableInputCount = typedArrayObtainStyledAttributes.getBoolean(com.support.input.R.styleable.COUIInputView_couiEnableInputCount, false);
        this.mInputType = typedArrayObtainStyledAttributes.getInt(com.support.input.R.styleable.COUIInputView_couiInputType, -1);
        this.mCustomFormat = typedArrayObtainStyledAttributes.getBoolean(com.support.input.R.styleable.COUIInputView_couiInputCustomFormat, true);
        this.mEditLineColor = typedArrayObtainStyledAttributes.getBoolean(com.support.input.R.styleable.COUIInputView_couiEditLineColor, false);
        typedArrayObtainStyledAttributes.recycle();
        LayoutInflater.from(getContext()).inflate(getLayoutResId(), (ViewGroup) this, true);
        this.mTitleTextView = (TextView) findViewById(com.support.input.R.id.title);
        this.mCountTextView = (TextView) findViewById(com.support.input.R.id.input_count);
        this.mErrorText = (TextView) findViewById(com.support.input.R.id.text_input_error);
        this.mButtonLayout = findViewById(com.support.input.R.id.button_layout);
        this.mEdittextContainer = (LinearLayout) findViewById(com.support.input.R.id.edittext_container);
        this.mDeleteButton = (ImageButton) findViewById(com.support.input.R.id.delete_button);
        this.mPasswordButton = (CheckBox) findViewById(com.support.input.R.id.checkbox_password);
        this.mDeleteIconMarginEndWithPsd = getResources().getDimensionPixelSize(com.support.input.R.dimen.coui_inputview_delete_button_margin_end_with_passwordicon);
        this.mTextMinHeightInInputView = getResources().getDimensionPixelOffset(com.support.input.R.dimen.coui_inputView_edittext_content_minheight);
        nowInit(context, attributeSet);
        initListener();
    }
}