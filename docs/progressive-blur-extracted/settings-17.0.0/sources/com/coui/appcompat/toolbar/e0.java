package com.coui.appcompat.toolbar;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class e0 implements View.OnLayoutChangeListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ToolbarMaterialEffectDelegate f13632a;

    public /* synthetic */ e0(ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate) {
        this.f13632a = toolbarMaterialEffectDelegate;
    }

    @Override // android.view.View.OnLayoutChangeListener
    public final void onLayoutChange(View view, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        ToolbarMaterialEffectDelegate.a(this.f13632a, view, i3, i4, i5, i6, i7, i8, i9, i10);
    }
}