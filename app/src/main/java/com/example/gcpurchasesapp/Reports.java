package com.example.gcpurchasesapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Reports extends AppCompatActivity {


    DatabaseHelper myDb;
    Button viewAllBtn, filteredViewBtn, searchBtn, totalSpentBtn, backBtn;
    EditText editSearch;
    int members, duration, fees;
   String cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        myDb = new DatabaseHelper(this);
        viewAllBtn = findViewById(R.id.btnViewAll);
        filteredViewBtn = findViewById(R.id.btnFilteredViews);
        searchBtn = findViewById(R.id.btnSearch);
        totalSpentBtn = findViewById(R.id.btnTotalSpent);
        backBtn = findViewById(R.id.backBtnReports);
        editSearch = findViewById(R.id.searchET);

        MainActivity mainInfo = new MainActivity();

        members = mainInfo.getNoOfmembers();
        duration = mainInfo.getDuration();
        fees = mainInfo.getFees();


        viewAll();
        search();


        totalSpentBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int tot = myDb.getTotal();

                        int annualTotal = members*duration*fees;

                        int diff = annualTotal-tot;



                        showTotalMessage("Spenditure to Date", "\nTotal Spent: R"+tot+"\n\n\nTotal left: R"+diff);


                    }
                }
        );


        backBtn.setOnClickListener((v)->{

            Intent intent = new Intent(Reports.this, Home.class);
            startActivity(intent);
        });

        filteredViewBtn.setOnClickListener((v)->{

            Intent intent = new Intent(Reports.this, FilterReports.class);

            startActivity(intent);
        });


    }

     public void viewAll() {
         viewAllBtn.setOnClickListener(
                 new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Cursor res = myDb.getAllData();
                         if (res.getCount() == 0) {
                             showMessage("Error", "Nothing found");
                             return;
                         }

                         StringBuffer buffer = new StringBuffer();
                         while (res.moveToNext()) {
                             buffer.append("ID: " + res.getString(0) + "\n");
                             buffer.append("PRODUCT: " + res.getString(1) + "\n");
                             buffer.append("CATEGORY: " + res.getString(2) + "\n");
                             buffer.append("QTY BOUGHT: " + res.getString(3) + "\n");
                             buffer.append("QTY PER PERSON: " + res.getString(4) + "\n");
                             buffer.append("UNIT PRICE: R" + res.getString(5) + "\n");
                             buffer.append("TOTAL PRICE: R" + res.getString(6) + "\n");
                             buffer.append("SUPPLIER: " + res.getString(7) + "\n");
                             buffer.append("DATE: " + res.getString(8) + "\n\n");
                         }
                         showMessage("View All", buffer.toString());


                     }
                 }
         );
     }

    public void search() {
        searchBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String search = editSearch.getText().toString();

                        Cursor res = myDb.search(search);
                        if (res.getCount() == 0) {
                            showMessage("Error", "Nothing found");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("ID:" + res.getString(0) + "\n");
                            buffer.append("PRODUCT:" + res.getString(1) + "\n");
                            buffer.append("CATEGORY:" + res.getString(2) + "\n");
                            buffer.append("QTY BOUGHT:" + res.getString(3) + "\n");
                            buffer.append("QTY PER PERSON:" + res.getString(4) + "\n");
                            buffer.append("UNIT PRICE: R" + res.getString(5) + "\n");
                            buffer.append("TOTAL PRICE:R" + res.getString(6) + "\n");
                            buffer.append("SUPPLIER:" + res.getString(7) + "\n");
                            buffer.append("DATE: " + res.getString(8) + "\n\n");
                        }
                        showMessage("Product", buffer.toString());
                        editSearch.setText("");


                    }
                }
        );
    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();


    }

    public void showTotalMessage(String title,String Message){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        builder.show();


    }

}
