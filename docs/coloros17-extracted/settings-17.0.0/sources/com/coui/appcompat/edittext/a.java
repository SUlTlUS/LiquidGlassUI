package com.coui.appcompat.edittext;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class a implements View.OnLayoutChangeListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIInputView f13365a;

    public /* synthetic */ a(COUIInputView cOUIInputView) {
        this.f13365a = cOUIInputView;
    }

    @Override // android.view.View.OnLayoutChangeListener
    public final void onLayoutChange(View view, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        COUIInputView.a(this.f13365a, view, i3, i4, i5, i6, i7, i8, i9, i10);
    }
}