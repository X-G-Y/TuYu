package com.example.start;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


/*  对于recyclerview重写的的适配器类*/
class MyRecyclerviewAdapter extends RecyclerView.Adapter<MyRecyclerviewAdapter.ViewHolder> implements View.OnLongClickListener {

    private List<DocumentManger> list_request;    //数据源
    private int position;
    private OnItemLongClickListener onItemLongClickListener;

    @Override
    public boolean onLongClick(View v) {
        return false;
    }


    //初始化item对象
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        android.widget.ImageButton ImageButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.item_name);
            ImageButton = itemView.findViewById(R.id.item_picture);
        }


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //将item对象传入到view中
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture,parent,false);
        view.setOnLongClickListener(this);
        return new ViewHolder(view);
    }



    //setOnItemLongClickListener 接口
    public interface OnItemLongClickListener{
        void onItemLongClick(View view,int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.onItemLongClickListener = mOnItemLongClickListener;
    }


    //获取list数量大小，在adapter的构造方法里传入需要的数据源，之后返回大小
    public MyRecyclerviewAdapter(List<DocumentManger> list_request) {
        this.list_request = list_request;
    }

    //获取List大小
    public int getItemCount() {
        return list_request == null ? 0 : list_request.size();
    }
    //获取列表项的编号
    public long getItemID(int position) { return position;}

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        DocumentManger picture = list_request.get(position);
        holder.ImageButton.setImageResource(picture.getImageID());
        holder.textView.setText(picture.getName());
        holder.ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(basic.myActivity, position + "", Toast.LENGTH_SHORT).show();
            }
        });
        holder.ImageButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getLayoutPosition();
                onItemLongClickListener.onItemLongClick(holder.itemView, position);
                return true;
            }
        });


    }



    //长按事件
    public interface RecyclerViewOnItemLongClickListener {

        boolean onItemLongClickListener(View view, int position);

    }



}