package com.example.pocketdevs.pocketcloset.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pocketdevs.pocketcloset.App;
import com.example.pocketdevs.pocketcloset.R;
import com.example.pocketdevs.pocketcloset.common.Constants;
import com.example.pocketdevs.pocketcloset.common.HttpBackend;
import com.example.pocketdevs.pocketcloset.common.OKHttpBackend;
import com.example.pocketdevs.pocketcloset.common.Utility;
import com.example.pocketdevs.pocketcloset.entity.Temperature;
import com.example.pocketdevs.pocketcloset.json.JsonBufferedObject;
import com.example.pocketdevs.pocketcloset.json.JsonValue;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class OutfitGenCurrenLocationActivity extends BaseActivity {

    private TextView locationName;
    private ImageButton topWear, bottomWear, footWear, outerWear;

    private Temperature temp;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit_gen_current);

        temp = (Temperature) getIntent().getExtras().getSerializable("temp");

        locationName = findViewById(R.id.location_name);
        locationName.setText("Your current temperature in " + temp.getCity() + " is " + temp.getTemp() + " C");

        topWear = findViewById(R.id.topWear);
        bottomWear = findViewById(R.id.bottomWear);
        footWear = findViewById(R.id.footWear);
        outerWear = findViewById(R.id.outerwear);

        topWear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivity(1, 541);
            }
        });

        bottomWear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivity(2, 542);
            }
        });

        footWear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivity(3, 543);
            }
        });

        outerWear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivity(4, 544);
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
        if (requestCode == 541 && resultCode == RESULT_OK) {
            topWear.setImageBitmap(getImage(data));

        } else if (requestCode == 542 && resultCode == RESULT_OK) {
            bottomWear.setImageBitmap(getImage(data));

        } else if (requestCode == 543 && resultCode == RESULT_OK) {
            footWear.setImageBitmap(getImage(data));

        } else if (requestCode == 544 && resultCode == RESULT_OK) {
            outerWear.setImageBitmap(getImage(data));
        }
    }

    /**
     *
     * @param type
     * @param reqCode
     */
    public void showActivity(int type,  int reqCode){
        Intent n = new Intent(OutfitGenCurrenLocationActivity.this, ShowOutfitsActivity.class);
        n.putExtra("type", type);
        n.putExtra("temp", temp.getTemp());
        n.putExtra("nameOfLocation", locationName.getText().toString());
        startActivityForResult(n, reqCode);
    }

    /**
     *
     * @param data
     * @return
     */
    public Bitmap getImage(Intent data){
        Bundle extras = data.getExtras();
        byte[] decodedString = Base64.decode(extras.getString("data"), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return decodedByte;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}
