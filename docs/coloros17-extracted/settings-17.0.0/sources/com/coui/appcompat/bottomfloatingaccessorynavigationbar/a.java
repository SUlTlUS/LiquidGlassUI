package com.coui.appcompat.bottomfloatingaccessorynavigationbar;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class a implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ AccessorySideExpansionController f13251a;

    public /* synthetic */ a(AccessorySideExpansionController accessorySideExpansionController) {
        this.f13251a = accessorySideExpansionController;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        AccessorySideExpansionController.d(this.f13251a, cOUIDynamicAnimation, f3, f4);
    }
}