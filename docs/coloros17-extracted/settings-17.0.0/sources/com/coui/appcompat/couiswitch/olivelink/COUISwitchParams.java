package com.coui.appcompat.couiswitch.olivelink;

import com.coui.iola.SpringData;
import com.oplus.settings.feature.language.localepicker.RegionPickerUtils;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b)\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\b\u0018\u0000 52\u00020\u0001:\u00015BK\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0005\u0012\b\b\u0002\u0010\b\u001a\u00020\u0005\u0012\b\b\u0002\u0010\t\u001a\u00020\u0005\u0012\b\b\u0002\u0010\n\u001a\u00020\u0005¢\u0006\u0002\u0010\u000bJ\t\u0010&\u001a\u00020\u0003HÆ\u0003J\t\u0010'\u001a\u00020\u0005HÆ\u0003J\t\u0010(\u001a\u00020\u0005HÆ\u0003J\t\u0010)\u001a\u00020\u0005HÆ\u0003J\t\u0010*\u001a\u00020\u0005HÆ\u0003J\t\u0010+\u001a\u00020\u0005HÆ\u0003J\t\u0010,\u001a\u00020\u0005HÆ\u0003JO\u0010-\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\u00052\b\b\u0002\u0010\t\u001a\u00020\u00052\b\b\u0002\u0010\n\u001a\u00020\u0005HÆ\u0001J\u0013\u0010.\u001a\u00020/2\b\u00100\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u00101\u001a\u000202HÖ\u0001J\t\u00103\u001a\u000204HÖ\u0001R \u0010\b\u001a\u00020\u0005X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\f\u0010\r\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R \u0010\u0006\u001a\u00020\u0005X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u0012\u0010\r\u001a\u0004\b\u0013\u0010\u000f\"\u0004\b\u0014\u0010\u0011R \u0010\u0007\u001a\u00020\u0005X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u0015\u0010\r\u001a\u0004\b\u0016\u0010\u000f\"\u0004\b\u0017\u0010\u0011R \u0010\u0004\u001a\u00020\u0005X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u0018\u0010\r\u001a\u0004\b\u0019\u0010\u000f\"\u0004\b\u001a\u0010\u0011R \u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u001b\u0010\r\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR \u0010\n\u001a\u00020\u0005X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b \u0010\r\u001a\u0004\b!\u0010\u000f\"\u0004\b\"\u0010\u0011R \u0010\t\u001a\u00020\u0005X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b#\u0010\r\u001a\u0004\b$\u0010\u000f\"\u0004\b%\u0010\u0011¨\u00066"}, d2 = {"Lcom/coui/appcompat/couiswitch/olivelink/COUISwitchParams;", "", "toggleSpring", "Lcom/coui/iola/SpringData;", "responseDrag", "", "bounceDrag", "responseAttach", "bounceAttach", "transformVelocity", "transformDistance", "(Lcom/coui/iola/SpringData;FFFFFF)V", "getBounceAttach$annotations", "()V", "getBounceAttach", "()F", "setBounceAttach", "(F)V", "getBounceDrag$annotations", "getBounceDrag", "setBounceDrag", "getResponseAttach$annotations", "getResponseAttach", "setResponseAttach", "getResponseDrag$annotations", "getResponseDrag", "setResponseDrag", "getToggleSpring$annotations", "getToggleSpring", "()Lcom/coui/iola/SpringData;", "setToggleSpring", "(Lcom/coui/iola/SpringData;)V", "getTransformDistance$annotations", "getTransformDistance", "setTransformDistance", "getTransformVelocity$annotations", "getTransformVelocity", "setTransformVelocity", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "", RegionPickerUtils.OTHER_SYSTEM_REGION_KEY, "hashCode", "", "toString", "", "Companion", "coui-support-core_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final /* data */ class COUISwitchParams {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private float bounceAttach;
    private float bounceDrag;
    private float responseAttach;
    private float responseDrag;
    private SpringData toggleSpring;
    private float transformDistance;
    private float transformVelocity;

    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lcom/coui/appcompat/couiswitch/olivelink/COUISwitchParams$Companion;", "", "()V", "coui-support-core_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public COUISwitchParams() {
        this(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 127, null);
    }

    public static /* synthetic */ COUISwitchParams copy$default(COUISwitchParams cOUISwitchParams, SpringData springData, float f3, float f4, float f5, float f6, float f7, float f8, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            springData = cOUISwitchParams.toggleSpring;
        }
        if ((i3 & 2) != 0) {
            f3 = cOUISwitchParams.responseDrag;
        }
        if ((i3 & 4) != 0) {
            f4 = cOUISwitchParams.bounceDrag;
        }
        if ((i3 & 8) != 0) {
            f5 = cOUISwitchParams.responseAttach;
        }
        if ((i3 & 16) != 0) {
            f6 = cOUISwitchParams.bounceAttach;
        }
        if ((i3 & 32) != 0) {
            f7 = cOUISwitchParams.transformVelocity;
        }
        if ((i3 & 64) != 0) {
            f8 = cOUISwitchParams.transformDistance;
        }
        float f9 = f7;
        float f10 = f8;
        float f11 = f6;
        float f12 = f4;
        return cOUISwitchParams.copy(springData, f3, f12, f5, f11, f9, f10);
    }

    public static /* synthetic */ void getBounceAttach$annotations() {
    }

    public static /* synthetic */ void getBounceDrag$annotations() {
    }

    public static /* synthetic */ void getResponseAttach$annotations() {
    }

    public static /* synthetic */ void getResponseDrag$annotations() {
    }

    public static /* synthetic */ void getToggleSpring$annotations() {
    }

    public static /* synthetic */ void getTransformDistance$annotations() {
    }

    public static /* synthetic */ void getTransformVelocity$annotations() {
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final SpringData getToggleSpring() {
        return this.toggleSpring;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final float getResponseDrag() {
        return this.responseDrag;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final float getBounceDrag() {
        return this.bounceDrag;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final float getResponseAttach() {
        return this.responseAttach;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final float getBounceAttach() {
        return this.bounceAttach;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final float getTransformVelocity() {
        return this.transformVelocity;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final float getTransformDistance() {
        return this.transformDistance;
    }

    public final COUISwitchParams copy(SpringData toggleSpring, float responseDrag, float bounceDrag, float responseAttach, float bounceAttach, float transformVelocity, float transformDistance) {
        Intrinsics.checkNotNullParameter(toggleSpring, "toggleSpring");
        return new COUISwitchParams(toggleSpring, responseDrag, bounceDrag, responseAttach, bounceAttach, transformVelocity, transformDistance);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof COUISwitchParams)) {
            return false;
        }
        COUISwitchParams cOUISwitchParams = (COUISwitchParams) other;
        return Intrinsics.areEqual(this.toggleSpring, cOUISwitchParams.toggleSpring) && Float.compare(this.responseDrag, cOUISwitchParams.responseDrag) == 0 && Float.compare(this.bounceDrag, cOUISwitchParams.bounceDrag) == 0 && Float.compare(this.responseAttach, cOUISwitchParams.responseAttach) == 0 && Float.compare(this.bounceAttach, cOUISwitchParams.bounceAttach) == 0 && Float.compare(this.transformVelocity, cOUISwitchParams.transformVelocity) == 0 && Float.compare(this.transformDistance, cOUISwitchParams.transformDistance) == 0;
    }

    public final float getBounceAttach() {
        return this.bounceAttach;
    }

    public final float getBounceDrag() {
        return this.bounceDrag;
    }

    public final float getResponseAttach() {
        return this.responseAttach;
    }

    public final float getResponseDrag() {
        return this.responseDrag;
    }

    public final SpringData getToggleSpring() {
        return this.toggleSpring;
    }

    public final float getTransformDistance() {
        return this.transformDistance;
    }

    public final float getTransformVelocity() {
        return this.transformVelocity;
    }

    public int hashCode() {
        return (((((((((((this.toggleSpring.hashCode() * 31) + Float.hashCode(this.responseDrag)) * 31) + Float.hashCode(this.bounceDrag)) * 31) + Float.hashCode(this.responseAttach)) * 31) + Float.hashCode(this.bounceAttach)) * 31) + Float.hashCode(this.transformVelocity)) * 31) + Float.hashCode(this.transformDistance);
    }

    public final void setBounceAttach(float f3) {
        this.bounceAttach = f3;
    }

    public final void setBounceDrag(float f3) {
        this.bounceDrag = f3;
    }

    public final void setResponseAttach(float f3) {
        this.responseAttach = f3;
    }

    public final void setResponseDrag(float f3) {
        this.responseDrag = f3;
    }

    public final void setToggleSpring(SpringData springData) {
        Intrinsics.checkNotNullParameter(springData, "<set-?>");
        this.toggleSpring = springData;
    }

    public final void setTransformDistance(float f3) {
        this.transformDistance = f3;
    }

    public final void setTransformVelocity(float f3) {
        this.transformVelocity = f3;
    }

    public String toString() {
        return "COUISwitchParams(toggleSpring=" + this.toggleSpring + ", responseDrag=" + this.responseDrag + ", bounceDrag=" + this.bounceDrag + ", responseAttach=" + this.responseAttach + ", bounceAttach=" + this.bounceAttach + ", transformVelocity=" + this.transformVelocity + ", transformDistance=" + this.transformDistance + ')';
    }

    public COUISwitchParams(SpringData toggleSpring, float f3, float f4, float f5, float f6, float f7, float f8) {
        Intrinsics.checkNotNullParameter(toggleSpring, "toggleSpring");
        this.toggleSpring = toggleSpring;
        this.responseDrag = f3;
        this.bounceDrag = f4;
        this.responseAttach = f5;
        this.bounceAttach = f6;
        this.transformVelocity = f7;
        this.transformDistance = f8;
    }

    public /* synthetic */ COUISwitchParams(SpringData springData, float f3, float f4, float f5, float f6, float f7, float f8, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? new SpringData(0.3f, 0.4f, 0.0f) : springData, (i3 & 2) != 0 ? 0.3f : f3, (i3 & 4) != 0 ? 0.0f : f4, (i3 & 8) != 0 ? 0.4f : f5, (i3 & 16) != 0 ? 0.2f : f6, (i3 & 32) != 0 ? 1000.0f : f7, (i3 & 64) != 0 ? 4.0f : f8);
    }
}