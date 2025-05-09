package com.gal.smartcalender.Settings;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.gal.smartcalender.AboutActivity;
import com.gal.smartcalender.CalenderManager;
import com.gal.smartcalender.Constants;
import com.gal.smartcalender.EditSystemPromptActivity;
import com.gal.smartcalender.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.prefrences, rootKey);
        populateInstalledApps();
        Preference openAboutActivityPref = findPreference("open_about_activity");
        if (openAboutActivityPref != null) {
            openAboutActivityPref.setOnPreferenceClickListener(preference -> {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
                return true; // Return true to indicate the click was handled
            });
        }
        Preference openSysPromptEditActivityPref = findPreference("open_system_prompt_edit_activity");
        if (openSysPromptEditActivityPref != null) {
            openSysPromptEditActivityPref.setOnPreferenceClickListener(preference -> {
                Intent intent = new Intent(getActivity(), EditSystemPromptActivity.class);
                startActivity(intent);
                return true; // Return true to indicate the click was handled
            });
        }
    }


    // For populating settings in the apps installed
    private void populateInstalledApps() {
        MultiSelectListPreference appListPref = findPreference(Constants.SELECTED_APPS_PREFERENCE);
        if (appListPref == null) return;

        PackageManager pm = requireContext().getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> pkgAppsList = pm.queryIntentActivities(mainIntent, PackageManager.GET_META_DATA);

        // Sort the list of ResolveInfo objects directly using their app labels
        pkgAppsList.sort((r1, r2) -> {
            String label1 = pm.getApplicationLabel(r1.activityInfo.applicationInfo).toString();
            String label2 = pm.getApplicationLabel(r2.activityInfo.applicationInfo).toString();
            return label1.compareToIgnoreCase(label2);
        });

        int size = pkgAppsList.size();
        String[] appNames = new String[size];
        String[] packageNames = new String[size];

        for (int i = 0; i < size; i++) {
            ResolveInfo info = pkgAppsList.get(i);
            appNames[i] = pm.getApplicationLabel(info.activityInfo.applicationInfo).toString();
            packageNames[i] = info.activityInfo.packageName;
        }

        appListPref.setEntries(appNames);
        appListPref.setEntryValues(packageNames);
    }

    // For populating settings in the apps installed
    private void populateCalenders() {
        MultiSelectListPreference appListPref = findPreference(Constants.SELECTED_APPS_PREFERENCE);
        if (appListPref == null) return;

        String[] calender_names;
        String[] calender_ids;

        CalenderManager calenderManager = new CalenderManager(this.getContext());

        List<Pair<Long, String>> cal_list = calenderManager.getCalendarIdsAndNames();

        for (int i = 0; i < cal_list.size(); i++) {

        }
        appListPref.setEntries(appNames);
        appListPref.setEntryValues(packageNames);
    }


}