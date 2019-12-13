package com.vtec.terrassteel.home.company.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.core.ui.AbstractFragment;
import com.vtec.terrassteel.home.company.customer.ui.ListCustomersActivity;
import com.vtec.terrassteel.home.company.employee.ui.ListEmployeesActivity;

import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyCompanyFragment extends AbstractFragment {

    @OnClick(R.id.employee_management_view)
    public void onClickEmployeeManagement(){
        startActivity(new Intent(getContext(), ListEmployeesActivity.class));
    }


    public static Fragment newInstance() {
        return (new MyCompanyFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_company_fragment, container, false);

        ButterKnife.bind(this, view);

        return view;
    }
}
