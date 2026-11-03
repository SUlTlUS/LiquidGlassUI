package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Checkable;
import android.widget.TextView;
import androidx.preference.CheckBoxPreference;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.COUIRecyclerView;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.cardlist.COUICardListSelectedItemLayout;
import com.support.appcompat.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIMarkPreference extends CheckBoxPreference implements COUICardSupportInterface, COUIRecyclerView.ICOUIDividerDecorationInterface {
    public static final int CIRCLE = 0;
    static final int DEFAULT_RADIUS = 14;
    public static final int HEAD_MARK = 1;
    public static final int ROUND = 1;
    public static final int TAIL_MARK = 0;
    private CharSequence mAssignment;
    private int mAssignmentColor;
    private int mDividerDefaultHorizontalPadding;
    private boolean mHasBorder;
    private int mIconStyle;
    private boolean mIsBackgroundAnimationEnabled;
    private boolean mIsCustom;
    private boolean mIsEnableClickSpan;
    private boolean mIsSupportCardUse;
    private View mItemView;
    int mMarkStyle;
    private int mRadius;
    private boolean mShowDivider;
    private TextView mTitleView;

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIMarkPreference$1, reason: invalid class name */
    public class AnonymousClass1 extends View.AccessibilityDelegate {
        final /* synthetic */ View val$checkableView;

        public AnonymousClass1(View view) {
            this.val$checkableView = view;
        }

        @Override // android.view.View.AccessibilityDelegate
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, COUIMarkPreference.this.getContext().getResources().getString(R.string.coui_accessibility_select)));
            KeyEvent.Callback callback = this.val$checkableView;
            if ((callback instanceof Checkable) && ((Checkable) callback).isChecked()) {
                accessibilityNodeInfo.setClickable(false);
                accessibilityNodeInfo.removeAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK);
            }
        }
    }

    public COUIMarkPreference(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mMarkStyle = 0;
        this.mShowDivider = true;
        this.mAssignmentColor = 0;
        this.mIsCustom = false;
        this.mIsBackgroundAnimationEnabled = true;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, com.support.preference.R.styleable.COUIMarkPreference, i3, i4);
        this.mMarkStyle = typedArrayObtainStyledAttributes.getInt(com.support.preference.R.styleable.COUIMarkPreference_couiMarkStyle, 0);
        this.mAssignment = typedArrayObtainStyledAttributes.getText(com.support.preference.R.styleable.COUIMarkPreference_couiMarkAssignment);
        typedArrayObtainStyledAttributes.recycle();
        TypedArray typedArrayObtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, com.support.preference.R.styleable.COUIPreference, i3, i4);
        this.mShowDivider = typedArrayObtainStyledAttributes2.getBoolean(com.support.preference.R.styleable.COUIPreference_couiShowDivider, this.mShowDivider);
        this.mIsEnableClickSpan = typedArrayObtainStyledAttributes2.getBoolean(com.support.preference.R.styleable.COUIPreference_couiEnalbeClickSpan, false);
        this.mIsSupportCardUse = typedArrayObtainStyledAttributes2.getBoolean(com.support.preference.R.styleable.COUIPreference_isSupportCardUse, true);
        this.mIconStyle = typedArrayObtainStyledAttributes2.getInt(com.support.preference.R.styleable.COUIPreference_couiIconStyle, 1);
        this.mHasBorder = typedArrayObtainStyledAttributes2.getBoolean(com.support.preference.R.styleable.COUIPreference_hasBorder, false);
        this.mRadius = typedArrayObtainStyledAttributes2.getDimensionPixelSize(com.support.preference.R.styleable.COUIPreference_preference_icon_radius, 14);
        this.mAssignmentColor = typedArrayObtainStyledAttributes2.getInt(com.support.preference.R.styleable.COUIPreference_couiAssignmentColor, 0);
        this.mIsBackgroundAnimationEnabled = typedArrayObtainStyledAttributes2.getBoolean(com.support.preference.R.styleable.COUIPreference_isBackgroundAnimationEnabled, true);
        typedArrayObtainStyledAttributes2.recycle();
        this.mDividerDefaultHorizontalPadding = getContext().getResources().getDimensionPixelSize(com.support.preference.R.dimen.coui_preference_divider_default_horizontal_padding);
        setChecked(true);
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

    public int getMarkStyle() {
        return this.mMarkStyle;
    }

    @Override // com.coui.appcompat.preference.COUICardSupportInterface
    public boolean isSupportCardUse() {
        return this.mIsSupportCardUse;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.preference.CheckBoxPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View view = preferenceViewHolder.itemView;
        this.mItemView = view;
        if (view instanceof ListSelectedItemLayout) {
            ((ListSelectedItemLayout) view).setBackgroundAnimationEnabled(this.mIsBackgroundAnimationEnabled);
        }
        View viewFindViewById = preferenceViewHolder.findViewById(com.support.preference.R.id.coui_tail_mark);
        this.mItemView.setAccessibilityDelegate(new AnonymousClass1(viewFindViewById));
        if (viewFindViewById != 0 && (viewFindViewById instanceof Checkable)) {
            if (this.mMarkStyle == 0) {
                viewFindViewById.setVisibility(0);
                ((Checkable) viewFindViewById).setChecked(isChecked());
            } else {
                viewFindViewById.setVisibility(8);
            }
        }
        View viewFindViewById2 = preferenceViewHolder.findViewById(com.support.preference.R.id.coui_head_mark);
        if (viewFindViewById2 != 0 && (viewFindViewById2 instanceof Checkable)) {
            if (this.mMarkStyle == 1) {
                viewFindViewById2.setVisibility(0);
                ((Checkable) viewFindViewById2).setChecked(isChecked());
            } else {
                viewFindViewById2.setVisibility(8);
            }
        }
        COUIPreferenceUtils.setIconStyle(preferenceViewHolder, getContext(), this.mRadius, this.mHasBorder, this.mIconStyle, this.mIsCustom);
        this.mTitleView = (TextView) preferenceViewHolder.findViewById(android.R.id.title);
        View viewFindViewById3 = preferenceViewHolder.findViewById(com.support.preference.R.id.img_layout);
        View viewFindViewById4 = preferenceViewHolder.findViewById(android.R.id.icon);
        if (viewFindViewById3 != null) {
            if (viewFindViewById4 != null) {
                viewFindViewById3.setVisibility(viewFindViewById4.getVisibility());
            } else {
                viewFindViewById3.setVisibility(8);
            }
        }
        if (this.mIsEnableClickSpan) {
            COUIPreferenceUtils.setSummaryView(getContext(), preferenceViewHolder);
        }
        COUIPreferenceUtils.bindAssignmentView(preferenceViewHolder, this.mAssignment, this.mAssignmentColor);
        COUICardListHelper.setItemCardBackground(preferenceViewHolder.itemView, COUICardListHelper.getPositionInGroup(this));
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

    public void setMarkStyle(int i3) {
        this.mMarkStyle = i3;
    }

    public COUIMarkPreference(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, com.support.preference.R.style.Preference_COUI_COUIMarkPreference);
    }

    public COUIMarkPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, com.support.preference.R.attr.couiMarkPreferenceStyle);
    }

    public COUIMarkPreference(Context context) {
        this(context, null);
    }
}