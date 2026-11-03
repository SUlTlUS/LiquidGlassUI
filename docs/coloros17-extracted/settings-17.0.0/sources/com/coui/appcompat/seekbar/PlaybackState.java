package com.coui.appcompat.seekbar;

import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007¨\u0006\b"}, d2 = {"Lcom/coui/appcompat/seekbar/PlaybackState;", "", "(Ljava/lang/String;I)V", "IDLE", "LOADING_INITIAL", "PLAYING", "PAUSED", "LOADING_BUFFERING", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public enum PlaybackState {
    IDLE,
    LOADING_INITIAL,
    PLAYING,
    PAUSED,
    LOADING_BUFFERING;

    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

    public static EnumEntries<PlaybackState> getEntries() {
        return $ENTRIES;
    }
}