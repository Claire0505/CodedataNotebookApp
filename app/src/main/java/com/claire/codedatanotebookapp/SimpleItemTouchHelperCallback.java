package com.claire.codedatanotebookapp;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

// step.2新建類繼承自ItemTouchHelper.Callback
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private RecyclerViewAdapter mAdapter;

    //限制ImageView的長度所能增加的最大值
    private double ICON_MAX_SIZE = 50;
    //ImageView的初始长宽
    private int fixedWidth = 150;

    public SimpleItemTouchHelperCallback(RecyclerViewAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        //重置改變，防止由於復用而導致的顯示問題
        viewHolder.itemView.setScrollX(0);
        ((RecyclerViewAdapter.ViewHolder)viewHolder).tv.setText("左滑删除");
        FrameLayout.LayoutParams params =
                (FrameLayout.LayoutParams)((RecyclerViewAdapter.ViewHolder)viewHolder).image.getLayoutParams();
        params.width = 150;
        params.height = 150;
        ((RecyclerViewAdapter.ViewHolder)viewHolder).image.setLayoutParams(params);
        ((RecyclerViewAdapter.ViewHolder)viewHolder).image.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //僅對側滑狀態下的效果做出改變
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            //如果dX小於等於刪除方塊的寬度，那麼我們把該方塊滑出來
            if (Math.abs(dX) <= getSlideLimitation(viewHolder)){
                viewHolder.itemView.scrollTo(-(int) dX,0);
            }
        }
        //如果dX還未達到能刪除的距離，此時慢慢增加“圖片”的大小，增加的最大值為ICON_MAX_SIZE
        else if (Math.abs(dX) <= recyclerView.getWidth() / 2){
            double distance = (recyclerView.getWidth() / 2 -getSlideLimitation(viewHolder));
            double factor = ICON_MAX_SIZE / distance;
            double diff =  (Math.abs(dX) - getSlideLimitation(viewHolder)) * factor;
            if (diff >= ICON_MAX_SIZE)
                diff = ICON_MAX_SIZE;
            ((RecyclerViewAdapter.ViewHolder)viewHolder).tv.setText("");   //把文字去掉
            ((RecyclerViewAdapter.ViewHolder) viewHolder).image.setVisibility(View.VISIBLE);  //显示圖片
            FrameLayout.LayoutParams params =
                    (FrameLayout.LayoutParams) ((RecyclerViewAdapter.ViewHolder) viewHolder).image.getLayoutParams();
            params.width = (int) (fixedWidth + diff);
            params.height = (int) (fixedWidth + diff);
            ((RecyclerViewAdapter.ViewHolder) viewHolder).image.setLayoutParams(params);

        } else {
            //拖拽狀態下不做改變，需要調用父類的方法
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

    }

    /**
    獲取刪除方塊的寬度
     */
    public int getSlideLimitation (RecyclerView.ViewHolder viewHolder){
        ViewGroup viewGroup = (ViewGroup)viewHolder.itemView;
        return viewGroup.getChildAt(1).getLayoutParams().width;
    }

    /**
     該方法用於返回可以滑動的方向，比如說允許從右到左側滑，允許上下拖動等。
     要使RecyclerView的Item可以上下拖動，同時允許從右到左側滑，但不許允許從左到右的側滑
     */
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN; //允許上下拖動
        int swipeFlags = ItemTouchHelper.LEFT; // 只允許從右滑到左
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     當用戶拖動一個Item進行上下移動從舊的位置到新的位置的時候會調用該方法，
     在該方法內，我們可以調用Adapter的notifyItemMoved方法來交換兩個ViewHolder的位置，最後返回true，
     表示被拖動的ViewHolder已經移動到了目的位置。所以，如果要實現拖動交換位置，可以重寫該方法（前提是支持上下拖動）
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        // onItemMove 是接口interface方法
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    /**
    當用戶左右滑動Item達到刪除條件時，會調用該方法，一般手指觸摸滑動的距離達到RecyclerView寬度的一半時，
     再鬆開手指，此時該Item會繼續向原先滑動方向滑過去並且調用onSwiped方法進行刪除，否則會反向滑回原來的位置。
     */
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // onItemDismiss 是接口方法
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());

    }

    // 該方法返回true時，表示支持長按拖動，即長按ItemView後才可以拖動，我們遇到的場景一般也是這樣的。默認是返回true。
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    // 该方法返回true时，表示如果用户触摸并左右滑动了View，那么可以执行滑动删除操作，即可以调用到onSwiped()方法。默认是返回true。
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

}
