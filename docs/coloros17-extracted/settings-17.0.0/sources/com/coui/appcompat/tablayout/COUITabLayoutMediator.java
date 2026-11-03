package com.coui.appcompat.tablayout;

import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.PathInterpolator;
import androidx.compose.material3.ProgressIndicatorKt;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.coui.appcompat.animation.COUIMoveEaseInterpolator;
import com.coui.appcompat.viewpager.COUIViewPager2;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes7.dex */
public final class COUITabLayoutMediator {
    private static final int ALL_CUSTOM_TAB_VIEW = 2;
    private static final int INVALID_VALUE = -1;
    private static final int NO_CUSTOM_TAB_VIEW = 0;
    private static final int PART_CUSTOM_TAB_VIEW = 1;
    private static final String SELECT_TAB_NAME = "TabLayout.selectTab(TabLayout.Tab, boolean)";
    private static final String SET_SCROLL_POSITION_NAME = "TabLayout.setScrollPosition(int, float, boolean, boolean)";
    private static Method sSelectTab;
    private static Method sSetScrollPosition;
    private RecyclerView.Adapter mAdapter;
    private boolean mAttached;
    private final boolean mAutoRefresh;
    private int mCustomTabViewType;
    private int mLayoutResAll;
    private SparseIntArray mLayoutResIdMap;
    private final OnConfigureTabCallback mOnConfigureTabCallback;
    private TabLayoutOnPageChangeCallback mOnPageChangeCallback;
    private COUITabLayout.OnTabSelectedListener mOnTabSelectedListener;
    private RecyclerView.AdapterDataObserver mPagerAdapterObserver;
    private Map<Integer, String> mTabContentDescMap;
    private final COUITabLayout mTabLayout;
    private final COUIViewPager2 mViewPager;

    public interface OnConfigureTabCallback {
        void onConfigureTab(COUITab cOUITab, int i3);
    }

    public class PagerAdapterObserver extends RecyclerView.AdapterDataObserver {
        public PagerAdapterObserver() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onChanged() {
            COUITabLayoutMediator.this.populateTabsFromPagerAdapter();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeChanged(int i3, int i4) {
            COUITabLayoutMediator.this.populateTabsFromPagerAdapter();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeInserted(int i3, int i4) {
            COUITabLayoutMediator.this.populateTabsFromPagerAdapter();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeMoved(int i3, int i4, int i5) {
            COUITabLayoutMediator.this.populateTabsFromPagerAdapter();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeRemoved(int i3, int i4) {
            COUITabLayoutMediator.this.populateTabsFromPagerAdapter();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeChanged(int i3, int i4, Object obj) {
            COUITabLayoutMediator.this.populateTabsFromPagerAdapter();
        }
    }

    public static class TabLayoutOnPageChangeCallback extends ViewPager2.OnPageChangeCallback {
        private int mPreviousScrollState;
        private int mScrollState;
        private final WeakReference<COUITabLayout> mTabLayoutRef;
        private final WeakReference<COUIViewPager2> mViewPager2Ref;

        public TabLayoutOnPageChangeCallback(COUITabLayout cOUITabLayout, COUIViewPager2 cOUIViewPager2) {
            this.mTabLayoutRef = new WeakReference<>(cOUITabLayout);
            this.mViewPager2Ref = new WeakReference<>(cOUIViewPager2);
            reset();
        }

        @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
        public void onPageScrollStateChanged(int i3) {
            this.mPreviousScrollState = this.mScrollState;
            this.mScrollState = i3;
        }

        @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
        public void onPageScrolled(int i3, float f3, int i4) {
            COUIViewPager2 cOUIViewPager2 = this.mViewPager2Ref.get();
            COUITabLayout cOUITabLayout = this.mTabLayoutRef.get();
            if (cOUITabLayout == null || cOUIViewPager2 == null || cOUIViewPager2.isFakeDragging()) {
                return;
            }
            int i5 = this.mScrollState;
            boolean z2 = true;
            boolean z3 = i5 != 2 || this.mPreviousScrollState == 1;
            if (i5 == 2 && this.mPreviousScrollState == 0) {
                z2 = false;
            }
            if (i5 == 0 && this.mPreviousScrollState == 0 && f3 != 0.0f) {
                ((RecyclerView) cOUIViewPager2.getChildAt(0)).scrollBy(i4, 0);
                cOUITabLayout.selectTab(cOUITabLayout.getTabAt(i3));
            } else {
                COUITabLayoutMediator.setScrollPosition(cOUITabLayout, i3, f3, z3, z2);
            }
            if (f3 != 0.0f || i3 == cOUITabLayout.getSelectedTabPosition()) {
                return;
            }
            cOUITabLayout.selectTab(cOUITabLayout.getTabAt(i3));
        }

        @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
        public void onPageSelected(int i3) {
            COUITabLayout cOUITabLayout = this.mTabLayoutRef.get();
            if (cOUITabLayout == null || cOUITabLayout.getSelectedTabPosition() == i3 || i3 >= cOUITabLayout.getTabCount()) {
                return;
            }
            int i4 = this.mScrollState;
            COUITabLayoutMediator.selectTab(cOUITabLayout, cOUITabLayout.getTabAt(i3), i4 == 0 || (i4 == 2 && this.mPreviousScrollState == 0));
        }

        public void reset() {
            this.mScrollState = 0;
            this.mPreviousScrollState = 0;
        }
    }

    public static class ViewPagerOnTabSelectedListener implements COUITabLayout.OnTabSelectedListener {
        private int[] mScrollDistanceAndDuration = new int[2];
        private PathInterpolator mScrollPathInterpolator = new COUIMoveEaseInterpolator();
        private final COUIViewPager2 mViewPager;

        public ViewPagerOnTabSelectedListener(COUIViewPager2 cOUIViewPager2) {
            this.mViewPager = cOUIViewPager2;
        }

        private void getScrollDistanceAndDuration(LinearLayoutManager linearLayoutManager, RecyclerView recyclerView, int i3) {
            View viewFindViewByPosition;
            int[] iArr = this.mScrollDistanceAndDuration;
            iArr[0] = 0;
            iArr[1] = 0;
            int iFindFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
            if (iFindFirstVisibleItemPosition == -1 || (viewFindViewByPosition = linearLayoutManager.findViewByPosition(iFindFirstVisibleItemPosition)) == null) {
                return;
            }
            int leftDecorationWidth = linearLayoutManager.getLeftDecorationWidth(viewFindViewByPosition);
            int rightDecorationWidth = linearLayoutManager.getRightDecorationWidth(viewFindViewByPosition);
            ViewGroup.LayoutParams layoutParams = viewFindViewByPosition.getLayoutParams();
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                leftDecorationWidth += marginLayoutParams.leftMargin;
                rightDecorationWidth += marginLayoutParams.rightMargin;
            }
            int width = viewFindViewByPosition.getWidth() + leftDecorationWidth + rightDecorationWidth;
            int left = (viewFindViewByPosition.getLeft() - leftDecorationWidth) - recyclerView.getPaddingLeft();
            if (linearLayoutManager.getLayoutDirection() == 1) {
                width = -width;
            }
            int i4 = ((i3 - iFindFirstVisibleItemPosition) * width) + left;
            int[] iArr2 = this.mScrollDistanceAndDuration;
            iArr2[0] = i4;
            iArr2[1] = getScrollDuration(Math.abs(i4), Math.abs(width));
        }

        private int getScrollDuration(int i3, int i4) {
            float f3 = i4 * 3;
            if (i3 <= i4) {
                return 350;
            }
            float f4 = i3;
            return f4 > f3 ? ProgressIndicatorKt.SecondLineHeadDelay : (int) (((f4 / f3) * 300.0f) + 350.0f);
        }

        @Override // com.coui.appcompat.tablayout.COUITabLayout.OnTabSelectedListener
        public void onTabReselected(COUITab cOUITab) {
        }

        @Override // com.coui.appcompat.tablayout.COUITabLayout.OnTabSelectedListener
        public void onTabSelected(COUITab cOUITab) {
            RecyclerView.Adapter adapter;
            if (cOUITab.mView.getSelectedByClick() && (adapter = this.mViewPager.getAdapter()) != null && adapter.getItemCount() > 0) {
                int iMin = Math.min(Math.max(cOUITab.getPosition(), 0), adapter.getItemCount() - 1);
                if (this.mViewPager.getChildAt(0) instanceof RecyclerView) {
                    this.mViewPager.setCurrentItemWithoutAnimation(iMin);
                    RecyclerView recyclerView = (RecyclerView) this.mViewPager.getChildAt(0);
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (linearLayoutManager == null) {
                        return;
                    }
                    getScrollDistanceAndDuration(linearLayoutManager, recyclerView, iMin);
                    this.mViewPager.beginFakeDrag();
                    int[] iArr = this.mScrollDistanceAndDuration;
                    recyclerView.smoothScrollBy(iArr[0], 0, this.mScrollPathInterpolator, iArr[1]);
                }
            }
        }

        @Override // com.coui.appcompat.tablayout.COUITabLayout.OnTabSelectedListener
        public void onTabUnselected(COUITab cOUITab) {
        }
    }

    static {
        try {
            Class cls = Integer.TYPE;
            Class cls2 = Float.TYPE;
            Class cls3 = Boolean.TYPE;
            Method declaredMethod = COUITabLayout.class.getDeclaredMethod("setScrollPosition", cls, cls2, cls3, cls3);
            sSetScrollPosition = declaredMethod;
            declaredMethod.setAccessible(true);
            Method declaredMethod2 = COUITabLayout.class.getDeclaredMethod("selectTab", COUITab.class, cls3);
            sSelectTab = declaredMethod2;
            declaredMethod2.setAccessible(true);
        } catch (NoSuchMethodException unused) {
            throw new IllegalStateException("Can't reflect into method TabLayout.setScrollPosition(int, float, boolean, boolean)");
        }
    }

    public COUITabLayoutMediator(COUITabLayout cOUITabLayout, COUIViewPager2 cOUIViewPager2, OnConfigureTabCallback onConfigureTabCallback) {
        this(cOUITabLayout, cOUIViewPager2, true, onConfigureTabCallback);
    }

    public static void selectTab(COUITabLayout cOUITabLayout, COUITab cOUITab, boolean z2) {
        try {
            Method method = sSelectTab;
            if (method != null) {
                method.invoke(cOUITabLayout, cOUITab, Boolean.valueOf(z2));
            } else {
                throwMethodNotFound(SELECT_TAB_NAME);
            }
        } catch (Exception unused) {
            throwInvokeFailed(SELECT_TAB_NAME);
        }
    }

    public static void setScrollPosition(COUITabLayout cOUITabLayout, int i3, float f3, boolean z2, boolean z3) {
        try {
            Method method = sSetScrollPosition;
            if (method != null) {
                method.invoke(cOUITabLayout, Integer.valueOf(i3), Float.valueOf(f3), Boolean.valueOf(z2), Boolean.valueOf(z3));
            } else {
                throwMethodNotFound(SET_SCROLL_POSITION_NAME);
            }
        } catch (Exception unused) {
            throwInvokeFailed(SET_SCROLL_POSITION_NAME);
        }
    }

    private static void throwInvokeFailed(String str) {
        throw new IllegalStateException("Couldn't invoke method " + str);
    }

    private static void throwMethodNotFound(String str) {
        throw new IllegalStateException("Method " + str + " not found");
    }

    public void attach() {
        if (this.mAttached) {
            throw new IllegalStateException("TabLayoutMediator is already attached");
        }
        RecyclerView.Adapter adapter = this.mViewPager.getAdapter();
        this.mAdapter = adapter;
        if (adapter == null) {
            throw new IllegalStateException("TabLayoutMediator attached before ViewPager2 has an adapter");
        }
        this.mAttached = true;
        TabLayoutOnPageChangeCallback tabLayoutOnPageChangeCallback = new TabLayoutOnPageChangeCallback(this.mTabLayout, this.mViewPager);
        this.mOnPageChangeCallback = tabLayoutOnPageChangeCallback;
        this.mViewPager.registerOnPageChangeCallback(tabLayoutOnPageChangeCallback);
        ViewPagerOnTabSelectedListener viewPagerOnTabSelectedListener = new ViewPagerOnTabSelectedListener(this.mViewPager);
        this.mOnTabSelectedListener = viewPagerOnTabSelectedListener;
        this.mTabLayout.addOnTabSelectedListener(viewPagerOnTabSelectedListener);
        if (this.mAutoRefresh) {
            PagerAdapterObserver pagerAdapterObserver = new PagerAdapterObserver();
            this.mPagerAdapterObserver = pagerAdapterObserver;
            this.mAdapter.registerAdapterDataObserver(pagerAdapterObserver);
        }
        populateTabsFromPagerAdapter();
        this.mTabLayout.setScrollPosition(this.mViewPager.getCurrentItem(), 0.0f, true);
    }

    public void detach() {
        if (this.mAutoRefresh) {
            this.mAdapter.unregisterAdapterDataObserver(this.mPagerAdapterObserver);
        }
        this.mTabLayout.removeOnTabSelectedListener(this.mOnTabSelectedListener);
        this.mViewPager.unregisterOnPageChangeCallback(this.mOnPageChangeCallback);
        this.mPagerAdapterObserver = null;
        this.mOnTabSelectedListener = null;
        this.mOnPageChangeCallback = null;
        this.mAttached = false;
    }

    public void populateTabsFromPagerAdapter() {
        this.mTabLayout.removeAllTabs();
        RecyclerView.Adapter adapter = this.mAdapter;
        if (adapter != null) {
            int itemCount = adapter.getItemCount();
            for (int i3 = 0; i3 < itemCount; i3++) {
                COUITab cOUITabNewTab = this.mTabLayout.newTab();
                int i4 = this.mCustomTabViewType;
                if (i4 != 1) {
                    if (i4 == 2) {
                        cOUITabNewTab.setCustomView(this.mLayoutResAll);
                    }
                } else if (this.mLayoutResIdMap.get(i3, -1) != -1) {
                    cOUITabNewTab.setCustomView(this.mLayoutResIdMap.get(i3));
                }
                String str = this.mTabContentDescMap.get(Integer.valueOf(i3));
                if (str != null) {
                    cOUITabNewTab.setContentDescription(str);
                }
                this.mOnConfigureTabCallback.onConfigureTab(cOUITabNewTab, i3);
                this.mTabLayout.addTab(cOUITabNewTab, false);
            }
            if (itemCount > 0) {
                int currentItem = this.mViewPager.getCurrentItem();
                COUITab tabAt = this.mTabLayout.getTabAt(currentItem);
                if (currentItem == this.mTabLayout.getSelectedTabPosition() || tabAt == null) {
                    return;
                }
                tabAt.select();
            }
        }
    }

    public void setTabCustomView(int i3) {
        this.mCustomTabViewType = 2;
        this.mLayoutResAll = i3;
        if (this.mAttached) {
            populateTabsFromPagerAdapter();
            this.mTabLayout.setScrollPosition(this.mViewPager.getCurrentItem(), 0.0f, true);
        }
    }

    public COUITabLayoutMediator(COUITabLayout cOUITabLayout, COUIViewPager2 cOUIViewPager2, boolean z2, OnConfigureTabCallback onConfigureTabCallback) {
        this.mTabLayout = cOUITabLayout;
        cOUITabLayout.setUpdateindicatorposition(true);
        this.mViewPager = cOUIViewPager2;
        this.mAutoRefresh = z2;
        this.mOnConfigureTabCallback = onConfigureTabCallback;
        this.mCustomTabViewType = 0;
        this.mLayoutResIdMap = new SparseIntArray();
        this.mTabContentDescMap = new HashMap();
    }

    public void setTabCustomView(int i3, int i4) {
        setTabCustomView(i3, i4, null);
    }

    public void setTabCustomView(int i3, int i4, String str) {
        this.mCustomTabViewType = 1;
        this.mLayoutResIdMap.put(i4, i3);
        this.mTabContentDescMap.put(Integer.valueOf(i4), str);
        if (this.mAttached) {
            populateTabsFromPagerAdapter();
            this.mTabLayout.setScrollPosition(this.mViewPager.getCurrentItem(), 0.0f, true);
        }
    }
}