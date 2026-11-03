package com.coui.appcompat.preference;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.DialogPreference;
import androidx.preference.EditTextPreferenceDialogFragmentCompat;
import com.coui.appcompat.dialog.COUIAlertDialogBuilder;
import com.coui.appcompat.edittext.COUIEditText;
import com.coui.appcompat.uiutil.AnimLevel;
import com.coui.appcompat.uiutil.UIUtil;
import com.support.dialog.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIEditTextPreferenceDialogFragment extends EditTextPreferenceDialogFragmentCompat {
    private static final String SAVE_STATE_BLUR_BACKGROUND = "ListPreferenceDialogFragment.SAVE_STATE_BLUR_BACKGROUND";
    private static final String SAVE_STATE_TEXT = "EditTextPreferenceDialogFragment.text";
    private static final String TAG = "COUIEditTextPreferenceDialogFragment";
    private boolean mBlurBackground = false;
    private AnimLevel mBlurMinAnimLevel = UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN;
    private COUIAlertDialogBuilder mBuilder;
    private COUIEditText mEditText;

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIEditTextPreferenceDialogFragment$1, reason: invalid class name */
    public class AnonymousClass1 implements TextWatcher {
        final /* synthetic */ AlertDialog val$dialog;
        final /* synthetic */ boolean val$supportEmptyInput;

        public AnonymousClass1(AlertDialog alertDialog, boolean z2) {
            this.val$dialog = alertDialog;
            this.val$supportEmptyInput = z2;
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            Button button = this.val$dialog.getButton(-1);
            if (button == null || this.val$supportEmptyInput) {
                return;
            }
            button.setEnabled(!TextUtils.isEmpty(editable));
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
        }
    }

    private COUIAlertDialogBuilder initCOUIAlertDialogBuilder() {
        return new COUIAlertDialogBuilder(requireContext(), R.style.COUIAlertDialog_BottomAssignment).setTitle(getPreference().getDialogTitle()).setMessage(getPreference().getDialogMessage()).setPositiveButton(getPreference().getPositiveButtonText(), (DialogInterface.OnClickListener) this).setNegativeButton(getPreference().getNegativeButtonText(), (DialogInterface.OnClickListener) this);
    }

    public static COUIEditTextPreferenceDialogFragment newInstance(String str) {
        COUIEditTextPreferenceDialogFragment cOUIEditTextPreferenceDialogFragment = new COUIEditTextPreferenceDialogFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("key", str);
        cOUIEditTextPreferenceDialogFragment.setArguments(bundle);
        return cOUIEditTextPreferenceDialogFragment;
    }

    @Override // androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        COUIEditTextPreference cOUIEditTextPreference;
        FragmentActivity activity = getActivity();
        this.mBuilder = initCOUIAlertDialogBuilder();
        View viewOnCreateDialogView = onCreateDialogView(activity);
        if (viewOnCreateDialogView == null) {
            Log.d(TAG, "COUIEditTextPreferenceDialogFragment  contentView == null ");
            return this.mBuilder.create();
        }
        this.mEditText = (COUIEditText) viewOnCreateDialogView.findViewById(android.R.id.edit);
        onBindDialogView(viewOnCreateDialogView);
        this.mBuilder.setView(viewOnCreateDialogView);
        if (getPreference() != null) {
            onBindDialogView(viewOnCreateDialogView);
        }
        onPrepareDialogBuilder(this.mBuilder);
        DialogPreference preference = getPreference();
        if (preference == null || !(preference instanceof COUIEditTextPreference)) {
            cOUIEditTextPreference = null;
        } else {
            cOUIEditTextPreference = (COUIEditTextPreference) preference;
            this.mBlurBackground = cOUIEditTextPreference.isBlurBackground();
            this.mBlurMinAnimLevel = cOUIEditTextPreference.getBlurMinAnimLevel();
        }
        AlertDialog alertDialogCreate = this.mBuilder.setBlurBackgroundDrawable(this.mBlurBackground, this.mBlurMinAnimLevel).create();
        this.mEditText.addTextChangedListener(new AnonymousClass1(alertDialogCreate, cOUIEditTextPreference != null ? cOUIEditTextPreference.isSupportEmptyInput() : false));
        return alertDialogCreate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        COUIEditText cOUIEditText = this.mEditText;
        if (cOUIEditText != null) {
            cOUIEditText.setFocusable(true);
            this.mEditText.requestFocus();
            if (getDialog() != null) {
                getDialog().getWindow().setSoftInputMode(5);
            }
        }
    }

    @Override // androidx.preference.EditTextPreferenceDialogFragmentCompat, androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        COUIEditText cOUIEditText = this.mEditText;
        if (cOUIEditText != null) {
            bundle.putCharSequence(SAVE_STATE_TEXT, cOUIEditText.getText());
            bundle.putBoolean(SAVE_STATE_BLUR_BACKGROUND, this.mBlurBackground);
        }
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