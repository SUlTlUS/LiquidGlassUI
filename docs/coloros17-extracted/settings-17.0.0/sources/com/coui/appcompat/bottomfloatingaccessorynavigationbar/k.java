package com.coui.appcompat.bottomfloatingaccessorynavigationbar;

import com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class k implements BaseBottomFloatingMenuView.OnDrawingBoundsChangedListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIBottomFloatingAccessoryNavigationBar f13261a;

    public /* synthetic */ k(COUIBottomFloatingAccessoryNavigationBar cOUIBottomFloatingAccessoryNavigationBar) {
        this.f13261a = cOUIBottomFloatingAccessoryNavigationBar;
    }

    @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView.OnDrawingBoundsChangedListener
    public final void onDrawingBoundsChanged() {
        COUIBottomFloatingAccessoryNavigationBar.a(this.f13261a);
    }
}