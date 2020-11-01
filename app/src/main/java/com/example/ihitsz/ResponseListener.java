package com.example.ihitsz;

import com.android.volley.Response;

public interface ResponseListener<T> extends Response.ErrorListener, Response.Listener<T> {
}
