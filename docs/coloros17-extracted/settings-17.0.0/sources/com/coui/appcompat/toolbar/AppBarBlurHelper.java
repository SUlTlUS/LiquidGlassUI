package com.coui.appcompat.toolbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.graphics.drawable.StateListDrawable;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.ColorUtils;
import com.coui.appcompat.COUIMaterialEffect;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.OnMaterialStateChangeListener;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.log.COUILog;
import com.google.android.material.appbar.COUIAppBarLayout;
import com.google.android.material.appbar.COUIDividerAppBarLayout;
import com.google.android.material.appbar.COUIFloatingScrollingViewBehavior;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.oplus.graphics.OplusRenderEffect;
import com.oplus.settings.feature.deviceinfo.regulatory.RegulatoryNodes;
import com.oplus.settings.feature.language.localepicker.RegionPickerUtils;
import com.support.appcompat.R;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.WeakHashMap;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.ranges.RangesKt;

/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000\u008c\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u001f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\f\bÆ\u0002\u0018\u00002\u00020\u0001:\u0004jklmB\u0007\b\u0002¢\u0006\u0002\u0010\u0002J)\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u00122\u0006\u0010 \u001a\u00020\u00122\n\b\u0002\u0010!\u001a\u0004\u0018\u00010\u0004H\u0003¢\u0006\u0002\u0010\"J\u0018\u0010#\u001a\u00020\u001e2\u0006\u0010$\u001a\u00020\u00122\u0006\u0010%\u001a\u00020\u0007H\u0002J\u0010\u0010&\u001a\u00020\u001e2\u0006\u0010'\u001a\u00020\u000fH\u0003J\u0010\u0010(\u001a\u00020\u001e2\u0006\u0010$\u001a\u00020\u0012H\u0003J\u001a\u0010)\u001a\u00020\u001e2\u0006\u0010$\u001a\u00020\u00122\b\b\u0002\u0010*\u001a\u00020\u0012H\u0002J\u0010\u0010+\u001a\u00020\u001e2\u0006\u0010'\u001a\u00020\u000fH\u0003J\b\u0010,\u001a\u00020\u001eH\u0002J\u0010\u0010-\u001a\u00020\u00122\u0006\u0010'\u001a\u00020\u000fH\u0002J\u0010\u0010.\u001a\u00020\u001e2\u0006\u0010'\u001a\u00020\u000fH\u0003J\u0010\u0010/\u001a\u00020\u001e2\u0006\u0010 \u001a\u00020\u0012H\u0003J\u0010\u00100\u001a\u00020\u001e2\u0006\u0010'\u001a\u00020\u000fH\u0003J\u0010\u00101\u001a\u00020\u001e2\u0006\u0010$\u001a\u00020\u0012H\u0007J\u0010\u00102\u001a\u00020\u001e2\u0006\u0010 \u001a\u00020\u0012H\u0003J\u0010\u00103\u001a\u00020\u001e2\u0006\u0010$\u001a\u00020\u0012H\u0002J\u0010\u00104\u001a\u00020\u001e2\u0006\u0010'\u001a\u00020\u000fH\u0007J\u0010\u00105\u001a\u00020\u001e2\u0006\u0010'\u001a\u00020\u000fH\u0007J\u0010\u00106\u001a\u00020\u001e2\u0006\u0010'\u001a\u00020\u000fH\u0002J\u001c\u00107\u001a\u0004\u0018\u00010\u00132\u0006\u00108\u001a\u00020\u00122\b\u00109\u001a\u0004\u0018\u00010\u0013H\u0002J!\u0010:\u001a\u00020\u00132\u0006\u0010\u001f\u001a\u00020\u00122\n\b\u0002\u0010!\u001a\u0004\u0018\u00010\u0004H\u0003¢\u0006\u0002\u0010;J$\u0010<\u001a\u00020\u001e2\u0006\u0010*\u001a\u00020\u00122\u0012\u0010=\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u001e0>H\u0003J\u0012\u0010?\u001a\u0004\u0018\u00010@2\u0006\u0010'\u001a\u00020\u000fH\u0002J\u0012\u0010A\u001a\u0004\u0018\u00010B2\u0006\u0010'\u001a\u00020\u000fH\u0002J\u0012\u0010C\u001a\u0004\u0018\u00010B2\u0006\u0010'\u001a\u00020\u000fH\u0007J\u0010\u0010D\u001a\u00020E2\u0006\u0010*\u001a\u00020\u0012H\u0002J\b\u0010F\u001a\u00020EH\u0002J\u0010\u0010G\u001a\u00020\u001e2\u0006\u0010'\u001a\u00020\u000fH\u0007J\u0010\u0010H\u001a\u00020\u001e2\u0006\u0010'\u001a\u00020\u000fH\u0007J\u0018\u0010I\u001a\u00020\u001e2\u0006\u0010*\u001a\u00020\u00122\u0006\u0010J\u001a\u00020\u0015H\u0003J\u0010\u0010K\u001a\u00020\u001e2\u0006\u0010*\u001a\u00020\u0012H\u0003J0\u0010L\u001a\u00020\u00042\u0006\u0010$\u001a\u00020\u00122\b\u0010M\u001a\u0004\u0018\u00010\u00042\u000e\b\u0002\u0010N\u001a\b\u0012\u0004\u0012\u00020\u00040OH\u0082\b¢\u0006\u0002\u0010PJ\u0010\u0010Q\u001a\u00020\u00042\u0006\u0010'\u001a\u00020\u000fH\u0002J\u0010\u0010R\u001a\u00020\u00042\u0006\u0010S\u001a\u00020TH\u0002J\u0019\u0010U\u001a\u0004\u0018\u00010\u00042\b\u0010V\u001a\u0004\u0018\u00010\u0013H\u0002¢\u0006\u0002\u0010WJ\u0010\u0010X\u001a\u00020\u00042\u0006\u0010$\u001a\u00020\u0012H\u0002J\u0010\u0010Y\u001a\u00020\u001e2\u0006\u0010'\u001a\u00020\u000fH\u0003J\u0010\u0010Z\u001a\u00020\u001e2\u0006\u0010[\u001a\u00020\u0004H\u0007J\u001a\u0010\\\u001a\u00020\u001e2\u0006\u0010$\u001a\u00020\u00122\b\u0010]\u001a\u0004\u0018\u00010\u0019H\u0007J\u001a\u0010^\u001a\u00020\u001e2\u0006\u0010$\u001a\u00020\u00122\b\u0010]\u001a\u0004\u0018\u00010\u001bH\u0007J\u0010\u0010_\u001a\u00020\u001e2\u0006\u0010$\u001a\u00020\u0012H\u0007J\u0010\u0010_\u001a\u00020\u001e2\u0006\u0010'\u001a\u00020\u000fH\u0007J\u0010\u0010`\u001a\u00020\u001e2\u0006\u0010'\u001a\u00020\u000fH\u0007J\b\u0010a\u001a\u00020bH\u0002J\u0010\u0010c\u001a\u00020\u001e2\u0006\u0010'\u001a\u00020\u000fH\u0003J\u0018\u0010d\u001a\u00020\u001e2\u0006\u0010$\u001a\u00020\u00122\u0006\u0010e\u001a\u00020\u0007H\u0007J\u0015\u0010f\u001a\u0004\u0018\u00010\u0004*\u0004\u0018\u00010\u0004H\u0002¢\u0006\u0002\u0010gJ\u0013\u0010h\u001a\u0004\u0018\u00010\u0004*\u00020TH\u0002¢\u0006\u0002\u0010iR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082T¢\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00100\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u0011\u001a\u0010\u0012\u0004\u0012\u00020\u0012\u0012\u0006\u0012\u0004\u0018\u00010\u00130\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00150\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0016\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0017R\u001a\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00190\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u001b0\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00040\u000eX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006n"}, d2 = {"Lcom/coui/appcompat/toolbar/AppBarBlurHelper;", "", "()V", "ALPHA_MAX", "", "ALPHA_MIN", "FULL_ALPHA_FLOAT", "", "GRADIENT_DARK_ALPHA", "MAX_DRAWABLE_UNWRAP_DEPTH", "NORMAL_BLUR_DARK_ALPHA", "TAG", "", "appBarAppearanceRestoreStates", "Ljava/util/WeakHashMap;", "Lcom/google/android/material/appbar/COUIAppBarLayout;", "Lcom/coui/appcompat/toolbar/AppBarBlurHelper$AppBarAppearanceRestoreState;", "backgroundRestoreSnapshots", "Landroid/view/View;", "Landroid/graphics/drawable/Drawable;", "blurWatchers", "Lcom/coui/appcompat/toolbar/AppBarBlurHelper$MaterialBlurWatcher;", "gradientBlurBgColor", "Ljava/lang/Integer;", "gradientBlurConfigs", "Lcom/coui/appcompat/toolbar/AppBarBlurHelper$GradientBlurConfig;", "normalBlurConfigs", "Lcom/coui/appcompat/toolbar/AppBarBlurHelper$NormalBlurConfig;", "normalBlurOriginalColors", "applyBlurAndGradientBackground", "", "colorSourceView", "blurTargetView", "resolvedBaseColor", "(Landroid/view/View;Landroid/view/View;Ljava/lang/Integer;)V", "applyGradientBackgroundFade", "view", "blurFactor", "applyGradientBlurBackgroundAppBar", "appBarLayout", "applyGradientBlurBackgroundPlain", "applyGradientBlurRenderEffect", "anchor", "applyNormalBlurBackground", "assertMainThread", "blurViewForAppBar", "captureAppBarAppearanceBeforeTransparent", "captureBackgroundBeforeApplyingBlurLayer", "clearBlurDividerSuppression", "clearGradientBlur", "clearGradientBlurEffectAndRestoreBackground", "clearGradientBlurEffectOnly", "clearGradientBlurForAppBar", "clearNormalBlurForAppBar", "clearNormalRenderEffect", "cloneDrawableForRestore", "host", "d", "createGradientBackground", "(Landroid/view/View;Ljava/lang/Integer;)Landroid/graphics/drawable/Drawable;", "ensureMaterialBlurWatcher", "onMaterialBlurChanged", "Lkotlin/Function1;", "findFloatingBehavior", "Lcom/google/android/material/appbar/COUIFloatingScrollingViewBehavior;", "findOverlayInParent", "Lcom/coui/appcompat/toolbar/AppBarBlurOverlayView;", "getOverlayView", "mergedParams", "Lcom/coui/appcompat/toolbar/AppBarBlurParams;", "params", "refreshGradientBlurConfig", "refreshNormalBlurConfig", "removeBlurWatcherIfCurrent", "watcher", "removeMaterialBlurWatcherIfAny", "resolveBaseColor", "explicitColor", "fallback", "Lkotlin/Function0;", "(Landroid/view/View;Ljava/lang/Integer;Lkotlin/jvm/functions/Function0;)I", "resolveGradientBlurBaseColorForAppBar", "resolvePageFallbackBackgroundColor", "context", "Landroid/content/Context;", "resolveSolidBackgroundColorFromDrawable", RegulatoryNodes.ATTRIBUTE_DRAWABLE, "(Landroid/graphics/drawable/Drawable;)Ljava/lang/Integer;", "resolveSolidBackgroundColorOrPageFallback", "restoreAppBarAppearance", "setGradientBlurBgColor", TypedValues.Custom.S_COLOR, "setGradientBlurConfig", "config", "setNormalBlurConfig", "setUpGradientBlurBackground", "setUpNormalBlurBackground", "shouldEnableBlur", "", "syncAppBarDividerWithMaterialBlur", "updateGradientBlurFraction", "fraction", "takeIfOpaque", "(Ljava/lang/Integer;)Ljava/lang/Integer;", "windowBackgroundOpaqueColor", "(Landroid/content/Context;)Ljava/lang/Integer;", "AppBarAppearanceRestoreState", "GradientBlurConfig", "MaterialBlurWatcher", "NormalBlurConfig", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class AppBarBlurHelper {
    private static final int ALPHA_MAX = 255;
    private static final int ALPHA_MIN = 0;
    private static final float FULL_ALPHA_FLOAT = 255.0f;
    private static final float GRADIENT_DARK_ALPHA = 0.4f;
    private static final int MAX_DRAWABLE_UNWRAP_DEPTH = 8;
    private static final float NORMAL_BLUR_DARK_ALPHA = 0.6f;
    private static final String TAG = "AppBarBlurHelper";
    private static volatile Integer gradientBlurBgColor;
    public static final AppBarBlurHelper INSTANCE = new AppBarBlurHelper();
    private static final WeakHashMap<View, MaterialBlurWatcher> blurWatchers = new WeakHashMap<>();
    private static final WeakHashMap<View, Drawable> backgroundRestoreSnapshots = new WeakHashMap<>();
    private static final WeakHashMap<COUIAppBarLayout, AppBarAppearanceRestoreState> appBarAppearanceRestoreStates = new WeakHashMap<>();
    private static final WeakHashMap<View, GradientBlurConfig> gradientBlurConfigs = new WeakHashMap<>();
    private static final WeakHashMap<View, NormalBlurConfig> normalBlurConfigs = new WeakHashMap<>();
    private static final WeakHashMap<View, Integer> normalBlurOriginalColors = new WeakHashMap<>();

    /* JADX INFO: loaded from: classes7.dex */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B\u0017\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u000b\u0010\u000b\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010\f\u001a\u00020\u0005HÆ\u0003J\u001f\u0010\r\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0015"}, d2 = {"Lcom/coui/appcompat/toolbar/AppBarBlurHelper$AppBarAppearanceRestoreState;", "", "background", "Landroid/graphics/drawable/Drawable;", "targetElevation", "", "(Landroid/graphics/drawable/Drawable;F)V", "getBackground", "()Landroid/graphics/drawable/Drawable;", "getTargetElevation", "()F", "component1", "component2", "copy", "equals", "", RegionPickerUtils.OTHER_SYSTEM_REGION_KEY, "hashCode", "", "toString", "", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final /* data */ class AppBarAppearanceRestoreState {
        private final Drawable background;
        private final float targetElevation;

        public AppBarAppearanceRestoreState(Drawable drawable, float f3) {
            this.background = drawable;
            this.targetElevation = f3;
        }

        public static /* synthetic */ AppBarAppearanceRestoreState copy$default(AppBarAppearanceRestoreState appBarAppearanceRestoreState, Drawable drawable, float f3, int i3, Object obj) {
            if ((i3 & 1) != 0) {
                drawable = appBarAppearanceRestoreState.background;
            }
            if ((i3 & 2) != 0) {
                f3 = appBarAppearanceRestoreState.targetElevation;
            }
            return appBarAppearanceRestoreState.copy(drawable, f3);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final Drawable getBackground() {
            return this.background;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final float getTargetElevation() {
            return this.targetElevation;
        }

        public final AppBarAppearanceRestoreState copy(Drawable background, float targetElevation) {
            return new AppBarAppearanceRestoreState(background, targetElevation);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof AppBarAppearanceRestoreState)) {
                return false;
            }
            AppBarAppearanceRestoreState appBarAppearanceRestoreState = (AppBarAppearanceRestoreState) other;
            return Intrinsics.areEqual(this.background, appBarAppearanceRestoreState.background) && Float.compare(this.targetElevation, appBarAppearanceRestoreState.targetElevation) == 0;
        }

        public final Drawable getBackground() {
            return this.background;
        }

        public final float getTargetElevation() {
            return this.targetElevation;
        }

        public int hashCode() {
            Drawable drawable = this.background;
            return ((drawable == null ? 0 : drawable.hashCode()) * 31) + Float.hashCode(this.targetElevation);
        }

        public String toString() {
            return "AppBarAppearanceRestoreState(background=" + this.background + ", targetElevation=" + this.targetElevation + ')';
        }
    }

    @Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0014\n\u0002\b\u001f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001Bw\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\u0007\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u000b\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000b\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000b¢\u0006\u0002\u0010\u000fJ\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\u0017J\u000b\u0010\u001f\u001a\u0004\u0018\u00010\u000bHÆ\u0003J\u0010\u0010 \u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\u0017J\u0010\u0010!\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\u0017J\t\u0010\"\u001a\u00020\u0007HÆ\u0003J\t\u0010#\u001a\u00020\u0007HÆ\u0003J\t\u0010$\u001a\u00020\u0007HÆ\u0003J\u000b\u0010%\u001a\u0004\u0018\u00010\u000bHÆ\u0003J\u000b\u0010&\u001a\u0004\u0018\u00010\u000bHÆ\u0003J\u000b\u0010'\u001a\u0004\u0018\u00010\u000bHÆ\u0003J\u0080\u0001\u0010(\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\u00072\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u000b2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000b2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000bHÆ\u0001¢\u0006\u0002\u0010)J\u0013\u0010*\u001a\u00020+2\b\u0010,\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010-\u001a\u00020\u0007HÖ\u0001J\t\u0010.\u001a\u00020/HÖ\u0001R\u0011\u0010\b\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0013\u0010\r\u001a\u0004\u0018\u00010\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0014R\u0015\u0010\u0004\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010\u0018\u001a\u0004\b\u0016\u0010\u0017R\u0015\u0010\u0005\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010\u0018\u001a\u0004\b\u0019\u0010\u0017R\u0013\u0010\f\u001a\u0004\u0018\u00010\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0014R\u0013\u0010\n\u001a\u0004\u0018\u00010\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0014R\u0011\u0010\t\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0011R\u0015\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010\u0018\u001a\u0004\b\u001d\u0010\u0017¨\u00060"}, d2 = {"Lcom/coui/appcompat/toolbar/AppBarBlurHelper$GradientBlurConfig;", "", "startBlurRadius", "", "endBlurRadius", "gradientBlurSpeed", "blendMode", "", "blendC", "mixC", "lightPositions", "", "lightAlphas", "darkPositions", "darkAlphas", "(Ljava/lang/Float;Ljava/lang/Float;Ljava/lang/Float;III[F[F[F[F)V", "getBlendC", "()I", "getBlendMode", "getDarkAlphas", "()[F", "getDarkPositions", "getEndBlurRadius", "()Ljava/lang/Float;", "Ljava/lang/Float;", "getGradientBlurSpeed", "getLightAlphas", "getLightPositions", "getMixC", "getStartBlurRadius", "component1", "component10", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/Float;Ljava/lang/Float;Ljava/lang/Float;III[F[F[F[F)Lcom/coui/appcompat/toolbar/AppBarBlurHelper$GradientBlurConfig;", "equals", "", RegionPickerUtils.OTHER_SYSTEM_REGION_KEY, "hashCode", "toString", "", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final /* data */ class GradientBlurConfig {
        private final int blendC;
        private final int blendMode;
        private final float[] darkAlphas;
        private final float[] darkPositions;
        private final Float endBlurRadius;
        private final Float gradientBlurSpeed;
        private final float[] lightAlphas;
        private final float[] lightPositions;
        private final int mixC;
        private final Float startBlurRadius;

        public GradientBlurConfig() {
            this(null, null, null, 0, 0, 0, null, null, null, null, 1023, null);
        }

        public static /* synthetic */ GradientBlurConfig copy$default(GradientBlurConfig gradientBlurConfig, Float f3, Float f4, Float f5, int i3, int i4, int i5, float[] fArr, float[] fArr2, float[] fArr3, float[] fArr4, int i6, Object obj) {
            if ((i6 & 1) != 0) {
                f3 = gradientBlurConfig.startBlurRadius;
            }
            if ((i6 & 2) != 0) {
                f4 = gradientBlurConfig.endBlurRadius;
            }
            if ((i6 & 4) != 0) {
                f5 = gradientBlurConfig.gradientBlurSpeed;
            }
            if ((i6 & 8) != 0) {
                i3 = gradientBlurConfig.blendMode;
            }
            if ((i6 & 16) != 0) {
                i4 = gradientBlurConfig.blendC;
            }
            if ((i6 & 32) != 0) {
                i5 = gradientBlurConfig.mixC;
            }
            if ((i6 & 64) != 0) {
                fArr = gradientBlurConfig.lightPositions;
            }
            if ((i6 & 128) != 0) {
                fArr2 = gradientBlurConfig.lightAlphas;
            }
            if ((i6 & 256) != 0) {
                fArr3 = gradientBlurConfig.darkPositions;
            }
            if ((i6 & 512) != 0) {
                fArr4 = gradientBlurConfig.darkAlphas;
            }
            float[] fArr5 = fArr3;
            float[] fArr6 = fArr4;
            float[] fArr7 = fArr;
            float[] fArr8 = fArr2;
            int i7 = i4;
            int i8 = i5;
            return gradientBlurConfig.copy(f3, f4, f5, i3, i7, i8, fArr7, fArr8, fArr5, fArr6);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final Float getStartBlurRadius() {
            return this.startBlurRadius;
        }

        /* JADX INFO: renamed from: component10, reason: from getter */
        public final float[] getDarkAlphas() {
            return this.darkAlphas;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final Float getEndBlurRadius() {
            return this.endBlurRadius;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final Float getGradientBlurSpeed() {
            return this.gradientBlurSpeed;
        }

        /* JADX INFO: renamed from: component4, reason: from getter */
        public final int getBlendMode() {
            return this.blendMode;
        }

        /* JADX INFO: renamed from: component5, reason: from getter */
        public final int getBlendC() {
            return this.blendC;
        }

        /* JADX INFO: renamed from: component6, reason: from getter */
        public final int getMixC() {
            return this.mixC;
        }

        /* JADX INFO: renamed from: component7, reason: from getter */
        public final float[] getLightPositions() {
            return this.lightPositions;
        }

        /* JADX INFO: renamed from: component8, reason: from getter */
        public final float[] getLightAlphas() {
            return this.lightAlphas;
        }

        /* JADX INFO: renamed from: component9, reason: from getter */
        public final float[] getDarkPositions() {
            return this.darkPositions;
        }

        public final GradientBlurConfig copy(Float startBlurRadius, Float endBlurRadius, Float gradientBlurSpeed, int blendMode, int blendC, int mixC, float[] lightPositions, float[] lightAlphas, float[] darkPositions, float[] darkAlphas) {
            return new GradientBlurConfig(startBlurRadius, endBlurRadius, gradientBlurSpeed, blendMode, blendC, mixC, lightPositions, lightAlphas, darkPositions, darkAlphas);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof GradientBlurConfig)) {
                return false;
            }
            GradientBlurConfig gradientBlurConfig = (GradientBlurConfig) other;
            return Intrinsics.areEqual((Object) this.startBlurRadius, (Object) gradientBlurConfig.startBlurRadius) && Intrinsics.areEqual((Object) this.endBlurRadius, (Object) gradientBlurConfig.endBlurRadius) && Intrinsics.areEqual((Object) this.gradientBlurSpeed, (Object) gradientBlurConfig.gradientBlurSpeed) && this.blendMode == gradientBlurConfig.blendMode && this.blendC == gradientBlurConfig.blendC && this.mixC == gradientBlurConfig.mixC && Intrinsics.areEqual(this.lightPositions, gradientBlurConfig.lightPositions) && Intrinsics.areEqual(this.lightAlphas, gradientBlurConfig.lightAlphas) && Intrinsics.areEqual(this.darkPositions, gradientBlurConfig.darkPositions) && Intrinsics.areEqual(this.darkAlphas, gradientBlurConfig.darkAlphas);
        }

        public final int getBlendC() {
            return this.blendC;
        }

        public final int getBlendMode() {
            return this.blendMode;
        }

        public final float[] getDarkAlphas() {
            return this.darkAlphas;
        }

        public final float[] getDarkPositions() {
            return this.darkPositions;
        }

        public final Float getEndBlurRadius() {
            return this.endBlurRadius;
        }

        public final Float getGradientBlurSpeed() {
            return this.gradientBlurSpeed;
        }

        public final float[] getLightAlphas() {
            return this.lightAlphas;
        }

        public final float[] getLightPositions() {
            return this.lightPositions;
        }

        public final int getMixC() {
            return this.mixC;
        }

        public final Float getStartBlurRadius() {
            return this.startBlurRadius;
        }

        public int hashCode() {
            Float f3 = this.startBlurRadius;
            int iHashCode = (f3 == null ? 0 : f3.hashCode()) * 31;
            Float f4 = this.endBlurRadius;
            int iHashCode2 = (iHashCode + (f4 == null ? 0 : f4.hashCode())) * 31;
            Float f5 = this.gradientBlurSpeed;
            int iHashCode3 = (((((((iHashCode2 + (f5 == null ? 0 : f5.hashCode())) * 31) + Integer.hashCode(this.blendMode)) * 31) + Integer.hashCode(this.blendC)) * 31) + Integer.hashCode(this.mixC)) * 31;
            float[] fArr = this.lightPositions;
            int iHashCode4 = (iHashCode3 + (fArr == null ? 0 : Arrays.hashCode(fArr))) * 31;
            float[] fArr2 = this.lightAlphas;
            int iHashCode5 = (iHashCode4 + (fArr2 == null ? 0 : Arrays.hashCode(fArr2))) * 31;
            float[] fArr3 = this.darkPositions;
            int iHashCode6 = (iHashCode5 + (fArr3 == null ? 0 : Arrays.hashCode(fArr3))) * 31;
            float[] fArr4 = this.darkAlphas;
            return iHashCode6 + (fArr4 != null ? Arrays.hashCode(fArr4) : 0);
        }

        public String toString() {
            return "GradientBlurConfig(startBlurRadius=" + this.startBlurRadius + ", endBlurRadius=" + this.endBlurRadius + ", gradientBlurSpeed=" + this.gradientBlurSpeed + ", blendMode=" + this.blendMode + ", blendC=" + this.blendC + ", mixC=" + this.mixC + ", lightPositions=" + Arrays.toString(this.lightPositions) + ", lightAlphas=" + Arrays.toString(this.lightAlphas) + ", darkPositions=" + Arrays.toString(this.darkPositions) + ", darkAlphas=" + Arrays.toString(this.darkAlphas) + ')';
        }

        public GradientBlurConfig(Float f3, Float f4, Float f5, int i3, int i4, int i5, float[] fArr, float[] fArr2, float[] fArr3, float[] fArr4) {
            this.startBlurRadius = f3;
            this.endBlurRadius = f4;
            this.gradientBlurSpeed = f5;
            this.blendMode = i3;
            this.blendC = i4;
            this.mixC = i5;
            this.lightPositions = fArr;
            this.lightAlphas = fArr2;
            this.darkPositions = fArr3;
            this.darkAlphas = fArr4;
            if (!(fArr == null && fArr2 == null) && (fArr == null || fArr2 == null || fArr.length != fArr2.length)) {
                throw new IllegalArgumentException("lightPositions and lightAlphas must be both null or both non-null with the same length");
            }
            if (fArr3 == null && fArr4 == null) {
                return;
            }
            if (fArr3 == null || fArr4 == null || fArr3.length != fArr4.length) {
                throw new IllegalArgumentException("darkPositions and darkAlphas must be both null or both non-null with the same length");
            }
        }

        public /* synthetic */ GradientBlurConfig(Float f3, Float f4, Float f5, int i3, int i4, int i5, float[] fArr, float[] fArr2, float[] fArr3, float[] fArr4, int i6, DefaultConstructorMarker defaultConstructorMarker) {
            this((i6 & 1) != 0 ? null : f3, (i6 & 2) != 0 ? null : f4, (i6 & 4) != 0 ? null : f5, (i6 & 8) != 0 ? 0 : i3, (i6 & 16) != 0 ? 0 : i4, (i6 & 32) != 0 ? 0 : i5, (i6 & 64) != 0 ? null : fArr, (i6 & 128) != 0 ? null : fArr2, (i6 & 256) != 0 ? null : fArr3, (i6 & 512) != 0 ? null : fArr4);
        }
    }

    /* JADX INFO: loaded from: classes7.dex */
    @Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0002\u0010\u0007J\u0006\u0010\u0010\u001a\u00020\u0006J\u0006\u0010\u0011\u001a\u00020\u0006J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\u0018\u0010\u0014\u001a\u00020\u00062\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u000fH\u0016R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\n\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0018"}, d2 = {"Lcom/coui/appcompat/toolbar/AppBarBlurHelper$MaterialBlurWatcher;", "Lcom/coui/appcompat/OnMaterialStateChangeListener;", "anchor", "Landroid/view/View;", "onMaterialBlurChanged", "Lkotlin/Function1;", "", "(Landroid/view/View;Lkotlin/jvm/functions/Function1;)V", "anchorRef", "Ljava/lang/ref/WeakReference;", "attachListener", "Landroid/view/View$OnAttachStateChangeListener;", "getAttachListener", "()Landroid/view/View$OnAttachStateChangeListener;", "lastProcessedBlurState", "", "dispose", "install", "onDump", "", "onMaterialStateChange", "flag", "", "newState", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class MaterialBlurWatcher implements OnMaterialStateChangeListener {
        private final WeakReference<View> anchorRef;
        private final View.OnAttachStateChangeListener attachListener;
        private boolean lastProcessedBlurState;
        private final Function1<View, Unit> onMaterialBlurChanged;

        /* JADX WARN: Multi-variable type inference failed */
        public MaterialBlurWatcher(View anchor, Function1<? super View, Unit> onMaterialBlurChanged) {
            Intrinsics.checkNotNullParameter(anchor, "anchor");
            Intrinsics.checkNotNullParameter(onMaterialBlurChanged, "onMaterialBlurChanged");
            this.onMaterialBlurChanged = onMaterialBlurChanged;
            this.anchorRef = new WeakReference<>(anchor);
            this.lastProcessedBlurState = COUIMaterialStateManager.INSTANCE.isBlurEffectEnable();
            this.attachListener = new AppBarBlurHelper$MaterialBlurWatcher$attachListener$1(this);
        }

        public static final /* synthetic */ Function1 access$getOnMaterialBlurChanged$p(MaterialBlurWatcher materialBlurWatcher) {
            return materialBlurWatcher.onMaterialBlurChanged;
        }

        public static final /* synthetic */ void access$setLastProcessedBlurState$p(MaterialBlurWatcher materialBlurWatcher, boolean z2) {
            materialBlurWatcher.lastProcessedBlurState = z2;
        }

        public final void dispose() {
            View view = this.anchorRef.get();
            if (view != null) {
                view.removeOnAttachStateChangeListener(this.attachListener);
            }
            COUIMaterialStateManager.INSTANCE.removeOnMaterialStateChangedListener(this);
            if (view != null) {
                AppBarBlurHelper.access$removeBlurWatcherIfCurrent(AppBarBlurHelper.INSTANCE, view, this);
            }
        }

        public final View.OnAttachStateChangeListener getAttachListener() {
            return this.attachListener;
        }

        public final void install() {
            View view = this.anchorRef.get();
            if (view == null) {
                return;
            }
            COUIMaterialStateManager cOUIMaterialStateManager = COUIMaterialStateManager.INSTANCE;
            this.lastProcessedBlurState = cOUIMaterialStateManager.isBlurEffectEnable();
            cOUIMaterialStateManager.addOnMaterialStateChangedListener(this);
            view.addOnAttachStateChangeListener(this.attachListener);
        }

        @Override // com.coui.appcompat.OnMaterialStateChangeListener
        public CharSequence onDump() {
            return "AppBarBlurHelper.MaterialBlurWatcher(anchor=" + this.anchorRef.get() + ')';
        }

        @Override // com.coui.appcompat.OnMaterialStateChangeListener
        public void onMaterialStateChange(int flag, boolean newState) {
            if (flag == 2 && newState != this.lastProcessedBlurState) {
                this.lastProcessedBlurState = newState;
                View view = this.anchorRef.get();
                if (view != null && view.isAttachedToWindow()) {
                    this.onMaterialBlurChanged.invoke(view);
                }
            }
        }
    }

    /* JADX INFO: loaded from: classes7.dex */
    @Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B)\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006¢\u0006\u0002\u0010\u0007J\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\tJ\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\tJ\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0006HÆ\u0003¢\u0006\u0002\u0010\fJ2\u0010\u0012\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006HÆ\u0001¢\u0006\u0002\u0010\u0013J\u0013\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0017\u001a\u00020\u0006HÖ\u0001J\t\u0010\u0018\u001a\u00020\u0019HÖ\u0001R\u0015\u0010\u0004\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010\n\u001a\u0004\b\b\u0010\tR\u0015\u0010\u0005\u001a\u0004\u0018\u00010\u0006¢\u0006\n\n\u0002\u0010\r\u001a\u0004\b\u000b\u0010\fR\u0015\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010\n\u001a\u0004\b\u000e\u0010\t¨\u0006\u001a"}, d2 = {"Lcom/coui/appcompat/toolbar/AppBarBlurHelper$NormalBlurConfig;", "", "blurRadiusPx", "", "backgroundAlphaFraction", "bgColor", "", "(Ljava/lang/Float;Ljava/lang/Float;Ljava/lang/Integer;)V", "getBackgroundAlphaFraction", "()Ljava/lang/Float;", "Ljava/lang/Float;", "getBgColor", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getBlurRadiusPx", "component1", "component2", "component3", "copy", "(Ljava/lang/Float;Ljava/lang/Float;Ljava/lang/Integer;)Lcom/coui/appcompat/toolbar/AppBarBlurHelper$NormalBlurConfig;", "equals", "", RegionPickerUtils.OTHER_SYSTEM_REGION_KEY, "hashCode", "toString", "", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final /* data */ class NormalBlurConfig {
        private final Float backgroundAlphaFraction;
        private final Integer bgColor;
        private final Float blurRadiusPx;

        public NormalBlurConfig() {
            this(null, null, null, 7, null);
        }

        public static /* synthetic */ NormalBlurConfig copy$default(NormalBlurConfig normalBlurConfig, Float f3, Float f4, Integer num, int i3, Object obj) {
            if ((i3 & 1) != 0) {
                f3 = normalBlurConfig.blurRadiusPx;
            }
            if ((i3 & 2) != 0) {
                f4 = normalBlurConfig.backgroundAlphaFraction;
            }
            if ((i3 & 4) != 0) {
                num = normalBlurConfig.bgColor;
            }
            return normalBlurConfig.copy(f3, f4, num);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final Float getBlurRadiusPx() {
            return this.blurRadiusPx;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final Float getBackgroundAlphaFraction() {
            return this.backgroundAlphaFraction;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final Integer getBgColor() {
            return this.bgColor;
        }

        public final NormalBlurConfig copy(Float blurRadiusPx, Float backgroundAlphaFraction, Integer bgColor) {
            return new NormalBlurConfig(blurRadiusPx, backgroundAlphaFraction, bgColor);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof NormalBlurConfig)) {
                return false;
            }
            NormalBlurConfig normalBlurConfig = (NormalBlurConfig) other;
            return Intrinsics.areEqual((Object) this.blurRadiusPx, (Object) normalBlurConfig.blurRadiusPx) && Intrinsics.areEqual((Object) this.backgroundAlphaFraction, (Object) normalBlurConfig.backgroundAlphaFraction) && Intrinsics.areEqual(this.bgColor, normalBlurConfig.bgColor);
        }

        public final Float getBackgroundAlphaFraction() {
            return this.backgroundAlphaFraction;
        }

        public final Integer getBgColor() {
            return this.bgColor;
        }

        public final Float getBlurRadiusPx() {
            return this.blurRadiusPx;
        }

        public int hashCode() {
            Float f3 = this.blurRadiusPx;
            int iHashCode = (f3 == null ? 0 : f3.hashCode()) * 31;
            Float f4 = this.backgroundAlphaFraction;
            int iHashCode2 = (iHashCode + (f4 == null ? 0 : f4.hashCode())) * 31;
            Integer num = this.bgColor;
            return iHashCode2 + (num != null ? num.hashCode() : 0);
        }

        public String toString() {
            return "NormalBlurConfig(blurRadiusPx=" + this.blurRadiusPx + ", backgroundAlphaFraction=" + this.backgroundAlphaFraction + ", bgColor=" + this.bgColor + ')';
        }

        public NormalBlurConfig(Float f3, Float f4, Integer num) {
            this.blurRadiusPx = f3;
            this.backgroundAlphaFraction = f4;
            this.bgColor = num;
        }

        public /* synthetic */ NormalBlurConfig(Float f3, Float f4, Integer num, int i3, DefaultConstructorMarker defaultConstructorMarker) {
            this((i3 & 1) != 0 ? null : f3, (i3 & 2) != 0 ? null : f4, (i3 & 4) != 0 ? null : num);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.toolbar.AppBarBlurHelper$resolveBaseColor$1, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0004\b\u0002\u0010\u0003"}, d2 = {"<anonymous>", "", "invoke", "()Ljava/lang/Integer;"}, k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class AnonymousClass1 extends Lambda implements Function0<Integer> {
        final /* synthetic */ View $view;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass1(View view) {
            super(0);
            this.$view = view;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        public final Integer invoke() {
            return Integer.valueOf(AppBarBlurHelper.access$resolveSolidBackgroundColorOrPageFallback(AppBarBlurHelper.INSTANCE, this.$view));
        }

        @Override // kotlin.jvm.functions.Function0
        public /* bridge */ /* synthetic */ Integer invoke() {
            return invoke();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.toolbar.AppBarBlurHelper$setUpGradientBlurBackground$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: loaded from: classes7.dex */
    @Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "anchor", "Landroid/view/View;", "invoke"}, k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class C13531 extends Lambda implements Function1<View, Unit> {
        public static final C13531 INSTANCE = new C13531();

        public C13531() {
            super(1);
        }

        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ Unit invoke(View view) {
            invoke2(view);
            return Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final void invoke2(View anchor) {
            Intrinsics.checkNotNullParameter(anchor, "anchor");
            COUIAppBarLayout cOUIAppBarLayout = anchor instanceof COUIAppBarLayout ? (COUIAppBarLayout) anchor : null;
            if (cOUIAppBarLayout != null) {
                AppBarBlurHelper.access$applyGradientBlurBackgroundAppBar(AppBarBlurHelper.INSTANCE, cOUIAppBarLayout);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.toolbar.AppBarBlurHelper$setUpGradientBlurBackground$2, reason: invalid class name */
    @Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "anchor", "Landroid/view/View;", "invoke"}, k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class AnonymousClass2 extends Lambda implements Function1<View, Unit> {
        public static final AnonymousClass2 INSTANCE = new AnonymousClass2();

        public AnonymousClass2() {
            super(1);
        }

        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ Unit invoke(View view) {
            invoke2(view);
            return Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final void invoke2(View anchor) {
            Intrinsics.checkNotNullParameter(anchor, "anchor");
            AppBarBlurHelper.access$applyGradientBlurBackgroundPlain(AppBarBlurHelper.INSTANCE, anchor);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.toolbar.AppBarBlurHelper$setUpNormalBlurBackground$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: loaded from: classes7.dex */
    @Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "anchor", "Landroid/view/View;", "invoke"}, k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class C13541 extends Lambda implements Function1<View, Unit> {
        public static final C13541 INSTANCE = new C13541();

        public C13541() {
            super(1);
        }

        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ Unit invoke(View view) {
            invoke2(view);
            return Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final void invoke2(View anchor) {
            Intrinsics.checkNotNullParameter(anchor, "anchor");
            COUIAppBarLayout cOUIAppBarLayout = anchor instanceof COUIAppBarLayout ? (COUIAppBarLayout) anchor : null;
            if (cOUIAppBarLayout != null) {
                AppBarBlurHelper.access$applyNormalBlurBackground(AppBarBlurHelper.INSTANCE, cOUIAppBarLayout);
            }
        }
    }

    private AppBarBlurHelper() {
    }

    public static final /* synthetic */ void access$applyGradientBlurBackgroundAppBar(AppBarBlurHelper appBarBlurHelper, COUIAppBarLayout cOUIAppBarLayout) {
        appBarBlurHelper.applyGradientBlurBackgroundAppBar(cOUIAppBarLayout);
    }

    public static final /* synthetic */ void access$applyGradientBlurBackgroundPlain(AppBarBlurHelper appBarBlurHelper, View view) {
        appBarBlurHelper.applyGradientBlurBackgroundPlain(view);
    }

    public static final /* synthetic */ void access$applyNormalBlurBackground(AppBarBlurHelper appBarBlurHelper, COUIAppBarLayout cOUIAppBarLayout) {
        appBarBlurHelper.applyNormalBlurBackground(cOUIAppBarLayout);
    }

    public static final /* synthetic */ void access$removeBlurWatcherIfCurrent(AppBarBlurHelper appBarBlurHelper, View view, MaterialBlurWatcher materialBlurWatcher) {
        appBarBlurHelper.removeBlurWatcherIfCurrent(view, materialBlurWatcher);
    }

    public static final /* synthetic */ int access$resolveSolidBackgroundColorOrPageFallback(AppBarBlurHelper appBarBlurHelper, View view) {
        return appBarBlurHelper.resolveSolidBackgroundColorOrPageFallback(view);
    }

    private final void applyBlurAndGradientBackground(View colorSourceView, View blurTargetView, Integer resolvedBaseColor) {
        captureBackgroundBeforeApplyingBlurLayer(blurTargetView);
        blurTargetView.setBackground(createGradientBackground(colorSourceView, resolvedBaseColor));
        applyGradientBlurRenderEffect(blurTargetView, colorSourceView);
    }

    public static /* synthetic */ void applyBlurAndGradientBackground$default(AppBarBlurHelper appBarBlurHelper, View view, View view2, Integer num, int i3, Object obj) {
        if ((i3 & 4) != 0) {
            num = null;
        }
        appBarBlurHelper.applyBlurAndGradientBackground(view, view2, num);
    }

    private final void applyGradientBackgroundFade(View view, float blurFactor) {
        Drawable background = view.getBackground();
        if (background == null) {
            return;
        }
        int iCoerceIn = RangesKt.coerceIn((int) (blurFactor * 255.0f), 0, 255);
        Drawable drawableMutate = background.mutate();
        Intrinsics.checkNotNullExpressionValue(drawableMutate, "mutate(...)");
        drawableMutate.setAlpha(iCoerceIn);
        if (drawableMutate != background) {
            view.setBackground(drawableMutate);
        }
    }

    private final void applyGradientBlurBackgroundAppBar(COUIAppBarLayout appBarLayout) {
        View viewBlurViewForAppBar = blurViewForAppBar(appBarLayout);
        if (!shouldEnableBlur()) {
            COUIFloatingScrollingViewBehavior cOUIFloatingScrollingViewBehaviorFindFloatingBehavior = findFloatingBehavior(appBarLayout);
            if (cOUIFloatingScrollingViewBehaviorFindFloatingBehavior != null) {
                cOUIFloatingScrollingViewBehaviorFindFloatingBehavior.setTransparentBg(false);
            }
            clearGradientBlurEffectAndRestoreBackground(viewBlurViewForAppBar);
            restoreAppBarAppearance(appBarLayout);
            syncAppBarDividerWithMaterialBlur(appBarLayout);
            return;
        }
        captureAppBarAppearanceBeforeTransparent(appBarLayout);
        int iResolveGradientBlurBaseColorForAppBar = resolveGradientBlurBaseColorForAppBar(appBarLayout);
        COUIFloatingScrollingViewBehavior cOUIFloatingScrollingViewBehaviorFindFloatingBehavior2 = findFloatingBehavior(appBarLayout);
        if (cOUIFloatingScrollingViewBehaviorFindFloatingBehavior2 != null) {
            cOUIFloatingScrollingViewBehaviorFindFloatingBehavior2.setTransparentBg(true, appBarLayout);
        }
        applyBlurAndGradientBackground(appBarLayout, viewBlurViewForAppBar, Integer.valueOf(iResolveGradientBlurBaseColorForAppBar));
        syncAppBarDividerWithMaterialBlur(appBarLayout);
    }

    private final void applyGradientBlurBackgroundPlain(View view) {
        if (shouldEnableBlur()) {
            applyBlurAndGradientBackground$default(this, view, view, null, 4, null);
            return;
        }
        if (COUILog.LOG_DEBUG || Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "Blur effect skipped: lowAnim or blur settings off");
        }
        clearGradientBlurEffectAndRestoreBackground(view);
    }

    private final void applyGradientBlurRenderEffect(View view, View anchor) {
        if (shouldEnableBlur() && COUIMaterialEffect.canApplyBackgroundRenderEffect()) {
            AppBarBlurParams appBarBlurParamsMergedParams = mergedParams(anchor);
            GradientBlurConfig gradientBlurConfig = gradientBlurConfigs.get(anchor);
            COUIMaterialEffect.setBackgroundRenderEffect(OplusRenderEffect.createGradientBlurEffect(appBarBlurParamsMergedParams.getStartBlurRadius(), appBarBlurParamsMergedParams.getEndBlurRadius(), true, appBarBlurParamsMergedParams.getGradientBlurSpeed(), gradientBlurConfig != null ? gradientBlurConfig.getBlendMode() : 0, gradientBlurConfig != null ? gradientBlurConfig.getBlendC() : 0, gradientBlurConfig != null ? gradientBlurConfig.getMixC() : 0), view);
            if (COUILog.LOG_DEBUG || Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "Gradient blur applied to " + view.getClass().getSimpleName());
            }
        }
    }

    public static /* synthetic */ void applyGradientBlurRenderEffect$default(AppBarBlurHelper appBarBlurHelper, View view, View view2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            view2 = view;
        }
        appBarBlurHelper.applyGradientBlurRenderEffect(view, view2);
    }

    /* JADX WARN: Code duplicated, block: B:18:0x0050  */
    /* JADX WARN: Code duplicated, block: B:20:0x0058  */
    private final void applyNormalBlurBackground(COUIAppBarLayout appBarLayout) {
        WeakHashMap<View, Integer> weakHashMap;
        Integer numValueOf;
        int iIntValue;
        Float blurRadiusPx;
        Float backgroundAlphaFraction;
        AppBarBlurParams appBarBlurParamsParams = params();
        NormalBlurConfig normalBlurConfig = normalBlurConfigs.get(appBarLayout);
        float normalBackgroundAlphaFraction = COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(appBarLayout.getContext()) ? 0.6f : appBarBlurParamsParams.getNormalBackgroundAlphaFraction();
        if (normalBlurConfig != null && (backgroundAlphaFraction = normalBlurConfig.getBackgroundAlphaFraction()) != null) {
            normalBackgroundAlphaFraction = backgroundAlphaFraction.floatValue();
        }
        int iCoerceIn = RangesKt.coerceIn((int) (255 * RangesKt.coerceIn(normalBackgroundAlphaFraction, 0.0f, 1.0f)), 0, 255);
        Integer bgColor = normalBlurConfig != null ? normalBlurConfig.getBgColor() : null;
        if (bgColor != null) {
            iIntValue = bgColor.intValue();
            if (Color.alpha(iIntValue) == 0) {
                weakHashMap = normalBlurOriginalColors;
                numValueOf = weakHashMap.get(appBarLayout);
                if (numValueOf == null) {
                    numValueOf = Integer.valueOf(INSTANCE.resolveSolidBackgroundColorOrPageFallback(appBarLayout));
                    weakHashMap.put(appBarLayout, numValueOf);
                }
                iIntValue = numValueOf.intValue();
            }
        } else {
            weakHashMap = normalBlurOriginalColors;
            numValueOf = weakHashMap.get(appBarLayout);
            if (numValueOf == null) {
                numValueOf = Integer.valueOf(INSTANCE.resolveSolidBackgroundColorOrPageFallback(appBarLayout));
                weakHashMap.put(appBarLayout, numValueOf);
            }
            iIntValue = numValueOf.intValue();
        }
        boolean z2 = COUILog.LOG_DEBUG;
        if (z2 || Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "applyNormalBlurBackground: baseColor=#" + Integer.toHexString(iIntValue) + " alpha=" + iCoerceIn);
        }
        if (shouldEnableBlur()) {
            appBarLayout.setBackgroundColor(ColorUtils.setAlphaComponent(iIntValue, iCoerceIn));
            float fCoerceAtLeast = RangesKt.coerceAtLeast((normalBlurConfig == null || (blurRadiusPx = normalBlurConfig.getBlurRadiusPx()) == null) ? appBarBlurParamsParams.getNormalBlurRadiusPx() : blurRadiusPx.floatValue(), 0.0f);
            RenderEffect renderEffectCreateBlurEffect = RenderEffect.createBlurEffect(fCoerceAtLeast, fCoerceAtLeast, Shader.TileMode.CLAMP);
            Intrinsics.checkNotNullExpressionValue(renderEffectCreateBlurEffect, "createBlurEffect(...)");
            COUIMaterialEffect.setBackgroundRenderEffect(renderEffectCreateBlurEffect, appBarLayout);
            if (z2 || Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "Normal background blur RenderEffect applied (radius=" + fCoerceAtLeast + ')');
            }
        } else {
            appBarLayout.setBackgroundColor(iIntValue);
            clearNormalRenderEffect(appBarLayout);
            if (z2 || Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "Normal blur: material blur disabled; solid color on AppBar only");
            }
        }
        syncAppBarDividerWithMaterialBlur(appBarLayout);
    }

    private final void assertMainThread() {
        if (COUILog.LOG_DEBUG && !Intrinsics.areEqual(Looper.myLooper(), Looper.getMainLooper())) {
            throw new IllegalStateException("AppBarBlurHelper must be called on the main thread");
        }
    }

    private final View blurViewForAppBar(COUIAppBarLayout appBarLayout) {
        AppBarBlurOverlayView appBarBlurOverlayViewFindOverlayInParent = findOverlayInParent(appBarLayout);
        return appBarBlurOverlayViewFindOverlayInParent != null ? appBarBlurOverlayViewFindOverlayInParent : appBarLayout;
    }

    private final void captureAppBarAppearanceBeforeTransparent(COUIAppBarLayout appBarLayout) {
        assertMainThread();
        WeakHashMap<COUIAppBarLayout, AppBarAppearanceRestoreState> weakHashMap = appBarAppearanceRestoreStates;
        if (weakHashMap.containsKey(appBarLayout)) {
            return;
        }
        weakHashMap.put(appBarLayout, new AppBarAppearanceRestoreState(cloneDrawableForRestore(appBarLayout, appBarLayout.getBackground()), appBarLayout.getTargetElevation()));
    }

    private final void captureBackgroundBeforeApplyingBlurLayer(View blurTargetView) {
        assertMainThread();
        Drawable background = blurTargetView.getBackground();
        if (background instanceof GradientBackgroundDrawable) {
            return;
        }
        WeakHashMap<View, Drawable> weakHashMap = backgroundRestoreSnapshots;
        if (weakHashMap.containsKey(blurTargetView)) {
            return;
        }
        weakHashMap.put(blurTargetView, cloneDrawableForRestore(blurTargetView, background));
    }

    private final void clearBlurDividerSuppression(COUIAppBarLayout appBarLayout) {
        assertMainThread();
        COUIDividerAppBarLayout cOUIDividerAppBarLayout = appBarLayout instanceof COUIDividerAppBarLayout ? (COUIDividerAppBarLayout) appBarLayout : null;
        if (cOUIDividerAppBarLayout != null) {
            cOUIDividerAppBarLayout.setBlurDividerSuppressed(false);
        }
    }

    @JvmStatic
    public static final void clearGradientBlur(View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        AppBarBlurHelper appBarBlurHelper = INSTANCE;
        appBarBlurHelper.removeMaterialBlurWatcherIfAny(view);
        appBarBlurHelper.clearGradientBlurEffectAndRestoreBackground(view);
    }

    private final void clearGradientBlurEffectAndRestoreBackground(View blurTargetView) {
        assertMainThread();
        clearGradientBlurEffectOnly(blurTargetView);
        Drawable drawableRemove = backgroundRestoreSnapshots.remove(blurTargetView);
        if (drawableRemove == null) {
            return;
        }
        blurTargetView.setBackground(drawableRemove);
    }

    private final void clearGradientBlurEffectOnly(View view) {
        COUIMaterialEffect.setBackgroundRenderEffect(null, view);
        if (COUILog.LOG_DEBUG || Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "Gradient blur cleared on " + view.getClass().getSimpleName());
        }
    }

    @JvmStatic
    public static final void clearGradientBlurForAppBar(COUIAppBarLayout appBarLayout) {
        Intrinsics.checkNotNullParameter(appBarLayout, "appBarLayout");
        AppBarBlurHelper appBarBlurHelper = INSTANCE;
        appBarBlurHelper.removeMaterialBlurWatcherIfAny(appBarLayout);
        COUIFloatingScrollingViewBehavior cOUIFloatingScrollingViewBehaviorFindFloatingBehavior = appBarBlurHelper.findFloatingBehavior(appBarLayout);
        if (cOUIFloatingScrollingViewBehaviorFindFloatingBehavior != null) {
            cOUIFloatingScrollingViewBehaviorFindFloatingBehavior.setTransparentBg(false);
        }
        appBarBlurHelper.clearGradientBlurEffectAndRestoreBackground(appBarBlurHelper.blurViewForAppBar(appBarLayout));
        appBarBlurHelper.restoreAppBarAppearance(appBarLayout);
        appBarBlurHelper.clearBlurDividerSuppression(appBarLayout);
    }

    @JvmStatic
    public static final void clearNormalBlurForAppBar(COUIAppBarLayout appBarLayout) {
        Intrinsics.checkNotNullParameter(appBarLayout, "appBarLayout");
        AppBarBlurHelper appBarBlurHelper = INSTANCE;
        appBarBlurHelper.removeMaterialBlurWatcherIfAny(appBarLayout);
        normalBlurOriginalColors.remove(appBarLayout);
        COUIFloatingScrollingViewBehavior cOUIFloatingScrollingViewBehaviorFindFloatingBehavior = appBarBlurHelper.findFloatingBehavior(appBarLayout);
        if (cOUIFloatingScrollingViewBehaviorFindFloatingBehavior != null) {
            cOUIFloatingScrollingViewBehaviorFindFloatingBehavior.setTransparentBg(false);
        }
        appBarBlurHelper.clearNormalRenderEffect(appBarLayout);
        appBarBlurHelper.clearBlurDividerSuppression(appBarLayout);
        if (COUILog.LOG_DEBUG || Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "Normal background blur cleared on AppBarLayout");
        }
    }

    private final void clearNormalRenderEffect(COUIAppBarLayout appBarLayout) {
        COUIMaterialEffect.setBackgroundRenderEffect(null, appBarLayout);
    }

    private final Drawable cloneDrawableForRestore(View host, Drawable d3) {
        if (d3 == null) {
            return null;
        }
        Drawable.ConstantState constantState = d3.getConstantState();
        return constantState == null ? d3.mutate() : constantState.newDrawable(host.getContext().getResources()).mutate();
    }

    /* JADX WARN: Code duplicated, block: B:9:0x0016  */
    private final Drawable createGradientBackground(View colorSourceView, Integer resolvedBaseColor) {
        int iAccess$resolveSolidBackgroundColorOrPageFallback;
        float[] lightPositions;
        float[] lightAlphas;
        float[] darkPositions;
        if (resolvedBaseColor != null) {
            iAccess$resolveSolidBackgroundColorOrPageFallback = resolvedBaseColor.intValue();
        } else {
            Integer num = gradientBlurBgColor;
            if (num != null) {
                iAccess$resolveSolidBackgroundColorOrPageFallback = num.intValue();
                if (Color.alpha(iAccess$resolveSolidBackgroundColorOrPageFallback) == 0) {
                    iAccess$resolveSolidBackgroundColorOrPageFallback = access$resolveSolidBackgroundColorOrPageFallback(INSTANCE, colorSourceView);
                }
            } else {
                iAccess$resolveSolidBackgroundColorOrPageFallback = access$resolveSolidBackgroundColorOrPageFallback(INSTANCE, colorSourceView);
            }
        }
        Context context = colorSourceView.getContext();
        AppBarBlurParams appBarBlurParamsParams = params();
        GradientBlurConfig gradientBlurConfig = gradientBlurConfigs.get(colorSourceView);
        if (COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(context)) {
            if (gradientBlurConfig == null || (darkPositions = gradientBlurConfig.getDarkPositions()) == null) {
                darkPositions = new float[]{appBarBlurParamsParams.getGradientPos0(), appBarBlurParamsParams.getGradientPos2()};
            }
            if (gradientBlurConfig == null || (lightAlphas = gradientBlurConfig.getDarkAlphas()) == null) {
                lightAlphas = new float[]{appBarBlurParamsParams.getGradientAlpha0(), 0.4f};
            }
        } else {
            if (gradientBlurConfig == null || (lightPositions = gradientBlurConfig.getLightPositions()) == null) {
                lightPositions = new float[]{appBarBlurParamsParams.getGradientPos0(), appBarBlurParamsParams.getGradientPos1(), appBarBlurParamsParams.getGradientPos2()};
            }
            if (gradientBlurConfig == null || (lightAlphas = gradientBlurConfig.getLightAlphas()) == null) {
                lightAlphas = new float[]{appBarBlurParamsParams.getGradientAlpha0(), appBarBlurParamsParams.getGradientAlpha1(), appBarBlurParamsParams.getGradientAlpha2()};
            }
            darkPositions = lightPositions;
        }
        return new GradientBackgroundDrawable(iAccess$resolveSolidBackgroundColorOrPageFallback, darkPositions, lightAlphas);
    }

    public static /* synthetic */ Drawable createGradientBackground$default(AppBarBlurHelper appBarBlurHelper, View view, Integer num, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            num = null;
        }
        return appBarBlurHelper.createGradientBackground(view, num);
    }

    private final void ensureMaterialBlurWatcher(View anchor, Function1<? super View, Unit> onMaterialBlurChanged) {
        assertMainThread();
        WeakHashMap<View, MaterialBlurWatcher> weakHashMap = blurWatchers;
        if (weakHashMap.containsKey(anchor)) {
            return;
        }
        MaterialBlurWatcher materialBlurWatcher = new MaterialBlurWatcher(anchor, onMaterialBlurChanged);
        weakHashMap.put(anchor, materialBlurWatcher);
        materialBlurWatcher.install();
    }

    private final COUIFloatingScrollingViewBehavior findFloatingBehavior(COUIAppBarLayout appBarLayout) {
        ViewParent parent = appBarLayout.getParent();
        CoordinatorLayout coordinatorLayout = parent instanceof CoordinatorLayout ? (CoordinatorLayout) parent : null;
        if (coordinatorLayout == null) {
            return null;
        }
        int childCount = coordinatorLayout.getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = coordinatorLayout.getChildAt(i3);
            if (childAt != appBarLayout) {
                ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                CoordinatorLayout.LayoutParams layoutParams2 = layoutParams instanceof CoordinatorLayout.LayoutParams ? (CoordinatorLayout.LayoutParams) layoutParams : null;
                if (layoutParams2 == null) {
                    continue;
                } else {
                    CoordinatorLayout.Behavior behavior = layoutParams2.getBehavior();
                    if (behavior instanceof COUIFloatingScrollingViewBehavior) {
                        return (COUIFloatingScrollingViewBehavior) behavior;
                    }
                }
            }
        }
        return null;
    }

    private final AppBarBlurOverlayView findOverlayInParent(COUIAppBarLayout appBarLayout) {
        ViewParent parent = appBarLayout.getParent();
        ViewGroup viewGroup = parent instanceof ViewGroup ? (ViewGroup) parent : null;
        if (viewGroup == null) {
            return null;
        }
        int childCount = viewGroup.getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = viewGroup.getChildAt(i3);
            if (childAt instanceof AppBarBlurOverlayView) {
                return (AppBarBlurOverlayView) childAt;
            }
        }
        return null;
    }

    @JvmStatic
    public static final AppBarBlurOverlayView getOverlayView(COUIAppBarLayout appBarLayout) {
        Intrinsics.checkNotNullParameter(appBarLayout, "appBarLayout");
        return INSTANCE.findOverlayInParent(appBarLayout);
    }

    private final AppBarBlurParams mergedParams(View anchor) {
        AppBarBlurParams appBarBlurParamsParams = params();
        GradientBlurConfig gradientBlurConfig = gradientBlurConfigs.get(anchor);
        if (gradientBlurConfig == null) {
            return appBarBlurParamsParams;
        }
        Float startBlurRadius = gradientBlurConfig.getStartBlurRadius();
        float fFloatValue = startBlurRadius != null ? startBlurRadius.floatValue() : appBarBlurParamsParams.getStartBlurRadius();
        Float endBlurRadius = gradientBlurConfig.getEndBlurRadius();
        float fFloatValue2 = endBlurRadius != null ? endBlurRadius.floatValue() : appBarBlurParamsParams.getEndBlurRadius();
        Float gradientBlurSpeed = gradientBlurConfig.getGradientBlurSpeed();
        return AppBarBlurParams.copy$default(appBarBlurParamsParams, fFloatValue, fFloatValue2, gradientBlurSpeed != null ? gradientBlurSpeed.floatValue() : appBarBlurParamsParams.getGradientBlurSpeed(), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2040, null);
    }

    private final AppBarBlurParams params() {
        return AppBarBlurParamsExtKt.get(AppBarBlurParams.INSTANCE);
    }

    @JvmStatic
    public static final void refreshGradientBlurConfig(COUIAppBarLayout appBarLayout) {
        Intrinsics.checkNotNullParameter(appBarLayout, "appBarLayout");
        INSTANCE.applyGradientBlurBackgroundAppBar(appBarLayout);
    }

    @JvmStatic
    public static final void refreshNormalBlurConfig(COUIAppBarLayout appBarLayout) {
        Intrinsics.checkNotNullParameter(appBarLayout, "appBarLayout");
        INSTANCE.applyNormalBlurBackground(appBarLayout);
    }

    private final void removeBlurWatcherIfCurrent(View anchor, MaterialBlurWatcher watcher) {
        assertMainThread();
        WeakHashMap<View, MaterialBlurWatcher> weakHashMap = blurWatchers;
        if (weakHashMap.get(anchor) == watcher) {
            weakHashMap.remove(anchor);
        }
    }

    private final void removeMaterialBlurWatcherIfAny(View anchor) {
        assertMainThread();
        MaterialBlurWatcher materialBlurWatcherRemove = blurWatchers.remove(anchor);
        if (materialBlurWatcherRemove == null) {
            return;
        }
        materialBlurWatcherRemove.dispose();
    }

    private final int resolveBaseColor(View view, Integer explicitColor, Function0<Integer> fallback) {
        if (explicitColor != null) {
            int iIntValue = explicitColor.intValue();
            if (Color.alpha(iIntValue) != 0) {
                return iIntValue;
            }
        }
        return fallback.invoke().intValue();
    }

    public static /* synthetic */ int resolveBaseColor$default(AppBarBlurHelper appBarBlurHelper, View view, Integer num, Function0 function0, int i3, Object obj) {
        if ((i3 & 4) != 0) {
            function0 = new AnonymousClass1(view);
        }
        if (num != null) {
            int iIntValue = num.intValue();
            if (Color.alpha(iIntValue) != 0) {
                return iIntValue;
            }
        }
        return ((Number) function0.invoke()).intValue();
    }

    private final int resolveGradientBlurBaseColorForAppBar(COUIAppBarLayout appBarLayout) {
        Integer num = gradientBlurBgColor;
        if (num != null) {
            int iIntValue = num.intValue();
            if (Color.alpha(iIntValue) != 0) {
                return iIntValue;
            }
        }
        AppBarBlurHelper appBarBlurHelper = INSTANCE;
        Integer numResolveSolidBackgroundColorFromDrawable = appBarBlurHelper.resolveSolidBackgroundColorFromDrawable(appBarLayout.getBackground());
        if (numResolveSolidBackgroundColorFromDrawable != null) {
            return numResolveSolidBackgroundColorFromDrawable.intValue();
        }
        AppBarAppearanceRestoreState appBarAppearanceRestoreState = appBarAppearanceRestoreStates.get(appBarLayout);
        Integer numResolveSolidBackgroundColorFromDrawable2 = appBarBlurHelper.resolveSolidBackgroundColorFromDrawable(appBarAppearanceRestoreState != null ? appBarAppearanceRestoreState.getBackground() : null);
        if (numResolveSolidBackgroundColorFromDrawable2 != null) {
            return numResolveSolidBackgroundColorFromDrawable2.intValue();
        }
        Context context = appBarLayout.getContext();
        Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
        return appBarBlurHelper.resolvePageFallbackBackgroundColor(context);
    }

    private final int resolvePageFallbackBackgroundColor(Context context) {
        Integer numWindowBackgroundOpaqueColor = windowBackgroundOpaqueColor(context);
        return numWindowBackgroundOpaqueColor != null ? numWindowBackgroundOpaqueColor.intValue() : COUIContextUtil.getAttrColor(context, R.attr.couiColorBackgroundWithCard);
    }

    private final Integer resolveSolidBackgroundColorFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        int i3 = 0;
        while (i3 < 8) {
            i3++;
            if (drawable instanceof ColorDrawable) {
                return takeIfOpaque(Integer.valueOf(((ColorDrawable) drawable).getColor()));
            }
            if (drawable instanceof MaterialShapeDrawable) {
                ColorStateList fillColor = ((MaterialShapeDrawable) drawable).getFillColor();
                return takeIfOpaque(fillColor != null ? Integer.valueOf(fillColor.getDefaultColor()) : null);
            }
            if (drawable instanceof StateListDrawable) {
                drawable = ((StateListDrawable) drawable).getCurrent();
                if (drawable == null) {
                    return null;
                }
            } else if (!(drawable instanceof DrawableWrapper) || (drawable = ((DrawableWrapper) drawable).getDrawable()) == null) {
                break;
            }
        }
        return null;
    }

    private final int resolveSolidBackgroundColorOrPageFallback(View view) {
        Integer numResolveSolidBackgroundColorFromDrawable = resolveSolidBackgroundColorFromDrawable(view.getBackground());
        if (numResolveSolidBackgroundColorFromDrawable != null) {
            return numResolveSolidBackgroundColorFromDrawable.intValue();
        }
        Context context = view.getContext();
        Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
        return resolvePageFallbackBackgroundColor(context);
    }

    private final void restoreAppBarAppearance(COUIAppBarLayout appBarLayout) {
        assertMainThread();
        AppBarAppearanceRestoreState appBarAppearanceRestoreStateRemove = appBarAppearanceRestoreStates.remove(appBarLayout);
        if (appBarAppearanceRestoreStateRemove == null) {
            return;
        }
        appBarLayout.setBackground(appBarAppearanceRestoreStateRemove.getBackground());
        appBarLayout.setTargetElevation(appBarAppearanceRestoreStateRemove.getTargetElevation());
    }

    @JvmStatic
    public static final void setGradientBlurBgColor(int color) {
        gradientBlurBgColor = Integer.valueOf(color);
    }

    @JvmStatic
    public static final void setGradientBlurConfig(View view, GradientBlurConfig config) {
        Intrinsics.checkNotNullParameter(view, "view");
        if (config != null) {
            gradientBlurConfigs.put(view, config);
        } else {
            gradientBlurConfigs.remove(view);
        }
    }

    @JvmStatic
    public static final void setNormalBlurConfig(View view, NormalBlurConfig config) {
        Intrinsics.checkNotNullParameter(view, "view");
        if (config != null) {
            normalBlurConfigs.put(view, config);
        } else {
            normalBlurConfigs.remove(view);
        }
    }

    @JvmStatic
    public static final void setUpGradientBlurBackground(COUIAppBarLayout appBarLayout) {
        Intrinsics.checkNotNullParameter(appBarLayout, "appBarLayout");
        AppBarBlurHelper appBarBlurHelper = INSTANCE;
        appBarBlurHelper.applyGradientBlurBackgroundAppBar(appBarLayout);
        appBarBlurHelper.ensureMaterialBlurWatcher(appBarLayout, C13531.INSTANCE);
    }

    @JvmStatic
    public static final void setUpNormalBlurBackground(COUIAppBarLayout appBarLayout) {
        Intrinsics.checkNotNullParameter(appBarLayout, "appBarLayout");
        AppBarBlurHelper appBarBlurHelper = INSTANCE;
        appBarBlurHelper.applyNormalBlurBackground(appBarLayout);
        appBarBlurHelper.ensureMaterialBlurWatcher(appBarLayout, C13541.INSTANCE);
    }

    private final boolean shouldEnableBlur() {
        COUIMaterialStateManager cOUIMaterialStateManager = COUIMaterialStateManager.INSTANCE;
        boolean z2 = cOUIMaterialStateManager.isBlurEffectEnable() && !cOUIMaterialStateManager.isLowAnimationLevel();
        if (!z2 && (COUILog.LOG_DEBUG || Log.isLoggable(TAG, 3))) {
            Log.d(TAG, "Material blur disabled for lowAnim or blur settings off");
        }
        return z2;
    }

    private final void syncAppBarDividerWithMaterialBlur(COUIAppBarLayout appBarLayout) {
        assertMainThread();
        COUIDividerAppBarLayout cOUIDividerAppBarLayout = appBarLayout instanceof COUIDividerAppBarLayout ? (COUIDividerAppBarLayout) appBarLayout : null;
        if (cOUIDividerAppBarLayout == null) {
            return;
        }
        boolean zShouldEnableBlur = shouldEnableBlur();
        cOUIDividerAppBarLayout.setBlurDividerSuppressed(zShouldEnableBlur);
        if (COUILog.LOG_DEBUG || Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "Divider blur suppression=" + zShouldEnableBlur + ", userHasDivider=" + cOUIDividerAppBarLayout.hasDivider() + " on " + appBarLayout.getClass().getSimpleName());
        }
    }

    private final Integer takeIfOpaque(Integer num) {
        if (num == null || Color.alpha(num.intValue()) == 0) {
            return null;
        }
        return num;
    }

    @JvmStatic
    public static final void updateGradientBlurFraction(View view, float fraction) {
        Intrinsics.checkNotNullParameter(view, "view");
        float fCoerceIn = RangesKt.coerceIn(fraction, 0.0f, 1.0f);
        float f3 = 1.0f - fCoerceIn;
        AppBarBlurHelper appBarBlurHelper = INSTANCE;
        appBarBlurHelper.applyGradientBackgroundFade(view, f3);
        if (appBarBlurHelper.shouldEnableBlur() && COUIMaterialEffect.canApplyBackgroundRenderEffect()) {
            AppBarBlurParams appBarBlurParamsMergedParams = appBarBlurHelper.mergedParams(view);
            GradientBlurConfig gradientBlurConfig = gradientBlurConfigs.get(view);
            COUIMaterialEffect.setBackgroundRenderEffect(OplusRenderEffect.createGradientBlurEffect(appBarBlurParamsMergedParams.getStartBlurRadius() * f3, appBarBlurParamsMergedParams.getEndBlurRadius() * f3, true, appBarBlurParamsMergedParams.getGradientBlurSpeed(), gradientBlurConfig != null ? gradientBlurConfig.getBlendMode() : 0, gradientBlurConfig != null ? gradientBlurConfig.getBlendC() : 0, gradientBlurConfig != null ? gradientBlurConfig.getMixC() : 0), view);
            if (COUILog.LOG_DEBUG || Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "Gradient blur updated: fraction=" + fCoerceIn + ", blurFactor=" + f3);
            }
        }
    }

    private final Integer windowBackgroundOpaqueColor(Context context) {
        TypedValue typedValue = new TypedValue();
        if (!context.getTheme().resolveAttribute(android.R.attr.windowBackground, typedValue, true)) {
            return null;
        }
        int i3 = typedValue.type;
        if (28 <= i3 && i3 < 32) {
            return takeIfOpaque(Integer.valueOf(typedValue.data));
        }
        int i4 = typedValue.resourceId;
        if (i4 != 0) {
            try {
                Integer numResolveSolidBackgroundColorFromDrawable = resolveSolidBackgroundColorFromDrawable(AppCompatResources.getDrawable(context, i4));
                if (numResolveSolidBackgroundColorFromDrawable != null) {
                    return Integer.valueOf(numResolveSolidBackgroundColorFromDrawable.intValue());
                }
            } catch (Resources.NotFoundException e3) {
                Log.w(TAG, "windowBackgroundOpaqueColor: resource not found, resId=" + typedValue.resourceId, e3);
            } catch (IllegalArgumentException e4) {
                Log.w(TAG, "windowBackgroundOpaqueColor: illegal argument, resId=" + typedValue.resourceId, e4);
            }
        }
        return null;
    }

    @JvmStatic
    public static final void setUpGradientBlurBackground(View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        AppBarBlurHelper appBarBlurHelper = INSTANCE;
        appBarBlurHelper.applyGradientBlurBackgroundPlain(view);
        appBarBlurHelper.ensureMaterialBlurWatcher(view, AnonymousClass2.INSTANCE);
    }
}