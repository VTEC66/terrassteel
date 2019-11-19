package com.vtec.terrassteel.home.company.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.core.ui.AbstractFragment;
import com.vtec.terrassteel.home.construction.ui.MyConstructionsFragment;

import androidx.fragment.app.Fragment;

public class MyCompanyFragment extends AbstractFragment {

    public static Fragment newInstance() {
        return (new MyCompanyFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_company_fragment, container, false);
    }
}
