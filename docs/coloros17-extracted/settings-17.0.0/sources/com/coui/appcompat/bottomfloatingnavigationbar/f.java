package com.coui.appcompat.bottomfloatingnavigationbar;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class f implements View.OnClickListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIBottomFloatingNavigationBar f13276a;

    public /* synthetic */ f(COUIBottomFloatingNavigationBar cOUIBottomFloatingNavigationBar) {
        this.f13276a = cOUIBottomFloatingNavigationBar;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        COUIBottomFloatingNavigationBar.f(this.f13276a, view);
    }
}