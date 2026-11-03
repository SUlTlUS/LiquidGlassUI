package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.preference.PreferenceViewHolder;
import com.coui.appcompat.stepper.COUIStepperView;
import com.coui.appcompat.stepper.IStepper;
import com.coui.appcompat.stepper.ObservableStep;
import com.coui.appcompat.stepper.OnStepChangeListener;
import com.support.preference.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIStepperPreference extends COUIPreference implements IStepper, OnStepChangeListener {
    private COUIStepperView mCOUIStepperView;
    private int mDefaultValue;
    private int mInitialMaximum;
    private int mInitialMinimum;
    private int mInitialValue;
    private OnStepChangeListener mOnStepChangeListener;
    private int mUnit;

    public COUIStepperPreference(Context context) {
        this(context, null);
    }

    public void changePersistence(Boolean bool) {
        if (bool.booleanValue()) {
            setPersistent(true);
            persistInt(this.mInitialValue);
        } else {
            persistInt(this.mDefaultValue);
            setPersistent(false);
        }
    }

    public COUIStepperView getCOUIStepperView() {
        return this.mCOUIStepperView;
    }

    @Override // com.coui.appcompat.stepper.IStepper
    public int getCurStep() {
        COUIStepperView cOUIStepperView = this.mCOUIStepperView;
        return cOUIStepperView != null ? cOUIStepperView.getCurStep() : this.mInitialValue;
    }

    @Override // com.coui.appcompat.stepper.IStepper
    public int getMaximum() {
        COUIStepperView cOUIStepperView = this.mCOUIStepperView;
        return cOUIStepperView != null ? cOUIStepperView.getMaximum() : this.mInitialMaximum;
    }

    @Override // com.coui.appcompat.stepper.IStepper
    public int getMinimum() {
        COUIStepperView cOUIStepperView = this.mCOUIStepperView;
        return cOUIStepperView != null ? cOUIStepperView.getMinimum() : this.mInitialMinimum;
    }

    @Override // com.coui.appcompat.stepper.IStepper
    public int getUnit() {
        COUIStepperView cOUIStepperView = this.mCOUIStepperView;
        return cOUIStepperView != null ? cOUIStepperView.getUnit() : this.mUnit;
    }

    @Override // com.coui.appcompat.stepper.IStepper
    public void minus() {
        COUIStepperView cOUIStepperView = this.mCOUIStepperView;
        if (cOUIStepperView != null) {
            cOUIStepperView.minus();
        }
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        COUIStepperView cOUIStepperView = (COUIStepperView) preferenceViewHolder.findViewById(R.id.stepper);
        this.mCOUIStepperView = cOUIStepperView;
        if (cOUIStepperView != null) {
            setMaximum(this.mInitialMaximum);
            setMinimum(this.mInitialMinimum);
            setCurStep(this.mInitialValue);
            setUnit(this.mUnit);
            this.mCOUIStepperView.setOnStepChangeListener(this);
        }
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onDetached() {
        super.onDetached();
        COUIStepperView cOUIStepperView = this.mCOUIStepperView;
        if (cOUIStepperView != null) {
            cOUIStepperView.release();
            this.mCOUIStepperView = null;
        }
    }

    @Override // androidx.preference.Preference
    public Object onGetDefaultValue(TypedArray typedArray, int i3) {
        return Integer.valueOf(typedArray.getInt(i3, 0));
    }

    @Override // androidx.preference.Preference
    public void onSetInitialValue(Object obj) {
        if (obj == null) {
            obj = 0;
        }
        this.mInitialValue = getPersistedInt(((Integer) obj).intValue());
    }

    @Override // com.coui.appcompat.stepper.OnStepChangeListener
    public void onStepChanged(int i3, int i4) {
        this.mInitialValue = i3;
        persistInt(i3);
        if (i3 != i4) {
            callChangeListener(Integer.valueOf(i3));
        }
        OnStepChangeListener onStepChangeListener = this.mOnStepChangeListener;
        if (onStepChangeListener == null || i3 == i4) {
            return;
        }
        onStepChangeListener.onStepChanged(i3, i4);
    }

    @Override // com.coui.appcompat.stepper.IStepper
    public void plus() {
        COUIStepperView cOUIStepperView = this.mCOUIStepperView;
        if (cOUIStepperView != null) {
            cOUIStepperView.plus();
        }
    }

    @Override // com.coui.appcompat.stepper.IStepper
    public void setCurStep(int i3) {
        COUIStepperView cOUIStepperView = this.mCOUIStepperView;
        if (cOUIStepperView != null) {
            cOUIStepperView.setCurStep(i3);
        }
    }

    @Override // com.coui.appcompat.stepper.IStepper
    public void setMaximum(int i3) {
        this.mInitialMaximum = i3;
        COUIStepperView cOUIStepperView = this.mCOUIStepperView;
        if (cOUIStepperView != null) {
            cOUIStepperView.setMaximum(i3);
        }
    }

    @Override // com.coui.appcompat.stepper.IStepper
    public void setMinimum(int i3) {
        this.mInitialMinimum = i3;
        COUIStepperView cOUIStepperView = this.mCOUIStepperView;
        if (cOUIStepperView != null) {
            cOUIStepperView.setMinimum(i3);
        }
    }

    @Override // com.coui.appcompat.stepper.IStepper
    public void setOnStepChangeListener(OnStepChangeListener onStepChangeListener) {
        this.mOnStepChangeListener = onStepChangeListener;
    }

    @Override // com.coui.appcompat.stepper.IStepper
    public void setUnit(int i3) {
        this.mUnit = i3;
        COUIStepperView cOUIStepperView = this.mCOUIStepperView;
        if (cOUIStepperView != null) {
            cOUIStepperView.setUnit(i3);
        }
    }

    public COUIStepperPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiStepperPreferenceStyle);
    }

    public COUIStepperPreference(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, R.style.Preference_COUI_COUIStepperPreference);
    }

    public COUIStepperPreference(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUIStepperPreference, i3, i4);
        this.mInitialMaximum = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIStepperPreference_couiMaximum, ObservableStep.MAX_VALUE);
        this.mInitialMinimum = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIStepperPreference_couiMinimum, -999);
        int i5 = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIStepperPreference_couiDefStep, 0);
        this.mInitialValue = i5;
        this.mDefaultValue = i5;
        this.mUnit = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIStepperPreference_couiUnit, 1);
        typedArrayObtainStyledAttributes.recycle();
    }
}