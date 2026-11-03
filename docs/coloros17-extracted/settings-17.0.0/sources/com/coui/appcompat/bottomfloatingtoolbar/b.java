package com.coui.appcompat.bottomfloatingtoolbar;

import java.util.function.Consumer;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class b implements Consumer {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIBottomFloatingToolbar f13281a;

    public /* synthetic */ b(COUIBottomFloatingToolbar cOUIBottomFloatingToolbar) {
        this.f13281a = cOUIBottomFloatingToolbar;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        COUIBottomFloatingToolbar.b(this.f13281a, (Boolean) obj);
    }
}