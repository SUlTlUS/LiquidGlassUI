package com.coui.appcompat.card;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroupAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.support.card.R;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\bJ\b\u0010\u0016\u001a\u00020\u000eH\u0002J\b\u0010\u0017\u001a\u00020\u000eH\u0002J(\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!H\u0017R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u00020\u000eX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0013\u001a\u00020\u000eX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0010\"\u0004\b\u0015\u0010\u0012¨\u0006\""}, d2 = {"Lcom/coui/appcompat/card/CardEntranceDecoration;", "Lcom/coui/appcompat/card/BaseCardItemDecoration;", "appContext", "Landroid/content/Context;", "cardPositionPredicate", "Lcom/coui/appcompat/card/CardPositionPredicate;", "adapter", "Landroidx/preference/PreferenceGroupAdapter;", "(Landroid/content/Context;Lcom/coui/appcompat/card/CardPositionPredicate;Landroidx/preference/PreferenceGroupAdapter;)V", "getAdapter", "()Landroidx/preference/PreferenceGroupAdapter;", "setAdapter", "(Landroidx/preference/PreferenceGroupAdapter;)V", "largeCardMargin", "Lcom/coui/appcompat/card/CardMargin;", "getLargeCardMargin", "()Lcom/coui/appcompat/card/CardMargin;", "setLargeCardMargin", "(Lcom/coui/appcompat/card/CardMargin;)V", "smallCardMargin", "getSmallCardMargin", "setSmallCardMargin", "getDefaultLargeCardMargin", "getDefaultSmallCardMargin", "getItemOffsets", "", "outRect", "Landroid/graphics/Rect;", "view", "Landroid/view/View;", "parent", "Landroidx/recyclerview/widget/RecyclerView;", "state", "Landroidx/recyclerview/widget/RecyclerView$State;", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class CardEntranceDecoration extends BaseCardItemDecoration {
    private PreferenceGroupAdapter adapter;
    private final CardPositionPredicate cardPositionPredicate;
    private CardMargin largeCardMargin;
    private CardMargin smallCardMargin;

    public /* synthetic */ CardEntranceDecoration(Context context, CardPositionPredicate cardPositionPredicate, PreferenceGroupAdapter preferenceGroupAdapter, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, cardPositionPredicate, (i3 & 4) != 0 ? null : preferenceGroupAdapter);
    }

    private final CardMargin getDefaultLargeCardMargin() {
        int i3 = R.dimen.coui_component_card_entrance_large_top_margin;
        int dimenPx = getDimenPx(i3);
        int dimenPx2 = getDimenPx(i3);
        int i4 = R.dimen.coui_component_card_entrance_large_horizontal_margin;
        return new CardMargin(dimenPx, dimenPx2, getDimenPx(i4), getDimenPx(i4));
    }

    private final CardMargin getDefaultSmallCardMargin() {
        return new CardMargin(getDimenPx(R.dimen.coui_component_card_entrance_small_top_margin_first), getDimenPx(R.dimen.coui_component_card_entrance_small_top_margin_other), getDimenPx(R.dimen.coui_component_card_entrance_small_horizontal_margin_outer), getDimenPx(R.dimen.coui_component_card_entrance_small_horizontal_margin_inner));
    }

    public final PreferenceGroupAdapter getAdapter() {
        return this.adapter;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        Intrinsics.checkNotNullParameter(outRect, "outRect");
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(parent, "parent");
        Intrinsics.checkNotNullParameter(state, "state");
        int childAdapterPosition = parent.getChildAdapterPosition(view);
        CardPosition cardPosition = new CardPosition(this.cardPositionPredicate, childAdapterPosition);
        PreferenceGroupAdapter preferenceGroupAdapter = this.adapter;
        Preference item = preferenceGroupAdapter != null ? preferenceGroupAdapter.getItem(childAdapterPosition) : null;
        if (item instanceof COUICardEntrancePreference) {
            int cardType = ((COUICardEntrancePreference) item).getCardType();
            if (cardType == 1) {
                setCardColumnMargin(outRect, this.smallCardMargin, cardPosition);
            } else {
                if (cardType != 2) {
                    return;
                }
                setCardColumnMargin(outRect, this.largeCardMargin, cardPosition);
            }
        }
    }

    public final CardMargin getLargeCardMargin() {
        return this.largeCardMargin;
    }

    public final CardMargin getSmallCardMargin() {
        return this.smallCardMargin;
    }

    public final void setAdapter(PreferenceGroupAdapter preferenceGroupAdapter) {
        this.adapter = preferenceGroupAdapter;
    }

    public final void setLargeCardMargin(CardMargin cardMargin) {
        Intrinsics.checkNotNullParameter(cardMargin, "<set-?>");
        this.largeCardMargin = cardMargin;
    }

    public final void setSmallCardMargin(CardMargin cardMargin) {
        Intrinsics.checkNotNullParameter(cardMargin, "<set-?>");
        this.smallCardMargin = cardMargin;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CardEntranceDecoration(Context appContext, CardPositionPredicate cardPositionPredicate, PreferenceGroupAdapter preferenceGroupAdapter) {
        super(appContext);
        Intrinsics.checkNotNullParameter(appContext, "appContext");
        Intrinsics.checkNotNullParameter(cardPositionPredicate, "cardPositionPredicate");
        this.cardPositionPredicate = cardPositionPredicate;
        this.adapter = preferenceGroupAdapter;
        this.largeCardMargin = getDefaultLargeCardMargin();
        this.smallCardMargin = getDefaultSmallCardMargin();
    }
}