package com.coui.appcompat.preference;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import androidx.preference.MultiSelectListPreferenceDialogFragmentCompat;
import com.coui.appcompat.dialog.COUIAlertDialogBuilder;
import com.coui.appcompat.dialog.adapter.ChoiceListAdapter;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.uiutil.AnimLevel;
import com.coui.appcompat.uiutil.UIUtil;
import com.support.dialog.R;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/* JADX INFO: loaded from: classes7.dex */
public class COUIMultiSelectListPreferenceDialogFragment extends MultiSelectListPreferenceDialogFragmentCompat {
    private static final String SAVE_STATE_BLUR_ANIM_LAVEL = "ListPreferenceDialogFragment.SAVE_STATE_BLUR_ANIM_LAVEL";
    private static final String SAVE_STATE_BLUR_BACKGROUND = "ListPreferenceDialogFragment.SAVE_STATE_BLUR_BACKGROUND";
    private static final String SAVE_STATE_ENTRIES = "MultiSelectListPreferenceDialogFragmentCompat.entries";
    private static final String SAVE_STATE_ENTRY_VALUES = "MultiSelectListPreferenceDialogFragmentCompat.entryValues";
    private static final String SAVE_STATE_FOLLOWHAND = "ListPreferenceDialogFragment.SAVE_STATE_FOLLOWHAND";
    private static final String SAVE_STATE_MESSAGE = "COUIMultiSelectListPreferenceDialogFragment.message";
    private static final String SAVE_STATE_NEGATIVE_BUTTON_TEXT = "COUIMultiSelectListPreferenceDialogFragment.negativeButtonTextitle";
    private static final String SAVE_STATE_POSITIVE_BUTTON_TEXT = "COUIMultiSelectListPreferenceDialogFragment.positiveButtonText";
    private static final String SAVE_STATE_SUMMARYS = "COUIMultiSelectListPreferenceDialogFragment.summarys";
    private static final String SAVE_STATE_TITLE = "COUIMultiSelectListPreferenceDialogFragment.title";
    private static final String SAVE_STATE_VALUES = "COUIMultiSelectListPreferenceDialogFragment.values";
    private static final String TAG = "COUIMultiDialogFragment";
    private ChoiceListAdapter mAdapter;
    private COUIAlertDialogBuilder mBuilder;
    private boolean[] mCheckboxStates;
    private CharSequence mDialogMessage;
    private CharSequence mDialogTitle;
    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;
    private CharSequence mNegativeButtonText;
    private CharSequence mPositiveButtonText;
    private COUIMultiSelectListPreference mPreference;
    private CharSequence[] mSummaries;
    private boolean mIfFollowHand = true;
    private boolean mBlurBackground = false;
    private AnimLevel mBlurMinAnimLevel = UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN;

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIMultiSelectListPreferenceDialogFragment$1, reason: invalid class name */
    public class AnonymousClass1 extends ChoiceListAdapter {
        public AnonymousClass1(Context context, int i3, CharSequence[] charSequenceArr, CharSequence[] charSequenceArr2, boolean[] zArr, boolean z2) {
            super(context, i3, charSequenceArr, charSequenceArr2, zArr, z2);
        }

        @Override // com.coui.appcompat.dialog.adapter.ChoiceListAdapter, android.widget.Adapter
        public View getView(int i3, View view, ViewGroup viewGroup) {
            View view2 = super.getView(i3, view, viewGroup);
            View viewFindViewById = view2.findViewById(R.id.item_divider);
            int count = getCount();
            if (viewFindViewById != null) {
                if (count != 1 && i3 != count - 1) {
                    viewFindViewById.setVisibility(0);
                    return view2;
                }
                viewFindViewById.setVisibility(8);
            }
            return view2;
        }
    }

    private boolean[] getCheckboxStatesFromValues(Set<String> set) {
        boolean[] zArr = new boolean[this.mEntries.length];
        int i3 = 0;
        while (true) {
            CharSequence[] charSequenceArr = this.mEntries;
            if (i3 >= charSequenceArr.length) {
                return zArr;
            }
            zArr[i3] = set.contains(charSequenceArr[i3].toString());
            i3++;
        }
    }

    private Set<String> getSelectedValues() {
        HashSet hashSet = new HashSet();
        boolean[] checkBoxStates = this.mAdapter.getCheckBoxStates();
        for (int i3 = 0; i3 < checkBoxStates.length; i3++) {
            CharSequence[] charSequenceArr = this.mEntryValues;
            if (i3 >= charSequenceArr.length) {
                break;
            }
            if (checkBoxStates[i3]) {
                hashSet.add(charSequenceArr[i3].toString());
            }
        }
        return hashSet;
    }

    public static COUIMultiSelectListPreferenceDialogFragment newInstance(String str) {
        COUIMultiSelectListPreferenceDialogFragment cOUIMultiSelectListPreferenceDialogFragment = new COUIMultiSelectListPreferenceDialogFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("key", str);
        cOUIMultiSelectListPreferenceDialogFragment.setArguments(bundle);
        return cOUIMultiSelectListPreferenceDialogFragment;
    }

    @Override // androidx.preference.MultiSelectListPreferenceDialogFragmentCompat, androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null && getPreference() == null) {
            this.mDialogTitle = bundle.getString(SAVE_STATE_TITLE);
            this.mDialogMessage = bundle.getString(SAVE_STATE_MESSAGE);
            this.mEntries = bundle.getCharSequenceArray(SAVE_STATE_ENTRIES);
            this.mEntryValues = bundle.getCharSequenceArray(SAVE_STATE_ENTRY_VALUES);
            this.mSummaries = bundle.getCharSequenceArray(SAVE_STATE_SUMMARYS);
            this.mPositiveButtonText = bundle.getString(SAVE_STATE_POSITIVE_BUTTON_TEXT);
            this.mNegativeButtonText = bundle.getString(SAVE_STATE_NEGATIVE_BUTTON_TEXT);
            this.mCheckboxStates = bundle.getBooleanArray(SAVE_STATE_VALUES);
            this.mIfFollowHand = bundle.getBoolean(SAVE_STATE_FOLLOWHAND);
            this.mBlurBackground = bundle.getBoolean(SAVE_STATE_BLUR_BACKGROUND);
            this.mBlurMinAnimLevel = AnimLevel.valueOf(bundle.getInt(SAVE_STATE_BLUR_ANIM_LAVEL, UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN.getIntValue()));
            COUILog.e(TAG, "OnCreate, mPreference == null");
            return;
        }
        COUIMultiSelectListPreference cOUIMultiSelectListPreference = (COUIMultiSelectListPreference) getPreference();
        this.mPreference = cOUIMultiSelectListPreference;
        if (cOUIMultiSelectListPreference.getEntries() == null || this.mPreference.getEntryValues() == null) {
            throw new IllegalStateException("MultiSelectListPreference requires an entries array and an entryValues array.");
        }
        this.mDialogTitle = this.mPreference.getDialogTitle();
        this.mDialogMessage = this.mPreference.getDialogMessage();
        this.mEntries = this.mPreference.getEntries();
        this.mEntryValues = this.mPreference.getEntryValues();
        this.mSummaries = this.mPreference.getSummaries();
        this.mPositiveButtonText = this.mPreference.getPositiveButtonText();
        this.mNegativeButtonText = this.mPreference.getNegativeButtonText();
        this.mCheckboxStates = getCheckboxStatesFromValues(this.mPreference.getValues());
        this.mIfFollowHand = this.mPreference.isIfFollowHand();
        this.mBlurBackground = this.mPreference.isBlurBackground();
        this.mBlurMinAnimLevel = this.mPreference.getBlurMinAnimLevel();
    }

    @Override // androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        Point lastTouchPoint;
        View preferenceView;
        this.mAdapter = new AnonymousClass1(getContext(), R.layout.coui_select_dialog_multichoice, this.mEntries, this.mSummaries, this.mCheckboxStates, true);
        Context context = getContext();
        Objects.requireNonNull(context);
        COUIAlertDialogBuilder blurBackgroundDrawable = new COUIAlertDialogBuilder(context, R.style.COUIAlertDialog_BottomAssignment).setTitle(this.mDialogTitle).setMessage(this.mDialogMessage).setAdapter((ListAdapter) this.mAdapter, (DialogInterface.OnClickListener) this).setPositiveButton(this.mPositiveButtonText, (DialogInterface.OnClickListener) this).setNegativeButton(this.mNegativeButtonText, (DialogInterface.OnClickListener) this).setBlurBackgroundDrawable(this.mBlurBackground, this.mBlurMinAnimLevel);
        this.mBuilder = blurBackgroundDrawable;
        if (!this.mIfFollowHand) {
            return blurBackgroundDrawable.create();
        }
        Point point = new Point();
        COUIMultiSelectListPreference cOUIMultiSelectListPreference = this.mPreference;
        if (cOUIMultiSelectListPreference != null) {
            preferenceView = cOUIMultiSelectListPreference.getPreferenceView();
            lastTouchPoint = this.mPreference.getLastTouchPoint();
        } else {
            lastTouchPoint = point;
            preferenceView = null;
        }
        return preferenceView == null ? this.mBuilder.create() : this.mBuilder.create(preferenceView, lastTouchPoint);
    }

    @Override // androidx.preference.MultiSelectListPreferenceDialogFragmentCompat, androidx.preference.PreferenceDialogFragmentCompat
    public void onDialogClosed(boolean z2) {
        COUIMultiSelectListPreference cOUIMultiSelectListPreference;
        super.onDialogClosed(z2);
        if (z2) {
            Set<String> selectedValues = getSelectedValues();
            if (getPreference() == null || (cOUIMultiSelectListPreference = (COUIMultiSelectListPreference) getPreference()) == null || !cOUIMultiSelectListPreference.callChangeListener(selectedValues)) {
                return;
            }
            cOUIMultiSelectListPreference.setValues(selectedValues);
        }
    }

    @Override // androidx.preference.MultiSelectListPreferenceDialogFragmentCompat, androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBooleanArray(SAVE_STATE_VALUES, this.mAdapter.getCheckBoxStates());
        CharSequence charSequence = this.mDialogTitle;
        if (charSequence != null) {
            bundle.putString(SAVE_STATE_TITLE, String.valueOf(charSequence));
        }
        CharSequence charSequence2 = this.mDialogMessage;
        if (charSequence2 != null) {
            bundle.putString(SAVE_STATE_MESSAGE, String.valueOf(charSequence2));
        }
        bundle.putString(SAVE_STATE_POSITIVE_BUTTON_TEXT, String.valueOf(this.mPositiveButtonText));
        bundle.putString(SAVE_STATE_NEGATIVE_BUTTON_TEXT, String.valueOf(this.mNegativeButtonText));
        bundle.putCharSequenceArray(SAVE_STATE_SUMMARYS, this.mSummaries);
        bundle.putBoolean(SAVE_STATE_FOLLOWHAND, this.mIfFollowHand);
        bundle.putBoolean(SAVE_STATE_BLUR_BACKGROUND, this.mBlurBackground);
        bundle.putInt(SAVE_STATE_BLUR_ANIM_LAVEL, this.mBlurMinAnimLevel.getIntValue());
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        if (getPreference() == null) {
            dismiss();
            return;
        }
        COUIAlertDialogBuilder cOUIAlertDialogBuilder = this.mBuilder;
        if (cOUIAlertDialogBuilder != null) {
            cOUIAlertDialogBuilder.updateViewAfterShown();
        }
    }
}