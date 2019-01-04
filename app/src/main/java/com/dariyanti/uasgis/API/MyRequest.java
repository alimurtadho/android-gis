package com.dariyanti.uasgis.API;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.request.MultiPartRequest;
import com.android.volley.toolbox.ImageCache;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.util.Map;

public class MyRequest {

    private static MyRequest mInstance;
    private static Context mContext;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private MyRequest(Context context){
        mContext = context;
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue, new ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }

            @Override
            public void invalidateBitmap(String url) {

            }

            @Override
            public void clear() {

            }
        });
    }

    public static synchronized MyRequest getInstance(Context context){
        if (mInstance == null){
            mInstance = new MyRequest(context);
        }

        return mInstance;
    }

    private RequestQueue getRequestQueue(){
        if (mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }

    public ImageLoader getImageLoader(){
        return mImageLoader;
    }

    static public String getDebugReqString(String url, MultipartJSONRequest request){
        String debugString = url + "/";
        int i = 0;
        for (Map.Entry<String, MultiPartRequest.MultiPartParam> entry : request.getMultipartParams().entrySet()){
            String separator = "&";
            if (i == 0){
                separator = "?";
            }
            debugString += separator + entry.getKey() + "=" + entry.getValue().value;
            i++;
        }
        return debugString;
    }

}
