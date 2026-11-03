package com.coui.appcompat.chip;

import android.view.View;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.Ref;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "view", "Landroid/view/View;", "invoke"}, k = 3, mv = {1, 9, 0}, xi = 48)
public final class COUIChipTabGroupFragmentMediator$startFadeInAnimationForFragment$startAnimation$1 extends Lambda implements Function1<View, Unit> {
    final /* synthetic */ Ref.BooleanRef $animationStarted;
    final /* synthetic */ int $delayMs;
    final /* synthetic */ COUIChipTabGroupFragmentMediator this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUIChipTabGroupFragmentMediator$startFadeInAnimationForFragment$startAnimation$1(Ref.BooleanRef booleanRef, COUIChipTabGroupFragmentMediator cOUIChipTabGroupFragmentMediator, int i3) {
        super(1);
        this.$animationStarted = booleanRef;
        this.this$0 = cOUIChipTabGroupFragmentMediator;
        this.$delayMs = i3;
    }

    public static /* synthetic */ void a(COUIChipTabGroupFragmentMediator cOUIChipTabGroupFragmentMediator, View view, int i3) {
        invoke$lambda$0(cOUIChipTabGroupFragmentMediator, view, i3);
    }

    private static final void invoke$lambda$0(COUIChipTabGroupFragmentMediator this$0, View view, int i3) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(view, "$view");
        COUIChipTabGroupFragmentMediator.access$startFadeInAnimation(this$0, view, i3);
    }

    @Override // kotlin.jvm.functions.Function1
    public /* bridge */ /* synthetic */ Unit invoke(View view) {
        invoke2(view);
        return Unit.INSTANCE;
    }

    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
    public final void invoke2(View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        Ref.BooleanRef booleanRef = this.$animationStarted;
        if (booleanRef.element) {
            return;
        }
        booleanRef.element = true;
        view.setAlpha(0.0f);
        view.post(new u(this.this$0, view, this.$delayMs));
    }
}