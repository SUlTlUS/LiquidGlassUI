package com.coui.appcompat.seekbar.effect;

import android.graphics.Canvas;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.exifinterface.media.ExifInterface;
import com.coui.appcompat.seekbar.core.SeekBarState;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\u0005J\u0006\u0010\t\u001a\u00020\u0007J\u001e\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010J&\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012J\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00050\u0014J%\u0010\u0015\u001a\u0004\u0018\u0001H\u0016\"\b\b\u0000\u0010\u0016*\u00020\u00052\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u0002H\u00160\u0018¢\u0006\u0002\u0010\u0019J\u0010\u0010\u001a\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\u0005R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001b"}, d2 = {"Lcom/coui/appcompat/seekbar/effect/EffectChainManager;", "", "()V", "mEffectRenderers", "", "Lcom/coui/appcompat/seekbar/effect/IEffectRenderer;", "addEffect", "", "effect", "clearEffects", "drawEffects", "canvas", "Landroid/graphics/Canvas;", "state", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "progressPosition", "", TypedValues.CycleType.S_WAVE_PHASE, "Lcom/coui/appcompat/seekbar/effect/DrawPhase;", "getAllEffects", "", "getEffect", ExifInterface.GPS_DIRECTION_TRUE, "effectClass", "Ljava/lang/Class;", "(Ljava/lang/Class;)Lcom/coui/appcompat/seekbar/effect/IEffectRenderer;", "removeEffect", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class EffectChainManager {
    private final List<IEffectRenderer> mEffectRenderers = new ArrayList();

    public final void addEffect(IEffectRenderer effect) {
        if (effect == null || this.mEffectRenderers.contains(effect)) {
            return;
        }
        this.mEffectRenderers.add(effect);
    }

    public final void clearEffects() {
        this.mEffectRenderers.clear();
    }

    public final void drawEffects(Canvas canvas, SeekBarState state, float progressPosition, DrawPhase phase) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        Intrinsics.checkNotNullParameter(state, "state");
        Intrinsics.checkNotNullParameter(phase, "phase");
        for (IEffectRenderer iEffectRenderer : this.mEffectRenderers) {
            if (iEffectRenderer.getMEnabled() && iEffectRenderer.getDrawPhase() == phase) {
                iEffectRenderer.drawEffect(canvas, state, progressPosition);
            }
        }
    }

    public final List<IEffectRenderer> getAllEffects() {
        return new ArrayList(this.mEffectRenderers);
    }

    public final <T extends IEffectRenderer> T getEffect(Class<T> effectClass) {
        Intrinsics.checkNotNullParameter(effectClass, "effectClass");
        Iterator<IEffectRenderer> it = this.mEffectRenderers.iterator();
        while (it.hasNext()) {
            T t2 = (T) it.next();
            if (effectClass.isInstance(t2)) {
                Intrinsics.checkNotNull(t2, "null cannot be cast to non-null type T of com.coui.appcompat.seekbar.effect.EffectChainManager.getEffect");
                return t2;
            }
        }
        return null;
    }

    public final void removeEffect(IEffectRenderer effect) {
        TypeIntrinsics.asMutableCollection(this.mEffectRenderers).remove(effect);
    }

    public final void drawEffects(Canvas canvas, SeekBarState state, float progressPosition) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        Intrinsics.checkNotNullParameter(state, "state");
        drawEffects(canvas, state, progressPosition, DrawPhase.POST_DRAW);
    }
}