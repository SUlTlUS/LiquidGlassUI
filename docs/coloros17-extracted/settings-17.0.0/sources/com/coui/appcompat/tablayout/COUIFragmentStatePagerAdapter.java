package com.coui.appcompat.tablayout;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/* JADX INFO: loaded from: classes7.dex */
public abstract class COUIFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    public COUIFragmentStatePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public int getPageIcon(int i3) {
        return 0;
    }
}