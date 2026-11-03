package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.support.preference.R;

/* JADX INFO: loaded from: classes.dex */
public class COUIBottomPreference extends Preference {
    private int mPlaceholderHeight;

    public COUIBottomPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R.layout.coui_preference_bottom);
        TypedArray typedArrayObtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.COUIBottomPreference, 0, 0);
        this.mPlaceholderHeight = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIBottomPreference_placeholderHeight, getContext().getResources().getDimensionPixelSize(R.dimen.support_preference_foot_preference_padding_bottom));
        typedArrayObtainStyledAttributes.recycle();
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ViewGroup.LayoutParams layoutParams = preferenceViewHolder.itemView.getLayoutParams();
        int i3 = layoutParams.height;
        int i4 = this.mPlaceholderHeight;
        if (i3 != i4) {
            layoutParams.height = i4;
            preferenceViewHolder.itemView.setLayoutParams(layoutParams);
        }
    }

    public void setPlaceholderHeight(int i3) {
        if (this.mPlaceholderHeight != i3) {
            this.mPlaceholderHeight = i3;
            notifyChanged();
        }
    }
}