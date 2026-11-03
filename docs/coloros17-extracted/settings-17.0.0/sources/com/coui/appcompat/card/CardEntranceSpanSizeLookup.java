package com.coui.appcompat.card;

import androidx.preference.Preference;
import androidx.preference.PreferenceGroupAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u0000 \u000b2\u00020\u0001:\u0001\u000bB\u0011\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tH\u0017R\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\u0004¨\u0006\f"}, d2 = {"Lcom/coui/appcompat/card/CardEntranceSpanSizeLookup;", "Landroidx/recyclerview/widget/GridLayoutManager$SpanSizeLookup;", "adapter", "Landroidx/preference/PreferenceGroupAdapter;", "(Landroidx/preference/PreferenceGroupAdapter;)V", "getAdapter", "()Landroidx/preference/PreferenceGroupAdapter;", "setAdapter", "getSpanSize", "", "position", "Companion", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class CardEntranceSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    public static final int SPAN_COUNT_CARD_TYPE_LARGE = 2;
    public static final int SPAN_COUNT_CARD_TYPE_SMALL = 1;
    private PreferenceGroupAdapter adapter;

    public CardEntranceSpanSizeLookup() {
        this(null, 1, null);
    }

    public final PreferenceGroupAdapter getAdapter() {
        return this.adapter;
    }

    @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
    public int getSpanSize(int position) {
        PreferenceGroupAdapter preferenceGroupAdapter = this.adapter;
        Preference item = preferenceGroupAdapter != null ? preferenceGroupAdapter.getItem(position) : null;
        if (!(item instanceof COUICardEntrancePreference)) {
            return 2;
        }
        int cardType = ((COUICardEntrancePreference) item).getCardType();
        return (cardType == 1 || cardType != 2) ? 1 : 2;
    }

    public final void setAdapter(PreferenceGroupAdapter preferenceGroupAdapter) {
        this.adapter = preferenceGroupAdapter;
    }

    public /* synthetic */ CardEntranceSpanSizeLookup(PreferenceGroupAdapter preferenceGroupAdapter, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? null : preferenceGroupAdapter);
    }

    public CardEntranceSpanSizeLookup(PreferenceGroupAdapter preferenceGroupAdapter) {
        this.adapter = preferenceGroupAdapter;
    }
}