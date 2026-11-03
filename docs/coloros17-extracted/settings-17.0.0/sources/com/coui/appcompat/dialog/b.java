package com.coui.appcompat.dialog;

import android.view.accessibility.AccessibilityManager;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class b implements AccessibilityManager.AccessibilityStateChangeListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIAlertDialogBuilder.AnonymousClass1 f13353a;

    public /* synthetic */ b(COUIAlertDialogBuilder.AnonymousClass1 anonymousClass1) {
        this.f13353a = anonymousClass1;
    }

    @Override // android.view.accessibility.AccessibilityManager.AccessibilityStateChangeListener
    public final void onAccessibilityStateChanged(boolean z2) {
        COUIAlertDialogBuilder.AnonymousClass1.b(this.f13353a, z2);
    }
}