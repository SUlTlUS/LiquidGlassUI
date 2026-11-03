package com.coui.appcompat.poplist;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.recyclerview.widget.COUIRecyclerView;
import androidx.recyclerview.widget.RecyclerView;
import com.coui.appcompat.log.COUILog;
import com.support.poplist.R;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes7.dex */
public class COUIRotatingPopupListWindow extends COUIPopupListWindow {
    private static final boolean COUI_DEBUG;
    private static final int MENU_CONTAINER_VERTICAL_PADDING_EDGES = 2;
    private static final String TAG = "COUIRotatingPopupList";
    private final Rect mClippedMainMenuBounds;
    private boolean mLastMainMenuAboveAnchor;
    private MainMenuPlacementSpec mMainMenuPlacementSpec;
    private boolean mMainMenuRotatedSlotLayoutActive;
    private float mMenuContentRotationDegrees;

    static {
        COUI_DEBUG = COUILog.LOG_DEBUG || COUILog.isLoggable(TAG, 3);
    }

    public COUIRotatingPopupListWindow(Context context) {
        super(context);
        this.mMainMenuPlacementSpec = MainMenuPlacementSpec.DEFAULT;
        this.mClippedMainMenuBounds = new Rect();
        this.mLocateHelper = new RotatingPopupMenuLocateHelper(context);
    }

    private void applyMainWrapperRotation() {
        if (this.mMainMenuWrapper == null || this.mContentView == null) {
            return;
        }
        if (isRotationNonZero()) {
            if (!isRotationOrthogonal()) {
                clearRotatedSlot();
            }
            rotatingContent().setMainRotation(this.mMenuContentRotationDegrees);
        } else {
            clearRotatedSlot();
            clearMenuWrapperRotation(this.mMainMenuWrapper);
            clearMenuWrapperRotation(this.mSubMenuWrapper);
        }
    }

    private void applyRotatedSlotIfNeeded() {
        if (!isRotationOrthogonal()) {
            clearRotatedSlot();
            return;
        }
        PopupMenuLocateHelper popupMenuLocateHelper = this.mLocateHelper;
        if (popupMenuLocateHelper == null || this.mContentView == null) {
            COUILog.w(TAG, "applyRotatedSlotIfNeeded: null helper or content view");
            clearRotatedSlot();
            return;
        }
        PopupMenuDomain domain = popupMenuLocateHelper.getDomain();
        if (domain == null || domain.mMainMenu.isEmpty()) {
            clearRotatedSlot();
            return;
        }
        int i3 = this.mMainMenuWidth;
        int i4 = this.mMainMenuHeight;
        if (i3 <= 0 || i4 <= 0) {
            COUILog.w(TAG, "applyRotatedSlotIfNeeded: invalid intrinsic size " + i3 + "x" + i4);
            clearRotatedSlot();
            return;
        }
        if (!domain.computeMainMenuClippedToAvailable(this.mClippedMainMenuBounds)) {
            COUILog.w(TAG, "applyRotatedSlotIfNeeded: slot rect does not intersect available rect");
            clearRotatedSlot();
            return;
        }
        if (this.mClippedMainMenuBounds.width() != domain.mMainMenu.width() || this.mClippedMainMenuBounds.height() != domain.mMainMenu.height()) {
            rotatingLocate().applyMainMenuLayoutRect(this.mClippedMainMenuBounds);
        }
        this.mMainMenuRotatedSlotLayoutActive = true;
        this.mContentView.setMainMenuSize(this.mClippedMainMenuBounds.width(), this.mClippedMainMenuBounds.height());
        rotatingContent().setMainMenuChildMeasureSize(i3, i4);
        rotatingContent().setMainMenuAnimTargetSize(i3, i4);
        this.mContentView.requestLayout();
    }

    private void clearMenuWrapperRotation(RoundFrameLayout roundFrameLayout) {
        if (roundFrameLayout == null) {
            return;
        }
        if (this.mContentView != null) {
            rotatingContent().clearMenuRotation(roundFrameLayout);
        }
        if (roundFrameLayout == this.mMainMenuWrapper) {
            clearRotatedSlot();
        }
    }

    private void clearRotatedSlot() {
        if (this.mMainMenuRotatedSlotLayoutActive) {
            restoreSlotIfActive();
        } else {
            clearSlotViewState();
        }
    }

    private void clearSlotViewState() {
        if (this.mContentView != null) {
            rotatingContent().clearMainMenuChildMeasureSize();
            rotatingContent().setMainMenuAnimTargetSize(0, 0);
        }
    }

    private int getRotatedLocateHeight() {
        return MenuRotationHelper.rotatedLocateHeight(rotationKind(), this.mMainMenuWidth, this.mMainMenuHeight);
    }

    private int getRotatedLocateWidth() {
        return MenuRotationHelper.rotatedLocateWidth(rotationKind(), this.mMainMenuWidth, this.mMainMenuHeight);
    }

    private boolean isRotationNonZero() {
        return rotationKind().isNonZeroRotation();
    }

    private boolean isRotationOrthogonal() {
        return rotationKind().isOrthogonalForLocate();
    }

    private void layoutDefaultMainMenu(boolean z2, boolean z3) {
        this.mContentView.setDomain(this.mLocateHelper.getDomain());
        if (z3) {
            this.mContentView.addMainMenuView(this.mMainMenuWrapper);
        }
        if (this.mMainMenuWidth == 0 || this.mMainMenuHeight == 0) {
            measurePopupWindow();
        }
        this.mContentView.setMainMenuSize(this.mMainMenuWidth, this.mMainMenuHeight);
        this.mLocateHelper.prepareShowMainMenu(this.mMainMenuWidth, this.mMainMenuHeight, z2, this.mGlobalOffsetX, this.mGlobalOffsetY);
        if (this.mUseRecyclerView) {
            return;
        }
        syncMainMenuSize();
    }

    private void layoutMainMenuForShow(View view, boolean z2, MainMenuPlacementSpec mainMenuPlacementSpec, boolean z3, boolean z4) {
        if (this.mLocateHelper == null || this.mContentView == null) {
            COUILog.w(TAG, "layoutMainMenuForShow: missing locate helper or content view");
            return;
        }
        if (mainMenuPlacementSpec != null && view == null) {
            COUILog.w(TAG, "layoutMainMenuForShow: anchor is required when measure caps are set");
            return;
        }
        if (mainMenuPlacementSpec != null) {
            rotatingLocate().computeMainMenuMeasureCaps(getMainMenuMaxWidth(), view, mainMenuPlacementSpec, isRotationOrthogonal());
            if (z4) {
                this.mContentView.setDomain(this.mLocateHelper.getDomain());
                this.mContentView.addMainMenuView(this.mMainMenuWrapper);
            }
            if (!z3) {
                measurePopupWindow();
            }
        }
        boolean zUseSidePlacement = useSidePlacement();
        boolean z5 = !zUseSidePlacement && isRotationOrthogonal();
        if (zUseSidePlacement) {
            rotatingLocate().prepareShowMainMenuExplicitSide(this.mMainMenuWidth, this.mMainMenuHeight, this.mMenuContentRotationDegrees, this.mGlobalOffsetX, this.mGlobalOffsetY);
            this.mContentView.setMainMenuSize(this.mLocateHelper.getMainMenuWidth(), this.mLocateHelper.getMainMenuHeight());
            int mainMenuRotationContentWidth = rotatingLocate().getMainMenuRotationContentWidth();
            int mainMenuRotationContentHeight = rotatingLocate().getMainMenuRotationContentHeight();
            if (mainMenuRotationContentWidth <= 0 || mainMenuRotationContentHeight <= 0) {
                rotatingContent().setMainMenuChildMeasureSize(this.mMainMenuWidth, this.mMainMenuHeight);
                rotatingContent().setMainMenuAnimTargetSize(this.mMainMenuWidth, this.mMainMenuHeight);
            } else {
                rotatingContent().setMainMenuChildMeasureSize(mainMenuRotationContentWidth, mainMenuRotationContentHeight);
                rotatingContent().setMainMenuAnimTargetSize(mainMenuRotationContentWidth, mainMenuRotationContentHeight);
            }
            this.mContentView.setDomain(this.mLocateHelper.getDomain());
            if (!this.mUseRecyclerView) {
                syncMainMenuSize();
            }
        } else if (z5) {
            rotatingLocate().prepareOrthogonalMainMenu(this.mMainMenuWidth, this.mMainMenuHeight, this.mMenuContentRotationDegrees, z2, this.mGlobalOffsetX, this.mGlobalOffsetY);
            this.mContentView.setMainMenuSize(this.mLocateHelper.getMainMenuWidth(), this.mLocateHelper.getMainMenuHeight());
            rotatingContent().setMainMenuChildMeasureSize(this.mMainMenuWidth, this.mMainMenuHeight);
            rotatingContent().setMainMenuAnimTargetSize(this.mMainMenuWidth, this.mMainMenuHeight);
            this.mContentView.setDomain(this.mLocateHelper.getDomain());
            if (!this.mUseRecyclerView) {
                syncMainMenuSize();
            }
        } else {
            this.mContentView.setMainMenuSize(getRotatedLocateWidth(), getRotatedLocateHeight());
            this.mLocateHelper.prepareShowMainMenu(getRotatedLocateWidth(), getRotatedLocateHeight(), z2, this.mGlobalOffsetX, this.mGlobalOffsetY);
            if (!this.mUseRecyclerView) {
                syncMainMenuSize();
            }
        }
        if (!zUseSidePlacement && !z5) {
            applyRotatedSlotIfNeeded();
        }
        applyMainWrapperRotation();
    }

    private boolean listHasExpandableSubMenu(List<PopupListItem> list) {
        if (list != null && !list.isEmpty()) {
            Iterator<PopupListItem> it = list.iterator();
            while (it.hasNext()) {
                if (hasAvailableSubMenu(it.next())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void logMainMenuShowDebug(MainMenuPlacementSpec mainMenuPlacementSpec) {
        String str;
        if (COUI_DEBUG) {
            if (!needsExtendedMainMenuLayout(mainMenuPlacementSpec)) {
                str = "default";
            } else if (useSidePlacement()) {
                str = "side";
            } else {
                str = isRotationOrthogonal() ? "orthogonal" : "legacy";
            }
            StringBuilder sb = new StringBuilder(128);
            sb.append("layout: path=");
            sb.append(str);
            sb.append(" content=");
            sb.append(this.mMainMenuWidth);
            sb.append('x');
            sb.append(this.mMainMenuHeight);
            COUIPopupMenuRootView cOUIPopupMenuRootView = this.mContentView;
            int i3 = cOUIPopupMenuRootView != null ? cOUIPopupMenuRootView.mMainMenuWidth : 0;
            int i4 = cOUIPopupMenuRootView != null ? cOUIPopupMenuRootView.mMainMenuHeight : 0;
            sb.append(" host=");
            sb.append(i3);
            sb.append('x');
            sb.append(i4);
            int mainMenuRotationContentWidth = rotatingLocate().getMainMenuRotationContentWidth();
            int mainMenuRotationContentHeight = rotatingLocate().getMainMenuRotationContentHeight();
            if (mainMenuRotationContentWidth > 0 && mainMenuRotationContentHeight > 0) {
                sb.append(" child=");
                sb.append(mainMenuRotationContentWidth);
                sb.append('x');
                sb.append(mainMenuRotationContentHeight);
            } else if (this.mMainMenuRotatedSlotLayoutActive) {
                sb.append(" child=");
                sb.append(this.mMainMenuWidth);
                sb.append('x');
                sb.append(this.mMainMenuHeight);
            } else {
                sb.append(" child=off");
            }
            int rotatedLocateWidth = getRotatedLocateWidth();
            int rotatedLocateHeight = getRotatedLocateHeight();
            if (rotatedLocateWidth != i3 || rotatedLocateHeight != i4) {
                sb.append(" locate=");
                sb.append(rotatedLocateWidth);
                sb.append('x');
                sb.append(rotatedLocateHeight);
            }
            if (needsExtendedMainMenuLayout(mainMenuPlacementSpec)) {
                sb.append(" maxWidth=");
                sb.append(rotatingLocate().getMainMenuMeasureMaxWidth(getMainMenuMaxWidth()));
            }
            sb.append(" rotation=");
            sb.append(rotationKind().toDisplayDegrees());
            if (this.mMainMenuRotatedSlotLayoutActive) {
                sb.append(" slotClip");
            }
            Log.d(TAG, sb.toString());
        }
    }

    private int mainMenuListMeasureAtMostWidthPx() {
        int mainMenuMaxWidth = getMainMenuMaxWidth();
        return resolveMainMenuListMeasureAtMostWidth(rotatingLocate().getMainMenuMeasureMaxWidth(mainMenuMaxWidth), getMainMenuMinWidth(), mainMenuMaxWidth);
    }

    private boolean needsExtendedMainMenuLayout(MainMenuPlacementSpec mainMenuPlacementSpec) {
        return isRotationNonZero() || mainMenuPlacementSpec.isSideForced() || mainMenuPlacementSpec.isCrossAxisStartOrEnd() || mainMenuPlacementSpec.isVerticalForced();
    }

    private void resetRotatedSlotForVerticalLocate() {
        this.mMainMenuRotatedSlotLayoutActive = false;
        clearSlotViewState();
    }

    private void resetUnsupportedRotationForShow() {
        if (isRotationNonZero() && listHasExpandableSubMenu(this.mMainMenuItemList)) {
            COUILog.w(TAG, "resetUnsupportedRotationForShow: rotation is not supported when the menu has expandable sub menus; reset to 0.");
            this.mMenuContentRotationDegrees = 0.0f;
        }
    }

    public static int resolveMainMenuListMeasureAtMostWidth(int i3, int i4, int i5) {
        if (i4 > 0) {
            return (i3 <= 0 || i3 < i4) ? Math.min(i5, i4) : i3;
        }
        return i3;
    }

    private void restoreSlotIfActive() {
        if (this.mMainMenuRotatedSlotLayoutActive) {
            this.mMainMenuRotatedSlotLayoutActive = false;
            clearSlotViewState();
            PopupMenuLocateHelper popupMenuLocateHelper = this.mLocateHelper;
            if (popupMenuLocateHelper == null || this.mContentView == null) {
                return;
            }
            popupMenuLocateHelper.prepareShowMainMenu(getRotatedLocateWidth(), getRotatedLocateHeight(), this.mLastMainMenuAboveAnchor, this.mGlobalOffsetX, this.mGlobalOffsetY);
            this.mContentView.setDomain(this.mLocateHelper.getDomain());
            this.mContentView.setMainMenuSize(getRotatedLocateWidth(), getRotatedLocateHeight());
            if (this.mUseRecyclerView) {
                return;
            }
            syncMainMenuSize();
        }
    }

    private COUIRotatingPopupMenuRootView rotatingContent() {
        return (COUIRotatingPopupMenuRootView) this.mContentView;
    }

    private RotatingPopupMenuLocateHelper rotatingLocate() {
        return (RotatingPopupMenuLocateHelper) this.mLocateHelper;
    }

    private MenuRotationHelper.RotationQuarter rotationKind() {
        return MenuRotationHelper.classifyDegrees(this.mMenuContentRotationDegrees);
    }

    private static void updateRecyclerViewOverScrollState(RecyclerView recyclerView, boolean z2) {
        if (recyclerView == null) {
            return;
        }
        if (recyclerView instanceof COUIRecyclerView) {
            ((COUIRecyclerView) recyclerView).setOverScrollEnable(z2);
        }
        recyclerView.setOverScrollMode(z2 ? 0 : 2);
        if (z2) {
            return;
        }
        recyclerView.stopScroll();
        recyclerView.scrollToPosition(0);
    }

    private boolean useSidePlacement() {
        PopupMenuLocateHelper popupMenuLocateHelper;
        PopupMenuDomain domain;
        return (!this.mMainMenuPlacementSpec.isSideForced() || (popupMenuLocateHelper = this.mLocateHelper) == null || (domain = popupMenuLocateHelper.getDomain()) == null || domain.mAnchor.isEmpty()) ? false : true;
    }

    public MainMenuPlacementSpec getMainMenuPlacement() {
        return this.mMainMenuPlacementSpec;
    }

    @Override // com.coui.appcompat.poplist.COUIPopupListWindow
    public void measurePopupWindowForRecyclerView(MixedPopupListAdapter mixedPopupListAdapter, List<PopupListItem> list, boolean z2) {
        int i3;
        int i4;
        int i5;
        ListView listView;
        int i6;
        int dividerHeight;
        if (mixedPopupListAdapter == null || mixedPopupListAdapter.getItemCount() == 0) {
            Log.w(TAG, "measurePopupWindowForRecyclerView: adapter is null or empty");
            return;
        }
        if (list == null || list.isEmpty()) {
            Log.w(TAG, "measurePopupWindowForRecyclerView: itemList is null or empty");
            return;
        }
        int dimensionPixelSize = ((COUIPopupListWindow) this).mContext.getResources().getDimensionPixelSize(R.dimen.coui_popup_list_padding_vertical) * 2;
        PopupMenuLocateHelper popupMenuLocateHelper = this.mLocateHelper;
        int maxMainMenuHeight = z2 ? popupMenuLocateHelper.getMaxMainMenuHeight() : popupMenuLocateHelper.getMaxSubMenuHeight();
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(z2 ? mainMenuListMeasureAtMostWidthPx() : getMainMenuMaxWidth(), Integer.MIN_VALUE);
        int i7 = 0;
        int iMakeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(0, 0);
        ListView listView2 = this.mListViewUsedToMeasure;
        RecyclerView recyclerView = z2 ? this.mMainRecyclerView : this.mSubRecyclerView;
        ensureViewPoolCapacity(recyclerView, maxMainMenuHeight, dimensionPixelSize, ((COUIPopupListWindow) this).mContext.getResources().getDimensionPixelSize(R.dimen.coui_popup_list_window_item_min_height));
        int itemCount = mixedPopupListAdapter.getItemCount();
        int i8 = dimensionPixelSize;
        int i9 = 0;
        int i10 = 0;
        boolean z3 = false;
        boolean z4 = false;
        boolean z5 = dimensionPixelSize <= maxMainMenuHeight;
        int i11 = i8;
        int i12 = 0;
        while (i10 < itemCount) {
            int i13 = itemCount;
            int itemViewType = mixedPopupListAdapter.getItemViewType(i10);
            int i14 = i11;
            if (itemViewType == 4 || itemViewType == 3) {
                i5 = iMakeMeasureSpec;
                listView = listView2;
                if (!z3 || z4) {
                    i6 = i14;
                } else {
                    if (z5 && i8 + i12 > maxMainMenuHeight) {
                        z5 = false;
                    }
                    i6 = i14 + i12;
                    i8 += i12;
                    z4 = true;
                }
                dividerHeight = mixedPopupListAdapter.getDividerHeight(itemViewType);
                if (z5 && i8 + dividerHeight > maxMainMenuHeight) {
                    z5 = false;
                }
            } else {
                if (mixedPopupListAdapter.getItem(i10) == null) {
                    listView = listView2;
                } else {
                    RecyclerView.ViewHolder viewHolderCreateViewHolder = mixedPopupListAdapter.createViewHolder(listView2, itemViewType);
                    mixedPopupListAdapter.onBindViewHolder(viewHolderCreateViewHolder, i10);
                    listView = listView2;
                    View view = viewHolderCreateViewHolder.itemView;
                    if (view != null) {
                        view.measure(iMakeMeasureSpec, iMakeMeasureSpec2);
                        recycleMeasuredHolder(viewHolderCreateViewHolder, recyclerView, itemViewType);
                        int measuredWidth = view.getMeasuredWidth();
                        int measuredHeight = view.getMeasuredHeight();
                        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                        i5 = iMakeMeasureSpec;
                        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                            measuredWidth += marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
                            measuredHeight += marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
                        }
                        dividerHeight = measuredHeight;
                        if (itemViewType == 0) {
                            i9 += measuredWidth;
                            if (dividerHeight > i12) {
                                i12 = dividerHeight;
                            }
                            i11 = i14;
                            z3 = true;
                        } else {
                            if (measuredWidth > i7) {
                                i7 = measuredWidth;
                            }
                            if (!z3 || z4) {
                                i6 = i14;
                            } else {
                                if (z5 && i8 + i12 > maxMainMenuHeight) {
                                    z5 = false;
                                }
                                i6 = i14 + i12;
                                i8 += i12;
                                z4 = true;
                            }
                            if (z5 && i8 + dividerHeight > maxMainMenuHeight) {
                                z5 = false;
                            }
                            if (z5) {
                            }
                            i11 = i6 + dividerHeight;
                        }
                    }
                    i10++;
                    itemCount = i13;
                    listView2 = listView;
                    iMakeMeasureSpec = i5;
                }
                i5 = iMakeMeasureSpec;
                i11 = i14;
                i10++;
                itemCount = i13;
                listView2 = listView;
                iMakeMeasureSpec = i5;
            }
            i8 += dividerHeight;
            i11 = i6 + dividerHeight;
            i10++;
            itemCount = i13;
            listView2 = listView;
            iMakeMeasureSpec = i5;
        }
        int i15 = i11;
        if (!z3 || z4) {
            i3 = i8;
            i4 = i15;
        } else {
            i4 = i15 + i12;
            i3 = i8 + i12;
        }
        if (i3 == 0) {
            i3 = maxMainMenuHeight;
        }
        int iMax = Math.max(i9, i7);
        int mainMenuMinWidth = getMainMenuMinWidth();
        int mainMenuMeasureMaxWidth = z2 ? rotatingLocate().getMainMenuMeasureMaxWidth(getMainMenuMaxWidth()) : getMainMenuMaxWidth();
        int iMax2 = Math.max(iMax, mainMenuMinWidth);
        if (mainMenuMeasureMaxWidth > 0 && mainMenuMeasureMaxWidth >= mainMenuMinWidth) {
            iMax2 = Math.min(iMax2, mainMenuMeasureMaxWidth);
        }
        if (z2) {
            this.mMainMenuWidth = iMax2;
            this.mMainMenuHeight = i3;
            if (COUI_DEBUG) {
                Log.d(TAG, "measure: content=" + this.mMainMenuWidth + "x" + this.mMainMenuHeight + " widthLimit=" + mainMenuListMeasureAtMostWidthPx() + " maxWidth=" + rotatingLocate().getMainMenuMeasureMaxWidth(getMainMenuMaxWidth()) + " maxHeight=" + maxMainMenuHeight);
            }
        } else {
            this.mSubMenuWidth = this.mMainMenuWidth;
            this.mSubMenuHeight = i3;
        }
        updateRecyclerViewOverScrollState(z2 ? this.mMainRecyclerView : this.mSubRecyclerView, i4 > i3);
    }

    @Override // com.coui.appcompat.poplist.COUIPopupListWindow
    public COUIPopupMenuRootView onCreatePopupMenuRootView(Context context) {
        return new COUIRotatingPopupMenuRootView(context);
    }

    @Override // com.coui.appcompat.poplist.COUIPopupListWindow
    public void prepareShowMainMenu(View view, int i3, int i4, boolean z2) {
        resetUnsupportedRotationForShow();
        this.mLastMainMenuAboveAnchor = z2;
        resetRotatedSlotForVerticalLocate();
        if (this.mUseRecyclerView) {
            configMainRecyclerView();
        } else {
            configMainListView();
        }
        this.mLocateHelper.prepareWindowAndAnchor(view, i3, i4, this.mRootView);
        MainMenuPlacementSpec mainMenuPlacementSpecResolveFittingSpec = rotatingLocate().resolveFittingSpec(this.mMainMenuPlacementSpec, view, getMainMenuMinWidth(), rotationKind(), this.mMainMenuWidth, this.mMainMenuHeight);
        rotatingLocate().setPlacementSpec(mainMenuPlacementSpecResolveFittingSpec);
        if (needsExtendedMainMenuLayout(mainMenuPlacementSpecResolveFittingSpec)) {
            layoutMainMenuForShow(view, z2, mainMenuPlacementSpecResolveFittingSpec, (this.mMainMenuWidth == 0 || this.mMainMenuHeight == 0) ? false : true, true);
        } else {
            layoutDefaultMainMenu(z2, true);
        }
        logMainMenuShowDebug(mainMenuPlacementSpecResolveFittingSpec);
    }

    public void resetMainMenuPlacement() {
        this.mMainMenuPlacementSpec = MainMenuPlacementSpec.DEFAULT;
    }

    @Override // com.coui.appcompat.poplist.COUIPopupListWindow
    public void reuseMainMenu(boolean z2) {
        COUIRotatingPopupListWindow cOUIRotatingPopupListWindow;
        resetUnsupportedRotationForShow();
        this.mLastMainMenuAboveAnchor = z2;
        resetRotatedSlotForVerticalLocate();
        MainMenuPlacementSpec mainMenuPlacementSpecResolveFittingSpec = rotatingLocate().resolveFittingSpec(this.mMainMenuPlacementSpec, this.mAnchorView, getMainMenuMinWidth(), rotationKind(), this.mMainMenuWidth, this.mMainMenuHeight);
        rotatingLocate().setPlacementSpec(mainMenuPlacementSpecResolveFittingSpec);
        boolean z3 = false;
        if (needsExtendedMainMenuLayout(mainMenuPlacementSpecResolveFittingSpec)) {
            if (this.mMainMenuWidth != 0 && this.mMainMenuHeight != 0) {
                z3 = true;
            }
            cOUIRotatingPopupListWindow = this;
            cOUIRotatingPopupListWindow.layoutMainMenuForShow(this.mAnchorView, z2, mainMenuPlacementSpecResolveFittingSpec, z3, false);
        } else {
            layoutDefaultMainMenu(z2, false);
            cOUIRotatingPopupListWindow = this;
        }
        cOUIRotatingPopupListWindow.logMainMenuShowDebug(mainMenuPlacementSpecResolveFittingSpec);
    }

    @Override // com.coui.appcompat.poplist.COUIPopupListWindow
    public void setItemList(List<PopupListItem> list, boolean z2) {
        if (isRotationNonZero() && listHasExpandableSubMenu(list)) {
            COUILog.w(TAG, "setItemList: expandable sub menus are not supported while menu content rotation is non-zero; list not applied.");
        } else {
            super.setItemList(list, z2);
        }
    }

    public void setMainMenuPlacement(MainMenuPlacementSpec mainMenuPlacementSpec) {
        if (mainMenuPlacementSpec == null) {
            mainMenuPlacementSpec = MainMenuPlacementSpec.DEFAULT;
        }
        this.mMainMenuPlacementSpec = mainMenuPlacementSpec;
    }

    public void setMenuContentRotationDegrees(float f3) {
        if (!Float.isFinite(f3)) {
            this.mMenuContentRotationDegrees = 0.0f;
            return;
        }
        MenuRotationHelper.RotationQuarter rotationQuarterTryParseQuarter = MenuRotationHelper.RotationQuarter.tryParseQuarter(f3);
        if (rotationQuarterTryParseQuarter == null) {
            COUILog.w(TAG, "setMenuContentRotationDegrees: only 0°, 90°, 270° (and equivalent angles) are supported; not supported: " + f3 + " (ignored).");
            return;
        }
        if (rotationQuarterTryParseQuarter.allowsExpandableSubMenu() || !listHasExpandableSubMenu(this.mMainMenuItemList)) {
            this.mMenuContentRotationDegrees = f3;
            return;
        }
        COUILog.w(TAG, "setMenuContentRotationDegrees: rotation is not supported when the menu has expandable sub menus; not supported: " + f3 + " (ignored).");
    }

    @Override // com.coui.appcompat.poplist.COUIPopupListWindow
    public void setNonApplicationType(boolean z2, boolean z3) {
        super.setNonApplicationType(z2, false);
    }

    @Override // com.coui.appcompat.poplist.COUIPopupListWindow
    public void syncMainMenuSize() {
        if (this.mMainMenuRotatedSlotLayoutActive) {
            return;
        }
        int mainMenuWidth = this.mLocateHelper.getMainMenuWidth();
        int mainMenuHeight = this.mLocateHelper.getMainMenuHeight();
        if (!isRotationOrthogonal()) {
            if (mainMenuWidth == this.mMainMenuWidth && mainMenuHeight == this.mMainMenuHeight) {
                return;
            }
            this.mMainMenuWidth = mainMenuWidth;
            this.mMainMenuHeight = mainMenuHeight;
            this.mContentView.setMainMenuSize(mainMenuWidth, mainMenuHeight);
            return;
        }
        if (mainMenuWidth <= 0 || mainMenuHeight <= 0) {
            return;
        }
        this.mContentView.setMainMenuSize(mainMenuWidth, mainMenuHeight);
        int mainMenuRotationContentWidth = rotatingLocate().getMainMenuRotationContentWidth();
        int mainMenuRotationContentHeight = rotatingLocate().getMainMenuRotationContentHeight();
        if (mainMenuRotationContentWidth <= 0 || mainMenuRotationContentHeight <= 0) {
            rotatingContent().setMainMenuChildMeasureSize(this.mMainMenuWidth, this.mMainMenuHeight);
        } else {
            rotatingContent().setMainMenuChildMeasureSize(mainMenuRotationContentWidth, mainMenuRotationContentHeight);
        }
    }
}