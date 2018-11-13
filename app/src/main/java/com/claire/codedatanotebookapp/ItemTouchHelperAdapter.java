package com.claire.codedatanotebookapp;

// step.1新建一個接口，讓Adapter實現之
public interface ItemTouchHelperAdapter {
    // 數據交換
    void onItemMove(int fromPosition, int position);
    // 數據刪除
    void onItemDismiss(int position);
}
