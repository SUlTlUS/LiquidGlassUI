package com.coui.appcompat.seekbar.handler;

import android.graphics.RectF;
import com.coui.appcompat.seekbar.core.COUISeekBarCore;
import com.coui.appcompat.seekbar.core.SeekBarState;
import com.oplus.dmp.sdk.search.SearchProtocol;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J \u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0018\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\nH\u0016J\u0018\u0010\f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\nH\u0016J \u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\u0015\u001a\u00020\u000e2\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\u0016\u001a\u00020\u000e2\u0006\u0010\u0007\u001a\u00020\bH\u0016¨\u0006\u0017"}, d2 = {"Lcom/coui/appcompat/seekbar/handler/HorizontalOrientationHandler;", "Lcom/coui/appcompat/seekbar/handler/IOrientationHandler;", "()V", "calculateBackgroundRect", "", "rect", "Landroid/graphics/RectF;", "seekBar", "Lcom/coui/appcompat/seekbar/core/COUISeekBarCore;", "state", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "calculateProgressRect", "calculateSecondaryProgressRect", "calculateThumbPosition", "", "progress", SearchProtocol.QUERY_LENGTH, "isRtl", "", "getCenterCoordinate", "", "getProgressStart", "getSeekBarLength", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class HorizontalOrientationHandler implements IOrientationHandler {
    @Override // com.coui.appcompat.seekbar.handler.IOrientationHandler
    public void calculateBackgroundRect(RectF rect, COUISeekBarCore seekBar, SeekBarState state) {
        Intrinsics.checkNotNullParameter(rect, "rect");
        Intrinsics.checkNotNullParameter(seekBar, "seekBar");
        Intrinsics.checkNotNullParameter(state, "state");
        int centerCoordinate = getCenterCoordinate(seekBar);
        float backgroundHorizontalInset = seekBar.getBackgroundHorizontalInset(state);
        float start = (seekBar.getStart() + state.getPaddingHorizontal()) - backgroundHorizontalInset;
        float width = ((seekBar.getWidth() - seekBar.getEnd()) - state.getPaddingHorizontal()) + backgroundHorizontalInset;
        float widthDeformedValue = state.getIsSupportDeformation() ? state.getWidthDeformedValue() : 0.0f;
        float f3 = centerCoordinate;
        float f4 = 2;
        float curBackgroundHeight = f3 - ((state.getCurBackgroundHeight() / f4) - widthDeformedValue);
        float curBackgroundHeight2 = f3 + ((state.getCurBackgroundHeight() / f4) - widthDeformedValue);
        if (state.getIsSupportDeformation()) {
            if (state.getIsRtl()) {
                start = (start - state.getHeightTopDeformedUpValue()) + state.getHeightTopDeformedDownValue();
                width = (width - state.getHeightBottomDeformedUpValue()) + state.getHeightBottomDeformedDownValue();
            } else {
                start = (start - state.getHeightBottomDeformedDownValue()) + state.getHeightBottomDeformedUpValue();
                width = (width + state.getHeightTopDeformedUpValue()) - state.getHeightTopDeformedDownValue();
            }
        }
        rect.set(start, curBackgroundHeight, width, curBackgroundHeight2);
    }

    @Override // com.coui.appcompat.seekbar.handler.IOrientationHandler
    public void calculateProgressRect(RectF rect, SeekBarState state) {
        float realScale;
        float heightBottomDeformedDownValue;
        float f3;
        float f4;
        float heightTopDeformedUpValue;
        Intrinsics.checkNotNullParameter(rect, "rect");
        Intrinsics.checkNotNullParameter(state, "state");
        COUISeekBarCore view = state.getView();
        if (view == null) {
            return;
        }
        float seekBarLength = getSeekBarLength(view);
        float progressStart = getProgressStart(view);
        int centerCoordinate = getCenterCoordinate(view);
        float realScale2 = view.getRealScale(state.getDrawProgressScale());
        if (state.getUseSeparateProgressAndThumbScale()) {
            int max = state.getMax() - state.getMin();
            realScale2 = RangesKt.coerceIn(max > 0 ? (state.getOldProgress() - state.getMin()) / max : 0.0f, 0.0f, 1.0f);
            realScale = view.getRealScale(state.getScale());
        } else {
            realScale = realScale2;
        }
        if (state.getIsStartFromMiddle()) {
            progressStart = view.getWidth() / 2;
            if (state.getIsRtl()) {
                f3 = progressStart - ((realScale2 - 0.5f) * seekBarLength);
                realScale -= 0.5f;
                f4 = progressStart - (realScale * seekBarLength);
                float f5 = f3;
                heightBottomDeformedDownValue = progressStart;
                progressStart = f5;
            } else {
                heightBottomDeformedDownValue = ((realScale2 - 0.5f) * seekBarLength) + progressStart;
                realScale -= 0.5f;
                f4 = progressStart + (realScale * seekBarLength);
            }
        } else if (state.getIsRtl()) {
            progressStart += seekBarLength;
            f3 = progressStart - (realScale2 * seekBarLength);
            f4 = progressStart - (realScale * seekBarLength);
            float f6 = f3;
            heightBottomDeformedDownValue = progressStart;
            progressStart = f6;
        } else {
            heightBottomDeformedDownValue = (realScale2 * seekBarLength) + progressStart;
            f4 = progressStart + (realScale * seekBarLength);
        }
        state.setThumbPosition(f4);
        float f7 = centerCoordinate;
        float f8 = 2;
        float progressRectHeight = (view.getProgressRectHeight(state) / f8) - (state.getIsSupportDeformation() ? state.getWidthDeformedValue() : 0.0f);
        float f9 = f7 - progressRectHeight;
        float f10 = f7 + progressRectHeight;
        if (state.getIsStartFromMiddle() && progressStart > heightBottomDeformedDownValue) {
            if (state.getIsRtl()) {
                if (state.getIsSupportDeformation()) {
                    heightBottomDeformedDownValue = (heightBottomDeformedDownValue - state.getHeightTopDeformedUpValue()) + state.getHeightBottomDeformedDownValue();
                    progressStart = (progressStart - state.getHeightBottomDeformedUpValue()) + state.getHeightBottomDeformedDownValue();
                }
            } else if (state.getIsSupportDeformation()) {
                heightBottomDeformedDownValue = (heightBottomDeformedDownValue - state.getHeightBottomDeformedDownValue()) + state.getHeightBottomDeformedUpValue();
                progressStart = (progressStart + state.getHeightTopDeformedUpValue()) - state.getHeightBottomDeformedDownValue();
            }
            heightTopDeformedUpValue = progressStart;
            progressStart = heightBottomDeformedDownValue;
        } else if (!state.getIsSupportDeformation()) {
            heightTopDeformedUpValue = heightBottomDeformedDownValue;
        } else if (state.getIsRtl()) {
            progressStart = (progressStart - state.getHeightTopDeformedUpValue()) + state.getHeightBottomDeformedDownValue();
            heightTopDeformedUpValue = state.getHeightBottomDeformedDownValue() + (heightBottomDeformedDownValue - state.getHeightBottomDeformedUpValue());
        } else {
            progressStart = (progressStart - state.getHeightBottomDeformedDownValue()) + state.getHeightBottomDeformedUpValue();
            heightTopDeformedUpValue = (heightBottomDeformedDownValue + state.getHeightTopDeformedUpValue()) - state.getHeightBottomDeformedDownValue();
        }
        if (state.getExtendProgressRectForRoundCorner()) {
            progressStart -= state.getProgressHeight() / f8;
            heightTopDeformedUpValue += state.getProgressHeight() / f8;
        }
        if (state.getIsSupportDeformation()) {
            float heightTopDeformedUpValue2 = state.getHeightTopDeformedUpValue() - state.getHeightBottomDeformedDownValue();
            if (state.getIsRtl()) {
                heightTopDeformedUpValue2 = -heightTopDeformedUpValue2;
            }
            state.setThumbPosition(f4 + heightTopDeformedUpValue2);
        }
        rect.set(progressStart, f9, heightTopDeformedUpValue, f10);
    }

    @Override // com.coui.appcompat.seekbar.handler.IOrientationHandler
    public void calculateSecondaryProgressRect(RectF rect, SeekBarState state) {
        Intrinsics.checkNotNullParameter(rect, "rect");
        Intrinsics.checkNotNullParameter(state, "state");
        COUISeekBarCore view = state.getView();
        if (view == null) {
            return;
        }
        int centerCoordinate = getCenterCoordinate(view);
        float backgroundHorizontalInset = view.getBackgroundHorizontalInset(state);
        float start = (view.getStart() + state.getPaddingHorizontal()) - backgroundHorizontalInset;
        float width = ((view.getWidth() - view.getEnd()) - state.getPaddingHorizontal()) + backgroundHorizontalInset;
        if (state.getIsSupportDeformation()) {
            if (state.getIsRtl()) {
                start = (start - state.getHeightTopDeformedUpValue()) + state.getHeightTopDeformedDownValue();
                width = (width - state.getHeightBottomDeformedUpValue()) + state.getHeightBottomDeformedDownValue();
            } else {
                start = (start - state.getHeightBottomDeformedDownValue()) + state.getHeightBottomDeformedUpValue();
                width = (width + state.getHeightTopDeformedUpValue()) - state.getHeightTopDeformedDownValue();
            }
        }
        float f3 = width - start;
        float realScale = view.getRealScale(state.getSecondaryProgressScale());
        if (state.getIsStartFromMiddle()) {
            start = (start + width) / 2;
            if (state.getIsRtl()) {
                width = start;
                start -= (realScale - 0.5f) * f3;
            } else {
                realScale -= 0.5f;
                width = start + (realScale * f3);
            }
        } else if (state.getIsRtl()) {
            start = width - (realScale * f3);
        } else {
            width = start + (realScale * f3);
        }
        if (state.getIsStartFromMiddle() && start > width) {
            float f4 = width;
            width = start;
            start = f4;
        }
        float f5 = centerCoordinate;
        float progressRectHeight = (view.getProgressRectHeight(state) / 2) - (state.getIsSupportDeformation() ? state.getWidthDeformedValue() : 0.0f);
        rect.set(start, f5 - progressRectHeight, width, f5 + progressRectHeight);
    }

    @Override // com.coui.appcompat.seekbar.handler.IOrientationHandler
    public float calculateThumbPosition(float progress, float length, boolean isRtl) {
        float f3 = progress * length;
        return isRtl ? length - f3 : f3;
    }

    @Override // com.coui.appcompat.seekbar.handler.IOrientationHandler
    public int getCenterCoordinate(COUISeekBarCore seekBar) {
        Intrinsics.checkNotNullParameter(seekBar, "seekBar");
        return seekBar.getSeekBarCenterY();
    }

    @Override // com.coui.appcompat.seekbar.handler.IOrientationHandler
    public float getProgressStart(COUISeekBarCore seekBar) {
        Intrinsics.checkNotNullParameter(seekBar, "seekBar");
        return seekBar.getStart() + seekBar.getState().getPaddingHorizontal();
    }

    @Override // com.coui.appcompat.seekbar.handler.IOrientationHandler
    public float getSeekBarLength(COUISeekBarCore seekBar) {
        Intrinsics.checkNotNullParameter(seekBar, "seekBar");
        return seekBar.getSeekBarWidth();
    }
}