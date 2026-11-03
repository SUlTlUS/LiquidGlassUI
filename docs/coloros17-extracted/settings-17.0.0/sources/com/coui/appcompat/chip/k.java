package com.coui.appcompat.chip;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class k implements COUIChipTabDrawable.OnCheckedLayerAlphaChangeListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIChipTab f13326a;

    public /* synthetic */ k(COUIChipTab cOUIChipTab) {
        this.f13326a = cOUIChipTab;
    }

    @Override // com.coui.appcompat.chip.COUIChipTabDrawable.OnCheckedLayerAlphaChangeListener
    public final void onAlphaChanged(float f3) {
        COUIChipTab.b(this.f13326a, f3);
    }
}