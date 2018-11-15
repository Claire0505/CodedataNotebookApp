package com.claire.codedatanotebookapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * 此適配器類將用於在回收視圖中，使用適當的數據來擴充佈局。
 * 添加了 removeItem() 和 restoreItem() 兩個額外的方法，以刪除/添加到recycler視圖
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Item> itemList;

    public RecyclerAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    // 一定要使用ViewHolder包裝畫面元件
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textTitle;
        TextView textDate;
        TextView tv_delete;
        ImageView image_delete;

        /**
         recycler_list_item.xml 利用FrameLayout
         所有的控件層疊顯示，默認放在屏幕的左上角，最先添加的控件放在最底層，後添加的控件在先添加的控件上面
         viewBackground 當成底部是滑動刪出時才會顯示出來
         viewForeground 一開始畫面呈現的放在上層
         */

        RelativeLayout viewBackground; //背景區塊
        CardView viewForeground; //前景

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textItem);
            textDate = itemView.findViewById(R.id.textDate);
            tv_delete = itemView.findViewById(R.id.delete_text);
            image_delete = itemView.findViewById(R.id.deleteImage);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
        }
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
                .inflate(R.layout.recycler_list_item, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        final Item item = itemList.get(position);
        viewHolder.textTitle.setText(item.getTitle());

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
        return itemList.size();
    }

    //移除数据
    public void removeData(int position){
        itemList.remove(position);
        // 該方法用於刪除一個數據的時候,position表示數據刪除的位置
        notifyItemRemoved(position);
        notifyItemChanged(position, itemList.size()-position);
    }

    public void removeItem(int position){
        /*
            通過位置通知項目
            執行回收站視圖刪除動畫
            注意：不要調用 notifyDataSetChanged()
         */
        itemList.remove(position);
        notifyItemRemoved(position);

    }

    //恢復
    public void restoreItem (Item item, int position){
        itemList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);

    }

}
