package com.coui.appcompat.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.oplus.anim.EffectiveAnimationView;
import com.support.progressbar.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUILottieLoadingView extends FrameLayout {
    private final EffectiveAnimationView mLoadingView;

    public COUILottieLoadingView(Context context) {
        this(context, null);
    }

    private void pauseAnimation() {
        EffectiveAnimationView effectiveAnimationView = this.mLoadingView;
        if (effectiveAnimationView == null || !effectiveAnimationView.isAnimating()) {
            return;
        }
        this.mLoadingView.pauseAnimation();
    }

    private void resumeAnimation() {
        if (this.mLoadingView != null && getVisibility() == 0 && getWindowVisibility() == 0) {
            this.mLoadingView.resumeAnimation();
        }
    }

    public EffectiveAnimationView getLoadingView() {
        return this.mLoadingView;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        resumeAnimation();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        pauseAnimation();
    }

    @Override // android.view.View
    public void onVisibilityChanged(View view, int i3) {
        super.onVisibilityChanged(view, i3);
        if (getVisibility() == 0) {
            resumeAnimation();
        } else {
            pauseAnimation();
        }
    }

    @Override // android.view.View
    public void onWindowVisibilityChanged(int i3) {
        super.onWindowVisibilityChanged(i3);
        if (i3 == 0) {
            resumeAnimation();
        } else {
            pauseAnimation();
        }
    }

    public COUILottieLoadingView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiLottieLoadingViewStyle);
    }

    public COUILottieLoadingView(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        COUIDarkModeUtil.setForceDarkAllow(this, false);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUILottieLoadingView, i3, 0);
        int dimensionPixelSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUILottieLoadingView_couiLottieLoadingViewWidth, getResources().getDimensionPixelOffset(R.dimen.coui_lottie_loading_view_large_width));
        int dimensionPixelSize2 = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUILottieLoadingView_couiLottieLoadingViewHeight, getResources().getDimensionPixelOffset(R.dimen.coui_lottie_loading_view_large_height));
        String string = typedArrayObtainStyledAttributes.getString(R.styleable.COUILottieLoadingView_couiLottieLoadingJsonName);
        string = string == null ? getResources().getString(R.string.coui_lottie_loading_large_json) : string;
        typedArrayObtainStyledAttributes.recycle();
        EffectiveAnimationView effectiveAnimationView = new EffectiveAnimationView(context);
        this.mLoadingView = effectiveAnimationView;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize2);
        layoutParams.gravity = 17;
        effectiveAnimationView.setLayoutParams(layoutParams);
        effectiveAnimationView.setRepeatCount(-1);
        effectiveAnimationView.setAnimation(string);
        addView(effectiveAnimationView);
    }
}