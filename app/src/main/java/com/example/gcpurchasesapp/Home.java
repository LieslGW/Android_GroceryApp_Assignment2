package com.example.gcpurchasesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Home extends AppCompatActivity {

    Button newProdBtn, editProdBtn, reportBtn; //closeBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        newProdBtn = findViewById(R.id.BtnNewPurchase);
        editProdBtn = findViewById(R.id.BtnEditProduct);
        reportBtn = findViewById(R.id.BtnReports);
        //closeBtn = findViewById(R.id.BtnClose);


        newProdBtn.setOnClickListener((v)->{

            Intent intent = new Intent(Home.this, NewProduct.class);
            startActivity(intent);
        });

        editProdBtn.setOnClickListener((v)->{

            Intent intent = new Intent(Home.this, EditProduct.class);
            startActivity(intent);
        });


        reportBtn.setOnClickListener((v)->{

            Intent intent = new Intent(Home.this, Reports.class);
            startActivity(intent);
        });

        /*closeBtn.setOnClickListener((v)->{
            finish();
            System.exit(0);

        });*/


    }
}
