package com.example.vehiclemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchActivity extends AppCompatActivity {
    final String DATABASE_NAME = "Vehicle.sqlite";
    SQLiteDatabase database;


    Button btn_search, btn_cancel;
    EditText edt_search;
    ImageView img_refesh;
    Spinner spinner;
    ListView listView;
    ArrayList<Vehicle> list;
    ArrayList<String> listItems;
    AdapterVehicle adapterVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_interface);
        anhxa();
        addItemToSpinner();
        readData();
        addEvent();
    }

    private void addEvent() {
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int iCurrentSelection = spinner.getSelectedItemPosition();
                String input = (String) edt_search.getText().toString();
                if (edt_search.length() == 0){
                    edt_search.setError("Please enter something");
                } else if (iCurrentSelection == getIndex(spinner, "Name")){
                    readData();
                    searchByName(input);
                } else if (iCurrentSelection == getIndex(spinner, "Brand")){
                    readData();
                    searchByBrand(input);
                } else if (iCurrentSelection == getIndex(spinner, "Type")){
                    readData();
                    searchByType(input);
                } else if(iCurrentSelection == getIndex(spinner, "ID")){
                    if (Pattern.matches("^[0-9]+$", input) == false) {
                        edt_search.setError("Please enter number only");
                    } else {
                        int number = Integer.parseInt(input);
                        readData();
                        searchById(number);
                    }
                }
            }
        });
        img_refesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refeshList();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void anhxa() {
        btn_search = (Button) findViewById(R.id.btnSearch);
        btn_cancel = (Button) findViewById(R.id.btnExit);
        edt_search = (EditText) findViewById(R.id.edtSearchInput);
        img_refesh = (ImageView) findViewById(R.id.imv_refesh);
        spinner = (Spinner) findViewById(R.id.spnType);
        listView = (ListView) findViewById(R.id.lsvSearchedList);
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
        ArrayAdapter adapter = new ArrayAdapter(SearchActivity.this, android.R.layout.simple_spinner_item, listItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SearchActivity.this, listItems.get(position),Toast.LENGTH_SHORT).show();

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

    private void refeshList(){
        adapterVehicle = new AdapterVehicle(this, list);
        listView.setAdapter(adapterVehicle);
        readData();
        adapterVehicle.notifyDataSetChanged();
    }

    public void searchByName(String input){
        ArrayList<Vehicle> tempList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++){
            if (list.get(i).name.equalsIgnoreCase(input) == true){
                tempList.add(list.get(i));
            }
        }
        adapterVehicle = new AdapterVehicle(SearchActivity.this, tempList);
        listView.setAdapter(adapterVehicle);
        adapterVehicle.notifyDataSetChanged();
    }

    public void searchByBrand(String input){
        ArrayList<Vehicle> tempList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++){
            if (list.get(i).brand.equalsIgnoreCase(input) == true){
                tempList.add(list.get(i));
            }
        }
        adapterVehicle = new AdapterVehicle(SearchActivity.this, tempList);
        listView.setAdapter(adapterVehicle);
        adapterVehicle.notifyDataSetChanged();
    }

    public void searchByType(String input){
        ArrayList<Vehicle> tempList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++){
            if (list.get(i).type.equalsIgnoreCase(input) == true){
                tempList.add(list.get(i));
            }
        }
        adapterVehicle = new AdapterVehicle(SearchActivity.this, tempList);
        listView.setAdapter(adapterVehicle);
        adapterVehicle.notifyDataSetChanged();
    }

    public void searchById(int number){
        ArrayList<Vehicle> tempList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++){
            if (list.get(i).id == number){
                tempList.add(list.get(i));
            }
        }
        adapterVehicle = new AdapterVehicle(SearchActivity.this, tempList);
        listView.setAdapter(adapterVehicle);
        adapterVehicle.notifyDataSetChanged();
    }


}