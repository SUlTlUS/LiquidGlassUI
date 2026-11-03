package com.coui.appcompat.poplist;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes7.dex */
public class COUIContextMenu {
    private static final String TAG = "COUIContextMenu";
    private PreciseLongPressHelper mHelper;
    private MenuShowStateListener mMenuShowStateListener;
    private COUIPopupListWindow mPopup;

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.COUIContextMenu$1, reason: invalid class name */
    public class AnonymousClass1 implements PreciseLongPressHelper.OnPreciseLongClickListener {
        public AnonymousClass1() {
        }

        @Override // com.coui.appcompat.poplist.PreciseLongPressHelper.OnPreciseLongClickListener
        public void onLongClick(View view, int i3, int i4) {
            COUIContextMenu.b(COUIContextMenu.this).show(view, i3, i4);
            if (COUIContextMenu.a(COUIContextMenu.this) != null) {
                COUIContextMenu.a(COUIContextMenu.this).onShow();
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.COUIContextMenu$2, reason: invalid class name */
    public class AnonymousClass2 implements PreciseLongPressHelper.OnPreciseLongClickListener {
        final /* synthetic */ COUIPopupListWindow val$popup;

        public AnonymousClass2(COUIPopupListWindow cOUIPopupListWindow) {
            this.val$popup = cOUIPopupListWindow;
        }

        @Override // com.coui.appcompat.poplist.PreciseLongPressHelper.OnPreciseLongClickListener
        public void onLongClick(View view, int i3, int i4) {
            this.val$popup.show(view, i3, i4);
            if (COUIContextMenu.a(COUIContextMenu.this) != null) {
                COUIContextMenu.a(COUIContextMenu.this).onShow();
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.COUIContextMenu$3, reason: invalid class name */
    public class AnonymousClass3 implements PopupWindow.OnDismissListener {
        public AnonymousClass3() {
        }

        @Override // android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            if (COUIContextMenu.a(COUIContextMenu.this) != null) {
                COUIContextMenu.a(COUIContextMenu.this).onDismiss();
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.COUIContextMenu$4, reason: invalid class name */
    public class AnonymousClass4 implements PopupWindow.OnDismissListener {
        public AnonymousClass4() {
        }

        @Override // android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            if (COUIContextMenu.a(COUIContextMenu.this) != null) {
                COUIContextMenu.a(COUIContextMenu.this).onDismiss();
            }
        }
    }

    public interface MenuShowStateListener {
        void onDismiss();

        void onShow();
    }

    public COUIContextMenu(Context context) {
        this(context, null);
    }

    public static /* bridge */ /* synthetic */ MenuShowStateListener a(COUIContextMenu cOUIContextMenu) {
        return cOUIContextMenu.mMenuShowStateListener;
    }

    public static /* bridge */ /* synthetic */ COUIPopupListWindow b(COUIContextMenu cOUIContextMenu) {
        return cOUIContextMenu.mPopup;
    }

    public void dismiss() {
        if (this.mPopup.isShowing()) {
            this.mPopup.dismiss();
        }
    }

    public COUIPopupListWindow getPopupListWindow() {
        return this.mPopup;
    }

    public void registerForContextMenu(View view, MenuBuilder menuBuilder) {
        if (menuBuilder.getNonActionItems().size() <= 0) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        PopupListItem.Builder builder = new PopupListItem.Builder();
        for (int i3 = 0; i3 < menuBuilder.getNonActionItems().size(); i3++) {
            MenuItemImpl menuItemImpl = menuBuilder.getNonActionItems().get(i3);
            builder.setId(menuItemImpl.getItemId()).setIcon(menuItemImpl.getIcon()).setTitle(menuItemImpl.getTitle().toString()).setIsChecked(menuItemImpl.isChecked()).setIsEnable(menuItemImpl.isEnabled());
            arrayList.add(builder.build());
        }
        this.mPopup.setItemList(arrayList);
        view.setLongClickable(true);
        PreciseLongPressHelper preciseLongPressHelper = new PreciseLongPressHelper(view, new AnonymousClass1());
        this.mHelper = preciseLongPressHelper;
        preciseLongPressHelper.setup();
    }

    public void setMenuShowStateListener(MenuShowStateListener menuShowStateListener) {
        this.mMenuShowStateListener = menuShowStateListener;
        this.mPopup.setOnDismissListener(new AnonymousClass4());
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mPopup.setOnItemClickListener(onItemClickListener);
    }

    public void setOnTouchListener(View.OnTouchListener onTouchListener) {
        PreciseLongPressHelper preciseLongPressHelper = this.mHelper;
        if (preciseLongPressHelper != null) {
            preciseLongPressHelper.setTouchListenerTransfer(onTouchListener);
        } else {
            Log.e(TAG, "Please call registerForContextMenu first!");
        }
    }

    public COUIContextMenu(Context context, View view) {
        COUIPopupListWindow cOUIPopupListWindow = new COUIPopupListWindow(context);
        this.mPopup = cOUIPopupListWindow;
        if (view != null) {
            cOUIPopupListWindow.setAnchorView(view);
        }
    }

    public void registerForContextMenu(View view, COUIPopupListWindow cOUIPopupListWindow) {
        cOUIPopupListWindow.setDismissTouchOutside(true);
        view.setLongClickable(true);
        this.mHelper = new PreciseLongPressHelper(view, new AnonymousClass2(cOUIPopupListWindow));
        cOUIPopupListWindow.setOnDismissListener(new AnonymousClass3());
        this.mHelper.setup();
    }
}