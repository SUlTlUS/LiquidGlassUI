package com.coui.appcompat.stepper;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.state.COUIMaskEffectDrawable;
import com.coui.appcompat.state.COUIStateEffectDrawable;
import com.coui.appcompat.state.COUIStrokeDrawable;
import com.support.stepper.R;
import java.util.Observable;
import java.util.Observer;

/* JADX INFO: loaded from: classes7.dex */
public class COUIStepperView extends ConstraintLayout implements IStepper, Observer {
    private final String TAG;
    private Context mContext;
    private OnStepChangeListener mListener;
    private ImageView mMinusImage;
    private LongPressProxy mMinusLongPressProxy;
    private final Runnable mMinusRunnable;
    private ImageView mPlusImage;
    private LongPressProxy mPlusLongPressProxy;
    private final Runnable mPlusRunnable;
    private ObservableStep mStep;
    private int mStyle;
    private int mUnit;
    private TextView mValueText;

    public COUIStepperView(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void a(COUIStepperView cOUIStepperView) {
        cOUIStepperView.performHapticFeedback(308, 0);
        cOUIStepperView.plus();
    }

    public static /* synthetic */ boolean b(COUIStateEffectDrawable cOUIStateEffectDrawable, View view, MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            cOUIStateEffectDrawable.setTouched(true);
        }
        if (motionEvent.getActionMasked() != 1 && motionEvent.getActionMasked() != 3) {
            return false;
        }
        cOUIStateEffectDrawable.setTouched(false);
        return false;
    }

    public static /* synthetic */ void c(COUIStepperView cOUIStepperView) {
        cOUIStepperView.performHapticFeedback(308, 0);
        cOUIStepperView.minus();
    }

    private void configEffectDrawableForButton() {
        configEffectDrawableForButton(this.mMinusImage, this.mMinusLongPressProxy);
        configEffectDrawableForButton(this.mPlusImage, this.mPlusLongPressProxy);
    }

    private int getNumForMaxWidth() {
        int i3 = 1;
        float f3 = 0.0f;
        for (int i4 = 0; i4 < 10; i4++) {
            float fMeasureText = this.mValueText.getPaint().measureText(String.valueOf(i4));
            if (fMeasureText > f3) {
                i3 = i4;
                f3 = fMeasureText;
            }
        }
        return i3;
    }

    private void initAttr(TypedArray typedArray) {
        try {
            int resourceId = typedArray.getResourceId(R.styleable.COUIStepperView_couiStepperTextStyle, 0);
            int resourceId2 = typedArray.getResourceId(R.styleable.COUIStepperView_couiStepperPlusImage, 0);
            int resourceId3 = typedArray.getResourceId(R.styleable.COUIStepperView_couiStepperMinusImage, 0);
            if (resourceId != 0) {
                this.mValueText.setTextAppearance(resourceId);
            }
            if (resourceId2 != 0) {
                this.mPlusImage.setImageDrawable(ContextCompat.getDrawable(getContext(), resourceId2));
            }
            if (resourceId3 != 0) {
                this.mMinusImage.setImageDrawable(ContextCompat.getDrawable(getContext(), resourceId3));
            }
            configEffectDrawableForButton();
        } catch (Resources.NotFoundException e3) {
            Log.e("COUIStepperView", e3.getMessage());
        }
    }

    @Override // com.coui.appcompat.stepper.IStepper
    public int getCurStep() {
        return this.mStep.getStep();
    }

    @Override // com.coui.appcompat.stepper.IStepper
    public int getMaximum() {
        return this.mStep.getMaximum();
    }

    @Override // com.coui.appcompat.stepper.IStepper
    public int getMinimum() {
        return this.mStep.getMinimum();
    }

    @Override // com.coui.appcompat.stepper.IStepper
    public int getUnit() {
        return this.mUnit;
    }

    public void init(AttributeSet attributeSet, int i3) {
        int i4 = R.style.COUIStepperViewDefStyle;
        this.mStyle = i4;
        LayoutInflater.from(getContext()).inflate(R.layout.coui_stepper_view, this);
        this.mPlusImage = (ImageView) findViewById(R.id.plus);
        this.mMinusImage = (ImageView) findViewById(R.id.minus);
        this.mValueText = (TextView) findViewById(R.id.indicator);
        this.mPlusLongPressProxy = new LongPressProxy(this.mPlusImage, this.mPlusRunnable);
        this.mMinusLongPressProxy = new LongPressProxy(this.mMinusImage, this.mMinusRunnable);
        TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.COUIStepperView, i3, i4);
        int i5 = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIStepperView_couiMaximum, ObservableStep.MAX_VALUE);
        int i6 = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIStepperView_couiMinimum, -999);
        int i7 = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIStepperView_couiDefStep, 0);
        this.mUnit = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIStepperView_couiUnit, 1);
        initAttr(typedArrayObtainStyledAttributes);
        typedArrayObtainStyledAttributes.recycle();
        ObservableStep observableStep = new ObservableStep();
        this.mStep = observableStep;
        observableStep.addObserver(this);
        setMaximum(i5);
        setMinimum(i6);
        setCurStep(i7);
    }

    @Override // com.coui.appcompat.stepper.IStepper
    public void minus() {
        ObservableStep observableStep = this.mStep;
        observableStep.setStep(observableStep.getStep() - getUnit());
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        int numForMaxWidth = getNumForMaxWidth();
        String[] strArrSplit = String.valueOf(getMaximum()).split("");
        StringBuilder sb = new StringBuilder();
        for (int i5 = 0; i5 < strArrSplit.length; i5++) {
            sb.append(numForMaxWidth);
        }
        this.mValueText.setWidth(Math.round(this.mValueText.getPaint().measureText(sb.toString())));
        super.onMeasure(i3, i4);
    }

    @Override // com.coui.appcompat.stepper.IStepper
    public void plus() {
        ObservableStep observableStep = this.mStep;
        observableStep.setStep(observableStep.getStep() + getUnit());
    }

    public void refresh() {
        TypedArray typedArrayObtainStyledAttributes = this.mContext.obtainStyledAttributes(null, R.styleable.COUIStepperView, 0, this.mStyle);
        if (typedArrayObtainStyledAttributes != null) {
            initAttr(typedArrayObtainStyledAttributes);
            typedArrayObtainStyledAttributes.recycle();
        }
    }

    public void release() {
        this.mPlusLongPressProxy.release();
        this.mMinusLongPressProxy.release();
        this.mStep.deleteObservers();
        this.mListener = null;
    }

    @Override // com.coui.appcompat.stepper.IStepper
    public void setCurStep(int i3) {
        this.mStep.setStep(i3);
    }

    @Override // com.coui.appcompat.stepper.IStepper
    public void setMaximum(int i3) {
        this.mStep.setMaximum(i3);
    }

    @Override // com.coui.appcompat.stepper.IStepper
    public void setMinimum(int i3) {
        this.mStep.setMinimum(i3);
    }

    @Override // com.coui.appcompat.stepper.IStepper
    public void setOnStepChangeListener(OnStepChangeListener onStepChangeListener) {
        this.mListener = onStepChangeListener;
    }

    @Override // com.coui.appcompat.stepper.IStepper
    public void setUnit(int i3) {
        this.mUnit = i3;
    }

    @Override // java.util.Observer
    public void update(Observable observable, Object obj) {
        int step = ((ObservableStep) observable).getStep();
        int iIntValue = ((Integer) obj).intValue();
        boolean z2 = false;
        this.mPlusImage.setEnabled(step < getMaximum() && isEnabled());
        ImageView imageView = this.mMinusImage;
        if (step > getMinimum() && isEnabled()) {
            z2 = true;
        }
        imageView.setEnabled(z2);
        this.mValueText.setText(String.valueOf(step));
        OnStepChangeListener onStepChangeListener = this.mListener;
        if (onStepChangeListener == null || step == iIntValue) {
            return;
        }
        onStepChangeListener.onStepChanged(step, iIntValue);
    }

    public COUIStepperView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiStepperViewStyle);
    }

    public COUIStepperView(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.TAG = "COUIStepperView";
        this.mPlusRunnable = new b(this);
        this.mMinusRunnable = new c(this);
        this.mContext = context;
        init(attributeSet, i3);
    }

    private void configEffectDrawableForButton(ImageView imageView, LongPressProxy longPressProxy) {
        float dimension = getContext().getResources().getDimension(R.dimen.stepper_button_size);
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        RectF rectF = new RectF(0.0f, 0.0f, dimension, dimension);
        shapeDrawable.getPaint().setColor(COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorContainer8));
        int i3 = (int) dimension;
        shapeDrawable.setBounds(0, 0, i3, i3);
        COUIMaskEffectDrawable cOUIMaskEffectDrawable = new COUIMaskEffectDrawable(getContext(), 0);
        float f3 = dimension / 2.0f;
        cOUIMaskEffectDrawable.setMaskRect(rectF, f3, f3);
        COUIStrokeDrawable cOUIStrokeDrawable = new COUIStrokeDrawable(getContext());
        cOUIStrokeDrawable.setStrokeRect(rectF, f3, f3);
        COUIStateEffectDrawable cOUIStateEffectDrawable = new COUIStateEffectDrawable(new Drawable[]{shapeDrawable, cOUIMaskEffectDrawable, cOUIStrokeDrawable});
        cOUIStateEffectDrawable.enableScaleEffect(imageView, 2);
        imageView.setBackground(cOUIStateEffectDrawable);
        longPressProxy.setOnTouchListener(new a(cOUIStateEffectDrawable));
    }
}