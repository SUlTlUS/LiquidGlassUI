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
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.lunarutil.COUILunarUtil;
import com.oplus.dmp.sdk.analyzer.timeextractor.detector.AbstractTimeDetector;
import com.oplus.settings.feature.smartkey.SmartKeyConstantUtils;
import com.support.picker.R;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/* JADX INFO: loaded from: classes7.dex */
public class COUILunarDatePicker extends FrameLayout {
    private static final int DAY_OF_REFRESH_SPINNER = 27;
    private static final boolean DEFAULT_CALENDAR_VIEW_SHOWN = true;
    private static final boolean DEFAULT_ENABLED_STATE = true;
    private static final int DEFAULT_END_DAY = 31;
    private static final int DEFAULT_END_HOUR = 23;
    private static final int DEFAULT_END_MINUTE = 59;
    private static final int DEFAULT_END_MONTH = 11;
    private static final int DEFAULT_END_YEAR = 2099;
    private static final boolean DEFAULT_SPINNERS_SHOWN = true;
    private static final int DEFAULT_START_YEAR = 1910;
    public static final int IGNORED_YEAR = Integer.MIN_VALUE;
    private static final int IGNORED_YEAR_MONTH_COUNT = 24;
    private static final int LEAPYEAR_MONTH_COUNT = 13;
    private static final int LONGPRESS_UPDATE_INTERVAL = 100;
    private static final int MIN_BACKGROUND_DIVIDER_HEIGHT = 1;
    private static final int MONTH_LONGPRESS_UPDATE_INTERVAL = 200;
    private static final int NORMAL_MONTH_COUNT = 12;
    private static final int PICKER_CHILD_COUNT = 3;
    private static final String TAG = "COUILunarDatePicker";
    private static final int TEN = 10;
    private static final int TWO = 2;
    private static final int ZERO = 0;
    private static String sLeapString;
    private int mBackgroundDividerHeight;
    private int mBackgroundLeft;
    private int mBackgroundRadius;
    private IncompleteDate mCurrentDate;
    private Locale mCurrentLocale;
    private final COUINumberPicker mDaySpinner;
    private boolean mIsEnabled;
    private int mMaxWidth;
    private final COUINumberPicker mMonthSpinner;
    private int mNumberOfMonths;
    private OnDateChangedListener mOnDateChangedListener;
    private String[] mShortMonths;
    private final LinearLayout mSpinners;
    private IncompleteDate mTempDate;
    private boolean mYearIgnorable;
    private final COUINumberPicker mYearSpinner;
    private static final String[] CHINESE_NUMBER = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};
    private static Calendar sMinDate = Calendar.getInstance();
    private static Calendar sMaxDate = Calendar.getInstance();

    /* JADX INFO: renamed from: com.coui.appcompat.picker.COUILunarDatePicker$1, reason: invalid class name */
    public class AnonymousClass1 implements COUINumberPicker.OnValueChangeListener {
        public AnonymousClass1() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.OnValueChangeListener
        public void onValueChange(COUINumberPicker cOUINumberPicker, int i3, int i4) {
            COUILunarDatePicker.access$100(COUILunarDatePicker.this).setWith(COUILunarDatePicker.access$000(COUILunarDatePicker.this));
            COUILunarUtil.calculateLunarByGregorian(COUILunarDatePicker.access$100(COUILunarDatePicker.this).get(1), COUILunarDatePicker.access$100(COUILunarDatePicker.this).get(2) + 1, COUILunarDatePicker.access$100(COUILunarDatePicker.this).get(5));
            if (cOUINumberPicker == COUILunarDatePicker.access$200(COUILunarDatePicker.this)) {
                COUILunarDatePicker.access$100(COUILunarDatePicker.this).change(5, i3, i4);
            } else if (cOUINumberPicker == COUILunarDatePicker.access$300(COUILunarDatePicker.this)) {
                COUILunarDatePicker.access$100(COUILunarDatePicker.this).change(2, i3, i4);
            } else {
                if (cOUINumberPicker != COUILunarDatePicker.access$400(COUILunarDatePicker.this)) {
                    throw new IllegalArgumentException();
                }
                COUILunarDatePicker.access$100(COUILunarDatePicker.this).change(1, i3, i4);
            }
            COUILunarDatePicker cOUILunarDatePicker = COUILunarDatePicker.this;
            COUILunarDatePicker.access$500(cOUILunarDatePicker, COUILunarDatePicker.access$100(cOUILunarDatePicker));
            COUILunarDatePicker.access$600(COUILunarDatePicker.this);
            COUILunarDatePicker.access$700(COUILunarDatePicker.this);
            COUILunarDatePicker.access$800(COUILunarDatePicker.this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.picker.COUILunarDatePicker$2, reason: invalid class name */
    public class AnonymousClass2 implements COUINumberPicker.OnScrollingStopListener {
        public AnonymousClass2() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.OnScrollingStopListener
        public void onScrollingStop() {
            COUILunarDatePicker cOUILunarDatePicker = COUILunarDatePicker.this;
            cOUILunarDatePicker.announceForAccessibility(COUILunarDatePicker.access$900(COUILunarDatePicker.access$000(cOUILunarDatePicker)));
        }
    }

    public interface OnDateChangedListener {
        void onLunarDateChanged(COUILunarDatePicker cOUILunarDatePicker, int i3, int i4, int i5);
    }

    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new AnonymousClass1();
        private final int mDay;
        private final int mMonth;
        private final int mYear;

        /* JADX INFO: renamed from: com.coui.appcompat.picker.COUILunarDatePicker$SavedState$1, reason: invalid class name */
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

        public static /* synthetic */ int access$1200(SavedState savedState) {
            return savedState.mYear;
        }

        public static /* synthetic */ int access$1300(SavedState savedState) {
            return savedState.mMonth;
        }

        public static /* synthetic */ int access$1400(SavedState savedState) {
            return savedState.mDay;
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i3) {
            super.writeToParcel(parcel, i3);
            parcel.writeInt(this.mYear);
            parcel.writeInt(this.mMonth);
            parcel.writeInt(this.mDay);
        }

        public /* synthetic */ SavedState(Parcelable parcelable, int i3, int i4, int i5, AnonymousClass1 anonymousClass1) {
            this(parcelable, i3, i4, i5);
        }

        private SavedState(Parcelable parcelable, int i3, int i4, int i5) {
            super(parcelable);
            this.mYear = i3;
            this.mMonth = i4;
            this.mDay = i5;
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.mYear = parcel.readInt();
            this.mMonth = parcel.readInt();
            this.mDay = parcel.readInt();
        }
    }

    static {
        sMinDate.set(1910, 2, 10, 0, 0);
        sMaxDate.set(2099, 11, 31, 23, 59);
    }

    public COUILunarDatePicker(Context context) {
        this(context, null);
    }

    public static /* synthetic */ IncompleteDate access$000(COUILunarDatePicker cOUILunarDatePicker) {
        return cOUILunarDatePicker.mCurrentDate;
    }

    public static /* synthetic */ IncompleteDate access$100(COUILunarDatePicker cOUILunarDatePicker) {
        return cOUILunarDatePicker.mTempDate;
    }

    public static /* synthetic */ COUINumberPicker access$200(COUILunarDatePicker cOUILunarDatePicker) {
        return cOUILunarDatePicker.mDaySpinner;
    }

    public static /* synthetic */ COUINumberPicker access$300(COUILunarDatePicker cOUILunarDatePicker) {
        return cOUILunarDatePicker.mMonthSpinner;
    }

    public static /* synthetic */ COUINumberPicker access$400(COUILunarDatePicker cOUILunarDatePicker) {
        return cOUILunarDatePicker.mYearSpinner;
    }

    public static /* synthetic */ void access$500(COUILunarDatePicker cOUILunarDatePicker, IncompleteDate incompleteDate) {
        cOUILunarDatePicker.setDate(incompleteDate);
    }

    public static /* synthetic */ void access$600(COUILunarDatePicker cOUILunarDatePicker) {
        cOUILunarDatePicker.updateSpinners();
    }

    public static /* synthetic */ void access$700(COUILunarDatePicker cOUILunarDatePicker) {
        cOUILunarDatePicker.updateCalendarView();
    }

    public static /* synthetic */ void access$800(COUILunarDatePicker cOUILunarDatePicker) {
        cOUILunarDatePicker.notifyDateChanged();
    }

    public static /* synthetic */ String access$900(IncompleteDate incompleteDate) {
        return getLunarDateString2(incompleteDate);
    }

    private void clampDate() {
        this.mCurrentDate.clampDate(sMinDate, sMaxDate);
    }

    private IncompleteDate getCalendarForLocale(IncompleteDate incompleteDate, Locale locale) {
        if (incompleteDate == null) {
            return new IncompleteDate(locale);
        }
        IncompleteDate incompleteDate2 = new IncompleteDate(locale);
        if (IncompleteDate.access$1000(incompleteDate)) {
            incompleteDate2.setWith(incompleteDate);
            return incompleteDate2;
        }
        incompleteDate2.setTimeInMillis(incompleteDate.getTimeInMillis());
        return incompleteDate2;
    }

    @Deprecated
    public static String getLunarDateString(Calendar calendar) {
        int[] iArrCalculateLunarByGregorian = COUILunarUtil.calculateLunarByGregorian(calendar.get(1), calendar.get(2) + 1, calendar.get(5));
        return getLunarDateString(iArrCalculateLunarByGregorian[0], iArrCalculateLunarByGregorian[1], iArrCalculateLunarByGregorian[2], iArrCalculateLunarByGregorian[3]);
    }

    private static String getLunarDateString2(IncompleteDate incompleteDate) {
        int[] iArrCalculateLunarByGregorian = COUILunarUtil.calculateLunarByGregorian(incompleteDate.get(1), incompleteDate.get(2) + 1, incompleteDate.get(5));
        return getLunarDateString(iArrCalculateLunarByGregorian[0], iArrCalculateLunarByGregorian[1], iArrCalculateLunarByGregorian[2], iArrCalculateLunarByGregorian[3]);
    }

    private boolean isNewDate(int i3, int i4, int i5) {
        return (this.mCurrentDate.get(1) == i3 && this.mCurrentDate.get(2) == i5 && this.mCurrentDate.get(5) == i4) ? false : true;
    }

    private void measureChildConstrained(View view, int i3, int i4) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        view.measure(ViewGroup.getChildMeasureSpec(i3, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin, marginLayoutParams.width), ViewGroup.getChildMeasureSpec(i4, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin, marginLayoutParams.height));
    }

    private void notifyDateChanged() {
        OnDateChangedListener onDateChangedListener = this.mOnDateChangedListener;
        if (onDateChangedListener != null) {
            onDateChangedListener.onLunarDateChanged(this, getYear(), getMonth(), getDayOfMonth());
        }
    }

    private void reorderSpinners() {
        this.mSpinners.removeAllViews();
        char[] dateFormatOrder = DateFormat.getDateFormatOrder(getContext());
        int length = dateFormatOrder.length;
        for (int i3 = 0; i3 < length; i3++) {
            char c3 = dateFormatOrder[i3];
            if (c3 == 'M') {
                this.mSpinners.addView(this.mMonthSpinner);
                setImeOptions(this.mMonthSpinner, length, i3);
            } else if (c3 == 'd') {
                this.mSpinners.addView(this.mDaySpinner);
                setImeOptions(this.mDaySpinner, length, i3);
            } else {
                if (c3 != 'y') {
                    throw new IllegalArgumentException();
                }
                this.mSpinners.addView(this.mYearSpinner);
                setImeOptions(this.mYearSpinner, length, i3);
            }
        }
    }

    private void setCurrentLocale(Locale locale) {
        if (locale.equals(this.mCurrentLocale)) {
            return;
        }
        this.mCurrentLocale = locale;
        this.mTempDate = getCalendarForLocale(this.mTempDate, locale);
        sMinDate = getCalendarForLocale(sMinDate, locale);
        sMaxDate = getCalendarForLocale(sMaxDate, locale);
        this.mCurrentDate = getCalendarForLocale(this.mCurrentDate, locale);
    }

    private void setDate(int i3, int i4, int i5) {
        this.mCurrentDate.set(i3, i4, i5);
        clampDate();
    }

    private void setImeOptions(COUINumberPicker cOUINumberPicker, int i3, int i4) {
        int i5 = i4 < i3 - 1 ? 5 : 6;
        if (cOUINumberPicker.getChildCount() != 3) {
            Log.e(TAG, "spinner.getChildCount() != 3,It isn't init ok.return");
        } else {
            ((TextView) cOUINumberPicker.getChildAt(1)).setImeOptions(i5);
        }
    }

    private void updateCalendarView() {
    }

    /* JADX WARN: Code duplicated, block: B:32:0x0084  */
    /* JADX WARN: Code duplicated, block: B:33:0x00b4  */
    /* JADX WARN: Code duplicated, block: B:35:0x00c0  */
    /* JADX WARN: Code duplicated, block: B:36:0x00eb  */
    /* JADX WARN: Code duplicated, block: B:39:0x011e  */
    /* JADX WARN: Code duplicated, block: B:41:0x0121 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:42:0x0123  */
    /* JADX WARN: Code duplicated, block: B:43:0x012a  */
    /* JADX WARN: Code duplicated, block: B:45:0x0146 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:46:0x0148  */
    /* JADX WARN: Code duplicated, block: B:48:0x014b A[LOOP:2: B:47:0x0149->B:48:0x014b, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:51:0x0170 A[LOOP:3: B:50:0x016e->B:51:0x0170, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:53:0x0190  */
    /* JADX WARN: Code duplicated, block: B:56:0x01bf A[LOOP:1: B:55:0x01bd->B:56:0x01bf, LOOP_END] */
    /* JADX WARN: Instruction removed from duplicated block: B:43:0x012a, please report this as an issue */
    private void updateSpinners() {
        boolean z2;
        int iDaysOfALunarMonth;
        int i3;
        String[] strArr;
        String[] strArr2;
        int i4;
        int i5;
        int maxValue;
        int minValue;
        String[] strArr3;
        int i6;
        int i7;
        int i8 = this.mCurrentDate.get(1);
        int[] iArrCalculateLunarByGregorian = COUILunarUtil.calculateLunarByGregorian(i8, this.mCurrentDate.get(2) + 1, this.mCurrentDate.get(5));
        int iLeapMonth = COUILunarUtil.leapMonth(iArrCalculateLunarByGregorian[0]);
        int i9 = iArrCalculateLunarByGregorian[1];
        String lunarDateString2 = getLunarDateString2(this.mCurrentDate);
        if (iLeapMonth == 0 || ((i9 < iLeapMonth && iLeapMonth != 0) || (i9 == iLeapMonth && !lunarDateString2.contains(sLeapString)))) {
            i9--;
        }
        if (i8 == Integer.MIN_VALUE && iArrCalculateLunarByGregorian[3] == 0) {
            i9 += 12;
        }
        if (i8 != Integer.MIN_VALUE) {
            if (iLeapMonth != 0) {
                this.mNumberOfMonths = 13;
                z2 = true;
            } else {
                this.mNumberOfMonths = 12;
            }
            iDaysOfALunarMonth = COUILunarUtil.daysOfALunarMonth(iArrCalculateLunarByGregorian[0], iArrCalculateLunarByGregorian[1]);
            if (iLeapMonth != 0 && i9 == iLeapMonth && lunarDateString2.contains(sLeapString)) {
                iDaysOfALunarMonth = COUILunarUtil.daysOfLeapMonthInLunarYear(iArrCalculateLunarByGregorian[0]);
            }
            if (this.mCurrentDate.beforeOrEqual(sMinDate)) {
                this.mDaySpinner.setDisplayedValues(null);
                i3 = 2;
                this.mDaySpinner.setMinValue(iArrCalculateLunarByGregorian[2]);
                this.mDaySpinner.setMaxValue(iDaysOfALunarMonth);
                this.mDaySpinner.setWrapSelectorWheel(false);
                this.mMonthSpinner.setDisplayedValues(null);
                this.mMonthSpinner.setMinValue(i9);
                this.mMonthSpinner.setMaxValue(this.mNumberOfMonths - 1);
                this.mMonthSpinner.setWrapSelectorWheel(false);
            } else {
                i3 = 2;
                if (this.mCurrentDate.afterOrEqual(sMaxDate)) {
                    this.mDaySpinner.setDisplayedValues(null);
                    this.mDaySpinner.setMinValue(1);
                    this.mDaySpinner.setMaxValue(iArrCalculateLunarByGregorian[2]);
                    this.mDaySpinner.setWrapSelectorWheel(false);
                    this.mMonthSpinner.setDisplayedValues(null);
                    this.mMonthSpinner.setMinValue(0);
                    this.mMonthSpinner.setMaxValue(i9);
                    this.mMonthSpinner.setWrapSelectorWheel(false);
                } else {
                    this.mDaySpinner.setDisplayedValues(null);
                    this.mDaySpinner.setMinValue(1);
                    this.mDaySpinner.setMaxValue(iDaysOfALunarMonth);
                    this.mDaySpinner.setWrapSelectorWheel(true);
                    this.mMonthSpinner.setDisplayedValues(null);
                    this.mMonthSpinner.setMinValue(0);
                    this.mMonthSpinner.setMaxValue(this.mNumberOfMonths - 1);
                    this.mMonthSpinner.setWrapSelectorWheel(true);
                }
            }
            int i10 = this.mNumberOfMonths;
            strArr = new String[i10];
            strArr2 = new String[i10];
            if (i8 == Integer.MIN_VALUE) {
                for (i7 = 0; i7 < 24; i7++) {
                    if (i7 < 12) {
                        strArr[i7] = this.mShortMonths[i7];
                    } else {
                        strArr[i7] = sLeapString + this.mShortMonths[i7 - 12];
                    }
                }
            } else if (z2) {
                i4 = 0;
                while (i4 < iLeapMonth) {
                    strArr2[i4] = this.mShortMonths[i4];
                    i4++;
                }
                strArr2[iLeapMonth] = sLeapString + this.mShortMonths[iLeapMonth - 1];
                for (i5 = i4 + 1; i5 < 13; i5++) {
                    strArr2[i5] = this.mShortMonths[i5 - 1];
                }
                strArr = (String[]) Arrays.copyOfRange(strArr2, this.mMonthSpinner.getMinValue(), this.mMonthSpinner.getMaxValue() + 1);
            } else {
                strArr = (String[]) Arrays.copyOfRange(this.mShortMonths, this.mMonthSpinner.getMinValue(), this.mMonthSpinner.getMaxValue() + 1);
            }
            this.mMonthSpinner.setDisplayedValues(strArr);
            maxValue = this.mDaySpinner.getMaxValue();
            minValue = this.mDaySpinner.getMinValue();
            strArr3 = new String[(maxValue - minValue) + 1];
            for (i6 = minValue; i6 <= maxValue; i6++) {
                strArr3[i6 - minValue] = COUILunarUtil.chineseStringOfALunarDay(i6);
            }
            this.mDaySpinner.setDisplayedValues(strArr3);
            int i11 = i3;
            int[] iArrCalculateLunarByGregorian2 = COUILunarUtil.calculateLunarByGregorian(sMinDate.get(1), sMinDate.get(i11) + 1, sMinDate.get(5));
            int i12 = sMaxDate.get(1);
            int i13 = sMaxDate.get(i11) + 1;
            int[] iArrCalculateLunarByGregorian3 = COUILunarUtil.calculateLunarByGregorian(i12, i13, i13);
            this.mYearSpinner.setMinValue(iArrCalculateLunarByGregorian2[0]);
            this.mYearSpinner.setMaxValue(iArrCalculateLunarByGregorian3[0]);
            this.mYearSpinner.setWrapSelectorWheel(true);
            this.mYearSpinner.setValue(iArrCalculateLunarByGregorian[0]);
            this.mMonthSpinner.setValue(i9);
            this.mDaySpinner.setValue(iArrCalculateLunarByGregorian[2]);
        }
        this.mNumberOfMonths = 24;
        z2 = false;
        iDaysOfALunarMonth = COUILunarUtil.daysOfALunarMonth(iArrCalculateLunarByGregorian[0], iArrCalculateLunarByGregorian[1]);
        if (iLeapMonth != 0) {
            iDaysOfALunarMonth = COUILunarUtil.daysOfLeapMonthInLunarYear(iArrCalculateLunarByGregorian[0]);
        }
        if (this.mCurrentDate.beforeOrEqual(sMinDate)) {
            this.mDaySpinner.setDisplayedValues(null);
            i3 = 2;
            this.mDaySpinner.setMinValue(iArrCalculateLunarByGregorian[2]);
            this.mDaySpinner.setMaxValue(iDaysOfALunarMonth);
            this.mDaySpinner.setWrapSelectorWheel(false);
            this.mMonthSpinner.setDisplayedValues(null);
            this.mMonthSpinner.setMinValue(i9);
            this.mMonthSpinner.setMaxValue(this.mNumberOfMonths - 1);
            this.mMonthSpinner.setWrapSelectorWheel(false);
        } else {
            i3 = 2;
            if (this.mCurrentDate.afterOrEqual(sMaxDate)) {
                this.mDaySpinner.setDisplayedValues(null);
                this.mDaySpinner.setMinValue(1);
                this.mDaySpinner.setMaxValue(iArrCalculateLunarByGregorian[2]);
                this.mDaySpinner.setWrapSelectorWheel(false);
                this.mMonthSpinner.setDisplayedValues(null);
                this.mMonthSpinner.setMinValue(0);
                this.mMonthSpinner.setMaxValue(i9);
                this.mMonthSpinner.setWrapSelectorWheel(false);
            } else {
                this.mDaySpinner.setDisplayedValues(null);
                this.mDaySpinner.setMinValue(1);
                this.mDaySpinner.setMaxValue(iDaysOfALunarMonth);
                this.mDaySpinner.setWrapSelectorWheel(true);
                this.mMonthSpinner.setDisplayedValues(null);
                this.mMonthSpinner.setMinValue(0);
                this.mMonthSpinner.setMaxValue(this.mNumberOfMonths - 1);
                this.mMonthSpinner.setWrapSelectorWheel(true);
            }
        }
        int i14 = this.mNumberOfMonths;
        strArr = new String[i14];
        strArr2 = new String[i14];
        if (i8 == Integer.MIN_VALUE) {
            while (i7 < 24) {
                if (i7 < 12) {
                    strArr[i7] = this.mShortMonths[i7];
                } else {
                    strArr[i7] = sLeapString + this.mShortMonths[i7 - 12];
                }
            }
        } else if (z2) {
            i4 = 0;
            while (i4 < iLeapMonth) {
                strArr2[i4] = this.mShortMonths[i4];
                i4++;
            }
            strArr2[iLeapMonth] = sLeapString + this.mShortMonths[iLeapMonth - 1];
            while (i5 < 13) {
                strArr2[i5] = this.mShortMonths[i5 - 1];
            }
            strArr = (String[]) Arrays.copyOfRange(strArr2, this.mMonthSpinner.getMinValue(), this.mMonthSpinner.getMaxValue() + 1);
        } else {
            strArr = (String[]) Arrays.copyOfRange(this.mShortMonths, this.mMonthSpinner.getMinValue(), this.mMonthSpinner.getMaxValue() + 1);
        }
        this.mMonthSpinner.setDisplayedValues(strArr);
        maxValue = this.mDaySpinner.getMaxValue();
        minValue = this.mDaySpinner.getMinValue();
        strArr3 = new String[(maxValue - minValue) + 1];
        while (i6 <= maxValue) {
            strArr3[i6 - minValue] = COUILunarUtil.chineseStringOfALunarDay(i6);
        }
        this.mDaySpinner.setDisplayedValues(strArr3);
        int i15 = i3;
        int[] iArrCalculateLunarByGregorian4 = COUILunarUtil.calculateLunarByGregorian(sMinDate.get(1), sMinDate.get(i15) + 1, sMinDate.get(5));
        int i16 = sMaxDate.get(1);
        int i17 = sMaxDate.get(i15) + 1;
        int[] iArrCalculateLunarByGregorian5 = COUILunarUtil.calculateLunarByGregorian(i16, i17, i17);
        this.mYearSpinner.setMinValue(iArrCalculateLunarByGregorian4[0]);
        this.mYearSpinner.setMaxValue(iArrCalculateLunarByGregorian5[0]);
        this.mYearSpinner.setWrapSelectorWheel(true);
        this.mYearSpinner.setValue(iArrCalculateLunarByGregorian[0]);
        this.mMonthSpinner.setValue(i9);
        this.mDaySpinner.setValue(iArrCalculateLunarByGregorian[2]);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(this.mDaySpinner.getBackgroundColor());
        int height = (int) ((getHeight() / 2.0f) - this.mBackgroundRadius);
        canvas.drawRect(this.mBackgroundLeft, height, getWidth() - this.mBackgroundLeft, height + this.mBackgroundDividerHeight, paint);
        int height2 = (int) ((getHeight() / 2.0f) + this.mBackgroundRadius);
        canvas.drawRect(this.mBackgroundLeft, height2, getWidth() - this.mBackgroundLeft, height2 + this.mBackgroundDividerHeight, paint);
        super.dispatchDraw(canvas);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchRestoreInstanceState(SparseArray<Parcelable> sparseArray) {
        dispatchThawSelfOnly(sparseArray);
    }

    public CalendarView getCalendarView() {
        return null;
    }

    public boolean getCalendarViewShown() {
        return false;
    }

    public int getDayOfMonth() {
        return this.mCurrentDate.get(5);
    }

    public COUINumberPicker getDaySpinner() {
        return this.mDaySpinner;
    }

    public int getLeapMonth() {
        return COUILunarUtil.leapMonth(this.mCurrentDate.get(1));
    }

    public int[] getLunarDate() {
        return COUILunarUtil.calculateLunarByGregorian(this.mCurrentDate.get(1), this.mCurrentDate.get(2) + 1, this.mCurrentDate.get(5));
    }

    public long getMaxDate() {
        return sMaxDate.getTimeInMillis();
    }

    public long getMinDate() {
        return sMinDate.getTimeInMillis();
    }

    public int getMonth() {
        return this.mCurrentDate.get(2);
    }

    public COUINumberPicker getMonthSpinner() {
        return this.mMonthSpinner;
    }

    public OnDateChangedListener getOnDateChangedListener() {
        return this.mOnDateChangedListener;
    }

    public boolean getSpinnersShown() {
        return this.mSpinners.isShown();
    }

    public int getYear() {
        return this.mCurrentDate.get(1);
    }

    public COUINumberPicker getYearSpinner() {
        return this.mYearSpinner;
    }

    public void init(int i3, int i4, int i5, OnDateChangedListener onDateChangedListener) {
        setDate(i3, i4, i5);
        updateSpinners();
        updateCalendarView();
        this.mOnDateChangedListener = onDateChangedListener;
    }

    @Override // android.view.View
    public boolean isEnabled() {
        return this.mIsEnabled;
    }

    public boolean isLayoutRtl() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1;
    }

    public boolean isLeapMonth(int i3) {
        return i3 == COUILunarUtil.leapMonth(this.mCurrentDate.get(1));
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
        this.mDaySpinner.clearNumberPickerPadding();
        this.mMonthSpinner.clearNumberPickerPadding();
        this.mYearSpinner.clearNumberPickerPadding();
        measureChildConstrained(this.mDaySpinner, i3, i4);
        measureChildConstrained(this.mMonthSpinner, i3, i4);
        measureChildConstrained(this.mYearSpinner, i3, i4);
        int measuredWidth = (((size - this.mDaySpinner.getMeasuredWidth()) - this.mMonthSpinner.getMeasuredWidth()) - this.mYearSpinner.getMeasuredWidth()) / 2;
        int childCount = this.mSpinners.getChildCount() - 1;
        if (this.mSpinners.getChildAt(0) instanceof COUINumberPicker) {
            ((COUINumberPicker) this.mSpinners.getChildAt(0)).setNumberPickerPaddingLeft(measuredWidth);
        }
        if (this.mSpinners.getChildAt(childCount) instanceof COUINumberPicker) {
            ((COUINumberPicker) this.mSpinners.getChildAt(childCount)).setNumberPickerPaddingRight(measuredWidth);
        }
        super.onMeasure(iMakeMeasureSpec, i4);
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setDate(SavedState.access$1200(savedState), SavedState.access$1300(savedState), SavedState.access$1400(savedState));
        updateSpinners();
        updateCalendarView();
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), getYear(), getMonth(), getDayOfMonth(), null);
    }

    public void refresh() {
        COUINumberPicker cOUINumberPicker = this.mDaySpinner;
        if (cOUINumberPicker != null) {
            cOUINumberPicker.refresh();
        }
        COUINumberPicker cOUINumberPicker2 = this.mMonthSpinner;
        if (cOUINumberPicker2 != null) {
            cOUINumberPicker2.refresh();
        }
        COUINumberPicker cOUINumberPicker3 = this.mYearSpinner;
        if (cOUINumberPicker3 != null) {
            cOUINumberPicker3.refresh();
        }
    }

    public void scrollForceFinished() {
        COUINumberPicker cOUINumberPicker = this.mDaySpinner;
        if (cOUINumberPicker != null) {
            cOUINumberPicker.scrollForceFinished();
        }
        COUINumberPicker cOUINumberPicker2 = this.mMonthSpinner;
        if (cOUINumberPicker2 != null) {
            cOUINumberPicker2.scrollForceFinished();
        }
        COUINumberPicker cOUINumberPicker3 = this.mYearSpinner;
        if (cOUINumberPicker3 != null) {
            cOUINumberPicker3.scrollForceFinished();
        }
    }

    public void setCalendarViewShown(boolean z2) {
    }

    @Override // android.view.View
    public void setEnabled(boolean z2) {
        if (this.mIsEnabled == z2) {
            return;
        }
        super.setEnabled(z2);
        this.mDaySpinner.setEnabled(z2);
        this.mMonthSpinner.setEnabled(z2);
        this.mYearSpinner.setEnabled(z2);
        this.mIsEnabled = z2;
    }

    public void setMaxDate(long j3) {
        this.mTempDate.setTimeInMillis(j3);
        if (this.mTempDate.get(1) != sMaxDate.get(1) || this.mTempDate.get(6) == sMaxDate.get(6)) {
            sMaxDate.setTimeInMillis(j3);
            if (this.mCurrentDate.after(sMaxDate)) {
                this.mCurrentDate.setTimeInMillis(sMaxDate.getTimeInMillis());
                updateCalendarView();
            }
            updateSpinners();
            return;
        }
        Log.w(TAG, "setMaxDate failed!:" + this.mTempDate.get(1) + "<->" + sMaxDate.get(1) + SmartKeyConstantUtils.TIMER_SEPARATOR + this.mTempDate.get(6) + "<->" + sMaxDate.get(6));
    }

    public void setMinDate(long j3) {
        this.mTempDate.setTimeInMillis(j3);
        if (this.mTempDate.get(1) != sMinDate.get(1) || this.mTempDate.get(6) == sMinDate.get(6)) {
            sMinDate.setTimeInMillis(j3);
            if (this.mCurrentDate.before(sMinDate)) {
                this.mCurrentDate.setTimeInMillis(sMinDate.getTimeInMillis());
                updateCalendarView();
            }
            updateSpinners();
            return;
        }
        Log.w(TAG, "setMinDate failed!:" + this.mTempDate.get(1) + "<->" + sMinDate.get(1) + SmartKeyConstantUtils.TIMER_SEPARATOR + this.mTempDate.get(6) + "<->" + sMinDate.get(6));
    }

    public void setNormalTextColor(int i3) {
        COUINumberPicker cOUINumberPicker = this.mDaySpinner;
        if (cOUINumberPicker != null) {
            cOUINumberPicker.setNormalTextColor(i3);
        }
        COUINumberPicker cOUINumberPicker2 = this.mMonthSpinner;
        if (cOUINumberPicker2 != null) {
            cOUINumberPicker2.setNormalTextColor(i3);
        }
        COUINumberPicker cOUINumberPicker3 = this.mYearSpinner;
        if (cOUINumberPicker3 != null) {
            cOUINumberPicker3.setNormalTextColor(i3);
        }
    }

    public void setOnDateChangedListener(OnDateChangedListener onDateChangedListener) {
        this.mOnDateChangedListener = onDateChangedListener;
    }

    public void setSpinnersShown(boolean z2) {
        this.mSpinners.setVisibility(z2 ? 0 : 8);
    }

    public void setVibrateIntensity(float f3) {
        this.mDaySpinner.setVibrateIntensity(f3);
        this.mMonthSpinner.setVibrateIntensity(f3);
        this.mYearSpinner.setVibrateIntensity(f3);
    }

    public void setVibrateLevel(int i3) {
        this.mDaySpinner.setVibrateLevel(i3);
        this.mMonthSpinner.setVibrateLevel(i3);
        this.mYearSpinner.setVibrateLevel(i3);
    }

    public void updateDate(int i3, int i4, int i5) {
        if (isNewDate(i3, i4, i5)) {
            setDate(i3, i4, i5);
            updateSpinners();
            updateCalendarView();
            notifyDateChanged();
        }
    }

    public static class IncompleteDate {
        public static final int LEAP_MONTH_ADDED_VALUE = 12;
        private Calendar mDate;
        private int mDay;
        private int mHour;
        private boolean mIsIncomplete;
        private int mMinute;
        private int mMonth;
        private int mYear;

        public IncompleteDate() {
            init(Calendar.getInstance());
        }

        public static /* synthetic */ boolean access$1000(IncompleteDate incompleteDate) {
            return incompleteDate.mIsIncomplete;
        }

        public void add(int i3, int i4) {
            if (!this.mIsIncomplete) {
                this.mDate.add(i3, i4);
            } else if (i3 == 5) {
                this.mDay += i4;
            } else if (i3 == 2) {
                this.mMonth += i4;
            }
        }

        public boolean after(Calendar calendar) {
            if (this.mIsIncomplete) {
                return false;
            }
            return this.mDate.after(calendar);
        }

        public boolean afterOrEqual(Calendar calendar) {
            if (this.mIsIncomplete) {
                return false;
            }
            return this.mDate.after(calendar) || this.mDate.equals(calendar);
        }

        public boolean before(Calendar calendar) {
            if (this.mIsIncomplete) {
                return false;
            }
            return this.mDate.before(calendar);
        }

        public boolean beforeOrEqual(Calendar calendar) {
            if (this.mIsIncomplete) {
                return false;
            }
            return this.mDate.before(calendar) || this.mDate.equals(calendar);
        }

        /* JADX WARN: Code duplicated, block: B:34:0x0068  */
        /* JADX WARN: Code duplicated, block: B:67:? A[RETURN, SYNTHETIC] */
        public void change(int i3, int i4, int i5) {
            Date dateLunarToSolar;
            boolean z2 = true;
            int[] iArrCalculateLunarByGregorian = COUILunarUtil.calculateLunarByGregorian(get(1), get(2) + 1, get(5));
            if (i3 == 5) {
                if (this.mIsIncomplete) {
                    this.mDay = i5;
                    return;
                }
                if (i4 > 27 && i5 == 1) {
                    this.mDate.add(5, 1 - i4);
                    return;
                } else if (i4 != 1 || i5 <= 27) {
                    this.mDate.add(5, i5 - i4);
                    return;
                } else {
                    this.mDate.add(5, i5 - 1);
                    return;
                }
            }
            if (i3 == 2) {
                if (this.mIsIncomplete) {
                    this.mMonth = i5;
                    return;
                }
                int i6 = i5 + 1;
                int iLeapMonth = COUILunarUtil.leapMonth(iArrCalculateLunarByGregorian[0]);
                if (iLeapMonth != 0 && i6 > iLeapMonth) {
                    if (i6 == iLeapMonth + 1) {
                        i5 = iLeapMonth;
                    }
                    dateLunarToSolar = COUILunarUtil.lunarToSolar(iArrCalculateLunarByGregorian[0], i5, COUILunarUtil.clampDay(iArrCalculateLunarByGregorian[0], i5, iArrCalculateLunarByGregorian[2], z2), z2);
                    if (dateLunarToSolar != null) {
                        setTimeInMillis(dateLunarToSolar.getTime());
                        return;
                    }
                    return;
                }
                i5 = i6;
                z2 = false;
                dateLunarToSolar = COUILunarUtil.lunarToSolar(iArrCalculateLunarByGregorian[0], i5, COUILunarUtil.clampDay(iArrCalculateLunarByGregorian[0], i5, iArrCalculateLunarByGregorian[2], z2), z2);
                if (dateLunarToSolar != null) {
                    setTimeInMillis(dateLunarToSolar.getTime());
                    return;
                }
                return;
            }
            if (i3 == 1) {
                boolean z3 = this.mIsIncomplete;
                if (!z3 && i5 != Integer.MIN_VALUE) {
                    setWith(COUILunarUtil.changeALunarYear(i5, iArrCalculateLunarByGregorian[1], iArrCalculateLunarByGregorian[2], iArrCalculateLunarByGregorian[3]));
                    return;
                }
                if (!z3 && i5 == Integer.MIN_VALUE) {
                    this.mIsIncomplete = true;
                    this.mYear = i5;
                    this.mMonth = (iArrCalculateLunarByGregorian[1] - 1) + (iArrCalculateLunarByGregorian[3] != 1 ? 12 : 0);
                    this.mDay = iArrCalculateLunarByGregorian[2];
                    this.mHour = this.mDate.get(11);
                    this.mMinute = this.mDate.get(12);
                    return;
                }
                if (!z3 || i5 == Integer.MIN_VALUE) {
                    this.mYear = i5;
                    return;
                }
                this.mIsIncomplete = false;
                this.mYear = i5;
                int i7 = this.mMonth;
                int i8 = (i7 % 12) + 1;
                z2 = i7 / 12 > 0 && COUILunarUtil.leapMonth(i5) == i8;
                int iClampDay = COUILunarUtil.clampDay(this.mYear, i8, this.mDay, z2);
                this.mDay = iClampDay;
                Date dateLunarToSolar2 = COUILunarUtil.lunarToSolar(this.mYear, i8, iClampDay, z2);
                if (dateLunarToSolar2 != null) {
                    setTimeInMillis(dateLunarToSolar2.getTime());
                }
            }
        }

        public void clampDate(Calendar calendar, Calendar calendar2) {
            if (this.mIsIncomplete) {
                return;
            }
            if (this.mDate.before(calendar)) {
                setTimeInMillis(calendar.getTimeInMillis());
            } else if (this.mDate.after(calendar2)) {
                setTimeInMillis(calendar2.getTimeInMillis());
            }
        }

        public void clear() {
            this.mDate.clear();
            this.mYear = 0;
            this.mMonth = 0;
            this.mDay = 0;
            this.mHour = 0;
            this.mMinute = 0;
            this.mIsIncomplete = false;
        }

        public int get(int i3) {
            if (!this.mIsIncomplete) {
                return this.mDate.get(i3);
            }
            if (i3 == 5) {
                return this.mDay;
            }
            if (i3 == 2) {
                return this.mMonth;
            }
            return i3 == 1 ? this.mYear : this.mDate.get(i3);
        }

        public int getActualMaximum(int i3) {
            return this.mDate.getActualMaximum(i3);
        }

        public int getActualMinimum(int i3) {
            return this.mDate.getActualMinimum(i3);
        }

        public Date getTime() {
            return this.mDate.getTime();
        }

        public long getTimeInMillis() {
            return this.mDate.getTimeInMillis();
        }

        public void init(Calendar calendar) {
            this.mDate = calendar;
            this.mIsIncomplete = false;
        }

        public void set(int i3, int i4, int i5) {
            if (i3 != Integer.MIN_VALUE) {
                this.mDate.set(1, i3);
                this.mDate.set(2, i4);
                this.mDate.set(5, i5);
                this.mIsIncomplete = false;
                return;
            }
            this.mYear = Integer.MIN_VALUE;
            this.mMonth = i4;
            this.mDay = i5;
            this.mIsIncomplete = true;
        }

        public void setTimeInMillis(long j3) {
            this.mDate.setTimeInMillis(j3);
            this.mIsIncomplete = false;
        }

        public void setWith(IncompleteDate incompleteDate) {
            this.mDate.setTimeInMillis(incompleteDate.mDate.getTimeInMillis());
            this.mYear = incompleteDate.mYear;
            this.mMonth = incompleteDate.mMonth;
            this.mDay = incompleteDate.mDay;
            this.mHour = incompleteDate.mHour;
            this.mMinute = incompleteDate.mMinute;
            this.mIsIncomplete = incompleteDate.mIsIncomplete;
        }

        public IncompleteDate(Locale locale) {
            init(Calendar.getInstance(locale));
        }

        public void set(int i3, int i4, int i5, int i6, int i7) {
            if (i3 != Integer.MIN_VALUE) {
                this.mDate.set(1, i3);
                this.mDate.set(2, i4);
                this.mDate.set(5, i5);
                this.mDate.set(11, i6);
                this.mDate.set(12, i7);
                this.mIsIncomplete = false;
                return;
            }
            this.mYear = Integer.MIN_VALUE;
            this.mMonth = i4;
            this.mDay = i5;
            this.mHour = i6;
            this.mMinute = i7;
            this.mIsIncomplete = true;
        }
    }

    public COUILunarDatePicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiDatePickerStyle);
    }

    public COUILunarDatePicker(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, R.style.DatePickerStyle);
    }

    private void setDate(IncompleteDate incompleteDate) {
        this.mCurrentDate.setWith(incompleteDate);
        clampDate();
    }

    public COUILunarDatePicker(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mNumberOfMonths = 12;
        this.mIsEnabled = true;
        COUIDarkModeUtil.setForceDarkAllow(this, false);
        setCurrentLocale(Locale.getDefault());
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUILunarDatePicker, i3, i4);
        this.mYearIgnorable = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUILunarDatePicker_couiYearIgnorable, false);
        typedArrayObtainStyledAttributes.recycle();
        TypedArray typedArrayObtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R.styleable.COUIPickersCommonAttrs, i3, i4);
        this.mMaxWidth = typedArrayObtainStyledAttributes2.getDimensionPixelSize(R.styleable.COUIPickersCommonAttrs_couiPickersMaxWidth, 0);
        typedArrayObtainStyledAttributes2.recycle();
        this.mBackgroundDividerHeight = Math.max(getResources().getDimensionPixelOffset(R.dimen.coui_number_picker_background_divider_height), 1);
        int i5 = R.layout.coui_lunar_date_picker;
        this.mShortMonths = getResources().getStringArray(R.array.coui_lunar_month);
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(i5, (ViewGroup) this, true);
        sLeapString = getResources().getString(R.string.coui_lunar_leap_string);
        AnonymousClass1 anonymousClass1 = new AnonymousClass1();
        AnonymousClass2 anonymousClass2 = new AnonymousClass2();
        this.mSpinners = (LinearLayout) findViewById(R.id.pickers);
        COUINumberPicker cOUINumberPicker = (COUINumberPicker) findViewById(R.id.day);
        this.mDaySpinner = cOUINumberPicker;
        cOUINumberPicker.setOnLongPressUpdateInterval(100L);
        cOUINumberPicker.setOnValueChangedListener(anonymousClass1);
        cOUINumberPicker.setOnScrollingStopListener(anonymousClass2);
        COUINumberPicker cOUINumberPicker2 = (COUINumberPicker) findViewById(R.id.month);
        this.mMonthSpinner = cOUINumberPicker2;
        cOUINumberPicker2.setMinValue(0);
        cOUINumberPicker2.setMaxValue(this.mNumberOfMonths - 1);
        cOUINumberPicker2.setDisplayedValues(this.mShortMonths);
        cOUINumberPicker2.setOnLongPressUpdateInterval(200L);
        cOUINumberPicker2.setOnValueChangedListener(anonymousClass1);
        cOUINumberPicker2.setOnScrollingStopListener(anonymousClass2);
        COUINumberPicker cOUINumberPicker3 = (COUINumberPicker) findViewById(R.id.year);
        this.mYearSpinner = cOUINumberPicker3;
        cOUINumberPicker3.setOnLongPressUpdateInterval(100L);
        cOUINumberPicker3.setOnValueChangedListener(anonymousClass1);
        cOUINumberPicker3.setOnScrollingStopListener(anonymousClass2);
        cOUINumberPicker3.setIgnorable(this.mYearIgnorable);
        setSpinnersShown(true);
        setCalendarViewShown(true);
        this.mTempDate.clear();
        this.mTempDate.set(1910, 0, 1);
        setMinDate(this.mTempDate.getTimeInMillis());
        this.mTempDate.clear();
        this.mTempDate.set(2099, 11, 31, 23, 59);
        setMaxDate(this.mTempDate.getTimeInMillis());
        this.mCurrentDate.setTimeInMillis(System.currentTimeMillis());
        init(this.mCurrentDate.get(1), this.mCurrentDate.get(2), this.mCurrentDate.get(5), null);
        if (cOUINumberPicker3.isAccessibilityEnable()) {
            cOUINumberPicker3.addTalkbackSuffix(AbstractTimeDetector.BASE_TIME_RANGE_YEAR);
        }
        this.mBackgroundRadius = context.getResources().getDimensionPixelOffset(R.dimen.coui_selected_background_radius);
        this.mBackgroundLeft = context.getResources().getDimensionPixelOffset(R.dimen.coui_selected_background_horizontal_padding);
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
    }

    private static String getLunarDateString(int i3, int i4, int i5, int i6) {
        if (i4 <= 0) {
            return "";
        }
        if (i3 != Integer.MIN_VALUE) {
            StringBuilder sb = new StringBuilder();
            sb.append(i3);
            sb.append(AbstractTimeDetector.BASE_TIME_RANGE_YEAR);
            sb.append(i6 == 0 ? sLeapString : "");
            sb.append(CHINESE_NUMBER[i4 - 1]);
            sb.append("月");
            sb.append(COUILunarUtil.chineseStringOfALunarDay(i5));
            return sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(i6 == 0 ? sLeapString : "");
        sb2.append(CHINESE_NUMBER[i4 - 1]);
        sb2.append("月");
        sb2.append(COUILunarUtil.chineseStringOfALunarDay(i5));
        return sb2.toString();
    }

    private Calendar getCalendarForLocale(Calendar calendar, Locale locale) {
        if (calendar == null) {
            return Calendar.getInstance(locale);
        }
        long timeInMillis = calendar.getTimeInMillis();
        Calendar calendar2 = Calendar.getInstance(locale);
        calendar2.setTimeInMillis(timeInMillis);
        return calendar2;
    }
}