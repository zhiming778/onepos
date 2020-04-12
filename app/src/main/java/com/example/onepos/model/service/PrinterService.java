package com.example.onepos.model.service;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.example.onepos.R;
import com.example.onepos.model.OrderItem;
import com.example.onepos.model.OrderMenuItem;
import com.example.onepos.model.OrderModifierItem;
import com.example.onepos.model.Receipt;
import com.example.onepos.model.Staff;
import com.example.onepos.model.service.print.BluetoothPrinter;
import com.example.onepos.model.service.print.Printer;
import com.example.onepos.util.MLog;
import com.example.onepos.view.activity.OrderActivity;

import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class PrinterService {

    private static volatile PrinterService service;
    private Application application;
    private Printer printer;

    public static PrinterService getInstance(Application application) {
        synchronized (PrinterService.class) {
            if (service == null) {
                service = new PrinterService(application);
            }
        }
        return service;
    }

    private PrinterService(Application application) {
        this.application = application;
        if (!searchPrinter()) {
            close();
            //Toast.makeText(application, "No printer found.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean searchPrinter() {
        if (!searchWifiPrinter())
            return searchBluetoothPrinter();
        return true;
    }

    private boolean searchBluetoothPrinter() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (!adapter.isEnabled())
            return false;
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            if (device.getName().equals("Printer001")) { //replace with sharedPreference
                printer = new BluetoothPrinter(device);
                return true;
            }
        }
        return false;
    }

    public void printTicket(Receipt receipt, Staff staff) {
        if (printer == null||!printer.isConnected()) {
            close();
            return;
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        String name = sharedPreferences.getString(application.getString(R.string.key_restaurant_name), application.getString(R.string.def_string_value));
        String address = sharedPreferences.getString(application.getString(R.string.key_restaurant_address), application.getString(R.string.def_string_value));
        String phone = sharedPreferences.getString(application.getString(R.string.key_restaurant_phone), application.getString(R.string.def_string_value));
        printer.setCenterAlign();
        printer.setTripleCharSize();
        if (!TextUtils.isEmpty(name))
            printer.write(name);
        printer.setNormalCharSize();
        if (!TextUtils.isEmpty(address))
            printer.write(address);
        if (!TextUtils.isEmpty(phone))
            printer.write(phone);
        printer.setRightAlign();
        printer.write("Staff: "+staff.getName());
        printer.drawDoubleDivider();
        writeItems(receipt);
        printer.feedLine();
        printer.setRightAlign();
        printer.write(String.format("%-10.10s$%7.2f", "Subtotal:", receipt.getCustomerOrder().getSubtotal()));
        printer.write(String.format("%-10.10s$%7.2f", "Tax:", receipt.getCustomerOrder().getTax()));
        printer.write(String.format("%-10.10s$%7.2f", "Total:", receipt.getCustomerOrder().getTotal()));
        printer.drawSingleDivider();
        printer.setCenterAlign();
        printer.write("Thank you for your business!");
        printer.cutPaper();
        close();
    }
    private void writeItems(Receipt receipt) {
        printer.setLeftAlign();
        int size = receipt.getNumOfItems();
        for (int i = 0; i < size; i++) {
            if (receipt.get(i).getMode() == OrderItem.MODE_OLD)
                continue;
            OrderItem orderItem = receipt.get(i);
            printer.writeItem(printer.formatItem(orderItem), orderItem.getPrice());
        }
    }
    private boolean searchWifiPrinter() {
        //TODO if Wifi printer is implemented
        return false;
    }

    public void close() {
        if (printer!=null)
            printer.close();
        application = null;
        service = null;
    }
}
