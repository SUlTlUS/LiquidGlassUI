package com.coui.appcompat.bottomfloatingnavigationbar;

import android.graphics.drawable.Drawable;

/* JADX INFO: loaded from: classes7.dex */
public class COUIBottomFloatingNavigationBarItem extends BaseBottomFloatingItem {
    private boolean mIsLargeIcon = false;

    public static class Builder extends BaseBottomFloatingItem.BaseBuilder<Builder> {
        private boolean mIsLargeIcon = false;

        public static /* synthetic */ boolean access$000(Builder builder) {
            return builder.mIsLargeIcon;
        }

        @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingItem.BaseBuilder
        public /* bridge */ /* synthetic */ BaseBottomFloatingItem build() {
            return build();
        }

        @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingItem.BaseBuilder
        public /* bridge */ /* synthetic */ BaseBottomFloatingItem.BaseBuilder reset() {
            return reset();
        }

        @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingItem.BaseBuilder
        public /* bridge */ /* synthetic */ BaseBottomFloatingItem.BaseBuilder setDescription(CharSequence charSequence) {
            return super.setDescription(charSequence);
        }

        @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingItem.BaseBuilder
        public /* bridge */ /* synthetic */ BaseBottomFloatingItem.BaseBuilder setIcon(Drawable drawable) {
            return super.setIcon(drawable);
        }

        @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingItem.BaseBuilder
        public /* bridge */ /* synthetic */ BaseBottomFloatingItem.BaseBuilder setIconResId(int i3) {
            return super.setIconResId(i3);
        }

        @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingItem.BaseBuilder
        public /* bridge */ /* synthetic */ BaseBottomFloatingItem.BaseBuilder setId(int i3) {
            return super.setId(i3);
        }

        public Builder setLargeIcon(boolean z2) {
            this.mIsLargeIcon = z2;
            return this;
        }

        @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingItem.BaseBuilder
        public /* bridge */ /* synthetic */ BaseBottomFloatingItem.BaseBuilder setRedDotNumber(int i3) {
            return super.setRedDotNumber(i3);
        }

        @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingItem.BaseBuilder
        public /* bridge */ /* synthetic */ BaseBottomFloatingItem.BaseBuilder setRedDotType(int i3) {
            return super.setRedDotType(i3);
        }

        @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingItem.BaseBuilder
        public /* bridge */ /* synthetic */ BaseBottomFloatingItem.BaseBuilder setTitle(String str) {
            return super.setTitle(str);
        }

        @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingItem.BaseBuilder
        public COUIBottomFloatingNavigationBarItem build() {
            COUIBottomFloatingNavigationBarItem cOUIBottomFloatingNavigationBarItem = new COUIBottomFloatingNavigationBarItem();
            COUIBottomFloatingNavigationBarItem.access$100(cOUIBottomFloatingNavigationBarItem, this);
            return cOUIBottomFloatingNavigationBarItem;
        }

        @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingItem.BaseBuilder
        public Builder reset() {
            super.reset();
            this.mIsLargeIcon = false;
            return this;
        }
    }

    public static /* synthetic */ void access$100(COUIBottomFloatingNavigationBarItem cOUIBottomFloatingNavigationBarItem, Builder builder) {
        cOUIBottomFloatingNavigationBarItem.apply(builder);
    }

    private void apply(Builder builder) {
        applyBaseProperties(builder);
        this.mIsLargeIcon = Builder.access$000(builder);
    }

    public boolean isLargeIcon() {
        return this.mIsLargeIcon;
    }

    @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingItem
    public String toString() {
        return "COUIBottomFloatingNavigationBarItem{mDescription=" + ((Object) this.mDescription) + ", mId=" + this.mId + ", mIcon=" + this.mIcon + ", mTitle='" + ((Object) this.mTitle) + "', mIsLargeIcon=" + this.mIsLargeIcon + '}';
    }
}