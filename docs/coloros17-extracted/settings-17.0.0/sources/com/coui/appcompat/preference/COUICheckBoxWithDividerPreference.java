package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.preference.CheckBoxPreference;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.COUIRecyclerView;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.cardlist.COUICardListSelectedItemLayout;
import com.coui.appcompat.checkbox.COUICheckBox;
import com.coui.appcompat.poplist.PreciseClickHelper;
import com.support.preference.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUICheckBoxWithDividerPreference extends CheckBoxPreference implements COUICardSupportInterface, COUIRecyclerView.ICOUIDividerDecorationInterface {
    private CharSequence mAssignment;
    private int mAssignmentColor;
    private LinearLayout mCheckBoxLayout;
    private OnMainLayoutClickListener mClickListener;
    private int mDividerDefaultHorizontalPadding;
    private boolean mIsBackgroundAnimationEnabled;
    private boolean mIsSupportCardUse;
    private View mItemView;
    private LinearLayout mMainLayout;
    private PreciseClickHelper mPreciseHelper;
    private PreciseClickHelper.OnPreciseClickListener mPreciseListener;
    private TextView mTitleView;

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUICheckBoxWithDividerPreference$1, reason: invalid class name */
    public class AnonymousClass1 implements View.OnClickListener {
        public AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (COUICheckBoxWithDividerPreference.access$000(COUICheckBoxWithDividerPreference.this) != null) {
                COUICheckBoxWithDividerPreference.access$000(COUICheckBoxWithDividerPreference.this).onMainLayoutClick();
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUICheckBoxWithDividerPreference$2, reason: invalid class name */
    public class AnonymousClass2 implements View.OnClickListener {
        public AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            COUICheckBoxWithDividerPreference.access$101(COUICheckBoxWithDividerPreference.this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUICheckBoxWithDividerPreference$3, reason: invalid class name */
    public class AnonymousClass3 implements PreciseClickHelper.OnPreciseClickListener {
        public AnonymousClass3() {
        }

        @Override // com.coui.appcompat.poplist.PreciseClickHelper.OnPreciseClickListener
        public void onClick(View view, int i3, int i4) {
            COUICheckBoxWithDividerPreference.access$200(COUICheckBoxWithDividerPreference.this).onClick(view, i3, i4);
            if (COUICheckBoxWithDividerPreference.access$300(COUICheckBoxWithDividerPreference.this) == null || COUICheckBoxWithDividerPreference.access$000(COUICheckBoxWithDividerPreference.this) == null) {
                return;
            }
            COUICheckBoxWithDividerPreference.access$000(COUICheckBoxWithDividerPreference.this).onMainLayoutClick();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUICheckBoxWithDividerPreference$4, reason: invalid class name */
    public class AnonymousClass4 implements View.OnClickListener {
        public AnonymousClass4() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (COUICheckBoxWithDividerPreference.access$000(COUICheckBoxWithDividerPreference.this) != null) {
                COUICheckBoxWithDividerPreference.access$000(COUICheckBoxWithDividerPreference.this).onMainLayoutClick();
            }
        }
    }

    public interface OnMainLayoutClickListener {
        void onMainLayoutClick();
    }

    public COUICheckBoxWithDividerPreference(Context context) {
        this(context, null);
    }

    public static /* synthetic */ OnMainLayoutClickListener access$000(COUICheckBoxWithDividerPreference cOUICheckBoxWithDividerPreference) {
        return cOUICheckBoxWithDividerPreference.mClickListener;
    }

    public static /* synthetic */ void access$101(COUICheckBoxWithDividerPreference cOUICheckBoxWithDividerPreference) {
        super.onClick();
    }

    public static /* synthetic */ PreciseClickHelper.OnPreciseClickListener access$200(COUICheckBoxWithDividerPreference cOUICheckBoxWithDividerPreference) {
        return cOUICheckBoxWithDividerPreference.mPreciseListener;
    }

    public static /* synthetic */ LinearLayout access$300(COUICheckBoxWithDividerPreference cOUICheckBoxWithDividerPreference) {
        return cOUICheckBoxWithDividerPreference.mMainLayout;
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

    public OnMainLayoutClickListener getOnMainLayoutClickListener() {
        return this.mClickListener;
    }

    @Override // com.coui.appcompat.preference.COUICardSupportInterface
    public boolean isSupportCardUse() {
        return this.mIsSupportCardUse;
    }

    @Override // androidx.preference.CheckBoxPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        LinearLayout linearLayout = (LinearLayout) preferenceViewHolder.itemView.findViewById(R.id.main_layout);
        this.mMainLayout = linearLayout;
        View view = preferenceViewHolder.itemView;
        if ((view instanceof COUICardListSelectedItemLayout) && linearLayout != null) {
            ((COUICardListSelectedItemLayout) view).setMainLayoutToSetExtraPadding(linearLayout);
        }
        View view2 = preferenceViewHolder.itemView;
        this.mItemView = view2;
        if (view2 instanceof ListSelectedItemLayout) {
            ((ListSelectedItemLayout) view2).setBackgroundAnimationEnabled(this.mIsBackgroundAnimationEnabled);
        }
        View viewFindViewById = preferenceViewHolder.findViewById(android.R.id.checkbox);
        View viewFindViewById2 = preferenceViewHolder.findViewById(android.R.id.icon);
        View viewFindViewById3 = preferenceViewHolder.findViewById(R.id.img_layout);
        if (viewFindViewById3 != null) {
            if (viewFindViewById2 != null) {
                viewFindViewById3.setVisibility(viewFindViewById2.getVisibility());
            } else {
                viewFindViewById3.setVisibility(8);
            }
        }
        if (viewFindViewById != null && (viewFindViewById instanceof COUICheckBox)) {
            ((COUICheckBox) viewFindViewById).setState(isChecked() ? 2 : 0);
        }
        this.mTitleView = (TextView) preferenceViewHolder.findViewById(android.R.id.title);
        initPreciseHelper();
        LinearLayout linearLayout2 = this.mMainLayout;
        if (linearLayout2 != null) {
            if (this.mPreciseListener == null) {
                linearLayout2.setOnClickListener(new AnonymousClass1());
            }
            this.mMainLayout.setClickable(isSelectable());
        }
        LinearLayout linearLayout3 = (LinearLayout) preferenceViewHolder.itemView.findViewById(R.id.check_box_layout);
        this.mCheckBoxLayout = linearLayout3;
        if (linearLayout3 != null) {
            linearLayout3.setOnClickListener(new AnonymousClass2());
            this.mCheckBoxLayout.setClickable(isSelectable());
        }
        COUIPreferenceUtils.bindAssignmentView(preferenceViewHolder, this.mAssignment, this.mAssignmentColor);
        COUICardListHelper.setItemCardBackground(preferenceViewHolder.itemView, COUICardListHelper.getPositionInGroup(this));
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

    @Override // com.coui.appcompat.preference.COUICardSupportInterface
    public void setIsSupportCardUse(boolean z2) {
        this.mIsSupportCardUse = z2;
    }

    public void setOnMainLayoutListener(OnMainLayoutClickListener onMainLayoutClickListener) {
        this.mClickListener = onMainLayoutClickListener;
    }

    public void setOnPreciseClickListener(PreciseClickHelper.OnPreciseClickListener onPreciseClickListener) {
        this.mPreciseListener = onPreciseClickListener;
        initPreciseHelper();
    }

    public COUICheckBoxWithDividerPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiCheckBoxWithDividerPreferenceStyle);
    }

    public COUICheckBoxWithDividerPreference(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, R.style.Preference_COUI_COUICheckBoxWithDividerPreference);
    }

    public COUICheckBoxWithDividerPreference(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mAssignmentColor = 0;
        this.mIsBackgroundAnimationEnabled = true;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUICheckBoxPreference, i3, i4);
        this.mAssignment = typedArrayObtainStyledAttributes.getText(R.styleable.COUICheckBoxPreference_couiCheckBoxAssignment);
        typedArrayObtainStyledAttributes.recycle();
        TypedArray typedArrayObtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R.styleable.COUIPreference, i3, i4);
        this.mIsSupportCardUse = typedArrayObtainStyledAttributes2.getBoolean(R.styleable.COUIPreference_isSupportCardUse, true);
        this.mAssignmentColor = typedArrayObtainStyledAttributes2.getInt(R.styleable.COUIPreference_couiAssignmentColor, 0);
        this.mIsBackgroundAnimationEnabled = typedArrayObtainStyledAttributes2.getBoolean(R.styleable.COUIPreference_isBackgroundAnimationEnabled, true);
        typedArrayObtainStyledAttributes2.recycle();
        this.mDividerDefaultHorizontalPadding = getContext().getResources().getDimensionPixelSize(R.dimen.coui_preference_divider_default_horizontal_padding);
    }
}