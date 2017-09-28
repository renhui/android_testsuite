package android_testsuite.application_search;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ren Hui
 * @since 1.0.1.058
 */

public class ScanPackageOnDevice {
    private Context mContext;
    private ArrayList<AppInfo> mAppOnDevice;

    public ScanPackageOnDevice(Context context) {
        this.mContext = context.getApplicationContext();
        this.mAppOnDevice = new ArrayList<AppInfo>();
    }

    public ArrayList<AppInfo> getAppOnDevice() {
        if (this.mAppOnDevice.isEmpty()) {
            scanPackage();
        }
        return this.mAppOnDevice;
    }

    private void scanPackage() {
        ApplicationInfo applicationInfo;
        PackageManager packageManager = this.mContext.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);

        //遍历应用信息。
        for (PackageInfo packageInfo : packageInfoList) {
            //获取到应用的信息
            try {
                //根据pid
                applicationInfo = packageManager.getApplicationInfo(packageInfo.packageName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("ScanPackageOnDevice", e.toString());
                continue;
            }
            // 不需要联网权限
            if (packageManager.checkPermission("android.permission.INTERNET", packageInfo.
                    packageName) == android.content.pm.PackageManager.PERMISSION_DENIED) {
               continue;
            }

            // 不是独立应用，而是应用的独立进程
            if (packageInfo.packageName.contains(":")) {
                continue;
            }

            AppInfo appInfo = new AppInfo();
            //赋值
            appInfo.setAppIcon(applicationInfo.loadIcon(packageManager));
            appInfo.setUid(applicationInfo.uid);
            appInfo.setAppName((String) packageManager.getApplicationLabel(applicationInfo));
            appInfo.setPid(packageInfo.packageName);
            this.mAppOnDevice.add(appInfo);
        }
    }

}
