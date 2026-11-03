package com.coui.appcompat.cardview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import com.coui.appcompat.roundRect.COUIRoundRectUtil;

/* JADX INFO: loaded from: classes7.dex */
class CardViewApi17Impl extends CardViewBaseImpl {

    /* JADX INFO: renamed from: com.coui.appcompat.cardview.CardViewApi17Impl$1, reason: invalid class name */
    public class AnonymousClass1 implements RoundRectDrawableWithShadow.RoundRectHelper {
        public AnonymousClass1() {
        }

        @Override // com.coui.appcompat.cardview.RoundRectDrawableWithShadow.RoundRectHelper
        public void drawRoundRect(Canvas canvas, RectF rectF, float f3, Paint paint) {
            canvas.drawPath(COUIRoundRectUtil.getInstance().getPath(rectF, f3), paint);
        }
    }

    @Override // com.coui.appcompat.cardview.CardViewBaseImpl, com.coui.appcompat.cardview.CardViewImpl
    public void initStatic() {
        RoundRectDrawableWithShadow.setRoundRectHelper(new AnonymousClass1());
    }
}