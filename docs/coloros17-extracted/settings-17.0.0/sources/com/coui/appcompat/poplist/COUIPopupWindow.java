package com.coui.appcompat.poplist;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.PopupWindow;
import androidx.core.content.ContextCompat;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.uiutil.ShadowUtils;
import com.support.appcompat.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIPopupWindow extends PopupWindow {
    private Context mContext;
    protected boolean mIsOutLineBackgroundInPopupWindow;
    private boolean mSetElevationInPopupwindow;
    protected WindowSpacingControlHelper mWindowSpacingControlHelper;

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.COUIPopupWindow$1, reason: invalid class name */
    public class AnonymousClass1 extends ViewOutlineProvider {
        public AnonymousClass1() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), COUIContextUtil.getAttrDimens(COUIPopupWindow.this.getContentView().getContext(), R.attr.couiRoundCornerM));
        }
    }

    public COUIPopupWindow(Context context) {
        this(context, null);
    }

    private void initPopupWindow(Context context) {
        this.mContext = context;
        TypedArray typedArrayObtainStyledAttributes = context.getTheme().obtainStyledAttributes(new int[]{com.support.poplist.R.attr.couiPopupWindowBackground});
        initPopupWindowBackground(context, typedArrayObtainStyledAttributes);
        typedArrayObtainStyledAttributes.recycle();
        setClippingEnabled(false);
        setElevation(0.0f);
        setExitTransition(null);
        setEnterTransition(null);
        setAnimationStyle(com.support.poplist.R.style.Animation_COUI_PopupListWindow);
    }

    public void addSpacingControlUtil(Context context, int i3, WindowSpacingControlHelper.AnchorViewTypeEnum anchorViewTypeEnum) {
        if (context != null) {
            addSpacingControlUtil(context.getResources().getDimensionPixelSize(i3), anchorViewTypeEnum);
        }
    }

    public int getAnchorViewSpacing(WindowSpacingControlHelper.AnchorViewTypeEnum anchorViewTypeEnum) {
        if (this.mWindowSpacingControlHelper.isUtilMapInit()) {
            return this.mWindowSpacingControlHelper.getAnchorViewSpacing(anchorViewTypeEnum);
        }
        return 0;
    }

    public void initElevationInPopupwindow() {
        if (!this.mSetElevationInPopupwindow || getContentView() == null) {
            return;
        }
        setBackgroundDrawable(null);
        if (ShadowUtils.checkOPlusViewElevationSDK()) {
            ShadowUtils.setElevationToView(getContentView(), 3);
        } else {
            setElevation(this.mContext.getResources().getDimensionPixelSize(R.dimen.support_shadow_size_level_five));
            getContentView().setOutlineSpotShadowColor(ContextCompat.getColor(this.mContext, com.support.poplist.R.color.coui_popup_outline_spot_shadow_color));
        }
    }

    public void initOutlineRoundRectBackground() {
        if (!this.mIsOutLineBackgroundInPopupWindow || getContentView() == null) {
            return;
        }
        getContentView().setOutlineProvider(new AnonymousClass1());
        getContentView().setClipToOutline(true);
    }

    public void initPopupWindowBackground(Context context, TypedArray typedArray) {
        typedArray.getDrawable(0);
        setBackgroundDrawable(context.getResources().getDrawable(com.support.poplist.R.drawable.coui_free_bottom_alert_poplist_background));
    }

    @Override // android.widget.PopupWindow
    public void setContentView(View view) {
        super.setContentView(view);
        initOutlineRoundRectBackground();
        initElevationInPopupwindow();
    }

    public void setDismissTouchOutside(boolean z2) {
        if (z2) {
            setTouchable(true);
            setFocusable(true);
            setOutsideTouchable(true);
        } else {
            setFocusable(false);
            setOutsideTouchable(false);
        }
        update();
    }

    public void setElevationInPopupwindow(boolean z2) {
        this.mSetElevationInPopupwindow = z2;
    }

    public COUIPopupWindow(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, androidx.appcompat.R.attr.popupWindowStyle);
    }

    public void addSpacingControlUtil(int i3, WindowSpacingControlHelper.AnchorViewTypeEnum anchorViewTypeEnum) {
        this.mWindowSpacingControlHelper.addAnchorViewSpacingMap(i3, anchorViewTypeEnum);
    }

    public COUIPopupWindow(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, com.support.poplist.R.style.Widget_COUI_PopupWindow);
    }

    public int getAnchorViewSpacing(View view, WindowSpacingControlHelper.AnchorViewTypeEnum anchorViewTypeEnum) {
        if (this.mWindowSpacingControlHelper.isUtilMapInit()) {
            return this.mWindowSpacingControlHelper.getAnchorViewSpacing(view, anchorViewTypeEnum);
        }
        return 0;
    }

    public COUIPopupWindow(View view, int i3, int i4) {
        this(view, i3, i4, false);
    }

    public COUIPopupWindow(View view, int i3, int i4, boolean z2) {
        super(view, i3, i4, z2);
        this.mSetElevationInPopupwindow = false;
        this.mIsOutLineBackgroundInPopupWindow = true;
        this.mWindowSpacingControlHelper = new WindowSpacingControlHelper();
        initPopupWindow(view.getContext());
    }

    public COUIPopupWindow(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mSetElevationInPopupwindow = false;
        this.mIsOutLineBackgroundInPopupWindow = true;
        this.mWindowSpacingControlHelper = new WindowSpacingControlHelper();
        initPopupWindow(context);
    }
}