package com.example.ihitsz.Activity;

import android.media.Image;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.ihitsz.Adapter.Fragment_adapter;
import com.example.ihitsz.Myclass.FormImg;
import com.example.ihitsz.R;

import java.util.ArrayList;
import java.util.List;

public class ShowBigPicture extends AppCompatActivity {

    private ViewPager viewPager;
    private List<Image> list;

    private List<FormImg> listall = new ArrayList<>();
    private List<FormImg> ShowList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_big_picture);

        viewPager = findViewById(R.id.img_viewpager);

        Fragment_adapter fragment_adapter = new Fragment_adapter();
        viewPager.setAdapter(fragment_adapter);


    }
}