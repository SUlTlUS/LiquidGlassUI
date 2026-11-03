package com.coui.appcompat.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.support.appcompat.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIAlertDialogOverlayHelper {
    private final Context mContext;
    private ColorDrawable mDefaultSurfaceOverlay;
    private Drawable mFallbackOverlay;
    private Drawable mOverlay;

    public enum OverlayMode {
        OVERLAY_ONLY,
        BLUR_ACTIVE,
        BLUR_DEGRADED
    }

    public COUIAlertDialogOverlayHelper(Context context) {
        this.mContext = context;
    }

    private Drawable getDefaultSurfaceOverlay() {
        if (this.mDefaultSurfaceOverlay == null) {
            this.mDefaultSurfaceOverlay = new ColorDrawable(COUIContextUtil.getAttrColor(this.mContext, R.attr.couiColorSurface, 0));
        }
        return this.mDefaultSurfaceOverlay;
    }

    private Drawable mutateDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        Drawable drawableMutate = drawable.mutate();
        drawableMutate.setCallback(null);
        return drawableMutate;
    }

    private Drawable resolveBlurFallbackOverlay() {
        Drawable drawable = this.mOverlay;
        if (drawable != null) {
            return drawable;
        }
        Drawable drawable2 = this.mFallbackOverlay;
        return drawable2 != null ? drawable2 : getDefaultSurfaceOverlay();
    }

    private Drawable resolveDialogOverlay() {
        Drawable drawable = this.mOverlay;
        return drawable != null ? drawable : getDefaultSurfaceOverlay();
    }

    public void applyOverlayState(View view, OverlayMode overlayMode) {
        if (view == null) {
            return;
        }
        int iOrdinal = overlayMode.ordinal();
        if (iOrdinal == 0) {
            view.setBackground(resolveDialogOverlay());
            view.setVisibility(0);
        } else if (iOrdinal == 1) {
            view.setBackground(null);
            view.setVisibility(8);
        } else {
            if (iOrdinal != 2) {
                return;
            }
            view.setBackground(resolveBlurFallbackOverlay());
            view.setVisibility(0);
        }
    }

    public boolean hasFallbackOverlay() {
        return this.mFallbackOverlay != null;
    }

    public boolean hasOverlay() {
        return this.mOverlay != null;
    }

    public boolean hasOverlayConfig() {
        return hasOverlay() || hasFallbackOverlay();
    }

    public void setBlurFallbackOverlay(Drawable drawable) {
        this.mFallbackOverlay = mutateDrawable(drawable);
        this.mDefaultSurfaceOverlay = null;
    }

    public void setDialogOverlay(Drawable drawable) {
        this.mOverlay = mutateDrawable(drawable);
        this.mDefaultSurfaceOverlay = null;
    }

    public void setFallbackColor(int i3) {
        this.mFallbackOverlay = new ColorDrawable(i3);
        this.mDefaultSurfaceOverlay = null;
    }
}