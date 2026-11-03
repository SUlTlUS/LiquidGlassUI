package com.coui.appcompat.poplist;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class l implements RegionBasedLongPressDetector.CancelPredicate {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIPopupListWindow f13438a;

    public /* synthetic */ l(COUIPopupListWindow cOUIPopupListWindow) {
        this.f13438a = cOUIPopupListWindow;
    }

    @Override // com.coui.appcompat.poplist.RegionBasedLongPressDetector.CancelPredicate
    public final boolean shouldCancel() {
        return COUIPopupListWindow.d(this.f13438a);
    }
}