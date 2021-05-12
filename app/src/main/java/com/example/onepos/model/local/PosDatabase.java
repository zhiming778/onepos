package com.example.onepos.model.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.onepos.model.Address;
import com.example.onepos.model.Customer;
import com.example.onepos.model.CustomerOrder;
import com.example.onepos.model.ItemTranslation;
import com.example.onepos.model.MenuItem;
import com.example.onepos.model.MenuItemTranslation;
import com.example.onepos.model.ModifierItem;
import com.example.onepos.model.ModifierItemTranslation;
import com.example.onepos.model.OrderMenuItem;
import com.example.onepos.model.OrderModifierItem;
import com.example.onepos.model.Staff;
import com.example.onepos.model.TimeClock;

@Database(entities = {Address.class,
        Customer.class,
        CustomerOrder.class,
        MenuItem.class,
        ModifierItem.class,
        OrderMenuItem.class,
        OrderModifierItem.class,
        TimeClock.class,
        MenuItemTranslation.class,
        ModifierItemTranslation.class,
        Staff.class},
        version = 1,
        exportSchema = true)

public abstract class PosDatabase extends RoomDatabase {
    public abstract AddressDAO addressDAO();

    public abstract CustomerDAO customerDAO();

    public abstract CustomerOrderDAO customerOrderDAO();

    public abstract MenuItemDAO menuItemDAO();

    public abstract ModifierItemDAO modifierItemDAO();

    public abstract OrderMenuItemDAO orderMenuItemDAO();

    public abstract OrderModifierItemDAO orderModifierItemDAO();

    public abstract StaffDAO staffDAO();

    public abstract ItemTranslationDAO itemTranslationDAO();

    public abstract TimeClockDAO timeClockDAO();

    private static volatile PosDatabase posDatabase;

    private static final String DATABASE_NAME = "PosDatabase";

    private static final String PREPACK_DATABASE_NAME = "db_onepos_prepack.db";

    public static void clearInstance() {
        if (posDatabase != null && posDatabase.isOpen()) {
            posDatabase.close();
            posDatabase = null;
        }
    }

    public static PosDatabase getInstance(final Context context) {
        if (posDatabase == null || !posDatabase.isOpen()) {
            synchronized (PosDatabase.class) {
                if (posDatabase == null || !posDatabase.isOpen()) {
                    posDatabase = Room.databaseBuilder(context, PosDatabase.class, DATABASE_NAME)
                            .createFromAsset(PREPACK_DATABASE_NAME)
                            .build();
                }
            }

        }
        return posDatabase;
    }
}
