package com.coui.appcompat.picker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.animation.PathInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.math.COUIMathUtils;
import com.coui.appcompat.soundloadutil.COUISoundLoadUtil;
import com.coui.appcompat.vibrateutil.VibrateUtils;
import com.decouple.injector.adaptor.BaseConfig;
import com.google.android.material.timepicker.TimeModel;
import com.oplus.os.LinearmotorVibrator;
import com.oplus.settings.feature.smartkey.SmartKeyConstantUtils;
import com.oplus.settings.romupdate.RomUpdateListParser;
import com.oplus.settings.utils.BreathingLightUtils;
import com.oplus.utils.Constant;
import com.oplus.wrapper.os.SystemProperties;
import com.support.picker.R;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes7.dex */
public class COUINumberPicker extends LinearLayout {
    public static final int ALIGN_LEFT = 1;
    public static final int ALIGN_MIDDLE = 0;
    public static final int ALIGN_RIGHT = 2;
    private static final float BASE_RATIO = 0.6f;
    private static final int CALCULATE_MAX_COUNT = 100;
    private static final long DEFAULT_LONG_PRESS_UPDATE_INTERVAL = 300;
    private static final int LOW_VELOCITY_THRESHOLD = 1000;
    private static final int MAX_VELOCITY = 5000;
    private static final int MID_VELOCITY_THRESHOLD = 2000;
    private static final int MILLISECOND_VELOCITY_UNIT = 1000;
    private static final int MINIMUM_FLING_VELOCITY = 750;
    private static final int MIN_BACKGROUND_DIVIDER_HEIGHT = 1;
    private static final int MSG_PLAY_SOUND = 0;
    private static final int MSG_PLAY_VIBRATE = 2;
    private static final int MSG_TALKBACK_VALUE_CHANGE = 1;
    private static final float NEXT_VALUE_ERROR = 0.05f;
    private static final int ONE_SECOND_MILLIS = 1000;
    private static final int PLAY_VIBRATE_DELAY_DURATION = 40;
    private static final float POINT_ZERO_ONE = 0.01f;
    private static final int SELECTOR_ADJUSTMENT_DURATION_MILLIS = 300;
    public static final int SELECTOR_INDEX_IGNORE = Integer.MIN_VALUE;
    private static final int SELECTOR_ITEM_DRAW_OUTSIDE_CACHE_NUMBER = 1;
    private static final int SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT = 8;
    private static final int SELECTOR_WHEEL_ITEM_COUNT_DEFAULT = 5;
    private static final int SIZE_UNSPECIFIED = -1;
    private static final int SNAP_SCROLL_DURATION = 300;
    private static final int STACK_DEPTH = 30;
    private static final String TAG = "COUINumberPicker";
    private static final float TOP_AND_BOTTOM_FADING_EDGE_STRENGTH = 0.9f;
    private static final float VALUE_SIXTY = 60.0f;
    private static final float VELOCITY_SPEED_UP_RATIO = 1.8f;
    public static final int VIBRATE_LEVEL_CRISP = 0;
    public static final int VIBRATE_LEVEL_SOFT = 1;
    private final int MAX_SCROLL_OFFSET;
    private AccessibilityManager mAccessibilityManager;
    private AccessibilityNodeProviderImpl mAccessibilityNodeProvider;
    private final Scroller mAdjustScroller;
    private int mAlignPosition;
    private int mAlphaEnd;
    private int mAlphaStart;
    private int mBackgroundColor;
    private int mBackgroundDividerHeight;
    private int mBackgroundLeft;
    private Paint mBackgroundPaint;
    private int mBackgroundRadius;
    private int mBlueEnd;
    private int mBlueStart;
    private int mBottomSelectionDividerBottom;
    private int mCalculateCount;
    private ChangeCurrentByOneFromLongPressCommand mChangeCurrentByOneFromLongPressCommand;
    private int mClickSoundId;
    private boolean mCurrentLanguageTooLong;
    private int mCurrentScrollOffset;
    private int mDebugY;
    private boolean mDecrementVirtualButtonPressed;
    private int mDeltaMoveY;
    private float mDiffusion;
    private String[] mDisplayedValues;
    private int mDrawItemOffsetY;
    private boolean mEnableAdaptiveVibrator;
    private final float mFlingFriction;
    private final Scroller mFlingScroller;
    int mFocusTextColor;
    private int mFocusTextSize;
    private Formatter mFormatter;
    private int mGradientPositionBottom;
    private int mGradientPositionTop;
    private int mGreenEnd;
    private int mGreenStart;
    private Handler mHandler;
    private boolean mHasBackground;
    private boolean mHasMotorVibrator;
    private boolean mIgnorable;
    private float mIgnoreBarHeight;
    private float mIgnoreBarSpacing;
    private float mIgnoreBarWidth;
    private boolean mIncrementVirtualButtonPressed;
    private int mInitTextMargin;
    private long mLastDownEventTime;
    private float mLastDownEventY;
    private float mLastDownOrMoveEventY;
    private int mLastHandledDownDpadKeyCode;
    private int mLastHoveredChildVirtualViewId;
    private Object mLinearMotorVibrator;
    private long mLongPressUpdateInterval;
    private final int mMaxHeight;
    private int mMaxValue;
    private int mMaxViewWidth;
    private int mMaxWidth;
    private int mMaximumFlingVelocity;
    private final Paint mMeasureTextSelectorPaint;
    private final int mMinHeight;
    private int mMinValue;
    private final int mMinWidth;
    private int mMinimumFlingVelocity;
    private float mNormalTextBottom;
    int mNormalTextColor;
    private int mNormalTextSize;
    private float mNormalTextTop;
    private int mNumberPickerPaddingLeft;
    private int mNumberPickerPaddingRight;
    private OnScrollListener mOnScrollListener;
    private OnScrollingStopListener mOnScrollingStopListener;
    private OnValueChangeListener mOnValueChangeListener;
    private boolean mPerformClickOnTap;
    private final float mPhysicalCoeff;
    private int mPickerOffset;
    private final float mPpi;
    private final PressedStateHelper mPressedStateHelper;
    private int mPreviousScrollerY;
    private int mPreviousTime;
    private int mRedEnd;
    private int mRedStart;
    int mRefreshStyle;
    private int mScrollState;
    private int mScrollerVelocity;
    private int mSelectedValueWidth;
    private int mSelectorElementHeight;
    private final SparseArray<String> mSelectorIndexToStringCache;
    private int[] mSelectorIndices;
    private int mSelectorItemCount;
    private int mSelectorMiddleItemIndex;
    private int mSelectorTextGapHeight;
    private final Paint mSelectorWheelPaint;
    private COUISoundLoadUtil mSoundUtil;
    private long mStartCalculateTime;
    private String mTalkbackSuffix;
    private int mTextMargin;
    private int mTopSelectionDividerTop;
    private int mTouchEffectInterval;
    private NumberPickerHandlerThread mTouchEffectThread;
    private int mTouchSlop;
    private TwoDigitFormatter mTwoDigitFormatter;
    private int mUnitMargin;
    private int mUnitMarginBottom;
    private int mUnitMinWidth;
    private String mUnitText;
    private final Paint mUnitTextPaint;
    private int mUnitTextSize;
    private int mValue;
    private VelocityTracker mVelocityTracker;
    private int mVelocityY;
    private boolean mVerticalFadingEdgeEnable;
    private float mVibrateIntensity;
    private int mVibrateLevel;
    private int mVisualWidth;
    private boolean mWrapSelectorWheel;
    private boolean mWrapSelectorWheelPreferred;
    private static final PathInterpolator FLING_INTERPOLATOR = new PathInterpolator(0.0f, 0.0f, 0.4f, 1.0f);
    private static final PathInterpolator SLOW_FLING_INTERPOLATOR = new PathInterpolator(0.0f, 0.23f, 0.1f, 1.0f);
    private static final float DECELERATION_RATE = (float) (Math.log(0.78d) / Math.log(0.9d));

    public class AccessibilityNodeProviderImpl extends AccessibilityNodeProvider {
        private static final int UNDEFINED = Integer.MIN_VALUE;
        private static final int VIRTUAL_VIEW_ID = 0;
        private static final int VIRTUAL_VIEW_ID_DECREMENT = 3;
        private static final int VIRTUAL_VIEW_ID_INCREMENT = 1;
        private static final int VIRTUAL_VIEW_ID_INPUT = 2;
        private final Rect mTempRect = new Rect();
        private final int[] mTempArray = new int[2];
        private int mAccessibilityFocusedView = Integer.MIN_VALUE;

        public AccessibilityNodeProviderImpl() {
        }

        private AccessibilityNodeInfo createAccessibilityNodeInfoForNumberPicker(String str, int i3, int i4, int i5, int i6) {
            AccessibilityNodeInfo accessibilityNodeInfoObtain = AccessibilityNodeInfo.obtain();
            accessibilityNodeInfoObtain.setClassName(COUINumberPicker.class.getName());
            accessibilityNodeInfoObtain.setPackageName(COUINumberPicker.this.getContext().getPackageName());
            accessibilityNodeInfoObtain.setSource(COUINumberPicker.this);
            if (!TextUtils.isEmpty(COUINumberPicker.access$1500(COUINumberPicker.this))) {
                str = str + COUINumberPicker.access$1500(COUINumberPicker.this);
            }
            accessibilityNodeInfoObtain.setText(str);
            accessibilityNodeInfoObtain.setStateDescription(str);
            accessibilityNodeInfoObtain.setParent((View) COUINumberPicker.this.getParentForAccessibility());
            accessibilityNodeInfoObtain.setEnabled(COUINumberPicker.this.isEnabled());
            accessibilityNodeInfoObtain.setScrollable(true);
            accessibilityNodeInfoObtain.setFocusable(true);
            accessibilityNodeInfoObtain.setAccessibilityFocused(this.mAccessibilityFocusedView == -1);
            Rect rect = this.mTempRect;
            rect.set(i3, i4, i5, i6);
            accessibilityNodeInfoObtain.setBoundsInParent(rect);
            accessibilityNodeInfoObtain.setVisibleToUser(COUINumberPicker.access$1700(COUINumberPicker.this, null));
            int[] iArr = this.mTempArray;
            COUINumberPicker.this.getLocationOnScreen(iArr);
            rect.offset(iArr[0], iArr[1]);
            accessibilityNodeInfoObtain.setBoundsInScreen(rect);
            if (this.mAccessibilityFocusedView != -1) {
                accessibilityNodeInfoObtain.addAction(64);
            }
            if (this.mAccessibilityFocusedView == -1) {
                accessibilityNodeInfoObtain.addAction(128);
            }
            if (COUINumberPicker.this.isEnabled()) {
                accessibilityNodeInfoObtain.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS);
                accessibilityNodeInfoObtain.setRangeInfo(AccessibilityNodeInfo.RangeInfo.obtain(1, COUINumberPicker.this.getMinValue() - 1, COUINumberPicker.this.getMaxValue() + 1, COUINumberPicker.this.getValue()));
                AccessibilityNodeInfoCompat.wrap(accessibilityNodeInfoObtain).setRoleDescription(COUINumberPicker.this.getContext().getResources().getString(R.string.time_picker));
                if (COUINumberPicker.this.getWrapSelectorWheel() || COUINumberPicker.this.getValue() < COUINumberPicker.this.getMaxValue()) {
                    accessibilityNodeInfoObtain.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD);
                    accessibilityNodeInfoObtain.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_DOWN);
                }
                if (COUINumberPicker.this.getWrapSelectorWheel() || COUINumberPicker.this.getValue() > COUINumberPicker.this.getMinValue()) {
                    accessibilityNodeInfoObtain.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_BACKWARD);
                    accessibilityNodeInfoObtain.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_UP);
                }
            }
            return accessibilityNodeInfoObtain;
        }

        private void findAccessibilityNodeInfosByTextInChild(String str, int i3, List<AccessibilityNodeInfo> list) {
            if (i3 == 1) {
                String virtualText = getVirtualText(COUINumberPicker.access$700(COUINumberPicker.this) + 1);
                if (TextUtils.isEmpty(virtualText) || !virtualText.toString().toLowerCase().contains(str)) {
                    return;
                }
                list.add(createAccessibilityNodeInfo(1));
                return;
            }
            if (i3 != 3) {
                return;
            }
            String virtualText2 = getVirtualText(COUINumberPicker.access$700(COUINumberPicker.this) - 1);
            if (TextUtils.isEmpty(virtualText2) || !virtualText2.toString().toLowerCase().contains(str)) {
                return;
            }
            list.add(createAccessibilityNodeInfo(3));
        }

        private String getVirtualText(int i3) {
            if (COUINumberPicker.access$900(COUINumberPicker.this)) {
                i3 = COUINumberPicker.access$1000(COUINumberPicker.this, i3);
            }
            if (i3 > COUINumberPicker.access$1100(COUINumberPicker.this) || i3 < COUINumberPicker.access$1200(COUINumberPicker.this)) {
                return null;
            }
            return COUINumberPicker.access$1300(COUINumberPicker.this) == null ? COUINumberPicker.access$1400(COUINumberPicker.this, i3) : COUINumberPicker.access$1300(COUINumberPicker.this)[i3 - COUINumberPicker.access$1200(COUINumberPicker.this)];
        }

        /* JADX WARN: Type inference fix 'apply assigned field type' failed
        java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
        	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
        	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
        	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        private void sendAccessibilityEventForVirtualText(int i3, String str) {
            if (!TextUtils.isEmpty(COUINumberPicker.access$1500(COUINumberPicker.this))) {
                str = str + COUINumberPicker.access$1500(COUINumberPicker.this);
            }
            if (COUINumberPicker.access$1600(COUINumberPicker.this).isEnabled()) {
                AccessibilityEvent accessibilityEventObtain = AccessibilityEvent.obtain(i3);
                accessibilityEventObtain.setPackageName(COUINumberPicker.this.getContext().getPackageName());
                accessibilityEventObtain.getText().add(str);
                accessibilityEventObtain.setEnabled(COUINumberPicker.this.isEnabled());
                accessibilityEventObtain.setSource(COUINumberPicker.this, -1);
                COUINumberPicker cOUINumberPicker = COUINumberPicker.this;
                cOUINumberPicker.requestSendAccessibilityEvent(cOUINumberPicker, accessibilityEventObtain);
            }
        }

        @Override // android.view.accessibility.AccessibilityNodeProvider
        public AccessibilityNodeInfo createAccessibilityNodeInfo(int i3) {
            return i3 != -1 ? super.createAccessibilityNodeInfo(i3) : createAccessibilityNodeInfoForNumberPicker(getVirtualText(COUINumberPicker.access$700(COUINumberPicker.this)), COUINumberPicker.this.getScrollX(), COUINumberPicker.this.getScrollY(), COUINumberPicker.this.getScrollX() + (COUINumberPicker.this.getRight() - COUINumberPicker.this.getLeft()), COUINumberPicker.this.getScrollY() + (COUINumberPicker.this.getBottom() - COUINumberPicker.this.getTop()));
        }

        @Override // android.view.accessibility.AccessibilityNodeProvider
        public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String str, int i3) {
            if (TextUtils.isEmpty(str)) {
                return Collections.EMPTY_LIST;
            }
            String lowerCase = str.toLowerCase();
            ArrayList arrayList = new ArrayList();
            if (i3 == -1) {
                findAccessibilityNodeInfosByTextInChild(lowerCase, 3, arrayList);
                findAccessibilityNodeInfosByTextInChild(lowerCase, 2, arrayList);
                findAccessibilityNodeInfosByTextInChild(lowerCase, 1, arrayList);
                return arrayList;
            }
            if (i3 != 1 && i3 != 2 && i3 != 3) {
                return super.findAccessibilityNodeInfosByText(str, i3);
            }
            findAccessibilityNodeInfosByTextInChild(lowerCase, i3, arrayList);
            return arrayList;
        }

        @Override // android.view.accessibility.AccessibilityNodeProvider
        public boolean performAction(int i3, int i4, Bundle bundle) {
            if (i3 == -1) {
                if (i4 == 64) {
                    if (this.mAccessibilityFocusedView == i3) {
                        return false;
                    }
                    this.mAccessibilityFocusedView = i3;
                    COUINumberPicker.this.sendAccessibilityEvent(32768);
                    return true;
                }
                if (i4 == 128) {
                    if (this.mAccessibilityFocusedView != i3) {
                        return false;
                    }
                    this.mAccessibilityFocusedView = Integer.MIN_VALUE;
                    COUINumberPicker.this.sendAccessibilityEvent(65536);
                    return true;
                }
                if (i4 == 4096) {
                    if (!COUINumberPicker.this.isEnabled()) {
                        return false;
                    }
                    COUINumberPicker.access$800(COUINumberPicker.this, 2);
                    COUINumberPicker.access$500(COUINumberPicker.this, true);
                    return true;
                }
                if (i4 == 8192) {
                    if (!COUINumberPicker.this.isEnabled()) {
                        return false;
                    }
                    COUINumberPicker.access$800(COUINumberPicker.this, 2);
                    COUINumberPicker.access$500(COUINumberPicker.this, false);
                    return true;
                }
            }
            return super.performAction(i3, i4, bundle);
        }

        public void sendAccessibilityEventForVirtualView(int i3, int i4) {
            if (i3 != -1) {
                return;
            }
            sendAccessibilityEventForVirtualText(i4, getVirtualText(COUINumberPicker.access$700(COUINumberPicker.this)));
        }
    }

    public class ChangeCurrentByOneFromLongPressCommand implements Runnable {
        private boolean mIncrement;

        public ChangeCurrentByOneFromLongPressCommand() {
        }

        public static /* synthetic */ void access$000(ChangeCurrentByOneFromLongPressCommand changeCurrentByOneFromLongPressCommand, boolean z2) {
            changeCurrentByOneFromLongPressCommand.setStep(z2);
        }

        private void setStep(boolean z2) {
            this.mIncrement = z2;
        }

        @Override // java.lang.Runnable
        public void run() {
            COUINumberPicker.access$500(COUINumberPicker.this, this.mIncrement);
            COUINumberPicker cOUINumberPicker = COUINumberPicker.this;
            cOUINumberPicker.postDelayed(this, COUINumberPicker.access$600(cOUINumberPicker));
        }
    }

    public interface Formatter {
        String format(int i3);
    }

    public interface OnScrollListener {
        public static final int SCROLL_STATE_FLING = 2;
        public static final int SCROLL_STATE_IDLE = 0;
        public static final int SCROLL_STATE_TOUCH_SCROLL = 1;

        void onScrollStateChange(COUINumberPicker cOUINumberPicker, int i3);
    }

    public interface OnScrollingStopListener {
        void onScrollingStop();
    }

    public interface OnValueChangeListener {
        void onValueChange(COUINumberPicker cOUINumberPicker, int i3, int i4);
    }

    public class PressedStateHelper implements Runnable {
        public static final int BUTTON_DECREMENT = 2;
        public static final int BUTTON_INCREMENT = 1;
        private final int MODE_PRESS = 1;
        private final int MODE_TAPPED = 2;
        private int mManagedButton;
        private int mMode;

        public PressedStateHelper() {
        }

        public void buttonPressDelayed(int i3) {
            cancel();
            this.mMode = 1;
            this.mManagedButton = i3;
            COUINumberPicker.this.postDelayed(this, ViewConfiguration.getTapTimeout());
        }

        public void buttonTapped(int i3) {
            cancel();
            this.mMode = 2;
            this.mManagedButton = i3;
            COUINumberPicker.this.post(this);
        }

        public void cancel() {
            this.mMode = 0;
            this.mManagedButton = 0;
            COUINumberPicker.this.removeCallbacks(this);
            if (COUINumberPicker.access$100(COUINumberPicker.this)) {
                COUINumberPicker.access$102(COUINumberPicker.this, false);
                COUINumberPicker cOUINumberPicker = COUINumberPicker.this;
                cOUINumberPicker.invalidate(0, COUINumberPicker.access$200(cOUINumberPicker), COUINumberPicker.this.getRight(), COUINumberPicker.this.getBottom());
            }
            COUINumberPicker.access$302(COUINumberPicker.this, false);
            if (COUINumberPicker.access$300(COUINumberPicker.this)) {
                COUINumberPicker cOUINumberPicker2 = COUINumberPicker.this;
                cOUINumberPicker2.invalidate(0, 0, cOUINumberPicker2.getRight(), COUINumberPicker.access$400(COUINumberPicker.this));
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            int i3 = this.mMode;
            if (i3 == 1) {
                int i4 = this.mManagedButton;
                if (i4 == 1) {
                    COUINumberPicker.access$102(COUINumberPicker.this, true);
                    COUINumberPicker cOUINumberPicker = COUINumberPicker.this;
                    cOUINumberPicker.invalidate(0, COUINumberPicker.access$200(cOUINumberPicker), COUINumberPicker.this.getRight(), COUINumberPicker.this.getBottom());
                    return;
                } else {
                    if (i4 != 2) {
                        return;
                    }
                    COUINumberPicker.access$302(COUINumberPicker.this, true);
                    COUINumberPicker cOUINumberPicker2 = COUINumberPicker.this;
                    cOUINumberPicker2.invalidate(0, 0, cOUINumberPicker2.getRight(), COUINumberPicker.access$400(COUINumberPicker.this));
                    return;
                }
            }
            if (i3 != 2) {
                return;
            }
            int i5 = this.mManagedButton;
            if (i5 == 1) {
                if (!COUINumberPicker.access$100(COUINumberPicker.this)) {
                    COUINumberPicker.this.postDelayed(this, ViewConfiguration.getPressedStateDuration());
                }
                COUINumberPicker.access$180(COUINumberPicker.this, 1);
                COUINumberPicker cOUINumberPicker3 = COUINumberPicker.this;
                cOUINumberPicker3.invalidate(0, COUINumberPicker.access$200(cOUINumberPicker3), COUINumberPicker.this.getRight(), COUINumberPicker.this.getBottom());
                return;
            }
            if (i5 != 2) {
                return;
            }
            if (!COUINumberPicker.access$300(COUINumberPicker.this)) {
                COUINumberPicker.this.postDelayed(this, ViewConfiguration.getPressedStateDuration());
            }
            COUINumberPicker.access$380(COUINumberPicker.this, 1);
            COUINumberPicker cOUINumberPicker4 = COUINumberPicker.this;
            cOUINumberPicker4.invalidate(0, 0, cOUINumberPicker4.getRight(), COUINumberPicker.access$400(COUINumberPicker.this));
        }
    }

    public class TouchEffectHandler extends Handler {
        public TouchEffectHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 0) {
                COUINumberPicker.access$1800(COUINumberPicker.this);
            }
            super.handleMessage(message);
        }
    }

    public class TwoDigitFormatter implements Formatter {
        DecimalFormat mDecimalFormat;
        java.util.Formatter mJavaFormatter;
        final StringBuilder mBuilder = new StringBuilder();
        final Object[] mArgs = new Object[1];

        public TwoDigitFormatter() {
            init(Locale.getDefault());
        }

        private void init(Locale locale) {
            this.mJavaFormatter = new java.util.Formatter(this.mBuilder, locale);
            this.mDecimalFormat = new DecimalFormat(BreathingLightUtils.KEY_BREATHING_LIGHT_DEFAULT_TIME_START_MINUTE);
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.Formatter
        public String format(int i3) {
            this.mArgs[0] = Integer.valueOf(i3);
            StringBuilder sb = this.mBuilder;
            sb.delete(0, sb.length());
            return this.mDecimalFormat.format(i3);
        }
    }

    public COUINumberPicker(Context context) {
        this(context, null);
    }

    public static /* synthetic */ boolean access$100(COUINumberPicker cOUINumberPicker) {
        return cOUINumberPicker.mIncrementVirtualButtonPressed;
    }

    public static /* synthetic */ int access$1000(COUINumberPicker cOUINumberPicker, int i3) {
        return cOUINumberPicker.getWrappedSelectorIndex(i3);
    }

    public static /* synthetic */ boolean access$102(COUINumberPicker cOUINumberPicker, boolean z2) {
        cOUINumberPicker.mIncrementVirtualButtonPressed = z2;
        return z2;
    }

    public static /* synthetic */ int access$1100(COUINumberPicker cOUINumberPicker) {
        return cOUINumberPicker.mMaxValue;
    }

    public static /* synthetic */ int access$1200(COUINumberPicker cOUINumberPicker) {
        return cOUINumberPicker.mMinValue;
    }

    public static /* synthetic */ String[] access$1300(COUINumberPicker cOUINumberPicker) {
        return cOUINumberPicker.mDisplayedValues;
    }

    public static /* synthetic */ String access$1400(COUINumberPicker cOUINumberPicker, int i3) {
        return cOUINumberPicker.formatNumber(i3);
    }

    public static /* synthetic */ String access$1500(COUINumberPicker cOUINumberPicker) {
        return cOUINumberPicker.mTalkbackSuffix;
    }

    public static /* synthetic */ AccessibilityManager access$1600(COUINumberPicker cOUINumberPicker) {
        return cOUINumberPicker.mAccessibilityManager;
    }

    public static /* synthetic */ boolean access$1700(COUINumberPicker cOUINumberPicker, Rect rect) {
        return cOUINumberPicker.isVisibleToUserRef(rect);
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [boolean, byte] */
    public static /* synthetic */ boolean access$180(COUINumberPicker cOUINumberPicker, int i3) {
        ?? r2 = (i3 ^ (cOUINumberPicker.mIncrementVirtualButtonPressed ? 1 : 0)) == true ? (byte) 1 : (byte) 0;
        cOUINumberPicker.mIncrementVirtualButtonPressed = r2;
        return r2;
    }

    public static /* synthetic */ void access$1800(COUINumberPicker cOUINumberPicker) {
        cOUINumberPicker.playSoundEffect();
    }

    public static /* synthetic */ int access$200(COUINumberPicker cOUINumberPicker) {
        return cOUINumberPicker.mBottomSelectionDividerBottom;
    }

    public static /* synthetic */ boolean access$300(COUINumberPicker cOUINumberPicker) {
        return cOUINumberPicker.mDecrementVirtualButtonPressed;
    }

    public static /* synthetic */ boolean access$302(COUINumberPicker cOUINumberPicker, boolean z2) {
        cOUINumberPicker.mDecrementVirtualButtonPressed = z2;
        return z2;
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [boolean, byte] */
    public static /* synthetic */ boolean access$380(COUINumberPicker cOUINumberPicker, int i3) {
        ?? r2 = (i3 ^ (cOUINumberPicker.mDecrementVirtualButtonPressed ? 1 : 0)) == true ? (byte) 1 : (byte) 0;
        cOUINumberPicker.mDecrementVirtualButtonPressed = r2;
        return r2;
    }

    public static /* synthetic */ int access$400(COUINumberPicker cOUINumberPicker) {
        return cOUINumberPicker.mTopSelectionDividerTop;
    }

    public static /* synthetic */ void access$500(COUINumberPicker cOUINumberPicker, boolean z2) {
        cOUINumberPicker.changeValueByOne(z2);
    }

    public static /* synthetic */ long access$600(COUINumberPicker cOUINumberPicker) {
        return cOUINumberPicker.mLongPressUpdateInterval;
    }

    public static /* synthetic */ int access$700(COUINumberPicker cOUINumberPicker) {
        return cOUINumberPicker.mValue;
    }

    public static /* synthetic */ void access$800(COUINumberPicker cOUINumberPicker, int i3) {
        cOUINumberPicker.onScrollStateChange(i3);
    }

    public static /* synthetic */ boolean access$900(COUINumberPicker cOUINumberPicker) {
        return cOUINumberPicker.mWrapSelectorWheel;
    }

    private void changeValueByOne(boolean z2) {
        if (!moveToFinalScrollerPosition(this.mFlingScroller)) {
            moveToFinalScrollerPosition(this.mAdjustScroller);
        }
        this.mPreviousScrollerY = 0;
        if (z2) {
            this.mFlingScroller.startScroll(0, 0, 0, (int) ((-this.mSelectorElementHeight) - this.mDiffusion), 300);
        } else {
            this.mFlingScroller.startScroll(0, 0, 0, (int) (this.mSelectorElementHeight + this.mDiffusion), 300);
        }
        invalidate();
    }

    private float computeDeceleration(float f3) {
        return this.mPpi * 386.0878f * f3;
    }

    private void decrementSelectorIndices(int[] iArr) {
        for (int i3 = 0; i3 < iArr.length; i3++) {
            iArr[i3] = getWrappedSelectorIndex(iArr[i3], -1);
        }
        ensureCachedScrollSelectorValue(iArr[0]);
    }

    private void ensureCachedScrollSelectorValue(int i3) {
        String number;
        SparseArray<String> sparseArray = this.mSelectorIndexToStringCache;
        if (sparseArray.get(i3) != null) {
            return;
        }
        int i4 = this.mMinValue;
        if (i3 < i4 || i3 > this.mMaxValue) {
            number = "";
        } else {
            String[] strArr = this.mDisplayedValues;
            number = strArr != null ? strArr[i3 - i4] : formatNumber(i3);
        }
        sparseArray.put(i3, number);
    }

    private boolean ensureScrollWheelAdjusted() {
        int i3 = -this.mCurrentScrollOffset;
        if (i3 == 0) {
            return false;
        }
        this.mPreviousScrollerY = 0;
        getSplineFlingDistance(this.mVelocityY);
        Math.signum(this.mVelocityY);
        getSplineFlingDuration(this.mVelocityY);
        float fAbs = Math.abs(i3);
        int i4 = this.mSelectorElementHeight;
        float f3 = this.mDiffusion;
        if (fAbs > (i4 + f3) / 2.0f) {
            i3 = (int) (i3 + (i3 > 0 ? (-i4) - f3 : i4 + f3));
        }
        this.mAdjustScroller.startScroll(0, 0, 0, i3, 300);
        invalidate();
        return true;
    }

    private void fling(int i3) {
        this.mVelocityY = i3;
        this.mPreviousScrollerY = 0;
        float f3 = i3;
        double splineFlingDistance = getSplineFlingDistance(f3);
        int i4 = this.mSelectorElementHeight;
        float f4 = this.mDiffusion;
        double d3 = splineFlingDistance > ((double) (((float) i4) + f4)) ? splineFlingDistance - (splineFlingDistance % ((double) (i4 + f4))) : splineFlingDistance % ((double) (i4 + f4));
        int i5 = this.mDeltaMoveY;
        double d4 = d3 + ((double) i5);
        this.mFlingScroller.startScroll(0, 0, 0, (int) (i3 < 0 ? -(d4 + ((double) ((this.mCurrentScrollOffset - i5) % (i4 + f4)))) : d4 - ((double) ((this.mCurrentScrollOffset + i5) % (i4 + f4)))), (int) (getSplineFlingDuration(f3) * 1.5f));
        invalidate();
    }

    private String formatNumber(int i3) {
        Formatter formatter = this.mFormatter;
        return formatter != null ? formatter.format(i3) : formatNumberWithLocale(i3);
    }

    private static String formatNumberWithLocale(int i3) {
        return String.format(Locale.getDefault(), TimeModel.NUMBER_FORMAT, Integer.valueOf(i3));
    }

    private String getCaller(StackTraceElement[] stackTraceElementArr, int i3) {
        int i4 = i3 + 4;
        if (i4 >= stackTraceElementArr.length) {
            return "<bottom of call stack>";
        }
        StackTraceElement stackTraceElement = stackTraceElementArr[i4];
        return stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + SmartKeyConstantUtils.TIMER_SEPARATOR + stackTraceElement.getLineNumber();
    }

    private String getCallers(int i3) {
        boolean z2;
        try {
            z2 = SystemProperties.getBoolean(Constant.LOG_PROPERTY, false);
        } catch (Error | Exception unused) {
            z2 = false;
        }
        if (!z2) {
            return "";
        }
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i4 = 0; i4 < i3; i4++) {
            stringBuffer.append(getCaller(stackTrace, i4));
            stringBuffer.append(" ");
        }
        return stringBuffer.toString();
    }

    private float getDampRatio() {
        return Math.min(VELOCITY_SPEED_UP_RATIO, 1.6f);
    }

    private int getGradientCoeff(int i3) {
        return Math.abs(i3 - (this.mSelectorMiddleItemIndex * this.mSelectorElementHeight)) / this.mSelectorElementHeight;
    }

    private double getSplineDeceleration(float f3) {
        return Math.log((Math.abs(f3) * 0.35f) / (this.mFlingFriction * this.mPhysicalCoeff));
    }

    private double getSplineFlingDistance(float f3) {
        double splineDeceleration = getSplineDeceleration(f3);
        float f4 = DECELERATION_RATE;
        return ((double) (this.mFlingFriction * this.mPhysicalCoeff)) * Math.exp((((double) f4) / (((double) f4) - 1.0d)) * splineDeceleration);
    }

    private int getSplineFlingDuration(float f3) {
        return (int) (Math.exp(getSplineDeceleration(f3) / (((double) DECELERATION_RATE) - 1.0d)) * 1000.0d);
    }

    private int getWrappedSelectorIndex(int i3) {
        return getWrappedSelectorIndex(i3, 0);
    }

    private int gradualChange(int i3, int i4, float f3) {
        return i4 - ((int) (((i4 - i3) * 2) * f3));
    }

    private float gradualChangeTextSize(int i3, int i4, int i5, int i6, int i7) {
        float f3;
        float f4;
        float f5;
        float f6;
        int i8 = this.mSelectorMiddleItemIndex - 1;
        int i9 = this.mSelectorElementHeight;
        int i10 = i8 * i9;
        int length = (this.mSelectorIndices.length - 3) * i9;
        double d3 = i7;
        double d4 = i10;
        if (d3 > d4 - (((double) i9) * 0.5d)) {
            f3 = 2.0f;
            if (d3 < d4 + (((double) i9) * 0.5d)) {
                return i4 - ((((i4 - i3) * 2.0f) * Math.abs(i7 - i10)) / this.mSelectorElementHeight);
            }
        } else {
            f3 = 2.0f;
        }
        if (i7 <= i10 - i9) {
            f4 = i5;
            f5 = (i6 - i5) * 1.0f;
            f6 = i7;
        } else {
            if (i7 < i10 + i9) {
                return i6;
            }
            f4 = i5;
            f5 = (i6 - i5) * 1.0f;
            f6 = length - i7;
        }
        return f4 + (((f5 * f6) / i9) / f3);
    }

    private void incrementSelectorIndices(int[] iArr) {
        for (int i3 = 0; i3 < iArr.length; i3++) {
            iArr[i3] = getWrappedSelectorIndex(iArr[i3], 1);
        }
        ensureCachedScrollSelectorValue(iArr[iArr.length - 1]);
    }

    private void initColorGradientRes() {
        int i3 = this.mSelectorElementHeight;
        int i4 = this.mSelectorMiddleItemIndex;
        this.mGradientPositionTop = (int) (((double) i3) * (((double) i4) - 0.5d));
        this.mGradientPositionBottom = (int) (((double) i3) * (((double) i4) + 0.5d));
    }

    private void initOrResetVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void initializeFadingEdges() {
        setVerticalFadingEdgeEnabled(this.mVerticalFadingEdgeEnable);
        setFadingEdgeLength(((getBottom() - getTop()) - this.mNormalTextSize) / 2);
    }

    private void initializeSelectorWheel() {
        initializeSelectorWheelIndices();
        int[] iArr = this.mSelectorIndices;
        int iMax = (int) ((Math.max(0, ((getBottom() - getTop()) - ((iArr.length - 2) * this.mNormalTextSize)) - this.mPickerOffset) / (iArr.length - 2)) + 0.5f);
        this.mSelectorTextGapHeight = iMax;
        this.mSelectorElementHeight = this.mNormalTextSize + iMax;
        this.mCurrentScrollOffset = 0;
        this.mTopSelectionDividerTop = (getHeight() / 2) - (this.mSelectorElementHeight / 2);
        this.mBottomSelectionDividerBottom = (getHeight() / 2) + (this.mSelectorElementHeight / 2);
    }

    private void initializeSelectorWheelIndices() {
        this.mSelectorIndexToStringCache.clear();
        int[] iArr = this.mSelectorIndices;
        int value = getValue();
        for (int i3 = 0; i3 < this.mSelectorIndices.length; i3++) {
            int i4 = i3 - this.mSelectorMiddleItemIndex;
            int wrappedSelectorIndex = this.mIgnorable ? getWrappedSelectorIndex(value, i4) : i4 + value;
            if (this.mWrapSelectorWheel) {
                wrappedSelectorIndex = getWrappedSelectorIndex(wrappedSelectorIndex);
            }
            iArr[i3] = wrappedSelectorIndex;
            ensureCachedScrollSelectorValue(wrappedSelectorIndex);
        }
    }

    private boolean isVisibleToUserRef(Rect rect) {
        try {
            Method declaredMethod = View.class.getDeclaredMethod("isVisibleToUser", Rect.class);
            declaredMethod.setAccessible(true);
            return ((Boolean) declaredMethod.invoke(this, rect)).booleanValue();
        } catch (Exception e3) {
            Log.e(TAG, "isUserVisible: error=" + e3.getMessage());
            return false;
        }
    }

    private int makeMeasureSpec(int i3, int i4) {
        if (i4 != -1) {
            int size = View.MeasureSpec.getSize(i3);
            int mode = View.MeasureSpec.getMode(i3);
            if (mode == Integer.MIN_VALUE) {
                String str = this.mUnitText;
                if (str != null) {
                    float fMeasureText = this.mUnitTextPaint.measureText(str);
                    int iMeasureText = this.mUnitMinWidth;
                    if (fMeasureText > iMeasureText) {
                        iMeasureText = (int) this.mUnitTextPaint.measureText(this.mUnitText);
                    }
                    int i5 = this.mInitTextMargin;
                    size = iMeasureText + (i5 - this.mUnitMinWidth) + i5 + this.mSelectedValueWidth;
                }
                return View.MeasureSpec.makeMeasureSpec(Math.min(size, i4), 1073741824);
            }
            if (mode == 0) {
                return View.MeasureSpec.makeMeasureSpec(i4, 1073741824);
            }
            if (mode != 1073741824) {
                throw new IllegalArgumentException("Unknown measure mode: " + mode);
            }
        }
        return i3;
    }

    private boolean moveToFinalScrollerPosition(Scroller scroller) {
        scroller.forceFinished(true);
        int finalY = scroller.getFinalY() - scroller.getCurrY();
        int i3 = -((this.mCurrentScrollOffset + finalY) % this.mSelectorElementHeight);
        if (i3 == 0) {
            return false;
        }
        int iAbs = Math.abs(i3);
        int i4 = this.mSelectorElementHeight;
        if (iAbs > i4 / 2) {
            i3 = i3 > 0 ? i3 - i4 : i3 + i4;
        }
        scrollBy(0, finalY + i3);
        return true;
    }

    private void notifyChange(int i3, int i4) {
        OnValueChangeListener onValueChangeListener = this.mOnValueChangeListener;
        if (onValueChangeListener != null) {
            onValueChangeListener.onValueChange(this, i3, this.mValue);
        }
    }

    private void onScrollStateChange(int i3) {
        if (this.mScrollState == i3) {
            return;
        }
        this.mScrollState = i3;
        OnScrollListener onScrollListener = this.mOnScrollListener;
        if (onScrollListener != null) {
            onScrollListener.onScrollStateChange(this, i3);
        }
        if (this.mScrollState == 0) {
            String str = this.mSelectorIndexToStringCache.get(getValue());
            if (!TextUtils.isEmpty(this.mTalkbackSuffix)) {
                str = str + this.mTalkbackSuffix;
            }
            announceForAccessibility(str);
            OnScrollingStopListener onScrollingStopListener = this.mOnScrollingStopListener;
            if (onScrollingStopListener != null) {
                onScrollingStopListener.onScrollingStop();
            }
        }
    }

    private void onScrollerFinished(Scroller scroller) {
        if (scroller == this.mFlingScroller) {
            ensureScrollWheelAdjusted();
            onScrollStateChange(0);
        }
    }

    private boolean performAdaptiveFeedback() {
        int iAbs;
        if (this.mLinearMotorVibrator == null) {
            LinearmotorVibrator linearMotorVibrator = VibrateUtils.getLinearMotorVibrator(getContext());
            this.mLinearMotorVibrator = linearMotorVibrator;
            this.mHasMotorVibrator = linearMotorVibrator != null;
        }
        if (this.mLinearMotorVibrator == null) {
            return false;
        }
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.computeCurrentVelocity(1000, this.mMaximumFlingVelocity);
            iAbs = (int) Math.abs(this.mVelocityTracker.getYVelocity());
        } else {
            iAbs = Math.abs(this.mScrollerVelocity);
        }
        int i3 = iAbs;
        VibrateUtils.setLinearMotorVibratorStrength((LinearmotorVibrator) this.mLinearMotorVibrator, i3 > 2000 ? 0 : 1, i3, this.mMaximumFlingVelocity, 1200, 1600, this.mVibrateLevel, this.mVibrateIntensity);
        return true;
    }

    private void performFeedback() {
        if ((this.mHasMotorVibrator && this.mEnableAdaptiveVibrator && performAdaptiveFeedback()) || performHapticFeedback(308)) {
            return;
        }
        performHapticFeedback(302);
    }

    private void playSoundEffect() {
        this.mSoundUtil.play(getContext(), this.mClickSoundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    private void postChangeCurrentByOneFromLongPress(boolean z2, long j3) {
        ChangeCurrentByOneFromLongPressCommand changeCurrentByOneFromLongPressCommand = this.mChangeCurrentByOneFromLongPressCommand;
        if (changeCurrentByOneFromLongPressCommand == null) {
            this.mChangeCurrentByOneFromLongPressCommand = new ChangeCurrentByOneFromLongPressCommand();
        } else {
            removeCallbacks(changeCurrentByOneFromLongPressCommand);
        }
        ChangeCurrentByOneFromLongPressCommand.access$000(this.mChangeCurrentByOneFromLongPressCommand, z2);
        postDelayed(this.mChangeCurrentByOneFromLongPressCommand, j3);
    }

    private void recycleVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private void removeAllCallbacks() {
        ChangeCurrentByOneFromLongPressCommand changeCurrentByOneFromLongPressCommand = this.mChangeCurrentByOneFromLongPressCommand;
        if (changeCurrentByOneFromLongPressCommand != null) {
            removeCallbacks(changeCurrentByOneFromLongPressCommand);
        }
        this.mPressedStateHelper.cancel();
    }

    private void removeChangeCurrentByOneFromLongPress() {
        ChangeCurrentByOneFromLongPressCommand changeCurrentByOneFromLongPressCommand = this.mChangeCurrentByOneFromLongPressCommand;
        if (changeCurrentByOneFromLongPressCommand != null) {
            removeCallbacks(changeCurrentByOneFromLongPressCommand);
        }
    }

    private int resolveSizeAndStateRespectingMinSize(int i3, int i4, int i5) {
        return i3 != -1 ? View.resolveSizeAndState(Math.max(i3, i4), i5, 0) : i4;
    }

    private void setValueInternal(int i3, boolean z2) {
        if (this.mStartCalculateTime == -1) {
            this.mStartCalculateTime = System.currentTimeMillis();
            this.mCalculateCount = 0;
        } else if (System.currentTimeMillis() - this.mStartCalculateTime < 1000) {
            int i4 = this.mCalculateCount + 1;
            this.mCalculateCount = i4;
            if (i4 >= 100) {
                this.mCalculateCount = 0;
                Log.d(TAG, getCallers(30) + "\nmCurrentScrollOffset = " + this.mCurrentScrollOffset + " ,mSelectorTextGapHeight = " + this.mSelectorTextGapHeight + " ,mSelectorElementHeight = " + this.mSelectorElementHeight + " ,mSelectorMiddleItemIndex = " + this.mSelectorMiddleItemIndex + " ,mWrapSelectorWheel = " + this.mWrapSelectorWheel + " ,mDebugY = " + this.mDebugY + " ,mMinValue = " + this.mMinValue);
            }
        } else {
            this.mStartCalculateTime = -1L;
        }
        Log.d(TAG, "setValueInternal current = " + i3);
        if (this.mValue == i3) {
            initializeSelectorWheelIndices();
            return;
        }
        int wrappedSelectorIndex = this.mWrapSelectorWheel ? getWrappedSelectorIndex(i3) : Math.min(Math.max(i3, this.mMinValue), this.mMaxValue);
        int i5 = this.mValue;
        this.mValue = wrappedSelectorIndex;
        if (z2) {
            notifyChange(i5, wrappedSelectorIndex);
            performFeedback();
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.removeMessages(0);
                this.mHandler.sendEmptyMessage(0);
            } else {
                Log.d(TAG, " mHandler not init yet , To prevent ANR, do not wait when initializing the handler. ");
            }
        }
        initializeSelectorWheelIndices();
        invalidate();
    }

    private void updateWrapSelectorWheel() {
        this.mWrapSelectorWheel = this.mMaxValue - this.mMinValue >= this.mSelectorIndices.length + (-2) && this.mWrapSelectorWheelPreferred;
    }

    public void addTalkbackSuffix(String str) {
        this.mTalkbackSuffix = str;
    }

    public void clearNumberPickerPadding() {
        this.mNumberPickerPaddingLeft = 0;
        this.mNumberPickerPaddingRight = 0;
        requestLayout();
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.mFlingScroller.isFinished()) {
            if (this.mAdjustScroller.isFinished()) {
                this.mScrollerVelocity = 0;
                return;
            }
            this.mAdjustScroller.computeScrollOffset();
            int currY = this.mAdjustScroller.getCurrY();
            if (this.mPreviousScrollerY == 0) {
                this.mPreviousScrollerY = this.mAdjustScroller.getStartY();
            }
            scrollBy(0, currY - this.mPreviousScrollerY);
            this.mPreviousScrollerY = currY;
            if (this.mAdjustScroller.isFinished()) {
                onScrollStateChange(0);
                return;
            } else {
                invalidate();
                return;
            }
        }
        this.mFlingScroller.computeScrollOffset();
        int currY2 = this.mFlingScroller.getCurrY();
        if (this.mPreviousScrollerY == 0) {
            this.mPreviousScrollerY = this.mFlingScroller.getStartY();
        }
        int iUptimeMillis = (int) (SystemClock.uptimeMillis() - ((long) this.mPreviousTime));
        int iAbs = Math.abs(currY2 - this.mPreviousScrollerY);
        if (iUptimeMillis != 0) {
            this.mScrollerVelocity = Math.min(this.mMaximumFlingVelocity, (int) (((iAbs * 1.0f) / iUptimeMillis) * 1000.0f));
        }
        scrollBy(0, currY2 - this.mPreviousScrollerY);
        this.mPreviousScrollerY = currY2;
        this.mPreviousTime = (int) SystemClock.uptimeMillis();
        if (this.mFlingScroller.isFinished()) {
            onScrollerFinished(this.mFlingScroller);
        } else {
            invalidate();
        }
    }

    @Override // android.view.View
    public int computeVerticalScrollExtent() {
        return getHeight();
    }

    @Override // android.view.View
    public int computeVerticalScrollOffset() {
        return this.mCurrentScrollOffset;
    }

    @Override // android.view.View
    public int computeVerticalScrollRange() {
        return ((this.mMaxValue - this.mMinValue) + 1) * this.mSelectorElementHeight;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchHoverEvent(MotionEvent motionEvent) {
        if (!this.mAccessibilityManager.isEnabled()) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        AccessibilityNodeProviderImpl accessibilityNodeProviderImpl = (AccessibilityNodeProviderImpl) getAccessibilityNodeProvider();
        if (actionMasked != 7) {
            if (actionMasked == 9) {
                sendAccessibilityEvent(128);
                this.mLastHoveredChildVirtualViewId = -1;
                return false;
            }
            if (actionMasked != 10) {
                return false;
            }
            accessibilityNodeProviderImpl.sendAccessibilityEventForVirtualView(-1, 256);
            this.mLastHoveredChildVirtualViewId = -1;
            return false;
        }
        int i3 = this.mLastHoveredChildVirtualViewId;
        if (i3 == -1 || i3 == -1) {
            return false;
        }
        accessibilityNodeProviderImpl.sendAccessibilityEventForVirtualView(i3, 256);
        accessibilityNodeProviderImpl.sendAccessibilityEventForVirtualView(-1, 128);
        this.mLastHoveredChildVirtualViewId = -1;
        accessibilityNodeProviderImpl.performAction(-1, 64, null);
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (keyCode == 19 || keyCode == 20) {
            int action = keyEvent.getAction();
            if (action == 0) {
                if (!this.mWrapSelectorWheel) {
                    if (keyCode == 20) {
                    }
                }
                requestFocus();
                this.mLastHandledDownDpadKeyCode = keyCode;
                removeAllCallbacks();
                if (this.mFlingScroller.isFinished()) {
                    changeValueByOne(keyCode == 20);
                }
                return true;
            }
            if (action == 1 && this.mLastHandledDownDpadKeyCode == keyCode) {
                this.mLastHandledDownDpadKeyCode = -1;
                return true;
            }
        } else if (keyCode == 23 || keyCode == 66) {
            removeAllCallbacks();
        }
        return super.dispatchKeyEvent(keyEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        Log.d(TAG, "dispatchTouchEvent event = " + motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1 || actionMasked == 3) {
            removeAllCallbacks();
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTrackballEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1 || actionMasked == 3) {
            removeAllCallbacks();
        }
        return super.dispatchTrackballEvent(motionEvent);
    }

    @Override // android.view.View
    public AccessibilityNodeProvider getAccessibilityNodeProvider() {
        if (this.mAccessibilityNodeProvider == null) {
            this.mAccessibilityNodeProvider = new AccessibilityNodeProviderImpl();
        }
        return this.mAccessibilityNodeProvider;
    }

    public int getBackgroundColor() {
        return this.mBackgroundColor;
    }

    @Override // android.view.View
    public float getBottomFadingEdgeStrength() {
        return 0.9f;
    }

    public String getCurrentText() {
        return this.mSelectorIndexToStringCache.get(getValue());
    }

    public String[] getDisplayedValues() {
        return this.mDisplayedValues;
    }

    public int getMaxValue() {
        return this.mMaxValue;
    }

    public int getMinValue() {
        return this.mMinValue;
    }

    public int getNumberPickerPaddingLeft() {
        return this.mNumberPickerPaddingLeft;
    }

    public int getNumberPickerPaddingRight() {
        return this.mNumberPickerPaddingRight;
    }

    public Paint getSelectorTextPaint() {
        return this.mSelectorWheelPaint;
    }

    public float getTextSize() {
        return this.mSelectorWheelPaint.getTextSize();
    }

    @Override // android.view.View
    public float getTopFadingEdgeStrength() {
        return 0.9f;
    }

    public int getTouchEffectInterval() {
        return this.mTouchEffectInterval;
    }

    public int getValue() {
        return this.mValue;
    }

    public boolean getWrapSelectorWheel() {
        return this.mWrapSelectorWheel;
    }

    public boolean isAccessibilityEnable() {
        AccessibilityManager accessibilityManager = this.mAccessibilityManager;
        return accessibilityManager != null && accessibilityManager.isEnabled();
    }

    public boolean isIgnorable() {
        return this.mIgnorable;
    }

    public boolean isLayoutRtl() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        NumberPickerHandlerThread numberPickerHandlerThread = new NumberPickerHandlerThread("touchEffect", -16);
        this.mTouchEffectThread = numberPickerHandlerThread;
        numberPickerHandlerThread.start();
        if (this.mTouchEffectThread.getLooper() != null) {
            this.mHandler = new TouchEffectHandler(this.mTouchEffectThread.getLooper());
        }
        VibrateUtils.registerHapticObserver(getContext());
        initOrResetVelocityTracker();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeAllCallbacks();
        NumberPickerHandlerThread numberPickerHandlerThread = this.mTouchEffectThread;
        if (numberPickerHandlerThread != null) {
            numberPickerHandlerThread.quit();
            this.mTouchEffectThread = null;
        }
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        VibrateUtils.unRegisterHapticObserver();
    }

    /* JADX WARN: Type inference failed for: r8v0 */
    /* JADX WARN: Type inference failed for: r8v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r8v3 */
    @Override // android.widget.LinearLayout, android.view.View
    public void onDraw(Canvas canvas) {
        Canvas canvas2;
        float f3;
        boolean z2;
        int i3;
        COUINumberPicker cOUINumberPicker = this;
        float f4 = 2.0f;
        if (cOUINumberPicker.mHasBackground) {
            int height = (int) (((cOUINumberPicker.getHeight() / 2.0f) - cOUINumberPicker.mBackgroundRadius) - cOUINumberPicker.mDiffusion);
            canvas.drawRect(cOUINumberPicker.mBackgroundLeft, height, cOUINumberPicker.getWidth() - cOUINumberPicker.mBackgroundLeft, height + cOUINumberPicker.mBackgroundDividerHeight, cOUINumberPicker.mBackgroundPaint);
            int height2 = (int) ((cOUINumberPicker.getHeight() / 2.0f) + cOUINumberPicker.mBackgroundRadius + cOUINumberPicker.mDiffusion);
            canvas.drawRect(cOUINumberPicker.mBackgroundLeft, height2, cOUINumberPicker.getWidth() - cOUINumberPicker.mBackgroundLeft, height2 + cOUINumberPicker.mBackgroundDividerHeight, cOUINumberPicker.mBackgroundPaint);
            canvas2 = canvas;
        } else {
            canvas2 = canvas;
        }
        float right = (((cOUINumberPicker.getRight() - cOUINumberPicker.getLeft()) - cOUINumberPicker.mNumberPickerPaddingLeft) - cOUINumberPicker.mNumberPickerPaddingRight) / 2.0f;
        if (cOUINumberPicker.mUnitText != null) {
            right = cOUINumberPicker.mTextMargin + (cOUINumberPicker.mSelectedValueWidth / 2.0f);
            if (cOUINumberPicker.isLayoutRtl()) {
                right = ((cOUINumberPicker.getMeasuredWidth() - right) - cOUINumberPicker.mNumberPickerPaddingRight) - cOUINumberPicker.mNumberPickerPaddingLeft;
            }
        }
        int i4 = cOUINumberPicker.mCurrentScrollOffset;
        int i5 = cOUINumberPicker.mVisualWidth;
        ?? r8 = 1;
        char c3 = 2;
        if (i5 != -1 && i5 < cOUINumberPicker.getRight() - cOUINumberPicker.getLeft()) {
            int i6 = cOUINumberPicker.mAlignPosition;
            if (i6 == 1) {
                i3 = cOUINumberPicker.mVisualWidth / 2;
            } else if (i6 == 2) {
                int right2 = cOUINumberPicker.getRight() - cOUINumberPicker.getLeft();
                int i7 = cOUINumberPicker.mVisualWidth;
                i3 = (right2 - i7) + (i7 / 2);
            }
            right = i3;
        }
        int i8 = cOUINumberPicker.mNumberPickerPaddingLeft;
        if (i8 != 0) {
            right += i8;
        }
        float f5 = right;
        int[] iArr = cOUINumberPicker.mSelectorIndices;
        boolean z3 = false;
        int i9 = i4 - cOUINumberPicker.mSelectorElementHeight;
        float f6 = f5;
        int i10 = 0;
        float f7 = 0.0f;
        while (i10 < iArr.length) {
            int i11 = iArr[i10];
            if (i9 > cOUINumberPicker.mGradientPositionTop && i9 < cOUINumberPicker.mGradientPositionBottom) {
                float gradientCoeff = cOUINumberPicker.getGradientCoeff(i9);
                cOUINumberPicker.gradualChange(cOUINumberPicker.mAlphaStart, cOUINumberPicker.mAlphaEnd, gradientCoeff);
                cOUINumberPicker.gradualChange(cOUINumberPicker.mRedStart, cOUINumberPicker.mRedEnd, gradientCoeff);
                cOUINumberPicker.gradualChange(cOUINumberPicker.mGreenStart, cOUINumberPicker.mGreenEnd, gradientCoeff);
                cOUINumberPicker.gradualChange(cOUINumberPicker.mBlueStart, cOUINumberPicker.mBlueEnd, gradientCoeff);
            }
            float f8 = f4;
            int iArgb = Color.argb(cOUINumberPicker.mAlphaStart, cOUINumberPicker.mRedStart, cOUINumberPicker.mGreenStart, cOUINumberPicker.mBlueStart);
            char c4 = c3;
            int iArgb2 = Color.argb(cOUINumberPicker.mAlphaEnd, cOUINumberPicker.mRedEnd, cOUINumberPicker.mGreenEnd, cOUINumberPicker.mBlueEnd);
            int i12 = cOUINumberPicker.mNormalTextSize;
            float fGradualChangeTextSize = cOUINumberPicker.gradualChangeTextSize(i12, cOUINumberPicker.mFocusTextSize, i12, i12, i9);
            int i13 = i9;
            cOUINumberPicker.mSelectorWheelPaint.setColor(iArgb);
            String str = cOUINumberPicker.mSelectorIndexToStringCache.get(i11);
            cOUINumberPicker.mSelectorWheelPaint.setTextSize(cOUINumberPicker.mNormalTextSize);
            if (cOUINumberPicker.mMeasureTextSelectorPaint.measureText(str) >= cOUINumberPicker.getMeasuredWidth()) {
                cOUINumberPicker.mCurrentLanguageTooLong = r8;
                cOUINumberPicker.mSelectorWheelPaint.setTextAlign(Paint.Align.LEFT);
                f3 = 0.0f;
            } else {
                cOUINumberPicker.mCurrentLanguageTooLong = z3;
                cOUINumberPicker.mSelectorWheelPaint.setTextAlign(Paint.Align.CENTER);
                f3 = f5;
            }
            if (i11 != Integer.MIN_VALUE) {
                int iRound = ((int) ((((((i13 + i13) + cOUINumberPicker.mSelectorElementHeight) - cOUINumberPicker.mNormalTextTop) - cOUINumberPicker.mNormalTextBottom) / f8) + (cOUINumberPicker.mPickerOffset / 2) + (cOUINumberPicker.mDiffusion * (i10 - Math.round((cOUINumberPicker.mSelectorIndices.length / f8) - 0.01f))))) + cOUINumberPicker.mDrawItemOffsetY;
                cOUINumberPicker.mUnitTextPaint.setTextSize(cOUINumberPicker.mNormalTextSize);
                Paint.FontMetrics fontMetrics = cOUINumberPicker.mUnitTextPaint.getFontMetrics();
                int i14 = cOUINumberPicker.mSelectorElementHeight;
                f7 = (int) ((((i14 - fontMetrics.top) - fontMetrics.bottom) / f8) + (cOUINumberPicker.mPickerOffset / 2) + ((cOUINumberPicker.mSelectorMiddleItemIndex - r8) * i14));
                int iSave = canvas2.save();
                canvas2.clipOutRect(0.0f, ((cOUINumberPicker.getHeight() / f8) - cOUINumberPicker.mBackgroundRadius) - cOUINumberPicker.mDiffusion, cOUINumberPicker.getWidth(), (cOUINumberPicker.getHeight() / f8) + cOUINumberPicker.mBackgroundRadius + cOUINumberPicker.mDiffusion);
                float f9 = iRound;
                canvas2.drawText(str != null ? str : "", f3, f9, cOUINumberPicker.mSelectorWheelPaint);
                canvas2.restoreToCount(iSave);
                int iSave2 = canvas2.save();
                z2 = false;
                canvas2.clipRect(0.0f, ((cOUINumberPicker.getHeight() / f8) - cOUINumberPicker.mBackgroundRadius) - cOUINumberPicker.mDiffusion, cOUINumberPicker.getWidth(), (cOUINumberPicker.getHeight() / f8) + cOUINumberPicker.mBackgroundRadius + cOUINumberPicker.mDiffusion);
                cOUINumberPicker.mSelectorWheelPaint.setColor(iArgb2);
                cOUINumberPicker.mSelectorWheelPaint.setTextSize(cOUINumberPicker.mFocusTextSize);
                canvas2.drawText(str != null ? str : "", f3, f9, cOUINumberPicker.mSelectorWheelPaint);
                canvas2.restoreToCount(iSave2);
            } else {
                z2 = false;
                float f10 = fGradualChangeTextSize / cOUINumberPicker.mFocusTextSize;
                float f11 = -0.5f;
                while (f11 < 1.0f) {
                    float f12 = cOUINumberPicker.mIgnoreBarWidth;
                    float f13 = (cOUINumberPicker.mIgnoreBarSpacing + f12) * f11 * f10;
                    float f14 = cOUINumberPicker.mIgnoreBarHeight * f10;
                    float f15 = f13 + f3;
                    float f16 = (f12 * f10) / f8;
                    float f17 = i13;
                    int i15 = cOUINumberPicker.mSelectorElementHeight;
                    float f18 = f14 / f8;
                    Canvas canvas3 = canvas2;
                    canvas3.drawRect(f15 - f16, (((i15 / f8) + f17) - f18) + 33.75f, f15 + f16, f17 + (i15 / f8) + f18 + 33.75f, cOUINumberPicker.mSelectorWheelPaint);
                    f11 += 1.0f;
                    canvas2 = canvas3;
                    cOUINumberPicker = cOUINumberPicker;
                    i13 = i13;
                    z2 = false;
                }
            }
            Canvas canvas4 = canvas2;
            COUINumberPicker cOUINumberPicker2 = cOUINumberPicker;
            i9 = i13 + cOUINumberPicker2.mSelectorElementHeight;
            i10++;
            canvas2 = canvas4;
            cOUINumberPicker = cOUINumberPicker2;
            f6 = f3;
            f4 = f8;
            c3 = c4;
            r8 = 1;
            z3 = false;
        }
        Canvas canvas5 = canvas2;
        COUINumberPicker cOUINumberPicker3 = cOUINumberPicker;
        if (cOUINumberPicker3.mUnitText != null) {
            if (cOUINumberPicker3.isLayoutRtl()) {
                f6 = (f6 + cOUINumberPicker3.mNumberPickerPaddingRight) - cOUINumberPicker3.mNumberPickerPaddingLeft;
            }
            float measuredWidth = f6 + (cOUINumberPicker3.mSelectedValueWidth / 2) + cOUINumberPicker3.mUnitMargin;
            if (cOUINumberPicker3.isLayoutRtl()) {
                measuredWidth = (cOUINumberPicker3.getMeasuredWidth() - measuredWidth) - cOUINumberPicker3.mUnitTextPaint.measureText(cOUINumberPicker3.mUnitText);
            }
            cOUINumberPicker3.mUnitTextPaint.setTextSize(cOUINumberPicker3.mUnitTextSize);
            canvas5.drawText(cOUINumberPicker3.mUnitText, measuredWidth, f7 - cOUINumberPicker3.mUnitMarginBottom, cOUINumberPicker3.mUnitTextPaint);
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled() || motionEvent.getActionMasked() != 0) {
            return false;
        }
        removeAllCallbacks();
        float y2 = motionEvent.getY();
        this.mLastDownEventY = y2;
        this.mLastDownOrMoveEventY = y2;
        this.mLastDownEventTime = motionEvent.getEventTime();
        this.mPerformClickOnTap = false;
        float f3 = this.mLastDownEventY;
        if (f3 < this.mTopSelectionDividerTop) {
            if (this.mScrollState == 0) {
                this.mPressedStateHelper.buttonPressDelayed(2);
            }
        } else if (f3 > this.mBottomSelectionDividerBottom && this.mScrollState == 0) {
            this.mPressedStateHelper.buttonPressDelayed(1);
        }
        getParent().requestDisallowInterceptTouchEvent(true);
        if (!this.mFlingScroller.isFinished()) {
            this.mFlingScroller.abortAnimation();
            this.mAdjustScroller.forceFinished(true);
            onScrollStateChange(0);
        } else if (this.mAdjustScroller.isFinished()) {
            float f4 = this.mLastDownEventY;
            if (f4 < this.mTopSelectionDividerTop) {
                postChangeCurrentByOneFromLongPress(false, ViewConfiguration.getLongPressTimeout());
            } else if (f4 > this.mBottomSelectionDividerBottom) {
                postChangeCurrentByOneFromLongPress(true, ViewConfiguration.getLongPressTimeout());
            } else {
                this.mPerformClickOnTap = true;
            }
        } else {
            this.mFlingScroller.abortAnimation();
            this.mAdjustScroller.forceFinished(true);
        }
        return true;
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        if (z2) {
            initializeSelectorWheel();
            initializeFadingEdges();
        }
        initColorGradientRes();
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        int iMakeMeasureSpec = makeMeasureSpec(i3, this.mMaxWidth);
        super.onMeasure(iMakeMeasureSpec, makeMeasureSpec(i4, this.mMaxHeight));
        if (View.MeasureSpec.getMode(iMakeMeasureSpec) != Integer.MIN_VALUE) {
            this.mTextMargin = (getMeasuredWidth() - this.mSelectedValueWidth) / 2;
        }
        int iResolveSizeAndStateRespectingMinSize = resolveSizeAndStateRespectingMinSize(this.mMinWidth, getMeasuredWidth(), i3) + this.mNumberPickerPaddingRight + this.mNumberPickerPaddingLeft;
        int i5 = this.mMaxViewWidth;
        if (i5 > 0 && iResolveSizeAndStateRespectingMinSize > i5) {
            iResolveSizeAndStateRespectingMinSize = i5;
        }
        setMeasuredDimension(iResolveSizeAndStateRespectingMinSize, resolveSizeAndStateRespectingMinSize(this.mMinHeight, getMeasuredHeight(), i4));
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            initOrResetVelocityTracker();
            this.mVelocityTracker.addMovement(motionEvent);
        } else if (actionMasked == 1) {
            removeChangeCurrentByOneFromLongPress();
            this.mPressedStateHelper.cancel();
            int y2 = (int) motionEvent.getY();
            int iAbs = (int) Math.abs(y2 - this.mLastDownEventY);
            this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaximumFlingVelocity);
            int yVelocity = (int) this.mVelocityTracker.getYVelocity();
            if (Math.abs(yVelocity) > this.mMinimumFlingVelocity) {
                fling((int) (yVelocity * getDampRatio()));
                onScrollStateChange(2);
            } else {
                long eventTime = motionEvent.getEventTime() - this.mLastDownEventTime;
                if (iAbs > this.mTouchSlop || eventTime >= ViewConfiguration.getLongPressTimeout()) {
                    ensureScrollWheelAdjusted();
                } else if (this.mPerformClickOnTap) {
                    this.mPerformClickOnTap = false;
                    performClick();
                } else {
                    int i3 = ((y2 / this.mSelectorElementHeight) - this.mSelectorMiddleItemIndex) + 1;
                    if (i3 > 0) {
                        changeValueByOne(true);
                        this.mPressedStateHelper.buttonTapped(1);
                    } else if (i3 < 0) {
                        changeValueByOne(false);
                        this.mPressedStateHelper.buttonTapped(2);
                    }
                    ensureScrollWheelAdjusted();
                }
            }
            recycleVelocityTracker();
        } else if (actionMasked == 2) {
            initVelocityTrackerIfNotExists();
            this.mVelocityTracker.addMovement(motionEvent);
            float y3 = motionEvent.getY();
            if (this.mScrollState == 1) {
                int i4 = (int) (y3 - this.mLastDownOrMoveEventY);
                this.mDeltaMoveY = i4;
                scrollBy(0, i4);
                invalidate();
            } else if (((int) Math.abs(y3 - this.mLastDownEventY)) > this.mTouchSlop) {
                removeAllCallbacks();
                onScrollStateChange(1);
            }
            this.mLastDownOrMoveEventY = y3;
        } else if (actionMasked == 3) {
            ensureScrollWheelAdjusted();
            recycleVelocityTracker();
        }
        return true;
    }

    public void refresh() {
        String resourceTypeName = getResources().getResourceTypeName(this.mRefreshStyle);
        TypedArray typedArrayObtainStyledAttributes = null;
        if (TextUtils.equals(resourceTypeName, "attr")) {
            typedArrayObtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(null, R.styleable.COUINumberPicker, this.mRefreshStyle, 0);
        } else if (TextUtils.equals(resourceTypeName, BaseConfig.KEY_STYLE)) {
            typedArrayObtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(null, R.styleable.COUINumberPicker, 0, this.mRefreshStyle);
        }
        if (typedArrayObtainStyledAttributes != null) {
            this.mNormalTextColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUINumberPicker_couiNormalTextColor, -1);
            this.mFocusTextColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUINumberPicker_couiFocusTextColor, -1);
            this.mBackgroundColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUINumberPicker_couiPickerBackgroundColor, -1);
            setSelectorTextColor(this.mNormalTextColor, this.mFocusTextColor);
            typedArrayObtainStyledAttributes.recycle();
        }
    }

    @Override // android.view.View
    public void scrollBy(int i3, int i4) {
        int i5;
        int[] iArr = this.mSelectorIndices;
        int i6 = this.mCurrentScrollOffset;
        boolean z2 = this.mWrapSelectorWheel;
        if (!z2 && i4 > 0 && iArr[this.mSelectorMiddleItemIndex] <= this.mMinValue && i6 + i4 >= 0) {
            this.mCurrentScrollOffset = 0;
            return;
        }
        if (!z2 && i4 < 0 && iArr[this.mSelectorMiddleItemIndex] >= this.mMaxValue && i6 + i4 <= 0) {
            this.mCurrentScrollOffset = 0;
            return;
        }
        if (i4 > 65535) {
            this.mDebugY = i4;
            return;
        }
        this.mCurrentScrollOffset = i4 + i6;
        while (true) {
            int i7 = this.mCurrentScrollOffset;
            float f3 = i7;
            int i8 = this.mSelectorElementHeight;
            float f4 = (i8 * 0.95f) + (this.mPickerOffset / 2.0f);
            float f5 = this.mDiffusion;
            if (f3 <= f4 + f5) {
                break;
            }
            this.mCurrentScrollOffset = (int) (i7 - (i8 + f5));
            decrementSelectorIndices(iArr);
            setValueInternal(iArr[this.mSelectorMiddleItemIndex], true);
            if (!this.mWrapSelectorWheel && iArr[this.mSelectorMiddleItemIndex] < this.mMinValue) {
                this.mCurrentScrollOffset = 0;
            }
        }
        while (true) {
            i5 = this.mCurrentScrollOffset;
            float f6 = i5;
            int i9 = this.mSelectorElementHeight;
            float f7 = ((-i9) * 0.95f) - (this.mPickerOffset / 2.0f);
            float f8 = this.mDiffusion;
            if (f6 >= f7 - f8) {
                break;
            }
            this.mCurrentScrollOffset = (int) (i5 + i9 + f8);
            incrementSelectorIndices(iArr);
            setValueInternal(iArr[this.mSelectorMiddleItemIndex], true);
            if (!this.mWrapSelectorWheel && iArr[this.mSelectorMiddleItemIndex] > this.mMaxValue) {
                this.mCurrentScrollOffset = 0;
            }
        }
        if (i6 != i5) {
            onScrollChanged(0, i5, 0, i6);
        }
    }

    public void scrollForceFinished() {
        if (!this.mFlingScroller.isFinished()) {
            moveToFinalScrollerPosition(this.mFlingScroller);
        }
        if (this.mAdjustScroller.isFinished()) {
            return;
        }
        moveToFinalScrollerPosition(this.mAdjustScroller);
    }

    public void setAlignPosition(int i3) {
        this.mAlignPosition = i3;
    }

    public void setBackgroundRadius(int i3) {
        this.mBackgroundRadius = i3;
        invalidate();
    }

    public void setDiffusion(int i3) {
        this.mDiffusion = i3;
        invalidate();
    }

    public void setDisplayedValues(String[] strArr) {
        if (this.mDisplayedValues == strArr) {
            return;
        }
        this.mDisplayedValues = strArr;
        initializeSelectorWheelIndices();
    }

    public void setDrawItemVerticalOffset(int i3) {
        this.mDrawItemOffsetY = i3;
        invalidate();
    }

    public void setEnableAdaptiveVibrator(boolean z2) {
        this.mEnableAdaptiveVibrator = z2;
    }

    public void setFocusTextSize(int i3) {
        this.mFocusTextSize = i3;
        invalidate();
    }

    public void setFormatter(Formatter formatter) {
        if (formatter == this.mFormatter) {
            return;
        }
        this.mFormatter = formatter;
        initializeSelectorWheelIndices();
    }

    public void setGradientColor(int i3, int i4) {
        this.mAlphaStart = Color.alpha(i3);
        this.mAlphaEnd = Color.alpha(i4);
        this.mRedStart = Color.red(i3);
        this.mRedEnd = Color.red(i4);
        this.mGreenStart = Color.green(i3);
        this.mGreenEnd = Color.green(i4);
        this.mBlueStart = Color.blue(i3);
        this.mBlueEnd = Color.blue(i4);
    }

    public void setHasBackground(boolean z2) {
        this.mHasBackground = z2;
    }

    public void setIgnorable(boolean z2) {
        if (this.mIgnorable == z2) {
            return;
        }
        this.mIgnorable = z2;
        initializeSelectorWheelIndices();
        invalidate();
    }

    public void setMaxValue(int i3) {
        if (this.mMaxValue == i3) {
            return;
        }
        if (i3 < 0) {
            throw new IllegalArgumentException("maxValue must be >= 0");
        }
        this.mMaxValue = i3;
        if (i3 < this.mValue) {
            this.mValue = i3;
        }
        initializeSelectorWheelIndices();
        invalidate();
    }

    public void setMinValue(int i3) {
        if (this.mMinValue == i3) {
            return;
        }
        if (i3 < 0) {
            throw new IllegalArgumentException("minValue must be >= 0");
        }
        this.mMinValue = i3;
        if (i3 > this.mValue) {
            this.mValue = i3;
        }
        initializeSelectorWheelIndices();
        invalidate();
    }

    public void setNormalTextColor(int i3) {
        if (this.mNormalTextColor != i3) {
            this.mNormalTextColor = i3;
            setSelectorTextColor(i3, this.mFocusTextColor);
        }
    }

    public void setNormalTextSize(int i3) {
        this.mNormalTextSize = i3;
        invalidate();
    }

    public void setNumberPickerPaddingLeft(int i3) {
        this.mNumberPickerPaddingLeft = i3;
        requestLayout();
    }

    public void setNumberPickerPaddingRight(int i3) {
        this.mNumberPickerPaddingRight = i3;
        requestLayout();
    }

    public void setOnLongPressUpdateInterval(long j3) {
        this.mLongPressUpdateInterval = j3;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }

    public void setOnScrollingStopListener(OnScrollingStopListener onScrollingStopListener) {
        this.mOnScrollingStopListener = onScrollingStopListener;
    }

    public void setOnValueChangedListener(OnValueChangeListener onValueChangeListener) {
        this.mOnValueChangeListener = onValueChangeListener;
    }

    public void setPickerFocusColor(int i3) {
        this.mAlphaEnd = Color.alpha(i3);
        this.mRedEnd = Color.red(i3);
        this.mGreenEnd = Color.green(i3);
        this.mBlueEnd = Color.green(i3);
    }

    public void setPickerNormalColor(int i3) {
        this.mAlphaStart = Color.alpha(i3);
        this.mRedStart = Color.red(i3);
        this.mGreenStart = Color.green(i3);
        this.mBlueStart = Color.green(i3);
    }

    public void setPickerOffset(int i3) {
        this.mPickerOffset = i3;
        invalidate();
    }

    public void setPickerRowNumber(int i3) {
        if (i3 <= 0 || i3 > 2147483645) {
            COUILog.e(TAG, "Illegal picker row number: " + i3);
            return;
        }
        int i4 = i3 + 2;
        this.mSelectorItemCount = i4;
        this.mSelectorMiddleItemIndex = i4 / 2;
        this.mSelectorIndices = new int[i4];
    }

    public void setSelectedValueWidth(int i3) {
        this.mSelectedValueWidth = i3;
    }

    public void setSelectorTextColor(int i3, int i4) {
        setGradientColor(i3, i4);
        invalidate();
    }

    public void setTouchEffectInterval(int i3) {
        this.mTouchEffectInterval = i3;
    }

    public void setTwoDigitFormatter() {
        if (this.mTwoDigitFormatter == null) {
            this.mTwoDigitFormatter = new TwoDigitFormatter();
        }
        this.mFormatter = this.mTwoDigitFormatter;
    }

    public void setUnitText(String str) {
        this.mUnitText = str;
    }

    public void setValue(int i3) {
        setValueInternal(i3, false);
    }

    public void setVerticalFadingEdgeEnable(boolean z2) {
        this.mVerticalFadingEdgeEnable = z2;
        requestLayout();
    }

    public void setVibrateIntensity(float f3) {
        this.mVibrateIntensity = f3;
    }

    public void setVibrateLevel(int i3) {
        this.mVibrateLevel = i3;
    }

    public void setWrapSelectorWheel(boolean z2) {
        this.mWrapSelectorWheelPreferred = z2;
        updateWrapSelectorWheel();
    }

    public COUINumberPicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiNumberPickerStyle);
    }

    private int getWrappedSelectorIndex(int i3, int i4) {
        int i5 = this.mMaxValue;
        int i6 = this.mMinValue;
        if (i5 - i6 <= 0) {
            return -1;
        }
        if (i3 == Integer.MIN_VALUE) {
            i3 = i6 - 1;
        }
        int iFloorMod = COUIMathUtils.floorMod((i3 - i6) + i4, (i5 - i6) + 1 + (this.mIgnorable ? 1 : 0));
        int i7 = this.mMaxValue;
        int i8 = this.mMinValue;
        if (iFloorMod < (i7 - i8) + 1) {
            return i8 + iFloorMod;
        }
        return Integer.MIN_VALUE;
    }

    public COUINumberPicker(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, COUIContextUtil.isCOUIDarkTheme(context) ? R.style.COUINumberPicker_Dark : R.style.COUINumberPicker);
    }

    public COUINumberPicker(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mFlingFriction = ViewConfiguration.getScrollFriction();
        this.MAX_SCROLL_OFFSET = 65535;
        this.mSelectorIndexToStringCache = new SparseArray<>();
        this.mWrapSelectorWheelPreferred = true;
        this.mLongPressUpdateInterval = 300L;
        this.mScrollState = 0;
        this.mLastHandledDownDpadKeyCode = -1;
        this.mDrawItemOffsetY = 0;
        this.mVerticalFadingEdgeEnable = false;
        this.mHasBackground = false;
        this.mEnableAdaptiveVibrator = true;
        this.mHasMotorVibrator = true;
        this.mLinearMotorVibrator = null;
        this.mStartCalculateTime = -1L;
        this.mVibrateIntensity = 1.0f;
        this.mPreviousTime = 0;
        this.mScrollerVelocity = 0;
        COUIDarkModeUtil.setForceDarkAllow(this, false);
        this.mAccessibilityManager = (AccessibilityManager) getContext().getSystemService(RomUpdateListParser.ACCESSIBILITY_TAG);
        COUISoundLoadUtil cOUISoundLoadUtil = COUISoundLoadUtil.getInstance();
        this.mSoundUtil = cOUISoundLoadUtil;
        this.mClickSoundId = cOUISoundLoadUtil.loadSoundFile(context, R.raw.coui_numberpicker_click);
        if (attributeSet != null) {
            this.mRefreshStyle = attributeSet.getStyleAttribute();
        }
        if (this.mRefreshStyle == 0) {
            this.mRefreshStyle = i3;
        }
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUINumberPicker, i3, i4);
        int integer = typedArrayObtainStyledAttributes.getInteger(R.styleable.COUINumberPicker_couiPickerRowNumber, 5) + 2;
        this.mSelectorItemCount = integer;
        this.mSelectorMiddleItemIndex = integer / 2;
        this.mSelectorIndices = new int[integer];
        int dimensionPixelSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUINumberPicker_internalMinHeight, -1);
        this.mMinHeight = dimensionPixelSize;
        int dimensionPixelSize2 = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUINumberPicker_internalMaxHeight, -1);
        this.mMaxHeight = dimensionPixelSize2;
        if (dimensionPixelSize != -1 && dimensionPixelSize2 != -1 && dimensionPixelSize > dimensionPixelSize2) {
            throw new IllegalArgumentException("minHeight > maxHeight");
        }
        int dimensionPixelSize3 = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUINumberPicker_internalMinWidth, -1);
        this.mMinWidth = dimensionPixelSize3;
        int dimensionPixelSize4 = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUINumberPicker_internalMaxWidth, -1);
        this.mMaxWidth = dimensionPixelSize4;
        if (dimensionPixelSize3 != -1 && dimensionPixelSize4 != -1 && dimensionPixelSize3 > dimensionPixelSize4) {
            throw new IllegalArgumentException("minWidth > maxWidth");
        }
        this.mAlignPosition = typedArrayObtainStyledAttributes.getInteger(R.styleable.COUINumberPicker_couiPickerAlignPosition, -1);
        this.mFocusTextSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUINumberPicker_focusTextSize, -1);
        this.mNormalTextSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUINumberPicker_startTextSize, -1);
        this.mVisualWidth = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUINumberPicker_couiPickerVisualWidth, -1);
        this.mNumberPickerPaddingLeft = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUINumberPicker_couiNOPickerPaddingLeft, 0);
        this.mNumberPickerPaddingRight = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUINumberPicker_couiNOPickerPaddingRight, 0);
        this.mNormalTextColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUINumberPicker_couiNormalTextColor, -1);
        this.mFocusTextColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUINumberPicker_couiFocusTextColor, -1);
        this.mBackgroundColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUINumberPicker_couiPickerBackgroundColor, -1);
        this.mTouchEffectInterval = typedArrayObtainStyledAttributes.getInt(R.styleable.COUINumberPicker_couiPickerTouchEffectInterval, 100);
        setGradientColor(this.mNormalTextColor, this.mFocusTextColor);
        this.mEnableAdaptiveVibrator = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUINumberPicker_couiPickerAdaptiveVibrator, true);
        this.mVibrateLevel = typedArrayObtainStyledAttributes.getInteger(R.styleable.COUINumberPicker_couiVibrateLevel, 0);
        this.mHasMotorVibrator = VibrateUtils.isLinearMotorVersion(context);
        this.mVerticalFadingEdgeEnable = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUINumberPicker_couiPickerVerticalFading, false);
        this.mDiffusion = typedArrayObtainStyledAttributes.getDimensionPixelOffset(R.styleable.COUINumberPicker_couiPickerDiffusion, 0);
        typedArrayObtainStyledAttributes.recycle();
        TypedArray typedArrayObtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R.styleable.COUIPickersCommonAttrs, i3, 0);
        this.mMaxViewWidth = typedArrayObtainStyledAttributes2.getDimensionPixelSize(R.styleable.COUIPickersCommonAttrs_couiPickersMaxWidth, 0);
        typedArrayObtainStyledAttributes2.recycle();
        this.mIgnoreBarWidth = getResources().getDimension(R.dimen.coui_numberpicker_ignore_bar_width);
        this.mIgnoreBarHeight = getResources().getDimension(R.dimen.coui_numberpicker_ignore_bar_height);
        this.mIgnoreBarSpacing = getResources().getDimension(R.dimen.coui_numberpicker_ignore_bar_spacing);
        this.mUnitMinWidth = getResources().getDimensionPixelOffset(R.dimen.coui_number_picker_unit_min_width);
        this.mUnitTextSize = getResources().getDimensionPixelSize(R.dimen.coui_numberpicker_unit_textSize);
        this.mUnitMarginBottom = getResources().getDimensionPixelSize(R.dimen.coui_numberpicker_unit_margin_bottom);
        this.mSelectedValueWidth = getResources().getDimensionPixelOffset(R.dimen.coui_number_picker_text_width);
        this.mUnitMargin = getResources().getDimensionPixelOffset(R.dimen.coui_number_picker_text_margin_start);
        this.mBackgroundDividerHeight = Math.max(getResources().getDimensionPixelSize(R.dimen.coui_number_picker_background_divider_height), 1);
        this.mPpi = getContext().getResources().getDisplayMetrics().density * 160.0f;
        this.mPhysicalCoeff = computeDeceleration(0.84f);
        int i5 = ((dimensionPixelSize3 - this.mSelectedValueWidth) - this.mUnitMinWidth) - (this.mUnitMargin * 2);
        this.mInitTextMargin = i5;
        this.mTextMargin = i5;
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mMinimumFlingVelocity = 750;
        this.mMaximumFlingVelocity = 5000;
        Paint paint = new Paint();
        paint.setTextSize(this.mNormalTextSize);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        paint.setTypeface(Typeface.create("sans-serif-medium", 0));
        this.mNormalTextTop = fontMetrics.top;
        this.mNormalTextBottom = fontMetrics.bottom;
        this.mSelectorWheelPaint = paint;
        this.mMeasureTextSelectorPaint = paint;
        paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.coui_numberpicker_textSize_big));
        this.mFlingScroller = new Scroller(getContext(), SLOW_FLING_INTERPOLATOR);
        this.mAdjustScroller = new Scroller(getContext(), FLING_INTERPOLATOR);
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
        this.mPressedStateHelper = new PressedStateHelper();
        setWillNotDraw(false);
        setVerticalScrollBarEnabled(false);
        Paint paint2 = new Paint();
        this.mUnitTextPaint = paint2;
        paint2.setAntiAlias(true);
        paint2.setTextSize(this.mUnitTextSize);
        paint2.setColor(this.mFocusTextColor);
        paint2.setTypeface(Typeface.create(Typeface.DEFAULT, 0));
        this.mBackgroundRadius = context.getResources().getDimensionPixelOffset(R.dimen.coui_selected_background_radius);
        this.mBackgroundLeft = context.getResources().getDimensionPixelOffset(R.dimen.coui_selected_background_horizontal_padding);
        this.mPickerOffset = 0;
        Paint paint3 = new Paint(1);
        this.mBackgroundPaint = paint3;
        paint3.setColor(this.mBackgroundColor);
    }
}