package com.coui.appcompat.poplist;

import android.view.View;
import android.widget.AdapterView;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class i implements AdapterView.OnItemClickListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIPopupListWindow f13435a;

    public /* synthetic */ i(COUIPopupListWindow cOUIPopupListWindow) {
        this.f13435a = cOUIPopupListWindow;
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public final void onItemClick(AdapterView adapterView, View view, int i3, long j3) {
        COUIPopupListWindow.b(this.f13435a, adapterView, view, i3, j3);
    }
}