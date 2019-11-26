package com.vtec.terrassteel.main.ui.workingorder.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.core.ui.AbstractFragment;
import com.vtec.terrassteel.main.ui.dashboard.ui.DashboardFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;

public class WorkingOrderFragment extends AbstractFragment {

    public static final String TAG = WorkingOrderFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View thisView = inflater.inflate(R.layout.working_order_fragment, container, false);

        ButterKnife.bind(this, thisView);

        return thisView;
    }
}
