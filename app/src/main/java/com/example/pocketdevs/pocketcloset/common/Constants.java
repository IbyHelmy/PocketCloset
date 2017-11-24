package com.example.pocketdevs.pocketcloset.common;

import android.Manifest;

import java.util.regex.Pattern;

/**
 * Constants used by the app
 */

public class Constants {
    public static final Pattern PATTERN = Pattern.compile("^(https?)://([^/]+)/+([^\\?#]+)((?:\\?[^#]+)?)((?:#.+)?)$");

    //Weather API URL
    public static final String WEATHER_API_URL = "http://api.openweathermap.org/data/2.5/weather";

    //Weather API ID
    public static final String WEATHER_API_APP_ID = "72a85e0c4e6076d0ad8979d58c857ab2";

    //Location
    public static final int LOCATION_PERMISSION_RESULT_CODE = 100;
    public static final int STORAGE_PERMISSION_RESULT_CODE = 200;
    public static final String[] PERMISSIONS_LOCATIONS    = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    //public static final int TYPE_BOTTOMWEAR = 2;

    //Head
    public static final int SUB_TYPE_BEANIE = 4;
    //Tops
    public static final int SUB_TYPE_DRESSSHIRT = 2;
    public static final int SUB_TYPE_BLOUSE = 3;
    public static final int SUB_TYPE_LONGSLEEVE = 4;
    public static final int SUB_TYPE_SWEATER = 5;
    public static final int SUB_TYPE_HOODIE = 6;
    public static final int SUB_TYPE_TURTLENECK = 7;
    //Bottoms
    public static final int SUB_TYPE_SHORTS = 5;
    //Footwear
    public static final int SUB_TYPE_FLIPFLOP = 4;
    public static final int SUB_TYPE_SANDALS = 5;
    public static final int SUB_TYPE_SLIPPERS = 6;
    //Outerwear
    //The following 3 are ok for summer, the last one is not ok for winter
    public static final int SUB_TYPE_RAINCOAT = 2;
    public static final int SUB_TYPE_LIGHTJACKET = 4;
    public static final int SUB_TYPE_BLAZER = 5;



    public static final int MATERIAL_COTTON = 0;
    public static final int MATERIAL_FUR = 2;
    public static final int MATERIAL_WOOL = 3;
    public static final int MATERIAL_SILK = 4;
    public static final int MATERIAL_SPANDEX = 5;
    public static final int MATERIAL_POLYESTER = 7;
}
