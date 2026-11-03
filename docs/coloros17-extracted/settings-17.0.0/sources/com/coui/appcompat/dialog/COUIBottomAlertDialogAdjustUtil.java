package com.coui.appcompat.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import com.coui.appcompat.dialog.widget.COUIAlertDialogMaxLinearLayout;
import com.coui.appcompat.uiutil.FollowHandManager;
import com.coui.appcompat.uiutil.ShadowUtils;
import com.coui.appcompat.uiutil.UIUtil;
import com.support.dialog.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIBottomAlertDialogAdjustUtil {

    /* JADX INFO: renamed from: com.coui.appcompat.dialog.COUIBottomAlertDialogAdjustUtil$1, reason: invalid class name */
    public class AnonymousClass1 implements OnFirstLayoutListener {
        final /* synthetic */ View val$anchorView;
        final /* synthetic */ Point val$extraOffsetPoint;
        final /* synthetic */ Point val$touchPositionPoint;
        final /* synthetic */ Window val$window;

        /* JADX INFO: renamed from: com.coui.appcompat.dialog.COUIBottomAlertDialogAdjustUtil$1$1, reason: invalid class name and collision with other inner class name */
        public class ViewOnLayoutChangeListenerC04511 implements View.OnLayoutChangeListener {
            final /* synthetic */ View val$parentPanel;

            public ViewOnLayoutChangeListenerC04511(View view) {
                this.val$parentPanel = view;
            }

            public static /* synthetic */ void a(Window window, View view, Point point, Point point2) {
                COUIBottomAlertDialogAdjustUtil.access$100(window, view, point, point2);
                window.getDecorView().setVisibility(0);
            }

            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View view, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
                this.val$parentPanel.removeOnLayoutChangeListener(this);
                View view2 = this.val$parentPanel;
                AnonymousClass1 anonymousClass1 = AnonymousClass1.this;
                view2.post(new d(anonymousClass1.val$window, anonymousClass1.val$anchorView, anonymousClass1.val$touchPositionPoint, anonymousClass1.val$extraOffsetPoint));
            }
        }

        public AnonymousClass1(Window window, Point point, View view, Point point2) {
            this.val$window = window;
            this.val$touchPositionPoint = point;
            this.val$anchorView = view;
            this.val$extraOffsetPoint = point2;
        }

        @Override // com.coui.appcompat.dialog.COUIBottomAlertDialogAdjustUtil.OnFirstLayoutListener
        public void onFirstLayout() {
            View viewFindViewById = this.val$window.findViewById(R.id.parentPanel);
            if (viewFindViewById == null) {
                return;
            }
            Point point = this.val$touchPositionPoint;
            if (point == null) {
                FollowHandManager.init(this.val$anchorView);
            } else {
                FollowHandManager.init(this.val$anchorView, point.x, point.y);
            }
            COUIBottomAlertDialogAdjustUtil.access$000(this.val$window, true);
            viewFindViewById.addOnLayoutChangeListener(new ViewOnLayoutChangeListenerC04511(viewFindViewById));
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.dialog.COUIBottomAlertDialogAdjustUtil$2, reason: invalid class name */
    public class AnonymousClass2 implements ViewTreeObserver.OnGlobalLayoutListener {
        final /* synthetic */ OnFirstLayoutListener val$onFirstLayoutListener;
        final /* synthetic */ Window val$window;

        public AnonymousClass2(Window window, OnFirstLayoutListener onFirstLayoutListener) {
            this.val$window = window;
            this.val$onFirstLayoutListener = onFirstLayoutListener;
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            this.val$window.getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
            this.val$onFirstLayoutListener.onFirstLayout();
        }
    }

    public interface OnFirstLayoutListener {
        void onFirstLayout();
    }

    public static /* synthetic */ void access$000(Window window, boolean z2) {
        updateParentPanel(window, z2);
    }

    public static /* synthetic */ void access$100(Window window, View view, Point point, Point point2) {
        updateWindowLocation(window, view, point, point2);
    }

    public static void adjustToFree(Window window, View view) {
        adjustToFree(window, view, null);
    }

    private static int dpToPx(Context context, float f3) {
        return Math.round(TypedValue.applyDimension(1, f3, context.getResources().getDisplayMetrics()));
    }

    private static int getDimensionPixel(Window window, int i3, int i4) {
        Resources resources = window.getDecorView().getResources();
        return (resources == null || i3 == 0) ? i4 : resources.getDimensionPixelOffset(i3);
    }

    private static Drawable getDrawable(Window window, int i3) {
        Context context = window.getDecorView().getContext();
        if (context == null || i3 == 0) {
            return null;
        }
        return context.getDrawable(i3);
    }

    public static Rect getLocationRectInScreen(View view) {
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        int i3 = iArr[0];
        return new Rect(i3, iArr[1], view.getMeasuredWidth() + i3, iArr[1] + view.getMeasuredHeight());
    }

    private static int lerp(int i3, int i4, int i5) {
        return Math.max(i4, Math.min(i3, i5));
    }

    private static void offsetWindowTo(Window window, int i3, int i4) {
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.x = i3;
        attributes.y = i4;
        window.setAttributes(attributes);
    }

    private static void refreshParentPanelLayoutMargin(View view) {
        if (view instanceof COUIAlertDialogMaxLinearLayout) {
            ((COUIAlertDialogMaxLinearLayout) view).refreshDialogLayoutMargin();
        }
    }

    private static void setFirstLayoutListener(Window window, OnFirstLayoutListener onFirstLayoutListener) {
        if (onFirstLayoutListener == null) {
            return;
        }
        window.getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new AnonymousClass2(window, onFirstLayoutListener));
    }

    private static void setWindowWidth(Window window, int i3) {
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = i3;
        window.setAttributes(attributes);
    }

    private static void updateParentPanel(Window window, boolean z2) {
        View viewFindViewById = window.findViewById(R.id.parentPanel);
        if (viewFindViewById instanceof COUIAlertDialogMaxLinearLayout) {
            if (z2) {
                int dimensionPixel = getDimensionPixel(window, com.support.appcompat.R.dimen.support_shadow_size_level_four, 0);
                LinearLayout linearLayout = new LinearLayout(window.getContext());
                linearLayout.setLayoutParams(new ViewGroup.LayoutParams(UIUtil.dip2px(window.getContext(), window.getContext().getResources().getConfiguration().screenWidthDp), FollowHandManager.getDecorViewRectInWindow().height()));
                if (FollowHandManager.ifWidthDpIsFullScreen(window.getContext()) && FollowHandManager.getWindowLocationOnScreen()[0] > 0) {
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(window.getAttributes());
                    layoutParams.gravity = 8388659;
                    layoutParams.x = FollowHandManager.getWindowLocationOnScreen()[0];
                    layoutParams.y = FollowHandManager.getWindowLocationOnScreen()[1];
                    window.setAttributes(layoutParams);
                }
                UIUtil.safeForceHasOverlappingRendering(linearLayout, false);
                linearLayout.setClipToOutline(false);
                linearLayout.setClipChildren(false);
                viewFindViewById.setClipToOutline(false);
                ((COUIAlertDialogMaxLinearLayout) viewFindViewById).setClipChildren(false);
                ViewGroup viewGroup = (ViewGroup) viewFindViewById.getParent();
                viewGroup.removeView(viewFindViewById);
                viewGroup.addView(linearLayout);
                linearLayout.addView(viewFindViewById);
                ShadowUtils.setElevationToView(viewFindViewById, 3, dimensionPixel, ContextCompat.getColor(window.getContext(), R.color.coui_dialog_follow_hand_spot_shadow_color));
            } else {
                ((COUIAlertDialogMaxLinearLayout) viewFindViewById).setMaxWidth(getDimensionPixel(window, R.dimen.coui_dialog_max_width, 0));
            }
            viewFindViewById.setBackground(getDrawable(window, R.drawable.coui_alert_dialog_builder_background));
            refreshParentPanelLayoutMargin(viewFindViewById);
            viewFindViewById.requestLayout();
        }
    }

    private static void updateWindowLocation(Window window, View view, Point point, Point point2) {
        Point pointCalculatePosition;
        if (view == null && point != null) {
            offsetWindowTo(window, point.x, point.y);
            return;
        }
        int dimensionPixel = getDimensionPixel(window, R.dimen.coui_alert_dialog_layout_anchor_view_padding_top, 0);
        View viewFindViewById = window.findViewById(R.id.parentPanel);
        if (point == null) {
            pointCalculatePosition = FollowHandManager.calculatePosition(view.getContext(), viewFindViewById.getMeasuredWidth(), viewFindViewById.getMeasuredHeight() + dimensionPixel, false);
            if (pointCalculatePosition.y < FollowHandManager.getClickPositionYInWindow()) {
                pointCalculatePosition.y += dpToPx(view.getContext(), 8.0f);
            }
        } else {
            pointCalculatePosition = FollowHandManager.calculatePosition(view.getContext(), viewFindViewById.getMeasuredWidth(), viewFindViewById.getMeasuredHeight() + dimensionPixel, false);
        }
        int i3 = pointCalculatePosition.y - FollowHandManager.getDecorViewRectInWindow().top;
        pointCalculatePosition.y = i3;
        if (point2 != null) {
            pointCalculatePosition.x += point2.x;
            pointCalculatePosition.y = i3 + point2.y;
        }
        if (viewFindViewById instanceof LinearLayout) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewFindViewById.getLayoutParams();
            layoutParams.topMargin = pointCalculatePosition.y + dimensionPixel;
            Rect rect = new Rect();
            if (FollowHandManager.getWindowLocationOnScreen()[0] == 0) {
                view.getWindowVisibleDisplayFrame(rect);
            }
            if (ViewCompat.getLayoutDirection(view) == 1) {
                layoutParams.setMarginStart(((FollowHandManager.getDecorViewRectInWindow().width() - pointCalculatePosition.x) - viewFindViewById.getMeasuredWidth()) + rect.left);
            } else {
                layoutParams.setMarginStart(pointCalculatePosition.x - rect.left);
            }
            viewFindViewById.setLayoutParams(layoutParams);
        }
    }

    public static void adjustToFree(Window window, View view, Point point) {
        adjustToFree(window, view, point, null);
    }

    public static void adjustToFree(Window window, View view, Point point, Point point2) {
        setWindowWidth(window, -2);
        window.clearFlags(2);
        window.setGravity(51);
        window.setWindowAnimations(R.style.Animation_COUI_DialogListWindow);
        setFirstLayoutListener(window, new AnonymousClass1(window, point, view, point2));
    }
}