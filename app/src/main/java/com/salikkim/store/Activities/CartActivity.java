package com.salikkim.store.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.salikkim.store.Adapters.AddrAdapter;
import com.salikkim.store.Adapters.CartAdapter;
import com.salikkim.store.Helper.VolleyEventListener;
import com.salikkim.store.Helper.VolleyRequest;
import com.salikkim.store.Models.Addresses;
import com.salikkim.store.Models.Cart;
import com.salikkim.store.R;
import com.salikkim.store.databinding.ActivityCartBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private String cart_url = "https://hawtie.000webhostapp.com/salikkim_store/cart.php";
    private ActivityCartBinding cartBinding;
    private CartAdapter cartAdapter;
    private List<Cart> cartList;
    private String[] keys = new String[]{"user_id"};
    private float totalPrice;
    private float totalDiscounts;
    private float originalPrice;
    private ArrayList<Addresses> address_lists;
    private String text_select_address = "Select";
    private String address_url = "https://hawtie.000webhostapp.com/salikkim_store/getaddress.php";
    private AddrAdapter addrAdapter;
    private String addr_id = "";
    private String user_id = "";
    private String[] del_keys = new String[]{"id", "column", "table"};
    private String table = "Cart";
    private String del_url = "https://hawtie.000webhostapp.com/salikkim_store/delete.php";
    private String column = "Cart_Id";
    private String[] set_keys = new String[]{"id", "qnty"};
    private String set_url = "https://hawtie.000webhostapp.com/salikkim_store/cart_qnty.php";
    private String[] order_key = new String[]{"user_id"};
    private String add_order_url = "https://hawtie.000webhostapp.com/salikkim_store/add_order.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cartBinding = ActivityCartBinding.inflate(getLayoutInflater());
        View view = cartBinding.getRoot();
        setContentView(view);
        cartBinding.toolbarCartActivity.setTitle("CART");
        setSupportActionBar(cartBinding.toolbarCartActivity);
        cartBinding.toolbarCartActivity.setNavigationIcon(R.drawable.ic_arrow_back);
        cartBinding.toolbarCartActivity.setNavigationOnClickListener(v -> finish());
        addr_id = getIntent().getExtras().getString("address_id");
        user_id = getIntent().getExtras().getString("user_id");

        if (!user_id.equals("")) {
            cartBinding.shimmerCart.setVisibility(View.VISIBLE);
            getAddress(addr_id);
            getCartList();
        } else {
            cartBinding.cartBtnLogin.setVisibility(View.VISIBLE);
            cartBinding.cartBtnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(CartActivity.this, OtpSendActivity.class));
                    finish();
                }
            });
        }
        cartBinding.cartAddrSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Addresses clickedItem = (Addresses) adapterView.getItemAtPosition(i);
                addr_id = clickedItem.getAddr_id();
                cartAdapter.setAvailableAddress(addr_id);
                cartBinding.tvProceedAlert.setText("");
                cartBinding.tvProceedAlert.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getAddress(String id) {
        String[] key = new String[]{};
        String[] value = new String[]{};
        address_lists = new ArrayList<>();
        address_lists.add(new Addresses("", text_select_address));
        new VolleyRequest(key, value, address_url, getApplicationContext(), new VolleyEventListener<String>() {
            @Override
            public void onSuccess(String object) {
                try {
                    JSONArray jsonArray = new JSONArray(object);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        address_lists.add(new Addresses(jsonObject.getString("Addr_Id"), jsonObject.getString("Address")));
                    }
                    addrAdapter = new AddrAdapter(CartActivity.this, address_lists);
                    cartBinding.cartAddrSpinner.setAdapter(addrAdapter);

                    for (int i = 0; i < address_lists.size(); i++) {
                        if (id.equals(address_lists.get(i).getAddr_id())) {
                            cartBinding.cartAddrSpinner.setSelection(i);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getCartList() {
        cartList = new ArrayList<>();
        totalPrice = 0;
        originalPrice = 0;
        totalDiscounts = 0;
        String[] value = new String[]{user_id};
        new VolleyRequest(keys, value, cart_url, CartActivity.this, new VolleyEventListener<String>() {
            @Override
            public void onSuccess(String object) {
                try {
                    JSONArray jsonArray = new JSONArray(object);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int cart_id = jsonObject.getInt("Cart_id");
                        int product_id = jsonObject.getInt("Product_id");
                        int size_id = jsonObject.getInt("Size_id");
                        String title = jsonObject.getString("Name");
                        String thumbnail = jsonObject.getString("Thumbnail");
                        String seller_name = jsonObject.getString("Seller_name");
                        int price = jsonObject.getInt("Price");
                        int sale_price = jsonObject.getInt("Sale_price");
                        String color = jsonObject.getString("Color");
                        String size = jsonObject.getString("Size");
                        int qnty = jsonObject.getInt("Qnty");
                        int total_qnty = jsonObject.getInt("Total_qnty");
                        String available_adresses = jsonObject.getString("Available_addresses");
                        cartList.add(new Cart(title, thumbnail, seller_name, color, size, available_adresses, cart_id, price * qnty, sale_price * qnty, qnty, total_qnty));
                        totalPrice = totalPrice + (sale_price * qnty);
                        totalDiscounts = totalDiscounts + ((price * qnty) - (sale_price) * qnty);
                        originalPrice = originalPrice + (price * qnty);
                    }
                    setUpView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setUpView() {
        cartAdapter = new CartAdapter(CartActivity.this, cartList, addr_id);
        cartBinding.recyclerViewCart.setHasFixedSize(true);
        cartBinding.recyclerViewCart.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        cartBinding.recyclerViewCart.setAdapter(cartAdapter);
        cartBinding.originalPrice.setText(getApplicationContext().getString(R.string.Rs) + originalPrice);
        cartBinding.totalPrice.setText(getApplicationContext().getString(R.string.Rs) + totalPrice);
        cartBinding.totalDiscounts.setText("-" + getApplicationContext().getString(R.string.Rs) + totalDiscounts);
        cartBinding.cartTotalText.setText(getApplicationContext().getString(R.string.Rs) + totalPrice);
        cartBinding.shimmerCart.setVisibility(View.GONE);
        cartBinding.cartChangeAddress.setVisibility(View.VISIBLE);
        cartBinding.cartNestedScroll.setVisibility(View.VISIBLE);
        cartBinding.cartBottomCheckout.setVisibility(View.VISIBLE);


        cartAdapter.setOnDelClickListener(new CartAdapter.DelClickListener() {
            @Override
            public void mClick(int position) {
                delete(cartList.get(position).getCart_id());
            }
        });

        cartAdapter.setQntyListener(new CartAdapter.OnQuantitySetListener() {
            @Override
            public void qClick(View v, int position) {
                PopupMenu popup = new PopupMenu(CartActivity.this, v);
                int qt = cartList.get(position).getTotal_qnty();
                for (int i = 1; i < qt + 1; i++) {
                    popup.getMenu().add(Menu.NONE, i, i, "" + i);
                }

                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        updateQnty(cartList.get(position).getCart_id(), menuItem.getItemId());
                        return false;
                    }
                });
                popup.show();
            }
        });

        cartBinding.btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAvail() != cartList.size()) {
                    cartBinding.tvProceedAlert.setText("Some item is not matching. Please check the available address or quantity");
                    cartBinding.tvProceedAlert.setVisibility(View.VISIBLE);
                }else {
                    checkoutProceed();
                }
            }

        });

    }

    private int checkAvail() {
        int count = 0;
        for (int i = 0; i < cartList.size(); i++) {
            String all_vals = cartList.get(i).getAvailable_adresses();
            List<String> list = Arrays.asList(all_vals.split(","));
            if (!list.contains(addr_id) | cartList.get(i).getQnty() > cartList.get(i).getTotal_qnty()) {
                count--;
            } else {
                count++;
            }
        }
        return count;
    }

    private void checkoutProceed() {
        order_key = new String[]{"user_id"};
        String[] value = new String[]{user_id};
        new VolleyRequest(order_key, value, add_order_url, getApplicationContext(), new VolleyEventListener<String>() {
            @Override
            public void onSuccess(String object) {
                Toast.makeText(CartActivity.this, "" + object, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateQnty(int cart_id, int qnt) {
        cartBinding.tvProceedAlert.setText("");
        cartBinding.tvProceedAlert.setVisibility(View.GONE);
        String[] value = new String[]{String.valueOf(cart_id), String.valueOf(qnt)};
        new VolleyRequest(set_keys, value, set_url, CartActivity.this, new VolleyEventListener<String>() {
            @Override
            public void onSuccess(String object) {
                if (object.equalsIgnoreCase("Updated")) {
                    getCartList();
                } else {
                    Toast.makeText(CartActivity.this, object, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void delete(int cart_id) {
        String[] value = new String[]{String.valueOf(cart_id), column, table};
        new VolleyRequest(del_keys, value, del_url, CartActivity.this, new VolleyEventListener<String>() {
            @Override
            public void onSuccess(String object) {
                if (object.equalsIgnoreCase("Deleted")) {
                    getCartList();
                }
            }
        });
    }
}