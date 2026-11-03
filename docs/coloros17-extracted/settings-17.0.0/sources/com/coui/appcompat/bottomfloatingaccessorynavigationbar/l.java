package com.coui.appcompat.bottomfloatingaccessorynavigationbar;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class l implements View.OnClickListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIBottomFloatingAccessoryNavigationBar.OnAccessoryClickListener f13262a;

    public /* synthetic */ l(COUIBottomFloatingAccessoryNavigationBar.OnAccessoryClickListener onAccessoryClickListener) {
        this.f13262a = onAccessoryClickListener;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        COUIBottomFloatingAccessoryNavigationBar.e(this.f13262a, view);
    }
}