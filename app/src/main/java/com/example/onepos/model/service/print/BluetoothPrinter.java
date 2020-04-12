package com.example.onepos.model.service.print;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.example.onepos.util.MLog;

import java.io.IOException;
import java.util.UUID;

public class BluetoothPrinter extends Printer {

    private BluetoothSocket socket;

    public BluetoothPrinter(BluetoothDevice device) {
        open(device);
    }

    private void open(BluetoothDevice device) {
        try {
            socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"));
            socket.connect();
            os = socket.getOutputStream();
            connected = socket.isConnected() && os != null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            super.close();
            if (socket!=null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
