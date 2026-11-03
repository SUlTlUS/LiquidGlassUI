package com.coui.appcompat.bottomfloatingaccessorynavigationbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import com.coui.appcompat.COUIMaterialEffect;
import com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingItem;
import com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView;
import com.coui.appcompat.bottomfloatingnavigationbar.COUIBottomFloatingNavigationBarItem;
import com.coui.appcompat.bottomfloatingnavigationbar.COUIBottomFloatingNavigationBarMenuView;
import com.coui.appcompat.bottomfloatingnavigationbar.MenuXmlParserUtils;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.coui.appcompat.uiutil.UIUtil;
import com.oplus.view.material.OplusMaterialMetaBallParams;
import com.support.bottomnavigation.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes7.dex */
public class COUIBottomFloatingAccessoryNavigationBar extends ViewGroup implements AccessorySideExpansionController.Host {
    private static final int MELT_BALL_ACCESSORY_VIEW_INDEX = 2;
    private static final float MELT_BALL_DRAW_ORDER_ELEVATION = 10000.0f;
    private static final float MELT_BALL_EXTENSION_FACTOR_X = 1.5f;
    private static final float MELT_BALL_EXTENSION_FACTOR_Y = 2.0f;
    private static final int MELT_BALL_MENU_VIEW_INDEX = 1;
    private static final int MELT_BALL_MODE = 0;
    private static final int SHADOW_CLIP_EXPAND_SIZE_PX = 120;
    private final int mAccessorySize;
    private final COUIBottomFloatingAccessoryView mEndAccessoryView;
    private final AccessorySideExpansionController mEndExpansionController;
    private int mLastOrientation;
    private int mLastScreenWidthDp;
    private ArraySet<View> mMeltBallChainViews;
    private final int mMeltBallParamExtension;
    private final MenuBuilder mMenuBuilder;
    private MenuInflater mMenuInflater;
    private final COUIBottomFloatingNavigationBarMenuView mMenuView;
    private final int mMinHeightWithoutPaddingVertical;
    private final ViewOutlineProvider mNoDropShadowOutline;
    private final int[] mOnLayoutChildLefts;
    private final int mPaddingHorizontal;
    private final int mSideViewSpacing;
    private boolean mSkipNextLayoutAnimation;
    private final COUIBottomFloatingAccessoryView mStartAccessoryView;
    private final AccessorySideExpansionController mStartExpansionController;

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingaccessorynavigationbar.COUIBottomFloatingAccessoryNavigationBar$1, reason: invalid class name */
    public class AnonymousClass1 extends ViewOutlineProvider {
        public AnonymousClass1() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setEmpty();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingaccessorynavigationbar.COUIBottomFloatingAccessoryNavigationBar$2, reason: invalid class name */
    public class AnonymousClass2 implements AccessorySideExpansionController.AccessoryAlphaDelegate {
        public AnonymousClass2() {
        }

        @Override // com.coui.appcompat.bottomfloatingaccessorynavigationbar.AccessorySideExpansionController.AccessoryAlphaDelegate
        public float getAccessoryViewAlpha(View view) {
            return COUIBottomFloatingAccessoryNavigationBar.this.getAccessoryViewAlpha(view);
        }

        @Override // com.coui.appcompat.bottomfloatingaccessorynavigationbar.AccessorySideExpansionController.AccessoryAlphaDelegate
        public void setAccessoryViewAlpha(View view, float f3) {
            COUIBottomFloatingAccessoryNavigationBar.this.setAccessoryViewAlpha(view, f3);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingaccessorynavigationbar.COUIBottomFloatingAccessoryNavigationBar$3, reason: invalid class name */
    public class AnonymousClass3 implements BaseBottomFloatingMenuView.OnItemClickListener {
        final /* synthetic */ OnItemSelectedListener val$listener;

        public AnonymousClass3(OnItemSelectedListener onItemSelectedListener) {
            this.val$listener = onItemSelectedListener;
        }

        @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView.OnItemClickListener
        public void onItemClick(View view, BaseBottomFloatingItem baseBottomFloatingItem, int i3) {
            OnItemSelectedListener onItemSelectedListener = this.val$listener;
            if (onItemSelectedListener == null || !(baseBottomFloatingItem instanceof COUIBottomFloatingNavigationBarItem)) {
                return;
            }
            onItemSelectedListener.onNavigationItemSelected(view, (COUIBottomFloatingNavigationBarItem) baseBottomFloatingItem, i3);
        }
    }

    public interface OnAccessoryClickListener {
        void onAccessoryClick(View view);
    }

    public interface OnItemSelectedListener {
        void onNavigationItemSelected(View view, COUIBottomFloatingNavigationBarItem cOUIBottomFloatingNavigationBarItem, int i3);
    }

    public COUIBottomFloatingAccessoryNavigationBar(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void a(COUIBottomFloatingAccessoryNavigationBar cOUIBottomFloatingAccessoryNavigationBar) {
        cOUIBottomFloatingAccessoryNavigationBar.onMenuDrawingBoundsChangedForAlpha();
    }

    private void applyExpansionViewMeltBallState(boolean z2) {
        boolean z3 = this.mStartExpansionController.isActive() || this.mEndExpansionController.isActive();
        if (isSupportMeltBall() && (z3 || z2)) {
            setMeltBallDrawOrder();
            setMeltBallParams();
        } else {
            clearMeltBallParams();
            resetMeltBallDrawOrder();
        }
    }

    public static /* synthetic */ void b(OnAccessoryClickListener onAccessoryClickListener, View view) {
        onAccessoryClickListener.onAccessoryClick(view);
    }

    public static /* synthetic */ boolean c(COUIBottomFloatingAccessoryNavigationBar cOUIBottomFloatingAccessoryNavigationBar) {
        return cOUIBottomFloatingAccessoryNavigationBar.isSupportMeltBall();
    }

    private void changePadding(int i3) {
        int i4 = COUIResponsiveUtils.isSmallScreen(getContext(), i3) ? this.mPaddingHorizontal : 0;
        if (i4 == getPaddingLeft() && i4 == getPaddingRight()) {
            return;
        }
        setPadding(i4, getPaddingTop(), i4, getPaddingBottom());
    }

    private void clearMeltBallParams() {
        COUIMaterialEffect.setMetaBallParams(this.mStartAccessoryView, null);
        COUIMaterialEffect.setMetaBallParams(this.mEndAccessoryView, null);
        COUIMaterialEffect.setMetaBallParams(this.mMenuView, null);
    }

    private int computeSideReserveForConfiguredIcons(boolean z2, boolean z3) {
        int i3 = z2 ? this.mAccessorySize + this.mSideViewSpacing : 0;
        return z3 ? i3 + this.mAccessorySize + this.mSideViewSpacing : i3;
    }

    private AccessorySideExpansionController.AccessoryAlphaDelegate createAccessoryAlphaDelegate() {
        return new AnonymousClass2();
    }

    public static /* synthetic */ boolean d(COUIBottomFloatingAccessoryNavigationBar cOUIBottomFloatingAccessoryNavigationBar) {
        return cOUIBottomFloatingAccessoryNavigationBar.isSupportMeltBall();
    }

    public static /* synthetic */ void e(OnAccessoryClickListener onAccessoryClickListener, View view) {
        onAccessoryClickListener.onAccessoryClick(view);
    }

    private MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            this.mMenuInflater = new MenuInflater(getContext());
        }
        return this.mMenuInflater;
    }

    private boolean isAccessoryIconConfigured(COUIBottomFloatingAccessoryView cOUIBottomFloatingAccessoryView) {
        return cOUIBottomFloatingAccessoryView.getIconDrawable() != null;
    }

    private boolean isAccessoryVisible(COUIBottomFloatingAccessoryView cOUIBottomFloatingAccessoryView) {
        if (!isAccessoryIconConfigured(cOUIBottomFloatingAccessoryView)) {
            return false;
        }
        if (cOUIBottomFloatingAccessoryView == this.mStartAccessoryView) {
            return this.mStartExpansionController.isActive();
        }
        if (cOUIBottomFloatingAccessoryView == this.mEndAccessoryView) {
            return this.mEndExpansionController.isActive();
        }
        return false;
    }

    private void layoutAccessoryIfVisible(COUIBottomFloatingAccessoryView cOUIBottomFloatingAccessoryView, int i3, int i4, int i5, int i6, boolean z2, boolean z3) {
        if (isAccessoryVisible(cOUIBottomFloatingAccessoryView)) {
            int i7 = this.mAccessorySize;
            int i8 = i4 + (((i6 - i4) - i7) / 2);
            if (!z2 ? !z3 : z3) {
                i3 = i5 - i7;
            }
            layoutAccessoryView(cOUIBottomFloatingAccessoryView, i3, i8, i7 + i8);
        }
    }

    private void layoutAccessoryView(COUIBottomFloatingAccessoryView cOUIBottomFloatingAccessoryView, int i3, int i4, int i5) {
        if (isAccessoryVisible(cOUIBottomFloatingAccessoryView)) {
            cOUIBottomFloatingAccessoryView.layout(i3, i4, this.mAccessorySize + i3, i5);
        }
    }

    /* JADX WARN: Code duplicated, block: B:22:0x0047 A[PHI: r5 r8
  0x0047: PHI (r5v4 int) = (r5v2 int), (r5v9 int) binds: [B:20:0x0041, B:14:0x0029] A[DONT_GENERATE, DONT_INLINE]
  0x0047: PHI (r8v7 int) = (r8v2 int), (r8v10 int) binds: [B:20:0x0041, B:14:0x0029] A[DONT_GENERATE, DONT_INLINE]] */
    private void layoutPositionsMediumScreen(int i3, int i4, int i5, boolean z2, boolean z3, boolean z4) {
        int i6;
        int i7;
        int i8 = z3 ? this.mAccessorySize + this.mSideViewSpacing + i5 : i5;
        if (z4) {
            i8 += this.mSideViewSpacing + this.mAccessorySize;
        }
        int i9 = i3 + ((i4 - i8) / 2);
        if (z2) {
            int i10 = i9 + i8;
            if (z3) {
                int i11 = i10 - this.mAccessorySize;
                i6 = i11;
                i10 = i11 - this.mSideViewSpacing;
            } else {
                i6 = 0;
            }
            i9 = i10 - i5;
            if (z4) {
                i7 = (i9 - this.mSideViewSpacing) - this.mAccessorySize;
            } else {
                i7 = 0;
            }
        } else {
            if (z3) {
                i6 = i9;
                i9 = this.mAccessorySize + this.mSideViewSpacing + i9;
            } else {
                i6 = 0;
            }
            int i12 = i5 + i9;
            if (z4) {
                i7 = i12 + this.mSideViewSpacing;
            } else {
                i7 = 0;
            }
        }
        int[] iArr = this.mOnLayoutChildLefts;
        iArr[0] = i9;
        iArr[1] = i6;
        iArr[2] = i7;
    }

    private void layoutPositionsSmallScreen(int i3, int i4, int i5, boolean z2, boolean z3, boolean z4) {
        int i6;
        int i7;
        if (z2) {
            i7 = i4 - this.mAccessorySize;
            i6 = i3;
        } else {
            i6 = i4 - this.mAccessorySize;
            i7 = i3;
        }
        if (!z3) {
            i7 = 0;
        }
        if (!z4) {
            i6 = 0;
        }
        boolean z5 = z2 ? z4 : z3;
        if (!z2) {
            z3 = z4;
        }
        if (z5) {
            i3 = i3 + this.mAccessorySize + this.mSideViewSpacing;
        }
        if (z3) {
            i4 = (i4 - this.mAccessorySize) - this.mSideViewSpacing;
        }
        int iMax = i3 + ((Math.max(0, i4 - i3) - i5) / 2);
        int[] iArr = this.mOnLayoutChildLefts;
        iArr[0] = iMax;
        iArr[1] = i7;
        iArr[2] = i6;
    }

    private void onMenuDrawingBoundsChangedForAlpha() {
        this.mStartExpansionController.onGeometryChangedForAlpha();
        this.mEndExpansionController.onGeometryChangedForAlpha();
    }

    private Drawable prepareAccessoryDrawable(Drawable drawable) {
        Drawable.ConstantState constantState = drawable.getConstantState();
        return constantState != null ? constantState.newDrawable().mutate() : drawable.mutate();
    }

    private void resetMeltBallDrawOrder() {
        setElevation(0.0f);
        setOutlineProvider(ViewOutlineProvider.BACKGROUND);
    }

    private void setMeltBallDrawOrder() {
        setElevation(10000.0f);
        setOutlineProvider(this.mNoDropShadowOutline);
    }

    private void setMeltBallParams() {
        OplusMaterialMetaBallParams oplusMaterialMetaBallParams = new OplusMaterialMetaBallParams(2, 0, this.mMeltBallParamExtension, 1.5f, 2.0f);
        OplusMaterialMetaBallParams oplusMaterialMetaBallParams2 = new OplusMaterialMetaBallParams(1, 0, this.mMeltBallParamExtension, 1.5f, 2.0f);
        COUIMaterialEffect.setMetaBallParams(this.mStartAccessoryView, oplusMaterialMetaBallParams);
        COUIMaterialEffect.setMetaBallParams(this.mEndAccessoryView, oplusMaterialMetaBallParams);
        COUIMaterialEffect.setMetaBallParams(this.mMenuView, oplusMaterialMetaBallParams2);
        if (this.mMeltBallChainViews == null) {
            ArraySet<View> arraySet = new ArraySet<>(3);
            this.mMeltBallChainViews = arraySet;
            arraySet.add(this.mStartAccessoryView);
            this.mMeltBallChainViews.add(this.mEndAccessoryView);
            this.mMeltBallChainViews.add(this.mMenuView);
        }
        COUIMaterialEffect.setMetaBallChain(this.mMeltBallChainViews);
    }

    public void addItems(List<COUIBottomFloatingNavigationBarItem> list) {
        this.mMenuView.addItems(list);
    }

    @Override // com.coui.appcompat.bottomfloatingaccessorynavigationbar.AccessorySideExpansionController.Host
    public void clearSideMeltBallParams(boolean z2) {
        if (z2) {
            COUIMaterialEffect.setMetaBallParams(this.mStartAccessoryView, null);
        } else {
            COUIMaterialEffect.setMetaBallParams(this.mEndAccessoryView, null);
        }
    }

    @Override // com.coui.appcompat.bottomfloatingaccessorynavigationbar.AccessorySideExpansionController.Host
    public int getAccessorySize() {
        return this.mAccessorySize;
    }

    public float getAccessoryViewAlpha(View view) {
        if (view != null) {
            return view.getAlpha();
        }
        return 1.0f;
    }

    public COUIBottomFloatingAccessoryView getEndAccessoryView() {
        return this.mEndAccessoryView;
    }

    @Override // com.coui.appcompat.bottomfloatingaccessorynavigationbar.AccessorySideExpansionController.Host
    public RectF getMenuDrawingBounds() {
        return this.mMenuView.getDrawingBounds();
    }

    public COUIBottomFloatingNavigationBarMenuView getMenuView() {
        return this.mMenuView;
    }

    public int getSelectedItemId() {
        return this.mMenuView.getSelectedItemId();
    }

    public COUIBottomFloatingAccessoryView getStartAccessoryView() {
        return this.mStartAccessoryView;
    }

    public void hideEndAccessoryView(boolean z2) {
        if (this.mEndExpansionController.isActive()) {
            if (!z2) {
                this.mSkipNextLayoutAnimation = true;
            }
            this.mEndExpansionController.setActive(false);
            applyExpansionViewMeltBallState(z2);
            requestLayout();
            this.mEndExpansionController.applyAlphaScaleState(false, z2);
        }
    }

    public void hideStartAccessoryView(boolean z2) {
        if (this.mStartExpansionController.isActive()) {
            if (!z2) {
                this.mSkipNextLayoutAnimation = true;
            }
            this.mStartExpansionController.setActive(false);
            applyExpansionViewMeltBallState(z2);
            requestLayout();
            this.mStartExpansionController.applyAlphaScaleState(false, z2);
        }
    }

    public final void inflateMenu(int i3) {
        if (i3 == 0) {
            return;
        }
        Map<Integer, MenuXmlParserUtils.MenuItemAttrs> menuItemAttrs = MenuXmlParserUtils.parseMenuItemAttrs(getContext(), i3);
        this.mMenuBuilder.clear();
        getMenuInflater().inflate(i3, this.mMenuBuilder);
        if (this.mMenuBuilder.getNonActionItems().isEmpty()) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        COUIBottomFloatingNavigationBarItem.Builder builder = new COUIBottomFloatingNavigationBarItem.Builder();
        for (MenuItemImpl menuItemImpl : this.mMenuBuilder.getNonActionItems()) {
            int itemId = menuItemImpl.getItemId();
            MenuXmlParserUtils.MenuItemAttrs menuItemAttrs2 = menuItemAttrs.get(Integer.valueOf(itemId));
            ((COUIBottomFloatingNavigationBarItem.Builder) ((COUIBottomFloatingNavigationBarItem.Builder) ((COUIBottomFloatingNavigationBarItem.Builder) builder.reset().setId(itemId)).setTitle(menuItemImpl.getTitle() == null ? "" : menuItemImpl.getTitle().toString())).setIcon(menuItemImpl.getIcon())).setDescription(menuItemImpl.getContentDescription());
            if (menuItemAttrs2 != null) {
                ((COUIBottomFloatingNavigationBarItem.Builder) ((COUIBottomFloatingNavigationBarItem.Builder) builder.setIconResId(menuItemAttrs2.iconResId)).setLargeIcon(menuItemAttrs2.isLargeIcon).setRedDotType(menuItemAttrs2.redDotType)).setRedDotNumber(menuItemAttrs2.redDotNumber);
            }
            arrayList.add(builder.build());
        }
        addItems(arrayList);
    }

    @Override // com.coui.appcompat.bottomfloatingaccessorynavigationbar.AccessorySideExpansionController.Host
    public boolean isSupportMeltBall() {
        return this.mMenuView.isSupportMeltBall();
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        boolean z2 = this.mLastOrientation != configuration.orientation;
        boolean z3 = this.mLastScreenWidthDp != configuration.screenWidthDp;
        if (z2 || z3) {
            this.mSkipNextLayoutAnimation = true;
            this.mStartExpansionController.cancelTranslationAnimIfRunning();
            this.mEndExpansionController.cancelTranslationAnimIfRunning();
        }
        this.mLastOrientation = configuration.orientation;
        this.mLastScreenWidthDp = configuration.screenWidthDp;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int i12 = i5 - i3;
        int paddingRight = i12 - getPaddingRight();
        int paddingBottom = (i6 - i4) - getPaddingBottom();
        int i13 = paddingRight - paddingLeft;
        int i14 = paddingBottom - paddingTop;
        boolean z3 = getLayoutDirection() == 1;
        boolean zIsSmallScreen = COUIResponsiveUtils.isSmallScreen(getContext(), i12);
        if (this.mMenuView.getVisibility() == 8) {
            boolean z4 = z3;
            layoutAccessoryIfVisible(this.mStartAccessoryView, paddingLeft, paddingTop, paddingRight, paddingBottom, z4, true);
            layoutAccessoryIfVisible(this.mEndAccessoryView, paddingLeft, paddingTop, paddingRight, paddingBottom, z4, false);
            return;
        }
        int measuredWidth = this.mMenuView.getMeasuredWidth();
        int measuredHeight = this.mMenuView.getMeasuredHeight();
        int i15 = paddingTop + ((i14 - measuredHeight) / 2);
        int i16 = this.mAccessorySize;
        int i17 = paddingTop + ((i14 - i16) / 2);
        int i18 = i17 + i16;
        boolean zIsAccessoryIconConfigured = isAccessoryIconConfigured(this.mStartAccessoryView);
        boolean zIsAccessoryIconConfigured2 = isAccessoryIconConfigured(this.mEndAccessoryView);
        if (zIsAccessoryIconConfigured || zIsAccessoryIconConfigured2) {
            if (zIsSmallScreen) {
                layoutPositionsSmallScreen(paddingLeft, paddingRight, measuredWidth, z3, zIsAccessoryIconConfigured, zIsAccessoryIconConfigured2);
                int[] iArr = this.mOnLayoutChildLefts;
                i7 = iArr[0];
                i8 = iArr[1];
                i9 = iArr[2];
            } else {
                layoutPositionsMediumScreen(paddingLeft, i13, measuredWidth, z3, zIsAccessoryIconConfigured, zIsAccessoryIconConfigured2);
                int[] iArr2 = this.mOnLayoutChildLefts;
                i7 = iArr2[0];
                i8 = iArr2[1];
                i9 = iArr2[2];
            }
            int i19 = i7;
            i10 = i9;
            i11 = i19;
        } else {
            i11 = paddingLeft + ((i13 - measuredWidth) / 2);
            i10 = 0;
            i8 = 0;
        }
        int i20 = i11 + measuredWidth;
        int i21 = measuredHeight + i15;
        this.mMenuView.layout(i11, i15, i20, i21);
        int i22 = i11;
        this.mMenuView.updateRealBounds(i22, i15, i20, i21, false);
        boolean z5 = isSupportMeltBall() && !this.mSkipNextLayoutAnimation;
        if (zIsAccessoryIconConfigured) {
            this.mStartExpansionController.setVisibleLayoutLeft(i8);
            if (isSupportMeltBall()) {
                this.mStartExpansionController.computeInactiveFakeBounds(i22, measuredWidth, i17, i18, z3);
            }
            this.mStartExpansionController.layoutChildAndApplyTranslation(i17, i18, z5);
        }
        if (zIsAccessoryIconConfigured2) {
            this.mEndExpansionController.setVisibleLayoutLeft(i10);
            if (isSupportMeltBall()) {
                this.mEndExpansionController.computeInactiveFakeBounds(i22, measuredWidth, i17, i18, z3);
            }
            this.mEndExpansionController.layoutChildAndApplyTranslation(i17, i18, z5);
        }
        this.mSkipNextLayoutAnimation = false;
    }

    @Override // android.view.View
    public void onMeasure(int i3, int i4) {
        int iMax;
        int iCombineMeasuredStates;
        int size = View.MeasureSpec.getSize(i3);
        changePadding(size);
        boolean zIsAccessoryIconConfigured = isAccessoryIconConfigured(this.mStartAccessoryView);
        boolean zIsAccessoryIconConfigured2 = isAccessoryIconConfigured(this.mEndAccessoryView);
        boolean zIsAccessoryVisible = isAccessoryVisible(this.mStartAccessoryView);
        boolean zIsAccessoryVisible2 = isAccessoryVisible(this.mEndAccessoryView);
        int iComputeSideReserveForConfiguredIcons = computeSideReserveForConfiguredIcons(zIsAccessoryIconConfigured, zIsAccessoryIconConfigured2);
        int iMax2 = 0;
        if (this.mMenuView.getVisibility() != 8) {
            this.mMenuView.measure(iComputeSideReserveForConfiguredIcons > 0 ? View.MeasureSpec.makeMeasureSpec(Math.max(0, ((size - getPaddingLeft()) - getPaddingRight()) - iComputeSideReserveForConfiguredIcons), Integer.MIN_VALUE) : i3, i4);
            iMax = this.mMenuView.getMeasuredWidth();
            iMax2 = this.mMenuView.getMeasuredHeight();
            iCombineMeasuredStates = View.combineMeasuredStates(0, this.mMenuView.getMeasuredState());
        } else {
            iMax = 0;
            iCombineMeasuredStates = 0;
        }
        if (zIsAccessoryVisible) {
            measureChild(this.mStartAccessoryView, i3, i4);
            iMax2 = Math.max(iMax2, this.mStartAccessoryView.getMeasuredHeight());
        }
        if (zIsAccessoryVisible2) {
            measureChild(this.mEndAccessoryView, i3, i4);
            iMax2 = Math.max(iMax2, this.mEndAccessoryView.getMeasuredHeight());
        }
        if (iComputeSideReserveForConfiguredIcons > 0) {
            iMax = Math.max(iMax, this.mMenuView.getMeasuredWidth() + iComputeSideReserveForConfiguredIcons);
        }
        setMeasuredDimension(View.resolveSizeAndState(Math.max(iMax + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), i3, iCombineMeasuredStates), View.resolveSizeAndState(Math.max(Math.max(iMax2 + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), this.mMinHeightWithoutPaddingVertical + getPaddingTop() + getPaddingBottom()), i4, iCombineMeasuredStates << 16));
    }

    public void refresh() {
        this.mMenuView.refreshThemeAppearance();
        invalidate();
    }

    public void setAccessoryViewAlpha(View view, float f3) {
        if (view != null) {
            view.setAlpha(f3);
        }
    }

    public void setDispatchItemClickWhenAlreadySelected(boolean z2) {
        this.mMenuView.setDispatchItemClickWhenAlreadySelected(z2);
    }

    public void setEndAccessoryBackgroundColor(int i3) {
        this.mEndAccessoryView.setAccessoryBackgroundColor(i3);
    }

    public void setEndAccessoryDrawable(Drawable drawable) {
        setEndAccessoryDrawable(drawable, false);
    }

    public void setEndAccessoryIcon(int i3) {
        setEndAccessoryIcon(i3, false);
    }

    public void setItemsTitleTextColor(ColorStateList colorStateList) {
        this.mMenuView.setItemsTitleTextColor(colorStateList);
    }

    public void setOnEndAccessoryClickListener(OnAccessoryClickListener onAccessoryClickListener) {
        this.mEndAccessoryView.setOnClickListener(onAccessoryClickListener == null ? null : new h(onAccessoryClickListener));
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.mMenuView.setOnItemClickListener(new AnonymousClass3(onItemSelectedListener));
    }

    public void setOnStartAccessoryClickListener(OnAccessoryClickListener onAccessoryClickListener) {
        this.mStartAccessoryView.setOnClickListener(onAccessoryClickListener == null ? null : new l(onAccessoryClickListener));
    }

    public void setRedDot(int i3, int i4, int i5) {
        this.mMenuView.setRedDot(i3, i4, i5);
    }

    public void setSelectedItemId(int i3) {
        this.mMenuView.setSelectedItem(i3);
    }

    public void setStartAccessoryBackgroundColor(int i3) {
        this.mStartAccessoryView.setAccessoryBackgroundColor(i3);
    }

    public void setStartAccessoryDrawable(Drawable drawable) {
        setStartAccessoryDrawable(drawable, false);
    }

    public void setStartAccessoryIcon(int i3) {
        setStartAccessoryIcon(i3, false);
    }

    public void showEndAccessoryView(boolean z2) {
        if (isAccessoryIconConfigured(this.mEndAccessoryView) && !this.mEndExpansionController.isActive()) {
            if (!z2) {
                this.mSkipNextLayoutAnimation = true;
            }
            this.mEndExpansionController.setActive(true);
            applyExpansionViewMeltBallState(z2);
            requestLayout();
            this.mEndExpansionController.applyAlphaScaleState(true, z2);
        }
    }

    public void showStartAccessoryView(boolean z2) {
        if (isAccessoryIconConfigured(this.mStartAccessoryView) && !this.mStartExpansionController.isActive()) {
            if (!z2) {
                this.mSkipNextLayoutAnimation = true;
            }
            this.mStartExpansionController.setActive(true);
            applyExpansionViewMeltBallState(z2);
            requestLayout();
            this.mStartExpansionController.applyAlphaScaleState(true, z2);
        }
    }

    public COUIBottomFloatingAccessoryNavigationBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiBottomFloatingAccessoryNavigationBarStyle);
    }

    public void setEndAccessoryDrawable(Drawable drawable, boolean z2) {
        if (drawable == null) {
            return;
        }
        this.mEndAccessoryView.setIconDrawable(prepareAccessoryDrawable(drawable), z2);
    }

    public void setEndAccessoryIcon(int i3, boolean z2) {
        if (i3 == 0) {
            return;
        }
        setEndAccessoryDrawable(AppCompatResources.getDrawable(getContext(), i3), z2);
    }

    public void setStartAccessoryDrawable(Drawable drawable, boolean z2) {
        if (drawable == null) {
            return;
        }
        this.mStartAccessoryView.setIconDrawable(prepareAccessoryDrawable(drawable), z2);
    }

    public void setStartAccessoryIcon(int i3, boolean z2) {
        if (i3 == 0) {
            return;
        }
        setStartAccessoryDrawable(AppCompatResources.getDrawable(getContext(), i3), z2);
    }

    public COUIBottomFloatingAccessoryNavigationBar(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0);
    }

    public COUIBottomFloatingAccessoryNavigationBar(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mNoDropShadowOutline = new AnonymousClass1();
        this.mLastOrientation = 0;
        this.mOnLayoutChildLefts = new int[3];
        this.mMenuBuilder = new MenuBuilder(context);
        this.mMinHeightWithoutPaddingVertical = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_navigation_bar_min_height_without_padding_vertical);
        this.mPaddingHorizontal = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_navigation_bar_padding_horizontal);
        this.mSideViewSpacing = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_navigation_bar_Minimum_spacing_with_slide_view);
        this.mAccessorySize = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_navigation_bar_height);
        this.mMeltBallParamExtension = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_accessory_navigation_bar_melt_ball_param_extension);
        COUIBottomFloatingNavigationBarMenuView cOUIBottomFloatingNavigationBarMenuView = new COUIBottomFloatingNavigationBarMenuView(context);
        this.mMenuView = cOUIBottomFloatingNavigationBarMenuView;
        COUIBottomFloatingAccessoryView cOUIBottomFloatingAccessoryView = new COUIBottomFloatingAccessoryView(context);
        this.mStartAccessoryView = cOUIBottomFloatingAccessoryView;
        COUIBottomFloatingAccessoryView cOUIBottomFloatingAccessoryView2 = new COUIBottomFloatingAccessoryView(context);
        this.mEndAccessoryView = cOUIBottomFloatingAccessoryView2;
        cOUIBottomFloatingAccessoryView.setEffectCapabilityHost(new i(this));
        cOUIBottomFloatingAccessoryView2.setEffectCapabilityHost(new j(this));
        cOUIBottomFloatingAccessoryView.setVisibility(8);
        cOUIBottomFloatingAccessoryView2.setVisibility(8);
        this.mStartExpansionController = new AccessorySideExpansionController(true, this, createAccessoryAlphaDelegate(), cOUIBottomFloatingAccessoryView);
        this.mEndExpansionController = new AccessorySideExpansionController(false, this, createAccessoryAlphaDelegate(), cOUIBottomFloatingAccessoryView2);
        cOUIBottomFloatingNavigationBarMenuView.setOnDrawingBoundsChangedListener(new k(this));
        addView(cOUIBottomFloatingAccessoryView);
        addView(cOUIBottomFloatingAccessoryView2);
        addView(cOUIBottomFloatingNavigationBarMenuView);
        setClipChildren(false);
        setClipToPadding(false);
        UIUtil.setViewClipExpandSize(this, 0, 120, 0, 0);
        COUIDarkModeUtil.setForceDarkAllow(this, false);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUIBottomFloatingAccessoryNavigationBar, i3, i4);
        int i5 = R.styleable.COUIBottomFloatingAccessoryNavigationBar_couiNavigationBarMenu;
        if (typedArrayObtainStyledAttributes.hasValue(i5)) {
            inflateMenu(typedArrayObtainStyledAttributes.getResourceId(i5, 0));
        }
        int i6 = R.styleable.COUIBottomFloatingAccessoryNavigationBar_couiStartAccessoryIcon;
        if (typedArrayObtainStyledAttributes.hasValue(i6)) {
            setStartAccessoryIcon(typedArrayObtainStyledAttributes.getResourceId(i6, 0));
        }
        int i7 = R.styleable.COUIBottomFloatingAccessoryNavigationBar_couiEndAccessoryIcon;
        if (typedArrayObtainStyledAttributes.hasValue(i7)) {
            setEndAccessoryIcon(typedArrayObtainStyledAttributes.getResourceId(i7, 0));
        }
        typedArrayObtainStyledAttributes.recycle();
        Configuration configuration = getResources().getConfiguration();
        this.mLastOrientation = configuration.orientation;
        this.mLastScreenWidthDp = configuration.screenWidthDp;
    }
}