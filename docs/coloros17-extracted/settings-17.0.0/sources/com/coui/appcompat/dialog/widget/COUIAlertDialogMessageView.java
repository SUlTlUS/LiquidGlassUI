package com.coui.appcompat.dialog.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import androidx.appcompat.widget.AppCompatTextView;

/* JADX INFO: loaded from: classes7.dex */
public class COUIAlertDialogMessageView extends AppCompatTextView implements ViewTreeObserver.OnGlobalLayoutListener {
    public COUIAlertDialogMessageView(Context context) {
        super(context);
    }

    @Override // android.widget.TextView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override // androidx.appcompat.widget.AppCompatTextView, android.view.View
    public void onDetachedFromWindow() {
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
        super.onDetachedFromWindow();
    }

    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
    public void onGlobalLayout() {
        if (getLineCount() > 1) {
            setTextAlignment(5);
            setGravity(8388611);
        } else {
            setTextAlignment(4);
            setGravity(17);
        }
    }

    public COUIAlertDialogMessageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public COUIAlertDialogMessageView(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
    }
}