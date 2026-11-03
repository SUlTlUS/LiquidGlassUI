package com.coui.appcompat.preference;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import androidx.preference.ListPreferenceDialogFragmentCompat;
import com.coui.appcompat.dialog.COUIAlertDialogBuilder;
import com.coui.appcompat.dialog.adapter.ChoiceListAdapter;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.uiutil.AnimLevel;
import com.coui.appcompat.uiutil.UIUtil;
import com.support.dialog.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIListPreferenceDialogFragment extends ListPreferenceDialogFragmentCompat {
    private static final String SAVE_STATE_BLUR_ANIM_LAVEL = "ListPreferenceDialogFragment.SAVE_STATE_BLUR_ANIM_LAVEL";
    private static final String SAVE_STATE_BLUR_BACKGROUND = "ListPreferenceDialogFragment.SAVE_STATE_BLUR_BACKGROUND";
    private static final String SAVE_STATE_ENTRIES = "ListPreferenceDialogFragment.entries";
    private static final String SAVE_STATE_ENTRY_VALUES = "ListPreferenceDialogFragment.entryValues";
    private static final String SAVE_STATE_FOLLOWHAND = "ListPreferenceDialogFragment.SAVE_STATE_FOLLOWHAND";
    private static final String SAVE_STATE_INDEX = "COUIListPreferenceDialogFragment.index";
    private static final String SAVE_STATE_MESSAGE = "COUIListPreferenceDialogFragment.message";
    private static final String SAVE_STATE_SUMMARYS = "COUListPreferenceDialogFragment.summarys";
    private static final String SAVE_STATE_TITLE = "COUIListPreferenceDialogFragment.title";
    private static final String TAG = "COUIListDialogFragment";
    private COUIAlertDialogBuilder mBuilder;
    private CharSequence mDialogMessage;
    private CharSequence mDialogTitle;
    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;
    private COUIListPreference mPreference;
    private CharSequence[] mSummaries;
    private int mClickedDialogEntryIndex = -1;
    private boolean mIfFollowHand = true;
    private boolean mBlurBackground = false;
    private AnimLevel mBlurMinAnimLevel = UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN;

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIListPreferenceDialogFragment$1, reason: invalid class name */
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

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIListPreferenceDialogFragment$2, reason: invalid class name */
    public class AnonymousClass2 implements DialogInterface.OnClickListener {
        public AnonymousClass2() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i3) {
            COUIListPreferenceDialogFragment.access$002(COUIListPreferenceDialogFragment.this, i3);
            COUIListPreferenceDialogFragment.this.onClick(dialogInterface, -1);
            dialogInterface.dismiss();
        }
    }

    public static /* synthetic */ int access$002(COUIListPreferenceDialogFragment cOUIListPreferenceDialogFragment, int i3) {
        cOUIListPreferenceDialogFragment.mClickedDialogEntryIndex = i3;
        return i3;
    }

    public static COUIListPreferenceDialogFragment newInstance(String str) {
        COUIListPreferenceDialogFragment cOUIListPreferenceDialogFragment = new COUIListPreferenceDialogFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("key", str);
        cOUIListPreferenceDialogFragment.setArguments(bundle);
        return cOUIListPreferenceDialogFragment;
    }

    private void saveSelectedIndex() {
        int i3 = this.mClickedDialogEntryIndex;
        if (i3 >= 0) {
            CharSequence[] charSequenceArr = this.mEntryValues;
            if (i3 < charSequenceArr.length) {
                String string = charSequenceArr[i3].toString();
                if (getPreference() != null) {
                    COUIListPreference cOUIListPreference = (COUIListPreference) getPreference();
                    if (cOUIListPreference.callChangeListener(string)) {
                        cOUIListPreference.setValue(string);
                    }
                }
            }
        }
    }

    @Override // androidx.preference.ListPreferenceDialogFragmentCompat, androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null && getPreference() == null) {
            this.mClickedDialogEntryIndex = bundle.getInt(SAVE_STATE_INDEX, -1);
            this.mDialogTitle = bundle.getString(SAVE_STATE_TITLE);
            this.mDialogMessage = bundle.getString(SAVE_STATE_MESSAGE);
            this.mEntries = bundle.getCharSequenceArray(SAVE_STATE_ENTRIES);
            this.mEntryValues = bundle.getCharSequenceArray(SAVE_STATE_ENTRY_VALUES);
            this.mSummaries = bundle.getCharSequenceArray(SAVE_STATE_SUMMARYS);
            this.mIfFollowHand = bundle.getBoolean(SAVE_STATE_FOLLOWHAND);
            this.mBlurBackground = bundle.getBoolean(SAVE_STATE_BLUR_BACKGROUND);
            this.mBlurMinAnimLevel = AnimLevel.valueOf(bundle.getInt(SAVE_STATE_BLUR_ANIM_LAVEL, UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN.getIntValue()));
            COUILog.e(TAG, "OnCreate, mPreference == null");
            return;
        }
        COUIListPreference cOUIListPreference = (COUIListPreference) getPreference();
        this.mPreference = cOUIListPreference;
        if (cOUIListPreference.getEntries() == null || this.mPreference.getEntryValues() == null) {
            throw new IllegalStateException("ListPreference requires an entries array and an entryValues array.");
        }
        this.mDialogTitle = this.mPreference.getDialogTitle();
        this.mDialogMessage = this.mPreference.getDialogMessage();
        this.mSummaries = this.mPreference.getSummaries();
        COUIListPreference cOUIListPreference2 = this.mPreference;
        this.mClickedDialogEntryIndex = cOUIListPreference2.findIndexOfValue(cOUIListPreference2.getValue());
        this.mEntries = this.mPreference.getEntries();
        this.mEntryValues = this.mPreference.getEntryValues();
        this.mIfFollowHand = this.mPreference.isIfFollowHand();
        this.mBlurBackground = this.mPreference.isBlurBackground();
        this.mBlurMinAnimLevel = this.mPreference.getBlurMinAnimLevel();
    }

    @Override // androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        boolean[] zArr;
        int i3;
        CharSequence[] charSequenceArr = this.mEntries;
        View preferenceView = null;
        if (charSequenceArr == null || (i3 = this.mClickedDialogEntryIndex) < 0 || i3 >= charSequenceArr.length) {
            zArr = null;
        } else {
            boolean[] zArr2 = new boolean[charSequenceArr.length];
            zArr2[i3] = true;
            zArr = zArr2;
        }
        COUIAlertDialogBuilder adapter = new COUIAlertDialogBuilder(requireContext(), R.style.COUIAlertDialog_BottomAssignment).setTitle(this.mDialogTitle).setMessage(this.mDialogMessage).setNegativeButton(com.support.preference.R.string.dialog_cancel, (DialogInterface.OnClickListener) null).setBlurBackgroundDrawable(this.mBlurBackground, this.mBlurMinAnimLevel).setAdapter((ListAdapter) new AnonymousClass1(getContext(), R.layout.coui_select_dialog_singlechoice, this.mEntries, this.mSummaries, zArr, false), (DialogInterface.OnClickListener) new AnonymousClass2());
        this.mBuilder = adapter;
        if (!this.mIfFollowHand) {
            return adapter.create();
        }
        Point point = new Point();
        COUIListPreference cOUIListPreference = this.mPreference;
        if (cOUIListPreference != null) {
            preferenceView = cOUIListPreference.getPreferenceView();
            point = this.mPreference.getLastTouchPoint();
        }
        return preferenceView == null ? this.mBuilder.create() : this.mBuilder.create(preferenceView, point);
    }

    @Override // androidx.preference.ListPreferenceDialogFragmentCompat, androidx.preference.PreferenceDialogFragmentCompat
    public void onDialogClosed(boolean z2) {
        if (!z2 || this.mEntries == null) {
            return;
        }
        saveSelectedIndex();
    }

    @Override // androidx.preference.ListPreferenceDialogFragmentCompat, androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(SAVE_STATE_INDEX, this.mClickedDialogEntryIndex);
        CharSequence charSequence = this.mDialogTitle;
        if (charSequence != null) {
            bundle.putString(SAVE_STATE_TITLE, String.valueOf(charSequence));
        }
        CharSequence charSequence2 = this.mDialogMessage;
        if (charSequence2 != null) {
            bundle.putString(SAVE_STATE_MESSAGE, String.valueOf(charSequence2));
        }
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