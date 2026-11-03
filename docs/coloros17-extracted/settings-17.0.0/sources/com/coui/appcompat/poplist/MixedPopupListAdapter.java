package com.coui.appcompat.poplist;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.coui.appcompat.accessibilityutil.COUIAccessibilityUtil;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.reddot.COUIHintRedDot;
import com.coui.appcompat.roundRect.COUIShapePath;
import com.coui.appcompat.state.COUIMaskEffectDrawable;
import com.coui.appcompat.textutil.COUIChangeTextUtil;
import com.coui.appcompat.uiutil.UIUtil;
import com.coui.appcompat.version.COUIVersionUtil;
import com.support.appcompat.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/* JADX INFO: loaded from: classes7.dex */
public class MixedPopupListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int DESCRIPTION_MAX_LINES = 2;
    private static final float DESCRIPTION_TEXT_SIZE_DP = 12.0f;
    private static final float FIRST_TITLE_SIZE_DP = 12.0f;
    private static final int FLAG_DESCRIPTION = 2;
    private static final int FLAG_HAS_SUB_MENU = 16;
    private static final int FLAG_HINT = 4;
    private static final int FLAG_ICON = 1;
    private static final int FLAG_STATE_ICON = 8;
    private static final int HORIZONTAL_TITLE_MAX_LINES = 2;
    private static final int HORIZONTAL_TITLE_MIN_LINES_SINGLE = 1;
    private static final int MASK_FULL_ROUND_HEIGHT_RADIUS_MULTIPLIER = 2;
    private static final float MASK_SMOOTH_ROUND_CORNER_WEIGHT_DEFAULT = 3.0f;
    private static final float MASK_SMOOTH_ROUND_CORNER_WEIGHT_FULL_ROUND = 1.0f;
    private static final float MASK_SMOOTH_ROUND_CORNER_WEIGHT_NONE = 0.0f;
    private static final int MAX_HORIZONTAL_ITEMS_ICON_ONLY = 5;
    private static final int MAX_HORIZONTAL_ITEMS_WITH_TEXT = 3;
    private static final String TAG = "MixedPopupListAdapter";
    private static final int TITLE_MAX_LINE_WITHOUT_DESCRIPTION = 3;
    private static final int TITLE_MAX_LINE_WITH_DESCRIPTION = 2;
    private static final float TITLE_TEXT_SIZE_DP = 16.0f;
    public static final int TYPE_CUSTOM = 5;
    public static final int TYPE_DIVIDER_DEFAULT = 3;
    public static final int TYPE_DIVIDER_GROUP = 4;
    public static final int TYPE_HEADER = 2;
    public static final int TYPE_HORIZONTAL = 0;
    public static final int TYPE_VERTICAL = 1;
    private int mAlertColor;
    private RecyclerView mAttachedRecyclerView;
    private final Context mContext;
    private int mDefaultDividerHeight;
    private int mDefaultDividerMarginHorizontal;
    private int mDescriptionColor;
    private int mGroupDividerTotalHeight;
    private int mHorizontalItemIconOnlyEdgeMargin;
    private int mHorizontalItemIconOnlyHeight;
    private int mHorizontalItemIconOnlyPaddingHorizontal;
    private int mHorizontalItemIconOnlySpacingDefault;
    private int mHorizontalItemIconOnlyWidth;
    private int mHorizontalItemIconSizeIconOnly;
    private int mHorizontalItemIconSizeWithText;
    private int mHorizontalItemIconTitleGap;
    private int mHorizontalItemMinWidth;
    private int mHorizontalItemMinWidthWithText;
    private int mHorizontalItemSpacingMin;
    private int mHorizontalItemTextEdgeMargin;
    private int mHorizontalItemTextMinHeight;
    private int mHorizontalItemTextPaddingHorizontal;
    private int mHorizontalItemTextPaddingVertical;
    private int mHorizontalItemTextSpacingDefault;
    private ColorStateList mItemTintList;
    private List<PopupListItem> mMenuItemList;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private AdapterView.OnItemLongClickListener mOnItemLongClickListener;
    private int mPopupListItemMinHeight;
    private int mPopupListItemPaddingVertical;
    private ListItemMaskEffectDrawable mSharedDrawable;
    private ColorStateList mStateIconTintList;
    private static final Typeface MEDIUM_TYPEFACE = Typeface.create("sans-serif-medium", 0);
    private static final Drawable DEFAULT_TRANSPARENT_HOLDER = new ColorDrawable(0);
    private static final int[] STATE_SELECTED = {16842910, 16842913};
    private static final int[] STATE_DISABLED = {-16842910};
    private final View.AccessibilityDelegate mAccessibilityDelegate = new AnonymousClass1();
    private int mHorizontalItemCount = 0;
    private boolean mHorizontalHasText = false;
    private boolean mHorizontalTitleForceTwoLines = false;
    private boolean mHorizontalTitleForceTwoLinesComputed = false;
    private boolean mIsAdaptiveFontSize = false;
    private boolean mIsFixedFontSize = false;
    private int mPrivateFlags = 0;
    private Set<Integer> mGroupSets = null;

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.MixedPopupListAdapter$1, reason: invalid class name */
    public class AnonymousClass1 extends View.AccessibilityDelegate {
        public AnonymousClass1() {
        }

        @Override // android.view.View.AccessibilityDelegate
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName("");
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.MixedPopupListAdapter$2, reason: invalid class name */
    public class AnonymousClass2 extends View.AccessibilityDelegate {
        final /* synthetic */ int val$dataIndex;

        public AnonymousClass2(int i3) {
            this.val$dataIndex = i3;
        }

        @Override // android.view.View.AccessibilityDelegate
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
            int i3;
            PopupListItem popupListItem;
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
            if (MixedPopupListAdapter.o(MixedPopupListAdapter.this) == null || (i3 = this.val$dataIndex) < 0 || i3 >= MixedPopupListAdapter.o(MixedPopupListAdapter.this).size() || (popupListItem = (PopupListItem) MixedPopupListAdapter.o(MixedPopupListAdapter.this).get(this.val$dataIndex)) == null) {
                return;
            }
            String strR = MixedPopupListAdapter.r(MixedPopupListAdapter.this, popupListItem);
            if (!TextUtils.isEmpty(strR)) {
                accessibilityNodeInfo.setContentDescription(strR);
            }
            if (popupListItem.hasSubMenu()) {
                accessibilityNodeInfo.setClassName(COUIAccessibilityUtil.BUTTON_CLASS_NAME);
                accessibilityNodeInfo.setCheckable(false);
                accessibilityNodeInfo.setChecked(false);
            } else if (popupListItem.getStateIconId() == 0 && popupListItem.getStateIcon() == null && popupListItem.isChecked()) {
                accessibilityNodeInfo.setCheckable(true);
                accessibilityNodeInfo.setChecked(popupListItem.isChecked());
                accessibilityNodeInfo.setClassName("android.widget.CheckBox");
            } else {
                accessibilityNodeInfo.setClassName(COUIAccessibilityUtil.BUTTON_CLASS_NAME);
                accessibilityNodeInfo.setCheckable(false);
            }
            accessibilityNodeInfo.setEnabled(popupListItem.isEnable());
        }

        @Override // android.view.View.AccessibilityDelegate
        public boolean performAccessibilityAction(View view, int i3, Bundle bundle) {
            int i4;
            PopupListItem popupListItem;
            if (i3 != 16) {
                return super.performAccessibilityAction(view, i3, bundle);
            }
            if (MixedPopupListAdapter.p(MixedPopupListAdapter.this) == null || MixedPopupListAdapter.o(MixedPopupListAdapter.this) == null || (i4 = this.val$dataIndex) < 0 || i4 >= MixedPopupListAdapter.o(MixedPopupListAdapter.this).size() || (popupListItem = (PopupListItem) MixedPopupListAdapter.o(MixedPopupListAdapter.this).get(this.val$dataIndex)) == null) {
                return false;
            }
            MixedPopupListAdapter.p(MixedPopupListAdapter.this).onItemClick(null, view, this.val$dataIndex, popupListItem.getId());
            return true;
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private final FrameLayout mContainer;

        public CustomViewHolder(FrameLayout frameLayout) {
            super(frameLayout);
            this.mContainer = frameLayout;
            frameLayout.setClickable(true);
            frameLayout.setFocusable(true);
            frameLayout.setFocusableInTouchMode(false);
            frameLayout.setDefaultFocusHighlightEnabled(false);
        }

        public void bind(PopupListItem popupListItem) {
            View customItemView = popupListItem.getCustomItemView();
            if (customItemView == null) {
                Log.e(MixedPopupListAdapter.TAG, "CustomViewHolder: customItemView is null for MENU_ITEM_TYPE_CUSTOM item");
                this.mContainer.removeAllViews();
                this.mContainer.setEnabled(popupListItem.isEnable());
                return;
            }
            ViewParent parent = customItemView.getParent();
            FrameLayout frameLayout = this.mContainer;
            if (parent == frameLayout) {
                frameLayout.setEnabled(popupListItem.isEnable());
                return;
            }
            ViewParent parent2 = customItemView.getParent();
            if (parent2 instanceof ViewGroup) {
                Log.w(MixedPopupListAdapter.TAG, "CustomViewHolder: customView already has a parent (" + parent2 + "). Make sure each PopupListItem owns a unique customView instance to avoid display artifacts.");
                ((ViewGroup) parent2).removeView(customItemView);
            }
            this.mContainer.removeAllViews();
            this.mContainer.addView(customItemView, new FrameLayout.LayoutParams(-1, -2));
            this.mContainer.setEnabled(popupListItem.isEnable());
        }
    }

    public class DividerViewHolder extends RecyclerView.ViewHolder {
        public DividerViewHolder(View view) {
            super(view);
        }

        public void bind(PopupListItem popupListItem, int i3) {
            ViewCompat.setImportantForAccessibility(this.itemView, 2);
            COUIDarkModeUtil.setForceDarkAllow(this.itemView, false);
            int iG = MixedPopupListAdapter.g(MixedPopupListAdapter.this);
            int iE = (iG - MixedPopupListAdapter.e(MixedPopupListAdapter.this)) / 2;
            int iF = MixedPopupListAdapter.f(MixedPopupListAdapter.this);
            this.itemView.setLayoutParams(new RecyclerView.LayoutParams(-1, iG));
            this.itemView.setBackground(new InsetDrawable((Drawable) new ColorDrawable(COUIContextUtil.getAttrColor(MixedPopupListAdapter.d(MixedPopupListAdapter.this), R.attr.couiColorDivider)), iF, iE, iF, iE));
            this.itemView.setFocusable(false);
            this.itemView.setClickable(false);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final float mBaseTitleTextSize;
        private TextView mTitle;

        public HeaderViewHolder(View view) {
            super(view);
            TextView textView = (TextView) view.findViewById(com.support.poplist.R.id.popup_list_window_header_item_title);
            this.mTitle = textView;
            this.mBaseTitleTextSize = textView.getTextSize();
        }

        public void bind(PopupListItem popupListItem) {
            this.mTitle.setText(popupListItem.getTitle());
            if (!TextUtils.isEmpty(popupListItem.getTitleContentDescription())) {
                this.mTitle.setContentDescription(popupListItem.getTitleContentDescription());
            }
            if (MixedPopupListAdapter.n(MixedPopupListAdapter.this)) {
                this.mTitle.setTextSize(1, 12.0f);
            } else {
                this.mTitle.setTextSize(0, this.mBaseTitleTextSize);
                if (MixedPopupListAdapter.m(MixedPopupListAdapter.this)) {
                    COUIChangeTextUtil.adaptFontSize(this.mTitle, 4);
                } else {
                    COUIChangeTextUtil.adaptFontSize(this.mTitle, 5);
                }
            }
            this.itemView.setClickable(false);
        }
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder {
        private int mBoundDataIndex;
        private boolean mBoundHasTitle;
        private PopupListItem mBoundItem;
        private ImageView mIcon;
        final View.OnLayoutChangeListener mLayoutChangeListener;
        private TextView mTitle;

        /* JADX INFO: renamed from: com.coui.appcompat.poplist.MixedPopupListAdapter$HorizontalViewHolder$1, reason: invalid class name */
        public class AnonymousClass1 implements View.OnLayoutChangeListener {
            final /* synthetic */ View val$itemView;
            final /* synthetic */ MixedPopupListAdapter val$this$0;

            public AnonymousClass1(MixedPopupListAdapter mixedPopupListAdapter, View view) {
                this.val$this$0 = mixedPopupListAdapter;
                this.val$itemView = view;
            }

            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View view, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
                if (HorizontalViewHolder.c(HorizontalViewHolder.this) == null) {
                    return;
                }
                HorizontalViewHolder horizontalViewHolder = HorizontalViewHolder.this;
                MixedPopupListAdapter.v(MixedPopupListAdapter.this, this.val$itemView, HorizontalViewHolder.c(horizontalViewHolder), HorizontalViewHolder.a(HorizontalViewHolder.this));
                if (!HorizontalViewHolder.b(HorizontalViewHolder.this) || MixedPopupListAdapter.l(MixedPopupListAdapter.this) || i5 - i3 <= 0) {
                    return;
                }
                boolean zK = MixedPopupListAdapter.k(MixedPopupListAdapter.this);
                HorizontalViewHolder horizontalViewHolder2 = HorizontalViewHolder.this;
                MixedPopupListAdapter.D(MixedPopupListAdapter.this, HorizontalViewHolder.d(horizontalViewHolder2), this.val$itemView);
                if (!MixedPopupListAdapter.l(MixedPopupListAdapter.this) || zK == MixedPopupListAdapter.k(MixedPopupListAdapter.this)) {
                    return;
                }
                MixedPopupListAdapter.this.notifyDataSetChanged();
            }
        }

        public HorizontalViewHolder(View view) {
            super(view);
            this.mIcon = (ImageView) view.findViewById(com.support.poplist.R.id.popup_list_window_horizontal_item_icon);
            TextView textView = (TextView) view.findViewById(com.support.poplist.R.id.popup_list_window_horizontal_item_title);
            this.mTitle = textView;
            textView.getPaint().setLinearText(true);
            this.mTitle.getPaint().setSubpixelText(true);
            view.setClickable(true);
            view.setFocusable(true);
            view.setFocusableInTouchMode(false);
            view.setDefaultFocusHighlightEnabled(false);
            this.mLayoutChangeListener = new AnonymousClass1(MixedPopupListAdapter.this, view);
        }

        public static /* bridge */ /* synthetic */ int a(HorizontalViewHolder horizontalViewHolder) {
            return horizontalViewHolder.mBoundDataIndex;
        }

        public static /* bridge */ /* synthetic */ boolean b(HorizontalViewHolder horizontalViewHolder) {
            return horizontalViewHolder.mBoundHasTitle;
        }

        public static /* bridge */ /* synthetic */ PopupListItem c(HorizontalViewHolder horizontalViewHolder) {
            return horizontalViewHolder.mBoundItem;
        }

        public static /* bridge */ /* synthetic */ TextView d(HorizontalViewHolder horizontalViewHolder) {
            return horizontalViewHolder.mTitle;
        }

        public void bind(PopupListItem popupListItem, int i3) {
            this.mBoundItem = popupListItem;
            this.mBoundDataIndex = i3;
            this.mBoundHasTitle = !TextUtils.isEmpty(popupListItem.getTitle());
            this.itemView.removeOnLayoutChangeListener(this.mLayoutChangeListener);
            this.itemView.addOnLayoutChangeListener(this.mLayoutChangeListener);
            boolean z2 = this.mBoundHasTitle;
            if (z2) {
                MixedPopupListAdapter.q(MixedPopupListAdapter.this, this.mTitle);
            }
            MixedPopupListAdapter.t(MixedPopupListAdapter.this, this.itemView, i3, z2, z2 ? MixedPopupListAdapter.s(MixedPopupListAdapter.this, this.mTitle) : 0);
            ViewGroup.LayoutParams layoutParams = this.mIcon.getLayoutParams();
            MixedPopupListAdapter mixedPopupListAdapter = MixedPopupListAdapter.this;
            int i4 = z2 ? MixedPopupListAdapter.i(mixedPopupListAdapter) : MixedPopupListAdapter.h(mixedPopupListAdapter);
            layoutParams.width = i4;
            layoutParams.height = i4;
            this.mIcon.setLayoutParams(layoutParams);
            MixedPopupListAdapter.z(MixedPopupListAdapter.this, this.mIcon, popupListItem);
            if (z2) {
                MixedPopupListAdapter.D(MixedPopupListAdapter.this, this.mTitle, this.itemView);
                MixedPopupListAdapter.B(MixedPopupListAdapter.this, this.mTitle, popupListItem);
                this.mTitle.setVisibility(0);
                ViewGroup.LayoutParams layoutParams2 = this.mTitle.getLayoutParams();
                if (layoutParams2 instanceof ViewGroup.MarginLayoutParams) {
                    ((ViewGroup.MarginLayoutParams) layoutParams2).topMargin = MixedPopupListAdapter.j(MixedPopupListAdapter.this);
                }
            } else {
                this.mTitle.setVisibility(8);
            }
            this.itemView.setEnabled(popupListItem.isEnable());
        }
    }

    public class VerticalViewHolder extends RecyclerView.ViewHolder {
        private int mBoundDataIndex;
        private PopupListItem mBoundItem;
        private CheckBox mCheckbox;
        private TextView mDescription;
        private Space mHintEndGap;
        private LinearLayout mHintLayout;
        private ImageView mIcon;
        final View.OnLayoutChangeListener mLayoutChangeListener;
        private TextView mTitle;
        private Space mTitleEndGap;

        /* JADX INFO: renamed from: com.coui.appcompat.poplist.MixedPopupListAdapter$VerticalViewHolder$1, reason: invalid class name */
        public class AnonymousClass1 implements View.OnLayoutChangeListener {
            final /* synthetic */ View val$itemView;
            final /* synthetic */ MixedPopupListAdapter val$this$0;

            public AnonymousClass1(MixedPopupListAdapter mixedPopupListAdapter, View view) {
                this.val$this$0 = mixedPopupListAdapter;
                this.val$itemView = view;
            }

            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View view, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
                if (VerticalViewHolder.b(VerticalViewHolder.this) == null) {
                    return;
                }
                VerticalViewHolder verticalViewHolder = VerticalViewHolder.this;
                MixedPopupListAdapter.v(MixedPopupListAdapter.this, this.val$itemView, VerticalViewHolder.b(verticalViewHolder), VerticalViewHolder.a(VerticalViewHolder.this));
            }
        }

        public VerticalViewHolder(View view) {
            super(view);
            this.mIcon = (ImageView) view.findViewById(com.support.poplist.R.id.popup_list_window_item_icon);
            TextView textView = (TextView) view.findViewById(com.support.poplist.R.id.popup_list_window_item_title);
            this.mTitle = textView;
            textView.getPaint().setLinearText(true);
            this.mTitle.getPaint().setSubpixelText(true);
            this.mDescription = (TextView) view.findViewById(com.support.poplist.R.id.popup_list_window_item_description);
            this.mTitleEndGap = (Space) view.findViewById(com.support.poplist.R.id.popup_list_window_item_title_end_gap);
            this.mHintLayout = (LinearLayout) view.findViewById(com.support.poplist.R.id.popup_list_window_item_hint_layout);
            this.mHintEndGap = (Space) view.findViewById(com.support.poplist.R.id.popup_list_window_item_hint_end_gap);
            CheckBox checkBox = (CheckBox) view.findViewById(com.support.poplist.R.id.popup_list_window_item_state_icon);
            this.mCheckbox = checkBox;
            if (checkBox != null) {
                checkBox.setAccessibilityDelegate(MixedPopupListAdapter.c(MixedPopupListAdapter.this));
                this.mCheckbox.setBackground(null);
            }
            view.setClickable(true);
            view.setFocusable(true);
            view.setFocusableInTouchMode(false);
            view.setDefaultFocusHighlightEnabled(false);
            this.mLayoutChangeListener = new AnonymousClass1(MixedPopupListAdapter.this, view);
        }

        public static /* bridge */ /* synthetic */ int a(VerticalViewHolder verticalViewHolder) {
            return verticalViewHolder.mBoundDataIndex;
        }

        public static /* bridge */ /* synthetic */ PopupListItem b(VerticalViewHolder verticalViewHolder) {
            return verticalViewHolder.mBoundItem;
        }

        public static /* bridge */ /* synthetic */ Space c(VerticalViewHolder verticalViewHolder) {
            return verticalViewHolder.mHintEndGap;
        }

        public static /* bridge */ /* synthetic */ Space d(VerticalViewHolder verticalViewHolder) {
            return verticalViewHolder.mTitleEndGap;
        }

        public void bind(PopupListItem popupListItem, int i3) {
            this.mBoundItem = popupListItem;
            this.mBoundDataIndex = i3;
            this.itemView.removeOnLayoutChangeListener(this.mLayoutChangeListener);
            this.itemView.addOnLayoutChangeListener(this.mLayoutChangeListener);
            MixedPopupListAdapter.u(MixedPopupListAdapter.this, this.itemView, i3);
            MixedPopupListAdapter.z(MixedPopupListAdapter.this, this.mIcon, popupListItem);
            MixedPopupListAdapter.C(MixedPopupListAdapter.this, this.mTitle, popupListItem, i3);
            MixedPopupListAdapter.w(MixedPopupListAdapter.this, this.mDescription, popupListItem);
            MixedPopupListAdapter.x(MixedPopupListAdapter.this, this, popupListItem);
            MixedPopupListAdapter.y(MixedPopupListAdapter.this, this.mHintLayout, popupListItem);
            MixedPopupListAdapter.A(MixedPopupListAdapter.this, this.mCheckbox, popupListItem);
            this.itemView.setEnabled(popupListItem.isEnable());
        }
    }

    public MixedPopupListAdapter(Context context) {
        this.mContext = context;
        initResources();
    }

    public static /* bridge */ /* synthetic */ void A(MixedPopupListAdapter mixedPopupListAdapter, CheckBox checkBox, PopupListItem popupListItem) {
        mixedPopupListAdapter.setStateIcon(checkBox, popupListItem);
    }

    public static /* bridge */ /* synthetic */ void B(MixedPopupListAdapter mixedPopupListAdapter, TextView textView, PopupListItem popupListItem) {
        mixedPopupListAdapter.setTitleForHorizontal(textView, popupListItem);
    }

    public static /* bridge */ /* synthetic */ void C(MixedPopupListAdapter mixedPopupListAdapter, TextView textView, PopupListItem popupListItem, int i3) {
        mixedPopupListAdapter.setTitleForVertical(textView, popupListItem, i3);
    }

    public static /* bridge */ /* synthetic */ void D(MixedPopupListAdapter mixedPopupListAdapter, TextView textView, View view) {
        mixedPopupListAdapter.updateHorizontalTitleForceTwoLine(textView, view);
    }

    public static /* synthetic */ boolean a(MixedPopupListAdapter mixedPopupListAdapter, int i3, PopupListItem popupListItem, View view) {
        return mixedPopupListAdapter.mOnItemLongClickListener.onItemLongClick(null, view, i3, popupListItem.getId());
    }

    private void applyHorizontalTitleTextAppearance(TextView textView) {
        textView.setTextAppearance(R.style.couiTextBodyXS);
    }

    public static /* synthetic */ void b(MixedPopupListAdapter mixedPopupListAdapter, int i3, PopupListItem popupListItem, View view) {
        AdapterView.OnItemClickListener onItemClickListener = mixedPopupListAdapter.mOnItemClickListener;
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(null, view, i3, popupListItem.getId());
        }
    }

    /* JADX WARN: Code duplicated, block: B:20:0x004b  */
    private String buildAccessibilityContentDescription(PopupListItem popupListItem) {
        boolean z2;
        if (popupListItem == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        String titleContentDescription = !TextUtils.isEmpty(popupListItem.getTitleContentDescription()) ? popupListItem.getTitleContentDescription() : popupListItem.getTitle();
        String str = "";
        if (TextUtils.isEmpty(titleContentDescription)) {
            titleContentDescription = "";
        }
        sb.append(titleContentDescription);
        if (!popupListItem.isEnable()) {
            sb.append(" - 已停用");
            return sb.toString();
        }
        if (popupListItem.getHintType() == 0) {
            z2 = true;
            if (!TextUtils.isEmpty(popupListItem.getRedDotText())) {
                str = " - 有新消息";
            } else if (popupListItem.getRedDotAmount() > 0) {
                str = " - 有" + popupListItem.getRedDotAmount() + "条新消息";
            } else if (popupListItem.getRedDotAmount() == 0) {
                str = " - 有新消息";
            }
        } else {
            z2 = false;
        }
        if (popupListItem.hasSubMenu()) {
            sb.append(str);
            sb.append(" - 按钮 - 已折叠 - 点按两次即可展开");
            return sb.toString();
        }
        if (popupListItem.getStateIconId() == 0 && popupListItem.getStateIcon() == null && !popupListItem.hasSubMenu()) {
            if (popupListItem.isChecked()) {
                sb.append(" - 已选中 - 点按两次即可切换");
                return sb.toString();
            }
            sb.append(" - 未选中 - 点按两次即可切换");
            return sb.toString();
        }
        if (!z2 || TextUtils.isEmpty(str)) {
            sb.append(" - 点按两次即可激活");
            return sb.toString();
        }
        sb.append(str);
        sb.append(" - 点按两次即可激活");
        return sb.toString();
    }

    public static /* bridge */ /* synthetic */ View.AccessibilityDelegate c(MixedPopupListAdapter mixedPopupListAdapter) {
        return mixedPopupListAdapter.mAccessibilityDelegate;
    }

    private int computeHorizontalTextUniformWidth(TextView textView) {
        List<PopupListItem> list = this.mMenuItemList;
        if (list == null || textView == null) {
            return Math.max(this.mHorizontalItemMinWidth, this.mHorizontalItemMinWidthWithText);
        }
        float fMax = 0.0f;
        for (PopupListItem popupListItem : list) {
            if (popupListItem != null && popupListItem.getItemType() == 4 && !TextUtils.isEmpty(popupListItem.getTitle())) {
                fMax = Math.max(fMax, textView.getPaint().measureText(popupListItem.getTitle()));
            }
        }
        int iCeil = (int) Math.ceil(fMax);
        int i3 = this.mHorizontalItemTextPaddingHorizontal;
        return Math.max(Math.max(this.mHorizontalItemMinWidth, this.mHorizontalItemMinWidthWithText), Math.max(iCeil + (i3 * 2), this.mHorizontalItemIconSizeWithText + (i3 * 2)));
    }

    private void configHorizontalItemLayout(View view, int i3, boolean z2, int i4) {
        List<PopupListItem> list;
        int iMax;
        if (view == null || (list = this.mMenuItemList) == null || i3 < 0 || i3 >= list.size()) {
            return;
        }
        int i5 = -1;
        int i6 = 0;
        for (int i7 = 0; i7 < this.mMenuItemList.size(); i7++) {
            PopupListItem popupListItem = this.mMenuItemList.get(i7);
            if (popupListItem != null && popupListItem.getItemType() == 4) {
                if (i7 == i3) {
                    i5 = i6;
                }
                i6++;
            }
        }
        if (i5 < 0 || i6 <= 0) {
            return;
        }
        int iMax2 = z2 ? this.mHorizontalItemTextEdgeMargin : this.mHorizontalItemIconOnlyEdgeMargin;
        int i8 = z2 ? this.mHorizontalItemTextSpacingDefault : this.mHorizontalItemIconOnlySpacingDefault;
        int i9 = this.mHorizontalItemSpacingMin;
        int horizontalContainerWidth = getHorizontalContainerWidth(view);
        int iMax3 = i6 <= 1 ? 0 : i8;
        if (i6 > 1 && horizontalContainerWidth > 0) {
            int i10 = i6 - 1;
            int i11 = (horizontalContainerWidth - (iMax2 * 2)) / i10;
            int iMin = Math.min(i8, i11);
            iMax3 = i11 >= i9 ? Math.max(i9, iMin) : Math.max(0, iMin);
            iMax2 = Math.min(iMax2, Math.max(0, (horizontalContainerWidth - (i10 * iMax3)) / 2));
        }
        if (i6 == 1) {
            iMax = iMax2;
        } else {
            int i12 = iMax2 * 2;
            float f3 = i6;
            int iRound = Math.round((((i6 - 1) * iMax3) + i12) / f3);
            iMax2 = Math.max(0, Math.round(((iMax2 * i6) - (i5 * (i12 - iMax3))) / f3));
            iMax = Math.max(0, iRound - iMax2);
        }
        int i13 = z2 ? this.mHorizontalItemTextPaddingHorizontal : this.mHorizontalItemIconOnlyPaddingHorizontal;
        int i14 = z2 ? this.mHorizontalItemTextPaddingVertical : 0;
        view.setPadding(i13, i14, i13, i14);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) (view.getLayoutParams() instanceof RecyclerView.LayoutParams ? view.getLayoutParams() : new RecyclerView.LayoutParams(-1, -2));
        ((ViewGroup.MarginLayoutParams) layoutParams).width = -1;
        ((ViewGroup.MarginLayoutParams) layoutParams).height = z2 ? -2 : this.mHorizontalItemIconOnlyHeight;
        ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = 0;
        ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = 0;
        layoutParams.setMarginStart(iMax2);
        layoutParams.setMarginEnd(iMax);
        view.setLayoutParams(layoutParams);
        if (z2) {
            view.setMinimumWidth(Math.max(Math.max(this.mHorizontalItemMinWidth, this.mHorizontalItemMinWidthWithText), i4));
            view.setMinimumHeight(this.mHorizontalItemTextMinHeight);
        } else {
            view.setMinimumWidth(this.mHorizontalItemIconOnlyWidth);
            view.setMinimumHeight(this.mHorizontalItemIconOnlyHeight);
        }
    }

    private void configItemVerticalPadding(View view, int i3) {
        if (view == null) {
            return;
        }
        view.setMinimumHeight(this.mPopupListItemMinHeight);
        view.setPadding(view.getPaddingStart(), this.mPopupListItemPaddingVertical, view.getPaddingEnd(), this.mPopupListItemPaddingVertical);
    }

    private void configStateEffectBackground(View view, PopupListItem popupListItem, int i3) {
        ListItemMaskEffectDrawable listItemMaskEffectDrawable;
        COUIMaskEffectDrawable cOUIMaskEffectDrawable;
        if (popupListItem != null && popupListItem.getItemType() == 4) {
            if (view.getBackground() instanceof COUIMaskEffectDrawable) {
                cOUIMaskEffectDrawable = (COUIMaskEffectDrawable) view.getBackground();
            } else {
                cOUIMaskEffectDrawable = new COUIMaskEffectDrawable(this.mContext, 1);
                cOUIMaskEffectDrawable.enableFocusedState(true);
                cOUIMaskEffectDrawable.enableSelectedState(false);
                cOUIMaskEffectDrawable.setIsRoundStyle(false);
                cOUIMaskEffectDrawable.setTouchExited();
                view.setBackground(cOUIMaskEffectDrawable);
            }
            configureHorizontalItemMaskPath(cOUIMaskEffectDrawable, view, TextUtils.isEmpty(popupListItem.getTitle()));
            view.setSelected(popupListItem.isChecked());
            if (view.getBackground() instanceof COUIMaskEffectDrawable) {
                COUIMaskEffectDrawable cOUIMaskEffectDrawable2 = (COUIMaskEffectDrawable) view.getBackground();
                if (popupListItem.isChecked()) {
                    cOUIMaskEffectDrawable2.setFocusStateLocked(true, true, true);
                    return;
                } else {
                    cOUIMaskEffectDrawable2.setFocusStateLocked(false, view.isFocused(), true);
                    return;
                }
            }
            return;
        }
        if (popupListItem != null && popupListItem.hasSubMenu() && popupListItem.getGroupState() != 2 && !(view.getBackground() instanceof ListItemMaskEffectDrawable)) {
            view.setBackground(new ListItemMaskEffectDrawable(this.mContext, 1, null));
        } else if (popupListItem != null && popupListItem.hasSubMenu() && popupListItem.getGroupState() == 2 && (listItemMaskEffectDrawable = this.mSharedDrawable) != null) {
            view.setBackground(listItemMaskEffectDrawable.getChild());
        } else if (!(view.getBackground() instanceof COUIMaskEffectDrawable)) {
            COUIMaskEffectDrawable cOUIMaskEffectDrawable3 = new COUIMaskEffectDrawable(this.mContext, 1);
            cOUIMaskEffectDrawable3.enableFocusedState(true);
            cOUIMaskEffectDrawable3.setIsRoundStyle(false);
            cOUIMaskEffectDrawable3.setTouchExited();
            configureMaskPath(cOUIMaskEffectDrawable3, view, i3);
            view.setBackground(cOUIMaskEffectDrawable3);
        }
        Drawable background = view.getBackground();
        if (background instanceof COUIMaskEffectDrawable) {
            configureMaskPath((COUIMaskEffectDrawable) background, view, i3);
        }
    }

    private void configureHorizontalItemMaskPath(COUIMaskEffectDrawable cOUIMaskEffectDrawable, View view, boolean z2) {
        boolean z3;
        float dimensionPixelSize;
        float f3;
        Context context = view.getContext();
        if (COUIVersionUtil.getOSVersionCode() <= 37 || !UIUtil.confirmLevelAnim(UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN)) {
            z3 = false;
            dimensionPixelSize = z2 ? context.getResources().getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_horizontal_mask_radius_icon_only) : context.getResources().getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_horizontal_mask_radius_with_text);
            f3 = 0.0f;
        } else {
            z3 = true;
            f3 = 3.0f;
            dimensionPixelSize = z2 ? context.getResources().getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_horizontal_mask_radius_icon_only_16_1) : context.getResources().getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_horizontal_mask_radius_with_text_16_1);
        }
        int width = view.getWidth();
        int height = view.getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        if (z2) {
            height = Math.min(height, this.mHorizontalItemIconOnlyHeight);
        }
        RectF rectF = new RectF(0.0f, 0.0f, width, height);
        if (!z3) {
            cOUIMaskEffectDrawable.setMaskPath(null);
            cOUIMaskEffectDrawable.setMaskRect(rectF, dimensionPixelSize, dimensionPixelSize);
        } else {
            Path path = new Path();
            COUIShapePath.getSmoothRoundRectPath(path, rectF, dimensionPixelSize, f3);
            cOUIMaskEffectDrawable.setMaskPath(path);
        }
    }

    private void configureMaskPath(COUIMaskEffectDrawable cOUIMaskEffectDrawable, View view, int i3) {
        float f3;
        Context context = view.getContext();
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_mask_margin_horizontal);
        int dimensionPixelSize2 = context.getResources().getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_vertical_mask_radius);
        int width = view.getWidth();
        int height = view.getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        if (height == dimensionPixelSize2 * 2) {
            f3 = 1.0f;
        } else {
            if (COUIVersionUtil.getOSVersionCode() > 37) {
                dimensionPixelSize2 = context.getResources().getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_vertical_mask_radius_16_1);
            }
            f3 = 3.0f;
        }
        RectF rectF = new RectF(dimensionPixelSize, 0, width - dimensionPixelSize, height);
        Path path = new Path();
        COUIShapePath.getSmoothRoundRectPath(path, rectF, dimensionPixelSize2, f3);
        cOUIMaskEffectDrawable.setMaskPath(path);
    }

    public static /* bridge */ /* synthetic */ Context d(MixedPopupListAdapter mixedPopupListAdapter) {
        return mixedPopupListAdapter.mContext;
    }

    public static /* bridge */ /* synthetic */ int e(MixedPopupListAdapter mixedPopupListAdapter) {
        return mixedPopupListAdapter.mDefaultDividerHeight;
    }

    public static /* bridge */ /* synthetic */ int f(MixedPopupListAdapter mixedPopupListAdapter) {
        return mixedPopupListAdapter.mDefaultDividerMarginHorizontal;
    }

    public static /* bridge */ /* synthetic */ int g(MixedPopupListAdapter mixedPopupListAdapter) {
        return mixedPopupListAdapter.mGroupDividerTotalHeight;
    }

    private View.AccessibilityDelegate getAccessibilityDelegate(int i3) {
        return new AnonymousClass2(i3);
    }

    private int getHorizontalContainerWidth(View view) {
        if (view != null) {
            Object parent = view.getParent();
            if (parent instanceof View) {
                View view2 = (View) parent;
                int width = view2.getWidth();
                if (width <= 0) {
                    width = view2.getMeasuredWidth();
                }
                if (width > 0) {
                    return (width - view2.getPaddingStart()) - view2.getPaddingEnd();
                }
            }
        }
        RecyclerView recyclerView = this.mAttachedRecyclerView;
        if (recyclerView == null) {
            return 0;
        }
        int width2 = recyclerView.getWidth();
        if (width2 <= 0) {
            width2 = this.mAttachedRecyclerView.getMeasuredWidth();
        }
        if (width2 > 0) {
            return (width2 - this.mAttachedRecyclerView.getPaddingStart()) - this.mAttachedRecyclerView.getPaddingEnd();
        }
        return 0;
    }

    private View getRedDotHint(PopupListItem popupListItem) {
        COUIHintRedDot cOUIHintRedDot = new COUIHintRedDot(this.mContext, null, com.support.reddot.R.style.Widget_COUI_COUIHintRedDot_Small);
        if (!TextUtils.isEmpty(popupListItem.getRedDotText())) {
            cOUIHintRedDot.setPointMode(2);
            cOUIHintRedDot.setPointText(popupListItem.getRedDotText());
            return cOUIHintRedDot;
        }
        cOUIHintRedDot.setPointNumber(popupListItem.getRedDotAmount());
        int redDotAmount = popupListItem.getRedDotAmount();
        if (redDotAmount == -1) {
            cOUIHintRedDot.setPointMode(0);
            return cOUIHintRedDot;
        }
        if (redDotAmount != 0) {
            cOUIHintRedDot.setPointMode(2);
            return cOUIHintRedDot;
        }
        cOUIHintRedDot.setPointMode(1);
        return cOUIHintRedDot;
    }

    private int getTintColorByState(ColorStateList colorStateList, PopupListItem popupListItem) {
        return getTintColorByState(colorStateList, popupListItem, false);
    }

    public static /* bridge */ /* synthetic */ int h(MixedPopupListAdapter mixedPopupListAdapter) {
        return mixedPopupListAdapter.mHorizontalItemIconSizeIconOnly;
    }

    private boolean hasIcon() {
        return (this.mPrivateFlags & 1) != 0;
    }

    public static /* bridge */ /* synthetic */ int i(MixedPopupListAdapter mixedPopupListAdapter) {
        return mixedPopupListAdapter.mHorizontalItemIconSizeWithText;
    }

    private void initResources() {
        Resources resources = this.mContext.getResources();
        this.mDefaultDividerHeight = resources.getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_divider_height);
        this.mPopupListItemPaddingVertical = resources.getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_item_padding_top_and_bottom);
        this.mPopupListItemMinHeight = resources.getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_item_min_height);
        this.mDefaultDividerMarginHorizontal = resources.getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_default_divider_margin_horizontal);
        this.mGroupDividerTotalHeight = resources.getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_group_divider_total_height);
        this.mHorizontalItemMinWidth = resources.getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_horizontal_item_min_width);
        this.mHorizontalItemMinWidthWithText = resources.getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_horizontal_item_min_width_with_text);
        this.mHorizontalItemIconOnlyWidth = resources.getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_horizontal_item_icon_only_width);
        this.mHorizontalItemIconOnlyHeight = resources.getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_horizontal_item_icon_only_height);
        this.mHorizontalItemTextMinHeight = resources.getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_horizontal_item_text_min_height);
        this.mHorizontalItemIconOnlyPaddingHorizontal = resources.getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_horizontal_item_icon_only_padding_horizontal);
        this.mHorizontalItemTextPaddingHorizontal = resources.getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_horizontal_item_text_padding_horizontal);
        this.mHorizontalItemTextPaddingVertical = resources.getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_horizontal_item_text_padding_vertical);
        this.mHorizontalItemIconOnlyEdgeMargin = resources.getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_horizontal_item_icon_only_edge_margin);
        this.mHorizontalItemTextEdgeMargin = resources.getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_horizontal_item_text_edge_margin);
        this.mHorizontalItemIconOnlySpacingDefault = resources.getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_horizontal_item_icon_only_spacing_default);
        this.mHorizontalItemTextSpacingDefault = resources.getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_horizontal_item_text_spacing_default);
        this.mHorizontalItemSpacingMin = resources.getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_horizontal_item_spacing_min);
        this.mHorizontalItemIconSizeIconOnly = resources.getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_horizontal_item_icon_size_icon_only);
        this.mHorizontalItemIconSizeWithText = resources.getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_horizontal_item_icon_size_with_text);
        this.mHorizontalItemIconTitleGap = resources.getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_horizontal_item_icon_title_gap);
        this.mItemTintList = UIUtil.getColorStateListCompatNoCache(this.mContext, com.support.poplist.R.color.coui_popup_list_window_item_tint_selector);
        this.mStateIconTintList = UIUtil.getColorStateListCompatNoCache(this.mContext, com.support.poplist.R.color.coui_popup_list_window_item_status_icon_tint_selector);
        this.mAlertColor = COUIContextUtil.getAttrColor(this.mContext, R.attr.couiColorError, R.color.coui_color_error);
        this.mDescriptionColor = COUIContextUtil.getAttrColor(this.mContext, R.attr.couiColorLabelSecondary, R.color.coui_color_secondary_neutral);
    }

    private boolean isDataIndex(int i3) {
        List<PopupListItem> list = this.mMenuItemList;
        if (list != null && !list.isEmpty()) {
            Set<Integer> set = this.mGroupSets;
            if (set != null && !set.isEmpty()) {
                boolean z2 = false;
                int i4 = 0;
                int i5 = 0;
                boolean z3 = false;
                for (int i6 = 0; i6 < this.mMenuItemList.size(); i6++) {
                    PopupListItem popupListItem = this.mMenuItemList.get(i6);
                    if (popupListItem != null) {
                        if (!isHorizontalItem(popupListItem)) {
                            if (z2 && z3) {
                                if (i4 + i5 == i3) {
                                    return false;
                                }
                                i5++;
                                z2 = false;
                            }
                            if (this.mGroupSets.contains(Integer.valueOf(i6)) && i6 > 0) {
                                if (i4 + i5 == i3) {
                                    return false;
                                }
                                i5++;
                            }
                            if (i4 + i5 == i3) {
                                return true;
                            }
                            i4++;
                            z3 = false;
                        } else {
                            if (i4 + i5 == i3) {
                                return true;
                            }
                            i4++;
                            z2 = true;
                            z3 = true;
                        }
                    }
                }
                return false;
            }
            boolean z4 = false;
            int i7 = 0;
            int i8 = 0;
            boolean z5 = false;
            for (int i9 = 0; i9 < this.mMenuItemList.size(); i9++) {
                PopupListItem popupListItem2 = this.mMenuItemList.get(i9);
                if (popupListItem2 != null) {
                    if (!isHorizontalItem(popupListItem2)) {
                        if (z4 && z5) {
                            if (i7 + i8 == i3) {
                                return false;
                            }
                            i8++;
                            z4 = false;
                        }
                        if (i7 + i8 == i3) {
                            return true;
                        }
                        i7++;
                        z5 = false;
                    } else {
                        if (i7 + i8 == i3) {
                            return true;
                        }
                        i7++;
                        z4 = true;
                        z5 = true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isGroupIndex(int i3) {
        if (i3 > 0 && !isHeaderPosition(i3)) {
            Set<Integer> set = this.mGroupSets;
            if (set != null && !set.isEmpty()) {
                boolean z2 = false;
                int i4 = 0;
                int i5 = 0;
                boolean z3 = false;
                for (int i6 = 0; i6 < this.mMenuItemList.size(); i6++) {
                    PopupListItem popupListItem = this.mMenuItemList.get(i6);
                    if (popupListItem != null) {
                        if (!isHorizontalItem(popupListItem)) {
                            if (z2 && z3) {
                                if (i4 + i5 == i3) {
                                    return true;
                                }
                                i5++;
                                z2 = false;
                            }
                            if (this.mGroupSets.contains(Integer.valueOf(i6)) && i6 > 0) {
                                if (i4 + i5 == i3) {
                                    return true;
                                }
                                i5++;
                            }
                            if (i4 + i5 == i3) {
                                return false;
                            }
                            i4++;
                            z3 = false;
                        } else {
                            if (i4 + i5 == i3) {
                                return false;
                            }
                            i4++;
                            z2 = true;
                            z3 = true;
                        }
                    }
                }
                return false;
            }
            int iRealPositionToDataIndex = realPositionToDataIndex(i3 - 1);
            int iRealPositionToDataIndex2 = realPositionToDataIndex(i3 + 1);
            if (iRealPositionToDataIndex >= 0 && iRealPositionToDataIndex < this.mMenuItemList.size() && iRealPositionToDataIndex2 >= 0 && iRealPositionToDataIndex2 < this.mMenuItemList.size()) {
                PopupListItem popupListItem2 = this.mMenuItemList.get(iRealPositionToDataIndex);
                PopupListItem popupListItem3 = this.mMenuItemList.get(iRealPositionToDataIndex2);
                boolean zIsHorizontalItem = isHorizontalItem(popupListItem2);
                boolean z4 = (popupListItem3 == null || isHorizontalItem(popupListItem3)) ? false : true;
                if (zIsHorizontalItem && z4) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isHeaderPosition(int i3) {
        if (i3 <= 0) {
            return false;
        }
        int iRealPositionToDataIndex = realPositionToDataIndex(i3 - 1);
        List<PopupListItem> list = this.mMenuItemList;
        return list != null && iRealPositionToDataIndex >= 0 && iRealPositionToDataIndex < list.size() && this.mMenuItemList.get(iRealPositionToDataIndex).getItemType() == 3;
    }

    private boolean isHorizontalItem(PopupListItem popupListItem) {
        return popupListItem != null && popupListItem.getItemType() == 4;
    }

    private boolean isLayoutRTL() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1;
    }

    public static /* bridge */ /* synthetic */ int j(MixedPopupListAdapter mixedPopupListAdapter) {
        return mixedPopupListAdapter.mHorizontalItemIconTitleGap;
    }

    public static /* bridge */ /* synthetic */ boolean k(MixedPopupListAdapter mixedPopupListAdapter) {
        return mixedPopupListAdapter.mHorizontalTitleForceTwoLines;
    }

    public static /* bridge */ /* synthetic */ boolean l(MixedPopupListAdapter mixedPopupListAdapter) {
        return mixedPopupListAdapter.mHorizontalTitleForceTwoLinesComputed;
    }

    public static /* bridge */ /* synthetic */ boolean m(MixedPopupListAdapter mixedPopupListAdapter) {
        return mixedPopupListAdapter.mIsAdaptiveFontSize;
    }

    public static /* bridge */ /* synthetic */ boolean n(MixedPopupListAdapter mixedPopupListAdapter) {
        return mixedPopupListAdapter.mIsFixedFontSize;
    }

    public static /* bridge */ /* synthetic */ List o(MixedPopupListAdapter mixedPopupListAdapter) {
        return mixedPopupListAdapter.mMenuItemList;
    }

    public static /* bridge */ /* synthetic */ AdapterView.OnItemClickListener p(MixedPopupListAdapter mixedPopupListAdapter) {
        return mixedPopupListAdapter.mOnItemClickListener;
    }

    public static /* bridge */ /* synthetic */ void q(MixedPopupListAdapter mixedPopupListAdapter, TextView textView) {
        mixedPopupListAdapter.applyHorizontalTitleTextAppearance(textView);
    }

    public static /* bridge */ /* synthetic */ String r(MixedPopupListAdapter mixedPopupListAdapter, PopupListItem popupListItem) {
        return mixedPopupListAdapter.buildAccessibilityContentDescription(popupListItem);
    }

    private int realPositionToDataIndex(int i3) {
        List<PopupListItem> list = this.mMenuItemList;
        if (list != null && !list.isEmpty()) {
            Set<Integer> set = this.mGroupSets;
            if (set != null && !set.isEmpty()) {
                boolean z2 = false;
                int i4 = 0;
                int i5 = 0;
                boolean z3 = false;
                for (int i6 = 0; i6 < this.mMenuItemList.size(); i6++) {
                    PopupListItem popupListItem = this.mMenuItemList.get(i6);
                    if (popupListItem != null) {
                        if (!isHorizontalItem(popupListItem)) {
                            if (z2 && z3) {
                                if (i4 + i5 == i3) {
                                    return -1;
                                }
                                i5++;
                                z2 = false;
                            }
                            if (this.mGroupSets.contains(Integer.valueOf(i6)) && i6 > 0) {
                                if (i4 + i5 == i3) {
                                    return -1;
                                }
                                i5++;
                            }
                            if (i4 + i5 == i3) {
                                return i6;
                            }
                            i4++;
                            z3 = false;
                        } else {
                            if (i4 + i5 == i3) {
                                return i6;
                            }
                            i4++;
                            z2 = true;
                            z3 = true;
                        }
                    }
                }
                return -1;
            }
            boolean z4 = false;
            int i7 = 0;
            int i8 = 0;
            boolean z5 = false;
            for (int i9 = 0; i9 < this.mMenuItemList.size(); i9++) {
                PopupListItem popupListItem2 = this.mMenuItemList.get(i9);
                if (popupListItem2 != null) {
                    if (!isHorizontalItem(popupListItem2)) {
                        if (z4 && z5) {
                            if (i7 + i8 == i3) {
                                return -1;
                            }
                            i8++;
                            z4 = false;
                        }
                        if (i7 + i8 == i3) {
                            return i9;
                        }
                        i7++;
                        z5 = false;
                    } else {
                        if (i7 + i8 == i3) {
                            return i9;
                        }
                        i7++;
                        z4 = true;
                        z5 = true;
                    }
                }
            }
        }
        return -1;
    }

    public static /* bridge */ /* synthetic */ int s(MixedPopupListAdapter mixedPopupListAdapter, TextView textView) {
        return mixedPopupListAdapter.computeHorizontalTextUniformWidth(textView);
    }

    private void setDescription(TextView textView, PopupListItem popupListItem) {
        if (TextUtils.isEmpty(popupListItem.getDescription())) {
            textView.setVisibility(8);
            return;
        }
        textView.setVisibility(0);
        textView.setTextAppearance(R.style.couiTextBodyXS);
        textView.setText(popupListItem.getDescription());
        if (this.mIsFixedFontSize) {
            textView.setTextSize(1, 12.0f);
        } else if (this.mIsAdaptiveFontSize) {
            COUIChangeTextUtil.adaptFontSize(textView, 4);
        }
        textView.setTextColor(this.mDescriptionColor);
        textView.setMaxLines(2);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        if (TextUtils.isEmpty(popupListItem.getDescriptionContentDescription())) {
            return;
        }
        textView.setContentDescription(popupListItem.getDescriptionContentDescription());
    }

    private void setGap(VerticalViewHolder verticalViewHolder, PopupListItem popupListItem) {
        boolean z2 = popupListItem.getHintType() != -1;
        boolean z3 = popupListItem.getStateIcon() != null || popupListItem.getStateIconId() != 0 || popupListItem.hasSubMenu() || (popupListItem.getStateIcon() == null && popupListItem.getStateIconId() == 0 && popupListItem.isChecked());
        if (!z2 && !z3) {
            VerticalViewHolder.d(verticalViewHolder).setVisibility(8);
            return;
        }
        VerticalViewHolder.d(verticalViewHolder).setVisibility(4);
        if (z2 && z3) {
            VerticalViewHolder.c(verticalViewHolder).setVisibility(4);
        } else {
            VerticalViewHolder.c(verticalViewHolder).setVisibility(8);
        }
    }

    private void setHint(ViewGroup viewGroup, PopupListItem popupListItem) {
        if (popupListItem.isEnable()) {
            viewGroup.setVisibility(0);
        } else {
            viewGroup.setVisibility(8);
        }
        viewGroup.removeAllViews();
        if (popupListItem.getHintType() == 0) {
            viewGroup.addView(getRedDotHint(popupListItem), new ViewGroup.LayoutParams(-2, -2));
            return;
        }
        if (popupListItem.getHintType() != 1 || popupListItem.getCustomHintView() == null) {
            return;
        }
        ViewParent parent = popupListItem.getCustomHintView().getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(popupListItem.getCustomHintView());
        }
        viewGroup.addView(popupListItem.getCustomHintView());
    }

    private void setIcon(ImageView imageView, PopupListItem popupListItem) {
        Drawable drawable;
        if ((this.mPrivateFlags & 1) != 0) {
            imageView.setVisibility(0);
            if (popupListItem.getIcon() != null) {
                drawable = popupListItem.getIcon();
            } else {
                drawable = popupListItem.getIconId() != 0 ? ResourcesCompat.getDrawable(this.mContext.getResources(), popupListItem.getIconId(), this.mContext.getTheme()) : null;
            }
            if ((popupListItem.getForceTint() & 1) != 0) {
                setIconTint(drawable, this.mItemTintList, popupListItem);
            }
            imageView.setSelected(popupListItem.isChecked());
            imageView.setImageDrawable(drawable);
        } else {
            imageView.setVisibility(8);
        }
        imageView.setEnabled(popupListItem.isEnable());
    }

    private void setIconTint(Drawable drawable, ColorStateList colorStateList, PopupListItem popupListItem) {
        setIconTint(drawable, colorStateList, popupListItem, false);
    }

    private void setStateIcon(CheckBox checkBox, PopupListItem popupListItem) {
        if (popupListItem.getStateIcon() != null || popupListItem.getStateIconId() != 0 || popupListItem.hasSubMenu() || popupListItem.isChecked()) {
            checkBox.setVisibility(0);
            Drawable drawable = DEFAULT_TRANSPARENT_HOLDER;
            if (popupListItem.hasSubMenu()) {
                drawable = ResourcesCompat.getDrawable(this.mContext.getResources(), com.support.listview.R.drawable.coui_list_expandable_indicator, this.mContext.getTheme());
                setIconTint(drawable, this.mStateIconTintList, popupListItem, true);
            } else {
                if (popupListItem.getStateIcon() != null) {
                    drawable = popupListItem.getStateIcon();
                } else if (popupListItem.getStateIconId() != 0) {
                    drawable = ResourcesCompat.getDrawable(this.mContext.getResources(), popupListItem.getStateIconId(), this.mContext.getTheme());
                } else if (popupListItem.isChecked()) {
                    drawable = ResourcesCompat.getDrawable(this.mContext.getResources(), com.support.poplist.R.drawable.coui_menu_ic_checkbox, this.mContext.getTheme());
                }
                if ((popupListItem.getForceTint() & 4) != 0) {
                    setIconTint(drawable, this.mStateIconTintList, popupListItem, true);
                }
            }
            checkBox.setButtonDrawable(drawable);
            checkBox.setChecked(popupListItem.isChecked());
        } else {
            checkBox.setVisibility(8);
        }
        checkBox.setEnabled(popupListItem.isEnable());
    }

    private void setTextViewTint(TextView textView, ColorStateList colorStateList, PopupListItem popupListItem) {
        if (textView == null) {
            return;
        }
        textView.setTextColor(getTintColorByState(colorStateList, popupListItem));
    }

    private void setTitleForHorizontal(TextView textView, PopupListItem popupListItem) {
        textView.setTextAppearance(R.style.couiTextBodyXS);
        textView.setText(popupListItem.getTitle());
        if (!TextUtils.isEmpty(popupListItem.getTitleContentDescription())) {
            textView.setContentDescription(popupListItem.getTitleContentDescription());
        }
        textView.setMinLines(this.mHorizontalTitleForceTwoLines ? 2 : 1);
        textView.setMaxLines(2);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        if ((popupListItem.getForceTint() & 2) != 0) {
            setTextViewTint(textView, this.mItemTintList, popupListItem);
        } else if (popupListItem.getTitleColorList() != null) {
            textView.setTextColor(popupListItem.getTitleColorList());
        }
        textView.setSelected(popupListItem.isChecked());
        textView.setEnabled(popupListItem.isEnable());
    }

    private void setTitleForVertical(TextView textView, PopupListItem popupListItem, int i3) {
        boolean zIsEmpty = TextUtils.isEmpty(popupListItem.getDescription());
        textView.setTextAppearance(R.style.couiTextBodyL);
        if (popupListItem.getGroupState() == 2 && i3 == 0) {
            textView.setTypeface(MEDIUM_TYPEFACE);
        } else {
            textView.setTypeface(null);
        }
        textView.setText(popupListItem.getTitle());
        if (!TextUtils.isEmpty(popupListItem.getTitleContentDescription())) {
            textView.setContentDescription(popupListItem.getTitleContentDescription());
        }
        if (this.mIsFixedFontSize) {
            textView.setTextSize(1, 16.0f);
        } else if (this.mIsAdaptiveFontSize) {
            COUIChangeTextUtil.adaptFontSize(textView, 4);
        } else {
            COUIChangeTextUtil.adaptFontSize(textView, 5);
        }
        if (zIsEmpty) {
            textView.setMaxLines(3);
        } else {
            textView.setMaxLines(2);
        }
        textView.setEllipsize(TextUtils.TruncateAt.END);
        if ((popupListItem.getForceTint() & 2) != 0) {
            setTextViewTint(textView, this.mItemTintList, popupListItem);
        } else if (popupListItem.getTitleColorList() != null) {
            textView.setTextColor(popupListItem.getTitleColorList());
        }
        textView.setSelected(popupListItem.isChecked());
        textView.setEnabled(popupListItem.isEnable());
    }

    public static /* bridge */ /* synthetic */ void t(MixedPopupListAdapter mixedPopupListAdapter, View view, int i3, boolean z2, int i4) {
        mixedPopupListAdapter.configHorizontalItemLayout(view, i3, z2, i4);
    }

    public static /* bridge */ /* synthetic */ void u(MixedPopupListAdapter mixedPopupListAdapter, View view, int i3) {
        mixedPopupListAdapter.configItemVerticalPadding(view, i3);
    }

    private void updateHorizontalTitleForceTwoLine(TextView textView, View view) {
        if (this.mHorizontalTitleForceTwoLinesComputed) {
            return;
        }
        boolean z2 = false;
        if (!this.mHorizontalHasText || this.mHorizontalItemCount <= 0 || this.mMenuItemList == null || textView == null || view == null) {
            this.mHorizontalTitleForceTwoLines = false;
            this.mHorizontalTitleForceTwoLinesComputed = true;
            return;
        }
        int horizontalContainerWidth = getHorizontalContainerWidth(view);
        int measuredWidth = view.getMeasuredWidth();
        if (measuredWidth <= 0) {
            measuredWidth = view.getWidth();
        }
        if (measuredWidth <= 0 && horizontalContainerWidth > 0) {
            measuredWidth = horizontalContainerWidth / Math.max(1, this.mHorizontalItemCount);
        }
        int paddingStart = (measuredWidth - view.getPaddingStart()) - view.getPaddingEnd();
        if (paddingStart <= 0) {
            this.mHorizontalTitleForceTwoLines = false;
            return;
        }
        for (PopupListItem popupListItem : this.mMenuItemList) {
            if (popupListItem != null && popupListItem.getItemType() == 4 && !TextUtils.isEmpty(popupListItem.getTitle()) && textView.getPaint().measureText(popupListItem.getTitle()) > paddingStart) {
                z2 = true;
                break;
            }
        }
        this.mHorizontalTitleForceTwoLines = z2;
        this.mHorizontalTitleForceTwoLinesComputed = true;
    }

    public static /* bridge */ /* synthetic */ void v(MixedPopupListAdapter mixedPopupListAdapter, View view, PopupListItem popupListItem, int i3) {
        mixedPopupListAdapter.configStateEffectBackground(view, popupListItem, i3);
    }

    public static /* bridge */ /* synthetic */ void w(MixedPopupListAdapter mixedPopupListAdapter, TextView textView, PopupListItem popupListItem) {
        mixedPopupListAdapter.setDescription(textView, popupListItem);
    }

    public static /* bridge */ /* synthetic */ void x(MixedPopupListAdapter mixedPopupListAdapter, VerticalViewHolder verticalViewHolder, PopupListItem popupListItem) {
        mixedPopupListAdapter.setGap(verticalViewHolder, popupListItem);
    }

    public static /* bridge */ /* synthetic */ void y(MixedPopupListAdapter mixedPopupListAdapter, ViewGroup viewGroup, PopupListItem popupListItem) {
        mixedPopupListAdapter.setHint(viewGroup, popupListItem);
    }

    public static /* bridge */ /* synthetic */ void z(MixedPopupListAdapter mixedPopupListAdapter, ImageView imageView, PopupListItem popupListItem) {
        mixedPopupListAdapter.setIcon(imageView, popupListItem);
    }

    public int convertAdapterPositionToDataIndex(int i3) {
        if (isDataIndex(i3)) {
            return realPositionToDataIndex(i3);
        }
        return -1;
    }

    public int getDividerHeight(int i3) {
        return this.mGroupDividerTotalHeight;
    }

    public PopupListItem getItem(int i3) {
        int iRealPositionToDataIndex;
        if (this.mMenuItemList != null && isDataIndex(i3) && (iRealPositionToDataIndex = realPositionToDataIndex(i3)) >= 0 && iRealPositionToDataIndex < this.mMenuItemList.size()) {
            return this.mMenuItemList.get(iRealPositionToDataIndex);
        }
        return null;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<PopupListItem> list = this.mMenuItemList;
        if (list == null || list.isEmpty()) {
            return 0;
        }
        Set<Integer> set = this.mGroupSets;
        if (set == null || set.isEmpty()) {
            int i3 = 0;
            int i4 = 0;
            boolean z2 = false;
            boolean z3 = false;
            for (int i5 = 0; i5 < this.mMenuItemList.size(); i5++) {
                PopupListItem popupListItem = this.mMenuItemList.get(i5);
                if (popupListItem != null) {
                    if (isHorizontalItem(popupListItem)) {
                        i3++;
                        z2 = true;
                        z3 = true;
                    } else {
                        if (z2 && z3) {
                            i4++;
                            z2 = false;
                        }
                        i3++;
                        z3 = false;
                    }
                }
            }
            return i3 + i4;
        }
        int i6 = 0;
        int i7 = 0;
        boolean z4 = false;
        boolean z5 = false;
        for (int i8 = 0; i8 < this.mMenuItemList.size(); i8++) {
            PopupListItem popupListItem2 = this.mMenuItemList.get(i8);
            if (popupListItem2 != null) {
                if (isHorizontalItem(popupListItem2)) {
                    i6++;
                    z4 = true;
                    z5 = true;
                } else {
                    if (z4 && z5) {
                        i7++;
                        z4 = false;
                    }
                    if (this.mGroupSets.contains(Integer.valueOf(i8)) && i8 > 0) {
                        i7++;
                    }
                    i6++;
                    z5 = false;
                }
            }
        }
        return i6 + i7;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i3) {
        if (this.mMenuItemList == null) {
            Log.w(TAG, "getItemViewType: position=" + i3 + ", list is null");
            return 1;
        }
        if (!isDataIndex(i3)) {
            return 4;
        }
        int iRealPositionToDataIndex = realPositionToDataIndex(i3);
        if (iRealPositionToDataIndex >= this.mMenuItemList.size()) {
            Log.w(TAG, "getItemViewType: position=" + i3 + ", dataIndex=" + iRealPositionToDataIndex + " out of bounds");
            return 1;
        }
        PopupListItem popupListItem = this.mMenuItemList.get(iRealPositionToDataIndex);
        if (popupListItem == null) {
            Log.w(TAG, "getItemViewType: position=" + i3 + ", item is null");
            return 1;
        }
        int itemType = popupListItem.getItemType();
        if (itemType == 4) {
            return 0;
        }
        if (itemType == 2) {
            return 5;
        }
        return itemType == 3 ? 2 : 1;
    }

    public List<PopupListItem> getMenuItemList() {
        return this.mMenuItemList;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mAttachedRecyclerView = recyclerView;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i3) {
        if (this.mMenuItemList == null) {
            Log.w(TAG, "onBindViewHolder: position=" + i3 + ", list is null");
            return;
        }
        if (viewHolder instanceof DividerViewHolder) {
            ((DividerViewHolder) viewHolder).bind(new PopupListItem.Builder().build(), getItemViewType(i3));
            return;
        }
        if (!isDataIndex(i3)) {
            Log.w(TAG, "onBindViewHolder: position=" + i3 + " is not a data index");
            return;
        }
        int iRealPositionToDataIndex = realPositionToDataIndex(i3);
        if (iRealPositionToDataIndex < 0 || iRealPositionToDataIndex >= this.mMenuItemList.size()) {
            Log.w(TAG, "onBindViewHolder: position=" + i3 + ", dataIndex=" + iRealPositionToDataIndex + " out of bounds");
            return;
        }
        PopupListItem popupListItem = this.mMenuItemList.get(iRealPositionToDataIndex);
        if (popupListItem == null) {
            Log.w(TAG, "onBindViewHolder: position=" + i3 + ", item is null");
            return;
        }
        if (viewHolder instanceof HorizontalViewHolder) {
            ((HorizontalViewHolder) viewHolder).bind(popupListItem, iRealPositionToDataIndex);
        } else if (viewHolder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) viewHolder).bind(popupListItem);
        } else if (viewHolder instanceof CustomViewHolder) {
            ((CustomViewHolder) viewHolder).bind(popupListItem);
        } else if (viewHolder instanceof VerticalViewHolder) {
            ((VerticalViewHolder) viewHolder).bind(popupListItem, iRealPositionToDataIndex);
        }
        viewHolder.itemView.setOnClickListener(new a0(this, iRealPositionToDataIndex, popupListItem));
        if (popupListItem.hasSubMenu() || this.mOnItemLongClickListener == null) {
            viewHolder.itemView.setOnLongClickListener(null);
            viewHolder.itemView.setOnTouchListener(null);
        } else {
            viewHolder.itemView.setOnLongClickListener(new b0(this, iRealPositionToDataIndex, popupListItem));
            viewHolder.itemView.setOnTouchListener(null);
        }
        if ((viewHolder instanceof HorizontalViewHolder) || (viewHolder instanceof VerticalViewHolder) || (viewHolder instanceof HeaderViewHolder) || (viewHolder instanceof CustomViewHolder)) {
            viewHolder.itemView.setAccessibilityDelegate(getAccessibilityDelegate(iRealPositionToDataIndex));
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i3) {
        LayoutInflater layoutInflaterFrom = LayoutInflater.from(this.mContext);
        if (i3 == 0) {
            return new HorizontalViewHolder(layoutInflaterFrom.inflate(com.support.poplist.R.layout.coui_popup_list_window_horizontal_item, viewGroup, false));
        }
        if (i3 == 2) {
            return new HeaderViewHolder(layoutInflaterFrom.inflate(com.support.poplist.R.layout.coui_popup_list_window_header_item, viewGroup, false));
        }
        if (i3 == 3 || i3 == 4) {
            View view = new View(viewGroup.getContext());
            view.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new DividerViewHolder(view);
        }
        if (i3 != 5) {
            return new VerticalViewHolder(layoutInflaterFrom.inflate(com.support.poplist.R.layout.coui_popup_list_window_item, viewGroup, false));
        }
        FrameLayout frameLayout = new FrameLayout(viewGroup.getContext());
        frameLayout.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
        return new CustomViewHolder(frameLayout);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (this.mAttachedRecyclerView == recyclerView) {
            this.mAttachedRecyclerView = null;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
        super.onViewRecycled(viewHolder);
        if (viewHolder instanceof HorizontalViewHolder) {
            HorizontalViewHolder horizontalViewHolder = (HorizontalViewHolder) viewHolder;
            horizontalViewHolder.itemView.removeOnLayoutChangeListener(horizontalViewHolder.mLayoutChangeListener);
        } else if (viewHolder instanceof VerticalViewHolder) {
            VerticalViewHolder verticalViewHolder = (VerticalViewHolder) viewHolder;
            verticalViewHolder.itemView.removeOnLayoutChangeListener(verticalViewHolder.mLayoutChangeListener);
        }
    }

    public void setAdapterFontSize(boolean z2) {
        this.mIsAdaptiveFontSize = z2;
    }

    public void setGroupSets(Set<Integer> set) {
        this.mGroupSets = set;
    }

    public void setIsFixedFontSize(boolean z2) {
        this.mIsFixedFontSize = z2;
    }

    public void setItemList(List<PopupListItem> list) {
        if (list == null) {
            this.mMenuItemList = null;
            Log.w(TAG, "setItemList: itemList is null");
            return;
        }
        ArrayList<PopupListItem> arrayList = new ArrayList(list);
        int i3 = 0;
        boolean z2 = false;
        for (PopupListItem popupListItem : arrayList) {
            if (popupListItem != null && popupListItem.getItemType() == 4) {
                i3++;
                if (!TextUtils.isEmpty(popupListItem.getTitle())) {
                    z2 = true;
                }
            }
        }
        if (i3 > 0) {
            int i4 = z2 ? 3 : 5;
            if (i3 > i4) {
                StringBuilder sb = new StringBuilder();
                sb.append("setItemList: Horizontal items exceed limit! type=");
                sb.append(z2 ? "with text" : "icon-only");
                sb.append(", count=");
                sb.append(i3);
                sb.append(", limit=");
                sb.append(i4);
                sb.append(", only first ");
                sb.append(i4);
                sb.append(" items will be displayed");
                Log.w(TAG, sb.toString());
                ArrayList arrayList2 = new ArrayList();
                int i5 = 0;
                for (PopupListItem popupListItem2 : arrayList) {
                    if (popupListItem2 == null || popupListItem2.getItemType() != 4) {
                        arrayList2.add(popupListItem2);
                    } else if (i5 < i4) {
                        arrayList2.add(popupListItem2);
                        i5++;
                    } else {
                        Log.w(TAG, "setItemList: Removing excess horizontal item: " + popupListItem2.getTitle());
                    }
                }
                arrayList = arrayList2;
            }
        }
        this.mMenuItemList = arrayList;
        this.mHorizontalItemCount = i3;
        this.mHorizontalHasText = z2;
        this.mHorizontalTitleForceTwoLines = false;
        this.mHorizontalTitleForceTwoLinesComputed = false;
        this.mPrivateFlags = 0;
        for (int i6 = 0; i6 < this.mMenuItemList.size(); i6++) {
            PopupListItem popupListItem3 = this.mMenuItemList.get(i6);
            if (popupListItem3 == null) {
                Log.w(TAG, "setItemList: index=" + i6 + ", item is null");
            } else {
                if (popupListItem3.getTitle() != null) {
                    popupListItem3.getTitle();
                }
                popupListItem3.getItemType();
                if (popupListItem3.getIconId() != 0 || popupListItem3.getIcon() != null) {
                    this.mPrivateFlags |= 1;
                }
                if (!TextUtils.isEmpty(popupListItem3.getDescription())) {
                    this.mPrivateFlags |= 2;
                }
                if (popupListItem3.getHintType() != -1) {
                    this.mPrivateFlags |= 4;
                }
                if (popupListItem3.getStateIconId() != 0 || popupListItem3.getStateIcon() != null) {
                    this.mPrivateFlags |= 8;
                }
                if (popupListItem3.hasSubMenu()) {
                    this.mPrivateFlags |= 16;
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    public void setSharedBackground(ListItemMaskEffectDrawable listItemMaskEffectDrawable) {
        this.mSharedDrawable = listItemMaskEffectDrawable;
    }

    private int getTintColorByState(ColorStateList colorStateList, PopupListItem popupListItem, boolean z2) {
        if (!popupListItem.isEnable()) {
            return colorStateList.getColorForState(STATE_DISABLED, R.color.coui_color_error);
        }
        if (popupListItem.getItemType() != 0) {
            return popupListItem.getItemType() == 1 ? this.mAlertColor : colorStateList.getDefaultColor();
        }
        if (!z2 || popupListItem.getGroupState() == 0) {
            return popupListItem.isChecked() ? colorStateList.getColorForState(STATE_SELECTED, R.color.coui_color_error) : colorStateList.getDefaultColor();
        }
        return colorStateList.getColorForState(STATE_SELECTED, R.color.coui_color_error);
    }

    private void setIconTint(Drawable drawable, ColorStateList colorStateList, PopupListItem popupListItem, boolean z2) {
        if (drawable == null) {
            return;
        }
        drawable.setTint(getTintColorByState(colorStateList, popupListItem, z2));
    }
}