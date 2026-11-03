package com.coui.appcompat.chip;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.scrollview.COUIHorizontalScrollView;
import com.coui.appcompat.view.ViewNative;
import com.oplus.accountsdk.service.sdk.AcCheckUpgradeUtil;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\"\u0018\u0000 A2\u00020\u0001:\u0002ABB%\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\rH\u0002J\b\u0010\u001d\u001a\u00020\u000eH\u0002J\n\u0010\u001e\u001a\u0004\u0018\u00010\u0017H\u0002J\u0010\u0010\u001f\u001a\u00020\u00072\u0006\u0010 \u001a\u00020!H\u0002J\u0010\u0010\"\u001a\u00020\u00072\u0006\u0010#\u001a\u00020\u0017H\u0002J\b\u0010$\u001a\u0004\u0018\u00010\rJ\u0006\u0010%\u001a\u00020\u0007J\b\u0010&\u001a\u00020\u000eH\u0002J\b\u0010'\u001a\u00020\u000eH\u0014J\b\u0010(\u001a\u00020\u000eH\u0014J0\u0010)\u001a\u00020\u000e2\u0006\u0010*\u001a\u00020\n2\u0006\u0010+\u001a\u00020\u00072\u0006\u0010,\u001a\u00020\u00072\u0006\u0010-\u001a\u00020\u00072\u0006\u0010.\u001a\u00020\u0007H\u0014J\u0018\u0010/\u001a\u00020\u000e2\u0006\u00100\u001a\u00020\u00072\u0006\u00101\u001a\u00020\u0007H\u0014J\u0012\u00102\u001a\u00020\u000e2\b\u0010 \u001a\u0004\u0018\u00010!H\u0016J\u0006\u00103\u001a\u00020\u000eJ\u0018\u00104\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\r2\u0006\u00105\u001a\u00020\u001bH\u0002J$\u00106\u001a\u00020\u000e2\u001c\u00107\u001a\u0018\u0012\u0006\u0012\u0004\u0018\u00010\r\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u000e\u0018\u00010\fJ\u001c\u00108\u001a\u00020\u000e2\b\u00109\u001a\u0004\u0018\u00010\r2\b\b\u0002\u0010:\u001a\u00020\nH\u0002J\u0018\u0010;\u001a\u00020\u000e2\u0006\u0010<\u001a\u00020\u00072\b\b\u0002\u0010:\u001a\u00020\nJ\u0010\u0010=\u001a\u00020\u000e2\u0006\u00109\u001a\u00020\rH\u0002J\b\u0010>\u001a\u00020\u000eH\u0002J\u0010\u0010?\u001a\u00020\u000e2\u0006\u00109\u001a\u00020\rH\u0002J\u0010\u0010@\u001a\u00020\n2\u0006\u0010#\u001a\u00020\u0017H\u0002R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R$\u0010\u000b\u001a\u0018\u0012\u0006\u0012\u0004\u0018\u00010\r\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u000e\u0018\u00010\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006C"}, d2 = {"Lcom/coui/appcompat/chip/COUIChipTabGroup;", "Lcom/coui/appcompat/chip/COUIChipGroup;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "isUpdatingSelection", "", "onSelectionChangeListener", "Lkotlin/Function2;", "Lcom/coui/appcompat/chip/COUIChip;", "", "scrollAnimation", "Lcom/coui/appcompat/animation/dynamicanimation/COUISpringAnimation;", "scrollAnimationSpring", "Lcom/coui/appcompat/animation/dynamicanimation/COUISpringForce;", "scrollValueHolder", "Landroidx/dynamicanimation/animation/FloatValueHolder;", "selectedChip", "targetScrollView", "Lcom/coui/appcompat/scrollview/COUIHorizontalScrollView;", "visibleRect", "Landroid/graphics/Rect;", "checkOcclusion", "Lcom/coui/appcompat/chip/COUIChipTabGroup$OcclusionType;", "chipTab", "ensureSingleSelection", "findHorizontalScrollViewParent", "getChildMeasuredWidth", AcCheckUpgradeUtil.TYPE_CHILD, "Landroid/view/View;", "getScrollRange", "scrollView", "getSelectedChip", "getSelectedChipIndex", "initScrollAnimation", "onDetachedFromWindow", "onFinishInflate", "onLayout", "changed", "left", "top", "right", "bottom", "onMeasure", "widthMeasureSpec", "heightMeasureSpec", "onViewAdded", "refresh", "scrollToChipTabWithAnimation", "occlusionType", "setOnSelectionChangeListener", "listener", "setSelectedChip", "chip", "notifyListener", "setSelectedChipByIndex", "index", "setupChipClickListener", "setupClickListenersForAllChips", "updateChipBounds", "updateContentVisibleRect", "Companion", "OcclusionType", "coui-support-chip_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class COUIChipTabGroup extends COUIChipGroup {
    private static final float SCROLL_ANIMATION_BOUNCE = 0.2f;
    private static final float SCROLL_ANIMATION_RESPONSE = 0.4f;
    private static final String TAG = "COUIChipTabGroup";
    private boolean isUpdatingSelection;
    private Function2<? super COUIChip, ? super Integer, Unit> onSelectionChangeListener;
    private COUISpringAnimation scrollAnimation;
    private COUISpringForce scrollAnimationSpring;
    private final FloatValueHolder scrollValueHolder;
    private COUIChip selectedChip;
    private COUIHorizontalScrollView targetScrollView;
    private final Rect visibleRect;

    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0082\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005¨\u0006\u0006"}, d2 = {"Lcom/coui/appcompat/chip/COUIChipTabGroup$OcclusionType;", "", "(Ljava/lang/String;I)V", "NONE", "LEFT", "RIGHT", "coui-support-chip_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public enum OcclusionType {
        NONE,
        LEFT,
        RIGHT;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

        public static EnumEntries<OcclusionType> getEntries() {
            return $ENTRIES;
        }
    }

    @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[OcclusionType.values().length];
            try {
                iArr[OcclusionType.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[OcclusionType.RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[OcclusionType.NONE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIChipTabGroup(Context context) {
        this(context, null, 0, 6, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    private final OcclusionType checkOcclusion(COUIChip chipTab) {
        COUIHorizontalScrollView cOUIHorizontalScrollViewFindHorizontalScrollViewParent = findHorizontalScrollViewParent();
        if (cOUIHorizontalScrollViewFindHorizontalScrollViewParent != null && updateContentVisibleRect(cOUIHorizontalScrollViewFindHorizontalScrollViewParent)) {
            IntRange intRange = new IntRange(chipTab.getLeft() + 1, chipTab.getRight() - 1);
            int first = intRange.getFirst();
            int last = intRange.getLast();
            int i3 = this.visibleRect.left;
            if (first <= i3 && i3 <= last) {
                return OcclusionType.LEFT;
            }
            int first2 = intRange.getFirst();
            int last2 = intRange.getLast();
            int i4 = this.visibleRect.right;
            return (first2 > i4 || i4 > last2) ? OcclusionType.NONE : OcclusionType.RIGHT;
        }
        return OcclusionType.NONE;
    }

    private final void ensureSingleSelection() {
        int chipCount = getChipCount();
        COUIChip cOUIChip = null;
        for (int i3 = 0; i3 < chipCount; i3++) {
            COUIChip chipAt = getChipAt(i3);
            if (chipAt != null && chipAt.isChecked()) {
                if (cOUIChip == null) {
                    cOUIChip = chipAt;
                } else {
                    chipAt.setChecked(false);
                    Log.d(TAG, "发现多个选中的 chip，取消: " + ((Object) chipAt.getText()));
                }
            }
        }
        this.selectedChip = cOUIChip;
        StringBuilder sb = new StringBuilder();
        sb.append("确保单选: 选中的 chip=");
        COUIChip cOUIChip2 = this.selectedChip;
        sb.append((Object) (cOUIChip2 != null ? cOUIChip2.getText() : null));
        Log.d(TAG, sb.toString());
    }

    public static /* synthetic */ void f(COUIChipTabGroup cOUIChipTabGroup, View view) {
        setupChipClickListener$lambda$8(cOUIChipTabGroup, view);
    }

    private final COUIHorizontalScrollView findHorizontalScrollViewParent() {
        for (ViewParent parent = getParent(); parent != null && (parent instanceof ViewGroup); parent = ((ViewGroup) parent).getParent()) {
            if (parent instanceof COUIHorizontalScrollView) {
                return (COUIHorizontalScrollView) parent;
            }
        }
        return null;
    }

    public static /* synthetic */ void g(COUIChipTabGroup cOUIChipTabGroup, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        initScrollAnimation$lambda$5$lambda$4(cOUIChipTabGroup, cOUIDynamicAnimation, z2, f3, f4);
    }

    private final int getChildMeasuredWidth(View child) {
        if (child instanceof COUIChip) {
            COUIChip cOUIChip = (COUIChip) child;
            ViewGroup.LayoutParams layoutParams = cOUIChip.getLayoutParams();
            if (layoutParams != null && layoutParams.width >= 0) {
                return cOUIChip.getMeasuredWidth();
            }
            COUIChipDrawable chipDrawable = cOUIChip.getChipDrawable();
            if (chipDrawable != null) {
                int[] iArr = new int[2];
                chipDrawable.getDeterminateChipSize(iArr);
                return iArr[0];
            }
        }
        return child.getMeasuredWidth();
    }

    private final int getScrollRange(COUIHorizontalScrollView scrollView) {
        View childAt = scrollView.getChildAt(0);
        return Math.max(0, (childAt != null ? childAt.getWidth() : 0) - ((scrollView.getWidth() - scrollView.getPaddingLeft()) - scrollView.getPaddingRight()));
    }

    public static /* synthetic */ void h(COUIChipTabGroup cOUIChipTabGroup, COUISpringAnimation cOUISpringAnimation, COUIHorizontalScrollView cOUIHorizontalScrollView, int i3, int i4, int i5) {
        scrollToChipTabWithAnimation$lambda$11$lambda$10(cOUIChipTabGroup, cOUISpringAnimation, cOUIHorizontalScrollView, i3, i4, i5);
    }

    public static /* synthetic */ void i(COUIChipTabGroup cOUIChipTabGroup, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        initScrollAnimation$lambda$5$lambda$2(cOUIChipTabGroup, cOUIDynamicAnimation, f3, f4);
    }

    private final void initScrollAnimation() {
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setResponse(0.4f);
        cOUISpringForce.setBounce(0.2f);
        this.scrollAnimationSpring = cOUISpringForce;
        COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(this.scrollValueHolder);
        cOUISpringAnimation.setSpring(this.scrollAnimationSpring);
        cOUISpringAnimation.addUpdateListener(new n(this));
        cOUISpringAnimation.addEndListener(new o(this));
        this.scrollAnimation = cOUISpringAnimation;
    }

    private static final void initScrollAnimation$lambda$5$lambda$2(COUIChipTabGroup this$0, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        COUIHorizontalScrollView cOUIHorizontalScrollView = this$0.targetScrollView;
        if (cOUIHorizontalScrollView != null) {
            ViewNative.setScrollX(cOUIHorizontalScrollView, (int) f3);
        }
    }

    private static final void initScrollAnimation$lambda$5$lambda$4(COUIChipTabGroup this$0, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        COUIHorizontalScrollView cOUIHorizontalScrollView;
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (z2 || (cOUIHorizontalScrollView = this$0.targetScrollView) == null) {
            return;
        }
        Log.d(TAG, "滚动验证: 目标=" + ((int) f3) + ", 实际=" + cOUIHorizontalScrollView.getScrollX());
    }

    private final void scrollToChipTabWithAnimation(COUIChip chipTab, OcclusionType occlusionType) {
        int i3;
        COUIHorizontalScrollView cOUIHorizontalScrollViewFindHorizontalScrollViewParent = findHorizontalScrollViewParent();
        if (cOUIHorizontalScrollViewFindHorizontalScrollViewParent == null) {
            return;
        }
        this.targetScrollView = cOUIHorizontalScrollViewFindHorizontalScrollViewParent;
        if (cOUIHorizontalScrollViewFindHorizontalScrollViewParent.getOverScrollMode() != 0) {
            cOUIHorizontalScrollViewFindHorizontalScrollViewParent.setOverScrollMode(0);
        }
        if (updateContentVisibleRect(cOUIHorizontalScrollViewFindHorizontalScrollViewParent)) {
            int left = chipTab.getLeft();
            int right = chipTab.getRight();
            int i4 = WhenMappings.$EnumSwitchMapping$0[occlusionType.ordinal()];
            if (i4 == 1) {
                i3 = left - this.visibleRect.left;
            } else {
                if (i4 != 2) {
                    if (i4 != 3) {
                        throw new NoWhenBranchMatchedException();
                    }
                    return;
                }
                i3 = right - this.visibleRect.right;
            }
            int i5 = i3;
            int scrollRange = getScrollRange(cOUIHorizontalScrollViewFindHorizontalScrollViewParent);
            boolean localVisibleRect = cOUIHorizontalScrollViewFindHorizontalScrollViewParent.getLocalVisibleRect(this.visibleRect);
            int scrollX = localVisibleRect ? this.visibleRect.left - cOUIHorizontalScrollViewFindHorizontalScrollViewParent.getScrollX() : 0;
            int scrollX2 = localVisibleRect ? this.visibleRect.right - cOUIHorizontalScrollViewFindHorizontalScrollViewParent.getScrollX() : cOUIHorizontalScrollViewFindHorizontalScrollViewParent.getWidth();
            int i6 = -Math.max(0, scrollX);
            int iMax = scrollRange + Math.max(0, cOUIHorizontalScrollViewFindHorizontalScrollViewParent.getWidth() - scrollX2);
            COUISpringAnimation cOUISpringAnimation = this.scrollAnimation;
            if (cOUISpringAnimation != null) {
                if (cOUISpringAnimation.isRunning()) {
                    cOUISpringAnimation.cancel();
                }
                cOUIHorizontalScrollViewFindHorizontalScrollViewParent.post(new p(this, cOUISpringAnimation, cOUIHorizontalScrollViewFindHorizontalScrollViewParent, i5, i6, iMax));
            }
        }
    }

    private static final void scrollToChipTabWithAnimation$lambda$11$lambda$10(COUIChipTabGroup this$0, COUISpringAnimation animation, COUIHorizontalScrollView scrollView, int i3, int i4, int i5) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(animation, "$animation");
        Intrinsics.checkNotNullParameter(scrollView, "$scrollView");
        COUISpringForce cOUISpringForce = this$0.scrollAnimationSpring;
        if (cOUISpringForce != null) {
            cOUISpringForce.setResponse(0.4f);
            cOUISpringForce.setBounce(0.2f);
        }
        animation.setSpring(this$0.scrollAnimationSpring);
        int scrollX = scrollView.getScrollX();
        this$0.scrollValueHolder.setValue(scrollX);
        animation.animateToFinalPosition(RangesKt.coerceIn(scrollX + i3, i4, i5));
    }

    private final void setSelectedChip(COUIChip chip, boolean notifyListener) {
        if (this.isUpdatingSelection) {
            return;
        }
        if (chip == null) {
            Log.w(TAG, "⚠ 不允许取消所有选中，必须保持至少一个 chip 选中");
            return;
        }
        this.isUpdatingSelection = true;
        try {
            if (Intrinsics.areEqual(chip, this.selectedChip)) {
                Log.d(TAG, "Chip 已经是选中状态，保持不变: " + ((Object) chip.getText()));
                return;
            }
            COUIChip cOUIChip = this.selectedChip;
            if (cOUIChip != null && cOUIChip.isCheckable()) {
                cOUIChip.setChecked(false);
                Log.d(TAG, "取消选中: " + ((Object) cOUIChip.getText()));
            }
            this.selectedChip = chip;
            if (chip.isCheckable()) {
                chip.setChecked(true);
                COUIChipDrawable chipDrawable = chip.getChipDrawable();
                COUIChipTabDrawable cOUIChipTabDrawable = chipDrawable instanceof COUIChipTabDrawable ? (COUIChipTabDrawable) chipDrawable : null;
                if (cOUIChipTabDrawable != null) {
                    cOUIChipTabDrawable.mLastCheckedState = true;
                }
                Log.d(TAG, "选中: " + ((Object) chip.getText()));
            }
            if (notifyListener) {
                int iIndexOfChild = indexOfChild(chip);
                Function2<? super COUIChip, ? super Integer, Unit> function2 = this.onSelectionChangeListener;
                if (function2 != null) {
                    function2.invoke(chip, Integer.valueOf(iIndexOfChild));
                }
                Log.d(TAG, "✓ 触发选中监听器回调: chip=" + ((Object) chip.getText()) + ", index=" + iIndexOfChild);
            }
        } finally {
            this.isUpdatingSelection = false;
        }
    }

    public static /* synthetic */ void setSelectedChip$default(COUIChipTabGroup cOUIChipTabGroup, COUIChip cOUIChip, boolean z2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            z2 = true;
        }
        cOUIChipTabGroup.setSelectedChip(cOUIChip, z2);
    }

    public static /* synthetic */ void setSelectedChipByIndex$default(COUIChipTabGroup cOUIChipTabGroup, int i3, boolean z2, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            z2 = true;
        }
        cOUIChipTabGroup.setSelectedChipByIndex(i3, z2);
    }

    private final void setupChipClickListener(COUIChip chip) {
        chip.setClickable(true);
        chip.setFocusable(true);
        chip.setOnClickListener(new m(this));
    }

    private static final void setupChipClickListener$lambda$8(COUIChipTabGroup this$0, View view) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (view instanceof COUIChip) {
            if (!Intrinsics.areEqual(view, this$0.selectedChip)) {
                COUIChip cOUIChip = (COUIChip) view;
                if (cOUIChip.isCheckable()) {
                    this$0.setSelectedChip(cOUIChip, true);
                }
            }
            COUIChip cOUIChip2 = (COUIChip) view;
            OcclusionType occlusionTypeCheckOcclusion = this$0.checkOcclusion(cOUIChip2);
            Log.d(TAG, "setupChipClickListener: occlusionType = " + occlusionTypeCheckOcclusion);
            if (occlusionTypeCheckOcclusion != OcclusionType.NONE) {
                this$0.scrollToChipTabWithAnimation(cOUIChip2, occlusionTypeCheckOcclusion);
            }
        }
    }

    private final void setupClickListenersForAllChips() {
        int chipCount = getChipCount();
        for (int i3 = 0; i3 < chipCount; i3++) {
            COUIChip chipAt = getChipAt(i3);
            if (chipAt != null) {
                setupChipClickListener(chipAt);
            }
        }
        ensureSingleSelection();
    }

    private final void updateChipBounds(COUIChip chip) {
        chip.updateChipRealBounds(chip.getLeft(), chip.getTop(), chip.getRight(), chip.getBottom(), isChipGroupAnimationsEnabled());
    }

    private final boolean updateContentVisibleRect(COUIHorizontalScrollView scrollView) {
        if (!getLocalVisibleRect(this.visibleRect)) {
            Log.d(TAG, "bounds: group getLocalVisibleRect=false");
            return false;
        }
        int scrollRange = getScrollRange(scrollView);
        boolean z2 = scrollView.getScrollX() > 0;
        boolean z3 = scrollView.getScrollX() < scrollRange;
        int horizontalFadingEdgeLength = scrollView.getHorizontalFadingEdgeLength();
        if (z2) {
            this.visibleRect.left += scrollView.getPaddingLeft() + horizontalFadingEdgeLength;
        }
        if (z3) {
            this.visibleRect.right -= scrollView.getPaddingRight() + horizontalFadingEdgeLength;
        }
        Rect rect = this.visibleRect;
        return rect.left < rect.right;
    }

    public final COUIChip getSelectedChip() {
        return this.selectedChip;
    }

    public final int getSelectedChipIndex() {
        COUIChip cOUIChip = this.selectedChip;
        if (cOUIChip != null) {
            return indexOfChild(cOUIChip);
        }
        return -1;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        COUISpringAnimation cOUISpringAnimation = this.scrollAnimation;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.cancel();
        }
        this.targetScrollView = null;
    }

    @Override // com.coui.appcompat.chip.COUIChipGroup, android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        setupClickListenersForAllChips();
    }

    @Override // com.coui.appcompat.chip.COUIChipGroup, android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int marginEnd;
        int marginStart;
        int chipSpacingHorizontal;
        boolean z2 = ViewCompat.getLayoutDirection(this) == 1;
        int paddingRight = z2 ? (right - left) - getPaddingRight() : getPaddingLeft();
        int paddingTop = getPaddingTop();
        int chipCount = getChipCount();
        for (int i3 = 0; i3 < chipCount; i3++) {
            COUIChip chipAt = getChipAt(i3);
            Intrinsics.checkNotNull(chipAt);
            if (chipAt.getVisibility() != 8 && indexOfChild(chipAt) != -1) {
                ViewGroup.LayoutParams layoutParams = chipAt.getLayoutParams();
                if (layoutParams instanceof COUIChipGroup.LayoutParams) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                    marginStart = MarginLayoutParamsCompat.getMarginStart(marginLayoutParams);
                    marginEnd = MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams);
                } else {
                    marginEnd = 0;
                    marginStart = 0;
                }
                int childMeasuredWidth = getChildMeasuredWidth(chipAt);
                int measuredHeight = chipAt.getMeasuredHeight();
                if (z2) {
                    int i4 = paddingRight - marginStart;
                    int i5 = i4 - childMeasuredWidth;
                    chipAt.layout(i5, paddingTop, i4, measuredHeight + paddingTop);
                    chipSpacingHorizontal = (i5 - marginEnd) - getChipSpacingHorizontal();
                } else {
                    int i6 = paddingRight + marginStart;
                    int i7 = childMeasuredWidth + i6;
                    chipAt.layout(i6, paddingTop, i7, measuredHeight + paddingTop);
                    chipSpacingHorizontal = i7 + marginEnd + getChipSpacingHorizontal();
                }
                updateChipBounds(chipAt);
                paddingRight = chipSpacingHorizontal;
            }
        }
    }

    @Override // com.coui.appcompat.chip.COUIChipGroup, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int marginEnd;
        int marginStart;
        int mode = View.MeasureSpec.getMode(heightMeasureSpec);
        int size = View.MeasureSpec.getSize(heightMeasureSpec);
        int paddingLeft = getPaddingLeft() + getPaddingRight();
        int chipCount = getChipCount();
        int iCoerceAtLeast = 0;
        for (int i3 = 0; i3 < chipCount; i3++) {
            COUIChip chipAt = getChipAt(i3);
            Intrinsics.checkNotNull(chipAt);
            if (chipAt.getVisibility() != 8) {
                measureChild(chipAt, widthMeasureSpec, heightMeasureSpec);
                ViewGroup.LayoutParams layoutParams = chipAt.getLayoutParams();
                if (layoutParams instanceof COUIChipGroup.LayoutParams) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                    marginStart = MarginLayoutParamsCompat.getMarginStart(marginLayoutParams);
                    marginEnd = MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams);
                } else {
                    marginEnd = 0;
                    marginStart = 0;
                }
                paddingLeft += marginStart + getChildMeasuredWidth(chipAt) + marginEnd;
                if (i3 < getChipCount() - 1) {
                    paddingLeft += getChipSpacingHorizontal();
                }
                iCoerceAtLeast = RangesKt.coerceAtLeast(iCoerceAtLeast, chipAt.getMeasuredHeight());
            }
        }
        if (mode == Integer.MIN_VALUE) {
            size = RangesKt.coerceAtMost(iCoerceAtLeast + getPaddingTop() + getPaddingBottom(), size);
        } else if (mode != 1073741824) {
            size = iCoerceAtLeast + getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(paddingLeft, size);
    }

    @Override // com.coui.appcompat.chip.COUIChipGroup, android.view.ViewGroup
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (child instanceof COUIChip) {
            COUIChip cOUIChip = (COUIChip) child;
            setupChipClickListener(cOUIChip);
            if (this.selectedChip == null && cOUIChip.isCheckable() && isSelectionRequired()) {
                setSelectedChip(cOUIChip, false);
                Log.d(TAG, "自动选中第一个 chip: " + ((Object) cOUIChip.getText()));
            }
        }
    }

    public final void refresh() {
        int chipCount = getChipCount();
        for (int i3 = 0; i3 < chipCount; i3++) {
            COUIChip chipAt = getChipAt(i3);
            if (chipAt instanceof COUIChipTab) {
                chipAt.refresh();
            }
        }
        invalidate();
    }

    public final void setOnSelectionChangeListener(Function2<? super COUIChip, ? super Integer, Unit> listener) {
        this.onSelectionChangeListener = listener;
        StringBuilder sb = new StringBuilder();
        sb.append("选中监听器已");
        sb.append(listener != null ? "设置" : "清空");
        Log.d(TAG, sb.toString());
    }

    public final void setSelectedChipByIndex(int index, boolean notifyListener) {
        if (index >= 0 && index < getChipCount()) {
            COUIChip chipAt = getChipAt(index);
            if (chipAt != null) {
                setSelectedChip(chipAt, notifyListener);
                return;
            }
            return;
        }
        Log.w(TAG, "⚠ 索引越界: " + index + ", chipCount=" + getChipCount());
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIChipTabGroup(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public /* synthetic */ COUIChipTabGroup(Context context, AttributeSet attributeSet, int i3, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i4 & 2) != 0 ? null : attributeSet, (i4 & 4) != 0 ? 0 : i3);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUIChipTabGroup(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        Intrinsics.checkNotNullParameter(context, "context");
        this.visibleRect = new Rect();
        this.scrollValueHolder = new FloatValueHolder(0.0f);
        setSingleLine(true);
        initScrollAnimation();
    }
}