package com.coui.appcompat.bottomnavigation;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.core.content.ContextCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.coui.appcompat.material.bottomnavigation.BottomNavigationMenuView;
import com.coui.appcompat.material.navigation.NavigationBarItemView;
import com.support.bottomnavigation.R;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes7.dex */
public class COUINavigationMenuView extends BottomNavigationMenuView {
    protected static final long ACTIVE_ANIMATION_DURATION_MS = 100;
    public static final int MAX_ITEM_COUNT = 10;
    public static final int MAX_ITEM_COUNT_LARGE = 7;
    public static final int MAX_ITEM_COUNT_MEDIUM = 6;
    public static final int MAX_ITEM_COUNT_SMALL = 5;
    private COUINavigationItemView mCOUINavigationItemView;
    private int mDefaultPadding;
    private int mEnlargeIndex;
    private int mItemHeight;
    private int mLayoutType;
    private int mNavigationType;
    private int[] mTempChildWidths;
    private int mTextSize;

    public COUINavigationMenuView(Context context) {
        super(context);
        this.mEnlargeIndex = -1;
        this.mDefaultPadding = getResources().getDimensionPixelSize(R.dimen.coui_tool_navigation_edge_item_padding);
        setClipChildren(false);
        setClipToPadding(false);
        this.mTempChildWidths = new int[10];
    }

    private void setLayoutPadding() {
        if (this.mLayoutType == 1) {
            this.mDefaultPadding = getResources().getDimensionPixelSize(R.dimen.coui_tool_navigation_edge_item_default_padding);
            this.mItemHeight = getResources().getDimensionPixelSize(R.dimen.coui_tool_navigation_item_default_height);
        } else {
            this.mDefaultPadding = getResources().getDimensionPixelSize(R.dimen.coui_tool_navigation_edge_item_padding);
            this.mItemHeight = getResources().getDimensionPixelSize(R.dimen.coui_tool_navigation_item_height);
        }
    }

    @Override // android.view.ViewGroup
    public void addView(View view) {
        super.addView(view);
    }

    public void clearColorFilter() {
        for (int i3 = 0; i3 < getMenu().size(); i3++) {
            NavigationBarItemView navigationBarItemViewFindItemView = findItemView(getMenu().getItem(i3).getItemId());
            if (navigationBarItemViewFindItemView instanceof COUINavigationItemView) {
                ((COUINavigationItemView) navigationBarItemViewFindItemView).clearColorFilter();
            }
        }
    }

    @Override // com.coui.appcompat.material.bottomnavigation.BottomNavigationMenuView, com.coui.appcompat.material.navigation.NavigationBarMenuView
    public NavigationBarItemView createNavigationBarItemView(Context context) {
        COUINavigationItemView cOUINavigationItemView = new COUINavigationItemView(context);
        this.mCOUINavigationItemView = cOUINavigationItemView;
        cOUINavigationItemView.setLongClickable(isLongClickable());
        return this.mCOUINavigationItemView;
    }

    public COUINavigationItemView getCOUINavigationItemView() {
        return this.mCOUINavigationItemView;
    }

    public int getEnlargeId() {
        int i3 = this.mEnlargeIndex;
        return i3 == -1 ? i3 : getMenu().getVisibleItems().get(this.mEnlargeIndex).getItemId();
    }

    public MenuItemImpl getVisibleItem(int i3) {
        return getMenu().getVisibleItems().get(i3);
    }

    public ArrayList<MenuItemImpl> getVisibleItems() {
        return getMenu().getVisibleItems();
    }

    public void initEnlargeParams() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        layoutParams.gravity = 81;
        setLayoutParams(layoutParams);
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setLayoutPadding();
    }

    @Override // com.coui.appcompat.material.navigation.NavigationBarMenuView, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (this.mNavigationType == 0) {
            AccessibilityNodeInfoCompat.wrap(accessibilityNodeInfo).setCollectionInfo(null);
        }
    }

    @Override // com.coui.appcompat.material.bottomnavigation.BottomNavigationMenuView, android.view.View
    public void onMeasure(int i3, int i4) {
        int size = View.MeasureSpec.getSize(i3) - (this.mDefaultPadding * 2);
        int size2 = getMenu().getVisibleItems().size();
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.coui_tool_navigation_item_height);
        this.mItemHeight = dimensionPixelSize;
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(dimensionPixelSize, 1073741824);
        int i5 = size / (size2 == 0 ? 1 : size2);
        int i6 = size - (i5 * size2);
        for (int i7 = 0; i7 < size2; i7++) {
            int[] iArr = this.mTempChildWidths;
            iArr[i7] = i5;
            if (i6 > 0) {
                iArr[i7] = i5 + 1;
                i6--;
            }
        }
        int measuredWidth = 0;
        int i8 = 0;
        for (int i9 = 0; i9 < getChildCount(); i9++) {
            View childAt = getChildAt(i9);
            if (childAt.getVisibility() != 8) {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(this.mTempChildWidths[i8], 1073741824), iMakeMeasureSpec);
                childAt.getLayoutParams().width = childAt.getMeasuredWidth();
                measuredWidth += childAt.getMeasuredWidth();
                i8++;
            }
        }
        setMeasuredDimension(View.resolveSizeAndState(measuredWidth, View.MeasureSpec.makeMeasureSpec(measuredWidth, 1073741824), 0), View.resolveSizeAndState(this.mItemHeight, iMakeMeasureSpec, 0));
    }

    public void setEnlarge(boolean z2, int i3) {
        this.mEnlargeIndex = i3;
        if (!z2 || i3 < 0) {
            return;
        }
        int i4 = 0;
        while (i4 < getMenu().getVisibleItems().size()) {
            NavigationBarItemView navigationBarItemViewFindItemView = findItemView(getMenu().getVisibleItems().get(i4).getItemId());
            if (navigationBarItemViewFindItemView instanceof COUINavigationItemView) {
                ((COUINavigationItemView) navigationBarItemViewFindItemView).setEnlarge(true, i4 == this.mEnlargeIndex);
            }
            i4++;
        }
    }

    public void setItemForEnlargeColor() {
        for (int i3 = 0; i3 < getMenu().size(); i3++) {
            NavigationBarItemView navigationBarItemViewFindItemView = findItemView(getMenu().getItem(i3).getItemId());
            if (navigationBarItemViewFindItemView instanceof COUINavigationItemView) {
                navigationBarItemViewFindItemView.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.coui_navigation_enlarge_item_color)));
                ((COUINavigationItemView) navigationBarItemViewFindItemView).setIconTintForWhite();
            }
        }
    }

    public void setItemLayoutType(int i3) {
        this.mLayoutType = i3;
        setLayoutPadding();
        for (int i4 = 0; i4 < getMenu().size(); i4++) {
            NavigationBarItemView navigationBarItemViewFindItemView = findItemView(getMenu().getItem(i4).getItemId());
            if (navigationBarItemViewFindItemView instanceof COUINavigationItemView) {
                ((COUINavigationItemView) navigationBarItemViewFindItemView).childLayout(this.mLayoutType);
            }
        }
    }

    public void setItemNavigationType(int i3) {
        if (this.mNavigationType == i3) {
            return;
        }
        this.mNavigationType = i3;
        if (getMenu() != null) {
            for (int i4 = 0; i4 < getMenu().size(); i4++) {
                NavigationBarItemView navigationBarItemViewFindItemView = findItemView(getMenu().getItem(i4).getItemId());
                if (navigationBarItemViewFindItemView instanceof COUINavigationItemView) {
                    ((COUINavigationItemView) navigationBarItemViewFindItemView).setNavigationType(i3);
                }
            }
        }
    }

    public void setShowPressShadow(boolean z2) {
        for (int i3 = 0; i3 < getMenu().size(); i3++) {
            NavigationBarItemView navigationBarItemViewFindItemView = findItemView(getMenu().getItem(i3).getItemId());
            if (navigationBarItemViewFindItemView instanceof COUINavigationItemView) {
                ((COUINavigationItemView) navigationBarItemViewFindItemView).setShowPressShadow(z2);
            }
        }
    }

    public void setTextSize(int i3) {
        this.mTextSize = i3;
        if (getMenu() != null) {
            for (int i4 = 0; i4 < getMenu().size(); i4++) {
                NavigationBarItemView navigationBarItemViewFindItemView = findItemView(getMenu().getItem(i4).getItemId());
                if (navigationBarItemViewFindItemView instanceof COUINavigationItemView) {
                    ((COUINavigationItemView) navigationBarItemViewFindItemView).setTextSize(this.mTextSize);
                }
            }
        }
    }
}