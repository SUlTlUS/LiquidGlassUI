package com.coui.appcompat.tips.def;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class h implements OnLinesChangedListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIDefaultTopTips.AnonymousClass3 f13614a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ COUIDefaultTopTipsView f13615b;

    public /* synthetic */ h(COUIDefaultTopTips.AnonymousClass3 anonymousClass3, COUIDefaultTopTipsView cOUIDefaultTopTipsView) {
        this.f13614a = anonymousClass3;
        this.f13615b = cOUIDefaultTopTipsView;
    }

    @Override // com.coui.appcompat.tips.def.OnLinesChangedListener
    public final void onLinesChanged(int i3) {
        COUIDefaultTopTips.AnonymousClass3.a(this.f13614a, this.f13615b, i3);
    }
}