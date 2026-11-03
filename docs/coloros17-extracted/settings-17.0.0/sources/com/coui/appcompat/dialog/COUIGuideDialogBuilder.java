package com.coui.appcompat.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import com.coui.appcompat.uiutil.UIUtil;
import com.support.dialog.R;
import java.util.List;

/* JADX INFO: loaded from: classes7.dex */
public class COUIGuideDialogBuilder {
    private AlertDialog mAlertDialog;
    private COUIGuideLayoutContentView mContentView;
    private Context mContext;
    private COUIAlertDialogBuilder mCouiAlertDialogBuilder;

    public COUIGuideDialogBuilder(Context context) {
        this(context, R.style.COUIAlertDialog_Guide);
    }

    public static /* synthetic */ void a(COUIGuideDialogBuilder cOUIGuideDialogBuilder, boolean z2) {
        cOUIGuideDialogBuilder.mContentView.setBlurState(z2);
    }

    public COUIGuideDialogBuilder addButtonClickListener(COUIGuideLayoutContentView.OnButtonClickListener onButtonClickListener) {
        this.mContentView.addButtonClickListener(onButtonClickListener);
        return this;
    }

    public COUIGuideDialogBuilder setBackgroundDrawable(Drawable drawable) {
        this.mCouiAlertDialogBuilder.setBackgroundDrawable(drawable);
        return this;
    }

    public COUIGuideDialogBuilder setBlurBackgroundDrawable(boolean z2) {
        this.mCouiAlertDialogBuilder.setBlurBackgroundDrawable(z2, UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN);
        return this;
    }

    public COUIGuideDialogBuilder setButtonText(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3) {
        this.mContentView.setButtonText(charSequence, charSequence2, charSequence3);
        return this;
    }

    public COUIGuideDialogBuilder setGuidePages(List<COUIGuidePageItem> list) {
        this.mContentView.setGuidePages(list);
        return this;
    }

    public COUIGuideDialogBuilder setImageHeightStyle(COUIGuideLayoutContentView.ImageHeightStyle imageHeightStyle) {
        this.mContentView.setImageHeightStyle(imageHeightStyle);
        return this;
    }

    public COUIGuideDialogBuilder setImagePadding(int i3, int i4, int i5, int i6) {
        this.mContentView.setImagePadding(i3, i4, i5, i6);
        return this;
    }

    public AlertDialog show() {
        AlertDialog alertDialogCreate = this.mCouiAlertDialogBuilder.create();
        this.mAlertDialog = alertDialogCreate;
        alertDialogCreate.setCanceledOnTouchOutside(false);
        this.mAlertDialog.show();
        this.mCouiAlertDialogBuilder.updateViewAfterShown();
        View viewFindViewById = this.mAlertDialog.findViewById(R.id.custom);
        if (viewFindViewById != null) {
            viewFindViewById.setPadding(viewFindViewById.getPaddingLeft(), 0, viewFindViewById.getPaddingRight(), viewFindViewById.getPaddingBottom());
        }
        if (this.mCouiAlertDialogBuilder.getBackgroundBlurBuilder() != null) {
            this.mCouiAlertDialogBuilder.setBlurryStateChangeListener(new e(this));
        }
        return this.mAlertDialog;
    }

    private COUIGuideDialogBuilder(Context context, int i3) {
        this.mCouiAlertDialogBuilder = null;
        this.mContext = context;
        this.mCouiAlertDialogBuilder = new COUIAlertDialogBuilder(context, i3);
        COUIGuideLayoutContentView cOUIGuideLayoutContentView = new COUIGuideLayoutContentView(context);
        this.mContentView = cOUIGuideLayoutContentView;
        this.mCouiAlertDialogBuilder.setView(cOUIGuideLayoutContentView);
    }

    public COUIGuideDialogBuilder setButtonText(CharSequence charSequence) {
        this.mContentView.setButtonText(charSequence);
        return this;
    }
}