package com.coui.appcompat.poplist;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class d implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIPopupListWindow f13420a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ View f13421b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ int f13422c;

    public /* synthetic */ d(COUIPopupListWindow cOUIPopupListWindow, View view, int i3) {
        this.f13420a = cOUIPopupListWindow;
        this.f13421b = view;
        this.f13422c = i3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUIPopupListWindow.g(this.f13420a, this.f13421b, this.f13422c);
    }
}