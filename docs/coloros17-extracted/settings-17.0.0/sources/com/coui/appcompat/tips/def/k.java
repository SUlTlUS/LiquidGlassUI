package com.coui.appcompat.tips.def;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class k implements View.OnClickListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIDefaultTopTipsView f13618a;

    public /* synthetic */ k(COUIDefaultTopTipsView cOUIDefaultTopTipsView) {
        this.f13618a = cOUIDefaultTopTipsView;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        COUIDefaultTopTipsView.b(this.f13618a, view);
    }
}