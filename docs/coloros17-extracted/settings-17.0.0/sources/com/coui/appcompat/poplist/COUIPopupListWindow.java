package com.coui.appcompat.poplist;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.coui.appcompat.list.IListSelectedItem;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.state.COUIMaskEffectDrawable;
import com.coui.appcompat.state.COUIStateEffectDrawable;
import com.coui.appcompat.state.DrawableStateProxy;
import com.coui.appcompat.uiutil.AnimLevel;
import com.coui.appcompat.uiutil.UIUtil;
import com.coui.component.responsiveui.unit.Dp;
import com.coui.component.responsiveui.window.WindowHeightSizeClass;
import com.coui.component.responsiveui.window.WindowSizeClass;
import com.coui.component.responsiveui.window.WindowWidthSizeClass;
import com.support.poplist.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes7.dex */
public class COUIPopupListWindow extends COUIPopupWindow {
    private static final boolean COUI_DEBUG;
    private static final int DEFAULT_REGION_BASED_LONG_PRESS_TIMEOUT_MS = 650;
    private static final int EXIT_DURATION = 350;
    public static final int GROUP_MIN_ITEMS = 3;
    private static final int HORIZONTAL_ITEM_COUNT = 5;
    private static final int HORIZONTAL_ITEM_COUNT_WITH_TEXT = 3;
    private static final int MENU_CONTAINER_VERTICAL_PADDING_EDGES = 2;
    private static final int RECYCLED_VIEW_POOL_DEFAULT_MAX_SCRAP = 5;
    private static final String TAG = "COUIPopupListWindow";
    View mAnchorView;
    COUIPopupMenuRootView mContentView;
    Context mContext;
    private int mCustomMenuMaxWidth;
    private int mCustomMenuWidth;
    private int mDefaultAnimationStyle;
    private boolean mDismissWhenLayoutChange;
    private boolean mDismissWhenWindowSizeChange;
    private boolean mDismissWithWindowAnimation;
    int mGlobalOffsetX;
    int mGlobalOffsetY;
    private boolean mIfNeedResetFocusableToTrue;
    private boolean mIfNeedResetTouchableToTrue;
    private boolean mIsAdaptiveFontSize;
    private boolean mIsDismissing;
    private boolean mIsFixedFontSize;
    private int mLastClickedMainMenuItemPosition;
    private View mListUnderDetectionForLongPress;
    ListView mListViewUsedToMeasure;
    PopupMenuLocateHelper mLocateHelper;
    private GridLayoutManager mMainGridLayoutManager;

    @Deprecated
    ListView mMainListView;

    @Deprecated
    DefaultAdapter mMainMenuAdapter;
    int mMainMenuHeight;
    private final AdapterView.OnItemClickListener mMainMenuItemClickListener;
    List<PopupListItem> mMainMenuItemList;
    private final AdapterView.OnItemLongClickListener mMainMenuItemLongClickListener;
    int mMainMenuWidth;
    RoundFrameLayout mMainMenuWrapper;
    MixedPopupListAdapter mMainRecyclerAdapter;
    RecyclerView mMainRecyclerView;
    private final View.OnLayoutChangeListener mMenuDismissWhenRootChange;
    private boolean mNeedOffsetWhenSetWindowType;
    private PopupWindow.OnDismissListener mOnDismissListener;
    private OnMainMenuAnimationListener mOnMainMenuAnimationListener;
    private AdapterView.OnItemClickListener mOnMainMenuItemClickListener;
    private AdapterView.OnItemLongClickListener mOnMainMenuItemLongClickListener;
    private AdapterView.OnItemClickListener mOnSubMenuItemClickListener;
    private AdapterView.OnItemLongClickListener mOnSubMenuItemLongClickListener;
    private OnTouchOutsideDismissListener mOnTouchOutsideDismissListener;
    private boolean mRegionBasedLongPressEnabled;
    private boolean mReuseMenuWhenOffsetChanged;
    View mRootView;
    private int mScrollXAtLongPressDetectionStart;
    private int mScrollYAtLongPressDetectionStart;
    private int mShowOffsetX;
    private int mShowOffsetY;
    private GridLayoutManager mSubGridLayoutManager;

    @Deprecated
    ListView mSubListView;

    @Deprecated
    private DefaultAdapter mSubMenuAdapter;
    private View mSubMenuAnchorView;
    int mSubMenuHeight;
    private final AdapterView.OnItemClickListener mSubMenuItemClickListener;
    private final AdapterView.OnItemLongClickListener mSubMenuItemLongClickListener;
    int mSubMenuWidth;
    RoundFrameLayout mSubMenuWrapper;
    private MixedPopupListAdapter mSubRecyclerAdapter;
    RecyclerView mSubRecyclerView;
    private float mTransferredPressProgress;
    private boolean mUsePopupExitAnimation;
    boolean mUseRecyclerView;
    private final Runnable mWindowAnimationDismissRunnable;

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.COUIPopupListWindow$1, reason: invalid class name */
    public class AnonymousClass1 implements View.OnLayoutChangeListener {
        public AnonymousClass1() {
        }

        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View view, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
            boolean z2 = (i3 == i7 && i4 == i8 && i5 == i9 && i6 == i10) ? false : true;
            COUILog.d(COUIPopupListWindow.TAG, "PopupWindow anchor layout changed! left:" + i3 + ",top:" + i4 + ",right:" + i5 + ",bottom:" + i6 + ",oldLeft:" + i7 + ",oldTop:" + i8 + ",oldRight:" + i9 + ",oldBottom:" + i10 + ",layoutChange:" + z2);
            if (z2) {
                if (!COUIPopupListWindow.l(COUIPopupListWindow.this)) {
                    if (!COUIPopupListWindow.m(COUIPopupListWindow.this)) {
                        return;
                    }
                    COUIPopupListWindow cOUIPopupListWindow = COUIPopupListWindow.this;
                    if (!cOUIPopupListWindow.mLocateHelper.checkIfLimitedWindowOrAnchorResized(cOUIPopupListWindow.mAnchorView, COUIPopupListWindow.t(cOUIPopupListWindow), COUIPopupListWindow.u(COUIPopupListWindow.this), COUIPopupListWindow.this.mRootView)) {
                        return;
                    }
                }
                COUIPopupListWindow.this.dismiss();
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.COUIPopupListWindow$2, reason: invalid class name */
    public class AnonymousClass2 implements AdapterView.OnItemClickListener {
        public AnonymousClass2() {
        }

        public static /* synthetic */ void a(AnonymousClass2 anonymousClass2, View view, int i3) {
            COUIPopupListWindow.G(COUIPopupListWindow.this, view, i3);
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i3, long j3) {
            View view2;
            if (DefaultAdapter.isDataIndex(i3)) {
                int iRealPositionToDataIndex = DefaultAdapter.realPositionToDataIndex(i3);
                if (COUIPopupListWindow.r(COUIPopupListWindow.this) != null) {
                    view2 = view;
                    COUIPopupListWindow.r(COUIPopupListWindow.this).onItemClick(adapterView, view2, iRealPositionToDataIndex, j3);
                } else {
                    view2 = view;
                }
                if (COUIPopupListWindow.this.mSubMenuWrapper.getParent() == null || COUIPopupListWindow.p(COUIPopupListWindow.this) == iRealPositionToDataIndex) {
                    COUIPopupListWindow.G(COUIPopupListWindow.this, view2, iRealPositionToDataIndex);
                } else {
                    COUIPopupListWindow.this.mContentView.hideSubMenu(false);
                    COUIPopupListWindow.this.mContentView.postSkipExitAnimationAndShowSubMenu(new o(this, view2, iRealPositionToDataIndex));
                }
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.COUIPopupListWindow$3, reason: invalid class name */
    public class AnonymousClass3 implements AdapterView.OnItemClickListener {
        public AnonymousClass3() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i3, long j3) {
            int iRealPositionToDataIndex = DefaultAdapter.realPositionToDataIndex(i3);
            if (COUIPopupListWindow.this.mLocateHelper.isCurrentContainerSmallScreen()) {
                iRealPositionToDataIndex--;
            }
            int i4 = iRealPositionToDataIndex;
            if (i4 < 0) {
                COUIPopupListWindow.this.mContentView.performSubMenuHeader(view);
            } else if (COUIPopupListWindow.s(COUIPopupListWindow.this) != null) {
                COUIPopupListWindow.s(COUIPopupListWindow.this).onItemClick(adapterView, view, i4, j3);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.COUIPopupListWindow$4, reason: invalid class name */
    public class AnonymousClass4 implements AdapterView.OnItemLongClickListener {
        public AnonymousClass4() {
        }

        @Override // android.widget.AdapterView.OnItemLongClickListener
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i3, long j3) {
            if (!DefaultAdapter.isDataIndex(i3)) {
                return false;
            }
            return COUIPopupListWindow.C(COUIPopupListWindow.this, adapterView, view, DefaultAdapter.realPositionToDataIndex(i3), j3);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.COUIPopupListWindow$5, reason: invalid class name */
    public class AnonymousClass5 implements AdapterView.OnItemLongClickListener {
        public AnonymousClass5() {
        }

        @Override // android.widget.AdapterView.OnItemLongClickListener
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i3, long j3) {
            int iRealPositionToDataIndex = DefaultAdapter.realPositionToDataIndex(i3);
            if (COUIPopupListWindow.this.mLocateHelper.isCurrentContainerSmallScreen()) {
                iRealPositionToDataIndex--;
            }
            return COUIPopupListWindow.D(COUIPopupListWindow.this, adapterView, view, iRealPositionToDataIndex, j3);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.COUIPopupListWindow$6, reason: invalid class name */
    public class AnonymousClass6 implements COUIPopupMenuRootView.OnMenuStateChangedListener {
        public AnonymousClass6() {
        }

        private void requestAccessibilityFocusForListHeader(ViewGroup viewGroup) {
            View childAt = viewGroup.getChildAt(0);
            if (childAt != null) {
                childAt.performAccessibilityAction(64, null);
            }
        }

        private void requestAccessibilityFocusForRecyclerViewHeader(RecyclerView recyclerView) {
            View view;
            RecyclerView.ViewHolder viewHolderFindViewHolderForAdapterPosition = recyclerView.findViewHolderForAdapterPosition(0);
            if (viewHolderFindViewHolderForAdapterPosition == null || (view = viewHolderFindViewHolderForAdapterPosition.itemView) == null) {
                return;
            }
            view.performAccessibilityAction(64, null);
        }

        private void setListViewFocusable(ListView listView, boolean z2) {
            if (listView != null) {
                listView.setFocusable(false);
                for (int i3 = 0; i3 < listView.getChildCount(); i3++) {
                    listView.getChildAt(i3).setFocusable(z2);
                }
            }
        }

        private void setRecyclerViewFocusable(RecyclerView recyclerView, boolean z2) {
            if (recyclerView != null) {
                recyclerView.setFocusable(false);
                for (int i3 = 0; i3 < recyclerView.getChildCount(); i3++) {
                    recyclerView.getChildAt(i3).setFocusable(z2);
                }
            }
        }

        @Override // com.coui.appcompat.poplist.COUIPopupMenuRootView.OnMenuStateChangedListener
        public void onMainMenuAnimationCanceled() {
            COUIPopupListWindow.x(COUIPopupListWindow.this, false);
            COUIPopupListWindow.B(COUIPopupListWindow.this, null);
            COUIPopupListWindow.this.mLocateHelper.clearMainMenuMeasureCaps();
            if (COUIPopupListWindow.q(COUIPopupListWindow.this) != null) {
                COUIPopupListWindow.q(COUIPopupListWindow.this).onMainMenuAnimationCanceled();
            }
            try {
                COUIPopupListWindow.access$101(COUIPopupListWindow.this);
            } catch (IllegalArgumentException e3) {
                COUILog.w(COUIPopupListWindow.TAG, "Failed to dismiss popup window, view may be detached: " + e3.getMessage());
            }
        }

        @Override // com.coui.appcompat.poplist.COUIPopupMenuRootView.OnMenuStateChangedListener
        public void onMainMenuEntered() {
            RecyclerView recyclerView;
            COUIPopupListWindow cOUIPopupListWindow = COUIPopupListWindow.this;
            if (!cOUIPopupListWindow.mUseRecyclerView || (recyclerView = cOUIPopupListWindow.mMainRecyclerView) == null) {
                ListView listView = cOUIPopupListWindow.mMainListView;
                if (listView != null) {
                    requestAccessibilityFocusForListHeader(listView);
                }
            } else {
                requestAccessibilityFocusForRecyclerViewHeader(recyclerView);
            }
            if (COUIPopupListWindow.q(COUIPopupListWindow.this) != null) {
                COUIPopupListWindow.q(COUIPopupListWindow.this).onMainMenuEntered();
            }
        }

        @Override // com.coui.appcompat.poplist.COUIPopupMenuRootView.OnMenuStateChangedListener
        public void onMainMenuExited() {
            COUIPopupListWindow.x(COUIPopupListWindow.this, false);
            COUIPopupListWindow.B(COUIPopupListWindow.this, null);
            COUIPopupListWindow cOUIPopupListWindow = COUIPopupListWindow.this;
            COUIPopupListWindow.E(cOUIPopupListWindow, false, cOUIPopupListWindow.mAnchorView);
            COUIPopupListWindow cOUIPopupListWindow2 = COUIPopupListWindow.this;
            cOUIPopupListWindow2.mMainMenuWidth = 0;
            cOUIPopupListWindow2.mMainMenuHeight = 0;
            cOUIPopupListWindow2.mLocateHelper.clearMainMenuMeasureCaps();
            if (COUIPopupListWindow.q(COUIPopupListWindow.this) != null) {
                COUIPopupListWindow.q(COUIPopupListWindow.this).onMainMenuExited();
            }
            try {
                COUIPopupListWindow.access$001(COUIPopupListWindow.this);
            } catch (IllegalArgumentException e3) {
                COUILog.w(COUIPopupListWindow.TAG, "Failed to dismiss popup window, view may be detached: " + e3.getMessage());
            }
        }

        @Override // com.coui.appcompat.poplist.COUIPopupMenuRootView.OnMenuStateChangedListener
        public void onMainMenuStartToEnter() {
            boolean z2;
            COUIPopupListWindow.x(COUIPopupListWindow.this, false);
            boolean z3 = true;
            if (COUIPopupListWindow.o(COUIPopupListWindow.this)) {
                COUIPopupListWindow.this.setTouchable(true);
                COUIPopupListWindow.w(COUIPopupListWindow.this, false);
                z2 = true;
            } else {
                z2 = false;
            }
            if (COUIPopupListWindow.n(COUIPopupListWindow.this)) {
                COUIPopupListWindow.this.setFocusable(true);
                COUIPopupListWindow.w(COUIPopupListWindow.this, false);
            } else {
                z3 = z2;
            }
            if (z3) {
                COUIPopupListWindow.this.update();
            }
            if (COUIPopupListWindow.q(COUIPopupListWindow.this) != null) {
                COUIPopupListWindow.q(COUIPopupListWindow.this).onMainMenuStartToEnter();
            }
        }

        @Override // com.coui.appcompat.poplist.COUIPopupMenuRootView.OnMenuStateChangedListener
        public void onMainMenuStartToExit() {
            COUIPopupListWindow.x(COUIPopupListWindow.this, true);
            if (COUIPopupListWindow.q(COUIPopupListWindow.this) != null) {
                COUIPopupListWindow.q(COUIPopupListWindow.this).onMainMenuStartToExit();
            }
        }

        @Override // com.coui.appcompat.poplist.COUIPopupMenuRootView.OnMenuStateChangedListener
        public void onSubMenuAnimationCanceled() {
            COUIPopupListWindow.F(COUIPopupListWindow.this, false);
        }

        @Override // com.coui.appcompat.poplist.COUIPopupMenuRootView.OnMenuStateChangedListener
        public void onSubMenuEntered() {
            RecyclerView recyclerView;
            COUIPopupListWindow cOUIPopupListWindow = COUIPopupListWindow.this;
            if (cOUIPopupListWindow.mUseRecyclerView && (recyclerView = cOUIPopupListWindow.mSubRecyclerView) != null) {
                requestAccessibilityFocusForRecyclerViewHeader(recyclerView);
                return;
            }
            ListView listView = cOUIPopupListWindow.mSubListView;
            if (listView != null) {
                requestAccessibilityFocusForListHeader(listView);
            }
        }

        @Override // com.coui.appcompat.poplist.COUIPopupMenuRootView.OnMenuStateChangedListener
        public void onSubMenuExited() {
            RecyclerView recyclerView;
            View view;
            COUIPopupListWindow cOUIPopupListWindow = COUIPopupListWindow.this;
            if (cOUIPopupListWindow.mUseRecyclerView) {
                RecyclerView recyclerView2 = cOUIPopupListWindow.mSubRecyclerView;
                if (recyclerView2 instanceof COUITouchRecyclerView) {
                    COUITouchRecyclerView cOUITouchRecyclerView = (COUITouchRecyclerView) recyclerView2;
                    cOUITouchRecyclerView.resetSharedMaskLockedStates();
                    cOUITouchRecyclerView.clearSharedMask(false);
                }
            }
            if (COUIPopupListWindow.v(COUIPopupListWindow.this) != null) {
                COUIPopupListWindow cOUIPopupListWindow2 = COUIPopupListWindow.this;
                if (!cOUIPopupListWindow2.mUseRecyclerView || (recyclerView = cOUIPopupListWindow2.mSubRecyclerView) == null) {
                    ListView listView = cOUIPopupListWindow2.mSubListView;
                    if (listView != null && listView.getChildAt(0) != null) {
                        COUIPopupListWindow.this.mSubListView.getChildAt(0).setBackground(null);
                    }
                } else {
                    RecyclerView.ViewHolder viewHolderFindViewHolderForAdapterPosition = recyclerView.findViewHolderForAdapterPosition(0);
                    if (viewHolderFindViewHolderForAdapterPosition != null && (view = viewHolderFindViewHolderForAdapterPosition.itemView) != null) {
                        view.setBackground(null);
                    }
                }
                COUIPopupListWindow.B(COUIPopupListWindow.this, null);
            }
        }

        @Override // com.coui.appcompat.poplist.COUIPopupMenuRootView.OnMenuStateChangedListener
        public void onSubMenuStartToEnter() {
            COUIPopupListWindow.F(COUIPopupListWindow.this, true);
            COUIPopupListWindow cOUIPopupListWindow = COUIPopupListWindow.this;
            if (cOUIPopupListWindow.mUseRecyclerView) {
                setRecyclerViewFocusable(cOUIPopupListWindow.mMainRecyclerView, false);
            } else {
                setListViewFocusable(cOUIPopupListWindow.mMainListView, false);
            }
        }

        @Override // com.coui.appcompat.poplist.COUIPopupMenuRootView.OnMenuStateChangedListener
        public void onSubMenuStartToExit() {
            COUIPopupListWindow.F(COUIPopupListWindow.this, false);
            COUIPopupListWindow cOUIPopupListWindow = COUIPopupListWindow.this;
            if (!cOUIPopupListWindow.mUseRecyclerView) {
                setListViewFocusable(cOUIPopupListWindow.mMainListView, true);
                return;
            }
            setRecyclerViewFocusable(cOUIPopupListWindow.mMainRecyclerView, true);
            RecyclerView recyclerView = COUIPopupListWindow.this.mSubRecyclerView;
            if (recyclerView instanceof COUITouchRecyclerView) {
                ((COUITouchRecyclerView) recyclerView).clearSharedMask(true);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.COUIPopupListWindow$7, reason: invalid class name */
    public class AnonymousClass7 extends GridLayoutManager.SpanSizeLookup {
        final /* synthetic */ int val$itemCount;
        final /* synthetic */ MixedPopupListAdapter val$mixedAdapter;
        final /* synthetic */ int val$spanCount;

        public AnonymousClass7(int i3, int i4, MixedPopupListAdapter mixedPopupListAdapter) {
            this.val$itemCount = i3;
            this.val$spanCount = i4;
            this.val$mixedAdapter = mixedPopupListAdapter;
        }

        @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
        public int getSpanSize(int i3) {
            if (i3 < 0 || i3 >= this.val$itemCount) {
                return this.val$spanCount;
            }
            int itemViewType = this.val$mixedAdapter.getItemViewType(i3);
            if (itemViewType == 0) {
                return 1;
            }
            return (itemViewType == 4 || itemViewType == 3) ? this.val$spanCount : this.val$spanCount;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.COUIPopupListWindow$8, reason: invalid class name */
    public class AnonymousClass8 implements RegionBasedLongPressDetector.OnDetectionStateListener {
        public AnonymousClass8() {
        }

        @Override // com.coui.appcompat.poplist.RegionBasedLongPressDetector.OnDetectionStateListener
        public void onDetectionEnded() {
            COUIPopupListWindow.y(COUIPopupListWindow.this, null);
        }

        @Override // com.coui.appcompat.poplist.RegionBasedLongPressDetector.OnDetectionStateListener
        public void onDetectionStarted(View view) {
            if (view == null) {
                return;
            }
            Object parent = view.getParent();
            if (!(parent instanceof RecyclerView)) {
                COUIPopupListWindow.y(COUIPopupListWindow.this, null);
                return;
            }
            COUIPopupListWindow.y(COUIPopupListWindow.this, (View) parent);
            RecyclerView recyclerView = (RecyclerView) parent;
            COUIPopupListWindow.A(COUIPopupListWindow.this, recyclerView.computeVerticalScrollOffset());
            COUIPopupListWindow.z(COUIPopupListWindow.this, recyclerView.computeHorizontalScrollOffset());
        }
    }

    public interface OnMainMenuAnimationListener {
        default void onMainMenuAnimationCanceled() {
        }

        default void onMainMenuEntered() {
        }

        default void onMainMenuExited() {
        }

        default void onMainMenuStartToEnter() {
        }

        default void onMainMenuStartToExit() {
        }
    }

    public interface OnTouchOutsideDismissListener {
        void onTouchOutsideDismiss();
    }

    public static final class PopupMenuNoPredictiveGridLayoutManager extends GridLayoutManager {
        public PopupMenuNoPredictiveGridLayoutManager(Context context, int i3) {
            super(context, i3);
        }

        @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }
    }

    public final class WindowAnimationDismissRunnable implements Runnable {
        public /* synthetic */ WindowAnimationDismissRunnable(COUIPopupListWindow cOUIPopupListWindow, p pVar) {
            this();
        }

        @Override // java.lang.Runnable
        public void run() {
            COUIPopupListWindow.this.forceDismiss();
        }

        private WindowAnimationDismissRunnable() {
        }
    }

    static {
        COUI_DEBUG = COUILog.LOG_DEBUG || COUILog.isLoggable(TAG, 3);
    }

    public COUIPopupListWindow(Context context) {
        super(context);
        this.mMenuDismissWhenRootChange = new AnonymousClass1();
        this.mMainMenuItemClickListener = new AnonymousClass2();
        this.mSubMenuItemClickListener = new AnonymousClass3();
        this.mMainMenuItemLongClickListener = new AnonymousClass4();
        this.mSubMenuItemLongClickListener = new AnonymousClass5();
        this.mWindowAnimationDismissRunnable = new WindowAnimationDismissRunnable(this, null);
        this.mRootView = null;
        this.mSubMenuAnchorView = null;
        this.mUseRecyclerView = false;
        this.mGlobalOffsetX = 0;
        this.mGlobalOffsetY = 0;
        this.mCustomMenuWidth = -1;
        this.mCustomMenuMaxWidth = -1;
        this.mShowOffsetX = Integer.MIN_VALUE;
        this.mShowOffsetY = Integer.MIN_VALUE;
        this.mLastClickedMainMenuItemPosition = -1;
        this.mDefaultAnimationStyle = -1;
        this.mTransferredPressProgress = 0.0f;
        this.mDismissWhenLayoutChange = false;
        this.mDismissWhenWindowSizeChange = true;
        this.mIsAdaptiveFontSize = false;
        this.mIsFixedFontSize = false;
        this.mNeedOffsetWhenSetWindowType = false;
        this.mIsDismissing = false;
        this.mReuseMenuWhenOffsetChanged = true;
        this.mIfNeedResetTouchableToTrue = false;
        this.mIfNeedResetFocusableToTrue = false;
        this.mDismissWithWindowAnimation = false;
        this.mUsePopupExitAnimation = false;
        this.mRegionBasedLongPressEnabled = true;
        this.mContext = context;
        setClippingEnabled(false);
        setTouchModal(false);
        setFocusable(true);
        setOutsideTouchable(true);
        setElevationInPopupwindow(true);
        setExitTransition(null);
        setEnterTransition(null);
        setAnimationStyle(R.style.Animation_COUI_PopupListWindow);
        ListView listView = new ListView(context);
        this.mListViewUsedToMeasure = listView;
        listView.setDivider(null);
        this.mListViewUsedToMeasure.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.mMainMenuItemList = new ArrayList();
        COUIPopupMenuRootView cOUIPopupMenuRootViewCreateContentView = createContentView();
        this.mContentView = cOUIPopupMenuRootViewCreateContentView;
        setContentView(cOUIPopupMenuRootViewCreateContentView);
        this.mLocateHelper = new PopupMenuLocateHelper(this.mContext);
    }

    public static /* bridge */ /* synthetic */ void A(COUIPopupListWindow cOUIPopupListWindow, int i3) {
        cOUIPopupListWindow.mScrollYAtLongPressDetectionStart = i3;
    }

    public static /* bridge */ /* synthetic */ void B(COUIPopupListWindow cOUIPopupListWindow, View view) {
        cOUIPopupListWindow.mSubMenuAnchorView = view;
    }

    public static /* bridge */ /* synthetic */ boolean C(COUIPopupListWindow cOUIPopupListWindow, AdapterView adapterView, View view, int i3, long j3) {
        return cOUIPopupListWindow.dispatchMainMenuMixedItemLongClick(adapterView, view, i3, j3);
    }

    public static /* bridge */ /* synthetic */ boolean D(COUIPopupListWindow cOUIPopupListWindow, AdapterView adapterView, View view, int i3, long j3) {
        return cOUIPopupListWindow.dispatchSubMenuMixedItemLongClick(adapterView, view, i3, j3);
    }

    public static /* bridge */ /* synthetic */ void E(COUIPopupListWindow cOUIPopupListWindow, boolean z2, View view) {
        cOUIPopupListWindow.setAnchorHoveredState(z2, view);
    }

    public static /* bridge */ /* synthetic */ void F(COUIPopupListWindow cOUIPopupListWindow, boolean z2) {
        cOUIPopupListWindow.setSubMenuGroupItemActivation(z2);
    }

    public static /* bridge */ /* synthetic */ void G(COUIPopupListWindow cOUIPopupListWindow, View view, int i3) {
        cOUIPopupListWindow.triggerShowSubMenu(view, i3);
    }

    public static /* synthetic */ void a(COUIPopupListWindow cOUIPopupListWindow, View view, int i3, boolean z2) {
        if (cOUIPopupListWindow.isRecyclerLongPressTargetValid(view, i3, z2)) {
            if (z2) {
                cOUIPopupListWindow.mContentView.hideSubMenuFromLongPress();
            } else if (view.getParent() == cOUIPopupListWindow.mSubRecyclerView) {
                cOUIPopupListWindow.dispatchSubMenuMixedItemLongClick(null, view, i3, cOUIPopupListWindow.getPopupListItemId(cOUIPopupListWindow.getCurrentSubMenuItem(i3)));
            } else {
                cOUIPopupListWindow.dispatchMainMenuMixedItemLongClick(null, view, i3, cOUIPopupListWindow.getPopupListItemId(cOUIPopupListWindow.getMainMenuItem(i3)));
            }
        }
    }

    public static /* synthetic */ void access$001(COUIPopupListWindow cOUIPopupListWindow) {
        super.dismiss();
    }

    public static /* synthetic */ void access$101(COUIPopupListWindow cOUIPopupListWindow) {
        super.dismiss();
    }

    private void applyMenuContainerPaddingForRecyclerView(RecyclerView recyclerView) {
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.coui_popup_list_padding_vertical);
        recyclerView.setPadding(recyclerView.getPaddingLeft(), dimensionPixelSize, recyclerView.getPaddingRight(), dimensionPixelSize);
        recyclerView.setClipToPadding(false);
    }

    private void attachRegionBasedLongPressDetector() {
        COUIPopupMenuRootView cOUIPopupMenuRootView = this.mContentView;
        if (cOUIPopupMenuRootView == null) {
            return;
        }
        if (!this.mUseRecyclerView) {
            cOUIPopupMenuRootView.setRegionBasedLongPressDetector(null);
            return;
        }
        if (!this.mRegionBasedLongPressEnabled) {
            cOUIPopupMenuRootView.setRegionBasedLongPressDetector(null);
            return;
        }
        RegionBasedLongPressDetector regionBasedLongPressDetector = new RegionBasedLongPressDetector(this.mContentView, new k(this));
        regionBasedLongPressDetector.setLongPressTimeout(650L);
        regionBasedLongPressDetector.setOnDetectionStateListener(new AnonymousClass8());
        regionBasedLongPressDetector.setCancelPredicate(new l(this));
        regionBasedLongPressDetector.setOnLongPressListener(new m(this));
        this.mContentView.setRegionBasedLongPressDetector(regionBasedLongPressDetector);
    }

    public static /* synthetic */ void b(COUIPopupListWindow cOUIPopupListWindow, AdapterView adapterView, View view, int i3, long j3) {
        cOUIPopupListWindow.dispatchSubMenuMixedItemClick(adapterView, view, i3, j3);
    }

    public static /* synthetic */ void c(COUIPopupListWindow cOUIPopupListWindow, View view) {
        OnTouchOutsideDismissListener onTouchOutsideDismissListener = cOUIPopupListWindow.mOnTouchOutsideDismissListener;
        if (onTouchOutsideDismissListener != null) {
            onTouchOutsideDismissListener.onTouchOutsideDismiss();
        }
        cOUIPopupListWindow.dismiss();
    }

    private void cancelDelayedDismiss() {
        COUIPopupMenuRootView cOUIPopupMenuRootView = this.mContentView;
        if (cOUIPopupMenuRootView != null) {
            cOUIPopupMenuRootView.removeDelayedDismiss();
        }
    }

    private boolean checkListElementsNotNull(List<?> list) {
        Iterator<?> it = list.iterator();
        while (it.hasNext()) {
            if (it.next() == null) {
                return false;
            }
        }
        return true;
    }

    private boolean checkListNotNull(List<?> list) {
        return (list == null || list.isEmpty()) ? false : true;
    }

    private void configSubListView() {
        this.mSubListView.setAdapter((ListAdapter) this.mSubMenuAdapter);
        this.mSubListView.setOnItemClickListener(this.mSubMenuItemClickListener);
        this.mSubListView.setOnItemLongClickListener(this.mSubMenuItemLongClickListener);
    }

    private void configSubRecyclerView() {
        RecyclerView recyclerView = this.mSubRecyclerView;
        if (recyclerView == null || this.mSubRecyclerAdapter == null) {
            return;
        }
        applyMenuContainerPaddingForRecyclerView(recyclerView);
        this.mSubRecyclerView.setAdapter(this.mSubRecyclerAdapter);
        GridLayoutManager gridLayoutManager = this.mSubGridLayoutManager;
        if (gridLayoutManager != null) {
            this.mSubRecyclerView.setLayoutManager(gridLayoutManager);
        }
        RecyclerView recyclerView2 = this.mSubRecyclerView;
        if (recyclerView2 instanceof COUITouchRecyclerView) {
            COUITouchRecyclerView cOUITouchRecyclerView = (COUITouchRecyclerView) recyclerView2;
            float f3 = this.mTransferredPressProgress;
            if (f3 > 0.0f) {
                cOUITouchRecyclerView.scheduleSharedMaskBoundsReady(0, new n(cOUITouchRecyclerView, f3));
                if (COUI_DEBUG) {
                    Log.d(TAG, "configSubRecyclerView: bounds via pre-draw, then handoff animate " + f3 + " -> 0f");
                }
            }
        }
        this.mTransferredPressProgress = 0.0f;
    }

    private void configureGridLayoutManager(MixedPopupListAdapter mixedPopupListAdapter, boolean z2) {
        GridLayoutManager gridLayoutManagerCreateMenuGridLayoutManager = createMenuGridLayoutManager(mixedPopupListAdapter);
        if (gridLayoutManagerCreateMenuGridLayoutManager == null) {
            Log.w(TAG, "configureGridLayoutManager: adapter is null, forMainMenu=" + z2);
            return;
        }
        if (z2) {
            this.mMainGridLayoutManager = gridLayoutManagerCreateMenuGridLayoutManager;
            this.mUseRecyclerView = true;
            RecyclerView recyclerView = this.mMainRecyclerView;
            if (recyclerView != null) {
                recyclerView.setLayoutManager(gridLayoutManagerCreateMenuGridLayoutManager);
                return;
            } else {
                Log.w(TAG, "configureGridLayoutManager: mMainRecyclerView is null");
                return;
            }
        }
        this.mSubGridLayoutManager = gridLayoutManagerCreateMenuGridLayoutManager;
        this.mUseRecyclerView = true;
        RecyclerView recyclerView2 = this.mSubRecyclerView;
        if (recyclerView2 != null) {
            recyclerView2.setLayoutManager(gridLayoutManagerCreateMenuGridLayoutManager);
        } else {
            Log.w(TAG, "configureGridLayoutManager: mSubRecyclerView is null");
        }
    }

    private COUIPopupMenuRootView createContentView() {
        COUIPopupMenuRootView cOUIPopupMenuRootViewOnCreatePopupMenuRootView = onCreatePopupMenuRootView(this.mContext);
        cOUIPopupMenuRootViewOnCreatePopupMenuRootView.setOnClickListener(new e(this));
        LayoutInflater layoutInflaterFrom = LayoutInflater.from(this.mContext);
        int i3 = R.layout.coui_popup_list_window_layout;
        this.mMainMenuWrapper = (RoundFrameLayout) layoutInflaterFrom.inflate(i3, (ViewGroup) cOUIPopupMenuRootViewOnCreatePopupMenuRootView, false);
        this.mSubMenuWrapper = (RoundFrameLayout) LayoutInflater.from(this.mContext).inflate(i3, (ViewGroup) cOUIPopupMenuRootViewOnCreatePopupMenuRootView, false);
        RoundFrameLayout roundFrameLayout = this.mMainMenuWrapper;
        int i4 = R.id.coui_popup_list_view;
        View viewFindViewById = roundFrameLayout.findViewById(i4);
        View viewFindViewById2 = this.mSubMenuWrapper.findViewById(i4);
        if (viewFindViewById instanceof COUITouchRecyclerView) {
            this.mMainRecyclerView = (COUITouchRecyclerView) viewFindViewById;
            this.mUseRecyclerView = true;
        } else if (viewFindViewById instanceof ListView) {
            this.mMainListView = (ListView) viewFindViewById;
        }
        if (viewFindViewById2 instanceof COUITouchRecyclerView) {
            this.mSubRecyclerView = (COUITouchRecyclerView) viewFindViewById2;
        } else if (viewFindViewById2 instanceof ListView) {
            this.mSubListView = (ListView) viewFindViewById2;
        }
        TypedArray typedArrayObtainStyledAttributes = this.mContext.getTheme().obtainStyledAttributes(new int[]{R.attr.couiPopupWindowBackground});
        Drawable drawable = typedArrayObtainStyledAttributes.getDrawable(0);
        if (drawable == null) {
            drawable = ResourcesCompat.getDrawable(this.mContext.getResources(), R.drawable.coui_popup_window_background, this.mContext.getTheme());
        }
        if (drawable != null) {
            this.mMainMenuWrapper.setBackground(drawable.getConstantState().newDrawable());
            this.mSubMenuWrapper.setBackground(drawable.getConstantState().newDrawable());
        }
        typedArrayObtainStyledAttributes.recycle();
        cOUIPopupMenuRootViewOnCreatePopupMenuRootView.setOnSubMenuStateChangedListener(new AnonymousClass6());
        return cOUIPopupMenuRootViewOnCreatePopupMenuRootView;
    }

    private GridLayoutManager createMenuGridLayoutManager(MixedPopupListAdapter mixedPopupListAdapter) {
        int iMax;
        PopupListItem popupListItem;
        if (mixedPopupListAdapter == null) {
            return null;
        }
        int itemCount = mixedPopupListAdapter.getItemCount();
        List<PopupListItem> menuItemList = mixedPopupListAdapter.getMenuItemList();
        int i3 = 0;
        boolean z2 = false;
        for (int i4 = 0; i4 < itemCount; i4++) {
            if (mixedPopupListAdapter.getItemViewType(i4) == 0) {
                i3++;
                int iConvertAdapterPositionToDataIndex = mixedPopupListAdapter.convertAdapterPositionToDataIndex(i4);
                if (menuItemList != null && iConvertAdapterPositionToDataIndex >= 0 && iConvertAdapterPositionToDataIndex < menuItemList.size() && (popupListItem = menuItemList.get(iConvertAdapterPositionToDataIndex)) != null && !TextUtils.isEmpty(popupListItem.getTitle())) {
                    z2 = true;
                }
            }
        }
        if (i3 > 0) {
            iMax = 3;
            if (!z2) {
                iMax = Math.max(3, Math.min(5, i3));
            }
        } else {
            iMax = 1;
        }
        if (COUI_DEBUG) {
            Log.d(TAG, "createAutoConfiguredPopupMenuGridLayoutManager: horizontalItemCount=" + i3 + ", hasText=" + z2 + ", spanCount=" + iMax);
        }
        PopupMenuNoPredictiveGridLayoutManager popupMenuNoPredictiveGridLayoutManager = new PopupMenuNoPredictiveGridLayoutManager(this.mContext, iMax);
        AnonymousClass7 anonymousClass7 = new AnonymousClass7(itemCount, iMax, mixedPopupListAdapter);
        anonymousClass7.setSpanIndexCacheEnabled(true);
        popupMenuNoPredictiveGridLayoutManager.setSpanSizeLookup(anonymousClass7);
        return popupMenuNoPredictiveGridLayoutManager;
    }

    public static /* synthetic */ boolean d(COUIPopupListWindow cOUIPopupListWindow) {
        View view = cOUIPopupListWindow.mListUnderDetectionForLongPress;
        if (view == null || !(view instanceof RecyclerView)) {
            return false;
        }
        RecyclerView recyclerView = (RecyclerView) view;
        return (recyclerView.computeVerticalScrollOffset() == cOUIPopupListWindow.mScrollYAtLongPressDetectionStart && recyclerView.computeHorizontalScrollOffset() == cOUIPopupListWindow.mScrollXAtLongPressDetectionStart) ? false : true;
    }

    private void dispatchMainMenuMixedItemClick(AdapterView<?> adapterView, View view, int i3, long j3) {
        View view2;
        int i4;
        List<PopupListItem> list;
        PopupListItem popupListItem;
        if (this.mSubMenuWrapper.getParent() != null) {
            dismissSubMenu();
        }
        AdapterView.OnItemClickListener onItemClickListener = this.mOnMainMenuItemClickListener;
        if (onItemClickListener != null) {
            if (adapterView == null) {
                adapterView = this.mListViewUsedToMeasure;
            }
            view2 = view;
            i4 = i3;
            onItemClickListener.onItemClick(adapterView, view2, i4, j3);
        } else {
            view2 = view;
            i4 = i3;
        }
        if (i4 < 0 || (list = this.mMainMenuItemList) == null || i4 >= list.size() || (popupListItem = this.mMainMenuItemList.get(i4)) == null || !popupListItem.isEnable() || !checkListNotNull(popupListItem.getSubMenuItemList()) || !checkListElementsNotNull(popupListItem.getSubMenuItemList())) {
            return;
        }
        if (this.mSubMenuWrapper.getParent() == null || this.mLastClickedMainMenuItemPosition == i4) {
            triggerShowSubMenu(view2, i4);
        } else {
            this.mContentView.hideSubMenu(false);
            this.mContentView.postSkipExitAnimationAndShowSubMenu(new d(this, view2, i4));
        }
    }

    private boolean dispatchMainMenuMixedItemLongClick(AdapterView<?> adapterView, View view, int i3, long j3) {
        View view2;
        int i4;
        boolean zOnItemLongClick;
        PopupListItem mainMenuItem = getMainMenuItem(i3);
        if (mainMenuItem == null || !mainMenuItem.isEnable()) {
            return false;
        }
        boolean zHasAvailableSubMenu = hasAvailableSubMenu(mainMenuItem);
        AdapterView.OnItemLongClickListener onItemLongClickListener = this.mOnMainMenuItemLongClickListener;
        if (onItemLongClickListener != null) {
            if (adapterView == null) {
                adapterView = this.mListViewUsedToMeasure;
            }
            view2 = view;
            i4 = i3;
            zOnItemLongClick = onItemLongClickListener.onItemLongClick(adapterView, view2, i4, j3);
        } else {
            view2 = view;
            i4 = i3;
            zOnItemLongClick = false;
        }
        if (!zHasAvailableSubMenu) {
            return zOnItemLongClick;
        }
        if (this.mSubMenuWrapper.getParent() == null) {
            triggerShowSubMenu(view2, i4);
            return true;
        }
        if (this.mLastClickedMainMenuItemPosition == i4) {
            this.mContentView.hideSubMenu(true);
        } else {
            this.mContentView.hideSubMenu(false);
            this.mContentView.postSkipExitAnimationAndShowSubMenu(new f(this, view2, i4));
        }
        return true;
    }

    private void dispatchSubMenuMixedItemClick(AdapterView<?> adapterView, View view, int i3, long j3) {
        if (this.mLocateHelper.isCurrentContainerSmallScreen()) {
            i3--;
        }
        int i4 = i3;
        if (i4 < 0) {
            this.mContentView.performSubMenuHeader(view);
            return;
        }
        AdapterView.OnItemClickListener onItemClickListener = this.mOnSubMenuItemClickListener;
        if (onItemClickListener != null) {
            if (adapterView == null) {
                adapterView = this.mListViewUsedToMeasure;
            }
            onItemClickListener.onItemClick(adapterView, view, i4, j3);
        }
    }

    private boolean dispatchSubMenuMixedItemLongClick(AdapterView<?> adapterView, View view, int i3, long j3) {
        AdapterView.OnItemLongClickListener onItemLongClickListener;
        if (i3 < 0) {
            this.mContentView.hideSubMenuFromLongPress();
            return true;
        }
        PopupListItem currentSubMenuItem = getCurrentSubMenuItem(i3);
        if (currentSubMenuItem == null || !currentSubMenuItem.isEnable() || (onItemLongClickListener = this.mOnSubMenuItemLongClickListener) == null) {
            return false;
        }
        if (adapterView == null) {
            adapterView = this.mListViewUsedToMeasure;
        }
        return onItemLongClickListener.onItemLongClick(adapterView, view, i3, j3);
    }

    public static /* synthetic */ int e(PopupListItem popupListItem, PopupListItem popupListItem2) {
        return popupListItem.getGroupId() - popupListItem2.getGroupId();
    }

    public static /* synthetic */ RegionBasedLongPressDetector.Target f(COUIPopupListWindow cOUIPopupListWindow, View view, float f3, float f4) {
        cOUIPopupListWindow.getClass();
        if (!(view instanceof COUIPopupMenuRootView)) {
            return null;
        }
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        COUIPopupMenuRootView cOUIPopupMenuRootView = (COUIPopupMenuRootView) view;
        RegionBasedLongPressDetector.Target targetResolveSubMenuLongPressTarget = cOUIPopupListWindow.resolveSubMenuLongPressTarget(iArr, f3, f4);
        if (targetResolveSubMenuLongPressTarget != null) {
            return targetResolveSubMenuLongPressTarget;
        }
        if (cOUIPopupMenuRootView.isSubMenuAttached() && cOUIPopupMenuRootView.isTouchPointInSubMenuRegion(f3, f4)) {
            return null;
        }
        return cOUIPopupListWindow.resolveMainMenuLongPressTarget(iArr, f3, f4);
    }

    public static /* synthetic */ void g(COUIPopupListWindow cOUIPopupListWindow, View view, int i3) {
        cOUIPopupListWindow.triggerShowSubMenu(view, i3);
    }

    private PopupListItem getCurrentSubMenuItem(int i3) {
        List<PopupListItem> currentSubMenuItemList = getCurrentSubMenuItemList();
        if (i3 < 0 || i3 >= currentSubMenuItemList.size()) {
            return null;
        }
        return currentSubMenuItemList.get(i3);
    }

    private List<PopupListItem> getCurrentSubMenuItemList() {
        PopupListItem mainMenuItem = getMainMenuItem(this.mLastClickedMainMenuItemPosition);
        return (mainMenuItem == null || mainMenuItem.getSubMenuItemList() == null) ? Collections.EMPTY_LIST : mainMenuItem.getSubMenuItemList();
    }

    private PopupListItem getMainMenuItem(int i3) {
        List<PopupListItem> list = this.mMainMenuItemList;
        if (list == null || i3 < 0 || i3 >= list.size()) {
            return null;
        }
        return this.mMainMenuItemList.get(i3);
    }

    private int getPoolRowCap(int i3, int i4, int i5) {
        return Math.max(1, ((Math.max(0, i3 - i4) + i5) - 1) / i5);
    }

    private long getPopupListItemId(PopupListItem popupListItem) {
        if (popupListItem == null) {
            return Long.MIN_VALUE;
        }
        return popupListItem.getId();
    }

    public static /* synthetic */ void h(COUIPopupListWindow cOUIPopupListWindow, View view, int i3) {
        cOUIPopupListWindow.triggerShowSubMenu(view, i3);
    }

    public static /* synthetic */ void i(COUIPopupListWindow cOUIPopupListWindow, AdapterView adapterView, View view, int i3, long j3) {
        cOUIPopupListWindow.dispatchMainMenuMixedItemClick(adapterView, view, i3, j3);
    }

    private boolean isRecyclerLongPressTargetValid(View view, int i3, boolean z2) {
        if (!(view.getParent() instanceof RecyclerView)) {
            return true;
        }
        RecyclerView recyclerView = (RecyclerView) view.getParent();
        int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
        if (childAdapterPosition == -1) {
            return false;
        }
        if (recyclerView != this.mSubRecyclerView) {
            if (recyclerView.getAdapter() instanceof MixedPopupListAdapter) {
                childAdapterPosition = ((MixedPopupListAdapter) recyclerView.getAdapter()).convertAdapterPositionToDataIndex(childAdapterPosition);
            }
            return childAdapterPosition >= 0 && childAdapterPosition == i3;
        }
        if ((recyclerView.getAdapter() instanceof MixedPopupListAdapter) && (childAdapterPosition = ((MixedPopupListAdapter) recyclerView.getAdapter()).convertAdapterPositionToDataIndex(childAdapterPosition)) < 0) {
            return false;
        }
        if (this.mLocateHelper.isCurrentContainerSmallScreen()) {
            if (childAdapterPosition == 0) {
                return z2;
            }
            childAdapterPosition--;
        }
        return !z2 && childAdapterPosition == i3;
    }

    private boolean isRtl(View view) {
        return ViewCompat.getLayoutDirection(view) == 1;
    }

    @Deprecated
    public static boolean isSmallScreen(Context context, int i3, int i4) {
        WindowSizeClass.Companion companion = WindowSizeClass.INSTANCE;
        Dp.Companion companion2 = Dp.INSTANCE;
        WindowSizeClass windowSizeClassCalculateFromSize = companion.calculateFromSize(companion2.pixel2Dp(context, Math.abs(i3)), companion2.pixel2Dp(context, Math.abs(i4)));
        return windowSizeClassCalculateFromSize.getWindowWidthSizeClass() == WindowWidthSizeClass.Compact || windowSizeClassCalculateFromSize.getWindowHeightSizeClass() == WindowHeightSizeClass.Compact;
    }

    public static /* synthetic */ int j(PopupListItem popupListItem, PopupListItem popupListItem2) {
        return popupListItem.getGroupId() - popupListItem2.getGroupId();
    }

    public static /* synthetic */ void k(COUITouchRecyclerView cOUITouchRecyclerView, float f3) {
        cOUITouchRecyclerView.beginSharedMaskPressHandoffThenAnimate(f3, 0.0f, true);
    }

    public static /* bridge */ /* synthetic */ boolean l(COUIPopupListWindow cOUIPopupListWindow) {
        return cOUIPopupListWindow.mDismissWhenLayoutChange;
    }

    public static /* bridge */ /* synthetic */ boolean m(COUIPopupListWindow cOUIPopupListWindow) {
        return cOUIPopupListWindow.mDismissWhenWindowSizeChange;
    }

    public static /* bridge */ /* synthetic */ boolean n(COUIPopupListWindow cOUIPopupListWindow) {
        return cOUIPopupListWindow.mIfNeedResetFocusableToTrue;
    }

    public static /* bridge */ /* synthetic */ boolean o(COUIPopupListWindow cOUIPopupListWindow) {
        return cOUIPopupListWindow.mIfNeedResetTouchableToTrue;
    }

    public static /* bridge */ /* synthetic */ int p(COUIPopupListWindow cOUIPopupListWindow) {
        return cOUIPopupListWindow.mLastClickedMainMenuItemPosition;
    }

    public static /* bridge */ /* synthetic */ OnMainMenuAnimationListener q(COUIPopupListWindow cOUIPopupListWindow) {
        return cOUIPopupListWindow.mOnMainMenuAnimationListener;
    }

    public static /* bridge */ /* synthetic */ AdapterView.OnItemClickListener r(COUIPopupListWindow cOUIPopupListWindow) {
        return cOUIPopupListWindow.mOnMainMenuItemClickListener;
    }

    private void refreshAdapter(List<PopupListItem> list, DefaultAdapter defaultAdapter) {
        defaultAdapter.setAdapterFontSize(this.mIsAdaptiveFontSize);
        defaultAdapter.setIsFixedFontSize(this.mIsFixedFontSize);
        defaultAdapter.setItemList(list);
    }

    private void resetRecyclerViewSharedMaskLockedStates() {
        if (this.mUseRecyclerView) {
            RecyclerView recyclerView = this.mMainRecyclerView;
            if (recyclerView instanceof COUITouchRecyclerView) {
                ((COUITouchRecyclerView) recyclerView).resetSharedMaskLockedStates();
            }
        }
    }

    private RegionBasedLongPressDetector.Target resolveMainMenuLongPressTarget(int[] iArr, float f3, float f4) {
        RecyclerView recyclerView;
        int childAdapterPosition;
        PopupListItem mainMenuItem;
        List<PopupListItem> list = this.mMainMenuItemList;
        if (list != null && !list.isEmpty() && (recyclerView = this.mMainRecyclerView) != null) {
            int[] iArr2 = new int[2];
            recyclerView.getLocationInWindow(iArr2);
            View viewFindChildViewUnder = this.mMainRecyclerView.findChildViewUnder((f3 + iArr[0]) - iArr2[0], (f4 + iArr[1]) - iArr2[1]);
            if (viewFindChildViewUnder == null || (childAdapterPosition = this.mMainRecyclerView.getChildAdapterPosition(viewFindChildViewUnder)) == -1) {
                return null;
            }
            MixedPopupListAdapter mixedPopupListAdapter = this.mMainRecyclerAdapter;
            if ((mixedPopupListAdapter == null || (childAdapterPosition = mixedPopupListAdapter.convertAdapterPositionToDataIndex(childAdapterPosition)) >= 0) && (mainMenuItem = getMainMenuItem(childAdapterPosition)) != null && mainMenuItem.isEnable() && hasAvailableSubMenu(mainMenuItem)) {
                return new RegionBasedLongPressDetector.Target(viewFindChildViewUnder, childAdapterPosition, true);
            }
            return null;
        }
        return null;
    }

    private RegionBasedLongPressDetector.Target resolveSubMenuLongPressTarget(int[] iArr, float f3, float f4) {
        RecyclerView recyclerView;
        int childAdapterPosition;
        if (this.mSubMenuWrapper.getParent() != null && (recyclerView = this.mSubRecyclerView) != null) {
            int[] iArr2 = new int[2];
            recyclerView.getLocationInWindow(iArr2);
            View viewFindChildViewUnder = this.mSubRecyclerView.findChildViewUnder((f3 + iArr[0]) - iArr2[0], (f4 + iArr[1]) - iArr2[1]);
            if (viewFindChildViewUnder == null || (childAdapterPosition = this.mSubRecyclerView.getChildAdapterPosition(viewFindChildViewUnder)) == -1) {
                return null;
            }
            MixedPopupListAdapter mixedPopupListAdapter = this.mSubRecyclerAdapter;
            if (mixedPopupListAdapter != null && (childAdapterPosition = mixedPopupListAdapter.convertAdapterPositionToDataIndex(childAdapterPosition)) < 0) {
                return null;
            }
            if (this.mLocateHelper.isCurrentContainerSmallScreen()) {
                if (childAdapterPosition == 0) {
                    return new RegionBasedLongPressDetector.Target(viewFindChildViewUnder, -1, true, true);
                }
                childAdapterPosition--;
            }
            PopupListItem currentSubMenuItem = getCurrentSubMenuItem(childAdapterPosition);
            if (currentSubMenuItem != null && currentSubMenuItem.isEnable() && this.mOnSubMenuItemLongClickListener != null) {
                return new RegionBasedLongPressDetector.Target(viewFindChildViewUnder, childAdapterPosition, false);
            }
        }
        return null;
    }

    public static /* bridge */ /* synthetic */ AdapterView.OnItemClickListener s(COUIPopupListWindow cOUIPopupListWindow) {
        return cOUIPopupListWindow.mOnSubMenuItemClickListener;
    }

    private void setAnchorHoveredState(boolean z2, View view) {
        if (view != null && (view instanceof IListSelectedItem)) {
            if (view.getBackground() instanceof DrawableStateProxy) {
                ((DrawableStateProxy) view.getBackground()).setStateLocked(16843623, z2, z2, true);
            }
            if (view.getBackground() instanceof COUIStateEffectDrawable) {
                ((COUIStateEffectDrawable) view.getBackground()).setStateLocked(16843623, z2, z2, true);
            }
        }
    }

    private void setItemListInternal(List<PopupListItem> list, DefaultAdapter defaultAdapter, boolean z2) {
        HashSet hashSet;
        if (list.size() >= 3) {
            if (z2) {
                Collections.sort(list, new g());
            }
            hashSet = new HashSet();
            int groupId = list.get(0).getGroupId();
            for (int i3 = 1; i3 < list.size(); i3++) {
                int groupId2 = list.get(i3).getGroupId();
                if (groupId2 != groupId) {
                    hashSet.add(Integer.valueOf(i3));
                    groupId = groupId2;
                }
            }
        } else {
            hashSet = null;
        }
        if (hashSet != null) {
            defaultAdapter.setGroupSets(hashSet);
        }
        refreshAdapter(list, defaultAdapter);
    }

    private void setSubMenuGroupItemActivation(boolean z2) {
        COUIMaskEffectDrawable sharedMaskDrawable;
        int i3;
        if (!this.mUseRecyclerView) {
            if (this.mSubMenuAdapter == null) {
                return;
            }
            if (this.mLocateHelper.isCurrentContainerSmallScreen()) {
                i3 = z2 ? 2 : 0;
                Object item = this.mSubMenuAdapter.getItem(0);
                if (item instanceof PopupListItem) {
                    ((PopupListItem) item).setGroupState(i3);
                    this.mSubMenuAdapter.notifyDataSetChanged();
                    return;
                }
                return;
            }
            int i4 = this.mLastClickedMainMenuItemPosition;
            if (i4 != -1) {
                Object item2 = this.mMainMenuAdapter.getItem(DefaultAdapter.dataIndexToRealPosition(i4));
                if (item2 instanceof PopupListItem) {
                    ((PopupListItem) item2).setGroupState(z2 ? 1 : 0);
                    this.mMainMenuAdapter.notifyDataSetChanged();
                }
            }
            View view = this.mSubMenuAnchorView;
            if (view == null || !(view.getBackground() instanceof ListItemMaskEffectDrawable)) {
                return;
            }
            ((ListItemMaskEffectDrawable) this.mSubMenuAnchorView.getBackground()).setHoverStateLocked(z2, z2, true);
            return;
        }
        PopupListItem popupListItem = null;
        if (this.mLocateHelper.isCurrentContainerSmallScreen()) {
            i3 = z2 ? 2 : 0;
            int i5 = this.mLastClickedMainMenuItemPosition;
            if (i5 >= 0 && i5 < this.mMainMenuItemList.size()) {
                popupListItem = this.mMainMenuItemList.get(this.mLastClickedMainMenuItemPosition);
            }
            if (popupListItem != null) {
                popupListItem.setGroupState(i3);
                MixedPopupListAdapter mixedPopupListAdapter = this.mSubRecyclerAdapter;
                if (mixedPopupListAdapter != null) {
                    mixedPopupListAdapter.notifyDataSetChanged();
                    return;
                }
                return;
            }
            return;
        }
        int i6 = this.mLastClickedMainMenuItemPosition;
        if (i6 != -1) {
            if (i6 >= 0 && i6 < this.mMainMenuItemList.size()) {
                popupListItem = this.mMainMenuItemList.get(this.mLastClickedMainMenuItemPosition);
            }
            if (popupListItem != null) {
                popupListItem.setGroupState(z2 ? 1 : 0);
                MixedPopupListAdapter mixedPopupListAdapter2 = this.mMainRecyclerAdapter;
                if (mixedPopupListAdapter2 != null) {
                    mixedPopupListAdapter2.notifyDataSetChanged();
                }
            }
        }
        if (this.mSubMenuAnchorView != null) {
            RecyclerView recyclerView = this.mMainRecyclerView;
            if (!(recyclerView instanceof COUITouchRecyclerView) || (sharedMaskDrawable = ((COUITouchRecyclerView) recyclerView).getSharedMaskDrawable()) == null) {
                return;
            }
            sharedMaskDrawable.setHoverStateLocked(z2, z2, true);
        }
    }

    private void showSub(View view, int i3) {
        ArrayList arrayList;
        PopupListItem popupListItem;
        if (this.mSubMenuWrapper.getParent() != null && i3 == this.mLastClickedMainMenuItemPosition) {
            this.mContentView.showSubMenu();
            return;
        }
        if (this.mUseRecyclerView) {
            configSubRecyclerView();
            if (this.mSubRecyclerAdapter != null) {
                int i4 = this.mLastClickedMainMenuItemPosition;
                if (i4 < 0 || i4 >= this.mMainMenuItemList.size() || (popupListItem = this.mMainMenuItemList.get(this.mLastClickedMainMenuItemPosition)) == null || popupListItem.getSubMenuItemList() == null) {
                    arrayList = null;
                } else {
                    arrayList = new ArrayList();
                    if (this.mLocateHelper.isCurrentContainerSmallScreen()) {
                        arrayList.add(popupListItem);
                    }
                    arrayList.addAll(popupListItem.getSubMenuItemList());
                }
                if (arrayList == null || arrayList.isEmpty()) {
                    Log.w(TAG, "showSub: Sub menu item list is null or empty, using default measurement");
                    this.mSubMenuWidth = this.mMainMenuWidth;
                    this.mSubMenuHeight = this.mLocateHelper.getMaxSubMenuHeight();
                } else {
                    measurePopupWindowForRecyclerView(this.mSubRecyclerAdapter, arrayList, false);
                }
            } else {
                Log.w(TAG, "showSub: Sub menu RecyclerView adapter is null, using default measurement");
                this.mSubMenuWidth = this.mMainMenuWidth;
                this.mSubMenuHeight = this.mLocateHelper.getMaxSubMenuHeight();
            }
        } else {
            configSubListView();
            measurePopupWindow(this.mSubMenuAdapter);
        }
        this.mContentView.setSubMenuSize(this.mSubMenuWidth, this.mSubMenuHeight);
        this.mLocateHelper.prepareShowSubMenu(this.mMainMenuWrapper, view, this.mSubMenuWidth, this.mSubMenuHeight, isRtl(view));
        if (!this.mUseRecyclerView) {
            syncSubMenuSize();
        }
        this.mContentView.addSubMenuView(this.mSubMenuWrapper);
    }

    public static /* bridge */ /* synthetic */ int t(COUIPopupListWindow cOUIPopupListWindow) {
        return cOUIPopupListWindow.mShowOffsetX;
    }

    /* JADX WARN: Code duplicated, block: B:11:0x0035  */
    /* JADX WARN: Code duplicated, block: B:7:0x001b  */
    private void triggerShowSubMenu(View view, int i3) {
        PopupListItem popupListItem;
        if (this.mUseRecyclerView) {
            RecyclerView recyclerView = this.mMainRecyclerView;
            if (recyclerView instanceof COUITouchRecyclerView) {
                this.mTransferredPressProgress = ((COUITouchRecyclerView) recyclerView).getSharedMaskPressProgress();
                ((COUITouchRecyclerView) this.mMainRecyclerView).clearSharedMask(true);
            } else if (view == null && (view.getBackground() instanceof ListItemMaskEffectDrawable)) {
                ListItemMaskEffectDrawable listItemMaskEffectDrawable = (ListItemMaskEffectDrawable) view.getBackground();
                this.mTransferredPressProgress = listItemMaskEffectDrawable.getPressProgressFraction();
                listItemMaskEffectDrawable.setTouchExited();
            } else {
                this.mTransferredPressProgress = 0.0f;
            }
        } else if (view == null) {
            this.mTransferredPressProgress = 0.0f;
        } else {
            this.mTransferredPressProgress = 0.0f;
        }
        this.mLastClickedMainMenuItemPosition = i3;
        if (this.mMainMenuItemList.isEmpty() || this.mMainMenuItemList.size() <= i3 || (popupListItem = this.mMainMenuItemList.get(i3)) == null || !popupListItem.isEnable() || !checkListNotNull(popupListItem.getSubMenuItemList()) || !checkListElementsNotNull(popupListItem.getSubMenuItemList())) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        if (this.mLocateHelper.isCurrentContainerSmallScreen()) {
            arrayList.add(popupListItem);
        }
        this.mLocateHelper.setSubMenuAnchorIsFirstItem(i3 == 0);
        arrayList.addAll(popupListItem.getSubMenuItemList());
        if (view == null) {
            Log.w(TAG, "triggerShowSubMenu: view is null (ListView path)");
            return;
        }
        if (this.mUseRecyclerView) {
            if (this.mSubRecyclerAdapter == null) {
                this.mSubRecyclerAdapter = new MixedPopupListAdapter(this.mContext);
            }
            setItemListInternal((List<PopupListItem>) arrayList, this.mSubRecyclerAdapter, false);
        } else {
            if (this.mSubMenuAdapter == null) {
                this.mSubMenuAdapter = new DefaultAdapter(this.mContext, null);
            }
            setItemListInternal((List<PopupListItem>) arrayList, this.mSubMenuAdapter, false);
            if (view.getBackground() instanceof ListItemMaskEffectDrawable) {
                this.mSubMenuAdapter.setSharedBackground((ListItemMaskEffectDrawable) view.getBackground());
            }
        }
        this.mSubMenuAnchorView = view;
        showSub(view, i3);
    }

    public static /* bridge */ /* synthetic */ int u(COUIPopupListWindow cOUIPopupListWindow) {
        return cOUIPopupListWindow.mShowOffsetY;
    }

    public static /* bridge */ /* synthetic */ View v(COUIPopupListWindow cOUIPopupListWindow) {
        return cOUIPopupListWindow.mSubMenuAnchorView;
    }

    public static /* bridge */ /* synthetic */ void w(COUIPopupListWindow cOUIPopupListWindow, boolean z2) {
        cOUIPopupListWindow.mIfNeedResetTouchableToTrue = z2;
    }

    public static /* bridge */ /* synthetic */ void x(COUIPopupListWindow cOUIPopupListWindow, boolean z2) {
        cOUIPopupListWindow.mIsDismissing = z2;
    }

    public static /* bridge */ /* synthetic */ void y(COUIPopupListWindow cOUIPopupListWindow, View view) {
        cOUIPopupListWindow.mListUnderDetectionForLongPress = view;
    }

    public static /* bridge */ /* synthetic */ void z(COUIPopupListWindow cOUIPopupListWindow, int i3) {
        cOUIPopupListWindow.mScrollXAtLongPressDetectionStart = i3;
    }

    public void configMainListView() {
        this.mLastClickedMainMenuItemPosition = -1;
        this.mMainListView.setAdapter((ListAdapter) this.mMainMenuAdapter);
        if (this.mOnMainMenuItemClickListener != null) {
            this.mMainListView.setOnItemClickListener(this.mMainMenuItemClickListener);
        }
        this.mMainListView.setOnItemLongClickListener(this.mMainMenuItemLongClickListener);
    }

    public void configMainRecyclerView() {
        MixedPopupListAdapter mixedPopupListAdapter;
        this.mLastClickedMainMenuItemPosition = -1;
        RecyclerView recyclerView = this.mMainRecyclerView;
        if (recyclerView == null || this.mMainRecyclerAdapter == null) {
            Log.w(TAG, "configMainRecyclerView: mMainRecyclerView or mMainRecyclerAdapter is null");
            return;
        }
        applyMenuContainerPaddingForRecyclerView(recyclerView);
        GridLayoutManager gridLayoutManager = this.mMainGridLayoutManager;
        if (gridLayoutManager != null) {
            this.mMainRecyclerView.setLayoutManager(gridLayoutManager);
        } else {
            Log.w(TAG, "configMainRecyclerView: mMainGridLayoutManager is null!");
        }
        if (this.mOnMainMenuItemClickListener != null && (mixedPopupListAdapter = this.mMainRecyclerAdapter) != null) {
            mixedPopupListAdapter.setOnItemClickListener(new h(this));
        }
        MixedPopupListAdapter mixedPopupListAdapter2 = this.mMainRecyclerAdapter;
        if (mixedPopupListAdapter2 != null) {
            mixedPopupListAdapter2.setOnItemLongClickListener(this.mOnMainMenuItemLongClickListener);
        }
        this.mMainRecyclerView.setAdapter(this.mMainRecyclerAdapter);
        attachRegionBasedLongPressDetector();
    }

    @Deprecated
    public boolean configPopupValue(View view, boolean z2) {
        return false;
    }

    @Override // android.widget.PopupWindow
    public void dismiss() {
        boolean z2;
        COUIPopupMenuRootView cOUIPopupMenuRootView = this.mContentView;
        if (cOUIPopupMenuRootView != null && !cOUIPopupMenuRootView.isAttachedToWindow()) {
            forceDismiss();
        }
        if (isTouchable()) {
            setTouchable(false);
            this.mIfNeedResetTouchableToTrue = true;
            z2 = true;
        } else {
            z2 = false;
        }
        if (isFocusable()) {
            setFocusable(false);
            this.mIfNeedResetFocusableToTrue = true;
            z2 = true;
        }
        if (z2) {
            update();
        }
        if (!isShowing() || this.mIsDismissing) {
            return;
        }
        cancelDelayedDismiss();
        if (this.mUsePopupExitAnimation) {
            forceDismiss();
            return;
        }
        if (this.mDismissWithWindowAnimation) {
            this.mIsDismissing = true;
            COUIPopupMenuRootView cOUIPopupMenuRootView2 = this.mContentView;
            if (cOUIPopupMenuRootView2 != null) {
                cOUIPopupMenuRootView2.postDelayedDismiss(this.mWindowAnimationDismissRunnable, 350L);
            }
        } else {
            View view = this.mAnchorView;
            if (view != null && view.getRootView() != null) {
                this.mAnchorView.getRootView().removeOnLayoutChangeListener(this.mMenuDismissWhenRootChange);
            }
            if (this.mUseRecyclerView) {
                if (this.mLastClickedMainMenuItemPosition != -1) {
                    COUILog.d(TAG, "LastClickedMainMenuItemPosition = " + this.mLastClickedMainMenuItemPosition);
                    int i3 = this.mLastClickedMainMenuItemPosition;
                    PopupListItem popupListItem = (i3 < 0 || i3 >= this.mMainMenuItemList.size()) ? null : this.mMainMenuItemList.get(this.mLastClickedMainMenuItemPosition);
                    if (popupListItem != null) {
                        popupListItem.setGroupState(0);
                        MixedPopupListAdapter mixedPopupListAdapter = this.mMainRecyclerAdapter;
                        if (mixedPopupListAdapter != null) {
                            mixedPopupListAdapter.notifyDataSetChanged();
                        }
                    }
                }
                resetRecyclerViewSharedMaskLockedStates();
                COUIPopupMenuRootView cOUIPopupMenuRootView3 = this.mContentView;
                if (cOUIPopupMenuRootView3 != null) {
                    cOUIPopupMenuRootView3.hideMainMenu(true);
                }
            } else {
                if (this.mLastClickedMainMenuItemPosition != -1 && this.mMainMenuAdapter != null) {
                    COUILog.d(TAG, "LastClickedMainMenuItemPosition = " + this.mLastClickedMainMenuItemPosition);
                    Object item = this.mMainMenuAdapter.getItem(DefaultAdapter.dataIndexToRealPosition(this.mLastClickedMainMenuItemPosition));
                    if (item instanceof PopupListItem) {
                        ((PopupListItem) item).setGroupState(0);
                    }
                }
                COUIPopupMenuRootView cOUIPopupMenuRootView4 = this.mContentView;
                if (cOUIPopupMenuRootView4 != null) {
                    cOUIPopupMenuRootView4.hideMainMenu(true);
                }
            }
            setAnchorHoveredState(false, this.mAnchorView);
        }
        PopupWindow.OnDismissListener onDismissListener = this.mOnDismissListener;
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
    }

    public void dismissSubMenu() {
        COUIPopupMenuRootView cOUIPopupMenuRootView = this.mContentView;
        if (cOUIPopupMenuRootView != null) {
            cOUIPopupMenuRootView.hideSubMenu(true);
        }
    }

    public void ensureViewPoolCapacity(RecyclerView recyclerView, int i3, int i4, int i5) {
        int poolRowCap;
        if (recyclerView != null && (poolRowCap = getPoolRowCap(i3, i4, i5)) > 5) {
            recyclerView.getRecycledViewPool().setMaxRecycledViews(1, poolRowCap);
        }
    }

    public void forceDismiss() {
        COUIPopupMenuRootView cOUIPopupMenuRootView;
        if (super.isShowing()) {
            if (!this.mUsePopupExitAnimation && (cOUIPopupMenuRootView = this.mContentView) != null && cOUIPopupMenuRootView.isAttachedToWindow()) {
                this.mContentView.stopAllAnimationsImmediately();
            }
            this.mLocateHelper.clearMainMenuMeasureCaps();
            cancelDelayedDismiss();
            View view = this.mAnchorView;
            if (view != null && view.getRootView() != null) {
                this.mAnchorView.getRootView().removeOnLayoutChangeListener(this.mMenuDismissWhenRootChange);
            }
            if (this.mUseRecyclerView) {
                if (this.mLastClickedMainMenuItemPosition != -1) {
                    COUILog.d(TAG, "LastClickedMainMenuItemPosition = " + this.mLastClickedMainMenuItemPosition);
                    int i3 = this.mLastClickedMainMenuItemPosition;
                    PopupListItem popupListItem = (i3 < 0 || i3 >= this.mMainMenuItemList.size()) ? null : this.mMainMenuItemList.get(this.mLastClickedMainMenuItemPosition);
                    if (popupListItem != null) {
                        popupListItem.setGroupState(0);
                        MixedPopupListAdapter mixedPopupListAdapter = this.mMainRecyclerAdapter;
                        if (mixedPopupListAdapter != null) {
                            mixedPopupListAdapter.notifyDataSetChanged();
                        }
                    }
                }
                resetRecyclerViewSharedMaskLockedStates();
            } else if (this.mLastClickedMainMenuItemPosition != -1 && this.mMainMenuAdapter != null) {
                COUILog.d(TAG, "LastClickedMainMenuItemPosition = " + this.mLastClickedMainMenuItemPosition);
                Object item = this.mMainMenuAdapter.getItem(DefaultAdapter.dataIndexToRealPosition(this.mLastClickedMainMenuItemPosition));
                if (item instanceof PopupListItem) {
                    ((PopupListItem) item).setGroupState(0);
                }
            }
            this.mSubMenuAnchorView = null;
            setAnchorHoveredState(false, this.mAnchorView);
            this.mIsDismissing = false;
            this.mMainMenuWidth = 0;
            this.mMainMenuHeight = 0;
            super.dismiss();
            PopupWindow.OnDismissListener onDismissListener = this.mOnDismissListener;
            if (onDismissListener != null && (!this.mDismissWithWindowAnimation || this.mUsePopupExitAnimation)) {
                onDismissListener.onDismiss();
            }
            this.mDismissWithWindowAnimation = false;
        }
    }

    @Deprecated
    public ListAdapter getAdapter() {
        ListView listView;
        if (this.mUseRecyclerView || (listView = this.mMainListView) == null) {
            return null;
        }
        return listView.getAdapter();
    }

    public View getAnchorView() {
        return this.mAnchorView;
    }

    public List<PopupListItem> getItemList() {
        return this.mMainMenuItemList;
    }

    @Deprecated
    public ListView getListView() {
        return null;
    }

    public PopupMenuLocateHelper getLocateHelper() {
        return this.mLocateHelper;
    }

    @Deprecated
    public ListView getMainMenuListView() {
        return this.mMainListView;
    }

    public int getMainMenuMaxWidth() {
        if (this.mCustomMenuWidth >= 0) {
            if (COUI_DEBUG) {
                Log.i(TAG, "Use custom menu width = " + this.mCustomMenuWidth);
            }
            return this.mCustomMenuWidth;
        }
        if (this.mCustomMenuMaxWidth >= getMainMenuMinWidth()) {
            return this.mCustomMenuMaxWidth;
        }
        Log.w(TAG, "Illegal max width! Custom menu max width smaller than min width!");
        boolean z2 = this.mUseRecyclerView;
        boolean zHasSubMenu = false;
        if (!z2 ? this.mMainMenuAdapter != null : this.mMainRecyclerAdapter != null) {
            Log.w(TAG, "Get main menu max width fail! Adapter is NULL!");
            return 0;
        }
        if (z2) {
            List<PopupListItem> list = this.mMainMenuItemList;
            if (list != null && !list.isEmpty()) {
                for (PopupListItem popupListItem : this.mMainMenuItemList) {
                    if (popupListItem != null) {
                        if (popupListItem.hasSubMenu()) {
                            zHasSubMenu = true;
                        }
                        if (popupListItem.getIconId() == 0) {
                            popupListItem.getIcon();
                        }
                    }
                }
            }
        } else {
            DefaultAdapter defaultAdapter = this.mMainMenuAdapter;
            if (defaultAdapter != null) {
                zHasSubMenu = defaultAdapter.hasSubMenu();
                this.mMainMenuAdapter.hasIcon();
            }
        }
        return zHasSubMenu ? this.mContext.getResources().getDimensionPixelOffset(R.dimen.coui_popup_list_window_max_width) : this.mContext.getResources().getDimensionPixelOffset(R.dimen.coui_popup_list_window_max_width);
    }

    public int getMainMenuMinWidth() {
        int i3 = this.mCustomMenuWidth;
        if (i3 >= 0) {
            return i3;
        }
        boolean z2 = this.mUseRecyclerView;
        boolean zHasSubMenu = false;
        if (!z2 ? this.mMainMenuAdapter != null : this.mMainRecyclerAdapter != null) {
            Log.w(TAG, "Get main menu min width fail! Adapter is NULL!");
            return 0;
        }
        if (z2) {
            List<PopupListItem> list = this.mMainMenuItemList;
            if (list != null && !list.isEmpty()) {
                for (PopupListItem popupListItem : this.mMainMenuItemList) {
                    if (popupListItem != null) {
                        if (popupListItem.hasSubMenu()) {
                            zHasSubMenu = true;
                        }
                        if (popupListItem.getIconId() == 0) {
                            popupListItem.getIcon();
                        }
                    }
                }
            }
        } else {
            DefaultAdapter defaultAdapter = this.mMainMenuAdapter;
            if (defaultAdapter != null) {
                zHasSubMenu = defaultAdapter.hasSubMenu();
                this.mMainMenuAdapter.hasIcon();
            }
        }
        return zHasSubMenu ? this.mContext.getResources().getDimensionPixelOffset(R.dimen.coui_popup_list_window_width_with_icon) : this.mContext.getResources().getDimensionPixelOffset(R.dimen.coui_popup_list_window_min_width);
    }

    public RecyclerView getMainMenuRecyclerView() {
        return this.mMainRecyclerView;
    }

    public RecyclerView.Adapter<?> getRecyclerAdapter() {
        if (!this.mUseRecyclerView) {
            return null;
        }
        RecyclerView recyclerView = this.mMainRecyclerView;
        return recyclerView != null ? recyclerView.getAdapter() : this.mMainRecyclerAdapter;
    }

    @Deprecated
    public ListView getSubMenuListView() {
        return this.mSubListView;
    }

    public RecyclerView getSubMenuRecyclerView() {
        return this.mSubRecyclerView;
    }

    public boolean hasAvailableSubMenu(PopupListItem popupListItem) {
        return popupListItem != null && checkListNotNull(popupListItem.getSubMenuItemList()) && checkListElementsNotNull(popupListItem.getSubMenuItemList());
    }

    @Override // com.coui.appcompat.poplist.COUIPopupWindow
    public void initElevationInPopupwindow() {
        setBackgroundDrawable(null);
    }

    @Override // com.coui.appcompat.poplist.COUIPopupWindow
    public void initOutlineRoundRectBackground() {
    }

    @Override // android.widget.PopupWindow
    public boolean isShowing() {
        return super.isShowing() && !this.mIsDismissing;
    }

    public void measurePopupWindow() {
        if (!this.mUseRecyclerView) {
            measurePopupWindow(this.mMainMenuAdapter);
            return;
        }
        MixedPopupListAdapter mixedPopupListAdapter = this.mMainRecyclerAdapter;
        if (mixedPopupListAdapter != null) {
            measurePopupWindowForRecyclerView(mixedPopupListAdapter, this.mMainMenuItemList, true);
        } else {
            Log.w(TAG, "measurePopupWindow: main RecyclerView adapter is null");
        }
    }

    public void measurePopupWindowForRecyclerView(MixedPopupListAdapter mixedPopupListAdapter, List<PopupListItem> list, boolean z2) {
        int i3;
        ListView listView;
        if (mixedPopupListAdapter == null || mixedPopupListAdapter.getItemCount() == 0) {
            Log.w(TAG, "measurePopupWindowForRecyclerView: adapter is null or empty");
            return;
        }
        if (list == null || list.isEmpty()) {
            Log.w(TAG, "measurePopupWindowForRecyclerView: itemList is null or empty");
            return;
        }
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.coui_popup_list_padding_vertical) * 2;
        PopupMenuLocateHelper popupMenuLocateHelper = this.mLocateHelper;
        int maxMainMenuHeight = z2 ? popupMenuLocateHelper.getMaxMainMenuHeight() : popupMenuLocateHelper.getMaxSubMenuHeight();
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMainMenuMaxWidth(), Integer.MIN_VALUE);
        int i4 = 0;
        int iMakeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(0, 0);
        ListView listView2 = this.mListViewUsedToMeasure;
        RecyclerView recyclerView = z2 ? this.mMainRecyclerView : this.mSubRecyclerView;
        ensureViewPoolCapacity(recyclerView, maxMainMenuHeight, dimensionPixelSize, this.mContext.getResources().getDimensionPixelSize(R.dimen.coui_popup_list_window_item_min_height));
        int itemCount = mixedPopupListAdapter.getItemCount();
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        boolean z3 = false;
        boolean z4 = false;
        boolean z5 = dimensionPixelSize <= maxMainMenuHeight;
        int i8 = dimensionPixelSize;
        while (i7 < itemCount) {
            int i9 = dimensionPixelSize;
            int itemViewType = mixedPopupListAdapter.getItemViewType(i7);
            int i10 = itemCount;
            if (itemViewType == 4 || itemViewType == 3) {
                listView = listView2;
                if (z3 && !z4) {
                    if (z5 && i9 + i5 > maxMainMenuHeight) {
                        z5 = false;
                    }
                    i8 += i5;
                    i9 += i5;
                    z4 = true;
                }
                int dividerHeight = mixedPopupListAdapter.getDividerHeight(itemViewType);
                if (z5 && i9 + dividerHeight > maxMainMenuHeight) {
                    z5 = false;
                }
                if (z5) {
                    i9 += dividerHeight;
                }
                i8 += dividerHeight;
            } else if (mixedPopupListAdapter.getItem(i7) == null) {
                listView = listView2;
            } else {
                RecyclerView.ViewHolder viewHolderCreateViewHolder = mixedPopupListAdapter.createViewHolder(listView2, itemViewType);
                mixedPopupListAdapter.onBindViewHolder(viewHolderCreateViewHolder, i7);
                listView = listView2;
                View view = viewHolderCreateViewHolder.itemView;
                if (view != null) {
                    view.measure(iMakeMeasureSpec, iMakeMeasureSpec2);
                    recycleMeasuredHolder(viewHolderCreateViewHolder, recyclerView, itemViewType);
                    int measuredWidth = view.getMeasuredWidth();
                    int measuredHeight = view.getMeasuredHeight();
                    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                    iMakeMeasureSpec = iMakeMeasureSpec;
                    if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                        measuredWidth += marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
                        measuredHeight += marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
                    }
                    int i11 = measuredHeight;
                    if (itemViewType == 0) {
                        i4 += measuredWidth;
                        if (i11 > i5) {
                            i5 = i11;
                        }
                        dimensionPixelSize = i9;
                        z3 = true;
                    } else {
                        if (measuredWidth > i6) {
                            i6 = measuredWidth;
                        }
                        if (!z3 || z4) {
                            dimensionPixelSize = i9;
                        } else {
                            if (z5 && i9 + i5 > maxMainMenuHeight) {
                                z5 = false;
                            }
                            dimensionPixelSize = i9 + i5;
                            i8 += i5;
                            z4 = true;
                        }
                        if (z5 && dimensionPixelSize + i11 > maxMainMenuHeight) {
                            z5 = false;
                        }
                        if (z5) {
                            dimensionPixelSize += i11;
                        }
                        i8 += i11;
                    }
                }
                i7++;
                itemCount = i10;
                listView2 = listView;
                iMakeMeasureSpec = iMakeMeasureSpec;
            }
            dimensionPixelSize = i9;
            i7++;
            itemCount = i10;
            listView2 = listView;
            iMakeMeasureSpec = iMakeMeasureSpec;
        }
        int i12 = dimensionPixelSize;
        if (!z3 || z4) {
            i3 = i12;
        } else {
            i3 = i12 + i5;
            i8 += i5;
        }
        if (i3 != 0) {
            maxMainMenuHeight = i3;
        }
        int iMax = Math.max(i4, i6);
        int mainMenuMinWidth = getMainMenuMinWidth();
        int mainMenuMaxWidth = getMainMenuMaxWidth();
        int iMax2 = Math.max(iMax, mainMenuMinWidth);
        if (mainMenuMaxWidth > 0 && mainMenuMaxWidth >= mainMenuMinWidth) {
            iMax2 = Math.min(iMax2, mainMenuMaxWidth);
        }
        if (z2) {
            this.mMainMenuWidth = iMax2;
            this.mMainMenuHeight = maxMainMenuHeight;
            return;
        }
        this.mSubMenuWidth = this.mMainMenuWidth;
        this.mSubMenuHeight = maxMainMenuHeight;
        RecyclerView recyclerView2 = this.mSubRecyclerView;
        if (recyclerView2 != null) {
            if (i8 <= maxMainMenuHeight) {
                recyclerView2.setOverScrollMode(2);
            } else {
                recyclerView2.setOverScrollMode(0);
            }
        }
    }

    public COUIPopupMenuRootView onCreatePopupMenuRootView(Context context) {
        return new COUIPopupMenuRootView(context);
    }

    public void prepareShowMainMenu(View view, int i3, int i4, boolean z2) {
        if (this.mUseRecyclerView) {
            configMainRecyclerView();
        } else {
            configMainListView();
        }
        this.mLocateHelper.prepareWindowAndAnchor(view, i3, i4, this.mRootView);
        this.mContentView.setDomain(this.mLocateHelper.getDomain());
        this.mContentView.addMainMenuView(this.mMainMenuWrapper);
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

    public void recycleMeasuredHolder(RecyclerView.ViewHolder viewHolder, RecyclerView recyclerView, int i3) {
        if (i3 == 4 || i3 == 3 || recyclerView == null || viewHolder == null || viewHolder.itemView == null) {
            return;
        }
        recyclerView.getRecycledViewPool().putRecycledView(viewHolder);
    }

    public void refresh() {
        TypedArray typedArrayObtainStyledAttributes = this.mContext.getTheme().obtainStyledAttributes(new int[]{R.attr.couiPopupWindowBackground});
        Drawable drawable = typedArrayObtainStyledAttributes.getDrawable(0);
        typedArrayObtainStyledAttributes.recycle();
        if (drawable == null) {
            drawable = ResourcesCompat.getDrawable(this.mContext.getResources(), R.drawable.coui_popup_window_background, this.mContext.getTheme());
        }
        if (drawable != null) {
            this.mMainMenuWrapper.setBackground(drawable.getConstantState().newDrawable());
            this.mSubMenuWrapper.setBackground(drawable.getConstantState().newDrawable());
        }
    }

    public void resetOffset() {
        setGlobalOffset(0, 0);
    }

    public void reuseMainMenu(boolean z2) {
        this.mContentView.setMainMenuSize(this.mMainMenuWidth, this.mMainMenuHeight);
        this.mLocateHelper.prepareShowMainMenu(this.mMainMenuWidth, this.mMainMenuHeight, z2, this.mGlobalOffsetX, this.mGlobalOffsetY);
        if (this.mUseRecyclerView) {
            return;
        }
        syncMainMenuSize();
    }

    public void reuseMenuWhenOffsetChanged(boolean z2) {
        this.mReuseMenuWhenOffsetChanged = z2;
    }

    @Deprecated
    public void setAdapter(BaseAdapter baseAdapter, boolean z2) {
    }

    @Deprecated
    public void setAdapterFontSize(boolean z2) {
        setIsAdaptiveFontSize(z2);
    }

    @Deprecated
    public void setAlwaysBelowAnchor(boolean z2) {
    }

    public void setAnchorView(View view) {
        this.mAnchorView = view;
    }

    @Deprecated
    public void setContentHeight(int i3) {
    }

    @Deprecated
    public void setContentWidth(int i3) {
    }

    public void setDismissWhenLayoutChange(boolean z2) {
        this.mDismissWhenLayoutChange = z2;
    }

    public void setDismissWhenWindowSizeChange(boolean z2) {
        this.mDismissWhenWindowSizeChange = z2;
    }

    public void setDismissWithPopupWindowAnimation(boolean z2) {
        if (this.mUsePopupExitAnimation == z2) {
            return;
        }
        if (z2) {
            this.mDefaultAnimationStyle = getAnimationStyle();
            setAnimationStyle(R.style.Animation_COUI_PopupListWindow_Dismiss);
        } else {
            int i3 = this.mDefaultAnimationStyle;
            if (i3 != -1) {
                setAnimationStyle(i3);
                this.mDefaultAnimationStyle = -1;
            }
        }
        this.mUsePopupExitAnimation = z2;
    }

    public void setDismissWithWindowAnimation(boolean z2) {
        this.mDismissWithWindowAnimation = z2;
    }

    @Deprecated
    public void setEnableAddExtraWidth(boolean z2) {
    }

    public void setGlobalOffset(int i3, int i4) {
        this.mGlobalOffsetX = i3;
        this.mGlobalOffsetY = i4;
    }

    public void setIsAdaptiveFontSize(boolean z2) {
        this.mIsAdaptiveFontSize = z2;
        DefaultAdapter defaultAdapter = this.mMainMenuAdapter;
        if (defaultAdapter != null) {
            defaultAdapter.setAdapterFontSize(z2);
        }
        DefaultAdapter defaultAdapter2 = this.mSubMenuAdapter;
        if (defaultAdapter2 != null) {
            defaultAdapter2.setAdapterFontSize(this.mIsAdaptiveFontSize);
        }
        MixedPopupListAdapter mixedPopupListAdapter = this.mMainRecyclerAdapter;
        if (mixedPopupListAdapter != null) {
            mixedPopupListAdapter.setAdapterFontSize(this.mIsAdaptiveFontSize);
        }
        MixedPopupListAdapter mixedPopupListAdapter2 = this.mSubRecyclerAdapter;
        if (mixedPopupListAdapter2 != null) {
            mixedPopupListAdapter2.setAdapterFontSize(this.mIsAdaptiveFontSize);
        }
    }

    public void setIsFixedFontSize(boolean z2) {
        this.mIsFixedFontSize = z2;
        DefaultAdapter defaultAdapter = this.mMainMenuAdapter;
        if (defaultAdapter != null) {
            defaultAdapter.setIsFixedFontSize(z2);
        }
        DefaultAdapter defaultAdapter2 = this.mSubMenuAdapter;
        if (defaultAdapter2 != null) {
            defaultAdapter2.setIsFixedFontSize(this.mIsFixedFontSize);
        }
        MixedPopupListAdapter mixedPopupListAdapter = this.mMainRecyclerAdapter;
        if (mixedPopupListAdapter != null) {
            mixedPopupListAdapter.setIsFixedFontSize(this.mIsFixedFontSize);
        }
        MixedPopupListAdapter mixedPopupListAdapter2 = this.mSubRecyclerAdapter;
        if (mixedPopupListAdapter2 != null) {
            mixedPopupListAdapter2.setIsFixedFontSize(this.mIsFixedFontSize);
        }
    }

    public void setItemList(List<PopupListItem> list) {
        setItemList(list, false);
    }

    @Deprecated
    public void setItemTextColor(ColorStateList colorStateList) {
    }

    @Deprecated
    public void setMaxShowItemCount(int i3) {
    }

    @Deprecated
    public void setMaxShowItemCountSubWindow(int i3) {
    }

    public void setMenuMaxWidth(int i3) {
        this.mCustomMenuMaxWidth = i3;
    }

    public void setMenuWidth(int i3) {
        this.mCustomMenuWidth = i3;
    }

    public void setNeedOffsetWhenSetWindowType(boolean z2) {
        this.mNeedOffsetWhenSetWindowType = z2;
    }

    public void setNonApplicationType(boolean z2, boolean z3) {
        this.mLocateHelper.useWindowBarrier(z2);
        this.mLocateHelper.setCenterAlign(z3);
    }

    @Deprecated
    public void setOffset(int i3, int i4, int i5, int i6) {
        setGlobalOffset(-i3, -i4);
    }

    @Override // android.widget.PopupWindow
    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        MixedPopupListAdapter mixedPopupListAdapter;
        if (onItemClickListener == null) {
            COUILog.w(TAG, "set main menu item click listener = null. caller = " + Log.getStackTraceString(new Throwable()));
        }
        this.mOnMainMenuItemClickListener = onItemClickListener;
        if (!this.mUseRecyclerView || (mixedPopupListAdapter = this.mMainRecyclerAdapter) == null) {
            return;
        }
        mixedPopupListAdapter.setOnItemClickListener(new h(this));
        attachRegionBasedLongPressDetector();
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        MixedPopupListAdapter mixedPopupListAdapter;
        if (onItemLongClickListener == null) {
            COUILog.w(TAG, "set main menu item long click listener = null. caller = " + Log.getStackTraceString(new Throwable()));
        }
        this.mOnMainMenuItemLongClickListener = onItemLongClickListener;
        if (!this.mUseRecyclerView || (mixedPopupListAdapter = this.mMainRecyclerAdapter) == null) {
            return;
        }
        mixedPopupListAdapter.setOnItemLongClickListener(onItemLongClickListener);
        attachRegionBasedLongPressDetector();
        RecyclerView recyclerView = this.mMainRecyclerView;
        if (recyclerView != null) {
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            MixedPopupListAdapter mixedPopupListAdapter2 = this.mMainRecyclerAdapter;
            if (adapter == mixedPopupListAdapter2) {
                mixedPopupListAdapter2.notifyDataSetChanged();
            }
        }
    }

    public void setOnMainMenuAnimationListener(OnMainMenuAnimationListener onMainMenuAnimationListener) {
        this.mOnMainMenuAnimationListener = onMainMenuAnimationListener;
    }

    public void setOnSubMenuItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        if (onItemLongClickListener == null) {
            COUILog.w(TAG, "set sub menu item long click listener = null. caller = " + Log.getStackTraceString(new Throwable()));
        }
        this.mOnSubMenuItemLongClickListener = onItemLongClickListener;
        if (!this.mUseRecyclerView || this.mSubRecyclerAdapter == null) {
            return;
        }
        attachRegionBasedLongPressDetector();
    }

    public void setOnTouchOutsideDismissListener(OnTouchOutsideDismissListener onTouchOutsideDismissListener) {
        this.mOnTouchOutsideDismissListener = onTouchOutsideDismissListener;
    }

    public void setPathAnchorAutoModeEnabled(boolean z2) {
        this.mLocateHelper.getDomain().setPathAnchorAutoModeEnabled(z2);
    }

    public void setPathAnchorAutoPadding(float f3) {
        this.mLocateHelper.getDomain().setPathAnchorAutoPadding(f3);
    }

    public void setPopupWindowLimitedRootView(View view) {
        this.mRootView = view;
    }

    @Deprecated
    public void setSelectItemColor(int i3) {
    }

    @Deprecated
    public void setShowInBottomSheetDialog(boolean z2) {
    }

    public void setSubMenuClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        MixedPopupListAdapter mixedPopupListAdapter;
        if (onItemClickListener == null) {
            COUILog.w(TAG, "set sub menu item click listener = null. caller = " + Log.getStackTraceString(new Throwable()));
        }
        this.mOnSubMenuItemClickListener = onItemClickListener;
        if (!this.mUseRecyclerView || (mixedPopupListAdapter = this.mSubRecyclerAdapter) == null) {
            return;
        }
        mixedPopupListAdapter.setOnItemClickListener(new i(this));
    }

    @Deprecated
    public void setSubMenuOffset(int i3, int i4) {
    }

    public void setUseBackgroundBlur(boolean z2) {
        setUseBackgroundBlur(z2, UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN);
        if (z2) {
            return;
        }
        refresh();
    }

    public void show() {
        View view = this.mAnchorView;
        if (view != null) {
            show(view);
        }
    }

    @Override // android.widget.PopupWindow
    public void showAsDropDown(View view, int i3, int i4, int i5) {
    }

    @Deprecated
    public void showAtAbove(View view) {
        show(view, true);
    }

    @Deprecated
    public void showAtAboveOrBelow(View view) {
        show(view, true);
    }

    @Deprecated
    public void showEndOfAnchorViewStart(View view) {
    }

    @Deprecated
    public void superDismiss() {
        super.dismiss();
    }

    public void syncMainMenuSize() {
        int mainMenuWidth = this.mLocateHelper.getMainMenuWidth();
        int mainMenuHeight = this.mLocateHelper.getMainMenuHeight();
        if (mainMenuWidth == this.mMainMenuWidth && mainMenuHeight == this.mMainMenuHeight) {
            return;
        }
        this.mMainMenuWidth = mainMenuWidth;
        this.mMainMenuHeight = mainMenuHeight;
        this.mContentView.setMainMenuSize(mainMenuWidth, mainMenuHeight);
    }

    public void syncSubMenuSize() {
        int subMenuWidth = this.mLocateHelper.getSubMenuWidth();
        int subMenuHeight = this.mLocateHelper.getSubMenuHeight();
        if (subMenuWidth == this.mSubMenuWidth && subMenuHeight == this.mSubMenuHeight) {
            return;
        }
        this.mSubMenuWidth = subMenuWidth;
        this.mSubMenuHeight = subMenuHeight;
        this.mContentView.setSubMenuSize(subMenuWidth, subMenuHeight);
    }

    @Deprecated
    public void setAdapter(BaseAdapter baseAdapter) {
        setAdapter(baseAdapter, true);
    }

    public void setItemList(List<PopupListItem> list, boolean z2) {
        if (!checkListNotNull(list) || !checkListElementsNotNull(list)) {
            Log.e(TAG, "Error! Item list must not be empty or null!");
            return;
        }
        this.mMainMenuItemList = list;
        if (this.mUseRecyclerView) {
            if (this.mMainRecyclerAdapter == null) {
                this.mMainRecyclerAdapter = new MixedPopupListAdapter(this.mContext);
            }
            setItemListInternal(this.mMainMenuItemList, this.mMainRecyclerAdapter, z2);
        } else {
            if (this.mMainMenuAdapter == null) {
                this.mMainMenuAdapter = new DefaultAdapter(this.mContext, null);
            }
            setItemListInternal(this.mMainMenuItemList, this.mMainMenuAdapter, z2);
        }
    }

    public void setUseBackgroundBlur(boolean z2, AnimLevel animLevel) {
        this.mSubMenuWrapper.initUseBackgroundBlur(z2, animLevel);
        this.mMainMenuWrapper.initUseBackgroundBlur(z2, animLevel);
    }

    public void show(View view) {
        show(view, false);
    }

    private void refreshAdapter(List<PopupListItem> list, MixedPopupListAdapter mixedPopupListAdapter) {
        mixedPopupListAdapter.setAdapterFontSize(this.mIsAdaptiveFontSize);
        mixedPopupListAdapter.setIsFixedFontSize(this.mIsFixedFontSize);
        mixedPopupListAdapter.setItemList(list);
        if (mixedPopupListAdapter == this.mMainRecyclerAdapter) {
            mixedPopupListAdapter.setOnItemClickListener(new h(this));
        } else {
            mixedPopupListAdapter.setOnItemClickListener(new i(this));
        }
        configureGridLayoutManager(mixedPopupListAdapter, mixedPopupListAdapter == this.mMainRecyclerAdapter);
    }

    public void show(View view, boolean z2) {
        show(view, z2, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    @Deprecated
    public void setSubMenuClickListener(COUISubMenuClickListener cOUISubMenuClickListener) {
        this.mOnSubMenuItemClickListener = cOUISubMenuClickListener;
    }

    public void show(View view, int i3, int i4) {
        show(view, false, i3, i4);
    }

    public void measurePopupWindow(DefaultAdapter defaultAdapter) {
        View view;
        int i3;
        boolean z2 = defaultAdapter == this.mMainMenuAdapter;
        PopupMenuLocateHelper popupMenuLocateHelper = this.mLocateHelper;
        int maxMainMenuHeight = z2 ? popupMenuLocateHelper.getMaxMainMenuHeight() : popupMenuLocateHelper.getMaxSubMenuHeight();
        ArrayList arrayList = new ArrayList();
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMainMenuMaxWidth(), Integer.MIN_VALUE);
        int iMakeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(0, 0);
        int count = defaultAdapter.getCount();
        View view2 = null;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        int dividerHeight = 0;
        View view3 = null;
        boolean z3 = true;
        while (i4 < count) {
            if (DefaultAdapter.isDataIndex(i4)) {
                if (defaultAdapter.getItemViewType(i4) == 3) {
                    view = defaultAdapter.getView(i4, view2, this.mListViewUsedToMeasure);
                } else {
                    view3 = defaultAdapter.getView(i4, view3, this.mListViewUsedToMeasure);
                    view = view3;
                }
                if (view != null) {
                    if ((view.getLayoutParams() instanceof AbsListView.LayoutParams) && (i3 = ((AbsListView.LayoutParams) view.getLayoutParams()).height) != -2) {
                        iMakeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(i3, 1073741824);
                    }
                    view.measure(iMakeMeasureSpec, iMakeMeasureSpec2);
                    int measuredWidth = view.getMeasuredWidth();
                    int measuredHeight = view.getMeasuredHeight();
                    if (measuredWidth > i6) {
                        i6 = measuredWidth;
                    }
                    if (z3 && i5 + measuredHeight > maxMainMenuHeight) {
                        i5 -= dividerHeight;
                        z3 = false;
                    }
                    if (z3) {
                        i5 += measuredHeight;
                    }
                    i7 += measuredHeight;
                    if (i4 != 0 && !arrayList.isEmpty()) {
                        arrayList.add(Integer.valueOf(measuredHeight + ((Integer) arrayList.get(i4 - 1)).intValue()));
                    } else {
                        arrayList.add(Integer.valueOf(measuredHeight));
                    }
                }
            } else {
                if (defaultAdapter.isGroupIndex(i4)) {
                    dividerHeight = defaultAdapter.getDividerHeight(2);
                } else {
                    dividerHeight = defaultAdapter.getDividerHeight(1);
                }
                if (z3) {
                    i5 += dividerHeight;
                }
                i7 += dividerHeight;
                if (i4 != 0 && !arrayList.isEmpty()) {
                    arrayList.add(Integer.valueOf(dividerHeight + ((Integer) arrayList.get(i4 - 1)).intValue()));
                } else {
                    arrayList.add(Integer.valueOf(dividerHeight));
                }
            }
            i4++;
            view2 = null;
        }
        if (i5 != 0) {
            maxMainMenuHeight = i5;
        }
        if (z2) {
            this.mMainMenuWidth = Math.max(i6, getMainMenuMinWidth());
            this.mMainMenuHeight = maxMainMenuHeight;
            ListView listView = this.mMainListView;
            if (listView instanceof COUITouchListView) {
                ((COUITouchListView) listView).setItemHeightMap(arrayList, i7);
                return;
            }
            return;
        }
        this.mSubMenuWidth = this.mMainMenuWidth;
        this.mSubMenuHeight = maxMainMenuHeight;
        ListView listView2 = this.mSubListView;
        if (listView2 instanceof COUITouchListView) {
            ((COUITouchListView) listView2).setItemHeightMap(arrayList, i7);
        }
    }

    public void show(View view, boolean z2, int i3, int i4) {
        int i5;
        WindowInsets rootWindowInsets;
        Context context = this.mContext;
        if (context == null) {
            Log.e(TAG, " The context of COUIPopupListWindow is null ");
            return;
        }
        if ((context instanceof Activity) && ((Activity) context).isFinishing()) {
            Log.e(TAG, " The context of COUIPopupListWindow is Finish ");
            return;
        }
        if (view != null && view.getContext() != null && view.getWindowToken() != null) {
            boolean z3 = this.mUseRecyclerView;
            if (z3) {
                if (this.mMainRecyclerAdapter == null) {
                    Log.e(TAG, "The MainMenuRecyclerAdapter is null");
                    return;
                }
            } else if (this.mMainMenuAdapter == null) {
                Log.e(TAG, "The MainMenuAdapter is null");
                return;
            }
            if (z3) {
                resetRecyclerViewSharedMaskLockedStates();
            }
            boolean z4 = this.mIsDismissing && this.mAnchorView == view;
            this.mAnchorView = view;
            if (this.mNeedOffsetWhenSetWindowType && (rootWindowInsets = view.getRootWindowInsets()) != null) {
                rootWindowInsets.getSystemWindowInsetLeft();
                getContentView().setTranslationX(-rootWindowInsets.getInsets(WindowInsets.Type.navigationBars()).left);
                COUILog.w(TAG, "mNeedOffsetWhenSetWindowType is true , offset the root view.");
            }
            int i6 = this.mMainMenuWidth;
            if (i6 != 0 && (i5 = this.mMainMenuHeight) != 0) {
                measurePopupWindow();
                z4 &= i6 == this.mMainMenuWidth && i5 == this.mMainMenuHeight;
            }
            if (z4 & (this.mReuseMenuWhenOffsetChanged || (this.mShowOffsetX == i3 && this.mShowOffsetY == i4)) & (!this.mDismissWithWindowAnimation)) {
                if (this.mUseRecyclerView) {
                    MixedPopupListAdapter mixedPopupListAdapter = this.mMainRecyclerAdapter;
                    if (mixedPopupListAdapter != null) {
                        mixedPopupListAdapter.notifyDataSetChanged();
                    }
                    MixedPopupListAdapter mixedPopupListAdapter2 = this.mSubRecyclerAdapter;
                    if (mixedPopupListAdapter2 != null) {
                        mixedPopupListAdapter2.notifyDataSetChanged();
                    }
                } else {
                    DefaultAdapter defaultAdapter = this.mMainMenuAdapter;
                    if (defaultAdapter != null) {
                        defaultAdapter.notifyDataSetChanged();
                    }
                    DefaultAdapter defaultAdapter2 = this.mSubMenuAdapter;
                    if (defaultAdapter2 != null) {
                        defaultAdapter2.notifyDataSetChanged();
                    }
                }
                reuseMainMenu(z2);
                setWidth(this.mLocateHelper.mApplicationWindow.width());
                setHeight(this.mLocateHelper.mApplicationWindow.height());
                this.mContentView.showMainMenu();
            } else {
                if (super.isShowing()) {
                    forceDismiss();
                }
                this.mShowOffsetX = i3;
                this.mShowOffsetY = i4;
                prepareShowMainMenu(view, i3, i4, z2);
                setWidth(this.mLocateHelper.mApplicationWindow.width());
                setHeight(this.mLocateHelper.mApplicationWindow.height());
                super.showAtLocation(view.getRootView(), 0, 0, 0);
            }
            view.getRootView().addOnLayoutChangeListener(this.mMenuDismissWhenRootChange);
            setAnchorHoveredState(true, view);
            return;
        }
        Log.e(TAG, " COUIPopupListWindow's anchor state is wrong ");
    }

    private void setItemListInternal(List<PopupListItem> list, MixedPopupListAdapter mixedPopupListAdapter, boolean z2) {
        HashSet hashSet;
        if (list.size() >= 3) {
            if (z2) {
                Collections.sort(list, new j());
            }
            hashSet = new HashSet();
            int groupId = list.get(0).getGroupId();
            for (int i3 = 1; i3 < list.size(); i3++) {
                int groupId2 = list.get(i3).getGroupId();
                if (groupId2 != groupId) {
                    hashSet.add(Integer.valueOf(i3));
                    groupId = groupId2;
                }
            }
        } else {
            hashSet = null;
        }
        if (hashSet != null) {
            mixedPopupListAdapter.setGroupSets(hashSet);
        }
        refreshAdapter(list, mixedPopupListAdapter);
    }

    @Deprecated
    public void measurePopupWindow(boolean z2) {
        measurePopupWindow();
    }
}