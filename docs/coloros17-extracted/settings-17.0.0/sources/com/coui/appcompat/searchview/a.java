package com.coui.appcompat.searchview;

import com.coui.appcompat.toolbar.ToolbarMaterialEffectDelegate;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class a implements ToolbarMaterialEffectDelegate.OnPressWhileInvisibleListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUISearchBar f13483a;

    public /* synthetic */ a(COUISearchBar cOUISearchBar) {
        this.f13483a = cOUISearchBar;
    }

    @Override // com.coui.appcompat.toolbar.ToolbarMaterialEffectDelegate.OnPressWhileInvisibleListener
    public final void onDownWhileInvisible() {
        COUISearchBar.l(this.f13483a);
    }
}