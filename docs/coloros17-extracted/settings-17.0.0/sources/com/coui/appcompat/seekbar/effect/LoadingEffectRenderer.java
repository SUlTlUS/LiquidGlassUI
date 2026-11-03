package com.coui.appcompat.seekbar.effect;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.View;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.seekbar.core.COUISeekBarCore;
import com.coui.appcompat.seekbar.core.SeekBarState;
import com.coui.appcompat.seekbar.olivelink.COUIIntentSeekBarParams;
import com.coui.appcompat.seekbar.olivelink.COUIIntentSeekBarParamsExtKt;
import com.oplus.dmp.sdk.search.SearchProtocol;
import com.oplus.settings.feature.display.atmospherelight.utils.AtmosphereLightStatisticsUtils;
import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\u008a\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\u0014\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0015\u0018\u0000 Z2\u00020\u0001:\u0002Z[B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020-H\u0002J\u0010\u0010.\u001a\u00020+2\u0006\u0010,\u001a\u00020-H\u0002J\u0016\u0010/\u001a\u00020+2\u0006\u00100\u001a\u0002012\u0006\u00102\u001a\u000203J \u00104\u001a\u00020+2\u0006\u00100\u001a\u0002012\u0006\u00102\u001a\u0002032\u0006\u00105\u001a\u00020\bH\u0016J\u0016\u00106\u001a\u00020+2\u0006\u00100\u001a\u0002012\u0006\u00102\u001a\u000203J\u0018\u00107\u001a\u00020\f2\u0006\u00108\u001a\u00020\b2\u0006\u00109\u001a\u00020\bH\u0002J\b\u0010:\u001a\u00020+H\u0002J@\u0010;\u001a\u00020+2\u0006\u0010<\u001a\u00020\b2\u0006\u0010=\u001a\u00020\b2\u0006\u0010>\u001a\u00020\b2\u0006\u0010?\u001a\u00020\b2\u0006\u0010@\u001a\u00020\b2\u0006\u0010A\u001a\u00020\b2\u0006\u0010B\u001a\u00020\bH\u0002J\u0006\u0010C\u001a\u00020$J\b\u0010D\u001a\u00020EH\u0016J\b\u0010F\u001a\u00020GH\u0016J\b\u0010H\u001a\u00020+H\u0002J\b\u0010I\u001a\u00020\u0019H\u0016J\b\u0010J\u001a\u00020\bH\u0002J\u0010\u0010K\u001a\u00020+2\u0006\u0010L\u001a\u00020\u0019H\u0002J\u0006\u0010M\u001a\u00020+J\u0010\u0010N\u001a\u00020+2\u0006\u0010O\u001a\u00020GH\u0016J\u0006\u0010P\u001a\u00020\u0019J\u0018\u0010Q\u001a\u00020\u00192\u0006\u0010R\u001a\u00020\b2\u0006\u0010S\u001a\u00020\bH\u0002J\u0006\u0010T\u001a\u00020+J\u0006\u0010U\u001a\u00020+J\u0006\u0010V\u001a\u00020+J\u0010\u0010W\u001a\u00020+2\b\b\u0002\u0010X\u001a\u00020\u0019J\u0006\u0010Y\u001a\u00020+R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0019X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020&X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020(X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\\"}, d2 = {"Lcom/coui/appcompat/seekbar/effect/LoadingEffectRenderer;", "Lcom/coui/appcompat/seekbar/effect/IEffectRenderer;", "hostView", "Landroid/view/View;", "(Landroid/view/View;)V", "mBufferingPaint", "Landroid/graphics/Paint;", "mBufferingProgress", "", "mBufferingSpringAnim", "Lcom/coui/appcompat/animation/dynamicanimation/COUISpringAnimation;", "mCachedGradient", "Landroid/graphics/LinearGradient;", "mClipPath", "Landroid/graphics/Path;", "mClipRect", "Landroid/graphics/RectF;", "mGradientColors", "", "mGradientPositions", "", "mInitialPaint", "mInitialProgress", "mInitialSpringAnim", "mInitialTailActive", "", "mLastBufferingProgressAtInvalidate", "mLastGradientLength", "mLastInitialProgressAtInvalidate", "mLastMidAlpha", "mLoadingClipCacheCenterY", "mLoadingClipCacheLength", "mLoadingClipCacheProgressHeight", "mLoadingClipCacheStart", "mLoadingClipPathGeomCacheValid", "mMode", "Lcom/coui/appcompat/seekbar/effect/LoadingEffectRenderer$LoadingMode;", "mParams", "Lcom/coui/appcompat/seekbar/effect/LoadingEffectParams;", "mShaderMatrix", "Landroid/graphics/Matrix;", "mThrottleSeekBarTrackLengthPx", "applyBufferingSpringParams", "", "springForce", "Lcom/coui/appcompat/animation/dynamicanimation/COUISpringForce;", "applyInitialLoadingSpringParams", "drawBufferingLoading", "canvas", "Landroid/graphics/Canvas;", "state", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "drawEffect", "progressPosition", "drawInitialLoading", "ensureGradient", "gradientBarLength", "midAlpha", "ensureInitialSpringAnimCreated", "ensureLoadingProgressClipPath", AtmosphereLightStatisticsUtils.VAL_ACTION_START, SearchProtocol.QUERY_LENGTH, "progressHeight", "centerY", "barTop", "barBottom", "progressRadius", "getCurrentMode", "getDrawPhase", "Lcom/coui/appcompat/seekbar/effect/DrawPhase;", "getEffectParams", "Lcom/coui/appcompat/seekbar/effect/EffectParams;", "invalidateLoadingClipPathGeomCache", "isEnabled", "maxLoadingTravelPx", "onInitialSpringAnimEnd", "canceled", "release", "setEffectParams", "params", "shouldDrawInitialLoadingTail", "shouldSkipLoadingInvalidate", "newProgress", "lastProgressAtInvalidate", "startBuffering", "startInitialLoading", "stopBuffering", "stopInitialLoading", "immediate", "stopLoading", "Companion", "LoadingMode", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class LoadingEffectRenderer implements IEffectRenderer {
    private static final int COLOR_CHANNEL_MAX = 255;
    private static final float LOADING_ANIM_INVALIDATE_MIN_PIXEL = 1.0f;
    private static final float SPRING_FINAL_POSITION = 10000.0f;
    private static final float TAIL_SKIP_PROGRESS_EPSILON = 0.99f;
    private final View hostView;
    private final Paint mBufferingPaint;
    private float mBufferingProgress;
    private COUISpringAnimation mBufferingSpringAnim;
    private LinearGradient mCachedGradient;
    private final Path mClipPath;
    private final RectF mClipRect;
    private final int[] mGradientColors;
    private final float[] mGradientPositions;
    private final Paint mInitialPaint;
    private float mInitialProgress;
    private COUISpringAnimation mInitialSpringAnim;
    private boolean mInitialTailActive;
    private float mLastBufferingProgressAtInvalidate;
    private float mLastGradientLength;
    private float mLastInitialProgressAtInvalidate;
    private float mLastMidAlpha;
    private float mLoadingClipCacheCenterY;
    private float mLoadingClipCacheLength;
    private float mLoadingClipCacheProgressHeight;
    private float mLoadingClipCacheStart;
    private boolean mLoadingClipPathGeomCacheValid;
    private LoadingMode mMode;
    private LoadingEffectParams mParams;
    private final Matrix mShaderMatrix;
    private float mThrottleSeekBarTrackLengthPx;

    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005¨\u0006\u0006"}, d2 = {"Lcom/coui/appcompat/seekbar/effect/LoadingEffectRenderer$LoadingMode;", "", "(Ljava/lang/String;I)V", "NONE", "INITIAL", "BUFFERING", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public enum LoadingMode {
        NONE,
        INITIAL,
        BUFFERING;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

        public static EnumEntries<LoadingMode> getEntries() {
            return $ENTRIES;
        }
    }

    @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[LoadingMode.values().length];
            try {
                iArr[LoadingMode.INITIAL.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[LoadingMode.BUFFERING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[LoadingMode.NONE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public LoadingEffectRenderer(View hostView) {
        Intrinsics.checkNotNullParameter(hostView, "hostView");
        this.hostView = hostView;
        this.mParams = new LoadingEffectParams();
        this.mMode = LoadingMode.NONE;
        this.mLastInitialProgressAtInvalidate = Float.NaN;
        Paint paint = new Paint(1);
        Paint.Style style = Paint.Style.FILL;
        paint.setStyle(style);
        this.mInitialPaint = paint;
        this.mLastBufferingProgressAtInvalidate = Float.NaN;
        Paint paint2 = new Paint(1);
        paint2.setStyle(style);
        this.mBufferingPaint = paint2;
        this.mClipRect = new RectF();
        this.mClipPath = new Path();
        this.mShaderMatrix = new Matrix();
        this.mLastMidAlpha = -1.0f;
        this.mGradientColors = new int[3];
        this.mGradientPositions = new float[]{0.0f, 0.5f, 1.0f};
    }

    public static /* synthetic */ void a(LoadingEffectRenderer loadingEffectRenderer, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        startBuffering$lambda$5(loadingEffectRenderer, cOUIDynamicAnimation, z2, f3, f4);
    }

    private final void applyBufferingSpringParams(COUISpringForce springForce) {
        COUIIntentSeekBarParams cOUIIntentSeekBarParams = COUIIntentSeekBarParamsExtKt.get(COUIIntentSeekBarParams.INSTANCE);
        springForce.setResponse(cOUIIntentSeekBarParams.getBufferingSpring().getResponse());
        springForce.setBounce(cOUIIntentSeekBarParams.getBufferingSpring().getBounce());
    }

    private final void applyInitialLoadingSpringParams(COUISpringForce springForce) {
        COUIIntentSeekBarParams cOUIIntentSeekBarParams = COUIIntentSeekBarParamsExtKt.get(COUIIntentSeekBarParams.INSTANCE);
        springForce.setResponse(cOUIIntentSeekBarParams.getInitialLoadingSpring().getResponse());
        springForce.setBounce(cOUIIntentSeekBarParams.getInitialLoadingSpring().getBounce());
    }

    public static /* synthetic */ void b(LoadingEffectRenderer loadingEffectRenderer, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        ensureInitialSpringAnimCreated$lambda$3(loadingEffectRenderer, cOUIDynamicAnimation, z2, f3, f4);
    }

    public static /* synthetic */ void c(LoadingEffectRenderer loadingEffectRenderer, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        ensureInitialSpringAnimCreated$lambda$2(loadingEffectRenderer, cOUIDynamicAnimation, f3, f4);
    }

    public static /* synthetic */ void d(LoadingEffectRenderer loadingEffectRenderer, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        startBuffering$lambda$4(loadingEffectRenderer, cOUIDynamicAnimation, f3, f4);
    }

    private final LinearGradient ensureGradient(float gradientBarLength, float midAlpha) {
        if (this.mCachedGradient == null || gradientBarLength != this.mLastGradientLength || midAlpha != this.mLastMidAlpha) {
            this.mGradientColors[0] = Color.argb(0, 255, 255, 255);
            this.mGradientColors[1] = Color.argb((int) (255 * midAlpha), 255, 255, 255);
            this.mGradientColors[2] = Color.argb(0, 255, 255, 255);
            this.mCachedGradient = new LinearGradient(0.0f, 0.0f, gradientBarLength, 0.0f, this.mGradientColors, this.mGradientPositions, Shader.TileMode.CLAMP);
            this.mLastGradientLength = gradientBarLength;
            this.mLastMidAlpha = midAlpha;
        }
        LinearGradient linearGradient = this.mCachedGradient;
        Intrinsics.checkNotNull(linearGradient);
        return linearGradient;
    }

    private final void ensureInitialSpringAnimCreated() {
        if (this.mInitialSpringAnim != null) {
            return;
        }
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        applyInitialLoadingSpringParams(cOUISpringForce);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mInitialSpringAnim = spring;
        if (spring != null) {
            spring.addUpdateListener(new j(this));
        }
        COUISpringAnimation cOUISpringAnimation = this.mInitialSpringAnim;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.addEndListener(new k(this));
        }
    }

    private static final void ensureInitialSpringAnimCreated$lambda$2(LoadingEffectRenderer this$0, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        float fCoerceIn = RangesKt.coerceIn(f3 / 10000.0f, 0.0f, 1.0f);
        this$0.mInitialProgress = fCoerceIn;
        if (this$0.shouldSkipLoadingInvalidate(fCoerceIn, this$0.mLastInitialProgressAtInvalidate)) {
            return;
        }
        this$0.mLastInitialProgressAtInvalidate = this$0.mInitialProgress;
        this$0.hostView.invalidate();
    }

    private static final void ensureInitialSpringAnimCreated$lambda$3(LoadingEffectRenderer this$0, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.onInitialSpringAnimEnd(z2);
    }

    private final void ensureLoadingProgressClipPath(float start, float length, float progressHeight, float centerY, float barTop, float barBottom, float progressRadius) {
        if (this.mLoadingClipPathGeomCacheValid && start == this.mLoadingClipCacheStart && length == this.mLoadingClipCacheLength && progressHeight == this.mLoadingClipCacheProgressHeight && centerY == this.mLoadingClipCacheCenterY) {
            return;
        }
        this.mClipRect.set(start, barTop, start + length, barBottom);
        this.mClipPath.reset();
        this.mClipPath.addRoundRect(this.mClipRect, progressRadius, progressRadius, Path.Direction.CW);
        this.mLoadingClipCacheStart = start;
        this.mLoadingClipCacheLength = length;
        this.mLoadingClipCacheProgressHeight = progressHeight;
        this.mLoadingClipCacheCenterY = centerY;
        this.mLoadingClipPathGeomCacheValid = true;
    }

    private final void invalidateLoadingClipPathGeomCache() {
        this.mLoadingClipPathGeomCacheValid = false;
    }

    private final float maxLoadingTravelPx() {
        float f3 = this.mThrottleSeekBarTrackLengthPx;
        if (f3 <= 0.0f) {
            return 0.0f;
        }
        return f3 + (this.mParams.getGradientLengthRatio() * f3);
    }

    private final void onInitialSpringAnimEnd(boolean canceled) {
        if (this.mInitialTailActive) {
            this.mInitialTailActive = false;
            this.mInitialProgress = 0.0f;
            this.hostView.invalidate();
        } else if (!canceled && this.mMode == LoadingMode.INITIAL) {
            COUISpringAnimation cOUISpringAnimation = this.mInitialSpringAnim;
            if (cOUISpringAnimation != null) {
                cOUISpringAnimation.setStartValue(0.0f);
            }
            COUISpringAnimation cOUISpringAnimation2 = this.mInitialSpringAnim;
            if (cOUISpringAnimation2 != null) {
                cOUISpringAnimation2.animateToFinalPosition(10000.0f);
            }
        }
    }

    private final boolean shouldSkipLoadingInvalidate(float newProgress, float lastProgressAtInvalidate) {
        float fMaxLoadingTravelPx = maxLoadingTravelPx();
        return fMaxLoadingTravelPx > 0.0f && !Float.isNaN(lastProgressAtInvalidate) && Math.abs(newProgress - lastProgressAtInvalidate) * fMaxLoadingTravelPx < 1.0f;
    }

    private static final void startBuffering$lambda$4(LoadingEffectRenderer this$0, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        float fCoerceIn = RangesKt.coerceIn(f3 / 10000.0f, 0.0f, 1.0f);
        this$0.mBufferingProgress = fCoerceIn;
        if (this$0.shouldSkipLoadingInvalidate(fCoerceIn, this$0.mLastBufferingProgressAtInvalidate)) {
            return;
        }
        this$0.mLastBufferingProgressAtInvalidate = this$0.mBufferingProgress;
        this$0.hostView.invalidate();
    }

    private static final void startBuffering$lambda$5(LoadingEffectRenderer this$0, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (z2 || this$0.mMode != LoadingMode.BUFFERING) {
            return;
        }
        COUISpringAnimation cOUISpringAnimation = this$0.mBufferingSpringAnim;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.setStartValue(0.0f);
        }
        COUISpringAnimation cOUISpringAnimation2 = this$0.mBufferingSpringAnim;
        if (cOUISpringAnimation2 != null) {
            cOUISpringAnimation2.animateToFinalPosition(10000.0f);
        }
    }

    public static /* synthetic */ void stopInitialLoading$default(LoadingEffectRenderer loadingEffectRenderer, boolean z2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            z2 = false;
        }
        loadingEffectRenderer.stopInitialLoading(z2);
    }

    public final void drawBufferingLoading(Canvas canvas, SeekBarState state) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        Intrinsics.checkNotNullParameter(state, "state");
        COUISeekBarCore view = state.getView();
        if (view == null) {
            this.mThrottleSeekBarTrackLengthPx = 0.0f;
            return;
        }
        float seekBarLength = view.getSeekBarLength();
        this.mThrottleSeekBarTrackLengthPx = seekBarLength;
        if (seekBarLength <= 0.0f) {
            return;
        }
        float progressStart = view.getProgressStart();
        float centerY = state.getCenterY();
        float progressRectHeight = view.getProgressRectHeight(state);
        float f3 = progressRectHeight / 2;
        float f4 = centerY - f3;
        float f5 = centerY + f3;
        float fCoerceIn = RangesKt.coerceIn(state.getDrawProgressScale(), 0.0f, 1.0f);
        float f6 = (state.getIsRtl() ? (1.0f - fCoerceIn) * seekBarLength : seekBarLength * fCoerceIn) + progressStart;
        if (!state.getIsRtl()) {
            fCoerceIn = 1.0f - fCoerceIn;
        }
        float f7 = fCoerceIn * seekBarLength;
        float gradientLengthRatio = this.mParams.getGradientLengthRatio() * seekBarLength;
        float f8 = f7 + gradientLengthRatio;
        float fCoerceIn2 = RangesKt.coerceIn(this.mBufferingProgress, 0.0f, 1.0f);
        float f9 = state.getIsRtl() ? f6 - (f8 * fCoerceIn2) : f6 + (f8 * fCoerceIn2);
        float f10 = f9 - gradientLengthRatio;
        ensureLoadingProgressClipPath(progressStart, seekBarLength, progressRectHeight, centerY, f4, f5, f3);
        canvas.save();
        canvas.clipPath(this.mClipPath);
        LinearGradient linearGradientEnsureGradient = ensureGradient(gradientLengthRatio, this.mParams.getGradientMidAlpha());
        this.mShaderMatrix.setTranslate(f10, 0.0f);
        linearGradientEnsureGradient.setLocalMatrix(this.mShaderMatrix);
        this.mBufferingPaint.setShader(linearGradientEnsureGradient);
        canvas.drawRoundRect(f10, f4, f9, f5, f3, f3, this.mBufferingPaint);
        this.mBufferingPaint.setShader(null);
        canvas.restore();
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    public void drawEffect(Canvas canvas, SeekBarState state, float progressPosition) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        Intrinsics.checkNotNullParameter(state, "state");
        int i3 = WhenMappings.$EnumSwitchMapping$0[this.mMode.ordinal()];
        if (i3 == 1) {
            drawInitialLoading(canvas, state);
        } else {
            if (i3 != 2) {
                return;
            }
            drawBufferingLoading(canvas, state);
        }
    }

    public final void drawInitialLoading(Canvas canvas, SeekBarState state) {
        float f3;
        float f4;
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        Intrinsics.checkNotNullParameter(state, "state");
        COUISeekBarCore view = state.getView();
        if (view == null) {
            this.mThrottleSeekBarTrackLengthPx = 0.0f;
            return;
        }
        float seekBarLength = view.getSeekBarLength();
        this.mThrottleSeekBarTrackLengthPx = seekBarLength;
        if (seekBarLength <= 0.0f) {
            return;
        }
        float progressStart = view.getProgressStart();
        float centerY = state.getCenterY();
        float progressRectHeight = view.getProgressRectHeight(state);
        float f5 = progressRectHeight / 2;
        float f6 = centerY - f5;
        float f7 = centerY + f5;
        float gradientLengthRatio = this.mParams.getGradientLengthRatio() * seekBarLength;
        float f8 = seekBarLength + gradientLengthRatio;
        float fCoerceIn = RangesKt.coerceIn(this.mInitialProgress, 0.0f, 1.0f);
        float f9 = progressStart + seekBarLength;
        if (state.getIsRtl()) {
            f4 = (f9 + gradientLengthRatio) - (f8 * fCoerceIn);
            f3 = f4 - gradientLengthRatio;
        } else {
            f3 = (f8 * fCoerceIn) + (progressStart - gradientLengthRatio);
            f4 = f3 + gradientLengthRatio;
        }
        ensureLoadingProgressClipPath(progressStart, seekBarLength, progressRectHeight, centerY, f6, f7, f5);
        canvas.save();
        canvas.clipPath(this.mClipPath);
        LinearGradient linearGradientEnsureGradient = ensureGradient(gradientLengthRatio, this.mParams.getGradientMidAlpha());
        this.mShaderMatrix.setTranslate(f3, 0.0f);
        linearGradientEnsureGradient.setLocalMatrix(this.mShaderMatrix);
        this.mInitialPaint.setShader(linearGradientEnsureGradient);
        canvas.drawRoundRect(f3, f6, f4, f7, f5, f5, this.mInitialPaint);
        this.mInitialPaint.setShader(null);
        canvas.restore();
    }

    /* JADX INFO: renamed from: getCurrentMode, reason: from getter */
    public final LoadingMode getMMode() {
        return this.mMode;
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    public DrawPhase getDrawPhase() {
        return DrawPhase.ACTIVE_TRACK;
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    /* JADX INFO: renamed from: getEffectParams */
    public EffectParams getMParams() {
        return this.mParams;
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    /* JADX INFO: renamed from: isEnabled */
    public boolean getMEnabled() {
        return this.mMode != LoadingMode.NONE;
    }

    public final void release() {
        stopLoading();
        this.mInitialSpringAnim = null;
        this.mBufferingSpringAnim = null;
        this.mCachedGradient = null;
        this.mThrottleSeekBarTrackLengthPx = 0.0f;
        invalidateLoadingClipPathGeomCache();
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    public void setEffectParams(EffectParams params) {
        Intrinsics.checkNotNullParameter(params, "params");
        if (params instanceof LoadingEffectParams) {
            this.mParams = (LoadingEffectParams) params;
        }
    }

    /* JADX INFO: renamed from: shouldDrawInitialLoadingTail, reason: from getter */
    public final boolean getMInitialTailActive() {
        return this.mInitialTailActive;
    }

    public final void startBuffering() {
        this.mMode = LoadingMode.BUFFERING;
        COUISpringAnimation cOUISpringAnimation = this.mBufferingSpringAnim;
        if (cOUISpringAnimation != null) {
            Intrinsics.checkNotNull(cOUISpringAnimation);
            if (cOUISpringAnimation.isRunning()) {
                return;
            }
            COUISpringAnimation cOUISpringAnimation2 = this.mBufferingSpringAnim;
            Intrinsics.checkNotNull(cOUISpringAnimation2);
            cOUISpringAnimation2.setStartValue(0.0f);
            COUISpringAnimation cOUISpringAnimation3 = this.mBufferingSpringAnim;
            Intrinsics.checkNotNull(cOUISpringAnimation3);
            cOUISpringAnimation3.animateToFinalPosition(10000.0f);
            return;
        }
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        applyBufferingSpringParams(cOUISpringForce);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mBufferingSpringAnim = spring;
        if (spring != null) {
            spring.addUpdateListener(new h(this));
        }
        COUISpringAnimation cOUISpringAnimation4 = this.mBufferingSpringAnim;
        if (cOUISpringAnimation4 != null) {
            cOUISpringAnimation4.addEndListener(new i(this));
        }
        COUISpringAnimation cOUISpringAnimation5 = this.mBufferingSpringAnim;
        if (cOUISpringAnimation5 != null) {
            cOUISpringAnimation5.setStartValue(0.0f);
        }
        COUISpringAnimation cOUISpringAnimation6 = this.mBufferingSpringAnim;
        if (cOUISpringAnimation6 != null) {
            cOUISpringAnimation6.animateToFinalPosition(10000.0f);
        }
    }

    public final void startInitialLoading() {
        this.mMode = LoadingMode.INITIAL;
        ensureInitialSpringAnimCreated();
        COUISpringAnimation cOUISpringAnimation = this.mInitialSpringAnim;
        if (cOUISpringAnimation == null) {
            return;
        }
        if (!cOUISpringAnimation.isRunning() || this.mInitialTailActive) {
            cOUISpringAnimation.cancel();
            cOUISpringAnimation.setStartValue(0.0f);
            cOUISpringAnimation.animateToFinalPosition(10000.0f);
        }
    }

    public final void stopBuffering() {
        COUISpringAnimation cOUISpringAnimation = this.mBufferingSpringAnim;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.cancel();
        }
        this.mBufferingProgress = 0.0f;
        this.mLastBufferingProgressAtInvalidate = Float.NaN;
        this.mThrottleSeekBarTrackLengthPx = 0.0f;
        invalidateLoadingClipPathGeomCache();
        if (this.mMode == LoadingMode.BUFFERING) {
            this.mMode = LoadingMode.NONE;
        }
    }

    public final void stopInitialLoading(boolean immediate) {
        COUISpringAnimation cOUISpringAnimation;
        if (immediate) {
            this.mInitialTailActive = false;
            COUISpringAnimation cOUISpringAnimation2 = this.mInitialSpringAnim;
            if (cOUISpringAnimation2 != null) {
                cOUISpringAnimation2.cancel();
            }
            this.mInitialProgress = 0.0f;
            this.mLastInitialProgressAtInvalidate = Float.NaN;
            this.mThrottleSeekBarTrackLengthPx = 0.0f;
            invalidateLoadingClipPathGeomCache();
            if (this.mMode == LoadingMode.INITIAL) {
                this.mMode = LoadingMode.NONE;
                return;
            }
            return;
        }
        if (this.mMode != LoadingMode.INITIAL) {
            COUISpringAnimation cOUISpringAnimation3 = this.mInitialSpringAnim;
            if (cOUISpringAnimation3 != null) {
                cOUISpringAnimation3.cancel();
            }
            if (this.mInitialTailActive) {
                return;
            }
            this.mInitialProgress = 0.0f;
            return;
        }
        ensureInitialSpringAnimCreated();
        if (this.mInitialProgress >= TAIL_SKIP_PROGRESS_EPSILON) {
            this.mInitialProgress = 0.0f;
            this.mMode = LoadingMode.NONE;
            this.mInitialTailActive = false;
            return;
        }
        this.mInitialTailActive = true;
        this.mMode = LoadingMode.NONE;
        COUISpringAnimation cOUISpringAnimation4 = this.mInitialSpringAnim;
        if ((cOUISpringAnimation4 == null || !cOUISpringAnimation4.isRunning()) && (cOUISpringAnimation = this.mInitialSpringAnim) != null) {
            cOUISpringAnimation.animateToFinalPosition(10000.0f);
        }
    }

    public final void stopLoading() {
        stopInitialLoading(true);
        stopBuffering();
        this.mMode = LoadingMode.NONE;
    }
}