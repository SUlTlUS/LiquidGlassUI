package com.coui.appcompat.seekbar.effect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import androidx.core.app.NotificationCompat;
import com.coui.appcompat.seekbar.core.COUISeekBarCore;
import com.coui.appcompat.seekbar.core.SeekBarState;
import com.coui.appcompat.spotlight.COUISpotLightEffect;
import com.coui.appcompat.spotlight.COUISpotLightEffectDrawable;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J \u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\u000e\u0010\u001a\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015J\b\u0010\u001b\u001a\u00020\u0013H\u0002J\b\u0010\u001c\u001a\u00020\u001dH\u0016J\b\u0010\u001e\u001a\u00020\u000eH\u0016J\b\u0010\u001f\u001a\u00020\nH\u0016J\u000e\u0010 \u001a\u00020\u00132\u0006\u0010!\u001a\u00020\u0006J\u0006\u0010\"\u001a\u00020\u0013J\u0016\u0010#\u001a\u00020\u00132\u0006\u0010$\u001a\u00020\f2\u0006\u0010%\u001a\u00020\fJ\u000e\u0010&\u001a\u00020\u00132\u0006\u0010'\u001a\u00020(J\u0010\u0010)\u001a\u00020\u00132\u0006\u0010*\u001a\u00020\u000eH\u0016J\u000e\u0010+\u001a\u00020\u00132\u0006\u0010,\u001a\u00020\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006-"}, d2 = {"Lcom/coui/appcompat/seekbar/effect/SpotLightEffectRenderer;", "Lcom/coui/appcompat/seekbar/effect/IEffectRenderer;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "mCallback", "Landroid/graphics/drawable/Drawable$Callback;", "mClipPath", "Landroid/graphics/Path;", "mEnabled", "", "mHeight", "", "mParams", "Lcom/coui/appcompat/seekbar/effect/EffectParams;", "mSpotLight", "Lcom/coui/appcompat/spotlight/COUISpotLightEffectDrawable;", "mWidth", "drawEffect", "", "canvas", "Landroid/graphics/Canvas;", "state", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "progressPosition", "", "drawSpotLight", "ensureSpotLightDrawable", "getDrawPhase", "Lcom/coui/appcompat/seekbar/effect/DrawPhase;", "getEffectParams", "isEnabled", "onAttachedToWindow", "callback", "onDetachedFromWindow", "onSizeChanged", "w", "h", "onTouchEvent", NotificationCompat.CATEGORY_EVENT, "Landroid/view/MotionEvent;", "setEffectParams", "params", "setEnabled", "enabled", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class SpotLightEffectRenderer implements IEffectRenderer {
    private final Context context;
    private Drawable.Callback mCallback;
    private final Path mClipPath;
    private boolean mEnabled;
    private int mHeight;
    private EffectParams mParams;
    private COUISpotLightEffectDrawable mSpotLight;
    private int mWidth;

    public SpotLightEffectRenderer(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
        this.mParams = new EffectParams();
        this.mClipPath = new Path();
    }

    private final void ensureSpotLightDrawable() {
        int i3;
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLight;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setCallback(this.mCallback);
            return;
        }
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable2 = new COUISpotLightEffectDrawable();
        cOUISpotLightEffectDrawable2.setSpotLightType(this.context, COUISpotLightEffect.SpotLightType.TYPE_OPAQUE_MEDIUM_1);
        int i4 = this.mWidth;
        if (i4 > 0 && (i3 = this.mHeight) > 0) {
            cOUISpotLightEffectDrawable2.setBounds(0, 0, i4, i3);
        }
        Drawable.Callback callback = this.mCallback;
        if (callback != null) {
            cOUISpotLightEffectDrawable2.setCallback(callback);
        }
        this.mSpotLight = cOUISpotLightEffectDrawable2;
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    public void drawEffect(Canvas canvas, SeekBarState state, float progressPosition) {
        COUISeekBarCore view;
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        Intrinsics.checkNotNullParameter(state, "state");
        if (this.mEnabled && (view = state.getView()) != null) {
            RectF mBackgroundRect = view.getMBackgroundRect();
            if (mBackgroundRect.isEmpty()) {
                return;
            }
            float backgroundDrawRadius = view.getBackgroundDrawRadius();
            canvas.save();
            this.mClipPath.reset();
            this.mClipPath.addRoundRect(mBackgroundRect, backgroundDrawRadius, backgroundDrawRadius, Path.Direction.CW);
            canvas.clipPath(this.mClipPath);
            drawSpotLight(canvas);
            canvas.restore();
        }
    }

    public final void drawSpotLight(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLight;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.draw(canvas);
        }
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    public DrawPhase getDrawPhase() {
        return DrawPhase.OVERLAY;
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    /* JADX INFO: renamed from: getEffectParams, reason: from getter */
    public EffectParams getMParams() {
        return this.mParams;
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    /* JADX INFO: renamed from: isEnabled, reason: from getter */
    public boolean getMEnabled() {
        return this.mEnabled;
    }

    public final void onAttachedToWindow(Drawable.Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        this.mCallback = callback;
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLight;
        if (cOUISpotLightEffectDrawable == null) {
            return;
        }
        cOUISpotLightEffectDrawable.setCallback(callback);
    }

    public final void onDetachedFromWindow() {
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLight;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setCallback(null);
        }
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable2 = this.mSpotLight;
        if (cOUISpotLightEffectDrawable2 != null) {
            cOUISpotLightEffectDrawable2.cancel();
        }
        this.mCallback = null;
    }

    public final void onSizeChanged(int w2, int h3) {
        this.mWidth = w2;
        this.mHeight = h3;
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLight;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setBounds(0, 0, w2, h3);
        }
    }

    public final void onTouchEvent(MotionEvent event) {
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable;
        Intrinsics.checkNotNullParameter(event, "event");
        if (this.mEnabled && (cOUISpotLightEffectDrawable = this.mSpotLight) != null) {
            cOUISpotLightEffectDrawable.onTouchEvent(event);
        }
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    public void setEffectParams(EffectParams params) {
        Intrinsics.checkNotNullParameter(params, "params");
        this.mParams = params;
    }

    public final void setEnabled(boolean enabled) {
        if (this.mEnabled == enabled) {
            return;
        }
        this.mEnabled = enabled;
        if (enabled) {
            ensureSpotLightDrawable();
            return;
        }
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLight;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.cancel();
        }
    }
}