package com.coui.appcompat.seekbar.animation;

import android.content.Context;
import android.view.VelocityTracker;
import com.coui.appcompat.log.COUILog;
import com.oplus.physicsengine.common.Compat;
import com.oplus.physicsengine.common.Vector;
import com.oplus.physicsengine.engine.AnimationListener;
import com.oplus.physicsengine.engine.AnimationUpdateListener;
import com.oplus.physicsengine.engine.BaseBehavior;
import com.oplus.physicsengine.engine.FlingBehavior;
import com.oplus.physicsengine.engine.FloatValueHolder;
import com.oplus.physicsengine.engine.PhysicalAnimator;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0018\u0018\u0000 D2\u00020\u00012\u00020\u0002:\u0003DEFB\u0005¢\u0006\u0002\u0010\u0003J\u0006\u0010$\u001a\u00020\u0005J\u0006\u0010%\u001a\u00020\u0005J\u0010\u0010&\u001a\u00020\u00052\u0006\u0010'\u001a\u00020\nH\u0002J\u0006\u0010(\u001a\u00020)J\u000e\u0010*\u001a\u00020)2\u0006\u0010+\u001a\u00020,J\u0010\u0010-\u001a\u00020)2\u0006\u0010.\u001a\u00020/H\u0016J\u0010\u00100\u001a\u00020)2\u0006\u0010.\u001a\u00020/H\u0016J\u0010\u00101\u001a\u00020)2\u0006\u0010.\u001a\u00020/H\u0016J\u0010\u00102\u001a\u00020)2\u0006\u00103\u001a\u00020/H\u0016J\u0006\u00104\u001a\u00020)J\u000e\u00105\u001a\u00020)2\u0006\u00106\u001a\u00020\u0005J\u000e\u00107\u001a\u00020)2\u0006\u00108\u001a\u00020\u0005J\u000e\u00109\u001a\u00020)2\u0006\u0010:\u001a\u00020\u0005J\u0010\u0010;\u001a\u00020)2\b\u0010<\u001a\u0004\u0018\u00010\u0010J\u0010\u0010=\u001a\u00020)2\b\u0010<\u001a\u0004\u0018\u00010\u0014J\u000e\u0010>\u001a\u00020\n2\u0006\u0010?\u001a\u00020\u0005J\u000e\u0010@\u001a\u00020)2\u0006\u0010A\u001a\u00020\u0005J\u0016\u0010@\u001a\u00020)2\u0006\u0010A\u001a\u00020\u00052\u0006\u0010?\u001a\u00020\u0005J\u0006\u0010B\u001a\u00020)J\u0006\u0010C\u001a\u00020)R\u001e\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n8F¢\u0006\u0006\u001a\u0004\b\t\u0010\u000bR\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R$\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u0019\u001a\u00020\u001a@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\"\u0010!\u001a\u0004\u0018\u00010 2\b\u0010\u0004\u001a\u0004\u0018\u00010 @BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010#¨\u0006G"}, d2 = {"Lcom/coui/appcompat/seekbar/animation/PhysicsAnimationHandler;", "Lcom/oplus/physicsengine/engine/AnimationListener;", "Lcom/oplus/physicsengine/engine/AnimationUpdateListener;", "()V", "<set-?>", "", "flingVelocity", "getFlingVelocity", "()F", "isRunning", "", "()Z", "mFlingBehavior", "Lcom/oplus/physicsengine/engine/FlingBehavior;", "mFlingDampingRatio", "mFlingEndListener", "Lcom/coui/appcompat/seekbar/animation/PhysicsAnimationHandler$OnFlingEndListener;", "mFlingFrequency", "mFlingLinearDamping", "mFlingUpdateListener", "Lcom/coui/appcompat/seekbar/animation/PhysicsAnimationHandler$OnFlingUpdateListener;", "mFlingValueHolder", "Lcom/oplus/physicsengine/engine/FloatValueHolder;", "mPhysicalAnimator", "Lcom/oplus/physicsengine/engine/PhysicalAnimator;", "value", "", "seekBarWidth", "getSeekBarWidth", "()I", "setSeekBarWidth", "(I)V", "Landroid/view/VelocityTracker;", "velocityTracker", "getVelocityTracker", "()Landroid/view/VelocityTracker;", "computeFlingVelocity", "computeFlingVelocityY", "getFlingVelocityInternal", "vertical", "initOrResetVelocityTracker", "", "initPhysicsAnimator", "context", "Landroid/content/Context;", "onAnimationCancel", "behavior", "Lcom/oplus/physicsengine/engine/BaseBehavior;", "onAnimationEnd", "onAnimationStart", "onAnimationUpdate", "baseBehavior", "recycleVelocityTracker", "setFlingDampingRatio", "ratio", "setFlingFrequency", "frequency", "setFlingLinearDamping", "damping", "setOnFlingEndListener", "listener", "setOnFlingUpdateListener", "shouldFling", "velocity", "startFling", "startValue", "stopPhysicsMove", "updateBehavior", "Companion", "OnFlingEndListener", "OnFlingUpdateListener", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class PhysicsAnimationHandler implements AnimationListener, AnimationUpdateListener {
    private static final float DEFAULT_FLING_FREQUENCY = 2.8f;
    private static final float DEFAULT_FLING_LINEAR_DAMPING = 15.0f;
    public static final int MAX_VELOCITY = 8000;
    public static final int ONE_SECOND_UNITS = 1000;
    public static final int PHYSICAL_VELOCITY_LIMIT = 100;
    private static final String TAG = "PhysicsAnimationHandler";
    public static final int VELOCITY_COMPUTE_TIME = 100;
    private float flingVelocity;
    private FlingBehavior mFlingBehavior;
    private OnFlingEndListener mFlingEndListener;
    private OnFlingUpdateListener mFlingUpdateListener;
    private FloatValueHolder mFlingValueHolder;
    private PhysicalAnimator mPhysicalAnimator;
    private int seekBarWidth;
    private VelocityTracker velocityTracker;
    private float mFlingFrequency = 2.8f;
    private float mFlingDampingRatio = 1.0f;
    private float mFlingLinearDamping = 15.0f;

    @Metadata(d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&¨\u0006\u0004"}, d2 = {"Lcom/coui/appcompat/seekbar/animation/PhysicsAnimationHandler$OnFlingEndListener;", "", "onFlingEnd", "", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public interface OnFlingEndListener {
        void onFlingEnd();
    }

    @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H&¨\u0006\u0007"}, d2 = {"Lcom/coui/appcompat/seekbar/animation/PhysicsAnimationHandler$OnFlingUpdateListener;", "", "onFlingUpdate", "", "value", "", "velocity", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public interface OnFlingUpdateListener {
        void onFlingUpdate(float value, float velocity);
    }

    private final float getFlingVelocityInternal(boolean vertical) {
        VelocityTracker velocityTracker = this.velocityTracker;
        if (velocityTracker == null) {
            return 0.0f;
        }
        velocityTracker.computeCurrentVelocity(1000, 8000.0f);
        float yVelocity = vertical ? velocityTracker.getYVelocity() : velocityTracker.getXVelocity();
        if (Math.abs(yVelocity) < 100.0f) {
            return 0.0f;
        }
        return yVelocity;
    }

    public final float computeFlingVelocity() {
        return getFlingVelocityInternal(false);
    }

    public final float computeFlingVelocityY() {
        return getFlingVelocityInternal(true);
    }

    public final float getFlingVelocity() {
        return this.flingVelocity;
    }

    public final int getSeekBarWidth() {
        return this.seekBarWidth;
    }

    public final VelocityTracker getVelocityTracker() {
        return this.velocityTracker;
    }

    public final void initOrResetVelocityTracker() {
        VelocityTracker velocityTracker = this.velocityTracker;
        if (velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        } else if (velocityTracker != null) {
            velocityTracker.clear();
        }
    }

    public final void initPhysicsAnimator(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        if (this.mPhysicalAnimator != null) {
            return;
        }
        this.mPhysicalAnimator = PhysicalAnimator.create(context);
        this.mFlingValueHolder = new FloatValueHolder(0.0f);
        COUILog.i(TAG, "initPhysicsAnimator: setActiveFrame=" + this.seekBarWidth);
        FlingBehavior flingBehavior = (FlingBehavior) ((FlingBehavior) ((FlingBehavior) new FlingBehavior(4, 0.0f, (float) this.seekBarWidth).withProperty(this.mFlingValueHolder)).setSpringProperty(this.mFlingFrequency, this.mFlingDampingRatio)).applyTo(null);
        this.mFlingBehavior = flingBehavior;
        if (flingBehavior != null) {
            flingBehavior.setLinearDamping(this.mFlingLinearDamping);
        }
        PhysicalAnimator physicalAnimator = this.mPhysicalAnimator;
        if (physicalAnimator != null) {
        }
        PhysicalAnimator physicalAnimator2 = this.mPhysicalAnimator;
        if (physicalAnimator2 != null) {
            physicalAnimator2.addAnimationListener(this.mFlingBehavior, this);
        }
        PhysicalAnimator physicalAnimator3 = this.mPhysicalAnimator;
        if (physicalAnimator3 != null) {
            physicalAnimator3.addAnimationUpdateListener(this.mFlingBehavior, this);
        }
    }

    public final boolean isRunning() {
        PhysicalAnimator physicalAnimator = this.mPhysicalAnimator;
        return physicalAnimator != null && physicalAnimator.isAnimatorRunning();
    }

    @Override // com.oplus.physicsengine.engine.AnimationListener
    public void onAnimationCancel(BaseBehavior behavior) {
        Intrinsics.checkNotNullParameter(behavior, "behavior");
        COUILog.i(TAG, "onAnimationCancel");
    }

    @Override // com.oplus.physicsengine.engine.AnimationListener
    public void onAnimationEnd(BaseBehavior behavior) {
        Intrinsics.checkNotNullParameter(behavior, "behavior");
        COUILog.i(TAG, "onAnimationEnd");
        OnFlingEndListener onFlingEndListener = this.mFlingEndListener;
        if (onFlingEndListener != null) {
            onFlingEndListener.onFlingEnd();
        }
    }

    @Override // com.oplus.physicsengine.engine.AnimationListener
    public void onAnimationStart(BaseBehavior behavior) {
        Intrinsics.checkNotNullParameter(behavior, "behavior");
        COUILog.i(TAG, "onAnimationStart");
    }

    @Override // com.oplus.physicsengine.engine.AnimationUpdateListener
    public void onAnimationUpdate(BaseBehavior baseBehavior) {
        Intrinsics.checkNotNullParameter(baseBehavior, "baseBehavior");
        Object animatedValue = baseBehavior.getAnimatedValue();
        if (animatedValue == null) {
            return;
        }
        float fFloatValue = ((Float) animatedValue).floatValue();
        Vector propertyBodyVelocity = baseBehavior.getPropertyBodyVelocity();
        float fPhysicalSizeToPixels = propertyBodyVelocity != null ? Compat.physicalSizeToPixels(propertyBodyVelocity.mX) : 0.0f;
        OnFlingUpdateListener onFlingUpdateListener = this.mFlingUpdateListener;
        if (onFlingUpdateListener != null) {
            onFlingUpdateListener.onFlingUpdate(fFloatValue, fPhysicalSizeToPixels);
        }
    }

    public final void recycleVelocityTracker() {
        VelocityTracker velocityTracker = this.velocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
        }
        this.velocityTracker = null;
    }

    public final void setFlingDampingRatio(float ratio) {
        FlingBehavior flingBehavior;
        this.mFlingDampingRatio = ratio;
        if (this.mPhysicalAnimator == null || (flingBehavior = this.mFlingBehavior) == null || flingBehavior == null) {
            return;
        }
    }

    public final void setFlingFrequency(float frequency) {
        FlingBehavior flingBehavior;
        this.mFlingFrequency = frequency;
        if (this.mPhysicalAnimator == null || (flingBehavior = this.mFlingBehavior) == null || flingBehavior == null) {
            return;
        }
    }

    public final void setFlingLinearDamping(float damping) {
        FlingBehavior flingBehavior;
        this.mFlingLinearDamping = damping;
        if (this.mPhysicalAnimator == null || (flingBehavior = this.mFlingBehavior) == null || flingBehavior == null) {
            return;
        }
        flingBehavior.setLinearDamping(damping);
    }

    public final void setOnFlingEndListener(OnFlingEndListener listener) {
        this.mFlingEndListener = listener;
    }

    public final void setOnFlingUpdateListener(OnFlingUpdateListener listener) {
        this.mFlingUpdateListener = listener;
    }

    public final void setSeekBarWidth(int i3) {
        this.seekBarWidth = i3;
        updateBehavior();
    }

    public final boolean shouldFling(float velocity) {
        return Math.abs(velocity) >= 100.0f;
    }

    public final void startFling(float startValue, float velocity) {
        PhysicalAnimator physicalAnimator = this.mPhysicalAnimator;
        FlingBehavior flingBehavior = this.mFlingBehavior;
        FloatValueHolder floatValueHolder = this.mFlingValueHolder;
        if (physicalAnimator == null || flingBehavior == null || floatValueHolder == null) {
            COUILog.i(TAG, "startFling: animator not initialized");
            return;
        }
        this.flingVelocity = velocity;
        floatValueHolder.setStartValue(startValue);
        flingBehavior.start(velocity);
        COUILog.i(TAG, "startFling: startValue=" + startValue + ", velocity=" + velocity);
    }

    public final void stopPhysicsMove() {
        PhysicalAnimator physicalAnimator = this.mPhysicalAnimator;
        FlingBehavior flingBehavior = this.mFlingBehavior;
        if (physicalAnimator == null || flingBehavior == null) {
            return;
        }
        flingBehavior.stop();
        COUILog.i(TAG, "stopPhysicsMove: stopped");
    }

    public final void updateBehavior() {
        int i3;
        FlingBehavior flingBehavior = this.mFlingBehavior;
        if (flingBehavior == null || (i3 = this.seekBarWidth) <= 0) {
            return;
        }
        flingBehavior.setActiveFrame(0.0f, i3);
        COUILog.i(TAG, "updateBehavior: setActiveFrame=" + this.seekBarWidth);
    }

    public final void startFling(float startValue) {
        PhysicalAnimator physicalAnimator = this.mPhysicalAnimator;
        FlingBehavior flingBehavior = this.mFlingBehavior;
        FloatValueHolder floatValueHolder = this.mFlingValueHolder;
        if (physicalAnimator == null || flingBehavior == null || floatValueHolder == null) {
            COUILog.i(TAG, "startFling: animator not initialized");
            return;
        }
        this.flingVelocity = 0.0f;
        floatValueHolder.setStartValue(startValue);
        flingBehavior.start();
        COUILog.i(TAG, "startFling: startValue=" + startValue + ", velocity=0");
    }
}