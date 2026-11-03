package com.coui.appcompat.button;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import com.coui.appcompat.button.listener.OnSizeChangeListener;
import com.coui.appcompat.button.listener.OnTextChangeListener;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.state.IViewStateController;
import com.coui.appcompat.state.Processor;
import com.coui.appcompat.state.SizeProcessor;
import com.coui.appcompat.textutil.COUIChangeTextUtil;
import com.support.button.R;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes7.dex */
public class SimpleButtonGroupCtrl implements IViewStateController, OnTextChangeListener, OnSizeChangeListener, View.OnLayoutChangeListener {
    public static final int MAX_SIZE = 2;
    public static final int MULTI_LINE = 2;
    public static final int SINGLE_LINE = 1;
    private static final String TAG = "SimpleButtonGroupCtrl";
    private COUIButton mCustomButton;
    private SingleButtonWrap mCustomButtonWarp;
    private LinearLayout mCustomLayout;
    private COUIButton mTextChangeBtn;
    private List<SingleButtonWrap> mSingleButtonWrapList = new LinkedList();
    private int mLineCountIndex = -1;
    private int mCurLineCount = 1;
    private int mType = 1;
    private boolean mParentListenerRegistered = false;
    private int mCachedHorizontalButtonMaxWidth = 0;

    /* JADX INFO: renamed from: com.coui.appcompat.button.SimpleButtonGroupCtrl$1, reason: invalid class name */
    public class AnonymousClass1 implements ViewTreeObserver.OnPreDrawListener {
        final /* synthetic */ COUIButton val$button;
        final /* synthetic */ int val$capturedLineCount;
        final /* synthetic */ int val$capturedLineCountIndex;
        final /* synthetic */ View val$view;

        public AnonymousClass1(View view, int i3, int i4, COUIButton cOUIButton) {
            this.val$view = view;
            this.val$capturedLineCount = i3;
            this.val$capturedLineCountIndex = i4;
            this.val$button = cOUIButton;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            ViewTreeObserver viewTreeObserver = this.val$view.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeOnPreDrawListener(this);
            }
            if (SimpleButtonGroupCtrl.access$000(SimpleButtonGroupCtrl.this) == null || SimpleButtonGroupCtrl.access$000(SimpleButtonGroupCtrl.this).isEmpty()) {
                return true;
            }
            SimpleButtonGroupCtrl.access$102(SimpleButtonGroupCtrl.this, this.val$capturedLineCount);
            SimpleButtonGroupCtrl.access$202(SimpleButtonGroupCtrl.this, this.val$capturedLineCountIndex);
            SimpleButtonGroupCtrl simpleButtonGroupCtrl = SimpleButtonGroupCtrl.this;
            simpleButtonGroupCtrl.onViewStateChanged(SimpleButtonGroupCtrl.access$100(simpleButtonGroupCtrl) > 1 ? 2 : 1);
            SimpleButtonGroupCtrl simpleButtonGroupCtrl2 = SimpleButtonGroupCtrl.this;
            SimpleButtonGroupCtrl.access$300(simpleButtonGroupCtrl2, this.val$button, this.val$capturedLineCountIndex, SimpleButtonGroupCtrl.access$000(simpleButtonGroupCtrl2));
            return false;
        }
    }

    public static /* synthetic */ void a(SimpleButtonGroupCtrl simpleButtonGroupCtrl, int i3, COUIButton cOUIButton, int i4) {
        List<SingleButtonWrap> list = simpleButtonGroupCtrl.mSingleButtonWrapList;
        if (list == null || list.isEmpty()) {
            return;
        }
        simpleButtonGroupCtrl.onViewStateChanged(i3 > 1 ? 2 : 1);
        simpleButtonGroupCtrl.postProcessExtraLongLogic(cOUIButton, i4, simpleButtonGroupCtrl.mSingleButtonWrapList);
    }

    public static /* synthetic */ List access$000(SimpleButtonGroupCtrl simpleButtonGroupCtrl) {
        return simpleButtonGroupCtrl.mSingleButtonWrapList;
    }

    public static /* synthetic */ int access$100(SimpleButtonGroupCtrl simpleButtonGroupCtrl) {
        return simpleButtonGroupCtrl.mCurLineCount;
    }

    public static /* synthetic */ int access$102(SimpleButtonGroupCtrl simpleButtonGroupCtrl, int i3) {
        simpleButtonGroupCtrl.mCurLineCount = i3;
        return i3;
    }

    public static /* synthetic */ int access$202(SimpleButtonGroupCtrl simpleButtonGroupCtrl, int i3) {
        simpleButtonGroupCtrl.mLineCountIndex = i3;
        return i3;
    }

    public static /* synthetic */ void access$300(SimpleButtonGroupCtrl simpleButtonGroupCtrl, COUIButton cOUIButton, int i3, List list) {
        simpleButtonGroupCtrl.postProcessExtraLongLogic(cOUIButton, i3, list);
    }

    private void adjustButtonWidths() {
        Iterator<SingleButtonWrap> it = this.mSingleButtonWrapList.iterator();
        while (it.hasNext()) {
            View processView = it.next().getProcessView();
            if (processView != null) {
                ViewGroup.LayoutParams layoutParams = processView.getLayoutParams();
                if (layoutParams instanceof LinearLayout.LayoutParams) {
                    LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layoutParams;
                    if (layoutParams2.width != -1) {
                        layoutParams2.width = -1;
                        layoutParams2.weight = 0.0f;
                        processView.setLayoutParams(layoutParams2);
                    }
                }
            }
        }
    }

    public static /* synthetic */ void b(SimpleButtonGroupCtrl simpleButtonGroupCtrl, COUIButtonLayout cOUIButtonLayout, int i3) {
        if (i3 != 0) {
            simpleButtonGroupCtrl.getClass();
        } else {
            if (simpleButtonGroupCtrl.mSingleButtonWrapList.isEmpty()) {
                return;
            }
            simpleButtonGroupCtrl.handleVisibilityChange(cOUIButtonLayout);
        }
    }

    private int computeSmallScreenHorizontalSlotWidth(COUIButtonLayout cOUIButtonLayout) {
        int width = cOUIButtonLayout.getWidth();
        if (width <= 0) {
            width = cOUIButtonLayout.getMeasuredWidth();
        }
        if (width <= 0) {
            return 0;
        }
        int paddingStart = (width - (cOUIButtonLayout.getOrientation() == 0 ? cOUIButtonLayout.getPaddingStart() + cOUIButtonLayout.getPaddingEnd() : cOUIButtonLayout.getContext().getResources().getDimensionPixelSize(R.dimen.coui_horizontal_btn_margin) * 2)) - (cOUIButtonLayout.getContext().getResources().getDimensionPixelSize(R.dimen.coui_horizontal_btn_padding) * 2);
        if (paddingStart <= 0) {
            return 0;
        }
        return paddingStart / 2;
    }

    private void ensureParentListenerRegistered(COUIButtonLayout cOUIButtonLayout) {
        if (this.mParentListenerRegistered) {
            return;
        }
        this.mParentListenerRegistered = true;
        cOUIButtonLayout.setOnButtonLayoutVisibilityChangedListener(new c(this, cOUIButtonLayout));
    }

    private int getButtonLineCount(COUIButton cOUIButton) {
        int dimensionPixelSize;
        Context context = cOUIButton.getContext();
        if (TextUtils.isEmpty(cOUIButton.getText())) {
            return 1;
        }
        if (!(cOUIButton.getParent() instanceof COUIButtonLayout)) {
            return cOUIButton.getLineCount();
        }
        if (cOUIButton.isDescType()) {
            return 1;
        }
        COUIButtonLayout cOUIButtonLayout = (COUIButtonLayout) cOUIButton.getParent();
        float fMeasureText = cOUIButton.getPaint().measureText(cOUIButton.getText().toString());
        if (COUIResponsiveUtils.isSmallScreen(context, context.getResources().getDisplayMetrics().widthPixels)) {
            dimensionPixelSize = computeSmallScreenHorizontalSlotWidth(cOUIButtonLayout);
            if (dimensionPixelSize > 0) {
                this.mCachedHorizontalButtonMaxWidth = dimensionPixelSize;
            } else {
                dimensionPixelSize = this.mCachedHorizontalButtonMaxWidth;
                if (dimensionPixelSize <= 0) {
                    dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.coui_medium_btn_width);
                }
            }
        } else {
            dimensionPixelSize = cOUIButtonLayout.getOrientation() == 0 ? context.getResources().getDimensionPixelSize(R.dimen.coui_medium_btn_width) : context.getResources().getDimensionPixelSize(R.dimen.coui_larger_btn_width);
        }
        return (fMeasureText <= ((float) ((dimensionPixelSize - cOUIButton.getPaddingStart()) - cOUIButton.getPaddingEnd())) || dimensionPixelSize == 0) ? 1 : 2;
    }

    private int getButtonMaxHeight(COUIButton cOUIButton, int i3) {
        if (cOUIButton == null) {
            return 0;
        }
        if (cOUIButton.isDescType()) {
            return (int) COUIChangeTextUtil.getDpG2Size(cOUIButton.getContext().getResources().getDimensionPixelSize(com.support.appcompat.R.dimen.coui_btn_desc_height_min), cOUIButton.getContext().getResources().getConfiguration().fontScale);
        }
        return (((cOUIButton.getLineHeight() * i3) + cOUIButton.getPaddingTop()) + cOUIButton.getPaddingBottom()) - ((int) (cOUIButton.getLineHeight() - (cOUIButton.getLineHeight() / cOUIButton.getLineSpacingMultiplier())));
    }

    private View getFirstButton() {
        return this.mSingleButtonWrapList.get(0).getProcessView();
    }

    private void handleExtraLongLogic(int i3, List<SingleButtonWrap> list) {
        SingleButtonWrap singleButtonWrap = list.get(i3);
        if (singleButtonWrap == null) {
            throw new IllegalArgumentException("ButtonGroupStateController: buttonWrap == null");
        }
        COUIButton cOUIButton = (COUIButton) singleButtonWrap.getProcessView();
        if (cOUIButton == null) {
            throw new IllegalArgumentException("ButtonGroupStateController: button == null");
        }
        if (cOUIButton.getParent() instanceof COUIButtonLayout) {
            Context context = cOUIButton.getContext();
            COUIButtonLayout cOUIButtonLayout = (COUIButtonLayout) cOUIButton.getParent();
            int dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.coui_medium_btn_width);
            int dimensionPixelSize2 = context.getResources().getDimensionPixelSize(R.dimen.coui_vertical_btn_margin_top);
            int height = cOUIButton.getHeight();
            boolean zIsSmallScreen = COUIResponsiveUtils.isSmallScreen(context, context.getResources().getDisplayMetrics().widthPixels);
            int dimensionPixelSize3 = context.getResources().getDimensionPixelSize(R.dimen.coui_medium_btn_height);
            boolean z2 = (height * 2) + dimensionPixelSize2 <= cOUIButtonLayout.getMaxHeight() && cOUIButtonLayout.getMaxHeight() != 0;
            int iMax = -2;
            int dimensionPixelSize4 = -1;
            if (this.mCurLineCount == 2 && z2 && !cOUIButtonLayout.isLimitHeight()) {
                cOUIButtonLayout.setOrientation(1);
                if (!zIsSmallScreen) {
                    dimensionPixelSize4 = context.getResources().getDimensionPixelSize(R.dimen.coui_larger_btn_width);
                }
            } else {
                cOUIButtonLayout.setOrientation(0);
                if (zIsSmallScreen || cOUIButtonLayout.isForceSmallScreenWidth()) {
                    dimensionPixelSize = -1;
                }
                int i4 = this.mCurLineCount;
                iMax = i4 == 2 ? Math.max(dimensionPixelSize3, getButtonMaxHeight(cOUIButton, i4)) : -2;
                dimensionPixelSize4 = dimensionPixelSize;
            }
            SizeProcessor sizeProcessorCreate = new SizeProcessor.Builder(this.mCurLineCount).setHeight(iMax).setWidth(dimensionPixelSize4).create();
            int i5 = cOUIButtonLayout.getOrientation() != 1 ? this.mCurLineCount : 1;
            for (int i6 = 0; i6 < list.size(); i6++) {
                List<Processor> list2 = list.get(i6).getProcessorMap().get(i5);
                if (list2 != null) {
                    for (Processor processor : list2) {
                        if (processor instanceof SizeProcessor) {
                            sizeProcessorCreate.process(list.get(i6).getProcessView());
                        } else {
                            processor.process(list.get(i6).getProcessView());
                        }
                    }
                }
            }
            setHorizontalButtonMargin(list);
        }
    }

    private void handleVisibilityChange(COUIButtonLayout cOUIButtonLayout) {
        View firstButton = getFirstButton();
        if (firstButton instanceof COUIButton) {
            if (((COUIButtonLayout) firstButton.getParent()).getOrientation() == 1) {
                adjustButtonWidths();
            }
            this.mTextChangeBtn = (COUIButton) firstButton;
            firstButton.requestLayout();
        }
    }

    private void postProcessExtraLongLogic(COUIButton cOUIButton, int i3, List<SingleButtonWrap> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        handleExtraLongLogic(i3, list);
    }

    private void processSameSize() {
        COUIButton cOUIButton = this.mCustomButton;
        if (cOUIButton == null || this.mCustomLayout == null) {
            return;
        }
        int dimensionPixelSize = cOUIButton.getContext().getResources().getDimensionPixelSize(R.dimen.coui_medium_btn_width);
        int i3 = getButtonLineCount(this.mCustomButton) != 2 ? 1 : 2;
        int buttonMaxHeight = getButtonMaxHeight(this.mCustomButton, i3);
        SingleButtonWrap singleButtonWrap = this.mCustomButtonWarp;
        if (singleButtonWrap != null) {
            singleButtonWrap.onViewStateChanged(i3);
        }
        if (this.mCustomButton.getLayoutParams() instanceof LinearLayout.LayoutParams) {
            boolean zIsSmallScreen = COUIResponsiveUtils.isSmallScreen(this.mCustomButton.getContext(), this.mCustomButton.getContext().getResources().getDisplayMetrics().widthPixels);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mCustomButton.getLayoutParams();
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mCustomLayout.getLayoutParams();
            if (zIsSmallScreen) {
                layoutParams.weight = 1.0f;
                layoutParams2.weight = 1.0f;
                dimensionPixelSize = -1;
            } else {
                layoutParams.weight = 0.0f;
                layoutParams2.weight = 0.0f;
            }
            SizeProcessor sizeProcessorCreate = new SizeProcessor.Builder(i3).setHeight(buttonMaxHeight).setWidth(dimensionPixelSize).create();
            sizeProcessorCreate.process(this.mCustomLayout);
            sizeProcessorCreate.process(this.mCustomButton);
            this.mCustomButton.setLayoutParams(layoutParams);
            this.mCustomLayout.setLayoutParams(layoutParams2);
        }
    }

    private void resetLayoutToHorizontal(Configuration configuration) {
        View processView;
        if (this.mSingleButtonWrapList.isEmpty() || (processView = this.mSingleButtonWrapList.get(0).getProcessView()) == null || !(processView.getParent() instanceof COUIButtonLayout)) {
            return;
        }
        COUIButtonLayout cOUIButtonLayout = (COUIButtonLayout) processView.getParent();
        Context context = processView.getContext();
        cOUIButtonLayout.setOrientation(0);
        this.mCachedHorizontalButtonMaxWidth = 0;
        boolean zIsSmallScreen = COUIResponsiveUtils.isSmallScreen(context, context.getResources().getDisplayMetrics().widthPixels);
        Iterator<SingleButtonWrap> it = this.mSingleButtonWrapList.iterator();
        while (it.hasNext()) {
            View processView2 = it.next().getProcessView();
            if (processView2 != null) {
                ViewGroup.LayoutParams layoutParams = processView2.getLayoutParams();
                if (layoutParams instanceof LinearLayout.LayoutParams) {
                    LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layoutParams;
                    if (zIsSmallScreen) {
                        layoutParams2.width = -1;
                        layoutParams2.weight = 1.0f;
                    } else {
                        layoutParams2.width = context.getResources().getDimensionPixelSize(R.dimen.coui_medium_btn_width);
                        layoutParams2.weight = 0.0f;
                    }
                    processView2.setLayoutParams(layoutParams2);
                }
            }
        }
    }

    private void setHorizontalButtonMargin(List<SingleButtonWrap> list) {
        if (list.size() != 2) {
            return;
        }
        SingleButtonWrap singleButtonWrap = list.get(0);
        int i3 = 1;
        SingleButtonWrap singleButtonWrap2 = list.get(1);
        COUIButton cOUIButton = (COUIButton) singleButtonWrap.getProcessView();
        COUIButton cOUIButton2 = (COUIButton) singleButtonWrap2.getProcessView();
        Context context = cOUIButton.getContext();
        if (cOUIButton.getParent() instanceof COUIButtonLayout) {
            boolean zIsSmallScreen = COUIResponsiveUtils.isSmallScreen(context, context.getResources().getDisplayMetrics().widthPixels);
            COUIButtonLayout cOUIButtonLayout = (COUIButtonLayout) cOUIButton.getParent();
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) cOUIButton.getLayoutParams();
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) cOUIButton2.getLayoutParams();
            cOUIButtonLayout.removeAllViews();
            if (cOUIButtonLayout.getOrientation() != 0) {
                layoutParams.setMarginEnd(0);
                layoutParams.setMarginStart(0);
                layoutParams.topMargin = context.getResources().getDimensionPixelSize(R.dimen.coui_vertical_btn_margin_top);
                float f3 = 0;
                layoutParams.weight = f3;
                cOUIButton.setLayoutParams(layoutParams);
                layoutParams2.setMarginEnd(0);
                layoutParams2.setMarginStart(0);
                layoutParams2.topMargin = 0;
                layoutParams2.weight = f3;
                cOUIButton2.setLayoutParams(layoutParams2);
                cOUIButtonLayout.addView(cOUIButton2);
                cOUIButtonLayout.addView(cOUIButton);
                return;
            }
            if (!zIsSmallScreen && !cOUIButtonLayout.isForceSmallScreenWidth()) {
                i3 = 0;
            }
            Resources resources = context.getResources();
            int i4 = R.dimen.coui_horizontal_btn_padding;
            layoutParams.setMarginEnd(resources.getDimensionPixelSize(i4));
            layoutParams.setMarginStart(0);
            layoutParams.topMargin = 0;
            float f4 = i3;
            layoutParams.weight = f4;
            cOUIButton.setLayoutParams(layoutParams);
            layoutParams2.setMarginEnd(0);
            layoutParams2.setMarginStart(context.getResources().getDimensionPixelSize(i4));
            layoutParams2.topMargin = 0;
            layoutParams2.weight = f4;
            cOUIButton2.setLayoutParams(layoutParams2);
            cOUIButtonLayout.addView(cOUIButton);
            cOUIButtonLayout.addView(cOUIButton2);
        }
    }

    public int getSingleButtonWrapListSize() {
        return this.mSingleButtonWrapList.size();
    }

    public int getType() {
        return this.mType;
    }

    public void onConfigurationChanged(Configuration configuration) {
        resetLayoutToHorizontal(configuration);
        Iterator<SingleButtonWrap> it = this.mSingleButtonWrapList.iterator();
        while (it.hasNext()) {
            it.next().onConfigurationChanged(configuration);
        }
        SingleButtonWrap singleButtonWrap = this.mCustomButtonWarp;
        if (singleButtonWrap != null) {
            singleButtonWrap.onConfigurationChanged(configuration);
            processSameSize();
        }
    }

    @Override // android.view.View.OnLayoutChangeListener
    public void onLayoutChange(View view, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        if ((view instanceof COUIButton) && view == this.mTextChangeBtn) {
            this.mTextChangeBtn = null;
            if (!this.mParentListenerRegistered && (view.getParent() instanceof COUIButtonLayout)) {
                ensureParentListenerRegistered((COUIButtonLayout) view.getParent());
            }
            COUIButton cOUIButton = (COUIButton) view;
            int buttonLineCount = getButtonLineCount(cOUIButton);
            int i11 = -1;
            int i12 = -1;
            for (int i13 = 0; i13 < this.mSingleButtonWrapList.size(); i13++) {
                COUIButton cOUIButton2 = (COUIButton) this.mSingleButtonWrapList.get(i13).getProcessView();
                int buttonLineCount2 = getButtonLineCount(cOUIButton2);
                if (cOUIButton2 == view) {
                    this.mLineCountIndex = i13;
                } else if (buttonLineCount2 > i11) {
                    i12 = i13;
                    i11 = buttonLineCount2;
                }
            }
            if (buttonLineCount > i11) {
                this.mCurLineCount = buttonLineCount;
            } else {
                this.mCurLineCount = i11;
                this.mLineCountIndex = i12;
            }
            int i14 = this.mCurLineCount;
            int i15 = this.mLineCountIndex;
            ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnPreDrawListener(new AnonymousClass1(view, i14, i15, cOUIButton));
            } else {
                cOUIButton.post(new d(this, i14, cOUIButton, i15));
            }
        }
    }

    @Override // com.coui.appcompat.button.listener.OnSizeChangeListener
    public void onSizeChanged(View view, int i3, int i4, int i5, int i6) {
        processSameSize();
    }

    @Override // com.coui.appcompat.button.listener.OnTextChangeListener
    public void onTextChanged(View view, CharSequence charSequence, int i3, int i4, int i5) {
        this.mTextChangeBtn = (COUIButton) view;
        view.requestLayout();
    }

    @Override // com.coui.appcompat.state.IViewStateController
    public void onViewStateChanged(int i3) {
        int i4 = this.mLineCountIndex;
        if (i4 < 0) {
            COUILog.e(TAG, "The mLineCountIndex cannot be less than zero");
            return;
        }
        SingleButtonWrap singleButtonWrap = this.mSingleButtonWrapList.get(i4);
        if (singleButtonWrap != null) {
            singleButtonWrap.onViewStateChanged(i3);
        }
        SingleButtonWrap singleButtonWrap2 = this.mCustomButtonWarp;
        if (singleButtonWrap2 != null) {
            singleButtonWrap2.onViewStateChanged(i3);
        }
    }

    public void registerButton(COUIButton cOUIButton) {
        registerButton(cOUIButton, 1);
    }

    public void registerButtonAndCustomView(COUIButton cOUIButton, LinearLayout linearLayout, int i3) {
        this.mCustomButtonWarp = new SingleButtonWrap(cOUIButton, i3);
        this.mCustomButton = cOUIButton;
        this.mCustomLayout = linearLayout;
        cOUIButton.setOnSizeChangeListener(this);
        cOUIButton.setText(cOUIButton.getText());
        processSameSize();
    }

    @Override // com.coui.appcompat.state.IViewStateController
    public void release() {
        View processView;
        if (this.mParentListenerRegistered && !this.mSingleButtonWrapList.isEmpty() && (processView = this.mSingleButtonWrapList.get(0).getProcessView()) != null && (processView.getParent() instanceof COUIButtonLayout)) {
            ((COUIButtonLayout) processView.getParent()).setOnButtonLayoutVisibilityChangedListener(null);
        }
        this.mParentListenerRegistered = false;
        for (SingleButtonWrap singleButtonWrap : this.mSingleButtonWrapList) {
            singleButtonWrap.getProcessView().removeOnLayoutChangeListener(this);
            singleButtonWrap.release();
        }
        this.mSingleButtonWrapList.clear();
        this.mTextChangeBtn = null;
        SingleButtonWrap singleButtonWrap2 = this.mCustomButtonWarp;
        if (singleButtonWrap2 != null) {
            singleButtonWrap2.release();
        }
    }

    public void setType(int i3) {
        this.mType = i3;
    }

    public void unregisterButton(COUIButton cOUIButton) {
        if (cOUIButton == null || this.mSingleButtonWrapList.size() == 0) {
            return;
        }
        Iterator<SingleButtonWrap> it = this.mSingleButtonWrapList.iterator();
        while (it.hasNext()) {
            SingleButtonWrap next = it.next();
            if (next.getProcessView() == cOUIButton) {
                next.release();
                it.remove();
            }
        }
    }

    public void registerButton(COUIButton cOUIButton, int i3) {
        if (this.mSingleButtonWrapList.size() >= 2) {
            return;
        }
        setType(i3);
        this.mSingleButtonWrapList.add(new SingleButtonWrap(cOUIButton, i3));
        cOUIButton.setOnTextChangeListener(this);
        cOUIButton.addOnLayoutChangeListener(this);
        cOUIButton.setText(cOUIButton.getText());
    }
}