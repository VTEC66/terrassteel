package com.vtec.terrassteel.home.adapter;

import android.content.Context;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.home.company.ui.MyCompanyFragment;
import com.vtec.terrassteel.home.construction.ui.MyConstructionsFragment;
import com.vtec.terrassteel.home.order.ui.MyOrdersFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class HomePagerAdapter extends FragmentPagerAdapter {

    Context context;

    public static final int CONSTRUCTION = 0;
    public static final int ORDER = 1;
    public static final int COMPANY = 2;

    public HomePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case CONSTRUCTION:
                return context.getString(R.string.tab_construction);
            case ORDER:
                return context.getString(R.string.tab_order);
            case COMPANY:
                return context.getString(R.string.tab_company);
            default:
                return null;
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case CONSTRUCTION: //Page number 1
                return MyConstructionsFragment.newInstance();
            case ORDER: //Page number 2
                return MyOrdersFragment.newInstance();
            case COMPANY: //Page number 3
                return MyCompanyFragment.newInstance();
            default:
                return null;
        }
    }

}
