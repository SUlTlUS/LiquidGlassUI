package com.coui.appcompat.chip;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class q implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIChipTabGroupFragmentMediator f13337a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ View f13338b;

    public /* synthetic */ q(COUIChipTabGroupFragmentMediator cOUIChipTabGroupFragmentMediator, View view) {
        this.f13337a = cOUIChipTabGroupFragmentMediator;
        this.f13338b = view;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUIChipTabGroupFragmentMediator.b(this.f13337a, this.f13338b);
    }
}