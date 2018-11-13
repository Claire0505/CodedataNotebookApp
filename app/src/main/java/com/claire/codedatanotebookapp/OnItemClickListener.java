package com.claire.codedatanotebookapp;

import android.view.View;

//RecyclerView沒有點擊事件，所以自定一個接口來摸擬ListView的OnItemClickListener
public interface OnItemClickListener {
    void onItemClick(View view, int position);
}
