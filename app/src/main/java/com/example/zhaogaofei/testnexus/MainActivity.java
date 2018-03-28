package com.example.zhaogaofei.testnexus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.testlibrary.Test;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Test test = new Test();
    }
}
