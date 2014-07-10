package com.r3n3.xposed.securewindow;

import android.content.pm.ApplicationInfo;

/**
 * Created by rene on 7/8/14.
 */
public class PreferencesModel {
    private ApplicationInfo appInfo;
    private boolean checked;

    public ApplicationInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(ApplicationInfo appInfo) {
        this.appInfo = appInfo;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
