package com.example.maxi.swenapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.example.maxi.swenapp.R;
import com.example.maxi.swenapp.adapters.MyAdapter;

public class TabsActivity extends ActionBarActivity implements ActionBar.TabListener{

    private final String[] mTabsHeader = { "Perfil", "Muro", "Explorar" };

    private ViewPager viewPager;
    private MyAdapter mAdapter;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOnPageChangeListener( new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //todo
            }

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //todo
            }
        });

        actionBar = getSupportActionBar();
        mAdapter = new MyAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        actionBar.addTab(actionBar.newTab()
                .setTabListener(this).setIcon(R.drawable.ic_person_white_24dp));
        actionBar.addTab(actionBar.newTab()
                .setTabListener(this).setIcon(R.drawable.ic_home_white_24dp));
        actionBar.addTab(actionBar.newTab()
                .setTabListener(this).setIcon(R.drawable.ic_playlist_add_check_white_24dp));

        actionBar.setDisplayShowHomeEnabled(true);

        viewPager.setCurrentItem(1);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
