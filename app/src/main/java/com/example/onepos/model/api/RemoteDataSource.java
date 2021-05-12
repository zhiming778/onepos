package com.example.onepos.model.api;

import android.content.ContentValues;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.onepos.BuildConfig;
import com.example.onepos.model.Address;
import com.example.onepos.model.DataSource;
import com.example.onepos.util.MLog;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;


public class RemoteDataSource implements DataSource {
    private final String UNIT_MILE = "mi";

    @Inject
    public RemoteDataSource() {
    }

    @Override
    public ContentValues getRoute(String startingPoint, String destination) throws IOException{
        ContentValues values = PosRetrofit.buildService()
                .getRoute(startingPoint, destination, 2, UNIT_MILE, BuildConfig.BING_MAP_KEY)
                .execute()
                .body();
        if (values == null)
            values = new ContentValues();
        return values;
    }

    @Override
    public Bitmap getMapImage(Resources res, String startingPoint, String destination)  throws IOException{
        final ResponseBody responseBody = PosRetrofit.buildService()
                .getMapImage(startingPoint, destination, BuildConfig.BING_MAP_KEY)
                .execute()
                .body();
        if (responseBody==null)
            return BitmapFactory.decodeResource(res, android.R.drawable.ic_delete);
        else {
            InputStream is = responseBody.byteStream();
            return BitmapFactory.decodeStream(is);
        }
    }

    @Override
    public List<Address> getSuggestAddresses(String query, String coordinates)  throws IOException{
        return PosRetrofit.buildService()
                .getSuggestAddresses(query, coordinates, BuildConfig.BING_MAP_KEY).execute().body();
    }
}
