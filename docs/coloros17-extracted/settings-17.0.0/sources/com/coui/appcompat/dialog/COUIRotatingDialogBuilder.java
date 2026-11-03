package com.coui.appcompat.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.coui.appcompat.textutil.COUIChangeTextUtil;
import com.oplus.anim.EffectiveAnimationView;
import com.support.dialog.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIRotatingDialogBuilder {
    private AlertDialog mAlertDialog;
    private int mAnimationViewMaxSize;
    private Drawable mBackgroundDrawable;
    private boolean mBlurBackgroundWindow;
    private String mCancelButton;
    private DialogInterface.OnClickListener mCancelClickListener;
    private Context mContext;
    private String mDialogTitle;
    private DialogInterface.OnDismissListener mDismissListener;
    private String mFileName;
    private String mMessageText;
    private TextView mMessageView;
    private int mRawResource;
    private int mRepeatCount;
    private EffectiveAnimationView mRotatingAnimationView;
    private COUIAlertDialogBuilder mRotatingDialogBuilder;
    private DialogInterface.OnShowListener mShowListener;
    private int mStyle;

    /* JADX INFO: renamed from: com.coui.appcompat.dialog.COUIRotatingDialogBuilder$1, reason: invalid class name */
    public class AnonymousClass1 implements DialogInterface.OnShowListener {
        public AnonymousClass1() {
        }

        @Override // android.content.DialogInterface.OnShowListener
        public void onShow(DialogInterface dialogInterface) {
            COUIRotatingDialogBuilder.access$100(COUIRotatingDialogBuilder.this).setRepeatCount(COUIRotatingDialogBuilder.access$000(COUIRotatingDialogBuilder.this));
            COUIRotatingDialogBuilder.access$100(COUIRotatingDialogBuilder.this).playAnimation();
            if (COUIRotatingDialogBuilder.access$200(COUIRotatingDialogBuilder.this) != null) {
                COUIRotatingDialogBuilder.access$200(COUIRotatingDialogBuilder.this).onShow(dialogInterface);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.dialog.COUIRotatingDialogBuilder$2, reason: invalid class name */
    public class AnonymousClass2 implements DialogInterface.OnDismissListener {
        public AnonymousClass2() {
        }

        @Override // android.content.DialogInterface.OnDismissListener
        public void onDismiss(DialogInterface dialogInterface) {
            COUIRotatingDialogBuilder.access$100(COUIRotatingDialogBuilder.this).pauseAnimation();
            if (COUIRotatingDialogBuilder.access$300(COUIRotatingDialogBuilder.this) != null) {
                COUIRotatingDialogBuilder.access$300(COUIRotatingDialogBuilder.this).onDismiss(dialogInterface);
            }
        }
    }

    public COUIRotatingDialogBuilder(Context context) {
        this(context, null);
    }

    public static /* synthetic */ int access$000(COUIRotatingDialogBuilder cOUIRotatingDialogBuilder) {
        return cOUIRotatingDialogBuilder.mRepeatCount;
    }

    public static /* synthetic */ EffectiveAnimationView access$100(COUIRotatingDialogBuilder cOUIRotatingDialogBuilder) {
        return cOUIRotatingDialogBuilder.mRotatingAnimationView;
    }

    public static /* synthetic */ DialogInterface.OnShowListener access$200(COUIRotatingDialogBuilder cOUIRotatingDialogBuilder) {
        return cOUIRotatingDialogBuilder.mShowListener;
    }

    public static /* synthetic */ DialogInterface.OnDismissListener access$300(COUIRotatingDialogBuilder cOUIRotatingDialogBuilder) {
        return cOUIRotatingDialogBuilder.mDismissListener;
    }

    private void addOnWindowAttachListener(AlertDialog alertDialog) {
        View decorView = alertDialog.getWindow().getDecorView();
        this.mRotatingAnimationView = (EffectiveAnimationView) decorView.findViewById(R.id.progress);
        TextView textView = (TextView) decorView.findViewById(R.id.progress_tips);
        this.mMessageView = textView;
        String str = this.mMessageText;
        if (str != null) {
            textView.setText(str);
            COUIChangeTextUtil.adaptFontSize(this.mMessageView, 4);
        }
        EffectiveAnimationView effectiveAnimationView = this.mRotatingAnimationView;
        if (effectiveAnimationView != null) {
            int i3 = this.mRawResource;
            if (i3 != -1 && this.mFileName != null) {
                throw new IllegalArgumentException("mRawResource and mFileName cannot be used at the same time. Please use only one at once.");
            }
            if (i3 != -1) {
                effectiveAnimationView.setAnimation(i3);
                if (hasButton()) {
                    return;
                }
                setLayoutParamsMaxSize(this.mRotatingAnimationView);
                return;
            }
            String str2 = this.mFileName;
            if (str2 != null) {
                effectiveAnimationView.setAnimation(str2);
                if (hasButton()) {
                    return;
                }
                setLayoutParamsMaxSize(this.mRotatingAnimationView);
            }
        }
    }

    private boolean hasButton() {
        return this.mCancelButton != null;
    }

    public AlertDialog create() {
        if (this.mRotatingDialogBuilder == null) {
            if (this.mStyle == 0) {
                if (this.mCancelButton == null) {
                    this.mStyle = R.style.COUIAlertDialog_Rotating;
                } else {
                    this.mStyle = R.style.COUIAlertDialog_Rotating_Cancelable;
                }
            }
            COUIAlertDialogBuilder cOUIAlertDialogBuilder = new COUIAlertDialogBuilder(this.mContext, this.mStyle);
            this.mRotatingDialogBuilder = cOUIAlertDialogBuilder;
            String str = this.mCancelButton;
            if (str != null) {
                cOUIAlertDialogBuilder.setNegativeButton((CharSequence) str, this.mCancelClickListener);
            }
            String str2 = this.mDialogTitle;
            if (str2 != null) {
                this.mRotatingDialogBuilder.setTitle((CharSequence) str2);
            }
            Drawable drawable = this.mBackgroundDrawable;
            if (drawable != null) {
                this.mRotatingDialogBuilder.setBackgroundDrawable(drawable);
            }
            this.mRotatingDialogBuilder.setBlurBackgroundDrawable(this.mBlurBackgroundWindow);
            AlertDialog alertDialogCreate = this.mRotatingDialogBuilder.create();
            this.mAlertDialog = alertDialogCreate;
            alertDialogCreate.setOnShowListener(new AnonymousClass1());
            this.mAlertDialog.setOnDismissListener(new AnonymousClass2());
        }
        return this.mAlertDialog;
    }

    public EffectiveAnimationView getAnimationView() {
        return this.mRotatingAnimationView;
    }

    public TextView getMessageView() {
        return this.mMessageView;
    }

    public COUIRotatingDialogBuilder setBackgroundDrawable(Drawable drawable) {
        this.mBackgroundDrawable = drawable;
        return this;
    }

    public COUIRotatingDialogBuilder setBlurBackgroundWindow(boolean z2) {
        this.mBlurBackgroundWindow = z2;
        return this;
    }

    public COUIRotatingDialogBuilder setCancelButton(int i3, DialogInterface.OnClickListener onClickListener) {
        setCancelButton(this.mContext.getString(i3), onClickListener);
        return this;
    }

    public COUIRotatingDialogBuilder setDialogTitle(int i3) {
        setDialogTitle(this.mContext.getString(i3));
        return this;
    }

    public COUIRotatingDialogBuilder setDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.mDismissListener = onDismissListener;
        return this;
    }

    public COUIRotatingDialogBuilder setFileName(String str) {
        this.mFileName = str;
        return this;
    }

    public COUIRotatingDialogBuilder setLayoutParamsMaxSize(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        int i3 = this.mAnimationViewMaxSize;
        layoutParams.width = i3;
        layoutParams.height = i3;
        view.setLayoutParams(layoutParams);
        return this;
    }

    @Deprecated
    public COUIRotatingDialogBuilder setLayoutParamsWrapContent(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = -2;
        layoutParams.height = -2;
        view.setLayoutParams(layoutParams);
        return this;
    }

    public COUIRotatingDialogBuilder setRawResource(int i3) {
        this.mRawResource = i3;
        return this;
    }

    public COUIRotatingDialogBuilder setRepeatCount(int i3) {
        this.mRepeatCount = i3;
        return this;
    }

    public COUIRotatingDialogBuilder setShowListener(DialogInterface.OnShowListener onShowListener) {
        this.mShowListener = onShowListener;
        return this;
    }

    public void setStyle(int i3) {
        this.mStyle = i3;
    }

    public AlertDialog show() {
        AlertDialog alertDialogCreate = create();
        alertDialogCreate.show();
        this.mRotatingDialogBuilder.updateViewAfterShown();
        addOnWindowAttachListener(alertDialogCreate);
        return alertDialogCreate;
    }

    public COUIRotatingDialogBuilder(Context context, String str) {
        this.mRepeatCount = -1;
        this.mRawResource = -1;
        this.mFileName = null;
        this.mStyle = 0;
        this.mBlurBackgroundWindow = false;
        this.mContext = context;
        this.mMessageText = str;
        this.mAnimationViewMaxSize = context.getResources().getDimensionPixelSize(R.dimen.coui_spinner_loading_height);
    }

    public COUIRotatingDialogBuilder setCancelButton(String str, DialogInterface.OnClickListener onClickListener) {
        this.mCancelButton = str;
        this.mCancelClickListener = onClickListener;
        return this;
    }

    public COUIRotatingDialogBuilder setDialogTitle(String str) {
        this.mDialogTitle = str;
        return this;
    }
}