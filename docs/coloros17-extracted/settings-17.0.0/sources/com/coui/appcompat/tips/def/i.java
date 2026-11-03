package com.coui.appcompat.tips.def;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class i implements View.OnClickListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIDefaultTopTipsView f13616a;

    public /* synthetic */ i(COUIDefaultTopTipsView cOUIDefaultTopTipsView) {
        this.f13616a = cOUIDefaultTopTipsView;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        COUIDefaultTopTipsView.a(this.f13616a, view);
    }
}