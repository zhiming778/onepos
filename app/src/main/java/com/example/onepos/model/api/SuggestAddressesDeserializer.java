package com.example.onepos.model.api;

import com.example.onepos.model.Address;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.onepos.model.PosContract.JsonLabel.*;
public class SuggestAddressesDeserializer implements JsonDeserializer<List<Address>>{


    @Override
    public List<Address> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonRoot = json.getAsJsonObject();
        final String statusDescription = jsonRoot.get(LABEL_STATUS_DESCRIPTION).getAsString();
        if (!statusDescription.equals(VALUE_STATUS_OK))
            return null;
        final JsonArray arrValue = jsonRoot.get(LABEL_RESOURCE_SETS)
                .getAsJsonArray().get(0).getAsJsonObject().get(LABEL_RESOURCES)
                .getAsJsonArray().get(0).getAsJsonObject().get(LABEL_VALUE).getAsJsonArray();
        if (arrValue.size() == 0)
            return Collections.EMPTY_LIST;
        return getAddresses(arrValue);
    }

    private List<Address> getAddresses(final JsonArray arrValue) {
        final int size = arrValue.size();
        final List<Address> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            final JsonElement je = arrValue.get(i).getAsJsonObject().get(LABEL_NAME);
            final String name = je==null ? null : je.getAsString();
            final JsonObject jbAddress = arrValue.get(i).getAsJsonObject().get(LABEL_ADDRESS).getAsJsonObject();
            final String city = jbAddress.get(LABEL_LOCALITY).getAsString();
            final JsonElement jeZipcode = jbAddress.get(LABEL_POSTAL_CODE);
            final int zipcode = jeZipcode==null?0:Integer.parseInt(jeZipcode.getAsString());
            final String streetAddress = jbAddress.get(LABEL_ADDRESS_LINE).getAsString();
            final Address address = new Address(streetAddress, null, zipcode, city, name, 0);
            list.add(address);
        }
        return list;
    }
}
