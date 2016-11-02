package com.example.gank.gank.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by dell on 2016/9/17.
 */
public class GankMainViewPageAdapter extends FragmentPagerAdapter {
    private String [] titles = {"Android","IOS","WEB","福利"};
    List<Fragment> mFragments;
    @Override
    public int getCount() {
        return mFragments.size();
    }



    public GankMainViewPageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;


    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {

        return mFragments.get(position);
    }




}
