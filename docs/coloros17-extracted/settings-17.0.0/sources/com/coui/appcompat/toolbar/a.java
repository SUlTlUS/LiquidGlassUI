package com.coui.appcompat.toolbar;

import com.google.android.material.appbar.COUIAppBarLayout;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class a implements COUIAppBarLayout.OnOffsetChangedListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ AppBarBlurOverlayBehavior f13619a;

    public /* synthetic */ a(AppBarBlurOverlayBehavior appBarBlurOverlayBehavior) {
        this.f13619a = appBarBlurOverlayBehavior;
    }

    @Override // com.google.android.material.appbar.COUIAppBarLayout.OnOffsetChangedListener, com.google.android.material.appbar.COUIAppBarLayout.BaseOnOffsetChangedListener
    public final void onOffsetChanged(COUIAppBarLayout cOUIAppBarLayout, int i3) {
        AppBarBlurOverlayBehavior.a(this.f13619a, cOUIAppBarLayout, i3);
    }
}