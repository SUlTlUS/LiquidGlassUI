package com.coui.appcompat.stepper;

import android.view.MotionEvent;
import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class d implements View.OnTouchListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ LongPressProxy f13593a;

    public /* synthetic */ d(LongPressProxy longPressProxy) {
        this.f13593a = longPressProxy;
    }

    @Override // android.view.View.OnTouchListener
    public final boolean onTouch(View view, MotionEvent motionEvent) {
        return LongPressProxy.a(this.f13593a, view, motionEvent);
    }
}