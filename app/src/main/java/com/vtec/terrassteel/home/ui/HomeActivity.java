package com.vtec.terrassteel.home.ui;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.vtec.terrassteel.R;
import com.vtec.terrassteel.core.ui.AbstractActivity;
import com.vtec.terrassteel.home.adapter.HomePagerAdapter;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AbstractActivity {

    @BindView(R.id.home_viewpager)
    public ViewPager pager;

    @BindView(R.id.home_tabs)
    public TabLayout tabs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_activity);
        ButterKnife.bind(this);

        pager.setAdapter(new HomePagerAdapter(this.getSupportFragmentManager(), getBaseContext()));

        tabs.setupWithViewPager(pager);
        tabs.setTabMode(TabLayout.MODE_FIXED);

        setupTabIcons();
    }

    private void setupTabIcons() {
        tabs.getTabAt(HomePagerAdapter.CONSTRUCTION).setIcon(R.drawable.ic_constrcution);
        tabs.getTabAt(HomePagerAdapter.COMPANY).setIcon(R.drawable.ic_business);
    }


}
