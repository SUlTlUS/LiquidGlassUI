package com.coui.appcompat.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.support.dialog.R;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes7.dex */
class COUIGuidePageAdapter extends RecyclerView.Adapter<GuideViewHolder> {
    private final Context mContext;
    private final List<COUIGuidePageItem> mItems;
    private int mImagePaddingLeftDp = 0;
    private int mImagePaddingTopDp = 0;
    private int mImagePaddingRightDp = 0;
    private int mImagePaddingBottomDp = 16;
    private int mImageHeightDp = 328;

    public static class GuideViewHolder extends RecyclerView.ViewHolder {
        final TextView mDescView;
        final ImageView mImageView;
        final TextView mTitleView;

        public GuideViewHolder(View view) {
            super(view);
            this.mImageView = (ImageView) view.findViewById(R.id.pager_item_im);
            this.mTitleView = (TextView) view.findViewById(R.id.guide_tips_title);
            this.mDescView = (TextView) view.findViewById(R.id.guide_tips_description);
        }
    }

    public COUIGuidePageAdapter(Context context, List<COUIGuidePageItem> list) {
        this.mContext = context;
        this.mItems = new ArrayList(list);
    }

    private static void bindTextOrGoneIfBlank(TextView textView, CharSequence charSequence) {
        if (charSequence == null || TextUtils.getTrimmedLength(charSequence) == 0) {
            textView.setVisibility(8);
        } else {
            textView.setVisibility(0);
            textView.setText(charSequence);
        }
    }

    private int dpToPx(int i3) {
        return Math.round(this.mContext.getResources().getDisplayMetrics().density * i3);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mItems.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public /* bridge */ /* synthetic */ void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i3) {
        onBindViewHolder((GuideViewHolder) viewHolder, i3);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public /* bridge */ /* synthetic */ RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i3) {
        return onCreateViewHolder(viewGroup, i3);
    }

    public void setImageHeightDp(int i3) {
        this.mImageHeightDp = i3;
        notifyDataSetChanged();
    }

    public void setImagePadding(int i3, int i4, int i5, int i6) {
        this.mImagePaddingLeftDp = i3;
        this.mImagePaddingTopDp = i4;
        this.mImagePaddingRightDp = i5;
        this.mImagePaddingBottomDp = i6;
        notifyDataSetChanged();
    }

    public void onBindViewHolder(GuideViewHolder guideViewHolder, int i3) {
        COUIGuidePageItem cOUIGuidePageItem = this.mItems.get(i3);
        if (cOUIGuidePageItem.getImageResId() != 0) {
            guideViewHolder.mImageView.setImageResource(cOUIGuidePageItem.getImageResId());
            guideViewHolder.mImageView.setVisibility(0);
        } else {
            guideViewHolder.mImageView.setVisibility(8);
        }
        bindTextOrGoneIfBlank(guideViewHolder.mTitleView, cOUIGuidePageItem.getTitle());
        bindTextOrGoneIfBlank(guideViewHolder.mDescView, cOUIGuidePageItem.getDescription());
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) guideViewHolder.mImageView.getLayoutParams();
        if (marginLayoutParams != null) {
            marginLayoutParams.setMargins(this.mImagePaddingLeftDp, this.mImagePaddingTopDp, this.mImagePaddingRightDp, this.mImagePaddingBottomDp);
            marginLayoutParams.height = dpToPx(this.mImageHeightDp);
            guideViewHolder.mImageView.setLayoutParams(marginLayoutParams);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public GuideViewHolder onCreateViewHolder(ViewGroup viewGroup, int i3) {
        return new GuideViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.coui_guide_page_item, viewGroup, false));
    }
}