package com.coui.appcompat.card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.oplus.anim.EffectiveAnimationView;
import com.oplus.settings.custom.quickapp.ItemInfo;
import com.support.card.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0002\u001a\u001bB\u0007\b\u0016¢\u0006\u0002\u0010\u0003B\u0013\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0002\u0010\u0007J\u001c\u0010\u0010\u001a\u00020\u00112\n\u0010\u0012\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0013\u001a\u00020\tH\u0016J\u001c\u0010\u0014\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\tH\u0016J\u0010\u0010\u0018\u001a\u00020\u00112\u0006\u0010\u0019\u001a\u00020\tH\u0007R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u001c\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000f¨\u0006\u001c"}, d2 = {"Lcom/coui/appcompat/card/CardInstructionSelectorAdapter;", "Lcom/coui/appcompat/card/BaseCardInstructionAdapter;", "Lcom/coui/appcompat/card/CardInstructionSelectorAdapter$SelectorHolder;", "()V", "displayInfos", "", "Lcom/coui/appcompat/card/BaseDisplayInfo;", "(Ljava/util/List;)V", "lastSelectedIndex", "", "onSelectedCardChangedListener", "Lcom/coui/appcompat/card/COUICardInstructionPreference$OnItemSelectedListener;", "getOnSelectedCardChangedListener", "()Lcom/coui/appcompat/card/COUICardInstructionPreference$OnItemSelectedListener;", "setOnSelectedCardChangedListener", "(Lcom/coui/appcompat/card/COUICardInstructionPreference$OnItemSelectedListener;)V", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", ItemInfo.KEY_VIEW_TYPE, "setSelectedIndex", "index", "Selector", "SelectorHolder", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class CardInstructionSelectorAdapter extends BaseCardInstructionAdapter<SelectorHolder> {
    private int lastSelectedIndex;
    private COUICardInstructionPreference.OnItemSelectedListener onSelectedCardChangedListener;

    @Metadata(d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0018R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0011\u001a\u00020\u0012¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014¨\u0006\u001a"}, d2 = {"Lcom/coui/appcompat/card/CardInstructionSelectorAdapter$Selector;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "animView", "Lcom/oplus/anim/EffectiveAnimationView;", "getAnimView", "()Lcom/oplus/anim/EffectiveAnimationView;", "radio", "Landroid/widget/RadioButton;", "getRadio", "()Landroid/widget/RadioButton;", "rootView", "Landroid/view/View;", "getRootView", "()Landroid/view/View;", "title", "Landroid/widget/TextView;", "getTitle", "()Landroid/widget/TextView;", "setAnimViewSize", "", "width", "", "height", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Selector {
        private final EffectiveAnimationView animView;
        private final RadioButton radio;
        private final View rootView;
        private final TextView title;

        public Selector(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            View viewInflate = View.inflate(context, R.layout.coui_component_card_instruction_selector, null);
            Intrinsics.checkNotNullExpressionValue(viewInflate, "inflate(...)");
            this.rootView = viewInflate;
            View viewFindViewById = viewInflate.findViewById(R.id.anim_view);
            Intrinsics.checkNotNullExpressionValue(viewFindViewById, "findViewById(...)");
            this.animView = (EffectiveAnimationView) viewFindViewById;
            View viewFindViewById2 = viewInflate.findViewById(R.id.title);
            Intrinsics.checkNotNullExpressionValue(viewFindViewById2, "findViewById(...)");
            this.title = (TextView) viewFindViewById2;
            View viewFindViewById3 = viewInflate.findViewById(R.id.radio);
            Intrinsics.checkNotNullExpressionValue(viewFindViewById3, "findViewById(...)");
            this.radio = (RadioButton) viewFindViewById3;
        }

        public final EffectiveAnimationView getAnimView() {
            return this.animView;
        }

        public final RadioButton getRadio() {
            return this.radio;
        }

        public final View getRootView() {
            return this.rootView;
        }

        public final TextView getTitle() {
            return this.title;
        }

        public final void setAnimViewSize(int width, int height) {
            if (width <= 0 || height <= 0) {
                return;
            }
            EffectiveAnimationView effectiveAnimationView = this.animView;
            ViewGroup.LayoutParams layoutParams = effectiveAnimationView.getLayoutParams();
            Intrinsics.checkNotNull(layoutParams, "null cannot be cast to non-null type androidx.constraintlayout.widget.ConstraintLayout.LayoutParams");
            ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) layoutParams;
            ((ViewGroup.MarginLayoutParams) layoutParams2).height = height;
            ((ViewGroup.MarginLayoutParams) layoutParams2).width = width;
            effectiveAnimationView.setLayoutParams(layoutParams2);
        }
    }

    @Metadata(d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0019\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0010\u0010\u0010\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0011H\u0002J\u0010\u0010\u0012\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0013H\u0002J\b\u0010\u0014\u001a\u00020\rH\u0002J\u0010\u0010\u0015\u001a\u00020\r2\u0006\u0010\u0016\u001a\u00020\u0017H\u0007R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0018"}, d2 = {"Lcom/coui/appcompat/card/CardInstructionSelectorAdapter$SelectorHolder;", "Lcom/coui/appcompat/card/BaseCardInstructionAdapter$BaseHolder;", "itemView", "Landroid/view/View;", "adapter", "Lcom/coui/appcompat/card/BaseCardInstructionAdapter;", "(Lcom/coui/appcompat/card/CardInstructionSelectorAdapter;Landroid/view/View;Lcom/coui/appcompat/card/BaseCardInstructionAdapter;)V", "selectorContainer", "Landroid/widget/LinearLayout;", "selectorGroup", "", "Lcom/coui/appcompat/card/CardInstructionSelectorAdapter$Selector;", "bind", "", "displayInfo", "Lcom/coui/appcompat/card/BaseDisplayInfo;", "bindAnimDisplayInfo", "Lcom/coui/appcompat/card/AnimDisplayInfo;", "bindImageDisplayInfo", "Lcom/coui/appcompat/card/ImageDisplayInfo;", "dealRadioGroupClickEvents", "setSelectedChoiceIndex", "index", "", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public final class SelectorHolder extends BaseCardInstructionAdapter.BaseHolder {
        private final LinearLayout selectorContainer;
        private final List<Selector> selectorGroup;
        final /* synthetic */ CardInstructionSelectorAdapter this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SelectorHolder(CardInstructionSelectorAdapter cardInstructionSelectorAdapter, View itemView, BaseCardInstructionAdapter<?> adapter) {
            super(itemView, adapter);
            Intrinsics.checkNotNullParameter(itemView, "itemView");
            Intrinsics.checkNotNullParameter(adapter, "adapter");
            this.this$0 = cardInstructionSelectorAdapter;
            this.selectorGroup = new ArrayList();
            View viewFindViewById = itemView.findViewById(R.id.container);
            Intrinsics.checkNotNullExpressionValue(viewFindViewById, "findViewById(...)");
            this.selectorContainer = (LinearLayout) viewFindViewById;
        }

        public static /* synthetic */ void b(SelectorHolder selectorHolder, Selector selector, CardInstructionSelectorAdapter cardInstructionSelectorAdapter, View view) {
            dealRadioGroupClickEvents$lambda$1$lambda$0(selectorHolder, selector, cardInstructionSelectorAdapter, view);
        }

        private final void bindAnimDisplayInfo(AnimDisplayInfo displayInfo) {
            if (!displayInfo.getAnimAssets().isEmpty() && !displayInfo.getAnimResources().isEmpty()) {
                throw new IllegalArgumentException("imageAssets and imageResources cannot be used at the same time. Please use only one at once.");
            }
            if (displayInfo.getAnimAssets().size() + displayInfo.getAnimResources().size() != displayInfo.getChoices().size()) {
                throw new IllegalArgumentException("the anim count must equal to the choice count");
            }
            Iterator<Integer> it = displayInfo.getAnimResources().iterator();
            int i3 = 0;
            int i4 = 0;
            while (it.hasNext()) {
                int iIntValue = it.next().intValue();
                Context context = this.itemView.getContext();
                Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
                Selector selector = new Selector(context);
                BaseCardInstructionAdapter.INSTANCE.updateContentAndVisibility(selector.getTitle(), displayInfo.getChoices().get(i4));
                selector.getAnimView().setAnimation(iIntValue);
                selector.setAnimViewSize(displayInfo.getAnimWidth(), displayInfo.getAnimHeight());
                this.selectorContainer.addView(selector.getRootView());
                this.selectorGroup.add(selector);
                i4++;
            }
            for (String str : displayInfo.getAnimAssets()) {
                Context context2 = this.itemView.getContext();
                Intrinsics.checkNotNullExpressionValue(context2, "getContext(...)");
                Selector selector2 = new Selector(context2);
                BaseCardInstructionAdapter.INSTANCE.updateContentAndVisibility(selector2.getTitle(), displayInfo.getChoices().get(i3));
                selector2.getAnimView().setAnimation(str);
                selector2.setAnimViewSize(displayInfo.getAnimWidth(), displayInfo.getAnimHeight());
                this.selectorContainer.addView(selector2.getRootView());
                this.selectorGroup.add(selector2);
                i3++;
            }
        }

        private final void bindImageDisplayInfo(ImageDisplayInfo displayInfo) {
            if (displayInfo.getImageResources().length != displayInfo.getChoices().size()) {
                throw new IllegalArgumentException("the image count must equal to the choice count");
            }
            Integer[] imageResources = displayInfo.getImageResources();
            int length = imageResources.length;
            for (int i3 = 0; i3 < length; i3++) {
                int iIntValue = imageResources[i3].intValue();
                Context context = this.itemView.getContext();
                Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
                Selector selector = new Selector(context);
                BaseCardInstructionAdapter.INSTANCE.updateContentAndVisibility(selector.getTitle(), displayInfo.getChoices().get(i3));
                selector.getAnimView().setImageResource(iIntValue);
                selector.setAnimViewSize(displayInfo.getAnimWidth(), displayInfo.getAnimHeight());
                this.selectorContainer.addView(selector.getRootView());
                this.selectorGroup.add(selector);
            }
        }

        private final void dealRadioGroupClickEvents() {
            List<Selector> list = this.selectorGroup;
            CardInstructionSelectorAdapter cardInstructionSelectorAdapter = this.this$0;
            for (Selector selector : list) {
                selector.getRootView().setOnClickListener(new d(this, selector, cardInstructionSelectorAdapter));
            }
        }

        private static final void dealRadioGroupClickEvents$lambda$1$lambda$0(SelectorHolder this$0, Selector selector, CardInstructionSelectorAdapter this$1, View view) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(selector, "$selector");
            Intrinsics.checkNotNullParameter(this$1, "this$1");
            int iIndexOf = this$0.selectorGroup.indexOf(selector);
            if (iIndexOf != CardInstructionSelectorAdapter.access$getLastSelectedIndex$p(this$1)) {
                CardInstructionSelectorAdapter.access$setLastSelectedIndex$p(this$1, iIndexOf);
                COUICardInstructionPreference.OnItemSelectedListener onSelectedCardChangedListener = this$1.getOnSelectedCardChangedListener();
                if (onSelectedCardChangedListener != null) {
                    onSelectedCardChangedListener.onItemSelected(iIndexOf);
                }
            }
            this$0.setSelectedChoiceIndex(iIndexOf);
        }

        @Override // com.coui.appcompat.card.BaseCardInstructionAdapter.BaseHolder
        public void bind(BaseDisplayInfo displayInfo) {
            Intrinsics.checkNotNullParameter(displayInfo, "displayInfo");
            this.selectorGroup.clear();
            this.selectorContainer.removeAllViews();
            if (displayInfo instanceof AnimDisplayInfo) {
                bindAnimDisplayInfo((AnimDisplayInfo) displayInfo);
            } else if (displayInfo instanceof ImageDisplayInfo) {
                bindImageDisplayInfo((ImageDisplayInfo) displayInfo);
            }
            dealRadioGroupClickEvents();
            setSelectedChoiceIndex(displayInfo.getSelectedIndex());
        }

        public final void setSelectedChoiceIndex(int index) {
            if (index < 0 || index >= this.selectorGroup.size()) {
                return;
            }
            Selector selector = this.selectorGroup.get(index);
            selector.getRadio().setChecked(true);
            selector.getTitle().setTextAppearance(com.support.appcompat.R.style.couiTextAppearanceButton);
            selector.getTitle().setTextColor(COUIContextUtil.getAttrColor(this.itemView.getContext(), com.support.appcompat.R.attr.couiColorPrimary));
            List<Selector> list = this.selectorGroup;
            ArrayList<Selector> arrayList = new ArrayList();
            for (Object obj : list) {
                if (!Intrinsics.areEqual((Selector) obj, selector)) {
                    arrayList.add(obj);
                }
            }
            for (Selector selector2 : arrayList) {
                selector2.getRadio().setChecked(false);
                selector2.getTitle().setTextAppearance(com.support.appcompat.R.style.couiTextAppearanceBody);
                selector2.getTitle().setTextColor(COUIContextUtil.getAttrColor(this.itemView.getContext(), com.support.appcompat.R.attr.couiColorSecondNeutral));
            }
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CardInstructionSelectorAdapter(List<BaseDisplayInfo> displayInfos) {
        super(displayInfos);
        Intrinsics.checkNotNullParameter(displayInfos, "displayInfos");
        this.lastSelectedIndex = -1;
    }

    public static final /* synthetic */ int access$getLastSelectedIndex$p(CardInstructionSelectorAdapter cardInstructionSelectorAdapter) {
        return cardInstructionSelectorAdapter.lastSelectedIndex;
    }

    public static final /* synthetic */ void access$setLastSelectedIndex$p(CardInstructionSelectorAdapter cardInstructionSelectorAdapter, int i3) {
        cardInstructionSelectorAdapter.lastSelectedIndex = i3;
    }

    public final COUICardInstructionPreference.OnItemSelectedListener getOnSelectedCardChangedListener() {
        return this.onSelectedCardChangedListener;
    }

    @Override // com.coui.appcompat.card.BaseCardInstructionAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public /* bridge */ /* synthetic */ void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i3) {
        onBindViewHolder((SelectorHolder) viewHolder, i3);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public /* bridge */ /* synthetic */ RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i3) {
        return onCreateViewHolder(viewGroup, i3);
    }

    public final void setOnSelectedCardChangedListener(COUICardInstructionPreference.OnItemSelectedListener onItemSelectedListener) {
        this.onSelectedCardChangedListener = onItemSelectedListener;
    }

    public final void setSelectedIndex(int index) {
        this.lastSelectedIndex = index;
        notifyDataSetChanged();
    }

    @Override // com.coui.appcompat.card.BaseCardInstructionAdapter
    public /* bridge */ /* synthetic */ void onBindViewHolder(BaseCardInstructionAdapter.BaseHolder baseHolder, int i3) {
        onBindViewHolder((SelectorHolder) baseHolder, i3);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public SelectorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        View viewInflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.coui_component_card_instruction_selector_page, parent, false);
        Intrinsics.checkNotNullExpressionValue(viewInflate, "inflate(...)");
        return new SelectorHolder(this, viewInflate, this);
    }

    public CardInstructionSelectorAdapter() {
        this(new ArrayList());
    }

    public void onBindViewHolder(SelectorHolder holder, int position) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        super.onBindViewHolder(holder, position);
        holder.setSelectedChoiceIndex(this.lastSelectedIndex);
    }
}