package com.example.pocketdevs.pocketcloset.common;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import java.net.URI;
import java.util.regex.Matcher;

/**
 * Helper functions
 */

public class Utility {

    /**
     *
     * @param key
     * @return
     */
    public static boolean isNull(String key) {
        if(key==null || key.equalsIgnoreCase(""))
            return true;

        return false;
    }

    /**
     *
     * @param context
     */
    public static void requestLocationPermission(final Activity context) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(context.findViewById(android.R.id.content),
                    "Location access is required for app to work properly.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(context, Constants.PERMISSIONS_LOCATIONS, Constants.LOCATION_PERMISSION_RESULT_CODE);
                }
            }).show();

        } else {
            ActivityCompat.requestPermissions(context, Constants.PERMISSIONS_LOCATIONS, Constants.LOCATION_PERMISSION_RESULT_CODE);
        }
    }

    /**
     * Returns true if the Activity has access to all given permissions.
     * Always returns true on platforms below M.
     *
     * @see Activity#checkSelfPermission(String)
     */
    public static boolean hasSelfPermission(Activity activity, String[] permissions) {
        // Below Android M all permissions are granted at install time and are already available.
        if (!isMNC()) {
            return true;
        }

        // Verify that all required permissions have been granted
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @return
     */
    public static boolean isMNC() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * @param url
     * @return
     */
    public static URI uriFromString(String url) {
        try {
            return new URI(url);

        } catch (Throwable t1) {
            try {
                final Matcher urlMatcher = Constants.PATTERN.matcher(url);
                if (urlMatcher.find()) {
                    final String scheme = urlMatcher.group(1);
                    final String authority = urlMatcher.group(2);
                    final String path = urlMatcher.group(3).length() == 0 ? null : '/' + urlMatcher.group(3);
                    final String query = urlMatcher.group(4).length() == 0 ? null : urlMatcher.group(4);
                    final String fragment = urlMatcher.group(5).length() == 0 ? null : urlMatcher.group(5);

                    try {
                        return new URI(scheme, authority, path, query, fragment);
                    } catch (Throwable t3) {

                        if (path != null && path.contains(" ")) {
                            return new URI(scheme, authority, path.replace(" ", "%20"), query, fragment);
                        } else {
                            return null;
                        }
                    }

                } else {
                    return null;
                }

            } catch (Throwable t2) {
                return null;
            }
        }
    }
}
