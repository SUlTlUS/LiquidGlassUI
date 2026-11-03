package com.coui.appcompat.poplist;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes7.dex */
public class PopupListItem {
    public static final int MENU_GROUP_ITEM_ACTIVATED_IN_MAIN = 1;
    public static final int MENU_GROUP_ITEM_ACTIVATED_IN_SUB = 2;
    public static final int MENU_GROUP_ITEM_INACTIVE = 0;
    public static final int MENU_HINT_TYPE_CUSTOM = 1;
    public static final int MENU_HINT_TYPE_NONE = -1;
    public static final int MENU_HINT_TYPE_RED_DOT = 0;
    public static final int MENU_ITEM_FORCE_TINT_ALL = 7;
    public static final int MENU_ITEM_FORCE_TINT_ICON = 1;
    public static final int MENU_ITEM_FORCE_TINT_NONE = 0;
    public static final int MENU_ITEM_FORCE_TINT_STATE_ICON = 4;
    public static final int MENU_ITEM_FORCE_TINT_TITLE = 2;
    public static final int MENU_ITEM_TYPE_ALERT = 1;
    public static final int MENU_ITEM_TYPE_CUSTOM = 2;
    public static final int MENU_ITEM_TYPE_DEFAULT = 0;
    public static final int MENU_ITEM_TYPE_HEADER = 3;
    public static final int MENU_ITEM_TYPE_HORIZONTAL = 4;
    private View mCustomHintView;
    private View mCustomItemView;
    private String mDescription;
    private String mDescriptionContentDescription;
    private int mForceTint;
    private int mGroupId;
    private int mGroupState;
    private int mHintType;
    private Drawable mIcon;
    private int mIconId;
    private int mId;
    private boolean mIsChecked;
    private boolean mIsEnable;
    private int mItemType;
    private int mRedDotAmount;
    private String mRedDotText;
    private boolean mSharedPressedStateEnabled;
    private Drawable mStateIcon;
    private int mStateIconId;
    private ArrayList<PopupListItem> mSubMenuItemList;
    private String mTitle;
    private ColorStateList mTitleColor;
    private String mTitleContentDescription;

    public static class Builder {
        private String mDescription;
        private String mDescriptionContentDescription;
        private String mRedDotText;
        private String mTitle;
        private String mTitleContentDescription;
        private int mId = -1;
        private int mIconId = 0;
        private int mGroupId = 0;
        private int mStateIconId = 0;
        private int mRedDotAmount = -1;
        private int mForceTint = 7;
        private int mItemType = 0;
        private int mHintType = -1;
        private boolean mIsEnable = true;
        private boolean mIsChecked = false;
        private Drawable mIcon = null;
        private Drawable mStateIcon = null;
        private ColorStateList mTitleColor = null;
        private ArrayList<PopupListItem> mSubMenuItemList = null;
        private View mCustomHintView = null;
        private View mCustomItemView = null;
        private boolean mSharedPressedStateEnabled = true;

        public static /* bridge */ /* synthetic */ View a(Builder builder) {
            return builder.mCustomHintView;
        }

        public static /* bridge */ /* synthetic */ View b(Builder builder) {
            return builder.mCustomItemView;
        }

        public static /* bridge */ /* synthetic */ String c(Builder builder) {
            return builder.mDescription;
        }

        public static /* bridge */ /* synthetic */ String d(Builder builder) {
            return builder.mDescriptionContentDescription;
        }

        public static /* bridge */ /* synthetic */ int e(Builder builder) {
            return builder.mForceTint;
        }

        public static /* bridge */ /* synthetic */ int f(Builder builder) {
            return builder.mGroupId;
        }

        public static /* bridge */ /* synthetic */ int g(Builder builder) {
            return builder.mHintType;
        }

        public static /* bridge */ /* synthetic */ Drawable h(Builder builder) {
            return builder.mIcon;
        }

        public static /* bridge */ /* synthetic */ int i(Builder builder) {
            return builder.mIconId;
        }

        public static /* bridge */ /* synthetic */ int j(Builder builder) {
            return builder.mId;
        }

        public static /* bridge */ /* synthetic */ boolean k(Builder builder) {
            return builder.mIsChecked;
        }

        public static /* bridge */ /* synthetic */ boolean l(Builder builder) {
            return builder.mIsEnable;
        }

        public static /* bridge */ /* synthetic */ int m(Builder builder) {
            return builder.mItemType;
        }

        public static /* bridge */ /* synthetic */ int n(Builder builder) {
            return builder.mRedDotAmount;
        }

        public static /* bridge */ /* synthetic */ String o(Builder builder) {
            return builder.mRedDotText;
        }

        public static /* bridge */ /* synthetic */ boolean p(Builder builder) {
            return builder.mSharedPressedStateEnabled;
        }

        public static /* bridge */ /* synthetic */ Drawable q(Builder builder) {
            return builder.mStateIcon;
        }

        public static /* bridge */ /* synthetic */ int r(Builder builder) {
            return builder.mStateIconId;
        }

        public static /* bridge */ /* synthetic */ ArrayList s(Builder builder) {
            return builder.mSubMenuItemList;
        }

        public static /* bridge */ /* synthetic */ String t(Builder builder) {
            return builder.mTitle;
        }

        public static /* bridge */ /* synthetic */ ColorStateList u(Builder builder) {
            return builder.mTitleColor;
        }

        public static /* bridge */ /* synthetic */ String v(Builder builder) {
            return builder.mTitleContentDescription;
        }

        public static /* bridge */ /* synthetic */ void w(Builder builder, View view) {
            builder.mCustomHintView = view;
        }

        public static /* bridge */ /* synthetic */ void x(Builder builder, ArrayList arrayList) {
            builder.mSubMenuItemList = arrayList;
        }

        public PopupListItem build() {
            PopupListItem popupListItem = new PopupListItem();
            PopupListItem.a(popupListItem, this);
            return popupListItem;
        }

        public Builder reset() {
            this.mId = -1;
            this.mIconId = 0;
            this.mIcon = null;
            this.mIsEnable = true;
            this.mTitle = null;
            this.mDescription = null;
            this.mItemType = 0;
            this.mTitleColor = null;
            this.mIsChecked = false;
            this.mStateIconId = 0;
            this.mStateIcon = null;
            this.mHintType = -1;
            this.mRedDotText = null;
            this.mRedDotAmount = -1;
            this.mForceTint = 7;
            this.mCustomHintView = null;
            this.mGroupId = 0;
            this.mSubMenuItemList = null;
            this.mCustomItemView = null;
            this.mSharedPressedStateEnabled = true;
            return this;
        }

        public Builder setCustomHintView(View view) {
            this.mCustomHintView = view;
            return this;
        }

        public Builder setCustomItemView(View view) {
            this.mCustomItemView = view;
            return this;
        }

        public Builder setDescription(String str) {
            this.mDescription = str;
            return this;
        }

        public Builder setDescriptionContentDescription(String str) {
            this.mDescriptionContentDescription = str;
            return this;
        }

        public Builder setForceTint(int i3) {
            this.mForceTint = i3;
            return this;
        }

        public Builder setGroupId(int i3) {
            this.mGroupId = i3;
            return this;
        }

        @Deprecated
        public Builder setHasSubArray(boolean z2) {
            return this;
        }

        public Builder setHintType(int i3) {
            this.mHintType = i3;
            return this;
        }

        public Builder setIcon(Drawable drawable) {
            this.mIcon = drawable;
            return this;
        }

        public Builder setIconId(int i3) {
            this.mIconId = i3;
            return this;
        }

        public Builder setId(int i3) {
            this.mId = i3;
            return this;
        }

        @Deprecated
        public Builder setIsCheckable(boolean z2) {
            return this;
        }

        public Builder setIsChecked(boolean z2) {
            this.mIsChecked = z2;
            return this;
        }

        @Deprecated
        public Builder setIsClickSubArray(boolean z2) {
            return this;
        }

        public Builder setIsEnable(boolean z2) {
            this.mIsEnable = z2;
            return this;
        }

        @Deprecated
        public Builder setItemColorSpecial(int i3) {
            return this;
        }

        public Builder setItemType(int i3) {
            this.mItemType = i3;
            return this;
        }

        @Deprecated
        public Builder setOperateIcon(Drawable drawable) {
            this.mStateIcon = drawable;
            return this;
        }

        public Builder setRedDotAmount(int i3) {
            this.mRedDotAmount = i3;
            return this;
        }

        public Builder setRedDotText(String str) {
            this.mRedDotText = str;
            return this;
        }

        public Builder setSharedPressedStateEnabled(boolean z2) {
            this.mSharedPressedStateEnabled = z2;
            return this;
        }

        public Builder setStateIcon(Drawable drawable) {
            this.mStateIcon = drawable;
            return this;
        }

        public Builder setStateIconId(int i3) {
            this.mStateIconId = i3;
            return this;
        }

        @Deprecated
        public Builder setSubArray(ArrayList<PopupListItem> arrayList) {
            this.mSubMenuItemList = arrayList;
            return this;
        }

        public Builder setSubMenuItemList(ArrayList<PopupListItem> arrayList) {
            this.mSubMenuItemList = arrayList;
            return this;
        }

        public Builder setTitle(String str) {
            this.mTitle = str;
            return this;
        }

        @Deprecated
        public Builder setTitleColorInt(int i3) {
            return this;
        }

        public Builder setTitleColorList(ColorStateList colorStateList) {
            this.mTitleColor = colorStateList;
            return this;
        }

        public Builder setTitleContentDescription(String str) {
            this.mTitleContentDescription = str;
            return this;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PopupMenuGroupState {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PopupMenuItemHintType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PopupMenuItemType {
    }

    public PopupListItem() {
        this.mId = -1;
        this.mGroupId = 0;
        this.mItemType = 0;
        this.mHintType = -1;
        this.mGroupState = 0;
        this.mForceTint = 7;
        this.mRedDotAmount = -1;
        this.mIconId = 0;
        this.mStateIconId = 0;
        this.mSharedPressedStateEnabled = true;
    }

    public static /* bridge */ /* synthetic */ void a(PopupListItem popupListItem, Builder builder) {
        popupListItem.apply(builder);
    }

    private void apply(Builder builder) {
        this.mId = Builder.j(builder);
        this.mIconId = Builder.i(builder);
        this.mIcon = Builder.h(builder);
        this.mIsEnable = Builder.l(builder);
        this.mTitle = Builder.t(builder);
        this.mTitleContentDescription = Builder.v(builder);
        this.mDescriptionContentDescription = Builder.d(builder);
        this.mDescription = Builder.c(builder);
        this.mItemType = Builder.m(builder);
        this.mIsChecked = Builder.k(builder);
        this.mStateIconId = Builder.r(builder);
        this.mStateIcon = Builder.q(builder);
        this.mHintType = Builder.g(builder);
        this.mRedDotText = Builder.o(builder);
        this.mRedDotAmount = Builder.n(builder);
        this.mForceTint = Builder.e(builder);
        ColorStateList colorStateListU = Builder.u(builder);
        this.mTitleColor = colorStateListU;
        if (colorStateListU != null) {
            this.mForceTint &= -3;
        }
        if (this.mHintType == 1) {
            this.mCustomHintView = Builder.a(builder);
            Builder.w(builder, null);
        }
        this.mGroupId = Builder.f(builder);
        if (Builder.s(builder) != null) {
            this.mSubMenuItemList = Builder.s(builder);
            Builder.x(builder, null);
        }
        this.mCustomItemView = Builder.b(builder);
        this.mSharedPressedStateEnabled = Builder.p(builder);
    }

    public View getCustomHintView() {
        return this.mCustomHintView;
    }

    public View getCustomItemView() {
        return this.mCustomItemView;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public String getDescriptionContentDescription() {
        return this.mDescriptionContentDescription;
    }

    public int getForceTint() {
        return this.mForceTint;
    }

    public int getGroupId() {
        return this.mGroupId;
    }

    public int getGroupState() {
        return this.mGroupState;
    }

    public int getHintType() {
        return this.mHintType;
    }

    public Drawable getIcon() {
        return this.mIcon;
    }

    public int getIconId() {
        return this.mIconId;
    }

    public int getId() {
        return this.mId;
    }

    @Deprecated
    public int getItemColorSpecial() {
        return -1;
    }

    public int getItemType() {
        return this.mItemType;
    }

    @Deprecated
    public Drawable getOperateIcon() {
        return this.mStateIcon;
    }

    public int getRedDotAmount() {
        return this.mRedDotAmount;
    }

    public String getRedDotText() {
        return this.mRedDotText;
    }

    public Drawable getStateIcon() {
        return this.mStateIcon;
    }

    public int getStateIconId() {
        return this.mStateIconId;
    }

    @Deprecated
    public ArrayList<PopupListItem> getSubArray() {
        return this.mSubMenuItemList;
    }

    public ArrayList<PopupListItem> getSubMenuItemList() {
        return this.mSubMenuItemList;
    }

    public String getTitle() {
        return this.mTitle;
    }

    @Deprecated
    public int getTitleColorInt() {
        return -1;
    }

    public ColorStateList getTitleColorList() {
        return this.mTitleColor;
    }

    public String getTitleContentDescription() {
        return this.mTitleContentDescription;
    }

    @Deprecated
    public boolean hasSubArray() {
        return hasSubMenu();
    }

    public boolean hasSubMenu() {
        ArrayList<PopupListItem> arrayList = this.mSubMenuItemList;
        return (arrayList == null || arrayList.isEmpty()) ? false : true;
    }

    @Deprecated
    public boolean isCheckable() {
        return true;
    }

    public boolean isChecked() {
        return this.mIsChecked;
    }

    public boolean isEnable() {
        return this.mIsEnable;
    }

    public boolean isSharedPressedStateEnabled() {
        return this.mSharedPressedStateEnabled;
    }

    @Deprecated
    public void setCheckable(boolean z2) {
    }

    public void setChecked(boolean z2) {
        this.mIsChecked = z2;
    }

    public void setCustomHintView(View view) {
        this.mCustomHintView = view;
    }

    public void setCustomItemView(View view) {
        this.mCustomItemView = view;
    }

    public void setDescription(String str) {
        this.mDescription = str;
    }

    public void setDescriptionContentDescription(String str) {
        this.mTitleContentDescription = str;
    }

    public void setEnable(boolean z2) {
        this.mIsEnable = z2;
    }

    public void setForceTint(int i3) {
        this.mForceTint = i3;
    }

    public void setGroupId(int i3) {
        this.mGroupId = i3;
    }

    public void setGroupState(int i3) {
        this.mGroupState = i3;
    }

    @Deprecated
    public void setHasSubArray(boolean z2) {
    }

    public void setHintType(int i3) {
        this.mHintType = i3;
    }

    public void setIcon(Drawable drawable) {
        this.mIcon = drawable;
    }

    public void setIconId(int i3) {
        this.mIconId = i3;
    }

    public void setId(int i3) {
        this.mId = i3;
    }

    @Deprecated
    public void setItemColorSpecial(int i3) {
    }

    public void setItemType(int i3) {
        this.mItemType = i3;
    }

    @Deprecated
    public void setOperateIcon(Drawable drawable) {
        this.mStateIcon = drawable;
    }

    public void setRedDotAmount(int i3) {
        this.mRedDotAmount = i3;
    }

    public void setRedDotText(String str) {
        this.mRedDotText = str;
    }

    public void setSharedPressedStateEnabled(boolean z2) {
        this.mSharedPressedStateEnabled = z2;
    }

    public void setStateIcon(Drawable drawable) {
        this.mStateIcon = drawable;
    }

    public void setStateIconId(int i3) {
        this.mStateIconId = i3;
    }

    @Deprecated
    public void setSubArray(ArrayList<PopupListItem> arrayList) {
        this.mSubMenuItemList = arrayList;
    }

    public void setSubMenuItemList(ArrayList<PopupListItem> arrayList) {
        this.mSubMenuItemList = arrayList;
    }

    public void setTitle(String str) {
        this.mTitle = str;
    }

    @Deprecated
    public void setTitleColorInt(int i3) {
    }

    public void setTitleColorList(ColorStateList colorStateList) {
        this.mTitleColor = colorStateList;
    }

    public void setTitleContentDescription(String str) {
        this.mTitleContentDescription = str;
    }

    @Deprecated
    public PopupListItem(String str, boolean z2) {
        this((Drawable) null, str, z2);
    }

    @Deprecated
    public PopupListItem(int i3, String str, boolean z2) {
        this.mId = -1;
        this.mGroupId = 0;
        this.mItemType = 0;
        this.mHintType = -1;
        this.mGroupState = 0;
        this.mForceTint = 7;
        this.mRedDotAmount = -1;
        this.mStateIconId = 0;
        this.mSharedPressedStateEnabled = true;
        this.mIconId = i3;
        this.mTitle = str;
        this.mIsEnable = z2;
    }

    @Deprecated
    public PopupListItem(String str, boolean z2, int i3) {
        this((Drawable) null, str, z2);
        this.mGroupId = i3;
    }

    @Deprecated
    public PopupListItem(Drawable drawable, String str, boolean z2) {
        this(drawable, str, z2, -1);
    }

    @Deprecated
    public PopupListItem(Drawable drawable, String str, boolean z2, int i3) {
        this(drawable, str, false, false, i3, z2);
    }

    @Deprecated
    public PopupListItem(Drawable drawable, String str, boolean z2, boolean z3) {
        this(drawable, str, z2, false, z3);
    }

    @Deprecated
    public PopupListItem(Drawable drawable, String str, boolean z2, boolean z3, boolean z4) {
        this(drawable, str, z2, z3, -1, z4);
    }

    @Deprecated
    public PopupListItem(Drawable drawable, String str, boolean z2, boolean z3, int i3, boolean z4) {
        this(drawable, str, z2, z3, i3, z4, null);
    }

    @Deprecated
    public PopupListItem(Drawable drawable, String str, boolean z2, boolean z3, int i3, boolean z4, ArrayList<PopupListItem> arrayList) {
        this(drawable, str, z2, z3, i3, z4, arrayList, null);
    }

    @Deprecated
    public PopupListItem(Drawable drawable, String str, boolean z2, boolean z3, int i3, boolean z4, ArrayList<PopupListItem> arrayList, String str2) {
        this(drawable, str, z2, z3, i3, z4, arrayList, str2, null);
    }

    @Deprecated
    public PopupListItem(Drawable drawable, String str, boolean z2, boolean z3, int i3, boolean z4, ArrayList<PopupListItem> arrayList, String str2, Drawable drawable2) {
        this(drawable, str, z2, z3, i3, z4, arrayList, str2, drawable2, -1);
    }

    @Deprecated
    public PopupListItem(Drawable drawable, String str, boolean z2, boolean z3, int i3, boolean z4, ArrayList<PopupListItem> arrayList, String str2, Drawable drawable2, int i4) {
        this(drawable, str, z2, z3, i3, z4, arrayList, str2, drawable2, i4, -1);
    }

    @Deprecated
    public PopupListItem(Drawable drawable, String str, boolean z2, boolean z3, int i3, boolean z4, ArrayList<PopupListItem> arrayList, String str2, Drawable drawable2, int i4, int i5) {
        this.mId = -1;
        this.mItemType = 0;
        this.mHintType = -1;
        this.mGroupState = 0;
        this.mForceTint = 7;
        this.mIconId = 0;
        this.mStateIconId = 0;
        this.mSharedPressedStateEnabled = true;
        this.mIcon = drawable;
        this.mTitle = str;
        this.mIsChecked = z3;
        this.mIsEnable = z4;
        this.mRedDotAmount = i3;
        this.mSubMenuItemList = arrayList;
        this.mRedDotText = str2;
        this.mStateIcon = drawable2;
        this.mGroupId = i5;
    }
}