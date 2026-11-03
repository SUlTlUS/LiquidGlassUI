package com.coui.appcompat.searchview;

import android.content.Context;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.searchview.params.IconCollapseToNormalParams;
import com.coui.appcompat.searchview.params.IconCollapseToNormalParamsHigh;
import com.coui.appcompat.searchview.params.IconCollapseToNormalParamsLow;
import com.coui.appcompat.searchview.params.IconExpandToEditParams;
import com.coui.appcompat.searchview.params.IconExpandToEditParamsHigh;
import com.coui.appcompat.searchview.params.IconExpandToEditParamsLow;
import com.coui.appcompat.searchview.params.StateChangedToEditParams;
import com.coui.appcompat.searchview.params.StateChangedToEditParamsHigh;
import com.coui.appcompat.searchview.params.StateChangedToEditParamsLow;
import com.coui.appcompat.searchview.params.StateChangedToNormalParams;
import com.coui.appcompat.searchview.params.StateChangedToNormalParamsHigh;
import com.coui.appcompat.searchview.params.StateChangedToNormalParamsLow;

/* JADX INFO: loaded from: classes.dex */
public interface SearchBarParamProvider {

    public static class Default implements SearchBarParamProvider {
        Context mContext;
        IconCollapseToNormalParams mIconCollapseToNormalParams;
        IconExpandToEditParams mIconExpandToEditParams;
        StateChangedToEditParams mStateChangedToEditParams;
        StateChangedToNormalParams mStateChangedToNormalParams;

        public Default(Context context) {
            this.mContext = context;
            initParams();
        }

        private void initParams() {
            if (COUIMaterialStateManager.INSTANCE.isLowAnimationLevel()) {
                this.mIconCollapseToNormalParams = new IconCollapseToNormalParamsLow();
                this.mIconExpandToEditParams = new IconExpandToEditParamsLow();
                this.mStateChangedToEditParams = new StateChangedToEditParamsLow();
                this.mStateChangedToNormalParams = new StateChangedToNormalParamsLow();
                return;
            }
            this.mIconCollapseToNormalParams = new IconCollapseToNormalParamsHigh();
            this.mIconExpandToEditParams = new IconExpandToEditParamsHigh();
            this.mStateChangedToEditParams = new StateChangedToEditParamsHigh();
            this.mStateChangedToNormalParams = new StateChangedToNormalParamsHigh();
        }

        @Override // com.coui.appcompat.searchview.SearchBarParamProvider
        public IconCollapseToNormalParams getIconCollapseToNormalParams() {
            return this.mIconCollapseToNormalParams;
        }

        @Override // com.coui.appcompat.searchview.SearchBarParamProvider
        public IconExpandToEditParams getIconExpandToEditParams() {
            return this.mIconExpandToEditParams;
        }

        @Override // com.coui.appcompat.searchview.SearchBarParamProvider
        public StateChangedToEditParams getStateChangedToEditParams() {
            return this.mStateChangedToEditParams;
        }

        @Override // com.coui.appcompat.searchview.SearchBarParamProvider
        public StateChangedToNormalParams getStateChangedToNormalParams() {
            return this.mStateChangedToNormalParams;
        }
    }

    /* JADX INFO: loaded from: classes7.dex */
    public static class OlivelinkDefault implements SearchBarParamProvider {
        Context mContext;
        IconCollapseToNormalParams mIconCollapseToNormalParams;
        IconExpandToEditParams mIconExpandToEditParams;
        StateChangedToEditParams mStateChangedToEditParams;
        StateChangedToNormalParams mStateChangedToNormalParams;

        public OlivelinkDefault(Context context) {
            this.mContext = context;
            initParams();
        }

        private void initParams() {
            if (COUIMaterialStateManager.INSTANCE.isLowAnimationLevel()) {
                this.mIconCollapseToNormalParams = new com.coui.appcompat.searchview.params.olivelink.IconCollapseToNormalParamsLow();
                this.mIconExpandToEditParams = new com.coui.appcompat.searchview.params.olivelink.IconExpandToEditParamsLow();
                this.mStateChangedToEditParams = new com.coui.appcompat.searchview.params.olivelink.StateChangedToEditParamsLow();
                this.mStateChangedToNormalParams = new com.coui.appcompat.searchview.params.olivelink.StateChangedToNormalParamsLow();
                return;
            }
            this.mIconCollapseToNormalParams = new com.coui.appcompat.searchview.params.olivelink.IconCollapseToNormalParamsHigh();
            this.mIconExpandToEditParams = new com.coui.appcompat.searchview.params.olivelink.IconExpandToEditParamsHigh();
            this.mStateChangedToEditParams = new com.coui.appcompat.searchview.params.olivelink.StateChangedToEditParamsHigh();
            this.mStateChangedToNormalParams = new com.coui.appcompat.searchview.params.olivelink.StateChangedToNormalParamsHigh();
        }

        @Override // com.coui.appcompat.searchview.SearchBarParamProvider
        public IconCollapseToNormalParams getIconCollapseToNormalParams() {
            return this.mIconCollapseToNormalParams;
        }

        @Override // com.coui.appcompat.searchview.SearchBarParamProvider
        public IconExpandToEditParams getIconExpandToEditParams() {
            return this.mIconExpandToEditParams;
        }

        @Override // com.coui.appcompat.searchview.SearchBarParamProvider
        public StateChangedToEditParams getStateChangedToEditParams() {
            return this.mStateChangedToEditParams;
        }

        @Override // com.coui.appcompat.searchview.SearchBarParamProvider
        public StateChangedToNormalParams getStateChangedToNormalParams() {
            return this.mStateChangedToNormalParams;
        }
    }

    IconCollapseToNormalParams getIconCollapseToNormalParams();

    IconExpandToEditParams getIconExpandToEditParams();

    StateChangedToEditParams getStateChangedToEditParams();

    StateChangedToNormalParams getStateChangedToNormalParams();
}