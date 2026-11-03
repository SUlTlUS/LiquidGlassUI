package com.coui.appcompat.card;

import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\r\n\u0002\b\t\n\u0002\u0010!\n\u0002\b\u0004\u0018\u00002\u00020\u0001B-\b\u0016\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0004¢\u0006\u0002\u0010\bB)\b\u0016\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\n¢\u0006\u0002\u0010\fB#\b\u0016\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003¢\u0006\u0002\u0010\u000eB#\b\u0016\u0012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003\u0012\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003¢\u0006\u0002\u0010\u0010B)\b\u0016\u0012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\n¢\u0006\u0002\u0010\u0011B-\b\u0016\u0012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0004¢\u0006\u0002\u0010\u0012B\u0019\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\n¢\u0006\u0002\u0010\u0013R\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00060\u0014¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0014¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0016¨\u0006\u0018"}, d2 = {"Lcom/coui/appcompat/card/AnimDisplayInfo;", "Lcom/coui/appcompat/card/BaseDisplayInfo;", "animResources", "", "", "choices", "", "selectedIndex", "([Ljava/lang/Integer;[Ljava/lang/String;I)V", "title", "", "summary", "([Ljava/lang/Integer;Ljava/lang/CharSequence;Ljava/lang/CharSequence;)V", "animTitles", "([Ljava/lang/Integer;[Ljava/lang/String;)V", "animAssets", "([Ljava/lang/String;[Ljava/lang/String;)V", "([Ljava/lang/String;Ljava/lang/CharSequence;Ljava/lang/CharSequence;)V", "([Ljava/lang/String;[Ljava/lang/String;I)V", "(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)V", "", "getAnimAssets", "()Ljava/util/List;", "getAnimResources", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class AnimDisplayInfo extends BaseDisplayInfo {
    private final List<String> animAssets;
    private final List<Integer> animResources;

    public AnimDisplayInfo() {
        this(null, null, 3, null);
    }

    public final List<String> getAnimAssets() {
        return this.animAssets;
    }

    public final List<Integer> getAnimResources() {
        return this.animResources;
    }

    public /* synthetic */ AnimDisplayInfo(String str, String str2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? "" : str, (i3 & 2) != 0 ? "" : str2);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AnimDisplayInfo(CharSequence title, CharSequence summary) {
        super(title, summary);
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(summary, "summary");
        this.animResources = new ArrayList();
        this.animAssets = new ArrayList();
    }

    public /* synthetic */ AnimDisplayInfo(Integer[] numArr, String[] strArr, int i3, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this(numArr, strArr, (i4 & 4) != 0 ? 0 : i3);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public AnimDisplayInfo(Integer[] animResources, String[] choices, int i3) {
        this("", "");
        Intrinsics.checkNotNullParameter(animResources, "animResources");
        Intrinsics.checkNotNullParameter(choices, "choices");
        this.animResources.clear();
        CollectionsKt.addAll(this.animResources, animResources);
        setChoices(choices);
        setSelectedIndex(i3);
    }

    public /* synthetic */ AnimDisplayInfo(Integer[] numArr, String str, String str2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(numArr, (i3 & 2) != 0 ? "" : str, (i3 & 4) != 0 ? "" : str2);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public AnimDisplayInfo(Integer[] animResources, CharSequence title, CharSequence summary) {
        this(title, summary);
        Intrinsics.checkNotNullParameter(animResources, "animResources");
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(summary, "summary");
        this.animResources.clear();
        CollectionsKt.addAll(this.animResources, animResources);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public AnimDisplayInfo(Integer[] animResources, String[] animTitles) {
        this("", "");
        Intrinsics.checkNotNullParameter(animResources, "animResources");
        Intrinsics.checkNotNullParameter(animTitles, "animTitles");
        this.animResources.clear();
        CollectionsKt.addAll(this.animResources, animResources);
        getAnimTitles().clear();
        CollectionsKt.addAll(getAnimTitles(), animTitles);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public AnimDisplayInfo(String[] animAssets, String[] animTitles) {
        this("", "");
        Intrinsics.checkNotNullParameter(animAssets, "animAssets");
        Intrinsics.checkNotNullParameter(animTitles, "animTitles");
        this.animAssets.clear();
        CollectionsKt.addAll(this.animAssets, animAssets);
        getAnimTitles().clear();
        CollectionsKt.addAll(getAnimTitles(), animTitles);
    }

    public /* synthetic */ AnimDisplayInfo(String[] strArr, String str, String str2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(strArr, (i3 & 2) != 0 ? "" : str, (i3 & 4) != 0 ? "" : str2);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public AnimDisplayInfo(String[] animAssets, CharSequence title, CharSequence summary) {
        this(title, summary);
        Intrinsics.checkNotNullParameter(animAssets, "animAssets");
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(summary, "summary");
        this.animAssets.clear();
        CollectionsKt.addAll(this.animAssets, animAssets);
    }

    public /* synthetic */ AnimDisplayInfo(String[] strArr, String[] strArr2, int i3, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this(strArr, strArr2, (i4 & 4) != 0 ? 0 : i3);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public AnimDisplayInfo(String[] animAssets, String[] choices, int i3) {
        this("", "");
        Intrinsics.checkNotNullParameter(animAssets, "animAssets");
        Intrinsics.checkNotNullParameter(choices, "choices");
        this.animAssets.clear();
        CollectionsKt.addAll(this.animAssets, animAssets);
        setChoices(choices);
        setSelectedIndex(i3);
    }
}