package com.coui.appcompat.preference;

import android.content.Context;
import android.util.AttributeSet;
import com.support.preference.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIActivityDialogPreference extends COUIListPreference {
    public COUIActivityDialogPreference(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
    }

    public COUIActivityDialogPreference(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0);
    }

    public COUIActivityDialogPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiActivityDialogPreferenceStyle);
    }

    public COUIActivityDialogPreference(Context context) {
        this(context, null);
    }
}