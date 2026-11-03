package com.coui.appcompat.searchview.params;

import kotlin.Metadata;

/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u000b\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\b\u0010\u0004\u001a\u00020\u0003H\u0016J\b\u0010\u0005\u001a\u00020\u0003H&J\b\u0010\u0006\u001a\u00020\u0003H\u0016J\b\u0010\u0007\u001a\u00020\u0003H&J\b\u0010\b\u001a\u00020\u0003H\u0016J\b\u0010\t\u001a\u00020\u0003H&J\b\u0010\n\u001a\u00020\u0003H\u0016J\b\u0010\u000b\u001a\u00020\u0003H&J\b\u0010\f\u001a\u00020\u0003H&J\b\u0010\r\u001a\u00020\u0003H&¨\u0006\u000e"}, d2 = {"Lcom/coui/appcompat/searchview/params/StateChangedToNormalParams;", "", "getBounce", "", "getFunBtnBounce", "getFunBtnMinimumVisibleChange", "getFunBtnResponse", "getFunctionalButtonEndScale", "getOuterBtnBounce", "getOuterBtnMinimumVisibleChange", "getOuterBtnResponse", "getOuterButtonInitScale", "getResponse", "getStateSpringMinimumVisibleChange", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface StateChangedToNormalParams {

    /* JADX INFO: loaded from: classes7.dex */
    @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class DefaultImpls {
        public static float getFunBtnBounce(StateChangedToNormalParams stateChangedToNormalParams) {
            return stateChangedToNormalParams.getBounce();
        }

        public static float getFunBtnResponse(StateChangedToNormalParams stateChangedToNormalParams) {
            return stateChangedToNormalParams.getResponse();
        }

        public static float getOuterBtnBounce(StateChangedToNormalParams stateChangedToNormalParams) {
            return stateChangedToNormalParams.getBounce();
        }

        public static float getOuterBtnResponse(StateChangedToNormalParams stateChangedToNormalParams) {
            return stateChangedToNormalParams.getResponse();
        }
    }

    float getBounce();

    float getFunBtnBounce();

    float getFunBtnMinimumVisibleChange();

    float getFunBtnResponse();

    float getFunctionalButtonEndScale();

    float getOuterBtnBounce();

    float getOuterBtnMinimumVisibleChange();

    float getOuterBtnResponse();

    float getOuterButtonInitScale();

    float getResponse();

    float getStateSpringMinimumVisibleChange();
}