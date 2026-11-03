package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.PreferenceViewHolder;
import com.coui.appcompat.button.COUIButton;
import com.support.preference.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIButtonPreference extends COUIPreference {
    private int mDrawableColor;
    private View.OnClickListener mListener;
    private OnButtonClickListener mOnButtonClickListener;
    private CharSequence mText;
    private int mTextColor;
    private int mTextSize;

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIButtonPreference$1, reason: invalid class name */
    public class AnonymousClass1 implements View.OnClickListener {
        public AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (COUIButtonPreference.access$000(COUIButtonPreference.this) != null) {
                COUIButtonPreference.access$000(COUIButtonPreference.this).onButtonClick();
            }
        }
    }

    public interface OnButtonClickListener {
        void onButtonClick();
    }

    public COUIButtonPreference(Context context) {
        this(context, null);
    }

    public static /* synthetic */ OnButtonClickListener access$000(COUIButtonPreference cOUIButtonPreference) {
        return cOUIButtonPreference.mOnButtonClickListener;
    }

    public CharSequence getBtnText() {
        return this.mText;
    }

    public int getDrawableColor() {
        return this.mDrawableColor;
    }

    public int getTextColor() {
        return this.mTextColor;
    }

    public int getTextSize() {
        return this.mTextSize;
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        COUIButton cOUIButton = (COUIButton) preferenceViewHolder.findViewById(R.id.coui_btn);
        if (cOUIButton != null) {
            cOUIButton.setText(getBtnText());
            cOUIButton.setTextSize(getTextSize());
            if (getTextColor() != 0) {
                cOUIButton.setTextColor(getTextColor());
            }
            if (getDrawableColor() != 0) {
                cOUIButton.setDrawableColor(getDrawableColor());
            }
            cOUIButton.setOnClickListener(this.mListener);
        }
    }

    public void setBtnText(CharSequence charSequence) {
        if (TextUtils.equals(charSequence, this.mText)) {
            return;
        }
        this.mText = charSequence;
        notifyChanged();
    }

    public void setDrawableColor(int i3) {
        if (this.mDrawableColor != i3) {
            this.mDrawableColor = i3;
            notifyChanged();
        }
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.mOnButtonClickListener = onButtonClickListener;
    }

    public void setTextColor(int i3) {
        if (this.mTextColor != i3) {
            this.mTextColor = i3;
            notifyChanged();
        }
    }

    public void setTextSize(int i3) {
        if (this.mTextSize != i3) {
            this.mTextSize = i3;
            notifyChanged();
        }
    }

    public COUIButtonPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiButtonPreferenceStyle);
    }

    public COUIButtonPreference(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, R.style.Preference_COUI_COUIButtonPreference);
    }

    public COUIButtonPreference(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mListener = new AnonymousClass1();
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUIButtonPreference, i3, i4);
        this.mText = typedArrayObtainStyledAttributes.getText(R.styleable.COUIButtonPreference_btnText);
        this.mTextSize = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIButtonPreference_btnTextSize, 14);
        this.mTextColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIButtonPreference_btnTextColor, 0);
        this.mDrawableColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIButtonPreference_btnDrawableColor, 0);
        typedArrayObtainStyledAttributes.recycle();
    }
}