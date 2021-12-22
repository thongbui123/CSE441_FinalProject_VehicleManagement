package com.example.vehiclemanagement;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    final String DATABASE_NAME = "Vehicle.sqlite";
    SQLiteDatabase database;

    ListView listView;
    ArrayList<Vehicle> list;
    AdapterVehicle adapterVehicle;
    TextView TV_SORT;
    Button BTN_ADD, BTN_SAVE, BTN_RESTORE, BTN_BACK;
    ImageView IMV_REFESH, IMV_SEARCH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_interface);
        anhxa();
        readData();

    }

    private void anhxa() {
        IMV_SEARCH = (ImageView) findViewById(R.id.imvSearch);
        IMV_SEARCH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        IMV_REFESH = (ImageView) findViewById(R.id.imvRefresh);
        IMV_REFESH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterVehicle.notifyDataSetChanged();
            }
        });
        TV_SORT = (TextView) findViewById(R.id.tvwSort);
        TV_SORT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SortActivity.class);
                startActivity(intent);
            }
        });
        BTN_ADD = (Button) findViewById(R.id.btnAdd);
        BTN_ADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
        BTN_BACK = (Button) findViewById(R.id.btnBack);
        BTN_BACK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
        listView = (ListView) findViewById(R.id.lsv_VehicleList);
        list = new ArrayList<>();
        adapterVehicle = new AdapterVehicle(this, list);
        listView.setAdapter(adapterVehicle);
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
        adapterVehicle.notifyDataSetChanged();

    }






}