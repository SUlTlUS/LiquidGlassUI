package com.coui.appcompat.theme;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.app.AppCompatViewInflater;
import androidx.appcompat.widget.AppCompatTextView;
import com.coui.appcompat.textview.COUITextView;

/* JADX INFO: loaded from: classes.dex */
public class COUIComponentsViewInflater extends AppCompatViewInflater {
    @Override // androidx.appcompat.app.AppCompatViewInflater
    public AppCompatTextView createTextView(Context context, AttributeSet attributeSet) {
        return new COUITextView(context, attributeSet);
    }
}