package com.coui.appcompat.searchview.params.olivelink;

import com.coui.appcompat.searchview.params.IconCollapseToNormalParams;
import com.coui.olivelink.ScalarData;
import com.coui.olivelink.SpringData;
import com.oplus.settings.feature.language.localepicker.RegionPickerUtils;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b \n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b\t\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BK\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0006\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003¢\u0006\u0002\u0010\u000bJ\t\u0010\u001e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001f\u001a\u00020\u0003HÆ\u0003J\t\u0010 \u001a\u00020\u0006HÆ\u0003J\t\u0010!\u001a\u00020\u0006HÆ\u0003J\t\u0010\"\u001a\u00020\u0003HÆ\u0003J\t\u0010#\u001a\u00020\u0003HÆ\u0003J\t\u0010$\u001a\u00020\u0003HÆ\u0003JO\u0010%\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u0003HÆ\u0001J\u0013\u0010&\u001a\u00020'2\b\u0010(\u001a\u0004\u0018\u00010)HÖ\u0003J\b\u0010*\u001a\u00020+H\u0016J\b\u0010,\u001a\u00020+H\u0016J\b\u0010-\u001a\u00020+H\u0016J\b\u0010.\u001a\u00020+H\u0016J\b\u0010/\u001a\u00020+H\u0016J\b\u00100\u001a\u00020+H\u0016J\b\u00101\u001a\u00020+H\u0016J\b\u00102\u001a\u00020+H\u0016J\b\u00103\u001a\u00020+H\u0016J\t\u00104\u001a\u000205HÖ\u0001J\t\u00106\u001a\u000207HÖ\u0001R\u001a\u0010\n\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\r\"\u0004\b\u0011\u0010\u000fR\u001a\u0010\t\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\r\"\u0004\b\u0013\u0010\u000fR\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001a\u0010\b\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\r\"\u0004\b\u0019\u0010\u000fR\u001a\u0010\u0007\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0015\"\u0004\b\u001b\u0010\u0017R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\r\"\u0004\b\u001d\u0010\u000f¨\u00068"}, d2 = {"Lcom/coui/appcompat/searchview/params/olivelink/IconCollapseToNormalParamsHigh;", "Lcom/coui/appcompat/searchview/params/IconCollapseToNormalParams;", "mToolbarInitScale", "Lcom/coui/olivelink/ScalarData;", "mFunctionalButtonEndScale", "mSpring", "Lcom/coui/olivelink/SpringData;", "mToolbarIconSpring", "mToolbarIconMinimumVisibleChange", "mIconExpandSpringMinimumVisibleChange", "mFadeSpringMinimumVisibleChange", "(Lcom/coui/olivelink/ScalarData;Lcom/coui/olivelink/ScalarData;Lcom/coui/olivelink/SpringData;Lcom/coui/olivelink/SpringData;Lcom/coui/olivelink/ScalarData;Lcom/coui/olivelink/ScalarData;Lcom/coui/olivelink/ScalarData;)V", "getMFadeSpringMinimumVisibleChange", "()Lcom/coui/olivelink/ScalarData;", "setMFadeSpringMinimumVisibleChange", "(Lcom/coui/olivelink/ScalarData;)V", "getMFunctionalButtonEndScale", "setMFunctionalButtonEndScale", "getMIconExpandSpringMinimumVisibleChange", "setMIconExpandSpringMinimumVisibleChange", "getMSpring", "()Lcom/coui/olivelink/SpringData;", "setMSpring", "(Lcom/coui/olivelink/SpringData;)V", "getMToolbarIconMinimumVisibleChange", "setMToolbarIconMinimumVisibleChange", "getMToolbarIconSpring", "setMToolbarIconSpring", "getMToolbarInitScale", "setMToolbarInitScale", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "", RegionPickerUtils.OTHER_SYSTEM_REGION_KEY, "", "getBounce", "", "getFadeSpringMinimumVisibleChange", "getFunctionalButtonEndScale", "getIconExpandSpringMinimumVisibleChange", "getResponse", "getToolbarIconMinimumVisibleChange", "getToolbarIconSpringBounce", "getToolbarIconSpringResponse", "getToolbarInitScale", "hashCode", "", "toString", "", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final /* data */ class IconCollapseToNormalParamsHigh implements IconCollapseToNormalParams {
    private ScalarData mFadeSpringMinimumVisibleChange;
    private ScalarData mFunctionalButtonEndScale;
    private ScalarData mIconExpandSpringMinimumVisibleChange;
    private SpringData mSpring;
    private ScalarData mToolbarIconMinimumVisibleChange;
    private SpringData mToolbarIconSpring;
    private ScalarData mToolbarInitScale;

    public IconCollapseToNormalParamsHigh() {
        this(null, null, null, null, null, null, null, 127, null);
    }

    public static /* synthetic */ IconCollapseToNormalParamsHigh copy$default(IconCollapseToNormalParamsHigh iconCollapseToNormalParamsHigh, ScalarData scalarData, ScalarData scalarData2, SpringData springData, SpringData springData2, ScalarData scalarData3, ScalarData scalarData4, ScalarData scalarData5, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            scalarData = iconCollapseToNormalParamsHigh.mToolbarInitScale;
        }
        if ((i3 & 2) != 0) {
            scalarData2 = iconCollapseToNormalParamsHigh.mFunctionalButtonEndScale;
        }
        if ((i3 & 4) != 0) {
            springData = iconCollapseToNormalParamsHigh.mSpring;
        }
        if ((i3 & 8) != 0) {
            springData2 = iconCollapseToNormalParamsHigh.mToolbarIconSpring;
        }
        if ((i3 & 16) != 0) {
            scalarData3 = iconCollapseToNormalParamsHigh.mToolbarIconMinimumVisibleChange;
        }
        if ((i3 & 32) != 0) {
            scalarData4 = iconCollapseToNormalParamsHigh.mIconExpandSpringMinimumVisibleChange;
        }
        if ((i3 & 64) != 0) {
            scalarData5 = iconCollapseToNormalParamsHigh.mFadeSpringMinimumVisibleChange;
        }
        ScalarData scalarData6 = scalarData4;
        ScalarData scalarData7 = scalarData5;
        ScalarData scalarData8 = scalarData3;
        SpringData springData3 = springData;
        return iconCollapseToNormalParamsHigh.copy(scalarData, scalarData2, springData3, springData2, scalarData8, scalarData6, scalarData7);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final ScalarData getMToolbarInitScale() {
        return this.mToolbarInitScale;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final ScalarData getMFunctionalButtonEndScale() {
        return this.mFunctionalButtonEndScale;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final SpringData getMSpring() {
        return this.mSpring;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final SpringData getMToolbarIconSpring() {
        return this.mToolbarIconSpring;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final ScalarData getMToolbarIconMinimumVisibleChange() {
        return this.mToolbarIconMinimumVisibleChange;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final ScalarData getMIconExpandSpringMinimumVisibleChange() {
        return this.mIconExpandSpringMinimumVisibleChange;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final ScalarData getMFadeSpringMinimumVisibleChange() {
        return this.mFadeSpringMinimumVisibleChange;
    }

    public final IconCollapseToNormalParamsHigh copy(ScalarData mToolbarInitScale, ScalarData mFunctionalButtonEndScale, SpringData mSpring, SpringData mToolbarIconSpring, ScalarData mToolbarIconMinimumVisibleChange, ScalarData mIconExpandSpringMinimumVisibleChange, ScalarData mFadeSpringMinimumVisibleChange) {
        Intrinsics.checkNotNullParameter(mToolbarInitScale, "mToolbarInitScale");
        Intrinsics.checkNotNullParameter(mFunctionalButtonEndScale, "mFunctionalButtonEndScale");
        Intrinsics.checkNotNullParameter(mSpring, "mSpring");
        Intrinsics.checkNotNullParameter(mToolbarIconSpring, "mToolbarIconSpring");
        Intrinsics.checkNotNullParameter(mToolbarIconMinimumVisibleChange, "mToolbarIconMinimumVisibleChange");
        Intrinsics.checkNotNullParameter(mIconExpandSpringMinimumVisibleChange, "mIconExpandSpringMinimumVisibleChange");
        Intrinsics.checkNotNullParameter(mFadeSpringMinimumVisibleChange, "mFadeSpringMinimumVisibleChange");
        return new IconCollapseToNormalParamsHigh(mToolbarInitScale, mFunctionalButtonEndScale, mSpring, mToolbarIconSpring, mToolbarIconMinimumVisibleChange, mIconExpandSpringMinimumVisibleChange, mFadeSpringMinimumVisibleChange);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof IconCollapseToNormalParamsHigh)) {
            return false;
        }
        IconCollapseToNormalParamsHigh iconCollapseToNormalParamsHigh = (IconCollapseToNormalParamsHigh) other;
        return Intrinsics.areEqual(this.mToolbarInitScale, iconCollapseToNormalParamsHigh.mToolbarInitScale) && Intrinsics.areEqual(this.mFunctionalButtonEndScale, iconCollapseToNormalParamsHigh.mFunctionalButtonEndScale) && Intrinsics.areEqual(this.mSpring, iconCollapseToNormalParamsHigh.mSpring) && Intrinsics.areEqual(this.mToolbarIconSpring, iconCollapseToNormalParamsHigh.mToolbarIconSpring) && Intrinsics.areEqual(this.mToolbarIconMinimumVisibleChange, iconCollapseToNormalParamsHigh.mToolbarIconMinimumVisibleChange) && Intrinsics.areEqual(this.mIconExpandSpringMinimumVisibleChange, iconCollapseToNormalParamsHigh.mIconExpandSpringMinimumVisibleChange) && Intrinsics.areEqual(this.mFadeSpringMinimumVisibleChange, iconCollapseToNormalParamsHigh.mFadeSpringMinimumVisibleChange);
    }

    @Override // com.coui.appcompat.searchview.params.IconCollapseToNormalParams
    public float getBounce() {
        return this.mSpring.getBounce();
    }

    @Override // com.coui.appcompat.searchview.params.IconCollapseToNormalParams
    public float getFadeSpringMinimumVisibleChange() {
        return this.mFadeSpringMinimumVisibleChange.floatValue();
    }

    @Override // com.coui.appcompat.searchview.params.IconCollapseToNormalParams
    public float getFunctionalButtonEndScale() {
        return this.mFunctionalButtonEndScale.floatValue();
    }

    @Override // com.coui.appcompat.searchview.params.IconCollapseToNormalParams
    public float getIconExpandSpringMinimumVisibleChange() {
        return this.mIconExpandSpringMinimumVisibleChange.floatValue();
    }

    public final ScalarData getMFadeSpringMinimumVisibleChange() {
        return this.mFadeSpringMinimumVisibleChange;
    }

    public final ScalarData getMFunctionalButtonEndScale() {
        return this.mFunctionalButtonEndScale;
    }

    public final ScalarData getMIconExpandSpringMinimumVisibleChange() {
        return this.mIconExpandSpringMinimumVisibleChange;
    }

    public final SpringData getMSpring() {
        return this.mSpring;
    }

    public final ScalarData getMToolbarIconMinimumVisibleChange() {
        return this.mToolbarIconMinimumVisibleChange;
    }

    public final SpringData getMToolbarIconSpring() {
        return this.mToolbarIconSpring;
    }

    public final ScalarData getMToolbarInitScale() {
        return this.mToolbarInitScale;
    }

    @Override // com.coui.appcompat.searchview.params.IconCollapseToNormalParams
    public float getResponse() {
        return this.mSpring.getResponse();
    }

    @Override // com.coui.appcompat.searchview.params.IconCollapseToNormalParams
    public float getToolbarIconMinimumVisibleChange() {
        return this.mToolbarIconMinimumVisibleChange.floatValue();
    }

    @Override // com.coui.appcompat.searchview.params.IconCollapseToNormalParams
    public float getToolbarIconSpringBounce() {
        return this.mToolbarIconSpring.getBounce();
    }

    @Override // com.coui.appcompat.searchview.params.IconCollapseToNormalParams
    public float getToolbarIconSpringResponse() {
        return this.mToolbarIconSpring.getResponse();
    }

    @Override // com.coui.appcompat.searchview.params.IconCollapseToNormalParams
    public float getToolbarInitScale() {
        return this.mToolbarInitScale.floatValue();
    }

    public int hashCode() {
        return (((((((((((this.mToolbarInitScale.hashCode() * 31) + this.mFunctionalButtonEndScale.hashCode()) * 31) + this.mSpring.hashCode()) * 31) + this.mToolbarIconSpring.hashCode()) * 31) + this.mToolbarIconMinimumVisibleChange.hashCode()) * 31) + this.mIconExpandSpringMinimumVisibleChange.hashCode()) * 31) + this.mFadeSpringMinimumVisibleChange.hashCode();
    }

    public final void setMFadeSpringMinimumVisibleChange(ScalarData scalarData) {
        Intrinsics.checkNotNullParameter(scalarData, "<set-?>");
        this.mFadeSpringMinimumVisibleChange = scalarData;
    }

    public final void setMFunctionalButtonEndScale(ScalarData scalarData) {
        Intrinsics.checkNotNullParameter(scalarData, "<set-?>");
        this.mFunctionalButtonEndScale = scalarData;
    }

    public final void setMIconExpandSpringMinimumVisibleChange(ScalarData scalarData) {
        Intrinsics.checkNotNullParameter(scalarData, "<set-?>");
        this.mIconExpandSpringMinimumVisibleChange = scalarData;
    }

    public final void setMSpring(SpringData springData) {
        Intrinsics.checkNotNullParameter(springData, "<set-?>");
        this.mSpring = springData;
    }

    public final void setMToolbarIconMinimumVisibleChange(ScalarData scalarData) {
        Intrinsics.checkNotNullParameter(scalarData, "<set-?>");
        this.mToolbarIconMinimumVisibleChange = scalarData;
    }

    public final void setMToolbarIconSpring(SpringData springData) {
        Intrinsics.checkNotNullParameter(springData, "<set-?>");
        this.mToolbarIconSpring = springData;
    }

    public final void setMToolbarInitScale(ScalarData scalarData) {
        Intrinsics.checkNotNullParameter(scalarData, "<set-?>");
        this.mToolbarInitScale = scalarData;
    }

    public String toString() {
        return "IconCollapseToNormalParamsHigh(mToolbarInitScale=" + this.mToolbarInitScale + ", mFunctionalButtonEndScale=" + this.mFunctionalButtonEndScale + ", mSpring=" + this.mSpring + ", mToolbarIconSpring=" + this.mToolbarIconSpring + ", mToolbarIconMinimumVisibleChange=" + this.mToolbarIconMinimumVisibleChange + ", mIconExpandSpringMinimumVisibleChange=" + this.mIconExpandSpringMinimumVisibleChange + ", mFadeSpringMinimumVisibleChange=" + this.mFadeSpringMinimumVisibleChange + ')';
    }

    public IconCollapseToNormalParamsHigh(ScalarData mToolbarInitScale, ScalarData mFunctionalButtonEndScale, SpringData mSpring, SpringData mToolbarIconSpring, ScalarData mToolbarIconMinimumVisibleChange, ScalarData mIconExpandSpringMinimumVisibleChange, ScalarData mFadeSpringMinimumVisibleChange) {
        Intrinsics.checkNotNullParameter(mToolbarInitScale, "mToolbarInitScale");
        Intrinsics.checkNotNullParameter(mFunctionalButtonEndScale, "mFunctionalButtonEndScale");
        Intrinsics.checkNotNullParameter(mSpring, "mSpring");
        Intrinsics.checkNotNullParameter(mToolbarIconSpring, "mToolbarIconSpring");
        Intrinsics.checkNotNullParameter(mToolbarIconMinimumVisibleChange, "mToolbarIconMinimumVisibleChange");
        Intrinsics.checkNotNullParameter(mIconExpandSpringMinimumVisibleChange, "mIconExpandSpringMinimumVisibleChange");
        Intrinsics.checkNotNullParameter(mFadeSpringMinimumVisibleChange, "mFadeSpringMinimumVisibleChange");
        this.mToolbarInitScale = mToolbarInitScale;
        this.mFunctionalButtonEndScale = mFunctionalButtonEndScale;
        this.mSpring = mSpring;
        this.mToolbarIconSpring = mToolbarIconSpring;
        this.mToolbarIconMinimumVisibleChange = mToolbarIconMinimumVisibleChange;
        this.mIconExpandSpringMinimumVisibleChange = mIconExpandSpringMinimumVisibleChange;
        this.mFadeSpringMinimumVisibleChange = mFadeSpringMinimumVisibleChange;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ IconCollapseToNormalParamsHigh(ScalarData scalarData, ScalarData scalarData2, SpringData springData, SpringData springData2, ScalarData scalarData3, ScalarData scalarData4, ScalarData scalarData5, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        int i4 = i3 & 1;
        Float fValueOf = Float.valueOf(0.01f);
        Float fValueOf2 = Float.valueOf(0.0f);
        Float fValueOf3 = Float.valueOf(0.5f);
        Float fValueOf4 = Float.valueOf(1.0f);
        this(i4 != 0 ? new ScalarData("SearchBar-Common", "ToolBarInitScale-全量-图标退出搜索", fValueOf3, "全量-退出搜索-搜索栏收起为图标-标题栏图标出现的缩放值", "IconCollapseToNormalHigh").min(fValueOf2).max(fValueOf4).step(fValueOf).build() : scalarData, (i3 & 2) != 0 ? new ScalarData("SearchBar-Common", "FunctionalButtonEndScale-全量-图标退出搜索", fValueOf3, "全量-退出搜索-搜索栏收起为图标-取消按钮缩放值", "IconCollapseToNormalHigh").min(fValueOf2).max(fValueOf4).step(fValueOf).build() : scalarData2, (i3 & 4) != 0 ? new SpringData("SearchBar-Common", "Spring-全量-图标退出搜索", "全量-退出搜索-搜索栏收起为图标-取消按钮缩放值", "IconCollapseToNormalHigh").bounce(0.1f).response(0.5f).velocity(0.0f).build() : springData, (i3 & 8) != 0 ? new SpringData("SearchBar-Common", "ToolBarIconSpring-全量-图标退出搜索", "全量-退出搜索-标题栏图标淡入/缩放独立弹簧", "IconCollapseToNormalHigh").bounce(0.1f).response(0.5f).velocity(0.0f).build() : springData2, (i3 & 16) != 0 ? new ScalarData("SearchBar-Common", "ToolBarIconMinVisibleChange-全量-图标退出搜索", Float.valueOf(0.001f), "全量-退出搜索-标题栏图标弹簧 MinimumVisibleChange", "IconCollapseToNormalHigh").min(Float.valueOf(0.001f)).max(fValueOf4).step(Float.valueOf(0.001f)).build() : scalarData3, (i3 & 32) != 0 ? new ScalarData("SearchBar-Common", "IconExpandSpringMinVisibleChange-全量-图标退出搜索", Float.valueOf(0.001f), "全量-退出搜索-图标位移动画弹簧 MinimumVisibleChange", "IconCollapseToNormalHigh").min(Float.valueOf(0.001f)).max(fValueOf4).step(Float.valueOf(0.001f)).build() : scalarData4, (i3 & 64) != 0 ? new ScalarData("SearchBar-Common", "FadeSpringMinVisibleChange-全量-图标退出搜索", Float.valueOf(0.001f), "全量-退出搜索-淡入淡出弹簧 MinimumVisibleChange", "IconCollapseToNormalHigh").min(Float.valueOf(0.001f)).max(fValueOf4).step(Float.valueOf(0.001f)).build() : scalarData5);
    }
}