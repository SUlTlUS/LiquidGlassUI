package com.coui.appcompat.toolbar;

import android.view.MotionEvent;
import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class d0 implements View.OnTouchListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ToolbarMaterialEffectDelegate f13630a;

    public /* synthetic */ d0(ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate) {
        this.f13630a = toolbarMaterialEffectDelegate;
    }

    @Override // android.view.View.OnTouchListener
    public final boolean onTouch(View view, MotionEvent motionEvent) {
        return ToolbarMaterialEffectDelegate.b(this.f13630a, view, motionEvent);
    }
}