package com.vtec.terrassteel.home.company.employee.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.home.company.employee.adapter.EmployeesAdapter;
import com.vtec.terrassteel.home.company.employee.adapter.JobAdapter;
import com.vtec.terrassteel.home.company.employee.callback.SelectJobCallback;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectJobDialogFragment extends DialogFragment {

    private SelectJobCallback callback;

    @BindView(R.id.job_recyclerview)
    RecyclerView jobRecyclerView;


    public SelectJobDialogFragment setCallBack(SelectJobCallback callBack) {
        this.callback = callBack;
        return this;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle state) {
        super.onCreateView(inflater, parent, state);

        View thisView = inflater.inflate(R.layout.select_job_dialog_fragment, parent, false);

        ButterKnife.bind(this, thisView);

        JobAdapter jobAdapter = new JobAdapter(callback);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        jobRecyclerView.setLayoutManager(linearLayoutManager);

        jobRecyclerView.addItemDecoration(new DividerItemDecoration(jobRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        jobRecyclerView.setAdapter(jobAdapter);

        return thisView;
    }

}
