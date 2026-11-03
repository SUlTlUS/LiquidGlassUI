package com.coui.appcompat.dialog.widget;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import androidx.appcompat.widget.LinearLayoutCompat;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.OnMaterialStateChangeListener;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.dialog.olivelink.DialogShadowEdgeParams;
import com.coui.appcompat.dialog.olivelink.DialogShadowEdgeParamsExtKt;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.shadowedge.COUIShadowEdgeDrawable;
import com.coui.appcompat.shadowedge.IShadowEdgeEffect;
import com.support.appcompat.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIAlertDialogRootLayout extends LinearLayoutCompat implements OnMaterialStateChangeListener {
    private static final int CORNER_TYPE = 2;
    private static final String TAG = "DialogRootLayout";
    private boolean mBlurBackgroundWindow;
    private int mCornerRadius;
    private boolean mIsTiny;
    private COUIShadowEdgeDrawable mShadowEdgeDrawable;

    public COUIAlertDialogRootLayout(Context context) {
        super(context);
    }

    private void applyShadowEdgeParams() {
        if (!this.mBlurBackgroundWindow) {
            COUILog.i(TAG, "mBlurBackgroundWindow is false return!");
            return;
        }
        boolean zIsCOUIDarkThemeOrNightMode = COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(getContext());
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = new COUIShadowEdgeDrawable();
        this.mShadowEdgeDrawable = cOUIShadowEdgeDrawable;
        cOUIShadowEdgeDrawable.setIsDarkMode(zIsCOUIDarkThemeOrNightMode);
        this.mShadowEdgeDrawable.setEdgeStyle(IShadowEdgeEffect.EdgeStyle.EDGE_STYLE_2);
        this.mShadowEdgeDrawable.setShadowStyle(IShadowEdgeEffect.ShadowStyle.SHADOW_STYLE_0);
        this.mShadowEdgeDrawable.setCornerType(2);
        DialogShadowEdgeParams dialogShadowEdgeParams = DialogShadowEdgeParamsExtKt.get(DialogShadowEdgeParams.INSTANCE);
        this.mShadowEdgeDrawable.setWeight(getContext().getResources().getFloat(R.dimen.weight_16_1_large));
        if (zIsCOUIDarkThemeOrNightMode) {
            this.mShadowEdgeDrawable.setLineAlpha(dialogShadowEdgeParams.getDarklineAlpha());
            this.mShadowEdgeDrawable.setLineAngle(dialogShadowEdgeParams.getDarklineAngle());
            this.mShadowEdgeDrawable.setLineWidth(dp2px(dialogShadowEdgeParams.getDarklineWidth()));
            this.mShadowEdgeDrawable.setFadeWidthScale(dialogShadowEdgeParams.getDarkfadeWidthScale());
            this.mShadowEdgeDrawable.setFadeAlpha(dialogShadowEdgeParams.getDarkfadeAlphaX(), dialogShadowEdgeParams.getDarkfadeAlphaY());
            this.mShadowEdgeDrawable.setPaintBlendMode(BlendMode.SRC_OVER);
        } else {
            this.mShadowEdgeDrawable.setLineAlpha(dialogShadowEdgeParams.getLineAlpha());
            this.mShadowEdgeDrawable.setLineAngle(dialogShadowEdgeParams.getLineAngle());
            this.mShadowEdgeDrawable.setLineWidth(dp2px(dialogShadowEdgeParams.getLineWidth()));
            this.mShadowEdgeDrawable.setFadeWidthScale(dialogShadowEdgeParams.getFadeWidthScale());
            this.mShadowEdgeDrawable.setFadeAlpha(dialogShadowEdgeParams.getFadeAlphaX(), dialogShadowEdgeParams.getFadeAlphaY());
            this.mShadowEdgeDrawable.setPaintBlendMode(BlendMode.SRC_OVER);
        }
        COUIMaterialStateManager cOUIMaterialStateManager = COUIMaterialStateManager.INSTANCE;
        this.mShadowEdgeDrawable.setDrawableEnabled((cOUIMaterialStateManager.materialVersionCheck() ? cOUIMaterialStateManager.isStrokeEffectEnable() : false) && this.mBlurBackgroundWindow);
        setWillNotDraw(false);
    }

    private float dp2px(float f3) {
        return TypedValue.applyDimension(1, f3, getContext().getResources().getDisplayMetrics());
    }

    private void ensureShadowEdgeReady() {
        if (!this.mBlurBackgroundWindow || this.mShadowEdgeDrawable == null) {
            return;
        }
        refreshShadowEdgeGeometry();
        if (isAttachedToWindow()) {
            this.mShadowEdgeDrawable.setCallback(this);
        }
    }

    private void refreshShadowEdgeGeometry() {
        int i3;
        if (this.mShadowEdgeDrawable == null || getWidth() <= 0 || getHeight() <= 0) {
            return;
        }
        this.mShadowEdgeDrawable.setBounds(0, 0, getWidth(), getHeight());
        if (!this.mIsTiny || (i3 = this.mCornerRadius) <= 0) {
            this.mShadowEdgeDrawable.setCornerRadius(this.mCornerRadius);
        } else {
            this.mShadowEdgeDrawable.setCornerRadius(i3, i3, 0.0f, 0.0f);
        }
    }

    private void setStrokeEffectEnable(boolean z2) {
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable;
        if (cOUIShadowEdgeDrawable != null) {
            cOUIShadowEdgeDrawable.setDrawableEnabled(z2);
            invalidate();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        COUIMaterialStateManager.INSTANCE.addOnMaterialStateChangedListener(this);
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable;
        if (cOUIShadowEdgeDrawable != null) {
            cOUIShadowEdgeDrawable.setCallback(this);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable;
        if (cOUIShadowEdgeDrawable != null) {
            cOUIShadowEdgeDrawable.setCallback(null);
        }
        COUIMaterialStateManager.INSTANCE.removeOnMaterialStateChangedListener(this);
        super.onDetachedFromWindow();
    }

    @Override // androidx.appcompat.widget.LinearLayoutCompat, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable;
        if (cOUIShadowEdgeDrawable != null) {
            cOUIShadowEdgeDrawable.draw(canvas);
        }
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public CharSequence onDump() {
        return toString();
    }

    @Override // androidx.appcompat.widget.LinearLayoutCompat, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        super.onLayout(z2, i3, i4, i5, i6);
        if (!this.mBlurBackgroundWindow || this.mShadowEdgeDrawable == null) {
            return;
        }
        refreshShadowEdgeGeometry();
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public void onMaterialStateChange(int i3, boolean z2) {
        if (i3 == 1) {
            setStrokeEffectEnable(z2 && this.mBlurBackgroundWindow);
        }
    }

    public void setBlurBackgroundWindow(boolean z2) {
        this.mBlurBackgroundWindow = z2;
        if (z2 && this.mShadowEdgeDrawable == null) {
            applyShadowEdgeParams();
            ensureShadowEdgeReady();
        }
        COUIMaterialStateManager cOUIMaterialStateManager = COUIMaterialStateManager.INSTANCE;
        boolean z3 = false;
        boolean zIsStrokeEffectEnable = cOUIMaterialStateManager.materialVersionCheck() ? cOUIMaterialStateManager.isStrokeEffectEnable() : false;
        if (z2 && zIsStrokeEffectEnable) {
            z3 = true;
        }
        setStrokeEffectEnable(z3);
    }

    public void setShadowEdgeCornerInfo(int i3, boolean z2) {
        if (this.mCornerRadius == i3 && this.mIsTiny == z2) {
            return;
        }
        this.mCornerRadius = i3;
        this.mIsTiny = z2;
        refreshShadowEdgeGeometry();
    }

    @Override // android.view.View
    public boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.mShadowEdgeDrawable;
    }

    public COUIAlertDialogRootLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public COUIAlertDialogRootLayout(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
    }
}