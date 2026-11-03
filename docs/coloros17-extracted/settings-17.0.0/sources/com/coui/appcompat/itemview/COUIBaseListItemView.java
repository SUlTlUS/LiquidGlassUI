package com.coui.appcompat.itemview;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.ICOUIBaseListItemView;
import com.coui.appcompat.imageview.COUIRoundImageView;
import com.coui.appcompat.preference.COUICustomListSelectedLinearLayout;
import com.coui.appcompat.reddot.COUIHintRedDot;

/* JADX INFO: loaded from: classes7.dex */
public class COUIBaseListItemView extends RelativeLayout implements ICOUIBaseListItemView {
    public static final int CIRCLE = 0;
    private static final int DEFAULT_RADIUS = 14;
    private static final int DELAY_TIME = 70;
    public static final int FORCE_CLICKABLE = 1;
    public static final int FORCE_UNCLICKABLE = 2;
    private static final int PER_HEIGHT = 6;
    public static final int ROUND = 1;
    private COUIRoundImageView mAssignIconView;
    private COUIHintRedDot mAssignRedDotView;
    private TextView mAssignView;
    private Context mContext;
    private COUIHintRedDot mEndRedDotView;
    private boolean mHasBorder;
    private COUIHintRedDot mIconRedDotView;
    private int mIconStyle;
    private COUIRoundImageView mIconView;
    private boolean mIsCustom;
    private boolean mItemEnabled;
    private int mRadius;
    private View mRootItemView;
    private TextView mSummaryView;
    private TextView mTitleView;
    private ViewGroup mWidgetFrame;

    /* JADX INFO: renamed from: com.coui.appcompat.itemview.COUIBaseListItemView$1, reason: invalid class name */
    public class AnonymousClass1 implements View.OnTouchListener {
        public AnonymousClass1() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int actionMasked = motionEvent.getActionMasked();
            int selectionStart = COUIBaseListItemView.access$000(COUIBaseListItemView.this).getSelectionStart();
            int selectionEnd = COUIBaseListItemView.access$000(COUIBaseListItemView.this).getSelectionEnd();
            int offsetForPosition = COUIBaseListItemView.access$000(COUIBaseListItemView.this).getOffsetForPosition(motionEvent.getX(), motionEvent.getY());
            boolean z2 = selectionStart == selectionEnd || offsetForPosition <= selectionStart || offsetForPosition >= selectionEnd;
            if (actionMasked != 0) {
                if (actionMasked == 1 || actionMasked == 3) {
                    COUIBaseListItemView.access$000(COUIBaseListItemView.this).setPressed(false);
                    COUIBaseListItemView.access$000(COUIBaseListItemView.this).postInvalidateDelayed(70L);
                }
            } else {
                if (z2) {
                    return false;
                }
                COUIBaseListItemView.access$000(COUIBaseListItemView.this).setPressed(true);
                COUIBaseListItemView.access$000(COUIBaseListItemView.this).invalidate();
            }
            return false;
        }
    }

    public COUIBaseListItemView(Context context) {
        this(context, null);
    }

    public static /* synthetic */ TextView access$000(COUIBaseListItemView cOUIBaseListItemView) {
        return cOUIBaseListItemView.mSummaryView;
    }

    private void setEnableState(View view, boolean z2) {
        view.setEnabled(z2);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
                setEnableState(viewGroup.getChildAt(childCount), z2);
            }
        }
    }

    private void setIconMarginDependOnImageView(boolean z2) {
        View view = this.mRootItemView;
        if (view instanceof COUICustomListSelectedLinearLayout) {
            ((COUICustomListSelectedLinearLayout) view).setIconMarginDependOnImageView(z2);
        }
    }

    public ImageView getAssignIconView() {
        return this.mAssignIconView;
    }

    public ImageView getIconView() {
        return this.mIconView;
    }

    @Override // androidx.recyclerview.widget.ICOUIBaseListItemView
    public boolean getItemEnabled() {
        return this.mItemEnabled;
    }

    public final View getRootItemView() {
        return this.mRootItemView;
    }

    public final void setAssignIcon(Drawable drawable) {
        COUIRoundImageView cOUIRoundImageView = this.mAssignIconView;
        if (cOUIRoundImageView != null) {
            if (drawable == null) {
                cOUIRoundImageView.setVisibility(8);
            } else {
                cOUIRoundImageView.setImageDrawable(drawable);
                this.mAssignIconView.setVisibility(0);
            }
        }
    }

    public void setAssignRedDotMode(int i3) {
        COUIHintRedDot cOUIHintRedDot = this.mAssignRedDotView;
        if (cOUIHintRedDot != null) {
            if (i3 == 0) {
                cOUIHintRedDot.setVisibility(8);
                return;
            }
            cOUIHintRedDot.setLaidOut();
            this.mAssignRedDotView.setVisibility(0);
            this.mAssignRedDotView.setPointMode(i3);
            this.mAssignRedDotView.invalidate();
        }
    }

    public final void setAssignment(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            this.mAssignView.setVisibility(8);
        } else {
            this.mAssignView.setText(charSequence);
            this.mAssignView.setVisibility(0);
        }
    }

    public void setAssignmentColor(int i3) {
        if (i3 != 0) {
            this.mAssignView.setTextColor(i3);
        }
    }

    public void setClickableStyle(int i3) {
        if (i3 == 1) {
            this.mRootItemView.setClickable(false);
        } else {
            if (i3 != 2) {
                return;
            }
            this.mRootItemView.setClickable(true);
        }
    }

    public void setCustomIconRadius(boolean z2) {
        this.mIsCustom = z2;
        setIconStyle(this.mRadius, this.mHasBorder, this.mIconStyle, z2);
    }

    @Deprecated
    public final void setEnable(boolean z2) {
        setEnableState(this, z2);
    }

    public void setEndRedDotMode(int i3, int i4) {
        if (i3 == 0) {
            this.mEndRedDotView.setVisibility(8);
            return;
        }
        this.mEndRedDotView.setLaidOut();
        this.mEndRedDotView.setVisibility(0);
        this.mEndRedDotView.setPointMode(i3);
        this.mEndRedDotView.setPointNumber(i4);
        this.mEndRedDotView.invalidate();
    }

    public final void setIcon(Drawable drawable) {
        if (drawable == null) {
            this.mIconView.setVisibility(8);
        } else {
            this.mIconView.setImageDrawable(drawable);
            this.mIconView.setVisibility(0);
        }
    }

    public void setIconBorderRadius(int i3) {
        this.mRadius = i3;
        setIconStyle(i3, this.mHasBorder, this.mIconStyle, this.mIsCustom);
    }

    public void setIconHasBorder(boolean z2) {
        this.mHasBorder = z2;
        setIconStyle(this.mRadius, z2, this.mIconStyle, this.mIsCustom);
    }

    public void setIconRedDotMode(int i3) {
        if (i3 == 0) {
            this.mIconRedDotView.setVisibility(8);
            return;
        }
        this.mIconRedDotView.setLaidOut();
        this.mIconRedDotView.setVisibility(0);
        this.mIconRedDotView.setPointMode(i3);
        this.mIconRedDotView.invalidate();
    }

    public void setIconStyle(int i3) {
        if (i3 == 0 || i3 == 1) {
            this.mIconStyle = i3;
            setIconStyle(this.mRadius, this.mHasBorder, i3, this.mIsCustom);
        }
    }

    public final void setItemBackground(Drawable drawable) {
        this.mRootItemView.setBackground(drawable);
    }

    @Override // androidx.recyclerview.widget.ICOUIBaseListItemView
    public void setItemEnabled(boolean z2) {
        if (this.mItemEnabled != z2) {
            this.mItemEnabled = z2;
            setEnableState(this, z2);
        }
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.mRootItemView.setOnClickListener(onClickListener);
    }

    public void setPaddingEnd(int i3) {
        View view = this.mRootItemView;
        view.setPaddingRelative(view.getPaddingStart(), this.mRootItemView.getPaddingTop(), i3, this.mRootItemView.getPaddingBottom());
    }

    public void setPaddingStart(int i3) {
        View view = this.mRootItemView;
        view.setPaddingRelative(i3, view.getPaddingTop(), this.mRootItemView.getPaddingEnd(), this.mRootItemView.getPaddingBottom());
    }

    public void setPaddingStartAndEnd(int i3, int i4) {
        View view = this.mRootItemView;
        view.setPaddingRelative(i3, view.getPaddingTop(), i4, this.mRootItemView.getPaddingBottom());
    }

    public final void setSummary(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            this.mSummaryView.setVisibility(8);
        } else {
            this.mSummaryView.setText(charSequence);
            this.mSummaryView.setVisibility(0);
        }
    }

    public void setSummaryClickSpan() {
        this.mSummaryView.setHighlightColor(getContext().getResources().getColor(R.color.transparent));
        this.mSummaryView.setMovementMethod(LinkMovementMethod.getInstance());
        this.mSummaryView.setOnTouchListener(new AnonymousClass1());
    }

    public void setSummaryColor(ColorStateList colorStateList) {
        if (colorStateList != null) {
            this.mSummaryView.setTextColor(colorStateList);
        }
    }

    public final void setTitle(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            this.mTitleView.setVisibility(8);
        } else {
            this.mTitleView.setText(charSequence);
            this.mTitleView.setVisibility(0);
        }
    }

    public void setTitleColor(ColorStateList colorStateList) {
        if (colorStateList != null) {
            this.mTitleView.setTextColor(colorStateList);
        }
    }

    public final void setWidgetView(int i3) {
        ViewGroup viewGroup = this.mWidgetFrame;
        if (viewGroup != null) {
            if (i3 == 0) {
                viewGroup.setVisibility(8);
                return;
            }
            viewGroup.setVisibility(0);
            this.mWidgetFrame.removeAllViews();
            View.inflate(this.mContext, i3, this.mWidgetFrame);
        }
    }

    public COUIBaseListItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIBaseListItemView(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0);
    }

    public final void setIconStyle(int i3, boolean z2, int i4, boolean z3) {
        if (z3) {
            this.mIconView.setHasBorder(z2);
            this.mIconView.setBorderRectRadius(0);
            this.mIconView.setType(i4);
            return;
        }
        Drawable drawable = this.mIconView.getDrawable();
        if (drawable != null && i3 == 14) {
            i3 = drawable.getIntrinsicHeight() / 6;
            Resources resources = getContext().getResources();
            int i5 = com.support.preference.R.dimen.coui_preference_icon_min_radius;
            if (i3 < resources.getDimensionPixelOffset(i5)) {
                i3 = getContext().getResources().getDimensionPixelOffset(i5);
            } else {
                Resources resources2 = getContext().getResources();
                int i6 = com.support.preference.R.dimen.coui_preference_icon_max_radius;
                if (i3 > resources2.getDimensionPixelOffset(i6)) {
                    i3 = getContext().getResources().getDimensionPixelOffset(i6);
                }
            }
        }
        this.mIconView.setHasBorder(z2);
        this.mIconView.setBorderRectRadius(i3);
        this.mIconView.setType(i4);
    }

    public COUIBaseListItemView(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mItemEnabled = true;
        this.mRadius = 14;
        this.mIconStyle = 1;
        this.mContext = context;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, com.support.preference.R.styleable.COUIBaseListItemView, i3, i4);
        boolean z2 = typedArrayObtainStyledAttributes.getBoolean(com.support.preference.R.styleable.COUIBaseListItemView_assignInRightAsMainLayout, true);
        boolean z3 = typedArrayObtainStyledAttributes.getBoolean(com.support.preference.R.styleable.COUIBaseListItemView_iconMarginDependOnImageView, false);
        this.mItemEnabled = typedArrayObtainStyledAttributes.getBoolean(com.support.preference.R.styleable.COUIBaseListItemView_itemEnabled, true);
        CharSequence text = typedArrayObtainStyledAttributes.getText(com.support.preference.R.styleable.COUIBaseListItemView_title);
        CharSequence text2 = typedArrayObtainStyledAttributes.getText(com.support.preference.R.styleable.COUIBaseListItemView_summary);
        Drawable drawable = typedArrayObtainStyledAttributes.getDrawable(com.support.preference.R.styleable.COUIBaseListItemView_icon);
        CharSequence text3 = typedArrayObtainStyledAttributes.getText(com.support.preference.R.styleable.COUIBaseListItemView_assignment);
        Drawable drawable2 = typedArrayObtainStyledAttributes.getDrawable(com.support.preference.R.styleable.COUIBaseListItemView_assignmentIcon);
        int resourceId = typedArrayObtainStyledAttributes.getResourceId(com.support.preference.R.styleable.COUIBaseListItemView_widgetLayout, 0);
        typedArrayObtainStyledAttributes.recycle();
        View viewInflate = View.inflate(context, z2 ? com.support.preference.R.layout.coui_preference_assignment_in_right : com.support.preference.R.layout.coui_preference, this);
        this.mRootItemView = viewInflate.findViewById(com.support.preference.R.id.coui_preference);
        View viewFindViewById = viewInflate.findViewById(com.support.preference.R.id.img_layout);
        this.mIconView = (COUIRoundImageView) viewInflate.findViewById(R.id.icon);
        this.mIconRedDotView = (COUIHintRedDot) viewInflate.findViewById(com.support.preference.R.id.img_red_dot);
        this.mTitleView = (TextView) viewInflate.findViewById(R.id.title);
        this.mSummaryView = (TextView) viewInflate.findViewById(R.id.summary);
        this.mEndRedDotView = (COUIHintRedDot) viewInflate.findViewById(com.support.preference.R.id.jump_icon_red_dot);
        this.mAssignRedDotView = (COUIHintRedDot) viewInflate.findViewById(com.support.preference.R.id.assignment_red_dot);
        this.mAssignIconView = (COUIRoundImageView) viewInflate.findViewById(com.support.preference.R.id.assignment_icon);
        this.mAssignView = (TextView) viewInflate.findViewById(com.support.preference.R.id.assignment);
        this.mWidgetFrame = (ViewGroup) viewInflate.findViewById(R.id.widget_frame);
        this.mRootItemView.setClickable(true);
        setIconMarginDependOnImageView(z3);
        viewFindViewById.setVisibility(0);
        setTitle(text);
        setSummary(text2);
        setIcon(drawable);
        setAssignment(text3);
        setAssignIcon(drawable2);
        setWidgetView(resourceId);
        setIconStyle(this.mRadius, this.mHasBorder, this.mIconStyle, this.mIsCustom);
        setEnableState(this, this.mItemEnabled);
    }

    public void setWidgetView(View view) {
        ViewGroup viewGroup = this.mWidgetFrame;
        if (viewGroup != null) {
            if (view != null) {
                viewGroup.setVisibility(0);
                this.mWidgetFrame.removeAllViews();
                this.mWidgetFrame.addView(view);
                return;
            }
            viewGroup.setVisibility(8);
        }
    }
}