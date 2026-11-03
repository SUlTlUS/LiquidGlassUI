package com.coui.appcompat.seekbar.effect;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import kotlin.Metadata;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u0007\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\f\"\u0004\b\u0011\u0010\u000eR\u001a\u0010\u0012\u001a\u00020\u0013X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017¨\u0006\u0018"}, d2 = {"Lcom/coui/appcompat/seekbar/effect/GlitterEffectParams;", "Lcom/coui/appcompat/seekbar/effect/EffectParams;", "()V", TypedValues.TransitionType.S_DURATION, "", "getDuration", "()J", "setDuration", "(J)V", "maxColor", "", "getMaxColor", "()I", "setMaxColor", "(I)V", "minColor", "getMinColor", "setMinColor", "scale", "", "getScale", "()F", "setScale", "(F)V", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class GlitterEffectParams extends EffectParams {
    private int minColor = 16777215;
    private int maxColor = -1593835521;
    private float scale = 1.0f;
    private long duration = 1000;

    public final long getDuration() {
        return this.duration;
    }

    public final int getMaxColor() {
        return this.maxColor;
    }

    public final int getMinColor() {
        return this.minColor;
    }

    public final float getScale() {
        return this.scale;
    }

    public final void setDuration(long j3) {
        this.duration = j3;
    }

    public final void setMaxColor(int i3) {
        this.maxColor = i3;
    }

    public final void setMinColor(int i3) {
        this.minColor = i3;
    }

    public final void setScale(float f3) {
        this.scale = f3;
    }
}