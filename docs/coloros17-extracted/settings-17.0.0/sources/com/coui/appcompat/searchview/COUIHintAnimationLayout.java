package com.coui.appcompat.searchview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.core.view.animation.PathInterpolatorCompat;
import com.coui.appcompat.animation.COUIEaseInterpolator;
import com.google.android.material.internal.TextWatcherAdapter;
import com.support.toolbar.R;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes7.dex */
public class COUIHintAnimationLayout extends FrameLayout {
    private static final int DURATION_TIME = 250;
    private static final int INTERVAL_TIME = 3000;
    private static final int IN_DELAY_TIME = 150;
    private static final String TAG = "COUIHintAnimationLayout";
    boolean mAnimationIsWorking;
    private AnimatorSet mAnimatorSetIn;
    private AnimatorSet mAnimatorSetOut;
    private COUIHintAnimationChangeListener mCOUIHintAnimationChangeListener;
    private Runnable mChangeHintRunnable;
    private int mCouiSearchBarAnimationTranslateExtra;
    private int mCurrentHintIndex;
    private TextView mCurrentHintTextView;
    private int mCurrentPlayTime;
    private ObjectAnimator mFadeIn;
    private ObjectAnimator mFadeOut;
    private List<String> mHintStrings;
    private ColorStateList mHintTextColor;
    private TextView mHintTextViewFirst;
    private TextView mHintTextViewThen;
    private ObjectAnimator mMoveIn;
    private ObjectAnimator mMoveOut;
    boolean mNeedRePlay;
    private boolean mNeedStopAnimation;
    private int mRepeatCount;
    private EditText mSearchEditText;
    private String mTempQueryHint;
    private static final TimeInterpolator TRANSLATEINTERPOLATOR = PathInterpolatorCompat.create(0.3f, 0.0f, 0.2f, 1.0f);
    private static final COUIEaseInterpolator EASEINTERPOLATOR = new COUIEaseInterpolator();

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUIHintAnimationLayout$1, reason: invalid class name */
    public class AnonymousClass1 implements Runnable {
        public AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (COUIHintAnimationLayout.access$000(COUIHintAnimationLayout.this).isEmpty()) {
                return;
            }
            COUIHintAnimationLayout cOUIHintAnimationLayout = COUIHintAnimationLayout.this;
            COUIHintAnimationLayout.access$102(cOUIHintAnimationLayout, (COUIHintAnimationLayout.access$100(cOUIHintAnimationLayout) + 1) % COUIHintAnimationLayout.access$000(COUIHintAnimationLayout.this).size());
            COUIHintAnimationLayout cOUIHintAnimationLayout2 = COUIHintAnimationLayout.this;
            if (cOUIHintAnimationLayout2.mAnimationIsWorking) {
                COUIHintAnimationLayout.access$200(cOUIHintAnimationLayout2, (String) COUIHintAnimationLayout.access$000(cOUIHintAnimationLayout2).get(COUIHintAnimationLayout.access$100(COUIHintAnimationLayout.this)));
            }
            COUIHintAnimationLayout.this.postDelayed(this, 3000L);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUIHintAnimationLayout$2, reason: invalid class name */
    public class AnonymousClass2 extends TextWatcherAdapter {
        public AnonymousClass2() {
        }

        @Override // com.google.android.material.internal.TextWatcherAdapter, android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            if (TextUtils.isEmpty(COUIHintAnimationLayout.access$300(COUIHintAnimationLayout.this).getText().toString())) {
                if (TextUtils.isEmpty(COUIHintAnimationLayout.access$900(COUIHintAnimationLayout.this)) || COUIHintAnimationLayout.access$1000(COUIHintAnimationLayout.this)) {
                    return;
                }
                COUIHintAnimationLayout.access$1100(COUIHintAnimationLayout.this).setText(COUIHintAnimationLayout.access$900(COUIHintAnimationLayout.this));
                COUIHintAnimationLayout.access$1100(COUIHintAnimationLayout.this).setVisibility(0);
                COUIHintAnimationLayout.access$1200(COUIHintAnimationLayout.this).setVisibility(8);
                return;
            }
            COUIHintAnimationLayout cOUIHintAnimationLayout = COUIHintAnimationLayout.this;
            cOUIHintAnimationLayout.removeCallbacks(COUIHintAnimationLayout.access$400(cOUIHintAnimationLayout));
            COUIHintAnimationLayout.access$500(COUIHintAnimationLayout.this).setVisibility(8);
            COUIHintAnimationLayout.access$600(COUIHintAnimationLayout.this).setVisibility(8);
            COUIHintAnimationLayout.access$700(COUIHintAnimationLayout.this);
            COUIHintAnimationLayout.access$800(COUIHintAnimationLayout.this);
            COUIHintAnimationLayout.this.mAnimationIsWorking = false;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUIHintAnimationLayout$3, reason: invalid class name */
    public class AnonymousClass3 extends AnimatorListenerAdapter {
        public AnonymousClass3() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            COUIHintAnimationLayout cOUIHintAnimationLayout = COUIHintAnimationLayout.this;
            COUIHintAnimationLayout.access$1102(cOUIHintAnimationLayout, COUIHintAnimationLayout.access$1200(cOUIHintAnimationLayout));
            if (COUIHintAnimationLayout.access$1300(COUIHintAnimationLayout.this)) {
                COUIHintAnimationLayout.this.pauseHintsAnimation();
                COUIHintAnimationLayout.access$1302(COUIHintAnimationLayout.this, false);
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            super.onAnimationStart(animator);
            COUIHintAnimationLayout.access$1200(COUIHintAnimationLayout.this).setText(COUIHintAnimationLayout.access$900(COUIHintAnimationLayout.this));
            COUIHintAnimationLayout.access$1200(COUIHintAnimationLayout.this).setVisibility(0);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUIHintAnimationLayout$4, reason: invalid class name */
    public class AnonymousClass4 implements Runnable {
        public AnonymousClass4() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUIHintAnimationLayout.access$1400(COUIHintAnimationLayout.this).start();
        }
    }

    public interface COUIHintAnimationChangeListener {
        void hintAnimationChange(int i3, String str, TextView textView);
    }

    public COUIHintAnimationLayout(Context context) {
        this(context, null);
    }

    public static /* synthetic */ List access$000(COUIHintAnimationLayout cOUIHintAnimationLayout) {
        return cOUIHintAnimationLayout.mHintStrings;
    }

    public static /* synthetic */ int access$100(COUIHintAnimationLayout cOUIHintAnimationLayout) {
        return cOUIHintAnimationLayout.mCurrentHintIndex;
    }

    public static /* synthetic */ boolean access$1000(COUIHintAnimationLayout cOUIHintAnimationLayout) {
        return cOUIHintAnimationLayout.animationIsRunning();
    }

    public static /* synthetic */ int access$102(COUIHintAnimationLayout cOUIHintAnimationLayout, int i3) {
        cOUIHintAnimationLayout.mCurrentHintIndex = i3;
        return i3;
    }

    public static /* synthetic */ TextView access$1100(COUIHintAnimationLayout cOUIHintAnimationLayout) {
        return cOUIHintAnimationLayout.mCurrentHintTextView;
    }

    public static /* synthetic */ TextView access$1102(COUIHintAnimationLayout cOUIHintAnimationLayout, TextView textView) {
        cOUIHintAnimationLayout.mCurrentHintTextView = textView;
        return textView;
    }

    public static /* synthetic */ TextView access$1200(COUIHintAnimationLayout cOUIHintAnimationLayout) {
        return cOUIHintAnimationLayout.getNextHintTextView();
    }

    public static /* synthetic */ boolean access$1300(COUIHintAnimationLayout cOUIHintAnimationLayout) {
        return cOUIHintAnimationLayout.mNeedStopAnimation;
    }

    public static /* synthetic */ boolean access$1302(COUIHintAnimationLayout cOUIHintAnimationLayout, boolean z2) {
        cOUIHintAnimationLayout.mNeedStopAnimation = z2;
        return z2;
    }

    public static /* synthetic */ AnimatorSet access$1400(COUIHintAnimationLayout cOUIHintAnimationLayout) {
        return cOUIHintAnimationLayout.mAnimatorSetIn;
    }

    public static /* synthetic */ void access$200(COUIHintAnimationLayout cOUIHintAnimationLayout, String str) {
        cOUIHintAnimationLayout.applyHintChangeAnimation(str);
    }

    public static /* synthetic */ EditText access$300(COUIHintAnimationLayout cOUIHintAnimationLayout) {
        return cOUIHintAnimationLayout.mSearchEditText;
    }

    public static /* synthetic */ Runnable access$400(COUIHintAnimationLayout cOUIHintAnimationLayout) {
        return cOUIHintAnimationLayout.mChangeHintRunnable;
    }

    public static /* synthetic */ TextView access$500(COUIHintAnimationLayout cOUIHintAnimationLayout) {
        return cOUIHintAnimationLayout.mHintTextViewFirst;
    }

    public static /* synthetic */ TextView access$600(COUIHintAnimationLayout cOUIHintAnimationLayout) {
        return cOUIHintAnimationLayout.mHintTextViewThen;
    }

    public static /* synthetic */ void access$700(COUIHintAnimationLayout cOUIHintAnimationLayout) {
        cOUIHintAnimationLayout.cancelAnimation();
    }

    public static /* synthetic */ void access$800(COUIHintAnimationLayout cOUIHintAnimationLayout) {
        cOUIHintAnimationLayout.resetHintTextView();
    }

    public static /* synthetic */ String access$900(COUIHintAnimationLayout cOUIHintAnimationLayout) {
        return cOUIHintAnimationLayout.mTempQueryHint;
    }

    private boolean animationIsRunning() {
        AnimatorSet animatorSet = this.mAnimatorSetIn;
        if (animatorSet != null && animatorSet.isRunning()) {
            return true;
        }
        AnimatorSet animatorSet2 = this.mAnimatorSetOut;
        return animatorSet2 != null && animatorSet2.isRunning();
    }

    private void applyHintChangeAnimation(String str) {
        if (this.mCurrentHintTextView == null) {
            return;
        }
        int i3 = this.mCurrentPlayTime + 1;
        this.mCurrentPlayTime = i3;
        int i4 = this.mRepeatCount;
        if (i4 != -1 && i3 > i4) {
            pauseHintsAnimation();
            return;
        }
        this.mTempQueryHint = str;
        int measuredHeight = ((getMeasuredHeight() - this.mCurrentHintTextView.getLineHeight()) / 2) + this.mCouiSearchBarAnimationTranslateExtra;
        if (this.mAnimatorSetOut == null || this.mAnimatorSetIn == null) {
            ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(this.mCurrentHintTextView, "translationY", 0.0f, -measuredHeight);
            this.mMoveOut = objectAnimatorOfFloat;
            TimeInterpolator timeInterpolator = TRANSLATEINTERPOLATOR;
            objectAnimatorOfFloat.setInterpolator(timeInterpolator);
            ObjectAnimator objectAnimatorOfFloat2 = ObjectAnimator.ofFloat(this.mCurrentHintTextView, "alpha", 1.0f, 0.0f);
            this.mFadeOut = objectAnimatorOfFloat2;
            COUIEaseInterpolator cOUIEaseInterpolator = EASEINTERPOLATOR;
            objectAnimatorOfFloat2.setInterpolator(cOUIEaseInterpolator);
            AnimatorSet animatorSet = new AnimatorSet();
            this.mAnimatorSetOut = animatorSet;
            animatorSet.playTogether(this.mMoveOut, this.mFadeOut);
            this.mAnimatorSetOut.setDuration(250L);
            ObjectAnimator objectAnimatorOfFloat3 = ObjectAnimator.ofFloat(getNextHintTextView(), "translationY", measuredHeight, 0.0f);
            this.mMoveIn = objectAnimatorOfFloat3;
            objectAnimatorOfFloat3.setInterpolator(timeInterpolator);
            ObjectAnimator objectAnimatorOfFloat4 = ObjectAnimator.ofFloat(getNextHintTextView(), "alpha", 0.0f, 1.0f);
            this.mFadeIn = objectAnimatorOfFloat4;
            objectAnimatorOfFloat4.setInterpolator(cOUIEaseInterpolator);
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.mAnimatorSetIn = animatorSet2;
            animatorSet2.playTogether(this.mMoveIn, this.mFadeIn);
            this.mAnimatorSetIn.setDuration(250L);
            this.mAnimatorSetIn.addListener(new AnonymousClass3());
        } else {
            this.mMoveOut.setTarget(this.mCurrentHintTextView);
            this.mFadeOut.setTarget(this.mCurrentHintTextView);
            this.mMoveIn.setTarget(getNextHintTextView());
            this.mFadeIn.setTarget(getNextHintTextView());
        }
        postDelayed(new AnonymousClass4(), 150L);
        this.mAnimatorSetOut.start();
        COUIHintAnimationChangeListener cOUIHintAnimationChangeListener = this.mCOUIHintAnimationChangeListener;
        if (cOUIHintAnimationChangeListener != null) {
            cOUIHintAnimationChangeListener.hintAnimationChange(this.mCurrentHintIndex, str, this.mCurrentHintTextView);
        }
    }

    private void cancelAnimation() {
        AnimatorSet animatorSet = this.mAnimatorSetIn;
        if (animatorSet != null && animatorSet.isRunning()) {
            this.mAnimatorSetIn.cancel();
        }
        AnimatorSet animatorSet2 = this.mAnimatorSetOut;
        if (animatorSet2 == null || !animatorSet2.isRunning()) {
            return;
        }
        this.mAnimatorSetOut.cancel();
    }

    private TextView getNextHintTextView() {
        TextView textView = this.mCurrentHintTextView;
        TextView textView2 = this.mHintTextViewFirst;
        return textView == textView2 ? this.mHintTextViewThen : textView2;
    }

    private void initHintAnimationTextView() {
        if (this.mHintTextViewFirst == null && this.mHintTextViewThen == null) {
            Context context = getContext();
            int i3 = R.style.Widget_COUI_EditText_SearchViewStyle_HintText;
            this.mHintTextViewFirst = new TextView(new ContextThemeWrapper(context, i3), null);
            this.mHintTextViewThen = new TextView(new ContextThemeWrapper(getContext(), i3), null);
            this.mHintTextViewFirst.setImportantForAccessibility(2);
            this.mHintTextViewThen.setImportantForAccessibility(2);
            TextView textView = this.mHintTextViewFirst;
            int i4 = com.support.appcompat.R.style.couiTextAppearanceBodyL;
            textView.setTextAppearance(i4);
            this.mHintTextViewThen.setTextAppearance(i4);
            ColorStateList colorStateList = this.mHintTextColor;
            if (colorStateList != null) {
                this.mHintTextViewFirst.setTextColor(colorStateList);
                this.mHintTextViewThen.setTextColor(this.mHintTextColor);
            }
            updateHintTextViewEnabled(this.mHintTextViewFirst, isEnabled());
            updateHintTextViewEnabled(this.mHintTextViewThen, isEnabled());
            this.mHintTextViewFirst.setId(R.id.coui_hint_text_view_first);
            this.mHintTextViewThen.setId(R.id.coui_hint_text_view_then);
            addView(this.mHintTextViewFirst);
            addView(this.mHintTextViewThen);
            EditText editText = this.mSearchEditText;
            if (editText != null) {
                setTextSize((int) editText.getTextSize());
            }
        }
    }

    private void initTextHintAnimation() {
        if (this.mChangeHintRunnable == null) {
            this.mHintStrings = new ArrayList();
            this.mChangeHintRunnable = new AnonymousClass1();
            this.mSearchEditText.addTextChangedListener(new AnonymousClass2());
        }
    }

    private void resetHintTextView() {
        this.mHintTextViewFirst.setTranslationY(0.0f);
        this.mHintTextViewFirst.setAlpha(1.0f);
        this.mHintTextViewThen.setTranslationY(0.0f);
        this.mHintTextViewThen.setAlpha(1.0f);
    }

    private void updateHintTextViewEnabled(TextView textView, boolean z2) {
        if (textView == null) {
            return;
        }
        textView.setEnabled(z2);
    }

    public TextView getCurrentHintTextView() {
        return this.mCurrentHintTextView;
    }

    public List<String> getHintStrings() {
        return this.mHintStrings;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        resumeHintsAnimation();
        super.onAttachedToWindow();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        pauseHintsAnimation();
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    public void onWindowVisibilityChanged(int i3) {
        super.onWindowVisibilityChanged(i3);
        if (i3 == 0) {
            if (this.mNeedRePlay) {
                resumeHintsAnimation();
                this.mNeedRePlay = false;
                return;
            }
            return;
        }
        if (this.mAnimationIsWorking) {
            pauseHintsAnimation();
            this.mNeedRePlay = true;
        }
    }

    public void pauseHintsAnimation() {
        List<String> list;
        removeCallbacks(this.mChangeHintRunnable);
        if (!this.mAnimationIsWorking || (list = this.mHintStrings) == null || list.size() == 0) {
            Log.e(TAG, "pauseHintsAnimation return");
            return;
        }
        if (animationIsRunning()) {
            this.mNeedStopAnimation = true;
            return;
        }
        resetHintTextView();
        this.mAnimationIsWorking = false;
        if (TextUtils.isEmpty(this.mTempQueryHint)) {
            this.mHintTextViewFirst.setVisibility(8);
            this.mHintTextViewThen.setVisibility(8);
        } else {
            this.mCurrentHintTextView.setText(this.mTempQueryHint);
            this.mCurrentHintTextView.setVisibility(0);
            getNextHintTextView().setVisibility(8);
        }
    }

    public void resumeHintsAnimation() {
        setHintsAnimation(this.mHintStrings);
    }

    public void setCOUIHintAnimationChangeListener(COUIHintAnimationChangeListener cOUIHintAnimationChangeListener) {
        this.mCOUIHintAnimationChangeListener = cOUIHintAnimationChangeListener;
    }

    @Override // android.view.View
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        updateHintTextViewEnabled(this.mHintTextViewFirst, z2);
        updateHintTextViewEnabled(this.mHintTextViewThen, z2);
    }

    public void setHintTextColor(ColorStateList colorStateList) {
        if (colorStateList == null) {
            return;
        }
        this.mHintTextColor = colorStateList;
        TextView textView = this.mHintTextViewFirst;
        if (textView != null) {
            textView.setTextColor(colorStateList);
        }
        TextView textView2 = this.mHintTextViewThen;
        if (textView2 != null) {
            textView2.setTextColor(colorStateList);
        }
    }

    public void setHintsAnimation(List<String> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        if (this.mSearchEditText == null) {
            if (!(getChildAt(0) instanceof EditText)) {
                Log.e(TAG, "Before calling this method, you must ensure that there is an edittext object in the container:1, you can call setSearchEditText or add an edittext yourself, refer to COUISearchBar2, you can put an edittext object in xml ( Refer to coui_search_view_animated_support_layout)to use the related functions of this animation container");
                return;
            }
            this.mSearchEditText = (EditText) getChildAt(0);
        }
        if (!TextUtils.isEmpty(this.mSearchEditText.getText().toString())) {
            Log.e(TAG, "Setting hints animation content is invalid when the searchEdittext has a value");
            return;
        }
        initHintAnimationTextView();
        initTextHintAnimation();
        if (!this.mHintStrings.equals(list)) {
            this.mHintStrings.clear();
            this.mHintStrings.addAll(list);
            this.mCurrentHintIndex = 0;
            this.mTempQueryHint = null;
            this.mCurrentPlayTime = 0;
        }
        if (this.mCurrentHintTextView == null) {
            this.mCurrentHintTextView = this.mHintTextViewFirst;
        }
        if (TextUtils.isEmpty(this.mTempQueryHint)) {
            this.mTempQueryHint = this.mHintStrings.get(this.mCurrentHintIndex);
        }
        this.mCurrentHintTextView.setText(this.mTempQueryHint);
        this.mCurrentHintTextView.setVisibility(0);
        removeCallbacks(this.mChangeHintRunnable);
        this.mSearchEditText.setHint("");
        postDelayed(this.mChangeHintRunnable, 3000L);
        this.mAnimationIsWorking = true;
    }

    public void setRepeatCount(int i3) {
        if (i3 <= 0) {
            Log.e(TAG, "RepeatCount must be greater than zero");
        } else {
            this.mRepeatCount = i3;
        }
    }

    public void setSearchEditText(EditText editText) {
        this.mSearchEditText = editText;
        if (getChildCount() == 0) {
            addView(this.mSearchEditText, new FrameLayout.LayoutParams(-1, -1));
        } else {
            Log.e(TAG, "setSearchEditText() can only be executed once");
        }
    }

    public void setTextSize(int i3) {
        TextView textView = this.mHintTextViewFirst;
        if (textView == null || this.mHintTextViewThen == null) {
            return;
        }
        float f3 = i3;
        textView.setTextSize(0, f3);
        this.mHintTextViewThen.setTextSize(0, f3);
    }

    public COUIHintAnimationLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mCurrentHintIndex = 0;
        this.mAnimationIsWorking = false;
        this.mNeedRePlay = false;
        this.mNeedStopAnimation = false;
        this.mRepeatCount = -1;
        this.mCurrentPlayTime = 0;
        this.mCouiSearchBarAnimationTranslateExtra = context.getResources().getDimensionPixelSize(R.dimen.coui_search_bar_animation_translate_extra);
    }
}