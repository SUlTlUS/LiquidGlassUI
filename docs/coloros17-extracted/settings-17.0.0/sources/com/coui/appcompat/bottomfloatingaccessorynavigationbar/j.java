package com.coui.appcompat.bottomfloatingaccessorynavigationbar;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class j implements COUIBottomFloatingAccessoryView.EffectCapabilityHost {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIBottomFloatingAccessoryNavigationBar f13260a;

    public /* synthetic */ j(COUIBottomFloatingAccessoryNavigationBar cOUIBottomFloatingAccessoryNavigationBar) {
        this.f13260a = cOUIBottomFloatingAccessoryNavigationBar;
    }

    @Override // com.coui.appcompat.bottomfloatingaccessorynavigationbar.COUIBottomFloatingAccessoryView.EffectCapabilityHost
    public final boolean isFullMotionEffect() {
        return COUIBottomFloatingAccessoryNavigationBar.d(this.f13260a);
    }
}