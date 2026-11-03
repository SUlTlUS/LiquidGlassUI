package com.coui.appcompat.checkbox;

import android.graphics.drawable.Drawable;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class a implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUICheckBox f13314a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ Drawable f13315b;

    public /* synthetic */ a(COUICheckBox cOUICheckBox, Drawable drawable) {
        this.f13314a = cOUICheckBox;
        this.f13315b = drawable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUICheckBox.LoadDrawableRunnable.a(this.f13314a, this.f13315b);
    }
}