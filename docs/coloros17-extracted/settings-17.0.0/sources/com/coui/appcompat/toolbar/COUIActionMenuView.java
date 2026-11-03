package com.coui.appcompat.toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.TooltipCompat;
import androidx.appcompat.widget.ViewUtils;
import androidx.collection.ArrayMap;
import androidx.core.view.ViewCompat;
import com.coui.appcompat.COUIMaterialBlurEffect;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.poplist.COUIPopupListWindow;
import com.coui.appcompat.poplist.COUISubMenuClickListener;
import com.coui.appcompat.poplist.PopupListItem;
import com.coui.appcompat.reddot.COUIHintRedDotHelper;
import com.coui.appcompat.state.COUIMaskRippleDrawable;
import com.coui.appcompat.textutil.COUIChangeTextUtil;
import com.coui.appcompat.uiutil.AnimLevel;
import com.coui.appcompat.uiutil.UIUtil;
import com.support.appcompat.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: loaded from: classes7.dex */
public class COUIActionMenuView extends ActionMenuView {
    private static final String EMPTY_TITLE = "";
    private static final int FULL_ALPHA = 255;
    private static final int HUNDRED = 100;
    private static final int MAX_TEXT_MENU_ITEM_LINE = 2;
    private static final String OVER_FLOW_MENU_CLASS = "androidx.appcompat.widget.ActionMenuPresenter$OverflowMenuButton";
    private static final String TAG = "COUIActionMenuView";
    private static final int TEN = 10;
    private AnimLevel mBlurMinAnimLevel;
    private int mEdgeIconItemMargin;
    private int mEdgeTextItemMargin;
    private boolean mEnableAddExtraWidth;
    private COUIHintRedDotHelper mHintRedDotHelper;
    private int mIconItemHorOffset;
    private boolean mIsFixTitleFontSize;
    private boolean mIsSameSide;
    private int mItemSpacing;
    private ArrayMap<Integer, Integer> mItemSpecialColorMap;
    private int mItemVerOffset;
    private COUIMaskRippleDrawable mMaskRippleDrawable;
    private boolean mMaterialEffectOsSupported;
    private int mMaterialType;
    private MenuBuilder mMenu;
    private int mMenuIconBgRadius;
    private int mMenuIconTopPadding;
    private MenuItemImpl mMenuItem;
    private int mMenuViewPadding;
    private int mNonActionRedDotCount;
    private int mNonActionRedDotSum;
    private PopupWindow.OnDismissListener mOnDismissListener;
    private AdapterView.OnItemClickListener mOnSubMenuItemClickListener;
    private final OpenOverflowRunnable mOpenOverflowRunnable;
    private String mOverFlowButtonDescription;
    private int mOverFlowHorPadding;
    private View mOverFlowMenuButton;
    private int mOverFlowMinWidth;
    private ToolbarMaterialEffectDelegate mOverflowEffectDelegate;
    private ArrayList<PopupListItem> mOverflowItems;
    private OverflowMenuListener mOverflowMenuListener;
    public COUIPopupListWindow mOverflowPopup;
    private COUIMaterialBlurEffect.BaseBlurEffectParams mPendingItemBlurParams;
    private boolean mPendingItemIsDrawableMaterialEffect;
    private int mPendingItemNonBlurFallbackColor;
    private List<Class<?>> mPresenterClasses;
    private String mRedDotDescription;
    private boolean mRedDotDirty;
    private int mRedDotHorizontalOffset;
    private HashMap<Integer, Integer> mRedDotMap;
    private int mRedDotVerticalOffset;
    private int mRedDotWithBigNumberHorizontalOffset;
    private int mRedDotWithNumberDescriptionId;
    private int mRedDotWithNumberHorizontalOffset;
    private int mRedDotWithNumberVerticalOffset;
    private int mRedDotWithSmallNumberHorizontalOffset;
    private ArrayMap<Integer, Integer> mSubItemSpecialColorMap;
    private ArrayList<PopupListItem> mSubMenuList;
    private int mSubPosition;
    private int mTextExtarPadding;
    private int mTextMenuItemHorizontalPadding;
    private int mTextMenuItemMaxWidth;
    private int mToolbarTitleMinWidth;
    private boolean mUseBackgroundBlur;
    private int mUseMaterialEffectAttr;

    /* JADX INFO: renamed from: com.coui.appcompat.toolbar.COUIActionMenuView$1, reason: invalid class name */
    public class AnonymousClass1 implements View.OnClickListener {
        public AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            COUIActionMenuView.access$100(COUIActionMenuView.this);
            COUIActionMenuView cOUIActionMenuView = COUIActionMenuView.this;
            cOUIActionMenuView.post(COUIActionMenuView.access$200(cOUIActionMenuView));
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.toolbar.COUIActionMenuView$2, reason: invalid class name */
    public class AnonymousClass2 implements AdapterView.OnItemClickListener {
        public AnonymousClass2() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i3, long j3) {
            if (i3 >= COUIActionMenuView.access$300(COUIActionMenuView.this).getNonActionItems().size()) {
                COUILog.e(COUIActionMenuView.TAG, "IndexOutOfBoundsException! position = " + i3 + " non action items size = " + COUIActionMenuView.access$300(COUIActionMenuView.this).getNonActionItems().size() + "popup menu size = " + COUIActionMenuView.this.mOverflowPopup.getItemList().size());
                return;
            }
            if (((PopupListItem) COUIActionMenuView.access$400(COUIActionMenuView.this).get(i3)).getSubMenuItemList() != null) {
                COUIActionMenuView.access$300(COUIActionMenuView.this).performItemAction(COUIActionMenuView.access$300(COUIActionMenuView.this).getNonActionItems().get(i3), 4);
                return;
            }
            MenuItemImpl menuItemImpl = COUIActionMenuView.access$300(COUIActionMenuView.this).getNonActionItems().get(i3);
            boolean zIsEnable = ((PopupListItem) COUIActionMenuView.access$400(COUIActionMenuView.this).get(i3)).isEnable();
            boolean z2 = menuItemImpl.getIntent() != null;
            COUIActionMenuView.access$300(COUIActionMenuView.this).performItemAction(menuItemImpl, 0);
            if (zIsEnable) {
                if (z2) {
                    COUIActionMenuView.this.mOverflowPopup.setDismissWithWindowAnimation(true);
                }
                COUIActionMenuView.this.mOverflowPopup.dismiss();
            }
        }
    }

    public class OpenOverflowRunnable implements Runnable {
        private OpenOverflowRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUIPopupListWindow cOUIPopupListWindow;
            if (COUIActionMenuView.access$300(COUIActionMenuView.this) != null) {
                COUIActionMenuView.access$300(COUIActionMenuView.this).changeMenuMode();
            }
            if (COUIActionMenuView.this.getWindowToken() == null || (cOUIPopupListWindow = COUIActionMenuView.this.mOverflowPopup) == null || cOUIPopupListWindow.isShowing()) {
                return;
            }
            COUIActionMenuView cOUIActionMenuView = COUIActionMenuView.this;
            cOUIActionMenuView.mOverflowPopup.show(COUIActionMenuView.access$500(cOUIActionMenuView));
        }

        public /* synthetic */ OpenOverflowRunnable(COUIActionMenuView cOUIActionMenuView, AnonymousClass1 anonymousClass1) {
            this();
        }
    }

    public interface OverflowMenuListener {
        void onOverflowMenuPreShow(COUIPopupListWindow cOUIPopupListWindow);
    }

    public COUIActionMenuView(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void access$100(COUIActionMenuView cOUIActionMenuView) {
        cOUIActionMenuView.tryBuildOverflowMenu();
    }

    public static /* synthetic */ OpenOverflowRunnable access$200(COUIActionMenuView cOUIActionMenuView) {
        return cOUIActionMenuView.mOpenOverflowRunnable;
    }

    public static /* synthetic */ MenuBuilder access$300(COUIActionMenuView cOUIActionMenuView) {
        return cOUIActionMenuView.mMenu;
    }

    public static /* synthetic */ ArrayList access$400(COUIActionMenuView cOUIActionMenuView) {
        return cOUIActionMenuView.mOverflowItems;
    }

    public static /* synthetic */ View access$500(COUIActionMenuView cOUIActionMenuView) {
        return cOUIActionMenuView.mOverFlowMenuButton;
    }

    private void configOverflowIconBackground() {
        COUIMaskRippleDrawable cOUIMaskRippleDrawable = new COUIMaskRippleDrawable(getContext());
        this.mMaskRippleDrawable = cOUIMaskRippleDrawable;
        cOUIMaskRippleDrawable.setCircleRippleMask(COUIMaskRippleDrawable.getMaskRippleRadiusByType(getContext(), 0));
        this.mOverFlowMenuButton.setBackground(this.mMaskRippleDrawable);
        COUIDarkModeUtil.setForceDarkAllow(this.mOverFlowMenuButton, false);
    }

    private void drawRedDot(View view, int i3, Canvas canvas) {
        int i4;
        int i5;
        int i6;
        float x2;
        float f3;
        float y2;
        float f4;
        float x3;
        if (i3 != -1) {
            i4 = i3 != 0 ? 2 : 1;
        } else {
            i4 = 0;
        }
        if (view != null) {
            int viewWidth = this.mHintRedDotHelper.getViewWidth(i4, i3);
            int viewHeight = this.mHintRedDotHelper.getViewHeight(i4);
            if (i4 == 1) {
                i5 = this.mRedDotHorizontalOffset;
                i6 = this.mRedDotVerticalOffset;
            } else if (i3 < 10) {
                i5 = this.mRedDotWithSmallNumberHorizontalOffset;
                i6 = this.mRedDotWithNumberVerticalOffset;
            } else if (i3 < 100) {
                i5 = this.mRedDotWithNumberHorizontalOffset;
                i6 = this.mRedDotWithNumberVerticalOffset;
            } else {
                i5 = this.mRedDotWithBigNumberHorizontalOffset;
                i6 = this.mRedDotWithNumberVerticalOffset;
            }
            RectF rectF = new RectF();
            if ((view instanceof ActionMenuItemView) && ((ActionMenuItemView) view).getItemData().getIcon() == null) {
                if (isLayoutRTL()) {
                    x3 = (view.getX() + i5) - this.mMenuViewPadding;
                    f3 = x3 - viewWidth;
                } else {
                    float x4 = this.mMenuViewPadding + ((view.getX() + view.getWidth()) - i5);
                    x3 = x4 + viewWidth;
                    f3 = x4;
                }
                y2 = (this.mMenuIconTopPadding - i6) + this.mItemVerOffset;
                f4 = viewHeight + y2;
            } else {
                if (isLayoutRTL()) {
                    float x5 = (view.getX() + ((view.getWidth() - this.mMenuIconBgRadius) / 2)) - i5;
                    x2 = viewWidth + x5;
                    f3 = x5;
                } else {
                    x2 = ((view.getX() + view.getWidth()) - ((view.getWidth() - this.mMenuIconBgRadius) / 2)) + i5;
                    f3 = x2 - viewWidth;
                }
                y2 = (view.getY() + ((view.getHeight() - this.mMenuIconBgRadius) / 2)) - i6;
                f4 = y2 + viewHeight;
                x3 = x2;
            }
            rectF.left = f3;
            rectF.top = y2;
            rectF.right = x3;
            rectF.bottom = f4;
            this.mRedDotDirty = false;
            if (view.getAlpha() == 1.0f && view.getScaleX() == 1.0f && view.getScaleY() == 1.0f) {
                this.mHintRedDotHelper.drawRedPoint(canvas, i4, Integer.valueOf(i3), rectF);
                return;
            }
            float fWidth = rectF.width();
            float fHeight = rectF.height();
            canvas.saveLayerAlpha(-fWidth, -fHeight, fWidth + getWidth(), fHeight + getHeight(), (int) (view.getAlpha() * 255.0f));
            canvas.scale(view.getScaleX(), view.getScaleY(), view.getLeft() + (view.getWidth() / 2.0f), view.getTop() + (view.getHeight() / 2.0f));
            this.mHintRedDotHelper.drawRedPoint(canvas, i4, Integer.valueOf(i3), rectF);
            canvas.restore();
        }
    }

    private void ensureOverflowMenu() {
        if (this.mOverflowPopup == null) {
            Context context = getContext();
            if (!COUIContextUtil.isCOUITheme(context)) {
                Configuration configuration = getContext().getResources().getConfiguration();
                configuration.densityDpi = getContext().getResources().getDisplayMetrics().densityDpi;
                context = new ContextThemeWrapper(getContext().createConfigurationContext(configuration), R.style.Theme_COUI);
            }
            COUIPopupListWindow cOUIPopupListWindow = new COUIPopupListWindow(context);
            this.mOverflowPopup = cOUIPopupListWindow;
            cOUIPopupListWindow.setUseBackgroundBlur(this.mUseBackgroundBlur, this.mBlurMinAnimLevel);
            this.mOverflowPopup.setInputMethodMode(2);
            this.mOverflowPopup.setOnDismissListener(this.mOnDismissListener);
            this.mOverflowItems = new ArrayList<>();
        }
    }

    private boolean isLayoutRTL() {
        return ViewCompat.getLayoutDirection(this) == 1;
    }

    /* JADX WARN: Code duplicated, block: B:26:0x008c  */
    private int measureChild(int i3, int i4) {
        int i5 = 0;
        if (!shouldUseStrictTextMeasure()) {
            int iMeasureChildCollapseMargins = 0;
            while (i5 < this.getChildCount()) {
                COUIActionMenuView cOUIActionMenuView = this;
                int i6 = i3;
                int i7 = i4;
                iMeasureChildCollapseMargins += cOUIActionMenuView.measureChildCollapseMargins(this.getChildAt(i5), i6, iMeasureChildCollapseMargins, i7, 0);
                i5++;
                this = cOUIActionMenuView;
                i3 = i6;
                i4 = i7;
            }
            return iMeasureChildCollapseMargins;
        }
        int size = View.MeasureSpec.getSize(i3);
        int iMeasureChildCollapseMargins2 = this.mToolbarTitleMinWidth;
        while (i5 < getChildCount()) {
            View childAt = getChildAt(i5);
            if (childAt instanceof COUIActionMenuItemView) {
                COUIActionMenuItemView cOUIActionMenuItemView = (COUIActionMenuItemView) childAt;
                if (cOUIActionMenuItemView.isTextMenuItem()) {
                    TextView textView = (TextView) childAt;
                    int iMeasureTextLineCount = COUIChangeTextUtil.measureTextLineCount(textView, this.mTextMenuItemMaxWidth, this.mTextMenuItemHorizontalPadding * 2);
                    if (i5 == 0) {
                        if (iMeasureTextLineCount <= 2) {
                            cOUIActionMenuItemView.setMaxWidth(this.mTextMenuItemMaxWidth);
                        } else {
                            cOUIActionMenuItemView.setMaxWidth((size - iMeasureChildCollapseMargins2) / 2);
                        }
                        iMeasureChildCollapseMargins2 += measureChildCollapseMargins(childAt, i3, ((size - iMeasureChildCollapseMargins2) / 2) + iMeasureChildCollapseMargins2, i4, 0);
                    } else {
                        if (iMeasureTextLineCount <= 2) {
                            cOUIActionMenuItemView.setMaxWidth(this.mTextMenuItemMaxWidth);
                        } else {
                            cOUIActionMenuItemView.setMaxWidth(COUIChangeTextUtil.binarySearchForOptimalTextViewWidth(textView, 2, this.mTextMenuItemMaxWidth, size - iMeasureChildCollapseMargins2, this.mTextMenuItemHorizontalPadding * 2));
                        }
                        int i8 = iMeasureChildCollapseMargins2;
                        iMeasureChildCollapseMargins2 = measureChildCollapseMargins(childAt, i3, i8, i4, 0) + i8;
                    }
                } else {
                    iMeasureChildCollapseMargins2 = iMeasureChildCollapseMargins2;
                }
            } else {
                iMeasureChildCollapseMargins2 = iMeasureChildCollapseMargins2;
            }
            i5++;
        }
        return iMeasureChildCollapseMargins2;
    }

    private int measureChildCollapseMargins(View view, int i3, int i4, int i5, int i6) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        int i7 = marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
        view.measure(ViewGroup.getChildMeasureSpec(i3, getPaddingLeft() + getPaddingRight() + i7 + i4, marginLayoutParams.width), ViewGroup.getChildMeasureSpec(i5, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + i6, marginLayoutParams.height));
        return view.getMeasuredWidth() + i7;
    }

    private void resetItemMargin() {
        int i3 = -1;
        int i4 = -1;
        int i5 = 0;
        for (int i6 = 0; i6 < getChildCount(); i6++) {
            if (getChildAt(i6).getVisibility() != 8) {
                i5++;
                if (i5 == 1) {
                    i3 = i6;
                    i4 = i3;
                } else {
                    i4 = i6;
                }
            }
        }
        if (i3 != -1 && !this.mIsSameSide && i5 > 1) {
            View childAt = getChildAt(i3);
            if (childAt instanceof ActionMenuItemView) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) childAt.getLayoutParams();
                if (((ActionMenuItemView) childAt).getItemData().getIcon() == null) {
                    if (isLayoutRTL()) {
                        marginLayoutParams.rightMargin = this.mEdgeTextItemMargin;
                    } else {
                        marginLayoutParams.leftMargin = this.mEdgeTextItemMargin;
                    }
                } else if (isLayoutRTL()) {
                    marginLayoutParams.rightMargin = this.mEdgeIconItemMargin;
                } else {
                    marginLayoutParams.leftMargin = this.mEdgeIconItemMargin;
                }
            }
        }
        if (i4 != -1) {
            View childAt2 = getChildAt(i4);
            if (childAt2 instanceof ActionMenuItemView) {
                ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) childAt2.getLayoutParams();
                if (((ActionMenuItemView) childAt2).getItemData().getIcon() == null) {
                    if (isLayoutRTL()) {
                        marginLayoutParams2.leftMargin = this.mEdgeTextItemMargin;
                        return;
                    } else {
                        marginLayoutParams2.rightMargin = this.mEdgeTextItemMargin;
                        return;
                    }
                }
                if (isLayoutRTL()) {
                    marginLayoutParams2.leftMargin = this.mEdgeIconItemMargin;
                } else {
                    marginLayoutParams2.rightMargin = this.mEdgeIconItemMargin;
                }
            }
        }
    }

    private String setRedDotDescription(int i3) {
        if (i3 != -1) {
            return i3 != 0 ? getResources().getQuantityString(this.mRedDotWithNumberDescriptionId, i3, Integer.valueOf(i3)) : this.mRedDotDescription;
        }
        return "";
    }

    private boolean shouldUseStrictTextMeasure() {
        if (getChildCount() != 2 || this.mIsSameSide) {
            return false;
        }
        for (int i3 = 0; i3 < getChildCount(); i3++) {
            View childAt = getChildAt(i3);
            if (!(childAt instanceof COUIActionMenuItemView) || !((COUIActionMenuItemView) childAt).isTextMenuItem()) {
                return false;
            }
        }
        return true;
    }

    private void tryBuildOverflowMenu() {
        ArrayList<PopupListItem> arrayList;
        Integer num;
        ensureOverflowMenu();
        this.mOverflowItems.clear();
        if (this.mMenu != null) {
            PopupListItem.Builder builder = new PopupListItem.Builder();
            for (int i3 = 0; i3 < this.mMenu.getNonActionItems().size(); i3++) {
                MenuItemImpl menuItemImpl = this.mMenu.getNonActionItems().get(i3);
                this.mMenuItem = menuItemImpl;
                if (menuItemImpl.hasSubMenu()) {
                    arrayList = new ArrayList<>();
                    SubMenu subMenu = this.mMenuItem.getSubMenu();
                    for (int i4 = 0; i4 < subMenu.size(); i4++) {
                        MenuItem item = subMenu.getItem(i4);
                        builder.reset().setId(item.getItemId()).setIcon(item.getIcon()).setTitle(item.getTitle() != null ? item.getTitle().toString() : "").setGroupId(item.getGroupId()).setIsEnable(item.isEnabled());
                        arrayList.add(builder.build());
                    }
                } else {
                    arrayList = null;
                }
                int i5 = -1;
                int iIntValue = (!this.mRedDotMap.containsKey(Integer.valueOf(this.mMenuItem.getItemId())) || (num = this.mRedDotMap.get(Integer.valueOf(this.mMenuItem.getItemId()))) == null) ? -1 : num.intValue();
                PopupListItem.Builder redDotAmount = builder.reset().setId(this.mMenuItem.getItemId()).setIcon(this.mMenuItem.getIcon()).setTitle(this.mMenuItem.getTitle() != null ? this.mMenuItem.getTitle().toString() : "").setIsChecked(this.mMenuItem.isChecked()).setGroupId(this.mMenuItem.getGroupId()).setRedDotAmount(iIntValue);
                if (iIntValue != -1) {
                    i5 = 0;
                }
                redDotAmount.setHintType(i5).setSubMenuItemList(arrayList).setIsEnable(this.mMenuItem.isEnabled());
                this.mOverflowItems.add(builder.build());
            }
            this.mOverflowPopup.setItemList(this.mOverflowItems);
            this.mOverflowPopup.setIsFixedFontSize(this.mIsFixTitleFontSize);
            this.mOverflowPopup.setOnItemClickListener(new AnonymousClass2());
            this.mOverflowPopup.setSubMenuClickListener(this.mOnSubMenuItemClickListener);
            OverflowMenuListener overflowMenuListener = this.mOverflowMenuListener;
            if (overflowMenuListener != null) {
                overflowMenuListener.onOverflowMenuPreShow(this.mOverflowPopup);
            }
        }
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i3, ViewGroup.LayoutParams layoutParams) {
        view.setHapticFeedbackEnabled(false);
        TooltipCompat.setTooltipText(view, "");
        if (((ActionMenuView.LayoutParams) layoutParams).isOverflowButton) {
            this.mOverFlowMenuButton = view;
            if (this.mMaterialEffectOsSupported) {
                ToolbarMaterialHelper.applyIconButtonSize(view);
                this.mOverflowEffectDelegate = ToolbarMaterialEffectDelegate.forMenuOverflowButton(this.mOverFlowMenuButton);
            } else {
                configOverflowIconBackground();
                layoutParams.height = -1;
                this.mOverFlowMenuButton.setMinimumWidth(this.mOverFlowMinWidth);
                View view2 = this.mOverFlowMenuButton;
                view2.setPadding(this.mOverFlowHorPadding, view2.getPaddingTop(), this.mOverFlowHorPadding, this.mOverFlowMenuButton.getPaddingBottom());
                this.mOverFlowMenuButton.setOnTouchListener(null);
            }
            this.mOverFlowMenuButton.setLongClickable(false);
            view.setOnClickListener(new AnonymousClass1());
            if (this.mOverflowEffectDelegate != null) {
                if (this.mUseMaterialEffectAttr != COUIToolbar.MaterialEffectState.FOLLOW_SYSTEM.getValue()) {
                    this.mOverflowEffectDelegate.setUseMaterialEffectAttr(this.mUseMaterialEffectAttr);
                }
                if (this.mPendingItemIsDrawableMaterialEffect) {
                    this.mOverflowEffectDelegate.setIsDrawableMaterialEffect(true);
                }
                COUIMaterialBlurEffect.BaseBlurEffectParams baseBlurEffectParams = this.mPendingItemBlurParams;
                if (baseBlurEffectParams != null) {
                    this.mOverflowEffectDelegate.setCustomBlurParams(baseBlurEffectParams);
                }
                int i4 = this.mPendingItemNonBlurFallbackColor;
                if (i4 != 0) {
                    this.mOverflowEffectDelegate.setCustomNonBlurFallbackColor(i4);
                }
                this.mOverflowEffectDelegate.setMaterialEffectType(this.mMaterialType);
            }
        }
        if (view instanceof COUIActionMenuItemView) {
            if (this.mUseMaterialEffectAttr != COUIToolbar.MaterialEffectState.FOLLOW_SYSTEM.getValue()) {
                ((COUIActionMenuItemView) view).setItemUseMaterialEffectAttr(this.mUseMaterialEffectAttr);
            }
            if (this.mPendingItemIsDrawableMaterialEffect) {
                ((COUIActionMenuItemView) view).setItemIsDrawableMaterialEffect(true);
            }
            COUIMaterialBlurEffect.BaseBlurEffectParams baseBlurEffectParams2 = this.mPendingItemBlurParams;
            if (baseBlurEffectParams2 != null) {
                ((COUIActionMenuItemView) view).setItemBlurParams(baseBlurEffectParams2);
            }
            int i5 = this.mPendingItemNonBlurFallbackColor;
            if (i5 != 0) {
                ((COUIActionMenuItemView) view).setItemNonBlurFallbackColor(i5);
            }
            ((COUIActionMenuItemView) view).setItemMaterialEffectType(this.mMaterialType);
        }
        super.addView(view, i3, layoutParams);
        if (this.mMaterialEffectOsSupported) {
            return;
        }
        configMenuItemViewAlignment();
    }

    public void clearRedDotInfo() {
        this.mNonActionRedDotSum = 0;
        this.mNonActionRedDotCount = 0;
        this.mRedDotMap.clear();
        postInvalidate();
    }

    public void configMenuItemViewAlignment() {
        if (getParent() instanceof COUIToolbar) {
            this.mIsSameSide = !((COUIToolbar) getParent()).getIsTitleCenterStyle();
        } else {
            this.mIsSameSide = true;
        }
        if (!this.mIsSameSide) {
            View view = null;
            for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
                View childAt = getChildAt(childCount);
                if (childAt instanceof ActionMenuItemView) {
                    if (view != null) {
                        childAt.setTextAlignment(5);
                        view.setTextAlignment(6);
                    } else {
                        childAt.setTextAlignment(6);
                    }
                    view = childAt;
                }
            }
            return;
        }
        int i3 = 0;
        for (int childCount2 = getChildCount() - 1; childCount2 >= 0; childCount2--) {
            View childAt2 = getChildAt(childCount2);
            if (childAt2 instanceof ActionMenuItemView) {
                i3++;
                childAt2.setTextAlignment(4);
            }
        }
        if (i3 == 1 && (getChildAt(0) instanceof COUIActionMenuItemView)) {
            COUIActionMenuItemView cOUIActionMenuItemView = (COUIActionMenuItemView) getChildAt(0);
            if (cOUIActionMenuItemView.isTextMenuItem()) {
                cOUIActionMenuItemView.setTextAlignment(6);
            }
        }
    }

    @Override // androidx.appcompat.widget.ActionMenuView
    public void dismissPopupMenus() {
        COUIPopupListWindow cOUIPopupListWindow = this.mOverflowPopup;
        if (cOUIPopupListWindow != null) {
            cOUIPopupListWindow.dismiss();
        }
        super.dismissPopupMenus();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        for (int i3 = 0; i3 < getChildCount(); i3++) {
            View childAt = getChildAt(i3);
            if (this.mRedDotMap.containsKey(Integer.valueOf(childAt.getId()))) {
                Integer num = this.mRedDotMap.get(Integer.valueOf(childAt.getId()));
                drawRedDot(childAt, num == null ? 0 : num.intValue(), canvas);
            }
            if (((ActionMenuView.LayoutParams) childAt.getLayoutParams()).isOverflowButton && this.mRedDotMap.size() > 0) {
                int i4 = this.mNonActionRedDotCount == 0 ? -1 : this.mNonActionRedDotSum;
                drawRedDot(childAt, i4, canvas);
                childAt.setContentDescription(TextUtils.isEmpty(setRedDotDescription(i4)) ? this.mOverFlowButtonDescription : this.mOverFlowButtonDescription + "," + setRedDotDescription(i4));
            }
        }
    }

    @Override // androidx.appcompat.widget.ActionMenuView
    public Menu getMenu() {
        MenuBuilder menuBuilder = (MenuBuilder) super.getMenu();
        this.mMenu = menuBuilder;
        return menuBuilder;
    }

    public View getOverFlowMenuButton() {
        return this.mOverFlowMenuButton;
    }

    public ToolbarMaterialEffectDelegate getOverflowButtonEffectDelegate() {
        return this.mOverflowEffectDelegate;
    }

    public COUIPopupListWindow getOverflowPopupWindow() {
        return this.mOverflowPopup;
    }

    @Override // androidx.appcompat.widget.ActionMenuView, androidx.appcompat.view.menu.MenuView
    public void initialize(MenuBuilder menuBuilder) {
        this.mMenu = menuBuilder;
        super.initialize(menuBuilder);
    }

    public boolean isMenuItemHasRedDot(View view) {
        if (this.mRedDotMap.containsKey(Integer.valueOf(view.getId()))) {
            return true;
        }
        return (view.getLayoutParams() instanceof ActionMenuView.LayoutParams) && ((ActionMenuView.LayoutParams) view.getLayoutParams()).isOverflowButton && this.mNonActionRedDotCount > 0;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void onDescendantInvalidated(View view, View view2) {
        super.onDescendantInvalidated(view, view2);
        if (this.mRedDotDirty || !isMenuItemHasRedDot(view)) {
            return;
        }
        this.mRedDotDirty = true;
        invalidate();
    }

    @Override // androidx.appcompat.widget.ActionMenuView, androidx.appcompat.widget.LinearLayoutCompat, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        int childCount = getChildCount();
        int i7 = 0;
        int i8 = 0;
        for (int i9 = 0; i9 < childCount; i9++) {
            if (getChildAt(i9).getVisibility() != 8) {
                i8++;
            }
        }
        boolean zIsLayoutRtl = ViewUtils.isLayoutRtl(this);
        int i10 = (i6 - i4) / 2;
        if (this.mIsSameSide) {
            if (zIsLayoutRtl) {
                int width = getWidth() - getPaddingRight();
                while (i7 < childCount) {
                    View childAt = getChildAt(i7);
                    ActionMenuView.LayoutParams layoutParams = (ActionMenuView.LayoutParams) childAt.getLayoutParams();
                    if (childAt.getVisibility() != 8) {
                        int i11 = width - ((LinearLayout.LayoutParams) layoutParams).rightMargin;
                        int measuredWidth = childAt.getMeasuredWidth();
                        int measuredHeight = childAt.getMeasuredHeight();
                        int i12 = i10 - (measuredHeight / 2);
                        childAt.layout(i11 - measuredWidth, i12, i11, measuredHeight + i12);
                        width = i11 - ((measuredWidth + ((LinearLayout.LayoutParams) layoutParams).leftMargin) + this.mItemSpacing);
                    }
                    i7++;
                }
                return;
            }
            int paddingLeft = getPaddingLeft();
            while (i7 < childCount) {
                View childAt2 = getChildAt(i7);
                ActionMenuView.LayoutParams layoutParams2 = (ActionMenuView.LayoutParams) childAt2.getLayoutParams();
                if (childAt2.getVisibility() != 8) {
                    int i13 = paddingLeft + ((LinearLayout.LayoutParams) layoutParams2).leftMargin;
                    int measuredWidth2 = childAt2.getMeasuredWidth();
                    int measuredHeight2 = childAt2.getMeasuredHeight();
                    int i14 = i10 - (measuredHeight2 / 2);
                    childAt2.layout(i13, i14, i13 + measuredWidth2, measuredHeight2 + i14);
                    paddingLeft = i13 + measuredWidth2 + ((LinearLayout.LayoutParams) layoutParams2).rightMargin + this.mItemSpacing;
                }
                i7++;
            }
            return;
        }
        if (zIsLayoutRtl) {
            int paddingLeft2 = getPaddingLeft();
            boolean z3 = true;
            for (int i15 = childCount - 1; i15 >= 0; i15--) {
                View childAt3 = getChildAt(i15);
                ActionMenuView.LayoutParams layoutParams3 = (ActionMenuView.LayoutParams) childAt3.getLayoutParams();
                if (childAt3.getVisibility() != 8) {
                    paddingLeft2 += ((LinearLayout.LayoutParams) layoutParams3).leftMargin;
                    if (z3) {
                        if ((childAt3 instanceof TextView) && !TextUtils.isEmpty(((TextView) childAt3).getText())) {
                            paddingLeft2 += this.mTextExtarPadding;
                        }
                        z3 = false;
                    }
                    int measuredWidth3 = childAt3.getMeasuredWidth();
                    int measuredHeight3 = childAt3.getMeasuredHeight();
                    int i16 = i10 - (measuredHeight3 / 2);
                    if (i15 != 0 || i8 <= 1) {
                        childAt3.layout(paddingLeft2, i16, paddingLeft2 + measuredWidth3, measuredHeight3 + i16);
                        paddingLeft2 += measuredWidth3 + ((LinearLayout.LayoutParams) layoutParams3).rightMargin + this.mItemSpacing;
                    } else {
                        int width2 = ((getWidth() - getPaddingRight()) - ((LinearLayout.LayoutParams) layoutParams3).rightMargin) - measuredWidth3;
                        if ((childAt3 instanceof TextView) && !TextUtils.isEmpty(((TextView) childAt3).getText())) {
                            width2 -= this.mMenuViewPadding;
                        }
                        childAt3.layout(width2, i16, measuredWidth3 + width2, measuredHeight3 + i16);
                    }
                }
            }
            return;
        }
        int width3 = getWidth() - getPaddingRight();
        boolean z4 = true;
        for (int i17 = childCount - 1; i17 >= 0; i17--) {
            View childAt4 = getChildAt(i17);
            ActionMenuView.LayoutParams layoutParams4 = (ActionMenuView.LayoutParams) childAt4.getLayoutParams();
            if (childAt4.getVisibility() != 8) {
                width3 -= ((LinearLayout.LayoutParams) layoutParams4).rightMargin;
                if (z4) {
                    if ((childAt4 instanceof TextView) && !TextUtils.isEmpty(((TextView) childAt4).getText())) {
                        width3 -= this.mTextExtarPadding;
                    }
                    z4 = false;
                }
                int measuredWidth4 = childAt4.getMeasuredWidth();
                int measuredHeight4 = childAt4.getMeasuredHeight();
                int i18 = i10 - (measuredHeight4 / 2);
                if (i17 != 0 || i8 <= 1) {
                    childAt4.layout(width3 - measuredWidth4, i18, width3, measuredHeight4 + i18);
                    width3 -= (measuredWidth4 + ((LinearLayout.LayoutParams) layoutParams4).leftMargin) + this.mItemSpacing;
                } else {
                    int paddingLeft3 = getPaddingLeft() + ((LinearLayout.LayoutParams) layoutParams4).leftMargin;
                    if ((childAt4 instanceof TextView) && !TextUtils.isEmpty(((TextView) childAt4).getText())) {
                        paddingLeft3 += this.mMenuViewPadding;
                    }
                    childAt4.layout(paddingLeft3, i18, measuredWidth4 + paddingLeft3, measuredHeight4 + i18);
                }
            }
        }
    }

    @Override // androidx.appcompat.widget.ActionMenuView, androidx.appcompat.widget.LinearLayoutCompat, android.view.View
    public void onMeasure(int i3, int i4) {
        if (this.mMenu == null) {
            super.onMeasure(i3, i4);
            return;
        }
        this.mIsSameSide = true;
        if ((getParent() instanceof COUIToolbar) && ((COUIToolbar) getParent()).getIsTitleCenterStyle()) {
            this.mIsSameSide = false;
        }
        setPadding(0, getPaddingTop(), 0, getPaddingBottom());
        boolean z2 = ViewCompat.getLayoutDirection(this) == 1;
        int size = View.MeasureSpec.getSize(i3);
        View.MeasureSpec.getSize(i4);
        resetItemMargin();
        int iMeasureChild = measureChild(i3, i4);
        int measuredHeight = 0;
        for (int i5 = 0; i5 < getChildCount(); i5++) {
            View childAt = getChildAt(i5);
            if (childAt.getMeasuredHeight() > measuredHeight) {
                measuredHeight = childAt.getMeasuredHeight();
            }
        }
        if (this.mIsSameSide) {
            int childCount = getChildCount();
            if (childCount > 0) {
                int i6 = 0;
                int i7 = -1;
                for (int i8 = 0; i8 < childCount; i8++) {
                    if (getChildAt(i8).getVisibility() != 8) {
                        i6++;
                        i7 = i8;
                    }
                }
                int i9 = iMeasureChild + ((i6 - 1) * this.mItemSpacing);
                if (i7 != -1) {
                    View childAt2 = getChildAt(i7);
                    if ((childAt2 instanceof TextView) && !TextUtils.isEmpty(((TextView) childAt2).getText())) {
                        i9 += this.mTextExtarPadding;
                    }
                }
                size = i9;
            } else {
                size = 0;
            }
            if (z2) {
                setPadding(getPaddingLeft(), getPaddingTop(), 0, getPaddingBottom());
            }
        }
        setMeasuredDimension(size, measuredHeight);
    }

    public void refresh() {
        COUIPopupListWindow cOUIPopupListWindow = this.mOverflowPopup;
        if (cOUIPopupListWindow != null) {
            cOUIPopupListWindow.refresh();
        }
        COUIMaskRippleDrawable cOUIMaskRippleDrawable = this.mMaskRippleDrawable;
        if (cOUIMaskRippleDrawable != null) {
            cOUIMaskRippleDrawable.refresh(getContext());
        }
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate = this.mOverflowEffectDelegate;
        if (toolbarMaterialEffectDelegate != null) {
            toolbarMaterialEffectDelegate.refreshRipple(getContext());
        }
        for (int i3 = 0; i3 < getChildCount(); i3++) {
            View childAt = getChildAt(i3);
            if (childAt instanceof COUIActionMenuItemView) {
                ((COUIActionMenuItemView) childAt).refresh();
            }
        }
    }

    public void setBlurMinAnimLevel(AnimLevel animLevel) {
        this.mBlurMinAnimLevel = animLevel;
    }

    public void setDismissMenuWithWindowAnimation(boolean z2) {
        ensureOverflowMenu();
        this.mOverflowPopup.setDismissWithWindowAnimation(z2);
    }

    @Deprecated
    public void setEnableAddExtraWidth(boolean z2) {
    }

    @Deprecated
    public void setIfInBlurWindow(boolean z2) {
        setMenuItemIsDrawableMaterialEffect(true);
        setMenuItemUseMaterialEffectAttr(z2 ? COUIToolbar.MaterialEffectState.FORCE_ENABLE.getValue() : COUIToolbar.MaterialEffectState.FORCE_DISABLE.getValue());
    }

    @Deprecated
    public void setIsFixTitleFontSize(boolean z2) {
        this.mIsFixTitleFontSize = z2;
    }

    @Deprecated
    public void setItemSpecialColor(int i3, int i4) {
    }

    public void setMenuItemBlurParams(COUIMaterialBlurEffect.BaseBlurEffectParams baseBlurEffectParams) {
        this.mPendingItemBlurParams = baseBlurEffectParams;
        for (int i3 = 0; i3 < getChildCount(); i3++) {
            View childAt = getChildAt(i3);
            if (childAt instanceof COUIActionMenuItemView) {
                ((COUIActionMenuItemView) childAt).setItemBlurParams(baseBlurEffectParams);
            }
        }
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate = this.mOverflowEffectDelegate;
        if (toolbarMaterialEffectDelegate != null) {
            toolbarMaterialEffectDelegate.setCustomBlurParams(baseBlurEffectParams);
        }
    }

    public void setMenuItemGap(boolean z2) {
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            if (childAt instanceof COUIActionMenuItemView) {
                ((COUIActionMenuItemView) childAt).setItemWithGap(z2);
            }
        }
    }

    public void setMenuItemIsDrawableMaterialEffect(boolean z2) {
        this.mPendingItemIsDrawableMaterialEffect = z2;
        for (int i3 = 0; i3 < getChildCount(); i3++) {
            View childAt = getChildAt(i3);
            if (childAt instanceof COUIActionMenuItemView) {
                ((COUIActionMenuItemView) childAt).setItemIsDrawableMaterialEffect(z2);
            }
        }
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate = this.mOverflowEffectDelegate;
        if (toolbarMaterialEffectDelegate != null) {
            toolbarMaterialEffectDelegate.setIsDrawableMaterialEffect(z2);
        }
    }

    public void setMenuItemMaterialEffectType(int i3) {
        if (this.mMaterialType == i3) {
            return;
        }
        this.mMaterialType = i3;
        for (int i4 = 0; i4 < getChildCount(); i4++) {
            View childAt = getChildAt(i4);
            if (childAt instanceof COUIActionMenuItemView) {
                ((COUIActionMenuItemView) childAt).setItemMaterialEffectType(i3);
            }
        }
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate = this.mOverflowEffectDelegate;
        if (toolbarMaterialEffectDelegate != null) {
            toolbarMaterialEffectDelegate.setMaterialEffectType(i3);
        }
    }

    public void setMenuItemNonBlurFallbackColor(int i3) {
        if (this.mPendingItemNonBlurFallbackColor == i3) {
            return;
        }
        this.mPendingItemNonBlurFallbackColor = i3;
        for (int i4 = 0; i4 < getChildCount(); i4++) {
            View childAt = getChildAt(i4);
            if (childAt instanceof COUIActionMenuItemView) {
                ((COUIActionMenuItemView) childAt).setItemNonBlurFallbackColor(i3);
            }
        }
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate = this.mOverflowEffectDelegate;
        if (toolbarMaterialEffectDelegate != null) {
            toolbarMaterialEffectDelegate.setCustomNonBlurFallbackColor(i3);
        }
    }

    public void setMenuItemUseMaterialEffectAttr(int i3) {
        if (this.mUseMaterialEffectAttr == i3) {
            return;
        }
        this.mUseMaterialEffectAttr = i3;
        int childCount = getChildCount();
        for (int i4 = 0; i4 < childCount; i4++) {
            View childAt = getChildAt(i4);
            if (childAt instanceof COUIActionMenuItemView) {
                ((COUIActionMenuItemView) childAt).setItemUseMaterialEffectAttr(this.mUseMaterialEffectAttr);
            }
        }
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate = this.mOverflowEffectDelegate;
        if (toolbarMaterialEffectDelegate != null) {
            toolbarMaterialEffectDelegate.setUseMaterialEffectAttr(this.mUseMaterialEffectAttr);
        }
    }

    public void setOnSubMenuItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mOnSubMenuItemClickListener = onItemClickListener;
    }

    public void setOverflowMenuListener(OverflowMenuListener overflowMenuListener) {
        this.mOverflowMenuListener = overflowMenuListener;
    }

    @Override // androidx.appcompat.widget.ActionMenuView
    public void setOverflowReserved(boolean z2) {
        super.setOverflowReserved(z2);
        COUIPopupListWindow cOUIPopupListWindow = this.mOverflowPopup;
        if (cOUIPopupListWindow == null || !cOUIPopupListWindow.isShowing()) {
            return;
        }
        if (this.mMenu.getNonActionItems().isEmpty()) {
            this.mOverflowPopup.dismiss();
        } else {
            tryBuildOverflowMenu();
        }
    }

    public void setPopupWindowOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    public void setRedDot(int i3, int i4) {
        MenuBuilder menuBuilder = this.mMenu;
        if (menuBuilder == null) {
            Log.e(TAG, "The MenuBuilder is null");
            return;
        }
        menuBuilder.flagActionItems();
        MenuItemImpl menuItemImpl = (MenuItemImpl) this.mMenu.findItem(i3);
        if (menuItemImpl == null) {
            return;
        }
        if (i4 != -1) {
            if (!menuItemImpl.isActionButton()) {
                if (this.mRedDotMap.containsKey(Integer.valueOf(i3))) {
                    Integer num = this.mRedDotMap.get(Integer.valueOf(i3));
                    this.mNonActionRedDotSum = (this.mNonActionRedDotSum + i4) - (num != null ? num.intValue() : 0);
                } else {
                    this.mNonActionRedDotCount++;
                    this.mNonActionRedDotSum += i4;
                }
            }
            this.mRedDotMap.put(Integer.valueOf(i3), Integer.valueOf(i4));
        } else if (this.mRedDotMap.containsKey(Integer.valueOf(i3))) {
            if (!menuItemImpl.isActionButton()) {
                int i5 = this.mNonActionRedDotCount;
                this.mNonActionRedDotCount = i5 - (i5 == 0 ? 0 : 1);
                Integer num2 = this.mRedDotMap.get(Integer.valueOf(i3));
                this.mNonActionRedDotSum -= num2 != null ? num2.intValue() : 0;
            }
            this.mRedDotMap.remove(Integer.valueOf(i3));
        }
        CharSequence title = menuItemImpl.getTitle();
        if (i4 != -1) {
            title = ((Object) title) + "," + setRedDotDescription(i4);
        }
        menuItemImpl.setContentDescription(title);
        postInvalidate();
    }

    @Deprecated
    public void setSubItemSpecialColor(int i3, int i4) {
    }

    @Deprecated
    public void setSubMenuClickListener(COUISubMenuClickListener cOUISubMenuClickListener) {
        this.mOnSubMenuItemClickListener = cOUISubMenuClickListener;
    }

    @Deprecated
    public void setSubMenuList(ArrayList<PopupListItem> arrayList, int i3) {
    }

    public void setUseBackgroundBlur(boolean z2) {
        this.mUseBackgroundBlur = z2;
    }

    @Override // androidx.appcompat.widget.ActionMenuView
    public boolean showOverflowMenu() {
        View view;
        Activity activityContextToActivity = UIUtil.contextToActivity(getContext());
        if ((activityContextToActivity != null && (activityContextToActivity.isFinishing() || activityContextToActivity.isDestroyed())) || this.mOverflowPopup == null || (view = this.mOverFlowMenuButton) == null || view.getParent() == null) {
            return false;
        }
        tryBuildOverflowMenu();
        post(this.mOpenOverflowRunnable);
        return true;
    }

    public COUIActionMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mOpenOverflowRunnable = new OpenOverflowRunnable(this, null);
        this.mMenu = null;
        this.mPresenterClasses = new ArrayList();
        this.mIsSameSide = true;
        this.mMenuViewPadding = 0;
        this.mSubMenuList = null;
        this.mSubPosition = -1;
        this.mEnableAddExtraWidth = true;
        this.mOverflowMenuListener = null;
        this.mUseBackgroundBlur = false;
        this.mBlurMinAnimLevel = UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN;
        this.mUseMaterialEffectAttr = COUIToolbar.MaterialEffectState.FOLLOW_SYSTEM.getValue();
        this.mRedDotDirty = false;
        this.mMaterialEffectOsSupported = false;
        this.mPendingItemNonBlurFallbackColor = 0;
        this.mPendingItemIsDrawableMaterialEffect = false;
        this.mMaterialType = 0;
        this.mOverFlowMinWidth = getResources().getDimensionPixelSize(com.support.toolbar.R.dimen.coui_action_menu_item_min_width);
        this.mOverFlowHorPadding = getResources().getDimensionPixelSize(com.support.toolbar.R.dimen.overflow_button_padding_horizontal);
        this.mEdgeIconItemMargin = getResources().getDimensionPixelSize(com.support.toolbar.R.dimen.toolbar_edge_icon_menu_item_margin);
        this.mIconItemHorOffset = getResources().getDimensionPixelSize(com.support.toolbar.R.dimen.toolbar_icon_item_horizontal_offset);
        this.mItemVerOffset = getResources().getDimensionPixelSize(com.support.toolbar.R.dimen.toolbar_item_vertical_offset);
        this.mItemSpacing = getResources().getDimensionPixelSize(com.support.toolbar.R.dimen.coui_actionbar_menuitemview_item_spacing);
        this.mRedDotMap = new HashMap<>();
        this.mRedDotHorizontalOffset = getResources().getDimensionPixelSize(com.support.toolbar.R.dimen.coui_toolbar_menu_red_dot_horizontal_offset);
        this.mRedDotVerticalOffset = getResources().getDimensionPixelSize(com.support.toolbar.R.dimen.coui_toolbar_menu_red_dot_vertical_offset);
        this.mRedDotWithNumberVerticalOffset = getResources().getDimensionPixelSize(com.support.toolbar.R.dimen.coui_toolbar_menu_red_dot_with_number_vertical_offset);
        this.mRedDotWithNumberHorizontalOffset = getResources().getDimensionPixelSize(com.support.toolbar.R.dimen.coui_toolbar_menu_red_dot_with_number_horizontal_offset);
        this.mRedDotWithSmallNumberHorizontalOffset = getResources().getDimensionPixelSize(com.support.toolbar.R.dimen.coui_toolbar_menu_red_dot_with_small_number_horizontal_offset);
        this.mRedDotWithBigNumberHorizontalOffset = getResources().getDimensionPixelSize(com.support.toolbar.R.dimen.coui_toolbar_menu_red_dot_with_big_number_horizontal_offset);
        this.mMenuIconTopPadding = getResources().getDimensionPixelSize(com.support.toolbar.R.dimen.coui_toolbar_menu_icon_top_padding);
        this.mHintRedDotHelper = new COUIHintRedDotHelper(getContext(), null, com.support.reddot.R.styleable.COUIHintRedDot, 0, com.support.reddot.R.style.Widget_COUI_COUIHintRedDot_Small);
        this.mOverFlowButtonDescription = getResources().getString(androidx.appcompat.R.string.abc_action_menu_overflow_description);
        this.mRedDotDescription = getResources().getString(com.support.reddot.R.string.red_dot_description);
        this.mRedDotWithNumberDescriptionId = com.support.reddot.R.plurals.red_dot_with_number_description;
        this.mMenuIconBgRadius = getResources().getDimensionPixelSize(com.support.toolbar.R.dimen.coui_toolbar_menu_bg_radius);
        this.mTextMenuItemMaxWidth = context.getResources().getDimensionPixelSize(com.support.toolbar.R.dimen.coui_action_bar_text_menu_item_max_width);
        this.mToolbarTitleMinWidth = context.getResources().getDimensionPixelSize(com.support.toolbar.R.dimen.coui_toolbar_title_min_width);
        this.mTextMenuItemHorizontalPadding = context.getResources().getDimensionPixelSize(com.support.toolbar.R.dimen.coui_toolbar_text_menu_bg_padding_horizontal);
        this.mMaterialEffectOsSupported = ToolbarMaterialHelper.isMaterialEffectOsSupported();
    }

    public void dismissPopupMenus(boolean z2) {
        COUIPopupListWindow cOUIPopupListWindow = this.mOverflowPopup;
        if (cOUIPopupListWindow != null) {
            if (z2) {
                cOUIPopupListWindow.dismiss();
            } else {
                cOUIPopupListWindow.superDismiss();
            }
        }
    }
}