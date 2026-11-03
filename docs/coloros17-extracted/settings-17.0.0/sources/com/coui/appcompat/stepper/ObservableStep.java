package com.coui.appcompat.stepper;

import java.util.Observable;

/* JADX INFO: loaded from: classes7.dex */
public class ObservableStep extends Observable {
    public static final int MAX_VALUE = 9999;
    public static final int MIN_VALUE = -999;
    private int mMax = MAX_VALUE;
    private int mMini = -999;
    private int mStep;

    public int getMaximum() {
        return this.mMax;
    }

    public int getMinimum() {
        return this.mMini;
    }

    public int getStep() {
        return this.mStep;
    }

    public void setMaximum(int i3) {
        if (i3 < this.mMini) {
            throw new IllegalArgumentException("maximum cannot be smaller than mMini");
        }
        if (i3 > 9999) {
            throw new IllegalArgumentException("maximum cannot be bigger than '9999'");
        }
        this.mMax = i3;
        if (this.mStep > i3) {
            setStep(i3);
        }
    }

    public void setMinimum(int i3) {
        if (i3 > this.mMax) {
            throw new IllegalArgumentException("minimum cannot be bigger than mMini");
        }
        if (i3 < -999) {
            throw new IllegalArgumentException("minimum cannot be smaller than '-999'");
        }
        this.mMini = i3;
        if (this.mStep < i3) {
            setStep(i3);
        }
    }

    public void setStep(int i3) {
        int iMin = Math.min(Math.max(i3, getMinimum()), getMaximum());
        int i4 = this.mStep;
        this.mStep = iMin;
        setChanged();
        notifyObservers(Integer.valueOf(i4));
    }
}