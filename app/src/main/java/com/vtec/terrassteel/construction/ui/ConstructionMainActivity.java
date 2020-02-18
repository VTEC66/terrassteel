package com.vtec.terrassteel.construction.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vtec.terrassteel.R;
import com.vtec.terrassteel.core.ui.AbstractActivity;
import com.vtec.terrassteel.home.ui.HomeActivity;
import com.vtec.terrassteel.construction.ui.dashboard.ui.DashboardFragment;
import com.vtec.terrassteel.construction.ui.imputation.ui.ImputationFragment;
import com.vtec.terrassteel.construction.ui.workorder.ui.WorkOrderFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ConstructionMainActivity extends AbstractActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        ButterKnife.bind(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_imputation_time:
                        showImputationTimeFragment();
                        break;
                    case R.id.action_work_order:
                        showWorkingOrderFragment();
                        break;
                    case R.id.action_dashboard:
                        showDashboardFragment();
                        break;
                }
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.action_imputation_time);
    }

    private void showFragment(Fragment fragment, String TAG) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.main_container, fragment, TAG);
        transaction.commitAllowingStateLoss();
    }

    private void showImputationTimeFragment() {
        Log.d(ConstructionMainActivity.class.getSimpleName(), "Show imputation time Fragment");

        ImputationFragment fragment = (ImputationFragment) getSupportFragmentManager().findFragmentByTag(ImputationFragment.TAG);

        if (fragment == null) {
            fragment = new ImputationFragment();
        }

        showFragment(fragment, ImputationFragment.TAG);
    }

    private void showDashboardFragment() {
        Log.d(ConstructionMainActivity.class.getSimpleName(), "Show dashboard Fragment");

        DashboardFragment fragment = (DashboardFragment) getSupportFragmentManager().findFragmentByTag(DashboardFragment.TAG);

        if (fragment == null) {
            fragment = new DashboardFragment();
        }

        showFragment(fragment, DashboardFragment.TAG);
    }

    private void showWorkingOrderFragment() {
        Log.d(ConstructionMainActivity.class.getSimpleName(), "Show working order Fragment");

        WorkOrderFragment fragment = (WorkOrderFragment) getSupportFragmentManager().findFragmentByTag(WorkOrderFragment.TAG);

        if (fragment == null) {
            fragment = new WorkOrderFragment();
        }

        showFragment(fragment, WorkOrderFragment.TAG);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(
                R.anim.fade_in,
                R.anim.fade_out
        );
        finishAffinity();
    }
}
