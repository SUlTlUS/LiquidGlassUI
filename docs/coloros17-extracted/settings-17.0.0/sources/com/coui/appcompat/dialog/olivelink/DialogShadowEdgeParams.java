package com.coui.appcompat.dialog.olivelink;

import com.oplus.settings.feature.language.localepicker.RegionPickerUtils;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\bA\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\b\u0018\u0000 K2\u00020\u0001:\u0001KB}\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0003¢\u0006\u0002\u0010\u000fJ\t\u00107\u001a\u00020\u0003HÆ\u0003J\t\u00108\u001a\u00020\u0003HÆ\u0003J\t\u00109\u001a\u00020\u0003HÆ\u0003J\t\u0010:\u001a\u00020\u0003HÆ\u0003J\t\u0010;\u001a\u00020\u0003HÆ\u0003J\t\u0010<\u001a\u00020\u0003HÆ\u0003J\t\u0010=\u001a\u00020\u0003HÆ\u0003J\t\u0010>\u001a\u00020\u0003HÆ\u0003J\t\u0010?\u001a\u00020\u0003HÆ\u0003J\t\u0010@\u001a\u00020\u0003HÆ\u0003J\t\u0010A\u001a\u00020\u0003HÆ\u0003J\t\u0010B\u001a\u00020\u0003HÆ\u0003J\u0081\u0001\u0010C\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u00032\b\b\u0002\u0010\u000e\u001a\u00020\u0003HÆ\u0001J\u0013\u0010D\u001a\u00020E2\b\u0010F\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010G\u001a\u00020HHÖ\u0001J\t\u0010I\u001a\u00020JHÖ\u0001R \u0010\r\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u0010\u0010\u0011\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R \u0010\u000e\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u0016\u0010\u0011\u001a\u0004\b\u0017\u0010\u0013\"\u0004\b\u0018\u0010\u0015R \u0010\n\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u0019\u0010\u0011\u001a\u0004\b\u001a\u0010\u0013\"\u0004\b\u001b\u0010\u0015R \u0010\u0006\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u001c\u0010\u0011\u001a\u0004\b\u001d\u0010\u0013\"\u0004\b\u001e\u0010\u0015R \u0010\b\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u001f\u0010\u0011\u001a\u0004\b \u0010\u0013\"\u0004\b!\u0010\u0015R \u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\"\u0010\u0011\u001a\u0004\b#\u0010\u0013\"\u0004\b$\u0010\u0015R \u0010\u000b\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b%\u0010\u0011\u001a\u0004\b&\u0010\u0013\"\u0004\b'\u0010\u0015R \u0010\f\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b(\u0010\u0011\u001a\u0004\b)\u0010\u0013\"\u0004\b*\u0010\u0015R \u0010\t\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b+\u0010\u0011\u001a\u0004\b,\u0010\u0013\"\u0004\b-\u0010\u0015R \u0010\u0005\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b.\u0010\u0011\u001a\u0004\b/\u0010\u0013\"\u0004\b0\u0010\u0015R \u0010\u0007\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b1\u0010\u0011\u001a\u0004\b2\u0010\u0013\"\u0004\b3\u0010\u0015R \u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b4\u0010\u0011\u001a\u0004\b5\u0010\u0013\"\u0004\b6\u0010\u0015¨\u0006L"}, d2 = {"Lcom/coui/appcompat/dialog/olivelink/DialogShadowEdgeParams;", "", "lineWidth", "", "darklineWidth", "lineAlpha", "darklineAlpha", "lineAngle", "darklineAngle", "fadeWidthScale", "darkfadeWidthScale", "fadeAlphaX", "fadeAlphaY", "darkfadeAlphaX", "darkfadeAlphaY", "(FFFFFFFFFFFF)V", "getDarkfadeAlphaX$annotations", "()V", "getDarkfadeAlphaX", "()F", "setDarkfadeAlphaX", "(F)V", "getDarkfadeAlphaY$annotations", "getDarkfadeAlphaY", "setDarkfadeAlphaY", "getDarkfadeWidthScale$annotations", "getDarkfadeWidthScale", "setDarkfadeWidthScale", "getDarklineAlpha$annotations", "getDarklineAlpha", "setDarklineAlpha", "getDarklineAngle$annotations", "getDarklineAngle", "setDarklineAngle", "getDarklineWidth$annotations", "getDarklineWidth", "setDarklineWidth", "getFadeAlphaX$annotations", "getFadeAlphaX", "setFadeAlphaX", "getFadeAlphaY$annotations", "getFadeAlphaY", "setFadeAlphaY", "getFadeWidthScale$annotations", "getFadeWidthScale", "setFadeWidthScale", "getLineAlpha$annotations", "getLineAlpha", "setLineAlpha", "getLineAngle$annotations", "getLineAngle", "setLineAngle", "getLineWidth$annotations", "getLineWidth", "setLineWidth", "component1", "component10", "component11", "component12", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", RegionPickerUtils.OTHER_SYSTEM_REGION_KEY, "hashCode", "", "toString", "", "Companion", "coui-support-dialog_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final /* data */ class DialogShadowEdgeParams {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private float darkfadeAlphaX;
    private float darkfadeAlphaY;
    private float darkfadeWidthScale;
    private float darklineAlpha;
    private float darklineAngle;
    private float darklineWidth;
    private float fadeAlphaX;
    private float fadeAlphaY;
    private float fadeWidthScale;
    private float lineAlpha;
    private float lineAngle;
    private float lineWidth;

    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lcom/coui/appcompat/dialog/olivelink/DialogShadowEdgeParams$Companion;", "", "()V", "coui-support-dialog_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public DialogShadowEdgeParams() {
        this(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 4095, null);
    }

    public static /* synthetic */ DialogShadowEdgeParams copy$default(DialogShadowEdgeParams dialogShadowEdgeParams, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            f3 = dialogShadowEdgeParams.lineWidth;
        }
        if ((i3 & 2) != 0) {
            f4 = dialogShadowEdgeParams.darklineWidth;
        }
        if ((i3 & 4) != 0) {
            f5 = dialogShadowEdgeParams.lineAlpha;
        }
        if ((i3 & 8) != 0) {
            f6 = dialogShadowEdgeParams.darklineAlpha;
        }
        if ((i3 & 16) != 0) {
            f7 = dialogShadowEdgeParams.lineAngle;
        }
        if ((i3 & 32) != 0) {
            f8 = dialogShadowEdgeParams.darklineAngle;
        }
        if ((i3 & 64) != 0) {
            f9 = dialogShadowEdgeParams.fadeWidthScale;
        }
        if ((i3 & 128) != 0) {
            f10 = dialogShadowEdgeParams.darkfadeWidthScale;
        }
        if ((i3 & 256) != 0) {
            f11 = dialogShadowEdgeParams.fadeAlphaX;
        }
        if ((i3 & 512) != 0) {
            f12 = dialogShadowEdgeParams.fadeAlphaY;
        }
        if ((i3 & 1024) != 0) {
            f13 = dialogShadowEdgeParams.darkfadeAlphaX;
        }
        if ((i3 & 2048) != 0) {
            f14 = dialogShadowEdgeParams.darkfadeAlphaY;
        }
        float f15 = f13;
        float f16 = f14;
        float f17 = f11;
        float f18 = f12;
        float f19 = f9;
        float f20 = f10;
        float f21 = f7;
        float f22 = f8;
        return dialogShadowEdgeParams.copy(f3, f4, f5, f6, f21, f22, f19, f20, f17, f18, f15, f16);
    }

    public static /* synthetic */ void getDarkfadeAlphaX$annotations() {
    }

    public static /* synthetic */ void getDarkfadeAlphaY$annotations() {
    }

    public static /* synthetic */ void getDarkfadeWidthScale$annotations() {
    }

    public static /* synthetic */ void getDarklineAlpha$annotations() {
    }

    public static /* synthetic */ void getDarklineAngle$annotations() {
    }

    public static /* synthetic */ void getDarklineWidth$annotations() {
    }

    public static /* synthetic */ void getFadeAlphaX$annotations() {
    }

    public static /* synthetic */ void getFadeAlphaY$annotations() {
    }

    public static /* synthetic */ void getFadeWidthScale$annotations() {
    }

    public static /* synthetic */ void getLineAlpha$annotations() {
    }

    public static /* synthetic */ void getLineAngle$annotations() {
    }

    public static /* synthetic */ void getLineWidth$annotations() {
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final float getLineWidth() {
        return this.lineWidth;
    }

    /* JADX INFO: renamed from: component10, reason: from getter */
    public final float getFadeAlphaY() {
        return this.fadeAlphaY;
    }

    /* JADX INFO: renamed from: component11, reason: from getter */
    public final float getDarkfadeAlphaX() {
        return this.darkfadeAlphaX;
    }

    /* JADX INFO: renamed from: component12, reason: from getter */
    public final float getDarkfadeAlphaY() {
        return this.darkfadeAlphaY;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final float getDarklineWidth() {
        return this.darklineWidth;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final float getLineAlpha() {
        return this.lineAlpha;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final float getDarklineAlpha() {
        return this.darklineAlpha;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final float getLineAngle() {
        return this.lineAngle;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final float getDarklineAngle() {
        return this.darklineAngle;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final float getFadeWidthScale() {
        return this.fadeWidthScale;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final float getDarkfadeWidthScale() {
        return this.darkfadeWidthScale;
    }

    /* JADX INFO: renamed from: component9, reason: from getter */
    public final float getFadeAlphaX() {
        return this.fadeAlphaX;
    }

    public final DialogShadowEdgeParams copy(float lineWidth, float darklineWidth, float lineAlpha, float darklineAlpha, float lineAngle, float darklineAngle, float fadeWidthScale, float darkfadeWidthScale, float fadeAlphaX, float fadeAlphaY, float darkfadeAlphaX, float darkfadeAlphaY) {
        return new DialogShadowEdgeParams(lineWidth, darklineWidth, lineAlpha, darklineAlpha, lineAngle, darklineAngle, fadeWidthScale, darkfadeWidthScale, fadeAlphaX, fadeAlphaY, darkfadeAlphaX, darkfadeAlphaY);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof DialogShadowEdgeParams)) {
            return false;
        }
        DialogShadowEdgeParams dialogShadowEdgeParams = (DialogShadowEdgeParams) other;
        return Float.compare(this.lineWidth, dialogShadowEdgeParams.lineWidth) == 0 && Float.compare(this.darklineWidth, dialogShadowEdgeParams.darklineWidth) == 0 && Float.compare(this.lineAlpha, dialogShadowEdgeParams.lineAlpha) == 0 && Float.compare(this.darklineAlpha, dialogShadowEdgeParams.darklineAlpha) == 0 && Float.compare(this.lineAngle, dialogShadowEdgeParams.lineAngle) == 0 && Float.compare(this.darklineAngle, dialogShadowEdgeParams.darklineAngle) == 0 && Float.compare(this.fadeWidthScale, dialogShadowEdgeParams.fadeWidthScale) == 0 && Float.compare(this.darkfadeWidthScale, dialogShadowEdgeParams.darkfadeWidthScale) == 0 && Float.compare(this.fadeAlphaX, dialogShadowEdgeParams.fadeAlphaX) == 0 && Float.compare(this.fadeAlphaY, dialogShadowEdgeParams.fadeAlphaY) == 0 && Float.compare(this.darkfadeAlphaX, dialogShadowEdgeParams.darkfadeAlphaX) == 0 && Float.compare(this.darkfadeAlphaY, dialogShadowEdgeParams.darkfadeAlphaY) == 0;
    }

    public final float getDarkfadeAlphaX() {
        return this.darkfadeAlphaX;
    }

    public final float getDarkfadeAlphaY() {
        return this.darkfadeAlphaY;
    }

    public final float getDarkfadeWidthScale() {
        return this.darkfadeWidthScale;
    }

    public final float getDarklineAlpha() {
        return this.darklineAlpha;
    }

    public final float getDarklineAngle() {
        return this.darklineAngle;
    }

    public final float getDarklineWidth() {
        return this.darklineWidth;
    }

    public final float getFadeAlphaX() {
        return this.fadeAlphaX;
    }

    public final float getFadeAlphaY() {
        return this.fadeAlphaY;
    }

    public final float getFadeWidthScale() {
        return this.fadeWidthScale;
    }

    public final float getLineAlpha() {
        return this.lineAlpha;
    }

    public final float getLineAngle() {
        return this.lineAngle;
    }

    public final float getLineWidth() {
        return this.lineWidth;
    }

    public int hashCode() {
        return (((((((((((((((((((((Float.hashCode(this.lineWidth) * 31) + Float.hashCode(this.darklineWidth)) * 31) + Float.hashCode(this.lineAlpha)) * 31) + Float.hashCode(this.darklineAlpha)) * 31) + Float.hashCode(this.lineAngle)) * 31) + Float.hashCode(this.darklineAngle)) * 31) + Float.hashCode(this.fadeWidthScale)) * 31) + Float.hashCode(this.darkfadeWidthScale)) * 31) + Float.hashCode(this.fadeAlphaX)) * 31) + Float.hashCode(this.fadeAlphaY)) * 31) + Float.hashCode(this.darkfadeAlphaX)) * 31) + Float.hashCode(this.darkfadeAlphaY);
    }

    public final void setDarkfadeAlphaX(float f3) {
        this.darkfadeAlphaX = f3;
    }

    public final void setDarkfadeAlphaY(float f3) {
        this.darkfadeAlphaY = f3;
    }

    public final void setDarkfadeWidthScale(float f3) {
        this.darkfadeWidthScale = f3;
    }

    public final void setDarklineAlpha(float f3) {
        this.darklineAlpha = f3;
    }

    public final void setDarklineAngle(float f3) {
        this.darklineAngle = f3;
    }

    public final void setDarklineWidth(float f3) {
        this.darklineWidth = f3;
    }

    public final void setFadeAlphaX(float f3) {
        this.fadeAlphaX = f3;
    }

    public final void setFadeAlphaY(float f3) {
        this.fadeAlphaY = f3;
    }

    public final void setFadeWidthScale(float f3) {
        this.fadeWidthScale = f3;
    }

    public final void setLineAlpha(float f3) {
        this.lineAlpha = f3;
    }

    public final void setLineAngle(float f3) {
        this.lineAngle = f3;
    }

    public final void setLineWidth(float f3) {
        this.lineWidth = f3;
    }

    public String toString() {
        return "DialogShadowEdgeParams(lineWidth=" + this.lineWidth + ", darklineWidth=" + this.darklineWidth + ", lineAlpha=" + this.lineAlpha + ", darklineAlpha=" + this.darklineAlpha + ", lineAngle=" + this.lineAngle + ", darklineAngle=" + this.darklineAngle + ", fadeWidthScale=" + this.fadeWidthScale + ", darkfadeWidthScale=" + this.darkfadeWidthScale + ", fadeAlphaX=" + this.fadeAlphaX + ", fadeAlphaY=" + this.fadeAlphaY + ", darkfadeAlphaX=" + this.darkfadeAlphaX + ", darkfadeAlphaY=" + this.darkfadeAlphaY + ')';
    }

    public DialogShadowEdgeParams(float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14) {
        this.lineWidth = f3;
        this.darklineWidth = f4;
        this.lineAlpha = f5;
        this.darklineAlpha = f6;
        this.lineAngle = f7;
        this.darklineAngle = f8;
        this.fadeWidthScale = f9;
        this.darkfadeWidthScale = f10;
        this.fadeAlphaX = f11;
        this.fadeAlphaY = f12;
        this.darkfadeAlphaX = f13;
        this.darkfadeAlphaY = f14;
    }

    public /* synthetic */ DialogShadowEdgeParams(float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? 4.0f : f3, (i3 & 2) != 0 ? 2.0f : f4, (i3 & 4) != 0 ? 0.6f : f5, (i3 & 8) != 0 ? 0.3f : f6, (i3 & 16) != 0 ? 0.0f : f7, (i3 & 32) != 0 ? 0.0f : f8, (i3 & 64) != 0 ? 1.0f : f9, (i3 & 128) == 0 ? f10 : 1.0f, (i3 & 256) != 0 ? 0.4f : f11, (i3 & 512) != 0 ? 0.0f : f12, (i3 & 1024) != 0 ? 0.03f : f13, (i3 & 2048) != 0 ? 0.0f : f14);
    }
}