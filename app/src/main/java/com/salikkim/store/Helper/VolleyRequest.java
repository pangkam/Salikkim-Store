package com.salikkim.store.Helper;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class VolleyRequest {
    private String[] key;
    private String[] value;
    private int index_no;
    private String url;
    private Context context;
    private VolleyEventListener<String> mCallBack;

    public VolleyRequest(String[] key, String[] value,String url, Context context, VolleyEventListener<String> mCallBack) {
        this.key = key;
        this.value = value;
        this.index_no = index_no;
        this.url = url;
        this.context = context;
        this.mCallBack = mCallBack;
        setPostRequest();
    }


    private void setPostRequest() {
        RequestQueue queue = Volley.newRequestQueue(this.context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        mCallBack.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            if (error instanceof TimeoutError) {
                                SnackBar("Timeout");
                            } else if (error instanceof NoConnectionError) {
                                SnackBar("Connection Lost!");
                            } else if (error instanceof AuthFailureError) {
                                SnackBar("Authentication Failed");
                            } else if (error instanceof ServerError) {
                                SnackBar("Server Error");
                            } else if (error instanceof NetworkError) {
                                SnackBar("Network Connection failed");
                            } else if (error instanceof ParseError) {
                                SnackBar("Parsing Error");
                            }

                        } catch (Exception e) {
                            Log.d(TAG, "onErrorResponse: " + e.getMessage());
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                for (int i = 0; i < key.length; i++)
                    params.put(key[i], value[i]);
                return params;
            }
        };
        postRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 3000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 5;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                SnackBar(error.getMessage());
            }
        });
        queue.add(postRequest);
    }

    private void SnackBar(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}
