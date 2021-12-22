package com.example.vehiclemanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class AdapterVehicle extends BaseAdapter {
    Activity context;
    ArrayList<Vehicle> list;

    public AdapterVehicle(Activity context, ArrayList<Vehicle> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.listview_row, null);
        ImageView IMG = (ImageView) row.findViewById(R.id.img);
        TextView TV_ID = (TextView) row.findViewById(R.id.tv_id) ;
        TextView TV_NAME = (TextView) row.findViewById(R.id.tv_name);
        TextView TV_DAYSTORED = (TextView) row.findViewById(R.id.tv_dayStored);
        TextView TV_PRICE = (TextView) row.findViewById(R.id.tv_price);
        TextView TV_TYPE = (TextView) row.findViewById(R.id.tv_type);
        TextView TV_BRAND = (TextView) row.findViewById(R.id.tv_brand);
        Button BTN_EDIT = (Button) row.findViewById(R.id.btn_edit);
        Button BTN_DEL = (Button) row.findViewById(R.id.btn_delete);

        Vehicle vehicle = list.get(position);
        TV_ID.setText("ID: " + vehicle.id + "");
        TV_NAME.setText("Name: " +vehicle.name + "");
        TV_BRAND.setText("Brand: " +vehicle.brand + "");
        TV_TYPE.setText("Type: " +vehicle.type + "");
        TV_DAYSTORED.setText("Day stored: " + vehicle.dayStored + "");
        TV_PRICE.setText("Price: " +vehicle.price + "");
        Bitmap bmImg = BitmapFactory.decodeByteArray(vehicle.image,0,vehicle.image.length);
        IMG.setImageBitmap(bmImg);

        BTN_EDIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("ID", vehicle.id);
                context.startActivity(intent);
            }
        });

        BTN_DEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Delete Confirm");
                builder.setMessage("Are you sure to delete this vehicle ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(vehicle.id);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return row;
    }
    private void delete(int idVehicle) {
        SQLiteDatabase database = Database.initDatabase(context, "Vehicle.sqlite");
        database.delete("Vehicle", "ID = ?", new String[]{idVehicle + ""});
        list.clear();
        Cursor cursor = database.rawQuery("SELECT * FROM Vehicle", null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String brand = cursor.getString(2);
            String type = cursor.getString(3);
            int dayStored = cursor.getInt(4);
            long price = cursor.getLong(5);
            byte[] image = cursor.getBlob(6);
            list.add(new Vehicle(id, name, brand, type, dayStored, price, image));
        }
        notifyDataSetChanged();
    }


}
