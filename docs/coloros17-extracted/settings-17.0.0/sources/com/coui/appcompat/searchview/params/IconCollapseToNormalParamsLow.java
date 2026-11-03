package com.coui.appcompat.searchview.params;

import com.oplus.settings.feature.language.localepicker.RegionPickerUtils;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b(\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\n\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B_\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003¢\u0006\u0002\u0010\fJ\t\u0010!\u001a\u00020\u0003HÆ\u0003J\t\u0010\"\u001a\u00020\u0003HÆ\u0003J\t\u0010#\u001a\u00020\u0003HÆ\u0003J\t\u0010$\u001a\u00020\u0003HÆ\u0003J\t\u0010%\u001a\u00020\u0003HÆ\u0003J\t\u0010&\u001a\u00020\u0003HÆ\u0003J\t\u0010'\u001a\u00020\u0003HÆ\u0003J\t\u0010(\u001a\u00020\u0003HÆ\u0003J\t\u0010)\u001a\u00020\u0003HÆ\u0003Jc\u0010*\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u0003HÆ\u0001J\u0013\u0010+\u001a\u00020,2\b\u0010-\u001a\u0004\u0018\u00010.HÖ\u0003J\b\u0010/\u001a\u00020\u0003H\u0016J\b\u00100\u001a\u00020\u0003H\u0016J\b\u00101\u001a\u00020\u0003H\u0016J\b\u00102\u001a\u00020\u0003H\u0016J\b\u00103\u001a\u00020\u0003H\u0016J\b\u00104\u001a\u00020\u0003H\u0016J\b\u00105\u001a\u00020\u0003H\u0016J\b\u00106\u001a\u00020\u0003H\u0016J\b\u00107\u001a\u00020\u0003H\u0016J\t\u00108\u001a\u000209HÖ\u0001J\t\u0010:\u001a\u00020;HÖ\u0001R\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u000b\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u000e\"\u0004\b\u0012\u0010\u0010R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u000e\"\u0004\b\u0014\u0010\u0010R\u001a\u0010\n\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u000e\"\u0004\b\u0016\u0010\u0010R\u001a\u0010\u0006\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u000e\"\u0004\b\u0018\u0010\u0010R\u001a\u0010\t\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u000e\"\u0004\b\u001a\u0010\u0010R\u001a\u0010\u0007\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u000e\"\u0004\b\u001c\u0010\u0010R\u001a\u0010\b\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u000e\"\u0004\b\u001e\u0010\u0010R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u000e\"\u0004\b \u0010\u0010¨\u0006<"}, d2 = {"Lcom/coui/appcompat/searchview/params/IconCollapseToNormalParamsLow;", "Lcom/coui/appcompat/searchview/params/IconCollapseToNormalParams;", "mToolbarInitScale", "", "mFunctionalButtonEndScale", "mBounce", "mResponse", "mToolbarIconSpringBounce", "mToolbarIconSpringResponse", "mToolbarIconMinimumVisibleChange", "mIconExpandSpringMinimumVisibleChange", "mFadeSpringMinimumVisibleChange", "(FFFFFFFFF)V", "getMBounce", "()F", "setMBounce", "(F)V", "getMFadeSpringMinimumVisibleChange", "setMFadeSpringMinimumVisibleChange", "getMFunctionalButtonEndScale", "setMFunctionalButtonEndScale", "getMIconExpandSpringMinimumVisibleChange", "setMIconExpandSpringMinimumVisibleChange", "getMResponse", "setMResponse", "getMToolbarIconMinimumVisibleChange", "setMToolbarIconMinimumVisibleChange", "getMToolbarIconSpringBounce", "setMToolbarIconSpringBounce", "getMToolbarIconSpringResponse", "setMToolbarIconSpringResponse", "getMToolbarInitScale", "setMToolbarInitScale", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", RegionPickerUtils.OTHER_SYSTEM_REGION_KEY, "", "getBounce", "getFadeSpringMinimumVisibleChange", "getFunctionalButtonEndScale", "getIconExpandSpringMinimumVisibleChange", "getResponse", "getToolbarIconMinimumVisibleChange", "getToolbarIconSpringBounce", "getToolbarIconSpringResponse", "getToolbarInitScale", "hashCode", "", "toString", "", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final /* data */ class IconCollapseToNormalParamsLow implements IconCollapseToNormalParams {
    private float mBounce;
    private float mFadeSpringMinimumVisibleChange;
    private float mFunctionalButtonEndScale;
    private float mIconExpandSpringMinimumVisibleChange;
    private float mResponse;
    private float mToolbarIconMinimumVisibleChange;
    private float mToolbarIconSpringBounce;
    private float mToolbarIconSpringResponse;
    private float mToolbarInitScale;

    public IconCollapseToNormalParamsLow() {
        this(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 511, null);
    }

    public static /* synthetic */ IconCollapseToNormalParamsLow copy$default(IconCollapseToNormalParamsLow iconCollapseToNormalParamsLow, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            f3 = iconCollapseToNormalParamsLow.mToolbarInitScale;
        }
        if ((i3 & 2) != 0) {
            f4 = iconCollapseToNormalParamsLow.mFunctionalButtonEndScale;
        }
        if ((i3 & 4) != 0) {
            f5 = iconCollapseToNormalParamsLow.mBounce;
        }
        if ((i3 & 8) != 0) {
            f6 = iconCollapseToNormalParamsLow.mResponse;
        }
        if ((i3 & 16) != 0) {
            f7 = iconCollapseToNormalParamsLow.mToolbarIconSpringBounce;
        }
        if ((i3 & 32) != 0) {
            f8 = iconCollapseToNormalParamsLow.mToolbarIconSpringResponse;
        }
        if ((i3 & 64) != 0) {
            f9 = iconCollapseToNormalParamsLow.mToolbarIconMinimumVisibleChange;
        }
        if ((i3 & 128) != 0) {
            f10 = iconCollapseToNormalParamsLow.mIconExpandSpringMinimumVisibleChange;
        }
        if ((i3 & 256) != 0) {
            f11 = iconCollapseToNormalParamsLow.mFadeSpringMinimumVisibleChange;
        }
        float f12 = f10;
        float f13 = f11;
        float f14 = f8;
        float f15 = f9;
        float f16 = f7;
        float f17 = f5;
        return iconCollapseToNormalParamsLow.copy(f3, f4, f17, f6, f16, f14, f15, f12, f13);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final float getMToolbarInitScale() {
        return this.mToolbarInitScale;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final float getMFunctionalButtonEndScale() {
        return this.mFunctionalButtonEndScale;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final float getMBounce() {
        return this.mBounce;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final float getMResponse() {
        return this.mResponse;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final float getMToolbarIconSpringBounce() {
        return this.mToolbarIconSpringBounce;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final float getMToolbarIconSpringResponse() {
        return this.mToolbarIconSpringResponse;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final float getMToolbarIconMinimumVisibleChange() {
        return this.mToolbarIconMinimumVisibleChange;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final float getMIconExpandSpringMinimumVisibleChange() {
        return this.mIconExpandSpringMinimumVisibleChange;
    }

    /* JADX INFO: renamed from: component9, reason: from getter */
    public final float getMFadeSpringMinimumVisibleChange() {
        return this.mFadeSpringMinimumVisibleChange;
    }

    public final IconCollapseToNormalParamsLow copy(float mToolbarInitScale, float mFunctionalButtonEndScale, float mBounce, float mResponse, float mToolbarIconSpringBounce, float mToolbarIconSpringResponse, float mToolbarIconMinimumVisibleChange, float mIconExpandSpringMinimumVisibleChange, float mFadeSpringMinimumVisibleChange) {
        return new IconCollapseToNormalParamsLow(mToolbarInitScale, mFunctionalButtonEndScale, mBounce, mResponse, mToolbarIconSpringBounce, mToolbarIconSpringResponse, mToolbarIconMinimumVisibleChange, mIconExpandSpringMinimumVisibleChange, mFadeSpringMinimumVisibleChange);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof IconCollapseToNormalParamsLow)) {
            return false;
        }
        IconCollapseToNormalParamsLow iconCollapseToNormalParamsLow = (IconCollapseToNormalParamsLow) other;
        return Float.compare(this.mToolbarInitScale, iconCollapseToNormalParamsLow.mToolbarInitScale) == 0 && Float.compare(this.mFunctionalButtonEndScale, iconCollapseToNormalParamsLow.mFunctionalButtonEndScale) == 0 && Float.compare(this.mBounce, iconCollapseToNormalParamsLow.mBounce) == 0 && Float.compare(this.mResponse, iconCollapseToNormalParamsLow.mResponse) == 0 && Float.compare(this.mToolbarIconSpringBounce, iconCollapseToNormalParamsLow.mToolbarIconSpringBounce) == 0 && Float.compare(this.mToolbarIconSpringResponse, iconCollapseToNormalParamsLow.mToolbarIconSpringResponse) == 0 && Float.compare(this.mToolbarIconMinimumVisibleChange, iconCollapseToNormalParamsLow.mToolbarIconMinimumVisibleChange) == 0 && Float.compare(this.mIconExpandSpringMinimumVisibleChange, iconCollapseToNormalParamsLow.mIconExpandSpringMinimumVisibleChange) == 0 && Float.compare(this.mFadeSpringMinimumVisibleChange, iconCollapseToNormalParamsLow.mFadeSpringMinimumVisibleChange) == 0;
    }

    @Override // com.coui.appcompat.searchview.params.IconCollapseToNormalParams
    public float getBounce() {
        return this.mBounce;
    }

    @Override // com.coui.appcompat.searchview.params.IconCollapseToNormalParams
    public float getFadeSpringMinimumVisibleChange() {
        return this.mFadeSpringMinimumVisibleChange;
    }

    @Override // com.coui.appcompat.searchview.params.IconCollapseToNormalParams
    public float getFunctionalButtonEndScale() {
        return this.mFunctionalButtonEndScale;
    }

    @Override // com.coui.appcompat.searchview.params.IconCollapseToNormalParams
    public float getIconExpandSpringMinimumVisibleChange() {
        return this.mIconExpandSpringMinimumVisibleChange;
    }

    public final float getMBounce() {
        return this.mBounce;
    }

    public final float getMFadeSpringMinimumVisibleChange() {
        return this.mFadeSpringMinimumVisibleChange;
    }

    public final float getMFunctionalButtonEndScale() {
        return this.mFunctionalButtonEndScale;
    }

    public final float getMIconExpandSpringMinimumVisibleChange() {
        return this.mIconExpandSpringMinimumVisibleChange;
    }

    public final float getMResponse() {
        return this.mResponse;
    }

    public final float getMToolbarIconMinimumVisibleChange() {
        return this.mToolbarIconMinimumVisibleChange;
    }

    public final float getMToolbarIconSpringBounce() {
        return this.mToolbarIconSpringBounce;
    }

    public final float getMToolbarIconSpringResponse() {
        return this.mToolbarIconSpringResponse;
    }

    public final float getMToolbarInitScale() {
        return this.mToolbarInitScale;
    }

    @Override // com.coui.appcompat.searchview.params.IconCollapseToNormalParams
    public float getResponse() {
        return this.mResponse;
    }

    @Override // com.coui.appcompat.searchview.params.IconCollapseToNormalParams
    public float getToolbarIconMinimumVisibleChange() {
        return this.mToolbarIconMinimumVisibleChange;
    }

    @Override // com.coui.appcompat.searchview.params.IconCollapseToNormalParams
    public float getToolbarIconSpringBounce() {
        return this.mToolbarIconSpringBounce;
    }

    @Override // com.coui.appcompat.searchview.params.IconCollapseToNormalParams
    public float getToolbarIconSpringResponse() {
        return this.mToolbarIconSpringResponse;
    }

    @Override // com.coui.appcompat.searchview.params.IconCollapseToNormalParams
    public float getToolbarInitScale() {
        return this.mToolbarInitScale;
    }

    public int hashCode() {
        return (((((((((((((((Float.hashCode(this.mToolbarInitScale) * 31) + Float.hashCode(this.mFunctionalButtonEndScale)) * 31) + Float.hashCode(this.mBounce)) * 31) + Float.hashCode(this.mResponse)) * 31) + Float.hashCode(this.mToolbarIconSpringBounce)) * 31) + Float.hashCode(this.mToolbarIconSpringResponse)) * 31) + Float.hashCode(this.mToolbarIconMinimumVisibleChange)) * 31) + Float.hashCode(this.mIconExpandSpringMinimumVisibleChange)) * 31) + Float.hashCode(this.mFadeSpringMinimumVisibleChange);
    }

    public final void setMBounce(float f3) {
        this.mBounce = f3;
    }

    public final void setMFadeSpringMinimumVisibleChange(float f3) {
        this.mFadeSpringMinimumVisibleChange = f3;
    }

    public final void setMFunctionalButtonEndScale(float f3) {
        this.mFunctionalButtonEndScale = f3;
    }

    public final void setMIconExpandSpringMinimumVisibleChange(float f3) {
        this.mIconExpandSpringMinimumVisibleChange = f3;
    }

    public final void setMResponse(float f3) {
        this.mResponse = f3;
    }

    public final void setMToolbarIconMinimumVisibleChange(float f3) {
        this.mToolbarIconMinimumVisibleChange = f3;
    }

    public final void setMToolbarIconSpringBounce(float f3) {
        this.mToolbarIconSpringBounce = f3;
    }

    public final void setMToolbarIconSpringResponse(float f3) {
        this.mToolbarIconSpringResponse = f3;
    }

    public final void setMToolbarInitScale(float f3) {
        this.mToolbarInitScale = f3;
    }

    public String toString() {
        return "IconCollapseToNormalParamsLow(mToolbarInitScale=" + this.mToolbarInitScale + ", mFunctionalButtonEndScale=" + this.mFunctionalButtonEndScale + ", mBounce=" + this.mBounce + ", mResponse=" + this.mResponse + ", mToolbarIconSpringBounce=" + this.mToolbarIconSpringBounce + ", mToolbarIconSpringResponse=" + this.mToolbarIconSpringResponse + ", mToolbarIconMinimumVisibleChange=" + this.mToolbarIconMinimumVisibleChange + ", mIconExpandSpringMinimumVisibleChange=" + this.mIconExpandSpringMinimumVisibleChange + ", mFadeSpringMinimumVisibleChange=" + this.mFadeSpringMinimumVisibleChange + ')';
    }

    public IconCollapseToNormalParamsLow(float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11) {
        this.mToolbarInitScale = f3;
        this.mFunctionalButtonEndScale = f4;
        this.mBounce = f5;
        this.mResponse = f6;
        this.mToolbarIconSpringBounce = f7;
        this.mToolbarIconSpringResponse = f8;
        this.mToolbarIconMinimumVisibleChange = f9;
        this.mIconExpandSpringMinimumVisibleChange = f10;
        this.mFadeSpringMinimumVisibleChange = f11;
    }

    public /* synthetic */ IconCollapseToNormalParamsLow(float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? 0.5f : f3, (i3 & 2) != 0 ? 0.5f : f4, (i3 & 4) != 0 ? 0.0f : f5, (i3 & 8) != 0 ? 0.3f : f6, (i3 & 16) != 0 ? 0.0f : f7, (i3 & 32) != 0 ? 0.3f : f8, (i3 & 64) != 0 ? 0.001f : f9, (i3 & 128) != 0 ? 0.001f : f10, (i3 & 256) != 0 ? 0.001f : f11);
    }
}