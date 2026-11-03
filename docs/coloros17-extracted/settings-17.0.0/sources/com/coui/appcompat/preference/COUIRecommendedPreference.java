package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.coui.appcompat.accessibilityutil.COUIAccessibilityUtil;
import com.coui.appcompat.cardlist.COUICardListSelectedItemLayout;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.coui.appcompat.version.COUIVersionUtil;
import com.support.preference.R;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes7.dex */
public class COUIRecommendedPreference extends Preference {
    private COUIRecommendedDrawable mBackground;
    private int mColor;
    private String mHeaderText;
    private float mRadius;
    private List<RecommendedEntity> mRecommendedEntityList;

    public interface OnRecommendedClickListener {
        void onRecommendedClick(View view);
    }

    public static class RecommendedAdapter extends RecyclerView.Adapter<RecommendedVH> {
        private static final int ITEM_VIEW_TYPE_ENTITY = 1;
        private static final int ITEM_VIEW_TYPE_HEADER = 0;
        private int mBackgroundColor;
        private Context mContext;
        private List<RecommendedEntity> mEntities = new ArrayList();
        private float mRadius;

        /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIRecommendedPreference$RecommendedAdapter$1, reason: invalid class name */
        public class AnonymousClass1 implements View.OnClickListener {
            final /* synthetic */ RecommendedEntity val$entity;

            public AnonymousClass1(RecommendedEntity recommendedEntity) {
                this.val$entity = recommendedEntity;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (RecommendedEntity.access$300(this.val$entity) != null) {
                    RecommendedEntity.access$300(this.val$entity).onRecommendedClick(view);
                }
            }
        }

        public RecommendedAdapter(Context context, List<RecommendedEntity> list, String str, float f3, int i3) {
            this.mContext = context;
            this.mRadius = f3;
            this.mBackgroundColor = i3;
            setData(list, str);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.mEntities.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i3) {
            return i3 == 0 ? 0 : 1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public /* bridge */ /* synthetic */ void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i3) {
            onBindViewHolder((RecommendedVH) viewHolder, i3);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public /* bridge */ /* synthetic */ RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i3) {
            return onCreateViewHolder(viewGroup, i3);
        }

        public void setData(List<RecommendedEntity> list, String str) {
            this.mEntities.clear();
            if (list != null) {
                this.mEntities.addAll(list);
                this.mEntities.add(0, new RecommendedEntity(str));
            }
            notifyDataSetChanged();
        }

        public void onBindViewHolder(RecommendedVH recommendedVH, int i3) {
            RecommendedEntity recommendedEntity = this.mEntities.get(i3);
            RecommendedVH.access$000(recommendedVH).setText(RecommendedEntity.access$100(recommendedEntity));
            RecommendedVH.access$200(recommendedVH).setRadius(this.mRadius);
            RecommendedVH.access$200(recommendedVH).setBackgroundColor(this.mBackgroundColor);
            if (i3 <= 0) {
                if (i3 == 0) {
                    RecommendedVH.access$200(recommendedVH).setClickable(false);
                    RecommendedVH.access$200(recommendedVH).setPositionInGroup(1);
                    return;
                }
                return;
            }
            if (i3 == getItemCount() - 1) {
                RecommendedVH.access$200(recommendedVH).setPositionInGroup(3);
                RecommendedVH.access$200(recommendedVH).setPaddingRelative(RecommendedVH.access$200(recommendedVH).getPaddingStart(), RecommendedVH.access$200(recommendedVH).getPaddingTop(), RecommendedVH.access$200(recommendedVH).getPaddingEnd(), this.mContext.getResources().getDimensionPixelOffset(R.dimen.recommended_recyclerView_padding_bottom));
            } else if (RecommendedVH.access$200(recommendedVH).getPaddingBottom() == this.mContext.getResources().getDimensionPixelOffset(R.dimen.recommended_recyclerView_padding_bottom)) {
                RecommendedVH.access$200(recommendedVH).setPaddingRelative(RecommendedVH.access$200(recommendedVH).getPaddingStart(), RecommendedVH.access$200(recommendedVH).getPaddingTop(), RecommendedVH.access$200(recommendedVH).getPaddingEnd(), 0);
                RecommendedVH.access$200(recommendedVH).setPositionInGroup(2);
            } else {
                RecommendedVH.access$200(recommendedVH).setPositionInGroup(2);
            }
            RecommendedVH.access$200(recommendedVH).setOnClickListener(new AnonymousClass1(recommendedEntity));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecommendedVH onCreateViewHolder(ViewGroup viewGroup, int i3) {
            return i3 == 0 ? new RecommendedVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recommended_head_textview, viewGroup, false)) : new RecommendedVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recommended_common_textview, viewGroup, false));
        }
    }

    public static class RecommendedVH extends RecyclerView.ViewHolder {
        private COUICardListSelectedItemLayout mLayout;
        private TextView mTitleView;

        /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIRecommendedPreference$RecommendedVH$1, reason: invalid class name */
        public class AnonymousClass1 extends View.AccessibilityDelegate {
            public AnonymousClass1() {
            }

            @Override // android.view.View.AccessibilityDelegate
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.setClassName(COUIAccessibilityUtil.BUTTON_CLASS_NAME);
            }
        }

        public RecommendedVH(View view) {
            super(view);
            this.mLayout = (COUICardListSelectedItemLayout) view;
            TextView textView = (TextView) view.findViewById(R.id.txt_content);
            this.mTitleView = textView;
            textView.setAccessibilityDelegate(new AnonymousClass1());
            this.mLayout.setClickable(true);
        }

        public static /* synthetic */ TextView access$000(RecommendedVH recommendedVH) {
            return recommendedVH.mTitleView;
        }

        public static /* synthetic */ COUICardListSelectedItemLayout access$200(RecommendedVH recommendedVH) {
            return recommendedVH.mLayout;
        }
    }

    public COUIRecommendedPreference(Context context) {
        this(context, null);
    }

    private float getInnerRadius(float f3) {
        return (RoundCornerUtil.getSmoothStyleType() != 1 || COUIVersionUtil.getOSVersionCode() <= 37) ? f3 : getContext().getResources().getDimensionPixelSize(com.support.appcompat.R.dimen.coui_round_corner_xl_radius_normal_16_1);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        RecyclerView recyclerView = (RecyclerView) preferenceViewHolder.itemView;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new RecommendedAdapter(getContext(), this.mRecommendedEntityList, this.mHeaderText, this.mRadius, this.mColor));
        } else {
            ((RecommendedAdapter) adapter).setData(this.mRecommendedEntityList, this.mHeaderText);
        }
        recyclerView.setFocusable(false);
    }

    public void setData(List<RecommendedEntity> list) {
        if (list == null || list.isEmpty()) {
            setVisible(false);
            return;
        }
        setVisible(true);
        this.mRecommendedEntityList = list;
        notifyChanged();
    }

    public void setHeaderText(String str) {
        setVisible(true);
        if (TextUtils.equals(this.mHeaderText, str)) {
            return;
        }
        this.mHeaderText = str;
        notifyChanged();
    }

    public static class RecommendedEntity {
        private OnRecommendedClickListener onRecommendedClickListener;
        private String title;

        public RecommendedEntity(String str) {
            this.title = str;
        }

        public static /* synthetic */ String access$100(RecommendedEntity recommendedEntity) {
            return recommendedEntity.title;
        }

        public static /* synthetic */ OnRecommendedClickListener access$300(RecommendedEntity recommendedEntity) {
            return recommendedEntity.onRecommendedClickListener;
        }

        public RecommendedEntity(String str, OnRecommendedClickListener onRecommendedClickListener) {
            this.title = str;
            this.onRecommendedClickListener = onRecommendedClickListener;
        }
    }

    public COUIRecommendedPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiRecommendedPreferenceStyle);
    }

    public COUIRecommendedPreference(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, R.style.Preference_COUIRecommendedPreference);
    }

    public COUIRecommendedPreference(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3);
        setLayoutResource(R.layout.coui_recommended_preference_layout);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUIRecommendedPreference, i3, 0);
        this.mRadius = getInnerRadius(typedArrayObtainStyledAttributes.getDimension(R.styleable.COUIRecommendedPreference_recommendedCardBgRadius, COUIContextUtil.getAttrDimens(getContext(), com.support.appcompat.R.attr.couiRoundCornerXL)));
        this.mColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIRecommendedPreference_recommendedCardBgColor, COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorContainer4));
        this.mBackground = new COUIRecommendedDrawable(this.mRadius, this.mColor);
        String string = typedArrayObtainStyledAttributes.getString(R.styleable.COUIRecommendedPreference_recommendedHeaderTitle);
        this.mHeaderText = string;
        if (string == null) {
            this.mHeaderText = getContext().getResources().getString(R.string.bottom_recommended_header_title);
        }
        typedArrayObtainStyledAttributes.recycle();
    }
}