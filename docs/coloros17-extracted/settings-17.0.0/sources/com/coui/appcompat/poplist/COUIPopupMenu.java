package com.coui.appcompat.poplist;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.PopupWindow;
import androidx.appcompat.R;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import java.util.ArrayList;
import java.util.HashMap;

/* JADX INFO: loaded from: classes7.dex */
public class COUIPopupMenu {
    private final Context mContext;
    private final MenuBuilder mMenu;
    private OnMenuItemClickListener mMenuItemClickListener;
    private OnDismissListener mOnDismissListener;
    private COUIPopupListWindow mPopup;
    private HashMap<Integer, Integer> mRedDotMap;

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.COUIPopupMenu$1, reason: invalid class name */
    public class AnonymousClass1 implements MenuBuilder.Callback {
        public AnonymousClass1() {
        }

        @Override // androidx.appcompat.view.menu.MenuBuilder.Callback
        public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
            if (COUIPopupMenu.a(COUIPopupMenu.this) != null) {
                return COUIPopupMenu.a(COUIPopupMenu.this).onMenuItemClick(menuItem);
            }
            return false;
        }

        @Override // androidx.appcompat.view.menu.MenuBuilder.Callback
        public void onMenuModeChange(MenuBuilder menuBuilder) {
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.COUIPopupMenu$2, reason: invalid class name */
    public class AnonymousClass2 implements PopupWindow.OnDismissListener {
        public AnonymousClass2() {
        }

        @Override // android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            if (COUIPopupMenu.b(COUIPopupMenu.this) != null) {
                COUIPopupMenu.b(COUIPopupMenu.this).onDismiss();
            }
        }
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(MenuItem menuItem);
    }

    public COUIPopupMenu(Context context) {
        this(context, null);
    }

    public static /* bridge */ /* synthetic */ OnMenuItemClickListener a(COUIPopupMenu cOUIPopupMenu) {
        return cOUIPopupMenu.mMenuItemClickListener;
    }

    public static /* bridge */ /* synthetic */ OnDismissListener b(COUIPopupMenu cOUIPopupMenu) {
        return cOUIPopupMenu.mOnDismissListener;
    }

    public void dismiss() {
        this.mPopup.dismiss();
    }

    @Deprecated
    public void dismissImmediatly() {
        this.mPopup.superDismiss();
    }

    public Menu getMenu() {
        return this.mMenu;
    }

    public MenuInflater getMenuInflater() {
        return new MenuInflater(this.mContext);
    }

    public COUIPopupListWindow getPopup() {
        return this.mPopup;
    }

    public void inflate(int i3) {
        ArrayList<PopupListItem> arrayList;
        Integer num;
        getMenuInflater().inflate(i3, this.mMenu);
        if (this.mMenu.getNonActionItems().size() <= 0) {
            return;
        }
        ArrayList arrayList2 = new ArrayList();
        PopupListItem.Builder builder = new PopupListItem.Builder();
        for (int i4 = 0; i4 < this.mMenu.getNonActionItems().size(); i4++) {
            MenuItemImpl menuItemImpl = this.mMenu.getNonActionItems().get(i4);
            if (menuItemImpl.hasSubMenu()) {
                arrayList = new ArrayList<>();
                SubMenu subMenu = menuItemImpl.getSubMenu();
                for (int i5 = 0; i5 < subMenu.size(); i5++) {
                    MenuItem item = subMenu.getItem(i5);
                    builder.reset().setIcon(item.getIcon()).setTitle(item.getTitle() != null ? item.getTitle().toString() : "").setIsChecked(item.isChecked()).setGroupId(item.getGroupId());
                    arrayList.add(builder.build());
                }
            } else {
                arrayList = null;
            }
            int i6 = -1;
            int iIntValue = (!this.mRedDotMap.containsKey(Integer.valueOf(menuItemImpl.getItemId())) || (num = this.mRedDotMap.get(Integer.valueOf(menuItemImpl.getItemId()))) == null) ? -1 : num.intValue();
            PopupListItem.Builder redDotAmount = builder.reset().setIcon(menuItemImpl.getIcon()).setTitle(menuItemImpl.getTitle().toString()).setIsChecked(menuItemImpl.isChecked()).setRedDotAmount(iIntValue);
            if (iIntValue != -1) {
                i6 = 0;
            }
            redDotAmount.setHintType(i6).setSubMenuItemList(arrayList).setGroupId(menuItemImpl.getGroupId());
            arrayList2.add(builder.build());
        }
        this.mPopup.setItemList(arrayList2);
    }

    public boolean isShowing() {
        COUIPopupListWindow cOUIPopupListWindow = this.mPopup;
        if (cOUIPopupListWindow != null) {
            return cOUIPopupListWindow.isShowing();
        }
        return false;
    }

    public void setAnchorView(View view) {
        this.mPopup.setAnchorView(view);
    }

    public void setMenuRedDot(int i3, int i4) {
        if (this.mPopup.getItemList().size() <= 0 || this.mMenu.findItem(i3) == null) {
            if (i4 == -1) {
                this.mRedDotMap.remove(Integer.valueOf(i3));
                return;
            } else {
                this.mRedDotMap.put(Integer.valueOf(i3), Integer.valueOf(i4));
                return;
            }
        }
        int iIndexOf = this.mMenu.getNonActionItems().indexOf(this.mMenu.findItem(i3));
        if (iIndexOf >= 0) {
            this.mPopup.getItemList().get(iIndexOf).setRedDotAmount(i4);
        }
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
        this.mPopup.setOnDismissListener(new AnonymousClass2());
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.mMenuItemClickListener = onMenuItemClickListener;
        this.mMenu.setCallback(new AnonymousClass1());
    }

    public void show() {
        this.mPopup.show();
    }

    public COUIPopupMenu(Context context, View view) {
        this(context, view, 0);
    }

    public void show(View view) {
        setAnchorView(view);
        show();
    }

    public COUIPopupMenu(Context context, View view, int i3) {
        this(context, view, i3, R.attr.popupMenuStyle, com.support.poplist.R.style.Widget_COUI_PopupMenu);
    }

    public COUIPopupMenu(Context context, View view, int i3, int i4, int i5) {
        this.mRedDotMap = new HashMap<>();
        this.mContext = context;
        this.mMenu = new MenuBuilder(context);
        COUIPopupListWindow cOUIPopupListWindow = new COUIPopupListWindow(context);
        this.mPopup = cOUIPopupListWindow;
        if (view != null) {
            cOUIPopupListWindow.setAnchorView(view);
        }
    }
}