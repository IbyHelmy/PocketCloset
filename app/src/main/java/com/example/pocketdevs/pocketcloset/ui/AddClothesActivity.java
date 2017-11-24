package com.example.pocketdevs.pocketcloset.ui;

/**
 * Created by Saif on 2017-10-23.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.pocketdevs.pocketcloset.R;
import com.example.pocketdevs.pocketcloset.entity.Clothes;

import java.io.ByteArrayOutputStream;


public class AddClothesActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;

    private ImageView img;

    private Spinner materialSpinner;
    private Spinner typeSpinner;
    private Spinner colorSpinner;
    private Spinner subTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes);
        img = findViewById(R.id.pickPicture);

        typeSpinner = findViewById(R.id.spinnerType);
        subTypeSpinner = findViewById(R.id.spinnerSubType);
        colorSpinner = findViewById(R.id.spinnerColor);
        materialSpinner = findViewById(R.id.spinnerMaterial);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> spinnerArrayAdapter = null;
                String data[] = null;
                switch (position){
                    case 0:
                        data = getResources().getStringArray(R.array.headwearList);
                        break;
                    case 1:
                        data = getResources().getStringArray(R.array.topsList);
                        break;
                    case 2:
                        data = getResources().getStringArray(R.array.bottomsList);
                        break;
                    case 3:
                        data = getResources().getStringArray(R.array.footwearList);
                        break;
                    case 4:
                        data = getResources().getStringArray(R.array.outerwearList);
                        break;
                    case 5:
                        data = getResources().getStringArray(R.array.skirtList);
                        break;
                    case 6:
                        data = getResources().getStringArray(R.array.dressList);
                        break;
                    case 7:
                        data = getResources().getStringArray(R.array.accessoriesList);
                        break;
                }

                spinnerArrayAdapter = new ArrayAdapter<String>(AddClothesActivity.this, android.R.layout.simple_spinner_dropdown_item, data);
                subTypeSpinner.setAdapter(spinnerArrayAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button gold = findViewById(R.id.capturePicture);
        gold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }


        });
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            img.setImageBitmap(imageBitmap);
        }
    }

    /**
     *
     * @param v
     */
    public void saveClothes(View v){
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        Clothes item = new Clothes(
                typeSpinner.getSelectedItemPosition(),
                subTypeSpinner.getSelectedItemPosition(),
                materialSpinner.getSelectedItemPosition(),
                colorSpinner.getSelectedItemPosition(),
                Base64.encodeToString(baos.toByteArray(), 0));
        item.save();

        finish();
    }
}