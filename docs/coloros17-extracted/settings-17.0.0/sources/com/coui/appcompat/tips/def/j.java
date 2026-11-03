package com.coui.appcompat.tips.def;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class j implements View.OnClickListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIDefaultTopTipsView f13617a;

    public /* synthetic */ j(COUIDefaultTopTipsView cOUIDefaultTopTipsView) {
        this.f13617a = cOUIDefaultTopTipsView;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        COUIDefaultTopTipsView.c(this.f13617a, view);
    }
}