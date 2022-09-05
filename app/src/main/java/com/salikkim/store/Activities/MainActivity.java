package com.salikkim.store.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.salikkim.store.Adapters.ProductAdapter;
import com.salikkim.store.Helper.VolleyEventListener;
import com.salikkim.store.Helper.VolleyRequest;
import com.salikkim.store.Models.Products;
import com.salikkim.store.R;
import com.salikkim.store.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String main_url = "https://hawtie.000webhostapp.com/salikkim_store/main.php";
    private String user_url = "https://hawtie.000webhostapp.com/salikkim_store/getuserprofile.php";
    private String setProfile_url = "https://hawtie.000webhostapp.com/salikkim_store/setprofile.php";
    private String updateProfile_url = "https://hawtie.000webhostapp.com/salikkim_store/updateprofile.php";
    private ProductAdapter productAdapter;
    private List<Products> itemsList = new ArrayList<>();
    private String[] keys = new String[]{"user_id"};
    private ActivityMainBinding mainBinding;
    private String user_id = "";
    private String user_name = "";
    private String user_email = "";
    private String address_id = "";
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mainBinding.getRoot();
        setContentView(view);
        MaterialToolbar toolbar = findViewById(R.id.toolbarMainActivity);
        mainBinding.toolbarMainActivity.setTitle("Salikkim Store");
        setSupportActionBar(toolbar);
        mainBinding.navigationView.setItemIconTintList(null);
        mainBinding.navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mainBinding.drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mainBinding.drawerLayout.addDrawerListener(toggle);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            getUserProfile();
        } else {
            hide_nav_items();
        }
        toggle.syncState();
        mainBinding.recyclerViewMain.setHasFixedSize(true);
        mainBinding.recyclerViewMain.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(this, itemsList);
        mainBinding.recyclerViewMain.setAdapter(productAdapter);
        getLists();
    }

    private void show_nav_items() {
        Menu nav_menu = mainBinding.navigationView.getMenu();
        mainBinding.navigationView.getHeaderView(0).findViewById(R.id.profile_layout).setVisibility(View.VISIBLE);
        mainBinding.navigationView.getHeaderView(0).findViewById(R.id.login).setVisibility(View.GONE);
        nav_menu.findItem(R.id.become_seller).setVisible(true);
        nav_menu.findItem(R.id.my_order).setVisible(true);
        nav_menu.findItem(R.id.edit_profile).setVisible(true);
        nav_menu.findItem(R.id.logout).setVisible(true);

    }

    private void getUserProfile() {
        user_id = currentUser.getPhoneNumber().substring(3, 13);
        String[] key = keys;
        String[] value = new String[]{user_id};
        new VolleyRequest(key, value, user_url, getApplicationContext(), new VolleyEventListener<String>() {
            @Override
            public void onSuccess(String object) {
                try {
                    JSONArray jsonArray = new JSONArray(object);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        user_name = jsonObject.getString("User_name");
                        user_email = jsonObject.getString("User_email");
                        address_id = jsonObject.getString("Addr_id");
                    }
                    TextView name = mainBinding.navigationView.getHeaderView(0).findViewById(R.id.profile_name);
                    TextView phone = mainBinding.navigationView.getHeaderView(0).findViewById(R.id.profile_phone);
                    name.setText(user_name);
                    phone.setText(user_id);
                    show_nav_items();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void hide_nav_items() {
        Menu nav_menu = mainBinding.navigationView.getMenu();
        mainBinding.navigationView.getHeaderView(0).findViewById(R.id.profile_layout).setVisibility(View.GONE);
        mainBinding.navigationView.getHeaderView(0).findViewById(R.id.login).setVisibility(View.VISIBLE);
        nav_menu.findItem(R.id.become_seller).setVisible(false);
        nav_menu.findItem(R.id.my_order).setVisible(false);
        nav_menu.findItem(R.id.edit_profile).setVisible(false);
        nav_menu.findItem(R.id.logout).setVisible(false);

        mainBinding.navigationView.getHeaderView(0).findViewById(R.id.login)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, OtpSendActivity.class));
                    }
                });
    }

    private void getLists() {
        String[] key = keys;
        String[] value = new String[]{"qry"};
        new VolleyRequest(key, value, main_url, getApplicationContext(), new VolleyEventListener<String>() {
            @Override
            public void onSuccess(String object) {
                try {
                    JSONArray jsonArray = new JSONArray(object);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int product_id = jsonObject.getInt("Product_Id");
                        String title = jsonObject.getString("Name");
                        String thumbnail = jsonObject.getString("Thumbnail");
                        int seller_id = jsonObject.getInt("Seller_Id");
                        String seller_name = jsonObject.getString("Seller_name");
                        int price = jsonObject.getInt("Price");
                        int sale_price = jsonObject.getInt("Sale_price");
                        String color = jsonObject.getString("Color");
                        String product_descriptions = jsonObject.getString("Product_descriptions");
                        String available_adresses = jsonObject.getString("Available_addresses");
                        itemsList.add(new Products(title, thumbnail, seller_name, color, product_descriptions, available_adresses, product_id, seller_id, price, sale_price));
                    }
                    mainBinding.shimmerMain.setVisibility(View.GONE);
                    mainBinding.recyclerViewMain.setVisibility(View.VISIBLE);
                    mainBinding.btnFilter.setVisibility(View.VISIBLE);
                    productAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cart:
                startActivity(new Intent(MainActivity.this, CartActivity.class)
                        .putExtra("address_id", address_id)
                        .putExtra("user_id", user_id));
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.become_seller:
                Toast.makeText(this, "Become seller", Toast.LENGTH_SHORT).show();
                break;
            case R.id.edit_profile:
                if (user_name.equals("")) {
                    updateProfile(user_id, "", "", "", setProfile_url, false);
                } else {
                    updateProfile(user_id, user_name, user_email, address_id, updateProfile_url, true);
                }
                break;
            case R.id.my_order:
                startActivity(new Intent(MainActivity.this, OrderActivity.class)
                        .putExtra("user_id", user_id));
                break;
            case R.id.logout:
                mAuth.signOut();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                break;
            case R.id.share_app:
                break;
        }
        return true;
    }

    private void updateProfile(String id, String name, String email, String addr, String url, boolean b) {
        startActivity(new Intent(MainActivity.this, SetupAcActivity.class)
                .putExtra("user_id", id)
                .putExtra("user_name", name)
                .putExtra("user_email", email)
                .putExtra("address_id", addr)
                .putExtra("url", url)
                .putExtra("ss", b));
    }
}