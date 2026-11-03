package com.coui.appcompat.bottomfloatingtoolbar;

import android.graphics.RectF;
import android.view.View;
import android.view.ViewParent;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;

/* JADX INFO: loaded from: classes7.dex */
public class MenuViewAnimatorImpl implements IBottomFloatingToolbarMenuViewAnimator {
    private static final float DEFAULT_HORIZONTAL_SPRING_BOUNCE = 0.4f;
    private static final float DEFAULT_HORIZONTAL_SPRING_RESPONSE = 0.5f;
    private static final float DEFAULT_SCALE_SPRING_BOUNCE = 0.4f;
    private static final float DEFAULT_SCALE_SPRING_RESPONSE = 0.5f;
    private static final float DEFAULT_SPRING_ANIMATION_RANGE = 10000.0f;
    private static final float HIDE_SCALE_SPRING_BOUNCE = 0.0f;
    private static final float HIDE_SCALE_SPRING_RESPONSE = 0.25f;
    private static final String TAG = "MenuViewAnimatorImpl";
    private boolean mAttached;
    private COUIBottomFloatingToolbar.InternalActionAnimationCounter mCounter;
    private COUISpringAnimation mHorizontalAnimation;
    private final COUIBottomFloatingToolbarMenuView mHost;
    private COUISpringAnimation mScaleAnimation;
    private final RectF mDrawingBounds = new RectF();
    private final RectF mRealBounds = new RectF();
    private boolean mNotHorizontalAnim = true;
    private float mScale = 0.0f;

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingtoolbar.MenuViewAnimatorImpl$1, reason: invalid class name */
    public class AnonymousClass1 extends FloatPropertyCompat<MenuViewAnimatorImpl> {
        final /* synthetic */ GetPropertyCall val$getCall;
        final /* synthetic */ SetPropertyCall val$setCall;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass1(String str, GetPropertyCall getPropertyCall, SetPropertyCall setPropertyCall) {
            super(str);
            this.val$getCall = getPropertyCall;
            this.val$setCall = setPropertyCall;
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public /* bridge */ /* synthetic */ float getValue(MenuViewAnimatorImpl menuViewAnimatorImpl) {
            return getValue2(menuViewAnimatorImpl);
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public /* bridge */ /* synthetic */ void setValue(MenuViewAnimatorImpl menuViewAnimatorImpl, float f3) {
            setValue2(menuViewAnimatorImpl, f3);
        }

        /* JADX INFO: renamed from: getValue, reason: avoid collision after fix types in other method */
        public float getValue2(MenuViewAnimatorImpl menuViewAnimatorImpl) {
            return this.val$getCall.get(menuViewAnimatorImpl);
        }

        /* JADX INFO: renamed from: setValue, reason: avoid collision after fix types in other method */
        public void setValue2(MenuViewAnimatorImpl menuViewAnimatorImpl, float f3) {
            this.val$setCall.set(menuViewAnimatorImpl, f3);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingtoolbar.MenuViewAnimatorImpl$2, reason: invalid class name */
    public class AnonymousClass2 implements Runnable {
        final /* synthetic */ int val$left;

        public AnonymousClass2(int i3) {
            this.val$left = i3;
        }

        @Override // java.lang.Runnable
        public void run() {
            MenuViewAnimatorImpl.access$000(MenuViewAnimatorImpl.this).disableTouchMotionForHorizontalAnim();
            ViewParent parent = MenuViewAnimatorImpl.access$000(MenuViewAnimatorImpl.this).getParent();
            if (parent instanceof COUIBottomFloatingToolbar) {
                MenuViewAnimatorImpl.access$102(MenuViewAnimatorImpl.this, ((COUIBottomFloatingToolbar) parent).getAnimationCounter());
            }
            if (!MenuViewAnimatorImpl.access$200(MenuViewAnimatorImpl.this).isRunning() && MenuViewAnimatorImpl.access$100(MenuViewAnimatorImpl.this) != null) {
                MenuViewAnimatorImpl.access$100(MenuViewAnimatorImpl.this).setAnimIncrement(true, "HorizontalAnimation from doHorizontalAnim");
            }
            MenuViewAnimatorImpl.access$200(MenuViewAnimatorImpl.this).setStartValue(MenuViewAnimatorImpl.access$300(MenuViewAnimatorImpl.this).left);
            MenuViewAnimatorImpl.access$200(MenuViewAnimatorImpl.this).animateToFinalPosition(this.val$left);
            MenuViewAnimatorImpl.access$402(MenuViewAnimatorImpl.this, false);
        }
    }

    public interface GetPropertyCall<T extends MenuViewAnimatorImpl> {
        float get(T t2);
    }

    public interface SetPropertyCall<T extends MenuViewAnimatorImpl> {
        void set(T t2, float f3);
    }

    public MenuViewAnimatorImpl(COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView) {
        this.mHost = cOUIBottomFloatingToolbarMenuView;
        initAnimation();
    }

    public static /* synthetic */ void a(MenuViewAnimatorImpl menuViewAnimatorImpl, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        COUIBottomFloatingToolbar.InternalActionAnimationCounter internalActionAnimationCounter = menuViewAnimatorImpl.mCounter;
        if (internalActionAnimationCounter != null) {
            internalActionAnimationCounter.setAnimDecrement(true, "HorizontalAnimation");
        }
        menuViewAnimatorImpl.mHost.enableTouchMotionAfterHorizontalAnim();
    }

    public static /* synthetic */ COUIBottomFloatingToolbarMenuView access$000(MenuViewAnimatorImpl menuViewAnimatorImpl) {
        return menuViewAnimatorImpl.mHost;
    }

    public static /* synthetic */ COUIBottomFloatingToolbar.InternalActionAnimationCounter access$100(MenuViewAnimatorImpl menuViewAnimatorImpl) {
        return menuViewAnimatorImpl.mCounter;
    }

    public static /* synthetic */ COUIBottomFloatingToolbar.InternalActionAnimationCounter access$102(MenuViewAnimatorImpl menuViewAnimatorImpl, COUIBottomFloatingToolbar.InternalActionAnimationCounter internalActionAnimationCounter) {
        menuViewAnimatorImpl.mCounter = internalActionAnimationCounter;
        return internalActionAnimationCounter;
    }

    public static /* synthetic */ COUISpringAnimation access$200(MenuViewAnimatorImpl menuViewAnimatorImpl) {
        return menuViewAnimatorImpl.mHorizontalAnimation;
    }

    public static /* synthetic */ RectF access$300(MenuViewAnimatorImpl menuViewAnimatorImpl) {
        return menuViewAnimatorImpl.mDrawingBounds;
    }

    public static /* synthetic */ boolean access$402(MenuViewAnimatorImpl menuViewAnimatorImpl, boolean z2) {
        menuViewAnimatorImpl.mNotHorizontalAnim = z2;
        return z2;
    }

    public static /* synthetic */ void b(MenuViewAnimatorImpl menuViewAnimatorImpl, float f3) {
        menuViewAnimatorImpl.setScale(f3);
    }

    public static /* synthetic */ float c(MenuViewAnimatorImpl menuViewAnimatorImpl) {
        return menuViewAnimatorImpl.getHorizontal();
    }

    private COUISpringAnimation createSpringAnimation(String str, COUISpringForce cOUISpringForce, GetPropertyCall<MenuViewAnimatorImpl> getPropertyCall, SetPropertyCall<MenuViewAnimatorImpl> setPropertyCall, COUIDynamicAnimation.OnAnimationUpdateListener onAnimationUpdateListener, COUIDynamicAnimation.OnAnimationEndListener onAnimationEndListener) {
        COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(this, new AnonymousClass1(str, getPropertyCall, setPropertyCall));
        cOUISpringAnimation.setSpring(cOUISpringForce);
        if (onAnimationUpdateListener != null) {
            cOUISpringAnimation.addUpdateListener(onAnimationUpdateListener);
        }
        if (onAnimationEndListener != null) {
            cOUISpringAnimation.addEndListener(onAnimationEndListener);
        }
        return cOUISpringAnimation;
    }

    public static /* synthetic */ void d(MenuViewAnimatorImpl menuViewAnimatorImpl, boolean z2) {
        COUIBottomFloatingToolbar.InternalActionAnimationCounter internalActionAnimationCounter;
        if (!menuViewAnimatorImpl.mScaleAnimation.isRunning() && (internalActionAnimationCounter = menuViewAnimatorImpl.mCounter) != null) {
            internalActionAnimationCounter.setAnimIncrement(true, "ScaleAnimation from updateAttachState");
        }
        menuViewAnimatorImpl.mScaleAnimation.getSpring().setResponse(z2 ? 0.5f : 0.25f);
        menuViewAnimatorImpl.mScaleAnimation.getSpring().setBounce(z2 ? 0.4f : 0.0f);
        menuViewAnimatorImpl.mScaleAnimation.setStartValue(menuViewAnimatorImpl.mHost.getScaleX() * 10000.0f);
        menuViewAnimatorImpl.mScaleAnimation.animateToFinalPosition(z2 ? 10000.0f : 0.0f);
    }

    private void doHorizontalAnim(int i3) {
        this.mHost.setX(this.mDrawingBounds.left);
        this.mHost.setLayoutAnimation(new AnonymousClass2(i3));
    }

    public static /* synthetic */ void e(MenuViewAnimatorImpl menuViewAnimatorImpl, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        menuViewAnimatorImpl.handleMenuHide(f3);
        COUIBottomFloatingToolbar.InternalActionAnimationCounter internalActionAnimationCounter = menuViewAnimatorImpl.mCounter;
        if (internalActionAnimationCounter != null) {
            internalActionAnimationCounter.setAnimDecrement(true, "ScaleAnimation");
        }
    }

    public static /* synthetic */ void f(MenuViewAnimatorImpl menuViewAnimatorImpl, float f3) {
        menuViewAnimatorImpl.setHorizontal(f3);
    }

    public static /* synthetic */ float g(MenuViewAnimatorImpl menuViewAnimatorImpl) {
        return menuViewAnimatorImpl.getScale();
    }

    private float getHorizontal() {
        return this.mDrawingBounds.left;
    }

    private float getScale() {
        return this.mScale * 10000.0f;
    }

    private void handleMenuHide(float f3) {
        if (f3 == 0.0f) {
            this.mHost.unbindController();
            ViewParent parent = this.mHost.getParent();
            if (parent instanceof COUIBottomFloatingToolbar) {
                ((COUIBottomFloatingToolbar) parent).endMenuViewTransition(this.mHost);
            }
            this.mRealBounds.setEmpty();
            this.mDrawingBounds.setEmpty();
        }
    }

    private void initAnimation() {
        this.mHorizontalAnimation = createSpringAnimation("horizontal", new COUISpringForce().setResponse(0.5f).setBounce(0.4f), new j(), new k(), null, new l(this));
        this.mScaleAnimation = createSpringAnimation("scale", new COUISpringForce().setResponse(0.5f).setBounce(0.4f), new m(), new n(), null, new o(this));
    }

    private void invalidateParent() {
        if (this.mHost.getParent() instanceof View) {
            ((View) this.mHost.getParent()).invalidate();
        }
    }

    private boolean isBoundsChange(float f3, int i3, int i4) {
        return isCenterChange(f3) && isLeftChange(i3) && isRightChange(i4);
    }

    private boolean isCenterChange(float f3) {
        return (this.mRealBounds.centerX() == f3 || this.mDrawingBounds.centerX() == f3) ? false : true;
    }

    private boolean isLeftChange(int i3) {
        float f3 = i3;
        return (this.mRealBounds.left == f3 || this.mDrawingBounds.left == f3) ? false : true;
    }

    private boolean isRightChange(int i3) {
        float f3 = i3;
        return (this.mRealBounds.right == f3 || this.mDrawingBounds.right == f3) ? false : true;
    }

    private void setHorizontal(float f3) {
        RectF rectF = this.mDrawingBounds;
        rectF.offsetTo(f3, rectF.top);
        this.mHost.setX(this.mDrawingBounds.left);
        invalidateParent();
    }

    private void setScale(float f3) {
        this.mScale = f3 / 10000.0f;
        int width = this.mHost.getWidth();
        int height = this.mHost.getHeight();
        if (width > 0 && height > 0) {
            this.mHost.setPivotX(width / 2.0f);
            this.mHost.setPivotY(height / 2.0f);
        }
        this.mHost.setScaleX(this.mScale);
        this.mHost.setScaleY(this.mScale);
    }

    public void cancelHorizontalAnim() {
        if (this.mHorizontalAnimation.isRunning()) {
            this.mHorizontalAnimation.cancel();
            setHorizontal(this.mRealBounds.left);
        }
    }

    public void cancelMenuAnim() {
        cancelHorizontalAnim();
        cancelScaleAnim();
    }

    public void cancelScaleAnim() {
        if (this.mScaleAnimation.isRunning()) {
            this.mScaleAnimation.cancel();
            float f3 = this.mAttached ? 10000.0f : 0.0f;
            setScale(f3);
            handleMenuHide(f3);
        }
    }

    public RectF getDrawingBounds() {
        return this.mDrawingBounds;
    }

    public boolean getNotHorizontalAnim() {
        return this.mNotHorizontalAnim;
    }

    public RectF getRealBounds() {
        return this.mRealBounds;
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarMenuViewAnimator
    public void unbindController() {
        this.mRealBounds.setEmpty();
        this.mDrawingBounds.setEmpty();
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarMenuViewAnimator
    public void updateAttachState(boolean z2, boolean z3) {
        this.mAttached = z2;
        if (!z3) {
            this.mHost.setScaleX(z2 ? 1.0f : 0.0f);
            this.mHost.setScaleY(z2 ? 1.0f : 0.0f);
            return;
        }
        ViewParent parent = this.mHost.getParent();
        if (parent instanceof COUIBottomFloatingToolbar) {
            this.mCounter = ((COUIBottomFloatingToolbar) parent).getAnimationCounter();
        }
        this.mHost.ensureItemViewAnimatorImpl();
        this.mHost.setAttachAnimation(new p(this, z2));
    }

    @Override // com.coui.appcompat.bottomfloatingtoolbar.IBottomFloatingToolbarMenuViewAnimator
    public void updateRealBounds(int i3, int i4, int i5, int i6, boolean z2) {
        this.mNotHorizontalAnim = true;
        RectF rectF = this.mRealBounds;
        float f3 = i3;
        if (rectF.left == f3 && rectF.top == i4 && rectF.right == i5 && rectF.bottom == i6) {
            this.mHost.mLayoutAnim = true;
            return;
        }
        float f4 = (i3 + i5) / 2.0f;
        if (rectF.isEmpty()) {
            this.mDrawingBounds.set(f3, i4, i5, i6);
        } else {
            COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView = this.mHost;
            if (cOUIBottomFloatingToolbarMenuView.mLayoutAnim) {
                ViewParent parent = cOUIBottomFloatingToolbarMenuView.getParent();
                if ((parent instanceof COUIBottomFloatingToolbar) && ((COUIBottomFloatingToolbar) parent).getGroupAlignStyle() == 1 && z2) {
                    doHorizontalAnim(i3);
                } else if (z2 && isBoundsChange(f4, i3, i5)) {
                    doHorizontalAnim(i3);
                }
            } else {
                this.mDrawingBounds.set(f3, i4, i5, i6);
            }
        }
        this.mRealBounds.set(f3, i4, i5, i6);
        COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView2 = this.mHost;
        cOUIBottomFloatingToolbarMenuView2.mLayoutAnim = true;
        if (cOUIBottomFloatingToolbarMenuView2.mDoItemAnimation) {
            return;
        }
        cOUIBottomFloatingToolbarMenuView2.setRight((int) this.mRealBounds.right);
    }
}