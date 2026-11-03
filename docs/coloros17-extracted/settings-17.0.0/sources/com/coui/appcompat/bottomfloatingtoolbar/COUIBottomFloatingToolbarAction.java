package com.coui.appcompat.bottomfloatingtoolbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes7.dex */
public class COUIBottomFloatingToolbarAction {
    private final COUIBottomFloatingToolbarActionData mActionData;
    private final ActionAnimationListener mAnimationListener;
    private final boolean mDoAnimation;
    private boolean mIsRollbackData;
    private boolean mIsSetRollbackData;
    private final COUIBottomFloatingToolbarActionData mRollbackActionData;

    public interface ActionAnimationListener {
        void onActionAnimationEnd();

        void onActionAnimationStart();
    }

    public static class Builder {
        private ActionAnimationListener mAnimationListener;
        private boolean mDoAnimation;
        private int mRemoveGroupId;
        private List<COUIBottomFloatingToolbarItem> mInsertGroupList = new ArrayList();
        private Map<COUIBottomFloatingToolbarItem, Integer> mInsertItemsMap = new LinkedHashMap();
        private List<COUIBottomFloatingToolbarItem> mRemoveItemsList = new ArrayList();
        private List<COUIBottomFloatingToolbarItem> mContentReplaceItemsList = new ArrayList();

        public static /* synthetic */ int a(Map.Entry entry, Map.Entry entry2) {
            return ((Integer) entry.getValue()).compareTo((Integer) entry2.getValue());
        }

        public static /* synthetic */ List access$000(Builder builder) {
            return builder.mInsertGroupList;
        }

        public static /* synthetic */ int access$100(Builder builder) {
            return builder.mRemoveGroupId;
        }

        public static /* synthetic */ Map access$200(Builder builder) {
            return builder.mInsertItemsMap;
        }

        public static /* synthetic */ List access$300(Builder builder) {
            return builder.mRemoveItemsList;
        }

        public static /* synthetic */ List access$400(Builder builder) {
            return builder.mContentReplaceItemsList;
        }

        public static /* synthetic */ boolean access$500(Builder builder) {
            return builder.mDoAnimation;
        }

        public static /* synthetic */ ActionAnimationListener access$600(Builder builder) {
            return builder.mAnimationListener;
        }

        private void sortByValue(Map<COUIBottomFloatingToolbarItem, Integer> map) {
            if (map.isEmpty()) {
                return;
            }
            ArrayList<Map.Entry> arrayList = new ArrayList(map.entrySet());
            Collections.sort(arrayList, new c());
            map.clear();
            for (Map.Entry entry : arrayList) {
                map.put((COUIBottomFloatingToolbarItem) entry.getKey(), (Integer) entry.getValue());
            }
        }

        public Builder animate() {
            this.mDoAnimation = true;
            return this;
        }

        public COUIBottomFloatingToolbarAction build() {
            return new COUIBottomFloatingToolbarAction(this, null);
        }

        public Builder contentReplaceItems(List<COUIBottomFloatingToolbarItem> list) {
            this.mContentReplaceItemsList.addAll(list);
            return this;
        }

        public Builder insertGroup(int i3, List<COUIBottomFloatingToolbarItem> list) {
            for (COUIBottomFloatingToolbarItem cOUIBottomFloatingToolbarItem : list) {
                if (cOUIBottomFloatingToolbarItem.getGroupId() == i3) {
                    this.mInsertGroupList.add(cOUIBottomFloatingToolbarItem);
                }
            }
            return this;
        }

        public Builder insertItems(Map<COUIBottomFloatingToolbarItem, Integer> map) {
            this.mInsertItemsMap.putAll(map);
            sortByValue(this.mInsertItemsMap);
            return this;
        }

        public Builder removeGroup(int i3) {
            this.mRemoveGroupId = i3;
            return this;
        }

        public Builder removeItems(List<COUIBottomFloatingToolbarItem> list) {
            this.mRemoveItemsList.addAll(list);
            return this;
        }

        public Builder setActionAnimationListener(ActionAnimationListener actionAnimationListener) {
            this.mAnimationListener = actionAnimationListener;
            return this;
        }
    }

    public /* synthetic */ COUIBottomFloatingToolbarAction(Builder builder, AnonymousClass1 anonymousClass1) {
        this(builder);
    }

    public void changeActionDataStatus() {
        this.mIsRollbackData = !this.mIsRollbackData;
    }

    public COUIBottomFloatingToolbarActionData getActionData() {
        return this.mIsRollbackData ? this.mRollbackActionData : this.mActionData;
    }

    public ActionAnimationListener getAnimationListener() {
        return this.mAnimationListener;
    }

    public COUIBottomFloatingToolbarActionData getRollbackActionData() {
        return this.mRollbackActionData;
    }

    public boolean isDoAnimation() {
        return this.mDoAnimation;
    }

    public boolean isSetRollbackData() {
        return this.mIsSetRollbackData;
    }

    public void setSetRollbackData(boolean z2) {
        this.mIsSetRollbackData = z2;
    }

    private COUIBottomFloatingToolbarAction(Builder builder) {
        COUIBottomFloatingToolbarActionData cOUIBottomFloatingToolbarActionData = new COUIBottomFloatingToolbarActionData();
        this.mActionData = cOUIBottomFloatingToolbarActionData;
        this.mRollbackActionData = new COUIBottomFloatingToolbarActionData();
        cOUIBottomFloatingToolbarActionData.setInsertGroupList(Builder.access$000(builder));
        cOUIBottomFloatingToolbarActionData.setRemoveGroupId(Builder.access$100(builder));
        cOUIBottomFloatingToolbarActionData.setInsertItemsMap(Builder.access$200(builder));
        cOUIBottomFloatingToolbarActionData.setRemoveItemsList(Builder.access$300(builder));
        cOUIBottomFloatingToolbarActionData.setContentReplaceItemsList(Builder.access$400(builder));
        this.mDoAnimation = Builder.access$500(builder);
        this.mAnimationListener = Builder.access$600(builder);
    }
}