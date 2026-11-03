package com.coui.appcompat.seekbar;

import com.coui.appcompat.seekbar.animation.ThumbStateAnimationHandler;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "Lcom/coui/appcompat/seekbar/animation/ThumbStateAnimationHandler;", "invoke"}, k = 3, mv = {1, 9, 0}, xi = 48)
public final class COUIIntentSeekBar$mThumbStateHandler$2 extends Lambda implements Function0<ThumbStateAnimationHandler> {
    final /* synthetic */ COUIIntentSeekBar this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUIIntentSeekBar$mThumbStateHandler$2(COUIIntentSeekBar cOUIIntentSeekBar) {
        super(0);
        this.this$0 = cOUIIntentSeekBar;
    }

    @Override // kotlin.jvm.functions.Function0
    public /* bridge */ /* synthetic */ ThumbStateAnimationHandler invoke() {
        return invoke();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    public final ThumbStateAnimationHandler invoke() {
        return new ThumbStateAnimationHandler(this.this$0.getState(), COUIIntentSeekBar.access$getMThumbStateCallback$p(this.this$0));
    }
}