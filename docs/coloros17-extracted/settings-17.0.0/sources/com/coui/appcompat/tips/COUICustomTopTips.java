package com.coui.appcompat.tips;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.coui.appcompat.cardview.COUICardView;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.support.appcompat.R;

/* JADX INFO: loaded from: classes7.dex */
public abstract class COUICustomTopTips extends COUICardView {
    private Animator.AnimatorListener mAnimatorDismissListener;
    private AnimatorSet mAnimatorSetDismiss;
    private AnimatorSet mAnimatorSetShow;
    private Animator.AnimatorListener mAnimatorShowListener;
    private View mView;

    /* JADX INFO: renamed from: com.coui.appcompat.tips.COUICustomTopTips$1, reason: invalid class name */
    public class AnonymousClass1 implements Animator.AnimatorListener {
        public AnonymousClass1() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (COUICustomTopTips.access$000(COUICustomTopTips.this) != null) {
                COUICustomTopTips.access$000(COUICustomTopTips.this).onAnimationCancel(animator);
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUICustomTopTips.access$000(COUICustomTopTips.this) != null) {
                COUICustomTopTips.access$000(COUICustomTopTips.this).onAnimationEnd(animator);
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
            if (COUICustomTopTips.access$000(COUICustomTopTips.this) != null) {
                COUICustomTopTips.access$000(COUICustomTopTips.this).onAnimationRepeat(animator);
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (COUICustomTopTips.access$000(COUICustomTopTips.this) != null) {
                COUICustomTopTips.access$000(COUICustomTopTips.this).onAnimationStart(animator);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.tips.COUICustomTopTips$2, reason: invalid class name */
    public class AnonymousClass2 implements Animator.AnimatorListener {
        public AnonymousClass2() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (COUICustomTopTips.access$100(COUICustomTopTips.this) != null) {
                COUICustomTopTips.access$100(COUICustomTopTips.this).onAnimationCancel(animator);
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUICustomTopTips.access$100(COUICustomTopTips.this) != null) {
                COUICustomTopTips.access$100(COUICustomTopTips.this).onAnimationEnd(animator);
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
            if (COUICustomTopTips.access$100(COUICustomTopTips.this) != null) {
                COUICustomTopTips.access$100(COUICustomTopTips.this).onAnimationRepeat(animator);
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (COUICustomTopTips.access$100(COUICustomTopTips.this) != null) {
                COUICustomTopTips.access$100(COUICustomTopTips.this).onAnimationStart(animator);
            }
        }
    }

    public COUICustomTopTips(Context context) {
        this(context, null);
    }

    public static /* synthetic */ Animator.AnimatorListener access$000(COUICustomTopTips cOUICustomTopTips) {
        return cOUICustomTopTips.mAnimatorShowListener;
    }

    public static /* synthetic */ Animator.AnimatorListener access$100(COUICustomTopTips cOUICustomTopTips) {
        return cOUICustomTopTips.mAnimatorDismissListener;
    }

    public void dismissWithAnim() {
        if (this.mAnimatorSetDismiss == null) {
            ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(this, "scaleY", 1.0f, 0.0f);
            objectAnimatorOfFloat.setDuration(250L);
            AnimatorSet animatorSet = new AnimatorSet();
            this.mAnimatorSetDismiss = animatorSet;
            animatorSet.play(objectAnimatorOfFloat);
        }
        this.mAnimatorSetDismiss.addListener(new AnonymousClass2());
        this.mAnimatorSetDismiss.start();
    }

    public AnimatorSet getAnimatorSetDismiss() {
        return this.mAnimatorSetDismiss;
    }

    public AnimatorSet getAnimatorSetShow() {
        return this.mAnimatorSetShow;
    }

    public View getContentView() {
        return this.mView;
    }

    public abstract int getContentViewId();

    public void init() {
        COUIDarkModeUtil.setForceDarkAllow(this, false);
        setContentView(getContentViewId());
        if (RoundCornerUtil.isVersionSupport()) {
            setRadius(COUIContextUtil.getAttrDimens(getContext(), R.attr.couiRoundCornerMRadius));
            setWeight(COUIContextUtil.getAttrFloat(getContext(), R.attr.couiRoundCornerMWeight));
        } else {
            setRadius(COUIContextUtil.getAttrDimens(getContext(), R.attr.couiRoundCornerM));
        }
        setCardBackgroundColor(ColorStateList.valueOf(COUIContextUtil.getAttrColor(getContext(), R.attr.couiColorFillThin)));
    }

    public void setAnimatorDismissListener(Animator.AnimatorListener animatorListener) {
        this.mAnimatorDismissListener = animatorListener;
    }

    public void setAnimatorSetDismiss(AnimatorSet animatorSet) {
        this.mAnimatorSetDismiss = animatorSet;
    }

    public void setAnimatorSetShow(AnimatorSet animatorSet) {
        this.mAnimatorSetShow = animatorSet;
    }

    public void setAnimatorShowListener(Animator.AnimatorListener animatorListener) {
        this.mAnimatorShowListener = animatorListener;
    }

    public void setContentView(View view) {
        if (this.mView != null) {
            throw new RuntimeException("Repeat calls are not allowed!!");
        }
        this.mView = view;
        addView(view);
    }

    public void showWithAnim() {
        if (this.mAnimatorSetShow == null) {
            ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(this, "scaleY", 0.0f, 1.0f);
            objectAnimatorOfFloat.setDuration(250L);
            AnimatorSet animatorSet = new AnimatorSet();
            this.mAnimatorSetShow = animatorSet;
            animatorSet.play(objectAnimatorOfFloat);
        }
        this.mAnimatorSetShow.addListener(new AnonymousClass1());
        this.mAnimatorSetShow.start();
    }

    public COUICustomTopTips(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUICustomTopTips(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        init();
    }

    public void setContentView(int i3) {
        if (i3 == 0) {
            return;
        }
        setContentView(LayoutInflater.from(getContext()).inflate(i3, (ViewGroup) this, false));
    }
}