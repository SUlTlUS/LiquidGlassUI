package com.coui.appcompat.preference;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.COUIRecyclerView;
import androidx.recyclerview.widget.RecyclerView;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.toolbar.COUIToolbar;
import com.google.android.material.appbar.COUIDividerAppBarLayout;
import com.support.preference.R;

/* JADX INFO: loaded from: classes7.dex */
public abstract class COUIPreferenceWithAppbarFragment extends COUIPreferenceFragment {
    private RecyclerView mRecyclerView = null;
    private COUIToolbar mToolbar = null;
    private boolean mContainerFitsSystemWindows = false;

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIPreferenceWithAppbarFragment$1, reason: invalid class name */
    public class AnonymousClass1 implements View.OnClickListener {
        public AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            FragmentActivity activity = COUIPreferenceWithAppbarFragment.this.getActivity();
            if (activity != null) {
                activity.finish();
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIPreferenceWithAppbarFragment$2, reason: invalid class name */
    public class AnonymousClass2 implements ViewTreeObserver.OnGlobalLayoutListener {
        public AnonymousClass2() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            RecyclerView.LayoutManager layoutManager = COUIPreferenceWithAppbarFragment.access$000(COUIPreferenceWithAppbarFragment.this).getLayoutManager();
            View viewFindViewByPosition = layoutManager == null ? null : layoutManager.findViewByPosition(0);
            if ((viewFindViewByPosition instanceof ViewGroup) && viewFindViewByPosition.getVisibility() == 4 && ((ViewGroup) viewFindViewByPosition).getChildCount() == 0) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) viewFindViewByPosition.getLayoutParams();
                if (((ViewGroup.MarginLayoutParams) layoutParams).height != 0) {
                    ((ViewGroup.MarginLayoutParams) layoutParams).height = 0;
                    viewFindViewByPosition.setLayoutParams(layoutParams);
                }
            }
            COUIPreferenceWithAppbarFragment.access$000(COUIPreferenceWithAppbarFragment.this).getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

    public static /* synthetic */ RecyclerView access$000(COUIPreferenceWithAppbarFragment cOUIPreferenceWithAppbarFragment) {
        return cOUIPreferenceWithAppbarFragment.mRecyclerView;
    }

    private int getStatusBarHeight(Context context) {
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return context.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    private View getStatusBarView() {
        ImageView imageView = new ImageView(getActivity());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, getStatusBarHeight(imageView.getContext())));
        return imageView;
    }

    public abstract String getTitle();

    public COUIToolbar getToolbar() {
        return this.mToolbar;
    }

    public boolean isCustomWindowBackground() {
        return false;
    }

    public boolean needStatusBarViewHolder() {
        if (Build.VERSION.SDK_INT > 34) {
            return !this.mContainerFitsSystemWindows;
        }
        return true;
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat
    public RecyclerView onCreateRecyclerView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        COUIRecyclerView cOUIRecyclerView = (COUIRecyclerView) layoutInflater.inflate(R.layout.coui_preference_percent_recyclerview, viewGroup, false);
        cOUIRecyclerView.setEnablePointerDownAction(false);
        cOUIRecyclerView.setLayoutManager(onCreateLayoutManager());
        COUIDarkModeUtil.setForceDarkAllow(cOUIRecyclerView, false);
        return cOUIRecyclerView;
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        if (Build.VERSION.SDK_INT > 34 && viewGroup != null) {
            this.mContainerFitsSystemWindows = viewGroup.getFitsSystemWindows();
        }
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        COUIToolbar cOUIToolbar = (COUIToolbar) view.findViewById(R.id.toolbar);
        this.mToolbar = cOUIToolbar;
        if (cOUIToolbar == null) {
            return;
        }
        cOUIToolbar.setNavigationIcon(com.support.appcompat.R.drawable.coui_back_arrow);
        this.mToolbar.setNavigationOnClickListener(new AnonymousClass1());
        this.mToolbar.setTitle(getTitle());
        COUIDividerAppBarLayout cOUIDividerAppBarLayout = (COUIDividerAppBarLayout) view.findViewById(R.id.appbar_layout);
        if (cOUIDividerAppBarLayout != null) {
            if (needStatusBarViewHolder()) {
                cOUIDividerAppBarLayout.setPadding(cOUIDividerAppBarLayout.getPaddingStart(), getStatusBarHeight(getActivity()), cOUIDividerAppBarLayout.getPaddingEnd(), cOUIDividerAppBarLayout.getPaddingBottom());
            }
            cOUIDividerAppBarLayout.setTouchscreenBlocksFocus(false);
            cOUIDividerAppBarLayout.setGradientBlurEnabled(true);
        }
        RecyclerView listView = getListView();
        this.mRecyclerView = listView;
        if (listView != null) {
            ViewCompat.setNestedScrollingEnabled(listView, true);
            if (cOUIDividerAppBarLayout != null) {
                cOUIDividerAppBarLayout.bindRecyclerView(this.mRecyclerView);
            }
            this.mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new AnonymousClass2());
        }
        if (getActivity() == null || isCustomWindowBackground()) {
            return;
        }
        getActivity().getWindow().setBackgroundDrawableResource(R.drawable.coui_window_background_with_card_selector);
    }
}