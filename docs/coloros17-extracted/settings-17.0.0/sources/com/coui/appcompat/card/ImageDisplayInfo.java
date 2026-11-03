package com.coui.appcompat.card;

import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\r\n\u0002\b\u0006\u0018\u00002\u00020\u0001B#\b\u0016\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003¢\u0006\u0002\u0010\u0007B#\b\u0016\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00060\t¢\u0006\u0002\u0010\nB'\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\b\b\u0002\u0010\r\u001a\u00020\f¢\u0006\u0002\u0010\u000eR\u0019\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\n\n\u0002\u0010\u0011\u001a\u0004\b\u000f\u0010\u0010¨\u0006\u0012"}, d2 = {"Lcom/coui/appcompat/card/ImageDisplayInfo;", "Lcom/coui/appcompat/card/BaseDisplayInfo;", "imageResources", "", "", "choices", "", "([Ljava/lang/Integer;[Ljava/lang/String;)V", "animTitles", "", "([Ljava/lang/Integer;Ljava/util/List;)V", "title", "", "summary", "([Ljava/lang/Integer;Ljava/lang/CharSequence;Ljava/lang/CharSequence;)V", "getImageResources", "()[Ljava/lang/Integer;", "[Ljava/lang/Integer;", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ImageDisplayInfo extends BaseDisplayInfo {
    private final Integer[] imageResources;

    public /* synthetic */ ImageDisplayInfo(Integer[] numArr, String str, String str2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(numArr, (i3 & 2) != 0 ? "" : str, (i3 & 4) != 0 ? "" : str2);
    }

    public final Integer[] getImageResources() {
        return this.imageResources;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ImageDisplayInfo(Integer[] imageResources, CharSequence title, CharSequence summary) {
        super(title, summary);
        Intrinsics.checkNotNullParameter(imageResources, "imageResources");
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(summary, "summary");
        this.imageResources = imageResources;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public ImageDisplayInfo(Integer[] imageResources, String[] choices) {
        this(imageResources, "", "");
        Intrinsics.checkNotNullParameter(imageResources, "imageResources");
        Intrinsics.checkNotNullParameter(choices, "choices");
        setChoices(choices);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public ImageDisplayInfo(Integer[] imageResources, List<String> animTitles) {
        this(imageResources, "", "");
        Intrinsics.checkNotNullParameter(imageResources, "imageResources");
        Intrinsics.checkNotNullParameter(animTitles, "animTitles");
        getAnimTitles().clear();
        getAnimTitles().addAll(animTitles);
    }
}