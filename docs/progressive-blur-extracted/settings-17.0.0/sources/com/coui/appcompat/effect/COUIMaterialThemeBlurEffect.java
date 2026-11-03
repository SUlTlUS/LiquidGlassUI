package com.coui.appcompat.effect;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.Color;
import android.view.View;
import com.coui.appcompat.COUIMaterialBlurEffect;
import com.coui.appcompat.COUIMaterialEffect;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.oplus.settings.feature.display.atmospherelight.AtmosphereLightConstants;
import com.oplus.settings.feature.language.localepicker.RegionPickerUtils;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\b\u0007\u0018\u0000 \u00032\u00020\u0001:\u0002\u0003\u0004B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0005"}, d2 = {"Lcom/coui/appcompat/effect/COUIMaterialThemeBlurEffect;", "", "()V", "Companion", "ThemeBlurEffectType", "coui-support-core_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class COUIMaterialThemeBlurEffect {
    private static final Companion.ThemeBlurEffectParams CONTENT_DISABLED_PRIMARY_PARAMS_DARK;
    private static final Companion.ThemeBlurEffectParams CONTENT_DISABLED_PRIMARY_PARAMS_LIGHT;
    private static final Companion.ThemeBlurEffectParams CONTENT_PRIMARY_PARAMS_DARK;
    private static final Companion.ThemeBlurEffectParams CONTENT_PRIMARY_PARAMS_LIGHT;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final Companion.ThemeBlurEffectParams FRAMEWORK_DISABLED_FAB_DARK;
    private static final Companion.ThemeBlurEffectParams FRAMEWORK_DISABLED_FAB_LIGHT;
    private static final Companion.ThemeBlurEffectParams FRAMEWORK_FAB_PARAMS_DARK;
    private static final Companion.ThemeBlurEffectParams FRAMEWORK_FAB_PARAMS_LIGHT;
    private static final Companion.ThemeBlurEffectParams NO_EFFECT;

    @Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001:\u0001\u001dB\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0018\u0010\r\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0007J2\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\u00172\b\b\u0001\u0010\u001a\u001a\u00020\u001bH\u0007J\u0018\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0010\u001a\u00020\u0011H\u0007J3\u0010\u0012\u001a\u00020\u0015*\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\u00172\b\b\u0001\u0010\u001a\u001a\u00020\u001bH\u0007¢\u0006\u0002\b\u001cJ\u0019\u0010\u0012\u001a\u00020\u0015*\u00020\u00152\u0006\u0010\u0010\u001a\u00020\u0011H\u0007¢\u0006\u0002\b\u001cR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001e"}, d2 = {"Lcom/coui/appcompat/effect/COUIMaterialThemeBlurEffect$Companion;", "", "()V", "CONTENT_DISABLED_PRIMARY_PARAMS_DARK", "Lcom/coui/appcompat/effect/COUIMaterialThemeBlurEffect$Companion$ThemeBlurEffectParams;", "CONTENT_DISABLED_PRIMARY_PARAMS_LIGHT", "CONTENT_PRIMARY_PARAMS_DARK", "CONTENT_PRIMARY_PARAMS_LIGHT", "FRAMEWORK_DISABLED_FAB_DARK", "FRAMEWORK_DISABLED_FAB_LIGHT", "FRAMEWORK_FAB_PARAMS_DARK", "FRAMEWORK_FAB_PARAMS_LIGHT", "NO_EFFECT", "getBlurEffectParamsByType", "context", "Landroid/content/Context;", "type", "Lcom/coui/appcompat/effect/COUIMaterialThemeBlurEffect$ThemeBlurEffectType;", "setViewBlurEffect", "", "view", "Landroid/view/View;", "startParams", "Lcom/coui/appcompat/COUIMaterialBlurEffect$BaseBlurEffectParams;", "endParams", "blendModeParams", "progress", "", "setViewBlurEffectExt", "ThemeBlurEffectParams", "coui-support-core_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {

        @Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BE\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u0005\u0012\u0006\u0010\u000b\u001a\u00020\u0007\u0012\u0006\u0010\f\u001a\u00020\t\u0012\u0006\u0010\r\u001a\u00020\u0005¢\u0006\u0002\u0010\u000eJ\t\u0010\u000f\u001a\u00020\u0003HÂ\u0003J\t\u0010\u0010\u001a\u00020\u0005HÂ\u0003J\t\u0010\u0011\u001a\u00020\u0007HÂ\u0003J\t\u0010\u0012\u001a\u00020\tHÂ\u0003J\t\u0010\u0013\u001a\u00020\u0005HÂ\u0003J\t\u0010\u0014\u001a\u00020\u0007HÂ\u0003J\t\u0010\u0015\u001a\u00020\tHÂ\u0003J\t\u0010\u0016\u001a\u00020\u0005HÂ\u0003JY\u0010\u0017\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u00052\b\b\u0002\u0010\u000b\u001a\u00020\u00072\b\b\u0002\u0010\f\u001a\u00020\t2\b\b\u0002\u0010\r\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u0018\u001a\u00020\u00072\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aHÖ\u0003J\u0012\u0010\u001b\u001a\u00020\u00052\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0016J\u0012\u0010\u001e\u001a\u00020\u00052\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0016J\t\u0010\u001f\u001a\u00020\u0005HÖ\u0001J\t\u0010 \u001a\u00020!HÖ\u0001R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\""}, d2 = {"Lcom/coui/appcompat/effect/COUIMaterialThemeBlurEffect$Companion$ThemeBlurEffectParams;", "Lcom/coui/appcompat/COUIMaterialBlurEffect$BaseBlurEffectParams;", "blurRadius", "", "colorLayer1", "", "color1IsAttr", "", "blendModeLayer1", "Landroid/graphics/BlendMode;", "colorLayer2", "color2IsAttr", "blendModeLayer2", "themeAlpha", "(FIZLandroid/graphics/BlendMode;IZLandroid/graphics/BlendMode;I)V", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", RegionPickerUtils.OTHER_SYSTEM_REGION_KEY, "", "getColorLayer1", "context", "Landroid/content/Context;", "getColorLayer2", "hashCode", "toString", "", "coui-support-core_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
        public static final /* data */ class ThemeBlurEffectParams extends COUIMaterialBlurEffect.BaseBlurEffectParams {
            private final BlendMode blendModeLayer1;
            private final BlendMode blendModeLayer2;
            private final float blurRadius;
            private final boolean color1IsAttr;
            private final boolean color2IsAttr;
            private final int colorLayer1;
            private final int colorLayer2;
            private final int themeAlpha;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public ThemeBlurEffectParams(float f3, int i3, boolean z2, BlendMode blendModeLayer1, int i4, boolean z3, BlendMode blendModeLayer2, int i5) {
                super(f3, blendModeLayer1, blendModeLayer2);
                Intrinsics.checkNotNullParameter(blendModeLayer1, "blendModeLayer1");
                Intrinsics.checkNotNullParameter(blendModeLayer2, "blendModeLayer2");
                this.blurRadius = f3;
                this.colorLayer1 = i3;
                this.color1IsAttr = z2;
                this.blendModeLayer1 = blendModeLayer1;
                this.colorLayer2 = i4;
                this.color2IsAttr = z3;
                this.blendModeLayer2 = blendModeLayer2;
                this.themeAlpha = i5;
            }

            /* JADX INFO: renamed from: component1, reason: from getter */
            private final float getBlurRadius() {
                return this.blurRadius;
            }

            /* JADX INFO: renamed from: component2, reason: from getter */
            private final int getColorLayer1() {
                return this.colorLayer1;
            }

            /* JADX INFO: renamed from: component3, reason: from getter */
            private final boolean getColor1IsAttr() {
                return this.color1IsAttr;
            }

            /* JADX INFO: renamed from: component4, reason: from getter */
            private final BlendMode getBlendModeLayer1() {
                return this.blendModeLayer1;
            }

            /* JADX INFO: renamed from: component5, reason: from getter */
            private final int getColorLayer2() {
                return this.colorLayer2;
            }

            /* JADX INFO: renamed from: component6, reason: from getter */
            private final boolean getColor2IsAttr() {
                return this.color2IsAttr;
            }

            /* JADX INFO: renamed from: component7, reason: from getter */
            private final BlendMode getBlendModeLayer2() {
                return this.blendModeLayer2;
            }

            /* JADX INFO: renamed from: component8, reason: from getter */
            private final int getThemeAlpha() {
                return this.themeAlpha;
            }

            public static /* synthetic */ ThemeBlurEffectParams copy$default(ThemeBlurEffectParams themeBlurEffectParams, float f3, int i3, boolean z2, BlendMode blendMode, int i4, boolean z3, BlendMode blendMode2, int i5, int i6, Object obj) {
                if ((i6 & 1) != 0) {
                    f3 = themeBlurEffectParams.blurRadius;
                }
                if ((i6 & 2) != 0) {
                    i3 = themeBlurEffectParams.colorLayer1;
                }
                if ((i6 & 4) != 0) {
                    z2 = themeBlurEffectParams.color1IsAttr;
                }
                if ((i6 & 8) != 0) {
                    blendMode = themeBlurEffectParams.blendModeLayer1;
                }
                if ((i6 & 16) != 0) {
                    i4 = themeBlurEffectParams.colorLayer2;
                }
                if ((i6 & 32) != 0) {
                    z3 = themeBlurEffectParams.color2IsAttr;
                }
                if ((i6 & 64) != 0) {
                    blendMode2 = themeBlurEffectParams.blendModeLayer2;
                }
                if ((i6 & 128) != 0) {
                    i5 = themeBlurEffectParams.themeAlpha;
                }
                BlendMode blendMode3 = blendMode2;
                int i7 = i5;
                int i8 = i4;
                boolean z4 = z3;
                return themeBlurEffectParams.copy(f3, i3, z2, blendMode, i8, z4, blendMode3, i7);
            }

            public final ThemeBlurEffectParams copy(float blurRadius, int colorLayer1, boolean color1IsAttr, BlendMode blendModeLayer1, int colorLayer2, boolean color2IsAttr, BlendMode blendModeLayer2, int themeAlpha) {
                Intrinsics.checkNotNullParameter(blendModeLayer1, "blendModeLayer1");
                Intrinsics.checkNotNullParameter(blendModeLayer2, "blendModeLayer2");
                return new ThemeBlurEffectParams(blurRadius, colorLayer1, color1IsAttr, blendModeLayer1, colorLayer2, color2IsAttr, blendModeLayer2, themeAlpha);
            }

            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                if (!(other instanceof ThemeBlurEffectParams)) {
                    return false;
                }
                ThemeBlurEffectParams themeBlurEffectParams = (ThemeBlurEffectParams) other;
                return Float.compare(this.blurRadius, themeBlurEffectParams.blurRadius) == 0 && this.colorLayer1 == themeBlurEffectParams.colorLayer1 && this.color1IsAttr == themeBlurEffectParams.color1IsAttr && this.blendModeLayer1 == themeBlurEffectParams.blendModeLayer1 && this.colorLayer2 == themeBlurEffectParams.colorLayer2 && this.color2IsAttr == themeBlurEffectParams.color2IsAttr && this.blendModeLayer2 == themeBlurEffectParams.blendModeLayer2 && this.themeAlpha == themeBlurEffectParams.themeAlpha;
            }

            @Override // com.coui.appcompat.COUIMaterialBlurEffect.BaseBlurEffectParams
            public int getColorLayer1(Context context) {
                if (!this.color1IsAttr || context == null) {
                    return this.colorLayer1;
                }
                int attrColor = COUIContextUtil.getAttrColor(context, this.colorLayer1);
                return Color.argb(this.themeAlpha, Color.red(attrColor), Color.green(attrColor), Color.blue(attrColor));
            }

            @Override // com.coui.appcompat.COUIMaterialBlurEffect.BaseBlurEffectParams
            public int getColorLayer2(Context context) {
                if (!this.color2IsAttr || context == null) {
                    return this.colorLayer2;
                }
                int attrColor = COUIContextUtil.getAttrColor(context, this.colorLayer2);
                return Color.argb(this.themeAlpha, Color.red(attrColor), Color.green(attrColor), Color.blue(attrColor));
            }

            public int hashCode() {
                return (((((((((((((Float.hashCode(this.blurRadius) * 31) + Integer.hashCode(this.colorLayer1)) * 31) + Boolean.hashCode(this.color1IsAttr)) * 31) + this.blendModeLayer1.hashCode()) * 31) + Integer.hashCode(this.colorLayer2)) * 31) + Boolean.hashCode(this.color2IsAttr)) * 31) + this.blendModeLayer2.hashCode()) * 31) + Integer.hashCode(this.themeAlpha);
            }

            public String toString() {
                return "ThemeBlurEffectParams(blurRadius=" + this.blurRadius + ", colorLayer1=" + this.colorLayer1 + ", color1IsAttr=" + this.color1IsAttr + ", blendModeLayer1=" + this.blendModeLayer1 + ", colorLayer2=" + this.colorLayer2 + ", color2IsAttr=" + this.color2IsAttr + ", blendModeLayer2=" + this.blendModeLayer2 + ", themeAlpha=" + this.themeAlpha + ')';
            }
        }

        @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
        public /* synthetic */ class WhenMappings {
            public static final /* synthetic */ int[] $EnumSwitchMapping$0;

            static {
                int[] iArr = new int[ThemeBlurEffectType.values().length];
                try {
                    iArr[ThemeBlurEffectType.TYPE_FRAMEWORK_FAB.ordinal()] = 1;
                } catch (NoSuchFieldError unused) {
                }
                try {
                    iArr[ThemeBlurEffectType.TYPE_CONTENT_PRIMARY_BUTTON.ordinal()] = 2;
                } catch (NoSuchFieldError unused2) {
                }
                try {
                    iArr[ThemeBlurEffectType.TYPE_FRAMEWORK_DISABLED_FAB.ordinal()] = 3;
                } catch (NoSuchFieldError unused3) {
                }
                try {
                    iArr[ThemeBlurEffectType.TYPE_CONTENT_DISABLED_PRIMARY_BUTTON.ordinal()] = 4;
                } catch (NoSuchFieldError unused4) {
                }
                try {
                    iArr[ThemeBlurEffectType.TYPE_NO_EFFECT.ordinal()] = 5;
                } catch (NoSuchFieldError unused5) {
                }
                $EnumSwitchMapping$0 = iArr;
            }
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @JvmStatic
        public final ThemeBlurEffectParams getBlurEffectParamsByType(Context context, ThemeBlurEffectType type) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(type, "type");
            int i3 = WhenMappings.$EnumSwitchMapping$0[type.ordinal()];
            if (i3 == 1) {
                return COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(context) ? COUIMaterialThemeBlurEffect.access$getFRAMEWORK_FAB_PARAMS_DARK$cp() : COUIMaterialThemeBlurEffect.access$getFRAMEWORK_FAB_PARAMS_LIGHT$cp();
            }
            if (i3 == 2) {
                return COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(context) ? COUIMaterialThemeBlurEffect.access$getCONTENT_PRIMARY_PARAMS_DARK$cp() : COUIMaterialThemeBlurEffect.access$getCONTENT_PRIMARY_PARAMS_LIGHT$cp();
            }
            if (i3 == 3) {
                return COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(context) ? COUIMaterialThemeBlurEffect.access$getFRAMEWORK_DISABLED_FAB_DARK$cp() : COUIMaterialThemeBlurEffect.access$getFRAMEWORK_DISABLED_FAB_LIGHT$cp();
            }
            if (i3 == 4) {
                return COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(context) ? COUIMaterialThemeBlurEffect.access$getCONTENT_DISABLED_PRIMARY_PARAMS_DARK$cp() : COUIMaterialThemeBlurEffect.access$getCONTENT_DISABLED_PRIMARY_PARAMS_LIGHT$cp();
            }
            if (i3 == 5) {
                return COUIMaterialThemeBlurEffect.access$getNO_EFFECT$cp();
            }
            throw new NoWhenBranchMatchedException();
        }

        @JvmStatic
        public final void setViewBlurEffect(View view, ThemeBlurEffectType type) {
            Intrinsics.checkNotNullParameter(view, "view");
            Intrinsics.checkNotNullParameter(type, "type");
            Context context = view.getContext();
            Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
            COUIMaterialEffect.setViewBlurEffect(view, getBlurEffectParamsByType(context, type));
        }

        @JvmStatic
        public final View setViewBlurEffectExt(View view, ThemeBlurEffectType type) {
            Intrinsics.checkNotNullParameter(view, "<this>");
            Intrinsics.checkNotNullParameter(type, "type");
            setViewBlurEffect(view, type);
            return view;
        }

        private Companion() {
        }

        @JvmStatic
        public final View setViewBlurEffectExt(View view, COUIMaterialBlurEffect.BaseBlurEffectParams startParams, COUIMaterialBlurEffect.BaseBlurEffectParams endParams, COUIMaterialBlurEffect.BaseBlurEffectParams blendModeParams, float f3) {
            Intrinsics.checkNotNullParameter(view, "<this>");
            Intrinsics.checkNotNullParameter(startParams, "startParams");
            Intrinsics.checkNotNullParameter(endParams, "endParams");
            Intrinsics.checkNotNullParameter(blendModeParams, "blendModeParams");
            setViewBlurEffect(view, startParams, endParams, blendModeParams, f3);
            return view;
        }

        @JvmStatic
        public final void setViewBlurEffect(View view, COUIMaterialBlurEffect.BaseBlurEffectParams startParams, COUIMaterialBlurEffect.BaseBlurEffectParams endParams, COUIMaterialBlurEffect.BaseBlurEffectParams blendModeParams, float progress) {
            Intrinsics.checkNotNullParameter(view, "view");
            Intrinsics.checkNotNullParameter(startParams, "startParams");
            Intrinsics.checkNotNullParameter(endParams, "endParams");
            Intrinsics.checkNotNullParameter(blendModeParams, "blendModeParams");
            COUIMaterialEffect.setViewBlurEffect(view, startParams, endParams, blendModeParams, progress);
        }
    }

    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007¨\u0006\b"}, d2 = {"Lcom/coui/appcompat/effect/COUIMaterialThemeBlurEffect$ThemeBlurEffectType;", "", "(Ljava/lang/String;I)V", "TYPE_NO_EFFECT", "TYPE_FRAMEWORK_FAB", "TYPE_FRAMEWORK_DISABLED_FAB", "TYPE_CONTENT_PRIMARY_BUTTON", "TYPE_CONTENT_DISABLED_PRIMARY_BUTTON", "coui-support-core_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public enum ThemeBlurEffectType {
        TYPE_NO_EFFECT,
        TYPE_FRAMEWORK_FAB,
        TYPE_FRAMEWORK_DISABLED_FAB,
        TYPE_CONTENT_PRIMARY_BUTTON,
        TYPE_CONTENT_DISABLED_PRIMARY_BUTTON;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

        public static EnumEntries<ThemeBlurEffectType> getEntries() {
            return $ENTRIES;
        }
    }

    static {
        BlendMode blendMode = BlendMode.SRC_OVER;
        NO_EFFECT = new Companion.ThemeBlurEffectParams(0.0f, 0, false, blendMode, 0, false, blendMode, 0);
        int i3 = com.support.appcompat.R.attr.couiColorContainerTheme;
        int color = Color.parseColor("#1AFFFFFF");
        BlendMode blendMode2 = BlendMode.OVERLAY;
        FRAMEWORK_FAB_PARAMS_LIGHT = new Companion.ThemeBlurEffectParams(10.0f, i3, true, blendMode, color, false, blendMode2, AtmosphereLightConstants.EffectId.ATMOSPHERE_FAN_BREATH);
        FRAMEWORK_DISABLED_FAB_LIGHT = new Companion.ThemeBlurEffectParams(10.0f, i3, true, blendMode, Color.parseColor("#19FFFFFF"), false, blendMode2, 77);
        CONTENT_PRIMARY_PARAMS_LIGHT = new Companion.ThemeBlurEffectParams(10.0f, i3, true, blendMode, Color.parseColor("#00FFFFFF"), false, blendMode2, AtmosphereLightConstants.EffectId.ATMOSPHERE_FAN_BREATH);
        CONTENT_DISABLED_PRIMARY_PARAMS_LIGHT = new Companion.ThemeBlurEffectParams(10.0f, i3, true, blendMode, Color.parseColor("#19FFFFFF"), false, blendMode2, 77);
        FRAMEWORK_FAB_PARAMS_DARK = new Companion.ThemeBlurEffectParams(10.0f, i3, true, blendMode, Color.parseColor("#19FFFFFF"), false, blendMode2, 230);
        FRAMEWORK_DISABLED_FAB_DARK = new Companion.ThemeBlurEffectParams(10.0f, i3, true, blendMode, Color.parseColor("#19FFFFFF"), false, blendMode2, 77);
        CONTENT_PRIMARY_PARAMS_DARK = new Companion.ThemeBlurEffectParams(10.0f, i3, true, blendMode, Color.parseColor("#19FFFFFF"), false, blendMode2, 230);
        CONTENT_DISABLED_PRIMARY_PARAMS_DARK = new Companion.ThemeBlurEffectParams(10.0f, i3, true, blendMode, Color.parseColor("#19FFFFFF"), false, blendMode2, 77);
    }

    public static final /* synthetic */ Companion.ThemeBlurEffectParams access$getCONTENT_DISABLED_PRIMARY_PARAMS_DARK$cp() {
        return CONTENT_DISABLED_PRIMARY_PARAMS_DARK;
    }

    public static final /* synthetic */ Companion.ThemeBlurEffectParams access$getCONTENT_DISABLED_PRIMARY_PARAMS_LIGHT$cp() {
        return CONTENT_DISABLED_PRIMARY_PARAMS_LIGHT;
    }

    public static final /* synthetic */ Companion.ThemeBlurEffectParams access$getCONTENT_PRIMARY_PARAMS_DARK$cp() {
        return CONTENT_PRIMARY_PARAMS_DARK;
    }

    public static final /* synthetic */ Companion.ThemeBlurEffectParams access$getCONTENT_PRIMARY_PARAMS_LIGHT$cp() {
        return CONTENT_PRIMARY_PARAMS_LIGHT;
    }

    public static final /* synthetic */ Companion.ThemeBlurEffectParams access$getFRAMEWORK_DISABLED_FAB_DARK$cp() {
        return FRAMEWORK_DISABLED_FAB_DARK;
    }

    public static final /* synthetic */ Companion.ThemeBlurEffectParams access$getFRAMEWORK_DISABLED_FAB_LIGHT$cp() {
        return FRAMEWORK_DISABLED_FAB_LIGHT;
    }

    public static final /* synthetic */ Companion.ThemeBlurEffectParams access$getFRAMEWORK_FAB_PARAMS_DARK$cp() {
        return FRAMEWORK_FAB_PARAMS_DARK;
    }

    public static final /* synthetic */ Companion.ThemeBlurEffectParams access$getFRAMEWORK_FAB_PARAMS_LIGHT$cp() {
        return FRAMEWORK_FAB_PARAMS_LIGHT;
    }

    public static final /* synthetic */ Companion.ThemeBlurEffectParams access$getNO_EFFECT$cp() {
        return NO_EFFECT;
    }

    @JvmStatic
    public static final Companion.ThemeBlurEffectParams getBlurEffectParamsByType(Context context, ThemeBlurEffectType themeBlurEffectType) {
        return INSTANCE.getBlurEffectParamsByType(context, themeBlurEffectType);
    }

    @JvmStatic
    public static final void setViewBlurEffect(View view, COUIMaterialBlurEffect.BaseBlurEffectParams baseBlurEffectParams, COUIMaterialBlurEffect.BaseBlurEffectParams baseBlurEffectParams2, COUIMaterialBlurEffect.BaseBlurEffectParams baseBlurEffectParams3, float f3) {
        INSTANCE.setViewBlurEffect(view, baseBlurEffectParams, baseBlurEffectParams2, baseBlurEffectParams3, f3);
    }

    @JvmStatic
    public static final View setViewBlurEffectExt(View view, COUIMaterialBlurEffect.BaseBlurEffectParams baseBlurEffectParams, COUIMaterialBlurEffect.BaseBlurEffectParams baseBlurEffectParams2, COUIMaterialBlurEffect.BaseBlurEffectParams baseBlurEffectParams3, float f3) {
        return INSTANCE.setViewBlurEffectExt(view, baseBlurEffectParams, baseBlurEffectParams2, baseBlurEffectParams3, f3);
    }

    @JvmStatic
    public static final void setViewBlurEffect(View view, ThemeBlurEffectType themeBlurEffectType) {
        INSTANCE.setViewBlurEffect(view, themeBlurEffectType);
    }

    @JvmStatic
    public static final View setViewBlurEffectExt(View view, ThemeBlurEffectType themeBlurEffectType) {
        return INSTANCE.setViewBlurEffectExt(view, themeBlurEffectType);
    }
}