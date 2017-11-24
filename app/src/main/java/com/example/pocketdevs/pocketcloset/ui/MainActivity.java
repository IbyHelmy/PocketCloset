package com.example.pocketdevs.pocketcloset.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

public class MainActivity extends BaseActivity {

    private Button weatherInfo;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private Temperature temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("");

        weatherInfo = findViewById(R.id.weathInfo);
        weatherInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(MainActivity.this, OutfitGenCurrenLocationActivity.class);
                n.putExtra("temp", temp);
                startActivity(n);
            }
        });

        disposables.add(Observable.defer(new Callable<ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> call() throws Exception {
                        //build API request
                        String url = Constants.WEATHER_API_URL + "?appid=" + Constants.WEATHER_API_APP_ID + "&units=metric";//Constants.WEATHER_API_APP_ID
                        if(BaseActivity.getMyLocation()!=null) {
                            url+="&lat=" + BaseActivity.getMyLocation()[0] + "&lon="+BaseActivity.getMyLocation()[1];
                        }else{
                            url+="&q=ottawa";
                        }

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

                                        //read json results
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
                                    weatherInfo.setEnabled(true);
                                    weatherInfo.setText(temp.getTemp() + " C");
                                    getSupportActionBar().setTitle("Current temperature is " + temp.getTemp() + " C");
                                }else{
                                    weatherInfo.setEnabled(false);
                                }
                            }

                            @Override public void onError(Throwable e) {}
                            @Override public void onNext(String string) {}
                        })
        );
    }

    //This is the function to send you to the add clothing page from main
    public void addClothes(View v){
        Intent intent = new Intent(MainActivity.this, AddClothesActivity.class);
        startActivity(intent);
    }

    //This is the function to send you to the view closet page from main
    public void viewCloset(View v){
        Intent intent = new Intent(MainActivity.this, ViewClosetActivity.class);
        startActivity(intent);
    }

    //This is the function to send you to the pack my bag page from main
    public void outfitButton(View v){
        Intent intent = new Intent(MainActivity.this, OutfitGenActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}