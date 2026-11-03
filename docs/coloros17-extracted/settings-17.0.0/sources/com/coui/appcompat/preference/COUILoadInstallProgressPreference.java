package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import androidx.preference.PreferenceViewHolder;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.progressbar.COUIInstallLoadProgress;
import com.coui.appcompat.progressbar.COUILoadProgress;
import com.coui.appcompat.statelistutil.COUIStateListUtil;
import com.coui.appcompat.textutil.COUIChangeTextUtil;
import com.support.preference.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUILoadInstallProgressPreference extends COUIPreference {
    private final int mDisabledColor;
    private ColorStateList mInstallBackgroundColorStateList;
    private int mInstallProgressTextColor;
    private COUILoadProgress.OnStateChangeListener mListener;
    private COUIInstallLoadProgress mLoadProgress;
    private int mMax;
    private OnStateChangeListener mOnStateChangeListener;
    private int mProgress;
    private ColorStateList mProgressBackgroundColorStateList;
    private CharSequence mProgressText;
    private ColorStateList mProgressTextColorStateList;
    private int mProgressTextSize;
    private int mState;

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUILoadInstallProgressPreference$1, reason: invalid class name */
    public class AnonymousClass1 implements COUILoadProgress.OnStateChangeListener {
        public AnonymousClass1() {
        }

        @Override // com.coui.appcompat.progressbar.COUILoadProgress.OnStateChangeListener
        public void onStateChanged(COUILoadProgress cOUILoadProgress, int i3) {
            if (COUILoadInstallProgressPreference.access$000(COUILoadInstallProgressPreference.this) != null) {
                COUILoadInstallProgressPreference.access$102(COUILoadInstallProgressPreference.this, i3);
                COUILoadInstallProgressPreference.access$000(COUILoadInstallProgressPreference.this).onStateChanged(cOUILoadProgress, i3);
            }
        }
    }

    public interface OnStateChangeListener {
        void onStateChanged(COUILoadProgress cOUILoadProgress, int i3);
    }

    public COUILoadInstallProgressPreference(Context context) {
        this(context, null);
    }

    public static /* synthetic */ OnStateChangeListener access$000(COUILoadInstallProgressPreference cOUILoadInstallProgressPreference) {
        return cOUILoadInstallProgressPreference.mOnStateChangeListener;
    }

    public static /* synthetic */ int access$102(COUILoadInstallProgressPreference cOUILoadInstallProgressPreference, int i3) {
        cOUILoadInstallProgressPreference.mState = i3;
        return i3;
    }

    private int getInstallProgressTextColor() {
        return this.mInstallProgressTextColor;
    }

    public COUIInstallLoadProgress getLoadProgressView() {
        return this.mLoadProgress;
    }

    public int getMax() {
        COUIInstallLoadProgress cOUIInstallLoadProgress = this.mLoadProgress;
        if (cOUIInstallLoadProgress != null) {
            return cOUIInstallLoadProgress.getMax();
        }
        return 0;
    }

    public int getProgress() {
        COUIInstallLoadProgress cOUIInstallLoadProgress = this.mLoadProgress;
        if (cOUIInstallLoadProgress != null) {
            return cOUIInstallLoadProgress.getProgress();
        }
        return 0;
    }

    public CharSequence getProgressText() {
        return this.mProgressText;
    }

    public int getProgressTextSize() {
        return this.mProgressTextSize;
    }

    public int getState() {
        COUIInstallLoadProgress cOUIInstallLoadProgress = this.mLoadProgress;
        if (cOUIInstallLoadProgress != null) {
            return cOUIInstallLoadProgress.getState();
        }
        return 0;
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        COUIInstallLoadProgress cOUIInstallLoadProgress = (COUIInstallLoadProgress) preferenceViewHolder.findViewById(R.id.coui_load_progress);
        this.mLoadProgress = cOUIInstallLoadProgress;
        if (cOUIInstallLoadProgress != null) {
            cOUIInstallLoadProgress.setText(getProgressText().toString());
            this.mLoadProgress.setDefaultTextSize(getProgressTextSize());
            ColorStateList colorStateList = this.mProgressTextColorStateList;
            if (colorStateList != null) {
                this.mLoadProgress.setBtnTextColorStateList(colorStateList);
            }
            ColorStateList colorStateList2 = this.mProgressBackgroundColorStateList;
            if (colorStateList2 != null) {
                this.mLoadProgress.setThemeSecondaryColorStateList(colorStateList2);
            }
            ColorStateList colorStateList3 = this.mInstallBackgroundColorStateList;
            if (colorStateList3 != null) {
                this.mLoadProgress.setThemeColorStateList(colorStateList3);
            }
            if (getInstallProgressTextColor() != 0) {
                this.mLoadProgress.setBtnTextColorBySurpassProgress(getInstallProgressTextColor());
            }
            int i3 = this.mMax;
            if (i3 != 0) {
                this.mLoadProgress.setMax(i3);
            }
            this.mLoadProgress.setProgress(this.mProgress);
            this.mLoadProgress.setState(this.mState);
            this.mLoadProgress.setOnStateChangeListener(this.mListener);
        }
    }

    public void setBackgroundColor(int i3) {
        this.mProgressBackgroundColorStateList = COUIStateListUtil.createColorStateList(i3, this.mDisabledColor);
        notifyChanged();
    }

    public void setInstallBackgroundColor(int i3) {
        this.mInstallBackgroundColorStateList = COUIStateListUtil.createColorStateList(i3, this.mDisabledColor);
        notifyChanged();
    }

    public void setInstallProgressTextColor(int i3) {
        if (this.mInstallProgressTextColor != i3) {
            this.mInstallProgressTextColor = i3;
            notifyChanged();
        }
    }

    public void setMax(int i3) {
        if (this.mMax != i3) {
            this.mMax = i3;
            notifyChanged();
        }
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.mOnStateChangeListener = onStateChangeListener;
    }

    public void setProgress(int i3) {
        this.mProgress = i3;
        COUIInstallLoadProgress cOUIInstallLoadProgress = this.mLoadProgress;
        if (cOUIInstallLoadProgress != null) {
            cOUIInstallLoadProgress.setProgress(i3);
        }
    }

    public void setProgressText(CharSequence charSequence) {
        if (TextUtils.equals(charSequence, this.mProgressText)) {
            return;
        }
        this.mProgressText = charSequence;
        COUIInstallLoadProgress cOUIInstallLoadProgress = this.mLoadProgress;
        if (cOUIInstallLoadProgress != null) {
            cOUIInstallLoadProgress.setText(charSequence.toString());
        }
    }

    public void setProgressTextColor(int i3) {
        this.mProgressTextColorStateList = COUIStateListUtil.createColorStateList(i3, this.mDisabledColor);
        notifyChanged();
    }

    public void setProgressTextSize(int i3) {
        if (this.mProgressTextSize != i3) {
            this.mProgressTextSize = i3;
            notifyChanged();
        }
    }

    public void setState(int i3) {
        this.mState = i3;
        COUIInstallLoadProgress cOUIInstallLoadProgress = this.mLoadProgress;
        if (cOUIInstallLoadProgress != null) {
            cOUIInstallLoadProgress.setState(i3);
        }
    }

    public COUILoadInstallProgressPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiLoadInstallProgressPreferenceStyle);
    }

    public COUILoadInstallProgressPreference(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, R.style.Preference_COUI_COUILoadInstallProgressPreference);
    }

    public COUILoadInstallProgressPreference(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mListener = new AnonymousClass1();
        this.mProgressText = "";
        int color = COUIContextUtil.getColor(getContext(), com.support.appcompat.R.color.coui_color_disabled_neutral);
        this.mDisabledColor = color;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUILoadInstallProgressPreference, i3, i4);
        this.mProgressText = typedArrayObtainStyledAttributes.getText(R.styleable.COUILoadInstallProgressPreference_progressText);
        this.mProgressTextSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUILoadInstallProgressPreference_progressTextSize, context.getResources().getDimensionPixelSize(com.support.progressbar.R.dimen.coui_install_download_progress_textsize));
        this.mProgressTextSize = (int) COUIChangeTextUtil.getSuitableFontSize(this.mProgressTextSize, context.getResources().getConfiguration().fontScale, 2);
        int color2 = typedArrayObtainStyledAttributes.getColor(R.styleable.COUILoadInstallProgressPreference_progressTextColor, 0);
        int color3 = typedArrayObtainStyledAttributes.getColor(R.styleable.COUILoadInstallProgressPreference_backgroundColor, 0);
        int color4 = typedArrayObtainStyledAttributes.getColor(R.styleable.COUILoadInstallProgressPreference_installBackgroundColor, 0);
        this.mInstallProgressTextColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUILoadInstallProgressPreference_installProgressTextColor, 0);
        typedArrayObtainStyledAttributes.recycle();
        if (color2 != 0) {
            this.mProgressTextColorStateList = COUIStateListUtil.createColorStateList(color2, color);
        }
        if (color3 != 0) {
            this.mProgressBackgroundColorStateList = COUIStateListUtil.createColorStateList(color3, color);
        }
        if (color4 != 0) {
            this.mInstallBackgroundColorStateList = COUIStateListUtil.createColorStateList(color4, color);
        }
    }
}