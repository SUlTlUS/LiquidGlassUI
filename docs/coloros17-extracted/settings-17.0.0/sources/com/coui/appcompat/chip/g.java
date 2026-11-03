package com.coui.appcompat.chip;

import java.util.Set;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class g implements COUICheckableGroup.OnCheckedStateChangeListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIChipGroup f13322a;

    public /* synthetic */ g(COUIChipGroup cOUIChipGroup) {
        this.f13322a = cOUIChipGroup;
    }

    @Override // com.coui.appcompat.chip.COUICheckableGroup.OnCheckedStateChangeListener
    public final void onCheckedStateChanged(Set set) {
        COUIChipGroup.b(this.f13322a, set);
    }
}