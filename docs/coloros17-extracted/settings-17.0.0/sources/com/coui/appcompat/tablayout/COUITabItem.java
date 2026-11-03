package com.coui.appcompat.tablayout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.widget.TintTypedArray;
import com.support.tablayout.R;

/* JADX INFO: loaded from: classes7.dex */
public final class COUITabItem extends View {
    final int mCustomLayout;
    final Drawable mIcon;
    final CharSequence mText;

    public COUITabItem(Context context) {
        this(context, null);
    }

    public COUITabItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TintTypedArray tintTypedArrayObtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, R.styleable.COUITabItem);
        this.mText = tintTypedArrayObtainStyledAttributes.getText(R.styleable.COUITabItem_android_text);
        this.mIcon = tintTypedArrayObtainStyledAttributes.getDrawable(R.styleable.COUITabItem_android_icon);
        this.mCustomLayout = tintTypedArrayObtainStyledAttributes.getResourceId(R.styleable.COUITabItem_android_layout, 0);
        tintTypedArrayObtainStyledAttributes.recycle();
    }
}