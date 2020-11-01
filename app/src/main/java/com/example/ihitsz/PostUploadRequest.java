package com.example.ihitsz;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ihitsz.Myclass.FormImg;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostUploadRequest extends Request<String> {

    // 用于回调，回复监听器
    private ResponseListener mListener ;
    // 传入图片
    private List<FormImg> mListItem ;

    private String BOUNDARY = "--------------520-13-14"; //数据分隔线
    private String MULTIPART_FORM_DATA = "multipart/form-data";

    public PostUploadRequest(String url, List<FormImg> listItem, ResponseListener listener) {
        super(Method.POST, url, listener);
        this.mListener = listener ;
        setShouldCache(false);
        mListItem = listItem ;
        //设置请求的响应事件，因为文件上传需要较长的时间，所以在这里加大了，设为5秒
        setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    // 解析 Response
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String mString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Log.v("zgy", "====mString===" + mString);

            return Response.success(mString,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    /**
     * 回调正确的数据
     * @param response The parsed response returned by
     */
    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public byte[] getBody() throws AuthFailureError {
        if (mListItem == null||mListItem.size() == 0){
            return super.getBody() ;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        int N = mListItem.size() ;
        FormImg formImage ;
        for (int i = 0; i < N ;i++){
            formImage = mListItem.get(i) ;
            StringBuilder sb= new StringBuilder() ;
            /*第一行*/
            //`"--" + BOUNDARY + "\r\n"`
            sb.append("--").append(BOUNDARY);
            sb.append("\r\n") ;
            /*第二行*/
            //Content-Disposition: form-data; name="参数的名称"; filename="上传的文件名" + "\r\n"
            sb.append("Content-Disposition: form-data;");
            sb.append(" name=\"");
            sb.append(formImage.getName()) ;
            sb.append("\"") ;
            sb.append("; filename=\"") ;
            sb.append(formImage.getFileName()) ;
            sb.append("\"");
            sb.append("\r\n") ;
            /*第三行*/
            //Content-Type: 文件的 mime 类型 + "\r\n"
            sb.append("Content-Type: ");
            sb.append(formImage.getMime()) ;
            sb.append("\r\n") ;
            /*第四行*/
            //"\r\n"
            sb.append("\r\n") ;
            try {
                bos.write(sb.toString().getBytes(StandardCharsets.UTF_8));
                /*第五行*/
                //文件的二进制数据 + "\r\n"
                bos.write(formImage.getValue());
                bos.write("\r\n".getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        /*结尾行*/
        //`"--" + BOUNDARY + "--" + "\r\n"`
        String endLine = "--" + BOUNDARY + "--" + "\r\n" ;
        try {
            bos.write(endLine.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v("zgy","=====formImage====\n"+bos.toString()) ;
        return bos.toByteArray();
    }
    //Content-Type: multipart/form-data; boundary=----------8888888888888
    @Override
    public String getBodyContentType() {
        return MULTIPART_FORM_DATA+"; boundary="+BOUNDARY;
    }

    public static class RegisterActivity extends AppCompatActivity {
        static final int LOGIN = 1;
        static final int REGISTER = 2;

        EditText userNameEdit;
        EditText passwordEdit1;
        EditText passwordEdit2;
        Button signUpButton;
        ImageButton backButton;

        String url = "http://33h79225f6.zicp.vip/MyFirstWebApp/LoginServlet";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);

            userNameEdit = findViewById(R.id.account_register);
            passwordEdit1 = findViewById(R.id.password1_register);
            passwordEdit2 = findViewById(R.id.password2_register);
            signUpButton = findViewById(R.id.register_button);
            backButton = findViewById(R.id.back_button);

            signUpButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    String userName = userNameEdit.getText().toString();
                    String password1 = passwordEdit1.getText().toString();
                    String password2 = passwordEdit2.getText().toString();

                    if(password1.equals(password2)){
                        registerRequest(userName, password1);
                    }else{
                        Toast.makeText(getApplicationContext(),"两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                        passwordEdit1.setText("");
                        passwordEdit2.setText("");
                    }
                }
            });

            backButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        protected void registerRequest(final String userName, final String password){
            String tag = "Check";

            // 取得请求队列
            final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

            // 防止重复请求，所以首先取消 tag 表示的请求队列
            requestQueue.cancelAll(tag);

            // 创建 StringRequest，定义字符串的请求方式为 POST
            final StringRequest request = new StringRequest(
                    Method.POST, url,
                    new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                                String result = jsonObject.getString("Result");
                                if(result.equals("bad-database")){
                                    Toast.makeText(getApplicationContext(),"数据库出错",Toast.LENGTH_SHORT).show();
                                }else if(result.equals("userName-has-existed")){
                                    Toast.makeText(getApplicationContext(),"该用户名已经存在",Toast.LENGTH_SHORT).show();
                                    userNameEdit.setText("");
                                }else{
                                    Toast.makeText(getApplication(), "注册成功，请重新登录", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                }
                            }catch (JSONException e){
                                Toast.makeText(getApplicationContext(),"网络异常",Toast.LENGTH_SHORT).show();
                                Log.e("TAG", e.getMessage(), e);
                            }
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),"请稍后再试", Toast.LENGTH_SHORT).show();
                            Log.e("TAG", error.getMessage(),error);
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("AccountNumber", userName);
                    params.put("Password", password);
                    params.put("LoginType", String.valueOf(REGISTER));
                    return params;
                }
            };

            request.setTag(tag);
            requestQueue.add(request);
        }

    }
}
