package com.coui.appcompat.toolbar;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.widget.TooltipCompat;
import com.coui.appcompat.COUIMaterialBlurEffect;
import com.coui.appcompat.accessibilityutil.COUIAccessibilityUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.state.COUIMaskRippleDrawable;
import com.coui.appcompat.textviewcompatutil.COUITextViewCompatUtil;
import com.support.toolbar.R;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes7.dex */
public class COUIActionMenuItemView extends ActionMenuItemView {
    private static final String TAG = "COUIActionMenuItem";
    private ToolbarMaterialEffectDelegate mEffectDelegate;
    private int mIconMenuMinHeight;
    private int mIconMenuMinWidth;
    private boolean mIsText;
    private boolean mItemIsDrawableMaterialEffect;
    private int mItemNonBlurFallbackColor;
    private View.OnTouchListener mItemOnTouchListener;
    private int mItemUseMaterialEffectAttr;
    private int mMarginEnd;
    private COUIMaskRippleDrawable mMaskRippleDrawable;
    private boolean mMaterialEffectEnabled;
    private boolean mMaterialEffectOsSupported;
    private int mMaterialType;
    private int mPaddingHorizontal;
    private int mPaddingVertical;
    private int mTextMenuItemMaxWidth;
    private int mTextMenuItemMinWidth;
    private int mTextPaddingHorizontal;
    private int mTextPaddingVertical;

    public COUIActionMenuItemView(Context context) {
        this(context, null);
    }

    private void configMenuIconBackground() {
        COUIMaskRippleDrawable cOUIMaskRippleDrawable = new COUIMaskRippleDrawable(getContext());
        this.mMaskRippleDrawable = cOUIMaskRippleDrawable;
        cOUIMaskRippleDrawable.setCircleRippleMask(COUIMaskRippleDrawable.getMaskRippleRadiusByType(getContext(), 0));
        setBackground(this.mMaskRippleDrawable);
        COUIDarkModeUtil.setForceDarkAllow(this, false);
    }

    private void ensureEffectDelegate() {
        if (this.mEffectDelegate == null && hasVisualContent(getItemData())) {
            this.mEffectDelegate = ToolbarMaterialEffectDelegate.forMenuItem(this);
            if (this.mItemUseMaterialEffectAttr != COUIToolbar.MaterialEffectState.FOLLOW_SYSTEM.getValue()) {
                this.mEffectDelegate.setUseMaterialEffectAttr(this.mItemUseMaterialEffectAttr);
            }
            if (this.mItemIsDrawableMaterialEffect) {
                this.mEffectDelegate.setIsDrawableMaterialEffect(true);
            }
            int i3 = this.mItemNonBlurFallbackColor;
            if (i3 != 0) {
                this.mEffectDelegate.setCustomNonBlurFallbackColor(i3);
            }
            this.mEffectDelegate.setMaterialEffectType(this.mMaterialType);
            View.OnTouchListener onTouchListener = this.mItemOnTouchListener;
            if (onTouchListener != null) {
                this.mEffectDelegate.setItemOnTouchListener(onTouchListener);
            }
        }
    }

    private boolean hasVisualContent(MenuItemImpl menuItemImpl) {
        return (menuItemImpl != null && menuItemImpl.getIcon() != null) || (TextUtils.getTrimmedLength(getText()) > 0 || (menuItemImpl != null && !TextUtils.isEmpty(menuItemImpl.getTitle())));
    }

    private void setReflectField(Class cls, Object obj, String str, Object obj2) {
        try {
            Field declaredField = cls.getDeclaredField(str);
            declaredField.setAccessible(true);
            declaredField.set(obj, obj2);
        } catch (Exception e3) {
            Log.e(TAG, "setReflectField error: " + e3.getMessage());
        }
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate = this.mEffectDelegate;
        if (toolbarMaterialEffectDelegate != null) {
            toolbarMaterialEffectDelegate.drawSpotLight(canvas);
        }
    }

    @Override // androidx.appcompat.view.menu.ActionMenuItemView, android.widget.TextView, android.view.View
    public CharSequence getAccessibilityClassName() {
        return COUIAccessibilityUtil.BUTTON_CLASS_NAME;
    }

    public ToolbarMaterialEffectDelegate getEffectDelegate() {
        return this.mEffectDelegate;
    }

    @Override // androidx.appcompat.view.menu.ActionMenuItemView, androidx.appcompat.view.menu.MenuView.ItemView
    public void initialize(MenuItemImpl menuItemImpl, int i3) {
        setReflectField(ActionMenuItemView.class, this, "mMaxIconSize", Integer.valueOf(this.mIconMenuMinWidth));
        super.initialize(menuItemImpl, i3);
        boolean z2 = menuItemImpl.getIcon() == null;
        this.mIsText = z2;
        if (z2) {
            setMaxWidth(this.mTextMenuItemMaxWidth);
            if (this.mMaterialEffectOsSupported) {
                setReflectField(ActionMenuItemView.class, this, "mMinWidth", Integer.valueOf(this.mTextMenuItemMinWidth));
                int i4 = this.mTextPaddingHorizontal;
                setPadding(i4, 0, i4, 0);
            } else {
                COUITextViewCompatUtil.setPressRippleDrawable(this);
            }
        } else {
            setReflectField(ActionMenuItemView.class, this, "mMinWidth", Integer.valueOf(this.mIconMenuMinWidth));
            int i5 = this.mPaddingHorizontal;
            int i6 = this.mPaddingVertical;
            setPadding(i5, i6, i5, i6);
            if (!this.mMaterialEffectOsSupported) {
                configMenuIconBackground();
            }
        }
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (this.mMaterialEffectOsSupported) {
            setGravity(17);
            ensureEffectDelegate();
        } else {
            layoutParams.height = this.mIsText ? -2 : -1;
        }
        if (!this.mIsText && (layoutParams instanceof ViewGroup.MarginLayoutParams)) {
            ((ViewGroup.MarginLayoutParams) layoutParams).setMarginEnd(this.mMarginEnd);
        }
        setLayoutParams(layoutParams);
    }

    public boolean isTextMenuItem() {
        return this.mIsText;
    }

    @Override // android.widget.TextView, android.view.View
    public void jumpDrawablesToCurrentState() {
    }

    @Override // androidx.appcompat.view.menu.ActionMenuItemView, android.widget.TextView, android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        MenuItemImpl itemData = getItemData();
        if (itemData == null || itemData.getIcon() == null) {
            return;
        }
        this.mMarginEnd = getContext().createConfigurationContext(configuration).getResources().getDimensionPixelSize(R.dimen.coui_action_menu_item_view_margin_end);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) layoutParams).setMarginEnd(this.mMarginEnd);
        }
        TooltipCompat.setTooltipText(this, null);
    }

    @Override // androidx.appcompat.view.menu.ActionMenuItemView, androidx.appcompat.widget.AppCompatTextView, android.widget.TextView, android.view.View
    public void onMeasure(int i3, int i4) {
        super.onMeasure(i3, i4);
        if (this.mMaterialEffectOsSupported) {
            int mode = View.MeasureSpec.getMode(i4);
            int size = View.MeasureSpec.getSize(i4);
            int measuredHeight = getMeasuredHeight();
            int iMin = mode == Integer.MIN_VALUE ? Math.min(size, this.mIconMenuMinHeight) : this.mIconMenuMinHeight;
            if (mode == 1073741824 || this.mIconMenuMinHeight <= 0 || measuredHeight >= iMin) {
                return;
            }
            setMeasuredDimension(getMeasuredWidth(), iMin);
        }
    }

    public void refresh() {
        COUIMaskRippleDrawable cOUIMaskRippleDrawable = this.mMaskRippleDrawable;
        if (cOUIMaskRippleDrawable != null) {
            cOUIMaskRippleDrawable.refresh(getContext());
        }
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate = this.mEffectDelegate;
        if (toolbarMaterialEffectDelegate != null) {
            toolbarMaterialEffectDelegate.refreshRipple(getContext());
        }
    }

    @Override // androidx.appcompat.view.menu.ActionMenuItemView, androidx.appcompat.view.menu.MenuView.ItemView
    public void setIcon(Drawable drawable) {
        super.setIcon(drawable);
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate = this.mEffectDelegate;
        if (toolbarMaterialEffectDelegate != null) {
            toolbarMaterialEffectDelegate.setEnabled(hasVisualContent(getItemData()));
        }
        TooltipCompat.setTooltipText(this, null);
    }

    @Deprecated
    public void setIfInBlurWindow(boolean z2) {
        setItemIsDrawableMaterialEffect(true);
        setItemUseMaterialEffectAttr(z2 ? COUIToolbar.MaterialEffectState.FORCE_ENABLE.getValue() : COUIToolbar.MaterialEffectState.FORCE_DISABLE.getValue());
    }

    public void setItemBlurParams(COUIMaterialBlurEffect.BaseBlurEffectParams baseBlurEffectParams) {
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate = this.mEffectDelegate;
        if (toolbarMaterialEffectDelegate != null) {
            toolbarMaterialEffectDelegate.setCustomBlurParams(baseBlurEffectParams);
        }
    }

    public void setItemIsDrawableMaterialEffect(boolean z2) {
        this.mItemIsDrawableMaterialEffect = z2;
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate = this.mEffectDelegate;
        if (toolbarMaterialEffectDelegate != null) {
            toolbarMaterialEffectDelegate.setIsDrawableMaterialEffect(z2);
        }
    }

    public void setItemMaterialEffectType(int i3) {
        if (this.mMaterialType == i3) {
            return;
        }
        this.mMaterialType = i3;
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate = this.mEffectDelegate;
        if (toolbarMaterialEffectDelegate != null) {
            toolbarMaterialEffectDelegate.setMaterialEffectType(i3);
        }
    }

    public void setItemNonBlurFallbackColor(int i3) {
        if (this.mItemNonBlurFallbackColor == i3) {
            return;
        }
        this.mItemNonBlurFallbackColor = i3;
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate = this.mEffectDelegate;
        if (toolbarMaterialEffectDelegate != null) {
            toolbarMaterialEffectDelegate.setCustomNonBlurFallbackColor(i3);
        }
    }

    public void setItemOnTouchListener(View.OnTouchListener onTouchListener) {
        this.mItemOnTouchListener = onTouchListener;
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate = this.mEffectDelegate;
        if (toolbarMaterialEffectDelegate != null) {
            toolbarMaterialEffectDelegate.setItemOnTouchListener(onTouchListener);
        }
    }

    public void setItemUseMaterialEffectAttr(int i3) {
        if (this.mItemUseMaterialEffectAttr == i3) {
            return;
        }
        this.mItemUseMaterialEffectAttr = i3;
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate = this.mEffectDelegate;
        if (toolbarMaterialEffectDelegate != null) {
            toolbarMaterialEffectDelegate.setUseMaterialEffectAttr(i3);
        }
    }

    public void setItemWithGap(boolean z2) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (!this.mIsText && (layoutParams instanceof ViewGroup.MarginLayoutParams)) {
            if (z2) {
                ((ViewGroup.MarginLayoutParams) layoutParams).setMarginEnd(this.mMarginEnd);
            } else {
                ((ViewGroup.MarginLayoutParams) layoutParams).setMarginEnd(0);
            }
        }
        setLayoutParams(layoutParams);
    }

    public void setMaterialEffectsEnabled(boolean z2) {
        if (this.mMaterialEffectEnabled != z2) {
            this.mMaterialEffectEnabled = z2;
            ensureEffectDelegate();
            ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate = this.mEffectDelegate;
            if (toolbarMaterialEffectDelegate != null) {
                toolbarMaterialEffectDelegate.setBlurOn(z2);
                this.mEffectDelegate.setStrokeOn(z2);
            }
        }
    }

    @Override // androidx.appcompat.view.menu.ActionMenuItemView, androidx.appcompat.view.menu.MenuView.ItemView
    public void setTitle(CharSequence charSequence) {
        super.setTitle(charSequence);
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate = this.mEffectDelegate;
        if (toolbarMaterialEffectDelegate != null) {
            toolbarMaterialEffectDelegate.setEnabled(hasVisualContent(getItemData()));
        }
        TooltipCompat.setTooltipText(this, null);
    }

    public COUIActionMenuItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIActionMenuItemView(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.mMaterialEffectEnabled = true;
        this.mItemIsDrawableMaterialEffect = false;
        this.mItemUseMaterialEffectAttr = COUIToolbar.MaterialEffectState.FOLLOW_SYSTEM.getValue();
        this.mItemNonBlurFallbackColor = 0;
        this.mMaterialType = 0;
        this.mMaterialEffectOsSupported = false;
        this.mPaddingHorizontal = context.getResources().getDimensionPixelSize(R.dimen.coui_toolbar_menu_bg_padding_horizontal);
        this.mPaddingVertical = context.getResources().getDimensionPixelSize(R.dimen.coui_toolbar_menu_bg_padding_vertical);
        this.mTextPaddingHorizontal = context.getResources().getDimensionPixelSize(R.dimen.coui_toolbar_text_menu_bg_padding_horizontal);
        this.mTextPaddingVertical = context.getResources().getDimensionPixelSize(R.dimen.coui_toolbar_text_menu_bg_padding_vertical);
        this.mMarginEnd = context.getResources().getDimensionPixelSize(R.dimen.coui_action_menu_item_view_margin_end);
        this.mIconMenuMinWidth = context.getResources().getDimensionPixelSize(R.dimen.coui_action_menu_item_min_width);
        this.mIconMenuMinHeight = context.getResources().getDimensionPixelSize(R.dimen.coui_action_menu_item_min_height);
        this.mTextMenuItemMinWidth = context.getResources().getDimensionPixelSize(R.dimen.coui_action_text_menu_item_min_width);
        this.mTextMenuItemMaxWidth = context.getResources().getDimensionPixelSize(R.dimen.coui_action_bar_text_menu_item_max_width);
        this.mMaterialEffectOsSupported = ToolbarMaterialHelper.isMaterialEffectOsSupported();
    }
}