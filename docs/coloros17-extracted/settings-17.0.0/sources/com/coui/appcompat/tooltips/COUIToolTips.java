package com.coui.appcompat.tooltips;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewParent;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.poplist.COUIPopupWindow;
import com.coui.appcompat.poplist.WindowSpacingControlHelper;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.coui.appcompat.uiutil.ShadowUtils;
import com.coui.appcompat.uiutil.UIUtil;
import com.coui.appcompat.version.COUIVersionUtil;
import com.oplus.graphics.OplusOutline;
import com.oplus.graphics.OplusOutlineAdapter;
import com.support.appcompat.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIToolTips extends COUIPopupWindow {
    public static final int ALIGN_BOTTOM = 128;
    public static final int ALIGN_END = 64;
    public static final int ALIGN_LEFT = 16;
    public static final int ALIGN_RIGHT = 8;
    public static final int ALIGN_START = 32;
    public static final int ALIGN_TOP = 256;
    public static final int ANIMATION_DURATION = 300;
    public static final int ANIMATION_EXIT_DURATION = 20;
    private static final int ARROW_DOWN = 8;
    private static final int ARROW_DOWN_LEFT = 9;
    private static final int ARROW_DOWN_RIGHT = 7;
    private static final int ARROW_LEFT = 11;
    private static final int ARROW_LEFT_DOWN = 10;
    private static final int ARROW_LEFT_UP = 12;
    private static final int ARROW_RIGHT = 5;
    private static final int ARROW_RIGHT_DOWN = 6;
    private static final int ARROW_RIGHT_UP = 4;
    private static final int ARROW_UP = 2;
    private static final int ARROW_UP_LEFT = 1;
    private static final int ARROW_UP_RIGHT = 3;
    public static final int DEFAULT_ALIGN_DIRECTION = 4;
    private static final float DEFAULT_SPRING_FACTOR = 10000.0f;
    private static final float DEFAULT_TOOLTIPS_ALPHA_SPRING_RESPONSE = 0.3f;
    private static final float DEFAULT_TOOLTIPS_SCALE_SPRING_RESPONSE = 0.4f;
    private static final float DEFAULT_TOOLTIPS_SPRING_BOUNCE = 0.2f;
    public static final int MODE_INFO = 1;
    public static final int MODE_TOOLTIPS = 0;
    private static final float ONE = 1.0f;
    private static final float OS_16_1_WEIGHT = 3.0f;
    private static final float POINT_FIVE = 0.5f;
    private static final float ROTATION_ANGLE_0 = 0.0f;
    private static final float ROTATION_ANGLE_180 = 180.0f;
    private static final float ROTATION_ANGLE_270 = 270.0f;
    private static final float ROTATION_ANGLE_90 = 90.0f;
    private static final String TAG = "COUIToolTips";
    private static final float ZERO = 0.0f;
    private IToolTipsAction mAction;
    private View mAnchor;
    private int mArrowDirection;
    private Drawable mArrowDownDrawable;
    private Drawable mArrowDownLeftDrawable;
    private Drawable mArrowDownRightDrawable;
    private Drawable mArrowLeftDownDrawable;
    private Drawable mArrowLeftDrawable;
    private Drawable mArrowLeftUpDrawable;
    private int mArrowOverflow;
    private Drawable mArrowRightDownDrawable;
    private Drawable mArrowRightDrawable;
    private Drawable mArrowRightUpDrawable;
    private int mArrowType;
    private Drawable mArrowUpDrawable;
    private Drawable mArrowUpLeftDrawable;
    private Drawable mArrowUpRightDrawable;
    private ImageView mArrowView;
    private int mBubbleHorizontalPadding;
    private COUIIBubbleStyle mBubbleStyle;
    private Runnable mCheckAnimateExit;
    private ViewGroup mContentContainer;
    private Rect mContentRectOnScreen;
    private final Context mContext;
    private final Point mCoordsOnWindow;
    private int mEdgeExtraDistance;
    private boolean mHasIndicator;
    private boolean mIfNeedResetTouchableToTrue;
    private int mIndicatorEmbedDistance;
    private boolean mIndicatorFollowContent;
    private boolean mIsDismissing;
    private boolean mIsEntering;
    private ViewGroup mMainPanel;
    private int mMode;
    private int mOffsetX;
    private int mOffsetY;
    private OnCloseIconClickListener mOnCloseIconClickListener;
    private View.OnLayoutChangeListener mOnLayoutChangeListener;
    private PopupWindow.OnDismissListener mOnPopupWindowDismissListener;
    private View mParent;
    private Rect mParentRectOnScreen;
    private float mPivotX;
    private float mPivotY;
    private int mRadius17dpForOS16_1;
    private int mShowDirection;
    private final int[] mTmpCoords;
    private final FloatPropertyCompat<Float> mToolTipsAlphaProperty;
    private float mToolTipsAnimationAlphaProgress;
    private float mToolTipsAnimationScaleProgress;
    private final FloatPropertyCompat<Float> mToolTipsScaleProperty;
    private Rect mViewPortOnScreen;
    private Rect mViewportOffset;
    private int[] mWindowLocationOnScreen;

    /* JADX INFO: renamed from: com.coui.appcompat.tooltips.COUIToolTips$1, reason: invalid class name */
    public class AnonymousClass1 extends FloatPropertyCompat<Float> {
        public AnonymousClass1(String str) {
            super(str);
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public /* bridge */ /* synthetic */ float getValue(Float f3) {
            return getValue2(f3);
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public /* bridge */ /* synthetic */ void setValue(Float f3, float f4) {
            setValue2(f3, f4);
        }

        /* JADX INFO: renamed from: getValue, reason: avoid collision after fix types in other method */
        public float getValue2(Float f3) {
            return COUIToolTips.access$000(COUIToolTips.this);
        }

        /* JADX INFO: renamed from: setValue, reason: avoid collision after fix types in other method */
        public void setValue2(Float f3, float f4) {
            COUIToolTips.access$100(COUIToolTips.this, f4);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.tooltips.COUIToolTips$2, reason: invalid class name */
    public class AnonymousClass2 extends FloatPropertyCompat<Float> {
        public AnonymousClass2(String str) {
            super(str);
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public /* bridge */ /* synthetic */ float getValue(Float f3) {
            return getValue2(f3);
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public /* bridge */ /* synthetic */ void setValue(Float f3, float f4) {
            setValue2(f3, f4);
        }

        /* JADX INFO: renamed from: getValue, reason: avoid collision after fix types in other method */
        public float getValue2(Float f3) {
            return COUIToolTips.access$200(COUIToolTips.this);
        }

        /* JADX INFO: renamed from: setValue, reason: avoid collision after fix types in other method */
        public void setValue2(Float f3, float f4) {
            COUIToolTips.access$300(COUIToolTips.this, f4);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.tooltips.COUIToolTips$3, reason: invalid class name */
    public class AnonymousClass3 implements View.OnLayoutChangeListener {
        public AnonymousClass3() {
        }

        public static /* synthetic */ void a(AnonymousClass3 anonymousClass3) {
            anonymousClass3.getClass();
            try {
                if (COUIToolTips.access$500(COUIToolTips.this) != null) {
                    COUIToolTips.this.refreshWhileLayoutChange();
                } else {
                    COUILog.e(COUIToolTips.TAG, "onLayoutChange post mParent == null!");
                }
            } catch (Exception e3) {
                Log.e(COUIToolTips.TAG, "refreshWhileLayoutChange fail,e:" + e3.getMessage());
            }
        }

        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View view, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
            Rect rect = new Rect(i3, i4, i5, i6);
            Rect rect2 = new Rect(i7, i8, i9, i10);
            if (!COUIToolTips.this.isShowing() || rect.equals(rect2) || COUIToolTips.access$400(COUIToolTips.this) == null) {
                return;
            }
            view.post(new b(this));
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.tooltips.COUIToolTips$4, reason: invalid class name */
    public class AnonymousClass4 implements PopupWindow.OnDismissListener {
        public AnonymousClass4() {
        }

        @Override // android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            COUIToolTips.access$600(COUIToolTips.this).removeAllViews();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.tooltips.COUIToolTips$5, reason: invalid class name */
    public class AnonymousClass5 implements IToolTipsAction {
        public AnonymousClass5() {
        }

        @Override // com.coui.appcompat.tooltips.IToolTipsAction
        public void onCloseClick() {
            COUIToolTips.this.dismiss();
            if (COUIToolTips.access$700(COUIToolTips.this) != null) {
                COUIToolTips.access$700(COUIToolTips.this).onCloseIconClick();
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.tooltips.COUIToolTips$6, reason: invalid class name */
    public class AnonymousClass6 implements Runnable {
        public AnonymousClass6() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (COUIToolTips.access$800(COUIToolTips.this)) {
                COUIToolTips.access$900(COUIToolTips.this);
                COUIToolTips.access$802(COUIToolTips.this, false);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.tooltips.COUIToolTips$7, reason: invalid class name */
    public class AnonymousClass7 extends ViewOutlineProvider {
        final /* synthetic */ int val$bottomPadding;
        final /* synthetic */ int val$endPadding;
        final /* synthetic */ float val$radius;
        final /* synthetic */ int val$startPadding;
        final /* synthetic */ int val$topPadding;

        public AnonymousClass7(int i3, int i4, int i5, int i6, float f3) {
            this.val$startPadding = i3;
            this.val$topPadding = i4;
            this.val$endPadding = i5;
            this.val$bottomPadding = i6;
            this.val$radius = f3;
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            Rect rect = new Rect(this.val$startPadding, this.val$topPadding, view.getWidth() - this.val$endPadding, view.getHeight() - this.val$bottomPadding);
            if (RoundCornerUtil.getSmoothStyleType() != 1) {
                if (RoundCornerUtil.getSmoothStyleType() == 0) {
                    new OplusOutline(outline).setSmoothRoundRect(rect, this.val$radius, COUIContextUtil.getFloat(COUIToolTips.access$1100(COUIToolTips.this), R.dimen.coui_round_corner_m_weight));
                    return;
                } else {
                    outline.setRoundRect(rect, this.val$radius);
                    return;
                }
            }
            OplusOutlineAdapter oplusOutlineAdapter = new OplusOutlineAdapter(outline, 1);
            if (COUIVersionUtil.getOSVersionCode() > 37) {
                oplusOutlineAdapter.setSmoothRoundRect(rect, COUIToolTips.access$1000(COUIToolTips.this), 3.0f);
            } else {
                oplusOutlineAdapter.setSmoothRoundRect(rect, this.val$radius);
            }
        }
    }

    public interface OnCloseIconClickListener {
        void onCloseIconClick();
    }

    @Deprecated
    public COUIToolTips(Window window) {
        this(window, 0);
    }

    public static /* synthetic */ void a(COUIToolTips cOUIToolTips) {
        cOUIToolTips.getClass();
        try {
            cOUIToolTips.refreshWhileLayoutChange();
        } catch (Exception e3) {
            Log.e(TAG, "refreshWhileLayoutChange fail,e:" + e3.getMessage());
        }
    }

    public static /* synthetic */ float access$000(COUIToolTips cOUIToolTips) {
        return cOUIToolTips.mToolTipsAnimationScaleProgress;
    }

    public static /* synthetic */ void access$100(COUIToolTips cOUIToolTips, float f3) {
        cOUIToolTips.setToolTipsScaleProgress(f3);
    }

    public static /* synthetic */ int access$1000(COUIToolTips cOUIToolTips) {
        return cOUIToolTips.mRadius17dpForOS16_1;
    }

    public static /* synthetic */ Context access$1100(COUIToolTips cOUIToolTips) {
        return cOUIToolTips.mContext;
    }

    public static /* synthetic */ float access$200(COUIToolTips cOUIToolTips) {
        return cOUIToolTips.mToolTipsAnimationAlphaProgress;
    }

    public static /* synthetic */ void access$300(COUIToolTips cOUIToolTips, float f3) {
        cOUIToolTips.setToolTipsAlphaProgress(f3);
    }

    public static /* synthetic */ View access$400(COUIToolTips cOUIToolTips) {
        return cOUIToolTips.mAnchor;
    }

    public static /* synthetic */ View access$500(COUIToolTips cOUIToolTips) {
        return cOUIToolTips.mParent;
    }

    public static /* synthetic */ ViewGroup access$600(COUIToolTips cOUIToolTips) {
        return cOUIToolTips.mContentContainer;
    }

    public static /* synthetic */ OnCloseIconClickListener access$700(COUIToolTips cOUIToolTips) {
        return cOUIToolTips.mOnCloseIconClickListener;
    }

    public static /* synthetic */ boolean access$800(COUIToolTips cOUIToolTips) {
        return cOUIToolTips.mIsDismissing;
    }

    public static /* synthetic */ boolean access$802(COUIToolTips cOUIToolTips, boolean z2) {
        cOUIToolTips.mIsDismissing = z2;
        return z2;
    }

    public static /* synthetic */ void access$900(COUIToolTips cOUIToolTips) {
        cOUIToolTips.dismissPopupWindow();
    }

    private void addIndicator(Rect rect, int i3, int i4) {
        if (isLayoutRtl(this.mAnchor)) {
            i3 = -i3;
        }
        int i5 = this.mShowDirection;
        if (i5 == 128 || i5 == 4) {
            i4 = 0;
        } else {
            i3 = 0;
        }
        this.mArrowView = new ImageView(this.mContext);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        int i6 = this.mShowDirection;
        if (i6 == 4 || i6 == 128) {
            handleAlignVerticalDirection(rect, layoutParams, i3, i4);
        } else if (i6 == 16) {
            handleAlignLeftDirection(rect, layoutParams, i3, i4);
        } else {
            handleAlignRightDirection(rect, layoutParams, i3, i4);
        }
        this.mContentContainer.addView(this.mArrowView, layoutParams);
        UIUtil.safeForceHasOverlappingRendering(this.mArrowView, false);
    }

    private void animateEnter() {
        ViewGroup viewGroup = this.mMainPanel;
        Resources resources = this.mContext.getResources();
        int i3 = R.dimen.support_shadow_size_level_four;
        int dimensionPixelOffset = resources.getDimensionPixelOffset(i3);
        Resources resources2 = this.mContext.getResources();
        int i4 = R.dimen.support_shadow_size_level_two;
        int dimensionPixelOffset2 = resources2.getDimensionPixelOffset(i4);
        Context context = this.mContext;
        int i5 = com.support.tips.R.color.coui_tool_tips_shadow_color;
        ShadowUtils.setElevationToView(viewGroup, 3, dimensionPixelOffset, dimensionPixelOffset2, ContextCompat.getColor(context, i5));
        ShadowUtils.setElevationToView(this.mArrowView, 3, this.mContext.getResources().getDimensionPixelOffset(i3), this.mContext.getResources().getDimensionPixelOffset(i4), ContextCompat.getColor(this.mContext, i5));
        animateSpring(true);
    }

    private void animateExit() {
        ShadowUtils.clearShadow(this.mMainPanel);
        ShadowUtils.clearShadow(this.mArrowView);
        animateSpring(false);
        this.mIsDismissing = true;
        if (isTouchable()) {
            setTouchable(false);
            update();
            this.mIfNeedResetTouchableToTrue = true;
        }
        this.mContentContainer.removeCallbacks(this.mCheckAnimateExit);
        this.mContentContainer.postDelayed(this.mCheckAnimateExit, 320L);
    }

    private void animateSpring(boolean z2) {
        this.mIsEntering = z2;
        this.mContentContainer.setPivotX(this.mPivotX * getWidth());
        this.mContentContainer.setPivotY(this.mPivotY * getHeight());
        COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(Float.valueOf(this.mToolTipsAnimationScaleProgress), this.mToolTipsScaleProperty);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(0.2f);
        cOUISpringForce.setResponse(0.4f);
        cOUISpringAnimation.setSpring(cOUISpringForce);
        cOUISpringAnimation.setStartValue(0.0f);
        cOUISpringAnimation.animateToFinalPosition(10000.0f);
        COUISpringAnimation cOUISpringAnimation2 = new COUISpringAnimation(Float.valueOf(this.mToolTipsAnimationAlphaProgress), this.mToolTipsAlphaProperty);
        COUISpringForce cOUISpringForce2 = new COUISpringForce();
        cOUISpringForce2.setBounce(0.2f);
        cOUISpringForce2.setResponse(0.3f);
        cOUISpringAnimation2.setSpring(cOUISpringForce2);
        cOUISpringAnimation2.setStartValue(0.0f);
        cOUISpringAnimation2.animateToFinalPosition(10000.0f);
    }

    private void calculatePivot() {
        int i3 = this.mShowDirection;
        if (i3 != 4 && i3 != 128) {
            this.mPivotX = i3 == 16 ? 1.0f : 0.0f;
            this.mPivotY = (getAnchorCenterYInParent() - this.mCoordsOnWindow.y) / getViewportHeight();
            return;
        }
        if (getAnchorCenterXInParent() - this.mCoordsOnWindow.x >= getViewportWidth()) {
            this.mPivotX = 1.0f;
        } else if (getViewportWidth() != 0) {
            int anchorCenterXInParent = getAnchorCenterXInParent() - this.mCoordsOnWindow.x;
            if (anchorCenterXInParent <= 0) {
                anchorCenterXInParent = -anchorCenterXInParent;
            }
            this.mPivotX = anchorCenterXInParent / getViewportWidth();
        } else {
            this.mPivotX = 0.5f;
        }
        if (this.mCoordsOnWindow.y >= getAnchorTopInParent()) {
            this.mPivotY = 0.0f;
        } else {
            this.mPivotY = 1.0f;
        }
    }

    private void calculatePosition() {
        this.mParent.getWindowVisibleDisplayFrame(this.mViewPortOnScreen);
        registerOrientationHandler();
        computeAnchorContentRectOnScreen();
        computeParentRectOnScreen();
        Rect rect = this.mViewPortOnScreen;
        rect.left = Math.max(rect.left, this.mParentRectOnScreen.left);
        Rect rect2 = this.mViewPortOnScreen;
        rect2.top = Math.max(rect2.top, this.mParentRectOnScreen.top);
        Rect rect3 = this.mViewPortOnScreen;
        rect3.right = Math.min(rect3.right, this.mParentRectOnScreen.right);
        Rect rect4 = this.mViewPortOnScreen;
        rect4.bottom = Math.min(rect4.bottom, this.mParentRectOnScreen.bottom);
        sizePopupWindow();
        refreshCoordinated(this.mContentRectOnScreen);
        if (this.mIndicatorFollowContent) {
            prepareContent(this.mContentRectOnScreen, this.mHasIndicator, 0, 0);
        } else {
            prepareContent(this.mContentRectOnScreen, this.mHasIndicator, -this.mOffsetX, -this.mOffsetY);
        }
        setContentView(this.mContentContainer);
        calculatePivot();
        animateEnter();
        if (isLayoutRtl(this.mAnchor)) {
            this.mCoordsOnWindow.x -= this.mOffsetX;
        } else {
            this.mCoordsOnWindow.x += this.mOffsetX;
        }
        this.mCoordsOnWindow.y += this.mOffsetY;
    }

    private void checkViewTypeEnum(WindowSpacingControlHelper.AnchorViewTypeEnum anchorViewTypeEnum) {
        WindowSpacingControlHelper.AnchorViewTypeEnum anchorViewTypeEnum2 = this.mWindowSpacingControlHelper.getAnchorViewTypeEnum(this.mAnchor);
        if (anchorViewTypeEnum2 == WindowSpacingControlHelper.AnchorViewTypeEnum.TOOLBAR || anchorViewTypeEnum2 == WindowSpacingControlHelper.AnchorViewTypeEnum.NAVIGATION) {
            return;
        }
        this.mArrowOverflow = getAnchorViewSpacing(anchorViewTypeEnum);
    }

    private void computeAnchorContentRectOnScreen() {
        this.mContentRectOnScreen = new Rect();
        int width = this.mAnchor.getWidth();
        int height = this.mAnchor.getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        int[] iArr = new int[2];
        this.mAnchor.getLocationOnScreen(iArr);
        Rect rect = new Rect();
        fillAnchorContentRectFromOrigin(resolveAnchorOriginForCenter(iArr, rect, obtainAnchorGlobalVisibleRectOnScreen(rect), width, height), width, height);
    }

    private void computeParentRectOnScreen() {
        Rect rect = new Rect();
        this.mParentRectOnScreen = rect;
        this.mParent.getGlobalVisibleRect(rect);
        int[] iArr = new int[2];
        this.mParent.getLocationOnScreen(iArr);
        this.mParentRectOnScreen.offset(iArr[0], iArr[1]);
    }

    private ViewGroup createContentContainer(Context context) {
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        return frameLayout;
    }

    private void dismissPopupWindow() {
        this.mBubbleStyle.dismissWindow();
        unregisterOrientationHandler();
        this.mParent = null;
        super.dismiss();
        this.mContentContainer.removeAllViews();
        this.mContentContainer.removeCallbacks(this.mCheckAnimateExit);
    }

    private void fillAnchorContentRectFromOrigin(int[] iArr, int i3, int i4) {
        int[] iArr2 = new int[2];
        this.mWindowSpacingControlHelper.setOriginCenterPoint(iArr, iArr2, this.mAnchor);
        Rect rect = this.mContentRectOnScreen;
        int i5 = iArr2[0];
        int i6 = i3 / 2;
        rect.left = i5 - i6;
        int i7 = iArr2[1];
        int i8 = i4 / 2;
        rect.top = i7 - i8;
        rect.right = i5 + i6;
        rect.bottom = i7 + i8;
    }

    private int getAnchorCenterXInParent() {
        this.mParent.getLocationOnScreen(this.mTmpCoords);
        return this.mContentRectOnScreen.centerX() - this.mTmpCoords[0];
    }

    private int getAnchorCenterYInParent() {
        this.mParent.getLocationOnScreen(this.mTmpCoords);
        return this.mContentRectOnScreen.centerY() - this.mTmpCoords[1];
    }

    private int getAnchorTopInParent() {
        this.mParent.getLocationOnScreen(this.mTmpCoords);
        return this.mContentRectOnScreen.top - this.mTmpCoords[1];
    }

    private int getDistanceFromAnchorCenterToViewportRight() {
        return this.mViewPortOnScreen.right - this.mContentRectOnScreen.centerX();
    }

    private int getViewportHeight() {
        int height = getHeight();
        Rect rect = this.mViewportOffset;
        return (height - rect.top) + rect.bottom;
    }

    private int getViewportWidth() {
        int width = getWidth();
        Rect rect = this.mViewportOffset;
        return (width - rect.left) + rect.right;
    }

    private void handleAlignBottomDirection(FrameLayout.LayoutParams layoutParams, float f3, float f4, float f5, int i3) {
        float f6 = layoutParams.leftMargin;
        int i4 = this.mEdgeExtraDistance;
        if (f6 < f4 + f3 + i4) {
            if (this.mArrowUpLeftDrawable == null) {
                this.mArrowUpLeftDrawable = DrawableUtils.rotateDrawable(this.mContext, this.mArrowUpRightDrawable, 0.0f, true);
            }
            this.mArrowDirection = 1;
            this.mArrowView.setBackground(this.mArrowUpLeftDrawable);
            layoutParams.leftMargin = Math.max((int) (f3 + f5 + this.mEdgeExtraDistance), getAnchorCenterXInParent());
            layoutParams.rightMargin = (getWidth() - layoutParams.leftMargin) - this.mArrowUpLeftDrawable.getIntrinsicWidth();
            layoutParams.topMargin = (this.mMainPanel.getPaddingTop() - this.mArrowUpLeftDrawable.getIntrinsicHeight()) + this.mIndicatorEmbedDistance;
        } else {
            float f7 = f3 + f5;
            if (layoutParams.rightMargin < i4 + f7) {
                this.mArrowDirection = 3;
                this.mArrowView.setBackground(this.mArrowUpRightDrawable);
                layoutParams.rightMargin = Math.max((int) (f7 + this.mEdgeExtraDistance), getDistanceFromAnchorCenterToViewportRight());
                layoutParams.leftMargin = (getWidth() - layoutParams.rightMargin) - this.mArrowUpRightDrawable.getIntrinsicWidth();
                layoutParams.topMargin = (this.mMainPanel.getPaddingTop() - this.mArrowUpRightDrawable.getIntrinsicHeight()) + this.mIndicatorEmbedDistance;
            } else {
                this.mArrowDirection = 2;
                this.mArrowView.setBackground(this.mArrowUpDrawable);
                layoutParams.topMargin = (this.mMainPanel.getPaddingTop() - this.mArrowUpDrawable.getIntrinsicHeight()) + this.mIndicatorEmbedDistance;
            }
        }
        layoutParams.topMargin += i3;
    }

    private void handleAlignLeftDirection(Rect rect, FrameLayout.LayoutParams layoutParams, int i3, int i4) {
        if (this.mArrowRightDrawable == null) {
            this.mArrowRightDrawable = DrawableUtils.rotateDrawable(this.mContext, this.mArrowUpDrawable, 90.0f, false);
        }
        layoutParams.rightMargin = ((this.mMainPanel.getPaddingRight() - this.mArrowRightDrawable.getIntrinsicWidth()) - i3) + this.mIndicatorEmbedDistance;
        layoutParams.leftMargin = ((getWidth() - layoutParams.rightMargin) - this.mArrowRightDrawable.getIntrinsicWidth()) - this.mIndicatorEmbedDistance;
        layoutParams.topMargin = (getAnchorCenterYInParent() - this.mCoordsOnWindow.y) - (this.mArrowRightDrawable.getIntrinsicHeight() / 2);
        layoutParams.bottomMargin = (getHeight() - layoutParams.topMargin) - this.mArrowRightDrawable.getIntrinsicHeight();
        float attrDimensFloat = COUIContextUtil.getAttrDimensFloat(this.mContext, RoundCornerUtil.isVersionSupport() ? R.attr.couiRoundCornerMRadius : R.attr.couiRoundCornerM);
        float paddingTop = this.mMainPanel.getPaddingTop();
        float paddingBottom = this.mMainPanel.getPaddingBottom();
        float f3 = paddingTop + attrDimensFloat;
        if (layoutParams.topMargin < f3) {
            if (this.mArrowRightUpDrawable == null) {
                this.mArrowRightUpDrawable = DrawableUtils.rotateDrawable(this.mContext, this.mArrowUpRightDrawable, 90.0f, true);
            }
            this.mArrowDirection = 4;
            this.mArrowView.setBackground(this.mArrowRightUpDrawable);
            layoutParams.topMargin = (int) f3;
            layoutParams.bottomMargin = (getHeight() - layoutParams.topMargin) - this.mArrowRightUpDrawable.getIntrinsicHeight();
        } else {
            float f4 = attrDimensFloat + paddingBottom;
            if (layoutParams.bottomMargin < f4) {
                if (this.mArrowRightDownDrawable == null) {
                    this.mArrowRightDownDrawable = DrawableUtils.rotateDrawable(this.mContext, this.mArrowUpRightDrawable, 90.0f, false);
                }
                this.mArrowDirection = 6;
                this.mArrowView.setBackground(this.mArrowRightDownDrawable);
                layoutParams.bottomMargin = (int) f4;
                layoutParams.topMargin = (getHeight() - layoutParams.bottomMargin) - this.mArrowRightDownDrawable.getIntrinsicHeight();
            } else {
                this.mArrowDirection = 5;
                this.mArrowView.setBackground(this.mArrowRightDrawable);
            }
        }
        layoutParams.topMargin += i4;
    }

    private void handleAlignRightDirection(Rect rect, FrameLayout.LayoutParams layoutParams, int i3, int i4) {
        if (this.mArrowLeftDrawable == null) {
            this.mArrowLeftDrawable = DrawableUtils.rotateDrawable(this.mContext, this.mArrowUpDrawable, ROTATION_ANGLE_270, false);
        }
        layoutParams.leftMargin = (this.mMainPanel.getPaddingLeft() - this.mArrowLeftDrawable.getIntrinsicWidth()) + i3 + this.mIndicatorEmbedDistance;
        layoutParams.rightMargin = ((getWidth() - layoutParams.leftMargin) - this.mArrowLeftDrawable.getIntrinsicWidth()) - this.mIndicatorEmbedDistance;
        layoutParams.topMargin = (getAnchorCenterYInParent() - this.mCoordsOnWindow.y) - (this.mArrowLeftDrawable.getIntrinsicHeight() / 2);
        layoutParams.bottomMargin = (getHeight() - layoutParams.topMargin) - this.mArrowLeftDrawable.getIntrinsicHeight();
        float attrDimensFloat = COUIContextUtil.getAttrDimensFloat(this.mContext, RoundCornerUtil.isVersionSupport() ? R.attr.couiRoundCornerMRadius : R.attr.couiRoundCornerM);
        float paddingTop = this.mMainPanel.getPaddingTop();
        float paddingBottom = this.mMainPanel.getPaddingBottom();
        float f3 = paddingTop + attrDimensFloat;
        if (layoutParams.topMargin < f3) {
            if (this.mArrowLeftUpDrawable == null) {
                this.mArrowLeftUpDrawable = DrawableUtils.rotateDrawable(this.mContext, this.mArrowUpRightDrawable, ROTATION_ANGLE_270, false);
            }
            this.mArrowDirection = 12;
            this.mArrowView.setBackground(this.mArrowLeftUpDrawable);
            layoutParams.topMargin = (int) f3;
            layoutParams.bottomMargin = (getHeight() - layoutParams.topMargin) - this.mArrowLeftUpDrawable.getIntrinsicHeight();
        } else if (layoutParams.bottomMargin < attrDimensFloat + paddingBottom) {
            if (this.mArrowLeftDownDrawable == null) {
                this.mArrowLeftDownDrawable = DrawableUtils.rotateDrawable(this.mContext, this.mArrowUpRightDrawable, ROTATION_ANGLE_270, true);
            }
            this.mArrowDirection = 10;
            this.mArrowView.setBackground(this.mArrowLeftDownDrawable);
            layoutParams.bottomMargin = (int) f3;
            layoutParams.topMargin = (getHeight() - layoutParams.bottomMargin) - this.mArrowLeftDownDrawable.getIntrinsicHeight();
        } else {
            this.mArrowDirection = 11;
            this.mArrowView.setBackground(this.mArrowLeftDrawable);
        }
        layoutParams.topMargin += i4;
    }

    private void handleAlignTopDirection(FrameLayout.LayoutParams layoutParams, float f3, float f4, float f5, int i3) {
        layoutParams.gravity = 80;
        if (layoutParams.leftMargin < f4 + f3 + this.mEdgeExtraDistance) {
            if (this.mArrowDownLeftDrawable == null) {
                this.mArrowDownLeftDrawable = DrawableUtils.rotateDrawable(this.mContext, this.mArrowUpRightDrawable, 180.0f, false);
            }
            this.mArrowDirection = 9;
            this.mArrowView.setBackground(this.mArrowDownLeftDrawable);
            layoutParams.leftMargin = Math.max((int) (f3 + f5 + this.mEdgeExtraDistance), getAnchorCenterXInParent());
            layoutParams.rightMargin = (getWidth() - layoutParams.leftMargin) - this.mArrowDownLeftDrawable.getIntrinsicWidth();
            layoutParams.bottomMargin = (this.mMainPanel.getPaddingBottom() - this.mArrowDownLeftDrawable.getIntrinsicHeight()) + this.mIndicatorEmbedDistance;
        } else {
            float f6 = f3 + f5;
            if (layoutParams.rightMargin < f6) {
                if (this.mArrowDownRightDrawable == null) {
                    this.mArrowDownRightDrawable = DrawableUtils.rotateDrawable(this.mContext, this.mArrowUpRightDrawable, 180.0f, true);
                }
                this.mArrowDirection = 7;
                this.mArrowView.setBackground(this.mArrowDownRightDrawable);
                layoutParams.rightMargin = Math.max((int) (f6 + this.mEdgeExtraDistance), getDistanceFromAnchorCenterToViewportRight());
                layoutParams.leftMargin = (getWidth() - layoutParams.rightMargin) - this.mArrowDownRightDrawable.getIntrinsicWidth();
                layoutParams.bottomMargin = (this.mMainPanel.getPaddingBottom() - this.mArrowDownRightDrawable.getIntrinsicHeight()) + this.mIndicatorEmbedDistance;
            } else {
                if (this.mArrowDownDrawable == null) {
                    this.mArrowDownDrawable = DrawableUtils.rotateDrawable(this.mContext, this.mArrowUpDrawable, 180.0f, false);
                }
                this.mArrowDirection = 8;
                this.mArrowView.setBackground(this.mArrowDownDrawable);
                layoutParams.bottomMargin = (this.mMainPanel.getPaddingBottom() - this.mArrowDownDrawable.getIntrinsicHeight()) + this.mIndicatorEmbedDistance;
            }
        }
        layoutParams.bottomMargin -= i3;
    }

    private void handleAlignVerticalDirection(Rect rect, FrameLayout.LayoutParams layoutParams, int i3, int i4) {
        layoutParams.leftMargin = ((getAnchorCenterXInParent() - this.mCoordsOnWindow.x) - (this.mArrowUpDrawable.getIntrinsicWidth() / 2)) + i3;
        layoutParams.rightMargin = (getWidth() - layoutParams.leftMargin) - this.mArrowUpDrawable.getIntrinsicWidth();
        int anchorTopInParent = getAnchorTopInParent();
        float attrDimensFloat = COUIContextUtil.getAttrDimensFloat(this.mContext, RoundCornerUtil.isVersionSupport() ? R.attr.couiRoundCornerMRadius : R.attr.couiRoundCornerM);
        float paddingLeft = this.mMainPanel.getPaddingLeft();
        float paddingRight = this.mMainPanel.getPaddingRight();
        if (this.mCoordsOnWindow.y >= anchorTopInParent) {
            handleAlignBottomDirection(layoutParams, attrDimensFloat, paddingLeft, paddingRight, i4);
        } else {
            handleAlignTopDirection(layoutParams, attrDimensFloat, paddingLeft, paddingRight, i4);
        }
    }

    private boolean isAnchorVisibleRectClipped(Rect rect, boolean z2, int i3, int i4) {
        if (!z2) {
            return true;
        }
        return rect.width() < Math.round(((float) i3) * this.mAnchor.getScaleX()) || rect.height() < Math.round(((float) i4) * this.mAnchor.getScaleY());
    }

    private boolean obtainAnchorGlobalVisibleRectOnScreen(Rect rect) {
        if (!this.mAnchor.getGlobalVisibleRect(rect)) {
            return false;
        }
        int[] iArr = new int[2];
        View view = this.mParent;
        if (view != null) {
            view.getLocationOnScreen(iArr);
        } else {
            this.mAnchor.getRootView().getLocationOnScreen(iArr);
        }
        rect.offset(iArr[0], iArr[1]);
        return true;
    }

    private void offsetRect(Rect rect, Rect rect2) {
        if (rect2 == null) {
            return;
        }
        rect.left += rect2.left;
        rect.top += rect2.top;
        rect.right += rect2.right;
        rect.bottom += rect2.bottom;
    }

    private void prepareContent(Rect rect, boolean z2, int i3, int i4) {
        this.mContentContainer.removeAllViews();
        this.mContentContainer.addView(this.mMainPanel);
        if (z2) {
            addIndicator(rect, i3, i4);
        }
    }

    private void refreshCoordinated(Rect rect) {
        int viewportWidth;
        int iMax;
        int viewportHeight;
        int i3;
        this.mArrowType = -1;
        int anchorViewSpacing = getAnchorViewSpacing(this.mWindowSpacingControlHelper.getAnchorViewTypeEnum(this.mAnchor));
        int i4 = this.mShowDirection;
        if (i4 == 4) {
            viewportWidth = Math.min(rect.centerX() - (getViewportWidth() / 2), this.mViewPortOnScreen.right - ((getViewportWidth() - this.mMainPanel.getPaddingRight()) + this.mBubbleHorizontalPadding));
            int i5 = rect.top;
            Rect rect2 = this.mViewPortOnScreen;
            int i6 = i5 - rect2.top;
            int i7 = (rect2.bottom - rect.bottom) - anchorViewSpacing;
            viewportHeight = getViewportHeight();
            if (i6 >= viewportHeight) {
                this.mArrowType = 4;
                i3 = rect.top;
                iMax = i3 - viewportHeight;
            } else if (i7 >= viewportHeight) {
                this.mArrowType = 128;
                iMax = rect.bottom;
            } else if (i6 > i7) {
                this.mArrowType = 4;
                iMax = this.mViewPortOnScreen.top;
                setHeight(i6);
            } else {
                this.mArrowType = 128;
                iMax = rect.bottom;
                setHeight(i7);
            }
        } else if (i4 == 128) {
            viewportWidth = Math.min(rect.centerX() - (getViewportWidth() / 2), this.mViewPortOnScreen.right - ((getViewportWidth() - this.mMainPanel.getPaddingRight()) + this.mBubbleHorizontalPadding));
            int i8 = rect.top;
            Rect rect3 = this.mViewPortOnScreen;
            int i9 = i8 - rect3.top;
            int i10 = (rect3.bottom - rect.bottom) - anchorViewSpacing;
            viewportHeight = getViewportHeight();
            if (i10 >= viewportHeight) {
                this.mArrowType = 128;
                iMax = rect.bottom;
            } else if (i9 >= viewportHeight) {
                this.mArrowType = 4;
                i3 = rect.top;
                iMax = i3 - viewportHeight;
            } else if (i9 > i10) {
                this.mArrowType = 4;
                iMax = this.mViewPortOnScreen.top;
                setHeight(i9);
            } else {
                this.mArrowType = 128;
                iMax = rect.bottom;
                setHeight(i10);
            }
        } else {
            viewportWidth = i4 == 16 ? rect.left - getViewportWidth() : rect.right;
            iMax = Math.max(rect.centerY() - (((getViewportHeight() + this.mMainPanel.getPaddingTop()) - this.mMainPanel.getPaddingBottom()) / 2), (this.mViewPortOnScreen.top + this.mViewportOffset.top) - this.mMainPanel.getPaddingTop());
        }
        this.mParent.getRootView().getLocationOnScreen(this.mTmpCoords);
        int[] iArr = this.mTmpCoords;
        int i11 = iArr[0];
        int i12 = iArr[1];
        this.mParent.getRootView().getLocationInWindow(this.mTmpCoords);
        int[] iArr2 = this.mTmpCoords;
        int i13 = iArr2[0];
        int i14 = iArr2[1];
        int[] iArr3 = this.mWindowLocationOnScreen;
        int i15 = i11 - i13;
        iArr3[0] = i15;
        int i16 = i12 - i14;
        iArr3[1] = i16;
        int i17 = viewportWidth - i15;
        Rect rect4 = this.mViewportOffset;
        int i18 = i17 - rect4.left;
        int i19 = (iMax - i16) - rect4.top;
        int i20 = this.mShowDirection;
        if (i20 == 8) {
            checkViewTypeEnum(WindowSpacingControlHelper.AnchorViewTypeEnum.END);
            i18 += this.mArrowOverflow;
        } else if (i20 == 16) {
            checkViewTypeEnum(WindowSpacingControlHelper.AnchorViewTypeEnum.START);
            i18 -= this.mArrowOverflow;
        } else {
            int i21 = this.mArrowType;
            if (i21 == 4) {
                checkViewTypeEnum(WindowSpacingControlHelper.AnchorViewTypeEnum.TOP);
                i19 -= this.mArrowOverflow;
            } else if (i21 == 128) {
                checkViewTypeEnum(WindowSpacingControlHelper.AnchorViewTypeEnum.BOTTOM);
                i19 += this.mArrowOverflow;
            }
        }
        this.mCoordsOnWindow.set(Math.max(this.mBubbleHorizontalPadding - this.mMainPanel.getPaddingLeft(), i18), Math.max(0, i19));
    }

    private void registerOrientationHandler() {
        unregisterOrientationHandler();
        this.mParent.addOnLayoutChangeListener(this.mOnLayoutChangeListener);
    }

    private int[] resolveAnchorOriginForCenter(int[] iArr, Rect rect, boolean z2, int i3, int i4) {
        int[] iArr2 = new int[2];
        if (isAnchorVisibleRectClipped(rect, z2, i3, i4)) {
            iArr2[0] = iArr[0];
            iArr2[1] = iArr[1];
            return iArr2;
        }
        iArr2[0] = rect.left;
        iArr2[1] = rect.top;
        return iArr2;
    }

    private void setArrowViewDrawable() {
        ImageView imageView = this.mArrowView;
        if (imageView != null) {
            switch (this.mArrowDirection) {
                case 1:
                    Drawable drawableRotateDrawable = DrawableUtils.rotateDrawable(this.mContext, this.mArrowUpRightDrawable, 0.0f, true);
                    this.mArrowUpLeftDrawable = drawableRotateDrawable;
                    this.mArrowView.setBackground(drawableRotateDrawable);
                    break;
                case 2:
                    imageView.setBackground(this.mArrowUpDrawable);
                    break;
                case 3:
                    imageView.setBackground(this.mArrowUpRightDrawable);
                    break;
                case 4:
                    Drawable drawableRotateDrawable2 = DrawableUtils.rotateDrawable(this.mContext, this.mArrowUpRightDrawable, 90.0f, true);
                    this.mArrowRightUpDrawable = drawableRotateDrawable2;
                    this.mArrowView.setBackground(drawableRotateDrawable2);
                    break;
                case 5:
                    Drawable drawableRotateDrawable3 = DrawableUtils.rotateDrawable(this.mContext, this.mArrowUpDrawable, 90.0f, false);
                    this.mArrowRightDrawable = drawableRotateDrawable3;
                    this.mArrowView.setBackground(drawableRotateDrawable3);
                    break;
                case 6:
                    Drawable drawableRotateDrawable4 = DrawableUtils.rotateDrawable(this.mContext, this.mArrowUpRightDrawable, 90.0f, false);
                    this.mArrowRightDownDrawable = drawableRotateDrawable4;
                    this.mArrowView.setBackground(drawableRotateDrawable4);
                    break;
                case 7:
                    Drawable drawableRotateDrawable5 = DrawableUtils.rotateDrawable(this.mContext, this.mArrowUpRightDrawable, 180.0f, true);
                    this.mArrowDownRightDrawable = drawableRotateDrawable5;
                    this.mArrowView.setBackground(drawableRotateDrawable5);
                    break;
                case 8:
                default:
                    Drawable drawableRotateDrawable6 = DrawableUtils.rotateDrawable(this.mContext, this.mArrowUpDrawable, 180.0f, false);
                    this.mArrowDownDrawable = drawableRotateDrawable6;
                    this.mArrowView.setBackground(drawableRotateDrawable6);
                    break;
                case 9:
                    Drawable drawableRotateDrawable7 = DrawableUtils.rotateDrawable(this.mContext, this.mArrowUpRightDrawable, 180.0f, false);
                    this.mArrowDownLeftDrawable = drawableRotateDrawable7;
                    this.mArrowView.setBackground(drawableRotateDrawable7);
                    break;
                case 10:
                    Drawable drawableRotateDrawable8 = DrawableUtils.rotateDrawable(this.mContext, this.mArrowUpRightDrawable, ROTATION_ANGLE_270, true);
                    this.mArrowLeftDownDrawable = drawableRotateDrawable8;
                    this.mArrowView.setBackground(drawableRotateDrawable8);
                    break;
                case 11:
                    Drawable drawableRotateDrawable9 = DrawableUtils.rotateDrawable(this.mContext, this.mArrowUpDrawable, ROTATION_ANGLE_270, false);
                    this.mArrowLeftDrawable = drawableRotateDrawable9;
                    this.mArrowView.setBackground(drawableRotateDrawable9);
                    break;
                case 12:
                    Drawable drawableRotateDrawable10 = DrawableUtils.rotateDrawable(this.mContext, this.mArrowUpRightDrawable, ROTATION_ANGLE_270, false);
                    this.mArrowLeftUpDrawable = drawableRotateDrawable10;
                    this.mArrowView.setBackground(drawableRotateDrawable10);
                    break;
            }
        }
    }

    private void setBackground(int i3, int i4, int i5, int i6, int i7) {
        float attrDimensFloat = COUIContextUtil.getAttrDimensFloat(this.mContext, RoundCornerUtil.getSmoothStyleType() == 0 ? R.attr.couiRoundCornerMRadius : R.attr.couiRoundCornerM);
        this.mMainPanel.setBackgroundColor(i3);
        this.mMainPanel.setPaddingRelative(i4, i5, i6, i7);
        this.mMainPanel.setOutlineProvider(new AnonymousClass7(i4, i5, i6, i7, attrDimensFloat));
        this.mMainPanel.setClipToOutline(true);
    }

    private void setToolTipsAlphaProgress(float f3) {
        float f4 = f3 / 10000.0f;
        this.mToolTipsAnimationAlphaProgress = f4;
        float f5 = 1.0f;
        float f6 = 0.0f;
        if (this.mIsEntering) {
            f6 = 1.0f;
            f5 = 0.0f;
        }
        this.mContentContainer.setAlpha(UIUtil.getConvertedFraction(f5, f6, f4));
    }

    private void setToolTipsScaleProgress(float f3) {
        float f4 = f3 / 10000.0f;
        this.mToolTipsAnimationScaleProgress = f4;
        float f5 = 1.0f;
        float f6 = 0.0f;
        if (this.mIsEntering) {
            f6 = 1.0f;
            f5 = 0.0f;
        }
        this.mContentContainer.setScaleX(UIUtil.getConvertedFraction(f5, f6, f4));
        this.mContentContainer.setScaleY(UIUtil.getConvertedFraction(f5, f6, this.mToolTipsAnimationScaleProgress));
    }

    private void showToolTips() {
        Activity activityContextToActivity = UIUtil.contextToActivity(this.mContext);
        if (activityContextToActivity == null || !(activityContextToActivity.isFinishing() || activityContextToActivity.isDestroyed())) {
            calculatePosition();
            View view = this.mParent;
            Point point = this.mCoordsOnWindow;
            showAtLocation(view, 0, point.x, point.y);
            UIUtil.safeForceHasOverlappingRendering(this.mContentContainer, false);
            for (ViewParent parent = this.mContentContainer.getParent(); parent != null && (parent instanceof ViewGroup); parent = parent.getParent()) {
                ViewGroup viewGroup = (ViewGroup) parent;
                viewGroup.setClipToOutline(false);
                viewGroup.setClipChildren(false);
                UIUtil.safeForceHasOverlappingRendering((View) parent, false);
            }
        }
    }

    private void sizePopupWindow() {
        int maxWidth = this.mBubbleStyle.getMaxWidth() + this.mMainPanel.getPaddingLeft() + this.mMainPanel.getPaddingRight();
        int i3 = this.mShowDirection;
        if (i3 == 8) {
            maxWidth = Math.min((((this.mViewPortOnScreen.right - this.mContentRectOnScreen.right) - this.mArrowOverflow) + this.mMainPanel.getPaddingRight()) - this.mBubbleHorizontalPadding, maxWidth);
        } else if (i3 == 16) {
            maxWidth = Math.min(((this.mContentRectOnScreen.left - this.mViewPortOnScreen.left) - this.mArrowOverflow) - (this.mBubbleHorizontalPadding - this.mMainPanel.getPaddingLeft()), maxWidth);
        }
        Rect rect = this.mViewPortOnScreen;
        int iMax = Math.max(Math.min(rect.right - rect.left, maxWidth), 0);
        this.mBubbleStyle.sizeBubbleStyle(this.mMainPanel, iMax);
        this.mMainPanel.measure(0, 0);
        setWidth(this.mBubbleStyle.getRealWidth(iMax, this.mMainPanel));
        int measuredHeight = this.mMainPanel.getMeasuredHeight();
        Rect rect2 = this.mViewPortOnScreen;
        setHeight(Math.min(measuredHeight, rect2.bottom - rect2.top));
        if (((this.mContentRectOnScreen.centerY() - (((getViewportHeight() + this.mMainPanel.getPaddingTop()) - this.mMainPanel.getPaddingBottom()) / 2)) + getViewportHeight()) - this.mMainPanel.getPaddingBottom() >= this.mViewPortOnScreen.bottom) {
            this.mShowDirection = 4;
            int maxWidth2 = this.mBubbleStyle.getMaxWidth() + this.mMainPanel.getPaddingLeft() + this.mMainPanel.getPaddingRight();
            Rect rect3 = this.mViewPortOnScreen;
            int iMax2 = Math.max(Math.min(rect3.right - rect3.left, maxWidth2), 0);
            this.mBubbleStyle.sizeBubbleStyle(this.mMainPanel, iMax2);
            this.mMainPanel.measure(0, 0);
            setWidth(this.mBubbleStyle.getRealWidth(iMax2, this.mMainPanel));
            setHeight(this.mMainPanel.getMeasuredHeight());
        }
    }

    private void unregisterOrientationHandler() {
        View view = this.mParent;
        if (view != null) {
            view.removeOnLayoutChangeListener(this.mOnLayoutChangeListener);
        }
    }

    public void calculateFakeBeyondScreenTips(View view, int i3, boolean z2, int i4, int i5) {
        if (isShowing()) {
            return;
        }
        this.mShowDirection = i3;
        this.mParent = view.getRootView();
        this.mOffsetY = i5;
        int i6 = this.mShowDirection;
        if (i6 == 32 || i6 == 64) {
            if (isLayoutRtl(view)) {
                this.mShowDirection = this.mShowDirection == 32 ? 8 : 16;
            } else {
                this.mShowDirection = this.mShowDirection != 32 ? 8 : 16;
            }
        } else if (i6 == 256) {
            this.mShowDirection = 4;
        }
        this.mAnchor = view;
        this.mParent.getWindowVisibleDisplayFrame(this.mViewPortOnScreen);
        registerOrientationHandler();
        computeAnchorContentRectOnScreen();
        computeParentRectOnScreen();
        Rect rect = this.mViewPortOnScreen;
        rect.left = Math.max(rect.left, this.mParentRectOnScreen.left);
        Rect rect2 = this.mViewPortOnScreen;
        rect2.top = Math.max(rect2.top, this.mParentRectOnScreen.top);
        Rect rect3 = this.mViewPortOnScreen;
        rect3.right = Math.min(rect3.right, this.mParentRectOnScreen.right);
        Rect rect4 = this.mViewPortOnScreen;
        rect4.bottom = Math.min(rect4.bottom, this.mParentRectOnScreen.bottom);
        sizePopupWindow();
        refreshCoordinated(this.mContentRectOnScreen);
        prepareContent(this.mContentRectOnScreen, z2, -i4, -i5);
        setContentView(this.mContentContainer);
        calculatePivot();
        animateEnter();
    }

    @Override // android.widget.PopupWindow
    public void dismiss() {
        unregisterOrientationHandler();
        if (!this.mIsDismissing) {
            animateExit();
        } else {
            dismissPopupWindow();
            this.mIsDismissing = false;
        }
    }

    public void dismissImmediately() {
        dismissPopupWindow();
        this.mIsDismissing = false;
    }

    @Deprecated
    public TextView getContentTv() {
        return this.mBubbleStyle.getContentView();
    }

    @Deprecated
    public ImageView getDismissIv() {
        return this.mBubbleStyle.getDismissIv();
    }

    @Deprecated
    public void hideDismissButton() {
        this.mBubbleStyle.hideDismissView();
    }

    public void init(int i3) {
        int i4 = this.mBubbleStyle.getDefStyleParams()[0];
        int i5 = this.mBubbleStyle.getDefStyleParams()[1];
        this.mMode = i3;
        if (this.mBubbleStyle instanceof COUIDefaultBubbleStyleImpl) {
            this.mArrowUpDrawable = ContextCompat.getDrawable(this.mContext, i3 == 0 ? com.support.tips.R.drawable.coui_blue_sharp_middle_arrow_up : com.support.tips.R.drawable.coui_white_sharp_middle_arrow_up);
            this.mArrowUpRightDrawable = ContextCompat.getDrawable(this.mContext, i3 == 0 ? com.support.tips.R.drawable.coui_blue_sharp_edges_arrow_up_right : com.support.tips.R.drawable.coui_white_sharp_edges_arrow_up_right);
        } else {
            this.mArrowUpDrawable = ContextCompat.getDrawable(this.mContext, com.support.tips.R.drawable.coui_white_sharp_middle_arrow_up);
            this.mArrowUpRightDrawable = ContextCompat.getDrawable(this.mContext, com.support.tips.R.drawable.coui_white_sharp_edges_arrow_up_right);
        }
        TypedArray typedArrayObtainStyledAttributes = this.mContext.obtainStyledAttributes(null, com.support.tips.R.styleable.COUIToolTips, i4, i5);
        int color = typedArrayObtainStyledAttributes.getColor(com.support.tips.R.styleable.COUIToolTips_couiToolTipsBackgroundColor, 0);
        int dimensionPixelSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(com.support.tips.R.styleable.COUIToolTips_couiToolTipsPaddingStart, -1);
        int dimensionPixelSize2 = typedArrayObtainStyledAttributes.getDimensionPixelSize(com.support.tips.R.styleable.COUIToolTips_couiToolTipsPaddingTop, -1);
        int dimensionPixelSize3 = typedArrayObtainStyledAttributes.getDimensionPixelSize(com.support.tips.R.styleable.COUIToolTips_couiToolTipsPaddingEnd, -1);
        int dimensionPixelSize4 = typedArrayObtainStyledAttributes.getDimensionPixelSize(com.support.tips.R.styleable.COUIToolTips_couiToolTipsPaddingBottom, -1);
        int i6 = com.support.tips.R.styleable.COUIToolTips_couiToolTipsArrowOverflowOffset;
        this.mArrowOverflow = typedArrayObtainStyledAttributes.getDimensionPixelSize(i6, 0);
        int dimensionPixelSize5 = typedArrayObtainStyledAttributes.getDimensionPixelSize(i6, 0);
        int dimensionPixelSize6 = typedArrayObtainStyledAttributes.getDimensionPixelSize(com.support.tips.R.styleable.COUIToolTips_couiToolTipsMinWidth, 0);
        int dimensionPixelSize7 = typedArrayObtainStyledAttributes.getDimensionPixelSize(com.support.tips.R.styleable.COUIToolTips_couiToolTipsViewportOffsetStart, 0);
        int dimensionPixelSize8 = typedArrayObtainStyledAttributes.getDimensionPixelSize(com.support.tips.R.styleable.COUIToolTips_couiToolTipsViewportOffsetTop, 0);
        int dimensionPixelSize9 = typedArrayObtainStyledAttributes.getDimensionPixelSize(com.support.tips.R.styleable.COUIToolTips_couiToolTipsViewportOffsetEnd, 0);
        int dimensionPixelSize10 = typedArrayObtainStyledAttributes.getDimensionPixelSize(com.support.tips.R.styleable.COUIToolTips_couiToolTipsViewportOffsetBottom, 0);
        typedArrayObtainStyledAttributes.recycle();
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this.mContext).inflate(this.mBubbleStyle.getLayoutId(), (ViewGroup) null);
        this.mMainPanel = viewGroup;
        viewGroup.setMinimumWidth(dimensionPixelSize6);
        setBackground(color, dimensionPixelSize, dimensionPixelSize2, dimensionPixelSize3, dimensionPixelSize4);
        ViewGroup viewGroupCreateContentContainer = createContentContainer(this.mContext);
        this.mContentContainer = viewGroupCreateContentContainer;
        COUIDarkModeUtil.setForceDarkAllow(viewGroupCreateContentContainer, false);
        this.mBubbleStyle.initBubbleStyle(this.mMainPanel);
        if (isLayoutRtl(this.mMainPanel)) {
            this.mViewportOffset = new Rect(dimensionPixelSize9, dimensionPixelSize8, dimensionPixelSize7, dimensionPixelSize10);
        } else {
            this.mViewportOffset = new Rect(dimensionPixelSize7, dimensionPixelSize8, dimensionPixelSize9, dimensionPixelSize10);
        }
        setClippingEnabled(false);
        setAnimationStyle(0);
        setElevation(this.mContext.getResources().getDimensionPixelOffset(R.dimen.support_shadow_size_level_four));
        setOnDismissListener(this.mOnPopupWindowDismissListener);
        ImageView imageView = this.mArrowView;
        if (imageView != null) {
            imageView.setBackground(this.mArrowUpDrawable);
        }
        this.mIsOutLineBackgroundInPopupWindow = false;
        setElevationInPopupwindow(false);
        int dimensionPixelSize11 = this.mContext.getResources().getDimensionPixelSize(com.support.tips.R.dimen.coui_tooltips_vertical_and_horizontal_gap_top) + dimensionPixelSize5;
        int dimensionPixelSize12 = this.mContext.getResources().getDimensionPixelSize(com.support.tips.R.dimen.coui_tooltips_vertical_and_horizontal_gap_bottom) + dimensionPixelSize5;
        addSpacingControlUtil(dimensionPixelSize11, WindowSpacingControlHelper.AnchorViewTypeEnum.TOP);
        addSpacingControlUtil(dimensionPixelSize12, WindowSpacingControlHelper.AnchorViewTypeEnum.BOTTOM);
        addSpacingControlUtil(dimensionPixelSize5, WindowSpacingControlHelper.AnchorViewTypeEnum.START);
        addSpacingControlUtil(dimensionPixelSize5, WindowSpacingControlHelper.AnchorViewTypeEnum.END);
        addSpacingControlUtil(this.mContext.getResources().getDimensionPixelSize(com.support.tips.R.dimen.coui_tooltips_navigation_margin), WindowSpacingControlHelper.AnchorViewTypeEnum.NAVIGATION);
        addSpacingControlUtil(this.mContext.getResources().getDimensionPixelSize(com.support.tips.R.dimen.coui_tooltips_toolbar_margin), WindowSpacingControlHelper.AnchorViewTypeEnum.TOOLBAR);
        this.mIndicatorEmbedDistance = this.mContext.getResources().getDimensionPixelSize(com.support.tips.R.dimen.coui_tooltips_indicator_embed_distance);
        this.mBubbleHorizontalPadding = this.mContext.getResources().getDimensionPixelSize(com.support.tips.R.dimen.coui_tooltips_bubble_horizontal_padding);
        this.mEdgeExtraDistance = this.mContext.getResources().getDimensionPixelSize(com.support.tips.R.dimen.coui_tooltips_edge_extra_distance);
        this.mRadius17dpForOS16_1 = this.mContext.getResources().getDimensionPixelSize(com.support.tips.R.dimen.coui_tooltips_os_16_1_radius_17_dp);
    }

    @Override // com.coui.appcompat.poplist.COUIPopupWindow
    public void initPopupWindowBackground(Context context, TypedArray typedArray) {
        setBackgroundDrawable(new ColorDrawable(0));
    }

    public boolean isLayoutRtl(View view) {
        return view.getLayoutDirection() == 1;
    }

    public void refresh() {
        View view;
        int i3 = this.mBubbleStyle.getDefStyleParams()[0];
        int i4 = this.mBubbleStyle.getDefStyleParams()[1];
        if (this.mBubbleStyle instanceof COUIDefaultBubbleStyleImpl) {
            this.mArrowUpDrawable = ContextCompat.getDrawable(this.mContext, this.mMode == 0 ? com.support.tips.R.drawable.coui_blue_sharp_middle_arrow_up : com.support.tips.R.drawable.coui_white_sharp_middle_arrow_up);
            this.mArrowUpRightDrawable = ContextCompat.getDrawable(this.mContext, this.mMode == 0 ? com.support.tips.R.drawable.coui_blue_sharp_edges_arrow_up_right : com.support.tips.R.drawable.coui_white_sharp_edges_arrow_up_right);
        } else {
            this.mArrowUpDrawable = ContextCompat.getDrawable(this.mContext, com.support.tips.R.drawable.coui_white_sharp_middle_arrow_up);
            this.mArrowUpRightDrawable = ContextCompat.getDrawable(this.mContext, com.support.tips.R.drawable.coui_white_sharp_edges_arrow_up_right);
        }
        TypedArray typedArrayObtainStyledAttributes = this.mContext.obtainStyledAttributes(null, com.support.tips.R.styleable.COUIToolTips, i3, i4);
        int color = typedArrayObtainStyledAttributes.getColor(com.support.tips.R.styleable.COUIToolTips_couiToolTipsBackgroundColor, 0);
        int dimensionPixelSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(com.support.tips.R.styleable.COUIToolTips_couiToolTipsPaddingStart, -1);
        int dimensionPixelSize2 = typedArrayObtainStyledAttributes.getDimensionPixelSize(com.support.tips.R.styleable.COUIToolTips_couiToolTipsPaddingTop, -1);
        int dimensionPixelSize3 = typedArrayObtainStyledAttributes.getDimensionPixelSize(com.support.tips.R.styleable.COUIToolTips_couiToolTipsPaddingEnd, -1);
        int dimensionPixelSize4 = typedArrayObtainStyledAttributes.getDimensionPixelSize(com.support.tips.R.styleable.COUIToolTips_couiToolTipsPaddingBottom, -1);
        this.mArrowOverflow = typedArrayObtainStyledAttributes.getDimensionPixelSize(com.support.tips.R.styleable.COUIToolTips_couiToolTipsArrowOverflowOffset, 0);
        ColorStateList colorStateList = typedArrayObtainStyledAttributes.getColorStateList(com.support.tips.R.styleable.COUIToolTips_couiToolTipsContentTextColor);
        typedArrayObtainStyledAttributes.recycle();
        int height = this.mMainPanel.getHeight();
        setBackground(color, dimensionPixelSize, dimensionPixelSize2, dimensionPixelSize3, dimensionPixelSize4);
        this.mBubbleStyle.refreshBubbleStyle(colorStateList);
        this.mBubbleStyle.refreshTextResources();
        setArrowViewDrawable();
        ViewGroup.LayoutParams layoutParams = this.mMainPanel.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = -2;
            this.mMainPanel.setLayoutParams(layoutParams);
        }
        this.mMainPanel.measure(0, 0);
        if (height == this.mMainPanel.getMeasuredHeight() || !isShowing() || (view = this.mAnchor) == null) {
            return;
        }
        view.post(new a(this));
    }

    public void refreshWhileLayoutChange() {
        Activity activityContextToActivity = UIUtil.contextToActivity(this.mContext);
        if (activityContextToActivity != null && (activityContextToActivity.isFinishing() || activityContextToActivity.isDestroyed())) {
            COUILog.e(TAG, "activity is not active when refreshWhileLayoutChange");
            return;
        }
        View view = this.mAnchor;
        if (view == null || !UIUtil.isInVisibleRect(view)) {
            dismissImmediately();
            COUILog.e(TAG, "mAnchor is now visible, so dismiss it.");
        } else {
            calculatePosition();
            Point point = this.mCoordsOnWindow;
            update(point.x, point.y, getWidth(), getHeight());
        }
    }

    public void setArrowOverflow(int i3) {
        this.mArrowOverflow = i3;
    }

    public void setBackgroundColor(int i3) {
        setBackgroundColor(ColorStateList.valueOf(i3));
    }

    public void setContent(CharSequence charSequence) {
        this.mBubbleStyle.setContentText(charSequence);
    }

    public void setContentRes(int i3) {
        this.mBubbleStyle.setContentTextRes(i3);
    }

    @Deprecated
    public void setContentTextColor(int i3) {
        setContentTextColor(ColorStateList.valueOf(i3));
    }

    @Deprecated
    public void setDelay(int i3) {
    }

    public void setDismissOnTouchOutside(boolean z2) {
        if (z2) {
            setTouchable(true);
            setFocusable(true);
            setOutsideTouchable(true);
        } else {
            setFocusable(false);
            setOutsideTouchable(false);
        }
        update();
    }

    public void setDismissTextRes(int i3) {
        this.mBubbleStyle.setDismissTextRes(i3);
    }

    public void setOnCloseIconClickListener(OnCloseIconClickListener onCloseIconClickListener) {
        this.mOnCloseIconClickListener = onCloseIconClickListener;
    }

    public void setTitleRes(int i3) {
        this.mBubbleStyle.setTitleRes(i3);
    }

    public void show(View view) {
        show(view, true);
    }

    public void showWithDirection(View view, int i3) {
        showWithDirection(view, i3, true);
    }

    @Deprecated
    public COUIToolTips(Window window, int i3) {
        this(window.getContext(), i3);
    }

    public void setBackgroundColor(ColorStateList colorStateList) {
        ViewGroup viewGroup = this.mMainPanel;
        if (viewGroup != null) {
            this.mMainPanel.setBackgroundColor(colorStateList.getColorForState(viewGroup.getDrawableState(), 0));
        }
        ImageView imageView = this.mArrowView;
        if (imageView != null) {
            Drawable background = imageView.getBackground();
            if ((background instanceof VectorDrawable) || (background instanceof BitmapDrawable)) {
                DrawableCompat.setTintList(DrawableCompat.wrap(background), colorStateList);
                this.mArrowView.invalidate();
            }
        }
    }

    public void setContent(View view) {
        this.mBubbleStyle.setContentView(view);
    }

    @Deprecated
    public void setContentTextColor(ColorStateList colorStateList) {
        this.mBubbleStyle.setContentTextColor(colorStateList);
    }

    public void show(View view, boolean z2) {
        showWithDirection(view, 4, z2);
    }

    public void showWithDirection(View view, int i3, boolean z2) {
        showWithDirection(view, i3, z2, 0, 0);
    }

    public COUIToolTips(Context context) {
        this(context, 0);
    }

    public void showWithDirection(View view, int i3, boolean z2, int i4, int i5) {
        showWithDirection(view, i3, z2, i4, i5, false);
    }

    public COUIToolTips(Context context, int i3) {
        this(context, i3, new COUIDefaultBubbleStyleImpl.Builder().build());
    }

    public void showWithDirection(View view, int i3, boolean z2, int i4, int i5, boolean z3) {
        if (isShowing()) {
            return;
        }
        if (this.mIfNeedResetTouchableToTrue) {
            setTouchable(true);
            update();
            this.mIfNeedResetTouchableToTrue = false;
        }
        WindowSpacingControlHelper.AnchorViewTypeEnum anchorViewTypeEnum = this.mWindowSpacingControlHelper.getAnchorViewTypeEnum(view);
        if (anchorViewTypeEnum == WindowSpacingControlHelper.AnchorViewTypeEnum.TOOLBAR) {
            this.mArrowOverflow = getAnchorViewSpacing(anchorViewTypeEnum);
        } else if (anchorViewTypeEnum == WindowSpacingControlHelper.AnchorViewTypeEnum.NAVIGATION) {
            this.mArrowOverflow = getAnchorViewSpacing(view, anchorViewTypeEnum);
        }
        this.mParent = view.getRootView();
        this.mHasIndicator = z2;
        this.mIndicatorFollowContent = z3;
        this.mOffsetY = i5;
        this.mOffsetX = i4;
        this.mShowDirection = i3;
        if (i3 == 32 || i3 == 64) {
            if (isLayoutRtl(view)) {
                this.mShowDirection = this.mShowDirection == 32 ? 8 : 16;
            } else {
                this.mShowDirection = this.mShowDirection != 32 ? 8 : 16;
            }
        } else if (i3 == 256) {
            this.mShowDirection = 4;
        }
        this.mAnchor = view;
        try {
            showToolTips();
        } catch (Exception e3) {
            Log.e(TAG, "showToolTips fail,e:" + e3.getMessage());
        }
        this.mContentContainer.removeCallbacks(this.mCheckAnimateExit);
    }

    public COUIToolTips(Context context, COUIIBubbleStyle cOUIIBubbleStyle) {
        this(context, 0, cOUIIBubbleStyle);
    }

    public COUIToolTips(Context context, int i3, COUIIBubbleStyle cOUIIBubbleStyle) {
        super(context);
        this.mTmpCoords = new int[2];
        this.mCoordsOnWindow = new Point();
        this.mIfNeedResetTouchableToTrue = false;
        this.mToolTipsScaleProperty = new AnonymousClass1("toolTipsScaleProperty");
        this.mToolTipsAlphaProperty = new AnonymousClass2("toolTipsAlphaProperty");
        this.mViewPortOnScreen = new Rect();
        this.mArrowDirection = 2;
        this.mShowDirection = 4;
        this.mWindowLocationOnScreen = new int[2];
        this.mArrowType = -1;
        this.mToolTipsAnimationScaleProgress = 0.0f;
        this.mToolTipsAnimationAlphaProgress = 0.0f;
        this.mOnLayoutChangeListener = new AnonymousClass3();
        this.mOnPopupWindowDismissListener = new AnonymousClass4();
        this.mAction = new AnonymousClass5();
        this.mCheckAnimateExit = new AnonymousClass6();
        this.mContext = context;
        this.mBubbleStyle = cOUIIBubbleStyle;
        cOUIIBubbleStyle.setToolTipsAction(this.mAction, context, i3);
        init(i3);
    }
}