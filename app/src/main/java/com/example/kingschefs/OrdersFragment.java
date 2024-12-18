package com.example.kingschefs;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrdersFragment extends Fragment implements homeRecyclerInterface{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    public static OrdersAdapter ordersAdapter;

    public static ArrayList<Orders> orderList;

    public OrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrdersFragment newInstance(String param1, String param2) {
        OrdersFragment fragment = new OrdersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataInitialize();
        recyclerView = view.findViewById(R.id.recyclerOrders);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.setHasFixedSize(true);
        ordersAdapter = new OrdersAdapter(getContext(), orderList, (homeRecyclerInterface) this);
        recyclerView.setAdapter(ordersAdapter);
        ordersAdapter.notifyDataSetChanged();
    }

    private void dataInitialize() {
        orderList = new ArrayList<Orders>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onItemClick(int position) {
        createPopUp(position);
    }
    private void createPopUp(int position){
        LayoutInflater inflater= (LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popUpView =inflater.inflate(R.layout.orderviewpopup,null);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popUpView,width,height,focusable);
        recyclerView.post(new Runnable(){
            @Override
            public void run() {
                popupWindow.showAtLocation(recyclerView, Gravity.CENTER,0,0);
            }
        });
        TextView orderNumber = popUpView.findViewById(R.id.orderNumberPopUp);
        TextView orderName = popUpView.findViewById(R.id.orderDetailsName);
        TextView orderAmount = popUpView.findViewById(R.id.orderDetailsAmount);
        TextView orderTotal = popUpView.findViewById(R.id.orderTotal);
        Button confirm = popUpView.findViewById(R.id.orderConfirm);
        Button cancel = popUpView.findViewById(R.id.orderCancel);
        Orders orders = orderList.get(position);
        orderNumber.setText("Order#: "+orders.getOrderNo());
        orderName.setText(orders.getOrderName());
        orderAmount.setText(orders.getOrderAmount());
        orderTotal.setText("Total: " + orders.getTotalPrice() +" Taka");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                History history = new History(orderList.get(position).getOrderNo(),orderList.get(position).getOrderName(), orderList.get(position).getOrderAmount(),orderList.get(position).getTotalPrice());

                orderList.remove(position);

                FirebaseDatabase firebaseDatabase;
                DatabaseReference databaseReference;
                firebaseDatabase = FirebaseDatabase.getInstance("https://the-king-s-chefs-default-rtdb.asia-southeast1.firebasedatabase.app/");
                databaseReference = firebaseDatabase.getReference("History");
                databaseReference.push().setValue(history).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Firebase", "Order Completed Successfully"); // Add this line
                        Toast toast = Toast.makeText(getContext(), "Order Completed", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Log.e("FirebaseError", "Error: " + task.getException()); // Add this line
                        Toast toast = Toast.makeText(getContext(), "Failed to complete order", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
                HistoryFragment.historyAdapter.updateRT();
                ordersAdapter.notifyDataSetChanged();
                popupWindow.dismiss();
            }
        });
    }
}