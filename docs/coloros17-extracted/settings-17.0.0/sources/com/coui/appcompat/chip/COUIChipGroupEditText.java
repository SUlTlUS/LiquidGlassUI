package com.coui.appcompat.chip;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.TextView;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.edittext.COUIEditText;
import com.support.appcompat.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIChipGroupEditText extends COUIEditText implements COUIChipGroup.IChipGroupAnimator {
    private static final int MIN_WIDTH = 10;
    private final COUIChipGroup.IChipGroupAnimator mChipGroupAnimator;
    private float mTextLength;

    public static class ChipGroupAnimatorImpl implements COUIChipGroup.IChipGroupAnimator {
        private COUIChipGroup mController;
        private COUISpringAnimation mHorizontalAnimation;
        private final COUIChipGroupEditText mHost;
        private final RectF mDrawingBounds = new RectF();
        private final RectF mRealBounds = new RectF();

        /* JADX INFO: renamed from: com.coui.appcompat.chip.COUIChipGroupEditText$ChipGroupAnimatorImpl$1, reason: invalid class name */
        public class AnonymousClass1 extends FloatPropertyCompat<ChipGroupAnimatorImpl> {
            public AnonymousClass1(String str) {
                super(str);
            }

            @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
            public /* bridge */ /* synthetic */ float getValue(ChipGroupAnimatorImpl chipGroupAnimatorImpl) {
                return getValue2(chipGroupAnimatorImpl);
            }

            @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
            public /* bridge */ /* synthetic */ void setValue(ChipGroupAnimatorImpl chipGroupAnimatorImpl, float f3) {
                setValue2(chipGroupAnimatorImpl, f3);
            }

            /* JADX INFO: renamed from: getValue, reason: avoid collision after fix types in other method */
            public float getValue2(ChipGroupAnimatorImpl chipGroupAnimatorImpl) {
                return chipGroupAnimatorImpl.getX();
            }

            /* JADX INFO: renamed from: setValue, reason: avoid collision after fix types in other method */
            public void setValue2(ChipGroupAnimatorImpl chipGroupAnimatorImpl, float f3) {
                chipGroupAnimatorImpl.setX(f3);
            }
        }

        public ChipGroupAnimatorImpl(COUIChipGroupEditText cOUIChipGroupEditText) {
            this.mHost = cOUIChipGroupEditText;
            initAnimation();
        }

        private void initAnimation() {
            AnonymousClass1 anonymousClass1 = new AnonymousClass1("ChipGroupAnimatorImpl");
            COUISpringForce cOUISpringForce = new COUISpringForce();
            cOUISpringForce.setResponse(0.3f);
            cOUISpringForce.setBounce(0.0f);
            COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(this, anonymousClass1);
            this.mHorizontalAnimation = cOUISpringAnimation;
            cOUISpringAnimation.setSpring(cOUISpringForce);
        }

        /* JADX WARN: Code duplicated, block: B:26:0x006f  */
        private void updateRealBounds(int i3, int i4, int i5, int i6, boolean z2) {
            RectF rectF = this.mRealBounds;
            float f3 = i3;
            if (rectF.left == f3 && rectF.top == i4 && rectF.right == i5 && rectF.bottom == i6) {
                return;
            }
            Editable editableText = this.mHost.getEditableText();
            if (editableText != null) {
                this.mHost.getPaint().measureText(editableText.toString());
            }
            if (!z2) {
                this.mDrawingBounds.set(f3, i4, i5, i6);
            } else if (this.mRealBounds.isEmpty() || 0.0f != COUIChipGroupEditText.access$000(this.mHost)) {
                this.mDrawingBounds.set(f3, i4, i5, i6);
                this.mHost.setX(this.mDrawingBounds.left);
            } else {
                RectF rectF2 = this.mRealBounds;
                if (rectF2.top != i4) {
                    this.mDrawingBounds.set(f3, i4, i5, i6);
                    this.mHost.setX(this.mDrawingBounds.left);
                } else if (rectF2.left != f3 && this.mDrawingBounds.left != f3) {
                    this.mHorizontalAnimation.animateToFinalPosition(f3);
                    this.mHost.setX(this.mDrawingBounds.left);
                }
            }
            this.mRealBounds.set(f3, i4, i5, i6);
        }

        @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
        public void bindController(COUIChipGroup cOUIChipGroup) {
            if (this.mController == null) {
                this.mController = cOUIChipGroup;
            }
        }

        @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
        public void forceFinishAllAnimation() {
            if (this.mHorizontalAnimation.canSkipToEnd()) {
                this.mHorizontalAnimation.skipToEnd();
            }
        }

        @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
        public void getDrawingBounds(RectF rectF) {
            rectF.set(this.mDrawingBounds);
        }

        public float getX() {
            return this.mDrawingBounds.left;
        }

        @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
        public boolean isChipAnimationRunning() {
            return this.mHorizontalAnimation.isRunning();
        }

        @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
        public void resetChipGroupAnimations() {
            this.mHorizontalAnimation.cancel();
            this.mHost.setTranslationX(0.0f);
        }

        public void setX(float f3) {
            RectF rectF = this.mDrawingBounds;
            rectF.offsetTo(f3, rectF.top);
            this.mHost.setX(this.mDrawingBounds.left);
        }

        @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
        public void unbindController() {
            this.mHorizontalAnimation.cancel();
            this.mHost.setTranslationX(0.0f);
            this.mRealBounds.setEmpty();
            this.mDrawingBounds.setEmpty();
            this.mController = null;
        }

        @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
        public void updateAttachState(boolean z2, boolean z3) {
        }

        @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
        public void updateChipRealBounds(int i3, int i4, int i5, int i6, boolean z2) {
            updateRealBounds(i3, i4, i5, i6, z2);
        }
    }

    public COUIChipGroupEditText(Context context) {
        super(context);
        this.mChipGroupAnimator = new ChipGroupAnimatorImpl(this);
        this.mTextLength = 0.0f;
        init();
    }

    public static /* synthetic */ boolean a(COUIChipGroupEditText cOUIChipGroupEditText, TextView textView, int i3, KeyEvent keyEvent) {
        cOUIChipGroupEditText.getClass();
        if (i3 != 6 && i3 != 0) {
            return false;
        }
        cOUIChipGroupEditText.requestFocus();
        return true;
    }

    public static /* synthetic */ float access$000(COUIChipGroupEditText cOUIChipGroupEditText) {
        return cOUIChipGroupEditText.mTextLength;
    }

    private void init() {
        setPadding(0, 0, 0, 0);
        setTextAppearance(getContext(), R.style.couiTextAppearanceBodyL);
        setMinimumHeight(0);
        setImeOptions(6);
        setOnEditorActionListener(new j(this));
        setBackground(null);
    }

    @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
    public void bindController(COUIChipGroup cOUIChipGroup) {
        this.mChipGroupAnimator.bindController(cOUIChipGroup);
    }

    public void ensureMinWidth() {
        setMinWidth(10);
    }

    @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
    public void forceFinishAllAnimation() {
        this.mChipGroupAnimator.forceFinishAllAnimation();
    }

    @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
    public void getDrawingBounds(RectF rectF) {
        this.mChipGroupAnimator.getDrawingBounds(rectF);
    }

    public int getEdittextMinWidth() {
        return 10;
    }

    @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
    public boolean isChipAnimationRunning() {
        return this.mChipGroupAnimator.isChipAnimationRunning();
    }

    @Override // com.coui.appcompat.edittext.COUIEditText, android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Editable editableText = getEditableText();
        if (editableText != null) {
            this.mTextLength = getPaint().measureText(editableText.toString());
        }
    }

    @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
    public void resetChipGroupAnimations() {
        this.mChipGroupAnimator.resetChipGroupAnimations();
    }

    @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
    public void unbindController() {
        this.mChipGroupAnimator.unbindController();
    }

    @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
    public void updateAttachState(boolean z2, boolean z3) {
        this.mChipGroupAnimator.updateAttachState(z2, z3);
    }

    @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
    public void updateChipRealBounds(int i3, int i4, int i5, int i6, boolean z2) {
        this.mChipGroupAnimator.updateChipRealBounds(i3, i4, i5, i6, z2);
    }

    public COUIChipGroupEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mChipGroupAnimator = new ChipGroupAnimatorImpl(this);
        this.mTextLength = 0.0f;
        init();
    }

    public COUIChipGroupEditText(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.mChipGroupAnimator = new ChipGroupAnimatorImpl(this);
        this.mTextLength = 0.0f;
        init();
    }
}