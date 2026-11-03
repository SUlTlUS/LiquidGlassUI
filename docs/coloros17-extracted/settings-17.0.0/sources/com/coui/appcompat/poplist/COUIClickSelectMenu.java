package com.coui.appcompat.poplist;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import com.oplus.settings.appfunctions.SystemUpdateScreenStateConfig;
import com.support.poplist.R;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes7.dex */
public class COUIClickSelectMenu {
    private static final String TAG = "COUIClickSelectMenu";
    private boolean mEnable;
    private PreciseClickHelper mHelper;
    private InputMethodManager mInputManager;
    private PreciseClickHelper.OnPreciseClickListener mListener;
    private COUIContextMenu.MenuShowStateListener mMenuShowStateListener;
    private final COUIPopupListWindow mPopup;

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.COUIClickSelectMenu$1, reason: invalid class name */
    public class AnonymousClass1 implements PreciseClickHelper.OnPreciseClickListener {
        public AnonymousClass1() {
        }

        public static /* synthetic */ void a(AnonymousClass1 anonymousClass1, View view, int i3, int i4) {
            COUIClickSelectMenu.this.setPopupShow(view, i3, i4);
        }

        @Override // com.coui.appcompat.poplist.PreciseClickHelper.OnPreciseClickListener
        public void onClick(View view, int i3, int i4) {
            if (COUIClickSelectMenu.b(COUIClickSelectMenu.this) != null) {
                COUIClickSelectMenu.b(COUIClickSelectMenu.this).onClick(view, i3, i4);
            }
            if (COUIClickSelectMenu.a(COUIClickSelectMenu.this) == null || !COUIClickSelectMenu.a(COUIClickSelectMenu.this).hideSoftInputFromWindow(view.getWindowToken(), 0)) {
                COUIClickSelectMenu.this.setPopupShow(view, i3, i4);
            } else {
                view.postDelayed(new c(this, view, i3, i4), view.getContext().getResources().getInteger(R.integer.support_menu_click_select_time));
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.COUIClickSelectMenu$2, reason: invalid class name */
    public class AnonymousClass2 implements PopupWindow.OnDismissListener {
        public AnonymousClass2() {
        }

        @Override // android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            if (COUIClickSelectMenu.c(COUIClickSelectMenu.this) != null) {
                COUIClickSelectMenu.c(COUIClickSelectMenu.this).onDismiss();
            }
        }
    }

    public COUIClickSelectMenu(Context context) {
        this(context, null);
    }

    public static /* bridge */ /* synthetic */ InputMethodManager a(COUIClickSelectMenu cOUIClickSelectMenu) {
        return cOUIClickSelectMenu.mInputManager;
    }

    public static /* bridge */ /* synthetic */ PreciseClickHelper.OnPreciseClickListener b(COUIClickSelectMenu cOUIClickSelectMenu) {
        return cOUIClickSelectMenu.mListener;
    }

    public static /* bridge */ /* synthetic */ COUIContextMenu.MenuShowStateListener c(COUIClickSelectMenu cOUIClickSelectMenu) {
        return cOUIClickSelectMenu.mMenuShowStateListener;
    }

    public void dismiss() {
        this.mPopup.dismiss();
    }

    public COUIPopupListWindow getPopup() {
        return this.mPopup;
    }

    public void registerForClickSelectItems(View view, ArrayList<PopupListItem> arrayList) {
        if (arrayList.size() <= 0) {
            return;
        }
        this.mPopup.setItemList(arrayList);
        view.setClickable(true);
        view.setLongClickable(true);
        this.mPopup.setAnchorView(view);
        PreciseClickHelper preciseClickHelper = this.mHelper;
        if (preciseClickHelper == null || preciseClickHelper.getTargetView() != view) {
            this.mHelper = new PreciseClickHelper(view, new AnonymousClass1());
        } else {
            Log.w(TAG, "ItemView is same, no need to create PreciseClickHelper");
        }
    }

    @Deprecated
    public void setEnableAddExtraWidth(boolean z2) {
        COUIPopupListWindow cOUIPopupListWindow;
        if (!this.mEnable || (cOUIPopupListWindow = this.mPopup) == null) {
            return;
        }
        cOUIPopupListWindow.setEnableAddExtraWidth(z2);
    }

    public void setHelperEnabled(boolean z2) {
        PreciseClickHelper preciseClickHelper = this.mHelper;
        if (preciseClickHelper != null) {
            this.mEnable = z2;
            if (z2) {
                preciseClickHelper.setup();
            } else {
                preciseClickHelper.unSet();
            }
        }
    }

    @Deprecated
    public void setMaxShowItemCount(int i3) {
        COUIPopupListWindow cOUIPopupListWindow = this.mPopup;
        if (cOUIPopupListWindow != null) {
            cOUIPopupListWindow.setMaxShowItemCount(i3);
        }
    }

    public void setMenuShowStateListener(COUIContextMenu.MenuShowStateListener menuShowStateListener) {
        this.mMenuShowStateListener = menuShowStateListener;
        this.mPopup.setOnDismissListener(new AnonymousClass2());
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mPopup.setOnItemClickListener(onItemClickListener);
    }

    public void setOnPreciseClickListener(PreciseClickHelper.OnPreciseClickListener onPreciseClickListener) {
        this.mListener = onPreciseClickListener;
    }

    public void setPopupShow(View view) {
        setPopupShow(view, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public void setPopupState(boolean z2) {
        if (!z2 || !this.mEnable) {
            dismiss();
            return;
        }
        this.mPopup.show();
        COUIContextMenu.MenuShowStateListener menuShowStateListener = this.mMenuShowStateListener;
        if (menuShowStateListener != null) {
            menuShowStateListener.onShow();
        }
    }

    public void setSubMenuItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mPopup.setSubMenuClickListener(onItemClickListener);
    }

    public COUIClickSelectMenu(Context context, View view) {
        this.mEnable = true;
        COUIPopupListWindow cOUIPopupListWindow = new COUIPopupListWindow(context);
        this.mPopup = cOUIPopupListWindow;
        if (view != null) {
            cOUIPopupListWindow.setAnchorView(view);
        }
        this.mInputManager = (InputMethodManager) context.getSystemService(SystemUpdateScreenStateConfig.KEY_INPUT_METHOD);
    }

    public void setPopupShow(View view, int i3, int i4) {
        if (this.mEnable) {
            this.mPopup.show(view, i3, i4);
            COUIContextMenu.MenuShowStateListener menuShowStateListener = this.mMenuShowStateListener;
            if (menuShowStateListener != null) {
                menuShowStateListener.onShow();
            }
        }
    }

    @Deprecated
    public void registerForClickSelectItems(View view, ArrayList<PopupListItem> arrayList, int i3) {
        registerForClickSelectItems(view, arrayList);
        this.mPopup.setSelectItemColor(i3);
    }
}