package com.coui.appcompat.bottomfloatingtoolbar;

import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes7.dex */
class COUIBottomFloatingToolbarActionData {
    private List<COUIBottomFloatingToolbarItem> mContentReplaceItemsList;
    private List<COUIBottomFloatingToolbarItem> mInsertGroupList;
    private Map<COUIBottomFloatingToolbarItem, Integer> mInsertItemsMap;
    private int mRemoveGroupId = -1;
    private List<COUIBottomFloatingToolbarItem> mRemoveItemsList;

    public List<COUIBottomFloatingToolbarItem> getContentReplaceItemsList() {
        return this.mContentReplaceItemsList;
    }

    public List<COUIBottomFloatingToolbarItem> getInsertGroupList() {
        return this.mInsertGroupList;
    }

    public Map<COUIBottomFloatingToolbarItem, Integer> getInsertItemsMap() {
        return this.mInsertItemsMap;
    }

    public int getRemoveGroupId() {
        return this.mRemoveGroupId;
    }

    public List<COUIBottomFloatingToolbarItem> getRemoveItemsList() {
        return this.mRemoveItemsList;
    }

    public void setContentReplaceItemsList(List<COUIBottomFloatingToolbarItem> list) {
        this.mContentReplaceItemsList = list;
    }

    public void setInsertGroupList(List<COUIBottomFloatingToolbarItem> list) {
        this.mInsertGroupList = list;
    }

    public void setInsertItemsMap(Map<COUIBottomFloatingToolbarItem, Integer> map) {
        this.mInsertItemsMap = map;
    }

    public void setRemoveGroupId(int i3) {
        this.mRemoveGroupId = i3;
    }

    public void setRemoveItemsList(List<COUIBottomFloatingToolbarItem> list) {
        this.mRemoveItemsList = list;
    }
}