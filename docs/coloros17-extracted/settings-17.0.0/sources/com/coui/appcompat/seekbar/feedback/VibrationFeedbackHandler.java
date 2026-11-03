package com.coui.appcompat.seekbar.feedback;

import android.content.Context;
import android.view.View;
import androidx.slice.core.SliceHints;
import com.coui.appcompat.vibrateutil.VibrateUtils;
import com.oplus.os.LinearmotorVibrator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import m0.a;
import m0.b;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000f\u0018\u0000 ,2\u00020\u0001:\u0001,B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010\u001b\u001a\u00020\u001cJ\u0012\u0010\u001d\u001a\u0004\u0018\u00010\u001e2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002J\u0010\u0010\u001f\u001a\u00020\u00062\u0006\u0010 \u001a\u00020\u0010H\u0002J\u000e\u0010!\u001a\u00020\u001c2\u0006\u0010 \u001a\u00020\u0010J\u0010\u0010\"\u001a\u00020\u001c2\u0006\u0010 \u001a\u00020\u0010H\u0002J\u0006\u0010#\u001a\u00020\u001cJ\u0006\u0010$\u001a\u00020\u001cJ\u000e\u0010%\u001a\u00020\u001c2\u0006\u0010&\u001a\u00020\u0006J\u0016\u0010'\u001a\u00020\u001c2\u0006\u0010(\u001a\u00020\u00102\u0006\u0010)\u001a\u00020\u0010J\u000e\u0010*\u001a\u00020\u001c2\u0006\u0010+\u001a\u00020\u0017R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\n\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0007\"\u0004\b\u000b\u0010\tR\u0010\u0010\f\u001a\u0004\u0018\u00010\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0001X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0018\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0007\"\u0004\b\u001a\u0010\t¨\u0006-"}, d2 = {"Lcom/coui/appcompat/seekbar/feedback/VibrationFeedbackHandler;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "isEnableAdaptiveVibrator", "", "()Z", "setEnableAdaptiveVibrator", "(Z)V", "isEnableVibrator", "setEnableVibrator", "mContext", "mHasMotorVibrator", "mIsDragging", "mLastRealProgress", "", "mLinearMotorVibrator", "mMax", "mMin", "mVibratorExecutor", "Ljava/util/concurrent/ExecutorService;", "mView", "Landroid/view/View;", "useSectionStyleFeedback", "getUseSectionStyleFeedback", "setUseSectionStyleFeedback", "clearPendingTasks", "", "getOrCreateLinearMotorVibrator", "Lcom/oplus/os/LinearmotorVibrator;", "performAdaptiveFeedback", "realProgress", "performFeedback", "performNormalFeedback", "release", "resetLastProgress", "setDragging", "dragging", "setMinMax", SliceHints.SUBTYPE_MIN, "max", "setView", "view", "Companion", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class VibrationFeedbackHandler {
    private static final int DEFAULT_MAX = 100;
    private static final String TAG = "VibrationFeedbackHandler";
    private boolean isEnableAdaptiveVibrator;
    private boolean isEnableVibrator;
    private Context mContext;
    private boolean mHasMotorVibrator;
    private boolean mIsDragging;
    private int mLastRealProgress;
    private Object mLinearMotorVibrator;
    private int mMax;
    private int mMin;
    private ExecutorService mVibratorExecutor;
    private View mView;
    private boolean useSectionStyleFeedback;

    public VibrationFeedbackHandler(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.isEnableVibrator = true;
        this.isEnableAdaptiveVibrator = true;
        this.mHasMotorVibrator = true;
        this.mContext = context;
        this.mLastRealProgress = -1;
        this.mMax = 100;
        this.mHasMotorVibrator = VibrateUtils.isLinearMotorVersion(context);
    }

    public static /* synthetic */ void a(VibrationFeedbackHandler vibrationFeedbackHandler, View view) {
        performNormalFeedback$lambda$1(vibrationFeedbackHandler, view);
    }

    public static /* synthetic */ void b(VibrationFeedbackHandler vibrationFeedbackHandler, LinearmotorVibrator linearmotorVibrator, int i3, int i4) {
        performAdaptiveFeedback$lambda$0(vibrationFeedbackHandler, linearmotorVibrator, i3, i4);
    }

    private final LinearmotorVibrator getOrCreateLinearMotorVibrator(Context context) {
        if (this.mLinearMotorVibrator == null) {
            LinearmotorVibrator linearMotorVibrator = VibrateUtils.getLinearMotorVibrator(context);
            this.mLinearMotorVibrator = linearMotorVibrator;
            this.mHasMotorVibrator = linearMotorVibrator != null;
        }
        Object obj = this.mLinearMotorVibrator;
        if (obj instanceof LinearmotorVibrator) {
            return (LinearmotorVibrator) obj;
        }
        return null;
    }

    private final boolean performAdaptiveFeedback(int realProgress) {
        LinearmotorVibrator orCreateLinearMotorVibrator;
        int i3;
        int i4;
        int i5;
        Context context = this.mContext;
        if (context == null || (orCreateLinearMotorVibrator = getOrCreateLinearMotorVibrator(context)) == null || (i5 = (i3 = this.mMax) - (i4 = this.mMin)) <= 0) {
            return false;
        }
        int i6 = realProgress - i4;
        try {
            if (realProgress == i3 || realProgress == i4) {
                VibrateUtils.setLinearMotorVibratorStrength(orCreateLinearMotorVibrator, 154, i6, i5, 800, 1200);
                return true;
            }
            boolean z2 = this.useSectionStyleFeedback;
            if (!z2 && !this.mIsDragging) {
                return true;
            }
            if (z2) {
                VibrateUtils.setLinearMotorVibratorStrength(orCreateLinearMotorVibrator, 0, i6, i5, 200, 2000);
                return true;
            }
            if (this.mVibratorExecutor == null) {
                this.mVibratorExecutor = Executors.newSingleThreadExecutor();
            }
            ExecutorService executorService = this.mVibratorExecutor;
            if (executorService != null) {
                executorService.execute(new b(this, orCreateLinearMotorVibrator, i6, i5));
            }
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    private static final void performAdaptiveFeedback$lambda$0(VibrationFeedbackHandler this$0, LinearmotorVibrator linearMotorVibrator, int i3, int i4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(linearMotorVibrator, "$linearMotorVibrator");
        if (this$0.mIsDragging) {
            VibrateUtils.setLinearMotorVibratorStrength(linearMotorVibrator, 152, i3, i4, 200, 2000);
        }
    }

    private final void performNormalFeedback(int realProgress) {
        View view = this.mView;
        if (view == null) {
            return;
        }
        try {
            if (realProgress != this.mMax && realProgress != this.mMin) {
                if (!this.mIsDragging && !this.useSectionStyleFeedback) {
                    return;
                }
                if (this.useSectionStyleFeedback) {
                    if (view.performHapticFeedback(308, 0)) {
                        return;
                    }
                    view.performHapticFeedback(302, 0);
                    return;
                } else {
                    if (this.mVibratorExecutor == null) {
                        this.mVibratorExecutor = Executors.newSingleThreadExecutor();
                    }
                    ExecutorService executorService = this.mVibratorExecutor;
                    if (executorService != null) {
                        executorService.execute(new a(this, view));
                        return;
                    }
                    return;
                }
            }
            view.performHapticFeedback(306, 0);
        } catch (Exception unused) {
        }
    }

    private static final void performNormalFeedback$lambda$1(VibrationFeedbackHandler this$0, View view) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(view, "$view");
        if (!this$0.mIsDragging || this$0.mView == null) {
            return;
        }
        view.performHapticFeedback(305, 0);
    }

    public final void clearPendingTasks() {
        ExecutorService executorService = this.mVibratorExecutor;
        if (executorService instanceof ThreadPoolExecutor) {
            ((ThreadPoolExecutor) executorService).getQueue().clear();
        }
    }

    public final boolean getUseSectionStyleFeedback() {
        return this.useSectionStyleFeedback;
    }

    /* JADX INFO: renamed from: isEnableAdaptiveVibrator, reason: from getter */
    public final boolean getIsEnableAdaptiveVibrator() {
        return this.isEnableAdaptiveVibrator;
    }

    /* JADX INFO: renamed from: isEnableVibrator, reason: from getter */
    public final boolean getIsEnableVibrator() {
        return this.isEnableVibrator;
    }

    public final void performFeedback(int realProgress) {
        if (this.isEnableVibrator && this.mLastRealProgress != realProgress) {
            this.mLastRealProgress = realProgress;
            if (this.mHasMotorVibrator && this.isEnableAdaptiveVibrator && performAdaptiveFeedback(realProgress)) {
                return;
            }
            performNormalFeedback(realProgress);
        }
    }

    public final void release() {
        ExecutorService executorService = this.mVibratorExecutor;
        if (executorService != null) {
            executorService.shutdown();
            this.mVibratorExecutor = null;
        }
    }

    public final void resetLastProgress() {
        this.mLastRealProgress = -1;
    }

    public final void setDragging(boolean dragging) {
        this.mIsDragging = dragging;
    }

    public final void setEnableAdaptiveVibrator(boolean z2) {
        this.isEnableAdaptiveVibrator = z2;
    }

    public final void setEnableVibrator(boolean z2) {
        this.isEnableVibrator = z2;
    }

    public final void setMinMax(int min, int max) {
        this.mMin = min;
        this.mMax = max;
    }

    public final void setUseSectionStyleFeedback(boolean z2) {
        this.useSectionStyleFeedback = z2;
    }

    public final void setView(View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        this.mView = view;
    }
}