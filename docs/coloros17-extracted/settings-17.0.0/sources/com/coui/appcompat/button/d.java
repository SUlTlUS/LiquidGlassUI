package com.coui.appcompat.button;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class d implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ SimpleButtonGroupCtrl f13300a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ int f13301b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ COUIButton f13302c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ int f13303d;

    public /* synthetic */ d(SimpleButtonGroupCtrl simpleButtonGroupCtrl, int i3, COUIButton cOUIButton, int i4) {
        this.f13300a = simpleButtonGroupCtrl;
        this.f13301b = i3;
        this.f13302c = cOUIButton;
        this.f13303d = i4;
    }

    @Override // java.lang.Runnable
    public final void run() {
        SimpleButtonGroupCtrl.a(this.f13300a, this.f13301b, this.f13302c, this.f13303d);
    }
}