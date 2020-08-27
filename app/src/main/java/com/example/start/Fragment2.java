package com.example.start;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    //如果用户第一次输入什么也没有输入
    private boolean editTextIsEmpty = false;
    //判断用户新建分类命名是否存在
    private boolean ifExact = false;
    //对话框
    private AlertDialog.Builder builder;
    //动态循环视图对象
    public RecyclerView recyclerView_dynamic;
    private int position = 3;  //定义新建item位置
    private int RenamePosition = 0;//定义重命名的item
    boolean ifstart = true;
    private View view;
    private ImageButton imageButton;
    private FloatingActionButton addManger;
    private DocumentManger doc = new DocumentManger("新建分类", R.drawable.documentpng);
    //线性适配器对象
    private MyRecyclerviewAdapter myRecyclerviewAdapter;
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

        // 新建分类按钮创建监听事件
        addManger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(basic.myActivity, "新建分类", Toast.LENGTH_SHORT).show();
                showCreateNewDocumentDialog();
            }
        });

        //创建一个网格视图管理器
        GridLayoutManager manager = new GridLayoutManager(
                getActivity(), 3
        );
        //设置管理器
        recyclerView_dynamic.setLayoutManager(manager);

        myRecyclerviewAdapter = new MyRecyclerviewAdapter(listAll);
        recyclerView_dynamic.setAdapter(myRecyclerviewAdapter);
        //设置默认动画效果
        recyclerView_dynamic.setItemAnimator(new DefaultItemAnimator());

        //设置RecyclerView的每一项的长按事件
        myRecyclerviewAdapter.setOnItemLongClickListener(new MyRecyclerviewAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                showPopupMenu(recyclerView_dynamic.getChildAt(position),position);
            }
        });

        //设置RecyclerView的每一项的点击事件
        myRecyclerviewAdapter.setOnItemClickListener(new MyRecyclerviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //点击进入该Item对应的页面
                Intent intent = new Intent(getActivity(), DocumentActivity.class);
                intent.putExtra("name", listAll.get(position).getName());
                startActivity(intent);
            }

        });

        //若没有在本地查询到缓存数据，则初始化
        //初始化recyclerview
        if(!IfGetdata()) init();
        else Getdata(listAll);



        return view;
    }







    //新建一个分类
    private void newManger(){
        listAll.add(doc);
        myRecyclerviewAdapter.notifyItemInserted(myRecyclerviewAdapter.getItemCount());
        recyclerView_dynamic.scrollToPosition(myRecyclerviewAdapter.getItemCount());
        //实时保存数据
        Save(listAll);
    }






    private void init(){
        //初始创建熊猫头，小鹦鹉，滑稽脸，可以动态刷新
        DocumentManger Panda = new DocumentManger("熊猫头", R.drawable.documentpng);
        listAll.add(Panda);
        DocumentManger parrot = new DocumentManger("小鹦鹉", R.drawable.documentpng);
        listAll.add(parrot);
        DocumentManger comical = new DocumentManger("滑稽", R.drawable.documentpng);
        listAll.add(comical);
        Save(listAll);


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






    //点击添加分类之后出现的对话框，左按钮是确定，右按钮是取消，
    private void showCreateNewDocumentDialog() {
        final EditText editText = new EditText(getActivity());
        //设置对空格的过滤器
        editText.setFilters(new InputFilter[]{new SpaceFilter()});
        builder = new AlertDialog.Builder(getActivity()).setTitle("输入分类名称").setView(editText)
                .setNegativeButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //先判断命名是否已经存在
                        //小彩蛋
                        if(editText.getText().length() == 0 && !editTextIsEmpty){
                            doc = new DocumentManger("我叫没名字", R.drawable.documentpng);
                            newManger();
                            editTextIsEmpty = true;
                        }
                        else if (editText.getText().length() == 0 && editTextIsEmpty){
                            //期望是dialog的动态刷新，目前以Toast的方式提示用户
                            Toast.makeText(basic.myActivity, "呐，才不要什么都不输入呢！", Toast.LENGTH_SHORT).show();
                            Toast.makeText(basic.myActivity, "就算是输入空格也没有用的呢",Toast.LENGTH_SHORT).show();
                        }
                        else if (IsExact(editText)){
                            Toast.makeText(basic.myActivity, "分类已存在",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            doc = new DocumentManger(editText.getText().toString(), R.drawable.documentpng);
                            newManger();
                        }
                    }
                });
        builder
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(basic.myActivity, "已取消",Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();

    }






    /**
     * 禁止输入空格
     *
     * @return
     */
    public class  SpaceFilter implements InputFilter {
        @Override
        public String filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            //返回null表示接收输入的字符,返回空字符串表示不接受输入的字符
            if (source.equals(" "))
                return "";
            return null;
        }
    }







    //判断用户输入的新分类名是否已经存在
    public boolean IsExact(EditText editText){
        ifExact = false;
        for(int i = 0;i < listAll.size();i++){
            if(listAll.get(i).getName().equals(editText.getText().toString())) {
                ifExact = true;
                break;
            }
        }
        return ifExact;
    }







    private void showPopupMenu(final View view, final int position) {
        // 这里的view代表popupMenu需要依附的view
        final PopupMenu popupMenu = new PopupMenu(basic.myActivity, view);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.pop, popupMenu.getMenu());
        popupMenu.show();
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 控件每一个item的点击事件
                switch (item.getItemId()){
                    //menu1是重命名
                    case R.id.menu1:
                        showRenameDialog(position);
                        break;
                        //menu2是移动

                        //menu3是删除
                    case R.id.menu3:
                        //删除
                        myRecyclerviewAdapter.notifyItemRemoved(position);
                        listAll.remove(position);
                        //刷新
                        myRecyclerviewAdapter.notifyItemChanged(position);
                        Save(listAll);
                        break;
                }
                return true;
            }
        });


        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                // 控件消失时的事件
            }
        });
    }








    //设置重命名时候的dialog
    public String showRenameDialog(final int position){
        final EditText editText = new EditText(getActivity());
        //设置对空格的过滤器
        editText.setFilters(new InputFilter[]{new SpaceFilter()});
        builder = new AlertDialog.Builder(getActivity()).setTitle("重命名").setView(editText)
                .setNegativeButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editTextIsEmpty = true;
                        //先判断命名是否已经存在
                        if (editText.getText().length() == 0 && editTextIsEmpty){
                            //期望是dialog的动态刷新，目前以Toast的方式提示用户
                            Toast.makeText(basic.myActivity, "呐，才不要什么都不输入呢！", Toast.LENGTH_SHORT).show();
                            Toast.makeText(basic.myActivity, "就算是输入空格也没有用的呢",Toast.LENGTH_SHORT).show();
                        }
                        else if (IsExact(editText)){
                            Toast.makeText(basic.myActivity, "分类已存在",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            listAll.get(position).Rename(editText.getText().toString());
                            myRecyclerviewAdapter.notifyItemChanged(position);
                            //实时保存数据
                            Save(listAll);
                            Toast.makeText(basic.myActivity, "Replace", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        builder
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(basic.myActivity, "已取消",Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
        return editText.getText().toString();
    }






    //储存数据
    //SharedPreferences
    //目前只是储存到本地，用户清理缓存后会被清除，考虑以后用户数据放到服务器上
    //todo
    public void Save(List<DocumentManger> list){
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE).edit();
        editor.putInt("Total", list.size());
        //判断用户是否为第一次使用，若是，则IFGETDATA为false
        //已经在本地缓存则为true
        editor.putBoolean("IFGETDATA", true);
        for(int i = 0;i < list.size();i++){
            //分别储存document中的名字与图片编号
            //读取时分别读取即可
            editor.putString("name"+i, list.get(i).getName());
            editor.putInt("ImageId"+i,list.get(i).getImageID());
            editor.apply();
            editor.commit();
        }
    }

    //读取数据，分条读取
    public void Getdata(List<DocumentManger> list){
        SharedPreferences pre = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        int Total = pre.getInt("Total", 0);
        int ImageID;
        String name;
        DocumentManger documentManger;
        for (int i = 0;i < Total;i++){
            ImageID = pre.getInt("ImageId"+i, 0);
            name = pre.getString("name"+i, "就这？");
            documentManger = new DocumentManger(name, ImageID);
            list.add(documentManger);
        }
    }

    //判断用户是否为第一次使用程序（或者用户没有对主界面有任何更改
    public boolean IfGetdata(){
        SharedPreferences pre = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        //默认为没有获取数据，则值为false
        return pre.getBoolean("IFGETDATA", false);
    }

}