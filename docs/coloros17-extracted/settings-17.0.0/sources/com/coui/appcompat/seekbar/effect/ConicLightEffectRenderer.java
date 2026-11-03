package com.coui.appcompat.seekbar.effect;

import android.graphics.Canvas;
import android.view.View;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.android.settings.accessibility.hearingdevices.data.HearingAidCompatibilityDataStore;
import com.android.settings.inputmethod.UserDictionaryAddWordContents;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.seekbar.COUIIntentSeekBar;
import com.coui.appcompat.seekbar.IntentSeekBarSize;
import com.coui.appcompat.seekbar.core.COUISeekBarCore;
import com.coui.appcompat.seekbar.core.SeekBarState;
import com.coui.appcompat.seekbar.shader.IConicLightDrawer;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u001d\u0018\u0000 ]2\u00020\u0001:\u0002]^B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010-\u001a\u00020.H\u0002J\u0006\u0010/\u001a\u00020.J\b\u00100\u001a\u00020.H\u0002J\b\u00101\u001a\u00020.H\u0002J,\u00102\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0006032\u0006\u00104\u001a\u00020\u00062\u0006\u00105\u001a\u00020\u00062\u0006\u00106\u001a\u00020\u0006H\u0002J \u00107\u001a\u00020.2\u0006\u00108\u001a\u0002092\u0006\u0010:\u001a\u00020;2\u0006\u0010<\u001a\u00020\u0006H\u0016J\b\u0010=\u001a\u00020\u0013H\u0007J\u0006\u0010>\u001a\u00020\u001bJ\b\u0010?\u001a\u00020\u0006H\u0002J\b\u0010@\u001a\u00020\u0006H\u0002J\b\u0010A\u001a\u00020BH\u0016J\b\u0010C\u001a\u00020\u0006H\u0002J\b\u0010D\u001a\u00020.H\u0002J\b\u0010E\u001a\u00020.H\u0002J\b\u0010F\u001a\u00020,H\u0016J\u0006\u0010G\u001a\u00020,J\u0006\u0010H\u001a\u00020.J\u0006\u0010I\u001a\u00020.J\b\u0010J\u001a\u00020.H\u0002J\b\u0010K\u001a\u00020.H\u0002J\b\u0010L\u001a\u00020.H\u0002J\u0010\u0010M\u001a\u00020.2\b\b\u0001\u0010N\u001a\u00020\u0013J\u0010\u0010O\u001a\u00020.2\b\u0010P\u001a\u0004\u0018\u00010\u0015J\u0010\u0010Q\u001a\u00020.2\u0006\u0010R\u001a\u00020BH\u0016J\b\u0010S\u001a\u00020.H\u0002J\b\u0010T\u001a\u00020.H\u0002J\b\u0010U\u001a\u00020.H\u0002J\u000e\u0010V\u001a\u00020.2\u0006\u0010W\u001a\u00020*J\b\u0010X\u001a\u00020.H\u0002J\b\u0010Y\u001a\u00020.H\u0002J\b\u0010Z\u001a\u00020.H\u0002J\u000e\u0010[\u001a\u00020.2\u0006\u0010\\\u001a\u00020\u001bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0012\u001a\u00020\u00138\u0002@\u0002X\u0083\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0006X\u0082D¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0006X\u0082D¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010#\u001a\u0004\u0018\u00010\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020*X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020,X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006_"}, d2 = {"Lcom/coui/appcompat/seekbar/effect/ConicLightEffectRenderer;", "Lcom/coui/appcompat/seekbar/effect/IEffectRenderer;", "hostView", "Landroid/view/View;", "(Landroid/view/View;)V", "mAlphaFromAlpha1", "", "mAlphaFromAlpha2", "mAlphaSpringAnim", "Lcom/coui/appcompat/animation/dynamicanimation/COUISpringAnimation;", "mAlphaToAlpha1", "mAlphaToAlpha2", "mBaseConicHeight", "mBaseConicHeightFalloff", "mBezierThreshold", "mConicAlpha1", "mConicAlpha2", "mConicConeK", "mConicLightColor", "", "mConicLightDrawer", "Lcom/coui/appcompat/seekbar/shader/IConicLightDrawer;", "mConicLightHeight", "mConicLightHorizontalPadding", "mConicSecondConeK", "mConicW0", "mCurrentMode", "Lcom/coui/appcompat/seekbar/effect/ConicLightEffectRenderer$ConicLightMode;", "mParams", "Lcom/coui/appcompat/seekbar/effect/ConicLightEffectParams;", "mShapeFromBaseHeight", "mShapeFromBaseHeightFalloff", "mShapeFromConeK", "mShapeFromSecondConeK", "mShapeFromW0", "mShapeSpringAnim", "mShapeToBaseHeight", "mShapeToBaseHeightFalloff", "mShapeToConeK", "mShapeToSecondConeK", "mShapeToW0", "mSizeType", "Lcom/coui/appcompat/seekbar/IntentSeekBarSize;", "mTransitionDrawing", "", "applyParamsToDrawer", "", "cancelAnimations", "captureAnimFromValues", "checkTransitionEnd", "computeModulatedHeightParams", "Lkotlin/Pair;", "process", "baseHeight", "baseHeightFalloff", "drawEffect", "canvas", "Landroid/graphics/Canvas;", "state", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "progressPosition", "getConicLightColor", "getCurrentMode", "getDragHeight", "getDragW0", "getEffectParams", "Lcom/coui/appcompat/seekbar/effect/EffectParams;", "getPlayW0", "initAlphaSpringAnim", "initShapeSpringAnim", "isEnabled", "isTransitionDrawing", "release", "resetToOff", "setAlphaToDragging", "setAlphaToOff", "setAlphaToPlaying", "setConicLightColor", TypedValues.Custom.S_COLOR, "setConicLightDrawer", "drawer", "setEffectParams", "params", "setShapeToDragging", "setShapeToOff", "setShapeToPlaying", "setSizeType", "sizeType", "startEnterDraggingAnim", "startEnterPlayingAnim", "startFadeOutAnim", "transitionTo", UserDictionaryAddWordContents.EXTRA_MODE, "Companion", "ConicLightMode", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ConicLightEffectRenderer implements IEffectRenderer {
    private static final float ANIM_FINAL = 10000.0f;
    private static final float BEZIER_POINT1_X = 0.0f;
    private static final float BEZIER_POINT1_Y = 0.0f;
    private static final float BEZIER_POINT2_X = 1.0f;
    private static final float BEZIER_POINT2_Y = 1.0f;
    private static final float CONIC_LIGHT_HEIGHT = 450.0f;
    private static final float CONIC_LIGHT_HORIZONTAL_PADDING = 300.0f;
    private static final float CONIC_LIGHT_THRESHOLD = 0.2f;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final float DRAG_ALPHA1 = 0.3f;
    private static final float DRAG_ALPHA2 = 0.3f;
    private static final float DRAG_CONE_K = 0.0f;
    private static final float DRAG_HEIGHT_DEFAULT = 0.2f;
    private static final float DRAG_HEIGHT_FALLOFF = 2.0f;
    private static final float DRAG_HEIGHT_LARGE = 0.4f;
    private static final float DRAG_HEIGHT_SMALL = 0.4f;
    private static final float DRAG_SECOND_CONE_K = 0.0f;
    private static final float DRAG_W0_DEFAULT = 0.014f;
    private static final float DRAG_W0_LARGE = 0.03f;
    private static final float DRAG_W0_SMALL = 0.03f;
    private static final float ENTER_DRAG_BOUNCE = 0.0f;
    private static final float ENTER_DRAG_RESPONSE = 0.4f;
    private static final float ENTER_PLAY_ALPHA_BOUNCE = 0.0f;
    private static final float ENTER_PLAY_ALPHA_RESPONSE = 0.3f;
    private static final float ENTER_PLAY_SHAPE_BOUNCE = 0.0f;
    private static final float ENTER_PLAY_SHAPE_RESPONSE = 0.9f;
    private static final float FADEOUT_BOUNCE = 0.0f;
    private static final float FADEOUT_RESPONSE = 0.4f;
    private static final float OFF_ALPHA1 = 0.0f;
    private static final float OFF_ALPHA2 = 0.0f;
    private static final float OFF_CONE_K = 0.0f;
    private static final float OFF_HEIGHT = 0.1f;
    private static final float OFF_HEIGHT_FALLOFF = 0.0f;
    private static final float OFF_SECOND_CONE_K = 0.0f;
    private static final float OFF_W0 = 0.0f;
    private static final float PLAY_ALPHA1 = 1.0f;
    private static final float PLAY_ALPHA2 = 0.6f;
    private static final float PLAY_CONE_K = 0.2f;
    private static final float PLAY_HEIGHT = 0.6f;
    private static final float PLAY_HEIGHT_FALLOFF = 0.6f;
    private static final float PLAY_SECOND_CONE_K = 0.2f;
    private static final float PLAY_W0_DEFAULT = 0.008f;
    private static final float PLAY_W0_LARGE = 0.012f;
    private static final float PLAY_W0_SMALL = 0.002f;
    private static final float THRESHOLD_EPSILON = 0.01f;
    private final View hostView;
    private float mAlphaFromAlpha1;
    private float mAlphaFromAlpha2;
    private COUISpringAnimation mAlphaSpringAnim;
    private float mAlphaToAlpha1;
    private float mAlphaToAlpha2;
    private float mBaseConicHeight;
    private float mBaseConicHeightFalloff;
    private float mBezierThreshold;
    private float mConicAlpha1;
    private float mConicAlpha2;
    private float mConicConeK;
    private int mConicLightColor;
    private IConicLightDrawer mConicLightDrawer;
    private final float mConicLightHeight;
    private final float mConicLightHorizontalPadding;
    private float mConicSecondConeK;
    private float mConicW0;
    private ConicLightMode mCurrentMode;
    private ConicLightEffectParams mParams;
    private float mShapeFromBaseHeight;
    private float mShapeFromBaseHeightFalloff;
    private float mShapeFromConeK;
    private float mShapeFromSecondConeK;
    private float mShapeFromW0;
    private COUISpringAnimation mShapeSpringAnim;
    private float mShapeToBaseHeight;
    private float mShapeToBaseHeightFalloff;
    private float mShapeToConeK;
    private float mShapeToSecondConeK;
    private float mShapeToW0;
    private IntentSeekBarSize mSizeType;
    private boolean mTransitionDrawing;

    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b2\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J0\u00100\u001a\u00020\u00042\u0006\u00101\u001a\u00020\u00042\u0006\u00102\u001a\u00020\u00042\u0006\u00103\u001a\u00020\u00042\u0006\u00104\u001a\u00020\u00042\u0006\u00105\u001a\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u00066"}, d2 = {"Lcom/coui/appcompat/seekbar/effect/ConicLightEffectRenderer$Companion;", "", "()V", "ANIM_FINAL", "", "BEZIER_POINT1_X", "BEZIER_POINT1_Y", "BEZIER_POINT2_X", "BEZIER_POINT2_Y", "CONIC_LIGHT_HEIGHT", "CONIC_LIGHT_HORIZONTAL_PADDING", "CONIC_LIGHT_THRESHOLD", "DRAG_ALPHA1", "DRAG_ALPHA2", "DRAG_CONE_K", "DRAG_HEIGHT_DEFAULT", "DRAG_HEIGHT_FALLOFF", "DRAG_HEIGHT_LARGE", "DRAG_HEIGHT_SMALL", "DRAG_SECOND_CONE_K", "DRAG_W0_DEFAULT", "DRAG_W0_LARGE", "DRAG_W0_SMALL", "ENTER_DRAG_BOUNCE", "ENTER_DRAG_RESPONSE", "ENTER_PLAY_ALPHA_BOUNCE", "ENTER_PLAY_ALPHA_RESPONSE", "ENTER_PLAY_SHAPE_BOUNCE", "ENTER_PLAY_SHAPE_RESPONSE", "FADEOUT_BOUNCE", "FADEOUT_RESPONSE", "OFF_ALPHA1", "OFF_ALPHA2", "OFF_CONE_K", "OFF_HEIGHT", "OFF_HEIGHT_FALLOFF", "OFF_SECOND_CONE_K", "OFF_W0", "PLAY_ALPHA1", "PLAY_ALPHA2", "PLAY_CONE_K", "PLAY_HEIGHT", "PLAY_HEIGHT_FALLOFF", "PLAY_SECOND_CONE_K", "PLAY_W0_DEFAULT", "PLAY_W0_LARGE", "PLAY_W0_SMALL", "THRESHOLD_EPSILON", "cubicBezierY", "x", "p1x", "p1y", "p2x", "p2y", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static final /* synthetic */ float access$cubicBezierY(Companion companion, float f3, float f4, float f5, float f6, float f7) {
            return companion.cubicBezierY(f3, f4, f5, f6, f7);
        }

        private final float cubicBezierY(float x2, float p1x, float p1y, float p2x, float p2y) {
            float f3 = p1x * 3.0f;
            float f4 = (p2x - (p1x * 2.0f)) * 3.0f;
            float f5 = (1.0f - (p2x * 3.0f)) + f3;
            float fCoerceIn = 0.5f;
            for (int i3 = 0; i3 < 8; i3++) {
                fCoerceIn = RangesKt.coerceIn(fCoerceIn - ((((((((f5 * fCoerceIn) + f4) * fCoerceIn) + f3) * fCoerceIn) + 0.0f) - x2) / Math.max(Math.abs(((((f5 * 3.0f) * fCoerceIn) + (f4 * 2.0f)) * fCoerceIn) + f3), 1.0E-6f)), 0.0f, 1.0f);
            }
            float f6 = p1y * 3.0f;
            return (((((((1.0f - (p2y * 3.0f)) + f6) * fCoerceIn) + ((p2y - (p1y * 2.0f)) * 3.0f)) * fCoerceIn) + f6) * fCoerceIn) + 0.0f;
        }

        private Companion() {
        }
    }

    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005¨\u0006\u0006"}, d2 = {"Lcom/coui/appcompat/seekbar/effect/ConicLightEffectRenderer$ConicLightMode;", "", "(Ljava/lang/String;I)V", HearingAidCompatibilityDataStore.HAC_VAL_OFF, "PLAYING", "DRAGGING", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public enum ConicLightMode {
        OFF,
        PLAYING,
        DRAGGING;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

        public static EnumEntries<ConicLightMode> getEntries() {
            return $ENTRIES;
        }
    }

    @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[IntentSeekBarSize.values().length];
            try {
                iArr[IntentSeekBarSize.SMALL.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[IntentSeekBarSize.LARGE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public ConicLightEffectRenderer(View hostView) {
        Intrinsics.checkNotNullParameter(hostView, "hostView");
        this.hostView = hostView;
        this.mParams = new ConicLightEffectParams();
        this.mCurrentMode = ConicLightMode.OFF;
        this.mSizeType = IntentSeekBarSize.DEFAULT;
        this.mConicLightColor = COUIIntentSeekBar.DEFAULT_CONIC_LIGHT_COLOR;
        this.mConicLightHeight = CONIC_LIGHT_HEIGHT;
        this.mConicLightHorizontalPadding = 300.0f;
        this.mBezierThreshold = 0.2f;
        this.mBaseConicHeight = 0.1f;
    }

    public static /* synthetic */ void a(ConicLightEffectRenderer conicLightEffectRenderer, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        initShapeSpringAnim$lambda$9(conicLightEffectRenderer, cOUIDynamicAnimation, z2, f3, f4);
    }

    private final void applyParamsToDrawer() {
        IConicLightDrawer iConicLightDrawer = this.mConicLightDrawer;
        if (iConicLightDrawer == null) {
            return;
        }
        iConicLightDrawer.setConeK(this.mConicConeK);
        iConicLightDrawer.setSecondConeK(this.mConicSecondConeK);
        iConicLightDrawer.setAlpha1(this.mConicAlpha1);
        iConicLightDrawer.setAlpha2(this.mConicAlpha2);
        iConicLightDrawer.setW0(this.mConicW0);
    }

    public static /* synthetic */ void b(ConicLightEffectRenderer conicLightEffectRenderer, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        initAlphaSpringAnim$lambda$11(conicLightEffectRenderer, cOUIDynamicAnimation, z2, f3, f4);
    }

    public static /* synthetic */ void c(ConicLightEffectRenderer conicLightEffectRenderer, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        initShapeSpringAnim$lambda$8(conicLightEffectRenderer, cOUIDynamicAnimation, f3, f4);
    }

    private final void captureAnimFromValues() {
        this.mShapeFromBaseHeight = this.mBaseConicHeight;
        this.mShapeFromBaseHeightFalloff = this.mBaseConicHeightFalloff;
        this.mShapeFromConeK = this.mConicConeK;
        this.mShapeFromSecondConeK = this.mConicSecondConeK;
        this.mShapeFromW0 = this.mConicW0;
        this.mAlphaFromAlpha1 = this.mConicAlpha1;
        this.mAlphaFromAlpha2 = this.mConicAlpha2;
    }

    private final void checkTransitionEnd() {
        if (this.mTransitionDrawing) {
            COUISpringAnimation cOUISpringAnimation = this.mShapeSpringAnim;
            boolean z2 = cOUISpringAnimation != null && cOUISpringAnimation.isRunning();
            COUISpringAnimation cOUISpringAnimation2 = this.mAlphaSpringAnim;
            boolean z3 = cOUISpringAnimation2 != null && cOUISpringAnimation2.isRunning();
            if (z2 || z3) {
                return;
            }
            this.mTransitionDrawing = false;
            this.hostView.invalidate();
        }
    }

    private final Pair<Float, Float> computeModulatedHeightParams(float process, float baseHeight, float baseHeightFalloff) {
        float f3 = this.mBezierThreshold;
        if (process > f3) {
            return TuplesKt.to(Float.valueOf(baseHeight), Float.valueOf(baseHeightFalloff));
        }
        float fAccess$cubicBezierY = Companion.access$cubicBezierY(INSTANCE, RangesKt.coerceIn(process / (f3 + 0.01f), 0.0f, 1.0f), 0.0f, 0.0f, 1.0f, 1.0f);
        return TuplesKt.to(Float.valueOf(baseHeight * fAccess$cubicBezierY), Float.valueOf(baseHeightFalloff * fAccess$cubicBezierY));
    }

    public static /* synthetic */ void d(ConicLightEffectRenderer conicLightEffectRenderer, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        initAlphaSpringAnim$lambda$10(conicLightEffectRenderer, cOUIDynamicAnimation, f3, f4);
    }

    private final float getDragHeight() {
        int i3 = WhenMappings.$EnumSwitchMapping$0[this.mSizeType.ordinal()];
        return (i3 == 1 || i3 == 2) ? 0.4f : 0.2f;
    }

    private final float getDragW0() {
        int i3 = WhenMappings.$EnumSwitchMapping$0[this.mSizeType.ordinal()];
        if (i3 == 1 || i3 == 2) {
            return 0.03f;
        }
        return DRAG_W0_DEFAULT;
    }

    private final float getPlayW0() {
        int i3 = WhenMappings.$EnumSwitchMapping$0[this.mSizeType.ordinal()];
        if (i3 != 1) {
            return i3 != 2 ? PLAY_W0_DEFAULT : PLAY_W0_LARGE;
        }
        return 0.002f;
    }

    private final void initAlphaSpringAnim() {
        if (this.mAlphaSpringAnim != null) {
            return;
        }
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setResponse(0.3f);
        cOUISpringForce.setBounce(0.0f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mAlphaSpringAnim = spring;
        if (spring != null) {
            spring.addUpdateListener(new c(this));
        }
        COUISpringAnimation cOUISpringAnimation = this.mAlphaSpringAnim;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.addEndListener(new d(this));
        }
    }

    private static final void initAlphaSpringAnim$lambda$10(ConicLightEffectRenderer this$0, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        float fCoerceIn = RangesKt.coerceIn(f3 / 10000.0f, 0.0f, 1.0f);
        float f5 = this$0.mAlphaFromAlpha1;
        this$0.mConicAlpha1 = f5 + ((this$0.mAlphaToAlpha1 - f5) * fCoerceIn);
        float f6 = this$0.mAlphaFromAlpha2;
        this$0.mConicAlpha2 = f6 + ((this$0.mAlphaToAlpha2 - f6) * fCoerceIn);
        this$0.applyParamsToDrawer();
        this$0.hostView.invalidate();
    }

    private static final void initAlphaSpringAnim$lambda$11(ConicLightEffectRenderer this$0, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (z2) {
            return;
        }
        this$0.mConicAlpha1 = this$0.mAlphaToAlpha1;
        this$0.mConicAlpha2 = this$0.mAlphaToAlpha2;
        this$0.applyParamsToDrawer();
        this$0.checkTransitionEnd();
    }

    private final void initShapeSpringAnim() {
        if (this.mShapeSpringAnim != null) {
            return;
        }
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setResponse(0.9f);
        cOUISpringForce.setBounce(0.0f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mShapeSpringAnim = spring;
        if (spring != null) {
            spring.addUpdateListener(new a(this));
        }
        COUISpringAnimation cOUISpringAnimation = this.mShapeSpringAnim;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.addEndListener(new b(this));
        }
    }

    private static final void initShapeSpringAnim$lambda$8(ConicLightEffectRenderer this$0, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        float fCoerceIn = RangesKt.coerceIn(f3 / 10000.0f, 0.0f, 1.0f);
        float f5 = this$0.mShapeFromBaseHeight;
        this$0.mBaseConicHeight = f5 + ((this$0.mShapeToBaseHeight - f5) * fCoerceIn);
        float f6 = this$0.mShapeFromBaseHeightFalloff;
        this$0.mBaseConicHeightFalloff = f6 + ((this$0.mShapeToBaseHeightFalloff - f6) * fCoerceIn);
        float f7 = this$0.mShapeFromConeK;
        this$0.mConicConeK = f7 + ((this$0.mShapeToConeK - f7) * fCoerceIn);
        float f8 = this$0.mShapeFromSecondConeK;
        this$0.mConicSecondConeK = f8 + ((this$0.mShapeToSecondConeK - f8) * fCoerceIn);
        float f9 = this$0.mShapeFromW0;
        this$0.mConicW0 = f9 + ((this$0.mShapeToW0 - f9) * fCoerceIn);
        this$0.applyParamsToDrawer();
        this$0.hostView.invalidate();
    }

    private static final void initShapeSpringAnim$lambda$9(ConicLightEffectRenderer this$0, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (z2) {
            return;
        }
        this$0.mBaseConicHeight = this$0.mShapeToBaseHeight;
        this$0.mBaseConicHeightFalloff = this$0.mShapeToBaseHeightFalloff;
        this$0.mConicConeK = this$0.mShapeToConeK;
        this$0.mConicSecondConeK = this$0.mShapeToSecondConeK;
        this$0.mConicW0 = this$0.mShapeToW0;
        this$0.applyParamsToDrawer();
        this$0.checkTransitionEnd();
    }

    private final void setAlphaToDragging() {
        this.mAlphaToAlpha1 = 0.3f;
        this.mAlphaToAlpha2 = 0.3f;
    }

    private final void setAlphaToOff() {
        this.mAlphaToAlpha1 = 0.0f;
        this.mAlphaToAlpha2 = 0.0f;
    }

    private final void setAlphaToPlaying() {
        this.mAlphaToAlpha1 = 1.0f;
        this.mAlphaToAlpha2 = 0.6f;
    }

    private final void setShapeToDragging() {
        this.mShapeToBaseHeight = getDragHeight();
        this.mShapeToBaseHeightFalloff = 2.0f;
        this.mShapeToConeK = 0.0f;
        this.mShapeToSecondConeK = 0.0f;
        this.mShapeToW0 = getDragW0();
    }

    private final void setShapeToOff() {
        this.mShapeToBaseHeight = 0.1f;
        this.mShapeToBaseHeightFalloff = 0.0f;
        this.mShapeToConeK = 0.0f;
        this.mShapeToSecondConeK = 0.0f;
        this.mShapeToW0 = 0.0f;
    }

    private final void setShapeToPlaying() {
        this.mShapeToBaseHeight = 0.6f;
        this.mShapeToBaseHeightFalloff = 0.6f;
        this.mShapeToConeK = 0.2f;
        this.mShapeToSecondConeK = 0.2f;
        this.mShapeToW0 = getPlayW0();
    }

    private final void startEnterDraggingAnim() {
        COUISpringForce spring;
        COUISpringForce spring2;
        captureAnimFromValues();
        setShapeToDragging();
        setAlphaToDragging();
        initShapeSpringAnim();
        COUISpringAnimation cOUISpringAnimation = this.mShapeSpringAnim;
        if (cOUISpringAnimation != null && (spring2 = cOUISpringAnimation.getSpring()) != null) {
            spring2.setResponse(0.4f);
            spring2.setBounce(0.0f);
        }
        COUISpringAnimation cOUISpringAnimation2 = this.mShapeSpringAnim;
        if (cOUISpringAnimation2 != null) {
            cOUISpringAnimation2.setStartValue(0.0f);
        }
        COUISpringAnimation cOUISpringAnimation3 = this.mShapeSpringAnim;
        if (cOUISpringAnimation3 != null) {
            cOUISpringAnimation3.animateToFinalPosition(10000.0f);
        }
        initAlphaSpringAnim();
        COUISpringAnimation cOUISpringAnimation4 = this.mAlphaSpringAnim;
        if (cOUISpringAnimation4 != null && (spring = cOUISpringAnimation4.getSpring()) != null) {
            spring.setResponse(0.4f);
            spring.setBounce(0.0f);
        }
        COUISpringAnimation cOUISpringAnimation5 = this.mAlphaSpringAnim;
        if (cOUISpringAnimation5 != null) {
            cOUISpringAnimation5.setStartValue(0.0f);
        }
        COUISpringAnimation cOUISpringAnimation6 = this.mAlphaSpringAnim;
        if (cOUISpringAnimation6 != null) {
            cOUISpringAnimation6.animateToFinalPosition(10000.0f);
        }
    }

    private final void startEnterPlayingAnim() {
        COUISpringForce spring;
        COUISpringForce spring2;
        captureAnimFromValues();
        setShapeToPlaying();
        setAlphaToPlaying();
        initShapeSpringAnim();
        COUISpringAnimation cOUISpringAnimation = this.mShapeSpringAnim;
        if (cOUISpringAnimation != null && (spring2 = cOUISpringAnimation.getSpring()) != null) {
            spring2.setResponse(0.9f);
            spring2.setBounce(0.0f);
        }
        COUISpringAnimation cOUISpringAnimation2 = this.mShapeSpringAnim;
        if (cOUISpringAnimation2 != null) {
            cOUISpringAnimation2.setStartValue(0.0f);
        }
        COUISpringAnimation cOUISpringAnimation3 = this.mShapeSpringAnim;
        if (cOUISpringAnimation3 != null) {
            cOUISpringAnimation3.animateToFinalPosition(10000.0f);
        }
        initAlphaSpringAnim();
        COUISpringAnimation cOUISpringAnimation4 = this.mAlphaSpringAnim;
        if (cOUISpringAnimation4 != null && (spring = cOUISpringAnimation4.getSpring()) != null) {
            spring.setResponse(0.3f);
            spring.setBounce(0.0f);
        }
        COUISpringAnimation cOUISpringAnimation5 = this.mAlphaSpringAnim;
        if (cOUISpringAnimation5 != null) {
            cOUISpringAnimation5.setStartValue(0.0f);
        }
        COUISpringAnimation cOUISpringAnimation6 = this.mAlphaSpringAnim;
        if (cOUISpringAnimation6 != null) {
            cOUISpringAnimation6.animateToFinalPosition(10000.0f);
        }
    }

    private final void startFadeOutAnim() {
        COUISpringForce spring;
        COUISpringForce spring2;
        captureAnimFromValues();
        setShapeToOff();
        setAlphaToOff();
        initShapeSpringAnim();
        COUISpringAnimation cOUISpringAnimation = this.mShapeSpringAnim;
        if (cOUISpringAnimation != null && (spring2 = cOUISpringAnimation.getSpring()) != null) {
            spring2.setResponse(0.4f);
            spring2.setBounce(0.0f);
        }
        COUISpringAnimation cOUISpringAnimation2 = this.mShapeSpringAnim;
        if (cOUISpringAnimation2 != null) {
            cOUISpringAnimation2.setStartValue(0.0f);
        }
        COUISpringAnimation cOUISpringAnimation3 = this.mShapeSpringAnim;
        if (cOUISpringAnimation3 != null) {
            cOUISpringAnimation3.animateToFinalPosition(10000.0f);
        }
        initAlphaSpringAnim();
        COUISpringAnimation cOUISpringAnimation4 = this.mAlphaSpringAnim;
        if (cOUISpringAnimation4 != null && (spring = cOUISpringAnimation4.getSpring()) != null) {
            spring.setResponse(0.4f);
            spring.setBounce(0.0f);
        }
        COUISpringAnimation cOUISpringAnimation5 = this.mAlphaSpringAnim;
        if (cOUISpringAnimation5 != null) {
            cOUISpringAnimation5.setStartValue(0.0f);
        }
        COUISpringAnimation cOUISpringAnimation6 = this.mAlphaSpringAnim;
        if (cOUISpringAnimation6 != null) {
            cOUISpringAnimation6.animateToFinalPosition(10000.0f);
        }
    }

    public final void cancelAnimations() {
        COUISpringAnimation cOUISpringAnimation = this.mShapeSpringAnim;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            cOUISpringAnimation.cancel();
        }
        COUISpringAnimation cOUISpringAnimation2 = this.mAlphaSpringAnim;
        if (cOUISpringAnimation2 == null || !cOUISpringAnimation2.isRunning()) {
            return;
        }
        cOUISpringAnimation2.cancel();
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    public void drawEffect(Canvas canvas, SeekBarState state, float progressPosition) {
        IConicLightDrawer iConicLightDrawer;
        COUISeekBarCore view;
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        Intrinsics.checkNotNullParameter(state, "state");
        if ((this.mCurrentMode != ConicLightMode.OFF || this.mTransitionDrawing) && (iConicLightDrawer = this.mConicLightDrawer) != null && iConicLightDrawer.isSupported() && (view = state.getView()) != null) {
            float seekBarLength = view.getSeekBarLength();
            if (seekBarLength <= 0.0f) {
                return;
            }
            float centerY = state.getCenterY();
            float progressStart = view.getProgressStart();
            float f3 = this.mConicLightHeight;
            canvas.save();
            canvas.translate(progressStart - this.mConicLightHorizontalPadding, centerY - (f3 / 2.0f));
            view.ensureThumbPositionForEffects();
            float thumbPosition = seekBarLength > 0.0f ? (state.getThumbPosition() - progressStart) / seekBarLength : RangesKt.coerceIn(state.getDrawProgressScale(), 0.0f, 1.0f);
            iConicLightDrawer.setRtl(view.getLayoutDirection() == 1 ? 1.0f : 0.0f);
            Pair<Float, Float> pairComputeModulatedHeightParams = computeModulatedHeightParams(thumbPosition, this.mBaseConicHeight, this.mBaseConicHeightFalloff);
            float fFloatValue = pairComputeModulatedHeightParams.component1().floatValue();
            float fFloatValue2 = pairComputeModulatedHeightParams.component2().floatValue();
            iConicLightDrawer.setHeight(fFloatValue);
            iConicLightDrawer.setHeightFalloff(fFloatValue2);
            iConicLightDrawer.draw(canvas, seekBarLength + (this.mConicLightHorizontalPadding * 2.0f), f3, thumbPosition, seekBarLength);
            canvas.restore();
        }
    }

    /* JADX INFO: renamed from: getConicLightColor, reason: from getter */
    public final int getMConicLightColor() {
        return this.mConicLightColor;
    }

    /* JADX INFO: renamed from: getCurrentMode, reason: from getter */
    public final ConicLightMode getMCurrentMode() {
        return this.mCurrentMode;
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    public DrawPhase getDrawPhase() {
        return IEffectRenderer.DefaultImpls.getDrawPhase(this);
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    /* JADX INFO: renamed from: getEffectParams */
    public EffectParams getMParams() {
        return this.mParams;
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    /* JADX INFO: renamed from: isEnabled */
    public boolean getMEnabled() {
        return true;
    }

    /* JADX INFO: renamed from: isTransitionDrawing, reason: from getter */
    public final boolean getMTransitionDrawing() {
        return this.mTransitionDrawing;
    }

    public final void release() {
        cancelAnimations();
        this.mShapeSpringAnim = null;
        this.mAlphaSpringAnim = null;
        this.mTransitionDrawing = false;
    }

    public final void resetToOff() {
        cancelAnimations();
        this.mCurrentMode = ConicLightMode.OFF;
        this.mTransitionDrawing = false;
        this.mBaseConicHeight = 0.1f;
        this.mBaseConicHeightFalloff = 0.0f;
        this.mConicConeK = 0.0f;
        this.mConicSecondConeK = 0.0f;
        this.mConicAlpha1 = 0.0f;
        this.mConicAlpha2 = 0.0f;
        this.mConicW0 = 0.0f;
        applyParamsToDrawer();
    }

    public final void setConicLightColor(int color) {
        if (this.mConicLightColor == color) {
            return;
        }
        this.mConicLightColor = color;
        IConicLightDrawer iConicLightDrawer = this.mConicLightDrawer;
        if (iConicLightDrawer != null) {
            iConicLightDrawer.setConicLightColor(color);
        }
    }

    public final void setConicLightDrawer(IConicLightDrawer drawer) {
        this.mConicLightDrawer = drawer;
        if (drawer != null) {
            drawer.setConicLightColor(this.mConicLightColor);
        }
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    public void setEffectParams(EffectParams params) {
        Intrinsics.checkNotNullParameter(params, "params");
        if (params instanceof ConicLightEffectParams) {
            this.mParams = (ConicLightEffectParams) params;
        }
    }

    public final void setSizeType(IntentSeekBarSize sizeType) {
        Intrinsics.checkNotNullParameter(sizeType, "sizeType");
        this.mSizeType = sizeType;
    }

    public final void transitionTo(ConicLightMode mode) {
        Intrinsics.checkNotNullParameter(mode, "mode");
        ConicLightMode conicLightMode = this.mCurrentMode;
        ConicLightMode conicLightMode2 = ConicLightMode.PLAYING;
        boolean z2 = conicLightMode == conicLightMode2 || conicLightMode == ConicLightMode.DRAGGING;
        boolean z3 = mode == conicLightMode2 || mode == ConicLightMode.DRAGGING;
        this.mCurrentMode = mode;
        if (mode == conicLightMode2) {
            this.mTransitionDrawing = false;
            startEnterPlayingAnim();
        } else if (mode == ConicLightMode.DRAGGING) {
            this.mTransitionDrawing = false;
            startEnterDraggingAnim();
        } else {
            if (!z2 || z3) {
                return;
            }
            cancelAnimations();
            this.mTransitionDrawing = true;
            startFadeOutAnim();
        }
    }
}