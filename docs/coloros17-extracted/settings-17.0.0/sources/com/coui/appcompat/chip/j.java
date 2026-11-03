package com.coui.appcompat.chip;

import android.view.KeyEvent;
import android.widget.TextView;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class j implements TextView.OnEditorActionListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIChipGroupEditText f13325a;

    public /* synthetic */ j(COUIChipGroupEditText cOUIChipGroupEditText) {
        this.f13325a = cOUIChipGroupEditText;
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public final boolean onEditorAction(TextView textView, int i3, KeyEvent keyEvent) {
        return COUIChipGroupEditText.a(this.f13325a, textView, i3, keyEvent);
    }
}