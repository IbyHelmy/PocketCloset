package com.example.pocketdevs.pocketcloset;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
    }

    void addClothes(View v){
        Intent intent = new Intent(MainPage.this, AddClothes.class);
        startActivity(intent);
    }
}
