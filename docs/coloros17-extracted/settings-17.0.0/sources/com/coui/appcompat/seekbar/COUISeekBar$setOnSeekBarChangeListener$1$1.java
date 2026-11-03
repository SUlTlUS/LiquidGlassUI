package com.coui.appcompat.seekbar;

import com.coui.appcompat.seekbar.core.COUISeekBarCore;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0016J\u0010\u0010\n\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u000b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\f"}, d2 = {"com/coui/appcompat/seekbar/COUISeekBar$setOnSeekBarChangeListener$1$1", "Lcom/coui/appcompat/seekbar/core/COUISeekBarCore$OnSeekBarChangeListener;", "onProgressChanged", "", "seekBar", "Lcom/coui/appcompat/seekbar/core/COUISeekBarCore;", "progress", "", "fromUser", "", "onStartTrackingTouch", "onStopTrackingTouch", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class COUISeekBar$setOnSeekBarChangeListener$1$1 implements COUISeekBarCore.OnSeekBarChangeListener {
    final /* synthetic */ COUISeekBar.OnSeekBarChangeListener $l;

    public COUISeekBar$setOnSeekBarChangeListener$1$1(COUISeekBar.OnSeekBarChangeListener onSeekBarChangeListener) {
        this.$l = onSeekBarChangeListener;
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore.OnSeekBarChangeListener
    public void onProgressChanged(COUISeekBarCore seekBar, int progress, boolean fromUser) {
        Intrinsics.checkNotNullParameter(seekBar, "seekBar");
        this.$l.onProgressChanged((COUISeekBar) seekBar, progress, fromUser);
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore.OnSeekBarChangeListener
    public void onStartTrackingTouch(COUISeekBarCore seekBar) {
        Intrinsics.checkNotNullParameter(seekBar, "seekBar");
        this.$l.onStartTrackingTouch((COUISeekBar) seekBar);
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore.OnSeekBarChangeListener
    public void onStopTrackingTouch(COUISeekBarCore seekBar) {
        Intrinsics.checkNotNullParameter(seekBar, "seekBar");
        this.$l.onStopTrackingTouch((COUISeekBar) seekBar);
    }
}