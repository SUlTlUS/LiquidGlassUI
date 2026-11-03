package com.coui.appcompat;

import java.lang.ref.WeakReference;
import kotlin.Metadata;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002¨\u0006\u0003"}, d2 = {"<anonymous>", "", "run", "com/coui/appcompat/COUIMaterialStateManager$dispatchOnMainThread$1"}, k = 3, mv = {1, 9, 0}, xi = 48)
public final class COUIMaterialStateManager$updateBlurStates$$inlined$dispatchOnMainThread$2 implements Runnable {
    final /* synthetic */ boolean $newState$inlined;

    public COUIMaterialStateManager$updateBlurStates$$inlined$dispatchOnMainThread$2(boolean z2) {
        this.$newState$inlined = z2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        for (WeakReference weakReference : COUIMaterialStateManager.access$getMaterialStateListeners$p()) {
            OnMaterialStateChangeListener onMaterialStateChangeListener = (OnMaterialStateChangeListener) weakReference.get();
            if (onMaterialStateChangeListener == null) {
                COUIMaterialStateManager.access$getMaterialStateListeners$p().remove(weakReference);
            } else {
                onMaterialStateChangeListener.onMaterialStateChange(16, this.$newState$inlined);
            }
        }
    }
}