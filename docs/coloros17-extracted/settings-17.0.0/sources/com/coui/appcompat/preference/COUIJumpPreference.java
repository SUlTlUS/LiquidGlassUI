package com.coui.appcompat.preference;

import android.content.Context;
import android.util.AttributeSet;
import com.support.preference.R;

/* JADX INFO: loaded from: classes.dex */
public class COUIJumpPreference extends COUIPreference {
    public COUIJumpPreference(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
    }

    public COUIJumpPreference(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, R.style.Preference_COUI_COUIJumpPreference);
    }

    public COUIJumpPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiJumpPreferenceStyle);
    }

    public COUIJumpPreference(Context context) {
        this(context, null);
    }
}