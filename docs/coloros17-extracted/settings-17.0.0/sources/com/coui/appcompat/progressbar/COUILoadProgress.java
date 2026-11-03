package com.coui.appcompat.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityManagerCompat;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.oplus.settings.romupdate.RomUpdateListParser;
import com.support.progressbar.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUILoadProgress extends AppCompatButton {
    public static final int DEFAULT_UP_OR_DOWN = 0;
    public static final int INSTALL_HAVE_GIFT = 4;
    private static final float ONE = 1.0f;
    private static final float ONE_MILLION = 1.0E-7f;
    private static final float PROGRESS_ANIM_DURATION = 1.0f;
    private static final int TIMEOUT_SEND_ACCESSIBILITY_EVENT = 10;
    public static final int UPING_OR_DOWNING = 1;
    public static final int UP_OR_DOWN_FAIL = 3;
    public static final int UP_OR_DOWN_WAIT = 2;
    private final boolean DEBUG;
    private final String TAG;
    private AccessibilityEventSender mAccessibilityEventSender;
    private boolean mBroadcasting;
    protected Drawable mButtonDrawable;
    protected Drawable mButtonDrawableReverseColor;
    private int mButtonResource;
    private Context mContext;
    protected boolean mIsUpdateWithAnimation;
    private final AccessibilityManager mManager;
    public int mMax;
    private OnStateChangeListener mOnStateChangeListener;
    private OnStateChangeListener mOnStateChangeWidgetListener;
    public int mProgress;
    public int mState;
    protected float mVisualProgress;
    private OnProgressAnimationUpdateListener mVisualProgressAnimationListener;
    private final FloatPropertyCompat<Float> mVisualProgressProperty;
    private COUISpringAnimation mVisualProgressSpringAnimation;
    private static final int[] DEFAULT_SET = {R.attr.coui_state_default};
    private static final int[] WAIT_SET = {R.attr.coui_state_wait};
    private static final int[] FAIL_SET = {R.attr.coui_state_fail};
    private static final int[] ING_SET = {R.attr.coui_state_ing};

    /* JADX INFO: renamed from: com.coui.appcompat.progressbar.COUILoadProgress$1, reason: invalid class name */
    public class AnonymousClass1 extends FloatPropertyCompat<Float> {
        public AnonymousClass1(String str) {
            super(str);
        }

        /* JADX INFO: renamed from: getValue, reason: avoid collision after fix types in other method */
        public float getValue2(Float f3) {
            return 0.0f;
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public /* bridge */ /* synthetic */ void setValue(Float f3, float f4) {
            setValue2(f3, f4);
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public /* bridge */ /* synthetic */ float getValue(Float f3) {
            return getValue2(f3);
        }

        /* JADX INFO: renamed from: setValue, reason: avoid collision after fix types in other method */
        public void setValue2(Float f3, float f4) {
            COUILoadProgress cOUILoadProgress = COUILoadProgress.this;
            cOUILoadProgress.mVisualProgress = f4;
            if (COUILoadProgress.access$000(cOUILoadProgress) != null) {
                OnProgressAnimationUpdateListener onProgressAnimationUpdateListenerAccess$000 = COUILoadProgress.access$000(COUILoadProgress.this);
                COUILoadProgress cOUILoadProgress2 = COUILoadProgress.this;
                onProgressAnimationUpdateListenerAccess$000.onAnimationUpdate(cOUILoadProgress2.mVisualProgress, cOUILoadProgress2.mState);
            }
            COUILoadProgress.this.invalidate();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.progressbar.COUILoadProgress$2, reason: invalid class name */
    public class AnonymousClass2 implements COUIDynamicAnimation.OnAnimationEndListener {
        public AnonymousClass2() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
        public void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
            COUILoadProgress.this.mIsUpdateWithAnimation = false;
        }
    }

    public class AccessibilityEventSender implements Runnable {
        private AccessibilityEventSender() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUILoadProgress.this.sendAccessibilityEvent(4);
        }

        public /* synthetic */ AccessibilityEventSender(COUILoadProgress cOUILoadProgress, AnonymousClass1 anonymousClass1) {
            this();
        }
    }

    public interface OnProgressAnimationUpdateListener {
        void onAnimationUpdate(float f3, int i3);
    }

    public interface OnStateChangeListener {
        void onStateChanged(COUILoadProgress cOUILoadProgress, int i3);
    }

    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new AnonymousClass1();
        int mProgress;
        int mState;

        /* JADX INFO: renamed from: com.coui.appcompat.progressbar.COUILoadProgress$SavedState$1, reason: invalid class name */
        public class AnonymousClass1 implements Parcelable.Creator<SavedState> {
            @Override // android.os.Parcelable.Creator
            public /* bridge */ /* synthetic */ SavedState createFromParcel(Parcel parcel) {
                return createFromParcel(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public /* bridge */ /* synthetic */ SavedState[] newArray(int i3) {
                return newArray(i3);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int i3) {
                return new SavedState[i3];
            }
        }

        public /* synthetic */ SavedState(Parcel parcel, AnonymousClass1 anonymousClass1) {
            this(parcel);
        }

        public String toString() {
            return "CompoundButton.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " mState = " + this.mState + " mProgress = " + this.mProgress + "}";
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i3) {
            super.writeToParcel(parcel, i3);
            parcel.writeValue(Integer.valueOf(this.mState));
            parcel.writeValue(Integer.valueOf(this.mProgress));
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.mState = ((Integer) parcel.readValue(null)).intValue();
            this.mProgress = ((Integer) parcel.readValue(null)).intValue();
        }
    }

    public COUILoadProgress(Context context) {
        this(context, null);
    }

    public static /* synthetic */ OnProgressAnimationUpdateListener access$000(COUILoadProgress cOUILoadProgress) {
        return cOUILoadProgress.mVisualProgressAnimationListener;
    }

    private void init() {
        this.mProgress = 0;
        this.mMax = 100;
    }

    private void refreshProgressWithAnim(int i3) {
        if (this.mVisualProgressSpringAnimation == null) {
            this.mVisualProgressSpringAnimation = new COUISpringAnimation(Float.valueOf(this.mVisualProgress), this.mVisualProgressProperty);
            COUISpringForce cOUISpringForce = new COUISpringForce();
            cOUISpringForce.setBounce(0.0f);
            cOUISpringForce.setResponse(1.0f);
            this.mVisualProgressSpringAnimation.setSpring(cOUISpringForce);
            this.mVisualProgressSpringAnimation.addEndListener(new AnonymousClass2());
        }
        if (this.mVisualProgressSpringAnimation.isRunning()) {
            this.mIsUpdateWithAnimation = true;
            this.mVisualProgressSpringAnimation.animateToFinalPosition(this.mProgress * 1.0f);
            return;
        }
        float f3 = i3 * 1.0f;
        this.mVisualProgress = f3;
        if (Math.abs(f3 - this.mProgress) <= ONE_MILLION) {
            this.mIsUpdateWithAnimation = false;
            invalidate();
            return;
        }
        this.mVisualProgressSpringAnimation.setStartValue(this.mVisualProgress);
        this.mVisualProgressSpringAnimation.animateToFinalPosition(this.mProgress * 1.0f);
        this.mVisualProgressSpringAnimation.setStartVelocity(0.0f);
        this.mVisualProgressSpringAnimation.start();
        this.mIsUpdateWithAnimation = true;
    }

    private void scheduleAccessibilityEventSender() {
        AccessibilityEventSender accessibilityEventSender = this.mAccessibilityEventSender;
        if (accessibilityEventSender == null) {
            this.mAccessibilityEventSender = new AccessibilityEventSender(this, null);
        } else {
            removeCallbacks(accessibilityEventSender);
        }
        postDelayed(this.mAccessibilityEventSender, 10L);
    }

    private void skipAnimation() {
        COUISpringAnimation cOUISpringAnimation = this.mVisualProgressSpringAnimation;
        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) {
            return;
        }
        this.mIsUpdateWithAnimation = false;
        if (this.mVisualProgressSpringAnimation.canSkipToEnd()) {
            this.mVisualProgressSpringAnimation.skipToEnd();
        } else {
            this.mVisualProgressSpringAnimation.cancel();
        }
    }

    @Override // androidx.appcompat.widget.AppCompatButton, android.widget.TextView, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mButtonDrawable != null) {
            this.mButtonDrawable.setState(getDrawableState());
            invalidate();
        }
    }

    public int getMax(int i3) {
        return this.mMax;
    }

    public int getProgress() {
        return this.mProgress;
    }

    public int getState() {
        return this.mState;
    }

    @Override // android.widget.TextView, android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.mButtonDrawable;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
    }

    @Override // android.widget.TextView, android.view.View
    public int[] onCreateDrawableState(int i3) {
        int[] iArrOnCreateDrawableState = super.onCreateDrawableState(i3 + 1);
        if (getState() == 0) {
            View.mergeDrawableStates(iArrOnCreateDrawableState, DEFAULT_SET);
        }
        if (getState() == 1) {
            View.mergeDrawableStates(iArrOnCreateDrawableState, ING_SET);
        }
        if (getState() == 2) {
            View.mergeDrawableStates(iArrOnCreateDrawableState, WAIT_SET);
        }
        if (getState() == 3) {
            View.mergeDrawableStates(iArrOnCreateDrawableState, FAIL_SET);
        }
        return iArrOnCreateDrawableState;
    }

    @Override // android.view.View
    public void onDetachedFromWindow() {
        AccessibilityEventSender accessibilityEventSender = this.mAccessibilityEventSender;
        if (accessibilityEventSender != null) {
            removeCallbacks(accessibilityEventSender);
        }
        skipAnimation();
        super.onDetachedFromWindow();
    }

    @Override // android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void onProgressRefresh(int i3) {
        AccessibilityManager accessibilityManager = this.mManager;
        if (accessibilityManager != null && accessibilityManager.isEnabled() && AccessibilityManagerCompat.isTouchExplorationEnabled(this.mManager)) {
            scheduleAccessibilityEventSender();
        }
    }

    @Override // android.widget.TextView, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setState(savedState.mState);
        setProgress(savedState.mProgress);
        requestLayout();
    }

    @Override // android.widget.TextView, android.view.View
    public Parcelable onSaveInstanceState() {
        setFreezesText(true);
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mState = getState();
        savedState.mProgress = this.mProgress;
        return savedState;
    }

    @Override // android.widget.TextView, android.view.View
    public void onVisibilityChanged(View view, int i3) {
        super.onVisibilityChanged(view, i3);
        if (i3 == 8 || i3 == 4) {
            skipAnimation();
            invalidate();
        }
    }

    @Override // android.view.View
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    public void setButtonDrawable(int i3) {
        if (i3 == 0 || i3 != this.mButtonResource) {
            this.mButtonResource = i3;
            setButtonDrawable(i3 != 0 ? getResources().getDrawable(this.mButtonResource) : null);
        }
    }

    public void setMax(int i3) {
        if (i3 < 0) {
            i3 = 0;
        }
        if (i3 != this.mMax) {
            this.mMax = i3;
            if (this.mProgress > i3) {
                this.mProgress = i3;
            }
            invalidate();
        }
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.mOnStateChangeListener = onStateChangeListener;
    }

    public void setOnStateChangeWidgetListener(OnStateChangeListener onStateChangeListener) {
        this.mOnStateChangeWidgetListener = onStateChangeListener;
    }

    public void setProgress(int i3) {
        setProgress(i3, true);
    }

    public void setState(int i3) {
        if (this.mState != i3) {
            this.mState = i3;
            refreshDrawableState();
            if (this.mBroadcasting) {
                return;
            }
            this.mBroadcasting = true;
            OnStateChangeListener onStateChangeListener = this.mOnStateChangeListener;
            if (onStateChangeListener != null) {
                onStateChangeListener.onStateChanged(this, this.mState);
            }
            OnStateChangeListener onStateChangeListener2 = this.mOnStateChangeWidgetListener;
            if (onStateChangeListener2 != null) {
                onStateChangeListener2.onStateChanged(this, this.mState);
            }
            this.mBroadcasting = false;
        }
    }

    public void setVisualProgressAnimationListener(OnProgressAnimationUpdateListener onProgressAnimationUpdateListener) {
        this.mVisualProgressAnimationListener = onProgressAnimationUpdateListener;
    }

    public void toggle() {
        int i3 = this.mState;
        if (i3 == 0) {
            setState(1);
            return;
        }
        if (i3 == 1) {
            setState(2);
        } else if (i3 == 2) {
            setState(1);
        } else if (i3 == 3) {
            setState(1);
        }
    }

    @Override // android.widget.TextView, android.view.View
    public boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.mButtonDrawable;
    }

    public COUILoadProgress(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiLoadProgressStyle);
    }

    public int getMax() {
        return this.mMax;
    }

    public void setProgress(int i3, boolean z2) {
        if (i3 < 0) {
            i3 = 0;
        }
        int i4 = this.mMax;
        if (i3 > i4) {
            i3 = i4;
        }
        if (z2) {
            int i5 = this.mProgress;
            if (i3 != i5) {
                this.mProgress = i3;
            }
            refreshProgressWithAnim(i5);
            return;
        }
        if (i3 != this.mProgress) {
            this.mProgress = i3;
        }
        if (this.mIsUpdateWithAnimation) {
            this.mIsUpdateWithAnimation = false;
        }
        invalidate();
        onProgressRefresh(i3);
    }

    public COUILoadProgress(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, R.style.Widget_COUI_COUILoadProgress);
    }

    public COUILoadProgress(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3);
        this.TAG = "COUILoadProgress";
        this.DEBUG = false;
        this.mVisualProgressProperty = new AnonymousClass1("VisualProgressProperty");
        this.mIsUpdateWithAnimation = false;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUILoadProgress, i3, i4);
        int integer = typedArrayObtainStyledAttributes.getInteger(R.styleable.COUILoadProgress_couiState, 0);
        Drawable drawable = typedArrayObtainStyledAttributes.getDrawable(R.styleable.COUILoadProgress_couiDefaultDrawable);
        if (drawable != null) {
            setButtonDrawable(drawable);
        }
        setProgress(typedArrayObtainStyledAttributes.getInt(R.styleable.COUILoadProgress_couiProgress, this.mProgress));
        setState(integer);
        typedArrayObtainStyledAttributes.recycle();
        init();
        if (ViewCompat.getImportantForAccessibility(this) == 0) {
            ViewCompat.setImportantForAccessibility(this, 1);
        }
        this.mManager = (AccessibilityManager) context.getSystemService(RomUpdateListParser.ACCESSIBILITY_TAG);
    }

    public void setButtonDrawable(Drawable drawable) {
        if (drawable != null) {
            Drawable drawable2 = this.mButtonDrawable;
            if (drawable2 != null) {
                drawable2.setCallback(null);
                unscheduleDrawable(this.mButtonDrawable);
            }
            drawable.setCallback(this);
            drawable.setState(getDrawableState());
            drawable.setVisible(getVisibility() == 0, false);
            this.mButtonDrawable = drawable;
            this.mButtonDrawableReverseColor = drawable.getConstantState().newDrawable();
            this.mButtonDrawable.setState(null);
            setMinHeight(this.mButtonDrawable.getIntrinsicHeight());
            refreshDrawableState();
            return;
        }
        this.mButtonDrawable = null;
        this.mButtonDrawableReverseColor = null;
        this.mButtonResource = 0;
    }
}