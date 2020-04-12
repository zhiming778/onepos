package com.example.onepos;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.onepos.model.Address;
import com.example.onepos.model.Customer;
import com.example.onepos.model.CustomerOrder;
import com.example.onepos.model.MenuItem;
import com.example.onepos.model.ModifierItem;
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
    public void testModifier() {
        ModifierItemDAO modifierItemDAO = PosDatabase.getInstance(ApplicationProvider.getApplicationContext()).modifierItemDAO();
        List<ModifierItem> list = modifierItemDAO.getModifierItemsByType(0, 1);
        assertEquals(0, list.size());
    }

    @Test
    public void testAddress() {
        AddressDAO addressDAO = PosDatabase.getInstance(ApplicationProvider.getApplicationContext()).addressDAO();
        List<Address> list = addressDAO.getAddressesByCustomerId(Long.valueOf(2));
        assertEquals("", list.toString());
    }

    @Test
    public void testCustomer() {
        CustomerDAO customerDAO = PosDatabase.getInstance(ApplicationProvider.getApplicationContext()).customerDAO();
        Customer customer = customerDAO.getCustomerByPhoneNum("123-456-7890");
        assertEquals(null, customer);
    }

    @Test
    public void testDate() {
        String[] old = "07/08/2019".split("/");
        assertEquals("1", old[1]);
        assertEquals("1", old[2]);
    }

    @Test
    public void testPassword() {
        List<Staff> list =  PosDatabase.getInstance(ApplicationProvider.getApplicationContext()).staffDAO().getStaffByPassword("5555");
        assertEquals(null, list.size());
    }
    @Test
    public void deleteStaff() {
        staffDAO.deleteStaffById(0);
        staffDAO.deleteStaffById(1);
        staffDAO.deleteStaffById(2);
        staffDAO.deleteStaffById(3);
        staffDAO.deleteStaffById(4);
        staffDAO.deleteStaffById(5);
        staffDAO.deleteStaffById(6);
    }

    @Test
    public void testCustomerOrder() {
        String.format("%-18.18s%7.2f", "Subtotal:", 22.22);
    }

    @Test
    public void testMenuitem() {
        MenuItem menuItem = PosDatabase.getInstance(ApplicationProvider.getApplicationContext()).menuItemDAO().getMenuitem();
        assertEquals(null, menuItem.getName());
    }
}