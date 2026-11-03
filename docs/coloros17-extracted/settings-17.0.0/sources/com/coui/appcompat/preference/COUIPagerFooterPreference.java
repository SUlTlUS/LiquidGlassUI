package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.support.preference.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIPagerFooterPreference extends Preference {
    private boolean mIsEnableClickSpan;
    private boolean mWithExtraMarginBottom;

    public COUIPagerFooterPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mWithExtraMarginBottom = true;
        setLayoutResource(R.layout.coui_pager_footer_preference);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUIPagerFooterPreference, 0, 0);
        this.mWithExtraMarginBottom = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIPagerFooterPreference_withExtraMarginBottom, this.mWithExtraMarginBottom);
        typedArrayObtainStyledAttributes.recycle();
        TypedArray typedArrayObtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R.styleable.COUIPreference, 0, 0);
        this.mIsEnableClickSpan = typedArrayObtainStyledAttributes2.getBoolean(R.styleable.COUIPreference_couiEnalbeClickSpan, false);
        typedArrayObtainStyledAttributes2.recycle();
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        if (preferenceViewHolder.itemView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) preferenceViewHolder.itemView.getLayoutParams();
            if (this.mWithExtraMarginBottom) {
                marginLayoutParams.bottomMargin = getContext().getResources().getDimensionPixelSize(R.dimen.support_preference_footer_preference_margin_bottom);
            } else {
                marginLayoutParams.bottomMargin = 0;
            }
            preferenceViewHolder.itemView.setLayoutParams(marginLayoutParams);
        }
        if (this.mIsEnableClickSpan) {
            COUIPreferenceUtils.setSummaryView(getContext(), preferenceViewHolder);
        }
    }

    public void setIsEnableClickSpan(boolean z2) {
        if (this.mIsEnableClickSpan != z2) {
            this.mIsEnableClickSpan = z2;
            notifyChanged();
        }
    }
}