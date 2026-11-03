package com.coui.appcompat.dialog;

/* JADX INFO: loaded from: classes7.dex */
public class COUIGuidePageItem {
    private final CharSequence mDescription;
    private final int mImageResId;
    private final CharSequence mTitle;

    public COUIGuidePageItem(int i3, CharSequence charSequence, CharSequence charSequence2) {
        this.mImageResId = i3;
        this.mTitle = charSequence;
        this.mDescription = charSequence2;
    }

    public CharSequence getDescription() {
        return this.mDescription;
    }

    public int getImageResId() {
        return this.mImageResId;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }
}