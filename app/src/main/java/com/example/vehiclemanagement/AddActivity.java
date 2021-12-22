package com.example.vehiclemanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    final String DATABASE_NAME = "Vehicle.sqlite";
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;
    Button BTN_SAVE, BTN_CLEAR, BTN_EXIT, BTN_TAKEPHOTE, BTN_CHOOSEPHOTO;
    EditText TV_NAME, TV_DAYSTORE, EDT_PRICE;
    TextView TV_BRAND;
    RadioButton RAD_CAR, RAD_MOTORBIKE;
    Spinner SPINNER;
    ImageView IMG_EDIT;
    final ArrayList<String> ListOfBrand = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_interface);
        anhxa();
        addItemToSpinner();
        addEvent();
    }

    //private method of your class
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }
    private void addItemToSpinner() {
        ListOfBrand.add("Rolls Royce");
        ListOfBrand.add("Mercedes Benz");
        ListOfBrand.add("Land Rover");
        ListOfBrand.add("Bentley");
        ListOfBrand.add("Lexus");
        ListOfBrand.add("Jaguar");
        ListOfBrand.add("BMW");
        ListOfBrand.add("Audi");
        ListOfBrand.add("Porsche");
        ListOfBrand.add("Maserati");
        ListOfBrand.add("Honda");
        ListOfBrand.add("Yamaha");
        ListOfBrand.add("Piaggio");
        ListOfBrand.add("Yamaha");
        ListOfBrand.add("SYM");
        ListOfBrand.add("Triumph");
        ListOfBrand.add("Suzuki");
        ListOfBrand.add("Ducati");
        ListOfBrand.add("KTM");
        ListOfBrand.add("Kawasaki");
        ListOfBrand.add("Ford");
        ListOfBrand.add("none");
        ArrayAdapter adapter = new ArrayAdapter(AddActivity.this, android.R.layout.simple_spinner_item, ListOfBrand);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SPINNER.setAdapter(adapter);
        SPINNER.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AddActivity.this, ListOfBrand.get(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void addEvent(){
        BTN_CHOOSEPHOTO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        BTN_TAKEPHOTE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        BTN_SAVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TV_NAME.length() == 0) {
                    TV_NAME.setError("Don't let name empty !!!");
                } else if (SPINNER.getSelectedItem().toString().equals("none")) {
                    TV_BRAND.setError("Your have to change another brand");
                } else if (Integer.parseInt(TV_DAYSTORE.getText().toString()) == 0) {
                    TV_DAYSTORE.setError("Pls enter Day Stored Number greater than 0 !!!");
                } else {
                    insert();
                }
            }
        });

        BTN_CLEAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll();
            }
        });

        BTN_EXIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    private void anhxa() {
        BTN_SAVE = (Button) findViewById(R.id.btnSave);
        BTN_CLEAR = (Button) findViewById(R.id.btnClearEdit);
        BTN_EXIT = (Button) findViewById(R.id.btnExitEdit);
        BTN_TAKEPHOTE = (Button) findViewById(R.id.btn_TakePhoto);
        BTN_CHOOSEPHOTO = (Button) findViewById(R.id.btn_ChoosePhoto);
        TV_NAME = (EditText) findViewById(R.id.edtNameEdit);
        TV_DAYSTORE = (EditText) findViewById(R.id.edtDayEdit);
        TV_BRAND = (TextView) findViewById(R.id.tvwBrandEdit);
        RAD_CAR = (RadioButton) findViewById(R.id.radCarEdit);
        RAD_MOTORBIKE = (RadioButton) findViewById(R.id.radMotobikeEdit);
        SPINNER = (Spinner) findViewById(R.id.spnBrandEdit);
        IMG_EDIT = (ImageView) findViewById(R.id.imv_ImageEdit);
        EDT_PRICE = (EditText) findViewById(R.id.edtPrice);
    }

    private void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private void choosePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOOSE_PHOTO) {
                try {
                    Uri imageUri = data.getData();
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    IMG_EDIT.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                IMG_EDIT.setImageBitmap(bitmap);
            }
        }
    }

    private void insert(){
        String name = TV_NAME.getText().toString();
        String brand = SPINNER.getSelectedItem().toString();
        String type = "Car";
        if (RAD_MOTORBIKE.isSelected()){
            type = "Motorbike";
        }
        int dayStored = Integer.parseInt(TV_DAYSTORE.getText().toString());
        long price = Long.parseLong(EDT_PRICE.getText().toString());
        byte[] image = getByteArrayFromImageView(IMG_EDIT);
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", name);
        contentValues.put("Brand", brand);
        contentValues.put("Type", type);
        contentValues.put("DayStored", dayStored);
        contentValues.put("Price", price);
        contentValues.put("Image", image);
        SQLiteDatabase database = Database.initDatabase(this, "Vehicle.sqlite");
        database.insert("Vehicle", null, contentValues);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private long doCalPriceByDay(int day) {
        float first_price = 10000f;
        float total = 0.0f;
        int dayStored = day;
        while (day > 0) {
            total += 7 * Math.max(7000, first_price);
            day -= 7;
            first_price -= 1000;
            if (day > 0 && day < 7) {
                total += day * Math.max(7000, first_price);
                break;
            }

        }
        if (dayStored <= 7) {
            total = dayStored * 10000f;
        }
        return (long) total;
    }

    private void cancel(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void clearAll(){
        TV_NAME.setText("");
        RAD_MOTORBIKE.setChecked(true);
        SPINNER.setSelection(getIndex(SPINNER, "none"), true);
        RAD_MOTORBIKE.setChecked(true);
        TV_DAYSTORE.setText(Integer.toString(0));
        EDT_PRICE.setText(Long.toString(0));
    }

    private byte[] getByteArrayFromImageView(ImageView imgv){
        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}