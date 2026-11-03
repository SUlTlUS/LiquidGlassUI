package com.coui.appcompat.poplist;

import android.content.Context;
import com.coui.appcompat.state.COUIMaskEffectDrawable;

/* JADX INFO: loaded from: classes7.dex */
public class ListItemMaskEffectDrawable extends COUIMaskEffectDrawable {
    public ListItemMaskEffectDrawable mChild;
    public ListItemMaskEffectDrawable mParent;

    public ListItemMaskEffectDrawable(Context context, int i3, ListItemMaskEffectDrawable listItemMaskEffectDrawable) {
        super(context, i3);
        this.mChild = null;
        this.mParent = null;
        if (listItemMaskEffectDrawable == null) {
            this.mChild = new ListItemMaskEffectDrawable(context, i3, this);
        } else {
            this.mParent = listItemMaskEffectDrawable;
        }
        setIsRoundStyle(false);
        enableFocusedState(false);
    }

    private void notifyTouchEntered() {
        super.setTouchEntered();
    }

    private void notifyTouchExited() {
        super.setTouchExited();
    }

    public ListItemMaskEffectDrawable getChild() {
        return this.mChild;
    }

    @Override // com.coui.appcompat.state.StatefulDrawable, com.coui.appcompat.state.DrawableStateProxy
    public void setTouchEntered() {
        ListItemMaskEffectDrawable listItemMaskEffectDrawable = this.mChild;
        if (listItemMaskEffectDrawable != null) {
            listItemMaskEffectDrawable.notifyTouchEntered();
        }
        ListItemMaskEffectDrawable listItemMaskEffectDrawable2 = this.mParent;
        if (listItemMaskEffectDrawable2 != null) {
            listItemMaskEffectDrawable2.notifyTouchEntered();
        }
        notifyTouchEntered();
    }

    @Override // com.coui.appcompat.state.StatefulDrawable, com.coui.appcompat.state.DrawableStateProxy
    public void setTouchExited() {
        ListItemMaskEffectDrawable listItemMaskEffectDrawable = this.mChild;
        if (listItemMaskEffectDrawable != null) {
            listItemMaskEffectDrawable.notifyTouchExited();
        }
        ListItemMaskEffectDrawable listItemMaskEffectDrawable2 = this.mParent;
        if (listItemMaskEffectDrawable2 != null) {
            listItemMaskEffectDrawable2.notifyTouchExited();
        }
        notifyTouchExited();
    }
}