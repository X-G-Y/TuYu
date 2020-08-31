package com.example.start;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
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
                Request.Method.POST, url,
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