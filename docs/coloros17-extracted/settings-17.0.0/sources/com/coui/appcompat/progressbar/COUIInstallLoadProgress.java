package com.coui.appcompat.progressbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.widget.ProgressBar;
import androidx.appcompat.widget.ViewUtils;
import androidx.core.graphics.ColorUtils;
import com.coui.appcompat.animation.COUIMoveEaseInterpolator;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.roundRect.COUIShapePath;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.coui.appcompat.textutil.COUIChangeTextUtil;
import com.coui.appcompat.tintimageview.COUITintUtil;
import com.decouple.injector.adaptor.BaseConfig;
import com.oplus.graphics.OplusPathAdapter;
import com.support.appcompat.R;
import java.util.Locale;

/* JADX INFO: loaded from: classes7.dex */
public class COUIInstallLoadProgress extends COUILoadProgress {
    private static final String CIRCLE_BRIGHTNESS_HOLDER = "circleBrightnessHolder";
    private static final String CIRCLE_IN_ALPHA_HOLDER = "circleInAlphaHolder";
    private static final int CIRCLE_MAX_ALPHA = 255;
    private static final String CIRCLE_OUT_ALPHA_HOLDER = "circleOutAlphaHolder";
    private static final String CIRCLE_RADIUS_HOLDER = "circleRadiusHolder";
    private static final float DEFAULT_BRIGHTNESS_MAX_VALUE = 0.8f;
    private static final float DEFAULT_MIN_PRESS_FEEDBACK = 0.005f;
    private static final float DEFAULT_NARROW_FINAL_VALUE = 0.92f;
    private static final float DEFAULT_SCALE_PARAMETER = 0.05f;
    private static final float DEFAULT_SCALE_PERCENT = 0.4f;
    private static final String HOLDER_BRIGHTNESS = "brightnessHolder";
    private static final String HOLDER_NARROW_FONT = "narrowHolderFont";
    private static final String HOLDER_NARROW_X = "narrowHolderX";
    private static final String HOLDER_NARROW_Y = "narrowHolderY";
    public static final int LOAD_STYLE_BIG_ROUND = 1;
    public static final int LOAD_STYLE_CIRCLE = 2;
    public static final int LOAD_STYLE_DEFAULT = 0;
    private static final int MAX_COLOR = 255;
    private static final int NORMAL_ANIMATOR_DURATION = 340;
    private static final float ONE_POINT_FIVE = 1.5f;
    private static final double POINT_FIVE = 0.5d;
    private static final int PRESS_ANIMATOR_DURATION = 200;
    private static final int[] mAttrArray = {R.attr.couiColorPrimary, R.attr.couiColorSecondary};
    private final boolean DEBUG;
    private final String TAG;
    private String mApostrophe;
    private int mBtnTextColor;
    private boolean mBtnTextColorChanged;
    private ColorStateList mBtnTextColorStateList;
    private Bitmap mCircleLoadBitmap;
    private Paint mCirclePaint;
    private Bitmap mCirclePauseBitmap;
    private Bitmap mCircleReloadBitmap;
    private float[] mColorHsl;
    private int mColorPrimary;
    private int mColorSecondary;
    private int mColorWhite;
    private Context mContext;
    private float mCurrentBrightness;
    private float mCurrentCircleRadius;
    private int mCurrentInBitmapAlpha;
    private int mCurrentOutBitmapAlpha;
    private int mCurrentRoundBorderRadius;
    private int mDefaultCircleRadius;
    private ColorStateList mDefaultTextColor;
    private int mDefaultTextSize;
    private int mDefaultWidth;
    private int mDisabledColor;
    private String mDownloadingContentDecrpition;
    private int mExpandOffsetX;
    private int mExpandOffsetY;
    private Paint.FontMetricsInt mFmi;
    private boolean mHasBrightness;
    private Paint mInBitmapPaint;
    private Drawable mInstallGiftBg;
    private boolean mIsChangeTextColor;
    private boolean mIsNeedVibrate;
    private int mLoadStyle;
    private Locale mLocale;
    private float mMaxBrightness;
    private float mNarrowOffsetFont;
    private Interpolator mNormalAnimationInterpolator;
    private ValueAnimator mNormalAnimator;
    private OplusPathAdapter mOplusPath;
    private Paint mOutBitmapPaint;
    private Interpolator mPressAnimationInterpolator;
    private ValueAnimator mPressedAnimator;
    private float mRadiusOffset;
    private Paint mRoundRectPaint;
    private Path mRoundRectPath;
    private int mSpace;
    private int mStyle;
    private int mSurpassProgressColor;
    private int mTextColor;
    private int mTextPadding;
    private TextPaint mTextPaint;
    private String mTextView;
    private int mThemeColor;
    private ColorStateList mThemeColorStateList;
    private int mThemeSecondaryColor;
    private ColorStateList mThemeSecondaryColorStateList;
    private int mTouchModeCircleRadius;
    private int mTouchModeHeight;
    private int mTouchModeWidth;
    private int mUserTextColor;
    private int mUserTextSize;

    /* JADX INFO: renamed from: com.coui.appcompat.progressbar.COUIInstallLoadProgress$1, reason: invalid class name */
    public class AnonymousClass1 extends ViewOutlineProvider {
        public AnonymousClass1() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setPath(COUIInstallLoadProgress.access$000(COUIInstallLoadProgress.this));
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.progressbar.COUIInstallLoadProgress$2, reason: invalid class name */
    public class AnonymousClass2 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass2() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIInstallLoadProgress.access$102(COUIInstallLoadProgress.this, ((Float) valueAnimator.getAnimatedValue(COUIInstallLoadProgress.HOLDER_BRIGHTNESS)).floatValue());
            float fFloatValue = ((Float) valueAnimator.getAnimatedValue(COUIInstallLoadProgress.HOLDER_NARROW_X)).floatValue();
            float fFloatValue2 = ((Float) valueAnimator.getAnimatedValue(COUIInstallLoadProgress.HOLDER_NARROW_Y)).floatValue();
            float fFloatValue3 = ((Float) valueAnimator.getAnimatedValue(COUIInstallLoadProgress.HOLDER_NARROW_FONT)).floatValue();
            if (fFloatValue < COUIInstallLoadProgress.this.getMeasuredWidth() * COUIInstallLoadProgress.DEFAULT_MIN_PRESS_FEEDBACK && fFloatValue2 < COUIInstallLoadProgress.this.getMeasuredHeight() * COUIInstallLoadProgress.DEFAULT_MIN_PRESS_FEEDBACK) {
                fFloatValue = COUIInstallLoadProgress.this.getMeasuredWidth() * COUIInstallLoadProgress.DEFAULT_MIN_PRESS_FEEDBACK;
                fFloatValue2 = COUIInstallLoadProgress.this.getMeasuredHeight() * COUIInstallLoadProgress.DEFAULT_MIN_PRESS_FEEDBACK;
            }
            COUIInstallLoadProgress.access$202(COUIInstallLoadProgress.this, (int) (((double) fFloatValue) + COUIInstallLoadProgress.POINT_FIVE));
            COUIInstallLoadProgress.access$302(COUIInstallLoadProgress.this, (int) (((double) fFloatValue2) + COUIInstallLoadProgress.POINT_FIVE));
            COUIInstallLoadProgress.access$402(COUIInstallLoadProgress.this, fFloatValue3);
            COUIInstallLoadProgress.access$500(COUIInstallLoadProgress.this);
            COUIInstallLoadProgress.this.invalidate();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.progressbar.COUIInstallLoadProgress$3, reason: invalid class name */
    public class AnonymousClass3 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass3() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIInstallLoadProgress.access$602(COUIInstallLoadProgress.this, ((Float) valueAnimator.getAnimatedValue(COUIInstallLoadProgress.CIRCLE_RADIUS_HOLDER)).floatValue());
            COUIInstallLoadProgress.access$102(COUIInstallLoadProgress.this, ((Float) valueAnimator.getAnimatedValue(COUIInstallLoadProgress.CIRCLE_BRIGHTNESS_HOLDER)).floatValue());
            COUIInstallLoadProgress.this.invalidate();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.progressbar.COUIInstallLoadProgress$4, reason: invalid class name */
    public class AnonymousClass4 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass4() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIInstallLoadProgress.access$102(COUIInstallLoadProgress.this, ((Float) valueAnimator.getAnimatedValue(COUIInstallLoadProgress.HOLDER_BRIGHTNESS)).floatValue());
            float fFloatValue = ((Float) valueAnimator.getAnimatedValue(COUIInstallLoadProgress.HOLDER_NARROW_X)).floatValue();
            float fFloatValue2 = ((Float) valueAnimator.getAnimatedValue(COUIInstallLoadProgress.HOLDER_NARROW_Y)).floatValue();
            COUIInstallLoadProgress.access$402(COUIInstallLoadProgress.this, ((Float) valueAnimator.getAnimatedValue(COUIInstallLoadProgress.HOLDER_NARROW_FONT)).floatValue());
            COUIInstallLoadProgress.access$202(COUIInstallLoadProgress.this, (int) (((double) fFloatValue) + COUIInstallLoadProgress.POINT_FIVE));
            COUIInstallLoadProgress.access$302(COUIInstallLoadProgress.this, (int) (((double) fFloatValue2) + COUIInstallLoadProgress.POINT_FIVE));
            COUIInstallLoadProgress.access$500(COUIInstallLoadProgress.this);
            COUIInstallLoadProgress.this.invalidate();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.progressbar.COUIInstallLoadProgress$5, reason: invalid class name */
    public class AnonymousClass5 extends AnimatorListenerAdapter {
        final /* synthetic */ boolean val$isActionUp;

        public AnonymousClass5(boolean z2) {
            this.val$isActionUp = z2;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (this.val$isActionUp) {
                COUIInstallLoadProgress.access$701(COUIInstallLoadProgress.this);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.progressbar.COUIInstallLoadProgress$6, reason: invalid class name */
    public class AnonymousClass6 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass6() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIInstallLoadProgress.access$602(COUIInstallLoadProgress.this, ((Float) valueAnimator.getAnimatedValue(COUIInstallLoadProgress.CIRCLE_RADIUS_HOLDER)).floatValue());
            COUIInstallLoadProgress.access$102(COUIInstallLoadProgress.this, ((Float) valueAnimator.getAnimatedValue(COUIInstallLoadProgress.CIRCLE_BRIGHTNESS_HOLDER)).floatValue());
            COUIInstallLoadProgress.access$802(COUIInstallLoadProgress.this, ((Integer) valueAnimator.getAnimatedValue(COUIInstallLoadProgress.CIRCLE_IN_ALPHA_HOLDER)).intValue());
            COUIInstallLoadProgress.access$902(COUIInstallLoadProgress.this, ((Integer) valueAnimator.getAnimatedValue(COUIInstallLoadProgress.CIRCLE_OUT_ALPHA_HOLDER)).intValue());
            COUIInstallLoadProgress.this.invalidate();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.progressbar.COUIInstallLoadProgress$7, reason: invalid class name */
    public class AnonymousClass7 extends AnimatorListenerAdapter {
        public AnonymousClass7() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUIInstallLoadProgress.access$1001(COUIInstallLoadProgress.this);
        }
    }

    public COUIInstallLoadProgress(Context context) {
        this(context, null);
    }

    public static /* synthetic */ Path access$000(COUIInstallLoadProgress cOUIInstallLoadProgress) {
        return cOUIInstallLoadProgress.mRoundRectPath;
    }

    public static /* synthetic */ boolean access$1001(COUIInstallLoadProgress cOUIInstallLoadProgress) {
        return super.performClick();
    }

    public static /* synthetic */ float access$102(COUIInstallLoadProgress cOUIInstallLoadProgress, float f3) {
        cOUIInstallLoadProgress.mCurrentBrightness = f3;
        return f3;
    }

    public static /* synthetic */ int access$202(COUIInstallLoadProgress cOUIInstallLoadProgress, int i3) {
        cOUIInstallLoadProgress.mExpandOffsetX = i3;
        return i3;
    }

    public static /* synthetic */ int access$302(COUIInstallLoadProgress cOUIInstallLoadProgress, int i3) {
        cOUIInstallLoadProgress.mExpandOffsetY = i3;
        return i3;
    }

    public static /* synthetic */ float access$402(COUIInstallLoadProgress cOUIInstallLoadProgress, float f3) {
        cOUIInstallLoadProgress.mNarrowOffsetFont = f3;
        return f3;
    }

    public static /* synthetic */ void access$500(COUIInstallLoadProgress cOUIInstallLoadProgress) {
        cOUIInstallLoadProgress.updateRoundRectPath();
    }

    public static /* synthetic */ float access$602(COUIInstallLoadProgress cOUIInstallLoadProgress, float f3) {
        cOUIInstallLoadProgress.mCurrentCircleRadius = f3;
        return f3;
    }

    public static /* synthetic */ boolean access$701(COUIInstallLoadProgress cOUIInstallLoadProgress) {
        return super.performClick();
    }

    public static /* synthetic */ int access$802(COUIInstallLoadProgress cOUIInstallLoadProgress, int i3) {
        cOUIInstallLoadProgress.mCurrentInBitmapAlpha = i3;
        return i3;
    }

    public static /* synthetic */ int access$902(COUIInstallLoadProgress cOUIInstallLoadProgress, int i3) {
        cOUIInstallLoadProgress.mCurrentOutBitmapAlpha = i3;
        return i3;
    }

    private void addApostrophe() {
        String displayText = getDisplayText(this.mTextView, this.mDefaultWidth);
        if (displayText.length() <= 0 || displayText.length() >= this.mTextView.length()) {
            return;
        }
        this.mTextView = isEnglish(getDisplayText(displayText, (this.mDefaultWidth - (this.mTextPadding * 2)) - ((int) this.mTextPaint.measureText(this.mApostrophe)))) + this.mApostrophe;
    }

    private void cancelAnim(ValueAnimator valueAnimator) {
        if (valueAnimator == null || !valueAnimator.isRunning()) {
            return;
        }
        valueAnimator.cancel();
    }

    private int dip2px(Context context, float f3) {
        return (int) (((double) (f3 * context.getResources().getDisplayMetrics().density)) + POINT_FIVE);
    }

    private Bitmap getBitmapFromVectorDrawable(int i3) {
        Drawable drawable = getContext().getDrawable(i3);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmapCreateBitmap;
    }

    private int getCurrentColor(int i3) {
        if (!isEnabled()) {
            return this.mDisabledColor;
        }
        ColorUtils.colorToHSL(i3, this.mColorHsl);
        float[] fArr = this.mColorHsl;
        fArr[2] = fArr[2] * this.mCurrentBrightness;
        int iHSLToColor = ColorUtils.HSLToColor(fArr);
        int iRed = Color.red(iHSLToColor);
        int iGreen = Color.green(iHSLToColor);
        int iBlue = Color.blue(iHSLToColor);
        int iAlpha = Color.alpha(i3);
        if (iRed > 255) {
            iRed = 255;
        }
        if (iGreen > 255) {
            iGreen = 255;
        }
        if (iBlue > 255) {
            iBlue = 255;
        }
        return Color.argb(iAlpha, iRed, iGreen, iBlue);
    }

    private int getDefaultSize(int i3, float f3, boolean z2) {
        return i3 - (z2 ? dip2px(getContext(), f3) : dip2px(getContext(), f3) * 2);
    }

    private String getDisplayText(String str, int i3) {
        int iBreakText = this.mTextPaint.breakText(str, true, i3, null);
        return (iBreakText == 0 || iBreakText == str.length()) ? str : str.substring(0, iBreakText - 1);
    }

    private void init() {
        if (this.mLoadStyle == 2) {
            return;
        }
        TextPaint textPaint = new TextPaint(1);
        this.mTextPaint = textPaint;
        textPaint.setAntiAlias(true);
        int i3 = this.mUserTextSize;
        if (i3 == 0) {
            i3 = this.mDefaultTextSize;
        }
        int i4 = this.mUserTextColor;
        this.mTextColor = i4;
        if (i4 == -1) {
            this.mTextColor = this.mDefaultTextColor.getColorForState(getDrawableState(), COUIContextUtil.getAttrColor(getContext(), R.attr.couiDefaultTextColor, 0));
        }
        this.mTextPaint.setTextSize(i3);
        COUIChangeTextUtil.adaptBoldAndMediumFont((Paint) this.mTextPaint, true);
        this.mFmi = this.mTextPaint.getFontMetricsInt();
        addApostrophe();
    }

    private static boolean isChinese(String str) {
        int i3 = 0;
        for (int i4 = 0; i4 < str.length(); i4++) {
            if (Character.toString(str.charAt(i4)).matches("^[一-龥]{1}$")) {
                i3++;
            }
        }
        return i3 > 0;
    }

    private String isEnglish(String str) {
        int iLastIndexOf;
        return (isChinese(str) || (iLastIndexOf = str.lastIndexOf(32)) <= 0) ? str : str.substring(0, iLastIndexOf);
    }

    private boolean isOs16() {
        return RoundCornerUtil.getSmoothStyleType() == 1;
    }

    private boolean isZhLanguage(Locale locale) {
        return "zh".equalsIgnoreCase(locale.getLanguage());
    }

    private void onDrawButtonDrawable(Canvas canvas, float f3, float f4, float f5, float f6) {
        Drawable drawable = this.mButtonDrawable;
        if (drawable != null) {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = this.mButtonDrawable.getIntrinsicHeight();
            int i3 = ((int) (f5 - intrinsicWidth)) / 2;
            int i4 = ((int) (f6 - intrinsicHeight)) / 2;
            int i5 = intrinsicWidth + i3;
            int i6 = intrinsicHeight + i4;
            this.mButtonDrawable.setBounds(i3, i4, i5, i6);
            Drawable drawable2 = this.mButtonDrawable;
            int i7 = this.mTextColor;
            PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN;
            drawable2.setColorFilter(i7, mode);
            this.mButtonDrawable.draw(canvas);
            if (this.mIsChangeTextColor) {
                canvas.save();
                this.mButtonDrawableReverseColor.setBounds(i3, i4, i5, i6);
                this.mButtonDrawableReverseColor.setColorFilter(this.mSurpassProgressColor, mode);
                if (ViewUtils.isLayoutRtl(this)) {
                    canvas.clipRect(f5 - this.mSpace, f4, f5, f6);
                } else {
                    canvas.clipRect(f3, f4, this.mSpace, f6);
                }
                this.mButtonDrawableReverseColor.draw(canvas);
                canvas.restore();
                this.mIsChangeTextColor = false;
            }
        }
    }

    private void onDrawCircle(Canvas canvas, float f3, float f4, boolean z2, Bitmap bitmap, Bitmap bitmap2) {
        if (bitmap == null || bitmap.isRecycled() || bitmap2 == null || bitmap2.isRecycled()) {
            return;
        }
        this.mCirclePaint.setColor(this.mThemeColorStateList == null ? getCurrentColor(this.mColorPrimary) : this.mThemeColor);
        if (!z2) {
            this.mCirclePaint.setColor(this.mThemeSecondaryColorStateList == null ? getCurrentColor(this.mColorSecondary) : this.mThemeSecondaryColor);
        }
        int width = (this.mTouchModeWidth - bitmap.getWidth()) / 2;
        int height = (this.mTouchModeHeight - bitmap.getHeight()) / 2;
        this.mInBitmapPaint.setAlpha(this.mCurrentInBitmapAlpha);
        this.mOutBitmapPaint.setAlpha(this.mCurrentOutBitmapAlpha);
        canvas.save();
        canvas.clipPath(this.mRoundRectPath);
        canvas.drawColor(this.mCirclePaint.getColor());
        float f5 = width;
        float f6 = height;
        canvas.drawBitmap(bitmap, f5, f6, this.mInBitmapPaint);
        canvas.drawBitmap(bitmap2, f5, f6, this.mOutBitmapPaint);
        canvas.restore();
        canvas.save();
    }

    private void onDrawRoundRect(Canvas canvas, float f3, float f4, float f5, float f6, boolean z2, float f7, float f8) {
        canvas.translate(f7, f8);
        this.mRoundRectPaint.setColor(this.mThemeColorStateList == null ? getCurrentColor(this.mColorPrimary) : this.mThemeColor);
        if (!z2) {
            this.mRoundRectPaint.setColor(this.mThemeSecondaryColorStateList == null ? getCurrentColor(this.mColorSecondary) : this.mThemeSecondaryColor);
        }
        canvas.drawPath(this.mRoundRectPath, this.mRoundRectPaint);
        canvas.translate(-f7, -f8);
    }

    private void onDrawText(Canvas canvas, float f3, float f4, float f5, float f6) {
        if (this.mTextView != null) {
            this.mTextPaint.setTextSize(this.mDefaultTextSize * this.mNarrowOffsetFont);
            float fMeasureText = this.mTextPaint.measureText(this.mTextView);
            int i3 = this.mTextPadding;
            float f7 = i3 + (((f5 - fMeasureText) - (i3 * 2)) / 2.0f);
            Paint.FontMetricsInt fontMetricsInt = this.mFmi;
            float f8 = ((f6 - fontMetricsInt.descent) - fontMetricsInt.ascent) / 2.0f;
            canvas.drawText(this.mTextView, f7, f8, this.mTextPaint);
            if (this.mIsChangeTextColor) {
                this.mTextPaint.setColor(this.mSurpassProgressColor);
                canvas.save();
                if (ViewUtils.isLayoutRtl(this)) {
                    canvas.clipRect(f5 - this.mSpace, f4, f5, f6);
                } else {
                    canvas.clipRect(f3, f4, this.mSpace, f6);
                }
                canvas.drawText(this.mTextView, f7, f8, this.mTextPaint);
                canvas.restore();
                this.mIsChangeTextColor = false;
            }
        }
    }

    private void performHapticFeedback() {
        if (this.mIsNeedVibrate) {
            performHapticFeedback(302);
        }
    }

    private void performTouchEndAnim(boolean z2) {
        performHapticFeedback();
        if (this.mHasBrightness) {
            cancelAnim(this.mPressedAnimator);
            int i3 = this.mLoadStyle;
            if (i3 == 0 || i3 == 1) {
                ValueAnimator valueAnimatorOfPropertyValuesHolder = ValueAnimator.ofPropertyValuesHolder(PropertyValuesHolder.ofFloat(HOLDER_BRIGHTNESS, this.mCurrentBrightness, 1.0f), PropertyValuesHolder.ofFloat(HOLDER_NARROW_X, this.mExpandOffsetX, 0.0f), PropertyValuesHolder.ofFloat(HOLDER_NARROW_Y, this.mExpandOffsetY, 0.0f), PropertyValuesHolder.ofFloat(HOLDER_NARROW_FONT, this.mNarrowOffsetFont, 1.0f));
                this.mNormalAnimator = valueAnimatorOfPropertyValuesHolder;
                valueAnimatorOfPropertyValuesHolder.setInterpolator(this.mNormalAnimationInterpolator);
                this.mNormalAnimator.setDuration(340L);
                this.mNormalAnimator.addUpdateListener(new AnonymousClass4());
                this.mNormalAnimator.addListener(new AnonymousClass5(z2));
                this.mNormalAnimator.start();
            } else if (i3 == 2) {
                ValueAnimator valueAnimatorOfPropertyValuesHolder2 = ValueAnimator.ofPropertyValuesHolder(PropertyValuesHolder.ofFloat(CIRCLE_RADIUS_HOLDER, this.mCurrentCircleRadius, this.mDefaultCircleRadius), PropertyValuesHolder.ofFloat(CIRCLE_BRIGHTNESS_HOLDER, this.mCurrentBrightness, 1.0f), PropertyValuesHolder.ofInt(CIRCLE_IN_ALPHA_HOLDER, 0, 255), PropertyValuesHolder.ofInt(CIRCLE_OUT_ALPHA_HOLDER, 255, 0));
                this.mNormalAnimator = valueAnimatorOfPropertyValuesHolder2;
                valueAnimatorOfPropertyValuesHolder2.setInterpolator(this.mNormalAnimationInterpolator);
                this.mNormalAnimator.setDuration(340L);
                this.mNormalAnimator.addUpdateListener(new AnonymousClass6());
                this.mNormalAnimator.addListener(new AnonymousClass7());
                this.mNormalAnimator.start();
            }
            this.mHasBrightness = false;
        }
    }

    private void performTouchStartAnim() {
        if (this.mHasBrightness) {
            return;
        }
        cancelAnim(this.mNormalAnimator);
        int i3 = this.mLoadStyle;
        if (i3 == 0 || i3 == 1) {
            ValueAnimator valueAnimatorOfPropertyValuesHolder = ValueAnimator.ofPropertyValuesHolder(PropertyValuesHolder.ofFloat(HOLDER_BRIGHTNESS, 1.0f, this.mMaxBrightness), PropertyValuesHolder.ofFloat(HOLDER_NARROW_X, 0.0f, getMeasuredWidth() * DEFAULT_SCALE_PARAMETER), PropertyValuesHolder.ofFloat(HOLDER_NARROW_Y, 0.0f, getMeasuredHeight() * DEFAULT_SCALE_PARAMETER), PropertyValuesHolder.ofFloat(HOLDER_NARROW_FONT, 1.0f, 0.92f));
            this.mPressedAnimator = valueAnimatorOfPropertyValuesHolder;
            valueAnimatorOfPropertyValuesHolder.setInterpolator(this.mPressAnimationInterpolator);
            this.mPressedAnimator.setDuration(200L);
            this.mPressedAnimator.addUpdateListener(new AnonymousClass2());
            this.mPressedAnimator.start();
        } else if (i3 == 2) {
            ValueAnimator valueAnimatorOfPropertyValuesHolder2 = ValueAnimator.ofPropertyValuesHolder(PropertyValuesHolder.ofFloat(CIRCLE_RADIUS_HOLDER, this.mCurrentCircleRadius, this.mDefaultCircleRadius * 0.9f), PropertyValuesHolder.ofFloat(CIRCLE_BRIGHTNESS_HOLDER, this.mCurrentBrightness, this.mMaxBrightness));
            this.mPressedAnimator = valueAnimatorOfPropertyValuesHolder2;
            valueAnimatorOfPropertyValuesHolder2.setInterpolator(this.mPressAnimationInterpolator);
            this.mPressedAnimator.setDuration(200L);
            this.mPressedAnimator.addUpdateListener(new AnonymousClass3());
            this.mPressedAnimator.start();
        }
        this.mHasBrightness = true;
    }

    private void updateRoundRectPath() {
        if (this.mLoadStyle == 2) {
            float f3 = (float) ((((double) this.mTouchModeWidth) * 1.0d) / 2.0d);
            float f4 = (float) ((((double) this.mTouchModeHeight) * 1.0d) / 2.0d);
            float f5 = this.mCurrentCircleRadius;
            COUIShapePath.getRoundRectPath(this.mRoundRectPath, new RectF(f3 - f5, f4 - f5, f3 + f5, f4 + f5), this.mCurrentRoundBorderRadius);
            return;
        }
        float f6 = this.mExpandOffsetX;
        float f7 = this.mExpandOffsetY;
        float width = getWidth() - this.mExpandOffsetX;
        float height = getHeight() - this.mExpandOffsetY;
        RectF rectF = new RectF(f6, f7, width, height);
        if (!isOs16()) {
            COUIShapePath.getRoundRectPath(this.mRoundRectPath, rectF, ((height - f7) / 2.0f) - this.mRadiusOffset);
        } else {
            this.mRoundRectPath.reset();
            this.mOplusPath.addSmoothRoundRect(rectF, (rectF.height() * getScaleY()) / 2.0f, (rectF.height() * getScaleY()) / 2.0f, Path.Direction.CCW);
            invalidateOutline();
        }
    }

    @Override // com.coui.appcompat.progressbar.COUILoadProgress, androidx.appcompat.widget.AppCompatButton, android.widget.TextView, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
    }

    @Override // android.widget.Button, android.widget.TextView, android.view.View
    public CharSequence getAccessibilityClassName() {
        return ProgressBar.class.getName();
    }

    @Override // android.widget.TextView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mLoadStyle == 2) {
            Bitmap bitmap = this.mCircleLoadBitmap;
            if (bitmap == null || bitmap.isRecycled()) {
                Bitmap bitmapFromVectorDrawable = getBitmapFromVectorDrawable(com.support.progressbar.R.drawable.coui_install_load_progress_circle_load);
                this.mCircleLoadBitmap = bitmapFromVectorDrawable;
                this.mCircleLoadBitmap = COUITintUtil.tintBitmap(bitmapFromVectorDrawable, this.mThemeColorStateList == null ? this.mColorPrimary : this.mThemeColor);
            }
            Bitmap bitmap2 = this.mCircleReloadBitmap;
            if (bitmap2 == null || bitmap2.isRecycled()) {
                this.mCircleReloadBitmap = getBitmapFromVectorDrawable(com.support.progressbar.R.drawable.coui_install_load_progress_circle_reload);
            }
            Bitmap bitmap3 = this.mCirclePauseBitmap;
            if (bitmap3 == null || bitmap3.isRecycled()) {
                this.mCirclePauseBitmap = getBitmapFromVectorDrawable(com.support.progressbar.R.drawable.coui_install_load_progress_circle_pause);
            }
        }
    }

    @Override // android.widget.TextView, android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Locale locale = Locale.getDefault();
        if (this.mLoadStyle != 0 || this.mLocale.getLanguage().equalsIgnoreCase(locale.getLanguage())) {
            return;
        }
        this.mLocale = locale;
        int dimensionPixelSize = getResources().getDimensionPixelSize(com.support.progressbar.R.dimen.coui_install_download_progress_width_in_foreign_language);
        if (isZhLanguage(this.mLocale)) {
            this.mTouchModeWidth -= dimensionPixelSize;
            this.mDefaultWidth -= dimensionPixelSize;
        } else {
            this.mTouchModeWidth += dimensionPixelSize;
            this.mDefaultWidth += dimensionPixelSize;
        }
        invalidate();
    }

    @Override // com.coui.appcompat.progressbar.COUILoadProgress, android.view.View
    public void onDetachedFromWindow() {
        if (this.mLoadStyle == 2) {
            Bitmap bitmap = this.mCircleLoadBitmap;
            if (bitmap != null && !bitmap.isRecycled()) {
                this.mCircleLoadBitmap.recycle();
            }
            Bitmap bitmap2 = this.mCirclePauseBitmap;
            if (bitmap2 != null && !bitmap2.isRecycled()) {
                this.mCirclePauseBitmap.recycle();
            }
            Bitmap bitmap3 = this.mCircleReloadBitmap;
            if (bitmap3 != null && !bitmap3.isRecycled()) {
                this.mCircleReloadBitmap.recycle();
            }
        }
        super.onDetachedFromWindow();
    }

    @Override // com.coui.appcompat.progressbar.COUILoadProgress, android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        float f3;
        float f4;
        boolean z2;
        float f5;
        int i3;
        float f6;
        float f7;
        float f8;
        float f9;
        COUIInstallLoadProgress cOUIInstallLoadProgress = this;
        super.onDraw(canvas);
        float f10 = cOUIInstallLoadProgress.mExpandOffsetX;
        float f11 = cOUIInstallLoadProgress.mExpandOffsetY;
        float width = cOUIInstallLoadProgress.getWidth() - cOUIInstallLoadProgress.mExpandOffsetX;
        float height = cOUIInstallLoadProgress.getHeight() - cOUIInstallLoadProgress.mExpandOffsetY;
        int i4 = cOUIInstallLoadProgress.mState;
        double d3 = 2.0d;
        if (i4 == 3) {
            if (cOUIInstallLoadProgress.mLoadStyle == 2) {
                cOUIInstallLoadProgress.onDrawCircle(canvas, (float) ((((double) cOUIInstallLoadProgress.mTouchModeWidth) * 1.0d) / 2.0d), (float) ((((double) cOUIInstallLoadProgress.mTouchModeHeight) * 1.0d) / 2.0d), true, cOUIInstallLoadProgress.mCircleReloadBitmap, cOUIInstallLoadProgress.mCirclePauseBitmap);
                return;
            }
            onDrawRoundRect(canvas, f10, f11, width, height, true, 0.0f, 0.0f);
            this.mTextPaint.setColor(this.mBtnTextColorChanged ? this.mBtnTextColor : this.mColorWhite);
            this.mIsChangeTextColor = false;
            if (this.mButtonDrawable == null) {
                onDrawText(canvas, f10, f11, this.mTouchModeWidth, this.mTouchModeHeight);
                return;
            } else {
                onDrawButtonDrawable(canvas, f10, f11, this.mTouchModeWidth, this.mTouchModeHeight);
                return;
            }
        }
        float f12 = f10;
        float f13 = f11;
        if (i4 == 0) {
            int i5 = cOUIInstallLoadProgress.mLoadStyle;
            if (i5 == 2) {
                z2 = true;
                cOUIInstallLoadProgress.onDrawCircle(canvas, (float) ((((double) cOUIInstallLoadProgress.mTouchModeWidth) * 1.0d) / 2.0d), (float) ((((double) cOUIInstallLoadProgress.mTouchModeHeight) * 1.0d) / 2.0d), false, cOUIInstallLoadProgress.mCircleLoadBitmap, cOUIInstallLoadProgress.mCirclePauseBitmap);
                cOUIInstallLoadProgress = this;
                f3 = width;
                f4 = height;
            } else {
                z2 = true;
                if (i5 == 1) {
                    cOUIInstallLoadProgress = this;
                    f8 = f12;
                    f9 = f13;
                    f3 = width;
                    f4 = height;
                    cOUIInstallLoadProgress.onDrawRoundRect(canvas, f8, f9, f3, f4, true, 0.0f, 0.0f);
                } else {
                    f8 = f12;
                    f9 = f13;
                    f3 = width;
                    f4 = height;
                    cOUIInstallLoadProgress = this;
                    cOUIInstallLoadProgress.onDrawRoundRect(canvas, f8, f9, f3, f4, false, 0.0f, 0.0f);
                }
                f12 = f8;
                f13 = f9;
            }
            int i6 = cOUIInstallLoadProgress.mLoadStyle;
            if (i6 == z2) {
                cOUIInstallLoadProgress.mTextPaint.setColor(cOUIInstallLoadProgress.mBtnTextColorChanged ? cOUIInstallLoadProgress.mBtnTextColor : cOUIInstallLoadProgress.mColorWhite);
            } else if (i6 == 0) {
                cOUIInstallLoadProgress.mTextPaint.setColor(cOUIInstallLoadProgress.mBtnTextColorStateList == null ? cOUIInstallLoadProgress.mColorPrimary : cOUIInstallLoadProgress.mBtnTextColor);
            }
        } else {
            d3 = 2.0d;
            f3 = width;
            f4 = height;
            z2 = true;
        }
        int i7 = cOUIInstallLoadProgress.mState;
        if (i7 != z2 && i7 != 2) {
            f6 = f12;
            f7 = f13;
        } else if (cOUIInstallLoadProgress.mLoadStyle == 2) {
            if (i7 == z2) {
                cOUIInstallLoadProgress.onDrawCircle(canvas, (float) ((((double) cOUIInstallLoadProgress.mTouchModeWidth) * 1.0d) / d3), (float) ((((double) cOUIInstallLoadProgress.mTouchModeHeight) * 1.0d) / d3), true, cOUIInstallLoadProgress.mCirclePauseBitmap, cOUIInstallLoadProgress.mCircleReloadBitmap);
            } else if (i7 == 2) {
                cOUIInstallLoadProgress.onDrawCircle(canvas, (float) ((((double) cOUIInstallLoadProgress.mTouchModeWidth) * 1.0d) / d3), (float) ((((double) cOUIInstallLoadProgress.mTouchModeHeight) * 1.0d) / d3), true, cOUIInstallLoadProgress.mCircleReloadBitmap, cOUIInstallLoadProgress.mCirclePauseBitmap);
            }
            f6 = f12;
            f7 = f13;
        } else {
            if (cOUIInstallLoadProgress.mIsUpdateWithAnimation) {
                f5 = cOUIInstallLoadProgress.mVisualProgress;
                i3 = cOUIInstallLoadProgress.mMax;
            } else {
                f5 = cOUIInstallLoadProgress.mProgress;
                i3 = cOUIInstallLoadProgress.mMax;
            }
            float f14 = f5 / i3;
            int i8 = cOUIInstallLoadProgress.mTouchModeWidth;
            int i9 = cOUIInstallLoadProgress.mExpandOffsetX;
            cOUIInstallLoadProgress.mSpace = ((int) (f14 * (i8 - (i9 * 2)))) + i9;
            f6 = f12;
            f7 = f13;
            cOUIInstallLoadProgress.onDrawRoundRect(canvas, f6, f7, f3, f4, false, 0.0f, 0.0f);
            canvas.save();
            if (ViewUtils.isLayoutRtl(cOUIInstallLoadProgress)) {
                canvas.translate(0.0f, 0.0f);
                canvas.clipRect((f3 - cOUIInstallLoadProgress.mSpace) + 0.0f, f7, f3, cOUIInstallLoadProgress.mTouchModeHeight);
                canvas.translate(-0.0f, 0.0f);
            } else {
                canvas.clipRect(f6, f7, cOUIInstallLoadProgress.mSpace, cOUIInstallLoadProgress.mTouchModeHeight);
            }
            if (cOUIInstallLoadProgress.mLoadStyle != 2) {
                cOUIInstallLoadProgress.onDrawRoundRect(canvas, f6, f7, f3, f4, true, 0.0f, 0.0f);
                canvas.restore();
            }
            cOUIInstallLoadProgress.mIsChangeTextColor = z2;
            cOUIInstallLoadProgress.mTextPaint.setColor(cOUIInstallLoadProgress.mBtnTextColorStateList == null ? cOUIInstallLoadProgress.mColorPrimary : cOUIInstallLoadProgress.mBtnTextColor);
        }
        if (cOUIInstallLoadProgress.mLoadStyle != 2) {
            if (cOUIInstallLoadProgress.mButtonDrawable == null) {
                cOUIInstallLoadProgress.onDrawText(canvas, f6, f7, cOUIInstallLoadProgress.mTouchModeWidth, cOUIInstallLoadProgress.mTouchModeHeight);
            } else {
                cOUIInstallLoadProgress.onDrawButtonDrawable(canvas, f6, f7, cOUIInstallLoadProgress.mTouchModeWidth, cOUIInstallLoadProgress.mTouchModeHeight);
            }
        }
    }

    @Override // androidx.appcompat.widget.AppCompatButton, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setItemCount(this.mMax);
        accessibilityEvent.setCurrentItemIndex(this.mProgress);
    }

    @Override // androidx.appcompat.widget.AppCompatButton, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        String str;
        String str2;
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        int i3 = this.mState;
        if ((i3 == 0 || i3 == 3 || i3 == 2) && (str = this.mTextView) != null) {
            accessibilityNodeInfo.setContentDescription(str);
        } else {
            if (i3 != 1 || (str2 = this.mDownloadingContentDecrpition) == null) {
                return;
            }
            accessibilityNodeInfo.setContentDescription(str2);
        }
    }

    @Override // android.widget.TextView, android.view.View
    public void onMeasure(int i3, int i4) {
        setMeasuredDimension(this.mTouchModeWidth, this.mTouchModeHeight);
    }

    @Override // android.view.View
    public void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        updateRoundRectPath();
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action != 0) {
            boolean z2 = false;
            if (action == 1) {
                float x2 = motionEvent.getX();
                float y2 = motionEvent.getY();
                if (x2 >= 0.0f && x2 <= this.mTouchModeWidth && y2 >= 0.0f && y2 <= this.mTouchModeHeight) {
                    z2 = true;
                }
                performTouchEndAnim(z2);
            } else if (action == 3) {
                performTouchEndAnim(false);
            }
        } else {
            performHapticFeedback();
            performTouchStartAnim();
        }
        return true;
    }

    public void refresh() {
        TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(mAttrArray);
        this.mColorPrimary = typedArrayObtainStyledAttributes.getColor(0, 0);
        this.mColorSecondary = typedArrayObtainStyledAttributes.getColor(1, 0);
        typedArrayObtainStyledAttributes.recycle();
        String resourceTypeName = getResources().getResourceTypeName(this.mStyle);
        TypedArray typedArrayObtainStyledAttributes2 = null;
        if ("attr".equals(resourceTypeName)) {
            typedArrayObtainStyledAttributes2 = this.mContext.obtainStyledAttributes(null, com.support.progressbar.R.styleable.COUIInstallLoadProgress, this.mStyle, 0);
        } else if (BaseConfig.KEY_STYLE.equals(resourceTypeName)) {
            typedArrayObtainStyledAttributes2 = this.mContext.obtainStyledAttributes(null, com.support.progressbar.R.styleable.COUIInstallLoadProgress, 0, this.mStyle);
        }
        if (typedArrayObtainStyledAttributes2 != null) {
            this.mDisabledColor = typedArrayObtainStyledAttributes2.getColor(com.support.progressbar.R.styleable.COUIInstallLoadProgress_disabledColor, 0);
            typedArrayObtainStyledAttributes2.recycle();
        }
    }

    @Deprecated
    public void setBtnTextColor(int i3) {
        this.mBtnTextColor = i3;
        this.mBtnTextColorChanged = true;
        invalidate();
    }

    public void setBtnTextColorBySurpassProgress(int i3) {
        this.mSurpassProgressColor = i3;
        invalidate();
    }

    public void setBtnTextColorStateList(ColorStateList colorStateList) {
        this.mBtnTextColorStateList = colorStateList;
        if (colorStateList == null) {
            setBtnTextColor(-1);
        } else {
            setBtnTextColor(colorStateList.getDefaultColor());
        }
    }

    public void setDefaultTextSize(int i3) {
        this.mDefaultTextSize = i3;
    }

    public void setDisabledColor(int i3) {
        this.mDisabledColor = i3;
    }

    public void setDownloadingContentDecrpition(String str) {
        this.mDownloadingContentDecrpition = str;
    }

    public void setIsNeedVibrate(boolean z2) {
        this.mIsNeedVibrate = z2;
    }

    public void setLoadStyle(int i3) {
        if (i3 != 2) {
            this.mLoadStyle = i3;
            this.mRoundRectPaint = new Paint(1);
            if (this.mTextPaint != null || this.mDefaultTextColor == null) {
                return;
            }
            init();
            return;
        }
        this.mLoadStyle = 2;
        Paint paint = new Paint(1);
        this.mCirclePaint = paint;
        paint.setAntiAlias(true);
        Paint paint2 = new Paint(1);
        this.mInBitmapPaint = paint2;
        paint2.setAntiAlias(true);
        Paint paint3 = new Paint(1);
        this.mOutBitmapPaint = paint3;
        paint3.setAntiAlias(true);
        this.mCircleLoadBitmap = getBitmapFromVectorDrawable(com.support.progressbar.R.drawable.coui_install_load_progress_circle_load);
        this.mCircleReloadBitmap = getBitmapFromVectorDrawable(com.support.progressbar.R.drawable.coui_install_load_progress_circle_reload);
        this.mCirclePauseBitmap = getBitmapFromVectorDrawable(com.support.progressbar.R.drawable.coui_install_load_progress_circle_pause);
        int dimensionPixelSize = getResources().getDimensionPixelSize(com.support.progressbar.R.dimen.coui_install_download_progress_default_circle_radius);
        this.mTouchModeCircleRadius = dimensionPixelSize;
        int defaultSize = getDefaultSize(dimensionPixelSize, 1.5f, true);
        this.mDefaultCircleRadius = defaultSize;
        this.mCurrentCircleRadius = defaultSize;
    }

    public void setMaxBrightness(int i3) {
        this.mMaxBrightness = i3;
    }

    public void setText(String str) {
        if (str.equals(this.mTextView)) {
            return;
        }
        this.mTextView = str;
        if (this.mTextPaint != null) {
            addApostrophe();
        }
        invalidate();
    }

    @Override // android.widget.TextView
    public void setTextColor(int i3) {
        if (i3 != 0) {
            this.mUserTextColor = i3;
        }
    }

    public void setTextId(int i3) {
        setText(getResources().getString(i3));
    }

    public void setTextPadding(int i3) {
        this.mTextPadding = i3;
    }

    public void setTextSize(int i3) {
        if (i3 != 0) {
            this.mUserTextSize = i3;
        }
    }

    @Deprecated
    public void setThemeColor(int i3) {
        this.mThemeColor = i3;
        Bitmap bitmap = this.mCircleLoadBitmap;
        if (bitmap == null || bitmap.isRecycled()) {
            this.mCircleLoadBitmap = getBitmapFromVectorDrawable(com.support.progressbar.R.drawable.coui_install_load_progress_circle_load);
        }
        this.mCircleLoadBitmap = COUITintUtil.tintBitmap(this.mCircleLoadBitmap, this.mThemeColor);
        invalidate();
    }

    public void setThemeColorStateList(ColorStateList colorStateList) {
        this.mThemeColorStateList = colorStateList;
        if (colorStateList == null) {
            setThemeColor(-1);
        } else {
            setThemeColor(colorStateList.getDefaultColor());
        }
    }

    @Deprecated
    public void setThemeSecondaryColor(int i3) {
        this.mThemeSecondaryColor = i3;
        invalidate();
    }

    public void setThemeSecondaryColorStateList(ColorStateList colorStateList) {
        this.mThemeSecondaryColorStateList = colorStateList;
        if (colorStateList == null) {
            setThemeSecondaryColor(-1);
        } else {
            setThemeSecondaryColor(colorStateList.getDefaultColor());
        }
    }

    public void setTouchModeHeight(int i3) {
        this.mTouchModeHeight = i3;
    }

    public void setTouchModeWidth(int i3) {
        this.mTouchModeWidth = i3;
        this.mDefaultWidth = getDefaultSize(i3, 1.5f, false);
        if (this.mTextPaint != null) {
            addApostrophe();
        }
        requestLayout();
    }

    public COUIInstallLoadProgress(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, com.support.progressbar.R.attr.couiInstallLoadProgressStyle);
    }

    public COUIInstallLoadProgress(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, com.support.progressbar.R.style.Widget_COUI_COUILoadProgress_InstallDownload);
    }

    public COUIInstallLoadProgress(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3);
        this.TAG = "COUIInstallLoadProgress";
        this.DEBUG = true;
        this.mTextPaint = null;
        this.mUserTextSize = 0;
        this.mTextPadding = 0;
        this.mApostrophe = null;
        this.mFmi = null;
        this.mCurrentRoundBorderRadius = 0;
        this.mRoundRectPaint = null;
        this.mSpace = 0;
        this.mIsChangeTextColor = false;
        this.mRoundRectPath = new Path();
        this.mTouchModeCircleRadius = 0;
        this.mDefaultCircleRadius = 0;
        this.mCurrentCircleRadius = 0.0f;
        this.mCurrentInBitmapAlpha = 255;
        this.mCurrentOutBitmapAlpha = 0;
        this.mCirclePaint = null;
        this.mInBitmapPaint = null;
        this.mOutBitmapPaint = null;
        this.mInstallGiftBg = null;
        this.mLoadStyle = 0;
        this.mBtnTextColorChanged = false;
        this.mCurrentBrightness = 1.0f;
        this.mUserTextColor = -1;
        this.mExpandOffsetY = 0;
        this.mExpandOffsetX = 0;
        this.mNarrowOffsetFont = 1.0f;
        this.mColorHsl = new float[3];
        COUIDarkModeUtil.setForceDarkAllow(this, false);
        if (attributeSet != null && attributeSet.getStyleAttribute() != 0) {
            this.mStyle = attributeSet.getStyleAttribute();
        } else {
            this.mStyle = i3;
        }
        this.mContext = context;
        TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(mAttrArray);
        this.mColorPrimary = typedArrayObtainStyledAttributes.getColor(0, 0);
        this.mColorSecondary = typedArrayObtainStyledAttributes.getColor(1, 0);
        typedArrayObtainStyledAttributes.recycle();
        this.mLocale = Locale.getDefault();
        TypedArray typedArrayObtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, com.support.progressbar.R.styleable.COUILoadProgress, i3, i4);
        Resources resources = getResources();
        int i5 = com.support.progressbar.R.color.coui_install_load_progress_text_color_in_progress;
        this.mColorWhite = resources.getColor(i5);
        this.mSurpassProgressColor = getResources().getColor(i5);
        this.mIsNeedVibrate = typedArrayObtainStyledAttributes2.getBoolean(com.support.progressbar.R.styleable.COUILoadProgress_loadingButtonNeedVibrate, false);
        Drawable drawable = typedArrayObtainStyledAttributes2.getDrawable(com.support.progressbar.R.styleable.COUILoadProgress_couiDefaultDrawable);
        if (drawable != null) {
            setButtonDrawable(drawable);
        }
        setState(typedArrayObtainStyledAttributes2.getInteger(com.support.progressbar.R.styleable.COUILoadProgress_couiState, 0));
        typedArrayObtainStyledAttributes2.recycle();
        int dimensionPixelSize = getResources().getDimensionPixelSize(com.support.progressbar.R.dimen.coui_install_download_progress_textsize);
        TypedArray typedArrayObtainStyledAttributes3 = context.obtainStyledAttributes(attributeSet, com.support.progressbar.R.styleable.COUIInstallLoadProgress, i3, i4);
        setLoadStyle(typedArrayObtainStyledAttributes3.getInteger(com.support.progressbar.R.styleable.COUIInstallLoadProgress_couiStyle, 0));
        this.mInstallGiftBg = typedArrayObtainStyledAttributes3.getDrawable(com.support.progressbar.R.styleable.COUIInstallLoadProgress_couiInstallGiftBg);
        this.mTouchModeHeight = typedArrayObtainStyledAttributes3.getDimensionPixelSize(com.support.progressbar.R.styleable.COUIInstallLoadProgress_couiInstallViewHeight, 0);
        int dimensionPixelOffset = typedArrayObtainStyledAttributes3.getDimensionPixelOffset(com.support.progressbar.R.styleable.COUIInstallLoadProgress_couiInstallViewWidth, 0);
        this.mTouchModeWidth = dimensionPixelOffset;
        this.mDefaultWidth = getDefaultSize(dimensionPixelOffset, 1.5f, false);
        this.mMaxBrightness = typedArrayObtainStyledAttributes3.getFloat(com.support.progressbar.R.styleable.COUIInstallLoadProgress_brightness, 0.8f);
        this.mDisabledColor = typedArrayObtainStyledAttributes3.getColor(com.support.progressbar.R.styleable.COUIInstallLoadProgress_disabledColor, 0);
        this.mPressAnimationInterpolator = new COUIMoveEaseInterpolator();
        this.mNormalAnimationInterpolator = new COUIMoveEaseInterpolator();
        int i6 = this.mLoadStyle;
        if (i6 != 2) {
            if (i6 == 1) {
                this.mCurrentRoundBorderRadius = getResources().getDimensionPixelSize(com.support.progressbar.R.dimen.coui_install_download_progress_round_border_radius);
            } else {
                this.mCurrentRoundBorderRadius = getResources().getDimensionPixelSize(com.support.progressbar.R.dimen.coui_install_download_progress_round_border_radius_small);
                if (!isZhLanguage(this.mLocale)) {
                    int dimensionPixelSize2 = getResources().getDimensionPixelSize(com.support.progressbar.R.dimen.coui_install_download_progress_width_in_foreign_language);
                    this.mTouchModeWidth += dimensionPixelSize2;
                    this.mDefaultWidth += dimensionPixelSize2;
                }
            }
            this.mDefaultTextColor = typedArrayObtainStyledAttributes3.getColorStateList(com.support.progressbar.R.styleable.COUIInstallLoadProgress_couiInstallDefaultColor);
            this.mTextPadding = typedArrayObtainStyledAttributes3.getDimensionPixelOffset(com.support.progressbar.R.styleable.COUIInstallLoadProgress_couiInstallPadding, 0);
            this.mTextView = typedArrayObtainStyledAttributes3.getString(com.support.progressbar.R.styleable.COUIInstallLoadProgress_couiInstallTextview);
            this.mDefaultTextSize = typedArrayObtainStyledAttributes3.getDimensionPixelSize(com.support.progressbar.R.styleable.COUIInstallLoadProgress_couiInstallTextsize, dimensionPixelSize);
            this.mDefaultTextSize = (int) COUIChangeTextUtil.getSuitableFontSize(this.mDefaultTextSize, getResources().getConfiguration().fontScale, 2);
            if (this.mApostrophe == null) {
                this.mApostrophe = getResources().getString(com.support.progressbar.R.string.coui_install_load_progress_apostrophe);
            }
        } else {
            this.mCurrentRoundBorderRadius = getResources().getDimensionPixelSize(com.support.progressbar.R.dimen.coui_install_download_progress_circle_round_border_radius);
        }
        setThemeColorStateList(typedArrayObtainStyledAttributes3.getColorStateList(com.support.progressbar.R.styleable.COUIInstallLoadProgress_couiThemeColor));
        setThemeSecondaryColorStateList(typedArrayObtainStyledAttributes3.getColorStateList(com.support.progressbar.R.styleable.COUIInstallLoadProgress_couiThemeColorSecondary));
        setBtnTextColorStateList(typedArrayObtainStyledAttributes3.getColorStateList(com.support.progressbar.R.styleable.COUIInstallLoadProgress_couiThemeTextColor));
        typedArrayObtainStyledAttributes3.recycle();
        this.mRadiusOffset = getResources().getDimension(com.support.progressbar.R.dimen.coui_install_download_progress_round_border_radius_offset);
        if (isOs16()) {
            this.mOplusPath = new OplusPathAdapter(this.mRoundRectPath, 1);
            setOutlineProvider(new AnonymousClass1());
            setClipToOutline(true);
        }
        init();
    }
}