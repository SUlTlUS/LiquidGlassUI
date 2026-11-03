package com.coui.appcompat.card;

import android.view.View;
import kotlin.Metadata;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0014\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0001H\u0002¨\u0006\u0004"}, d2 = {"getDimenPx", "", "Landroid/view/View;", "resId", "coui-support-card_release"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class CardInstructionDescriptionAdapterKt {
    public static final /* synthetic */ int access$getDimenPx(View view, int i3) {
        return getDimenPx(view, i3);
    }

    private static final int getDimenPx(View view, int i3) {
        return view.getContext().getResources().getDimensionPixelSize(i3);
    }
}