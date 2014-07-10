package com.r3n3.xposed.securewindow;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;

import com.random.xposed.securewindow.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class PreferencesActivity extends ListActivity {
    public static final String SECURE_APPS = "securedApps";
    List<PreferencesModel> installedApplications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        getInstalledApplications();
        ApplicationsAdapter listAdapter = new ApplicationsAdapter(this, R.layout.list_row,
                installedApplications);

        setListAdapter(listAdapter);

        setContentView(R.layout.activity_preferences);

        getListView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);


    }

    @Override
    protected void onStop() {
        super.onStop();
        HashSet<String> secureApps = new HashSet<String>();
        for (PreferencesModel m : installedApplications) {
            if (m.isChecked())
                secureApps.add(m.getAppInfo().packageName);
        }
        save(secureApps);
    }

    private void save(Set<String> apps) {
        SharedPreferences preferences = getPreferences(MODE_WORLD_READABLE);
        preferences.edit().putStringSet(SECURE_APPS, apps).apply();

    }

    private Collection<String> getSecuredApps() {
        SharedPreferences preferences = getPreferences(MODE_WORLD_READABLE);
        return preferences.getStringSet(SECURE_APPS, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.preferences, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getInstalledApplications() {

        installedApplications = new ArrayList<PreferencesModel>();
        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> allApps = packageManager.getInstalledApplications(0);
        Collection<String> securedApps = getSecuredApps();
        if (securedApps == null)
            securedApps = new HashSet<String>();

        for (ApplicationInfo app : allApps) {
            if (packageManager.getLaunchIntentForPackage(app.packageName) != null) {
                PreferencesModel m = new PreferencesModel();
                m.setAppInfo(app);
                m.setChecked(securedApps.contains(app.packageName));
                installedApplications.add(m);
            }
        }
    }
}
