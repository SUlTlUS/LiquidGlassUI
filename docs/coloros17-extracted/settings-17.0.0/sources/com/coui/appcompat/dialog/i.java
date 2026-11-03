package com.coui.appcompat.dialog;

import com.coui.appcompat.indicator.COUIPageIndicator;
import com.coui.appcompat.viewpager.COUIViewPager2;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class i implements COUIPageIndicator.OnIndicatorDotClickListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIViewPager2 f13363a;

    public /* synthetic */ i(COUIViewPager2 cOUIViewPager2) {
        this.f13363a = cOUIViewPager2;
    }

    @Override // com.coui.appcompat.indicator.COUIPageIndicator.OnIndicatorDotClickListener
    public final void onClick(int i3) {
        this.f13363a.setCurrentItem(i3);
    }
}