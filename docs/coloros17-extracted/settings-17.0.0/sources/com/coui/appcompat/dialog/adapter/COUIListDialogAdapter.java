package com.coui.appcompat.dialog.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.support.dialog.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIListDialogAdapter extends BaseAdapter {
    private Context mContext;
    private CharSequence[] mItems;
    private int[] mTextAppearances;
    private final int LAYOUT = R.layout.coui_list_dialog_item;
    private boolean mIsTop = false;
    private boolean mIsBottom = false;

    public class ViewHolder {
        ImageView divider;
        TextView mTextView;
        LinearLayout mainLayout;

        public ViewHolder() {
        }
    }

    public COUIListDialogAdapter(Context context, CharSequence[] charSequenceArr, int[] iArr) {
        this.mContext = context;
        this.mItems = charSequenceArr;
        this.mTextAppearances = iArr;
    }

    private View getViewInternal(int i3, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(this.mContext).inflate(this.LAYOUT, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) view.findViewById(android.R.id.text1);
            viewHolder.divider = (ImageView) view.findViewById(R.id.item_divider);
            viewHolder.mainLayout = (LinearLayout) view.findViewById(R.id.main_layout);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mTextView.setText(getItem(i3));
        int[] iArr = this.mTextAppearances;
        if (iArr != null) {
            int i4 = iArr[i3];
            if (i4 > 0) {
                viewHolder.mTextView.setTextAppearance(this.mContext, i4);
            } else {
                viewHolder.mTextView.setTextAppearance(this.mContext, R.style.DefaultDialogItemTextStyle);
            }
        }
        if (viewHolder.divider != null) {
            if (getCount() > 1 && i3 != getCount() - 1) {
                viewHolder.divider.setVisibility(0);
                return view;
            }
            viewHolder.divider.setVisibility(8);
        }
        return view;
    }

    private void resetPadding(int i3, View view) {
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.coui_bottom_alert_dialog_vertical_button_padding_bottom_extra_new);
        Resources resources = this.mContext.getResources();
        int i4 = R.dimen.coui_bottom_alert_dialog_vertical_button_padding_vertical_new;
        int dimensionPixelSize2 = resources.getDimensionPixelSize(i4);
        int dimensionPixelSize3 = this.mContext.getResources().getDimensionPixelSize(R.dimen.alert_dialog_list_item_padding_left);
        int dimensionPixelSize4 = this.mContext.getResources().getDimensionPixelSize(i4);
        int dimensionPixelSize5 = this.mContext.getResources().getDimensionPixelSize(R.dimen.alert_dialog_list_item_padding_right);
        this.mContext.getResources().getDimensionPixelSize(R.dimen.alert_dialog_list_item_min_height);
        if (i3 == getCount() - 1 && this.mIsBottom) {
            view.setPadding(dimensionPixelSize3, dimensionPixelSize2, dimensionPixelSize5, dimensionPixelSize4 + dimensionPixelSize);
        } else if (i3 == 0 && this.mIsTop) {
            view.setPadding(dimensionPixelSize3, dimensionPixelSize2 + dimensionPixelSize, dimensionPixelSize5, dimensionPixelSize4);
        } else {
            view.setPadding(dimensionPixelSize3, dimensionPixelSize2, dimensionPixelSize5, dimensionPixelSize4);
        }
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

    @Override // android.widget.Adapter
    public View getView(int i3, View view, ViewGroup viewGroup) {
        View viewInternal = getViewInternal(i3, view, viewGroup);
        resetPadding(i3, viewInternal.findViewById(R.id.main_layout));
        return viewInternal;
    }

    public void setIsBottom(boolean z2) {
        this.mIsBottom = z2;
    }

    public void setIsTop(boolean z2) {
        this.mIsTop = z2;
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