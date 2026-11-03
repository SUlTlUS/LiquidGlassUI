package com.coui.appcompat.stepper;

import android.view.MotionEvent;
import android.view.View;
import com.coui.appcompat.state.COUIStateEffectDrawable;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class a implements View.OnTouchListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIStateEffectDrawable f13590a;

    public /* synthetic */ a(COUIStateEffectDrawable cOUIStateEffectDrawable) {
        this.f13590a = cOUIStateEffectDrawable;
    }

    @Override // android.view.View.OnTouchListener
    public final boolean onTouch(View view, MotionEvent motionEvent) {
        return COUIStepperView.b(this.f13590a, view, motionEvent);
    }
}