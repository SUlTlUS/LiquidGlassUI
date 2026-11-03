package com.coui.appcompat.chip;

import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.scrollview.COUIHorizontalScrollView;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class p implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIChipTabGroup f13331a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ COUISpringAnimation f13332b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ COUIHorizontalScrollView f13333c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ int f13334d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final /* synthetic */ int f13335e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final /* synthetic */ int f13336f;

    public /* synthetic */ p(COUIChipTabGroup cOUIChipTabGroup, COUISpringAnimation cOUISpringAnimation, COUIHorizontalScrollView cOUIHorizontalScrollView, int i3, int i4, int i5) {
        this.f13331a = cOUIChipTabGroup;
        this.f13332b = cOUISpringAnimation;
        this.f13333c = cOUIHorizontalScrollView;
        this.f13334d = i3;
        this.f13335e = i4;
        this.f13336f = i5;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUIChipTabGroup.h(this.f13331a, this.f13332b, this.f13333c, this.f13334d, this.f13335e, this.f13336f);
    }
}