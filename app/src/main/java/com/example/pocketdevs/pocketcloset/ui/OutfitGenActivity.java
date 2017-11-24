package com.example.pocketdevs.pocketcloset.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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

public class OutfitGenActivity extends AppCompatActivity {

    private EditText locationText;
    private Button locationBtn;
    private TextView locationName;
    private ImageButton topWear, bottomWear, footWear, outerWear;

    private Temperature temp;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit_gen);

        locationBtn = findViewById(R.id.location_btn);
        locationName = findViewById(R.id.location_name);
        locationText = findViewById(R.id.location_txt);

        topWear = findViewById(R.id.topWear);
        topWear.setEnabled(false);
        bottomWear = findViewById(R.id.bottomWear);
        bottomWear.setEnabled(false);
        footWear = findViewById(R.id.footWear);
        footWear.setEnabled(false);
        outerWear = findViewById(R.id.outerwear);
        outerWear.setEnabled(false);

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

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disposables.add(Observable.defer(new Callable<ObservableSource<String>>() {
                            @Override
                            public ObservableSource<String> call() throws Exception {
                                String url = Constants.WEATHER_API_URL + "?appid=" + Constants.WEATHER_API_APP_ID + "&units=metric" + "&q=" + locationText.getText().toString().trim();

                                final HttpBackend.Request request =
                                        new OKHttpBackend().prepareRequest(App.get(),
                                                new HttpBackend.RequestDetails(
                                                        Utility.uriFromString(url), null));

                                request.executeInThisThread(new HttpBackend.Listener() {
                                    @Override
                                    public void onError(HttpBackend.RequestFailureType failureType, Throwable exception, Integer httpStatus) {
                                        Log.e("onError", "Error getting data from server! " + httpStatus);
                                    }

                                    @Override
                                    public void onSuccess(String mimetype, Long bodyBytes, InputStream body) {
                                        if (body != null) {
                                            try {
                                                final JsonValue jsonValue = new JsonValue(body);
                                                jsonValue.buildInThisThread();

                                                JsonBufferedObject root = jsonValue.asObject().getObject("main");
                                                temp = new Temperature();
                                                temp.setTemp(Math.round(Float.parseFloat(root.getString("temp"))));
                                                temp.setCity(jsonValue.asObject().getString("name"));

                                            }catch (Exception ignored){
                                                //do nothing
                                            }finally {
                                                try {
                                                    if(body!=null)
                                                        body.close();
                                                } catch (IOException ignored) { }
                                            }
                                        }
                                    }
                                });

                                return Observable.just("");
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(new DisposableObserver<String>() {
                                    @Override public void onComplete() {
                                        if(temp!=null) {
                                            locationName.setText("The current temperature in " + temp.getCity() + " is " + temp.getTemp() + " C");
                                            topWear.setEnabled(true);
                                            bottomWear.setEnabled(true);
                                            footWear.setEnabled(true);
                                            outerWear.setEnabled(true);
                                        }
                                    }

                                    @Override public void onError(Throwable e) {}
                                    @Override public void onNext(String string) {}
                                })
                );
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
        Intent n = new Intent(OutfitGenActivity.this, ShowOutfitsActivity.class);
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
