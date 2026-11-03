package com.coui.appcompat.poplist;

import android.view.View;
import com.coui.appcompat.anim.param.PopupGravityMotionParams;
import com.coui.appcompat.animation.bezier.BezierGravityConfig;
import com.coui.appcompat.animation.bezier.BezierGravityEndpoints;
import com.coui.appcompat.animation.bezier.BezierGravityPathGeometry;
import com.coui.appcompat.animation.bezier.BezierGravityPathMath;

/* JADX INFO: loaded from: classes7.dex */
public final class PopupMotionTrajectoryUtil {
    private static final float BEZIER_T_COERCE_MAX = 0.95f;
    private static final float BEZIER_T_COERCE_MIN = 0.05f;

    public static final class Snapshot {
        public BezierGravityPathGeometry geometry;
        public float menuCenterScreenX;
        public float menuCenterScreenY;
        public int pathAnchorMode;
        public int resolvedPathAnchorSlot;
    }

    private PopupMotionTrajectoryUtil() {
    }

    public static Snapshot snapshot(COUIPopupListWindow cOUIPopupListWindow, PopupGravityMotionParams popupGravityMotionParams) {
        PopupMenuLocateHelper locateHelper;
        if (cOUIPopupListWindow == null || popupGravityMotionParams == null || (locateHelper = cOUIPopupListWindow.getLocateHelper()) == null) {
            return null;
        }
        PopupMenuDomain domain = locateHelper.getDomain();
        if (!domain.mMainMenu.isEmpty() && !domain.mAnchor.isEmpty()) {
            float[] fArr = {domain.mLastPathAnchorX - domain.mMainMenu.centerX(), domain.mLastPathAnchorY - domain.mMainMenu.centerY()};
            BezierGravityConfig bezierGravityConfig = new BezierGravityConfig(popupGravityMotionParams.getGravityDistanceMinPx(), popupGravityMotionParams.getGravityDistanceMaxPx(), popupGravityMotionParams.getGravityArcMaxPx());
            float p1t = popupGravityMotionParams.getP1T();
            float p2t = popupGravityMotionParams.getP2T();
            if (p1t < BEZIER_T_COERCE_MIN) {
                p1t = 0.05f;
            } else if (p1t > 0.95f) {
                p1t = 0.95f;
            }
            if (p2t < BEZIER_T_COERCE_MIN) {
                p2t = 0.05f;
            } else if (p2t > 0.95f) {
                p2t = 0.95f;
            }
            BezierGravityPathGeometry bezierGravityPathGeometryControlPoints = BezierGravityPathMath.controlPoints(new BezierGravityEndpoints(fArr[0], fArr[1], 0.0f, 0.0f), bezierGravityConfig, p1t, p2t);
            View contentView = cOUIPopupListWindow.getContentView();
            if (contentView == null) {
                return null;
            }
            int[] iArr = new int[2];
            contentView.getLocationOnScreen(iArr);
            Snapshot snapshot = new Snapshot();
            snapshot.geometry = bezierGravityPathGeometryControlPoints;
            snapshot.menuCenterScreenX = iArr[0] + domain.mMainMenu.centerX();
            snapshot.menuCenterScreenY = iArr[1] + domain.mMainMenu.centerY();
            snapshot.resolvedPathAnchorSlot = domain.mResolvedPathAnchorSlot;
            snapshot.pathAnchorMode = domain.mPathAnchorMode;
            return snapshot;
        }
        return null;
    }
}