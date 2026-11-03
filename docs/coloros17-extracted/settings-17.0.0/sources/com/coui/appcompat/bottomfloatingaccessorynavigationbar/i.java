package com.coui.appcompat.bottomfloatingaccessorynavigationbar;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class i implements COUIBottomFloatingAccessoryView.EffectCapabilityHost {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIBottomFloatingAccessoryNavigationBar f13259a;

    public /* synthetic */ i(COUIBottomFloatingAccessoryNavigationBar cOUIBottomFloatingAccessoryNavigationBar) {
        this.f13259a = cOUIBottomFloatingAccessoryNavigationBar;
    }

    @Override // com.coui.appcompat.bottomfloatingaccessorynavigationbar.COUIBottomFloatingAccessoryView.EffectCapabilityHost
    public final boolean isFullMotionEffect() {
        return COUIBottomFloatingAccessoryNavigationBar.c(this.f13259a);
    }
}