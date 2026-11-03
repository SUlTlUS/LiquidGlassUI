package com.coui.appcompat.picker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.android.settings.datetime.TimeFormatTernaryPreferenceController;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.oplus.settings.custom.SettingsCustomUtils;
import com.support.picker.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/* JADX INFO: loaded from: classes7.dex */
public class COUITimePicker extends FrameLayout {
    private static final int HOURS_OF_A_DAY = 23;
    private static final int HOURS_OF_HALF_DAY = 12;
    private static final int LEAPYEAR_STEP_FOUR = 4;
    private static final int LEAPYEAR_STEP_FOUR_HUN = 400;
    private static final int LEAPYEAR_STEP_HUN = 100;
    private static final long MILLISECOND_A_DAY = 86400000;
    private static final int MINUTES_OF_A_HOUR = 59;
    private static final int MINUTES_STEP = 5;
    private static final int MIN_BACKGROUND_DIVIDER_HEIGHT = 1;
    private static final int START_YEAR = 1900;
    private static final String TAG = "COUITimePicker";
    private static final int TOTAL_YEAR = 100;
    private static final int YEAR_AMOUNT_LEAP = 366;
    private static final int YEAR_AMOUNT_NOT_LEAP = 365;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private Format f13401f;
    private int mAmPm;
    private int mBackgroundDividerHeight;
    private int mBackgroundLeft;
    private int mBackgroundRadius;
    private Calendar mCalendar;
    private Context mContext;
    private String[] mDateNamesTemp;
    private Calendar mDefaultCalendar;
    private Date mEndDate;
    private boolean mIsMinuteFiveStep;
    int mLeftPickerPosition;
    private int mMaxWidth;
    private OnTimeChangeListener mOnTimeChangeListener;
    private SimpleDateFormat mOutformatter;
    private COUINumberPicker mPickerAmPm;
    private COUINumberPicker mPickerDate;
    private COUINumberPicker mPickerHour;
    private LinearLayout mPickerLayout;
    private COUINumberPicker mPickerMinute;
    int mRightPickerPosition;
    private long mStartTime;
    private String[] mTextAMPM;
    private String[] mTextDates;
    private String mTextDay;
    private String mTextToday;
    private Calendar mTodayCalendar;
    private int mTodayDate;
    private int mTodayIndex;
    private int mTodayMonth;
    private int mTodayYear;

    /* JADX INFO: renamed from: com.coui.appcompat.picker.COUITimePicker$1, reason: invalid class name */
    public class AnonymousClass1 implements COUINumberPicker.OnValueChangeListener {
        public AnonymousClass1() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.OnValueChangeListener
        public void onValueChange(COUINumberPicker cOUINumberPicker, int i3, int i4) {
            COUITimePicker.access$002(COUITimePicker.this, cOUINumberPicker.getValue());
            COUITimePicker.access$100(COUITimePicker.this).set(9, cOUINumberPicker.getValue());
            if (COUITimePicker.access$200(COUITimePicker.this) != null) {
                OnTimeChangeListener onTimeChangeListenerAccess$200 = COUITimePicker.access$200(COUITimePicker.this);
                COUITimePicker cOUITimePicker = COUITimePicker.this;
                onTimeChangeListenerAccess$200.onTimeChange(cOUITimePicker, COUITimePicker.access$100(cOUITimePicker));
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.picker.COUITimePicker$2, reason: invalid class name */
    public class AnonymousClass2 implements COUINumberPicker.OnScrollingStopListener {
        public AnonymousClass2() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.OnScrollingStopListener
        public void onScrollingStop() {
            COUITimePicker cOUITimePicker = COUITimePicker.this;
            cOUITimePicker.announceForAccessibility(COUITimePicker.access$300(cOUITimePicker));
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.picker.COUITimePicker$3, reason: invalid class name */
    public class AnonymousClass3 implements COUINumberPicker.OnValueChangeListener {
        public AnonymousClass3() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.OnValueChangeListener
        public void onValueChange(COUINumberPicker cOUINumberPicker, int i3, int i4) {
            if (COUITimePicker.access$400(COUITimePicker.this) || COUITimePicker.access$000(COUITimePicker.this) == 0) {
                COUITimePicker.access$100(COUITimePicker.this).set(11, cOUINumberPicker.getValue());
            } else if (COUITimePicker.access$000(COUITimePicker.this) == 1) {
                if (cOUINumberPicker.getValue() != 12) {
                    COUITimePicker.access$100(COUITimePicker.this).set(11, cOUINumberPicker.getValue() + 12);
                } else {
                    COUITimePicker.access$100(COUITimePicker.this).set(11, 0);
                }
            }
            if (!COUITimePicker.access$400(COUITimePicker.this) && cOUINumberPicker.getValue() == 12) {
                COUITimePicker cOUITimePicker = COUITimePicker.this;
                COUITimePicker.access$002(cOUITimePicker, 1 - COUITimePicker.access$000(cOUITimePicker));
                COUITimePicker.access$500(COUITimePicker.this).setValue(COUITimePicker.access$000(COUITimePicker.this));
            }
            if (COUITimePicker.access$200(COUITimePicker.this) != null) {
                OnTimeChangeListener onTimeChangeListenerAccess$200 = COUITimePicker.access$200(COUITimePicker.this);
                COUITimePicker cOUITimePicker2 = COUITimePicker.this;
                onTimeChangeListenerAccess$200.onTimeChange(cOUITimePicker2, COUITimePicker.access$100(cOUITimePicker2));
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.picker.COUITimePicker$4, reason: invalid class name */
    public class AnonymousClass4 implements COUINumberPicker.OnScrollingStopListener {
        public AnonymousClass4() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.OnScrollingStopListener
        public void onScrollingStop() {
            COUITimePicker cOUITimePicker = COUITimePicker.this;
            cOUITimePicker.announceForAccessibility(COUITimePicker.access$300(cOUITimePicker));
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.picker.COUITimePicker$5, reason: invalid class name */
    public class AnonymousClass5 implements COUINumberPicker.OnValueChangeListener {
        public AnonymousClass5() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.OnValueChangeListener
        public void onValueChange(COUINumberPicker cOUINumberPicker, int i3, int i4) {
            if (COUITimePicker.access$600(COUITimePicker.this)) {
                COUITimePicker.access$100(COUITimePicker.this).set(12, cOUINumberPicker.getValue() * 5);
            } else {
                COUITimePicker.access$100(COUITimePicker.this).set(12, cOUINumberPicker.getValue());
            }
            if (COUITimePicker.access$200(COUITimePicker.this) != null) {
                OnTimeChangeListener onTimeChangeListenerAccess$200 = COUITimePicker.access$200(COUITimePicker.this);
                COUITimePicker cOUITimePicker = COUITimePicker.this;
                onTimeChangeListenerAccess$200.onTimeChange(cOUITimePicker, COUITimePicker.access$100(cOUITimePicker));
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.picker.COUITimePicker$6, reason: invalid class name */
    public class AnonymousClass6 implements COUINumberPicker.OnScrollingStopListener {
        public AnonymousClass6() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.OnScrollingStopListener
        public void onScrollingStop() {
            COUITimePicker cOUITimePicker = COUITimePicker.this;
            cOUITimePicker.announceForAccessibility(COUITimePicker.access$300(cOUITimePicker));
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.picker.COUITimePicker$7, reason: invalid class name */
    public class AnonymousClass7 implements COUINumberPicker.OnValueChangeListener {
        public AnonymousClass7() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.OnValueChangeListener
        public void onValueChange(COUINumberPicker cOUINumberPicker, int i3, int i4) {
            Date dateAccess$700 = COUITimePicker.access$700(COUITimePicker.this, cOUINumberPicker.getValue());
            if (dateAccess$700 != null) {
                COUITimePicker.access$100(COUITimePicker.this).set(2, dateAccess$700.getMonth());
                COUITimePicker.access$100(COUITimePicker.this).set(5, dateAccess$700.getDate());
                COUITimePicker.access$100(COUITimePicker.this).set(1, dateAccess$700.getYear() + 1900);
                if (COUITimePicker.access$200(COUITimePicker.this) != null) {
                    OnTimeChangeListener onTimeChangeListenerAccess$200 = COUITimePicker.access$200(COUITimePicker.this);
                    COUITimePicker cOUITimePicker = COUITimePicker.this;
                    onTimeChangeListenerAccess$200.onTimeChange(cOUITimePicker, COUITimePicker.access$100(cOUITimePicker));
                }
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.picker.COUITimePicker$8, reason: invalid class name */
    public class AnonymousClass8 implements COUINumberPicker.OnScrollingStopListener {
        public AnonymousClass8() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.OnScrollingStopListener
        public void onScrollingStop() {
            COUITimePicker cOUITimePicker = COUITimePicker.this;
            cOUITimePicker.announceForAccessibility(COUITimePicker.access$300(cOUITimePicker));
        }
    }

    public class Format implements COUINumberPicker.Formatter {
        public Format() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.Formatter
        public String format(int i3) {
            int i4 = i3 - 1;
            COUITimePicker.access$900(COUITimePicker.this)[i4] = COUITimePicker.access$800(COUITimePicker.this, i3);
            if (i3 == COUITimePicker.access$1000(COUITimePicker.this)) {
                COUITimePicker.access$1100(COUITimePicker.this)[i4] = COUITimePicker.access$1200(COUITimePicker.this);
                return COUITimePicker.access$1100(COUITimePicker.this)[i4];
            }
            if (!Locale.getDefault().getLanguage().equals("zh")) {
                return DateUtils.formatDateTime(COUITimePicker.this.getContext(), COUITimePicker.access$1400(COUITimePicker.this).getTime(), 524314);
            }
            return new SimpleDateFormat("MMMdd" + COUITimePicker.access$1300(COUITimePicker.this) + " E", Locale.getDefault()).format(Long.valueOf(COUITimePicker.access$1400(COUITimePicker.this).getTime()));
        }
    }

    public interface OnTimeChangeListener {
        void onTimeChange(View view, Calendar calendar);
    }

    public COUITimePicker(Context context) {
        this(context, null);
    }

    public static /* synthetic */ int access$000(COUITimePicker cOUITimePicker) {
        return cOUITimePicker.mAmPm;
    }

    public static /* synthetic */ int access$002(COUITimePicker cOUITimePicker, int i3) {
        cOUITimePicker.mAmPm = i3;
        return i3;
    }

    public static /* synthetic */ Calendar access$100(COUITimePicker cOUITimePicker) {
        return cOUITimePicker.mCalendar;
    }

    public static /* synthetic */ int access$1000(COUITimePicker cOUITimePicker) {
        return cOUITimePicker.mTodayIndex;
    }

    public static /* synthetic */ String[] access$1100(COUITimePicker cOUITimePicker) {
        return cOUITimePicker.mTextDates;
    }

    public static /* synthetic */ String access$1200(COUITimePicker cOUITimePicker) {
        return cOUITimePicker.mTextToday;
    }

    public static /* synthetic */ String access$1300(COUITimePicker cOUITimePicker) {
        return cOUITimePicker.mTextDay;
    }

    public static /* synthetic */ Date access$1400(COUITimePicker cOUITimePicker) {
        return cOUITimePicker.mEndDate;
    }

    public static /* synthetic */ OnTimeChangeListener access$200(COUITimePicker cOUITimePicker) {
        return cOUITimePicker.mOnTimeChangeListener;
    }

    public static /* synthetic */ String access$300(COUITimePicker cOUITimePicker) {
        return cOUITimePicker.reorderUtterance();
    }

    public static /* synthetic */ boolean access$400(COUITimePicker cOUITimePicker) {
        return cOUITimePicker.is24Hours();
    }

    public static /* synthetic */ COUINumberPicker access$500(COUITimePicker cOUITimePicker) {
        return cOUITimePicker.mPickerAmPm;
    }

    public static /* synthetic */ boolean access$600(COUITimePicker cOUITimePicker) {
        return cOUITimePicker.mIsMinuteFiveStep;
    }

    public static /* synthetic */ Date access$700(COUITimePicker cOUITimePicker, int i3) {
        return cOUITimePicker.getDateFromValue(i3);
    }

    public static /* synthetic */ String access$800(COUITimePicker cOUITimePicker, int i3) {
        return cOUITimePicker.getDateYMDW(i3);
    }

    public static /* synthetic */ String[] access$900(COUITimePicker cOUITimePicker) {
        return cOUITimePicker.mDateNamesTemp;
    }

    private String deduplicate(String str) {
        String strValueOf = String.valueOf(str.charAt(0));
        for (int i3 = 1; i3 < str.length(); i3++) {
            char cCharAt = str.charAt(i3);
            if (cCharAt != str.charAt(i3 - 1)) {
                strValueOf = strValueOf + cCharAt;
            }
        }
        return strValueOf;
    }

    private Date getDateFromValue(int i3) {
        try {
            return this.mOutformatter.parse(this.mDateNamesTemp[i3 - 1]);
        } catch (ParseException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    private String getDateYMDW(int i3) {
        this.mEndDate.setTime(this.mStartTime + (((long) i3) * 86400000));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.mEndDate);
        if (isToday(calendar.get(1), calendar.get(2), calendar.get(5))) {
            this.mTodayIndex = i3;
        } else {
            this.mTodayIndex = -1;
        }
        return this.mOutformatter.format(Long.valueOf(this.mEndDate.getTime()));
    }

    private int getDaysAmountOfYear(int i3) {
        return isLeapYear(i3) ? YEAR_AMOUNT_LEAP : YEAR_AMOUNT_NOT_LEAP;
    }

    private boolean is24Hours() {
        String string = Settings.System.getString(this.mContext.getContentResolver(), "time_12_24");
        return string != null && string.equals(TimeFormatTernaryPreferenceController.HOURS_24);
    }

    private boolean isLeapYear(int i3) {
        return (i3 % 4 == 0 && i3 % 100 != 0) || i3 % 400 == 0;
    }

    private boolean isToday(int i3, int i4, int i5) {
        return i3 == this.mTodayYear && i4 == this.mTodayMonth && i5 == this.mTodayDate;
    }

    private void measureChildConstrained(View view, int i3, int i4, float f3) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (f3 < 1.0f) {
            marginLayoutParams.width = (int) (marginLayoutParams.width * f3);
        }
        view.measure(ViewGroup.getChildMeasureSpec(i3, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin, marginLayoutParams.width), ViewGroup.getChildMeasureSpec(i4, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin, marginLayoutParams.height));
    }

    /* JADX WARN: Code duplicated, block: B:32:0x007d A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:33:0x007f  */
    /* JADX WARN: Code duplicated, block: B:34:0x008b  */
    private void reorderSpinners() {
        String strDeduplicate = deduplicate(DateFormat.getBestDateTimePattern(Locale.getDefault(), "yyyyMMMddhm"));
        ViewGroup viewGroup = (ViewGroup) this.mPickerDate.getParent();
        viewGroup.removeAllViews();
        ArrayList arrayList = new ArrayList();
        int i3 = 0;
        boolean z2 = false;
        boolean z3 = false;
        while (i3 < strDeduplicate.length()) {
            char cCharAt = strDeduplicate.charAt(i3);
            if (cCharAt == '\'') {
                int i4 = i3 + 1;
                if (i4 >= strDeduplicate.length() || strDeduplicate.charAt(i4) != '\'') {
                    z2 = !z2;
                } else {
                    i3 = i4;
                }
            } else if (!z2) {
                if (cCharAt == 'K') {
                    viewGroup.addView(this.mPickerHour);
                    arrayList.add("h");
                } else if (cCharAt == 'M') {
                    if (!z3) {
                        viewGroup.addView(this.mPickerDate);
                        arrayList.add("D");
                        z3 = true;
                    }
                } else if (cCharAt == 'a') {
                    viewGroup.addView(this.mPickerAmPm);
                    arrayList.add(SettingsCustomUtils.CUSTOMIZE_LIST_TAG_ADMIN);
                } else if (cCharAt == 'd') {
                    if (!z3) {
                        viewGroup.addView(this.mPickerDate);
                        arrayList.add("D");
                        z3 = true;
                    }
                } else if (cCharAt == 'h') {
                    viewGroup.addView(this.mPickerHour);
                    arrayList.add("h");
                } else if (cCharAt == 'm') {
                    viewGroup.addView(this.mPickerMinute);
                    arrayList.add("m");
                } else if (cCharAt == 'y') {
                    if (!z3) {
                        viewGroup.addView(this.mPickerDate);
                        arrayList.add("D");
                        z3 = true;
                    }
                }
                if (!is24Hours()) {
                    if (this.mLeftPickerPosition == -1) {
                        this.mLeftPickerPosition = viewGroup.getChildCount() - 1;
                    }
                    this.mRightPickerPosition = viewGroup.getChildCount() - 1;
                } else if (viewGroup.getChildAt(viewGroup.getChildCount() - 1) != this.mPickerAmPm) {
                    if (this.mLeftPickerPosition == -1) {
                        this.mLeftPickerPosition = viewGroup.getChildCount() - 1;
                    }
                    this.mRightPickerPosition = viewGroup.getChildCount() - 1;
                }
            }
            i3++;
        }
        if (isLayoutRtl()) {
            int i5 = this.mLeftPickerPosition;
            this.mLeftPickerPosition = this.mRightPickerPosition;
            this.mRightPickerPosition = i5;
        }
    }

    /* JADX WARN: Code duplicated, block: B:28:0x0083 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:29:0x0085  */
    /* JADX WARN: Code duplicated, block: B:30:0x00a7  */
    /* JADX WARN: Code duplicated, block: B:38:0x00c7 A[SYNTHETIC] */
    /* JADX WARN: Instruction removed from duplicated block: B:29:0x0085, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:30:0x00a7, please report this as an issue */
    private String reorderUtterance() {
        String strDeduplicate = deduplicate(DateFormat.getBestDateTimePattern(Locale.getDefault(), "yyyyMMMddhm"));
        String str = "";
        boolean z2 = false;
        for (int i3 = 0; i3 < strDeduplicate.length(); i3++) {
            char cCharAt = strDeduplicate.charAt(i3);
            if (cCharAt == 'K') {
                str = str + this.mPickerHour.getCurrentText() + this.mContext.getString(R.string.coui_hour_abbreviation);
            } else if (cCharAt == 'M') {
                if (!z2) {
                    str = str + this.f13401f.format(this.mPickerDate.getValue()) + ",";
                    z2 = true;
                }
            } else if (cCharAt != 'a') {
                if (cCharAt == 'd') {
                    if (!z2) {
                        str = str + this.f13401f.format(this.mPickerDate.getValue()) + ",";
                        z2 = true;
                    }
                } else if (cCharAt == 'h') {
                    str = str + this.mPickerHour.getCurrentText() + this.mContext.getString(R.string.coui_hour_abbreviation);
                } else if (cCharAt == 'm') {
                    str = str + this.mPickerMinute.getCurrentText() + this.mContext.getString(R.string.coui_minute_abbreviation);
                } else if (cCharAt == 'y') {
                    if (!z2) {
                        str = str + this.f13401f.format(this.mPickerDate.getValue()) + ",";
                        z2 = true;
                    }
                }
            } else if (!is24Hours()) {
                str = str + (is24Hours() ? this.mTextAMPM[0] : this.mTextAMPM[1]);
            }
        }
        return str;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        if (!is24Hours()) {
            this.mBackgroundLeft = 0;
        }
        Paint paint = new Paint();
        paint.setColor(this.mPickerDate.getBackgroundColor());
        int height = (int) ((getHeight() / 2.0f) - this.mBackgroundRadius);
        canvas.drawRect(this.mBackgroundLeft, height, getWidth() - this.mBackgroundLeft, height + this.mBackgroundDividerHeight, paint);
        int height2 = (int) ((getHeight() / 2.0f) + this.mBackgroundRadius);
        canvas.drawRect(this.mBackgroundLeft, height2, getWidth() - this.mBackgroundLeft, height2 + this.mBackgroundDividerHeight, paint);
        super.dispatchDraw(canvas);
    }

    public COUINumberPicker getPickerAmPm() {
        return this.mPickerAmPm;
    }

    public COUINumberPicker getPickerDate() {
        return this.mPickerDate;
    }

    public COUINumberPicker getPickerHour() {
        return this.mPickerHour;
    }

    public COUINumberPicker getPickerMinute() {
        return this.mPickerMinute;
    }

    public View getTimePicker() {
        int i3;
        StringBuilder sb;
        Calendar calendar = this.mDefaultCalendar;
        if (calendar != null) {
            i3 = calendar.get(1);
        } else {
            calendar = this.mTodayCalendar;
            i3 = calendar.get(1);
        }
        int i4 = i3;
        int i5 = calendar.get(2);
        int i6 = i5 + 1;
        int i7 = calendar.get(5);
        int i8 = calendar.get(11);
        int i9 = calendar.get(9);
        int i10 = calendar.get(12);
        this.mCalendar.setTimeZone(calendar.getTimeZone());
        this.mOutformatter.setTimeZone(calendar.getTimeZone());
        this.mCalendar.set(i4, i5, i7, i8, i10);
        int daysAmountOfYear = 36500;
        for (int i11 = 0; i11 < 100; i11++) {
            daysAmountOfYear += getDaysAmountOfYear((i4 - 50) + i11);
        }
        int daysAmountOfYear2 = 0;
        for (int i12 = 0; i12 < 50; i12++) {
            daysAmountOfYear2 += getDaysAmountOfYear((i4 - 50) + i12);
        }
        String[] strArr = new String[daysAmountOfYear];
        this.mTextDates = strArr;
        this.mDateNamesTemp = (String[]) strArr.clone();
        if (i6 > 2 && !isLeapYear(i4 - 50) && isLeapYear(i4)) {
            daysAmountOfYear2++;
        }
        if (i6 > 2 && isLeapYear(i4 - 50)) {
            daysAmountOfYear2--;
        }
        int i13 = daysAmountOfYear;
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeZone(calendar.getTimeZone());
        calendar2.set(i4, i5, i7, i8, i10);
        if (isLeapYear(i4) && i6 == 2 && i7 == 29) {
            calendar2.add(5, 1);
        }
        calendar2.add(1, -50);
        this.mStartTime = calendar2.getTimeInMillis();
        this.mEndDate = new Date();
        if (is24Hours()) {
            this.mPickerHour.setMaxValue(23);
            this.mPickerHour.setMinValue(0);
            this.mPickerHour.setTwoDigitFormatter();
            this.mPickerAmPm.setVisibility(8);
        } else {
            this.mPickerHour.setMaxValue(12);
            this.mPickerHour.setMinValue(1);
            this.mPickerAmPm.setMaxValue(this.mTextAMPM.length - 1);
            this.mPickerAmPm.setMinValue(0);
            this.mPickerAmPm.setDisplayedValues(this.mTextAMPM);
            this.mPickerAmPm.setVisibility(0);
            this.mPickerAmPm.setWrapSelectorWheel(false);
        }
        this.mPickerHour.setWrapSelectorWheel(true);
        if (is24Hours()) {
            this.mPickerHour.setValue(i8);
        } else {
            if (i9 > 0) {
                this.mPickerHour.setValue(i8 - 12);
            } else {
                this.mPickerHour.setValue(i8);
            }
            this.mPickerAmPm.setValue(i9);
            this.mAmPm = i9;
        }
        this.mPickerAmPm.setOnValueChangedListener(new AnonymousClass1());
        this.mPickerAmPm.setOnScrollingStopListener(new AnonymousClass2());
        this.mPickerHour.setOnValueChangedListener(new AnonymousClass3());
        this.mPickerHour.setOnScrollingStopListener(new AnonymousClass4());
        this.mPickerMinute.setMinValue(0);
        if (this.mIsMinuteFiveStep) {
            this.mPickerMinute.setMinValue(0);
            this.mPickerMinute.setMaxValue(11);
            String[] strArr2 = new String[12];
            int i14 = 0;
            for (int i15 = 12; i14 < i15; i15 = 12) {
                int i16 = i14 * 5;
                if (i16 < 10) {
                    sb = new StringBuilder();
                    sb.append("0");
                    sb.append(i16);
                } else {
                    sb = new StringBuilder();
                    sb.append(i16);
                    sb.append("");
                }
                strArr2[i14] = sb.toString();
                i14++;
            }
            this.mPickerMinute.setDisplayedValues(strArr2);
            int i17 = i10 / 5;
            this.mPickerMinute.setValue(i17);
            this.mCalendar.set(12, Integer.parseInt(strArr2[i17]));
        } else {
            this.mPickerMinute.setMaxValue(59);
            this.mPickerMinute.setValue(i10);
        }
        this.mPickerMinute.setTwoDigitFormatter();
        this.mPickerMinute.setWrapSelectorWheel(true);
        this.mPickerMinute.setOnValueChangedListener(new AnonymousClass5());
        this.mPickerMinute.setOnScrollingStopListener(new AnonymousClass6());
        this.mPickerDate.setMinValue(1);
        this.mPickerDate.setMaxValue(i13);
        this.mPickerDate.setWrapSelectorWheel(false);
        this.mPickerDate.setValue(daysAmountOfYear2);
        Format format = new Format();
        this.f13401f = format;
        this.mPickerDate.setFormatter(format);
        this.mPickerDate.setOnValueChangedListener(new AnonymousClass7());
        this.mPickerDate.setOnScrollingStopListener(new AnonymousClass8());
        return this;
    }

    public boolean isLayoutRtl() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1;
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
        this.mPickerMinute.clearNumberPickerPadding();
        this.mPickerHour.clearNumberPickerPadding();
        this.mPickerDate.clearNumberPickerPadding();
        this.mPickerAmPm.clearNumberPickerPadding();
        float f3 = size / (((this.mPickerMinute.getLayoutParams().width + this.mPickerHour.getLayoutParams().width) + this.mPickerDate.getLayoutParams().width) + this.mPickerAmPm.getLayoutParams().width);
        measureChildConstrained(this.mPickerMinute, i3, i4, f3);
        measureChildConstrained(this.mPickerHour, i3, i4, f3);
        measureChildConstrained(this.mPickerDate, i3, i4, f3);
        measureChildConstrained(this.mPickerAmPm, i3, i4, f3);
        int measuredWidth = ((((size - this.mPickerMinute.getMeasuredWidth()) - this.mPickerHour.getMeasuredWidth()) - this.mPickerDate.getMeasuredWidth()) - (is24Hours() ? 0 : this.mPickerAmPm.getMeasuredWidth())) / 2;
        if (this.mPickerLayout.getChildAt(this.mLeftPickerPosition) instanceof COUINumberPicker) {
            ((COUINumberPicker) this.mPickerLayout.getChildAt(this.mLeftPickerPosition)).setNumberPickerPaddingLeft(measuredWidth);
        }
        if (this.mPickerLayout.getChildAt(this.mRightPickerPosition) instanceof COUINumberPicker) {
            ((COUINumberPicker) this.mPickerLayout.getChildAt(this.mRightPickerPosition)).setNumberPickerPaddingRight(measuredWidth);
        }
        super.onMeasure(iMakeMeasureSpec, i4);
    }

    public void refresh() {
        COUINumberPicker cOUINumberPicker = this.mPickerDate;
        if (cOUINumberPicker != null) {
            cOUINumberPicker.refresh();
        }
        COUINumberPicker cOUINumberPicker2 = this.mPickerHour;
        if (cOUINumberPicker2 != null) {
            cOUINumberPicker2.refresh();
        }
        COUINumberPicker cOUINumberPicker3 = this.mPickerMinute;
        if (cOUINumberPicker3 != null) {
            cOUINumberPicker3.refresh();
        }
        COUINumberPicker cOUINumberPicker4 = this.mPickerAmPm;
        if (cOUINumberPicker4 != null) {
            cOUINumberPicker4.refresh();
        }
    }

    public void scrollForceFinished() {
        COUINumberPicker cOUINumberPicker = this.mPickerDate;
        if (cOUINumberPicker != null) {
            cOUINumberPicker.scrollForceFinished();
        }
        COUINumberPicker cOUINumberPicker2 = this.mPickerHour;
        if (cOUINumberPicker2 != null) {
            cOUINumberPicker2.scrollForceFinished();
        }
        COUINumberPicker cOUINumberPicker3 = this.mPickerMinute;
        if (cOUINumberPicker3 != null) {
            cOUINumberPicker3.scrollForceFinished();
        }
        if (this.mPickerAmPm == null || is24Hours()) {
            return;
        }
        this.mPickerAmPm.scrollForceFinished();
    }

    public void setMinuteStepToFive() {
        this.mIsMinuteFiveStep = true;
    }

    public void setNormalTextColor(int i3) {
        COUINumberPicker cOUINumberPicker = this.mPickerDate;
        if (cOUINumberPicker != null) {
            cOUINumberPicker.setNormalTextColor(i3);
        }
        COUINumberPicker cOUINumberPicker2 = this.mPickerHour;
        if (cOUINumberPicker2 != null) {
            cOUINumberPicker2.setNormalTextColor(i3);
        }
        COUINumberPicker cOUINumberPicker3 = this.mPickerMinute;
        if (cOUINumberPicker3 != null) {
            cOUINumberPicker3.setNormalTextColor(i3);
        }
        COUINumberPicker cOUINumberPicker4 = this.mPickerAmPm;
        if (cOUINumberPicker4 != null) {
            cOUINumberPicker4.setNormalTextColor(i3);
        }
    }

    public void setOnTimeChangeListener(OnTimeChangeListener onTimeChangeListener) {
        this.mOnTimeChangeListener = onTimeChangeListener;
    }

    @Deprecated
    public void setTimePicker(int i3, Calendar calendar) {
        this.mDefaultCalendar = calendar;
        getTimePicker();
    }

    public void setUnitVisible(boolean z2) {
        if (z2) {
            this.mPickerHour.setUnitText(getContext().getString(R.string.coui_hour_abbreviation));
            this.mPickerMinute.setUnitText(getContext().getString(R.string.coui_minute_abbreviation));
        } else {
            this.mPickerHour.setUnitText("");
            this.mPickerMinute.setUnitText("");
        }
    }

    public void setVibrateIntensity(float f3) {
        this.mPickerDate.setVibrateIntensity(f3);
        this.mPickerHour.setVibrateIntensity(f3);
        this.mPickerMinute.setVibrateIntensity(f3);
        this.mPickerAmPm.setVibrateIntensity(f3);
    }

    public void setVibrateLevel(int i3) {
        this.mPickerDate.setVibrateLevel(i3);
        this.mPickerHour.setVibrateLevel(i3);
        this.mPickerMinute.setVibrateLevel(i3);
        this.mPickerAmPm.setVibrateLevel(i3);
    }

    public COUITimePicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiTimePickerStyle);
    }

    public COUITimePicker(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, R.style.TimePickerStyle);
    }

    public void setTimePicker(Calendar calendar) {
        this.mDefaultCalendar = calendar;
        getTimePicker();
    }

    public COUITimePicker(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mTodayIndex = -1;
        this.mAmPm = -1;
        this.mLeftPickerPosition = -1;
        this.mRightPickerPosition = -1;
        COUIDarkModeUtil.setForceDarkAllow(this, false);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUIPickersCommonAttrs, i3, i4);
        this.mMaxWidth = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIPickersCommonAttrs_couiPickersMaxWidth, 0);
        typedArrayObtainStyledAttributes.recycle();
        this.mContext = context;
        this.mTextAMPM = context.getResources().getStringArray(R.array.coui_time_picker_ampm);
        this.mTextToday = this.mContext.getResources().getString(R.string.coui_time_picker_today);
        this.mTextDay = this.mContext.getResources().getString(R.string.coui_time_picker_day);
        this.mCalendar = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        this.mTodayCalendar = calendar;
        this.mTodayYear = calendar.get(1);
        this.mTodayMonth = this.mTodayCalendar.get(2);
        this.mTodayDate = this.mTodayCalendar.get(5);
        this.mOutformatter = new SimpleDateFormat("yyyy MMM dd" + this.mTextDay + " E", Locale.getDefault());
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this.mContext).inflate(R.layout.coui_time_picker, (ViewGroup) this, true);
        this.mPickerDate = (COUINumberPicker) viewGroup.findViewById(R.id.coui_time_picker_date);
        this.mPickerHour = (COUINumberPicker) viewGroup.findViewById(R.id.coui_time_picker_hour);
        this.mPickerMinute = (COUINumberPicker) viewGroup.findViewById(R.id.coui_time_picker_minute);
        this.mPickerAmPm = (COUINumberPicker) viewGroup.findViewById(R.id.coui_time_picker_ampm);
        this.mPickerLayout = (LinearLayout) viewGroup.findViewById(R.id.pickers);
        this.mBackgroundRadius = context.getResources().getDimensionPixelOffset(R.dimen.coui_selected_background_radius);
        this.mBackgroundLeft = context.getResources().getDimensionPixelOffset(R.dimen.coui_selected_background_horizontal_padding);
        this.mBackgroundDividerHeight = Math.max(getResources().getDimensionPixelOffset(R.dimen.coui_number_picker_background_divider_height), 1);
        if (!Locale.getDefault().getLanguage().equals("zh") && !Locale.getDefault().getLanguage().equals("en")) {
            this.mPickerDate.getLayoutParams().width = getResources().getDimensionPixelOffset(R.dimen.coui_number_picker_width_biggest);
        }
        reorderSpinners();
        COUINumberPicker cOUINumberPicker = this.mPickerHour;
        if (cOUINumberPicker != null && cOUINumberPicker.isAccessibilityEnable()) {
            this.mPickerHour.addTalkbackSuffix(context.getResources().getString(R.string.coui_hour_abbreviation));
            COUINumberPicker cOUINumberPicker2 = this.mPickerMinute;
            if (cOUINumberPicker2 != null) {
                cOUINumberPicker2.addTalkbackSuffix(context.getResources().getString(R.string.coui_minute_abbreviation));
            }
            COUINumberPicker cOUINumberPicker3 = this.mPickerAmPm;
            if (cOUINumberPicker3 != null) {
                cOUINumberPicker3.addTalkbackSuffix(context.getResources().getString(R.string.coui_minute_abbreviation));
            }
        }
        setImportantForAccessibility(1);
    }
}