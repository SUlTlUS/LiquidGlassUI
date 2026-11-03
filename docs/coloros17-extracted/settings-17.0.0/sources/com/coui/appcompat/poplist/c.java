package com.coui.appcompat.poplist;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class c implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIClickSelectMenu.AnonymousClass1 f13415a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ View f13416b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ int f13417c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ int f13418d;

    public /* synthetic */ c(COUIClickSelectMenu.AnonymousClass1 anonymousClass1, View view, int i3, int i4) {
        this.f13415a = anonymousClass1;
        this.f13416b = view;
        this.f13417c = i3;
        this.f13418d = i4;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUIClickSelectMenu.AnonymousClass1.a(this.f13415a, this.f13416b, this.f13417c, this.f13418d);
    }
}