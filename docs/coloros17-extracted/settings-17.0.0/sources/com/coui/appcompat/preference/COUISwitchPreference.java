package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.SwitchPreference;
import androidx.recyclerview.widget.COUIRecyclerView;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.cardlist.COUICardListSelectedItemLayout;
import com.coui.appcompat.couiswitch.COUISwitch;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.reddot.COUIHintRedDot;
import com.support.preference.R;

/* JADX INFO: loaded from: classes.dex */
public class COUISwitchPreference extends SwitchPreference implements COUICardSupportInterface, COUIRecyclerView.ICOUIDividerDecorationInterface {
    public static final int CIRCLE = 0;
    static final int DEFAULT_RADIUS = 14;
    public static final int ROUND = 1;
    private static final String TAG = "COUISwitchPreference";
    private CharSequence mAssignment;
    private int mAssignmentColor;
    private int mDividerDefaultHorizontalPadding;
    private boolean mHasBorder;
    private boolean mHasRedDot;
    private int mIconStyle;
    private boolean mIsBackgroundAnimationEnabled;
    private boolean mIsCustom;
    private boolean mIsEnableClickSpan;
    private boolean mIsSupportCardUse;
    private View mItemView;
    private final Listener mListener;
    private int mRadius;
    private int mRedDotDiameter;
    private int mRedDotMarginStart;
    private int mSwitchBarCheckedColor;
    private COUISwitchTouchEventDelegate mSwitchTouchEventDelegate;
    private int mSwitchUseMaterialEffect;
    private COUISwitch mSwitchView;
    private CharSequence mTitle;
    private TextView mTitleView;

    public class Listener implements CompoundButton.OnCheckedChangeListener {
        private Listener() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
            if (COUISwitchPreference.this.isChecked() == z2) {
                return;
            }
            if (!COUISwitchPreference.access$100(COUISwitchPreference.this, Boolean.valueOf(z2))) {
                compoundButton.setChecked(!z2);
            } else {
                COUISwitchPreference.this.setChecked(z2);
                COUISwitchPreferenceClickHelper.dispatchOnPreferenceClickIfUserSwitchToggle(compoundButton, COUISwitchPreference.this);
            }
        }

        public /* synthetic */ Listener(COUISwitchPreference cOUISwitchPreference, AnonymousClass1 anonymousClass1) {
            this();
        }
    }

    public COUISwitchPreference(Context context) {
        this(context, null);
    }

    public static /* synthetic */ boolean access$100(COUISwitchPreference cOUISwitchPreference, Object obj) {
        return cOUISwitchPreference.callCustomChangeListener(obj);
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

    public CharSequence getAssignment() {
        return this.mAssignment;
    }

    public int getBorderRectRadius(int i3) {
        return (i3 == 1 || i3 == 2 || i3 != 3) ? 14 : 16;
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

    @Override // com.coui.appcompat.preference.COUICardSupportInterface
    public boolean isSupportCardUse() {
        return this.mIsSupportCardUse;
    }

    @Override // androidx.preference.SwitchPreference, androidx.preference.Preference
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
        View viewFindViewById2 = preferenceViewHolder.findViewById(android.R.id.switch_widget);
        View viewFindViewById3 = preferenceViewHolder.findViewById(R.id.jump_icon_red_dot);
        if (viewFindViewById2 instanceof COUISwitch) {
            COUISwitch cOUISwitch = (COUISwitch) viewFindViewById2;
            cOUISwitch.setOnCheckedChangeListener(this.mListener);
            cOUISwitch.setVerticalScrollBarEnabled(false);
            this.mSwitchView = cOUISwitch;
            cOUISwitch.setUseMaterialEffect(this.mSwitchUseMaterialEffect);
            int i3 = this.mSwitchBarCheckedColor;
            if (i3 != -1) {
                cOUISwitch.setBarCheckedColor(i3);
            }
            if (this.mItemView != null) {
                if (this.mSwitchTouchEventDelegate == null) {
                    COUILog.d(TAG, "delegate create title=" + ((Object) getTitle()));
                    this.mSwitchTouchEventDelegate = new COUISwitchTouchEventDelegate(cOUISwitch, this.mItemView, null);
                }
                this.mSwitchTouchEventDelegate.attach(this.mItemView, cOUISwitch);
            }
        }
        super.onBindViewHolder(preferenceViewHolder);
        if (this.mIsEnableClickSpan) {
            COUIPreferenceUtils.setSummaryView(getContext(), preferenceViewHolder);
        }
        COUIPreferenceUtils.setIconStyle(preferenceViewHolder, getContext(), this.mRadius, this.mHasBorder, this.mIconStyle, this.mIsCustom);
        View viewFindViewById4 = preferenceViewHolder.findViewById(R.id.img_layout);
        View viewFindViewById5 = preferenceViewHolder.itemView.findViewById(android.R.id.icon);
        if (viewFindViewById4 != null) {
            if (viewFindViewById5 != null) {
                viewFindViewById4.setVisibility(viewFindViewById5.getVisibility());
            } else {
                viewFindViewById4.setVisibility(8);
            }
        }
        TextView textView = (TextView) preferenceViewHolder.findViewById(android.R.id.title);
        this.mTitleView = textView;
        textView.setText(this.mTitle);
        if (viewFindViewById3 != null) {
            if (this.mHasRedDot) {
                COUIHintRedDot cOUIHintRedDot = (COUIHintRedDot) viewFindViewById3;
                cOUIHintRedDot.setLaidOut();
                viewFindViewById3.setVisibility(0);
                cOUIHintRedDot.setPointMode(1);
            } else {
                viewFindViewById3.setVisibility(8);
            }
            viewFindViewById3.invalidate();
        }
        COUIPreferenceUtils.bindAssignmentView(preferenceViewHolder, this.mAssignment, this.mAssignmentColor);
        COUICardListHelper.setItemCardBackground(preferenceViewHolder.itemView, COUICardListHelper.getPositionInGroup(this));
    }

    @Override // androidx.preference.TwoStatePreference, androidx.preference.Preference
    public void onClick() {
        COUISwitch cOUISwitch = this.mSwitchView;
        if (cOUISwitch == null || !cOUISwitch.isDraggingOrAttach()) {
            setPlaySound(true);
            setPerformFeedBack(true);
            super.onClick();
        }
    }

    public void refresh() {
        COUISwitch cOUISwitch = this.mSwitchView;
        if (cOUISwitch != null) {
            cOUISwitch.refresh();
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

    public void setBorderRectRadius(int i3) {
        if (this.mRadius != i3) {
            this.mRadius = i3;
            notifyChanged();
        }
    }

    public void setHasRedDot(boolean z2) {
        if (this.mHasRedDot != z2) {
            this.mHasRedDot = z2;
            notifyChanged();
        }
    }

    public void setIsCustomIconRadius(boolean z2) {
        this.mIsCustom = z2;
    }

    public void setIsEnableClickSpan(boolean z2) {
        this.mIsEnableClickSpan = z2;
    }

    @Override // com.coui.appcompat.preference.COUICardSupportInterface
    public void setIsSupportCardUse(boolean z2) {
        this.mIsSupportCardUse = z2;
    }

    public void setPerformFeedBack(boolean z2) {
        COUISwitch cOUISwitch = this.mSwitchView;
        if (cOUISwitch != null) {
            cOUISwitch.setTactileFeedbackEnabled(z2);
        }
    }

    public void setPlaySound(boolean z2) {
        COUISwitch cOUISwitch = this.mSwitchView;
        if (cOUISwitch != null) {
            cOUISwitch.setShouldPlaySound(z2);
        }
    }

    public final void setSwitchBarCheckedColor(int i3) {
        this.mSwitchBarCheckedColor = i3;
    }

    public void setSwitchUseMaterialEffect(int i3) {
        this.mSwitchUseMaterialEffect = i3;
        COUISwitch cOUISwitch = this.mSwitchView;
        if (cOUISwitch != null) {
            cOUISwitch.setUseMaterialEffect(i3);
        }
    }

    @Override // androidx.preference.Preference
    public void setTitle(CharSequence charSequence) {
        super.setTitle(charSequence);
        this.mTitle = getTitle();
    }

    public COUISwitchPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, androidx.preference.R.attr.switchPreferenceStyle);
    }

    public COUISwitchPreference(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0);
    }

    public COUISwitchPreference(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mListener = new Listener(this, null);
        this.mSwitchUseMaterialEffect = -1;
        this.mAssignmentColor = 0;
        this.mIsCustom = false;
        this.mSwitchBarCheckedColor = -1;
        this.mIsBackgroundAnimationEnabled = true;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUIPreference, i3, i4);
        this.mIsEnableClickSpan = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIPreference_couiEnalbeClickSpan, false);
        this.mAssignment = typedArrayObtainStyledAttributes.getText(R.styleable.COUIPreference_couiAssignment);
        this.mAssignmentColor = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIPreference_couiAssignmentColor, 0);
        this.mIsSupportCardUse = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIPreference_isSupportCardUse, true);
        this.mIconStyle = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIPreference_couiIconStyle, 1);
        this.mHasBorder = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIPreference_hasBorder, false);
        this.mRadius = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIPreference_preference_icon_radius, 14);
        this.mIsBackgroundAnimationEnabled = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIPreference_isBackgroundAnimationEnabled, true);
        typedArrayObtainStyledAttributes.recycle();
        TypedArray typedArrayObtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R.styleable.COUISwitchPreference, i3, i4);
        this.mHasRedDot = typedArrayObtainStyledAttributes2.getBoolean(R.styleable.COUISwitchPreference_hasTitleRedDot, false);
        typedArrayObtainStyledAttributes2.recycle();
        this.mDividerDefaultHorizontalPadding = getContext().getResources().getDimensionPixelSize(R.dimen.coui_preference_divider_default_horizontal_padding);
        this.mTitle = getTitle();
        this.mRedDotDiameter = context.getResources().getDimensionPixelOffset(com.support.reddot.R.dimen.coui_dot_diameter_small);
        this.mRedDotMarginStart = context.getResources().getDimensionPixelOffset(com.support.reddot.R.dimen.coui_switch_preference_dot_margin_start);
    }
}