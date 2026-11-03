package com.coui.appcompat.tips;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.hardware.display.DisplayManager;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Display;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.collection.SieveCacheKt;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.core.app.NotificationCompat;
import com.coui.appcompat.animation.COUILinearInterpolator;
import com.coui.appcompat.log.COUILog;
import com.oplus.settings.cloud.SettingsHelper;
import com.support.tips.R;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000|\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\r\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0000\u0018\u0000 H2\u00020\u0001:\u0002HIB'\b\u0007\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\b\u0010*\u001a\u00020+H\u0002J\u0006\u0010,\u001a\u00020+J\b\u0010-\u001a\u00020.H\u0002J\b\u0010/\u001a\u00020\nH\u0014J\b\u00100\u001a\u00020\nH\u0014J\b\u00101\u001a\u00020+H\u0002J\b\u00102\u001a\u00020+H\u0002J\b\u00103\u001a\u00020+H\u0014J\u0010\u00104\u001a\u00020+2\u0006\u00105\u001a\u000206H\u0014J\u0012\u00107\u001a\u00020+2\b\u00108\u001a\u0004\u0018\u000109H\u0016J\u0010\u0010:\u001a\u00020+2\u0006\u0010;\u001a\u00020<H\u0016J\u0018\u0010=\u001a\u00020+2\u0006\u0010>\u001a\u00020\u00072\u0006\u0010?\u001a\u00020\u0007H\u0014J\b\u0010@\u001a\u00020+H\u0002J\u001c\u0010A\u001a\u00020+2\b\u0010B\u001a\u0004\u0018\u00010\u001e2\b\u0010C\u001a\u0004\u0018\u00010DH\u0016J\u0010\u0010E\u001a\u00020+2\u0006\u0010F\u001a\u00020\u0007H\u0016J\u0006\u0010G\u001a\u00020+R\u001e\u0010\u000b\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\n@BX\u0082\u000e¢\u0006\b\n\u0000\"\u0004\b\f\u0010\rR\u001e\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\t\u001a\u00020\u000e@BX\u0082\u000e¢\u0006\b\n\u0000\"\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R$\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\t\u001a\u00020\u000e@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0011R\u0014\u0010\u0016\u001a\u00020\n8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0018R\u000e\u0010\u0019\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u001eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010#\u001a\u0004\u0018\u00010$X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010&\u001a\b\u0018\u00010'R\u00020\u0000X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006J"}, d2 = {"Lcom/coui/appcompat/tips/COUIMarqueeTextView;", "Landroidx/appcompat/widget/AppCompatTextView;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "value", "", "fadingEdgeStrength", "setFadingEdgeStrength", "(F)V", "", "isActualMarqueeByMeasured", "setActualMarqueeByMeasured", "(Z)V", "isAllCharactersLtR", "isMarqueeEnable", "()Z", "setMarqueeEnable", "mContentHeight", "getMContentHeight", "()F", "mContinueScrollingEnable", "mCurrentScrollLocation", "mFinalDrawText", "Landroid/text/SpannableStringBuilder;", "mIndividuallyAssembledText", "", "mIndividuallyAssembledTextWidth", "mInitStringWidth", "mOriginText", "mScrollRepeatCount", "mScroller", "Landroid/animation/ValueAnimator;", "mScrollerSpeed", "mStartScrollRunnable", "Lcom/coui/appcompat/tips/COUIMarqueeTextView$StartScrollRunnable;", "mSuppressAccessibilityEvents", "mTextViewScrollDistance", "checkAllCharactersDirections", "", "continueRoll", "generateTextDistance", "", "getLeftFadingEdgeStrength", "getRightFadingEdgeStrength", "initSpeed", "initTextViewAttributes", "onDetachedFromWindow", "onDraw", "canvas", "Landroid/graphics/Canvas;", "onInitializeAccessibilityEvent", NotificationCompat.CATEGORY_EVENT, "Landroid/view/accessibility/AccessibilityEvent;", "onInitializeAccessibilityNodeInfo", SettingsHelper.KEY_INFO, "Landroid/view/accessibility/AccessibilityNodeInfo;", "onMeasure", "widthMeasureSpec", "heightMeasureSpec", "setContent", "setText", "text", "type", "Landroid/widget/TextView$BufferType;", "setTextColor", TypedValues.Custom.S_COLOR, "stopRoll", "Companion", "StartScrollRunnable", "coui-support-tips_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class COUIMarqueeTextView extends AppCompatTextView {
    private static final float DEFAULT_DISPLAY_REFRESH = 60.0f;
    private static final long DEFAULT_SCROLL_DELAY_DURATION = 1000;
    private static final String TAG = "MarqueeView";
    private float fadingEdgeStrength;
    private boolean isActualMarqueeByMeasured;
    private boolean isAllCharactersLtR;
    private boolean isMarqueeEnable;
    private boolean mContinueScrollingEnable;
    private float mCurrentScrollLocation;
    private SpannableStringBuilder mFinalDrawText;
    private CharSequence mIndividuallyAssembledText;
    private int mIndividuallyAssembledTextWidth;
    private int mInitStringWidth;
    private CharSequence mOriginText;
    private int mScrollRepeatCount;
    private ValueAnimator mScroller;
    private float mScrollerSpeed;
    private StartScrollRunnable mStartScrollRunnable;
    private boolean mSuppressAccessibilityEvents;
    private final int mTextViewScrollDistance;

    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016¨\u0006\u0005"}, d2 = {"Lcom/coui/appcompat/tips/COUIMarqueeTextView$StartScrollRunnable;", "Ljava/lang/Runnable;", "(Lcom/coui/appcompat/tips/COUIMarqueeTextView;)V", "run", "", "coui-support-tips_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public final class StartScrollRunnable implements Runnable {
        public StartScrollRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUIMarqueeTextView.this.continueRoll();
        }
    }

    public COUIMarqueeTextView(Context context) {
        this(context, null, 0, 6, null);
    }

    public static /* synthetic */ void a(COUIMarqueeTextView cOUIMarqueeTextView, ValueAnimator valueAnimator) {
        continueRoll$lambda$2$lambda$1(cOUIMarqueeTextView, valueAnimator);
    }

    private final void checkAllCharactersDirections() {
        CharSequence charSequence = this.mOriginText;
        StaticLayout staticLayoutBuild = StaticLayout.Builder.obtain(charSequence, 0, charSequence.length(), getPaint(), getWidth()).build();
        Intrinsics.checkNotNull(staticLayoutBuild);
        this.isAllCharactersLtR = true;
        int length = this.mOriginText.length();
        for (int i3 = 0; i3 < length; i3++) {
            if (staticLayoutBuild.isRtlCharAt(i3)) {
                this.isAllCharactersLtR = false;
                return;
            }
        }
    }

    private static final void continueRoll$lambda$2$lambda$1(COUIMarqueeTextView this$0, ValueAnimator it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.mCurrentScrollLocation -= this$0.mScrollerSpeed;
        this$0.invalidate();
    }

    private final String generateTextDistance() {
        int iCeil = (int) Math.ceil(this.mTextViewScrollDistance / getPaint().measureText(" "));
        String str = this.mTextViewScrollDistance != 0 ? "" : " ";
        if (iCeil >= 0) {
            int i3 = 0;
            while (true) {
                str = str + ' ';
                if (i3 == iCeil) {
                    break;
                }
                i3++;
            }
        }
        return str;
    }

    private final float getMContentHeight() {
        return Math.abs(getPaint().getFontMetrics().bottom - getPaint().getFontMetrics().top) / 2;
    }

    private final void initSpeed() {
        Display display = ((DisplayManager) getContext().getSystemService(DisplayManager.class)).getDisplay(0);
        getResources().getDisplayMetrics();
        this.mScrollerSpeed = getResources().getDimensionPixelOffset(R.dimen.coui_top_tips_scroll_speed) / display.getRefreshRate();
        this.mStartScrollRunnable = new StartScrollRunnable();
    }

    private final void initTextViewAttributes() {
        setHorizontalFadingEdgeEnabled(true);
        setFadingEdgeLength(getResources().getDimensionPixelSize(R.dimen.coui_top_tips_fading_edge_size));
        this.mCurrentScrollLocation = getResources().getDimensionPixelOffset(R.dimen.coui_top_tips_scroll_text_start_location);
        getPaint().setColor(getCurrentTextColor());
        setImportantForAccessibility(1);
    }

    private final void setActualMarqueeByMeasured(boolean z2) {
        setFadingEdgeStrength((z2 && this.isMarqueeEnable) ? 1.0f : 0.0f);
        this.isActualMarqueeByMeasured = z2;
    }

    private final void setContent() {
        CharSequence charSequenceConcat = TextUtils.concat(this.mOriginText, generateTextDistance());
        Intrinsics.checkNotNullExpressionValue(charSequenceConcat, "concat(...)");
        this.mIndividuallyAssembledText = charSequenceConcat;
        this.mScrollRepeatCount = 0;
        TextPaint paint = getPaint();
        CharSequence charSequence = this.mIndividuallyAssembledText;
        this.mIndividuallyAssembledTextWidth = (int) paint.measureText(charSequence, 0, charSequence.length());
        int iCeil = (int) Math.ceil(((double) (getMeasuredWidth() / this.mIndividuallyAssembledTextWidth)) + 1.0d);
        this.mFinalDrawText = new SpannableStringBuilder(this.mIndividuallyAssembledText);
        if (iCeil >= 0) {
            int i3 = 0;
            while (true) {
                this.mFinalDrawText.append(this.mIndividuallyAssembledText);
                if (i3 == iCeil) {
                    break;
                } else {
                    i3++;
                }
            }
        }
        TextPaint paint2 = getPaint();
        SpannableStringBuilder spannableStringBuilder = this.mFinalDrawText;
        this.mInitStringWidth = (int) paint2.measureText(spannableStringBuilder, 0, spannableStringBuilder.length());
        checkAllCharactersDirections();
        super.setText(this.mFinalDrawText, TextView.BufferType.NORMAL);
    }

    private final void setFadingEdgeStrength(float f3) {
        this.fadingEdgeStrength = Math.signum(f3);
    }

    public final void continueRoll() {
        setMarqueeEnable(true);
        if (getPaint().measureText(getText().toString()) <= getMeasuredWidth() || this.mContinueScrollingEnable) {
            return;
        }
        ValueAnimator valueAnimator = this.mScroller;
        if (valueAnimator != null) {
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            this.mScroller = null;
        }
        this.mContinueScrollingEnable = true;
        ValueAnimator valueAnimatorOfInt = ValueAnimator.ofInt(Integer.MAX_VALUE);
        this.mScroller = valueAnimatorOfInt;
        if (valueAnimatorOfInt != null) {
            valueAnimatorOfInt.setDuration(SieveCacheKt.NodeLinkMask);
            valueAnimatorOfInt.setInterpolator(new COUILinearInterpolator());
            valueAnimatorOfInt.setRepeatCount(-1);
            valueAnimatorOfInt.setRepeatMode(1);
            valueAnimatorOfInt.addUpdateListener(new a(this));
            valueAnimatorOfInt.start();
        }
    }

    @Override // android.widget.TextView, android.view.View
    public float getLeftFadingEdgeStrength() {
        return this.fadingEdgeStrength;
    }

    @Override // android.widget.TextView, android.view.View
    public float getRightFadingEdgeStrength() {
        return this.fadingEdgeStrength;
    }

    /* JADX INFO: renamed from: isMarqueeEnable, reason: from getter */
    public final boolean getIsMarqueeEnable() {
        return this.isMarqueeEnable;
    }

    @Override // androidx.appcompat.widget.AppCompatTextView, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.isMarqueeEnable) {
            stopRoll();
            removeCallbacks(this.mStartScrollRunnable);
        }
    }

    @Override // android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        if (!this.isMarqueeEnable || !this.isActualMarqueeByMeasured) {
            COUILog.d(TAG, "onDraw: isMarqueeEnable=" + this.isMarqueeEnable + ", isActualMarqueeByMeasured=" + this.isActualMarqueeByMeasured);
            super.onDraw(canvas);
            return;
        }
        float f3 = this.mCurrentScrollLocation;
        if (f3 < 0.0f) {
            int iAbs = (int) Math.abs(f3 / this.mIndividuallyAssembledTextWidth);
            int i3 = this.mScrollRepeatCount;
            if (iAbs >= i3) {
                this.mScrollRepeatCount = i3 + 1;
                if (this.mCurrentScrollLocation <= (-this.mInitStringWidth)) {
                    this.mFinalDrawText.delete(0, this.mIndividuallyAssembledText.length());
                    this.mCurrentScrollLocation += this.mIndividuallyAssembledTextWidth;
                    this.mScrollRepeatCount--;
                }
                this.mFinalDrawText.append(this.mIndividuallyAssembledText);
                this.mSuppressAccessibilityEvents = true;
                super.setText(this.mFinalDrawText, TextView.BufferType.NORMAL);
            }
        }
        if (getLayout() == null) {
            super.onDraw(canvas);
        }
        canvas.save();
        float f4 = this.mCurrentScrollLocation;
        if (!this.isAllCharactersLtR) {
            f4 = -f4;
        }
        canvas.translate(f4, 0.0f);
        getLayout().draw(canvas);
        canvas.restore();
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        List<CharSequence> text;
        if (!this.mSuppressAccessibilityEvents) {
            super.onInitializeAccessibilityEvent(event);
            return;
        }
        if (event != null && (text = event.getText()) != null) {
            text.clear();
        }
        if (event != null) {
            event.setContentDescription(null);
        }
        this.mSuppressAccessibilityEvents = false;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        Intrinsics.checkNotNullParameter(info, "info");
        super.onInitializeAccessibilityNodeInfo(info);
        info.setText(this.mOriginText);
        info.setContentDescription(this.mOriginText);
    }

    @Override // androidx.appcompat.widget.AppCompatTextView, android.widget.TextView, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getPaint().measureText(getText().toString()) <= getMeasuredWidth()) {
            setActualMarqueeByMeasured(false);
            return;
        }
        setActualMarqueeByMeasured(true);
        if (this.isMarqueeEnable) {
            setContent();
        }
    }

    public final void setMarqueeEnable(boolean z2) {
        float f3;
        if (z2) {
            setSingleLine(true);
            setMaxLines(1);
            f3 = 1.0f;
        } else {
            setSingleLine(false);
            setMaxLines(Integer.MAX_VALUE);
            f3 = 0.0f;
        }
        setFadingEdgeStrength(f3);
        this.isMarqueeEnable = z2;
    }

    @Override // android.widget.TextView
    public void setText(CharSequence text, TextView.BufferType type) {
        this.mOriginText = text == null ? "" : text;
        super.setText(text, type);
    }

    @Override // android.widget.TextView
    public void setTextColor(int color) {
        super.setTextColor(color);
        getPaint().setColor(getCurrentTextColor());
    }

    public final void stopRoll() {
        this.mContinueScrollingEnable = false;
        this.mCurrentScrollLocation = getResources().getDimensionPixelOffset(R.dimen.coui_top_tips_scroll_text_start_location);
        ValueAnimator valueAnimator = this.mScroller;
        if (valueAnimator != null && valueAnimator != null) {
            valueAnimator.cancel();
        }
        this.mScroller = null;
        this.mScrollRepeatCount = 0;
        this.mFinalDrawText.clear();
        if (this.mOriginText.length() > 0) {
            super.setText(this.mOriginText, TextView.BufferType.NORMAL);
        }
    }

    public COUIMarqueeTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, null);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUIMarqueeTextView(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        Intrinsics.checkNotNull(context);
        this.mOriginText = "";
        this.mFinalDrawText = new SpannableStringBuilder();
        this.mScrollerSpeed = getResources().getDimensionPixelOffset(R.dimen.coui_top_tips_scroll_speed);
        this.mCurrentScrollLocation = getResources().getDimensionPixelOffset(R.dimen.coui_top_tips_scroll_text_start_location);
        this.mIndividuallyAssembledText = "";
        this.mTextViewScrollDistance = getResources().getDimensionPixelOffset(R.dimen.coui_top_tips_scroll_text_interval);
        this.isAllCharactersLtR = true;
        initSpeed();
        initTextViewAttributes();
        if (this.isMarqueeEnable) {
            postDelayed(this.mStartScrollRunnable, 1000L);
        }
    }

    public /* synthetic */ COUIMarqueeTextView(Context context, AttributeSet attributeSet, int i3, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i4 & 2) != 0 ? null : attributeSet, (i4 & 4) != 0 ? 0 : i3);
    }
}