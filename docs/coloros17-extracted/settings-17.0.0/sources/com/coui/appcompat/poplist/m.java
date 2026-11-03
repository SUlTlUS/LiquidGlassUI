package com.coui.appcompat.poplist;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class m implements RegionBasedLongPressDetector.OnLongPressListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIPopupListWindow f13439a;

    public /* synthetic */ m(COUIPopupListWindow cOUIPopupListWindow) {
        this.f13439a = cOUIPopupListWindow;
    }

    @Override // com.coui.appcompat.poplist.RegionBasedLongPressDetector.OnLongPressListener
    public final void onLongPress(View view, int i3, boolean z2) {
        COUIPopupListWindow.a(this.f13439a, view, i3, z2);
    }
}