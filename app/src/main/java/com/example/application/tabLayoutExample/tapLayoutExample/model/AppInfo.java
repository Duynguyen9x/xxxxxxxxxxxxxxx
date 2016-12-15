package com.example.application.tabLayoutExample.tapLayoutExample.model;

import android.graphics.drawable.Drawable;

/**
 * Created by 8470p on 9/7/2016.
 */
public class AppInfo {
    private Drawable icon;
    private String name;
    private String packageName;
    private long size;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
