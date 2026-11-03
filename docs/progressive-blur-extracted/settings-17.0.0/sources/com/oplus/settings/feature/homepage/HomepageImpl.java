package com.oplus.settings.feature.homepage;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import android.window.OnBackInvokedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.dynamicanimation.animation.FloatValueHolder;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.android.settings.SettingsActivity;
import com.android.settings.intelligence.search.BaseSettingsSearchIndexingCallback;
import com.android.settings.intelligence.search.SearchViewAnimate;
import com.android.settings.intelligence.search.external.SettingsSearchResult;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.search.SearchFeatureProvider;
import com.android.wm.shell.shared.TransitionUtil;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.OnMaterialStateChangeListener;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.searchview.COUISearchBar;
import com.coui.appcompat.toolbar.AppBarBlurHelper;
import com.coui.appcompat.toolbar.AppBarBlurOverlayView;
import com.coui.appcompat.toolbar.COUIToolbar;
import com.google.android.material.appbar.COUIAppBarLayout;
import com.google.android.material.appbar.COUIFloatingScrollingViewBehavior;
import com.google.android.material.appbar.SettingsCollapsableAppBarLayout;
import com.google.android.material.appbar.SettingsCollapsingToolbarLayout;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.oplus.settings.SettingsApplicationEx;
import com.oplus.settings.appfunctions.SystemUpdateScreenStateConfig;
import com.oplus.settings.feature.compatwindow.CompatWindowUtil;
import com.oplus.settings.feature.compatwindow.FoldableScreenMonitor;
import com.oplus.settings.feature.compatwindow.FoldableScreenUtil;
import com.oplus.settings.feature.display.DisplaySettingsFragment;
import com.oplus.settings.feature.homepage.search.AiSearchConstants;
import com.oplus.settings.feature.homepage.search.AsyncViewStub;
import com.oplus.settings.feature.homepage.search.GradientRoundRectButton;
import com.oplus.settings.feature.homepage.search.HomepageSearchFeature;
import com.oplus.settings.feature.homepage.search.SearchIntentItem;
import com.oplus.settings.feature.homepage.search.SearchPreloadUtils;
import com.oplus.settings.utils.DcsTrackerEntry;
import com.oplus.settings.utils.IntentUtils;
import com.oplus.settings.utils.JankManagerUtil;
import com.oplus.settings.utils.LogUtils;
import com.oplus.settings.utils.SettingsDcsRecordUtils;
import com.oplus.settings.utils.SettingsUtils;
import com.oplus.settings.utils.TabletUtils;
import com.oplus.settings.utils.ThemeUtils;
import com.oplus.settings.utils.packages.SettingsPackageUtils;
import com.oplus.util.OplusContextUtil;
import com.oplus.view.JankManager;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/* JADX INFO: loaded from: classes.dex */
public class HomepageImpl extends BaseHomePageImpl implements HomepageSearchFeature.Callback {
    private static final String ACTION_LAUNCH_SEARCH = "oplus.intent.action.settings.MAIN_SEARCH";
    private static final float APPBAR_EXPANDED_HALF = 0.5f;
    private static final long CHECK_RESTORE_DELAY = 450;
    private static final float COLD_START_RESET_TRANSLATION_Y = 0.0f;
    private static final float COLD_START_SEARCH_ELEVATION = -1.0f;
    private static final float COLD_START_TITLE_FRACTION = 0.0f;
    private static final float COLD_START_VISIBLE_ALPHA = 1.0f;
    private static final int COLOR_VALUE = 255;
    private static final String EXTRA_COLOR_FROM = "color_from";
    private static final long FINAL_POSITION = 1000;
    private static final String KEY_SAVED_SEARCH_VIEW_ACCUMULATE_TRANSITION_Y = "search_view_accumulate_transition_y";
    private static final String KEY_SAVED_UI_MODE_NIGHT_FOR_RECREATE = "homepage_impl_saved_ui_mode_night_for_recreate";
    private static final float SPRING_RESPONSE_FAST = 0.1f;
    private static final float SPRING_RESPONSE_SLOW = 0.35f;
    private static final String TAG = "HomepageImpl";
    private static final long TITLE_OFFSET_PX = -200;
    private static final String VALUE_CLICK_SEARCH_VIEW = "com.android.settings_clickSearchView";
    private View mAISearchResultContainer;
    private AsyncViewStub mAISearchResultViewStub;
    private Activity mActivity;
    private SettingsCollapsableAppBarLayout mAppBarLayout;
    private boolean mBlurOverlayExtraHeightApplied;
    private SettingsCollapsingToolbarLayout mCollapsingToolbarLayout;
    private FoldableScreenMonitor mFoldableScreenMonitor;
    private boolean mHomepageExitSpringAnimationPending;
    private int mInitSearchViewY;
    private boolean mIsInSearchState;
    private boolean mIsStopped;
    private int mMainAnimAppBarMeasuredHeightPx;
    private float mMainAnimSearchTopMinusScrollPx;
    private COUISpringAnimation mMainContentSpringAnim;
    private COUISpringForce mMainContentSpringForce;
    private View mMainLayout;
    private boolean mMaterialBlurListenerRegistered;
    private boolean mNeedRestoreColdStartState;
    private COUIAppBarLayout.OnOffsetChangedListener mOffsetChangedListener;
    private boolean mRestoreAppBarExpend;
    private COUISpringAnimation mSearchBarEnterAnim;
    private View mSearchBgMaskContainer;
    private View mSearchBgMaskView;
    private View mSearchContentLayout;
    private boolean mSearchDisabled;
    private boolean mSearchEditBlurForced;
    private NestedScrollView mSearchHistoryScrollView;
    private View mSearchResultContainer;
    private RecyclerView mSearchResultRecyclerView;
    private COUISpringAnimation mSearchResultSpringAnim;
    private COUISpringForce mSearchResultSpringForce;
    private AsyncViewStub mSearchResultViewStub;
    private COUISpringForce mSearchUiSpringForce;
    private SearchViewAnimate mSearchViewAnimate;
    private boolean mSkipRec;
    private int mStatusBarHeight;
    private COUIToolbar mToolbar;
    private RecyclerView mTopLevelSettingsRecyclerView;
    private final HomepageSearchFeature mHomepageSearchFeature = new HomepageSearchFeature(this);
    private final OnBackInvokedCallback mPressBack = new c(this);
    private final View.OnLayoutChangeListener mSearchEditBlurAlphaGuard = new g(this);
    private final OnMaterialStateChangeListener mMaterialBlurStateListener = new AnonymousClass1();
    private boolean mWindowHasFocus = true;
    private boolean mIsPreload = false;
    private float mScrollFraction = 0.0f;
    private float mAccumulatedRecyclerScroll = 0.0f;
    private BaseSettingsSearchIndexingCallback mSearchIndexCallback = null;

    /* JADX INFO: renamed from: com.oplus.settings.feature.homepage.HomepageImpl$1, reason: invalid class name */
    public class AnonymousClass1 implements OnMaterialStateChangeListener {
        public AnonymousClass1() {
        }

        public static /* synthetic */ void a(HomepageImpl homepageImpl) {
            HomepageImpl.i0(homepageImpl);
        }

        @Override // com.coui.appcompat.OnMaterialStateChangeListener
        public CharSequence onDump() {
            return null;
        }

        @Override // com.coui.appcompat.OnMaterialStateChangeListener
        public void onMaterialStateChange(int i3, boolean z2) {
            if (i3 != 2 || HomepageImpl.r(HomepageImpl.this) == null) {
                return;
            }
            HomepageImpl.r(HomepageImpl.this).post(new p(HomepageImpl.this));
        }
    }

    /* JADX INFO: renamed from: com.oplus.settings.feature.homepage.HomepageImpl$2, reason: invalid class name */
    public class AnonymousClass2 implements Function1<View, Unit> {
        final /* synthetic */ OplusSettingsHomepageActivity val$homepageActivity;

        /* JADX INFO: renamed from: com.oplus.settings.feature.homepage.HomepageImpl$2$1, reason: invalid class name */
        /* JADX INFO: loaded from: classes12.dex */
        public class AnonymousClass1 extends RecyclerView.OnScrollListener {
            public AnonymousClass1() {
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i3, int i4) {
                if (HomepageImpl.C(HomepageImpl.this).getSearchState() != 1) {
                    HomepageImpl homepageImpl = HomepageImpl.this;
                    HomepageImpl.F(homepageImpl, HomepageImpl.p(homepageImpl) + i4);
                    HomepageImpl homepageImpl2 = HomepageImpl.this;
                    HomepageImpl.F(homepageImpl2, Math.max(HomepageImpl.p(homepageImpl2), 0.0f));
                }
                HomepageImpl.T(HomepageImpl.this, recyclerView);
                if (HomepageImpl.f0(HomepageImpl.this)) {
                    LogUtils.d(HomepageImpl.TAG, "onScrolled: skip setTranslationY during search spring, dy = " + i4 + ", accumulated = " + HomepageImpl.p(HomepageImpl.this));
                    HomepageImpl.a0(HomepageImpl.this);
                    return;
                }
                if (HomepageImpl.C(HomepageImpl.this).getSearchState() == 1) {
                    LogUtils.d(HomepageImpl.TAG, "onScrolled: skip accum & setTranslationY in STATE_EDIT, dy = " + i4 + ", accumulated = " + HomepageImpl.p(HomepageImpl.this));
                }
            }
        }

        /* JADX INFO: renamed from: com.oplus.settings.feature.homepage.HomepageImpl$2$2, reason: invalid class name and collision with other inner class name */
        /* JADX INFO: loaded from: classes12.dex */
        public class ViewTreeObserverOnGlobalLayoutListenerC05002 implements ViewTreeObserver.OnGlobalLayoutListener {
            public ViewTreeObserverOnGlobalLayoutListenerC05002() {
            }

            public static /* synthetic */ void a(ViewTreeObserverOnGlobalLayoutListenerC05002 viewTreeObserverOnGlobalLayoutListenerC05002, COUIAppBarLayout cOUIAppBarLayout, int i3) {
                viewTreeObserverOnGlobalLayoutListenerC05002.lambda$onGlobalLayout$0(cOUIAppBarLayout, i3);
            }

            private /* synthetic */ void lambda$onGlobalLayout$0(COUIAppBarLayout cOUIAppBarLayout, int i3) {
                if (HomepageImpl.f0(HomepageImpl.this)) {
                    return;
                }
                LogUtils.d("zmmtest", "appBarLayout.getTotalScrollRange():" + cOUIAppBarLayout.getTotalScrollRange() + " verticalOffset:" + i3 + " mScrollFraction:" + HomepageImpl.w(HomepageImpl.this));
                if (cOUIAppBarLayout.getTotalScrollRange() == 0) {
                    return;
                }
                float fAbs = Math.abs(i3) / cOUIAppBarLayout.getTotalScrollRange();
                if (HomepageImpl.w(HomepageImpl.this) == fAbs) {
                    return;
                }
                HomepageImpl.L(HomepageImpl.this, fAbs);
                if (HomepageImpl.C(HomepageImpl.this) != null) {
                    if (fAbs > 0.5f) {
                        HomepageImpl.C(HomepageImpl.this).updateDropShadow(HomepageImpl.w(HomepageImpl.this));
                        HomepageImpl.C(HomepageImpl.this).setNoShadowInNormalState(false);
                    } else {
                        HomepageImpl.C(HomepageImpl.this).updateDropShadow(0.0f);
                        HomepageImpl.C(HomepageImpl.this).setNoShadowInNormalState(true);
                    }
                    if (HomepageImpl.z(HomepageImpl.this)) {
                        HomepageImpl.W(HomepageImpl.this);
                    } else {
                        HomepageImpl homepageImpl = HomepageImpl.this;
                        HomepageImpl.l0(homepageImpl, HomepageImpl.r(homepageImpl).isCollapsed());
                    }
                }
            }

            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                HomepageImpl.k0(HomepageImpl.this);
                HomepageImpl.r(HomepageImpl.this).setHasDivider(false);
                if (TabletUtils.isTablet()) {
                    HomepageImpl.r(HomepageImpl.this).enableAutoExpand(false);
                }
                HomepageImpl.K(HomepageImpl.this, new s(this));
                HomepageImpl.r(HomepageImpl.this).addOnOffsetChangedListener(HomepageImpl.v(HomepageImpl.this));
                if (!HomepageImpl.y(HomepageImpl.this)) {
                    HomepageImpl.x(HomepageImpl.this).setPadding(0, HomepageImpl.E(HomepageImpl.this).getMeasuredHeight() + HomepageImpl.D(HomepageImpl.this), 0, 0);
                    HomepageImpl.c0(HomepageImpl.this);
                    HomepageImpl.t(HomepageImpl.this).applyHomepageSearchIcon();
                }
                HomepageImpl.r(HomepageImpl.this).getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }

        public AnonymousClass2(OplusSettingsHomepageActivity oplusSettingsHomepageActivity) {
            this.val$homepageActivity = oplusSettingsHomepageActivity;
        }

        public static /* synthetic */ void a(AnonymousClass2 anonymousClass2, RecyclerView recyclerView) {
            anonymousClass2.lambda$invoke$0(recyclerView);
        }

        public static /* synthetic */ void b(HomepageImpl homepageImpl, int i3) {
            HomepageImpl.h0(homepageImpl, i3);
        }

        private /* synthetic */ void lambda$invoke$0(RecyclerView recyclerView) {
            recyclerView.addOnScrollListener(new AnonymousClass1());
        }

        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ Unit invoke(View view) {
            return invoke2(view);
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public Unit invoke2(View view) {
            HomepageImpl.G(HomepageImpl.this, (SettingsCollapsableAppBarLayout) view);
            LogUtils.d(HomepageImpl.TAG, "VERSION.SDK_INT: " + Build.VERSION.SDK_INT);
            HomepageImpl.S(HomepageImpl.this, (COUIToolbar) view.findViewById(R.id.toolbar_options));
            HomepageImpl.R(HomepageImpl.this, SettingsUtils.getStatusBarHeight(this.val$homepageActivity));
            HomepageImpl.H(HomepageImpl.this, (SettingsCollapsingToolbarLayout) view.findViewById(R.id.collapsingToolbarLayout));
            HomepageImpl.X(HomepageImpl.this);
            HomepageImpl.j0(HomepageImpl.this);
            HomepageImpl.Q(HomepageImpl.this, (SearchViewAnimate) view.findViewById(R.id.searchView));
            ToolbarEdgeToEdgeInsets.apply(HomepageImpl.q(HomepageImpl.this), R.id.settings_homepage_container, HomepageImpl.r(HomepageImpl.this), HomepageImpl.D(HomepageImpl.this));
            Activity activityQ = HomepageImpl.q(HomepageImpl.this);
            if (activityQ instanceof OplusSettingsHomepageActivity) {
                ((OplusTopLevelSettings) ((OplusSettingsHomepageActivity) activityQ).getMainFragment()).setTopLevelListViewCreatedListener(new q(this));
            }
            if (HomepageImpl.y(HomepageImpl.this)) {
                HomepageImpl.C(HomepageImpl.this).setVisibility(8);
            } else {
                HomepageImpl.J(HomepageImpl.this, true);
                HomepageImpl.d0(HomepageImpl.this, this.val$homepageActivity);
                LogUtils.d(HomepageImpl.TAG, "onCreate: mIsInSearchState: " + HomepageImpl.u(HomepageImpl.this));
                if (HomepageImpl.u(HomepageImpl.this)) {
                    HomepageImpl.Z(HomepageImpl.this);
                }
            }
            HomepageImpl.g0(HomepageImpl.this, this.val$homepageActivity, this.val$homepageActivity.getIntent());
            if (HomepageImpl.r(HomepageImpl.this) != null) {
                HomepageImpl.r(HomepageImpl.this).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserverOnGlobalLayoutListenerC05002());
            } else {
                LogUtils.d(HomepageImpl.TAG, "onCreate : mAppBarLayout is null");
            }
            if (!FoldableScreenUtil.isFoldableScreen()) {
                return null;
            }
            if (HomepageImpl.s(HomepageImpl.this) == null) {
                HomepageImpl.I(HomepageImpl.this, new FoldableScreenMonitor(this.val$homepageActivity, new r(HomepageImpl.this)));
            }
            HomepageImpl.s(HomepageImpl.this).register();
            return null;
        }
    }

    /* JADX INFO: renamed from: com.oplus.settings.feature.homepage.HomepageImpl$3, reason: invalid class name */
    public class AnonymousClass3 implements Runnable {
        public AnonymousClass3() {
        }

        @Override // java.lang.Runnable
        public void run() {
            HomepageImpl.U(HomepageImpl.this);
        }
    }

    /* JADX INFO: renamed from: com.oplus.settings.feature.homepage.HomepageImpl$4, reason: invalid class name */
    public class AnonymousClass4 implements Runnable {
        public AnonymousClass4() {
        }

        @Override // java.lang.Runnable
        public void run() {
            HomepageImpl.V(HomepageImpl.this);
        }
    }

    /* JADX INFO: renamed from: com.oplus.settings.feature.homepage.HomepageImpl$5, reason: invalid class name */
    public class AnonymousClass5 implements COUISearchBar.OnAnimationListener {
        public AnonymousClass5(HomepageImpl homepageImpl) {
        }

        @Override // com.coui.appcompat.searchview.COUISearchBar.OnAnimationListener
        public void onAnimationEnd(int i3) {
        }

        @Override // com.coui.appcompat.searchview.COUISearchBar.OnAnimationListener
        public void onAnimationStart(int i3) {
        }

        @Override // com.coui.appcompat.searchview.COUISearchBar.OnAnimationListener
        public void onUpdate(float f3) {
            LogUtils.d(HomepageImpl.TAG, "onUpdate");
        }
    }

    /* JADX INFO: renamed from: com.oplus.settings.feature.homepage.HomepageImpl$6, reason: invalid class name */
    public class AnonymousClass6 implements Function1<View, Unit> {
        public AnonymousClass6() {
        }

        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ Unit invoke(View view) {
            return invoke2(view);
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public Unit invoke2(View view) {
            if (!HomepageImpl.e0(HomepageImpl.this)) {
                LogUtils.w(HomepageImpl.TAG, "initSearchResultView: activity destroyed, skip");
                return null;
            }
            HomepageImpl.t(HomepageImpl.this).initSearchResultView(true);
            HomepageImpl homepageImpl = HomepageImpl.this;
            HomepageImpl.O(homepageImpl, HomepageImpl.q(homepageImpl).findViewById(R.id.search_result_panel));
            if (HomepageImpl.B(HomepageImpl.this) == null) {
                LogUtils.w(HomepageImpl.TAG, "initSearchResultView: search_result_panel not found, skip");
                return null;
            }
            HomepageImpl homepageImpl2 = HomepageImpl.this;
            HomepageImpl.M(homepageImpl2, HomepageImpl.q(homepageImpl2).findViewById(R.id.search_content_layout));
            HomepageImpl homepageImpl3 = HomepageImpl.this;
            HomepageImpl.P(homepageImpl3, (RecyclerView) HomepageImpl.q(homepageImpl3).findViewById(R.id.search_result_recycler_view));
            HomepageImpl homepageImpl4 = HomepageImpl.this;
            HomepageImpl.N(homepageImpl4, (NestedScrollView) HomepageImpl.q(homepageImpl4).findViewById(R.id.search_history_scroll_View));
            HomepageImpl.Y(HomepageImpl.this);
            HomepageImpl.b0(HomepageImpl.this, (GradientRoundRectButton) view.findViewById(R.id.ai_search_button));
            HomepageImpl.m0(HomepageImpl.this);
            return null;
        }
    }

    /* JADX INFO: renamed from: com.oplus.settings.feature.homepage.HomepageImpl$7, reason: invalid class name */
    /* JADX INFO: loaded from: classes12.dex */
    public class AnonymousClass7 extends BaseSettingsSearchIndexingCallback {
        final Context mAppContext;

        public AnonymousClass7() {
            this.mAppContext = HomepageImpl.q(HomepageImpl.this).getApplicationContext();
        }

        @Override // com.android.settings.intelligence.search.BaseSettingsSearchIndexingCallback, com.android.settings.intelligence.search.external.ISettingsSearchIndexingCallback
        public void onIndexingFinished() {
            LogUtils.d(HomepageImpl.TAG, "onIndexingFinished");
            if (HomepageImpl.A(HomepageImpl.this) != this) {
                LogUtils.d(HomepageImpl.TAG, "onIndexingFinished not current so return");
            } else {
                SearchPreloadUtils.INSTANCE.getInstance().doFinish(this.mAppContext);
            }
        }

        @Override // com.android.settings.intelligence.search.BaseSettingsSearchIndexingCallback, com.android.settings.intelligence.search.external.ISettingsSearchIndexingCallback
        public void onIndexingProgress() {
        }
    }

    /* JADX INFO: loaded from: classes12.dex */
    public static final class SearchLoaderId {
        public static final int CHECK_IS_FULL_INDEX = 6;
        public static final int REMOVE_QUERY_TASK = 3;
        public static final int SAVED_QUERIES = 4;
        public static final int SAVE_QUERY_TASK = 2;
        public static final int SEARCH_RECOMMEND = 5;
        public static final int SEARCH_RESULT = 1;
    }

    public static /* bridge */ /* synthetic */ BaseSettingsSearchIndexingCallback A(HomepageImpl homepageImpl) {
        return homepageImpl.mSearchIndexCallback;
    }

    public static /* bridge */ /* synthetic */ View B(HomepageImpl homepageImpl) {
        return homepageImpl.mSearchResultContainer;
    }

    public static /* bridge */ /* synthetic */ SearchViewAnimate C(HomepageImpl homepageImpl) {
        return homepageImpl.mSearchViewAnimate;
    }

    public static /* bridge */ /* synthetic */ int D(HomepageImpl homepageImpl) {
        return homepageImpl.mStatusBarHeight;
    }

    public static /* bridge */ /* synthetic */ COUIToolbar E(HomepageImpl homepageImpl) {
        return homepageImpl.mToolbar;
    }

    public static /* bridge */ /* synthetic */ void F(HomepageImpl homepageImpl, float f3) {
        homepageImpl.mAccumulatedRecyclerScroll = f3;
    }

    public static /* bridge */ /* synthetic */ void G(HomepageImpl homepageImpl, SettingsCollapsableAppBarLayout settingsCollapsableAppBarLayout) {
        homepageImpl.mAppBarLayout = settingsCollapsableAppBarLayout;
    }

    public static /* bridge */ /* synthetic */ void H(HomepageImpl homepageImpl, SettingsCollapsingToolbarLayout settingsCollapsingToolbarLayout) {
        homepageImpl.mCollapsingToolbarLayout = settingsCollapsingToolbarLayout;
    }

    public static /* bridge */ /* synthetic */ void I(HomepageImpl homepageImpl, FoldableScreenMonitor foldableScreenMonitor) {
        homepageImpl.mFoldableScreenMonitor = foldableScreenMonitor;
    }

    public static /* bridge */ /* synthetic */ void J(HomepageImpl homepageImpl, boolean z2) {
        homepageImpl.mIsPreload = z2;
    }

    public static /* bridge */ /* synthetic */ void K(HomepageImpl homepageImpl, COUIAppBarLayout.OnOffsetChangedListener onOffsetChangedListener) {
        homepageImpl.mOffsetChangedListener = onOffsetChangedListener;
    }

    public static /* bridge */ /* synthetic */ void L(HomepageImpl homepageImpl, float f3) {
        homepageImpl.mScrollFraction = f3;
    }

    public static /* bridge */ /* synthetic */ void M(HomepageImpl homepageImpl, View view) {
        homepageImpl.mSearchContentLayout = view;
    }

    public static /* bridge */ /* synthetic */ void N(HomepageImpl homepageImpl, NestedScrollView nestedScrollView) {
        homepageImpl.mSearchHistoryScrollView = nestedScrollView;
    }

    public static /* bridge */ /* synthetic */ void O(HomepageImpl homepageImpl, View view) {
        homepageImpl.mSearchResultContainer = view;
    }

    public static /* bridge */ /* synthetic */ void P(HomepageImpl homepageImpl, RecyclerView recyclerView) {
        homepageImpl.mSearchResultRecyclerView = recyclerView;
    }

    public static /* bridge */ /* synthetic */ void Q(HomepageImpl homepageImpl, SearchViewAnimate searchViewAnimate) {
        homepageImpl.mSearchViewAnimate = searchViewAnimate;
    }

    public static /* bridge */ /* synthetic */ void R(HomepageImpl homepageImpl, int i3) {
        homepageImpl.mStatusBarHeight = i3;
    }

    public static /* bridge */ /* synthetic */ void S(HomepageImpl homepageImpl, COUIToolbar cOUIToolbar) {
        homepageImpl.mToolbar = cOUIToolbar;
    }

    public static /* bridge */ /* synthetic */ void T(HomepageImpl homepageImpl, RecyclerView recyclerView) {
        homepageImpl.mTopLevelSettingsRecyclerView = recyclerView;
    }

    public static /* bridge */ /* synthetic */ void U(HomepageImpl homepageImpl) {
        homepageImpl.animToEdit();
    }

    public static /* bridge */ /* synthetic */ void V(HomepageImpl homepageImpl) {
        homepageImpl.animToNormal();
    }

    public static /* bridge */ /* synthetic */ void W(HomepageImpl homepageImpl) {
        homepageImpl.applyForcedSearchOverlayAlpha();
    }

    public static /* bridge */ /* synthetic */ void X(HomepageImpl homepageImpl) {
        homepageImpl.applyHomepageAppBarBackground();
    }

    public static /* bridge */ /* synthetic */ void Y(HomepageImpl homepageImpl) {
        homepageImpl.applySearchContentUnderSearchBarInset();
    }

    public static /* bridge */ /* synthetic */ void Z(HomepageImpl homepageImpl) {
        homepageImpl.attachSearch();
    }

    public static /* bridge */ /* synthetic */ void a0(HomepageImpl homepageImpl) {
        homepageImpl.endHomepageSearchSpringTransition();
    }

    private void animToEdit() {
        SearchViewAnimate searchViewAnimate = this.mSearchViewAnimate;
        if (searchViewAnimate == null || searchViewAnimate.getSearchState() != 0) {
            return;
        }
        applySearchEditBlur();
        this.mSearchViewAnimate.getHintAnimationLayout().pauseHintsAnimation();
        this.mSearchViewAnimate.changeStateWithAnimation(1);
        animToEditStart();
        this.mSearchViewAnimate.setElevation(0.0f);
        captureMainContentAnimLayoutAnchors();
        this.mMainContentSpringForce.setResponse(0.35f);
        this.mSearchUiSpringForce.setResponse(0.35f);
        this.mSearchResultSpringForce.setResponse(0.35f);
        this.mMainContentSpringAnim.setStartValue(0.0f);
        this.mSearchBarEnterAnim.setStartValue(0.0f);
        this.mHomepageExitSpringAnimationPending = false;
        this.mMainContentSpringAnim.animateToFinalPosition(1000.0f);
        this.mSearchBarEnterAnim.animateToFinalPosition(1000.0f);
        if (this.mSearchResultContainer != null) {
            this.mSearchResultSpringAnim.setStartValue(0.0f);
            this.mSearchResultSpringAnim.animateToFinalPosition(1000.0f);
        }
        this.mRestoreAppBarExpend = !this.mAppBarLayout.isCollapsed();
        this.mAppBarLayout.setMode(1);
    }

    private void animToEditStart() {
        LogUtils.d(TAG, "onAnimationStart -- enter");
        this.mHomepageSearchFeature.onEnterAnimStart();
        View view = this.mSearchBgMaskView;
        if (view != null) {
            view.setVisibility(0);
        }
        if (this.mActivity != null) {
            LogUtils.d("gfxScene home_jump_to_search_page -- start");
            JankManager.getInstance().gfxSceneBegin(this.mActivity, 1, JankManagerUtil.HOME_JUMP_TO_SEARCH_ANIM, 2L);
        }
    }

    private void animToNormal() {
        this.mHomepageExitSpringAnimationPending = true;
        releaseSearchEditBlur();
        if (isHomepageBlurOverlayEnabled()) {
            setupHomepageGradientBlur();
            updateBlurOverlayExtraHeight(!this.mRestoreAppBarExpend);
        }
        LogUtils.d(TAG, "animBack");
        this.mSearchViewAnimate.changeStateWithAnimation(0);
        this.mSearchViewAnimate.setNoShadowInNormalState(this.mRestoreAppBarExpend);
        animToNormalStart();
        this.mSearchViewAnimate.setElevation(-1.0f);
        this.mAppBarLayout.setMode(0);
        if (this.mRestoreAppBarExpend) {
            this.mAppBarLayout.setExpanded(true, true);
        }
        syncAccumulatedRecyclerScrollFromList();
        captureMainContentAnimLayoutAnchors();
        this.mMainContentSpringForce.setResponse(0.35f);
        this.mSearchUiSpringForce.setResponse(0.35f);
        this.mSearchResultSpringForce.setResponse(0.1f);
        this.mMainContentSpringAnim.setStartValue(1000.0f);
        this.mSearchBarEnterAnim.setStartValue(1000.0f);
        this.mMainContentSpringAnim.animateToFinalPosition(0.0f);
        this.mSearchBarEnterAnim.animateToFinalPosition(0.0f);
        if (this.mSearchResultContainer != null) {
            this.mSearchResultSpringAnim.setStartValue(1000.0f);
            this.mSearchResultSpringAnim.animateToFinalPosition(0.0f);
        }
    }

    private void animToNormalStart() {
        LogUtils.d(TAG, "onAnimationStart -- exit");
        View view = this.mSearchBgMaskView;
        if (view != null) {
            view.setVisibility(8);
        }
        this.mHomepageSearchFeature.onExitAnimStart();
        View view2 = this.mMainLayout;
        if (view2 != null) {
            view2.setVisibility(0);
        }
        if (this.mActivity != null) {
            LogUtils.d("gfxScene search_jump_to_home_page -- start");
            JankManager.getInstance().gfxSceneBegin(this.mActivity, 2, JankManagerUtil.SEARCH_JUMP_TO_HOME_ANIM, 2L);
        }
    }

    private void applyForcedSearchOverlayAlpha() {
        SettingsCollapsableAppBarLayout settingsCollapsableAppBarLayout;
        AppBarBlurOverlayView overlayView;
        if (!this.mSearchEditBlurForced || (settingsCollapsableAppBarLayout = this.mAppBarLayout) == null || (overlayView = AppBarBlurHelper.getOverlayView(settingsCollapsableAppBarLayout)) == null) {
            return;
        }
        overlayView.setAlpha(1.0f);
    }

    private void applyHomepageAppBarBackground() {
        Activity activity;
        if (this.mAppBarLayout == null || (activity = this.mActivity) == null) {
            return;
        }
        boolean zIsRejectTheme = ThemeUtils.isRejectTheme(activity);
        View view = this.mMainLayout;
        if (view != null) {
            CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) view.getLayoutParams()).getBehavior();
            if (behavior instanceof COUIFloatingScrollingViewBehavior) {
                ((COUIFloatingScrollingViewBehavior) behavior).setHeaderOffsetEnabled(!zIsRejectTheme);
            } else {
                LogUtils.w(TAG, "unexpected behavior.");
            }
        }
        if (zIsRejectTheme) {
            disableHomepageBlurOverlay();
        } else {
            setupHomepageGradientBlur();
        }
    }

    private void applySearchContentUnderSearchBarInset() {
        COUIToolbar cOUIToolbar = this.mToolbar;
        int measuredHeight = cOUIToolbar != null ? cOUIToolbar.getMeasuredHeight() + this.mStatusBarHeight : 0;
        View view = this.mSearchResultContainer;
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            viewGroup.setPadding(0, 0, 0, 0);
            viewGroup.setClipToPadding(false);
            viewGroup.setClipChildren(false);
        }
        View view2 = this.mSearchContentLayout;
        if (view2 instanceof ViewGroup) {
            ViewGroup viewGroup2 = (ViewGroup) view2;
            viewGroup2.setClipToPadding(false);
            viewGroup2.setClipChildren(false);
        }
        applyUnderSearchBarInset(this.mSearchResultRecyclerView, measuredHeight);
        applySearchTopSpaceHeight(measuredHeight);
        Activity activity = this.mActivity;
        if (activity != null) {
            applyUnderSearchBarTopMargin(activity.findViewById(R.id.search_empty_container), measuredHeight);
            applyUnderSearchBarTopMargin(this.mActivity.findViewById(R.id.searching_by_ai_view), measuredHeight);
        }
    }

    private void applySearchEditBlur() {
        if (isHomepageBlurOverlayEnabled()) {
            boolean zIsBlurBackground = SettingsApplicationEx.isBlurBackground();
            this.mBlurOverlayExtraHeightApplied = !zIsBlurBackground;
            updateBlurOverlayExtraHeight(zIsBlurBackground);
            this.mSearchEditBlurForced = zIsBlurBackground;
            AppBarBlurOverlayView overlayView = AppBarBlurHelper.getOverlayView(this.mAppBarLayout);
            if (overlayView == null) {
                return;
            }
            overlayView.removeOnLayoutChangeListener(this.mSearchEditBlurAlphaGuard);
            if (zIsBlurBackground) {
                overlayView.addOnLayoutChangeListener(this.mSearchEditBlurAlphaGuard);
                overlayView.post(new j(this));
            }
        }
    }

    private void applySearchTopSpaceHeight(int i3) {
        View viewFindViewById;
        ViewGroup.LayoutParams layoutParams;
        Activity activity = this.mActivity;
        if (activity == null || (viewFindViewById = activity.findViewById(R.id.search_content_top_space)) == null || (layoutParams = viewFindViewById.getLayoutParams()) == null || layoutParams.height == i3) {
            return;
        }
        layoutParams.height = i3;
        viewFindViewById.setLayoutParams(layoutParams);
    }

    private void applyUnderSearchBarInset(View view, int i3) {
        if (view == null) {
            return;
        }
        view.setPadding(view.getPaddingLeft(), i3, view.getPaddingRight(), view.getPaddingBottom());
        if (view instanceof ViewGroup) {
            ((ViewGroup) view).setClipToPadding(false);
        }
    }

    private void applyUnderSearchBarTopMargin(View view, int i3) {
        if (view == null) {
            return;
        }
        if (view.getPaddingTop() != 0) {
            view.setPadding(view.getPaddingLeft(), 0, view.getPaddingRight(), view.getPaddingBottom());
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            if (marginLayoutParams.topMargin != i3) {
                marginLayoutParams.topMargin = i3;
                view.setLayoutParams(marginLayoutParams);
            }
        }
    }

    private void attachSearch() {
        this.mHomepageSearchFeature.attachSearch(false);
    }

    public static /* bridge */ /* synthetic */ void b0(HomepageImpl homepageImpl, GradientRoundRectButton gradientRoundRectButton) {
        homepageImpl.initAiSearchButtonWithHomePage(gradientRoundRectButton);
    }

    public static /* synthetic */ void c(HomepageImpl homepageImpl, View view) {
        homepageImpl.lambda$initAiSearchButtonWithHomePage$5(view);
    }

    public static /* bridge */ /* synthetic */ void c0(HomepageImpl homepageImpl) {
        homepageImpl.initAnimators();
    }

    private void cancelAllAnimators() {
        COUISpringAnimation cOUISpringAnimation = this.mSearchBarEnterAnim;
        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) {
            return;
        }
        this.mSearchBarEnterAnim.cancel();
    }

    private void cancelAnimator(Animator animator) {
        if (animator == null || !animator.isRunning()) {
            return;
        }
        animator.cancel();
    }

    private void captureMainContentAnimLayoutAnchors() {
        SettingsCollapsableAppBarLayout settingsCollapsableAppBarLayout;
        if (this.mSearchViewAnimate == null || (settingsCollapsableAppBarLayout = this.mAppBarLayout) == null) {
            return;
        }
        this.mMainAnimSearchTopMinusScrollPx = settingsCollapsableAppBarLayout.getTotalScrollRange() + this.mAppBarLayout.getOffset();
        this.mMainAnimAppBarMeasuredHeightPx = this.mAppBarLayout.getMeasuredHeight();
    }

    public static /* synthetic */ void d(HomepageImpl homepageImpl, View view, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        homepageImpl.lambda$new$1(view, i3, i4, i5, i6, i7, i8, i9, i10);
    }

    public static /* bridge */ /* synthetic */ void d0(HomepageImpl homepageImpl, Activity activity) {
        homepageImpl.initSearchView(activity);
    }

    private void detachSearch() {
        this.mHomepageSearchFeature.detachSearch();
    }

    private void disableHomepageBlurOverlay() {
        AppBarBlurOverlayView overlayView;
        View viewFindViewById;
        Activity activity = this.mActivity;
        if (activity != null && (viewFindViewById = activity.findViewById(R.id.blur_overlay)) != null) {
            viewFindViewById.setVisibility(8);
        }
        SettingsCollapsableAppBarLayout settingsCollapsableAppBarLayout = this.mAppBarLayout;
        if (settingsCollapsableAppBarLayout != null && (overlayView = AppBarBlurHelper.getOverlayView(settingsCollapsableAppBarLayout)) != null) {
            overlayView.setExtraHeightPx(0);
        }
        this.mBlurOverlayExtraHeightApplied = false;
    }

    public static /* synthetic */ void e(HomepageImpl homepageImpl) {
        homepageImpl.lambda$initSearchView$2();
    }

    public static /* bridge */ /* synthetic */ boolean e0(HomepageImpl homepageImpl) {
        return homepageImpl.isHomepageActivityAlive();
    }

    private void endHomepageSearchSpringTransition() {
        COUISpringAnimation cOUISpringAnimation = this.mMainContentSpringAnim;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning() && this.mMainContentSpringAnim.canSkipToEnd()) {
            this.mMainContentSpringAnim.skipToEnd();
        }
        COUISpringAnimation cOUISpringAnimation2 = this.mSearchBarEnterAnim;
        if (cOUISpringAnimation2 != null && cOUISpringAnimation2.isRunning() && this.mSearchBarEnterAnim.canSkipToEnd()) {
            this.mSearchBarEnterAnim.skipToEnd();
        }
        COUISpringAnimation cOUISpringAnimation3 = this.mSearchResultSpringAnim;
        if (cOUISpringAnimation3 != null && cOUISpringAnimation3.isRunning() && this.mSearchResultSpringAnim.canSkipToEnd()) {
            this.mSearchResultSpringAnim.skipToEnd();
        }
    }

    public static /* synthetic */ void f(HomepageImpl homepageImpl, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        homepageImpl.lambda$initAnimators$9(cOUIDynamicAnimation, f3, f4);
    }

    public static /* bridge */ /* synthetic */ boolean f0(HomepageImpl homepageImpl) {
        return homepageImpl.isHomepageSearchSpringTransitionRunning();
    }

    public static /* synthetic */ void g(HomepageImpl homepageImpl, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        homepageImpl.lambda$initAnimators$8(cOUIDynamicAnimation, z2, f3, f4);
    }

    public static /* bridge */ /* synthetic */ boolean g0(HomepageImpl homepageImpl, OplusSettingsHomepageActivity oplusSettingsHomepageActivity, Intent intent) {
        return homepageImpl.launchSearchFromShortcut(oplusSettingsHomepageActivity, intent);
    }

    public static /* synthetic */ void h(HomepageImpl homepageImpl, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        homepageImpl.lambda$initAnimators$7(cOUIDynamicAnimation, z2, f3, f4);
    }

    public static /* bridge */ /* synthetic */ void h0(HomepageImpl homepageImpl, int i3) {
        homepageImpl.onFoldableScreenStateChange(i3);
    }

    public static /* synthetic */ Unit i(HomepageImpl homepageImpl, View view) {
        return homepageImpl.lambda$initSearchResultView$4(view);
    }

    public static /* bridge */ /* synthetic */ void i0(HomepageImpl homepageImpl) {
        homepageImpl.refreshHomepageAppBarBackgroundOnBlurStateChange();
    }

    private void initAiSearchButtonWithHomePage(GradientRoundRectButton gradientRoundRectButton) {
        if (gradientRoundRectButton != null) {
            LogUtils.d(TAG, "initAiSearchButtonWithHomePage: " + gradientRoundRectButton);
            AiSearchConstants aiSearchConstants = AiSearchConstants.INSTANCE;
            gradientRoundRectButton.setGradientColorAndPositionArray(aiSearchConstants.getAI_SEARCH_GRADIENT_COLORS(), aiSearchConstants.getSTOKE_COLOR_POSITION());
            gradientRoundRectButton.setOnClickListener(new k(this));
        }
    }

    private void initAnimators() {
        Activity activity = this.mActivity;
        this.mInitSearchViewY = activity != null ? activity.getResources().getDimensionPixelOffset(R.dimen.homepage_init_searchbar_top) : 0;
        COUISpringForce cOUISpringForce = new COUISpringForce();
        this.mMainContentSpringForce = cOUISpringForce;
        cOUISpringForce.setBounce(0.0f);
        this.mMainContentSpringForce.setResponse(0.35f);
        COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(new FloatValueHolder());
        this.mMainContentSpringAnim = cOUISpringAnimation;
        cOUISpringAnimation.setSpring(this.mMainContentSpringForce);
        this.mMainContentSpringAnim.addUpdateListener(new l(this));
        this.mMainContentSpringAnim.addEndListener(new m(this));
        COUISpringForce cOUISpringForce2 = new COUISpringForce();
        this.mSearchUiSpringForce = cOUISpringForce2;
        cOUISpringForce2.setBounce(0.0f);
        this.mSearchUiSpringForce.setResponse(0.35f);
        COUISpringAnimation cOUISpringAnimation2 = new COUISpringAnimation(new FloatValueHolder());
        this.mSearchBarEnterAnim = cOUISpringAnimation2;
        cOUISpringAnimation2.setSpring(this.mSearchUiSpringForce);
        this.mSearchBarEnterAnim.addEndListener(new n(this));
        COUISpringForce cOUISpringForce3 = new COUISpringForce();
        this.mSearchResultSpringForce = cOUISpringForce3;
        cOUISpringForce3.setBounce(0.0f);
        this.mSearchResultSpringForce.setResponse(0.35f);
        COUISpringAnimation cOUISpringAnimation3 = new COUISpringAnimation(new FloatValueHolder());
        this.mSearchResultSpringAnim = cOUISpringAnimation3;
        cOUISpringAnimation3.setSpring(this.mSearchResultSpringForce);
        this.mSearchResultSpringAnim.addUpdateListener(new o(this));
        this.mSearchResultSpringAnim.addEndListener(new d(this));
    }

    private void initSearchResultView() {
        AsyncViewStub asyncViewStub;
        AsyncViewStub asyncViewStub2;
        if (this.mSearchResultContainer == null || ((asyncViewStub2 = this.mSearchResultViewStub) != null && isStubNotInflate(asyncViewStub2))) {
            LogUtils.d(TAG, "initSearchView");
            this.mSearchResultViewStub.inflateAsync(new AnonymousClass6());
        }
        if (this.mAISearchResultContainer == null || ((asyncViewStub = this.mAISearchResultViewStub) != null && isStubNotInflate(asyncViewStub))) {
            this.mAISearchResultViewStub.inflateAsync(new i(this));
        }
    }

    private void initSearchView(Activity activity) {
        this.mHomepageSearchFeature.initSearchView(new AnonymousClass3(), new AnonymousClass4());
        SearchViewAnimate searchViewAnimate = this.mSearchViewAnimate;
        if (searchViewAnimate != null) {
            searchViewAnimate.setOnAnimationListener(new AnonymousClass5(this));
            this.mSearchViewAnimate.post(new e(this));
            this.mSearchViewAnimate.setSearchAnimateType(0);
            this.mSearchViewAnimate.setNoShadowInNormalState(true);
            this.mSearchViewAnimate.setNoMaterialInContent(false);
            this.mHomepageSearchFeature.applyHomepageSearchIcon();
        }
        this.mSearchBgMaskContainer = activity.findViewById(R.id.search_bg_mask_container);
        View viewFindViewById = activity.findViewById(R.id.search_bg_mask);
        this.mSearchBgMaskView = viewFindViewById;
        viewFindViewById.setOnTouchListener(new f(this));
        this.mSearchResultViewStub = (AsyncViewStub) activity.findViewById(R.id.stub_search_result);
        this.mAISearchResultViewStub = (AsyncViewStub) activity.findViewById(R.id.stub_ai_search_all_result_layout);
    }

    private boolean isHomepageActivityAlive() {
        Activity activity = this.mActivity;
        return (activity == null || activity.isFinishing() || this.mActivity.isDestroyed()) ? false : true;
    }

    private boolean isHomepageBlurOverlayEnabled() {
        Activity activity = this.mActivity;
        return (activity == null || ThemeUtils.isRejectTheme(activity)) ? false : true;
    }

    private boolean isHomepageSearchSpringTransitionRunning() {
        COUISpringAnimation cOUISpringAnimation = this.mMainContentSpringAnim;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            return true;
        }
        COUISpringAnimation cOUISpringAnimation2 = this.mSearchBarEnterAnim;
        if (cOUISpringAnimation2 != null && cOUISpringAnimation2.isRunning()) {
            return true;
        }
        COUISpringAnimation cOUISpringAnimation3 = this.mSearchResultSpringAnim;
        return cOUISpringAnimation3 != null && cOUISpringAnimation3.isRunning();
    }

    public static boolean isNightModeChangedOnRecreate(Bundle bundle, Configuration configuration) {
        return (bundle == null || !bundle.containsKey(KEY_SAVED_UI_MODE_NIGHT_FOR_RECREATE) || bundle.getInt(KEY_SAVED_UI_MODE_NIGHT_FOR_RECREATE) == nightModeBits(configuration)) ? false : true;
    }

    private boolean isStubNotInflate(AsyncViewStub asyncViewStub) {
        return asyncViewStub.getParent() != null;
    }

    public static /* synthetic */ boolean j(HomepageImpl homepageImpl, View view, MotionEvent motionEvent) {
        return homepageImpl.lambda$initSearchView$3(view, motionEvent);
    }

    public static /* bridge */ /* synthetic */ void j0(HomepageImpl homepageImpl) {
        homepageImpl.registerMaterialBlurStateListener();
    }

    public static /* synthetic */ void k(HomepageImpl homepageImpl, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        homepageImpl.lambda$initAnimators$10(cOUIDynamicAnimation, z2, f3, f4);
    }

    public static /* bridge */ /* synthetic */ void k0(HomepageImpl homepageImpl) {
        homepageImpl.resetHeaderLayout();
    }

    public static /* synthetic */ void l(HomepageImpl homepageImpl) {
        homepageImpl.lambda$new$0();
    }

    public static /* bridge */ /* synthetic */ void l0(HomepageImpl homepageImpl, boolean z2) {
        homepageImpl.updateBlurOverlayExtraHeight(z2);
    }

    private /* synthetic */ void lambda$initAiSearchButtonWithHomePage$5(View view) {
        this.mHomepageSearchFeature.onAIButtonClick();
    }

    private /* synthetic */ void lambda$initAnimators$10(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        if (!z2 && this.mSearchViewAnimate.getSearchState() == 0) {
            LogUtils.d(TAG, "searchResultSpringAnim onEnd -- exit (result panel alpha done, GONE deferred)");
        }
    }

    private /* synthetic */ void lambda$initAnimators$6(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        float f5 = f3 / 1000.0f;
        float fPow = 1.0f - f5;
        if (this.mSearchViewAnimate.getSearchState() == 1) {
            fPow = (float) Math.pow(fPow, 3.0d);
        }
        LogUtils.d(TAG, "mainContentSpringAnim value = " + f5 + " alpha = " + fPow);
        this.mCollapsingToolbarLayout.setTitleTransform((0.2f * fPow) + 0.8f, fPow, f5 * (-200.0f));
        this.mMainLayout.setAlpha(fPow);
    }

    private /* synthetic */ void lambda$initAnimators$7(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        View view;
        if (z2) {
            return;
        }
        if (this.mSearchViewAnimate.getSearchState() != 1) {
            if (this.mSearchViewAnimate.getSearchState() != 0 || (view = this.mSearchResultContainer) == null) {
                return;
            }
            view.setVisibility(8);
            return;
        }
        LogUtils.d(TAG, "mainContentSpringAnim onEnd -- enter (toolbar/main hide done)");
        View view2 = this.mMainLayout;
        if (view2 != null) {
            view2.setVisibility(4);
        }
    }

    private /* synthetic */ void lambda$initAnimators$8(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        if (z2) {
            return;
        }
        if (this.mSearchViewAnimate.getSearchState() != 0) {
            LogUtils.d(TAG, "searchUiSpringAnim onEnd -- enter (search bar show done)");
            this.mHomepageSearchFeature.onEnterAnimEnd();
            if (this.mActivity != null) {
                LogUtils.d("gfxScene home_jump_to_search_page -- end");
                JankManager.getInstance().gfxSceneEnd(this.mActivity, 1);
                return;
            }
            return;
        }
        this.mHomepageExitSpringAnimationPending = false;
        this.mHomepageSearchFeature.onExitAnimEnd();
        this.mSearchViewAnimate.getHintAnimationLayout().resumeHintsAnimation();
        LogUtils.d(TAG, "searchUiSpringAnim onEnd -- exit (search bar hide done)");
        if (this.mActivity != null) {
            LogUtils.d("gfxScene search_jump_to_home_page -- end");
            JankManager.getInstance().gfxSceneEnd(this.mActivity, 2);
        }
    }

    private /* synthetic */ void lambda$initAnimators$9(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        View view = this.mSearchResultContainer;
        if (view == null) {
            return;
        }
        float f5 = 1.0f - (f3 / 1000.0f);
        view.setTranslationY(this.mInitSearchViewY * f5);
        this.mSearchResultContainer.setAlpha(1.0f - f5);
    }

    private /* synthetic */ Unit lambda$initSearchResultView$4(View view) {
        if (!isHomepageActivityAlive()) {
            LogUtils.w(TAG, "initAISearchResultView: activity destroyed, skip");
            return null;
        }
        this.mHomepageSearchFeature.initAISearchResultView();
        View viewFindViewById = this.mActivity.findViewById(R.id.ai_search_result_toolbar);
        if (viewFindViewById != null) {
            ViewGroup.LayoutParams layoutParams = viewFindViewById.getLayoutParams();
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                marginLayoutParams.topMargin = this.mStatusBarHeight;
                viewFindViewById.setLayoutParams(marginLayoutParams);
            }
        }
        return null;
    }

    private /* synthetic */ void lambda$initSearchView$2() {
        this.mHomepageSearchFeature.setQueryText("");
    }

    private /* synthetic */ boolean lambda$initSearchView$3(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() != 0) {
            return false;
        }
        hideKeyboard();
        return true;
    }

    private /* synthetic */ void lambda$new$0() {
        LogUtils.d(TAG, "mPressBack#onBackInvoked");
        onBackPressed();
    }

    private /* synthetic */ void lambda$new$1(View view, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        applyForcedSearchOverlayAlpha();
    }

    private /* synthetic */ void lambda$onConfigurationChanged$11(boolean z2) {
        COUIToolbar cOUIToolbar;
        View view = this.mSearchBgMaskContainer;
        if (view != null && (cOUIToolbar = this.mToolbar) != null) {
            view.setPadding(0, cOUIToolbar.getMeasuredHeight() + this.mStatusBarHeight, 0, 0);
        }
        applySearchContentUnderSearchBarInset();
        if (!z2) {
            this.mAccumulatedRecyclerScroll = 0.0f;
            this.mSearchViewAnimate.setTranslationY(0.0f);
        } else {
            View view2 = this.mSearchResultContainer;
            if (view2 != null) {
                view2.setTranslationY(0.0f);
            }
        }
    }

    private boolean launchSearchFromShortcut(OplusSettingsHomepageActivity oplusSettingsHomepageActivity, Intent intent) {
        if (intent == null) {
            return false;
        }
        if (!ACTION_LAUNCH_SEARCH.equals(intent.getAction())) {
            return true;
        }
        SearchFeatureProvider searchFeatureProvider = FeatureFactory.getFeatureFactory().getSearchFeatureProvider();
        if (!WizardManagerHelper.isDeviceProvisioned(oplusSettingsHomepageActivity) || !SettingsUtils.isSettingsIntelligenceEnabled(oplusSettingsHomepageActivity)) {
            return true;
        }
        launchSettingsSearch(searchFeatureProvider, oplusSettingsHomepageActivity, 1502, false);
        return true;
    }

    private void launchSettingsSearch(SearchFeatureProvider searchFeatureProvider, Activity activity, int i3, boolean z2) {
        Context applicationContext = activity.getApplicationContext();
        Intent intentBuildSearchIntent = searchFeatureProvider.buildSearchIntent(applicationContext, i3);
        if (activity.getPackageManager().queryIntentActivities(intentBuildSearchIntent, 65536).isEmpty()) {
            return;
        }
        FeatureFactory featureFactory = FeatureFactory.getFeatureFactory();
        featureFactory.getSlicesFeatureProvider().indexSliceDataAsync(applicationContext);
        featureFactory.getMetricsFeatureProvider().action(applicationContext, 226, new Pair[0]);
        DcsTrackerEntry.onCommon(applicationContext, DcsTrackerEntry.START_SEARCH);
        if (!z2) {
            activity.startActivityForResult(intentBuildSearchIntent, 501);
            return;
        }
        intentBuildSearchIntent.putExtra(EXTRA_COLOR_FROM, VALUE_CLICK_SEARCH_VIEW);
        activity.startActivityForResult(intentBuildSearchIntent, 501);
        activity.overridePendingTransition(R.anim.search_page_enter, R.anim.search_page_enter_exit);
    }

    public static /* synthetic */ void m(HomepageImpl homepageImpl, boolean z2) {
        homepageImpl.lambda$onConfigurationChanged$11(z2);
    }

    public static /* bridge */ /* synthetic */ void m0(HomepageImpl homepageImpl) {
        homepageImpl.updateSearchContentIndention();
    }

    public static /* synthetic */ void n(HomepageImpl homepageImpl) {
        homepageImpl.applyForcedSearchOverlayAlpha();
    }

    private static int nightModeBits(Configuration configuration) {
        return configuration.uiMode & 48;
    }

    public static /* synthetic */ void o(HomepageImpl homepageImpl, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        homepageImpl.lambda$initAnimators$6(cOUIDynamicAnimation, f3, f4);
    }

    private void onFoldableScreenStateChange(int i3) {
        Activity activity;
        if ((FoldableScreenMonitor.isUnFolded(i3) || ((activity = this.mActivity) != null && activity.isResumed())) && this.mHomepageSearchFeature.getMIsSearchMode()) {
            LogUtils.d(TAG, "onFoldableScreenStateChange " + i3 + ", search again.");
            this.mHomepageSearchFeature.refreshIndex();
        }
    }

    public static /* bridge */ /* synthetic */ float p(HomepageImpl homepageImpl) {
        return homepageImpl.mAccumulatedRecyclerScroll;
    }

    private void preloadSearchData() {
        AnonymousClass7 anonymousClass7 = new AnonymousClass7();
        this.mSearchIndexCallback = anonymousClass7;
        SearchPreloadUtils.INSTANCE.getInstance().preloadSearchData(this.mActivity.getApplicationContext(), anonymousClass7);
    }

    public static /* bridge */ /* synthetic */ Activity q(HomepageImpl homepageImpl) {
        return homepageImpl.mActivity;
    }

    public static /* bridge */ /* synthetic */ SettingsCollapsableAppBarLayout r(HomepageImpl homepageImpl) {
        return homepageImpl.mAppBarLayout;
    }

    private void refreshHomepageAppBarBackgroundOnBlurStateChange() {
        if (this.mAppBarLayout == null || this.mActivity == null) {
            return;
        }
        applyHomepageAppBarBackground();
        syncBlurOverlayExtraHeightForCurrentState();
    }

    private void registerMaterialBlurStateListener() {
        if (this.mMaterialBlurListenerRegistered) {
            return;
        }
        COUIMaterialStateManager.INSTANCE.addOnMaterialStateChangedListener(this.mMaterialBlurStateListener);
        this.mMaterialBlurListenerRegistered = true;
    }

    private void releaseSearchEditBlur() {
        AppBarBlurOverlayView overlayView;
        this.mSearchEditBlurForced = false;
        SettingsCollapsableAppBarLayout settingsCollapsableAppBarLayout = this.mAppBarLayout;
        if (settingsCollapsableAppBarLayout == null || (overlayView = AppBarBlurHelper.getOverlayView(settingsCollapsableAppBarLayout)) == null) {
            return;
        }
        overlayView.removeOnLayoutChangeListener(this.mSearchEditBlurAlphaGuard);
    }

    private void resetHeaderLayout() {
        SettingsCollapsingToolbarLayout settingsCollapsingToolbarLayout;
        if (this.mAppBarLayout == null || (settingsCollapsingToolbarLayout = this.mCollapsingToolbarLayout) == null || !settingsCollapsingToolbarLayout.isExpanded()) {
            return;
        }
        this.mAppBarLayout.reset();
    }

    public static /* bridge */ /* synthetic */ FoldableScreenMonitor s(HomepageImpl homepageImpl) {
        return homepageImpl.mFoldableScreenMonitor;
    }

    public static void saveNightModeToInstanceState(Bundle bundle, Configuration configuration) {
        bundle.putInt(KEY_SAVED_UI_MODE_NIGHT_FOR_RECREATE, nightModeBits(configuration));
    }

    private void setupHomepageGradientBlur() {
        if (this.mAppBarLayout == null || this.mActivity == null || !isHomepageBlurOverlayEnabled()) {
            return;
        }
        View viewFindViewById = this.mActivity.findViewById(R.id.blur_overlay);
        if (viewFindViewById != null) {
            viewFindViewById.setVisibility(0);
        }
        AppBarBlurHelper.setUpGradientBlurBackground((COUIAppBarLayout) this.mAppBarLayout);
        LogUtils.d(TAG, "app bar apply blur, blur enable = " + SettingsApplicationEx.isBlurBackground());
        syncHomepageAppBarShellBackground();
        this.mBlurOverlayExtraHeightApplied = true;
        updateBlurOverlayExtraHeight(false);
    }

    private boolean shouldSkipResetHeaderForSearchEdit() {
        if (this.mIsInSearchState) {
            return true;
        }
        SearchViewAnimate searchViewAnimate = this.mSearchViewAnimate;
        return searchViewAnimate != null && searchViewAnimate.getSearchState() == 1;
    }

    private void syncAccumulatedRecyclerScrollFromList() {
        Activity activity;
        RecyclerView recyclerView = this.mTopLevelSettingsRecyclerView;
        if (recyclerView == null && (activity = this.mActivity) != null) {
            recyclerView = (RecyclerView) activity.findViewById(R.id.recycler_view);
        }
        if (recyclerView != null) {
            this.mAccumulatedRecyclerScroll = Math.max(0.0f, recyclerView.computeVerticalScrollOffset());
        }
    }

    private void syncBlurOverlayExtraHeightForCurrentState() {
        if (!isHomepageBlurOverlayEnabled() || this.mAppBarLayout == null) {
            return;
        }
        boolean z2 = SettingsApplicationEx.isBlurBackground() && this.mAppBarLayout.isCollapsed();
        this.mBlurOverlayExtraHeightApplied = !z2;
        updateBlurOverlayExtraHeight(z2);
    }

    private void syncHomepageAppBarShellBackground() {
        if (this.mAppBarLayout == null || this.mActivity == null || !isHomepageBlurOverlayEnabled()) {
            return;
        }
        if (SettingsApplicationEx.isBlurBackground()) {
            this.mAppBarLayout.setBackground(null);
        } else {
            this.mAppBarLayout.setBackgroundColor(COUIContextUtil.getAttrColor(this.mActivity, R.attr.couiColorBackgroundWithCard));
        }
    }

    public static /* bridge */ /* synthetic */ HomepageSearchFeature t(HomepageImpl homepageImpl) {
        return homepageImpl.mHomepageSearchFeature;
    }

    private void tryRefreshIndex() {
        if (CompatWindowUtil.isSupportCompactWindow()) {
            this.mHomepageSearchFeature.tryRefreshIndex();
        }
    }

    public static /* bridge */ /* synthetic */ boolean u(HomepageImpl homepageImpl) {
        return homepageImpl.mIsInSearchState;
    }

    private void unregisterMaterialBlurStateListener() {
        if (this.mMaterialBlurListenerRegistered) {
            COUIMaterialStateManager.INSTANCE.removeOnMaterialStateChangedListener(this.mMaterialBlurStateListener);
            this.mMaterialBlurListenerRegistered = false;
        }
    }

    private void updateBlurOverlayExtraHeight(boolean z2) {
        SettingsCollapsableAppBarLayout settingsCollapsableAppBarLayout;
        AppBarBlurOverlayView overlayView;
        if (!isHomepageBlurOverlayEnabled() || (settingsCollapsableAppBarLayout = this.mAppBarLayout) == null || this.mActivity == null || this.mBlurOverlayExtraHeightApplied == z2 || (overlayView = AppBarBlurHelper.getOverlayView(settingsCollapsableAppBarLayout)) == null) {
            return;
        }
        overlayView.setExtraHeightPx(z2 ? this.mActivity.getResources().getDimensionPixelSize(R.dimen.homepage_appbar_blur_extra_height) : 0);
        this.mBlurOverlayExtraHeightApplied = z2;
    }

    private void updateSearchContentIndention() {
        SettingsUtils.setViewSupportContentIndentation(this.mSearchContentLayout, false);
    }

    public static /* bridge */ /* synthetic */ COUIAppBarLayout.OnOffsetChangedListener v(HomepageImpl homepageImpl) {
        return homepageImpl.mOffsetChangedListener;
    }

    public static /* bridge */ /* synthetic */ float w(HomepageImpl homepageImpl) {
        return homepageImpl.mScrollFraction;
    }

    public static /* bridge */ /* synthetic */ View x(HomepageImpl homepageImpl) {
        return homepageImpl.mSearchBgMaskContainer;
    }

    public static /* bridge */ /* synthetic */ boolean y(HomepageImpl homepageImpl) {
        return homepageImpl.mSearchDisabled;
    }

    public static /* bridge */ /* synthetic */ boolean z(HomepageImpl homepageImpl) {
        return homepageImpl.mSearchEditBlurForced;
    }

    @Override // com.oplus.settings.feature.homepage.search.HomepageSearchFeature.Callback
    public void hideKeyboard() {
        View currentFocus;
        Activity activity = this.mActivity;
        if (activity != null && (currentFocus = activity.getCurrentFocus()) != null) {
            ((InputMethodManager) activity.getSystemService(SystemUpdateScreenStateConfig.KEY_INPUT_METHOD)).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
        SearchViewAnimate searchViewAnimate = this.mSearchViewAnimate;
        if (searchViewAnimate != null) {
            searchViewAnimate.openSoftInput(false);
        }
        RecyclerView recyclerView = this.mSearchResultRecyclerView;
        if (recyclerView == null || recyclerView.getVisibility() != 0) {
            return;
        }
        this.mSearchResultRecyclerView.requestFocus();
    }

    @Override // com.oplus.settings.feature.homepage.BaseHomePageImpl
    public boolean initToolbar(OplusSettingsHomepageActivity oplusSettingsHomepageActivity) {
        COUIToolbar cOUIToolbar = (COUIToolbar) oplusSettingsHomepageActivity.findViewById(R.id.toolbar_options);
        if (cOUIToolbar == null) {
            return true;
        }
        int attrColor = OplusContextUtil.getAttrColor(oplusSettingsHomepageActivity, R.attr.couiColorPrimaryNeutral);
        cOUIToolbar.setTitleTextColor(Color.argb(255, Color.red(attrColor), Color.green(attrColor), Color.blue(attrColor)));
        oplusSettingsHomepageActivity.setSupportActionBar(cOUIToolbar);
        return true;
    }

    @Override // com.oplus.settings.feature.homepage.search.HomepageSearchFeature.Callback
    public boolean isHomepageSearchExitAnimationRunning() {
        return this.mHomepageExitSpringAnimationPending;
    }

    @Override // com.oplus.settings.feature.homepage.BaseHomePageImpl
    public boolean onBackPressed() {
        if (!this.mHomepageSearchFeature.getMIsSearchMode()) {
            return false;
        }
        this.mHomepageSearchFeature.recordAutoCompleteExitIfNeeded();
        if (this.mHomepageSearchFeature.checkAISearchResultShow()) {
            return true;
        }
        LogUtils.d(TAG, "onBackPressed -- change to normal");
        animToNormal();
        return true;
    }

    @Override // com.oplus.settings.feature.homepage.BaseHomePageImpl
    public void onConfigurationChanged(Configuration configuration) {
        LogUtils.d(TAG, "onConfigurationChanged");
        boolean zShouldSkipResetHeaderForSearchEdit = shouldSkipResetHeaderForSearchEdit();
        super.onConfigurationChanged(configuration);
        Activity activity = this.mActivity;
        if (activity != null) {
            CompatWindowUtil.onHomePageConfigurationChange((AppCompatActivity) activity, configuration);
        }
        updateSearchContentIndention();
        Activity activity2 = this.mActivity;
        if (activity2 == null || this.mSearchViewAnimate == null) {
            return;
        }
        Context contextCreateConfigurationContext = activity2.createConfigurationContext(configuration);
        int dimensionPixelOffset = contextCreateConfigurationContext.getResources().getDimensionPixelOffset(R.dimen.coui_search_view_padding_start_in_toolbar);
        int dimensionPixelOffset2 = contextCreateConfigurationContext.getResources().getDimensionPixelOffset(R.dimen.coui_search_view_padding_end_in_toolbar);
        SearchViewAnimate searchViewAnimate = this.mSearchViewAnimate;
        searchViewAnimate.setPadding(dimensionPixelOffset, searchViewAnimate.getPaddingTop(), dimensionPixelOffset2, this.mSearchViewAnimate.getPaddingBottom());
        this.mSearchViewAnimate.refresh();
        this.mHomepageSearchFeature.applyHomepageSearchIcon();
        this.mInitSearchViewY = contextCreateConfigurationContext.getResources().getDimensionPixelOffset(R.dimen.homepage_init_searchbar_top);
        this.mStatusBarHeight = SettingsUtils.getStatusBarHeight(this.mActivity);
        HomepageSearchFeature homepageSearchFeature = this.mHomepageSearchFeature;
        if (homepageSearchFeature != null) {
            homepageSearchFeature.updateImgTopPadding(this.mActivity);
        }
        this.mSearchViewAnimate.post(new h(this, zShouldSkipResetHeaderForSearchEdit));
    }

    @Override // com.oplus.settings.feature.homepage.BaseHomePageImpl
    public void onCreate(OplusSettingsHomepageActivity oplusSettingsHomepageActivity, Bundle bundle) {
        super.onCreate(oplusSettingsHomepageActivity, bundle);
        CompatWindowUtil.onHomePageCreate(oplusSettingsHomepageActivity);
        this.mActivity = oplusSettingsHomepageActivity;
        this.mNeedRestoreColdStartState = !isNightModeChangedOnRecreate(bundle, oplusSettingsHomepageActivity.getResources().getConfiguration());
        if (bundle != null) {
            this.mAccumulatedRecyclerScroll = bundle.getFloat(KEY_SAVED_SEARCH_VIEW_ACCUMULATE_TRANSITION_Y);
        }
        this.mSearchDisabled = HomepageSearchFeature.isSearchDisabled(oplusSettingsHomepageActivity);
        this.mHomepageSearchFeature.onCreate(oplusSettingsHomepageActivity);
        this.mMainLayout = oplusSettingsHomepageActivity.findViewById(R.id.main_content_scrollable_container);
        AsyncViewStub asyncViewStub = (AsyncViewStub) oplusSettingsHomepageActivity.findViewById(R.id.stub_toolbar_layout);
        if (asyncViewStub == null || !isStubNotInflate(asyncViewStub)) {
            return;
        }
        asyncViewStub.inflate(this.mActivity, new AnonymousClass2(oplusSettingsHomepageActivity));
    }

    @Override // com.oplus.settings.feature.homepage.BaseHomePageImpl
    public void onDestroy() {
        COUIAppBarLayout.OnOffsetChangedListener onOffsetChangedListener;
        super.onDestroy();
        LogUtils.d(TAG, "onDestroy: ");
        detachSearch();
        cancelAllAnimators();
        SettingsCollapsableAppBarLayout settingsCollapsableAppBarLayout = this.mAppBarLayout;
        if (settingsCollapsableAppBarLayout != null && (onOffsetChangedListener = this.mOffsetChangedListener) != null) {
            settingsCollapsableAppBarLayout.removeOnOffsetChangedListener(onOffsetChangedListener);
            this.mOffsetChangedListener = null;
        }
        Activity activity = this.mActivity;
        if (activity != null && this.mPressBack != null) {
            try {
                activity.getOnBackInvokedDispatcher().unregisterOnBackInvokedCallback(this.mPressBack);
            } catch (Exception e3) {
                LogUtils.e(TAG, "Failed to unregister OnBackInvokedCallback: " + e3);
            }
        }
        FoldableScreenMonitor foldableScreenMonitor = this.mFoldableScreenMonitor;
        if (foldableScreenMonitor != null) {
            foldableScreenMonitor.unregister();
            this.mFoldableScreenMonitor = null;
        }
        unregisterMaterialBlurStateListener();
        this.mSearchIndexCallback = null;
        this.mActivity = null;
    }

    @Override // com.oplus.settings.feature.homepage.BaseHomePageImpl
    public boolean onNewIntent(OplusSettingsHomepageActivity oplusSettingsHomepageActivity, Intent intent) {
        return launchSearchFromShortcut(oplusSettingsHomepageActivity, intent);
    }

    @Override // com.oplus.settings.feature.homepage.search.HomepageSearchFeature.Callback
    public void onOnStateChangeEdit() {
        LogUtils.d(TAG, "onOnStateChangeEdit: attach to settings search;");
        attachSearch();
        initSearchResultView();
        this.mIsInSearchState = true;
        this.mActivity.getOnBackInvokedDispatcher().registerOnBackInvokedCallback(0, this.mPressBack);
    }

    @Override // com.oplus.settings.feature.homepage.search.HomepageSearchFeature.Callback
    public void onOnStateChangeNormal() {
        if (LogUtils.isDebugMode()) {
            LogUtils.d(TAG, "onOnStateChangeNormal: mIsStopped: " + this.mIsStopped);
        }
        this.mIsInSearchState = false;
        if (this.mIsStopped) {
            detachSearch();
        }
        this.mActivity.getOnBackInvokedDispatcher().unregisterOnBackInvokedCallback(this.mPressBack);
    }

    @Override // com.oplus.settings.feature.homepage.BaseHomePageImpl
    public void onPause() {
        LogUtils.d(TAG, "onPause");
        this.mHomepageSearchFeature.recordSearchBehaviorEvent("onPause", null);
        this.mHomepageSearchFeature.recordAiSearchResultArrayEvent("onPause", null);
    }

    @Override // com.oplus.settings.feature.homepage.BaseHomePageImpl
    public void onRestart() {
        LogUtils.d(TAG, "onRestart: mSearchDisabled: " + this.mSearchDisabled + ", mIsInSearchState: " + this.mIsInSearchState);
        if (this.mSearchDisabled || !this.mIsInSearchState) {
            return;
        }
        boolean z2 = this.mSkipRec;
        this.mSkipRec = true;
        this.mHomepageSearchFeature.attachSearch(z2);
    }

    @Override // com.oplus.settings.feature.homepage.BaseHomePageImpl
    public void onResume() {
        LogUtils.d(TAG, "onResume");
        this.mIsStopped = false;
        if (this.mSearchViewAnimate != null) {
            LogUtils.d(TAG, "onResume: search state = " + this.mSearchViewAnimate.getSearchState());
            if (this.mSearchViewAnimate.getSearchState() != 1) {
                this.mSearchViewAnimate.openSoftInput(false);
            }
        }
        if (this.mIsPreload) {
            preloadSearchData();
            this.mIsPreload = false;
        }
        this.mHomepageSearchFeature.refreshIndex();
        this.mHomepageSearchFeature.recordCurrentAiSearchResultData();
    }

    @Override // com.oplus.settings.feature.homepage.BaseHomePageImpl
    public void onSaveInstanceState(OplusSettingsHomepageActivity oplusSettingsHomepageActivity, Bundle bundle) {
        saveNightModeToInstanceState(bundle, oplusSettingsHomepageActivity.getResources().getConfiguration());
        bundle.putFloat(KEY_SAVED_SEARCH_VIEW_ACCUMULATE_TRANSITION_Y, this.mAccumulatedRecyclerScroll);
    }

    @Override // com.oplus.settings.feature.homepage.search.HomepageSearchFeature.Callback
    public void onSearchIntentItemClick(SearchIntentItem searchIntentItem) {
        String title = searchIntentItem.getTitle();
        String deepLink = searchIntentItem.getDeepLink();
        if (!TextUtils.isEmpty(deepLink)) {
            try {
                if (this.mActivity == null) {
                    this.mHomepageSearchFeature.setQueryText(title);
                    return;
                }
                this.mSkipRec = true;
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(deepLink));
                intent.addFlags(TransitionUtil.FLAG_IS_DESKTOP_WALLPAPER_ACTIVITY);
                this.mActivity.startActivity(intent);
                return;
            } catch (Exception e3) {
                LogUtils.e(TAG, "onSearchIntentItemClick deepLink error " + title + ", " + e3.getMessage());
                this.mHomepageSearchFeature.setQueryText(title);
                return;
            }
        }
        Intent intent2 = searchIntentItem.getIntent();
        if (intent2 == null) {
            this.mHomepageSearchFeature.setQueryText(title);
            return;
        }
        hideKeyboard();
        intent2.putExtra(SettingsActivity.EXTRA_OPLUS_FROM_MAIN_PAGE, true);
        intent2.putExtra(SettingsUtils.EXTRA_SETTINGS_FROM_SEARCH, true);
        intent2.putExtra(SettingsDcsRecordUtils.SETTIGNS_FROM, "1");
        intent2.putExtra(SettingsDcsRecordUtils.SEARCH_QUERY, this.mHomepageSearchFeature.getMQuery());
        intent2.putExtra(SettingsDcsRecordUtils.SEARCH_RES_CLICK, searchIntentItem.getTitle());
        intent2.setIdentifier(SettingsUtils.OPLUS_INTENT_CATEGORY_START_FROM_SETTINGS_MAIN_PAGE);
        if (!SettingsPackageUtils.isIntentSupport(intent2)) {
            this.mHomepageSearchFeature.setQueryText(title);
            return;
        }
        this.mSkipRec = true;
        CompatWindowUtil.startSearchTargetPage(this.mActivity, intent2);
        CompatWindowUtil.onSearchItemClick(searchIntentItem.getRootPath(this.mActivity), this.mActivity);
    }

    @Override // com.oplus.settings.feature.homepage.search.HomepageSearchFeature.Callback
    public void onSearchResultClicked(SettingsSearchResult settingsSearchResult) {
        LogUtils.d(TAG, "onSearchResultClicked -- result key: " + settingsSearchResult.dataKey);
        Intent intent = settingsSearchResult.payload.getIntent();
        intent.putExtra(SettingsActivity.EXTRA_OPLUS_FROM_MAIN_PAGE, true);
        intent.setIdentifier(SettingsUtils.OPLUS_INTENT_CATEGORY_START_FROM_SETTINGS_MAIN_PAGE);
        String stringExtra = IntentUtils.getStringExtra(intent, SettingsActivity.EXTRA_SHOW_FRAGMENT);
        String stringExtra2 = IntentUtils.getStringExtra(intent, ":settings:fragment_args_key");
        if (DisplaySettingsFragment.class.getName().equals(stringExtra) && "screen_refresh_rate".equals(stringExtra2)) {
            Toast.makeText(this.mActivity, R.string.refresh_rate_toast, 1).show();
            return;
        }
        intent.putExtra(SettingsUtils.EXTRA_SETTINGS_FROM_SEARCH, true);
        intent.putExtra(SettingsDcsRecordUtils.SETTIGNS_FROM, "1");
        intent.putExtra(SettingsDcsRecordUtils.SEARCH_QUERY, this.mHomepageSearchFeature.getMQuery());
        intent.putExtra(SettingsDcsRecordUtils.SEARCH_RES_CLICK, settingsSearchResult.title);
        intent.putExtra(SettingsUtils.EXTRA_SETTINGS_CLEAR_TOP, true);
        CompatWindowUtil.startSearchTargetPage(this.mActivity, intent);
        List<String> list = settingsSearchResult.breadcrumbs;
        if (list == null || list.isEmpty()) {
            return;
        }
        CompatWindowUtil.onSearchItemClick(settingsSearchResult.breadcrumbs.get(0), this.mActivity);
    }

    @Override // com.oplus.settings.feature.homepage.BaseHomePageImpl
    public void onStop() {
        this.mIsStopped = true;
        LogUtils.d(TAG, "onStop: mSearchDisabled: " + this.mSearchDisabled + ", mIsInSearchState: " + this.mIsInSearchState);
        if (this.mSearchDisabled || this.mIsInSearchState) {
            return;
        }
        detachSearch();
    }

    @Override // com.oplus.settings.feature.homepage.BaseHomePageImpl
    public void onWindowFocusChanged(boolean z2) {
        super.onWindowFocusChanged(z2);
        if (LogUtils.isDebugMode()) {
            LogUtils.d(TAG, "onWindowFocusChanged " + z2);
        }
        boolean z3 = this.mWindowHasFocus;
        this.mWindowHasFocus = z2;
        if (z3 || !z2) {
            return;
        }
        tryRefreshIndex();
        CompatWindowUtil.checkRestoreRightPage(CHECK_RESTORE_DELAY, "HomePage#HasFocus");
    }

    @Override // com.oplus.settings.feature.homepage.BaseHomePageImpl
    public void resetHeaderToExpand(RecyclerView recyclerView) {
        resetHeaderToExpand(recyclerView, false);
    }

    @Override // com.oplus.settings.feature.homepage.BaseHomePageImpl
    public boolean showContextualCards() {
        return false;
    }

    private void resetHeaderToExpand(RecyclerView recyclerView, boolean z2) {
        if (!z2 && shouldSkipResetHeaderForSearchEdit()) {
            LogUtils.d(TAG, "resetHeaderToExpand: skip in search edit state");
            return;
        }
        if (this.mAppBarLayout == null || recyclerView == null) {
            LogUtils.e("mAppBarLayout = " + this.mAppBarLayout + ", listView = " + recyclerView);
            return;
        }
        this.mAccumulatedRecyclerScroll = 0.0f;
        recyclerView.stopScroll();
        this.mAppBarLayout.setExpanded(true, true);
        recyclerView.scrollToPosition(0);
        SearchViewAnimate searchViewAnimate = this.mSearchViewAnimate;
        if (searchViewAnimate != null) {
            searchViewAnimate.setTranslationY(0.0f);
            this.mSearchViewAnimate.setSearchViewAnimateHeightPercent(1.0f);
        }
    }
}