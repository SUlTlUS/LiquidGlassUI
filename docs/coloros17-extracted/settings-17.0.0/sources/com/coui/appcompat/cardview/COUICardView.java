package com.coui.appcompat.cardview;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/* JADX INFO: loaded from: classes7.dex */
public class COUICardView extends FrameLayout {
    private static final int[] COLOR_BACKGROUND_ATTR = {R.attr.colorBackground};
    private static final CardViewImpl IMPL;
    private final CardViewDelegate mCardViewDelegate;
    private boolean mCompatPadding;
    final Rect mContentPadding;
    private boolean mPreventCornerOverlap;
    final Rect mShadowBounds;
    int mUserSetMinHeight;
    int mUserSetMinWidth;

    /* JADX INFO: renamed from: com.coui.appcompat.cardview.COUICardView$1, reason: invalid class name */
    public class AnonymousClass1 implements CardViewDelegate {
        private Drawable mCardBackground;

        public AnonymousClass1() {
        }

        @Override // com.coui.appcompat.cardview.CardViewDelegate
        public Drawable getCardBackground() {
            return this.mCardBackground;
        }

        @Override // com.coui.appcompat.cardview.CardViewDelegate
        public View getCardView() {
            return COUICardView.this;
        }

        @Override // com.coui.appcompat.cardview.CardViewDelegate
        public boolean getPreventCornerOverlap() {
            return COUICardView.this.getPreventCornerOverlap();
        }

        @Override // com.coui.appcompat.cardview.CardViewDelegate
        public boolean getUseCompatPadding() {
            return COUICardView.this.getUseCompatPadding();
        }

        @Override // com.coui.appcompat.cardview.CardViewDelegate
        public void setCardBackground(Drawable drawable) {
            this.mCardBackground = drawable;
            COUICardView.this.setBackgroundDrawable(drawable);
        }

        @Override // com.coui.appcompat.cardview.CardViewDelegate
        public void setMinWidthHeightInternal(int i3, int i4) {
            COUICardView cOUICardView = COUICardView.this;
            if (i3 > cOUICardView.mUserSetMinWidth) {
                COUICardView.access$101(cOUICardView, i3);
            }
            COUICardView cOUICardView2 = COUICardView.this;
            if (i4 > cOUICardView2.mUserSetMinHeight) {
                COUICardView.access$201(cOUICardView2, i4);
            }
        }

        @Override // com.coui.appcompat.cardview.CardViewDelegate
        public void setShadowPadding(int i3, int i4, int i5, int i6) {
            COUICardView.this.mShadowBounds.set(i3, i4, i5, i6);
            COUICardView cOUICardView = COUICardView.this;
            Rect rect = cOUICardView.mContentPadding;
            COUICardView.access$001(cOUICardView, i3 + rect.left, i4 + rect.top, i5 + rect.right, i6 + rect.bottom);
        }
    }

    static {
        CardViewApi21Impl cardViewApi21Impl = new CardViewApi21Impl();
        IMPL = cardViewApi21Impl;
        cardViewApi21Impl.initStatic();
    }

    public COUICardView(Context context) {
        super(context);
        this.mContentPadding = new Rect();
        this.mShadowBounds = new Rect();
        this.mCardViewDelegate = new AnonymousClass1();
        initialize(context, null, 0);
    }

    public static /* synthetic */ void access$001(COUICardView cOUICardView, int i3, int i4, int i5, int i6) {
        super.setPadding(i3, i4, i5, i6);
    }

    public static /* synthetic */ void access$101(COUICardView cOUICardView, int i3) {
        super.setMinimumWidth(i3);
    }

    public static /* synthetic */ void access$201(COUICardView cOUICardView, int i3) {
        super.setMinimumHeight(i3);
    }

    private void initialize(Context context, AttributeSet attributeSet, int i3) {
        ColorStateList colorStateListValueOf;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, com.support.cardview.R.styleable.COUICardView, i3, 0);
        int i4 = com.support.cardview.R.styleable.COUICardView_cardBackgroundColor;
        if (typedArrayObtainStyledAttributes.hasValue(i4)) {
            colorStateListValueOf = typedArrayObtainStyledAttributes.getColorStateList(i4);
        } else {
            TypedArray typedArrayObtainStyledAttributes2 = getContext().obtainStyledAttributes(COLOR_BACKGROUND_ATTR);
            int color = typedArrayObtainStyledAttributes2.getColor(0, 0);
            typedArrayObtainStyledAttributes2.recycle();
            float[] fArr = new float[3];
            Color.colorToHSV(color, fArr);
            colorStateListValueOf = ColorStateList.valueOf(fArr[2] > 0.5f ? getResources().getColor(com.support.cardview.R.color.cardview_light_background) : getResources().getColor(com.support.cardview.R.color.cardview_dark_background));
        }
        ColorStateList colorStateList = colorStateListValueOf;
        float dimension = typedArrayObtainStyledAttributes.getDimension(com.support.cardview.R.styleable.COUICardView_cardCornerRadius, 0.0f);
        float f3 = typedArrayObtainStyledAttributes.getFloat(com.support.cardview.R.styleable.COUICardView_couiCardCornerWeight, 0.0f);
        float dimension2 = typedArrayObtainStyledAttributes.getDimension(com.support.cardview.R.styleable.COUICardView_couiCardRoundCornerRadius, 0.0f);
        float dimension3 = typedArrayObtainStyledAttributes.getDimension(com.support.cardview.R.styleable.COUICardView_cardElevation, 0.0f);
        float dimension4 = typedArrayObtainStyledAttributes.getDimension(com.support.cardview.R.styleable.COUICardView_cardMaxElevation, 0.0f);
        this.mCompatPadding = typedArrayObtainStyledAttributes.getBoolean(com.support.cardview.R.styleable.COUICardView_cardUseCompatPadding, false);
        this.mPreventCornerOverlap = typedArrayObtainStyledAttributes.getBoolean(com.support.cardview.R.styleable.COUICardView_cardPreventCornerOverlap, true);
        int dimensionPixelSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(com.support.cardview.R.styleable.COUICardView_contentPadding, 0);
        this.mContentPadding.left = typedArrayObtainStyledAttributes.getDimensionPixelSize(com.support.cardview.R.styleable.COUICardView_contentPaddingLeft, dimensionPixelSize);
        this.mContentPadding.top = typedArrayObtainStyledAttributes.getDimensionPixelSize(com.support.cardview.R.styleable.COUICardView_contentPaddingTop, dimensionPixelSize);
        this.mContentPadding.right = typedArrayObtainStyledAttributes.getDimensionPixelSize(com.support.cardview.R.styleable.COUICardView_contentPaddingRight, dimensionPixelSize);
        this.mContentPadding.bottom = typedArrayObtainStyledAttributes.getDimensionPixelSize(com.support.cardview.R.styleable.COUICardView_contentPaddingBottom, dimensionPixelSize);
        float f4 = dimension3 > dimension4 ? dimension3 : dimension4;
        this.mUserSetMinWidth = typedArrayObtainStyledAttributes.getDimensionPixelSize(com.support.cardview.R.styleable.COUICardView_android_minWidth, 0);
        this.mUserSetMinHeight = typedArrayObtainStyledAttributes.getDimensionPixelSize(com.support.cardview.R.styleable.COUICardView_android_minHeight, 0);
        typedArrayObtainStyledAttributes.recycle();
        IMPL.initialize(this.mCardViewDelegate, context, colorStateList, dimension, dimension3, f4, f3, dimension2);
    }

    public ColorStateList getCardBackgroundColor() {
        return IMPL.getBackgroundColor(this.mCardViewDelegate);
    }

    public float getCardElevation() {
        return IMPL.getElevation(this.mCardViewDelegate);
    }

    public float getCardRoundCornerRadius() {
        return IMPL.getCardRoundCornerRadius(this.mCardViewDelegate);
    }

    public int getContentPaddingBottom() {
        return this.mContentPadding.bottom;
    }

    public int getContentPaddingLeft() {
        return this.mContentPadding.left;
    }

    public int getContentPaddingRight() {
        return this.mContentPadding.right;
    }

    public int getContentPaddingTop() {
        return this.mContentPadding.top;
    }

    public float getMaxCardElevation() {
        return IMPL.getMaxElevation(this.mCardViewDelegate);
    }

    public boolean getPreventCornerOverlap() {
        return this.mPreventCornerOverlap;
    }

    public float getRadius() {
        return IMPL.getRadius(this.mCardViewDelegate);
    }

    public boolean getUseCompatPadding() {
        return this.mCompatPadding;
    }

    public float getWeight() {
        return IMPL.getWeight(this.mCardViewDelegate);
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        CardViewImpl cardViewImpl = IMPL;
        if (cardViewImpl instanceof CardViewApi21Impl) {
            super.onMeasure(i3, i4);
            return;
        }
        int mode = View.MeasureSpec.getMode(i3);
        if (mode == Integer.MIN_VALUE || mode == 1073741824) {
            i3 = View.MeasureSpec.makeMeasureSpec(Math.max((int) Math.ceil(cardViewImpl.getMinWidth(this.mCardViewDelegate)), View.MeasureSpec.getSize(i3)), mode);
        }
        int mode2 = View.MeasureSpec.getMode(i4);
        if (mode2 == Integer.MIN_VALUE || mode2 == 1073741824) {
            i4 = View.MeasureSpec.makeMeasureSpec(Math.max((int) Math.ceil(cardViewImpl.getMinHeight(this.mCardViewDelegate)), View.MeasureSpec.getSize(i4)), mode2);
        }
        super.onMeasure(i3, i4);
    }

    public void setCardBackgroundColor(int i3) {
        IMPL.setBackgroundColor(this.mCardViewDelegate, ColorStateList.valueOf(i3));
    }

    public void setCardElevation(float f3) {
        IMPL.setElevation(this.mCardViewDelegate, f3);
    }

    public void setCardRoundCornerRadius(float f3) {
        IMPL.setCardRoundCornerRadius(this.mCardViewDelegate, f3);
    }

    public void setContentPadding(int i3, int i4, int i5, int i6) {
        this.mContentPadding.set(i3, i4, i5, i6);
        IMPL.updatePadding(this.mCardViewDelegate);
    }

    public void setMaxCardElevation(float f3) {
        IMPL.setMaxElevation(this.mCardViewDelegate, f3);
    }

    @Override // android.view.View
    public void setMinimumHeight(int i3) {
        this.mUserSetMinHeight = i3;
        super.setMinimumHeight(i3);
    }

    @Override // android.view.View
    public void setMinimumWidth(int i3) {
        this.mUserSetMinWidth = i3;
        super.setMinimumWidth(i3);
    }

    @Override // android.view.View
    public void setPadding(int i3, int i4, int i5, int i6) {
    }

    @Override // android.view.View
    public void setPaddingRelative(int i3, int i4, int i5, int i6) {
    }

    public void setPreventCornerOverlap(boolean z2) {
        if (z2 != this.mPreventCornerOverlap) {
            this.mPreventCornerOverlap = z2;
            IMPL.onPreventCornerOverlapChanged(this.mCardViewDelegate);
        }
    }

    public void setRadius(float f3) {
        IMPL.setRadius(this.mCardViewDelegate, f3);
    }

    public void setUseCompatPadding(boolean z2) {
        if (this.mCompatPadding != z2) {
            this.mCompatPadding = z2;
            IMPL.onCompatPaddingChanged(this.mCardViewDelegate);
        }
    }

    public void setWeight(float f3) {
        IMPL.setWeight(this.mCardViewDelegate, f3);
    }

    public void setCardBackgroundColor(ColorStateList colorStateList) {
        IMPL.setBackgroundColor(this.mCardViewDelegate, colorStateList);
    }

    public COUICardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContentPadding = new Rect();
        this.mShadowBounds = new Rect();
        this.mCardViewDelegate = new AnonymousClass1();
        initialize(context, attributeSet, 0);
    }

    public COUICardView(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.mContentPadding = new Rect();
        this.mShadowBounds = new Rect();
        this.mCardViewDelegate = new AnonymousClass1();
        initialize(context, attributeSet, i3);
    }
}