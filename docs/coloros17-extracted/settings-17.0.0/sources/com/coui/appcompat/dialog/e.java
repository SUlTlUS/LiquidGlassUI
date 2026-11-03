package com.coui.appcompat.dialog;

import com.coui.appcompat.uiutil.COUIBackgroundBlurBuilder;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class e implements COUIBackgroundBlurBuilder.OnBlurryStateChangeListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIGuideDialogBuilder f13359a;

    public /* synthetic */ e(COUIGuideDialogBuilder cOUIGuideDialogBuilder) {
        this.f13359a = cOUIGuideDialogBuilder;
    }

    @Override // com.coui.appcompat.uiutil.COUIBackgroundBlurBuilder.OnBlurryStateChangeListener
    public final void onBlurryStateChanged(boolean z2) {
        COUIGuideDialogBuilder.a(this.f13359a, z2);
    }
}