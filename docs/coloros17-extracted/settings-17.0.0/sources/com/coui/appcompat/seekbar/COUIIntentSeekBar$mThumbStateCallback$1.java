package com.coui.appcompat.seekbar;

import com.coui.appcompat.seekbar.animation.IAnimationHandler;
import com.coui.appcompat.seekbar.animation.ThumbStateAnimationHandler;
import kotlin.Metadata;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\b\u0010\u0004\u001a\u00020\u0003H\u0016J\b\u0010\u0005\u001a\u00020\u0003H\u0016¨\u0006\u0006"}, d2 = {"com/coui/appcompat/seekbar/COUIIntentSeekBar$mThumbStateCallback$1", "Lcom/coui/appcompat/seekbar/animation/ThumbStateAnimationHandler$Callback;", "onPendingShrinkTriggered", "", "requestInvalidate", "stopExistingThumbScaleAnimation", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class COUIIntentSeekBar$mThumbStateCallback$1 implements ThumbStateAnimationHandler.Callback {
    final /* synthetic */ COUIIntentSeekBar this$0;

    public COUIIntentSeekBar$mThumbStateCallback$1(COUIIntentSeekBar cOUIIntentSeekBar) {
        this.this$0 = cOUIIntentSeekBar;
    }

    @Override // com.coui.appcompat.seekbar.animation.ThumbStateAnimationHandler.Callback
    public void onPendingShrinkTriggered() {
        COUIIntentSeekBar.access$executeThumbReleaseAfterDrag(this.this$0);
    }

    @Override // com.coui.appcompat.seekbar.animation.ThumbStateAnimationHandler.Callback
    public void requestInvalidate() {
        this.this$0.invalidate();
    }

    @Override // com.coui.appcompat.seekbar.animation.ThumbStateAnimationHandler.Callback
    public void stopExistingThumbScaleAnimation() {
        IAnimationHandler iAnimationHandlerAccess$getMAnimationHandler = COUIIntentSeekBar.access$getMAnimationHandler(this.this$0);
        if (iAnimationHandlerAccess$getMAnimationHandler != null) {
            iAnimationHandlerAccess$getMAnimationHandler.stopThumbScaleAnimation();
        }
    }
}