package com.example.start;

import com.android.volley.Response;

public interface ResponseListener<T> extends Response.ErrorListener, Response.Listener<T> {
}
