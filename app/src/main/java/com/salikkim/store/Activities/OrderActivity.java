package com.salikkim.store.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.salikkim.store.Adapters.OrdersAdapter;
import com.salikkim.store.Helper.VolleyEventListener;
import com.salikkim.store.Helper.VolleyRequest;
import com.salikkim.store.Models.Orders;
import com.salikkim.store.R;
import com.salikkim.store.databinding.ActivityOrderBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private List<Orders> ordersList = new ArrayList<>();
    private ActivityOrderBinding orderBinding;
    private String user_id = "";
    private String[] keys = new String[]{"user_id"};
    private OrdersAdapter ordersAdapter;
    private String order_url= "https://hawtie.000webhostapp.com/salikkim_store/orders.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderBinding = ActivityOrderBinding.inflate(getLayoutInflater());
        View view = orderBinding.getRoot();
        setContentView(view);
        orderBinding.toolbarOrderActivity.setTitle("MY ORDERS");
        setSupportActionBar(orderBinding.toolbarOrderActivity);
        orderBinding.toolbarOrderActivity.setNavigationIcon(R.drawable.ic_arrow_back);
        orderBinding.toolbarOrderActivity.setNavigationOnClickListener(v -> finish());
        if (savedInstanceState == null)
            user_id = getIntent().getExtras().getString("user_id");

        if (!user_id.equals("")) {
            getOrderLists();
        }
    }

    private void getOrderLists() {
        String[] value = new String[]{user_id};
        new VolleyRequest(keys, value, order_url, OrderActivity.this, new VolleyEventListener<String>() {
            @Override
            public void onSuccess(String object) {
                try {
                    JSONArray jsonArray = new JSONArray(object);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int order_id = jsonObject.getInt("Order_id");
                        String title = jsonObject.getString("Name");
                        String thumbnail = jsonObject.getString("Thumbnail");
                        int price = jsonObject.getInt("Price");
                        int sale_price = jsonObject.getInt("Sale_price");
                        String color = jsonObject.getString("Color");
                        String size = jsonObject.getString("Size");
                        int qnty = jsonObject.getInt("Qnty");
                        String seller_name = jsonObject.getString("Seller_name");
                        String order_date = jsonObject.getString("Order_date");
                        int status = jsonObject.getInt("Status");
                        String status_info = jsonObject.getString("Status_info");
                        ordersList.add(new Orders(title, thumbnail, seller_name, color, size, order_date,status_info, order_id, price, sale_price,qnty,status));
                    }
                    ordersAdapter = new OrdersAdapter(OrderActivity.this, ordersList);
                    orderBinding.recyclerViewOrder.setHasFixedSize(true);
                    orderBinding.recyclerViewOrder.setLayoutManager(new LinearLayoutManager(OrderActivity.this));
                    orderBinding.recyclerViewOrder.setAdapter(ordersAdapter);
                    orderBinding.shimmerOrder.setVisibility(View.GONE);
                    orderBinding.recyclerViewOrder.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}