package com.coui.appcompat.preference;

import android.view.MotionEvent;
import android.view.View;
import com.coui.appcompat.couiswitch.COUISwitch;
import com.coui.appcompat.log.COUILog;
import com.support.preference.R;

/* JADX INFO: loaded from: classes.dex */
public class COUISwitchTouchEventDelegate implements View.OnTouchListener {
    private static final String TAG = "COUISwitchPress";
    private boolean mIsTouchInSwitchView;
    private View mItemView;
    private final OnItemViewShareEventListener mShareEventListener;
    private COUISwitch mSwitchView;

    public interface OnItemViewShareEventListener {
        void onShareEvent(MotionEvent motionEvent);
    }

    public COUISwitchTouchEventDelegate(COUISwitch cOUISwitch, View view, OnItemViewShareEventListener onItemViewShareEventListener) {
        this.mIsTouchInSwitchView = false;
        this.mShareEventListener = onItemViewShareEventListener;
        attach(view, cOUISwitch);
    }

    public static COUISwitchTouchEventDelegate bind(View view, COUISwitch cOUISwitch, OnItemViewShareEventListener onItemViewShareEventListener) {
        COUISwitchTouchEventDelegate cOUISwitchTouchEventDelegate = new COUISwitchTouchEventDelegate(cOUISwitch, view, onItemViewShareEventListener);
        cOUISwitchTouchEventDelegate.attach(view, cOUISwitch);
        return cOUISwitchTouchEventDelegate;
    }

    private boolean delegateTouchEventToSwitch(MotionEvent motionEvent) {
        dispatchItemViewPressAnimation(motionEvent);
        OnItemViewShareEventListener onItemViewShareEventListener = this.mShareEventListener;
        if (onItemViewShareEventListener != null) {
            onItemViewShareEventListener.onShareEvent(motionEvent);
        }
        int[] iArr = new int[2];
        this.mSwitchView.getLocationInWindow(iArr);
        int[] iArr2 = new int[2];
        this.mItemView.getLocationInWindow(iArr2);
        MotionEvent motionEventObtain = MotionEvent.obtain(motionEvent);
        motionEventObtain.offsetLocation(iArr2[0] - iArr[0], iArr2[1] - iArr[1]);
        try {
            this.mSwitchView.setDelegateCall(true);
            return this.mSwitchView.onTouchEvent(motionEventObtain);
        } finally {
            this.mSwitchView.setDelegateCall(false);
            motionEventObtain.recycle();
        }
    }

    private void dispatchItemViewPressAnimation(MotionEvent motionEvent) {
        View view = this.mItemView;
        if (view instanceof ListSelectedItemLayout) {
            ((ListSelectedItemLayout) view).handlePressAnimationInternal(motionEvent);
        }
    }

    private boolean isSwitchViewAvailable() {
        return this.mSwitchView.getVisibility() == 0 && this.mSwitchView.isEnabled();
    }

    private boolean isTouchDownInSwitchView(MotionEvent motionEvent) {
        float x2 = motionEvent.getX();
        float y2 = motionEvent.getY();
        int[] iArr = new int[2];
        this.mSwitchView.getLocationInWindow(iArr);
        int[] iArr2 = new int[2];
        this.mItemView.getLocationInWindow(iArr2);
        float f3 = x2 + (iArr2[0] - iArr[0]);
        float f4 = y2 + (iArr2[1] - iArr[1]);
        return f3 >= 0.0f && f3 <= ((float) this.mSwitchView.getWidth()) && f4 >= 0.0f && f4 <= ((float) this.mSwitchView.getHeight());
    }

    private void resetTouchState() {
        this.mIsTouchInSwitchView = false;
    }

    public final void attach(View view, COUISwitch cOUISwitch) {
        if (this.mIsTouchInSwitchView) {
            COUILog.e(TAG, "attach skip, touching");
            return;
        }
        View view2 = this.mItemView;
        if (view2 == view && this.mSwitchView == cOUISwitch) {
            return;
        }
        if (view2 == null || view2 == view) {
            COUILog.d(TAG, "attach bind " + view);
        } else {
            COUILog.d(TAG, "attach migrate " + this.mItemView + " -> " + view);
            View view3 = this.mItemView;
            int i3 = R.id.coui_switch_touch_delegate_owner;
            if (view3.getTag(i3) == this) {
                this.mItemView.setOnTouchListener(null);
                this.mItemView.setTag(i3, null);
            }
        }
        this.mItemView = view;
        this.mSwitchView = cOUISwitch;
        if (view != null) {
            view.setOnTouchListener(this);
            view.setTag(R.id.coui_switch_touch_delegate_owner, this);
        }
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!isSwitchViewAvailable()) {
            resetTouchState();
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked != 1 && actionMasked != 3) {
                return this.mIsTouchInSwitchView && delegateTouchEventToSwitch(motionEvent);
            }
            if (!this.mIsTouchInSwitchView) {
                resetTouchState();
                return false;
            }
            boolean zDelegateTouchEventToSwitch = delegateTouchEventToSwitch(motionEvent);
            resetTouchState();
            return zDelegateTouchEventToSwitch;
        }
        boolean zIsTouchDownInSwitchView = isTouchDownInSwitchView(motionEvent);
        this.mIsTouchInSwitchView = zIsTouchDownInSwitchView;
        if (!zIsTouchDownInSwitchView) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("press touch=");
        sb.append(view);
        sb.append(" delegate=");
        sb.append(this.mItemView);
        sb.append(" ok=");
        sb.append(view == this.mItemView);
        COUILog.d(TAG, sb.toString());
        return delegateTouchEventToSwitch(motionEvent);
    }

    public static COUISwitchTouchEventDelegate bind(View view, COUISwitch cOUISwitch) {
        return bind(view, cOUISwitch, null);
    }

    public COUISwitchTouchEventDelegate(COUISwitch cOUISwitch, View view) {
        this(cOUISwitch, view, null);
    }
}