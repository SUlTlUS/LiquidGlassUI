package com.coui.appcompat.chip;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.BoringLayout;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.core.text.BidiFormatter;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.reddot.COUIHintRedDotHelper;
import com.coui.appcompat.roundRect.COUIShapePath;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.coui.appcompat.state.COUIMaskEffectDrawable;
import com.coui.appcompat.state.COUIStrokeDrawable;
import com.coui.appcompat.state.StatefulDrawableListener;
import com.decouple.injector.adaptor.BaseConfig;
import com.google.android.material.canvas.CanvasCompat;
import com.google.android.material.drawable.DrawableUtils;
import com.google.android.material.internal.TextDrawableHelper;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.resources.TextAppearance;
import com.oplus.graphics.OplusPathAdapter;
import com.support.chip.R;
import java.lang.ref.WeakReference;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class COUIChipDrawable extends Drawable implements Drawable.Callback, TextDrawableHelper.TextDrawableDelegate, StatefulDrawableListener {
    private static final int COUNT_TEXT_SIZE = 12;
    private static final boolean DEBUG;
    private static final int[] DEFAULT_STATE;
    private static final int ELLIPSIZE_END = 3;
    private static final int ELLIPSIZE_MARQUEE = 4;
    private static final int ELLIPSIZE_MIDDLE = 2;
    private static final int ELLIPSIZE_START = 1;
    private static final float FLOATING_PRECISE = 1.0E-8f;
    private static final float FULL_ALPHA = 255.0f;
    private static final float INVALID_VALUE = -1.0f;
    private static final String TAG = "COUIChipDrawable";
    private int mBackgroundTintCurrentValue;
    private int mCheckedBackgroundColor;
    private int mCheckedChipIconTint;
    private int mCheckedDisabledBackgroundColor;
    private int mCheckedDisabledChipIconTint;
    private int mCheckedDisabledTextColor;
    private TextAppearance mCheckedTextAppearance;
    private int mCheckedTextColor;
    private float mChipCornerRadius;
    protected float mChipEndPadding;
    private float mChipIconEndPadding;
    private float mChipIconSize;
    private float mChipIconStartPadding;
    private int mChipIconTintCurrentValue;
    private float mChipMinHeight;
    protected float mChipStartPadding;
    private CharSequence mCloseIconContentDescription;
    private final COUIMaskEffectDrawable mCloseIconEffectDrawable;
    private float mCloseIconEndPadding;
    private float mCloseIconSize;
    private float mCloseIconStartPadding;
    private int[] mCloseIconStateSet;
    private float mCloseIconTouchBoundsSize;
    private boolean mCloseIconVisible;
    private Context mContext;
    protected float mCountMarginEnd;
    protected float mCountMarginStart;
    private CharSequence mCountText;
    private TextAppearance mCountTextAppearance;
    private int mCountTextColor;
    private Paint mCountTextPaint;
    private boolean mCountVisible;
    private Paint mDebugPaint;
    private TextAppearance mDefaultTextAppearance;
    private TextAppearance mDescriptionTextAppearance;
    private final COUIMaskEffectDrawable mMaskEffectDrawable;
    protected final int mRedDotOffsetHorizontal;
    private final int mRedDotOffsetVertical;
    private int mRefreshStyle;
    private boolean mShouldDrawText;
    private boolean mShowRedDot;
    private final SmoothRoundCornerHelper mSmoothRoundCornerHelper;
    private final COUIStrokeDrawable mStrokeDrawable;
    protected CharSequence mText;
    protected final TextDrawableHelper mTextDrawableHelper;
    protected float mTextEndPadding;
    private int mTextMaxWidth;
    protected float mTextStartPadding;
    private int mTextTintCurrentValue;
    private TextUtils.TruncateAt mTruncateAt;
    private int mUncheckedBackgroundColor;
    private int mUncheckedChipIconTint;
    private int mUncheckedDisabledBackgroundColor;
    private int mUncheckedDisabledChipIconTint;
    private int mUncheckedDisabledTextColor;
    private int mUncheckedTextColor;
    private final Paint.FontMetrics mFontMetrics = new Paint.FontMetrics();
    private final PointF mTmpLocation = new PointF();
    private final RectF mTmpBounds = new RectF();
    private final RectF mCloseIconTouchRect = new RectF();
    private final Paint mBackgroundPaint = new Paint(1);
    private final Path mBackgroundPath = new Path();
    protected COUIHintRedDotHelper mHintRedDotHelper = null;
    protected float mChipBackgroundWidthCloseIconInvisible = -1.0f;
    protected float mChipBackgroundWidthCloseIconVisible = -1.0f;
    protected float mChipBackgroundCurrentWidth = -1.0f;
    protected float mChipTextPaddingEndCloseIconInvisible = -1.0f;
    protected int mMaxWidth = Integer.MAX_VALUE;
    protected int mIntrinsicWidth = 0;
    protected int mMinWidth = 0;
    protected boolean mChipIconApplyTint = false;
    private float mChipTextDrawTranslationX = 0.0f;
    private float mChipTextDrawTranslationY = 0.0f;
    protected float mChipTextPaddingStartCloseIconInvisible = -1.0f;
    private float mChipTextPaddingStartCloseIconVisible = -1.0f;
    private float mChipTextPaddingEndCloseIconVisible = -1.0f;
    private TintAnimation mTintAnimation = null;
    private CloseIconAnimation mCloseIconAnimation = null;
    private Drawable mChipBackground = null;
    private Drawable mChipIcon = new ColorDrawable(-16711936);
    private Drawable mCloseIcon = new ColorDrawable(-16777216);
    private boolean mCheckable = false;
    private boolean mIsChecked = false;
    private boolean mChipIconVisible = true;
    private int mAlpha = 255;
    private WeakReference<COUIChipDrawableDelegate> mDelegate = new WeakReference<>(null);

    public interface COUIChipDrawableDelegate {
        void onChipDrawableSizeChange();

        void onChipTextColorChange(int i3);

        void onChipTextFontChanged(Typeface typeface);

        void onChipTextOffsetChanged(float f3, float f4);
    }

    public static abstract class ChipAnimation {
        static final ArgbEvaluator DEFAULT_ARGB_EVALUATOR = new ArgbEvaluator();
        static final float DEFAULT_CHIP_SPRING_ANIMATION_BOUNCE = 0.0f;
        static final float DEFAULT_CHIP_SPRING_ANIMATION_RANGE = 10000.0f;
        static final float DEFAULT_CHIP_SPRING_ANIMATION_RESPONSE = 0.3f;
        COUISpringAnimation mAnimation;
        final String mAnimationTag;
        float mCurrentProgress = 0.0f;
        final FloatPropertyCompat<ChipAnimation> mTransition;

        /* JADX INFO: renamed from: com.coui.appcompat.chip.COUIChipDrawable$ChipAnimation$1, reason: invalid class name */
        /* JADX INFO: loaded from: classes7.dex */
        public class AnonymousClass1 extends FloatPropertyCompat<ChipAnimation> {
            public AnonymousClass1(String str) {
                super(str);
            }

            @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
            public /* bridge */ /* synthetic */ float getValue(ChipAnimation chipAnimation) {
                return getValue2(chipAnimation);
            }

            @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
            public /* bridge */ /* synthetic */ void setValue(ChipAnimation chipAnimation, float f3) {
                setValue2(chipAnimation, f3);
            }

            /* JADX INFO: renamed from: getValue, reason: avoid collision after fix types in other method */
            public float getValue2(ChipAnimation chipAnimation) {
                return chipAnimation.getValue();
            }

            /* JADX INFO: renamed from: setValue, reason: avoid collision after fix types in other method */
            public void setValue2(ChipAnimation chipAnimation, float f3) {
                chipAnimation.setValue(f3);
            }
        }

        public ChipAnimation(String str) {
            this.mAnimationTag = str;
            this.mTransition = new AnonymousClass1(str);
        }

        public COUISpringAnimation getAnimation() {
            return this.mAnimation;
        }

        public float getFraction() {
            return this.mCurrentProgress / 10000.0f;
        }

        public float getValue() {
            return this.mCurrentProgress;
        }

        public void setValue(float f3) {
            this.mCurrentProgress = f3;
        }
    }

    public final class CloseIconAnimation extends ChipAnimation {
        private static final float CLOSE_ICON_SCALE_MIN_VALUE = 0.3f;

        public CloseIconAnimation() {
            super("CloseIconAnimation");
            COUISpringForce cOUISpringForce = new COUISpringForce();
            cOUISpringForce.setBounce(0.0f);
            cOUISpringForce.setResponse(0.3f);
            COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(this, this.mTransition);
            this.mAnimation = cOUISpringAnimation;
            cOUISpringAnimation.setSpring(cOUISpringForce);
            this.mAnimation.addEndListener(new d(this));
        }

        public static /* synthetic */ void a(CloseIconAnimation closeIconAnimation, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
            COUIChipDrawable.access$1000(COUIChipDrawable.this, 0.0f, 0.0f);
        }

        public float getCloseIconScaleFraction() {
            return (getFraction() * 0.7f) + 0.3f;
        }

        /* JADX WARN: Code duplicated, block: B:11:0x0054  */
        /* JADX WARN: Code duplicated, block: B:7:0x0040  */
        /* JADX WARN: Code duplicated, block: B:9:0x0048  */
        @Override // com.coui.appcompat.chip.COUIChipDrawable.ChipAnimation
        public void setValue(float f3) {
            COUIChipDrawable cOUIChipDrawable;
            super.setValue(f3);
            COUIChipDrawable cOUIChipDrawable2 = COUIChipDrawable.this;
            float fraction = (cOUIChipDrawable2.mChipBackgroundWidthCloseIconVisible - cOUIChipDrawable2.mChipBackgroundWidthCloseIconInvisible) * getFraction();
            COUIChipDrawable cOUIChipDrawable3 = COUIChipDrawable.this;
            cOUIChipDrawable2.mChipBackgroundCurrentWidth = fraction + cOUIChipDrawable3.mChipBackgroundWidthCloseIconInvisible;
            if (!cOUIChipDrawable3.isRTL()) {
                COUIChipDrawable cOUIChipDrawable4 = COUIChipDrawable.this;
                if (cOUIChipDrawable4.mChipTextPaddingStartCloseIconInvisible != COUIChipDrawable.access$900(cOUIChipDrawable4)) {
                    COUIChipDrawable cOUIChipDrawable5 = COUIChipDrawable.this;
                    COUIChipDrawable.access$1000(cOUIChipDrawable5, ((int) (cOUIChipDrawable5.mChipTextPaddingStartCloseIconInvisible - COUIChipDrawable.access$900(cOUIChipDrawable5))) * (1.0f - getFraction()), 0.0f);
                } else if (COUIChipDrawable.this.isRTL()) {
                    cOUIChipDrawable = COUIChipDrawable.this;
                    if (cOUIChipDrawable.mChipTextPaddingEndCloseIconInvisible != COUIChipDrawable.access$1100(cOUIChipDrawable)) {
                        COUIChipDrawable cOUIChipDrawable6 = COUIChipDrawable.this;
                        COUIChipDrawable.access$1000(cOUIChipDrawable6, ((int) (cOUIChipDrawable6.mChipTextPaddingEndCloseIconInvisible - COUIChipDrawable.access$1100(cOUIChipDrawable6))) * (1.0f - getFraction()), 0.0f);
                    }
                }
            } else if (COUIChipDrawable.this.isRTL()) {
                cOUIChipDrawable = COUIChipDrawable.this;
                if (cOUIChipDrawable.mChipTextPaddingEndCloseIconInvisible != COUIChipDrawable.access$1100(cOUIChipDrawable)) {
                    COUIChipDrawable cOUIChipDrawable7 = COUIChipDrawable.this;
                    COUIChipDrawable.access$1000(cOUIChipDrawable7, ((int) (cOUIChipDrawable7.mChipTextPaddingEndCloseIconInvisible - COUIChipDrawable.access$1100(cOUIChipDrawable7))) * (1.0f - getFraction()), 0.0f);
                }
            }
            if (f3 == 0.0f) {
                COUIChipDrawable.this.onSizeChange();
            }
            COUIChipDrawable.this.invalidateSelf();
        }
    }

    public final class SmoothRoundCornerHelper {
        private OplusPathAdapter mPathAdapter;
        private final int mSmoothStyleType;

        public SmoothRoundCornerHelper() {
            this.mPathAdapter = null;
            int smoothStyleType = RoundCornerUtil.getSmoothStyleType();
            this.mSmoothStyleType = smoothStyleType;
            if (smoothStyleType == 1) {
                this.mPathAdapter = new OplusPathAdapter(COUIChipDrawable.access$1200(COUIChipDrawable.this), smoothStyleType);
            }
        }

        public OplusPathAdapter getPathAdapter() {
            return this.mPathAdapter;
        }

        public int getSmoothStyleType() {
            return this.mSmoothStyleType;
        }

        public void updatePath(float f3) {
            COUIShapePath.getRoundRectPath(COUIChipDrawable.access$1200(COUIChipDrawable.this), COUIChipDrawable.access$1300(COUIChipDrawable.this), f3);
        }
    }

    public final class TintAnimation extends ChipAnimation {
        public TintAnimation() {
            super("TintAnimation");
            COUISpringForce cOUISpringForce = new COUISpringForce();
            cOUISpringForce.setBounce(0.0f);
            cOUISpringForce.setResponse(0.3f);
            COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(this, this.mTransition);
            this.mAnimation = cOUISpringAnimation;
            cOUISpringAnimation.setSpring(cOUISpringForce);
        }

        @Override // com.coui.appcompat.chip.COUIChipDrawable.ChipAnimation
        public void setValue(float f3) {
            super.setValue(f3);
            COUIChipDrawable cOUIChipDrawable = COUIChipDrawable.this;
            ArgbEvaluator argbEvaluator = ChipAnimation.DEFAULT_ARGB_EVALUATOR;
            COUIChipDrawable.access$002(cOUIChipDrawable, ((Integer) argbEvaluator.evaluate(getFraction(), Integer.valueOf(COUIChipDrawable.access$100(COUIChipDrawable.this)), Integer.valueOf(COUIChipDrawable.access$200(COUIChipDrawable.this)))).intValue());
            COUIChipDrawable.access$302(COUIChipDrawable.this, ((Integer) argbEvaluator.evaluate(getFraction(), Integer.valueOf(COUIChipDrawable.access$400(COUIChipDrawable.this)), Integer.valueOf(COUIChipDrawable.access$500(COUIChipDrawable.this)))).intValue());
            COUIChipDrawable.access$602(COUIChipDrawable.this, ((Integer) argbEvaluator.evaluate(getFraction(), Integer.valueOf(COUIChipDrawable.access$700(COUIChipDrawable.this)), Integer.valueOf(COUIChipDrawable.access$800(COUIChipDrawable.this)))).intValue());
            COUIChipDrawable cOUIChipDrawable2 = COUIChipDrawable.this;
            cOUIChipDrawable2.onChipTextColorChange(COUIChipDrawable.access$600(cOUIChipDrawable2));
            COUIChipDrawable.this.invalidateSelf();
        }
    }

    static {
        DEBUG = COUILog.LOG_DEBUG || COUILog.isLoggable(TAG, 3);
        DEFAULT_STATE = new int[]{16842910};
    }

    public COUIChipDrawable(Context context, AttributeSet attributeSet, int i3, int i4) {
        this.mDebugPaint = null;
        this.mCountTextPaint = null;
        this.mContext = context;
        TextDrawableHelper textDrawableHelper = new TextDrawableHelper(this);
        this.mTextDrawableHelper = textDrawableHelper;
        textDrawableHelper.getTextPaint().density = context.getResources().getDisplayMetrics().density;
        this.mText = "";
        Paint paint = DEBUG ? new Paint(1) : null;
        this.mDebugPaint = paint;
        if (paint != null) {
            paint.setStyle(Paint.Style.STROKE);
        }
        int[] iArr = DEFAULT_STATE;
        setCloseIconState(iArr);
        setState(iArr);
        this.mSmoothRoundCornerHelper = new SmoothRoundCornerHelper();
        setShouldDrawText(true);
        COUIMaskEffectDrawable cOUIMaskEffectDrawable = new COUIMaskEffectDrawable(context, 0);
        this.mMaskEffectDrawable = cOUIMaskEffectDrawable;
        COUIStrokeDrawable cOUIStrokeDrawable = new COUIStrokeDrawable(context);
        this.mStrokeDrawable = cOUIStrokeDrawable;
        cOUIMaskEffectDrawable.setStatefulDrawableListener(this);
        cOUIStrokeDrawable.setStatefulDrawableListener(this);
        COUIMaskEffectDrawable cOUIMaskEffectDrawable2 = new COUIMaskEffectDrawable(context, 1);
        this.mCloseIconEffectDrawable = cOUIMaskEffectDrawable2;
        cOUIMaskEffectDrawable2.setStatefulDrawableListener(this);
        this.mRedDotOffsetHorizontal = context.getResources().getDimensionPixelOffset(R.dimen.coui_chip_red_dot_offset_horizontal);
        this.mRedDotOffsetVertical = context.getResources().getDimensionPixelOffset(R.dimen.coui_chip_red_dot_offset_vertical);
        Paint paint2 = new Paint(1);
        this.mCountTextPaint = paint2;
        paint2.setTextAlign(Paint.Align.LEFT);
        try {
            int attrColor = COUIContextUtil.getAttrColor(context, com.support.appcompat.R.attr.couiColorLabelSecondary);
            this.mCountTextColor = attrColor;
            this.mCountTextPaint.setColor(attrColor);
        } catch (Exception unused) {
            this.mCountTextColor = -7829368;
            this.mCountTextPaint.setColor(-7829368);
        }
        this.mCountMarginStart = context.getResources().getDisplayMetrics().density * 2.0f;
        this.mCountVisible = false;
        loadFromAttributes(attributeSet, i3, i4);
    }

    public static /* synthetic */ int access$002(COUIChipDrawable cOUIChipDrawable, int i3) {
        cOUIChipDrawable.mBackgroundTintCurrentValue = i3;
        return i3;
    }

    public static /* synthetic */ int access$100(COUIChipDrawable cOUIChipDrawable) {
        return cOUIChipDrawable.mUncheckedBackgroundColor;
    }

    public static /* synthetic */ void access$1000(COUIChipDrawable cOUIChipDrawable, float f3, float f4) {
        cOUIChipDrawable.onChipTextOffsetChanged(f3, f4);
    }

    public static /* synthetic */ float access$1100(COUIChipDrawable cOUIChipDrawable) {
        return cOUIChipDrawable.mChipTextPaddingEndCloseIconVisible;
    }

    public static /* synthetic */ Path access$1200(COUIChipDrawable cOUIChipDrawable) {
        return cOUIChipDrawable.mBackgroundPath;
    }

    public static /* synthetic */ RectF access$1300(COUIChipDrawable cOUIChipDrawable) {
        return cOUIChipDrawable.mTmpBounds;
    }

    public static /* synthetic */ int access$200(COUIChipDrawable cOUIChipDrawable) {
        return cOUIChipDrawable.mCheckedBackgroundColor;
    }

    public static /* synthetic */ int access$302(COUIChipDrawable cOUIChipDrawable, int i3) {
        cOUIChipDrawable.mChipIconTintCurrentValue = i3;
        return i3;
    }

    public static /* synthetic */ int access$400(COUIChipDrawable cOUIChipDrawable) {
        return cOUIChipDrawable.mUncheckedChipIconTint;
    }

    public static /* synthetic */ int access$500(COUIChipDrawable cOUIChipDrawable) {
        return cOUIChipDrawable.mCheckedChipIconTint;
    }

    public static /* synthetic */ int access$600(COUIChipDrawable cOUIChipDrawable) {
        return cOUIChipDrawable.mTextTintCurrentValue;
    }

    public static /* synthetic */ int access$602(COUIChipDrawable cOUIChipDrawable, int i3) {
        cOUIChipDrawable.mTextTintCurrentValue = i3;
        return i3;
    }

    public static /* synthetic */ int access$700(COUIChipDrawable cOUIChipDrawable) {
        return cOUIChipDrawable.mUncheckedTextColor;
    }

    public static /* synthetic */ int access$800(COUIChipDrawable cOUIChipDrawable) {
        return cOUIChipDrawable.mCheckedTextColor;
    }

    public static /* synthetic */ float access$900(COUIChipDrawable cOUIChipDrawable) {
        return cOUIChipDrawable.mChipTextPaddingStartCloseIconVisible;
    }

    private void applyChildDrawable(Drawable drawable) {
        if (drawable == null) {
            return;
        }
        drawable.setCallback(this);
        DrawableCompat.setLayoutDirection(drawable, DrawableCompat.getLayoutDirection(this));
        drawable.setLevel(getLevel());
        drawable.setVisible(isVisible(), false);
        if (drawable == this.mChipIcon && drawable.isStateful()) {
            drawable.setState(getState());
        }
        if (drawable == this.mCloseIcon && drawable.isStateful()) {
            drawable.setState(getCloseIconState());
        }
    }

    private boolean applyCountTextFromTextAppearance(TextAppearance textAppearance) {
        if (this.mCountTextPaint == null || textAppearance == null || textAppearance.getTextSize() <= 0.0f) {
            return false;
        }
        this.mCountTextPaint.setTextSize(textAppearance.getTextSize());
        Typeface fallbackFont = textAppearance.getFallbackFont();
        if (fallbackFont == null) {
            return true;
        }
        this.mCountTextPaint.setTypeface(fallbackFont);
        return true;
    }

    private void calculateChipTouchBounds(Rect rect, RectF rectF) {
        rectF.set(rect);
        if (showsCloseIcon()) {
            float f3 = this.mCloseIconEndPadding + this.mCloseIconSize + this.mCloseIconStartPadding;
            if (isRTL()) {
                rectF.left = rect.left + f3;
            } else {
                rectF.right = rect.right - f3;
            }
        }
    }

    private void calculateCloseIconBounds(Rect rect, RectF rectF) {
        rectF.setEmpty();
        if (showsCloseIcon() || closeIconIsVisibleAndAnimating()) {
            float f3 = this.mCloseIconEndPadding;
            if (isRTL()) {
                float f4 = rect.left + f3;
                rectF.left = f4;
                rectF.right = f4 + this.mCloseIconSize;
            } else {
                float f5 = rect.right - f3;
                rectF.right = f5;
                rectF.left = f5 - this.mCloseIconSize;
            }
            float fExactCenterY = rect.exactCenterY();
            float f6 = this.mCloseIconSize;
            float f7 = fExactCenterY - (f6 / 2.0f);
            rectF.top = f7;
            rectF.bottom = f7 + f6;
        }
    }

    private void calculateCloseIconTouchBounds(Rect rect, RectF rectF) {
        rectF.setEmpty();
        if (showsCloseIcon()) {
            float closeIconSize = this.mCloseIconEndPadding + getCloseIconSize() + this.mCloseIconStartPadding;
            if (isRTL()) {
                int i3 = rect.left;
                rectF.left = i3;
                rectF.right = i3 + closeIconSize;
            } else {
                float f3 = rect.right;
                rectF.right = f3;
                rectF.left = f3 - closeIconSize;
            }
            rectF.top = rect.top;
            rectF.bottom = rect.bottom;
        }
    }

    private float calculateCountTextCenterFromBaseline() {
        Paint paint = this.mCountTextPaint;
        if (paint == null) {
            return 0.0f;
        }
        paint.getFontMetrics(this.mFontMetrics);
        Paint.FontMetrics fontMetrics = this.mFontMetrics;
        return (fontMetrics.descent + fontMetrics.ascent) / 2.0f;
    }

    private int calculateMinTextWidth() {
        if (TextUtils.isEmpty(getText())) {
            return 0;
        }
        return Math.max((int) (((((this.mMinWidth - this.mChipStartPadding) - (showsChipIcon() ? calculateChipIconWidth() : 0.0f)) - this.mTextStartPadding) - this.mTextEndPadding) - ((showsCloseIcon() || closeIconIsVisibleAndAnimating()) ? calculateCloseIconWidth() : this.mChipEndPadding)), 0);
    }

    private float calculateTextCenterFromBaseline() {
        this.mTextDrawableHelper.getTextPaint().getFontMetrics(this.mFontMetrics);
        Paint.FontMetrics fontMetrics = this.mFontMetrics;
        return (fontMetrics.descent + fontMetrics.ascent) / 2.0f;
    }

    private void clipSmoothRoundRect(Canvas canvas, float f3) {
        canvas.save();
        this.mBackgroundPath.reset();
        OplusPathAdapter pathAdapter = this.mSmoothRoundCornerHelper.getPathAdapter();
        RectF rectF = this.mTmpBounds;
        pathAdapter.addSmoothRoundRect(rectF.left, rectF.top, rectF.right, rectF.bottom, f3, f3, Path.Direction.CCW);
        canvas.clipPath(this.mBackgroundPath);
        canvas.drawColor(this.mBackgroundPaint.getColor());
        canvas.restore();
    }

    public static COUIChipDrawable createFromAttributes(Context context, AttributeSet attributeSet, int i3, int i4) {
        return new COUIChipDrawable(context, attributeSet, i3, i4);
    }

    public static COUIChipDrawable createFromResource(Context context, int i3) {
        AttributeSet drawableXml = DrawableUtils.parseDrawableXml(context, i3, "chip");
        int styleAttribute = drawableXml.getStyleAttribute();
        if (styleAttribute == 0) {
            styleAttribute = R.style.Widget_COUI_Chip_Suggestion;
        }
        return createFromAttributes(context, drawableXml, R.attr.couiChipStyle, styleAttribute);
    }

    private void drawChipBackground(Canvas canvas, Rect rect) {
        if (this.mChipBackgroundCurrentWidth == -1.0f) {
            this.mChipBackgroundCurrentWidth = showsCloseIcon() ? this.mChipBackgroundWidthCloseIconVisible : this.mChipBackgroundWidthCloseIconInvisible;
        }
        Drawable drawable = this.mChipBackground;
        if (drawable != null) {
            drawable.setBounds(rect);
            this.mChipBackground.draw(canvas);
            return;
        }
        this.mBackgroundPaint.setColor(this.mBackgroundTintCurrentValue);
        if (this.mCloseIcon == null) {
            this.mTmpBounds.set(rect);
        } else if (isRTL()) {
            RectF rectF = this.mTmpBounds;
            int i3 = rect.right;
            rectF.set(i3 - this.mChipBackgroundCurrentWidth, rect.top, i3, rect.bottom);
        } else {
            RectF rectF2 = this.mTmpBounds;
            int i4 = rect.left;
            rectF2.set(i4, rect.top, i4 + this.mChipBackgroundCurrentWidth, rect.bottom);
        }
        float fMin = this.mChipCornerRadius;
        if (fMin < 0.0f) {
            fMin = Math.min(this.mTmpBounds.width(), this.mTmpBounds.height()) / 2.0f;
        }
        int smoothStyleType = this.mSmoothRoundCornerHelper.getSmoothStyleType();
        if (smoothStyleType == 0) {
            drawSmoothRoundRect(canvas, fMin);
        } else if (smoothStyleType != 1) {
            drawRoundRect(canvas, fMin);
        } else {
            clipSmoothRoundRect(canvas, fMin);
        }
    }

    private void drawRoundRect(Canvas canvas, float f3) {
        canvas.drawRoundRect(this.mTmpBounds, f3, f3, this.mBackgroundPaint);
    }

    private void drawSmoothRoundRect(Canvas canvas, float f3) {
        canvas.save();
        this.mBackgroundPath.reset();
        this.mSmoothRoundCornerHelper.updatePath(f3);
        canvas.drawPath(this.mBackgroundPath, this.mBackgroundPaint);
        canvas.restore();
    }

    private boolean equals(float f3, float f4) {
        return Math.abs(f3 - f4) < FLOATING_PRECISE;
    }

    private float getCurrentChipIconHeight() {
        Drawable drawable;
        float f3 = this.mChipIconSize;
        return (f3 > 0.0f || (drawable = this.mChipIcon) == null) ? f3 : Math.min(drawable.getIntrinsicHeight(), getIntrinsicHeight());
    }

    private float getCurrentChipIconWidth() {
        Drawable drawable;
        float f3 = this.mChipIconSize;
        return (f3 > 0.0f || (drawable = this.mChipIcon) == null) ? f3 : drawable.getIntrinsicWidth();
    }

    private void loadFromAttributes(AttributeSet attributeSet, int i3, int i4) {
        if (attributeSet != null) {
            this.mRefreshStyle = attributeSet.getStyleAttribute();
        }
        if (this.mRefreshStyle == 0) {
            this.mRefreshStyle = i3;
        }
        TypedArray typedArrayObtainStyledAttributes = this.mContext.obtainStyledAttributes(attributeSet, R.styleable.COUIChip, i3, i4);
        setCheckable(typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIChip_android_checkable, false));
        this.mIsChecked = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIChip_android_checked, false);
        this.mUncheckedBackgroundColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIChip_uncheckedBackgroundColor, 0);
        this.mCheckedBackgroundColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIChip_checkedBackgroundColor, 0);
        this.mUncheckedDisabledBackgroundColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIChip_uncheckedDisabledBackgroundColor, 0);
        this.mCheckedDisabledBackgroundColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIChip_checkedDisabledBackgroundColor, 0);
        this.mChipIconApplyTint = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIChip_chipIconApplyTint, true);
        this.mUncheckedChipIconTint = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIChip_uncheckedChipIconTint, 0);
        this.mCheckedChipIconTint = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIChip_checkedChipIconTint, 0);
        this.mUncheckedDisabledChipIconTint = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIChip_uncheckedDisabledChipIconTint, 0);
        this.mCheckedDisabledChipIconTint = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIChip_checkedDisabledChipIconTint, 0);
        this.mUncheckedTextColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIChip_uncheckedTextColor, 0);
        this.mCheckedTextColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIChip_checkedTextColor, 0);
        this.mUncheckedDisabledTextColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIChip_uncheckedDisabledTextColor, 0);
        this.mCheckedDisabledTextColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIChip_checkedDisabledTextColor, 0);
        this.mTextMaxWidth = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIChip_chipTextMaxWidth, Integer.MAX_VALUE);
        this.mChipMinHeight = typedArrayObtainStyledAttributes.getDimension(R.styleable.COUIChip_android_minHeight, 0.0f);
        this.mMinWidth = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIChip_android_minWidth, 0);
        this.mMaxWidth = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIChip_android_maxWidth, Integer.MAX_VALUE);
        this.mChipStartPadding = typedArrayObtainStyledAttributes.getDimension(R.styleable.COUIChip_chipStartPadding, 0.0f);
        this.mChipEndPadding = typedArrayObtainStyledAttributes.getDimension(R.styleable.COUIChip_chipEndPadding, 0.0f);
        this.mChipCornerRadius = typedArrayObtainStyledAttributes.getDimension(R.styleable.COUIChip_chipCornerRadius, -1.0f);
        this.mChipIcon = typedArrayObtainStyledAttributes.getDrawable(R.styleable.COUIChip_chipIcon);
        this.mChipIconVisible = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIChip_chipIconVisible, false);
        this.mChipIconSize = typedArrayObtainStyledAttributes.getDimension(R.styleable.COUIChip_chipIconSize, -1.0f);
        this.mChipIconStartPadding = typedArrayObtainStyledAttributes.getDimension(R.styleable.COUIChip_chipIconStartPadding, 0.0f);
        this.mChipIconEndPadding = typedArrayObtainStyledAttributes.getDimension(R.styleable.COUIChip_chipIconEndPadding, 0.0f);
        CharSequence text = typedArrayObtainStyledAttributes.getText(R.styleable.COUIChip_android_text);
        this.mText = text;
        if (text == null) {
            this.mText = "";
        }
        this.mTextStartPadding = typedArrayObtainStyledAttributes.getDimension(R.styleable.COUIChip_textStartPadding, 0.0f);
        this.mTextEndPadding = typedArrayObtainStyledAttributes.getDimension(R.styleable.COUIChip_textEndPadding, 0.0f);
        setTextAppearance(MaterialResources.getTextAppearance(this.mContext, typedArrayObtainStyledAttributes, R.styleable.COUIChip_android_textAppearance));
        setCheckedTextAppearance(MaterialResources.getTextAppearance(this.mContext, typedArrayObtainStyledAttributes, R.styleable.COUIChip_checkedTextAppearance));
        setDescriptionTextAppearance(MaterialResources.getTextAppearance(this.mContext, typedArrayObtainStyledAttributes, R.styleable.COUIChip_descriptionTextAppearance));
        setCountTextAppearance(MaterialResources.getTextAppearance(this.mContext, typedArrayObtainStyledAttributes, R.styleable.COUIChip_countTextAppearance));
        int i5 = R.styleable.COUIChip_android_textSize;
        TextAppearance textAppearance = this.mDefaultTextAppearance;
        setTextSize(typedArrayObtainStyledAttributes.getDimension(i5, textAppearance != null ? textAppearance.getTextSize() : 0.0f));
        int i6 = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIChip_android_ellipsize, 0);
        if (i6 == 1) {
            setEllipsize(TextUtils.TruncateAt.START);
        } else if (i6 == 2) {
            setEllipsize(TextUtils.TruncateAt.MIDDLE);
        } else if (i6 == 3) {
            setEllipsize(TextUtils.TruncateAt.END);
        }
        this.mCountMarginStart = typedArrayObtainStyledAttributes.getDimension(R.styleable.COUIChip_chipCountStartMargin, 0.0f);
        this.mCloseIcon = typedArrayObtainStyledAttributes.getDrawable(R.styleable.COUIChip_closeIcon);
        this.mCloseIconVisible = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIChip_closeIconVisible, false);
        this.mCloseIconSize = typedArrayObtainStyledAttributes.getDimension(R.styleable.COUIChip_closeIconSize, 0.0f);
        this.mCloseIconStartPadding = typedArrayObtainStyledAttributes.getDimension(R.styleable.COUIChip_closeIconStartPadding, 0.0f);
        this.mCloseIconEndPadding = typedArrayObtainStyledAttributes.getDimension(R.styleable.COUIChip_closeIconEndPadding, 0.0f);
        this.mCloseIconTouchBoundsSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIChip_closeIconTouchBoundsSize, 0);
        onTextSizeChange();
        typedArrayObtainStyledAttributes.recycle();
    }

    private float measureWithStaticLayout(TextView textView) {
        float f3 = 0.0f;
        if (textView.getText() != null && !TextUtils.isEmpty(textView.getText().toString())) {
            StaticLayout staticLayoutBuild = StaticLayout.Builder.obtain(textView.getText(), 0, textView.getText().length(), textView.getPaint(), Integer.MAX_VALUE).build();
            for (int i3 = 0; i3 < staticLayoutBuild.getLineCount(); i3++) {
                float lineWidth = staticLayoutBuild.getLineWidth(i3);
                if (lineWidth > f3) {
                    f3 = lineWidth;
                }
            }
        }
        return f3;
    }

    private void onChipTextOffsetChanged(float f3, float f4) {
        this.mChipTextDrawTranslationX = f3;
        this.mChipTextDrawTranslationY = f4;
        COUIChipDrawableDelegate cOUIChipDrawableDelegate = this.mDelegate.get();
        if (cOUIChipDrawableDelegate != null) {
            cOUIChipDrawableDelegate.onChipTextOffsetChanged(f3, f4);
        }
    }

    private void unapplyChildDrawable(Drawable drawable) {
        if (drawable != null) {
            drawable.setCallback(null);
        }
    }

    private void updateCountTextSize() {
        if (this.mCountTextPaint == null) {
            return;
        }
        if (applyCountTextFromTextAppearance(this.mCountTextAppearance) || applyCountTextFromTextAppearance(this.mDescriptionTextAppearance)) {
            this.mTextDrawableHelper.setTextWidthDirty(true);
            return;
        }
        float textSize = getTextSize();
        if (textSize <= 0.0f) {
            this.mCountTextPaint.setTextSize(this.mContext.getResources().getDisplayMetrics().density * 12.0f);
        } else {
            this.mCountTextPaint.setTextSize(textSize);
            this.mCountTextPaint.setTypeface(this.mTextDrawableHelper.getTextPaint().getTypeface());
            this.mTextDrawableHelper.setTextWidthDirty(true);
        }
    }

    private void updateIntrinsicWidth() {
        updateIntrinsicWidthCloseIconInvisible();
        updateIntrinsicWidthCloseIconVisible();
        int iMin = Math.min(Math.max(Math.round(this.mChipStartPadding + (showsChipIcon() ? calculateChipIconWidth() : 0.0f) + this.mTextStartPadding + getTextWidth() + this.mTextEndPadding + (showsCount() ? calculateCountWidth() : 0.0f) + ((showsCloseIcon() || closeIconIsVisibleAndAnimating()) ? calculateCloseIconWidth() : this.mChipEndPadding)), this.mMinWidth), this.mMaxWidth);
        this.mIntrinsicWidth = iMin;
        updateChipTextOffsetRange(iMin);
    }

    private void updateIntrinsicWidthCloseIconInvisible() {
        this.mChipBackgroundWidthCloseIconInvisible = Math.min(Math.max(Math.round(this.mChipStartPadding + (showsChipIcon() ? calculateChipIconWidth() : 0.0f) + this.mTextStartPadding + getTextWidth() + this.mTextEndPadding + (showsCount() ? calculateCountWidth() : 0.0f) + this.mChipEndPadding), this.mMinWidth), this.mMaxWidth);
    }

    private void updateIntrinsicWidthCloseIconVisible() {
        this.mChipBackgroundWidthCloseIconVisible = Math.min(Math.max(Math.round(this.mChipStartPadding + (showsChipIcon() ? calculateChipIconWidth() : 0.0f) + this.mTextStartPadding + getTextWidth() + this.mTextEndPadding + (showsCount() ? calculateCountWidth() : 0.0f) + (this.mCloseIcon != null ? this.mCloseIconStartPadding + this.mCloseIconSize + this.mCloseIconEndPadding : this.mChipEndPadding)), this.mMinWidth), this.mMaxWidth);
    }

    public void calculateChipIconBounds(Rect rect, RectF rectF) {
        rectF.setEmpty();
        if (showsChipIcon()) {
            float f3 = this.mChipStartPadding + this.mChipIconStartPadding;
            float currentChipIconWidth = getCurrentChipIconWidth();
            if (isRTL()) {
                float f4 = rect.right - f3;
                rectF.right = f4;
                rectF.left = f4 - currentChipIconWidth;
            } else {
                float f5 = rect.left + f3;
                rectF.left = f5;
                rectF.right = f5 + currentChipIconWidth;
            }
            float currentChipIconHeight = getCurrentChipIconHeight();
            float fExactCenterY = rect.exactCenterY() - (currentChipIconHeight / 2.0f);
            rectF.top = fExactCenterY;
            rectF.bottom = fExactCenterY + currentChipIconHeight;
        }
    }

    public float calculateChipIconWidth() {
        if (showsChipIcon()) {
            return this.mChipIconStartPadding + getCurrentChipIconWidth() + this.mChipIconEndPadding;
        }
        return 0.0f;
    }

    public float calculateCloseIconWidth() {
        return this.mCloseIconStartPadding + this.mCloseIconSize + this.mCloseIconEndPadding;
    }

    public void calculateCountBounds(Rect rect, RectF rectF) {
        CharSequence charSequence;
        rectF.setEmpty();
        if (!showsCount() || (charSequence = this.mText) == null || this.mCountTextPaint == null || TextUtils.isEmpty(charSequence.toString()) || TextUtils.isEmpty(getCountText())) {
            return;
        }
        float fMeasureText = this.mTextDrawableHelper.getTextPaint().measureText(getText().toString()) + this.mChipStartPadding + (showsChipIcon() ? calculateChipIconWidth() : 0.0f) + this.mTextStartPadding + this.mCountMarginStart;
        float fMeasureText2 = this.mCountTextPaint.measureText(getCountText().toString());
        if (isRTL()) {
            float f3 = rect.right - fMeasureText;
            rectF.right = f3;
            rectF.left = f3 - fMeasureText2;
        } else {
            float f4 = rect.left + fMeasureText;
            rectF.left = f4;
            rectF.right = f4 + fMeasureText2;
        }
        rectF.top = rect.top;
        rectF.bottom = rect.bottom;
    }

    public float calculateCountWidth() {
        Paint paint;
        if (!showsCount() || (paint = this.mCountTextPaint) == null) {
            return 0.0f;
        }
        return this.mCountMarginStart + paint.measureText(this.mCountText.toString()) + this.mCountMarginEnd;
    }

    public void calculateRedDotBounds(Rect rect, RectF rectF) {
        if (isShowRedDot()) {
            rectF.setEmpty();
            int viewWidth = this.mHintRedDotHelper.getViewWidth(1, 0);
            int viewHeight = this.mHintRedDotHelper.getViewHeight(1);
            if (showsCount()) {
                calculateCountBounds(rect, rectF);
            } else if (!TextUtils.isEmpty(this.mText)) {
                calculateTextBounds(rect, rectF);
            } else if (!showsChipIcon()) {
                return;
            } else {
                calculateChipIconBounds(rect, rectF);
            }
            if (isRTL()) {
                float f3 = rectF.left - this.mRedDotOffsetHorizontal;
                rectF.right = f3;
                rectF.left = f3 - viewWidth;
            } else {
                float f4 = rectF.right + this.mRedDotOffsetHorizontal;
                rectF.left = f4;
                rectF.right = f4 + viewWidth;
            }
            float f5 = this.mRedDotOffsetVertical;
            rectF.top = f5;
            rectF.bottom = f5 + viewHeight;
        }
    }

    public float calculateRedDotWidth() {
        COUIHintRedDotHelper cOUIHintRedDotHelper = this.mHintRedDotHelper;
        if (cOUIHintRedDotHelper == null) {
            return 0.0f;
        }
        return this.mRedDotOffsetHorizontal + cOUIHintRedDotHelper.getViewWidth(1, 0);
    }

    public void calculateTextBounds(Rect rect, RectF rectF) {
        rectF.setEmpty();
        if (this.mText != null) {
            float fMeasureText = this.mTextDrawableHelper.getTextPaint().measureText(getText().toString());
            float fCalculateChipIconWidth = this.mChipStartPadding + (showsChipIcon() ? calculateChipIconWidth() : 0.0f) + this.mTextStartPadding;
            float fCalculateCloseIconWidth = ((showsCloseIcon() || closeIconIsVisibleAndAnimating()) ? calculateCloseIconWidth() : this.mChipEndPadding) + this.mTextEndPadding + (showsCount() ? calculateCountWidth() : 0.0f);
            if (!TextUtils.isEmpty(getText().toString()) && (rect.width() - fCalculateCloseIconWidth) - fCalculateChipIconWidth > fMeasureText) {
                float fWidth = (((rect.width() - fCalculateCloseIconWidth) - fCalculateChipIconWidth) - fMeasureText) / 2.0f;
                fCalculateChipIconWidth += fWidth;
                fCalculateCloseIconWidth += fWidth;
            }
            if (isRTL()) {
                rectF.left = rect.left + fCalculateCloseIconWidth;
                rectF.right = rect.right - fCalculateChipIconWidth;
            } else {
                rectF.left = rect.left + fCalculateChipIconWidth;
                rectF.right = rect.right - fCalculateCloseIconWidth;
            }
            if (DEBUG) {
                Log.d(TAG, "calculateTextBounds outBounds.left = " + rectF.left + ", outBounds.right = " + rectF.right);
            }
            rectF.top = rect.top;
            rectF.bottom = rect.bottom;
        }
    }

    public Paint.Align calculateTextOriginAndAlignment(Rect rect, PointF pointF) {
        Paint.Align align = Paint.Align.LEFT;
        pointF.set(0.0f, 0.0f);
        if (this.mText != null) {
            if (isRTL()) {
                pointF.x = rect.right - this.mTmpBounds.right;
                align = Paint.Align.RIGHT;
            } else {
                pointF.x = rect.left + this.mTmpBounds.left;
            }
            pointF.y = rect.centerY() - calculateTextCenterFromBaseline();
        }
        return align;
    }

    public boolean closeIconIsVisibleAndAnimating() {
        CloseIconAnimation closeIconAnimation = this.mCloseIconAnimation;
        return closeIconAnimation != null && closeIconAnimation.getFraction() > 0.0f;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Canvas canvas2;
        int iSaveLayerAlpha;
        Rect bounds = getBounds();
        if (bounds.isEmpty() || getAlpha() == 0) {
            return;
        }
        int i3 = this.mAlpha;
        if (i3 < 255.0f) {
            canvas2 = canvas;
            iSaveLayerAlpha = CanvasCompat.saveLayerAlpha(canvas2, bounds.left, bounds.top, bounds.right, bounds.bottom, i3);
        } else {
            canvas2 = canvas;
            iSaveLayerAlpha = 0;
        }
        drawChipBackground(canvas2, bounds);
        drawStateEffects(canvas2, bounds);
        drawChipIcon(canvas2, bounds);
        if (this.mShouldDrawText) {
            drawText(canvas2, bounds);
        }
        drawCount(canvas2, bounds);
        drawCloseIcon(canvas2, bounds);
        drawRedDot(canvas2, bounds);
        drawDebug(canvas2, bounds);
        if (this.mAlpha < 255.0f) {
            canvas2.restoreToCount(iSaveLayerAlpha);
        }
    }

    public void drawChipIcon(Canvas canvas, Rect rect) {
        if (showsChipIcon()) {
            calculateChipIconBounds(rect, this.mTmpBounds);
            RectF rectF = this.mTmpBounds;
            float f3 = rectF.left;
            float f4 = rectF.top;
            canvas.translate(f3, f4);
            this.mChipIcon.setBounds(0, 0, (int) this.mTmpBounds.width(), (int) this.mTmpBounds.height());
            if (this.mChipIconApplyTint) {
                this.mChipIcon.setTint(this.mChipIconTintCurrentValue);
            }
            this.mChipIcon.draw(canvas);
            canvas.translate(-f3, -f4);
        }
    }

    public void drawCloseIcon(Canvas canvas, Rect rect) {
        if (showsCloseIcon() || closeIconIsVisibleAndAnimating()) {
            CloseIconAnimation closeIconAnimation = this.mCloseIconAnimation;
            float fraction = closeIconAnimation != null ? closeIconAnimation.getFraction() : 1.0f;
            CloseIconAnimation closeIconAnimation2 = this.mCloseIconAnimation;
            float closeIconScaleFraction = closeIconAnimation2 != null ? closeIconAnimation2.getCloseIconScaleFraction() : 1.0f;
            calculateCloseIconBounds(rect, this.mTmpBounds);
            RectF rectF = this.mTmpBounds;
            float f3 = rectF.left;
            float f4 = rectF.top;
            float fCenterX = ((isRTL() ? this.mChipBackgroundWidthCloseIconVisible - (this.mChipBackgroundCurrentWidth / 2.0f) : this.mChipBackgroundCurrentWidth / 2.0f) + (rectF.centerX() - (this.mChipBackgroundWidthCloseIconVisible / 2.0f))) - f3;
            float fHeight = (int) ((this.mTmpBounds.height() * (1.0f - closeIconScaleFraction)) / 2.0f);
            canvas.translate(f3, f4);
            this.mCloseIcon.setAlpha((int) (fraction * 255.0f));
            this.mCloseIcon.setBounds((int) (fCenterX - ((this.mTmpBounds.width() * closeIconScaleFraction) / 2.0f)), (int) fHeight, (int) (((this.mTmpBounds.width() * closeIconScaleFraction) / 2.0f) + fCenterX), (int) (fHeight + (this.mTmpBounds.height() * closeIconScaleFraction)));
            RectF rectF2 = this.mCloseIconTouchRect;
            float f5 = fCenterX - ((this.mCloseIconTouchBoundsSize * closeIconScaleFraction) / 2.0f);
            float fHeight2 = this.mTmpBounds.height() / 2.0f;
            float f6 = this.mCloseIconTouchBoundsSize;
            rectF2.set(f5, fHeight2 - ((f6 * closeIconScaleFraction) / 2.0f), fCenterX + ((f6 * closeIconScaleFraction) / 2.0f), (this.mTmpBounds.height() / 2.0f) + ((this.mCloseIconTouchBoundsSize * closeIconScaleFraction) / 2.0f));
            COUIMaskEffectDrawable cOUIMaskEffectDrawable = this.mCloseIconEffectDrawable;
            RectF rectF3 = this.mCloseIconTouchRect;
            cOUIMaskEffectDrawable.setMaskRect(rectF3, rectF3.width() / 2.0f, this.mCloseIconTouchRect.height() / 2.0f);
            this.mCloseIconEffectDrawable.draw(canvas);
            this.mCloseIcon.draw(canvas);
            canvas.translate(-f3, -f4);
        }
    }

    public void drawCount(Canvas canvas, Rect rect) {
        if (DEBUG) {
            Log.d(TAG, "drawCount: showsCount() = " + showsCount() + ", mCountTextPaint = " + this.mCountTextPaint);
        }
        if (showsCount() && this.mCountTextPaint != null) {
            calculateCountBounds(rect, this.mTmpBounds);
            this.mCountTextPaint.setLinearText(true);
            this.mCountTextPaint.setSubpixelText(true);
            if (this.mTmpBounds.isEmpty()) {
                return;
            }
            Paint.Align align = Paint.Align.LEFT;
            this.mTmpLocation.set(0.0f, 0.0f);
            if (isRTL()) {
                this.mTmpLocation.x = this.mTmpBounds.right;
                align = Paint.Align.RIGHT;
            } else {
                this.mTmpLocation.x = this.mTmpBounds.left;
            }
            this.mTmpLocation.y = rect.centerY() - calculateTextCenterFromBaseline();
            this.mCountTextPaint.setColor(this.mCountTextColor);
            this.mCountTextPaint.setTextAlign(align);
            CharSequence charSequence = this.mCountText;
            int length = charSequence.length();
            PointF pointF = this.mTmpLocation;
            canvas.drawText(charSequence, 0, length, pointF.x, pointF.y, this.mCountTextPaint);
        }
    }

    public void drawDebug(Canvas canvas, Rect rect) {
        Paint paint = this.mDebugPaint;
        if (paint != null) {
            paint.setColor(ColorUtils.setAlphaComponent(-16777216, 127));
            canvas.drawRect(rect, this.mDebugPaint);
            if (showsChipIcon()) {
                calculateChipIconBounds(rect, this.mTmpBounds);
                canvas.drawRect(this.mTmpBounds, this.mDebugPaint);
            }
            if (showsCloseIcon() || closeIconIsVisibleAndAnimating()) {
                calculateCloseIconBounds(rect, this.mTmpBounds);
                canvas.drawRect(this.mTmpBounds, this.mDebugPaint);
            }
            this.mDebugPaint.setColor(ColorUtils.setAlphaComponent(SupportMenu.CATEGORY_MASK, 127));
            calculateCloseIconTouchBounds(rect, this.mTmpBounds);
            canvas.drawRect(this.mTmpBounds, this.mDebugPaint);
            if (this.mText != null) {
                calculateTextOriginAndAlignment(rect, this.mTmpLocation);
                float f3 = rect.left;
                float f4 = this.mFontMetrics.top;
                float f5 = this.mTmpLocation.y;
                canvas.drawLine(f3, f4 + f5, rect.right, f4 + f5, this.mDebugPaint);
                float f6 = rect.left;
                float f7 = this.mFontMetrics.ascent;
                float f8 = this.mTmpLocation.y;
                canvas.drawLine(f6, f7 + f8, rect.right, f7 + f8, this.mDebugPaint);
                float f9 = rect.left;
                float f10 = this.mFontMetrics.descent;
                float f11 = this.mTmpLocation.y;
                canvas.drawLine(f9, f10 + f11, rect.right, f10 + f11, this.mDebugPaint);
                float f12 = rect.left;
                float f13 = this.mFontMetrics.bottom;
                float f14 = this.mTmpLocation.y;
                canvas.drawLine(f12, f13 + f14, rect.right, f13 + f14, this.mDebugPaint);
                float f15 = rect.left;
                float f16 = this.mFontMetrics.leading;
                float f17 = this.mTmpLocation.y;
                canvas.drawLine(f15, f16 + f17, rect.right, f16 + f17, this.mDebugPaint);
                this.mDebugPaint.setColor(ColorUtils.setAlphaComponent(-16777216, 127));
                canvas.drawLine(rect.left, rect.exactCenterY(), rect.right, rect.exactCenterY(), this.mDebugPaint);
            }
        }
    }

    public void drawRedDot(Canvas canvas, Rect rect) {
        if (!this.mShowRedDot || this.mHintRedDotHelper == null) {
            return;
        }
        calculateRedDotBounds(rect, this.mTmpBounds);
        this.mHintRedDotHelper.drawRedPoint(canvas, 1, 1, this.mTmpBounds);
    }

    public void drawStateEffects(Canvas canvas, Rect rect) {
        float fMin = this.mChipCornerRadius;
        if (fMin < 0.0f) {
            fMin = Math.min(rect.width(), rect.height()) / 2.0f;
        }
        if (this.mChipBackground != null || this.mCloseIcon == null) {
            this.mTmpBounds.set(rect);
        } else if (isRTL()) {
            RectF rectF = this.mTmpBounds;
            int i3 = rect.right;
            rectF.set(i3 - this.mChipBackgroundCurrentWidth, rect.top, i3, rect.bottom);
        } else {
            RectF rectF2 = this.mTmpBounds;
            int i4 = rect.left;
            rectF2.set(i4, rect.top, i4 + this.mChipBackgroundCurrentWidth, rect.bottom);
        }
        this.mMaskEffectDrawable.setMaskRect(this.mTmpBounds, fMin, fMin);
        this.mStrokeDrawable.setStrokeRect(this.mTmpBounds, fMin, fMin);
        this.mMaskEffectDrawable.draw(canvas);
        this.mStrokeDrawable.draw(canvas);
    }

    public void drawText(Canvas canvas, Rect rect) {
        if (this.mText != null) {
            calculateTextBounds(rect, this.mTmpBounds);
            Paint.Align alignCalculateTextOriginAndAlignment = calculateTextOriginAndAlignment(rect, this.mTmpLocation);
            if (this.mTextDrawableHelper.getTextAppearance() != null) {
                this.mTextDrawableHelper.getTextPaint().drawableState = getState();
                this.mTextDrawableHelper.updateTextPaintDrawState(this.mContext);
            }
            this.mTextDrawableHelper.getTextPaint().setTextAlign(alignCalculateTextOriginAndAlignment);
            int iSave = 0;
            boolean z2 = Math.round(getTextWidth()) > Math.round(this.mTmpBounds.width());
            if (z2) {
                iSave = canvas.save();
                canvas.clipRect(this.mTmpBounds);
            }
            CharSequence charSequenceEllipsize = this.mText;
            if (z2 && this.mTruncateAt != null) {
                charSequenceEllipsize = TextUtils.ellipsize(charSequenceEllipsize, this.mTextDrawableHelper.getTextPaint(), this.mTmpBounds.width(), this.mTruncateAt);
            }
            CharSequence charSequence = charSequenceEllipsize;
            canvas.translate(this.mChipTextDrawTranslationX, this.mChipTextDrawTranslationY);
            int length = charSequence.length();
            PointF pointF = this.mTmpLocation;
            canvas.drawText(charSequence, 0, length, pointF.x, pointF.y, this.mTextDrawableHelper.getTextPaint());
            if (z2) {
                canvas.restoreToCount(iSave);
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.mAlpha;
    }

    public int getCheckedBackgroundColor() {
        return this.mCheckedBackgroundColor;
    }

    public int getCheckedChipIconTint() {
        return this.mCheckedChipIconTint;
    }

    public int getCheckedDisabledBackgroundColor() {
        return this.mCheckedDisabledBackgroundColor;
    }

    public int getCheckedDisabledChipIconTint() {
        return this.mCheckedDisabledChipIconTint;
    }

    public int getCheckedDisabledTextColor() {
        return this.mCheckedDisabledTextColor;
    }

    public TextAppearance getCheckedTextAppearance() {
        return this.mCheckedTextAppearance;
    }

    public int getCheckedTextColor() {
        return this.mCheckedTextColor;
    }

    public Drawable getChipBackground() {
        return this.mChipBackground;
    }

    public float getChipCornerRadius() {
        return this.mChipCornerRadius;
    }

    public float getChipEndPadding() {
        return this.mChipEndPadding;
    }

    public Drawable getChipIcon() {
        Drawable drawable = this.mChipIcon;
        if (drawable != null) {
            return DrawableCompat.unwrap(drawable);
        }
        return null;
    }

    public float getChipIconEndPadding() {
        return this.mChipIconEndPadding;
    }

    public float getChipIconSize() {
        return this.mChipIconSize;
    }

    public float getChipIconStartPadding() {
        return this.mChipIconStartPadding;
    }

    public float getChipMinHeight() {
        return this.mChipMinHeight;
    }

    public float getChipStartPadding() {
        return this.mChipStartPadding;
    }

    public void getChipTouchBounds(RectF rectF) {
        calculateChipTouchBounds(getBounds(), rectF);
    }

    public Drawable getCloseIcon() {
        Drawable drawable = this.mCloseIcon;
        if (drawable != null) {
            return DrawableCompat.unwrap(drawable);
        }
        return null;
    }

    public CharSequence getCloseIconContentDescription() {
        return this.mCloseIconContentDescription;
    }

    public float getCloseIconEndPadding() {
        return this.mCloseIconEndPadding;
    }

    public float getCloseIconSize() {
        return this.mCloseIconSize;
    }

    public float getCloseIconStartPadding() {
        return this.mCloseIconStartPadding;
    }

    public int[] getCloseIconState() {
        return this.mCloseIconStateSet;
    }

    public void getCloseIconTouchBounds(RectF rectF) {
        if (showsCloseIcon()) {
            calculateCloseIconTouchBounds(getBounds(), rectF);
            float fWidth = rectF.width();
            float f3 = this.mCloseIconTouchBoundsSize;
            if (fWidth < f3) {
                float fWidth2 = (f3 - rectF.width()) / 2.0f;
                rectF.left -= fWidth2;
                rectF.right += fWidth2;
            }
            float fHeight = rectF.height();
            float f4 = this.mCloseIconTouchBoundsSize;
            if (fHeight < f4) {
                float fHeight2 = (f4 - rectF.height()) / 2.0f;
                rectF.top -= fHeight2;
                rectF.bottom += fHeight2;
            }
        }
    }

    public CharSequence getCountText() {
        return this.mCountText;
    }

    public int getCountTextColor() {
        return this.mCountTextColor;
    }

    public float getCurrentChipBackgroundWidth() {
        return this.mChipBackgroundCurrentWidth;
    }

    public void getDeterminateChipSize(int[] iArr) {
        iArr[0] = Math.min(Math.max(Math.round(this.mChipStartPadding + (showsChipIcon() ? calculateChipIconWidth() : 0.0f) + this.mTextStartPadding + getTextWidth() + this.mTextEndPadding + (showsCount() ? calculateCountWidth() : 0.0f) + (showsCloseIcon() ? calculateCloseIconWidth() : this.mChipEndPadding)), this.mMinWidth), this.mMaxWidth);
        iArr[1] = getIntrinsicHeight();
    }

    public TextUtils.TruncateAt getEllipsize() {
        return this.mTruncateAt;
    }

    public float getEndPaddingForTextCloseIconInvisible() {
        return this.mChipTextPaddingEndCloseIconInvisible;
    }

    public float getEndPaddingForTextCloseIconVisible() {
        return this.mChipTextPaddingEndCloseIconVisible;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return (int) this.mChipMinHeight;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        updateIntrinsicWidth();
        return this.mIntrinsicWidth;
    }

    public int getMaxWidth() {
        return this.mMaxWidth;
    }

    public int getMinWidth() {
        return this.mMinWidth;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    public float getStartPaddingForTextCloseIconInvisible() {
        return this.mChipTextPaddingStartCloseIconInvisible;
    }

    public float getStartPaddingForTextCloseIconVisible() {
        return this.mChipTextPaddingStartCloseIconVisible;
    }

    public CharSequence getText() {
        return this.mText;
    }

    public TextAppearance getTextAppearance() {
        return this.mDefaultTextAppearance;
    }

    public float getTextEndPadding() {
        return this.mTextEndPadding;
    }

    public int getTextMaxWidth() {
        return this.mTextMaxWidth;
    }

    public float getTextSize() {
        return this.mTextDrawableHelper.getTextPaint().getTextSize();
    }

    public float getTextStartPadding() {
        return this.mTextStartPadding;
    }

    public float getTextWidth() {
        Object obj = (COUIChipDrawableDelegate) this.mDelegate.get();
        if (shouldDrawText() || !(obj instanceof TextView)) {
            return Math.min(this.mTextDrawableHelper.getTextWidth(getText().toString()), this.mTextMaxWidth);
        }
        TextView textView = (TextView) obj;
        if (TextUtils.isEmpty(textView.getText())) {
            return 0.0f;
        }
        BoringLayout.Metrics metricsIsBoring = BoringLayout.isBoring(textView.getText(), textView.getPaint());
        return metricsIsBoring != null ? Math.min(metricsIsBoring.width, this.mTextMaxWidth) : Math.min(measureWithStaticLayout(textView), this.mTextMaxWidth);
    }

    public int getUncheckedBackgroundColor() {
        return this.mUncheckedBackgroundColor;
    }

    public int getUncheckedChipIconTint() {
        return this.mUncheckedChipIconTint;
    }

    public int getUncheckedDisabledBackgroundColor() {
        return this.mUncheckedDisabledBackgroundColor;
    }

    public int getUncheckedDisabledChipIconTint() {
        return this.mUncheckedDisabledChipIconTint;
    }

    public int getUncheckedDisabledTextColor() {
        return this.mUncheckedDisabledTextColor;
    }

    public int getUncheckedTextColor() {
        return this.mUncheckedTextColor;
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    public boolean isCheckable() {
        return this.mCheckable;
    }

    public boolean isChecked() {
        return this.mIsChecked;
    }

    public boolean isChipIconStateful() {
        return isStateful(this.mChipIcon);
    }

    public boolean isChipIconVisible() {
        return this.mChipIconVisible;
    }

    public boolean isCloseIconStateful() {
        return true;
    }

    public boolean isCloseIconVisible() {
        return this.mCloseIconVisible;
    }

    public boolean isCountVisible() {
        return this.mCountVisible;
    }

    public boolean isRTL() {
        return DrawableCompat.getLayoutDirection(this) == 1;
    }

    public boolean isShowRedDot() {
        return this.mShowRedDot;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        return true;
    }

    public float measureCountTextWidth(CharSequence charSequence) {
        Paint paint = this.mCountTextPaint;
        if (paint == null) {
            return 0.0f;
        }
        return paint.measureText(charSequence.toString());
    }

    public void onChipTextColorChange(int i3) {
        COUIChipDrawableDelegate cOUIChipDrawableDelegate = this.mDelegate.get();
        if (cOUIChipDrawableDelegate != null) {
            cOUIChipDrawableDelegate.onChipTextColorChange(i3);
        }
        if (this.mShouldDrawText) {
            TextAppearance textAppearance = this.mDefaultTextAppearance;
            if (textAppearance != null) {
                textAppearance.setTextColor(ColorStateList.valueOf(i3));
            }
            TextAppearance textAppearance2 = this.mCheckedTextAppearance;
            if (textAppearance2 != null) {
                textAppearance2.setTextColor(ColorStateList.valueOf(i3));
            }
        }
        invalidateSelf();
    }

    public void onChipTextFontChanged() {
        COUIChipDrawableDelegate cOUIChipDrawableDelegate = this.mDelegate.get();
        if (cOUIChipDrawableDelegate == null || this.mTextDrawableHelper.getTextAppearance() == null) {
            return;
        }
        cOUIChipDrawableDelegate.onChipTextFontChanged(this.mTextDrawableHelper.getTextAppearance().getFont(this.mContext));
    }

    @Override // com.coui.appcompat.state.StatefulDrawableListener
    public void onDrawableUpdate() {
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean onLayoutDirectionChanged(int i3) {
        boolean zOnLayoutDirectionChanged = super.onLayoutDirectionChanged(i3);
        Drawable drawable = this.mChipBackground;
        if (drawable != null) {
            zOnLayoutDirectionChanged |= DrawableCompat.setLayoutDirection(drawable, i3);
        }
        if (showsChipIcon()) {
            zOnLayoutDirectionChanged |= DrawableCompat.setLayoutDirection(this.mChipIcon, i3);
        }
        if (showsCloseIcon()) {
            zOnLayoutDirectionChanged |= DrawableCompat.setLayoutDirection(this.mCloseIcon, i3);
        }
        if (!zOnLayoutDirectionChanged) {
            return true;
        }
        invalidateSelf();
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean onLevelChange(int i3) {
        boolean zOnLevelChange = super.onLevelChange(i3);
        Drawable drawable = this.mChipBackground;
        if (drawable != null) {
            zOnLevelChange |= drawable.setLevel(i3);
        }
        if (showsChipIcon()) {
            zOnLevelChange |= this.mChipIcon.setLevel(i3);
        }
        if (showsCloseIcon()) {
            zOnLevelChange |= this.mCloseIcon.setLevel(i3);
        }
        if (zOnLevelChange) {
            invalidateSelf();
        }
        return zOnLevelChange;
    }

    public void onSizeChange() {
        updateIntrinsicWidth();
        CloseIconAnimation closeIconAnimation = this.mCloseIconAnimation;
        if (closeIconAnimation == null || !closeIconAnimation.getAnimation().isRunning()) {
            this.mChipBackgroundCurrentWidth = -1.0f;
        }
        COUIChipDrawableDelegate cOUIChipDrawableDelegate = this.mDelegate.get();
        if (cOUIChipDrawableDelegate != null) {
            cOUIChipDrawableDelegate.onChipDrawableSizeChange();
        }
    }

    @Override // android.graphics.drawable.Drawable, com.google.android.material.internal.TextDrawableHelper.TextDrawableDelegate
    public boolean onStateChange(int[] iArr) {
        return onStateChange(iArr, getCloseIconState());
    }

    @Override // com.google.android.material.internal.TextDrawableHelper.TextDrawableDelegate
    public void onTextSizeChange() {
        onSizeChange();
        invalidateSelf();
    }

    public void refresh(Context context) {
        int i3;
        int i4;
        this.mContext = context;
        this.mMaskEffectDrawable.refresh(context);
        this.mStrokeDrawable.refresh(context);
        this.mCloseIconEffectDrawable.refresh(context);
        String resourceTypeName = context.getResources().getResourceTypeName(this.mRefreshStyle);
        if (TextUtils.equals(resourceTypeName, "attr")) {
            i3 = this.mRefreshStyle;
            i4 = 0;
        } else if (TextUtils.equals(resourceTypeName, BaseConfig.KEY_STYLE)) {
            i4 = this.mRefreshStyle;
            i3 = 0;
        } else {
            i3 = 0;
            i4 = 0;
        }
        TypedArray typedArrayObtainStyledAttributes = context.getTheme().obtainStyledAttributes(null, R.styleable.COUIChip, i3, i4);
        this.mUncheckedBackgroundColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIChip_uncheckedBackgroundColor, 0);
        this.mCheckedBackgroundColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIChip_checkedBackgroundColor, 0);
        this.mUncheckedDisabledBackgroundColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIChip_uncheckedDisabledBackgroundColor, 0);
        this.mCheckedDisabledBackgroundColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIChip_checkedDisabledBackgroundColor, 0);
        this.mChipIconApplyTint = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIChip_chipIconApplyTint, true);
        this.mUncheckedChipIconTint = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIChip_uncheckedChipIconTint, 0);
        this.mCheckedChipIconTint = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIChip_checkedChipIconTint, 0);
        this.mUncheckedDisabledChipIconTint = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIChip_uncheckedDisabledChipIconTint, 0);
        this.mCheckedDisabledChipIconTint = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIChip_checkedDisabledChipIconTint, 0);
        this.mUncheckedTextColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIChip_uncheckedTextColor, 0);
        this.mCheckedTextColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIChip_checkedTextColor, 0);
        this.mUncheckedDisabledTextColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIChip_uncheckedDisabledTextColor, 0);
        this.mCheckedDisabledTextColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIChip_checkedDisabledTextColor, 0);
        try {
            setCountTextColor(COUIContextUtil.getAttrColor(this.mContext, com.support.appcompat.R.attr.couiColorLabelSecondary));
        } catch (Exception unused) {
        }
        this.mCloseIcon = typedArrayObtainStyledAttributes.getDrawable(R.styleable.COUIChip_closeIcon);
        updateCountTextSize();
        onStateChange(getState());
        invalidateSelf();
        typedArrayObtainStyledAttributes.recycle();
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void scheduleDrawable(Drawable drawable, Runnable runnable, long j3) {
        Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, runnable, j3);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i3) {
        if (this.mAlpha != i3) {
            this.mAlpha = i3;
            invalidateSelf();
        }
    }

    public void setCheckable(boolean z2) {
        if (this.mCheckable != z2) {
            this.mCheckable = z2;
            if (z2 || !this.mIsChecked) {
                return;
            }
            this.mIsChecked = false;
        }
    }

    public void setCheckedBackgroundColor(int i3) {
        if (this.mCheckedBackgroundColor != i3) {
            this.mCheckedBackgroundColor = i3;
            if (this.mIsChecked) {
                TintAnimation tintAnimation = this.mTintAnimation;
                if (tintAnimation == null || !tintAnimation.getAnimation().isRunning()) {
                    this.mBackgroundTintCurrentValue = this.mCheckedBackgroundColor;
                    invalidateSelf();
                }
            }
        }
    }

    public void setCheckedChipIconTint(int i3) {
        if (this.mCheckedChipIconTint != i3) {
            this.mCheckedChipIconTint = i3;
            if (this.mIsChecked) {
                TintAnimation tintAnimation = this.mTintAnimation;
                if (tintAnimation == null || !tintAnimation.getAnimation().isRunning()) {
                    this.mChipIconTintCurrentValue = this.mCheckedChipIconTint;
                    invalidateSelf();
                }
            }
        }
    }

    public void setCheckedDisabledBackgroundColor(int i3) {
        if (this.mCheckedDisabledBackgroundColor != i3) {
            this.mCheckedDisabledBackgroundColor = i3;
            if (this.mIsChecked) {
                TintAnimation tintAnimation = this.mTintAnimation;
                if (tintAnimation == null || !tintAnimation.getAnimation().isRunning()) {
                    this.mBackgroundTintCurrentValue = this.mCheckedDisabledBackgroundColor;
                    invalidateSelf();
                }
            }
        }
    }

    public void setCheckedDisabledChipIconTint(int i3) {
        if (this.mCheckedDisabledChipIconTint != i3) {
            this.mCheckedDisabledChipIconTint = i3;
            if (this.mIsChecked) {
                TintAnimation tintAnimation = this.mTintAnimation;
                if (tintAnimation == null || !tintAnimation.getAnimation().isRunning()) {
                    this.mChipIconTintCurrentValue = this.mCheckedDisabledChipIconTint;
                    invalidateSelf();
                }
            }
        }
    }

    public void setCheckedDisabledTextColor(int i3) {
        if (this.mCheckedDisabledTextColor != i3) {
            this.mCheckedDisabledTextColor = i3;
            if (this.mIsChecked) {
                TintAnimation tintAnimation = this.mTintAnimation;
                if (tintAnimation == null || !tintAnimation.getAnimation().isRunning()) {
                    this.mTextTintCurrentValue = this.mCheckedDisabledTextColor;
                    invalidateSelf();
                }
            }
        }
    }

    public void setCheckedTextAppearance(TextAppearance textAppearance) {
        if (isCheckable() && this.mIsChecked) {
            this.mTextDrawableHelper.setTextAppearance(textAppearance, this.mContext);
        }
        this.mCheckedTextAppearance = textAppearance;
    }

    public void setCheckedTextColor(int i3) {
        if (this.mCheckedTextColor != i3) {
            this.mCheckedTextColor = i3;
            if (this.mIsChecked) {
                TintAnimation tintAnimation = this.mTintAnimation;
                if (tintAnimation == null || !tintAnimation.getAnimation().isRunning()) {
                    this.mTextTintCurrentValue = this.mCheckedTextColor;
                    invalidateSelf();
                }
            }
        }
    }

    public void setChipBackground(Drawable drawable) {
        this.mChipBackground = drawable;
        onStateChange(getState());
    }

    public void setChipCornerRadius(float f3) {
        this.mChipCornerRadius = f3;
        invalidateSelf();
    }

    public void setChipEndPadding(float f3) {
        if (this.mChipEndPadding != f3) {
            this.mChipEndPadding = f3;
            invalidateSelf();
            onSizeChange();
        }
    }

    public void setChipIcon(Drawable drawable) {
        Drawable drawable2 = this.mChipIcon;
        if (drawable2 != drawable) {
            float fCalculateChipIconWidth = calculateChipIconWidth();
            this.mChipIcon = drawable;
            float fCalculateChipIconWidth2 = calculateChipIconWidth();
            unapplyChildDrawable(drawable2);
            applyChildDrawable(this.mChipIcon);
            onStateChange(getState());
            if (fCalculateChipIconWidth != fCalculateChipIconWidth2) {
                onSizeChange();
            }
        }
    }

    public void setChipIconEndPadding(float f3) {
        if (this.mChipIconEndPadding != f3) {
            this.mChipIconEndPadding = f3;
            invalidateSelf();
            onSizeChange();
        }
    }

    public void setChipIconSize(float f3) {
        if (this.mChipIconSize != f3) {
            float fCalculateChipIconWidth = calculateChipIconWidth();
            this.mChipIconSize = f3;
            float fCalculateChipIconWidth2 = calculateChipIconWidth();
            invalidateSelf();
            if (!showsChipIcon() || fCalculateChipIconWidth == fCalculateChipIconWidth2) {
                return;
            }
            onSizeChange();
        }
    }

    public void setChipIconStartPadding(float f3) {
        if (this.mChipIconStartPadding != f3) {
            this.mChipIconStartPadding = f3;
            invalidateSelf();
            onSizeChange();
        }
    }

    public void setChipIconVisible(boolean z2) {
        if (this.mChipIconVisible != z2) {
            boolean zShowsChipIcon = showsChipIcon();
            this.mChipIconVisible = z2;
            boolean zShowsChipIcon2 = showsChipIcon();
            if (zShowsChipIcon != zShowsChipIcon2) {
                if (zShowsChipIcon2) {
                    applyChildDrawable(this.mChipIcon);
                } else {
                    unapplyChildDrawable(this.mChipIcon);
                }
                invalidateSelf();
                onSizeChange();
            }
        }
    }

    public void setChipMinHeight(float f3) {
        if (this.mChipMinHeight != f3) {
            this.mChipMinHeight = f3;
            invalidateSelf();
            onSizeChange();
        }
    }

    public void setChipStartPadding(float f3) {
        if (this.mChipStartPadding != f3) {
            this.mChipStartPadding = f3;
            invalidateSelf();
            onSizeChange();
        }
    }

    public void setCloseIcon(Drawable drawable) {
        Drawable drawable2 = this.mCloseIcon;
        if (drawable2 != drawable) {
            float fCalculateCloseIconWidth = calculateCloseIconWidth();
            this.mCloseIcon = drawable;
            float fCalculateCloseIconWidth2 = calculateCloseIconWidth();
            unapplyChildDrawable(drawable2);
            applyChildDrawable(this.mCloseIcon);
            onStateChange(getState());
            if (fCalculateCloseIconWidth != fCalculateCloseIconWidth2) {
                onSizeChange();
            }
        }
    }

    public void setCloseIconContentDescription(CharSequence charSequence) {
        if (this.mCloseIconContentDescription != charSequence) {
            this.mCloseIconContentDescription = BidiFormatter.getInstance().unicodeWrap(charSequence);
            invalidateSelf();
        }
    }

    public void setCloseIconEndPadding(float f3) {
        if (this.mCloseIconEndPadding != f3) {
            this.mCloseIconEndPadding = f3;
            if (showsCloseIcon() || closeIconIsVisibleAndAnimating()) {
                invalidateSelf();
                onSizeChange();
            }
        }
    }

    public void setCloseIconSize(float f3) {
        if (this.mCloseIconSize != f3) {
            float fCalculateCloseIconWidth = calculateCloseIconWidth();
            this.mCloseIconSize = f3;
            float fCalculateCloseIconWidth2 = calculateCloseIconWidth();
            invalidateSelf();
            if (!showsCloseIcon() || fCalculateCloseIconWidth == fCalculateCloseIconWidth2) {
                return;
            }
            onSizeChange();
        }
    }

    public void setCloseIconStartPadding(float f3) {
        if (this.mCloseIconStartPadding != f3) {
            this.mCloseIconStartPadding = f3;
            if (showsCloseIcon() || closeIconIsVisibleAndAnimating()) {
                invalidateSelf();
                onSizeChange();
            }
        }
    }

    public final boolean setCloseIconState(int[] iArr) {
        if (Arrays.equals(this.mCloseIconStateSet, iArr)) {
            return false;
        }
        this.mCloseIconStateSet = iArr;
        if (showsCloseIcon()) {
            return onStateChange(getState(), iArr);
        }
        return false;
    }

    public void setCloseIconTouched(boolean z2) {
        if (!showsCloseIcon() && !closeIconIsVisibleAndAnimating()) {
            if (closeIconIsVisibleAndAnimating()) {
                return;
            }
            this.mCloseIconEffectDrawable.reset();
        } else if (z2) {
            this.mCloseIconEffectDrawable.setTouchEntered();
        } else {
            this.mCloseIconEffectDrawable.setTouchExited();
        }
    }

    public void setCloseIconVisible(boolean z2) {
        setCloseIconVisible(z2, true);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public void setCountText(CharSequence charSequence) {
        if (charSequence == null) {
            charSequence = "";
        }
        boolean zShowsCount = showsCount();
        if (TextUtils.equals(this.mCountText, charSequence)) {
            return;
        }
        this.mCountText = charSequence;
        boolean zShowsCount2 = showsCount();
        invalidateSelf();
        if (zShowsCount != zShowsCount2) {
            onSizeChange();
        }
    }

    public void setCountTextAppearance(TextAppearance textAppearance) {
        this.mCountTextAppearance = textAppearance;
        updateCountTextSize();
        invalidateSelf();
    }

    public void setCountTextColor(int i3) {
        if (this.mCountTextColor != i3) {
            this.mCountTextColor = i3;
            Paint paint = this.mCountTextPaint;
            if (paint != null) {
                paint.setColor(i3);
            }
            invalidateSelf();
        }
    }

    public void setCountVisible(boolean z2) {
        if (this.mCountVisible != z2) {
            this.mCountVisible = z2;
            invalidateSelf();
            onSizeChange();
        }
    }

    public void setDelegate(COUIChipDrawableDelegate cOUIChipDrawableDelegate) {
        this.mDelegate = new WeakReference<>(cOUIChipDrawableDelegate);
    }

    public void setDescriptionTextAppearance(TextAppearance textAppearance) {
        this.mDescriptionTextAppearance = textAppearance;
        updateCountTextSize();
        invalidateSelf();
    }

    public void setEllipsize(TextUtils.TruncateAt truncateAt) {
        this.mTruncateAt = truncateAt;
    }

    public void setMaxWidth(int i3) {
        this.mMaxWidth = i3;
    }

    public void setMinWidth(int i3) {
        if (this.mMinWidth != i3) {
            this.mMinWidth = i3;
            invalidateSelf();
            onSizeChange();
        }
    }

    public final void setShouldDrawText(boolean z2) {
        if (this.mShouldDrawText != z2) {
            this.mShouldDrawText = z2;
            invalidateSelf();
        }
    }

    public void setShowRedDot(boolean z2) {
        this.mShowRedDot = z2;
        if (z2 && this.mHintRedDotHelper == null) {
            this.mHintRedDotHelper = new COUIHintRedDotHelper(this.mContext, null, com.support.reddot.R.styleable.COUIHintRedDot, 0, com.support.reddot.R.style.Widget_COUI_COUIHintRedDot_Small);
        }
        invalidateSelf();
    }

    public void setText(CharSequence charSequence) {
        if (charSequence == null) {
            charSequence = "";
        }
        if (TextUtils.equals(this.mText, charSequence)) {
            return;
        }
        this.mText = charSequence;
        this.mTextDrawableHelper.setTextWidthDirty(true);
        invalidateSelf();
        onSizeChange();
    }

    public void setTextAppearance(TextAppearance textAppearance) {
        if (!isCheckable() || !this.mIsChecked) {
            this.mTextDrawableHelper.setTextAppearance(textAppearance, this.mContext);
        }
        this.mDefaultTextAppearance = textAppearance;
    }

    public void setTextEndPadding(float f3) {
        if (this.mTextEndPadding != f3) {
            this.mTextEndPadding = f3;
            invalidateSelf();
            onSizeChange();
        }
    }

    public void setTextMaxWidth(int i3) {
        if (this.mTextMaxWidth != i3) {
            this.mTextMaxWidth = i3;
            invalidateSelf();
            onSizeChange();
        }
    }

    public void setTextSize(float f3) {
        TextAppearance textAppearance = this.mDefaultTextAppearance;
        if (textAppearance != null) {
            textAppearance.setTextSize(f3);
        }
        TextAppearance textAppearance2 = this.mCheckedTextAppearance;
        if (textAppearance2 != null) {
            textAppearance2.setTextSize(f3);
        }
        this.mTextDrawableHelper.getTextPaint().setTextSize(f3);
        this.mTextDrawableHelper.setTextWidthDirty(true);
        updateCountTextSize();
        onTextSizeChange();
    }

    public void setTextStartPadding(float f3) {
        if (this.mTextStartPadding != f3) {
            this.mTextStartPadding = f3;
            invalidateSelf();
            onSizeChange();
        }
    }

    public void setTouched(boolean z2) {
        if (z2) {
            this.mMaskEffectDrawable.setTouchEntered();
        } else {
            this.mMaskEffectDrawable.setTouchExited();
        }
    }

    public void setUncheckedBackgroundColor(int i3) {
        if (this.mUncheckedBackgroundColor != i3) {
            this.mUncheckedBackgroundColor = i3;
            if (this.mIsChecked) {
                return;
            }
            TintAnimation tintAnimation = this.mTintAnimation;
            if (tintAnimation == null || !tintAnimation.getAnimation().isRunning()) {
                this.mBackgroundTintCurrentValue = this.mUncheckedBackgroundColor;
                invalidateSelf();
            }
        }
    }

    public void setUncheckedChipIconTint(int i3) {
        if (this.mUncheckedChipIconTint != i3) {
            this.mUncheckedChipIconTint = i3;
            if (this.mIsChecked) {
                return;
            }
            TintAnimation tintAnimation = this.mTintAnimation;
            if (tintAnimation == null || !tintAnimation.getAnimation().isRunning()) {
                this.mChipIconTintCurrentValue = this.mUncheckedChipIconTint;
                invalidateSelf();
            }
        }
    }

    public void setUncheckedDisabledBackgroundColor(int i3) {
        if (this.mUncheckedDisabledBackgroundColor != i3) {
            this.mUncheckedDisabledBackgroundColor = i3;
            if (this.mIsChecked) {
                return;
            }
            TintAnimation tintAnimation = this.mTintAnimation;
            if (tintAnimation == null || !tintAnimation.getAnimation().isRunning()) {
                this.mBackgroundTintCurrentValue = this.mUncheckedDisabledBackgroundColor;
                invalidateSelf();
            }
        }
    }

    public void setUncheckedDisabledChipIconTint(int i3) {
        if (this.mUncheckedDisabledChipIconTint != i3) {
            this.mUncheckedDisabledChipIconTint = i3;
            if (this.mIsChecked) {
                return;
            }
            TintAnimation tintAnimation = this.mTintAnimation;
            if (tintAnimation == null || !tintAnimation.getAnimation().isRunning()) {
                this.mChipIconTintCurrentValue = this.mUncheckedDisabledChipIconTint;
                invalidateSelf();
            }
        }
    }

    public void setUncheckedDisabledTextColor(int i3) {
        if (this.mUncheckedDisabledTextColor != i3) {
            this.mUncheckedDisabledTextColor = i3;
            if (this.mIsChecked) {
                return;
            }
            TintAnimation tintAnimation = this.mTintAnimation;
            if (tintAnimation == null || !tintAnimation.getAnimation().isRunning()) {
                this.mTextTintCurrentValue = this.mUncheckedDisabledTextColor;
                invalidateSelf();
            }
        }
    }

    public void setUncheckedTextColor(int i3) {
        if (this.mUncheckedTextColor != i3) {
            this.mUncheckedTextColor = i3;
            if (this.mIsChecked) {
                return;
            }
            TintAnimation tintAnimation = this.mTintAnimation;
            if (tintAnimation == null || !tintAnimation.getAnimation().isRunning()) {
                this.mTextTintCurrentValue = this.mUncheckedTextColor;
                invalidateSelf();
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setVisible(boolean z2, boolean z3) {
        boolean visible = super.setVisible(z2, z3);
        Drawable drawable = this.mChipBackground;
        if (drawable != null) {
            visible |= drawable.setVisible(z2, z3);
        }
        if (showsChipIcon()) {
            visible |= this.mChipIcon.setVisible(z2, z3);
        }
        if (showsCloseIcon()) {
            visible |= this.mCloseIcon.setVisible(z2, z3);
        }
        if (visible) {
            invalidateSelf();
        }
        return visible;
    }

    public boolean shouldDrawText() {
        return this.mShouldDrawText;
    }

    public final boolean showsChipIcon() {
        return this.mChipIconVisible && this.mChipIcon != null;
    }

    public final boolean showsCloseIcon() {
        return this.mCloseIconVisible && this.mCloseIcon != null;
    }

    public final boolean showsCount() {
        CharSequence charSequence;
        return (!this.mCountVisible || (charSequence = this.mCountText) == null || TextUtils.isEmpty(charSequence.toString())) ? false : true;
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, runnable);
        }
    }

    public void updateChipTextOffsetRange(int i3) {
        float textWidth = getTextWidth();
        float fCalculateCountWidth = showsCount() ? calculateCountWidth() : 0.0f;
        float fCalculateChipIconWidth = (((((this.mChipBackgroundWidthCloseIconInvisible - this.mChipStartPadding) - (showsChipIcon() ? calculateChipIconWidth() : 0.0f)) - this.mTextStartPadding) - this.mTextEndPadding) - this.mChipEndPadding) - fCalculateCountWidth;
        float fCalculateChipIconWidth2 = (((((this.mChipBackgroundWidthCloseIconVisible - this.mChipStartPadding) - (showsChipIcon() ? calculateChipIconWidth() : 0.0f)) - this.mTextStartPadding) - this.mTextEndPadding) - (this.mCloseIcon != null ? (this.mCloseIconStartPadding + this.mCloseIconSize) + this.mCloseIconEndPadding : this.mChipEndPadding)) - fCalculateCountWidth;
        this.mChipTextPaddingStartCloseIconInvisible = this.mChipStartPadding + (showsChipIcon() ? calculateChipIconWidth() : 0.0f) + this.mTextStartPadding + (Math.max(fCalculateChipIconWidth - textWidth, 0.0f) / 2.0f);
        this.mChipTextPaddingStartCloseIconVisible = this.mChipStartPadding + (showsChipIcon() ? calculateChipIconWidth() : 0.0f) + this.mTextStartPadding + (Math.max(fCalculateChipIconWidth2 - textWidth, 0.0f) / 2.0f);
        float f3 = i3 - textWidth;
        this.mChipTextPaddingEndCloseIconInvisible = Math.max(this.mChipEndPadding, f3 - this.mChipTextPaddingStartCloseIconInvisible);
        this.mChipTextPaddingEndCloseIconVisible = Math.max(this.mChipEndPadding, f3 - this.mChipTextPaddingStartCloseIconVisible);
    }

    private static boolean isStateful(Drawable drawable) {
        return drawable != null && drawable.isStateful();
    }

    private boolean onStateChange(int[] iArr, int[] iArr2) {
        TextAppearance textAppearance;
        boolean z2 = false;
        boolean z3 = false;
        for (int i3 : iArr2) {
            if (z2 && z3) {
                break;
            }
            if (i3 == 16843623) {
                z3 = true;
            } else if (i3 == 16842908) {
                z2 = true;
            }
        }
        int[] iArr3 = new int[iArr.length];
        boolean z4 = false;
        boolean z5 = false;
        int i4 = 0;
        for (int i5 : iArr) {
            if (i5 == 16842912) {
                z5 = true;
            } else if (i5 == 16842910) {
                z4 = true;
            }
            if ((!z2 || i5 != 16842908) && (!z3 || i5 != 16843623)) {
                iArr3[i4] = i5;
                i4++;
            }
        }
        super.onStateChange(iArr3);
        if (z4 && isVisible()) {
            if (z5 != this.mIsChecked && this.mTintAnimation == null) {
                TintAnimation tintAnimation = new TintAnimation();
                this.mTintAnimation = tintAnimation;
                tintAnimation.setValue(z5 ? 10000.0f : 0.0f);
            }
            TintAnimation tintAnimation2 = this.mTintAnimation;
            if (tintAnimation2 != null) {
                tintAnimation2.getAnimation().animateToFinalPosition(z5 ? 10000.0f : 0.0f);
            } else {
                this.mBackgroundTintCurrentValue = z5 ? this.mCheckedBackgroundColor : this.mUncheckedBackgroundColor;
                int i6 = z5 ? this.mCheckedTextColor : this.mUncheckedTextColor;
                this.mTextTintCurrentValue = i6;
                this.mChipIconTintCurrentValue = z5 ? this.mCheckedChipIconTint : this.mUncheckedChipIconTint;
                onChipTextColorChange(i6);
            }
        } else {
            TintAnimation tintAnimation3 = this.mTintAnimation;
            if (tintAnimation3 != null) {
                tintAnimation3.getAnimation().cancel();
                this.mTintAnimation.getAnimation().setStartValue(z5 ? 10000.0f : 0.0f);
                this.mTintAnimation.getAnimation().setStartVelocity(0.0f);
            }
            if (z4) {
                this.mBackgroundTintCurrentValue = z5 ? this.mCheckedBackgroundColor : this.mUncheckedBackgroundColor;
                this.mTextTintCurrentValue = z5 ? this.mCheckedTextColor : this.mUncheckedTextColor;
                this.mChipIconTintCurrentValue = z5 ? this.mCheckedChipIconTint : this.mUncheckedChipIconTint;
            } else {
                this.mBackgroundTintCurrentValue = z5 ? this.mCheckedDisabledBackgroundColor : this.mUncheckedDisabledBackgroundColor;
                this.mTextTintCurrentValue = z5 ? this.mCheckedDisabledTextColor : this.mUncheckedDisabledTextColor;
                this.mChipIconTintCurrentValue = z5 ? this.mCheckedDisabledChipIconTint : this.mUncheckedDisabledChipIconTint;
            }
            onChipTextColorChange(this.mTextTintCurrentValue);
        }
        COUIMaskEffectDrawable cOUIMaskEffectDrawable = this.mMaskEffectDrawable;
        if (cOUIMaskEffectDrawable != null) {
            cOUIMaskEffectDrawable.setState(iArr3);
        }
        COUIStrokeDrawable cOUIStrokeDrawable = this.mStrokeDrawable;
        if (cOUIStrokeDrawable != null) {
            cOUIStrokeDrawable.setState(iArr3);
        }
        COUIMaskEffectDrawable cOUIMaskEffectDrawable2 = this.mCloseIconEffectDrawable;
        if (cOUIMaskEffectDrawable2 != null) {
            cOUIMaskEffectDrawable2.setState(iArr2);
        }
        if (this.mChipIconVisible) {
            applyChildDrawable(this.mChipIcon);
        }
        if (this.mCloseIconVisible) {
            applyChildDrawable(this.mCloseIcon);
        }
        if (z5 != this.mIsChecked) {
            if (z5 && (textAppearance = this.mCheckedTextAppearance) != null && textAppearance != this.mTextDrawableHelper.getTextAppearance()) {
                this.mTextDrawableHelper.setTextAppearance(this.mCheckedTextAppearance, this.mContext);
                onChipTextFontChanged();
            } else if (!z5 && this.mDefaultTextAppearance != this.mTextDrawableHelper.getTextAppearance()) {
                this.mTextDrawableHelper.setTextAppearance(this.mDefaultTextAppearance, this.mContext);
                onChipTextFontChanged();
            }
        }
        this.mIsChecked = z5;
        return true;
    }

    public void setCloseIconVisible(boolean z2, boolean z3) {
        if (this.mCloseIconVisible != z2) {
            boolean zShowsCloseIcon = showsCloseIcon();
            this.mCloseIconVisible = z2;
            boolean zShowsCloseIcon2 = showsCloseIcon();
            boolean z4 = zShowsCloseIcon != zShowsCloseIcon2;
            if (z3) {
                if (this.mCloseIconAnimation == null && z2) {
                    this.mCloseIconAnimation = new CloseIconAnimation();
                }
                if (z4) {
                    if (zShowsCloseIcon2) {
                        CloseIconAnimation closeIconAnimation = this.mCloseIconAnimation;
                        if (closeIconAnimation != null) {
                            closeIconAnimation.getAnimation().animateToFinalPosition(10000.0f);
                        } else {
                            this.mChipBackgroundCurrentWidth = this.mChipBackgroundWidthCloseIconVisible;
                        }
                    } else {
                        CloseIconAnimation closeIconAnimation2 = this.mCloseIconAnimation;
                        if (closeIconAnimation2 != null) {
                            closeIconAnimation2.getAnimation().animateToFinalPosition(0.0f);
                        } else {
                            this.mChipBackgroundCurrentWidth = this.mChipBackgroundWidthCloseIconInvisible;
                        }
                    }
                    invalidateSelf();
                    if (this.mChipBackgroundWidthCloseIconVisible != this.mChipBackgroundWidthCloseIconInvisible) {
                        onSizeChange();
                        if (this.mCloseIconVisible && !isRTL() && !equals(this.mChipTextPaddingStartCloseIconInvisible, this.mChipTextPaddingStartCloseIconVisible)) {
                            onChipTextOffsetChanged((int) (this.mChipTextPaddingStartCloseIconInvisible - this.mChipTextPaddingStartCloseIconVisible), 0.0f);
                        } else if (this.mCloseIconVisible && isRTL() && !equals(this.mChipTextPaddingEndCloseIconInvisible, this.mChipTextPaddingEndCloseIconVisible)) {
                            onChipTextOffsetChanged((int) (this.mChipTextPaddingEndCloseIconInvisible - this.mChipTextPaddingEndCloseIconVisible), 0.0f);
                        }
                    }
                }
            } else if (z4) {
                if (zShowsCloseIcon2) {
                    this.mChipBackgroundCurrentWidth = this.mChipBackgroundWidthCloseIconVisible;
                } else {
                    this.mChipBackgroundCurrentWidth = this.mChipBackgroundWidthCloseIconInvisible;
                }
                invalidateSelf();
                if (this.mChipBackgroundWidthCloseIconVisible != this.mChipBackgroundWidthCloseIconInvisible) {
                    onSizeChange();
                }
            }
            if (getCallback() == null) {
                setBounds(0, 0, getIntrinsicWidth(), getIntrinsicHeight());
            }
        }
    }
}