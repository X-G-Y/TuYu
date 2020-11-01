package com.example.ihitsz.Myclass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;


public class ImageManager{
    public boolean PushImageRequest(final Bitmap bt, final String fileName, final Context context) {
        final String PushImage = "1";
        final boolean[] flag = {false};

        ByteArrayOutputStream boss = new ByteArrayOutputStream();
        bt.compress(Bitmap.CompressFormat.JPEG,100,boss);
        final String image = boss.toString();
        Log.d("image", image);

        //请求地址
        String url = "http://33h79225f6.zicp.vip/MyFirstWebApp/ImageServlet";    //注①
        String tag = "PushImage";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("ShowToast")
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response == null || response.equals("")) Log.d("response", "onResponse: response is null");
                            assert response != null;
                            Log.d("jcy", response);
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");  //注③
                            String result = jsonObject.getString("Result");  //注④
                            if(result.equals("success")) flag[0] = true;
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Bitmap", image);  //注⑥
                params.put("FileName", fileName);
                params.put("Type", PushImage);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
        return flag[0];
    }

    public String PullImageRequest(final String fileName, final Context context) {
        final String PullImage = "2";
        final String[] image = new String[1];

        //请求地址
        String url = "http://33h79225f6.zicp.vip/MyFirstWebApp/ImageServlet";    //注①
        String tag = "PullImage";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("ShowToast")
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response == null || response.equals("")) Log.d("response", "onResponse: response is null");
                            assert response != null;
                            Log.d("jcy", response);
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");  //注③
                            String result = jsonObject.getString("Result");  //注④
                            image[0] = result;
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("FileName", fileName);
                params.put("Type", PullImage);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
        return image[0];
    }
}
