package com.example.gcpurchasesapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.lang.reflect.Array;

public class FilterReports extends AppCompatActivity {

    DatabaseHelper myDb;
    Spinner categoryMenu;
    Button categoryGO, storeGo, back;
    EditText store;
    String valueFromSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_reports);

        myDb = new DatabaseHelper(this);
        categoryMenu = (Spinner) findViewById(R.id.spinnerCatFilter);
        categoryGO = (Button) findViewById(R.id.btnGo1);
        storeGo = (Button) findViewById(R.id.btnGo2);
        store = (EditText) findViewById(R.id.storeFilter);
        back = (Button) findViewById(R.id.backBtnFilter);



        // adding a dropdown menu for category using a spinner
        String[] ddMenu = getResources().getStringArray(R.array.category_names);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ddMenu);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryMenu.setAdapter(adapter);


        back.setOnClickListener((v) -> {

            Intent intent = new Intent(FilterReports.this, Reports.class);
            startActivity(intent);
        });

        viewByCategory();
        viewByStore();

    }

    public void viewByCategory() {
        categoryMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                valueFromSpinner = parent.getItemAtPosition(position).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        categoryGO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Cursor res = myDb.getDataByCategory(valueFromSpinner);

            if (res.getCount() == 0) {
                showMessage("Error", "Nothing found");
                return;
            }

               StringBuffer buffer = new StringBuffer();
                int num = 1;

            while (res.moveToNext()) {

                buffer.append(num + ". " +res.getString(1) + "\t\t\t\t\t\t");
                buffer.append("x " + res.getString(4) + " p/p\n");
                num++;
            }
            showMessage(valueFromSpinner, buffer.toString());
            }
    });
}

    public void viewByStore() {

        storeGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String storeSearch = store.getText().toString();




                Cursor res = myDb.getDataByStore(storeSearch);




                    if (res.getCount() == 0) {
                        showMessage("Error", "Store not found");
                        return;
                    }

                    StringBuffer buffer = new StringBuffer();
                    int num = 1;
                    while (res.moveToNext()) {

                        buffer.append(num + ". " + res.getString(1) + "\t\t\t\t");
                        buffer.append("Qty:  " + res.getString(3) + "\n");
                        buffer.append("Total Price: R" + res.getString(6) + "\n\n");
                        num++;
                    }
                    showMessage(storeSearch, buffer.toString());
                }

            });
    }


    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();

    }
}
