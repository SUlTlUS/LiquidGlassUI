package com.coui.appcompat.checkbox;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Checkable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.ViewUtils;
import androidx.core.content.res.ResourcesCompat;
import com.coui.appcompat.accessibilityutil.COUIAccessibilityUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.state.COUIMaskRippleDrawable;
import com.coui.appcompat.state.COUIStateEffectDrawable;
import com.coui.appcompat.uiutil.COUIWorkHandler;
import com.decouple.injector.adaptor.BaseConfig;
import com.oplus.settings.romupdate.RomUpdateListParser;
import com.support.appcompat.R;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes7.dex */
public class COUICheckBox extends AppCompatButton implements Checkable {
    private static final int[] ALLSELECT_SET;
    private static final Rect BUTTON_DRAWABLE_BOUNDS;
    private static final boolean COUI_DEBUG;
    private static final boolean DEBUG = false;
    private static final long DEFAULT_LOAD_DRAWABLE_DELAY = 100;
    private static final int MAX_INDEX = 2;
    private static final int[] PARTSELECT_SET;
    public static final int SELECT_ALL = 2;
    public static final int SELECT_NONE = 0;
    public static final int SELECT_PART = 1;
    public static final int SELECT_UNSPECIFIC = -1;
    private static final String TAG = "COUICheckBox";
    private AccessibilityManager mAccessibilityManager;
    private final AtomicBoolean mAsyncLock;
    private boolean mBroadcasting;
    private Drawable mButtonDrawable;
    private int mButtonResource;
    private int mDrawableTextMargin;
    private COUIMaskRippleDrawable mMaskRippleDrawable;
    private OnStateChangeListener mOnStateChangeListener;
    private int mPendingState;
    private int mState;
    private COUIStateEffectDrawable mStateEffectBackground;
    private int mStyle;

    public static class LoadDrawableRunnable implements Runnable {
        private final AttributeSet mAttrs;
        private final int mDefStyle;
        private final WeakReference<COUICheckBox> mWeakCheckbox;

        public LoadDrawableRunnable(COUICheckBox cOUICheckBox, AttributeSet attributeSet, int i3) {
            this.mWeakCheckbox = new WeakReference<>(cOUICheckBox);
            this.mAttrs = attributeSet;
            this.mDefStyle = i3;
        }

        public static /* synthetic */ void a(COUICheckBox cOUICheckBox, Drawable drawable) {
            COUICheckBox.access$200(cOUICheckBox, drawable);
        }

        private void configAnimatedVectorDrawableAndSetState(COUICheckBox cOUICheckBox, Drawable drawable) {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                COUICheckBox.access$200(cOUICheckBox, drawable);
            } else {
                cOUICheckBox.postOnAnimation(new a(cOUICheckBox, drawable));
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            COUICheckBox cOUICheckBox = this.mWeakCheckbox.get();
            if (cOUICheckBox != null && COUICheckBox.access$000(cOUICheckBox)) {
                long jCurrentTimeMillis = System.currentTimeMillis();
                if (COUICheckBox.access$100()) {
                    Log.d(COUICheckBox.TAG, "runnable run, current thread = " + Thread.currentThread() + " start time = " + jCurrentTimeMillis);
                }
                TypedArray typedArrayObtainStyledAttributes = cOUICheckBox.getContext().obtainStyledAttributes(this.mAttrs, R.styleable.COUICheckBox, this.mDefStyle, 0);
                Drawable drawable = typedArrayObtainStyledAttributes.getDrawable(R.styleable.COUICheckBox_couiButton);
                if (drawable != null) {
                    configAnimatedVectorDrawableAndSetState(cOUICheckBox, drawable);
                }
                if (COUICheckBox.access$100()) {
                    Log.d(COUICheckBox.TAG, "end time = " + (System.currentTimeMillis() - jCurrentTimeMillis));
                }
                typedArrayObtainStyledAttributes.recycle();
            }
        }
    }

    public interface OnStateChangeListener {
        void onStateChanged(COUICheckBox cOUICheckBox, int i3);
    }

    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new AnonymousClass1();
        int mState;

        /* JADX INFO: renamed from: com.coui.appcompat.checkbox.COUICheckBox$SavedState$1, reason: invalid class name */
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
            return "CompoundButton.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " state=" + this.mState + "}";
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i3) {
            super.writeToParcel(parcel, i3);
            parcel.writeValue(Integer.valueOf(this.mState));
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
            this.mState = 0;
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.mState = 0;
            this.mState = ((Integer) parcel.readValue(null)).intValue();
        }
    }

    static {
        COUI_DEBUG = COUILog.LOG_DEBUG || COUILog.isLoggable(TAG, 3);
        ALLSELECT_SET = new int[]{R.attr.coui_state_allSelect};
        PARTSELECT_SET = new int[]{R.attr.coui_state_partSelect};
        BUTTON_DRAWABLE_BOUNDS = new Rect();
    }

    public COUICheckBox(Context context) {
        this(context, null);
    }

    public static /* synthetic */ boolean access$000(COUICheckBox cOUICheckBox) {
        return cOUICheckBox.requestLock();
    }

    public static /* synthetic */ boolean access$100() {
        return COUI_DEBUG;
    }

    public static /* synthetic */ void access$200(COUICheckBox cOUICheckBox, Drawable drawable) {
        cOUICheckBox.configAnimatedVectorDrawableAndSetState(drawable);
    }

    private void asyncLoadAnimatedVectorDrawable(Runnable runnable) {
        COUIWorkHandler.getInstance().start(runnable);
        postDelayed(runnable, 100L);
        configDefaultDrawable(this.mPendingState);
    }

    private void checkDrawableEnableState() {
        if (isFocusable() || isClickable()) {
            this.mMaskRippleDrawable.setDrawableEnabled(true);
        } else {
            this.mMaskRippleDrawable.setDrawableEnabled(false);
        }
    }

    private void configAnimatedVectorDrawableAndSetState(Drawable drawable) {
        setButtonDrawable(drawable);
        jumpToCurrentState(drawable, this.mPendingState);
        int i3 = this.mPendingState;
        this.mPendingState = -1;
        setState(i3);
    }

    private void configDefaultDrawable(int i3) {
        int i4;
        if (i3 == 0) {
            i4 = isEnabled() ? R.drawable.coui_btn_check_off_normal : R.drawable.coui_btn_check_off_disabled;
        } else if (i3 == 1) {
            i4 = isEnabled() ? R.drawable.coui_btn_part_check_on_normal : R.drawable.coui_btn_part_check_on_disabled;
        } else if (i3 != 2) {
            i4 = -1;
        } else {
            i4 = isEnabled() ? R.drawable.coui_btn_check_on_normal : R.drawable.coui_btn_check_on_disabled;
        }
        if (i4 != -1) {
            setButtonDrawable(i4);
        }
    }

    private void configStateDrawable() {
        this.mStateEffectBackground.setBounds(BUTTON_DRAWABLE_BOUNDS);
    }

    private void configStateEffectBackground() {
        COUIMaskRippleDrawable cOUIMaskRippleDrawable = new COUIMaskRippleDrawable(getContext());
        this.mMaskRippleDrawable = cOUIMaskRippleDrawable;
        cOUIMaskRippleDrawable.setCircleRippleMask(COUIMaskRippleDrawable.getMaskRippleRadiusByType(getContext(), 1));
        COUIStateEffectDrawable cOUIStateEffectDrawable = new COUIStateEffectDrawable(new Drawable[]{getBackground() == null ? new ColorDrawable(0) : getBackground(), this.mMaskRippleDrawable});
        this.mStateEffectBackground = cOUIStateEffectDrawable;
        super.setBackground(cOUIStateEffectDrawable);
        COUIDarkModeUtil.setForceDarkAllow(this, false);
    }

    private CharSequence getButtonStateDescription() {
        int i3 = this.mState;
        if (i3 == 2) {
            return getContext().getResources().getString(R.string.coui_accessibility_checked);
        }
        return i3 == 0 ? getContext().getResources().getString(R.string.coui_accessibility_unchecked) : getContext().getResources().getString(R.string.coui_accessibility_partchecked);
    }

    private void jumpToCurrentState(Drawable drawable, int i3) {
        if (i3 == 1) {
            drawable.setState(PARTSELECT_SET);
        } else if (i3 == 2) {
            drawable.setState(ALLSELECT_SET);
        }
        drawable.jumpToCurrentState();
    }

    private boolean requestLock() {
        return this.mAsyncLock.compareAndSet(false, true);
    }

    private void updateButtonDrawableBounds() {
        int height;
        Drawable drawable = this.mButtonDrawable;
        if (drawable != null) {
            int gravity = getGravity() & 112;
            int intrinsicHeight = drawable.getIntrinsicHeight();
            int intrinsicWidth = drawable.getIntrinsicWidth();
            if (gravity != 16) {
                height = gravity != 80 ? 0 : getHeight() - intrinsicHeight;
            } else {
                height = (getHeight() - intrinsicHeight) / 2;
            }
            BUTTON_DRAWABLE_BOUNDS.set(ViewUtils.isLayoutRtl(this) ? (getWidth() - intrinsicWidth) - getPaddingRight() : getPaddingLeft(), height, ViewUtils.isLayoutRtl(this) ? getWidth() - getPaddingRight() : intrinsicWidth + getPaddingLeft(), intrinsicHeight + height);
        }
    }

    private void updateStateDescription() {
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
        if (this.mAccessibilityManager == null) {
            this.mAccessibilityManager = (AccessibilityManager) getContext().getSystemService(RomUpdateListParser.ACCESSIBILITY_TAG);
        }
        if (this.mAccessibilityManager.isEnabled()) {
            AccessibilityEvent accessibilityEventObtain = AccessibilityEvent.obtain();
            accessibilityEventObtain.setEventType(2048);
            accessibilityEventObtain.setContentChangeTypes(64);
            sendAccessibilityEventUnchecked(accessibilityEventObtain);
        }
    }

    @Override // android.view.View
    public boolean dispatchHoverEvent(MotionEvent motionEvent) {
        if (isEnabled() && motionEvent.getActionMasked() == 9) {
            setHovered(true);
        }
        if (motionEvent.getActionMasked() == 10) {
            setHovered(false);
        }
        return super.dispatchHoverEvent(motionEvent);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        updateButtonDrawableBounds();
        checkDrawableEnableState();
        configStateDrawable();
        super.draw(canvas);
    }

    @Override // androidx.appcompat.widget.AppCompatButton, android.widget.TextView, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mButtonDrawable != null) {
            this.mButtonDrawable.setState(getDrawableState());
            invalidate();
        }
    }

    @Override // android.widget.Button, android.widget.TextView, android.view.View
    public CharSequence getAccessibilityClassName() {
        return COUIAccessibilityUtil.COMPOUND_BUTTON_CLASS_NAME;
    }

    @Override // android.widget.TextView
    public int getCompoundPaddingLeft() {
        Drawable drawable;
        int compoundPaddingLeft = super.getCompoundPaddingLeft();
        if (ViewUtils.isLayoutRtl(this) || (drawable = this.mButtonDrawable) == null) {
            return compoundPaddingLeft;
        }
        int intrinsicWidth = compoundPaddingLeft + drawable.getIntrinsicWidth();
        return !TextUtils.isEmpty(getText()) ? intrinsicWidth + this.mDrawableTextMargin : intrinsicWidth;
    }

    @Override // android.widget.TextView
    public int getCompoundPaddingRight() {
        Drawable drawable;
        int compoundPaddingRight = super.getCompoundPaddingRight();
        if (!ViewUtils.isLayoutRtl(this) || (drawable = this.mButtonDrawable) == null) {
            return compoundPaddingRight;
        }
        int intrinsicWidth = compoundPaddingRight + drawable.getIntrinsicWidth();
        return !TextUtils.isEmpty(getText()) ? intrinsicWidth + this.mDrawableTextMargin : intrinsicWidth;
    }

    @ViewDebug.ExportedProperty
    public int getState() {
        return this.mState;
    }

    @Override // android.widget.Checkable
    public boolean isChecked() {
        return getState() == 2;
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
        if (getState() == 1) {
            View.mergeDrawableStates(iArrOnCreateDrawableState, PARTSELECT_SET);
        }
        if (getState() == 2) {
            View.mergeDrawableStates(iArrOnCreateDrawableState, ALLSELECT_SET);
        }
        return iArrOnCreateDrawableState;
    }

    @Override // android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable drawable = this.mButtonDrawable;
        if (drawable != null) {
            Rect rect = BUTTON_DRAWABLE_BOUNDS;
            drawable.setBounds(rect);
            drawable.draw(canvas);
            Drawable background = getBackground();
            if (background != null) {
                background.setHotspotBounds(rect.left, rect.top, rect.right, rect.bottom);
            }
        }
    }

    @Override // androidx.appcompat.widget.AppCompatButton, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        if (this.mState == 2) {
            accessibilityEvent.setChecked(true);
        } else {
            accessibilityEvent.setChecked(false);
        }
    }

    @Override // androidx.appcompat.widget.AppCompatButton, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setCheckable(true);
        if (this.mState == 2) {
            accessibilityNodeInfo.setChecked(true);
        } else {
            accessibilityNodeInfo.setChecked(false);
        }
        accessibilityNodeInfo.setClassName(COUIAccessibilityUtil.COMPOUND_BUTTON_CLASS_NAME);
        accessibilityNodeInfo.setStateDescription(getButtonStateDescription());
    }

    @Override // android.widget.TextView, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setState(savedState.mState);
        requestLayout();
    }

    @Override // android.widget.TextView, android.view.View
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mState = getState();
        return savedState;
    }

    @Override // android.view.View
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    public void refresh() {
        String resourceTypeName = getResources().getResourceTypeName(this.mStyle);
        TypedArray typedArrayObtainStyledAttributes = null;
        if ("attr".equals(resourceTypeName)) {
            typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(null, R.styleable.COUICheckBox, this.mStyle, 0);
        } else if (BaseConfig.KEY_STYLE.equals(resourceTypeName)) {
            typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(null, R.styleable.COUICheckBox, 0, this.mStyle);
        }
        if (typedArrayObtainStyledAttributes != null) {
            Drawable drawable = typedArrayObtainStyledAttributes.getDrawable(R.styleable.COUICheckBox_couiButton);
            if (drawable != null) {
                setButtonDrawable(drawable);
            }
            typedArrayObtainStyledAttributes.recycle();
        }
        COUIStateEffectDrawable cOUIStateEffectDrawable = this.mStateEffectBackground;
        if (cOUIStateEffectDrawable != null) {
            cOUIStateEffectDrawable.refresh(getContext());
        }
    }

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        COUIStateEffectDrawable cOUIStateEffectDrawable = this.mStateEffectBackground;
        if (cOUIStateEffectDrawable == null) {
            super.setBackground(drawable);
        } else if (drawable == null) {
            cOUIStateEffectDrawable.setViewBackground(new ColorDrawable(0));
        } else {
            cOUIStateEffectDrawable.setViewBackground(drawable);
        }
    }

    public void setButtonDrawable(int i3) {
        if (i3 == 0 || i3 != this.mButtonResource) {
            this.mButtonResource = i3;
            setButtonDrawable(i3 != 0 ? ResourcesCompat.getDrawable(getResources(), this.mButtonResource, getContext().getTheme()) : null);
        }
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean z2) {
        if (z2) {
            setState(2);
        } else {
            setState(0);
        }
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.mOnStateChangeListener = onStateChangeListener;
    }

    public void setState(int i3) {
        if (this.mPendingState != -1) {
            this.mPendingState = i3;
            configDefaultDrawable(i3);
            return;
        }
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
            this.mBroadcasting = false;
            updateStateDescription();
        }
    }

    @Override // android.widget.Checkable
    public void toggle() {
        setState(this.mState >= 2 ? 0 : 2);
    }

    @Override // android.widget.TextView, android.view.View
    public boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.mButtonDrawable;
    }

    public COUICheckBox(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiCheckBoxStyle);
    }

    public COUICheckBox(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.mAsyncLock = new AtomicBoolean(false);
        if (attributeSet != null && attributeSet.getStyleAttribute() != 0) {
            this.mStyle = attributeSet.getStyleAttribute();
        } else {
            this.mStyle = i3;
        }
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUICheckBox, i3, 0);
        boolean z2 = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUICheckBox_couiAsyncLoad, false);
        int i4 = R.styleable.COUICheckBox_couiButton;
        int resourceId = typedArrayObtainStyledAttributes.getResourceId(i4, -1);
        int integer = typedArrayObtainStyledAttributes.getInteger(R.styleable.COUICheckBox_couiCheckBoxState, 0);
        this.mPendingState = integer;
        if (COUI_DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("asyncLoad = ");
            sb.append(z2);
            sb.append(" drawable check = ");
            sb.append(resourceId == R.drawable.coui_checkbox_state);
            sb.append(" thread check = ");
            sb.append(Looper.getMainLooper() == Looper.myLooper());
            Log.d(TAG, sb.toString());
        }
        if (z2 && resourceId == R.drawable.coui_checkbox_state && Looper.getMainLooper() == Looper.myLooper()) {
            asyncLoadAnimatedVectorDrawable(new LoadDrawableRunnable(this, attributeSet, i3));
        } else {
            Drawable drawable = typedArrayObtainStyledAttributes.getDrawable(i4);
            if (drawable != null) {
                setButtonDrawable(drawable);
                this.mPendingState = -1;
                setState(integer);
            }
        }
        typedArrayObtainStyledAttributes.recycle();
        if (attributeSet != null) {
            int styleAttribute = attributeSet.getStyleAttribute();
            this.mStyle = styleAttribute;
            if (styleAttribute == 0) {
                this.mStyle = i3;
            }
        } else {
            this.mStyle = i3;
        }
        configStateEffectBackground();
        this.mDrawableTextMargin = getContext().getResources().getDimensionPixelSize(R.dimen.coui_checkbox_margin_between_text_drawable);
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
            drawable.setState(null);
            setMinHeight(this.mButtonDrawable.getIntrinsicHeight());
        }
        refreshDrawableState();
    }
}