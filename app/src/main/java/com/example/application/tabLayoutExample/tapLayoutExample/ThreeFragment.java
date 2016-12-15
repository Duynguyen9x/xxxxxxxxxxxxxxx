package com.example.application.tabLayoutExample.tapLayoutExample;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.example.application.tabLayoutExample.tapLayoutExample.model.AppInfo;

/**
 * Created by 8470p on 9/7/2016.
 */
public class ThreeFragment extends ChildHomeFragment {

    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("duy.nq", "ThreeFragment onCreateView");
        // Inflate the layout for this fragment
       super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onResume() {
        Log.i("duy.nq", "ThreeFragment onResume");
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        Log.i("duy.nq", "ThreeFragment onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("duy.nq", "ThreeFragment onDestroy");
    }

    public ArrayList<AppInfo> getListApp() {
        ArrayList<AppInfo> arr = new ArrayList<>();
        List<PackageInfo> packages = getContext().getPackageManager().getInstalledPackages(0);

        int numberGet = 500;

        for (int i = 0; i < packages.size(); i++) {
            if (i == numberGet)
                break;

            PackageInfo packageInfo = packages.get(i);
            AppInfo app = new AppInfo();

            app.setName(packageInfo.applicationInfo.loadLabel(getContext().getPackageManager()).toString());
            app.setIcon(packageInfo.applicationInfo.loadIcon(getContext().getPackageManager()));
            app.setPackageName(packageInfo.applicationInfo.packageName);
            app.setSize(getSizeApp(packageInfo));

            arr.add(app);
        }
        return arr;
    }
}
