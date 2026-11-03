package com.coui.appcompat.searchview;

import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class t implements View.OnClickListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ SearchBarBackgroundView f13508a;

    public /* synthetic */ t(SearchBarBackgroundView searchBarBackgroundView) {
        this.f13508a = searchBarBackgroundView;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        SearchBarBackgroundView.a(this.f13508a, view);
    }
}