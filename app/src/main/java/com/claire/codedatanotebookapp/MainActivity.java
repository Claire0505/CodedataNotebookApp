package com.claire.codedatanotebookapp;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRecyclerView();
    }

    private void setRecyclerView() {
        //建立測式資料
        ArrayList<String> myDataSet = new ArrayList<>();
        for (int i = 0 ; i < 50; i++){
            myDataSet.add("Android Tutorial " + i);
        }

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(myDataSet);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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
