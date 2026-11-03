package com.coui.appcompat.panel;

import android.R;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.coui.appcompat.checkbox.COUICheckBox;
import java.util.HashSet;

/* JADX INFO: loaded from: classes7.dex */
public class COUIBottomSheetChoiceListAdapter extends RecyclerView.Adapter<ViewHolder> {
    private HashSet<Integer> mCheckBoxStates;
    private int mCheckedItem;
    private Context mContext;
    private boolean mIsMultiChoice;
    private CharSequence[] mItems;
    private int mLayoutResId;
    private OnItemClickListener mOnItemClickListener;
    private CharSequence[] mSummaries;

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetChoiceListAdapter$1, reason: invalid class name */
    public class AnonymousClass1 implements View.OnClickListener {
        final /* synthetic */ ViewHolder val$holder;
        final /* synthetic */ int val$position;

        public AnonymousClass1(ViewHolder viewHolder, int i3) {
            this.val$holder = viewHolder;
            this.val$position = i3;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int i3;
            if (COUIBottomSheetChoiceListAdapter.access$000(COUIBottomSheetChoiceListAdapter.this)) {
                if (this.val$holder.checkBox.getState() != 2) {
                    COUIBottomSheetChoiceListAdapter.access$100(COUIBottomSheetChoiceListAdapter.this).add(Integer.valueOf(this.val$position));
                } else {
                    COUIBottomSheetChoiceListAdapter.access$100(COUIBottomSheetChoiceListAdapter.this).remove(Integer.valueOf(this.val$position));
                }
                i3 = COUIBottomSheetChoiceListAdapter.access$100(COUIBottomSheetChoiceListAdapter.this).contains(Integer.valueOf(this.val$position)) ? 2 : 0;
                this.val$holder.checkBox.setState(i3);
            } else {
                if (this.val$position == COUIBottomSheetChoiceListAdapter.access$200(COUIBottomSheetChoiceListAdapter.this)) {
                    COUIBottomSheetChoiceListAdapter.access$300(COUIBottomSheetChoiceListAdapter.this).onItemClick(view, this.val$position, 0);
                    return;
                }
                boolean zIsChecked = this.val$holder.radioButton.isChecked();
                i3 = !zIsChecked ? 1 : 0;
                this.val$holder.radioButton.setChecked(!zIsChecked);
                COUIBottomSheetChoiceListAdapter cOUIBottomSheetChoiceListAdapter = COUIBottomSheetChoiceListAdapter.this;
                cOUIBottomSheetChoiceListAdapter.notifyItemChanged(COUIBottomSheetChoiceListAdapter.access$200(cOUIBottomSheetChoiceListAdapter));
                COUIBottomSheetChoiceListAdapter.access$202(COUIBottomSheetChoiceListAdapter.this, this.val$position);
            }
            COUIBottomSheetChoiceListAdapter.access$300(COUIBottomSheetChoiceListAdapter.this).onItemClick(view, this.val$position, i3);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int i3, int i4);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        COUICheckBox checkBox;
        TextView itemText;
        View mLayout;
        RadioButton radioButton;
        TextView summaryText;

        public ViewHolder(View view) {
            super(view);
            this.itemText = (TextView) view.findViewById(R.id.text1);
            this.summaryText = (TextView) view.findViewById(com.support.dialog.R.id.summary_text2);
            if (COUIBottomSheetChoiceListAdapter.access$000(COUIBottomSheetChoiceListAdapter.this)) {
                this.checkBox = (COUICheckBox) view.findViewById(com.support.dialog.R.id.checkbox);
            } else {
                this.radioButton = (RadioButton) view.findViewById(com.support.dialog.R.id.radio_button);
            }
            view.setBackground(COUIBottomSheetChoiceListAdapter.access$400(COUIBottomSheetChoiceListAdapter.this).getDrawable(com.support.appcompat.R.drawable.coui_list_selector_background));
            this.mLayout = view;
        }
    }

    public COUIBottomSheetChoiceListAdapter(Context context, int i3, CharSequence[] charSequenceArr, CharSequence[] charSequenceArr2, int i4, boolean[] zArr, boolean z2) {
        this.mCheckedItem = -1;
        this.mContext = context;
        this.mLayoutResId = i3;
        this.mItems = charSequenceArr;
        this.mSummaries = charSequenceArr2;
        this.mIsMultiChoice = z2;
        this.mCheckBoxStates = new HashSet<>();
        this.mCheckedItem = i4;
        if (zArr != null) {
            initCheckboxStates(zArr);
        }
    }

    public static /* synthetic */ boolean access$000(COUIBottomSheetChoiceListAdapter cOUIBottomSheetChoiceListAdapter) {
        return cOUIBottomSheetChoiceListAdapter.mIsMultiChoice;
    }

    public static /* synthetic */ HashSet access$100(COUIBottomSheetChoiceListAdapter cOUIBottomSheetChoiceListAdapter) {
        return cOUIBottomSheetChoiceListAdapter.mCheckBoxStates;
    }

    public static /* synthetic */ int access$200(COUIBottomSheetChoiceListAdapter cOUIBottomSheetChoiceListAdapter) {
        return cOUIBottomSheetChoiceListAdapter.mCheckedItem;
    }

    public static /* synthetic */ int access$202(COUIBottomSheetChoiceListAdapter cOUIBottomSheetChoiceListAdapter, int i3) {
        cOUIBottomSheetChoiceListAdapter.mCheckedItem = i3;
        return i3;
    }

    public static /* synthetic */ OnItemClickListener access$300(COUIBottomSheetChoiceListAdapter cOUIBottomSheetChoiceListAdapter) {
        return cOUIBottomSheetChoiceListAdapter.mOnItemClickListener;
    }

    public static /* synthetic */ Context access$400(COUIBottomSheetChoiceListAdapter cOUIBottomSheetChoiceListAdapter) {
        return cOUIBottomSheetChoiceListAdapter.mContext;
    }

    private void initCheckboxStates(boolean[] zArr) {
        for (int i3 = 0; i3 < zArr.length; i3++) {
            if (zArr[i3]) {
                this.mCheckBoxStates.add(Integer.valueOf(i3));
            }
        }
    }

    public CharSequence getItem(int i3) {
        CharSequence[] charSequenceArr = this.mItems;
        if (charSequenceArr == null || i3 >= charSequenceArr.length) {
            return null;
        }
        return charSequenceArr[i3];
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        CharSequence[] charSequenceArr = this.mItems;
        if (charSequenceArr == null) {
            return 0;
        }
        return charSequenceArr.length;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i3) {
        return i3;
    }

    public CharSequence getSummary(int i3) {
        CharSequence[] charSequenceArr = this.mSummaries;
        if (charSequenceArr == null || i3 >= charSequenceArr.length) {
            return null;
        }
        return charSequenceArr[i3];
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public /* bridge */ /* synthetic */ void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i3) {
        onBindViewHolder((ViewHolder) viewHolder, i3);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public /* bridge */ /* synthetic */ RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i3) {
        return onCreateViewHolder(viewGroup, i3);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i3) {
        if (this.mIsMultiChoice) {
            viewHolder.checkBox.setState(this.mCheckBoxStates.contains(Integer.valueOf(i3)) ? 2 : 0);
        } else {
            viewHolder.radioButton.setChecked(this.mCheckedItem == i3);
        }
        CharSequence item = getItem(i3);
        CharSequence summary = getSummary(i3);
        viewHolder.itemText.setText(item);
        if (TextUtils.isEmpty(summary)) {
            viewHolder.summaryText.setVisibility(8);
        } else {
            viewHolder.summaryText.setVisibility(0);
            viewHolder.summaryText.setText(summary);
        }
        if (this.mOnItemClickListener != null) {
            viewHolder.mLayout.setOnClickListener(new AnonymousClass1(viewHolder, i3));
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i3) {
        return new ViewHolder(LayoutInflater.from(this.mContext).inflate(this.mLayoutResId, viewGroup, false));
    }

    public COUIBottomSheetChoiceListAdapter(Context context, int i3, CharSequence[] charSequenceArr, CharSequence[] charSequenceArr2, int i4) {
        this(context, i3, charSequenceArr, charSequenceArr2, i4, null, false);
    }
}