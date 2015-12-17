package com.example.maxi.swenapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.maxi.swenapp.fragments.ExplorarFragment;
import com.example.maxi.swenapp.fragments.PagesListFragment;
import com.example.maxi.swenapp.fragments.PerfilFragment;


public class MyAdapter extends FragmentPagerAdapter {

    public MyAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        if (position==0){
            fragment = new PerfilFragment();
        }
        if (position==1){
            fragment = PagesListFragment.getInstance();
        }
        if (position==2){
            fragment = new ExplorarFragment();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
