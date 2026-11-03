package com.coui.appcompat.bottomfloatingtoolbar;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class d implements View.OnClickListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIBottomFloatingToolbarMenuView f13282a;

    public /* synthetic */ d(COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView) {
        this.f13282a = cOUIBottomFloatingToolbarMenuView;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        COUIBottomFloatingToolbarMenuView.b(this.f13282a, view);
    }
}