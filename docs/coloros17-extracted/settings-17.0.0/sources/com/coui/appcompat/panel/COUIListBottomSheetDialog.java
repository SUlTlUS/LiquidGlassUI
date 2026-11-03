package com.coui.appcompat.panel;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.COUIPanelPreferenceLinearLayoutManager;
import androidx.recyclerview.widget.COUIRecyclerView;
import com.coui.appcompat.toolbar.COUIToolbar;
import com.support.panel.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIListBottomSheetDialog {
    private COUIBottomSheetDialog mBottomSheetDialog;

    public static class Builder extends AlertDialog.Builder {
        private View.OnClickListener mCenterButtonClickListener;
        private String mCenterButtonText;
        public int mCheckedItem;
        public boolean[] mCheckedItems;
        private Context mContext;

        @Deprecated
        private int mFinalNavColorAfterDismiss;

        @Deprecated
        private boolean mIsExecuteNavColorAnimAfterDismiss;
        private boolean mIsMultiChoice;
        private CharSequence[] mItems;
        private View mLayout;
        private View.OnClickListener mLeftButtonClickListener;
        private String mLeftButtonText;
        private COUIListBottomSheetDialog mListBottomSheetDialog;
        public DialogInterface.OnMultiChoiceClickListener mOnCheckboxClickListener;
        public OnMenuItemClickListener mOnMenuItemClick;
        public DialogInterface.OnClickListener mOnRadioButtonClickListener;
        private View.OnClickListener mRightButtonClickListener;
        private String mRightButtonText;
        private CharSequence[] mSummaries;
        private CharSequence mTitle;

        /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIListBottomSheetDialog$Builder$1, reason: invalid class name */
        public class AnonymousClass1 implements COUIBottomSheetChoiceListAdapter.OnItemClickListener {
            public AnonymousClass1() {
            }

            @Override // com.coui.appcompat.panel.COUIBottomSheetChoiceListAdapter.OnItemClickListener
            public void onItemClick(View view, int i3, int i4) {
                if (Builder.access$100(Builder.this)) {
                    Builder builder = Builder.this;
                    DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener = builder.mOnCheckboxClickListener;
                    if (onMultiChoiceClickListener != null) {
                        onMultiChoiceClickListener.onClick(COUIListBottomSheetDialog.access$000(Builder.access$200(builder)), i3, i4 == 2);
                        return;
                    }
                    return;
                }
                Builder builder2 = Builder.this;
                DialogInterface.OnClickListener onClickListener = builder2.mOnRadioButtonClickListener;
                if (onClickListener != null) {
                    onClickListener.onClick(COUIListBottomSheetDialog.access$000(Builder.access$200(builder2)), i3);
                }
            }
        }

        public Builder(Context context) {
            super(context);
            this.mCheckedItem = -1;
            this.mListBottomSheetDialog = new COUIListBottomSheetDialog();
            this.mIsMultiChoice = false;
            init(context);
        }

        public static /* synthetic */ boolean access$100(Builder builder) {
            return builder.mIsMultiChoice;
        }

        public static /* synthetic */ COUIListBottomSheetDialog access$200(Builder builder) {
            return builder.mListBottomSheetDialog;
        }

        private void init(Context context) {
            this.mContext = context;
            this.mLayout = LayoutInflater.from(context).inflate(R.layout.coui_list_bottom_sheet_dialog_layout, (ViewGroup) null);
        }

        public COUIListBottomSheetDialog createDialog() {
            COUIBottomSheetChoiceListAdapter cOUIBottomSheetChoiceListAdapter;
            COUIListBottomSheetDialog.access$002(this.mListBottomSheetDialog, new COUIBottomSheetDialog(this.mContext, R.style.DefaultBottomSheetDialog));
            COUIListBottomSheetDialog.access$000(this.mListBottomSheetDialog).setContentView(this.mLayout);
            COUIRecyclerView cOUIRecyclerView = (COUIRecyclerView) COUIListBottomSheetDialog.access$000(this.mListBottomSheetDialog).findViewById(R.id.select_dialog_listview);
            COUIPanelPreferenceLinearLayoutManager cOUIPanelPreferenceLinearLayoutManager = new COUIPanelPreferenceLinearLayoutManager(this.mContext);
            cOUIPanelPreferenceLinearLayoutManager.setOrientation(1);
            cOUIRecyclerView.setLayoutManager(cOUIPanelPreferenceLinearLayoutManager);
            cOUIRecyclerView.setItemAnimator(null);
            COUIToolbar cOUIToolbar = (COUIToolbar) COUIListBottomSheetDialog.access$000(this.mListBottomSheetDialog).findViewById(R.id.toolbar);
            cOUIToolbar.setTitle(this.mTitle);
            cOUIToolbar.setIsTitleCenterStyle(true);
            if (this.mIsMultiChoice) {
                ((LinearLayout.LayoutParams) cOUIRecyclerView.getLayoutParams()).bottomMargin = 0;
                cOUIBottomSheetChoiceListAdapter = new COUIBottomSheetChoiceListAdapter(this.mContext, com.support.dialog.R.layout.coui_select_dialog_multichoice, this.mItems, this.mSummaries, -1, this.mCheckedItems, true);
            } else {
                cOUIBottomSheetChoiceListAdapter = new COUIBottomSheetChoiceListAdapter(this.mContext, com.support.dialog.R.layout.coui_select_dialog_singlechoice, this.mItems, this.mSummaries, this.mCheckedItem);
            }
            COUIListBottomSheetDialog.access$000(this.mListBottomSheetDialog).getDragableLinearLayout().getDragView().setVisibility(4);
            cOUIRecyclerView.setAdapter(cOUIBottomSheetChoiceListAdapter);
            cOUIBottomSheetChoiceListAdapter.setOnItemClickListener(new AnonymousClass1());
            return this.mListBottomSheetDialog;
        }

        public Dialog getBottomSheetDialog() {
            return COUIListBottomSheetDialog.access$000(this.mListBottomSheetDialog);
        }

        public Builder setCenterButton(String str, View.OnClickListener onClickListener) {
            this.mCenterButtonText = str;
            this.mCenterButtonClickListener = onClickListener;
            return this;
        }

        @Deprecated
        public Builder setExecuteNavColorAnimAfterDismiss(boolean z2) {
            this.mIsExecuteNavColorAnimAfterDismiss = z2;
            return this;
        }

        @Deprecated
        public Builder setFinalNavColorAfterDismiss(int i3) {
            this.mFinalNavColorAfterDismiss = i3;
            return this;
        }

        public Builder setLeftButton(String str, View.OnClickListener onClickListener) {
            this.mLeftButtonText = str;
            this.mLeftButtonClickListener = onClickListener;
            return this;
        }

        @Deprecated
        public Builder setMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
            this.mOnMenuItemClick = onMenuItemClickListener;
            return this;
        }

        @Override // androidx.appcompat.app.AlertDialog.Builder
        public /* bridge */ /* synthetic */ AlertDialog.Builder setMultiChoiceItems(int i3, boolean[] zArr, DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener) {
            return setMultiChoiceItems(i3, zArr, onMultiChoiceClickListener);
        }

        public Builder setRightButton(String str, View.OnClickListener onClickListener) {
            this.mRightButtonText = str;
            this.mRightButtonClickListener = onClickListener;
            return this;
        }

        @Override // androidx.appcompat.app.AlertDialog.Builder
        public /* bridge */ /* synthetic */ AlertDialog.Builder setSingleChoiceItems(int i3, int i4, DialogInterface.OnClickListener onClickListener) {
            return setSingleChoiceItems(i3, i4, onClickListener);
        }

        public Builder setSummaries(int i3) {
            this.mSummaries = this.mContext.getResources().getTextArray(i3);
            return this;
        }

        @Override // androidx.appcompat.app.AlertDialog.Builder
        public /* bridge */ /* synthetic */ AlertDialog.Builder setTitle(int i3) {
            return setTitle(i3);
        }

        @Override // androidx.appcompat.app.AlertDialog.Builder
        public /* bridge */ /* synthetic */ AlertDialog.Builder setMultiChoiceItems(CharSequence[] charSequenceArr, boolean[] zArr, DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener) {
            return setMultiChoiceItems(charSequenceArr, zArr, onMultiChoiceClickListener);
        }

        @Override // androidx.appcompat.app.AlertDialog.Builder
        public /* bridge */ /* synthetic */ AlertDialog.Builder setSingleChoiceItems(CharSequence[] charSequenceArr, int i3, DialogInterface.OnClickListener onClickListener) {
            return setSingleChoiceItems(charSequenceArr, i3, onClickListener);
        }

        public Builder setSummaries(CharSequence[] charSequenceArr) {
            this.mSummaries = charSequenceArr;
            return this;
        }

        @Override // androidx.appcompat.app.AlertDialog.Builder
        public /* bridge */ /* synthetic */ AlertDialog.Builder setTitle(CharSequence charSequence) {
            return setTitle(charSequence);
        }

        @Override // androidx.appcompat.app.AlertDialog.Builder
        public Builder setMultiChoiceItems(int i3, boolean[] zArr, DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener) {
            this.mItems = this.mContext.getResources().getTextArray(i3);
            this.mCheckedItems = zArr;
            this.mIsMultiChoice = true;
            this.mOnCheckboxClickListener = onMultiChoiceClickListener;
            return this;
        }

        @Override // androidx.appcompat.app.AlertDialog.Builder
        public Builder setSingleChoiceItems(int i3, int i4, DialogInterface.OnClickListener onClickListener) {
            this.mItems = this.mContext.getResources().getTextArray(i3);
            this.mOnRadioButtonClickListener = onClickListener;
            this.mCheckedItem = i4;
            this.mIsMultiChoice = false;
            return this;
        }

        @Override // androidx.appcompat.app.AlertDialog.Builder
        public Builder setTitle(CharSequence charSequence) {
            this.mTitle = charSequence;
            return this;
        }

        @Override // androidx.appcompat.app.AlertDialog.Builder
        public Builder setTitle(int i3) {
            this.mTitle = this.mContext.getString(i3);
            return this;
        }

        public Builder(Context context, int i3) {
            super(context, i3);
            this.mCheckedItem = -1;
            this.mListBottomSheetDialog = new COUIListBottomSheetDialog();
            this.mIsMultiChoice = false;
            init(new ContextThemeWrapper(context, i3));
        }

        @Override // androidx.appcompat.app.AlertDialog.Builder
        public Builder setMultiChoiceItems(CharSequence[] charSequenceArr, boolean[] zArr, DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener) {
            this.mItems = charSequenceArr;
            this.mCheckedItems = zArr;
            this.mIsMultiChoice = true;
            this.mOnCheckboxClickListener = onMultiChoiceClickListener;
            return this;
        }

        @Override // androidx.appcompat.app.AlertDialog.Builder
        public Builder setSingleChoiceItems(CharSequence[] charSequenceArr, int i3, DialogInterface.OnClickListener onClickListener) {
            this.mItems = charSequenceArr;
            this.mOnRadioButtonClickListener = onClickListener;
            this.mCheckedItem = i3;
            this.mIsMultiChoice = false;
            return this;
        }

        public Builder setMultiChoiceItems(int i3, boolean[] zArr, int i4, DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener) {
            this.mItems = this.mContext.getResources().getTextArray(i3);
            this.mCheckedItems = zArr;
            this.mIsMultiChoice = true;
            this.mSummaries = this.mContext.getResources().getTextArray(i4);
            this.mOnCheckboxClickListener = onMultiChoiceClickListener;
            return this;
        }

        public Builder setSingleChoiceItems(int i3, int i4, int i5, DialogInterface.OnClickListener onClickListener) {
            this.mItems = this.mContext.getResources().getTextArray(i3);
            this.mOnRadioButtonClickListener = onClickListener;
            this.mCheckedItem = i4;
            this.mIsMultiChoice = false;
            this.mSummaries = this.mContext.getResources().getTextArray(i5);
            return this;
        }

        public Builder setMultiChoiceItems(CharSequence[] charSequenceArr, boolean[] zArr, CharSequence[] charSequenceArr2, DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener) {
            this.mItems = charSequenceArr;
            this.mCheckedItems = zArr;
            this.mIsMultiChoice = true;
            this.mSummaries = charSequenceArr2;
            this.mOnCheckboxClickListener = onMultiChoiceClickListener;
            return this;
        }

        public Builder setSingleChoiceItems(CharSequence[] charSequenceArr, int i3, CharSequence[] charSequenceArr2, DialogInterface.OnClickListener onClickListener) {
            this.mItems = charSequenceArr;
            this.mOnRadioButtonClickListener = onClickListener;
            this.mCheckedItem = i3;
            this.mIsMultiChoice = false;
            this.mSummaries = charSequenceArr2;
            return this;
        }
    }

    public interface OnMenuItemClickListener {
        void onCancelItemClick();

        void onSaveItemClick();
    }

    public static /* synthetic */ COUIBottomSheetDialog access$000(COUIListBottomSheetDialog cOUIListBottomSheetDialog) {
        return cOUIListBottomSheetDialog.mBottomSheetDialog;
    }

    public static /* synthetic */ COUIBottomSheetDialog access$002(COUIListBottomSheetDialog cOUIListBottomSheetDialog, COUIBottomSheetDialog cOUIBottomSheetDialog) {
        cOUIListBottomSheetDialog.mBottomSheetDialog = cOUIBottomSheetDialog;
        return cOUIBottomSheetDialog;
    }

    public void dismiss() {
        COUIBottomSheetDialog cOUIBottomSheetDialog = this.mBottomSheetDialog;
        if (cOUIBottomSheetDialog != null) {
            cOUIBottomSheetDialog.dismiss();
        }
    }

    public boolean isShowing() {
        COUIBottomSheetDialog cOUIBottomSheetDialog = this.mBottomSheetDialog;
        if (cOUIBottomSheetDialog != null) {
            return cOUIBottomSheetDialog.isShowing();
        }
        return false;
    }

    public void refresh() {
        COUIBottomSheetDialog cOUIBottomSheetDialog = this.mBottomSheetDialog;
        if (cOUIBottomSheetDialog != null) {
            cOUIBottomSheetDialog.refresh();
        }
    }

    public void show() {
        COUIBottomSheetDialog cOUIBottomSheetDialog = this.mBottomSheetDialog;
        if (cOUIBottomSheetDialog != null) {
            cOUIBottomSheetDialog.show();
        }
    }
}