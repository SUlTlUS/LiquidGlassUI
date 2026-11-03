package com.coui.appcompat.snackbar;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.PathInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import com.coui.appcompat.animation.COUIMoveEaseInterpolator;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.button.COUIButton;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.coui.appcompat.uiutil.ShadowUtils;
import com.oplus.graphics.OplusOutline;
import com.support.appcompat.R;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes7.dex */
public class COUINotificationSnackBar extends COUISnackBar {
    private static final int MAX_DURATION = 10000;
    private static final int MIN_DURATION = 4000;
    private static final PathInterpolator MOVE_EASE_INTERPOLATOR = new COUIMoveEaseInterpolator();
    private static final int SLIDING_LIMIT_VALUE = 8;
    private static final float SLIDING_SPEED = 1.75f;
    private static final int SWIPED_INDEX_X = 1;
    private static final int SWIPED_INDEX_Y = 2;
    private static final String TAG = "COUINotificationSnackBar";
    private View.OnClickListener mButtonClickListener;
    private COUIButton mButtonTv;
    private Drawable mCloseDrawable;
    private ImageView mCloseIv;
    private View.OnClickListener mCloseIvClickListener;
    private COUISpringAnimation mCouiSpringAnimationX;
    private COUISpringAnimation mCouiSpringAnimationY;
    private int mHorizontalImageHeight;
    private int mHorizontalImageWidth;
    private int mInterceptX;
    private int mInterceptY;
    private boolean mIsSupportSmoothRoundCorner;
    private int mLastX;
    private int mLastY;
    private ImageView mNotificationIcon;
    private int mScreenWidth;
    private int mSquareImageWidth;
    private TextView mSubContentTv;
    private int mSwipedIndex;
    private int mVerticalImageHeight;
    private int mVerticalImageWidth;

    /* JADX INFO: renamed from: com.coui.appcompat.snackbar.COUINotificationSnackBar$1, reason: invalid class name */
    public class AnonymousClass1 extends ViewOutlineProvider {
        final /* synthetic */ Context val$context;

        public AnonymousClass1(Context context) {
            this.val$context = context;
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            if (!COUINotificationSnackBar.access$000(COUINotificationSnackBar.this)) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), COUIContextUtil.getAttrDimens(this.val$context, R.attr.couiRoundCornerL));
            } else {
                new OplusOutline(outline).setSmoothRoundRect(0, 0, view.getWidth(), view.getHeight(), COUIContextUtil.getAttrDimens(this.val$context, R.attr.couiRoundCornerLRadius), COUIContextUtil.getAttrFloat(this.val$context, R.attr.couiRoundCornerLWeight));
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.snackbar.COUINotificationSnackBar$2, reason: invalid class name */
    public class AnonymousClass2 implements Animator.AnimatorListener {
        public AnonymousClass2() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUINotificationSnackBar.this.dismiss();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }
    }

    @Target({ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ImageType {
        public static final int Horizontal = 2;
        public static final int Square = 0;
        public static final int Vertical = 1;
    }

    public COUINotificationSnackBar(Context context) {
        super(context);
    }

    public static /* synthetic */ void a(COUINotificationSnackBar cOUINotificationSnackBar, View view) {
        View.OnClickListener onClickListener = cOUINotificationSnackBar.mButtonClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(cOUINotificationSnackBar.mButtonTv);
        }
    }

    public static /* synthetic */ boolean access$000(COUINotificationSnackBar cOUINotificationSnackBar) {
        return cOUINotificationSnackBar.mIsSupportSmoothRoundCorner;
    }

    public static /* synthetic */ void b(COUINotificationSnackBar cOUINotificationSnackBar, View view) {
        View.OnClickListener onClickListener = cOUINotificationSnackBar.mCloseIvClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(cOUINotificationSnackBar.mCloseIv);
        }
        cOUINotificationSnackBar.dismiss();
    }

    private boolean checkSwipedFarEnoughByX() {
        return Math.abs(getTranslationX()) > ((float) (getMeasuredWidth() / 8));
    }

    private boolean checkSwipedFarEnoughByY() {
        float translationY = getTranslationY();
        return Math.abs(translationY) > ((float) (getMeasuredWidth() / 8)) && translationY > 0.0f;
    }

    private ObjectAnimator createTranslationAnimation(View view, float f3, Property<View, Float> property) {
        return ObjectAnimator.ofFloat(view, property, f3);
    }

    private COUISpringAnimation getViewSpringTranslationAnimator(View view, float f3, COUIDynamicAnimation.ViewProperty viewProperty) {
        COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(view, viewProperty, f3);
        cOUISpringAnimation.getSpring().setBounce(0.0f);
        cOUISpringAnimation.getSpring().setResponse(0.35f);
        return cOUISpringAnimation;
    }

    private Animator getViewTranslationAnimator(View view, float f3, int i3, Property<View, Float> property) {
        ObjectAnimator objectAnimatorCreateTranslationAnimation = createTranslationAnimation(view, f3, property);
        objectAnimatorCreateTranslationAnimation.addListener(new AnonymousClass2());
        objectAnimatorCreateTranslationAnimation.setDuration(i3);
        objectAnimatorCreateTranslationAnimation.setInterpolator(MOVE_EASE_INTERPOLATOR);
        return objectAnimatorCreateTranslationAnimation;
    }

    private void initResource() {
        this.mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        this.mSquareImageWidth = getContext().getResources().getDimensionPixelSize(com.support.snackbar.R.dimen.coui_notification_snack_bar_icon_width);
        this.mHorizontalImageWidth = getContext().getResources().getDimensionPixelSize(com.support.snackbar.R.dimen.coui_notification_snack_bar_horizontal_icon_width);
        this.mHorizontalImageHeight = getContext().getResources().getDimensionPixelSize(com.support.snackbar.R.dimen.coui_notification_snack_bar_horizontal_icon_height);
        this.mVerticalImageWidth = getContext().getResources().getDimensionPixelSize(com.support.snackbar.R.dimen.coui_notification_snack_bar_vertical_icon_width);
        this.mVerticalImageHeight = getContext().getResources().getDimensionPixelSize(com.support.snackbar.R.dimen.coui_notification_snack_bar_vertical_icon_height);
        Drawable drawable = getContext().getResources().getDrawable(com.support.snackbar.R.drawable.coui_menu_ic_cancel_normal, getContext().getTheme());
        this.mCloseDrawable = drawable;
        drawable.setColorFilter(COUIContextUtil.getAttrColor(getContext(), R.attr.couiColorLabelTertiary, 0), PorterDuff.Mode.SRC_IN);
    }

    public static COUINotificationSnackBar make(View view, String str, String str2, int i3) {
        return make(view.getContext(), view, str, str2, i3);
    }

    private void setButtonText(String str) {
        if (this.mButtonTv == null || TextUtils.isEmpty(str)) {
            return;
        }
        this.mButtonTv.setText(str);
        this.mButtonTv.setVisibility(0);
    }

    private void setCloseIcon() {
        ImageView imageView = this.mCloseIv;
        if (imageView == null) {
            return;
        }
        imageView.setImageDrawable(this.mCloseDrawable);
        this.mCloseIv.setVisibility(0);
    }

    private void setOnClick() {
        ImageView imageView = this.mCloseIv;
        if (imageView != null) {
            imageView.setOnClickListener(new a(this));
        }
        COUIButton cOUIButton = this.mButtonTv;
        if (cOUIButton != null) {
            cOUIButton.setOnClickListener(new b(this));
        }
    }

    private void startDismissAnimationX() {
        float translationX = getTranslationX();
        int measuredWidth = ((this.mScreenWidth - getMeasuredWidth()) / 2) + getMeasuredWidth();
        float f3 = measuredWidth;
        int iAbs = (int) ((f3 - Math.abs(translationX)) / SLIDING_SPEED);
        if (translationX < 0.0f) {
            f3 = -measuredWidth;
        }
        ((ObjectAnimator) getViewTranslationAnimator(this, f3, iAbs, View.TRANSLATION_X)).start();
    }

    private void startDismissAnimationY() {
        float bottom = getBottom() + getMeasuredHeight();
        ((ObjectAnimator) getViewTranslationAnimator(this, bottom, (int) (bottom / SLIDING_SPEED), View.TRANSLATION_Y)).start();
    }

    private void startOriginalAnimationX() {
        if (this.mCouiSpringAnimationX == null) {
            this.mCouiSpringAnimationX = getViewSpringTranslationAnimator(this, 0.0f, COUIDynamicAnimation.TRANSLATION_X);
        }
        this.mCouiSpringAnimationX.start();
    }

    private void startOriginalAnimationY() {
        if (this.mCouiSpringAnimationY == null) {
            this.mCouiSpringAnimationY = getViewSpringTranslationAnimator(this, 0.0f, COUIDynamicAnimation.TRANSLATION_Y);
        }
        this.mCouiSpringAnimationY.start();
    }

    public COUIButton getNotificationButton() {
        return this.mButtonTv;
    }

    public ViewGroup getSnackBarLayout() {
        return this.mSnackBarLayout;
    }

    public TextView getSubContentView() {
        return this.mSubContentTv;
    }

    @Override // com.coui.appcompat.snackbar.COUISnackBar
    public void initCOUISnackBar(Context context, AttributeSet attributeSet) {
        initResource();
        View viewInflate = View.inflate(context, com.support.snackbar.R.layout.coui_notification_snack_bar_item, this);
        this.mRootView = viewInflate;
        this.mSnackBarLayout = (ViewGroup) viewInflate.findViewById(com.support.snackbar.R.id.snack_bar);
        this.mContentView = (TextView) this.mRootView.findViewById(com.support.snackbar.R.id.tv_snack_bar_content);
        this.mActionView = (TextView) this.mRootView.findViewById(com.support.snackbar.R.id.tv_snack_bar_action);
        this.mIconDrawableView = (ImageView) this.mRootView.findViewById(com.support.snackbar.R.id.iv_snack_bar_icon);
        this.mCloseIv = (ImageView) this.mRootView.findViewById(com.support.snackbar.R.id.iv_notification_snack_bar_close);
        this.mButtonTv = (COUIButton) this.mRootView.findViewById(com.support.snackbar.R.id.bt_notification_snack_bar);
        this.mNotificationIcon = (ImageView) this.mRootView.findViewById(com.support.snackbar.R.id.iv_notification_snack_bar_icon);
        this.mSubContentTv = (TextView) this.mRootView.findViewById(com.support.snackbar.R.id.tv_snack_bar_sub_content);
        COUISnackBar.mCOUISnackBarBottomMargin = new ViewGroup.MarginLayoutParams(context, attributeSet).bottomMargin;
        setVisibility(8);
        this.mAutoDismissRunnable = new COUISnackBar.AutoDismissRunnable();
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, com.support.snackbar.R.styleable.COUISnackBar, 0, 0);
        try {
            try {
                int i3 = com.support.snackbar.R.styleable.COUISnackBar_defaultSnackBarContentText;
                if (typedArrayObtainStyledAttributes.getString(i3) != null) {
                    setContentText(typedArrayObtainStyledAttributes.getString(i3));
                    setDuration(typedArrayObtainStyledAttributes.getInt(com.support.snackbar.R.styleable.COUISnackBar_snackBarDisappearTime, 0));
                }
            } catch (Exception e3) {
                Log.e(TAG, "Failure setting COUINotificationSnackBar " + e3.getMessage());
            }
            typedArrayObtainStyledAttributes.recycle();
            this.mIsSupportSmoothRoundCorner = RoundCornerUtil.isVersionSupport();
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(context);
            setOnClick();
            this.mSnackBarLayout.setOutlineProvider(anonymousClass1);
            this.mSnackBarLayout.setClipToOutline(true);
            ShadowUtils.setElevationToView(this.mSnackBarLayout, 2, COUIContextUtil.getAttrDimens(context, R.attr.couiRoundCornerL), context.getResources().getDimensionPixelOffset(R.dimen.support_shadow_size_level_four), getContext().getResources().getColor(com.support.snackbar.R.color.coui_snack_bar_background_shadow_color));
        } catch (Throwable th) {
            typedArrayObtainStyledAttributes.recycle();
            throw th;
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int rawY = (int) motionEvent.getRawY();
        int rawX = (int) motionEvent.getRawX();
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mInterceptX = rawX;
            this.mInterceptY = rawY;
            return false;
        }
        if (action != 2) {
            return false;
        }
        this.mLastX = rawX;
        this.mLastY = rawY;
        if (Math.abs(rawX - this.mInterceptX) <= 0 && Math.abs(this.mLastY - this.mInterceptY) <= 0) {
            return false;
        }
        this.mSwipedIndex = -1;
        return true;
    }

    @Override // com.coui.appcompat.snackbar.COUISnackBar, android.widget.RelativeLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        this.isAdjustLayout = false;
        super.onLayout(z2, i3, i4, i5, i6);
    }

    /* JADX WARN: Code duplicated, block: B:27:0x0063  */
    /* JADX WARN: Code duplicated, block: B:29:0x0067  */
    /* JADX WARN: Code duplicated, block: B:31:0x006d  */
    /* JADX WARN: Code duplicated, block: B:32:0x0071  */
    /* JADX WARN: Code duplicated, block: B:33:0x0075  */
    /* JADX WARN: Code duplicated, block: B:35:0x007b  */
    /* JADX WARN: Code duplicated, block: B:36:0x007f  */
    @Override // com.coui.appcompat.snackbar.COUISnackBar, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int rawX = (int) motionEvent.getRawX();
        int rawY = (int) motionEvent.getRawY();
        int i3 = rawX - this.mLastX;
        int i4 = rawY - this.mLastY;
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mSwipedIndex = -1;
        } else if (action != 1) {
            if (action == 2) {
                if (this.mSwipedIndex == -1 && (i3 != 0 || i4 != 0)) {
                    this.mSwipedIndex = Math.abs(i3) > Math.abs(i4) ? 1 : 2;
                }
                int i5 = this.mSwipedIndex;
                if (i5 != -1) {
                    if (i5 == 1) {
                        setTranslationX(getTranslationX() + i3);
                    } else {
                        float translationY = getTranslationY() + i4;
                        int measuredWidth = getMeasuredWidth() / 8;
                        if (translationY < 0.0f) {
                            translationY = Math.max(-measuredWidth, translationY);
                        }
                        setTranslationY(translationY);
                    }
                }
            } else if (action == 3) {
                if (this.mSwipedIndex == 1) {
                    if (checkSwipedFarEnoughByX()) {
                        startDismissAnimationX();
                    } else {
                        startOriginalAnimationX();
                    }
                } else if (checkSwipedFarEnoughByY()) {
                    startDismissAnimationY();
                } else {
                    startOriginalAnimationY();
                }
            }
        } else if (this.mSwipedIndex == 1) {
            if (checkSwipedFarEnoughByX()) {
                startDismissAnimationX();
            } else {
                startOriginalAnimationX();
            }
        } else if (checkSwipedFarEnoughByY()) {
            startDismissAnimationY();
        } else {
            startOriginalAnimationY();
        }
        this.mLastX = rawX;
        this.mLastY = rawY;
        return super.onTouchEvent(motionEvent);
    }

    public void setButtonClickListener(View.OnClickListener onClickListener) {
        this.mButtonClickListener = onClickListener;
    }

    public void setCloseButtonClickListener(View.OnClickListener onClickListener) {
        this.mCloseIvClickListener = onClickListener;
    }

    public void setNotificationIcon(Drawable drawable, int i3) {
        ImageView imageView = this.mNotificationIcon;
        if (imageView == null || drawable == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        if (i3 == 0) {
            int i4 = this.mSquareImageWidth;
            layoutParams.width = i4;
            layoutParams.height = i4;
        } else if (i3 == 1) {
            layoutParams.width = this.mVerticalImageWidth;
            layoutParams.height = this.mVerticalImageHeight;
        } else if (i3 == 2) {
            layoutParams.width = this.mHorizontalImageWidth;
            layoutParams.height = this.mHorizontalImageHeight;
        }
        this.mNotificationIcon.setLayoutParams(layoutParams);
        this.mNotificationIcon.setImageDrawable(drawable);
        this.mNotificationIcon.setVisibility(0);
    }

    public void setSubContent(String str) {
        if (this.mSubContentTv == null || TextUtils.isEmpty(str)) {
            return;
        }
        this.mSubContentTv.setText(str);
        this.mSubContentTv.setVisibility(0);
    }

    public COUINotificationSnackBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public static COUINotificationSnackBar make(Context context, View view, String str, String str2, int i3) {
        return make(context, view, str, str2, i3, context.getResources().getDimensionPixelSize(com.support.snackbar.R.dimen.coui_snack_bar_margin_bottom));
    }

    public static COUINotificationSnackBar make(View view, String str, String str2, int i3, int i4) {
        return make(view.getContext(), view, str, str2, i3, i4);
    }

    public static COUINotificationSnackBar make(Context context, View view, String str, String str2, int i3, int i4) {
        ViewGroup viewGroupFindSuitableParent = COUISnackBar.findSuitableParent(view);
        if (viewGroupFindSuitableParent != null) {
            if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
                int iMin = Math.min(Math.max(i3, MIN_DURATION), 10000);
                TypedValue typedValue = new TypedValue();
                if (!context.getTheme().resolveAttribute(R.attr.couiColorBackgroundTop, typedValue, true) || !context.getTheme().resolveAttribute(R.attr.couiColorPrimaryNeutral, typedValue, true)) {
                    context = new ContextThemeWrapper(context, R.style.Theme_COUI_Main);
                }
                COUINotificationSnackBar cOUINotificationSnackBar = (COUINotificationSnackBar) LayoutInflater.from(context).inflate(com.support.snackbar.R.layout.coui_notification_snack_bar_show_layout, viewGroupFindSuitableParent, false);
                cOUINotificationSnackBar.setContentText(str);
                cOUINotificationSnackBar.setDuration(iMin);
                cOUINotificationSnackBar.setParent(viewGroupFindSuitableParent);
                cOUINotificationSnackBar.setButtonText(str2);
                cOUINotificationSnackBar.setCloseIcon();
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) cOUINotificationSnackBar.getLayoutParams();
                marginLayoutParams.bottomMargin = i4;
                cOUINotificationSnackBar.setTranslationY(cOUINotificationSnackBar.getHeight() + i4);
                boolean z2 = false;
                for (int i5 = 0; i5 < viewGroupFindSuitableParent.getChildCount(); i5++) {
                    if (viewGroupFindSuitableParent.getChildAt(i5) instanceof COUISnackBar) {
                        z2 = viewGroupFindSuitableParent.getChildAt(i5).getVisibility() != 8;
                    }
                }
                if (!z2) {
                    viewGroupFindSuitableParent.addView(cOUINotificationSnackBar, marginLayoutParams);
                }
                return cOUINotificationSnackBar;
            }
            throw new IllegalArgumentException("Content text and button text can not be empty");
        }
        throw new IllegalArgumentException("No suitable parent found from the given view. Please provide a valid view.");
    }

    public void setNotificationIcon(int i3, int i4) {
        setNotificationIcon(getResources().getDrawable(i3, getContext().getTheme()), i4);
    }
}