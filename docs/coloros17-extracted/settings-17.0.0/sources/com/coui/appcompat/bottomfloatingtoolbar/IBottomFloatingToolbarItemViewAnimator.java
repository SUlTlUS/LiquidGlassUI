package com.coui.appcompat.bottomfloatingtoolbar;

import android.view.View;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes7.dex */
interface IBottomFloatingToolbarItemViewAnimator {
    default void animationRollBack() {
    }

    default List<View> getAnimatingViews() {
        return new ArrayList();
    }

    default void handleOverflowItem(COUIBottomFloatingToolbarItemView cOUIBottomFloatingToolbarItemView, boolean z2) {
    }

    default boolean isRunningAnimation() {
        return false;
    }

    default void onMenuViewWidthChanged(int i3, int i4) {
    }

    default void setAttachAnimation(Runnable runnable) {
    }

    default void setItemList(ArrayList<View> arrayList) {
    }

    default void setLayoutAnimation(Runnable runnable) {
    }

    default void skipAnimation() {
    }

    default void updateAllAnimatingViews() {
    }
}