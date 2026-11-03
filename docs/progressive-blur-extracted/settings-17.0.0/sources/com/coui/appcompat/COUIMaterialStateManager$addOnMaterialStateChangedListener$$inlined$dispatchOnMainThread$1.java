package com.coui.appcompat;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import kotlin.Metadata;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002¨\u0006\u0003"}, d2 = {"<anonymous>", "", "run", "com/coui/appcompat/COUIMaterialStateManager$dispatchOnMainThread$1"}, k = 3, mv = {1, 9, 0}, xi = 48)
public final class COUIMaterialStateManager$addOnMaterialStateChangedListener$$inlined$dispatchOnMainThread$1 implements Runnable {
    final /* synthetic */ OnMaterialStateChangeListener $listener$inlined;

    public COUIMaterialStateManager$addOnMaterialStateChangedListener$$inlined$dispatchOnMainThread$1(OnMaterialStateChangeListener onMaterialStateChangeListener) {
        this.$listener$inlined = onMaterialStateChangeListener;
    }

    @Override // java.lang.Runnable
    public final void run() {
        CopyOnWriteArrayList copyOnWriteArrayListAccess$getMaterialStateListeners$p = COUIMaterialStateManager.access$getMaterialStateListeners$p();
        if (copyOnWriteArrayListAccess$getMaterialStateListeners$p == null || !copyOnWriteArrayListAccess$getMaterialStateListeners$p.isEmpty()) {
            Iterator it = copyOnWriteArrayListAccess$getMaterialStateListeners$p.iterator();
            while (it.hasNext()) {
                if (((WeakReference) it.next()).get() == this.$listener$inlined) {
                    if ((COUIMaterialStateManager.access$getRegisteredFlags$p() & 15) == 0) {
                        this.$listener$inlined.onMaterialStateChange(1, COUIMaterialStateManager.access$getMATERIAL_STROKE_ENABLE$p());
                        this.$listener$inlined.onMaterialStateChange(2, COUIMaterialStateManager.access$getMATERIAL_BLUR_ENABLE$p());
                        OnMaterialStateChangeListener onMaterialStateChangeListener = this.$listener$inlined;
                        COUIMaterialStateManager cOUIMaterialStateManager = COUIMaterialStateManager.INSTANCE;
                        onMaterialStateChangeListener.onMaterialStateChange(4, cOUIMaterialStateManager.getMATERIAL_SPOTLIGHT_ENABLE$coui_support_effect_release());
                        this.$listener$inlined.onMaterialStateChange(8, cOUIMaterialStateManager.getMATERIAL_CAUSTIC_SHADOW_ENABLE$coui_support_effect_release());
                    } else {
                        if ((COUIMaterialStateManager.access$getRegisteredFlags$p() & 1) == 1) {
                            this.$listener$inlined.onMaterialStateChange(1, COUIMaterialStateManager.access$getMATERIAL_STROKE_ENABLE$p());
                            this.$listener$inlined.onMaterialStateChange(8, COUIMaterialStateManager.INSTANCE.getMATERIAL_CAUSTIC_SHADOW_ENABLE$coui_support_effect_release());
                        }
                        if ((COUIMaterialStateManager.access$getRegisteredFlags$p() & 2) == 2) {
                            this.$listener$inlined.onMaterialStateChange(2, COUIMaterialStateManager.access$getMATERIAL_BLUR_ENABLE$p());
                            this.$listener$inlined.onMaterialStateChange(4, COUIMaterialStateManager.INSTANCE.getMATERIAL_SPOTLIGHT_ENABLE$coui_support_effect_release());
                        }
                    }
                    if (COUIMaterialStateManager.access$getRegisteredFlags$p() == 0 || (COUIMaterialStateManager.access$getRegisteredFlags$p() & 16) == 16) {
                        this.$listener$inlined.onMaterialStateChange(16, COUIMaterialStateManager.access$getWINDOW_BACKGROUND_BLUR_ENABLE$p());
                        return;
                    }
                    return;
                }
            }
        }
    }
}