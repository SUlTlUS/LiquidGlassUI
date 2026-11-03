package com.coui.appcompat.stepper;

/* JADX INFO: loaded from: classes7.dex */
public interface IStepper {
    int getCurStep();

    int getMaximum();

    int getMinimum();

    int getUnit();

    void minus();

    void plus();

    void setCurStep(int i3);

    void setMaximum(int i3);

    void setMinimum(int i3);

    void setOnStepChangeListener(OnStepChangeListener onStepChangeListener);

    void setUnit(int i3);
}