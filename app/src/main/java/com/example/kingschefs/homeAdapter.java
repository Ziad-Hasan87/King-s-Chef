package com.example.kingschefs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class homeAdapter extends RecyclerView.Adapter<homeAdapter.homeViewHolder> {
    Context context;
    private final homeRecyclerInterface homeInterface;
    ArrayList<item> itemArrayList;
    public static class homeViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView  name;
        TextView price;
        TextView quantity;
        public homeViewHolder(@NonNull View itemView, homeRecyclerInterface h) {
            super(itemView);
            image = itemView.findViewById(R.id.itemImage);
            name = itemView.findViewById(R.id.itemName);
            price = itemView.findViewById(R.id.itemPrice);
            quantity = itemView.findViewById(R.id.itemQuantity);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(h != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            h.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public homeAdapter(Context context, ArrayList<item> itemArrayList, homeRecyclerInterface interfaceHome) {
        this.context = context;
        this.itemArrayList = itemArrayList;
        this.homeInterface = interfaceHome;
    }

    @NonNull
    @Override
    public homeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_view,parent, false);
        return new homeViewHolder(v, homeInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull homeViewHolder holder, int position) {
        item items = itemArrayList.get(position);
        holder.name.setText(items.getName());
        holder.price.setText(String.valueOf(items.getPrice() )+ " Taka");
        holder.quantity.setText("x" + String.valueOf(items.getQuantity()));
        holder.image.setImageResource(items.getImage());
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }
}
