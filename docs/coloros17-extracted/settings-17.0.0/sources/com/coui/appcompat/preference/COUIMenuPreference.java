package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.poplist.COUIClickSelectMenu;
import com.coui.appcompat.poplist.PopupListItem;
import com.coui.appcompat.poplist.PreciseClickHelper;
import com.coui.appcompat.uiutil.AnimLevel;
import com.coui.appcompat.uiutil.UIUtil;
import com.support.preference.R;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes7.dex */
public class COUIMenuPreference extends COUIPreference {
    private static final String TAG = "COUIMenuPreference";
    private AnimLevel mBlurMinAnimLevel;
    private COUIClickSelectMenu mCouiClickSelectMenu;
    private boolean mEnableAddExtraWdith;
    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;
    private int[] mGroupIds;
    private boolean mHelperEnabled;
    private final AdapterView.OnItemClickListener mItemClickListener;
    private PreciseClickHelper.OnPreciseClickListener mListener;
    private int mMaxShowItemCount;
    private int mPopInputMethod;
    private ArrayList<PopupListItem> mPopupListItems;
    private boolean mReuseMenuWhenOffsetChanged;
    private ColorStateList mSelectItemColor;
    private String mSummary;
    private boolean mUseBackgroundBlur;
    private String mValue;
    private boolean mValueSet;

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIMenuPreference$1, reason: invalid class name */
    public class AnonymousClass1 implements AdapterView.OnItemClickListener {
        public AnonymousClass1() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i3, long j3) {
            int iAccess$000 = COUIMenuPreference.access$000(COUIMenuPreference.this, i3);
            if (iAccess$000 < 0 || COUIMenuPreference.access$100(COUIMenuPreference.this) == null || iAccess$000 >= COUIMenuPreference.access$100(COUIMenuPreference.this).length) {
                COUILog.d(COUIMenuPreference.TAG, "OnItemClick: skip value update, position=" + i3 + ", valueIndex=" + iAccess$000);
            } else {
                COUIMenuPreference cOUIMenuPreference = COUIMenuPreference.this;
                if (cOUIMenuPreference.callChangeListener(COUIMenuPreference.access$100(cOUIMenuPreference)[iAccess$000].toString())) {
                    COUIMenuPreference cOUIMenuPreference2 = COUIMenuPreference.this;
                    cOUIMenuPreference2.setValue(COUIMenuPreference.access$100(cOUIMenuPreference2)[iAccess$000].toString());
                }
            }
            COUIMenuPreference.access$200(COUIMenuPreference.this).dismiss();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIMenuPreference$2, reason: invalid class name */
    public class AnonymousClass2 implements View.OnAttachStateChangeListener {
        final /* synthetic */ PreferenceViewHolder val$holder;

        public AnonymousClass2(PreferenceViewHolder preferenceViewHolder) {
            this.val$holder = preferenceViewHolder;
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            this.val$holder.itemView.removeOnAttachStateChangeListener(this);
            if (COUIMenuPreference.access$200(COUIMenuPreference.this) == null || !COUIMenuPreference.access$200(COUIMenuPreference.this).getPopup().isShowing()) {
                return;
            }
            COUIMenuPreference.access$200(COUIMenuPreference.this).getPopup().dismiss();
        }
    }

    public COUIMenuPreference(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0);
    }

    public static /* synthetic */ int access$000(COUIMenuPreference cOUIMenuPreference, int i3) {
        return cOUIMenuPreference.getValueIndexByPosition(i3);
    }

    public static /* synthetic */ CharSequence[] access$100(COUIMenuPreference cOUIMenuPreference) {
        return cOUIMenuPreference.mEntryValues;
    }

    public static /* synthetic */ COUIClickSelectMenu access$200(COUIMenuPreference cOUIMenuPreference) {
        return cOUIMenuPreference.mCouiClickSelectMenu;
    }

    private int getValueIndex() {
        return findIndexOfValue(this.mValue);
    }

    private int getValueIndexByPosition(int i3) {
        PopupListItem popupListItem;
        int itemType;
        ArrayList<PopupListItem> arrayList = this.mPopupListItems;
        if (arrayList == null || i3 < 0 || i3 >= arrayList.size() || (popupListItem = this.mPopupListItems.get(i3)) == null) {
            return -1;
        }
        int i4 = -1;
        for (int i5 = 0; i5 <= i3 && i5 < this.mPopupListItems.size(); i5++) {
            PopupListItem popupListItem2 = this.mPopupListItems.get(i5);
            if (popupListItem2 != null && (itemType = popupListItem2.getItemType()) != 4 && itemType != 3 && itemType != 2) {
                i4++;
            }
        }
        int itemType2 = popupListItem.getItemType();
        if (itemType2 == 4 || itemType2 == 3 || itemType2 == 2) {
            return -1;
        }
        return i4;
    }

    public int findIndexOfValue(String str) {
        CharSequence[] charSequenceArr;
        if (str == null || (charSequenceArr = this.mEntryValues) == null) {
            return 0;
        }
        for (int length = charSequenceArr.length - 1; length >= 0; length--) {
            if (!TextUtils.isEmpty(this.mEntryValues[length]) && this.mEntryValues[length].equals(str)) {
                return length;
            }
        }
        return 0;
    }

    public CharSequence[] getEntries() {
        return this.mEntries;
    }

    public CharSequence[] getEntryValues() {
        return this.mEntryValues;
    }

    @Override // androidx.preference.Preference
    public CharSequence getSummary() {
        if (getSummaryProvider() != null) {
            return getSummaryProvider().provideSummary(this);
        }
        String value = getValue();
        CharSequence summary = super.getSummary();
        String str = this.mSummary;
        if (str != null) {
            if (value == null) {
                value = "";
            }
            String str2 = String.format(str, value);
            if (!TextUtils.equals(str2, summary)) {
                Log.w(TAG, "Setting a summary with a String formatting marker is no longer supported. You should use a SummaryProvider instead.");
                return str2;
            }
        }
        return summary;
    }

    public String getValue() {
        return this.mValue;
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        if (this.mCouiClickSelectMenu == null) {
            this.mCouiClickSelectMenu = new COUIClickSelectMenu(getContext(), preferenceViewHolder.itemView);
        }
        setReusePopup(this.mReuseMenuWhenOffsetChanged);
        this.mCouiClickSelectMenu.getPopup().setUseBackgroundBlur(this.mUseBackgroundBlur, this.mBlurMinAnimLevel);
        this.mCouiClickSelectMenu.getPopup().setInputMethodMode(this.mPopInputMethod);
        ColorStateList colorStateList = this.mSelectItemColor;
        if (colorStateList != null) {
            this.mCouiClickSelectMenu.registerForClickSelectItems(preferenceViewHolder.itemView, this.mPopupListItems, colorStateList.getDefaultColor());
        } else {
            this.mCouiClickSelectMenu.registerForClickSelectItems(preferenceViewHolder.itemView, this.mPopupListItems);
        }
        this.mCouiClickSelectMenu.setEnableAddExtraWidth(this.mEnableAddExtraWdith);
        this.mCouiClickSelectMenu.setHelperEnabled(this.mHelperEnabled);
        PreciseClickHelper.OnPreciseClickListener onPreciseClickListener = this.mListener;
        if (onPreciseClickListener != null) {
            this.mCouiClickSelectMenu.setOnPreciseClickListener(onPreciseClickListener);
        }
        this.mCouiClickSelectMenu.setOnItemClickListener(this.mItemClickListener);
        this.mCouiClickSelectMenu.setMaxShowItemCount(this.mMaxShowItemCount);
        preferenceViewHolder.itemView.addOnAttachStateChangeListener(new AnonymousClass2(preferenceViewHolder));
    }

    @Override // androidx.preference.Preference
    public Object onGetDefaultValue(TypedArray typedArray, int i3) {
        return typedArray.getString(i3);
    }

    @Override // androidx.preference.Preference
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable == null || !parcelable.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        if (this.mValueSet) {
            return;
        }
        setValue(savedState.mValue);
    }

    @Override // androidx.preference.Preference
    public Parcelable onSaveInstanceState() {
        Parcelable parcelableOnSaveInstanceState = super.onSaveInstanceState();
        if (isPersistent()) {
            return parcelableOnSaveInstanceState;
        }
        SavedState savedState = new SavedState(parcelableOnSaveInstanceState);
        savedState.mValue = getValue();
        return savedState;
    }

    @Override // androidx.preference.Preference
    public void onSetInitialValue(Object obj) {
        setValue(getPersistedString((String) obj));
    }

    public void setBlurMinAnimLevel(AnimLevel animLevel) {
        this.mBlurMinAnimLevel = animLevel;
        COUIClickSelectMenu cOUIClickSelectMenu = this.mCouiClickSelectMenu;
        if (cOUIClickSelectMenu != null) {
            cOUIClickSelectMenu.getPopup().setUseBackgroundBlur(this.mUseBackgroundBlur, this.mBlurMinAnimLevel);
        }
    }

    public void setEnableAddExtraWidth(boolean z2) {
        this.mEnableAddExtraWdith = z2;
    }

    @Override // androidx.preference.Preference
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        setMenuShowEnabled(z2);
    }

    public void setEntries(CharSequence[] charSequenceArr) {
        this.mEntries = charSequenceArr;
        this.mValueSet = false;
        if (charSequenceArr == null || charSequenceArr.length <= 0) {
            return;
        }
        this.mPopupListItems.clear();
        PopupListItem.Builder builder = new PopupListItem.Builder();
        for (int i3 = 0; i3 < charSequenceArr.length; i3++) {
            PopupListItem.Builder title = builder.reset().setTitle((String) charSequenceArr[i3]);
            int[] iArr = this.mGroupIds;
            title.setGroupId(iArr != null ? iArr[i3] : -1);
            this.mPopupListItems.add(builder.build());
        }
    }

    public void setEntryValues(CharSequence[] charSequenceArr) {
        this.mEntryValues = charSequenceArr;
        this.mValueSet = false;
        if (this.mEntries != null || charSequenceArr == null || charSequenceArr.length <= 0) {
            return;
        }
        this.mPopupListItems.clear();
        PopupListItem.Builder builder = new PopupListItem.Builder();
        for (int i3 = 0; i3 < charSequenceArr.length; i3++) {
            PopupListItem.Builder title = builder.reset().setTitle((String) charSequenceArr[i3]);
            int[] iArr = this.mGroupIds;
            title.setGroupId(iArr != null ? iArr[i3] : -1);
            this.mPopupListItems.add(builder.build());
        }
    }

    public void setMaxShowItemCount(int i3) {
        if (this.mMaxShowItemCount != i3) {
            this.mMaxShowItemCount = i3;
            notifyChanged();
        }
    }

    public void setMenuItemSelectColor(ColorStateList colorStateList) {
        this.mSelectItemColor = colorStateList;
    }

    public void setMenuShow(boolean z2) {
        COUIClickSelectMenu cOUIClickSelectMenu = this.mCouiClickSelectMenu;
        if (cOUIClickSelectMenu != null) {
            cOUIClickSelectMenu.setPopupState(z2);
        }
    }

    public void setMenuShowEnabled(boolean z2) {
        this.mHelperEnabled = z2;
        COUIClickSelectMenu cOUIClickSelectMenu = this.mCouiClickSelectMenu;
        if (cOUIClickSelectMenu != null) {
            cOUIClickSelectMenu.setHelperEnabled(z2);
        }
    }

    @Override // com.coui.appcompat.preference.COUIPreference
    public void setOnPreciseClickListener(PreciseClickHelper.OnPreciseClickListener onPreciseClickListener) {
        this.mListener = onPreciseClickListener;
    }

    public void setPopInputMethod(int i3) {
        this.mPopInputMethod = i3;
        COUIClickSelectMenu cOUIClickSelectMenu = this.mCouiClickSelectMenu;
        if (cOUIClickSelectMenu != null) {
            cOUIClickSelectMenu.getPopup().setInputMethodMode(this.mPopInputMethod);
        }
    }

    public void setPopupList(ArrayList<PopupListItem> arrayList) {
        this.mPopupListItems.clear();
        this.mPopupListItems.addAll(arrayList);
    }

    public void setReusePopup(boolean z2) {
        this.mReuseMenuWhenOffsetChanged = z2;
        COUIClickSelectMenu cOUIClickSelectMenu = this.mCouiClickSelectMenu;
        if (cOUIClickSelectMenu != null) {
            cOUIClickSelectMenu.getPopup().reuseMenuWhenOffsetChanged(z2);
        }
    }

    @Override // androidx.preference.Preference
    public void setSummary(CharSequence charSequence) {
        super.setSummary(charSequence);
        if (charSequence == null && this.mSummary != null) {
            this.mSummary = null;
        } else {
            if (charSequence == null || charSequence.equals(this.mSummary)) {
                return;
            }
            this.mSummary = charSequence.toString();
        }
    }

    public void setUseBackgroundBlur(boolean z2) {
        this.mUseBackgroundBlur = z2;
        COUIClickSelectMenu cOUIClickSelectMenu = this.mCouiClickSelectMenu;
        if (cOUIClickSelectMenu != null) {
            cOUIClickSelectMenu.getPopup().setUseBackgroundBlur(this.mUseBackgroundBlur, this.mBlurMinAnimLevel);
        }
    }

    public void setValue(String str) {
        if (TextUtils.equals(this.mValue, str) && this.mValueSet) {
            return;
        }
        this.mValue = str;
        this.mValueSet = true;
        if (this.mPopupListItems.size() > 0 && !TextUtils.isEmpty(str)) {
            for (int i3 = 0; i3 < this.mPopupListItems.size(); i3++) {
                PopupListItem popupListItem = this.mPopupListItems.get(i3);
                String title = popupListItem.getTitle();
                CharSequence[] charSequenceArr = this.mEntries;
                if (TextUtils.equals(title, charSequenceArr != null ? charSequenceArr[findIndexOfValue(str)] : str)) {
                    popupListItem.setChecked(true);
                } else {
                    popupListItem.setChecked(false);
                }
            }
        }
        persistString(str);
        notifyChanged();
    }

    public void setValueIndex(int i3) {
        CharSequence[] charSequenceArr = this.mEntryValues;
        if (charSequenceArr != null) {
            setValue(charSequenceArr[i3].toString());
        }
    }

    public static class SavedState extends Preference.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new AnonymousClass1();
        String mValue;

        /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIMenuPreference$SavedState$1, reason: invalid class name */
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
                return new SavedState(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int i3) {
                return new SavedState[i3];
            }
        }

        public SavedState(Parcel parcel) {
            super(parcel);
            this.mValue = parcel.readString();
        }

        @Override // android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i3) {
            super.writeToParcel(parcel, i3);
            parcel.writeString(this.mValue);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }
    }

    public COUIMenuPreference(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3);
        this.mItemClickListener = new AnonymousClass1();
        this.mPopupListItems = new ArrayList<>();
        this.mHelperEnabled = true;
        this.mEnableAddExtraWdith = true;
        this.mMaxShowItemCount = -1;
        this.mUseBackgroundBlur = false;
        this.mBlurMinAnimLevel = UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN;
        this.mReuseMenuWhenOffsetChanged = false;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUIMenuPreference, i3, 0);
        int i5 = R.styleable.COUIMenuPreference_android_entryValues;
        this.mEntryValues = TypedArrayUtils.getTextArray(typedArrayObtainStyledAttributes, i5, i5);
        int i6 = R.styleable.COUIMenuPreference_android_entries;
        this.mEntries = TypedArrayUtils.getTextArray(typedArrayObtainStyledAttributes, i6, i6);
        this.mMaxShowItemCount = typedArrayObtainStyledAttributes.getInteger(R.styleable.COUIMenuPreference_maxShowItemCount, -1);
        this.mPopInputMethod = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIMenuPreference_popInputMethod, 0);
        int resourceId = typedArrayObtainStyledAttributes.getResourceId(R.styleable.COUIMenuPreference_groupIds, 0);
        if (resourceId != 0) {
            this.mGroupIds = context.getResources().getIntArray(resourceId);
        }
        this.mValue = typedArrayObtainStyledAttributes.getString(R.styleable.COUIMenuPreference_android_value);
        typedArrayObtainStyledAttributes.recycle();
        setEntryValues(this.mEntryValues);
        setEntries(this.mEntries);
        setValue(this.mValue);
    }

    public void setEntries(int i3) {
        setEntries(getContext().getResources().getTextArray(i3));
    }

    public void setEntryValues(int i3) {
        setEntryValues(getContext().getResources().getTextArray(i3));
    }

    public COUIMenuPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiMenuPreferenceStyle);
    }

    public COUIMenuPreference(Context context) {
        this(context, null);
    }
}