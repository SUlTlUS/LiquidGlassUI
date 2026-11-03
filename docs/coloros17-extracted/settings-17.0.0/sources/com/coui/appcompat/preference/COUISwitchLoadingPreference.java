package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.SwitchPreferenceCompat;
import androidx.recyclerview.widget.COUIRecyclerView;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.cardlist.COUICardListSelectedItemLayout;
import com.coui.appcompat.couiswitch.COUISwitch;
import com.support.preference.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUISwitchLoadingPreference extends SwitchPreferenceCompat implements COUICardSupportInterface, COUIRecyclerView.ICOUIDividerDecorationInterface {
    private CharSequence mAssignment;
    private int mAssignmentColor;
    View mCheckableView;
    private int mDividerDefaultHorizontalPadding;
    private boolean mIsBackgroundAnimationEnabled;
    private boolean mIsEnableClickSpan;
    private boolean mIsSupportCardUse;
    private View mItemView;
    private final Listener mListener;
    private COUISwitch.OnLoadingStateChangedListener mOnLoadingStateChangedListener;
    private COUISwitch mSwitchView;
    private TextView mTitleView;

    public class Listener implements CompoundButton.OnCheckedChangeListener {
        private Listener() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
            if (COUISwitchLoadingPreference.access$100(COUISwitchLoadingPreference.this, Boolean.valueOf(z2))) {
                COUISwitchLoadingPreference.this.setChecked(z2);
            } else {
                compoundButton.setChecked(!z2);
            }
        }

        public /* synthetic */ Listener(COUISwitchLoadingPreference cOUISwitchLoadingPreference, AnonymousClass1 anonymousClass1) {
            this();
        }
    }

    public COUISwitchLoadingPreference(Context context) {
        this(context, null);
    }

    public static /* synthetic */ boolean access$100(COUISwitchLoadingPreference cOUISwitchLoadingPreference, Object obj) {
        return cOUISwitchLoadingPreference.callCustomChangeListener(obj);
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
        return this.mTitleView;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.ICOUIDividerDecorationInterface
    public int getDividerStartInset() {
        return this.mDividerDefaultHorizontalPadding;
    }

    public View getSwitch() {
        return this.mCheckableView;
    }

    @Override // com.coui.appcompat.preference.COUICardSupportInterface
    public boolean isSupportCardUse() {
        return this.mIsSupportCardUse;
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
            viewFindViewById.setHapticFeedbackEnabled(false);
        }
        View viewFindViewById2 = preferenceViewHolder.findViewById(R.id.switchWidget);
        this.mCheckableView = viewFindViewById2;
        if (viewFindViewById2 instanceof COUISwitch) {
            COUISwitch cOUISwitch = (COUISwitch) viewFindViewById2;
            cOUISwitch.setOnCheckedChangeListener(null);
            cOUISwitch.setVerticalScrollBarEnabled(false);
            this.mSwitchView = cOUISwitch;
        }
        super.onBindViewHolder(preferenceViewHolder);
        View view2 = this.mCheckableView;
        if (view2 instanceof COUISwitch) {
            COUISwitch cOUISwitch2 = (COUISwitch) view2;
            cOUISwitch2.setLoadingStyle(true);
            cOUISwitch2.setOnLoadingStateChangedListener(this.mOnLoadingStateChangedListener);
            cOUISwitch2.setOnCheckedChangeListener(this.mListener);
        }
        if (this.mIsEnableClickSpan) {
            COUIPreferenceUtils.setSummaryView(getContext(), preferenceViewHolder);
        }
        this.mTitleView = (TextView) preferenceViewHolder.findViewById(android.R.id.title);
        View viewFindViewById3 = preferenceViewHolder.itemView.findViewById(android.R.id.icon);
        View viewFindViewById4 = preferenceViewHolder.findViewById(R.id.img_layout);
        if (viewFindViewById4 != null) {
            if (viewFindViewById3 != null) {
                viewFindViewById4.setVisibility(viewFindViewById3.getVisibility());
            } else {
                viewFindViewById4.setVisibility(8);
            }
        }
        COUIPreferenceUtils.bindAssignmentView(preferenceViewHolder, this.mAssignment, this.mAssignmentColor);
        COUICardListHelper.setItemCardBackground(preferenceViewHolder.itemView, COUICardListHelper.getPositionInGroup(this));
    }

    @Override // androidx.preference.TwoStatePreference, androidx.preference.Preference
    public void onClick() {
        COUISwitch cOUISwitch = this.mSwitchView;
        if (cOUISwitch != null) {
            cOUISwitch.setShouldPlaySound(true);
            this.mSwitchView.setTactileFeedbackEnabled(true);
            this.mSwitchView.startLoading();
        }
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

    @Override // com.coui.appcompat.preference.COUICardSupportInterface
    public void setIsSupportCardUse(boolean z2) {
        this.mIsSupportCardUse = z2;
    }

    public void setOnLoadingStateChangedListener(COUISwitch.OnLoadingStateChangedListener onLoadingStateChangedListener) {
        this.mOnLoadingStateChangedListener = onLoadingStateChangedListener;
        View view = this.mCheckableView;
        if (view instanceof COUISwitch) {
            ((COUISwitch) view).setOnLoadingStateChangedListener(onLoadingStateChangedListener);
        }
    }

    public void startLoading() {
        View view = this.mCheckableView;
        if (view == null || !(view instanceof COUISwitch)) {
            return;
        }
        ((COUISwitch) view).startLoading();
    }

    public void stopLoading() {
        View view = this.mCheckableView;
        if (view == null || !(view instanceof COUISwitch)) {
            return;
        }
        ((COUISwitch) view).stopLoading();
    }

    public COUISwitchLoadingPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiSwitchLoadPreferenceStyle);
    }

    public COUISwitchLoadingPreference(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, R.style.Preference_COUI_SwitchPreference_Loading);
    }

    public COUISwitchLoadingPreference(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mListener = new Listener(this, null);
        this.mAssignmentColor = 0;
        this.mIsBackgroundAnimationEnabled = true;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUIPreference, i3, 0);
        this.mIsEnableClickSpan = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIPreference_couiEnalbeClickSpan, false);
        this.mIsSupportCardUse = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIPreference_isSupportCardUse, true);
        this.mAssignment = typedArrayObtainStyledAttributes.getText(R.styleable.COUIPreference_couiAssignment);
        this.mAssignmentColor = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIPreference_couiAssignmentColor, 0);
        this.mIsBackgroundAnimationEnabled = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIPreference_isBackgroundAnimationEnabled, true);
        typedArrayObtainStyledAttributes.recycle();
        this.mDividerDefaultHorizontalPadding = getContext().getResources().getDimensionPixelSize(R.dimen.coui_preference_divider_default_horizontal_padding);
    }
}