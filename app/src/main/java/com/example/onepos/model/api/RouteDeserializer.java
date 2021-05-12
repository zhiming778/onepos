package com.example.onepos.model.api;

import android.content.ContentValues;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import static com.example.onepos.model.PosContract.JsonLabel.*;
public class RouteDeserializer implements JsonDeserializer<ContentValues>{


    @Override
    public ContentValues deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonRoot = json.getAsJsonObject();
        final String statusDescription = jsonRoot.get(LABEL_STATUS_DESCRIPTION).getAsString();
        if (!statusDescription.equals(VALUE_STATUS_OK))
            return null;
        final JsonObject jbRessource = jsonRoot.get(LABEL_RESOURCE_SETS)
                .getAsJsonArray().get(0).getAsJsonObject().get(LABEL_RESOURCES)
                .getAsJsonArray().get(0).getAsJsonObject();
        String trafficCongestion = jbRessource.get(LABEL_TRAFFIC_CONGESTION).getAsString();
        double trafficDistance = jbRessource.get(LABEL_TRAVEL_DISTANCE).getAsDouble();
        int trafficDuration = (int)Math.ceil(jbRessource.get(LABEL_TRAVEL_DURATION).getAsInt()/60.0); //Round up to nearest minute
        final ContentValues values = new ContentValues(3);
        values.put(LABEL_TRAFFIC_CONGESTION, trafficCongestion);
        values.put(LABEL_TRAVEL_DISTANCE, trafficDistance);
        values.put(LABEL_TRAVEL_DURATION, trafficDuration);
        values.put(LABEL_STATUS_DESCRIPTION, VALUE_STATUS_OK);
        return values;
    }
}
