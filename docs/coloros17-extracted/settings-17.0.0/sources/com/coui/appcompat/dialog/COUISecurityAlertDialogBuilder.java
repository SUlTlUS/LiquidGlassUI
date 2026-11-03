package com.coui.appcompat.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;
import androidx.appcompat.app.AlertDialog;
import com.coui.appcompat.checkbox.COUICheckBox;
import com.coui.appcompat.clickablespan.COUIClickableSpan;
import com.coui.appcompat.textutil.COUIChangeTextUtil;
import com.support.dialog.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUISecurityAlertDialogBuilder extends COUIAlertDialogBuilder {
    private static final int DELAY = 70;
    private static final String TAG = "COUISecurityBuilder";
    private CharSequence mCheckBoxString;
    private Context mContext;
    private AlertDialog mDialog;
    private boolean mHasCheckBox;
    private boolean mIsChecked;
    private boolean mIsShowStatementText;
    private int mLinkId;
    private DialogInterface.OnKeyListener mOnKeyListener;
    private OnLinkTextClickListener mOnLinkTextClickListener;
    private OnSelectedListener mOnSelectedListener;
    private OnBackInvokedCallback mPredictiveBackCallback;
    private int mStatementId;

    /* JADX INFO: renamed from: com.coui.appcompat.dialog.COUISecurityAlertDialogBuilder$1, reason: invalid class name */
    public class AnonymousClass1 implements DialogInterface.OnKeyListener {
        public AnonymousClass1() {
        }

        @Override // android.content.DialogInterface.OnKeyListener
        public boolean onKey(DialogInterface dialogInterface, int i3, KeyEvent keyEvent) {
            if (i3 != 4) {
                return false;
            }
            if (keyEvent.getAction() == 1 && COUISecurityAlertDialogBuilder.access$000(COUISecurityAlertDialogBuilder.this) != null && COUISecurityAlertDialogBuilder.access$000(COUISecurityAlertDialogBuilder.this).isShowing() && COUISecurityAlertDialogBuilder.access$100(COUISecurityAlertDialogBuilder.this) != null) {
                COUISecurityAlertDialogBuilder.access$100(COUISecurityAlertDialogBuilder.this).onSelected(-2, COUISecurityAlertDialogBuilder.access$200(COUISecurityAlertDialogBuilder.this));
            }
            return true;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.dialog.COUISecurityAlertDialogBuilder$2, reason: invalid class name */
    public class AnonymousClass2 implements View.OnAttachStateChangeListener {
        public AnonymousClass2() {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            COUISecurityAlertDialogBuilder.access$300(COUISecurityAlertDialogBuilder.this);
            view.removeOnAttachStateChangeListener(this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.dialog.COUISecurityAlertDialogBuilder$3, reason: invalid class name */
    public class AnonymousClass3 implements COUIClickableSpan.SpannableStrClickListener {
        public AnonymousClass3() {
        }

        @Override // com.coui.appcompat.clickablespan.COUIClickableSpan.SpannableStrClickListener
        public void onClick() {
            if (COUISecurityAlertDialogBuilder.access$400(COUISecurityAlertDialogBuilder.this) != null) {
                COUISecurityAlertDialogBuilder.access$400(COUISecurityAlertDialogBuilder.this).onLinkTextClick();
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.dialog.COUISecurityAlertDialogBuilder$4, reason: invalid class name */
    public class AnonymousClass4 implements View.OnTouchListener {
        final /* synthetic */ int val$termsIndex;
        final /* synthetic */ int val$termsLength;

        public AnonymousClass4(int i3, int i4) {
            this.val$termsIndex = i3;
            this.val$termsLength = i4;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (!(view instanceof TextView)) {
                return false;
            }
            int actionMasked = motionEvent.getActionMasked();
            int offsetForPosition = ((TextView) view).getOffsetForPosition(motionEvent.getX(), motionEvent.getY());
            int i3 = this.val$termsIndex;
            boolean z2 = offsetForPosition <= i3 || offsetForPosition >= i3 + this.val$termsLength;
            if (actionMasked != 0) {
                if (actionMasked == 1 || actionMasked == 3) {
                    view.setPressed(false);
                    view.postInvalidateDelayed(70L);
                }
            } else {
                if (z2) {
                    return true;
                }
                view.setPressed(true);
                view.invalidate();
            }
            return false;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.dialog.COUISecurityAlertDialogBuilder$5, reason: invalid class name */
    public class AnonymousClass5 implements COUICheckBox.OnStateChangeListener {
        public AnonymousClass5() {
        }

        @Override // com.coui.appcompat.checkbox.COUICheckBox.OnStateChangeListener
        public void onStateChanged(COUICheckBox cOUICheckBox, int i3) {
            COUISecurityAlertDialogBuilder.access$202(COUISecurityAlertDialogBuilder.this, i3 == 2);
            if (COUISecurityAlertDialogBuilder.access$100(COUISecurityAlertDialogBuilder.this) != null) {
                COUISecurityAlertDialogBuilder.access$100(COUISecurityAlertDialogBuilder.this).onSelected(0, COUISecurityAlertDialogBuilder.access$200(COUISecurityAlertDialogBuilder.this));
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.dialog.COUISecurityAlertDialogBuilder$6, reason: invalid class name */
    public class AnonymousClass6 implements DialogInterface.OnClickListener {
        public AnonymousClass6() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i3) {
            if (COUISecurityAlertDialogBuilder.access$100(COUISecurityAlertDialogBuilder.this) != null) {
                COUISecurityAlertDialogBuilder.access$100(COUISecurityAlertDialogBuilder.this).onSelected(i3, COUISecurityAlertDialogBuilder.access$200(COUISecurityAlertDialogBuilder.this));
            }
        }
    }

    public interface OnLinkTextClickListener {
        void onLinkTextClick();
    }

    public interface OnSelectedListener {
        void onSelected(int i3, boolean z2);
    }

    public COUISecurityAlertDialogBuilder(Context context) {
        super(context, R.style.COUIAlertDialog_Security_Bottom);
        this.mHasCheckBox = true;
        this.mOnKeyListener = new AnonymousClass1();
        this.mContext = context;
        init();
    }

    public static /* synthetic */ AlertDialog access$000(COUISecurityAlertDialogBuilder cOUISecurityAlertDialogBuilder) {
        return cOUISecurityAlertDialogBuilder.mDialog;
    }

    public static /* synthetic */ OnSelectedListener access$100(COUISecurityAlertDialogBuilder cOUISecurityAlertDialogBuilder) {
        return cOUISecurityAlertDialogBuilder.mOnSelectedListener;
    }

    public static /* synthetic */ boolean access$200(COUISecurityAlertDialogBuilder cOUISecurityAlertDialogBuilder) {
        return cOUISecurityAlertDialogBuilder.mIsChecked;
    }

    public static /* synthetic */ boolean access$202(COUISecurityAlertDialogBuilder cOUISecurityAlertDialogBuilder, boolean z2) {
        cOUISecurityAlertDialogBuilder.mIsChecked = z2;
        return z2;
    }

    public static /* synthetic */ void access$300(COUISecurityAlertDialogBuilder cOUISecurityAlertDialogBuilder) {
        cOUISecurityAlertDialogBuilder.unregisterPredictiveBackCallback();
    }

    public static /* synthetic */ OnLinkTextClickListener access$400(COUISecurityAlertDialogBuilder cOUISecurityAlertDialogBuilder) {
        return cOUISecurityAlertDialogBuilder.mOnLinkTextClickListener;
    }

    public static /* synthetic */ void b(COUISecurityAlertDialogBuilder cOUISecurityAlertDialogBuilder) {
        OnSelectedListener onSelectedListener;
        AlertDialog alertDialog = cOUISecurityAlertDialogBuilder.mDialog;
        if (alertDialog == null || !alertDialog.isShowing() || (onSelectedListener = cOUISecurityAlertDialogBuilder.mOnSelectedListener) == null) {
            return;
        }
        onSelectedListener.onSelected(-2, cOUISecurityAlertDialogBuilder.mIsChecked);
    }

    private void configureDefaultPredictiveBack() {
        OnBackInvokedDispatcher onBackInvokedDispatcher;
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog == null || !alertDialog.isShowing() || this.mOnSelectedListener == null) {
            return;
        }
        unregisterPredictiveBackCallback();
        Window window = this.mDialog.getWindow();
        View decorView = window != null ? window.getDecorView() : null;
        if (decorView == null || (onBackInvokedDispatcher = this.mDialog.getOnBackInvokedDispatcher()) == null) {
            return;
        }
        this.mDialog.setCancelable(false);
        j jVar = new j(this);
        this.mPredictiveBackCallback = jVar;
        try {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(0, jVar);
            decorView.addOnAttachStateChangeListener(new AnonymousClass2());
        } catch (Exception e3) {
            Log.e(TAG, "registerOnBackInvokedCallback fail,e:" + e3.getMessage());
            this.mPredictiveBackCallback = null;
        }
    }

    private DialogInterface.OnClickListener getDefaultButtonClickListener() {
        return new AnonymousClass6();
    }

    private SpannableStringBuilder getStatementStringBuilder(String str, int i3, int i4) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
        COUIClickableSpan cOUIClickableSpan = new COUIClickableSpan(this.mContext);
        cOUIClickableSpan.setStatusBarClickListener(new AnonymousClass3());
        spannableStringBuilder.setSpan(cOUIClickableSpan, i3, i4 + i3, 33);
        return spannableStringBuilder;
    }

    private View.OnTouchListener getStatementTextTouchListener(int i3, int i4) {
        return new AnonymousClass4(i3, i4);
    }

    private void init() {
        this.mCheckBoxString = this.mContext.getString(R.string.coui_security_alertdialog_checkbox_msg);
    }

    private void initCheckBox() {
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog == null) {
            return;
        }
        View viewFindViewById = alertDialog.findViewById(R.id.coui_security_alert_dialog_checkbox);
        if (viewFindViewById instanceof COUICheckBox) {
            if (!this.mHasCheckBox) {
                viewFindViewById.setVisibility(8);
                return;
            }
            viewFindViewById.setVisibility(0);
            COUICheckBox cOUICheckBox = (COUICheckBox) viewFindViewById;
            cOUICheckBox.setChecked(this.mIsChecked);
            cOUICheckBox.setText(this.mCheckBoxString);
            cOUICheckBox.setTextSize(0, COUIChangeTextUtil.getSuitableFontSize(this.mContext.getResources().getDimensionPixelSize(R.dimen.coui_security_alert_dialog_checkbox_text_size), this.mContext.getResources().getConfiguration().fontScale, 5));
            cOUICheckBox.setOnStateChangeListener(new AnonymousClass5());
        }
    }

    private void initMessageText() {
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog == null) {
            return;
        }
        View viewFindViewById = alertDialog.findViewById(android.R.id.message);
        if (viewFindViewById instanceof TextView) {
            ((TextView) viewFindViewById).setTextSize(0, (int) COUIChangeTextUtil.getSuitableFontSize(this.mContext.getResources().getDimensionPixelSize(R.dimen.coui_alert_dialog_builder_message_text_size), this.mContext.getResources().getConfiguration().fontScale, 5));
        }
    }

    private void initStatementText() {
        TextView textView;
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog == null || (textView = (TextView) alertDialog.findViewById(R.id.coui_security_alertdialog_statement)) == null) {
            return;
        }
        if (!this.mIsShowStatementText) {
            textView.setVisibility(8);
            return;
        }
        int i3 = this.mLinkId;
        String string = i3 <= 0 ? this.mContext.getString(R.string.coui_security_alertdailog_privacy) : this.mContext.getString(i3);
        int i4 = this.mStatementId;
        String string2 = i4 <= 0 ? this.mContext.getString(R.string.coui_security_alertdailog_statement, string) : this.mContext.getString(i4, string);
        int iIndexOf = string2.indexOf(string);
        int length = string.length();
        textView.setVisibility(0);
        textView.setHighlightColor(0);
        textView.setText(getStatementStringBuilder(string2, iIndexOf, length));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setOnTouchListener(getStatementTextTouchListener(iIndexOf, length));
    }

    private void initView() {
        initMessageText();
        initStatementText();
        initCheckBox();
    }

    private void unregisterPredictiveBackCallback() {
        AlertDialog alertDialog;
        if (this.mPredictiveBackCallback == null || (alertDialog = this.mDialog) == null) {
            return;
        }
        OnBackInvokedDispatcher onBackInvokedDispatcher = alertDialog.getOnBackInvokedDispatcher();
        if (onBackInvokedDispatcher != null) {
            try {
                onBackInvokedDispatcher.unregisterOnBackInvokedCallback(this.mPredictiveBackCallback);
            } catch (Exception e3) {
                Log.e(TAG, "unregisterOnBackInvokedCallback fail,e:" + e3.getMessage());
            }
        }
        this.mPredictiveBackCallback = null;
    }

    @Override // com.coui.appcompat.dialog.COUIAlertDialogBuilder, androidx.appcompat.app.AlertDialog.Builder
    public AlertDialog create() {
        super.setOnKeyListener(this.mOnKeyListener);
        AlertDialog alertDialogCreate = super.create();
        this.mDialog = alertDialogCreate;
        return alertDialogCreate;
    }

    public COUISecurityAlertDialogBuilder setCheckBoxString(CharSequence charSequence) {
        this.mCheckBoxString = charSequence;
        return this;
    }

    public COUISecurityAlertDialogBuilder setChecked(boolean z2) {
        this.mIsChecked = z2;
        return this;
    }

    public COUISecurityAlertDialogBuilder setHasCheckBox(boolean z2) {
        this.mHasCheckBox = z2;
        return this;
    }

    public COUISecurityAlertDialogBuilder setNegativeString(CharSequence charSequence) {
        super.setNegativeButton(charSequence, getDefaultButtonClickListener());
        return this;
    }

    public COUISecurityAlertDialogBuilder setNeutralString(CharSequence charSequence) {
        super.setNeutralButton(charSequence, getDefaultButtonClickListener());
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public /* bridge */ /* synthetic */ AlertDialog.Builder setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        return setOnKeyListener(onKeyListener);
    }

    public COUISecurityAlertDialogBuilder setOnLinkTextClickListener(OnLinkTextClickListener onLinkTextClickListener) {
        this.mOnLinkTextClickListener = onLinkTextClickListener;
        return this;
    }

    public COUISecurityAlertDialogBuilder setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.mOnSelectedListener = onSelectedListener;
        return this;
    }

    public COUISecurityAlertDialogBuilder setPositiveString(CharSequence charSequence) {
        super.setPositiveButton(charSequence, getDefaultButtonClickListener());
        return this;
    }

    public COUISecurityAlertDialogBuilder setShowStatementText(boolean z2) {
        this.mIsShowStatementText = z2;
        return this;
    }

    public COUISecurityAlertDialogBuilder setStatementLinkString(int i3, int i4) {
        if (i3 <= 0) {
            this.mStatementId = -1;
        } else {
            String string = this.mContext.getString(i3);
            if (TextUtils.isEmpty(string) || !(string.contains("%1$s") || string.contains("%s"))) {
                this.mStatementId = -1;
            } else {
                this.mStatementId = i3;
            }
        }
        this.mLinkId = i4;
        return this;
    }

    @Override // com.coui.appcompat.dialog.COUIAlertDialogBuilder, androidx.appcompat.app.AlertDialog.Builder
    public AlertDialog show() {
        this.mDialog = super.show();
        initView();
        return this.mDialog;
    }

    @Override // com.coui.appcompat.dialog.COUIAlertDialogBuilder
    public void updateViewAfterShown() {
        super.updateViewAfterShown();
        initView();
        configureDefaultPredictiveBack();
    }

    public COUISecurityAlertDialogBuilder setCheckBoxString(int i3) {
        this.mCheckBoxString = this.mContext.getString(i3);
        return this;
    }

    public COUISecurityAlertDialogBuilder setNegativeString(int i3) {
        super.setNegativeButton(i3, getDefaultButtonClickListener());
        return this;
    }

    public COUISecurityAlertDialogBuilder setNeutralString(int i3) {
        super.setNeutralButton(i3, getDefaultButtonClickListener());
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public COUISecurityAlertDialogBuilder setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        this.mOnKeyListener = onKeyListener;
        super.setOnKeyListener(onKeyListener);
        return this;
    }

    public COUISecurityAlertDialogBuilder setPositiveString(int i3) {
        super.setPositiveButton(i3, getDefaultButtonClickListener());
        return this;
    }

    public COUISecurityAlertDialogBuilder(Context context, int i3) {
        super(context, i3, R.style.COUIAlertDialog_Security_Bottom);
        this.mHasCheckBox = true;
        this.mOnKeyListener = new AnonymousClass1();
        this.mContext = context;
        init();
    }

    public COUISecurityAlertDialogBuilder(Context context, int i3, int i4) {
        super(context, i3, i4);
        this.mHasCheckBox = true;
        this.mOnKeyListener = new AnonymousClass1();
        this.mContext = context;
        init();
    }
}