package com.coui.appcompat.seekbar;

import com.coui.appcompat.seekbar.animation.SectionAnimationHandler;
import com.coui.appcompat.seekbar.core.COUISeekBarCore;
import com.coui.appcompat.seekbar.feedback.VibrationFeedbackHandler;
import kotlin.Metadata;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\u001f\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\b*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\b\u0010\b\u001a\u00020\u0003H\u0016J\u0010\u0010\t\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\b\u0010\n\u001a\u00020\u0003H\u0016J\u0010\u0010\u000b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0018\u0010\f\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0007H\u0016J\u0010\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u000f"}, d2 = {"com/coui/appcompat/seekbar/COUISectionSeekBar$mSectionAnimCallback$1", "Lcom/coui/appcompat/seekbar/animation/SectionAnimationHandler$Callback;", "notifyProgressChanged", "", "progress", "", "fromUser", "", "performFeedback", "performVibrationForSection", "requestInvalidate", "setInternalProgress", "stopTrackingTouch", "isTouching", "syncProgressToParent", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class COUISectionSeekBar$mSectionAnimCallback$1 implements SectionAnimationHandler.Callback {
    final /* synthetic */ COUISectionSeekBar this$0;

    public COUISectionSeekBar$mSectionAnimCallback$1(COUISectionSeekBar cOUISectionSeekBar) {
        this.this$0 = cOUISectionSeekBar;
    }

    @Override // com.coui.appcompat.seekbar.animation.SectionAnimationHandler.Callback
    public void notifyProgressChanged(int progress, boolean fromUser) {
        COUISeekBarCore.OnSeekBarChangeListener onSeekBarChangeListenerAccess$getMOnSeekBarChangeListener = COUISectionSeekBar.access$getMOnSeekBarChangeListener(this.this$0);
        if (onSeekBarChangeListenerAccess$getMOnSeekBarChangeListener != null) {
            onSeekBarChangeListenerAccess$getMOnSeekBarChangeListener.onProgressChanged(this.this$0, progress, fromUser);
        }
    }

    @Override // com.coui.appcompat.seekbar.animation.SectionAnimationHandler.Callback
    public void performFeedback() {
        COUISectionSeekBar.access$performFeedback(this.this$0);
    }

    @Override // com.coui.appcompat.seekbar.animation.SectionAnimationHandler.Callback
    public void performVibrationForSection(int progress) {
        VibrationFeedbackHandler vibrationFeedbackHandlerAccess$getMVibrationHandler = COUISectionSeekBar.access$getMVibrationHandler(this.this$0);
        if (vibrationFeedbackHandlerAccess$getMVibrationHandler != null) {
            vibrationFeedbackHandlerAccess$getMVibrationHandler.setDragging(false);
        }
        VibrationFeedbackHandler vibrationFeedbackHandlerAccess$getMVibrationHandler2 = COUISectionSeekBar.access$getMVibrationHandler(this.this$0);
        if (vibrationFeedbackHandlerAccess$getMVibrationHandler2 != null) {
            vibrationFeedbackHandlerAccess$getMVibrationHandler2.setMinMax(this.this$0.getState().getMin(), this.this$0.getState().getMax());
        }
        VibrationFeedbackHandler vibrationFeedbackHandlerAccess$getMVibrationHandler3 = COUISectionSeekBar.access$getMVibrationHandler(this.this$0);
        if (vibrationFeedbackHandlerAccess$getMVibrationHandler3 != null) {
            vibrationFeedbackHandlerAccess$getMVibrationHandler3.performFeedback(progress);
        }
    }

    @Override // com.coui.appcompat.seekbar.animation.SectionAnimationHandler.Callback
    public void requestInvalidate() {
        this.this$0.invalidate();
    }

    @Override // com.coui.appcompat.seekbar.animation.SectionAnimationHandler.Callback
    public void setInternalProgress(int progress) {
        COUISectionSeekBar.access$setMInternalProgress(this.this$0, progress);
    }

    @Override // com.coui.appcompat.seekbar.animation.SectionAnimationHandler.Callback
    public void stopTrackingTouch(boolean fromUser, boolean isTouching) {
        COUISectionSeekBar.access$stopTrackingTouch(this.this$0, fromUser, isTouching);
    }

    @Override // com.coui.appcompat.seekbar.animation.SectionAnimationHandler.Callback
    public void syncProgressToParent(int progress) {
        this.this$0.syncProgressToParent(progress);
    }
}