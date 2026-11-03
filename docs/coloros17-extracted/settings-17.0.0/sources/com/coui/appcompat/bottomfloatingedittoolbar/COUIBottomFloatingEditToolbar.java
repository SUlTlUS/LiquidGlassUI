package com.coui.appcompat.bottomfloatingedittoolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingItem;
import com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView;
import com.coui.appcompat.bottomfloatingnavigationbar.MenuXmlParserUtils;
import com.coui.appcompat.bottomfloatingnavigationbar.OnMenuViewShowHideAnimListener;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.coui.appcompat.uiutil.UIUtil;
import com.support.bottomnavigation.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes7.dex */
public class COUIBottomFloatingEditToolbar extends ViewGroup {
    private static final int SHADOW_CLIP_EXPAND_SIZE_PX = 120;
    private static final float TOOLBAR_TRANSLATION_Y_MIN_VISIBLE_CHANGE = 0.001f;
    private static final float TOOLBAR_TRANSLATION_Y_SPRING_BOUNCE = 0.25f;
    private static final float TOOLBAR_TRANSLATION_Y_SPRING_RESPONSE = 0.38f;
    private int mContainerHeight;
    private boolean mIsMenuViewVisible;
    private boolean mIsSmallScreen;
    private final int mLargePaddingHorizontal;
    private final int mMediumPaddingHorizontal;
    private final MenuBuilder mMenuBuilder;
    private MenuInflater mMenuInflater;
    private final COUIBottomFloatingEditToolbarMenuView mMenuView;
    private final int mMinHeightWithoutPaddingVertical;
    private OnMenuViewShowHideAnimListener mOnMenuViewShowHideAnimListener;
    private final int mSmallPaddingHorizontal;
    private COUISpringAnimation mToolbarTranslationYAnim;

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingedittoolbar.COUIBottomFloatingEditToolbar$1, reason: invalid class name */
    public class AnonymousClass1 implements COUIDynamicAnimation.OnAnimationUpdateListener {
        public AnonymousClass1() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
        public void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
            COUIBottomFloatingEditToolbar.this.setTranslationY(f3);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingedittoolbar.COUIBottomFloatingEditToolbar$2, reason: invalid class name */
    public class AnonymousClass2 implements BaseBottomFloatingMenuView.OnItemClickListener {
        final /* synthetic */ OnItemSelectedListener val$listener;

        public AnonymousClass2(OnItemSelectedListener onItemSelectedListener) {
            this.val$listener = onItemSelectedListener;
        }

        @Override // com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView.OnItemClickListener
        public void onItemClick(View view, BaseBottomFloatingItem baseBottomFloatingItem, int i3) {
            OnItemSelectedListener onItemSelectedListener = this.val$listener;
            if (onItemSelectedListener == null || !(baseBottomFloatingItem instanceof COUIBottomFloatingEditToolbarItem)) {
                return;
            }
            onItemSelectedListener.onEditToolbarItemSelected(view, (COUIBottomFloatingEditToolbarItem) baseBottomFloatingItem, i3);
        }
    }

    public interface OnItemSelectedListener {
        void onEditToolbarItemSelected(View view, COUIBottomFloatingEditToolbarItem cOUIBottomFloatingEditToolbarItem, int i3);
    }

    public COUIBottomFloatingEditToolbar(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void a(COUIBottomFloatingEditToolbar cOUIBottomFloatingEditToolbar, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        OnMenuViewShowHideAnimListener onMenuViewShowHideAnimListener = cOUIBottomFloatingEditToolbar.mOnMenuViewShowHideAnimListener;
        if (onMenuViewShowHideAnimListener != null) {
            onMenuViewShowHideAnimListener.onMenuViewShowHideAnimEnd();
        }
    }

    private void changePadding(int i3) {
        int i4;
        if (COUIResponsiveUtils.isSmallScreen(getContext(), i3)) {
            i4 = this.mSmallPaddingHorizontal;
        } else if (COUIResponsiveUtils.isMediumScreen(getContext(), i3)) {
            i4 = this.mMediumPaddingHorizontal;
        } else {
            i4 = COUIResponsiveUtils.isLargeScreen(getContext(), i3) ? this.mLargePaddingHorizontal : 0;
        }
        if (i4 == getPaddingLeft() && i4 == getPaddingRight()) {
            return;
        }
        setPadding(i4, getPaddingTop(), i4, getPaddingBottom());
    }

    private MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            this.mMenuInflater = new MenuInflater(getContext());
        }
        return this.mMenuInflater;
    }

    private float getMenuViewHiddenTranslationY() {
        int height = this.mContainerHeight;
        if (height <= 0) {
            height = getHeight();
        }
        return height - this.mMenuView.getTop();
    }

    private void initEditToolbarTranslationYAnim() {
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(0.25f);
        cOUISpringForce.setResponse(0.38f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mToolbarTranslationYAnim = spring;
        spring.setMinimumVisibleChange(0.001f);
        this.mToolbarTranslationYAnim.addUpdateListener(new AnonymousClass1());
        this.mToolbarTranslationYAnim.addEndListener(new a(this));
    }

    public void addItems(List<COUIBottomFloatingEditToolbarItem> list) {
        this.mMenuView.addItems(list);
    }

    public int getMenuViewHeight() {
        return this.mMenuView.getMenuHeight();
    }

    public void hideMenuView() {
        if (this.mIsMenuViewVisible) {
            this.mIsMenuViewVisible = false;
            this.mToolbarTranslationYAnim.setStartValue(getTranslationY());
            this.mToolbarTranslationYAnim.animateToFinalPosition(getMenuViewHiddenTranslationY());
            OnMenuViewShowHideAnimListener onMenuViewShowHideAnimListener = this.mOnMenuViewShowHideAnimListener;
            if (onMenuViewShowHideAnimListener != null) {
                onMenuViewShowHideAnimListener.onMenuViewShowHideAnimStart(false);
            }
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
        COUIBottomFloatingEditToolbarItem.Builder builder = new COUIBottomFloatingEditToolbarItem.Builder();
        for (MenuItemImpl menuItemImpl : this.mMenuBuilder.getNonActionItems()) {
            int itemId = menuItemImpl.getItemId();
            MenuXmlParserUtils.MenuItemAttrs menuItemAttrs2 = menuItemAttrs.get(Integer.valueOf(itemId));
            ((COUIBottomFloatingEditToolbarItem.Builder) ((COUIBottomFloatingEditToolbarItem.Builder) ((COUIBottomFloatingEditToolbarItem.Builder) ((COUIBottomFloatingEditToolbarItem.Builder) builder.reset()).setId(itemId)).setTitle(menuItemImpl.getTitle() == null ? "" : menuItemImpl.getTitle().toString())).setIcon(menuItemImpl.getIcon())).setDescription(menuItemImpl.getContentDescription());
            if (menuItemAttrs2 != null) {
                ((COUIBottomFloatingEditToolbarItem.Builder) builder.setRedDotType(menuItemAttrs2.redDotType)).setRedDotNumber(menuItemAttrs2.redDotNumber);
            }
            arrayList.add(builder.build());
        }
        addItems(arrayList);
    }

    public boolean isSmallScreen() {
        return this.mIsSmallScreen;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        int i7 = i6 - i4;
        this.mContainerHeight = i7;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = ((i5 - i3) - getPaddingRight()) - paddingLeft;
        int paddingBottom = (i7 - getPaddingBottom()) - paddingTop;
        if (this.mMenuView.getVisibility() != 8) {
            int measuredWidth = this.mMenuView.getMeasuredWidth();
            int measuredHeight = this.mMenuView.getMeasuredHeight();
            int i8 = paddingLeft + ((paddingRight - measuredWidth) / 2);
            int i9 = paddingTop + ((paddingBottom - measuredHeight) / 2);
            this.mMenuView.layout(i8, i9, measuredWidth + i8, measuredHeight + i9);
            if (this.mIsMenuViewVisible) {
                return;
            }
            float menuViewHiddenTranslationY = getMenuViewHiddenTranslationY();
            if (this.mToolbarTranslationYAnim.isRunning()) {
                this.mToolbarTranslationYAnim.cancel();
            }
            setTranslationY(menuViewHiddenTranslationY);
        }
    }

    @Override // android.view.View
    public void onMeasure(int i3, int i4) {
        int iCombineMeasuredStates;
        int iMax;
        int size = View.MeasureSpec.getSize(i3);
        changePadding(size);
        boolean zIsSmallScreen = COUIResponsiveUtils.isSmallScreen(getContext(), size);
        this.mIsSmallScreen = zIsSmallScreen;
        this.mMenuView.setHorizontalStyle(!zIsSmallScreen);
        int i5 = 0;
        if (this.mMenuView.getVisibility() != 8) {
            measureChild(this.mMenuView, i3, i4);
            int iMax2 = Math.max(0, this.mMenuView.getMeasuredWidth());
            iMax = Math.max(0, this.mMenuView.getMeasuredHeight());
            i5 = iMax2;
            iCombineMeasuredStates = View.combineMeasuredStates(0, this.mMenuView.getMeasuredState());
        } else {
            iCombineMeasuredStates = 0;
            iMax = 0;
        }
        setMeasuredDimension(View.resolveSizeAndState(Math.max(i5 + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), i3, iCombineMeasuredStates), View.resolveSizeAndState(Math.max(Math.max(iMax + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), this.mMinHeightWithoutPaddingVertical + getPaddingTop() + getPaddingBottom()), i4, iCombineMeasuredStates << 16));
    }

    public void setIsDrawableMaterialEffect(boolean z2) {
        this.mMenuView.setIsDrawableMaterialEffect(z2);
    }

    public void setItemEnable(int i3, boolean z2) {
        this.mMenuView.setItemEnable(i3, z2);
    }

    public void setOnItemClickListener(OnItemSelectedListener onItemSelectedListener) {
        this.mMenuView.setOnItemClickListener(new AnonymousClass2(onItemSelectedListener));
    }

    public void setOnMenuViewShowHideAnimListener(OnMenuViewShowHideAnimListener onMenuViewShowHideAnimListener) {
        this.mOnMenuViewShowHideAnimListener = onMenuViewShowHideAnimListener;
    }

    public void setRedDot(int i3, int i4, int i5) {
        this.mMenuView.setRedDot(i3, i4, i5);
    }

    public void setUseMaterialEffectAttr(BaseBottomFloatingMenuView.MaterialEffectState materialEffectState) {
        this.mMenuView.setUseMaterialEffectAttr(materialEffectState);
    }

    public void showMenuView() {
        if (this.mIsMenuViewVisible) {
            if (this.mContainerHeight <= 0) {
                return;
            } else {
                setTranslationY(getMenuViewHiddenTranslationY());
            }
        }
        this.mIsMenuViewVisible = true;
        this.mToolbarTranslationYAnim.setStartValue(getTranslationY());
        this.mToolbarTranslationYAnim.animateToFinalPosition(0.0f);
        OnMenuViewShowHideAnimListener onMenuViewShowHideAnimListener = this.mOnMenuViewShowHideAnimListener;
        if (onMenuViewShowHideAnimListener != null) {
            onMenuViewShowHideAnimListener.onMenuViewShowHideAnimStart(true);
        }
    }

    public COUIBottomFloatingEditToolbar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiBottomFloatingNavigationBarStyle);
    }

    public COUIBottomFloatingEditToolbar(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0);
    }

    public COUIBottomFloatingEditToolbar(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mIsMenuViewVisible = true;
        this.mIsSmallScreen = false;
        this.mContainerHeight = 0;
        this.mMenuBuilder = new MenuBuilder(context);
        this.mMinHeightWithoutPaddingVertical = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_edit_toolbar_min_height_without_padding_vertical);
        this.mSmallPaddingHorizontal = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_edit_toolbar_padding_horizontal_small_screen);
        this.mMediumPaddingHorizontal = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_edit_toolbar_padding_horizontal_medium_screen);
        this.mLargePaddingHorizontal = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_edit_toolbar_padding_horizontal_large_screen);
        COUIBottomFloatingEditToolbarMenuView cOUIBottomFloatingEditToolbarMenuView = new COUIBottomFloatingEditToolbarMenuView(context);
        this.mMenuView = cOUIBottomFloatingEditToolbarMenuView;
        addView(cOUIBottomFloatingEditToolbarMenuView);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUIBottomFloatingEditToolbar, i3, i4);
        int i5 = R.styleable.COUIBottomFloatingEditToolbar_couiEditToolbarMenu;
        if (typedArrayObtainStyledAttributes.hasValue(i5)) {
            inflateMenu(typedArrayObtainStyledAttributes.getResourceId(i5, 0));
        }
        typedArrayObtainStyledAttributes.recycle();
        initEditToolbarTranslationYAnim();
        setClipChildren(false);
        setClipToPadding(false);
        UIUtil.setViewClipExpandSize(this, 0, 120, 0, 0);
        COUIDarkModeUtil.setForceDarkAllow(this, false);
    }
}