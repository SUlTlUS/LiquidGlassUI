package com.coui.appcompat.tooltips;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.textviewcompatutil.COUITextViewCompatUtil;
import com.oplus.anim.EffectiveAnimationView;
import com.support.tips.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIImageBubbleStyleImpl implements COUIIBubbleStyle {
    private int mAnimResId;
    private CharSequence mContentText;
    private int mContentTextResId;
    private TextView mContentTv;
    private Context mContext;
    private int[] mDefStyleParams;
    private CharSequence mDismissText;
    private int mDismissTextResId;
    private EffectiveAnimationView mEavAnim;
    private Bitmap mImageBitmap;
    private Drawable mImageDrawable;
    private int mImageResId;
    private ImageView mIvImage;
    private boolean mMediaResourceWithEdges;
    private ScrollView mScrollView;
    private CharSequence mTitle;
    private int mTitleResId;
    private IToolTipsAction mToolTipsAction;
    private TextView mTvDismiss;
    private TextView mTvTitle;

    /* JADX INFO: renamed from: com.coui.appcompat.tooltips.COUIImageBubbleStyleImpl$1, reason: invalid class name */
    public class AnonymousClass1 extends ViewOutlineProvider {
        public AnonymousClass1() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(new Rect(0, 0, view.getWidth(), view.getHeight()), COUIImageBubbleStyleImpl.access$1100(COUIImageBubbleStyleImpl.this).getResources().getDimension(R.dimen.coui_tooltips_imageview_radius));
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.tooltips.COUIImageBubbleStyleImpl$2, reason: invalid class name */
    public class AnonymousClass2 implements View.OnClickListener {
        public AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            COUIImageBubbleStyleImpl.access$1200(COUIImageBubbleStyleImpl.this).onCloseClick();
        }
    }

    public /* synthetic */ COUIImageBubbleStyleImpl(Builder builder, AnonymousClass1 anonymousClass1) {
        this(builder);
    }

    public static /* synthetic */ Context access$1100(COUIImageBubbleStyleImpl cOUIImageBubbleStyleImpl) {
        return cOUIImageBubbleStyleImpl.mContext;
    }

    public static /* synthetic */ IToolTipsAction access$1200(COUIImageBubbleStyleImpl cOUIImageBubbleStyleImpl) {
        return cOUIImageBubbleStyleImpl.mToolTipsAction;
    }

    private void refreshMediaResources() {
        EffectiveAnimationView effectiveAnimationView;
        ImageView imageView;
        if (this.mContext == null) {
            return;
        }
        int i3 = this.mImageResId;
        if (i3 != 0 && (imageView = this.mIvImage) != null) {
            imageView.setImageResource(i3);
            setMediaViewVisibility(true, false);
            return;
        }
        int i4 = this.mAnimResId;
        if (i4 == 0 || (effectiveAnimationView = this.mEavAnim) == null) {
            return;
        }
        effectiveAnimationView.setAnimation(i4);
        this.mEavAnim.loop(true);
        this.mEavAnim.playAnimation();
        setMediaViewVisibility(false, true);
    }

    private void setMediaViewVisibility(boolean z2, boolean z3) {
        ImageView imageView = this.mIvImage;
        if (imageView != null) {
            imageView.setVisibility(z2 ? 0 : 8);
        }
        EffectiveAnimationView effectiveAnimationView = this.mEavAnim;
        if (effectiveAnimationView != null) {
            effectiveAnimationView.setVisibility(z3 ? 0 : 8);
        }
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public void dismissWindow() {
        EffectiveAnimationView effectiveAnimationView = this.mEavAnim;
        if (effectiveAnimationView == null || !effectiveAnimationView.isAnimating()) {
            return;
        }
        this.mEavAnim.cancelAnimation();
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public TextView getContentView() {
        return this.mContentTv;
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public int[] getDefStyleParams() {
        int[] iArr = this.mDefStyleParams;
        iArr[0] = R.attr.couiToolTipsImageStyle;
        iArr[1] = R.style.COUIToolTips_Image;
        return iArr;
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public int getLayoutId() {
        return this.mMediaResourceWithEdges ? R.layout.coui_tool_tips_image_with_edges_style_layout : R.layout.coui_tool_tips_image_no_edges_style_layout;
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public int getMaxWidth() {
        return this.mContext.getResources().getDimensionPixelSize(R.dimen.tool_tips_image_style_max_width);
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public int getRealWidth(int i3, ViewGroup viewGroup) {
        return i3;
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public void initBubbleStyle(ViewGroup viewGroup) {
        Context context = this.mContext;
        int[] iArr = R.styleable.COUIToolTips;
        int[] iArr2 = this.mDefStyleParams;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(null, iArr, iArr2[0], iArr2[1]);
        ColorStateList colorStateList = typedArrayObtainStyledAttributes.getColorStateList(R.styleable.COUIToolTips_couiToolTipsContentTextColor);
        typedArrayObtainStyledAttributes.recycle();
        this.mIvImage = (ImageView) viewGroup.findViewById(R.id.iv_image);
        this.mEavAnim = (EffectiveAnimationView) viewGroup.findViewById(R.id.eav_anim);
        this.mTvTitle = (TextView) viewGroup.findViewById(R.id.tv_title);
        this.mScrollView = (ScrollView) viewGroup.findViewById(R.id.scrollView);
        this.mContentTv = (TextView) viewGroup.findViewById(R.id.contentTv);
        this.mTvDismiss = (TextView) viewGroup.findViewById(R.id.tv_dismiss);
        if (this.mMediaResourceWithEdges) {
            this.mEavAnim.setOutlineProvider(new AnonymousClass1());
            this.mEavAnim.setClipToOutline(true);
        }
        int i3 = this.mImageResId;
        if (i3 != 0) {
            this.mIvImage.setImageResource(i3);
            setMediaViewVisibility(true, false);
        } else {
            Drawable drawable = this.mImageDrawable;
            if (drawable != null) {
                this.mIvImage.setImageDrawable(drawable);
                setMediaViewVisibility(true, false);
            } else {
                Bitmap bitmap = this.mImageBitmap;
                if (bitmap != null) {
                    this.mIvImage.setImageBitmap(bitmap);
                    setMediaViewVisibility(true, false);
                } else {
                    int i4 = this.mAnimResId;
                    if (i4 != 0) {
                        this.mEavAnim.setAnimation(i4);
                        this.mEavAnim.loop(true);
                        this.mEavAnim.playAnimation();
                        setMediaViewVisibility(false, true);
                    } else {
                        setMediaViewVisibility(false, false);
                    }
                }
            }
        }
        int i5 = this.mTitleResId;
        if (i5 != 0) {
            this.mTvTitle.setText(this.mContext.getString(i5));
        } else if (!TextUtils.isEmpty(this.mTitle)) {
            this.mTvTitle.setText(this.mTitle);
        }
        this.mContentTv.setMovementMethod(LinkMovementMethod.getInstance());
        if (colorStateList != null) {
            this.mContentTv.setTextColor(colorStateList);
        }
        int i6 = this.mContentTextResId;
        if (i6 != 0) {
            this.mContentTv.setText(this.mContext.getString(i6));
        } else if (!TextUtils.isEmpty(this.mContentText)) {
            this.mContentTv.setText(this.mContentText);
        }
        int i7 = this.mDismissTextResId;
        if (i7 != 0) {
            this.mTvDismiss.setText(this.mContext.getString(i7));
        } else if (!TextUtils.isEmpty(this.mDismissText)) {
            this.mTvDismiss.setText(this.mDismissText);
        }
        this.mTvDismiss.setOnClickListener(new AnonymousClass2());
        COUITextViewCompatUtil.setPressRippleDrawable(this.mTvDismiss);
    }

    public void loadAnim(int i3) {
        this.mAnimResId = i3;
        this.mImageResId = 0;
        this.mImageDrawable = null;
        this.mImageBitmap = null;
        EffectiveAnimationView effectiveAnimationView = this.mEavAnim;
        if (effectiveAnimationView != null) {
            effectiveAnimationView.setAnimation(i3);
            this.mEavAnim.loop(true);
            this.mEavAnim.playAnimation();
            setMediaViewVisibility(false, true);
        }
    }

    public void loadImage(int i3) {
        this.mImageResId = i3;
        this.mAnimResId = 0;
        this.mImageDrawable = null;
        this.mImageBitmap = null;
        ImageView imageView = this.mIvImage;
        if (imageView != null) {
            imageView.setImageResource(i3);
            setMediaViewVisibility(true, false);
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
        refreshMediaResources();
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
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mContentTv.getLayoutParams();
        this.mContentTv.setMaxWidth((((i3 - viewGroup.getPaddingLeft()) - viewGroup.getPaddingRight()) - layoutParams.getMarginStart()) - layoutParams.getMarginEnd());
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mTvTitle.getLayoutParams();
        this.mTvTitle.setMaxWidth((((i3 - viewGroup.getPaddingLeft()) - viewGroup.getPaddingRight()) - layoutParams2.getMarginStart()) - layoutParams2.getMarginEnd());
        LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) this.mTvDismiss.getLayoutParams();
        this.mTvDismiss.setMaxWidth((((i3 - viewGroup.getPaddingLeft()) - viewGroup.getPaddingRight()) - layoutParams3.getMarginStart()) - layoutParams3.getMarginEnd());
    }

    private COUIImageBubbleStyleImpl(Builder builder) {
        this.mDefStyleParams = new int[2];
        this.mMediaResourceWithEdges = true;
        this.mMediaResourceWithEdges = Builder.access$000(builder);
        this.mAnimResId = Builder.access$100(builder);
        this.mImageResId = Builder.access$200(builder);
        this.mImageDrawable = Builder.access$300(builder);
        this.mImageBitmap = Builder.access$400(builder);
        this.mTitle = Builder.access$500(builder);
        this.mContentText = Builder.access$600(builder);
        this.mDismissText = Builder.access$700(builder);
        this.mTitleResId = Builder.access$800(builder);
        this.mContentTextResId = Builder.access$900(builder);
        this.mDismissTextResId = Builder.access$1000(builder);
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public void setContentTextColor(ColorStateList colorStateList) {
        TextView textView = this.mContentTv;
        if (textView != null) {
            textView.setTextColor(colorStateList);
        }
    }

    public static class Builder {
        private int mAnimResId;
        private CharSequence mContentText;
        private int mContentTextResId;
        private CharSequence mDismissText;
        private int mDismissTextResId;
        private Bitmap mImageBitmap;
        private Drawable mImageDrawable;
        private int mImageResId;
        private boolean mMediaResourceWithEdges = true;
        private CharSequence mTitle;
        private int mTitleResId;

        public static /* synthetic */ boolean access$000(Builder builder) {
            return builder.mMediaResourceWithEdges;
        }

        public static /* synthetic */ int access$100(Builder builder) {
            return builder.mAnimResId;
        }

        public static /* synthetic */ int access$1000(Builder builder) {
            return builder.mDismissTextResId;
        }

        public static /* synthetic */ int access$200(Builder builder) {
            return builder.mImageResId;
        }

        public static /* synthetic */ Drawable access$300(Builder builder) {
            return builder.mImageDrawable;
        }

        public static /* synthetic */ Bitmap access$400(Builder builder) {
            return builder.mImageBitmap;
        }

        public static /* synthetic */ CharSequence access$500(Builder builder) {
            return builder.mTitle;
        }

        public static /* synthetic */ CharSequence access$600(Builder builder) {
            return builder.mContentText;
        }

        public static /* synthetic */ CharSequence access$700(Builder builder) {
            return builder.mDismissText;
        }

        public static /* synthetic */ int access$800(Builder builder) {
            return builder.mTitleResId;
        }

        public static /* synthetic */ int access$900(Builder builder) {
            return builder.mContentTextResId;
        }

        public COUIImageBubbleStyleImpl build() {
            return new COUIImageBubbleStyleImpl(this, null);
        }

        public Builder loadAnim(int i3) {
            this.mAnimResId = i3;
            this.mImageResId = 0;
            this.mImageDrawable = null;
            this.mImageBitmap = null;
            return this;
        }

        public Builder loadImage(int i3) {
            this.mImageResId = i3;
            this.mAnimResId = 0;
            this.mImageDrawable = null;
            this.mImageBitmap = null;
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

        public Builder setMediaResourceWithEdges(boolean z2) {
            this.mMediaResourceWithEdges = z2;
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

        public Builder loadImage(Drawable drawable) {
            this.mImageDrawable = drawable;
            this.mImageResId = 0;
            this.mAnimResId = 0;
            this.mImageBitmap = null;
            return this;
        }

        public Builder loadImage(Bitmap bitmap) {
            this.mImageBitmap = bitmap;
            this.mImageResId = 0;
            this.mAnimResId = 0;
            this.mImageDrawable = null;
            return this;
        }
    }

    public void loadImage(Drawable drawable) {
        this.mImageResId = 0;
        this.mAnimResId = 0;
        this.mImageBitmap = null;
        this.mImageDrawable = drawable;
        ImageView imageView = this.mIvImage;
        if (imageView != null) {
            imageView.setImageDrawable(drawable);
            setMediaViewVisibility(true, false);
        }
    }

    public void loadImage(Bitmap bitmap) {
        this.mImageResId = 0;
        this.mAnimResId = 0;
        this.mImageDrawable = null;
        this.mImageBitmap = bitmap;
        ImageView imageView = this.mIvImage;
        if (imageView != null) {
            imageView.setImageBitmap(bitmap);
            setMediaViewVisibility(true, false);
        }
    }
}