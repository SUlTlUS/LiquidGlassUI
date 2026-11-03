package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import com.support.preference.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUISlideSelectPreference extends COUIPreference {
    public static final int FORCE_CLICK = 1;
    public static final int FORCE_UNCLICK = 2;
    public static final int NORMAL = 0;
    private int mClickStyle;
    Context mContext;
    CharSequence mSelectionText;
    private TextView mStatus1;

    public COUISlideSelectPreference(Context context) {
        this(context, null);
    }

    public CharSequence getSelectionText() {
        CharSequence charSequence = this.mSelectionText;
        return charSequence != null ? charSequence : "";
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View viewFindViewById = preferenceViewHolder.findViewById(R.id.coui_preference);
        if (viewFindViewById != null) {
            viewFindViewById.setTag(new Object());
            int i3 = this.mClickStyle;
            if (i3 == 1) {
                viewFindViewById.setClickable(false);
            } else if (i3 == 2) {
                viewFindViewById.setClickable(true);
            }
        }
        View viewFindViewById2 = preferenceViewHolder.findViewById(R.id.coui_statusText_select);
        if (viewFindViewById2 == null || !(viewFindViewById2 instanceof TextView)) {
            return;
        }
        this.mStatus1 = (TextView) viewFindViewById2;
        CharSequence charSequence = this.mSelectionText;
        if (TextUtils.isEmpty(charSequence)) {
            this.mStatus1.setVisibility(8);
        } else {
            this.mStatus1.setText(charSequence);
            this.mStatus1.setVisibility(0);
        }
    }

    public void setBlurView(View view) {
    }

    public void setSelectionText(CharSequence charSequence) {
        if ((charSequence != null || this.mSelectionText == null) && (charSequence == null || charSequence.equals(this.mSelectionText))) {
            return;
        }
        this.mSelectionText = charSequence;
        notifyChanged();
    }

    public void setStatusText(CharSequence charSequence) {
        if ((charSequence != null || this.mSelectionText == null) && (charSequence == null || charSequence.equals(this.mSelectionText))) {
            return;
        }
        this.mSelectionText = charSequence;
        notifyChanged();
    }

    public COUISlideSelectPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiSlideSelectPreferenceStyle);
    }

    public COUISlideSelectPreference(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, R.style.Preference_COUI_COUISelectPreference);
    }

    public COUISlideSelectPreference(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3);
        this.mClickStyle = 0;
        this.mContext = context;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUISlideSelectPreference, i3, i4);
        this.mSelectionText = typedArrayObtainStyledAttributes.getText(R.styleable.COUISlideSelectPreference_coui_select_status1);
        typedArrayObtainStyledAttributes.recycle();
    }
}