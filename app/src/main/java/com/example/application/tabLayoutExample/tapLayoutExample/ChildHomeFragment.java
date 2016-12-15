package com.example.application.tabLayoutExample.tapLayoutExample;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.application.tabLayoutExample.R;
import com.example.application.tabLayoutExample.tapLayoutExample.adapter.ViewAppAdapter;
import com.example.application.tabLayoutExample.tapLayoutExample.model.AppInfo;

/**
 * Created by 8470p on 9/20/2016.
 */
public class ChildHomeFragment extends Fragment implements SearchView.OnQueryTextListener {

    private static final String SCHEME = "package";
    private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
    private static final String APP_PKG_NAME_22 = "pkg";
    private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
    private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";
    public static int mStyleSort = 0;

    private ArrayList<AppInfo> mArrList = null;
    private ViewAppAdapter mViewAdapter = null;
    private ListView mListView = null;
    private Context mContext;
    BroadcastReceiver mReceiver = null;
    View view = null;


    public ChildHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("duy.nq", "TwoFragment onCreate");
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("duy.nq", "TwoFragment onCreateView");
        view = inflater.inflate(R.layout.fragment_one, container, false);

        initAppList();
        initReceiver();

        return view;
    }

    public void initAppList() {
        mListView = (ListView) view.findViewById(R.id.list_view);

        mArrList = new ArrayList<>();

        mViewAdapter = new ViewAppAdapter(getContext(), R.layout.app_item_layout, mArrList);
        mListView.setAdapter(mViewAdapter);
        initLoadData();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                // custom dialog
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.custom_dialog);
                dialog.setCanceledOnTouchOutside(true);

                // set the custom dialog components - text, image and button

                Button getInfoButton = (Button) dialog.findViewById(R.id.btn_getInfo);
                // if button is clicked, close the custom dialog
                getInfoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        getAppInfo(position);
                        dialog.dismiss();
                    }
                });

                Button launchButton = (Button) dialog.findViewById(R.id.btn_lauchApp);
                // if button is clicked, close the custom dialog
                launchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            AppInfo app = mViewAdapter.getItem(position);
                            Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(app.getPackageName());

                            if (null != intent) {
                                dialog.dismiss();
                                startActivity(intent);
                            }
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                dialog.show();

            }
        });

        mListView.setTextFilterEnabled(false);

        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        // Capture ListView item click
        mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = mListView.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class
                mViewAdapter.toggleSelection(position);
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        // Calls getSelectedIds method from ListViewAdapter Class
                        SparseBooleanArray selected = mViewAdapter.getSelectedIds();
                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                AppInfo selectedItem = mViewAdapter.getItem(selected.keyAt(i));
                                // Remove selected items following the ids
                                mViewAdapter.remove(selectedItem);
                            }
                        }
                        // Close CAB
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_multi_select, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO Auto-generated method stub
                mViewAdapter.removeSelection();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }
        });

        //        registerForContextMenu(mListView);
    }

    public void initLoadData() {
        AsyncTask<Void, Void, List<AppInfo>> loadBitmapTask = new AsyncTask<Void, Void, List<AppInfo>>() {
            private ProgressDialog progress = null;
            @Override
            protected void onPreExecute() {
//                progress = ProgressDialog.show(getContext(), null, "Loading application info...");
                super.onPreExecute();
            }

            @Override
            protected  List<AppInfo> doInBackground(Void... params) {
                return getListApp();
            }

            @Override
            protected void onPostExecute( List<AppInfo> listApps) {
                try {
//                    progress.dismiss();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                mViewAdapter.addAll(listApps);
                mListView.setAdapter(mViewAdapter);
                super.onPostExecute(listApps);
            }
        };

        loadBitmapTask.execute();
    }

    public List<AppInfo> getListApp() {
        List<AppInfo> arr = new ArrayList<>();
        List<PackageInfo> packList = mContext.getPackageManager().getInstalledPackages(0);
        for (int i=0; i < packList.size(); i++)
        {
            PackageInfo packInfo = packList.get(i);
            if (  (packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
                AppInfo app = new AppInfo();
                app.setName(packInfo.applicationInfo.loadLabel(mContext.getPackageManager()).toString());
                app.setIcon(packInfo.applicationInfo.loadIcon(mContext.getPackageManager()));
                app.setPackageName(packInfo.applicationInfo.packageName);
                app.setSize(getSizeApp(packInfo));

                arr.add(app);
            }
        }

        return arr;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mViewAdapter.getFilter().filter(newText);

        return true;
    }

    public void getAppInfo(int position) {
        AppInfo app = mViewAdapter.getItem(position);
        Intent intent = new Intent();
        final int apiLevel = Build.VERSION.SDK_INT;
        if (apiLevel >= 9) { // above 2.3
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts(SCHEME, app.getPackageName(), null);
            intent.setData(uri);
        } else { // below 2.3
            final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22
                    : APP_PKG_NAME_21);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName(APP_DETAILS_PACKAGE_NAME, APP_DETAILS_CLASS_NAME);
            intent.putExtra(appPkgName, app.getPackageName());
        }
        startActivity(intent);
    }

    public long getSizeApp(PackageInfo packageInfo) {
        File file = new File(packageInfo.applicationInfo.sourceDir);
        long size = file.length();
        return size;
    }

    public void initReceiver() {

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        filter.addDataScheme("package");

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("duy.nq", "TwoFragment : onReceiveeeeeeeeeeeeeeeeeeeeeeeee");
                mViewAdapter.notifyDataSetChanged();
            }
        };

        Log.i("duy.nq", "TwoFragment : registerReceiver");
        getContext().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onResume() {
        Log.i("duy.nq", "TwoFragment onResume");
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main_fragment_1, menu);

        SearchManager searchManager = (SearchManager) mContext.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // search action
                return true;
            case R.id.action_name_sort:
                mStyleSort = 0;
                mViewAdapter.getListItem(mArrList, mStyleSort);
                return true;
            case R.id.action_size_sort:
                mStyleSort = 1;
                mViewAdapter.getListItem(mArrList, mStyleSort);
                return true;
            case R.id.action_date_sort:
                // refresh
                return true;
            case R.id.action_check_updates:
                // help action
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroyView() {
        Log.i("duy.nq", "TwoFragment onDestroyView");
        super.onDestroyView();

        getContext().unregisterReceiver(mReceiver);
    }
}
