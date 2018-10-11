package com.example.dickyeka.garasibuku;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by DICKYEKA on 08/05/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    final Context context;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = FragmentUmum.newInstance();
                break;
            case 1:
                fragment = FragmentKhusus.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getResources().getString(R.string.tab_1);
            case 1:
                return context.getResources().getString(R.string.tab_2);
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}