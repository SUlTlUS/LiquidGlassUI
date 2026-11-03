package com.coui.appcompat.button;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class c implements COUIButtonLayout.OnButtonLayoutVisibilityChangedListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ SimpleButtonGroupCtrl f13298a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ COUIButtonLayout f13299b;

    public /* synthetic */ c(SimpleButtonGroupCtrl simpleButtonGroupCtrl, COUIButtonLayout cOUIButtonLayout) {
        this.f13298a = simpleButtonGroupCtrl;
        this.f13299b = cOUIButtonLayout;
    }

    @Override // com.coui.appcompat.button.COUIButtonLayout.OnButtonLayoutVisibilityChangedListener
    public final void onButtonLayoutVisibilityChanged(int i3) {
        SimpleButtonGroupCtrl.b(this.f13298a, this.f13299b, i3);
    }
}