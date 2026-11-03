package com.coui.appcompat.seekbar;

import com.coui.appcompat.seekbar.animation.LoadingDragReturnHandler;
import kotlin.Metadata;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\u001b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\b\u0010\u0004\u001a\u00020\u0003H\u0016J\b\u0010\u0005\u001a\u00020\u0003H\u0016J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u0003H\u0016J\b\u0010\n\u001a\u00020\u0007H\u0016¨\u0006\u000b"}, d2 = {"com/coui/appcompat/seekbar/COUIIntentSeekBar$mLoadingDragReturnHandler$1", "Lcom/coui/appcompat/seekbar/animation/LoadingDragReturnHandler$Callback;", "getCurrentProgress", "", "getMax", "getMin", "onReturnAnimComplete", "", "setProgress", "progress", "stopPhysicsMove", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class COUIIntentSeekBar$mLoadingDragReturnHandler$1 implements LoadingDragReturnHandler.Callback {
    final /* synthetic */ COUIIntentSeekBar this$0;

    public COUIIntentSeekBar$mLoadingDragReturnHandler$1(COUIIntentSeekBar cOUIIntentSeekBar) {
        this.this$0 = cOUIIntentSeekBar;
    }

    @Override // com.coui.appcompat.seekbar.animation.LoadingDragReturnHandler.Callback
    public int getCurrentProgress() {
        return this.this$0.getState().getProgress();
    }

    @Override // com.coui.appcompat.seekbar.animation.LoadingDragReturnHandler.Callback
    public int getMax() {
        return this.this$0.getMax();
    }

    @Override // com.coui.appcompat.seekbar.animation.LoadingDragReturnHandler.Callback
    public int getMin() {
        return this.this$0.getMin();
    }

    @Override // com.coui.appcompat.seekbar.animation.LoadingDragReturnHandler.Callback
    public void onReturnAnimComplete() {
        COUIIntentSeekBar.OnLoadingDragReturnCompleteListener onLoadingDragReturnCompleteListenerAccess$getMOnLoadingDragReturnCompleteListener$p = COUIIntentSeekBar.access$getMOnLoadingDragReturnCompleteListener$p(this.this$0);
        if (onLoadingDragReturnCompleteListenerAccess$getMOnLoadingDragReturnCompleteListener$p != null) {
            onLoadingDragReturnCompleteListenerAccess$getMOnLoadingDragReturnCompleteListener$p.onLoadingDragReturnComplete(this.this$0);
        }
        COUIIntentSeekBar.access$applyVisualState(this.this$0);
        this.this$0.invalidate();
    }

    @Override // com.coui.appcompat.seekbar.animation.LoadingDragReturnHandler.Callback
    public void setProgress(int progress) {
        this.this$0.setProgress(progress);
    }

    @Override // com.coui.appcompat.seekbar.animation.LoadingDragReturnHandler.Callback
    public void stopPhysicsMove() {
        this.this$0.stopPhysicsMove();
    }
}