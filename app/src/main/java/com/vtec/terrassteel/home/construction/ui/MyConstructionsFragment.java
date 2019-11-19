package com.vtec.terrassteel.home.construction.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.model.Construction;
import com.vtec.terrassteel.common.model.ConstructionStatus;
import com.vtec.terrassteel.core.ui.AbstractFragment;
import com.vtec.terrassteel.home.construction.adapter.ConstructionAdapter;
import com.vtec.terrassteel.home.construction.callback.ConstructionCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyConstructionsFragment extends AbstractFragment implements ConstructionCallback {

    private static final int ADD_CONSTRUCTION_INTENT_CODE = 12;

    @BindView(R.id.construction_listview)
    RecyclerView constructionRecyclerView;

    @OnClick(R.id.add_fab)
    public void onClicAddButton(){
        startActivityForResult(new Intent(getContext(), AddConstructionActivity.class), ADD_CONSTRUCTION_INTENT_CODE);
        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    private ConstructionAdapter constructionAdapter;

    public static Fragment newInstance() {
        return (new MyConstructionsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_constructions_fragment, container, false);

        ButterKnife.bind(this, view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        constructionRecyclerView.setLayoutManager(linearLayoutManager);

        constructionAdapter = new ConstructionAdapter(getContext());
        constructionAdapter.setData(makeMock());
        constructionAdapter.setCallback(this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(constructionRecyclerView.getContext(),
                linearLayoutManager.getOrientation());

        constructionRecyclerView.addItemDecoration(dividerItemDecoration);
        constructionRecyclerView.setAdapter(constructionAdapter);

        return view;
    }

    private ArrayList<Construction> makeMock() {
        ArrayList<Construction> mokeConstructions = new ArrayList<>();

        mokeConstructions.add(new Construction("Chantier perpignan", "Client Conseil Générale", ConstructionStatus.IN_PROGRESS));

        return mokeConstructions;
    }

    @Override
    public void onConstructionSelected(Construction construction) {

    }
}
