package com.coui.appcompat.seekbar.core;

import android.os.Parcel;
import android.os.Parcelable;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0010\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u001d\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0016¢\u0006\u0002\u0010\n¨\u0006\u000b"}, d2 = {"com/coui/appcompat/seekbar/core/COUISeekBarCore$SavedState$Companion$CREATOR$1", "Landroid/os/Parcelable$Creator;", "Lcom/coui/appcompat/seekbar/core/COUISeekBarCore$SavedState;", "createFromParcel", "source", "Landroid/os/Parcel;", "newArray", "", "size", "", "(I)[Lcom/coui/appcompat/seekbar/core/COUISeekBarCore$SavedState;", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class COUISeekBarCore$SavedState$Companion$CREATOR$1 implements Parcelable.Creator<COUISeekBarCore.SavedState> {
    @Override // android.os.Parcelable.Creator
    public /* bridge */ /* synthetic */ COUISeekBarCore.SavedState createFromParcel(Parcel parcel) {
        return createFromParcel(parcel);
    }

    @Override // android.os.Parcelable.Creator
    public /* bridge */ /* synthetic */ COUISeekBarCore.SavedState[] newArray(int i3) {
        return newArray(i3);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public COUISeekBarCore.SavedState createFromParcel(Parcel source) {
        Intrinsics.checkNotNullParameter(source, "source");
        return new COUISeekBarCore.SavedState(source, null);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public COUISeekBarCore.SavedState[] newArray(int size) {
        return new COUISeekBarCore.SavedState[size];
    }
}