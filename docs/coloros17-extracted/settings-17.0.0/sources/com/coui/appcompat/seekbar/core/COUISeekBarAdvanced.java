package com.coui.appcompat.seekbar.core;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import androidx.core.app.NotificationCompat;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.OnMaterialStateChangeListener;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.seekbar.animation.ClickAnimationHandler;
import com.coui.appcompat.seekbar.animation.FlexibleFollowHandHandler;
import com.coui.appcompat.seekbar.animation.IAnimationHandler;
import com.coui.appcompat.seekbar.animation.PhysicsAnimationHandler;
import com.coui.appcompat.seekbar.effect.DeformationEffectParams;
import com.coui.appcompat.seekbar.effect.DeformationEffectRenderer;
import com.coui.appcompat.seekbar.effect.GlitterEffectRenderer;
import com.coui.appcompat.seekbar.effect.SpotLightEffectRenderer;
import com.coui.appcompat.seekbar.effect.TouchMotionEffectRenderer;
import com.coui.appcompat.seekbar.feedback.VibrationFeedbackHandler;
import com.coui.appcompat.seekbar.listener.DeformedValueBean;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.support.seekbar.R;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.math.MathKt;
import kotlin.ranges.RangesKt;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\u009c\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b*\n\u0002\u0010\r\n\u0002\b\u001e\n\u0002\u0018\u0002\n\u0002\b*\b\u0017\u0018\u0000 ¿\u00012\u00020\u00012\u00020\u0002:\u0002¿\u0001B/\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\b¢\u0006\u0002\u0010\nJ\u0010\u0010H\u001a\u00020\u00142\u0006\u0010I\u001a\u00020JH\u0002J\b\u0010K\u001a\u00020LH\u0014J\b\u0010M\u001a\u00020LH\u0014J\u0010\u0010N\u001a\u00020.2\u0006\u0010I\u001a\u00020JH\u0016J \u0010O\u001a\u00020L2\u0006\u0010P\u001a\u00020\u00142\u0006\u0010Q\u001a\u00020\u00142\u0006\u0010R\u001a\u00020\u0014H\u0014J\u0010\u0010S\u001a\u00020L2\u0006\u0010I\u001a\u00020JH\u0014J\b\u0010T\u001a\u00020LH\u0014J\b\u0010U\u001a\u00020LH\u0002J\u0010\u0010V\u001a\u00020L2\u0006\u0010Q\u001a\u00020\u0014H\u0002J\b\u0010W\u001a\u00020\u0014H\u0002J\u0010\u0010X\u001a\u00020\u00142\u0006\u0010I\u001a\u00020JH\u0002J\u0010\u0010Y\u001a\u00020L2\u0006\u0010I\u001a\u00020JH\u0014J\u0010\u0010Z\u001a\u00020L2\u0006\u0010I\u001a\u00020JH\u0014J*\u0010[\u001a\u00020L2\u0006\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u0014J\b\u0010\\\u001a\u00020LH\u0014J\b\u0010]\u001a\u00020LH\u0014J\b\u0010^\u001a\u00020LH\u0002J\b\u0010_\u001a\u00020LH\u0014J\b\u0010`\u001a\u00020LH\u0014J\u0010\u0010a\u001a\u00020L2\u0006\u0010\u0003\u001a\u00020\u0004H\u0014J\u0010\u0010b\u001a\u00020L2\u0006\u0010\u0003\u001a\u00020\u0004H\u0014J\b\u0010c\u001a\u00020LH\u0014J\u0010\u0010d\u001a\u00020L2\u0006\u0010\u0003\u001a\u00020\u0004H\u0014J\b\u0010e\u001a\u00020.H\u0016J\b\u0010f\u001a\u00020.H\u0016J\b\u0010g\u001a\u00020.H\u0016J\b\u0010h\u001a\u00020.H\u0016J\u0010\u0010i\u001a\u00020.2\u0006\u0010j\u001a\u00020\u0014H\u0002J\u0010\u0010k\u001a\u00020.2\u0006\u0010j\u001a\u00020\u0014H\u0002J\b\u0010l\u001a\u00020.H\u0016J\b\u0010m\u001a\u00020.H\u0016J\u0018\u0010n\u001a\u00020.2\u0006\u0010o\u001a\u00020\u00142\u0006\u0010p\u001a\u00020\u0014H\u0014J\b\u0010q\u001a\u00020LH\u0014J\u0010\u0010r\u001a\u00020L2\u0006\u0010s\u001a\u00020\u0014H\u0014J\b\u0010t\u001a\u00020LH\u0014J\u0010\u0010u\u001a\u00020L2\u0006\u0010s\u001a\u00020\u0014H\u0014J\b\u0010v\u001a\u00020wH\u0016J0\u0010x\u001a\u00020L2\u0006\u0010y\u001a\u00020.2\u0006\u0010z\u001a\u00020\b2\u0006\u0010{\u001a\u00020\b2\u0006\u0010|\u001a\u00020\b2\u0006\u0010}\u001a\u00020\bH\u0014J\u0019\u0010~\u001a\u00020L2\u0006\u0010\u007f\u001a\u00020\b2\u0007\u0010\u0080\u0001\u001a\u00020.H\u0016J\t\u0010\u0081\u0001\u001a\u00020LH\u0014J\u0019\u0010\u0082\u0001\u001a\u00020L2\u0006\u0010s\u001a\u00020\u00142\u0006\u0010Q\u001a\u00020\u0014H\u0014J-\u0010\u0083\u0001\u001a\u00020L2\u0007\u0010\u0084\u0001\u001a\u00020\b2\u0007\u0010\u0085\u0001\u001a\u00020\b2\u0007\u0010\u0086\u0001\u001a\u00020\b2\u0007\u0010\u0087\u0001\u001a\u00020\bH\u0014J\u0012\u0010\u0088\u0001\u001a\u00020L2\u0007\u0010\u0089\u0001\u001a\u00020.H\u0014J\u0011\u0010\u008a\u0001\u001a\u00020.2\u0006\u0010I\u001a\u00020JH\u0016J\t\u0010\u008b\u0001\u001a\u00020LH\u0014J\t\u0010\u008c\u0001\u001a\u00020LH\u0014J\t\u0010\u008d\u0001\u001a\u00020LH\u0014J\t\u0010\u008e\u0001\u001a\u00020LH\u0014J\t\u0010\u008f\u0001\u001a\u00020LH\u0014J\u0012\u0010\u0090\u0001\u001a\u00020\u00142\u0007\u0010\u0091\u0001\u001a\u00020\u0014H\u0002J\u0012\u0010\u0092\u0001\u001a\u00020L2\u0007\u0010\u0093\u0001\u001a\u00020\u0014H\u0002J\u0015\u0010\u0094\u0001\u001a\u00020L2\n\u0010\u0095\u0001\u001a\u0005\u0018\u00010\u0096\u0001H\u0016J\u0012\u0010\u0097\u0001\u001a\u00020L2\u0007\u0010\u0098\u0001\u001a\u00020.H\u0016J\u0012\u0010\u0099\u0001\u001a\u00020L2\u0007\u0010\u0098\u0001\u001a\u00020.H\u0016J\u0012\u0010\u009a\u0001\u001a\u00020L2\u0007\u0010\u009b\u0001\u001a\u00020\u0014H\u0016J\u001b\u0010\u009c\u0001\u001a\u00020L2\u0007\u0010\u009d\u0001\u001a\u00020\u00142\u0007\u0010\u009e\u0001\u001a\u00020\u0014H\u0016J\u0012\u0010\u009f\u0001\u001a\u00020L2\u0007\u0010\u0093\u0001\u001a\u00020\u0014H\u0002J\u0012\u0010 \u0001\u001a\u00020L2\u0007\u0010¡\u0001\u001a\u00020\u0014H\u0016J\u0012\u0010¢\u0001\u001a\u00020L2\u0007\u0010£\u0001\u001a\u00020\bH\u0016J\u0012\u0010¤\u0001\u001a\u00020L2\u0007\u0010¥\u0001\u001a\u00020\u0014H\u0016J\u0012\u0010¦\u0001\u001a\u00020L2\u0007\u0010\u0098\u0001\u001a\u00020.H\u0016J\u0012\u0010§\u0001\u001a\u00020L2\u0007\u0010¨\u0001\u001a\u00020.H\u0016J\u0012\u0010©\u0001\u001a\u00020L2\u0007\u0010ª\u0001\u001a\u00020.H\u0016J\u0011\u0010«\u0001\u001a\u00020L2\u0006\u0010I\u001a\u00020JH\u0002J\t\u0010¬\u0001\u001a\u00020\u0014H\u0002J\t\u0010\u00ad\u0001\u001a\u00020\u0014H\u0002J\u0011\u0010®\u0001\u001a\u00020L2\u0006\u0010I\u001a\u00020JH\u0014J\t\u0010¯\u0001\u001a\u00020LH\u0014J\u001b\u0010°\u0001\u001a\u00020L2\u0007\u0010±\u0001\u001a\u00020\u00142\u0007\u0010²\u0001\u001a\u00020\u0014H\u0002J\u0011\u0010³\u0001\u001a\u00020L2\u0006\u0010Q\u001a\u00020\u0014H\u0002J\u0011\u0010´\u0001\u001a\u00020L2\u0006\u0010Q\u001a\u00020\u0014H\u0014J\u0012\u0010µ\u0001\u001a\u00020L2\u0007\u0010¶\u0001\u001a\u00020\u0014H\u0002J\t\u0010·\u0001\u001a\u00020LH\u0016J\t\u0010¸\u0001\u001a\u00020LH\u0016J\t\u0010¹\u0001\u001a\u00020LH\u0004J\t\u0010º\u0001\u001a\u00020LH\u0016J\u0011\u0010»\u0001\u001a\u00020.2\u0006\u0010I\u001a\u00020JH\u0014J\u0011\u0010¼\u0001\u001a\u00020L2\u0006\u0010I\u001a\u00020JH\u0014J\u0011\u0010½\u0001\u001a\u00020L2\u0006\u0010I\u001a\u00020JH\u0014J\t\u0010¾\u0001\u001a\u00020.H\u0014R\u001c\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0013\u001a\u00020\u0014X\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u001b\u0010\u0019\u001a\u00020\u001a8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u001d\u0010\u001e\u001a\u0004\b\u001b\u0010\u001cR\u000e\u0010\u001f\u001a\u00020 X\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010!\u001a\u0004\u0018\u00010\"X\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&R\u001c\u0010'\u001a\u0004\u0018\u00010(X\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010*\"\u0004\b+\u0010,R\u000e\u0010-\u001a\u00020.X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020.X\u0082\u000e¢\u0006\u0002\n\u0000R\u001c\u00100\u001a\u0004\u0018\u000101X\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b2\u00103\"\u0004\b4\u00105R\u001c\u00106\u001a\u0004\u0018\u000107X\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b8\u00109\"\u0004\b:\u0010;R\u001a\u0010<\u001a\u00020=X\u0084.¢\u0006\u000e\n\u0000\u001a\u0004\b>\u0010?\"\u0004\b@\u0010AR\u001c\u0010B\u001a\u0004\u0018\u00010CX\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bD\u0010E\"\u0004\bF\u0010G¨\u0006À\u0001"}, d2 = {"Lcom/coui/appcompat/seekbar/core/COUISeekBarAdvanced;", "Lcom/coui/appcompat/seekbar/core/COUISeekBarCore;", "Lcom/coui/appcompat/OnMaterialStateChangeListener;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "defStyleRes", "(Landroid/content/Context;Landroid/util/AttributeSet;II)V", "mClickAnimHandler", "Lcom/coui/appcompat/seekbar/animation/ClickAnimationHandler;", "getMClickAnimHandler", "()Lcom/coui/appcompat/seekbar/animation/ClickAnimationHandler;", "setMClickAnimHandler", "(Lcom/coui/appcompat/seekbar/animation/ClickAnimationHandler;)V", "mDragTransitionAnim", "Lcom/coui/appcompat/animation/dynamicanimation/COUISpringAnimation;", "mFastMoveScaleOffsetX", "", "getMFastMoveScaleOffsetX", "()F", "setMFastMoveScaleOffsetX", "(F)V", "mFastMoveSpring", "Lcom/facebook/rebound/Spring;", "getMFastMoveSpring", "()Lcom/facebook/rebound/Spring;", "mFastMoveSpring$delegate", "Lkotlin/Lazy;", "mFastMoveSpringConfig", "Lcom/facebook/rebound/SpringConfig;", "mFlexibleFollowHandHandler", "Lcom/coui/appcompat/seekbar/animation/FlexibleFollowHandHandler;", "getMFlexibleFollowHandHandler", "()Lcom/coui/appcompat/seekbar/animation/FlexibleFollowHandHandler;", "setMFlexibleFollowHandHandler", "(Lcom/coui/appcompat/seekbar/animation/FlexibleFollowHandHandler;)V", "mGlitterRenderer", "Lcom/coui/appcompat/seekbar/effect/GlitterEffectRenderer;", "getMGlitterRenderer", "()Lcom/coui/appcompat/seekbar/effect/GlitterEffectRenderer;", "setMGlitterRenderer", "(Lcom/coui/appcompat/seekbar/effect/GlitterEffectRenderer;)V", "mIsDragTransitioning", "", "mNeedDragTransition", "mPhysicsHandler", "Lcom/coui/appcompat/seekbar/animation/PhysicsAnimationHandler;", "getMPhysicsHandler", "()Lcom/coui/appcompat/seekbar/animation/PhysicsAnimationHandler;", "setMPhysicsHandler", "(Lcom/coui/appcompat/seekbar/animation/PhysicsAnimationHandler;)V", "mSpotLightRenderer", "Lcom/coui/appcompat/seekbar/effect/SpotLightEffectRenderer;", "getMSpotLightRenderer", "()Lcom/coui/appcompat/seekbar/effect/SpotLightEffectRenderer;", "setMSpotLightRenderer", "(Lcom/coui/appcompat/seekbar/effect/SpotLightEffectRenderer;)V", "mTouchMotionEffectRenderer", "Lcom/coui/appcompat/seekbar/effect/TouchMotionEffectRenderer;", "getMTouchMotionEffectRenderer", "()Lcom/coui/appcompat/seekbar/effect/TouchMotionEffectRenderer;", "setMTouchMotionEffectRenderer", "(Lcom/coui/appcompat/seekbar/effect/TouchMotionEffectRenderer;)V", "mVibrationHandler", "Lcom/coui/appcompat/seekbar/feedback/VibrationFeedbackHandler;", "getMVibrationHandler", "()Lcom/coui/appcompat/seekbar/feedback/VibrationFeedbackHandler;", "setMVibrationHandler", "(Lcom/coui/appcompat/seekbar/feedback/VibrationFeedbackHandler;)V", "calculateDragTargetPixel", NotificationCompat.CATEGORY_EVENT, "Landroid/view/MotionEvent;", "cancelDeformationRelease", "", "cancelFlexibleFollowHandAnim", "dispatchTouchEvent", "executeFlingGlitterEffectAnim", "currentScale", "velocity", "lastScale", "executeThumbScaleAnim", "executeTouchGlitterEffectAnim", "flingBehaviorAfterDeformationDrag", "flingBehaviorAfterEndDrag", "getDeformationFlingScale", "getTargetPixelFromEvent", "handleMotionEventDown", "handleMotionEventUp", "initAdvancedFeatures", "initClickAnimation", "initDeformationEffect", "initDragTransitionAnim", "initFlexibleFollowHand", "initGlitterEffect", "initPhysicsAnimation", "initSpotLightEffect", "initTouchMotionEffect", "initVibrationFeedback", "isEnableAdaptiveVibrator", "isEnableVibrator", "isMoveFollowHand", "isPhysicalEnabled", "isPrimaryOnTrackForSnap", "primary", "isPrimaryWithinViewBoundsForSnap", "isStartFromMiddle", "isSupportDeformation", "isWithinThumbBounds", "x", "y", "onAttachedToWindow", "onClickAnimationUpdate", "value", "onDetachedFromWindow", "onDragTransitionAnimUpdate", "onDump", "", "onLayout", "changed", "left", "top", "right", "bottom", "onMaterialStateChange", "flag", "newState", "onPhysicsFlingEnd", "onPhysicsFlingUpdate", "onSizeChanged", "w", "h", "oldw", "oldh", "onSpotLightEnabledChanged", "enabled", "onTouchEvent", "performFeedback", "performVibrateFeedback", "refreshPixPerProgress", "releaseThumbScaleAnim", "resetDeformationValue", "resolvePrimaryForSnap", "rawPrimary", "setDeformationScale", "scale", "setDeformedParams", "bean", "Lcom/coui/appcompat/seekbar/listener/DeformedValueBean;", "setEnableAdaptiveVibrator", "enable", "setEnableVibrator", "setFlingLinearDamping", "linearDamping", "setFlingProperty", "frequency", "dampingRatio", "setFlingScale", "setMaxHeightDeformed", "pxMaxHeightDeformedValue", "setMaxMovingDistance", "pxMovingDistance", "setMaxWidthDeformed", "pxMaxWidthDeformedValue", "setPhysicalEnabled", "setStartFromMiddle", "startFromMiddle", "setSupportDeformation", "supportDeformation", "snapScaleToFingerPosition", "snapTrackPrimaryMax", "snapTrackPrimaryMin", "startClickTransitionAnim", "startDeformationRelease", "startDragTransitionAnim", "startPixel", "endPixel", "startFastMoveAnimation", "startFling", "startGlitterEffectAnim", "xVelocity", "stopAutoProgressAnimations", "stopClikAnim", "stopDragTransitionAnim", "stopPhysicsMove", "touchInSeekBar", "trackTouchEvent", "trackTouchEventByFinger", "useDragTransitionAnimation", "Companion", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class COUISeekBarAdvanced extends COUISeekBarCore implements OnMaterialStateChangeListener {
    private static final double DEFAULT_SPRING_FRICTION = 30.0d;
    private static final double DEFAULT_SPRING_TENSION = 500.0d;
    private static final float DRAG_TRANSITION_SPRING_BOUNCE = 0.0f;
    private static final float DRAG_TRANSITION_SPRING_RESPONSE = 0.15f;
    private static final float FAST_MOVE_VELOCITY = 95.0f;
    private static final float MAX_FAST_MOVE_PERCENT = 0.95f;
    private static final int MAX_VELOCITY = 8000;
    private static final float MIN_FAST_MOVE_PERCENT = 0.05f;
    private static final String TAG = "COUISeekBarAdvanced";
    private static final int VELOCITY_COMPUTE_TIME = 100;
    private static final int VELOCITY_UNITS_PX_PER_SEC = 1000;
    private ClickAnimationHandler mClickAnimHandler;
    private COUISpringAnimation mDragTransitionAnim;
    private float mFastMoveScaleOffsetX;

    /* JADX INFO: renamed from: mFastMoveSpring$delegate, reason: from kotlin metadata */
    private final Lazy mFastMoveSpring;
    private final SpringConfig mFastMoveSpringConfig;
    private FlexibleFollowHandHandler mFlexibleFollowHandHandler;
    private GlitterEffectRenderer mGlitterRenderer;
    private boolean mIsDragTransitioning;
    private boolean mNeedDragTransition;
    private PhysicsAnimationHandler mPhysicsHandler;
    private SpotLightEffectRenderer mSpotLightRenderer;
    protected TouchMotionEffectRenderer mTouchMotionEffectRenderer;
    private VibrationFeedbackHandler mVibrationHandler;

    /* JADX INFO: renamed from: com.coui.appcompat.seekbar.core.COUISeekBarAdvanced$initClickAnimation$1, reason: invalid class name */
    @Metadata(d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, d2 = {"com/coui/appcompat/seekbar/core/COUISeekBarAdvanced$initClickAnimation$1", "Lcom/coui/appcompat/seekbar/animation/ClickAnimationHandler$OnClickAnimUpdateListener;", "onClickAnimUpdate", "", "value", "", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class AnonymousClass1 implements ClickAnimationHandler.OnClickAnimUpdateListener {
        public AnonymousClass1() {
        }

        @Override // com.coui.appcompat.seekbar.animation.ClickAnimationHandler.OnClickAnimUpdateListener
        public void onClickAnimUpdate(float value) {
            COUISeekBarAdvanced.this.onClickAnimationUpdate(value);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.seekbar.core.COUISeekBarAdvanced$initPhysicsAnimation$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0016¨\u0006\u0007"}, d2 = {"com/coui/appcompat/seekbar/core/COUISeekBarAdvanced$initPhysicsAnimation$1", "Lcom/coui/appcompat/seekbar/animation/PhysicsAnimationHandler$OnFlingUpdateListener;", "onFlingUpdate", "", "value", "", "velocity", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class C13511 implements PhysicsAnimationHandler.OnFlingUpdateListener {
        public C13511() {
        }

        @Override // com.coui.appcompat.seekbar.animation.PhysicsAnimationHandler.OnFlingUpdateListener
        public void onFlingUpdate(float value, float velocity) {
            COUISeekBarAdvanced.this.onPhysicsFlingUpdate(value, velocity);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.seekbar.core.COUISeekBarAdvanced$initPhysicsAnimation$2, reason: invalid class name */
    @Metadata(d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, d2 = {"com/coui/appcompat/seekbar/core/COUISeekBarAdvanced$initPhysicsAnimation$2", "Lcom/coui/appcompat/seekbar/animation/PhysicsAnimationHandler$OnFlingEndListener;", "onFlingEnd", "", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class AnonymousClass2 implements PhysicsAnimationHandler.OnFlingEndListener {
        public AnonymousClass2() {
        }

        @Override // com.coui.appcompat.seekbar.animation.PhysicsAnimationHandler.OnFlingEndListener
        public void onFlingEnd() {
            COUISeekBarAdvanced.this.onPhysicsFlingEnd();
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUISeekBarAdvanced(Context context) {
        this(context, null, 0, 0, 14, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public static final /* synthetic */ SpringConfig access$getMFastMoveSpringConfig$p(COUISeekBarAdvanced cOUISeekBarAdvanced) {
        return cOUISeekBarAdvanced.mFastMoveSpringConfig;
    }

    private final float calculateDragTargetPixel(MotionEvent event) {
        float touchPrimaryCoordinate = getTouchPrimaryCoordinate(event);
        float seekBarLength = getSeekBarLength();
        if (seekBarLength <= 0.0f || getMPixPerProgress() <= 0.0f) {
            return -1.0f;
        }
        SeekBarState state = getState();
        int max = state.getMax() - state.getMin();
        if (max <= 0) {
            return -1.0f;
        }
        return ((RangesKt.coerceIn(primaryToHorizontalAbsoluteScale(touchPrimaryCoordinate, seekBarLength), 0.0f, 1.0f) * max) + state.getMin()) * getMPixPerProgress();
    }

    public static /* synthetic */ void d(COUISeekBarAdvanced cOUISeekBarAdvanced, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        initDragTransitionAnim$lambda$5(cOUISeekBarAdvanced, cOUIDynamicAnimation, f3, f4);
    }

    public static /* synthetic */ void e(COUISeekBarAdvanced cOUISeekBarAdvanced, Ref.IntRef intRef, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        startClickTransitionAnim$lambda$8(cOUISeekBarAdvanced, intRef, cOUIDynamicAnimation, z2, f3, f4);
    }

    public static /* synthetic */ void f(COUISeekBarAdvanced cOUISeekBarAdvanced, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        initDragTransitionAnim$lambda$6(cOUISeekBarAdvanced, cOUIDynamicAnimation, z2, f3, f4);
    }

    private final void flingBehaviorAfterDeformationDrag() {
        float max;
        PhysicsAnimationHandler physicsAnimationHandler = this.mPhysicsHandler;
        if (physicsAnimationHandler == null || !getMState().getIsSupportDeformation()) {
            return;
        }
        if (getMState().getScale() > 1.0f || getMState().getScale() < 0.0f) {
            float seekBarLength = getSeekBarLength();
            int max2 = getMState().getMax() - getMState().getMin();
            float f3 = max2 > 0 ? seekBarLength / max2 : 0.0f;
            float deformationFlingScale = getDeformationFlingScale();
            if (isVertical()) {
                max = (1.0f - deformationFlingScale) * seekBarLength;
            } else {
                max = getMState().getIsRtl() ? (getMState().getMax() - (deformationFlingScale * max2)) * f3 : deformationFlingScale * max2 * f3;
            }
            ClickAnimationHandler clickAnimationHandler = this.mClickAnimHandler;
            if (clickAnimationHandler != null) {
                clickAnimationHandler.stopClikAnim();
            }
            physicsAnimationHandler.startFling(max);
        }
    }

    private final void flingBehaviorAfterEndDrag(float velocity) {
        int progress;
        float deformationFlingScale;
        float deformationFlingScale2;
        PhysicsAnimationHandler physicsAnimationHandler = this.mPhysicsHandler;
        if (physicsAnimationHandler == null) {
            return;
        }
        ClickAnimationHandler clickAnimationHandler = this.mClickAnimHandler;
        if (clickAnimationHandler != null) {
            clickAnimationHandler.stopClikAnim();
        }
        FlexibleFollowHandHandler flexibleFollowHandHandler = this.mFlexibleFollowHandHandler;
        if (flexibleFollowHandHandler != null) {
            flexibleFollowHandHandler.cancelFlexibleFollowHandAnim();
        }
        float seekBarLength = getSeekBarLength();
        int max = getMState().getMax() - getMState().getMin();
        float f3 = max > 0 ? seekBarLength / max : 0.0f;
        if (isVertical()) {
            deformationFlingScale2 = (1.0f - (getMState().getIsSupportDeformation() ? getDeformationFlingScale() : (getMState().getProgress() - getMState().getMin()) / max)) * seekBarLength;
        } else {
            if (getMState().getIsRtl()) {
                if (getMState().getIsSupportDeformation()) {
                    deformationFlingScale = getMState().getMax() - (getDeformationFlingScale() * max);
                } else {
                    progress = (getMState().getMax() - getMState().getProgress()) + getMState().getMin();
                    deformationFlingScale = progress;
                }
            } else if (getMState().getIsSupportDeformation()) {
                deformationFlingScale = getDeformationFlingScale() * max;
            } else {
                progress = getMState().getProgress() - getMState().getMin();
                deformationFlingScale = progress;
            }
            deformationFlingScale2 = deformationFlingScale * f3;
        }
        physicsAnimationHandler.startFling(deformationFlingScale2, velocity);
    }

    private final float getDeformationFlingScale() {
        if (getMState().getScale() > 1.0f) {
            return ((getMState().getScale() - 1.0f) / 5) + 1.0f;
        }
        return getMState().getScale() < 0.0f ? getMState().getScale() / 5 : getMState().getScale();
    }

    private final Spring getMFastMoveSpring() {
        Object value = this.mFastMoveSpring.getValue();
        Intrinsics.checkNotNullExpressionValue(value, "getValue(...)");
        return (Spring) value;
    }

    private final float getTargetPixelFromEvent(MotionEvent event) {
        float touchPrimaryCoordinate = getTouchPrimaryCoordinate(event);
        float seekBarLength = getSeekBarLength();
        if (seekBarLength <= 0.0f || getMPixPerProgress() <= 0.0f) {
            return -1.0f;
        }
        SeekBarState state = getState();
        int max = state.getMax() - state.getMin();
        if (max <= 0) {
            return -1.0f;
        }
        return ((RangesKt.coerceIn(primaryToHorizontalAbsoluteScale(touchPrimaryCoordinate, seekBarLength), -1.0f, 2.0f) * max) + state.getMin()) * getMPixPerProgress();
    }

    private final void initDragTransitionAnim() {
        if (this.mDragTransitionAnim != null) {
            return;
        }
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(0.0f);
        cOUISpringForce.setResponse(0.15f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mDragTransitionAnim = spring;
        if (spring != null) {
            spring.addUpdateListener(new a(this));
        }
        COUISpringAnimation cOUISpringAnimation = this.mDragTransitionAnim;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.addEndListener(new b(this));
        }
    }

    private static final void initDragTransitionAnim$lambda$5(COUISeekBarAdvanced this$0, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.onDragTransitionAnimUpdate(f3);
    }

    private static final void initDragTransitionAnim$lambda$6(COUISeekBarAdvanced this$0, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (!this$0.getState().getIsDragging()) {
            this$0.mIsDragTransitioning = false;
        }
        this$0.setMInternalProgress(this$0.getState().getProgress());
        FlexibleFollowHandHandler flexibleFollowHandHandler = this$0.mFlexibleFollowHandHandler;
        if (flexibleFollowHandHandler != null) {
            flexibleFollowHandHandler.setStartValue(this$0.getState().getDrawProgressScale());
        }
    }

    private final boolean isPrimaryOnTrackForSnap(float primary) {
        if (getSeekBarLength() <= 0.0f) {
            return isPrimaryWithinViewBoundsForSnap(primary);
        }
        return primary >= snapTrackPrimaryMin() && primary <= snapTrackPrimaryMax();
    }

    private final boolean isPrimaryWithinViewBoundsForSnap(float primary) {
        if (isVertical()) {
            return primary >= ((float) getPaddingTop()) && primary <= ((float) (getHeight() - getPaddingBottom()));
        }
        return primary >= 0.0f && primary <= ((float) getWidth());
    }

    private final float resolvePrimaryForSnap(float rawPrimary) {
        return isPrimaryOnTrackForSnap(rawPrimary) ? rawPrimary : RangesKt.coerceIn(rawPrimary, snapTrackPrimaryMin(), snapTrackPrimaryMax());
    }

    private final void setDeformationScale(float scale) {
        if (scale > 1.0f) {
            scale = ((scale - 1.0f) * 5) + 1.0f;
        } else if (scale < 0.0f) {
            scale *= 5;
        }
        getMState().setScale(RangesKt.coerceIn(scale, -1.0f, 2.0f));
        getMState().setDrawProgressScale(getMState().getScale());
    }

    private final void setFlingScale(float scale) {
        if (!getMState().getIsSupportDeformation()) {
            getMState().setScale(RangesKt.coerceIn(scale, 0.0f, 1.0f));
            getMState().setDrawProgressScale(getMState().getScale());
            return;
        }
        DeformationEffectRenderer mDeformationRenderer = getMDeformationRenderer();
        if (mDeformationRenderer != null) {
            mDeformationRenderer.calculateFlingDeformationValue(scale);
        }
        setDeformationScale(scale);
        DeformationEffectRenderer mDeformationRenderer2 = getMDeformationRenderer();
        if (mDeformationRenderer2 != null) {
            mDeformationRenderer2.notifyScaleChanged(getMInternalProgress());
        }
    }

    private final void snapScaleToFingerPosition(MotionEvent event) {
        COUISeekBarCore.OnSeekBarChangeListener mOnSeekBarChangeListener;
        float fResolvePrimaryForSnap = resolvePrimaryForSnap(getTouchPrimaryCoordinate(event));
        float seekBarLength = getSeekBarLength();
        if (seekBarLength <= 0.0f) {
            return;
        }
        float progressStart = getProgressStart();
        SeekBarState state = getState();
        int max = state.getMax() - state.getMin();
        if (max <= 0) {
            return;
        }
        int progress = state.getProgress();
        setTouchScale(isVertical() ? ((progressStart + seekBarLength) - fResolvePrimaryForSnap) / seekBarLength : primaryToHorizontalAbsoluteScale(fResolvePrimaryForSnap, seekBarLength), false);
        int progressLimit = getProgressLimit(MathKt.roundToInt((state.getScale() * max) + state.getMin()));
        setMInternalProgress(progressLimit);
        state.setProgress(getRealProgress(progressLimit));
        state.setDrawProgressScale(RangesKt.coerceIn(state.getScale(), interactionFloorScale(), 1.0f));
        syncProgressToParent(state.getProgress());
        if (progress != state.getProgress() && (mOnSeekBarChangeListener = getMOnSeekBarChangeListener()) != null) {
            mOnSeekBarChangeListener.onProgressChanged(this, state.getProgress(), true);
        }
        setMLastX(fResolvePrimaryForSnap);
    }

    private final float snapTrackPrimaryMax() {
        float seekBarLength = getSeekBarLength();
        SeekBarState state = getState();
        if (seekBarLength <= 0.0f) {
            return isVertical() ? getHeight() - getPaddingBottom() : getWidth();
        }
        if (!isVertical() && state.getIsRtl()) {
            return (getWidth() - getEnd()) - state.getPaddingHorizontal();
        }
        return getProgressStart() + seekBarLength;
    }

    private final float snapTrackPrimaryMin() {
        float seekBarLength = getSeekBarLength();
        SeekBarState state = getState();
        if (seekBarLength <= 0.0f) {
            if (isVertical()) {
                return getPaddingTop();
            }
            return 0.0f;
        }
        if (!isVertical() && state.getIsRtl()) {
            return ((getWidth() - getEnd()) - state.getPaddingHorizontal()) - seekBarLength;
        }
        return getProgressStart();
    }

    private static final void startClickTransitionAnim$lambda$8(COUISeekBarAdvanced this$0, Ref.IntRef targetProgress, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        COUISeekBarCore.OnSeekBarChangeListener mOnSeekBarChangeListener;
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(targetProgress, "$targetProgress");
        if (!z2 && (mOnSeekBarChangeListener = this$0.getMOnSeekBarChangeListener()) != null) {
            mOnSeekBarChangeListener.onProgressChanged(this$0, targetProgress.element, true);
        }
        this$0.stopTrackingTouch(true, true);
        this$0.invalidate();
    }

    private final void startDragTransitionAnim(float startPixel, float endPixel) {
        initDragTransitionAnim();
        COUISpringAnimation cOUISpringAnimation = this.mDragTransitionAnim;
        if (cOUISpringAnimation == null) {
            return;
        }
        if (cOUISpringAnimation.isRunning()) {
            cOUISpringAnimation.cancel();
        }
        this.mIsDragTransitioning = true;
        cOUISpringAnimation.setStartValue(startPixel);
        cOUISpringAnimation.animateToFinalPosition(endPixel);
    }

    private final void startFastMoveAnimation(float velocity) {
        Spring mFastMoveSpring = getMFastMoveSpring();
        if (mFastMoveSpring.getCurrentValue() == mFastMoveSpring.getEndValue()) {
            int max = getMState().getMax() - getMState().getMin();
            float progress = getMState().getProgress();
            float f3 = max;
            boolean z2 = progress <= 0.95f * f3 && progress >= f3 * MIN_FAST_MOVE_PERCENT;
            if (velocity >= FAST_MOVE_VELOCITY && z2) {
                mFastMoveSpring.setEndValue(1.0d);
            } else if (velocity > -95.0f || !z2) {
                mFastMoveSpring.setEndValue(0.0d);
            } else {
                mFastMoveSpring.setEndValue(-1.0d);
            }
        }
    }

    private final void startGlitterEffectAnim(float xVelocity) {
        getMState().setBumpingEdges(true);
        GlitterEffectRenderer glitterEffectRenderer = this.mGlitterRenderer;
        if (glitterEffectRenderer != null) {
            glitterEffectRenderer.startAnimation(this, xVelocity);
        }
        invalidate();
    }

    public void cancelDeformationRelease() {
        DeformationEffectRenderer mDeformationRenderer = getMDeformationRenderer();
        if (mDeformationRenderer != null) {
            mDeformationRenderer.cancelDeformationRelease();
        }
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void cancelFlexibleFollowHandAnim() {
        FlexibleFollowHandHandler flexibleFollowHandHandler = this.mFlexibleFollowHandHandler;
        if (flexibleFollowHandHandler != null) {
            flexibleFollowHandHandler.cancelFlexibleFollowHandAnim();
        }
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore, android.view.View
    public boolean dispatchTouchEvent(MotionEvent event) {
        SpotLightEffectRenderer spotLightEffectRenderer;
        Intrinsics.checkNotNullParameter(event, "event");
        if (this.mTouchMotionEffectRenderer != null) {
            getMTouchMotionEffectRenderer().handleTouchEvent(event);
        }
        if (isEnabled() && (spotLightEffectRenderer = this.mSpotLightRenderer) != null) {
            spotLightEffectRenderer.onTouchEvent(event);
        }
        return super.dispatchTouchEvent(event);
    }

    public void executeFlingGlitterEffectAnim(float currentScale, float velocity, float lastScale) {
        if (!getMState().getIsStartFromMiddle()) {
            if (currentScale < 1.0f || getMState().getIsBumpingEdges() || lastScale >= 1.0f) {
                return;
            }
            startGlitterEffectAnim(velocity);
            return;
        }
        if (currentScale >= 1.0f && !getMState().getIsBumpingEdges() && lastScale < 1.0f) {
            startGlitterEffectAnim(velocity);
        } else {
            if (currentScale > 0.0f || getMState().getIsBumpingEdges() || lastScale <= 0.0f) {
                return;
            }
            startGlitterEffectAnim(velocity);
        }
    }

    public void executeThumbScaleAnim(MotionEvent event) {
        IAnimationHandler mAnimationHandler;
        Intrinsics.checkNotNullParameter(event, "event");
        if (!getMState().getShowThumb() || getMAnimationHandler() == null || !isWithinThumbBounds(event.getX(), event.getY()) || (mAnimationHandler = getMAnimationHandler()) == null) {
            return;
        }
        mAnimationHandler.startThumbScaleAnimation(true);
    }

    public void executeTouchGlitterEffectAnim() {
        VelocityTracker velocityTracker;
        PhysicsAnimationHandler physicsAnimationHandler = this.mPhysicsHandler;
        if (physicsAnimationHandler == null || (velocityTracker = physicsAnimationHandler.getVelocityTracker()) == null) {
            return;
        }
        velocityTracker.computeCurrentVelocity(1000, 8000.0f);
        float xVelocity = velocityTracker.getXVelocity();
        if (!getMState().getIsStartFromMiddle()) {
            if (getMState().getIsRtl()) {
                if (getMState().getScale() < 1.0f || getMState().getIsBumpingEdges() || xVelocity >= 0.0f) {
                    return;
                }
                startGlitterEffectAnim(xVelocity);
                return;
            }
            if (getMState().getScale() < 1.0f || getMState().getIsBumpingEdges() || xVelocity <= 0.0f) {
                return;
            }
            startGlitterEffectAnim(xVelocity);
            return;
        }
        if (getMState().getIsRtl()) {
            if (getMState().getScale() <= 0.0f && !getMState().getIsBumpingEdges() && xVelocity > 0.0f) {
                startGlitterEffectAnim(xVelocity);
                return;
            } else {
                if (getMState().getScale() < 1.0f || getMState().getIsBumpingEdges() || xVelocity >= 0.0f) {
                    return;
                }
                startGlitterEffectAnim(xVelocity);
                return;
            }
        }
        if (getMState().getScale() <= 0.0f && !getMState().getIsBumpingEdges() && xVelocity < 0.0f) {
            startGlitterEffectAnim(xVelocity);
        } else {
            if (getMState().getScale() < 1.0f || getMState().getIsBumpingEdges() || xVelocity <= 0.0f) {
                return;
            }
            startGlitterEffectAnim(xVelocity);
        }
    }

    public final ClickAnimationHandler getMClickAnimHandler() {
        return this.mClickAnimHandler;
    }

    public final float getMFastMoveScaleOffsetX() {
        return this.mFastMoveScaleOffsetX;
    }

    public final FlexibleFollowHandHandler getMFlexibleFollowHandHandler() {
        return this.mFlexibleFollowHandHandler;
    }

    public final GlitterEffectRenderer getMGlitterRenderer() {
        return this.mGlitterRenderer;
    }

    public final PhysicsAnimationHandler getMPhysicsHandler() {
        return this.mPhysicsHandler;
    }

    public final SpotLightEffectRenderer getMSpotLightRenderer() {
        return this.mSpotLightRenderer;
    }

    public final TouchMotionEffectRenderer getMTouchMotionEffectRenderer() {
        TouchMotionEffectRenderer touchMotionEffectRenderer = this.mTouchMotionEffectRenderer;
        if (touchMotionEffectRenderer != null) {
            return touchMotionEffectRenderer;
        }
        Intrinsics.throwUninitializedPropertyAccessException("mTouchMotionEffectRenderer");
        return null;
    }

    public final VibrationFeedbackHandler getMVibrationHandler() {
        return this.mVibrationHandler;
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void handleMotionEventDown(MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        super.handleMotionEventDown(event);
        PhysicsAnimationHandler physicsAnimationHandler = this.mPhysicsHandler;
        if (physicsAnimationHandler != null && getMState().getIsPhysicalEnabled()) {
            physicsAnimationHandler.initOrResetVelocityTracker();
            VelocityTracker velocityTracker = physicsAnimationHandler.getVelocityTracker();
            if (velocityTracker != null) {
                velocityTracker.addMovement(event);
            }
        }
        stopPhysicsMove();
        executeThumbScaleAnim(event);
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void handleMotionEventUp(MotionEvent event) {
        float fComputeFlingVelocityY;
        Intrinsics.checkNotNullParameter(event, "event");
        releaseThumbScaleAnim();
        getMFastMoveSpring().setEndValue(0.0d);
        if (getMState().getIsDragging()) {
            getMState().setDragging(false);
            setMStartDragging(false);
            PhysicsAnimationHandler physicsAnimationHandler = this.mPhysicsHandler;
            if (physicsAnimationHandler == null || !getMState().getIsPhysicalEnabled()) {
                fComputeFlingVelocityY = 0.0f;
            } else {
                fComputeFlingVelocityY = isVertical() ? physicsAnimationHandler.computeFlingVelocityY() : physicsAnimationHandler.computeFlingVelocity();
            }
            if (getMState().getIsPhysicalEnabled() && physicsAnimationHandler != null && physicsAnimationHandler.shouldFling(fComputeFlingVelocityY)) {
                flingBehaviorAfterEndDrag(fComputeFlingVelocityY);
            } else {
                float scale = getMState().getScale();
                if (0.0f <= scale && scale <= 1.0f) {
                    onStopTrackingTouch(true);
                }
                flingBehaviorAfterDeformationDrag();
            }
            setPressed(false);
        }
        PhysicsAnimationHandler physicsAnimationHandler2 = this.mPhysicsHandler;
        if (physicsAnimationHandler2 != null) {
            physicsAnimationHandler2.recycleVelocityTracker();
        }
    }

    public void initAdvancedFeatures(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        Intrinsics.checkNotNullParameter(context, "context");
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.COUISeekBar, defStyleAttr, defStyleRes);
        Intrinsics.checkNotNullExpressionValue(typedArrayObtainStyledAttributes, "obtainStyledAttributes(...)");
        try {
            getMState().setEnableVibrator(typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUISeekBar_couiSeekBarEnableVibrator, true));
            getMState().setEnableAdaptiveVibrator(typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUISeekBar_couiSeekBarAdaptiveVibrator, false));
            getMState().setPhysicalEnabled(typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUISeekBar_couiSeekBarPhysicsEnable, true));
            getMState().setStartFromMiddle(typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUISeekBar_couiSeekBarStartMiddle, false));
            typedArrayObtainStyledAttributes.recycle();
            initTouchMotionEffect();
            initPhysicsAnimation(context);
            initFlexibleFollowHand();
            initClickAnimation();
            initVibrationFeedback(context);
            initGlitterEffect();
            initSpotLightEffect(context);
        } catch (Throwable th) {
            typedArrayObtainStyledAttributes.recycle();
            throw th;
        }
    }

    public void initClickAnimation() {
        ClickAnimationHandler clickAnimationHandler = new ClickAnimationHandler();
        this.mClickAnimHandler = clickAnimationHandler;
        clickAnimationHandler.setState(getMState());
        ClickAnimationHandler clickAnimationHandler2 = this.mClickAnimHandler;
        if (clickAnimationHandler2 != null) {
            clickAnimationHandler2.setView(this);
        }
        ClickAnimationHandler clickAnimationHandler3 = this.mClickAnimHandler;
        if (clickAnimationHandler3 != null) {
            clickAnimationHandler3.initClickAnim();
        }
        ClickAnimationHandler clickAnimationHandler4 = this.mClickAnimHandler;
        if (clickAnimationHandler4 != null) {
            clickAnimationHandler4.setOnClickAnimUpdateListener(new AnonymousClass1());
        }
    }

    public void initDeformationEffect() {
        setMDeformationRenderer(new DeformationEffectRenderer());
        DeformationEffectRenderer mDeformationRenderer = getMDeformationRenderer();
        if (mDeformationRenderer != null) {
            mDeformationRenderer.setState(getMState());
        }
        DeformationEffectRenderer mDeformationRenderer2 = getMDeformationRenderer();
        if (mDeformationRenderer2 != null) {
            mDeformationRenderer2.initDeformationAnim();
        }
        addEffect(getMDeformationRenderer());
    }

    public void initFlexibleFollowHand() {
        FlexibleFollowHandHandler flexibleFollowHandHandler = new FlexibleFollowHandHandler();
        this.mFlexibleFollowHandHandler = flexibleFollowHandHandler;
        flexibleFollowHandHandler.setState(getMState());
        FlexibleFollowHandHandler flexibleFollowHandHandler2 = this.mFlexibleFollowHandHandler;
        if (flexibleFollowHandHandler2 != null) {
            flexibleFollowHandHandler2.setView(this);
        }
        FlexibleFollowHandHandler flexibleFollowHandHandler3 = this.mFlexibleFollowHandHandler;
        if (flexibleFollowHandHandler3 != null) {
            flexibleFollowHandHandler3.initFlexibleFollowHandAnim();
        }
    }

    public void initGlitterEffect() {
        GlitterEffectRenderer glitterEffectRenderer = new GlitterEffectRenderer();
        this.mGlitterRenderer = glitterEffectRenderer;
        addEffect(glitterEffectRenderer);
    }

    public void initPhysicsAnimation(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        PhysicsAnimationHandler physicsAnimationHandler = new PhysicsAnimationHandler();
        this.mPhysicsHandler = physicsAnimationHandler;
        physicsAnimationHandler.setOnFlingUpdateListener(new C13511());
        PhysicsAnimationHandler physicsAnimationHandler2 = this.mPhysicsHandler;
        if (physicsAnimationHandler2 != null) {
            physicsAnimationHandler2.setOnFlingEndListener(new AnonymousClass2());
        }
    }

    public void initSpotLightEffect(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        SpotLightEffectRenderer spotLightEffectRenderer = new SpotLightEffectRenderer(context);
        this.mSpotLightRenderer = spotLightEffectRenderer;
        addEffect(spotLightEffectRenderer);
    }

    public void initTouchMotionEffect() {
        Context context = getContext();
        Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
        setMTouchMotionEffectRenderer(new TouchMotionEffectRenderer(context, this));
        addEffect(getMTouchMotionEffectRenderer());
        getMState().setSupportDeformation(false);
    }

    public void initVibrationFeedback(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        VibrationFeedbackHandler vibrationFeedbackHandler = new VibrationFeedbackHandler(context);
        this.mVibrationHandler = vibrationFeedbackHandler;
        vibrationFeedbackHandler.setView(this);
        VibrationFeedbackHandler vibrationFeedbackHandler2 = this.mVibrationHandler;
        if (vibrationFeedbackHandler2 != null) {
            vibrationFeedbackHandler2.setEnableVibrator(getMState().getEnableVibrator());
        }
        VibrationFeedbackHandler vibrationFeedbackHandler3 = this.mVibrationHandler;
        if (vibrationFeedbackHandler3 != null) {
            vibrationFeedbackHandler3.setEnableAdaptiveVibrator(getMState().getEnableAdaptiveVibrator());
        }
        VibrationFeedbackHandler vibrationFeedbackHandler4 = this.mVibrationHandler;
        if (vibrationFeedbackHandler4 != null) {
            vibrationFeedbackHandler4.setMinMax(getMState().getMin(), getMState().getMax());
        }
    }

    public boolean isEnableAdaptiveVibrator() {
        return getMState().getEnableAdaptiveVibrator();
    }

    public boolean isEnableVibrator() {
        return getMState().getEnableVibrator();
    }

    public boolean isMoveFollowHand() {
        return getMState().getMoveType() != 2;
    }

    public boolean isPhysicalEnabled() {
        return getMState().getIsPhysicalEnabled();
    }

    public boolean isStartFromMiddle() {
        return getMState().getIsStartFromMiddle();
    }

    public boolean isSupportDeformation() {
        return getMState().getIsSupportDeformation();
    }

    public boolean isWithinThumbBounds(float x2, float y2) {
        float paddingVertical = isVertical() ? getMState().getPaddingVertical() : getMState().getPaddingHorizontal();
        if (paddingVertical <= 0.0f) {
            paddingVertical = RangesKt.coerceAtLeast(getMState().getCurThumbRadius(), getMState().getThumbOutRadius()) * 1.5f;
        }
        if (isVertical()) {
            int seekBarCenterX = getSeekBarCenterX();
            if (y2 >= getMState().getThumbPosition() - paddingVertical && y2 <= getMState().getThumbPosition() + paddingVertical) {
                float f3 = seekBarCenterX;
                if (x2 >= f3 - paddingVertical && x2 <= f3 + paddingVertical) {
                    return true;
                }
            }
            return false;
        }
        int seekBarCenterY = getSeekBarCenterY();
        if (x2 >= getMState().getThumbPosition() - paddingVertical && x2 <= getMState().getThumbPosition() + paddingVertical) {
            float f4 = seekBarCenterY;
            if (y2 >= f4 - paddingVertical && y2 <= f4 + paddingVertical) {
                return true;
            }
        }
        return false;
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore, android.widget.ProgressBar, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        SpotLightEffectRenderer spotLightEffectRenderer = this.mSpotLightRenderer;
        if (spotLightEffectRenderer != null) {
            spotLightEffectRenderer.onAttachedToWindow(this);
        }
        COUIMaterialStateManager.INSTANCE.addOnMaterialStateChangedListener(this);
    }

    public void onClickAnimationUpdate(float value) {
        if (getMPixPerProgress() <= 0.0f) {
            return;
        }
        float seekBarLength = getSeekBarLength();
        float fInteractionFloorScale = interactionFloorScale();
        int iEffectiveLowerBound = effectiveLowerBound();
        if (isVertical()) {
            float max = (getMState().getMax() - iEffectiveLowerBound) * getMPixPerProgress();
            if (fInteractionFloorScale > 0.0f) {
                value = RangesKt.coerceAtMost(value, max);
            }
            getMState().setProgress((int) (getMState().getMax() - (value / getMPixPerProgress())));
            getMState().setProgress(RangesKt.coerceIn(getMState().getProgress(), iEffectiveLowerBound, getMState().getMax()));
            getMState().setScale(seekBarLength > 0.0f ? 1.0f - (value / seekBarLength) : 0.0f);
        } else {
            float mPixPerProgress = iEffectiveLowerBound * getMPixPerProgress();
            if (fInteractionFloorScale > 0.0f) {
                value = RangesKt.coerceAtLeast(value, mPixPerProgress);
            }
            getMState().setProgress((int) (value / getMPixPerProgress()));
            getMState().setProgress(RangesKt.coerceIn(getMState().getProgress(), iEffectiveLowerBound, getMState().getMax()));
            getMState().setScale(seekBarLength > 0.0f ? (value - (getMState().getMin() * getMPixPerProgress())) / seekBarLength : 0.0f);
        }
        getMState().setScale(RangesKt.coerceIn(getMState().getScale(), fInteractionFloorScale, 1.0f));
        getMState().setDrawProgressScale(getMState().getScale());
        syncProgressToParent(getMState().getProgress());
        invalidate();
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore, android.widget.ProgressBar, android.view.View
    public void onDetachedFromWindow() {
        COUIMaterialStateManager.INSTANCE.removeOnMaterialStateChangedListener(this);
        SpotLightEffectRenderer spotLightEffectRenderer = this.mSpotLightRenderer;
        if (spotLightEffectRenderer != null) {
            spotLightEffectRenderer.onDetachedFromWindow();
        }
        stopPhysicsMove();
        getMFastMoveSpring().setAtRest();
        stopDragTransitionAnim();
        ClickAnimationHandler clickAnimationHandler = this.mClickAnimHandler;
        if (clickAnimationHandler != null) {
            clickAnimationHandler.stopClikAnim();
        }
        FlexibleFollowHandHandler flexibleFollowHandHandler = this.mFlexibleFollowHandHandler;
        if (flexibleFollowHandHandler != null) {
            flexibleFollowHandHandler.cancelFlexibleFollowHandAnim();
        }
        GlitterEffectRenderer glitterEffectRenderer = this.mGlitterRenderer;
        if (glitterEffectRenderer != null) {
            glitterEffectRenderer.cancelAnimation();
        }
        DeformationEffectRenderer mDeformationRenderer = getMDeformationRenderer();
        if (mDeformationRenderer != null) {
            mDeformationRenderer.cancelAll();
        }
        if (this.mTouchMotionEffectRenderer != null) {
            getMTouchMotionEffectRenderer().release();
        }
        VibrationFeedbackHandler vibrationFeedbackHandler = this.mVibrationHandler;
        if (vibrationFeedbackHandler != null) {
            vibrationFeedbackHandler.release();
        }
        super.onDetachedFromWindow();
    }

    public void onDragTransitionAnimUpdate(float value) {
        COUISeekBarCore.OnSeekBarChangeListener mOnSeekBarChangeListener;
        if (getMPixPerProgress() <= 0.0f) {
            return;
        }
        float seekBarLength = getSeekBarLength();
        if (seekBarLength <= 0.0f) {
            return;
        }
        SeekBarState state = getState();
        int progress = state.getProgress();
        float min = seekBarLength > 0.0f ? (value - (state.getMin() * getMPixPerProgress())) / seekBarLength : 0.0f;
        float fInteractionFloorScale = interactionFloorScale();
        state.setScale(RangesKt.coerceIn(min, fInteractionFloorScale > 0.0f ? fInteractionFloorScale : -1.0f, 2.0f));
        state.setDrawProgressScale(RangesKt.coerceIn(min, fInteractionFloorScale, 1.0f));
        state.setProgress(RangesKt.coerceIn((int) (value / getMPixPerProgress()), effectiveLowerBound(), state.getMax()));
        setMInternalProgress(state.getProgress());
        if (state.getIsSupportDeformation()) {
            calculateTouchDeformationValue();
            notifyDeformedListener();
        }
        syncProgressToParent(state.getProgress());
        if (progress != state.getProgress() && (mOnSeekBarChangeListener = getMOnSeekBarChangeListener()) != null) {
            mOnSeekBarChangeListener.onProgressChanged(this, state.getProgress(), true);
        }
        invalidate();
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public CharSequence onDump() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append("(spotlight=");
        SpotLightEffectRenderer spotLightEffectRenderer = this.mSpotLightRenderer;
        boolean z2 = false;
        if (spotLightEffectRenderer != null && spotLightEffectRenderer.getMEnabled()) {
            z2 = true;
        }
        sb.append(z2);
        sb.append(')');
        return sb.toString();
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore, android.view.View
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        PhysicsAnimationHandler physicsAnimationHandler = this.mPhysicsHandler;
        if (physicsAnimationHandler != null) {
            int seekBarLength = (int) getSeekBarLength();
            physicsAnimationHandler.setSeekBarWidth(seekBarLength);
            if (!getMState().getIsPhysicalEnabled() || seekBarLength <= 0) {
                return;
            }
            Context context = getContext();
            Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
            physicsAnimationHandler.initPhysicsAnimator(context);
        }
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public void onMaterialStateChange(int flag, boolean newState) {
        if (flag == 4) {
            SpotLightEffectRenderer spotLightEffectRenderer = this.mSpotLightRenderer;
            if (spotLightEffectRenderer != null) {
                spotLightEffectRenderer.setEnabled(newState);
            }
            onSpotLightEnabledChanged(newState);
        }
    }

    public void onPhysicsFlingEnd() {
        DeformationEffectRenderer mDeformationRenderer = getMDeformationRenderer();
        if (mDeformationRenderer != null) {
            mDeformationRenderer.resetDeformationValue();
        }
        COUISeekBarCore.OnSeekBarChangeListener mOnSeekBarChangeListener = getMOnSeekBarChangeListener();
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onStopTrackingTouch(this);
        }
        invalidate();
    }

    public void onPhysicsFlingUpdate(float value, float velocity) {
        float f3;
        COUISeekBarCore.OnSeekBarChangeListener mOnSeekBarChangeListener;
        float seekBarLength = getSeekBarLength();
        if (seekBarLength <= 0.0f) {
            return;
        }
        float scale = getMState().getScale();
        if (isVertical()) {
            f3 = 1.0f - (value / seekBarLength);
        } else {
            f3 = getMState().getIsRtl() ? (seekBarLength - value) / seekBarLength : value / seekBarLength;
        }
        float fInteractionFloorScale = interactionFloorScale();
        boolean z2 = fInteractionFloorScale > 0.0f && f3 < fInteractionFloorScale;
        if (z2) {
            f3 = fInteractionFloorScale;
        }
        setFlingScale(f3);
        executeFlingGlitterEffectAnim(getMState().getScale(), RangesKt.coerceAtMost(Math.abs(velocity), 8000.0f), scale);
        int progressLimit = getProgressLimit(MathKt.roundToInt((getMState().getMax() - getMState().getMin()) * getMState().getScale()) + getMState().getMin());
        int mInternalProgress = getMInternalProgress();
        int progress = getMState().getProgress();
        setMInternalProgress(progressLimit);
        getMState().setProgress(getRealProgress(progressLimit));
        syncProgressToParent(getMState().getProgress());
        if (mInternalProgress != getMInternalProgress()) {
            setMLastX(value + getStart());
            if (progress != getMState().getProgress() && (mOnSeekBarChangeListener = getMOnSeekBarChangeListener()) != null) {
                mOnSeekBarChangeListener.onProgressChanged(this, getMState().getProgress(), true);
            }
        }
        invalidate();
        if (z2) {
            stopPhysicsMove();
        }
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore, android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    public void onSizeChanged(int w2, int h3, int oldw, int oldh) {
        super.onSizeChanged(w2, h3, oldw, oldh);
        if (this.mTouchMotionEffectRenderer != null) {
            getMTouchMotionEffectRenderer().onViewSizeChanged(w2, h3);
        }
        SpotLightEffectRenderer spotLightEffectRenderer = this.mSpotLightRenderer;
        if (spotLightEffectRenderer != null) {
            spotLightEffectRenderer.onSizeChanged(w2, h3);
        }
    }

    public void onSpotLightEnabledChanged(boolean enabled) {
    }

    /* JADX WARN: Code duplicated, block: B:40:0x00a1  */
    /* JADX WARN: Code duplicated, block: B:42:0x00a8  */
    /* JADX WARN: Code duplicated, block: B:44:0x00ac  */
    /* JADX WARN: Code duplicated, block: B:48:0x00b8  */
    /* JADX WARN: Code duplicated, block: B:51:0x00c2  */
    /* JADX WARN: Code duplicated, block: B:54:0x00cb  */
    /* JADX WARN: Code duplicated, block: B:56:0x00d1  */
    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore, android.widget.AbsSeekBar, android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        ClickAnimationHandler clickAnimationHandler;
        VibrationFeedbackHandler vibrationFeedbackHandler;
        PhysicsAnimationHandler physicsAnimationHandler;
        VelocityTracker velocityTracker;
        Intrinsics.checkNotNullParameter(event, "event");
        if (!isEnabled()) {
            return super.onTouchEvent(event);
        }
        if (getMState().getIsPhysicalEnabled() && event.getAction() == 2 && (physicsAnimationHandler = this.mPhysicsHandler) != null && (velocityTracker = physicsAnimationHandler.getVelocityTracker()) != null) {
            velocityTracker.addMovement(event);
        }
        int action = event.getAction();
        if (action != 0) {
            if (action == 1) {
                if (useDragTransitionAnimation()) {
                    if (this.mIsDragTransitioning) {
                        snapScaleToFingerPosition(event);
                    }
                    stopDragTransitionAnim();
                    this.mNeedDragTransition = false;
                }
                clickAnimationHandler = this.mClickAnimHandler;
                if (clickAnimationHandler != null) {
                    clickAnimationHandler.stopClikAnim();
                }
                cancelFlexibleFollowHandAnim();
                vibrationFeedbackHandler = this.mVibrationHandler;
                if (vibrationFeedbackHandler != null) {
                    vibrationFeedbackHandler.clearPendingTasks();
                }
                if (!getMStartDragging()) {
                    if (shouldEnlargeOnDown()) {
                        releaseAnim();
                    }
                    handleMotionEventUp(event);
                    getMState().setDragging(false);
                    if (isEnabled() && isMoveFollowHand() && touchInSeekBar(event) && event.getAction() == 1) {
                        startClickTransitionAnim(event);
                    }
                    invalidate();
                    return true;
                }
            } else if (action == 2) {
                if (getMState().getIsDragging()) {
                    performVibrateFeedback();
                }
                if (!getMStartDragging() && Math.abs(event.getX() - getMTouchDownX()) > getMTouchSlop()) {
                    ClickAnimationHandler clickAnimationHandler2 = this.mClickAnimHandler;
                    if (clickAnimationHandler2 != null) {
                        clickAnimationHandler2.stopClikAnim();
                    }
                    stopPhysicsMove();
                    FlexibleFollowHandHandler flexibleFollowHandHandler = this.mFlexibleFollowHandHandler;
                    if (flexibleFollowHandHandler != null) {
                        flexibleFollowHandHandler.setStartValue(getMState().getMax() - getMState().getMin() > 0 ? getMState().progressToScale(getProgress()) : getMState().getScale());
                    }
                }
            } else if (action == 3) {
                if (useDragTransitionAnimation()) {
                    if (this.mIsDragTransitioning) {
                        snapScaleToFingerPosition(event);
                    }
                    stopDragTransitionAnim();
                    this.mNeedDragTransition = false;
                }
                clickAnimationHandler = this.mClickAnimHandler;
                if (clickAnimationHandler != null) {
                    clickAnimationHandler.stopClikAnim();
                }
                cancelFlexibleFollowHandAnim();
                vibrationFeedbackHandler = this.mVibrationHandler;
                if (vibrationFeedbackHandler != null) {
                    vibrationFeedbackHandler.clearPendingTasks();
                }
                if (!getMStartDragging()) {
                    if (shouldEnlargeOnDown()) {
                        releaseAnim();
                    }
                    handleMotionEventUp(event);
                    getMState().setDragging(false);
                    if (isEnabled()) {
                        startClickTransitionAnim(event);
                    }
                    invalidate();
                    return true;
                }
            }
        } else if (useDragTransitionAnimation()) {
            this.mNeedDragTransition = isMoveFollowHand();
            stopDragTransitionAnim();
        }
        return super.onTouchEvent(event);
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void performFeedback() {
        performVibrateFeedback();
    }

    public void performVibrateFeedback() {
        VibrationFeedbackHandler vibrationFeedbackHandler = this.mVibrationHandler;
        if (vibrationFeedbackHandler == null || !getMState().getEnableVibrator()) {
            return;
        }
        vibrationFeedbackHandler.setDragging(getMState().getIsDragging());
        vibrationFeedbackHandler.setMinMax(getMState().getMin(), getMState().getMax());
        vibrationFeedbackHandler.performFeedback(getMState().getProgress());
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void refreshPixPerProgress() {
        super.refreshPixPerProgress();
        ClickAnimationHandler clickAnimationHandler = this.mClickAnimHandler;
        if (clickAnimationHandler != null) {
            clickAnimationHandler.setPixPerProgress(getMPixPerProgress());
        }
    }

    public void releaseThumbScaleAnim() {
        IAnimationHandler mAnimationHandler;
        if (!getMState().getShowThumb() || getMAnimationHandler() == null) {
            return;
        }
        SeekBarState mState = getMState();
        if (mState.getCurThumbRadius() == mState.getThumbRadius() || (mAnimationHandler = getMAnimationHandler()) == null) {
            return;
        }
        mAnimationHandler.startThumbScaleAnimation(false);
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void resetDeformationValue() {
        DeformationEffectRenderer mDeformationRenderer;
        if (!getMState().getIsSupportDeformation() || (mDeformationRenderer = getMDeformationRenderer()) == null) {
            return;
        }
        mDeformationRenderer.resetDeformationValue();
    }

    public void setDeformedParams(DeformedValueBean bean) {
        DeformationEffectRenderer mDeformationRenderer = getMDeformationRenderer();
        if (mDeformationRenderer != null) {
            mDeformationRenderer.setDeformedParams(bean);
        }
    }

    public void setEnableAdaptiveVibrator(boolean enable) {
        getMState().setEnableAdaptiveVibrator(enable);
        VibrationFeedbackHandler vibrationFeedbackHandler = this.mVibrationHandler;
        if (vibrationFeedbackHandler != null) {
            vibrationFeedbackHandler.setEnableAdaptiveVibrator(enable);
        }
    }

    public void setEnableVibrator(boolean enable) {
        getMState().setEnableVibrator(enable);
        VibrationFeedbackHandler vibrationFeedbackHandler = this.mVibrationHandler;
        if (vibrationFeedbackHandler != null) {
            vibrationFeedbackHandler.setEnableVibrator(enable);
        }
    }

    public void setFlingLinearDamping(float linearDamping) {
        PhysicsAnimationHandler physicsAnimationHandler;
        if (!getMState().getIsPhysicalEnabled() || (physicsAnimationHandler = this.mPhysicsHandler) == null || physicsAnimationHandler == null) {
            return;
        }
        physicsAnimationHandler.setFlingLinearDamping(linearDamping);
    }

    public void setFlingProperty(float frequency, float dampingRatio) {
        PhysicsAnimationHandler physicsAnimationHandler;
        if (!getMState().getIsPhysicalEnabled() || (physicsAnimationHandler = this.mPhysicsHandler) == null) {
            return;
        }
        if (physicsAnimationHandler != null) {
            physicsAnimationHandler.setFlingFrequency(frequency);
        }
        PhysicsAnimationHandler physicsAnimationHandler2 = this.mPhysicsHandler;
        if (physicsAnimationHandler2 != null) {
            physicsAnimationHandler2.setFlingDampingRatio(dampingRatio);
        }
    }

    public final void setMClickAnimHandler(ClickAnimationHandler clickAnimationHandler) {
        this.mClickAnimHandler = clickAnimationHandler;
    }

    public final void setMFastMoveScaleOffsetX(float f3) {
        this.mFastMoveScaleOffsetX = f3;
    }

    public final void setMFlexibleFollowHandHandler(FlexibleFollowHandHandler flexibleFollowHandHandler) {
        this.mFlexibleFollowHandHandler = flexibleFollowHandHandler;
    }

    public final void setMGlitterRenderer(GlitterEffectRenderer glitterEffectRenderer) {
        this.mGlitterRenderer = glitterEffectRenderer;
    }

    public final void setMPhysicsHandler(PhysicsAnimationHandler physicsAnimationHandler) {
        this.mPhysicsHandler = physicsAnimationHandler;
    }

    public final void setMSpotLightRenderer(SpotLightEffectRenderer spotLightEffectRenderer) {
        this.mSpotLightRenderer = spotLightEffectRenderer;
    }

    public final void setMTouchMotionEffectRenderer(TouchMotionEffectRenderer touchMotionEffectRenderer) {
        Intrinsics.checkNotNullParameter(touchMotionEffectRenderer, "<set-?>");
        this.mTouchMotionEffectRenderer = touchMotionEffectRenderer;
    }

    public final void setMVibrationHandler(VibrationFeedbackHandler vibrationFeedbackHandler) {
        this.mVibrationHandler = vibrationFeedbackHandler;
    }

    public void setMaxHeightDeformed(float pxMaxHeightDeformedValue) {
        DeformationEffectRenderer mDeformationRenderer = getMDeformationRenderer();
        DeformationEffectParams deformationParams = mDeformationRenderer != null ? mDeformationRenderer.getMParams() : null;
        if (deformationParams != null) {
            deformationParams.setMaxHeightDeformedValue(pxMaxHeightDeformedValue);
        }
    }

    public void setMaxMovingDistance(int pxMovingDistance) {
        DeformationEffectRenderer mDeformationRenderer = getMDeformationRenderer();
        DeformationEffectParams deformationParams = mDeformationRenderer != null ? mDeformationRenderer.getMParams() : null;
        if (deformationParams != null) {
            deformationParams.setMaxMovingDistance(pxMovingDistance);
        }
    }

    public void setMaxWidthDeformed(float pxMaxWidthDeformedValue) {
        DeformationEffectRenderer mDeformationRenderer = getMDeformationRenderer();
        DeformationEffectParams deformationParams = mDeformationRenderer != null ? mDeformationRenderer.getMParams() : null;
        if (deformationParams != null) {
            deformationParams.setMaxWidthDeformedValue(pxMaxWidthDeformedValue);
        }
    }

    public void setPhysicalEnabled(boolean enable) {
        getMState().setPhysicalEnabled(enable);
    }

    public void setStartFromMiddle(boolean startFromMiddle) {
        getMState().setStartFromMiddle(startFromMiddle);
        invalidate();
    }

    public void setSupportDeformation(boolean supportDeformation) {
        getMState().setSupportDeformation(supportDeformation);
    }

    public void startClickTransitionAnim(MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        float touchPrimaryCoordinate = getTouchPrimaryCoordinate(event);
        float seekBarLength = getSeekBarLength();
        if (seekBarLength <= 0.0f) {
            return;
        }
        float fCoerceIn = RangesKt.coerceIn(isVertical() ? ((getProgressStart() + seekBarLength) - touchPrimaryCoordinate) / seekBarLength : primaryToHorizontalAbsoluteScale(touchPrimaryCoordinate, seekBarLength), interactionFloorScale(), 1.0f);
        int max = getMState().getMax() - getMState().getMin();
        Ref.IntRef intRef = new Ref.IntRef();
        float f3 = max;
        int iRoundToInt = MathKt.roundToInt((fCoerceIn * f3) + getMState().getMin());
        intRef.element = iRoundToInt;
        int iCoerceIn = RangesKt.coerceIn(iRoundToInt, effectiveLowerBound(), getMState().getMax());
        intRef.element = iCoerceIn;
        float min = (iCoerceIn - getMState().getMin()) / f3;
        float scale = getMState().getScale();
        ClickAnimationHandler clickAnimationHandler = this.mClickAnimHandler;
        if (clickAnimationHandler == null || clickAnimationHandler.getMClickAnim() == null || getMPixPerProgress() <= 0.0f) {
            getMState().setScale(min);
            getMState().setDrawProgressScale(min);
            getMState().setProgress(intRef.element);
            syncProgressToParent(intRef.element);
            COUISeekBarCore.OnSeekBarChangeListener mOnSeekBarChangeListener = getMOnSeekBarChangeListener();
            if (mOnSeekBarChangeListener != null) {
                mOnSeekBarChangeListener.onProgressChanged(this, intRef.element, true);
                return;
            }
            return;
        }
        float min2 = (scale * f3) + getMState().getMin();
        c cVar = new c(this, intRef);
        clickAnimationHandler.stopClikAnim();
        clickAnimationHandler.addEndListener(cVar);
        startTrackingTouch(true, true);
        float max2 = isVertical() ? (getMState().getMax() - min2) * getMPixPerProgress() : getMPixPerProgress() * min2;
        float max3 = (isVertical() ? getMState().getMax() - intRef.element : intRef.element) * getMPixPerProgress();
        COUISpringAnimation clickAnim = clickAnimationHandler.getMClickAnim();
        if (clickAnim != null) {
            clickAnim.setStartValue(max2);
        }
        clickAnimationHandler.startClickAnim(max3);
    }

    public void startDeformationRelease() {
        DeformationEffectRenderer mDeformationRenderer;
        if (!getMState().getIsSupportDeformation() || (mDeformationRenderer = getMDeformationRenderer()) == null) {
            return;
        }
        mDeformationRenderer.startDeformationRelease();
    }

    public void startFling(float velocity) {
        PhysicsAnimationHandler physicsAnimationHandler = this.mPhysicsHandler;
        if (physicsAnimationHandler == null || !getMState().getIsPhysicalEnabled()) {
            return;
        }
        int seekBarWidth = getSeekBarWidth();
        float deformationFlingScale = getMState().getIsSupportDeformation() ? getDeformationFlingScale() : getMState().getScale();
        physicsAnimationHandler.startFling(getMState().getIsRtl() ? (1 - deformationFlingScale) * seekBarWidth : seekBarWidth * deformationFlingScale, velocity);
    }

    public void stopAutoProgressAnimations() {
        stopPhysicsMove();
        stopClikAnim();
        stopDragTransitionAnim();
    }

    public void stopClikAnim() {
        ClickAnimationHandler clickAnimationHandler = this.mClickAnimHandler;
        if (clickAnimationHandler == null || !clickAnimationHandler.isRunning()) {
            return;
        }
        clickAnimationHandler.stopClikAnim();
    }

    public final void stopDragTransitionAnim() {
        COUISpringAnimation cOUISpringAnimation = this.mDragTransitionAnim;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            cOUISpringAnimation.cancel();
        }
        this.mIsDragTransitioning = false;
    }

    public void stopPhysicsMove() {
        PhysicsAnimationHandler physicsAnimationHandler = this.mPhysicsHandler;
        if (physicsAnimationHandler != null) {
            physicsAnimationHandler.stopPhysicsMove();
        }
    }

    public boolean touchInSeekBar(MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        float x2 = event.getX();
        float y2 = event.getY();
        return x2 >= 0.0f && x2 <= ((float) getWidth()) && y2 >= ((float) getPaddingTop()) && y2 <= ((float) (getHeight() - getPaddingBottom()));
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void trackTouchEvent(MotionEvent event) {
        VelocityTracker velocityTracker;
        COUISpringAnimation cOUISpringAnimation;
        Intrinsics.checkNotNullParameter(event, "event");
        if (useDragTransitionAnimation() && isMoveFollowHand()) {
            if (this.mIsDragTransitioning) {
                float targetPixelFromEvent = getTargetPixelFromEvent(event);
                if (targetPixelFromEvent == -1.0f || (cOUISpringAnimation = this.mDragTransitionAnim) == null) {
                    return;
                }
                cOUISpringAnimation.animateToFinalPosition(targetPixelFromEvent);
                return;
            }
            if (this.mNeedDragTransition) {
                this.mNeedDragTransition = false;
                SeekBarState state = getState();
                int max = state.getMax() - state.getMin();
                if (max > 0 && getMPixPerProgress() > 0.0f) {
                    float drawProgressScale = ((state.getDrawProgressScale() * max) + state.getMin()) * getMPixPerProgress();
                    float fCalculateDragTargetPixel = calculateDragTargetPixel(event);
                    if (fCalculateDragTargetPixel >= 0.0f && Math.abs(fCalculateDragTargetPixel - drawProgressScale) > getMPixPerProgress()) {
                        startDragTransitionAnim(drawProgressScale, fCalculateDragTargetPixel);
                        return;
                    }
                }
            }
        }
        super.trackTouchEvent(event);
        executeTouchGlitterEffectAnim();
        FlexibleFollowHandHandler flexibleFollowHandHandler = this.mFlexibleFollowHandHandler;
        if (flexibleFollowHandHandler != null) {
            flexibleFollowHandHandler.updateFlexibleFollowHand(getMState().getScale());
        }
        PhysicsAnimationHandler physicsAnimationHandler = this.mPhysicsHandler;
        if (physicsAnimationHandler == null || (velocityTracker = physicsAnimationHandler.getVelocityTracker()) == null) {
            return;
        }
        velocityTracker.computeCurrentVelocity(100);
        startFastMoveAnimation(isVertical() ? velocityTracker.getYVelocity() : velocityTracker.getXVelocity());
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void trackTouchEventByFinger(MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        super.trackTouchEventByFinger(event);
        executeTouchGlitterEffectAnim();
        FlexibleFollowHandHandler flexibleFollowHandHandler = this.mFlexibleFollowHandHandler;
        if (flexibleFollowHandHandler != null) {
            flexibleFollowHandHandler.updateFlexibleFollowHand(getMState().getScale());
        }
    }

    public boolean useDragTransitionAnimation() {
        return false;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUISeekBarAdvanced(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUISeekBarAdvanced(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0, 8, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public /* synthetic */ COUISeekBarAdvanced(Context context, AttributeSet attributeSet, int i3, int i4, int i5, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i5 & 2) != 0 ? null : attributeSet, (i5 & 4) != 0 ? R.attr.couiSeekBarStyle : i3, (i5 & 8) != 0 ? R.style.COUISeekBar : i4);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUISeekBarAdvanced(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        Intrinsics.checkNotNullParameter(context, "context");
        SpringConfig springConfigFromOrigamiTensionAndFriction = SpringConfig.fromOrigamiTensionAndFriction(500.0d, DEFAULT_SPRING_FRICTION);
        Intrinsics.checkNotNullExpressionValue(springConfigFromOrigamiTensionAndFriction, "fromOrigamiTensionAndFriction(...)");
        this.mFastMoveSpringConfig = springConfigFromOrigamiTensionAndFriction;
        this.mFastMoveSpring = LazyKt.lazy(new COUISeekBarAdvanced$mFastMoveSpring$2(this));
        initAdvancedFeatures(context, attributeSet, i3, i4);
    }
}