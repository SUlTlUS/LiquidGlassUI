package com.coui.appcompat.seekbar.core;

import androidx.slice.core.SliceHints;
import kotlin.Metadata;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0006\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\b\u0010\u0004\u001a\u00020\u0003H&J\b\u0010\u0005\u001a\u00020\u0003H&J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0003H&J\u0010\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u0003H&J\u0010\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0003H&¨\u0006\r"}, d2 = {"Lcom/coui/appcompat/seekbar/core/ICOUISeekBar;", "", "getMax", "", "getMin", "getProgress", "setMax", "", "max", "setMin", SliceHints.SUBTYPE_MIN, "setProgress", "progress", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface ICOUISeekBar {
    int getMax();

    int getMin();

    int getProgress();

    void setMax(int max);

    void setMin(int min);

    void setProgress(int progress);
}