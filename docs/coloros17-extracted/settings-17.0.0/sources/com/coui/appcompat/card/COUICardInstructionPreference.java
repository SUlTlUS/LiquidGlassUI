package com.coui.appcompat.card;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.PreferenceViewHolder;
import androidx.viewpager2.widget.ViewPager2;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.indicator.COUIPageIndicator;
import com.coui.appcompat.preference.COUIPreference;
import com.oplus.dmp.sdk.aiask.AIAskManager;
import com.support.card.R;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 '2\u00020\u0001:\u0002'(B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007¢\u0006\u0002\u0010\tJ\u0018\u0010\u0013\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u00122\u0006\u0010\u000b\u001a\u00020\u0007H\u0002J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\u0014\u0010\u0019\u001a\u00020\u00162\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001c0\u001bJ\u000e\u0010\u001d\u001a\u00020\u00162\u0006\u0010\u001e\u001a\u00020\u001fJ\u0018\u0010 \u001a\u00020\u00162\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$H\u0002J\u000e\u0010%\u001a\u00020\u00162\u0006\u0010&\u001a\u00020\u0007R$\u0010\u000b\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u0007@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0011\u001a\u0006\u0012\u0002\b\u00030\u0012X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006)"}, d2 = {"Lcom/coui/appcompat/card/COUICardInstructionPreference;", "Lcom/coui/appcompat/preference/COUIPreference;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "defStyleRes", "(Landroid/content/Context;Landroid/util/AttributeSet;II)V", "value", AIAskManager.TRACE_EVENT_KEY_CARD_TYPE, "getCardType", "()I", "setCardType", "(I)V", "lastPagerItem", "pageAdapter", "Lcom/coui/appcompat/card/BaseCardInstructionAdapter;", "getPageAdapter", "Lcom/coui/appcompat/card/BaseCardInstructionAdapter$BaseHolder;", "onBindViewHolder", "", "holder", "Landroidx/preference/PreferenceViewHolder;", "setDisplayInfos", "displayInfos", "", "Lcom/coui/appcompat/card/BaseDisplayInfo;", "setOnItemSelected", "onItemSelectedListener", "Lcom/coui/appcompat/card/COUICardInstructionPreference$OnItemSelectedListener;", "setPagerCallback", "viewPager", "Landroidx/viewpager2/widget/ViewPager2;", "indicator", "Lcom/coui/appcompat/indicator/COUIPageIndicator;", "setSelectedIndex", "index", "Companion", "OnItemSelectedListener", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class COUICardInstructionPreference extends COUIPreference {
    public static final int CARD_INSTRUCTION_TYPE_DESCRIPTION = 1;
    public static final int CARD_INSTRUCTION_TYPE_SELECTOR = 2;
    private int cardType;
    private int lastPagerItem;
    private BaseCardInstructionAdapter<?> pageAdapter;

    @Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\bæ\u0080\u0001\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&¨\u0006\u0006"}, d2 = {"Lcom/coui/appcompat/card/COUICardInstructionPreference$OnItemSelectedListener;", "", "onItemSelected", "", "index", "", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public interface OnItemSelectedListener {
        void onItemSelected(int index);
    }

    /* JADX INFO: renamed from: com.coui.appcompat.card.COUICardInstructionPreference$setPagerCallback$1, reason: invalid class name */
    @Metadata(d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J \u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0005H\u0016J\u0010\u0010\u000b\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u0005H\u0016¨\u0006\f"}, d2 = {"com/coui/appcompat/card/COUICardInstructionPreference$setPagerCallback$1", "Landroidx/viewpager2/widget/ViewPager2$OnPageChangeCallback;", "onPageScrollStateChanged", "", "state", "", "onPageScrolled", "position", "positionOffset", "", "positionOffsetPixels", "onPageSelected", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class AnonymousClass1 extends ViewPager2.OnPageChangeCallback {
        final /* synthetic */ COUIPageIndicator $indicator;
        final /* synthetic */ COUICardInstructionPreference this$0;

        public AnonymousClass1(COUIPageIndicator cOUIPageIndicator, COUICardInstructionPreference cOUICardInstructionPreference) {
            this.$indicator = cOUIPageIndicator;
            this.this$0 = cOUICardInstructionPreference;
        }

        @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
            this.$indicator.onPageScrollStateChanged(state);
        }

        @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            this.$indicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            this.$indicator.onPageSelected(position);
            COUICardInstructionPreference.access$setLastPagerItem$p(this.this$0, position);
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUICardInstructionPreference(Context context) {
        this(context, null, 0, 0, 14, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public static final /* synthetic */ void access$setLastPagerItem$p(COUICardInstructionPreference cOUICardInstructionPreference, int i3) {
        cOUICardInstructionPreference.lastPagerItem = i3;
    }

    private final BaseCardInstructionAdapter<? extends BaseCardInstructionAdapter.BaseHolder> getPageAdapter(int cardType) {
        if (cardType != 1 && cardType == 2) {
            return new CardInstructionSelectorAdapter();
        }
        return new CardInstructionDescriptionAdapter();
    }

    private final void setPagerCallback(ViewPager2 viewPager, COUIPageIndicator indicator) {
        viewPager.registerOnPageChangeCallback(new AnonymousClass1(indicator, this));
    }

    public final int getCardType() {
        return this.cardType;
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder holder) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        super.onBindViewHolder(holder);
        COUIDarkModeUtil.setForceDarkAllow(holder.itemView, false);
        View viewFindViewById = holder.findViewById(R.id.pager);
        Intrinsics.checkNotNull(viewFindViewById, "null cannot be cast to non-null type androidx.viewpager2.widget.ViewPager2");
        ViewPager2 viewPager2 = (ViewPager2) viewFindViewById;
        View viewFindViewById2 = holder.findViewById(R.id.indicator);
        Intrinsics.checkNotNull(viewFindViewById2, "null cannot be cast to non-null type com.coui.appcompat.indicator.COUIPageIndicator");
        COUIPageIndicator cOUIPageIndicator = (COUIPageIndicator) viewFindViewById2;
        cOUIPageIndicator.setVisibility(this.pageAdapter.getItemCount() > 1 ? 0 : 8);
        if (this.pageAdapter.getItemCount() > 0) {
            viewPager2.setAdapter(this.pageAdapter);
            viewPager2.setCurrentItem(this.lastPagerItem);
            viewPager2.setOffscreenPageLimit(this.pageAdapter.getItemCount());
            cOUIPageIndicator.setDotsCount(this.pageAdapter.getItemCount());
            setPagerCallback(viewPager2, cOUIPageIndicator);
        }
    }

    public final void setCardType(int i3) {
        this.cardType = i3;
        this.pageAdapter = getPageAdapter(i3);
        notifyChanged();
    }

    public final void setDisplayInfos(List<? extends BaseDisplayInfo> displayInfos) {
        Intrinsics.checkNotNullParameter(displayInfos, "displayInfos");
        this.pageAdapter.updateDisplayInfos(displayInfos);
        notifyChanged();
    }

    public final void setOnItemSelected(OnItemSelectedListener onItemSelectedListener) {
        Intrinsics.checkNotNullParameter(onItemSelectedListener, "onItemSelectedListener");
        BaseCardInstructionAdapter<?> baseCardInstructionAdapter = this.pageAdapter;
        CardInstructionSelectorAdapter cardInstructionSelectorAdapter = baseCardInstructionAdapter instanceof CardInstructionSelectorAdapter ? (CardInstructionSelectorAdapter) baseCardInstructionAdapter : null;
        if (cardInstructionSelectorAdapter != null) {
            cardInstructionSelectorAdapter.setOnSelectedCardChangedListener(onItemSelectedListener);
        }
        notifyChanged();
    }

    public final void setSelectedIndex(int index) {
        BaseCardInstructionAdapter<?> baseCardInstructionAdapter = this.pageAdapter;
        CardInstructionSelectorAdapter cardInstructionSelectorAdapter = baseCardInstructionAdapter instanceof CardInstructionSelectorAdapter ? (CardInstructionSelectorAdapter) baseCardInstructionAdapter : null;
        if (cardInstructionSelectorAdapter != null) {
            cardInstructionSelectorAdapter.setSelectedIndex(index);
        }
        notifyChanged();
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUICardInstructionPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUICardInstructionPreference(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0, 8, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public /* synthetic */ COUICardInstructionPreference(Context context, AttributeSet attributeSet, int i3, int i4, int i5, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i5 & 2) != 0 ? null : attributeSet, (i5 & 4) != 0 ? androidx.preference.R.attr.preferenceStyle : i3, (i5 & 8) != 0 ? 0 : i4);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUICardInstructionPreference(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        Intrinsics.checkNotNullParameter(context, "context");
        this.cardType = 1;
        setLayoutResource(R.layout.coui_component_card_instruction_preference);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUICardInstructionPreference, i3, i4);
        setCardType(typedArrayObtainStyledAttributes.getInteger(R.styleable.COUICardInstructionPreference_instructionCardType, 1));
        typedArrayObtainStyledAttributes.recycle();
        this.pageAdapter = getPageAdapter(this.cardType);
    }
}