package com.claire.codedatanotebookapp;

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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerViewAdapter adapter;

    private ArrayList<String> myDataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initRecyclerView();
    }

    private void initData() {
        //建立測式資料
        myDataSet = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            myDataSet.add("Android Tutorial " + i);
        }
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
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
        Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show();
    }
}
