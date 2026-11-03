package com.coui.appcompat.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import androidx.preference.PreferenceViewHolder;
import com.coui.appcompat.cardlist.COUICardListSelectedItemLayout;
import com.coui.appcompat.poplist.PreciseClickHelper;
import com.support.preference.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIMarkWithDividerPreference extends COUIMarkPreference {
    private OnMainLayoutClickListener mClickListener;
    private LinearLayout mMainLayout;
    private PreciseClickHelper mPreciseHelper;
    private PreciseClickHelper.OnPreciseClickListener mPreciseListener;
    private LinearLayout mRadioLayout;

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIMarkWithDividerPreference$1, reason: invalid class name */
    public class AnonymousClass1 implements View.OnClickListener {
        public AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (COUIMarkWithDividerPreference.access$000(COUIMarkWithDividerPreference.this) != null) {
                COUIMarkWithDividerPreference.access$000(COUIMarkWithDividerPreference.this).onMainLayoutClick();
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIMarkWithDividerPreference$2, reason: invalid class name */
    public class AnonymousClass2 implements View.OnClickListener {
        public AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            COUIMarkWithDividerPreference.access$101(COUIMarkWithDividerPreference.this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIMarkWithDividerPreference$3, reason: invalid class name */
    public class AnonymousClass3 implements PreciseClickHelper.OnPreciseClickListener {
        public AnonymousClass3() {
        }

        @Override // com.coui.appcompat.poplist.PreciseClickHelper.OnPreciseClickListener
        public void onClick(View view, int i3, int i4) {
            COUIMarkWithDividerPreference.access$200(COUIMarkWithDividerPreference.this).onClick(view, i3, i4);
            if (COUIMarkWithDividerPreference.access$300(COUIMarkWithDividerPreference.this) == null || COUIMarkWithDividerPreference.access$000(COUIMarkWithDividerPreference.this) == null) {
                return;
            }
            COUIMarkWithDividerPreference.access$000(COUIMarkWithDividerPreference.this).onMainLayoutClick();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIMarkWithDividerPreference$4, reason: invalid class name */
    public class AnonymousClass4 implements View.OnClickListener {
        public AnonymousClass4() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (COUIMarkWithDividerPreference.access$000(COUIMarkWithDividerPreference.this) != null) {
                COUIMarkWithDividerPreference.access$000(COUIMarkWithDividerPreference.this).onMainLayoutClick();
            }
        }
    }

    public interface OnMainLayoutClickListener {
        void onMainLayoutClick();
    }

    public COUIMarkWithDividerPreference(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
    }

    public static /* synthetic */ OnMainLayoutClickListener access$000(COUIMarkWithDividerPreference cOUIMarkWithDividerPreference) {
        return cOUIMarkWithDividerPreference.mClickListener;
    }

    public static /* synthetic */ void access$101(COUIMarkWithDividerPreference cOUIMarkWithDividerPreference) {
        super.onClick();
    }

    public static /* synthetic */ PreciseClickHelper.OnPreciseClickListener access$200(COUIMarkWithDividerPreference cOUIMarkWithDividerPreference) {
        return cOUIMarkWithDividerPreference.mPreciseListener;
    }

    public static /* synthetic */ LinearLayout access$300(COUIMarkWithDividerPreference cOUIMarkWithDividerPreference) {
        return cOUIMarkWithDividerPreference.mMainLayout;
    }

    private void initPreciseHelper() {
        if (this.mMainLayout == null || this.mPreciseListener == null) {
            return;
        }
        removePreciseClickListener();
        PreciseClickHelper preciseClickHelper = new PreciseClickHelper(this.mMainLayout, new AnonymousClass3());
        this.mPreciseHelper = preciseClickHelper;
        preciseClickHelper.setup();
    }

    public OnMainLayoutClickListener getOnMainLayoutClickListener() {
        return this.mClickListener;
    }

    @Override // com.coui.appcompat.preference.COUIMarkPreference, androidx.preference.CheckBoxPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        LinearLayout linearLayout = (LinearLayout) preferenceViewHolder.itemView.findViewById(R.id.main_layout);
        this.mMainLayout = linearLayout;
        View view = preferenceViewHolder.itemView;
        if ((view instanceof COUICardListSelectedItemLayout) && linearLayout != null) {
            ((COUICardListSelectedItemLayout) view).setMainLayoutToSetExtraPadding(linearLayout);
        }
        super.onBindViewHolder(preferenceViewHolder);
        initPreciseHelper();
        LinearLayout linearLayout2 = this.mMainLayout;
        if (linearLayout2 != null) {
            if (this.mPreciseListener == null) {
                linearLayout2.setOnClickListener(new AnonymousClass1());
            }
            this.mMainLayout.setClickable(isSelectable());
        }
        LinearLayout linearLayout3 = (LinearLayout) preferenceViewHolder.itemView.findViewById(R.id.radio_layout);
        this.mRadioLayout = linearLayout3;
        if (linearLayout3 != null) {
            linearLayout3.setOnClickListener(new AnonymousClass2());
            this.mRadioLayout.setClickable(isSelectable());
        }
    }

    public void removePreciseClickListener() {
        PreciseClickHelper preciseClickHelper = this.mPreciseHelper;
        if (preciseClickHelper != null) {
            preciseClickHelper.unSet();
            this.mPreciseHelper = null;
        }
        LinearLayout linearLayout = this.mMainLayout;
        if (linearLayout != null) {
            linearLayout.setOnClickListener(new AnonymousClass4());
        }
    }

    public void setOnMainLayoutListener(OnMainLayoutClickListener onMainLayoutClickListener) {
        this.mClickListener = onMainLayoutClickListener;
    }

    public void setOnPreciseClickListener(PreciseClickHelper.OnPreciseClickListener onPreciseClickListener) {
        this.mPreciseListener = onPreciseClickListener;
        initPreciseHelper();
    }

    public COUIMarkWithDividerPreference(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, R.style.Preference_COUI_COUIRadioWithDividerPreference);
    }

    public COUIMarkWithDividerPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiRadioWithDividerPreferenceStyle);
    }

    public COUIMarkWithDividerPreference(Context context) {
        this(context, null);
    }
}