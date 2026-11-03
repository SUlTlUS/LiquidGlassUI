package com.coui.appcompat.button;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import com.coui.appcompat.button.listener.OnSizeChangeListener;
import com.coui.appcompat.button.listener.OnTextChangeListener;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.coui.appcompat.state.COUIViewStateController;
import com.coui.appcompat.state.PaddingProcessor;
import com.coui.appcompat.state.Processor;
import com.coui.appcompat.state.SizeProcessor;
import com.coui.appcompat.state.TextSizeProcessor;
import com.coui.appcompat.textutil.COUIChangeTextUtil;
import com.coui.appcompat.uiutil.UIUtil;
import com.support.button.R;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes7.dex */
public class SingleButtonWrap extends COUIViewStateController implements OnTextChangeListener, OnSizeChangeListener {
    public static final int BUTTON_MAX_LINE = 2;
    public static final int MULTI_LINE = 2;
    public static final int SINGLE_LINE = 1;
    private COUIButton mCOUIButton;
    private int mSmallPaddingStart;
    private int mType;
    private int mLargeButtonCurrentLines = 0;
    private Runnable setCOUIButtonRequestLayoutRunnable = new AnonymousClass1();

    /* JADX INFO: renamed from: com.coui.appcompat.button.SingleButtonWrap$1, reason: invalid class name */
    public class AnonymousClass1 implements Runnable {
        public AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (SingleButtonWrap.access$000(SingleButtonWrap.this) != null) {
                SingleButtonWrap.access$000(SingleButtonWrap.this).requestLayout();
            }
        }
    }

    @Target({ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
        public static final int DescLarge = 7;
        public static final int DescMedium = 4;
        public static final int Large = 0;
        public static final int Medium = 1;
        public static final int PanelMedium = 3;
        public static final int SingleCentralLarge = 6;
        public static final int Small = 2;
        public static final int TextLarge = 5;
    }

    public SingleButtonWrap(COUIButton cOUIButton, int i3) {
        this.mType = 0;
        this.mSmallPaddingStart = 0;
        if (cOUIButton == null) {
            throw new IllegalArgumentException(getClass().getSimpleName() + ": parameter is null!");
        }
        this.mCOUIButton = cOUIButton;
        cOUIButton.setDrawableRadius(-1);
        this.mCOUIButton.setIncludeFontPadding(false);
        this.mType = i3;
        this.mCOUIButton.setOnSizeChangeListener(this);
        this.mCOUIButton.setOnTextChangeListener(this);
        this.mCOUIButton.setSingleLine(false);
        this.mCOUIButton.setMaxLines(2);
        this.mSmallPaddingStart = this.mCOUIButton.getContext().getResources().getDimensionPixelSize(R.dimen.coui_small_single_btn_padding_horizontal);
        initProcessor();
        setSmallButtonStateChange();
        LimitLargeButtonMaxWidth();
    }

    private void LimitLargeButtonMaxWidth() {
        if (needLimitLargeButtonMaxWidth()) {
            this.mCOUIButton.setNeedLimitMaxWidth(true);
        } else {
            this.mCOUIButton.setNeedLimitMaxWidth(false);
        }
    }

    public static /* synthetic */ void a(SingleButtonWrap singleButtonWrap) {
        super.release();
        singleButtonWrap.initProcessor();
    }

    public static /* synthetic */ COUIButton access$000(SingleButtonWrap singleButtonWrap) {
        return singleButtonWrap.mCOUIButton;
    }

    public static /* synthetic */ void b(SingleButtonWrap singleButtonWrap) {
        COUIButton cOUIButton = singleButtonWrap.mCOUIButton;
        if (cOUIButton == null) {
            return;
        }
        if (singleButtonWrap.getButtonMaxHeight(cOUIButton) <= singleButtonWrap.mCOUIButton.getMeasureMaxHeight() || singleButtonWrap.mCOUIButton.getMeasureMaxHeight() == 0 || !singleButtonWrap.mCOUIButton.isLimitHeight()) {
            singleButtonWrap.mCOUIButton.setSingleLine(false);
            singleButtonWrap.mCOUIButton.setMaxLines(2);
        } else {
            singleButtonWrap.mCOUIButton.setSingleLine(false);
            singleButtonWrap.mCOUIButton.setMaxLines(1);
        }
        singleButtonWrap.mCOUIButton.requestLayout();
    }

    private static int dp2px(Context context, float f3) {
        return Math.round(TypedValue.applyDimension(1, f3, context.getResources().getDisplayMetrics()));
    }

    private static float getAdapterSize(Context context, int i3, int i4) {
        return COUIChangeTextUtil.getSuitableFontSize(i3, context.getResources().getConfiguration().fontScale, i4);
    }

    private int getButtonMaxHeight(COUIButton cOUIButton) {
        if (cOUIButton == null) {
            return 0;
        }
        return (((cOUIButton.getLineHeight() * 2) + cOUIButton.getPaddingTop()) + cOUIButton.getPaddingBottom()) - ((int) (cOUIButton.getLineHeight() - (cOUIButton.getLineHeight() / cOUIButton.getLineSpacingMultiplier())));
    }

    private List<Processor> getCentralLargeProcessor(Context context) {
        ArrayList arrayList = new ArrayList();
        int largeButtonWidth = getLargeButtonWidth(context);
        arrayList.add(new SizeProcessor.Builder(1).setHeight(-2).setWidth(largeButtonWidth).create());
        arrayList.add(new SizeProcessor.Builder(2).setHeight(-2).setWidth(largeButtonWidth).create());
        return arrayList;
    }

    private List<Processor> getDescMediumProcessor(Context context) {
        ArrayList arrayList = new ArrayList();
        int dimensionPixelSize = this.mType == 4 ? context.getResources().getDimensionPixelSize(R.dimen.coui_medium_btn_width) : context.getResources().getDimensionPixelSize(R.dimen.coui_larger_btn_width);
        if (COUIResponsiveUtils.isSmallScreen(context, context.getResources().getDisplayMetrics().widthPixels)) {
            dimensionPixelSize = this.mType == 7 ? getLargeButtonWidth(context) : -1;
        }
        arrayList.add(new SizeProcessor.Builder(1).setHeight(-2).setWidth(dimensionPixelSize).create());
        PaddingProcessor.Builder builder = new PaddingProcessor.Builder(1);
        Resources resources = context.getResources();
        int i3 = com.support.appcompat.R.dimen.coui_btn_desc_padding_vertical;
        PaddingProcessor.Builder paddingTop = builder.setPaddingBottom(resources.getDimensionPixelSize(i3)).setPaddingTop(context.getResources().getDimensionPixelSize(i3));
        Resources resources2 = context.getResources();
        int i4 = R.dimen.coui_btn_padding_horizontal;
        arrayList.add(paddingTop.setPaddingStart(resources2.getDimensionPixelSize(i4)).setPaddingEnd(context.getResources().getDimensionPixelSize(i4)).create());
        TextSizeProcessor.Builder builder2 = new TextSizeProcessor.Builder(1);
        Resources resources3 = context.getResources();
        int i5 = R.dimen.coui_btn_group_text_size;
        arrayList.add(builder2.setTextSize(getAdapterSize(context, resources3.getDimensionPixelSize(i5), 4)).setSizeType(2.0f).create());
        arrayList.add(new SizeProcessor.Builder(2).setHeight(-2).setWidth(dimensionPixelSize).create());
        arrayList.add(new PaddingProcessor.Builder(2).setPaddingBottom(context.getResources().getDimensionPixelSize(i3)).setPaddingTop(context.getResources().getDimensionPixelSize(i3)).setPaddingStart(context.getResources().getDimensionPixelSize(i4)).setPaddingEnd(context.getResources().getDimensionPixelSize(i4)).create());
        arrayList.add(new TextSizeProcessor.Builder(2).setTextSize(getAdapterSize(context, context.getResources().getDimensionPixelSize(i5), 4)).setSizeType(2.0f).create());
        return arrayList;
    }

    private int getLargeButtonWidth(Context context) {
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.coui_larger_btn_width);
        if (!COUIResponsiveUtils.isSmallScreen(context, context.getResources().getDisplayMetrics().widthPixels) || this.mCOUIButton == null) {
            return dimensionPixelSize;
        }
        int dimensionPixelSize2 = context.getResources().getDimensionPixelSize(com.support.appcompat.R.dimen.coui_single_larger_btn_width);
        if (UIUtil.getScreenWidthMetrics(context) >= context.getResources().getDimensionPixelSize(R.dimen.coui_single_larger_window_screen)) {
            return dimensionPixelSize2;
        }
        return -1;
    }

    private List<Processor> getLargeProcessor(Context context) {
        ArrayList arrayList = new ArrayList();
        int largeButtonWidth = getLargeButtonWidth(context);
        arrayList.add(new SizeProcessor.Builder(1).setHeight(-2).setWidth(largeButtonWidth).create());
        arrayList.add(new SizeProcessor.Builder(2).setHeight(-2).setWidth(largeButtonWidth).create());
        PaddingProcessor.Builder builder = new PaddingProcessor.Builder(1);
        Resources resources = context.getResources();
        int i3 = com.support.appcompat.R.dimen.coui_btn_padding_vertical;
        PaddingProcessor.Builder paddingTop = builder.setPaddingBottom(resources.getDimensionPixelSize(i3)).setPaddingTop(context.getResources().getDimensionPixelSize(i3));
        Resources resources2 = context.getResources();
        int i4 = R.dimen.coui_btn_padding_horizontal;
        arrayList.add(paddingTop.setPaddingStart(resources2.getDimensionPixelSize(i4)).setPaddingEnd(context.getResources().getDimensionPixelSize(i4)).create());
        arrayList.add(new PaddingProcessor.Builder(2).setPaddingBottom(context.getResources().getDimensionPixelSize(i3)).setPaddingTop(context.getResources().getDimensionPixelSize(i3)).setPaddingStart(context.getResources().getDimensionPixelSize(i4)).setPaddingEnd(context.getResources().getDimensionPixelSize(i4)).create());
        TextSizeProcessor.Builder builder2 = new TextSizeProcessor.Builder(1);
        Resources resources3 = context.getResources();
        int i5 = R.dimen.coui_btn_group_text_size;
        arrayList.add(builder2.setTextSize(getAdapterSize(context, resources3.getDimensionPixelSize(i5), 4)).setSizeType(2.0f).create());
        arrayList.add(new TextSizeProcessor.Builder(2).setTextSize(getAdapterSize(context, context.getResources().getDimensionPixelSize(i5), 4)).setSizeType(2.0f).create());
        return arrayList;
    }

    private List<Processor> getMediumProcessor(Context context) {
        ArrayList arrayList = new ArrayList();
        SizeProcessor.Builder height = new SizeProcessor.Builder(1).setHeight(-2);
        Resources resources = context.getResources();
        int i3 = R.dimen.coui_medium_btn_width;
        arrayList.add(height.setWidth(resources.getDimensionPixelSize(i3)).create());
        PaddingProcessor.Builder builder = new PaddingProcessor.Builder(1);
        Resources resources2 = context.getResources();
        int i4 = com.support.appcompat.R.dimen.coui_btn_padding_vertical;
        PaddingProcessor.Builder paddingTop = builder.setPaddingBottom(resources2.getDimensionPixelSize(i4)).setPaddingTop(context.getResources().getDimensionPixelSize(i4));
        Resources resources3 = context.getResources();
        int i5 = R.dimen.coui_btn_padding_horizontal;
        arrayList.add(paddingTop.setPaddingStart(resources3.getDimensionPixelSize(i5)).setPaddingEnd(context.getResources().getDimensionPixelSize(i5)).create());
        TextSizeProcessor.Builder builder2 = new TextSizeProcessor.Builder(1);
        Resources resources4 = context.getResources();
        int i6 = R.dimen.coui_btn_group_text_size;
        arrayList.add(builder2.setTextSize(getAdapterSize(context, resources4.getDimensionPixelSize(i6), 4)).setSizeType(2.0f).create());
        arrayList.add(new SizeProcessor.Builder(2).setHeight(-2).setWidth(context.getResources().getDimensionPixelSize(i3)).create());
        arrayList.add(new PaddingProcessor.Builder(2).setPaddingBottom(context.getResources().getDimensionPixelSize(i4)).setPaddingTop(context.getResources().getDimensionPixelSize(i4)).setPaddingStart(context.getResources().getDimensionPixelSize(i5)).setPaddingEnd(context.getResources().getDimensionPixelSize(i5)).create());
        arrayList.add(new TextSizeProcessor.Builder(2).setTextSize(getAdapterSize(context, context.getResources().getDimensionPixelSize(i6), 4)).setSizeType(2.0f).create());
        return arrayList;
    }

    private List<Processor> getPanelMediumProcessor(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new SizeProcessor.Builder(1).setHeight(context.getResources().getDimensionPixelSize(R.dimen.coui_medium_btn_height)).setWidth(0).setWeight(1.0f).create());
        PaddingProcessor.Builder builder = new PaddingProcessor.Builder(1);
        Resources resources = context.getResources();
        int i3 = com.support.appcompat.R.dimen.coui_btn_padding_vertical;
        PaddingProcessor.Builder paddingTop = builder.setPaddingBottom(resources.getDimensionPixelSize(i3)).setPaddingTop(context.getResources().getDimensionPixelSize(i3));
        Resources resources2 = context.getResources();
        int i4 = R.dimen.coui_btn_padding_horizontal;
        arrayList.add(paddingTop.setPaddingStart(resources2.getDimensionPixelSize(i4)).setPaddingEnd(context.getResources().getDimensionPixelSize(i4)).create());
        arrayList.add(new TextSizeProcessor.Builder(1).setTextSize(16.0f).setSizeType(1.0f).create());
        arrayList.add(new SizeProcessor.Builder(2).setHeight(-2).setWidth(0).setWeight(1.0f).create());
        arrayList.add(new PaddingProcessor.Builder(2).setPaddingBottom(context.getResources().getDimensionPixelSize(i3)).setPaddingTop(context.getResources().getDimensionPixelSize(i3)).setPaddingStart(context.getResources().getDimensionPixelSize(i4)).setPaddingEnd(context.getResources().getDimensionPixelSize(i4)).create());
        arrayList.add(new TextSizeProcessor.Builder(2).setTextSize(16.0f).setSizeType(1.0f).create());
        return arrayList;
    }

    private List<Processor> getSmallProcessor(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new SizeProcessor.Builder(1).setHeight(-2).setWidth(-2).create());
        PaddingProcessor.Builder builder = new PaddingProcessor.Builder(1);
        Resources resources = context.getResources();
        int i3 = R.dimen.coui_small_btn_padding_victical;
        PaddingProcessor.Builder paddingTop = builder.setPaddingBottom(resources.getDimensionPixelSize(i3)).setPaddingTop(context.getResources().getDimensionPixelSize(i3));
        Resources resources2 = context.getResources();
        int i4 = R.dimen.coui_small_single_btn_padding_horizontal;
        arrayList.add(paddingTop.setPaddingStart(resources2.getDimensionPixelSize(i4)).setPaddingEnd(context.getResources().getDimensionPixelSize(i4)).create());
        TextSizeProcessor.Builder builder2 = new TextSizeProcessor.Builder(1);
        Resources resources3 = context.getResources();
        int i5 = R.dimen.coui_btn_group_small_single_text_size;
        arrayList.add(builder2.setTextSize(getAdapterSize(context, resources3.getDimensionPixelSize(i5), 2)).setSizeType(2.0f).create());
        arrayList.add(new SizeProcessor.Builder(2).setHeight(-2).setWidth(-2).create());
        PaddingProcessor.Builder paddingTop2 = new PaddingProcessor.Builder(2).setPaddingBottom(context.getResources().getDimensionPixelSize(i3)).setPaddingTop(context.getResources().getDimensionPixelSize(i3));
        Resources resources4 = context.getResources();
        int i6 = R.dimen.coui_small_btn_padding_horizontal;
        arrayList.add(paddingTop2.setPaddingStart(resources4.getDimensionPixelSize(i6)).setPaddingEnd(context.getResources().getDimensionPixelSize(i6)).create());
        arrayList.add(new TextSizeProcessor.Builder(2).setTextSize(getAdapterSize(context, context.getResources().getDimensionPixelSize(i5), 2)).setSizeType(2.0f).create());
        return arrayList;
    }

    private void initProcessor() {
        COUIButton cOUIButton = this.mCOUIButton;
        if (cOUIButton == null) {
            return;
        }
        int i3 = this.mType;
        if (i3 == 0 || i3 == 5) {
            addViewStateProcessor(getLargeProcessor(cOUIButton.getContext()));
        } else if (i3 == 6) {
            addViewStateProcessor(getCentralLargeProcessor(cOUIButton.getContext()));
        } else if (i3 == 1) {
            addViewStateProcessor(getMediumProcessor(cOUIButton.getContext()));
        } else if (i3 == 2) {
            addViewStateProcessor(getSmallProcessor(cOUIButton.getContext()));
        } else if (i3 == 4 || i3 == 7) {
            addViewStateProcessor(getDescMediumProcessor(cOUIButton.getContext()));
            onViewStateChanged(1);
        } else {
            addViewStateProcessor(getPanelMediumProcessor(cOUIButton.getContext()));
        }
        COUIButton cOUIButton2 = this.mCOUIButton;
        cOUIButton2.setText(cOUIButton2.getText());
    }

    private boolean needLimitLargeButtonMaxWidth() {
        int i3;
        COUIButton cOUIButton = this.mCOUIButton;
        return (cOUIButton == null || (i3 = this.mType) == 2 || i3 == 4 || i3 == 1 || !COUIResponsiveUtils.isSmallScreen(cOUIButton.getContext(), this.mCOUIButton.getContext().getResources().getDisplayMetrics().widthPixels)) ? false : true;
    }

    private void reInProcessor() {
        if (needLimitLargeButtonMaxWidth()) {
            if (this.mCOUIButton.getMeasuredWidth() >= this.mCOUIButton.getContext().getResources().getDimensionPixelSize(com.support.appcompat.R.dimen.coui_single_larger_btn_width)) {
                super.release();
                initProcessor();
                this.mCOUIButton.removeCallbacks(this.setCOUIButtonRequestLayoutRunnable);
                this.mCOUIButton.post(this.setCOUIButtonRequestLayoutRunnable);
                return;
            }
            if (this.mCOUIButton.getLineCount() != this.mLargeButtonCurrentLines) {
                onViewStateChanged(this.mCOUIButton.getLineCount());
                this.mLargeButtonCurrentLines = this.mCOUIButton.getLineCount();
            }
        }
    }

    private void setLargeButtonLines(COUIButton cOUIButton, float f3) {
        int dimensionPixelSize = cOUIButton.getContext().getResources().getDimensionPixelSize(R.dimen.coui_larger_btn_width);
        if (COUIResponsiveUtils.isSmallScreen(cOUIButton.getContext(), cOUIButton.getContext().getResources().getDisplayMetrics().widthPixels)) {
            dimensionPixelSize = Math.min(Math.max(dimensionPixelSize, cOUIButton.getMeasuredWidth()), cOUIButton.getContext().getResources().getDimensionPixelSize(com.support.appcompat.R.dimen.coui_single_larger_btn_width));
        }
        if (f3 > dimensionPixelSize - (cOUIButton.getContext().getResources().getDimensionPixelSize(R.dimen.coui_btn_padding_horizontal) * 2)) {
            onViewStateChanged(2);
            this.mLargeButtonCurrentLines = 2;
        } else {
            onViewStateChanged(1);
            this.mLargeButtonCurrentLines = 1;
        }
    }

    private void setMediumButtonLines(COUIButton cOUIButton, float f3) {
        if (f3 > cOUIButton.getContext().getResources().getDimensionPixelSize(R.dimen.coui_medium_btn_width) - (cOUIButton.getContext().getResources().getDimensionPixelSize(R.dimen.coui_btn_padding_horizontal) * 2)) {
            onViewStateChanged(2);
        } else {
            onViewStateChanged(1);
        }
    }

    private void setSmallButtonLines() {
        if (this.mType == 2) {
            this.mCOUIButton.post(new f(this));
        }
    }

    private void setSmallButtonStateChange() {
        if (this.mType == 2) {
            if (this.mCOUIButton.getPaint().measureText(this.mCOUIButton.getText() == null ? "" : this.mCOUIButton.getText().toString()) > this.mCOUIButton.getMeasuredWidth() - (this.mSmallPaddingStart * 2)) {
                onViewStateChanged(2);
                this.mCOUIButton.setDrawableRadius(-1);
            } else {
                onViewStateChanged(1);
                this.mCOUIButton.setDrawableRadius(-1);
            }
            this.mCOUIButton.requestLayout();
        }
    }

    @Override // com.coui.appcompat.state.COUIViewStateController
    public View getProcessView() {
        return this.mCOUIButton;
    }

    @Override // com.coui.appcompat.state.COUIViewStateController, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mCOUIButton.post(new e(this));
        LimitLargeButtonMaxWidth();
    }

    @Override // com.coui.appcompat.button.listener.OnSizeChangeListener
    public void onSizeChanged(View view, int i3, int i4, int i5, int i6) {
        if (view != null) {
            if (i3 == i5 && i4 == i6) {
                return;
            }
            setSmallButtonStateChange();
            setTextButtonPressBackGround(this.mCOUIButton);
            reInProcessor();
        }
    }

    @Override // com.coui.appcompat.button.listener.OnTextChangeListener
    public void onTextChanged(View view, CharSequence charSequence, int i3, int i4, int i5) {
        if (view == null || !(view instanceof COUIButton)) {
            return;
        }
        COUIButton cOUIButton = (COUIButton) view;
        float fMeasureText = cOUIButton.getPaint().measureText(cOUIButton.getText().toString());
        int i6 = this.mType;
        if (i6 == 0 || i6 == 5 || i6 == 6) {
            setLargeButtonLines(cOUIButton, fMeasureText);
        } else if (i6 == 1) {
            setMediumButtonLines(cOUIButton, fMeasureText);
        } else if (i6 == 2) {
            setSmallButtonLines();
        }
    }

    @Override // com.coui.appcompat.state.COUIViewStateController, com.coui.appcompat.state.IViewStateController
    public void release() {
        super.release();
        COUIButton cOUIButton = this.mCOUIButton;
        if (cOUIButton != null) {
            cOUIButton.setOnTextChangeListener(null);
            this.mCOUIButton.setOnSizeChangeListener(null);
            this.mCOUIButton.removeCallbacks(this.setCOUIButtonRequestLayoutRunnable);
            this.mCOUIButton = null;
        }
    }

    public void setTextButtonPressBackGround(COUIButton cOUIButton) {
        if (this.mType == 5) {
            cOUIButton.setAnimType(0);
        }
    }
}