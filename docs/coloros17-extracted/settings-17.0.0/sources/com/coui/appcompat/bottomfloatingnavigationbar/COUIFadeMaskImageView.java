package com.coui.appcompat.bottomfloatingnavigationbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.support.bottomnavigation.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIFadeMaskImageView extends AppCompatImageView {
    private Paint mFadePaint;
    private Shader mFadeShader;
    private boolean mShowMask;
    private PorterDuffXfermode mXfermode;

    public COUIFadeMaskImageView(Context context) {
        super(context);
        this.mShowMask = false;
        init();
    }

    private void init() {
        this.mFadePaint = new Paint(1);
        this.mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    }

    @Override // android.widget.ImageView, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mShowMask) {
            int iSaveLayer = canvas.saveLayer(0.0f, 0.0f, getWidth(), getHeight(), null);
            Shader shader = this.mFadeShader;
            if (shader != null) {
                this.mFadePaint.setShader(shader);
                this.mFadePaint.setXfermode(this.mXfermode);
                canvas.drawRect(0.0f, 0.0f, getWidth(), getHeight(), this.mFadePaint);
            }
            canvas.restoreToCount(iSaveLayer);
        }
    }

    @Override // android.view.View
    public void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        if (i3 <= 0 || i4 <= 0) {
            return;
        }
        this.mFadeShader = new RadialGradient(i3 / 2.0f, 0.0f, i4, COUIContextUtil.getColor(getContext(), R.color.coui_bottom_floating_navigation_bar_mask_center_color), COUIContextUtil.getColor(getContext(), R.color.coui_bottom_floating_navigation_bar_mask_edge_color), Shader.TileMode.CLAMP);
    }

    public void setShowMask(boolean z2) {
        this.mShowMask = z2;
    }

    public COUIFadeMaskImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mShowMask = false;
        init();
    }

    public COUIFadeMaskImageView(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.mShowMask = false;
        init();
    }
}