package com.coui.appcompat.card;

import android.widget.TextView;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class b implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUICardButtonPreference f13307a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ TextView f13308b;

    public /* synthetic */ b(COUICardButtonPreference cOUICardButtonPreference, TextView textView) {
        this.f13307a = cOUICardButtonPreference;
        this.f13308b = textView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUICardButtonPreference.d(this.f13307a, this.f13308b);
    }
}