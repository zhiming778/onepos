package com.example.onepos.model.service.print;

import com.example.onepos.model.OrderItem;
import com.example.onepos.model.OrderModifierItem;
import com.example.onepos.model.Receipt;
import com.example.onepos.view.activity.OrderActivity;

import java.io.IOException;
import java.io.OutputStream;

public class Printer {

    OutputStream os;
    boolean connected;

    public void write(String s) {
        try {
            os.write(s.getBytes());
            os.write(Commands.LF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendCommand(byte[] bytes) {
        try {
            os.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (os!=null)
                os.close();
            connected = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cutPaper() {
        sendCommand(Commands.CUT_PAPER);
    }

    public void setNormalCharSize() {
        sendCommand(Commands.CHAR_SIZE_X1);
    }

    public void setDoubleCharSize() {
        sendCommand(Commands.CHAR_SIZE_X2);
    }

    public void setTripleCharSize() {
        sendCommand(Commands.CHAR_SIZE_X3);
    }

    public void set4XCharSize() {
        sendCommand(Commands.CHAR_SIZE_X4);
    }

    public void setLeftAlign() {
        sendCommand(Commands.LEFT_ALIGN);
    }

    public void setCenterAlign() {
        sendCommand(Commands.CENTER_ALIGN);
    }

    public void setRightAlign() {
        sendCommand(Commands.RIGHT_ALIGN);
    }

    public void drawSingleDivider() {
        try {
            for (int i = 0; i < Commands.NUM_OF_LINE_CHAR; i++) {
                os.write(Commands.SINGLE_DIVIDER);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String formatItem(OrderItem orderItem) {
        StringBuilder item = new StringBuilder();
        if (orderItem instanceof OrderModifierItem)
            item.append("  ");
        if (orderItem.getMode() == OrderItem.MODE_DELETED)
            item.append("(DELETED)");
        int quantity = orderItem.getQuantity();
        if (quantity > 1)
            item.append(quantity).append("X ");
        return item.append(orderItem.getItem().getTitle()).toString();
    }

    public void writeItem(String item, double price) {
        write(String.format("%-41.41s%7.2f", item, price));
    }

    public void drawDoubleDivider() {
        try {
            for (int i = 0; i < Commands.NUM_OF_LINE_CHAR; i++) {
                os.write(Commands.DOUBLE_DIVIDER);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void feedLine() {
        try {
            os.write(Commands.LF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return connected;
    }
}
