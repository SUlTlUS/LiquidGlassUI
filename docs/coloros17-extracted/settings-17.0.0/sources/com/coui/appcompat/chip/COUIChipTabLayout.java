package com.coui.appcompat.chip;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.support.chip.R;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 !2\u00020\u0001:\u0001!B%\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0014J\u0010\u0010\u0015\u001a\u00020\u00072\u0006\u0010\u0016\u001a\u00020\u000eH\u0002J\u0006\u0010\u0017\u001a\u00020\fJ\u0006\u0010\u0018\u001a\u00020\u0019J\u0006\u0010\u001a\u001a\u00020\u0012J\u0010\u0010\u001b\u001a\u00020\u00122\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dJ\u000e\u0010\u001e\u001a\u00020\u00122\u0006\u0010\u001f\u001a\u00020\u0019J\b\u0010 \u001a\u00020\u0012H\u0003R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\""}, d2 = {"Lcom/coui/appcompat/chip/COUIChipTabLayout;", "Landroid/widget/LinearLayout;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "chipScrollView", "Lcom/coui/appcompat/chip/ChipMaskHorizontalScrollView;", "chipTabGroup", "Lcom/coui/appcompat/chip/COUIChipTabGroup;", "clipCanvasExpandBottomPx", "", "iconOnlyChip", "Lcom/coui/appcompat/chip/COUIChipTab;", "dispatchDraw", "", "canvas", "Landroid/graphics/Canvas;", "dpToPx", "dp", "getChipTabGroup", "isIconOnlyChipVisible", "", "refresh", "setIconOnlyChipClickListener", "listener", "Landroid/view/View$OnClickListener;", "setIconOnlyChipVisible", "visible", "setupIconOnlyChip", "Companion", "coui-support-chip_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class COUIChipTabLayout extends LinearLayout {
    private static final float CLIP_CANVAS_EXPAND_BOTTOM_DP = 100.0f;
    private static final float ICON_SIZE_20 = 20.0f;
    private static final float PADDING_HORIZONTAL_16 = 16.0f;
    private static final float PADDING_HORIZONTAL_6 = 6.0f;
    private static final float PADDING_HORIZONTAL_WITH_ICON_ONLY_CHIP = 8.0f;
    private ChipMaskHorizontalScrollView chipScrollView;
    private COUIChipTabGroup chipTabGroup;
    private final float clipCanvasExpandBottomPx;
    private COUIChipTab iconOnlyChip;

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIChipTabLayout(Context context) {
        this(context, null, 0, 6, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    private final int dpToPx(float dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    private final void setupIconOnlyChip() {
        Drawable drawable = getContext().getDrawable(R.drawable.function_icon);
        float fDpToPx = dpToPx(20.0f);
        float fDpToPx2 = dpToPx(6.0f);
        COUIChipTab cOUIChipTab = this.iconOnlyChip;
        cOUIChipTab.setChipIcon(drawable);
        cOUIChipTab.setChipIconVisible(true);
        cOUIChipTab.setShowIconOnly(true);
        COUIChipTabDrawable tabDrawable = cOUIChipTab.getTabDrawable();
        if (tabDrawable != null) {
            tabDrawable.setChipIconSize(fDpToPx);
            tabDrawable.setChipStartPadding(fDpToPx2);
            tabDrawable.setChipEndPadding(fDpToPx2);
        }
        cOUIChipTab.setCheckable(true);
        cOUIChipTab.setChecked(true);
        cOUIChipTab.setText("");
        this.iconOnlyChip.setVisibility(8);
        this.chipScrollView.setPadding(dpToPx(16.0f), 0, dpToPx(16.0f), 0);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        int iSaveLayer = canvas.saveLayer(0.0f, -this.clipCanvasExpandBottomPx, getWidth(), getHeight() + this.clipCanvasExpandBottomPx, null);
        super.dispatchDraw(canvas);
        canvas.restoreToCount(iSaveLayer);
    }

    public final COUIChipTabGroup getChipTabGroup() {
        return this.chipTabGroup;
    }

    public final boolean isIconOnlyChipVisible() {
        return this.iconOnlyChip.getVisibility() == 0;
    }

    public final void refresh() {
        this.iconOnlyChip.refresh();
        this.chipTabGroup.refresh();
        invalidate();
    }

    public final void setIconOnlyChipClickListener(View.OnClickListener listener) {
        this.iconOnlyChip.setOnClickListener(listener);
    }

    public final void setIconOnlyChipVisible(boolean visible) {
        if ((this.iconOnlyChip.getVisibility() == 0) == visible) {
            return;
        }
        this.iconOnlyChip.setVisibility(visible ? 0 : 8);
        int iDpToPx = visible ? dpToPx(8.0f) : dpToPx(16.0f);
        this.chipScrollView.setPadding(iDpToPx, 0, iDpToPx, 0);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIChipTabLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public /* synthetic */ COUIChipTabLayout(Context context, AttributeSet attributeSet, int i3, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i4 & 2) != 0 ? null : attributeSet, (i4 & 4) != 0 ? 0 : i3);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUIChipTabLayout(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        Intrinsics.checkNotNullParameter(context, "context");
        this.clipCanvasExpandBottomPx = dpToPx(100.0f);
        setOrientation(0);
        setGravity(16);
        LayoutInflater.from(context).inflate(R.layout.layout_chip_tab, (ViewGroup) this, true);
        View viewFindViewById = findViewById(R.id.icon_only_chip);
        Intrinsics.checkNotNullExpressionValue(viewFindViewById, "findViewById(...)");
        this.iconOnlyChip = (COUIChipTab) viewFindViewById;
        View viewFindViewById2 = findViewById(R.id.chip_scroll_view);
        Intrinsics.checkNotNullExpressionValue(viewFindViewById2, "findViewById(...)");
        ChipMaskHorizontalScrollView chipMaskHorizontalScrollView = (ChipMaskHorizontalScrollView) viewFindViewById2;
        this.chipScrollView = chipMaskHorizontalScrollView;
        chipMaskHorizontalScrollView.setOffscreenLayerHandledByLayout$coui_support_chip_release(true);
        View viewFindViewById3 = findViewById(R.id.chip_tab_group);
        Intrinsics.checkNotNullExpressionValue(viewFindViewById3, "findViewById(...)");
        this.chipTabGroup = (COUIChipTabGroup) viewFindViewById3;
        setupIconOnlyChip();
    }
}