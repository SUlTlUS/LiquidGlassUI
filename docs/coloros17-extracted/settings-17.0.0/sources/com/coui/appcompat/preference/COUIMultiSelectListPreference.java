package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.COUIRecyclerView;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.cardlist.COUICardListSelectedItemLayout;
import com.coui.appcompat.uiutil.AnimLevel;
import com.coui.appcompat.uiutil.UIUtil;
import com.support.preference.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIMultiSelectListPreference extends MultiSelectListPreference implements COUICardSupportInterface, COUIRecyclerView.ICOUIDividerDecorationInterface {
    private CharSequence mAssignment;
    private boolean mBlurBackground;
    private AnimLevel mBlurMinAnimLevel;
    Context mContext;
    private int mDividerDefaultHorizontalPadding;
    private boolean mIfFollowHand;
    private boolean mIsBackgroundAnimationEnabled;
    private boolean mIsSupportCardUse;
    private View mItemView;
    Drawable mJumpRes;
    private Point mLastTouchPoint;
    private View mPreferenceView;
    CharSequence mStatusText1;
    private CharSequence[] mSummaries;
    private TextView mTitleView;

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIMultiSelectListPreference$1, reason: invalid class name */
    public class AnonymousClass1 implements View.OnTouchListener {
        public AnonymousClass1() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getActionMasked() != 0) {
                return false;
            }
            COUIMultiSelectListPreference.access$000(COUIMultiSelectListPreference.this).set((int) motionEvent.getX(), (int) motionEvent.getY());
            return false;
        }
    }

    public COUIMultiSelectListPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mLastTouchPoint = new Point();
        this.mIfFollowHand = true;
        this.mBlurBackground = false;
        this.mIsBackgroundAnimationEnabled = true;
        this.mContext = context;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUIPreference, 0, R.style.Preference_COUI_COUIWithPopupIcon);
        this.mIsSupportCardUse = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIPreference_isSupportCardUse, true);
        this.mAssignment = typedArrayObtainStyledAttributes.getText(R.styleable.COUIPreference_couiAssignment);
        this.mJumpRes = typedArrayObtainStyledAttributes.getDrawable(R.styleable.COUIPreference_coui_jump_mark);
        this.mStatusText1 = typedArrayObtainStyledAttributes.getText(R.styleable.COUIPreference_coui_jump_status1);
        this.mIfFollowHand = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIPreference_couiIfFollowHand, true);
        this.mBlurBackground = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIPreference_couiDialogBlurBackground, false);
        this.mBlurMinAnimLevel = AnimLevel.valueOf(typedArrayObtainStyledAttributes.getInt(R.styleable.COUIPreference_couiBlurAnimLevel, UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN.getIntValue()));
        this.mIsBackgroundAnimationEnabled = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIPreference_isBackgroundAnimationEnabled, true);
        typedArrayObtainStyledAttributes.recycle();
        this.mDividerDefaultHorizontalPadding = getContext().getResources().getDimensionPixelSize(R.dimen.coui_preference_divider_default_horizontal_padding);
    }

    public static /* synthetic */ Point access$000(COUIMultiSelectListPreference cOUIMultiSelectListPreference) {
        return cOUIMultiSelectListPreference.mLastTouchPoint;
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

    public AnimLevel getBlurMinAnimLevel() {
        return this.mBlurMinAnimLevel;
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

    public Drawable getJump() {
        return this.mJumpRes;
    }

    public Point getLastTouchPoint() {
        return this.mLastTouchPoint;
    }

    public View getPreferenceView() {
        return this.mPreferenceView;
    }

    public CharSequence getStatusText1() {
        return this.mStatusText1;
    }

    public CharSequence[] getSummaries() {
        return this.mSummaries;
    }

    public boolean isBlurBackground() {
        return this.mBlurBackground;
    }

    public boolean isIfFollowHand() {
        return this.mIfFollowHand;
    }

    @Override // com.coui.appcompat.preference.COUICardSupportInterface
    public boolean isSupportCardUse() {
        return this.mIsSupportCardUse;
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View view = preferenceViewHolder.itemView;
        this.mItemView = view;
        if (view instanceof ListSelectedItemLayout) {
            ((ListSelectedItemLayout) view).setBackgroundAnimationEnabled(this.mIsBackgroundAnimationEnabled);
        }
        COUIPreferenceUtils.bindView(preferenceViewHolder, this.mJumpRes, this.mStatusText1, getAssignment());
        COUICardListHelper.setItemCardBackground(preferenceViewHolder.itemView, COUICardListHelper.getPositionInGroup(this));
        this.mTitleView = (TextView) preferenceViewHolder.findViewById(android.R.id.title);
        View view2 = preferenceViewHolder.itemView;
        this.mPreferenceView = view2;
        view2.setOnTouchListener(new AnonymousClass1());
    }

    public void setAssignment(CharSequence charSequence) {
        if (TextUtils.equals(this.mAssignment, charSequence)) {
            return;
        }
        this.mAssignment = charSequence;
        notifyChanged();
    }

    public void setBackgroundAnimationEnabled(boolean z2) {
        if (this.mIsBackgroundAnimationEnabled != z2) {
            this.mIsBackgroundAnimationEnabled = z2;
            notifyChanged();
        }
    }

    public void setBlurBackground(boolean z2) {
        this.mBlurBackground = z2;
    }

    public void setBlurMinAnimLevel(AnimLevel animLevel) {
        this.mBlurMinAnimLevel = animLevel;
    }

    public void setIfFollowHand(boolean z2) {
        this.mIfFollowHand = z2;
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

    public void setStatusText1(CharSequence charSequence) {
        if ((charSequence != null || this.mStatusText1 == null) && (charSequence == null || charSequence.equals(this.mStatusText1))) {
            return;
        }
        this.mStatusText1 = charSequence;
        notifyChanged();
    }

    public void setSummaries(CharSequence[] charSequenceArr) {
        this.mSummaries = charSequenceArr;
    }

    public void setJump(int i3) {
        setJump(this.mContext.getResources().getDrawable(i3));
    }

    public COUIMultiSelectListPreference(Context context) {
        super(context, null);
        this.mLastTouchPoint = new Point();
        this.mIfFollowHand = true;
        this.mBlurBackground = false;
        this.mIsBackgroundAnimationEnabled = true;
    }
}