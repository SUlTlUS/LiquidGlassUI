package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import com.coui.appcompat.progressbar.COUICompProgressIndicator;
import com.support.preference.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUILoadingPreferenceCategory extends COUIPreferenceCategory {
    private static final String TAG = "LoadingCategory";
    private String mLoadingText;
    private TextView mLoadingTextView;
    private LoadingType mLoadingType;
    private COUICompProgressIndicator mLoadingView;
    private int mWidgetLayoutAfterLoading;
    private int mWidgetLayoutBeforeLoading;

    public enum LoadingType {
        LOADING,
        PAUSE,
        INVISIBLE,
        AFTER_LOADING,
        BEFORE_LOADING
    }

    public COUILoadingPreferenceCategory(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mLoadingType = LoadingType.LOADING;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUILoadingPreferenceCategory, 0, 0);
        this.mWidgetLayoutAfterLoading = typedArrayObtainStyledAttributes.getResourceId(R.styleable.COUILoadingPreferenceCategory_coui_loading_after_layout, 0);
        this.mWidgetLayoutBeforeLoading = typedArrayObtainStyledAttributes.getResourceId(R.styleable.COUILoadingPreferenceCategory_coui_loading_before_layout, 0);
        this.mLoadingText = typedArrayObtainStyledAttributes.getString(R.styleable.COUILoadingPreferenceCategory_text_in_loading);
        typedArrayObtainStyledAttributes.recycle();
        if (this.mWidgetLayoutBeforeLoading != 0) {
            this.mLoadingType = LoadingType.BEFORE_LOADING;
        }
    }

    public LoadingType getLoadingType() {
        return this.mLoadingType;
    }

    public void hideLoading() {
        LoadingType loadingType = this.mLoadingType;
        if (loadingType == LoadingType.AFTER_LOADING) {
            Log.e(TAG, "It is no longer loading state");
            return;
        }
        LoadingType loadingType2 = LoadingType.INVISIBLE;
        if (loadingType != loadingType2) {
            this.mLoadingType = loadingType2;
            notifyChanged();
        }
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceCategory, androidx.preference.PreferenceCategory, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        int iOrdinal = this.mLoadingType.ordinal();
        if (iOrdinal == 0) {
            setWidgetLayoutRes(R.layout.coui_preference_category_widget_layout_loading);
            super.onBindViewHolder(preferenceViewHolder);
            this.mLoadingView = (COUICompProgressIndicator) getWidgetLayout().findViewById(R.id.catagory_loading);
            this.mLoadingTextView = (TextView) getWidgetLayout().findViewById(R.id.text_in_loading);
            this.mLoadingView.setVisibility(0);
            if (this.mLoadingView.getAnimationView() != null) {
                this.mLoadingView.getAnimationView().playAnimation();
            }
            if (TextUtils.isEmpty(this.mLoadingText)) {
                this.mLoadingTextView.setVisibility(8);
            } else {
                this.mLoadingTextView.setText(this.mLoadingText);
                this.mLoadingTextView.setVisibility(0);
            }
            if (TextUtils.isEmpty(this.mLoadingText)) {
                getWidgetLayout().setBackground(null);
                return;
            }
            return;
        }
        if (iOrdinal == 1) {
            COUICompProgressIndicator cOUICompProgressIndicator = this.mLoadingView;
            if (cOUICompProgressIndicator != null) {
                cOUICompProgressIndicator.setVisibility(0);
                this.mLoadingView.getAnimationView().pauseAnimation();
                return;
            }
            return;
        }
        if (iOrdinal == 2) {
            COUICompProgressIndicator cOUICompProgressIndicator2 = this.mLoadingView;
            if (cOUICompProgressIndicator2 != null) {
                cOUICompProgressIndicator2.setVisibility(8);
                return;
            }
            return;
        }
        if (iOrdinal == 3) {
            setWidgetLayoutRes(this.mWidgetLayoutAfterLoading);
            super.onBindViewHolder(preferenceViewHolder);
        } else {
            if (iOrdinal != 4) {
                return;
            }
            setWidgetLayoutRes(this.mWidgetLayoutBeforeLoading);
            super.onBindViewHolder(preferenceViewHolder);
        }
    }

    public void pauseLoading() {
        LoadingType loadingType = this.mLoadingType;
        if (loadingType == LoadingType.AFTER_LOADING) {
            Log.e(TAG, "It is no longer loading state");
            return;
        }
        LoadingType loadingType2 = LoadingType.PAUSE;
        if (loadingType != loadingType2) {
            this.mLoadingType = loadingType2;
            notifyChanged();
        }
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceCategory
    public boolean rightTextfixSecondaryColor() {
        return true;
    }

    public void setLoadingText(String str) {
        if (TextUtils.equals(this.mLoadingText, str)) {
            return;
        }
        this.mLoadingText = str;
        notifyChanged();
    }

    public void setShowAfterView() {
        setShowAfterView(this.mWidgetLayoutAfterLoading);
    }

    public void startLoading() {
        LoadingType loadingType = this.mLoadingType;
        LoadingType loadingType2 = LoadingType.LOADING;
        if (loadingType != loadingType2) {
            this.mLoadingType = loadingType2;
            notifyChanged();
        }
    }

    public void setShowAfterView(int i3) {
        this.mWidgetLayoutAfterLoading = i3;
        this.mLoadingType = LoadingType.AFTER_LOADING;
        notifyChanged();
    }
}