package com.coui.appcompat.seekbar;

import com.coui.appcompat.seekbar.animation.SectionAnimationHandler;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "Lcom/coui/appcompat/seekbar/animation/SectionAnimationHandler;", "invoke"}, k = 3, mv = {1, 9, 0}, xi = 48)
public final class COUISectionSeekBar$mSectionAnimHandler$2 extends Lambda implements Function0<SectionAnimationHandler> {
    final /* synthetic */ COUISectionSeekBar this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUISectionSeekBar$mSectionAnimHandler$2(COUISectionSeekBar cOUISectionSeekBar) {
        super(0);
        this.this$0 = cOUISectionSeekBar;
    }

    @Override // kotlin.jvm.functions.Function0
    public /* bridge */ /* synthetic */ SectionAnimationHandler invoke() {
        return invoke();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    public final SectionAnimationHandler invoke() {
        return new SectionAnimationHandler(this.this$0.getState(), COUISectionSeekBar.access$getMSectionAnimCallback$p(this.this$0));
    }
}