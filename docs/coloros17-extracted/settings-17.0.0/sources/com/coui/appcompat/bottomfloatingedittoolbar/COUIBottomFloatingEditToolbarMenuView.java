package com.coui.appcompat.bottomfloatingedittoolbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.coui.appcompat.accessibilityutil.COUIAccessibilityUtil;
import com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingItem;
import com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView;
import com.coui.appcompat.bottomfloatingtoolbar.COUIBottomFloatingToolbarItemView;
import com.support.bottomnavigation.R;
import java.util.List;

/* JADX INFO: loaded from: classes7.dex */
public class COUIBottomFloatingEditToolbarMenuView extends BaseBottomFloatingMenuView {
    private int mInnerPaddingHorizontal;
    private int mInnerPaddingVertical;
    private boolean mIsHorizontalStyle;
    private int mItemMaxWidthHorizontal;
    private int mItemMaxWidthVertical;
    private final int mMenuHeightMediumLarge;
    private final int mMenuHeightSmall;
    private int mSingleItemWidthHorizontal;
    private int mSingleItemWidthVertical;

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingedittoolbar.COUIBottomFloatingEditToolbarMenuView$1, reason: invalid class name */
    public class AnonymousClass1 extends AccessibilityDelegateCompat {
        public AnonymousClass1() {
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            CharSequence title;
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            CharSequence contentDescription = view.getContentDescription();
            if ((contentDescription == null || contentDescription.length() == 0) && (view instanceof COUIBottomFloatingToolbarItemView) && (title = ((COUIBottomFloatingToolbarItemView) view).getTitle()) != null && title.length() > 0) {
                accessibilityNodeInfoCompat.setContentDescription(title);
            }
            accessibilityNodeInfoCompat.setRoleDescription(null);
            accessibilityNodeInfoCompat.setClassName(COUIAccessibilityUtil.BUTTON_CLASS_NAME);
            accessibilityNodeInfoCompat.setClickable(true);
            accessibilityNodeInfoCompat.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingedittoolbar.COUIBottomFloatingEditToolbarMenuView$2, reason: invalid class name */
    public class AnonymousClass2 implements View.OnClickListener {
        final /* synthetic */ COUIBottomFloatingEditToolbarItem val$item;
        final /* synthetic */ int val$position;

        public AnonymousClass2(COUIBottomFloatingEditToolbarItem cOUIBottomFloatingEditToolbarItem, int i3) {
            this.val$item = cOUIBottomFloatingEditToolbarItem;
            this.val$position = i3;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (COUIBottomFloatingEditToolbarMenuView.access$000(COUIBottomFloatingEditToolbarMenuView.this) != null) {
                COUIBottomFloatingEditToolbarMenuView.access$100(COUIBottomFloatingEditToolbarMenuView.this).onItemClick(view, this.val$item, this.val$position);
            }
        }
    }

    public COUIBottomFloatingEditToolbarMenuView(Context context) {
        this(context, null);
    }

    public static /* synthetic */ BaseBottomFloatingMenuView.OnItemClickListener access$000(COUIBottomFloatingEditToolbarMenuView cOUIBottomFloatingEditToolbarMenuView) {
        return cOUIBottomFloatingEditToolbarMenuView.mOnItemClickListener;
    }

    public static /* synthetic */ BaseBottomFloatingMenuView.OnItemClickListener access$100(COUIBottomFloatingEditToolbarMenuView cOUIBottomFloatingEditToolbarMenuView) {
        return cOUIBottomFloatingEditToolbarMenuView.mOnItemClickListener;
    }

    private void addItem(COUIBottomFloatingEditToolbarItem cOUIBottomFloatingEditToolbarItem, int i3) {
        COUIBottomFloatingToolbarItemView cOUIBottomFloatingToolbarItemView = new COUIBottomFloatingToolbarItemView(getContext());
        cOUIBottomFloatingToolbarItemView.setItemStyle(getItemStyle());
        cOUIBottomFloatingToolbarItemView.setId(cOUIBottomFloatingEditToolbarItem.getId());
        cOUIBottomFloatingToolbarItemView.setTitle(cOUIBottomFloatingEditToolbarItem.getTitle());
        cOUIBottomFloatingToolbarItemView.setIcon(cOUIBottomFloatingEditToolbarItem.getIcon());
        cOUIBottomFloatingToolbarItemView.setContentDescription(cOUIBottomFloatingEditToolbarItem.getDescription());
        cOUIBottomFloatingToolbarItemView.setRedDot(cOUIBottomFloatingEditToolbarItem.getRedDotType(), cOUIBottomFloatingEditToolbarItem.getRedDotNumber());
        cOUIBottomFloatingToolbarItemView.setOnClickListener(new AnonymousClass2(cOUIBottomFloatingEditToolbarItem, i3));
        setupItemAccessibility(cOUIBottomFloatingToolbarItemView, i3);
        addView(cOUIBottomFloatingToolbarItemView);
    }

    private int getItemStyle() {
        return this.mIsHorizontalStyle ? 4 : 3;
    }

    @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView
    public void addItems(List<? extends BaseBottomFloatingItem> list) {
        removeAllViews();
        if (list != null) {
            for (int i3 = 0; i3 < list.size(); i3++) {
                BaseBottomFloatingItem baseBottomFloatingItem = list.get(i3);
                if (baseBottomFloatingItem instanceof COUIBottomFloatingEditToolbarItem) {
                    addItem((COUIBottomFloatingEditToolbarItem) baseBottomFloatingItem, i3);
                }
            }
        }
        applyUniformItemTitleTextColors();
        refreshMenuTouchFeedback();
    }

    public int getMenuHeight() {
        return this.mFixedVisibleHeight;
    }

    @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView
    public boolean isMenuTouchFeedbackEnabled() {
        int childCount = getChildCount();
        if (childCount == 0) {
            return true;
        }
        for (int i3 = 0; i3 < childCount; i3++) {
            if (getChildAt(i3).isEnabled()) {
                return true;
            }
        }
        return false;
    }

    @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        super.onLayout(z2, i3, i4, i5, i6);
        syncDrawingBoundsWithPosition();
    }

    @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView, android.view.View
    public void onMeasure(int i3, int i4) {
        this.mFixedVisibleHeight = this.mIsHorizontalStyle ? this.mMenuHeightMediumLarge : this.mMenuHeightSmall;
        super.onMeasure(i3, i4);
    }

    public void setHorizontalStyle(boolean z2) {
        if (getChildCount() == 1) {
            this.mItemMaxWidth = z2 ? this.mSingleItemWidthHorizontal : this.mSingleItemWidthVertical;
        } else {
            this.mItemMaxWidth = z2 ? this.mItemMaxWidthHorizontal : this.mItemMaxWidthVertical;
        }
        this.mInnerPadding = z2 ? this.mInnerPaddingHorizontal : this.mInnerPaddingVertical;
        if (this.mIsHorizontalStyle != z2) {
            this.mIsHorizontalStyle = z2;
            for (int i3 = 0; i3 < getChildCount(); i3++) {
                View childAt = getChildAt(i3);
                if (childAt instanceof COUIBottomFloatingToolbarItemView) {
                    ((COUIBottomFloatingToolbarItemView) childAt).setItemStyle(getItemStyle());
                }
            }
        }
    }

    @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView
    public void setItemEnable(int i3, boolean z2) {
        super.setItemEnable(i3, z2);
        refreshMenuTouchFeedback();
    }

    @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView
    public void setupItemAccessibility(View view, int i3) {
        ViewCompat.setAccessibilityDelegate(view, new AnonymousClass1());
    }

    @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView
    public boolean shouldPerformClickOnTouchUp() {
        return false;
    }

    public void syncDrawingBoundsWithPosition() {
        float x2 = getX();
        float y2 = getY();
        this.mDrawingBounds.set(x2, y2, getMeasuredWidth() + x2, getMeasuredHeight() + y2);
    }

    public COUIBottomFloatingEditToolbarMenuView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIBottomFloatingEditToolbarMenuView(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0);
    }

    public COUIBottomFloatingEditToolbarMenuView(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mIsHorizontalStyle = false;
        this.mItemMaxWidthVertical = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_edit_toolbar_item_max_width_vertical);
        this.mItemMaxWidthHorizontal = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_edit_toolbar_item_max_width_horizontal);
        this.mSingleItemWidthVertical = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_edit_toolbar_single_item_width_vertical);
        this.mSingleItemWidthHorizontal = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_edit_toolbar_single_item_width_horizontal);
        this.mInnerPaddingVertical = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_edit_toolbar_inner_padding_vertical);
        this.mInnerPaddingHorizontal = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_edit_toolbar_inner_padding_horizontal);
        this.mMenuHeightSmall = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_navigation_bar_height);
        this.mMenuHeightMediumLarge = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_edit_toolbar_menu_height_medium_large);
        this.mItemMaxWidth = this.mIsHorizontalStyle ? this.mItemMaxWidthHorizontal : this.mItemMaxWidthVertical;
    }
}