package com.coui.appcompat.tablayout;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.TextViewCompat;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.reddot.COUIHintRedDot;
import com.coui.appcompat.reddot.COUIHintRedDotMemento;
import com.coui.appcompat.state.COUIMaskEffectDrawable;
import com.coui.appcompat.state.COUIStateEffectDrawable;
import com.coui.appcompat.textutil.COUIChangeTextUtil;
import com.support.reddot.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUITabView extends LinearLayout {
    private COUITabLayout mCOUITabLayout;
    private ImageView mCustomIconView;
    private TextView mCustomTextView;
    protected View mCustomView;
    private int mDefaultMaxLines;
    private COUIHintRedDot mHintRedDot;
    private ImageView mIconView;
    private COUIMaskEffectDrawable mMaskEffectDrawable;
    private int mNoticeWithNumberDescriptionId;
    private boolean mSelectedByClick;
    private COUIStateEffectDrawable mStateEffectBackground;
    private COUITab mTab;
    private final RectF mTabRect;
    private TextView mTextFrame;

    public COUITabView(Context context, COUITabLayout cOUITabLayout) {
        super(context);
        this.mTabRect = new RectF();
        this.mDefaultMaxLines = 1;
        this.mCOUITabLayout = cOUITabLayout;
        if (cOUITabLayout.mTabBackgroundResId != 0) {
            ViewCompat.setBackground(this, ResourcesCompat.getDrawable(context.getResources(), this.mCOUITabLayout.mTabBackgroundResId, getContext().getTheme()));
        }
        ViewCompat.setPaddingRelative(this, this.mCOUITabLayout.getTabPaddingStart(), this.mCOUITabLayout.getTabPaddingTop(), this.mCOUITabLayout.getTabPaddingEnd(), this.mCOUITabLayout.getTabPaddingBottom());
        setGravity(17);
        setOrientation(0);
        setClickable(true);
        configStateEffectBackground();
        this.mNoticeWithNumberDescriptionId = R.plurals.red_dot_with_number_description;
    }

    public static /* synthetic */ void a(COUITabView cOUITabView) {
        COUITabLayout cOUITabLayout = cOUITabView.mCOUITabLayout;
        cOUITabLayout.mTabAlreadyMeasure = false;
        cOUITabLayout.mTabStrip.requestLayout();
    }

    private float approximateLineWidth(Layout layout, int i3, float f3) {
        return layout.getLineWidth(i3) * (f3 / layout.getPaint().getTextSize());
    }

    private void configStateEffectBackground() {
        setDefaultFocusHighlightEnabled(false);
        COUIMaskEffectDrawable cOUIMaskEffectDrawable = new COUIMaskEffectDrawable(getContext(), 1);
        this.mMaskEffectDrawable = cOUIMaskEffectDrawable;
        cOUIMaskEffectDrawable.setMaskRect(this.mTabRect, dpToPx(8), dpToPx(8));
        this.mMaskEffectDrawable.enableSelectedState(false);
        this.mMaskEffectDrawable.setMinProgressForTouchEnterAnimation(0.0f);
        COUIStateEffectDrawable cOUIStateEffectDrawable = new COUIStateEffectDrawable(new Drawable[]{getBackground() == null ? new ColorDrawable(0) : getBackground(), this.mMaskEffectDrawable});
        this.mStateEffectBackground = cOUIStateEffectDrawable;
        super.setBackground(cOUIStateEffectDrawable);
        COUIDarkModeUtil.setForceDarkAllow(this, false);
    }

    private static Integer stringToInteger(String str) {
        if (str != null && !str.trim().isEmpty()) {
            try {
                return Integer.valueOf(str.trim());
            } catch (Exception unused) {
            }
        }
        return null;
    }

    private void updateTextAndIcon(TextView textView, ImageView imageView) {
        COUITab cOUITab = this.mTab;
        Drawable icon = cOUITab != null ? cOUITab.getIcon() : null;
        COUITab cOUITab2 = this.mTab;
        CharSequence text = cOUITab2 != null ? cOUITab2.getText() : null;
        COUITab cOUITab3 = this.mTab;
        CharSequence contentDescription = cOUITab3 != null ? cOUITab3.getContentDescription() : null;
        int iDpToPx = 0;
        if (imageView != null) {
            if (icon != null) {
                imageView.setImageDrawable(icon);
                imageView.setVisibility(0);
                setVisibility(0);
            } else {
                imageView.setVisibility(8);
                imageView.setImageDrawable(null);
            }
            imageView.setContentDescription(contentDescription);
        }
        boolean zIsEmpty = TextUtils.isEmpty(text);
        if (textView != null) {
            if (zIsEmpty) {
                textView.setVisibility(8);
                textView.setText((CharSequence) null);
            } else {
                CharSequence text2 = textView.getText();
                textView.setText(text);
                textView.setVisibility(0);
                COUITabLayout cOUITabLayout = this.mCOUITabLayout;
                if (cOUITabLayout.mTabAlreadyMeasure) {
                    COUISlidingTabStrip cOUISlidingTabStrip = cOUITabLayout.mTabStrip;
                    if (cOUISlidingTabStrip != null) {
                        cOUITabLayout.mTabAlreadyMeasure = false;
                        cOUISlidingTabStrip.requestLayout();
                    }
                } else if (!text.equals(text2)) {
                    this.mCOUITabLayout.mTabStrip.post(new a(this));
                }
                textView.setMaxLines(this.mDefaultMaxLines);
                setVisibility(0);
            }
            textView.setContentDescription(contentDescription);
        }
        if (imageView != null) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
            if (!zIsEmpty && imageView.getVisibility() == 0) {
                iDpToPx = dpToPx(8);
            }
            if (iDpToPx != marginLayoutParams.bottomMargin) {
                marginLayoutParams.bottomMargin = iDpToPx;
                imageView.requestLayout();
            }
        }
        TooltipCompat.setTooltipText(this, zIsEmpty ? contentDescription : null);
    }

    public int dpToPx(int i3) {
        return Math.round(getResources().getDisplayMetrics().density * i3);
    }

    public COUIHintRedDot getHintRedDot() {
        return this.mHintRedDot;
    }

    public boolean getSelectedByClick() {
        return this.mSelectedByClick;
    }

    public COUITab getTab() {
        return this.mTab;
    }

    public TextView getTextView() {
        return this.mTextFrame;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(ActionBar.Tab.class.getName());
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        TextView textView;
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(ActionBar.Tab.class.getName());
        AccessibilityNodeInfoCompat accessibilityNodeInfoCompatWrap = AccessibilityNodeInfoCompat.wrap(accessibilityNodeInfo);
        accessibilityNodeInfoCompatWrap.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(0, 1, this.mTab.getPosition(), 1, false, isSelected()));
        if (isSelected()) {
            accessibilityNodeInfoCompatWrap.setClickable(false);
            accessibilityNodeInfoCompatWrap.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK);
        }
        CharSequence contentDescription = this.mTab.getContentDescription();
        if (this.mCustomView == null && ((contentDescription == null || contentDescription == "") && (textView = this.mTextFrame) != null)) {
            contentDescription = textView.getText();
        }
        if (this.mHintRedDot.getPointMode() == 1 || this.mHintRedDot.getPointMode() == 4) {
            contentDescription = ((Object) contentDescription) + "," + getResources().getString(R.string.red_dot_description);
        }
        if (this.mHintRedDot.getPointMode() == 2 || this.mHintRedDot.getPointMode() == 5) {
            String quantityString = "," + getResources().getString(R.string.red_dot_description);
            Integer numStringToInteger = stringToInteger(this.mHintRedDot.getPointText());
            if (numStringToInteger != null) {
                quantityString = getResources().getQuantityString(this.mNoticeWithNumberDescriptionId, numStringToInteger.intValue(), numStringToInteger);
            }
            contentDescription = ((Object) contentDescription) + "," + quantityString;
        }
        accessibilityNodeInfo.setContentDescription(contentDescription);
        accessibilityNodeInfoCompatWrap.setRoleDescription(getResources().getString(com.google.android.material.R.string.item_view_role_description));
        if (isSelected()) {
            return;
        }
        accessibilityNodeInfo.setStateDescription(getContext().getResources().getString(com.support.appcompat.R.string.coui_accessibility_unselected));
    }

    @Override // android.view.View
    public void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        this.mTabRect.set(0.0f, 0.0f, i3, i4);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (isEnabled()) {
            if (motionEvent.getAction() == 0) {
                COUITabLayout cOUITabLayout = this.mCOUITabLayout;
                if (cOUITabLayout.mEnableVibrator) {
                    COUITab cOUITab = cOUITabLayout.mSelectedTab;
                    if (cOUITab != null && cOUITab.mView != this) {
                        performHapticFeedback(302);
                    }
                    this.mStateEffectBackground.setTouched(true);
                }
            }
            if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                this.mStateEffectBackground.setTouched(false);
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean performClick() {
        boolean zPerformClick = super.performClick();
        if (this.mTab == null) {
            return zPerformClick;
        }
        if (!zPerformClick) {
            playSoundEffect(0);
        }
        this.mCOUITabLayout.mNeedAdjust = false;
        this.mSelectedByClick = true;
        this.mTab.select();
        this.mSelectedByClick = false;
        return true;
    }

    public void refresh() {
        COUIStateEffectDrawable cOUIStateEffectDrawable = this.mStateEffectBackground;
        if (cOUIStateEffectDrawable != null) {
            cOUIStateEffectDrawable.refresh(getContext());
        }
    }

    public void reset() {
        setTab(null);
        setSelected(false);
    }

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        COUIStateEffectDrawable cOUIStateEffectDrawable = this.mStateEffectBackground;
        if (cOUIStateEffectDrawable == null) {
            super.setBackground(drawable);
        } else if (drawable == null) {
            cOUIStateEffectDrawable.setViewBackground(new ColorDrawable(0));
        } else {
            cOUIStateEffectDrawable.setViewBackground(drawable);
        }
    }

    @Override // android.view.View
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        TextView textView = this.mTextFrame;
        if (textView != null) {
            textView.setEnabled(z2);
        }
        ImageView imageView = this.mIconView;
        if (imageView != null) {
            imageView.setEnabled(z2);
        }
        View view = this.mCustomView;
        if (view != null) {
            view.setEnabled(z2);
        }
    }

    @Override // android.view.View
    public void setSelected(boolean z2) {
        TextView textView;
        boolean z3 = isSelected() != z2;
        super.setSelected(z2);
        if (z3 && (textView = this.mTextFrame) != null) {
            if (z2) {
                textView.setTypeface(this.mCOUITabLayout.mSelectedTypeface);
            } else {
                textView.setTypeface(this.mCOUITabLayout.mNormalTypeface);
            }
        }
        TextView textView2 = this.mTextFrame;
        if (textView2 != null) {
            COUIDarkModeUtil.setForceDarkAllow(textView2, !z2);
        }
        TextView textView3 = this.mTextFrame;
        if (textView3 != null) {
            textView3.setSelected(z2);
        }
        ImageView imageView = this.mIconView;
        if (imageView != null) {
            imageView.setSelected(z2);
        }
        View view = this.mCustomView;
        if (view != null) {
            view.setSelected(z2);
        }
    }

    public void setTab(COUITab cOUITab) {
        if (cOUITab != this.mTab) {
            this.mTab = cOUITab;
            update();
        }
    }

    public final void update() {
        COUITab cOUITab = this.mTab;
        COUIHintRedDotMemento cOUIHintRedDotMementoSaveMemento = null;
        View customView = cOUITab != null ? cOUITab.getCustomView() : null;
        boolean z2 = false;
        if (customView != null) {
            ViewParent parent = customView.getParent();
            if (parent != this) {
                if (parent != null) {
                    ((ViewGroup) parent).removeView(customView);
                }
                addView(customView, 0, new ViewGroup.LayoutParams(-2, -2));
            }
            this.mCustomView = customView;
            TextView textView = this.mTextFrame;
            if (textView != null) {
                textView.setVisibility(8);
            }
            ImageView imageView = this.mIconView;
            if (imageView != null) {
                imageView.setVisibility(8);
                this.mIconView.setImageDrawable(null);
            }
            TextView textView2 = (TextView) customView.findViewById(android.R.id.text1);
            this.mCustomTextView = textView2;
            if (textView2 != null) {
                this.mDefaultMaxLines = TextViewCompat.getMaxLines(textView2);
            }
            this.mCustomIconView = (ImageView) customView.findViewById(android.R.id.icon);
        } else {
            View view = this.mCustomView;
            if (view != null) {
                removeView(view);
                this.mCustomView = null;
            }
            this.mCustomTextView = null;
            this.mCustomIconView = null;
        }
        if (this.mCustomView == null) {
            if (this.mIconView == null) {
                ImageView imageView2 = (ImageView) LayoutInflater.from(getContext()).inflate(com.support.tablayout.R.layout.coui_tab_layout_icon, (ViewGroup) this, false);
                addView(imageView2, 0);
                this.mIconView = imageView2;
            }
            if (this.mTextFrame == null) {
                TextView textView3 = (TextView) LayoutInflater.from(getContext()).inflate(com.support.tablayout.R.layout.coui_tab_layout_text, (ViewGroup) this, false);
                this.mTextFrame = textView3;
                addView(textView3);
                TextView textView4 = this.mTextFrame;
                COUITabLayout cOUITabLayout = this.mCOUITabLayout;
                ViewCompat.setPaddingRelative(textView4, cOUITabLayout.mTabPaddingStart, cOUITabLayout.mTabPaddingTop, cOUITabLayout.mTabPaddingEnd, cOUITabLayout.mTabPaddingBottom);
                this.mDefaultMaxLines = TextViewCompat.getMaxLines(this.mTextFrame);
                COUIChangeTextUtil.adaptBoldAndMediumFont(this.mTextFrame, cOUITab != null && cOUITab.isSelected());
            }
            COUIHintRedDot cOUIHintRedDot = this.mHintRedDot;
            if (cOUIHintRedDot != null) {
                cOUIHintRedDotMementoSaveMemento = cOUIHintRedDot.saveMemento();
                removeView(this.mHintRedDot);
            }
            this.mHintRedDot = new COUIHintRedDot(getContext());
            this.mHintRedDot.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
            addView(this.mHintRedDot);
            if (cOUIHintRedDotMementoSaveMemento != null) {
                cOUIHintRedDotMementoSaveMemento.applyTo(this.mHintRedDot);
            }
            this.mTextFrame.setTextSize(0, this.mCOUITabLayout.getTabTextSize());
            if (cOUITab == null || !cOUITab.isSelected()) {
                this.mTextFrame.setTypeface(this.mCOUITabLayout.mNormalTypeface);
            } else {
                this.mTextFrame.setTypeface(this.mCOUITabLayout.mSelectedTypeface);
            }
            this.mTextFrame.setIncludeFontPadding(false);
            ColorStateList colorStateList = this.mCOUITabLayout.mTabTextColors;
            if (colorStateList != null) {
                this.mTextFrame.setTextColor(colorStateList);
            }
            updateTextAndIcon(this.mTextFrame, this.mIconView);
        } else {
            if (this.mTextFrame == null) {
                this.mTextFrame = (TextView) LayoutInflater.from(getContext()).inflate(com.support.tablayout.R.layout.coui_tab_layout_text, (ViewGroup) this, false);
            }
            TextView textView5 = this.mCustomTextView;
            if (textView5 != null || this.mCustomIconView != null) {
                updateTextAndIcon(textView5, this.mCustomIconView);
            }
        }
        if (cOUITab != null && cOUITab.isSelected()) {
            z2 = true;
        }
        setSelected(z2);
    }
}