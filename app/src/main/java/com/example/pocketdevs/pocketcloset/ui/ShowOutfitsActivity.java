package com.example.pocketdevs.pocketcloset.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pocketdevs.pocketcloset.R;
import com.example.pocketdevs.pocketcloset.adapter.OutfitAdapter;
import com.example.pocketdevs.pocketcloset.common.Constants;
import com.example.pocketdevs.pocketcloset.common.HttpBackend;
import com.example.pocketdevs.pocketcloset.common.ListClickInterface;
import com.example.pocketdevs.pocketcloset.common.OKHttpBackend;
import com.example.pocketdevs.pocketcloset.common.Utility;
import com.example.pocketdevs.pocketcloset.entity.Clothes;
import com.example.pocketdevs.pocketcloset.json.JsonBufferedObject;
import com.example.pocketdevs.pocketcloset.json.JsonValue;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ShowOutfitsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView emptyView;
    private TextView locationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_outfits);

        //get values from previous activity
        int type = getIntent().getExtras().getInt("type");
        int temp = getIntent().getExtras().getInt("temp");
        String nameOfLocation = getIntent().getExtras().getString("nameOfLocation");

        //Filter data based on temperature
        final List<Clothes> oList = Clothes.find(Clothes.class, "type = ?", String.valueOf(type));
        List<Clothes> tempList = oList;
        for(int i=0; i<tempList.size();i++){
            Clothes clothes = tempList.get(i);
            if(temp > 0) {
                if(!isGoodForSummer(clothes.getSubType()) || !isSummerMaterial(clothes.getSubType())){
                    oList.remove(i);
                }
            }else{
                if(!isGoodForWinter(clothes.getSubType(), clothes.getMaterial())){
                    oList.remove(i);
                }
            }
        }

        //screen title
        String[] data = getResources().getStringArray(R.array.typeList);
        getSupportActionBar().setTitle(data[type]);

        emptyView = findViewById(R.id.emptyView);
        locationName = findViewById(R.id.location_name);
        locationName.setText(nameOfLocation);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new OutfitAdapter(this, type, oList, new ListClickInterface() {
            @Override
            public void onItemClick(View view, int position) {
                Intent n = new Intent();
                n.putExtra("data", oList.get(position).getPhotoId());
                setResult(RESULT_OK, n);
                finish();
            }
        }));

        //if list is empty show empty message
        if(oList==null || oList.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Check the material
     * @param subType
     * @return
     */
    public boolean isGoodForSummer(int subType){
        if(subType == Constants.SUB_TYPE_RAINCOAT ||subType == Constants.SUB_TYPE_LIGHTJACKET || subType == Constants.SUB_TYPE_BLAZER){
            return true;
        }else if(subType == Constants.SUB_TYPE_BEANIE){
            return false;
        }else if(subType == Constants.SUB_TYPE_LONGSLEEVE || subType == Constants.SUB_TYPE_SWEATER || subType == Constants.SUB_TYPE_HOODIE || subType == Constants.SUB_TYPE_TURTLENECK){
            return false;
        }

        return false;
    }

    /**
     * Check the material
     * @param subType
     * @param material
     * @return
     */
    public boolean isGoodForWinter(int subType, int material){
        if(material != Constants.MATERIAL_SPANDEX) {
            if (subType == Constants.SUB_TYPE_BLAZER) { //make these a huge if?
                return false;
            } else if (subType == Constants.SUB_TYPE_DRESSSHIRT || subType == Constants.SUB_TYPE_BLOUSE) {
                return false;
            } else if (subType == Constants.SUB_TYPE_SHORTS) {
                return false;
            } else if (subType == Constants.SUB_TYPE_FLIPFLOP || subType == Constants.SUB_TYPE_SANDALS || subType == Constants.SUB_TYPE_SLIPPERS) {
                return false;
            }
               return true;
        }
        return false;
    }


    /**
     * Check if item has a summer material
     * @param material
     * @return
     */
    public boolean isSummerMaterial(int material){
        if(material==Constants.MATERIAL_COTTON){
            return true;
        }else if(material==Constants.MATERIAL_SILK){
            return true;
        }else if(material==Constants.MATERIAL_SPANDEX){
            return true;
        }else if(material==Constants.MATERIAL_POLYESTER){
            return true;
        }
        return false;
    }
}