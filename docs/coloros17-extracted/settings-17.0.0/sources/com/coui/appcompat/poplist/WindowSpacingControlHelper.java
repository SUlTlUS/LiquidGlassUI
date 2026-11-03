package com.coui.appcompat.poplist;

import android.util.Log;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0015\n\u0002\b\t\u0018\u0000 )2\u00020\u0001:\u0002()B\u0005¢\u0006\u0002\u0010\u0002J\u0016\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\tJ\u000e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0012\u001a\u00020\tJ\u0018\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0012\u001a\u00020\tH\u0002J\u0016\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\tJ\u0016\u0010\u001a\u001a\u00020\n2\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0012\u001a\u00020\tJ\u000e\u0010\u001a\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\tJ\u000e\u0010\u001b\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\u0017J'\u0010\u001c\u001a\u00020\n2\b\u0010\u0011\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0012\u001a\u00020\tH\u0002¢\u0006\u0002\u0010\u001dJ0\u0010\u001e\u001a\u00020\n2\u0006\u0010\u001f\u001a\u00020\u00172\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020!2\u0006\u0010\u0012\u001a\u00020\tH\u0002J\u000e\u0010#\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\tJ\u0006\u0010$\u001a\u00020\u0014J \u0010%\u001a\u00020\u00102\u0006\u0010&\u001a\u00020!2\u0006\u0010'\u001a\u00020!2\u0006\u0010\u0016\u001a\u00020\u0017H\u0007R\u0014\u0010\u0003\u001a\u00020\u0004X\u0086D¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R&\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e¨\u0006*"}, d2 = {"Lcom/coui/appcompat/poplist/WindowSpacingControlHelper;", "", "()V", "TAG", "", "getTAG", "()Ljava/lang/String;", "marginMap", "", "Lcom/coui/appcompat/poplist/WindowSpacingControlHelper$AnchorViewTypeEnum;", "", "getMarginMap", "()Ljava/util/Map;", "setMarginMap", "(Ljava/util/Map;)V", "addAnchorViewSpacingMap", "", "margin", "enumType", "checkInMarginMap", "", "checkViewFromEnumType", "anchorView", "Landroid/view/View;", "getAnchorViewParentView", "viewTypeEnum", "getAnchorViewSpacing", "getAnchorViewTypeEnum", "getEnumViewSpacing", "(Ljava/lang/Integer;Landroid/view/View;Lcom/coui/appcompat/poplist/WindowSpacingControlHelper$AnchorViewTypeEnum;)I", "getOrientationSpacing", "parentView", "parentLocation", "", "anchorViewLocation", "getOrientationValue", "isUtilMapInit", "setOriginCenterPoint", "anchorViewLocationInScreen", "resultOriginCenterPoint", "AnchorViewTypeEnum", "Companion", "coui-support-poplist_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class WindowSpacingControlHelper {
    private final String TAG = "WindowSpacingControlHelper";
    private Map<AnchorViewTypeEnum, Integer> marginMap = new HashMap();

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final int ORIENTATION_VERTICAL_TOP = 1;
    private static final int ORIENTATION_VERTICAL_BOTTOM = 2;

    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\t\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\t¨\u0006\n"}, d2 = {"Lcom/coui/appcompat/poplist/WindowSpacingControlHelper$AnchorViewTypeEnum;", "", "(Ljava/lang/String;I)V", "NORMAL", "TOOLBAR", "NAVIGATION", "START", "END", "TOP", "BOTTOM", "coui-support-poplist_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public enum AnchorViewTypeEnum {
        NORMAL,
        TOOLBAR,
        NAVIGATION,
        START,
        END,
        TOP,
        BOTTOM;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

        public static EnumEntries<AnchorViewTypeEnum> getEntries() {
            return $ENTRIES;
        }
    }

    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u001c\u0010\u0003\u001a\u00020\u00048\u0006X\u0087D¢\u0006\u000e\n\u0000\u0012\u0004\b\u0005\u0010\u0002\u001a\u0004\b\u0006\u0010\u0007R\u001c\u0010\b\u001a\u00020\u00048\u0006X\u0087D¢\u0006\u000e\n\u0000\u0012\u0004\b\t\u0010\u0002\u001a\u0004\b\n\u0010\u0007¨\u0006\u000b"}, d2 = {"Lcom/coui/appcompat/poplist/WindowSpacingControlHelper$Companion;", "", "()V", "ORIENTATION_VERTICAL_BOTTOM", "", "getORIENTATION_VERTICAL_BOTTOM$annotations", "getORIENTATION_VERTICAL_BOTTOM", "()I", "ORIENTATION_VERTICAL_TOP", "getORIENTATION_VERTICAL_TOP$annotations", "getORIENTATION_VERTICAL_TOP", "coui-support-poplist_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @JvmStatic
        public static /* synthetic */ void getORIENTATION_VERTICAL_BOTTOM$annotations() {
        }

        @JvmStatic
        public static /* synthetic */ void getORIENTATION_VERTICAL_TOP$annotations() {
        }

        public final int getORIENTATION_VERTICAL_BOTTOM() {
            return WindowSpacingControlHelper.access$getORIENTATION_VERTICAL_BOTTOM$cp();
        }

        public final int getORIENTATION_VERTICAL_TOP() {
            return WindowSpacingControlHelper.access$getORIENTATION_VERTICAL_TOP$cp();
        }

        private Companion() {
        }
    }

    @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[AnchorViewTypeEnum.values().length];
            try {
                iArr[AnchorViewTypeEnum.TOOLBAR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[AnchorViewTypeEnum.NAVIGATION.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public static final /* synthetic */ int access$getORIENTATION_VERTICAL_BOTTOM$cp() {
        return ORIENTATION_VERTICAL_BOTTOM;
    }

    public static final /* synthetic */ int access$getORIENTATION_VERTICAL_TOP$cp() {
        return ORIENTATION_VERTICAL_TOP;
    }

    private final boolean checkViewFromEnumType(View anchorView, AnchorViewTypeEnum enumType) {
        int i3 = WhenMappings.$EnumSwitchMapping$0[enumType.ordinal()];
        if (i3 == 1) {
            return anchorView instanceof Toolbar;
        }
        if (i3 != 2) {
            return false;
        }
        return anchorView instanceof BottomNavigationView;
    }

    private final int getEnumViewSpacing(Integer margin, View anchorView, AnchorViewTypeEnum enumType) {
        View view = anchorView;
        while (view != null) {
            if (this.checkViewFromEnumType(view, enumType)) {
                int[] iArr = new int[2];
                anchorView.getLocationInWindow(iArr);
                int[] iArr2 = new int[2];
                view.getLocationInWindow(iArr2);
                Integer numValueOf = margin != null ? Integer.valueOf(margin.intValue() + this.getOrientationSpacing(view, anchorView, iArr2, iArr, enumType)) : null;
                if (numValueOf != null) {
                    return numValueOf.intValue();
                }
                return 0;
            }
            WindowSpacingControlHelper windowSpacingControlHelper = this;
            View view2 = anchorView;
            AnchorViewTypeEnum anchorViewTypeEnum = enumType;
            if (!(view.getParent() instanceof View)) {
                Log.e(WindowSpacingControlHelper.class.getName(), "getToolbarViewSpacing  tempView " + view.getClass().getName());
                if (margin != null) {
                    return margin.intValue();
                }
                return 0;
            }
            Object parent = view.getParent();
            Intrinsics.checkNotNull(parent, "null cannot be cast to non-null type android.view.View");
            view = (View) parent;
            this = windowSpacingControlHelper;
            anchorView = view2;
            enumType = anchorViewTypeEnum;
        }
        if (margin != null) {
            return margin.intValue();
        }
        return 0;
    }

    public static final int getORIENTATION_VERTICAL_BOTTOM() {
        return INSTANCE.getORIENTATION_VERTICAL_BOTTOM();
    }

    public static final int getORIENTATION_VERTICAL_TOP() {
        return INSTANCE.getORIENTATION_VERTICAL_TOP();
    }

    private final int getOrientationSpacing(View parentView, View anchorView, int[] parentLocation, int[] anchorViewLocation, AnchorViewTypeEnum enumType) {
        int height;
        int height2;
        int orientationValue = getOrientationValue(enumType);
        if (orientationValue == ORIENTATION_VERTICAL_TOP) {
            height = parentLocation[1];
            height2 = anchorViewLocation[1];
        } else {
            if (orientationValue != ORIENTATION_VERTICAL_BOTTOM) {
                return 0;
            }
            height = parentLocation[1] + parentView.getHeight();
            height2 = anchorViewLocation[1] + anchorView.getHeight();
        }
        return height - height2;
    }

    public final void addAnchorViewSpacingMap(int margin, AnchorViewTypeEnum enumType) {
        Intrinsics.checkNotNullParameter(enumType, "enumType");
        Map<AnchorViewTypeEnum, Integer> map = this.marginMap;
        if (map != null) {
            map.put(enumType, Integer.valueOf(margin));
        }
    }

    public final boolean checkInMarginMap(AnchorViewTypeEnum enumType) {
        Intrinsics.checkNotNullParameter(enumType, "enumType");
        if (this.marginMap.isEmpty()) {
            return false;
        }
        Iterator<AnchorViewTypeEnum> it = this.marginMap.keySet().iterator();
        while (it.hasNext()) {
            if (it.next() == enumType) {
                return true;
            }
        }
        return false;
    }

    public final View getAnchorViewParentView(View anchorView, AnchorViewTypeEnum viewTypeEnum) {
        Intrinsics.checkNotNullParameter(anchorView, "anchorView");
        Intrinsics.checkNotNullParameter(viewTypeEnum, "viewTypeEnum");
        while (!checkViewFromEnumType(anchorView, viewTypeEnum)) {
            if (!(anchorView.getParent() instanceof View)) {
                Log.e(WindowSpacingControlHelper.class.getName(), "getAnchorViewParentView  tempView " + anchorView.getClass().getName());
                return anchorView;
            }
            Object parent = anchorView.getParent();
            Intrinsics.checkNotNull(parent, "null cannot be cast to non-null type android.view.View");
            anchorView = (View) parent;
        }
        return anchorView;
    }

    public final int getAnchorViewSpacing(AnchorViewTypeEnum enumType) {
        Integer num;
        Intrinsics.checkNotNullParameter(enumType, "enumType");
        if (this.marginMap.isEmpty() || this.marginMap.get(enumType) == null || (num = this.marginMap.get(enumType)) == null) {
            return 0;
        }
        return num.intValue();
    }

    public final AnchorViewTypeEnum getAnchorViewTypeEnum(View anchorView) {
        Intrinsics.checkNotNullParameter(anchorView, "anchorView");
        while (!(anchorView instanceof Toolbar)) {
            if (anchorView instanceof BottomNavigationView) {
                return AnchorViewTypeEnum.NAVIGATION;
            }
            if (!(anchorView.getParent() instanceof View)) {
                Log.e(WindowSpacingControlHelper.class.getName(), "getAnchorViewTypeEnum  tempView " + anchorView.getClass().getName());
                return AnchorViewTypeEnum.NORMAL;
            }
            Object parent = anchorView.getParent();
            Intrinsics.checkNotNull(parent, "null cannot be cast to non-null type android.view.View");
            anchorView = (View) parent;
        }
        return AnchorViewTypeEnum.TOOLBAR;
    }

    public final Map<AnchorViewTypeEnum, Integer> getMarginMap() {
        return this.marginMap;
    }

    public final int getOrientationValue(AnchorViewTypeEnum enumType) {
        Intrinsics.checkNotNullParameter(enumType, "enumType");
        int i3 = WhenMappings.$EnumSwitchMapping$0[enumType.ordinal()];
        if (i3 != 1) {
            return i3 != 2 ? ORIENTATION_VERTICAL_TOP : ORIENTATION_VERTICAL_TOP;
        }
        return ORIENTATION_VERTICAL_BOTTOM;
    }

    public final String getTAG() {
        return this.TAG;
    }

    public final boolean isUtilMapInit() {
        return !this.marginMap.isEmpty();
    }

    public final void setMarginMap(Map<AnchorViewTypeEnum, Integer> map) {
        Intrinsics.checkNotNullParameter(map, "<set-?>");
        this.marginMap = map;
    }

    public final void setOriginCenterPoint(int[] anchorViewLocationInScreen, int[] resultOriginCenterPoint, View anchorView) {
        Intrinsics.checkNotNullParameter(anchorViewLocationInScreen, "anchorViewLocationInScreen");
        Intrinsics.checkNotNullParameter(resultOriginCenterPoint, "resultOriginCenterPoint");
        Intrinsics.checkNotNullParameter(anchorView, "anchorView");
        if (anchorView.getWidth() <= 0 || anchorView.getHeight() <= 0) {
            Log.e(this.TAG, "setOriginCenterPoint anchorView.width <= 0 or anchorView.height <= 0");
            resultOriginCenterPoint[0] = (int) anchorView.getPivotX();
            resultOriginCenterPoint[1] = (int) anchorView.getPivotY();
        }
        float pivotX = anchorView.getPivotX() / anchorView.getWidth();
        float pivotY = anchorView.getPivotY() / anchorView.getHeight();
        float f3 = 2;
        float scaleX = anchorViewLocationInScreen[0] + ((anchorView.getScaleX() * anchorView.getWidth()) / f3);
        float scaleY = anchorViewLocationInScreen[1] + ((anchorView.getScaleY() * anchorView.getHeight()) / f3);
        float f4 = 1;
        resultOriginCenterPoint[0] = Math.round(scaleX + ((pivotX - 0.5f) * (anchorView.getScaleX() - f4) * anchorView.getWidth()));
        resultOriginCenterPoint[1] = Math.round(scaleY + ((pivotY - 0.5f) * (anchorView.getScaleY() - f4) * anchorView.getHeight()));
    }

    public final int getAnchorViewSpacing(View anchorView, AnchorViewTypeEnum enumType) {
        Intrinsics.checkNotNullParameter(anchorView, "anchorView");
        Intrinsics.checkNotNullParameter(enumType, "enumType");
        if (this.marginMap.isEmpty() || this.marginMap.get(enumType) == null) {
            return 0;
        }
        return getEnumViewSpacing(this.marginMap.get(enumType), anchorView, enumType);
    }
}