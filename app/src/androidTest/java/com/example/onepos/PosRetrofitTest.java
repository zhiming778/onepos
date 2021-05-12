package com.example.onepos;

import android.content.ContentValues;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.runner.AndroidJUnitRunner;

import com.example.onepos.model.api.RemoteDataSource;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class PosRetrofitTest {

    @Test
    public void testRouteDeserializer() {
        try {
            ContentValues contentValues = new RemoteDataSource().getRoute("1533 snelling ave n", "4610 nicollet ave s");
            //assertEquals(null, contentValues.get("statusDescript"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
