package com.coui.appcompat.card;

import android.view.MotionEvent;
import android.view.View;
import com.coui.appcompat.pressfeedback.COUIPressFeedbackHelper;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class c implements View.OnTouchListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIPressFeedbackJumpPreference f13309a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ COUIPressFeedbackHelper f13310b;

    public /* synthetic */ c(COUIPressFeedbackJumpPreference cOUIPressFeedbackJumpPreference, COUIPressFeedbackHelper cOUIPressFeedbackHelper) {
        this.f13309a = cOUIPressFeedbackJumpPreference;
        this.f13310b = cOUIPressFeedbackHelper;
    }

    @Override // android.view.View.OnTouchListener
    public final boolean onTouch(View view, MotionEvent motionEvent) {
        return COUIPressFeedbackJumpPreference.c(this.f13309a, this.f13310b, view, motionEvent);
    }
}