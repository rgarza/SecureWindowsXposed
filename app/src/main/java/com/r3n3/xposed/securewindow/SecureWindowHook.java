package com.r3n3.xposed.securewindow;

import android.content.SharedPreferences;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import java.util.HashSet;
import java.util.Set;
import java.util.prefs.Preferences;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by rene on 7/6/14.
 */
public class SecureWindowHook implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    private static XSharedPreferences preferences;

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {

        preferences = new XSharedPreferences(PreferencesActivity.class.getPackage().getName(), "PreferencesActivity");
        XposedBridge.log(preferences.getFile().getPath());
    }


    XC_MethodHook hideScreenshot = new XC_MethodHook() {
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

        }

        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            if (param.getResult() == null) {
                return;
            }
            Window window = (Window) param.getResult();
            if (!((window.getAttributes().flags & WindowManager.LayoutParams.FLAG_SECURE) == WindowManager.LayoutParams.FLAG_SECURE)) {
                window.addFlags(WindowManager.LayoutParams.FLAG_SECURE);
            }

        }
    };


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return;
        }
        preferences.reload();
        Set<String> set = preferences.getStringSet(PreferencesActivity.SECURE_APPS, null);
        if (set != null)
            if(set.contains(lpparam.packageName)) {
                XposedBridge.log("hoooking " + lpparam.packageName);
                findAndHookMethod("android.app.Activity", lpparam.classLoader, "getWindow", hideScreenshot);
            }

    }
}
