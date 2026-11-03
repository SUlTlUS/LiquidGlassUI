package com.coui.appcompat.dialog;

import android.view.accessibility.AccessibilityManager;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class c implements AccessibilityManager.TouchExplorationStateChangeListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIAlertDialogBuilder.AnonymousClass1 f13354a;

    public /* synthetic */ c(COUIAlertDialogBuilder.AnonymousClass1 anonymousClass1) {
        this.f13354a = anonymousClass1;
    }

    @Override // android.view.accessibility.AccessibilityManager.TouchExplorationStateChangeListener
    public final void onTouchExplorationStateChanged(boolean z2) {
        COUIAlertDialogBuilder.AnonymousClass1.a(this.f13354a, z2);
    }
}