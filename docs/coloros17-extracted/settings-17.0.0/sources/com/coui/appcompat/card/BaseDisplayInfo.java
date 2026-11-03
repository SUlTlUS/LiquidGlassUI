package com.coui.appcompat.card;

import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\r\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010!\n\u0002\u0010\u000e\n\u0002\b\u0011\n\u0002\u0010\u0002\n\u0002\u0010\u0011\n\u0002\b\u0004\b\u0016\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\u0019\u0010\u001f\u001a\u00020 2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u000e0!¢\u0006\u0002\u0010\"J\u000e\u0010#\u001a\u00020 2\u0006\u0010$\u001a\u00020\u0007R\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\t\"\u0004\b\u0013\u0010\u000bR\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u000e0\r¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0010R\u001a\u0010\u0016\u001a\u00020\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\t\"\u0004\b\u0018\u0010\u000bR\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001a\"\u0004\b\u001e\u0010\u001c¨\u0006%"}, d2 = {"Lcom/coui/appcompat/card/BaseDisplayInfo;", "", "title", "", "summary", "(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)V", "animHeight", "", "getAnimHeight", "()I", "setAnimHeight", "(I)V", "animTitles", "", "", "getAnimTitles", "()Ljava/util/List;", "animWidth", "getAnimWidth", "setAnimWidth", "choices", "getChoices", "selectedIndex", "getSelectedIndex", "setSelectedIndex", "getSummary", "()Ljava/lang/CharSequence;", "setSummary", "(Ljava/lang/CharSequence;)V", "getTitle", "setTitle", "setChoices", "", "", "([Ljava/lang/String;)V", "setSelectedChoice", "index", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class BaseDisplayInfo {
    private int animHeight;
    private final List<String> animTitles;
    private int animWidth;
    private final List<String> choices;
    private int selectedIndex;
    private CharSequence summary;
    private CharSequence title;

    public BaseDisplayInfo(CharSequence title, CharSequence summary) {
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(summary, "summary");
        this.title = title;
        this.summary = summary;
        this.choices = new ArrayList();
        this.animTitles = new ArrayList();
    }

    public final int getAnimHeight() {
        return this.animHeight;
    }

    public final List<String> getAnimTitles() {
        return this.animTitles;
    }

    public final int getAnimWidth() {
        return this.animWidth;
    }

    public final List<String> getChoices() {
        return this.choices;
    }

    public final int getSelectedIndex() {
        return this.selectedIndex;
    }

    public final CharSequence getSummary() {
        return this.summary;
    }

    public final CharSequence getTitle() {
        return this.title;
    }

    public final void setAnimHeight(int i3) {
        this.animHeight = i3;
    }

    public final void setAnimWidth(int i3) {
        this.animWidth = i3;
    }

    public final void setChoices(String[] choices) {
        Intrinsics.checkNotNullParameter(choices, "choices");
        this.choices.clear();
        CollectionsKt.addAll(this.choices, choices);
    }

    public final void setSelectedChoice(int index) {
        this.selectedIndex = index;
    }

    public final void setSelectedIndex(int i3) {
        this.selectedIndex = i3;
    }

    public final void setSummary(CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(charSequence, "<set-?>");
        this.summary = charSequence;
    }

    public final void setTitle(CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(charSequence, "<set-?>");
        this.title = charSequence;
    }
}