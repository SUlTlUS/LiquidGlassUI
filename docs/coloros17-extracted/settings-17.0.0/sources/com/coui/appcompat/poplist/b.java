package com.coui.appcompat.poplist;

import android.view.MotionEvent;
import com.coui.appcompat.anim.ability.COUIPopupTouchMotionAnimator;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class b implements RoundFrameLayout.OnDispatchTouchEventListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BasePopupMenuAnimationController f13409a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ RoundFrameLayout f13410b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ COUIPopupTouchMotionAnimator f13411c;

    public /* synthetic */ b(BasePopupMenuAnimationController basePopupMenuAnimationController, RoundFrameLayout roundFrameLayout, COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator) {
        this.f13409a = basePopupMenuAnimationController;
        this.f13410b = roundFrameLayout;
        this.f13411c = cOUIPopupTouchMotionAnimator;
    }

    @Override // com.coui.appcompat.poplist.RoundFrameLayout.OnDispatchTouchEventListener
    public final boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return BasePopupMenuAnimationController.b(this.f13409a, this.f13410b, this.f13411c, motionEvent);
    }
}