package com.coui.appcompat.tips.def;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.coui.appcompat.animation.COUIEaseInterpolator;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.coui.appcompat.tips.COUICustomTopTips;
import com.coui.appcompat.version.COUIVersionUtil;
import com.oplus.graphics.OplusOutline;
import com.oplus.graphics.OplusOutlineAdapter;
import com.support.appcompat.R;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes7.dex */
public class COUIDefaultTopTips extends COUICustomTopTips implements IDefaultTopTips {
    private static final int CLIP_ANIMATION_DURATION = 250;
    private static final String ICON_TRANSLATION_Y = "translationY";
    private static final float WEIGHT_MULTIPLE_LINE = 3.0f;
    private static final float WEIGHT_MULTIPLE_LINE_OS15 = 1.1f;
    private static final float WEIGHT_SINGLE_LINE = 1.0f;
    private static final int WIDTH_CHANGE_ANIMATION_DURATION = 300;
    private View mBackgroundOverlay;
    private Drawable mBackgroundOverlayDrawable;
    private int mCachedSmoothStyleType;
    private ValueAnimator mClipAnimator;
    private int mClipHeight;
    private final Rect mClipRect;
    private IDefaultTopTips mDefaultTopTips;
    private boolean mIsClipHeightAnimating;
    private boolean mIsWidthAnimating;
    private BackgroundStyle mLastBackgroundStyle;
    private boolean mSmoothStyleTypeCached;
    private boolean mSuppressRequestLayout;
    private TopTipsStyle mTopTipsStyle;
    private AnimatorSet mWidthChangeAnimatorSet;

    /* JADX INFO: renamed from: com.coui.appcompat.tips.def.COUIDefaultTopTips$1, reason: invalid class name */
    public class AnonymousClass1 extends ViewOutlineProvider {
        public AnonymousClass1() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            int width = view.getWidth();
            int height = view.getHeight();
            if (width <= 0 || height <= 0) {
                outline.setEmpty();
                return;
            }
            int iAccess$000 = COUIDefaultTopTips.access$000(COUIDefaultTopTips.this, height);
            if (iAccess$000 <= 0) {
                outline.setEmpty();
            } else {
                COUIDefaultTopTips.access$100(COUIDefaultTopTips.this, outline, width, iAccess$000);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.tips.def.COUIDefaultTopTips$2, reason: invalid class name */
    public class AnonymousClass2 extends AnimatorListenerAdapter {
        final /* synthetic */ int val$endValue;
        final /* synthetic */ boolean val$isExpanding;
        final /* synthetic */ Runnable val$onEndCallback;

        public AnonymousClass2(int i3, boolean z2, Runnable runnable) {
            this.val$endValue = i3;
            this.val$isExpanding = z2;
            this.val$onEndCallback = runnable;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            COUIDefaultTopTips.access$202(COUIDefaultTopTips.this, false);
            COUIDefaultTopTips.this.invalidateOutline();
            if (COUIDefaultTopTips.access$400(COUIDefaultTopTips.this) instanceof COUIDefaultTopTipsView) {
                ((COUIDefaultTopTipsView) COUIDefaultTopTips.access$400(COUIDefaultTopTips.this)).setChildViewsAlpha(1.0f);
            }
            Runnable runnable = this.val$onEndCallback;
            if (runnable != null) {
                runnable.run();
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUIDefaultTopTips.access$202(COUIDefaultTopTips.this, false);
            if (this.val$endValue > 0) {
                COUIDefaultTopTips cOUIDefaultTopTips = COUIDefaultTopTips.this;
                COUIDefaultTopTips.access$302(cOUIDefaultTopTips, cOUIDefaultTopTips.getHeight());
            }
            COUIDefaultTopTips.this.invalidateOutline();
            if (COUIDefaultTopTips.access$400(COUIDefaultTopTips.this) instanceof COUIDefaultTopTipsView) {
                ((COUIDefaultTopTipsView) COUIDefaultTopTips.access$400(COUIDefaultTopTips.this)).setChildViewsAlpha(this.val$isExpanding ? 1.0f : 0.0f);
            }
            Runnable runnable = this.val$onEndCallback;
            if (runnable != null) {
                runnable.run();
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUIDefaultTopTips.access$202(COUIDefaultTopTips.this, true);
            COUIDefaultTopTips.this.setVisibility(0);
            COUIDefaultTopTips.this.invalidateOutline();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.tips.def.COUIDefaultTopTips$3, reason: invalid class name */
    public class AnonymousClass3 extends AnimatorListenerAdapter {
        final /* synthetic */ int val$endWidth;
        final /* synthetic */ COUIDefaultTopTipsView val$newContentView;
        final /* synthetic */ ImageView val$newImage;
        final /* synthetic */ COUIDefaultTopTipsView val$oldContentView;
        final /* synthetic */ ImageView val$oldImage;

        public AnonymousClass3(COUIDefaultTopTipsView cOUIDefaultTopTipsView, ImageView imageView, COUIDefaultTopTipsView cOUIDefaultTopTipsView2, int i3, ImageView imageView2) {
            this.val$oldContentView = cOUIDefaultTopTipsView;
            this.val$newImage = imageView;
            this.val$newContentView = cOUIDefaultTopTipsView2;
            this.val$endWidth = i3;
            this.val$oldImage = imageView2;
        }

        public static /* synthetic */ void a(AnonymousClass3 anonymousClass3, COUIDefaultTopTipsView cOUIDefaultTopTipsView, int i3) {
            COUIDefaultTopTips.access$700(COUIDefaultTopTips.this, cOUIDefaultTopTipsView.hasTitle(), cOUIDefaultTopTipsView.isSingleLineText());
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            COUIDefaultTopTips.access$602(COUIDefaultTopTips.this, false);
            COUIDefaultTopTips.access$502(COUIDefaultTopTips.this, false);
            COUIDefaultTopTips.this.removeView(this.val$newContentView);
            this.val$oldContentView.removeSpaceAndRestoreLayout();
            this.val$oldContentView.setChildViewsAlpha(1.0f);
            ImageView imageView = this.val$oldImage;
            if (imageView != null) {
                imageView.setAlpha(1.0f);
            }
            ImageView imageView2 = this.val$newImage;
            if (imageView2 != null) {
                imageView2.setTranslationY(0.0f);
            }
            COUIDefaultTopTips.this.invalidateOutline();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.val$oldContentView.removeSpaceAndRestoreLayout();
            COUIDefaultTopTips.this.removeView(this.val$oldContentView);
            ImageView imageView = this.val$newImage;
            if (imageView != null) {
                imageView.setTranslationY(0.0f);
            }
            COUIDefaultTopTips.access$402(COUIDefaultTopTips.this, this.val$newContentView);
            COUIDefaultTopTipsView cOUIDefaultTopTipsView = this.val$newContentView;
            cOUIDefaultTopTipsView.setOnLinesChangedListener(new h(this, cOUIDefaultTopTipsView));
            COUIDefaultTopTips.access$602(COUIDefaultTopTips.this, false);
            ViewGroup.LayoutParams layoutParams = COUIDefaultTopTips.this.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.width = this.val$endWidth;
                layoutParams.height = -2;
                COUIDefaultTopTips.this.setLayoutParams(layoutParams);
            }
            ViewGroup.LayoutParams layoutParams2 = this.val$newContentView.getLayoutParams();
            if (layoutParams2 != null) {
                layoutParams2.width = -1;
                layoutParams2.height = -2;
                this.val$newContentView.setLayoutParams(layoutParams2);
            }
            COUIDefaultTopTips.access$502(COUIDefaultTopTips.this, false);
            this.val$newContentView.setChildViewsAlpha(1.0f);
            COUIDefaultTopTips.this.invalidateOutline();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUIDefaultTopTips.access$502(COUIDefaultTopTips.this, true);
            COUIDefaultTopTips.access$602(COUIDefaultTopTips.this, true);
        }
    }

    public static final class BackgroundOverlayView extends View {
        public BackgroundOverlayView(Context context) {
            super(context);
        }

        @Override // android.view.View
        public void onMeasure(int i3, int i4) {
            setMeasuredDimension(0, 0);
        }
    }

    public enum BackgroundStyle {
        WHITE,
        GRAY
    }

    public enum TopTipsStyle {
        STANDARD,
        AI_PAYMENT
    }

    public COUIDefaultTopTips(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void a(COUIDefaultTopTips cOUIDefaultTopTips, boolean z2, ValueAnimator valueAnimator) {
        cOUIDefaultTopTips.getClass();
        cOUIDefaultTopTips.mClipHeight = ((Integer) valueAnimator.getAnimatedValue()).intValue();
        cOUIDefaultTopTips.invalidateOutline();
        if (cOUIDefaultTopTips.mDefaultTopTips instanceof COUIDefaultTopTipsView) {
            ((COUIDefaultTopTipsView) cOUIDefaultTopTips.mDefaultTopTips).setChildViewsAlpha(z2 ? valueAnimator.getAnimatedFraction() : 1.0f - valueAnimator.getAnimatedFraction());
        }
    }

    public static /* synthetic */ int access$000(COUIDefaultTopTips cOUIDefaultTopTips, int i3) {
        return cOUIDefaultTopTips.calculateClipHeight(i3);
    }

    public static /* synthetic */ void access$100(COUIDefaultTopTips cOUIDefaultTopTips, Outline outline, int i3, int i4) {
        cOUIDefaultTopTips.applyRoundCornerToOutline(outline, i3, i4);
    }

    public static /* synthetic */ boolean access$202(COUIDefaultTopTips cOUIDefaultTopTips, boolean z2) {
        cOUIDefaultTopTips.mIsClipHeightAnimating = z2;
        return z2;
    }

    public static /* synthetic */ int access$302(COUIDefaultTopTips cOUIDefaultTopTips, int i3) {
        cOUIDefaultTopTips.mClipHeight = i3;
        return i3;
    }

    public static /* synthetic */ IDefaultTopTips access$400(COUIDefaultTopTips cOUIDefaultTopTips) {
        return cOUIDefaultTopTips.mDefaultTopTips;
    }

    public static /* synthetic */ IDefaultTopTips access$402(COUIDefaultTopTips cOUIDefaultTopTips, IDefaultTopTips iDefaultTopTips) {
        cOUIDefaultTopTips.mDefaultTopTips = iDefaultTopTips;
        return iDefaultTopTips;
    }

    public static /* synthetic */ boolean access$502(COUIDefaultTopTips cOUIDefaultTopTips, boolean z2) {
        cOUIDefaultTopTips.mIsWidthAnimating = z2;
        return z2;
    }

    public static /* synthetic */ boolean access$602(COUIDefaultTopTips cOUIDefaultTopTips, boolean z2) {
        cOUIDefaultTopTips.mSuppressRequestLayout = z2;
        return z2;
    }

    public static /* synthetic */ void access$700(COUIDefaultTopTips cOUIDefaultTopTips, boolean z2, boolean z3) {
        cOUIDefaultTopTips.updateRadius(z2, z3);
    }

    private void applyBackgroundOverlayState() {
        ensureBackgroundOverlay();
        Drawable drawable = this.mBackgroundOverlayDrawable;
        if (drawable != null) {
            this.mBackgroundOverlay.setBackground(drawable);
            this.mBackgroundOverlay.setVisibility(0);
            setCardBackgroundColor(ColorStateList.valueOf(0));
        } else {
            View view = this.mBackgroundOverlay;
            if (view != null) {
                view.setBackground(null);
                this.mBackgroundOverlay.setVisibility(8);
                applySolidBackgroundColor(this.mLastBackgroundStyle);
            }
        }
        invalidateOutline();
    }

    private void applyRoundCornerToOutline(Outline outline, int i3, int i4) {
        float radius = getRadius();
        float weight = getWeight();
        int smoothStyleType = this.mSmoothStyleTypeCached ? this.mCachedSmoothStyleType : RoundCornerUtil.getSmoothStyleType();
        if (!isSupportSmoothRoundCorner()) {
            outline.setRoundRect(0, 0, i3, i4, radius);
            return;
        }
        if (smoothStyleType != 1) {
            if (smoothStyleType != 0 || weight <= 0.0f) {
                return;
            }
            this.mClipRect.set(0, 0, i3, i4);
            new OplusOutline(outline).setSmoothRoundRect(this.mClipRect, radius, weight);
            return;
        }
        this.mClipRect.set(0, 0, i3, i4);
        OplusOutlineAdapter oplusOutlineAdapter = new OplusOutlineAdapter(outline, 1);
        if (COUIVersionUtil.getOSVersionCode() < 37 || weight <= 0.0f) {
            oplusOutlineAdapter.setSmoothRoundRect(this.mClipRect, radius);
        } else {
            oplusOutlineAdapter.setSmoothRoundRect(this.mClipRect, radius, weight);
        }
    }

    private void applySolidBackgroundColor(BackgroundStyle backgroundStyle) {
        if (backgroundStyle == null) {
            backgroundStyle = BackgroundStyle.GRAY;
        }
        int iOrdinal = backgroundStyle.ordinal();
        int i3 = (iOrdinal == 0 || iOrdinal != 1) ? R.attr.couiColorCard : R.attr.couiColorContainer4;
        setCardBackgroundColor(ColorStateList.valueOf(COUIContextUtil.getAttrColor(getContext(), i3)));
    }

    private void applyTopTipsStyle() {
        IDefaultTopTips iDefaultTopTips = this.mDefaultTopTips;
        if (iDefaultTopTips instanceof COUIDefaultTopTipsView) {
            ((COUIDefaultTopTipsView) iDefaultTopTips).applyTopTipsStyle(this.mTopTipsStyle);
        }
    }

    public static /* synthetic */ void b(COUIDefaultTopTips cOUIDefaultTopTips, float f3, float f4, float f5, float f6, ValueAnimator valueAnimator) {
        cOUIDefaultTopTips.getClass();
        float fFloatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        cOUIDefaultTopTips.setRadius(f3 + ((f4 - f3) * fFloatValue));
        cOUIDefaultTopTips.setWeight(f5 + ((f6 - f5) * fFloatValue));
    }

    public static /* synthetic */ void c(View[] viewArr, View[] viewArr2, ValueAnimator valueAnimator) {
        float fFloatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        for (View view : viewArr) {
            if (view != null) {
                view.setAlpha(1.0f - fFloatValue);
            }
        }
        for (View view2 : viewArr2) {
            if (view2 != null) {
                view2.setAlpha(fFloatValue);
            }
        }
    }

    private void cacheSmoothStyleType() {
        if (isSupportSmoothRoundCorner()) {
            this.mCachedSmoothStyleType = RoundCornerUtil.getSmoothStyleType();
            this.mSmoothStyleTypeCached = true;
        }
    }

    private int calculateClipHeight(int i3) {
        if (this.mIsClipHeightAnimating) {
            return Math.min(this.mClipHeight, i3);
        }
        if (getVisibility() != 0) {
            return 0;
        }
        int i4 = this.mClipHeight;
        return i4 > 0 ? Math.min(i4, i3) : i3;
    }

    private void cancelAnimationIfRunning() {
        ValueAnimator valueAnimator = this.mClipAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mClipAnimator.cancel();
        }
        this.mIsClipHeightAnimating = false;
    }

    private void copyContentToView(COUIDefaultTopTipsView cOUIDefaultTopTipsView, COUIDefaultTopTipsView cOUIDefaultTopTipsView2) {
        if (cOUIDefaultTopTipsView.getImage() != null && cOUIDefaultTopTipsView.getImage().getDrawable() != null) {
            cOUIDefaultTopTipsView2.setStartIcon(cOUIDefaultTopTipsView.getImage().getDrawable());
        }
        if (cOUIDefaultTopTipsView.getTitle() != null && !TextUtils.isEmpty(cOUIDefaultTopTipsView.getTitle().getText())) {
            cOUIDefaultTopTipsView2.setTitleText(cOUIDefaultTopTipsView.getTitle().getText());
        }
        if (cOUIDefaultTopTipsView.getContent() != null && !TextUtils.isEmpty(cOUIDefaultTopTipsView.getContent().getText())) {
            cOUIDefaultTopTipsView2.setTipsText(cOUIDefaultTopTipsView.getContent().getText());
            if (cOUIDefaultTopTipsView.getTopTipsStyle() == TopTipsStyle.STANDARD) {
                cOUIDefaultTopTipsView2.setTipsTextColor(cOUIDefaultTopTipsView.getContent().getCurrentTextColor());
            }
        }
        if (cOUIDefaultTopTipsView.getAction() != null && !TextUtils.isEmpty(cOUIDefaultTopTipsView.getAction().getText())) {
            cOUIDefaultTopTipsView2.setPositiveButton(cOUIDefaultTopTipsView.getAction().getText());
            cOUIDefaultTopTipsView2.setPositiveButtonColor(cOUIDefaultTopTipsView.getAction().getCurrentTextColor());
        }
        if (cOUIDefaultTopTipsView.getIgnore() != null && !TextUtils.isEmpty(cOUIDefaultTopTipsView.getIgnore().getText())) {
            cOUIDefaultTopTipsView2.setNegativeButton(cOUIDefaultTopTipsView.getIgnore().getText());
            cOUIDefaultTopTipsView2.setNegativeButtonColor(cOUIDefaultTopTipsView.getIgnore().getCurrentTextColor());
        }
        if (cOUIDefaultTopTipsView.getClose() != null && cOUIDefaultTopTipsView.getClose().getDrawable() != null) {
            cOUIDefaultTopTipsView2.setCloseDrawable(cOUIDefaultTopTipsView.getClose().getDrawable());
        }
        cOUIDefaultTopTipsView2.applyTopTipsStyle(cOUIDefaultTopTipsView.getTopTipsStyle());
        cOUIDefaultTopTipsView.copyInteractionStateTo(cOUIDefaultTopTipsView2);
    }

    private void createClipAnimator(int i3, int i4, Runnable runnable) {
        boolean z2 = i4 > i3;
        ValueAnimator valueAnimatorOfInt = ValueAnimator.ofInt(i3, i4);
        this.mClipAnimator = valueAnimatorOfInt;
        valueAnimatorOfInt.setDuration(250L);
        this.mClipAnimator.setInterpolator(new COUIEaseInterpolator());
        this.mClipAnimator.addUpdateListener(new f(this, z2));
        this.mClipAnimator.addListener(new AnonymousClass2(i4, z2, runnable));
        this.mClipAnimator.start();
    }

    private ValueAnimator createCornerAnimator(int i3, COUIEaseInterpolator cOUIEaseInterpolator) {
        float radius = getRadius();
        float weight = getWeight();
        float singleLineRadius = getSingleLineRadius();
        boolean z2 = Math.abs(radius - singleLineRadius) < 1.0f;
        if (z2) {
            singleLineRadius = getMultiLineRadius();
        }
        float f3 = singleLineRadius;
        float multiLineWeight = z2 ? getMultiLineWeight() : getSingleLineWeight();
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimatorOfFloat.setDuration(i3);
        valueAnimatorOfFloat.setInterpolator(cOUIEaseInterpolator);
        valueAnimatorOfFloat.addUpdateListener(new e(this, radius, f3, weight, multiLineWeight));
        return valueAnimatorOfFloat;
    }

    public static /* synthetic */ void d(COUIDefaultTopTips cOUIDefaultTopTips) {
        cOUIDefaultTopTips.setVisibility(8);
        cOUIDefaultTopTips.mClipHeight = 0;
        cOUIDefaultTopTips.invalidateOutline();
    }

    public static /* synthetic */ void e(COUIDefaultTopTips cOUIDefaultTopTips) {
        cOUIDefaultTopTips.measureAndStartExpandAnimation();
    }

    private void ensureBackgroundOverlay() {
        if (this.mBackgroundOverlay != null) {
            return;
        }
        BackgroundOverlayView backgroundOverlayView = new BackgroundOverlayView(getContext());
        this.mBackgroundOverlay = backgroundOverlayView;
        backgroundOverlayView.setClickable(false);
        this.mBackgroundOverlay.setFocusable(false);
        this.mBackgroundOverlay.setImportantForAccessibility(2);
        this.mBackgroundOverlay.setMinimumWidth(0);
        this.mBackgroundOverlay.setMinimumHeight(0);
        this.mBackgroundOverlay.setVisibility(8);
        addView(this.mBackgroundOverlay, 0, new FrameLayout.LayoutParams(0, 0));
    }

    public static /* synthetic */ void f(COUIDefaultTopTips cOUIDefaultTopTips, int i3, int i4, int i5, int i6, ValueAnimator valueAnimator) {
        cOUIDefaultTopTips.getClass();
        float fFloatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        int i7 = i3 + ((int) ((i4 - i3) * fFloatValue));
        int i8 = i5 + ((int) ((i6 - i5) * fFloatValue));
        ViewGroup.LayoutParams layoutParams = cOUIDefaultTopTips.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = i7;
            layoutParams.height = i8;
        }
        cOUIDefaultTopTips.layout(cOUIDefaultTopTips.getLeft(), cOUIDefaultTopTips.getTop(), cOUIDefaultTopTips.getLeft() + i7, cOUIDefaultTopTips.getTop() + i8);
        cOUIDefaultTopTips.invalidateOutline();
    }

    public static /* synthetic */ void g(COUIDefaultTopTips cOUIDefaultTopTips, COUIDefaultTopTipsView cOUIDefaultTopTipsView, int i3) {
        cOUIDefaultTopTips.getClass();
        cOUIDefaultTopTips.updateRadius(cOUIDefaultTopTipsView.hasTitle(), cOUIDefaultTopTipsView.isSingleLineText());
    }

    private float getMultiLineRadius() {
        int attrDimens;
        if (isSupportSmoothRoundCorner()) {
            int oSVersionCode = COUIVersionUtil.getOSVersionCode();
            if (oSVersionCode > 37) {
                attrDimens = getContext().getResources().getDimensionPixelSize(com.support.tips.R.dimen.coui_toptips_view_radius);
            } else {
                attrDimens = oSVersionCode == 37 ? getContext().getResources().getDimensionPixelSize(com.support.tips.R.dimen.coui_toptips_view_radius_os16) : getContext().getResources().getDimensionPixelSize(com.support.tips.R.dimen.coui_toptips_view_radius_os15);
            }
        } else {
            attrDimens = COUIContextUtil.getAttrDimens(getContext(), R.attr.couiRoundCornerXL);
        }
        return attrDimens;
    }

    private float getMultiLineWeight() {
        if (!isSupportSmoothRoundCorner()) {
            return 0.0f;
        }
        int oSVersionCode = COUIVersionUtil.getOSVersionCode();
        if (oSVersionCode > 37) {
            return 3.0f;
        }
        return oSVersionCode == 37 ? 0.0f : 1.1f;
    }

    private float getSingleLineRadius() {
        return isSupportSmoothRoundCorner() ? COUIContextUtil.getAttrDimens(getContext(), R.attr.couiRoundCornerFULL) : COUIContextUtil.getAttrDimens(getContext(), R.attr.couiRoundCornerXXL);
    }

    private float getSingleLineWeight() {
        if (!isSupportSmoothRoundCorner()) {
            return 0.0f;
        }
        int oSVersionCode = COUIVersionUtil.getOSVersionCode();
        return (oSVersionCode <= 37 && oSVersionCode == 37) ? 0.0f : 1.0f;
    }

    private void initializeRoundCorner() {
        setRadius(getMultiLineRadius());
        setWeight(getMultiLineWeight());
    }

    private boolean isSupportSmoothRoundCorner() {
        return RoundCornerUtil.isVersionSupport() && RoundCornerUtil.isSmoothRoundRectOn();
    }

    private void layoutBackgroundOverlay() {
        View view = this.mBackgroundOverlay;
        if (view == null || view.getVisibility() != 0) {
            return;
        }
        int width = getWidth();
        int height = getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        this.mBackgroundOverlay.layout(0, 0, width, height);
    }

    private void measureAndStartExpandAnimation() {
        measure(View.MeasureSpec.makeMeasureSpec(getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(0, 0));
        startExpandAnimation(getMeasuredHeight());
    }

    private void setupOutlineProvider() {
        setClipToOutline(true);
        setOutlineProvider(new AnonymousClass1());
    }

    private void startExpandAnimation(int i3) {
        if (i3 <= 0) {
            setVisibility(0);
            this.mClipHeight = 0;
            invalidateOutline();
            return;
        }
        if (getVisibility() == 0) {
            setVisibility(4);
        }
        this.mClipHeight = 0;
        invalidateOutline();
        IDefaultTopTips iDefaultTopTips = this.mDefaultTopTips;
        if (iDefaultTopTips instanceof COUIDefaultTopTipsView) {
            ((COUIDefaultTopTipsView) iDefaultTopTips).setChildViewsAlpha(0.0f);
        }
        createClipAnimator(0, i3, null);
    }

    private void startWidthHeightAndCrossFadeAnimation(int i3, int i4, int i5, int i6, COUIDefaultTopTipsView cOUIDefaultTopTipsView, COUIDefaultTopTipsView cOUIDefaultTopTipsView2) {
        ObjectAnimator objectAnimatorOfFloat;
        int top = cOUIDefaultTopTipsView.getImage() != null ? cOUIDefaultTopTipsView.getImage().getTop() : 0;
        boolean z2 = (cOUIDefaultTopTipsView.hasTitle() || cOUIDefaultTopTipsView.isSingleLineText() == cOUIDefaultTopTipsView2.isSingleLineText()) ? false : true;
        int paddingLeft = (i4 - getPaddingLeft()) - getPaddingRight();
        int paddingTop = (i6 - getPaddingTop()) - getPaddingBottom();
        cOUIDefaultTopTipsView2.setChildViewsAlpha(0.0f);
        addView(cOUIDefaultTopTipsView2, new FrameLayout.LayoutParams(paddingLeft, paddingTop));
        cOUIDefaultTopTipsView2.measure(View.MeasureSpec.makeMeasureSpec(paddingLeft, 1073741824), View.MeasureSpec.makeMeasureSpec(paddingTop, 1073741824));
        cOUIDefaultTopTipsView2.layout(0, 0, paddingLeft, paddingTop);
        ImageView image = cOUIDefaultTopTipsView.getImage();
        ImageView image2 = cOUIDefaultTopTipsView2.getImage();
        int top2 = (image2 != null ? image2.getTop() : 0) - top;
        if (image != null) {
            image.setAlpha(0.0f);
        }
        if (image2 != null) {
            image2.setAlpha(1.0f);
            image2.setTranslationY(-top2);
        }
        COUIEaseInterpolator cOUIEaseInterpolator = new COUIEaseInterpolator();
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        long j3 = 300;
        valueAnimatorOfFloat.setDuration(j3);
        valueAnimatorOfFloat.setInterpolator(cOUIEaseInterpolator);
        boolean z3 = z2;
        valueAnimatorOfFloat.addUpdateListener(new a(this, i3, i4, i5, i6));
        View[] animatableViewsExceptImage = cOUIDefaultTopTipsView.getAnimatableViewsExceptImage();
        View[] animatableViewsExceptImage2 = cOUIDefaultTopTipsView2.getAnimatableViewsExceptImage();
        ValueAnimator valueAnimatorOfFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimatorOfFloat2.setDuration(j3);
        valueAnimatorOfFloat2.setInterpolator(cOUIEaseInterpolator);
        valueAnimatorOfFloat2.addUpdateListener(new b(animatableViewsExceptImage, animatableViewsExceptImage2));
        if (top2 == 0 || image2 == null) {
            objectAnimatorOfFloat = null;
        } else {
            objectAnimatorOfFloat = ObjectAnimator.ofFloat(image2, "translationY", -top2, 0.0f);
            objectAnimatorOfFloat.setDuration(j3);
            objectAnimatorOfFloat.setInterpolator(cOUIEaseInterpolator);
        }
        ValueAnimator valueAnimatorCreateCornerAnimator = z3 ? createCornerAnimator(300, cOUIEaseInterpolator) : null;
        ArrayList arrayList = new ArrayList();
        arrayList.add(valueAnimatorOfFloat);
        arrayList.add(valueAnimatorOfFloat2);
        if (objectAnimatorOfFloat != null) {
            arrayList.add(objectAnimatorOfFloat);
        }
        if (valueAnimatorCreateCornerAnimator != null) {
            arrayList.add(valueAnimatorCreateCornerAnimator);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        this.mWidthChangeAnimatorSet = animatorSet;
        animatorSet.playTogether(arrayList);
        this.mWidthChangeAnimatorSet.addListener(new AnonymousClass3(cOUIDefaultTopTipsView, image2, cOUIDefaultTopTipsView2, i4, image));
        this.mWidthChangeAnimatorSet.start();
    }

    private void updateClipHeightIfNeeded(int i3) {
        if (this.mIsClipHeightAnimating || getVisibility() != 0 || i3 <= 0 || this.mClipHeight == i3) {
            return;
        }
        this.mClipHeight = i3;
        invalidateOutline();
    }

    private void updateRadius(boolean z2, boolean z3) {
        if (z2 || !z3) {
            setRadius(getMultiLineRadius());
            setWeight(getMultiLineWeight());
        } else {
            setRadius(getSingleLineRadius());
            setWeight(getSingleLineWeight());
        }
    }

    public void animateCollapse() {
        cancelAnimationIfRunning();
        int height = getHeight();
        if (height != 0) {
            createClipAnimator(height, 0, new c(this));
        } else {
            setVisibility(8);
            this.mClipHeight = 0;
        }
    }

    public void animateExpand() {
        cancelAnimationIfRunning();
        if (getVisibility() == 8) {
            setVisibility(4);
        }
        if (getWidth() == 0) {
            post(new g(this));
        } else {
            measureAndStartExpandAnimation();
        }
    }

    public void animateWidthChange(int i3, int i4) {
        if (i3 <= 0 || i4 <= 0 || i3 == i4) {
            return;
        }
        if (this.mIsWidthAnimating) {
            cancelWidthChangeAnimation();
        }
        IDefaultTopTips iDefaultTopTips = this.mDefaultTopTips;
        if (iDefaultTopTips instanceof COUIDefaultTopTipsView) {
            COUIDefaultTopTipsView cOUIDefaultTopTipsView = (COUIDefaultTopTipsView) iDefaultTopTips;
            int height = getHeight();
            COUIDefaultTopTipsView cOUIDefaultTopTipsViewCreateForLayoutMode = COUIDefaultTopTipsView.createForLayoutMode(getContext(), cOUIDefaultTopTipsView.getContentLayoutMode());
            copyContentToView(cOUIDefaultTopTipsView, cOUIDefaultTopTipsViewCreateForLayoutMode);
            int iCalculateHeightForWidth = cOUIDefaultTopTipsViewCreateForLayoutMode.calculateHeightForWidth((i4 - getPaddingLeft()) - getPaddingRight()) + getPaddingTop() + getPaddingBottom();
            cOUIDefaultTopTipsView.createSpaceAndFixLayout(i3, height);
            startWidthHeightAndCrossFadeAnimation(i3, i4, height, iCalculateHeightForWidth, cOUIDefaultTopTipsView, cOUIDefaultTopTipsViewCreateForLayoutMode);
        }
    }

    public void cancelWidthChangeAnimation() {
        AnimatorSet animatorSet = this.mWidthChangeAnimatorSet;
        if (animatorSet != null && animatorSet.isRunning()) {
            this.mWidthChangeAnimatorSet.cancel();
        }
        invalidateOutline();
    }

    public IDefaultTopTips generateView() {
        COUIDefaultTopTipsView cOUIDefaultTopTipsView = new COUIDefaultTopTipsView(getContext());
        cOUIDefaultTopTipsView.setOnLinesChangedListener(new d(this, cOUIDefaultTopTipsView));
        cOUIDefaultTopTipsView.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
        setContentView(cOUIDefaultTopTipsView);
        return cOUIDefaultTopTipsView;
    }

    @Override // com.coui.appcompat.tips.COUICustomTopTips
    public int getContentViewId() {
        return 0;
    }

    @Override // com.coui.appcompat.tips.COUICustomTopTips
    public void init() {
        if (this.mLastBackgroundStyle == null) {
            this.mLastBackgroundStyle = BackgroundStyle.GRAY;
        }
        COUIDarkModeUtil.setForceDarkAllow(this, false);
        initializeRoundCorner();
        this.mDefaultTopTips = generateView();
        applySolidBackgroundColor(this.mLastBackgroundStyle);
        cacheSmoothStyleType();
        setupOutlineProvider();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelAnimationIfRunning();
        cancelWidthChangeAnimation();
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        super.onLayout(z2, i3, i4, i5, i6);
        layoutBackgroundOverlay();
        updateClipHeightIfNeeded(getHeight());
    }

    public void refreshContentLayout() {
        IDefaultTopTips iDefaultTopTips = this.mDefaultTopTips;
        if (iDefaultTopTips instanceof COUIDefaultTopTipsView) {
            COUIDefaultTopTipsView cOUIDefaultTopTipsView = (COUIDefaultTopTipsView) iDefaultTopTips;
            cOUIDefaultTopTipsView.applyNewLayoutConstraints();
            cOUIDefaultTopTipsView.requestLayout();
        }
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.mSuppressRequestLayout) {
            return;
        }
        super.requestLayout();
    }

    @Override // com.coui.appcompat.tips.def.IDefaultTopTips
    public void setCloseBtnListener(View.OnClickListener onClickListener) {
        this.mDefaultTopTips.setCloseBtnListener(onClickListener);
    }

    @Override // com.coui.appcompat.tips.def.IDefaultTopTips
    public void setCloseDrawable(Drawable drawable) {
        this.mDefaultTopTips.setCloseDrawable(drawable);
    }

    @Override // com.coui.appcompat.tips.def.IDefaultTopTips
    public void setNegativeButton(CharSequence charSequence) {
        this.mDefaultTopTips.setNegativeButton(charSequence);
    }

    @Override // com.coui.appcompat.tips.def.IDefaultTopTips
    public void setNegativeButtonColor(int i3) {
        this.mDefaultTopTips.setNegativeButtonColor(i3);
    }

    @Override // com.coui.appcompat.tips.def.IDefaultTopTips
    public void setNegativeButtonListener(View.OnClickListener onClickListener) {
        this.mDefaultTopTips.setNegativeButtonListener(onClickListener);
    }

    @Override // com.coui.appcompat.tips.def.IDefaultTopTips
    public void setPositiveButton(CharSequence charSequence) {
        this.mDefaultTopTips.setPositiveButton(charSequence);
    }

    @Override // com.coui.appcompat.tips.def.IDefaultTopTips
    public void setPositiveButtonColor(int i3) {
        this.mDefaultTopTips.setPositiveButtonColor(i3);
    }

    @Override // com.coui.appcompat.tips.def.IDefaultTopTips
    public void setPositiveButtonListener(View.OnClickListener onClickListener) {
        this.mDefaultTopTips.setPositiveButtonListener(onClickListener);
    }

    @Override // com.coui.appcompat.tips.def.IDefaultTopTips
    public void setStartIcon(Drawable drawable) {
        this.mDefaultTopTips.setStartIcon(drawable);
    }

    public void setTipsBackgroundColor(BackgroundStyle backgroundStyle) {
        this.mLastBackgroundStyle = backgroundStyle;
        if (this.mBackgroundOverlayDrawable != null) {
            return;
        }
        applySolidBackgroundColor(backgroundStyle);
    }

    public void setTipsBackgroundDrawable(Drawable drawable) {
        this.mBackgroundOverlayDrawable = drawable;
        applyBackgroundOverlayState();
    }

    @Override // com.coui.appcompat.tips.def.IDefaultTopTips
    public void setTipsText(CharSequence charSequence) {
        this.mDefaultTopTips.setTipsText(charSequence);
    }

    @Override // com.coui.appcompat.tips.def.IDefaultTopTips
    public void setTipsTextColor(int i3) {
        this.mDefaultTopTips.setTipsTextColor(i3);
    }

    @Override // com.coui.appcompat.tips.def.IDefaultTopTips
    public void setTitleText(CharSequence charSequence) {
        this.mDefaultTopTips.setTitleText(charSequence);
    }

    public void setTopTipsStyle(TopTipsStyle topTipsStyle) {
        if (topTipsStyle == null) {
            topTipsStyle = TopTipsStyle.STANDARD;
        }
        if (this.mTopTipsStyle == topTipsStyle) {
            return;
        }
        this.mTopTipsStyle = topTipsStyle;
        applyTopTipsStyle();
    }

    public COUIDefaultTopTips(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIDefaultTopTips(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.mClipRect = new Rect();
        this.mClipHeight = 0;
        this.mIsClipHeightAnimating = false;
        this.mCachedSmoothStyleType = -1;
        this.mSmoothStyleTypeCached = false;
        this.mIsWidthAnimating = false;
        this.mSuppressRequestLayout = false;
        this.mTopTipsStyle = TopTipsStyle.STANDARD;
        this.mLastBackgroundStyle = BackgroundStyle.GRAY;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, com.support.tips.R.styleable.COUIDefaultTopTips, i3, 0);
        int i4 = typedArrayObtainStyledAttributes.getInt(com.support.tips.R.styleable.COUIDefaultTopTips_couiTopTipsStyle, 0);
        typedArrayObtainStyledAttributes.recycle();
        if (i4 == 1) {
            setTopTipsStyle(TopTipsStyle.AI_PAYMENT);
            ensureBackgroundOverlay();
        }
    }
}