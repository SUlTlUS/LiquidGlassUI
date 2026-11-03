package com.coui.appcompat.seekbar.effect;

import kotlin.Metadata;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\b¨\u0006\f"}, d2 = {"Lcom/coui/appcompat/seekbar/effect/LoadingEffectParams;", "Lcom/coui/appcompat/seekbar/effect/EffectParams;", "()V", "gradientLengthRatio", "", "getGradientLengthRatio", "()F", "setGradientLengthRatio", "(F)V", "gradientMidAlpha", "getGradientMidAlpha", "setGradientMidAlpha", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class LoadingEffectParams extends EffectParams {
    private float gradientLengthRatio = 0.6f;
    private float gradientMidAlpha = 0.6f;

    public final float getGradientLengthRatio() {
        return this.gradientLengthRatio;
    }

    public final float getGradientMidAlpha() {
        return this.gradientMidAlpha;
    }

    public final void setGradientLengthRatio(float f3) {
        this.gradientLengthRatio = f3;
    }

    public final void setGradientMidAlpha(float f3) {
        this.gradientMidAlpha = f3;
    }
}