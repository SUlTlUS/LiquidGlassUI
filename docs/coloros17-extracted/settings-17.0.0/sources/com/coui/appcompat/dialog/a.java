package com.coui.appcompat.dialog;

import android.view.View;
import com.coui.appcompat.dialog.widget.COUIAlertDialogMaxLinearLayout;
import com.coui.appcompat.uiutil.COUIBackgroundBlurBuilder;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class a implements COUIBackgroundBlurBuilder.OnBlurryStateChangeListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIAlertDialogBuilder f13348a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ COUIAlertDialogMaxLinearLayout f13349b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ View f13350c;

    public /* synthetic */ a(COUIAlertDialogBuilder cOUIAlertDialogBuilder, COUIAlertDialogMaxLinearLayout cOUIAlertDialogMaxLinearLayout, View view) {
        this.f13348a = cOUIAlertDialogBuilder;
        this.f13349b = cOUIAlertDialogMaxLinearLayout;
        this.f13350c = view;
    }

    @Override // com.coui.appcompat.uiutil.COUIBackgroundBlurBuilder.OnBlurryStateChangeListener
    public final void onBlurryStateChanged(boolean z2) {
        COUIAlertDialogBuilder.a(this.f13348a, this.f13349b, this.f13350c, z2);
    }
}