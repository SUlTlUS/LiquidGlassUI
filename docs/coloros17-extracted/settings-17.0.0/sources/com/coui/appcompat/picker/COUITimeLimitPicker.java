package com.coui.appcompat.picker;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.oplus.settings.custom.SettingsCustomUtils;
import com.support.picker.R;
import java.util.Calendar;
import java.util.Locale;

/* JADX INFO: loaded from: classes7.dex */
public class COUITimeLimitPicker extends FrameLayout {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final boolean DEFAULT_ENABLED_STATE = true;
    private static final int HOURS_IN_HALF_DAY = 12;
    private static final int LONGPRESS_UPDATE_INTERVAL = 100;
    private static final int MAX_MINUTE = 59;
    private static final int MIN_BACKGROUND_DIVIDER_HEIGHT = 1;
    private static final OnTimeChangedListener NO_OP_CHANGE_LISTENER = new AnonymousClass1();
    private final Button mAmPmButton;
    private final String[] mAmPmStrings;
    private int mBackgroundDividerHeight;
    private int mBackgroundLeft;
    private int mBackgroundRadius;
    private final COUINumberPicker mCOUIAmPmSpinner;
    private final COUINumberPicker mCOUIHourSpinner;
    private final COUINumberPicker mCOUIMinuteSpinner;
    private Context mContext;
    private Locale mCurrentLocale;
    private TextView mHourText;
    private boolean mIs24HourView;
    private boolean mIsAm;
    private boolean mIsEnabled;
    int mLeftPickerPosition;
    private int mMaxWidth;
    private TextView mMinuteText;
    private OnTimeChangedListener mOnTimeChangedListener;
    private LinearLayout mPickerLayout;
    int mRightPickerPosition;
    private Calendar mTempCalendar;

    /* JADX INFO: renamed from: com.coui.appcompat.picker.COUITimeLimitPicker$1, reason: invalid class name */
    public class AnonymousClass1 implements OnTimeChangedListener {
        @Override // com.coui.appcompat.picker.COUITimeLimitPicker.OnTimeChangedListener
        public void onTimeChanged(COUITimeLimitPicker cOUITimeLimitPicker, int i3, int i4) {
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.picker.COUITimeLimitPicker$2, reason: invalid class name */
    public class AnonymousClass2 implements COUINumberPicker.OnValueChangeListener {
        public AnonymousClass2() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.OnValueChangeListener
        public void onValueChange(COUINumberPicker cOUINumberPicker, int i3, int i4) {
            COUITimeLimitPicker.access$000(COUITimeLimitPicker.this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.picker.COUITimeLimitPicker$3, reason: invalid class name */
    public class AnonymousClass3 implements COUINumberPicker.OnScrollingStopListener {
        public AnonymousClass3() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.OnScrollingStopListener
        public void onScrollingStop() {
            COUITimeLimitPicker.access$100(COUITimeLimitPicker.this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.picker.COUITimeLimitPicker$4, reason: invalid class name */
    public class AnonymousClass4 implements COUINumberPicker.OnValueChangeListener {
        public AnonymousClass4() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.OnValueChangeListener
        public void onValueChange(COUINumberPicker cOUINumberPicker, int i3, int i4) {
            COUITimeLimitPicker.access$000(COUITimeLimitPicker.this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.picker.COUITimeLimitPicker$5, reason: invalid class name */
    public class AnonymousClass5 implements COUINumberPicker.OnScrollingStopListener {
        public AnonymousClass5() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.OnScrollingStopListener
        public void onScrollingStop() {
            COUITimeLimitPicker.access$100(COUITimeLimitPicker.this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.picker.COUITimeLimitPicker$6, reason: invalid class name */
    public class AnonymousClass6 implements View.OnClickListener {
        public AnonymousClass6() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            view.requestFocus();
            COUITimeLimitPicker cOUITimeLimitPicker = COUITimeLimitPicker.this;
            COUITimeLimitPicker.access$202(cOUITimeLimitPicker, !COUITimeLimitPicker.access$200(cOUITimeLimitPicker));
            COUITimeLimitPicker.access$300(COUITimeLimitPicker.this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.picker.COUITimeLimitPicker$7, reason: invalid class name */
    public class AnonymousClass7 implements COUINumberPicker.OnValueChangeListener {
        public AnonymousClass7() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.OnValueChangeListener
        public void onValueChange(COUINumberPicker cOUINumberPicker, int i3, int i4) {
            cOUINumberPicker.requestFocus();
            COUITimeLimitPicker cOUITimeLimitPicker = COUITimeLimitPicker.this;
            COUITimeLimitPicker.access$202(cOUITimeLimitPicker, !COUITimeLimitPicker.access$200(cOUITimeLimitPicker));
            COUITimeLimitPicker.access$300(COUITimeLimitPicker.this);
            COUITimeLimitPicker.access$000(COUITimeLimitPicker.this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.picker.COUITimeLimitPicker$8, reason: invalid class name */
    public class AnonymousClass8 implements COUINumberPicker.OnScrollingStopListener {
        public AnonymousClass8() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.OnScrollingStopListener
        public void onScrollingStop() {
            COUITimeLimitPicker.access$100(COUITimeLimitPicker.this);
        }
    }

    public interface OnTimeChangedListener {
        void onTimeChanged(COUITimeLimitPicker cOUITimeLimitPicker, int i3, int i4);
    }

    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new AnonymousClass1();
        private final int mHour;
        private final int mMinute;

        /* JADX INFO: renamed from: com.coui.appcompat.picker.COUITimeLimitPicker$SavedState$1, reason: invalid class name */
        public class AnonymousClass1 implements Parcelable.Creator<SavedState> {
            @Override // android.os.Parcelable.Creator
            public /* bridge */ /* synthetic */ SavedState createFromParcel(Parcel parcel) {
                return createFromParcel(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public /* bridge */ /* synthetic */ SavedState[] newArray(int i3) {
                return newArray(i3);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int i3) {
                return new SavedState[i3];
            }
        }

        public /* synthetic */ SavedState(Parcel parcel, AnonymousClass1 anonymousClass1) {
            this(parcel);
        }

        public int getHour() {
            return this.mHour;
        }

        public int getMinute() {
            return this.mMinute;
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i3) {
            super.writeToParcel(parcel, i3);
            parcel.writeInt(this.mHour);
            parcel.writeInt(this.mMinute);
        }

        public /* synthetic */ SavedState(Parcelable parcelable, int i3, int i4, AnonymousClass1 anonymousClass1) {
            this(parcelable, i3, i4);
        }

        private SavedState(Parcelable parcelable, int i3, int i4) {
            super(parcelable);
            this.mHour = i3;
            this.mMinute = i4;
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.mHour = parcel.readInt();
            this.mMinute = parcel.readInt();
        }
    }

    public COUITimeLimitPicker(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void access$000(COUITimeLimitPicker cOUITimeLimitPicker) {
        cOUITimeLimitPicker.onTimeChanged();
    }

    public static /* synthetic */ void access$100(COUITimeLimitPicker cOUITimeLimitPicker) {
        cOUITimeLimitPicker.announceForAccessibility();
    }

    public static /* synthetic */ boolean access$200(COUITimeLimitPicker cOUITimeLimitPicker) {
        return cOUITimeLimitPicker.mIsAm;
    }

    public static /* synthetic */ boolean access$202(COUITimeLimitPicker cOUITimeLimitPicker, boolean z2) {
        cOUITimeLimitPicker.mIsAm = z2;
        return z2;
    }

    public static /* synthetic */ void access$300(COUITimeLimitPicker cOUITimeLimitPicker) {
        cOUITimeLimitPicker.updateAmPmControl();
    }

    private void announceForAccessibility() {
        String str;
        String str2 = this.mIsAm ? this.mAmPmStrings[0] : this.mAmPmStrings[1];
        if (this.mIs24HourView) {
            str = this.mCOUIHourSpinner.getCurrentText() + this.mContext.getString(R.string.coui_hour_abbreviation) + "," + this.mCOUIMinuteSpinner.getCurrentText() + this.mContext.getString(R.string.coui_minute_abbreviation);
        } else {
            str = str2 + "," + this.mCOUIHourSpinner.getCurrentText() + this.mContext.getString(R.string.coui_hour_abbreviation) + "," + this.mCOUIMinuteSpinner.getCurrentText() + this.mContext.getString(R.string.coui_minute_abbreviation);
        }
        announceForAccessibility(str);
    }

    private void measureChildConstrained(View view, int i3, int i4) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        view.measure(ViewGroup.getChildMeasureSpec(i3, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin, marginLayoutParams.width), ViewGroup.getChildMeasureSpec(i4, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin, marginLayoutParams.height));
    }

    private void onTimeChanged() {
        OnTimeChangedListener onTimeChangedListener = this.mOnTimeChangedListener;
        if (onTimeChangedListener != null) {
            onTimeChangedListener.onTimeChanged(this, getCurrentHour().intValue(), getCurrentMinute().intValue());
        }
    }

    private void reorderSpinners() {
        COUINumberPicker cOUINumberPicker;
        if (DateFormat.getBestDateTimePattern(Locale.getDefault(), "hm").startsWith(SettingsCustomUtils.CUSTOMIZE_LIST_TAG_ADMIN) || (cOUINumberPicker = this.mCOUIAmPmSpinner) == null) {
            return;
        }
        ViewGroup viewGroup = (ViewGroup) cOUINumberPicker.getParent();
        viewGroup.removeView(this.mCOUIAmPmSpinner);
        viewGroup.addView(this.mCOUIAmPmSpinner);
    }

    private void setCurrentLocale(Locale locale) {
        if (locale.equals(this.mCurrentLocale)) {
            return;
        }
        this.mCurrentLocale = locale;
        this.mTempCalendar = Calendar.getInstance(locale);
    }

    private void updateAmPmControl() {
        if (is24HourView()) {
            COUINumberPicker cOUINumberPicker = this.mCOUIAmPmSpinner;
            if (cOUINumberPicker != null) {
                cOUINumberPicker.setVisibility(8);
                return;
            } else {
                this.mAmPmButton.setVisibility(8);
                return;
            }
        }
        int i3 = !this.mIsAm ? 1 : 0;
        COUINumberPicker cOUINumberPicker2 = this.mCOUIAmPmSpinner;
        if (cOUINumberPicker2 != null) {
            cOUINumberPicker2.setValue(i3);
            this.mCOUIAmPmSpinner.setVisibility(0);
        } else {
            this.mAmPmButton.setText(this.mAmPmStrings[i3]);
            this.mAmPmButton.setVisibility(0);
        }
    }

    private void updateHourControl() {
        if (is24HourView()) {
            this.mCOUIHourSpinner.setMinValue(0);
            this.mCOUIHourSpinner.setMaxValue(23);
            this.mCOUIHourSpinner.setTwoDigitFormatter();
        } else {
            this.mCOUIHourSpinner.setMinValue(1);
            this.mCOUIHourSpinner.setMaxValue(12);
        }
        this.mCOUIHourSpinner.setWrapSelectorWheel(true);
        this.mCOUIMinuteSpinner.setWrapSelectorWheel(true);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(this.mCOUIHourSpinner.getBackgroundColor());
        int height = (int) ((getHeight() / 2.0f) - this.mBackgroundRadius);
        canvas.drawRect(this.mBackgroundLeft, height, getWidth() - this.mBackgroundLeft, height + this.mBackgroundDividerHeight, paint);
        int height2 = (int) ((getHeight() / 2.0f) + this.mBackgroundRadius);
        canvas.drawRect(this.mBackgroundLeft, height2, getWidth() - this.mBackgroundLeft, height2 + this.mBackgroundDividerHeight, paint);
        super.dispatchDraw(canvas);
    }

    public COUINumberPicker getAmPmSpinner() {
        return this.mCOUIAmPmSpinner;
    }

    @Override // android.view.View
    public int getBaseline() {
        return this.mCOUIHourSpinner.getBaseline();
    }

    public Integer getCurrentHour() {
        int value = this.mCOUIHourSpinner.getValue();
        if (is24HourView()) {
            return Integer.valueOf(value);
        }
        return this.mIsAm ? Integer.valueOf(value % 12) : Integer.valueOf((value % 12) + 12);
    }

    public Integer getCurrentMinute() {
        return Integer.valueOf(this.mCOUIMinuteSpinner.getValue());
    }

    public COUINumberPicker getHourSpinner() {
        return this.mCOUIHourSpinner;
    }

    public COUINumberPicker getMinuteSpinner() {
        return this.mCOUIMinuteSpinner;
    }

    public boolean is24HourView() {
        return this.mIs24HourView;
    }

    @Override // android.view.View
    public boolean isEnabled() {
        return this.mIsEnabled;
    }

    public boolean isLayoutRtl() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1;
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setCurrentLocale(configuration.locale);
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        int mode = View.MeasureSpec.getMode(i3);
        int size = View.MeasureSpec.getSize(i3);
        int i5 = this.mMaxWidth;
        if (i5 > 0 && size > i5) {
            size = i5;
        }
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, mode);
        this.mLeftPickerPosition = -1;
        for (int i6 = 0; i6 < this.mPickerLayout.getChildCount(); i6++) {
            View childAt = this.mPickerLayout.getChildAt(i6);
            if ((childAt instanceof COUINumberPicker) && childAt.getVisibility() == 0) {
                if (this.mLeftPickerPosition == -1) {
                    this.mLeftPickerPosition = i6;
                }
                this.mRightPickerPosition = i6;
                ((COUINumberPicker) childAt).clearNumberPickerPadding();
                measureChildConstrained(childAt, i3, i4);
                size -= childAt.getMeasuredWidth();
            }
        }
        int i7 = size / 2;
        if (isLayoutRtl()) {
            int i8 = this.mLeftPickerPosition;
            this.mLeftPickerPosition = this.mRightPickerPosition;
            this.mRightPickerPosition = i8;
        }
        if (this.mPickerLayout.getChildAt(this.mLeftPickerPosition) instanceof COUINumberPicker) {
            ((COUINumberPicker) this.mPickerLayout.getChildAt(this.mLeftPickerPosition)).setNumberPickerPaddingLeft(i7);
        }
        if (this.mPickerLayout.getChildAt(this.mRightPickerPosition) instanceof COUINumberPicker) {
            ((COUINumberPicker) this.mPickerLayout.getChildAt(this.mRightPickerPosition)).setNumberPickerPaddingRight(i7);
        }
        super.onMeasure(iMakeMeasureSpec, i4);
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setCurrentHour(Integer.valueOf(savedState.getHour()));
        setCurrentMinute(Integer.valueOf(savedState.getMinute()));
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), getCurrentHour().intValue(), getCurrentMinute().intValue(), null);
    }

    public void refresh() {
        COUINumberPicker cOUINumberPicker = this.mCOUIHourSpinner;
        if (cOUINumberPicker != null) {
            cOUINumberPicker.refresh();
        }
        COUINumberPicker cOUINumberPicker2 = this.mCOUIMinuteSpinner;
        if (cOUINumberPicker2 != null) {
            cOUINumberPicker2.refresh();
        }
        COUINumberPicker cOUINumberPicker3 = this.mCOUIAmPmSpinner;
        if (cOUINumberPicker3 != null) {
            cOUINumberPicker3.refresh();
        }
    }

    public void scrollForceFinished() {
        COUINumberPicker cOUINumberPicker = this.mCOUIHourSpinner;
        if (cOUINumberPicker != null) {
            cOUINumberPicker.scrollForceFinished();
        }
        COUINumberPicker cOUINumberPicker2 = this.mCOUIMinuteSpinner;
        if (cOUINumberPicker2 != null) {
            cOUINumberPicker2.scrollForceFinished();
        }
        COUINumberPicker cOUINumberPicker3 = this.mCOUIAmPmSpinner;
        if (cOUINumberPicker3 != null) {
            cOUINumberPicker3.scrollForceFinished();
        }
    }

    public void setCurrentData(Integer num, Integer num2) {
        if (num2.equals(getCurrentMinute()) && num.equals(getCurrentHour())) {
            return;
        }
        int iIntValue = num.intValue();
        if (!is24HourView()) {
            if (num.intValue() >= 12) {
                this.mIsAm = false;
                if (num.intValue() > 12) {
                    iIntValue = num.intValue() - 12;
                }
            } else {
                this.mIsAm = true;
                if (num.intValue() == 0) {
                    iIntValue = 12;
                }
            }
            updateAmPmControl();
        }
        this.mCOUIHourSpinner.setValue(iIntValue);
        this.mCOUIMinuteSpinner.setValue(num2.intValue());
        onTimeChanged();
    }

    public void setCurrentHour(Integer num) {
        if (num == null || num.intValue() == getCurrentHour().intValue()) {
            return;
        }
        if (!is24HourView()) {
            if (num.intValue() >= 12) {
                this.mIsAm = false;
                if (num.intValue() > 12) {
                    num = Integer.valueOf(num.intValue() - 12);
                }
            } else {
                this.mIsAm = true;
                if (num.intValue() == 0) {
                    num = 12;
                }
            }
            updateAmPmControl();
        }
        this.mCOUIHourSpinner.setValue(num.intValue());
        onTimeChanged();
    }

    public void setCurrentMinute(Integer num) {
        if (num.equals(getCurrentMinute())) {
            return;
        }
        this.mCOUIMinuteSpinner.setValue(num.intValue());
        onTimeChanged();
    }

    @Override // android.view.View
    public void setEnabled(boolean z2) {
        if (this.mIsEnabled == z2) {
            return;
        }
        super.setEnabled(z2);
        this.mCOUIMinuteSpinner.setEnabled(z2);
        this.mCOUIHourSpinner.setEnabled(z2);
        COUINumberPicker cOUINumberPicker = this.mCOUIAmPmSpinner;
        if (cOUINumberPicker != null) {
            cOUINumberPicker.setEnabled(z2);
        } else {
            this.mAmPmButton.setEnabled(z2);
        }
        this.mIsEnabled = z2;
    }

    public void setIs24HourView(Boolean bool) {
        if (this.mIs24HourView == bool.booleanValue()) {
            return;
        }
        Integer currentHour = getCurrentHour();
        currentHour.intValue();
        this.mIs24HourView = bool.booleanValue();
        updateHourControl();
        setCurrentHour(currentHour);
        updateAmPmControl();
        this.mCOUIHourSpinner.requestLayout();
    }

    public void setNormalTextColor(int i3) {
        COUINumberPicker cOUINumberPicker = this.mCOUIHourSpinner;
        if (cOUINumberPicker != null) {
            cOUINumberPicker.setNormalTextColor(i3);
        }
        COUINumberPicker cOUINumberPicker2 = this.mCOUIMinuteSpinner;
        if (cOUINumberPicker2 != null) {
            cOUINumberPicker2.setNormalTextColor(i3);
        }
        COUINumberPicker cOUINumberPicker3 = this.mCOUIAmPmSpinner;
        if (cOUINumberPicker3 != null) {
            cOUINumberPicker3.setNormalTextColor(i3);
        }
    }

    public void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener) {
        this.mOnTimeChangedListener = onTimeChangedListener;
    }

    public void setRowNumber(int i3) {
        COUINumberPicker cOUINumberPicker;
        if (i3 <= 0 || (cOUINumberPicker = this.mCOUIHourSpinner) == null || this.mCOUIMinuteSpinner == null || this.mCOUIAmPmSpinner == null) {
            return;
        }
        cOUINumberPicker.setPickerRowNumber(i3);
        this.mCOUIMinuteSpinner.setPickerRowNumber(i3);
        this.mCOUIAmPmSpinner.setPickerRowNumber(i3);
    }

    public void setTextVisibility(boolean z2) {
        if (z2) {
            this.mMinuteText.setVisibility(0);
            this.mHourText.setVisibility(0);
        } else {
            this.mMinuteText.setVisibility(8);
            this.mHourText.setVisibility(8);
        }
    }

    public void setUnitVisible(boolean z2) {
        if (z2) {
            this.mCOUIHourSpinner.setUnitText(getContext().getString(R.string.coui_hour_abbreviation));
            this.mCOUIMinuteSpinner.setUnitText(getContext().getString(R.string.coui_minute_abbreviation));
        } else {
            this.mCOUIHourSpinner.setUnitText("");
            this.mCOUIMinuteSpinner.setUnitText("");
        }
    }

    public void setVibrateIntensity(float f3) {
        this.mCOUIHourSpinner.setVibrateIntensity(f3);
        this.mCOUIMinuteSpinner.setVibrateIntensity(f3);
        this.mCOUIAmPmSpinner.setVibrateIntensity(f3);
    }

    public void setVibrateLevel(int i3) {
        this.mCOUIHourSpinner.setVibrateLevel(i3);
        this.mCOUIMinuteSpinner.setVibrateLevel(i3);
        this.mCOUIAmPmSpinner.setVibrateLevel(i3);
    }

    public COUITimeLimitPicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiTimePickerStyle);
    }

    public COUITimeLimitPicker(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, R.style.TimePickerStyle);
    }

    public COUITimeLimitPicker(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mLeftPickerPosition = -1;
        this.mRightPickerPosition = -1;
        this.mIsEnabled = true;
        COUIDarkModeUtil.setForceDarkAllow(this, false);
        setCurrentLocale(Locale.getDefault());
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUIPickersCommonAttrs, i3, i4);
        this.mMaxWidth = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIPickersCommonAttrs_couiPickersMaxWidth, 0);
        typedArrayObtainStyledAttributes.recycle();
        this.mBackgroundDividerHeight = Math.max(getResources().getDimensionPixelOffset(R.dimen.coui_number_picker_background_divider_height), 1);
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.coui_time_limit_picker, (ViewGroup) this, true);
        this.mMinuteText = (TextView) findViewById(R.id.coui_timepicker_minute_text);
        this.mHourText = (TextView) findViewById(R.id.coui_timepicker_hour_text);
        COUINumberPicker cOUINumberPicker = (COUINumberPicker) findViewById(R.id.hour);
        this.mCOUIHourSpinner = cOUINumberPicker;
        cOUINumberPicker.setOnValueChangedListener(new AnonymousClass2());
        cOUINumberPicker.setOnScrollingStopListener(new AnonymousClass3());
        cOUINumberPicker.setUnitText("");
        this.mMinuteText.setTextAlignment(5);
        this.mHourText.setTextAlignment(5);
        this.mPickerLayout = (LinearLayout) findViewById(R.id.time_pickers);
        COUINumberPicker cOUINumberPicker2 = (COUINumberPicker) findViewById(R.id.minute);
        this.mCOUIMinuteSpinner = cOUINumberPicker2;
        cOUINumberPicker2.setTwoDigitFormatter();
        cOUINumberPicker2.setMinValue(0);
        cOUINumberPicker2.setMaxValue(59);
        cOUINumberPicker2.setUnitText("");
        cOUINumberPicker2.setOnLongPressUpdateInterval(100L);
        cOUINumberPicker2.setOnValueChangedListener(new AnonymousClass4());
        cOUINumberPicker2.setOnScrollingStopListener(new AnonymousClass5());
        String[] stringArray = getContext().getResources().getStringArray(R.array.coui_time_picker_ampm);
        this.mAmPmStrings = stringArray;
        View viewFindViewById = findViewById(R.id.amPm);
        if (viewFindViewById instanceof Button) {
            this.mCOUIAmPmSpinner = null;
            Button button = (Button) viewFindViewById;
            this.mAmPmButton = button;
            button.setOnClickListener(new AnonymousClass6());
        } else {
            this.mAmPmButton = null;
            COUINumberPicker cOUINumberPicker3 = (COUINumberPicker) viewFindViewById;
            this.mCOUIAmPmSpinner = cOUINumberPicker3;
            cOUINumberPicker3.setMinValue(0);
            cOUINumberPicker3.setMaxValue(1);
            cOUINumberPicker3.setDisplayedValues(stringArray);
            cOUINumberPicker3.setOnValueChangedListener(new AnonymousClass7());
            cOUINumberPicker3.setOnScrollingStopListener(new AnonymousClass8());
        }
        updateHourControl();
        updateAmPmControl();
        setOnTimeChangedListener(NO_OP_CHANGE_LISTENER);
        setCurrentHour(Integer.valueOf(this.mTempCalendar.get(11)));
        setCurrentMinute(Integer.valueOf(this.mTempCalendar.get(12)));
        if (!isEnabled()) {
            setEnabled(false);
        }
        reorderSpinners();
        if (cOUINumberPicker.isAccessibilityEnable()) {
            cOUINumberPicker.addTalkbackSuffix(context.getString(R.string.coui_hour_abbreviation));
            cOUINumberPicker2.addTalkbackSuffix(context.getString(R.string.coui_minute_abbreviation));
        }
        this.mBackgroundRadius = context.getResources().getDimensionPixelOffset(R.dimen.coui_selected_background_radius);
        this.mBackgroundLeft = context.getResources().getDimensionPixelOffset(R.dimen.coui_selected_background_horizontal_padding);
        setImportantForAccessibility(1);
        this.mContext = context;
    }
}