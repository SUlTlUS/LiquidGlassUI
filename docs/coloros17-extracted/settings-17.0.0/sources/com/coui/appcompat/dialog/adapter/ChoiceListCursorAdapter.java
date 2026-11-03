package com.coui.appcompat.dialog.adapter;

import android.R;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import androidx.cursoradapter.widget.CursorAdapter;
import com.coui.appcompat.checkbox.COUICheckBox;
import java.util.HashSet;

/* JADX INFO: loaded from: classes7.dex */
public class ChoiceListCursorAdapter extends CursorAdapter {
    private HashSet<Integer> mCheckBoxStates;
    private String mIsCheckedColumn;
    private int mIsCheckedIndex;
    private boolean mIsMultiChoice;
    private String mLabelColumn;
    private int mLabelIndex;
    private int mLayoutResId;
    private String mSummaryColumn;
    private int mSummaryIndex;

    public ChoiceListCursorAdapter(Context context, Cursor cursor, int i3, String str, String str2) {
        this(context, cursor, i3, str, null, str2, false);
    }

    @Override // androidx.cursoradapter.widget.CursorAdapter
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView) view.findViewById(R.id.text1);
        TextView textView2 = (TextView) view.findViewById(com.support.dialog.R.id.summary_text2);
        int i3 = this.mCheckBoxStates.contains(Integer.valueOf(cursor.getPosition())) ? 2 : 0;
        if (this.mIsMultiChoice) {
            ((COUICheckBox) view.findViewById(com.support.dialog.R.id.checkbox)).setState(i3);
        }
        textView.setText(cursor.getString(this.mLabelIndex));
        if (this.mSummaryColumn == null) {
            textView2.setVisibility(8);
        } else {
            textView2.setVisibility(0);
            textView2.setText(cursor.getString(this.mSummaryIndex));
        }
    }

    @Override // androidx.cursoradapter.widget.CursorAdapter
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(this.mLayoutResId, viewGroup, false);
    }

    public void setCheckboxState(int i3, int i4, ListView listView) {
        int firstVisiblePosition = i4 - listView.getFirstVisiblePosition();
        if (firstVisiblePosition >= 0) {
            ((COUICheckBox) listView.getChildAt(firstVisiblePosition).findViewById(com.support.dialog.R.id.checkbox)).setState(i3);
            if (i3 == 2) {
                this.mCheckBoxStates.add(Integer.valueOf(i4));
            } else {
                this.mCheckBoxStates.remove(Integer.valueOf(i4));
            }
        }
    }

    public ChoiceListCursorAdapter(Context context, Cursor cursor, int i3, String str, String str2, String str3, boolean z2) {
        this(context, cursor);
        this.mIsMultiChoice = z2;
        this.mLabelColumn = str;
        this.mSummaryColumn = str3;
        this.mIsCheckedColumn = str2;
        this.mLayoutResId = i3;
        this.mCheckBoxStates = new HashSet<>();
        this.mLabelIndex = cursor.getColumnIndexOrThrow(this.mLabelColumn);
        String str4 = this.mSummaryColumn;
        if (str4 != null) {
            this.mSummaryIndex = cursor.getColumnIndexOrThrow(str4);
        }
        if (z2) {
            this.mIsCheckedIndex = cursor.getColumnIndexOrThrow(this.mIsCheckedColumn);
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getInt(this.mIsCheckedIndex) == 1) {
                        this.mCheckBoxStates.add(Integer.valueOf(cursor.getPosition()));
                    }
                } while (cursor.moveToNext());
            }
            cursor.moveToFirst();
        }
    }

    public ChoiceListCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.mIsMultiChoice = false;
        this.mIsCheckedIndex = 0;
    }
}