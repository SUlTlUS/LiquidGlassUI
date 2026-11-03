package com.coui.appcompat.bottomnavigation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.AnimatedStateListDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import com.coui.appcompat.textutil.COUIChangeTextUtil;
import com.support.bottomnavigation.R;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes7.dex */
public class COUINavigationPopupMenu implements PopupWindow.OnDismissListener, AdapterView.OnItemClickListener, View.OnKeyListener, ViewTreeObserver.OnGlobalLayoutListener, View.OnLayoutChangeListener {
    private static final int DROP_DOWN_GRAVITY = 8388693;
    private static final int DROP_DOWN_GRAVITY_RTL = 8388693;
    private static final float POINT_FIVE = 0.5f;
    private final MenuAdapter mAdapter;
    private COUINavigationMenuView mAnchor;
    private final Context mContext;
    private int mDropDownGravity;
    private final LayoutInflater mInflater;
    private ListView mListViewUsedToMeasure;
    private ListPopupWindow mPopup;
    private int mPopupHorizontalMargin;
    private int mPopupMaxHeight;
    private int mPopupVerticalMargin;
    private int mPopupWidth;
    private int mScreenWidth;
    private final float mSuitableFontSize;
    private ViewTreeObserver mTreeObserver;
    private int mUsedSpace;
    private static final int ITEM_LAYOUT = R.layout.coui_navigation_popup_item;
    private static final int POPUP_WINDOW_BACKGROUND = R.drawable.coui_navigation_popup_bg;
    private List<COUINavigationItemView> mMenuList = new ArrayList();
    private List<COUINavigationItemView> mVisibleMenus = new ArrayList();

    public class MenuAdapter extends BaseAdapter {
        public MenuAdapter() {
        }

        private void setIcon(ImageView imageView, COUINavigationItemView cOUINavigationItemView) {
            MenuItemImpl itemData = cOUINavigationItemView.getItemData();
            Drawable icon = itemData.getIcon();
            if (icon != null) {
                imageView.setVisibility(0);
                if (icon instanceof AnimatedStateListDrawable) {
                    imageView.setImageState(new int[]{(itemData.isChecked() ? 1 : -1) * android.R.attr.state_checked}, true);
                } else {
                    Drawable.ConstantState constantState = icon.getConstantState();
                    if (constantState != null) {
                        icon = constantState.newDrawable();
                    }
                    icon = DrawableCompat.wrap(icon).mutate();
                    DrawableCompat.setTintList(icon, COUINavigationPopupMenu.access$300(COUINavigationPopupMenu.this).getIconTintList());
                }
            } else {
                imageView.setVisibility(8);
            }
            imageView.setImageDrawable(icon);
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return COUINavigationPopupMenu.access$000(COUINavigationPopupMenu.this).size();
        }

        @Override // android.widget.Adapter
        public /* bridge */ /* synthetic */ Object getItem(int i3) {
            return getItem(i3);
        }

        @Override // android.widget.Adapter
        public long getItemId(int i3) {
            return i3;
        }

        @Override // android.widget.Adapter
        public View getView(int i3, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = COUINavigationPopupMenu.access$200(COUINavigationPopupMenu.this).inflate(COUINavigationPopupMenu.access$100(), viewGroup, false);
            }
            if (i3 == 0) {
                view.setBackgroundResource(R.drawable.coui_popup_list_top_selector);
            } else if (i3 == getCount() - 1) {
                view.setBackgroundResource(R.drawable.coui_popup_list_bottom_selector);
            } else {
                view.setBackgroundResource(R.drawable.coui_popup_list_center_selector);
            }
            boolean zIsEnabled = ((COUINavigationItemView) COUINavigationPopupMenu.access$000(COUINavigationPopupMenu.this).get(i3)).isEnabled();
            view.setEnabled(zIsEnabled);
            ImageView imageView = (ImageView) view.findViewById(R.id.popup_item_imageView);
            TextView textView = (TextView) view.findViewById(R.id.popup_item_textView);
            imageView.setEnabled(zIsEnabled);
            textView.setEnabled(zIsEnabled);
            setIcon(imageView, (COUINavigationItemView) COUINavigationPopupMenu.access$000(COUINavigationPopupMenu.this).get(i3));
            textView.setText(((COUINavigationItemView) COUINavigationPopupMenu.access$000(COUINavigationPopupMenu.this).get(i3)).getItemData().getTitle());
            textView.setTextColor(COUINavigationPopupMenu.access$300(COUINavigationPopupMenu.this).getItemTextColor());
            textView.setTextSize(0, COUINavigationPopupMenu.access$400(COUINavigationPopupMenu.this));
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
            if (ViewCompat.getLayoutDirection(viewGroup) == 1) {
                marginLayoutParams.rightMargin = COUINavigationPopupMenu.access$500(COUINavigationPopupMenu.this).getResources().getDimensionPixelSize(R.dimen.coui_navigation_popup_horizontal_margin);
                return view;
            }
            marginLayoutParams.leftMargin = COUINavigationPopupMenu.access$500(COUINavigationPopupMenu.this).getResources().getDimensionPixelSize(R.dimen.coui_navigation_popup_horizontal_margin);
            return view;
        }

        @Override // android.widget.Adapter
        public MenuItemImpl getItem(int i3) {
            return ((COUINavigationItemView) COUINavigationPopupMenu.access$000(COUINavigationPopupMenu.this).get(i3)).getItemData();
        }
    }

    public COUINavigationPopupMenu(Context context, COUINavigationMenuView cOUINavigationMenuView) {
        this.mContext = context;
        this.mAnchor = cOUINavigationMenuView;
        cOUINavigationMenuView.addOnLayoutChangeListener(this);
        this.mInflater = LayoutInflater.from(context);
        this.mAdapter = new MenuAdapter();
        if (ViewCompat.getLayoutDirection(cOUINavigationMenuView) == 1) {
            this.mDropDownGravity = 8388693;
        } else {
            this.mDropDownGravity = 8388693;
        }
        Resources resources = context.getResources();
        this.mSuitableFontSize = COUIChangeTextUtil.getSuitableFontSize(resources.getDimensionPixelSize(R.dimen.coui_navigation_popup_text_size), resources.getConfiguration().fontScale, 5);
        this.mPopupHorizontalMargin = resources.getDimensionPixelOffset(R.dimen.coui_navigation_popup_horizontal_margin);
        this.mPopupVerticalMargin = resources.getDimensionPixelOffset(R.dimen.coui_navigation_popup_vertical_margin);
        this.mUsedSpace = resources.getDimensionPixelSize(R.dimen.coui_navigation_popup_item_used_space);
        this.mPopupWidth = resources.getDimensionPixelOffset(R.dimen.coui_navigation_popup_item_min_width);
        this.mPopupMaxHeight = resources.getDimensionPixelOffset(R.dimen.coui_navigation_popup_max_height);
        if (resources.getConfiguration().orientation == 1) {
            this.mScreenWidth = resources.getDisplayMetrics().widthPixels;
        } else {
            this.mScreenWidth = resources.getDisplayMetrics().heightPixels;
        }
        ListView listView = new ListView(context);
        this.mListViewUsedToMeasure = listView;
        listView.setDivider(null);
        this.mListViewUsedToMeasure.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
    }

    public static /* synthetic */ List access$000(COUINavigationPopupMenu cOUINavigationPopupMenu) {
        return cOUINavigationPopupMenu.mVisibleMenus;
    }

    public static /* synthetic */ int access$100() {
        return ITEM_LAYOUT;
    }

    public static /* synthetic */ LayoutInflater access$200(COUINavigationPopupMenu cOUINavigationPopupMenu) {
        return cOUINavigationPopupMenu.mInflater;
    }

    public static /* synthetic */ COUINavigationMenuView access$300(COUINavigationPopupMenu cOUINavigationPopupMenu) {
        return cOUINavigationPopupMenu.mAnchor;
    }

    public static /* synthetic */ float access$400(COUINavigationPopupMenu cOUINavigationPopupMenu) {
        return cOUINavigationPopupMenu.mSuitableFontSize;
    }

    public static /* synthetic */ Context access$500(COUINavigationPopupMenu cOUINavigationPopupMenu) {
        return cOUINavigationPopupMenu.mContext;
    }

    private int measureHeightOfChildrenCompat(int i3) {
        int i4;
        MenuAdapter menuAdapter = this.mAdapter;
        int count = menuAdapter.getCount();
        int measuredHeight = 0;
        int i5 = 0;
        View view = null;
        for (int i6 = 0; i6 < count; i6++) {
            int itemViewType = menuAdapter.getItemViewType(i6);
            if (itemViewType != i5) {
                view = null;
                i5 = itemViewType;
            }
            view = menuAdapter.getView(i6, view, this.mListViewUsedToMeasure);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            view.measure(i3, (layoutParams == null || (i4 = layoutParams.height) <= 0) ? View.MeasureSpec.makeMeasureSpec(0, 0) : View.MeasureSpec.makeMeasureSpec(i4, 1073741824));
            measuredHeight += view.getMeasuredHeight();
        }
        return measuredHeight;
    }

    private boolean tryShow() {
        ListPopupWindow listPopupWindow = new ListPopupWindow(this.mContext, null, 0);
        this.mPopup = listPopupWindow;
        listPopupWindow.setOnDismissListener(this);
        this.mPopup.setOnItemClickListener(this);
        this.mPopup.setAdapter(this.mAdapter);
        this.mPopup.setModal(true);
        COUINavigationMenuView cOUINavigationMenuView = this.mAnchor;
        if (cOUINavigationMenuView == null) {
            return false;
        }
        boolean z2 = this.mTreeObserver == null;
        ViewTreeObserver viewTreeObserver = cOUINavigationMenuView.getViewTreeObserver();
        this.mTreeObserver = viewTreeObserver;
        if (z2) {
            viewTreeObserver.addOnGlobalLayoutListener(this);
        }
        this.mPopup.setAnchorView(cOUINavigationMenuView);
        this.mPopup.setDropDownGravity(this.mDropDownGravity);
        Rect rect = new Rect();
        Drawable drawable = this.mContext.getResources().getDrawable(POPUP_WINDOW_BACKGROUND);
        drawable.getPadding(rect);
        int i3 = this.mPopupWidth;
        int i4 = rect.left;
        int i5 = rect.right;
        int iMin = Math.min(i3 + i4 + i5, (this.mScreenWidth - (this.mPopupHorizontalMargin * 2)) + i4 + i5);
        this.mPopup.setContentWidth(iMin);
        int iMeasureHeightOfChildrenCompat = measureHeightOfChildrenCompat(View.MeasureSpec.makeMeasureSpec((iMin - rect.right) - rect.left, 1073741824));
        this.mPopup.setBackgroundDrawable(drawable);
        int i6 = rect.top;
        int i7 = rect.bottom;
        int i8 = iMeasureHeightOfChildrenCompat + i6 + i7;
        int i9 = this.mPopupMaxHeight;
        boolean z3 = i8 >= i9;
        this.mPopup.setHeight(Math.min(iMeasureHeightOfChildrenCompat + i6 + i7, i9));
        this.mPopup.setInputMethodMode(2);
        int i10 = (-this.mPopupHorizontalMargin) + rect.right;
        int i11 = rect.bottom - this.mPopupVerticalMargin;
        this.mPopup.setHorizontalOffset(i10);
        this.mPopup.setVerticalOffset(i11);
        this.mPopup.show();
        ListView listView = this.mPopup.getListView();
        listView.setBackgroundDrawable(null);
        listView.setSelector(new ColorDrawable(0));
        listView.setOnKeyListener(this);
        if (z3) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) listView.getLayoutParams();
            marginLayoutParams.leftMargin = rect.left;
            marginLayoutParams.rightMargin = rect.right;
            listView.setLayoutParams(marginLayoutParams);
        }
        return true;
    }

    public void addMenuItem(COUINavigationItemView cOUINavigationItemView) {
        this.mMenuList.add(cOUINavigationItemView);
        MenuItemImpl itemData = cOUINavigationItemView.getItemData();
        if (itemData.isVisible()) {
            Paint paint = new Paint();
            paint.setTextSize(this.mSuitableFontSize);
            COUIChangeTextUtil.adaptBoldAndMediumFont(paint, true);
            this.mVisibleMenus.add(cOUINavigationItemView);
            this.mPopupWidth = (int) (Math.max(this.mPopupWidth, paint.measureText(itemData.getTitle().toString()) + this.mUsedSpace) + 0.5f);
        }
    }

    public void clearMenuItems() {
        this.mMenuList.clear();
        this.mVisibleMenus.clear();
    }

    public void dismiss() {
        this.mPopup.dismiss();
    }

    public boolean isShowing() {
        ListPopupWindow listPopupWindow = this.mPopup;
        return listPopupWindow != null && listPopupWindow.isShowing();
    }

    public void notifyMenuChange() {
        this.mVisibleMenus.clear();
        for (COUINavigationItemView cOUINavigationItemView : this.mMenuList) {
            MenuItemImpl itemData = cOUINavigationItemView.getItemData();
            if (itemData.isVisible() && itemData.isEnabled()) {
                this.mVisibleMenus.add(cOUINavigationItemView);
            }
        }
        this.mAdapter.notifyDataSetChanged();
    }

    @Override // android.widget.PopupWindow.OnDismissListener
    public void onDismiss() {
        ViewTreeObserver viewTreeObserver = this.mTreeObserver;
        if (viewTreeObserver != null) {
            if (!viewTreeObserver.isAlive()) {
                this.mTreeObserver = this.mAnchor.getViewTreeObserver();
            }
            this.mTreeObserver.removeGlobalOnLayoutListener(this);
            this.mTreeObserver = null;
        }
    }

    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
    public void onGlobalLayout() {
        if (isShowing()) {
            COUINavigationMenuView cOUINavigationMenuView = this.mAnchor;
            if (cOUINavigationMenuView == null || !cOUINavigationMenuView.isShown()) {
                dismiss();
            } else if (isShowing()) {
                this.mPopup.show();
            }
        }
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i3, long j3) {
        if (this.mVisibleMenus.get(i3).isEnabled()) {
            this.mPopup.dismiss();
            this.mVisibleMenus.get(i3).performClick();
        }
    }

    @Override // android.view.View.OnKeyListener
    public boolean onKey(View view, int i3, KeyEvent keyEvent) {
        if (keyEvent.getAction() != 1 || i3 != 82) {
            return false;
        }
        dismiss();
        return true;
    }

    @Override // android.view.View.OnLayoutChangeListener
    public void onLayoutChange(View view, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        ListPopupWindow listPopupWindow = this.mPopup;
        if (listPopupWindow == null || !listPopupWindow.isShowing()) {
            return;
        }
        this.mPopup.dismiss();
    }

    public void setGravity(int i3) {
        this.mDropDownGravity = i3;
    }

    public void show() {
        if (!tryShow()) {
            throw new IllegalStateException("MenuPopupHelper cannot be used without an anchor");
        }
    }
}