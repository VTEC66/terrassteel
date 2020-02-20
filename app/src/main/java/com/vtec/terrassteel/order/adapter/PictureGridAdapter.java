package com.vtec.terrassteel.order.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.model.Order;
import com.vtec.terrassteel.common.model.Picture;
import com.vtec.terrassteel.order.callback.PictureOrderCallback;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PictureGridAdapter extends RecyclerView.Adapter<PictureGridAdapter.ViewHolder> {

    private final Context context;
    private final Order currentOrder;

    private ArrayList<Picture> elements = new ArrayList<>();
    private PictureOrderCallback callback;

    public PictureGridAdapter(Context context, Order currentOrder) {
        this.context = context;
        this.currentOrder = currentOrder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PictureGridAdapter.ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_order_itemview, parent, false));    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picture picture = elements.get(position);

        String completePath = Environment.getExternalStorageDirectory().toString() + "/terrassteel/commandes/" + currentOrder.getOrderCode() + "-" + currentOrder.getCustomer() + "/" + picture.getPictureName();

        File file = new File(completePath);
        Uri imageUri = Uri.fromFile(file);

        Glide.with(context)
                .load(imageUri)
                .centerCrop()
                .into(holder.pictureHolder);

        holder.itemView.setOnClickListener(v -> callback.onPictureSelected(picture));


    }

    @Override
    public int getItemCount() {
        if (elements.size() > 0)
            return elements.size();
        return 0;
    }

    public void setCallback(PictureOrderCallback callback) {
        this.callback = callback;
    }

    public void setData(ArrayList<Picture> pictures) {
        this.elements = pictures;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.picture)
        ImageView pictureHolder;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}