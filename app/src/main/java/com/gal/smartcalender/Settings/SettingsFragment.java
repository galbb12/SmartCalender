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
        populateCalenders();
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

        List<ResolveInfo> pkgAppsList = pm.queryIntentActivities(mainIntent, 0); // Remove GET_META_DATA flag

        // Create a list of app data objects for efficient sorting
        List<AppData> appDataList = new ArrayList<>(pkgAppsList.size());

        for (ResolveInfo info : pkgAppsList) {
            String label = pm.getApplicationLabel(info.activityInfo.applicationInfo).toString();
            appDataList.add(new AppData(label, info.activityInfo.packageName));
        }

        // Sort once using the pre-fetched labels
        appDataList.sort((a1, a2) -> a1.name.compareToIgnoreCase(a2.name));

        // Convert to arrays
        int size = appDataList.size();
        String[] appNames = new String[size];
        String[] packageNames = new String[size];

        for (int i = 0; i < size; i++) {
            AppData data = appDataList.get(i);
            appNames[i] = data.name;
            packageNames[i] = data.packageName;
        }

        appListPref.setEntries(appNames);
        appListPref.setEntryValues(packageNames);
    }

    // Helper class for efficient sorting
    private static class AppData {
        final String name;
        final String packageName;

        AppData(String name, String packageName) {
            this.name = name;
            this.packageName = packageName;
        }
    }


    // For populating settings in the apps installed
    private void populateCalenders() {
        MultiSelectListPreference appListPref = findPreference(Constants.SELECTED_CALENDERS_PREFERENCE);
        if (appListPref == null) return;

        CalenderManager calenderManager = new CalenderManager(this.getContext());

        List<Pair<Long, String>> cal_list = calenderManager.getCalendarIdsAndNames();

        String[] calender_names = new String[cal_list.size()];
        String[] calender_ids = new String[cal_list.size()];


        for (int i = 0; i < cal_list.size(); i++) {
            calender_names[i] = cal_list.get(i).second;
            calender_ids[i] = Long.toString(cal_list.get(i).first);
        }
        appListPref.setEntries(calender_names);
        appListPref.setEntryValues(calender_ids);
    }


}