package com.coui.appcompat.itemview;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.COUIRecyclerView;
import androidx.recyclerview.widget.RecyclerView;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.cardlist.COUICardListSelectedItemLayout;
import com.support.preference.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIBaseListItemViewHolder extends RecyclerView.ViewHolder implements COUIRecyclerView.ICOUIDividerDecorationInterface {
    public static final int BOTH_RIGHT_ANGLE = 2;
    public static final int BOTH_ROUNDED_CORNER = 4;
    public static final int BOTTOM_ROUNDED_CORNER = 3;
    public static final int TOP_ROUNDED_CORNER = 1;
    private int mCardType;
    private int mDividerPadding;
    private TextView mListTitleView;
    private View mRootListItemView;

    public COUIBaseListItemViewHolder(View view) {
        super(view);
        this.mCardType = 2;
        this.mDividerPadding = view.getContext().getResources().getDimensionPixelSize(R.dimen.coui_preference_divider_default_horizontal_padding);
        this.mRootListItemView = view.findViewById(R.id.coui_preference);
        this.mListTitleView = (TextView) view.findViewById(android.R.id.title);
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.ICOUIDividerDecorationInterface
    public boolean drawDivider() {
        View view = this.mRootListItemView;
        if (view == null || !(view instanceof COUICardListSelectedItemLayout)) {
            return false;
        }
        int i3 = this.mCardType;
        return i3 == 1 || i3 == 2;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.ICOUIDividerDecorationInterface
    public View getDividerEndAlignView() {
        return null;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.ICOUIDividerDecorationInterface
    public int getDividerEndInset() {
        return this.mDividerPadding;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.ICOUIDividerDecorationInterface
    public View getDividerStartAlignView() {
        return this.mListTitleView;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.ICOUIDividerDecorationInterface
    public int getDividerStartInset() {
        return this.mDividerPadding;
    }

    public void setCornerType(int i3) {
        this.mCardType = i3;
        COUICardListHelper.setItemCardBackground(this.mRootListItemView, i3);
    }
}