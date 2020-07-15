package com.example.start;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

//这是主界面
//BlandFragment对应社区界面
//Fragment2对应本地管理界面
//Fragment3对应设置界面

import static com.example.start.R.layout.activity_basic;

public class basic extends AppCompatActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_basic);


        //打印日志
        final int v = Log.v("basic", "cao");
        //初始化界面应该是用户本地表情包管理界面，此处直接使用Replace()方法将fragment2填充到容器内
        ImageButton imageButtonl = findViewById(R.id.User);
        imageButtonl.setOnClickListener(this);
        replaceFragment(new Fragment2());

        ImageButton imageButton2 = findViewById(R.id.Settings);
        imageButton2.setOnClickListener(this);

        ImageButton imageButton3 = findViewById(R.id.Social);
        imageButton3.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.User:
                replaceFragment(new Fragment2());
                break;
            case R.id.Settings:
                replaceFragment(new Fragment3());
                break;
            case R.id.Social:
                replaceFragment(new BlankFragment());
                break;
            default:
                break;
        }
    }
    //创建待添加的碎片实例
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();//通过getSupportManger方法得到FragmentManger,
        FragmentTransaction transaction = fragmentManager.beginTransaction();//使用beginTransaction方法开启事务
        transaction.replace(R.id.frame,fragment);//用replace()方法替换碎片,传入容器的id与待添加的碎片实例
        transaction.commit();//commit方法呈现
    }
}