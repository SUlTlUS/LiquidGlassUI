package com.coui.appcompat.preference;

import android.content.Context;
import android.util.AttributeSet;
import com.support.preference.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUISpannablePreference extends COUIPreference {
    public COUISpannablePreference(Context context) {
        this(context, null);
    }

    public COUISpannablePreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiSpannablePreferenceStyle);
    }

    public COUISpannablePreference(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3, R.style.Preference_COUI_COUISpannablePreference);
    }
}