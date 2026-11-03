package com.coui.appcompat.dialog.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.appcompat.content.res.AppCompatResources;
import com.coui.appcompat.checkbox.COUICheckBox;
import com.support.dialog.R;

/* JADX INFO: loaded from: classes7.dex */
public class ChoiceListAdapter extends BaseAdapter {
    private boolean[] mCheckBoxStates;
    private Context mContext;
    private boolean[] mDisableStatus;
    private Drawable[] mDrawables;
    private int[] mIcons;
    private boolean mIsBottom;
    private boolean mIsMultiChoice;
    private boolean mIsTop;
    private CharSequence[] mItems;
    private int mLayoutResId;
    private MaxCheckedListener mMaxCheckedListener;
    private int mMaxCheckedNum;
    private MultiChoiceItemClickListener mMultiChoiceItemClickListener;
    private CharSequence[] mSummaries;

    /* JADX INFO: renamed from: com.coui.appcompat.dialog.adapter.ChoiceListAdapter$1, reason: invalid class name */
    public class AnonymousClass1 implements View.OnTouchListener {
        public AnonymousClass1() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return true;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.dialog.adapter.ChoiceListAdapter$2, reason: invalid class name */
    public class AnonymousClass2 implements View.OnClickListener {
        final /* synthetic */ int val$position;

        public AnonymousClass2(int i3) {
            this.val$position = i3;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            View viewFindViewById = view.findViewById(R.id.checkbox);
            if (!(viewFindViewById instanceof COUICheckBox)) {
                if (viewFindViewById instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) viewFindViewById;
                    checkBox.setChecked(!checkBox.isChecked());
                    if (ChoiceListAdapter.access$400(ChoiceListAdapter.this) != null) {
                        ChoiceListAdapter.access$400(ChoiceListAdapter.this).onClick(this.val$position, checkBox.isChecked());
                        return;
                    }
                    return;
                }
                return;
            }
            COUICheckBox cOUICheckBox = (COUICheckBox) viewFindViewById;
            if (cOUICheckBox.getState() == 2) {
                cOUICheckBox.setState(0);
                ChoiceListAdapter.access$000(ChoiceListAdapter.this)[this.val$position] = false;
            } else if (ChoiceListAdapter.access$100(ChoiceListAdapter.this) <= 0 || ChoiceListAdapter.access$100(ChoiceListAdapter.this) > ChoiceListAdapter.access$200(ChoiceListAdapter.this)) {
                cOUICheckBox.setState(2);
                ChoiceListAdapter.access$000(ChoiceListAdapter.this)[this.val$position] = true;
            } else if (ChoiceListAdapter.access$300(ChoiceListAdapter.this) != null) {
                ChoiceListAdapter.access$300(ChoiceListAdapter.this).maxCheckedNotice(ChoiceListAdapter.access$100(ChoiceListAdapter.this));
            }
            if (ChoiceListAdapter.access$400(ChoiceListAdapter.this) != null) {
                ChoiceListAdapter.access$400(ChoiceListAdapter.this).onClick(this.val$position, cOUICheckBox.getState() == 2);
            }
        }
    }

    public interface MaxCheckedListener {
        void maxCheckedNotice(int i3);
    }

    /* JADX INFO: loaded from: classes.dex */
    public interface MultiChoiceItemClickListener {
        void onClick(int i3, boolean z2);
    }

    public static class ViewHolder {
        COUICheckBox checkBox;
        ImageView divider;
        ImageView icon;
        TextView itemText;
        RadioButton radioButton;
        FrameLayout radioLayout;
        TextView summaryText;
        LinearLayout textLayout;
    }

    public ChoiceListAdapter(Context context, int i3, CharSequence[] charSequenceArr, CharSequence[] charSequenceArr2, boolean[] zArr, boolean z2) {
        this(context, i3, charSequenceArr, charSequenceArr2, zArr, null, z2);
    }

    public static /* synthetic */ boolean[] access$000(ChoiceListAdapter choiceListAdapter) {
        return choiceListAdapter.mCheckBoxStates;
    }

    public static /* synthetic */ int access$100(ChoiceListAdapter choiceListAdapter) {
        return choiceListAdapter.mMaxCheckedNum;
    }

    public static /* synthetic */ int access$200(ChoiceListAdapter choiceListAdapter) {
        return choiceListAdapter.getCheckedNum();
    }

    public static /* synthetic */ MaxCheckedListener access$300(ChoiceListAdapter choiceListAdapter) {
        return choiceListAdapter.mMaxCheckedListener;
    }

    public static /* synthetic */ MultiChoiceItemClickListener access$400(ChoiceListAdapter choiceListAdapter) {
        return choiceListAdapter.mMultiChoiceItemClickListener;
    }

    private int getCheckedNum() {
        int i3 = 0;
        for (boolean z2 : this.mCheckBoxStates) {
            if (z2) {
                i3++;
            }
        }
        return i3;
    }

    private void initCheckboxStates(boolean[] zArr) {
        for (int i3 = 0; i3 < zArr.length; i3++) {
            boolean[] zArr2 = this.mCheckBoxStates;
            if (i3 >= zArr2.length) {
                return;
            }
            zArr2[i3] = zArr[i3];
        }
    }

    private void initCheckboxStatesDisable(boolean[] zArr) {
        for (int i3 = 0; i3 < zArr.length; i3++) {
            boolean[] zArr2 = this.mDisableStatus;
            if (i3 >= zArr2.length) {
                return;
            }
            zArr2[i3] = zArr[i3];
        }
    }

    private void setPaddingBottom(View view, int i3) {
        if (view == null) {
            return;
        }
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), i3);
    }

    public boolean[] getCheckBoxStates() {
        return this.mCheckBoxStates;
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

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int i3) {
        return i3;
    }

    public MultiChoiceItemClickListener getMultiChoiceItemClickListener() {
        return this.mMultiChoiceItemClickListener;
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
        View viewInflate;
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            viewInflate = LayoutInflater.from(this.mContext).inflate(this.mLayoutResId, viewGroup, false);
            viewHolder.icon = (ImageView) viewInflate.findViewById(R.id.alertdialog_choice_icon);
            viewHolder.textLayout = (LinearLayout) viewInflate.findViewById(R.id.text_layout);
            viewHolder.itemText = (TextView) viewInflate.findViewById(android.R.id.text1);
            viewHolder.summaryText = (TextView) viewInflate.findViewById(R.id.summary_text2);
            viewHolder.divider = (ImageView) viewInflate.findViewById(R.id.item_divider);
            if (this.mIsMultiChoice) {
                viewHolder.checkBox = (COUICheckBox) viewInflate.findViewById(R.id.checkbox);
            } else {
                viewHolder.radioLayout = (FrameLayout) viewInflate.findViewById(R.id.radio_layout);
                viewHolder.radioButton = (RadioButton) viewInflate.findViewById(R.id.radio_button);
            }
            viewInflate.setTag(viewHolder);
        } else {
            viewInflate = view;
            viewHolder = (ViewHolder) view.getTag();
        }
        boolean z2 = this.mDisableStatus[i3];
        viewHolder.itemText.setEnabled(!z2);
        viewHolder.summaryText.setEnabled(!z2);
        if (this.mIsMultiChoice) {
            viewHolder.checkBox.setEnabled(!z2);
        } else {
            viewHolder.radioButton.setEnabled(!z2);
        }
        if (z2) {
            viewInflate.setOnTouchListener(new AnonymousClass1());
        } else {
            viewInflate.setOnTouchListener(null);
        }
        if (this.mIsMultiChoice) {
            viewHolder.checkBox.setState(this.mCheckBoxStates[i3] ? 2 : 0);
            viewInflate.setOnClickListener(new AnonymousClass2(i3));
        } else {
            viewHolder.radioButton.setChecked(this.mCheckBoxStates[i3]);
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
        ImageView imageView = viewHolder.divider;
        if (imageView != null) {
            imageView.setVisibility(0);
        }
        int[] iArr = this.mIcons;
        if (iArr != null && i3 < iArr.length) {
            Drawable drawable = AppCompatResources.getDrawable(this.mContext, iArr[i3]);
            if (drawable == null) {
                viewHolder.icon.setVisibility(8);
                return viewInflate;
            }
            viewHolder.icon.setVisibility(0);
            viewHolder.icon.setImageDrawable(drawable);
            return viewInflate;
        }
        Drawable[] drawableArr = this.mDrawables;
        if (drawableArr == null || i3 >= drawableArr.length) {
            viewHolder.icon.setVisibility(8);
            return viewInflate;
        }
        Drawable drawable2 = drawableArr[i3];
        if (drawable2 == null) {
            viewHolder.icon.setVisibility(8);
            return viewInflate;
        }
        viewHolder.icon.setVisibility(0);
        viewHolder.icon.setImageDrawable(drawable2);
        return viewInflate;
    }

    public void setCheckboxState(int i3, int i4, ListView listView) {
        View childAt;
        COUICheckBox cOUICheckBox;
        int firstVisiblePosition = i4 - listView.getFirstVisiblePosition();
        if (firstVisiblePosition < 0 || (childAt = listView.getChildAt(firstVisiblePosition)) == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) childAt.getTag();
        if (!this.mIsMultiChoice || (cOUICheckBox = viewHolder.checkBox) == null) {
            return;
        }
        cOUICheckBox.setState(i3);
        this.mCheckBoxStates[i4] = i3 == 2;
    }

    public void setDrawables(Drawable[] drawableArr) {
        this.mDrawables = drawableArr;
    }

    public void setIcons(int[] iArr) {
        this.mIcons = iArr;
    }

    public void setIsBottom(boolean z2) {
        this.mIsBottom = z2;
    }

    public void setIsTop(boolean z2) {
        this.mIsTop = z2;
    }

    public void setMaxCheckedListener(MaxCheckedListener maxCheckedListener) {
        this.mMaxCheckedListener = maxCheckedListener;
    }

    public void setMultiChoiceItemClickListener(MultiChoiceItemClickListener multiChoiceItemClickListener) {
        this.mMultiChoiceItemClickListener = multiChoiceItemClickListener;
    }

    public ChoiceListAdapter(Context context, int i3, CharSequence[] charSequenceArr, CharSequence[] charSequenceArr2, boolean[] zArr, boolean[] zArr2, boolean z2) {
        this(context, i3, charSequenceArr, charSequenceArr2, zArr, zArr2, z2, 0);
    }

    @Override // android.widget.Adapter
    public CharSequence getItem(int i3) {
        CharSequence[] charSequenceArr = this.mItems;
        if (charSequenceArr == null) {
            return null;
        }
        return charSequenceArr[i3];
    }

    public ChoiceListAdapter(Context context, int i3, CharSequence[] charSequenceArr, CharSequence[] charSequenceArr2, boolean[] zArr, boolean[] zArr2, boolean z2, int i4) {
        this.mIsTop = false;
        this.mIsBottom = false;
        this.mContext = context;
        this.mLayoutResId = i3;
        this.mItems = charSequenceArr;
        this.mSummaries = charSequenceArr2;
        this.mIsMultiChoice = z2;
        this.mCheckBoxStates = new boolean[charSequenceArr.length];
        if (zArr != null) {
            initCheckboxStates(zArr);
        }
        this.mDisableStatus = new boolean[this.mItems.length];
        if (zArr2 != null) {
            initCheckboxStatesDisable(zArr2);
        }
        this.mMaxCheckedNum = i4;
    }

    public ChoiceListAdapter(Context context, int i3, CharSequence[] charSequenceArr, CharSequence[] charSequenceArr2) {
        this(context, i3, charSequenceArr, charSequenceArr2, null, false);
    }
}