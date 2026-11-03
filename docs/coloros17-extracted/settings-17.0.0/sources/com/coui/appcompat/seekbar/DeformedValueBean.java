package com.coui.appcompat.seekbar;

/* JADX INFO: loaded from: classes7.dex */
public class DeformedValueBean {
    private float mDrawProgressScale;
    private float mHeightBottomDeformedDownValue;
    private float mHeightBottomDeformedUpValue;
    private float mHeightTopDeformedDownValue;
    private float mHeightTopDeformedUpValue;
    private int mProgress;
    private float mScale;
    private float mWidthDeformedValue;

    public DeformedValueBean(float f3, float f4, float f5, float f6, float f7, int i3) {
        this.mHeightBottomDeformedUpValue = f3;
        this.mHeightTopDeformedUpValue = f4;
        this.mWidthDeformedValue = f5;
        this.mHeightBottomDeformedDownValue = f6;
        this.mHeightTopDeformedDownValue = f7;
        this.mProgress = i3;
    }

    public float getDrawProgressScale() {
        return this.mDrawProgressScale;
    }

    public float getHeightBottomDeformedDownValue() {
        return this.mHeightBottomDeformedDownValue;
    }

    public float getHeightBottomDeformedUpValue() {
        return this.mHeightBottomDeformedUpValue;
    }

    public float getHeightTopDeformedDownValue() {
        return this.mHeightTopDeformedDownValue;
    }

    public float getHeightTopDeformedUpValue() {
        return this.mHeightTopDeformedUpValue;
    }

    public int getProgress() {
        return this.mProgress;
    }

    public float getScale() {
        return this.mScale;
    }

    public float getWidthDeformedValue() {
        return this.mWidthDeformedValue;
    }

    public void setDrawProgressScale(float f3) {
        this.mDrawProgressScale = f3;
    }

    public void setHeightBottomDeformedDownValue(float f3) {
        this.mHeightBottomDeformedDownValue = f3;
    }

    public void setHeightBottomDeformedUpValue(float f3) {
        this.mHeightBottomDeformedUpValue = f3;
    }

    public void setHeightTopDeformedDownValue(float f3) {
        this.mHeightTopDeformedDownValue = f3;
    }

    public void setHeightTopDeformedUpValue(float f3) {
        this.mHeightTopDeformedUpValue = f3;
    }

    public void setProgress(int i3) {
        this.mProgress = i3;
    }

    public void setScale(float f3) {
        this.mScale = f3;
    }

    public void setWidthDeformedValue(float f3) {
        this.mWidthDeformedValue = f3;
    }
}