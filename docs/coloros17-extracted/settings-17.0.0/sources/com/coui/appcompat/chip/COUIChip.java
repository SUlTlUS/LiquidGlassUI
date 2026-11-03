package com.coui.appcompat.chip;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.pressfeedback.COUIPressFeedbackHelper;
import com.coui.appcompat.textutil.COUIChangeTextUtil;
import com.google.android.material.navigation.NavigationBarView;
import com.support.chip.R;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class COUIChip extends AppCompatCheckBox implements COUIChipDrawable.COUIChipDrawableDelegate, COUICheckable<COUIChip>, COUIChipGroup.IChipGroupAnimator {
    private static final String BUTTON_ACCESSIBILITY_CLASS_NAME = "android.widget.Button";
    private static final int[] CHECKABLE_STATE_SET;
    private static final int CHIP_BODY_VIRTUAL_ID = 0;
    private static final int CLOSE_ICON_VIRTUAL_ID = 1;
    private static final String COMPOUND_BUTTON_ACCESSIBILITY_CLASS_NAME = "android.widget.CompoundButton";
    private static final boolean DEBUG;
    private static final int DEF_STYLE_RES;
    private static final Rect EMPTY_BOUNDS;
    private static final String GENERIC_VIEW_ACCESSIBILITY_CLASS_NAME = "android.view.View";
    private static final String NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android";
    private static final String RADIO_BUTTON_ACCESSIBILITY_CLASS_NAME = "android.widget.RadioButton";
    private static final int[] SELECTED_STATE;
    private static final String TAG = "COUIChip";
    private CharSequence mAccessibilityClassName;
    private COUIChipDrawable mChipDrawable;
    private COUIChipGroup.IChipGroupAnimator mChipGroupAnimator;
    private final ChipTouchHelper mChipTouchHelper;
    private boolean mCloseIconFocused;
    private boolean mCloseIconHovered;
    private boolean mCloseIconPressed;
    private final RectF mCloseIconTouchBounds;
    private final Rect mCloseIconTouchBoundsInt;
    private boolean mDeferredCheckedValue;
    private boolean mIsAttachedToChipGroup;
    private int mLastLayoutDirection;
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;
    private COUICheckable.OnCheckedChangeListener<COUIChip> mOnCheckedChangeListenerInternal;
    private View.OnClickListener mOnCloseIconClickListener;
    private final COUIPressFeedbackHelper mPressFeedbackHelper;
    private float mTextTranslationX;
    private float mTextTranslationY;
    private boolean mTouchHelperEnabled;

    /* JADX INFO: loaded from: classes7.dex */
    public class ChipGroupAnimatorImpl implements COUIChipGroup.IChipGroupAnimator {
        private COUISpringAnimation mAlphaAnimation;
        private COUIChipGroup mController;
        private COUISpringAnimation mHorizontalAnimation;
        private final COUIChip mHost;
        private COUISpringAnimation mScaleAnimation;
        private COUISpringAnimation mVerticalAnimation;
        private final RectF mDrawingBounds = new RectF();
        private final RectF mRealBounds = new RectF();
        private float mScale = 0.8f;

        /* JADX INFO: renamed from: com.coui.appcompat.chip.COUIChip$ChipGroupAnimatorImpl$1, reason: invalid class name */
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

        /* JADX INFO: renamed from: com.coui.appcompat.chip.COUIChip$ChipGroupAnimatorImpl$2, reason: invalid class name */
        public class AnonymousClass2 extends FloatPropertyCompat<ChipGroupAnimatorImpl> {
            public AnonymousClass2(String str) {
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
                return chipGroupAnimatorImpl.getY();
            }

            /* JADX INFO: renamed from: setValue, reason: avoid collision after fix types in other method */
            public void setValue2(ChipGroupAnimatorImpl chipGroupAnimatorImpl, float f3) {
                chipGroupAnimatorImpl.setY(f3);
            }
        }

        /* JADX INFO: renamed from: com.coui.appcompat.chip.COUIChip$ChipGroupAnimatorImpl$3, reason: invalid class name */
        public class AnonymousClass3 extends FloatPropertyCompat<ChipGroupAnimatorImpl> {
            public AnonymousClass3(String str) {
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
                return chipGroupAnimatorImpl.getAlpha();
            }

            /* JADX INFO: renamed from: setValue, reason: avoid collision after fix types in other method */
            public void setValue2(ChipGroupAnimatorImpl chipGroupAnimatorImpl, float f3) {
                chipGroupAnimatorImpl.setAlpha(f3);
            }
        }

        /* JADX INFO: renamed from: com.coui.appcompat.chip.COUIChip$ChipGroupAnimatorImpl$4, reason: invalid class name */
        public class AnonymousClass4 extends FloatPropertyCompat<ChipGroupAnimatorImpl> {
            public AnonymousClass4(String str) {
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
                return chipGroupAnimatorImpl.getScale();
            }

            /* JADX INFO: renamed from: setValue, reason: avoid collision after fix types in other method */
            public void setValue2(ChipGroupAnimatorImpl chipGroupAnimatorImpl, float f3) {
                chipGroupAnimatorImpl.setScale(f3);
            }
        }

        public ChipGroupAnimatorImpl(COUIChip cOUIChip) {
            this.mHost = cOUIChip;
            initAnimation();
        }

        public static /* synthetic */ void a(ChipGroupAnimatorImpl chipGroupAnimatorImpl, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
            if (f3 == 0.0f) {
                COUIChipGroup cOUIChipGroup = chipGroupAnimatorImpl.mController;
                if (cOUIChipGroup != null) {
                    cOUIChipGroup.onChipHideAnimationEnd(chipGroupAnimatorImpl.mHost);
                    return;
                }
                return;
            }
            if (f3 != 10000.0f) {
                chipGroupAnimatorImpl.getClass();
                return;
            }
            COUIChipGroup cOUIChipGroup2 = chipGroupAnimatorImpl.mController;
            if (cOUIChipGroup2 != null) {
                cOUIChipGroup2.onChipShowAnimationEnd(chipGroupAnimatorImpl.mHost);
            }
        }

        private void initAnimation() {
            AnonymousClass1 anonymousClass1 = new AnonymousClass1("ChipGroupAnimatorImpl");
            COUISpringForce cOUISpringForce = new COUISpringForce();
            cOUISpringForce.setResponse(0.3f);
            cOUISpringForce.setBounce(0.0f);
            COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(this, anonymousClass1);
            this.mHorizontalAnimation = cOUISpringAnimation;
            cOUISpringAnimation.setSpring(cOUISpringForce);
            AnonymousClass2 anonymousClass2 = new AnonymousClass2("ChipGroupAnimatorImpl");
            COUISpringForce cOUISpringForce2 = new COUISpringForce();
            cOUISpringForce2.setResponse(0.3f);
            cOUISpringForce2.setBounce(0.0f);
            COUISpringAnimation cOUISpringAnimation2 = new COUISpringAnimation(this, anonymousClass2);
            this.mVerticalAnimation = cOUISpringAnimation2;
            cOUISpringAnimation2.setSpring(cOUISpringForce2);
            AnonymousClass3 anonymousClass3 = new AnonymousClass3("ChipGroupAnimatorImpl");
            COUISpringForce cOUISpringForce3 = new COUISpringForce();
            cOUISpringForce3.setResponse(0.2f);
            cOUISpringForce3.setBounce(0.0f);
            COUISpringAnimation cOUISpringAnimation3 = new COUISpringAnimation(this, anonymousClass3);
            this.mAlphaAnimation = cOUISpringAnimation3;
            cOUISpringAnimation3.setSpring(cOUISpringForce3);
            this.mAlphaAnimation.addEndListener(new c(this));
            AnonymousClass4 anonymousClass4 = new AnonymousClass4("ChipGroupAnimatorImpl");
            COUISpringForce cOUISpringForce4 = new COUISpringForce();
            cOUISpringForce4.setResponse(0.2f);
            cOUISpringForce4.setBounce(0.0f);
            COUISpringAnimation cOUISpringAnimation4 = new COUISpringAnimation(this, anonymousClass4);
            this.mScaleAnimation = cOUISpringAnimation4;
            cOUISpringAnimation4.setSpring(cOUISpringForce4);
        }

        private boolean isRTL() {
            return ViewCompat.getLayoutDirection(this.mHost) == 1;
        }

        /* JADX WARN: Code duplicated, block: B:23:0x0050  */
        /* JADX WARN: Code duplicated, block: B:25:0x0056  */
        /* JADX WARN: Code duplicated, block: B:27:0x005f  */
        /* JADX WARN: Code duplicated, block: B:29:0x0067  */
        private void updateRealBounds(int i3, int i4, int i5, int i6, boolean z2) {
            float f3;
            float f4;
            RectF rectF = this.mRealBounds;
            float f5 = i3;
            if (rectF.left == f5 && rectF.top == i4 && rectF.right == i5 && rectF.bottom == i6) {
                return;
            }
            if (z2) {
                if (rectF.isEmpty() || !COUIChip.access$800(COUIChip.this)) {
                    this.mDrawingBounds.set(f5, i4, i5, i6);
                    this.mHost.setX(this.mDrawingBounds.left);
                    this.mHost.setY(this.mDrawingBounds.top);
                } else {
                    if (!isRTL() && this.mRealBounds.left != f5) {
                        float f6 = this.mDrawingBounds.left;
                        if (f6 != f5) {
                            this.mHost.setX(f6);
                            this.mHorizontalAnimation.animateToFinalPosition(f5);
                        } else if (isRTL()) {
                            f3 = i5;
                            if (this.mRealBounds.right != f3) {
                                f4 = this.mDrawingBounds.right;
                                if (f4 != f3) {
                                    float f7 = i5 - i3;
                                    this.mHorizontalAnimation.setStartValue(f4 - f7);
                                    this.mHost.setX(this.mDrawingBounds.right - f7);
                                    this.mHorizontalAnimation.animateToFinalPosition(f5);
                                }
                            }
                        }
                    } else if (isRTL()) {
                        f3 = i5;
                        if (this.mRealBounds.right != f3) {
                            f4 = this.mDrawingBounds.right;
                            if (f4 != f3) {
                                float f8 = i5 - i3;
                                this.mHorizontalAnimation.setStartValue(f4 - f8);
                                this.mHost.setX(this.mDrawingBounds.right - f8);
                                this.mHorizontalAnimation.animateToFinalPosition(f5);
                            }
                        }
                    }
                    float f9 = i4;
                    if (this.mRealBounds.top != f9 && this.mDrawingBounds.top != f9) {
                        this.mVerticalAnimation.animateToFinalPosition(f9);
                        this.mHost.setY(this.mDrawingBounds.top);
                    }
                }
                if (isRTL()) {
                    RectF rectF2 = this.mDrawingBounds;
                    rectF2.left = rectF2.right - (i5 - i3);
                } else {
                    RectF rectF3 = this.mDrawingBounds;
                    rectF3.right = rectF3.left + (i5 - i3);
                }
            } else {
                this.mDrawingBounds.set(f5, i4, i5, i6);
            }
            this.mRealBounds.set(f5, i4, i5, i6);
        }

        @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
        public void bindController(COUIChipGroup cOUIChipGroup) {
            if (this.mController == null) {
                this.mController = cOUIChipGroup;
            }
        }

        @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
        public void forceFinishAllAnimation() {
            if (this.mVerticalAnimation.canSkipToEnd()) {
                this.mVerticalAnimation.skipToEnd();
            }
            if (this.mHorizontalAnimation.canSkipToEnd()) {
                this.mHorizontalAnimation.skipToEnd();
            }
            if (this.mAlphaAnimation.canSkipToEnd()) {
                this.mAlphaAnimation.skipToEnd();
            }
        }

        public float getAlpha() {
            return this.mHost.getAlpha() * 10000.0f;
        }

        @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
        public void getDrawingBounds(RectF rectF) {
            rectF.set(this.mDrawingBounds);
        }

        public float getScale() {
            return this.mScale * 10000.0f;
        }

        public float getX() {
            return this.mDrawingBounds.left;
        }

        public float getY() {
            return this.mDrawingBounds.top;
        }

        @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
        public boolean isChipAnimationRunning() {
            return this.mHorizontalAnimation.isRunning() || this.mVerticalAnimation.isRunning() || this.mAlphaAnimation.isRunning();
        }

        @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
        public void resetChipGroupAnimations() {
            this.mHorizontalAnimation.cancel();
            this.mVerticalAnimation.cancel();
            this.mAlphaAnimation.cancel();
            this.mHost.setTranslationX(0.0f);
            this.mHost.setTranslationY(0.0f);
            this.mHost.setAlpha(1.0f);
        }

        public void setAlpha(float f3) {
            COUIChipGroup cOUIChipGroup;
            this.mHost.setAlpha(f3 / 10000.0f);
            if (this.mHost.getParent() != null || (cOUIChipGroup = this.mController) == null) {
                return;
            }
            cOUIChipGroup.invalidate();
        }

        public void setScale(float f3) {
            COUIChipGroup cOUIChipGroup;
            this.mScale = f3 / 10000.0f;
            COUIChip cOUIChip = this.mHost;
            cOUIChip.setPivotX(cOUIChip.getWidth() / 2.0f);
            COUIChip cOUIChip2 = this.mHost;
            cOUIChip2.setPivotY(cOUIChip2.getHeight() / 2.0f);
            this.mHost.setScaleX(this.mScale);
            this.mHost.setScaleY(this.mScale);
            if (this.mHost.getParent() != null || (cOUIChipGroup = this.mController) == null) {
                return;
            }
            cOUIChipGroup.invalidate();
        }

        public void setX(float f3) {
            RectF rectF = this.mDrawingBounds;
            rectF.offsetTo(f3, rectF.top);
            this.mHost.setX(this.mDrawingBounds.left);
        }

        public void setY(float f3) {
            RectF rectF = this.mDrawingBounds;
            rectF.offsetTo(rectF.left, f3);
            this.mHost.setY(this.mDrawingBounds.top);
        }

        @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
        public void unbindController() {
            this.mHorizontalAnimation.cancel();
            this.mVerticalAnimation.cancel();
            this.mAlphaAnimation.cancel();
            this.mHost.setTranslationX(0.0f);
            this.mHost.setTranslationY(0.0f);
            this.mHost.setAlpha(1.0f);
            this.mScale = 0.8f;
            this.mRealBounds.setEmpty();
            this.mDrawingBounds.setEmpty();
            this.mController = null;
        }

        @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
        public void updateAttachState(boolean z2, boolean z3) {
            if (COUIChip.access$800(COUIChip.this) != z2) {
                COUIChip.access$802(COUIChip.this, z2);
                if (!z3) {
                    this.mHost.setAlpha(COUIChip.access$800(COUIChip.this) ? 1.0f : 0.0f);
                    this.mHost.setScaleX(1.0f);
                    this.mHost.setScaleY(1.0f);
                    return;
                }
                this.mHost.setAlpha(COUIChip.access$800(COUIChip.this) ? 0.0f : 1.0f);
                this.mAlphaAnimation.animateToFinalPosition(COUIChip.access$800(COUIChip.this) ? 10000.0f : 0.0f);
                if (COUIChip.access$800(COUIChip.this) && this.mRealBounds.isEmpty()) {
                    COUIChip cOUIChip = this.mHost;
                    cOUIChip.setPivotX(cOUIChip.getWidth() / 2.0f);
                    COUIChip cOUIChip2 = this.mHost;
                    cOUIChip2.setPivotY(cOUIChip2.getHeight() / 2.0f);
                    this.mHost.setScaleX(0.8f);
                    this.mHost.setScaleY(0.8f);
                    this.mScaleAnimation.animateToFinalPosition(10000.0f);
                }
            }
        }

        @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
        public void updateChipRealBounds(int i3, int i4, int i5, int i6, boolean z2) {
            updateRealBounds(i3, i4, i5, i6, z2);
        }
    }

    public class ChipTouchHelper extends ExploreByTouchHelper {
        public ChipTouchHelper(COUIChip cOUIChip) {
            super(cOUIChip);
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public int getVirtualViewAt(float f3, float f4) {
            return (COUIChip.access$000(COUIChip.this) && COUIChip.access$100(COUIChip.this).contains(f3, f4)) ? 1 : 0;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public void getVisibleVirtualViews(List<Integer> list) {
            list.add(0);
            if (COUIChip.access$000(COUIChip.this) && COUIChip.this.isCloseIconVisible() && COUIChip.access$200(COUIChip.this) != null) {
                list.add(1);
            }
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public boolean onPerformActionForVirtualView(int i3, int i4, Bundle bundle) {
            if (i4 != 16) {
                return false;
            }
            if (i3 == 0) {
                return COUIChip.this.performClick();
            }
            if (i3 == 1) {
                return COUIChip.this.performCloseIconClick();
            }
            return false;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public void onPopulateNodeForHost(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            accessibilityNodeInfoCompat.setCheckable(COUIChip.this.isCheckable());
            accessibilityNodeInfoCompat.setClickable(COUIChip.this.isClickable());
            CharSequence text = COUIChip.this.getText();
            if (COUIChip.access$600(COUIChip.this) != null && COUIChip.access$600(COUIChip.this).showsCount()) {
                CharSequence countText = COUIChip.access$600(COUIChip.this).getCountText();
                if (!TextUtils.isEmpty(countText)) {
                    try {
                        CharSequence charSequenceConcat = TextUtils.concat(text, COUIChip.this.getContext().getString(R.string.coui_chip_count_separator), String.format(COUIChip.this.getContext().getString(R.string.coui_chip_count_format), countText));
                        if (COUIChip.access$700()) {
                            Log.d(COUIChip.TAG, "Accessibility text with count: " + ((Object) charSequenceConcat));
                        }
                        text = charSequenceConcat;
                    } catch (Exception e3) {
                        text = TextUtils.concat(text, ", ", countText);
                        if (COUIChip.access$700()) {
                            Log.w(COUIChip.TAG, "Failed to load string resources, using default format", e3);
                        }
                    }
                }
            }
            accessibilityNodeInfoCompat.setClassName(COUIChip.this.getAccessibilityClassName());
            accessibilityNodeInfoCompat.setText(text);
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public void onPopulateNodeForVirtualView(int i3, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            if (i3 != 1) {
                accessibilityNodeInfoCompat.setContentDescription("");
                accessibilityNodeInfoCompat.setBoundsInParent(COUIChip.access$500());
                return;
            }
            CharSequence closeIconContentDescription = COUIChip.this.getCloseIconContentDescription();
            if (closeIconContentDescription != null) {
                accessibilityNodeInfoCompat.setContentDescription(closeIconContentDescription);
            } else {
                CharSequence text = COUIChip.this.getText();
                accessibilityNodeInfoCompat.setContentDescription(COUIChip.this.getContext().getString(com.google.android.material.R.string.mtrl_chip_close_icon_content_description, TextUtils.isEmpty(text) ? "" : text).trim());
            }
            accessibilityNodeInfoCompat.setBoundsInParent(COUIChip.access$400(COUIChip.this));
            accessibilityNodeInfoCompat.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK);
            accessibilityNodeInfoCompat.setEnabled(COUIChip.this.isEnabled());
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public void onVirtualViewKeyboardFocusChanged(int i3, boolean z2) {
            if (i3 == 1) {
                COUIChip.access$302(COUIChip.this, z2);
                COUIChip.this.refreshDrawableState();
            }
        }
    }

    static {
        DEBUG = COUILog.LOG_DEBUG || COUILog.isLoggable(TAG, 3);
        DEF_STYLE_RES = R.style.Widget_COUI_Chip;
        EMPTY_BOUNDS = new Rect();
        SELECTED_STATE = new int[]{16842913};
        CHECKABLE_STATE_SET = new int[]{android.R.attr.state_checkable};
    }

    public COUIChip(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void a(COUIChip cOUIChip, CompoundButton compoundButton, boolean z2) {
        COUICheckable.OnCheckedChangeListener<COUIChip> onCheckedChangeListener = cOUIChip.mOnCheckedChangeListenerInternal;
        if (onCheckedChangeListener != null) {
            onCheckedChangeListener.onCheckedChanged(cOUIChip, z2);
        }
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener2 = cOUIChip.mOnCheckedChangeListener;
        if (onCheckedChangeListener2 != null) {
            onCheckedChangeListener2.onCheckedChanged(compoundButton, z2);
        }
    }

    public static /* synthetic */ boolean access$000(COUIChip cOUIChip) {
        return cOUIChip.hasCloseIcon();
    }

    public static /* synthetic */ RectF access$100(COUIChip cOUIChip) {
        return cOUIChip.getCloseIconTouchBounds();
    }

    public static /* synthetic */ View.OnClickListener access$200(COUIChip cOUIChip) {
        return cOUIChip.mOnCloseIconClickListener;
    }

    public static /* synthetic */ boolean access$302(COUIChip cOUIChip, boolean z2) {
        cOUIChip.mCloseIconFocused = z2;
        return z2;
    }

    public static /* synthetic */ Rect access$400(COUIChip cOUIChip) {
        return cOUIChip.getCloseIconTouchBoundsInt();
    }

    public static /* synthetic */ Rect access$500() {
        return EMPTY_BOUNDS;
    }

    public static /* synthetic */ COUIChipDrawable access$600(COUIChip cOUIChip) {
        return cOUIChip.mChipDrawable;
    }

    public static /* synthetic */ boolean access$700() {
        return DEBUG;
    }

    public static /* synthetic */ boolean access$800(COUIChip cOUIChip) {
        return cOUIChip.mIsAttachedToChipGroup;
    }

    public static /* synthetic */ boolean access$802(COUIChip cOUIChip, boolean z2) {
        cOUIChip.mIsAttachedToChipGroup = z2;
        return z2;
    }

    private void applyChipDrawable(COUIChipDrawable cOUIChipDrawable) {
        cOUIChipDrawable.setDelegate(this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [boolean, int] */
    private int[] createCloseIconDrawableState() {
        ?? IsEnabled = isEnabled();
        int i3 = IsEnabled;
        if (this.mCloseIconFocused) {
            i3 = IsEnabled + 1;
        }
        int i4 = i3;
        if (this.mCloseIconHovered) {
            i4 = i3 + 1;
        }
        int i5 = i4;
        if (this.mCloseIconPressed) {
            i5 = i4 + 1;
        }
        int i6 = i5;
        if (isChecked()) {
            i6 = i5 + 1;
        }
        int[] iArr = new int[i6];
        int i7 = 0;
        if (isEnabled()) {
            iArr[0] = 16842910;
            i7 = 1;
        }
        if (this.mCloseIconFocused) {
            iArr[i7] = 16842908;
            i7++;
        }
        if (this.mCloseIconHovered) {
            iArr[i7] = 16843623;
            i7++;
        }
        if (this.mCloseIconPressed) {
            iArr[i7] = 16842919;
            i7++;
        }
        if (isChecked()) {
            iArr[i7] = 16842913;
        }
        return iArr;
    }

    private RectF getCloseIconTouchBounds() {
        COUIChipDrawable cOUIChipDrawable;
        this.mCloseIconTouchBounds.setEmpty();
        if (hasCloseIcon() && this.mOnCloseIconClickListener != null && (cOUIChipDrawable = this.mChipDrawable) != null) {
            cOUIChipDrawable.getCloseIconTouchBounds(this.mCloseIconTouchBounds);
        }
        return this.mCloseIconTouchBounds;
    }

    private Rect getCloseIconTouchBoundsInt() {
        RectF closeIconTouchBounds = getCloseIconTouchBounds();
        this.mCloseIconTouchBoundsInt.set((int) closeIconTouchBounds.left, (int) closeIconTouchBounds.top, (int) closeIconTouchBounds.right, (int) closeIconTouchBounds.bottom);
        return this.mCloseIconTouchBoundsInt;
    }

    private void handleActionCancel() {
        if (this.mCloseIconPressed) {
            setCloseIconPressed(false);
            COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
            if (cOUIChipDrawable != null) {
                cOUIChipDrawable.setCloseIconTouched(false);
                return;
            }
            return;
        }
        this.mPressFeedbackHelper.executeFeedbackAnimator(false);
        COUIChipDrawable cOUIChipDrawable2 = this.mChipDrawable;
        if (cOUIChipDrawable2 != null) {
            cOUIChipDrawable2.setTouched(false);
        }
    }

    private boolean handleActionDown(boolean z2) {
        if (z2) {
            setCloseIconPressed(true);
            COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
            if (cOUIChipDrawable != null) {
                cOUIChipDrawable.setCloseIconTouched(true);
            }
            return true;
        }
        this.mPressFeedbackHelper.executeFeedbackAnimator(true);
        COUIChipDrawable cOUIChipDrawable2 = this.mChipDrawable;
        if (cOUIChipDrawable2 == null) {
            return false;
        }
        cOUIChipDrawable2.setTouched(true);
        return false;
    }

    private boolean handleActionMove(boolean z2) {
        if (!this.mCloseIconPressed) {
            return false;
        }
        if (z2) {
            return true;
        }
        setCloseIconPressed(false);
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable == null) {
            return true;
        }
        cOUIChipDrawable.setCloseIconTouched(false);
        return true;
    }

    private boolean handleActionUp() {
        if (!this.mCloseIconPressed) {
            this.mPressFeedbackHelper.executeFeedbackAnimator(false);
            COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
            if (cOUIChipDrawable != null) {
                cOUIChipDrawable.setTouched(false);
            }
            return false;
        }
        performCloseIconClick();
        setCloseIconPressed(false);
        COUIChipDrawable cOUIChipDrawable2 = this.mChipDrawable;
        if (cOUIChipDrawable2 == null) {
            return true;
        }
        cOUIChipDrawable2.setCloseIconTouched(false);
        return true;
    }

    private boolean hasCloseIcon() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        return (cOUIChipDrawable == null || cOUIChipDrawable.getCloseIcon() == null) ? false : true;
    }

    private void setCloseIconHovered(boolean z2) {
        if (this.mCloseIconHovered != z2) {
            this.mCloseIconHovered = z2;
            refreshDrawableState();
        }
    }

    private void setCloseIconPressed(boolean z2) {
        if (this.mCloseIconPressed != z2) {
            this.mCloseIconPressed = z2;
            refreshDrawableState();
        }
    }

    private void unapplyChipDrawable(COUIChipDrawable cOUIChipDrawable) {
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setDelegate(null);
        }
    }

    private void updateAccessibilityDelegate() {
        if (hasCloseIcon() && isCloseIconVisible() && this.mOnCloseIconClickListener != null) {
            ViewCompat.setAccessibilityDelegate(this, this.mChipTouchHelper);
            this.mTouchHelperEnabled = true;
        } else {
            ViewCompat.setAccessibilityDelegate(this, null);
            this.mTouchHelperEnabled = false;
        }
    }

    private void updateBackgroundDrawable() {
        ViewCompat.setBackground(this, getBackgroundDrawable());
        updatePaddingInternal();
    }

    private void updatePaddingInternal() {
        int startPaddingForTextCloseIconVisible;
        int endPaddingForTextCloseIconVisible;
        if (this.mChipDrawable != null) {
            if (!TextUtils.isEmpty(getText()) || this.mChipDrawable.showsCloseIcon()) {
                this.mChipDrawable.getIntrinsicWidth();
                startPaddingForTextCloseIconVisible = (int) ((this.mChipDrawable.showsCloseIcon() || this.mChipDrawable.closeIconIsVisibleAndAnimating()) ? this.mChipDrawable.getStartPaddingForTextCloseIconVisible() : this.mChipDrawable.getStartPaddingForTextCloseIconInvisible());
                endPaddingForTextCloseIconVisible = (int) ((this.mChipDrawable.showsCloseIcon() || this.mChipDrawable.closeIconIsVisibleAndAnimating()) ? this.mChipDrawable.getEndPaddingForTextCloseIconVisible() : this.mChipDrawable.getEndPaddingForTextCloseIconInvisible());
            } else {
                startPaddingForTextCloseIconVisible = 0;
                endPaddingForTextCloseIconVisible = 0;
            }
            ViewCompat.setPaddingRelative(this, startPaddingForTextCloseIconVisible, getPaddingTop(), endPaddingForTextCloseIconVisible, getPaddingBottom());
        }
    }

    private void validateAttributes(AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        if (attributeSet.getAttributeValue(NAMESPACE_ANDROID, "background") != null) {
            Log.w(TAG, "Do not set the background; Chip manages its own background drawable.");
        }
        if (attributeSet.getAttributeValue(NAMESPACE_ANDROID, "drawableLeft") != null) {
            throw new UnsupportedOperationException("Please set left drawable using R.attr#chipIcon.");
        }
        if (attributeSet.getAttributeValue(NAMESPACE_ANDROID, "drawableStart") != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (attributeSet.getAttributeValue(NAMESPACE_ANDROID, "drawableEnd") != null) {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        if (attributeSet.getAttributeValue(NAMESPACE_ANDROID, "drawableRight") != null) {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        if (!attributeSet.getAttributeBooleanValue(NAMESPACE_ANDROID, "singleLine", true) || attributeSet.getAttributeIntValue(NAMESPACE_ANDROID, "lines", 1) != 1 || attributeSet.getAttributeIntValue(NAMESPACE_ANDROID, "minLines", 1) != 1 || attributeSet.getAttributeIntValue(NAMESPACE_ANDROID, "maxLines", 1) != 1) {
            throw new UnsupportedOperationException("Chip does not support multi-line text");
        }
        if (attributeSet.getAttributeIntValue(NAMESPACE_ANDROID, "gravity", NavigationBarView.ITEM_GRAVITY_START_CENTER) != 8388627) {
            Log.w(TAG, "Chip text must be vertically center and start aligned");
        }
    }

    @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
    public void bindController(COUIChipGroup cOUIChipGroup) {
        if (this.mChipGroupAnimator == null) {
            this.mChipGroupAnimator = new ChipGroupAnimatorImpl(this);
        }
        this.mChipGroupAnimator.bindController(cOUIChipGroup);
    }

    @Override // android.view.View
    public boolean dispatchHoverEvent(MotionEvent motionEvent) {
        if (this.mTouchHelperEnabled) {
            return this.mChipTouchHelper.dispatchHoverEvent(motionEvent) || super.dispatchHoverEvent(motionEvent);
        }
        return super.dispatchHoverEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (!this.mTouchHelperEnabled) {
            return super.dispatchKeyEvent(keyEvent);
        }
        if (!this.mChipTouchHelper.dispatchKeyEvent(keyEvent) || this.mChipTouchHelper.getKeyboardFocusedVirtualViewId() == Integer.MIN_VALUE) {
            return super.dispatchKeyEvent(keyEvent);
        }
        return true;
    }

    @Override // androidx.appcompat.widget.AppCompatCheckBox, android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if ((cOUIChipDrawable == null || !cOUIChipDrawable.isCloseIconStateful()) ? false : this.mChipDrawable.setCloseIconState(createCloseIconDrawableState())) {
            invalidate();
        }
    }

    @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
    public void forceFinishAllAnimation() {
        COUIChipGroup.IChipGroupAnimator iChipGroupAnimator = this.mChipGroupAnimator;
        if (iChipGroupAnimator != null) {
            iChipGroupAnimator.forceFinishAllAnimation();
        }
    }

    @Override // android.widget.CheckBox, android.widget.CompoundButton, android.widget.Button, android.widget.TextView, android.view.View
    public CharSequence getAccessibilityClassName() {
        if (TextUtils.isEmpty(this.mAccessibilityClassName)) {
            return (isCheckable() || isClickable()) ? "android.widget.Button" : "android.view.View";
        }
        return this.mAccessibilityClassName;
    }

    public Drawable getBackgroundDrawable() {
        return this.mChipDrawable;
    }

    public int getCheckedBackgroundColor() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getCheckedBackgroundColor();
        }
        return 0;
    }

    public int getCheckedChipIconTint() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getCheckedChipIconTint();
        }
        return 0;
    }

    public int getCheckedDisabledBackgroundColor() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getCheckedDisabledBackgroundColor();
        }
        return 0;
    }

    public int getCheckedDisabledChipIconTint() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getCheckedDisabledChipIconTint();
        }
        return 0;
    }

    public int getCheckedDisabledTextColor() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getCheckedDisabledTextColor();
        }
        return 0;
    }

    public int getCheckedTextColor() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getCheckedTextColor();
        }
        return 0;
    }

    public float getChipCornerRadius() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getChipCornerRadius();
        }
        return -1.0f;
    }

    public COUIChipDrawable getChipDrawable() {
        return this.mChipDrawable;
    }

    public float getChipEndPadding() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getChipStartPadding();
        }
        return 0.0f;
    }

    public Drawable getChipIcon() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getChipIcon();
        }
        return null;
    }

    public float getChipIconEndPadding() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getChipIconEndPadding();
        }
        return 0.0f;
    }

    public float getChipIconSize() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getChipIconSize();
        }
        return 0.0f;
    }

    public float getChipIconStartPadding() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getChipIconStartPadding();
        }
        return 0.0f;
    }

    public float getChipMinHeight() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getChipMinHeight();
        }
        return 0.0f;
    }

    public int getChipPositionInParent() {
        ViewParent parent = getParent();
        if (!(parent instanceof ViewGroup)) {
            return -1;
        }
        ViewGroup viewGroup = (ViewGroup) parent;
        int i3 = 0;
        for (int i4 = 0; i4 < viewGroup.getChildCount(); i4++) {
            View childAt = viewGroup.getChildAt(i4);
            if ((childAt instanceof COUIChip) && childAt.getVisibility() == 0) {
                if (childAt == this) {
                    return i3 + 1;
                }
                i3++;
            }
        }
        return -1;
    }

    public float getChipStartPadding() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getChipStartPadding();
        }
        return 0.0f;
    }

    public Drawable getCloseIcon() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getCloseIcon();
        }
        return null;
    }

    public CharSequence getCloseIconContentDescription() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getCloseIconContentDescription();
        }
        return null;
    }

    public float getCloseIconEndPadding() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getCloseIconEndPadding();
        }
        return 0.0f;
    }

    public float getCloseIconSize() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getCloseIconSize();
        }
        return 0.0f;
    }

    public float getCloseIconStartPadding() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getCloseIconStartPadding();
        }
        return 0.0f;
    }

    public CharSequence getCountText() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getCountText();
        }
        return null;
    }

    public int getCountTextColor() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getCountTextColor();
        }
        return 0;
    }

    @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
    public void getDrawingBounds(RectF rectF) {
        COUIChipGroup.IChipGroupAnimator iChipGroupAnimator = this.mChipGroupAnimator;
        if (iChipGroupAnimator != null) {
            iChipGroupAnimator.getDrawingBounds(rectF);
        }
    }

    @Override // android.widget.TextView
    public TextUtils.TruncateAt getEllipsize() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getEllipsize();
        }
        return null;
    }

    @Override // android.widget.TextView, android.view.View
    public void getFocusedRect(Rect rect) {
        if (this.mTouchHelperEnabled && (this.mChipTouchHelper.getKeyboardFocusedVirtualViewId() == 1 || this.mChipTouchHelper.getAccessibilityFocusedVirtualViewId() == 1)) {
            rect.set(getCloseIconTouchBoundsInt());
        } else {
            super.getFocusedRect(rect);
        }
    }

    public float getTextEndPadding() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getTextEndPadding();
        }
        return 0.0f;
    }

    public int getTextMaxWidth() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getTextMaxWidth();
        }
        Log.w(TAG, "Chip drawable not set!");
        return 0;
    }

    public float getTextStartPadding() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getTextStartPadding();
        }
        return 0.0f;
    }

    public int getTotalChipCountInParent() {
        ViewParent parent = getParent();
        if (!(parent instanceof ViewGroup)) {
            return 0;
        }
        ViewGroup viewGroup = (ViewGroup) parent;
        int i3 = 0;
        for (int i4 = 0; i4 < viewGroup.getChildCount(); i4++) {
            View childAt = viewGroup.getChildAt(i4);
            if ((childAt instanceof COUIChip) && childAt.getVisibility() == 0) {
                i3++;
            }
        }
        return i3;
    }

    public int getUncheckedBackgroundColor() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getUncheckedBackgroundColor();
        }
        return 0;
    }

    public int getUncheckedChipIconTint() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getUncheckedChipIconTint();
        }
        return 0;
    }

    public int getUncheckedDisabledBackgroundColor() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getUncheckedDisabledBackgroundColor();
        }
        return 0;
    }

    public int getUncheckedDisabledChipIconTint() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getUncheckedDisabledChipIconTint();
        }
        return 0;
    }

    public int getUncheckedDisabledTextColor() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getUncheckedDisabledTextColor();
        }
        return 0;
    }

    public int getUncheckedTextColor() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            return cOUIChipDrawable.getUncheckedTextColor();
        }
        return 0;
    }

    public boolean isCheckable() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        return cOUIChipDrawable != null && cOUIChipDrawable.isCheckable();
    }

    @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
    public boolean isChipAnimationRunning() {
        COUIChipGroup.IChipGroupAnimator iChipGroupAnimator = this.mChipGroupAnimator;
        if (iChipGroupAnimator != null) {
            return iChipGroupAnimator.isChipAnimationRunning();
        }
        return false;
    }

    public boolean isChipIconVisible() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        return cOUIChipDrawable != null && cOUIChipDrawable.isChipIconVisible();
    }

    public boolean isCloseIconVisible() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        return cOUIChipDrawable != null && cOUIChipDrawable.isCloseIconVisible();
    }

    public boolean isShowRedDot() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        return cOUIChipDrawable != null && cOUIChipDrawable.isShowRedDot();
    }

    @Override // com.coui.appcompat.chip.COUIChipDrawable.COUIChipDrawableDelegate
    public void onChipDrawableSizeChange() {
        updatePaddingInternal();
        requestLayout();
    }

    @Override // com.coui.appcompat.chip.COUIChipDrawable.COUIChipDrawableDelegate
    public void onChipTextColorChange(int i3) {
        setTextColor(i3);
    }

    @Override // com.coui.appcompat.chip.COUIChipDrawable.COUIChipDrawableDelegate
    public void onChipTextFontChanged(Typeface typeface) {
        setTypeface(typeface);
    }

    @Override // com.coui.appcompat.chip.COUIChipDrawable.COUIChipDrawableDelegate
    public void onChipTextOffsetChanged(float f3, float f4) {
        this.mTextTranslationX = f3;
        this.mTextTranslationY = f4;
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public int[] onCreateDrawableState(int i3) {
        int[] iArrOnCreateDrawableState = super.onCreateDrawableState(i3 + 2);
        if (isChecked()) {
            View.mergeDrawableStates(iArrOnCreateDrawableState, SELECTED_STATE);
        }
        if (isCheckable()) {
            View.mergeDrawableStates(iArrOnCreateDrawableState, CHECKABLE_STATE_SET);
        }
        return iArrOnCreateDrawableState;
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        canvas.translate(this.mTextTranslationX, this.mTextTranslationY);
        super.onDraw(canvas);
        canvas.translate(-this.mTextTranslationX, -this.mTextTranslationY);
    }

    @Override // android.widget.TextView, android.view.View
    public void onFocusChanged(boolean z2, int i3, Rect rect) {
        super.onFocusChanged(z2, i3, rect);
        if (this.mTouchHelperEnabled) {
            this.mChipTouchHelper.onFocusChanged(z2, i3, rect);
        }
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 7) {
            setCloseIconHovered(getCloseIconTouchBounds().contains(motionEvent.getX(), motionEvent.getY()));
        } else if (actionMasked == 10) {
            setCloseIconHovered(false);
        }
        return super.onHoverEvent(motionEvent);
    }

    /* JADX WARN: Code duplicated, block: B:27:0x00dd  */
    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        CharSequence charSequenceConcat;
        char c3;
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(getAccessibilityClassName());
        accessibilityNodeInfo.setCheckable(isCheckable());
        accessibilityNodeInfo.setClickable(isClickable());
        CharSequence text = getText();
        String string = getContext().getString(R.string.coui_chip_count_separator);
        String string2 = getContext().getString(R.string.coui_chip_tab_unit);
        boolean z2 = this instanceof COUIChipTab;
        if (z2) {
            charSequenceConcat = TextUtils.concat(text, string, ((Object) text) + string2);
        } else {
            charSequenceConcat = text;
        }
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable == null || !cOUIChipDrawable.showsCount()) {
            c3 = 2;
        } else {
            CharSequence countText = this.mChipDrawable.getCountText();
            if (TextUtils.isEmpty(countText)) {
                c3 = 2;
            } else {
                try {
                    String string3 = String.format(getContext().getString(R.string.coui_chip_count_format), countText);
                    if (countText.toString().contains("+")) {
                        int iIndexOf = string3.indexOf("+");
                        StringBuilder sb = new StringBuilder();
                        c3 = 2;
                        try {
                            sb.append(string3.substring(0, iIndexOf));
                            sb.append(" ");
                            sb.append(string3.substring(iIndexOf));
                            string3 = sb.toString();
                        } catch (Exception e3) {
                            e = e3;
                            CharSequence[] charSequenceArr = new CharSequence[3];
                            charSequenceArr[0] = text;
                            charSequenceArr[1] = " ";
                            charSequenceArr[c3] = countText;
                            charSequenceConcat = TextUtils.concat(charSequenceArr);
                            if (DEBUG) {
                                Log.w(TAG, "Failed to load string resources, using default format", e);
                            }
                        }
                    } else {
                        c3 = 2;
                    }
                    CharSequence[] charSequenceArr2 = new CharSequence[3];
                    charSequenceArr2[0] = charSequenceConcat;
                    charSequenceArr2[1] = string;
                    charSequenceArr2[c3] = string3;
                    charSequenceConcat = TextUtils.concat(charSequenceArr2);
                } catch (Exception e4) {
                    e = e4;
                    c3 = 2;
                }
            }
        }
        if (z2) {
            String str = String.format(getContext().getString(R.string.coui_chip_tab_position_in_group), Integer.valueOf(getChipPositionInParent()), Integer.valueOf(getTotalChipCountInParent())) + string;
            CharSequence[] charSequenceArr3 = new CharSequence[3];
            charSequenceArr3[0] = charSequenceConcat;
            charSequenceArr3[1] = string;
            charSequenceArr3[c3] = str;
            charSequenceConcat = TextUtils.concat(charSequenceArr3);
        }
        if (DEBUG) {
            Log.d(TAG, "onInitializeAccessibilityNodeInfo accessibilityText = " + ((Object) charSequenceConcat));
        }
        accessibilityNodeInfo.setText(charSequenceConcat);
        if (getParent() instanceof COUIChipGroup) {
            COUIChipGroup cOUIChipGroup = (COUIChipGroup) getParent();
            AccessibilityNodeInfoCompat.wrap(accessibilityNodeInfo).setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(cOUIChipGroup.getRowIndex(this), 1, cOUIChipGroup.isSingleLine() ? cOUIChipGroup.indexOfChips(this) : -1, 1, false, isChecked()));
            if (cOUIChipGroup.isSingleSelection() && isChecked()) {
                accessibilityNodeInfo.setClickable(false);
                accessibilityNodeInfo.removeAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK);
            }
            if (cOUIChipGroup.isSingleSelection()) {
                accessibilityNodeInfo.setStateDescription(isChecked() ? getContext().getResources().getString(com.support.appcompat.R.string.coui_accessibility_chosen) : getContext().getResources().getString(com.support.appcompat.R.string.coui_accessibility_unchosen));
            } else {
                accessibilityNodeInfo.setStateDescription(isChecked() ? getContext().getResources().getString(com.support.appcompat.R.string.coui_accessibility_selected) : getContext().getResources().getString(com.support.appcompat.R.string.coui_accessibility_unselected));
            }
        }
    }

    @Override // android.widget.TextView, android.view.View
    public void onMeasure(int i3, int i4) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable == null) {
            super.onMeasure(i3, i4);
            return;
        }
        int intrinsicWidth = cOUIChipDrawable.getIntrinsicWidth();
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(intrinsicWidth, 1073741824), i4);
        int intrinsicWidth2 = this.mChipDrawable.getIntrinsicWidth();
        if (intrinsicWidth2 != intrinsicWidth) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(intrinsicWidth2, 1073741824), i4);
        }
    }

    @Override // android.widget.Button, android.widget.TextView, android.view.View
    public PointerIcon onResolvePointerIcon(MotionEvent motionEvent, int i3) {
        if (getCloseIconTouchBounds().contains(motionEvent.getX(), motionEvent.getY()) && isEnabled()) {
            return PointerIcon.getSystemIcon(getContext(), 1002);
        }
        return null;
    }

    @Override // android.widget.TextView, android.view.View
    public void onRtlPropertiesChanged(int i3) {
        super.onRtlPropertiesChanged(i3);
        if (this.mLastLayoutDirection != i3) {
            this.mLastLayoutDirection = i3;
            updatePaddingInternal();
        }
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean zHandleActionDown;
        if (!isEnabled()) {
            return super.onTouchEvent(motionEvent);
        }
        boolean zContains = getCloseIconTouchBounds().contains(motionEvent.getX(), motionEvent.getY());
        int action = motionEvent.getAction();
        if (action == 0) {
            zHandleActionDown = handleActionDown(zContains);
        } else if (action == 1) {
            zHandleActionDown = handleActionUp();
        } else if (action != 2) {
            if (action == 3) {
                handleActionCancel();
            }
            zHandleActionDown = false;
        } else {
            zHandleActionDown = handleActionMove(zContains);
        }
        return zHandleActionDown || super.onTouchEvent(motionEvent);
    }

    public boolean performCloseIconClick() {
        boolean z2 = false;
        playSoundEffect(0);
        View.OnClickListener onClickListener = this.mOnCloseIconClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(this);
            z2 = true;
        }
        if (this.mTouchHelperEnabled) {
            this.mChipTouchHelper.sendEventForVirtualView(1, 1);
        }
        return z2;
    }

    public void refresh() {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.refresh(getContext());
        }
    }

    @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
    public void resetChipGroupAnimations() {
        COUIChipGroup.IChipGroupAnimator iChipGroupAnimator = this.mChipGroupAnimator;
        if (iChipGroupAnimator != null) {
            iChipGroupAnimator.resetChipGroupAnimations();
        }
    }

    public void setAccessibilityClassName(CharSequence charSequence) {
        this.mAccessibilityClassName = charSequence;
    }

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        if (drawable != getBackgroundDrawable()) {
            Log.w(TAG, "Do not set the background; COUIChip manages its own background drawable.");
        } else {
            super.setBackground(drawable);
        }
    }

    @Override // android.view.View
    public void setBackgroundColor(int i3) {
        Log.w(TAG, "Do not set the background color; COUIChip manages its own background drawable.");
    }

    @Override // androidx.appcompat.widget.AppCompatCheckBox, android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        if (drawable != getBackgroundDrawable()) {
            Log.w(TAG, "Do not set the background drawable; COUIChip manages its own background drawable.");
        } else {
            super.setBackgroundDrawable(drawable);
        }
    }

    @Override // androidx.appcompat.widget.AppCompatCheckBox, android.view.View
    public void setBackgroundResource(int i3) {
        Log.w(TAG, "Do not set the background resource; COUIChip manages its own background drawable.");
    }

    @Override // android.view.View
    public void setBackgroundTintList(ColorStateList colorStateList) {
        Log.w(TAG, "Do not set the background tint list; COUIChip manages its own background drawable.");
    }

    @Override // android.view.View
    public void setBackgroundTintMode(PorterDuff.Mode mode) {
        Log.w(TAG, "Do not set the background tint mode; COUIChip manages its own background drawable.");
    }

    public void setCheckable(boolean z2) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setCheckable(z2);
        }
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean z2) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable == null) {
            this.mDeferredCheckedValue = z2;
        } else if (cOUIChipDrawable.isCheckable()) {
            super.setChecked(z2);
        }
    }

    public void setCheckedBackgroundColor(int i3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setCheckedBackgroundColor(i3);
        }
    }

    public void setCheckedChipIconTint(int i3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setCheckedChipIconTint(i3);
        }
    }

    public void setCheckedDisabledBackgroundColor(int i3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setCheckedDisabledBackgroundColor(i3);
        }
    }

    public void setCheckedDisabledChipIconTint(int i3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setCheckedDisabledChipIconTint(i3);
        }
    }

    public void setCheckedDisabledTextColor(int i3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setCheckedDisabledTextColor(i3);
        }
    }

    public void setCheckedTextColor(int i3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setCheckedTextColor(i3);
        }
    }

    public void setChipCornerRadius(float f3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setChipCornerRadius(f3);
        }
    }

    public final void setChipDrawable(COUIChipDrawable cOUIChipDrawable) {
        COUIChipDrawable cOUIChipDrawable2 = this.mChipDrawable;
        if (cOUIChipDrawable2 != cOUIChipDrawable) {
            unapplyChipDrawable(cOUIChipDrawable2);
            this.mChipDrawable = cOUIChipDrawable;
            cOUIChipDrawable.setShouldDrawText(false);
            applyChipDrawable(this.mChipDrawable);
            updateBackgroundDrawable();
        }
    }

    public void setChipEndPadding(float f3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setChipEndPadding(f3);
        }
    }

    public void setChipIcon(Drawable drawable) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setChipIcon(drawable);
        }
    }

    public void setChipIconEndPadding(float f3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setChipIconEndPadding(f3);
        }
    }

    public void setChipIconSize(float f3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setChipIconSize(f3);
        }
    }

    public void setChipIconStartPadding(float f3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setChipIconStartPadding(f3);
        }
    }

    public void setChipIconVisible(boolean z2) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setChipIconVisible(z2);
        }
    }

    public void setChipMinHeight(float f3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setChipMinHeight(f3);
        }
        setMinimumHeight((int) f3);
    }

    public void setChipStartPadding(float f3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setChipStartPadding(f3);
        }
    }

    public void setCloseIcon(Drawable drawable) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setCloseIcon(drawable);
        }
        updateAccessibilityDelegate();
    }

    public void setCloseIconContentDescription(CharSequence charSequence) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setCloseIconContentDescription(charSequence);
        }
    }

    public void setCloseIconEndPadding(float f3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setCloseIconEndPadding(f3);
        }
    }

    public void setCloseIconSize(float f3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setCloseIconSize(f3);
        }
    }

    public void setCloseIconStartPadding(float f3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setCloseIconStartPadding(f3);
        }
    }

    public void setCloseIconVisible(boolean z2) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setCloseIconVisible(z2);
        }
        updateAccessibilityDelegate();
        requestLayout();
    }

    @Override // androidx.appcompat.widget.AppCompatCheckBox, android.widget.TextView
    public void setCompoundDrawables(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (drawable3 != null) {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        super.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
    }

    @Override // androidx.appcompat.widget.AppCompatCheckBox, android.widget.TextView
    public void setCompoundDrawablesRelative(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (drawable3 != null) {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        super.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(int i3, int i4, int i5, int i6) {
        if (i3 != 0) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (i5 != 0) {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(i3, i4, i5, i6);
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesWithIntrinsicBounds(int i3, int i4, int i5, int i6) {
        if (i3 != 0) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (i5 != 0) {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        super.setCompoundDrawablesWithIntrinsicBounds(i3, i4, i5, i6);
    }

    public void setCountText(CharSequence charSequence) {
        boolean z2 = DEBUG;
        if (z2) {
            Log.d(TAG, "=== setCountText ===");
            Log.d(TAG, "Count text: " + ((Object) charSequence));
        }
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.showsCount();
            this.mChipDrawable.setCountText(charSequence);
            this.mChipDrawable.showsCount();
        } else if (z2) {
            Log.w(TAG, "ChipDrawable is null, cannot set count text");
        }
    }

    public void setCountTextColor(int i3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setCountTextColor(i3);
        }
    }

    public void setCountVisible(boolean z2) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.isCountVisible();
            this.mChipDrawable.setCountVisible(z2);
        }
    }

    @Override // android.widget.TextView
    public final void setEllipsize(TextUtils.TruncateAt truncateAt) {
        if (this.mChipDrawable == null) {
            return;
        }
        if (truncateAt == TextUtils.TruncateAt.MARQUEE) {
            throw new UnsupportedOperationException("Text within a chip are not allowed to scroll.");
        }
        super.setEllipsize(truncateAt);
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setEllipsize(truncateAt);
        }
    }

    @Override // android.widget.TextView
    public final void setGravity(int i3) {
        if (i3 != 8388627) {
            Log.w(TAG, "Chip text must be vertically center and start aligned");
        } else {
            super.setGravity(i3);
        }
    }

    @Override // com.coui.appcompat.chip.COUICheckable
    public void setInternalOnCheckedChangeListener(COUICheckable.OnCheckedChangeListener<COUIChip> onCheckedChangeListener) {
        this.mOnCheckedChangeListenerInternal = onCheckedChangeListener;
    }

    @Override // android.view.View
    public void setLayoutDirection(int i3) {
        if (this.mChipDrawable == null) {
            return;
        }
        super.setLayoutDirection(i3);
    }

    @Override // android.widget.TextView
    public final void setLines(int i3) {
        if (i3 > 1) {
            throw new UnsupportedOperationException("COUIChip does not support multi-line text");
        }
        super.setLines(i3);
    }

    @Override // android.widget.TextView
    public void setMaxLines(int i3) {
        if (i3 > 1) {
            throw new UnsupportedOperationException("COUIChip does not support multi-line text");
        }
        super.setMaxLines(i3);
    }

    @Override // android.widget.TextView
    public void setMaxWidth(int i3) {
        super.setMaxWidth(i3);
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setMaxWidth(i3);
        }
    }

    @Override // android.widget.TextView
    public void setMinLines(int i3) {
        if (i3 > 1) {
            throw new UnsupportedOperationException("COUIChip does not support multi-line text");
        }
        super.setMinLines(i3);
    }

    @Override // android.widget.CompoundButton
    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        this.mOnCheckedChangeListener = onCheckedChangeListener;
    }

    public void setOnCloseIconClickListener(View.OnClickListener onClickListener) {
        this.mOnCloseIconClickListener = onClickListener;
        updateAccessibilityDelegate();
    }

    public void setShowRedDot(boolean z2) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setShowRedDot(z2);
        }
    }

    @Override // android.widget.TextView
    public void setSingleLine(boolean z2) {
        if (!z2) {
            throw new UnsupportedOperationException("COUIChip does not support multi-line text");
        }
        super.setSingleLine(z2);
    }

    @Override // android.widget.TextView
    public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable == null) {
            return;
        }
        if (charSequence == null) {
            charSequence = "";
        }
        super.setText(cOUIChipDrawable.shouldDrawText() ? null : charSequence, bufferType);
        COUIChipDrawable cOUIChipDrawable2 = this.mChipDrawable;
        if (cOUIChipDrawable2 != null) {
            cOUIChipDrawable2.setText(charSequence);
        }
    }

    public void setTextEndPadding(float f3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setTextEndPadding(f3);
        }
    }

    public void setTextMaxWidth(int i3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setTextMaxWidth(i3);
        } else {
            Log.w(TAG, "Chip drawable not set!");
        }
    }

    @Override // android.widget.TextView
    public void setTextSize(int i3, float f3) {
        super.setTextSize(i3, f3);
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setTextSize(getTextSize());
        }
    }

    public void setTextStartPadding(float f3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setTextStartPadding(f3);
        }
    }

    public void setUncheckedBackgroundColor(int i3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setUncheckedBackgroundColor(i3);
        }
    }

    public void setUncheckedChipIconTint(int i3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setUncheckedChipIconTint(i3);
        }
    }

    public void setUncheckedDisabledBackgroundColor(int i3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setUncheckedDisabledBackgroundColor(i3);
        }
    }

    public void setUncheckedDisabledChipIconTint(int i3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setUncheckedDisabledChipIconTint(i3);
        }
    }

    public void setUncheckedDisabledTextColor(int i3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setUncheckedDisabledTextColor(i3);
        }
    }

    public void setUncheckedTextColor(int i3) {
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setUncheckedTextColor(i3);
        }
    }

    @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
    public void unbindController() {
        COUIChipGroup.IChipGroupAnimator iChipGroupAnimator = this.mChipGroupAnimator;
        if (iChipGroupAnimator != null) {
            iChipGroupAnimator.unbindController();
        }
    }

    @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
    public void updateAttachState(boolean z2, boolean z3) {
        COUIChipGroup.IChipGroupAnimator iChipGroupAnimator = this.mChipGroupAnimator;
        if (iChipGroupAnimator != null) {
            iChipGroupAnimator.updateAttachState(z2, z3);
        } else {
            this.mIsAttachedToChipGroup = z2;
        }
    }

    @Override // com.coui.appcompat.chip.COUIChipGroup.IChipGroupAnimator
    public void updateChipRealBounds(int i3, int i4, int i5, int i6, boolean z2) {
        COUIChipGroup.IChipGroupAnimator iChipGroupAnimator = this.mChipGroupAnimator;
        if (iChipGroupAnimator != null) {
            iChipGroupAnimator.updateChipRealBounds(i3, i4, i5, i6, z2);
        }
    }

    public COUIChip(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiChipStyle);
    }

    public COUIChip(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, DEF_STYLE_RES);
    }

    public COUIChip(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3);
        this.mCloseIconTouchBounds = new RectF();
        this.mCloseIconTouchBoundsInt = new Rect();
        this.mTextTranslationX = 0.0f;
        this.mTextTranslationY = 0.0f;
        this.mIsAttachedToChipGroup = false;
        this.mTouchHelperEnabled = true;
        this.mChipGroupAnimator = null;
        validateAttributes(attributeSet);
        COUIChipDrawable cOUIChipDrawableCreateFromAttributes = COUIChipDrawable.createFromAttributes(context, attributeSet, i3, i4);
        setChipDrawable(cOUIChipDrawableCreateFromAttributes);
        this.mChipTouchHelper = new ChipTouchHelper(this);
        updateAccessibilityDelegate();
        this.mPressFeedbackHelper = new COUIPressFeedbackHelper(this);
        setDefaultFocusHighlightEnabled(false);
        COUIDarkModeUtil.setForceDarkAllow(this, false);
        setChecked(this.mDeferredCheckedValue);
        setText(cOUIChipDrawableCreateFromAttributes.getText());
        setEllipsize(cOUIChipDrawableCreateFromAttributes.getEllipsize());
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null && !cOUIChipDrawable.shouldDrawText()) {
            setLines(1);
            setHorizontallyScrolling(true);
        }
        setGravity(NavigationBarView.ITEM_GRAVITY_START_CENTER);
        updatePaddingInternal();
        this.mLastLayoutDirection = ViewCompat.getLayoutDirection(this);
        super.setOnCheckedChangeListener(new b(this));
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (drawable3 == null) {
            super.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
            return;
        }
        throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set left drawable using R.attr#chipIcon.");
        }
        if (drawable3 == null) {
            super.setCompoundDrawablesWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
            return;
        }
        throw new UnsupportedOperationException("Please set right drawable using R.attr#closeIcon.");
    }

    @Override // android.widget.TextView
    public void setTextSize(float f3) {
        super.setTextSize(f3);
        COUIChangeTextUtil.adaptFontSize(this, 4);
        COUIChipDrawable cOUIChipDrawable = this.mChipDrawable;
        if (cOUIChipDrawable != null) {
            cOUIChipDrawable.setTextSize(getTextSize());
        }
    }
}