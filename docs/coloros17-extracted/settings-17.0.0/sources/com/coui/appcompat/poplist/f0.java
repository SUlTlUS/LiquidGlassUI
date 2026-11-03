package com.coui.appcompat.poplist;

import com.coui.appcompat.anim.ability.SubMenuTransitionUnit;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class f0 implements SubMenuTransitionUnit.OnProgressUpdateCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ SmallScreenAnimationController f13431a;

    public /* synthetic */ f0(SmallScreenAnimationController smallScreenAnimationController) {
        this.f13431a = smallScreenAnimationController;
    }

    @Override // com.coui.appcompat.anim.ability.SubMenuTransitionUnit.OnProgressUpdateCallback
    public final void onProgressUpdate(float f3) {
        SmallScreenAnimationController.f(this.f13431a, f3);
    }
}