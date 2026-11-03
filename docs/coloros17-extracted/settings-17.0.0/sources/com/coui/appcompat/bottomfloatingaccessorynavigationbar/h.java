package com.coui.appcompat.bottomfloatingaccessorynavigationbar;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class h implements View.OnClickListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIBottomFloatingAccessoryNavigationBar.OnAccessoryClickListener f13258a;

    public /* synthetic */ h(COUIBottomFloatingAccessoryNavigationBar.OnAccessoryClickListener onAccessoryClickListener) {
        this.f13258a = onAccessoryClickListener;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        COUIBottomFloatingAccessoryNavigationBar.b(this.f13258a, view);
    }
}