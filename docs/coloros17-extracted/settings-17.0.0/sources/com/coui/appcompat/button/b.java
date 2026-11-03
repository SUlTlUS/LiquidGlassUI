package com.coui.appcompat.button;

import android.widget.TextView;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class b implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIButton f13295a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ CharSequence f13296b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ TextView.BufferType f13297c;

    public /* synthetic */ b(COUIButton cOUIButton, CharSequence charSequence, TextView.BufferType bufferType) {
        this.f13295a = cOUIButton;
        this.f13296b = charSequence;
        this.f13297c = bufferType;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUIButton.a(this.f13295a, this.f13296b, this.f13297c);
    }
}