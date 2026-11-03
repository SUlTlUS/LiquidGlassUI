package com.coui.appcompat.panel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.fragment.app.Fragment;
import com.coui.appcompat.toolbar.COUIToolbar;
import com.support.panel.R;
import java.io.Serializable;

/* JADX INFO: loaded from: classes7.dex */
public class COUIPanelFragment extends Fragment implements Serializable {
    private static final long ENTER_MASK_ALPHA_ANIM_DURATION = 350;
    private static final long EXIT_MASK_ALPHA_ANIM_DURATION = 300;
    private static final long MASK_ALPHA = 255;
    private static final String SAVE_IS_IN_TINY_SCREEN_PANEL_KEY = "SAVE_IS_IN_TINY_SCREEN_PANEL_KEY";
    private static final String SAVE_IS_SHOW_IN_FIRST_PANEL_KEY = "SAVE_IS_SHOW_IN_FIRST_PANEL_KEY";
    private View mContentView;
    private DialogInterface.OnKeyListener mDialogOnKeyListener;
    private COUIPanelDragListener mDragPanelListener;
    private View mDragView;
    protected boolean mIsInTinyScreen;
    private COUIBottomSheetDialog.OnBackInvokedLocalListener mOnBackInvokedLocalListener;
    private View.OnTouchListener mOutSideViewOnTouchListener;
    private COUIPanelBarView mPanelBarView;
    private PanelFragmentAnimationListener mPanelFragmentAnimationListener;
    private COUIPanelContentLayout mPanelView;
    private View mTitleView;
    private FrameLayout mTitleViewLayout;
    private COUIToolbar mToolbar;
    protected static final int CUSTOM_ANIMATION_ENTER = R.anim.coui_close_panel_fragment_enter;
    protected static final int CUSTOM_ANIMATION_EXIT = R.anim.coui_open_panel_fragment_exit;
    private static final Interpolator ENTER_MASK_ALPHA_ANIM_INTERPOLATOR = new PathInterpolator(0.3f, 0.26f, 0.4f, 1.0f);
    private static final Interpolator EXIT_MASK_ALPHA_ANIM_INTERPOLATOR = new PathInterpolator(0.3f, 0.15f, 0.3f, 1.0f);
    private Boolean mIsShowOnFirstPanel = Boolean.FALSE;
    private boolean mBluring = false;

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIPanelFragment$1, reason: invalid class name */
    public class AnonymousClass1 extends AnimatorListenerAdapter {
        public AnonymousClass1() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            if (COUIPanelFragment.access$000(COUIPanelFragment.this) != null) {
                COUIPanelFragment.access$000(COUIPanelFragment.this).onAnimationEnd();
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIPanelFragment$2, reason: invalid class name */
    public class AnonymousClass2 implements Animation.AnimationListener {
        final /* synthetic */ ValueAnimator val$finalAlphaAnimation;

        public AnonymousClass2(ValueAnimator valueAnimator) {
            this.val$finalAlphaAnimation = valueAnimator;
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
            this.val$finalAlphaAnimation.start();
        }
    }

    public interface PanelFragmentAnimationListener {
        void onAnimationEnd();
    }

    public static /* synthetic */ PanelFragmentAnimationListener access$000(COUIPanelFragment cOUIPanelFragment) {
        return cOUIPanelFragment.mPanelFragmentAnimationListener;
    }

    private void ensurePanelView() {
        if (this.mPanelView == null) {
            this.mPanelView = (COUIPanelContentLayout) getLayoutInflater().inflate(this.mIsInTinyScreen ? R.layout.coui_panel_view_layout_tiny : R.layout.coui_panel_view_layout, (ViewGroup) null);
        }
    }

    private void setBottomButtonBar(String str, View.OnClickListener onClickListener, String str2, View.OnClickListener onClickListener2, String str3, View.OnClickListener onClickListener3) {
        ensurePanelView();
        this.mPanelView.setUpBottomBar(true, str, onClickListener, str2, onClickListener2, str3, onClickListener3);
    }

    public Button getCenterButton() {
        COUIPanelContentLayout cOUIPanelContentLayout = this.mPanelView;
        if (cOUIPanelContentLayout != null) {
            return (Button) cOUIPanelContentLayout.findViewById(android.R.id.button3);
        }
        return null;
    }

    public int getContentResId() {
        return R.id.panel_container;
    }

    public View getContentView() {
        return this.mContentView;
    }

    public DialogInterface.OnKeyListener getDialogOnKeyListener() {
        return this.mDialogOnKeyListener;
    }

    public COUIPanelDragListener getDragPanelListener() {
        return this.mDragPanelListener;
    }

    public View getDragView() {
        return this.mDragView;
    }

    public int getDragViewHeight() {
        View view = this.mDragView;
        if (view != null) {
            return view.getHeight();
        }
        return 0;
    }

    public COUIPanelContentLayout getDraggableLinearLayout() {
        return this.mPanelView;
    }

    public Button getLeftButton() {
        COUIPanelContentLayout cOUIPanelContentLayout = this.mPanelView;
        if (cOUIPanelContentLayout != null) {
            return (Button) cOUIPanelContentLayout.findViewById(android.R.id.button2);
        }
        return null;
    }

    public COUIBottomSheetDialog.OnBackInvokedLocalListener getOnBackInvokedLocalListener() {
        return this.mOnBackInvokedLocalListener;
    }

    public View.OnTouchListener getOutSideViewOnTouchListener() {
        return this.mOutSideViewOnTouchListener;
    }

    public COUIPanelBarView getPanelBarView() {
        return this.mPanelBarView;
    }

    public Button getRightButton() {
        COUIPanelContentLayout cOUIPanelContentLayout = this.mPanelView;
        if (cOUIPanelContentLayout != null) {
            return (Button) cOUIPanelContentLayout.findViewById(android.R.id.button1);
        }
        return null;
    }

    public Boolean getShowOnFirstPanel() {
        return this.mIsShowOnFirstPanel;
    }

    public View getTitleView() {
        return this.mTitleView;
    }

    public COUIToolbar getToolbar() {
        return this.mToolbar;
    }

    public int getToolbarHeight() {
        COUIToolbar cOUIToolbar = this.mToolbar;
        if (cOUIToolbar != null) {
            return cOUIToolbar.getHeight();
        }
        return 0;
    }

    public void hideDragView() {
        ViewGroup.LayoutParams layoutParams = getDragView().getLayoutParams();
        layoutParams.height = getDragView().getContext().getResources().getDimensionPixelSize(R.dimen.coui_panel_drag_view_hide_height);
        getDragView().setVisibility(4);
        getDragView().setLayoutParams(layoutParams);
    }

    public void initView(View view) {
    }

    public void onAbandon(Boolean bool) {
        setPanelDragListener(null);
        setDialogOnKeyListener(null);
        setOnBackInvokedLocalListener(null);
        setOutSideViewOnTouchListener(null);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (bundle != null) {
            this.mIsShowOnFirstPanel = Boolean.valueOf(bundle.getBoolean(SAVE_IS_SHOW_IN_FIRST_PANEL_KEY, false));
            if (getParentFragment() instanceof COUIBottomSheetDialogFragment) {
                ((COUIBottomSheetDialogFragment) getParentFragment()).setPanelFragment(this, this.mIsShowOnFirstPanel);
            }
        }
        initView(this.mPanelView);
    }

    public void onAdd(Boolean bool) {
    }

    @Override // androidx.fragment.app.Fragment
    public Animation onCreateAnimation(int i3, boolean z2, int i4) {
        ValueAnimator valueAnimatorOfFloat;
        Animation animationLoadAnimation;
        if (i4 == CUSTOM_ANIMATION_EXIT) {
            valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            valueAnimatorOfFloat.setDuration(300L);
            valueAnimatorOfFloat.setInterpolator(EXIT_MASK_ALPHA_ANIM_INTERPOLATOR);
            animationLoadAnimation = AnimationUtils.loadAnimation(getContext(), i4);
        } else {
            valueAnimatorOfFloat = null;
            animationLoadAnimation = null;
        }
        if (i4 == CUSTOM_ANIMATION_ENTER) {
            valueAnimatorOfFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
            valueAnimatorOfFloat.setDuration(ENTER_MASK_ALPHA_ANIM_DURATION);
            valueAnimatorOfFloat.setInterpolator(ENTER_MASK_ALPHA_ANIM_INTERPOLATOR);
            valueAnimatorOfFloat.addListener(new AnonymousClass1());
            animationLoadAnimation = AnimationUtils.loadAnimation(getContext(), i4);
        }
        if (valueAnimatorOfFloat == null || animationLoadAnimation == null) {
            return super.onCreateAnimation(i3, z2, i4);
        }
        animationLoadAnimation.setAnimationListener(new AnonymousClass2(valueAnimatorOfFloat));
        return animationLoadAnimation;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        if (bundle != null) {
            this.mIsInTinyScreen = bundle.getBoolean(SAVE_IS_IN_TINY_SCREEN_PANEL_KEY, false);
        }
        COUIPanelContentLayout cOUIPanelContentLayout = (COUIPanelContentLayout) getLayoutInflater().inflate(this.mIsInTinyScreen ? R.layout.coui_panel_view_layout_tiny : R.layout.coui_panel_view_layout, (ViewGroup) null);
        this.mPanelView = cOUIPanelContentLayout;
        cOUIPanelContentLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        this.mDragView = this.mPanelView.getDragView();
        this.mPanelBarView = this.mPanelView.getPanelBarView();
        View viewInflate = layoutInflater.inflate(R.layout.coui_panel_layout, viewGroup, false);
        COUIToolbar cOUIToolbar = (COUIToolbar) viewInflate.findViewById(R.id.bottom_sheet_toolbar);
        this.mToolbar = cOUIToolbar;
        if (cOUIToolbar != null) {
            cOUIToolbar.setUseMaterialEffectAttr(this.mBluring ? COUIToolbar.MaterialEffectState.FORCE_ENABLE : COUIToolbar.MaterialEffectState.FORCE_DISABLE);
            this.mToolbar.setIsDrawableMaterialEffect(true);
        }
        this.mTitleViewLayout = (FrameLayout) viewInflate.findViewById(R.id.title_view_container);
        this.mContentView = viewInflate.findViewById(getContentResId());
        this.mPanelView.addContentView(viewInflate);
        return this.mPanelView;
    }

    public void onHide(Boolean bool) {
    }

    @Override // androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean(SAVE_IS_SHOW_IN_FIRST_PANEL_KEY, this.mIsShowOnFirstPanel.booleanValue());
        bundle.putBoolean(SAVE_IS_IN_TINY_SCREEN_PANEL_KEY, this.mIsInTinyScreen);
    }

    public void onShow(Boolean bool) {
    }

    public void setContentView(View view) {
        this.mContentView = view;
    }

    public void setDialogOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        this.mDialogOnKeyListener = onKeyListener;
    }

    public void setIsBluring(boolean z2) {
        if (this.mBluring != z2) {
            this.mBluring = z2;
            COUIToolbar cOUIToolbar = this.mToolbar;
            if (cOUIToolbar != null) {
                cOUIToolbar.setIsDrawableMaterialEffect(true);
                this.mToolbar.setUseMaterialEffectAttr(this.mBluring ? COUIToolbar.MaterialEffectState.FORCE_ENABLE : COUIToolbar.MaterialEffectState.FORCE_DISABLE);
            }
        }
    }

    public void setIsInTinyScreen(boolean z2) {
        this.mIsInTinyScreen = z2;
    }

    public void setOnBackInvokedLocalListener(COUIBottomSheetDialog.OnBackInvokedLocalListener onBackInvokedLocalListener) {
        this.mOnBackInvokedLocalListener = onBackInvokedLocalListener;
    }

    public void setOutSideViewOnTouchListener(View.OnTouchListener onTouchListener) {
        this.mOutSideViewOnTouchListener = onTouchListener;
    }

    public void setPanelDragListener(COUIPanelDragListener cOUIPanelDragListener) {
        this.mDragPanelListener = cOUIPanelDragListener;
    }

    public void setPanelFragmentAnimationListener(PanelFragmentAnimationListener panelFragmentAnimationListener) {
        this.mPanelFragmentAnimationListener = panelFragmentAnimationListener;
    }

    public void setShowOnFirstPanel(Boolean bool) {
        this.mIsShowOnFirstPanel = bool;
    }

    public void setTitleView(int i3) {
        if (i3 > 0) {
            setTitleView(LayoutInflater.from(getContext()).inflate(i3, (ViewGroup) this.mTitleViewLayout, false));
        }
    }

    public void setToolbar(COUIToolbar cOUIToolbar) {
        if (cOUIToolbar == null || this.mToolbar == null) {
            return;
        }
        this.mTitleViewLayout.setVisibility(8);
        this.mToolbar.setVisibility(0);
        this.mToolbar = cOUIToolbar;
        cOUIToolbar.setUseMaterialEffectAttr(this.mBluring ? COUIToolbar.MaterialEffectState.FORCE_ENABLE : COUIToolbar.MaterialEffectState.FORCE_DISABLE);
        this.mToolbar.setIsDrawableMaterialEffect(true);
    }

    public void setToolbarJust(COUIToolbar cOUIToolbar) {
        if (cOUIToolbar == null || this.mToolbar == null) {
            return;
        }
        this.mToolbar = cOUIToolbar;
        cOUIToolbar.setUseMaterialEffectAttr(this.mBluring ? COUIToolbar.MaterialEffectState.FORCE_ENABLE : COUIToolbar.MaterialEffectState.FORCE_DISABLE);
        this.mToolbar.setIsDrawableMaterialEffect(true);
    }

    public void showDragView() {
        ViewGroup.LayoutParams layoutParams = getDragView().getLayoutParams();
        layoutParams.height = getDragView().getContext().getResources().getDimensionPixelSize(R.dimen.coui_panel_drag_view_height);
        getDragView().setVisibility(0);
        getDragView().setLayoutParams(layoutParams);
    }

    public void setTitleView(View view) {
        this.mTitleView = view;
        if (this.mTitleViewLayout == null || view == null || view.getVisibility() == 8) {
            return;
        }
        this.mToolbar.setVisibility(8);
        this.mTitleViewLayout.setVisibility(0);
        this.mTitleView = view;
        this.mTitleViewLayout.addView(view);
    }
}