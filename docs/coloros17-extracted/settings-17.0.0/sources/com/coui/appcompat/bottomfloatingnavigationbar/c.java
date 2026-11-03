package com.coui.appcompat.bottomfloatingnavigationbar;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class c implements BaseBottomFloatingMenuView.OnDrawingBoundsChangedListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIBottomFloatingNavigationBar f13273a;

    public /* synthetic */ c(COUIBottomFloatingNavigationBar cOUIBottomFloatingNavigationBar) {
        this.f13273a = cOUIBottomFloatingNavigationBar;
    }

    @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView.OnDrawingBoundsChangedListener
    public final void onDrawingBoundsChanged() {
        COUIBottomFloatingNavigationBar.b(this.f13273a);
    }
}