package com.claire.codedatanotebookapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

// step.2新建類繼承自ItemTouchHelper.Callback
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ItemTouchHelperAdapter mAdapter;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter mAdapter) {
        this.mAdapter = mAdapter;
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
