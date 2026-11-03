package com.coui.appcompat.preference;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.preference.PreferenceViewHolder;
import com.coui.appcompat.textview.COUITextView;
import com.oplus.anim.EffectiveAnimationView;
import com.support.preference.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIStatusSwitchingPreference extends COUIPreference {
    private static final int STATUS_TYPE_ANIM = 1;
    private static final int STATUS_TYPE_DEFAULT = 0;
    private static final int STATUS_TYPE_IMAGE = 3;
    private static final int STATUS_TYPE_TEXT = 2;
    private EffectiveAnimationView mAnimView;
    private int mAnimViewHeight;
    private int mAnimViewWidth;
    private int mCurrentType;
    private Drawable mImage;
    private ImageView mImageView;
    private int mRawResId;
    private CharSequence mText;
    private COUITextView mTextView;

    public COUIStatusSwitchingPreference(Context context) {
        this(context, null);
    }

    private void executePlayAnim() {
        EffectiveAnimationView effectiveAnimationView = this.mAnimView;
        if (effectiveAnimationView != null) {
            effectiveAnimationView.setVisibility(0);
            this.mTextView.setVisibility(8);
            this.mImageView.setVisibility(8);
            setAnimViewSize();
            playAnimView();
        }
    }

    private void executeShowImage() {
        if (this.mImageView == null || this.mImage == null) {
            return;
        }
        stopAnim();
        this.mAnimView.setVisibility(8);
        this.mTextView.setVisibility(8);
        this.mImageView.setVisibility(0);
        this.mImageView.setImageDrawable(this.mImage);
    }

    private void executeShowText() {
        if (this.mTextView != null) {
            stopAnim();
            this.mAnimView.setVisibility(8);
            this.mTextView.setVisibility(0);
            this.mImageView.setVisibility(8);
            this.mTextView.setText(this.mText);
        }
    }

    private void playAnimView() {
        stopAnim();
        this.mAnimView.setAnimation(this.mRawResId);
        this.mAnimView.loop(true);
        this.mAnimView.playAnimation();
    }

    private void setAnimViewSize() {
        ViewGroup.LayoutParams layoutParams = this.mAnimView.getLayoutParams();
        layoutParams.width = this.mAnimViewWidth;
        layoutParams.height = this.mAnimViewHeight;
        this.mAnimView.setLayoutParams(layoutParams);
    }

    private void stopAnim() {
        EffectiveAnimationView effectiveAnimationView = this.mAnimView;
        if (effectiveAnimationView == null || !effectiveAnimationView.isAnimating()) {
            return;
        }
        this.mAnimView.cancelAnimation();
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.mAnimView = (EffectiveAnimationView) preferenceViewHolder.findViewById(R.id.coui_anim);
        this.mTextView = (COUITextView) preferenceViewHolder.findViewById(R.id.coui_text);
        this.mImageView = (ImageView) preferenceViewHolder.findViewById(R.id.coui_image);
        int i3 = this.mCurrentType;
        if (i3 == 1) {
            executePlayAnim();
        } else if (i3 == 2) {
            executeShowText();
        } else {
            if (i3 != 3) {
                return;
            }
            executeShowImage();
        }
    }

    public void showAnim(int i3) {
        this.mCurrentType = 1;
        this.mRawResId = i3;
        notifyChanged();
    }

    public void showIcon(Drawable drawable) {
        if (drawable != this.mImage) {
            this.mCurrentType = 3;
            this.mImage = drawable;
            notifyChanged();
        }
    }

    public void showText(CharSequence charSequence) {
        if (TextUtils.equals(charSequence, this.mText)) {
            return;
        }
        this.mCurrentType = 2;
        this.mText = charSequence;
        notifyChanged();
    }

    public COUIStatusSwitchingPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiStatusSwitchingPreferenceStyle);
    }

    public COUIStatusSwitchingPreference(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, i3);
    }

    public COUIStatusSwitchingPreference(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3);
        this.mCurrentType = 0;
        this.mAnimViewWidth = -2;
        this.mAnimViewHeight = -2;
    }

    public void showAnim(int i3, int i4, int i5) {
        this.mCurrentType = 1;
        this.mRawResId = i3;
        this.mAnimViewWidth = i4;
        this.mAnimViewHeight = i5;
        notifyChanged();
    }
}