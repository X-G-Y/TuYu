package com.example.ihitsz.Adapter;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ihitsz.Myclass.FormImg;
import com.example.ihitsz.R;

import java.util.List;


/*  对于recyclerview重写的的适配器类*/
public class RecyclerViewAdapterForPicture extends RecyclerView.Adapter<RecyclerViewAdapterForPicture.ViewHolder> implements View.OnLongClickListener {

    private List<FormImg> list_request;    //数据源
    private int position;
    private OnItemLongClickListener onItemLongClickListener;
    private OnItemClickListener onItemClickListener;
    private GridLayoutManager gridLayoutManager;

    @Override
    public boolean onLongClick(View v) {
        return false;
    }


    //初始化item对象
    static class ViewHolder extends RecyclerView.ViewHolder {

        android.widget.ImageButton ImageButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ImageButton = itemView.findViewById(R.id.display_picture);
        }


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //将item对象传入到view中
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picturefordisplay,parent,false);
        view.setOnLongClickListener(this);
        return new ViewHolder(view);
    }



    //setOnItemClickListener 接口
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }



    //setOnItemLongClickListener 接口
    public interface OnItemLongClickListener{
        void onItemLongClick(View view,int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.onItemLongClickListener = mOnItemLongClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.onItemClickListener = mOnItemClickListener;
    }


    //获取list数量大小，在adapter的构造方法里传入需要的数据源，之后返回大小
    public RecyclerViewAdapterForPicture(List<FormImg> list_request) {
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
        FormImg picture = list_request.get(position);
        ViewGroup.LayoutParams parm = holder.itemView.getLayoutParams();
        parm.height =
                gridLayoutManager.getWidth()/gridLayoutManager.getSpanCount()
                        - 2*holder.itemView.getPaddingLeft() - 2*((ViewGroup.MarginLayoutParams)parm).leftMargin;
        Drawable drawable = new BitmapDrawable(picture.getmBitmap());
        holder.ImageButton.setImageDrawable(drawable);
        //holder.ImageButton.setImageResource(R.drawable.god);
        holder.ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();
                onItemClickListener.onItemClick(holder.itemView, position);
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

    public void GetManger(GridLayoutManager gridLayoutManager){
        this.gridLayoutManager = gridLayoutManager;
    }



}