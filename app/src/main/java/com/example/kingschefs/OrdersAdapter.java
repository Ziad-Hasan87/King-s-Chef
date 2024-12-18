package com.example.kingschefs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ordersViewHolder> {

    Context context;
    protected final homeRecyclerInterface homeInterface;
    ArrayList<Orders> ordersList;
    public OrdersAdapter(Context context, ArrayList<Orders> ordersList, homeRecyclerInterface interfaceHome)
    {
        this.context = context;
        this.ordersList = ordersList;
        this.homeInterface = interfaceHome;
    }

    @NonNull
    @Override
    public ordersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_view, parent, false);

        return new ordersViewHolder(v, homeInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ordersViewHolder holder, int position) {
        Orders orders = ordersList.get(position);
        holder.textView.setText("Order#: " + orders.getOrderNo());
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public static class ordersViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ordersViewHolder(@NonNull View itemView, homeRecyclerInterface h) {
            super(itemView);
            textView = itemView.findViewById(R.id.orderNumber);
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
}
