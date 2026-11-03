package com.coui.appcompat.poplist;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class b0 implements View.OnLongClickListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ MixedPopupListAdapter f13412a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ int f13413b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ PopupListItem f13414c;

    public /* synthetic */ b0(MixedPopupListAdapter mixedPopupListAdapter, int i3, PopupListItem popupListItem) {
        this.f13412a = mixedPopupListAdapter;
        this.f13413b = i3;
        this.f13414c = popupListItem;
    }

    @Override // android.view.View.OnLongClickListener
    public final boolean onLongClick(View view) {
        return MixedPopupListAdapter.a(this.f13412a, this.f13413b, this.f13414c, view);
    }
}