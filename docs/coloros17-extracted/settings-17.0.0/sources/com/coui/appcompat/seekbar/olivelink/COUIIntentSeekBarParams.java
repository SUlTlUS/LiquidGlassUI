package com.coui.appcompat.seekbar.olivelink;

import com.coui.iola.SpringData;
import com.oplus.settings.feature.language.localepicker.RegionPickerUtils;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\b\u0018\u0000 \u00192\u00020\u0001:\u0001\u0019B\u0019\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0015\u001a\u00020\u0016HÖ\u0001J\t\u0010\u0017\u001a\u00020\u0018HÖ\u0001R \u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u0006\u0010\u0007\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR \u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\f\u0010\u0007\u001a\u0004\b\r\u0010\t\"\u0004\b\u000e\u0010\u000b¨\u0006\u001a"}, d2 = {"Lcom/coui/appcompat/seekbar/olivelink/COUIIntentSeekBarParams;", "", "initialLoadingSpring", "Lcom/coui/iola/SpringData;", "bufferingSpring", "(Lcom/coui/iola/SpringData;Lcom/coui/iola/SpringData;)V", "getBufferingSpring$annotations", "()V", "getBufferingSpring", "()Lcom/coui/iola/SpringData;", "setBufferingSpring", "(Lcom/coui/iola/SpringData;)V", "getInitialLoadingSpring$annotations", "getInitialLoadingSpring", "setInitialLoadingSpring", "component1", "component2", "copy", "equals", "", RegionPickerUtils.OTHER_SYSTEM_REGION_KEY, "hashCode", "", "toString", "", "Companion", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final /* data */ class COUIIntentSeekBarParams {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final float DEFAULT_LOADING_SPRING_BOUNCE = 0.0f;
    private static final float DEFAULT_LOADING_SPRING_RESPONSE = 0.7f;
    private static final float DEFAULT_LOADING_SPRING_VELOCITY = 0.0f;
    private SpringData bufferingSpring;
    private SpringData initialLoadingSpring;

    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0007"}, d2 = {"Lcom/coui/appcompat/seekbar/olivelink/COUIIntentSeekBarParams$Companion;", "", "()V", "DEFAULT_LOADING_SPRING_BOUNCE", "", "DEFAULT_LOADING_SPRING_RESPONSE", "DEFAULT_LOADING_SPRING_VELOCITY", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public COUIIntentSeekBarParams() {
        this(null, null, 3, null);
    }

    public static /* synthetic */ COUIIntentSeekBarParams copy$default(COUIIntentSeekBarParams cOUIIntentSeekBarParams, SpringData springData, SpringData springData2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            springData = cOUIIntentSeekBarParams.initialLoadingSpring;
        }
        if ((i3 & 2) != 0) {
            springData2 = cOUIIntentSeekBarParams.bufferingSpring;
        }
        return cOUIIntentSeekBarParams.copy(springData, springData2);
    }

    public static /* synthetic */ void getBufferingSpring$annotations() {
    }

    public static /* synthetic */ void getInitialLoadingSpring$annotations() {
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final SpringData getInitialLoadingSpring() {
        return this.initialLoadingSpring;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final SpringData getBufferingSpring() {
        return this.bufferingSpring;
    }

    public final COUIIntentSeekBarParams copy(SpringData initialLoadingSpring, SpringData bufferingSpring) {
        Intrinsics.checkNotNullParameter(initialLoadingSpring, "initialLoadingSpring");
        Intrinsics.checkNotNullParameter(bufferingSpring, "bufferingSpring");
        return new COUIIntentSeekBarParams(initialLoadingSpring, bufferingSpring);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof COUIIntentSeekBarParams)) {
            return false;
        }
        COUIIntentSeekBarParams cOUIIntentSeekBarParams = (COUIIntentSeekBarParams) other;
        return Intrinsics.areEqual(this.initialLoadingSpring, cOUIIntentSeekBarParams.initialLoadingSpring) && Intrinsics.areEqual(this.bufferingSpring, cOUIIntentSeekBarParams.bufferingSpring);
    }

    public final SpringData getBufferingSpring() {
        return this.bufferingSpring;
    }

    public final SpringData getInitialLoadingSpring() {
        return this.initialLoadingSpring;
    }

    public int hashCode() {
        return (this.initialLoadingSpring.hashCode() * 31) + this.bufferingSpring.hashCode();
    }

    public final void setBufferingSpring(SpringData springData) {
        Intrinsics.checkNotNullParameter(springData, "<set-?>");
        this.bufferingSpring = springData;
    }

    public final void setInitialLoadingSpring(SpringData springData) {
        Intrinsics.checkNotNullParameter(springData, "<set-?>");
        this.initialLoadingSpring = springData;
    }

    public String toString() {
        return "COUIIntentSeekBarParams(initialLoadingSpring=" + this.initialLoadingSpring + ", bufferingSpring=" + this.bufferingSpring + ')';
    }

    public COUIIntentSeekBarParams(SpringData initialLoadingSpring, SpringData bufferingSpring) {
        Intrinsics.checkNotNullParameter(initialLoadingSpring, "initialLoadingSpring");
        Intrinsics.checkNotNullParameter(bufferingSpring, "bufferingSpring");
        this.initialLoadingSpring = initialLoadingSpring;
        this.bufferingSpring = bufferingSpring;
    }

    public /* synthetic */ COUIIntentSeekBarParams(SpringData springData, SpringData springData2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? new SpringData(0.0f, 0.7f, 0.0f) : springData, (i3 & 2) != 0 ? new SpringData(0.0f, 0.7f, 0.0f) : springData2);
    }
}