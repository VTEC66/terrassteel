package com.vtec.terrassteel.main.ui.imputation.adapter;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.model.Assign;
import com.vtec.terrassteel.common.model.Imputation;
import com.vtec.terrassteel.core.model.DefaultResponse;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.database.DatabaseManager;
import com.vtec.terrassteel.main.ui.imputation.callback.ImputationCallback;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ImputationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int HEADER = 1;
    private static final int ITEM = 2;


    private Context context;
    private ArrayList<Assign> elements = new ArrayList<>();
    private ImputationCallback callback;

    public ImputationAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case HEADER:
            default:
                return new HeaderViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.imputation_header_itemview, parent, false));
            case ITEM:
                return new ItemViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.imputation_item_itemview, parent, false));
        }
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Assign assign = elements.get(position);

        switch (holder.getItemViewType()) {
            case HEADER:
                HeaderViewHolder headerViewHolder = (HeaderViewHolder)holder;

                headerViewHolder.workorderNameTextView.setText(context.getString(R.string.work_order,assign.getWorkOrder().getWorkOrderReference()));

                headerViewHolder.addAsignView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.addAssignToWorkOrder(assign.getWorkOrder());
                    }
                });

                headerViewHolder.workorderNameTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.workOrderSelected(assign.getWorkOrder());
                    }
                });
                break;

            case ITEM:
                ItemViewHolder itemViewHolder = (ItemViewHolder)holder;

                itemViewHolder.employeeNameTextView.setText(assign.getEmployee().getEmployeeName());
                itemViewHolder.setupChronometer(assign);

                itemViewHolder.setDrawableForActionView();

                itemViewHolder.actionView.setOnClickListener(v -> {

                    if(itemViewHolder.isWorking){

                        itemViewHolder.chronometer.stop();

                        DatabaseManager.getInstance(context).stopImputation(itemViewHolder.imputation, new DatabaseOperationCallBack<DefaultResponse>() {
                            @Override
                            public void onSuccess(DefaultResponse defaultResponse) {
                                itemViewHolder.isWorking = false;
                            }
                        });

                    }else{

                        DatabaseManager.getInstance(context).startImputation(assign, new DatabaseOperationCallBack<DefaultResponse>() {
                            @Override
                            public void onSuccess(DefaultResponse defaultResponse) {
                                itemViewHolder.isWorking = true;
                            }
                        });
                    }

                    itemViewHolder.setupChronometer(assign);
                    itemViewHolder.setDrawableForActionView();
                });

                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        Assign assign = elements.get(position);

        if(assign.employee == null){
            return HEADER;
        }else{
            return ITEM;
        }
    }

    @Override
    public int getItemCount() {
        if (elements.size() > 0)
            return elements.size();
        return 0;
    }

    public void setCallback(ImputationCallback callback) {
        this.callback = callback;
    }

    public void setData(ArrayList<Assign> elements) {
        this.elements = elements;
    }


    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.workorder_name_tv)
        TextView workorderNameTextView;

        @BindView(R.id.add_assign_view)
        View addAsignView;


        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.employee_name_tv)
        TextView employeeNameTextView;

        @BindView(R.id.chronometer_view)
        Chronometer chronometer;

        @BindView(R.id.action_view)
        ImageView actionView;

        Imputation imputation;
        boolean isWorking;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            imputation = null;
        }

        public void setDrawableForActionView(){
            if(isWorking){
                actionView.setImageDrawable(context.getDrawable(R.drawable.ic_pause_circle));
            }else{
                actionView.setImageDrawable(context.getDrawable(R.drawable.ic_play_circle));
            }
        }

        public void setupChronometer(Assign assign) {

            imputation = DatabaseManager.getInstance(context).getLastImputationForAssign(assign.getAssignId());

            chronometer.setBase(getBaseTime(assign));

            if(imputation != null){
                if(imputation.getImputationEnd() == 0){
                    this.isWorking = true;
                    chronometer.start();
                }else{
                    this.isWorking = false;
                }
            }else{
                this.isWorking = false;
            }
        }

        private long getBaseTime(Assign assign){
            if(imputation == null){
                return SystemClock.elapsedRealtime();
            }else{
                int totalTime = DatabaseManager.getInstance(context).getConsumedTimeForAssign(assign); //TODO
                Log.d(this.getClass().getSimpleName(), "Total time for this assign : " + (SystemClock.elapsedRealtime() - totalTime));
                return SystemClock.elapsedRealtime() - totalTime; //imputation.imputationTotalTime * 1000;
            }
        }
    }



}
