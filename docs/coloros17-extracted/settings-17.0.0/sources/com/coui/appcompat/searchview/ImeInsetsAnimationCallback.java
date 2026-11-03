package com.coui.appcompat.searchview;

import android.view.WindowInsets;
import androidx.core.view.WindowInsetsAnimationCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001:\u0001\u0018B\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u001e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fH\u0016J\u0018\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u0012H\u0016J\u000e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0006R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\n¨\u0006\u0019"}, d2 = {"Lcom/coui/appcompat/searchview/ImeInsetsAnimationCallback;", "Landroidx/core/view/WindowInsetsAnimationCompat$Callback;", "dispatchMode", "", "(I)V", "imeListener", "Lcom/coui/appcompat/searchview/ImeInsetsAnimationCallback$OnImeAnimationListener;", "getImeListener", "()Lcom/coui/appcompat/searchview/ImeInsetsAnimationCallback$OnImeAnimationListener;", "setImeListener", "(Lcom/coui/appcompat/searchview/ImeInsetsAnimationCallback$OnImeAnimationListener;)V", "onProgress", "Landroidx/core/view/WindowInsetsCompat;", "insets", "runningAnimations", "", "Landroidx/core/view/WindowInsetsAnimationCompat;", "onStart", "Landroidx/core/view/WindowInsetsAnimationCompat$BoundsCompat;", "animation", "bounds", "setImeAnimationListener", "", "listener", "OnImeAnimationListener", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ImeInsetsAnimationCallback extends WindowInsetsAnimationCompat.Callback {
    private OnImeAnimationListener imeListener;

    /* JADX INFO: loaded from: classes.dex */
    @Metadata(d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&¨\u0006\u0004"}, d2 = {"Lcom/coui/appcompat/searchview/ImeInsetsAnimationCallback$OnImeAnimationListener;", "", "onImeAnimStart", "", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public interface OnImeAnimationListener {
        void onImeAnimStart();
    }

    public ImeInsetsAnimationCallback() {
        this(0, 1, null);
    }

    public final OnImeAnimationListener getImeListener() {
        return this.imeListener;
    }

    @Override // androidx.core.view.WindowInsetsAnimationCompat.Callback
    public WindowInsetsCompat onProgress(WindowInsetsCompat insets, List<WindowInsetsAnimationCompat> runningAnimations) {
        Intrinsics.checkNotNullParameter(insets, "insets");
        Intrinsics.checkNotNullParameter(runningAnimations, "runningAnimations");
        return insets;
    }

    @Override // androidx.core.view.WindowInsetsAnimationCompat.Callback
    public WindowInsetsAnimationCompat.BoundsCompat onStart(WindowInsetsAnimationCompat animation, WindowInsetsAnimationCompat.BoundsCompat bounds) {
        OnImeAnimationListener onImeAnimationListener;
        Intrinsics.checkNotNullParameter(animation, "animation");
        Intrinsics.checkNotNullParameter(bounds, "bounds");
        if ((animation.getTypeMask() & WindowInsets.Type.ime()) != 0 && (onImeAnimationListener = this.imeListener) != null) {
            onImeAnimationListener.onImeAnimStart();
        }
        WindowInsetsAnimationCompat.BoundsCompat boundsCompatOnStart = super.onStart(animation, bounds);
        Intrinsics.checkNotNullExpressionValue(boundsCompatOnStart, "onStart(...)");
        return boundsCompatOnStart;
    }

    public final void setImeAnimationListener(OnImeAnimationListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.imeListener = listener;
    }

    public final void setImeListener(OnImeAnimationListener onImeAnimationListener) {
        this.imeListener = onImeAnimationListener;
    }

    public /* synthetic */ ImeInsetsAnimationCallback(int i3, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this((i4 & 1) != 0 ? 0 : i3);
    }

    public ImeInsetsAnimationCallback(int i3) {
        super(i3);
    }
}