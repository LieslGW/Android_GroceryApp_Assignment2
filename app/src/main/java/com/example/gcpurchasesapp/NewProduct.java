package com.example.gcpurchasesapp;


import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;

import android.os.Bundle;

import android.text.InputType;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;


import android.widget.Spinner;

import android.widget.Toast;

import java.util.Calendar;


public class NewProduct extends AppCompatActivity {

    //declare variables
    DatabaseHelper myDb;
    EditText prodDescEdit, qtyBoughtEdit,totalEdit, storeEdit, dateEdit;
    Spinner categoryMenu;
    Button saveBtn, backBtn;
    int members, duration, fees;

    String valueFromSpinner;

    DatePickerDialog picker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        myDb = new DatabaseHelper(this);
        prodDescEdit = (EditText) findViewById(R.id.prodTB);
       categoryMenu = (Spinner) findViewById(R.id.categoryTB);
        qtyBoughtEdit = (EditText) findViewById(R.id.totBoughtTB);
        totalEdit = (EditText) findViewById(R.id.totPriceTB);
        storeEdit = (EditText) findViewById(R.id.shopTB);
        dateEdit = (EditText) findViewById(R.id.dateTB);
        saveBtn = (Button) findViewById(R.id.saveProdBtn);
        backBtn = (Button) findViewById(R.id.backBtn);




        MainActivity mainInfo = new MainActivity();

        members = mainInfo.getNoOfmembers();
        duration = mainInfo.getDuration();
        fees = mainInfo.getFees();



        // adding a dropdown menu for category using a spinner
        String[] ddMenu = getResources().getStringArray(R.array.category_names);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,ddMenu);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryMenu.setAdapter(adapter);


        AddData();


        backBtn.setOnClickListener((v) -> {

            Intent intent = new Intent(NewProduct.this, Home.class);
            startActivity(intent);
        });

    }




    public void AddData() {

        dateEdit.setInputType(InputType.TYPE_NULL);
        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(NewProduct.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateEdit.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        saveBtn.setOnClickListener(
                (v) -> {

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


                    String desc = prodDescEdit.getText().toString();
                    String cat = valueFromSpinner;
                    String qtyBought = qtyBoughtEdit.getText().toString();

                    String total = totalEdit.getText().toString();

                    String store = storeEdit.getText().toString();
                    String date = dateEdit.getText().toString();

                    if (desc.isEmpty() || qtyBought.isEmpty()|| total.isEmpty() || store.isEmpty() || date.isEmpty()) {
                        Toast.makeText(NewProduct.this, "MISSING VALUES\nPlease enter all values to proceed", Toast.LENGTH_SHORT).show();
                    } else  {
                        double qty = Double.parseDouble(qtyBought);
                        double tot = Double.parseDouble(total);

                        if(qty <= 0 || tot <= 0) {
                            Toast.makeText(NewProduct.this, "All Values must be greater than zero", Toast.LENGTH_SHORT).show();
                        }

                        else{

                            String qtyPerson = String.valueOf(Double.parseDouble(qtyBought) / members);
                            String unitPrice = String.valueOf(Double.parseDouble(total) / Double.parseDouble(qtyBought));

                            boolean Isinserted = myDb.insertData(desc, cat, qtyBought, qtyPerson, unitPrice, total, store, date);

                            if (Isinserted == true) {
                                Toast.makeText(NewProduct.this, "Data inserted", Toast.LENGTH_SHORT).show();
                                prodDescEdit.setText("");
                                categoryMenu.setSelection(0);
                                qtyBoughtEdit.setText("");
                                totalEdit.setText("");
                                storeEdit.setText("");
                                dateEdit.setText("");

                            } else {
                                Toast.makeText(NewProduct.this, "Data not inserted", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                });
    }



}
