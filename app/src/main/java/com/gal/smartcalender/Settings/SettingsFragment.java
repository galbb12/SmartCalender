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
import androidx.preference.PreferenceFragmentCompat;

import com.gal.smartcalender.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String SELECTED_APPS_PREFERENCE  = "selected_apps";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.prefrences, rootKey);
        populateInstalledApps();
    }


    // For populating settings in the apps installed
    private void populateInstalledApps() {
        MultiSelectListPreference appListPref = findPreference(SELECTED_APPS_PREFERENCE);
        if (appListPref == null) return;

        PackageManager pm = requireContext().getPackageManager();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> pkgAppsList = pm.queryIntentActivities(mainIntent, PackageManager.GET_META_DATA);

        List<String> packageNames = new ArrayList<>();
        List<String> appNames = new ArrayList<>();

        for (ResolveInfo resolveInfo : pkgAppsList) {
            String appName = pm.getApplicationLabel(resolveInfo.activityInfo.applicationInfo).toString();
            String packageName = resolveInfo.activityInfo.packageName;

            packageNames.add(packageName);
            appNames.add(appName);
        }

        // Create list of pairs inorder to sort both in the same order
        List<Pair<String, String>> appPackagePairs = new ArrayList<>();
        for (int i = 0; i < appNames.size(); i++) {
            appPackagePairs.add(new Pair<>(appNames.get(i), packageNames.get(i)));
        }

        // Sort the pairs by the app name (alphabetically)
        Collections.sort(appPackagePairs, new Comparator<Pair<String, String>>() {
            @Override
            public int compare(Pair<String, String> pair1, Pair<String, String> pair2) {
                return pair1.first.compareTo(pair2.first); // Compare by appName (first element)
            }
        });

        List<String> packageNamesSort = new ArrayList<>();
        List<String> appNamesSort = new ArrayList<>();

        // After sorting, extract the sorted appNames and packageNames
        appNames.clear();
        packageNames.clear();
        for (Pair<String, String> pair : appPackagePairs) {
            appNamesSort.add(pair.first);
            packageNamesSort.add(pair.second);
        }

        // Set the sorted entries and entry values in the preference
        appListPref.setEntries(appNamesSort.toArray(new String[0]));
        appListPref.setEntryValues(packageNamesSort.toArray(new String[0]));
    }
}