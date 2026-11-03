package com.coui.appcompat.card;

import com.oplus.settings.feature.language.localepicker.RegionPickerUtils;
import kotlin.Metadata;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003¢\u0006\u0002\u0010\u0007J\t\u0010\r\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J1\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0015\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0016\u001a\u00020\u0017HÖ\u0001R\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\t¨\u0006\u0018"}, d2 = {"Lcom/coui/appcompat/card/CardMargin;", "", "firstRowTop", "", "otherRowTop", "firstColumnOuter", "firstColumnInner", "(IIII)V", "getFirstColumnInner", "()I", "getFirstColumnOuter", "getFirstRowTop", "getOtherRowTop", "component1", "component2", "component3", "component4", "copy", "equals", "", RegionPickerUtils.OTHER_SYSTEM_REGION_KEY, "hashCode", "toString", "", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final /* data */ class CardMargin {
    private final int firstColumnInner;
    private final int firstColumnOuter;
    private final int firstRowTop;
    private final int otherRowTop;

    public CardMargin(int i3, int i4, int i5, int i6) {
        this.firstRowTop = i3;
        this.otherRowTop = i4;
        this.firstColumnOuter = i5;
        this.firstColumnInner = i6;
    }

    public static /* synthetic */ CardMargin copy$default(CardMargin cardMargin, int i3, int i4, int i5, int i6, int i7, Object obj) {
        if ((i7 & 1) != 0) {
            i3 = cardMargin.firstRowTop;
        }
        if ((i7 & 2) != 0) {
            i4 = cardMargin.otherRowTop;
        }
        if ((i7 & 4) != 0) {
            i5 = cardMargin.firstColumnOuter;
        }
        if ((i7 & 8) != 0) {
            i6 = cardMargin.firstColumnInner;
        }
        return cardMargin.copy(i3, i4, i5, i6);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getFirstRowTop() {
        return this.firstRowTop;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int getOtherRowTop() {
        return this.otherRowTop;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final int getFirstColumnOuter() {
        return this.firstColumnOuter;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final int getFirstColumnInner() {
        return this.firstColumnInner;
    }

    public final CardMargin copy(int firstRowTop, int otherRowTop, int firstColumnOuter, int firstColumnInner) {
        return new CardMargin(firstRowTop, otherRowTop, firstColumnOuter, firstColumnInner);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CardMargin)) {
            return false;
        }
        CardMargin cardMargin = (CardMargin) other;
        return this.firstRowTop == cardMargin.firstRowTop && this.otherRowTop == cardMargin.otherRowTop && this.firstColumnOuter == cardMargin.firstColumnOuter && this.firstColumnInner == cardMargin.firstColumnInner;
    }

    public final int getFirstColumnInner() {
        return this.firstColumnInner;
    }

    public final int getFirstColumnOuter() {
        return this.firstColumnOuter;
    }

    public final int getFirstRowTop() {
        return this.firstRowTop;
    }

    public final int getOtherRowTop() {
        return this.otherRowTop;
    }

    public int hashCode() {
        return (((((Integer.hashCode(this.firstRowTop) * 31) + Integer.hashCode(this.otherRowTop)) * 31) + Integer.hashCode(this.firstColumnOuter)) * 31) + Integer.hashCode(this.firstColumnInner);
    }

    public String toString() {
        return "CardMargin(firstRowTop=" + this.firstRowTop + ", otherRowTop=" + this.otherRowTop + ", firstColumnOuter=" + this.firstColumnOuter + ", firstColumnInner=" + this.firstColumnInner + ')';
    }
}