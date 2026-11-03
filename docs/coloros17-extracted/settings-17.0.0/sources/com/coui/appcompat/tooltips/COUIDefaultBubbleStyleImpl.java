package com.coui.appcompat.tooltips;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.coordinatorlayout.widget.ViewGroupUtils;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.textutil.COUIChangeTextUtil;
import com.support.tips.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIDefaultBubbleStyleImpl implements COUIIBubbleStyle {
    private CharSequence mContentText;
    private int mContentTextResId;
    private TextView mContentTv;
    private Context mContext;
    private int[] mDefStyleParams;
    private ImageView mDismissIv;
    private int mMode;
    private ScrollView mScrollView;
    private IToolTipsAction mToolTipsAction;

    /* JADX INFO: renamed from: com.coui.appcompat.tooltips.COUIDefaultBubbleStyleImpl$1, reason: invalid class name */
    public class AnonymousClass1 implements View.OnClickListener {
        public AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            COUIDefaultBubbleStyleImpl.access$200(COUIDefaultBubbleStyleImpl.this).onCloseClick();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.tooltips.COUIDefaultBubbleStyleImpl$2, reason: invalid class name */
    public class AnonymousClass2 implements Runnable {
        final /* synthetic */ int val$insect;
        final /* synthetic */ ViewGroup val$mainPanel;

        public AnonymousClass2(ViewGroup viewGroup, int i3) {
            this.val$mainPanel = viewGroup;
            this.val$insect = i3;
        }

        @Override // java.lang.Runnable
        public void run() {
            Rect rect = new Rect();
            ViewGroupUtils.getDescendantRect(this.val$mainPanel, COUIDefaultBubbleStyleImpl.access$300(COUIDefaultBubbleStyleImpl.this), rect);
            int i3 = this.val$insect;
            rect.inset(-i3, -i3);
            this.val$mainPanel.setTouchDelegate(new TouchDelegate(rect, COUIDefaultBubbleStyleImpl.access$300(COUIDefaultBubbleStyleImpl.this)));
        }
    }

    public static class Builder {
        private CharSequence mContentText;
        private int mContentTextResId;

        public static /* synthetic */ CharSequence access$000(Builder builder) {
            return builder.mContentText;
        }

        public static /* synthetic */ int access$100(Builder builder) {
            return builder.mContentTextResId;
        }

        public COUIDefaultBubbleStyleImpl build() {
            return new COUIDefaultBubbleStyleImpl(this, null);
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
    }

    public /* synthetic */ COUIDefaultBubbleStyleImpl(Builder builder, AnonymousClass1 anonymousClass1) {
        this(builder);
    }

    public static /* synthetic */ IToolTipsAction access$200(COUIDefaultBubbleStyleImpl cOUIDefaultBubbleStyleImpl) {
        return cOUIDefaultBubbleStyleImpl.mToolTipsAction;
    }

    public static /* synthetic */ ImageView access$300(COUIDefaultBubbleStyleImpl cOUIDefaultBubbleStyleImpl) {
        return cOUIDefaultBubbleStyleImpl.mDismissIv;
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
        if (this.mMode == 0) {
            int[] iArr = this.mDefStyleParams;
            iArr[0] = R.attr.couiToolTipsStyle;
            iArr[1] = COUIContextUtil.isCOUIDarkTheme(this.mContext) ? R.style.COUIToolTips_Dark : R.style.COUIToolTips;
        } else {
            int[] iArr2 = this.mDefStyleParams;
            iArr2[0] = R.attr.couiToolTipsDetailFloatingStyle;
            iArr2[1] = COUIContextUtil.isCOUIDarkTheme(this.mContext) ? R.style.COUIToolTips_DetailFloating_Dark : R.style.COUIToolTips_DetailFloating;
        }
        return this.mDefStyleParams;
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public ImageView getDismissIv() {
        return this.mDismissIv;
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public int getLayoutId() {
        return R.layout.coui_tool_tips_layout;
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
    public void hideDismissView() {
        ImageView imageView = this.mDismissIv;
        if (imageView != null) {
            imageView.setVisibility(8);
        }
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public void initBubbleStyle(ViewGroup viewGroup) {
        Context context = this.mContext;
        int[] iArr = R.styleable.COUIToolTips;
        int[] iArr2 = this.mDefStyleParams;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(null, iArr, iArr2[0], iArr2[1]);
        int i3 = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIToolTips_couiToolTipsContainerLayoutGravity, 0);
        int dimensionPixelSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIToolTips_couiToolTipsContainerLayoutMarginStart, 0);
        int dimensionPixelSize2 = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIToolTips_couiToolTipsContainerLayoutMarginTop, 0);
        int dimensionPixelSize3 = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIToolTips_couiToolTipsContainerLayoutMarginEnd, 0);
        int dimensionPixelSize4 = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIToolTips_couiToolTipsContainerLayoutMarginBottom, 0);
        ColorStateList colorStateList = typedArrayObtainStyledAttributes.getColorStateList(R.styleable.COUIToolTips_couiToolTipsContentTextColor);
        typedArrayObtainStyledAttributes.recycle();
        TextView textView = (TextView) viewGroup.findViewById(R.id.contentTv);
        this.mContentTv = textView;
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        ScrollView scrollView = (ScrollView) viewGroup.findViewById(R.id.scrollView);
        this.mScrollView = scrollView;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) scrollView.getLayoutParams();
        layoutParams.gravity = i3;
        layoutParams.setMargins(dimensionPixelSize, dimensionPixelSize2, dimensionPixelSize3, dimensionPixelSize4);
        layoutParams.setMarginStart(dimensionPixelSize);
        layoutParams.setMarginEnd(dimensionPixelSize3);
        this.mScrollView.setLayoutParams(layoutParams);
        this.mContentTv.setTextSize(0, (int) COUIChangeTextUtil.getSuitableFontSize(this.mContext.getResources().getDimensionPixelSize(this.mMode == 0 ? R.dimen.tool_tips_content_text_size : R.dimen.detail_floating_content_text_size), this.mContext.getResources().getConfiguration().fontScale, 4));
        this.mContentTv.setTextAppearance(this.mContext, com.support.appcompat.R.style.couiTextBodyS);
        if (colorStateList != null) {
            this.mContentTv.setTextColor(colorStateList);
        }
        int i4 = this.mContentTextResId;
        if (i4 != 0) {
            this.mContentTv.setText(this.mContext.getString(i4));
        } else if (!TextUtils.isEmpty(this.mContentText)) {
            this.mContentTv.setText(this.mContentText);
        }
        ImageView imageView = (ImageView) viewGroup.findViewById(R.id.dismissIv);
        this.mDismissIv = imageView;
        if (this.mMode == 0) {
            imageView.setVisibility(0);
            this.mDismissIv.setOnClickListener(new AnonymousClass1());
        } else {
            imageView.setVisibility(8);
        }
        this.mDismissIv.post(new AnonymousClass2(viewGroup, this.mContext.getResources().getDimensionPixelOffset(R.dimen.couiToolTipsCancelButtonInsects)));
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public void refreshBubbleStyle(ColorStateList colorStateList) {
        TextView textView = this.mContentTv;
        if (textView == null || colorStateList == null) {
            return;
        }
        textView.setTextColor(colorStateList);
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public void refreshTextResources() {
        int i3;
        TextView textView;
        Context context = this.mContext;
        if (context == null || (i3 = this.mContentTextResId) == 0 || (textView = this.mContentTv) == null) {
            return;
        }
        textView.setText(context.getString(i3));
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

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public void setToolTipsAction(IToolTipsAction iToolTipsAction, Context context, int i3) {
        this.mToolTipsAction = iToolTipsAction;
        this.mContext = context;
        this.mMode = i3;
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public void sizeBubbleStyle(ViewGroup viewGroup, int i3) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mScrollView.getLayoutParams();
        this.mContentTv.setMaxWidth((((i3 - viewGroup.getPaddingLeft()) - viewGroup.getPaddingRight()) - layoutParams.leftMargin) - layoutParams.rightMargin);
    }

    private COUIDefaultBubbleStyleImpl(Builder builder) {
        this.mDefStyleParams = new int[2];
        this.mContentText = Builder.access$000(builder);
        this.mContentTextResId = Builder.access$100(builder);
    }

    @Override // com.coui.appcompat.tooltips.COUIIBubbleStyle
    public void setContentTextColor(ColorStateList colorStateList) {
        TextView textView = this.mContentTv;
        if (textView != null) {
            textView.setTextColor(colorStateList);
        }
    }
}