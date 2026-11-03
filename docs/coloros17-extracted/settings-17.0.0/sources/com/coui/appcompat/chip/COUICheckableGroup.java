package com.coui.appcompat.chip;

import android.view.View;
import android.view.ViewGroup;
import com.coui.appcompat.chip.COUICheckable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes7.dex */
public class COUICheckableGroup<T extends COUICheckable<T>> {
    private final Map<Integer, T> mCheckables = new HashMap();
    private final Set<Integer> mCheckedIds = new HashSet();
    private OnCheckedStateChangeListener mOnCheckedStateChangeListener;
    private boolean mSelectionRequired;
    private boolean mSingleSelection;

    public interface OnCheckedStateChangeListener {
        void onCheckedStateChanged(Set<Integer> set);
    }

    public static /* synthetic */ void a(COUICheckableGroup cOUICheckableGroup, COUICheckable cOUICheckable, boolean z2) {
        if (z2) {
            if (!cOUICheckableGroup.checkInternal(cOUICheckable)) {
                return;
            }
        } else if (!cOUICheckableGroup.uncheckInternal(cOUICheckable, cOUICheckableGroup.mSelectionRequired)) {
            return;
        }
        cOUICheckableGroup.onCheckedStateChanged();
    }

    private boolean checkInternal(COUICheckable<T> cOUICheckable) {
        int id = cOUICheckable.getId();
        if (this.mCheckedIds.contains(Integer.valueOf(id))) {
            return false;
        }
        T t2 = this.mCheckables.get(Integer.valueOf(getSingleCheckedId()));
        if (t2 != null) {
            uncheckInternal(t2, false);
        }
        boolean zAdd = this.mCheckedIds.add(Integer.valueOf(id));
        if (!cOUICheckable.isChecked()) {
            cOUICheckable.setChecked(true);
        }
        return zAdd;
    }

    private void onCheckedStateChanged() {
        OnCheckedStateChangeListener onCheckedStateChangeListener = this.mOnCheckedStateChangeListener;
        if (onCheckedStateChangeListener != null) {
            onCheckedStateChangeListener.onCheckedStateChanged(getCheckedIds());
        }
    }

    private boolean uncheckInternal(COUICheckable<T> cOUICheckable, boolean z2) {
        int id = cOUICheckable.getId();
        if (!this.mCheckedIds.contains(Integer.valueOf(id))) {
            return false;
        }
        if (z2 && this.mCheckedIds.size() == 1 && this.mCheckedIds.contains(Integer.valueOf(id))) {
            cOUICheckable.setChecked(true);
            return false;
        }
        boolean zRemove = this.mCheckedIds.remove(Integer.valueOf(id));
        if (cOUICheckable.isChecked()) {
            cOUICheckable.setChecked(false);
        }
        return zRemove;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void addCheckable(T t2) {
        this.mCheckables.put(Integer.valueOf(t2.getId()), t2);
        if (t2.isChecked()) {
            checkInternal(t2);
        }
        t2.setInternalOnCheckedChangeListener(new a(this));
    }

    public void check(int i3) {
        T t2 = this.mCheckables.get(Integer.valueOf(i3));
        if (t2 != null && checkInternal(t2)) {
            onCheckedStateChanged();
        }
    }

    public void clearCheck() {
        boolean zIsEmpty = this.mCheckedIds.isEmpty();
        Iterator<T> it = this.mCheckables.values().iterator();
        while (it.hasNext()) {
            uncheckInternal(it.next(), false);
        }
        if (zIsEmpty) {
            return;
        }
        onCheckedStateChanged();
    }

    public Set<Integer> getCheckedIds() {
        return new HashSet(this.mCheckedIds);
    }

    public List<Integer> getCheckedIdsSortedByChildOrder(ViewGroup viewGroup) {
        Set<Integer> checkedIds = getCheckedIds();
        ArrayList arrayList = new ArrayList();
        for (int i3 = 0; i3 < viewGroup.getChildCount(); i3++) {
            View childAt = viewGroup.getChildAt(i3);
            if ((childAt instanceof COUICheckable) && checkedIds.contains(Integer.valueOf(childAt.getId()))) {
                arrayList.add(Integer.valueOf(childAt.getId()));
            }
        }
        return arrayList;
    }

    public int getSingleCheckedId() {
        if (!this.mSingleSelection || this.mCheckedIds.isEmpty()) {
            return -1;
        }
        return this.mCheckedIds.iterator().next().intValue();
    }

    public boolean isSelectionRequired() {
        return this.mSelectionRequired;
    }

    public boolean isSingleSelection() {
        return this.mSingleSelection;
    }

    public void removeCheckable(T t2) {
        t2.setInternalOnCheckedChangeListener(null);
        this.mCheckables.remove(Integer.valueOf(t2.getId()));
        this.mCheckedIds.remove(Integer.valueOf(t2.getId()));
    }

    public void setOnCheckedStateChangeListener(OnCheckedStateChangeListener onCheckedStateChangeListener) {
        this.mOnCheckedStateChangeListener = onCheckedStateChangeListener;
    }

    public void setSelectionRequired(boolean z2) {
        this.mSelectionRequired = z2;
    }

    public void setSingleSelection(boolean z2) {
        if (this.mSingleSelection != z2) {
            this.mSingleSelection = z2;
            clearCheck();
        }
    }

    public void uncheck(int i3) {
        T t2 = this.mCheckables.get(Integer.valueOf(i3));
        if (t2 != null && uncheckInternal(t2, this.mSelectionRequired)) {
            onCheckedStateChanged();
        }
    }
}