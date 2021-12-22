package com.example.vehiclemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class SortActivity extends AppCompatActivity {

    final String DATABASE_NAME = "Vehicle.sqlite";
    SQLiteDatabase database;

    Button btn_sort, btn_cancel;
    Spinner spinner;
    ListView listView;
    ArrayList<Vehicle> list;
    ArrayList<String> listItems;
    AdapterVehicle adapterVehicle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sort_interface);
        anhxa();
        addItemToSpinner();
        readData();
        addEvent();
    }

    private void anhxa() {
        btn_sort = (Button) findViewById(R.id.btnSort);
        btn_cancel = (Button) findViewById(R.id.btnCancel);
        spinner = (Spinner) findViewById(R.id.spnCategory);
        listView = (ListView) findViewById(R.id.lsvSortedList);
        list = new ArrayList<>();
        listItems = new ArrayList<>();
        adapterVehicle = new AdapterVehicle(this, list);
        listView.setAdapter(adapterVehicle);
    }

    private void addItemToSpinner() {
        listItems.add("ID");
        listItems.add("Name");
        listItems.add("Brand");
        listItems.add("Type");
        listItems.add("Day Stored");
        listItems.add("Price");
        ArrayAdapter adapter = new ArrayAdapter(SortActivity.this, android.R.layout.simple_spinner_item, listItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SortActivity.this, listItems.get(position),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }

    private void addEvent(){
        btn_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int iCurrentSelection = spinner.getSelectedItemPosition();
                if (iCurrentSelection == getIndex(spinner, "ID")){
                    readData();
                    sortById();
                }
                if (iCurrentSelection == getIndex(spinner, "Name")){
                    readData();
                    sortByName();
                }
                if (iCurrentSelection == getIndex(spinner, "Brand")){
                    readData();
                    sortByBrand();
                }
                if (iCurrentSelection == getIndex(spinner, "Type")){
                    readData();
                    sortByType();
                }
                if (iCurrentSelection == getIndex(spinner, "Day Stored")){
                    readData();
                    sortByDay();
                }
                if (iCurrentSelection == getIndex(spinner, "Price")){
                    readData();
                    sortByPrice();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void readData() {
        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM Vehicle", null);
        list.clear();
        Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_LONG).show();
        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String brand = cursor.getString(2);
            String type = cursor.getString(3);
            int dayStored = cursor.getInt(4);
            long price = cursor.getLong(5);
            byte[] image = cursor.getBlob(6);
            list.add(new Vehicle(id, name, brand, type, dayStored, price, image));
        }

    }

    private void sortById(){
        spinner.setSelection(getIndex(spinner, "ID"), true);
        Collections.sort(list, (v1, v2) -> {
            int compare = Integer.compare(v1.getId(), v2.getId());
            return compare;
        });
        adapterVehicle.notifyDataSetChanged();
    }

    private void sortByName(){
        spinner.setSelection(getIndex(spinner, "Name"), true);
        Collections.sort(list, (v1, v2) -> {
            int compare = v1.getName().compareTo(v2.getName());
            return compare;
        });
        adapterVehicle.notifyDataSetChanged();
    }

    private void sortByBrand(){
        spinner.setSelection(getIndex(spinner, "Brand"), true);
        Collections.sort(list, (v1, v2) -> {
            int compare = v1.getBrand().compareTo(v2.getBrand());
            return compare;
        });
        adapterVehicle.notifyDataSetChanged();
    }

    private void sortByType(){
        spinner.setSelection(getIndex(spinner, "Type"), true);
        Collections.sort(list, (v1, v2) -> {
            int compare = v1.getType().compareTo(v2.getType());
            return compare;
        });
        adapterVehicle.notifyDataSetChanged();
    }

    private void sortByDay(){
        spinner.setSelection(getIndex(spinner, "Day Stored"), true);
        Collections.sort(list, (v1, v2) -> {
            int compare = Integer.compare(v2.getDayStored(), v1.getDayStored());
            return compare;
        });
        adapterVehicle.notifyDataSetChanged();
    }

    private void sortByPrice(){
        spinner.setSelection(getIndex(spinner, "Day Stored"), true);
        Collections.sort(list, (v1, v2) -> {
            int compare = Long.compare(v2.getPrice(), v1.getPrice());
            return compare;
        });
        adapterVehicle.notifyDataSetChanged();
    }
}