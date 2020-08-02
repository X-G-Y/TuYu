package com.example.start;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


/**
 * Fragment2对应本地管理界面
 */
public class Fragment2 extends Fragment implements MyRecyclerviewAdapter.RecyclerViewOnItemLongClickListener {

    @NonNull
    @Override
    public LiveData<LifecycleOwner> getViewLifecycleOwnerLiveData() {
        return super.getViewLifecycleOwnerLiveData();
    }
    //动态循环视图对象
    private RecyclerView recyclerView_dynamic;
    private int position = 3;  //定义新建item位置
    boolean ifstart = true;
    private View view;
    private ImageButton imageButton;
    private FloatingActionButton addManger;
    private DocumentManger doc = new DocumentManger("新建分类", R.drawable.documentpng);
    //线性适配器对象
    private MyRecyclerviewAdapter myRecyclerviewAdapter;
    //当前队列
    private List<DocumentManger> listNow = new ArrayList<>();
    //所有队列
    private List<DocumentManger> listAll = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";




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

        view = inflater.inflate(R.layout.fragment_2, container, false);
        //搜索按钮
        imageButton = (ImageButton) view.findViewById(R.id.Search);

        //新建分类按钮
        addManger = (FloatingActionButton)view.findViewById(R.id.fab);

        //获取recyclerview
        recyclerView_dynamic = view.findViewById(R.id.recyclerview);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.e(getTag(), "onClick: ");
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);

            }
        });

        addManger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(basic.myActivity, "新建分类", 1000).show();
                listAll.add(doc);
                myRecyclerviewAdapter.notifyItemInserted(myRecyclerviewAdapter.getItemCount());
                recyclerView_dynamic.scrollToPosition(myRecyclerviewAdapter.getItemCount());
            }
        });

        //初始化recyclerview
        init();
        //设置RecyclerView的每一项的长按事件
        myRecyclerviewAdapter.setOnItemLongClickListener(new MyRecyclerviewAdapter.RecyclerViewOnItemLongClickListener() {
            public boolean onItemLongClickListener(View view, int position) {
                Snackbar.make(view, "长按事件：" + listAll.get(position), Snackbar.LENGTH_SHORT).show();
                Toast.makeText(basic.myActivity, position + "xgy", 300).show();
                return true;
            }
        });

        return view;
    }

    private void init(){
        //创建一个网格视图管理器
        GridLayoutManager manager = new GridLayoutManager(
                getActivity(), 3
        );
        //设置管理器
        recyclerView_dynamic.setLayoutManager(manager);
        //初始创建熊猫头，小鹦鹉，滑稽脸，可以动态刷新
        DocumentManger Panda = new DocumentManger("熊猫头", R.drawable.documentpng);
        listAll.add(Panda);
        DocumentManger parrot = new DocumentManger("小鹦鹉", R.drawable.documentpng);
        listAll.add(parrot);
        DocumentManger comical = new DocumentManger("滑稽", R.drawable.documentpng);
        listAll.add(comical);
        myRecyclerviewAdapter = new MyRecyclerviewAdapter(listAll);
        recyclerView_dynamic.setAdapter(myRecyclerviewAdapter);
        //设置默认动画效果
        recyclerView_dynamic.setItemAnimator(new DefaultItemAnimator());

    }


    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }


    @Override
    public boolean onItemLongClickListener(View view, int position) {
        return false;
    }
}
