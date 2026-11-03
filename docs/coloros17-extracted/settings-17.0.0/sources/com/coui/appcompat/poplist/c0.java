package com.coui.appcompat.poplist;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class c0 implements PopupMenuControlRule {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ PopupMenuLocateHelper f13419a;

    public /* synthetic */ c0(PopupMenuLocateHelper popupMenuLocateHelper) {
        this.f13419a = popupMenuLocateHelper;
    }

    @Override // com.coui.appcompat.poplist.PopupMenuControlRule
    public final void operation(PopupMenuDomain popupMenuDomain) {
        PopupMenuLocateHelper.a(this.f13419a, popupMenuDomain);
    }
}