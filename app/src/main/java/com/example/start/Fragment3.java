package com.example.start;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.fragment.app.ListFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment3#newInstance} factory method to
 * create an instance of this fragment.
 */

/**
 * @描述 在Fragment中要使用ListView，就要用ListFragment
 * */


/**
 * Fragment3对应设置界面
 */

public class Fragment3 extends ListFragment {
    private String TAG = Fragment3.class.getName();


    private View view;
    private ArrayAdapter<String> adapter;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment3.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment3 newInstance(String param1, String param2) {
        Fragment3 fragment = new Fragment3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "--------onCreate");
        //通过泛型来指定要适配的数据类型，然后在构造函数中把适配的数据传入。
        //android.R.layout.simple_list_item_1是ListView内置的一个子项布局，里面只有一个TextView，可显示一段文本
        //data表示要适配的数据
        List<String> data = new ArrayList<String>();
        //账号管理中心，内应含（以后写）：账号资料，用户协议，隐私政策等
        data.add("账号中心");
        //目前只有中文，但是不妨碍我们展望未来（画饼充饥）
        data.add("语言设置");
        //同上
        data.add("OCR识别语言");
        //可以让我先当一段时间客服
        data.add("联系客服");
        //版本号与更新
        data.add("关于图遇");
        //如题
        data.add("退出登陆");


        adapter = new ArrayAdapter<String>(this.getActivity(),   android.R.layout.simple_list_item_1,
               data);
        //添加适配器
        setListAdapter(adapter);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }




    /**
     * @描述 在onCreateView中加载布局
     * */

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //打印日志
        Log.d(TAG, "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }
}
