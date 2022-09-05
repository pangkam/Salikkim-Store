package com.salikkim.store.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.salikkim.store.Adapters.AddrAdapter;
import com.salikkim.store.Helper.VolleyEventListener;
import com.salikkim.store.Helper.VolleyRequest;
import com.salikkim.store.Models.Addresses;
import com.salikkim.store.databinding.ActivitySetupAcBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SetupAcActivity extends AppCompatActivity {
    private String address_url = "https://hawtie.000webhostapp.com/salikkim_store/getaddress.php";
    private ActivitySetupAcBinding setupAcBinding;
    private String selected_address_id = "";
    private String[] keys = new String[]{"user_id","full_name", "email", "address"};
    private ArrayList<Addresses> address_lists;
    private AddrAdapter addrAdapter;
    private String text_select_address = "Select Address";
    private String user_id;
    private String user_name;
    private String user_email;
    private String addr_id;
    private String set_profile_url;
    private boolean ss;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupAcBinding = ActivitySetupAcBinding.inflate(getLayoutInflater());
        View view = setupAcBinding.getRoot();
        setContentView(view);
        if (savedInstanceState == null) {
            user_id = getIntent().getExtras().getString("user_id");
            user_name = getIntent().getExtras().getString("user_name");
            user_email = getIntent().getExtras().getString("user_email");
            addr_id = getIntent().getExtras().getString("address_id");
            set_profile_url = getIntent().getExtras().getString("url");
            ss = getIntent().getExtras().getBoolean("ss");
            setupAcBinding.etFullName.setText(user_name);
            setupAcBinding.etEmail.setText(user_email);
            getAddress(addr_id, ss);
        }

        setupAcBinding.btnProfileSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (setupAcBinding.etFullName.getText().toString().isEmpty()) {
                    Toast.makeText(SetupAcActivity.this, "Cannot leave blank", Toast.LENGTH_SHORT).show();
                } else if (selected_address_id == "") {
                    Toast.makeText(SetupAcActivity.this, "Please select address", Toast.LENGTH_SHORT).show();
                } /*else if(encodeImageString==""){
                    Toast.makeText(SetupAcActivity.this, "Please add profile picture", Toast.LENGTH_SHORT).show();
                }*/ else {
                    saveProfile(setupAcBinding.etFullName.getText().toString(), setupAcBinding.etEmail.getText().toString(), selected_address_id);
                }

            }
        });
    }

    private void getAddress(String id, boolean s) {
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
                    addrAdapter = new AddrAdapter(SetupAcActivity.this, address_lists);
                    setupAcBinding.addrSpinner.setAdapter(addrAdapter);
                    setupAcBinding.addrSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Addresses clickedItem = (Addresses) adapterView.getItemAtPosition(i);
                            selected_address_id = clickedItem.getAddr_id();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    if (s) {
                        for (int i = 0; i < address_lists.size(); i++) {
                            if (id.equals(address_lists.get(i).getAddr_id())) {
                                setupAcBinding.addrSpinner.setSelection(i);
                            }
                        }
                    }
                    setupAcBinding.setupShimmer.setVisibility(View.GONE);
                    setupAcBinding.setupSroll.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void saveProfile(String fullname, String email, String add) {
        ProgressDialog progressDialog = new ProgressDialog(SetupAcActivity.this);
        progressDialog.setMessage("Updating profile");
        progressDialog.show();
            String[] key = keys;
            String[] value = new String[]{user_id,fullname, email, add};
            new VolleyRequest(key, value, set_profile_url, getApplicationContext(), new VolleyEventListener<String>() {
                @Override
                public void onSuccess(String object) {
                    progressDialog.dismiss();
                    Toast.makeText(SetupAcActivity.this, object, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SetupAcActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }
}