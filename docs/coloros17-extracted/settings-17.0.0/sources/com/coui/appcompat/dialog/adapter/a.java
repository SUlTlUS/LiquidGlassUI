package com.coui.appcompat.dialog.adapter;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class a implements View.OnClickListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ SummaryAdapter f13351a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ int f13352b;

    public /* synthetic */ a(SummaryAdapter summaryAdapter, int i3) {
        this.f13351a = summaryAdapter;
        this.f13352b = i3;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        SummaryAdapter.a(this.f13351a, this.f13352b, view);
    }
}