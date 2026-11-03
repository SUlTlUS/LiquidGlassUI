package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceViewHolder;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.reddot.COUIHintRedDot;
import com.coui.appcompat.rippleutil.COUIRippleDrawableUtil;
import com.coui.appcompat.state.COUIMaskRippleDrawable;
import com.coui.appcompat.textviewcompatutil.COUITextViewCompatUtil;
import com.support.preference.R;

/* JADX INFO: loaded from: classes.dex */
public class COUIPreferenceCategory extends PreferenceCategory {
    public static final int MARGIN_TYPE_LARGE = 0;
    public static final int MARGIN_TYPE_SMALL = 1;
    public static final int MARGIN_TYPE_ZERO = 2;
    private static final String TAG = "COUIPreferenceCategory";
    private static final float TITLE_LARGE_TEXTSIZE = 16.0f;
    public static final int TITLE_MARGIN_START_TYPE_LARGE = 1;
    public static final int TITLE_MARGIN_START_TYPE_SMALL = 0;
    private static final float TITLE_MEDIUM_TEXTSIZE = 14.0f;
    private static final float TITLE_SMALL_TEXTSIZE = 12.0f;
    public static final int TITLE_TYPE_LARGE = 2;
    public static final int TITLE_TYPE_MEDIUM = 1;
    public static final int TITLE_TYPE_SMALL = 0;
    private Context mContext;
    private IGetWidgetLayoutListener mGetWidgetLayoutListener;
    private int mHorizontalMarginType;
    private int mIconInRight;
    private int mIconWithTitle;
    private View.OnClickListener mItemviewClickListener;
    private int mLayoutTitleMarginEndLarge;
    private int mLayoutTitleMarginStartLarge;
    private int mLayoutTitleMarginStartSmall;
    private ArrayMap<Integer, Integer> mMarginEndMap;
    private int mMarginTopType;
    private COUIMaskRippleDrawable mMaskRippleDrawable;
    private String mRightIconContentDescription;
    private String mTextInReddot;
    private String mTextInRight;
    private View.OnClickListener mTitleIconClickListener;
    private String mTitleIconContentDescription;
    private boolean mTitleOnly;
    private TextView mTitleTextView;
    private int mTitleType;
    private View mWidgetLayout;
    private View.OnClickListener mWidgetLayoutClickListener;
    private int mWidgetLayoutRes;

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIPreferenceCategory$1, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    public class AnonymousClass1 implements View.OnAttachStateChangeListener {

        /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIPreferenceCategory$1$1, reason: invalid class name and collision with other inner class name */
        public class RunnableC04521 implements Runnable {
            public RunnableC04521() {
            }

            @Override // java.lang.Runnable
            public void run() {
                COUIPreferenceCategory.access$100(COUIPreferenceCategory.this).widgetInflated(COUIPreferenceCategory.access$000(COUIPreferenceCategory.this));
            }
        }

        public AnonymousClass1() {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            COUIPreferenceCategory.access$000(COUIPreferenceCategory.this).post(new RunnableC04521());
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            view.removeOnAttachStateChangeListener(this);
        }
    }

    /* JADX INFO: loaded from: classes7.dex */
    public interface IGetWidgetLayoutListener {
        void widgetInflated(View view);
    }

    public COUIPreferenceCategory(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mHorizontalMarginType = 1;
        this.mTitleType = 0;
        this.mMarginTopType = 0;
        this.mTitleTextView = null;
        this.mTitleOnly = false;
        this.mContext = context;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUIPreferenceCategory, 0, 0);
        this.mTextInRight = typedArrayObtainStyledAttributes.getString(R.styleable.COUIPreferenceCategory_text_in_right);
        this.mIconInRight = typedArrayObtainStyledAttributes.getResourceId(R.styleable.COUIPreferenceCategory_icon_in_right, 0);
        this.mIconWithTitle = typedArrayObtainStyledAttributes.getResourceId(R.styleable.COUIPreferenceCategory_icon_with_title, 0);
        this.mTextInReddot = typedArrayObtainStyledAttributes.getString(R.styleable.COUIPreferenceCategory_text_in_reddot);
        this.mHorizontalMarginType = typedArrayObtainStyledAttributes.getInteger(R.styleable.COUIPreferenceCategory_title_margin_start_type, this.mHorizontalMarginType);
        this.mTitleType = typedArrayObtainStyledAttributes.getInteger(R.styleable.COUIPreferenceCategory_title_type, this.mTitleType);
        this.mMarginTopType = typedArrayObtainStyledAttributes.getInteger(R.styleable.COUIPreferenceCategory_top_margin_type, this.mMarginTopType);
        this.mTitleIconContentDescription = typedArrayObtainStyledAttributes.getString(R.styleable.COUIPreferenceCategory_icon_with_title_content_description);
        this.mRightIconContentDescription = typedArrayObtainStyledAttributes.getString(R.styleable.COUIPreferenceCategory_icon_in_right_content_description);
        typedArrayObtainStyledAttributes.recycle();
        TypedArray typedArrayObtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, androidx.preference.R.styleable.Preference, 0, 0);
        this.mWidgetLayoutRes = TypedArrayUtils.getResourceId(typedArrayObtainStyledAttributes2, androidx.preference.R.styleable.Preference_widgetLayout, androidx.preference.R.styleable.Preference_android_widgetLayout, 0);
        typedArrayObtainStyledAttributes2.recycle();
        this.mLayoutTitleMarginStartLarge = context.getResources().getDimensionPixelSize(R.dimen.support_preference_category_layout_title_margin_start_large);
        this.mLayoutTitleMarginStartSmall = context.getResources().getDimensionPixelSize(R.dimen.support_preference_category_layout_title_margin_start_small);
        this.mLayoutTitleMarginEndLarge = context.getResources().getDimensionPixelSize(R.dimen.support_preference_category_layout_title_margin_end_large);
        ArrayMap<Integer, Integer> arrayMap = new ArrayMap<>();
        this.mMarginEndMap = arrayMap;
        arrayMap.put(Integer.valueOf(R.layout.coui_preference_category_widget_layout_checkbox), 0);
        this.mMarginEndMap.put(Integer.valueOf(R.layout.coui_preference_category_widget_layout_loading), 0);
        this.mMarginEndMap.put(Integer.valueOf(R.layout.coui_preference_category_widget_layout_singleicon), Integer.valueOf(context.getResources().getDimensionPixelSize(R.dimen.coui_preference_category_Loading_marginend)));
        this.mMarginEndMap.put(Integer.valueOf(R.layout.coui_preference_category_widget_layout_textbutton), Integer.valueOf(context.getResources().getDimensionPixelSize(R.dimen.coui_preference_category_textbutton_marginend)));
        this.mMarginEndMap.put(Integer.valueOf(R.layout.coui_preference_category_widget_layout_textwithicon), 0);
        ensureMaskRippleDrawable();
    }

    public static /* synthetic */ View access$000(COUIPreferenceCategory cOUIPreferenceCategory) {
        return cOUIPreferenceCategory.mWidgetLayout;
    }

    public static /* synthetic */ IGetWidgetLayoutListener access$100(COUIPreferenceCategory cOUIPreferenceCategory) {
        return cOUIPreferenceCategory.mGetWidgetLayoutListener;
    }

    private void ensureMaskRippleDrawable() {
        if (this.mMaskRippleDrawable == null) {
            COUIMaskRippleDrawable cOUIMaskRippleDrawable = new COUIMaskRippleDrawable(getContext());
            this.mMaskRippleDrawable = cOUIMaskRippleDrawable;
            cOUIMaskRippleDrawable.setCircleRippleMask(getContext().getResources().getDimensionPixelSize(R.dimen.coui_preference_widget_layout_single_icon_radius));
        }
    }

    private void initEndRect(PreferenceViewHolder preferenceViewHolder) {
        TextView textView;
        TextView textView2;
        int i3;
        int i4;
        View viewFindViewById = preferenceViewHolder.findViewById(android.R.id.widget_frame);
        if (this.mWidgetLayoutRes != 0) {
            if (!(viewFindViewById instanceof LinearLayout)) {
                return;
            }
            LinearLayout linearLayout = (LinearLayout) viewFindViewById;
            if (linearLayout != null && linearLayout.getChildCount() > 0) {
                linearLayout.removeAllViews();
            }
            View viewInflate = LayoutInflater.from(this.mContext).inflate(this.mWidgetLayoutRes, (ViewGroup) linearLayout, false);
            this.mWidgetLayout = viewInflate;
            if (viewInflate == null) {
                Log.e(TAG, "inflate mWidgetLayoutRes failed");
                return;
            }
            this.mTitleOnly = false;
            if (viewInflate.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mWidgetLayout.getLayoutParams();
                if (this.mHorizontalMarginType == 0) {
                    if (marginLayoutParams.getMarginEnd() != this.mMarginEndMap.get(Integer.valueOf(this.mWidgetLayoutRes)).intValue()) {
                        marginLayoutParams.setMarginEnd(this.mMarginEndMap.get(Integer.valueOf(this.mWidgetLayoutRes)).intValue());
                        this.mWidgetLayout.setLayoutParams(marginLayoutParams);
                    }
                } else if (marginLayoutParams.getMarginEnd() != this.mMarginEndMap.get(Integer.valueOf(this.mWidgetLayoutRes)).intValue() + this.mLayoutTitleMarginEndLarge) {
                    marginLayoutParams.setMarginEnd(this.mMarginEndMap.get(Integer.valueOf(this.mWidgetLayoutRes)).intValue() + this.mLayoutTitleMarginEndLarge);
                    this.mWidgetLayout.setLayoutParams(marginLayoutParams);
                }
            }
            if (this.mGetWidgetLayoutListener != null) {
                this.mWidgetLayout.addOnAttachStateChangeListener(new AnonymousClass1());
            }
            if (!(this.mWidgetLayout.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
                return;
            }
            ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) this.mWidgetLayout.getLayoutParams();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(marginLayoutParams2.width, marginLayoutParams2.height);
            layoutParams.gravity = 16;
            layoutParams.setMarginStart(marginLayoutParams2.getMarginStart());
            layoutParams.topMargin = marginLayoutParams2.topMargin;
            layoutParams.setMarginEnd(marginLayoutParams2.getMarginEnd());
            layoutParams.bottomMargin = marginLayoutParams2.bottomMargin;
            linearLayout.addView(this.mWidgetLayout, layoutParams);
            linearLayout.setVisibility(0);
            if (this.mWidgetLayoutClickListener != null) {
                if (this.mWidgetLayoutRes == R.layout.coui_preference_category_widget_layout_singleicon) {
                    ensureMaskRippleDrawable();
                    linearLayout.getChildAt(0).setBackground(this.mMaskRippleDrawable);
                } else {
                    COUITextViewCompatUtil.setPressRippleDrawable(this.mWidgetLayout, false);
                }
                this.mWidgetLayout.setOnClickListener(this.mWidgetLayoutClickListener);
            } else {
                View.OnClickListener onClickListener = this.mItemviewClickListener;
                if (onClickListener != null) {
                    preferenceViewHolder.itemView.setOnClickListener(onClickListener);
                    COUIRippleDrawableUtil.setPressRippleDrawable(preferenceViewHolder.itemView, 0, false);
                }
            }
            int i5 = this.mWidgetLayoutRes;
            if (i5 == R.layout.coui_preference_category_widget_layout_textwithicon) {
                textView2 = (TextView) linearLayout.findViewById(R.id.text_in_composition);
                if (textView2 != null && !TextUtils.isEmpty(this.mTextInRight)) {
                    textView2.setText(this.mTextInRight);
                    textView2.setVisibility(0);
                }
                if (this.mWidgetLayoutClickListener == null || rightTextfixSecondaryColor()) {
                    textView2.setTextColor(COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorSecondNeutral, 0));
                } else {
                    textView2.setTextColor(COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorPrimaryNeutral, 0));
                }
                ImageView imageView = (ImageView) linearLayout.findViewById(R.id.icon_in_composition);
                if (imageView != null && (i4 = this.mIconInRight) != 0) {
                    imageView.setImageResource(i4);
                    imageView.setVisibility(0);
                }
            } else if (i5 == R.layout.coui_preference_category_widget_layout_textbutton) {
                textView2 = (TextView) linearLayout.findViewById(R.id.text_button);
                if (textView2 != null && !TextUtils.isEmpty(this.mTextInRight)) {
                    textView2.setText(this.mTextInRight);
                    textView2.setVisibility(0);
                    COUITextViewCompatUtil.setPressRippleDrawable(textView2);
                }
            } else {
                if (i5 == R.layout.coui_preference_category_widget_layout_singleicon) {
                    ImageView imageView2 = (ImageView) linearLayout.findViewById(R.id.singleIcon);
                    if (imageView2 != null && (i3 = this.mIconInRight) != 0) {
                        imageView2.setImageResource(i3);
                        imageView2.setVisibility(0);
                        if (!TextUtils.isEmpty(this.mRightIconContentDescription)) {
                            imageView2.setContentDescription(this.mRightIconContentDescription);
                        }
                    }
                } else if (i5 == R.layout.coui_preference_category_widget_layout_loading) {
                    textView2 = (TextView) this.mWidgetLayout.findViewById(R.id.text_in_loading);
                }
                textView2 = null;
            }
            if (textView2 != null) {
                if (this.mTitleType == 0) {
                    textView2.setTextSize(2, 12.0f);
                } else {
                    textView2.setTextSize(2, 14.0f);
                    linearLayout.getChildAt(0).setMinimumHeight(getContext().getResources().getDimensionPixelSize(R.dimen.coui_preference_widget_layout_min_height_when_title_isnot_small));
                }
            }
        } else if (viewFindViewById != null) {
            viewFindViewById.setVisibility(8);
        }
        if (this.mTitleOnly && (textView = this.mTitleTextView) != null && (textView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
            ViewGroup.MarginLayoutParams marginLayoutParams3 = (ViewGroup.MarginLayoutParams) this.mTitleTextView.getLayoutParams();
            marginLayoutParams3.setMarginEnd(marginLayoutParams3.getMarginStart());
            this.mTitleTextView.setLayoutParams(marginLayoutParams3);
        }
    }

    private void initStartRect(PreferenceViewHolder preferenceViewHolder) {
        ImageView imageView;
        boolean z2;
        this.mTitleOnly = true;
        View viewFindViewById = preferenceViewHolder.findViewById(android.R.id.title);
        if (viewFindViewById instanceof TextView) {
            this.mTitleTextView = (TextView) viewFindViewById;
        }
        View viewFindViewById2 = preferenceViewHolder.findViewById(R.id.icon_with_title);
        if (viewFindViewById2 instanceof ImageView) {
            imageView = (ImageView) viewFindViewById2;
            View.OnClickListener onClickListener = this.mTitleIconClickListener;
            if (onClickListener != null) {
                imageView.setOnClickListener(onClickListener);
                ensureMaskRippleDrawable();
                imageView.setBackground(this.mMaskRippleDrawable);
            }
            if (!TextUtils.isEmpty(this.mTitleIconContentDescription)) {
                imageView.setContentDescription(this.mTitleIconContentDescription);
            }
        } else {
            imageView = null;
        }
        View viewFindViewById3 = preferenceViewHolder.findViewById(R.id.reddot_with_title);
        COUIHintRedDot cOUIHintRedDot = viewFindViewById3 instanceof COUIHintRedDot ? (COUIHintRedDot) viewFindViewById3 : null;
        if (imageView == null) {
            z2 = false;
        } else {
            int i3 = this.mIconWithTitle;
            if (i3 != 0) {
                imageView.setImageResource(i3);
                imageView.setVisibility(0);
                this.mTitleOnly = false;
                if (viewFindViewById.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) viewFindViewById.getLayoutParams();
                    if (this.mTitleType == 2) {
                        marginLayoutParams.setMarginEnd(getContext().getResources().getDimensionPixelSize(R.dimen.coui_category_title_margin_end_with_icon_large));
                    } else {
                        marginLayoutParams.setMarginEnd(getContext().getResources().getDimensionPixelSize(R.dimen.coui_category_title_margin_end_with_icon_small));
                    }
                    viewFindViewById.setLayoutParams(marginLayoutParams);
                }
                z2 = true;
            } else {
                imageView.setVisibility(8);
                z2 = false;
            }
        }
        if (cOUIHintRedDot != null) {
            if (TextUtils.isEmpty(this.mTextInReddot) || z2) {
                cOUIHintRedDot.setVisibility(8);
            } else {
                cOUIHintRedDot.setPointMode(2);
                cOUIHintRedDot.setPointText(this.mTextInReddot);
                cOUIHintRedDot.setVisibility(0);
                this.mTitleOnly = false;
                if ((viewFindViewById.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) && (viewFindViewById.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
                    ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) viewFindViewById.getLayoutParams();
                    marginLayoutParams2.setMarginEnd(getContext().getResources().getDimensionPixelSize(R.dimen.coui_category_title_pading_end_with_reddot_default));
                    viewFindViewById.setLayoutParams(marginLayoutParams2);
                }
            }
        }
        TextView textView = this.mTitleTextView;
        if (textView != null && textView.getVisibility() == 0) {
            int i4 = this.mTitleType;
            if (i4 == 0) {
                this.mTitleTextView.setTextSize(2, 12.0f);
                this.mTitleTextView.setMinHeight(getContext().getResources().getDimensionPixelSize(R.dimen.coui_preference_category_text_height));
                if (z2) {
                    this.mTitleTextView.setTextColor(COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorPrimaryNeutral));
                } else {
                    this.mTitleTextView.setTextColor(COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorSecondNeutral));
                }
            } else if (i4 == 2) {
                this.mTitleTextView.setTextSize(2, 16.0f);
                this.mTitleTextView.setMinHeight(getContext().getResources().getDimensionPixelSize(R.dimen.coui_preference_category_text_height_large));
                this.mTitleTextView.setTextColor(COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorPrimaryNeutral));
            } else {
                this.mTitleTextView.setTextSize(2, 14.0f);
                this.mTitleTextView.setMinHeight(getContext().getResources().getDimensionPixelSize(R.dimen.coui_preference_category_text_height_medium));
                this.mTitleTextView.setTextColor(COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorPrimaryNeutral));
            }
            if (this.mTitleTextView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams3 = (ViewGroup.MarginLayoutParams) this.mTitleTextView.getLayoutParams();
                if (this.mHorizontalMarginType == 0) {
                    int marginStart = marginLayoutParams3.getMarginStart();
                    int i5 = this.mLayoutTitleMarginStartSmall;
                    if (marginStart != i5) {
                        marginLayoutParams3.setMarginStart(i5);
                    }
                } else {
                    this.mTitleTextView.setLayoutParams(marginLayoutParams3);
                    int marginStart2 = marginLayoutParams3.getMarginStart();
                    int i6 = this.mLayoutTitleMarginStartLarge;
                    if (marginStart2 != i6) {
                        marginLayoutParams3.setMarginStart(i6);
                    }
                }
                this.mTitleTextView.setLayoutParams(marginLayoutParams3);
                View view = preferenceViewHolder.itemView;
                view.setPadding(view.getPaddingStart(), 0, preferenceViewHolder.itemView.getPaddingEnd(), 0);
                int i7 = this.mTitleType;
                if (i7 == 0) {
                    Resources resources = getContext().getResources();
                    int i8 = R.dimen.support_preference_category_layout_title_margin_end_new;
                    marginLayoutParams3.topMargin = resources.getDimensionPixelSize(i8);
                    marginLayoutParams3.bottomMargin = getContext().getResources().getDimensionPixelSize(i8);
                } else if (i7 == 2) {
                    marginLayoutParams3.topMargin = getContext().getResources().getDimensionPixelSize(R.dimen.coui_common_category_text_padding_top_Large_style);
                    marginLayoutParams3.bottomMargin = getContext().getResources().getDimensionPixelSize(R.dimen.coui_common_category_text_padding_bottom_large_style);
                } else {
                    marginLayoutParams3.topMargin = getContext().getResources().getDimensionPixelSize(R.dimen.coui_common_category_text_padding_top_medium_style);
                    marginLayoutParams3.bottomMargin = getContext().getResources().getDimensionPixelSize(R.dimen.coui_common_category_text_padding_bottom_medium_style);
                }
                this.mTitleTextView.setLayoutParams(marginLayoutParams3);
            }
        }
        if (preferenceViewHolder.itemView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams4 = (ViewGroup.MarginLayoutParams) preferenceViewHolder.itemView.getLayoutParams();
            int i9 = this.mMarginTopType;
            if (i9 == 0) {
                marginLayoutParams4.topMargin = getContext().getResources().getDimensionPixelSize(R.dimen.coui_preference_category_margintop_large);
            } else if (i9 == 1) {
                marginLayoutParams4.topMargin = getContext().getResources().getDimensionPixelSize(R.dimen.coui_preference_category_margintop_small);
            } else if (i9 == 2) {
                marginLayoutParams4.topMargin = getContext().getResources().getDimensionPixelSize(R.dimen.coui_preference_category_margintop_zero);
            }
        }
    }

    public void getWidgetLayout(IGetWidgetLayoutListener iGetWidgetLayoutListener) {
        View view = this.mWidgetLayout;
        if (view != null) {
            iGetWidgetLayoutListener.widgetInflated(view);
        } else {
            this.mGetWidgetLayoutListener = iGetWidgetLayoutListener;
        }
    }

    @Override // androidx.preference.PreferenceCategory, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        initStartRect(preferenceViewHolder);
        initEndRect(preferenceViewHolder);
    }

    public boolean rightTextfixSecondaryColor() {
        return false;
    }

    public void setHorizontalMarginType(int i3) {
        if (this.mHorizontalMarginType != i3) {
            this.mHorizontalMarginType = i3;
            notifyChanged();
        }
    }

    public void setIconInRight(int i3) {
        if (this.mIconInRight != i3) {
            this.mIconInRight = i3;
            notifyChanged();
        }
    }

    public void setIconWithTitle(int i3) {
        this.mIconWithTitle = i3;
    }

    public void setItemViewLayoutClickListener(View.OnClickListener onClickListener) {
        this.mItemviewClickListener = onClickListener;
    }

    public void setMarginTopType(int i3) {
        if (this.mMarginTopType != i3) {
            this.mMarginTopType = i3;
            notifyChanged();
        }
    }

    public void setRightIconContentDescription(String str) {
        this.mRightIconContentDescription = str;
        notifyChanged();
    }

    public void setTextInReddot(String str) {
        this.mTextInReddot = str;
    }

    public void setTextInRight(String str) {
        if (TextUtils.equals(str, this.mTextInRight)) {
            return;
        }
        this.mTextInRight = str;
        notifyChanged();
    }

    public void setTitleIconClickListener(View.OnClickListener onClickListener) {
        this.mTitleIconClickListener = onClickListener;
    }

    public void setTitleIconContentDescription(String str) {
        this.mTitleIconContentDescription = str;
        notifyChanged();
    }

    public void setTitleType(int i3) {
        if (this.mTitleType != i3) {
            this.mTitleType = i3;
            notifyChanged();
        }
    }

    public void setWidgetLayoutClickListener(View.OnClickListener onClickListener) {
        this.mWidgetLayoutClickListener = onClickListener;
    }

    public void setWidgetLayoutRes(int i3) {
        this.mWidgetLayoutRes = i3;
    }

    public View getWidgetLayout() {
        return this.mWidgetLayout;
    }
}