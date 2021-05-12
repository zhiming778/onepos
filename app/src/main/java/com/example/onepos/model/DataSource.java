package com.example.onepos.model;

import android.content.ContentValues;
import android.content.res.Resources;
import android.graphics.Bitmap;

import java.io.IOException;
import java.util.List;

public interface DataSource {

    ContentValues getRoute(String startingPoint, String destination) throws IOException;

    Bitmap getMapImage(Resources res, String startingPoint, String destination) throws IOException;

    List<Address> getSuggestAddresses(String query, String coordinates) throws IOException;
}
