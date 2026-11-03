package com.coui.appcompat.seekbar.effect;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import kotlin.Metadata;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0005\b\u0016\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0010X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014¨\u0006\u0015"}, d2 = {"Lcom/coui/appcompat/seekbar/effect/EffectParams;", "", "()V", TypedValues.Custom.S_COLOR, "", "getColor", "()I", "setColor", "(I)V", "enabled", "", "getEnabled", "()Z", "setEnabled", "(Z)V", "intensity", "", "getIntensity", "()F", "setIntensity", "(F)V", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class EffectParams {
    private boolean enabled = true;
    private int color = -1;
    private float intensity = 1.0f;

    public int getColor() {
        return this.color;
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public float getIntensity() {
        return this.intensity;
    }

    public void setColor(int i3) {
        this.color = i3;
    }

    public void setEnabled(boolean z2) {
        this.enabled = z2;
    }

    public void setIntensity(float f3) {
        this.intensity = f3;
    }
}