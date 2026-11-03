package com.coui.appcompat.edittext;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.EditText;
import androidx.core.view.GravityCompat;
import com.android.settings.security.SecuritySettings;
import com.coui.appcompat.textutil.COUIChangeTextUtil;
import java.util.ArrayList;
import java.util.Locale;

/* JADX INFO: loaded from: classes7.dex */
public class COUICutoutDrawable extends GradientDrawable {
    private final RectF mCutoutBounds;
    private final Paint mCutoutPaint = new Paint(1);
    private int mSavedLayer;

    public static final class COUICollapseTextHelper {
        private static final boolean DEBUG_DRAW = false;
        public static final int DEFAULT_HINT_LINES = 1;
        public static final int MAX_HINT_LINES = 3;
        private static final float POINT_001 = 0.001f;
        private static final float SCALE_MY = 1.3f;
        private static final String TAG = "COUICollapseTextHelper";
        private boolean mBoundsChanged;
        private final Rect mCollapsedBounds;
        private float mCollapsedDrawX;
        private float mCollapsedDrawY;
        private ColorStateList mCollapsedTextColor;
        private final RectF mCurrentBounds;
        private float mCurrentDrawX;
        private float mCurrentDrawY;
        private float mCurrentTextSize;
        private boolean mDrawTitle;
        private final Rect mExpandedBounds;
        private float mExpandedDrawX;
        private float mExpandedDrawY;
        private float mExpandedFraction;
        private ColorStateList mExpandedTextColor;
        private Bitmap mExpandedTitleTexture;
        private float mHintPaddingStart;
        private boolean mIsRtl;
        private Interpolator mPositionInterpolator;
        private float mScale;
        private int[] mState;
        private CharSequence mText;
        private final TextPaint mTextPaint;
        private Interpolator mTextSizeInterpolator;
        private CharSequence mTextToDraw;
        private float mTextureAscent;
        private float mTextureDescent;
        private Paint mTexturePaint;
        private final TextPaint mTmpPaint;
        private boolean mUseTexture;
        private final View mView;
        private static final boolean USE_SCALING_TEXTURE = false;
        private static final Paint DEBUG_DRAW_PAINT = null;
        private int mExpandedTextGravity = 16;
        private int mCollapsedTextGravity = 16;
        private float mExpandedTextSize = 30.0f;
        private float mCollapsedTextSize = 30.0f;
        private ArrayList<CharSequence> mTextToDrawList = new ArrayList<>();
        private int mHintLines = 1;

        public COUICollapseTextHelper(View view) {
            this.mView = view;
            TextPaint textPaint = new TextPaint(SecuritySettings.UNIFY_LOCK_CONFIRM_PROFILE_REQUEST);
            this.mTextPaint = textPaint;
            this.mTmpPaint = new TextPaint(textPaint);
            this.mCollapsedBounds = new Rect();
            this.mExpandedBounds = new Rect();
            this.mCurrentBounds = new RectF();
        }

        private static int blendColors(int i3, int i4, float f3) {
            float f4 = 1.0f - f3;
            return Color.argb((int) ((Color.alpha(i3) * f4) + (Color.alpha(i4) * f3)), (int) ((Color.red(i3) * f4) + (Color.red(i4) * f3)), (int) ((Color.green(i3) * f4) + (Color.green(i4) * f3)), (int) ((Color.blue(i3) * f4) + (Color.blue(i4) * f3)));
        }

        private void calculateBaseOffsets() {
            int i3;
            float f3 = this.mCurrentTextSize;
            calculateUsingTextSize(this.mCollapsedTextSize);
            float fCalculateTextWidth = calculateTextWidth();
            int absoluteGravity = GravityCompat.getAbsoluteGravity(this.mCollapsedTextGravity, this.mIsRtl ? 1 : 0);
            if (this.mHintLines <= 1) {
                int i4 = absoluteGravity & 112;
                if (i4 != 48) {
                    if (i4 != 80) {
                        this.mCollapsedDrawY = this.mCollapsedBounds.centerY() + (((this.mTextPaint.descent() - this.mTextPaint.ascent()) / 2.0f) - this.mTextPaint.descent());
                    } else {
                        this.mCollapsedDrawY = this.mCollapsedBounds.bottom;
                    }
                } else if (Locale.getDefault().getLanguage().equals("my")) {
                    this.mCollapsedDrawY = this.mCollapsedBounds.top - (this.mTextPaint.ascent() * 1.3f);
                } else {
                    this.mCollapsedDrawY = this.mCollapsedBounds.top - this.mTextPaint.ascent();
                }
            } else if (Locale.getDefault().getLanguage().equals("my")) {
                this.mCollapsedDrawY = this.mCollapsedBounds.top - (this.mTextPaint.ascent() * 1.3f);
            } else {
                this.mCollapsedDrawY = this.mCollapsedBounds.top - this.mTextPaint.ascent();
            }
            int i5 = absoluteGravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
            if (i5 == 1) {
                this.mCollapsedDrawX = this.mCollapsedBounds.centerX() - (fCalculateTextWidth / 2.0f);
            } else if (i5 != 5) {
                this.mCollapsedDrawX = this.mCollapsedBounds.left;
            } else {
                this.mCollapsedDrawX = this.mCollapsedBounds.right - fCalculateTextWidth;
            }
            calculateUsingTextSize(this.mExpandedTextSize);
            float fCalculateTextWidth2 = calculateTextWidth();
            int absoluteGravity2 = GravityCompat.getAbsoluteGravity(this.mExpandedTextGravity, this.mIsRtl ? 1 : 0);
            if (this.mHintLines > 1 || (i3 = absoluteGravity2 & 112) == 48) {
                this.mExpandedDrawY = this.mExpandedBounds.top - this.mTextPaint.ascent();
            } else if (i3 != 80) {
                this.mExpandedDrawY = this.mExpandedBounds.centerY() + (((this.mTextPaint.getFontMetrics().bottom - this.mTextPaint.getFontMetrics().top) / 2.0f) - this.mTextPaint.getFontMetrics().bottom);
            } else {
                this.mExpandedDrawY = this.mExpandedBounds.bottom;
            }
            int i6 = absoluteGravity2 & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
            if (i6 == 1) {
                this.mExpandedDrawX = this.mExpandedBounds.centerX() - (fCalculateTextWidth2 / 2.0f);
            } else if (i6 != 5) {
                this.mExpandedDrawX = this.mExpandedBounds.left;
            } else {
                this.mExpandedDrawX = this.mExpandedBounds.right - fCalculateTextWidth2;
            }
            clearTexture();
            setInterpolatedTextSize(f3);
        }

        private void calculateCurrentOffsets() {
            calculateOffsets(this.mExpandedFraction);
        }

        private boolean calculateIsRtl(CharSequence charSequence) {
            return isRtlMode();
        }

        private void calculateOffsets(float f3) {
            interpolateBounds(f3);
            this.mCurrentDrawX = lerp(this.mExpandedDrawX, this.mCollapsedDrawX, f3, this.mPositionInterpolator);
            this.mCurrentDrawY = lerp(this.mExpandedDrawY, this.mCollapsedDrawY, f3, this.mPositionInterpolator);
            setInterpolatedTextSize(lerp(this.mExpandedTextSize, this.mCollapsedTextSize, f3, this.mTextSizeInterpolator));
            if (this.mCollapsedTextColor != this.mExpandedTextColor) {
                this.mTextPaint.setColor(blendColors(getCurrentExpandedTextColor(), getCurrentCollapsedTextColor(), f3));
            } else {
                this.mTextPaint.setColor(getCurrentCollapsedTextColor());
            }
            this.mView.postInvalidate();
        }

        private float calculateTextWidth() {
            CharSequence charSequence = this.mTextToDraw;
            float fMeasureText = charSequence != null ? this.mTextPaint.measureText(charSequence, 0, charSequence.length()) : 0.0f;
            return (this.mHintLines <= 1 || this.mTextToDraw == null || this.mTextToDrawList.isEmpty()) ? fMeasureText : Math.max(this.mTextPaint.measureText(this.mTextToDrawList.get(0).toString()), fMeasureText);
        }

        private void calculateUsingTextSize(float f3) {
            float f4;
            boolean z2;
            if (this.mText == null) {
                return;
            }
            float fWidth = this.mCollapsedBounds.width();
            float fWidth2 = this.mExpandedBounds.width();
            if (isClose(f3, this.mCollapsedTextSize)) {
                f4 = this.mCollapsedTextSize;
                this.mScale = 1.0f;
            } else {
                float f5 = this.mExpandedTextSize;
                if (isClose(f3, f5)) {
                    this.mScale = 1.0f;
                } else {
                    this.mScale = f3 / this.mExpandedTextSize;
                }
                float f6 = this.mCollapsedTextSize / this.mExpandedTextSize;
                fWidth = fWidth2 * f6 > fWidth ? Math.min(fWidth / f6, fWidth2) : fWidth2;
                f4 = f5;
            }
            if (fWidth > 0.0f) {
                z2 = this.mCurrentTextSize != f4 || this.mBoundsChanged;
                this.mCurrentTextSize = f4;
                this.mBoundsChanged = false;
            } else {
                z2 = false;
            }
            if (this.mTextToDraw == null || z2) {
                this.mTextPaint.setTextSize(this.mCurrentTextSize);
                this.mTextPaint.setLinearText(this.mScale != 1.0f);
                CharSequence charSequenceEllipsize = TextUtils.ellipsize(this.mText, this.mTextPaint, fWidth - this.mHintPaddingStart, TextUtils.TruncateAt.END);
                if (!TextUtils.equals(charSequenceEllipsize, this.mTextToDraw)) {
                    this.mTextToDraw = charSequenceEllipsize;
                }
            }
            this.mIsRtl = isRtlMode();
        }

        private void clearTexture() {
            Bitmap bitmap = this.mExpandedTitleTexture;
            if (bitmap != null) {
                bitmap.recycle();
                this.mExpandedTitleTexture = null;
            }
        }

        private float constrain(float f3, float f4, float f5) {
            if (f3 < f4) {
                return f4;
            }
            return f3 > f5 ? f5 : f3;
        }

        private void ensureExpandedTexture() {
            if (this.mExpandedTitleTexture != null || this.mExpandedBounds.isEmpty() || TextUtils.isEmpty(this.mTextToDraw)) {
                return;
            }
            calculateOffsets(0.0f);
            this.mTextureAscent = this.mTextPaint.ascent();
            this.mTextureDescent = this.mTextPaint.descent();
            TextPaint textPaint = this.mTextPaint;
            CharSequence charSequence = this.mTextToDraw;
            int iRound = Math.round(textPaint.measureText(charSequence, 0, charSequence.length()));
            int iRound2 = Math.round(this.mTextureDescent - this.mTextureAscent);
            if (iRound <= 0 || iRound2 <= 0) {
                return;
            }
            this.mExpandedTitleTexture = Bitmap.createBitmap(iRound, iRound2, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(this.mExpandedTitleTexture);
            CharSequence charSequence2 = this.mTextToDraw;
            canvas.drawText(charSequence2, 0, charSequence2.length(), 0.0f, iRound2 - this.mTextPaint.descent(), this.mTextPaint);
            if (this.mTexturePaint == null) {
                this.mTexturePaint = new Paint(3);
            }
        }

        private int getCurrentExpandedTextColor() {
            int[] iArr = this.mState;
            return iArr != null ? this.mExpandedTextColor.getColorForState(iArr, 0) : this.mExpandedTextColor.getDefaultColor();
        }

        private TextDirectionHeuristic getTextDirectionHeuristic() {
            return isRtlMode() ? TextDirectionHeuristics.RTL : TextDirectionHeuristics.LTR;
        }

        private void getTextPaintCollapsed(TextPaint textPaint) {
            textPaint.setTextSize(this.mCollapsedTextSize);
        }

        private void interpolateBounds(float f3) {
            this.mCurrentBounds.left = lerp(this.mExpandedBounds.left, this.mCollapsedBounds.left, f3, this.mPositionInterpolator);
            this.mCurrentBounds.top = lerp(this.mExpandedDrawY, this.mCollapsedDrawY, f3, this.mPositionInterpolator);
            this.mCurrentBounds.right = lerp(this.mExpandedBounds.right, this.mCollapsedBounds.right, f3, this.mPositionInterpolator);
            this.mCurrentBounds.bottom = lerp(this.mExpandedBounds.bottom, this.mCollapsedBounds.bottom, f3, this.mPositionInterpolator);
        }

        private static boolean isClose(float f3, float f4) {
            return Math.abs(f3 - f4) < 0.001f;
        }

        private boolean isRtlMode() {
            return this.mView.getLayoutDirection() == 1;
        }

        private static float lerp(float f3, float f4, float f5) {
            return f3 + (f5 * (f4 - f3));
        }

        private void onBoundsChanged() {
            this.mDrawTitle = this.mCollapsedBounds.width() > 0 && this.mCollapsedBounds.height() > 0 && this.mExpandedBounds.width() > 0 && this.mExpandedBounds.height() > 0;
        }

        private static boolean rectEquals(Rect rect, int i3, int i4, int i5, int i6) {
            return rect.left == i3 && rect.top == i4 && rect.right == i5 && rect.bottom == i6;
        }

        private void setInterpolatedTextSize(float f3) {
            calculateUsingTextSize(f3);
            boolean z2 = USE_SCALING_TEXTURE && this.mScale != 1.0f;
            this.mUseTexture = z2;
            if (z2) {
                ensureExpandedTexture();
            }
            this.mView.postInvalidate();
        }

        public float calculateCollapsedTextWidth() {
            if (this.mText == null) {
                return 0.0f;
            }
            getTextPaintCollapsed(this.mTmpPaint);
            TextPaint textPaint = this.mTmpPaint;
            CharSequence charSequence = this.mText;
            return textPaint.measureText(charSequence, 0, charSequence.length());
        }

        public void draw(Canvas canvas) {
            float fAscent;
            int iSave = canvas.save();
            float lineSpacingExtra = 0.0f;
            if (this.mTextToDraw == null || !this.mDrawTitle) {
                canvas.drawText(" ", 0.0f, 0.0f, this.mTextPaint);
            } else {
                float f3 = this.mCurrentDrawX;
                float f4 = this.mCurrentDrawY;
                boolean z2 = this.mUseTexture && this.mExpandedTitleTexture != null;
                if (z2) {
                    fAscent = this.mTextureAscent * this.mScale;
                } else {
                    fAscent = this.mTextPaint.ascent() * this.mScale;
                    this.mTextPaint.descent();
                }
                if (z2) {
                    f4 += fAscent;
                }
                float f5 = this.mScale;
                float lineSpacingMultiplier = 1.0f;
                if (f5 != 1.0f) {
                    canvas.scale(f5, f5, f3, f4);
                }
                if (z2) {
                    canvas.drawBitmap(this.mExpandedTitleTexture, f3, f4, this.mTexturePaint);
                } else {
                    View view = this.mView;
                    if (view instanceof EditText) {
                        EditText editText = (EditText) view;
                        lineSpacingExtra = editText.getLineSpacingExtra();
                        lineSpacingMultiplier = editText.getLineSpacingMultiplier();
                    }
                    CharSequence charSequence = this.mHintLines > 1 ? this.mText : this.mTextToDraw;
                    StaticLayout staticLayoutBuild = StaticLayout.Builder.obtain(charSequence, 0, charSequence.length(), this.mTextPaint, (int) this.mCurrentBounds.width()).setAlignment(Layout.Alignment.ALIGN_NORMAL).setIncludePad(true).setEllipsize(TextUtils.TruncateAt.END).setTextDirection(getTextDirectionHeuristic()).setMaxLines(this.mHintLines).setLineSpacing(lineSpacingExtra, lineSpacingMultiplier).build();
                    if (staticLayoutBuild != null) {
                        canvas.save();
                        canvas.translate(this.mIsRtl ? this.mCurrentBounds.left - this.mHintPaddingStart : this.mCurrentBounds.left + this.mHintPaddingStart, f4 - staticLayoutBuild.getLineBaseline(0));
                        staticLayoutBuild.draw(canvas);
                        canvas.restore();
                    }
                }
            }
            canvas.restoreToCount(iSave);
        }

        public Rect getCollapsedBounds() {
            return this.mCollapsedBounds;
        }

        public void getCollapsedTextActualBounds(RectF rectF) {
            boolean zCalculateIsRtl = calculateIsRtl(this.mText);
            float fCalculateCollapsedTextWidth = !zCalculateIsRtl ? this.mCollapsedBounds.left : this.mCollapsedBounds.right - calculateCollapsedTextWidth();
            rectF.left = fCalculateCollapsedTextWidth;
            Rect rect = this.mCollapsedBounds;
            rectF.top = rect.top;
            rectF.right = !zCalculateIsRtl ? fCalculateCollapsedTextWidth + calculateCollapsedTextWidth() : rect.right;
            rectF.bottom = this.mCollapsedBounds.top + getCollapsedTextHeight();
        }

        public ColorStateList getCollapsedTextColor() {
            return this.mCollapsedTextColor;
        }

        public int getCollapsedTextGravity() {
            return this.mCollapsedTextGravity;
        }

        public float getCollapsedTextHeight() {
            getTextPaintCollapsed(this.mTmpPaint);
            return Locale.getDefault().getLanguage().equals("my") ? (-this.mTmpPaint.ascent()) * 1.3f : -this.mTmpPaint.ascent();
        }

        public float getCollapsedTextSize() {
            return this.mCollapsedTextSize;
        }

        public int getCurrentCollapsedTextColor() {
            ColorStateList colorStateList = this.mCollapsedTextColor;
            if (colorStateList == null) {
                return 0;
            }
            int[] iArr = this.mState;
            return iArr != null ? colorStateList.getColorForState(iArr, 0) : colorStateList.getDefaultColor();
        }

        public Rect getExpandedBounds() {
            return this.mExpandedBounds;
        }

        public float getExpandedFraction() {
            return this.mExpandedFraction;
        }

        public ColorStateList getExpandedTextColor() {
            return this.mExpandedTextColor;
        }

        public int getExpandedTextGravity() {
            return this.mExpandedTextGravity;
        }

        public float getExpandedTextSize() {
            return this.mExpandedTextSize;
        }

        public float getExpansionFraction() {
            return this.mExpandedFraction;
        }

        public float getHintHeight() {
            getTextPaintCollapsed(this.mTmpPaint);
            float fDescent = this.mTmpPaint.descent() - this.mTmpPaint.ascent();
            return Locale.getDefault().getLanguage().equals("my") ? fDescent * 1.3f : fDescent;
        }

        public CharSequence getText() {
            return this.mText;
        }

        public final boolean isStateful() {
            ColorStateList colorStateList = this.mCollapsedTextColor;
            if (colorStateList != null && colorStateList.isStateful()) {
                return true;
            }
            ColorStateList colorStateList2 = this.mExpandedTextColor;
            return colorStateList2 != null && colorStateList2.isStateful();
        }

        public void recalculate() {
            if (this.mView.getHeight() <= 0 || this.mView.getWidth() <= 0) {
                return;
            }
            calculateBaseOffsets();
            calculateCurrentOffsets();
        }

        public void setCollapsedBounds(int i3, int i4, int i5, int i6) {
            if (rectEquals(this.mCollapsedBounds, i3, i4, i5, i6)) {
                return;
            }
            this.mCollapsedBounds.set(i3, i4, i5, i6);
            this.mBoundsChanged = true;
            onBoundsChanged();
            Log.d(TAG, "setCollapsedBounds: " + this.mCollapsedBounds);
        }

        public void setCollapsedTextAppearance(int i3, ColorStateList colorStateList) {
            this.mCollapsedTextColor = colorStateList;
            this.mCollapsedTextSize = i3;
            recalculate();
        }

        public void setCollapsedTextColor(ColorStateList colorStateList) {
            if (this.mCollapsedTextColor != colorStateList) {
                this.mCollapsedTextColor = colorStateList;
                recalculate();
            }
        }

        public void setCollapsedTextGravity(int i3) {
            if (this.mCollapsedTextGravity != i3) {
                this.mCollapsedTextGravity = i3;
                recalculate();
            }
        }

        public void setCollapsedTextSize(float f3) {
            if (this.mCollapsedTextSize != f3) {
                this.mCollapsedTextSize = f3;
                recalculate();
            }
        }

        public void setExpandedBounds(int i3, int i4, int i5, int i6) {
            if (rectEquals(this.mExpandedBounds, i3, i4, i5, i6)) {
                return;
            }
            this.mExpandedBounds.set(i3, i4, i5, i6);
            this.mBoundsChanged = true;
            onBoundsChanged();
            Log.d(TAG, "setExpandedBounds: " + this.mExpandedBounds);
        }

        public void setExpandedTextColor(ColorStateList colorStateList) {
            if (this.mExpandedTextColor != colorStateList) {
                this.mExpandedTextColor = colorStateList;
                recalculate();
            }
        }

        public void setExpandedTextGravity(int i3) {
            if (this.mExpandedTextGravity != i3) {
                this.mExpandedTextGravity = i3;
                recalculate();
            }
        }

        public void setExpandedTextSize(float f3) {
            if (this.mExpandedTextSize != f3) {
                this.mExpandedTextSize = f3;
                recalculate();
            }
        }

        public void setExpansionFraction(float f3) {
            float fConstrain = constrain(f3, 0.0f, 1.0f);
            if (fConstrain != this.mExpandedFraction) {
                this.mExpandedFraction = fConstrain;
                calculateCurrentOffsets();
            }
        }

        public void setHintLines(int i3) {
            this.mHintLines = Math.min(3, Math.max(1, i3));
        }

        public void setHintPaddingStart(float f3) {
            if (f3 > 0.0f) {
                this.mHintPaddingStart = f3;
            }
        }

        public void setPositionInterpolator(Interpolator interpolator) {
            this.mPositionInterpolator = interpolator;
            recalculate();
        }

        public final boolean setState(int[] iArr) {
            this.mState = iArr;
            if (!isStateful()) {
                return false;
            }
            recalculate();
            return true;
        }

        public void setText(CharSequence charSequence) {
            if (charSequence == null || !charSequence.equals(this.mText)) {
                this.mText = charSequence;
                this.mTextToDraw = null;
                this.mTextToDrawList.clear();
                clearTexture();
                recalculate();
            }
        }

        public void setTextSizeInterpolator(Interpolator interpolator) {
            this.mTextSizeInterpolator = interpolator;
            recalculate();
        }

        public void setTypefaces(Typeface typeface) {
            COUIChangeTextUtil.adaptBoldAndMediumFont((Paint) this.mTextPaint, true);
            COUIChangeTextUtil.adaptBoldAndMediumFont((Paint) this.mTmpPaint, true);
            recalculate();
        }

        private int constrain(int i3, int i4, int i5) {
            if (i3 < i4) {
                return i4;
            }
            return i3 > i5 ? i5 : i3;
        }

        private static float lerp(float f3, float f4, float f5, Interpolator interpolator) {
            if (interpolator != null) {
                f5 = interpolator.getInterpolation(f5);
            }
            return lerp(f3, f4, f5);
        }
    }

    public COUICutoutDrawable() {
        setPaintStyles();
        this.mCutoutBounds = new RectF();
    }

    private void postDraw(Canvas canvas) {
        if (useHardwareLayer(getCallback())) {
            return;
        }
        canvas.restoreToCount(this.mSavedLayer);
    }

    private void preDraw(Canvas canvas) {
        Drawable.Callback callback = getCallback();
        if (useHardwareLayer(callback)) {
            ((View) callback).setLayerType(2, null);
        } else {
            saveCanvasLayer(canvas);
        }
    }

    private void saveCanvasLayer(Canvas canvas) {
        this.mSavedLayer = canvas.saveLayer(0.0f, 0.0f, canvas.getWidth(), canvas.getHeight(), null);
    }

    private void setPaintStyles() {
        this.mCutoutPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.mCutoutPaint.setColor(-1);
        this.mCutoutPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    private boolean useHardwareLayer(Drawable.Callback callback) {
        return callback instanceof View;
    }

    @Override // android.graphics.drawable.GradientDrawable, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        preDraw(canvas);
        super.draw(canvas);
        canvas.drawRect(this.mCutoutBounds, this.mCutoutPaint);
        postDraw(canvas);
    }

    public RectF getCutout() {
        return this.mCutoutBounds;
    }

    public boolean hasCutout() {
        return !this.mCutoutBounds.isEmpty();
    }

    public void removeCutout() {
        setCutout(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public void setCutout(float f3, float f4, float f5, float f6) {
        RectF rectF = this.mCutoutBounds;
        if (f3 == rectF.left && f4 == rectF.top && f5 == rectF.right && f6 == rectF.bottom) {
            return;
        }
        rectF.set(f3, f4, f5, f6);
        invalidateSelf();
    }

    public void setCutout(RectF rectF) {
        setCutout(rectF.left, rectF.top, rectF.right, rectF.bottom);
    }
}