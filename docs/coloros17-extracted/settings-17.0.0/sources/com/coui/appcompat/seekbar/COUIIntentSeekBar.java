package com.coui.appcompat.seekbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.seekbar.animation.IAnimationHandler;
import com.coui.appcompat.seekbar.animation.LoadingDragReturnHandler;
import com.coui.appcompat.seekbar.animation.ThumbStateAnimationHandler;
import com.coui.appcompat.seekbar.core.COUISeekBarAdvanced;
import com.coui.appcompat.seekbar.core.SeekBarState;
import com.coui.appcompat.seekbar.effect.BlurBlendEffectRenderer;
import com.coui.appcompat.seekbar.effect.ConicLightEffectRenderer;
import com.coui.appcompat.seekbar.effect.LoadingEffectRenderer;
import com.coui.appcompat.seekbar.handler.IOrientationHandler;
import com.coui.appcompat.seekbar.renderer.IProgressRenderer;
import com.coui.appcompat.seekbar.renderer.IThumbRenderer;
import com.coui.appcompat.seekbar.renderer.IntentConicProgressRenderer;
import com.coui.appcompat.seekbar.renderer.IntentThumbRenderer;
import com.coui.appcompat.seekbar.renderer.SecondaryProgressRenderer;
import com.coui.appcompat.seekbar.shader.ConicLightPaintHostDrawer;
import com.coui.appcompat.seekbar.shader.IConicLightDrawer;
import com.oplus.settings.feature.display.atmospherelight.utils.AtmosphereLightStatisticsUtils;
import com.support.seekbar.R;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.math.MathKt;
import kotlin.ranges.RangesKt;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000Í\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b+\n\u0002\u0018\u0002\n\u0002\b\u0016\n\u0002\u0018\u0002\n\u0002\b'*\u00013\b\u0017\u0018\u0000 ¾\u00012\u00020\u0001:\b¾\u0001¿\u0001À\u0001Á\u0001B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007¢\u0006\u0002\u0010\tJ\u0010\u0010A\u001a\u00020B2\u0006\u0010C\u001a\u00020\u0017H\u0002J\b\u0010D\u001a\u00020BH\u0002J\b\u0010E\u001a\u00020BH\u0002J\u0010\u0010F\u001a\u00020B2\u0006\u0010G\u001a\u000201H\u0002J\b\u0010H\u001a\u00020BH\u0002J\b\u0010I\u001a\u00020BH\u0002J\b\u0010J\u001a\u00020BH\u0002J\b\u0010K\u001a\u00020BH\u0002J\u0006\u0010L\u001a\u00020BJ\b\u0010M\u001a\u00020NH\u0014J\b\u0010O\u001a\u00020PH\u0014J\u0010\u0010Q\u001a\u00020B2\u0006\u0010R\u001a\u00020SH\u0014J\u0010\u0010T\u001a\u00020B2\u0006\u0010R\u001a\u00020SH\u0014J\b\u0010U\u001a\u00020BH\u0002J\u0010\u0010V\u001a\u0002012\u0006\u0010W\u001a\u00020XH\u0016J\b\u0010Y\u001a\u00020\u0007H\u0007J\b\u0010Z\u001a\u000201H\u0014J\u0010\u0010[\u001a\u00020B2\u0006\u0010\\\u001a\u00020\rH\u0002J\b\u0010]\u001a\u000201H\u0014J\u0006\u0010^\u001a\u00020$J\u0010\u0010_\u001a\u0002012\u0006\u0010W\u001a\u00020XH\u0016J\b\u0010`\u001a\u00020\u0007H\u0016J\u0006\u0010a\u001a\u00020/J\u0010\u0010b\u001a\u00020B2\u0006\u0010\\\u001a\u00020\rH\u0002J\u0006\u0010c\u001a\u00020\u0007J\u0018\u0010d\u001a\u00020B2\u0006\u0010e\u001a\u00020$2\u0006\u0010f\u001a\u00020$H\u0002J\b\u0010g\u001a\u00020\u000fH\u0002J*\u0010h\u001a\u00020B2\u0006\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u0014J*\u0010i\u001a\u00020B2\u0006\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u0002J\b\u0010j\u001a\u00020BH\u0002J\b\u0010k\u001a\u00020\u000fH\u0003J\u0006\u0010l\u001a\u00020\u000fJ\b\u0010m\u001a\u00020\u000fH\u0007J\u0006\u0010n\u001a\u00020\u000fJ\u0006\u0010o\u001a\u00020\u000fJ\u0006\u0010p\u001a\u00020\u000fJ\b\u0010q\u001a\u00020\u000fH\u0014J\u0006\u0010r\u001a\u00020\u000fJ\u0006\u0010s\u001a\u00020\u000fJ\u0006\u0010t\u001a\u00020\u000fJ\u0006\u0010u\u001a\u00020\u000fJ\u0006\u0010v\u001a\u00020\u000fJ\b\u0010w\u001a\u00020BH\u0014J\u0010\u0010x\u001a\u00020B2\u0006\u0010y\u001a\u000201H\u0014J\u0018\u0010z\u001a\u00020B2\u0006\u0010{\u001a\u00020\u00072\u0006\u0010|\u001a\u00020\u0007H\u0014J\b\u0010}\u001a\u00020BH\u0014J\u0018\u0010~\u001a\u00020B2\u0006\u0010e\u001a\u00020$2\u0006\u0010f\u001a\u00020$H\u0002J\u001a\u0010\u007f\u001a\u00020B2\u0007\u0010\u0080\u0001\u001a\u00020\u00072\u0007\u0010\u0081\u0001\u001a\u00020\u000fH\u0016J\u0013\u0010\u0082\u0001\u001a\u00020\u000f2\b\u0010\u0083\u0001\u001a\u00030\u0084\u0001H\u0016J\u0019\u0010\u0085\u0001\u001a\u00020B2\u0006\u0010e\u001a\u00020\u00172\u0006\u0010f\u001a\u00020\u0017H\u0002J\t\u0010\u0086\u0001\u001a\u00020BH\u0002J\t\u0010\u0087\u0001\u001a\u00020BH\u0014J\t\u0010\u0088\u0001\u001a\u00020\u0017H\u0002J\u0011\u0010\u0089\u0001\u001a\u00020\u00072\u0006\u0010G\u001a\u00020\u0007H\u0002J\u0012\u0010\u008a\u0001\u001a\u00020B2\t\b\u0001\u0010\u008b\u0001\u001a\u00020\u0007J\u0010\u0010\u008c\u0001\u001a\u00020B2\u0007\u0010\u008d\u0001\u001a\u00020\u000fJ\u0010\u0010\u008e\u0001\u001a\u00020B2\u0007\u0010\u008f\u0001\u001a\u00020\u000fJ\u0010\u0010\u0090\u0001\u001a\u00020B2\u0007\u0010\u0091\u0001\u001a\u00020\u000fJ\u0010\u0010\u0092\u0001\u001a\u00020B2\u0007\u0010\u0093\u0001\u001a\u00020\u000fJ\u0010\u0010\u0094\u0001\u001a\u00020B2\u0007\u0010\u008d\u0001\u001a\u00020\u000fJ\u0012\u0010\u0095\u0001\u001a\u00020B2\t\u0010\u0096\u0001\u001a\u0004\u0018\u00010 J\u0012\u0010\u0097\u0001\u001a\u00020B2\t\u0010\u0096\u0001\u001a\u0004\u0018\u00010\"J\u000f\u0010\u0098\u0001\u001a\u00020B2\u0006\u0010W\u001a\u00020$J\u0015\u0010\u0099\u0001\u001a\u00020B2\n\u0010\u009a\u0001\u001a\u0005\u0018\u00010\u009b\u0001H\u0016J\u0012\u0010\u009c\u0001\u001a\u00020B2\u0007\u0010\u009d\u0001\u001a\u00020\u0007H\u0016J\u0015\u0010\u009e\u0001\u001a\u00020B2\n\u0010\u009f\u0001\u001a\u0005\u0018\u00010\u009b\u0001H\u0016J+\u0010 \u0001\u001a\u00020B2\n\u0010\u009f\u0001\u001a\u0005\u0018\u00010\u009b\u00012\n\u0010\u009a\u0001\u001a\u0005\u0018\u00010\u009b\u00012\n\u0010¡\u0001\u001a\u0005\u0018\u00010\u009b\u0001J\u0010\u0010¢\u0001\u001a\u00020B2\u0007\u0010£\u0001\u001a\u00020\u000fJ\u0010\u0010¤\u0001\u001a\u00020B2\u0007\u0010£\u0001\u001a\u00020\u000fJ\u0010\u0010¥\u0001\u001a\u00020B2\u0007\u0010¦\u0001\u001a\u00020/J\u001b\u0010§\u0001\u001a\u00020B2\u0007\u0010¨\u0001\u001a\u0002012\u0007\u0010©\u0001\u001a\u00020\u000fH\u0014J\u000f\u0010ª\u0001\u001a\u00020B2\u0006\u0010G\u001a\u00020\u0007J\t\u0010«\u0001\u001a\u00020\u000fH\u0002J\t\u0010¬\u0001\u001a\u00020\u000fH\u0003J\t\u0010\u00ad\u0001\u001a\u00020\u000fH\u0002J\u0012\u0010®\u0001\u001a\u00020\u000f2\u0007\u0010\u0081\u0001\u001a\u00020\u000fH\u0002J\t\u0010¯\u0001\u001a\u00020BH\u0002J\u001b\u0010°\u0001\u001a\u00020B2\u0007\u0010±\u0001\u001a\u00020\u000f2\u0007\u0010²\u0001\u001a\u00020\u000fH\u0014J\t\u0010³\u0001\u001a\u00020BH\u0002J\t\u0010´\u0001\u001a\u00020BH\u0002J\t\u0010µ\u0001\u001a\u00020BH\u0014J$\u0010¶\u0001\u001a\u00020\u000f2\u0007\u0010·\u0001\u001a\u0002012\u0007\u0010¸\u0001\u001a\u0002012\u0007\u0010¹\u0001\u001a\u00020\u0007H\u0002J\t\u0010º\u0001\u001a\u00020BH\u0002J\t\u0010»\u0001\u001a\u00020BH\u0014J\t\u0010¼\u0001\u001a\u00020BH\u0014J\t\u0010½\u0001\u001a\u00020\u000fH\u0014R\u000e\u0010\n\u001a\u00020\u000bX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0010\u001a\u00020\u00078\u0002@\u0002X\u0083\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX\u0082.¢\u0006\u0002\n\u0000R\u0010\u0010\u001f\u001a\u0004\u0018\u00010 X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010!\u001a\u0004\u0018\u00010\"X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020(X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010*\u001a\u0004\u0018\u00010+X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020-X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020/X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00100\u001a\u000201X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u00102\u001a\u000203X\u0082\u0004¢\u0006\u0004\n\u0002\u00104R\u001b\u00105\u001a\u0002068BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b9\u0010:\u001a\u0004\b7\u00108R\u000e\u0010;\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010<\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0004\n\u0002\u0010=R\u0012\u0010>\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0004\n\u0002\u0010=R\u0012\u0010?\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0004\n\u0002\u0010=R\u000e\u0010@\u001a\u00020\u000fX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006Â\u0001"}, d2 = {"Lcom/coui/appcompat/seekbar/COUIIntentSeekBar;", "Lcom/coui/appcompat/seekbar/core/COUISeekBarAdvanced;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "defStyleRes", "(Landroid/content/Context;Landroid/util/AttributeSet;II)V", "mBlurBlendRenderer", "Lcom/coui/appcompat/seekbar/effect/BlurBlendEffectRenderer;", "mColorResult", "", "mColorsDirty", "", "mConicLightColor", "mConicLightDrawer", "Lcom/coui/appcompat/seekbar/shader/IConicLightDrawer;", "mConicLightEnabled", "mConicLightRenderer", "Lcom/coui/appcompat/seekbar/effect/ConicLightEffectRenderer;", "mCurrentVisualState", "Lcom/coui/appcompat/seekbar/COUIIntentSeekBar$VisualState;", "mFollowSystemTheme", "mHoldToSpeedUpConeLightActive", "mLastDrawnSecondaryProgressForSnap", "mLoadingDragReturnHandler", "Lcom/coui/appcompat/seekbar/animation/LoadingDragReturnHandler;", "mLoadingRenderer", "Lcom/coui/appcompat/seekbar/effect/LoadingEffectRenderer;", "mOnDragHorizontalOffsetChangeListener", "Lcom/coui/appcompat/seekbar/COUIIntentSeekBar$OnDragHorizontalOffsetChangeListener;", "mOnLoadingDragReturnCompleteListener", "Lcom/coui/appcompat/seekbar/COUIIntentSeekBar$OnLoadingDragReturnCompleteListener;", "mPlaybackState", "Lcom/coui/appcompat/seekbar/PlaybackState;", "mPrimaryAttrId", "mProgrammaticConicSeekActive", "mProgressClipPath", "Landroid/graphics/Path;", "mQuaternaryAttrId", "mSecondaryProgressAnim", "Lcom/coui/appcompat/animation/dynamicanimation/COUISpringAnimation;", "mSecondaryProgressRenderer", "Lcom/coui/appcompat/seekbar/renderer/SecondaryProgressRenderer;", "mSize", "Lcom/coui/appcompat/seekbar/IntentSeekBarSize;", "mThumbOutShadeRadius", "", "mThumbStateCallback", "com/coui/appcompat/seekbar/COUIIntentSeekBar$mThumbStateCallback$1", "Lcom/coui/appcompat/seekbar/COUIIntentSeekBar$mThumbStateCallback$1;", "mThumbStateHandler", "Lcom/coui/appcompat/seekbar/animation/ThumbStateAnimationHandler;", "getMThumbStateHandler", "()Lcom/coui/appcompat/seekbar/animation/ThumbStateAnimationHandler;", "mThumbStateHandler$delegate", "Lkotlin/Lazy;", "mUseMaterialEffect", "mUserBackgroundColor", "Ljava/lang/Integer;", "mUserProgressColor", "mUserSecondaryProgressColor", "mWasDragging", "applyConicLightModeForVisualState", "", "visual", "applyIntentColors", "applyIntentThumbColor", "applySecondaryProgress", "value", "applySizeHeight", "applyThumbSize", "applyVisualState", "clearProgrammaticConicSeekVisualIfNeeded", "collapseConeLightForSeek", "createProgressRenderer", "Lcom/coui/appcompat/seekbar/renderer/IProgressRenderer;", "createThumbRenderer", "Lcom/coui/appcompat/seekbar/renderer/IThumbRenderer;", "drawActiveTrack", "canvas", "Landroid/graphics/Canvas;", "drawBackground", "executeThumbReleaseAfterDrag", "getBackgroundHorizontalInset", "state", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "getConicLightColor", "getDefaultBackgroundEnlargeScale", "getFixedColors", "out", "getMinEnlargeHeight", "getPlaybackState", "getProgressRectHeight", "getSecondaryProgress", "getSize", "getThemeFallbackColors", "getUseMaterialEffect", "handleThumbStateTransition", "oldState", "newState", "hasCustomUserColors", "initAdvancedFeatures", "initIntentAttributes", "initSecondaryProgressAnim", "isConicLightCapabilitySupported", "isConicLightEnabled", "isConicLightProgressStyle", "isDraggingState", "isFollowSystemTheme", "isFollowThumb", "isInvalidateThrottleEnabled", "isLargeSize", "isLoadingDragReturnEnabled", "isLoadingDragReturnInProgress", "isShowSecondaryProgress", "isShowThumb", "onDetachedFromWindow", "onHeightSpringUpdate", "fraction", "onMeasure", "widthMeasureSpec", "heightMeasureSpec", "onPhysicsFlingEnd", "onPlaybackStateChanged", "onProgressAnimationEnd", "targetProgress", "fromUser", "onTouchEvent", NotificationCompat.CATEGORY_EVENT, "Landroid/view/MotionEvent;", "onVisualStateChanged", "refreshConicLightActivation", "releaseThumbScaleAnim", "resolveVisualState", "sanitizeUseMaterialEffect", "setConicLightColor", TypedValues.Custom.S_COLOR, "setConicLightEnabled", "enabled", "setFollowSystemTheme", "follow", "setFollowThumb", "followThumb", "setHoldToSpeedUpConeLightActive", "active", "setLoadingDragReturnEnabled", "setOnDragHorizontalOffsetChangeListener", "listener", "setOnLoadingDragReturnCompleteListener", "setPlaybackState", "setProgressColor", "progressColor", "Landroid/content/res/ColorStateList;", "setSecondaryProgress", "secondaryProgress", "setSeekBarBackgroundColor", "backgroundColor", "setSeekBarColors", "secondaryProgressColor", "setShowSecondaryProgress", "show", "setShowThumb", "setSize", "size", "setTouchScale", "scale", "updateDrawProgressScale", "setUseMaterialEffect", "shouldDrawThumbNow", "shouldEquipConicLightDrawer", "shouldPlayingAuxiliaryEffectsUseDraggingConeLightVisual", "shouldProgrammaticConicSeekUseDraggingVisual", "stopSecondaryProgressAnim", "stopTrackingTouch", "isFromUser", "isTouching", "syncConicLightToVisualState", "syncSecondaryProgressScale", "touchAnim", "trySnapSecondaryProgress", AtmosphereLightStatisticsUtils.VAL_ACTION_START, AtmosphereLightStatisticsUtils.VAL_ACTION_END, TypedValues.AttributesType.S_TARGET, "updateBlurBlendRendererState", "updateColors", "updateState", "useDragTransitionAnimation", "Companion", "OnDragHorizontalOffsetChangeListener", "OnLoadingDragReturnCompleteListener", "VisualState", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class COUIIntentSeekBar extends COUISeekBarAdvanced {
    public static final int DEFAULT = -1;
    private static final float DEFAULT_BACKGROUND_ENLARGE_SCALE = 6.0f;
    public static final int DEFAULT_CONIC_LIGHT_COLOR = -493674;
    public static final float DEFAULT_INVALIDATE_PIXEL_THRESHOLD = 1.0f;
    private static final float DEFAULT_PROGRESS_ENLARGE_SCALE = 4.0f;
    public static final int FORCE_DISABLED = 0;
    public static final int FORCE_ENABLE = 1;
    private static final float MIN_ENLARGE_HEIGHT_DP = 2.0f;
    private static final float MIN_TOUCH_TARGET_HEIGHT_DP = 24.0f;
    private static final float RADIUS_TO_DIAMETER_FACTOR = 2.0f;
    private static final float SECONDARY_PROGRESS_SPRING_BOUNCE = 0.0f;
    private static final float SECONDARY_PROGRESS_SPRING_RESPONSE = 0.5f;
    private static final String TAG = "COUIIntentSeekBar";
    private BlurBlendEffectRenderer mBlurBlendRenderer;
    private final int[] mColorResult;
    private boolean mColorsDirty;
    private int mConicLightColor;
    private IConicLightDrawer mConicLightDrawer;
    private boolean mConicLightEnabled;
    private ConicLightEffectRenderer mConicLightRenderer;
    private VisualState mCurrentVisualState;
    private boolean mFollowSystemTheme;
    private boolean mHoldToSpeedUpConeLightActive;
    private int mLastDrawnSecondaryProgressForSnap;
    private final LoadingDragReturnHandler mLoadingDragReturnHandler;
    private LoadingEffectRenderer mLoadingRenderer;
    private OnDragHorizontalOffsetChangeListener mOnDragHorizontalOffsetChangeListener;
    private OnLoadingDragReturnCompleteListener mOnLoadingDragReturnCompleteListener;
    private PlaybackState mPlaybackState;
    private final int mPrimaryAttrId;
    private boolean mProgrammaticConicSeekActive;
    private final Path mProgressClipPath;
    private final int mQuaternaryAttrId;
    private COUISpringAnimation mSecondaryProgressAnim;
    private SecondaryProgressRenderer mSecondaryProgressRenderer;
    private IntentSeekBarSize mSize;
    private float mThumbOutShadeRadius;
    private final COUIIntentSeekBar$mThumbStateCallback$1 mThumbStateCallback;

    /* JADX INFO: renamed from: mThumbStateHandler$delegate, reason: from kotlin metadata */
    private final Lazy mThumbStateHandler;
    private int mUseMaterialEffect;
    private Integer mUserBackgroundColor;
    private Integer mUserProgressColor;
    private Integer mUserSecondaryProgressColor;
    private boolean mWasDragging;

    /* JADX INFO: renamed from: com.coui.appcompat.seekbar.COUIIntentSeekBar$1, reason: invalid class name */
    @Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "offsetX", "", "invoke"}, k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class AnonymousClass1 extends Lambda implements Function1<Float, Unit> {
        public AnonymousClass1() {
            super(1);
        }

        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ Unit invoke(Float f3) {
            invoke(f3.floatValue());
            return Unit.INSTANCE;
        }

        public final void invoke(float f3) {
            OnDragHorizontalOffsetChangeListener onDragHorizontalOffsetChangeListenerAccess$getMOnDragHorizontalOffsetChangeListener$p = COUIIntentSeekBar.access$getMOnDragHorizontalOffsetChangeListener$p(COUIIntentSeekBar.this);
            if (onDragHorizontalOffsetChangeListenerAccess$getMOnDragHorizontalOffsetChangeListener$p != null) {
                onDragHorizontalOffsetChangeListenerAccess$getMOnDragHorizontalOffsetChangeListener$p.onDragHorizontalOffsetChanged(COUIIntentSeekBar.this, f3);
            }
        }
    }

    @Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\bæ\u0080\u0001\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&¨\u0006\b"}, d2 = {"Lcom/coui/appcompat/seekbar/COUIIntentSeekBar$OnDragHorizontalOffsetChangeListener;", "", "onDragHorizontalOffsetChanged", "", "seekBar", "Lcom/coui/appcompat/seekbar/COUIIntentSeekBar;", "offsetX", "", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public interface OnDragHorizontalOffsetChangeListener {
        void onDragHorizontalOffsetChanged(COUIIntentSeekBar seekBar, float offsetX);
    }

    @Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bæ\u0080\u0001\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&¨\u0006\u0006"}, d2 = {"Lcom/coui/appcompat/seekbar/COUIIntentSeekBar$OnLoadingDragReturnCompleteListener;", "", "onLoadingDragReturnComplete", "", "seekBar", "Lcom/coui/appcompat/seekbar/COUIIntentSeekBar;", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public interface OnLoadingDragReturnCompleteListener {
        void onLoadingDragReturnComplete(COUIIntentSeekBar seekBar);
    }

    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0082\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007¨\u0006\b"}, d2 = {"Lcom/coui/appcompat/seekbar/COUIIntentSeekBar$VisualState;", "", "(Ljava/lang/String;I)V", "STATIC", "PLAYING_CONE_LIGHT", "DRAGGING_CONE_LIGHT", "LOADING_FULL", "LOADING_PARTIAL", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public enum VisualState {
        STATIC,
        PLAYING_CONE_LIGHT,
        DRAGGING_CONE_LIGHT,
        LOADING_FULL,
        LOADING_PARTIAL;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

        public static EnumEntries<VisualState> getEntries() {
            return $ENTRIES;
        }
    }

    @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;
        public static final /* synthetic */ int[] $EnumSwitchMapping$1;
        public static final /* synthetic */ int[] $EnumSwitchMapping$2;

        static {
            int[] iArr = new int[IntentSeekBarSize.values().length];
            try {
                iArr[IntentSeekBarSize.DEFAULT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[IntentSeekBarSize.SMALL.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[IntentSeekBarSize.LARGE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            $EnumSwitchMapping$0 = iArr;
            int[] iArr2 = new int[VisualState.values().length];
            try {
                iArr2[VisualState.PLAYING_CONE_LIGHT.ordinal()] = 1;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                iArr2[VisualState.DRAGGING_CONE_LIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                iArr2[VisualState.LOADING_FULL.ordinal()] = 3;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                iArr2[VisualState.LOADING_PARTIAL.ordinal()] = 4;
            } catch (NoSuchFieldError unused7) {
            }
            $EnumSwitchMapping$1 = iArr2;
            int[] iArr3 = new int[PlaybackState.values().length];
            try {
                iArr3[PlaybackState.IDLE.ordinal()] = 1;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                iArr3[PlaybackState.LOADING_INITIAL.ordinal()] = 2;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                iArr3[PlaybackState.PLAYING.ordinal()] = 3;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                iArr3[PlaybackState.PAUSED.ordinal()] = 4;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                iArr3[PlaybackState.LOADING_BUFFERING.ordinal()] = 5;
            } catch (NoSuchFieldError unused12) {
            }
            $EnumSwitchMapping$2 = iArr3;
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIIntentSeekBar(Context context) {
        this(context, null, 0, 0, 14, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public static final /* synthetic */ void access$applyVisualState(COUIIntentSeekBar cOUIIntentSeekBar) {
        cOUIIntentSeekBar.applyVisualState();
    }

    public static final /* synthetic */ void access$executeThumbReleaseAfterDrag(COUIIntentSeekBar cOUIIntentSeekBar) {
        cOUIIntentSeekBar.executeThumbReleaseAfterDrag();
    }

    public static final /* synthetic */ IAnimationHandler access$getMAnimationHandler(COUIIntentSeekBar cOUIIntentSeekBar) {
        return cOUIIntentSeekBar.getMAnimationHandler();
    }

    public static final /* synthetic */ OnDragHorizontalOffsetChangeListener access$getMOnDragHorizontalOffsetChangeListener$p(COUIIntentSeekBar cOUIIntentSeekBar) {
        return cOUIIntentSeekBar.mOnDragHorizontalOffsetChangeListener;
    }

    public static final /* synthetic */ OnLoadingDragReturnCompleteListener access$getMOnLoadingDragReturnCompleteListener$p(COUIIntentSeekBar cOUIIntentSeekBar) {
        return cOUIIntentSeekBar.mOnLoadingDragReturnCompleteListener;
    }

    public static final /* synthetic */ COUIIntentSeekBar$mThumbStateCallback$1 access$getMThumbStateCallback$p(COUIIntentSeekBar cOUIIntentSeekBar) {
        return cOUIIntentSeekBar.mThumbStateCallback;
    }

    private final void applyConicLightModeForVisualState(VisualState visual) {
        ConicLightEffectRenderer.ConicLightMode conicLightMode;
        if (isConicLightProgressStyle()) {
            int i3 = WhenMappings.$EnumSwitchMapping$1[visual.ordinal()];
            if (i3 != 1) {
                conicLightMode = i3 != 2 ? ConicLightEffectRenderer.ConicLightMode.OFF : ConicLightEffectRenderer.ConicLightMode.DRAGGING;
            } else {
                conicLightMode = ConicLightEffectRenderer.ConicLightMode.PLAYING;
            }
            ConicLightEffectRenderer conicLightEffectRenderer = this.mConicLightRenderer;
            if (conicLightEffectRenderer == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mConicLightRenderer");
                conicLightEffectRenderer = null;
            }
            conicLightEffectRenderer.transitionTo(conicLightMode);
        }
    }

    /* JADX WARN: Code duplicated, block: B:19:0x0067  */
    /* JADX WARN: Code duplicated, block: B:22:0x0076  */
    /* JADX WARN: Code duplicated, block: B:23:0x007b  */
    /* JADX WARN: Code duplicated, block: B:26:0x0087  */
    /* JADX WARN: Code duplicated, block: B:27:0x008c  */
    /* JADX WARN: Code duplicated, block: B:30:0x0096  */
    /* JADX WARN: Code duplicated, block: B:33:0x009d  */
    private final void applyIntentColors() {
        int i3;
        int attrColor;
        int attrColor2;
        int iIntValue;
        Integer num;
        Integer num2;
        int iIntValue2;
        Integer num3;
        int iIntValue3;
        IProgressRenderer mProgressRenderer;
        SecondaryProgressRenderer secondaryProgressRenderer;
        if (!this.mFollowSystemTheme) {
            getFixedColors(this.mColorResult);
            int[] iArr = this.mColorResult;
            i3 = iArr[0];
            attrColor = iArr[1];
            attrColor2 = iArr[2];
        } else {
            if (this.mQuaternaryAttrId != 0 && this.mPrimaryAttrId != 0) {
                iIntValue = COUIContextUtil.getAttrColor(getContext(), this.mQuaternaryAttrId, 0);
                attrColor = COUIContextUtil.getAttrColor(getContext(), this.mPrimaryAttrId, 0);
                attrColor2 = COUIContextUtil.getAttrColor(getContext(), this.mQuaternaryAttrId, 0);
                if (iIntValue == 0 || attrColor == 0 || attrColor2 == 0) {
                    getThemeFallbackColors(this.mColorResult);
                    int[] iArr2 = this.mColorResult;
                    i3 = iArr2[0];
                    attrColor = iArr2[1];
                    attrColor2 = iArr2[2];
                }
                SeekBarState state = getState();
                num = this.mUserBackgroundColor;
                if (num != null) {
                    iIntValue = num.intValue();
                }
                state.setBackgroundColor(iIntValue);
                SeekBarState state2 = getState();
                num2 = this.mUserProgressColor;
                if (num2 != null) {
                    iIntValue2 = num2.intValue();
                } else {
                    iIntValue2 = attrColor;
                }
                state2.setProgressColor(iIntValue2);
                SeekBarState state3 = getState();
                num3 = this.mUserSecondaryProgressColor;
                if (num3 != null) {
                    iIntValue3 = num3.intValue();
                } else {
                    iIntValue3 = attrColor2;
                }
                state3.setSecondaryProgressColor(iIntValue3);
                mProgressRenderer = getMProgressRenderer();
                if (mProgressRenderer != null) {
                    mProgressRenderer.setProgressColor(attrColor);
                }
                secondaryProgressRenderer = this.mSecondaryProgressRenderer;
                if (secondaryProgressRenderer == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("mSecondaryProgressRenderer");
                    secondaryProgressRenderer = null;
                }
                secondaryProgressRenderer.setSecondaryProgressColor(attrColor2);
                applyIntentThumbColor();
            }
            getThemeFallbackColors(this.mColorResult);
            int[] iArr3 = this.mColorResult;
            i3 = iArr3[0];
            attrColor = iArr3[1];
            attrColor2 = iArr3[2];
        }
        iIntValue = i3;
        SeekBarState state4 = getState();
        num = this.mUserBackgroundColor;
        if (num != null) {
            iIntValue = num.intValue();
        }
        state4.setBackgroundColor(iIntValue);
        SeekBarState state5 = getState();
        num2 = this.mUserProgressColor;
        if (num2 != null) {
            iIntValue2 = num2.intValue();
        } else {
            iIntValue2 = attrColor;
        }
        state5.setProgressColor(iIntValue2);
        SeekBarState state6 = getState();
        num3 = this.mUserSecondaryProgressColor;
        if (num3 != null) {
            iIntValue3 = num3.intValue();
        } else {
            iIntValue3 = attrColor2;
        }
        state6.setSecondaryProgressColor(iIntValue3);
        mProgressRenderer = getMProgressRenderer();
        if (mProgressRenderer != null) {
            mProgressRenderer.setProgressColor(attrColor);
        }
        secondaryProgressRenderer = this.mSecondaryProgressRenderer;
        if (secondaryProgressRenderer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mSecondaryProgressRenderer");
            secondaryProgressRenderer = null;
        }
        secondaryProgressRenderer.setSecondaryProgressColor(attrColor2);
        applyIntentThumbColor();
    }

    private final void applyIntentThumbColor() {
        int color = this.mFollowSystemTheme ? COUIContextUtil.getColor(getContext(), R.color.coui_seekbar_intent_thumb_color) : COUIContextUtil.getColor(getContext(), R.color.coui_seekbar_intent_thumb_color_fixed);
        getState().setThumbColor(color);
        IThumbRenderer mThumbRenderer = getMThumbRenderer();
        if (mThumbRenderer != null) {
            mThumbRenderer.setThumbColor(color);
        }
    }

    private final void applySecondaryProgress(float value) {
        SeekBarState state = getState();
        state.setSecondaryProgress(RangesKt.coerceIn(MathKt.roundToInt(value), state.getMin(), state.getMax()));
        syncSecondaryProgressScale();
        invalidate();
    }

    private final void applySizeHeight() {
        Pair pair;
        int i3 = WhenMappings.$EnumSwitchMapping$0[this.mSize.ordinal()];
        if (i3 == 1) {
            pair = TuplesKt.to(Integer.valueOf(R.dimen.coui_seekbar_intent_height_default), Integer.valueOf(R.dimen.coui_seekbar_intent_max_background_height_default));
        } else if (i3 == 2) {
            pair = TuplesKt.to(Integer.valueOf(R.dimen.coui_seekbar_intent_height_small), Integer.valueOf(R.dimen.coui_seekbar_intent_max_background_height_small));
        } else {
            if (i3 != 3) {
                throw new NoWhenBranchMatchedException();
            }
            pair = TuplesKt.to(Integer.valueOf(R.dimen.coui_seekbar_intent_height_large), Integer.valueOf(R.dimen.coui_seekbar_intent_max_background_height_large));
        }
        int iIntValue = ((Number) pair.component1()).intValue();
        int iIntValue2 = ((Number) pair.component2()).intValue();
        float dimension = getResources().getDimension(iIntValue);
        float dimension2 = getResources().getDimension(iIntValue2);
        getState().setProgressHeight(dimension);
        getState().setBackgroundHeight(dimension);
        getState().setCurProgressHeight(dimension);
        getState().setCurBackgroundHeight(dimension);
        setMMaxBackgroundHeight(dimension2);
        applyThumbSize();
    }

    private final void applyThumbSize() {
        Pair pair;
        int i3 = WhenMappings.$EnumSwitchMapping$0[this.mSize.ordinal()];
        if (i3 == 1) {
            pair = TuplesKt.to(Integer.valueOf(R.dimen.coui_seekbar_intent_thumb_radius_default), Integer.valueOf(R.dimen.coui_seekbar_intent_thumb_max_radius_default));
        } else if (i3 == 2) {
            pair = TuplesKt.to(Integer.valueOf(R.dimen.coui_seekbar_intent_thumb_radius_small), Integer.valueOf(R.dimen.coui_seekbar_intent_thumb_max_radius_small));
        } else {
            if (i3 != 3) {
                throw new NoWhenBranchMatchedException();
            }
            pair = TuplesKt.to(Integer.valueOf(R.dimen.coui_seekbar_intent_thumb_radius_large), Integer.valueOf(R.dimen.coui_seekbar_intent_thumb_max_radius_large));
        }
        int iIntValue = ((Number) pair.component1()).intValue();
        int iIntValue2 = ((Number) pair.component2()).intValue();
        getState().setThumbRadius(getResources().getDimension(iIntValue));
        getState().setThumbMaxRadius(getResources().getDimension(iIntValue2));
        getState().setThumbOutRadius(getState().getThumbRadius());
        getState().setCurThumbRadius(getState().getThumbRadius());
    }

    private final void applyVisualState() {
        VisualState visualStateResolveVisualState = resolveVisualState();
        VisualState visualState = this.mCurrentVisualState;
        if (visualState != visualStateResolveVisualState) {
            this.mCurrentVisualState = visualStateResolveVisualState;
            onVisualStateChanged(visualState, visualStateResolveVisualState);
        }
    }

    private final void clearProgrammaticConicSeekVisualIfNeeded() {
        if (this.mProgrammaticConicSeekActive) {
            this.mProgrammaticConicSeekActive = false;
            applyVisualState();
        }
    }

    private final void executeThumbReleaseAfterDrag() {
        if (!getState().getShowThumb()) {
            super.releaseThumbScaleAnim();
        } else if (this.mPlaybackState == PlaybackState.PAUSED) {
            super.releaseThumbScaleAnim();
        } else {
            getMThumbStateHandler().startDisappear(getState().getCurThumbRadius());
        }
    }

    public static /* synthetic */ void g(COUIIntentSeekBar cOUIIntentSeekBar, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        initSecondaryProgressAnim$lambda$2$lambda$1(cOUIIntentSeekBar, cOUIDynamicAnimation, f3, f4);
    }

    private final void getFixedColors(int[] out) {
        out[0] = COUIContextUtil.getColor(getContext(), R.color.coui_seekbar_intent_background_color_fixed);
        out[1] = COUIContextUtil.getColor(getContext(), R.color.coui_seekbar_intent_progress_color_fixed);
        out[2] = COUIContextUtil.getColor(getContext(), R.color.coui_seekbar_intent_secondary_progress_color_fixed);
    }

    private final ThumbStateAnimationHandler getMThumbStateHandler() {
        return (ThumbStateAnimationHandler) this.mThumbStateHandler.getValue();
    }

    private final void getThemeFallbackColors(int[] out) {
        out[0] = COUIContextUtil.getColor(getContext(), R.color.coui_seekbar_background_color_normal);
        out[1] = COUIContextUtil.getColor(getContext(), R.color.coui_seekbar_progress_color_normal);
        out[2] = COUIContextUtil.getColor(getContext(), R.color.coui_seekbar_secondary_progress_color);
    }

    /* JADX WARN: Code duplicated, block: B:20:0x0036  */
    private final void handleThumbStateTransition(PlaybackState oldState, PlaybackState newState) {
        float curThumbRadius;
        if (getState().getShowThumb()) {
            PlaybackState playbackState = PlaybackState.PAUSED;
            boolean z2 = oldState == playbackState;
            boolean z3 = newState == playbackState;
            if (!z2 && z3) {
                if (!getMStartDragging() && !getState().getIsDragging()) {
                    curThumbRadius = getEnlargeFraction() > 0.0f ? getState().getCurThumbRadius() : 0.0f;
                }
                getMThumbStateHandler().startAppear(curThumbRadius, getState().getThumbRadius());
                return;
            }
            if (!z2 || z3 || getMStartDragging() || getState().getIsDragging()) {
                return;
            }
            getMThumbStateHandler().setPendingShrink(false);
            getMThumbStateHandler().startDisappear(getState().getCurThumbRadius());
        }
    }

    private final boolean hasCustomUserColors() {
        return (this.mUserBackgroundColor == null && this.mUserProgressColor == null && this.mUserSecondaryProgressColor == null) ? false : true;
    }

    private final void initIntentAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        ColorStateList colorStateList;
        ColorStateList colorStateList2;
        ColorStateList colorStateList3;
        int[] iArr = R.styleable.COUIIntentSeekBar;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attrs, iArr, defStyleAttr, defStyleRes);
        Intrinsics.checkNotNullExpressionValue(typedArrayObtainStyledAttributes, "obtainStyledAttributes(...)");
        int[] iArr2 = R.styleable.COUISeekBar;
        TypedArray typedArrayObtainStyledAttributes2 = context.obtainStyledAttributes(attrs, iArr2, defStyleAttr, defStyleRes);
        Intrinsics.checkNotNullExpressionValue(typedArrayObtainStyledAttributes2, "obtainStyledAttributes(...)");
        try {
            this.mSize = IntentSeekBarSize.INSTANCE.fromInt(typedArrayObtainStyledAttributes.getInt(R.styleable.COUIIntentSeekBar_couiIntentSeekBarSize, 0));
            applySizeHeight();
            this.mFollowSystemTheme = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIIntentSeekBar_couiIntentSeekBarFollowSystemTheme, true);
            TypedArray typedArrayObtainStyledAttributes3 = context.obtainStyledAttributes(attrs, iArr, 0, 0);
            Intrinsics.checkNotNullExpressionValue(typedArrayObtainStyledAttributes3, "obtainStyledAttributes(...)");
            TypedArray typedArrayObtainStyledAttributes4 = context.obtainStyledAttributes(attrs, iArr2, 0, 0);
            Intrinsics.checkNotNullExpressionValue(typedArrayObtainStyledAttributes4, "obtainStyledAttributes(...)");
            int i3 = R.styleable.COUISeekBar_couiSeekBarBackgroundColor;
            boolean zHasValue = typedArrayObtainStyledAttributes4.hasValue(i3);
            int i4 = R.styleable.COUISeekBar_couiSeekBarProgressColor;
            boolean zHasValue2 = typedArrayObtainStyledAttributes4.hasValue(i4);
            int i5 = R.styleable.COUIIntentSeekBar_couiSeekBarSecondaryProgressColor;
            boolean zHasValue3 = typedArrayObtainStyledAttributes3.hasValue(i5);
            typedArrayObtainStyledAttributes3.recycle();
            typedArrayObtainStyledAttributes4.recycle();
            if (zHasValue && (colorStateList3 = typedArrayObtainStyledAttributes2.getColorStateList(i3)) != null) {
                this.mUserBackgroundColor = Integer.valueOf(getColor(this, colorStateList3, COUIContextUtil.getColor(context, R.color.coui_seekbar_background_selector)));
                this.mFollowSystemTheme = false;
            }
            if (zHasValue2 && (colorStateList2 = typedArrayObtainStyledAttributes2.getColorStateList(i4)) != null) {
                this.mUserProgressColor = Integer.valueOf(getColor(this, colorStateList2, COUIContextUtil.getColor(context, R.color.coui_seekbar_progress_selector)));
                this.mFollowSystemTheme = false;
            }
            if (zHasValue3 && (colorStateList = typedArrayObtainStyledAttributes.getColorStateList(i5)) != null) {
                this.mUserSecondaryProgressColor = Integer.valueOf(getColor(this, colorStateList, COUIContextUtil.getColor(context, R.color.coui_seekbar_progress_color_normal)));
                this.mFollowSystemTheme = false;
            }
            getState().setShowThumb(typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIIntentSeekBar_couiIntentSeekBarShowThumb, false));
            getState().setShowSecondaryProgress(typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIIntentSeekBar_couiIntentSeekBarShowSecondaryProgress, true));
            this.mConicLightEnabled = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIIntentSeekBar_couiIntentSeekBarConicLight, false);
            this.mConicLightColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIIntentSeekBar_couiIntentSeekBarConicLightColor, DEFAULT_CONIC_LIGHT_COLOR);
            this.mUseMaterialEffect = sanitizeUseMaterialEffect(typedArrayObtainStyledAttributes.getInt(R.styleable.COUIIntentSeekBar_couiIntentSeekBarUseMaterialEffect, -1));
            int i6 = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIIntentSeekBar_couiIntentSeekBarPlaybackState, 0);
            PlaybackState[] playbackStateArrValues = PlaybackState.values();
            PlaybackState playbackState = (PlaybackState) ArraysKt.getOrNull(playbackStateArrValues, RangesKt.coerceIn(i6, 0, playbackStateArrValues.length - 1));
            if (playbackState == null) {
                playbackState = PlaybackState.IDLE;
            }
            this.mPlaybackState = playbackState;
            this.mCurrentVisualState = resolveVisualState();
            boolean z2 = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIIntentSeekBar_couiSeekBarIsFollowThumb, false);
            this.mThumbOutShadeRadius = getResources().getDimensionPixelSize(R.dimen.coui_seekbar_intent_thumb_out_shade_radius);
            getState().setMoveType(z2 ? 0 : 2);
            typedArrayObtainStyledAttributes.recycle();
            applyIntentColors();
            this.mColorsDirty = false;
            try {
                getState().setProgressEnlargeScale(typedArrayObtainStyledAttributes2.getFloat(R.styleable.COUISeekBar_couiSeekBarProgressEnlargeScale, 4.0f));
                getState().setPaddingHorizontal(typedArrayObtainStyledAttributes2.getDimension(R.styleable.COUISeekBar_couiSeekBarProgressPaddingHorizontal, getResources().getDimension(R.dimen.coui_seekbar_progress_padding_horizontal)));
                typedArrayObtainStyledAttributes2.recycle();
                initEnlargeAnim();
                getState().setExtendProgressRectForRoundCorner(false);
                getState().setSupportDeformation(false);
                updateBlurBlendRendererState();
            } catch (Throwable th) {
                typedArrayObtainStyledAttributes2.recycle();
                throw th;
            }
        } catch (Throwable th2) {
            typedArrayObtainStyledAttributes.recycle();
            throw th2;
        }
    }

    private final void initSecondaryProgressAnim() {
        if (this.mSecondaryProgressAnim != null) {
            return;
        }
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(0.0f);
        cOUISpringForce.setResponse(0.5f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        spring.addUpdateListener(new a(this));
        this.mSecondaryProgressAnim = spring;
    }

    private static final void initSecondaryProgressAnim$lambda$2$lambda$1(COUIIntentSeekBar this$0, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.applySecondaryProgress(f3);
    }

    private final boolean isConicLightCapabilitySupported() {
        COUIMaterialStateManager cOUIMaterialStateManager = COUIMaterialStateManager.INSTANCE;
        return !cOUIMaterialStateManager.isLowAnimationLevel() && cOUIMaterialStateManager.materialVersionCheck() && cOUIMaterialStateManager.isFeatureEnabled() && !cOUIMaterialStateManager.isLowPowerMode();
    }

    private final void onPlaybackStateChanged(PlaybackState oldState, PlaybackState newState) {
        handleThumbStateTransition(oldState, newState);
    }

    private final void onVisualStateChanged(VisualState oldState, VisualState newState) {
        applyConicLightModeForVisualState(newState);
        int[] iArr = WhenMappings.$EnumSwitchMapping$1;
        int i3 = iArr[oldState.ordinal()];
        LoadingEffectRenderer loadingEffectRenderer = null;
        if (i3 == 3) {
            LoadingEffectRenderer loadingEffectRenderer2 = this.mLoadingRenderer;
            if (loadingEffectRenderer2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mLoadingRenderer");
                loadingEffectRenderer2 = null;
            }
            loadingEffectRenderer2.stopInitialLoading(newState == VisualState.DRAGGING_CONE_LIGHT);
        } else if (i3 == 4) {
            LoadingEffectRenderer loadingEffectRenderer3 = this.mLoadingRenderer;
            if (loadingEffectRenderer3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mLoadingRenderer");
                loadingEffectRenderer3 = null;
            }
            loadingEffectRenderer3.stopBuffering();
        }
        int i4 = iArr[newState.ordinal()];
        if (i4 == 3) {
            LoadingEffectRenderer loadingEffectRenderer4 = this.mLoadingRenderer;
            if (loadingEffectRenderer4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mLoadingRenderer");
            } else {
                loadingEffectRenderer = loadingEffectRenderer4;
            }
            loadingEffectRenderer.startInitialLoading();
            return;
        }
        if (i4 != 4) {
            return;
        }
        LoadingEffectRenderer loadingEffectRenderer5 = this.mLoadingRenderer;
        if (loadingEffectRenderer5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mLoadingRenderer");
        } else {
            loadingEffectRenderer = loadingEffectRenderer5;
        }
        loadingEffectRenderer.startBuffering();
    }

    private final void refreshConicLightActivation() {
        ConicLightEffectRenderer conicLightEffectRenderer = null;
        if (!shouldEquipConicLightDrawer()) {
            ConicLightEffectRenderer conicLightEffectRenderer2 = this.mConicLightRenderer;
            if (conicLightEffectRenderer2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mConicLightRenderer");
            } else {
                conicLightEffectRenderer = conicLightEffectRenderer2;
            }
            conicLightEffectRenderer.resetToOff();
            return;
        }
        if (this.mConicLightDrawer == null) {
            this.mConicLightDrawer = new ConicLightPaintHostDrawer();
            ConicLightEffectRenderer conicLightEffectRenderer3 = this.mConicLightRenderer;
            if (conicLightEffectRenderer3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mConicLightRenderer");
            } else {
                conicLightEffectRenderer = conicLightEffectRenderer3;
            }
            conicLightEffectRenderer.setConicLightDrawer(this.mConicLightDrawer);
        }
    }

    private final VisualState resolveVisualState() {
        PlaybackState playbackState;
        if ((getState().getIsDragging() || getMStartDragging()) && ((playbackState = this.mPlaybackState) == PlaybackState.PLAYING || playbackState == PlaybackState.LOADING_BUFFERING)) {
            return VisualState.DRAGGING_CONE_LIGHT;
        }
        if (this.mLoadingDragReturnHandler.getIsReturning() || this.mLoadingDragReturnHandler.getIsDraggingDuringLoading()) {
            return VisualState.STATIC;
        }
        if (this.mProgrammaticConicSeekActive && shouldProgrammaticConicSeekUseDraggingVisual(false)) {
            return VisualState.DRAGGING_CONE_LIGHT;
        }
        if (this.mHoldToSpeedUpConeLightActive && shouldPlayingAuxiliaryEffectsUseDraggingConeLightVisual()) {
            return VisualState.DRAGGING_CONE_LIGHT;
        }
        int i3 = WhenMappings.$EnumSwitchMapping$2[this.mPlaybackState.ordinal()];
        if (i3 == 1) {
            return VisualState.STATIC;
        }
        if (i3 == 2) {
            return VisualState.LOADING_FULL;
        }
        if (i3 == 3) {
            return VisualState.PLAYING_CONE_LIGHT;
        }
        if (i3 == 4) {
            return VisualState.STATIC;
        }
        if (i3 == 5) {
            return VisualState.LOADING_PARTIAL;
        }
        throw new NoWhenBranchMatchedException();
    }

    private final int sanitizeUseMaterialEffect(int value) {
        if (value == -1 || value == 0 || value == 1) {
            return value;
        }
        return -1;
    }

    private final boolean shouldDrawThumbNow() {
        return this.mPlaybackState == PlaybackState.PAUSED || getMStartDragging() || getState().getIsDragging() || getEnlargeFraction() > 0.0f || getMThumbStateHandler().isRunning();
    }

    private final boolean shouldEquipConicLightDrawer() {
        int i3 = this.mUseMaterialEffect;
        if (i3 == 0) {
            return false;
        }
        if (i3 != 1) {
            return this.mConicLightEnabled && isConicLightCapabilitySupported();
        }
        return true;
    }

    private final boolean shouldPlayingAuxiliaryEffectsUseDraggingConeLightVisual() {
        return this.mPlaybackState == PlaybackState.PLAYING && isConicLightProgressStyle();
    }

    private final boolean shouldProgrammaticConicSeekUseDraggingVisual(boolean fromUser) {
        if (fromUser) {
            return false;
        }
        return shouldPlayingAuxiliaryEffectsUseDraggingConeLightVisual();
    }

    private final void stopSecondaryProgressAnim() {
        COUISpringAnimation cOUISpringAnimation = this.mSecondaryProgressAnim;
        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) {
            return;
        }
        cOUISpringAnimation.cancel();
    }

    private final void syncConicLightToVisualState() {
        ConicLightEffectRenderer.ConicLightMode conicLightMode;
        if (isConicLightProgressStyle()) {
            int i3 = WhenMappings.$EnumSwitchMapping$1[this.mCurrentVisualState.ordinal()];
            if (i3 != 1) {
                conicLightMode = i3 != 2 ? ConicLightEffectRenderer.ConicLightMode.OFF : ConicLightEffectRenderer.ConicLightMode.DRAGGING;
            } else {
                conicLightMode = ConicLightEffectRenderer.ConicLightMode.PLAYING;
            }
            ConicLightEffectRenderer conicLightEffectRenderer = this.mConicLightRenderer;
            if (conicLightEffectRenderer == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mConicLightRenderer");
                conicLightEffectRenderer = null;
            }
            conicLightEffectRenderer.transitionTo(conicLightMode);
        }
    }

    private final void syncSecondaryProgressScale() {
        SeekBarState state = getState();
        int max = state.getMax() - state.getMin();
        state.setSecondaryProgressScale(max > 0 ? (state.getSecondaryProgress() - state.getMin()) / max : 0.0f);
        state.setSecondaryProgressScale(RangesKt.coerceIn(state.getSecondaryProgressScale(), 0.0f, 1.0f));
    }

    private final boolean trySnapSecondaryProgress(float start, float end, int target) {
        if (Math.abs(end - start) >= 0.5f) {
            return false;
        }
        stopSecondaryProgressAnim();
        getState().setSecondaryProgress(target);
        syncSecondaryProgressScale();
        invalidate();
        return true;
    }

    private final void updateBlurBlendRendererState() {
        BlurBlendEffectRenderer blurBlendEffectRenderer = this.mBlurBlendRenderer;
        if (blurBlendEffectRenderer == null) {
            return;
        }
        if (blurBlendEffectRenderer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mBlurBlendRenderer");
            blurBlendEffectRenderer = null;
        }
        blurBlendEffectRenderer.updateState(this.mFollowSystemTheme, hasCustomUserColors());
    }

    public final void collapseConeLightForSeek() {
        if (this.mProgrammaticConicSeekActive) {
            return;
        }
        this.mProgrammaticConicSeekActive = true;
        applyVisualState();
        invalidate();
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public IProgressRenderer createProgressRenderer() {
        return new IntentConicProgressRenderer(this);
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public IThumbRenderer createThumbRenderer() {
        return new IntentThumbRenderer();
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void drawActiveTrack(Canvas canvas) {
        IThumbRenderer mThumbRenderer;
        IOrientationHandler mOrientationHandler;
        IProgressRenderer mProgressRenderer;
        IOrientationHandler mOrientationHandler2;
        IProgressRenderer mProgressRenderer2;
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        SeekBarState state = getState();
        LoadingEffectRenderer loadingEffectRenderer = null;
        if (this.mCurrentVisualState == VisualState.LOADING_FULL) {
            drawBackground(canvas);
            LoadingEffectRenderer loadingEffectRenderer2 = this.mLoadingRenderer;
            if (loadingEffectRenderer2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mLoadingRenderer");
            } else {
                loadingEffectRenderer = loadingEffectRenderer2;
            }
            loadingEffectRenderer.drawInitialLoading(canvas, state);
            return;
        }
        float backgroundRadiusForDraw = getBackgroundRadiusForDraw();
        this.mProgressClipPath.reset();
        this.mProgressClipPath.addRoundRect(getMBackgroundRect(), backgroundRadiusForDraw, backgroundRadiusForDraw, Path.Direction.CW);
        if (this.mCurrentVisualState == VisualState.LOADING_PARTIAL) {
            canvas.save();
            canvas.clipPath(this.mProgressClipPath);
            LoadingEffectRenderer loadingEffectRenderer3 = this.mLoadingRenderer;
            if (loadingEffectRenderer3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mLoadingRenderer");
                loadingEffectRenderer3 = null;
            }
            loadingEffectRenderer3.drawBufferingLoading(canvas, state);
            if (state.getShowProgress() && getMProgressRenderer() != null && (mProgressRenderer2 = getMProgressRenderer()) != null) {
                mProgressRenderer2.drawProgress(canvas, state);
            }
            canvas.restore();
            LoadingEffectRenderer loadingEffectRenderer4 = this.mLoadingRenderer;
            if (loadingEffectRenderer4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mLoadingRenderer");
                loadingEffectRenderer4 = null;
            }
            if (loadingEffectRenderer4.getMInitialTailActive()) {
                LoadingEffectRenderer loadingEffectRenderer5 = this.mLoadingRenderer;
                if (loadingEffectRenderer5 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("mLoadingRenderer");
                } else {
                    loadingEffectRenderer = loadingEffectRenderer5;
                }
                loadingEffectRenderer.drawInitialLoading(canvas, state);
            }
            if (!state.getShowThumb() || !shouldDrawThumbNow() || getMOrientationHandler() == null || getMThumbRenderer() == null) {
                return;
            }
            if ((!state.getShowProgress() || state.getProgress() <= state.getMin()) && (mOrientationHandler2 = getMOrientationHandler()) != null) {
                mOrientationHandler2.calculateProgressRect(getMTempRect(), state);
            }
            IThumbRenderer mThumbRenderer2 = getMThumbRenderer();
            if (mThumbRenderer2 != null) {
                mThumbRenderer2.drawThumb(canvas, state);
                return;
            }
            return;
        }
        canvas.save();
        canvas.clipPath(this.mProgressClipPath);
        if (state.getShowProgress()) {
            SecondaryProgressRenderer secondaryProgressRenderer = this.mSecondaryProgressRenderer;
            if (secondaryProgressRenderer == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mSecondaryProgressRenderer");
                secondaryProgressRenderer = null;
            }
            secondaryProgressRenderer.drawProgress(canvas, state);
        }
        if (state.getShowProgress() && getMProgressRenderer() != null && (mProgressRenderer = getMProgressRenderer()) != null) {
            mProgressRenderer.drawProgress(canvas, state);
        }
        canvas.restore();
        LoadingEffectRenderer loadingEffectRenderer6 = this.mLoadingRenderer;
        if (loadingEffectRenderer6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mLoadingRenderer");
            loadingEffectRenderer6 = null;
        }
        if (loadingEffectRenderer6.getMInitialTailActive()) {
            LoadingEffectRenderer loadingEffectRenderer7 = this.mLoadingRenderer;
            if (loadingEffectRenderer7 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mLoadingRenderer");
            } else {
                loadingEffectRenderer = loadingEffectRenderer7;
            }
            loadingEffectRenderer.drawInitialLoading(canvas, state);
        }
        if (state.getShowThumb() && getMOrientationHandler() != null && ((!state.getShowProgress() || state.getProgress() <= state.getMin()) && (mOrientationHandler = getMOrientationHandler()) != null)) {
            mOrientationHandler.calculateProgressRect(getMTempRect(), state);
        }
        boolean showThumb = state.getShowThumb();
        if (state.getShowThumb()) {
            state.setShowThumb(shouldDrawThumbNow());
        }
        if (state.getShowThumb() && getMThumbRenderer() != null && (mThumbRenderer = getMThumbRenderer()) != null) {
            mThumbRenderer.drawThumb(canvas, state);
        }
        state.setShowThumb(showThumb);
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void drawBackground(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        if (getMOrientationHandler() == null) {
            return;
        }
        IOrientationHandler mOrientationHandler = getMOrientationHandler();
        if (mOrientationHandler != null) {
            mOrientationHandler.calculateBackgroundRect(getMBackgroundRect(), this, getMState());
        }
        BlurBlendEffectRenderer blurBlendEffectRenderer = this.mBlurBlendRenderer;
        BlurBlendEffectRenderer blurBlendEffectRenderer2 = null;
        if (blurBlendEffectRenderer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mBlurBlendRenderer");
            blurBlendEffectRenderer = null;
        }
        if (!blurBlendEffectRenderer.getMEnabled()) {
            drawInactiveTrack(canvas);
            return;
        }
        BlurBlendEffectRenderer blurBlendEffectRenderer3 = this.mBlurBlendRenderer;
        if (blurBlendEffectRenderer3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mBlurBlendRenderer");
        } else {
            blurBlendEffectRenderer2 = blurBlendEffectRenderer3;
        }
        blurBlendEffectRenderer2.updateBaseParams(getMBackgroundRect(), getBackgroundRadiusForDraw());
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public float getBackgroundHorizontalInset(SeekBarState state) {
        Intrinsics.checkNotNullParameter(state, "state");
        return 0.0f;
    }

    /* JADX INFO: renamed from: getConicLightColor, reason: from getter */
    public final int getMConicLightColor() {
        return this.mConicLightColor;
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public float getDefaultBackgroundEnlargeScale() {
        return 6.0f;
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public float getMinEnlargeHeight() {
        return getContext().getResources().getDisplayMetrics().density * 2.0f;
    }

    /* JADX INFO: renamed from: getPlaybackState, reason: from getter */
    public final PlaybackState getMPlaybackState() {
        return this.mPlaybackState;
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public float getProgressRectHeight(SeekBarState state) {
        Intrinsics.checkNotNullParameter(state, "state");
        return state.getCurBackgroundHeight();
    }

    @Override // android.widget.ProgressBar
    public int getSecondaryProgress() {
        return getState().getSecondaryProgress();
    }

    /* JADX INFO: renamed from: getSize, reason: from getter */
    public final IntentSeekBarSize getMSize() {
        return this.mSize;
    }

    /* JADX INFO: renamed from: getUseMaterialEffect, reason: from getter */
    public final int getMUseMaterialEffect() {
        return this.mUseMaterialEffect;
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarAdvanced
    public void initAdvancedFeatures(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        Intrinsics.checkNotNullParameter(context, "context");
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.COUISeekBar, defStyleAttr, defStyleRes);
        Intrinsics.checkNotNullExpressionValue(typedArrayObtainStyledAttributes, "obtainStyledAttributes(...)");
        try {
            getState().setEnableVibrator(typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUISeekBar_couiSeekBarEnableVibrator, true));
            getState().setEnableAdaptiveVibrator(typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUISeekBar_couiSeekBarAdaptiveVibrator, false));
            getState().setPhysicalEnabled(typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUISeekBar_couiSeekBarPhysicsEnable, true));
            getState().setStartFromMiddle(typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUISeekBar_couiSeekBarStartMiddle, false));
            typedArrayObtainStyledAttributes.recycle();
            initTouchMotionEffect();
            initPhysicsAnimation(context);
            initFlexibleFollowHand();
            initClickAnimation();
            initVibrationFeedback(context);
        } catch (Throwable th) {
            typedArrayObtainStyledAttributes.recycle();
            throw th;
        }
    }

    /* JADX INFO: renamed from: isConicLightEnabled, reason: from getter */
    public final boolean getMConicLightEnabled() {
        return this.mConicLightEnabled;
    }

    public final boolean isConicLightProgressStyle() {
        int i3 = this.mUseMaterialEffect;
        if (i3 == 0) {
            return false;
        }
        if (i3 != 1) {
            return this.mConicLightEnabled && !this.mFollowSystemTheme && isConicLightCapabilitySupported();
        }
        return true;
    }

    public final boolean isDraggingState() {
        return getState().getIsDragging() || getMStartDragging();
    }

    /* JADX INFO: renamed from: isFollowSystemTheme, reason: from getter */
    public final boolean getMFollowSystemTheme() {
        return this.mFollowSystemTheme;
    }

    public final boolean isFollowThumb() {
        return isMoveFollowHand();
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public boolean isInvalidateThrottleEnabled() {
        return true;
    }

    public final boolean isLargeSize() {
        return this.mSize == IntentSeekBarSize.LARGE;
    }

    public final boolean isLoadingDragReturnEnabled() {
        return this.mLoadingDragReturnHandler.getIsEnabled();
    }

    public final boolean isLoadingDragReturnInProgress() {
        return this.mLoadingDragReturnHandler.isInProgress();
    }

    public final boolean isShowSecondaryProgress() {
        return getState().getShowSecondaryProgress();
    }

    public final boolean isShowThumb() {
        return getState().getShowThumb();
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarAdvanced, com.coui.appcompat.seekbar.core.COUISeekBarCore, android.widget.ProgressBar, android.view.View
    public void onDetachedFromWindow() {
        this.mProgrammaticConicSeekActive = false;
        this.mHoldToSpeedUpConeLightActive = false;
        ConicLightEffectRenderer conicLightEffectRenderer = this.mConicLightRenderer;
        if (conicLightEffectRenderer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mConicLightRenderer");
            conicLightEffectRenderer = null;
        }
        conicLightEffectRenderer.release();
        LoadingEffectRenderer loadingEffectRenderer = this.mLoadingRenderer;
        if (loadingEffectRenderer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mLoadingRenderer");
            loadingEffectRenderer = null;
        }
        loadingEffectRenderer.release();
        this.mLoadingDragReturnHandler.cancel();
        getMThumbStateHandler().cancel();
        BlurBlendEffectRenderer blurBlendEffectRenderer = this.mBlurBlendRenderer;
        if (blurBlendEffectRenderer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mBlurBlendRenderer");
            blurBlendEffectRenderer = null;
        }
        blurBlendEffectRenderer.clearOnDetach();
        IProgressRenderer mProgressRenderer = getMProgressRenderer();
        IntentConicProgressRenderer intentConicProgressRenderer = mProgressRenderer instanceof IntentConicProgressRenderer ? (IntentConicProgressRenderer) mProgressRenderer : null;
        if (intentConicProgressRenderer != null) {
            intentConicProgressRenderer.release();
        }
        stopSecondaryProgressAnim();
        super.onDetachedFromWindow();
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void onHeightSpringUpdate(float fraction) {
        super.onHeightSpringUpdate(fraction);
        float progressEnlargeScale = getState().getProgressEnlargeScale();
        if (progressEnlargeScale <= 0.0f) {
            getState().setProgressEnlargeScale(4.0f);
            progressEnlargeScale = 4.0f;
        }
        float progressHeight = getState().getProgressHeight();
        if (progressHeight <= 0.0f) {
            progressHeight = getContext().getResources().getDimension(R.dimen.coui_seekbar_progress_height);
            getState().setProgressHeight(progressHeight);
        }
        getState().setCurProgressHeight(progressHeight + (((progressHeight * progressEnlargeScale) - progressHeight) * fraction));
        float progressRadius = getState().getProgressRadius();
        getState().setCurProgressRadius(progressRadius + (((progressEnlargeScale * progressRadius) - progressRadius) * fraction));
        getMThumbStateHandler().checkPendingShrink(fraction);
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore, android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float mMaxBackgroundHeight;
        int mode = View.MeasureSpec.getMode(heightMeasureSpec);
        int size = View.MeasureSpec.getSize(heightMeasureSpec);
        int size2 = View.MeasureSpec.getSize(widthMeasureSpec);
        if (getState().getShowThumb()) {
            mMaxBackgroundHeight = Math.max(getMMaxBackgroundHeight(), getState().getThumbMaxRadius() * 2.0f);
        } else {
            mMaxBackgroundHeight = getMMaxBackgroundHeight();
        }
        int iMax = (int) (Math.max(mMaxBackgroundHeight, getContext().getResources().getDisplayMetrics().density * 24.0f) + getPaddingTop() + getPaddingBottom());
        if (1073741824 != mode || size < iMax) {
            size = iMax;
        }
        setMeasuredDimension(size2, size);
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarAdvanced
    public void onPhysicsFlingEnd() {
        if (this.mLoadingDragReturnHandler.getIsReturning()) {
            return;
        }
        super.onPhysicsFlingEnd();
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void onProgressAnimationEnd(int targetProgress, boolean fromUser) {
        super.onProgressAnimationEnd(targetProgress, fromUser);
        if (this.mProgrammaticConicSeekActive) {
            this.mProgrammaticConicSeekActive = false;
            applyVisualState();
        }
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarAdvanced, com.coui.appcompat.seekbar.core.COUISeekBarCore, android.widget.AbsSeekBar, android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        boolean z2;
        Intrinsics.checkNotNullParameter(event, "event");
        LoadingDragReturnHandler loadingDragReturnHandler = this.mLoadingDragReturnHandler;
        if (event.getAction() == 0) {
            loadingDragReturnHandler.cancel();
            clearProgrammaticConicSeekVisualIfNeeded();
            if (loadingDragReturnHandler.getIsEnabled() && this.mPlaybackState == PlaybackState.LOADING_INITIAL) {
                loadingDragReturnHandler.setDraggingDuringLoading(true);
                applyVisualState();
            }
        }
        boolean z3 = getState().getIsDragging() || getMStartDragging();
        boolean zOnTouchEvent = super.onTouchEvent(event);
        boolean z4 = getState().getIsDragging() || getMStartDragging();
        if (loadingDragReturnHandler.getIsEnabled() && loadingDragReturnHandler.getIsDraggingDuringLoading() && z3 && !z4) {
            loadingDragReturnHandler.setDraggingDuringLoading(false);
            loadingDragReturnHandler.setReturning(true);
            z2 = true;
        } else {
            z2 = false;
        }
        if (loadingDragReturnHandler.getIsDraggingDuringLoading() && !z3 && (event.getAction() == 1 || event.getAction() == 3)) {
            loadingDragReturnHandler.setDraggingDuringLoading(false);
            applyVisualState();
        }
        if (z3 != z4) {
            this.mWasDragging = z4;
            applyVisualState();
        }
        if (z2) {
            loadingDragReturnHandler.startReturnAnimation();
        }
        return zOnTouchEvent;
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarAdvanced
    public void releaseThumbScaleAnim() {
        COUISpringAnimation mHeightSpringAnim = getMHeightSpringAnim();
        if (mHeightSpringAnim != null && mHeightSpringAnim.isRunning() && getEnlargeFraction() < 1.0f) {
            getMThumbStateHandler().setPendingShrink(true);
        } else {
            getMThumbStateHandler().setPendingShrink(false);
            executeThumbReleaseAfterDrag();
        }
    }

    public final void setConicLightColor(int color) {
        if (this.mConicLightColor == color) {
            return;
        }
        this.mConicLightColor = color;
        ConicLightEffectRenderer conicLightEffectRenderer = this.mConicLightRenderer;
        if (conicLightEffectRenderer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mConicLightRenderer");
            conicLightEffectRenderer = null;
        }
        conicLightEffectRenderer.setConicLightColor(color);
        IProgressRenderer mProgressRenderer = getMProgressRenderer();
        IntentConicProgressRenderer intentConicProgressRenderer = mProgressRenderer instanceof IntentConicProgressRenderer ? (IntentConicProgressRenderer) mProgressRenderer : null;
        if (intentConicProgressRenderer != null) {
            intentConicProgressRenderer.setProgressColorA(color);
        }
        invalidate();
    }

    public final void setConicLightEnabled(boolean enabled) {
        if (this.mConicLightEnabled == enabled) {
            return;
        }
        this.mConicLightEnabled = enabled;
        refreshConicLightActivation();
        applyConicLightModeForVisualState(resolveVisualState());
        invalidate();
    }

    public final void setFollowSystemTheme(boolean follow) {
        if (this.mFollowSystemTheme == follow) {
            return;
        }
        this.mFollowSystemTheme = follow;
        applyIntentColors();
        this.mColorsDirty = false;
        updateBlurBlendRendererState();
        if (follow && this.mUseMaterialEffect != 1) {
            ConicLightEffectRenderer conicLightEffectRenderer = this.mConicLightRenderer;
            if (conicLightEffectRenderer == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mConicLightRenderer");
                conicLightEffectRenderer = null;
            }
            conicLightEffectRenderer.resetToOff();
        }
        applyConicLightModeForVisualState(resolveVisualState());
        invalidate();
    }

    public final void setFollowThumb(boolean followThumb) {
        int i3 = followThumb ? 0 : 2;
        if (getState().getMoveType() != i3) {
            setMoveType(i3);
            invalidate();
        }
    }

    public final void setHoldToSpeedUpConeLightActive(boolean active) {
        if (this.mHoldToSpeedUpConeLightActive == active) {
            return;
        }
        this.mHoldToSpeedUpConeLightActive = active;
        applyVisualState();
        invalidate();
    }

    public final void setLoadingDragReturnEnabled(boolean enabled) {
        this.mLoadingDragReturnHandler.setEnabled(enabled);
    }

    public final void setOnDragHorizontalOffsetChangeListener(OnDragHorizontalOffsetChangeListener listener) {
        this.mOnDragHorizontalOffsetChangeListener = listener;
    }

    public final void setOnLoadingDragReturnCompleteListener(OnLoadingDragReturnCompleteListener listener) {
        this.mOnLoadingDragReturnCompleteListener = listener;
    }

    public final void setPlaybackState(PlaybackState state) {
        Intrinsics.checkNotNullParameter(state, "state");
        PlaybackState playbackState = this.mPlaybackState;
        if (playbackState != state) {
            this.mPlaybackState = state;
            if (state != PlaybackState.PLAYING) {
                if (this.mProgrammaticConicSeekActive) {
                    this.mProgrammaticConicSeekActive = false;
                }
                if (this.mHoldToSpeedUpConeLightActive) {
                    this.mHoldToSpeedUpConeLightActive = false;
                }
            }
            onPlaybackStateChanged(playbackState, state);
            applyVisualState();
            invalidate();
        }
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void setProgressColor(ColorStateList progressColor) {
    }

    @Override // android.widget.ProgressBar
    public void setSecondaryProgress(int secondaryProgress) {
        if (secondaryProgress < 0) {
            return;
        }
        int iCoerceIn = RangesKt.coerceIn(secondaryProgress, getMin(), getMax());
        SeekBarState state = getState();
        if (state.getSecondaryProgress() == iCoerceIn) {
            return;
        }
        state.setSecondaryProgress(iCoerceIn);
        syncSecondaryProgressScale();
        if (shouldThrottleInvalidate(iCoerceIn, false, this.mLastDrawnSecondaryProgressForSnap)) {
            return;
        }
        this.mLastDrawnSecondaryProgressForSnap = iCoerceIn;
        invalidate();
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void setSeekBarBackgroundColor(ColorStateList backgroundColor) {
    }

    public final void setSeekBarColors(ColorStateList backgroundColor, ColorStateList progressColor, ColorStateList secondaryProgressColor) {
        boolean z2;
        boolean z3 = true;
        if (backgroundColor != null) {
            this.mUserBackgroundColor = Integer.valueOf(getColor(this, backgroundColor, COUIContextUtil.getColor(getContext(), R.color.coui_seekbar_background_selector)));
            z2 = true;
        } else {
            z2 = false;
        }
        if (progressColor != null) {
            this.mUserProgressColor = Integer.valueOf(getColor(this, progressColor, COUIContextUtil.getColor(getContext(), R.color.coui_seekbar_progress_selector)));
            z2 = true;
        }
        if (secondaryProgressColor != null) {
            this.mUserSecondaryProgressColor = Integer.valueOf(getColor(this, secondaryProgressColor, ContextCompat.getColor(getContext(), R.color.coui_seekbar_secondary_progress_color)));
        } else {
            z3 = z2;
        }
        if (z3) {
            this.mFollowSystemTheme = false;
            applyIntentColors();
            this.mColorsDirty = false;
            updateBlurBlendRendererState();
            invalidate();
        }
    }

    public final void setShowSecondaryProgress(boolean show) {
        if (getState().getShowSecondaryProgress() == show) {
            return;
        }
        getState().setShowSecondaryProgress(show);
        invalidate();
    }

    public final void setShowThumb(boolean show) {
        if (getState().getShowThumb() == show) {
            return;
        }
        getState().setShowThumb(show);
        invalidate();
    }

    public final void setSize(IntentSeekBarSize size) {
        Intrinsics.checkNotNullParameter(size, "size");
        if (this.mSize == size) {
            return;
        }
        this.mSize = size;
        ConicLightEffectRenderer conicLightEffectRenderer = this.mConicLightRenderer;
        if (conicLightEffectRenderer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mConicLightRenderer");
            conicLightEffectRenderer = null;
        }
        conicLightEffectRenderer.setSizeType(size);
        applySizeHeight();
        requestLayout();
        invalidate();
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void setTouchScale(float scale, boolean updateDrawProgressScale) {
        super.setTouchScale(scale, true);
    }

    public final void setUseMaterialEffect(int value) {
        int iSanitizeUseMaterialEffect = sanitizeUseMaterialEffect(value);
        if (this.mUseMaterialEffect == iSanitizeUseMaterialEffect) {
            return;
        }
        this.mUseMaterialEffect = iSanitizeUseMaterialEffect;
        refreshConicLightActivation();
        syncConicLightToVisualState();
        invalidate();
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void stopTrackingTouch(boolean isFromUser, boolean isTouching) {
        boolean z2 = true;
        boolean z3 = getState().getIsDragging() || getMStartDragging();
        super.stopTrackingTouch(isFromUser, isTouching);
        if (!getState().getIsDragging() && !getMStartDragging()) {
            z2 = false;
        }
        if (z3 != z2) {
            this.mWasDragging = z2;
            applyVisualState();
        }
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void touchAnim() {
        getMThumbStateHandler().cancel();
        getMThumbStateHandler().setPendingShrink(false);
        super.touchAnim();
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void updateColors() {
        super.updateColors();
        if (this.mSecondaryProgressRenderer != null) {
            this.mColorsDirty = true;
        }
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void updateState() {
        super.updateState();
        if (!this.mColorsDirty || this.mSecondaryProgressRenderer == null) {
            return;
        }
        this.mColorsDirty = false;
        applyIntentColors();
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarAdvanced
    public boolean useDragTransitionAnimation() {
        return true;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIIntentSeekBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIIntentSeekBar(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0, 8, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ COUIIntentSeekBar(Context context, AttributeSet attributeSet, int i3, int i4, int i5, DefaultConstructorMarker defaultConstructorMarker) {
        attributeSet = (i5 & 2) != 0 ? null : attributeSet;
        i3 = (i5 & 4) != 0 ? R.attr.couiIntentSeekBarStyle : i3;
        if ((i5 & 8) != 0) {
            if (COUIContextUtil.isCOUIDarkTheme(context)) {
                i4 = R.style.COUIIntentSeekBar_Dark;
            } else {
                i4 = R.style.COUIIntentSeekBar;
            }
        }
        this(context, attributeSet, i3, i4);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUIIntentSeekBar(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        Intrinsics.checkNotNullParameter(context, "context");
        this.mProgressClipPath = new Path();
        this.mQuaternaryAttrId = com.support.appcompat.R.attr.couiColorLabelQuaternary;
        this.mPrimaryAttrId = com.support.appcompat.R.attr.couiColorLabelPrimary;
        this.mColorsDirty = true;
        this.mPlaybackState = PlaybackState.IDLE;
        this.mCurrentVisualState = VisualState.STATIC;
        this.mThumbStateHandler = LazyKt.lazy(new COUIIntentSeekBar$mThumbStateHandler$2(this));
        this.mThumbStateCallback = new COUIIntentSeekBar$mThumbStateCallback$1(this);
        this.mSize = IntentSeekBarSize.DEFAULT;
        this.mFollowSystemTheme = true;
        this.mUseMaterialEffect = -1;
        this.mConicLightColor = DEFAULT_CONIC_LIGHT_COLOR;
        this.mLoadingDragReturnHandler = new LoadingDragReturnHandler(new COUIIntentSeekBar$mLoadingDragReturnHandler$1(this));
        this.mColorResult = new int[3];
        this.mLastDrawnSecondaryProgressForSnap = Integer.MIN_VALUE;
        setInvalidatePixelThreshold(1.0f);
        this.mSecondaryProgressRenderer = new SecondaryProgressRenderer();
        ConicLightEffectRenderer conicLightEffectRenderer = new ConicLightEffectRenderer(this);
        this.mConicLightRenderer = conicLightEffectRenderer;
        addEffect(conicLightEffectRenderer);
        this.mLoadingRenderer = new LoadingEffectRenderer(this);
        this.mBlurBlendRenderer = new BlurBlendEffectRenderer(this);
        getMTouchMotionEffectRenderer().setOnTranslationXChangeListener(new AnonymousClass1());
        initIntentAttributes(context, attributeSet, i3, i4);
        ConicLightEffectRenderer conicLightEffectRenderer2 = this.mConicLightRenderer;
        if (conicLightEffectRenderer2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mConicLightRenderer");
            conicLightEffectRenderer2 = null;
        }
        conicLightEffectRenderer2.setSizeType(this.mSize);
        ConicLightEffectRenderer conicLightEffectRenderer3 = this.mConicLightRenderer;
        if (conicLightEffectRenderer3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mConicLightRenderer");
            conicLightEffectRenderer3 = null;
        }
        conicLightEffectRenderer3.setConicLightColor(this.mConicLightColor);
        refreshConicLightActivation();
        IProgressRenderer mProgressRenderer = getMProgressRenderer();
        IntentConicProgressRenderer intentConicProgressRenderer = mProgressRenderer instanceof IntentConicProgressRenderer ? (IntentConicProgressRenderer) mProgressRenderer : null;
        if (intentConicProgressRenderer != null) {
            intentConicProgressRenderer.setProgressColorA(this.mConicLightColor);
        }
    }
}