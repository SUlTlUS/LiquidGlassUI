package com.coui.appcompat.preference;

import android.view.MotionEvent;
import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class a implements View.OnTouchListener {
    @Override // android.view.View.OnTouchListener
    public final boolean onTouch(View view, MotionEvent motionEvent) {
        return COUICheckBoxPreference.c(view, motionEvent);
    }
}