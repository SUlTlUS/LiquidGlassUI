package com.coui.appcompat.seekbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.core.app.NotificationCompat;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.seekbar.core.COUISeekBarAdvanced;
import com.coui.appcompat.seekbar.effect.GlitterEffectParams;
import com.coui.appcompat.seekbar.effect.GlitterEffectRenderer;
import com.coui.appcompat.seekbar.renderer.IProgressRenderer;
import com.coui.appcompat.seekbar.renderer.IThumbRenderer;
import com.coui.appcompat.seekbar.renderer.TexturedThumbRenderer;
import com.coui.appcompat.statelistutil.COUIStateListUtil;
import com.coui.appcompat.vibrateutil.VibrateUtils;
import com.support.appcompat.R;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0017\u0018\u0000 )2\u00020\u0001:\u0002)*B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007¢\u0006\u0002\u0010\tJ\b\u0010\r\u001a\u00020\u000eH\u0002J\b\u0010\u000f\u001a\u00020\u0010H\u0014J\u0010\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u0015H\u0014J\n\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0002J*\u0010\u0018\u001a\u00020\u000e2\u0006\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u0002J*\u0010\u0019\u001a\u00020\u000e2\u0006\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u0002J\b\u0010\u001a\u001a\u00020\u000eH\u0002J\b\u0010\u001b\u001a\u00020\u000eH\u0014J\b\u0010\u001c\u001a\u00020\u000eH\u0014J\u0010\u0010\u001d\u001a\u00020\u000e2\u0006\u0010\u001e\u001a\u00020\fH\u0014J\b\u0010\u001f\u001a\u00020\u000eH\u0002J\u0010\u0010 \u001a\u00020\u000e2\u0006\u0010\u001e\u001a\u00020\fH\u0016J\u0012\u0010!\u001a\u00020\u000e2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016J\u0012\u0010$\u001a\u00020\u000e2\b\u0010%\u001a\u0004\u0018\u00010&H\u0016J\b\u0010'\u001a\u00020\u000eH\u0014J\b\u0010(\u001a\u00020\fH\u0014R\u000e\u0010\n\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006+"}, d2 = {"Lcom/coui/appcompat/seekbar/COUISeekBar;", "Lcom/coui/appcompat/seekbar/core/COUISeekBarAdvanced;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "defStyleRes", "(Landroid/content/Context;Landroid/util/AttributeSet;II)V", "mContainerThemeColor", "mIsProgressColorCustomized", "", "applyProgressThemeColor", "", "createThumbRenderer", "Lcom/coui/appcompat/seekbar/renderer/IThumbRenderer;", "dispatchTouchEvent", NotificationCompat.CATEGORY_EVENT, "Landroid/view/MotionEvent;", "getMinEnlargeHeight", "", "getTexturedThumbRenderer", "Lcom/coui/appcompat/seekbar/renderer/TexturedThumbRenderer;", "initAdditionalAttributes", "initGlitterEffect", "initProgressThemeColor", "onAttachedToWindow", "onDetachedFromWindow", "onSpotLightEnabledChanged", "enabled", "resolveContainerThemeColor", "setGlitterEffectEnabled", "setOnSeekBarChangeListener", "listener", "Lcom/coui/appcompat/seekbar/COUISeekBar$OnSeekBarChangeListener;", "setProgressColor", "progressColor", "Landroid/content/res/ColorStateList;", "updateColors", "useDragTransitionAnimation", "Companion", "OnSeekBarChangeListener", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class COUISeekBar extends COUISeekBarAdvanced {
    public static final int MOVE_BY_DEFAULT = 0;
    public static final int MOVE_BY_DISTANCE = 2;
    public static final int MOVE_BY_FINGER = 1;
    private int mContainerThemeColor;
    private boolean mIsProgressColorCustomized;

    /* JADX INFO: loaded from: classes.dex */
    @Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH&J\u0010\u0010\n\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u000b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&¨\u0006\f"}, d2 = {"Lcom/coui/appcompat/seekbar/COUISeekBar$OnSeekBarChangeListener;", "", "onProgressChanged", "", "seekBar", "Lcom/coui/appcompat/seekbar/COUISeekBar;", "progress", "", "fromUser", "", "onStartTrackingTouch", "onStopTrackingTouch", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public interface OnSeekBarChangeListener {
        void onProgressChanged(COUISeekBar seekBar, int progress, boolean fromUser);

        void onStartTrackingTouch(COUISeekBar seekBar);

        void onStopTrackingTouch(COUISeekBar seekBar);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUISeekBar(Context context) {
        this(context, null, 0, 0, 14, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    private final void applyProgressThemeColor() {
        int i3;
        if (this.mIsProgressColorCustomized || (i3 = this.mContainerThemeColor) == 0 || i3 == 0) {
            return;
        }
        getState().setProgressColorStateList$coui_support_seekbar_release(COUIStateListUtil.createColorStateList(this.mContainerThemeColor, COUIContextUtil.getAttrColor(getContext(), R.attr.couiColorDisable)));
        int color = getColor(this, getState().getProgressColorStateList(), this.mContainerThemeColor);
        getState().setProgressColor(color);
        IProgressRenderer mProgressRenderer = getMProgressRenderer();
        if (mProgressRenderer != null) {
            mProgressRenderer.setProgressColor(color);
        }
    }

    private final TexturedThumbRenderer getTexturedThumbRenderer() {
        IThumbRenderer mThumbRenderer = getMThumbRenderer();
        if (mThumbRenderer instanceof TexturedThumbRenderer) {
            return (TexturedThumbRenderer) mThumbRenderer;
        }
        return null;
    }

    private final void initAdditionalAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attrs, com.support.seekbar.R.styleable.COUISeekBar, defStyleAttr, defStyleRes);
        Intrinsics.checkNotNullExpressionValue(typedArrayObtainStyledAttributes, "obtainStyledAttributes(...)");
        typedArrayObtainStyledAttributes.recycle();
    }

    private final void initGlitterEffect(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray typedArrayObtainStyledAttributes = null;
        try {
            try {
                typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attrs, com.support.seekbar.R.styleable.COUISeekBar, defStyleAttr, defStyleRes);
                boolean z2 = typedArrayObtainStyledAttributes.getBoolean(com.support.seekbar.R.styleable.COUISeekBar_couiSeekBarShowGlitterEffect, true);
                GlitterEffectParams glitterEffectParams = new GlitterEffectParams();
                glitterEffectParams.setEnabled(true);
                glitterEffectParams.setIntensity(1.0f);
                try {
                    glitterEffectParams.setMinColor(context.getResources().getColor(com.support.seekbar.R.color.coui_seekbar_glitter_effect_min_color));
                    glitterEffectParams.setMaxColor(context.getResources().getColor(com.support.seekbar.R.color.coui_seekbar_glitter_effect_max_color));
                } catch (Exception unused) {
                }
                GlitterEffectRenderer mGlitterRenderer = getMGlitterRenderer();
                if (mGlitterRenderer != null) {
                    mGlitterRenderer.setEffectParams(glitterEffectParams);
                }
                getState().setShowGlitterEffect(z2);
                typedArrayObtainStyledAttributes.recycle();
            } catch (Throwable th) {
                if (typedArrayObtainStyledAttributes != null) {
                    typedArrayObtainStyledAttributes.recycle();
                }
                throw th;
            }
        } catch (Exception e3) {
            throw e3;
        }
    }

    private final void initProgressThemeColor() {
        resolveContainerThemeColor();
        applyProgressThemeColor();
    }

    private final void resolveContainerThemeColor() {
        int identifier = getContext().getResources().getIdentifier("couiColorContainerTheme", "attr", getContext().getPackageName());
        if (identifier != 0) {
            this.mContainerThemeColor = COUIContextUtil.getAttrColor(getContext(), identifier, 0);
        }
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public IThumbRenderer createThumbRenderer() {
        return new TexturedThumbRenderer(this);
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarAdvanced, com.coui.appcompat.seekbar.core.COUISeekBarCore, android.view.View
    public boolean dispatchTouchEvent(MotionEvent event) {
        TexturedThumbRenderer texturedThumbRenderer;
        Intrinsics.checkNotNullParameter(event, "event");
        if (isEnabled()) {
            int actionMasked = event.getActionMasked();
            if (actionMasked == 0) {
                TexturedThumbRenderer texturedThumbRenderer2 = getTexturedThumbRenderer();
                if (texturedThumbRenderer2 != null) {
                    texturedThumbRenderer2.onPressDown();
                }
            } else if ((actionMasked == 1 || actionMasked == 3) && (texturedThumbRenderer = getTexturedThumbRenderer()) != null) {
                texturedThumbRenderer.onPressUp();
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public float getMinEnlargeHeight() {
        float mMaxBackgroundHeight = getMMaxBackgroundHeight() - getState().getBackgroundHeight();
        if (mMaxBackgroundHeight > 0.0f) {
            return mMaxBackgroundHeight / 2.0f;
        }
        return 0.0f;
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarAdvanced, com.coui.appcompat.seekbar.core.COUISeekBarCore, android.widget.ProgressBar, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        VibrateUtils.registerHapticObserver(getContext());
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarAdvanced, com.coui.appcompat.seekbar.core.COUISeekBarCore, android.widget.ProgressBar, android.view.View
    public void onDetachedFromWindow() {
        TexturedThumbRenderer texturedThumbRenderer = getTexturedThumbRenderer();
        if (texturedThumbRenderer != null) {
            texturedThumbRenderer.cancelAnimations();
        }
        super.onDetachedFromWindow();
        VibrateUtils.unRegisterHapticObserver();
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarAdvanced
    public void onSpotLightEnabledChanged(boolean enabled) {
        super.onSpotLightEnabledChanged(enabled);
        TexturedThumbRenderer texturedThumbRenderer = getTexturedThumbRenderer();
        if (texturedThumbRenderer != null) {
            texturedThumbRenderer.setEnabled(enabled);
        }
    }

    public void setGlitterEffectEnabled(boolean enabled) {
        if (getMGlitterRenderer() != null) {
            getState().setShowGlitterEffect(enabled);
            invalidate();
        }
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener listener) {
        super.setOnSeekBarChangeListener(listener != null ? new COUISeekBar$setOnSeekBarChangeListener$1$1(listener) : null);
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void setProgressColor(ColorStateList progressColor) {
        super.setProgressColor(progressColor);
        if (progressColor != null) {
            this.mIsProgressColorCustomized = true;
        }
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarCore
    public void updateColors() {
        super.updateColors();
        resolveContainerThemeColor();
        applyProgressThemeColor();
    }

    @Override // com.coui.appcompat.seekbar.core.COUISeekBarAdvanced
    public boolean useDragTransitionAnimation() {
        return true;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUISeekBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUISeekBar(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0, 8, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public /* synthetic */ COUISeekBar(Context context, AttributeSet attributeSet, int i3, int i4, int i5, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i5 & 2) != 0 ? null : attributeSet, (i5 & 4) != 0 ? com.support.seekbar.R.attr.couiSeekBarStyle : i3, (i5 & 8) != 0 ? com.support.seekbar.R.style.COUISeekBar : i4);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUISeekBar(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        Intrinsics.checkNotNullParameter(context, "context");
        initAdditionalAttributes(context, attributeSet, i3, i4);
        initProgressThemeColor();
    }
}