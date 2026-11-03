package com.coui.appcompat.poplist;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class o implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIPopupListWindow.AnonymousClass2 f13442a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ View f13443b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ int f13444c;

    public /* synthetic */ o(COUIPopupListWindow.AnonymousClass2 anonymousClass2, View view, int i3) {
        this.f13442a = anonymousClass2;
        this.f13443b = view;
        this.f13444c = i3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUIPopupListWindow.AnonymousClass2.a(this.f13442a, this.f13443b, this.f13444c);
    }
}