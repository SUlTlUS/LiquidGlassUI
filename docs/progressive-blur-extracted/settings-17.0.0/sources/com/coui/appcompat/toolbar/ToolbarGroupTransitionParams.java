package com.coui.appcompat.toolbar;

import com.coui.iola.SpringData;
import com.oplus.settings.feature.language.localepicker.RegionPickerUtils;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\bT\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\b\u0018\u0000 b2\u00020\u0001:\u0001bB¥\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0007\u0012\b\b\u0002\u0010\r\u001a\u00020\u0007\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0007\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0007\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0007\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0007¢\u0006\u0002\u0010\u0014J\t\u0010J\u001a\u00020\u0003HÆ\u0003J\t\u0010K\u001a\u00020\u0007HÆ\u0003J\t\u0010L\u001a\u00020\u0007HÆ\u0003J\t\u0010M\u001a\u00020\u0007HÆ\u0003J\t\u0010N\u001a\u00020\u0007HÆ\u0003J\t\u0010O\u001a\u00020\u0003HÆ\u0003J\t\u0010P\u001a\u00020\u0003HÆ\u0003J\t\u0010Q\u001a\u00020\u0007HÆ\u0003J\t\u0010R\u001a\u00020\u0003HÆ\u0003J\t\u0010S\u001a\u00020\u0003HÆ\u0003J\t\u0010T\u001a\u00020\u0007HÆ\u0003J\t\u0010U\u001a\u00020\u0007HÆ\u0003J\t\u0010V\u001a\u00020\u0003HÆ\u0003J\t\u0010W\u001a\u00020\u0003HÆ\u0003J\t\u0010X\u001a\u00020\u0003HÆ\u0003J\t\u0010Y\u001a\u00020\u0007HÆ\u0003J©\u0001\u0010Z\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00072\b\b\u0002\u0010\r\u001a\u00020\u00072\b\b\u0002\u0010\u000e\u001a\u00020\u00072\b\b\u0002\u0010\u000f\u001a\u00020\u00072\b\b\u0002\u0010\u0010\u001a\u00020\u00072\b\b\u0002\u0010\u0011\u001a\u00020\u00032\b\b\u0002\u0010\u0012\u001a\u00020\u00032\b\b\u0002\u0010\u0013\u001a\u00020\u0007HÆ\u0001J\u0013\u0010[\u001a\u00020\\2\b\u0010]\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010^\u001a\u00020_HÖ\u0001J\t\u0010`\u001a\u00020aHÖ\u0001R \u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u0015\u0010\u0016\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR \u0010\u0005\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u001b\u0010\u0016\u001a\u0004\b\u001c\u0010\u0018\"\u0004\b\u001d\u0010\u001aR \u0010\b\u001a\u00020\u0007X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u001e\u0010\u0016\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R \u0010\u0006\u001a\u00020\u0007X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b#\u0010\u0016\u001a\u0004\b$\u0010 \"\u0004\b%\u0010\"R \u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b&\u0010\u0016\u001a\u0004\b'\u0010\u0018\"\u0004\b(\u0010\u001aR \u0010\t\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b)\u0010\u0016\u001a\u0004\b*\u0010\u0018\"\u0004\b+\u0010\u001aR \u0010\u000b\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b,\u0010\u0016\u001a\u0004\b-\u0010\u0018\"\u0004\b.\u0010\u001aR \u0010\r\u001a\u00020\u0007X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b/\u0010\u0016\u001a\u0004\b0\u0010 \"\u0004\b1\u0010\"R \u0010\n\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b2\u0010\u0016\u001a\u0004\b3\u0010\u0018\"\u0004\b4\u0010\u001aR \u0010\f\u001a\u00020\u0007X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b5\u0010\u0016\u001a\u0004\b6\u0010 \"\u0004\b7\u0010\"R \u0010\u000e\u001a\u00020\u0007X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b8\u0010\u0016\u001a\u0004\b9\u0010 \"\u0004\b:\u0010\"R \u0010\u0010\u001a\u00020\u0007X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b;\u0010\u0016\u001a\u0004\b<\u0010 \"\u0004\b=\u0010\"R \u0010\u000f\u001a\u00020\u0007X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b>\u0010\u0016\u001a\u0004\b?\u0010 \"\u0004\b@\u0010\"R \u0010\u0011\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\bA\u0010\u0016\u001a\u0004\bB\u0010\u0018\"\u0004\bC\u0010\u001aR \u0010\u0013\u001a\u00020\u0007X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\bD\u0010\u0016\u001a\u0004\bE\u0010 \"\u0004\bF\u0010\"R \u0010\u0012\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\bG\u0010\u0016\u001a\u0004\bH\u0010\u0018\"\u0004\bI\u0010\u001a¨\u0006c"}, d2 = {"Lcom/coui/appcompat/toolbar/ToolbarGroupTransitionParams;", "", "buttonEnterAlphaSpring", "Lcom/coui/iola/SpringData;", "buttonEnterScaleSpring", "buttonEnterBlurSpring", "buttonEnterScaleFrom", "", "buttonEnterMaxBlurRadius", "buttonExitAlphaSpring", "buttonExitScaleSpring", "buttonExitBlurSpring", "buttonExitScaleTo", "buttonExitMaxBlurRadius", "buttonGroupExitAlphaMinimumVisibleChange", "buttonGroupExitScaleMinimumVisibleChange", "buttonGroupExitBlurMinimumVisibleChange", "textEnterAlphaSpring", "textExitAlphaSpring", "textExitAlphaMinimumVisibleChange", "(Lcom/coui/iola/SpringData;Lcom/coui/iola/SpringData;Lcom/coui/iola/SpringData;FFLcom/coui/iola/SpringData;Lcom/coui/iola/SpringData;Lcom/coui/iola/SpringData;FFFFFLcom/coui/iola/SpringData;Lcom/coui/iola/SpringData;F)V", "getButtonEnterAlphaSpring$annotations", "()V", "getButtonEnterAlphaSpring", "()Lcom/coui/iola/SpringData;", "setButtonEnterAlphaSpring", "(Lcom/coui/iola/SpringData;)V", "getButtonEnterBlurSpring$annotations", "getButtonEnterBlurSpring", "setButtonEnterBlurSpring", "getButtonEnterMaxBlurRadius$annotations", "getButtonEnterMaxBlurRadius", "()F", "setButtonEnterMaxBlurRadius", "(F)V", "getButtonEnterScaleFrom$annotations", "getButtonEnterScaleFrom", "setButtonEnterScaleFrom", "getButtonEnterScaleSpring$annotations", "getButtonEnterScaleSpring", "setButtonEnterScaleSpring", "getButtonExitAlphaSpring$annotations", "getButtonExitAlphaSpring", "setButtonExitAlphaSpring", "getButtonExitBlurSpring$annotations", "getButtonExitBlurSpring", "setButtonExitBlurSpring", "getButtonExitMaxBlurRadius$annotations", "getButtonExitMaxBlurRadius", "setButtonExitMaxBlurRadius", "getButtonExitScaleSpring$annotations", "getButtonExitScaleSpring", "setButtonExitScaleSpring", "getButtonExitScaleTo$annotations", "getButtonExitScaleTo", "setButtonExitScaleTo", "getButtonGroupExitAlphaMinimumVisibleChange$annotations", "getButtonGroupExitAlphaMinimumVisibleChange", "setButtonGroupExitAlphaMinimumVisibleChange", "getButtonGroupExitBlurMinimumVisibleChange$annotations", "getButtonGroupExitBlurMinimumVisibleChange", "setButtonGroupExitBlurMinimumVisibleChange", "getButtonGroupExitScaleMinimumVisibleChange$annotations", "getButtonGroupExitScaleMinimumVisibleChange", "setButtonGroupExitScaleMinimumVisibleChange", "getTextEnterAlphaSpring$annotations", "getTextEnterAlphaSpring", "setTextEnterAlphaSpring", "getTextExitAlphaMinimumVisibleChange$annotations", "getTextExitAlphaMinimumVisibleChange", "setTextExitAlphaMinimumVisibleChange", "getTextExitAlphaSpring$annotations", "getTextExitAlphaSpring", "setTextExitAlphaSpring", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", RegionPickerUtils.OTHER_SYSTEM_REGION_KEY, "hashCode", "", "toString", "", "Companion", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final /* data */ class ToolbarGroupTransitionParams {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private SpringData buttonEnterAlphaSpring;
    private SpringData buttonEnterBlurSpring;
    private float buttonEnterMaxBlurRadius;
    private float buttonEnterScaleFrom;
    private SpringData buttonEnterScaleSpring;
    private SpringData buttonExitAlphaSpring;
    private SpringData buttonExitBlurSpring;
    private float buttonExitMaxBlurRadius;
    private SpringData buttonExitScaleSpring;
    private float buttonExitScaleTo;
    private float buttonGroupExitAlphaMinimumVisibleChange;
    private float buttonGroupExitBlurMinimumVisibleChange;
    private float buttonGroupExitScaleMinimumVisibleChange;
    private SpringData textEnterAlphaSpring;
    private float textExitAlphaMinimumVisibleChange;
    private SpringData textExitAlphaSpring;

    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lcom/coui/appcompat/toolbar/ToolbarGroupTransitionParams$Companion;", "", "()V", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public ToolbarGroupTransitionParams() {
        this(null, null, null, 0.0f, 0.0f, null, null, null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, null, null, 0.0f, 65535, null);
    }

    public static /* synthetic */ ToolbarGroupTransitionParams copy$default(ToolbarGroupTransitionParams toolbarGroupTransitionParams, SpringData springData, SpringData springData2, SpringData springData3, float f3, float f4, SpringData springData4, SpringData springData5, SpringData springData6, float f5, float f6, float f7, float f8, float f9, SpringData springData7, SpringData springData8, float f10, int i3, Object obj) {
        return toolbarGroupTransitionParams.copy((i3 & 1) != 0 ? toolbarGroupTransitionParams.buttonEnterAlphaSpring : springData, (i3 & 2) != 0 ? toolbarGroupTransitionParams.buttonEnterScaleSpring : springData2, (i3 & 4) != 0 ? toolbarGroupTransitionParams.buttonEnterBlurSpring : springData3, (i3 & 8) != 0 ? toolbarGroupTransitionParams.buttonEnterScaleFrom : f3, (i3 & 16) != 0 ? toolbarGroupTransitionParams.buttonEnterMaxBlurRadius : f4, (i3 & 32) != 0 ? toolbarGroupTransitionParams.buttonExitAlphaSpring : springData4, (i3 & 64) != 0 ? toolbarGroupTransitionParams.buttonExitScaleSpring : springData5, (i3 & 128) != 0 ? toolbarGroupTransitionParams.buttonExitBlurSpring : springData6, (i3 & 256) != 0 ? toolbarGroupTransitionParams.buttonExitScaleTo : f5, (i3 & 512) != 0 ? toolbarGroupTransitionParams.buttonExitMaxBlurRadius : f6, (i3 & 1024) != 0 ? toolbarGroupTransitionParams.buttonGroupExitAlphaMinimumVisibleChange : f7, (i3 & 2048) != 0 ? toolbarGroupTransitionParams.buttonGroupExitScaleMinimumVisibleChange : f8, (i3 & 4096) != 0 ? toolbarGroupTransitionParams.buttonGroupExitBlurMinimumVisibleChange : f9, (i3 & 8192) != 0 ? toolbarGroupTransitionParams.textEnterAlphaSpring : springData7, (i3 & 16384) != 0 ? toolbarGroupTransitionParams.textExitAlphaSpring : springData8, (i3 & 32768) != 0 ? toolbarGroupTransitionParams.textExitAlphaMinimumVisibleChange : f10);
    }

    public static /* synthetic */ void getButtonEnterAlphaSpring$annotations() {
    }

    public static /* synthetic */ void getButtonEnterBlurSpring$annotations() {
    }

    public static /* synthetic */ void getButtonEnterMaxBlurRadius$annotations() {
    }

    public static /* synthetic */ void getButtonEnterScaleFrom$annotations() {
    }

    public static /* synthetic */ void getButtonEnterScaleSpring$annotations() {
    }

    public static /* synthetic */ void getButtonExitAlphaSpring$annotations() {
    }

    public static /* synthetic */ void getButtonExitBlurSpring$annotations() {
    }

    public static /* synthetic */ void getButtonExitMaxBlurRadius$annotations() {
    }

    public static /* synthetic */ void getButtonExitScaleSpring$annotations() {
    }

    public static /* synthetic */ void getButtonExitScaleTo$annotations() {
    }

    public static /* synthetic */ void getButtonGroupExitAlphaMinimumVisibleChange$annotations() {
    }

    public static /* synthetic */ void getButtonGroupExitBlurMinimumVisibleChange$annotations() {
    }

    public static /* synthetic */ void getButtonGroupExitScaleMinimumVisibleChange$annotations() {
    }

    public static /* synthetic */ void getTextEnterAlphaSpring$annotations() {
    }

    public static /* synthetic */ void getTextExitAlphaMinimumVisibleChange$annotations() {
    }

    public static /* synthetic */ void getTextExitAlphaSpring$annotations() {
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final SpringData getButtonEnterAlphaSpring() {
        return this.buttonEnterAlphaSpring;
    }

    /* JADX INFO: renamed from: component10, reason: from getter */
    public final float getButtonExitMaxBlurRadius() {
        return this.buttonExitMaxBlurRadius;
    }

    /* JADX INFO: renamed from: component11, reason: from getter */
    public final float getButtonGroupExitAlphaMinimumVisibleChange() {
        return this.buttonGroupExitAlphaMinimumVisibleChange;
    }

    /* JADX INFO: renamed from: component12, reason: from getter */
    public final float getButtonGroupExitScaleMinimumVisibleChange() {
        return this.buttonGroupExitScaleMinimumVisibleChange;
    }

    /* JADX INFO: renamed from: component13, reason: from getter */
    public final float getButtonGroupExitBlurMinimumVisibleChange() {
        return this.buttonGroupExitBlurMinimumVisibleChange;
    }

    /* JADX INFO: renamed from: component14, reason: from getter */
    public final SpringData getTextEnterAlphaSpring() {
        return this.textEnterAlphaSpring;
    }

    /* JADX INFO: renamed from: component15, reason: from getter */
    public final SpringData getTextExitAlphaSpring() {
        return this.textExitAlphaSpring;
    }

    /* JADX INFO: renamed from: component16, reason: from getter */
    public final float getTextExitAlphaMinimumVisibleChange() {
        return this.textExitAlphaMinimumVisibleChange;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final SpringData getButtonEnterScaleSpring() {
        return this.buttonEnterScaleSpring;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final SpringData getButtonEnterBlurSpring() {
        return this.buttonEnterBlurSpring;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final float getButtonEnterScaleFrom() {
        return this.buttonEnterScaleFrom;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final float getButtonEnterMaxBlurRadius() {
        return this.buttonEnterMaxBlurRadius;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final SpringData getButtonExitAlphaSpring() {
        return this.buttonExitAlphaSpring;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final SpringData getButtonExitScaleSpring() {
        return this.buttonExitScaleSpring;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final SpringData getButtonExitBlurSpring() {
        return this.buttonExitBlurSpring;
    }

    /* JADX INFO: renamed from: component9, reason: from getter */
    public final float getButtonExitScaleTo() {
        return this.buttonExitScaleTo;
    }

    public final ToolbarGroupTransitionParams copy(SpringData buttonEnterAlphaSpring, SpringData buttonEnterScaleSpring, SpringData buttonEnterBlurSpring, float buttonEnterScaleFrom, float buttonEnterMaxBlurRadius, SpringData buttonExitAlphaSpring, SpringData buttonExitScaleSpring, SpringData buttonExitBlurSpring, float buttonExitScaleTo, float buttonExitMaxBlurRadius, float buttonGroupExitAlphaMinimumVisibleChange, float buttonGroupExitScaleMinimumVisibleChange, float buttonGroupExitBlurMinimumVisibleChange, SpringData textEnterAlphaSpring, SpringData textExitAlphaSpring, float textExitAlphaMinimumVisibleChange) {
        Intrinsics.checkNotNullParameter(buttonEnterAlphaSpring, "buttonEnterAlphaSpring");
        Intrinsics.checkNotNullParameter(buttonEnterScaleSpring, "buttonEnterScaleSpring");
        Intrinsics.checkNotNullParameter(buttonEnterBlurSpring, "buttonEnterBlurSpring");
        Intrinsics.checkNotNullParameter(buttonExitAlphaSpring, "buttonExitAlphaSpring");
        Intrinsics.checkNotNullParameter(buttonExitScaleSpring, "buttonExitScaleSpring");
        Intrinsics.checkNotNullParameter(buttonExitBlurSpring, "buttonExitBlurSpring");
        Intrinsics.checkNotNullParameter(textEnterAlphaSpring, "textEnterAlphaSpring");
        Intrinsics.checkNotNullParameter(textExitAlphaSpring, "textExitAlphaSpring");
        return new ToolbarGroupTransitionParams(buttonEnterAlphaSpring, buttonEnterScaleSpring, buttonEnterBlurSpring, buttonEnterScaleFrom, buttonEnterMaxBlurRadius, buttonExitAlphaSpring, buttonExitScaleSpring, buttonExitBlurSpring, buttonExitScaleTo, buttonExitMaxBlurRadius, buttonGroupExitAlphaMinimumVisibleChange, buttonGroupExitScaleMinimumVisibleChange, buttonGroupExitBlurMinimumVisibleChange, textEnterAlphaSpring, textExitAlphaSpring, textExitAlphaMinimumVisibleChange);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ToolbarGroupTransitionParams)) {
            return false;
        }
        ToolbarGroupTransitionParams toolbarGroupTransitionParams = (ToolbarGroupTransitionParams) other;
        return Intrinsics.areEqual(this.buttonEnterAlphaSpring, toolbarGroupTransitionParams.buttonEnterAlphaSpring) && Intrinsics.areEqual(this.buttonEnterScaleSpring, toolbarGroupTransitionParams.buttonEnterScaleSpring) && Intrinsics.areEqual(this.buttonEnterBlurSpring, toolbarGroupTransitionParams.buttonEnterBlurSpring) && Float.compare(this.buttonEnterScaleFrom, toolbarGroupTransitionParams.buttonEnterScaleFrom) == 0 && Float.compare(this.buttonEnterMaxBlurRadius, toolbarGroupTransitionParams.buttonEnterMaxBlurRadius) == 0 && Intrinsics.areEqual(this.buttonExitAlphaSpring, toolbarGroupTransitionParams.buttonExitAlphaSpring) && Intrinsics.areEqual(this.buttonExitScaleSpring, toolbarGroupTransitionParams.buttonExitScaleSpring) && Intrinsics.areEqual(this.buttonExitBlurSpring, toolbarGroupTransitionParams.buttonExitBlurSpring) && Float.compare(this.buttonExitScaleTo, toolbarGroupTransitionParams.buttonExitScaleTo) == 0 && Float.compare(this.buttonExitMaxBlurRadius, toolbarGroupTransitionParams.buttonExitMaxBlurRadius) == 0 && Float.compare(this.buttonGroupExitAlphaMinimumVisibleChange, toolbarGroupTransitionParams.buttonGroupExitAlphaMinimumVisibleChange) == 0 && Float.compare(this.buttonGroupExitScaleMinimumVisibleChange, toolbarGroupTransitionParams.buttonGroupExitScaleMinimumVisibleChange) == 0 && Float.compare(this.buttonGroupExitBlurMinimumVisibleChange, toolbarGroupTransitionParams.buttonGroupExitBlurMinimumVisibleChange) == 0 && Intrinsics.areEqual(this.textEnterAlphaSpring, toolbarGroupTransitionParams.textEnterAlphaSpring) && Intrinsics.areEqual(this.textExitAlphaSpring, toolbarGroupTransitionParams.textExitAlphaSpring) && Float.compare(this.textExitAlphaMinimumVisibleChange, toolbarGroupTransitionParams.textExitAlphaMinimumVisibleChange) == 0;
    }

    public final SpringData getButtonEnterAlphaSpring() {
        return this.buttonEnterAlphaSpring;
    }

    public final SpringData getButtonEnterBlurSpring() {
        return this.buttonEnterBlurSpring;
    }

    public final float getButtonEnterMaxBlurRadius() {
        return this.buttonEnterMaxBlurRadius;
    }

    public final float getButtonEnterScaleFrom() {
        return this.buttonEnterScaleFrom;
    }

    public final SpringData getButtonEnterScaleSpring() {
        return this.buttonEnterScaleSpring;
    }

    public final SpringData getButtonExitAlphaSpring() {
        return this.buttonExitAlphaSpring;
    }

    public final SpringData getButtonExitBlurSpring() {
        return this.buttonExitBlurSpring;
    }

    public final float getButtonExitMaxBlurRadius() {
        return this.buttonExitMaxBlurRadius;
    }

    public final SpringData getButtonExitScaleSpring() {
        return this.buttonExitScaleSpring;
    }

    public final float getButtonExitScaleTo() {
        return this.buttonExitScaleTo;
    }

    public final float getButtonGroupExitAlphaMinimumVisibleChange() {
        return this.buttonGroupExitAlphaMinimumVisibleChange;
    }

    public final float getButtonGroupExitBlurMinimumVisibleChange() {
        return this.buttonGroupExitBlurMinimumVisibleChange;
    }

    public final float getButtonGroupExitScaleMinimumVisibleChange() {
        return this.buttonGroupExitScaleMinimumVisibleChange;
    }

    public final SpringData getTextEnterAlphaSpring() {
        return this.textEnterAlphaSpring;
    }

    public final float getTextExitAlphaMinimumVisibleChange() {
        return this.textExitAlphaMinimumVisibleChange;
    }

    public final SpringData getTextExitAlphaSpring() {
        return this.textExitAlphaSpring;
    }

    public int hashCode() {
        return (((((((((((((((((((((((((((((this.buttonEnterAlphaSpring.hashCode() * 31) + this.buttonEnterScaleSpring.hashCode()) * 31) + this.buttonEnterBlurSpring.hashCode()) * 31) + Float.hashCode(this.buttonEnterScaleFrom)) * 31) + Float.hashCode(this.buttonEnterMaxBlurRadius)) * 31) + this.buttonExitAlphaSpring.hashCode()) * 31) + this.buttonExitScaleSpring.hashCode()) * 31) + this.buttonExitBlurSpring.hashCode()) * 31) + Float.hashCode(this.buttonExitScaleTo)) * 31) + Float.hashCode(this.buttonExitMaxBlurRadius)) * 31) + Float.hashCode(this.buttonGroupExitAlphaMinimumVisibleChange)) * 31) + Float.hashCode(this.buttonGroupExitScaleMinimumVisibleChange)) * 31) + Float.hashCode(this.buttonGroupExitBlurMinimumVisibleChange)) * 31) + this.textEnterAlphaSpring.hashCode()) * 31) + this.textExitAlphaSpring.hashCode()) * 31) + Float.hashCode(this.textExitAlphaMinimumVisibleChange);
    }

    public final void setButtonEnterAlphaSpring(SpringData springData) {
        Intrinsics.checkNotNullParameter(springData, "<set-?>");
        this.buttonEnterAlphaSpring = springData;
    }

    public final void setButtonEnterBlurSpring(SpringData springData) {
        Intrinsics.checkNotNullParameter(springData, "<set-?>");
        this.buttonEnterBlurSpring = springData;
    }

    public final void setButtonEnterMaxBlurRadius(float f3) {
        this.buttonEnterMaxBlurRadius = f3;
    }

    public final void setButtonEnterScaleFrom(float f3) {
        this.buttonEnterScaleFrom = f3;
    }

    public final void setButtonEnterScaleSpring(SpringData springData) {
        Intrinsics.checkNotNullParameter(springData, "<set-?>");
        this.buttonEnterScaleSpring = springData;
    }

    public final void setButtonExitAlphaSpring(SpringData springData) {
        Intrinsics.checkNotNullParameter(springData, "<set-?>");
        this.buttonExitAlphaSpring = springData;
    }

    public final void setButtonExitBlurSpring(SpringData springData) {
        Intrinsics.checkNotNullParameter(springData, "<set-?>");
        this.buttonExitBlurSpring = springData;
    }

    public final void setButtonExitMaxBlurRadius(float f3) {
        this.buttonExitMaxBlurRadius = f3;
    }

    public final void setButtonExitScaleSpring(SpringData springData) {
        Intrinsics.checkNotNullParameter(springData, "<set-?>");
        this.buttonExitScaleSpring = springData;
    }

    public final void setButtonExitScaleTo(float f3) {
        this.buttonExitScaleTo = f3;
    }

    public final void setButtonGroupExitAlphaMinimumVisibleChange(float f3) {
        this.buttonGroupExitAlphaMinimumVisibleChange = f3;
    }

    public final void setButtonGroupExitBlurMinimumVisibleChange(float f3) {
        this.buttonGroupExitBlurMinimumVisibleChange = f3;
    }

    public final void setButtonGroupExitScaleMinimumVisibleChange(float f3) {
        this.buttonGroupExitScaleMinimumVisibleChange = f3;
    }

    public final void setTextEnterAlphaSpring(SpringData springData) {
        Intrinsics.checkNotNullParameter(springData, "<set-?>");
        this.textEnterAlphaSpring = springData;
    }

    public final void setTextExitAlphaMinimumVisibleChange(float f3) {
        this.textExitAlphaMinimumVisibleChange = f3;
    }

    public final void setTextExitAlphaSpring(SpringData springData) {
        Intrinsics.checkNotNullParameter(springData, "<set-?>");
        this.textExitAlphaSpring = springData;
    }

    public String toString() {
        return "ToolbarGroupTransitionParams(buttonEnterAlphaSpring=" + this.buttonEnterAlphaSpring + ", buttonEnterScaleSpring=" + this.buttonEnterScaleSpring + ", buttonEnterBlurSpring=" + this.buttonEnterBlurSpring + ", buttonEnterScaleFrom=" + this.buttonEnterScaleFrom + ", buttonEnterMaxBlurRadius=" + this.buttonEnterMaxBlurRadius + ", buttonExitAlphaSpring=" + this.buttonExitAlphaSpring + ", buttonExitScaleSpring=" + this.buttonExitScaleSpring + ", buttonExitBlurSpring=" + this.buttonExitBlurSpring + ", buttonExitScaleTo=" + this.buttonExitScaleTo + ", buttonExitMaxBlurRadius=" + this.buttonExitMaxBlurRadius + ", buttonGroupExitAlphaMinimumVisibleChange=" + this.buttonGroupExitAlphaMinimumVisibleChange + ", buttonGroupExitScaleMinimumVisibleChange=" + this.buttonGroupExitScaleMinimumVisibleChange + ", buttonGroupExitBlurMinimumVisibleChange=" + this.buttonGroupExitBlurMinimumVisibleChange + ", textEnterAlphaSpring=" + this.textEnterAlphaSpring + ", textExitAlphaSpring=" + this.textExitAlphaSpring + ", textExitAlphaMinimumVisibleChange=" + this.textExitAlphaMinimumVisibleChange + ')';
    }

    public ToolbarGroupTransitionParams(SpringData buttonEnterAlphaSpring, SpringData buttonEnterScaleSpring, SpringData buttonEnterBlurSpring, float f3, float f4, SpringData buttonExitAlphaSpring, SpringData buttonExitScaleSpring, SpringData buttonExitBlurSpring, float f5, float f6, float f7, float f8, float f9, SpringData textEnterAlphaSpring, SpringData textExitAlphaSpring, float f10) {
        Intrinsics.checkNotNullParameter(buttonEnterAlphaSpring, "buttonEnterAlphaSpring");
        Intrinsics.checkNotNullParameter(buttonEnterScaleSpring, "buttonEnterScaleSpring");
        Intrinsics.checkNotNullParameter(buttonEnterBlurSpring, "buttonEnterBlurSpring");
        Intrinsics.checkNotNullParameter(buttonExitAlphaSpring, "buttonExitAlphaSpring");
        Intrinsics.checkNotNullParameter(buttonExitScaleSpring, "buttonExitScaleSpring");
        Intrinsics.checkNotNullParameter(buttonExitBlurSpring, "buttonExitBlurSpring");
        Intrinsics.checkNotNullParameter(textEnterAlphaSpring, "textEnterAlphaSpring");
        Intrinsics.checkNotNullParameter(textExitAlphaSpring, "textExitAlphaSpring");
        this.buttonEnterAlphaSpring = buttonEnterAlphaSpring;
        this.buttonEnterScaleSpring = buttonEnterScaleSpring;
        this.buttonEnterBlurSpring = buttonEnterBlurSpring;
        this.buttonEnterScaleFrom = f3;
        this.buttonEnterMaxBlurRadius = f4;
        this.buttonExitAlphaSpring = buttonExitAlphaSpring;
        this.buttonExitScaleSpring = buttonExitScaleSpring;
        this.buttonExitBlurSpring = buttonExitBlurSpring;
        this.buttonExitScaleTo = f5;
        this.buttonExitMaxBlurRadius = f6;
        this.buttonGroupExitAlphaMinimumVisibleChange = f7;
        this.buttonGroupExitScaleMinimumVisibleChange = f8;
        this.buttonGroupExitBlurMinimumVisibleChange = f9;
        this.textEnterAlphaSpring = textEnterAlphaSpring;
        this.textExitAlphaSpring = textExitAlphaSpring;
        this.textExitAlphaMinimumVisibleChange = f10;
    }

    public /* synthetic */ ToolbarGroupTransitionParams(SpringData springData, SpringData springData2, SpringData springData3, float f3, float f4, SpringData springData4, SpringData springData5, SpringData springData6, float f5, float f6, float f7, float f8, float f9, SpringData springData7, SpringData springData8, float f10, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? new SpringData(0.0f, 0.2f, 0.0f) : springData, (i3 & 2) != 0 ? new SpringData(0.35f, 0.3f, 0.0f) : springData2, (i3 & 4) != 0 ? new SpringData(0.0f, 0.2f, 0.0f) : springData3, (i3 & 8) != 0 ? 0.8f : f3, (i3 & 16) != 0 ? 10.0f : f4, (i3 & 32) != 0 ? new SpringData(0.0f, 0.2f, 0.0f) : springData4, (i3 & 64) != 0 ? new SpringData(0.0f, 0.2f, 0.0f) : springData5, (i3 & 128) != 0 ? new SpringData(0.0f, 0.2f, 0.0f) : springData6, (i3 & 256) == 0 ? f5 : 0.8f, (i3 & 512) == 0 ? f6 : 10.0f, (i3 & 1024) != 0 ? 0.00390625f : f7, (i3 & 2048) != 0 ? 0.001f : f8, (i3 & 4096) != 0 ? 1.0f : f9, (i3 & 8192) != 0 ? new SpringData(0.0f, 0.2f, 0.0f) : springData7, (i3 & 16384) != 0 ? new SpringData(0.0f, 0.2f, 0.0f) : springData8, (i3 & 32768) != 0 ? 0.00390625f : f10);
    }
}