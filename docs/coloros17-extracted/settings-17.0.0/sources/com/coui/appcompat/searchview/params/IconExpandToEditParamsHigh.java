package com.coui.appcompat.searchview.params;

import com.oplus.settings.feature.language.localepicker.RegionPickerUtils;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b(\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\n\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B_\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003¢\u0006\u0002\u0010\fJ\t\u0010!\u001a\u00020\u0003HÆ\u0003J\t\u0010\"\u001a\u00020\u0003HÆ\u0003J\t\u0010#\u001a\u00020\u0003HÆ\u0003J\t\u0010$\u001a\u00020\u0003HÆ\u0003J\t\u0010%\u001a\u00020\u0003HÆ\u0003J\t\u0010&\u001a\u00020\u0003HÆ\u0003J\t\u0010'\u001a\u00020\u0003HÆ\u0003J\t\u0010(\u001a\u00020\u0003HÆ\u0003J\t\u0010)\u001a\u00020\u0003HÆ\u0003Jc\u0010*\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u0003HÆ\u0001J\u0013\u0010+\u001a\u00020,2\b\u0010-\u001a\u0004\u0018\u00010.HÖ\u0003J\b\u0010/\u001a\u00020\u0003H\u0016J\b\u00100\u001a\u00020\u0003H\u0016J\b\u00101\u001a\u00020\u0003H\u0016J\b\u00102\u001a\u00020\u0003H\u0016J\b\u00103\u001a\u00020\u0003H\u0016J\b\u00104\u001a\u00020\u0003H\u0016J\b\u00105\u001a\u00020\u0003H\u0016J\b\u00106\u001a\u00020\u0003H\u0016J\b\u00107\u001a\u00020\u0003H\u0016J\t\u00108\u001a\u000209HÖ\u0001J\t\u0010:\u001a\u00020;HÖ\u0001R\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u000b\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u000e\"\u0004\b\u0012\u0010\u0010R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u000e\"\u0004\b\u0014\u0010\u0010R\u001a\u0010\n\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u000e\"\u0004\b\u0016\u0010\u0010R\u001a\u0010\u0006\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u000e\"\u0004\b\u0018\u0010\u0010R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u000e\"\u0004\b\u001a\u0010\u0010R\u001a\u0010\t\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u000e\"\u0004\b\u001c\u0010\u0010R\u001a\u0010\u0007\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u000e\"\u0004\b\u001e\u0010\u0010R\u001a\u0010\b\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u000e\"\u0004\b \u0010\u0010¨\u0006<"}, d2 = {"Lcom/coui/appcompat/searchview/params/IconExpandToEditParamsHigh;", "Lcom/coui/appcompat/searchview/params/IconExpandToEditParams;", "mToolbarEndScale", "", "mFunctionalButtonInitScale", "mBounce", "mResponse", "mToolbarIconSpringBounce", "mToolbarIconSpringResponse", "mToolbarIconMinimumVisibleChange", "mIconExpandSpringMinimumVisibleChange", "mFadeSpringMinimumVisibleChange", "(FFFFFFFFF)V", "getMBounce", "()F", "setMBounce", "(F)V", "getMFadeSpringMinimumVisibleChange", "setMFadeSpringMinimumVisibleChange", "getMFunctionalButtonInitScale", "setMFunctionalButtonInitScale", "getMIconExpandSpringMinimumVisibleChange", "setMIconExpandSpringMinimumVisibleChange", "getMResponse", "setMResponse", "getMToolbarEndScale", "setMToolbarEndScale", "getMToolbarIconMinimumVisibleChange", "setMToolbarIconMinimumVisibleChange", "getMToolbarIconSpringBounce", "setMToolbarIconSpringBounce", "getMToolbarIconSpringResponse", "setMToolbarIconSpringResponse", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", RegionPickerUtils.OTHER_SYSTEM_REGION_KEY, "", "getBounce", "getFadeSpringMinimumVisibleChange", "getFunctionalButtonInitScale", "getIconExpandSpringMinimumVisibleChange", "getResponse", "getToolbarEndScale", "getToolbarIconMinimumVisibleChange", "getToolbarIconSpringBounce", "getToolbarIconSpringResponse", "hashCode", "", "toString", "", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final /* data */ class IconExpandToEditParamsHigh implements IconExpandToEditParams {
    private float mBounce;
    private float mFadeSpringMinimumVisibleChange;
    private float mFunctionalButtonInitScale;
    private float mIconExpandSpringMinimumVisibleChange;
    private float mResponse;
    private float mToolbarEndScale;
    private float mToolbarIconMinimumVisibleChange;
    private float mToolbarIconSpringBounce;
    private float mToolbarIconSpringResponse;

    public IconExpandToEditParamsHigh() {
        this(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 511, null);
    }

    public static /* synthetic */ IconExpandToEditParamsHigh copy$default(IconExpandToEditParamsHigh iconExpandToEditParamsHigh, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            f3 = iconExpandToEditParamsHigh.mToolbarEndScale;
        }
        if ((i3 & 2) != 0) {
            f4 = iconExpandToEditParamsHigh.mFunctionalButtonInitScale;
        }
        if ((i3 & 4) != 0) {
            f5 = iconExpandToEditParamsHigh.mBounce;
        }
        if ((i3 & 8) != 0) {
            f6 = iconExpandToEditParamsHigh.mResponse;
        }
        if ((i3 & 16) != 0) {
            f7 = iconExpandToEditParamsHigh.mToolbarIconSpringBounce;
        }
        if ((i3 & 32) != 0) {
            f8 = iconExpandToEditParamsHigh.mToolbarIconSpringResponse;
        }
        if ((i3 & 64) != 0) {
            f9 = iconExpandToEditParamsHigh.mToolbarIconMinimumVisibleChange;
        }
        if ((i3 & 128) != 0) {
            f10 = iconExpandToEditParamsHigh.mIconExpandSpringMinimumVisibleChange;
        }
        if ((i3 & 256) != 0) {
            f11 = iconExpandToEditParamsHigh.mFadeSpringMinimumVisibleChange;
        }
        float f12 = f10;
        float f13 = f11;
        float f14 = f8;
        float f15 = f9;
        float f16 = f7;
        float f17 = f5;
        return iconExpandToEditParamsHigh.copy(f3, f4, f17, f6, f16, f14, f15, f12, f13);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final float getMToolbarEndScale() {
        return this.mToolbarEndScale;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final float getMFunctionalButtonInitScale() {
        return this.mFunctionalButtonInitScale;
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

    public final IconExpandToEditParamsHigh copy(float mToolbarEndScale, float mFunctionalButtonInitScale, float mBounce, float mResponse, float mToolbarIconSpringBounce, float mToolbarIconSpringResponse, float mToolbarIconMinimumVisibleChange, float mIconExpandSpringMinimumVisibleChange, float mFadeSpringMinimumVisibleChange) {
        return new IconExpandToEditParamsHigh(mToolbarEndScale, mFunctionalButtonInitScale, mBounce, mResponse, mToolbarIconSpringBounce, mToolbarIconSpringResponse, mToolbarIconMinimumVisibleChange, mIconExpandSpringMinimumVisibleChange, mFadeSpringMinimumVisibleChange);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof IconExpandToEditParamsHigh)) {
            return false;
        }
        IconExpandToEditParamsHigh iconExpandToEditParamsHigh = (IconExpandToEditParamsHigh) other;
        return Float.compare(this.mToolbarEndScale, iconExpandToEditParamsHigh.mToolbarEndScale) == 0 && Float.compare(this.mFunctionalButtonInitScale, iconExpandToEditParamsHigh.mFunctionalButtonInitScale) == 0 && Float.compare(this.mBounce, iconExpandToEditParamsHigh.mBounce) == 0 && Float.compare(this.mResponse, iconExpandToEditParamsHigh.mResponse) == 0 && Float.compare(this.mToolbarIconSpringBounce, iconExpandToEditParamsHigh.mToolbarIconSpringBounce) == 0 && Float.compare(this.mToolbarIconSpringResponse, iconExpandToEditParamsHigh.mToolbarIconSpringResponse) == 0 && Float.compare(this.mToolbarIconMinimumVisibleChange, iconExpandToEditParamsHigh.mToolbarIconMinimumVisibleChange) == 0 && Float.compare(this.mIconExpandSpringMinimumVisibleChange, iconExpandToEditParamsHigh.mIconExpandSpringMinimumVisibleChange) == 0 && Float.compare(this.mFadeSpringMinimumVisibleChange, iconExpandToEditParamsHigh.mFadeSpringMinimumVisibleChange) == 0;
    }

    @Override // com.coui.appcompat.searchview.params.IconExpandToEditParams
    public float getBounce() {
        return this.mBounce;
    }

    @Override // com.coui.appcompat.searchview.params.IconExpandToEditParams
    public float getFadeSpringMinimumVisibleChange() {
        return this.mFadeSpringMinimumVisibleChange;
    }

    @Override // com.coui.appcompat.searchview.params.IconExpandToEditParams
    public float getFunctionalButtonInitScale() {
        return this.mFunctionalButtonInitScale;
    }

    @Override // com.coui.appcompat.searchview.params.IconExpandToEditParams
    public float getIconExpandSpringMinimumVisibleChange() {
        return this.mIconExpandSpringMinimumVisibleChange;
    }

    public final float getMBounce() {
        return this.mBounce;
    }

    public final float getMFadeSpringMinimumVisibleChange() {
        return this.mFadeSpringMinimumVisibleChange;
    }

    public final float getMFunctionalButtonInitScale() {
        return this.mFunctionalButtonInitScale;
    }

    public final float getMIconExpandSpringMinimumVisibleChange() {
        return this.mIconExpandSpringMinimumVisibleChange;
    }

    public final float getMResponse() {
        return this.mResponse;
    }

    public final float getMToolbarEndScale() {
        return this.mToolbarEndScale;
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

    @Override // com.coui.appcompat.searchview.params.IconExpandToEditParams
    public float getResponse() {
        return this.mResponse;
    }

    @Override // com.coui.appcompat.searchview.params.IconExpandToEditParams
    public float getToolbarEndScale() {
        return this.mToolbarEndScale;
    }

    @Override // com.coui.appcompat.searchview.params.IconExpandToEditParams
    public float getToolbarIconMinimumVisibleChange() {
        return this.mToolbarIconMinimumVisibleChange;
    }

    @Override // com.coui.appcompat.searchview.params.IconExpandToEditParams
    public float getToolbarIconSpringBounce() {
        return this.mToolbarIconSpringBounce;
    }

    @Override // com.coui.appcompat.searchview.params.IconExpandToEditParams
    public float getToolbarIconSpringResponse() {
        return this.mToolbarIconSpringResponse;
    }

    public int hashCode() {
        return (((((((((((((((Float.hashCode(this.mToolbarEndScale) * 31) + Float.hashCode(this.mFunctionalButtonInitScale)) * 31) + Float.hashCode(this.mBounce)) * 31) + Float.hashCode(this.mResponse)) * 31) + Float.hashCode(this.mToolbarIconSpringBounce)) * 31) + Float.hashCode(this.mToolbarIconSpringResponse)) * 31) + Float.hashCode(this.mToolbarIconMinimumVisibleChange)) * 31) + Float.hashCode(this.mIconExpandSpringMinimumVisibleChange)) * 31) + Float.hashCode(this.mFadeSpringMinimumVisibleChange);
    }

    public final void setMBounce(float f3) {
        this.mBounce = f3;
    }

    public final void setMFadeSpringMinimumVisibleChange(float f3) {
        this.mFadeSpringMinimumVisibleChange = f3;
    }

    public final void setMFunctionalButtonInitScale(float f3) {
        this.mFunctionalButtonInitScale = f3;
    }

    public final void setMIconExpandSpringMinimumVisibleChange(float f3) {
        this.mIconExpandSpringMinimumVisibleChange = f3;
    }

    public final void setMResponse(float f3) {
        this.mResponse = f3;
    }

    public final void setMToolbarEndScale(float f3) {
        this.mToolbarEndScale = f3;
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

    public String toString() {
        return "IconExpandToEditParamsHigh(mToolbarEndScale=" + this.mToolbarEndScale + ", mFunctionalButtonInitScale=" + this.mFunctionalButtonInitScale + ", mBounce=" + this.mBounce + ", mResponse=" + this.mResponse + ", mToolbarIconSpringBounce=" + this.mToolbarIconSpringBounce + ", mToolbarIconSpringResponse=" + this.mToolbarIconSpringResponse + ", mToolbarIconMinimumVisibleChange=" + this.mToolbarIconMinimumVisibleChange + ", mIconExpandSpringMinimumVisibleChange=" + this.mIconExpandSpringMinimumVisibleChange + ", mFadeSpringMinimumVisibleChange=" + this.mFadeSpringMinimumVisibleChange + ')';
    }

    public IconExpandToEditParamsHigh(float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11) {
        this.mToolbarEndScale = f3;
        this.mFunctionalButtonInitScale = f4;
        this.mBounce = f5;
        this.mResponse = f6;
        this.mToolbarIconSpringBounce = f7;
        this.mToolbarIconSpringResponse = f8;
        this.mToolbarIconMinimumVisibleChange = f9;
        this.mIconExpandSpringMinimumVisibleChange = f10;
        this.mFadeSpringMinimumVisibleChange = f11;
    }

    public /* synthetic */ IconExpandToEditParamsHigh(float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? 0.8f : f3, (i3 & 2) != 0 ? 0.6f : f4, (i3 & 4) != 0 ? 0.2f : f5, (i3 & 8) != 0 ? 0.55f : f6, (i3 & 16) != 0 ? 0.3f : f7, (i3 & 32) != 0 ? 0.4f : f8, (i3 & 64) != 0 ? 0.001f : f9, (i3 & 128) != 0 ? 0.001f : f10, (i3 & 256) != 0 ? 0.001f : f11);
    }
}