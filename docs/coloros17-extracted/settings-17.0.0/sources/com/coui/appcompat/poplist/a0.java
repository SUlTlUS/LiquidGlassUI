package com.coui.appcompat.poplist;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class a0 implements View.OnClickListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ MixedPopupListAdapter f13406a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ int f13407b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ PopupListItem f13408c;

    public /* synthetic */ a0(MixedPopupListAdapter mixedPopupListAdapter, int i3, PopupListItem popupListItem) {
        this.f13406a = mixedPopupListAdapter;
        this.f13407b = i3;
        this.f13408c = popupListItem;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        MixedPopupListAdapter.b(this.f13406a, this.f13407b, this.f13408c, view);
    }
}