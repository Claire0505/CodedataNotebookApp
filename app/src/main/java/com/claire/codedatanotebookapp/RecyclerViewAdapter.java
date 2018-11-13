package com.claire.codedatanotebookapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
        implements ItemTouchHelperAdapter{
    private List<String> mData;

    public RecyclerViewAdapter(List<String> mData) {
        this.mData = mData;
    }

    // ⑴ RecyclerView沒有點擊事件，所以自定兩個Interface接口來摸擬ListView的OnItemClickListener
    // ⑵ 新建兩個私有變量用於保存用戶設置的監聽器及其set方法：
    // ⑶ 在onBindViewHolder方法内，实现回调
    private OnItemClickListener mOnItemClickListener; //Interface
    private OnItemLongClickListener mOnItemLongClickListener; //Interface

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        viewHolder.textView.setText(mData.get(position));

        /*
        這裡實際上用到了 子Item View的onClickListener和onLongClickListener這兩個監聽器，
        如果當前子item view被點擊了，會觸發點擊事件進行回調，
        然後在步驟 ⑴ 處獲取當前點擊位置的position值，
        接著在⑵號代碼處進行再次回調，而這一次的回調是我們自己手動添加的，需要實現上面所述的接口。

        接著在MainActivity.java中設置監聽器，採用匿名內部類的形式實現了onItemClickListener、onItemLongClickListener接口
         */

        // ⑶ 在onBindViewHolder方法内，实现回调
        //判断是否设置了监听器
        if(mOnItemClickListener != null){
            //為ItemView设置监听器
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getLayoutPosition(); // ⑴
                    mOnItemClickListener.onItemClick(viewHolder.itemView,position); // ⑵
                }
            });
        }


        if (mOnItemLongClickListener != null){
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = viewHolder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(viewHolder.itemView, position);
                    //返回true 表示消耗了事件 事件不會繼續傳遞
                    return true;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // 一定要使用ViewHolder包裝畫面元件
    public class ViewHolder extends RecyclerView.ViewHolder{
         TextView textView;
         TextView tv;
         ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textItem);
            tv = itemView.findViewById(R.id.tv_text);
            image = itemView.findViewById(R.id.iv_img);
        }
    }

    //移除数据
    public void removeData(int position){
        mData.remove(position);
        // 該方法用於刪除一個數據的時候,position表示數據刪除的位置
        notifyItemRemoved(position);
        notifyItemChanged(position, mData.size()-position);
    }
    //新增數據
    public void addData(int position){
        mData.add(position, "Add One");
        // 該方法用於當增加一個數據的時候,position表示新增數據顯示的位置
        notifyItemInserted(position);
    }
    //更改某個位置的數據
    public void changeData(int position){
        mData.set(position, "Item has changed");
        notifyItemChanged(position);
    }

    // 實現ItemTouchHelperAdapter接口
    @Override
    public void onItemMove(int fromPosition, int position) {
        // 交換位置
        Collections.swap(mData, fromPosition, position);
        notifyItemMoved(fromPosition, position);
    }

    @Override
    public void onItemDismiss(int position) {
        // 刪除數據
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position, mData.size()-position);
    }


}
