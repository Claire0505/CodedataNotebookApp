package com.claire.codedatanotebookapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

//1. 從AppcompatActivity改為Activity
public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //2. 取消元件的應用程式標題，要放在setContentView之上，不然會報錯
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about);
    }

    public void clickOK(View view) {
        finish(); //結束
    }
}
