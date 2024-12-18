package com.example.kingschefs;

import static androidx.browser.customtabs.CustomTabsClient.getPackageName;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.trusted.sharing.ShareTarget;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements homeRecyclerInterface{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList <item> itemlist;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private homeAdapter hadapter;
    private Button proceed;
    private Button cancel;
    public String orderDetails, orderAmount;
    RequestQueue requestQueue;

    public HomeFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(requireContext());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataInitialize();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerHome);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setHasFixedSize(true);
        hadapter = new homeAdapter(getContext(), itemlist, (homeRecyclerInterface) this);
        recyclerView.setAdapter(hadapter);
        hadapter.notifyDataSetChanged();
        proceed  = view.findViewById(R.id.proceedButton);
        cancel = view.findViewById(R.id.cancelButton);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderDetails = "";
                orderAmount = "";
                int total = 0;
                for(int i = 0; i < itemlist.size();i++){
                    item temp = itemlist.get(i);
                    if(temp.getQuantity() == 0) continue;
                    else {
                        total += temp.getQuantity()*temp.getPrice();
                        orderDetails = orderDetails  +  temp.getName() + "\n";
                        orderAmount = orderAmount + String.valueOf(temp.getPrice()) + " x" + String.valueOf(temp.getQuantity()) + "\n";
                        temp.setQuantity(0);
                    }
                }
                if(orderDetails.isEmpty()){
                    Toast toast = Toast.makeText(getActivity().getApplicationContext() , "Order is Empty", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    Date currentDate = new Date();

                    // Create SimpleDateFormat objects for date and time
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyMMdd");
                    SimpleDateFormat timeFormatter = new SimpleDateFormat("HHmmss");

                    // Format the date and time
                    String datePart = dateFormatter.format(currentDate); // e.g., "120310"
                    String timePart = timeFormatter.format(currentDate); // e.g., "203422"

                    // Combine into the desired format
                    String str = datePart + timePart;
                    OrdersFragment.orderList.add(new Orders(str,orderDetails, orderAmount, String.valueOf(total)));
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),"Order Placed", Toast.LENGTH_SHORT);
                    toast.show();
                    OrdersFragment.ordersAdapter.notifyDataSetChanged();
                }
                hadapter.notifyDataSetChanged();
            };
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < itemlist.size();i++){
                    item temp = itemlist.get(i);
                    temp.setQuantity(0);
                }
                hadapter.notifyDataSetChanged();
            }
        });
    }

    private void dataInitialize() {
        itemlist = new ArrayList<>();
        String url = "https://api.myjson.online/v1/records/78426dbb-e57a-4e60-8288-9041483817ce";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject dataObject = response.getJSONObject("data");
                            JSONArray jsonArray = dataObject.getJSONArray("menu");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject food = jsonArray.getJSONObject(i);
                                String name = food.getString("name");
                                int price = food.getInt("price");
                                String image = food.getString("image");
                                int resId = getResources().getIdentifier(image, "drawable", requireContext().getPackageName());

                                if (resId == 0) {
                                    Log.w("JSON Parsing", "Drawable not found for image: " + image);
                                }
                                item it = new item(name, price, 0, resId);
                                itemlist.add(it);
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (hadapter != null) {
                                        hadapter.notifyDataSetChanged();  // Update the adapter
                                    }
                                }
                            });
                        }catch (JSONException e){
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error here
                        error.printStackTrace();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onItemClick(int position) {

        item i = itemlist.get(position);
        i.setQuantity(i.getQuantity() + 1);
        hadapter.notifyDataSetChanged();
    }
}