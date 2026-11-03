package com.coui.appcompat.panel;

import android.view.MotionEvent;
import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class d implements View.OnTouchListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIPanelContentLayout f13394a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ View f13395b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ boolean f13396c;

    public /* synthetic */ d(COUIPanelContentLayout cOUIPanelContentLayout, View view, boolean z2) {
        this.f13394a = cOUIPanelContentLayout;
        this.f13395b = view;
        this.f13396c = z2;
    }

    @Override // android.view.View.OnTouchListener
    public final boolean onTouch(View view, MotionEvent motionEvent) {
        return COUIPanelContentLayout.a(this.f13394a, this.f13395b, this.f13396c, view, motionEvent);
    }
}