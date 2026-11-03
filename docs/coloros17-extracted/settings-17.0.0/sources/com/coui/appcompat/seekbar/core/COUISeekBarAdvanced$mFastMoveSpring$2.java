package com.coui.appcompat.seekbar.core;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\n \u0002*\u0004\u0018\u00010\u00010\u0001H\n¢\u0006\u0002\b\u0003"}, d2 = {"<anonymous>", "Lcom/facebook/rebound/Spring;", "kotlin.jvm.PlatformType", "invoke"}, k = 3, mv = {1, 9, 0}, xi = 48)
public final class COUISeekBarAdvanced$mFastMoveSpring$2 extends Lambda implements Function0<Spring> {
    final /* synthetic */ COUISeekBarAdvanced this$0;

    /* JADX INFO: renamed from: com.coui.appcompat.seekbar.core.COUISeekBarAdvanced$mFastMoveSpring$2$1, reason: invalid class name */
    @Metadata(d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0007\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\t"}, d2 = {"com/coui/appcompat/seekbar/core/COUISeekBarAdvanced$mFastMoveSpring$2$1", "Lcom/facebook/rebound/SpringListener;", "onSpringActivate", "", "spring", "Lcom/facebook/rebound/Spring;", "onSpringAtRest", "onSpringEndStateChange", "onSpringUpdate", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class AnonymousClass1 implements SpringListener {
        final /* synthetic */ COUISeekBarAdvanced this$0;

        public AnonymousClass1(COUISeekBarAdvanced cOUISeekBarAdvanced) {
            this.this$0 = cOUISeekBarAdvanced;
        }

        @Override // com.facebook.rebound.SpringListener
        public void onSpringActivate(Spring spring) {
            Intrinsics.checkNotNullParameter(spring, "spring");
        }

        @Override // com.facebook.rebound.SpringListener
        public void onSpringAtRest(Spring spring) {
            Intrinsics.checkNotNullParameter(spring, "spring");
        }

        @Override // com.facebook.rebound.SpringListener
        public void onSpringEndStateChange(Spring spring) {
            Intrinsics.checkNotNullParameter(spring, "spring");
        }

        @Override // com.facebook.rebound.SpringListener
        public void onSpringUpdate(Spring spring) {
            Intrinsics.checkNotNullParameter(spring, "spring");
            if (this.this$0.getMFastMoveScaleOffsetX() == ((float) spring.getEndValue())) {
                return;
            }
            COUISeekBarAdvanced cOUISeekBarAdvanced = this.this$0;
            cOUISeekBarAdvanced.setMFastMoveScaleOffsetX(cOUISeekBarAdvanced.isEnabled() ? (float) spring.getCurrentValue() : 0.0f);
            this.this$0.invalidate();
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUISeekBarAdvanced$mFastMoveSpring$2(COUISeekBarAdvanced cOUISeekBarAdvanced) {
        super(0);
        this.this$0 = cOUISeekBarAdvanced;
    }

    @Override // kotlin.jvm.functions.Function0
    public /* bridge */ /* synthetic */ Spring invoke() {
        return invoke();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    public final Spring invoke() {
        Spring springCreateSpring = SpringSystem.create().createSpring();
        springCreateSpring.setSpringConfig(COUISeekBarAdvanced.access$getMFastMoveSpringConfig$p(this.this$0));
        springCreateSpring.addListener(new AnonymousClass1(this.this$0));
        return springCreateSpring;
    }
}