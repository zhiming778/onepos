package com.example.onepos;

import android.content.Context;
import android.database.Cursor;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.onepos.model.Address;
import com.example.onepos.model.Customer;
import com.example.onepos.model.CustomerOrder;
import com.example.onepos.model.MenuItem;
import com.example.onepos.model.ModifierItem;
import com.example.onepos.model.OrderMenuItem;
import com.example.onepos.model.Staff;
import com.example.onepos.model.local.AddressDAO;
import com.example.onepos.model.local.CustomerDAO;
import com.example.onepos.model.local.CustomerOrderDAO;
import com.example.onepos.model.local.MenuItemDAO;
import com.example.onepos.model.local.ModifierItemDAO;
import com.example.onepos.model.local.OrderMenuItemDAO;
import com.example.onepos.model.local.OrderModifierItemDAO;
import com.example.onepos.model.local.PosDatabase;
import com.example.onepos.model.local.StaffDAO;
import com.example.onepos.util.DateUtil;
import com.example.onepos.util.MLog;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PosDatabaseTest {

    private PosDatabase posDatabase;
    private AddressDAO addressDAO;
    private CustomerDAO customerDAO;
    private CustomerOrderDAO customerOrderDAO;
    private MenuItemDAO menuItemDAO;
    private ModifierItemDAO modifierItemDAO;
    private OrderMenuItemDAO orderMenuItemDAO;
    private OrderModifierItemDAO orderModifierItemDAO;
    private StaffDAO staffDAO;
    private final String phoneNum1 = "1234567898";
    private final String phoneNum2 = "2345678987";
    private final String expectedName1 = "Warron Buffet";
    private final String expectedName2 = "Bill Gates";

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        posDatabase = PosDatabase.getInstance(context);
        addressDAO = posDatabase.addressDAO();
        customerDAO = posDatabase.customerDAO();
        customerOrderDAO = posDatabase.customerOrderDAO();
        menuItemDAO = posDatabase.menuItemDAO();
        modifierItemDAO = posDatabase.modifierItemDAO();
        orderMenuItemDAO = posDatabase.orderMenuItemDAO();
        orderModifierItemDAO = posDatabase.orderModifierItemDAO();
        staffDAO = posDatabase.staffDAO();
    }

    @After
    public void close() {
        posDatabase.close();
    }

    @Test
    public void testMenuItemDAO() {
        Cursor cursor = menuItemDAO.getMenuItemsByLangAndParentId(0, 1);
        List<MenuItem> list = MenuItem.fromCursor(cursor);
        for (MenuItem item : list) {
            MLog.d(item.toString());
        }
    }

    @Test
    public void testAddress() {
        List<Address> list = addressDAO.getAllAddresses();
        for (Address address : list) {
            MLog.d(address.getFkCustomerId()+" "+address.toString());
        }
    }
    @Test
    public void testCustomerOrder() {
        List<CustomerOrder> list = customerOrderDAO.getCustomerOrderByPhoneNumber("444-444-4444");
        MLog.d(""+list.size());
    }
}