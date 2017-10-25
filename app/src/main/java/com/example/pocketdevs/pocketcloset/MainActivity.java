package com.example.pocketdevs.pocketcloset;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    //This is the function to send you to the add clothing page from main
    public void addClothes(View v){
        Intent intent = new Intent(MainActivity.this, AddClothes.class);
        startActivity(intent);
    }

    //This is the function to send you to the view closet page from main
    public void viewCloset(View v){
        Intent intent = new Intent(MainActivity.this, ViewCloset.class);
        startActivity(intent);
    }

    //This is the function to send you to the pack my bag page from main
    public void packMyBag(View v){
        Intent intent = new Intent(MainActivity.this, PackMyBag.class);
        startActivity(intent);
    }
}
