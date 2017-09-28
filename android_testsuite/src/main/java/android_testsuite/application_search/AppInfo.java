package android_testsuite.application_search;

import android.graphics.drawable.Drawable;

/**
 * 手机APP信息基类
 * @author Ren Hui
 * @since 1.0.1.058
 */
public class AppInfo {
    private Drawable appIcon;
    private int uid;
    private String pid;
    private String appName;

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return this.uid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPid() {
        return pid;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppName() {
        return this.appName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof AppInfo)) return false;
        AppInfo appInfo = (AppInfo) obj;
        return appInfo.getUid() == this.uid;
    }
}
