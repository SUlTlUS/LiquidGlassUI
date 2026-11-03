package com.coui.appcompat.tooltips;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.textviewcompatutil.COUITextViewCompatUtil;
import com.support.tips.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIIconBubbleStyleImpl implements COUIIBubbleStyle {
    private CharSequence mContentText;
    private int mContentTextResId;
    private TextView mContentTv;
    private Context mContext;
    private int[] mDefStyleParams;
    private CharSequence mDismissText;
    private int mDismissTextResId;
    private Bitmap mIconBitmap;
    private Drawable mIconDrawable;
    private int mIconResId;
    private ImageView mIvIcon;
    private ScrollView mScrollView;
    private CharSequence mTitle;
    private int mTitleResId;
    private IToolTipsAction mToolTipsAction;
    private TextView mTvDismiss;
    private TextView mTvTitle;

    /* JADX INFO: renamed from: com.coui.appcompat.tooltips.COUIIconBubbleStyleImpl$1, reason: invalid class name */
    public class AnonymousClass1 implements View.OnClickListener {
        public AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            COUIIconBubbleStyleImpl.access$900(COUIIconBubbleStyleImpl.this).onCloseClick();
        }
    }

    public static class Builder {
        private CharSequence mContentText;
        private int mContentTextResId;
        private CharSequence mDismissText;
        private int mDismissTextResId;
        private Bitmap mIconBitmap;
        private Drawable mIconDrawable;
        private int mIconResId;
        private CharSequence mTitle;
        private int mTitleResId;

        public static /* synthetic */ int access$000(Builder builder) {
            return builder.mIconResId;
        }

        public static /* synthetic */ Drawable access$100(Builder builder) {
            return builder.mIconDrawable;
        }

        public static /* synthetic */ Bitmap access$200(Builder builder) {
            return builder.mIconBitmap;
        }

        public static /* synthetic */ CharSequence access$300(Builder builder) {
            return builder.mTitle;
        }

        public static /* synthetic */ CharSequence access$400(Builder builder) {
            return builder.mContentText;
        }

        public static /* synthetic */ CharSequence access$500(Builder builder) {
            return builder.mDismissText;
        }

        public static /* synthetic */ int access$600(Builder builder) {
            return builder.mTitleResId;
        }

        public static /* synthetic */ int access$700(Builder builder) {
            return builder.mContentTextResId;
        }

        public static /* synthetic */ int access$800(Builder builder) {
            return builder.mDismissTextResId;
        }

        public COUIIconBubbleStyleImpl build() {
            return new COUIIconBubbleStyleImpl(this, null);
        }

        public Builder loadIcon(int i3) {
            this.mIconResId = i3;
            return this;
        }

        public Builder setContentText(CharSequence charSequence) {
            this.mContentText = charSequence;
            this.mContentTextResId = 0;
            return this;
        }

        public Builder setContentTextRes(int i3) {
            this.mContentTextResId = i3;
            this.mContentText = null;
            return this;
        }

        public Builder setDismissText(CharSequence charSequence) {
            this.mDismissText = charSequence;
            this.mDismissTextResId = 0;
            return this;
        }

        public Builder setDismissTextRes(int i3) {
            this.mDismissTextResId = i3;
            this.mDismissText = null;
            return this;
        }

        public Builder setTitle(CharSequence charSequence) {
            this.mTitle = charSequence;
            this.mTitleResId = 0;
            return this;
        }

        public Builder setTitleRes(int i3) {
            this.mTitleResId = i3;
            this.mTitle = null;
            return this;
        }

        public Builder loadIcon(Drawable drawable) {
            this.mIconDrawable = drawable;
            return this;
        }

        public Builder loadIcon(Bitmap bitmap) {
            this.mIconBitmap = bitmap;
            return this;
        }
    }

    public /* synthetic */ COUIIconBubbleStyleImpl(Builder builder, AnonymousClass1 anonymousClass1) {
        this(builder);
    }

    public static /* synthetic */ IToolTipsAction access$900(COUIIconBubbleStyleImpl cOUIIconBubbleStyleImpl) {
        return cOUIIconBubbleStyleImpl.mToolTipsAction;
    }

    private void refreshIconResource() {
        int i3;
        ImageView imageView = this.mIvIcon;
        if (imageView == null || (i3 = this.mIconResId) == 0) {
            return;
        }
        imageView.setImageResource(i3);
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public void dismissWindow() {
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public TextView getContentView() {
        return this.mContentTv;
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public int[] getDefStyleParams() {
        int[] iArr = this.mDefStyleParams;
        iArr[0] = R.attr.couiToolTipsIconStyle;
        iArr[1] = R.style.COUIToolTips_Icon;
        return iArr;
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public int getLayoutId() {
        return R.layout.coui_tool_tips_icon_style_layout;
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public int getMaxWidth() {
        return this.mContext.getResources().getDimensionPixelSize(R.dimen.tool_tips_max_width);
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public int getRealWidth(int i3, ViewGroup viewGroup) {
        return Math.min(viewGroup.getMeasuredWidth(), i3);
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public void initBubbleStyle(ViewGroup viewGroup) {
        Context context = this.mContext;
        int[] iArr = R.styleable.COUIToolTips;
        int[] iArr2 = this.mDefStyleParams;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(null, iArr, iArr2[0], iArr2[1]);
        ColorStateList colorStateList = typedArrayObtainStyledAttributes.getColorStateList(R.styleable.COUIToolTips_couiToolTipsContentTextColor);
        typedArrayObtainStyledAttributes.recycle();
        this.mIvIcon = (ImageView) viewGroup.findViewById(R.id.iv_icon);
        this.mTvTitle = (TextView) viewGroup.findViewById(R.id.tv_title);
        this.mScrollView = (ScrollView) viewGroup.findViewById(R.id.scrollView);
        this.mContentTv = (TextView) viewGroup.findViewById(R.id.contentTv);
        this.mTvDismiss = (TextView) viewGroup.findViewById(R.id.tv_dismiss);
        int i3 = this.mIconResId;
        if (i3 != 0) {
            this.mIvIcon.setImageResource(i3);
        } else {
            Drawable drawable = this.mIconDrawable;
            if (drawable != null) {
                this.mIvIcon.setImageDrawable(drawable);
            } else {
                Bitmap bitmap = this.mIconBitmap;
                if (bitmap != null) {
                    this.mIvIcon.setImageBitmap(bitmap);
                }
            }
        }
        int i4 = this.mTitleResId;
        if (i4 != 0) {
            this.mTvTitle.setText(this.mContext.getString(i4));
        } else if (!TextUtils.isEmpty(this.mTitle)) {
            this.mTvTitle.setText(this.mTitle);
        }
        this.mContentTv.setMovementMethod(LinkMovementMethod.getInstance());
        if (colorStateList != null) {
            this.mContentTv.setTextColor(colorStateList);
        }
        int i5 = this.mContentTextResId;
        if (i5 != 0) {
            this.mContentTv.setText(this.mContext.getString(i5));
        } else if (!TextUtils.isEmpty(this.mContentText)) {
            this.mContentTv.setText(this.mContentText);
        }
        int i6 = this.mDismissTextResId;
        if (i6 != 0) {
            this.mTvDismiss.setText(this.mContext.getString(i6));
        } else if (!TextUtils.isEmpty(this.mDismissText)) {
            this.mTvDismiss.setText(this.mDismissText);
        }
        this.mTvDismiss.setOnClickListener(new AnonymousClass1());
        COUITextViewCompatUtil.setPressRippleDrawable(this.mTvDismiss);
    }

    public void loadIcon(int i3) {
        this.mIconResId = i3;
        this.mIconDrawable = null;
        this.mIconBitmap = null;
        ImageView imageView = this.mIvIcon;
        if (imageView != null) {
            imageView.setImageResource(i3);
        }
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public void refreshBubbleStyle(ColorStateList colorStateList) {
        Context context;
        Context context2;
        TextView textView = this.mContentTv;
        if (textView != null && colorStateList != null) {
            textView.setTextColor(colorStateList);
        }
        if (this.mTvTitle != null && (context2 = this.mContext) != null) {
            this.mTvTitle.setTextColor(COUIContextUtil.getAttrColor(context2, com.support.appcompat.R.attr.couiColorLabelPrimary));
        }
        if (this.mTvDismiss == null || (context = this.mContext) == null) {
            return;
        }
        this.mTvDismiss.setTextColor(COUIContextUtil.getAttrColor(context, com.support.appcompat.R.attr.couiColorLabelTheme));
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public void refreshTextResources() {
        TextView textView;
        TextView textView2;
        TextView textView3;
        Context context = this.mContext;
        if (context == null) {
            return;
        }
        int i3 = this.mTitleResId;
        if (i3 != 0 && (textView3 = this.mTvTitle) != null) {
            textView3.setText(context.getString(i3));
        }
        int i4 = this.mContentTextResId;
        if (i4 != 0 && (textView2 = this.mContentTv) != null) {
            textView2.setText(this.mContext.getString(i4));
        }
        int i5 = this.mDismissTextResId;
        if (i5 != 0 && (textView = this.mTvDismiss) != null) {
            textView.setText(this.mContext.getString(i5));
        }
        refreshIconResource();
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public void setContentText(CharSequence charSequence) {
        this.mContentTextResId = 0;
        TextView textView = this.mContentTv;
        if (textView != null) {
            textView.setText(charSequence);
        } else {
            this.mContentText = charSequence;
        }
    }

    public void setContentTextColor(int i3) {
        setContentTextColor(ColorStateList.valueOf(i3));
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public void setContentTextRes(int i3) {
        Context context;
        this.mContentTextResId = i3;
        TextView textView = this.mContentTv;
        if (textView == null || (context = this.mContext) == null || i3 == 0) {
            return;
        }
        textView.setText(context.getString(i3));
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public void setContentView(View view) {
        ScrollView scrollView = this.mScrollView;
        if (scrollView != null) {
            scrollView.removeAllViews();
            this.mScrollView.addView(view);
        }
    }

    public void setDismissButtonTextRes(int i3) {
        setDismissTextRes(i3);
    }

    public void setDismissText(CharSequence charSequence) {
        this.mDismissTextResId = 0;
        TextView textView = this.mTvDismiss;
        if (textView != null) {
            textView.setText(charSequence);
        } else {
            this.mDismissText = charSequence;
        }
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public void setDismissTextRes(int i3) {
        Context context;
        this.mDismissTextResId = i3;
        TextView textView = this.mTvDismiss;
        if (textView == null || (context = this.mContext) == null || i3 == 0) {
            return;
        }
        textView.setText(context.getString(i3));
    }

    public void setTitle(CharSequence charSequence) {
        this.mTitleResId = 0;
        TextView textView = this.mTvTitle;
        if (textView != null) {
            textView.setText(charSequence);
        } else {
            this.mTitle = charSequence;
        }
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public void setTitleRes(int i3) {
        Context context;
        this.mTitleResId = i3;
        TextView textView = this.mTvTitle;
        if (textView == null || (context = this.mContext) == null || i3 == 0) {
            return;
        }
        textView.setText(context.getString(i3));
    }

    public void setTitleTextRes(int i3) {
        setTitleRes(i3);
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public void setToolTipsAction(IToolTipsAction iToolTipsAction, Context context, int i3) {
        this.mToolTipsAction = iToolTipsAction;
        this.mContext = context;
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public void sizeBubbleStyle(ViewGroup viewGroup, int i3) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mContentTv.getLayoutParams();
        this.mContentTv.setMaxWidth((((i3 - viewGroup.getPaddingLeft()) - viewGroup.getPaddingRight()) - layoutParams.getMarginStart()) - layoutParams.getMarginEnd());
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.mTvTitle.getLayoutParams();
        this.mTvTitle.setMaxWidth((((i3 - viewGroup.getPaddingLeft()) - viewGroup.getPaddingRight()) - layoutParams2.getMarginStart()) - layoutParams2.getMarginEnd());
        LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) this.mTvDismiss.getLayoutParams();
        this.mTvDismiss.setMaxWidth((((i3 - viewGroup.getPaddingLeft()) - viewGroup.getPaddingRight()) - layoutParams3.getMarginStart()) - layoutParams3.getMarginEnd());
    }

    private COUIIconBubbleStyleImpl(Builder builder) {
        this.mDefStyleParams = new int[2];
        this.mIconResId = Builder.access$000(builder);
        this.mIconDrawable = Builder.access$100(builder);
        this.mIconBitmap = Builder.access$200(builder);
        this.mTitle = Builder.access$300(builder);
        this.mContentText = Builder.access$400(builder);
        this.mDismissText = Builder.access$500(builder);
        this.mTitleResId = Builder.access$600(builder);
        this.mContentTextResId = Builder.access$700(builder);
        this.mDismissTextResId = Builder.access$800(builder);
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public void setContentTextColor(ColorStateList colorStateList) {
        TextView textView = this.mContentTv;
        if (textView != null) {
            textView.setTextColor(colorStateList);
        }
    }

    public void loadIcon(Drawable drawable) {
        this.mIconResId = 0;
        this.mIconDrawable = drawable;
        this.mIconBitmap = null;
        ImageView imageView = this.mIvIcon;
        if (imageView != null) {
            imageView.setImageDrawable(drawable);
        }
    }

    public void loadIcon(Bitmap bitmap) {
        this.mIconResId = 0;
        this.mIconDrawable = null;
        this.mIconBitmap = bitmap;
        ImageView imageView = this.mIvIcon;
        if (imageView != null) {
            imageView.setImageBitmap(bitmap);
        }
    }
}