package com.example.start;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * Fragment2对应本地管理界面
 */
public class Fragment2 extends Fragment {
    boolean ifstart = true;
    private List<DocumentManger> list = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    GridLayoutManager layoutManager;
    MyRecyclerviewAdapter myRecyclerviewAdapter;
    RecyclerView recyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);




        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    //引用Fragment布局中的控件
    //在onCreate方法中无法使用findViewById方法，考虑在onStart中重写
    @Override
    public void onStart(){
        super.onStart();
        //getActivity().setContentView(R.layout.picture);
        //判断是否已经进行过一次初始化
        if(ifstart){
            //添加recyclerview控件
            init();
            RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerview);
            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),3);
            MyRecyclerviewAdapter myRecyclerviewAdapter = new MyRecyclerviewAdapter(list);
            //设置布局管理器
            recyclerView.setLayoutManager(layoutManager);
            //设置适配器
            recyclerView.setAdapter(myRecyclerviewAdapter);
        }


        ifstart = false;

        //添加分类按钮
        ImageButton addmanger = getActivity().findViewById((R.id.AddManger));
        addmanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentManger newone = new DocumentManger("新建分类", R.drawable.documentpng);
                list.add(newone);
            }
        });
        //搜索按钮
        ImageButton imageButton = getActivity().findViewById(R.id.Search);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });



    }

    private void init(){
        //熊猫头，小鹦鹉，滑稽脸
        DocumentManger Panda = new DocumentManger("熊猫头", R.drawable.documentpng);
        list.add(Panda);
        DocumentManger parrot = new DocumentManger("小鹦鹉", R.drawable.documentpng);
        list.add(parrot);
        DocumentManger comical = new DocumentManger("滑稽", R.drawable.documentpng);
        list.add(comical);
    }





    //在activity进程没结束之前的onAttach方法中将activity赋予成员变量
    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_2, container, false);
    }







}
