package com.coui.appcompat.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.coui.appcompat.animation.COUIInEaseInterpolator;
import com.coui.appcompat.animation.COUISpringInterpolator;
import com.coui.appcompat.indicator.COUIPageIndicator;
import com.coui.appcompat.viewpager.COUIViewPager2;
import com.coui.appcompat.viewpager.COUIViewPager2.AnimationConfig.FlingScrollConfig;
import com.coui.appcompat.viewpager.COUIViewPager2.AnimationConfig.ProgramScrollConfig;
import com.support.dialog.R;
import java.util.List;
import java.util.Objects;

/* JADX INFO: loaded from: classes7.dex */
public class COUIGuideLayoutContentView extends LinearLayout {
    private static final int FLING_MAX_VELOCITY = 6000;
    private static final int FLING_SCROLL_MAX_DURATION_MS = 1000;
    private static final int FLING_SCROLL_MIN_DURATION_MS = 500;
    private static final float FLING_VELOCITY_DECAY_FACTOR = 2.0f;
    private static final float FLING_VELOCITY_THRESHOLD = 2500.0f;
    private static final int PROGRAM_SCROLL_DURATION_MS = 650;
    private static final float SPRING_DAMPING_RATIO = 0.6f;
    private COUIGuideDialogButtonLayout mButtonLayout;
    private boolean mButtonTextSetByUser;
    private ImageHeightStyle mImageHeightStyle;
    private int mImagePaddingBottomDp;
    private int mImagePaddingLeftDp;
    private int mImagePaddingRightDp;
    private int mImagePaddingTopDp;
    private final OnButtonClickListener mNextPageClickListener;
    private COUIPageIndicator mPageIndicator;
    private COUIViewPager2 mViewPager;

    /* JADX INFO: renamed from: com.coui.appcompat.dialog.COUIGuideLayoutContentView$1, reason: invalid class name */
    public class AnonymousClass1 implements OnButtonClickListener {
        public AnonymousClass1() {
        }

        @Override // com.coui.appcompat.dialog.COUIGuideLayoutContentView.OnButtonClickListener
        public void onNextClick() {
            COUIGuideLayoutContentView.access$000(COUIGuideLayoutContentView.this).setCurrentItem(COUIGuideLayoutContentView.access$000(COUIGuideLayoutContentView.this).getCurrentItem() + 1, true);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.dialog.COUIGuideLayoutContentView$2, reason: invalid class name */
    public class AnonymousClass2 extends ViewPager2.OnPageChangeCallback {
        public AnonymousClass2() {
        }

        @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
        public void onPageScrollStateChanged(int i3) {
            super.onPageScrollStateChanged(i3);
            COUIGuideLayoutContentView.access$100(COUIGuideLayoutContentView.this).onPageScrollStateChanged(i3);
            COUIGuideLayoutContentView.access$200(COUIGuideLayoutContentView.this).onPageScrollStateChanged(i3);
        }

        @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
        public void onPageScrolled(int i3, float f3, int i4) {
            super.onPageScrolled(i3, f3, i4);
            COUIGuideLayoutContentView.access$100(COUIGuideLayoutContentView.this).onPageScrolled(i3, f3, i4);
            COUIGuideLayoutContentView.access$200(COUIGuideLayoutContentView.this).onPageScrolled(i3, f3, i4);
        }

        @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
        public void onPageSelected(int i3) {
            super.onPageSelected(i3);
            COUIGuideLayoutContentView.access$100(COUIGuideLayoutContentView.this).onPageSelected(i3);
            COUIGuideLayoutContentView.access$200(COUIGuideLayoutContentView.this).onPageSelected(i3);
        }
    }

    public enum ImageHeightStyle {
        DEFAULT(328),
        HEIGHT_240DP(240);

        private final int mHeightDp;

        ImageHeightStyle(int i3) {
            this.mHeightDp = i3;
        }

        public int getHeightDp() {
            return this.mHeightDp;
        }
    }

    public interface OnButtonClickListener {
        default void onNextClick() {
        }

        default void onSkipClick() {
        }

        default void onStartClick() {
        }
    }

    public COUIGuideLayoutContentView(Context context) {
        this(context, null);
    }

    public static /* synthetic */ COUIViewPager2 access$000(COUIGuideLayoutContentView cOUIGuideLayoutContentView) {
        return cOUIGuideLayoutContentView.mViewPager;
    }

    public static /* synthetic */ COUIPageIndicator access$100(COUIGuideLayoutContentView cOUIGuideLayoutContentView) {
        return cOUIGuideLayoutContentView.mPageIndicator;
    }

    public static /* synthetic */ COUIGuideDialogButtonLayout access$200(COUIGuideLayoutContentView cOUIGuideLayoutContentView) {
        return cOUIGuideLayoutContentView.mButtonLayout;
    }

    private int dpToPx(int i3) {
        return Math.round(getContext().getResources().getDisplayMetrics().density * i3);
    }

    private void initAfterAdapterSet() {
        int dimensionPixelSize;
        RecyclerView.Adapter adapter = this.mViewPager.getAdapter();
        int itemCount = adapter != null ? adapter.getItemCount() : 0;
        this.mPageIndicator.setDotsCount(itemCount);
        COUIPageIndicator cOUIPageIndicator = this.mPageIndicator;
        COUIViewPager2 cOUIViewPager2 = this.mViewPager;
        Objects.requireNonNull(cOUIViewPager2);
        cOUIPageIndicator.setOnDotClickListener(new i(cOUIViewPager2));
        this.mButtonLayout.setPagerCount(itemCount);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mViewPager.getLayoutParams();
        if (layoutParams != null) {
            if (itemCount <= 1) {
                this.mViewPager.setUserInputEnabled(false);
                this.mPageIndicator.setVisibility(8);
                dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.coui_dialog_guide_viewpager2_margin_single_bottom);
            } else {
                this.mViewPager.setUserInputEnabled(true);
                this.mPageIndicator.setVisibility(0);
                dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.coui_dialog_guide_viewpager2_margin_bottom);
            }
            layoutParams.setMargins(0, 0, 0, dimensionPixelSize);
            this.mViewPager.setLayoutParams(layoutParams);
        }
        if (this.mButtonTextSetByUser) {
            return;
        }
        setDefaultButtonText(itemCount);
    }

    private void initLayout() {
        setOrientation(1);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.coui_dialog_guide_button_layout_padding_horizontal);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R.dimen.coui_dialog_guide_viewpager2_margin_bottom);
        int dimensionPixelSize3 = getResources().getDimensionPixelSize(R.dimen.coui_dialog_guide_indicator_margin_bottom);
        int dimensionPixelSize4 = getResources().getDimensionPixelSize(R.dimen.coui_dialog_guide_button_layout_margin_bottom);
        COUIViewPager2 cOUIViewPager2 = new COUIViewPager2(getContext());
        this.mViewPager = cOUIViewPager2;
        cOUIViewPager2.setClickable(false);
        this.mViewPager.setAnimationConfig(createGuideDialogConfig());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, 0, 1.0f);
        layoutParams.setMargins(0, 0, 0, dimensionPixelSize2);
        addView(this.mViewPager, layoutParams);
        COUIPageIndicator cOUIPageIndicator = new COUIPageIndicator(getContext());
        this.mPageIndicator = cOUIPageIndicator;
        cOUIPageIndicator.setIsClickable(false);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams2.setMargins(0, 0, 0, dimensionPixelSize3);
        layoutParams2.gravity = 1;
        addView(this.mPageIndicator, layoutParams2);
        COUIGuideDialogButtonLayout cOUIGuideDialogButtonLayout = new COUIGuideDialogButtonLayout(getContext());
        this.mButtonLayout = cOUIGuideDialogButtonLayout;
        cOUIGuideDialogButtonLayout.setClipToPadding(false);
        this.mButtonLayout.setPadding(dimensionPixelSize, 0, dimensionPixelSize, 0);
        this.mButtonLayout.setNextButtonClickListener(this.mNextPageClickListener);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams3.setMargins(0, 0, 0, dimensionPixelSize4);
        addView(this.mButtonLayout, layoutParams3);
        setClipChildren(false);
    }

    private void initViewPager() {
        this.mViewPager.setClickable(false);
        this.mViewPager.registerOnPageChangeCallback(new AnonymousClass2());
    }

    private void setDefaultButtonText(int i3) {
        String string = getResources().getString(R.string.coui_guide_dialog_skip_text);
        String string2 = getResources().getString(R.string.coui_guide_dialog_next_text);
        String string3 = getResources().getString(R.string.coui_guide_dialog_known_text);
        if (i3 <= 1) {
            this.mButtonLayout.getNextButton().setStartText(string3);
            this.mButtonLayout.getNextButton().setText(string3);
            this.mButtonLayout.setButtonLayoutType(1);
        } else {
            this.mButtonLayout.getSkipButton().setText(string);
            this.mButtonLayout.getNextButton().setNextText(string2);
            this.mButtonLayout.getNextButton().setStartText(string3);
            this.mButtonLayout.getNextButton().setText(string2);
            this.mButtonLayout.setButtonLayoutType(0);
        }
    }

    public void addButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.mButtonLayout.addButtonClickListener(onButtonClickListener);
    }

    public COUIViewPager2.AnimationConfig createGuideDialogConfig() {
        new COUIInEaseInterpolator();
        COUISpringInterpolator cOUISpringInterpolator = new COUISpringInterpolator(0.6000000238418579d, 0.0d);
        COUIViewPager2.AnimationConfig animationConfig = new COUIViewPager2.AnimationConfig();
        COUIViewPager2.AnimationConfig.ProgramScrollConfig programScrollConfig = animationConfig.new ProgramScrollConfig(650, cOUISpringInterpolator);
        return animationConfig.setProgramScrollConfig(programScrollConfig).setFlingScrollConfig(animationConfig.new FlingScrollConfig(500, 1000, cOUISpringInterpolator, FLING_VELOCITY_THRESHOLD, 6000, 2.0f));
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeButtonClickListener(this.mNextPageClickListener);
    }

    public void removeButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.mButtonLayout.removeButtonClickListener(onButtonClickListener);
    }

    public void setBlurState(boolean z2) {
        this.mButtonLayout.setBlurState(z2);
    }

    public void setButtonText(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3) {
        this.mButtonTextSetByUser = true;
        this.mButtonLayout.getSkipButton().setText(charSequence);
        this.mButtonLayout.getNextButton().setNextText(charSequence2);
        this.mButtonLayout.getNextButton().setStartText(charSequence3);
        this.mButtonLayout.getNextButton().setText(charSequence2);
        this.mButtonLayout.setButtonLayoutType(0);
    }

    public void setGuidePages(List<COUIGuidePageItem> list) {
        COUIGuidePageAdapter cOUIGuidePageAdapter = new COUIGuidePageAdapter(getContext(), list);
        cOUIGuidePageAdapter.setImagePadding(this.mImagePaddingLeftDp, this.mImagePaddingTopDp, this.mImagePaddingRightDp, this.mImagePaddingBottomDp);
        cOUIGuidePageAdapter.setImageHeightDp(this.mImageHeightStyle.getHeightDp());
        setViewPagerAdapter(cOUIGuidePageAdapter);
    }

    public void setImageHeightStyle(ImageHeightStyle imageHeightStyle) {
        this.mImageHeightStyle = imageHeightStyle;
        COUIViewPager2 cOUIViewPager2 = this.mViewPager;
        RecyclerView.Adapter adapter = cOUIViewPager2 != null ? cOUIViewPager2.getAdapter() : null;
        if (adapter instanceof COUIGuidePageAdapter) {
            ((COUIGuidePageAdapter) adapter).setImageHeightDp(imageHeightStyle.getHeightDp());
        }
    }

    public void setImagePadding(int i3, int i4, int i5, int i6) {
        this.mImagePaddingLeftDp = dpToPx(i3);
        this.mImagePaddingTopDp = dpToPx(i4);
        this.mImagePaddingRightDp = dpToPx(i5);
        this.mImagePaddingBottomDp = dpToPx(i6);
        COUIViewPager2 cOUIViewPager2 = this.mViewPager;
        RecyclerView.Adapter adapter = cOUIViewPager2 != null ? cOUIViewPager2.getAdapter() : null;
        if (adapter instanceof COUIGuidePageAdapter) {
            ((COUIGuidePageAdapter) adapter).setImagePadding(i3, i4, i5, i6);
        }
    }

    public void setViewPagerAdapter(RecyclerView.Adapter<?> adapter) {
        this.mViewPager.setAdapter(adapter);
        if (adapter instanceof COUIGuidePageAdapter) {
            ((COUIGuidePageAdapter) adapter).setImageHeightDp(this.mImageHeightStyle.getHeightDp());
        }
        initAfterAdapterSet();
    }

    public COUIGuideLayoutContentView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIGuideLayoutContentView(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.mNextPageClickListener = new AnonymousClass1();
        this.mImageHeightStyle = ImageHeightStyle.DEFAULT;
        this.mImagePaddingLeftDp = 0;
        this.mImagePaddingTopDp = 0;
        this.mImagePaddingRightDp = 0;
        this.mImagePaddingBottomDp = 0;
        this.mButtonTextSetByUser = false;
        initLayout();
        initViewPager();
    }

    public void setButtonText(CharSequence charSequence) {
        this.mButtonTextSetByUser = true;
        this.mButtonLayout.getNextButton().setStartText(charSequence);
        this.mButtonLayout.getNextButton().setText(charSequence);
        this.mButtonLayout.setButtonLayoutType(1);
    }
}