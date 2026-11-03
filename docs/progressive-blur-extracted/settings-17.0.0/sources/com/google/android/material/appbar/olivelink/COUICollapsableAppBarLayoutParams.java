package com.google.android.material.appbar.olivelink;

import com.coui.iola.SpringData;
import com.oplus.settings.feature.language.localepicker.RegionPickerUtils;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b1\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\b\u0018\u0000 D2\u00020\u0001:\u0001DBi\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\b\b\u0002\u0010\r\u001a\u00020\f¢\u0006\u0002\u0010\u000eJ\t\u00102\u001a\u00020\u0003HÆ\u0003J\t\u00103\u001a\u00020\fHÆ\u0003J\t\u00104\u001a\u00020\u0003HÆ\u0003J\t\u00105\u001a\u00020\u0003HÆ\u0003J\t\u00106\u001a\u00020\u0003HÆ\u0003J\t\u00107\u001a\u00020\u0003HÆ\u0003J\t\u00108\u001a\u00020\u0003HÆ\u0003J\t\u00109\u001a\u00020\u0003HÆ\u0003J\t\u0010:\u001a\u00020\u0003HÆ\u0003J\t\u0010;\u001a\u00020\fHÆ\u0003Jm\u0010<\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\fHÆ\u0001J\u0013\u0010=\u001a\u00020>2\b\u0010?\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010@\u001a\u00020AHÖ\u0001J\t\u0010B\u001a\u00020CHÖ\u0001R \u0010\u000b\u001a\u00020\fX\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u000f\u0010\u0010\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R \u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u0015\u0010\u0010\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R \u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u001a\u0010\u0010\u001a\u0004\b\u001b\u0010\u0017\"\u0004\b\u001c\u0010\u0019R \u0010\u0006\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u001d\u0010\u0010\u001a\u0004\b\u001e\u0010\u0017\"\u0004\b\u001f\u0010\u0019R \u0010\n\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b \u0010\u0010\u001a\u0004\b!\u0010\u0017\"\u0004\b\"\u0010\u0019R \u0010\t\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b#\u0010\u0010\u001a\u0004\b$\u0010\u0017\"\u0004\b%\u0010\u0019R \u0010\b\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b&\u0010\u0010\u001a\u0004\b'\u0010\u0017\"\u0004\b(\u0010\u0019R \u0010\u0007\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b)\u0010\u0010\u001a\u0004\b*\u0010\u0017\"\u0004\b+\u0010\u0019R \u0010\u0005\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b,\u0010\u0010\u001a\u0004\b-\u0010\u0017\"\u0004\b.\u0010\u0019R \u0010\r\u001a\u00020\fX\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b/\u0010\u0010\u001a\u0004\b0\u0010\u0012\"\u0004\b1\u0010\u0014¨\u0006E"}, d2 = {"Lcom/google/android/material/appbar/olivelink/COUICollapsableAppBarLayoutParams;", "", "attachResponse", "", "attachBounce", "followResponse", "followBounce", "followListResponseUp", "followListResponseDown", "followHeaderResponse", "followHeaderBounce", "alphaSpring", "Lcom/coui/iola/SpringData;", "titleAlphaSpring", "(FFFFFFFFLcom/coui/iola/SpringData;Lcom/coui/iola/SpringData;)V", "getAlphaSpring$annotations", "()V", "getAlphaSpring", "()Lcom/coui/iola/SpringData;", "setAlphaSpring", "(Lcom/coui/iola/SpringData;)V", "getAttachBounce$annotations", "getAttachBounce", "()F", "setAttachBounce", "(F)V", "getAttachResponse$annotations", "getAttachResponse", "setAttachResponse", "getFollowBounce$annotations", "getFollowBounce", "setFollowBounce", "getFollowHeaderBounce$annotations", "getFollowHeaderBounce", "setFollowHeaderBounce", "getFollowHeaderResponse$annotations", "getFollowHeaderResponse", "setFollowHeaderResponse", "getFollowListResponseDown$annotations", "getFollowListResponseDown", "setFollowListResponseDown", "getFollowListResponseUp$annotations", "getFollowListResponseUp", "setFollowListResponseUp", "getFollowResponse$annotations", "getFollowResponse", "setFollowResponse", "getTitleAlphaSpring$annotations", "getTitleAlphaSpring", "setTitleAlphaSpring", "component1", "component10", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", RegionPickerUtils.OTHER_SYSTEM_REGION_KEY, "hashCode", "", "toString", "", "Companion", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final /* data */ class COUICollapsableAppBarLayoutParams {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private SpringData alphaSpring;
    private float attachBounce;
    private float attachResponse;
    private float followBounce;
    private float followHeaderBounce;
    private float followHeaderResponse;
    private float followListResponseDown;
    private float followListResponseUp;
    private float followResponse;
    private SpringData titleAlphaSpring;

    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lcom/google/android/material/appbar/olivelink/COUICollapsableAppBarLayoutParams$Companion;", "", "()V", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public COUICollapsableAppBarLayoutParams() {
        this(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, null, null, 1023, null);
    }

    public static /* synthetic */ COUICollapsableAppBarLayoutParams copy$default(COUICollapsableAppBarLayoutParams cOUICollapsableAppBarLayoutParams, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, SpringData springData, SpringData springData2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            f3 = cOUICollapsableAppBarLayoutParams.attachResponse;
        }
        if ((i3 & 2) != 0) {
            f4 = cOUICollapsableAppBarLayoutParams.attachBounce;
        }
        if ((i3 & 4) != 0) {
            f5 = cOUICollapsableAppBarLayoutParams.followResponse;
        }
        if ((i3 & 8) != 0) {
            f6 = cOUICollapsableAppBarLayoutParams.followBounce;
        }
        if ((i3 & 16) != 0) {
            f7 = cOUICollapsableAppBarLayoutParams.followListResponseUp;
        }
        if ((i3 & 32) != 0) {
            f8 = cOUICollapsableAppBarLayoutParams.followListResponseDown;
        }
        if ((i3 & 64) != 0) {
            f9 = cOUICollapsableAppBarLayoutParams.followHeaderResponse;
        }
        if ((i3 & 128) != 0) {
            f10 = cOUICollapsableAppBarLayoutParams.followHeaderBounce;
        }
        if ((i3 & 256) != 0) {
            springData = cOUICollapsableAppBarLayoutParams.alphaSpring;
        }
        if ((i3 & 512) != 0) {
            springData2 = cOUICollapsableAppBarLayoutParams.titleAlphaSpring;
        }
        SpringData springData3 = springData;
        SpringData springData4 = springData2;
        float f11 = f9;
        float f12 = f10;
        float f13 = f7;
        float f14 = f8;
        return cOUICollapsableAppBarLayoutParams.copy(f3, f4, f5, f6, f13, f14, f11, f12, springData3, springData4);
    }

    public static /* synthetic */ void getAlphaSpring$annotations() {
    }

    public static /* synthetic */ void getAttachBounce$annotations() {
    }

    public static /* synthetic */ void getAttachResponse$annotations() {
    }

    public static /* synthetic */ void getFollowBounce$annotations() {
    }

    public static /* synthetic */ void getFollowHeaderBounce$annotations() {
    }

    public static /* synthetic */ void getFollowHeaderResponse$annotations() {
    }

    public static /* synthetic */ void getFollowListResponseDown$annotations() {
    }

    public static /* synthetic */ void getFollowListResponseUp$annotations() {
    }

    public static /* synthetic */ void getFollowResponse$annotations() {
    }

    public static /* synthetic */ void getTitleAlphaSpring$annotations() {
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final float getAttachResponse() {
        return this.attachResponse;
    }

    /* JADX INFO: renamed from: component10, reason: from getter */
    public final SpringData getTitleAlphaSpring() {
        return this.titleAlphaSpring;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final float getAttachBounce() {
        return this.attachBounce;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final float getFollowResponse() {
        return this.followResponse;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final float getFollowBounce() {
        return this.followBounce;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final float getFollowListResponseUp() {
        return this.followListResponseUp;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final float getFollowListResponseDown() {
        return this.followListResponseDown;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final float getFollowHeaderResponse() {
        return this.followHeaderResponse;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final float getFollowHeaderBounce() {
        return this.followHeaderBounce;
    }

    /* JADX INFO: renamed from: component9, reason: from getter */
    public final SpringData getAlphaSpring() {
        return this.alphaSpring;
    }

    public final COUICollapsableAppBarLayoutParams copy(float attachResponse, float attachBounce, float followResponse, float followBounce, float followListResponseUp, float followListResponseDown, float followHeaderResponse, float followHeaderBounce, SpringData alphaSpring, SpringData titleAlphaSpring) {
        Intrinsics.checkNotNullParameter(alphaSpring, "alphaSpring");
        Intrinsics.checkNotNullParameter(titleAlphaSpring, "titleAlphaSpring");
        return new COUICollapsableAppBarLayoutParams(attachResponse, attachBounce, followResponse, followBounce, followListResponseUp, followListResponseDown, followHeaderResponse, followHeaderBounce, alphaSpring, titleAlphaSpring);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof COUICollapsableAppBarLayoutParams)) {
            return false;
        }
        COUICollapsableAppBarLayoutParams cOUICollapsableAppBarLayoutParams = (COUICollapsableAppBarLayoutParams) other;
        return Float.compare(this.attachResponse, cOUICollapsableAppBarLayoutParams.attachResponse) == 0 && Float.compare(this.attachBounce, cOUICollapsableAppBarLayoutParams.attachBounce) == 0 && Float.compare(this.followResponse, cOUICollapsableAppBarLayoutParams.followResponse) == 0 && Float.compare(this.followBounce, cOUICollapsableAppBarLayoutParams.followBounce) == 0 && Float.compare(this.followListResponseUp, cOUICollapsableAppBarLayoutParams.followListResponseUp) == 0 && Float.compare(this.followListResponseDown, cOUICollapsableAppBarLayoutParams.followListResponseDown) == 0 && Float.compare(this.followHeaderResponse, cOUICollapsableAppBarLayoutParams.followHeaderResponse) == 0 && Float.compare(this.followHeaderBounce, cOUICollapsableAppBarLayoutParams.followHeaderBounce) == 0 && Intrinsics.areEqual(this.alphaSpring, cOUICollapsableAppBarLayoutParams.alphaSpring) && Intrinsics.areEqual(this.titleAlphaSpring, cOUICollapsableAppBarLayoutParams.titleAlphaSpring);
    }

    public final SpringData getAlphaSpring() {
        return this.alphaSpring;
    }

    public final float getAttachBounce() {
        return this.attachBounce;
    }

    public final float getAttachResponse() {
        return this.attachResponse;
    }

    public final float getFollowBounce() {
        return this.followBounce;
    }

    public final float getFollowHeaderBounce() {
        return this.followHeaderBounce;
    }

    public final float getFollowHeaderResponse() {
        return this.followHeaderResponse;
    }

    public final float getFollowListResponseDown() {
        return this.followListResponseDown;
    }

    public final float getFollowListResponseUp() {
        return this.followListResponseUp;
    }

    public final float getFollowResponse() {
        return this.followResponse;
    }

    public final SpringData getTitleAlphaSpring() {
        return this.titleAlphaSpring;
    }

    public int hashCode() {
        return (((((((((((((((((Float.hashCode(this.attachResponse) * 31) + Float.hashCode(this.attachBounce)) * 31) + Float.hashCode(this.followResponse)) * 31) + Float.hashCode(this.followBounce)) * 31) + Float.hashCode(this.followListResponseUp)) * 31) + Float.hashCode(this.followListResponseDown)) * 31) + Float.hashCode(this.followHeaderResponse)) * 31) + Float.hashCode(this.followHeaderBounce)) * 31) + this.alphaSpring.hashCode()) * 31) + this.titleAlphaSpring.hashCode();
    }

    public final void setAlphaSpring(SpringData springData) {
        Intrinsics.checkNotNullParameter(springData, "<set-?>");
        this.alphaSpring = springData;
    }

    public final void setAttachBounce(float f3) {
        this.attachBounce = f3;
    }

    public final void setAttachResponse(float f3) {
        this.attachResponse = f3;
    }

    public final void setFollowBounce(float f3) {
        this.followBounce = f3;
    }

    public final void setFollowHeaderBounce(float f3) {
        this.followHeaderBounce = f3;
    }

    public final void setFollowHeaderResponse(float f3) {
        this.followHeaderResponse = f3;
    }

    public final void setFollowListResponseDown(float f3) {
        this.followListResponseDown = f3;
    }

    public final void setFollowListResponseUp(float f3) {
        this.followListResponseUp = f3;
    }

    public final void setFollowResponse(float f3) {
        this.followResponse = f3;
    }

    public final void setTitleAlphaSpring(SpringData springData) {
        Intrinsics.checkNotNullParameter(springData, "<set-?>");
        this.titleAlphaSpring = springData;
    }

    public String toString() {
        return "COUICollapsableAppBarLayoutParams(attachResponse=" + this.attachResponse + ", attachBounce=" + this.attachBounce + ", followResponse=" + this.followResponse + ", followBounce=" + this.followBounce + ", followListResponseUp=" + this.followListResponseUp + ", followListResponseDown=" + this.followListResponseDown + ", followHeaderResponse=" + this.followHeaderResponse + ", followHeaderBounce=" + this.followHeaderBounce + ", alphaSpring=" + this.alphaSpring + ", titleAlphaSpring=" + this.titleAlphaSpring + ')';
    }

    public COUICollapsableAppBarLayoutParams(float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, SpringData alphaSpring, SpringData titleAlphaSpring) {
        Intrinsics.checkNotNullParameter(alphaSpring, "alphaSpring");
        Intrinsics.checkNotNullParameter(titleAlphaSpring, "titleAlphaSpring");
        this.attachResponse = f3;
        this.attachBounce = f4;
        this.followResponse = f5;
        this.followBounce = f6;
        this.followListResponseUp = f7;
        this.followListResponseDown = f8;
        this.followHeaderResponse = f9;
        this.followHeaderBounce = f10;
        this.alphaSpring = alphaSpring;
        this.titleAlphaSpring = titleAlphaSpring;
    }

    public /* synthetic */ COUICollapsableAppBarLayoutParams(float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, SpringData springData, SpringData springData2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? 0.25f : f3, (i3 & 2) != 0 ? 0.0f : f4, (i3 & 4) != 0 ? 0.3f : f5, (i3 & 8) != 0 ? 0.0f : f6, (i3 & 16) != 0 ? 0.2f : f7, (i3 & 32) != 0 ? 0.1f : f8, (i3 & 64) != 0 ? 0.15f : f9, (i3 & 128) != 0 ? 0.0f : f10, (i3 & 256) != 0 ? new SpringData(0.0f, 0.3f, 0.0f) : springData, (i3 & 512) != 0 ? new SpringData(0.0f, 0.15f, 0.0f) : springData2);
    }
}