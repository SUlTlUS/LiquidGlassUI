package com.coui.appcompat.panel;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.WindowInsets;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.grid.COUIPercentWidthFrameLayout;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.roundRect.COUIShapePath;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.coui.appcompat.uiutil.UIUtil;
import com.coui.appcompat.version.COUIVersionUtil;
import com.oplus.graphics.OplusOutline;
import com.oplus.graphics.OplusOutlineAdapter;
import com.support.panel.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIPanelPercentFrameLayout extends COUIPercentWidthFrameLayout {
    private static final int MAX_ALPHA = 255;
    public static final float MEDIUM_AND_LARGE_SCREEN = 2.0f;
    public static final float SMALL_SCREEN = 1.0f;
    private static final String TAG = "COUIPanelPercentFrameLayout";
    private static final int UNSET_WIDTH = -1;
    private Bitmap mBitmap;
    private int mBottomDiff;
    private final Paint mClipPaint;
    private boolean mHasAnchor;
    private boolean mIsHandlePanel;
    protected boolean mIsInTinyScreen;
    private boolean mIsSupportSmoothRoundCorner;
    private int mMaxHeight;
    private int mMaxHeightOfAttr;
    private int mMaxWidth;
    private final Rect mMeasureRect;
    private Rect mOplusOutLineRect;
    private ViewOutlineProvider mOutlineProvider;
    private int mPanelMaxWidthPx;
    private final Path mPath;
    private int mPreferWidth;
    private float mRadius;
    private float mRatio;
    private final RectF mRectF;
    private Drawable mShadowEdgeDrawable;
    private boolean mUseBackgroundBlur;
    private boolean mUseNormalSmoothCorner;
    private float mWeight;
    private float mWeightForOS16_1;

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIPanelPercentFrameLayout$1, reason: invalid class name */
    public class AnonymousClass1 extends ViewOutlineProvider {
        public AnonymousClass1() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            COUIPanelPercentFrameLayout.access$000(COUIPanelPercentFrameLayout.this);
            int iAccess$100 = COUIPanelPercentFrameLayout.access$100(COUIPanelPercentFrameLayout.this);
            int height = view.getHeight() + COUIPanelPercentFrameLayout.access$200(COUIPanelPercentFrameLayout.this);
            if (!COUIPanelPercentFrameLayout.access$300(COUIPanelPercentFrameLayout.this)) {
                outline.setRoundRect(0, 0, view.getWidth(), height + iAccess$100, COUIPanelPercentFrameLayout.access$400(COUIPanelPercentFrameLayout.this));
                return;
            }
            if (RoundCornerUtil.getSmoothStyleType() == 0) {
                new OplusOutline(outline).setSmoothRoundRect(0, 0, view.getWidth(), height + iAccess$100, COUIPanelPercentFrameLayout.access$400(COUIPanelPercentFrameLayout.this), COUIPanelPercentFrameLayout.access$500(COUIPanelPercentFrameLayout.this));
                return;
            }
            if (RoundCornerUtil.getSmoothStyleType() == 1) {
                OplusOutlineAdapter oplusOutlineAdapter = new OplusOutlineAdapter(outline, RoundCornerUtil.getSmoothStyleType());
                COUIPanelPercentFrameLayout.access$600(COUIPanelPercentFrameLayout.this).left = 0;
                COUIPanelPercentFrameLayout.access$600(COUIPanelPercentFrameLayout.this).top = 0;
                COUIPanelPercentFrameLayout.access$600(COUIPanelPercentFrameLayout.this).right = view.getWidth();
                COUIPanelPercentFrameLayout.access$600(COUIPanelPercentFrameLayout.this).bottom = height + iAccess$100;
                if (COUIVersionUtil.getOSVersionCode() > 37) {
                    oplusOutlineAdapter.setSmoothRoundRect(COUIPanelPercentFrameLayout.access$600(COUIPanelPercentFrameLayout.this), COUIPanelPercentFrameLayout.access$400(COUIPanelPercentFrameLayout.this), COUIPanelPercentFrameLayout.access$500(COUIPanelPercentFrameLayout.this));
                } else {
                    oplusOutlineAdapter.setSmoothRoundRect(COUIPanelPercentFrameLayout.access$600(COUIPanelPercentFrameLayout.this), COUIPanelPercentFrameLayout.access$400(COUIPanelPercentFrameLayout.this));
                }
            }
        }
    }

    public COUIPanelPercentFrameLayout(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void access$000(COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout) {
        cOUIPanelPercentFrameLayout.enforceChangeScreenWidth();
    }

    public static /* synthetic */ int access$100(COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout) {
        return cOUIPanelPercentFrameLayout.updateBottomCornerRadius();
    }

    public static /* synthetic */ int access$200(COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout) {
        return cOUIPanelPercentFrameLayout.mBottomDiff;
    }

    public static /* synthetic */ boolean access$300(COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout) {
        return cOUIPanelPercentFrameLayout.mIsSupportSmoothRoundCorner;
    }

    public static /* synthetic */ float access$400(COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout) {
        return cOUIPanelPercentFrameLayout.mRadius;
    }

    public static /* synthetic */ float access$500(COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout) {
        return cOUIPanelPercentFrameLayout.mWeight;
    }

    public static /* synthetic */ Rect access$600(COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout) {
        return cOUIPanelPercentFrameLayout.mOplusOutLineRect;
    }

    private Bitmap createClipSmoothRoundBitmap() {
        if (this.mRectF.width() <= 0.0f || this.mRectF.height() <= 0.0f) {
            COUILog.i(TAG, "createClipSmoothRoundBitmap return for width and height must be > 0");
            return null;
        }
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap((int) this.mRectF.width(), (int) this.mRectF.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        Paint paint = new Paint(1);
        paint.setColor(-1);
        canvas.drawPath(this.mPath, paint);
        return bitmapCreateBitmap;
    }

    private void enforceChangeScreenWidth() {
        if (this.mPreferWidth == -1) {
            return;
        }
        try {
            Resources resources = getContext().getResources();
            Configuration configuration = resources.getConfiguration();
            int i3 = configuration.screenWidthDp;
            int i4 = this.mPreferWidth;
            if (i3 == i4) {
                return;
            }
            configuration.screenWidthDp = i4;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            Log.d(TAG, "enforceChangeScreenWidth : PreferWidth:" + this.mPreferWidth);
        } catch (Exception unused) {
            Log.d(TAG, "enforceChangeScreenWidth : failed to updateConfiguration");
        }
    }

    private void initAttr(AttributeSet attributeSet) {
        if (getContext() != null) {
            TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.COUIPanelPercentFrameLayout);
            this.mMaxHeight = typedArrayObtainStyledAttributes.getDimensionPixelOffset(R.styleable.COUIPanelPercentFrameLayout_maxPanelHeight, 0);
            typedArrayObtainStyledAttributes.recycle();
        }
        this.mMaxHeightOfAttr = this.mMaxHeight;
        this.mWeightForOS16_1 = getResources().getFloat(com.support.appcompat.R.dimen.weight_16_1_large);
        this.mPanelMaxWidthPx = getResources().getDimensionPixelSize(R.dimen.coui_panel_max_width);
        this.mRatio = COUIPanelMultiWindowUtils.isSmallScreen(getContext(), null) ? 1.0f : 2.0f;
        initRadiusAndWeight();
    }

    private void initRadiusAndWeight() {
        boolean z2 = RoundCornerUtil.isPathSupportSingleCorner() && RoundCornerUtil.isSmoothRoundRectOn() && !this.mUseNormalSmoothCorner;
        this.mIsSupportSmoothRoundCorner = z2;
        if (!z2) {
            this.mRadius = COUIContextUtil.getAttrDimens(getContext(), this.mIsInTinyScreen ? com.support.appcompat.R.attr.couiRoundCornerXXXL : com.support.appcompat.R.attr.couiRoundCornerXXXXL);
            this.mWeight = 0.0f;
            updateClipToOutline(true);
            return;
        }
        if (RoundCornerUtil.getSmoothStyleType() == 0) {
            if (this.mIsInTinyScreen) {
                this.mRadius = COUIContextUtil.getAttrDimens(getContext(), com.support.appcompat.R.attr.couiRoundCornerXXXLRadius);
                this.mWeight = COUIContextUtil.getAttrFloat(getContext(), com.support.appcompat.R.attr.couiRoundCornerXXXLWeight);
                return;
            } else {
                this.mRadius = COUIContextUtil.getAttrDimens(getContext(), com.support.appcompat.R.attr.couiRoundCornerXXXXLRadius);
                this.mWeight = COUIContextUtil.getAttrFloat(getContext(), com.support.appcompat.R.attr.couiRoundCornerXXXXLWeight);
                return;
            }
        }
        if (RoundCornerUtil.getSmoothStyleType() == 1) {
            updateClipToOutline(false);
            if (COUIVersionUtil.getOSVersionCode() > 37) {
                this.mRadius = getResources().getDimension(this.mIsInTinyScreen ? com.support.appcompat.R.dimen.coui_round_corner_xxxl_radius_large_16_1 : com.support.appcompat.R.dimen.coui_round_corner_xxxxl_radius_large_16_1);
                this.mWeight = this.mWeightForOS16_1;
            } else {
                this.mRadius = COUIContextUtil.getAttrDimens(getContext(), this.mIsInTinyScreen ? com.support.appcompat.R.attr.couiRoundCornerXXXL : com.support.appcompat.R.attr.couiRoundCornerXXXXL);
                this.mWeight = 0.0f;
            }
        }
    }

    private int updateBottomCornerRadius() {
        if (this.mIsHandlePanel) {
            return getContext().getResources().getDimensionPixelOffset(R.dimen.coui_bottom_sheet_bg_top_corner_radius);
        }
        int dimensionPixelOffset = getContext().getResources().getDimensionPixelOffset(R.dimen.coui_bottom_sheet_bg_bottom_corner_radius);
        Activity activityContextToActivity = UIUtil.contextToActivity(getContext());
        if (activityContextToActivity != null) {
            int requestedOrientation = activityContextToActivity.getRequestedOrientation();
            if (requestedOrientation == 1 && (activityContextToActivity.getResources().getConfiguration().screenLayout & 48) == 32) {
                return getContext().getResources().getDimensionPixelOffset(R.dimen.coui_bottom_sheet_bg_top_corner_radius);
            }
            if (requestedOrientation == 0) {
                return 0;
            }
        }
        return dimensionPixelOffset;
    }

    private void updatePath() {
        this.mPath.reset();
        if (updateBottomCornerRadius() == 0) {
            COUIShapePath.getSmoothRoundRectPath(this.mPath, this.mRectF, this.mRadius, this.mWeight);
        } else {
            COUIShapePath.getSmoothRoundRectPath(this.mPath, this.mRectF, this.mRadius, this.mWeight, true, true, false, false);
        }
    }

    public void delPreferWidth() {
        this.mPreferWidth = -1;
        Log.d(TAG, "delPreferWidth");
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        if (getClipToOutline()) {
            super.draw(canvas);
            Drawable drawable = this.mShadowEdgeDrawable;
            if (drawable != null) {
                drawable.draw(canvas);
                return;
            }
            return;
        }
        if (this.mBitmap == null || COUIVersionUtil.getOSVersionCode() >= 37) {
            canvas.save();
            canvas.clipPath(this.mPath);
            super.draw(canvas);
            canvas.restore();
        } else {
            int iSaveLayer = canvas.saveLayer(null, null);
            super.draw(canvas);
            canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, this.mClipPaint);
            canvas.restoreToCount(iSaveLayer);
        }
        Drawable drawable2 = this.mShadowEdgeDrawable;
        if (drawable2 != null) {
            drawable2.draw(canvas);
        }
    }

    public int getGridNumber() {
        return this.mGridNumber;
    }

    public boolean getHasAnchor() {
        return this.mHasAnchor;
    }

    public int getMaxHeight() {
        return this.mMaxHeight;
    }

    public int getPaddingSize() {
        return this.mPaddingSize;
    }

    public int getPaddingType() {
        return this.mPaddingType;
    }

    public float getRatio() {
        if (this.mIsHandlePanel) {
            return 1.0f;
        }
        return this.mRatio;
    }

    public boolean isIsHandlePanel() {
        return this.mIsHandlePanel;
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        int paddingBottom = getPaddingBottom();
        WindowInsets windowInsetsOnApplyWindowInsets = super.onApplyWindowInsets(windowInsets);
        if (paddingBottom != 0 && getPaddingBottom() == 0) {
            setPaddingRelative(getPaddingStart(), getPaddingTop(), getPaddingEnd(), paddingBottom);
        }
        return windowInsetsOnApplyWindowInsets;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mRatio = COUIPanelMultiWindowUtils.isSmallScreen(getContext(), null) ? 1.0f : 2.0f;
    }

    @Override // com.coui.appcompat.grid.COUIPercentWidthFrameLayout, android.widget.FrameLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        getWindowVisibleDisplayFrame(this.mMeasureRect);
        int iHeight = this.mMeasureRect.height();
        int i5 = this.mMaxHeight;
        if (iHeight > i5 && i5 > 0 && i5 < View.MeasureSpec.getSize(i4)) {
            i4 = View.MeasureSpec.makeMeasureSpec(this.mMaxHeight, View.MeasureSpec.getMode(i4));
        }
        if (this.mMaxWidth != 0) {
            setPercentIndentEnabled(false);
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(this.mMaxWidth, View.MeasureSpec.getMode(i3)), i4);
            return;
        }
        setPercentIndentEnabled((COUIPanelMultiWindowUtils.isSmallScreen(getContext(), null) || View.MeasureSpec.getSize(i3) >= this.mMeasureRect.width()) && !COUIResponsiveUtils.isSmallScreen(getContext(), this.mMeasureRect.width()) && this.mMaxWidth == 0);
        super.onMeasure(i3, i4);
        if (getMeasuredWidth() > this.mPanelMaxWidthPx) {
            setPercentIndentEnabled(false);
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(this.mPanelMaxWidthPx, 1073741824), i4);
        }
    }

    @Override // android.view.View
    public void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        enforceChangeScreenWidth();
        if (getClipToOutline()) {
            return;
        }
        this.mRectF.set(0.0f, 0.0f, i3, i4);
        updatePath();
        if (this.mIsSupportSmoothRoundCorner) {
            if (!(this.mBitmap != null && i3 == i5 && i4 == i6) && COUIVersionUtil.getOSVersionCode() < 37) {
                this.mBitmap = createClipSmoothRoundBitmap();
            }
        }
    }

    public void restoreDefaultMaxSize() {
        if (this.mMaxWidth == 0) {
            return;
        }
        this.mMaxWidth = 0;
        this.mMaxHeight = this.mMaxHeightOfAttr;
        requestLayout();
    }

    @Override // android.view.View
    public void setAlpha(float f3) {
        super.setAlpha(f3);
        if (!this.mUseBackgroundBlur || getBackground() == null) {
            return;
        }
        getBackground().setAlpha((int) (f3 * 255.0f));
    }

    public void setHasAnchor(boolean z2) {
        if (this.mHasAnchor != z2) {
            this.mHasAnchor = z2;
            updateClipToOutline(z2);
        }
    }

    public void setIsHandlePanel(boolean z2) {
        this.mIsHandlePanel = z2;
    }

    public void setIsInTinyScreen(boolean z2) {
        if (this.mIsInTinyScreen != z2) {
            this.mIsInTinyScreen = z2;
            initRadiusAndWeight();
        }
    }

    public void setMaxHeight(int i3) {
        if (this.mMaxHeight != i3) {
            this.mMaxHeight = i3;
            requestLayout();
        }
    }

    public void setMaxSize(int i3, int i4) {
        if (i4 == this.mMaxHeight && i3 == this.mMaxWidth) {
            return;
        }
        this.mMaxWidth = i3;
        this.mMaxHeight = i4;
        requestLayout();
    }

    public void setOutlineBottomOffset(int i3) {
        this.mBottomDiff = i3;
    }

    @Override // android.view.View
    public void setOutlineProvider(ViewOutlineProvider viewOutlineProvider) {
    }

    public void setPreferWidth(int i3) {
        this.mPreferWidth = i3;
        Log.d(TAG, "setPreferWidth =：" + this.mPreferWidth);
    }

    public void setShadowEdgeDrawable(Drawable drawable) {
        if (this.mShadowEdgeDrawable == drawable) {
            return;
        }
        this.mShadowEdgeDrawable = drawable;
        if (drawable != null) {
            setWillNotDraw(false);
        } else {
            setWillNotDraw(true);
        }
        invalidate();
    }

    public void setUseBackgroundBlur(boolean z2) {
        this.mUseBackgroundBlur = z2;
    }

    public void setUseNormalSmoothCorner(boolean z2) {
        if (this.mUseNormalSmoothCorner != z2) {
            this.mUseNormalSmoothCorner = z2;
            initRadiusAndWeight();
        }
    }

    public void updateClipToOutline(boolean z2) {
        COUILog.i(TAG, "updateClipToOutline setOutlineProvider=" + z2);
        if (z2) {
            super.setOutlineProvider(this.mOutlineProvider);
            setClipToOutline(true);
        } else {
            super.setOutlineProvider(null);
            setClipToOutline(false);
        }
    }

    public void updateLayoutWhileConfigChange(Configuration configuration) {
        this.mRatio = COUIPanelMultiWindowUtils.isSmallScreen(getContext(), configuration) ? 1.0f : 2.0f;
    }

    @Override // android.view.View
    public boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.mShadowEdgeDrawable;
    }

    public COUIPanelPercentFrameLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIPanelPercentFrameLayout(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.mPath = new Path();
        this.mRectF = new RectF();
        Paint paint = new Paint(1);
        this.mClipPaint = paint;
        this.mRatio = 1.0f;
        this.mHasAnchor = false;
        this.mPreferWidth = -1;
        this.mIsSupportSmoothRoundCorner = false;
        this.mBitmap = null;
        this.mOplusOutLineRect = new Rect();
        this.mOutlineProvider = new AnonymousClass1();
        initAttr(attributeSet);
        this.mMeasureRect = new Rect();
        if (COUIVersionUtil.getOSVersionCode() < 37) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        }
    }
}