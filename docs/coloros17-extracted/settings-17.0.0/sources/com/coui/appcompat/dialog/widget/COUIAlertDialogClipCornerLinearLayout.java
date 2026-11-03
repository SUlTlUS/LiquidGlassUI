package com.coui.appcompat.dialog.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.appcompat.widget.LinearLayoutCompat;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.oplus.graphics.OplusOutline;
import com.support.appcompat.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIAlertDialogClipCornerLinearLayout extends LinearLayoutCompat {
    private static final String TAG = "COUIAlertDialogClipCorner";
    private boolean mBlurBackgroundWindow;
    private boolean mIsSupportRoundCornerWhenBlur;
    private boolean mIsSupportSmoothRoundCorner;
    private int mRadius;

    /* JADX INFO: renamed from: com.coui.appcompat.dialog.widget.COUIAlertDialogClipCornerLinearLayout$1, reason: invalid class name */
    public class AnonymousClass1 extends ViewOutlineProvider {
        public AnonymousClass1() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            boolean z2 = COUIAlertDialogClipCornerLinearLayout.access$000(COUIAlertDialogClipCornerLinearLayout.this) && !COUIAlertDialogClipCornerLinearLayout.access$100(COUIAlertDialogClipCornerLinearLayout.this);
            if (!COUIAlertDialogClipCornerLinearLayout.access$200(COUIAlertDialogClipCornerLinearLayout.this) || z2) {
                outline.setRoundRect(0, 0, COUIAlertDialogClipCornerLinearLayout.this.getMeasuredWidth(), COUIAlertDialogClipCornerLinearLayout.this.getMeasuredHeight(), COUIAlertDialogClipCornerLinearLayout.access$300(COUIAlertDialogClipCornerLinearLayout.this));
            } else {
                new OplusOutline(outline).setSmoothRoundRect(0, 0, COUIAlertDialogClipCornerLinearLayout.this.getMeasuredWidth(), COUIAlertDialogClipCornerLinearLayout.this.getMeasuredHeight(), COUIAlertDialogClipCornerLinearLayout.access$300(COUIAlertDialogClipCornerLinearLayout.this), COUIContextUtil.getFloat(COUIAlertDialogClipCornerLinearLayout.this.getContext(), R.dimen.coui_round_corner_xl_weight));
            }
            COUILog.i(COUIAlertDialogClipCornerLinearLayout.TAG, "getOutline: notUseRoundCornerWhenBlur" + z2 + " mBlurBackgroundWindow=" + COUIAlertDialogClipCornerLinearLayout.access$000(COUIAlertDialogClipCornerLinearLayout.this) + " mIsSupportRoundCornerWhenBlur=" + COUIAlertDialogClipCornerLinearLayout.access$100(COUIAlertDialogClipCornerLinearLayout.this) + " mIsSupportSmoothRoundCorner=" + COUIAlertDialogClipCornerLinearLayout.access$200(COUIAlertDialogClipCornerLinearLayout.this) + " mRadius=" + COUIAlertDialogClipCornerLinearLayout.access$300(COUIAlertDialogClipCornerLinearLayout.this));
        }
    }

    public COUIAlertDialogClipCornerLinearLayout(Context context) {
        super(context);
        this.mBlurBackgroundWindow = false;
        this.mIsSupportRoundCornerWhenBlur = false;
        this.mIsSupportSmoothRoundCorner = false;
    }

    public static /* synthetic */ boolean access$000(COUIAlertDialogClipCornerLinearLayout cOUIAlertDialogClipCornerLinearLayout) {
        return cOUIAlertDialogClipCornerLinearLayout.mBlurBackgroundWindow;
    }

    public static /* synthetic */ boolean access$100(COUIAlertDialogClipCornerLinearLayout cOUIAlertDialogClipCornerLinearLayout) {
        return cOUIAlertDialogClipCornerLinearLayout.mIsSupportRoundCornerWhenBlur;
    }

    public static /* synthetic */ boolean access$200(COUIAlertDialogClipCornerLinearLayout cOUIAlertDialogClipCornerLinearLayout) {
        return cOUIAlertDialogClipCornerLinearLayout.mIsSupportSmoothRoundCorner;
    }

    public static /* synthetic */ int access$300(COUIAlertDialogClipCornerLinearLayout cOUIAlertDialogClipCornerLinearLayout) {
        return cOUIAlertDialogClipCornerLinearLayout.mRadius;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mRadius > 0) {
            setClipToOutline(true);
            setOutlineProvider(new AnonymousClass1());
        }
    }

    public void setBlurBackgroundWindow(boolean z2) {
        this.mBlurBackgroundWindow = z2;
    }

    public void setIsSupportRoundCornerWhenBlur(boolean z2) {
        this.mIsSupportRoundCornerWhenBlur = z2;
    }

    public COUIAlertDialogClipCornerLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mBlurBackgroundWindow = false;
        this.mIsSupportRoundCornerWhenBlur = false;
        this.mIsSupportSmoothRoundCorner = false;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, com.support.dialog.R.styleable.COUIAlertDialogClipCornerLinearLayout);
        boolean zIsVersionSupport = RoundCornerUtil.isVersionSupport();
        this.mIsSupportSmoothRoundCorner = zIsVersionSupport;
        this.mRadius = typedArrayObtainStyledAttributes.getDimensionPixelSize(com.support.dialog.R.styleable.COUIAlertDialogClipCornerLinearLayout_clip_radius, COUIContextUtil.getAttrDimens(getContext(), zIsVersionSupport ? R.attr.couiRoundCornerXLRadius : R.attr.couiRoundCornerXL));
        typedArrayObtainStyledAttributes.recycle();
    }

    public COUIAlertDialogClipCornerLinearLayout(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.mBlurBackgroundWindow = false;
        this.mIsSupportRoundCornerWhenBlur = false;
        this.mIsSupportSmoothRoundCorner = false;
    }
}