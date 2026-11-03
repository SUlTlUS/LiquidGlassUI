package com.coui.appcompat.seekbar.effect;

import com.coui.appcompat.seekbar.listener.DeformedValueBean;
import kotlin.Metadata;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b \n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000 92\u00020\u0001:\u00019B\u0005¢\u0006\u0002\u0010\u0002J\u0006\u00100\u001a\u000201J\u0010\u00102\u001a\u0002012\b\u00103\u001a\u0004\u0018\u000104J\u001e\u00105\u001a\u0002042\u0006\u00106\u001a\u00020%2\u0006\u00107\u001a\u00020\u00042\u0006\u00108\u001a\u00020\u0004R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR\u001a\u0010\f\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u0006\"\u0004\b\u000e\u0010\bR\u001a\u0010\u000f\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0006\"\u0004\b\u0011\u0010\bR\u001a\u0010\u0012\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0006\"\u0004\b\u0014\u0010\bR\u001a\u0010\u0015\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0006\"\u0004\b\u0017\u0010\bR\u001a\u0010\u0018\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0006\"\u0004\b\u001a\u0010\bR\u001a\u0010\u001b\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u0006\"\u0004\b\u001d\u0010\bR\u001a\u0010\u001e\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u0006\"\u0004\b \u0010\bR\u001a\u0010!\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010\u0006\"\u0004\b#\u0010\bR\u001a\u0010$\u001a\u00020%X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b&\u0010'\"\u0004\b(\u0010)R\u001a\u0010*\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b+\u0010\u0006\"\u0004\b,\u0010\bR\u001a\u0010-\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b.\u0010\u0006\"\u0004\b/\u0010\b¨\u0006:"}, d2 = {"Lcom/coui/appcompat/seekbar/effect/DeformationEffectParams;", "Lcom/coui/appcompat/seekbar/effect/EffectParams;", "()V", "curBottomDeformationValue", "", "getCurBottomDeformationValue", "()F", "setCurBottomDeformationValue", "(F)V", "curTopDeformationValue", "getCurTopDeformationValue", "setCurTopDeformationValue", "flingDampingRatio", "getFlingDampingRatio", "setFlingDampingRatio", "flingFrequency", "getFlingFrequency", "setFlingFrequency", "flingLinearDamping", "getFlingLinearDamping", "setFlingLinearDamping", "heightBottomDeformedDownValue", "getHeightBottomDeformedDownValue", "setHeightBottomDeformedDownValue", "heightBottomDeformedUpValue", "getHeightBottomDeformedUpValue", "setHeightBottomDeformedUpValue", "heightTopDeformedDownValue", "getHeightTopDeformedDownValue", "setHeightTopDeformedDownValue", "heightTopDeformedUpValue", "getHeightTopDeformedUpValue", "setHeightTopDeformedUpValue", "maxHeightDeformedValue", "getMaxHeightDeformedValue", "setMaxHeightDeformedValue", "maxMovingDistance", "", "getMaxMovingDistance", "()I", "setMaxMovingDistance", "(I)V", "maxWidthDeformedValue", "getMaxWidthDeformedValue", "setMaxWidthDeformedValue", "widthDeformedValue", "getWidthDeformedValue", "setWidthDeformedValue", "resetDeformationValues", "", "setFromBean", "bean", "Lcom/coui/appcompat/seekbar/listener/DeformedValueBean;", "toBean", "progress", "scale", "drawProgressScale", "Companion", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class DeformationEffectParams extends EffectParams {
    public static final float DEFAULT_FLING_FREQUENCY = 2.8f;
    public static final float DEFAULT_FLING_LINEAR_DAMPING = 15.0f;
    public static final float DEFAULT_MAX_HEIGHT_DEFORMED_VALUE = 28.5f;
    public static final int DEFAULT_MAX_MOVING_DISTANCE = 30;
    public static final float DEFAULT_MAX_WIDTH_DEFORMED_VALUE = 4.7f;
    public static final int DEFORMATION_SCALE_FACTOR = 100000;
    public static final float DEFORMATION_SPRING_RESPONSE = 0.1f;
    public static final float SCALE_DEFORMATION_MAX = 2.0f;
    public static final float SCALE_DEFORMATION_MIN = -1.0f;
    public static final int SCALE_DEFORMATION_TIMES = 5;
    public static final float SPRING_BOUNCE = 0.0f;
    private float curBottomDeformationValue;
    private float curTopDeformationValue;
    private float heightBottomDeformedDownValue;
    private float heightBottomDeformedUpValue;
    private float heightTopDeformedDownValue;
    private float heightTopDeformedUpValue;
    private float widthDeformedValue;
    private int maxMovingDistance = 30;
    private float maxHeightDeformedValue = 28.5f;
    private float maxWidthDeformedValue = 4.7f;
    private float flingFrequency = 2.8f;
    private float flingDampingRatio = 1.0f;
    private float flingLinearDamping = 15.0f;

    public final float getCurBottomDeformationValue() {
        return this.curBottomDeformationValue;
    }

    public final float getCurTopDeformationValue() {
        return this.curTopDeformationValue;
    }

    public final float getFlingDampingRatio() {
        return this.flingDampingRatio;
    }

    public final float getFlingFrequency() {
        return this.flingFrequency;
    }

    public final float getFlingLinearDamping() {
        return this.flingLinearDamping;
    }

    public final float getHeightBottomDeformedDownValue() {
        return this.heightBottomDeformedDownValue;
    }

    public final float getHeightBottomDeformedUpValue() {
        return this.heightBottomDeformedUpValue;
    }

    public final float getHeightTopDeformedDownValue() {
        return this.heightTopDeformedDownValue;
    }

    public final float getHeightTopDeformedUpValue() {
        return this.heightTopDeformedUpValue;
    }

    public final float getMaxHeightDeformedValue() {
        return this.maxHeightDeformedValue;
    }

    public final int getMaxMovingDistance() {
        return this.maxMovingDistance;
    }

    public final float getMaxWidthDeformedValue() {
        return this.maxWidthDeformedValue;
    }

    public final float getWidthDeformedValue() {
        return this.widthDeformedValue;
    }

    public final void resetDeformationValues() {
        this.heightBottomDeformedUpValue = 0.0f;
        this.heightTopDeformedUpValue = 0.0f;
        this.widthDeformedValue = 0.0f;
        this.heightBottomDeformedDownValue = 0.0f;
        this.heightTopDeformedDownValue = 0.0f;
        this.curTopDeformationValue = 0.0f;
        this.curBottomDeformationValue = 0.0f;
    }

    public final void setCurBottomDeformationValue(float f3) {
        this.curBottomDeformationValue = f3;
    }

    public final void setCurTopDeformationValue(float f3) {
        this.curTopDeformationValue = f3;
    }

    public final void setFlingDampingRatio(float f3) {
        this.flingDampingRatio = f3;
    }

    public final void setFlingFrequency(float f3) {
        this.flingFrequency = f3;
    }

    public final void setFlingLinearDamping(float f3) {
        this.flingLinearDamping = f3;
    }

    public final void setFromBean(DeformedValueBean bean) {
        if (bean != null) {
            this.heightBottomDeformedUpValue = bean.getHeightBottomDeformedUpValue();
            this.heightTopDeformedUpValue = bean.getHeightTopDeformedUpValue();
            this.widthDeformedValue = bean.getWidthDeformedValue();
            this.heightBottomDeformedDownValue = bean.getHeightBottomDeformedDownValue();
            this.heightTopDeformedDownValue = bean.getHeightTopDeformedDownValue();
        }
    }

    public final void setHeightBottomDeformedDownValue(float f3) {
        this.heightBottomDeformedDownValue = f3;
    }

    public final void setHeightBottomDeformedUpValue(float f3) {
        this.heightBottomDeformedUpValue = f3;
    }

    public final void setHeightTopDeformedDownValue(float f3) {
        this.heightTopDeformedDownValue = f3;
    }

    public final void setHeightTopDeformedUpValue(float f3) {
        this.heightTopDeformedUpValue = f3;
    }

    public final void setMaxHeightDeformedValue(float f3) {
        this.maxHeightDeformedValue = f3;
    }

    public final void setMaxMovingDistance(int i3) {
        this.maxMovingDistance = i3;
    }

    public final void setMaxWidthDeformedValue(float f3) {
        this.maxWidthDeformedValue = f3;
    }

    public final void setWidthDeformedValue(float f3) {
        this.widthDeformedValue = f3;
    }

    public final DeformedValueBean toBean(int progress, float scale, float drawProgressScale) {
        return new DeformedValueBean(this.heightBottomDeformedUpValue, this.heightTopDeformedUpValue, this.widthDeformedValue, this.heightBottomDeformedDownValue, this.heightTopDeformedDownValue, progress, scale, drawProgressScale);
    }
}