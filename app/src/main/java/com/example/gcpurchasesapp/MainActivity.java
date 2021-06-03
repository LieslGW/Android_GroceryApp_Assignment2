package com.example.gcpurchasesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    Button btnProceed;
    static int noOfmembers =12;
    static int duration =10;
    static int fees=300;


    public static final String DATABASE_NAME = "GroceryClub.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnProceed = findViewById(R.id.proceedBtn);


        btnProceed.setOnClickListener((v)-> {


            Intent intent = new Intent(MainActivity.this, Home.class);

            startActivity(intent);

        });
    }



    public int getNoOfmembers(){
        return noOfmembers;
    }



    public int getDuration() {
        return duration;
    }


    public int getFees(){
        return fees;
    }
}
