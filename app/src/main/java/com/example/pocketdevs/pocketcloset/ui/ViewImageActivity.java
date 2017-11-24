package com.example.pocketdevs.pocketcloset.ui;

/**
 * Created by Saif on 2017-11-01.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pocketdevs.pocketcloset.R;
import com.example.pocketdevs.pocketcloset.entity.Clothes;

public class ViewImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        ImageView image = findViewById(R.id.image);
        TextView details = findViewById(R.id.details);

        long clothId = getIntent().getExtras().getLong("clothId");
        final Clothes cloth = Clothes.findById(Clothes.class, clothId);

        String data[] = null;
        switch (cloth.getType()){
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

        details.setText(getResources().getStringArray(R.array.typeList)[cloth.getType()]
                + " / " + data[cloth.getSubType()]
                + " / " + getResources().getStringArray(R.array.materialList)[cloth.getMaterial()]
                + " / " + getResources().getStringArray(R.array.colorList)[cloth.getColour()]);

        byte[] decodedString = Base64.decode(cloth.getPhotoId(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        image.setImageBitmap(decodedByte);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cloth.delete();
                finish();
            }
        });

        setResult(RESULT_OK);
    }
}