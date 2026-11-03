package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import com.coui.appcompat.imageview.COUIRoundImageView;
import com.support.preference.R;

/* JADX INFO: loaded from: classes.dex */
public class COUIPreferenceUtils {
    static final int DEFALUT_RADIUS = 14;
    static final int DELAY_TIME = 70;
    public static final int ICON_SIZE_DP_LARGE = 50;
    public static final int ICON_SIZE_DP_MEDIUM = 32;
    public static final int ICON_SIZE_DP_MEDIUM_LARGE = 36;
    public static final int ICON_SIZE_DP_SMALL = 24;

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIPreferenceUtils$1, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass1 implements View.OnTouchListener {
        final /* synthetic */ TextView val$summaryView;

        public AnonymousClass1(TextView textView) {
            this.val$summaryView = textView;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int actionMasked = motionEvent.getActionMasked();
            int selectionStart = this.val$summaryView.getSelectionStart();
            int selectionEnd = this.val$summaryView.getSelectionEnd();
            int offsetForPosition = this.val$summaryView.getOffsetForPosition(motionEvent.getX(), motionEvent.getY());
            boolean z2 = selectionStart == selectionEnd || offsetForPosition <= selectionStart || offsetForPosition >= selectionEnd;
            if (actionMasked != 0) {
                if (actionMasked == 1 || actionMasked == 3) {
                    this.val$summaryView.setPressed(false);
                    this.val$summaryView.postInvalidateDelayed(70L);
                }
            } else {
                if (z2) {
                    return false;
                }
                this.val$summaryView.setPressed(true);
                this.val$summaryView.invalidate();
            }
            return false;
        }
    }

    public static void bindAssignmentView(PreferenceViewHolder preferenceViewHolder, CharSequence charSequence, int i3) {
        TextView textView = (TextView) preferenceViewHolder.findViewById(R.id.assignment);
        if (textView != null) {
            if (TextUtils.isEmpty(charSequence)) {
                textView.setVisibility(8);
                return;
            }
            textView.setText(charSequence);
            textView.setVisibility(0);
            if (i3 != 0) {
                textView.setTextColor(i3);
            }
        }
    }

    public static void bindView(PreferenceViewHolder preferenceViewHolder, Drawable drawable, CharSequence charSequence, CharSequence charSequence2) {
        bindView(preferenceViewHolder, drawable, charSequence, charSequence2, 0);
    }

    public static void setIconStyle(PreferenceViewHolder preferenceViewHolder, Context context, int i3, boolean z2, int i4, boolean z3) {
        View viewFindViewById = preferenceViewHolder.findViewById(android.R.id.icon);
        if (viewFindViewById == null || !(viewFindViewById instanceof COUIRoundImageView)) {
            return;
        }
        if (z3) {
            COUIRoundImageView cOUIRoundImageView = (COUIRoundImageView) viewFindViewById;
            cOUIRoundImageView.setHasBorder(z2);
            cOUIRoundImageView.setBorderRectRadius(0);
            cOUIRoundImageView.setType(i4);
            return;
        }
        COUIRoundImageView cOUIRoundImageView2 = (COUIRoundImageView) viewFindViewById;
        Drawable drawable = cOUIRoundImageView2.getDrawable();
        if (drawable != null && i3 == 14) {
            i3 = drawable.getIntrinsicHeight() / 6;
            Resources resources = context.getResources();
            int i5 = R.dimen.coui_preference_icon_min_radius;
            if (i3 < resources.getDimensionPixelOffset(i5)) {
                i3 = context.getResources().getDimensionPixelOffset(i5);
            } else {
                Resources resources2 = context.getResources();
                int i6 = R.dimen.coui_preference_icon_max_radius;
                if (i3 > resources2.getDimensionPixelOffset(i6)) {
                    i3 = context.getResources().getDimensionPixelOffset(i6);
                }
            }
        }
        cOUIRoundImageView2.setHasBorder(z2);
        cOUIRoundImageView2.setBorderRectRadius(i3);
        cOUIRoundImageView2.setType(i4);
    }

    public static void setSummaryView(Context context, PreferenceViewHolder preferenceViewHolder) {
        TextView textView = (TextView) preferenceViewHolder.findViewById(android.R.id.summary);
        if (textView != null) {
            textView.setHighlightColor(context.getResources().getColor(android.R.color.transparent));
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setOnTouchListener(new AnonymousClass1(textView));
        }
    }

    public static void setSummaryViewColor(PreferenceViewHolder preferenceViewHolder, ColorStateList colorStateList) {
        TextView textView = (TextView) preferenceViewHolder.findViewById(android.R.id.summary);
        if (textView == null || colorStateList == null) {
            return;
        }
        textView.setTextColor(colorStateList);
    }

    public static void setTitleViewColor(Context context, PreferenceViewHolder preferenceViewHolder, ColorStateList colorStateList) {
        View viewFindViewById = preferenceViewHolder.findViewById(android.R.id.title);
        if (viewFindViewById == null || colorStateList == null) {
            return;
        }
        ((TextView) viewFindViewById).setTextColor(colorStateList);
    }

    public static void bindView(PreferenceViewHolder preferenceViewHolder, Drawable drawable, CharSequence charSequence, CharSequence charSequence2, int i3) {
        ImageView imageView = (ImageView) preferenceViewHolder.findViewById(R.id.coui_preference_widget_jump);
        if (imageView != null) {
            if (drawable != null) {
                imageView.setImageDrawable(drawable);
                imageView.setVisibility(0);
            } else {
                imageView.setVisibility(8);
            }
        }
        View viewFindViewById = preferenceViewHolder.findViewById(android.R.id.icon);
        View viewFindViewById2 = preferenceViewHolder.findViewById(R.id.img_layout);
        if (viewFindViewById2 != null) {
            if (viewFindViewById != null) {
                viewFindViewById2.setVisibility(viewFindViewById.getVisibility());
            } else {
                viewFindViewById2.setVisibility(8);
            }
        }
        TextView textView = (TextView) preferenceViewHolder.findViewById(R.id.coui_statusText1);
        if (textView != null) {
            if (TextUtils.isEmpty(charSequence)) {
                textView.setVisibility(8);
            } else {
                textView.setText(charSequence);
                textView.setVisibility(0);
            }
        }
        bindAssignmentView(preferenceViewHolder, charSequence2, i3);
    }
}