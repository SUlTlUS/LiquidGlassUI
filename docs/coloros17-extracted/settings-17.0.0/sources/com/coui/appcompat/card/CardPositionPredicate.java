package com.coui.appcompat.card;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u000b\n\u0002\b\u0006\u0018\u00002\u00020\u0001BU\u0012\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003\u0012\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003\u0012\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003\u0012\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\u0002\u0010\tR\u001d\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\nR\u001d\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\nR\u001d\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\nR\u001d\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\n¨\u0006\u000b"}, d2 = {"Lcom/coui/appcompat/card/CardPositionPredicate;", "", "isFirstRow", "Lkotlin/Function1;", "", "", "isLastRow", "isFirstColumn", "isLastColumn", "(Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "()Lkotlin/jvm/functions/Function1;", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class CardPositionPredicate {
    private final Function1<Integer, Boolean> isFirstColumn;
    private final Function1<Integer, Boolean> isFirstRow;
    private final Function1<Integer, Boolean> isLastColumn;
    private final Function1<Integer, Boolean> isLastRow;

    /* JADX WARN: Multi-variable type inference failed */
    public CardPositionPredicate(Function1<? super Integer, Boolean> isFirstRow, Function1<? super Integer, Boolean> isLastRow, Function1<? super Integer, Boolean> isFirstColumn, Function1<? super Integer, Boolean> isLastColumn) {
        Intrinsics.checkNotNullParameter(isFirstRow, "isFirstRow");
        Intrinsics.checkNotNullParameter(isLastRow, "isLastRow");
        Intrinsics.checkNotNullParameter(isFirstColumn, "isFirstColumn");
        Intrinsics.checkNotNullParameter(isLastColumn, "isLastColumn");
        this.isFirstRow = isFirstRow;
        this.isLastRow = isLastRow;
        this.isFirstColumn = isFirstColumn;
        this.isLastColumn = isLastColumn;
    }

    public final Function1<Integer, Boolean> isFirstColumn() {
        return this.isFirstColumn;
    }

    public final Function1<Integer, Boolean> isFirstRow() {
        return this.isFirstRow;
    }

    public final Function1<Integer, Boolean> isLastColumn() {
        return this.isLastColumn;
    }

    public final Function1<Integer, Boolean> isLastRow() {
        return this.isLastRow;
    }
}