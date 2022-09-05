package com.salikkim.store.Helper;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class StringRequestFromUrl extends StringRequest {

    public StringRequestFromUrl(int method, String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {

        super(method, url, listener, errorListener);
    }
}
