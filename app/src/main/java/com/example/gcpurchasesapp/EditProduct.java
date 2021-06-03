package com.example.gcpurchasesapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class EditProduct extends AppCompatActivity {

    Button backBtn, saveBtn,searchBtn,deleteBtn;
    DatabaseHelper myDb;
    EditText prodDescEdit,  qtyBoughtEdit,totalEdit, storeEdit, idBox, dateEdit;
    int members, duration, fees;
    Spinner categoryEdit;
    String valueFromSpinner;
    DatePickerDialog picker;

    int categoryNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        saveBtn = (Button) findViewById(R.id.saveProdBtnEdit);
        backBtn = (Button) findViewById(R.id.backBtnEdit);
       deleteBtn = (Button) findViewById(R.id.deleteProdBtnEdit);
        prodDescEdit = (EditText) findViewById(R.id.prodTB_Edit);
        categoryEdit = (Spinner) findViewById(R.id.catTB_Edit);
        qtyBoughtEdit = (EditText) findViewById(R.id.totBoughtTB_Edit);
        totalEdit = (EditText) findViewById(R.id.totPriceTB_Edit);
        storeEdit = (EditText) findViewById(R.id.shopTB_Edit);
        dateEdit = (EditText) findViewById(R.id.dateTB_Edit);
        idBox = (EditText) findViewById(R.id.IDbox);
        searchBtn = findViewById(R.id.searchBtn);
        myDb = new DatabaseHelper(this);

        MainActivity mainInfo = new MainActivity();
        members = mainInfo.getNoOfmembers();
        duration = mainInfo.getDuration();
        fees = mainInfo.getFees();

        // adding a dropdown menu for category using a spinner
        String[] ddMenu = getResources().getStringArray(R.array.category_names);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,ddMenu);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryEdit.setAdapter(adapter);


        UpdateData();
        SearchData();
        deleteData();

        backBtn.setOnClickListener((v)->{

            Intent intent = new Intent(EditProduct.this, Home.class);
            startActivity(intent);
        });
    }

    public void SearchData(){

        searchBtn.setOnClickListener((v)->{

            String id = idBox.getText().toString();


            Cursor res = myDb.searchByID(id);
            if (res.getCount() == 0) {
                Toast.makeText(EditProduct.this, "No data found", Toast.LENGTH_SHORT).show();
                return;
            }

            while (res.moveToNext()) {


                 if(res.getString(2).equalsIgnoreCase("kitchen"))
                    categoryNum=0;
                else if(res.getString(2).equalsIgnoreCase("bathroom"))
                    categoryNum=1;
                else if(res.getString(2).equalsIgnoreCase("detergents"))
                    categoryNum=2;
                else if(res.getString(2).equalsIgnoreCase("personal"))
                    categoryNum=3;
                else if(res.getString(2).equalsIgnoreCase("laundry"))
                    categoryNum=4;
                else if(res.getString(2).equalsIgnoreCase("food"))
                    categoryNum=5;



                prodDescEdit.setText(res.getString(1));
                categoryEdit.setSelection(5);
                qtyBoughtEdit.setText(res.getString(3));
                totalEdit.setText(res.getString(6));
                storeEdit.setText(res.getString(7));
                dateEdit.setText(res.getString(8));
            }

        });


    }


    public void UpdateData()
    {

        categoryEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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

        dateEdit.setInputType(InputType.TYPE_NULL);
        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(EditProduct.this,
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
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        String id = idBox.getText().toString();
                        String desc = prodDescEdit.getText().toString();
                        String cat = valueFromSpinner;
                        String qtyBought = qtyBoughtEdit.getText().toString();
                        String total = totalEdit.getText().toString();
                        String store = storeEdit.getText().toString();
                        String date = dateEdit.getText().toString();

                        if (id.isEmpty() || desc.isEmpty() || qtyBought.isEmpty() || total.isEmpty() || store.isEmpty() || date.isEmpty()) {
                            Toast.makeText(EditProduct.this, "MISSING VALUES\nPlease enter all values to proceed", Toast.LENGTH_SHORT).show();
                        } else {
                            double qty = Double.parseDouble(qtyBought);
                            double tot = Double.parseDouble(total);


                            if (qty <= 0 || tot <= 0) {
                                Toast.makeText(EditProduct.this, "All Values must be greater than zero", Toast.LENGTH_SHORT).show();
                            }else
                                 {

                                    String qtyPerson = String.valueOf(Integer.parseInt(qtyBought) / members);
                                    String unitPrice = String.valueOf(Integer.parseInt(total) / Integer.parseInt(qtyBought));

                                    boolean IsUpdate = myDb.updateData(id, desc, cat, qtyBought, qtyPerson, unitPrice, total, store, date);
                                    if (IsUpdate == true) {
                                        Toast.makeText(EditProduct.this, "Data updated", Toast.LENGTH_SHORT).show();
                                        idBox.setText("");
                                        prodDescEdit.setText("");
                                        categoryEdit.setSelection(0);
                                        qtyBoughtEdit.setText("");
                                        totalEdit.setText("");
                                        storeEdit.setText("");
                                        dateEdit.setText("");
                                    } else {
                                        Toast.makeText(EditProduct.this, "Data not updated", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    }} );
    }

    public void deleteData(){

        deleteBtn.setOnClickListener((v)->{

            showMessage("Delete", "Are you sure you want to delete product ID: "+idBox.getText().toString()+"?");

        });


    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.setNegativeButton("YES",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Integer deletedRows = myDb.deleteData(idBox.getText().toString());

                if (deletedRows > 0) {
                    Toast.makeText(EditProduct.this, "Product Deleted", Toast.LENGTH_LONG).show();
                    idBox.setText("");
                    prodDescEdit.setText("");
                    categoryEdit.setSelection(0);
                    qtyBoughtEdit.setText("");
                    totalEdit.setText("");
                    storeEdit.setText("");
                    dateEdit.setText("");
                } else {
                    Toast.makeText(EditProduct.this, "Data not detected", Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setPositiveButton("NO",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });


        builder.show();


    }
}

