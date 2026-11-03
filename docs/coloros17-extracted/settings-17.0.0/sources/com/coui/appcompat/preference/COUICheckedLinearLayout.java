package com.coui.appcompat.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.Checkable;
import android.widget.LinearLayout;

/* JADX INFO: loaded from: classes.dex */
public class COUICheckedLinearLayout extends LinearLayout implements Checkable {
    public COUICheckedLinearLayout(Context context) {
        this(context, null);
    }

    @Override // android.widget.Checkable
    public boolean isChecked() {
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            KeyEvent.Callback childAt = getChildAt(i3);
            if (childAt instanceof Checkable) {
                return ((Checkable) childAt).isChecked();
            }
        }
        return false;
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean z2) {
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            KeyEvent.Callback childAt = getChildAt(i3);
            if (childAt instanceof Checkable) {
                ((Checkable) childAt).setChecked(z2);
            }
        }
    }

    @Override // android.widget.Checkable
    public void toggle() {
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            KeyEvent.Callback childAt = getChildAt(i3);
            if (childAt instanceof Checkable) {
                ((Checkable) childAt).toggle();
            }
        }
    }

    public COUICheckedLinearLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUICheckedLinearLayout(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0);
    }

    public COUICheckedLinearLayout(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3);
    }
}