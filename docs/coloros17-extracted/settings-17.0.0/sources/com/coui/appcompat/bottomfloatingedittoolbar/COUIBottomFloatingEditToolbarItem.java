package com.coui.appcompat.bottomfloatingedittoolbar;

import android.graphics.drawable.Drawable;
import com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingItem;

/* JADX INFO: loaded from: classes7.dex */
public class COUIBottomFloatingEditToolbarItem extends BaseBottomFloatingItem {

    public static class Builder extends BaseBottomFloatingItem.BaseBuilder<Builder> {
        @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingItem.BaseBuilder
        public /* bridge */ /* synthetic */ BaseBottomFloatingItem build() {
            return build();
        }

        @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingItem.BaseBuilder
        public /* bridge */ /* synthetic */ BaseBottomFloatingItem.BaseBuilder reset() {
            return super.reset();
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
        public COUIBottomFloatingEditToolbarItem build() {
            COUIBottomFloatingEditToolbarItem cOUIBottomFloatingEditToolbarItem = new COUIBottomFloatingEditToolbarItem();
            COUIBottomFloatingEditToolbarItem.access$000(cOUIBottomFloatingEditToolbarItem, this);
            return cOUIBottomFloatingEditToolbarItem;
        }
    }

    public static /* synthetic */ void access$000(COUIBottomFloatingEditToolbarItem cOUIBottomFloatingEditToolbarItem, Builder builder) {
        cOUIBottomFloatingEditToolbarItem.apply(builder);
    }

    private void apply(Builder builder) {
        applyBaseProperties(builder);
    }

    @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingItem
    public String toString() {
        return "COUIBottomFloatingEditToolbarItem{mDescription=" + ((Object) this.mDescription) + ", mId=" + this.mId + ", mIcon=" + this.mIcon + ", mTitle='" + ((Object) this.mTitle) + "', mRedDotType=" + this.mRedDotType + ", mRedDotNumber=" + this.mRedDotNumber + '}';
    }
}