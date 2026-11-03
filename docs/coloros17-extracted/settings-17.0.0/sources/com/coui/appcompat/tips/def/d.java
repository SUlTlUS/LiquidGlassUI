package com.coui.appcompat.tips.def;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class d implements OnLinesChangedListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIDefaultTopTips f13604a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ COUIDefaultTopTipsView f13605b;

    public /* synthetic */ d(COUIDefaultTopTips cOUIDefaultTopTips, COUIDefaultTopTipsView cOUIDefaultTopTipsView) {
        this.f13604a = cOUIDefaultTopTips;
        this.f13605b = cOUIDefaultTopTipsView;
    }

    @Override // com.coui.appcompat.tips.def.OnLinesChangedListener
    public final void onLinesChanged(int i3) {
        COUIDefaultTopTips.g(this.f13604a, this.f13605b, i3);
    }
}