package com.coui.appcompat.bottomfloatingtoolbar;

import android.graphics.drawable.Drawable;
import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public class COUIBottomFloatingToolbarItem {
    private Drawable mIcon;
    private CharSequence mTitle;
    private View mView;
    private int mId = -1;
    private int mGroupId = 0;
    private CharSequence mDescription = "";
    private boolean mShowRedDot = false;
    private boolean mEnabled = false;

    public static class Builder {
        private CharSequence mDescription;
        private Drawable mIcon;
        private CharSequence mTitle;
        private View mView;
        private int mId = -1;
        private int mGroupId = 0;
        private boolean mShowRedDot = false;
        private boolean mEnabled = true;

        public static /* synthetic */ int access$000(Builder builder) {
            return builder.mId;
        }

        public static /* synthetic */ int access$100(Builder builder) {
            return builder.mGroupId;
        }

        public static /* synthetic */ Drawable access$200(Builder builder) {
            return builder.mIcon;
        }

        public static /* synthetic */ CharSequence access$300(Builder builder) {
            return builder.mTitle;
        }

        public static /* synthetic */ CharSequence access$400(Builder builder) {
            return builder.mDescription;
        }

        public static /* synthetic */ boolean access$500(Builder builder) {
            return builder.mShowRedDot;
        }

        public static /* synthetic */ boolean access$600(Builder builder) {
            return builder.mEnabled;
        }

        public static /* synthetic */ View access$700(Builder builder) {
            return builder.mView;
        }

        public COUIBottomFloatingToolbarItem build() {
            COUIBottomFloatingToolbarItem cOUIBottomFloatingToolbarItem = new COUIBottomFloatingToolbarItem();
            COUIBottomFloatingToolbarItem.access$800(cOUIBottomFloatingToolbarItem, this);
            return cOUIBottomFloatingToolbarItem;
        }

        public Builder reset() {
            this.mId = -1;
            this.mGroupId = 0;
            this.mIcon = null;
            this.mTitle = "";
            this.mDescription = "";
            this.mShowRedDot = false;
            this.mEnabled = true;
            this.mView = null;
            return this;
        }

        public Builder setCustomView(View view) {
            this.mView = view;
            return this;
        }

        public Builder setDescription(CharSequence charSequence) {
            this.mDescription = charSequence;
            return this;
        }

        public Builder setEnabled(boolean z2) {
            this.mEnabled = z2;
            return this;
        }

        public Builder setGroupId(int i3) {
            this.mGroupId = i3;
            return this;
        }

        public Builder setIcon(Drawable drawable) {
            this.mIcon = drawable;
            return this;
        }

        public Builder setId(int i3) {
            this.mId = i3;
            return this;
        }

        public Builder setShowRedDot(boolean z2) {
            this.mShowRedDot = z2;
            return this;
        }

        public Builder setTitle(String str) {
            this.mTitle = str;
            return this;
        }
    }

    public static /* synthetic */ void access$800(COUIBottomFloatingToolbarItem cOUIBottomFloatingToolbarItem, Builder builder) {
        cOUIBottomFloatingToolbarItem.apply(builder);
    }

    private void apply(Builder builder) {
        this.mId = Builder.access$000(builder);
        this.mGroupId = Builder.access$100(builder);
        this.mIcon = Builder.access$200(builder);
        this.mTitle = Builder.access$300(builder);
        this.mDescription = Builder.access$400(builder);
        this.mShowRedDot = Builder.access$500(builder);
        this.mEnabled = Builder.access$600(builder);
        View viewAccess$700 = Builder.access$700(builder);
        this.mView = viewAccess$700;
        if (viewAccess$700 != null) {
            viewAccess$700.setId(Builder.access$000(builder));
        }
    }

    public CharSequence getDescription() {
        return this.mDescription;
    }

    public int getGroupId() {
        return this.mGroupId;
    }

    public Drawable getIcon() {
        return this.mIcon;
    }

    public int getId() {
        return this.mId;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public View getView() {
        View view = this.mView;
        if (view != null) {
            view.setId(this.mId);
        }
        return this.mView;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public boolean isShowRedDot() {
        return this.mShowRedDot;
    }

    public void setDescription(CharSequence charSequence) {
        this.mDescription = charSequence;
    }

    public void setEnabled(boolean z2) {
        this.mEnabled = z2;
    }

    public void setGroupId(int i3) {
        this.mGroupId = i3;
    }

    public void setIcon(Drawable drawable) {
        this.mIcon = drawable;
    }

    public void setId(int i3) {
        this.mId = i3;
    }

    public void setShowRedDot(boolean z2) {
        this.mShowRedDot = z2;
    }

    public void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
    }

    public void setView(View view) {
        this.mView = view;
    }

    public String toString() {
        return "COUIBottomFloatingToolbarItem{mDescription=" + ((Object) this.mDescription) + ", mId=" + this.mId + ", mGroupId=" + this.mGroupId + ", mIcon=" + this.mIcon + ", mTitle='" + ((Object) this.mTitle) + "', mShowRedDot=" + this.mShowRedDot + ", mEnabled=" + this.mEnabled + ", mView=" + this.mView + '}';
    }
}