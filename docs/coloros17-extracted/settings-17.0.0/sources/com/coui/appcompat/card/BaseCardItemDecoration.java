package com.coui.appcompat.card;

import android.content.Context;
import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0016\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0004J \u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0004J\u0014\u0010\u0005\u001a\u00020\u0006*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u0006H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lcom/coui/appcompat/card/BaseCardItemDecoration;", "Landroidx/recyclerview/widget/RecyclerView$ItemDecoration;", "appContext", "Landroid/content/Context;", "(Landroid/content/Context;)V", "getDimenPx", "", "resId", "setCardColumnMargin", "", "outRect", "Landroid/graphics/Rect;", "cardMargin", "Lcom/coui/appcompat/card/CardMargin;", "cardPosition", "Lcom/coui/appcompat/card/CardPosition;", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class BaseCardItemDecoration extends RecyclerView.ItemDecoration {
    private final Context appContext;

    public BaseCardItemDecoration(Context appContext) {
        Intrinsics.checkNotNullParameter(appContext, "appContext");
        this.appContext = appContext;
    }

    public final int getDimenPx(int resId) {
        return getDimenPx(this.appContext, resId);
    }

    public final void setCardColumnMargin(Rect outRect, CardMargin cardMargin, CardPosition cardPosition) {
        Intrinsics.checkNotNullParameter(outRect, "outRect");
        Intrinsics.checkNotNullParameter(cardMargin, "cardMargin");
        Intrinsics.checkNotNullParameter(cardPosition, "cardPosition");
        outRect.top = cardMargin.getOtherRowTop();
        outRect.left = cardMargin.getFirstColumnInner();
        outRect.right = cardMargin.getFirstColumnInner();
        if (cardPosition.getIsFirstColumn()) {
            outRect.left = cardMargin.getFirstColumnOuter();
        }
        if (cardPosition.getIsLastColumn()) {
            outRect.right = cardMargin.getFirstColumnOuter();
        }
        if (cardPosition.getIsFirstRow()) {
            outRect.top = cardMargin.getFirstRowTop();
        }
    }

    private final int getDimenPx(Context context, int i3) {
        return context.getResources().getDimensionPixelSize(i3);
    }
}