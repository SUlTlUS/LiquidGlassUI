package com.coui.appcompat.searchview.params;

import com.oplus.settings.feature.language.localepicker.RegionPickerUtils;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b \n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\f\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BK\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003¢\u0006\u0002\u0010\nJ\t\u0010\u001b\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001c\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001d\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001f\u001a\u00020\u0003HÆ\u0003J\t\u0010 \u001a\u00020\u0003HÆ\u0003J\t\u0010!\u001a\u00020\u0003HÆ\u0003JO\u0010\"\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u0003HÆ\u0001J\u0013\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010&HÖ\u0003J\b\u0010'\u001a\u00020\u0003H\u0016J\b\u0010(\u001a\u00020\u0003H\u0016J\b\u0010)\u001a\u00020\u0003H\u0016J\b\u0010*\u001a\u00020\u0003H\u0016J\b\u0010+\u001a\u00020\u0003H\u0016J\b\u0010,\u001a\u00020\u0003H\u0016J\b\u0010-\u001a\u00020\u0003H\u0016J\b\u0010.\u001a\u00020\u0003H\u0016J\b\u0010/\u001a\u00020\u0003H\u0016J\b\u00100\u001a\u00020\u0003H\u0016J\b\u00101\u001a\u00020\u0003H\u0016J\t\u00102\u001a\u000203HÖ\u0001J\t\u00104\u001a\u000205HÖ\u0001R\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\b\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\f\"\u0004\b\u0010\u0010\u000eR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\f\"\u0004\b\u0012\u0010\u000eR\u001a\u0010\t\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\f\"\u0004\b\u0014\u0010\u000eR\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\f\"\u0004\b\u0016\u0010\u000eR\u001a\u0010\u0006\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\f\"\u0004\b\u0018\u0010\u000eR\u001a\u0010\u0007\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\f\"\u0004\b\u001a\u0010\u000e¨\u00066"}, d2 = {"Lcom/coui/appcompat/searchview/params/StateChangedToNormalParamsLow;", "Lcom/coui/appcompat/searchview/params/StateChangedToNormalParams;", "mFunctionalButtonEndScale", "", "mOuterButtonInitScale", "mBounce", "mResponse", "mStateSpringMinimumVisibleChange", "mFunBtnMinimumVisibleChange", "mOuterBtnMinimumVisibleChange", "(FFFFFFF)V", "getMBounce", "()F", "setMBounce", "(F)V", "getMFunBtnMinimumVisibleChange", "setMFunBtnMinimumVisibleChange", "getMFunctionalButtonEndScale", "setMFunctionalButtonEndScale", "getMOuterBtnMinimumVisibleChange", "setMOuterBtnMinimumVisibleChange", "getMOuterButtonInitScale", "setMOuterButtonInitScale", "getMResponse", "setMResponse", "getMStateSpringMinimumVisibleChange", "setMStateSpringMinimumVisibleChange", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "", RegionPickerUtils.OTHER_SYSTEM_REGION_KEY, "", "getBounce", "getFunBtnBounce", "getFunBtnMinimumVisibleChange", "getFunBtnResponse", "getFunctionalButtonEndScale", "getOuterBtnBounce", "getOuterBtnMinimumVisibleChange", "getOuterBtnResponse", "getOuterButtonInitScale", "getResponse", "getStateSpringMinimumVisibleChange", "hashCode", "", "toString", "", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final /* data */ class StateChangedToNormalParamsLow implements StateChangedToNormalParams {
    private float mBounce;
    private float mFunBtnMinimumVisibleChange;
    private float mFunctionalButtonEndScale;
    private float mOuterBtnMinimumVisibleChange;
    private float mOuterButtonInitScale;
    private float mResponse;
    private float mStateSpringMinimumVisibleChange;

    public StateChangedToNormalParamsLow() {
        this(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 127, null);
    }

    public static /* synthetic */ StateChangedToNormalParamsLow copy$default(StateChangedToNormalParamsLow stateChangedToNormalParamsLow, float f3, float f4, float f5, float f6, float f7, float f8, float f9, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            f3 = stateChangedToNormalParamsLow.mFunctionalButtonEndScale;
        }
        if ((i3 & 2) != 0) {
            f4 = stateChangedToNormalParamsLow.mOuterButtonInitScale;
        }
        if ((i3 & 4) != 0) {
            f5 = stateChangedToNormalParamsLow.mBounce;
        }
        if ((i3 & 8) != 0) {
            f6 = stateChangedToNormalParamsLow.mResponse;
        }
        if ((i3 & 16) != 0) {
            f7 = stateChangedToNormalParamsLow.mStateSpringMinimumVisibleChange;
        }
        if ((i3 & 32) != 0) {
            f8 = stateChangedToNormalParamsLow.mFunBtnMinimumVisibleChange;
        }
        if ((i3 & 64) != 0) {
            f9 = stateChangedToNormalParamsLow.mOuterBtnMinimumVisibleChange;
        }
        float f10 = f8;
        float f11 = f9;
        float f12 = f7;
        float f13 = f5;
        return stateChangedToNormalParamsLow.copy(f3, f4, f13, f6, f12, f10, f11);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final float getMFunctionalButtonEndScale() {
        return this.mFunctionalButtonEndScale;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final float getMOuterButtonInitScale() {
        return this.mOuterButtonInitScale;
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
    public final float getMStateSpringMinimumVisibleChange() {
        return this.mStateSpringMinimumVisibleChange;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final float getMFunBtnMinimumVisibleChange() {
        return this.mFunBtnMinimumVisibleChange;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final float getMOuterBtnMinimumVisibleChange() {
        return this.mOuterBtnMinimumVisibleChange;
    }

    public final StateChangedToNormalParamsLow copy(float mFunctionalButtonEndScale, float mOuterButtonInitScale, float mBounce, float mResponse, float mStateSpringMinimumVisibleChange, float mFunBtnMinimumVisibleChange, float mOuterBtnMinimumVisibleChange) {
        return new StateChangedToNormalParamsLow(mFunctionalButtonEndScale, mOuterButtonInitScale, mBounce, mResponse, mStateSpringMinimumVisibleChange, mFunBtnMinimumVisibleChange, mOuterBtnMinimumVisibleChange);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof StateChangedToNormalParamsLow)) {
            return false;
        }
        StateChangedToNormalParamsLow stateChangedToNormalParamsLow = (StateChangedToNormalParamsLow) other;
        return Float.compare(this.mFunctionalButtonEndScale, stateChangedToNormalParamsLow.mFunctionalButtonEndScale) == 0 && Float.compare(this.mOuterButtonInitScale, stateChangedToNormalParamsLow.mOuterButtonInitScale) == 0 && Float.compare(this.mBounce, stateChangedToNormalParamsLow.mBounce) == 0 && Float.compare(this.mResponse, stateChangedToNormalParamsLow.mResponse) == 0 && Float.compare(this.mStateSpringMinimumVisibleChange, stateChangedToNormalParamsLow.mStateSpringMinimumVisibleChange) == 0 && Float.compare(this.mFunBtnMinimumVisibleChange, stateChangedToNormalParamsLow.mFunBtnMinimumVisibleChange) == 0 && Float.compare(this.mOuterBtnMinimumVisibleChange, stateChangedToNormalParamsLow.mOuterBtnMinimumVisibleChange) == 0;
    }

    @Override // com.coui.appcompat.searchview.params.StateChangedToNormalParams
    public float getBounce() {
        return this.mBounce;
    }

    @Override // com.coui.appcompat.searchview.params.StateChangedToNormalParams
    public float getFunBtnBounce() {
        return 0.0f;
    }

    @Override // com.coui.appcompat.searchview.params.StateChangedToNormalParams
    public float getFunBtnMinimumVisibleChange() {
        return this.mFunBtnMinimumVisibleChange;
    }

    @Override // com.coui.appcompat.searchview.params.StateChangedToNormalParams
    public float getFunBtnResponse() {
        return 0.2f;
    }

    @Override // com.coui.appcompat.searchview.params.StateChangedToNormalParams
    public float getFunctionalButtonEndScale() {
        return this.mFunctionalButtonEndScale;
    }

    public final float getMBounce() {
        return this.mBounce;
    }

    public final float getMFunBtnMinimumVisibleChange() {
        return this.mFunBtnMinimumVisibleChange;
    }

    public final float getMFunctionalButtonEndScale() {
        return this.mFunctionalButtonEndScale;
    }

    public final float getMOuterBtnMinimumVisibleChange() {
        return this.mOuterBtnMinimumVisibleChange;
    }

    public final float getMOuterButtonInitScale() {
        return this.mOuterButtonInitScale;
    }

    public final float getMResponse() {
        return this.mResponse;
    }

    public final float getMStateSpringMinimumVisibleChange() {
        return this.mStateSpringMinimumVisibleChange;
    }

    @Override // com.coui.appcompat.searchview.params.StateChangedToNormalParams
    public float getOuterBtnBounce() {
        return 0.2f;
    }

    @Override // com.coui.appcompat.searchview.params.StateChangedToNormalParams
    public float getOuterBtnMinimumVisibleChange() {
        return this.mOuterBtnMinimumVisibleChange;
    }

    @Override // com.coui.appcompat.searchview.params.StateChangedToNormalParams
    public float getOuterBtnResponse() {
        return 0.45f;
    }

    @Override // com.coui.appcompat.searchview.params.StateChangedToNormalParams
    public float getOuterButtonInitScale() {
        return this.mOuterButtonInitScale;
    }

    @Override // com.coui.appcompat.searchview.params.StateChangedToNormalParams
    public float getResponse() {
        return this.mResponse;
    }

    @Override // com.coui.appcompat.searchview.params.StateChangedToNormalParams
    public float getStateSpringMinimumVisibleChange() {
        return this.mStateSpringMinimumVisibleChange;
    }

    public int hashCode() {
        return (((((((((((Float.hashCode(this.mFunctionalButtonEndScale) * 31) + Float.hashCode(this.mOuterButtonInitScale)) * 31) + Float.hashCode(this.mBounce)) * 31) + Float.hashCode(this.mResponse)) * 31) + Float.hashCode(this.mStateSpringMinimumVisibleChange)) * 31) + Float.hashCode(this.mFunBtnMinimumVisibleChange)) * 31) + Float.hashCode(this.mOuterBtnMinimumVisibleChange);
    }

    public final void setMBounce(float f3) {
        this.mBounce = f3;
    }

    public final void setMFunBtnMinimumVisibleChange(float f3) {
        this.mFunBtnMinimumVisibleChange = f3;
    }

    public final void setMFunctionalButtonEndScale(float f3) {
        this.mFunctionalButtonEndScale = f3;
    }

    public final void setMOuterBtnMinimumVisibleChange(float f3) {
        this.mOuterBtnMinimumVisibleChange = f3;
    }

    public final void setMOuterButtonInitScale(float f3) {
        this.mOuterButtonInitScale = f3;
    }

    public final void setMResponse(float f3) {
        this.mResponse = f3;
    }

    public final void setMStateSpringMinimumVisibleChange(float f3) {
        this.mStateSpringMinimumVisibleChange = f3;
    }

    public String toString() {
        return "StateChangedToNormalParamsLow(mFunctionalButtonEndScale=" + this.mFunctionalButtonEndScale + ", mOuterButtonInitScale=" + this.mOuterButtonInitScale + ", mBounce=" + this.mBounce + ", mResponse=" + this.mResponse + ", mStateSpringMinimumVisibleChange=" + this.mStateSpringMinimumVisibleChange + ", mFunBtnMinimumVisibleChange=" + this.mFunBtnMinimumVisibleChange + ", mOuterBtnMinimumVisibleChange=" + this.mOuterBtnMinimumVisibleChange + ')';
    }

    public StateChangedToNormalParamsLow(float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        this.mFunctionalButtonEndScale = f3;
        this.mOuterButtonInitScale = f4;
        this.mBounce = f5;
        this.mResponse = f6;
        this.mStateSpringMinimumVisibleChange = f7;
        this.mFunBtnMinimumVisibleChange = f8;
        this.mOuterBtnMinimumVisibleChange = f9;
    }

    public /* synthetic */ StateChangedToNormalParamsLow(float f3, float f4, float f5, float f6, float f7, float f8, float f9, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? 0.5f : f3, (i3 & 2) != 0 ? 0.5f : f4, (i3 & 4) != 0 ? 0.2f : f5, (i3 & 8) != 0 ? 0.45f : f6, (i3 & 16) != 0 ? 0.001f : f7, (i3 & 32) != 0 ? 0.001f : f8, (i3 & 64) != 0 ? 0.001f : f9);
    }
}