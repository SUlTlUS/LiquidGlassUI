package com.coui.appcompat.poplist;

import android.view.MotionEvent;
import com.coui.appcompat.anim.ability.COUIPopupTouchMotionAnimator;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class a implements RoundFrameLayout.OnDispatchTouchEventListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BasePopupMenuAnimationController f13403a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ RoundFrameLayout f13404b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ COUIPopupTouchMotionAnimator f13405c;

    public /* synthetic */ a(BasePopupMenuAnimationController basePopupMenuAnimationController, RoundFrameLayout roundFrameLayout, COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator) {
        this.f13403a = basePopupMenuAnimationController;
        this.f13404b = roundFrameLayout;
        this.f13405c = cOUIPopupTouchMotionAnimator;
    }

    @Override // com.coui.appcompat.poplist.RoundFrameLayout.OnDispatchTouchEventListener
    public final boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return BasePopupMenuAnimationController.a(this.f13403a, this.f13404b, this.f13405c, motionEvent);
    }
}