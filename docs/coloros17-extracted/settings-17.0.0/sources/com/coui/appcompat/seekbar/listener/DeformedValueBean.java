package com.coui.appcompat.seekbar.listener;

import com.oplus.settings.feature.language.localepicker.RegionPickerUtils;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b!\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BU\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003¢\u0006\u0002\u0010\fJ\t\u0010!\u001a\u00020\u0003HÆ\u0003J\t\u0010\"\u001a\u00020\u0003HÆ\u0003J\t\u0010#\u001a\u00020\u0003HÆ\u0003J\t\u0010$\u001a\u00020\u0003HÆ\u0003J\t\u0010%\u001a\u00020\u0003HÆ\u0003J\t\u0010&\u001a\u00020\tHÆ\u0003J\t\u0010'\u001a\u00020\u0003HÆ\u0003J\t\u0010(\u001a\u00020\u0003HÆ\u0003JY\u0010)\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u0003HÆ\u0001J\u0013\u0010*\u001a\u00020+2\b\u0010,\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010-\u001a\u00020\tHÖ\u0001J\t\u0010.\u001a\u00020/HÖ\u0001R\u001a\u0010\u000b\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0006\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u000e\"\u0004\b\u0012\u0010\u0010R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u000e\"\u0004\b\u0014\u0010\u0010R\u001a\u0010\u0007\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u000e\"\u0004\b\u0016\u0010\u0010R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u000e\"\u0004\b\u0018\u0010\u0010R\u001a\u0010\b\u001a\u00020\tX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u001a\u0010\n\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u000e\"\u0004\b\u001e\u0010\u0010R\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u000e\"\u0004\b \u0010\u0010¨\u00060"}, d2 = {"Lcom/coui/appcompat/seekbar/listener/DeformedValueBean;", "", "heightBottomDeformedUpValue", "", "heightTopDeformedUpValue", "widthDeformedValue", "heightBottomDeformedDownValue", "heightTopDeformedDownValue", "progress", "", "scale", "drawProgressScale", "(FFFFFIFF)V", "getDrawProgressScale", "()F", "setDrawProgressScale", "(F)V", "getHeightBottomDeformedDownValue", "setHeightBottomDeformedDownValue", "getHeightBottomDeformedUpValue", "setHeightBottomDeformedUpValue", "getHeightTopDeformedDownValue", "setHeightTopDeformedDownValue", "getHeightTopDeformedUpValue", "setHeightTopDeformedUpValue", "getProgress", "()I", "setProgress", "(I)V", "getScale", "setScale", "getWidthDeformedValue", "setWidthDeformedValue", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "", RegionPickerUtils.OTHER_SYSTEM_REGION_KEY, "hashCode", "toString", "", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final /* data */ class DeformedValueBean {
    private float drawProgressScale;
    private float heightBottomDeformedDownValue;
    private float heightBottomDeformedUpValue;
    private float heightTopDeformedDownValue;
    private float heightTopDeformedUpValue;
    private int progress;
    private float scale;
    private float widthDeformedValue;

    public DeformedValueBean() {
        this(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0, 0.0f, 0.0f, 255, null);
    }

    public static /* synthetic */ DeformedValueBean copy$default(DeformedValueBean deformedValueBean, float f3, float f4, float f5, float f6, float f7, int i3, float f8, float f9, int i4, Object obj) {
        if ((i4 & 1) != 0) {
            f3 = deformedValueBean.heightBottomDeformedUpValue;
        }
        if ((i4 & 2) != 0) {
            f4 = deformedValueBean.heightTopDeformedUpValue;
        }
        if ((i4 & 4) != 0) {
            f5 = deformedValueBean.widthDeformedValue;
        }
        if ((i4 & 8) != 0) {
            f6 = deformedValueBean.heightBottomDeformedDownValue;
        }
        if ((i4 & 16) != 0) {
            f7 = deformedValueBean.heightTopDeformedDownValue;
        }
        if ((i4 & 32) != 0) {
            i3 = deformedValueBean.progress;
        }
        if ((i4 & 64) != 0) {
            f8 = deformedValueBean.scale;
        }
        if ((i4 & 128) != 0) {
            f9 = deformedValueBean.drawProgressScale;
        }
        float f10 = f8;
        float f11 = f9;
        float f12 = f7;
        int i5 = i3;
        return deformedValueBean.copy(f3, f4, f5, f6, f12, i5, f10, f11);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final float getHeightBottomDeformedUpValue() {
        return this.heightBottomDeformedUpValue;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final float getHeightTopDeformedUpValue() {
        return this.heightTopDeformedUpValue;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final float getWidthDeformedValue() {
        return this.widthDeformedValue;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final float getHeightBottomDeformedDownValue() {
        return this.heightBottomDeformedDownValue;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final float getHeightTopDeformedDownValue() {
        return this.heightTopDeformedDownValue;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final int getProgress() {
        return this.progress;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final float getScale() {
        return this.scale;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final float getDrawProgressScale() {
        return this.drawProgressScale;
    }

    public final DeformedValueBean copy(float heightBottomDeformedUpValue, float heightTopDeformedUpValue, float widthDeformedValue, float heightBottomDeformedDownValue, float heightTopDeformedDownValue, int progress, float scale, float drawProgressScale) {
        return new DeformedValueBean(heightBottomDeformedUpValue, heightTopDeformedUpValue, widthDeformedValue, heightBottomDeformedDownValue, heightTopDeformedDownValue, progress, scale, drawProgressScale);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof DeformedValueBean)) {
            return false;
        }
        DeformedValueBean deformedValueBean = (DeformedValueBean) other;
        return Float.compare(this.heightBottomDeformedUpValue, deformedValueBean.heightBottomDeformedUpValue) == 0 && Float.compare(this.heightTopDeformedUpValue, deformedValueBean.heightTopDeformedUpValue) == 0 && Float.compare(this.widthDeformedValue, deformedValueBean.widthDeformedValue) == 0 && Float.compare(this.heightBottomDeformedDownValue, deformedValueBean.heightBottomDeformedDownValue) == 0 && Float.compare(this.heightTopDeformedDownValue, deformedValueBean.heightTopDeformedDownValue) == 0 && this.progress == deformedValueBean.progress && Float.compare(this.scale, deformedValueBean.scale) == 0 && Float.compare(this.drawProgressScale, deformedValueBean.drawProgressScale) == 0;
    }

    public final float getDrawProgressScale() {
        return this.drawProgressScale;
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

    public final int getProgress() {
        return this.progress;
    }

    public final float getScale() {
        return this.scale;
    }

    public final float getWidthDeformedValue() {
        return this.widthDeformedValue;
    }

    public int hashCode() {
        return (((((((((((((Float.hashCode(this.heightBottomDeformedUpValue) * 31) + Float.hashCode(this.heightTopDeformedUpValue)) * 31) + Float.hashCode(this.widthDeformedValue)) * 31) + Float.hashCode(this.heightBottomDeformedDownValue)) * 31) + Float.hashCode(this.heightTopDeformedDownValue)) * 31) + Integer.hashCode(this.progress)) * 31) + Float.hashCode(this.scale)) * 31) + Float.hashCode(this.drawProgressScale);
    }

    public final void setDrawProgressScale(float f3) {
        this.drawProgressScale = f3;
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

    public final void setProgress(int i3) {
        this.progress = i3;
    }

    public final void setScale(float f3) {
        this.scale = f3;
    }

    public final void setWidthDeformedValue(float f3) {
        this.widthDeformedValue = f3;
    }

    public String toString() {
        return "DeformedValueBean(heightBottomDeformedUpValue=" + this.heightBottomDeformedUpValue + ", heightTopDeformedUpValue=" + this.heightTopDeformedUpValue + ", widthDeformedValue=" + this.widthDeformedValue + ", heightBottomDeformedDownValue=" + this.heightBottomDeformedDownValue + ", heightTopDeformedDownValue=" + this.heightTopDeformedDownValue + ", progress=" + this.progress + ", scale=" + this.scale + ", drawProgressScale=" + this.drawProgressScale + ')';
    }

    public DeformedValueBean(float f3, float f4, float f5, float f6, float f7, int i3, float f8, float f9) {
        this.heightBottomDeformedUpValue = f3;
        this.heightTopDeformedUpValue = f4;
        this.widthDeformedValue = f5;
        this.heightBottomDeformedDownValue = f6;
        this.heightTopDeformedDownValue = f7;
        this.progress = i3;
        this.scale = f8;
        this.drawProgressScale = f9;
    }

    public /* synthetic */ DeformedValueBean(float f3, float f4, float f5, float f6, float f7, int i3, float f8, float f9, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this((i4 & 1) != 0 ? 0.0f : f3, (i4 & 2) != 0 ? 0.0f : f4, (i4 & 4) != 0 ? 0.0f : f5, (i4 & 8) != 0 ? 0.0f : f6, (i4 & 16) != 0 ? 0.0f : f7, (i4 & 32) != 0 ? 0 : i3, (i4 & 64) != 0 ? 0.0f : f8, (i4 & 128) != 0 ? 0.0f : f9);
    }
}