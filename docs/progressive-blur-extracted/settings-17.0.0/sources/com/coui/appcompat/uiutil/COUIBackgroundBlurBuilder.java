package com.coui.appcompat.uiutil;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.OnMaterialStateChangeListener;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.effect.R;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.coui.appcompat.version.COUIVersionUtil;
import com.oplus.graphics.OplusBlurParam;
import com.oplus.view.ViewRootManager;

/* JADX INFO: loaded from: classes7.dex */
@Deprecated
public class COUIBackgroundBlurBuilder implements OnMaterialStateChangeListener {
    private static final int BLUR_TYPE_MAJOR_VERSION = 38;
    private static final int BLUR_TYPE_SUB_VERSION = 41;
    private static final int MAX_ALPHA = 255;
    private static final int SMOOTH_CORNER_TYPE = 1;
    private static final String SYSTEM_MATERIAL_BLUR_ENABLE = "system_material_blur_enable";
    private static final String TAG = "BackgroundBlurBuilder";
    private Drawable mBackgroundBlurDrawable;
    private int mBlurRadius;
    private OnBlurryStateChangeListener mBlurryStateChangeListener;
    private Context mContext;
    private float mCornerRadiusBottomLeft;
    private float mCornerRadiusBottomRight;
    private float mCornerRadiusTopLeft;
    private float mCornerRadiusTopRight;
    private boolean mIsDarkMode;
    private Drawable mOriginalBackground;
    private View mRootView;
    private float mSmoothWeight;
    private View mTargetView;
    private boolean mUseBackgroundBlur;
    private ViewRootManager mViewRootManager;
    private float[] mBlendColorLight = null;
    private float[] mBlendColorDark = null;
    private float[] mMixColorLight = null;
    private float[] mMixColorDark = null;
    private boolean mMaterialBlurState = false;
    private boolean mMaterialStateListenerRegistered = false;
    private int mMode = -1;
    private boolean mIsBlurryState = false;
    private int mColorNoBlur = -1;
    private boolean mAlwayBlur = false;

    public interface OnBlurryStateChangeListener {
        void onBlurryStateChanged(boolean z2);
    }

    public COUIBackgroundBlurBuilder(Context context) {
        this.mContext = context;
        init();
    }

    private OplusBlurParam createBlurParams() {
        float[] fArr;
        float[] fArr2;
        OplusBlurParam oplusBlurParam = new OplusBlurParam();
        oplusBlurParam.setBlurType(2);
        if (this.mMode == -1) {
            this.mMode = this.mIsDarkMode ? 2 : 3;
        }
        if (this.mIsDarkMode) {
            fArr = this.mBlendColorDark;
            fArr2 = this.mMixColorDark;
        } else {
            fArr = this.mBlendColorLight;
            fArr2 = this.mMixColorLight;
        }
        oplusBlurParam.setMaterialParams(this.mMode, fArr, fArr2);
        if (RoundCornerUtil.isSupportRoundCornerWhenBlur()) {
            oplusBlurParam.setSmoothCornerWeight(this.mSmoothWeight);
            COUILog.i(TAG, "Current version supports roundCorner when using blur");
            if (COUIVersionUtil.checkOPlusViewSubSDK(38, 41)) {
                oplusBlurParam.setSmoothCornerType(1);
            }
        }
        return oplusBlurParam;
    }

    private int getEffectiveBlurRadius() {
        if (shouldEnableBlur()) {
            return this.mBlurRadius;
        }
        return 0;
    }

    private void init() {
        this.mIsDarkMode = COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(this.mContext);
    }

    private void registerMaterialStateListener() {
        if (this.mMaterialStateListenerRegistered) {
            return;
        }
        COUIMaterialStateManager cOUIMaterialStateManager = COUIMaterialStateManager.INSTANCE;
        cOUIMaterialStateManager.registerMaterialStateObserver(this.mContext, 16);
        cOUIMaterialStateManager.addOnMaterialStateChangedListener(this);
        this.mMaterialStateListenerRegistered = true;
    }

    private void restoreOriginalBackground() {
        this.mTargetView.setBackground(this.mOriginalBackground);
    }

    private void setBlurEnable(String str) {
        if (this.mViewRootManager == null || this.mTargetView == null) {
            COUILog.w(TAG, "setBlurEnable skipped: mViewRootManager or mTargetView is null, tag:" + str);
            return;
        }
        boolean zShouldEnableBlur = shouldEnableBlur();
        if (zShouldEnableBlur && this.mBackgroundBlurDrawable != null) {
            Drawable background = this.mTargetView.getBackground();
            Drawable drawable = this.mBackgroundBlurDrawable;
            if (background != drawable) {
                drawable.setAlpha((int) (this.mTargetView.getAlpha() * 255.0f));
                this.mTargetView.setBackground(this.mBackgroundBlurDrawable);
            }
        }
        this.mViewRootManager.setBlurRadius(getEffectiveBlurRadius());
        if (!zShouldEnableBlur) {
            restoreOriginalBackground();
        }
        this.mTargetView.invalidate();
        COUILog.i(TAG, "setBlurEnable mLastBlurState = " + this.mMaterialBlurState + ",tag:" + str);
    }

    private boolean shouldEnableBlur() {
        if (this.mUseBackgroundBlur) {
            return this.mAlwayBlur || this.mMaterialBlurState;
        }
        return false;
    }

    private void unregisterMaterialStateListener() {
        if (this.mMaterialStateListenerRegistered) {
            COUIMaterialStateManager.INSTANCE.removeOnMaterialStateChangedListener(this);
            this.mMaterialStateListenerRegistered = false;
        }
    }

    private void updateBlurState() {
        if (this.mViewRootManager == null) {
            return;
        }
        this.mViewRootManager.setBlurParams(createBlurParams());
        this.mViewRootManager.setBlurRadius(getEffectiveBlurRadius());
        refreshCornerRadius();
    }

    private void updateBlurryState() {
        boolean zShouldEnableBlur = shouldEnableBlur();
        if (this.mIsBlurryState != zShouldEnableBlur) {
            this.mIsBlurryState = zShouldEnableBlur;
            OnBlurryStateChangeListener onBlurryStateChangeListener = this.mBlurryStateChangeListener;
            if (onBlurryStateChangeListener != null) {
                onBlurryStateChangeListener.onBlurryStateChanged(zShouldEnableBlur);
            }
        }
    }

    public void applyBlurBackground() {
        if (useBackgroundBlur()) {
            COUILog.i(TAG, "applyBlurBackground");
            registerMaterialStateListener();
            if (this.mTargetView == null) {
                throw new IllegalStateException("Must setTargetView before applyBlurBackground");
            }
            if (this.mViewRootManager == null) {
                if (this.mRootView != null) {
                    this.mViewRootManager = new ViewRootManager(this.mRootView);
                } else {
                    this.mViewRootManager = new ViewRootManager(this.mTargetView);
                }
            }
            Drawable backgroundBlurDrawable = this.mViewRootManager.getBackgroundBlurDrawable();
            if (backgroundBlurDrawable != null) {
                this.mBackgroundBlurDrawable = backgroundBlurDrawable;
                if (this.mTargetView.getBackground() != backgroundBlurDrawable) {
                    this.mOriginalBackground = this.mTargetView.getBackground();
                }
                backgroundBlurDrawable.setAlpha((int) (this.mTargetView.getAlpha() * 255.0f));
                if (shouldEnableBlur()) {
                    this.mTargetView.setBackground(backgroundBlurDrawable);
                }
            }
            updateBlurState();
            setBlurEnable("ApplyBlurBackground");
            updateBlurryState();
        }
    }

    public boolean isAlwayBlur() {
        return this.mAlwayBlur;
    }

    public boolean isBlurryState() {
        return this.mIsBlurryState;
    }

    public boolean isMaterialBlurEnabled() {
        return Settings.System.getInt(this.mContext.getContentResolver(), SYSTEM_MATERIAL_BLUR_ENABLE, 0) == 1;
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public CharSequence onDump() {
        return "BackgroundBlurBuilder: blurEnabled=" + this.mMaterialBlurState;
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public void onMaterialStateChange(int i3, boolean z2) {
        if (i3 != 16 || this.mMaterialBlurState == z2) {
            return;
        }
        this.mMaterialBlurState = z2;
        setBlurEnable("MaterialStateManager");
        updateBlurryState();
    }

    public void onTargetViewAlphaChanged() {
        View view = this.mTargetView;
        if (view == null || this.mBackgroundBlurDrawable == null) {
            return;
        }
        Drawable background = view.getBackground();
        Drawable drawable = this.mBackgroundBlurDrawable;
        if (background == drawable) {
            drawable.setAlpha((int) (this.mTargetView.getAlpha() * 255.0f));
        }
    }

    public void refreshBlurParams() {
        if (!useBackgroundBlur() || this.mViewRootManager == null) {
            return;
        }
        this.mViewRootManager.setBlurParams(createBlurParams());
        this.mViewRootManager.setBlurRadius(getEffectiveBlurRadius());
        refreshCornerRadius();
        View view = this.mTargetView;
        if (view != null) {
            view.invalidate();
        }
    }

    public void refreshCornerRadius() {
        ViewRootManager viewRootManager = this.mViewRootManager;
        if (viewRootManager != null) {
            viewRootManager.setCornerRadius(this.mCornerRadiusTopLeft, this.mCornerRadiusTopRight, this.mCornerRadiusBottomLeft, this.mCornerRadiusBottomRight);
        }
    }

    public void release() {
        unregisterMaterialStateListener();
        View view = this.mTargetView;
        if (view != null && this.mBackgroundBlurDrawable != null && view.getBackground() == this.mBackgroundBlurDrawable) {
            restoreOriginalBackground();
        }
        this.mViewRootManager = null;
        this.mBackgroundBlurDrawable = null;
        this.mOriginalBackground = null;
        COUILog.i(TAG, "release");
    }

    public COUIBackgroundBlurBuilder setAlwayBlur(boolean z2) {
        this.mAlwayBlur = z2;
        return this;
    }

    public void setBlendColorDark(float[] fArr) {
        this.mBlendColorDark = fArr;
    }

    public void setBlendColorLight(float[] fArr) {
        this.mBlendColorLight = fArr;
    }

    public COUIBackgroundBlurBuilder setBlurRadius(int i3) {
        this.mBlurRadius = i3;
        return this;
    }

    @Deprecated
    public void setColorNoBlur(int i3) {
    }

    public COUIBackgroundBlurBuilder setCornerRadius(float f3) {
        this.mCornerRadiusTopLeft = f3;
        this.mCornerRadiusTopRight = f3;
        this.mCornerRadiusBottomLeft = f3;
        this.mCornerRadiusBottomRight = f3;
        return this;
    }

    @Deprecated
    public void setDetachBlurDrawableWhenDisabled(boolean z2) {
    }

    @Deprecated
    public void setManageColorViaOverlay(boolean z2) {
    }

    public void setMixColorDark(float[] fArr) {
        this.mMixColorDark = fArr;
    }

    public void setMixColorLight(float[] fArr) {
        this.mMixColorLight = fArr;
    }

    public void setMode(int i3) {
        this.mMode = i3;
    }

    public void setOnBlurryStateChangeListener(OnBlurryStateChangeListener onBlurryStateChangeListener) {
        this.mBlurryStateChangeListener = onBlurryStateChangeListener;
    }

    public void setOriginDrawable(Drawable drawable) {
        this.mOriginalBackground = drawable;
        setBlurEnable("setOriginDrawable");
    }

    public COUIBackgroundBlurBuilder setRootView(View view) {
        this.mRootView = view;
        return this;
    }

    public COUIBackgroundBlurBuilder setSmoothWeight(float f3) {
        this.mSmoothWeight = f3;
        return this;
    }

    public COUIBackgroundBlurBuilder setTargetView(View view) {
        this.mTargetView = view;
        return this;
    }

    public COUIBackgroundBlurBuilder setUseBackgroundBlur(boolean z2, AnimLevel animLevel) {
        return setUseBackgroundBlur(z2, animLevel, this.mContext.getResources().getBoolean(R.bool.coui_blur_enable) & COUIMaterialStateManager.INSTANCE.checkThemeEnable(this.mContext));
    }

    public boolean useBackgroundBlur() {
        return this.mUseBackgroundBlur;
    }

    public COUIBackgroundBlurBuilder setUseBackgroundBlur(boolean z2, AnimLevel animLevel, boolean z3) {
        if (ShadowUtils.checkOPlusViewElevationSDK() && UIUtil.confirmLevelAnim(animLevel) && z3) {
            this.mUseBackgroundBlur = z2;
            if (z2) {
                registerMaterialStateListener();
            }
        } else {
            Log.e(TAG, "Machines below V do not support setting blurred backgrounds or current animLevel is too low or is in third party theme");
            this.mUseBackgroundBlur = false;
        }
        updateBlurryState();
        return this;
    }

    public COUIBackgroundBlurBuilder setCornerRadius(float f3, float f4, float f5, float f6) {
        this.mCornerRadiusTopLeft = f3;
        this.mCornerRadiusTopRight = f4;
        this.mCornerRadiusBottomLeft = f5;
        this.mCornerRadiusBottomRight = f6;
        return this;
    }
}