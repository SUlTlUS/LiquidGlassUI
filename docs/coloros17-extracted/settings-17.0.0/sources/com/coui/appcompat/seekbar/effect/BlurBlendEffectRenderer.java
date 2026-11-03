package com.coui.appcompat.seekbar.effect;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.view.View;
import com.coui.appcompat.COUIMaterialEffect;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.seekbar.core.SeekBarState;
import com.oplus.view.material.OplusMaterialBaseParams;
import com.oplus.view.material.OplusMaterialCornerParams;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 $2\u00020\u0001:\u0001$B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\r\u001a\u00020\u000eH\u0002J\u0006\u0010\u000f\u001a\u00020\u000eJ \u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\nH\u0016J\b\u0010\u0016\u001a\u00020\fH\u0016J\u0006\u0010\u0017\u001a\u00020\bJ\b\u0010\u0018\u001a\u00020\bH\u0016J\u0010\u0010\u0019\u001a\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\fH\u0016J\b\u0010\u001b\u001a\u00020\u000eH\u0002J\u0018\u0010\u001c\u001a\u00020\b2\u0006\u0010\u001d\u001a\u00020\b2\u0006\u0010\u001e\u001a\u00020\bH\u0002J\u0016\u0010\u001f\u001a\u00020\u000e2\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\nJ\u0018\u0010#\u001a\u00020\u000e2\u0006\u0010\u001d\u001a\u00020\b2\b\b\u0002\u0010\u001e\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006%"}, d2 = {"Lcom/coui/appcompat/seekbar/effect/BlurBlendEffectRenderer;", "Lcom/coui/appcompat/seekbar/effect/IEffectRenderer;", "hostView", "Landroid/view/View;", "(Landroid/view/View;)V", "mBaseRect", "Landroid/graphics/Rect;", "mEnabled", "", "mLastBgRadius", "", "mParams", "Lcom/coui/appcompat/seekbar/effect/EffectParams;", "clearBlurBlend", "", "clearOnDetach", "drawEffect", "canvas", "Landroid/graphics/Canvas;", "state", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "progressPosition", "getEffectParams", "isBlurBlendActive", "isEnabled", "setEffectParams", "params", "setupBlurBlend", "shouldEnableBlurBlend", "followSystemTheme", "hasCustomUserColors", "updateBaseParams", "backgroundRect", "Landroid/graphics/RectF;", "bgRadius", "updateState", "Companion", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class BlurBlendEffectRenderer implements IEffectRenderer {
    private static final float BLUR_RADIUS = 30.0f;
    private static final String LAYER1_COLOR = "#66333333";
    private static final String LAYER2_COLOR = "#4DFFFFFF";
    private final View hostView;
    private final Rect mBaseRect;
    private boolean mEnabled;
    private float mLastBgRadius;
    private EffectParams mParams;

    public BlurBlendEffectRenderer(View hostView) {
        Intrinsics.checkNotNullParameter(hostView, "hostView");
        this.hostView = hostView;
        this.mBaseRect = new Rect();
        this.mParams = new EffectParams();
        this.mLastBgRadius = -1.0f;
    }

    private final void clearBlurBlend() {
        COUIMaterialEffect.setBackgroundRenderEffect(null, this.hostView);
    }

    private final void setupBlurBlend() {
        RenderEffect renderEffectCreateBlurEffect = RenderEffect.createBlurEffect(30.0f, 30.0f, Shader.TileMode.MIRROR);
        Intrinsics.checkNotNullExpressionValue(renderEffectCreateBlurEffect, "createBlurEffect(...)");
        RenderEffect renderEffectCreateColorFilterEffect = RenderEffect.createColorFilterEffect(new BlendModeColorFilter(Color.parseColor(LAYER1_COLOR), BlendMode.LIGHTEN), renderEffectCreateBlurEffect);
        Intrinsics.checkNotNullExpressionValue(renderEffectCreateColorFilterEffect, "createColorFilterEffect(...)");
        RenderEffect renderEffectCreateColorFilterEffect2 = RenderEffect.createColorFilterEffect(new BlendModeColorFilter(Color.parseColor(LAYER2_COLOR), BlendMode.SOFT_LIGHT), renderEffectCreateColorFilterEffect);
        Intrinsics.checkNotNullExpressionValue(renderEffectCreateColorFilterEffect2, "createColorFilterEffect(...)");
        COUIMaterialEffect.setBackgroundRenderEffect(renderEffectCreateColorFilterEffect2, this.hostView);
    }

    private final boolean shouldEnableBlurBlend(boolean followSystemTheme, boolean hasCustomUserColors) {
        if (followSystemTheme || hasCustomUserColors) {
            return false;
        }
        COUIMaterialStateManager cOUIMaterialStateManager = COUIMaterialStateManager.INSTANCE;
        return !cOUIMaterialStateManager.isLowAnimationLevel() && cOUIMaterialStateManager.materialVersionCheck();
    }

    public static /* synthetic */ void updateState$default(BlurBlendEffectRenderer blurBlendEffectRenderer, boolean z2, boolean z3, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            z3 = false;
        }
        blurBlendEffectRenderer.updateState(z2, z3);
    }

    public final void clearOnDetach() {
        if (this.mEnabled) {
            clearBlurBlend();
            this.mEnabled = false;
        }
        this.mLastBgRadius = -1.0f;
        this.mBaseRect.setEmpty();
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    public void drawEffect(Canvas canvas, SeekBarState state, float progressPosition) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        Intrinsics.checkNotNullParameter(state, "state");
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    public DrawPhase getDrawPhase() {
        return IEffectRenderer.DefaultImpls.getDrawPhase(this);
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    /* JADX INFO: renamed from: getEffectParams, reason: from getter */
    public EffectParams getMParams() {
        return this.mParams;
    }

    /* JADX INFO: renamed from: isBlurBlendActive, reason: from getter */
    public final boolean getMEnabled() {
        return this.mEnabled;
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    /* JADX INFO: renamed from: isEnabled */
    public boolean getMEnabled() {
        return this.mEnabled;
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    public void setEffectParams(EffectParams params) {
        Intrinsics.checkNotNullParameter(params, "params");
        this.mParams = params;
    }

    public final void updateBaseParams(RectF backgroundRect, float bgRadius) {
        Intrinsics.checkNotNullParameter(backgroundRect, "backgroundRect");
        int i3 = (int) backgroundRect.left;
        int i4 = (int) backgroundRect.top;
        int i5 = (int) backgroundRect.right;
        int i6 = (int) backgroundRect.bottom;
        Rect rect = this.mBaseRect;
        boolean z2 = (rect.left == i3 && rect.top == i4 && rect.right == i5 && rect.bottom == i6) ? false : true;
        boolean z3 = this.mLastBgRadius == bgRadius;
        if (z2 || !z3) {
            if (z2) {
                rect.set(i3, i4, i5, i6);
                COUIMaterialEffect.setBaseParams(this.hostView, new OplusMaterialBaseParams(0, this.mBaseRect));
            }
            if (z3) {
                return;
            }
            this.mLastBgRadius = bgRadius;
            COUIMaterialEffect.setCornerParams(this.hostView, new OplusMaterialCornerParams(bgRadius, 1.0f));
        }
    }

    public final void updateState(boolean followSystemTheme, boolean hasCustomUserColors) {
        boolean zShouldEnableBlurBlend = shouldEnableBlurBlend(followSystemTheme, hasCustomUserColors);
        if (zShouldEnableBlurBlend == this.mEnabled) {
            return;
        }
        if (zShouldEnableBlurBlend) {
            setupBlurBlend();
        } else {
            clearBlurBlend();
        }
        this.mEnabled = zShouldEnableBlurBlend;
    }
}