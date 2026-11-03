package com.coui.appcompat.seekbar.shader;

import android.graphics.RuntimeShader;
import kotlin.Metadata;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u0012\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\tH&J\b\u0010\n\u001a\u00020\tH\u0016R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005¨\u0006\u000b"}, d2 = {"Lcom/coui/appcompat/seekbar/shader/BaseShaderInterface;", "", "runtimeShader", "Landroid/graphics/RuntimeShader;", "getRuntimeShader", "()Landroid/graphics/RuntimeShader;", "flushIfDirty", "", "force", "", "isSupported", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface BaseShaderInterface {

    @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class DefaultImpls {
        public static /* synthetic */ void flushIfDirty$default(BaseShaderInterface baseShaderInterface, boolean z2, int i3, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: flushIfDirty");
            }
            if ((i3 & 1) != 0) {
                z2 = false;
            }
            baseShaderInterface.flushIfDirty(z2);
        }

        public static boolean isSupported(BaseShaderInterface baseShaderInterface) {
            return true;
        }
    }

    void flushIfDirty(boolean force);

    RuntimeShader getRuntimeShader();

    boolean isSupported();
}