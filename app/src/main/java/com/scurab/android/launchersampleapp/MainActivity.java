package com.scurab.android.launchersampleapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        loadApps();
    }

    @Override
    public void onBackPressed() {
        //do nothing main activity does nothing
    }

    private void loadApps() {
        final PackageManager packageManager = getPackageManager();
        final Adapter adapter = new Adapter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Adapter a = (Adapter) mRecyclerView.getAdapter();
                int adapterPosition = mRecyclerView.getChildViewHolder(v).getAdapterPosition();
                AppDetail item = a.getItem(adapterPosition);
                Intent launchIntentForPackage = packageManager.getLaunchIntentForPackage(item.getName());
                startActivity(launchIntentForPackage);
            }
        });

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false));
        new AsyncTask<Void, Integer, List<AppDetail>>() {

            @Override
            protected List<AppDetail> doInBackground(Void... params) {

                Intent i = new Intent(Intent.ACTION_MAIN, null);
                i.addCategory(Intent.CATEGORY_LAUNCHER);

                List<ResolveInfo> availableActivities = packageManager.queryIntentActivities(i, 0);
                List<AppDetail> result = new ArrayList<>();
                for (ResolveInfo ri : availableActivities) {
                    result.add(new AppDetail(ri, packageManager));
                }
                Collections.sort(result);
                return result;
            }

            @Override
            protected void onPostExecute(List<AppDetail> appDetails) {
                adapter.setData(appDetails, true);
            }
        }.execute();
    }


    public static class Adapter extends RecyclerView.Adapter<MyViewHolder> {

        private final View.OnClickListener mClickListener;
        private List<AppDetail> mData;

        public Adapter(View.OnClickListener clickListener) {
            mClickListener = clickListener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = View.inflate(parent.getContext(), R.layout.app_icon, null);
            return new MyViewHolder(inflate, mClickListener);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            AppDetail appDetail = mData.get(position);
            holder.title.setText(appDetail.getLabel());
            holder.icon.setImageDrawable(appDetail.getIcon());
        }

        @Override
        public int getItemCount() {
            return mData != null ? mData.size() : 0;
        }

        public void setData(List<AppDetail> data, boolean notify) {
            mData = data;
            if (notify) {
                notifyDataSetChanged();
            }
        }

        public AppDetail getItem(int position) {
            return mData.get(position);
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView icon;

        public MyViewHolder(View itemView, View.OnClickListener clickListener) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            title = (TextView) itemView.findViewById(R.id.title);
            itemView.setOnClickListener(clickListener);
        }
    }
}
