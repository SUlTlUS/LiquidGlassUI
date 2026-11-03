package com.coui.appcompat.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.log.COUILog;
import com.oplus.anim.EffectiveAnimationView;
import com.support.progressbar.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUICompProgressIndicator extends LinearLayout {
    public static final int LARGE_ANIMATION = 0;
    public static final int LARGE_ANIMATION_WITH_TEXT_VERTICAL = 3;
    public static final int SMALL_ANIMATION = 1;
    public static final int SMALL_ANIMATION_WITH_TEXT_HORIZONTAL = 2;
    public static final int SMALL_ANIMATION_WITH_TEXT_VERTICAL = 4;
    private static final String TAG = "COUICompProgressIndicator";
    private static final int TEXTSIZE_DP_VALUE = 12;
    private AttributeSet mAttrs;
    private boolean mAutoPlay;
    private Context mContext;
    private int mCouiLargeLoadingTextviewTopMargin;
    private int mCouiLoadingTextviewBottomMargin;
    private int mCouiLoadingTextviewLeftMargin;
    private int mCouiLoadingType;
    private String mCouiLottieLoadingJsonName;
    private int mCouiLottieLoadingRawRes;
    private int mCouiLottieLoadingViewHeight;
    private int mCouiLottieLoadingViewHeightMax;
    private int mCouiLottieLoadingViewWidth;
    private int mCouiLottieLoadingViewWidthMax;
    private int mCouiSmallLoadingTextviewTopMargin;
    private int mCouiSmallLottieLoadingViewHeight;
    private int mCouiSmallLottieLoadingViewWidth;
    private int mDefStyle;
    private int mDefaultType;
    private EffectiveAnimationView mEffectViewAnimationView;
    private String mLoadingTips;
    private boolean mNeedRePlay;
    private int mRepeatCount;
    private boolean mTextFix;
    private TextView mTipsTextView;

    public COUICompProgressIndicator(Context context) {
        this(context, null);
    }

    private void addEffectiveView(boolean z2) {
        EffectiveAnimationView effectiveAnimationView = new EffectiveAnimationView(this.mContext);
        this.mEffectViewAnimationView = effectiveAnimationView;
        effectiveAnimationView.setRepeatCount(this.mRepeatCount);
        LinearLayout.LayoutParams layoutParams = z2 ? new LinearLayout.LayoutParams(this.mCouiLottieLoadingViewWidth, this.mCouiLottieLoadingViewHeight) : new LinearLayout.LayoutParams(this.mCouiSmallLottieLoadingViewWidth, this.mCouiSmallLottieLoadingViewHeight);
        layoutParams.gravity = 17;
        this.mEffectViewAnimationView.setLayoutParams(layoutParams);
        if (!TextUtils.isEmpty(this.mCouiLottieLoadingJsonName)) {
            this.mEffectViewAnimationView.setAnimation(this.mCouiLottieLoadingJsonName);
        }
        int i3 = this.mCouiLottieLoadingRawRes;
        if (i3 != -1) {
            this.mEffectViewAnimationView.setAnimation(i3);
        }
        addView(this.mEffectViewAnimationView);
        if (this.mAutoPlay) {
            this.mEffectViewAnimationView.playAnimation();
        }
    }

    private void addSubView() {
        int i3 = this.mCouiLoadingType;
        if (i3 == 0) {
            addEffectiveView(true);
            return;
        }
        if (i3 == 1) {
            addEffectiveView(false);
            return;
        }
        if (i3 == 2) {
            setOrientation(0);
            addEffectiveView(false);
            addTipsTextView(false);
        } else if (i3 == 3) {
            addEffectiveView(true);
            addTipsTextView(true);
        } else {
            if (i3 != 4) {
                return;
            }
            addEffectiveView(false);
            addTipsTextView(true);
        }
    }

    private void addTipsTextView(boolean z2) {
        TextView textView = new TextView(new ContextThemeWrapper(this.mContext, z2 ? R.style.Widget_COUI_COUICompProgressIndicator_TipsTextView_Vertical : R.style.Widget_COUI_COUICompProgressIndicator_TipsTextView));
        this.mTipsTextView = textView;
        textView.setText(this.mLoadingTips);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        int i3 = this.mCouiLoadingType;
        if (i3 == 2) {
            layoutParams.setMarginStart(this.mCouiLoadingTextviewLeftMargin);
        } else if (i3 == 3) {
            layoutParams.setMargins(0, this.mCouiLargeLoadingTextviewTopMargin, 0, this.mCouiLoadingTextviewBottomMargin);
        } else if (i3 == 4) {
            layoutParams.setMargins(0, this.mCouiSmallLoadingTextviewTopMargin, 0, this.mCouiLoadingTextviewBottomMargin);
        }
        if (this.mTextFix) {
            this.mTipsTextView.setTextSize(1, 12.0f);
        }
        addView(this.mTipsTextView, layoutParams);
    }

    public EffectiveAnimationView getAnimationView() {
        return this.mEffectViewAnimationView;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mEffectViewAnimationView == null) {
            addSubView();
        }
        EffectiveAnimationView effectiveAnimationView = this.mEffectViewAnimationView;
        if (effectiveAnimationView == null || !this.mNeedRePlay) {
            return;
        }
        effectiveAnimationView.resumeAnimation();
        this.mNeedRePlay = false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EffectiveAnimationView effectiveAnimationView = this.mEffectViewAnimationView;
        if (effectiveAnimationView == null || !effectiveAnimationView.isAnimating()) {
            return;
        }
        this.mNeedRePlay = true;
        this.mEffectViewAnimationView.pauseAnimation();
    }

    @Override // android.view.View
    public void onWindowVisibilityChanged(int i3) {
        super.onWindowVisibilityChanged(i3);
        EffectiveAnimationView effectiveAnimationView = this.mEffectViewAnimationView;
        if (effectiveAnimationView != null) {
            if (i3 != 0) {
                if (effectiveAnimationView.isAnimating()) {
                    this.mNeedRePlay = true;
                    this.mEffectViewAnimationView.pauseAnimation();
                    return;
                }
                return;
            }
            if (this.mNeedRePlay) {
                effectiveAnimationView.resumeAnimation();
                this.mNeedRePlay = false;
            }
        }
    }

    public void refresh() {
        if (this.mAttrs != null || this.mDefStyle != 0) {
            TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(this.mAttrs, R.styleable.COUICompProgressIndicator, this.mDefStyle, 0);
            this.mCouiLottieLoadingJsonName = typedArrayObtainStyledAttributes.getString(R.styleable.COUICompProgressIndicator_couiLottieLoadingJsonName);
            this.mCouiLottieLoadingRawRes = typedArrayObtainStyledAttributes.getResourceId(R.styleable.COUICompProgressIndicator_couiLottieLoadingRawRes, -1);
            typedArrayObtainStyledAttributes.recycle();
        }
        if (TextUtils.isEmpty(this.mCouiLottieLoadingJsonName)) {
            this.mCouiLottieLoadingJsonName = COUIContextUtil.getAttrString(this.mContext, com.support.appcompat.R.attr.couiRotatingSpinnerJsonName);
        }
        if (!TextUtils.isEmpty(this.mCouiLottieLoadingJsonName)) {
            this.mEffectViewAnimationView.setAnimation(this.mCouiLottieLoadingJsonName);
        }
        int i3 = this.mCouiLottieLoadingRawRes;
        if (i3 != -1) {
            this.mEffectViewAnimationView.setAnimation(i3);
        }
        if (this.mAutoPlay) {
            this.mEffectViewAnimationView.playAnimation();
        }
        TextView textView = this.mTipsTextView;
        if (textView != null) {
            textView.setTextColor(COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorLabelSecondary));
        }
    }

    public void setLoadingTips(String str) {
        this.mLoadingTips = str;
        TextView textView = this.mTipsTextView;
        if (textView != null) {
            textView.setText(str);
        } else {
            Log.e(TAG, "This method only takes effect when mCouiLoadingType is SMALL_ANIMATION_WITH_TEXT_HORIZONTAL 、LARGE_ANIMATION_WITH_TEXT_VERTICAL、SMALL_ANIMATION_WITH_TEXT_VERTICAL");
        }
    }

    public void setLoadingType(int i3) {
        this.mCouiLoadingType = i3;
    }

    public COUICompProgressIndicator(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUICompProgressIndicator(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0);
    }

    public COUICompProgressIndicator(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mDefaultType = 0;
        this.mTextFix = false;
        this.mCouiLottieLoadingRawRes = -1;
        this.mRepeatCount = -1;
        this.mAutoPlay = true;
        this.mNeedRePlay = false;
        this.mAttrs = attributeSet;
        this.mDefStyle = i3;
        this.mContext = context;
        this.mCouiLottieLoadingViewWidthMax = getResources().getDimensionPixelSize(R.dimen.coui_loading_max_large_width);
        this.mCouiLottieLoadingViewHeightMax = getResources().getDimensionPixelSize(R.dimen.coui_loading_max_large_height);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUICompProgressIndicator, i3, 0);
        this.mCouiLoadingType = typedArrayObtainStyledAttributes.getInt(R.styleable.COUICompProgressIndicator_couiLoadingType, this.mDefaultType);
        this.mLoadingTips = typedArrayObtainStyledAttributes.getString(R.styleable.COUICompProgressIndicator_loadingTips);
        this.mCouiLottieLoadingJsonName = typedArrayObtainStyledAttributes.getString(R.styleable.COUICompProgressIndicator_couiLottieLoadingJsonName);
        this.mCouiLottieLoadingRawRes = typedArrayObtainStyledAttributes.getResourceId(R.styleable.COUICompProgressIndicator_couiLottieLoadingRawRes, -1);
        this.mRepeatCount = typedArrayObtainStyledAttributes.getInt(R.styleable.COUICompProgressIndicator_couiRepeatCount, this.mRepeatCount);
        this.mAutoPlay = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUICompProgressIndicator_couiAutoPlay, this.mAutoPlay);
        this.mCouiLottieLoadingViewWidth = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUICompProgressIndicator_couiLottieLoadingViewWidth, getResources().getDimensionPixelOffset(R.dimen.coui_loading_large_width));
        this.mCouiLottieLoadingViewHeight = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUICompProgressIndicator_couiLottieLoadingViewHeight, getResources().getDimensionPixelOffset(R.dimen.coui_loading_large_height));
        this.mCouiSmallLottieLoadingViewWidth = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUICompProgressIndicator_couiSmallLottieLoadingViewWidth, getResources().getDimensionPixelOffset(R.dimen.coui_loading_small_width));
        this.mCouiSmallLottieLoadingViewHeight = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUICompProgressIndicator_couiSmallLottieLoadingViewHeight, getResources().getDimensionPixelOffset(R.dimen.coui_loading_small_height));
        int i5 = this.mCouiLottieLoadingViewWidth;
        int i6 = this.mCouiLottieLoadingViewWidthMax;
        if (i5 > i6) {
            this.mCouiLottieLoadingViewWidth = i6;
            COUILog.v(TAG, "couiLottieLoadingViewWidth Cannot be larger than 40 dp");
        }
        int i7 = this.mCouiLottieLoadingViewHeight;
        int i8 = this.mCouiLottieLoadingViewHeightMax;
        if (i7 > i8) {
            this.mCouiLottieLoadingViewHeight = i8;
            COUILog.v(TAG, "couiLottieLoadingViewHeight Cannot be larger than 40 dp");
        }
        int i9 = this.mCouiSmallLottieLoadingViewWidth;
        int i10 = this.mCouiLottieLoadingViewWidthMax;
        if (i9 > i10) {
            this.mCouiSmallLottieLoadingViewWidth = i10;
            COUILog.v(TAG, "couiSmallLottieLoadingViewWidth Cannot be larger than 40 dp");
        }
        int i11 = this.mCouiSmallLottieLoadingViewHeight;
        int i12 = this.mCouiLottieLoadingViewHeightMax;
        if (i11 > i12) {
            this.mCouiSmallLottieLoadingViewHeight = i12;
            COUILog.v(TAG, "couiSmallLottieLoadingViewHeight Cannot be larger than 40 dp");
        }
        if (TextUtils.isEmpty(this.mCouiLottieLoadingJsonName)) {
            this.mCouiLottieLoadingJsonName = COUIContextUtil.getAttrString(this.mContext, com.support.appcompat.R.attr.couiRotatingSpinnerJsonName);
        }
        this.mTextFix = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUICompProgressIndicator_couiTextFix, this.mTextFix);
        typedArrayObtainStyledAttributes.recycle();
        this.mCouiLoadingTextviewLeftMargin = context.getResources().getDimensionPixelSize(R.dimen.coui_loading_textview_left_margin);
        this.mCouiLargeLoadingTextviewTopMargin = context.getResources().getDimensionPixelSize(R.dimen.coui_loading_textview_top_margin);
        this.mCouiSmallLoadingTextviewTopMargin = context.getResources().getDimensionPixelSize(R.dimen.coui_loading_textview_top_margin_small);
        this.mCouiLoadingTextviewBottomMargin = context.getResources().getDimensionPixelSize(R.dimen.coui_loading_textview_bottom_margin);
        setGravity(17);
        setOrientation(1);
    }

    public void setLoadingTips(int i3) {
        setLoadingTips(this.mContext.getString(i3));
    }
}