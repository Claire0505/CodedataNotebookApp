package com.claire.codedatanotebookapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.claire.codedatanotebookapp.old_recycleradapter.RecyclerViewAdapter;
import com.claire.codedatanotebookapp.old_recycleradapter.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * 創建RecyclerItemTouchHelper的實例並將其分配給RecyclerView。這裡只定義LEFT方向。
 * 執行滑動時將調用onSwiped（）方法。這裡是刪除行項的重要步驟。
 * 調用mAdapter.removeItem（）以從RecyclerView中刪除該行。
 * 刪除行後，Snackbar用於顯示帶有UNDO選項的消息。在單擊UNDO時，使用mAdapter.restoreItem（）方法恢復該行。
 *  deletedItem，deletedIndex變量用於臨時存儲已刪除的項目和索引，直到顯示Snackbar。
 */

public class MainActivity extends AppCompatActivity
        implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private RecyclerViewAdapter adapter;
    private ArrayList<String> myDataSet;

    private RecyclerAdapter mAdapter;
    private List<Item> itemList;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initData();
        //initRecyclerView();

        testData();
        initRecyclerView2();
    }

    private void initData() {
        //建立測式資料
        myDataSet = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            myDataSet.add("Android Tutorial " + i);
        }
    }

    private void testData(){
        itemList = new ArrayList<>();
        itemList.add(new Item("Android Tutorial 01" ));
        itemList.add(new Item("Android Tutorial 02" ));
        itemList.add(new Item("Android Tutorial 03" ));
        itemList.add(new Item("Android Tutorial 04" ));
        itemList.add(new Item("Android Tutorial 05" ));
        itemList.add(new Item("Android Tutorial 06" ));
        itemList.add(new Item("Android Tutorial 07" ));
        itemList.add(new Item("Android Tutorial 08" ));
        itemList.add(new Item("Android Tutorial 09" ));
        itemList.add(new Item("Android Tutorial 10" ));
        itemList.add(new Item("Android Tutorial 11" ));
        itemList.add(new Item("Android Tutorial 12" ));
    }

    private void initRecyclerView2() {
        constraintLayout = findViewById(R.id.constraintLayout);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setItemAnimator(new DefaultItemAnimator()); // 這個主要用於當一個item添加或者刪除的時候出現的動畫效果
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new RecyclerAdapter(this, itemList);
        recyclerView.setAdapter(mAdapter);

        // 接著在MainActivity.java中設置監聽器，採用匿名內部類的形式實現了onItemClickListener、onItemLongClickListener接口
        // 調用在RecyclerViewAdapter建立的點擊事件
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, itemList.get(position).getTitle(),
                        Toast.LENGTH_SHORT).show();
            }

        });

        mAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                // 長按某個item後，將移除這個item
                mAdapter.removeData(position);
                //adapter.addData(position);
                //adapter.changeData(position);

            }
        });

        /**
            adding item touch helper
            only ItemTouchHelper.LEFT added to detect Right to Left swipe
            僅添加ItemTouchHelper.LEFT以檢測從右向左滑動
            如果你想要Right -> Left 和 Left -> Right
            添加傳遞ItemTouchHelper.LEFT | ItemTouchHelper.Right 作為參數
         */
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }


    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator()); // 這個主要用於當一個item添加或者刪除的時候出現的動畫效果
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapter(myDataSet);
        recyclerView.setAdapter(adapter);

        // 接著在MainActivity.java中設置監聽器，採用匿名內部類的形式實現了onItemClickListener、onItemLongClickListener接口
        // 調用在RecyclerViewAdapter建立的點擊事件
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, myDataSet.get(position),
                        Toast.LENGTH_SHORT).show();
            }

        });

        adapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                // 長按某個item後，將移除這個item
                adapter.removeData(position);
                //adapter.addData(position);
                //adapter.changeData(position);

            }
        });

        //step.3為RecycleView添加ItemTouchHelper
        //先實例化Callback
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        //用Callback構造ItemTouchHelper
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //調用ItemTouchHelper的attachToRecyclerView方法建立聯繫
        touchHelper.attachToRecyclerView(recyclerView);

    }

    /**
        callback when recycler view is swiped (刷新回收視圖時的回調)
        item will be removed on swiped (物品將被刷掉)
        undo option will be provided in Snackbar to restore the item (提供撤消選項以恢復該項目)
     */
    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof  RecyclerAdapter.ViewHolder){
            // get the removed item name to display it int snackbar
            final String title = itemList.get(viewHolder.getAdapterPosition()).getTitle();

            // backup of removed item for undo purpose
            final Item deletedItem = itemList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(constraintLayout, title + " removed from item!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initRecyclerView2();
                        }
                    });
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_item:
                Toast.makeText(this, R.string.search, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.add_item:
                Toast.makeText(this, R.string.add, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.revert_item:
                return true;
            case R.id.delete_item:
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    public void aboutApp(View view) {
        startActivity(new Intent(MainActivity.this, AboutActivity.class));
    }

}
