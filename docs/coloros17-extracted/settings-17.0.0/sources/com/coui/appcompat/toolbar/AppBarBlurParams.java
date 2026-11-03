package com.coui.appcompat.toolbar;

import com.oplus.settings.feature.language.localepicker.RegionPickerUtils;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b<\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\b\u0018\u0000 F2\u00020\u0001:\u0001FBs\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003¢\u0006\u0002\u0010\u000eJ\t\u00103\u001a\u00020\u0003HÆ\u0003J\t\u00104\u001a\u00020\u0003HÆ\u0003J\t\u00105\u001a\u00020\u0003HÆ\u0003J\t\u00106\u001a\u00020\u0003HÆ\u0003J\t\u00107\u001a\u00020\u0003HÆ\u0003J\t\u00108\u001a\u00020\u0003HÆ\u0003J\t\u00109\u001a\u00020\u0003HÆ\u0003J\t\u0010:\u001a\u00020\u0003HÆ\u0003J\t\u0010;\u001a\u00020\u0003HÆ\u0003J\t\u0010<\u001a\u00020\u0003HÆ\u0003J\t\u0010=\u001a\u00020\u0003HÆ\u0003Jw\u0010>\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u0003HÆ\u0001J\u0013\u0010?\u001a\u00020@2\b\u0010A\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010B\u001a\u00020CHÖ\u0001J\t\u0010D\u001a\u00020EHÖ\u0001R \u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u000f\u0010\u0010\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R \u0010\t\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u0015\u0010\u0010\u001a\u0004\b\u0016\u0010\u0012\"\u0004\b\u0017\u0010\u0014R \u0010\n\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u0018\u0010\u0010\u001a\u0004\b\u0019\u0010\u0012\"\u0004\b\u001a\u0010\u0014R \u0010\u000b\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u001b\u0010\u0010\u001a\u0004\b\u001c\u0010\u0012\"\u0004\b\u001d\u0010\u0014R \u0010\u0005\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u001e\u0010\u0010\u001a\u0004\b\u001f\u0010\u0012\"\u0004\b \u0010\u0014R \u0010\u0006\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b!\u0010\u0010\u001a\u0004\b\"\u0010\u0012\"\u0004\b#\u0010\u0014R \u0010\u0007\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b$\u0010\u0010\u001a\u0004\b%\u0010\u0012\"\u0004\b&\u0010\u0014R \u0010\b\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b'\u0010\u0010\u001a\u0004\b(\u0010\u0012\"\u0004\b)\u0010\u0014R \u0010\r\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b*\u0010\u0010\u001a\u0004\b+\u0010\u0012\"\u0004\b,\u0010\u0014R \u0010\f\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b-\u0010\u0010\u001a\u0004\b.\u0010\u0012\"\u0004\b/\u0010\u0014R \u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b0\u0010\u0010\u001a\u0004\b1\u0010\u0012\"\u0004\b2\u0010\u0014¨\u0006G"}, d2 = {"Lcom/coui/appcompat/toolbar/AppBarBlurParams;", "", "startBlurRadius", "", "endBlurRadius", "gradientBlurSpeed", "gradientPos0", "gradientPos1", "gradientPos2", "gradientAlpha0", "gradientAlpha1", "gradientAlpha2", "normalBlurRadiusPx", "normalBackgroundAlphaFraction", "(FFFFFFFFFFF)V", "getEndBlurRadius$annotations", "()V", "getEndBlurRadius", "()F", "setEndBlurRadius", "(F)V", "getGradientAlpha0$annotations", "getGradientAlpha0", "setGradientAlpha0", "getGradientAlpha1$annotations", "getGradientAlpha1", "setGradientAlpha1", "getGradientAlpha2$annotations", "getGradientAlpha2", "setGradientAlpha2", "getGradientBlurSpeed$annotations", "getGradientBlurSpeed", "setGradientBlurSpeed", "getGradientPos0$annotations", "getGradientPos0", "setGradientPos0", "getGradientPos1$annotations", "getGradientPos1", "setGradientPos1", "getGradientPos2$annotations", "getGradientPos2", "setGradientPos2", "getNormalBackgroundAlphaFraction$annotations", "getNormalBackgroundAlphaFraction", "setNormalBackgroundAlphaFraction", "getNormalBlurRadiusPx$annotations", "getNormalBlurRadiusPx", "setNormalBlurRadiusPx", "getStartBlurRadius$annotations", "getStartBlurRadius", "setStartBlurRadius", "component1", "component10", "component11", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", RegionPickerUtils.OTHER_SYSTEM_REGION_KEY, "hashCode", "", "toString", "", "Companion", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final /* data */ class AppBarBlurParams {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private float endBlurRadius;
    private float gradientAlpha0;
    private float gradientAlpha1;
    private float gradientAlpha2;
    private float gradientBlurSpeed;
    private float gradientPos0;
    private float gradientPos1;
    private float gradientPos2;
    private float normalBackgroundAlphaFraction;
    private float normalBlurRadiusPx;
    private float startBlurRadius;

    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lcom/coui/appcompat/toolbar/AppBarBlurParams$Companion;", "", "()V", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public AppBarBlurParams() {
        this(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2047, null);
    }

    public static /* synthetic */ AppBarBlurParams copy$default(AppBarBlurParams appBarBlurParams, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            f3 = appBarBlurParams.startBlurRadius;
        }
        if ((i3 & 2) != 0) {
            f4 = appBarBlurParams.endBlurRadius;
        }
        if ((i3 & 4) != 0) {
            f5 = appBarBlurParams.gradientBlurSpeed;
        }
        if ((i3 & 8) != 0) {
            f6 = appBarBlurParams.gradientPos0;
        }
        if ((i3 & 16) != 0) {
            f7 = appBarBlurParams.gradientPos1;
        }
        if ((i3 & 32) != 0) {
            f8 = appBarBlurParams.gradientPos2;
        }
        if ((i3 & 64) != 0) {
            f9 = appBarBlurParams.gradientAlpha0;
        }
        if ((i3 & 128) != 0) {
            f10 = appBarBlurParams.gradientAlpha1;
        }
        if ((i3 & 256) != 0) {
            f11 = appBarBlurParams.gradientAlpha2;
        }
        if ((i3 & 512) != 0) {
            f12 = appBarBlurParams.normalBlurRadiusPx;
        }
        if ((i3 & 1024) != 0) {
            f13 = appBarBlurParams.normalBackgroundAlphaFraction;
        }
        float f14 = f12;
        float f15 = f13;
        float f16 = f10;
        float f17 = f11;
        float f18 = f8;
        float f19 = f9;
        float f20 = f7;
        float f21 = f5;
        return appBarBlurParams.copy(f3, f4, f21, f6, f20, f18, f19, f16, f17, f14, f15);
    }

    public static /* synthetic */ void getEndBlurRadius$annotations() {
    }

    public static /* synthetic */ void getGradientAlpha0$annotations() {
    }

    public static /* synthetic */ void getGradientAlpha1$annotations() {
    }

    public static /* synthetic */ void getGradientAlpha2$annotations() {
    }

    public static /* synthetic */ void getGradientBlurSpeed$annotations() {
    }

    public static /* synthetic */ void getGradientPos0$annotations() {
    }

    public static /* synthetic */ void getGradientPos1$annotations() {
    }

    public static /* synthetic */ void getGradientPos2$annotations() {
    }

    public static /* synthetic */ void getNormalBackgroundAlphaFraction$annotations() {
    }

    public static /* synthetic */ void getNormalBlurRadiusPx$annotations() {
    }

    public static /* synthetic */ void getStartBlurRadius$annotations() {
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final float getStartBlurRadius() {
        return this.startBlurRadius;
    }

    /* JADX INFO: renamed from: component10, reason: from getter */
    public final float getNormalBlurRadiusPx() {
        return this.normalBlurRadiusPx;
    }

    /* JADX INFO: renamed from: component11, reason: from getter */
    public final float getNormalBackgroundAlphaFraction() {
        return this.normalBackgroundAlphaFraction;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final float getEndBlurRadius() {
        return this.endBlurRadius;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final float getGradientBlurSpeed() {
        return this.gradientBlurSpeed;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final float getGradientPos0() {
        return this.gradientPos0;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final float getGradientPos1() {
        return this.gradientPos1;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final float getGradientPos2() {
        return this.gradientPos2;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final float getGradientAlpha0() {
        return this.gradientAlpha0;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final float getGradientAlpha1() {
        return this.gradientAlpha1;
    }

    /* JADX INFO: renamed from: component9, reason: from getter */
    public final float getGradientAlpha2() {
        return this.gradientAlpha2;
    }

    public final AppBarBlurParams copy(float startBlurRadius, float endBlurRadius, float gradientBlurSpeed, float gradientPos0, float gradientPos1, float gradientPos2, float gradientAlpha0, float gradientAlpha1, float gradientAlpha2, float normalBlurRadiusPx, float normalBackgroundAlphaFraction) {
        return new AppBarBlurParams(startBlurRadius, endBlurRadius, gradientBlurSpeed, gradientPos0, gradientPos1, gradientPos2, gradientAlpha0, gradientAlpha1, gradientAlpha2, normalBlurRadiusPx, normalBackgroundAlphaFraction);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AppBarBlurParams)) {
            return false;
        }
        AppBarBlurParams appBarBlurParams = (AppBarBlurParams) other;
        return Float.compare(this.startBlurRadius, appBarBlurParams.startBlurRadius) == 0 && Float.compare(this.endBlurRadius, appBarBlurParams.endBlurRadius) == 0 && Float.compare(this.gradientBlurSpeed, appBarBlurParams.gradientBlurSpeed) == 0 && Float.compare(this.gradientPos0, appBarBlurParams.gradientPos0) == 0 && Float.compare(this.gradientPos1, appBarBlurParams.gradientPos1) == 0 && Float.compare(this.gradientPos2, appBarBlurParams.gradientPos2) == 0 && Float.compare(this.gradientAlpha0, appBarBlurParams.gradientAlpha0) == 0 && Float.compare(this.gradientAlpha1, appBarBlurParams.gradientAlpha1) == 0 && Float.compare(this.gradientAlpha2, appBarBlurParams.gradientAlpha2) == 0 && Float.compare(this.normalBlurRadiusPx, appBarBlurParams.normalBlurRadiusPx) == 0 && Float.compare(this.normalBackgroundAlphaFraction, appBarBlurParams.normalBackgroundAlphaFraction) == 0;
    }

    public final float getEndBlurRadius() {
        return this.endBlurRadius;
    }

    public final float getGradientAlpha0() {
        return this.gradientAlpha0;
    }

    public final float getGradientAlpha1() {
        return this.gradientAlpha1;
    }

    public final float getGradientAlpha2() {
        return this.gradientAlpha2;
    }

    public final float getGradientBlurSpeed() {
        return this.gradientBlurSpeed;
    }

    public final float getGradientPos0() {
        return this.gradientPos0;
    }

    public final float getGradientPos1() {
        return this.gradientPos1;
    }

    public final float getGradientPos2() {
        return this.gradientPos2;
    }

    public final float getNormalBackgroundAlphaFraction() {
        return this.normalBackgroundAlphaFraction;
    }

    public final float getNormalBlurRadiusPx() {
        return this.normalBlurRadiusPx;
    }

    public final float getStartBlurRadius() {
        return this.startBlurRadius;
    }

    public int hashCode() {
        return (((((((((((((((((((Float.hashCode(this.startBlurRadius) * 31) + Float.hashCode(this.endBlurRadius)) * 31) + Float.hashCode(this.gradientBlurSpeed)) * 31) + Float.hashCode(this.gradientPos0)) * 31) + Float.hashCode(this.gradientPos1)) * 31) + Float.hashCode(this.gradientPos2)) * 31) + Float.hashCode(this.gradientAlpha0)) * 31) + Float.hashCode(this.gradientAlpha1)) * 31) + Float.hashCode(this.gradientAlpha2)) * 31) + Float.hashCode(this.normalBlurRadiusPx)) * 31) + Float.hashCode(this.normalBackgroundAlphaFraction);
    }

    public final void setEndBlurRadius(float f3) {
        this.endBlurRadius = f3;
    }

    public final void setGradientAlpha0(float f3) {
        this.gradientAlpha0 = f3;
    }

    public final void setGradientAlpha1(float f3) {
        this.gradientAlpha1 = f3;
    }

    public final void setGradientAlpha2(float f3) {
        this.gradientAlpha2 = f3;
    }

    public final void setGradientBlurSpeed(float f3) {
        this.gradientBlurSpeed = f3;
    }

    public final void setGradientPos0(float f3) {
        this.gradientPos0 = f3;
    }

    public final void setGradientPos1(float f3) {
        this.gradientPos1 = f3;
    }

    public final void setGradientPos2(float f3) {
        this.gradientPos2 = f3;
    }

    public final void setNormalBackgroundAlphaFraction(float f3) {
        this.normalBackgroundAlphaFraction = f3;
    }

    public final void setNormalBlurRadiusPx(float f3) {
        this.normalBlurRadiusPx = f3;
    }

    public final void setStartBlurRadius(float f3) {
        this.startBlurRadius = f3;
    }

    public String toString() {
        return "AppBarBlurParams(startBlurRadius=" + this.startBlurRadius + ", endBlurRadius=" + this.endBlurRadius + ", gradientBlurSpeed=" + this.gradientBlurSpeed + ", gradientPos0=" + this.gradientPos0 + ", gradientPos1=" + this.gradientPos1 + ", gradientPos2=" + this.gradientPos2 + ", gradientAlpha0=" + this.gradientAlpha0 + ", gradientAlpha1=" + this.gradientAlpha1 + ", gradientAlpha2=" + this.gradientAlpha2 + ", normalBlurRadiusPx=" + this.normalBlurRadiusPx + ", normalBackgroundAlphaFraction=" + this.normalBackgroundAlphaFraction + ')';
    }

    public AppBarBlurParams(float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13) {
        this.startBlurRadius = f3;
        this.endBlurRadius = f4;
        this.gradientBlurSpeed = f5;
        this.gradientPos0 = f6;
        this.gradientPos1 = f7;
        this.gradientPos2 = f8;
        this.gradientAlpha0 = f9;
        this.gradientAlpha1 = f10;
        this.gradientAlpha2 = f11;
        this.normalBlurRadiusPx = f12;
        this.normalBackgroundAlphaFraction = f13;
    }

    public /* synthetic */ AppBarBlurParams(float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? 30.0f : f3, (i3 & 2) != 0 ? 0.0f : f4, (i3 & 4) != 0 ? 3.0f : f5, (i3 & 8) != 0 ? 0.0f : f6, (i3 & 16) != 0 ? 0.4f : f7, (i3 & 32) != 0 ? 1.0f : f8, (i3 & 64) != 0 ? 0.0f : f9, (i3 & 128) != 0 ? 0.7f : f10, (i3 & 256) != 0 ? 1.0f : f11, (i3 & 512) != 0 ? 50.0f : f12, (i3 & 1024) != 0 ? 0.8f : f13);
    }
}