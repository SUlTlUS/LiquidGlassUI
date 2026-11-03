package com.coui.appcompat.chip;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.android.settings.slices.SlicesDatabaseHelper;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.Ref;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 -2\u00020\u0001:\u0002-.B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0006\u0010\u001e\u001a\u00020\u000fJ\u0006\u0010\u001f\u001a\u00020\u000fJ\u0010\u0010 \u001a\u00020\u001a2\u0006\u0010!\u001a\u00020\u000eH\u0002J\u0006\u0010\"\u001a\u00020\nJ\u001a\u0010#\u001a\u00020\u000f2\u0006\u0010$\u001a\u00020%2\b\b\u0002\u0010&\u001a\u00020\u000eH\u0002J\u0018\u0010'\u001a\u00020\u000f2\u0006\u0010(\u001a\u00020\u001a2\u0006\u0010&\u001a\u00020\u000eH\u0002J\u001e\u0010)\u001a\u00020\u000f2\u0006\u0010$\u001a\u00020%2\f\u0010*\u001a\b\u0012\u0004\u0012\u00020\u000f0+H\u0002J\u0010\u0010,\u001a\u00020\u000f2\u0006\u0010!\u001a\u00020\u000eH\u0002R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R0\u0010\u000b\u001a\u0018\u0012\u0006\u0012\u0004\u0018\u00010\r\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000f\u0018\u00010\fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u001a0\u0019X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001cX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006/"}, d2 = {"Lcom/coui/appcompat/chip/COUIChipTabGroupFragmentMediator;", "", "chipTabGroup", "Lcom/coui/appcompat/chip/COUIChipTabGroup;", "fragmentSwitcher", "Lcom/coui/appcompat/chip/COUIChipTabGroupFragmentMediator$FragmentSwitcher;", "(Lcom/coui/appcompat/chip/COUIChipTabGroup;Lcom/coui/appcompat/chip/COUIChipTabGroupFragmentMediator$FragmentSwitcher;)V", "animationHandler", "Landroid/os/Handler;", "attached", "", "chipSelectionListener", "Lkotlin/Function2;", "Lcom/coui/appcompat/chip/COUIChip;", "", "", "getChipSelectionListener", "()Lkotlin/jvm/functions/Function2;", "setChipSelectionListener", "(Lkotlin/jvm/functions/Function2;)V", "currentFragmentIndex", "fadeInAnimation", "Lcom/coui/appcompat/animation/dynamicanimation/COUISpringAnimation;", "fadeOutAnimation", "fragmentCache", "", "Landroidx/fragment/app/Fragment;", "pageFadeInSpring", "Lcom/coui/appcompat/animation/dynamicanimation/COUISpringForce;", "pageFadeOutSpring", "attach", "detach", "getOrCreateFragment", "index", "isAttached", "startFadeInAnimation", "view", "Landroid/view/View;", "delayMs", "startFadeInAnimationForFragment", SlicesDatabaseHelper.IndexColumns.FRAGMENT, "startFadeOutAnimation", "onEnd", "Lkotlin/Function0;", "switchToFragment", "Companion", "FragmentSwitcher", "coui-support-chip_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class COUIChipTabGroupFragmentMediator {
    private static final float BOUNCE = 0.0f;
    private static final int PAGE_APPEAR_DELAY = 150;
    private static final float RESPONSE_FADE_IN = 0.3f;
    private static final float RESPONSE_FADE_OUT = 0.2f;
    private static final String TAG = "ChipTabFragmentMediator";
    private final Handler animationHandler;
    private boolean attached;
    private Function2<? super COUIChip, ? super Integer, Unit> chipSelectionListener;
    private final COUIChipTabGroup chipTabGroup;
    private int currentFragmentIndex;
    private COUISpringAnimation fadeInAnimation;
    private COUISpringAnimation fadeOutAnimation;
    private final Map<Integer, Fragment> fragmentCache;
    private final FragmentSwitcher fragmentSwitcher;
    private final COUISpringForce pageFadeInSpring;
    private final COUISpringForce pageFadeOutSpring;

    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u0003H&J\n\u0010\u0007\u001a\u0004\u0018\u00010\bH&J\b\u0010\t\u001a\u00020\u0003H&J\b\u0010\n\u001a\u00020\u000bH&¨\u0006\f"}, d2 = {"Lcom/coui/appcompat/chip/COUIChipTabGroupFragmentMediator$FragmentSwitcher;", "", "getCurrentFragmentIndex", "", "getFragment", "Landroidx/fragment/app/Fragment;", "index", "getFragmentContainer", "Landroid/view/ViewGroup;", "getFragmentCount", "getFragmentManager", "Landroidx/fragment/app/FragmentManager;", "coui-support-chip_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public interface FragmentSwitcher {
        int getCurrentFragmentIndex();

        Fragment getFragment(int index);

        ViewGroup getFragmentContainer();

        int getFragmentCount();

        FragmentManager getFragmentManager();
    }

    /* JADX INFO: renamed from: com.coui.appcompat.chip.COUIChipTabGroupFragmentMediator$attach$1, reason: invalid class name */
    @Metadata(d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\u0010\u0000\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\n¢\u0006\u0002\b\u0006"}, d2 = {"<anonymous>", "", "<anonymous parameter 0>", "Lcom/coui/appcompat/chip/COUIChip;", "index", "", "invoke"}, k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class AnonymousClass1 extends Lambda implements Function2<COUIChip, Integer, Unit> {
        final /* synthetic */ int $fragmentCount;
        final /* synthetic */ COUIChipTabGroupFragmentMediator this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass1(int i3, COUIChipTabGroupFragmentMediator cOUIChipTabGroupFragmentMediator) {
            super(2);
            this.$fragmentCount = i3;
            this.this$0 = cOUIChipTabGroupFragmentMediator;
        }

        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ Unit invoke(COUIChip cOUIChip, Integer num) {
            invoke(cOUIChip, num.intValue());
            return Unit.INSTANCE;
        }

        public final void invoke(COUIChip cOUIChip, int i3) {
            if (i3 < 0 || i3 >= this.$fragmentCount || COUIChipTabGroupFragmentMediator.access$getCurrentFragmentIndex$p(this.this$0) == i3) {
                return;
            }
            Log.d(COUIChipTabGroupFragmentMediator.TAG, "Chip selected: " + i3 + ", switching Fragment");
            COUIChipTabGroupFragmentMediator.access$switchToFragment(this.this$0, i3);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.chip.COUIChipTabGroupFragmentMediator$startFadeInAnimationForFragment$2, reason: invalid class name */
    @Metadata(d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, d2 = {"com/coui/appcompat/chip/COUIChipTabGroupFragmentMediator$startFadeInAnimationForFragment$2", "Landroid/view/ViewTreeObserver$OnGlobalLayoutListener;", "onGlobalLayout", "", "coui-support-chip_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class AnonymousClass2 implements ViewTreeObserver.OnGlobalLayoutListener {
        final /* synthetic */ ViewGroup $containerView;
        final /* synthetic */ Fragment $fragment;
        final /* synthetic */ Function1<View, Unit> $startAnimation;

        /* JADX WARN: Multi-variable type inference failed */
        public AnonymousClass2(Fragment fragment, ViewGroup viewGroup, Function1<? super View, Unit> function1) {
            this.$fragment = fragment;
            this.$containerView = viewGroup;
            this.$startAnimation = function1;
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            View view = this.$fragment.getView();
            if (view != null) {
                ViewGroup viewGroup = this.$containerView;
                Function1<View, Unit> function1 = this.$startAnimation;
                viewGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                function1.invoke(view);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.chip.COUIChipTabGroupFragmentMediator$switchToFragment$2, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "invoke"}, k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class C13502 extends Lambda implements Function0<Unit> {
        final /* synthetic */ FragmentManager $fragmentManager;
        final /* synthetic */ Fragment $oldFragment;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public C13502(Fragment fragment, FragmentManager fragmentManager) {
            super(0);
            this.$oldFragment = fragment;
            this.$fragmentManager = fragmentManager;
        }

        @Override // kotlin.jvm.functions.Function0
        public /* bridge */ /* synthetic */ Unit invoke() {
            invoke2();
            return Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final void invoke2() {
            this.$fragmentManager.beginTransaction().hide(this.$oldFragment).commitAllowingStateLoss();
        }
    }

    public COUIChipTabGroupFragmentMediator(COUIChipTabGroup chipTabGroup, FragmentSwitcher fragmentSwitcher) {
        Intrinsics.checkNotNullParameter(chipTabGroup, "chipTabGroup");
        Intrinsics.checkNotNullParameter(fragmentSwitcher, "fragmentSwitcher");
        this.chipTabGroup = chipTabGroup;
        this.fragmentSwitcher = fragmentSwitcher;
        this.animationHandler = new Handler(Looper.getMainLooper());
        COUISpringForce cOUISpringForce = new COUISpringForce(0.0f);
        cOUISpringForce.setResponse(0.2f);
        cOUISpringForce.setBounce(0.0f);
        this.pageFadeOutSpring = cOUISpringForce;
        COUISpringForce cOUISpringForce2 = new COUISpringForce(1.0f);
        cOUISpringForce2.setResponse(0.3f);
        cOUISpringForce2.setBounce(0.0f);
        this.pageFadeInSpring = cOUISpringForce2;
        this.fragmentCache = new LinkedHashMap();
        this.currentFragmentIndex = -1;
    }

    public static /* synthetic */ void a(COUIChipTabGroupFragmentMediator cOUIChipTabGroupFragmentMediator, View view) {
        switchToFragment$lambda$5$lambda$4(cOUIChipTabGroupFragmentMediator, view);
    }

    public static final /* synthetic */ int access$getCurrentFragmentIndex$p(COUIChipTabGroupFragmentMediator cOUIChipTabGroupFragmentMediator) {
        return cOUIChipTabGroupFragmentMediator.currentFragmentIndex;
    }

    public static final /* synthetic */ void access$startFadeInAnimation(COUIChipTabGroupFragmentMediator cOUIChipTabGroupFragmentMediator, View view, int i3) {
        cOUIChipTabGroupFragmentMediator.startFadeInAnimation(view, i3);
    }

    public static final /* synthetic */ void access$switchToFragment(COUIChipTabGroupFragmentMediator cOUIChipTabGroupFragmentMediator, int i3) {
        cOUIChipTabGroupFragmentMediator.switchToFragment(i3);
    }

    public static /* synthetic */ void b(COUIChipTabGroupFragmentMediator cOUIChipTabGroupFragmentMediator, View view) {
        startFadeInAnimation$lambda$11(cOUIChipTabGroupFragmentMediator, view);
    }

    public static /* synthetic */ void c(Function0 function0, COUIChipTabGroupFragmentMediator cOUIChipTabGroupFragmentMediator, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        startFadeOutAnimation$lambda$8$lambda$7(function0, cOUIChipTabGroupFragmentMediator, cOUIDynamicAnimation, z2, f3, f4);
    }

    public static /* synthetic */ void d(COUIChipTabGroupFragmentMediator cOUIChipTabGroupFragmentMediator, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        startFadeInAnimation$lambda$11$lambda$10$lambda$9(cOUIChipTabGroupFragmentMediator, cOUIDynamicAnimation, z2, f3, f4);
    }

    private final Fragment getOrCreateFragment(int index) {
        Fragment fragment = this.fragmentCache.get(Integer.valueOf(index));
        if (fragment != null) {
            return fragment;
        }
        Fragment fragment2 = this.fragmentSwitcher.getFragment(index);
        if (fragment2 != null) {
            this.fragmentCache.put(Integer.valueOf(index), fragment2);
            return fragment2;
        }
        throw new IllegalStateException("FragmentSwitcher.getFragment(" + index + ") returned null");
    }

    private final void startFadeInAnimation(View view, int delayMs) {
        COUISpringAnimation cOUISpringAnimation = this.fadeInAnimation;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.cancel();
        }
        view.setAlpha(0.0f);
        view.setVisibility(0);
        q qVar = new q(this, view);
        if (delayMs > 0) {
            this.animationHandler.postDelayed(qVar, delayMs);
        } else {
            qVar.run();
        }
    }

    public static /* synthetic */ void startFadeInAnimation$default(COUIChipTabGroupFragmentMediator cOUIChipTabGroupFragmentMediator, View view, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i3 = 0;
        }
        cOUIChipTabGroupFragmentMediator.startFadeInAnimation(view, i3);
    }

    private static final void startFadeInAnimation$lambda$11(COUIChipTabGroupFragmentMediator this$0, View view) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(view, "$view");
        COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(view, COUIDynamicAnimation.ALPHA, 1.0f);
        cOUISpringAnimation.setSpring(this$0.pageFadeInSpring);
        cOUISpringAnimation.addEndListener(new s(this$0));
        this$0.fadeInAnimation = cOUISpringAnimation;
        cOUISpringAnimation.start();
    }

    private static final void startFadeInAnimation$lambda$11$lambda$10$lambda$9(COUIChipTabGroupFragmentMediator this$0, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.fadeInAnimation = null;
    }

    private final void startFadeInAnimationForFragment(Fragment fragment, int delayMs) {
        ViewGroup fragmentContainer = this.fragmentSwitcher.getFragmentContainer();
        COUIChipTabGroupFragmentMediator$startFadeInAnimationForFragment$startAnimation$1 cOUIChipTabGroupFragmentMediator$startFadeInAnimationForFragment$startAnimation$1 = new COUIChipTabGroupFragmentMediator$startFadeInAnimationForFragment$startAnimation$1(new Ref.BooleanRef(), this, delayMs);
        View view = fragment.getView();
        if (view != null) {
            cOUIChipTabGroupFragmentMediator$startFadeInAnimationForFragment$startAnimation$1.invoke(view);
        }
        if (fragment.getView() != null || fragmentContainer == null) {
            return;
        }
        fragmentContainer.getViewTreeObserver().addOnGlobalLayoutListener(new AnonymousClass2(fragment, fragmentContainer, cOUIChipTabGroupFragmentMediator$startFadeInAnimationForFragment$startAnimation$1));
    }

    private final void startFadeOutAnimation(View view, Function0<Unit> onEnd) {
        COUISpringAnimation cOUISpringAnimation = this.fadeOutAnimation;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.cancel();
        }
        COUISpringAnimation cOUISpringAnimation2 = new COUISpringAnimation(view, COUIDynamicAnimation.ALPHA, 0.0f);
        cOUISpringAnimation2.setSpring(this.pageFadeOutSpring);
        cOUISpringAnimation2.addEndListener(new r(onEnd, this));
        this.fadeOutAnimation = cOUISpringAnimation2;
        cOUISpringAnimation2.start();
    }

    private static final void startFadeOutAnimation$lambda$8$lambda$7(Function0 onEnd, COUIChipTabGroupFragmentMediator this$0, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        Intrinsics.checkNotNullParameter(onEnd, "$onEnd");
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        onEnd.invoke();
        this$0.fadeOutAnimation = null;
    }

    private final void switchToFragment(int index) {
        int fragmentCount = this.fragmentSwitcher.getFragmentCount();
        if (index < 0 || index >= fragmentCount) {
            return;
        }
        int i3 = this.currentFragmentIndex;
        if (index != i3 || i3 < 0) {
            COUISpringAnimation cOUISpringAnimation = this.fadeOutAnimation;
            if (cOUISpringAnimation != null) {
                cOUISpringAnimation.cancel();
            }
            COUISpringAnimation cOUISpringAnimation2 = this.fadeInAnimation;
            if (cOUISpringAnimation2 != null) {
                cOUISpringAnimation2.cancel();
            }
            this.animationHandler.removeCallbacksAndMessages(null);
            FragmentManager fragmentManager = this.fragmentSwitcher.getFragmentManager();
            ViewGroup fragmentContainer = this.fragmentSwitcher.getFragmentContainer();
            if (fragmentContainer == null) {
                return;
            }
            Fragment orCreateFragment = getOrCreateFragment(index);
            String str = "fragment_tab_" + index;
            FragmentTransaction fragmentTransactionBeginTransaction = fragmentManager.beginTransaction();
            Intrinsics.checkNotNullExpressionValue(fragmentTransactionBeginTransaction, "beginTransaction(...)");
            if (this.currentFragmentIndex < 0) {
                Fragment fragmentFindFragmentById = fragmentManager.findFragmentById(fragmentContainer.getId());
                if (fragmentFindFragmentById != null) {
                    fragmentTransactionBeginTransaction.remove(fragmentFindFragmentById);
                }
                fragmentTransactionBeginTransaction.add(fragmentContainer.getId(), orCreateFragment, str);
                fragmentTransactionBeginTransaction.commitAllowingStateLoss();
                startFadeInAnimationForFragment(orCreateFragment, 0);
            } else {
                Fragment fragmentFindFragmentByTag = fragmentManager.findFragmentByTag("fragment_tab_" + this.currentFragmentIndex);
                View view = fragmentFindFragmentByTag != null ? fragmentFindFragmentByTag.getView() : null;
                if (view != null && view.getAlpha() > 0.0f) {
                    startFadeOutAnimation(view, new C13502(fragmentFindFragmentByTag, fragmentManager));
                } else if (fragmentFindFragmentByTag != null) {
                    fragmentTransactionBeginTransaction.hide(fragmentFindFragmentByTag);
                }
                Fragment fragmentFindFragmentByTag2 = fragmentManager.findFragmentByTag(str);
                if (fragmentFindFragmentByTag2 != null) {
                    fragmentTransactionBeginTransaction.show(fragmentFindFragmentByTag2);
                    fragmentTransactionBeginTransaction.commitAllowingStateLoss();
                    View view2 = fragmentFindFragmentByTag2.getView();
                    if (view2 != null) {
                        view2.setAlpha(0.0f);
                        view2.post(new t(this, view2));
                    }
                } else {
                    fragmentTransactionBeginTransaction.add(fragmentContainer.getId(), orCreateFragment, str);
                    fragmentTransactionBeginTransaction.commitAllowingStateLoss();
                    startFadeInAnimationForFragment(orCreateFragment, 150);
                }
            }
            this.currentFragmentIndex = index;
        }
    }

    private static final void switchToFragment$lambda$5$lambda$4(COUIChipTabGroupFragmentMediator this$0, View this_apply) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(this_apply, "$this_apply");
        this$0.startFadeInAnimation(this_apply, 150);
    }

    public final void attach() {
        if (this.attached) {
            throw new IllegalStateException("COUIChipTabGroupFragmentMediator is already attached");
        }
        int fragmentCount = this.fragmentSwitcher.getFragmentCount();
        int chipCount = this.chipTabGroup.getChipCount();
        if (fragmentCount != chipCount) {
            throw new IllegalStateException("Fragment count (" + fragmentCount + ") does not match ChipTab count (" + chipCount + ')');
        }
        this.attached = true;
        this.currentFragmentIndex = this.fragmentSwitcher.getCurrentFragmentIndex();
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(fragmentCount, this);
        this.chipSelectionListener = anonymousClass1;
        this.chipTabGroup.setOnSelectionChangeListener(anonymousClass1);
        int i3 = this.currentFragmentIndex;
        if (i3 >= 0 && i3 < chipCount && this.chipTabGroup.getSelectedChipIndex() != this.currentFragmentIndex) {
            Log.d(TAG, "Initial sync: Fragment index=" + this.currentFragmentIndex + ", syncing to Chip");
            this.chipTabGroup.setSelectedChipByIndex(this.currentFragmentIndex, false);
        }
        int i4 = this.currentFragmentIndex;
        if (i4 >= 0) {
            switchToFragment(i4);
        }
        Log.d(TAG, "Mediator attached: " + fragmentCount + " fragments, " + chipCount + " chips");
    }

    public final void detach() {
        if (this.attached) {
            COUISpringAnimation cOUISpringAnimation = this.fadeOutAnimation;
            if (cOUISpringAnimation != null) {
                cOUISpringAnimation.cancel();
            }
            COUISpringAnimation cOUISpringAnimation2 = this.fadeInAnimation;
            if (cOUISpringAnimation2 != null) {
                cOUISpringAnimation2.cancel();
            }
            this.animationHandler.removeCallbacksAndMessages(null);
            this.chipTabGroup.setOnSelectionChangeListener(null);
            this.chipSelectionListener = null;
            this.attached = false;
            this.fragmentCache.clear();
            this.currentFragmentIndex = -1;
            Log.d(TAG, "Mediator detached");
        }
    }

    public final Function2<COUIChip, Integer, Unit> getChipSelectionListener() {
        return this.chipSelectionListener;
    }

    /* JADX INFO: renamed from: isAttached, reason: from getter */
    public final boolean getAttached() {
        return this.attached;
    }

    public final void setChipSelectionListener(Function2<? super COUIChip, ? super Integer, Unit> function2) {
        this.chipSelectionListener = function2;
    }
}