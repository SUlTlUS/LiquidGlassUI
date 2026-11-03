package com.coui.appcompat.preference;

import android.widget.CompoundButton;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import com.coui.appcompat.couiswitch.COUISwitch;

/* JADX INFO: loaded from: classes7.dex */
final class COUISwitchPreferenceClickHelper {
    private COUISwitchPreferenceClickHelper() {
    }

    private static void dispatchClickListeners(Preference preference) {
        PreferenceManager preferenceManager;
        PreferenceManager.OnPreferenceTreeClickListener onPreferenceTreeClickListener;
        Preference.OnPreferenceClickListener onPreferenceClickListener = preference.getOnPreferenceClickListener();
        if ((onPreferenceClickListener != null && onPreferenceClickListener.onPreferenceClick(preference)) || (preferenceManager = preference.getPreferenceManager()) == null || (onPreferenceTreeClickListener = preferenceManager.getOnPreferenceTreeClickListener()) == null) {
            return;
        }
        onPreferenceTreeClickListener.onPreferenceTreeClick(preference);
    }

    public static void dispatchOnPreferenceClickIfUserSwitchToggle(CompoundButton compoundButton, Preference preference) {
        if ((compoundButton instanceof COUISwitch) && ((COUISwitch) compoundButton).isUserClickInProgress()) {
            dispatchClickListeners(preference);
        }
    }
}