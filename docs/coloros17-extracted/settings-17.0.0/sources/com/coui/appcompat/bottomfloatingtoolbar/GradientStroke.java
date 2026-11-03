package com.coui.appcompat.bottomfloatingtoolbar;

import android.graphics.RuntimeShader;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b-\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\f\b\u0007\u0018\u0000 W2\u00020\u0001:\u0002WXB\u0005¢\u0006\u0002\u0010\u0002J\b\u0010E\u001a\u00020FH\u0002J\u001e\u0010G\u001a\u00020H2\u0006\u0010I\u001a\u00020\r2\u0006\u0010J\u001a\u00020\u000b2\u0006\u0010K\u001a\u00020\u000bJ\u0016\u0010L\u001a\u00020M2\u0006\u0010N\u001a\u00020\u000b2\u0006\u0010O\u001a\u00020\u000bJ\u0016\u0010P\u001a\u00020M2\u0006\u0010Q\u001a\u00020\u00042\u0006\u0010R\u001a\u00020\u0004J\u0010\u0010S\u001a\u00020M2\u0006\u0010T\u001a\u00020HH\u0002J\b\u0010U\u001a\u00020MH\u0002J\b\u0010V\u001a\u00020MH\u0002R$\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R$\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R$\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0011\"\u0004\b\u0016\u0010\u0013R$\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0011\"\u0004\b\u0019\u0010\u0013R$\u0010\u001a\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u0011\"\u0004\b\u001c\u0010\u0013R$\u0010\u001d\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u0011\"\u0004\b\u001f\u0010\u0013R$\u0010 \u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\u0011\"\u0004\b\"\u0010\u0013R$\u0010#\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b$\u0010\u0011\"\u0004\b%\u0010\u0013R$\u0010&\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b'\u0010\u0011\"\u0004\b(\u0010\u0013R$\u0010)\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b*\u0010\u0011\"\u0004\b+\u0010\u0013R$\u0010,\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b-\u0010\u0011\"\u0004\b.\u0010\u0013R$\u0010/\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b0\u0010\u0011\"\u0004\b1\u0010\u0013R$\u00102\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b3\u0010\u0011\"\u0004\b4\u0010\u0013R\u000e\u00105\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00106\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R$\u00107\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b8\u0010\u0011\"\u0004\b9\u0010\u0013R\u001a\u0010:\u001a\u00020;X\u0086.¢\u0006\u000e\n\u0000\u001a\u0004\b<\u0010=\"\u0004\b>\u0010?R\u000e\u0010@\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010A\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R$\u0010B\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bC\u0010\u0011\"\u0004\bD\u0010\u0013¨\u0006Y"}, d2 = {"Lcom/coui/appcompat/bottomfloatingtoolbar/GradientStroke;", "", "()V", "value", "", TypedValues.Custom.S_COLOR, "getColor", "()I", "setColor", "(I)V", "cornerRadius", "", "cornerType", "Lcom/coui/appcompat/bottomfloatingtoolbar/GradientStroke$CornerType;", "cornerWeight", "farLineAlpha", "getFarLineAlpha", "()F", "setFarLineAlpha", "(F)V", "farLineFadeToSides1", "getFarLineFadeToSides1", "setFarLineFadeToSides1", "farLineFadeToSides2", "getFarLineFadeToSides2", "setFarLineFadeToSides2", "farLineFadeTowardCenter1", "getFarLineFadeTowardCenter1", "setFarLineFadeTowardCenter1", "farLineFadeTowardCenter2", "getFarLineFadeTowardCenter2", "setFarLineFadeTowardCenter2", "farLineWidth", "getFarLineWidth", "setFarLineWidth", "nearLineAlpha", "getNearLineAlpha", "setNearLineAlpha", "nearLineFadeToSides1", "getNearLineFadeToSides1", "setNearLineFadeToSides1", "nearLineFadeToSides2", "getNearLineFadeToSides2", "setNearLineFadeToSides2", "nearLineFadeTowardCenter1", "getNearLineFadeTowardCenter1", "setNearLineFadeTowardCenter1", "nearLineFadeTowardCenter2", "getNearLineFadeTowardCenter2", "setNearLineFadeTowardCenter2", "nearLineWidth", "getNearLineWidth", "setNearLineWidth", "paddingHorizontal", "paddingVertical", "ratio", "getRatio", "setRatio", "shader", "Landroid/graphics/RuntimeShader;", "getShader", "()Landroid/graphics/RuntimeShader;", "setShader", "(Landroid/graphics/RuntimeShader;)V", "sizeHeight", "sizeWidth", "width", "getWidth", "setWidth", "getShaderString", "", "setCorner", "", "type", "radius", "weight", "setPadding", "", "horizontal", "vertical", "setResolution", "viewWidth", "viewHeight", "updateShader", "cornerChange", "updateShaderString", "updateShaderUniform", "Companion", "CornerType", "coui-support-bottomnavigation_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class GradientStroke {
    public static final float DEFAULT_RATIO = 0.68f;
    public static final float DEFAULT_STROKE_CORNER_RADIUS = 200.0f;
    public static final float DEFAULT_STROKE_CORNER_WEIGHT = 1.3f;
    public static final float DEFAULT_STROKE_FAR_LINE_ALPHA = 0.4f;
    public static final float DEFAULT_STROKE_FAR_LINE_FADE_TOWARD_CENTER_1 = 0.08f;
    public static final float DEFAULT_STROKE_FAR_LINE_FADE_TOWARD_CENTER_2 = 0.12f;
    public static final float DEFAULT_STROKE_FAR_LINE_FADE_TO_SIDES_1 = 0.35f;
    public static final float DEFAULT_STROKE_FAR_LINE_FADE_TO_SIDES_2 = 0.3f;
    public static final float DEFAULT_STROKE_FAR_LINE_WIDTH = 3.0f;
    public static final float DEFAULT_STROKE_NEAR_LINE_ALPHA = 0.8f;
    public static final float DEFAULT_STROKE_NEAR_LINE_FADE_TOWARD_CENTER_1 = 0.3f;
    public static final float DEFAULT_STROKE_NEAR_LINE_FADE_TOWARD_CENTER_2 = 0.02f;
    public static final float DEFAULT_STROKE_NEAR_LINE_FADE_TO_SIDES_1 = 0.38f;
    public static final float DEFAULT_STROKE_NEAR_LINE_FADE_TO_SIDES_2 = 0.12f;
    public static final float DEFAULT_STROKE_NEAR_LINE_WIDTH = 3.0f;
    public static final float DEFAULT_STROKE_WIDTH = 50.0f;
    public static final float ONE_POINT_FIVE = 1.5f;
    public static final float POINT_FIVE = 0.5f;
    public static final float WEIGHT_RATE = 2.69f;
    public static final float WEIGHT_RATE1 = 1.023f;
    private float paddingHorizontal;
    private float paddingVertical;
    public RuntimeShader shader;
    private int sizeHeight;
    private int sizeWidth;
    private int color = -1;
    private float ratio = 0.68f;
    private CornerType cornerType = CornerType.SMOOTH;
    private float cornerRadius = 200.0f;
    private float cornerWeight = 1.3f;
    private float width = 50.0f;
    private float nearLineWidth = 3.0f;
    private float nearLineAlpha = 0.8f;
    private float nearLineFadeToSides1 = 0.38f;
    private float nearLineFadeToSides2 = 0.12f;
    private float nearLineFadeTowardCenter1 = 0.3f;
    private float nearLineFadeTowardCenter2 = 0.02f;
    private float farLineWidth = 3.0f;
    private float farLineAlpha = 0.4f;
    private float farLineFadeToSides1 = 0.35f;
    private float farLineFadeToSides2 = 0.3f;
    private float farLineFadeTowardCenter1 = 0.08f;
    private float farLineFadeTowardCenter2 = 0.12f;

    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005¨\u0006\u0006"}, d2 = {"Lcom/coui/appcompat/bottomfloatingtoolbar/GradientStroke$CornerType;", "", "(Ljava/lang/String;I)V", "FULL", "BEZIER", "SMOOTH", "coui-support-bottomnavigation_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public enum CornerType {
        FULL,
        BEZIER,
        SMOOTH;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

        public static EnumEntries<CornerType> getEntries() {
            return $ENTRIES;
        }
    }

    @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[CornerType.values().length];
            try {
                iArr[CornerType.FULL.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[CornerType.BEZIER.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[CornerType.SMOOTH.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public GradientStroke() {
        updateShader(true);
    }

    private final String getShaderString() {
        String str;
        String str2;
        StringBuilder sb = new StringBuilder();
        sb.append("layout(color) uniform half4 u_color;\nuniform float u_width;\nuniform float u_corner;\nuniform float u_weight;\nuniform float u_ratio;\nuniform float2 u_size;\nuniform float2 u_padding;\n// 线段线段（分为Near端与Far端）渐变参数\n// 对角线段宽度\nuniform float u_nearLineWidth;\nuniform float u_farLineWidth;\n// 对角线段最大alpha\nuniform float u_nearLineAlpha;\nuniform float u_farLineAlpha;\n// 对角线段横向渐变比例\nuniform float2 u_nearLineFadeToSides;\nuniform float2 u_farLineFadeToSides;\n// 对角线段纵向渐变比例\nuniform float2 u_nearLineFadeTowardsCenter;\nuniform float2 u_farLineFadeTowardsCenter;\nconst float PI = 2.0 * asin(1.0);");
        sb.append("float RBox(float2 p,float2 b,float r){\n    float2 q=abs(p)-b+r;\n    return min(max(q.x,q.y),0.)+length(max(q,0.))-r;\n}");
        CornerType cornerType = this.cornerType;
        int[] iArr = WhenMappings.$EnumSwitchMapping$0;
        int i3 = iArr[cornerType.ordinal()];
        if (i3 == 1) {
            str = "";
        } else if (i3 == 2) {
            str = "float cubicBezierDistance(vec2 p, vec2 p0, vec2 p1, vec2 p2, vec2 p3) {\n    // 将贝塞尔转换为幂基形式：B(t) = c0 + c1 t + c2 t^2 + c3 t^3\n    vec2 c0 = p0;\n    vec2 c1 = 3.0 * (p1 - p0);\n    vec2 c2 = 3.0 * (p2 - 2.0 * p1 + p0);\n    vec2 c3 = p3 - 3.0 * p2 + 3.0 * p1 - p0;\n\n    float t = 0.5; // 初始值\n    for (int i = 0; i < 10; i++) {\n        vec2 Bt   = ((c3 * t + c2) * t + c1) * t + c0;\n        vec2 dBt  = (3.0 * c3 * t + 2.0 * c2) * t + c1;\n        vec2 ddBt = 6.0 * c3 * t + 2.0 * c2;\n        vec2 r    = Bt - p;\n        // E(t) = |B(t)-p|^2 的一阶、二阶导\n        float f1 = dot(dBt, r);\n        float f2 = dot(ddBt, r) + dot(dBt, dBt);\n        // 避免除零\n        float denom = max(f2, 1e-4);\n        t = clamp(t - f1 / denom, 0.0, 1.0);\n    }\n    vec2 Bt = ((c3 * t + c2) * t + c1) * t + c0;\n    vec2 re = Bt - p;\n\n    // 计算曲线在最近点处的切线向量\n    vec2 tangent = (3.0 * c3 * t + 2.0 * c2) * t + c1;\n    \n    // 计算法向量（切线逆时针旋转90度）\n    vec2 normal = vec2(-tangent.y, tangent.x);\n    \n    // 归一化法向量\n    vec2 normalizedNormal = normalize(normal);\n    \n    // 计算有符号距离\n    float dis = length(re);\n    float signedDistance = dot(re, normalizedNormal) > 0.0 ? dis : -dis;\n\n    return signedDistance ;\n}\nfloat sdBezierDistance(vec2 p, vec2 b, float cc) {\n    float sdf = 0.0;\n    if(any(lessThanEqual(abs(p), b - cc))) {\n        sdf = RBox(p, b, cc);\n    }else{\n        float point  = max(cc*u_weight*0.5,0.001);\n        sdf = cubicBezierDistance(abs(p)-b+cc,vec2(cc, 0.0), vec2(cc, point), vec2(point, cc),vec2(0.0, cc));\n    }\n    return sdf;\n}";
        } else {
            if (i3 != 3) {
                throw new NoWhenBranchMatchedException();
            }
            str = "float approx_sdSquircle(vec2 p, float n){\n    // symmetries\n    p = abs(p); // if( p.y>p.x ) p=p.yx;\n    float w = pow(p.x,n) + pow(p.y,n);\n\n    // linearlized implicit (taylor)\n    float b = 2.0*n-2.0;\n    float a = 1.0-1.0/n;\n    return (w-pow(w,a)) * inversesqrt(pow(p.x,b)+pow(p.y,b));\n}\n\nfloat opSmoothIntersectionOne(float d1,float d2){\n    float h=clamp(.5-.5*(d2-d1),0.,1.);\n    return mix(d2,d1,h) + h*(1.-h);\n}\nvec2 getPreCalcMA() {\n    float halfSizeX = u_size.x * 0.5;\n    float halfSizeY = u_size.y * 0.5;\n    float maX = 0.0;\n    float maY = 0.0;\n    if (halfSizeX > halfSizeY) {\n        maX = halfSizeX;\n        maY = halfSizeY * 0.5;\n    } else {\n        maX = halfSizeX * 0.5;\n        maY = halfSizeY;\n    }\n    return vec2(maX, maY);\n}\n\nvec2 sdRoundedBoxMerge(vec2 p,  vec2 b,  float cr){\n    vec2 q1 = abs(p)-b;\n    float r1 = min(max(q1.x,q1.y),0.0) + length(max(q1,0.0));\n    vec2 q2 = q1 + cr;\n    float r2 = min(max(q2.x,q2.y),0.0) + length(max(q2,0.0)) - cr;\n    float h=clamp(.5-.5*(r2-r1),0.,1.);\n    return vec2(mix(r2,r1,h)+h*(1.-h), r2);\n}\n\nfloat sdSquircle(vec2 p, in vec2 b, float cc) {\n    float sdf = 0.0;\n    float sc =  cc + u_weight * cc;\n    float rbRes = 0.0;\n    if(any(lessThan(abs(p), b - sc))) {\n        vec2 res = sdRoundedBoxMerge(p, b, cc);\n        sdf = res.x;\n        rbRes = res.y;\n    } else {\n        vec2 sp = (abs(p) - b + sc) / sc;\n        float angle = abs(atan(sp.y, sp.x) - 0.785398); // 0.785398 for 45 degree\n        float preCalcN1Delta = 3.02 + u_weight * 2.44;\n        float preCalcN2Delta = 2.05 + u_weight * 2.44;\n        float sn = mix(preCalcN1Delta, preCalcN2Delta, smoothstep(22.0, 0.0, degrees(angle)));\n        sdf = approx_sdSquircle(sp, sn) * sc;\n        rbRes = RBox(p, b, cc);\n        sdf = opSmoothIntersectionOne(sdf, rbRes);\n    }\n\n    if((u_corner) == min(b.x, b.y)) {\n        float sdfRect = RBox(p, getPreCalcMA(), 0);\n        sdf = min(max(sdfRect, rbRes), sdf);\n    }\n\n    // return smoothstep(0.0, -antiAliasing, sdf);\n    return sdf;\n}";
        }
        sb.append(str);
        sb.append("float getDis(float2 pos,float2 halfsize){\n    float2 rect=abs(pos-halfsize);\n    return rect.x+rect.y;\n}\nfloat3 getLineFadeTowardCenter(float2 u_lineFadeTowardsCenter, float lineWidth) {\n    float x = clamp(u_lineFadeTowardsCenter.x, 0.0, 1.0);\n    float y = clamp(u_lineFadeTowardsCenter.y, 0.0, 1.0 - x);\n    float z = clamp((1.0 - x - y), 0.01, 1.0);\n    float3 lineFadeTowardsCenter = lineWidth * float3(x, y, z);\n    lineFadeTowardsCenter.y += lineFadeTowardsCenter.x;\n    lineFadeTowardsCenter.z += lineFadeTowardsCenter.y;\n    lineFadeTowardsCenter *= -1.;\n    return lineFadeTowardsCenter;\n}\n\nhalf4 gradientStrokeColorWithRatio(float2 fragCoord) {\n    float ratio = u_ratio;\n\n    float2 rectSize = u_size - u_padding * 2.0;\n    float2 halfSize = rectSize / 2.0;\n    float2 pos = fragCoord - u_size / 2.0;\n    float2 dir_pos= normalize(pos);\n    float2 dirAbs_pos = abs(dir_pos);\n    float2 pos_n = min(float2(dirAbs_pos.xy*halfSize.yx/max(vec2(.001),dirAbs_pos.yx)),halfSize)*sign(dir_pos);\n\n    float2 halfSize_symobol = float2(-halfSize.y,halfSize.x);\n    float flag = mix(-1.0,1.0, step(ratio,0.5))*dot(halfSize_symobol,pos_n);\n\n    float distanceAll= rectSize.x + rectSize.y;\n    float dis_point = distanceAll*min(ratio,1.0-ratio)*2.0;\n    float dis_pos = getDis(pos_n,halfSize);\n\n    float distance_near = mix(dis_point+dis_pos,abs(dis_point-dis_pos),step(0.,flag));\n\n    distance_near=min(2.*distanceAll-distance_near,distance_near);\n\n    float2 nearLineFade = float2(u_nearLineFadeToSides.x, u_nearLineFadeToSides.x + u_nearLineFadeToSides.y);\n    float2 farLineFade = float2(u_farLineFadeToSides.x, u_farLineFadeToSides.x + u_farLineFadeToSides.y);\n    float lineNearAlpha = u_nearLineAlpha*smoothstep(nearLineFade.y*distanceAll, nearLineFade.x*distanceAll, distance_near);\n    float lineFarAlpha = u_farLineAlpha*smoothstep(farLineFade.y*distanceAll, farLineFade.x*distanceAll, distanceAll-distance_near);\n\n    float3 farLineFadeTowardsCenter = getLineFadeTowardCenter(u_farLineFadeTowardsCenter, u_farLineWidth);\n    float3 nearLineFadeTowardsCenter = getLineFadeTowardCenter(u_nearLineFadeTowardsCenter, u_nearLineWidth);\n\n    float corner = min(u_corner, min(halfSize.x, halfSize.y));");
        int i4 = iArr[this.cornerType.ordinal()];
        if (i4 == 1) {
            str2 = "float rectSdf = RBox(pos, halfSize, corner);";
        } else if (i4 == 2) {
            str2 = "float rectSdf= sdBezierDistance(pos, halfSize, corner);";
        } else {
            if (i4 != 3) {
                throw new NoWhenBranchMatchedException();
            }
            str2 = "float rectSdf= sdSquircle(pos,halfSize, corner);";
        }
        sb.append(str2);
        sb.append(GradientStrokeKt.GRADIENT_STROKE_SHADER_STRING_METHOD_PART_3);
        String string = sb.toString();
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    private final void updateShader(boolean cornerChange) {
        if (cornerChange) {
            updateShaderString();
        }
        updateShaderUniform();
    }

    private final void updateShaderString() {
        setShader(new RuntimeShader(getShaderString()));
    }

    private final void updateShaderUniform() {
        RuntimeShader shader = getShader();
        shader.setColorUniform("u_color", this.color);
        shader.setFloatUniform("u_width", this.width * 0.5f);
        shader.setFloatUniform("u_corner", this.cornerRadius);
        shader.setFloatUniform("u_weight", this.cornerWeight);
        shader.setFloatUniform("u_ratio", this.ratio);
        shader.setFloatUniform("u_size", this.sizeWidth, this.sizeHeight);
        shader.setFloatUniform("u_padding", this.paddingHorizontal, this.paddingVertical);
        shader.setFloatUniform("u_farLineWidth", this.farLineWidth);
        shader.setFloatUniform("u_nearLineWidth", this.nearLineWidth);
        shader.setFloatUniform("u_farLineAlpha", this.farLineAlpha);
        shader.setFloatUniform("u_nearLineAlpha", this.nearLineAlpha);
        shader.setFloatUniform("u_farLineFadeToSides", this.farLineFadeToSides1, this.farLineFadeToSides2);
        shader.setFloatUniform("u_nearLineFadeToSides", this.nearLineFadeToSides1, this.nearLineFadeToSides2);
        shader.setFloatUniform("u_farLineFadeTowardsCenter", this.farLineFadeTowardCenter1, this.farLineFadeTowardCenter2);
        shader.setFloatUniform("u_nearLineFadeTowardsCenter", this.nearLineFadeTowardCenter1, this.nearLineFadeTowardCenter2);
    }

    public final int getColor() {
        return this.color;
    }

    public final float getFarLineAlpha() {
        return this.farLineAlpha;
    }

    public final float getFarLineFadeToSides1() {
        return this.farLineFadeToSides1;
    }

    public final float getFarLineFadeToSides2() {
        return this.farLineFadeToSides2;
    }

    public final float getFarLineFadeTowardCenter1() {
        return this.farLineFadeTowardCenter1;
    }

    public final float getFarLineFadeTowardCenter2() {
        return this.farLineFadeTowardCenter2;
    }

    public final float getFarLineWidth() {
        return this.farLineWidth;
    }

    public final float getNearLineAlpha() {
        return this.nearLineAlpha;
    }

    public final float getNearLineFadeToSides1() {
        return this.nearLineFadeToSides1;
    }

    public final float getNearLineFadeToSides2() {
        return this.nearLineFadeToSides2;
    }

    public final float getNearLineFadeTowardCenter1() {
        return this.nearLineFadeTowardCenter1;
    }

    public final float getNearLineFadeTowardCenter2() {
        return this.nearLineFadeTowardCenter2;
    }

    public final float getNearLineWidth() {
        return this.nearLineWidth;
    }

    public final float getRatio() {
        return this.ratio;
    }

    public final RuntimeShader getShader() {
        RuntimeShader runtimeShader = this.shader;
        if (runtimeShader != null) {
            return runtimeShader;
        }
        Intrinsics.throwUninitializedPropertyAccessException("shader");
        return null;
    }

    public final float getWidth() {
        return this.width;
    }

    public final void setColor(int i3) {
        if (this.color != i3) {
            this.color = i3;
            getShader().setColorUniform("u_color", i3);
        }
    }

    public final boolean setCorner(CornerType type, float radius, float weight) {
        Intrinsics.checkNotNullParameter(type, "type");
        CornerType cornerType = this.cornerType;
        if (cornerType == type && this.cornerRadius == radius && this.cornerWeight == weight) {
            return false;
        }
        CornerType cornerType2 = CornerType.BEZIER;
        if (type == cornerType2) {
            radius *= 1.5f;
        }
        this.cornerRadius = radius;
        if (type == cornerType2) {
            weight = (2.69f * weight) / (weight + 1.023f);
        }
        this.cornerWeight = weight;
        if (cornerType != type) {
            this.cornerType = type;
            updateShader(true);
            return true;
        }
        getShader().setFloatUniform("u_corner", this.cornerRadius);
        getShader().setFloatUniform("u_weight", this.cornerWeight);
        return false;
    }

    public final void setFarLineAlpha(float f3) {
        if (this.farLineAlpha == f3) {
            return;
        }
        this.farLineAlpha = f3;
        getShader().setFloatUniform("u_farLineAlpha", f3);
    }

    public final void setFarLineFadeToSides1(float f3) {
        if (this.farLineFadeToSides1 == f3) {
            return;
        }
        this.farLineFadeToSides1 = f3;
        getShader().setFloatUniform("u_farLineFadeToSides", f3, this.farLineFadeToSides2);
    }

    public final void setFarLineFadeToSides2(float f3) {
        if (this.farLineFadeToSides2 == f3) {
            return;
        }
        this.farLineFadeToSides2 = f3;
        getShader().setFloatUniform("u_farLineFadeToSides", this.farLineFadeToSides1, f3);
    }

    public final void setFarLineFadeTowardCenter1(float f3) {
        float fMax = Math.max(0.0f, Math.min(f3, 1.0f));
        if (this.farLineFadeTowardCenter1 == fMax) {
            return;
        }
        this.farLineFadeTowardCenter1 = fMax;
        getShader().setFloatUniform("u_farLineFadeTowardsCenter", fMax, this.farLineFadeTowardCenter2);
    }

    public final void setFarLineFadeTowardCenter2(float f3) {
        float fMax = Math.max(0.0f, Math.min(f3, 1.0f - this.farLineFadeTowardCenter1));
        if (this.farLineFadeTowardCenter2 == fMax) {
            return;
        }
        this.farLineFadeTowardCenter2 = fMax;
        getShader().setFloatUniform("u_farLineFadeTowardsCenter", this.farLineFadeTowardCenter1, f3);
    }

    public final void setFarLineWidth(float f3) {
        if (this.farLineWidth == f3) {
            return;
        }
        this.farLineWidth = f3;
        getShader().setFloatUniform("u_farLineWidth", f3);
    }

    public final void setNearLineAlpha(float f3) {
        if (this.nearLineAlpha == f3) {
            return;
        }
        this.nearLineAlpha = f3;
        getShader().setFloatUniform("u_nearLineAlpha", f3);
    }

    public final void setNearLineFadeToSides1(float f3) {
        if (this.nearLineFadeToSides1 == f3) {
            return;
        }
        this.nearLineFadeToSides1 = f3;
        getShader().setFloatUniform("u_nearLineFadeToSides", f3, this.nearLineFadeToSides2);
    }

    public final void setNearLineFadeToSides2(float f3) {
        if (this.nearLineFadeToSides2 == f3) {
            return;
        }
        this.nearLineFadeToSides2 = f3;
        getShader().setFloatUniform("u_nearLineFadeToSides", this.nearLineFadeToSides1, f3);
    }

    public final void setNearLineFadeTowardCenter1(float f3) {
        float fMax = Math.max(0.0f, Math.min(f3, 1.0f));
        if (this.nearLineFadeTowardCenter1 == fMax) {
            return;
        }
        this.nearLineFadeTowardCenter1 = fMax;
        getShader().setFloatUniform("u_nearLineFadeTowardsCenter", fMax, this.nearLineFadeTowardCenter2);
    }

    public final void setNearLineFadeTowardCenter2(float f3) {
        float fMax = Math.max(0.0f, Math.min(f3, 1.0f - this.nearLineFadeTowardCenter1));
        if (this.nearLineFadeTowardCenter2 == fMax) {
            return;
        }
        this.nearLineFadeTowardCenter2 = fMax;
        getShader().setFloatUniform("u_nearLineFadeTowardsCenter", this.nearLineFadeTowardCenter1, f3);
    }

    public final void setNearLineWidth(float f3) {
        if (this.nearLineWidth == f3) {
            return;
        }
        this.nearLineWidth = f3;
        getShader().setFloatUniform("u_nearLineWidth", f3);
    }

    public final void setPadding(float horizontal, float vertical) {
        if (this.paddingHorizontal == horizontal && this.paddingVertical == vertical) {
            return;
        }
        this.paddingHorizontal = horizontal;
        this.paddingVertical = vertical;
        getShader().setFloatUniform("u_padding", this.paddingHorizontal, this.paddingVertical);
    }

    public final void setRatio(float f3) {
        if (this.ratio == f3) {
            return;
        }
        this.ratio = f3;
        getShader().setFloatUniform("u_ratio", f3);
    }

    public final void setResolution(int viewWidth, int viewHeight) {
        if (this.sizeWidth == viewWidth && this.sizeHeight == viewHeight) {
            return;
        }
        this.sizeWidth = viewWidth;
        this.sizeHeight = viewHeight;
        getShader().setFloatUniform("u_size", this.sizeWidth, this.sizeHeight);
    }

    public final void setShader(RuntimeShader runtimeShader) {
        Intrinsics.checkNotNullParameter(runtimeShader, "<set-?>");
        this.shader = runtimeShader;
    }

    public final void setWidth(float f3) {
        if (this.width == f3) {
            return;
        }
        this.width = f3;
        getShader().setFloatUniform("u_width", f3 * 0.5f);
    }
}