package com.example.start;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbarr = findViewById(R.id.toolbarr);
        setSupportActionBar(toolbarr);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        //显示返回键
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //不显示标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 获取一个带有search的menu
        getMenuInflater().inflate(R.menu.menue, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // 设置SearchView
        MenuItem menuItem = menu.findItem(R.id.action_search);
            // 获取到SearchView（必须在xml item中设置app:actionViewClass="android.widget.SearchView"）
            SearchView searchView = (SearchView)menuItem.getActionView();
            searchView.setQueryHint("请输入文字");
            // 在右侧添加提交按钮
            searchView.setSubmitButtonEnabled(true);
            searchView.setIconifiedByDefault(false);
            // 根据源码获取子View的id，然后获取子View来设置其属性
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            /*case R.id.home:
                finish();
                return true;

             */


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}

