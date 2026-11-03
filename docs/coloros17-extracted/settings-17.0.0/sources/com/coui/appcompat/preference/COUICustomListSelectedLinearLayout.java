package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import com.coui.appcompat.cardlist.COUICardListSelectedItemLayout;
import com.coui.appcompat.uiutil.UIUtil;
import com.support.preference.R;

/* JADX INFO: loaded from: classes.dex */
public class COUICustomListSelectedLinearLayout extends COUICardListSelectedItemLayout {
    public static final int REMAINING_TOTAL_LINE = 2;
    private boolean mIconMarginDependOnImageView;
    private int mMarginBetweenLine;
    private int mTextContentPaddingTop;
    private boolean mWithDividerItem;

    public COUICustomListSelectedLinearLayout(Context context) {
        this(context, null);
    }

    private int checkFirstTwoLineCenter(TextView textView, int i3, int i4, float[] fArr) {
        if (i4 == 0 || textView == null || textView.getVisibility() != 0 || TextUtils.isEmpty(textView.getText())) {
            return i4;
        }
        Layout layout = textView.getLayout();
        int lineCount = layout.getLineCount();
        if (i4 != 2) {
            if (i4 != 1) {
                return i4;
            }
            fArr[1] = i3 + layout.getLineBottom(0);
            return i4 - 1;
        }
        fArr[0] = layout.getLineTop(0) + i3;
        int i5 = i4 - 1;
        if (lineCount < 2) {
            return i5;
        }
        fArr[1] = i3 + layout.getLineBottom(1);
        return i4 - 2;
    }

    private void init(Context context, AttributeSet attributeSet) {
        setOrientation(0);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUICustomListSelectedLinearLayout);
        this.mWithDividerItem = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUICustomListSelectedLinearLayout_couiPreferenceWithDividerItem, false);
        typedArrayObtainStyledAttributes.recycle();
        this.mTextContentPaddingTop = getContext().getResources().getDimensionPixelSize(R.dimen.support_preference_text_content_padding_top);
        this.mMarginBetweenLine = getContext().getResources().getDimensionPixelSize(R.dimen.support_preference_margin_between_line);
    }

    private boolean operateMultilineIconPosition() {
        AppCompatImageView appCompatImageView;
        int measuredHeight;
        int lineCount;
        int measuredHeight2;
        int lineCount2;
        int iPx2dip;
        View viewFindViewById = findViewById(R.id.img_layout);
        if (viewFindViewById == null || viewFindViewById.getVisibility() != 0 || (appCompatImageView = (AppCompatImageView) findViewById(android.R.id.icon)) == null) {
            return false;
        }
        boolean z2 = findViewById(R.id.messageLayout) != null;
        TextView textView = (TextView) findViewById(android.R.id.title);
        TextView textView2 = (TextView) findViewById(android.R.id.summary);
        if (textView == null || textView.getVisibility() != 0) {
            measuredHeight = 0;
            lineCount = 0;
        } else {
            lineCount = textView.getLineCount();
            measuredHeight = textView.getMeasuredHeight() + this.mMarginBetweenLine;
        }
        if (textView2 == null || textView2.getVisibility() != 0) {
            measuredHeight2 = 0;
            lineCount2 = 0;
        } else {
            lineCount2 = textView2.getLineCount();
            measuredHeight2 = textView2.getMeasuredHeight() + this.mMarginBetweenLine;
        }
        TextView textView3 = (TextView) findViewById(R.id.assignment);
        int lineCount3 = (z2 || textView3 == null || textView3.getVisibility() != 0) ? 0 : textView3.getLineCount();
        if (this.mIconMarginDependOnImageView) {
            iPx2dip = UIUtil.px2dip(getContext(), appCompatImageView.getMeasuredHeight());
        } else {
            iPx2dip = UIUtil.px2dip(getContext(), appCompatImageView.getDrawable() != null ? appCompatImageView.getDrawable().getIntrinsicHeight() : 0);
        }
        int i3 = lineCount + lineCount2 + lineCount3;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewFindViewById.getLayoutParams();
        if (i3 > 2) {
            setIconTopInParent(layoutParams, measuredHeight, measuredHeight2, viewFindViewById);
        } else {
            setIconCenterInParent(layoutParams, iPx2dip, i3);
        }
        viewFindViewById.setLayoutParams(layoutParams);
        return true;
    }

    private void setIconCenterInParent(LinearLayout.LayoutParams layoutParams, int i3, int i4) {
        layoutParams.gravity = 16;
        int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(R.dimen.coui_preference_icon_margin_top);
        if (i3 == 24) {
            dimensionPixelSize = i4 <= 1 ? getContext().getResources().getDimensionPixelSize(R.dimen.coui_preference_24icon_margin_vertical_default) : getContext().getResources().getDimensionPixelSize(R.dimen.coui_preference_24icon_margin_top_multiline);
        } else if (i3 == 32) {
            dimensionPixelSize = i4 <= 1 ? getContext().getResources().getDimensionPixelSize(R.dimen.coui_preference_32icon_margin_vertical_default) : getContext().getResources().getDimensionPixelSize(R.dimen.coui_preference_32icon_margin_top_multiline);
        } else if (i3 == 36) {
            dimensionPixelSize = i4 <= 1 ? getContext().getResources().getDimensionPixelSize(R.dimen.coui_preference_36icon_margin_vertical_default) : getContext().getResources().getDimensionPixelSize(R.dimen.coui_preference_36icon_margin_top_multiline);
        } else if (i3 == 50) {
            dimensionPixelSize = getContext().getResources().getDimensionPixelSize(R.dimen.coui_preference_50icon_margin_vertical_default);
        }
        if (layoutParams.topMargin == dimensionPixelSize && layoutParams.bottomMargin == dimensionPixelSize) {
            return;
        }
        layoutParams.topMargin = dimensionPixelSize;
        layoutParams.bottomMargin = dimensionPixelSize;
    }

    private void setIconTopInParent(LinearLayout.LayoutParams layoutParams, int i3, int i4, View view) {
        layoutParams.gravity = 48;
        float[] fArr = new float[2];
        int i5 = this.mTextContentPaddingTop;
        int i6 = i5 + i3;
        checkFirstTwoLineCenter((TextView) findViewById(R.id.assignment), i3 + i5 + i4, checkFirstTwoLineCenter((TextView) findViewById(android.R.id.summary), i6, checkFirstTwoLineCenter((TextView) findViewById(android.R.id.title), i5, 2, fArr), fArr), fArr);
        int iMax = Math.max((int) (((fArr[0] + fArr[1]) / 2.0f) - (view.getMeasuredHeight() / 2.0f)), getContext().getResources().getDimensionPixelSize(R.dimen.coui_preference_50icon_margin_vertical_default));
        if (layoutParams.topMargin != iMax) {
            layoutParams.topMargin = iMax;
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        super.onMeasure(i3, i4);
        if (operateMultilineIconPosition()) {
            super.onMeasure(i3, i4);
        }
    }

    @Override // com.coui.appcompat.preference.ListSelectedItemLayout, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mWithDividerItem) {
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    public void setIconMarginDependOnImageView(boolean z2) {
        this.mIconMarginDependOnImageView = z2;
    }

    public COUICustomListSelectedLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mWithDividerItem = false;
        init(context, attributeSet);
    }
}