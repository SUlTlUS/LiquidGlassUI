package com.coui.appcompat.poplist;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import com.coui.appcompat.log.COUILog;
import com.support.poplist.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIIsolatedPopupListWindow extends COUIPopupListWindow {
    private static final boolean COUI_DEBUG;
    private static final String TAG = "COUIIsolatedPopupList";
    private PopupWindow.OnDismissListener mOnDismissListener;

    public class DummyAnchorView extends View implements PopupMenuConfigRule {
        private boolean mConfigRuleEnabled;
        private int mPositionX;
        private int mPositionY;

        public DummyAnchorView(Context context) {
            super(context);
            this.mConfigRuleEnabled = true;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
        public int getBarrierDirection() {
            return -1;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
        public Rect getDisplayFrame() {
            int i3 = this.mPositionX;
            int i4 = this.mPositionY;
            return new Rect(i3, i4, i3 + 1, i4 + 1);
        }

        @Override // android.view.View
        public boolean getGlobalVisibleRect(Rect rect, Point point) {
            int x2 = (int) getX();
            int y2 = (int) getY();
            int width = getWidth() + x2;
            int height = getHeight() + y2;
            rect.set(x2, y2, width, height);
            if (!COUIIsolatedPopupListWindow.H()) {
                return true;
            }
            Log.d(COUIIsolatedPopupListWindow.TAG, "DummyAnchorView getGlobalVisibleRect x " + x2 + " y " + y2 + " right " + width + " bottom " + height);
            return true;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
        public Rect getOutsets() {
            return new Rect(0, 0, 0, 0);
        }

        @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
        public boolean getPopupMenuRuleEnabled() {
            return this.mConfigRuleEnabled;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
        public int getType() {
            return 1;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
        public void setPopupMenuRuleEnabled(boolean z2) {
            this.mConfigRuleEnabled = z2;
        }

        public void setPosition(int i3, int i4) {
            this.mPositionX = i3;
            this.mPositionY = i4;
        }
    }

    public class DummyRootView extends ViewGroup {
        public DummyRootView(Context context) {
            super(context);
        }

        @Override // android.view.View
        public boolean getGlobalVisibleRect(Rect rect, Point point) {
            int x2 = (int) getX();
            int y2 = (int) getY();
            int width = getWidth() + x2;
            int height = getHeight() + y2;
            rect.set(x2, y2, width, height);
            if (!COUIIsolatedPopupListWindow.H()) {
                return true;
            }
            Log.d(COUIIsolatedPopupListWindow.TAG, "DummyRootView getGlobalVisibleRect x " + x2 + " y " + y2 + " right " + width + " bottom " + height);
            return true;
        }

        @Override // android.view.ViewGroup, android.view.View
        public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
            if (COUIIsolatedPopupListWindow.H()) {
                Log.d(COUIIsolatedPopupListWindow.TAG, "onLayout b " + z2 + " left " + i3 + " top " + i4 + " right " + i5 + " bottom " + i6);
            }
        }
    }

    static {
        COUI_DEBUG = COUILog.LOG_DEBUG || COUILog.isLoggable(TAG, 3);
    }

    public COUIIsolatedPopupListWindow(Context context) {
        super(context);
    }

    public static /* bridge */ /* synthetic */ boolean H() {
        return COUI_DEBUG;
    }

    private View buildDummyAnchor(Context context, int i3, int i4) {
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int i5 = point.y;
        int i6 = point.x;
        DummyRootView dummyRootView = new DummyRootView(context);
        DummyAnchorView dummyAnchorView = new DummyAnchorView(context);
        dummyRootView.setRight(i6);
        dummyRootView.setBottom(i5);
        dummyAnchorView.setX(i3);
        dummyAnchorView.setY(i4);
        dummyAnchorView.setRight(1);
        dummyAnchorView.setBottom(1);
        if (COUI_DEBUG) {
            Log.d(TAG, "buildDummyAnchor x " + i3 + " y " + i4);
        }
        dummyAnchorView.setPosition(i3, i4);
        dummyRootView.addView(dummyAnchorView);
        return dummyAnchorView;
    }

    @Override // com.coui.appcompat.poplist.COUIPopupListWindow, android.widget.PopupWindow
    public void dismiss() {
        try {
            ((WindowManager) getContentView().getContext().getSystemService("window")).removeViewImmediate(getContentView());
        } catch (Exception e3) {
            COUILog.e(TAG, "Dismiss exception:" + e3.getMessage());
        }
        PopupWindow.OnDismissListener onDismissListener = this.mOnDismissListener;
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
    }

    public void setIsolatedOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    public void show(Context context, int i3, int i4) {
        prepareShowMainMenu(buildDummyAnchor(context, i3, i4), 0, 0, false);
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = 2038;
        layoutParams.width = getLocateHelper().mApplicationWindow.width();
        layoutParams.height = getLocateHelper().mApplicationWindow.height();
        layoutParams.format = 1;
        layoutParams.gravity = 51;
        layoutParams.windowAnimations = R.style.Animation_COUI_IsolatedPopupListWindow;
        windowManager.addView(getContentView(), layoutParams);
    }

    public void show(Context context, int i3, int i4, WindowManager.LayoutParams layoutParams) {
        prepareShowMainMenu(buildDummyAnchor(context, i3, i4), 0, 0, false);
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        layoutParams.windowAnimations = R.style.Animation_COUI_IsolatedPopupListWindow;
        windowManager.addView(getContentView(), layoutParams);
    }
}