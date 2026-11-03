package com.coui.appcompat.card;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\tR\u0011\u0010\n\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0011\u0010\u000b\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\tR\u0011\u0010\f\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\t¨\u0006\r"}, d2 = {"Lcom/coui/appcompat/card/CardPosition;", "", "cardPositionPredicate", "Lcom/coui/appcompat/card/CardPositionPredicate;", "position", "", "(Lcom/coui/appcompat/card/CardPositionPredicate;I)V", "isFirstColumn", "", "()Z", "isFirstRow", "isLastColumn", "isLastRow", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class CardPosition {
    private final boolean isFirstColumn;
    private final boolean isFirstRow;
    private final boolean isLastColumn;
    private final boolean isLastRow;

    public CardPosition(CardPositionPredicate cardPositionPredicate, int i3) {
        Intrinsics.checkNotNullParameter(cardPositionPredicate, "cardPositionPredicate");
        this.isFirstRow = cardPositionPredicate.isFirstRow().invoke(Integer.valueOf(i3)).booleanValue();
        this.isLastRow = cardPositionPredicate.isLastRow().invoke(Integer.valueOf(i3)).booleanValue();
        this.isFirstColumn = cardPositionPredicate.isFirstColumn().invoke(Integer.valueOf(i3)).booleanValue();
        this.isLastColumn = cardPositionPredicate.isLastColumn().invoke(Integer.valueOf(i3)).booleanValue();
    }

    /* JADX INFO: renamed from: isFirstColumn, reason: from getter */
    public final boolean getIsFirstColumn() {
        return this.isFirstColumn;
    }

    /* JADX INFO: renamed from: isFirstRow, reason: from getter */
    public final boolean getIsFirstRow() {
        return this.isFirstRow;
    }

    /* JADX INFO: renamed from: isLastColumn, reason: from getter */
    public final boolean getIsLastColumn() {
        return this.isLastColumn;
    }

    /* JADX INFO: renamed from: isLastRow, reason: from getter */
    public final boolean getIsLastRow() {
        return this.isLastRow;
    }
}