package com.coui.appcompat.seekbar.listener;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0016J\u0010\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u0016¨\u0006\n"}, d2 = {"Lcom/coui/appcompat/seekbar/listener/OnDeformedListener;", "", "onHeightDeformedChanged", "", "topDeformedValue", "", "bottomDeformedValue", "onScaleChanged", "bean", "Lcom/coui/appcompat/seekbar/listener/DeformedValueBean;", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface OnDeformedListener {

    @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class DefaultImpls {
        public static void onHeightDeformedChanged(OnDeformedListener onDeformedListener, float f3, float f4) {
        }

        public static void onScaleChanged(OnDeformedListener onDeformedListener, DeformedValueBean bean) {
            Intrinsics.checkNotNullParameter(bean, "bean");
        }
    }

    void onHeightDeformedChanged(float topDeformedValue, float bottomDeformedValue);

    void onScaleChanged(DeformedValueBean bean);
}