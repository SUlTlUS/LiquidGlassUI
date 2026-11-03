package com.coui.appcompat.seekbar.handler;

import android.graphics.RectF;
import com.coui.appcompat.seekbar.core.COUISeekBarCore;
import com.coui.appcompat.seekbar.core.SeekBarState;
import com.oplus.dmp.sdk.search.SearchProtocol;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J \u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0018\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\nH\u0016J\u0018\u0010\f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\nH\u0016J \u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\u0015\u001a\u00020\u000e2\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\u0016\u001a\u00020\u000e2\u0006\u0010\u0007\u001a\u00020\bH\u0016¨\u0006\u0017"}, d2 = {"Lcom/coui/appcompat/seekbar/handler/VerticalOrientationHandler;", "Lcom/coui/appcompat/seekbar/handler/IOrientationHandler;", "()V", "calculateBackgroundRect", "", "rect", "Landroid/graphics/RectF;", "seekBar", "Lcom/coui/appcompat/seekbar/core/COUISeekBarCore;", "state", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "calculateProgressRect", "calculateSecondaryProgressRect", "calculateThumbPosition", "", "progress", SearchProtocol.QUERY_LENGTH, "isRtl", "", "getCenterCoordinate", "", "getProgressStart", "getSeekBarLength", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class VerticalOrientationHandler implements IOrientationHandler {
    @Override // com.coui.appcompat.seekbar.handler.IOrientationHandler
    public void calculateBackgroundRect(RectF rect, COUISeekBarCore seekBar, SeekBarState state) {
        Intrinsics.checkNotNullParameter(rect, "rect");
        Intrinsics.checkNotNullParameter(seekBar, "seekBar");
        Intrinsics.checkNotNullParameter(state, "state");
        int centerCoordinate = getCenterCoordinate(seekBar);
        float paddingTop = (seekBar.getPaddingTop() + state.getCurPaddingVertical()) - state.getCurBackgroundRadius();
        float height = ((seekBar.getHeight() - seekBar.getPaddingBottom()) - state.getCurPaddingVertical()) + state.getCurBackgroundRadius();
        float widthDeformedValue = state.getIsSupportDeformation() ? state.getWidthDeformedValue() : 0.0f;
        float f3 = centerCoordinate;
        float f4 = 2;
        float curBackgroundHeight = f3 - ((state.getCurBackgroundHeight() / f4) - widthDeformedValue);
        float curBackgroundHeight2 = f3 + ((state.getCurBackgroundHeight() / f4) - widthDeformedValue);
        if (state.getIsSupportDeformation()) {
            paddingTop = (paddingTop - state.getHeightTopDeformedUpValue()) + state.getHeightTopDeformedDownValue();
            height = (height - state.getHeightBottomDeformedUpValue()) + state.getHeightBottomDeformedDownValue();
        }
        rect.set((int) curBackgroundHeight, (int) paddingTop, (int) curBackgroundHeight2, (int) height);
    }

    @Override // com.coui.appcompat.seekbar.handler.IOrientationHandler
    public void calculateProgressRect(RectF rect, SeekBarState state) {
        Intrinsics.checkNotNullParameter(rect, "rect");
        Intrinsics.checkNotNullParameter(state, "state");
        COUISeekBarCore view = state.getView();
        if (view == null) {
            return;
        }
        float seekBarLength = getSeekBarLength(view);
        float progressStart = getProgressStart(view);
        int centerCoordinate = getCenterCoordinate(view);
        float realScale = ((1.0f - view.getRealScale(state.getDrawProgressScale())) * seekBarLength) + progressStart;
        float heightBottomDeformedDownValue = progressStart + seekBarLength;
        state.setThumbPosition(realScale);
        float widthDeformedValue = state.getIsSupportDeformation() ? state.getWidthDeformedValue() : 0.0f;
        float f3 = centerCoordinate;
        float f4 = 2;
        float curProgressHeight = f3 - ((state.getCurProgressHeight() / f4) - widthDeformedValue);
        float curProgressHeight2 = f3 + ((state.getCurProgressHeight() / f4) - widthDeformedValue);
        if (state.getIsSupportDeformation()) {
            float heightTopDeformedDownValue = (state.getHeightTopDeformedDownValue() + realScale) - state.getHeightTopDeformedUpValue();
            heightBottomDeformedDownValue = (heightBottomDeformedDownValue + state.getHeightBottomDeformedDownValue()) - state.getHeightBottomDeformedUpValue();
            state.setThumbPosition(realScale + (state.getHeightTopDeformedUpValue() - state.getHeightBottomDeformedDownValue()));
            realScale = heightTopDeformedDownValue;
        }
        rect.set(curProgressHeight, realScale - state.getCurProgressRadius(), curProgressHeight2, heightBottomDeformedDownValue + state.getCurProgressRadius());
    }

    @Override // com.coui.appcompat.seekbar.handler.IOrientationHandler
    public void calculateSecondaryProgressRect(RectF rect, SeekBarState state) {
        Intrinsics.checkNotNullParameter(rect, "rect");
        Intrinsics.checkNotNullParameter(state, "state");
        COUISeekBarCore view = state.getView();
        if (view == null) {
            return;
        }
        float seekBarLength = getSeekBarLength(view);
        float progressStart = getProgressStart(view);
        int centerCoordinate = getCenterCoordinate(view);
        float realScale = ((1.0f - view.getRealScale(state.getSecondaryProgressScale())) * seekBarLength) + progressStart;
        float heightBottomDeformedDownValue = progressStart + seekBarLength;
        float widthDeformedValue = state.getIsSupportDeformation() ? state.getWidthDeformedValue() : 0.0f;
        float f3 = centerCoordinate;
        float f4 = 2;
        float curProgressHeight = f3 - ((state.getCurProgressHeight() / f4) - widthDeformedValue);
        float curProgressHeight2 = f3 + ((state.getCurProgressHeight() / f4) - widthDeformedValue);
        if (state.getIsSupportDeformation()) {
            realScale = (realScale + state.getHeightTopDeformedDownValue()) - state.getHeightTopDeformedUpValue();
            heightBottomDeformedDownValue = (heightBottomDeformedDownValue + state.getHeightBottomDeformedDownValue()) - state.getHeightBottomDeformedUpValue();
        }
        rect.set(curProgressHeight, realScale - state.getCurProgressRadius(), curProgressHeight2, heightBottomDeformedDownValue + state.getCurProgressRadius());
    }

    @Override // com.coui.appcompat.seekbar.handler.IOrientationHandler
    public float calculateThumbPosition(float progress, float length, boolean isRtl) {
        return (1.0f - progress) * length;
    }

    @Override // com.coui.appcompat.seekbar.handler.IOrientationHandler
    public int getCenterCoordinate(COUISeekBarCore seekBar) {
        Intrinsics.checkNotNullParameter(seekBar, "seekBar");
        return seekBar.getSeekBarCenterX();
    }

    @Override // com.coui.appcompat.seekbar.handler.IOrientationHandler
    public float getProgressStart(COUISeekBarCore seekBar) {
        Intrinsics.checkNotNullParameter(seekBar, "seekBar");
        return seekBar.getPaddingTop() + seekBar.getState().getCurPaddingVertical();
    }

    @Override // com.coui.appcompat.seekbar.handler.IOrientationHandler
    public float getSeekBarLength(COUISeekBarCore seekBar) {
        Intrinsics.checkNotNullParameter(seekBar, "seekBar");
        return seekBar.getSeekBarHeight();
    }
}