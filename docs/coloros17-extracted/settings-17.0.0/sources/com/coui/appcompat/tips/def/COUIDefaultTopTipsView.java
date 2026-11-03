package com.coui.appcompat.tips.def;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.coui.appcompat.button.COUIButton;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.textviewcompatutil.COUITextViewCompatUtil;
import com.coui.appcompat.tips.COUIMarqueeTextView;
import com.coui.appcompat.uiutil.UIUtil;
import com.support.tips.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIDefaultTopTipsView extends ConstraintLayout implements IDefaultTopTips {
    public static final int ACTION_ID = 3;
    private static final float CENTER_BIAS = 0.5f;
    public static final int CLOSE_ID = 4;
    private static final int DUAL_BUTTON_COUNT = 2;
    private static final float HALF_SIZE = 2.0f;
    public static final int ICON_ID = 0;
    public static final int IGNORE_ID = 2;
    public static final int TITLE_ID = 1;
    private COUIButton mAction;
    private int mActionCloseGap;
    private int mAiPaymentDualBtnBottomMargin;
    private int mBtnEndMargin;
    private int mBtnTopMargin;
    private ImageView mClose;
    private View.OnClickListener mCloseBtnClickListener;
    private int mCloseEndMargin;
    private final ConstraintSet mConstraintSet;
    private COUIMarqueeTextView mContent;
    private int mContentEndMargin;
    private int mContentLines;
    private int mContentStartMargin;
    private int mContentTopMargin;
    private int mDualBtnMinHeight;
    private int mDualBtnMinWidth;
    private boolean mHasSetCloseDrawable;
    private int mHorButHorPadding;
    private int mIconLargeSize;
    private int mIconSmallSize;
    private int mIconStartMargin;
    private TextView mIgnore;
    private ImageView mImage;
    private boolean mIsSingleLineText;
    private boolean mIsTitleNeedMultiLine;
    private Space mLayoutFixSpace;
    private LayoutMode mLayoutMode;
    private int mMultiBtnBottomMargin;
    private boolean mNeedUpdateLayout;
    private View.OnClickListener mNegativeClickListener;
    private OnLinesChangedListener mOnLinesChangedListener;
    private ConstraintSet mOriginalConstraintSet;
    private View.OnClickListener mPositiveClickListener;
    private int mTextToBtnGap;
    private TextView mTitle;
    private int mTitleContentGap;
    private int mTitleIconTextGap;
    private int mTitleMinHeight;
    private COUIDefaultTopTips.TopTipsStyle mTopTipsStyle;

    public enum LayoutMode {
        DEFAULT,
        AI_PAYMENT
    }

    public COUIDefaultTopTipsView(Context context) {
        this(context, (AttributeSet) null);
    }

    public static /* synthetic */ void a(COUIDefaultTopTipsView cOUIDefaultTopTipsView, View view) {
        View.OnClickListener onClickListener = cOUIDefaultTopTipsView.mNegativeClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }

    private void applyAiPaymentSecondaryButtonStyle() {
        COUIButton ignoreButton = getIgnoreButton();
        if (ignoreButton == null) {
            return;
        }
        COUIButton.ButtonType buttonType = COUIButton.ButtonType.AI_PAYMENT_SECONDARY;
        ignoreButton.setButtonType(buttonType);
        ignoreButton.setIsDrawableMaterialEffect(true);
        ignoreButton.setUseMaterialEffectAttr(COUIButton.MaterialEffectState.FOLLOW_SYSTEM, buttonType);
        ignoreButton.setDrawableRadius(-1);
    }

    private void applyAiPaymentTextColors() {
        if (this.mTopTipsStyle == COUIDefaultTopTips.TopTipsStyle.AI_PAYMENT && isAiPaymentLayout()) {
            boolean zIsCOUIDarkThemeOrNightMode = COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(getContext());
            TextView textView = this.mTitle;
            if (textView != null) {
                textView.setTextColor(COUIContextUtil.getColor(getContext(), zIsCOUIDarkThemeOrNightMode ? R.color.coui_toptips_ai_payment_title_text_color_dark : R.color.coui_toptips_ai_payment_title_text_color_light));
            }
            COUIMarqueeTextView cOUIMarqueeTextView = this.mContent;
            if (cOUIMarqueeTextView != null) {
                cOUIMarqueeTextView.setTextColor(COUIContextUtil.getColor(getContext(), zIsCOUIDarkThemeOrNightMode ? R.color.coui_toptips_ai_payment_content_text_color_dark : R.color.coui_toptips_ai_payment_content_text_color_light));
            }
            if (this.mAction != null) {
                this.mAction.setTextColor(new ColorStateList(new int[][]{new int[]{-16842910}, new int[0]}, new int[]{COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorAiPaymentPrimaryTextDisabled), COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorAiPaymentPrimaryText)}));
            }
            COUIButton ignoreButton = getIgnoreButton();
            if (ignoreButton != null) {
                ignoreButton.setTextColor(UIUtil.getColorStateListCompatNoCache(getContext(), com.support.appcompat.R.color.coui_btn_ai_payment_secondary_text_color));
            }
        }
    }

    private void applyContentAreaStartConstraintsWithoutTitle() {
        if (!hasStartIcon()) {
            this.mConstraintSet.setVisibility(R.id.image, 8);
            ConstraintSet constraintSet = this.mConstraintSet;
            int i3 = R.id.content;
            constraintSet.connect(i3, 6, 0, 6);
            this.mConstraintSet.setMargin(i3, 6, this.mContentEndMargin);
            return;
        }
        ConstraintSet constraintSet2 = this.mConstraintSet;
        int i4 = R.id.image;
        constraintSet2.setVisibility(i4, 0);
        this.mConstraintSet.constrainWidth(i4, this.mIconSmallSize);
        this.mConstraintSet.constrainHeight(i4, this.mIconSmallSize);
        this.mConstraintSet.connect(i4, 6, 0, 6);
        this.mConstraintSet.setMargin(i4, 6, this.mIconStartMargin);
        ConstraintSet constraintSet3 = this.mConstraintSet;
        int i5 = R.id.content;
        constraintSet3.connect(i5, 6, i4, 7);
        this.mConstraintSet.setMargin(i5, 6, this.mContentStartMargin);
    }

    private void applyDefaultSecondaryButtonStyle() {
        COUIButton ignoreButton = getIgnoreButton();
        if (ignoreButton == null) {
            return;
        }
        COUIButton.MaterialEffectState materialEffectState = COUIButton.MaterialEffectState.FORCE_DISABLE;
        COUIButton.ButtonType buttonType = COUIButton.ButtonType.SECONDARY;
        ignoreButton.setUseMaterialEffectAttr(materialEffectState, buttonType);
        ignoreButton.setButtonType(buttonType);
        ignoreButton.setDrawableRadius(-1);
        ignoreButton.setMinimumHeight(0);
    }

    private void applyDualButtonStyles() {
        if (this.mTopTipsStyle == COUIDefaultTopTips.TopTipsStyle.AI_PAYMENT) {
            applyAiPaymentButtonStyle();
            applyAiPaymentSecondaryButtonStyle();
        } else {
            resetDefaultButtonStyle();
            applyDefaultSecondaryButtonStyle();
        }
        configureDualButtonSizing();
    }

    private void applyDualButtonsHorizontalConstraints() {
        ConstraintSet constraintSet = this.mConstraintSet;
        int i3 = R.id.ignore;
        constraintSet.clear(i3, 6);
        this.mConstraintSet.clear(i3, 7);
        this.mConstraintSet.clear(i3, 3);
        this.mConstraintSet.clear(i3, 4);
        ConstraintSet constraintSet2 = this.mConstraintSet;
        int i4 = R.id.action;
        constraintSet2.clear(i4, 6);
        this.mConstraintSet.clear(i4, 7);
        this.mConstraintSet.clear(i4, 3);
        this.mConstraintSet.clear(i4, 4);
        this.mConstraintSet.connect(i3, 6, 0, 6);
        this.mConstraintSet.setMargin(i3, 6, this.mContentEndMargin);
        this.mConstraintSet.connect(i4, 6, i3, 7);
        this.mConstraintSet.setMargin(i4, 6, this.mTextToBtnGap);
        this.mConstraintSet.connect(i3, 3, R.id.content, 4);
        this.mConstraintSet.setMargin(i3, 3, this.mBtnTopMargin);
        this.mConstraintSet.connect(i3, 4, 0, 4);
        this.mConstraintSet.setMargin(i3, 4, getDualButtonBottomMargin());
        this.mConstraintSet.connect(i4, 3, i3, 3);
        this.mConstraintSet.constrainWidth(i3, -2);
        this.mConstraintSet.constrainWidth(i4, -2);
        this.mConstraintSet.constrainedWidth(i3, false);
        this.mConstraintSet.constrainedWidth(i4, false);
    }

    private void applyDualButtonsVerticalConstraints() {
        ConstraintSet constraintSet = this.mConstraintSet;
        int i3 = R.id.ignore;
        constraintSet.clear(i3, 6);
        this.mConstraintSet.clear(i3, 7);
        this.mConstraintSet.clear(i3, 3);
        this.mConstraintSet.clear(i3, 4);
        ConstraintSet constraintSet2 = this.mConstraintSet;
        int i4 = R.id.action;
        constraintSet2.clear(i4, 6);
        this.mConstraintSet.clear(i4, 7);
        this.mConstraintSet.clear(i4, 3);
        this.mConstraintSet.clear(i4, 4);
        this.mConstraintSet.connect(i4, 3, R.id.content, 4);
        this.mConstraintSet.setMargin(i4, 3, this.mBtnTopMargin);
        this.mConstraintSet.connect(i4, 6, 0, 6);
        this.mConstraintSet.setMargin(i4, 6, this.mContentEndMargin);
        this.mConstraintSet.connect(i4, 7, 0, 7);
        this.mConstraintSet.setMargin(i4, 7, this.mContentEndMargin);
        this.mConstraintSet.constrainWidth(i4, 0);
        this.mConstraintSet.constrainedWidth(i4, true);
        this.mConstraintSet.connect(i3, 3, i4, 4);
        this.mConstraintSet.setMargin(i3, 3, this.mTextToBtnGap);
        this.mConstraintSet.connect(i3, 4, 0, 4);
        this.mConstraintSet.setMargin(i3, 4, getDualButtonBottomMargin());
        this.mConstraintSet.connect(i3, 6, 0, 6);
        this.mConstraintSet.setMargin(i3, 6, this.mContentEndMargin);
        this.mConstraintSet.connect(i3, 7, 0, 7);
        this.mConstraintSet.setMargin(i3, 7, this.mContentEndMargin);
        this.mConstraintSet.constrainWidth(i3, 0);
        this.mConstraintSet.constrainedWidth(i3, true);
    }

    private void applyLayoutConstraints() {
        if (!isAiPaymentLayout()) {
            if (hasTitle()) {
                changeDefaultLayoutWithTitle();
                return;
            } else {
                changeDefaultLayoutWithoutTitle();
                return;
            }
        }
        if (hasDualButtons()) {
            changeLayoutWithDualButtons(hasTitle());
            return;
        }
        clearDualButtonSizing();
        if (hasTitle()) {
            changeLayoutWithTitle();
        } else {
            changeLayoutWithoutTitle();
        }
    }

    private void applyLayoutModeStyles() {
        if (this.mLayoutMode == LayoutMode.DEFAULT) {
            COUITextViewCompatUtil.setPressRippleDrawable(this.mIgnore);
        } else if (this.mTopTipsStyle == COUIDefaultTopTips.TopTipsStyle.AI_PAYMENT) {
            applyAiPaymentButtonStyle();
            applyAiPaymentTextColors();
        }
    }

    private void applyLegacyIgnoreLinkStyle() {
        COUIButton ignoreButton = getIgnoreButton();
        if (ignoreButton == null) {
            return;
        }
        clearDualButtonSizing();
        COUIButton.MaterialEffectState materialEffectState = COUIButton.MaterialEffectState.FORCE_DISABLE;
        COUIButton.ButtonType buttonType = COUIButton.ButtonType.TRANSPARENT;
        ignoreButton.setUseMaterialEffectAttr(materialEffectState, buttonType);
        ignoreButton.setButtonType(buttonType);
        ignoreButton.setDrawableRadius(-1);
        ignoreButton.setTextColor(COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorPrimaryText));
        ignoreButton.setMinimumHeight(0);
        ignoreButton.setPadding(0, 0, 0, 0);
    }

    private void applyLegacyIgnoreVisibility() {
        if (!hasLegacyIgnoreLink()) {
            this.mConstraintSet.setVisibility(R.id.ignore, 8);
        } else {
            this.mConstraintSet.setVisibility(R.id.ignore, 0);
            applyLegacyIgnoreLinkStyle();
        }
    }

    private void applyTitleAreaStartIconConstraints() {
        if (!hasStartIcon()) {
            this.mConstraintSet.setVisibility(R.id.image, 8);
            ConstraintSet constraintSet = this.mConstraintSet;
            int i3 = R.id.title;
            constraintSet.connect(i3, 6, 0, 6);
            this.mConstraintSet.setMargin(i3, 6, this.mContentEndMargin);
            return;
        }
        ConstraintSet constraintSet2 = this.mConstraintSet;
        int i4 = R.id.image;
        constraintSet2.constrainWidth(i4, this.mIconLargeSize);
        this.mConstraintSet.constrainHeight(i4, this.mIconLargeSize);
        this.mConstraintSet.setVisibility(i4, 0);
        this.mConstraintSet.connect(i4, 6, 0, 6);
        this.mConstraintSet.setMargin(i4, 6, this.mIconStartMargin);
        ConstraintSet constraintSet3 = this.mConstraintSet;
        int i5 = R.id.title;
        constraintSet3.connect(i5, 6, i4, 7);
        this.mConstraintSet.setMargin(i5, 6, this.mTitleIconTextGap);
    }

    public static /* synthetic */ void b(COUIDefaultTopTipsView cOUIDefaultTopTipsView, View view) {
        View.OnClickListener onClickListener = cOUIDefaultTopTipsView.mCloseBtnClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }

    private void bindChildViews() {
        this.mImage = (ImageView) findViewById(R.id.image);
        this.mTitle = (TextView) findViewById(R.id.title);
        this.mContent = (COUIMarqueeTextView) findViewById(R.id.content);
        TextView textView = (TextView) findViewById(R.id.ignore);
        this.mIgnore = textView;
        textView.setOnClickListener(new i(this));
        COUIButton cOUIButton = (COUIButton) findViewById(R.id.action);
        this.mAction = cOUIButton;
        cOUIButton.setOnClickListener(new j(this));
        ImageView imageView = (ImageView) findViewById(R.id.close);
        this.mClose = imageView;
        imageView.setOnClickListener(new k(this));
    }

    public static /* synthetic */ void c(COUIDefaultTopTipsView cOUIDefaultTopTipsView, View view) {
        View.OnClickListener onClickListener = cOUIDefaultTopTipsView.mPositiveClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }

    private void changeDefaultLayoutWithTitle() {
        this.mConstraintSet.clone(this);
        this.mConstraintSet.setVisibility(R.id.ignore, 8);
        ConstraintSet constraintSet = this.mConstraintSet;
        int i3 = R.id.title;
        constraintSet.setVisibility(i3, 0);
        ConstraintSet constraintSet2 = this.mConstraintSet;
        int i4 = R.id.content;
        constraintSet2.setVisibility(i4, 0);
        ConstraintSet constraintSet3 = this.mConstraintSet;
        int i5 = R.id.action;
        constraintSet3.setVisibility(i5, TextUtils.isEmpty(this.mAction.getText()) ? 8 : 0);
        boolean z2 = hasIgnoreText() || this.mHasSetCloseDrawable;
        boolean zIsEmpty = TextUtils.isEmpty(this.mAction.getText());
        applyTitleAreaStartIconConstraints();
        if (isNeedDefaultMultiTextWithTitle()) {
            this.mConstraintSet.connect(i3, 3, 0, 3);
            this.mConstraintSet.setMargin(i3, 3, this.mContentTopMargin);
            if (z2) {
                this.mConstraintSet.connect(i3, 7, R.id.close, 6);
                this.mConstraintSet.setMargin(i3, 7, this.mTextToBtnGap);
            } else {
                this.mConstraintSet.connect(i3, 7, 0, 7);
                this.mConstraintSet.setMargin(i3, 7, this.mContentEndMargin);
            }
            if (z2) {
                ConstraintSet constraintSet4 = this.mConstraintSet;
                int i6 = R.id.close;
                constraintSet4.setVisibility(i6, 0);
                this.mConstraintSet.connect(i6, 7, 0, 7);
                this.mConstraintSet.setMargin(i6, 7, this.mBtnEndMargin);
                this.mConstraintSet.connect(i6, 3, i3, 3);
                this.mConstraintSet.setMargin(i6, 3, 0);
                this.mConstraintSet.connect(i6, 4, -1, 4);
            } else {
                this.mConstraintSet.setVisibility(R.id.close, 8);
            }
            if (hasStartIcon()) {
                if (this.mIsTitleNeedMultiLine && isTitleMultiLineInMultiLayout(z2)) {
                    ConstraintSet constraintSet5 = this.mConstraintSet;
                    int i7 = R.id.image;
                    constraintSet5.connect(i7, 3, i3, 3);
                    this.mConstraintSet.setMargin(i7, 3, 0);
                    this.mConstraintSet.connect(i7, 4, i3, 4);
                } else {
                    int iconTopForSingleTitleMultiContent = getIconTopForSingleTitleMultiContent(this.mIconLargeSize);
                    ConstraintSet constraintSet6 = this.mConstraintSet;
                    int i8 = R.id.image;
                    constraintSet6.connect(i8, 3, 0, 3);
                    this.mConstraintSet.setMargin(i8, 3, iconTopForSingleTitleMultiContent);
                    this.mConstraintSet.connect(i8, 4, -1, 4);
                }
            }
            this.mConstraintSet.connect(i4, 6, i3, 6);
            this.mConstraintSet.setMargin(i4, 6, 0);
            this.mConstraintSet.connect(i4, 3, i3, 4);
            this.mConstraintSet.setMargin(i4, 3, this.mTitleContentGap);
            this.mConstraintSet.connect(i4, 7, 0, 7);
            this.mConstraintSet.setMargin(i4, 7, this.mContentEndMargin);
            if (zIsEmpty) {
                this.mConstraintSet.connect(i4, 4, 0, 4);
            } else {
                this.mConstraintSet.connect(i4, 4, -1, 4);
            }
            if (zIsEmpty) {
                this.mConstraintSet.setVisibility(i5, 8);
            } else {
                this.mConstraintSet.setVisibility(i5, 0);
                this.mConstraintSet.setHorizontalBias(i5, 0.0f);
                this.mConstraintSet.setVerticalBias(i5, 0.0f);
                this.mConstraintSet.connect(i5, 6, i3, 6);
                this.mConstraintSet.setMargin(i5, 6, 0);
                this.mConstraintSet.connect(i5, 3, i4, 4);
                this.mConstraintSet.setMargin(i5, 3, this.mBtnTopMargin);
                this.mConstraintSet.connect(i5, 4, 0, 4);
                this.mConstraintSet.setMargin(i5, 4, this.mMultiBtnBottomMargin);
                this.mConstraintSet.connect(i5, 7, 0, 7);
                this.mConstraintSet.setMargin(i5, 7, this.mContentEndMargin);
                this.mConstraintSet.constrainedWidth(i5, true);
            }
        } else {
            this.mConstraintSet.connect(i3, 3, 0, 3);
            this.mConstraintSet.setMargin(i3, 3, this.mContentTopMargin);
            if (!zIsEmpty) {
                this.mConstraintSet.connect(i3, 7, i5, 6);
                this.mConstraintSet.setMargin(i3, 7, this.mTextToBtnGap);
            } else if (z2) {
                this.mConstraintSet.connect(i3, 7, R.id.close, 6);
                this.mConstraintSet.setMargin(i3, 7, this.mTextToBtnGap);
            } else {
                this.mConstraintSet.connect(i3, 7, 0, 7);
                this.mConstraintSet.setMargin(i3, 7, this.mContentEndMargin);
            }
            if (z2) {
                ConstraintSet constraintSet7 = this.mConstraintSet;
                int i9 = R.id.close;
                constraintSet7.setVisibility(i9, 0);
                this.mConstraintSet.connect(i9, 7, 0, 7);
                this.mConstraintSet.setMargin(i9, 7, this.mCloseEndMargin);
                this.mConstraintSet.connect(i9, 3, i3, 3);
                this.mConstraintSet.setMargin(i9, 3, 0);
                this.mConstraintSet.connect(i9, 4, i4, 4);
            } else {
                this.mConstraintSet.setVisibility(R.id.close, 8);
            }
            this.mConstraintSet.connect(i4, 6, i3, 6);
            this.mConstraintSet.setMargin(i4, 6, 0);
            this.mConstraintSet.connect(i4, 3, i3, 4);
            this.mConstraintSet.setMargin(i4, 3, this.mTitleContentGap);
            this.mConstraintSet.connect(i4, 4, 0, 4);
            if (!zIsEmpty) {
                this.mConstraintSet.connect(i4, 7, i5, 6);
                this.mConstraintSet.setMargin(i4, 7, this.mTextToBtnGap);
            } else if (z2) {
                this.mConstraintSet.connect(i4, 7, R.id.close, 6);
                this.mConstraintSet.setMargin(i4, 7, this.mTextToBtnGap);
            } else {
                this.mConstraintSet.connect(i4, 7, 0, 7);
                this.mConstraintSet.setMargin(i4, 7, this.mContentEndMargin);
            }
            if (hasStartIcon()) {
                ConstraintSet constraintSet8 = this.mConstraintSet;
                int i10 = R.id.image;
                constraintSet8.connect(i10, 3, i3, 3);
                this.mConstraintSet.setMargin(i10, 3, 0);
                this.mConstraintSet.connect(i10, 4, i4, 4);
            }
            if (zIsEmpty) {
                this.mConstraintSet.setVisibility(i5, 8);
            } else {
                this.mConstraintSet.setVisibility(i5, 0);
                this.mConstraintSet.clear(i5, 6);
                this.mConstraintSet.setVerticalBias(i5, 0.5f);
                this.mConstraintSet.constrainedWidth(i5, false);
                this.mConstraintSet.connect(i5, 3, i3, 3);
                this.mConstraintSet.connect(i5, 4, i4, 4);
                this.mConstraintSet.setMargin(i5, 3, 0);
                this.mConstraintSet.setMargin(i5, 4, 0);
                if (z2) {
                    this.mConstraintSet.connect(i5, 7, R.id.close, 6);
                    this.mConstraintSet.setMargin(i5, 7, this.mActionCloseGap);
                } else {
                    this.mConstraintSet.connect(i5, 7, 0, 7);
                    this.mConstraintSet.setMargin(i5, 7, this.mBtnEndMargin);
                }
            }
        }
        notifyContentLinesChanged();
        this.mConstraintSet.applyTo(this);
    }

    private void changeDefaultLayoutWithoutTitle() {
        this.mConstraintSet.clone(this);
        this.mConstraintSet.setVisibility(R.id.ignore, 8);
        this.mConstraintSet.setVisibility(R.id.title, 8);
        ConstraintSet constraintSet = this.mConstraintSet;
        int i3 = R.id.action;
        constraintSet.setVisibility(i3, TextUtils.isEmpty(this.mAction.getText()) ? 8 : 0);
        ConstraintSet constraintSet2 = this.mConstraintSet;
        int i4 = R.id.content;
        constraintSet2.connect(i4, 3, 0, 3);
        this.mConstraintSet.setMargin(i4, 3, this.mContentTopMargin);
        applyContentAreaStartConstraintsWithoutTitle();
        this.mConstraintSet.clear(i3, 6);
        boolean z2 = hasIgnoreText() || this.mHasSetCloseDrawable;
        boolean zIsEmpty = TextUtils.isEmpty(this.mAction.getText());
        if (isNeedDefaultMultiTextWithoutTitle()) {
            if (z2) {
                this.mConstraintSet.connect(i4, 7, R.id.close, 6);
                this.mConstraintSet.setMargin(i4, 7, this.mTextToBtnGap);
            } else {
                this.mConstraintSet.connect(i4, 7, 0, 7);
                this.mConstraintSet.setMargin(i4, 7, this.mContentEndMargin);
            }
            if (zIsEmpty) {
                this.mConstraintSet.connect(i4, 4, 0, 4);
            } else {
                this.mConstraintSet.connect(i4, 4, -1, 4);
            }
            if (zIsEmpty) {
                this.mConstraintSet.setVisibility(i3, 8);
            } else {
                this.mConstraintSet.setVisibility(i3, 0);
                this.mConstraintSet.setHorizontalBias(i3, 0.0f);
                this.mConstraintSet.setVerticalBias(i3, 0.0f);
                this.mConstraintSet.connect(i3, 6, i4, 6);
                this.mConstraintSet.connect(i3, 3, i4, 4);
                this.mConstraintSet.connect(i3, 4, 0, 4);
                this.mConstraintSet.connect(i3, 7, 0, 7);
                this.mConstraintSet.setMargin(i3, 3, this.mBtnTopMargin);
                this.mConstraintSet.setMargin(i3, 4, this.mMultiBtnBottomMargin);
                this.mConstraintSet.setMargin(i3, 7, this.mContentEndMargin);
                this.mConstraintSet.constrainedWidth(i3, true);
            }
            if (z2) {
                ConstraintSet constraintSet3 = this.mConstraintSet;
                int i5 = R.id.close;
                constraintSet3.setVisibility(i5, 0);
                this.mConstraintSet.connect(i5, 7, 0, 7);
                this.mConstraintSet.setMargin(i5, 7, this.mBtnEndMargin);
                this.mConstraintSet.connect(i5, 3, i4, 3);
                this.mConstraintSet.setMargin(i5, 3, getIconMarginAlignedToFirstTwoLines(this.mIconSmallSize));
                this.mConstraintSet.connect(i5, 4, -1, 4);
            } else {
                this.mConstraintSet.setVisibility(R.id.close, 8);
            }
            if (hasStartIcon()) {
                ConstraintSet constraintSet4 = this.mConstraintSet;
                int i6 = R.id.image;
                constraintSet4.connect(i6, 6, 0, 6);
                this.mConstraintSet.setMargin(i6, 6, this.mIconStartMargin);
                this.mConstraintSet.connect(i6, 3, i4, 3);
                this.mConstraintSet.setMargin(i6, 3, getIconMarginAlignedToFirstTwoLines(this.mIconSmallSize));
                this.mConstraintSet.connect(i6, 4, -1, 4);
            }
        } else {
            if (!zIsEmpty) {
                this.mConstraintSet.connect(i4, 7, i3, 6);
                this.mConstraintSet.setMargin(i4, 7, this.mTextToBtnGap);
            } else if (z2) {
                this.mConstraintSet.connect(i4, 7, R.id.close, 6);
                this.mConstraintSet.setMargin(i4, 7, this.mTextToBtnGap);
            } else {
                this.mConstraintSet.connect(i4, 7, 0, 7);
                this.mConstraintSet.setMargin(i4, 7, this.mContentEndMargin);
            }
            this.mConstraintSet.connect(i4, 4, 0, 4);
            if (zIsEmpty) {
                this.mConstraintSet.setVisibility(i3, 8);
            } else {
                this.mConstraintSet.setVisibility(i3, 0);
                this.mConstraintSet.setVerticalBias(i3, 0.5f);
                this.mConstraintSet.constrainedWidth(i3, false);
                this.mConstraintSet.connect(i3, 3, i4, 3);
                this.mConstraintSet.connect(i3, 4, i4, 4);
                this.mConstraintSet.setMargin(i3, 3, 0);
                this.mConstraintSet.setMargin(i3, 4, 0);
                if (z2) {
                    this.mConstraintSet.connect(i3, 7, R.id.close, 6);
                    this.mConstraintSet.setMargin(i3, 7, this.mActionCloseGap);
                } else {
                    this.mConstraintSet.connect(i3, 7, 0, 7);
                    this.mConstraintSet.setMargin(i3, 7, this.mIsSingleLineText ? this.mTextToBtnGap : this.mBtnEndMargin);
                }
            }
            if (z2) {
                ConstraintSet constraintSet5 = this.mConstraintSet;
                int i7 = R.id.close;
                constraintSet5.setVisibility(i7, 0);
                this.mConstraintSet.connect(i7, 7, 0, 7);
                this.mConstraintSet.setMargin(i7, 7, !zIsEmpty ? this.mCloseEndMargin : this.mBtnEndMargin);
                this.mConstraintSet.connect(i7, 3, i4, 3);
                this.mConstraintSet.setMargin(i7, 3, 0);
                this.mConstraintSet.connect(i7, 4, i4, 4);
            } else {
                this.mConstraintSet.setVisibility(R.id.close, 8);
            }
            if (hasStartIcon()) {
                ConstraintSet constraintSet6 = this.mConstraintSet;
                int i8 = R.id.image;
                constraintSet6.connect(i8, 3, i4, 3);
                this.mConstraintSet.connect(i8, 4, i4, 4);
                this.mConstraintSet.setMargin(i8, 3, 0);
            }
        }
        notifyContentLinesChanged();
        this.mConstraintSet.applyTo(this);
    }

    private void changeLayoutWithDualButtons(boolean z2) {
        this.mConstraintSet.clone(this);
        applyDualButtonStyles();
        this.mConstraintSet.setVisibility(R.id.close, 8);
        this.mConstraintSet.setVisibility(R.id.action, 0);
        this.mConstraintSet.setVisibility(R.id.ignore, 0);
        boolean zNeedDualButtonsVertical = needDualButtonsVertical(getMeasuredWidth());
        configureDualButtonTextLines();
        if (z2) {
            this.mConstraintSet.setVisibility(R.id.title, 0);
            this.mConstraintSet.setVisibility(R.id.content, 0);
            setupDualButtonsTextAreaWithTitle();
        } else {
            this.mConstraintSet.setVisibility(R.id.title, 8);
            this.mConstraintSet.setVisibility(R.id.content, 0);
            setupDualButtonsTextAreaWithoutTitle();
        }
        if (zNeedDualButtonsVertical) {
            applyDualButtonsVerticalConstraints();
        } else {
            applyDualButtonsHorizontalConstraints();
        }
        if (this.mOnLinesChangedListener != null) {
            int lineCount = this.mContent.getLineCount();
            this.mContentLines = lineCount;
            this.mOnLinesChangedListener.onLinesChanged(lineCount);
        }
        this.mIsSingleLineText = false;
        this.mConstraintSet.applyTo(this);
    }

    /* JADX WARN: Code duplicated, block: B:32:0x0101  */
    private void changeLayoutWithTitle() {
        this.mConstraintSet.clone(this);
        applyLegacyIgnoreVisibility();
        ConstraintSet constraintSet = this.mConstraintSet;
        int i3 = R.id.title;
        constraintSet.setVisibility(i3, 0);
        ConstraintSet constraintSet2 = this.mConstraintSet;
        int i4 = R.id.content;
        constraintSet2.setVisibility(i4, 0);
        ConstraintSet constraintSet3 = this.mConstraintSet;
        int i5 = R.id.action;
        constraintSet3.setVisibility(i5, TextUtils.isEmpty(this.mAction.getText()) ? 8 : 0);
        boolean zHasCloseIcon = hasCloseIcon();
        boolean zHasLegacyIgnoreLink = hasLegacyIgnoreLink();
        boolean zIsEmpty = TextUtils.isEmpty(this.mAction.getText());
        applyTitleAreaStartIconConstraints();
        if (isNeedMultiTextWithTitle()) {
            this.mConstraintSet.connect(i3, 3, 0, 3);
            this.mConstraintSet.setMargin(i3, 3, this.mContentTopMargin);
            if (zHasCloseIcon) {
                this.mConstraintSet.connect(i3, 7, R.id.close, 6);
                this.mConstraintSet.setMargin(i3, 7, this.mTextToBtnGap);
            } else if (zHasLegacyIgnoreLink) {
                this.mConstraintSet.connect(i3, 7, R.id.ignore, 6);
                this.mConstraintSet.setMargin(i3, 7, this.mTextToBtnGap);
            } else {
                this.mConstraintSet.connect(i3, 7, 0, 7);
                this.mConstraintSet.setMargin(i3, 7, this.mContentEndMargin);
            }
            if (zHasCloseIcon) {
                ConstraintSet constraintSet4 = this.mConstraintSet;
                int i6 = R.id.close;
                constraintSet4.setVisibility(i6, 0);
                this.mConstraintSet.connect(i6, 7, 0, 7);
                this.mConstraintSet.setMargin(i6, 7, this.mBtnEndMargin);
                this.mConstraintSet.connect(i6, 3, i3, 3);
                this.mConstraintSet.setMargin(i6, 3, 0);
                this.mConstraintSet.connect(i6, 4, -1, 4);
            } else {
                this.mConstraintSet.setVisibility(R.id.close, 8);
            }
            if (zHasLegacyIgnoreLink) {
                ConstraintSet constraintSet5 = this.mConstraintSet;
                int i7 = R.id.ignore;
                constraintSet5.connect(i7, 7, 0, 7);
                this.mConstraintSet.setMargin(i7, 7, this.mBtnEndMargin);
                this.mConstraintSet.connect(i7, 3, i3, 3);
                this.mConstraintSet.setMargin(i7, 3, 0);
                this.mConstraintSet.connect(i7, 4, -1, 4);
            }
            if (hasStartIcon()) {
                if (this.mIsTitleNeedMultiLine) {
                    if (isTitleMultiLineInMultiLayout(zHasCloseIcon || zHasLegacyIgnoreLink)) {
                        ConstraintSet constraintSet6 = this.mConstraintSet;
                        int i8 = R.id.image;
                        constraintSet6.connect(i8, 3, i3, 3);
                        this.mConstraintSet.setMargin(i8, 3, 0);
                        this.mConstraintSet.connect(i8, 4, i3, 4);
                    } else {
                        int iconTopForSingleTitleMultiContent = getIconTopForSingleTitleMultiContent(this.mIconLargeSize);
                        ConstraintSet constraintSet7 = this.mConstraintSet;
                        int i9 = R.id.image;
                        constraintSet7.connect(i9, 3, 0, 3);
                        this.mConstraintSet.setMargin(i9, 3, iconTopForSingleTitleMultiContent);
                        this.mConstraintSet.connect(i9, 4, -1, 4);
                    }
                } else {
                    int iconTopForSingleTitleMultiContent2 = getIconTopForSingleTitleMultiContent(this.mIconLargeSize);
                    ConstraintSet constraintSet8 = this.mConstraintSet;
                    int i10 = R.id.image;
                    constraintSet8.connect(i10, 3, 0, 3);
                    this.mConstraintSet.setMargin(i10, 3, iconTopForSingleTitleMultiContent2);
                    this.mConstraintSet.connect(i10, 4, -1, 4);
                }
            }
            this.mConstraintSet.connect(i4, 6, i3, 6);
            this.mConstraintSet.setMargin(i4, 6, 0);
            this.mConstraintSet.connect(i4, 3, i3, 4);
            this.mConstraintSet.setMargin(i4, 3, this.mTitleContentGap);
            this.mConstraintSet.connect(i4, 7, 0, 7);
            this.mConstraintSet.setMargin(i4, 7, this.mContentEndMargin);
            if (zIsEmpty) {
                this.mConstraintSet.connect(i4, 4, 0, 4);
            } else {
                this.mConstraintSet.connect(i4, 4, -1, 4);
            }
            if (zIsEmpty) {
                this.mConstraintSet.setVisibility(i5, 8);
            } else {
                this.mConstraintSet.setVisibility(i5, 0);
                this.mConstraintSet.setHorizontalBias(i5, 0.0f);
                this.mConstraintSet.setVerticalBias(i5, 0.0f);
                this.mConstraintSet.connect(i5, 6, i3, 6);
                this.mConstraintSet.setMargin(i5, 6, 0);
                this.mConstraintSet.connect(i5, 3, i4, 4);
                this.mConstraintSet.setMargin(i5, 3, this.mBtnTopMargin);
                this.mConstraintSet.connect(i5, 4, 0, 4);
                this.mConstraintSet.setMargin(i5, 4, this.mMultiBtnBottomMargin);
                this.mConstraintSet.connect(i5, 7, 0, 7);
                this.mConstraintSet.setMargin(i5, 7, this.mContentEndMargin);
                this.mConstraintSet.constrainedWidth(i5, true);
            }
        } else {
            this.mConstraintSet.connect(i3, 3, 0, 3);
            this.mConstraintSet.setMargin(i3, 3, this.mContentTopMargin);
            if (!zIsEmpty) {
                this.mConstraintSet.connect(i3, 7, i5, 6);
                this.mConstraintSet.setMargin(i3, 7, this.mTextToBtnGap);
            } else if (zHasLegacyIgnoreLink) {
                this.mConstraintSet.connect(i3, 7, R.id.ignore, 6);
                this.mConstraintSet.setMargin(i3, 7, this.mTextToBtnGap);
            } else if (zHasCloseIcon) {
                this.mConstraintSet.connect(i3, 7, R.id.close, 6);
                this.mConstraintSet.setMargin(i3, 7, this.mTextToBtnGap);
            } else {
                this.mConstraintSet.connect(i3, 7, 0, 7);
                this.mConstraintSet.setMargin(i3, 7, this.mContentEndMargin);
            }
            if (zHasCloseIcon) {
                ConstraintSet constraintSet9 = this.mConstraintSet;
                int i11 = R.id.close;
                constraintSet9.setVisibility(i11, 0);
                this.mConstraintSet.connect(i11, 7, 0, 7);
                this.mConstraintSet.setMargin(i11, 7, this.mCloseEndMargin);
                this.mConstraintSet.connect(i11, 3, i3, 3);
                this.mConstraintSet.setMargin(i11, 3, 0);
                this.mConstraintSet.connect(i11, 4, i4, 4);
            } else {
                this.mConstraintSet.setVisibility(R.id.close, 8);
            }
            this.mConstraintSet.connect(i4, 6, i3, 6);
            this.mConstraintSet.setMargin(i4, 6, 0);
            this.mConstraintSet.connect(i4, 3, i3, 4);
            this.mConstraintSet.setMargin(i4, 3, this.mTitleContentGap);
            this.mConstraintSet.connect(i4, 4, 0, 4);
            if (!zIsEmpty) {
                this.mConstraintSet.connect(i4, 7, i5, 6);
                this.mConstraintSet.setMargin(i4, 7, this.mTextToBtnGap);
            } else if (zHasLegacyIgnoreLink) {
                this.mConstraintSet.connect(i4, 7, R.id.ignore, 6);
                this.mConstraintSet.setMargin(i4, 7, this.mTextToBtnGap);
            } else if (zHasCloseIcon) {
                this.mConstraintSet.connect(i4, 7, R.id.close, 6);
                this.mConstraintSet.setMargin(i4, 7, this.mTextToBtnGap);
            } else {
                this.mConstraintSet.connect(i4, 7, 0, 7);
                this.mConstraintSet.setMargin(i4, 7, this.mContentEndMargin);
            }
            if (hasStartIcon()) {
                ConstraintSet constraintSet10 = this.mConstraintSet;
                int i12 = R.id.image;
                constraintSet10.connect(i12, 3, i3, 3);
                this.mConstraintSet.setMargin(i12, 3, 0);
                this.mConstraintSet.connect(i12, 4, i4, 4);
            }
            if (zIsEmpty) {
                this.mConstraintSet.setVisibility(i5, 8);
            } else {
                this.mConstraintSet.setVisibility(i5, 0);
                this.mConstraintSet.clear(i5, 6);
                this.mConstraintSet.setVerticalBias(i5, 0.5f);
                this.mConstraintSet.constrainedWidth(i5, false);
                this.mConstraintSet.connect(i5, 3, i3, 3);
                this.mConstraintSet.connect(i5, 4, i4, 4);
                this.mConstraintSet.setMargin(i5, 3, 0);
                this.mConstraintSet.setMargin(i5, 4, 0);
                if (zHasCloseIcon) {
                    this.mConstraintSet.connect(i5, 7, R.id.close, 6);
                    this.mConstraintSet.setMargin(i5, 7, this.mActionCloseGap);
                } else {
                    this.mConstraintSet.connect(i5, 7, 0, 7);
                    this.mConstraintSet.setMargin(i5, 7, this.mBtnEndMargin);
                }
            }
            if (zHasLegacyIgnoreLink) {
                connectLegacyIgnoreInline(i3, i4);
            }
        }
        if (this.mOnLinesChangedListener != null) {
            int lineCount = this.mContent.getLineCount();
            this.mContentLines = lineCount;
            this.mOnLinesChangedListener.onLinesChanged(lineCount);
        }
        this.mConstraintSet.applyTo(this);
    }

    private void changeLayoutWithoutTitle() {
        this.mConstraintSet.clone(this);
        applyLegacyIgnoreVisibility();
        this.mConstraintSet.setVisibility(R.id.title, 8);
        ConstraintSet constraintSet = this.mConstraintSet;
        int i3 = R.id.action;
        constraintSet.setVisibility(i3, TextUtils.isEmpty(this.mAction.getText()) ? 8 : 0);
        ConstraintSet constraintSet2 = this.mConstraintSet;
        int i4 = R.id.content;
        constraintSet2.connect(i4, 3, 0, 3);
        this.mConstraintSet.setMargin(i4, 3, this.mContentTopMargin);
        applyContentAreaStartConstraintsWithoutTitle();
        this.mConstraintSet.clear(i3, 6);
        boolean zHasCloseIcon = hasCloseIcon();
        boolean zHasLegacyIgnoreLink = hasLegacyIgnoreLink();
        boolean zIsEmpty = TextUtils.isEmpty(this.mAction.getText());
        if (isNeedMultiTextWithoutTitle()) {
            if (zHasCloseIcon) {
                this.mConstraintSet.connect(i4, 7, R.id.close, 6);
                this.mConstraintSet.setMargin(i4, 7, this.mTextToBtnGap);
            } else if (zHasLegacyIgnoreLink) {
                this.mConstraintSet.connect(i4, 7, R.id.ignore, 6);
                this.mConstraintSet.setMargin(i4, 7, this.mTextToBtnGap);
            } else {
                this.mConstraintSet.connect(i4, 7, 0, 7);
                this.mConstraintSet.setMargin(i4, 7, this.mContentEndMargin);
            }
            if (zIsEmpty) {
                this.mConstraintSet.connect(i4, 4, 0, 4);
            } else {
                this.mConstraintSet.connect(i4, 4, -1, 4);
            }
            if (zIsEmpty) {
                this.mConstraintSet.setVisibility(i3, 8);
            } else {
                this.mConstraintSet.setVisibility(i3, 0);
                this.mConstraintSet.setHorizontalBias(i3, 0.0f);
                this.mConstraintSet.setVerticalBias(i3, 0.0f);
                this.mConstraintSet.connect(i3, 6, i4, 6);
                this.mConstraintSet.connect(i3, 3, i4, 4);
                this.mConstraintSet.connect(i3, 4, 0, 4);
                this.mConstraintSet.connect(i3, 7, 0, 7);
                this.mConstraintSet.setMargin(i3, 3, this.mBtnTopMargin);
                this.mConstraintSet.setMargin(i3, 4, this.mMultiBtnBottomMargin);
                this.mConstraintSet.setMargin(i3, 7, this.mContentEndMargin);
                this.mConstraintSet.constrainedWidth(i3, true);
            }
            if (zHasCloseIcon) {
                ConstraintSet constraintSet3 = this.mConstraintSet;
                int i5 = R.id.close;
                constraintSet3.setVisibility(i5, 0);
                this.mConstraintSet.connect(i5, 7, 0, 7);
                this.mConstraintSet.setMargin(i5, 7, this.mBtnEndMargin);
                this.mConstraintSet.connect(i5, 3, i4, 3);
                this.mConstraintSet.setMargin(i5, 3, getIconMarginAlignedToFirstTwoLines(this.mIconSmallSize));
                this.mConstraintSet.connect(i5, 4, -1, 4);
            } else {
                this.mConstraintSet.setVisibility(R.id.close, 8);
            }
            if (zHasLegacyIgnoreLink) {
                ConstraintSet constraintSet4 = this.mConstraintSet;
                int i6 = R.id.ignore;
                constraintSet4.connect(i6, 7, 0, 7);
                this.mConstraintSet.setMargin(i6, 7, this.mBtnEndMargin);
                this.mConstraintSet.connect(i6, 3, i4, 3);
                this.mConstraintSet.setMargin(i6, 3, 0);
                this.mConstraintSet.connect(i6, 4, -1, 4);
            }
            if (hasStartIcon()) {
                ConstraintSet constraintSet5 = this.mConstraintSet;
                int i7 = R.id.image;
                constraintSet5.connect(i7, 6, 0, 6);
                this.mConstraintSet.setMargin(i7, 6, this.mIconStartMargin);
                this.mConstraintSet.connect(i7, 3, i4, 3);
                this.mConstraintSet.setMargin(i7, 3, getIconMarginAlignedToFirstTwoLines(this.mIconSmallSize));
                this.mConstraintSet.connect(i7, 4, -1, 4);
            }
        } else {
            if (!zIsEmpty) {
                this.mConstraintSet.connect(i4, 7, i3, 6);
                this.mConstraintSet.setMargin(i4, 7, this.mTextToBtnGap);
            } else if (zHasLegacyIgnoreLink) {
                this.mConstraintSet.connect(i4, 7, R.id.ignore, 6);
                this.mConstraintSet.setMargin(i4, 7, this.mTextToBtnGap);
            } else if (zHasCloseIcon) {
                this.mConstraintSet.connect(i4, 7, R.id.close, 6);
                this.mConstraintSet.setMargin(i4, 7, this.mTextToBtnGap);
            } else {
                this.mConstraintSet.connect(i4, 7, 0, 7);
                this.mConstraintSet.setMargin(i4, 7, this.mContentEndMargin);
            }
            this.mConstraintSet.connect(i4, 4, 0, 4);
            if (zIsEmpty) {
                this.mConstraintSet.setVisibility(i3, 8);
            } else {
                this.mConstraintSet.setVisibility(i3, 0);
                this.mConstraintSet.setVerticalBias(i3, 0.5f);
                this.mConstraintSet.constrainedWidth(i3, false);
                this.mConstraintSet.connect(i3, 3, i4, 3);
                this.mConstraintSet.connect(i3, 4, i4, 4);
                this.mConstraintSet.setMargin(i3, 3, 0);
                this.mConstraintSet.setMargin(i3, 4, 0);
                if (zHasCloseIcon) {
                    this.mConstraintSet.connect(i3, 7, R.id.close, 6);
                    this.mConstraintSet.setMargin(i3, 7, this.mActionCloseGap);
                } else {
                    this.mConstraintSet.connect(i3, 7, 0, 7);
                    this.mConstraintSet.setMargin(i3, 7, this.mIsSingleLineText ? this.mTextToBtnGap : this.mBtnEndMargin);
                }
            }
            if (zHasLegacyIgnoreLink) {
                connectLegacyIgnoreInline(i4, i4);
            }
            if (zHasCloseIcon) {
                ConstraintSet constraintSet6 = this.mConstraintSet;
                int i8 = R.id.close;
                constraintSet6.setVisibility(i8, 0);
                this.mConstraintSet.connect(i8, 7, 0, 7);
                this.mConstraintSet.setMargin(i8, 7, (!zIsEmpty || zHasLegacyIgnoreLink) ? this.mCloseEndMargin : this.mBtnEndMargin);
                this.mConstraintSet.connect(i8, 3, i4, 3);
                this.mConstraintSet.setMargin(i8, 3, 0);
                this.mConstraintSet.connect(i8, 4, i4, 4);
            } else {
                this.mConstraintSet.setVisibility(R.id.close, 8);
            }
            if (hasStartIcon()) {
                ConstraintSet constraintSet7 = this.mConstraintSet;
                int i9 = R.id.image;
                constraintSet7.connect(i9, 3, i4, 3);
                this.mConstraintSet.connect(i9, 4, i4, 4);
                this.mConstraintSet.setMargin(i9, 3, 0);
            }
        }
        if (this.mOnLinesChangedListener != null) {
            int lineCount = this.mContent.getLineCount();
            this.mContentLines = lineCount;
            this.mOnLinesChangedListener.onLinesChanged(lineCount);
        }
        this.mConstraintSet.applyTo(this);
    }

    private void clearDualButtonSizing() {
        this.mAction.setMinimumWidth(0);
        this.mAction.setMinimumHeight(0);
        this.mAction.setMaxLines(1);
        COUIButton ignoreButton = getIgnoreButton();
        if (ignoreButton != null) {
            ignoreButton.setMinimumWidth(0);
            ignoreButton.setMinimumHeight(0);
            ignoreButton.setMaxLines(1);
        }
    }

    private void configureDualButtonSizing() {
        COUIButton ignoreButton = getIgnoreButton();
        if (!hasDualButtons() || ignoreButton == null) {
            return;
        }
        this.mAction.setMinimumWidth(this.mDualBtnMinWidth);
        ignoreButton.setMinimumWidth(this.mDualBtnMinWidth);
        int paddingTop = this.mAction.getPaddingTop();
        int paddingBottom = this.mAction.getPaddingBottom();
        COUIButton cOUIButton = this.mAction;
        int i3 = this.mHorButHorPadding;
        cOUIButton.setPaddingRelative(i3, paddingTop, i3, paddingBottom);
        int i4 = this.mHorButHorPadding;
        ignoreButton.setPaddingRelative(i4, paddingTop, i4, paddingBottom);
    }

    private void configureDualButtonTextLines() {
        COUIButton ignoreButton = getIgnoreButton();
        if (ignoreButton == null) {
            return;
        }
        this.mAction.setMaxLines(1);
        ignoreButton.setMaxLines(1);
        this.mAction.setMinimumHeight(this.mDualBtnMinHeight);
        ignoreButton.setMinimumHeight(this.mDualBtnMinHeight);
    }

    private void connectLegacyIgnoreInline(int i3, int i4) {
        ConstraintSet constraintSet = this.mConstraintSet;
        int i5 = R.id.ignore;
        constraintSet.setVerticalBias(i5, 0.5f);
        this.mConstraintSet.constrainedWidth(i5, false);
        this.mConstraintSet.connect(i5, 3, i3, 3);
        this.mConstraintSet.connect(i5, 4, i4, 4);
        this.mConstraintSet.setMargin(i5, 3, 0);
        this.mConstraintSet.setMargin(i5, 4, 0);
        if (hasCloseIcon()) {
            this.mConstraintSet.connect(i5, 7, R.id.close, 6);
            this.mConstraintSet.setMargin(i5, 7, this.mActionCloseGap);
        } else {
            this.mConstraintSet.connect(i5, 7, 0, 7);
            this.mConstraintSet.setMargin(i5, 7, this.mBtnEndMargin);
        }
    }

    public static COUIDefaultTopTipsView createForLayoutMode(Context context, LayoutMode layoutMode) {
        return new COUIDefaultTopTipsView(context, layoutMode);
    }

    private void ensureLayoutMode() {
        LayoutMode layoutModeResolveTargetLayoutMode = resolveTargetLayoutMode();
        if (layoutModeResolveTargetLayoutMode == this.mLayoutMode) {
            return;
        }
        switchLayoutMode(layoutModeResolveTargetLayoutMode);
    }

    private int getDualButtonBottomMargin() {
        return this.mAiPaymentDualBtnBottomMargin;
    }

    private int getIconMarginAlignedToFirstTwoLines(int i3) {
        Layout layout = this.mContent.getLayout();
        if (layout == null || layout.getLineCount() < 2) {
            return 0;
        }
        return Math.max(0, (int) ((this.mContent.getCompoundPaddingTop() + ((layout.getLineTop(0) + layout.getLineBottom(1)) / 2.0f)) - (i3 / 2.0f)));
    }

    private int getIconTopForSingleTitleMultiContent(int i3) {
        float paddingTop;
        Paint.FontMetrics fontMetrics = this.mTitle.getPaint().getFontMetrics();
        float fMax = Math.max(this.mTitleMinHeight, ((int) Math.ceil(fontMetrics.bottom - fontMetrics.top)) + this.mTitle.getPaddingTop() + this.mTitle.getPaddingBottom());
        int i4 = this.mContentTopMargin;
        float f3 = i4 + (fMax / 2.0f);
        float f4 = i4 + fMax + this.mTitleContentGap;
        Layout layout = this.mContent.getLayout();
        if (layout == null || layout.getLineCount() <= 0) {
            Paint.FontMetrics fontMetrics2 = this.mContent.getPaint().getFontMetrics();
            paddingTop = f4 + this.mContent.getPaddingTop() + ((fontMetrics2.bottom - fontMetrics2.top) / 2.0f);
        } else {
            paddingTop = f4 + this.mContent.getCompoundPaddingTop() + ((layout.getLineTop(0) + layout.getLineBottom(0)) / 2.0f);
        }
        return Math.max(0, (int) (((f3 + paddingTop) / 2.0f) - (i3 / 2.0f)));
    }

    private COUIButton getIgnoreButton() {
        TextView textView = this.mIgnore;
        if (textView instanceof COUIButton) {
            return (COUIButton) textView;
        }
        return null;
    }

    private int getTitleAreaLeftFixedWidth() {
        if (!hasStartIcon()) {
            return this.mContentEndMargin;
        }
        int measuredWidth = this.mImage.getMeasuredWidth();
        if (measuredWidth <= 0) {
            measuredWidth = this.mIconLargeSize;
        }
        return this.mIconStartMargin + measuredWidth + this.mTitleIconTextGap;
    }

    private boolean hasCloseIcon() {
        return this.mHasSetCloseDrawable;
    }

    private boolean hasDualButtons() {
        return !TextUtils.isEmpty(this.mAction.getText()) && hasIgnoreText();
    }

    private boolean hasIgnoreText() {
        return !TextUtils.isEmpty(this.mIgnore.getText());
    }

    private boolean hasLegacyIgnoreLink() {
        return hasIgnoreText() && TextUtils.isEmpty(this.mAction.getText());
    }

    private boolean hasStartIcon() {
        ImageView imageView = this.mImage;
        return (imageView == null || imageView.getDrawable() == null) ? false : true;
    }

    private void inflateLayout(LayoutMode layoutMode) {
        LayoutInflater.from(getContext()).inflate(layoutMode == LayoutMode.AI_PAYMENT ? R.layout.coui_ai_payment_toptips : R.layout.coui_default_toptips, this);
    }

    private boolean isAiPaymentLayout() {
        return this.mLayoutMode == LayoutMode.AI_PAYMENT;
    }

    /* JADX WARN: Code duplicated, block: B:32:0x00a9  */
    /* JADX WARN: Code duplicated, block: B:33:0x00ab  */
    /* JADX WARN: Code duplicated, block: B:36:0x00b0  */
    /* JADX WARN: Code duplicated, block: B:37:0x00b2  */
    /* JADX WARN: Code duplicated, block: B:40:0x00bb A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:41:0x00bc  */
    private boolean isNeedDefaultMultiTextWithTitle() {
        float measuredWidth;
        float f3;
        float f4;
        int i3;
        float fMeasureText;
        float fMeasureText2;
        float f5;
        boolean z2;
        boolean z3;
        int measuredWidth2 = getMeasuredWidth();
        if (measuredWidth2 <= 0) {
            return false;
        }
        boolean zIsEmpty = TextUtils.isEmpty(this.mAction.getText());
        boolean z4 = hasIgnoreText() || this.mHasSetCloseDrawable;
        float f6 = 0.0f;
        if (zIsEmpty) {
            measuredWidth = 0.0f;
        } else {
            this.mAction.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
            measuredWidth = this.mAction.getMeasuredWidth();
        }
        if (z4) {
            float measuredWidth3 = this.mClose.getMeasuredWidth();
            f6 = measuredWidth3 <= 0.0f ? this.mIconSmallSize : measuredWidth3;
        }
        if (!zIsEmpty && z4) {
            f4 = this.mTextToBtnGap + measuredWidth + this.mActionCloseGap + f6;
            i3 = this.mCloseEndMargin;
        } else {
            if (zIsEmpty) {
                if (z4) {
                    f4 = this.mTextToBtnGap + f6;
                    i3 = this.mCloseEndMargin;
                } else {
                    f3 = this.mContentEndMargin;
                }
                int titleAreaLeftFixedWidth = (measuredWidth2 - getTitleAreaLeftFixedWidth()) - ((int) f3);
                fMeasureText = this.mTitle.getPaint().measureText(this.mTitle.getText().toString());
                fMeasureText2 = this.mContent.getPaint().measureText(this.mContent.getText().toString());
                this.mIsSingleLineText = false;
                f5 = titleAreaLeftFixedWidth;
                if (fMeasureText > f5) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (fMeasureText2 > f5) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                this.mIsTitleNeedMultiLine = z2;
                if (hasStartIcon()) {
                    return !z2 || z3;
                }
                return false;
            }
            f4 = this.mTextToBtnGap + measuredWidth;
            i3 = this.mBtnEndMargin;
        }
        f3 = f4 + i3;
        int titleAreaLeftFixedWidth2 = (measuredWidth2 - getTitleAreaLeftFixedWidth()) - ((int) f3);
        fMeasureText = this.mTitle.getPaint().measureText(this.mTitle.getText().toString());
        fMeasureText2 = this.mContent.getPaint().measureText(this.mContent.getText().toString());
        this.mIsSingleLineText = false;
        f5 = titleAreaLeftFixedWidth2;
        if (fMeasureText > f5) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (fMeasureText2 > f5) {
            z3 = true;
        } else {
            z3 = false;
        }
        this.mIsTitleNeedMultiLine = z2;
        if (hasStartIcon()) {
            return false;
        }
        if (z2) {
        }
    }

    /* JADX WARN: Code duplicated, block: B:34:0x0078 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:35:0x0079  */
    /* JADX WARN: Code duplicated, block: B:37:0x007f  */
    /* JADX WARN: Code duplicated, block: B:39:0x0087  */
    /* JADX WARN: Code duplicated, block: B:40:0x008e  */
    /* JADX WARN: Code duplicated, block: B:42:0x0097  */
    /* JADX WARN: Code duplicated, block: B:45:0x009f A[ADDED_TO_REGION, RETURN] */
    /* JADX WARN: Code duplicated, block: B:46:0x00a0  */
    /* JADX WARN: Code duplicated, block: B:50:0x00c9  */
    /* JADX WARN: Code duplicated, block: B:51:0x00cb  */
    /* JADX WARN: Code duplicated, block: B:54:0x00d4 A[ADDED_TO_REGION, RETURN] */
    /* JADX WARN: Code duplicated, block: B:55:0x00d5  */
    private boolean isNeedDefaultMultiTextWithoutTitle() {
        float measuredWidth;
        float f3;
        float f4;
        int i3;
        float f5;
        int measuredWidth2;
        int i4;
        int i5;
        int lineCount;
        boolean z2;
        int measuredWidth3;
        if (this.mContent.getMaxLines() == 1) {
            this.mIsSingleLineText = true;
            return false;
        }
        boolean zIsEmpty = TextUtils.isEmpty(this.mAction.getText());
        boolean z3 = hasIgnoreText() || this.mHasSetCloseDrawable;
        float f6 = 0.0f;
        if (zIsEmpty) {
            measuredWidth = 0.0f;
        } else {
            this.mAction.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
            measuredWidth = this.mAction.getMeasuredWidth();
        }
        if (z3) {
            float measuredWidth4 = this.mClose.getMeasuredWidth();
            f6 = measuredWidth4 <= 0.0f ? this.mIconSmallSize : measuredWidth4;
        }
        if (zIsEmpty) {
            if (z3) {
                f4 = this.mTextToBtnGap + f6;
                i3 = this.mBtnEndMargin;
            } else {
                f3 = this.mContentEndMargin;
            }
            measuredWidth2 = getMeasuredWidth();
            if (measuredWidth2 <= 0) {
                return false;
            }
            if (hasStartIcon()) {
                if (this.mImage.getMeasuredWidth() > 0) {
                    measuredWidth3 = this.mImage.getMeasuredWidth();
                } else {
                    measuredWidth3 = this.mIconSmallSize;
                }
                i4 = measuredWidth3 + this.mIconStartMargin + this.mContentStartMargin;
            } else {
                i4 = this.mContentEndMargin;
            }
            i5 = (measuredWidth2 - i4) - ((int) f3);
            if (i5 <= 0) {
                return false;
            }
            lineCount = new StaticLayout(this.mContent.getText(), this.mContent.getPaint(), i5, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false).getLineCount();
            if (lineCount == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            this.mIsSingleLineText = z2;
            if (hasStartIcon()) {
                return true;
            }
            return false;
        }
        f4 = this.mTextToBtnGap + measuredWidth;
        if (z3) {
            f5 = this.mActionCloseGap + f6 + this.mCloseEndMargin;
        } else {
            i3 = this.mBtnEndMargin;
        }
        f3 = f4 + f5;
        measuredWidth2 = getMeasuredWidth();
        if (measuredWidth2 <= 0) {
            return false;
        }
        if (hasStartIcon()) {
            if (this.mImage.getMeasuredWidth() > 0) {
                measuredWidth3 = this.mImage.getMeasuredWidth();
            } else {
                measuredWidth3 = this.mIconSmallSize;
            }
            i4 = measuredWidth3 + this.mIconStartMargin + this.mContentStartMargin;
        } else {
            i4 = this.mContentEndMargin;
        }
        i5 = (measuredWidth2 - i4) - ((int) f3);
        if (i5 <= 0 && !TextUtils.isEmpty(this.mContent.getText())) {
            lineCount = new StaticLayout(this.mContent.getText(), this.mContent.getPaint(), i5, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false).getLineCount();
            if (lineCount == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            this.mIsSingleLineText = z2;
            if (hasStartIcon() && lineCount > 2) {
                return true;
            }
        }
        return false;
        f5 = i3;
        f3 = f4 + f5;
        measuredWidth2 = getMeasuredWidth();
        if (measuredWidth2 <= 0) {
            return false;
        }
        if (hasStartIcon()) {
            if (this.mImage.getMeasuredWidth() > 0) {
                measuredWidth3 = this.mImage.getMeasuredWidth();
            } else {
                measuredWidth3 = this.mIconSmallSize;
            }
            i4 = measuredWidth3 + this.mIconStartMargin + this.mContentStartMargin;
        } else {
            i4 = this.mContentEndMargin;
        }
        i5 = (measuredWidth2 - i4) - ((int) f3);
        if (i5 <= 0) {
            return false;
        }
        lineCount = new StaticLayout(this.mContent.getText(), this.mContent.getPaint(), i5, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false).getLineCount();
        if (lineCount == 1) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.mIsSingleLineText = z2;
        if (hasStartIcon()) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Code duplicated, block: B:41:0x00e8  */
    /* JADX WARN: Code duplicated, block: B:42:0x00ea  */
    /* JADX WARN: Code duplicated, block: B:45:0x00ef  */
    /* JADX WARN: Code duplicated, block: B:46:0x00f1  */
    /* JADX WARN: Code duplicated, block: B:49:0x00fa A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:50:0x00fb  */
    private boolean isNeedMultiTextWithTitle() {
        float measuredWidth;
        float measuredWidth2;
        float f3;
        float f4;
        int i3;
        float fMeasureText;
        float fMeasureText2;
        float f5;
        boolean z2;
        boolean z3;
        COUIButton ignoreButton;
        if (hasDualButtons()) {
            return true;
        }
        int measuredWidth3 = getMeasuredWidth();
        if (measuredWidth3 <= 0) {
            return false;
        }
        boolean zIsEmpty = TextUtils.isEmpty(this.mAction.getText());
        boolean zHasCloseIcon = hasCloseIcon();
        boolean zHasLegacyIgnoreLink = hasLegacyIgnoreLink();
        if (hasStartIcon()) {
            this.mImage.getMeasuredWidth();
        }
        float f6 = 0.0f;
        if (zIsEmpty) {
            measuredWidth = 0.0f;
        } else {
            this.mAction.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
            measuredWidth = this.mAction.getMeasuredWidth();
        }
        if (!zHasLegacyIgnoreLink || (ignoreButton = getIgnoreButton()) == null) {
            measuredWidth2 = 0.0f;
        } else {
            ignoreButton.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
            measuredWidth2 = ignoreButton.getMeasuredWidth();
        }
        if (zHasCloseIcon) {
            float measuredWidth4 = this.mClose.getMeasuredWidth();
            f6 = measuredWidth4 <= 0.0f ? this.mIconSmallSize : measuredWidth4;
        }
        if (!zIsEmpty && zHasCloseIcon) {
            f4 = this.mTextToBtnGap + measuredWidth + this.mActionCloseGap + f6;
            i3 = this.mCloseEndMargin;
        } else if (!zIsEmpty) {
            f4 = this.mTextToBtnGap + measuredWidth;
            i3 = this.mBtnEndMargin;
        } else if (zHasLegacyIgnoreLink && zHasCloseIcon) {
            f4 = this.mTextToBtnGap + measuredWidth2 + this.mActionCloseGap + f6;
            i3 = this.mCloseEndMargin;
        } else {
            if (!zHasLegacyIgnoreLink) {
                if (zHasCloseIcon) {
                    f4 = this.mTextToBtnGap + f6;
                    i3 = this.mCloseEndMargin;
                } else {
                    f3 = this.mContentEndMargin;
                }
                int titleAreaLeftFixedWidth = (measuredWidth3 - getTitleAreaLeftFixedWidth()) - ((int) f3);
                fMeasureText = this.mTitle.getPaint().measureText(this.mTitle.getText().toString());
                fMeasureText2 = this.mContent.getPaint().measureText(this.mContent.getText().toString());
                this.mIsSingleLineText = false;
                f5 = titleAreaLeftFixedWidth;
                if (fMeasureText > f5) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (fMeasureText2 > f5) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                this.mIsTitleNeedMultiLine = z2;
                if (hasStartIcon()) {
                    return !z2 || z3;
                }
                return false;
            }
            f4 = this.mTextToBtnGap + measuredWidth2;
            i3 = this.mBtnEndMargin;
        }
        f3 = f4 + i3;
        int titleAreaLeftFixedWidth2 = (measuredWidth3 - getTitleAreaLeftFixedWidth()) - ((int) f3);
        fMeasureText = this.mTitle.getPaint().measureText(this.mTitle.getText().toString());
        fMeasureText2 = this.mContent.getPaint().measureText(this.mContent.getText().toString());
        this.mIsSingleLineText = false;
        f5 = titleAreaLeftFixedWidth2;
        if (fMeasureText > f5) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (fMeasureText2 > f5) {
            z3 = true;
        } else {
            z3 = false;
        }
        this.mIsTitleNeedMultiLine = z2;
        if (hasStartIcon()) {
            return false;
        }
        if (z2) {
        }
    }

    private boolean isNeedMultiTextWithoutTitle() {
        float measuredWidth;
        float measuredWidth2;
        float measuredWidth3;
        float f3;
        float f4;
        int i3;
        float f5;
        int i4;
        float f6;
        int measuredWidth4;
        COUIButton ignoreButton;
        if (hasDualButtons()) {
            this.mIsSingleLineText = false;
            return true;
        }
        if (this.mContent.getMaxLines() == 1) {
            this.mIsSingleLineText = true;
            return false;
        }
        boolean zIsEmpty = TextUtils.isEmpty(this.mAction.getText());
        boolean zHasCloseIcon = hasCloseIcon();
        boolean zHasLegacyIgnoreLink = hasLegacyIgnoreLink();
        if (zIsEmpty) {
            measuredWidth = 0.0f;
        } else {
            this.mAction.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
            measuredWidth = this.mAction.getMeasuredWidth();
        }
        if (!zHasLegacyIgnoreLink || (ignoreButton = getIgnoreButton()) == null) {
            measuredWidth2 = 0.0f;
        } else {
            ignoreButton.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
            measuredWidth2 = ignoreButton.getMeasuredWidth();
        }
        if (zHasCloseIcon) {
            measuredWidth3 = this.mClose.getMeasuredWidth();
            if (measuredWidth3 <= 0.0f) {
                measuredWidth3 = this.mIconSmallSize;
            }
        } else {
            measuredWidth3 = 0.0f;
        }
        if (!zIsEmpty) {
            f4 = this.mTextToBtnGap + measuredWidth + 0.0f;
            if (zHasCloseIcon) {
                f5 = this.mActionCloseGap + measuredWidth3;
                i4 = this.mCloseEndMargin;
                f6 = f5 + i4;
                f3 = f4 + f6;
            } else {
                i3 = this.mBtnEndMargin;
                f6 = i3;
                f3 = f4 + f6;
            }
        } else if (zHasLegacyIgnoreLink) {
            f4 = this.mTextToBtnGap + measuredWidth2 + 0.0f;
            if (zHasCloseIcon) {
                f5 = this.mActionCloseGap + measuredWidth3;
                i4 = this.mCloseEndMargin;
                f6 = f5 + i4;
                f3 = f4 + f6;
            } else {
                i3 = this.mBtnEndMargin;
                f6 = i3;
                f3 = f4 + f6;
            }
        } else {
            f3 = (zHasCloseIcon ? this.mTextToBtnGap + measuredWidth3 + this.mBtnEndMargin : this.mContentEndMargin) + 0.0f;
        }
        int measuredWidth5 = getMeasuredWidth();
        if (measuredWidth5 <= 0) {
            return false;
        }
        if (hasStartIcon()) {
            measuredWidth4 = (this.mImage.getMeasuredWidth() > 0 ? this.mImage.getMeasuredWidth() : this.mIconSmallSize) + this.mIconStartMargin + this.mContentStartMargin;
        } else {
            measuredWidth4 = this.mContentEndMargin;
        }
        int i5 = (measuredWidth5 - measuredWidth4) - ((int) f3);
        if (i5 > 0 && !TextUtils.isEmpty(this.mContent.getText())) {
            int lineCount = new StaticLayout(this.mContent.getText(), this.mContent.getPaint(), i5, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false).getLineCount();
            this.mIsSingleLineText = lineCount == 1;
            if (hasStartIcon() && lineCount > 2) {
                return true;
            }
        }
        return false;
    }

    private boolean isTitleMultiLineInMultiLayout(boolean z2) {
        return this.mTitle.getPaint().measureText(this.mTitle.getText().toString()) > ((float) ((((getMeasuredWidth() - getTitleAreaLeftFixedWidth()) - (z2 ? (this.mTextToBtnGap + this.mIconSmallSize) + this.mBtnEndMargin : this.mContentEndMargin)) - this.mTitle.getPaddingStart()) - this.mTitle.getPaddingEnd()));
    }

    private void loadDimenResources() {
        Resources resources = getResources();
        this.mIconSmallSize = resources.getDimensionPixelSize(R.dimen.coui_toptips_view_icon_btn_size);
        this.mIconLargeSize = resources.getDimensionPixelSize(R.dimen.coui_toptips_view_title_icon_size);
        this.mIconStartMargin = resources.getDimensionPixelSize(R.dimen.coui_toptips_view_icon_start_margin);
        this.mBtnEndMargin = resources.getDimensionPixelSize(R.dimen.coui_toptips_view_btn_margin);
        this.mTextToBtnGap = resources.getDimensionPixelSize(R.dimen.coui_toptips_view_text_to_btn_gap);
        this.mActionCloseGap = resources.getDimensionPixelSize(R.dimen.coui_toptips_view_action_close_gap);
        this.mCloseEndMargin = resources.getDimensionPixelSize(R.dimen.coui_toptips_view_close_end_margin);
        this.mContentStartMargin = resources.getDimensionPixelSize(R.dimen.coui_toptips_view_content_start_margin);
        this.mContentTopMargin = resources.getDimensionPixelSize(R.dimen.coui_toptips_view_content_top_margin);
        this.mContentEndMargin = resources.getDimensionPixelSize(R.dimen.coui_toptips_view_content_end_margin);
        this.mTitleIconTextGap = resources.getDimensionPixelSize(R.dimen.coui_toptips_view_title_icon_text_gap);
        this.mTitleContentGap = resources.getDimensionPixelSize(R.dimen.coui_toptips_view_title_content_gap);
        this.mTitleMinHeight = resources.getDimensionPixelSize(R.dimen.coui_toptips_view_title_min_height);
        this.mBtnTopMargin = resources.getDimensionPixelSize(R.dimen.coui_toptips_view_btn_top_margin);
        this.mMultiBtnBottomMargin = resources.getDimensionPixelSize(R.dimen.coui_toptips_view_multi_btn_text_bottom_margin);
        if (this.mLayoutMode == LayoutMode.AI_PAYMENT) {
            this.mAiPaymentDualBtnBottomMargin = resources.getDimensionPixelSize(R.dimen.coui_toptips_ai_payment_dual_btn_bottom_margin);
            this.mHorButHorPadding = resources.getDimensionPixelSize(R.dimen.coui_toptips_ai_payment_btn_horizontal_padding);
            this.mDualBtnMinWidth = resources.getDimensionPixelSize(R.dimen.coui_toptips_ai_payment_btn_min_width);
            this.mDualBtnMinHeight = resources.getDimensionPixelSize(R.dimen.coui_toptips_ai_payment_btn_min_height);
        }
    }

    private int measureDualButtonDesiredWidth(COUIButton cOUIButton, int i3) {
        if (cOUIButton == null || TextUtils.isEmpty(cOUIButton.getText())) {
            return 0;
        }
        cOUIButton.measure(View.MeasureSpec.makeMeasureSpec(i3, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(0, 0));
        return Math.max(this.mDualBtnMinWidth, cOUIButton.getMeasuredWidth());
    }

    private boolean needDualButtonsVertical(int i3) {
        int i4;
        if (i3 > 0 && (i4 = i3 - (this.mContentEndMargin * 2)) > 0) {
            return (measureDualButtonDesiredWidth(getIgnoreButton(), i4) + this.mTextToBtnGap) + measureDualButtonDesiredWidth(this.mAction, i4) > i4;
        }
        return false;
    }

    private void notifyContentLinesChanged() {
        if (this.mOnLinesChangedListener != null) {
            int lineCount = this.mContent.getLineCount();
            this.mContentLines = lineCount;
            this.mOnLinesChangedListener.onLinesChanged(lineCount);
        }
    }

    private void refreshNegativeButtonStyle() {
        if (hasDualButtons()) {
            applyDualButtonStyles();
        } else if (hasLegacyIgnoreLink()) {
            applyLegacyIgnoreLinkStyle();
        }
    }

    private LayoutMode resolveTargetLayoutMode() {
        return this.mTopTipsStyle == COUIDefaultTopTips.TopTipsStyle.AI_PAYMENT ? LayoutMode.AI_PAYMENT : LayoutMode.DEFAULT;
    }

    private void saveCurrentLayout() {
        if (this.mOriginalConstraintSet == null) {
            this.mOriginalConstraintSet = new ConstraintSet();
        }
        this.mOriginalConstraintSet.clone(this);
    }

    private void setBtnColorImpl(int i3, int i4) {
        if (i3 == 2) {
            this.mIgnore.setTextColor(i4);
        } else {
            if (i3 != 3) {
                throw new IllegalArgumentException("setBtnColorImpl parameter 'which' is wrong");
            }
            this.mAction.setTextColor(i4);
        }
    }

    private void setBtnDrawableImpl(int i3, Drawable drawable) {
        if (i3 != 4) {
            throw new IllegalArgumentException("setBtnDrawableImpl parameter 'which' is wrong");
        }
        this.mClose.setImageDrawable(drawable);
        this.mHasSetCloseDrawable = drawable != null;
        this.mNeedUpdateLayout = true;
    }

    private void setBtnTextImpl(int i3, CharSequence charSequence) {
        if (i3 == 2) {
            this.mIgnore.setText(charSequence);
            this.mNeedUpdateLayout = true;
        } else {
            if (i3 != 3) {
                throw new IllegalArgumentException("setBtnTextImpl parameter 'which' is wrong");
            }
            this.mAction.setText(charSequence);
            this.mNeedUpdateLayout = true;
        }
    }

    private void setupDualButtonsTextAreaWithTitle() {
        ConstraintSet constraintSet = this.mConstraintSet;
        int i3 = R.id.image;
        constraintSet.constrainWidth(i3, this.mIconLargeSize);
        this.mConstraintSet.constrainHeight(i3, this.mIconLargeSize);
        if (hasStartIcon()) {
            this.mConstraintSet.setVisibility(i3, 0);
            this.mConstraintSet.connect(i3, 6, 0, 6);
            this.mConstraintSet.setMargin(i3, 6, this.mIconStartMargin);
            ConstraintSet constraintSet2 = this.mConstraintSet;
            int i4 = R.id.title;
            constraintSet2.connect(i4, 6, i3, 7);
            this.mConstraintSet.setMargin(i4, 6, this.mTitleIconTextGap);
            this.mConstraintSet.connect(i3, 3, i4, 3);
            this.mConstraintSet.connect(i3, 4, i4, 4);
        } else {
            this.mConstraintSet.setVisibility(i3, 8);
            ConstraintSet constraintSet3 = this.mConstraintSet;
            int i5 = R.id.title;
            constraintSet3.connect(i5, 6, 0, 6);
            this.mConstraintSet.setMargin(i5, 6, this.mContentEndMargin);
        }
        ConstraintSet constraintSet4 = this.mConstraintSet;
        int i6 = R.id.title;
        constraintSet4.connect(i6, 3, 0, 3);
        this.mConstraintSet.setMargin(i6, 3, this.mContentTopMargin);
        this.mConstraintSet.connect(i6, 7, 0, 7);
        this.mConstraintSet.setMargin(i6, 7, this.mContentEndMargin);
        ConstraintSet constraintSet5 = this.mConstraintSet;
        int i7 = R.id.content;
        constraintSet5.connect(i7, 6, i6, 6);
        this.mConstraintSet.setMargin(i7, 6, 0);
        this.mConstraintSet.connect(i7, 3, i6, 4);
        this.mConstraintSet.setMargin(i7, 3, this.mTitleContentGap);
        this.mConstraintSet.connect(i7, 7, 0, 7);
        this.mConstraintSet.setMargin(i7, 7, this.mContentEndMargin);
        this.mConstraintSet.connect(i7, 4, -1, 4);
    }

    private void setupDualButtonsTextAreaWithoutTitle() {
        ConstraintSet constraintSet = this.mConstraintSet;
        int i3 = R.id.image;
        constraintSet.constrainWidth(i3, this.mIconSmallSize);
        this.mConstraintSet.constrainHeight(i3, this.mIconSmallSize);
        ConstraintSet constraintSet2 = this.mConstraintSet;
        int i4 = R.id.content;
        constraintSet2.connect(i4, 3, 0, 3);
        this.mConstraintSet.setMargin(i4, 3, this.mContentTopMargin);
        if (hasStartIcon()) {
            this.mConstraintSet.setVisibility(i3, 0);
            this.mConstraintSet.connect(i3, 6, 0, 6);
            this.mConstraintSet.setMargin(i3, 6, this.mIconStartMargin);
            this.mConstraintSet.connect(i4, 6, i3, 7);
            this.mConstraintSet.setMargin(i4, 6, this.mContentStartMargin);
            this.mConstraintSet.connect(i3, 3, i4, 3);
            this.mConstraintSet.connect(i3, 4, i4, 4);
        } else {
            this.mConstraintSet.setVisibility(i3, 8);
            this.mConstraintSet.connect(i4, 6, 0, 6);
            this.mConstraintSet.setMargin(i4, 6, this.mContentEndMargin);
        }
        this.mConstraintSet.connect(i4, 7, 0, 7);
        this.mConstraintSet.setMargin(i4, 7, this.mContentEndMargin);
        this.mConstraintSet.connect(i4, 4, -1, 4);
    }

    private void switchLayoutMode(LayoutMode layoutMode) {
        this.mLayoutFixSpace = null;
        this.mOriginalConstraintSet = null;
        removeAllViews();
        this.mLayoutMode = layoutMode;
        inflateLayout(layoutMode);
        bindChildViews();
        loadDimenResources();
        applyLayoutModeStyles();
    }

    public void applyAiPaymentButtonStyle() {
        COUIButton cOUIButton = this.mAction;
        COUIButton.ButtonType buttonType = COUIButton.ButtonType.AI_PAYMENT;
        cOUIButton.setButtonType(buttonType);
        this.mAction.setUseMaterialEffectAttr(COUIButton.MaterialEffectState.FOLLOW_SYSTEM, buttonType);
        this.mAction.setDrawableRadius(-1);
        if (hasDualButtons() || hasIgnoreText()) {
            applyAiPaymentSecondaryButtonStyle();
        }
        this.mNeedUpdateLayout = true;
    }

    public void applyNewLayoutConstraints() {
        applyLayoutConstraints();
    }

    public void applyTopTipsStyle(COUIDefaultTopTips.TopTipsStyle topTipsStyle) {
        if (topTipsStyle == null) {
            topTipsStyle = COUIDefaultTopTips.TopTipsStyle.STANDARD;
        }
        if (this.mTopTipsStyle == topTipsStyle) {
            ensureLayoutMode();
            return;
        }
        this.mTopTipsStyle = topTipsStyle;
        ensureLayoutMode();
        if (isAiPaymentLayout()) {
            if (topTipsStyle == COUIDefaultTopTips.TopTipsStyle.AI_PAYMENT) {
                applyAiPaymentButtonStyle();
                if (hasDualButtons() || hasIgnoreText()) {
                    applyAiPaymentSecondaryButtonStyle();
                }
                applyAiPaymentTextColors();
                return;
            }
            resetDefaultButtonStyle();
            if (hasDualButtons()) {
                applyDefaultSecondaryButtonStyle();
            } else if (hasLegacyIgnoreLink()) {
                applyLegacyIgnoreLinkStyle();
            }
        }
    }

    public int calculateHeightForWidth(int i3) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        setLayoutParams(new ViewGroup.LayoutParams(i3, -2));
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i3, 1073741824);
        int iMakeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(0, 0);
        measure(iMakeMeasureSpec, iMakeMeasureSpec2);
        applyLayoutConstraints();
        measure(iMakeMeasureSpec, iMakeMeasureSpec2);
        layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
        if (layoutParams != null) {
            setLayoutParams(layoutParams);
        }
        return getMeasuredHeight();
    }

    public void copyInteractionStateTo(COUIDefaultTopTipsView cOUIDefaultTopTipsView) {
        cOUIDefaultTopTipsView.mPositiveClickListener = this.mPositiveClickListener;
        cOUIDefaultTopTipsView.mNegativeClickListener = this.mNegativeClickListener;
        cOUIDefaultTopTipsView.mCloseBtnClickListener = this.mCloseBtnClickListener;
    }

    public void createSpaceAndFixLayout(int i3, int i4) {
        ConstraintSet.Constraint constraint;
        saveCurrentLayout();
        if (this.mLayoutFixSpace == null) {
            Space space = new Space(getContext());
            this.mLayoutFixSpace = space;
            space.setId(View.generateViewId());
        }
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(i3, i4);
        layoutParams.startToStart = 0;
        layoutParams.topToTop = 0;
        this.mLayoutFixSpace.setLayoutParams(layoutParams);
        if (this.mLayoutFixSpace.getParent() == null) {
            addView(this.mLayoutFixSpace, 0);
        }
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);
        int[] iArr = {R.id.image, R.id.title, R.id.content, R.id.action, R.id.close, R.id.ignore};
        for (int i5 = 0; i5 < 6; i5++) {
            int i6 = iArr[i5];
            View viewFindViewById = findViewById(i6);
            if (viewFindViewById != null && viewFindViewById.getVisibility() != 8 && (constraint = constraintSet.getConstraint(i6)) != null) {
                if (constraint.layout.endToEnd == 0) {
                    constraintSet.connect(i6, 7, this.mLayoutFixSpace.getId(), 7);
                }
                if (constraint.layout.startToStart == 0) {
                    constraintSet.connect(i6, 6, this.mLayoutFixSpace.getId(), 6);
                }
                if (constraint.layout.bottomToBottom == 0) {
                    constraintSet.connect(i6, 4, this.mLayoutFixSpace.getId(), 4);
                }
                if (constraint.layout.topToTop == 0) {
                    constraintSet.connect(i6, 3, this.mLayoutFixSpace.getId(), 3);
                }
            }
        }
        constraintSet.applyTo(this);
    }

    public TextView getAction() {
        return this.mAction;
    }

    public View[] getAnimatableViews() {
        return (isAiPaymentLayout() && hasDualButtons()) ? new View[]{this.mImage, this.mTitle, this.mContent, this.mAction, this.mIgnore, this.mClose} : new View[]{this.mImage, this.mTitle, this.mContent, this.mAction, this.mClose};
    }

    public View[] getAnimatableViewsExceptImage() {
        return (isAiPaymentLayout() && hasDualButtons()) ? new View[]{this.mTitle, this.mContent, this.mAction, this.mIgnore, this.mClose} : new View[]{this.mTitle, this.mContent, this.mAction, this.mClose};
    }

    public ImageView getClose() {
        return this.mClose;
    }

    public TextView getContent() {
        return this.mContent;
    }

    public LayoutMode getContentLayoutMode() {
        return this.mLayoutMode;
    }

    public TextView getIgnore() {
        return this.mIgnore;
    }

    public ImageView getImage() {
        return this.mImage;
    }

    public TextView getTitle() {
        return this.mTitle;
    }

    public COUIDefaultTopTips.TopTipsStyle getTopTipsStyle() {
        return this.mTopTipsStyle;
    }

    public boolean hasTitle() {
        TextView textView = this.mTitle;
        return (textView == null || TextUtils.isEmpty(textView.getText())) ? false : true;
    }

    public void init() {
        inflateLayout(this.mLayoutMode);
        bindChildViews();
        loadDimenResources();
        this.mIsSingleLineText = false;
        applyLayoutModeStyles();
    }

    public boolean isSingleLineText() {
        return this.mIsSingleLineText;
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        super.onLayout(z2, i3, i4, i5, i6);
        if (this.mNeedUpdateLayout) {
            this.mNeedUpdateLayout = false;
            applyLayoutConstraints();
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        super.onMeasure(i3, i4);
    }

    public void removeSpaceAndRestoreLayout() {
        Space space = this.mLayoutFixSpace;
        if (space != null && space.getParent() != null) {
            removeView(this.mLayoutFixSpace);
        }
        ConstraintSet constraintSet = this.mOriginalConstraintSet;
        if (constraintSet != null) {
            constraintSet.applyTo(this);
            this.mOriginalConstraintSet = null;
        }
    }

    public void resetDefaultButtonStyle() {
        COUIButton cOUIButton = this.mAction;
        COUIButton.ButtonType buttonType = COUIButton.ButtonType.PRIMARY;
        cOUIButton.setButtonType(buttonType);
        this.mAction.setIsDrawableMaterialEffect(false);
        this.mAction.setUseMaterialEffectAttr(COUIButton.MaterialEffectState.FORCE_DISABLE, buttonType);
        this.mAction.setDrawableColor(COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorContainerTheme, 0));
        this.mAction.setDisabledColor(COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorDisable, 0));
        this.mAction.setAnimEnable(true);
        this.mAction.setAnimType(1);
        this.mAction.setDrawableRadius(-1);
        this.mAction.setTextColor(UIUtil.getColorStateListCompatNoCache(getContext(), com.support.appcompat.R.color.coui_btn_default_text_color));
        if (hasDualButtons()) {
            applyDefaultSecondaryButtonStyle();
        } else {
            clearDualButtonSizing();
            if (hasLegacyIgnoreLink()) {
                applyLegacyIgnoreLinkStyle();
            }
        }
        this.mNeedUpdateLayout = true;
    }

    public void setChildViewsAlpha(float f3) {
        for (View view : getAnimatableViews()) {
            if (view != null) {
                view.setAlpha(f3);
            }
        }
    }

    @Override // com.coui.appcompat.tips.def.IDefaultTopTips
    public void setCloseBtnListener(View.OnClickListener onClickListener) {
        this.mCloseBtnClickListener = onClickListener;
    }

    @Override // com.coui.appcompat.tips.def.IDefaultTopTips
    public void setCloseDrawable(Drawable drawable) {
        setBtnDrawableImpl(4, drawable);
    }

    @Override // com.coui.appcompat.tips.def.IDefaultTopTips
    public void setNegativeButton(CharSequence charSequence) {
        setBtnTextImpl(2, charSequence);
        refreshNegativeButtonStyle();
    }

    @Override // com.coui.appcompat.tips.def.IDefaultTopTips
    public void setNegativeButtonColor(int i3) {
        if (this.mTopTipsStyle == COUIDefaultTopTips.TopTipsStyle.AI_PAYMENT && hasDualButtons()) {
            return;
        }
        setBtnColorImpl(2, i3);
    }

    @Override // com.coui.appcompat.tips.def.IDefaultTopTips
    public void setNegativeButtonListener(View.OnClickListener onClickListener) {
        this.mNegativeClickListener = onClickListener;
    }

    public void setOnLinesChangedListener(OnLinesChangedListener onLinesChangedListener) {
        this.mOnLinesChangedListener = onLinesChangedListener;
    }

    @Override // com.coui.appcompat.tips.def.IDefaultTopTips
    public void setPositiveButton(CharSequence charSequence) {
        setBtnTextImpl(3, charSequence);
        if (this.mTopTipsStyle == COUIDefaultTopTips.TopTipsStyle.AI_PAYMENT) {
            applyAiPaymentButtonStyle();
        }
        refreshNegativeButtonStyle();
    }

    @Override // com.coui.appcompat.tips.def.IDefaultTopTips
    public void setPositiveButtonColor(int i3) {
        if (this.mTopTipsStyle == COUIDefaultTopTips.TopTipsStyle.AI_PAYMENT) {
            return;
        }
        setBtnColorImpl(3, i3);
    }

    @Override // com.coui.appcompat.tips.def.IDefaultTopTips
    public void setPositiveButtonListener(View.OnClickListener onClickListener) {
        this.mPositiveClickListener = onClickListener;
    }

    @Override // com.coui.appcompat.tips.def.IDefaultTopTips
    public void setStartIcon(Drawable drawable) {
        this.mImage.setImageDrawable(drawable);
        this.mNeedUpdateLayout = true;
    }

    @Override // com.coui.appcompat.tips.def.IDefaultTopTips
    public void setTipsText(CharSequence charSequence) {
        this.mContent.setText(charSequence);
        this.mNeedUpdateLayout = true;
    }

    @Override // com.coui.appcompat.tips.def.IDefaultTopTips
    public void setTipsTextColor(int i3) {
        if (this.mTopTipsStyle == COUIDefaultTopTips.TopTipsStyle.AI_PAYMENT && isAiPaymentLayout()) {
            return;
        }
        this.mContent.setTextColor(i3);
    }

    @Override // com.coui.appcompat.tips.def.IDefaultTopTips
    public void setTitleText(CharSequence charSequence) {
        this.mTitle.setText(charSequence);
        this.mNeedUpdateLayout = true;
    }

    public void startRoll() {
        this.mContent.continueRoll();
    }

    public void stopRoll() {
        this.mContent.stopRoll();
        this.mContent.setMarqueeEnable(false);
    }

    public COUIDefaultTopTipsView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIDefaultTopTipsView(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.mConstraintSet = new ConstraintSet();
        this.mNeedUpdateLayout = true;
        this.mHasSetCloseDrawable = false;
        this.mIsSingleLineText = false;
        this.mIsTitleNeedMultiLine = false;
        this.mTopTipsStyle = COUIDefaultTopTips.TopTipsStyle.STANDARD;
        this.mLayoutMode = LayoutMode.DEFAULT;
        this.mContentLines = -1;
        init();
    }

    private COUIDefaultTopTipsView(Context context, LayoutMode layoutMode) {
        super(context);
        this.mConstraintSet = new ConstraintSet();
        this.mNeedUpdateLayout = true;
        this.mHasSetCloseDrawable = false;
        this.mIsSingleLineText = false;
        this.mIsTitleNeedMultiLine = false;
        this.mTopTipsStyle = COUIDefaultTopTips.TopTipsStyle.STANDARD;
        LayoutMode layoutMode2 = LayoutMode.DEFAULT;
        this.mContentLines = -1;
        this.mLayoutMode = layoutMode;
        init();
    }
}