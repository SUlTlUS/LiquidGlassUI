package com.coui.appcompat.chip;

import android.widget.CompoundButton;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class b implements CompoundButton.OnCheckedChangeListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIChip f13317a;

    public /* synthetic */ b(COUIChip cOUIChip) {
        this.f13317a = cOUIChip;
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public final void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
        COUIChip.a(this.f13317a, compoundButton, z2);
    }
}