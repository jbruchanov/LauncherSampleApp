package com.scurab.android.launchersampleapp;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

/**
 * Created by jiribruchanov on 9/22/16.
 */

public class AppDetail implements Comparable<AppDetail> {
    private final CharSequence mLabel;
    private final String mLabelStr;
    private final String mName;
    private final Drawable mIcon;

    public AppDetail(ResolveInfo info, PackageManager packageManager) {
        mLabel = info.loadLabel(packageManager);
        mLabelStr = mLabel.toString();
        mName = info.activityInfo.packageName;
        mIcon = info.activityInfo.loadIcon(packageManager);
    }

    public CharSequence getLabel() {
        return mLabel;
    }

    public String getName() {
        return mName;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    @Override
    public int compareTo(AppDetail o) {
        return mLabelStr.compareTo(o.mLabelStr);
    }
}
