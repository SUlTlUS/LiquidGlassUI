package com.coui.appcompat.dialog;

import android.graphics.Point;
import android.view.View;
import android.view.Window;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class d implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ Window f13355a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ View f13356b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ Point f13357c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ Point f13358d;

    public /* synthetic */ d(Window window, View view, Point point, Point point2) {
        this.f13355a = window;
        this.f13356b = view;
        this.f13357c = point;
        this.f13358d = point2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUIBottomAlertDialogAdjustUtil.AnonymousClass1.ViewOnLayoutChangeListenerC04511.a(this.f13355a, this.f13356b, this.f13357c, this.f13358d);
    }
}