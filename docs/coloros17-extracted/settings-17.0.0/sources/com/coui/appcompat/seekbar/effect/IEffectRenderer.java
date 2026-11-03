package com.coui.appcompat.seekbar.effect;

import android.graphics.Canvas;
import com.coui.appcompat.seekbar.core.SeekBarState;
import kotlin.Metadata;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH&J\b\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\rH&J\b\u0010\u000e\u001a\u00020\u000fH&J\u0010\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\rH&¨\u0006\u0012"}, d2 = {"Lcom/coui/appcompat/seekbar/effect/IEffectRenderer;", "", "drawEffect", "", "canvas", "Landroid/graphics/Canvas;", "state", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "progressPosition", "", "getDrawPhase", "Lcom/coui/appcompat/seekbar/effect/DrawPhase;", "getEffectParams", "Lcom/coui/appcompat/seekbar/effect/EffectParams;", "isEnabled", "", "setEffectParams", "params", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface IEffectRenderer {

    @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class DefaultImpls {
        public static DrawPhase getDrawPhase(IEffectRenderer iEffectRenderer) {
            return DrawPhase.POST_DRAW;
        }
    }

    void drawEffect(Canvas canvas, SeekBarState state, float progressPosition);

    DrawPhase getDrawPhase();

    /* JADX INFO: renamed from: getEffectParams */
    EffectParams getMParams();

    /* JADX INFO: renamed from: isEnabled */
    boolean getMEnabled();

    void setEffectParams(EffectParams params);
}