package com.coui.appcompat.chip;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class u implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIChipTabGroupFragmentMediator f13344a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ View f13345b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ int f13346c;

    public /* synthetic */ u(COUIChipTabGroupFragmentMediator cOUIChipTabGroupFragmentMediator, View view, int i3) {
        this.f13344a = cOUIChipTabGroupFragmentMediator;
        this.f13345b = view;
        this.f13346c = i3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUIChipTabGroupFragmentMediator$startFadeInAnimationForFragment$startAnimation$1.a(this.f13344a, this.f13345b, this.f13346c);
    }
}