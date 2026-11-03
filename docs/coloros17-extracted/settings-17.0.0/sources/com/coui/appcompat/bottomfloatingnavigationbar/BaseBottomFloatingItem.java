package com.coui.appcompat.bottomfloatingnavigationbar;

import android.graphics.drawable.Drawable;

/* JADX INFO: loaded from: classes7.dex */
public abstract class BaseBottomFloatingItem {
    public static final int RED_DOT_NONE = 0;
    public static final int RED_DOT_NUMBER = 2;
    public static final int RED_DOT_POINT = 1;
    protected Drawable mIcon;
    protected CharSequence mTitle;
    protected int mId = -1;
    protected CharSequence mDescription = "";
    protected int mRedDotType = 0;
    protected int mRedDotNumber = 0;
    protected int mIconResId = 0;

    public static abstract class BaseBuilder<T extends BaseBuilder<T>> {
        protected CharSequence mDescription;
        protected Drawable mIcon;
        protected CharSequence mTitle;
        protected int mId = -1;
        protected int mRedDotType = 0;
        protected int mRedDotNumber = 0;
        protected int mIconResId = 0;

        public abstract BaseBottomFloatingItem build();

        public T reset() {
            this.mId = -1;
            this.mIcon = null;
            this.mTitle = "";
            this.mDescription = "";
            this.mRedDotType = 0;
            this.mRedDotNumber = 0;
            this.mIconResId = 0;
            return (T) self();
        }

        public T self() {
            return this;
        }

        public T setDescription(CharSequence charSequence) {
            this.mDescription = charSequence;
            return (T) self();
        }

        public T setIcon(Drawable drawable) {
            this.mIcon = drawable;
            return (T) self();
        }

        public T setIconResId(int i3) {
            this.mIconResId = i3;
            return (T) self();
        }

        public T setId(int i3) {
            this.mId = i3;
            return (T) self();
        }

        public T setRedDotNumber(int i3) {
            this.mRedDotNumber = i3;
            return (T) self();
        }

        public T setRedDotType(int i3) {
            this.mRedDotType = i3;
            return (T) self();
        }

        public T setTitle(String str) {
            this.mTitle = str;
            return (T) self();
        }
    }

    public void applyBaseProperties(BaseBuilder<?> baseBuilder) {
        this.mId = baseBuilder.mId;
        this.mIcon = baseBuilder.mIcon;
        this.mTitle = baseBuilder.mTitle;
        this.mDescription = baseBuilder.mDescription;
        this.mRedDotType = baseBuilder.mRedDotType;
        this.mRedDotNumber = baseBuilder.mRedDotNumber;
        this.mIconResId = baseBuilder.mIconResId;
    }

    public CharSequence getDescription() {
        return this.mDescription;
    }

    public Drawable getIcon() {
        return this.mIcon;
    }

    public int getIconResId() {
        return this.mIconResId;
    }

    public int getId() {
        return this.mId;
    }

    public int getRedDotNumber() {
        return this.mRedDotNumber;
    }

    public int getRedDotType() {
        return this.mRedDotType;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public String toString() {
        return getClass().getSimpleName() + "{mDescription=" + ((Object) this.mDescription) + ", mId=" + this.mId + ", mIcon=" + this.mIcon + ", mTitle='" + ((Object) this.mTitle) + "', mRedDotType=" + this.mRedDotType + ", mRedDotNumber=" + this.mRedDotNumber + '}';
    }
}