package com.example.application.tabLayoutExample.tapLayoutExample.adapter;

/**
 * Created by 8470p on 9/7/2016.
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.application.tabLayoutExample.R;
import com.example.application.tabLayoutExample.tapLayoutExample.OneFragment;
import com.example.application.tabLayoutExample.tapLayoutExample.model.AppInfo;
import com.example.application.tabLayoutExample.tapLayoutExample.model.CompareApps;


public class ViewAppAdapter extends ArrayAdapter<AppInfo> implements Filterable {
    private AppFilter mAppFilter;
    private List<AppInfo> mAppsList;
    private List<AppInfo> mFilteredList;
    private Context mContext;
    private SparseBooleanArray mSelectedItemsIds;

    public ViewAppAdapter(Context context, int textViewResourceId, List<AppInfo> appsList) {
        super(context, textViewResourceId, appsList);
        mSelectedItemsIds = new SparseBooleanArray();
        mContext = context;
        mAppsList = appsList;
        mFilteredList = appsList;
        getFilter();
    }

    @Override
    public int getCount() {
        return ((null != mFilteredList) ? mFilteredList.size() : 0);
    }

    @Override
    public AppInfo getItem(int position) {
        return ((null != mFilteredList) ? mFilteredList.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        AppInfo applicationInfo = getItem(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.app_item_layout, null);

            holder = new ViewHolder();

            holder.appName = (TextView) convertView.findViewById(R.id.app_name);
            holder.sizeApp = (TextView) convertView.findViewById(R.id.app_size);
            holder.iconView = (ImageView) convertView.findViewById(R.id.app_icon);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (null != applicationInfo) {
            holder.appName.setText(applicationInfo.getName());
            holder.sizeApp.setText(android.text.format.Formatter.formatFileSize(mContext, applicationInfo.getSize()));
            holder.iconView.setImageDrawable(applicationInfo.getIcon());
        }
        return convertView;
    }

    @Override
    public void remove(AppInfo object) {
        mAppsList.remove(object);
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    private static class ViewHolder {
        public TextView appName;
        public TextView sizeApp;
        public ImageView iconView;
    }

    @Override
    public Filter getFilter() {
        if (mAppFilter == null) {
            mAppFilter = new AppFilter();
        }

        return mAppFilter;
    }

    /**
     * Custom filter for friend list
     * Filter content in friend list according to the search text
     */
    private class AppFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<AppInfo> tempList = new ArrayList<>();

                // search content in friend list
                for (AppInfo app : mAppsList) {
                    if (app.getName().toString().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))) {
                        tempList.add(app);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = mAppsList.size();
                filterResults.values = mAppsList;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mFilteredList = (List<AppInfo>) results.values;
            Collections.sort(mFilteredList, new CompareApps(OneFragment.mStyleSort));
            notifyDataSetChanged();
        }
    }

    public void getListItem(ArrayList<AppInfo> appInfos, int style) {
        mAppsList = appInfos;
        Collections.sort(mAppsList, new CompareApps(style));
        notifyDataSetChanged();
    }
}