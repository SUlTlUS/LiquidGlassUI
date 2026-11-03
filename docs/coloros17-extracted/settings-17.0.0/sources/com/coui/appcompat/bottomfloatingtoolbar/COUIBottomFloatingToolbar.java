package com.coui.appcompat.bottomfloatingtoolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.RenderEffect;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.core.view.ViewCompat;
import com.coui.appcompat.COUIMaterialEffect;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.poplist.PopupMenuConfigRule;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.coui.appcompat.uiutil.UIUtil;
import com.coui.appcompat.version.COUIVersionUtil;
import com.oplus.graphics.OplusPathAdapter;
import com.support.bottomnavigation.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes7.dex */
public class COUIBottomFloatingToolbar extends ViewGroup implements PopupMenuConfigRule {
    private static final int GROUP_COUNT_ONE = 1;
    private static final int GROUP_COUNT_THREE = 3;
    private static final int GROUP_COUNT_TWO = 2;
    private static final int MAX_GROUP = 3;
    public static final int PACKED = 1;
    private static final int SHADOW_CLIP_EXPAND_SIZE_PX = 120;
    public static final int SPREAD = 0;
    private static final String SYSTEM_MATERIAL_BLUR_ENABLE = "system_material_blur_enable";
    private static final String TAG = "COUIBottomFloatingToolbar";
    private final InternalActionAnimationCounter mAnimationCounter;
    private final Path mBackgroundPath;
    private SmoothRoundCornerHelper mBackgroundSmoothRoundCornerHelper;
    private RenderEffect mBlurEffect;
    private final ContentObserver mBlurSettingObserver;
    private boolean mCenterDisplay;
    private ItemClickCallback mClickCallBack;
    private List<COUIBottomFloatingToolbarItem> mContentReplaceItemsList;
    private Context mContext;
    private Consumer<Boolean> mCrossWindowBlurEnabledListener;
    private COUIBottomFloatingToolbarAction mCurAnimAction;
    private Rect mDisplayFrame;
    private Rect mDisplayFrameOutsets;
    private boolean mDoAnimation;
    private final Paint mDropShadowPaint;
    private final float mDropShadowWidth;
    private int mGroupCount;
    private int[] mGroupIdOrderArray;
    private boolean mHasRegistered;
    private List<COUIBottomFloatingToolbarItem> mInsertGroupList;
    private Map<COUIBottomFloatingToolbarItem, Integer> mInsertItemsMap;
    private boolean mIsDrawableMaterialEffect;
    private MaterialEffectState mMaterialEffectState;
    private final MenuBuilder mMenu;
    private Map<Integer, COUIBottomFloatingToolbarMenuView> mMenuGroupViewMap;
    private boolean mMenuViewBlur;
    private int mMenuViewPadding;
    private int mMinGroupSpace;
    private int mMinGroupWidth;
    private int mMinHeight;
    private int mOldWidth;
    private boolean mPopupRuleEnable;
    private float mRadius;
    private final RectF mRect;
    private int mRemoveGroupId;
    private List<COUIBottomFloatingToolbarItem> mRemoveItemsList;
    private boolean mRequestUpdateVisibleList;
    private final boolean mSdkCheck;
    private boolean mSettingBlurState;
    private final int mShadowColor;
    private final float mShadowDx;
    private final float mShadowDy;
    private final float mShadowRadius;
    private boolean mStrokeAndInnerShadowEnabled;
    private Rect mTmpMenuDisplayFrame;
    private boolean mToolbarBackgroundBlur;
    private ArrayList<COUIBottomFloatingToolbarMenuView> mTransitioningViews;
    private final ArrayList<COUIBottomFloatingToolbarMenuView> mVisibleMenuList;
    private boolean mWindowBlurEnable;
    private Rect mWindowFrame;
    private WindowManager mWindowManager;

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingtoolbar.COUIBottomFloatingToolbar$1, reason: invalid class name */
    public class AnonymousClass1 extends ContentObserver {
        public AnonymousClass1(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z2) {
            boolean zAccess$000 = COUIBottomFloatingToolbar.access$000(COUIBottomFloatingToolbar.this);
            if (COUIBottomFloatingToolbar.access$100(COUIBottomFloatingToolbar.this) != zAccess$000) {
                COUIBottomFloatingToolbar.access$102(COUIBottomFloatingToolbar.this, zAccess$000);
                COUIBottomFloatingToolbar.access$200(COUIBottomFloatingToolbar.this);
                COUIBottomFloatingToolbar.access$300(COUIBottomFloatingToolbar.this);
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FloatingToolbarAlignType {
    }

    public class InternalActionAnimationCounter {
        private boolean mPendingAnimationFinished;
        protected final AtomicInteger mPendingAnimationCounter = new AtomicInteger(0);
        private final AtomicInteger mAnimationCounter = new AtomicInteger(0);

        public InternalActionAnimationCounter() {
        }

        public static /* synthetic */ boolean access$400(InternalActionAnimationCounter internalActionAnimationCounter) {
            return internalActionAnimationCounter.isAnimationEnd();
        }

        public static /* synthetic */ boolean access$500(InternalActionAnimationCounter internalActionAnimationCounter) {
            return internalActionAnimationCounter.isPendingAnimEnd();
        }

        private boolean isAnimationEnd() {
            return this.mAnimationCounter.get() == 0;
        }

        private boolean isPendingAnimEnd() {
            return this.mPendingAnimationCounter.get() == 0;
        }

        private void onAllPendingAnimFinish() {
            COUIBottomFloatingToolbar.access$600(COUIBottomFloatingToolbar.this);
        }

        private void onPendingAnimationFinished() {
            COUIBottomFloatingToolbar.access$900(COUIBottomFloatingToolbar.this);
        }

        private void onRunningAnimationFinished() {
            COUIBottomFloatingToolbar.access$900(COUIBottomFloatingToolbar.this);
        }

        private void setAnimationEnd(boolean z2) {
            COUIBottomFloatingToolbarAction.ActionAnimationListener animationListener;
            if (z2) {
                onRunningAnimationFinished();
            }
            if (COUIBottomFloatingToolbar.access$800(COUIBottomFloatingToolbar.this) == null || (animationListener = COUIBottomFloatingToolbar.access$800(COUIBottomFloatingToolbar.this).getAnimationListener()) == null) {
                return;
            }
            animationListener.onActionAnimationEnd();
        }

        private void setAnimationStart() {
            COUIBottomFloatingToolbarAction.ActionAnimationListener animationListener;
            if (COUIBottomFloatingToolbar.access$800(COUIBottomFloatingToolbar.this) == null || (animationListener = COUIBottomFloatingToolbar.access$800(COUIBottomFloatingToolbar.this).getAnimationListener()) == null) {
                return;
            }
            animationListener.onActionAnimationStart();
        }

        public void abandonPendingSlot() {
            if (this.mPendingAnimationCounter.get() > 0) {
                COUILog.d(COUIBottomFloatingToolbar.TAG, "Pending Abandon result : " + this.mPendingAnimationCounter.decrementAndGet());
            }
        }

        public void setAnimDecrement(boolean z2, String str) {
            int iDecrementAndGet = this.mAnimationCounter.decrementAndGet();
            if (isAnimationEnd()) {
                setAnimationEnd(z2);
                this.mPendingAnimationFinished = false;
                Iterator it = COUIBottomFloatingToolbar.access$700(COUIBottomFloatingToolbar.this).iterator();
                while (it.hasNext()) {
                    COUIBottomFloatingToolbar.this.endMenuViewTransition((COUIBottomFloatingToolbarMenuView) it.next());
                }
            }
            COUILog.d(COUIBottomFloatingToolbar.TAG, "Anim Decrement result : " + iDecrementAndGet + ",msg : " + str);
        }

        public void setAnimIncrement(boolean z2, String str) {
            if (isAnimationEnd()) {
                setAnimationStart();
            }
            if (z2 && !this.mPendingAnimationFinished) {
                this.mPendingAnimationFinished = true;
                onPendingAnimationFinished();
            }
            COUILog.d(COUIBottomFloatingToolbar.TAG, "Anim Increment result : " + this.mAnimationCounter.incrementAndGet() + ",msg : " + str);
        }

        public void setPendingAnimDecrement() {
            int iDecrementAndGet = this.mPendingAnimationCounter.decrementAndGet();
            if (isPendingAnimEnd()) {
                onAllPendingAnimFinish();
            }
            COUILog.d(COUIBottomFloatingToolbar.TAG, "Pending Decrement result : " + iDecrementAndGet);
        }

        public void setPendingAnimIncrement() {
            COUILog.d(COUIBottomFloatingToolbar.TAG, "Pending Increment result : " + this.mPendingAnimationCounter.incrementAndGet());
        }
    }

    public interface ItemClickCallback {
        void onItemViewClick(COUIBottomFloatingToolbarItemView cOUIBottomFloatingToolbarItemView);
    }

    public enum MaterialEffectState {
        FORCE_ENABLE(1),
        FORCE_DISABLE(-1),
        FOLLOW_SYSTEM(0);

        private final int mValue;

        MaterialEffectState(int i3) {
            this.mValue = i3;
        }

        public int getValue() {
            return this.mValue;
        }
    }

    public static class SmoothRoundCornerHelper {
        private OplusPathAdapter mPathAdapter;
        private final int mSmoothStyleType;

        public SmoothRoundCornerHelper(Path path) {
            this.mPathAdapter = null;
            int smoothStyleType = RoundCornerUtil.getSmoothStyleType();
            this.mSmoothStyleType = smoothStyleType;
            if (smoothStyleType == 1) {
                this.mPathAdapter = new OplusPathAdapter(path, smoothStyleType);
            }
        }

        public OplusPathAdapter getPathAdapter() {
            return this.mPathAdapter;
        }

        public int getSmoothStyleType() {
            return this.mSmoothStyleType;
        }
    }

    public COUIBottomFloatingToolbar(Context context) {
        this(context, null);
    }

    public static /* synthetic */ int a(COUIBottomFloatingToolbarItem cOUIBottomFloatingToolbarItem, COUIBottomFloatingToolbarItem cOUIBottomFloatingToolbarItem2) {
        return cOUIBottomFloatingToolbarItem.getGroupId() - cOUIBottomFloatingToolbarItem2.getGroupId();
    }

    public static /* synthetic */ boolean access$000(COUIBottomFloatingToolbar cOUIBottomFloatingToolbar) {
        return cOUIBottomFloatingToolbar.isMaterialBlurEnabled();
    }

    public static /* synthetic */ boolean access$100(COUIBottomFloatingToolbar cOUIBottomFloatingToolbar) {
        return cOUIBottomFloatingToolbar.mSettingBlurState;
    }

    public static /* synthetic */ boolean access$102(COUIBottomFloatingToolbar cOUIBottomFloatingToolbar, boolean z2) {
        cOUIBottomFloatingToolbar.mSettingBlurState = z2;
        return z2;
    }

    public static /* synthetic */ void access$200(COUIBottomFloatingToolbar cOUIBottomFloatingToolbar) {
        cOUIBottomFloatingToolbar.applyToolbarBackgroundBlur();
    }

    public static /* synthetic */ void access$300(COUIBottomFloatingToolbar cOUIBottomFloatingToolbar) {
        cOUIBottomFloatingToolbar.applyMenuViewBlur();
    }

    public static /* synthetic */ void access$600(COUIBottomFloatingToolbar cOUIBottomFloatingToolbar) {
        cOUIBottomFloatingToolbar.executeMenuAnim();
    }

    public static /* synthetic */ ArrayList access$700(COUIBottomFloatingToolbar cOUIBottomFloatingToolbar) {
        return cOUIBottomFloatingToolbar.mTransitioningViews;
    }

    public static /* synthetic */ COUIBottomFloatingToolbarAction access$800(COUIBottomFloatingToolbar cOUIBottomFloatingToolbar) {
        return cOUIBottomFloatingToolbar.mCurAnimAction;
    }

    public static /* synthetic */ void access$900(COUIBottomFloatingToolbar cOUIBottomFloatingToolbar) {
        cOUIBottomFloatingToolbar.requestUpdateVisibleMenuList();
    }

    private void animNotify() {
        this.mAnimationCounter.setAnimIncrement(true, "no anim from animNotify");
        this.mAnimationCounter.setAnimDecrement(true, "no anim from animNotify");
    }

    private void applyMenuViewBlur() {
        if (BottomFloatingToolbarUtil.isOs17OrAbove()) {
            return;
        }
        if (this.mMenuViewBlur) {
            registerObserverIfNeeded();
        }
        Iterator<Map.Entry<Integer, COUIBottomFloatingToolbarMenuView>> it = this.mMenuGroupViewMap.entrySet().iterator();
        while (it.hasNext()) {
            it.next().getValue().setMenuViewMaterial(isMenuViewBlurEnable());
        }
    }

    private void applyToolbarBackgroundBlur() {
        if (BottomFloatingToolbarUtil.isOs17OrAbove()) {
            return;
        }
        if (this.mToolbarBackgroundBlur) {
            registerObserverIfNeeded();
        }
        COUIMaterialEffect.setBackgroundRenderEffect(isToolbarBlurEnable() ? this.mBlurEffect : null, this);
    }

    public static /* synthetic */ void b(COUIBottomFloatingToolbar cOUIBottomFloatingToolbar, Boolean bool) {
        if (cOUIBottomFloatingToolbar.mWindowBlurEnable != bool.booleanValue()) {
            cOUIBottomFloatingToolbar.mWindowBlurEnable = bool.booleanValue();
            cOUIBottomFloatingToolbar.applyToolbarBackgroundBlur();
            cOUIBottomFloatingToolbar.applyMenuViewBlur();
        }
    }

    private int calculateAnimationCount() {
        int i3 = !this.mContentReplaceItemsList.isEmpty() ? 1 : 0;
        if (!this.mInsertItemsMap.isEmpty()) {
            i3++;
        }
        if (!this.mRemoveItemsList.isEmpty()) {
            i3++;
        }
        if (this.mRemoveGroupId != -1) {
            i3++;
        }
        return !this.mInsertGroupList.isEmpty() ? i3 + 1 : i3;
    }

    private void changePadding() {
        int dimensionPixelOffset;
        if (COUIResponsiveUtils.isSmallScreen(getContext(), getMeasuredWidth())) {
            dimensionPixelOffset = getContext().getResources().getDimensionPixelOffset(R.dimen.coui_floating_toolbar_group_margin_small_screen);
        } else if (COUIResponsiveUtils.isMediumScreen(getContext(), getMeasuredWidth(), UIUtil.getScreenHeightMetrics(getContext()))) {
            dimensionPixelOffset = getContext().getResources().getDimensionPixelOffset(R.dimen.coui_floating_toolbar_group_margin_medium_screen);
        } else {
            dimensionPixelOffset = COUIResponsiveUtils.isLargeScreen(getContext(), getMeasuredWidth(), UIUtil.getScreenHeightMetrics(getContext())) ? getContext().getResources().getDimensionPixelOffset(R.dimen.coui_floating_toolbar_group_margin_large_screen) : 0;
        }
        if (dimensionPixelOffset == getPaddingLeft() && dimensionPixelOffset == getPaddingRight()) {
            return;
        }
        setPadding(dimensionPixelOffset, getPaddingTop(), dimensionPixelOffset, getPaddingBottom());
    }

    private void checkActionData(COUIBottomFloatingToolbarAction cOUIBottomFloatingToolbarAction) {
        checkInsertGroupData(cOUIBottomFloatingToolbarAction);
        checkRemoveGroupData(cOUIBottomFloatingToolbarAction);
        checkRemoveItemsData(cOUIBottomFloatingToolbarAction);
        checkInsertItemsData(cOUIBottomFloatingToolbarAction);
        checkContentReplaceItemsData(cOUIBottomFloatingToolbarAction);
        checkDoAnimation(cOUIBottomFloatingToolbarAction);
    }

    private void checkContentReplaceItemsData(COUIBottomFloatingToolbarAction cOUIBottomFloatingToolbarAction) {
        this.mContentReplaceItemsList.clear();
        Iterator<Map.Entry<Integer, COUIBottomFloatingToolbarMenuView>> it = this.mMenuGroupViewMap.entrySet().iterator();
        while (it.hasNext()) {
            it.next().getValue().clearContentReplaceItemsData();
        }
        ArrayList arrayList = new ArrayList();
        List<COUIBottomFloatingToolbarItem> contentReplaceItemsList = cOUIBottomFloatingToolbarAction.getActionData().getContentReplaceItemsList();
        if (contentReplaceItemsList != null) {
            for (COUIBottomFloatingToolbarItem cOUIBottomFloatingToolbarItem : contentReplaceItemsList) {
                int groupId = cOUIBottomFloatingToolbarItem.getGroupId();
                if (this.mMenuGroupViewMap.containsKey(Integer.valueOf(groupId))) {
                    COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView = this.mMenuGroupViewMap.get(Integer.valueOf(groupId));
                    if (cOUIBottomFloatingToolbarMenuView.checkContentReplaceItemsData(cOUIBottomFloatingToolbarItem)) {
                        this.mContentReplaceItemsList.add(cOUIBottomFloatingToolbarItem);
                        arrayList.add(cOUIBottomFloatingToolbarMenuView.getItemBenList().get(cOUIBottomFloatingToolbarMenuView.findIndexById(cOUIBottomFloatingToolbarItem.getId())));
                    }
                }
            }
        }
        if (cOUIBottomFloatingToolbarAction.isSetRollbackData() || arrayList.isEmpty()) {
            return;
        }
        cOUIBottomFloatingToolbarAction.getRollbackActionData().setContentReplaceItemsList(arrayList);
    }

    private void checkDoAnimation(COUIBottomFloatingToolbarAction cOUIBottomFloatingToolbarAction) {
        this.mDoAnimation = false;
        this.mCurAnimAction = null;
        cOUIBottomFloatingToolbarAction.setSetRollbackData(true);
        if (cOUIBottomFloatingToolbarAction.isDoAnimation()) {
            if (calculateAnimationCount() == 0) {
                animNotify();
            } else {
                this.mDoAnimation = true;
                this.mCurAnimAction = cOUIBottomFloatingToolbarAction;
            }
        }
    }

    private void checkInsertGroupData(COUIBottomFloatingToolbarAction cOUIBottomFloatingToolbarAction) {
        List<COUIBottomFloatingToolbarItem> insertGroupList;
        this.mInsertGroupList.clear();
        if (this.mGroupCount >= 3 || (insertGroupList = cOUIBottomFloatingToolbarAction.getActionData().getInsertGroupList()) == null || insertGroupList.isEmpty() || this.mMenuGroupViewMap.containsKey(Integer.valueOf(insertGroupList.get(0).getGroupId()))) {
            return;
        }
        this.mInsertGroupList.addAll(insertGroupList);
        if (cOUIBottomFloatingToolbarAction.isSetRollbackData()) {
            return;
        }
        cOUIBottomFloatingToolbarAction.getRollbackActionData().setRemoveGroupId(insertGroupList.get(0).getGroupId());
    }

    private void checkInsertItemsData(COUIBottomFloatingToolbarAction cOUIBottomFloatingToolbarAction) {
        this.mInsertItemsMap.clear();
        Iterator<Map.Entry<Integer, COUIBottomFloatingToolbarMenuView>> it = this.mMenuGroupViewMap.entrySet().iterator();
        while (it.hasNext()) {
            it.next().getValue().clearInsertItemsData();
        }
        ArrayList arrayList = new ArrayList();
        Map<COUIBottomFloatingToolbarItem, Integer> insertItemsMap = cOUIBottomFloatingToolbarAction.getActionData().getInsertItemsMap();
        if (insertItemsMap != null) {
            for (Map.Entry<COUIBottomFloatingToolbarItem, Integer> entry : insertItemsMap.entrySet()) {
                int groupId = entry.getKey().getGroupId();
                if (this.mMenuGroupViewMap.containsKey(Integer.valueOf(groupId)) && this.mMenuGroupViewMap.get(Integer.valueOf(groupId)).checkInsertItemsData(entry.getKey(), entry.getValue().intValue())) {
                    this.mInsertItemsMap.put(entry.getKey(), entry.getValue());
                    arrayList.add(entry.getKey());
                }
            }
        }
        if (cOUIBottomFloatingToolbarAction.isSetRollbackData() || arrayList.isEmpty()) {
            return;
        }
        cOUIBottomFloatingToolbarAction.getRollbackActionData().setRemoveItemsList(arrayList);
    }

    private boolean checkListElementsNotNull(List<?> list) {
        Iterator<?> it = list.iterator();
        while (it.hasNext()) {
            if (it.next() == null) {
                return false;
            }
        }
        return true;
    }

    private boolean checkListNotNull(List<?> list) {
        return (list == null || list.isEmpty()) ? false : true;
    }

    private void checkRemoveGroupData(COUIBottomFloatingToolbarAction cOUIBottomFloatingToolbarAction) {
        this.mRemoveGroupId = -1;
        if (this.mGroupCount > 1 && this.mMenuGroupViewMap.containsKey(Integer.valueOf(cOUIBottomFloatingToolbarAction.getActionData().getRemoveGroupId()))) {
            int removeGroupId = cOUIBottomFloatingToolbarAction.getActionData().getRemoveGroupId();
            this.mRemoveGroupId = removeGroupId;
            List<COUIBottomFloatingToolbarItem> itemBenList = this.mMenuGroupViewMap.get(Integer.valueOf(removeGroupId)).getItemBenList();
            if (cOUIBottomFloatingToolbarAction.isSetRollbackData()) {
                return;
            }
            cOUIBottomFloatingToolbarAction.getRollbackActionData().setInsertGroupList(itemBenList);
        }
    }

    private void checkRemoveItemsData(COUIBottomFloatingToolbarAction cOUIBottomFloatingToolbarAction) {
        this.mRemoveItemsList.clear();
        Iterator<Map.Entry<Integer, COUIBottomFloatingToolbarMenuView>> it = this.mMenuGroupViewMap.entrySet().iterator();
        while (it.hasNext()) {
            it.next().getValue().clearRemoveItemsData();
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        List<COUIBottomFloatingToolbarItem> removeItemsList = cOUIBottomFloatingToolbarAction.getActionData().getRemoveItemsList();
        if (removeItemsList != null) {
            for (COUIBottomFloatingToolbarItem cOUIBottomFloatingToolbarItem : removeItemsList) {
                int groupId = cOUIBottomFloatingToolbarItem.getGroupId();
                if (this.mMenuGroupViewMap.containsKey(Integer.valueOf(groupId)) && this.mMenuGroupViewMap.get(Integer.valueOf(groupId)).checkRemoveItemsData(cOUIBottomFloatingToolbarItem)) {
                    this.mRemoveItemsList.add(cOUIBottomFloatingToolbarItem);
                    linkedHashMap.put(cOUIBottomFloatingToolbarItem, Integer.valueOf(this.mMenuGroupViewMap.get(Integer.valueOf(groupId)).findIndexById(cOUIBottomFloatingToolbarItem.getId())));
                }
            }
        }
        if (cOUIBottomFloatingToolbarAction.isSetRollbackData() || linkedHashMap.isEmpty()) {
            return;
        }
        cOUIBottomFloatingToolbarAction.getRollbackActionData().setInsertItemsMap(linkedHashMap);
    }

    private COUIBottomFloatingToolbarMenuView createMenuView(List<COUIBottomFloatingToolbarItem> list) {
        COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView = new COUIBottomFloatingToolbarMenuView(this.mContext, isMenuViewBlurEnable());
        if (!BottomFloatingToolbarUtil.isOs17OrAbove()) {
            cOUIBottomFloatingToolbarMenuView.setStrokeAndInnerShadowEnabled(this.mStrokeAndInnerShadowEnabled);
        }
        cOUIBottomFloatingToolbarMenuView.setPadding(this.mMenuViewPadding, cOUIBottomFloatingToolbarMenuView.getPaddingTop(), this.mMenuViewPadding, cOUIBottomFloatingToolbarMenuView.getPaddingBottom());
        cOUIBottomFloatingToolbarMenuView.registerItemClick(this.mClickCallBack);
        cOUIBottomFloatingToolbarMenuView.setItemList(list);
        cOUIBottomFloatingToolbarMenuView.setScaleX(0.0f);
        cOUIBottomFloatingToolbarMenuView.setScaleY(0.0f);
        cOUIBottomFloatingToolbarMenuView.setIsDrawableMaterialEffect(this.mIsDrawableMaterialEffect);
        cOUIBottomFloatingToolbarMenuView.setUseMaterialEffectAttr(this.mMaterialEffectState);
        return cOUIBottomFloatingToolbarMenuView;
    }

    private void drawDropShadow(Canvas canvas) {
        canvas.save();
        canvas.clipPath(this.mBackgroundPath, Region.Op.DIFFERENCE);
        canvas.drawPath(this.mBackgroundPath, this.mDropShadowPaint);
        canvas.restore();
    }

    private void drawMenuViewsDropShadowIfNeeded(Canvas canvas) {
        if (BottomFloatingToolbarUtil.isOs17OrAbove()) {
            return;
        }
        Iterator<Map.Entry<Integer, COUIBottomFloatingToolbarMenuView>> it = this.mMenuGroupViewMap.entrySet().iterator();
        while (it.hasNext()) {
            updatePathAndDrawDropShadow(it.next().getValue(), canvas);
        }
        Iterator<COUIBottomFloatingToolbarMenuView> it2 = this.mTransitioningViews.iterator();
        while (it2.hasNext()) {
            updatePathAndDrawDropShadow(it2.next(), canvas);
        }
    }

    private void executeMenuAnim() {
        for (int i3 = 0; i3 < getChildCount(); i3++) {
            View childAt = getChildAt(i3);
            if (childAt instanceof COUIBottomFloatingToolbarMenuView) {
                COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView = (COUIBottomFloatingToolbarMenuView) childAt;
                runMenuHorizontalAnim(cOUIBottomFloatingToolbarMenuView);
                runMenuScaleAnim(cOUIBottomFloatingToolbarMenuView);
            }
        }
        for (int i4 = 0; i4 < this.mTransitioningViews.size(); i4++) {
            runMenuScaleAnim(this.mTransitioningViews.get(i4));
        }
    }

    private int[] getKeyArray(Map<Integer, COUIBottomFloatingToolbarMenuView> map) {
        int[] iArr = new int[map.size()];
        Iterator<Integer> it = map.keySet().iterator();
        int i3 = 0;
        while (it.hasNext()) {
            iArr[i3] = it.next().intValue();
            i3++;
        }
        return iArr;
    }

    private int getMeasuredWidthWithoutPadding(View view) {
        return (view.getMeasuredWidth() - view.getPaddingLeft()) - view.getPaddingRight();
    }

    private MenuInflater getMenuInflater() {
        return new MenuInflater(this.mContext);
    }

    private void handleAction() {
        handleAnimation();
        handleContentReplaceAction();
        handleInsertItemsAction();
        handleRemoveItemsAction();
        handleRemoveGroupAction();
        handleInsertGroupAction();
    }

    private void handleAnimation() {
        Iterator<Map.Entry<Integer, COUIBottomFloatingToolbarMenuView>> it = this.mMenuGroupViewMap.entrySet().iterator();
        while (it.hasNext()) {
            it.next().getValue().handleAnimation(this.mDoAnimation);
        }
    }

    private void handleContentReplaceAction() {
        Iterator<Map.Entry<Integer, COUIBottomFloatingToolbarMenuView>> it = this.mMenuGroupViewMap.entrySet().iterator();
        while (it.hasNext()) {
            it.next().getValue().handleContentReplaceAction();
        }
    }

    private Map<Integer, List<COUIBottomFloatingToolbarItem>> handleExtremeScene(List<COUIBottomFloatingToolbarItem> list) {
        Collections.sort(list, new a());
        TreeMap treeMap = new TreeMap();
        int i3 = 0;
        int groupId = list.get(0).getGroupId();
        ArrayList arrayList = new ArrayList();
        for (COUIBottomFloatingToolbarItem cOUIBottomFloatingToolbarItem : list) {
            int groupId2 = cOUIBottomFloatingToolbarItem.getGroupId();
            if (groupId2 != groupId) {
                treeMap.put(Integer.valueOf(groupId), arrayList);
                i3++;
                if (i3 >= 3) {
                    break;
                }
                arrayList = new ArrayList();
                groupId = groupId2;
            }
            arrayList.add(cOUIBottomFloatingToolbarItem);
        }
        if (i3 < 3 && !arrayList.isEmpty()) {
            treeMap.put(Integer.valueOf(groupId), arrayList);
        }
        return treeMap;
    }

    private void handleInsertGroupAction() {
        COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuViewCreateMenuView;
        if (this.mInsertGroupList.isEmpty()) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        int groupId = this.mInsertGroupList.get(0).getGroupId();
        arrayList.addAll(this.mInsertGroupList);
        ArrayList<COUIBottomFloatingToolbarMenuView> arrayList2 = this.mTransitioningViews;
        if (arrayList2 == null) {
            cOUIBottomFloatingToolbarMenuViewCreateMenuView = null;
            break;
        }
        Iterator<COUIBottomFloatingToolbarMenuView> it = arrayList2.iterator();
        do {
            if (!it.hasNext()) {
                cOUIBottomFloatingToolbarMenuViewCreateMenuView = null;
                break;
            }
            cOUIBottomFloatingToolbarMenuViewCreateMenuView = it.next();
        } while (groupId != cOUIBottomFloatingToolbarMenuViewCreateMenuView.mGroupId);
        if (cOUIBottomFloatingToolbarMenuViewCreateMenuView == null) {
            cOUIBottomFloatingToolbarMenuViewCreateMenuView = createMenuView(arrayList);
        }
        insertMenuGroup(cOUIBottomFloatingToolbarMenuViewCreateMenuView, this.mDoAnimation);
        addView(cOUIBottomFloatingToolbarMenuViewCreateMenuView);
        this.mMenuGroupViewMap.put(Integer.valueOf(groupId), cOUIBottomFloatingToolbarMenuViewCreateMenuView);
        this.mGroupIdOrderArray = getKeyArray(this.mMenuGroupViewMap);
        this.mGroupCount = this.mMenuGroupViewMap.size();
    }

    private void handleInsertItemsAction() {
        Iterator<Map.Entry<Integer, COUIBottomFloatingToolbarMenuView>> it = this.mMenuGroupViewMap.entrySet().iterator();
        while (it.hasNext()) {
            it.next().getValue().handleInsertItemsAction();
        }
    }

    private void handleRemoveGroupAction() {
        int i3 = this.mRemoveGroupId;
        if (i3 != -1) {
            COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView = this.mMenuGroupViewMap.get(Integer.valueOf(i3));
            removeMenuGroup(cOUIBottomFloatingToolbarMenuView, this.mDoAnimation);
            removeView(cOUIBottomFloatingToolbarMenuView);
            this.mMenuGroupViewMap.remove(Integer.valueOf(this.mRemoveGroupId));
            this.mGroupIdOrderArray = getKeyArray(this.mMenuGroupViewMap);
            this.mGroupCount = this.mMenuGroupViewMap.size();
        }
    }

    private void handleRemoveItemsAction() {
        Iterator<Map.Entry<Integer, COUIBottomFloatingToolbarMenuView>> it = this.mMenuGroupViewMap.entrySet().iterator();
        while (it.hasNext()) {
            it.next().getValue().handleRemoveItemsAction();
        }
    }

    private void initShadowLayerPaint() {
        if (BottomFloatingToolbarUtil.isOs17OrAbove()) {
            return;
        }
        this.mDropShadowPaint.setColor(0);
        this.mDropShadowPaint.setStyle(Paint.Style.STROKE);
        this.mDropShadowPaint.setStrokeWidth(20.0f);
        this.mDropShadowPaint.setShadowLayer(26.0f, 0.0f, 9.0f, this.mShadowColor);
        this.mBackgroundSmoothRoundCornerHelper = new SmoothRoundCornerHelper(this.mBackgroundPath);
    }

    private void insertMenuGroup(COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView, boolean z2) {
        ArrayList<COUIBottomFloatingToolbarMenuView> arrayList;
        if (z2 && (arrayList = this.mTransitioningViews) != null && arrayList.contains(cOUIBottomFloatingToolbarMenuView)) {
            endMenuViewTransition(cOUIBottomFloatingToolbarMenuView);
        }
    }

    private boolean isMaterialBlurEnabled() {
        return Settings.System.getInt(this.mContext.getContentResolver(), SYSTEM_MATERIAL_BLUR_ENABLE, 0) == 1;
    }

    private boolean isMenuViewBlurEnable() {
        return this.mMenuViewBlur && this.mSettingBlurState && this.mWindowBlurEnable;
    }

    private boolean isRtl() {
        return ViewCompat.getLayoutDirection(this) == 1;
    }

    private boolean isToolbarBlurEnable() {
        return this.mToolbarBackgroundBlur && this.mSettingBlurState && this.mWindowBlurEnable;
    }

    private void layoutMenuView(View view, int i3, int i4) {
        int paddingLeft = i3 - view.getPaddingLeft();
        int paddingTop = getPaddingTop() + Math.max(0, (((i4 - getPaddingTop()) - getPaddingBottom()) - view.getMeasuredHeight()) / 2);
        view.layout(paddingLeft, paddingTop, Math.max(view.getMeasuredWidth(), view.getWidth()) + paddingLeft, view.getMeasuredHeight() + paddingTop);
        notifyChildLayoutChanged(view, paddingLeft, paddingTop, paddingLeft + view.getMeasuredWidth(), paddingTop + view.getMeasuredHeight());
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void notifyChildLayoutChanged(View view, int i3, int i4, int i5, int i6) {
        if (view instanceof IBottomFloatingToolbarMenuViewAnimator) {
            ((IBottomFloatingToolbarMenuViewAnimator) view).updateRealBounds(i3, i4, i5, i6, this.mDoAnimation);
        }
    }

    private void onLayoutMenuViewOneMenu(int i3, int i4, int i5, int i6) {
        COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView = this.mMenuGroupViewMap.get(Integer.valueOf(this.mGroupIdOrderArray[0]));
        layoutMenuView(cOUIBottomFloatingToolbarMenuView, Math.max(getPaddingLeft(), getPaddingLeft() + ((getMeasuredWidthWithoutPadding(this) - getMeasuredWidthWithoutPadding(cOUIBottomFloatingToolbarMenuView)) / 2)), i6 - i4);
    }

    private void onLayoutMenuViewThreeMenu(int i3, int i4, int i5, int i6) {
        Map<Integer, COUIBottomFloatingToolbarMenuView> map;
        int i7;
        if (isRtl()) {
            map = this.mMenuGroupViewMap;
            i7 = this.mGroupIdOrderArray[2];
        } else {
            map = this.mMenuGroupViewMap;
            i7 = this.mGroupIdOrderArray[0];
        }
        COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView = map.get(Integer.valueOf(i7));
        COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView2 = this.mMenuGroupViewMap.get(Integer.valueOf(this.mGroupIdOrderArray[1]));
        COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView3 = isRtl() ? this.mMenuGroupViewMap.get(Integer.valueOf(this.mGroupIdOrderArray[0])) : this.mMenuGroupViewMap.get(Integer.valueOf(this.mGroupIdOrderArray[2]));
        if (this.mCenterDisplay) {
            int measuredWidthWithoutPadding = ((getMeasuredWidthWithoutPadding(this) - (((getMeasuredWidthWithoutPadding(cOUIBottomFloatingToolbarMenuView) + getMeasuredWidthWithoutPadding(cOUIBottomFloatingToolbarMenuView2)) + getMeasuredWidthWithoutPadding(cOUIBottomFloatingToolbarMenuView3)) + (this.mMinGroupSpace * 2))) / 2) + getPaddingLeft();
            int i8 = i6 - i4;
            layoutMenuView(cOUIBottomFloatingToolbarMenuView, measuredWidthWithoutPadding, i8);
            int measuredWidth = measuredWidthWithoutPadding + ((cOUIBottomFloatingToolbarMenuView.getMeasuredWidth() - cOUIBottomFloatingToolbarMenuView.getPaddingRight()) - cOUIBottomFloatingToolbarMenuView2.getPaddingLeft()) + this.mMinGroupSpace;
            layoutMenuView(cOUIBottomFloatingToolbarMenuView2, measuredWidth, i8);
            layoutMenuView(cOUIBottomFloatingToolbarMenuView3, measuredWidth + ((cOUIBottomFloatingToolbarMenuView2.getMeasuredWidth() - cOUIBottomFloatingToolbarMenuView2.getPaddingRight()) - cOUIBottomFloatingToolbarMenuView3.getPaddingLeft()) + this.mMinGroupSpace, i8);
            return;
        }
        int i9 = i6 - i4;
        layoutMenuView(cOUIBottomFloatingToolbarMenuView, getPaddingLeft(), i9);
        layoutMenuView(cOUIBottomFloatingToolbarMenuView3, ((i5 - i3) - getPaddingRight()) - getMeasuredWidthWithoutPadding(cOUIBottomFloatingToolbarMenuView3), i9);
        int right = (cOUIBottomFloatingToolbarMenuView.getRight() - cOUIBottomFloatingToolbarMenuView.getPaddingRight()) + this.mMinGroupSpace;
        int left = (cOUIBottomFloatingToolbarMenuView3.getLeft() + cOUIBottomFloatingToolbarMenuView3.getPaddingLeft()) - this.mMinGroupSpace;
        int measuredWidthWithoutPadding2 = getMeasuredWidthWithoutPadding(this);
        int measuredWidthWithoutPadding3 = ((measuredWidthWithoutPadding2 - getMeasuredWidthWithoutPadding(cOUIBottomFloatingToolbarMenuView2)) / 2) + getPaddingLeft();
        int measuredWidthWithoutPadding4 = ((measuredWidthWithoutPadding2 + getMeasuredWidthWithoutPadding(cOUIBottomFloatingToolbarMenuView2)) / 2) + getPaddingLeft();
        if (measuredWidthWithoutPadding3 >= right && measuredWidthWithoutPadding4 <= left) {
            right = measuredWidthWithoutPadding3;
        } else if (measuredWidthWithoutPadding3 >= right) {
            right = left - getMeasuredWidthWithoutPadding(cOUIBottomFloatingToolbarMenuView2);
        }
        layoutMenuView(cOUIBottomFloatingToolbarMenuView2, right, i9);
    }

    private void onLayoutMenuViewTwoMenu(int i3, int i4, int i5, int i6) {
        Map<Integer, COUIBottomFloatingToolbarMenuView> map;
        int i7;
        if (isRtl()) {
            map = this.mMenuGroupViewMap;
            i7 = this.mGroupIdOrderArray[1];
        } else {
            map = this.mMenuGroupViewMap;
            i7 = this.mGroupIdOrderArray[0];
        }
        COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView = map.get(Integer.valueOf(i7));
        COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView2 = isRtl() ? this.mMenuGroupViewMap.get(Integer.valueOf(this.mGroupIdOrderArray[0])) : this.mMenuGroupViewMap.get(Integer.valueOf(this.mGroupIdOrderArray[1]));
        if (!this.mCenterDisplay) {
            int i8 = i6 - i4;
            layoutMenuView(cOUIBottomFloatingToolbarMenuView, getPaddingLeft(), i8);
            layoutMenuView(cOUIBottomFloatingToolbarMenuView2, ((i5 - i3) - getPaddingRight()) - getMeasuredWidthWithoutPadding(cOUIBottomFloatingToolbarMenuView2), i8);
        } else {
            int iMax = Math.max(getPaddingLeft(), getPaddingLeft() + ((getMeasuredWidthWithoutPadding(this) - ((getMeasuredWidthWithoutPadding(cOUIBottomFloatingToolbarMenuView) + getMeasuredWidthWithoutPadding(cOUIBottomFloatingToolbarMenuView2)) + this.mMinGroupSpace)) / 2));
            int i9 = i6 - i4;
            layoutMenuView(cOUIBottomFloatingToolbarMenuView, iMax, i9);
            layoutMenuView(cOUIBottomFloatingToolbarMenuView2, iMax + ((cOUIBottomFloatingToolbarMenuView.getMeasuredWidth() - cOUIBottomFloatingToolbarMenuView.getPaddingRight()) - cOUIBottomFloatingToolbarMenuView2.getPaddingLeft()) + this.mMinGroupSpace, i9);
        }
    }

    private void onMeasureMenuViewOneMenu(View view, int i3, int i4) {
        view.measure(View.MeasureSpec.makeMeasureSpec(i3 + view.getPaddingLeft() + view.getPaddingRight(), Integer.MIN_VALUE), ViewGroup.getChildMeasureSpec(i4, getPaddingTop() + getPaddingBottom(), view.getLayoutParams().height));
    }

    private void onMeasureMenuViewThreeMenu(int i3, int i4) {
        COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView = this.mMenuGroupViewMap.get(Integer.valueOf(this.mGroupIdOrderArray[1]));
        onMeasureMenuViewOneMenu(cOUIBottomFloatingToolbarMenuView, i3 - ((this.mMinGroupSpace + this.mMinGroupWidth) * 2), i4);
        onMeasureMenuViewTwoMenu(this.mMenuGroupViewMap.get(Integer.valueOf(this.mGroupIdOrderArray[0])), this.mMenuGroupViewMap.get(Integer.valueOf(this.mGroupIdOrderArray[2])), (((i3 - cOUIBottomFloatingToolbarMenuView.getMeasuredWidth()) + cOUIBottomFloatingToolbarMenuView.getPaddingLeft()) + cOUIBottomFloatingToolbarMenuView.getPaddingRight()) - (this.mMinGroupSpace * 2), i4, false);
    }

    private void onMeasureMenuViewTwoMenu(View view, View view2, int i3, int i4, boolean z2) {
        onMeasureMenuViewOneMenu(view, i3, i4);
        onMeasureMenuViewOneMenu(view2, i3, i4);
        int i5 = i3 / 2;
        int iMin = Math.min((view.getMeasuredWidth() - view.getPaddingLeft()) - view.getPaddingRight(), (view2.getMeasuredWidth() - view2.getPaddingLeft()) - view2.getPaddingRight());
        if (iMin > i5) {
            onMeasureMenuViewOneMenu(view, i5, i4);
            onMeasureMenuViewOneMenu(view2, i5, i4);
            return;
        }
        if ((iMin == (view.getMeasuredWidth() - view.getPaddingLeft()) - view.getPaddingRight() ? view : view2) == view) {
            view = view2;
        }
        if ((view.getMeasuredWidth() - view.getPaddingLeft()) - view.getPaddingRight() > i5) {
            if (z2) {
                i5 = i3 - iMin;
            }
            onMeasureMenuViewOneMenu(view, i5, i4);
        }
    }

    private void registerObserverIfNeeded() {
        if (this.mHasRegistered) {
            return;
        }
        this.mSettingBlurState = isMaterialBlurEnabled();
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor(SYSTEM_MATERIAL_BLUR_ENABLE), false, this.mBlurSettingObserver);
        if (this.mCrossWindowBlurEnabledListener == null) {
            this.mCrossWindowBlurEnabledListener = new b(this);
        }
        if (this.mWindowManager == null) {
            this.mWindowManager = (WindowManager) this.mContext.getSystemService("window");
        }
        WindowManager windowManager = this.mWindowManager;
        if (windowManager != null) {
            windowManager.addCrossWindowBlurEnabledListener(this.mCrossWindowBlurEnabledListener);
            this.mWindowBlurEnable = this.mWindowManager.isCrossWindowBlurEnabled();
        }
        this.mHasRegistered = true;
    }

    private void removeMenuGroup(COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView, boolean z2) {
        if (z2) {
            startMenuViewTransition(cOUIBottomFloatingToolbarMenuView);
        }
    }

    private void requestUpdateVisibleMenuList() {
        this.mVisibleMenuList.clear();
        for (int i3 = 0; i3 < getChildCount(); i3++) {
            View childAt = getChildAt(i3);
            if (childAt instanceof COUIBottomFloatingToolbarMenuView) {
                this.mVisibleMenuList.add((COUIBottomFloatingToolbarMenuView) childAt);
            }
        }
    }

    private void requestUpdateVisibleMenuListInNextLayout() {
        this.mRequestUpdateVisibleList = true;
    }

    private void runMenuHorizontalAnim(COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView) {
        ItemViewAnimatorImpl itemViewAnimatorImpl = cOUIBottomFloatingToolbarMenuView.mItemViewAnimatorImpl;
        if (itemViewAnimatorImpl != null) {
            itemViewAnimatorImpl.executeLayoutAnimRunnable();
        }
    }

    private void runMenuScaleAnim(COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView) {
        ItemViewAnimatorImpl itemViewAnimatorImpl = cOUIBottomFloatingToolbarMenuView.mItemViewAnimatorImpl;
        if (itemViewAnimatorImpl != null) {
            itemViewAnimatorImpl.executeAttachAnimRunnable();
        }
    }

    private void setItemListInternal(List<COUIBottomFloatingToolbarItem> list) {
        COUILog.d(TAG, "setItemListInternal mainItemList: " + list);
        Map<Integer, List<COUIBottomFloatingToolbarItem>> mapHandleExtremeScene = handleExtremeScene(list);
        removeAllViews();
        this.mMenuGroupViewMap.clear();
        this.mDoAnimation = false;
        for (Map.Entry<Integer, List<COUIBottomFloatingToolbarItem>> entry : mapHandleExtremeScene.entrySet()) {
            COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuViewCreateMenuView = createMenuView(entry.getValue());
            addView(cOUIBottomFloatingToolbarMenuViewCreateMenuView);
            this.mMenuGroupViewMap.put(entry.getKey(), cOUIBottomFloatingToolbarMenuViewCreateMenuView);
        }
        this.mGroupIdOrderArray = getKeyArray(this.mMenuGroupViewMap);
        this.mGroupCount = this.mMenuGroupViewMap.size();
    }

    private void startMenuViewTransition(COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView) {
        if (this.mTransitioningViews == null) {
            this.mTransitioningViews = new ArrayList<>();
        }
        if (this.mTransitioningViews.contains(cOUIBottomFloatingToolbarMenuView)) {
            return;
        }
        this.mTransitioningViews.add(cOUIBottomFloatingToolbarMenuView);
        startViewTransition(cOUIBottomFloatingToolbarMenuView);
    }

    private void unRegisterObserverIfNeeded() {
        if (this.mHasRegistered) {
            this.mContext.getContentResolver().unregisterContentObserver(this.mBlurSettingObserver);
            this.mHasRegistered = false;
            WindowManager windowManager = this.mWindowManager;
            if (windowManager != null) {
                windowManager.removeCrossWindowBlurEnabledListener(this.mCrossWindowBlurEnabledListener);
            }
        }
    }

    private void updatePath() {
        this.mBackgroundPath.reset();
        if (this.mBackgroundSmoothRoundCornerHelper.getSmoothStyleType() == 1) {
            OplusPathAdapter pathAdapter = this.mBackgroundSmoothRoundCornerHelper.getPathAdapter();
            RectF rectF = this.mRect;
            float f3 = this.mRadius;
            pathAdapter.addSmoothRoundRect(rectF, f3, f3, Path.Direction.CCW);
            return;
        }
        Path path = this.mBackgroundPath;
        RectF rectF2 = this.mRect;
        float f4 = this.mRadius;
        path.addRoundRect(rectF2, f4, f4, Path.Direction.CCW);
    }

    private void updatePathAndDrawDropShadow(COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView, Canvas canvas) {
        float x2 = cOUIBottomFloatingToolbarMenuView.getX();
        float y2 = cOUIBottomFloatingToolbarMenuView.getY();
        float f3 = cOUIBottomFloatingToolbarMenuView.getBounds().right - cOUIBottomFloatingToolbarMenuView.getBounds().left;
        float f4 = cOUIBottomFloatingToolbarMenuView.getBounds().bottom - cOUIBottomFloatingToolbarMenuView.getBounds().top;
        this.mRect.left = cOUIBottomFloatingToolbarMenuView.getBounds().left + (((1.0f - cOUIBottomFloatingToolbarMenuView.getScaleX()) * f3) / 2.0f) + x2;
        this.mRect.right = (cOUIBottomFloatingToolbarMenuView.getBounds().right - ((f3 * (1.0f - cOUIBottomFloatingToolbarMenuView.getScaleX())) / 2.0f)) + x2;
        this.mRect.top = cOUIBottomFloatingToolbarMenuView.getBounds().top + (((1.0f - cOUIBottomFloatingToolbarMenuView.getScaleY()) * f4) / 2.0f) + y2;
        this.mRect.bottom = (cOUIBottomFloatingToolbarMenuView.getBounds().bottom - ((f4 * (1.0f - cOUIBottomFloatingToolbarMenuView.getScaleY())) / 2.0f)) + y2;
        RectF rectF = this.mRect;
        this.mRadius = (rectF.bottom - rectF.top) / 2.0f;
        this.mDropShadowPaint.setStrokeWidth(cOUIBottomFloatingToolbarMenuView.getScaleX() * 20.0f);
        updatePath();
        drawDropShadow(canvas);
    }

    public void animationRollback() {
        COUIBottomFloatingToolbarAction cOUIBottomFloatingToolbarAction = this.mCurAnimAction;
        if (cOUIBottomFloatingToolbarAction != null) {
            cOUIBottomFloatingToolbarAction.changeActionDataStatus();
            executeAction(this.mCurAnimAction);
            requestUpdateVisibleMenuListInNextLayout();
        }
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawMenuViewsDropShadowIfNeeded(canvas);
    }

    public void endMenuViewTransition(COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView) {
        if (this.mTransitioningViews == null) {
            this.mTransitioningViews = new ArrayList<>();
        }
        this.mTransitioningViews.remove(cOUIBottomFloatingToolbarMenuView);
        endViewTransition(cOUIBottomFloatingToolbarMenuView);
    }

    public void executeAction(COUIBottomFloatingToolbarAction cOUIBottomFloatingToolbarAction) {
        if (InternalActionAnimationCounter.access$400(this.mAnimationCounter) || this.mCurAnimAction == cOUIBottomFloatingToolbarAction) {
            checkActionData(cOUIBottomFloatingToolbarAction);
            handleAction();
        }
    }

    public ArrayList<View> getAllItemViews() {
        ArrayList<View> arrayList = new ArrayList<>();
        for (int i3 : this.mGroupIdOrderArray) {
            arrayList.addAll(this.mMenuGroupViewMap.get(Integer.valueOf(i3)).getItemViewList());
        }
        return arrayList;
    }

    public final InternalActionAnimationCounter getAnimationCounter() {
        return this.mAnimationCounter;
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public int getBarrierDirection() {
        if (getDisplayFrame().isEmpty()) {
            return -1;
        }
        if (this.mWindowFrame == null) {
            this.mWindowFrame = new Rect();
        }
        getRootView().getGlobalVisibleRect(this.mWindowFrame);
        return this.mWindowFrame.height() <= getContext().getResources().getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_min_window_height_to_apply_vertical_barrier) ? -1 : 3;
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public Rect getDisplayFrame() {
        if (this.mDisplayFrame == null) {
            this.mDisplayFrame = new Rect();
        }
        if (this.mTmpMenuDisplayFrame == null) {
            this.mTmpMenuDisplayFrame = new Rect();
        }
        Iterator<Map.Entry<Integer, COUIBottomFloatingToolbarMenuView>> it = this.mMenuGroupViewMap.entrySet().iterator();
        boolean z2 = false;
        while (it.hasNext()) {
            COUIBottomFloatingToolbarMenuView value = it.next().getValue();
            if (value.getVisibility() == 0 && value.getGlobalVisibleRect(this.mTmpMenuDisplayFrame) && !this.mTmpMenuDisplayFrame.isEmpty()) {
                if (z2) {
                    this.mDisplayFrame.union(this.mTmpMenuDisplayFrame);
                } else {
                    this.mDisplayFrame.set(this.mTmpMenuDisplayFrame);
                    z2 = true;
                }
            }
        }
        if (!z2) {
            getGlobalVisibleRect(this.mDisplayFrame);
        }
        return this.mDisplayFrame;
    }

    public int getGroupAlignStyle() {
        return this.mCenterDisplay ? 1 : 0;
    }

    public List<COUIBottomFloatingToolbarItem> getInVisibleItemsByGroupId(int i3) {
        for (int i4 : this.mGroupIdOrderArray) {
            if (i4 == i3) {
                COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView = this.mMenuGroupViewMap.get(Integer.valueOf(i3));
                List<COUIBottomFloatingToolbarItem> itemBenList = cOUIBottomFloatingToolbarMenuView.getItemBenList();
                return itemBenList.subList(cOUIBottomFloatingToolbarMenuView.getVisibleViewsSize(), itemBenList.size());
            }
        }
        return new ArrayList();
    }

    public COUIBottomFloatingToolbarItemView getItemMoreView(int i3) {
        for (int i4 : this.mGroupIdOrderArray) {
            if (i4 == i3) {
                return (COUIBottomFloatingToolbarItemView) this.mMenuGroupViewMap.get(Integer.valueOf(i3)).findViewById(R.id.floating_toolbar_group_more);
            }
        }
        return null;
    }

    public List<COUIBottomFloatingToolbarItem> getItemsByGroupId(int i3) {
        for (int i4 : this.mGroupIdOrderArray) {
            if (i4 == i3) {
                return this.mMenuGroupViewMap.get(Integer.valueOf(i3)).getItemBenList();
            }
        }
        return new ArrayList();
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public Rect getOutsets() {
        if (this.mDisplayFrameOutsets == null) {
            this.mDisplayFrameOutsets = new Rect(0, getContext().getResources().getDimensionPixelOffset(R.dimen.coui_popup_list_window_gap_to_navigation_view), 0, 0);
        }
        return this.mDisplayFrameOutsets;
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public boolean getPopupMenuRuleEnabled() {
        return this.mPopupRuleEnable;
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public int getType() {
        return 2;
    }

    public final void inflateMenu(int i3) {
        this.mMenu.clear();
        getMenuInflater().inflate(i3, this.mMenu);
        if (this.mMenu.getNonActionItems().isEmpty()) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        COUIBottomFloatingToolbarItem.Builder builder = new COUIBottomFloatingToolbarItem.Builder();
        for (MenuItemImpl menuItemImpl : this.mMenu.getNonActionItems()) {
            builder.reset().setId(menuItemImpl.getItemId()).setDescription(menuItemImpl.getContentDescription()).setGroupId(menuItemImpl.getGroupId()).setTitle(menuItemImpl.getTitle() == null ? "" : menuItemImpl.getTitle().toString()).setIcon(menuItemImpl.getIcon()).setEnabled(menuItemImpl.isEnabled());
            arrayList.add(builder.build());
        }
        setItemList(arrayList);
    }

    @Override // android.view.ViewGroup
    public void measureChild(View view, int i3, int i4) {
    }

    @Override // android.view.ViewGroup
    public void measureChildWithMargins(View view, int i3, int i4, int i5, int i6) {
    }

    @Override // android.view.ViewGroup
    public void measureChildren(int i3, int i4) {
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Iterator<Map.Entry<Integer, COUIBottomFloatingToolbarMenuView>> it = this.mMenuGroupViewMap.entrySet().iterator();
        while (it.hasNext()) {
            COUIBottomFloatingToolbarMenuView value = it.next().getValue();
            MenuViewAnimatorImpl menuViewAnimatorImpl = value.mMenuViewAnimatorImpl;
            if (menuViewAnimatorImpl != null) {
                menuViewAnimatorImpl.cancelHorizontalAnim();
                value.mMenuViewAnimatorImpl.cancelScaleAnim();
            }
            ItemViewAnimatorImpl itemViewAnimatorImpl = value.mItemViewAnimatorImpl;
            if (itemViewAnimatorImpl != null) {
                itemViewAnimatorImpl.cancelTransitionAnim();
                value.mItemViewAnimatorImpl.cancelGapScaleAnim();
            }
        }
        unRegisterObserverIfNeeded();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        Iterator<Map.Entry<Integer, COUIBottomFloatingToolbarMenuView>> it = this.mMenuGroupViewMap.entrySet().iterator();
        while (true) {
            boolean z3 = false;
            if (!it.hasNext()) {
                break;
            }
            COUIBottomFloatingToolbarMenuView value = it.next().getValue();
            if (this.mOldWidth == getMeasuredWidth()) {
                z3 = true;
            }
            value.setScreenSwitch(z3);
        }
        this.mOldWidth = getMeasuredWidth();
        int i7 = this.mGroupCount;
        if (i7 != 0) {
            if (i7 == 1) {
                onLayoutMenuViewOneMenu(i3, i4, i5, i6);
            } else if (i7 == 2) {
                onLayoutMenuViewTwoMenu(i3, i4, i5, i6);
            } else {
                if (i7 != 3) {
                    throw new RuntimeException();
                }
                onLayoutMenuViewThreeMenu(i3, i4, i5, i6);
            }
        }
        if (!this.mDoAnimation || this.mRequestUpdateVisibleList) {
            this.mRequestUpdateVisibleList = false;
            requestUpdateVisibleMenuList();
        }
        if (InternalActionAnimationCounter.access$500(this.mAnimationCounter)) {
            executeMenuAnim();
        }
    }

    @Override // android.view.View
    public void onMeasure(int i3, int i4) {
        int size = View.MeasureSpec.getSize(i3);
        int size2 = View.MeasureSpec.getSize(i4);
        if (1073741824 != View.MeasureSpec.getMode(i4)) {
            size2 = this.mMinHeight + getPaddingTop() + getPaddingBottom();
        } else {
            int i5 = this.mMinHeight;
            if (size2 < i5) {
                size2 = i5;
            }
        }
        setMeasuredDimension(size, size2);
        changePadding();
        int iMax = Math.max(0, (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight());
        int i6 = this.mGroupCount;
        if (i6 != 0) {
            if (i6 == 1) {
                onMeasureMenuViewOneMenu(this.mMenuGroupViewMap.get(Integer.valueOf(this.mGroupIdOrderArray[0])), iMax, i4);
            } else if (i6 == 2) {
                onMeasureMenuViewTwoMenu(this.mMenuGroupViewMap.get(Integer.valueOf(this.mGroupIdOrderArray[0])), this.mMenuGroupViewMap.get(Integer.valueOf(this.mGroupIdOrderArray[1])), iMax - this.mMinGroupSpace, i4, true);
            } else {
                if (i6 != 3) {
                    throw new RuntimeException();
                }
                onMeasureMenuViewThreeMenu(iMax, i4);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.view.ViewGroup
    public void onViewAdded(View view) {
        super.onViewAdded(view);
        if (view instanceof IBottomFloatingToolbarMenuViewAnimator) {
            ((IBottomFloatingToolbarMenuViewAnimator) view).updateAttachState(true, this.mDoAnimation);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.view.ViewGroup
    public void onViewRemoved(View view) {
        super.onViewRemoved(view);
        if (view instanceof IBottomFloatingToolbarMenuViewAnimator) {
            IBottomFloatingToolbarMenuViewAnimator iBottomFloatingToolbarMenuViewAnimator = (IBottomFloatingToolbarMenuViewAnimator) view;
            iBottomFloatingToolbarMenuViewAnimator.unbindController();
            iBottomFloatingToolbarMenuViewAnimator.updateAttachState(false, this.mDoAnimation);
        }
    }

    public void registerItemClick(ItemClickCallback itemClickCallback) {
        this.mClickCallBack = itemClickCallback;
        Iterator<Map.Entry<Integer, COUIBottomFloatingToolbarMenuView>> it = this.mMenuGroupViewMap.entrySet().iterator();
        while (it.hasNext()) {
            it.next().getValue().registerItemClick(itemClickCallback);
        }
    }

    public void setBackgroundBlurEffect(RenderEffect renderEffect) {
        this.mBlurEffect = renderEffect;
        applyToolbarBackgroundBlur();
    }

    public void setGroupAlignStyle(int i3) {
        this.mCenterDisplay = i3 == 1;
    }

    public void setIsDrawableMaterialEffect(boolean z2) {
        if (this.mIsDrawableMaterialEffect == z2) {
            return;
        }
        this.mIsDrawableMaterialEffect = z2;
        Iterator<Map.Entry<Integer, COUIBottomFloatingToolbarMenuView>> it = this.mMenuGroupViewMap.entrySet().iterator();
        while (it.hasNext()) {
            it.next().getValue().setIsDrawableMaterialEffect(z2);
        }
    }

    public void setItemList(List<COUIBottomFloatingToolbarItem> list) {
        if (checkListNotNull(list) && checkListElementsNotNull(list)) {
            setItemListInternal(new ArrayList(list));
        } else {
            COUILog.e(TAG, "Error! Item list must not be empty or null!");
        }
    }

    public final void setMenuViewBlur(boolean z2) {
        if (BottomFloatingToolbarUtil.isOs17OrAbove() || z2 == this.mMenuViewBlur) {
            return;
        }
        boolean z3 = this.mContext.getResources().getBoolean(com.coui.appcompat.effect.R.bool.coui_blur_enable);
        if (UIUtil.confirmLevelAnim(UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN) && z3) {
            this.mMenuViewBlur = z2;
        } else {
            COUILog.e(TAG, "do not support setting blurred backgrounds or current animLevel is too low or is in third party theme");
            this.mMenuViewBlur = false;
        }
        applyMenuViewBlur();
    }

    @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
    public void setPopupMenuRuleEnabled(boolean z2) {
        this.mPopupRuleEnable = z2;
    }

    @Deprecated
    public void setStrokeAndInnerShadowEnabled(boolean z2) {
    }

    public final void setToolbarBackgroundBlur(boolean z2) {
        if (BottomFloatingToolbarUtil.isOs17OrAbove() || z2 == this.mToolbarBackgroundBlur) {
            return;
        }
        boolean z3 = this.mContext.getResources().getBoolean(com.coui.appcompat.effect.R.bool.coui_blur_enable);
        if (this.mSdkCheck && UIUtil.confirmLevelAnim(UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN) && z3) {
            this.mToolbarBackgroundBlur = z2;
        } else {
            COUILog.e(TAG, "do not support setting blurred backgrounds or current animLevel is too low or is in third party theme");
            this.mToolbarBackgroundBlur = false;
        }
        applyToolbarBackgroundBlur();
    }

    public void setUseMaterialEffectAttr(MaterialEffectState materialEffectState) {
        if (this.mMaterialEffectState == materialEffectState) {
            return;
        }
        this.mMaterialEffectState = materialEffectState;
        Iterator<Map.Entry<Integer, COUIBottomFloatingToolbarMenuView>> it = this.mMenuGroupViewMap.entrySet().iterator();
        while (it.hasNext()) {
            it.next().getValue().setUseMaterialEffectAttr(materialEffectState);
        }
    }

    public boolean shouldMenuViewAlignCenter(COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView) {
        if (this.mCenterDisplay) {
            return false;
        }
        return (this.mVisibleMenuList.size() == 1 && this.mVisibleMenuList.get(0) == cOUIBottomFloatingToolbarMenuView) || (this.mVisibleMenuList.size() == 3 && this.mVisibleMenuList.get(1) == cOUIBottomFloatingToolbarMenuView);
    }

    public boolean shouldMenuViewAlignLeft(COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView) {
        if (this.mCenterDisplay) {
            return false;
        }
        boolean z2 = this.mVisibleMenuList.size() > 1 && this.mVisibleMenuList.get(0) == cOUIBottomFloatingToolbarMenuView;
        if (!isRtl()) {
            return z2;
        }
        if (this.mVisibleMenuList.size() > 1) {
            ArrayList<COUIBottomFloatingToolbarMenuView> arrayList = this.mVisibleMenuList;
            if (arrayList.get(arrayList.size() - 1) == cOUIBottomFloatingToolbarMenuView) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Code duplicated, block: B:10:0x001e  */
    public boolean shouldMenuViewAlignRight(COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView) {
        boolean z2;
        if (this.mCenterDisplay) {
            return false;
        }
        if (this.mVisibleMenuList.size() > 1) {
            ArrayList<COUIBottomFloatingToolbarMenuView> arrayList = this.mVisibleMenuList;
            if (arrayList.get(arrayList.size() - 1) == cOUIBottomFloatingToolbarMenuView) {
                z2 = true;
            } else {
                z2 = false;
            }
        } else {
            z2 = false;
        }
        if (isRtl()) {
            return this.mVisibleMenuList.size() > 1 && this.mVisibleMenuList.get(0) == cOUIBottomFloatingToolbarMenuView;
        }
        return z2;
    }

    public void unRegisterItemClick() {
        this.mClickCallBack = null;
        Iterator<Map.Entry<Integer, COUIBottomFloatingToolbarMenuView>> it = this.mMenuGroupViewMap.entrySet().iterator();
        while (it.hasNext()) {
            it.next().getValue().unRegisterItemClick();
        }
    }

    public COUIBottomFloatingToolbar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiFloatingToolbarStyle);
    }

    public COUIBottomFloatingToolbar(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0);
    }

    public COUIBottomFloatingToolbar(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mVisibleMenuList = new ArrayList<>();
        this.mAnimationCounter = new InternalActionAnimationCounter();
        this.mShadowRadius = 26.0f;
        this.mShadowDx = 0.0f;
        this.mShadowDy = 9.0f;
        this.mShadowColor = Color.argb(27, 0, 0, 0);
        this.mDropShadowPaint = new Paint();
        this.mDropShadowWidth = 20.0f;
        this.mBackgroundPath = new Path();
        this.mRect = new RectF();
        this.mBlurSettingObserver = new AnonymousClass1(new Handler(Looper.getMainLooper()));
        this.mStrokeAndInnerShadowEnabled = true;
        this.mIsDrawableMaterialEffect = false;
        this.mMaterialEffectState = MaterialEffectState.FOLLOW_SYSTEM;
        this.mSettingBlurState = false;
        this.mWindowBlurEnable = false;
        this.mMenuGroupViewMap = new TreeMap();
        this.mInsertGroupList = new ArrayList();
        this.mRemoveGroupId = -1;
        this.mInsertItemsMap = new LinkedHashMap();
        this.mRemoveItemsList = new ArrayList();
        this.mContentReplaceItemsList = new ArrayList();
        this.mTransitioningViews = new ArrayList<>();
        this.mOldWidth = 0;
        this.mRadius = 0.0f;
        this.mMenuViewPadding = 0;
        this.mRequestUpdateVisibleList = false;
        this.mPopupRuleEnable = true;
        this.mDisplayFrame = null;
        this.mWindowFrame = null;
        this.mDisplayFrameOutsets = null;
        this.mTmpMenuDisplayFrame = null;
        this.mSdkCheck = COUIVersionUtil.checkOPlusViewBackgroundRenderEffectSupport();
        this.mContext = context;
        this.mMenu = new MenuBuilder(this.mContext);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUIFloatingToolbar, i3, i4);
        setToolbarBackgroundBlur(typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIFloatingToolbar_couiFloatingToolbarBackgroundBlur, false));
        setMenuViewBlur(typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIFloatingToolbar_couiFloatingToolbarMenuViewMaterial, false));
        this.mMinGroupSpace = getResources().getDimensionPixelSize(R.dimen.coui_floating_toolbar_min_group_space);
        this.mMinHeight = getResources().getDimensionPixelSize(R.dimen.coui_floating_toolbar_min_height);
        this.mMinGroupWidth = getResources().getDimensionPixelSize(R.dimen.coui_floating_toolbar_item_width);
        this.mMenuViewPadding = getResources().getDimensionPixelSize(R.dimen.coui_floating_toolbar_menu_view_padding);
        int i5 = R.styleable.COUIFloatingToolbar_couiFloatingToolbarMenu;
        if (typedArrayObtainStyledAttributes.hasValue(i5)) {
            inflateMenu(typedArrayObtainStyledAttributes.getResourceId(i5, 0));
        }
        typedArrayObtainStyledAttributes.recycle();
        initShadowLayerPaint();
        setClipChildren(false);
        setClipToPadding(false);
        setWillNotDraw(false);
        setClipToOutline(false);
        UIUtil.setViewClipExpandSize(this, 0, 120, 0, 0);
    }
}