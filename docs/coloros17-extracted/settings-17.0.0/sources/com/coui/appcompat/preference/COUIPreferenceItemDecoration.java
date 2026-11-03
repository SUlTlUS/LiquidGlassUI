package com.coui.appcompat.preference;

import android.content.Context;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroupAdapter;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.COUIRecyclerView;
import androidx.recyclerview.widget.RecyclerView;
import com.coui.appcompat.cardlist.COUICardListSelectedItemLayout;

/* JADX INFO: loaded from: classes.dex */
public class COUIPreferenceItemDecoration extends COUIRecyclerView.COUIDividerItemDecoration {
    private final int[] mChildLocation;
    private final int[] mItemLocation;
    private PreferenceScreen mPreferenceScreen;

    public COUIPreferenceItemDecoration(Context context, PreferenceScreen preferenceScreen) {
        super(context);
        this.mItemLocation = new int[2];
        this.mChildLocation = new int[2];
        this.mPreferenceScreen = preferenceScreen;
    }

    private static boolean isFadingIn(View view) {
        return view != null && view.getAlpha() < 1.0f;
    }

    private static boolean isItemSelected(View view, Preference preference) {
        if ((view instanceof COUICardListSelectedItemLayout) && ((COUICardListSelectedItemLayout) view).getIsSelected()) {
            return true;
        }
        return isPreferenceSelected(preference);
    }

    private static boolean isPreferenceSelected(Preference preference) {
        return (preference instanceof COUIPreference) && preference.isVisible() && ((COUIPreference) preference).getIsSelected();
    }

    private boolean shouldHideDividerWhenSelected(RecyclerView recyclerView, int i3) {
        int childAdapterPosition;
        View childAt = recyclerView.getChildAt(i3);
        if (childAt == null) {
            return false;
        }
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (!(adapter instanceof PreferenceGroupAdapter) || (childAdapterPosition = recyclerView.getChildAdapterPosition(childAt)) == -1) {
            return false;
        }
        PreferenceGroupAdapter preferenceGroupAdapter = (PreferenceGroupAdapter) adapter;
        if (isItemSelected(childAt, preferenceGroupAdapter.getItem(childAdapterPosition))) {
            return true;
        }
        int i4 = childAdapterPosition + 1;
        if (i4 < adapter.getItemCount()) {
            if (isPreferenceSelected(preferenceGroupAdapter.getItem(i4))) {
                return true;
            }
            View childAt2 = recyclerView.getChildAt(i3 + 1);
            if (childAt2 != null && recyclerView.getChildAdapterPosition(childAt2) == i4 && (childAt2 instanceof COUICardListSelectedItemLayout) && childAt2.getVisibility() == 0 && childAt2.getHeight() > 0 && ((COUICardListSelectedItemLayout) childAt2).getIsSelected()) {
                return true;
            }
        }
        return false;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.COUIDividerItemDecoration
    public int getDividerInsetEnd(RecyclerView recyclerView, int i3) {
        int width;
        int width2;
        if (this.mPreferenceScreen == null) {
            return super.getDividerInsetEnd(recyclerView, i3);
        }
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter instanceof PreferenceGroupAdapter) {
            View childAt = recyclerView.getChildAt(i3);
            Object item = ((PreferenceGroupAdapter) adapter).getItem(recyclerView.getChildAdapterPosition(childAt));
            if (item != null && (item instanceof COUIRecyclerView.ICOUIDividerDecorationInterface)) {
                boolean z2 = childAt.getLayoutDirection() == 1;
                COUIRecyclerView.ICOUIDividerDecorationInterface iCOUIDividerDecorationInterface = (COUIRecyclerView.ICOUIDividerDecorationInterface) item;
                View dividerEndAlignView = iCOUIDividerDecorationInterface.getDividerEndAlignView();
                if (dividerEndAlignView == null) {
                    return iCOUIDividerDecorationInterface.getMDividerDefaultHorizontalPadding();
                }
                childAt.getLocationInWindow(this.mItemLocation);
                dividerEndAlignView.getLocationInWindow(this.mChildLocation);
                if (z2) {
                    width = this.mChildLocation[0] + dividerEndAlignView.getPaddingEnd();
                    width2 = this.mItemLocation[0];
                } else {
                    width = this.mItemLocation[0] + childAt.getWidth();
                    width2 = (this.mChildLocation[0] + dividerEndAlignView.getWidth()) - dividerEndAlignView.getPaddingEnd();
                }
                return width - width2;
            }
        }
        return super.getDividerInsetStart(recyclerView, i3);
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.COUIDividerItemDecoration
    public int getDividerInsetStart(RecyclerView recyclerView, int i3) {
        int paddingStart;
        int width;
        if (this.mPreferenceScreen == null) {
            return super.getDividerInsetStart(recyclerView, i3);
        }
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter instanceof PreferenceGroupAdapter) {
            View childAt = recyclerView.getChildAt(i3);
            Object item = ((PreferenceGroupAdapter) adapter).getItem(recyclerView.getChildAdapterPosition(childAt));
            if (item != null && (item instanceof COUIRecyclerView.ICOUIDividerDecorationInterface)) {
                boolean z2 = childAt.getLayoutDirection() == 1;
                COUIRecyclerView.ICOUIDividerDecorationInterface iCOUIDividerDecorationInterface = (COUIRecyclerView.ICOUIDividerDecorationInterface) item;
                View dividerStartAlignView = iCOUIDividerDecorationInterface.getDividerStartAlignView();
                if (dividerStartAlignView == null) {
                    return iCOUIDividerDecorationInterface.getDividerStartInset();
                }
                childAt.getLocationInWindow(this.mItemLocation);
                dividerStartAlignView.getLocationInWindow(this.mChildLocation);
                if (z2) {
                    paddingStart = this.mItemLocation[0] + childAt.getWidth();
                    width = (this.mChildLocation[0] + dividerStartAlignView.getWidth()) - dividerStartAlignView.getPaddingStart();
                } else {
                    paddingStart = this.mChildLocation[0] + dividerStartAlignView.getPaddingStart();
                    width = this.mItemLocation[0];
                }
                return paddingStart - width;
            }
        }
        return super.getDividerInsetStart(recyclerView, i3);
    }

    public PreferenceScreen getPreferenceScreen() {
        return this.mPreferenceScreen;
    }

    public void onDestroy() {
        this.mPreferenceScreen = null;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.COUIDividerItemDecoration
    public boolean shouldDrawDivider(RecyclerView recyclerView, int i3) {
        Object item;
        if (this.mPreferenceScreen == null || shouldHideDividerWhenSelected(recyclerView, i3)) {
            return false;
        }
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter instanceof PreferenceGroupAdapter) {
            View childAt = recyclerView.getChildAt(i3);
            View childAt2 = recyclerView.getChildAt(i3 + 1);
            if (!isFadingIn(childAt) && !isFadingIn(childAt2) && (item = ((PreferenceGroupAdapter) adapter).getItem(recyclerView.getChildAdapterPosition(childAt))) != null && (item instanceof COUIRecyclerView.ICOUIDividerDecorationInterface)) {
                return ((COUIRecyclerView.ICOUIDividerDecorationInterface) item).drawDivider();
            }
        }
        return false;
    }
}