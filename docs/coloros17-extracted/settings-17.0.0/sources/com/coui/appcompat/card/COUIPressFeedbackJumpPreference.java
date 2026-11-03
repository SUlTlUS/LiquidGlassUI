package com.coui.appcompat.card;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.preference.PreferenceViewHolder;
import com.coui.appcompat.preference.COUIJumpPreference;
import com.coui.appcompat.pressfeedback.COUIPressFeedbackHelper;
import com.support.preference.R;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0016\u0018\u00002\u00020\u0001B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007¢\u0006\u0002\u0010\tJ\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0017R\u001c\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000f¨\u0006\u0014"}, d2 = {"Lcom/coui/appcompat/card/COUIPressFeedbackJumpPreference;", "Lcom/coui/appcompat/preference/COUIJumpPreference;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "defStyleRes", "(Landroid/content/Context;Landroid/util/AttributeSet;II)V", "rootView", "Landroid/view/View;", "getRootView", "()Landroid/view/View;", "setRootView", "(Landroid/view/View;)V", "onBindViewHolder", "", "holder", "Landroidx/preference/PreferenceViewHolder;", "coui-support-card_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class COUIPressFeedbackJumpPreference extends COUIJumpPreference {
    private View rootView;

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIPressFeedbackJumpPreference(Context context) {
        this(context, null, 0, 0, 14, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public static /* synthetic */ boolean c(COUIPressFeedbackJumpPreference cOUIPressFeedbackJumpPreference, COUIPressFeedbackHelper cOUIPressFeedbackHelper, View view, MotionEvent motionEvent) {
        return onBindViewHolder$lambda$0(cOUIPressFeedbackJumpPreference, cOUIPressFeedbackHelper, view, motionEvent);
    }

    private static final boolean onBindViewHolder$lambda$0(COUIPressFeedbackJumpPreference this$0, COUIPressFeedbackHelper pressFeedbackHelper, View view, MotionEvent event) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(pressFeedbackHelper, "$pressFeedbackHelper");
        Intrinsics.checkNotNullParameter(view, "<anonymous parameter 0>");
        Intrinsics.checkNotNullParameter(event, "event");
        View view2 = this$0.rootView;
        if (view2 != null && view2.isEnabled()) {
            int action = event.getAction();
            if (action == 0) {
                pressFeedbackHelper.executeFeedbackAnimator(true);
            } else if (action == 1 || action == 3) {
                pressFeedbackHelper.executeFeedbackAnimator(false);
            }
        }
        return false;
    }

    public final View getRootView() {
        return this.rootView;
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder holder) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        super.onBindViewHolder(holder);
        View view = holder.itemView;
        this.rootView = view;
        COUIPressFeedbackHelper cOUIPressFeedbackHelper = new COUIPressFeedbackHelper(view, 0);
        View view2 = this.rootView;
        if (view2 == null || !view2.isEnabled()) {
            View view3 = this.rootView;
            if (view3 != null) {
                view3.setOnTouchListener(null);
                return;
            }
            return;
        }
        View view4 = this.rootView;
        if (view4 != null) {
            view4.setOnTouchListener(new c(this, cOUIPressFeedbackHelper));
        }
    }

    public final void setRootView(View view) {
        this.rootView = view;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIPressFeedbackJumpPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIPressFeedbackJumpPreference(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0, 8, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public /* synthetic */ COUIPressFeedbackJumpPreference(Context context, AttributeSet attributeSet, int i3, int i4, int i5, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i5 & 2) != 0 ? null : attributeSet, (i5 & 4) != 0 ? R.attr.couiJumpPreferenceStyle : i3, (i5 & 8) != 0 ? 0 : i4);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUIPressFeedbackJumpPreference(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        Intrinsics.checkNotNullParameter(context, "context");
    }
}