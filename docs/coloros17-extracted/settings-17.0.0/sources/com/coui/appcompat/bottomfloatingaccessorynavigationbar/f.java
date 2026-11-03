package com.coui.appcompat.bottomfloatingaccessorynavigationbar;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class f implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ AccessorySideExpansionController f13256a;

    public /* synthetic */ f(AccessorySideExpansionController accessorySideExpansionController) {
        this.f13256a = accessorySideExpansionController;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        AccessorySideExpansionController.c(this.f13256a, cOUIDynamicAnimation, f3, f4);
    }
}