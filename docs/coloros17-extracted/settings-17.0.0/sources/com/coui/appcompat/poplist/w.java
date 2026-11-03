package com.coui.appcompat.poplist;

import android.view.MotionEvent;
import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class w implements View.OnTouchListener {
    @Override // android.view.View.OnTouchListener
    public final boolean onTouch(View view, MotionEvent motionEvent) {
        return DefaultAdapter.a(view, motionEvent);
    }
}