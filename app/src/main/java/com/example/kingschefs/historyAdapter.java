package com.example.kingschefs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class historyAdapter extends RecyclerView.Adapter<historyAdapter.historyViewHolder> {
    Context context;
    protected final homeRecyclerInterface homeInterface;
    public static ArrayList<History> historyList;
    public void updateRT(){
        historyList.clear();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://the-king-s-chefs-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference databaseReference = firebaseDatabase.getReference("History");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                historyList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    History history = dataSnapshot.getValue(History.class);
                    historyList.add(history);
                }
                Collections.reverse(historyList);
                notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public historyAdapter(Context context, ArrayList<History> ordersList, homeRecyclerInterface interfaceHome)
    {
        this.context=context;
        this.historyList = ordersList;
        this.homeInterface = interfaceHome;

    }
    @NonNull
    @Override
    public historyAdapter.historyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_view, parent, false);

        return new historyAdapter.historyViewHolder(v, homeInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull historyAdapter.historyViewHolder holder, int position) {
        History history = historyList.get(position);
        holder.textView.setText("Order#: " + history.getOrderNo());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }
    public static class historyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public historyViewHolder(@NonNull View itemView, homeRecyclerInterface h) {
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