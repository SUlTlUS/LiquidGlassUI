package com.coui.appcompat.card;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.coui.appcompat.card.BaseCardInstructionAdapter.BaseHolder;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0003\b&\u0018\u0000 \u0014*\b\b\u0000\u0010\u0001*\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003:\u0002\u0013\u0014B\u0017\b\u0016\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0002\u0010\u0007B\u0005¢\u0006\u0002\u0010\bJ\b\u0010\u000b\u001a\u00020\nH\u0016J\u001d\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00028\u00002\u0006\u0010\u000f\u001a\u00020\nH\u0016¢\u0006\u0002\u0010\u0010J\u0016\u0010\u0011\u001a\u00020\r2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0012H\u0007R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lcom/coui/appcompat/card/BaseCardInstructionAdapter;", "HOLDER", "Lcom/coui/appcompat/card/BaseCardInstructionAdapter$BaseHolder;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "displayInfos", "", "Lcom/coui/appcompat/card/BaseDisplayInfo;", "(Ljava/util/List;)V", "()V", "pagerLastHeight", "", "getItemCount", "onBindViewHolder", "", "holder", "position", "(Lcom/coui/appcompat/card/BaseCardInstructionAdapter$BaseHolder;I)V", "updateDisplayInfos", "", "BaseHolder", "Companion", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class BaseCardInstructionAdapter<HOLDER extends BaseHolder> extends RecyclerView.Adapter<HOLDER> {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final String EMPTY_STRING = "";
    private final List<BaseDisplayInfo> displayInfos;
    private int pagerLastHeight;

    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b&\u0018\u00002\u00020\u0001B\u0019\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH&J\u0006\u0010\r\u001a\u00020\nR\u0018\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005X\u0084\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\u000e"}, d2 = {"Lcom/coui/appcompat/card/BaseCardInstructionAdapter$BaseHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "adapter", "Lcom/coui/appcompat/card/BaseCardInstructionAdapter;", "(Landroid/view/View;Lcom/coui/appcompat/card/BaseCardInstructionAdapter;)V", "getAdapter", "()Lcom/coui/appcompat/card/BaseCardInstructionAdapter;", "bind", "", "displayInfo", "Lcom/coui/appcompat/card/BaseDisplayInfo;", "setMatchChildrenMaxHeight", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static abstract class BaseHolder extends RecyclerView.ViewHolder {
        private final BaseCardInstructionAdapter<?> adapter;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public BaseHolder(View itemView, BaseCardInstructionAdapter<?> adapter) {
            super(itemView);
            Intrinsics.checkNotNullParameter(itemView, "itemView");
            Intrinsics.checkNotNullParameter(adapter, "adapter");
            this.adapter = adapter;
        }

        public static /* synthetic */ void a(BaseHolder baseHolder) {
            setMatchChildrenMaxHeight$lambda$1(baseHolder);
        }

        private static final void setMatchChildrenMaxHeight$lambda$1(BaseHolder this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this$0.itemView.measure(View.MeasureSpec.makeMeasureSpec(this$0.itemView.getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(0, Integer.MIN_VALUE));
            ViewParent parent = this$0.itemView.getParent();
            ViewParent parent2 = parent != null ? parent.getParent() : null;
            if (parent2 instanceof ViewPager2) {
                ViewPager2 viewPager2 = (ViewPager2) parent2;
                if (viewPager2.getLayoutParams().height != RangesKt.coerceAtLeast(BaseCardInstructionAdapter.access$getPagerLastHeight$p(this$0.adapter), this$0.itemView.getMeasuredHeight())) {
                    ViewGroup.LayoutParams layoutParams = viewPager2.getLayoutParams();
                    int iCoerceAtLeast = RangesKt.coerceAtLeast(BaseCardInstructionAdapter.access$getPagerLastHeight$p(this$0.adapter), this$0.itemView.getMeasuredHeight());
                    layoutParams.height = iCoerceAtLeast;
                    BaseCardInstructionAdapter.access$setPagerLastHeight$p(this$0.adapter, iCoerceAtLeast);
                    viewPager2.setLayoutParams(layoutParams);
                }
            }
        }

        public abstract void bind(BaseDisplayInfo displayInfo);

        public final BaseCardInstructionAdapter<?> getAdapter() {
            return this.adapter;
        }

        public final void setMatchChildrenMaxHeight() {
            if (BaseCardInstructionAdapter.access$getDisplayInfos$p(this.adapter).size() <= 1) {
                return;
            }
            this.itemView.post(new a(this));
        }
    }

    @Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0005\u001a\u00020\u0006*\u00020\u00072\u0006\u0010\b\u001a\u00020\tJ\u001a\u0010\u0005\u001a\u00020\u0006*\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lcom/coui/appcompat/card/BaseCardInstructionAdapter$Companion;", "", "()V", "EMPTY_STRING", "", "updateContentAndVisibility", "", "Landroid/widget/TextView;", "content", "", "view", "Landroid/view/View;", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final void updateContentAndVisibility(TextView textView, CharSequence content) {
            Intrinsics.checkNotNullParameter(textView, "<this>");
            Intrinsics.checkNotNullParameter(content, "content");
            updateContentAndVisibility(textView, content, textView);
        }

        private Companion() {
        }

        public final void updateContentAndVisibility(TextView textView, CharSequence content, View view) {
            Intrinsics.checkNotNullParameter(textView, "<this>");
            Intrinsics.checkNotNullParameter(content, "content");
            Intrinsics.checkNotNullParameter(view, "view");
            if (content.length() <= 0) {
                view.setVisibility(8);
            } else {
                view.setVisibility(0);
                textView.setText(content);
            }
        }
    }

    public BaseCardInstructionAdapter() {
        this.displayInfos = new ArrayList();
    }

    public static final /* synthetic */ List access$getDisplayInfos$p(BaseCardInstructionAdapter baseCardInstructionAdapter) {
        return baseCardInstructionAdapter.displayInfos;
    }

    public static final /* synthetic */ int access$getPagerLastHeight$p(BaseCardInstructionAdapter baseCardInstructionAdapter) {
        return baseCardInstructionAdapter.pagerLastHeight;
    }

    public static final /* synthetic */ void access$setPagerLastHeight$p(BaseCardInstructionAdapter baseCardInstructionAdapter, int i3) {
        baseCardInstructionAdapter.pagerLastHeight = i3;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.displayInfos.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public /* bridge */ /* synthetic */ void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i3) {
        onBindViewHolder((BaseHolder) viewHolder, i3);
    }

    public final void updateDisplayInfos(List<? extends BaseDisplayInfo> displayInfos) {
        Intrinsics.checkNotNullParameter(displayInfos, "displayInfos");
        this.displayInfos.clear();
        this.displayInfos.addAll(displayInfos);
        this.pagerLastHeight = 0;
        notifyDataSetChanged();
    }

    public void onBindViewHolder(HOLDER holder, int position) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        holder.bind(this.displayInfos.get(position));
        holder.setMatchChildrenMaxHeight();
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public BaseCardInstructionAdapter(List<BaseDisplayInfo> displayInfos) {
        this();
        Intrinsics.checkNotNullParameter(displayInfos, "displayInfos");
        this.displayInfos.clear();
        this.displayInfos.addAll(displayInfos);
    }

    public /* synthetic */ BaseCardInstructionAdapter(List list, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? new ArrayList() : list);
    }
}