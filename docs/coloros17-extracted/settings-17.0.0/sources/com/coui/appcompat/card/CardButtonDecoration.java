package com.coui.appcompat.card;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import androidx.preference.PreferenceGroupAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.support.card.R;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\bJ\b\u0010\u0013\u001a\u00020\u000eH\u0002J(\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dH\u0017R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u00020\u000eX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001e"}, d2 = {"Lcom/coui/appcompat/card/CardButtonDecoration;", "Lcom/coui/appcompat/card/BaseCardItemDecoration;", "appContext", "Landroid/content/Context;", "cardPositionPredicate", "Lcom/coui/appcompat/card/CardPositionPredicate;", "adapter", "Landroidx/preference/PreferenceGroupAdapter;", "(Landroid/content/Context;Lcom/coui/appcompat/card/CardPositionPredicate;Landroidx/preference/PreferenceGroupAdapter;)V", "getAdapter", "()Landroidx/preference/PreferenceGroupAdapter;", "setAdapter", "(Landroidx/preference/PreferenceGroupAdapter;)V", "cardMargin", "Lcom/coui/appcompat/card/CardMargin;", "getCardMargin", "()Lcom/coui/appcompat/card/CardMargin;", "setCardMargin", "(Lcom/coui/appcompat/card/CardMargin;)V", "getDefaultCardMargin", "getItemOffsets", "", "outRect", "Landroid/graphics/Rect;", "view", "Landroid/view/View;", "parent", "Landroidx/recyclerview/widget/RecyclerView;", "state", "Landroidx/recyclerview/widget/RecyclerView$State;", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class CardButtonDecoration extends BaseCardItemDecoration {
    private PreferenceGroupAdapter adapter;
    private CardMargin cardMargin;
    private final CardPositionPredicate cardPositionPredicate;

    public /* synthetic */ CardButtonDecoration(Context context, CardPositionPredicate cardPositionPredicate, PreferenceGroupAdapter preferenceGroupAdapter, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, cardPositionPredicate, (i3 & 4) != 0 ? null : preferenceGroupAdapter);
    }

    private final CardMargin getDefaultCardMargin() {
        int dimenPx = getDimenPx(R.dimen.coui_component_card_button_first_top_margin);
        int i3 = R.dimen.coui_component_card_button_horizontal_margin_inner;
        return new CardMargin(0, dimenPx, getDimenPx(i3), getDimenPx(i3));
    }

    public final PreferenceGroupAdapter getAdapter() {
        return this.adapter;
    }

    public final CardMargin getCardMargin() {
        return this.cardMargin;
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
        if ((preferenceGroupAdapter != null ? preferenceGroupAdapter.getItem(childAdapterPosition) : null) instanceof COUICardButtonPreference) {
            setCardColumnMargin(outRect, this.cardMargin, cardPosition);
        }
    }

    public final void setAdapter(PreferenceGroupAdapter preferenceGroupAdapter) {
        this.adapter = preferenceGroupAdapter;
    }

    public final void setCardMargin(CardMargin cardMargin) {
        Intrinsics.checkNotNullParameter(cardMargin, "<set-?>");
        this.cardMargin = cardMargin;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CardButtonDecoration(Context appContext, CardPositionPredicate cardPositionPredicate, PreferenceGroupAdapter preferenceGroupAdapter) {
        super(appContext);
        Intrinsics.checkNotNullParameter(appContext, "appContext");
        Intrinsics.checkNotNullParameter(cardPositionPredicate, "cardPositionPredicate");
        this.cardPositionPredicate = cardPositionPredicate;
        this.adapter = preferenceGroupAdapter;
        this.cardMargin = getDefaultCardMargin();
    }
}