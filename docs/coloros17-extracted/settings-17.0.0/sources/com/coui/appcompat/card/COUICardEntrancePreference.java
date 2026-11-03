package com.coui.appcompat.card;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.theme.COUIThemeOverlay;
import com.oplus.dmp.sdk.aiask.AIAskManager;
import com.support.card.R;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\r\n\u0002\b\u0004\b\u0016\u0018\u0000 *2\u00020\u0001:\u0001*B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007¢\u0006\u0002\u0010\tJ\u0010\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!H\u0002J\u0010\u0010\"\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u0007H\u0002J\u0010\u0010#\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!H\u0004J\u0010\u0010$\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!H\u0016J\u0016\u0010%\u001a\u00020\u001f2\u0006\u0010&\u001a\u00020'2\u0006\u0010\u0011\u001a\u00020\u0010J\u0012\u0010%\u001a\u00020\u001f2\b\u0010&\u001a\u0004\u0018\u00010'H\u0016J\u0010\u0010%\u001a\u00020\u001f2\u0006\u0010(\u001a\u00020\u0007H\u0016J\u0010\u0010)\u001a\u00020\u001f2\u0006\u0010\u0016\u001a\u00020\u0010H\u0007R$\u0010\u000b\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u0007@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR$\u0010\u0011\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\u0010@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R$\u0010\u0016\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\u0010@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0013\"\u0004\b\u0018\u0010\u0015R\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u001aX\u0082\u000e¢\u0006\u0002\n\u0000R$\u0010\u001b\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u0007@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\r\"\u0004\b\u001d\u0010\u000f¨\u0006+"}, d2 = {"Lcom/coui/appcompat/card/COUICardEntrancePreference;", "Lcom/coui/appcompat/card/COUIPressFeedbackJumpPreference;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "defStyleRes", "(Landroid/content/Context;Landroid/util/AttributeSet;II)V", "value", AIAskManager.TRACE_EVENT_KEY_CARD_TYPE, "getCardType", "()I", "setCardType", "(I)V", "", "showSummary", "getShowSummary", "()Z", "setShowSummary", "(Z)V", "statusOn", "getStatusOn", "setStatusOn", "summaryView", "Landroid/widget/TextView;", "tintIcon", "getTintIcon", "setTintIcon", "applyIconToTintType", "", "holder", "Landroidx/preference/PreferenceViewHolder;", "getLayoutResByCardType", "initSummaryView", "onBindViewHolder", "setSummary", "summary", "", "summaryResId", "setSummaryStatus", "Companion", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class COUICardEntrancePreference extends COUIPressFeedbackJumpPreference {
    public static final int CARD_TYPE_LARGE = 2;
    public static final int CARD_TYPE_SMALL = 1;
    public static final int TINT_ICON_ANYWAY = 2;
    public static final int TINT_ICON_BY_GLOBAL_THEME = 1;
    public static final int TINT_ICON_NONE = 0;
    private int cardType;
    private boolean showSummary;
    private boolean statusOn;
    private TextView summaryView;
    private int tintIcon;
    private static final int LAYOUT_RES_TYPE_SMALL = R.layout.coui_component_card_entrance_preference_type_small;
    private static final int LAYOUT_RES_TYPE_LARGE = R.layout.coui_component_card_entrance_preference_type_large;

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUICardEntrancePreference(Context context) {
        this(context, null, 0, 0, 14, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    private final void applyIconToTintType(PreferenceViewHolder holder) {
        int i3 = this.tintIcon;
        if (i3 == 2 || i3 == 1) {
            COUIThemeOverlay cOUIThemeOverlay = COUIThemeOverlay.getInstance();
            Context context = getContext();
            View viewFindViewById = holder.findViewById(android.R.id.icon);
            cOUIThemeOverlay.applyCOUITintIcon(context, viewFindViewById instanceof ImageView ? (ImageView) viewFindViewById : null, this.tintIcon == 2);
        }
    }

    private final int getLayoutResByCardType(int cardType) {
        if (cardType != 1 && cardType == 2) {
            return LAYOUT_RES_TYPE_LARGE;
        }
        return LAYOUT_RES_TYPE_SMALL;
    }

    public final int getCardType() {
        return this.cardType;
    }

    public final boolean getShowSummary() {
        return this.showSummary;
    }

    public final boolean getStatusOn() {
        return this.statusOn;
    }

    public final int getTintIcon() {
        return this.tintIcon;
    }

    public final void initSummaryView(PreferenceViewHolder holder) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        View viewFindViewById = holder.findViewById(android.R.id.summary);
        Intrinsics.checkNotNull(viewFindViewById, "null cannot be cast to non-null type android.widget.TextView");
        TextView textView = (TextView) viewFindViewById;
        this.summaryView = textView;
        if (textView != null) {
            COUIDarkModeUtil.setForceDarkAllow(textView, false);
        }
        setSummaryStatus(this.statusOn);
    }

    @Override // com.coui.appcompat.card.COUIPressFeedbackJumpPreference, com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder holder) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        super.onBindViewHolder(holder);
        COUIDarkModeUtil.setForceDarkAllow(holder.itemView, false);
        initSummaryView(holder);
        applyIconToTintType(holder);
    }

    public final void setCardType(int i3) {
        setLayoutResource(getLayoutResByCardType(i3));
        this.cardType = i3;
        notifyChanged();
    }

    public final void setShowSummary(boolean z2) {
        this.showSummary = z2;
        notifyChanged();
    }

    public final void setStatusOn(boolean z2) {
        this.statusOn = z2;
        notifyChanged();
    }

    @Override // androidx.preference.Preference
    public void setSummary(CharSequence summary) {
        if (this.showSummary) {
            super.setSummary(summary);
        } else {
            setStatusText1(summary);
        }
    }

    public final void setSummaryStatus(boolean statusOn) {
        int attrColor = COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorSecondNeutral, 0);
        int attrColor2 = COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorPrimaryText, 0);
        TextView textView = this.summaryView;
        if (textView != null) {
            if (statusOn) {
                attrColor = attrColor2;
            }
            textView.setTextColor(attrColor);
        }
    }

    public final void setTintIcon(int i3) {
        this.tintIcon = i3;
        notifyChanged();
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUICardEntrancePreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUICardEntrancePreference(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0, 8, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public /* synthetic */ COUICardEntrancePreference(Context context, AttributeSet attributeSet, int i3, int i4, int i5, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i5 & 2) != 0 ? null : attributeSet, (i5 & 4) != 0 ? com.support.preference.R.attr.couiJumpPreferenceStyle : i3, (i5 & 8) != 0 ? 0 : i4);
    }

    @Override // androidx.preference.Preference
    public void setSummary(int summaryResId) {
        setSummary(getContext().getString(summaryResId));
    }

    public final void setSummary(CharSequence summary, boolean showSummary) {
        Intrinsics.checkNotNullParameter(summary, "summary");
        if (showSummary) {
            super.setSummary(summary);
        } else {
            setSummary(summary);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUICardEntrancePreference(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        Intrinsics.checkNotNullParameter(context, "context");
        this.cardType = 1;
        this.showSummary = true;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUICardEntrancePreference, i3, i4);
        setCardType(typedArrayObtainStyledAttributes.getInteger(R.styleable.COUICardEntrancePreference_entranceCardType, 1));
        setShowSummary(typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUICardEntrancePreference_showSummary, true));
        setTintIcon(typedArrayObtainStyledAttributes.getInteger(R.styleable.COUICardEntrancePreference_tintIcon, 0));
        typedArrayObtainStyledAttributes.recycle();
    }
}