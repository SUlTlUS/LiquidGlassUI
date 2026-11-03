package com.coui.appcompat.poplist;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class d0 implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ RegionBasedLongPressDetector f13423a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ View f13424b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ int f13425c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ boolean f13426d;

    public /* synthetic */ d0(RegionBasedLongPressDetector regionBasedLongPressDetector, View view, int i3, boolean z2) {
        this.f13423a = regionBasedLongPressDetector;
        this.f13424b = view;
        this.f13425c = i3;
        this.f13426d = z2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        RegionBasedLongPressDetector.a(this.f13423a, this.f13424b, this.f13425c, this.f13426d);
    }
}