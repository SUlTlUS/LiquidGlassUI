package com.coui.appcompat.card;

import android.content.Context;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.oplus.anim.EffectiveAnimationView;
import com.oplus.settings.custom.quickapp.ItemInfo;
import com.support.card.R;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\u0018\u0000 \u000e2\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0003\r\u000e\u000fB\u0007\b\u0016¢\u0006\u0002\u0010\u0003B\u0013\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0002\u0010\u0007J\u001c\u0010\b\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016¨\u0006\u0010"}, d2 = {"Lcom/coui/appcompat/card/CardInstructionDescriptionAdapter;", "Lcom/coui/appcompat/card/BaseCardInstructionAdapter;", "Lcom/coui/appcompat/card/CardInstructionDescriptionAdapter$DescriptionHolder;", "()V", "displayInfos", "", "Lcom/coui/appcompat/card/BaseDisplayInfo;", "(Ljava/util/List;)V", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", ItemInfo.KEY_VIEW_TYPE, "", "AnimViewGroup", "Companion", "DescriptionHolder", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class CardInstructionDescriptionAdapter extends BaseCardInstructionAdapter<DescriptionHolder> {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);

    @Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J&\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\bH\u0002¨\u0006\n"}, d2 = {"Lcom/coui/appcompat/card/CardInstructionDescriptionAdapter$Companion;", "", "()V", "getAnimViewLayoutParam", "Landroid/widget/LinearLayout$LayoutParams;", "layoutParams", "Landroid/view/ViewGroup$LayoutParams;", "width", "", "height", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static final /* synthetic */ LinearLayout.LayoutParams access$getAnimViewLayoutParam(Companion companion, ViewGroup.LayoutParams layoutParams, int i3, int i4) {
            return companion.getAnimViewLayoutParam(layoutParams, i3, i4);
        }

        private final LinearLayout.LayoutParams getAnimViewLayoutParam(ViewGroup.LayoutParams layoutParams, int width, int height) {
            LinearLayout.LayoutParams layoutParams2 = (layoutParams == null || !(layoutParams instanceof LinearLayout.LayoutParams)) ? new LinearLayout.LayoutParams(-2, -2) : (LinearLayout.LayoutParams) layoutParams;
            layoutParams2.gravity = 1;
            if (width > 0 && height > 0) {
                layoutParams2.height = height;
                layoutParams2.width = width;
            }
            return layoutParams2;
        }

        public static /* synthetic */ LinearLayout.LayoutParams getAnimViewLayoutParam$default(Companion companion, ViewGroup.LayoutParams layoutParams, int i3, int i4, int i5, Object obj) {
            if ((i5 & 2) != 0) {
                i3 = 0;
            }
            if ((i5 & 4) != 0) {
                i4 = 0;
            }
            return companion.getAnimViewLayoutParam(layoutParams, i3, i4);
        }

        private Companion() {
        }
    }

    @Metadata(d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0019\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u0010\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0014H\u0002J\u0010\u0010\u0015\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0016H\u0002R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0017"}, d2 = {"Lcom/coui/appcompat/card/CardInstructionDescriptionAdapter$DescriptionHolder;", "Lcom/coui/appcompat/card/BaseCardInstructionAdapter$BaseHolder;", "itemView", "Landroid/view/View;", "adapter", "Lcom/coui/appcompat/card/BaseCardInstructionAdapter;", "(Lcom/coui/appcompat/card/CardInstructionDescriptionAdapter;Landroid/view/View;Lcom/coui/appcompat/card/BaseCardInstructionAdapter;)V", "animContainer", "Landroid/widget/LinearLayout;", "scrollViewContainer", "Lcom/coui/appcompat/card/COUIMutableSizeScrollView;", "summary", "Landroid/widget/TextView;", "summaryContainer", "title", "bind", "", "displayInfo", "Lcom/coui/appcompat/card/BaseDisplayInfo;", "bindAnimDisplayInfo", "Lcom/coui/appcompat/card/AnimDisplayInfo;", "bindImageDisplayInfo", "Lcom/coui/appcompat/card/ImageDisplayInfo;", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public final class DescriptionHolder extends BaseCardInstructionAdapter.BaseHolder {
        private final LinearLayout animContainer;
        private final COUIMutableSizeScrollView scrollViewContainer;
        private final TextView summary;
        private final LinearLayout summaryContainer;
        final /* synthetic */ CardInstructionDescriptionAdapter this$0;
        private final TextView title;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public DescriptionHolder(CardInstructionDescriptionAdapter cardInstructionDescriptionAdapter, View itemView, BaseCardInstructionAdapter<?> adapter) {
            super(itemView, adapter);
            Intrinsics.checkNotNullParameter(itemView, "itemView");
            Intrinsics.checkNotNullParameter(adapter, "adapter");
            this.this$0 = cardInstructionDescriptionAdapter;
            View viewFindViewById = itemView.findViewById(R.id.anim_container);
            Intrinsics.checkNotNullExpressionValue(viewFindViewById, "findViewById(...)");
            this.animContainer = (LinearLayout) viewFindViewById;
            View viewFindViewById2 = itemView.findViewById(R.id.title);
            Intrinsics.checkNotNullExpressionValue(viewFindViewById2, "findViewById(...)");
            this.title = (TextView) viewFindViewById2;
            View viewFindViewById3 = itemView.findViewById(R.id.summary);
            Intrinsics.checkNotNullExpressionValue(viewFindViewById3, "findViewById(...)");
            this.summary = (TextView) viewFindViewById3;
            View viewFindViewById4 = itemView.findViewById(R.id.summary_container);
            Intrinsics.checkNotNullExpressionValue(viewFindViewById4, "findViewById(...)");
            this.summaryContainer = (LinearLayout) viewFindViewById4;
            View viewFindViewById5 = itemView.findViewById(R.id.content_container);
            Intrinsics.checkNotNullExpressionValue(viewFindViewById5, "findViewById(...)");
            this.scrollViewContainer = (COUIMutableSizeScrollView) viewFindViewById5;
        }

        private final void bindAnimDisplayInfo(AnimDisplayInfo displayInfo) {
            int dimensionPixelSize = this.animContainer.getContext().getResources().getDimensionPixelSize(R.dimen.coui_component_card_instruction_divider_width);
            if (!displayInfo.getAnimAssets().isEmpty() && !displayInfo.getAnimResources().isEmpty()) {
                throw new IllegalArgumentException("imageAssets and imageResources cannot be used at the same time. Please use only one at once.");
            }
            if (displayInfo.getAnimTitles().size() > 0 && displayInfo.getAnimAssets().size() + displayInfo.getAnimResources().size() != displayInfo.getAnimTitles().size()) {
                throw new IllegalArgumentException("the image count must equals to the animTitle count");
            }
            int size = displayInfo.getAnimResources().size();
            for (int i3 = 0; i3 < size; i3++) {
                Context context = this.itemView.getContext();
                Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
                AnimViewGroup animViewGroup = new AnimViewGroup(context);
                animViewGroup.bind(displayInfo, i3);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2);
                layoutParams.weight = 1.0f;
                if (this.animContainer.getChildCount() != 0) {
                    layoutParams.setMarginStart(dimensionPixelSize);
                }
                animViewGroup.getRootView().setLayoutParams(layoutParams);
                this.animContainer.addView(animViewGroup.getRootView());
            }
            int size2 = displayInfo.getAnimAssets().size();
            for (int i4 = 0; i4 < size2; i4++) {
                Context context2 = this.itemView.getContext();
                Intrinsics.checkNotNullExpressionValue(context2, "getContext(...)");
                AnimViewGroup animViewGroup2 = new AnimViewGroup(context2);
                animViewGroup2.bind(displayInfo, i4);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(0, -2);
                layoutParams2.weight = 1.0f;
                if (this.animContainer.getChildCount() != 0) {
                    layoutParams2.setMarginStart(dimensionPixelSize);
                }
                animViewGroup2.getRootView().setLayoutParams(layoutParams2);
                this.animContainer.addView(animViewGroup2.getRootView());
            }
        }

        private final void bindImageDisplayInfo(ImageDisplayInfo displayInfo) {
            if (displayInfo.getAnimTitles().size() > 0 && displayInfo.getImageResources().length != displayInfo.getAnimTitles().size()) {
                throw new IllegalArgumentException("the anim count must equals to the animTitle count");
            }
            int dimensionPixelSize = this.animContainer.getContext().getResources().getDimensionPixelSize(R.dimen.coui_component_card_instruction_divider_width);
            int length = displayInfo.getImageResources().length;
            for (int i3 = 0; i3 < length; i3++) {
                Context context = this.itemView.getContext();
                Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
                AnimViewGroup animViewGroup = new AnimViewGroup(context);
                animViewGroup.bind(displayInfo, i3);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2);
                layoutParams.weight = 1.0f;
                if (this.animContainer.getChildCount() != 0) {
                    layoutParams.setMarginStart(dimensionPixelSize);
                }
                animViewGroup.getRootView().setLayoutParams(layoutParams);
                this.animContainer.addView(animViewGroup.getRootView());
            }
        }

        /* JADX WARN: Code duplicated, block: B:10:0x004d  */
        @Override // com.coui.appcompat.card.BaseCardInstructionAdapter.BaseHolder
        public void bind(BaseDisplayInfo displayInfo) {
            int i3;
            Intrinsics.checkNotNullParameter(displayInfo, "displayInfo");
            BaseCardInstructionAdapter.Companion companion = BaseCardInstructionAdapter.INSTANCE;
            companion.updateContentAndVisibility(this.title, displayInfo.getTitle());
            companion.updateContentAndVisibility(this.summary, displayInfo.getSummary(), this.summaryContainer);
            CharSequence summary = displayInfo.getSummary();
            if (summary instanceof Spanned) {
                Object[] spans = ((Spanned) summary).getSpans(0, summary.length(), ClickableSpan.class);
                Intrinsics.checkNotNullExpressionValue(spans, "getSpans(...)");
                if (spans.length == 0) {
                    this.summary.setMovementMethod(null);
                } else {
                    this.summary.setMovementMethod(LinkMovementMethod.getInstance());
                    this.summary.setHighlightColor(0);
                }
            } else {
                this.summary.setMovementMethod(null);
            }
            if (this.title.getVisibility() == 0) {
                COUIMutableSizeScrollView cOUIMutableSizeScrollView = this.scrollViewContainer;
                cOUIMutableSizeScrollView.setMaxHeight(cOUIMutableSizeScrollView.getResources().getDimensionPixelSize(R.dimen.coui_component_card_instruction_content_height_complete));
                i3 = R.dimen.coui_component_card_instruction_summary_margin_top_small;
            } else {
                COUIMutableSizeScrollView cOUIMutableSizeScrollView2 = this.scrollViewContainer;
                cOUIMutableSizeScrollView2.setMaxHeight(cOUIMutableSizeScrollView2.getResources().getDimensionPixelSize(R.dimen.coui_component_card_instruction_content_height_part));
                i3 = R.dimen.coui_component_card_instruction_summary_margin_top_large;
            }
            LinearLayout linearLayout = this.summaryContainer;
            ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
            Intrinsics.checkNotNull(layoutParams, "null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layoutParams;
            layoutParams2.topMargin = CardInstructionDescriptionAdapterKt.access$getDimenPx(this.summaryContainer, i3);
            linearLayout.setLayoutParams(layoutParams2);
            if (displayInfo instanceof AnimDisplayInfo) {
                bindAnimDisplayInfo((AnimDisplayInfo) displayInfo);
            } else if (displayInfo instanceof ImageDisplayInfo) {
                bindImageDisplayInfo((ImageDisplayInfo) displayInfo);
            }
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CardInstructionDescriptionAdapter(List<BaseDisplayInfo> displayInfos) {
        super(displayInfos);
        Intrinsics.checkNotNullParameter(displayInfos, "displayInfos");
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public /* bridge */ /* synthetic */ RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i3) {
        return onCreateViewHolder(viewGroup, i3);
    }

    public CardInstructionDescriptionAdapter() {
        this(new ArrayList());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public DescriptionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        View viewInflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.coui_component_card_instruction_description_page, parent, false);
        Intrinsics.checkNotNullExpressionValue(viewInflate, "inflate(...)");
        return new DescriptionHolder(this, viewInflate, this);
    }

    @Metadata(d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017J\u0016\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00182\u0006\u0010\u0016\u001a\u00020\u0017R\u0019\u0010\u0005\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0019\u0010\n\u001a\n \u0007*\u0004\u0018\u00010\u000b0\u000b¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u000f¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011¨\u0006\u0019"}, d2 = {"Lcom/coui/appcompat/card/CardInstructionDescriptionAdapter$AnimViewGroup;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "animTitle", "Landroid/widget/TextView;", "kotlin.jvm.PlatformType", "getAnimTitle", "()Landroid/widget/TextView;", "animView", "Lcom/oplus/anim/EffectiveAnimationView;", "getAnimView", "()Lcom/oplus/anim/EffectiveAnimationView;", "rootView", "Landroid/widget/LinearLayout;", "getRootView", "()Landroid/widget/LinearLayout;", "bind", "", "displayInfo", "Lcom/coui/appcompat/card/AnimDisplayInfo;", "index", "", "Lcom/coui/appcompat/card/ImageDisplayInfo;", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class AnimViewGroup {
        private final TextView animTitle;
        private final EffectiveAnimationView animView;
        private final LinearLayout rootView;

        public AnimViewGroup(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            View viewInflate = View.inflate(context, R.layout.coui_component_card_instruction_anim, null);
            Intrinsics.checkNotNull(viewInflate, "null cannot be cast to non-null type android.widget.LinearLayout");
            LinearLayout linearLayout = (LinearLayout) viewInflate;
            this.rootView = linearLayout;
            this.animView = (EffectiveAnimationView) linearLayout.findViewById(R.id.anim_view);
            this.animTitle = (TextView) linearLayout.findViewById(R.id.anim_title);
        }

        public final void bind(ImageDisplayInfo displayInfo, int index) {
            Intrinsics.checkNotNullParameter(displayInfo, "displayInfo");
            if (displayInfo.getAnimTitles().size() > 0) {
                TextView textView = this.animTitle;
                Intrinsics.checkNotNull(textView);
                textView.setVisibility(0);
                this.animTitle.setText(displayInfo.getAnimTitles().get(index));
            }
            this.animView.setImageResource(displayInfo.getImageResources()[index].intValue());
            EffectiveAnimationView effectiveAnimationView = this.animView;
            effectiveAnimationView.setLayoutParams(Companion.access$getAnimViewLayoutParam(CardInstructionDescriptionAdapter.INSTANCE, effectiveAnimationView.getLayoutParams(), displayInfo.getAnimWidth(), displayInfo.getAnimHeight()));
        }

        public final TextView getAnimTitle() {
            return this.animTitle;
        }

        public final EffectiveAnimationView getAnimView() {
            return this.animView;
        }

        public final LinearLayout getRootView() {
            return this.rootView;
        }

        public final void bind(AnimDisplayInfo displayInfo, int index) {
            Intrinsics.checkNotNullParameter(displayInfo, "displayInfo");
            if (displayInfo.getAnimTitles().size() > 0) {
                this.animTitle.setText(displayInfo.getAnimTitles().get(index));
            } else {
                TextView textView = this.animTitle;
                Intrinsics.checkNotNull(textView);
                textView.setVisibility(8);
            }
            if (displayInfo.getAnimResources().size() > 0) {
                this.animView.setAnimation(displayInfo.getAnimResources().get(index).intValue());
                EffectiveAnimationView effectiveAnimationView = this.animView;
                effectiveAnimationView.setLayoutParams(Companion.access$getAnimViewLayoutParam(CardInstructionDescriptionAdapter.INSTANCE, effectiveAnimationView.getLayoutParams(), displayInfo.getAnimWidth(), displayInfo.getAnimHeight()));
            } else {
                this.animView.setAnimation(displayInfo.getAnimAssets().get(index));
                EffectiveAnimationView effectiveAnimationView2 = this.animView;
                effectiveAnimationView2.setLayoutParams(Companion.access$getAnimViewLayoutParam(CardInstructionDescriptionAdapter.INSTANCE, effectiveAnimationView2.getLayoutParams(), displayInfo.getAnimWidth(), displayInfo.getAnimHeight()));
            }
        }
    }
}