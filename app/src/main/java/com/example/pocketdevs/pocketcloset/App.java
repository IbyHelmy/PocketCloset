package com.example.pocketdevs.pocketcloset;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.orm.SugarContext;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * App instance.
 *
 */
public class App extends Application {

    private static Context appContext;
    private static OkHttpClient mClient;
    private static Picasso mPicasso;


    @Override
    public void onCreate() {
        super.onCreate();

        appContext = this;

        SetupOkHttpClient();
        setupPicasso(this, mClient);
        SugarContext.init(this);
    }

    public static synchronized Context get() {
        return appContext;
    }

    /**
     *
     * @return
     */
    private synchronized static void SetupOkHttpClient() {
        if(mClient==null) {
            mClient = new OkHttpClient();
            mClient.setFollowRedirects(true);
            mClient.setFollowSslRedirects(true);
            mClient.setConnectTimeout(20, TimeUnit.SECONDS);
            mClient.setReadTimeout(20, TimeUnit.SECONDS);
        }
    }

    /**
     *
     * @param context
     * @param okHttpClient
     * @return
     */
    private static synchronized void setupPicasso(Context context, OkHttpClient okHttpClient){
        if(mPicasso==null){
            mPicasso = new Picasso.Builder(context).downloader(new OkHttpDownloader(okHttpClient)).build();
        }
    }

    /**
     *
     * @param isCache
     * @return
     */
    public static synchronized OkHttpClient getOkHttpClientInstance(boolean isCache){
        try {
            if (isCache && (ContextCompat.checkSelfPermission(appContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)) {
                mClient.networkInterceptors().add(NETWORK_INTERCEPTOR);
                mClient.setCache(getCache());
            }

        } catch (Exception e) {
            Log.e("OkHttpClient cache", "Error with cache");
        }

        return mClient;
    }

    /**
     *
     * @return
     */
    public static synchronized Picasso getPicassoInstance(){
        return mPicasso;
    }

    /**
     *
     * @return
     */
    private static Cache getCache(){
        File httpCacheDir;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            httpCacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"PocketCloset");
        else {
            httpCacheDir = new File(App.get().getCacheDir(), "PocketCloset");
        }

        if(!httpCacheDir.exists()){
            httpCacheDir.mkdirs();
        }

        return new Cache(httpCacheDir, 30 * 1024 * 1024);
    }

    /**
     *
     */
    private static Interceptor NETWORK_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response.Builder request = chain.proceed(chain.request()).newBuilder();
            request.addHeader("Accept", "application/json");
            request.addHeader("Cache-Control", "public, max-age=" + 60 * 60);

            return request.build();
        }
    };

    /**
     *
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}