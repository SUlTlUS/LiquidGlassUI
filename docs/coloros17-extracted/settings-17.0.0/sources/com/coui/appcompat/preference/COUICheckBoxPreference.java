package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;
import androidx.preference.CheckBoxPreference;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.COUIRecyclerView;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.cardlist.COUICardListSelectedItemLayout;
import com.coui.appcompat.checkbox.COUICheckBox;
import com.oplus.settings.cloud.SettingsHelper;
import com.support.appcompat.R;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u0011\b\u0016\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004¢\u0006\u0002\u0010\u0005B\u001b\b\u0016\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\bB#\b\u0016\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\u0006\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u000bB+\b\u0016\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\f\u001a\u00020\n¢\u0006\u0002\u0010\rJ\b\u0010\u001b\u001a\u00020\u0016H\u0016J\b\u0010\u001c\u001a\u00020\nH\u0016J\n\u0010\u001d\u001a\u0004\u0018\u00010\u0018H\u0016J\b\u0010\u001e\u001a\u00020\nH\u0016J\u0012\u0010\u001f\u001a\u00020 2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016J\u000e\u0010#\u001a\u00020 2\u0006\u0010$\u001a\u00020\u000fJ\u000e\u0010%\u001a\u00020 2\u0006\u0010&\u001a\u00020\nJ\u000e\u0010'\u001a\u00020 2\u0006\u0010(\u001a\u00020\u0016R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u001aX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006)"}, d2 = {"Lcom/coui/appcompat/preference/COUICheckBoxPreference;", "Landroidx/preference/CheckBoxPreference;", "Landroidx/recyclerview/widget/COUIRecyclerView$ICOUIDividerDecorationInterface;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "defStyleRes", "(Landroid/content/Context;Landroid/util/AttributeSet;II)V", "mAssignment", "", "mAssignmentColor", "mCheckBox", "Lcom/coui/appcompat/checkbox/COUICheckBox;", "mContext", "mDividerDefaultHorizontalPadding", "mIsBackgroundAnimationEnabled", "", "mItemView", "Landroid/view/View;", "mTitleView", "Landroid/widget/TextView;", "drawDivider", "getDividerEndInset", "getDividerStartAlignView", "getDividerStartInset", "onBindViewHolder", "", "holder", "Landroidx/preference/PreferenceViewHolder;", "setAssignment", "assignment", "setAssignmentColor", "assignmentColor", "setBackgroundAnimationEnabled", "isAnimationEnabled", "coui-support-preference_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class COUICheckBoxPreference extends CheckBoxPreference implements COUIRecyclerView.ICOUIDividerDecorationInterface {
    private CharSequence mAssignment;
    private int mAssignmentColor;
    private COUICheckBox mCheckBox;
    private Context mContext;
    private int mDividerDefaultHorizontalPadding;
    private boolean mIsBackgroundAnimationEnabled;
    private View mItemView;
    private TextView mTitleView;

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUICheckBoxPreference$onBindViewHolder$1, reason: invalid class name */
    @Metadata(d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, d2 = {"com/coui/appcompat/preference/COUICheckBoxPreference$onBindViewHolder$1", "Landroid/view/View$AccessibilityDelegate;", "onInitializeAccessibilityNodeInfo", "", "host", "Landroid/view/View;", SettingsHelper.KEY_INFO, "Landroid/view/accessibility/AccessibilityNodeInfo;", "coui-support-preference_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class AnonymousClass1 extends View.AccessibilityDelegate {
        public AnonymousClass1() {
        }

        @Override // android.view.View.AccessibilityDelegate
        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfo info) {
            Intrinsics.checkNotNullParameter(host, "host");
            Intrinsics.checkNotNullParameter(info, "info");
            super.onInitializeAccessibilityNodeInfo(host, info);
            String string = COUICheckBoxPreference.this.getContext().getResources().getString(R.string.coui_accessibility_switch);
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            String string2 = COUICheckBoxPreference.this.getContext().getResources().getString(R.string.coui_accessibility_select_all);
            Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
            COUICheckBox cOUICheckBoxAccess$getMCheckBox$p = COUICheckBoxPreference.access$getMCheckBox$p(COUICheckBoxPreference.this);
            if (cOUICheckBoxAccess$getMCheckBox$p != null && cOUICheckBoxAccess$getMCheckBox$p.getState() == 1) {
                string = string2;
            }
            info.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, string));
        }
    }

    public COUICheckBoxPreference(Context context) {
        this(context, null);
    }

    public static final /* synthetic */ COUICheckBox access$getMCheckBox$p(COUICheckBoxPreference cOUICheckBoxPreference) {
        return cOUICheckBoxPreference.mCheckBox;
    }

    public static /* synthetic */ boolean c(View view, MotionEvent motionEvent) {
        return onBindViewHolder$lambda$0(view, motionEvent);
    }

    private static final boolean onBindViewHolder$lambda$0(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() != 0) {
            return false;
        }
        view.performHapticFeedback(302);
        return false;
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
    /* JADX INFO: renamed from: getDividerEndInset, reason: from getter */
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

    @Override // androidx.preference.CheckBoxPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder holder) {
        View view;
        super.onBindViewHolder(holder);
        View view2 = holder != null ? holder.itemView : null;
        this.mItemView = view2;
        if (view2 instanceof ListSelectedItemLayout) {
            Intrinsics.checkNotNull(view2, "null cannot be cast to non-null type com.coui.appcompat.preference.ListSelectedItemLayout");
            ((ListSelectedItemLayout) view2).setBackgroundAnimationEnabled(this.mIsBackgroundAnimationEnabled);
        }
        View view3 = this.mItemView;
        if (view3 != null) {
            view3.setAccessibilityDelegate(new AnonymousClass1());
        }
        View viewFindViewById = holder != null ? holder.findViewById(android.R.id.title) : null;
        this.mTitleView = viewFindViewById instanceof TextView ? (TextView) viewFindViewById : null;
        View viewFindViewById2 = holder != null ? holder.findViewById(android.R.id.checkbox) : null;
        COUICheckBox cOUICheckBox = viewFindViewById2 instanceof COUICheckBox ? (COUICheckBox) viewFindViewById2 : null;
        this.mCheckBox = cOUICheckBox;
        if (cOUICheckBox != null) {
            cOUICheckBox.setState(this.mChecked ? 2 : 0);
        }
        if (holder != null && (view = holder.itemView) != null) {
            view.setOnTouchListener(new a());
        }
        View viewFindViewById3 = holder != null ? holder.findViewById(android.R.id.icon) : null;
        View viewFindViewById4 = holder != null ? holder.findViewById(com.support.preference.R.id.img_layout) : null;
        if (viewFindViewById4 != null) {
            if (viewFindViewById3 != null) {
                viewFindViewById4.setVisibility(viewFindViewById3.getVisibility());
            } else {
                viewFindViewById4.setVisibility(8);
            }
        }
        COUIPreferenceUtils.bindAssignmentView(holder, this.mAssignment, this.mAssignmentColor);
        Intrinsics.checkNotNull(holder);
        COUICardListHelper.setItemCardBackground(holder.itemView, COUICardListHelper.getPositionInGroup(this));
    }

    public final void setAssignment(CharSequence assignment) {
        Intrinsics.checkNotNullParameter(assignment, "assignment");
        if (TextUtils.equals(this.mAssignment, assignment)) {
            return;
        }
        this.mAssignment = assignment;
        notifyChanged();
    }

    public final void setAssignmentColor(int assignmentColor) {
        if (this.mAssignmentColor != assignmentColor) {
            this.mAssignmentColor = assignmentColor;
            notifyChanged();
        }
    }

    public final void setBackgroundAnimationEnabled(boolean isAnimationEnabled) {
        if (this.mIsBackgroundAnimationEnabled != isAnimationEnabled) {
            this.mIsBackgroundAnimationEnabled = isAnimationEnabled;
            notifyChanged();
        }
    }

    public COUICheckBoxPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, com.support.preference.R.attr.couiCheckBoxPreferenceStyle);
    }

    public COUICheckBoxPreference(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, com.support.preference.R.style.Preference_COUI_COUICheckBoxPreference);
    }

    public COUICheckBoxPreference(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mIsBackgroundAnimationEnabled = true;
        this.mContext = context;
        this.mDividerDefaultHorizontalPadding = getContext().getResources().getDimensionPixelSize(com.support.preference.R.dimen.coui_preference_divider_default_horizontal_padding);
        Intrinsics.checkNotNull(context);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, com.support.preference.R.styleable.COUIPreference, i3, i4);
        Intrinsics.checkNotNullExpressionValue(typedArrayObtainStyledAttributes, "obtainStyledAttributes(...)");
        this.mAssignmentColor = typedArrayObtainStyledAttributes.getInt(com.support.preference.R.styleable.COUIPreference_couiAssignmentColor, 0);
        this.mIsBackgroundAnimationEnabled = typedArrayObtainStyledAttributes.getBoolean(com.support.preference.R.styleable.COUIPreference_isBackgroundAnimationEnabled, true);
        typedArrayObtainStyledAttributes.recycle();
        TypedArray typedArrayObtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, com.support.preference.R.styleable.COUICheckBoxPreference, i3, i4);
        Intrinsics.checkNotNullExpressionValue(typedArrayObtainStyledAttributes2, "obtainStyledAttributes(...)");
        this.mAssignment = typedArrayObtainStyledAttributes2.getText(com.support.preference.R.styleable.COUICheckBoxPreference_couiCheckBoxAssignment);
        typedArrayObtainStyledAttributes2.recycle();
    }
}