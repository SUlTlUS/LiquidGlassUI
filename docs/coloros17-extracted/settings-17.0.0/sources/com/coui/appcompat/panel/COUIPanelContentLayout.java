package com.coui.appcompat.panel;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import com.coui.appcompat.buttonBar.COUIButtonBarLayout;
import com.coui.appcompat.poplist.PopupMenuConfigRule;
import com.coui.appcompat.uiutil.UIUtil;
import com.support.panel.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIPanelContentLayout extends LinearLayout implements PopupMenuConfigRule {
    private static final Rect PANEL_OUTSETS = new Rect();
    private COUIPanelPressHelper mCOUIPanelPressHelper;
    private Rect mDisplayFrame;
    private boolean mIsLayoutAtMaxHeight;
    public boolean mIsTurnOnAnim;
    private int mPaddingBottomTemp;
    private final int mPanelHorizontalPadding;
    private boolean mPopupRuleEnable;
    private boolean mPopupVerticalRuleEnable;
    private Rect mWindowFrame;

    public COUIPanelContentLayout(Context context) {
        this(context, null);
    }

    public static /* synthetic */ boolean a(COUIPanelContentLayout cOUIPanelContentLayout, View view, boolean z2, View view2, MotionEvent motionEvent) {
        cOUIPanelContentLayout.getClass();
        if (motionEvent.getAction() == 0) {
            if (view != null) {
                view.setVisibility(0);
            }
            if (z2) {
                cOUIPanelContentLayout.mIsTurnOnAnim = true;
                cOUIPanelContentLayout.mCOUIPanelPressHelper.startAnim(view);
            }
        }
        return true;
    }

    private int getNavigationBarHeight(WindowInsets windowInsets, Configuration configuration) {
        if (windowInsets != null) {
            return windowInsets.getInsets(WindowInsets.Type.navigationBars()).bottom;
        }
        int identifier = getContext().getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        return configuration != null ? getContext().createConfigurationContext(configuration).getResources().getDimensionPixelSize(identifier) : getContext().getResources().getDimensionPixelSize(identifier);
    }

    private void initButton(Button button, String str, View.OnClickListener onClickListener) {
        if (button != null) {
            if (TextUtils.isEmpty(str)) {
                button.setVisibility(8);
                return;
            }
            button.setVisibility(0);
            button.setText(str);
            button.setOnClickListener(onClickListener);
        }
    }

    private void setSpecifyViewPaddingButton(boolean z2, int i3) {
        View viewFindViewById = getRootView().findViewById(R.id.coui_need_set_paddingbottom_id);
        if (viewFindViewById != null) {
            if (!z2) {
                if (this.mPaddingBottomTemp != -1) {
                    viewFindViewById.setPadding(viewFindViewById.getPaddingStart(), viewFindViewById.getPaddingTop(), viewFindViewById.getPaddingEnd(), this.mPaddingBottomTemp);
                    this.mPaddingBottomTemp = -1;
                    return;
                }
                return;
            }
            if (i3 > 0) {
                if (this.mPaddingBottomTemp == -1) {
                    this.mPaddingBottomTemp = viewFindViewById.getPaddingBottom();
                    viewFindViewById.setPadding(viewFindViewById.getPaddingStart(), viewFindViewById.getPaddingTop(), viewFindViewById.getPaddingEnd(), i3 + this.mPaddingBottomTemp);
                    return;
                }
                return;
            }
            if (this.mPaddingBottomTemp != -1) {
                viewFindViewById.setPadding(viewFindViewById.getPaddingStart(), viewFindViewById.getPaddingTop(), viewFindViewById.getPaddingEnd(), this.mPaddingBottomTemp);
                this.mPaddingBottomTemp = -1;
            }
        }
    }

    public void addContentView(View view) {
        LinearLayout linearLayout;
        if (view == null || (linearLayout = (LinearLayout) findViewById(R.id.panel_content)) == null) {
            return;
        }
        linearLayout.setClipChildren(false);
        linearLayout.addView(view, new LinearLayout.LayoutParams(-1, -1));
    }

    public void dragBgEndAnim() {
        this.mCOUIPanelPressHelper.endAnim(findViewById(R.id.tv_drag_press_bg));
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public int getBarrierDirection() {
        return getDisplayFrame().isEmpty() ? -1 : 4;
    }

    public COUIButtonBarLayout getBtnBarLayout() {
        return (COUIButtonBarLayout) findViewById(R.id.bottom_bar);
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public Rect getDisplayFrame() {
        if (this.mDisplayFrame == null) {
            this.mDisplayFrame = new Rect();
        }
        getGlobalVisibleRect(this.mDisplayFrame);
        Rect rect = this.mDisplayFrame;
        int i3 = rect.left;
        int i4 = this.mPanelHorizontalPadding;
        rect.left = i3 + i4;
        rect.right -= i4;
        return rect;
    }

    public View getDivider() {
        return findViewById(R.id.divider_line);
    }

    public View getDragBgView() {
        return findViewById(R.id.tv_drag_press_bg);
    }

    public ImageView getDragView() {
        return (ImageView) findViewById(R.id.drag_img);
    }

    public FrameLayout getDrawLayout() {
        return (FrameLayout) findViewById(R.id.drag_layout);
    }

    public boolean getLayoutAtMaxHeight() {
        return this.mIsLayoutAtMaxHeight;
    }

    public int getMaxHeight() {
        return COUIPanelMultiWindowUtils.getPanelMaxHeight(getContext(), null);
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public Rect getOutsets() {
        return PANEL_OUTSETS;
    }

    public COUIPanelBarView getPanelBarView() {
        return (COUIPanelBarView) findViewById(R.id.panel_drag_bar);
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public boolean getPopupMenuRuleEnabled() {
        return this.mPopupRuleEnable;
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public boolean getPopupMenuVerticalRuleEnabled() {
        if (!this.mPopupVerticalRuleEnable) {
            return false;
        }
        if (this.mWindowFrame == null) {
            this.mWindowFrame = new Rect();
        }
        getGlobalVisibleRect(this.mWindowFrame);
        if (this.mWindowFrame.isEmpty()) {
            return false;
        }
        return this.mWindowFrame.height() > getContext().getResources().getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_min_window_height_to_apply_vertical_barrier);
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public int getType() {
        return 2;
    }

    public void refresh() {
        findViewById(R.id.tv_drag_press_bg).setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.coui_pannel_press_shadow_bg));
    }

    public void removeContentView() {
        ((LinearLayout) findViewById(R.id.panel_content)).removeAllViews();
    }

    public void removeDragViewPressAnim() {
        View viewFindViewById = findViewById(R.id.tv_drag_press_bg);
        if (viewFindViewById != null) {
            viewFindViewById.setOnTouchListener(null);
        }
    }

    public void setCenterButton(String str, View.OnClickListener onClickListener) {
        initButton((Button) findViewById(android.R.id.button3), str, onClickListener);
    }

    public void setDividerVisibility(boolean z2) {
        View viewFindViewById = findViewById(R.id.divider_line);
        if (z2) {
            viewFindViewById.setVisibility(0);
        } else {
            viewFindViewById.setVisibility(8);
        }
    }

    public void setDragViewDrawable(Drawable drawable) {
        if (drawable != null) {
            ((ImageView) findViewById(R.id.drag_img)).setImageDrawable(drawable);
        }
    }

    public void setDragViewDrawableTintColor(int i3) {
        ((AppCompatImageView) findViewById(R.id.drag_img)).getDrawable().setTint(i3);
    }

    public void setDragViewPressAnim(boolean z2) {
        View viewFindViewById = findViewById(R.id.tv_drag_press_bg);
        if (viewFindViewById != null) {
            viewFindViewById.setOnTouchListener(null);
            viewFindViewById.setOnTouchListener(new d(this, viewFindViewById, z2));
        }
    }

    public void setLayoutAtMaxHeight(boolean z2) {
        this.mIsLayoutAtMaxHeight = z2;
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            if (z2) {
                layoutParams.height = -1;
            } else {
                layoutParams.height = -2;
            }
            requestLayout();
        }
    }

    public void setLeftButton(String str, View.OnClickListener onClickListener) {
        initButton((Button) findViewById(android.R.id.button2), str, onClickListener);
    }

    @Deprecated
    public void setNavigationMargin(Configuration configuration, int i3, WindowInsets windowInsets) {
        setNavigationMargin(configuration, windowInsets, true, false);
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public void setPopupMenuRuleEnabled(boolean z2) {
        this.mPopupRuleEnable = z2;
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public void setPopupMenuVerticalRuleEnabled(boolean z2) {
        this.mPopupVerticalRuleEnable = z2;
    }

    public void setRightButton(String str, View.OnClickListener onClickListener) {
        initButton((Button) findViewById(android.R.id.button1), str, onClickListener);
    }

    public void setUpBottomBar(boolean z2, String str, View.OnClickListener onClickListener, String str2, View.OnClickListener onClickListener2, String str3, View.OnClickListener onClickListener3) {
        setDividerVisibility(z2);
        COUIButtonBarLayout cOUIButtonBarLayout = (COUIButtonBarLayout) findViewById(R.id.bottom_bar);
        if (TextUtils.isEmpty(str) && TextUtils.isEmpty(str2) && TextUtils.isEmpty(str3)) {
            cOUIButtonBarLayout.setVisibility(8);
            return;
        }
        cOUIButtonBarLayout.setVisibility(0);
        cOUIButtonBarLayout.setVerButDividerVerMargin(getContext().getResources().getDimensionPixelOffset(R.dimen.coui_panel_bottom_bar_padding_top));
        cOUIButtonBarLayout.setVerButVerPadding(getContext().getResources().getDimensionPixelOffset(R.dimen.coui_panel_bottom_button_vertical_padding));
        cOUIButtonBarLayout.setVerPaddingBottom(getContext().getResources().getDimensionPixelOffset(R.dimen.coui_panel_bottom_bar_padding_bottom));
        cOUIButtonBarLayout.setVerButPaddingOffset(0);
        Button button = (Button) findViewById(android.R.id.button2);
        Button button2 = (Button) findViewById(android.R.id.button3);
        Button button3 = (Button) findViewById(android.R.id.button1);
        initButton(button, str, onClickListener);
        initButton(button2, str2, onClickListener2);
        initButton(button3, str3, onClickListener3);
    }

    public COUIPanelContentLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    /* JADX WARN: Code duplicated, block: B:7:0x0043  */
    public void setNavigationMargin(Configuration configuration, WindowInsets windowInsets, boolean z2, boolean z3) {
        boolean zIsIsHandlePanel;
        boolean zIsDisplayInUpperWindow = COUIPanelMultiWindowUtils.isDisplayInUpperWindow(COUIPanelMultiWindowUtils.contextToActivity(getContext()));
        boolean zIsInMultiWindowMode = COUIPanelMultiWindowUtils.isInMultiWindowMode(COUIPanelMultiWindowUtils.contextToActivity(getContext()));
        boolean zIsSmallScreen = COUIPanelMultiWindowUtils.isSmallScreen(getContext(), null);
        int navigationBarHeight = getNavigationBarHeight(windowInsets, configuration);
        View viewFindViewById = getRootView().findViewById(R.id.coordinator);
        if (viewFindViewById != null) {
            View viewFindViewById2 = viewFindViewById.findViewById(com.support.appcompat.R.id.design_bottom_sheet);
            if (viewFindViewById2 instanceof COUIPanelPercentFrameLayout) {
                zIsIsHandlePanel = ((COUIPanelPercentFrameLayout) viewFindViewById2).isIsHandlePanel();
            } else {
                zIsIsHandlePanel = false;
            }
        } else {
            zIsIsHandlePanel = false;
        }
        if ((!zIsDisplayInUpperWindow || !zIsInMultiWindowMode) && !zIsSmallScreen && !zIsIsHandlePanel) {
            navigationBarHeight = 0;
        }
        if (COUINavigationBarUtil.isGestureNavigation(getContext()) || UIUtil.isInFreeFormModeWindowMode(getContext()) || z3) {
            setSpecifyViewPaddingButton(z2, navigationBarHeight);
        } else if (this.mPaddingBottomTemp != -1) {
            setSpecifyViewPaddingButton(z2, 0);
        }
    }

    public COUIPanelContentLayout(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.mPopupRuleEnable = true;
        this.mPopupVerticalRuleEnable = true;
        this.mWindowFrame = null;
        this.mCOUIPanelPressHelper = new COUIPanelPressHelper();
        this.mPanelHorizontalPadding = context.getResources().getDimensionPixelOffset(R.dimen.coui_bottom_sheet_content_horizontal_padding_with_card);
        this.mPaddingBottomTemp = -1;
    }
}