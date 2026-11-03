package com.coui.appcompat.seekbar.animation;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import com.coui.appcompat.seekbar.core.COUISeekBarCore;
import kotlin.Metadata;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H&J(\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH&J\u0010\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\bH&J\b\u0010\r\u001a\u00020\u0003H&J\b\u0010\u000e\u001a\u00020\u0003H&¨\u0006\u000f"}, d2 = {"Lcom/coui/appcompat/seekbar/animation/IAnimationHandler;", "", "startProgressAnimation", "", "from", "", TypedValues.TransitionType.S_TO, "fromUser", "", "seekBar", "Lcom/coui/appcompat/seekbar/core/COUISeekBarCore;", "startThumbScaleAnimation", "enlarge", "stopAllAnimations", "stopThumbScaleAnimation", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface IAnimationHandler {
    void startProgressAnimation(int from, int to);

    void startProgressAnimation(int from, int to, boolean fromUser, COUISeekBarCore seekBar);

    void startThumbScaleAnimation(boolean enlarge);

    void stopAllAnimations();

    void stopThumbScaleAnimation();
}