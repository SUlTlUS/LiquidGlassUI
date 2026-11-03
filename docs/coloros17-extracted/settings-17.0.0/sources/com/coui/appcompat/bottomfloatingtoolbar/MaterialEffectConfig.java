package com.coui.appcompat.bottomfloatingtoolbar;

/* JADX INFO: loaded from: classes7.dex */
class MaterialEffectConfig {

    public static class BlurConfig {
        public final float mBlurRadius;

        public static class Builder {
            private float mBlurRadius = 30.0f;

            public static /* synthetic */ float access$3300(Builder builder) {
                return builder.mBlurRadius;
            }

            public BlurConfig build() {
                return new BlurConfig(this, null);
            }

            public Builder setBlurRadius(float f3) {
                this.mBlurRadius = f3;
                return this;
            }
        }

        public /* synthetic */ BlurConfig(Builder builder, AnonymousClass1 anonymousClass1) {
            this(builder);
        }

        private BlurConfig(Builder builder) {
            this.mBlurRadius = Builder.access$3300(builder);
        }
    }

    public static class InnerShadowConfig {
        public final float mShadow0Alpha;
        public final int mShadow0Color;
        public final float mShadow0Dx;
        public final float mShadow0Dy;
        public final float mShadow0Radius;
        public final float mShadow0Width;
        public final float mShadow1Alpha;
        public final int mShadow1Color;
        public final float mShadow1Dx;
        public final float mShadow1Dy;
        public final float mShadow1Radius;
        public final float mShadow1Width;
        public final float mShadowLayerStretchDx;
        public final float mShadowLayerStretchDy;

        public static class Builder {
            private float mShadow0Alpha = 0.5f;
            private float mShadow0Width = 54.0f;
            private float mShadow0Radius = 93.0f;
            private float mShadow0Dx = 0.0f;
            private float mShadow0Dy = -22.0f;
            private int mShadow0Color = -1;
            private float mShadow1Alpha = 0.8f;
            private float mShadow1Width = 2.0f;
            private float mShadow1Radius = 1.0f;
            private float mShadow1Dx = 0.0f;
            private float mShadow1Dy = 0.0f;
            private int mShadow1Color = -1;
            private float mShadowLayerStretchDx = 12.0f;
            private float mShadowLayerStretchDy = 12.0f;

            public static /* synthetic */ float access$1800(Builder builder) {
                return builder.mShadow0Alpha;
            }

            public static /* synthetic */ float access$1900(Builder builder) {
                return builder.mShadow0Width;
            }

            public static /* synthetic */ float access$2000(Builder builder) {
                return builder.mShadow0Radius;
            }

            public static /* synthetic */ float access$2100(Builder builder) {
                return builder.mShadow0Dx;
            }

            public static /* synthetic */ float access$2200(Builder builder) {
                return builder.mShadow0Dy;
            }

            public static /* synthetic */ int access$2300(Builder builder) {
                return builder.mShadow0Color;
            }

            public static /* synthetic */ float access$2400(Builder builder) {
                return builder.mShadow1Alpha;
            }

            public static /* synthetic */ float access$2500(Builder builder) {
                return builder.mShadow1Width;
            }

            public static /* synthetic */ float access$2600(Builder builder) {
                return builder.mShadow1Radius;
            }

            public static /* synthetic */ float access$2700(Builder builder) {
                return builder.mShadow1Dx;
            }

            public static /* synthetic */ float access$2800(Builder builder) {
                return builder.mShadow1Dy;
            }

            public static /* synthetic */ int access$2900(Builder builder) {
                return builder.mShadow1Color;
            }

            public static /* synthetic */ float access$3000(Builder builder) {
                return builder.mShadowLayerStretchDx;
            }

            public static /* synthetic */ float access$3100(Builder builder) {
                return builder.mShadowLayerStretchDy;
            }

            public InnerShadowConfig build() {
                return new InnerShadowConfig(this, null);
            }

            public Builder setShadow0Alpha(float f3) {
                this.mShadow0Alpha = f3;
                return this;
            }

            public Builder setShadow0Color(int i3) {
                this.mShadow0Color = i3;
                return this;
            }

            public Builder setShadow0Dx(float f3) {
                this.mShadow0Dx = f3;
                return this;
            }

            public Builder setShadow0Dy(float f3) {
                this.mShadow0Dy = f3;
                return this;
            }

            public Builder setShadow0Radius(float f3) {
                this.mShadow0Radius = f3;
                return this;
            }

            public Builder setShadow0Width(float f3) {
                this.mShadow0Width = f3;
                return this;
            }

            public Builder setShadow1Alpha(float f3) {
                this.mShadow1Alpha = f3;
                return this;
            }

            public Builder setShadow1Color(int i3) {
                this.mShadow1Color = i3;
                return this;
            }

            public Builder setShadow1Dx(float f3) {
                this.mShadow1Dx = f3;
                return this;
            }

            public Builder setShadow1Dy(float f3) {
                this.mShadow1Dy = f3;
                return this;
            }

            public Builder setShadow1Radius(float f3) {
                this.mShadow1Radius = f3;
                return this;
            }

            public Builder setShadow1Width(float f3) {
                this.mShadow1Width = f3;
                return this;
            }

            public Builder setShadowLayerStretchDx(float f3) {
                this.mShadowLayerStretchDx = f3;
                return this;
            }

            public Builder setShadowLayerStretchDy(float f3) {
                this.mShadowLayerStretchDy = f3;
                return this;
            }
        }

        public /* synthetic */ InnerShadowConfig(Builder builder, AnonymousClass1 anonymousClass1) {
            this(builder);
        }

        private InnerShadowConfig(Builder builder) {
            this.mShadow0Alpha = Builder.access$1800(builder);
            this.mShadow0Width = Builder.access$1900(builder);
            this.mShadow0Radius = Builder.access$2000(builder);
            this.mShadow0Dx = Builder.access$2100(builder);
            this.mShadow0Dy = Builder.access$2200(builder);
            this.mShadow0Color = Builder.access$2300(builder);
            this.mShadow1Alpha = Builder.access$2400(builder);
            this.mShadow1Width = Builder.access$2500(builder);
            this.mShadow1Radius = Builder.access$2600(builder);
            this.mShadow1Dx = Builder.access$2700(builder);
            this.mShadow1Dy = Builder.access$2800(builder);
            this.mShadow1Color = Builder.access$2900(builder);
            this.mShadowLayerStretchDx = Builder.access$3000(builder);
            this.mShadowLayerStretchDy = Builder.access$3100(builder);
        }
    }

    public static class StrokeConfig {
        public final int mColor;
        public final float mCorner;
        public final float mFarLineAlpha;
        public final float mFarLineFadeToSides1;
        public final float mFarLineFadeToSides2;
        public final float mFarLineFadeTowardCenter1;
        public final float mFarLineFadeTowardCenter2;
        public final float mFarLineWidth;
        public final float mNearLineAlpha;
        public final float mNearLineFadeToSides1;
        public final float mNearLineFadeToSides2;
        public final float mNearLineFadeTowardCenter1;
        public final float mNearLineFadeTowardCenter2;
        public final float mNearLineWidth;
        public final float mRatio;
        public final float mWeight;
        public final float mWidth;

        public static class Builder {
            private int mColor = -1;
            private float mWidth = 12.0f;
            private float mRatio = 0.68f;
            private float mNearLineWidth = 3.0f;
            private float mNearLineAlpha = 0.8f;
            private float mNearLineFadeToSides1 = 0.38f;
            private float mNearLineFadeToSides2 = 0.12f;
            private float mNearLineFadeTowardCenter1 = 0.3f;
            private float mNearLineFadeTowardCenter2 = 0.02f;
            private float mFarLineWidth = 3.0f;
            private float mFarLineAlpha = 0.4f;
            private float mFarLineFadeToSides1 = 0.35f;
            private float mFarLineFadeToSides2 = 0.3f;
            private float mFarLineFadeTowardCenter1 = 0.08f;
            private float mFarLineFadeTowardCenter2 = 0.12f;
            private float mCorner = Float.MAX_VALUE;
            private float mWeight = 1.3f;

            public static /* synthetic */ int access$000(Builder builder) {
                return builder.mColor;
            }

            public static /* synthetic */ float access$100(Builder builder) {
                return builder.mWidth;
            }

            public static /* synthetic */ float access$1000(Builder builder) {
                return builder.mFarLineAlpha;
            }

            public static /* synthetic */ float access$1100(Builder builder) {
                return builder.mFarLineFadeToSides1;
            }

            public static /* synthetic */ float access$1200(Builder builder) {
                return builder.mFarLineFadeToSides2;
            }

            public static /* synthetic */ float access$1300(Builder builder) {
                return builder.mFarLineFadeTowardCenter1;
            }

            public static /* synthetic */ float access$1400(Builder builder) {
                return builder.mFarLineFadeTowardCenter2;
            }

            public static /* synthetic */ float access$1500(Builder builder) {
                return builder.mCorner;
            }

            public static /* synthetic */ float access$1600(Builder builder) {
                return builder.mWeight;
            }

            public static /* synthetic */ float access$200(Builder builder) {
                return builder.mRatio;
            }

            public static /* synthetic */ float access$300(Builder builder) {
                return builder.mNearLineWidth;
            }

            public static /* synthetic */ float access$400(Builder builder) {
                return builder.mNearLineAlpha;
            }

            public static /* synthetic */ float access$500(Builder builder) {
                return builder.mNearLineFadeToSides1;
            }

            public static /* synthetic */ float access$600(Builder builder) {
                return builder.mNearLineFadeToSides2;
            }

            public static /* synthetic */ float access$700(Builder builder) {
                return builder.mNearLineFadeTowardCenter1;
            }

            public static /* synthetic */ float access$800(Builder builder) {
                return builder.mNearLineFadeTowardCenter2;
            }

            public static /* synthetic */ float access$900(Builder builder) {
                return builder.mFarLineWidth;
            }

            public StrokeConfig build() {
                return new StrokeConfig(this, null);
            }

            public Builder setColor(int i3) {
                this.mColor = i3;
                return this;
            }

            public Builder setCorner(float f3) {
                this.mCorner = f3;
                return this;
            }

            public Builder setFarLineAlpha(float f3) {
                this.mFarLineAlpha = f3;
                return this;
            }

            public Builder setFarLineFadeToSides1(float f3) {
                this.mFarLineFadeToSides1 = f3;
                return this;
            }

            public Builder setFarLineFadeToSides2(float f3) {
                this.mFarLineFadeToSides2 = f3;
                return this;
            }

            public Builder setFarLineFadeTowardCenter1(float f3) {
                this.mFarLineFadeTowardCenter1 = f3;
                return this;
            }

            public Builder setFarLineFadeTowardCenter2(float f3) {
                this.mFarLineFadeTowardCenter2 = f3;
                return this;
            }

            public Builder setFarLineWidth(float f3) {
                this.mFarLineWidth = f3;
                return this;
            }

            public Builder setNearLineAlpha(float f3) {
                this.mNearLineAlpha = f3;
                return this;
            }

            public Builder setNearLineFadeToSides1(float f3) {
                this.mNearLineFadeToSides1 = f3;
                return this;
            }

            public Builder setNearLineFadeToSides2(float f3) {
                this.mNearLineFadeToSides2 = f3;
                return this;
            }

            public Builder setNearLineFadeTowardCenter1(float f3) {
                this.mNearLineFadeTowardCenter1 = f3;
                return this;
            }

            public Builder setNearLineFadeTowardCenter2(float f3) {
                this.mNearLineFadeTowardCenter2 = f3;
                return this;
            }

            public Builder setNearLineWidth(float f3) {
                this.mNearLineWidth = f3;
                return this;
            }

            public Builder setRatio(float f3) {
                this.mRatio = f3;
                return this;
            }

            public Builder setWeight(float f3) {
                this.mWeight = f3;
                return this;
            }

            public Builder setWidth(float f3) {
                this.mWidth = f3;
                return this;
            }
        }

        public /* synthetic */ StrokeConfig(Builder builder, AnonymousClass1 anonymousClass1) {
            this(builder);
        }

        private StrokeConfig(Builder builder) {
            this.mColor = Builder.access$000(builder);
            this.mWidth = Builder.access$100(builder);
            this.mRatio = Builder.access$200(builder);
            this.mNearLineWidth = Builder.access$300(builder);
            this.mNearLineAlpha = Builder.access$400(builder);
            this.mNearLineFadeToSides1 = Builder.access$500(builder);
            this.mNearLineFadeToSides2 = Builder.access$600(builder);
            this.mNearLineFadeTowardCenter1 = Builder.access$700(builder);
            this.mNearLineFadeTowardCenter2 = Builder.access$800(builder);
            this.mFarLineWidth = Builder.access$900(builder);
            this.mFarLineAlpha = Builder.access$1000(builder);
            this.mFarLineFadeToSides1 = Builder.access$1100(builder);
            this.mFarLineFadeToSides2 = Builder.access$1200(builder);
            this.mFarLineFadeTowardCenter1 = Builder.access$1300(builder);
            this.mFarLineFadeTowardCenter2 = Builder.access$1400(builder);
            this.mCorner = Builder.access$1500(builder);
            this.mWeight = Builder.access$1600(builder);
        }
    }
}