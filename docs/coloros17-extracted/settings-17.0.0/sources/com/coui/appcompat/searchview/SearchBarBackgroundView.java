package com.coui.appcompat.searchview;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import com.coui.appcompat.COUIMaterialBlurEffect;
import com.coui.appcompat.COUIMaterialEffect;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.COUIMaterialStrokeEffect;
import com.coui.appcompat.animation.touchanimation.COUIViewTouchAnimator;
import com.coui.appcompat.animation.touchanimation.TouchMotionParamConfig;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.rippleutil.COUIRippleDrawableUtil;
import com.coui.appcompat.shadowedge.COUIShadowEdgeDrawable;
import com.coui.appcompat.shadowedge.IShadowEdgeEffect;
import com.coui.appcompat.spotlight.COUISpotLightEffect;
import com.coui.appcompat.spotlight.COUISpotLightEffectDrawable;
import com.coui.appcompat.state.COUIMaskEffectDrawable;
import com.coui.appcompat.state.COUIStateEffectDrawable;
import com.coui.appcompat.toolbar.ToolbarMaterialEffectResolver;
import com.coui.appcompat.uiutil.UIUtil;
import com.oplus.view.material.OplusMaterialBaseParams;
import com.oplus.view.material.OplusMaterialCornerParams;
import com.support.toolbar.R;

/* JADX INFO: loaded from: classes.dex */
public class SearchBarBackgroundView extends ViewGroup {
    private static final float CORNER_RADIUS_PARAM = 60.0f;
    private static final ArgbEvaluator DEFAULT_EVALUATOR = new ArgbEvaluator();
    private static final int DEFAULT_MAX_LINES = 1;
    private static final int SHADOW_EDGE_CORNER_TYPE_G2 = 2;
    private static final float SHADOW_EDGE_CORNER_WEIGHT = 1.0f;
    private static final float SMOOTH_CORNER_WEIGHT = 1.0f;
    private static final int SNAP_THRESHOLD = 30;
    private boolean mAddToToolbarAtBehind;
    private int mAnimEditFrameMaxWidth;
    private int mAnimEditFrameMinWidth;
    private final RectF mBackgroundBounds;
    private final ColorDrawable mBackgroundColor;
    private final Rect mBackgroundRect;
    private final COUISearchBar.SmoothRoundCornerHelper mBackgroundSmoothRoundCornerHelper;
    private boolean mBlurEffective;
    private int mButtonDividerColor;
    private COUIHintAnimationLayout mCOUIHintAnimationLayout;
    private int mClearTextDrawableResourceId;
    private OplusMaterialCornerParams mCornerParams;
    private int mCurrentAnimClipWidth;
    private int mCurrentBackgroundColor;
    private float mDividerAlpha;
    private int mDividerHeight;
    private final Paint mDividerPaint;
    private final Rect mDividerRect;
    private int mDividerWidth;
    private final RectF mDrawingBounds;
    private final Rect mEditFrameClipRect;
    private int mEditFrameEndGap;
    private TextView mFunctionalButton;
    private boolean mHideFunctionalButton;
    private final RectF mIconExpandClipBounds;
    private int mIconExpandSearchIconStartGap;
    private int mIconMaxHeight;
    private int mIconMaxWidth;
    private int mInnerIconSize;
    private ImageView mInnerPrimaryButton;
    private ImageView mInnerSecondaryButton;
    private boolean mIsAnimating;
    private boolean mIsDragging;
    private boolean mIsDrawableMaterialEffect;
    private boolean mIsIconExpandClipping;
    private boolean mIsToolbarFadeAnimating;
    private COUIMaskEffectDrawable mMaskEffectDrawable;
    private OplusMaterialBaseParams mMaterialBaseParams;
    private int mMaterialType;
    private COUIShadowEdgeDrawable mNoBlurShadowEdge;
    private int mNoBlurSolidColor;
    private float mNoBlurStrokeProgress;
    private int mNonInstantSearchInnerGap;
    private int mNormalBackgroundHeight;
    private ColorStateList mNormalHintColor;
    private ImageView mQuickDeleteButton;
    private EditText mSearchEditText;
    private int mSearchIconStartGap;
    private ImageView mSearchIconView;
    private int mSearchViewType;
    private final Path mSmoothRoundPath;
    private final RectF mSmoothRoundRectF;
    private final COUISpotLightEffectDrawable mSpotLight;
    private COUISpotLightEffect.SpotLightType mSpotLightType;
    private boolean mSpotlightEnabled;
    private int mState;
    private boolean mStrokeEffectForceDark;
    private boolean mStrokeEnabled;
    private COUIMaterialStrokeEffect.StrokeEffectType mStrokeType;
    private COUIViewTouchAnimator mTouchMotionAnimator;

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.SearchBarBackgroundView$1, reason: invalid class name */
    public class AnonymousClass1 extends ViewOutlineProvider {
        public AnonymousClass1() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            if (SearchBarBackgroundView.access$000(SearchBarBackgroundView.this)) {
                outline.setPath(SearchBarBackgroundView.access$100(SearchBarBackgroundView.this));
            } else {
                outline.setRoundRect(SearchBarBackgroundView.access$200(SearchBarBackgroundView.this), SearchBarBackgroundView.access$200(SearchBarBackgroundView.this).height() / 2.0f);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.SearchBarBackgroundView$2, reason: invalid class name */
    public class AnonymousClass2 implements TextWatcher {
        public AnonymousClass2() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            if (TextUtils.isEmpty(editable)) {
                SearchBarBackgroundView searchBarBackgroundView = SearchBarBackgroundView.this;
                SearchBarBackgroundView.access$500(searchBarBackgroundView, SearchBarBackgroundView.access$400(searchBarBackgroundView), true);
                SearchBarBackgroundView searchBarBackgroundView2 = SearchBarBackgroundView.this;
                SearchBarBackgroundView.access$500(searchBarBackgroundView2, SearchBarBackgroundView.access$600(searchBarBackgroundView2), true);
                SearchBarBackgroundView searchBarBackgroundView3 = SearchBarBackgroundView.this;
                SearchBarBackgroundView.access$500(searchBarBackgroundView3, SearchBarBackgroundView.access$700(searchBarBackgroundView3), false);
                return;
            }
            SearchBarBackgroundView searchBarBackgroundView4 = SearchBarBackgroundView.this;
            SearchBarBackgroundView.access$500(searchBarBackgroundView4, SearchBarBackgroundView.access$400(searchBarBackgroundView4), false);
            SearchBarBackgroundView searchBarBackgroundView5 = SearchBarBackgroundView.this;
            SearchBarBackgroundView.access$500(searchBarBackgroundView5, SearchBarBackgroundView.access$600(searchBarBackgroundView5), false);
            SearchBarBackgroundView searchBarBackgroundView6 = SearchBarBackgroundView.this;
            SearchBarBackgroundView.access$500(searchBarBackgroundView6, SearchBarBackgroundView.access$700(searchBarBackgroundView6), true);
            if (SearchBarBackgroundView.access$400(SearchBarBackgroundView.this) != null) {
                SearchBarBackgroundView.access$400(SearchBarBackgroundView.this).setTranslationX(0.0f);
            }
            if (SearchBarBackgroundView.access$600(SearchBarBackgroundView.this) != null) {
                SearchBarBackgroundView.access$600(SearchBarBackgroundView.this).setTranslationX(0.0f);
            }
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
            SearchBarBackgroundView.access$300(SearchBarBackgroundView.this);
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
        }
    }

    public SearchBarBackgroundView(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void a(SearchBarBackgroundView searchBarBackgroundView, View view) {
        EditText editText = searchBarBackgroundView.mSearchEditText;
        if (editText != null) {
            editText.setText((CharSequence) null);
        }
    }

    public static /* synthetic */ boolean access$000(SearchBarBackgroundView searchBarBackgroundView) {
        return searchBarBackgroundView.isSmoothOn();
    }

    public static /* synthetic */ Path access$100(SearchBarBackgroundView searchBarBackgroundView) {
        return searchBarBackgroundView.mSmoothRoundPath;
    }

    public static /* synthetic */ Rect access$200(SearchBarBackgroundView searchBarBackgroundView) {
        return searchBarBackgroundView.mBackgroundRect;
    }

    public static /* synthetic */ void access$300(SearchBarBackgroundView searchBarBackgroundView) {
        searchBarBackgroundView.ensureQuickDeleteButton();
    }

    public static /* synthetic */ ImageView access$400(SearchBarBackgroundView searchBarBackgroundView) {
        return searchBarBackgroundView.mInnerPrimaryButton;
    }

    public static /* synthetic */ void access$500(SearchBarBackgroundView searchBarBackgroundView, View view, boolean z2) {
        searchBarBackgroundView.showButton(view, z2);
    }

    public static /* synthetic */ ImageView access$600(SearchBarBackgroundView searchBarBackgroundView) {
        return searchBarBackgroundView.mInnerSecondaryButton;
    }

    public static /* synthetic */ ImageView access$700(SearchBarBackgroundView searchBarBackgroundView) {
        return searchBarBackgroundView.mQuickDeleteButton;
    }

    private void applyFrameworkMaterialSurface() {
        if (this.mBlurEffective) {
            COUIMaterialEffect.setViewBlurEffect(this, ToolbarMaterialEffectResolver.getBlurEffectType(this.mMaterialType));
        } else {
            COUIMaterialEffect.setViewBlurEffect(this, COUIMaterialBlurEffect.BlurEffectType.TYPE_NO_EFFECT);
        }
        if (this.mStrokeEnabled) {
            COUIMaterialEffect.setViewStrokeEffect(this, ToolbarMaterialEffectResolver.getCapsuleStrokeEffectType(this.mMaterialType));
        } else {
            COUIMaterialEffect.setViewStrokeEffect(this, COUIMaterialStrokeEffect.StrokeEffectType.TYPE_NO_EFFECT);
        }
        setBackgroundColorTransparent(this.mBlurEffective);
        setClipToOutline(!this.mBlurEffective);
        setCornerRadius(getMeasuredHeight() / 2.0f);
    }

    private void applyNoBlurMaterialSurface() {
        COUIMaterialEffect.setViewBlurEffect(this, COUIMaterialBlurEffect.BlurEffectType.TYPE_NO_EFFECT);
        COUIMaterialEffect.setViewStrokeEffect(this, COUIMaterialStrokeEffect.StrokeEffectType.TYPE_NO_EFFECT);
        this.mBackgroundColor.setColor(this.mNoBlurSolidColor);
        setBackgroundColorTransparent(false);
        setClipToOutline(true);
        setCornerRadius(getMeasuredHeight() / 2.0f);
        if (COUIMaterialStateManager.INSTANCE.materialVersionCheck() && this.mStrokeEnabled) {
            applyNoBlurShadowInterpolated();
        } else {
            releaseNoBlurShadowEdge();
        }
        invalidate();
    }

    private void applyNoBlurShadowInterpolated() {
        ensureNoBlurShadowEdge();
        if (this.mNoBlurShadowEdge == null) {
            return;
        }
        setStrokeEffectType(this.mStrokeType, null);
    }

    private int calculateRelativeTop(int i3, int i4, int i5) {
        return i3 + ((i4 - i5) / 2);
    }

    private void clearEditFrameClip() {
        this.mCurrentAnimClipWidth = -1;
        View searchEditOrAnimationLayout = getSearchEditOrAnimationLayout();
        if (searchEditOrAnimationLayout != null) {
            searchEditOrAnimationLayout.setClipBounds(null);
        }
    }

    private void configImageViewDrawable(ImageView imageView, Drawable drawable, int i3) {
        if (imageView != null) {
            imageView.setImageDrawable(drawable);
            imageView.setClickable(true);
            if (drawable != null) {
                int intrinsicWidth = (i3 - drawable.getIntrinsicWidth()) / 2;
                imageView.setPadding(intrinsicWidth, 0, intrinsicWidth, 0);
            }
        }
    }

    private void drawNoBlurShadowEdge(Canvas canvas) {
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mNoBlurShadowEdge;
        if (cOUIShadowEdgeDrawable == null || !cOUIShadowEdgeDrawable.getMDrawableEnabled()) {
            return;
        }
        RectF rectF = this.mIsIconExpandClipping ? this.mIconExpandClipBounds : this.mBackgroundBounds;
        float fWidth = rectF.width();
        float fHeight = rectF.height();
        if (fWidth <= 0.0f || fHeight <= 0.0f || this.mSmoothRoundPath.isEmpty()) {
            return;
        }
        this.mNoBlurShadowEdge.setBounds(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.left + fWidth), Math.round(rectF.top + fHeight));
        this.mNoBlurShadowEdge.setCornerRadius(fHeight / 2.0f);
        this.mNoBlurShadowEdge.draw(canvas);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        drawable.draw(canvas);
        return bitmapCreateBitmap;
    }

    private ImageView ensureImageView(Drawable drawable, boolean z2, boolean z3, int i3) {
        if (drawable == null) {
            return null;
        }
        ImageView imageView = new ImageView(getContext());
        if (!z3) {
            imageView.setClickable(false);
            imageView.setFocusable(false);
        }
        if (z2 && z3) {
            COUIRippleDrawableUtil.setIconPressRippleDrawable(imageView, i3);
        }
        addView(imageView);
        return imageView;
    }

    private void ensureNoBlurShadowEdge() {
        if (this.mNoBlurShadowEdge != null) {
            return;
        }
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = new COUIShadowEdgeDrawable();
        this.mNoBlurShadowEdge = cOUIShadowEdgeDrawable;
        cOUIShadowEdgeDrawable.setCallback(this);
        this.mNoBlurShadowEdge.setEdgeEnable(true).setShadowEnable(true).setEdgeStyle(IShadowEdgeEffect.EdgeStyle.EDGE_STYLE_0).setShadowStyle(IShadowEdgeEffect.ShadowStyle.SHADOW_STYLE_0).setCornerType(2).setWeight(1.0f).setIsDarkMode(resolveShadowEdgeIsDarkMode());
        this.mNoBlurShadowEdge.setPaintBlendMode(BlendMode.SRC_OVER);
    }

    private void ensureQuickDeleteButton() {
        Drawable drawable;
        if (this.mQuickDeleteButton == null && (drawable = ResourcesCompat.getDrawable(getResources(), this.mClearTextDrawableResourceId, getContext().getTheme())) != null) {
            ImageView imageViewEnsureImageView = ensureImageView(drawable, true, true, this.mInnerIconSize / 2);
            this.mQuickDeleteButton = imageViewEnsureImageView;
            imageViewEnsureImageView.setContentDescription(getResources().getString(R.string.coui_search_clear_button_description));
            configImageViewDrawable(this.mQuickDeleteButton, drawable, this.mInnerIconSize);
            showButton(this.mQuickDeleteButton, false);
            this.mQuickDeleteButton.setOnClickListener(new t(this));
        }
    }

    private boolean functionalButtonShouldShow() {
        return !this.mHideFunctionalButton;
    }

    private int getSearchIconStartGapForLayout() {
        return this.mAddToToolbarAtBehind ? this.mIconExpandSearchIconStartGap : this.mSearchIconStartGap;
    }

    private void init(Context context) {
        this.mEditFrameEndGap = context.getResources().getDimensionPixelOffset(R.dimen.coui_search_bar_edittext_end_gap);
        this.mNonInstantSearchInnerGap = context.getResources().getDimensionPixelOffset(R.dimen.coui_search_bar_non_instant_search_inner_gap);
        this.mNormalBackgroundHeight = context.getResources().getDimensionPixelOffset(R.dimen.coui_search_bar_normal_background_height);
        this.mInnerIconSize = context.getResources().getDimensionPixelOffset(R.dimen.coui_search_bar_inner_search_icon_size);
        this.mSearchIconStartGap = context.getResources().getDimensionPixelOffset(R.dimen.coui_search_bar_inner_search_icon_start_gap);
        this.mIconExpandSearchIconStartGap = context.getResources().getDimensionPixelOffset(R.dimen.coui_search_bar_icon_expand_search_icon_start_gap);
        this.mDividerWidth = context.getResources().getDimensionPixelOffset(R.dimen.coui_search_bar_functional_button_divider_width);
        this.mDividerHeight = context.getResources().getDimensionPixelOffset(R.dimen.coui_search_bar_functional_button_divider_height);
    }

    private void initEdittext() {
        AppCompatEditText appCompatEditText = new AppCompatEditText(new ContextThemeWrapper(getContext(), R.style.Widget_COUI_EditText_SearchViewStyle), null);
        this.mSearchEditText = appCompatEditText;
        appCompatEditText.setVerticalScrollBarEnabled(false);
        this.mSearchEditText.setMaxLines(1);
        this.mSearchEditText.setInputType(1);
        this.mSearchEditText.setEllipsize(TextUtils.TruncateAt.END);
        this.mSearchEditText.setImeOptions(3);
        this.mSearchEditText.setId(R.id.search_src_text);
        this.mSearchEditText.setImportantForAccessibility(2);
        this.mSearchEditText.setImportantForAutofill(2);
        this.mSearchEditText.addTextChangedListener(new AnonymousClass2());
        addView(this.mSearchEditText);
    }

    private void initTouchAnimation() {
        this.mTouchMotionAnimator = new COUIViewTouchAnimator(getContext(), this);
        setTouchAnimationEnable(true);
        this.mTouchMotionAnimator.config(new TouchMotionParamConfig());
    }

    private boolean isInsideBackground(float f3, float f4) {
        return this.mIsIconExpandClipping ? this.mIconExpandClipBounds.contains((int) f3, (int) f4) : this.mDrawingBounds.contains((int) f3, (int) f4);
    }

    private boolean isInsideFunctionalButton(float f3, float f4) {
        return this.mSearchViewType == 1 && isInsideBackground(f3, f4) && isInsideView(this.mFunctionalButton, f3, f4);
    }

    private boolean isInsideView(View view, float f3, float f4) {
        return view != null && view.getVisibility() != 8 && f3 >= ((float) view.getLeft()) + view.getTranslationX() && f3 <= ((float) view.getRight()) + view.getTranslationX() && f4 >= ((float) view.getTop()) + view.getTranslationY() && f4 <= ((float) view.getBottom()) + view.getTranslationY();
    }

    private boolean isRtl() {
        return ViewCompat.getLayoutDirection(this) == 1;
    }

    private boolean isSmoothOn() {
        return this.mBackgroundSmoothRoundCornerHelper.getSmoothStyleType() == 1;
    }

    private void layoutBackground() {
        if (isRtl()) {
            this.mBackgroundRect.set(0, 0, Math.max(getMeasuredWidth(), getWidth()), getMeasuredHeight());
        } else {
            this.mBackgroundRect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        }
    }

    private void layoutBackgroundArea() {
        layoutBackground();
        layoutSearchIcon();
        layoutEditFrame();
        int iLayoutInnerButton = layoutInnerButton();
        if (this.mSearchViewType == 1) {
            layoutFunctionalButton(layoutDivider(iLayoutInnerButton));
        }
    }

    private int layoutDivider(int i3) {
        int iWidth;
        int i4;
        Rect rect = this.mBackgroundRect;
        int iCalculateRelativeTop = calculateRelativeTop(rect.top, rect.height(), this.mDividerRect.height());
        int iHeight = this.mDividerRect.height() + iCalculateRelativeTop;
        if (isRtl()) {
            int iWidth2 = i3 - this.mDividerRect.width();
            i4 = i3 - this.mNonInstantSearchInnerGap;
            iWidth = i3;
            i3 = iWidth2;
        } else {
            iWidth = this.mDividerRect.width() + i3;
            i4 = this.mNonInstantSearchInnerGap + i3;
        }
        int iWidth3 = i4 + this.mDividerRect.width();
        this.mDividerRect.set(i3, iCalculateRelativeTop, iWidth, iHeight);
        return iWidth3;
    }

    private void layoutEditFrame() {
        Rect rect = this.mBackgroundRect;
        int iCalculateRelativeTop = calculateRelativeTop(rect.top, rect.height(), getSearchEditOrAnimationLayout().getMeasuredHeight());
        if (isRtl()) {
            int left = shouldLayoutOut(this.mSearchIconView) ? this.mSearchIconView.getLeft() : this.mBackgroundRect.right;
            getSearchEditOrAnimationLayout().layout(left - getSearchEditOrAnimationLayout().getMeasuredWidth(), iCalculateRelativeTop, left, getSearchEditOrAnimationLayout().getMeasuredHeight() + iCalculateRelativeTop);
        } else {
            int right = shouldLayoutOut(this.mSearchIconView) ? this.mSearchIconView.getRight() : this.mBackgroundRect.left;
            getSearchEditOrAnimationLayout().layout(right, iCalculateRelativeTop, getSearchEditOrAnimationLayout().getMeasuredWidth() + right, getSearchEditOrAnimationLayout().getMeasuredHeight() + iCalculateRelativeTop);
        }
    }

    private void layoutFunctionalButton(int i3) {
        if (shouldLayoutOut(this.mFunctionalButton)) {
            Rect rect = this.mBackgroundRect;
            int iCalculateRelativeTop = calculateRelativeTop(rect.top, rect.height(), this.mFunctionalButton.getMeasuredHeight());
            if (isRtl()) {
                TextView textView = this.mFunctionalButton;
                textView.layout(i3 - textView.getMeasuredWidth(), iCalculateRelativeTop, i3, this.mFunctionalButton.getMeasuredHeight() + iCalculateRelativeTop);
            } else {
                TextView textView2 = this.mFunctionalButton;
                textView2.layout(i3, iCalculateRelativeTop, textView2.getMeasuredWidth() + i3, this.mFunctionalButton.getMeasuredHeight() + iCalculateRelativeTop);
            }
        }
    }

    private int layoutInnerButton() {
        if (isRtl()) {
            int left = getSearchEditOrAnimationLayout().getLeft() - this.mEditFrameEndGap;
            if (shouldLayoutOut(this.mQuickDeleteButton)) {
                Rect rect = this.mBackgroundRect;
                int iCalculateRelativeTop = calculateRelativeTop(rect.top, rect.height(), this.mQuickDeleteButton.getMeasuredHeight());
                ImageView imageView = this.mQuickDeleteButton;
                imageView.layout(left - imageView.getMeasuredWidth(), iCalculateRelativeTop, left, this.mQuickDeleteButton.getMeasuredHeight() + iCalculateRelativeTop);
                left -= this.mQuickDeleteButton.getMeasuredWidth();
            }
            if (shouldLayoutOut(this.mInnerPrimaryButton)) {
                Rect rect2 = this.mBackgroundRect;
                int iCalculateRelativeTop2 = calculateRelativeTop(rect2.top, rect2.height(), this.mInnerPrimaryButton.getMeasuredHeight());
                ImageView imageView2 = this.mInnerPrimaryButton;
                imageView2.layout(left - imageView2.getMeasuredWidth(), iCalculateRelativeTop2, left, this.mInnerPrimaryButton.getMeasuredHeight() + iCalculateRelativeTop2);
                left -= this.mInnerPrimaryButton.getMeasuredWidth();
            }
            if (shouldLayoutOut(this.mInnerSecondaryButton)) {
                Rect rect3 = this.mBackgroundRect;
                int iCalculateRelativeTop3 = calculateRelativeTop(rect3.top, rect3.height(), this.mInnerSecondaryButton.getMeasuredHeight());
                ImageView imageView3 = this.mInnerSecondaryButton;
                imageView3.layout(left - imageView3.getMeasuredWidth(), iCalculateRelativeTop3, left, this.mInnerSecondaryButton.getMeasuredHeight() + iCalculateRelativeTop3);
                left -= this.mInnerSecondaryButton.getMeasuredWidth();
            }
            return left != getSearchEditOrAnimationLayout().getLeft() ? left - this.mNonInstantSearchInnerGap : left;
        }
        int right = getSearchEditOrAnimationLayout().getRight() + this.mEditFrameEndGap;
        if (shouldLayoutOut(this.mQuickDeleteButton)) {
            Rect rect4 = this.mBackgroundRect;
            int iCalculateRelativeTop4 = calculateRelativeTop(rect4.top, rect4.height(), this.mQuickDeleteButton.getMeasuredHeight());
            ImageView imageView4 = this.mQuickDeleteButton;
            imageView4.layout(right, iCalculateRelativeTop4, imageView4.getMeasuredWidth() + right, this.mQuickDeleteButton.getMeasuredHeight() + iCalculateRelativeTop4);
            right += this.mQuickDeleteButton.getMeasuredWidth();
        }
        if (shouldLayoutOut(this.mInnerPrimaryButton)) {
            Rect rect5 = this.mBackgroundRect;
            int iCalculateRelativeTop5 = calculateRelativeTop(rect5.top, rect5.height(), this.mInnerPrimaryButton.getMeasuredHeight());
            ImageView imageView5 = this.mInnerPrimaryButton;
            imageView5.layout(right, iCalculateRelativeTop5, imageView5.getMeasuredWidth() + right, this.mInnerPrimaryButton.getMeasuredHeight() + iCalculateRelativeTop5);
            right += this.mInnerPrimaryButton.getMeasuredWidth();
        }
        if (shouldLayoutOut(this.mInnerSecondaryButton)) {
            Rect rect6 = this.mBackgroundRect;
            int iCalculateRelativeTop6 = calculateRelativeTop(rect6.top, rect6.height(), this.mInnerSecondaryButton.getMeasuredHeight());
            ImageView imageView6 = this.mInnerSecondaryButton;
            imageView6.layout(right, iCalculateRelativeTop6, imageView6.getMeasuredWidth() + right, this.mInnerSecondaryButton.getMeasuredHeight() + iCalculateRelativeTop6);
            right += this.mInnerSecondaryButton.getMeasuredWidth();
        }
        return right != getSearchEditOrAnimationLayout().getRight() ? right + this.mNonInstantSearchInnerGap : right;
    }

    private void layoutSearchIcon() {
        if (shouldLayoutOut(this.mSearchIconView)) {
            Rect rect = this.mBackgroundRect;
            int iCalculateRelativeTop = calculateRelativeTop(rect.top, rect.height(), this.mSearchIconView.getMeasuredHeight());
            int searchIconStartGapForLayout = getSearchIconStartGapForLayout();
            if (isRtl()) {
                int i3 = this.mBackgroundRect.right - searchIconStartGapForLayout;
                ImageView imageView = this.mSearchIconView;
                imageView.layout(i3 - imageView.getMeasuredWidth(), iCalculateRelativeTop, i3, this.mSearchIconView.getMeasuredHeight() + iCalculateRelativeTop);
            } else {
                int i4 = this.mBackgroundRect.left + searchIconStartGapForLayout;
                ImageView imageView2 = this.mSearchIconView;
                imageView2.layout(i4, iCalculateRelativeTop, imageView2.getMeasuredWidth() + i4, this.mSearchIconView.getMeasuredHeight() + iCalculateRelativeTop);
            }
        }
    }

    private void loadHideFunctionButtonAttr(Context context, AttributeSet attributeSet, int i3, int i4) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUISearchBar, i3, i4);
        this.mHideFunctionalButton = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUISearchBar_couiHideFunctionalButton, false);
        typedArrayObtainStyledAttributes.recycle();
    }

    private int measureDivider(int i3) {
        if (this.mSearchViewType != 1) {
            return i3;
        }
        measureRect(this.mDividerRect, this.mDividerWidth, this.mDividerHeight);
        return (i3 - this.mNonInstantSearchInnerGap) - this.mDividerWidth;
    }

    private void measureEditFrame(int i3) {
        int i4 = this.mCurrentAnimClipWidth;
        if (i4 > 0) {
            i3 = i4;
        }
        measureView(getSearchEditOrAnimationLayout(), View.MeasureSpec.makeMeasureSpec(i3, 1073741824), View.MeasureSpec.makeMeasureSpec(this.mNormalBackgroundHeight, Integer.MIN_VALUE));
    }

    private int measureInnerButton(int i3) {
        int i4;
        int iMeasureView = shouldLayoutOut(this.mInnerPrimaryButton) ? i3 - measureView(this.mInnerPrimaryButton, View.MeasureSpec.makeMeasureSpec(this.mInnerIconSize, 1073741824), View.MeasureSpec.makeMeasureSpec(this.mInnerIconSize, 1073741824)) : i3;
        if (shouldLayoutOut(this.mInnerSecondaryButton)) {
            iMeasureView -= measureView(this.mInnerSecondaryButton, View.MeasureSpec.makeMeasureSpec(this.mInnerIconSize, 1073741824), View.MeasureSpec.makeMeasureSpec(this.mInnerIconSize, 1073741824));
        }
        if (shouldLayoutOut(this.mQuickDeleteButton)) {
            iMeasureView -= measureView(this.mQuickDeleteButton, View.MeasureSpec.makeMeasureSpec(this.mInnerIconSize, 1073741824), View.MeasureSpec.makeMeasureSpec(this.mInnerIconSize, 1073741824));
        }
        if (iMeasureView != i3) {
            i4 = this.mNonInstantSearchInnerGap;
        } else {
            if (this.mSearchViewType != 1 || !shouldLayoutOut(this.mFunctionalButton)) {
                return iMeasureView;
            }
            i4 = this.mNonInstantSearchInnerGap;
        }
        return iMeasureView - i4;
    }

    private void measureInsideBackground(int i3) {
        int iMeasureSearchIcon = measureSearchIcon(i3);
        int i4 = this.mSearchViewType;
        if (i4 == 0) {
            iMeasureSearchIcon = measureInnerButton(iMeasureSearchIcon);
        } else if (i4 == 1) {
            if (shouldLayoutOut(this.mFunctionalButton)) {
                iMeasureSearchIcon = measureDivider(iMeasureSearchIcon - (this.mFunctionalButton.getMeasuredWidth() + this.mNonInstantSearchInnerGap));
            }
            iMeasureSearchIcon = measureInnerButton(iMeasureSearchIcon);
        }
        measureEditFrame(iMeasureSearchIcon - this.mEditFrameEndGap);
    }

    private void measureRect(Rect rect, int i3, int i4) {
        if (rect != null) {
            rect.set(0, 0, i3, i4);
        }
    }

    private int measureSearchIcon(int i3) {
        if (!shouldLayoutOut(this.mSearchIconView)) {
            return i3;
        }
        int iMeasureView = i3 - measureView(this.mSearchIconView, View.MeasureSpec.makeMeasureSpec(this.mInnerIconSize, 1073741824), View.MeasureSpec.makeMeasureSpec(this.mInnerIconSize, 1073741824));
        return iMeasureView != i3 ? iMeasureView - getSearchIconStartGapForLayout() : iMeasureView;
    }

    private int measureView(View view, int i3, int i4) {
        view.measure(i3, i4);
        return view.getMeasuredWidth();
    }

    private void refreshMaterialSurfaceEffects() {
        if (this.mIsDrawableMaterialEffect) {
            applyNoBlurMaterialSurface();
        } else {
            applyFrameworkMaterialSurface();
        }
    }

    private void releaseNoBlurShadowEdge() {
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mNoBlurShadowEdge;
        if (cOUIShadowEdgeDrawable != null) {
            cOUIShadowEdgeDrawable.setCallback(null);
            this.mNoBlurShadowEdge = null;
        }
    }

    private boolean resolveShadowEdgeIsDarkMode() {
        return this.mStrokeEffectForceDark || COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(getContext());
    }

    private void setCornerRadius(float f3) {
        if (this.mBlurEffective) {
            if (this.mCornerParams == null) {
                this.mCornerParams = new OplusMaterialCornerParams(CORNER_RADIUS_PARAM, 1.0f);
            }
            this.mCornerParams.setRadius(f3);
            if (this.mIsDrawableMaterialEffect) {
                return;
            }
            COUIMaterialEffect.setCornerParams(this, this.mCornerParams);
        }
    }

    private boolean shouldLayoutOut(View view) {
        if (view == null) {
            return false;
        }
        if (view instanceof ImageView) {
            return (((ImageView) view).getDrawable() == null || view.getVisibility() == 8) ? false : true;
        }
        return view.getVisibility() != 8;
    }

    private void showButton(View view, boolean z2) {
        if (view == null) {
            return;
        }
        int i3 = z2 ? 0 : 8;
        if (view.getVisibility() == i3) {
            return;
        }
        view.setVisibility(i3);
        if ((this.mIsIconExpandClipping || this.mIsToolbarFadeAnimating) && view == this.mQuickDeleteButton && ViewCompat.isLaidOut(this) && getMeasuredWidth() > 0) {
            measureInsideBackground(getMeasuredWidth());
            layoutBackgroundArea();
        }
    }

    private void updateBackgroundBounds() {
        if (!isRtl()) {
            this.mBackgroundBounds.set(0.0f, 0.0f, this.mDrawingBounds.width(), this.mDrawingBounds.height());
        } else {
            this.mBackgroundBounds.set(getWidth() - this.mDrawingBounds.width(), getHeight() - this.mDrawingBounds.height(), getWidth(), getHeight());
        }
    }

    private void updateEditFrameClip(int i3) {
        View searchEditOrAnimationLayout = getSearchEditOrAnimationLayout();
        if (searchEditOrAnimationLayout == null || i3 <= 0) {
            return;
        }
        this.mCurrentAnimClipWidth = i3;
        int i4 = this.mAnimEditFrameMinWidth;
        if (i4 <= 0 || Math.abs(i3 - i4) >= 30) {
            int i5 = this.mAnimEditFrameMaxWidth;
            if (i5 > 0) {
                measureView(searchEditOrAnimationLayout, View.MeasureSpec.makeMeasureSpec(i5, 1073741824), View.MeasureSpec.makeMeasureSpec(this.mNormalBackgroundHeight, Integer.MIN_VALUE));
                layoutEditFrame(this.mAnimEditFrameMaxWidth);
            }
        } else {
            measureView(searchEditOrAnimationLayout, View.MeasureSpec.makeMeasureSpec(this.mAnimEditFrameMinWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(this.mNormalBackgroundHeight, Integer.MIN_VALUE));
            layoutEditFrame(this.mAnimEditFrameMinWidth);
        }
        int measuredWidth = searchEditOrAnimationLayout.getMeasuredWidth();
        int measuredHeight = searchEditOrAnimationLayout.getMeasuredHeight();
        if (measuredWidth <= 0 || measuredHeight <= 0) {
            return;
        }
        if (isRtl()) {
            this.mEditFrameClipRect.set(measuredWidth - i3, 0, measuredWidth, measuredHeight);
        } else {
            this.mEditFrameClipRect.set(0, 0, i3, measuredHeight);
        }
        searchEditOrAnimationLayout.setClipBounds(this.mEditFrameClipRect);
    }

    private void updateSmoothRoundPath() {
        float fHeight;
        this.mSmoothRoundPath.reset();
        if (this.mIsIconExpandClipping) {
            this.mBackgroundRect.set(Math.round(this.mIconExpandClipBounds.left), Math.round(this.mIconExpandClipBounds.top), Math.round(this.mIconExpandClipBounds.right), Math.round(this.mIconExpandClipBounds.bottom));
            this.mSmoothRoundRectF.set(this.mIconExpandClipBounds);
            fHeight = this.mIconExpandClipBounds.height();
        } else {
            this.mBackgroundRect.set(Math.round(this.mBackgroundBounds.left), Math.round(this.mBackgroundBounds.top), Math.round(this.mBackgroundBounds.right), Math.round(this.mBackgroundBounds.bottom));
            RectF rectF = this.mSmoothRoundRectF;
            RectF rectF2 = this.mBackgroundBounds;
            rectF.set(rectF2.left, rectF2.top, rectF2.right, rectF2.bottom);
            fHeight = this.mBackgroundBounds.height();
        }
        float f3 = fHeight / 2.0f;
        if (isSmoothOn()) {
            this.mBackgroundSmoothRoundCornerHelper.getPathAdapter().addSmoothRoundRect(this.mSmoothRoundRectF, f3, f3, 1.0f, Path.Direction.CCW);
        } else {
            this.mSmoothRoundPath.addRoundRect(this.mSmoothRoundRectF, f3, f3, Path.Direction.CW);
        }
        invalidateOutline();
        updateBallBounds(this, this.mBackgroundRect);
    }

    private Drawable zoomDrawable(Drawable drawable, int i3, int i4) {
        return new BitmapDrawable(Bitmap.createScaledBitmap(drawableToBitmap(drawable), i3, i4, true));
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        if (!this.mBlurEffective || this.mSmoothRoundPath.isEmpty()) {
            super.dispatchDraw(canvas);
        } else {
            canvas.clipPath(this.mSmoothRoundPath);
            super.dispatchDraw(canvas);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchHoverEvent(MotionEvent motionEvent) {
        if (this.mIsAnimating) {
            this.mMaskEffectDrawable.setHoverExited();
            return super.dispatchHoverEvent(motionEvent);
        }
        if (isEnabled() && motionEvent.getActionMasked() == 9) {
            this.mMaskEffectDrawable.setHoverEntered();
        }
        if (motionEvent.getActionMasked() == 10) {
            this.mMaskEffectDrawable.setHoverExited();
        }
        return super.dispatchHoverEvent(motionEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        float x2 = motionEvent.getX();
        float y2 = motionEvent.getY();
        int actionMasked = motionEvent.getActionMasked();
        if (!isEnabled()) {
            return super.dispatchTouchEvent(motionEvent);
        }
        if (actionMasked == 0) {
            this.mIsDragging = (!isInsideBackground(x2, y2) || isInsideView(this.mInnerPrimaryButton, x2, y2) || isInsideView(this.mInnerSecondaryButton, x2, y2) || isInsideFunctionalButton(x2, y2)) ? false : true;
        }
        if (this.mIsDragging) {
            requestDisallowInterceptTouchEvent(true);
            this.mTouchMotionAnimator.handleTouchEvent(motionEvent);
            this.mSpotLight.onTouchEvent(motionEvent);
            if (actionMasked == 1 || actionMasked == 3) {
                this.mIsDragging = false;
            }
        }
        if (actionMasked != 0 || isInsideBackground(x2, y2)) {
            return super.dispatchTouchEvent(motionEvent);
        }
        return false;
    }

    public void doAnimateEnd() {
        this.mIsAnimating = false;
        this.mAnimEditFrameMaxWidth = -1;
        this.mAnimEditFrameMinWidth = -1;
        clearEditFrameClip();
        requestLayout();
        if (shouldLayoutOut(this.mInnerPrimaryButton)) {
            this.mInnerPrimaryButton.setTranslationX(0.0f);
        }
        if (shouldLayoutOut(this.mInnerSecondaryButton)) {
            this.mInnerSecondaryButton.setTranslationX(0.0f);
        }
    }

    public void doBeforeAnimate(int i3, int i4, boolean z2, boolean z3) {
        int i5 = z2 ? i3 : i4;
        if (isRtl()) {
            RectF rectF = this.mDrawingBounds;
            rectF.left = rectF.right - i5;
        } else {
            RectF rectF2 = this.mDrawingBounds;
            rectF2.right = rectF2.left + i5;
        }
        int measuredWidth = (z2 ? i4 : i3) - getSearchEditOrAnimationLayout().getMeasuredWidth();
        int i6 = i3 - measuredWidth;
        this.mAnimEditFrameMaxWidth = i6;
        int i7 = i4 - measuredWidth;
        this.mAnimEditFrameMinWidth = i7;
        if (!z2) {
            i6 = i7;
        }
        updateEditFrameClip(i6);
        innerButtonSetX();
    }

    public void doWidthChangedAnimate(float f3, float f4, float f5) {
        this.mIsAnimating = true;
        float f6 = (f4 - f3) * f5;
        if (isRtl()) {
            RectF rectF = this.mDrawingBounds;
            rectF.left = (rectF.right - f3) - f6;
        } else {
            RectF rectF2 = this.mDrawingBounds;
            rectF2.right = rectF2.left + f3 + f6;
        }
        int i3 = this.mAnimEditFrameMaxWidth;
        if (i3 > 0) {
            updateEditFrameClip((int) (i3 + f6));
        }
        innerButtonSetX();
        invalidate();
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        updateBackgroundBounds();
        updateSmoothRoundPath();
        super.draw(canvas);
    }

    public void endIconExpandClipping() {
        this.mIsAnimating = false;
        this.mIsIconExpandClipping = false;
        invalidate();
    }

    public int getCurrentBackgroundColor() {
        return this.mCurrentBackgroundColor;
    }

    public COUIHintAnimationLayout getHintAnimationLayout() {
        if (this.mCOUIHintAnimationLayout == null) {
            this.mCOUIHintAnimationLayout = new COUIHintAnimationLayout(getContext());
            removeView(this.mSearchEditText);
            this.mCOUIHintAnimationLayout.setSearchEditText(this.mSearchEditText);
            addView(this.mCOUIHintAnimationLayout);
            ColorStateList colorStateList = this.mNormalHintColor;
            if (colorStateList != null) {
                this.mCOUIHintAnimationLayout.setHintTextColor(colorStateList);
            }
        }
        return this.mCOUIHintAnimationLayout;
    }

    public COUIHintAnimationLayout getHintLayoutMayBeNull() {
        return this.mCOUIHintAnimationLayout;
    }

    public ImageView getInnerPrimaryButton() {
        return this.mInnerPrimaryButton;
    }

    public ImageView getInnerSecondaryButton() {
        return this.mInnerSecondaryButton;
    }

    public ImageView getQuickDeleteButton() {
        return this.mQuickDeleteButton;
    }

    public View getSearchEditOrAnimationLayout() {
        COUIHintAnimationLayout cOUIHintAnimationLayout = this.mCOUIHintAnimationLayout;
        return cOUIHintAnimationLayout != null ? cOUIHintAnimationLayout : this.mSearchEditText;
    }

    public EditText getSearchEditText() {
        return this.mSearchEditText;
    }

    public ImageView getSearchIconView() {
        return this.mSearchIconView;
    }

    public void initDrawingBounds(int i3, int i4, int i5, int i6) {
        this.mDrawingBounds.set(i3, i4, i5, i6);
    }

    public void initFunctionButton(TextView textView) {
        this.mFunctionalButton = textView;
    }

    public void innerButtonSetX() {
        if (isRtl()) {
            float width = getWidth() - this.mDrawingBounds.width();
            if (!shouldLayoutOut(this.mInnerSecondaryButton)) {
                if (shouldLayoutOut(this.mInnerPrimaryButton)) {
                    this.mInnerPrimaryButton.setX(width + this.mNonInstantSearchInnerGap);
                    return;
                }
                return;
            } else {
                this.mInnerSecondaryButton.setX(this.mNonInstantSearchInnerGap + width);
                if (shouldLayoutOut(this.mInnerPrimaryButton)) {
                    this.mInnerPrimaryButton.setX(width + this.mNonInstantSearchInnerGap + this.mInnerSecondaryButton.getMeasuredWidth());
                    return;
                }
                return;
            }
        }
        if (!shouldLayoutOut(this.mInnerSecondaryButton)) {
            if (shouldLayoutOut(this.mInnerPrimaryButton)) {
                ImageView imageView = this.mInnerPrimaryButton;
                imageView.setX((this.mDrawingBounds.right - this.mNonInstantSearchInnerGap) - imageView.getMeasuredWidth());
                return;
            }
            return;
        }
        ImageView imageView2 = this.mInnerSecondaryButton;
        imageView2.setX((this.mDrawingBounds.right - this.mNonInstantSearchInnerGap) - imageView2.getMeasuredWidth());
        if (shouldLayoutOut(this.mInnerPrimaryButton)) {
            this.mInnerPrimaryButton.setX(((this.mDrawingBounds.right - this.mNonInstantSearchInnerGap) - this.mInnerSecondaryButton.getMeasuredWidth()) - this.mInnerPrimaryButton.getMeasuredWidth());
        }
    }

    public void loadAttr(Context context, AttributeSet attributeSet, int i3, int i4) {
        Resources resources = context.getResources();
        int i5 = R.dimen.coui_search_view_icon_size;
        this.mIconMaxWidth = resources.getDimensionPixelSize(i5);
        this.mIconMaxHeight = context.getResources().getDimensionPixelSize(i5);
        loadHideFunctionButtonAttr(context, attributeSet, i3, i4);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUISearchBar, i3, i4);
        int i6 = R.styleable.COUISearchBar_inputTextSize;
        if (typedArrayObtainStyledAttributes.hasValue(i6)) {
            int dimensionPixelSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(i6, 0);
            this.mSearchEditText.setTextSize(0, dimensionPixelSize);
            COUIHintAnimationLayout cOUIHintAnimationLayout = this.mCOUIHintAnimationLayout;
            if (cOUIHintAnimationLayout != null) {
                cOUIHintAnimationLayout.setTextSize(dimensionPixelSize);
            }
        }
        int i7 = R.styleable.COUISearchBar_inputTextColor;
        if (typedArrayObtainStyledAttributes.hasValue(i7)) {
            this.mSearchEditText.setTextColor(typedArrayObtainStyledAttributes.getColorStateList(i7));
        }
        int i8 = R.styleable.COUISearchBar_normalHintColor;
        if (typedArrayObtainStyledAttributes.hasValue(i8)) {
            ColorStateList colorStateList = typedArrayObtainStyledAttributes.getColorStateList(i8);
            this.mNormalHintColor = colorStateList;
            this.mSearchEditText.setHintTextColor(colorStateList);
            COUIHintAnimationLayout cOUIHintAnimationLayout2 = this.mCOUIHintAnimationLayout;
            if (cOUIHintAnimationLayout2 != null) {
                cOUIHintAnimationLayout2.setHintTextColor(colorStateList);
            }
        }
        int i9 = R.styleable.COUISearchBar_functionalButtonText;
        if (typedArrayObtainStyledAttributes.hasValue(i9) && functionalButtonShouldShow()) {
            this.mFunctionalButton.setText(typedArrayObtainStyledAttributes.getString(i9));
        }
        int i10 = R.styleable.COUISearchBar_functionalButtonTextColor;
        if (typedArrayObtainStyledAttributes.hasValue(i10) && functionalButtonShouldShow()) {
            this.mFunctionalButton.setTextColor(typedArrayObtainStyledAttributes.getColorStateList(i10));
        }
        Drawable drawable = ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.coui_search_view_icon, getContext().getTheme());
        int i11 = R.styleable.COUISearchBar_couiSearchIcon;
        if (typedArrayObtainStyledAttributes.hasValue(i11)) {
            drawable = typedArrayObtainStyledAttributes.getDrawable(i11);
        }
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int i12 = this.mIconMaxWidth;
        if (intrinsicWidth > i12) {
            drawable = zoomDrawable(drawable, (int) (i12 * getResources().getDisplayMetrics().density), (int) (this.mIconMaxHeight * getResources().getDisplayMetrics().density));
        }
        if (this.mSearchIconView == null) {
            ImageView imageViewEnsureImageView = ensureImageView(drawable, false, false, 0);
            this.mSearchIconView = imageViewEnsureImageView;
            imageViewEnsureImageView.setId(R.id.animated_search_icon);
            this.mSearchIconView.setImportantForAccessibility(2);
        }
        configImageViewDrawable(this.mSearchIconView, drawable, this.mInnerIconSize);
        int i13 = R.styleable.COUISearchBar_searchHint;
        if (typedArrayObtainStyledAttributes.hasValue(i13)) {
            this.mSearchEditText.setHint(typedArrayObtainStyledAttributes.getString(i13));
        }
        this.mClearTextDrawableResourceId = typedArrayObtainStyledAttributes.getResourceId(R.styleable.COUISearchBar_couiSearchClearSelector, R.drawable.ic_edit_text_delete_search_view);
        typedArrayObtainStyledAttributes.recycle();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mSpotLight.setCallback(this);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mSpotLight.setCallback(null);
        this.mSpotLight.cancel();
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!this.mDividerRect.isEmpty()) {
            this.mDividerPaint.setColor(((Integer) DEFAULT_EVALUATOR.evaluate(this.mDividerAlpha, 0, Integer.valueOf(this.mButtonDividerColor))).intValue());
            canvas.drawRect(this.mDividerRect, this.mDividerPaint);
        }
        if (this.mSpotLight.getMEnabled()) {
            canvas.save();
            canvas.clipPath(this.mSmoothRoundPath);
            this.mSpotLight.draw(canvas);
            canvas.restore();
        }
        if (this.mIsDrawableMaterialEffect) {
            drawNoBlurShadowEdge(canvas);
        }
    }

    @Override // android.view.View
    public void onFocusChanged(boolean z2, int i3, Rect rect) {
        super.onFocusChanged(z2, i3, rect);
        if (z2) {
            this.mMaskEffectDrawable.setFocusEntered();
        } else {
            this.mMaskEffectDrawable.setFocusExited();
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        float x2 = motionEvent.getX();
        float y2 = motionEvent.getY();
        if (isInsideFunctionalButton(x2, y2) || isInsideView(this.mInnerPrimaryButton, x2, y2) || isInsideView(this.mInnerSecondaryButton, x2, y2)) {
            return false;
        }
        if (this.mState == 0 && isInsideBackground(x2, y2)) {
            return true;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        layoutBackgroundArea();
    }

    @Override // android.view.View
    public void onMeasure(int i3, int i4) {
        setMeasuredDimension(View.MeasureSpec.getSize(i3), View.MeasureSpec.getSize(i4));
        measureInsideBackground(View.MeasureSpec.getSize(i3));
    }

    @Override // android.view.View
    public void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        this.mTouchMotionAnimator.onSizeChange(i3, i4);
        this.mSpotLight.setBounds(0, 0, i3, i4);
        setCornerRadius(getMeasuredHeight() / 2.0f);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        return true;
    }

    public void refresh() {
        ImageView imageView = this.mQuickDeleteButton;
        if (imageView != null) {
            imageView.setImageDrawable(ResourcesCompat.getDrawable(getContext().getResources(), this.mClearTextDrawableResourceId, getContext().getTheme()));
        }
        refreshMaterialSurfaceEffects();
    }

    public void setAddToToolbarAtBehind(boolean z2) {
        this.mAddToToolbarAtBehind = z2;
    }

    public final void setBackgroundColorTransparent(boolean z2) {
        if (this.mMaskEffectDrawable == null) {
            COUIMaskEffectDrawable cOUIMaskEffectDrawable = new COUIMaskEffectDrawable(getContext(), 1);
            this.mMaskEffectDrawable = cOUIMaskEffectDrawable;
            cOUIMaskEffectDrawable.setMaskPath(this.mSmoothRoundPath);
        }
        COUIMaskEffectDrawable cOUIMaskEffectDrawable2 = this.mMaskEffectDrawable;
        Drawable[] drawableArr = {cOUIMaskEffectDrawable2};
        if (!z2) {
            drawableArr = new Drawable[]{this.mBackgroundColor, cOUIMaskEffectDrawable2};
        }
        setBackground(new COUIStateEffectDrawable(drawableArr));
    }

    public final void setBlurEffectEnable(boolean z2) {
        this.mBlurEffective = z2;
        refreshMaterialSurfaceEffects();
    }

    public void setButtonDividerColor(int i3) {
        this.mButtonDividerColor = i3;
        invalidate();
    }

    public final void setCrossWindowBlurEffect(boolean z2, boolean z3, int i3) {
        if (this.mIsDrawableMaterialEffect == z2 && this.mStrokeEffectForceDark == z3 && this.mNoBlurSolidColor == i3) {
            return;
        }
        this.mIsDrawableMaterialEffect = z2;
        if (!z2) {
            this.mBackgroundColor.setColor(this.mCurrentBackgroundColor);
            releaseNoBlurShadowEdge();
        }
        this.mStrokeEffectForceDark = z3;
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mNoBlurShadowEdge;
        if (cOUIShadowEdgeDrawable != null) {
            cOUIShadowEdgeDrawable.setIsDarkMode(resolveShadowEdgeIsDarkMode());
        }
        this.mNoBlurSolidColor = i3;
        if (this.mIsDrawableMaterialEffect) {
            this.mBackgroundColor.setColor(i3);
            refreshMaterialSurfaceEffects();
        }
    }

    public void setCurrentBackgroundColor(int i3) {
        this.mCurrentBackgroundColor = i3;
        this.mBackgroundColor.setColor(i3);
        invalidate();
    }

    public void setDividerAlpha(float f3) {
        this.mDividerAlpha = f3;
        invalidate();
    }

    @Override // android.view.View
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        if (getSearchIconView() != null) {
            getSearchIconView().setEnabled(z2);
        }
        if (getQuickDeleteButton() != null) {
            getQuickDeleteButton().setEnabled(z2);
        }
        if (getInnerPrimaryButton() != null) {
            getInnerPrimaryButton().setEnabled(z2);
        }
        if (getInnerSecondaryButton() != null) {
            getInnerSecondaryButton().setEnabled(z2);
        }
        EditText editText = this.mSearchEditText;
        if (editText != null) {
            editText.setEnabled(z2);
        }
        COUIHintAnimationLayout cOUIHintAnimationLayout = this.mCOUIHintAnimationLayout;
        if (cOUIHintAnimationLayout != null) {
            cOUIHintAnimationLayout.setEnabled(z2);
        }
        setSpotLightEnabled(this.mSpotlightEnabled);
    }

    public void setIconExpandClip(boolean z2) {
        this.mIsIconExpandClipping = z2;
    }

    public void setInnerPrimaryButton(ImageView imageView) {
        this.mInnerPrimaryButton = imageView;
    }

    public void setInnerSecondaryButton(ImageView imageView) {
        this.mInnerSecondaryButton = imageView;
    }

    public final void setMaterialEffectType(int i3) {
        if (this.mMaterialType == i3) {
            return;
        }
        this.mMaterialType = i3;
        setCurrentBackgroundColor(ToolbarMaterialEffectResolver.getToolbarBackgroundColor(getContext(), this.mMaterialType));
        refreshMaterialSurfaceEffects();
    }

    public final void setNoBlurStrokeProgress(float f3) {
        this.mNoBlurStrokeProgress = Math.max(0.0f, Math.min(f3, 1.0f));
        if (this.mIsDrawableMaterialEffect && this.mStrokeEnabled) {
            applyNoBlurShadowInterpolated();
            invalidate();
        }
    }

    public void setSearchAnimateType(int i3) {
        this.mSearchViewType = i3;
    }

    public final void setSpotLightEffectType(COUISpotLightEffect.SpotLightType spotLightType, Context context) {
        if (spotLightType == null) {
            this.mSpotLightType = COUISpotLightEffect.SpotLightType.TYPE_OPAQUE_MEDIUM_2;
        } else {
            this.mSpotLightType = spotLightType;
        }
        if (context == null) {
            context = getContext();
        }
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLight;
        if (cOUISpotLightEffectDrawable == null || context == null) {
            return;
        }
        cOUISpotLightEffectDrawable.setSpotLightType(context, this.mSpotLightType);
    }

    public void setSpotLightEnabled(boolean z2) {
        this.mSpotlightEnabled = z2;
        this.mSpotLight.setEnabled(z2 && isEnabled());
    }

    public void setState(int i3) {
        this.mState = i3;
    }

    public final void setStrokeEffectEnable(boolean z2) {
        this.mStrokeEnabled = z2;
        refreshMaterialSurfaceEffects();
    }

    public final void setStrokeEffectType(COUIMaterialStrokeEffect.StrokeEffectType strokeEffectType, Context context) {
        if (strokeEffectType == null) {
            this.mStrokeType = ToolbarMaterialEffectResolver.getCapsuleStrokeEffectTypeInWindow(this.mMaterialType);
        } else {
            this.mStrokeType = strokeEffectType;
        }
        if (context == null) {
            context = getContext();
        }
        if (this.mNoBlurShadowEdge == null || context == null) {
            return;
        }
        COUIMaterialStrokeEffect.Companion.StrokeEffectParams strokeParamsByType = COUIMaterialEffect.getStrokeParamsByType(context, this.mStrokeType);
        float f3 = this.mNoBlurStrokeProgress;
        this.mNoBlurShadowEdge.setLineAlpha(strokeParamsByType.getEdgeAlpha() * f3);
        this.mNoBlurShadowEdge.setLineWidth(strokeParamsByType.getEdgeWidth(context) * f3);
        this.mNoBlurShadowEdge.setLineAngle(strokeParamsByType.getEdgeAngle());
        this.mNoBlurShadowEdge.setFadeAlpha(strokeParamsByType.getShadowFadeIn() * f3, strokeParamsByType.getShadowFadeOut() * f3);
        this.mNoBlurShadowEdge.setFadeWidthScale(strokeParamsByType.getShadowFadeScale() * f3);
        this.mNoBlurShadowEdge.setDrawableEnabled(true);
    }

    public void setToolbarFadeAnimating(boolean z2) {
        this.mIsToolbarFadeAnimating = z2;
    }

    public void setTouchAnimationEnable(boolean z2) {
        this.mTouchMotionAnimator.setDeformationEnable(z2).setRubberEnable(z2).setPressScaleEnable(z2);
    }

    public void setTouchAnimationReset(boolean z2) {
        this.mTouchMotionAnimator.reset(z2);
    }

    public void startIconExpandClipping(float f3) {
        this.mIsIconExpandClipping = true;
        if (isRtl()) {
            float fMax = Math.max(getWidth(), f3);
            this.mIconExpandClipBounds.set(fMax - f3, 0.0f, fMax, f3);
        } else {
            this.mIconExpandClipBounds.set(0.0f, 0.0f, f3, f3);
        }
        invalidate();
    }

    public void updateBallBounds(View view, Rect rect) {
        if (this.mBlurEffective) {
            if (this.mMaterialBaseParams == null) {
                this.mMaterialBaseParams = new OplusMaterialBaseParams(0, (Rect) null);
            }
            this.mMaterialBaseParams.setInnerBounds(rect);
            if (this.mIsDrawableMaterialEffect) {
                return;
            }
            COUIMaterialEffect.setBaseParams(view, this.mMaterialBaseParams);
        }
    }

    public void updateIconExpandClippingWidth(float f3, float f4, float f5) {
        this.mIsAnimating = true;
        if (isRtl()) {
            float fMax = Math.max(f4, getWidth());
            RectF rectF = this.mIconExpandClipBounds;
            rectF.left = fMax - f5;
            rectF.right = fMax;
        } else {
            RectF rectF2 = this.mIconExpandClipBounds;
            rectF2.left = 0.0f;
            rectF2.right = f5;
        }
        invalidate();
    }

    public SearchBarBackgroundView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SearchBarBackgroundView(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0);
    }

    public SearchBarBackgroundView(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mDividerPaint = new Paint(1);
        this.mIconExpandClipBounds = new RectF();
        Path path = new Path();
        this.mSmoothRoundPath = path;
        this.mSmoothRoundRectF = new RectF();
        this.mEditFrameClipRect = new Rect();
        this.mSpotLight = new COUISpotLightEffectDrawable();
        ColorDrawable colorDrawable = new ColorDrawable(COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorBar));
        this.mBackgroundColor = colorDrawable;
        this.mDrawingBounds = new RectF();
        this.mBackgroundBounds = new RectF();
        this.mBackgroundRect = new Rect();
        this.mDividerRect = new Rect();
        this.mCornerParams = null;
        this.mMaterialBaseParams = null;
        this.mSearchViewType = 0;
        this.mClearTextDrawableResourceId = -1;
        this.mHideFunctionalButton = false;
        this.mDividerAlpha = 0.0f;
        this.mIsIconExpandClipping = false;
        this.mIsToolbarFadeAnimating = false;
        this.mAnimEditFrameMaxWidth = -1;
        this.mAnimEditFrameMinWidth = -1;
        this.mCurrentAnimClipWidth = -1;
        this.mIsDrawableMaterialEffect = false;
        this.mNoBlurSolidColor = 0;
        this.mBlurEffective = false;
        this.mStrokeEnabled = false;
        this.mNoBlurStrokeProgress = 1.0f;
        this.mSpotlightEnabled = false;
        this.mMaterialType = 0;
        this.mState = 0;
        this.mIsDragging = false;
        this.mStrokeType = null;
        this.mSpotLightType = null;
        init(context);
        initEdittext();
        setWillNotDraw(false);
        setOutlineProvider(new AnonymousClass1());
        setClipToOutline(!this.mBlurEffective);
        UIUtil.setViewShadowClippingEnabled(this, true);
        if (COUIMaterialStateManager.INSTANCE.materialVersionCheck()) {
            this.mCurrentBackgroundColor = ToolbarMaterialEffectResolver.getToolbarBackgroundColor(context, this.mMaterialType);
        } else {
            this.mCurrentBackgroundColor = ResourcesCompat.getColor(getContext().getResources(), R.color.coui_search_view_selector_color_normal, getContext().getTheme());
        }
        colorDrawable.setColor(this.mCurrentBackgroundColor);
        initTouchAnimation();
        setSpotLightEffectType(this.mSpotLightType, context);
        setFocusable(true);
        setClickable(true);
        this.mBackgroundSmoothRoundCornerHelper = new COUISearchBar.SmoothRoundCornerHelper(path);
    }

    private void layoutEditFrame(int i3) {
        Rect rect = this.mBackgroundRect;
        int iCalculateRelativeTop = calculateRelativeTop(rect.top, rect.height(), getSearchEditOrAnimationLayout().getMeasuredHeight());
        if (isRtl()) {
            int left = shouldLayoutOut(this.mSearchIconView) ? this.mSearchIconView.getLeft() : this.mBackgroundRect.right;
            getSearchEditOrAnimationLayout().layout(left - i3, iCalculateRelativeTop, left, getSearchEditOrAnimationLayout().getMeasuredHeight() + iCalculateRelativeTop);
        } else {
            int right = shouldLayoutOut(this.mSearchIconView) ? this.mSearchIconView.getRight() : this.mBackgroundRect.left;
            getSearchEditOrAnimationLayout().layout(right, iCalculateRelativeTop, i3 + right, getSearchEditOrAnimationLayout().getMeasuredHeight() + iCalculateRelativeTop);
        }
    }
}