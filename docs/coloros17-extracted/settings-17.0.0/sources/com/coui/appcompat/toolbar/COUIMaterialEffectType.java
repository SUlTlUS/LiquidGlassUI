package com.coui.appcompat.toolbar;

import com.heytap.cloud.sdk.utils.Constants;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import kotlin.Metadata;
import kotlin.annotation.AnnotationRetention;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001\u0007B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"Lcom/coui/appcompat/toolbar/COUIMaterialEffectType;", "", "()V", Constants.RecoveryProcessDataType.TYPE_DEFAULT, "", "TYPE_DEFAULT_BLUR", "TYPE_IMMERSIVE", "TYPE", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class COUIMaterialEffectType {
    public static final COUIMaterialEffectType INSTANCE = new COUIMaterialEffectType();
    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_DEFAULT_BLUR = 2;
    public static final int TYPE_IMMERSIVE = 1;

    @Metadata(d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\b\u0087\u0002\u0018\u00002\u00020\u0001B\u0000¨\u0006\u0002"}, d2 = {"Lcom/coui/appcompat/toolbar/COUIMaterialEffectType$TYPE;", "", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    @Retention(RetentionPolicy.SOURCE)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    public @interface TYPE {
    }

    private COUIMaterialEffectType() {
    }
}