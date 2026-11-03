package com.coui.appcompat.card;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class d implements View.OnClickListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ CardInstructionSelectorAdapter.SelectorHolder f13311a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ CardInstructionSelectorAdapter.Selector f13312b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ CardInstructionSelectorAdapter f13313c;

    public /* synthetic */ d(CardInstructionSelectorAdapter.SelectorHolder selectorHolder, CardInstructionSelectorAdapter.Selector selector, CardInstructionSelectorAdapter cardInstructionSelectorAdapter) {
        this.f13311a = selectorHolder;
        this.f13312b = selector;
        this.f13313c = cardInstructionSelectorAdapter;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        CardInstructionSelectorAdapter.SelectorHolder.b(this.f13311a, this.f13312b, this.f13313c, view);
    }
}