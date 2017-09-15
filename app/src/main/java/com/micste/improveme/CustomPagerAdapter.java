package com.micste.improveme;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by micste on 2017-08-28.
 */

public class CustomPagerAdapter extends FragmentPagerAdapter {

    final int pages = 2;
    private String tabTitles[] = new String[] { "Active", "Completed" };
    private Context context;

    public CustomPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment;

        switch (position+1) {
            case 1:
                fragment = new ActiveGoalsFragment();
                break;
            case 2:
                fragment = new CompletedGoalsFragment();
                break;
            default:
                fragment = null;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return pages;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
