package com.coui.appcompat.searchview.params.olivelink;

import com.coui.appcompat.searchview.params.StateChangedToNormalParams;
import com.coui.olivelink.ScalarData;
import com.coui.olivelink.SpringData;
import com.oplus.settings.feature.language.localepicker.RegionPickerUtils;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b$\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u000b\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BU\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0006\u0012\b\b\u0002\u0010\b\u001a\u00020\u0006\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003¢\u0006\u0002\u0010\fJ\t\u0010!\u001a\u00020\u0003HÆ\u0003J\t\u0010\"\u001a\u00020\u0003HÆ\u0003J\t\u0010#\u001a\u00020\u0006HÆ\u0003J\t\u0010$\u001a\u00020\u0006HÆ\u0003J\t\u0010%\u001a\u00020\u0006HÆ\u0003J\t\u0010&\u001a\u00020\u0003HÆ\u0003J\t\u0010'\u001a\u00020\u0003HÆ\u0003J\t\u0010(\u001a\u00020\u0003HÆ\u0003JY\u0010)\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u0003HÆ\u0001J\u0013\u0010*\u001a\u00020+2\b\u0010,\u001a\u0004\u0018\u00010-HÖ\u0003J\b\u0010.\u001a\u00020/H\u0016J\b\u00100\u001a\u00020/H\u0016J\b\u00101\u001a\u00020/H\u0016J\b\u00102\u001a\u00020/H\u0016J\b\u00103\u001a\u00020/H\u0016J\b\u00104\u001a\u00020/H\u0016J\b\u00105\u001a\u00020/H\u0016J\b\u00106\u001a\u00020/H\u0016J\b\u00107\u001a\u00020/H\u0016J\b\u00108\u001a\u00020/H\u0016J\b\u00109\u001a\u00020/H\u0016J\t\u0010:\u001a\u00020;HÖ\u0001J\t\u0010<\u001a\u00020=HÖ\u0001R\u001a\u0010\n\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0007\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u000e\"\u0004\b\u0016\u0010\u0010R\u001a\u0010\u000b\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u000e\"\u0004\b\u0018\u0010\u0010R\u001a\u0010\b\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0012\"\u0004\b\u001a\u0010\u0014R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u000e\"\u0004\b\u001c\u0010\u0010R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u0012\"\u0004\b\u001e\u0010\u0014R\u001a\u0010\t\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u000e\"\u0004\b \u0010\u0010¨\u0006>"}, d2 = {"Lcom/coui/appcompat/searchview/params/olivelink/StateChangedToNormalParamsHigh;", "Lcom/coui/appcompat/searchview/params/StateChangedToNormalParams;", "mFunctionalButtonEndScale", "Lcom/coui/olivelink/ScalarData;", "mOuterButtonInitScale", "mSpring", "Lcom/coui/olivelink/SpringData;", "mFunBtnSpring", "mOuterBtnSpring", "mStateSpringMinimumVisibleChange", "mFunBtnMinimumVisibleChange", "mOuterBtnMinimumVisibleChange", "(Lcom/coui/olivelink/ScalarData;Lcom/coui/olivelink/ScalarData;Lcom/coui/olivelink/SpringData;Lcom/coui/olivelink/SpringData;Lcom/coui/olivelink/SpringData;Lcom/coui/olivelink/ScalarData;Lcom/coui/olivelink/ScalarData;Lcom/coui/olivelink/ScalarData;)V", "getMFunBtnMinimumVisibleChange", "()Lcom/coui/olivelink/ScalarData;", "setMFunBtnMinimumVisibleChange", "(Lcom/coui/olivelink/ScalarData;)V", "getMFunBtnSpring", "()Lcom/coui/olivelink/SpringData;", "setMFunBtnSpring", "(Lcom/coui/olivelink/SpringData;)V", "getMFunctionalButtonEndScale", "setMFunctionalButtonEndScale", "getMOuterBtnMinimumVisibleChange", "setMOuterBtnMinimumVisibleChange", "getMOuterBtnSpring", "setMOuterBtnSpring", "getMOuterButtonInitScale", "setMOuterButtonInitScale", "getMSpring", "setMSpring", "getMStateSpringMinimumVisibleChange", "setMStateSpringMinimumVisibleChange", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "", RegionPickerUtils.OTHER_SYSTEM_REGION_KEY, "", "getBounce", "", "getFunBtnBounce", "getFunBtnMinimumVisibleChange", "getFunBtnResponse", "getFunctionalButtonEndScale", "getOuterBtnBounce", "getOuterBtnMinimumVisibleChange", "getOuterBtnResponse", "getOuterButtonInitScale", "getResponse", "getStateSpringMinimumVisibleChange", "hashCode", "", "toString", "", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final /* data */ class StateChangedToNormalParamsHigh implements StateChangedToNormalParams {
    private ScalarData mFunBtnMinimumVisibleChange;
    private SpringData mFunBtnSpring;
    private ScalarData mFunctionalButtonEndScale;
    private ScalarData mOuterBtnMinimumVisibleChange;
    private SpringData mOuterBtnSpring;
    private ScalarData mOuterButtonInitScale;
    private SpringData mSpring;
    private ScalarData mStateSpringMinimumVisibleChange;

    public StateChangedToNormalParamsHigh() {
        this(null, null, null, null, null, null, null, null, 255, null);
    }

    public static /* synthetic */ StateChangedToNormalParamsHigh copy$default(StateChangedToNormalParamsHigh stateChangedToNormalParamsHigh, ScalarData scalarData, ScalarData scalarData2, SpringData springData, SpringData springData2, SpringData springData3, ScalarData scalarData3, ScalarData scalarData4, ScalarData scalarData5, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            scalarData = stateChangedToNormalParamsHigh.mFunctionalButtonEndScale;
        }
        if ((i3 & 2) != 0) {
            scalarData2 = stateChangedToNormalParamsHigh.mOuterButtonInitScale;
        }
        if ((i3 & 4) != 0) {
            springData = stateChangedToNormalParamsHigh.mSpring;
        }
        if ((i3 & 8) != 0) {
            springData2 = stateChangedToNormalParamsHigh.mFunBtnSpring;
        }
        if ((i3 & 16) != 0) {
            springData3 = stateChangedToNormalParamsHigh.mOuterBtnSpring;
        }
        if ((i3 & 32) != 0) {
            scalarData3 = stateChangedToNormalParamsHigh.mStateSpringMinimumVisibleChange;
        }
        if ((i3 & 64) != 0) {
            scalarData4 = stateChangedToNormalParamsHigh.mFunBtnMinimumVisibleChange;
        }
        if ((i3 & 128) != 0) {
            scalarData5 = stateChangedToNormalParamsHigh.mOuterBtnMinimumVisibleChange;
        }
        ScalarData scalarData6 = scalarData4;
        ScalarData scalarData7 = scalarData5;
        SpringData springData4 = springData3;
        ScalarData scalarData8 = scalarData3;
        return stateChangedToNormalParamsHigh.copy(scalarData, scalarData2, springData, springData2, springData4, scalarData8, scalarData6, scalarData7);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final ScalarData getMFunctionalButtonEndScale() {
        return this.mFunctionalButtonEndScale;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final ScalarData getMOuterButtonInitScale() {
        return this.mOuterButtonInitScale;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final SpringData getMSpring() {
        return this.mSpring;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final SpringData getMFunBtnSpring() {
        return this.mFunBtnSpring;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final SpringData getMOuterBtnSpring() {
        return this.mOuterBtnSpring;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final ScalarData getMStateSpringMinimumVisibleChange() {
        return this.mStateSpringMinimumVisibleChange;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final ScalarData getMFunBtnMinimumVisibleChange() {
        return this.mFunBtnMinimumVisibleChange;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final ScalarData getMOuterBtnMinimumVisibleChange() {
        return this.mOuterBtnMinimumVisibleChange;
    }

    public final StateChangedToNormalParamsHigh copy(ScalarData mFunctionalButtonEndScale, ScalarData mOuterButtonInitScale, SpringData mSpring, SpringData mFunBtnSpring, SpringData mOuterBtnSpring, ScalarData mStateSpringMinimumVisibleChange, ScalarData mFunBtnMinimumVisibleChange, ScalarData mOuterBtnMinimumVisibleChange) {
        Intrinsics.checkNotNullParameter(mFunctionalButtonEndScale, "mFunctionalButtonEndScale");
        Intrinsics.checkNotNullParameter(mOuterButtonInitScale, "mOuterButtonInitScale");
        Intrinsics.checkNotNullParameter(mSpring, "mSpring");
        Intrinsics.checkNotNullParameter(mFunBtnSpring, "mFunBtnSpring");
        Intrinsics.checkNotNullParameter(mOuterBtnSpring, "mOuterBtnSpring");
        Intrinsics.checkNotNullParameter(mStateSpringMinimumVisibleChange, "mStateSpringMinimumVisibleChange");
        Intrinsics.checkNotNullParameter(mFunBtnMinimumVisibleChange, "mFunBtnMinimumVisibleChange");
        Intrinsics.checkNotNullParameter(mOuterBtnMinimumVisibleChange, "mOuterBtnMinimumVisibleChange");
        return new StateChangedToNormalParamsHigh(mFunctionalButtonEndScale, mOuterButtonInitScale, mSpring, mFunBtnSpring, mOuterBtnSpring, mStateSpringMinimumVisibleChange, mFunBtnMinimumVisibleChange, mOuterBtnMinimumVisibleChange);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof StateChangedToNormalParamsHigh)) {
            return false;
        }
        StateChangedToNormalParamsHigh stateChangedToNormalParamsHigh = (StateChangedToNormalParamsHigh) other;
        return Intrinsics.areEqual(this.mFunctionalButtonEndScale, stateChangedToNormalParamsHigh.mFunctionalButtonEndScale) && Intrinsics.areEqual(this.mOuterButtonInitScale, stateChangedToNormalParamsHigh.mOuterButtonInitScale) && Intrinsics.areEqual(this.mSpring, stateChangedToNormalParamsHigh.mSpring) && Intrinsics.areEqual(this.mFunBtnSpring, stateChangedToNormalParamsHigh.mFunBtnSpring) && Intrinsics.areEqual(this.mOuterBtnSpring, stateChangedToNormalParamsHigh.mOuterBtnSpring) && Intrinsics.areEqual(this.mStateSpringMinimumVisibleChange, stateChangedToNormalParamsHigh.mStateSpringMinimumVisibleChange) && Intrinsics.areEqual(this.mFunBtnMinimumVisibleChange, stateChangedToNormalParamsHigh.mFunBtnMinimumVisibleChange) && Intrinsics.areEqual(this.mOuterBtnMinimumVisibleChange, stateChangedToNormalParamsHigh.mOuterBtnMinimumVisibleChange);
    }

    @Override // com.coui.appcompat.searchview.params.StateChangedToNormalParams
    public float getBounce() {
        return this.mSpring.getBounce();
    }

    @Override // com.coui.appcompat.searchview.params.StateChangedToNormalParams
    public float getFunBtnBounce() {
        return this.mFunBtnSpring.getBounce();
    }

    @Override // com.coui.appcompat.searchview.params.StateChangedToNormalParams
    public float getFunBtnMinimumVisibleChange() {
        return this.mFunBtnMinimumVisibleChange.floatValue();
    }

    @Override // com.coui.appcompat.searchview.params.StateChangedToNormalParams
    public float getFunBtnResponse() {
        return this.mFunBtnSpring.getResponse();
    }

    @Override // com.coui.appcompat.searchview.params.StateChangedToNormalParams
    public float getFunctionalButtonEndScale() {
        return this.mFunctionalButtonEndScale.floatValue();
    }

    public final ScalarData getMFunBtnMinimumVisibleChange() {
        return this.mFunBtnMinimumVisibleChange;
    }

    public final SpringData getMFunBtnSpring() {
        return this.mFunBtnSpring;
    }

    public final ScalarData getMFunctionalButtonEndScale() {
        return this.mFunctionalButtonEndScale;
    }

    public final ScalarData getMOuterBtnMinimumVisibleChange() {
        return this.mOuterBtnMinimumVisibleChange;
    }

    public final SpringData getMOuterBtnSpring() {
        return this.mOuterBtnSpring;
    }

    public final ScalarData getMOuterButtonInitScale() {
        return this.mOuterButtonInitScale;
    }

    public final SpringData getMSpring() {
        return this.mSpring;
    }

    public final ScalarData getMStateSpringMinimumVisibleChange() {
        return this.mStateSpringMinimumVisibleChange;
    }

    @Override // com.coui.appcompat.searchview.params.StateChangedToNormalParams
    public float getOuterBtnBounce() {
        return this.mOuterBtnSpring.getBounce();
    }

    @Override // com.coui.appcompat.searchview.params.StateChangedToNormalParams
    public float getOuterBtnMinimumVisibleChange() {
        return this.mOuterBtnMinimumVisibleChange.floatValue();
    }

    @Override // com.coui.appcompat.searchview.params.StateChangedToNormalParams
    public float getOuterBtnResponse() {
        return this.mOuterBtnSpring.getResponse();
    }

    @Override // com.coui.appcompat.searchview.params.StateChangedToNormalParams
    public float getOuterButtonInitScale() {
        return this.mOuterButtonInitScale.floatValue();
    }

    @Override // com.coui.appcompat.searchview.params.StateChangedToNormalParams
    public float getResponse() {
        return this.mSpring.getResponse();
    }

    @Override // com.coui.appcompat.searchview.params.StateChangedToNormalParams
    public float getStateSpringMinimumVisibleChange() {
        return this.mStateSpringMinimumVisibleChange.floatValue();
    }

    public int hashCode() {
        return (((((((((((((this.mFunctionalButtonEndScale.hashCode() * 31) + this.mOuterButtonInitScale.hashCode()) * 31) + this.mSpring.hashCode()) * 31) + this.mFunBtnSpring.hashCode()) * 31) + this.mOuterBtnSpring.hashCode()) * 31) + this.mStateSpringMinimumVisibleChange.hashCode()) * 31) + this.mFunBtnMinimumVisibleChange.hashCode()) * 31) + this.mOuterBtnMinimumVisibleChange.hashCode();
    }

    public final void setMFunBtnMinimumVisibleChange(ScalarData scalarData) {
        Intrinsics.checkNotNullParameter(scalarData, "<set-?>");
        this.mFunBtnMinimumVisibleChange = scalarData;
    }

    public final void setMFunBtnSpring(SpringData springData) {
        Intrinsics.checkNotNullParameter(springData, "<set-?>");
        this.mFunBtnSpring = springData;
    }

    public final void setMFunctionalButtonEndScale(ScalarData scalarData) {
        Intrinsics.checkNotNullParameter(scalarData, "<set-?>");
        this.mFunctionalButtonEndScale = scalarData;
    }

    public final void setMOuterBtnMinimumVisibleChange(ScalarData scalarData) {
        Intrinsics.checkNotNullParameter(scalarData, "<set-?>");
        this.mOuterBtnMinimumVisibleChange = scalarData;
    }

    public final void setMOuterBtnSpring(SpringData springData) {
        Intrinsics.checkNotNullParameter(springData, "<set-?>");
        this.mOuterBtnSpring = springData;
    }

    public final void setMOuterButtonInitScale(ScalarData scalarData) {
        Intrinsics.checkNotNullParameter(scalarData, "<set-?>");
        this.mOuterButtonInitScale = scalarData;
    }

    public final void setMSpring(SpringData springData) {
        Intrinsics.checkNotNullParameter(springData, "<set-?>");
        this.mSpring = springData;
    }

    public final void setMStateSpringMinimumVisibleChange(ScalarData scalarData) {
        Intrinsics.checkNotNullParameter(scalarData, "<set-?>");
        this.mStateSpringMinimumVisibleChange = scalarData;
    }

    public String toString() {
        return "StateChangedToNormalParamsHigh(mFunctionalButtonEndScale=" + this.mFunctionalButtonEndScale + ", mOuterButtonInitScale=" + this.mOuterButtonInitScale + ", mSpring=" + this.mSpring + ", mFunBtnSpring=" + this.mFunBtnSpring + ", mOuterBtnSpring=" + this.mOuterBtnSpring + ", mStateSpringMinimumVisibleChange=" + this.mStateSpringMinimumVisibleChange + ", mFunBtnMinimumVisibleChange=" + this.mFunBtnMinimumVisibleChange + ", mOuterBtnMinimumVisibleChange=" + this.mOuterBtnMinimumVisibleChange + ')';
    }

    public StateChangedToNormalParamsHigh(ScalarData mFunctionalButtonEndScale, ScalarData mOuterButtonInitScale, SpringData mSpring, SpringData mFunBtnSpring, SpringData mOuterBtnSpring, ScalarData mStateSpringMinimumVisibleChange, ScalarData mFunBtnMinimumVisibleChange, ScalarData mOuterBtnMinimumVisibleChange) {
        Intrinsics.checkNotNullParameter(mFunctionalButtonEndScale, "mFunctionalButtonEndScale");
        Intrinsics.checkNotNullParameter(mOuterButtonInitScale, "mOuterButtonInitScale");
        Intrinsics.checkNotNullParameter(mSpring, "mSpring");
        Intrinsics.checkNotNullParameter(mFunBtnSpring, "mFunBtnSpring");
        Intrinsics.checkNotNullParameter(mOuterBtnSpring, "mOuterBtnSpring");
        Intrinsics.checkNotNullParameter(mStateSpringMinimumVisibleChange, "mStateSpringMinimumVisibleChange");
        Intrinsics.checkNotNullParameter(mFunBtnMinimumVisibleChange, "mFunBtnMinimumVisibleChange");
        Intrinsics.checkNotNullParameter(mOuterBtnMinimumVisibleChange, "mOuterBtnMinimumVisibleChange");
        this.mFunctionalButtonEndScale = mFunctionalButtonEndScale;
        this.mOuterButtonInitScale = mOuterButtonInitScale;
        this.mSpring = mSpring;
        this.mFunBtnSpring = mFunBtnSpring;
        this.mOuterBtnSpring = mOuterBtnSpring;
        this.mStateSpringMinimumVisibleChange = mStateSpringMinimumVisibleChange;
        this.mFunBtnMinimumVisibleChange = mFunBtnMinimumVisibleChange;
        this.mOuterBtnMinimumVisibleChange = mOuterBtnMinimumVisibleChange;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ StateChangedToNormalParamsHigh(ScalarData scalarData, ScalarData scalarData2, SpringData springData, SpringData springData2, SpringData springData3, ScalarData scalarData3, ScalarData scalarData4, ScalarData scalarData5, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        int i4 = i3 & 1;
        Float fValueOf = Float.valueOf(0.01f);
        Float fValueOf2 = Float.valueOf(0.5f);
        Float fValueOf3 = Float.valueOf(1.0f);
        Float fValueOf4 = Float.valueOf(0.0f);
        this(i4 != 0 ? new ScalarData("SearchBar-Common", "FunctionalButtonEndScale-全量-退出搜索", fValueOf2, "全量-退出搜索-功能按钮结束缩放值", "StateChangedToNormalHigh").min(fValueOf4).max(fValueOf3).step(fValueOf).build() : scalarData, (i3 & 2) != 0 ? new ScalarData("SearchBar-Common", "OuterButtonInitScale-全量-退出搜索", fValueOf2, "全量-退出搜索-外部按钮初始缩放值", "StateChangedToNormalHigh").min(fValueOf4).max(fValueOf3).step(fValueOf).build() : scalarData2, (i3 & 4) != 0 ? new SpringData("SearchBar-Common", "Spring-全量-退出搜索", "全量-退出搜索-弹簧动画参数", "StateChangedToNormalHigh").bounce(0.3f).response(0.6f).velocity(0.0f).build() : springData, (i3 & 8) != 0 ? new SpringData("SearchBar-Common", "FunBtnSpring-全量-退出搜索", "全量-退出搜索-功能按钮弹簧动画参数", "StateChangedToNormalHigh").bounce(0.3f).response(0.6f).velocity(0.0f).build() : springData2, (i3 & 16) != 0 ? new SpringData("SearchBar-Common", "OuterBtnSpring-全量-退出搜索", "全量-退出搜索-框外按钮弹簧动画参数", "StateChangedToNormalHigh").bounce(0.3f).response(0.6f).velocity(0.0f).build() : springData3, (i3 & 32) != 0 ? new ScalarData("SearchBar-Common", "StateSpringMinVisibleChange-全量-退出搜索", Float.valueOf(0.001f), "全量-退出搜索-主进度弹簧 MinimumVisibleChange", "StateChangedToNormalHigh").min(Float.valueOf(0.001f)).max(fValueOf3).step(Float.valueOf(0.001f)).build() : scalarData3, (i3 & 64) != 0 ? new ScalarData("SearchBar-Common", "FunBtnMinVisibleChange-全量-退出搜索", Float.valueOf(0.001f), "全量-退出搜索-功能按钮弹簧 MinimumVisibleChange", "StateChangedToNormalHigh").min(Float.valueOf(0.001f)).max(fValueOf3).step(Float.valueOf(0.001f)).build() : scalarData4, (i3 & 128) != 0 ? new ScalarData("SearchBar-Common", "OuterBtnMinVisibleChange-全量-退出搜索", Float.valueOf(0.001f), "全量-退出搜索-框外按钮弹簧 MinimumVisibleChange", "StateChangedToNormalHigh").min(Float.valueOf(0.001f)).max(fValueOf3).step(Float.valueOf(0.001f)).build() : scalarData5);
    }
}