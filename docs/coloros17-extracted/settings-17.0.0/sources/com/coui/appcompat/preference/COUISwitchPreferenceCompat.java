package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.SwitchPreferenceCompat;
import androidx.recyclerview.widget.COUIRecyclerView;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.cardlist.COUICardListSelectedItemLayout;
import com.coui.appcompat.couiswitch.COUISwitch;
import com.support.preference.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUISwitchPreferenceCompat extends SwitchPreferenceCompat implements COUIRecyclerView.ICOUIDividerDecorationInterface {
    private CharSequence mAssignment;
    private int mAssignmentColor;
    private int mDividerDefaultHorizontalPadding;
    private boolean mIsBackgroundAnimationEnabled;
    private boolean mIsEnableClickSpan;
    private View mItemView;
    private final Listener mListener;
    private int mSwitchUseMaterialEffect;
    private COUISwitch mSwitchView;

    public class Listener implements CompoundButton.OnCheckedChangeListener {
        private Listener() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
            if (COUISwitchPreferenceCompat.this.isChecked() == z2) {
                return;
            }
            if (!COUISwitchPreferenceCompat.access$100(COUISwitchPreferenceCompat.this, Boolean.valueOf(z2))) {
                compoundButton.setChecked(!z2);
            } else {
                COUISwitchPreferenceCompat.this.setChecked(z2);
                COUISwitchPreferenceClickHelper.dispatchOnPreferenceClickIfUserSwitchToggle(compoundButton, COUISwitchPreferenceCompat.this);
            }
        }

        public /* synthetic */ Listener(COUISwitchPreferenceCompat cOUISwitchPreferenceCompat, AnonymousClass1 anonymousClass1) {
            this();
        }
    }

    public COUISwitchPreferenceCompat(Context context) {
        this(context, null);
    }

    public static /* synthetic */ boolean access$100(COUISwitchPreferenceCompat cOUISwitchPreferenceCompat, Object obj) {
        return cOUISwitchPreferenceCompat.callCustomChangeListener(obj);
    }

    private boolean callCustomChangeListener(Object obj) {
        if (getOnPreferenceChangeListener() == null) {
            return true;
        }
        return getOnPreferenceChangeListener().onPreferenceChange(this, obj);
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.ICOUIDividerDecorationInterface
    public boolean drawDivider() {
        if (!(this.mItemView instanceof COUICardListSelectedItemLayout)) {
            return false;
        }
        int positionInGroup = COUICardListHelper.getPositionInGroup(this);
        return positionInGroup == 1 || positionInGroup == 2;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.ICOUIDividerDecorationInterface
    public View getDividerEndAlignView() {
        return null;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.ICOUIDividerDecorationInterface
    /* JADX INFO: renamed from: getDividerEndInset */
    public int getMDividerDefaultHorizontalPadding() {
        return this.mDividerDefaultHorizontalPadding;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.ICOUIDividerDecorationInterface
    public View getDividerStartAlignView() {
        return this.mItemView;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.ICOUIDividerDecorationInterface
    public int getDividerStartInset() {
        return this.mDividerDefaultHorizontalPadding;
    }

    @Override // androidx.preference.SwitchPreferenceCompat, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        View view = preferenceViewHolder.itemView;
        this.mItemView = view;
        if (view instanceof ListSelectedItemLayout) {
            ((ListSelectedItemLayout) view).setBackgroundAnimationEnabled(this.mIsBackgroundAnimationEnabled);
        }
        View viewFindViewById = preferenceViewHolder.findViewById(R.id.coui_preference);
        if (viewFindViewById != null) {
            viewFindViewById.setSoundEffectsEnabled(false);
        }
        View viewFindViewById2 = preferenceViewHolder.findViewById(R.id.switchWidget);
        boolean z2 = viewFindViewById2 instanceof COUISwitch;
        if (z2) {
            COUISwitch cOUISwitch = (COUISwitch) viewFindViewById2;
            cOUISwitch.setOnCheckedChangeListener(null);
            cOUISwitch.setVerticalScrollBarEnabled(false);
            this.mSwitchView = cOUISwitch;
            cOUISwitch.setUseMaterialEffect(this.mSwitchUseMaterialEffect);
        }
        super.onBindViewHolder(preferenceViewHolder);
        if (z2) {
            ((COUISwitch) viewFindViewById2).setOnCheckedChangeListener(this.mListener);
        }
        if (this.mIsEnableClickSpan) {
            COUIPreferenceUtils.setSummaryView(getContext(), preferenceViewHolder);
        }
        COUIPreferenceUtils.bindAssignmentView(preferenceViewHolder, this.mAssignment, this.mAssignmentColor);
        COUICardListHelper.setItemCardBackground(preferenceViewHolder.itemView, COUICardListHelper.getPositionInGroup(this));
    }

    @Override // androidx.preference.TwoStatePreference, androidx.preference.Preference
    public void onClick() {
        COUISwitch cOUISwitch = this.mSwitchView;
        if (cOUISwitch != null) {
            cOUISwitch.setShouldPlaySound(true);
        }
        super.onClick();
    }

    public void setAssignment(CharSequence charSequence) {
        if (TextUtils.equals(this.mAssignment, charSequence)) {
            return;
        }
        this.mAssignment = charSequence;
        notifyChanged();
    }

    public void setAssignmentColor(int i3) {
        if (this.mAssignmentColor != i3) {
            this.mAssignmentColor = i3;
            notifyChanged();
        }
    }

    public void setBackgroundAnimationEnabled(boolean z2) {
        if (this.mIsBackgroundAnimationEnabled != z2) {
            this.mIsBackgroundAnimationEnabled = z2;
            notifyChanged();
        }
    }

    public void setIsEnableClickSpan(boolean z2) {
        this.mIsEnableClickSpan = z2;
    }

    public void setSwitchUseMaterialEffect(int i3) {
        this.mSwitchUseMaterialEffect = i3;
        COUISwitch cOUISwitch = this.mSwitchView;
        if (cOUISwitch != null) {
            cOUISwitch.setUseMaterialEffect(i3);
        }
    }

    public COUISwitchPreferenceCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, androidx.preference.R.attr.switchPreferenceCompatStyle);
    }

    public COUISwitchPreferenceCompat(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0);
    }

    public COUISwitchPreferenceCompat(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3);
        this.mListener = new Listener(this, null);
        this.mSwitchUseMaterialEffect = -1;
        this.mIsBackgroundAnimationEnabled = true;
        this.mAssignmentColor = 0;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUIPreference, i3, 0);
        this.mIsEnableClickSpan = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIPreference_couiEnalbeClickSpan, false);
        this.mAssignment = typedArrayObtainStyledAttributes.getText(R.styleable.COUIPreference_couiAssignment);
        this.mAssignmentColor = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIPreference_couiAssignmentColor, 0);
        this.mIsBackgroundAnimationEnabled = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIPreference_isBackgroundAnimationEnabled, true);
        typedArrayObtainStyledAttributes.recycle();
        this.mDividerDefaultHorizontalPadding = getContext().getResources().getDimensionPixelSize(R.dimen.coui_preference_divider_default_horizontal_padding);
    }
}