package com.claire.codedatanotebookapp;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

/**
    當視圖中有UI更改時，ItemTouchHelper將使用 getDefaultUIUtil() 來檢測。
    使用此函數將 背景視圖保持在靜態位置 並移動 前景視圖。
    在onChildDrawOver()中，當用戶滑動視圖時，前景視圖的 X 位置會發生變化。
    RecyclerItemTouchHelperListener 接口，用於將回調發送到實現活動。
    將在MainActivity中觸發偵聽。
 */

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private RecyclerItemTouchHelperListener listener;

    /**
     create interface RecyclerItemTouchHelperListener
     */
    public interface RecyclerItemTouchHelperListener {
        void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return true;
    }

    /**
    從靜止狀態變為拖拽或則滑動的時候會回調該方法，參數actionState表示當前的狀態。
     */
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null){
            final View foregroundView = ((RecyclerAdapter.ViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

//    @Override
//    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
//                            @NonNull RecyclerView.ViewHolder viewHolder,
//                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        //前景
//       final View foregroundView = ((RecyclerAdapter.ViewHolder) viewHolder).viewForeground;
//       getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
//               actionState, isCurrentlyActive);
//    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((RecyclerAdapter.ViewHolder) viewHolder).viewForeground;
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //僅對側滑狀態下的效果做出改變
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            //前景
            final View foregroundView = ((RecyclerAdapter.ViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }

        //拖拽狀態下不做改變，需要調用父類的方法
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwipe(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    // 该方法返回true时，表示如果用户触摸并左右滑动了View，那么可以执行滑动删除操作，即可以调用到onSwiped()方法。默认是返回true。
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

}
