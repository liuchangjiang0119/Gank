package com.example.gank.gank;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gank.R;
import com.example.gank.gank.adapter.GankMainViewPageAdapter;
import com.example.gank.gank.fragment.AndroidFragment;
import com.example.gank.gank.fragment.ImageFragment;
import com.example.gank.gank.fragment.IosFragment;
import com.example.gank.gank.fragment.WebFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 2016/9/16.
 */
public class GankMainFragment extends Fragment{

    @BindView(R.id.tabs)
    TabLayout mTabLayout;


    @BindView(R.id.viewPage)
    ViewPager mViewPager;

    GankMainViewPageAdapter mViewPageAdapter;

    List<Fragment> fragmemts = new ArrayList<>();
    String [] tabTitles = new String[] {
            "ANDROID","iOS","WEB","福利"
    };
    public void initTab(){


        fragmemts.add(AndroidFragment.newInsance());
        fragmemts.add(IosFragment.newInstance());
        fragmemts.add(WebFragment.newInstance());

        fragmemts.add(ImageFragment.newInstance());
        mViewPageAdapter = new GankMainViewPageAdapter(getChildFragmentManager(),fragmemts);
        mViewPager.setAdapter(mViewPageAdapter);

        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());

        mTabLayout.setupWithViewPager(mViewPager);
//        不显示文字，需重新设置一遍


        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gank_main_fragment,container,false);
        ButterKnife.bind(this,view);

        initTab();
        return view;
    }



}
