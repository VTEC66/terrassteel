package com.vtec.terrassteel.construction.ui.dashboard.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.ui.ActionBar;
import com.vtec.terrassteel.core.ui.AbstractFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardFragment extends AbstractFragment {

    public static final String TAG = DashboardFragment.class.getSimpleName();

    @BindView(R.id.action_bar)
    ActionBar actionBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View thisView = inflater.inflate(R.layout.dashboard_fragment, container, false);

        ButterKnife.bind(this, thisView);

        actionBar.setTitle(sessionManager.getContruction().getConstructionName());

        return thisView;
    }
}
