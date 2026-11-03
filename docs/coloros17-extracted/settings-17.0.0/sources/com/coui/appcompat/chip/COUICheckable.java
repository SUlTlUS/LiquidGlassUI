package com.coui.appcompat.chip;

import android.widget.Checkable;
import com.coui.appcompat.chip.COUICheckable;

/* JADX INFO: loaded from: classes.dex */
public interface COUICheckable<T extends COUICheckable<T>> extends Checkable {

    /* JADX INFO: loaded from: classes7.dex */
    public interface OnCheckedChangeListener<C> {
        void onCheckedChanged(C c3, boolean z2);
    }

    int getId();

    void setInternalOnCheckedChangeListener(OnCheckedChangeListener<T> onCheckedChangeListener);
}