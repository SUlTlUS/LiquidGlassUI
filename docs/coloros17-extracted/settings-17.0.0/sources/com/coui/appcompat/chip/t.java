package com.coui.appcompat.chip;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class t implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIChipTabGroupFragmentMediator f13342a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ View f13343b;

    public /* synthetic */ t(COUIChipTabGroupFragmentMediator cOUIChipTabGroupFragmentMediator, View view) {
        this.f13342a = cOUIChipTabGroupFragmentMediator;
        this.f13343b = view;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUIChipTabGroupFragmentMediator.a(this.f13342a, this.f13343b);
    }
}