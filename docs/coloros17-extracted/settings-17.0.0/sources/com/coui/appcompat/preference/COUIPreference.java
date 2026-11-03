package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.COUIRecyclerView;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.cardlist.COUICardListSelectedItemLayout;
import com.coui.appcompat.imageview.COUIRoundImageView;
import com.coui.appcompat.poplist.PreciseClickHelper;
import com.coui.appcompat.reddot.COUIHintRedDot;
import com.support.preference.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes.dex */
public class COUIPreference extends Preference implements COUICardSupportInterface, COUIRecyclerView.ICOUIDividerDecorationInterface {
    public static final int CIRCLE = 0;
    static final int DEFAULT_RADIUS = 14;
    static final int DEFAULT_SCALE = 3;
    public static final int FORCE_CLICK = 1;
    public static final int FORCE_UNCLICK = 2;
    static final int MAX_RADIUS = 36;
    static final int MIN_RADIUS = 14;
    public static final int NORMAL = 0;
    static final int NO_ICON_HEIGHT = 0;
    public static final int ROUND = 1;
    public static final int SUMMARY_LINE_DEFAULT = 0;
    public static final int SUMMARY_LINE_ONE = 1;
    public static final int SUMMARY_LINE_TWO = 2;
    private static final String TAG = "COUIPreference-";
    static final int ratio = 6;
    private COUIHintRedDot assignRedDot;
    private COUIRoundImageView assignmentIcon;
    private COUIHintRedDot endRedDot;
    private View iconRedDot;
    private Drawable mAssignIconRes;
    private int mAssignRedDotMode;
    private CharSequence mAssignment;
    private int mAssignmentColor;
    private int mClickStyle;
    private Context mContext;
    private boolean mCouiSetDefaultColor;
    private int mCouiSummaryLineLimit;
    private int mDividerDefaultHorizontalPadding;
    private int mEndRedDotMode;
    private int mEndRedDotNum;
    private boolean mHasBorder;
    private int mIconRedDotMode;
    private int mIconStyle;
    private boolean mIsBackgroundAnimationEnabled;
    private boolean mIsCustom;
    private boolean mIsEnableClickSpan;
    private boolean mIsSelected;
    private boolean mIsSupportCardUse;
    private View mItemView;
    Drawable mJumpRes;
    private PreciseClickHelper mPreciseHelper;
    protected PreciseClickHelper.OnPreciseClickListener mPreciseListener;
    private int mRadius;
    private boolean mShowDivider;
    CharSequence mStatusText1;
    private ColorStateList mSummaryTextColor;
    private TextView mSummaryView;
    private ColorStateList mTitleTextColor;
    private TextView mTitleView;

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIPreference$1, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass1 implements PreciseClickHelper.OnPreciseClickListener {
        public AnonymousClass1() {
        }

        @Override // com.coui.appcompat.poplist.PreciseClickHelper.OnPreciseClickListener
        public void onClick(View view, int i3, int i4) {
            COUIPreference.this.mPreciseListener.onClick(view, i3, i4);
        }
    }

    /* JADX INFO: loaded from: classes7.dex */
    @Retention(RetentionPolicy.SOURCE)
    public @interface SummaryLineType {
    }

    public COUIPreference(Context context) {
        this(context, null);
    }

    private void initPreciseHelper() {
        if (this.mItemView == null || this.mPreciseListener == null) {
            return;
        }
        removePreciseClickListener();
        PreciseClickHelper preciseClickHelper = new PreciseClickHelper(this.mItemView, new AnonymousClass1());
        this.mPreciseHelper = preciseClickHelper;
        preciseClickHelper.setup();
    }

    public void changeEndRedDotNumberWithAnim(int i3) {
        if (this.endRedDot != null) {
            this.mEndRedDotNum = i3;
            this.endRedDot.changePointNumber(i3);
            if (i3 > 0) {
                this.endRedDot.setPointNumber(i3);
            }
        }
    }

    public void dismissAssignRedDot() {
        if (this.assignRedDot == null || this.assignRedDot.getVisibility() != 0) {
            return;
        }
        this.assignRedDot.executeScaleAnim(false);
        notifyChanged();
    }

    public void dismissEndRedDot() {
        if (this.endRedDot == null || this.endRedDot.getVisibility() != 0) {
            return;
        }
        this.endRedDot.executeScaleAnim(false);
        notifyChanged();
    }

    public void dismissIconRedDot() {
        View view = this.iconRedDot;
        if ((view instanceof COUIHintRedDot) && view.getVisibility() == 0) {
            ((COUIHintRedDot) this.iconRedDot).executeScaleAnim(false);
            notifyChanged();
        }
    }

    public boolean drawDivider() {
        if (!this.mShowDivider || !(this.mItemView instanceof COUICardListSelectedItemLayout)) {
            return false;
        }
        int positionInGroup = COUICardListHelper.getPositionInGroup(this);
        return positionInGroup == 1 || positionInGroup == 2;
    }

    public int getAssignRedDotMode() {
        return this.mAssignRedDotMode;
    }

    public CharSequence getAssignment() {
        return this.mAssignment;
    }

    public int getBorderRectRadius(int i3) {
        return (i3 == 1 || i3 == 2 || i3 != 3) ? 14 : 16;
    }

    public int getClickStyle() {
        return this.mClickStyle;
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

    public int getEndRedDotMode() {
        return this.mEndRedDotMode;
    }

    public int getEndRedDotNum() {
        return this.mEndRedDotNum;
    }

    public int getIconRedDotMode() {
        return this.mIconRedDotMode;
    }

    public int getIconStyle() {
        return this.mIconStyle;
    }

    public boolean getIsSelected() {
        return this.mIsSelected;
    }

    public CharSequence getStatusText1() {
        return this.mStatusText1;
    }

    public boolean isShowDivider() {
        return this.mShowDivider;
    }

    @Override // com.coui.appcompat.preference.COUICardSupportInterface
    public boolean isSupportCardUse() {
        return this.mIsSupportCardUse;
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        boolean z2;
        super.onBindViewHolder(preferenceViewHolder);
        COUICardListHelper.setItemCardBackground(preferenceViewHolder.itemView, COUICardListHelper.getPositionInGroup(this));
        View view = preferenceViewHolder.itemView;
        if (view instanceof COUICardListSelectedItemLayout) {
            ((COUICardListSelectedItemLayout) view).consumeDispatchingEventForState(false);
        }
        View viewFindViewById = preferenceViewHolder.findViewById(R.id.coui_preference);
        boolean z3 = true;
        if (viewFindViewById != null) {
            int i3 = this.mClickStyle;
            if (i3 == 1) {
                viewFindViewById.setClickable(false);
            } else if (i3 == 2) {
                viewFindViewById.setClickable(true);
            }
        }
        this.mItemView = preferenceViewHolder.itemView;
        initPreciseHelper();
        View view2 = this.mItemView;
        if (view2 != null) {
            if (view2 instanceof ListSelectedItemLayout) {
                ((ListSelectedItemLayout) view2).setBackgroundAnimationEnabled(this.mIsBackgroundAnimationEnabled);
            }
            View view3 = this.mItemView;
            if (view3 instanceof COUICardListSelectedItemLayout) {
                ((COUICardListSelectedItemLayout) view3).setIsSelected(this.mIsSelected);
            }
        }
        if (this.mAssignmentColor == 0) {
            COUIPreferenceUtils.bindView(preferenceViewHolder, this.mJumpRes, this.mStatusText1, getAssignment());
        } else {
            COUIPreferenceUtils.bindView(preferenceViewHolder, this.mJumpRes, this.mStatusText1, getAssignment(), this.mAssignmentColor);
        }
        COUIPreferenceUtils.setTitleViewColor(getContext(), preferenceViewHolder, this.mTitleTextColor);
        COUIPreferenceUtils.setIconStyle(preferenceViewHolder, getContext(), this.mRadius, this.mHasBorder, this.mIconStyle, this.mIsCustom);
        COUIPreferenceUtils.setSummaryViewColor(preferenceViewHolder, this.mSummaryTextColor);
        if (this.mIsEnableClickSpan) {
            COUIPreferenceUtils.setSummaryView(getContext(), preferenceViewHolder);
        }
        this.mTitleView = (TextView) preferenceViewHolder.findViewById(android.R.id.title);
        TextView textView = (TextView) preferenceViewHolder.findViewById(android.R.id.summary);
        this.mSummaryView = textView;
        if (textView != null) {
            int i4 = this.mCouiSummaryLineLimit;
            if (i4 == 0) {
                textView.setMaxLines(Integer.MAX_VALUE);
                this.mSummaryView.setEllipsize(null);
            } else {
                textView.setMaxLines(i4);
                this.mSummaryView.setEllipsize(TextUtils.TruncateAt.END);
            }
        }
        this.iconRedDot = preferenceViewHolder.findViewById(R.id.img_red_dot);
        this.endRedDot = (COUIHintRedDot) preferenceViewHolder.findViewById(R.id.jump_icon_red_dot);
        this.assignRedDot = (COUIHintRedDot) preferenceViewHolder.findViewById(R.id.assignment_red_dot);
        this.assignmentIcon = (COUIRoundImageView) preferenceViewHolder.findViewById(R.id.assignment_icon);
        View view4 = this.iconRedDot;
        if (view4 instanceof COUIHintRedDot) {
            if (this.mIconRedDotMode != 0) {
                ((COUIHintRedDot) view4).setLaidOut();
                this.iconRedDot.setVisibility(0);
                ((COUIHintRedDot) this.iconRedDot).setPointMode(this.mIconRedDotMode);
                this.iconRedDot.invalidate();
            } else {
                view4.setVisibility(8);
            }
        }
        COUIRoundImageView cOUIRoundImageView = this.assignmentIcon;
        if (cOUIRoundImageView == null) {
            z2 = false;
        } else {
            Drawable drawable = this.mAssignIconRes;
            if (drawable != null) {
                cOUIRoundImageView.setImageDrawable(drawable);
                this.assignmentIcon.setVisibility(0);
                z2 = true;
            } else {
                cOUIRoundImageView.setVisibility(8);
                z2 = false;
            }
        }
        if (this.endRedDot != null) {
            if (this.mEndRedDotMode != 0) {
                this.endRedDot.setLaidOut();
                this.endRedDot.setVisibility(0);
                this.endRedDot.setPointMode(this.mEndRedDotMode);
                this.endRedDot.setPointNumber(this.mEndRedDotNum);
                this.endRedDot.invalidate();
                z2 = true;
            } else {
                this.endRedDot.setVisibility(8);
            }
        }
        if (this.assignRedDot == null) {
            z3 = z2;
        } else if (this.mAssignRedDotMode != 0) {
            this.assignRedDot.setLaidOut();
            this.assignRedDot.setVisibility(0);
            this.assignRedDot.setPointMode(this.mAssignRedDotMode);
            this.assignRedDot.invalidate();
        } else {
            this.assignRedDot.setVisibility(8);
            z3 = z2;
        }
        COUIHintRedDot cOUIHintRedDot = this.assignRedDot;
        if (cOUIHintRedDot != null) {
            ((ViewGroup) cOUIHintRedDot.getParent()).setVisibility(z3 ? 0 : 8);
        }
    }

    @Override // androidx.preference.Preference
    public void onDetached() {
        removePreciseClickListener();
        super.onDetached();
    }

    public void removePreciseClickListener() {
        PreciseClickHelper preciseClickHelper = this.mPreciseHelper;
        if (preciseClickHelper != null) {
            preciseClickHelper.unSet();
            this.mPreciseHelper = null;
        }
    }

    public void setAssignIconRes(Drawable drawable) {
        if (this.mAssignIconRes != drawable) {
            this.mAssignIconRes = drawable;
            notifyChanged();
        }
    }

    public void setAssignRedDotMode(int i3) {
        if (this.mAssignRedDotMode != i3) {
            this.mAssignRedDotMode = i3;
            notifyChanged();
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

    public void setClickStyle(int i3) {
        this.mClickStyle = i3;
    }

    public void setCouiSummaryLine(int i3) {
        if (this.mCouiSummaryLineLimit != i3) {
            this.mCouiSummaryLineLimit = i3;
            notifyChanged();
        }
    }

    public void setEndRedDotMode(int i3) {
        if (this.mEndRedDotMode != i3) {
            this.mEndRedDotMode = i3;
            notifyChanged();
        }
    }

    public void setEndRedDotNum(int i3) {
        if (this.mEndRedDotNum != i3) {
            this.mEndRedDotNum = i3;
            notifyChanged();
        }
    }

    public void setIconRedDotMode(int i3) {
        if (this.mIconRedDotMode != i3) {
            this.mIconRedDotMode = i3;
            notifyChanged();
        }
    }

    public void setIconStyle(int i3) {
        if (i3 == 0 || i3 == 1) {
            this.mIconStyle = i3;
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

    public void setJump(Drawable drawable) {
        if (this.mJumpRes != drawable) {
            this.mJumpRes = drawable;
            notifyChanged();
        }
    }

    public void setOnPreciseClickListener(PreciseClickHelper.OnPreciseClickListener onPreciseClickListener) {
        this.mPreciseListener = onPreciseClickListener;
        initPreciseHelper();
    }

    public void setSelected(boolean z2) {
        if (this.mIsSelected != z2) {
            this.mIsSelected = z2;
            notifyChanged();
        }
    }

    public void setSelectedState(boolean z2) {
        this.mIsSelected = z2;
    }

    public void setShowDivider(boolean z2) {
        if (this.mShowDivider != z2) {
            this.mShowDivider = z2;
            notifyChanged();
        }
    }

    public void setStatusText1(CharSequence charSequence) {
        if ((charSequence != null || this.mStatusText1 == null) && (charSequence == null || charSequence.equals(this.mStatusText1))) {
            return;
        }
        this.mStatusText1 = charSequence;
        notifyChanged();
    }

    public void setSummaryTextColor(ColorStateList colorStateList) {
        this.mSummaryTextColor = colorStateList;
        notifyChanged();
    }

    public void setTitleColor(ColorStateList colorStateList) {
        if (this.mTitleTextColor != colorStateList) {
            this.mTitleTextColor = colorStateList;
            notifyChanged();
        }
    }

    public void showAssignRedDot() {
        if (this.assignRedDot != null) {
            this.assignRedDot.executeScaleAnim(true);
            notifyChanged();
        }
    }

    public void showEndRedDot() {
        if (this.endRedDot != null) {
            this.endRedDot.executeScaleAnim(true);
            notifyChanged();
        }
    }

    public void showIconRedDot() {
        View view = this.iconRedDot;
        if (view instanceof COUIHintRedDot) {
            ((COUIHintRedDot) view).executeScaleAnim(true);
            notifyChanged();
        }
    }

    public COUIPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, androidx.preference.R.attr.preferenceStyle);
    }

    public COUIPreference(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0);
    }

    public COUIPreference(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mShowDivider = true;
        this.mClickStyle = 0;
        this.mIsSelected = false;
        this.mIsBackgroundAnimationEnabled = true;
        this.mIsCustom = false;
        this.mTitleTextColor = null;
        this.mSummaryTextColor = null;
        this.mCouiSetDefaultColor = false;
        this.mCouiSummaryLineLimit = 0;
        this.mContext = context;
        this.mDividerDefaultHorizontalPadding = context.getResources().getDimensionPixelSize(R.dimen.coui_preference_divider_default_horizontal_padding);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUIPreference, i3, i4);
        this.mShowDivider = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIPreference_couiShowDivider, this.mShowDivider);
        this.mIsEnableClickSpan = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIPreference_couiEnalbeClickSpan, false);
        this.mJumpRes = typedArrayObtainStyledAttributes.getDrawable(R.styleable.COUIPreference_coui_jump_mark);
        this.mAssignIconRes = typedArrayObtainStyledAttributes.getDrawable(R.styleable.COUIPreference_coui_assign_icon);
        this.mStatusText1 = typedArrayObtainStyledAttributes.getText(R.styleable.COUIPreference_coui_jump_status1);
        this.mClickStyle = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIPreference_couiClickStyle, 0);
        this.mAssignment = typedArrayObtainStyledAttributes.getText(R.styleable.COUIPreference_couiAssignment);
        this.mAssignmentColor = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIPreference_couiAssignmentColor, 0);
        this.mIconStyle = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIPreference_couiIconStyle, 1);
        this.mHasBorder = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIPreference_hasBorder, false);
        this.mRadius = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIPreference_preference_icon_radius, 14);
        this.mIconRedDotMode = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIPreference_iconRedDotMode, 0);
        this.mEndRedDotMode = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIPreference_endRedDotMode, 0);
        this.mAssignRedDotMode = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIPreference_assignRedDotMode, 0);
        this.mEndRedDotNum = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIPreference_endRedDotNum, 0);
        this.mIsBackgroundAnimationEnabled = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIPreference_isBackgroundAnimationEnabled, true);
        this.mIsSupportCardUse = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIPreference_isSupportCardUse, true);
        boolean z2 = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIPreference_couiSetDefaultColor, false);
        this.mCouiSetDefaultColor = z2;
        if (z2) {
            this.mTitleTextColor = typedArrayObtainStyledAttributes.getColorStateList(R.styleable.COUIPreference_titleTextColor);
            this.mSummaryTextColor = typedArrayObtainStyledAttributes.getColorStateList(R.styleable.COUIPreference_couiSummaryColor);
        }
        this.mIsCustom = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIPreference_couiIsCustomIcon, false);
        this.mCouiSummaryLineLimit = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIPreference_couiSummaryLineLimit, 0);
        typedArrayObtainStyledAttributes.recycle();
    }

    public void setJump(int i3) {
        setJump(this.mContext.getResources().getDrawable(i3));
    }
}