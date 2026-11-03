package com.coui.appcompat.poplist;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class f implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIPopupListWindow f13428a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ View f13429b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ int f13430c;

    public /* synthetic */ f(COUIPopupListWindow cOUIPopupListWindow, View view, int i3) {
        this.f13428a = cOUIPopupListWindow;
        this.f13429b = view;
        this.f13430c = i3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUIPopupListWindow.h(this.f13428a, this.f13429b, this.f13430c);
    }
}