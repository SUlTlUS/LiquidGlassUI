package com.coui.appcompat.bottomfloatingaccessorynavigationbar;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class b implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ AccessorySideExpansionController f13252a;

    public /* synthetic */ b(AccessorySideExpansionController accessorySideExpansionController) {
        this.f13252a = accessorySideExpansionController;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        AccessorySideExpansionController.b(this.f13252a, cOUIDynamicAnimation, z2, f3, f4);
    }
}