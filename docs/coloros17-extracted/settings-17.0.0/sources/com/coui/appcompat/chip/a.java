package com.coui.appcompat.chip;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class a implements COUICheckable.OnCheckedChangeListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUICheckableGroup f13316a;

    public /* synthetic */ a(COUICheckableGroup cOUICheckableGroup) {
        this.f13316a = cOUICheckableGroup;
    }

    @Override // com.coui.appcompat.chip.COUICheckable.OnCheckedChangeListener
    public final void onCheckedChanged(Object obj, boolean z2) {
        COUICheckableGroup.a(this.f13316a, (COUICheckable) obj, z2);
    }
}