package com.coui.appcompat.seekbar.shader;

import android.graphics.RuntimeShader;
import com.android.settings.inputmethod.UserDictionaryAddWordContents;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0010\u0014\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u000f\n\u0002\u0010\u000e\n\u0002\b\u0011\n\u0002\u0010\b\n\u0002\b\u000f\b\u0007\u0018\u0000 X2\u00020\u0001:\u0001XB\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010'\u001a\u00020(2\u0006\u0010)\u001a\u00020\fH\u0016J\u0006\u0010*\u001a\u00020\u0004J\u0006\u0010+\u001a\u00020\u0004J\u0006\u0010,\u001a\u00020\u0004J\u0006\u0010-\u001a\u00020\u0004J\u0006\u0010.\u001a\u00020\u0004J\u0006\u0010/\u001a\u00020\u0004J\u0006\u00100\u001a\u00020\u0004J\u0006\u00101\u001a\u00020\u0004J\u0006\u00102\u001a\u00020\u0004J\u0006\u00103\u001a\u00020\u0004J\u0006\u00104\u001a\u00020\u0004J\u0006\u00105\u001a\u00020\u0004J\u000e\u00106\u001a\u00020\u00152\u0006\u00107\u001a\u000208J\u0006\u00109\u001a\u00020\u0004J\u000e\u0010:\u001a\u00020(2\u0006\u0010;\u001a\u00020\u0004J\u000e\u0010<\u001a\u00020(2\u0006\u0010;\u001a\u00020\u0004J\u000e\u0010=\u001a\u00020(2\u0006\u0010;\u001a\u00020\u0004J\u000e\u0010>\u001a\u00020(2\u0006\u0010;\u001a\u00020\u0004J\u000e\u0010?\u001a\u00020(2\u0006\u0010;\u001a\u00020\u0004J\u000e\u0010@\u001a\u00020(2\u0006\u0010;\u001a\u00020\u0004J\u000e\u0010A\u001a\u00020(2\u0006\u0010;\u001a\u00020\u0004J\u001e\u0010B\u001a\u00020(2\u0006\u0010C\u001a\u00020\u00042\u0006\u0010D\u001a\u00020\u00042\u0006\u0010E\u001a\u00020\u0004J\u000e\u0010F\u001a\u00020(2\u0006\u0010;\u001a\u00020\u0004J\u000e\u0010G\u001a\u00020(2\u0006\u0010;\u001a\u00020\u0004J\u000e\u0010H\u001a\u00020(2\u0006\u0010I\u001a\u00020JJ\u000e\u0010K\u001a\u00020(2\u0006\u0010;\u001a\u00020\u0004J\u000e\u0010L\u001a\u00020(2\u0006\u0010;\u001a\u00020\u0004J\u000e\u0010M\u001a\u00020(2\u0006\u0010;\u001a\u00020\u0004J\u0016\u0010N\u001a\u00020(2\u0006\u0010O\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u0004J\u000e\u0010P\u001a\u00020(2\u0006\u0010;\u001a\u00020\u0004J\u000e\u0010Q\u001a\u00020(2\u0006\u0010;\u001a\u00020\u0004J\u000e\u0010R\u001a\u00020(2\u0006\u0010;\u001a\u00020\u0004J\u001e\u0010S\u001a\u00020(2\u0006\u0010C\u001a\u00020\u00042\u0006\u0010D\u001a\u00020\u00042\u0006\u0010E\u001a\u00020\u0004J\u000e\u0010T\u001a\u00020(2\u0006\u0010;\u001a\u00020\u0004J\u0016\u0010U\u001a\u00020(2\u0006\u00107\u001a\u0002082\u0006\u0010;\u001a\u00020\fJ\u0016\u0010U\u001a\u00020(2\u0006\u00107\u001a\u0002082\u0006\u0010;\u001a\u00020\u0004J\u0016\u0010U\u001a\u00020(2\u0006\u00107\u001a\u0002082\u0006\u0010V\u001a\u00020\u0015J\u000e\u0010W\u001a\u00020(2\u0006\u0010;\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u001e\u001a\u00020\u001fX\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u000e\u0010\"\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006Y"}, d2 = {"Lcom/coui/appcompat/seekbar/shader/ConicLightShaderParams;", "Lcom/coui/appcompat/seekbar/shader/BaseShaderInterface;", "()V", "alpha", "", "alpha1", "alpha2", "anti", "axialFalloffValue", "coneKValue", "density", "dirtyAlphaParams", "", "dirtyBasicParams", "dirtyConeParams", "dirtyEndColor", "dirtyMiscParams", "dirtyProgressHeightParams", "dirtyResolution", "dirtyStartColor", "endColor", "", "height", "heightFalloff", "isRTL", "lightBlendMode", "process", "radialFalloffValue", "rectWidth", "resolution", "runtimeShader", "Landroid/graphics/RuntimeShader;", "getRuntimeShader", "()Landroid/graphics/RuntimeShader;", "secondConeK", "spotRadius", "startColor", "threshold", "w0Value", "flushIfDirty", "", "force", "getAlpha1", "getAlpha2", "getAxialFalloff", "getConeK", "getHeight", "getHeightFalloff", "getLightBlendMode", "getProcess", "getRadialFalloff", "getSecondConeK", "getSpotRadius", "getThreshold", "getUniformArray", "name", "", "getW0", "setAlpha", "value", "setAlpha1", "setAlpha2", "setAnti", "setAxialFalloff", "setConeK", "setDensity", "setEndColor", "r", "g", "b", "setHeight", "setHeightFalloff", "setLightBlendMode", UserDictionaryAddWordContents.EXTRA_MODE, "", "setProcess", "setRadialFalloff", "setRectWidth", "setResolution", "width", "setRtl", "setSecondConeK", "setSpotRadius", "setStartColor", "setThreshold", "setUniformByName", "values", "setW0", "Companion", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ConicLightShaderParams implements BaseShaderInterface {
    private static final String AGSL_CODE = "\n         // @Vec2(u_resolution, 1.0) = [800, 600]\n         uniform float2 u_resolution;\n \n         // @Vec3(u_startColor, 0.01) = [1.0, 1.0, 1.0]\n         uniform float3 u_startColor;\n         // @Vec3(u_endColor, 0.01) = [1.0, 1.0, 1.0]\n         uniform float3 u_endColor;\n \n         // Group 1: Progress & Height Parameters\n         // @Vec4(u_progressHeightParams) = [process, rectWidth, height, heightFalloff]\n         uniform float4 u_progressHeightParams;\n \n         // Group 2: Basic Parameters\n         // @Vec4(u_basicParams) = [density, alpha, anti, threshold]\n         uniform float4 u_basicParams;\n \n         // Group 3: Alpha & Spot Parameters\n         // @Vec4(u_alphaParams) = [alpha1, alpha2, w0, spotRadius]\n         uniform float4 u_alphaParams;\n \n         // Group 4: Cone & Falloff Parameters\n         // @Vec4(u_coneParams) = [coneK, second_coneK, axialFalloff, radialFalloff]\n         uniform float4 u_coneParams;\n \n         // Group 5: Misc Parameters\n         // @Vec4(u_miscParams) = [lightBlendMode, is_RTL, reserved, reserved]\n         uniform float4 u_miscParams;\n \n         const float TO_POINT = 0.003921;  // 1.0 / 255.0\n \n         float3 blendPlusLighter(float3 base, float3 blend) {\n             return min(float3(1.0), base + blend);\n         }\n         float4 getBloom(float2 q, float coneK, float alpha1, float2 fragCoord) {\n             // 4. 光轴距离和圆形衰减计算（提前计算共用值）\n             float u_height = u_progressHeightParams.z;\n             float u_heightFalloff = u_progressHeightParams.w;\n             float u_alpha = u_basicParams.y;\n             float w0 = u_alphaParams.z;\n             float u_spotRadius = u_alphaParams.w;\n             float radialFalloff = u_coneParams.w;\n             float axialFalloff = u_coneParams.z;\n             float u_rectWidth = u_progressHeightParams.y;\n             float u_process = u_progressHeightParams.x;\n \n             float xp = max(-q.x, 0.0);\n             float feather = u_spotRadius;\n             float w0_px = w0 * feather;\n \n             // 5. 光锥宽度（去除不必要的 step + mix 分支）\n             float wCone = coneK * xp + w0_px;\n             float w = mix(w0_px, wCone, step(0.0, xp));  // 简化条件分支\n \n             // 6. 边缘遮罩\n             float abs_y = abs(q.y);\n             float d = abs_y - w;\n             float edgeMask = 1.0 - smoothstep(0.0, feather * 2.0, d);\n \n             // 7. 径向衰减（合并除法和 exp）\n             float rNorm = abs_y / (w + 1e-4);\n             float radial = exp(-radialFalloff * rNorm * rNorm);\n \n             // 轴向衰减（基于像素距离，使用 spotRadius 归一化）\n             float xpNorm = xp / feather;  // 归一化到 0-1 范围\n             float axial = exp(-axialFalloff * xpNorm);\n \n             // 8. 高度衰减（预计算公共部分）\n             // float2 lightCenter = float2(u_rectWidth * u_process + feather * 0.5, u_resolution.y * 0.5);   // check it\n             // float circDist = length(fragCoord - lightCenter);\n \n             // float circFactor = circDist / (feather * u_height + 1e-4);\n             // float heightAttenuation = exp(-u_heightFalloff * circFactor * circFactor);\n \n             float maxHeight = feather * u_height;\n             float heightFactor = clamp(xp / (maxHeight + 1e-4), 0.0, 1.0);\n             float heightAttenuation = 1.0 - pow(heightFactor, u_heightFalloff);\n             float cond = step(maxHeight, xp);\n             float overDistance = (xp - maxHeight) / feather * cond;\n             heightAttenuation *= exp(-overDistance * 3.0);\n \n             // 9. 强度计算（合并多次乘法）\n             float intensity = edgeMask * radial * heightAttenuation * axial;\n             intensity *= smoothstep(0.0, 0.03 * feather, xp);\n \n             float3 glowColor = mix(u_startColor, u_endColor, heightFactor) * TO_POINT;\n             return half4(glowColor* intensity, intensity * alpha1 * u_alpha);\n         }\n \n         float max3(float3 v) {\n             return max(max(v.r, v.g), v.b);\n         }\n \n           // 给 BlendMode.PLUS 使用：保留光能量，同时保证 rgb <= alpha\n         float4 premulForAdditiveBlend(float4 c) {\n             c.rgb = clamp(c.rgb, 0.0, 1.0);\n             c.a = clamp(max(c.a, max3(c.rgb)), 0.0, 1.0);\n             return c;\n         }\n \n         half4 main(float2 fragCoord) {\n             // 1. 提前读取所有 uniform，减少重复访问\n             float u_process = u_progressHeightParams.x;\n             float u_rectWidth = u_progressHeightParams.y;\n \n             float u_alpha1 = u_alphaParams.x;\n             float u_alpha2 = u_alphaParams.y;\n \n             float coneK = u_coneParams.x;\n             float second_coneK = u_coneParams.y;\n \n             float u_lightBlendMode = u_miscParams.x;\n             float is_RTL = u_miscParams.y;\n \n             // 2. 计算像素坐标和光源位置\n             float2 pixelPos = fragCoord - u_resolution * 0.5;\n             float spotX = (u_process - 0.5) * u_rectWidth;\n             float2 q = pixelPos - float2(spotX, 0.0);\n \n             // 3. RTL 处理：合并到一次计算\n             q.x *= 1.0 - 2.0 * is_RTL;  // 替代 mix(q.x, -q.x, is_RTL)\n \n \n             float4 blend_color = float4(0.0);\n             float4 bloom_color = getBloom(q, coneK, u_alpha1, fragCoord);\n             // vec4 bloom_color2 = bloom_color;\n             float4 bloom_color2 = getBloom(q, second_coneK, u_alpha2, fragCoord);\n \n             float4 base = bloom_color;\n             float4 blend = bloom_color2;\n             blend_color.rgb = blendPlusLighter(base.rgb, blend.rgb);\n             blend_color.a = base.a * (1.0 - blend.a) + blend.a;\n \n             float percent = abs(blend.a - base.a) / blend_color.a;\n             float mix_bg = step(blend.a, base.a) * percent;\n             float mix_fg = step(base.a, blend.a) * percent;\n             blend_color.rgb = mix(blend_color.rgb, base.rgb, mix_bg);\n             blend_color.rgb = mix(blend_color.rgb, blend.rgb, mix_fg);\n \n             float4 final_color = mix(blend_color, float4(0.0), step(0.0,q.x));\n \n             // premultiplied alpha\n             return premulForAdditiveBlend(final_color);\n         }\n         ";
    private float isRTL;
    private final RuntimeShader runtimeShader = new RuntimeShader(AGSL_CODE);
    private float[] resolution = {800.0f, 600.0f};
    private boolean dirtyResolution = true;
    private float density = 3.0f;
    private float alpha = 0.1f;
    private float anti = 2.0f;
    private float threshold = 0.5f;
    private boolean dirtyBasicParams = true;
    private float process = 0.5f;
    private float rectWidth = 624.0f;
    private float height = 1.0f;
    private float heightFalloff = 1.0f;
    private boolean dirtyProgressHeightParams = true;
    private float alpha1 = 1.0f;
    private float alpha2 = 1.0f;
    private float w0Value = 0.001f;
    private float spotRadius = 600.0f;
    private boolean dirtyAlphaParams = true;
    private float[] startColor = {255.0f, 255.0f, 255.0f};
    private boolean dirtyStartColor = true;
    private float[] endColor = {255.0f, 255.0f, 255.0f};
    private boolean dirtyEndColor = true;
    private float coneKValue = 0.31f;
    private float secondConeK = 0.31f;
    private float axialFalloffValue = 1.05f;
    private float radialFalloffValue = 1.25f;
    private boolean dirtyConeParams = true;
    private float lightBlendMode = 1.0f;
    private boolean dirtyMiscParams = true;

    public ConicLightShaderParams() {
        flushIfDirty(true);
    }

    @Override // com.coui.appcompat.seekbar.shader.BaseShaderInterface
    public void flushIfDirty(boolean force) {
        if (force || this.dirtyResolution) {
            RuntimeShader runtimeShader = getRuntimeShader();
            float[] fArr = this.resolution;
            runtimeShader.setFloatUniform("u_resolution", fArr[0], fArr[1]);
            this.dirtyResolution = false;
        }
        if (force || this.dirtyStartColor) {
            RuntimeShader runtimeShader2 = getRuntimeShader();
            float[] fArr2 = this.startColor;
            runtimeShader2.setFloatUniform("u_startColor", fArr2[0], fArr2[1], fArr2[2]);
            this.dirtyStartColor = false;
        }
        if (force || this.dirtyEndColor) {
            RuntimeShader runtimeShader3 = getRuntimeShader();
            float[] fArr3 = this.endColor;
            runtimeShader3.setFloatUniform("u_endColor", fArr3[0], fArr3[1], fArr3[2]);
            this.dirtyEndColor = false;
        }
        if (force || this.dirtyProgressHeightParams) {
            getRuntimeShader().setFloatUniform("u_progressHeightParams", this.process, this.rectWidth, this.height, this.heightFalloff);
            this.dirtyProgressHeightParams = false;
        }
        if (force || this.dirtyBasicParams) {
            getRuntimeShader().setFloatUniform("u_basicParams", this.density, this.alpha, this.anti, this.threshold);
            this.dirtyBasicParams = false;
        }
        if (force || this.dirtyAlphaParams) {
            getRuntimeShader().setFloatUniform("u_alphaParams", this.alpha1, this.alpha2, this.w0Value, this.spotRadius);
            this.dirtyAlphaParams = false;
        }
        if (force || this.dirtyConeParams) {
            getRuntimeShader().setFloatUniform("u_coneParams", this.coneKValue, this.secondConeK, this.axialFalloffValue, this.radialFalloffValue);
            this.dirtyConeParams = false;
        }
        if (force || this.dirtyMiscParams) {
            getRuntimeShader().setFloatUniform("u_miscParams", this.lightBlendMode, this.isRTL, 0.0f, 0.0f);
            this.dirtyMiscParams = false;
        }
    }

    public final float getAlpha1() {
        return this.alpha1;
    }

    public final float getAlpha2() {
        return this.alpha2;
    }

    /* JADX INFO: renamed from: getAxialFalloff, reason: from getter */
    public final float getAxialFalloffValue() {
        return this.axialFalloffValue;
    }

    /* JADX INFO: renamed from: getConeK, reason: from getter */
    public final float getConeKValue() {
        return this.coneKValue;
    }

    public final float getHeight() {
        return this.height;
    }

    public final float getHeightFalloff() {
        return this.heightFalloff;
    }

    public final float getLightBlendMode() {
        return this.lightBlendMode;
    }

    public final float getProcess() {
        return this.process;
    }

    /* JADX INFO: renamed from: getRadialFalloff, reason: from getter */
    public final float getRadialFalloffValue() {
        return this.radialFalloffValue;
    }

    @Override // com.coui.appcompat.seekbar.shader.BaseShaderInterface
    public RuntimeShader getRuntimeShader() {
        return this.runtimeShader;
    }

    public final float getSecondConeK() {
        return this.secondConeK;
    }

    public final float getSpotRadius() {
        return this.spotRadius;
    }

    public final float getThreshold() {
        return this.threshold;
    }

    public final float[] getUniformArray(String name) {
        Intrinsics.checkNotNullParameter(name, "name");
        int iHashCode = name.hashCode();
        if (iHashCode != 272821202) {
            if (iHashCode != 1706058422) {
                if (iHashCode == 1706938923 && name.equals("u_startColor")) {
                    float[] fArr = this.startColor;
                    float[] fArrCopyOf = Arrays.copyOf(fArr, fArr.length);
                    Intrinsics.checkNotNullExpressionValue(fArrCopyOf, "copyOf(...)");
                    return fArrCopyOf;
                }
            } else if (name.equals("u_resolution")) {
                float[] fArr2 = this.resolution;
                float[] fArrCopyOf2 = Arrays.copyOf(fArr2, fArr2.length);
                Intrinsics.checkNotNullExpressionValue(fArrCopyOf2, "copyOf(...)");
                return fArrCopyOf2;
            }
        } else if (name.equals("u_endColor")) {
            float[] fArr3 = this.endColor;
            float[] fArrCopyOf3 = Arrays.copyOf(fArr3, fArr3.length);
            Intrinsics.checkNotNullExpressionValue(fArrCopyOf3, "copyOf(...)");
            return fArrCopyOf3;
        }
        return new float[0];
    }

    /* JADX INFO: renamed from: getW0, reason: from getter */
    public final float getW0Value() {
        return this.w0Value;
    }

    @Override // com.coui.appcompat.seekbar.shader.BaseShaderInterface
    public boolean isSupported() {
        return BaseShaderInterface.DefaultImpls.isSupported(this);
    }

    public final void setAlpha(float value) {
        this.alpha = RangesKt.coerceIn(value, 0.0f, 1.0f);
        this.dirtyBasicParams = true;
    }

    public final void setAlpha1(float value) {
        this.alpha1 = RangesKt.coerceIn(value, 0.0f, 1.0f);
        this.dirtyAlphaParams = true;
    }

    public final void setAlpha2(float value) {
        this.alpha2 = RangesKt.coerceIn(value, 0.0f, 1.0f);
        this.dirtyAlphaParams = true;
    }

    public final void setAnti(float value) {
        this.anti = RangesKt.coerceIn(value, 0.0f, 10.0f);
        this.dirtyBasicParams = true;
    }

    public final void setAxialFalloff(float value) {
        this.axialFalloffValue = RangesKt.coerceIn(value, 0.0f, 3.0f);
        this.dirtyConeParams = true;
    }

    public final void setConeK(float value) {
        this.coneKValue = RangesKt.coerceIn(value, 0.0f, 1.0f);
        this.dirtyConeParams = true;
    }

    public final void setDensity(float value) {
        this.density = RangesKt.coerceIn(value, 0.0f, 10.0f);
        this.dirtyBasicParams = true;
    }

    public final void setEndColor(float r2, float g3, float b3) {
        this.endColor[0] = RangesKt.coerceIn(r2, 0.0f, 255.0f);
        this.endColor[1] = RangesKt.coerceIn(g3, 0.0f, 255.0f);
        this.endColor[2] = RangesKt.coerceIn(b3, 0.0f, 255.0f);
        this.dirtyEndColor = true;
    }

    public final void setHeight(float value) {
        this.height = RangesKt.coerceIn(value, 0.0f, 2.0f);
        this.dirtyProgressHeightParams = true;
    }

    public final void setHeightFalloff(float value) {
        this.heightFalloff = RangesKt.coerceIn(value, 0.0f, 5.0f);
        this.dirtyProgressHeightParams = true;
    }

    public final void setLightBlendMode(int mode) {
        this.lightBlendMode = RangesKt.coerceIn(mode, 0, 5);
        this.dirtyMiscParams = true;
    }

    public final void setProcess(float value) {
        this.process = value;
        this.dirtyProgressHeightParams = true;
    }

    public final void setRadialFalloff(float value) {
        this.radialFalloffValue = RangesKt.coerceIn(value, 0.0f, 3.0f);
        this.dirtyConeParams = true;
    }

    public final void setRectWidth(float value) {
        this.rectWidth = Math.max(1.0f, value);
        this.dirtyProgressHeightParams = true;
    }

    public final void setResolution(float width, float height) {
        float[] fArr = this.resolution;
        fArr[0] = width;
        fArr[1] = height;
        this.dirtyResolution = true;
    }

    public final void setRtl(float value) {
        this.isRTL = value;
        this.dirtyMiscParams = true;
    }

    public final void setSecondConeK(float value) {
        this.secondConeK = RangesKt.coerceIn(value, 0.0f, 1.0f);
        this.dirtyConeParams = true;
    }

    public final void setSpotRadius(float value) {
        this.spotRadius = RangesKt.coerceIn(value, 10.0f, 2000.0f);
        this.dirtyAlphaParams = true;
    }

    public final void setStartColor(float r2, float g3, float b3) {
        this.startColor[0] = RangesKt.coerceIn(r2, 0.0f, 255.0f);
        this.startColor[1] = RangesKt.coerceIn(g3, 0.0f, 255.0f);
        this.startColor[2] = RangesKt.coerceIn(b3, 0.0f, 255.0f);
        this.dirtyStartColor = true;
    }

    public final void setThreshold(float value) {
        this.threshold = RangesKt.coerceIn(value, 0.0f, 1.0f);
        this.dirtyBasicParams = true;
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public final void setUniformByName(String name, float value) {
        Intrinsics.checkNotNullParameter(name, "name");
        switch (name.hashCode()) {
            case -2132617688:
                if (name.equals("u_lightBlendMode")) {
                    setLightBlendMode((int) value);
                    break;
                }
                break;
            case -1665892701:
                if (name.equals("u_heightFalloff")) {
                    setHeightFalloff(value);
                    break;
                }
                break;
            case -1616265995:
                if (name.equals("radialFalloff")) {
                    setRadialFalloff(value);
                    break;
                }
                break;
            case -1402800354:
                if (name.equals("u_density")) {
                    setDensity(value);
                    break;
                }
                break;
            case -955909731:
                if (name.equals("u_alpha1")) {
                    setAlpha1(value);
                    break;
                }
                break;
            case -955909730:
                if (name.equals("u_alpha2")) {
                    setAlpha2(value);
                    break;
                }
                break;
            case -762179535:
                if (name.equals("u_height")) {
                    setHeight(value);
                    break;
                }
                break;
            case -723572460:
                if (name.equals("u_alpha")) {
                    setAlpha(value);
                    break;
                }
                break;
            case -376523267:
                if (name.equals("second_coneK")) {
                    setSecondConeK(value);
                    break;
                }
                break;
            case -194645442:
                if (name.equals("u_spotRadius")) {
                    setSpotRadius(value);
                    break;
                }
                break;
            case -166912264:
                if (name.equals("u_rectWidth")) {
                    setRectWidth(value);
                    break;
                }
                break;
            case 3737:
                if (name.equals("w0")) {
                    setW0(value);
                    break;
                }
                break;
            case 2045826:
                if (name.equals("Anti")) {
                    setAnti(value);
                    break;
                }
                break;
            case 94844296:
                if (name.equals("coneK")) {
                    setConeK(value);
                    break;
                }
                break;
            case 187616919:
                if (name.equals("axialFalloff")) {
                    setAxialFalloff(value);
                    break;
                }
                break;
            case 1029931173:
                if (name.equals("u_process")) {
                    setProcess(value);
                    break;
                }
                break;
            case 1470664993:
                if (name.equals("u_threshold")) {
                    setThreshold(value);
                    break;
                }
                break;
        }
    }

    public final void setW0(float value) {
        this.w0Value = RangesKt.coerceIn(value, 0.0f, 0.1f);
        this.dirtyAlphaParams = true;
    }

    public final void setUniformByName(String name, boolean value) {
        Intrinsics.checkNotNullParameter(name, "name");
        if (Intrinsics.areEqual(name, "is_RTL")) {
            setRtl(value ? 1.0f : 0.0f);
        }
    }

    public final void setUniformByName(String name, float[] values) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(values, "values");
        int iHashCode = name.hashCode();
        if (iHashCode == 272821202) {
            if (name.equals("u_endColor") && values.length >= 3) {
                setEndColor(values[0], values[1], values[2]);
                return;
            }
            return;
        }
        if (iHashCode != 1706058422) {
            if (iHashCode == 1706938923 && name.equals("u_startColor") && values.length >= 3) {
                setStartColor(values[0], values[1], values[2]);
                return;
            }
            return;
        }
        if (name.equals("u_resolution") && values.length >= 2) {
            setResolution(values[0], values[1]);
        }
    }
}