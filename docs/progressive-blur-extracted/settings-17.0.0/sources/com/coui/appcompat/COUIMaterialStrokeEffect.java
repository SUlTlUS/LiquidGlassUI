package com.coui.appcompat;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import com.oplus.settings.feature.display.atmospherelight.utils.AtmosphereLightStatisticsUtils;
import com.oplus.settings.feature.language.localepicker.RegionPickerUtils;
import com.oplus.view.material.OplusMaterialEdgeParams;
import com.oplus.view.material.OplusMaterialShadowParams;
import com.oplus.view.material.OplusMaterialUtil;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\b\u0007\u0018\u0000 \u00032\u00020\u0001:\u0002\u0003\u0004B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0005"}, d2 = {"Lcom/coui/appcompat/COUIMaterialStrokeEffect;", "", "()V", "Companion", "StrokeEffectType", "coui-support-effect_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class COUIMaterialStrokeEffect {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final Companion.StrokeEffectParams NO_EFFECT = new Companion.StrokeEffectParams(0, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private static final Companion.StrokeEffectParams NO_EFFECT_BASE_1 = new Companion.StrokeEffectParams(1, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private static final Companion.StrokeEffectParams NO_EFFECT_BASE_2 = new Companion.StrokeEffectParams(2, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_1_LIGHT = new Companion.StrokeEffectParams(1, 0.2f, 2.2f, 0.0f, 0.1f, 1.0f, 1.15f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_1_DISABLED_LIGHT = new Companion.StrokeEffectParams(1, 0.09f, 2.2f, 0.0f, 0.003f, 0.3f, 1.15f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_2_LIGHT = new Companion.StrokeEffectParams(1, 0.2f, 2.0f, 0.0f, 0.1f, 1.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_2_DISABLED_LIGHT = new Companion.StrokeEffectParams(1, 0.06f, 2.0f, 0.0f, 0.03f, 0.3f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_3_LIGHT = new Companion.StrokeEffectParams(1, 0.25f, 2.5f, 0.0f, 0.1f, 1.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_3_DISABLED_LIGHT = new Companion.StrokeEffectParams(1, 0.075f, 2.5f, 0.0f, 0.03f, 0.3f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_4_LIGHT = new Companion.StrokeEffectParams(1, 0.2f, 2.2f, 0.0f, 0.15f, 1.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_4_DISABLED_LIGHT = new Companion.StrokeEffectParams(1, 0.06f, 2.2f, 0.0f, 0.045f, 0.3f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_5_LIGHT = new Companion.StrokeEffectParams(1, 0.2f, 2.3f, 0.0f, 0.1f, 1.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_5_DISABLED_LIGHT = new Companion.StrokeEffectParams(1, 0.075f, 2.5f, 0.0f, 0.03f, 0.3f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_6_LIGHT = new Companion.StrokeEffectParams(1, 0.6f, 2.5f, 0.0f, 0.15f, 1.0f, 1.15f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_6_DISABLED_LIGHT = new Companion.StrokeEffectParams(1, 0.024f, 2.5f, 0.0f, 0.03f, 0.3f, 1.15f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CIRCLE_PARAMS_1_LIGHT = new Companion.StrokeEffectParams(2, 0.2f, 1.9f, 0.0f, 0.15f, 1.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CIRCLE_PARAMS_1_DISABLED_LIGHT = new Companion.StrokeEffectParams(2, 0.06f, 1.9f, 0.0f, 0.045f, 0.3f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CIRCLE_PARAMS_2_LIGHT = new Companion.StrokeEffectParams(2, 0.25f, 2.5f, 0.0f, 0.3f, 1.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CIRCLE_PARAMS_2_DISABLED_LIGHT = new Companion.StrokeEffectParams(2, 0.075f, 2.5f, 0.0f, 0.09f, 0.3f, 2.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CIRCLE_PARAMS_3 = new Companion.StrokeEffectParams(2, 0.5f, 2.0f, 0.0f, 0.01f, 1.0f, 1.15f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CIRCLE_PARAMS_4_LIGHT = new Companion.StrokeEffectParams(2, 0.8f, 1.8f, 0.0f, 0.1f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CIRCLE_PARAMS_5 = new Companion.StrokeEffectParams(2, 0.28f, 2.5f, 0.0f, 0.01f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_1_LIGHT = new Companion.StrokeEffectParams(1, 0.3f, 2.8f, 0.0f, 0.1f, 1.0f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_1_DISABLED_LIGHT = new Companion.StrokeEffectParams(1, 0.12f, 2.8f, 0.0f, 0.03f, 0.3f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_2_LIGHT = new Companion.StrokeEffectParams(1, 0.5f, 2.8f, 0.0f, 0.1f, 1.0f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_2_DISABLED_LIGHT = new Companion.StrokeEffectParams(1, 0.15f, 2.8f, 0.0f, 0.03f, 0.3f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_3_LIGHT = new Companion.StrokeEffectParams(1, 0.5f, 2.8f, 0.0f, 0.05f, 1.0f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_3_DISABLED_LIGHT = new Companion.StrokeEffectParams(1, 0.15f, 2.8f, 0.0f, 0.015f, 0.3f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_4_LIGHT = new Companion.StrokeEffectParams(1, 0.3f, 2.5f, 0.0f, 0.1f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_4_DISABLED_LIGHT = new Companion.StrokeEffectParams(1, 0.3f, 2.5f, 0.0f, 0.04f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_5_LIGHT = new Companion.StrokeEffectParams(1, 0.8f, 3.0f, 0.0f, 0.05f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_5_DISABLED_LIGHT = new Companion.StrokeEffectParams(1, 0.24f, 3.0f, 0.0f, 0.05f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_6_LIGHT = new Companion.StrokeEffectParams(1, 0.5f, 3.0f, 0.0f, 0.01f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_6_DISABLED_LIGHT = new Companion.StrokeEffectParams(1, 0.15f, 3.0f, 0.0f, 0.01f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_8_LIGHT = new Companion.StrokeEffectParams(1, 0.8f, 1.8f, 0.0f, 0.1f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_9 = new Companion.StrokeEffectParams(1, 0.18f, 2.5f, 0.0f, 0.01f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_10_LIGHT = new Companion.StrokeEffectParams(1, 0.8f, 1.8f, 0.0f, 0.05f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_11_LIGHT = new Companion.StrokeEffectParams(1, 0.45f, 2.5f, 0.0f, 0.15f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_12_LIGHT = new Companion.StrokeEffectParams(1, 0.8f, 2.0f, 0.0f, 0.1f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_13_LIGHT = new Companion.StrokeEffectParams(1, 0.8f, 2.3f, 0.0f, 0.1f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_13_DISABLED_LIGHT = new Companion.StrokeEffectParams(1, 0.24f, 2.3f, 0.0f, 0.03f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_1_DARK = new Companion.StrokeEffectParams(1, 0.6f, 2.2f, 0.0f, 0.01f, 1.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_1_DISABLED_DARK = new Companion.StrokeEffectParams(1, 0.18f, 2.2f, 0.0f, 0.003f, 0.3f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_2_DARK = new Companion.StrokeEffectParams(1, 0.6f, 2.0f, 0.0f, 0.02f, 1.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_2_DISABLED_DARK = new Companion.StrokeEffectParams(1, 0.18f, 2.0f, 0.0f, 0.006f, 0.3f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_3_DARK = new Companion.StrokeEffectParams(1, 0.8f, 2.5f, 0.0f, 0.03f, 1.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_3_DISABLED_DARK = new Companion.StrokeEffectParams(1, 0.24f, 2.5f, 0.0f, 0.009f, 0.3f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_4_DARK = new Companion.StrokeEffectParams(1, 0.6f, 2.2f, 0.0f, 0.1f, 1.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_4_DISABLED_DARK = new Companion.StrokeEffectParams(1, 0.18f, 2.2f, 0.0f, 0.03f, 0.3f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_5_DARK = new Companion.StrokeEffectParams(1, 0.8f, 2.5f, 0.0f, 0.03f, 1.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_5_DISABLED_DARK = new Companion.StrokeEffectParams(1, 0.24f, 2.5f, 0.0f, 0.09f, 0.3f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_6_DARK = new Companion.StrokeEffectParams(1, 0.8f, 2.5f, 0.0f, 0.05f, 1.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_6_DISABLED_DARK = new Companion.StrokeEffectParams(1, 0.18f, 2.5f, 0.0f, 0.015f, 0.3f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_7 = new Companion.StrokeEffectParams(1, 0.5f, 2.5f, 0.0f, 0.05f, 1.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CIRCLE_PARAMS_1_DARK = new Companion.StrokeEffectParams(2, 0.6f, 1.9f, 0.0f, 0.1f, 1.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CIRCLE_PARAMS_1_DISABLED_DARK = new Companion.StrokeEffectParams(2, 0.18f, 1.9f, 0.0f, 0.03f, 0.3f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CIRCLE_PARAMS_2_DARK = new Companion.StrokeEffectParams(2, 0.25f, 2.5f, 0.0f, 0.3f, 1.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CIRCLE_PARAMS_2_DISABLED_DARK = new Companion.StrokeEffectParams(2, 0.12f, 2.5f, 0.0f, 0.09f, 0.3f, 2.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CIRCLE_PARAMS_4_DARK = new Companion.StrokeEffectParams(2, 0.15f, 1.8f, 0.0f, 0.01f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_1_DARK = new Companion.StrokeEffectParams(1, 0.26f, 2.8f, 0.0f, 0.08f, 1.0f, 0.8f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_1_DISABLED_DARK = new Companion.StrokeEffectParams(1, 0.18f, 2.8f, 0.0f, 0.024f, 0.3f, 0.8f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_2_DARK = new Companion.StrokeEffectParams(1, 0.5f, 2.8f, 0.0f, 0.05f, 1.0f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_2_DISABLED_DARK = new Companion.StrokeEffectParams(1, 0.15f, 2.8f, 0.0f, 0.015f, 0.3f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_3_DARK = new Companion.StrokeEffectParams(1, 0.5f, 2.8f, 0.0f, 0.05f, 1.0f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_3_DISABLED_DARK = new Companion.StrokeEffectParams(1, 0.15f, 2.8f, 0.0f, 0.015f, 0.3f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_4_DARK = new Companion.StrokeEffectParams(1, 0.25f, 2.5f, 0.0f, 0.1f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_4_DISABLED_DARK = new Companion.StrokeEffectParams(1, 0.05f, 2.5f, 0.0f, 0.06f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_5_DARK = new Companion.StrokeEffectParams(1, 0.1f, 3.0f, 0.0f, 0.01f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_5_DISABLED_DARK = new Companion.StrokeEffectParams(1, 0.03f, 3.0f, 0.0f, 0.01f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_6_DARK = new Companion.StrokeEffectParams(1, 0.15f, 3.0f, 0.0f, 0.01f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_6_DISABLED_DARK = new Companion.StrokeEffectParams(1, 0.045f, 3.0f, 0.0f, 0.01f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_8_DARK = new Companion.StrokeEffectParams(1, 0.15f, 1.8f, 0.0f, 0.01f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_10_DARK = new Companion.StrokeEffectParams(1, 0.15f, 1.8f, 0.0f, 0.02f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_11_DARK = new Companion.StrokeEffectParams(1, 0.1f, 2.5f, 0.0f, 0.03f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_12_DARK = new Companion.StrokeEffectParams(1, 0.15f, 2.0f, 0.0f, 0.01f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_13_DARK = new Companion.StrokeEffectParams(1, 0.15f, 2.3f, 0.0f, 0.01f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams FRAMEWORK_CAPSULE_PARAMS_13_DISABLED_DARK = new Companion.StrokeEffectParams(1, 0.045f, 2.3f, 0.0f, 0.003f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_7 = new Companion.StrokeEffectParams(1, 0.25f, 1.8f, 0.0f, 0.03f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_7_DISABLED = new Companion.StrokeEffectParams(1, 0.08f, 1.8f, 0.0f, 0.01f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_8 = new Companion.StrokeEffectParams(1, 0.25f, 1.8f, 0.0f, 0.03f, 0.0f, 1.0f);
    private static final Companion.StrokeEffectParams CONTENT_CAPSULE_PARAMS_8_DISABLED = new Companion.StrokeEffectParams(1, 0.08f, 1.8f, 0.0f, 0.01f, 0.0f, 1.0f);

    @Metadata(d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\bQ\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001:\u0002tuB\u0007\b\u0002¢\u0006\u0002\u0010\u0002J \u0010U\u001a\u00020V2\u0006\u0010W\u001a\u00020V2\u0006\u0010X\u001a\u00020V2\u0006\u0010Y\u001a\u00020VH\u0002J\u0018\u0010Z\u001a\u00020[2\u0006\u0010\\\u001a\u00020]2\u0006\u0010^\u001a\u00020\u0004H\u0002J\u0018\u0010_\u001a\u00020[2\u0006\u0010\\\u001a\u00020]2\u0006\u0010`\u001a\u00020aH\u0002J\u0018\u0010b\u001a\u00020c2\u0006\u0010\\\u001a\u00020]2\u0006\u0010^\u001a\u00020\u0004H\u0002J\u0018\u0010d\u001a\u00020c2\u0006\u0010\\\u001a\u00020]2\u0006\u0010`\u001a\u00020aH\u0002J\u001d\u0010e\u001a\u00020f2\u0006\u0010\\\u001a\u00020]2\u0006\u0010^\u001a\u00020\u0004H\u0000¢\u0006\u0002\bgJ\u001d\u0010e\u001a\u00020f2\u0006\u0010\\\u001a\u00020]2\u0006\u0010`\u001a\u00020aH\u0000¢\u0006\u0002\bgJ\u001d\u0010h\u001a\u00020\u00042\u0006\u0010\\\u001a\u00020]2\u0006\u0010`\u001a\u00020aH\u0000¢\u0006\u0002\biJ\u0010\u0010j\u001a\u00020k2\u0006\u0010\\\u001a\u00020]H\u0002J\u001d\u0010l\u001a\u00020m2\u0006\u0010n\u001a\u00020o2\u0006\u0010^\u001a\u00020\u0004H\u0000¢\u0006\u0002\bpJ9\u0010l\u001a\u00020m2\u0006\u0010n\u001a\u00020o2\u0006\u0010q\u001a\u00020\u00042\u0006\u0010r\u001a\u00020\u00042\b\u0010s\u001a\u0004\u0018\u00010f2\b\b\u0001\u0010Y\u001a\u00020VH\u0000¢\u0006\u0002\bpJ\u001d\u0010l\u001a\u00020m2\u0006\u0010n\u001a\u00020o2\u0006\u0010`\u001a\u00020aH\u0000¢\u0006\u0002\bpR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u00102\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u00103\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u00105\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u00106\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u00108\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u00109\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010;\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010<\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010=\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010>\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010?\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010@\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010A\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010B\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010C\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010D\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010E\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010F\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010G\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010H\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010I\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010J\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010K\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010L\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010M\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010N\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010O\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010P\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010Q\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010R\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010S\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010T\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006v"}, d2 = {"Lcom/coui/appcompat/COUIMaterialStrokeEffect$Companion;", "", "()V", "CONTENT_CAPSULE_PARAMS_1_DARK", "Lcom/coui/appcompat/COUIMaterialStrokeEffect$Companion$StrokeEffectParams;", "CONTENT_CAPSULE_PARAMS_1_DISABLED_DARK", "CONTENT_CAPSULE_PARAMS_1_DISABLED_LIGHT", "CONTENT_CAPSULE_PARAMS_1_LIGHT", "CONTENT_CAPSULE_PARAMS_2_DARK", "CONTENT_CAPSULE_PARAMS_2_DISABLED_DARK", "CONTENT_CAPSULE_PARAMS_2_DISABLED_LIGHT", "CONTENT_CAPSULE_PARAMS_2_LIGHT", "CONTENT_CAPSULE_PARAMS_3_DARK", "CONTENT_CAPSULE_PARAMS_3_DISABLED_DARK", "CONTENT_CAPSULE_PARAMS_3_DISABLED_LIGHT", "CONTENT_CAPSULE_PARAMS_3_LIGHT", "CONTENT_CAPSULE_PARAMS_4_DARK", "CONTENT_CAPSULE_PARAMS_4_DISABLED_DARK", "CONTENT_CAPSULE_PARAMS_4_DISABLED_LIGHT", "CONTENT_CAPSULE_PARAMS_4_LIGHT", "CONTENT_CAPSULE_PARAMS_5_DARK", "CONTENT_CAPSULE_PARAMS_5_DISABLED_DARK", "CONTENT_CAPSULE_PARAMS_5_DISABLED_LIGHT", "CONTENT_CAPSULE_PARAMS_5_LIGHT", "CONTENT_CAPSULE_PARAMS_6_DARK", "CONTENT_CAPSULE_PARAMS_6_DISABLED_DARK", "CONTENT_CAPSULE_PARAMS_6_DISABLED_LIGHT", "CONTENT_CAPSULE_PARAMS_6_LIGHT", "CONTENT_CAPSULE_PARAMS_7", "CONTENT_CAPSULE_PARAMS_7_DISABLED", "CONTENT_CAPSULE_PARAMS_8", "CONTENT_CAPSULE_PARAMS_8_DISABLED", "FRAMEWORK_CAPSULE_PARAMS_10_DARK", "FRAMEWORK_CAPSULE_PARAMS_10_LIGHT", "FRAMEWORK_CAPSULE_PARAMS_11_DARK", "FRAMEWORK_CAPSULE_PARAMS_11_LIGHT", "FRAMEWORK_CAPSULE_PARAMS_12_DARK", "FRAMEWORK_CAPSULE_PARAMS_12_LIGHT", "FRAMEWORK_CAPSULE_PARAMS_13_DARK", "FRAMEWORK_CAPSULE_PARAMS_13_DISABLED_DARK", "FRAMEWORK_CAPSULE_PARAMS_13_DISABLED_LIGHT", "FRAMEWORK_CAPSULE_PARAMS_13_LIGHT", "FRAMEWORK_CAPSULE_PARAMS_1_DARK", "FRAMEWORK_CAPSULE_PARAMS_1_DISABLED_DARK", "FRAMEWORK_CAPSULE_PARAMS_1_DISABLED_LIGHT", "FRAMEWORK_CAPSULE_PARAMS_1_LIGHT", "FRAMEWORK_CAPSULE_PARAMS_2_DARK", "FRAMEWORK_CAPSULE_PARAMS_2_DISABLED_DARK", "FRAMEWORK_CAPSULE_PARAMS_2_DISABLED_LIGHT", "FRAMEWORK_CAPSULE_PARAMS_2_LIGHT", "FRAMEWORK_CAPSULE_PARAMS_3_DARK", "FRAMEWORK_CAPSULE_PARAMS_3_DISABLED_DARK", "FRAMEWORK_CAPSULE_PARAMS_3_DISABLED_LIGHT", "FRAMEWORK_CAPSULE_PARAMS_3_LIGHT", "FRAMEWORK_CAPSULE_PARAMS_4_DARK", "FRAMEWORK_CAPSULE_PARAMS_4_DISABLED_DARK", "FRAMEWORK_CAPSULE_PARAMS_4_DISABLED_LIGHT", "FRAMEWORK_CAPSULE_PARAMS_4_LIGHT", "FRAMEWORK_CAPSULE_PARAMS_5_DARK", "FRAMEWORK_CAPSULE_PARAMS_5_DISABLED_DARK", "FRAMEWORK_CAPSULE_PARAMS_5_DISABLED_LIGHT", "FRAMEWORK_CAPSULE_PARAMS_5_LIGHT", "FRAMEWORK_CAPSULE_PARAMS_6_DARK", "FRAMEWORK_CAPSULE_PARAMS_6_DISABLED_DARK", "FRAMEWORK_CAPSULE_PARAMS_6_DISABLED_LIGHT", "FRAMEWORK_CAPSULE_PARAMS_6_LIGHT", "FRAMEWORK_CAPSULE_PARAMS_7", "FRAMEWORK_CAPSULE_PARAMS_8_DARK", "FRAMEWORK_CAPSULE_PARAMS_8_LIGHT", "FRAMEWORK_CAPSULE_PARAMS_9", "FRAMEWORK_CIRCLE_PARAMS_1_DARK", "FRAMEWORK_CIRCLE_PARAMS_1_DISABLED_DARK", "FRAMEWORK_CIRCLE_PARAMS_1_DISABLED_LIGHT", "FRAMEWORK_CIRCLE_PARAMS_1_LIGHT", "FRAMEWORK_CIRCLE_PARAMS_2_DARK", "FRAMEWORK_CIRCLE_PARAMS_2_DISABLED_DARK", "FRAMEWORK_CIRCLE_PARAMS_2_DISABLED_LIGHT", "FRAMEWORK_CIRCLE_PARAMS_2_LIGHT", "FRAMEWORK_CIRCLE_PARAMS_3", "FRAMEWORK_CIRCLE_PARAMS_4_DARK", "FRAMEWORK_CIRCLE_PARAMS_4_LIGHT", "FRAMEWORK_CIRCLE_PARAMS_5", "NO_EFFECT", "NO_EFFECT_BASE_1", "NO_EFFECT_BASE_2", "floatEvaluator", "", AtmosphereLightStatisticsUtils.VAL_ACTION_START, AtmosphereLightStatisticsUtils.VAL_ACTION_END, "progress", "getEdgeParams", "Lcom/oplus/view/material/OplusMaterialEdgeParams;", "context", "Landroid/content/Context;", "params", "getEdgeParamsByType", "type", "Lcom/coui/appcompat/COUIMaterialStrokeEffect$StrokeEffectType;", "getShadowParams", "Lcom/oplus/view/material/OplusMaterialShadowParams;", "getShadowParamsByType", "getStrokeEffectParamsHolder", "Lcom/coui/appcompat/COUIMaterialStrokeEffect$Companion$StrokeEffectParamsHolder;", "getStrokeEffectParamsHolder$coui_support_effect_release", "getStrokeParamsByType", "getStrokeParamsByType$coui_support_effect_release", "isNightMode", "", "setViewStrokeEffect", "", "view", "Landroid/view/View;", "setViewStrokeEffect$coui_support_effect_release", "startParams", "endParams", "holder", "StrokeEffectParams", "StrokeEffectParamsHolder", "coui-support-effect_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {

        @Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0019\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B=\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\u0006\u0010\b\u001a\u00020\u0005\u0012\u0006\u0010\t\u001a\u00020\u0005\u0012\u0006\u0010\n\u001a\u00020\u0005¢\u0006\u0002\u0010\u000bJ\t\u0010\u0015\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0016\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0017\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0018\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0019\u001a\u00020\u0005HÆ\u0003J\t\u0010\u001a\u001a\u00020\u0005HÆ\u0003J\t\u0010\u001b\u001a\u00020\u0005HÆ\u0003JO\u0010\u001c\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\u00052\b\b\u0002\u0010\t\u001a\u00020\u00052\b\b\u0002\u0010\n\u001a\u00020\u0005HÆ\u0001J\u0018\u0010\u001d\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0005H\u0002J\u0013\u0010!\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\u000e\u0010$\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00020\u001fJ\t\u0010%\u001a\u00020\u0003HÖ\u0001J\t\u0010&\u001a\u00020'HÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0007\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\rR\u0011\u0010\b\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\rR\u0011\u0010\t\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\rR\u0011\u0010\n\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\rR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014¨\u0006("}, d2 = {"Lcom/coui/appcompat/COUIMaterialStrokeEffect$Companion$StrokeEffectParams;", "", "strokeType", "", "edgeAlpha", "", "edgeWidthDp", "edgeAngle", "shadowFadeIn", "shadowFadeOut", "shadowFadeScale", "(IFFFFFF)V", "getEdgeAlpha", "()F", "getEdgeAngle", "getEdgeWidthDp", "getShadowFadeIn", "getShadowFadeOut", "getShadowFadeScale", "getStrokeType", "()I", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "dp2px", "context", "Landroid/content/Context;", "dp", "equals", "", RegionPickerUtils.OTHER_SYSTEM_REGION_KEY, "getEdgeWidth", "hashCode", "toString", "", "coui-support-effect_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
        public static final /* data */ class StrokeEffectParams {
            private final float edgeAlpha;
            private final float edgeAngle;
            private final float edgeWidthDp;
            private final float shadowFadeIn;
            private final float shadowFadeOut;
            private final float shadowFadeScale;
            private final int strokeType;

            public StrokeEffectParams(int i3, float f3, float f4, float f5, float f6, float f7, float f8) {
                this.strokeType = i3;
                this.edgeAlpha = f3;
                this.edgeWidthDp = f4;
                this.edgeAngle = f5;
                this.shadowFadeIn = f6;
                this.shadowFadeOut = f7;
                this.shadowFadeScale = f8;
            }

            public static /* synthetic */ StrokeEffectParams copy$default(StrokeEffectParams strokeEffectParams, int i3, float f3, float f4, float f5, float f6, float f7, float f8, int i4, Object obj) {
                if ((i4 & 1) != 0) {
                    i3 = strokeEffectParams.strokeType;
                }
                if ((i4 & 2) != 0) {
                    f3 = strokeEffectParams.edgeAlpha;
                }
                if ((i4 & 4) != 0) {
                    f4 = strokeEffectParams.edgeWidthDp;
                }
                if ((i4 & 8) != 0) {
                    f5 = strokeEffectParams.edgeAngle;
                }
                if ((i4 & 16) != 0) {
                    f6 = strokeEffectParams.shadowFadeIn;
                }
                if ((i4 & 32) != 0) {
                    f7 = strokeEffectParams.shadowFadeOut;
                }
                if ((i4 & 64) != 0) {
                    f8 = strokeEffectParams.shadowFadeScale;
                }
                float f9 = f7;
                float f10 = f8;
                float f11 = f6;
                float f12 = f4;
                return strokeEffectParams.copy(i3, f3, f12, f5, f11, f9, f10);
            }

            private final float dp2px(Context context, float dp) {
                return TypedValue.applyDimension(1, dp, context.getResources().getDisplayMetrics());
            }

            /* JADX INFO: renamed from: component1, reason: from getter */
            public final int getStrokeType() {
                return this.strokeType;
            }

            /* JADX INFO: renamed from: component2, reason: from getter */
            public final float getEdgeAlpha() {
                return this.edgeAlpha;
            }

            /* JADX INFO: renamed from: component3, reason: from getter */
            public final float getEdgeWidthDp() {
                return this.edgeWidthDp;
            }

            /* JADX INFO: renamed from: component4, reason: from getter */
            public final float getEdgeAngle() {
                return this.edgeAngle;
            }

            /* JADX INFO: renamed from: component5, reason: from getter */
            public final float getShadowFadeIn() {
                return this.shadowFadeIn;
            }

            /* JADX INFO: renamed from: component6, reason: from getter */
            public final float getShadowFadeOut() {
                return this.shadowFadeOut;
            }

            /* JADX INFO: renamed from: component7, reason: from getter */
            public final float getShadowFadeScale() {
                return this.shadowFadeScale;
            }

            public final StrokeEffectParams copy(int strokeType, float edgeAlpha, float edgeWidthDp, float edgeAngle, float shadowFadeIn, float shadowFadeOut, float shadowFadeScale) {
                return new StrokeEffectParams(strokeType, edgeAlpha, edgeWidthDp, edgeAngle, shadowFadeIn, shadowFadeOut, shadowFadeScale);
            }

            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                if (!(other instanceof StrokeEffectParams)) {
                    return false;
                }
                StrokeEffectParams strokeEffectParams = (StrokeEffectParams) other;
                return this.strokeType == strokeEffectParams.strokeType && Float.compare(this.edgeAlpha, strokeEffectParams.edgeAlpha) == 0 && Float.compare(this.edgeWidthDp, strokeEffectParams.edgeWidthDp) == 0 && Float.compare(this.edgeAngle, strokeEffectParams.edgeAngle) == 0 && Float.compare(this.shadowFadeIn, strokeEffectParams.shadowFadeIn) == 0 && Float.compare(this.shadowFadeOut, strokeEffectParams.shadowFadeOut) == 0 && Float.compare(this.shadowFadeScale, strokeEffectParams.shadowFadeScale) == 0;
            }

            public final float getEdgeAlpha() {
                return this.edgeAlpha;
            }

            public final float getEdgeAngle() {
                return this.edgeAngle;
            }

            public final float getEdgeWidth(Context context) {
                Intrinsics.checkNotNullParameter(context, "context");
                return dp2px(context, this.edgeWidthDp);
            }

            public final float getEdgeWidthDp() {
                return this.edgeWidthDp;
            }

            public final float getShadowFadeIn() {
                return this.shadowFadeIn;
            }

            public final float getShadowFadeOut() {
                return this.shadowFadeOut;
            }

            public final float getShadowFadeScale() {
                return this.shadowFadeScale;
            }

            public final int getStrokeType() {
                return this.strokeType;
            }

            public int hashCode() {
                return (((((((((((Integer.hashCode(this.strokeType) * 31) + Float.hashCode(this.edgeAlpha)) * 31) + Float.hashCode(this.edgeWidthDp)) * 31) + Float.hashCode(this.edgeAngle)) * 31) + Float.hashCode(this.shadowFadeIn)) * 31) + Float.hashCode(this.shadowFadeOut)) * 31) + Float.hashCode(this.shadowFadeScale);
            }

            public String toString() {
                return "StrokeEffectParams(strokeType=" + this.strokeType + ", edgeAlpha=" + this.edgeAlpha + ", edgeWidthDp=" + this.edgeWidthDp + ", edgeAngle=" + this.edgeAngle + ", shadowFadeIn=" + this.shadowFadeIn + ", shadowFadeOut=" + this.shadowFadeOut + ", shadowFadeScale=" + this.shadowFadeScale + ')';
            }
        }

        @Metadata(d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u001d\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0000¢\u0006\u0002\b\u0011J>\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\u00152\u0006\u0010\u0019\u001a\u00020\u00152\u0006\u0010\u001a\u001a\u00020\u0015R\u0014\u0010\u0007\u001a\u00020\bX\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0014\u0010\u000b\u001a\u00020\fX\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u001b"}, d2 = {"Lcom/coui/appcompat/COUIMaterialStrokeEffect$Companion$StrokeEffectParamsHolder;", "", "context", "Landroid/content/Context;", "params", "Lcom/coui/appcompat/COUIMaterialStrokeEffect$Companion$StrokeEffectParams;", "(Landroid/content/Context;Lcom/coui/appcompat/COUIMaterialStrokeEffect$Companion$StrokeEffectParams;)V", "edgeParams", "Lcom/oplus/view/material/OplusMaterialEdgeParams;", "getEdgeParams$coui_support_effect_release", "()Lcom/oplus/view/material/OplusMaterialEdgeParams;", "shadowParams", "Lcom/oplus/view/material/OplusMaterialShadowParams;", "getShadowParams$coui_support_effect_release", "()Lcom/oplus/view/material/OplusMaterialShadowParams;", "updateParams", "", "updateParams$coui_support_effect_release", "strokeType", "", "edgeAlpha", "", "edgeWidth", "edgeAngle", "shadowFadeIn", "shadowFadeOut", "shadowFadeScale", "coui-support-effect_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
        public static final class StrokeEffectParamsHolder {
            private final OplusMaterialEdgeParams edgeParams;
            private final OplusMaterialShadowParams shadowParams;

            public StrokeEffectParamsHolder(Context context, StrokeEffectParams params) {
                Intrinsics.checkNotNullParameter(context, "context");
                Intrinsics.checkNotNullParameter(params, "params");
                Companion companion = COUIMaterialStrokeEffect.INSTANCE;
                this.edgeParams = Companion.access$getEdgeParams(companion, context, params);
                this.shadowParams = Companion.access$getShadowParams(companion, context, params);
            }

            /* JADX INFO: renamed from: getEdgeParams$coui_support_effect_release, reason: from getter */
            public final OplusMaterialEdgeParams getEdgeParams() {
                return this.edgeParams;
            }

            /* JADX INFO: renamed from: getShadowParams$coui_support_effect_release, reason: from getter */
            public final OplusMaterialShadowParams getShadowParams() {
                return this.shadowParams;
            }

            public final void updateParams(int strokeType, float edgeAlpha, float edgeWidth, float edgeAngle, float shadowFadeIn, float shadowFadeOut, float shadowFadeScale) {
                this.edgeParams.setType(strokeType);
                this.edgeParams.setLineAlpha(edgeAlpha);
                this.edgeParams.setLineWidth(edgeWidth);
                this.edgeParams.setLineAngle(edgeAngle);
                this.shadowParams.setType(strokeType);
                this.shadowParams.setFadeInAlpha(shadowFadeIn);
                this.shadowParams.setFadeOutAlpha(shadowFadeOut);
                this.shadowParams.setFadeWidthScale(shadowFadeScale);
            }

            public final void updateParams$coui_support_effect_release(Context context, StrokeEffectParams params) {
                Intrinsics.checkNotNullParameter(context, "context");
                Intrinsics.checkNotNullParameter(params, "params");
                this.edgeParams.setType(params.getStrokeType());
                this.edgeParams.setLineAlpha(params.getEdgeAlpha());
                this.edgeParams.setLineWidth(params.getEdgeWidth(context));
                this.edgeParams.setLineAngle(params.getEdgeAngle());
                this.shadowParams.setType(params.getStrokeType());
                this.shadowParams.setFadeInAlpha(params.getShadowFadeIn());
                this.shadowParams.setFadeOutAlpha(params.getShadowFadeOut());
                this.shadowParams.setFadeWidthScale(params.getShadowFadeScale());
            }
        }

        @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
        public /* synthetic */ class WhenMappings {
            public static final /* synthetic */ int[] $EnumSwitchMapping$0;

            static {
                int[] iArr = new int[StrokeEffectType.values().length];
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_1.ordinal()] = 1;
                } catch (NoSuchFieldError unused) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_DISABLED_1.ordinal()] = 2;
                } catch (NoSuchFieldError unused2) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_2.ordinal()] = 3;
                } catch (NoSuchFieldError unused3) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_DISABLED_2.ordinal()] = 4;
                } catch (NoSuchFieldError unused4) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_3.ordinal()] = 5;
                } catch (NoSuchFieldError unused5) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_DISABLED_3.ordinal()] = 6;
                } catch (NoSuchFieldError unused6) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_4.ordinal()] = 7;
                } catch (NoSuchFieldError unused7) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_DISABLED_4.ordinal()] = 8;
                } catch (NoSuchFieldError unused8) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_5.ordinal()] = 9;
                } catch (NoSuchFieldError unused9) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_DISABLED_5.ordinal()] = 10;
                } catch (NoSuchFieldError unused10) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_6.ordinal()] = 11;
                } catch (NoSuchFieldError unused11) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_DISABLED_6.ordinal()] = 12;
                } catch (NoSuchFieldError unused12) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CIRCLE_1.ordinal()] = 13;
                } catch (NoSuchFieldError unused13) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CIRCLE_DISABLED_1.ordinal()] = 14;
                } catch (NoSuchFieldError unused14) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CIRCLE_2.ordinal()] = 15;
                } catch (NoSuchFieldError unused15) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CIRCLE_DISABLED_2.ordinal()] = 16;
                } catch (NoSuchFieldError unused16) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_7.ordinal()] = 17;
                } catch (NoSuchFieldError unused17) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CIRCLE_3.ordinal()] = 18;
                } catch (NoSuchFieldError unused18) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CIRCLE_4.ordinal()] = 19;
                } catch (NoSuchFieldError unused19) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CIRCLE_5.ordinal()] = 20;
                } catch (NoSuchFieldError unused20) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_CONTENT_CAPSULE_1.ordinal()] = 21;
                } catch (NoSuchFieldError unused21) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_CONTENT_CAPSULE_DISABLED_1.ordinal()] = 22;
                } catch (NoSuchFieldError unused22) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_CONTENT_CAPSULE_2.ordinal()] = 23;
                } catch (NoSuchFieldError unused23) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_CONTENT_CAPSULE_DISABLED_2.ordinal()] = 24;
                } catch (NoSuchFieldError unused24) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_CONTENT_CAPSULE_3.ordinal()] = 25;
                } catch (NoSuchFieldError unused25) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_CONTENT_CAPSULE_DISABLED_3.ordinal()] = 26;
                } catch (NoSuchFieldError unused26) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_CONTENT_CAPSULE_4.ordinal()] = 27;
                } catch (NoSuchFieldError unused27) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_CONTENT_CAPSULE_DISABLED_4.ordinal()] = 28;
                } catch (NoSuchFieldError unused28) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_CONTENT_CAPSULE_5.ordinal()] = 29;
                } catch (NoSuchFieldError unused29) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_CONTENT_CAPSULE_DISABLED_5.ordinal()] = 30;
                } catch (NoSuchFieldError unused30) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_CONTENT_CAPSULE_6.ordinal()] = 31;
                } catch (NoSuchFieldError unused31) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_CONTENT_CAPSULE_DISABLED_6.ordinal()] = 32;
                } catch (NoSuchFieldError unused32) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_8.ordinal()] = 33;
                } catch (NoSuchFieldError unused33) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_9.ordinal()] = 34;
                } catch (NoSuchFieldError unused34) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_10.ordinal()] = 35;
                } catch (NoSuchFieldError unused35) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_11.ordinal()] = 36;
                } catch (NoSuchFieldError unused36) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_12.ordinal()] = 37;
                } catch (NoSuchFieldError unused37) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_13.ordinal()] = 38;
                } catch (NoSuchFieldError unused38) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_DISABLED_13.ordinal()] = 39;
                } catch (NoSuchFieldError unused39) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_CONTENT_CAPSULE_7.ordinal()] = 40;
                } catch (NoSuchFieldError unused40) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_CONTENT_CAPSULE_DISABLED_7.ordinal()] = 41;
                } catch (NoSuchFieldError unused41) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_CONTENT_CAPSULE_8.ordinal()] = 42;
                } catch (NoSuchFieldError unused42) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_CONTENT_CAPSULE_DISABLED_8.ordinal()] = 43;
                } catch (NoSuchFieldError unused43) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_NO_EFFECT.ordinal()] = 44;
                } catch (NoSuchFieldError unused44) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_NO_EFFECT_BASE_1.ordinal()] = 45;
                } catch (NoSuchFieldError unused45) {
                }
                try {
                    iArr[StrokeEffectType.TYPE_NO_EFFECT_BASE_2.ordinal()] = 46;
                } catch (NoSuchFieldError unused46) {
                }
                $EnumSwitchMapping$0 = iArr;
            }
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static final /* synthetic */ OplusMaterialEdgeParams access$getEdgeParams(Companion companion, Context context, StrokeEffectParams strokeEffectParams) {
            return companion.getEdgeParams(context, strokeEffectParams);
        }

        public static final /* synthetic */ OplusMaterialShadowParams access$getShadowParams(Companion companion, Context context, StrokeEffectParams strokeEffectParams) {
            return companion.getShadowParams(context, strokeEffectParams);
        }

        private final float floatEvaluator(float start, float end, float progress) {
            return start + ((end - start) * progress);
        }

        private final OplusMaterialEdgeParams getEdgeParams(Context context, StrokeEffectParams params) {
            return new OplusMaterialEdgeParams(params.getStrokeType(), params.getEdgeWidth(context), params.getEdgeAlpha(), params.getEdgeAngle());
        }

        private final OplusMaterialEdgeParams getEdgeParamsByType(Context context, StrokeEffectType type) {
            return getEdgeParams(context, getStrokeParamsByType$coui_support_effect_release(context, type));
        }

        private final OplusMaterialShadowParams getShadowParams(Context context, StrokeEffectParams params) {
            return new OplusMaterialShadowParams(params.getStrokeType(), params.getShadowFadeScale(), params.getShadowFadeIn(), params.getShadowFadeOut());
        }

        private final OplusMaterialShadowParams getShadowParamsByType(Context context, StrokeEffectType type) {
            return getShadowParams(context, getStrokeParamsByType$coui_support_effect_release(context, type));
        }

        private final boolean isNightMode(Context context) {
            return COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(context);
        }

        public final StrokeEffectParamsHolder getStrokeEffectParamsHolder$coui_support_effect_release(Context context, StrokeEffectType type) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(type, "type");
            return getStrokeEffectParamsHolder$coui_support_effect_release(context, getStrokeParamsByType$coui_support_effect_release(context, type));
        }

        public final StrokeEffectParams getStrokeParamsByType$coui_support_effect_release(Context context, StrokeEffectType type) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(type, "type");
            switch (WhenMappings.$EnumSwitchMapping$0[type.ordinal()]) {
                case 1:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_1_DARK$cp() : COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_1_LIGHT$cp();
                case 2:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_1_DISABLED_DARK$cp() : COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_1_DISABLED_LIGHT$cp();
                case 3:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_2_DARK$cp() : COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_2_LIGHT$cp();
                case 4:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_2_DISABLED_DARK$cp() : COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_2_DISABLED_LIGHT$cp();
                case 5:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_3_DARK$cp() : COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_3_LIGHT$cp();
                case 6:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_3_DISABLED_DARK$cp() : COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_3_DISABLED_LIGHT$cp();
                case 7:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_4_DARK$cp() : COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_4_LIGHT$cp();
                case 8:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_4_DISABLED_DARK$cp() : COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_4_DISABLED_LIGHT$cp();
                case 9:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_5_DARK$cp() : COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_5_LIGHT$cp();
                case 10:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_5_DISABLED_DARK$cp() : COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_5_DISABLED_LIGHT$cp();
                case 11:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_6_DARK$cp() : COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_6_LIGHT$cp();
                case 12:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_6_DISABLED_DARK$cp() : COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_6_DISABLED_LIGHT$cp();
                case 13:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getFRAMEWORK_CIRCLE_PARAMS_1_DARK$cp() : COUIMaterialStrokeEffect.access$getFRAMEWORK_CIRCLE_PARAMS_1_LIGHT$cp();
                case 14:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getFRAMEWORK_CIRCLE_PARAMS_1_DISABLED_DARK$cp() : COUIMaterialStrokeEffect.access$getFRAMEWORK_CIRCLE_PARAMS_1_DISABLED_LIGHT$cp();
                case 15:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getFRAMEWORK_CIRCLE_PARAMS_2_DARK$cp() : COUIMaterialStrokeEffect.access$getFRAMEWORK_CIRCLE_PARAMS_2_LIGHT$cp();
                case 16:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getFRAMEWORK_CIRCLE_PARAMS_2_DISABLED_DARK$cp() : COUIMaterialStrokeEffect.access$getFRAMEWORK_CIRCLE_PARAMS_2_DISABLED_LIGHT$cp();
                case 17:
                    return COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_7$cp();
                case 18:
                    return COUIMaterialStrokeEffect.access$getFRAMEWORK_CIRCLE_PARAMS_3$cp();
                case 19:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getFRAMEWORK_CIRCLE_PARAMS_4_DARK$cp() : COUIMaterialStrokeEffect.access$getFRAMEWORK_CIRCLE_PARAMS_4_LIGHT$cp();
                case 20:
                    return COUIMaterialStrokeEffect.access$getFRAMEWORK_CIRCLE_PARAMS_5$cp();
                case 21:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_1_DARK$cp() : COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_1_LIGHT$cp();
                case 22:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_1_DISABLED_DARK$cp() : COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_1_DISABLED_LIGHT$cp();
                case 23:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_2_DARK$cp() : COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_2_LIGHT$cp();
                case 24:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_2_DISABLED_DARK$cp() : COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_2_DISABLED_LIGHT$cp();
                case 25:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_3_DARK$cp() : COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_3_LIGHT$cp();
                case 26:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_3_DISABLED_DARK$cp() : COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_3_DISABLED_LIGHT$cp();
                case 27:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_4_DARK$cp() : COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_4_LIGHT$cp();
                case 28:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_4_DISABLED_DARK$cp() : COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_4_DISABLED_LIGHT$cp();
                case 29:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_5_DARK$cp() : COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_5_LIGHT$cp();
                case 30:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_5_DISABLED_DARK$cp() : COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_5_DISABLED_LIGHT$cp();
                case 31:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_6_DARK$cp() : COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_6_LIGHT$cp();
                case 32:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_6_DISABLED_DARK$cp() : COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_6_DISABLED_LIGHT$cp();
                case 33:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_8_DARK$cp() : COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_8_LIGHT$cp();
                case 34:
                    return COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_9$cp();
                case 35:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_10_DARK$cp() : COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_10_LIGHT$cp();
                case 36:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_11_DARK$cp() : COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_11_LIGHT$cp();
                case 37:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_12_DARK$cp() : COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_12_LIGHT$cp();
                case 38:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_13_DARK$cp() : COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_13_LIGHT$cp();
                case 39:
                    return isNightMode(context) ? COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_13_DISABLED_DARK$cp() : COUIMaterialStrokeEffect.access$getFRAMEWORK_CAPSULE_PARAMS_13_DISABLED_LIGHT$cp();
                case 40:
                    return COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_7$cp();
                case 41:
                    return COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_7_DISABLED$cp();
                case 42:
                    return COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_8$cp();
                case 43:
                    return COUIMaterialStrokeEffect.access$getCONTENT_CAPSULE_PARAMS_8_DISABLED$cp();
                case 44:
                    return COUIMaterialStrokeEffect.access$getNO_EFFECT$cp();
                case 45:
                    return COUIMaterialStrokeEffect.access$getNO_EFFECT_BASE_1$cp();
                case 46:
                    return COUIMaterialStrokeEffect.access$getNO_EFFECT_BASE_2$cp();
                default:
                    throw new NoWhenBranchMatchedException();
            }
        }

        public final void setViewStrokeEffect$coui_support_effect_release(View view, StrokeEffectType type) {
            String message;
            String message2;
            Intrinsics.checkNotNullParameter(view, "view");
            Intrinsics.checkNotNullParameter(type, "type");
            if (COUIEffectVersionUtil.isMaterialVersion()) {
                try {
                    Context context = view.getContext();
                    Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
                    OplusMaterialUtil.setEdgeParams(view, getEdgeParamsByType(context, type));
                    Context context2 = view.getContext();
                    Intrinsics.checkNotNullExpressionValue(context2, "getContext(...)");
                    OplusMaterialUtil.setShadowParams(view, getShadowParamsByType(context2, type));
                } catch (NoClassDefFoundError e3) {
                    if (!COUIMaterialEffect.INSTANCE.getDEBUG$coui_support_effect_release() || (message2 = e3.getMessage()) == null) {
                        return;
                    }
                    Log.e(COUIMaterialEffect.TAG, message2);
                } catch (NoSuchMethodError e4) {
                    if (!COUIMaterialEffect.INSTANCE.getDEBUG$coui_support_effect_release() || (message = e4.getMessage()) == null) {
                        return;
                    }
                    Log.e(COUIMaterialEffect.TAG, message);
                }
            }
        }

        private Companion() {
        }

        public final StrokeEffectParamsHolder getStrokeEffectParamsHolder$coui_support_effect_release(Context context, StrokeEffectParams params) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(params, "params");
            return new StrokeEffectParamsHolder(context, params);
        }

        public final void setViewStrokeEffect$coui_support_effect_release(View view, StrokeEffectParams params) {
            String message;
            String message2;
            Intrinsics.checkNotNullParameter(view, "view");
            Intrinsics.checkNotNullParameter(params, "params");
            if (COUIEffectVersionUtil.isMaterialVersion()) {
                try {
                    Context context = view.getContext();
                    Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
                    OplusMaterialUtil.setEdgeParams(view, getEdgeParams(context, params));
                    Context context2 = view.getContext();
                    Intrinsics.checkNotNullExpressionValue(context2, "getContext(...)");
                    OplusMaterialUtil.setShadowParams(view, getShadowParams(context2, params));
                } catch (NoClassDefFoundError e3) {
                    if (!COUIMaterialEffect.INSTANCE.getDEBUG$coui_support_effect_release() || (message2 = e3.getMessage()) == null) {
                        return;
                    }
                    Log.e(COUIMaterialEffect.TAG, message2);
                } catch (NoSuchMethodError e4) {
                    if (!COUIMaterialEffect.INSTANCE.getDEBUG$coui_support_effect_release() || (message = e4.getMessage()) == null) {
                        return;
                    }
                    Log.e(COUIMaterialEffect.TAG, message);
                }
            }
        }

        public final void setViewStrokeEffect$coui_support_effect_release(View view, StrokeEffectParams startParams, StrokeEffectParams endParams, StrokeEffectParamsHolder holder, float progress) {
            StrokeEffectParamsHolder strokeEffectParamsHolder;
            String message;
            String message2;
            Intrinsics.checkNotNullParameter(view, "view");
            Intrinsics.checkNotNullParameter(startParams, "startParams");
            Intrinsics.checkNotNullParameter(endParams, "endParams");
            if (COUIEffectVersionUtil.isMaterialVersion()) {
                Context context = view.getContext();
                float fFloatEvaluator = floatEvaluator(startParams.getEdgeAlpha(), endParams.getEdgeAlpha(), progress);
                Intrinsics.checkNotNull(context);
                float fFloatEvaluator2 = floatEvaluator(startParams.getEdgeWidth(context), endParams.getEdgeWidth(context), progress);
                float fFloatEvaluator3 = floatEvaluator(startParams.getEdgeAngle(), endParams.getEdgeAngle(), progress);
                float fFloatEvaluator4 = floatEvaluator(startParams.getShadowFadeIn(), endParams.getShadowFadeIn(), progress);
                float fFloatEvaluator5 = floatEvaluator(startParams.getShadowFadeOut(), endParams.getShadowFadeOut(), progress);
                float fFloatEvaluator6 = floatEvaluator(startParams.getShadowFadeScale(), endParams.getShadowFadeScale(), progress);
                if (holder == null) {
                    Context context2 = view.getContext();
                    Intrinsics.checkNotNullExpressionValue(context2, "getContext(...)");
                    strokeEffectParamsHolder = new StrokeEffectParamsHolder(context2, startParams);
                } else {
                    strokeEffectParamsHolder = holder;
                }
                strokeEffectParamsHolder.updateParams(startParams.getStrokeType(), fFloatEvaluator, fFloatEvaluator2, fFloatEvaluator3, fFloatEvaluator4, fFloatEvaluator5, fFloatEvaluator6);
                try {
                    OplusMaterialUtil.setEdgeParams(view, strokeEffectParamsHolder.getEdgeParams());
                    OplusMaterialUtil.setShadowParams(view, strokeEffectParamsHolder.getShadowParams());
                } catch (NoClassDefFoundError e3) {
                    if (!COUIMaterialEffect.INSTANCE.getDEBUG$coui_support_effect_release() || (message2 = e3.getMessage()) == null) {
                        return;
                    }
                    Log.e(COUIMaterialEffect.TAG, message2);
                } catch (NoSuchMethodError e4) {
                    if (!COUIMaterialEffect.INSTANCE.getDEBUG$coui_support_effect_release() || (message = e4.getMessage()) == null) {
                        return;
                    }
                    Log.e(COUIMaterialEffect.TAG, message);
                }
            }
        }
    }

    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b0\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011j\u0002\b\u0012j\u0002\b\u0013j\u0002\b\u0014j\u0002\b\u0015j\u0002\b\u0016j\u0002\b\u0017j\u0002\b\u0018j\u0002\b\u0019j\u0002\b\u001aj\u0002\b\u001bj\u0002\b\u001cj\u0002\b\u001dj\u0002\b\u001ej\u0002\b\u001fj\u0002\b j\u0002\b!j\u0002\b\"j\u0002\b#j\u0002\b$j\u0002\b%j\u0002\b&j\u0002\b'j\u0002\b(j\u0002\b)j\u0002\b*j\u0002\b+j\u0002\b,j\u0002\b-j\u0002\b.j\u0002\b/j\u0002\b0¨\u00061"}, d2 = {"Lcom/coui/appcompat/COUIMaterialStrokeEffect$StrokeEffectType;", "", "(Ljava/lang/String;I)V", "TYPE_NO_EFFECT", "TYPE_NO_EFFECT_BASE_1", "TYPE_NO_EFFECT_BASE_2", "TYPE_FRAMEWORK_CAPSULE_1", "TYPE_FRAMEWORK_CAPSULE_DISABLED_1", "TYPE_FRAMEWORK_CAPSULE_2", "TYPE_FRAMEWORK_CAPSULE_DISABLED_2", "TYPE_FRAMEWORK_CAPSULE_3", "TYPE_FRAMEWORK_CAPSULE_DISABLED_3", "TYPE_FRAMEWORK_CAPSULE_4", "TYPE_FRAMEWORK_CAPSULE_DISABLED_4", "TYPE_FRAMEWORK_CAPSULE_5", "TYPE_FRAMEWORK_CAPSULE_DISABLED_5", "TYPE_FRAMEWORK_CAPSULE_6", "TYPE_FRAMEWORK_CAPSULE_DISABLED_6", "TYPE_FRAMEWORK_CAPSULE_7", "TYPE_FRAMEWORK_CAPSULE_8", "TYPE_FRAMEWORK_CAPSULE_9", "TYPE_FRAMEWORK_CAPSULE_10", "TYPE_FRAMEWORK_CAPSULE_11", "TYPE_FRAMEWORK_CAPSULE_12", "TYPE_FRAMEWORK_CAPSULE_13", "TYPE_FRAMEWORK_CAPSULE_DISABLED_13", "TYPE_FRAMEWORK_CIRCLE_1", "TYPE_FRAMEWORK_CIRCLE_DISABLED_1", "TYPE_FRAMEWORK_CIRCLE_2", "TYPE_FRAMEWORK_CIRCLE_DISABLED_2", "TYPE_FRAMEWORK_CIRCLE_3", "TYPE_FRAMEWORK_CIRCLE_4", "TYPE_FRAMEWORK_CIRCLE_5", "TYPE_CONTENT_CAPSULE_1", "TYPE_CONTENT_CAPSULE_DISABLED_1", "TYPE_CONTENT_CAPSULE_2", "TYPE_CONTENT_CAPSULE_DISABLED_2", "TYPE_CONTENT_CAPSULE_3", "TYPE_CONTENT_CAPSULE_DISABLED_3", "TYPE_CONTENT_CAPSULE_4", "TYPE_CONTENT_CAPSULE_DISABLED_4", "TYPE_CONTENT_CAPSULE_5", "TYPE_CONTENT_CAPSULE_DISABLED_5", "TYPE_CONTENT_CAPSULE_6", "TYPE_CONTENT_CAPSULE_DISABLED_6", "TYPE_CONTENT_CAPSULE_7", "TYPE_CONTENT_CAPSULE_DISABLED_7", "TYPE_CONTENT_CAPSULE_8", "TYPE_CONTENT_CAPSULE_DISABLED_8", "coui-support-effect_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public enum StrokeEffectType {
        TYPE_NO_EFFECT,
        TYPE_NO_EFFECT_BASE_1,
        TYPE_NO_EFFECT_BASE_2,
        TYPE_FRAMEWORK_CAPSULE_1,
        TYPE_FRAMEWORK_CAPSULE_DISABLED_1,
        TYPE_FRAMEWORK_CAPSULE_2,
        TYPE_FRAMEWORK_CAPSULE_DISABLED_2,
        TYPE_FRAMEWORK_CAPSULE_3,
        TYPE_FRAMEWORK_CAPSULE_DISABLED_3,
        TYPE_FRAMEWORK_CAPSULE_4,
        TYPE_FRAMEWORK_CAPSULE_DISABLED_4,
        TYPE_FRAMEWORK_CAPSULE_5,
        TYPE_FRAMEWORK_CAPSULE_DISABLED_5,
        TYPE_FRAMEWORK_CAPSULE_6,
        TYPE_FRAMEWORK_CAPSULE_DISABLED_6,
        TYPE_FRAMEWORK_CAPSULE_7,
        TYPE_FRAMEWORK_CAPSULE_8,
        TYPE_FRAMEWORK_CAPSULE_9,
        TYPE_FRAMEWORK_CAPSULE_10,
        TYPE_FRAMEWORK_CAPSULE_11,
        TYPE_FRAMEWORK_CAPSULE_12,
        TYPE_FRAMEWORK_CAPSULE_13,
        TYPE_FRAMEWORK_CAPSULE_DISABLED_13,
        TYPE_FRAMEWORK_CIRCLE_1,
        TYPE_FRAMEWORK_CIRCLE_DISABLED_1,
        TYPE_FRAMEWORK_CIRCLE_2,
        TYPE_FRAMEWORK_CIRCLE_DISABLED_2,
        TYPE_FRAMEWORK_CIRCLE_3,
        TYPE_FRAMEWORK_CIRCLE_4,
        TYPE_FRAMEWORK_CIRCLE_5,
        TYPE_CONTENT_CAPSULE_1,
        TYPE_CONTENT_CAPSULE_DISABLED_1,
        TYPE_CONTENT_CAPSULE_2,
        TYPE_CONTENT_CAPSULE_DISABLED_2,
        TYPE_CONTENT_CAPSULE_3,
        TYPE_CONTENT_CAPSULE_DISABLED_3,
        TYPE_CONTENT_CAPSULE_4,
        TYPE_CONTENT_CAPSULE_DISABLED_4,
        TYPE_CONTENT_CAPSULE_5,
        TYPE_CONTENT_CAPSULE_DISABLED_5,
        TYPE_CONTENT_CAPSULE_6,
        TYPE_CONTENT_CAPSULE_DISABLED_6,
        TYPE_CONTENT_CAPSULE_7,
        TYPE_CONTENT_CAPSULE_DISABLED_7,
        TYPE_CONTENT_CAPSULE_8,
        TYPE_CONTENT_CAPSULE_DISABLED_8;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

        public static EnumEntries<StrokeEffectType> getEntries() {
            return $ENTRIES;
        }
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_1_DARK$cp() {
        return CONTENT_CAPSULE_PARAMS_1_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_1_DISABLED_DARK$cp() {
        return CONTENT_CAPSULE_PARAMS_1_DISABLED_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_1_DISABLED_LIGHT$cp() {
        return CONTENT_CAPSULE_PARAMS_1_DISABLED_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_1_LIGHT$cp() {
        return CONTENT_CAPSULE_PARAMS_1_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_2_DARK$cp() {
        return CONTENT_CAPSULE_PARAMS_2_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_2_DISABLED_DARK$cp() {
        return CONTENT_CAPSULE_PARAMS_2_DISABLED_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_2_DISABLED_LIGHT$cp() {
        return CONTENT_CAPSULE_PARAMS_2_DISABLED_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_2_LIGHT$cp() {
        return CONTENT_CAPSULE_PARAMS_2_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_3_DARK$cp() {
        return CONTENT_CAPSULE_PARAMS_3_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_3_DISABLED_DARK$cp() {
        return CONTENT_CAPSULE_PARAMS_3_DISABLED_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_3_DISABLED_LIGHT$cp() {
        return CONTENT_CAPSULE_PARAMS_3_DISABLED_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_3_LIGHT$cp() {
        return CONTENT_CAPSULE_PARAMS_3_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_4_DARK$cp() {
        return CONTENT_CAPSULE_PARAMS_4_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_4_DISABLED_DARK$cp() {
        return CONTENT_CAPSULE_PARAMS_4_DISABLED_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_4_DISABLED_LIGHT$cp() {
        return CONTENT_CAPSULE_PARAMS_4_DISABLED_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_4_LIGHT$cp() {
        return CONTENT_CAPSULE_PARAMS_4_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_5_DARK$cp() {
        return CONTENT_CAPSULE_PARAMS_5_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_5_DISABLED_DARK$cp() {
        return CONTENT_CAPSULE_PARAMS_5_DISABLED_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_5_DISABLED_LIGHT$cp() {
        return CONTENT_CAPSULE_PARAMS_5_DISABLED_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_5_LIGHT$cp() {
        return CONTENT_CAPSULE_PARAMS_5_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_6_DARK$cp() {
        return CONTENT_CAPSULE_PARAMS_6_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_6_DISABLED_DARK$cp() {
        return CONTENT_CAPSULE_PARAMS_6_DISABLED_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_6_DISABLED_LIGHT$cp() {
        return CONTENT_CAPSULE_PARAMS_6_DISABLED_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_6_LIGHT$cp() {
        return CONTENT_CAPSULE_PARAMS_6_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_7$cp() {
        return CONTENT_CAPSULE_PARAMS_7;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_7_DISABLED$cp() {
        return CONTENT_CAPSULE_PARAMS_7_DISABLED;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_8$cp() {
        return CONTENT_CAPSULE_PARAMS_8;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getCONTENT_CAPSULE_PARAMS_8_DISABLED$cp() {
        return CONTENT_CAPSULE_PARAMS_8_DISABLED;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_10_DARK$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_10_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_10_LIGHT$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_10_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_11_DARK$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_11_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_11_LIGHT$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_11_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_12_DARK$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_12_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_12_LIGHT$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_12_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_13_DARK$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_13_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_13_DISABLED_DARK$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_13_DISABLED_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_13_DISABLED_LIGHT$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_13_DISABLED_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_13_LIGHT$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_13_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_1_DARK$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_1_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_1_DISABLED_DARK$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_1_DISABLED_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_1_DISABLED_LIGHT$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_1_DISABLED_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_1_LIGHT$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_1_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_2_DARK$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_2_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_2_DISABLED_DARK$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_2_DISABLED_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_2_DISABLED_LIGHT$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_2_DISABLED_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_2_LIGHT$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_2_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_3_DARK$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_3_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_3_DISABLED_DARK$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_3_DISABLED_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_3_DISABLED_LIGHT$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_3_DISABLED_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_3_LIGHT$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_3_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_4_DARK$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_4_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_4_DISABLED_DARK$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_4_DISABLED_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_4_DISABLED_LIGHT$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_4_DISABLED_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_4_LIGHT$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_4_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_5_DARK$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_5_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_5_DISABLED_DARK$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_5_DISABLED_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_5_DISABLED_LIGHT$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_5_DISABLED_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_5_LIGHT$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_5_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_6_DARK$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_6_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_6_DISABLED_DARK$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_6_DISABLED_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_6_DISABLED_LIGHT$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_6_DISABLED_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_6_LIGHT$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_6_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_7$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_7;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_8_DARK$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_8_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_8_LIGHT$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_8_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CAPSULE_PARAMS_9$cp() {
        return FRAMEWORK_CAPSULE_PARAMS_9;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CIRCLE_PARAMS_1_DARK$cp() {
        return FRAMEWORK_CIRCLE_PARAMS_1_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CIRCLE_PARAMS_1_DISABLED_DARK$cp() {
        return FRAMEWORK_CIRCLE_PARAMS_1_DISABLED_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CIRCLE_PARAMS_1_DISABLED_LIGHT$cp() {
        return FRAMEWORK_CIRCLE_PARAMS_1_DISABLED_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CIRCLE_PARAMS_1_LIGHT$cp() {
        return FRAMEWORK_CIRCLE_PARAMS_1_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CIRCLE_PARAMS_2_DARK$cp() {
        return FRAMEWORK_CIRCLE_PARAMS_2_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CIRCLE_PARAMS_2_DISABLED_DARK$cp() {
        return FRAMEWORK_CIRCLE_PARAMS_2_DISABLED_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CIRCLE_PARAMS_2_DISABLED_LIGHT$cp() {
        return FRAMEWORK_CIRCLE_PARAMS_2_DISABLED_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CIRCLE_PARAMS_2_LIGHT$cp() {
        return FRAMEWORK_CIRCLE_PARAMS_2_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CIRCLE_PARAMS_3$cp() {
        return FRAMEWORK_CIRCLE_PARAMS_3;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CIRCLE_PARAMS_4_DARK$cp() {
        return FRAMEWORK_CIRCLE_PARAMS_4_DARK;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CIRCLE_PARAMS_4_LIGHT$cp() {
        return FRAMEWORK_CIRCLE_PARAMS_4_LIGHT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getFRAMEWORK_CIRCLE_PARAMS_5$cp() {
        return FRAMEWORK_CIRCLE_PARAMS_5;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getNO_EFFECT$cp() {
        return NO_EFFECT;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getNO_EFFECT_BASE_1$cp() {
        return NO_EFFECT_BASE_1;
    }

    public static final /* synthetic */ Companion.StrokeEffectParams access$getNO_EFFECT_BASE_2$cp() {
        return NO_EFFECT_BASE_2;
    }
}