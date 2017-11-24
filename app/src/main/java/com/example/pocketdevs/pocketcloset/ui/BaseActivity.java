package com.example.pocketdevs.pocketcloset.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.example.pocketdevs.pocketcloset.common.Constants;
import com.example.pocketdevs.pocketcloset.common.LocationUtils;
import com.example.pocketdevs.pocketcloset.common.Utility;


public class BaseActivity extends AppCompatActivity {

    protected static String cords[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ask user permission for location
        if (Utility.isMNC()) {
            if (Utility.hasSelfPermission(BaseActivity.this, Constants.PERMISSIONS_LOCATIONS)) {
                getLastLocation(this);
            } else {
                Utility.requestLocationPermission(BaseActivity.this);
            }
        } else
            getLastLocation(this);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (requestCode == Constants.LOCATION_PERMISSION_RESULT_CODE) {
			if (grantResults.length > 0) {
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
					getLastLocation(this);
			}

		}else if(requestCode == Constants.STORAGE_PERMISSION_RESULT_CODE) {
			//do nothing
		}
	}

	/**
	 * Get user current GPS location
	 */
	private void getLastLocation(final Activity context) {
		LocationUtils.getLastLocation(context, new LocationUtils.LocationListener() {
			@Override
			public void onLocationUpdate(String location) {
                cords = location.split(",");
			}
		});
	}

	protected static String[] getMyLocation(){
	    return cords;
    }
}