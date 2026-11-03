package com.coui.appcompat.picker;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.google.android.material.timepicker.TimeModel;
import com.support.picker.R;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/* JADX INFO: loaded from: classes7.dex */
public class COUIDatePicker extends FrameLayout {
    private static final int A_LEAP_YEAR = 2020;
    private static final String DATE_FORMAT = "MM/dd/yyyy";
    private static final boolean DEFAULT_CALENDAR_VIEW_SHOWN = true;
    private static final boolean DEFAULT_ENABLED_STATE = true;
    private static final int DEFAULT_END_YEAR = 2100;
    private static final boolean DEFAULT_SPINNERS_SHOWN = true;
    private static final int DEFAULT_START_YEAR = 1900;
    private static final int HOURS_OF_HALF_DAY = 12;
    public static final int IGNORED_YEAR = Integer.MIN_VALUE;
    private static final String LOG_TAG = "COUIDatePicker";
    private static final int LONGPRESS_UPDATE_INTERVAL = 100;
    private static final String MD_FORMAT = "MM/dd";
    private static final int MIN_BACKGROUND_DIVIDER_HEIGHT = 1;
    private static final int MONTH_LONGPRESS_UPDATE_INTERVAL = 200;
    private static final String TAG = "COUIDatePicker";
    private static char[] sOrderEn = {'d', 'M', 'y'};
    private int mBackgroundDividerHeight;
    private int mBackgroundLeft;
    private int mBackgroundRadius;
    private Context mContext;
    private IncompleteDate mCurrentDate;
    private Locale mCurrentLocale;
    private final DateFormat mDateFormat;
    private Format mDayFormat;
    private final COUINumberPicker mDaySpinner;
    private Date mEndDate;
    private int mFocusColor;
    private boolean mIsEnabled;
    int mLeftPickerPosition;
    private Calendar mMaxDate;
    private int mMaxWidth;
    private Calendar mMinDate;
    private Format mMonthFormat;
    private final COUINumberPicker mMonthSpinner;
    private int mNormalColor;
    private int mNumberOfMonths;
    private OnDateChangedListener mOnDateChangedListener;
    int mRightPickerPosition;
    private String[] mShortMonths;
    private final LinearLayout mSpinners;
    private IncompleteDate mTempDate;
    private Format mYearFormat;
    private boolean mYearIgnorable;
    private final COUINumberPicker mYearSpinner;

    /* JADX INFO: renamed from: com.coui.appcompat.picker.COUIDatePicker$1, reason: invalid class name */
    public class AnonymousClass1 implements COUINumberPicker.OnValueChangeListener {
        public AnonymousClass1() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.OnValueChangeListener
        public void onValueChange(COUINumberPicker cOUINumberPicker, int i3, int i4) {
            COUIDatePicker.access$100(COUIDatePicker.this).setWith(COUIDatePicker.access$000(COUIDatePicker.this));
            if (cOUINumberPicker == COUIDatePicker.access$200(COUIDatePicker.this)) {
                COUIDatePicker.access$100(COUIDatePicker.this).set(5, i4);
            } else if (cOUINumberPicker == COUIDatePicker.access$300(COUIDatePicker.this)) {
                COUIDatePicker.access$100(COUIDatePicker.this).set(2, i4);
            } else {
                if (cOUINumberPicker != COUIDatePicker.access$400(COUIDatePicker.this)) {
                    throw new IllegalArgumentException();
                }
                COUIDatePicker.access$100(COUIDatePicker.this).set(1, i4);
            }
            COUIDatePicker cOUIDatePicker = COUIDatePicker.this;
            COUIDatePicker.access$500(cOUIDatePicker, COUIDatePicker.access$100(cOUIDatePicker));
            COUIDatePicker.access$600(COUIDatePicker.this);
            COUIDatePicker.access$700(COUIDatePicker.this);
            COUIDatePicker.access$800(COUIDatePicker.this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.picker.COUIDatePicker$2, reason: invalid class name */
    public class AnonymousClass2 implements COUINumberPicker.OnScrollingStopListener {
        public AnonymousClass2() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.OnScrollingStopListener
        public void onScrollingStop() {
            COUIDatePicker cOUIDatePicker = COUIDatePicker.this;
            cOUIDatePicker.announceForAccessibility(COUIDatePicker.access$900(cOUIDatePicker));
        }
    }

    public class Format implements COUINumberPicker.Formatter {
        int mId;
        String mTag;

        public Format(int i3, String str) {
            this.mId = i3;
            this.mTag = str;
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.Formatter
        public String format(int i3) {
            if (this.mTag.equals("MONTH")) {
                COUIDatePicker.access$1700(COUIDatePicker.this).setMonth(i3);
                return DateUtils.formatDateTime(COUIDatePicker.this.getContext(), COUIDatePicker.access$1700(COUIDatePicker.this).getTime(), 65576);
            }
            if (!Locale.getDefault().getLanguage().equals("zh")) {
                Formatter formatter = new Formatter(new StringBuilder(), COUIDatePicker.access$1800(COUIDatePicker.this));
                if (this.mTag.equals("YEAR")) {
                    formatter.format(TimeModel.NUMBER_FORMAT, Integer.valueOf(i3));
                    return formatter.toString();
                }
                if (this.mTag.equals("DAY")) {
                    formatter.format(TimeModel.ZERO_LEADING_NUMBER_FORMAT, Integer.valueOf(i3));
                    return formatter.toString();
                }
            }
            return i3 + COUIDatePicker.this.getResources().getString(this.mId);
        }
    }

    public interface OnDateChangedListener {
        void onDateChanged(COUIDatePicker cOUIDatePicker, int i3, int i4, int i5);
    }

    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new AnonymousClass1();
        private final int mDay;
        private final int mMonth;
        private final int mYear;

        /* JADX INFO: renamed from: com.coui.appcompat.picker.COUIDatePicker$SavedState$1, reason: invalid class name */
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

        public static /* synthetic */ int access$1300(SavedState savedState) {
            return savedState.mYear;
        }

        public static /* synthetic */ int access$1400(SavedState savedState) {
            return savedState.mMonth;
        }

        public static /* synthetic */ int access$1500(SavedState savedState) {
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

    public COUIDatePicker(Context context) {
        this(context, null);
    }

    public static /* synthetic */ IncompleteDate access$000(COUIDatePicker cOUIDatePicker) {
        return cOUIDatePicker.mCurrentDate;
    }

    public static /* synthetic */ IncompleteDate access$100(COUIDatePicker cOUIDatePicker) {
        return cOUIDatePicker.mTempDate;
    }

    public static /* synthetic */ Date access$1700(COUIDatePicker cOUIDatePicker) {
        return cOUIDatePicker.mEndDate;
    }

    public static /* synthetic */ Locale access$1800(COUIDatePicker cOUIDatePicker) {
        return cOUIDatePicker.mCurrentLocale;
    }

    public static /* synthetic */ COUINumberPicker access$200(COUIDatePicker cOUIDatePicker) {
        return cOUIDatePicker.mDaySpinner;
    }

    public static /* synthetic */ COUINumberPicker access$300(COUIDatePicker cOUIDatePicker) {
        return cOUIDatePicker.mMonthSpinner;
    }

    public static /* synthetic */ COUINumberPicker access$400(COUIDatePicker cOUIDatePicker) {
        return cOUIDatePicker.mYearSpinner;
    }

    public static /* synthetic */ void access$500(COUIDatePicker cOUIDatePicker, IncompleteDate incompleteDate) {
        cOUIDatePicker.setDate(incompleteDate);
    }

    public static /* synthetic */ void access$600(COUIDatePicker cOUIDatePicker) {
        cOUIDatePicker.updateSpinners();
    }

    public static /* synthetic */ void access$700(COUIDatePicker cOUIDatePicker) {
        cOUIDatePicker.updateCalendarView();
    }

    public static /* synthetic */ void access$800(COUIDatePicker cOUIDatePicker) {
        cOUIDatePicker.notifyDateChanged();
    }

    public static /* synthetic */ String access$900(COUIDatePicker cOUIDatePicker) {
        return cOUIDatePicker.formatDate();
    }

    private void clampDate() {
        this.mCurrentDate.clampDate(this.mMinDate, this.mMaxDate);
    }

    private String formatDate() {
        return !IncompleteDate.access$1100(this.mCurrentDate) ? DateUtils.formatDateTime(this.mContext, IncompleteDate.access$1000(this.mCurrentDate).getTimeInMillis(), 20) : DateUtils.formatDateTime(this.mContext, IncompleteDate.access$1000(this.mCurrentDate).getTimeInMillis(), 24);
    }

    private IncompleteDate getCalendarForLocale(IncompleteDate incompleteDate, Locale locale) {
        if (incompleteDate == null) {
            return new IncompleteDate(locale);
        }
        IncompleteDate incompleteDate2 = new IncompleteDate(locale);
        if (IncompleteDate.access$1100(incompleteDate)) {
            incompleteDate2.setWith(incompleteDate);
            return incompleteDate2;
        }
        incompleteDate2.setTimeInMillis(incompleteDate.getTimeInMillis());
        return incompleteDate2;
    }

    private boolean isNewDate(int i3, int i4, int i5) {
        return (this.mCurrentDate.get(1) == i3 && this.mCurrentDate.get(2) == i4 && this.mCurrentDate.get(5) == i5) ? false : true;
    }

    private void measureChildConstrained(View view, int i3, int i4) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        view.measure(ViewGroup.getChildMeasureSpec(i3, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin, marginLayoutParams.width), ViewGroup.getChildMeasureSpec(i4, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin, marginLayoutParams.height));
    }

    private void notifyDateChanged() {
        OnDateChangedListener onDateChangedListener = this.mOnDateChangedListener;
        if (onDateChangedListener != null) {
            onDateChangedListener.onDateChanged(this, getYear(), getMonth(), getDayOfMonth());
        }
    }

    private boolean parseDate(String str, Calendar calendar) {
        try {
            calendar.setTime(this.mDateFormat.parse(str));
            return true;
        } catch (ParseException unused) {
            return false;
        }
    }

    private void reorderSpinners() {
        String bestDateTimePattern = android.text.format.DateFormat.getBestDateTimePattern(Locale.getDefault(), "yyyyMMMMdd");
        if (isLayoutRtl()) {
            bestDateTimePattern = TextUtils.getReverse(bestDateTimePattern, 0, bestDateTimePattern.length()).toString();
        }
        this.mSpinners.removeAllViews();
        ArrayList arrayList = new ArrayList();
        for (int i3 = 0; i3 < bestDateTimePattern.length(); i3++) {
            char cCharAt = bestDateTimePattern.charAt(i3);
            if (cCharAt != 'M') {
                if (cCharAt != 'd') {
                    if (cCharAt == 'y' && this.mYearSpinner.getParent() == null) {
                        this.mSpinners.addView(this.mYearSpinner);
                        arrayList.add("y");
                    }
                } else if (this.mDaySpinner.getParent() == null) {
                    this.mSpinners.addView(this.mDaySpinner);
                    arrayList.add("d");
                }
            } else if (this.mMonthSpinner.getParent() == null) {
                this.mSpinners.addView(this.mMonthSpinner);
                arrayList.add("M");
            }
            if (this.mLeftPickerPosition == -1) {
                this.mLeftPickerPosition = this.mSpinners.getChildCount() - 1;
            }
            this.mRightPickerPosition = this.mSpinners.getChildCount() - 1;
        }
    }

    private void setCurrentLocale(Locale locale) {
        if (locale.equals(this.mCurrentLocale)) {
            return;
        }
        this.mCurrentLocale = locale;
        this.mTempDate = getCalendarForLocale(this.mTempDate, locale);
        this.mMinDate = getCalendarForLocale(this.mMinDate, locale);
        this.mMaxDate = getCalendarForLocale(this.mMaxDate, locale);
        this.mCurrentDate = getCalendarForLocale(this.mCurrentDate, locale);
        int actualMaximum = this.mTempDate.getActualMaximum(2) + 1;
        this.mNumberOfMonths = actualMaximum;
        this.mShortMonths = new String[actualMaximum];
    }

    private void setDate(int i3, int i4, int i5) {
        this.mCurrentDate.set(i3, i4, i5);
        clampDate();
    }

    private void updateCalendarView() {
    }

    private void updateSpinnerColor() {
        int i3 = this.mNormalColor;
        if (i3 != -1) {
            this.mDaySpinner.setPickerNormalColor(i3);
            this.mMonthSpinner.setPickerNormalColor(this.mNormalColor);
            this.mYearSpinner.setPickerNormalColor(this.mNormalColor);
        }
        int i4 = this.mFocusColor;
        if (i4 != -1) {
            this.mDaySpinner.setPickerFocusColor(i4);
            this.mMonthSpinner.setPickerFocusColor(this.mFocusColor);
            this.mYearSpinner.setPickerFocusColor(this.mFocusColor);
        }
    }

    private void updateSpinners() {
        this.mMonthSpinner.setFormatter(this.mMonthFormat);
        boolean z2 = false;
        if (this.mCurrentDate.get(1) == this.mMinDate.get(1) && this.mCurrentDate.get(1) != this.mMaxDate.get(1)) {
            this.mMonthSpinner.setMinValue(this.mMinDate.get(2));
            this.mMonthSpinner.setMaxValue(this.mMinDate.getActualMaximum(2));
            this.mMonthSpinner.setWrapSelectorWheel(this.mMinDate.get(2) == 0);
        } else if (this.mCurrentDate.get(1) != this.mMinDate.get(1) && this.mCurrentDate.get(1) == this.mMaxDate.get(1)) {
            this.mMonthSpinner.setMinValue(0);
            this.mMonthSpinner.setMaxValue(this.mMaxDate.get(2));
            this.mMonthSpinner.setWrapSelectorWheel(this.mMaxDate.get(2) == this.mMaxDate.getActualMaximum(2));
        } else if (this.mCurrentDate.get(1) == this.mMinDate.get(1) && this.mCurrentDate.get(1) == this.mMaxDate.get(1)) {
            this.mMonthSpinner.setMinValue(this.mMinDate.get(2));
            this.mMonthSpinner.setMaxValue(this.mMaxDate.get(2));
            this.mMonthSpinner.setWrapSelectorWheel(this.mMaxDate.get(2) == this.mMaxDate.getActualMaximum(2) && this.mMinDate.get(2) == 0);
        } else {
            this.mMonthSpinner.setMinValue(this.mCurrentDate.getActualMinimum(2));
            this.mMonthSpinner.setMaxValue(this.mCurrentDate.getActualMaximum(2));
            this.mMonthSpinner.setWrapSelectorWheel(true);
        }
        if (this.mCurrentDate.get(1) == this.mMinDate.get(1) && this.mCurrentDate.get(2) == this.mMinDate.get(2) && (this.mCurrentDate.get(1) != this.mMaxDate.get(1) || this.mCurrentDate.get(2) != this.mMaxDate.get(2))) {
            this.mDaySpinner.setMinValue(this.mMinDate.get(5));
            this.mDaySpinner.setMaxValue(this.mMinDate.getActualMaximum(5));
            this.mDaySpinner.setWrapSelectorWheel(this.mMinDate.get(5) == 1);
        } else if (!(this.mCurrentDate.get(1) == this.mMinDate.get(1) && this.mCurrentDate.get(2) == this.mMinDate.get(2)) && this.mCurrentDate.get(1) == this.mMaxDate.get(1) && this.mCurrentDate.get(2) == this.mMaxDate.get(2)) {
            this.mDaySpinner.setMinValue(1);
            this.mDaySpinner.setMaxValue(this.mMaxDate.get(5));
            this.mDaySpinner.setWrapSelectorWheel(this.mMaxDate.get(5) == this.mMaxDate.getActualMaximum(5));
        } else if (this.mCurrentDate.get(1) == this.mMinDate.get(1) && this.mCurrentDate.get(2) == this.mMinDate.get(2) && this.mCurrentDate.get(1) == this.mMaxDate.get(1) && this.mCurrentDate.get(2) == this.mMaxDate.get(2)) {
            this.mDaySpinner.setMinValue(this.mMinDate.get(5));
            this.mDaySpinner.setMaxValue(this.mMaxDate.get(5));
            COUINumberPicker cOUINumberPicker = this.mDaySpinner;
            if (this.mMaxDate.get(5) == this.mMaxDate.getActualMaximum(5) && this.mMinDate.get(5) == 1) {
                z2 = true;
            }
            cOUINumberPicker.setWrapSelectorWheel(z2);
        } else {
            this.mDaySpinner.setMinValue(this.mCurrentDate.getActualMinimum(5));
            this.mDaySpinner.setMaxValue(this.mCurrentDate.getActualMaximum(5));
            this.mDaySpinner.setWrapSelectorWheel(true);
        }
        this.mYearSpinner.setMinValue(this.mMinDate.get(1));
        this.mYearSpinner.setMaxValue(this.mMaxDate.get(1));
        this.mYearSpinner.setWrapSelectorWheel(true);
        this.mYearSpinner.setFormatter(this.mYearFormat);
        this.mYearSpinner.setValue(this.mCurrentDate.get(1));
        this.mMonthSpinner.setValue(this.mCurrentDate.get(2));
        this.mDaySpinner.setValue(this.mCurrentDate.get(5));
        this.mDaySpinner.setFormatter(this.mDayFormat);
        if (this.mDaySpinner.getValue() > 27) {
            this.mDaySpinner.invalidate();
        }
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

    public long getMaxDate() {
        return this.mMaxDate.getTimeInMillis();
    }

    public long getMinDate() {
        return this.mMinDate.getTimeInMillis();
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
        if (this.mSpinners.getChildAt(this.mLeftPickerPosition) instanceof COUINumberPicker) {
            ((COUINumberPicker) this.mSpinners.getChildAt(this.mLeftPickerPosition)).setNumberPickerPaddingLeft(measuredWidth);
        }
        if (this.mSpinners.getChildAt(this.mRightPickerPosition) instanceof COUINumberPicker) {
            ((COUINumberPicker) this.mSpinners.getChildAt(this.mRightPickerPosition)).setNumberPickerPaddingRight(measuredWidth);
        }
        super.onMeasure(iMakeMeasureSpec, i4);
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setDate(SavedState.access$1300(savedState), SavedState.access$1400(savedState), SavedState.access$1500(savedState));
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

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        setBackgroundDrawable(drawable);
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

    public void setFocusColor(int i3) {
        this.mFocusColor = i3;
        updateSpinnerColor();
    }

    public void setMaxDate(long j3) {
        this.mTempDate.setTimeInMillis(j3);
        if (this.mTempDate.get(1) != this.mMaxDate.get(1) || this.mTempDate.get(6) == this.mMaxDate.get(6)) {
            this.mMaxDate.setTimeInMillis(j3);
            if (this.mCurrentDate.after(this.mMaxDate)) {
                this.mCurrentDate.setTimeInMillis(this.mMaxDate.getTimeInMillis());
                updateCalendarView();
            }
            updateSpinners();
        }
    }

    public void setMinDate(long j3) {
        this.mTempDate.setTimeInMillis(j3);
        if (this.mTempDate.get(1) != this.mMinDate.get(1) || this.mTempDate.get(6) == this.mMinDate.get(6)) {
            this.mMinDate.setTimeInMillis(j3);
            if (this.mCurrentDate.before(this.mMinDate)) {
                this.mCurrentDate.setTimeInMillis(this.mMinDate.getTimeInMillis());
                updateCalendarView();
            }
            updateSpinners();
        }
    }

    public void setNormalColor(int i3) {
        this.mNormalColor = i3;
        updateSpinnerColor();
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

    public COUIDatePicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiDatePickerStyle);
    }

    public void setBackground(int i3) {
        setBackgroundDrawable(getContext().getResources().getDrawable(i3));
    }

    public COUIDatePicker(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, R.style.DatePickerStyle);
    }

    private void setDate(IncompleteDate incompleteDate) {
        this.mCurrentDate.setWith(incompleteDate);
        clampDate();
    }

    public COUIDatePicker(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mDateFormat = new SimpleDateFormat(DATE_FORMAT);
        this.mLeftPickerPosition = -1;
        this.mRightPickerPosition = -1;
        this.mIsEnabled = true;
        COUIDarkModeUtil.setForceDarkAllow(this, false);
        this.mContext = context;
        setCurrentLocale(Locale.getDefault());
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUIDatePicker, i3, i4);
        boolean z2 = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIDatePicker_spinnerShown, true);
        boolean z3 = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIDatePicker_calendarViewShown, true);
        int i5 = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIDatePicker_beginYear, 1900);
        int i6 = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIDatePicker_endYear, 2100);
        String string = typedArrayObtainStyledAttributes.getString(R.styleable.COUIDatePicker_minDate);
        String string2 = typedArrayObtainStyledAttributes.getString(R.styleable.COUIDatePicker_maxDate);
        this.mShortMonths = getResources().getStringArray(R.array.coui_solor_mounth);
        this.mNormalColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIDatePicker_calendarTextColor, -1);
        this.mFocusColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIDatePicker_calendarSelectedTextColor, -1);
        int i7 = R.layout.coui_date_picker;
        this.mYearIgnorable = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIDatePicker_couiYearIgnorable, false);
        typedArrayObtainStyledAttributes.recycle();
        TypedArray typedArrayObtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R.styleable.COUIPickersCommonAttrs, i3, 0);
        this.mMaxWidth = typedArrayObtainStyledAttributes2.getDimensionPixelSize(R.styleable.COUIPickersCommonAttrs_couiPickersMaxWidth, 0);
        typedArrayObtainStyledAttributes2.recycle();
        this.mBackgroundDividerHeight = Math.max(getResources().getDimensionPixelOffset(R.dimen.coui_number_picker_background_divider_height), 1);
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(i7, (ViewGroup) this, true);
        AnonymousClass1 anonymousClass1 = new AnonymousClass1();
        AnonymousClass2 anonymousClass2 = new AnonymousClass2();
        this.mSpinners = (LinearLayout) findViewById(R.id.pickers);
        this.mYearFormat = new Format(R.string.coui_year, "YEAR");
        this.mMonthFormat = new Format(R.string.coui_month, "MONTH");
        this.mDayFormat = new Format(R.string.coui_day, "DAY");
        this.mEndDate = new Date();
        COUINumberPicker cOUINumberPicker = (COUINumberPicker) findViewById(R.id.day);
        this.mDaySpinner = cOUINumberPicker;
        cOUINumberPicker.setOnLongPressUpdateInterval(100L);
        cOUINumberPicker.setOnValueChangedListener(anonymousClass1);
        cOUINumberPicker.setOnScrollingStopListener(anonymousClass2);
        COUINumberPicker cOUINumberPicker2 = (COUINumberPicker) findViewById(R.id.month);
        this.mMonthSpinner = cOUINumberPicker2;
        cOUINumberPicker2.setMinValue(0);
        cOUINumberPicker2.setMaxValue(this.mNumberOfMonths - 1);
        cOUINumberPicker2.setOnLongPressUpdateInterval(200L);
        cOUINumberPicker2.setOnValueChangedListener(anonymousClass1);
        cOUINumberPicker2.setOnScrollingStopListener(anonymousClass2);
        COUINumberPicker cOUINumberPicker3 = (COUINumberPicker) findViewById(R.id.year);
        this.mYearSpinner = cOUINumberPicker3;
        cOUINumberPicker3.setOnLongPressUpdateInterval(100L);
        cOUINumberPicker3.setOnValueChangedListener(anonymousClass1);
        cOUINumberPicker3.setOnScrollingStopListener(anonymousClass2);
        cOUINumberPicker3.setIgnorable(this.mYearIgnorable);
        updateSpinnerColor();
        if (!z2 && !z3) {
            setSpinnersShown(true);
        } else {
            setSpinnersShown(z2);
            setCalendarViewShown(z3);
        }
        this.mTempDate.clear();
        if (TextUtils.isEmpty(string) || !parseDate(string, IncompleteDate.access$1000(this.mTempDate))) {
            this.mTempDate.set(i5, 0, 1);
        }
        setMinDate(IncompleteDate.access$1000(this.mTempDate).getTimeInMillis());
        this.mTempDate.clear();
        if (TextUtils.isEmpty(string2) || !parseDate(string2, IncompleteDate.access$1000(this.mTempDate))) {
            this.mTempDate.set(i6, 11, 31);
        }
        setMaxDate(IncompleteDate.access$1000(this.mTempDate).getTimeInMillis());
        this.mCurrentDate.setTimeInMillis(System.currentTimeMillis());
        init(this.mCurrentDate.get(1), this.mCurrentDate.get(2), this.mCurrentDate.get(5), null);
        reorderSpinners();
        this.mBackgroundRadius = context.getResources().getDimensionPixelOffset(R.dimen.coui_selected_background_radius);
        this.mBackgroundLeft = context.getResources().getDimensionPixelOffset(R.dimen.coui_selected_background_horizontal_padding);
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
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

    public static class IncompleteDate {
        private Calendar mDate;
        private boolean mIsIncomplete;

        public IncompleteDate(Locale locale) {
            this.mDate = Calendar.getInstance(locale);
        }

        public static /* synthetic */ Calendar access$1000(IncompleteDate incompleteDate) {
            return incompleteDate.mDate;
        }

        public static /* synthetic */ boolean access$1100(IncompleteDate incompleteDate) {
            return incompleteDate.mIsIncomplete;
        }

        public boolean after(Calendar calendar) {
            if (this.mIsIncomplete) {
                return false;
            }
            return this.mDate.after(calendar);
        }

        public boolean before(Calendar calendar) {
            if (this.mIsIncomplete) {
                return false;
            }
            return this.mDate.before(calendar);
        }

        public void clampDate(Calendar calendar, Calendar calendar2) {
            if (this.mIsIncomplete) {
                return;
            }
            if (this.mDate.before(calendar)) {
                set(1, calendar.get(1));
                set(2, calendar.get(2));
                set(5, calendar.get(5));
            } else if (this.mDate.after(calendar2)) {
                set(1, calendar2.get(1));
                set(2, calendar2.get(2));
                set(5, calendar2.get(5));
            }
        }

        public int clampDay(int i3) {
            int actualMaximum = this.mDate.getActualMaximum(5);
            return i3 > actualMaximum ? actualMaximum : i3;
        }

        public void clear() {
            this.mDate.clear();
            this.mIsIncomplete = false;
        }

        public int get(int i3) {
            if (this.mIsIncomplete && i3 != 5 && i3 != 2 && i3 == 1) {
                return Integer.MIN_VALUE;
            }
            return this.mDate.get(i3);
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

        public void set(int i3, int i4) {
            if (i3 != 1) {
                if (i3 != 2) {
                    if (i3 == 5) {
                        this.mDate.set(5, clampDay(i4));
                        return;
                    }
                    return;
                } else {
                    int i5 = this.mDate.get(1);
                    int i6 = this.mDate.get(5);
                    this.mDate.clear();
                    this.mDate.set(1, i5);
                    this.mDate.set(2, i4);
                    this.mDate.set(5, clampDay(i6));
                    return;
                }
            }
            if (i4 != Integer.MIN_VALUE) {
                this.mIsIncomplete = false;
                int i7 = this.mDate.get(2);
                int i8 = this.mDate.get(5);
                this.mDate.clear();
                this.mDate.set(1, i4);
                this.mDate.set(2, i7);
                this.mDate.set(5, clampDay(i8));
                return;
            }
            this.mIsIncomplete = true;
            int i9 = this.mDate.get(2);
            int i10 = this.mDate.get(5);
            this.mDate.clear();
            this.mDate.set(i3, 2020);
            this.mDate.set(2, i9);
            this.mDate.set(5, clampDay(i10));
        }

        public void setTimeInMillis(long j3) {
            this.mDate.setTimeInMillis(j3);
            this.mIsIncomplete = false;
        }

        public void setWith(IncompleteDate incompleteDate) {
            this.mDate.setTimeInMillis(incompleteDate.mDate.getTimeInMillis());
            this.mIsIncomplete = incompleteDate.mIsIncomplete;
        }

        public void set(int i3, int i4, int i5) {
            set(1, i3);
            set(2, i4);
            set(5, i5);
        }
    }
}