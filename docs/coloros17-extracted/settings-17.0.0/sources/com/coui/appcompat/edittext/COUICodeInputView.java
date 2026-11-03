package com.coui.appcompat.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.roundRect.COUIShapePath;
import com.support.input.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes7.dex */
public class COUICodeInputView extends RelativeLayout {
    private final int CELL_COUNT;
    private final int DEFAULT_SCREEN_WIDTH_DP;
    private List<CodeItemView> codeViews;
    private List<String> codes;
    private int mCellCount;
    private int mCellHeight;
    private int mCellMarginHorizontal;
    private int mCellWidth;
    private LinearLayout mCodeViewsContainer;
    private int mCodeViewsContainerMarginHorizontal;
    private EditText mEditText;
    private boolean mIsEnableSecurity;
    private int mMaxCellMarginHorizontal;
    private int mMinCellMarginHorizontal;
    private UpdateItemWidthRunnable mUpdateItemWidthRunnable;
    private OnInputListener onInputListener;

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUICodeInputView$1, reason: invalid class name */
    public class AnonymousClass1 implements TextWatcher {
        public AnonymousClass1() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            if (editable == null || editable.length() <= 0) {
                return;
            }
            COUICodeInputView.access$100(COUICodeInputView.this).setText("");
            if (COUICodeInputView.access$200(COUICodeInputView.this).size() < COUICodeInputView.access$300(COUICodeInputView.this)) {
                String strTrim = editable.toString().trim();
                if (strTrim.length() > 1) {
                    if (strTrim.length() > COUICodeInputView.access$300(COUICodeInputView.this)) {
                        strTrim = strTrim.substring(0, COUICodeInputView.access$300(COUICodeInputView.this));
                    }
                    COUICodeInputView.access$202(COUICodeInputView.this, new ArrayList(Arrays.asList(strTrim.split(""))));
                } else {
                    COUICodeInputView.access$200(COUICodeInputView.this).add(strTrim);
                }
            }
            COUICodeInputView.access$400(COUICodeInputView.this);
            COUICodeInputView.access$500(COUICodeInputView.this);
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUICodeInputView$2, reason: invalid class name */
    public class AnonymousClass2 implements View.OnKeyListener {
        public AnonymousClass2() {
        }

        @Override // android.view.View.OnKeyListener
        public boolean onKey(View view, int i3, KeyEvent keyEvent) {
            COUICodeInputView cOUICodeInputView = COUICodeInputView.this;
            if (!COUICodeInputView.access$600(cOUICodeInputView, COUICodeInputView.access$200(cOUICodeInputView)) || i3 != 67 || keyEvent.getAction() != 0 || COUICodeInputView.access$200(COUICodeInputView.this).size() <= 0) {
                return false;
            }
            COUICodeInputView.access$200(COUICodeInputView.this).remove(COUICodeInputView.access$200(COUICodeInputView.this).size() - 1);
            COUICodeInputView.access$400(COUICodeInputView.this);
            COUICodeInputView.access$500(COUICodeInputView.this);
            return true;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUICodeInputView$3, reason: invalid class name */
    public class AnonymousClass3 implements View.OnFocusChangeListener {
        public AnonymousClass3() {
        }

        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View view, boolean z2) {
            CodeItemView codeItemView = (CodeItemView) COUICodeInputView.access$700(COUICodeInputView.this).get(Math.min(COUICodeInputView.access$200(COUICodeInputView.this).size(), COUICodeInputView.access$300(COUICodeInputView.this) - 1));
            codeItemView.setIsSelected(z2);
            codeItemView.invalidate();
        }
    }

    public static class CodeItemView extends View {
        private COUICodeInputHelper codeInputHelper;
        private int mCircleColor;
        private Paint mCirclePaint;
        private int mCircleRadius;
        private boolean mIsEnableSecurity;
        private boolean mIsSelected;
        private String mNumber;
        private TextPaint mNumberTextPaint;
        private Paint mPaint;
        private Path mPath;
        private int mRadius;
        private Paint mStrokePaint;
        private int mTextSize;
        private int strokeWidth;

        public CodeItemView(Context context) {
            super(context);
            this.mTextSize = getResources().getDimensionPixelSize(R.dimen.coui_code_input_cell_text_size);
            this.mRadius = COUIContextUtil.getAttrDimens(getContext(), com.support.appcompat.R.attr.couiRoundCornerS);
            this.strokeWidth = getResources().getDimensionPixelSize(R.dimen.coui_code_input_cell_stroke_width);
            this.mCircleRadius = getResources().getDimensionPixelSize(R.dimen.coui_code_input_cell_security_circle_radius);
            this.mCircleColor = COUIContextUtil.getColor(getContext(), R.color.coui_code_input_security_circle_color);
            this.mNumberTextPaint = new TextPaint();
            this.mPaint = new Paint();
            this.mStrokePaint = new Paint();
            this.mCirclePaint = new Paint();
            this.mPath = new Path();
            this.mNumber = "";
            this.mNumberTextPaint.setTextSize(this.mTextSize);
            this.mNumberTextPaint.setAntiAlias(true);
            this.mNumberTextPaint.setColor(COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorPrimaryNeutral));
            this.mPaint.setColor(COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorCardBackground));
            this.mStrokePaint.setColor(COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorPrimary));
            this.mStrokePaint.setStyle(Paint.Style.STROKE);
            this.mStrokePaint.setStrokeWidth(this.strokeWidth);
            this.mCirclePaint.setColor(this.mCircleColor);
            this.mCirclePaint.setAntiAlias(true);
            this.codeInputHelper = new COUICodeInputHelper(this);
        }

        private float getDrawTextStartX(int i3, String str) {
            return (i3 / 2) - (this.mNumberTextPaint.measureText(str) / 2.0f);
        }

        private float getDrawTextStartY(int i3) {
            Paint.FontMetricsInt fontMetricsInt = this.mNumberTextPaint.getFontMetricsInt();
            return (i3 / 2) - ((fontMetricsInt.descent + fontMetricsInt.ascent) / 2);
        }

        @Override // android.view.View
        public void onDraw(Canvas canvas) {
            float drawTextStartX;
            float drawTextStartY;
            int width = getWidth();
            int height = getHeight();
            Path roundRectPath = COUIShapePath.getRoundRectPath(this.mPath, new RectF(0.0f, 0.0f, width, height), this.mRadius);
            this.mPath = roundRectPath;
            canvas.drawPath(roundRectPath, this.mPaint);
            if (this.mIsSelected || this.codeInputHelper.isInboxAnimatorRuning()) {
                int i3 = this.strokeWidth >> 1;
                float f3 = i3;
                RectF rectF = new RectF(f3, f3, width - i3, height - i3);
                this.mStrokePaint.setAlpha((int) (this.codeInputHelper.getCurrentInboxAlpha() * 255.0f));
                Path roundRectPath2 = COUIShapePath.getRoundRectPath(this.mPath, rectF, this.mRadius);
                this.mPath = roundRectPath2;
                canvas.drawPath(roundRectPath2, this.mStrokePaint);
            }
            if (!TextUtils.isEmpty(this.mNumber) || this.codeInputHelper.isNumberAnimatorRuning()) {
                if (this.mIsEnableSecurity) {
                    canvas.drawCircle(width / 2, height / 2, this.mCircleRadius, this.mCirclePaint);
                    return;
                }
                if (!this.codeInputHelper.isNumberAnimatorRuning()) {
                    float drawTextStartX2 = getDrawTextStartX(width, this.mNumber);
                    float drawTextStartY2 = getDrawTextStartY(height);
                    this.mNumberTextPaint.setAlpha(255);
                    canvas.drawText(this.mNumber, drawTextStartX2, drawTextStartY2, this.mNumberTextPaint);
                    return;
                }
                float currentNumberAlpha = this.codeInputHelper.getCurrentNumberAlpha();
                String animatorNumber = this.mNumber;
                this.mNumberTextPaint.setAlpha((int) (currentNumberAlpha * 255.0f));
                if (this.codeInputHelper.isCurrentNumberAppear()) {
                    drawTextStartX = getDrawTextStartX(width, animatorNumber);
                    drawTextStartY = getDrawTextStartY(height);
                    float currentNumberScale = this.codeInputHelper.getCurrentNumberScale();
                    canvas.scale(currentNumberScale, currentNumberScale, drawTextStartX, drawTextStartY);
                } else {
                    animatorNumber = this.codeInputHelper.getAnimatorNumber();
                    drawTextStartX = getDrawTextStartX(width, animatorNumber);
                    drawTextStartY = getDrawTextStartY(height);
                }
                canvas.drawText(animatorNumber, drawTextStartX, drawTextStartY, this.mNumberTextPaint);
            }
        }

        public void setEnableSecurity(boolean z2) {
            this.mIsEnableSecurity = z2;
        }

        public void setIsSelected(boolean z2) {
            if (z2 != this.mIsSelected) {
                this.codeInputHelper.startInboxAnimator(z2);
            }
            this.mIsSelected = z2;
        }

        public void setNumber(String str) {
            if (!this.mIsEnableSecurity) {
                if (!TextUtils.isEmpty(this.mNumber) && TextUtils.isEmpty(str)) {
                    this.codeInputHelper.startNumberAnimator(false, this.mNumber);
                } else if (TextUtils.isEmpty(this.mNumber) && !TextUtils.isEmpty(str)) {
                    this.codeInputHelper.startNumberAnimator(true, str);
                }
            }
            this.mNumber = str;
        }
    }

    public interface OnInputListener {
        void onInput();

        void onSuccess(String str);
    }

    public static class UpdateItemWidthRunnable implements Runnable {
        private View codeViewsContainer;

        private UpdateItemWidthRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            View view = this.codeViewsContainer;
            if (view != null) {
                view.requestLayout();
                this.codeViewsContainer = null;
            }
        }

        public void setCodeViewsContainer(View view) {
            this.codeViewsContainer = view;
        }

        public /* synthetic */ UpdateItemWidthRunnable(AnonymousClass1 anonymousClass1) {
            this();
        }
    }

    public COUICodeInputView(Context context) {
        this(context, null);
    }

    public static /* synthetic */ EditText access$100(COUICodeInputView cOUICodeInputView) {
        return cOUICodeInputView.mEditText;
    }

    public static /* synthetic */ List access$200(COUICodeInputView cOUICodeInputView) {
        return cOUICodeInputView.codes;
    }

    public static /* synthetic */ List access$202(COUICodeInputView cOUICodeInputView, List list) {
        cOUICodeInputView.codes = list;
        return list;
    }

    public static /* synthetic */ int access$300(COUICodeInputView cOUICodeInputView) {
        return cOUICodeInputView.mCellCount;
    }

    public static /* synthetic */ void access$400(COUICodeInputView cOUICodeInputView) {
        cOUICodeInputView.updateViewsByCodesChange();
    }

    public static /* synthetic */ void access$500(COUICodeInputView cOUICodeInputView) {
        cOUICodeInputView.callBack();
    }

    public static /* synthetic */ boolean access$600(COUICodeInputView cOUICodeInputView, List list) {
        return cOUICodeInputView.isNotEmpty(list);
    }

    public static /* synthetic */ List access$700(COUICodeInputView cOUICodeInputView) {
        return cOUICodeInputView.codeViews;
    }

    private void callBack() {
        if (this.onInputListener == null) {
            return;
        }
        if (this.codes.size() == this.mCellCount) {
            this.onInputListener.onSuccess(getPhoneCode());
        } else {
            this.onInputListener.onInput();
        }
    }

    private int getCellMarginHorizontal(int i3, int i4) {
        int iMin = Math.min(Math.max(Math.round(((i3 - (i4 * this.codeViews.size())) - (this.mCodeViewsContainerMarginHorizontal * 2)) / ((this.codeViews.size() * 2) - 2)), this.mMinCellMarginHorizontal), this.mMaxCellMarginHorizontal);
        this.mCellMarginHorizontal = iMin;
        return iMin;
    }

    private void initResource() {
        this.mCellWidth = getResources().getDimensionPixelSize(R.dimen.coui_code_input_cell_width);
        this.mCellMarginHorizontal = getResources().getDimensionPixelSize(R.dimen.coui_code_input_cell_margin_horizontal);
        this.mCellHeight = getResources().getDimensionPixelSize(R.dimen.coui_code_input_cell_height);
        this.mMaxCellMarginHorizontal = getResources().getDimensionPixelSize(R.dimen.coui_code_input_cell_max_margin_horizontal);
        this.mMinCellMarginHorizontal = getResources().getDimensionPixelSize(R.dimen.coui_code_input_cell_min_margin_horizontal);
        this.mCodeViewsContainerMarginHorizontal = getResources().getDimensionPixelSize(R.dimen.coui_code_input_layout_margin_start);
    }

    private void initView(View view) {
        this.mCodeViewsContainer = (LinearLayout) view.findViewById(R.id.code_container_layout);
        for (int i3 = 0; i3 < this.mCellCount; i3++) {
            CodeItemView codeItemView = new CodeItemView(getContext());
            codeItemView.setEnableSecurity(this.mIsEnableSecurity);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(this.mCellWidth, -1);
            layoutParams.setMarginStart(this.mCellMarginHorizontal);
            layoutParams.setMarginEnd(this.mCellMarginHorizontal);
            this.mCodeViewsContainer.addView(codeItemView, layoutParams);
            this.codeViews.add(codeItemView);
        }
        this.codeViews.get(0).setIsSelected(true);
        EditText editText = (EditText) view.findViewById(R.id.code_container_edittext);
        this.mEditText = editText;
        editText.requestFocus();
        this.mEditText.addTextChangedListener(new AnonymousClass1());
        this.mEditText.setOnKeyListener(new AnonymousClass2());
        this.mEditText.setOnFocusChangeListener(new AnonymousClass3());
    }

    private boolean isNotEmpty(List<String> list) {
        return !list.isEmpty();
    }

    private void setCodeItemWidth(int i3) {
        double dMin = Math.min(getResources().getConfiguration().screenWidthDp, 360.0d) / 360.0d;
        int i4 = (int) (((double) this.mCellWidth) * dMin);
        int i5 = (int) (((double) this.mCellHeight) * dMin);
        this.mCellMarginHorizontal = getCellMarginHorizontal(i3, i4);
        for (int i6 = 0; i6 < this.mCodeViewsContainer.getChildCount(); i6++) {
            View childAt = this.mCodeViewsContainer.getChildAt(i6);
            if (childAt != null && (childAt instanceof CodeItemView)) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) childAt.getLayoutParams();
                layoutParams.width = i4;
                layoutParams.height = i5;
                if (i6 == 0) {
                    layoutParams.setMarginStart(0);
                } else {
                    layoutParams.setMarginStart(this.mCellMarginHorizontal);
                }
                if (i6 == this.mCellCount - 1) {
                    layoutParams.setMarginEnd(0);
                } else {
                    layoutParams.setMarginEnd(this.mCellMarginHorizontal);
                }
                childAt.setLayoutParams(layoutParams);
            }
        }
        this.mUpdateItemWidthRunnable.setCodeViewsContainer(this.mCodeViewsContainer);
        post(this.mUpdateItemWidthRunnable);
    }

    private void updateViewsByCodesChange() {
        int size = this.codes.size();
        int i3 = 0;
        while (i3 < this.mCellCount) {
            String str = size > i3 ? this.codes.get(i3) : "";
            CodeItemView codeItemView = this.codeViews.get(i3);
            codeItemView.setNumber(str);
            int i4 = this.mCellCount;
            if (size == i4 && i3 == i4 - 1) {
                codeItemView.setIsSelected(true);
            } else {
                codeItemView.setIsSelected(size == i3);
            }
            codeItemView.invalidate();
            i3++;
        }
    }

    public void clearAll() {
        this.mEditText.setText("");
        this.codes.clear();
        updateViewsByCodesChange();
    }

    public String getPhoneCode() {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = this.codes.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
        }
        return sb.toString();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        UpdateItemWidthRunnable updateItemWidthRunnable = this.mUpdateItemWidthRunnable;
        if (updateItemWidthRunnable != null) {
            removeCallbacks(updateItemWidthRunnable);
        }
    }

    @Override // android.view.View
    public void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        if (i3 != i5) {
            setCodeItemWidth(i3);
        }
    }

    public void setOnInputListener(OnInputListener onInputListener) {
        this.onInputListener = onInputListener;
    }

    public COUICodeInputView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUICodeInputView(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.CELL_COUNT = 6;
        this.DEFAULT_SCREEN_WIDTH_DP = 360;
        this.mIsEnableSecurity = false;
        this.codes = new ArrayList();
        this.codeViews = new ArrayList();
        this.mUpdateItemWidthRunnable = new UpdateItemWidthRunnable(null);
        COUIDarkModeUtil.setForceDarkAllow(this, false);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUICodeInputView, i3, 0);
        this.mCellCount = typedArrayObtainStyledAttributes.getInteger(R.styleable.COUICodeInputView_couiCodeInputCount, 6);
        this.mIsEnableSecurity = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUICodeInputView_couiEnableSecurityInput, false);
        typedArrayObtainStyledAttributes.recycle();
        View viewInflate = LayoutInflater.from(context).inflate(R.layout.coui_phone_code_layout, this);
        initResource();
        initView(viewInflate);
    }
}