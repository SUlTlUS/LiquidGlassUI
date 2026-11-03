package com.coui.appcompat.poplist;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class k implements RegionBasedLongPressDetector.TargetResolver {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIPopupListWindow f13437a;

    public /* synthetic */ k(COUIPopupListWindow cOUIPopupListWindow) {
        this.f13437a = cOUIPopupListWindow;
    }

    @Override // com.coui.appcompat.poplist.RegionBasedLongPressDetector.TargetResolver
    public final RegionBasedLongPressDetector.Target resolve(View view, float f3, float f4) {
        return COUIPopupListWindow.f(this.f13437a, view, f3, f4);
    }
}