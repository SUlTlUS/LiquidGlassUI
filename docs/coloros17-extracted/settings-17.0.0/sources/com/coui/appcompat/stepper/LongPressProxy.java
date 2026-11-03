package com.coui.appcompat.stepper;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.view.GestureDetectorCompat;

/* JADX INFO: loaded from: classes7.dex */
class LongPressProxy {
    public static final int CLICK_EVENT = 1;
    public static final int LONG_CLICK_EVENT = 2;
    private GestureDetectorCompat mGestureDetectorCompat;
    private View.OnTouchListener mOnTouchListener;
    private Runnable mRunnable;
    private View mView;
    private Handler mHandler = new MyHandler(Looper.getMainLooper());
    private GestureDetector.OnGestureListener mGestureListener = new AnonymousClass1();

    /* JADX INFO: renamed from: com.coui.appcompat.stepper.LongPressProxy$1, reason: invalid class name */
    public class AnonymousClass1 extends GestureDetector.SimpleOnGestureListener {
        public AnonymousClass1() {
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
            super.onLongPress(motionEvent);
            LongPressProxy.access$000(LongPressProxy.this).sendEmptyMessage(2);
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            LongPressProxy.access$000(LongPressProxy.this).sendEmptyMessage(1);
            return true;
        }
    }

    public class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i3 = message.what;
            if (i3 == 1) {
                LongPressProxy.access$100(LongPressProxy.this).run();
            } else if (i3 == 2 && LongPressProxy.access$200(LongPressProxy.this).isEnabled()) {
                LongPressProxy.access$100(LongPressProxy.this).run();
                sendEmptyMessageDelayed(2, 100L);
            }
        }
    }

    public LongPressProxy(View view, Runnable runnable) {
        this.mView = view;
        this.mRunnable = runnable;
        this.mGestureDetectorCompat = new GestureDetectorCompat(this.mView.getContext(), this.mGestureListener);
        init();
    }

    public static /* synthetic */ boolean a(LongPressProxy longPressProxy, View view, MotionEvent motionEvent) {
        View.OnTouchListener onTouchListener = longPressProxy.mOnTouchListener;
        if (onTouchListener != null) {
            onTouchListener.onTouch(view, motionEvent);
        }
        longPressProxy.mGestureDetectorCompat.onTouchEvent(motionEvent);
        if (motionEvent.getActionMasked() != 3 && motionEvent.getActionMasked() != 1) {
            return true;
        }
        longPressProxy.mHandler.removeMessages(2);
        return true;
    }

    public static /* synthetic */ Handler access$000(LongPressProxy longPressProxy) {
        return longPressProxy.mHandler;
    }

    public static /* synthetic */ Runnable access$100(LongPressProxy longPressProxy) {
        return longPressProxy.mRunnable;
    }

    public static /* synthetic */ View access$200(LongPressProxy longPressProxy) {
        return longPressProxy.mView;
    }

    private void init() {
        this.mView.setOnTouchListener(new d(this));
    }

    public void release() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            this.mHandler = null;
        }
        View view = this.mView;
        if (view != null) {
            view.setOnTouchListener(null);
            this.mView.removeCallbacks(this.mRunnable);
            this.mView = null;
        }
        this.mRunnable = null;
        this.mOnTouchListener = null;
    }

    public void setOnTouchListener(View.OnTouchListener onTouchListener) {
        this.mOnTouchListener = onTouchListener;
    }
}