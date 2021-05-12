package com.example.onepos.model;

public class PosContract {


    class ItemEntry{
        static final String COLUMN_ID = "id";
        static final String COLUMN_PRICE= "price";
        static final String COLUMN_HAS_DESCENDANT = "has_descendant";
        static final String COLUMN_TYPE = "type";
        static final String COLUMN_FK_CATEGORY_ID = "FK_category_id";
        static final String COLUMN_PARENT_ID = "parent_id";
    }

    class ItemTranslationEntry {
        static final String COLUMN_ID = "id";
        static final String COLUMN_FK_ITEM_ID = "FK_item_id";
        static final String COLUMN_LANG = "lang";
        static final String COLUMN_TITLE = "title";

    }

    class OrderItemEntry {
        static final String COLUMN_ID = "id";
        static final String COLUMN_QUANTITY = "quantity";
        static final String COLUMN_DISCOUNT = "discount";
        static final String COLUMN_FK_CUSTOMER_ORDER_ID = "FK_customer_order_id";
        static final String COLUMN_FK_ITEM_ID = "FK_item_id";
        static final String COLUMN_COOK_INSTRUCTION = "cook_instruction";
        static final String COLUMN_FK_ORDER_MENUITEM_ID = "FK_order_menuitem_id";

    }

    class StaffEntry {
        static final String COLUMN_ID = "id";
        static final String COLUMN_TITLE= "title";
        static final String COLUMN_NAME = "name";
        static final String COLUMN_SSN = "ssn";
        static final String COLUMN_LANG = "lang";
        static final String COLUMN_PHONE_NUMBER = "phone_number";
        static final String COLUMN_PASSWORD = "password";
        static final String COLUMN_BIRTH = "date_of_birth";
        static final String COLUMN_ADDRESS = "address";
    }

    public class JsonLabel {
        public static final String LABEL_STATUS_DESCRIPTION = "statusDescription";
        public static final String LABEL_RESOURCE_SETS = "resourceSets";
        public static final String LABEL_RESOURCES = "resources";
        public static final String LABEL_TRAVEL_DISTANCE = "travelDistance";
        public static final String LABEL_TRAVEL_DURATION = "travelDurationTraffic";
        public static final String LABEL_TRAFFIC_CONGESTION = "trafficCongestion";
        public static final String LABEL_VALUE = "value";
        public static final String LABEL_ADDRESS = "address";
        public static final String LABEL_LOCALITY = "locality"; //city
        public static final String LABEL_ADMIN_DISTRICT = "adminDistrict"; //state no necessary so far ...
        public static final String LABEL_POSTAL_CODE = "postalCode";
        public static final String LABEL_ADDRESS_LINE = "addressLine";
        public static final String LABEL_NAME = "name";
        public static final String VALUE_STATUS_OK = "OK";
    }
}
