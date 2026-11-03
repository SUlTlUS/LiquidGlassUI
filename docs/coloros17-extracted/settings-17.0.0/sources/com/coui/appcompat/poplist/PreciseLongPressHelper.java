package com.coui.appcompat.poplist;

import android.view.MotionEvent;
import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public class PreciseLongPressHelper {
    private OnPreciseLongClickListener mOnPreciseLongClickListener;
    private View mTarget;
    private View.OnTouchListener mTouchListenerTransfer;
    private float[] mLastTouchDownXY = new float[2];
    private View.OnTouchListener mOnTouchListener = new AnonymousClass1();
    private View.OnLongClickListener mOnLongClickListener = new AnonymousClass2();

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.PreciseLongPressHelper$1, reason: invalid class name */
    public class AnonymousClass1 implements View.OnTouchListener {
        public AnonymousClass1() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (PreciseLongPressHelper.c(PreciseLongPressHelper.this) != null) {
                PreciseLongPressHelper.c(PreciseLongPressHelper.this).onTouch(view, motionEvent);
            }
            if (motionEvent.getActionMasked() == 0) {
                PreciseLongPressHelper.a(PreciseLongPressHelper.this)[0] = motionEvent.getX();
                PreciseLongPressHelper.a(PreciseLongPressHelper.this)[1] = motionEvent.getY();
            }
            return false;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.PreciseLongPressHelper$2, reason: invalid class name */
    public class AnonymousClass2 implements View.OnLongClickListener {
        public AnonymousClass2() {
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            PreciseLongPressHelper.b(PreciseLongPressHelper.this).onLongClick(view, Math.round(PreciseLongPressHelper.a(PreciseLongPressHelper.this)[0]), Math.round(PreciseLongPressHelper.a(PreciseLongPressHelper.this)[1]));
            return true;
        }
    }

    public interface OnPreciseLongClickListener {
        void onLongClick(View view, int i3, int i4);
    }

    public PreciseLongPressHelper(View view, OnPreciseLongClickListener onPreciseLongClickListener) {
        this.mTarget = view;
        this.mOnPreciseLongClickListener = onPreciseLongClickListener;
    }

    public static /* bridge */ /* synthetic */ float[] a(PreciseLongPressHelper preciseLongPressHelper) {
        return preciseLongPressHelper.mLastTouchDownXY;
    }

    public static /* bridge */ /* synthetic */ OnPreciseLongClickListener b(PreciseLongPressHelper preciseLongPressHelper) {
        return preciseLongPressHelper.mOnPreciseLongClickListener;
    }

    public static /* bridge */ /* synthetic */ View.OnTouchListener c(PreciseLongPressHelper preciseLongPressHelper) {
        return preciseLongPressHelper.mTouchListenerTransfer;
    }

    public void setTouchListenerTransfer(View.OnTouchListener onTouchListener) {
        this.mTouchListenerTransfer = onTouchListener;
    }

    public void setup() {
        this.mTarget.setOnTouchListener(this.mOnTouchListener);
        this.mTarget.setOnLongClickListener(this.mOnLongClickListener);
    }
}