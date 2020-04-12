package com.example.onepos.model;

import com.example.onepos.util.MLog;
import com.example.onepos.util.MoneyUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//list of OrderMenuItem and OrderModifierItem
public class Receipt {

    private List<OrderItem> listOrderItems;
    private final double TAX_RATE;
    private CustomerOrder customerOrder;

    public Receipt(double taxRate) {
        listOrderItems = new LinkedList<>();
        TAX_RATE = taxRate;
        customerOrder = new CustomerOrder();
    }

    public void addMenuItem(MenuItem menuItem) {
        listOrderItems.add(new OrderMenuItem(menuItem, OrderItem.MODE_ADDED));
        recalcMoneyData();
    }

    public void quantityIncrementByPos(int pos) {
        listOrderItems.get(pos).quantityIncrement();
        recalcMoneyData();
    }

    public int quantityDecrementByPos(int pos) {
        int numDeleted = 0;
        if (listOrderItems.get(pos).getQuantity() == 1){
            if (listOrderItems.get(pos) instanceof OrderMenuItem) {
                for (int i = getLastModifierItemPosition(pos); i >= pos; i--) {
                    listOrderItems.remove(i);
                    numDeleted++;
                }
            }
            else{
                listOrderItems.remove(pos);
                numDeleted++;
            }
            recalcMoneyData();
            return numDeleted;
        }
        else{
            listOrderItems.get(pos).quantityDecrement();
            recalcMoneyData();
            return numDeleted;
        }
    }

    public void markAsDeleted(int pos) {
        int first = getFirstModifierItemPosition(pos);
        int last = getLastModifierItemPosition(pos);
        for (int i=first-1;i<=last;i++) {
            OrderItem item = listOrderItems.get(i);
            item.setMode(OrderItem.MODE_DELETED);
            item.getItem().setPrice(0);
        }
        recalcMoneyData();
    }

    private int getMenuItemPosition(int pos) {
        return getFirstModifierItemPosition(pos) - 1;
    }

    private int getModifierItemCount(int pos) {
        return getLastModifierItemPosition(pos) - getFirstModifierItemPosition(pos) + 1;
    }
    public void addModifierItem(int originalPos, ModifierItem modifierItem) {
        int length = listOrderItems.size();
        if (originalPos >= length||originalPos<0) {
            throw new IndexOutOfBoundsException("The index is out of bounds of OrderItem list");
        }
        int modifierType = modifierItem.getType();
        if (!ModifierItem.isModifierSide(modifierType)) { // If the modifier to be added isn't side ...
            int lastPos = getLastModifierItemPosition(originalPos);
            listOrderItems.add(lastPos + 1, new OrderModifierItem(modifierItem, OrderItem.MODE_ADDED));
        }
        else
        {
            int pos = getFirstModifierItemPosition(originalPos);
            boolean isInserted = false;
            int offset = Math.min(ModifierItem.NUM_OF_SIDE, getModifierItemCount(originalPos));
            for (int i = pos; i<pos+offset; i++) {
                if (((OrderModifierItem) listOrderItems.get(i)).getType() == modifierType) {
                    listOrderItems.set(i, new OrderModifierItem(modifierItem, OrderItem.MODE_ADDED));
                    isInserted = true;
                }
            }
            if (!isInserted) {
                listOrderItems.add(pos, new OrderModifierItem(modifierItem, OrderItem.MODE_ADDED));
            }
        }
        recalcMoneyData();
    }
    /*public void addModifierItem(int originalPos, ModifierItem modifierItem) {
        int length = listOrderItems.size();
        if (originalPos >= length||originalPos<0) {
            throw new IndexOutOfBoundsException("The index is out of bounds of OrderItem list");
        }
        int pos = getFirstModifierItemPosition(originalPos);
        if (pos == length||listOrderItems.get(pos) instanceof OrderMenuItem) { // There is not modifier for this menuItem
            listOrderItems.add(pos, new OrderModifierItem(modifierItem, OrderItem.MODE_ADDED));
        }
        else{
            int modifierType = modifierItem.getType();
            if (ModifierItem.isModifierSide(modifierType)) { // If the modifier to be added is side ...
                boolean isInserted = false;
                int offset = Math.min(ModifierItem.NUM_OF_SIDE, getModifierItemCount(originalPos));
                for (int i = pos; i<pos+offset; i++) {
                    if (((OrderModifierItem) listOrderItems.get(i)).getType() == modifierType) {
                        listOrderItems.set(i, new OrderModifierItem(modifierItem, OrderItem.MODE_ADDED));
                        isInserted = true;
                    }
                }
                if (!isInserted) {
                    listOrderItems.add(pos, new OrderModifierItem(modifierItem, OrderItem.MODE_ADDED));
                }
            }
            else
            {
                int lastPos = getLastModifierItemPosition(originalPos);
                listOrderItems.add(lastPos + 1, new OrderModifierItem(modifierItem, OrderItem.MODE_ADDED));
            }
        }
        recalcMoneyData();
    }*/

    public long getItemCategoryId(int originalPos) {
        Item item = listOrderItems.get(originalPos).getItem();
        return (item instanceof MenuItem)?((MenuItem) item).getParentId():((ModifierItem)item).getFkCategoryId();
    }
    //always returns the index next to the menuitem even its null
    private int getFirstModifierItemPosition(int originalPos) {
        for (int i = originalPos; i >= 0; i--) {
            if (listOrderItems.get(i) instanceof OrderMenuItem)
                return i+1;
        }
        return 0;
    }
    //return the index of last modifier item.
    //if the menuitem has no modifier item, its own index would be returned.
    private int getLastModifierItemPosition(int originalPos) {
        for (int i = originalPos+1; i < listOrderItems.size(); i++) {
            if (listOrderItems.get(i) instanceof OrderMenuItem) {
                return i-1;
            }
        }
        return listOrderItems.size() - 1;
    }

    public int getNumOfItems() {
        return listOrderItems.size();
    }

    public OrderItem get(int pos) {
        return listOrderItems.get(pos);
    }

    public long getItemId(int pos) {
        return listOrderItems.get(pos).getId();
    }

    public void setCustomerOrderId(long id) {
        for (OrderItem orderItem:listOrderItems)
            orderItem.setFkCustomerOrderId(id);
    }
    private void recalcMoneyData() {
        double subtotal = 0;
        double menuitemSubtotal = 0;
        for (int i = listOrderItems.size()-1; i >=0; i--) {
            OrderItem orderItem = listOrderItems.get(i);
                menuitemSubtotal += orderItem.getPrice();
            if (orderItem instanceof OrderMenuItem) {
                subtotal += menuitemSubtotal;
                menuitemSubtotal = 0;
            }
        }
        subtotal -= MoneyUtil.doubleTimesDouble(subtotal, customerOrder.getDiscount());
        double tax = MoneyUtil.doubleTimesDouble(subtotal, TAX_RATE/100.00);
        double total = MoneyUtil.doublePlusDouble(subtotal, tax);
        customerOrder.setTotal(total);
        customerOrder.setSubtotal(subtotal);
        customerOrder.setTax(tax);
    }

    public OrderMenuItem[] getAddedOrderMenuItems() {
        List<OrderMenuItem> list = new ArrayList<>();
        for (OrderItem orderItem : listOrderItems)
            if (orderItem instanceof OrderMenuItem && orderItem.getMode()==OrderItem.MODE_ADDED)
                list.add((OrderMenuItem)orderItem);
        return list.toArray(new OrderMenuItem[0]);
    }
    public OrderModifierItem[] getAddedOrderModifierItems() {
        List<OrderModifierItem> list = new ArrayList<>();
        for (OrderItem orderItem : listOrderItems) {
            if (orderItem instanceof OrderModifierItem && orderItem.getMode() == OrderItem.MODE_ADDED) {
                list.add((OrderModifierItem)orderItem);
            }
        }
        return list.toArray(new OrderModifierItem[0]);
    }

    public void setFKOrderMenuItemIds(long[] ids) {
        int indexOfIds = -1;
        for (OrderItem orderItem : listOrderItems) {
            if (orderItem.getMode()!=OrderItem.MODE_ADDED)
                continue;
            if (orderItem instanceof OrderMenuItem)
                indexOfIds++;
            else
                ((OrderModifierItem)orderItem).setFkOrderMenuitemId(ids[indexOfIds]);
        }
    }


    public void setList(List<OrderItem> list) {
        listOrderItems = list;
        recalcMoneyData();
    }

    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }

    public void setDiscount(double discount) {
        customerOrder.setDiscount(discount);
        recalcMoneyData();
    }

    public void setDiscount(int pos, double discount) {
        listOrderItems.get(pos).setDiscount(discount);
        recalcMoneyData();
    }
}
