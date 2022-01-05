package com2027.cw.dp00405.vsa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterBook extends RecyclerView.Adapter<AdapterBook.ViewHolder> {

    private ArrayList<String> titles;
    private ArrayList<String> descriptions;
    private Context context;

    Integer list = R.layout.list_bookmark;

    public AdapterBook(Context context, ArrayList<String> titles, ArrayList<String> descriptions) {
        this.titles = titles;
        this.descriptions = descriptions;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewTitle.setText(titles.get(position));
        holder.textViewDesc.setText(descriptions.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        TextView textViewDesc;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tBookTitle);
            textViewDesc = itemView.findViewById(R.id.tBookDesc);
            relativeLayout = itemView.findViewById(R.id.relative_layout_book);
        }
    }

}
