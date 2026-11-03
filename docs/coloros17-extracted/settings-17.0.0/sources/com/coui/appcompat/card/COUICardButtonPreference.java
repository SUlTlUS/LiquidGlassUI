package com.coui.appcompat.card;

import android.R;
import android.content.Context;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u0000 \u00122\u00020\u0001:\u0001\u0012B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007¢\u0006\u0002\u0010\tJ\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016J\f\u0010\u000e\u001a\u00020\u0007*\u00020\u000fH\u0002J\f\u0010\u0010\u001a\u00020\u0011*\u00020\u000fH\u0002¨\u0006\u0013"}, d2 = {"Lcom/coui/appcompat/card/COUICardButtonPreference;", "Lcom/coui/appcompat/card/COUIPressFeedbackJumpPreference;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "defStyleRes", "(Landroid/content/Context;Landroid/util/AttributeSet;II)V", "onBindViewHolder", "", "holder", "Landroidx/preference/PreferenceViewHolder;", "getAvailableWidth", "Landroid/widget/TextView;", "isOverFlowed", "", "Companion", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class COUICardButtonPreference extends COUIPressFeedbackJumpPreference {
    public static final float CARD_BUTTON_TEXT_SIZE_NORMAL = 12.0f;
    public static final float CARD_BUTTON_TEXT_SIZE_SMALL = 10.0f;

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUICardButtonPreference(Context context) {
        this(context, null, 0, 0, 14, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public static /* synthetic */ void d(COUICardButtonPreference cOUICardButtonPreference, TextView textView) {
        onBindViewHolder$lambda$0(cOUICardButtonPreference, textView);
    }

    private final int getAvailableWidth(TextView textView) {
        return (textView.getWidth() - textView.getPaddingLeft()) - textView.getPaddingRight();
    }

    private final boolean isOverFlowed(TextView textView) {
        TextPaint paint = textView.getPaint();
        Intrinsics.checkNotNullExpressionValue(paint, "getPaint(...)");
        return paint.measureText(textView.getText().toString()) > ((float) getAvailableWidth(textView));
    }

    private static final void onBindViewHolder$lambda$0(COUICardButtonPreference this$0, TextView title) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(title, "$title");
        if (this$0.isOverFlowed(title)) {
            title.setTextSize(10.0f);
        } else {
            title.setTextSize(12.0f);
        }
    }

    @Override // com.coui.appcompat.card.COUIPressFeedbackJumpPreference, com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder holder) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        super.onBindViewHolder(holder);
        COUIDarkModeUtil.setForceDarkAllow(holder.itemView, false);
        View viewFindViewById = holder.findViewById(R.id.title);
        Intrinsics.checkNotNull(viewFindViewById, "null cannot be cast to non-null type android.widget.TextView");
        TextView textView = (TextView) viewFindViewById;
        textView.post(new b(this, textView));
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUICardButtonPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUICardButtonPreference(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0, 8, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public /* synthetic */ COUICardButtonPreference(Context context, AttributeSet attributeSet, int i3, int i4, int i5, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i5 & 2) != 0 ? null : attributeSet, (i5 & 4) != 0 ? com.support.preference.R.attr.couiJumpPreferenceStyle : i3, (i5 & 8) != 0 ? 0 : i4);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUICardButtonPreference(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        Intrinsics.checkNotNullParameter(context, "context");
        setLayoutResource(com.support.card.R.layout.coui_component_card_button_preference);
    }
}