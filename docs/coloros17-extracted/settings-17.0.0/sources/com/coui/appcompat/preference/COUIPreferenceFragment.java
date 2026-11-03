package com.coui.appcompat.preference;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.DialogFragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.COUIRecyclerView;
import androidx.recyclerview.widget.RecyclerView;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.support.preference.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIPreferenceFragment extends PreferenceFragmentCompat {
    private static final String DIALOG_FRAGMENT_TAG = "androidx.preference.PreferenceFragment.DIALOG";
    private boolean mEnableInternalDivider = true;
    private COUIPreferenceItemDecoration mPreferenceItemDecoration;

    public COUIPreferenceItemDecoration getItemDecoration() {
        return this.mPreferenceItemDecoration;
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    public RecyclerView onCreateRecyclerView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        COUIRecyclerView cOUIRecyclerView = (COUIRecyclerView) layoutInflater.inflate(R.layout.coui_preference_recyclerview, viewGroup, false);
        cOUIRecyclerView.setEnablePointerDownAction(false);
        cOUIRecyclerView.setLayoutManager(onCreateLayoutManager());
        COUIDarkModeUtil.setForceDarkAllow(cOUIRecyclerView, false);
        return cOUIRecyclerView;
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewOnCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        setDivider(null);
        setDividerHeight(0);
        return viewOnCreateView;
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onDestroyView() {
        COUIPreferenceItemDecoration cOUIPreferenceItemDecoration = this.mPreferenceItemDecoration;
        if (cOUIPreferenceItemDecoration != null) {
            cOUIPreferenceItemDecoration.onDestroy();
        }
        super.onDestroyView();
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.OnDisplayPreferenceDialogListener
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment dialogFragmentNewInstance;
        if (getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG) != null) {
            return;
        }
        if (preference instanceof COUIActivityDialogPreference) {
            dialogFragmentNewInstance = COUIActivityDialogFragment.newInstance(preference.getKey());
        } else if (preference instanceof COUIEditTextPreference) {
            dialogFragmentNewInstance = COUIEditTextPreferenceDialogFragment.newInstance(preference.getKey());
        } else if (preference instanceof COUIMultiSelectListPreference) {
            dialogFragmentNewInstance = COUIMultiSelectListPreferenceDialogFragment.newInstance(preference.getKey());
        } else {
            if (!(preference instanceof ListPreference)) {
                super.onDisplayPreferenceDialog(preference);
                return;
            }
            dialogFragmentNewInstance = COUIListPreferenceDialogFragment.newInstance(preference.getKey());
        }
        dialogFragmentNewInstance.setTargetFragment(this, 0);
        dialogFragmentNewInstance.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (getListView() == null || this.mPreferenceItemDecoration == null || !this.mEnableInternalDivider) {
            return;
        }
        getListView().removeItemDecoration(this.mPreferenceItemDecoration);
        if (this.mPreferenceItemDecoration.getPreferenceScreen() == null) {
            this.mPreferenceItemDecoration = new COUIPreferenceItemDecoration(getContext(), getPreferenceScreen());
        }
        getListView().addItemDecoration(this.mPreferenceItemDecoration);
    }

    public void setEnableCOUIPreferenceDivider(boolean z2) throws NullPointerException {
        this.mEnableInternalDivider = z2;
        if (!z2) {
            if (getListView() != null) {
                getListView().removeItemDecoration(this.mPreferenceItemDecoration);
            }
        } else {
            if (getListView() == null || this.mPreferenceItemDecoration == null) {
                return;
            }
            getListView().removeItemDecoration(this.mPreferenceItemDecoration);
            if (this.mPreferenceItemDecoration.getPreferenceScreen() == null) {
                this.mPreferenceItemDecoration = new COUIPreferenceItemDecoration(getContext(), getPreferenceScreen());
            }
            getListView().addItemDecoration(this.mPreferenceItemDecoration);
        }
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
        if (preferenceScreen == getPreferenceScreen()) {
            return;
        }
        super.setPreferenceScreen(preferenceScreen);
        if (this.mPreferenceItemDecoration != null && getListView() != null) {
            getListView().removeItemDecoration(this.mPreferenceItemDecoration);
        }
        this.mPreferenceItemDecoration = new COUIPreferenceItemDecoration(getContext(), preferenceScreen);
        if (getListView() == null || !this.mEnableInternalDivider) {
            return;
        }
        getListView().addItemDecoration(this.mPreferenceItemDecoration);
    }
}