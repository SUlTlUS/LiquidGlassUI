package com.coui.appcompat.stepper;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class b implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIStepperView f13591a;

    public /* synthetic */ b(COUIStepperView cOUIStepperView) {
        this.f13591a = cOUIStepperView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUIStepperView.a(this.f13591a);
    }
}