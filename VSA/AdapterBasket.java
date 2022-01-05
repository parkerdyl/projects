package com2027.cw.dp00405.vsa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterBasket extends RecyclerView.Adapter<AdapterBasket.ViewHolder> {

    private ArrayList<Integer> images;
    private ArrayList<String> brands;
    private ArrayList<String> models;
    private ArrayList<String> prices;
    private Context context;

    public AdapterBasket(Context context, ArrayList<Integer> images, ArrayList<String> brands, ArrayList<String> models, ArrayList<String> prices) {
        this.images = images;
        this.brands = brands;
        this.models = models;
        this.prices = prices;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (images != null && images.get(position) != null) {
            holder.imageViewPadLock.setImageResource(images.get(position));
        } else if (images == null){
            holder.imageViewPadLock.setImageResource(R.drawable.ic_lock_outline_red_32dp);
        } else if (images.get(position) == null) {
            holder.imageViewPadLock.setImageResource(R.drawable.ic_lock_open_757575_32dp);
        }
        holder.textViewBrand.setText(brands.get(position));
        holder.textViewModel.setText(models.get(position));
        holder.textViewPrice.setText(prices.get(position));

        /*holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                return true;
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return brands.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewPadLock;
        TextView textViewBrand;
        TextView textViewModel;
        TextView textViewPrice;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPadLock = itemView.findViewById(R.id.imageLock);
            textViewBrand = itemView.findViewById(R.id.tPhoneBrand);
            textViewModel = itemView.findViewById(R.id.tPhoneModel);
            textViewPrice = itemView.findViewById(R.id.tPhonePrice);
            relativeLayout = itemView.findViewById(R.id.relative_layout);
        }
    }

}
