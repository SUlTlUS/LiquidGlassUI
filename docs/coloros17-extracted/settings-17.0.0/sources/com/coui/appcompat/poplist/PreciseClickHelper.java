package com.coui.appcompat.poplist;

import android.view.MotionEvent;
import android.view.View;
import com.coui.appcompat.AccessibilityUtils.COUIAccessibilityUtil;

/* JADX INFO: loaded from: classes7.dex */
public class PreciseClickHelper {
    private OnPreciseClickListener mOnPreciseClickListener;
    private View mTarget;
    private float[] mLastTouchDownXY = new float[2];
    private View.OnTouchListener mOnTouchListener = new AnonymousClass1();
    private View.OnClickListener mOnClickListener = new AnonymousClass2();

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.PreciseClickHelper$1, reason: invalid class name */
    public class AnonymousClass1 implements View.OnTouchListener {
        public AnonymousClass1() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getActionMasked() == 0) {
                PreciseClickHelper.a(PreciseClickHelper.this)[0] = motionEvent.getX();
                PreciseClickHelper.a(PreciseClickHelper.this)[1] = motionEvent.getY();
            }
            return false;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.PreciseClickHelper$2, reason: invalid class name */
    public class AnonymousClass2 implements View.OnClickListener {
        public AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (COUIAccessibilityUtil.isTalkbackEnabled(view.getContext()) || (PreciseClickHelper.a(PreciseClickHelper.this)[0] == 0.0f && PreciseClickHelper.a(PreciseClickHelper.this)[1] == 0.0f)) {
                PreciseClickHelper.b(PreciseClickHelper.this).onClick(view, view.getWidth() / 2, view.getHeight() / 2);
                return;
            }
            PreciseClickHelper.b(PreciseClickHelper.this).onClick(view, Math.round(PreciseClickHelper.a(PreciseClickHelper.this)[0]), Math.round(PreciseClickHelper.a(PreciseClickHelper.this)[1]));
        }
    }

    public interface OnPreciseClickListener {
        void onClick(View view, int i3, int i4);
    }

    public PreciseClickHelper(View view, OnPreciseClickListener onPreciseClickListener) {
        this.mTarget = view;
        this.mOnPreciseClickListener = onPreciseClickListener;
    }

    public static /* bridge */ /* synthetic */ float[] a(PreciseClickHelper preciseClickHelper) {
        return preciseClickHelper.mLastTouchDownXY;
    }

    public static /* bridge */ /* synthetic */ OnPreciseClickListener b(PreciseClickHelper preciseClickHelper) {
        return preciseClickHelper.mOnPreciseClickListener;
    }

    public View getTargetView() {
        return this.mTarget;
    }

    public void setup() {
        this.mTarget.setOnTouchListener(this.mOnTouchListener);
        this.mTarget.setOnClickListener(this.mOnClickListener);
    }

    public void unSet() {
        this.mTarget.setOnClickListener(null);
        this.mTarget.setOnTouchListener(null);
    }
}