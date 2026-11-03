package com.coui.appcompat.dialog.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.coui.appcompat.button.COUIButton;
import com.coui.appcompat.button.COUILoadingButton;
import com.support.dialog.R;

/* JADX INFO: loaded from: classes7.dex */
public class SummaryAdapter extends BaseAdapter {
    private static final int LAYOUT = R.layout.coui_alert_dialog_summary_item;
    private Context mContext;
    private DialogInterface mHostDialog;
    private boolean mIsBlurState = false;
    private boolean mIsBottom;
    private boolean mIsTop;
    private final DialogInterface.OnClickListener mItemClickListener;
    private CharSequence[] mItems;
    private CharSequence[] mSummaries;
    private int[] mTextColor;

    public static class ViewHolder {
        COUILoadingButton mButton;
        TextView mSummaryView;

        private ViewHolder() {
        }

        public /* synthetic */ ViewHolder(AnonymousClass1 anonymousClass1) {
            this();
        }
    }

    public SummaryAdapter(Context context, boolean z2, boolean z3, CharSequence[] charSequenceArr, CharSequence[] charSequenceArr2, int[] iArr, DialogInterface.OnClickListener onClickListener) {
        this.mIsTop = z2;
        this.mIsBottom = z3;
        this.mContext = context;
        this.mItems = charSequenceArr;
        this.mSummaries = charSequenceArr2;
        this.mTextColor = iArr;
        this.mItemClickListener = onClickListener;
    }

    public static /* synthetic */ void a(SummaryAdapter summaryAdapter, int i3, View view) {
        DialogInterface dialogInterface = summaryAdapter.mHostDialog;
        if (dialogInterface != null) {
            summaryAdapter.mItemClickListener.onClick(dialogInterface, i3);
        }
    }

    private void applyBlurEffect(COUILoadingButton cOUILoadingButton) {
        cOUILoadingButton.setUseMaterialEffectAttr(this.mIsBlurState ? COUIButton.MaterialEffectState.FORCE_ENABLE : COUIButton.MaterialEffectState.FORCE_DISABLE, COUIButton.ButtonType.SECONDARY);
    }

    private void applyListItemConsistentWidth(View view, ViewGroup viewGroup) {
        if (viewGroup instanceof AbsListView) {
            int measuredWidth = viewGroup.getMeasuredWidth();
            if (measuredWidth <= 0) {
                measuredWidth = viewGroup.getWidth();
            }
            int paddingLeft = measuredWidth - (viewGroup.getPaddingLeft() + viewGroup.getPaddingRight());
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams instanceof AbsListView.LayoutParams) {
                AbsListView.LayoutParams layoutParams2 = (AbsListView.LayoutParams) layoutParams;
                if (paddingLeft > 0) {
                    if (layoutParams2.width != paddingLeft) {
                        layoutParams2.width = paddingLeft;
                        view.setLayoutParams(layoutParams2);
                        return;
                    }
                    return;
                }
                if (layoutParams2.width != -1) {
                    layoutParams2.width = -1;
                    view.setLayoutParams(layoutParams2);
                }
            }
        }
    }

    private float dp2px(float f3) {
        return TypedValue.applyDimension(1, f3, this.mContext.getResources().getDisplayMetrics());
    }

    private void resetPadding(int i3, View view) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (marginLayoutParams == null) {
            return;
        }
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.coui_alert_dialog_button_parent_padding_vertical);
        int dimensionPixelSize2 = this.mContext.getResources().getDimensionPixelSize(R.dimen.coui_alert_dialog_button_parent_padding_bottom);
        marginLayoutParams.topMargin = (i3 == 0 && this.mIsTop) ? dimensionPixelSize2 : dimensionPixelSize;
        if (i3 == getCount() - 1 && this.mIsBottom) {
            dimensionPixelSize = dimensionPixelSize2;
        }
        marginLayoutParams.bottomMargin = dimensionPixelSize;
        view.setLayoutParams(marginLayoutParams);
    }

    @Override // android.widget.Adapter
    public int getCount() {
        CharSequence[] charSequenceArr = this.mItems;
        if (charSequenceArr == null) {
            return 0;
        }
        return charSequenceArr.length;
    }

    @Override // android.widget.Adapter
    public /* bridge */ /* synthetic */ Object getItem(int i3) {
        return getItem(i3);
    }

    @Override // android.widget.Adapter
    public long getItemId(int i3) {
        return i3;
    }

    public CharSequence getSummary(int i3) {
        CharSequence[] charSequenceArr = this.mSummaries;
        if (charSequenceArr != null && i3 < charSequenceArr.length) {
            return charSequenceArr[i3];
        }
        return null;
    }

    @Override // android.widget.Adapter
    public View getView(int i3, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(this.mContext).inflate(LAYOUT, viewGroup, false);
            viewHolder = new ViewHolder(null);
            viewHolder.mButton = (COUILoadingButton) view.findViewById(android.R.id.text1);
            viewHolder.mSummaryView = (TextView) view.findViewById(R.id.summary_text2);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        CharSequence item = getItem(i3);
        CharSequence summary = getSummary(i3);
        viewHolder.mButton.setText(item);
        if (TextUtils.isEmpty(summary)) {
            viewHolder.mSummaryView.setVisibility(8);
        } else {
            viewHolder.mSummaryView.setVisibility(0);
            viewHolder.mSummaryView.setText(summary);
        }
        resetPadding(i3, viewHolder.mButton);
        int[] iArr = this.mTextColor;
        if (iArr != null && i3 >= 0 && i3 < iArr.length) {
            viewHolder.mButton.setTextColor(iArr[i3]);
        }
        applyBlurEffect(viewHolder.mButton);
        if (this.mItemClickListener != null) {
            viewHolder.mButton.setOnClickListener(new a(this, i3));
        } else {
            viewHolder.mButton.setOnClickListener(null);
        }
        applyListItemConsistentWidth(view, viewGroup);
        return view;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public boolean hasStableIds() {
        return true;
    }

    public void setBlurState(boolean z2) {
        if (this.mIsBlurState == z2) {
            return;
        }
        this.mIsBlurState = z2;
        notifyDataSetChanged();
    }

    public void setHostDialog(DialogInterface dialogInterface) {
        this.mHostDialog = dialogInterface;
    }

    @Override // android.widget.Adapter
    public CharSequence getItem(int i3) {
        CharSequence[] charSequenceArr = this.mItems;
        if (charSequenceArr == null) {
            return null;
        }
        return charSequenceArr[i3];
    }
}