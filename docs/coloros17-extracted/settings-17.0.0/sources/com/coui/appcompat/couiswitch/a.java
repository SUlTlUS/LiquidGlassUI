package com.coui.appcompat.couiswitch;

import com.coui.appcompat.spotlight.COUISpotLightEffect;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class a implements COUISpotLightEffect.OnSpotLightProgressScopeListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUISwitch f13347a;

    public /* synthetic */ a(COUISwitch cOUISwitch) {
        this.f13347a = cOUISwitch;
    }

    @Override // com.coui.appcompat.spotlight.COUISpotLightEffect.OnSpotLightProgressScopeListener
    public final void onProgressScopeChanged(float f3) {
        COUISwitch.a(this.f13347a, f3);
    }
}