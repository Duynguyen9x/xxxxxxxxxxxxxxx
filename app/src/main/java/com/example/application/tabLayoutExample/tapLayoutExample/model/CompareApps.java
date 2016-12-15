package com.example.application.tabLayoutExample.tapLayoutExample.model;

import java.util.Comparator;

/**
 * Created by 8470p on 9/7/2016.
 */
public class CompareApps implements Comparator<AppInfo> {

    private static final int SEARCH_APP_NAME_A_TO_Z = 0;
    private static final int SEARCH_APP_NAME_BY_SIZE = 1;
    private static final int SEARCH_APP_NAME_BY_DATE = 2;
    private int style;

    public CompareApps(int style) {
        super();
        this.style = style;
    }

    @Override
    public int compare(AppInfo app1, AppInfo app2) {

        switch (style) {
            case SEARCH_APP_NAME_A_TO_Z:
                // name from a to z
                return app1.getName().compareTo(app2.getName());
            case SEARCH_APP_NAME_BY_SIZE:
                // search app by size
                if (app1.getSize() == app2.getSize())
                    return 0;
                else if (app1.getSize() > app2.getSize()) {
                    return -1;
                } else {
                    return 1;
                }
            default:
                return -1;
        }
    }
}
