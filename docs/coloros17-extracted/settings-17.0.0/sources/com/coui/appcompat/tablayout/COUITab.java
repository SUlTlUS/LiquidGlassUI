package com.coui.appcompat.tablayout;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.ResourcesCompat;
import com.coui.appcompat.reddot.COUIHintRedDot;

/* JADX INFO: loaded from: classes7.dex */
public class COUITab {
    public static final int INVALID_POSITION = -1;
    private CharSequence mContentDesc;
    private View mCustomView;
    private Drawable mIcon;
    COUITabLayout mParent;
    private Object mTag;
    private CharSequence mText;
    COUITabView mView;
    private int mPosition = -1;
    private boolean mIsBold = true;

    public CharSequence getContentDescription() {
        return this.mContentDesc;
    }

    public View getCustomView() {
        return this.mCustomView;
    }

    public Drawable getIcon() {
        return this.mIcon;
    }

    @Deprecated
    public int getPointMode() {
        COUITabView cOUITabView = this.mView;
        if (cOUITabView == null || cOUITabView.getHintRedDot() == null) {
            return 0;
        }
        return this.mView.getHintRedDot().getPointMode();
    }

    @Deprecated
    public int getPointNumber() {
        COUITabView cOUITabView = this.mView;
        if (cOUITabView == null || cOUITabView.getHintRedDot() == null) {
            return 0;
        }
        return this.mView.getHintRedDot().getPointNumber();
    }

    public int getPosition() {
        return this.mPosition;
    }

    public COUIHintRedDot getRedPoint() {
        COUITabView cOUITabView = this.mView;
        if (cOUITabView != null) {
            return cOUITabView.getHintRedDot();
        }
        return null;
    }

    public boolean getSelectedByClick() {
        COUITabView cOUITabView = this.mView;
        if (cOUITabView != null) {
            return cOUITabView.getSelectedByClick();
        }
        return false;
    }

    public Object getTag() {
        return this.mTag;
    }

    public CharSequence getText() {
        return this.mText;
    }

    public COUITabView getView() {
        return this.mView;
    }

    public boolean isBold() {
        return this.mIsBold;
    }

    public boolean isSelected() {
        COUITabLayout cOUITabLayout = this.mParent;
        if (cOUITabLayout != null) {
            return cOUITabLayout.getSelectedTabPosition() == this.mPosition;
        }
        throw new IllegalArgumentException("Tab not attached to a COUITabLayout");
    }

    public void reset() {
        this.mParent = null;
        this.mView = null;
        this.mTag = null;
        this.mIcon = null;
        this.mText = null;
        this.mContentDesc = null;
        this.mPosition = -1;
        this.mCustomView = null;
    }

    public void select() {
        COUITabLayout cOUITabLayout = this.mParent;
        if (cOUITabLayout == null) {
            throw new IllegalArgumentException("Tab not attached to a COUITabLayout");
        }
        cOUITabLayout.selectTab(this);
    }

    public void setBold(boolean z2) {
        this.mIsBold = z2;
    }

    public COUITab setContentDescription(int i3) {
        COUITabLayout cOUITabLayout = this.mParent;
        if (cOUITabLayout != null) {
            return setContentDescription(cOUITabLayout.getResources().getText(i3));
        }
        throw new IllegalArgumentException("Tab not attached to a COUITabLayout");
    }

    public COUITab setCustomView(View view) {
        this.mCustomView = view;
        return this;
    }

    public COUITab setIcon(Drawable drawable) {
        this.mIcon = drawable;
        updateView();
        return this;
    }

    @Deprecated
    public COUITab setPointMode(int i3) {
        COUITabView cOUITabView = this.mView;
        if (cOUITabView != null && cOUITabView.getHintRedDot() != null && i3 != this.mView.getHintRedDot().getPointMode()) {
            this.mView.getHintRedDot().setPointMode(i3);
        }
        return this;
    }

    @Deprecated
    public COUITab setPointNumber(int i3) {
        COUITabView cOUITabView = this.mView;
        if (cOUITabView != null && cOUITabView.getHintRedDot() != null && i3 != this.mView.getHintRedDot().getPointNumber()) {
            this.mView.getHintRedDot().setPointNumber(i3);
        }
        return this;
    }

    public void setPosition(int i3) {
        this.mPosition = i3;
    }

    public COUITab setTag(Object obj) {
        this.mTag = obj;
        return this;
    }

    public COUITab setText(CharSequence charSequence) {
        this.mText = charSequence;
        updateView();
        return this;
    }

    public COUITab updateTabView() {
        updateView();
        return this;
    }

    public void updateView() {
        COUITabView cOUITabView = this.mView;
        if (cOUITabView != null) {
            cOUITabView.update();
        }
    }

    public COUITab setCustomView(int i3) {
        COUITabLayout cOUITabLayout = this.mParent;
        if (cOUITabLayout == null) {
            throw new IllegalArgumentException("Tab not attached to a COUITabLayout");
        }
        this.mCustomView = LayoutInflater.from(cOUITabLayout.getContext()).inflate(i3, (ViewGroup) this.mParent, false);
        return this;
    }

    public COUITab setIcon(int i3) {
        COUITabLayout cOUITabLayout = this.mParent;
        if (cOUITabLayout != null) {
            return setIcon(ResourcesCompat.getDrawable(cOUITabLayout.getResources(), i3, null));
        }
        throw new IllegalArgumentException("Tab not attached to a TabLayout");
    }

    public COUITab setText(int i3) {
        COUITabLayout cOUITabLayout = this.mParent;
        if (cOUITabLayout != null) {
            return setText(cOUITabLayout.getResources().getText(i3));
        }
        throw new IllegalArgumentException("Tab not attached to a TabLayout");
    }

    public COUITab setContentDescription(CharSequence charSequence) {
        this.mContentDesc = charSequence;
        updateView();
        return this;
    }
}