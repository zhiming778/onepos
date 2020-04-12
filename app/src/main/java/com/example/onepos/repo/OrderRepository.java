package com.example.onepos.repo;

import android.app.Application;

import com.example.onepos.model.Address;
import com.example.onepos.model.Customer;
import com.example.onepos.model.CustomerOrder;
import com.example.onepos.model.MenuItem;
import com.example.onepos.model.ModifierItem;
import com.example.onepos.model.OrderItem;
import com.example.onepos.model.OrderMenuItem;
import com.example.onepos.model.OrderModifierItem;
import com.example.onepos.model.Receipt;
import com.example.onepos.model.Staff;
import com.example.onepos.model.local.CustomerDAO;
import com.example.onepos.model.local.CustomerOrderDAO;
import com.example.onepos.model.local.ModifierItemDAO;
import com.example.onepos.model.local.OrderMenuItemDAO;
import com.example.onepos.model.local.OrderModifierItemDAO;
import com.example.onepos.model.local.PosDatabase;
import com.example.onepos.model.service.PrinterService;
import com.example.onepos.view.activity.OrderActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class OrderRepository extends BaseRepository{

    @Inject
    public OrderRepository(Application application) {
        super(application);
    }

    public Single<List<MenuItem>> getCategories() {
        return Single.create(emitter -> {
            emitter.onSuccess(PosDatabase.getInstance(application).menuItemDAO()
                    .getMenuItems(MenuItem.CATEGORY_MENUITEM_PARENT_ID));
        });
    }

    public Single<List<MenuItem>> getMenuItems(long parentId) {
        return Single.create(emitter -> {
            emitter.onSuccess(PosDatabase.getInstance(application).menuItemDAO()
                    .getMenuItems(parentId));
        });
    }

    public Single<List<ModifierItem>> getModifierItems(int type, long fkCategoryId) {
        ModifierItemDAO modifierItemDAO = PosDatabase.getInstance(application).modifierItemDAO();
        return Single.create(emitter -> {
            List<ModifierItem> list = null;
            if (type == 0 || type == 1) {
                list = modifierItemDAO.getModifierItemsByType(0, fkCategoryId);
                list.addAll(modifierItemDAO.getModifierItemsByType(1, fkCategoryId));
            } else {
                list = modifierItemDAO.getModifierItemsByType(type, 1);
            }
            emitter.onSuccess(list);
        });
    }

    public Maybe<Customer> getCustomerByPhoneNumber(String phoneNumber) {
        return Maybe.create(emitter -> {
            Customer customer = PosDatabase
                    .getInstance(application)
                    .customerDAO()
                    .getCustomerByPhoneNum(phoneNumber);
            if (customer != null) {
                emitter.onSuccess(customer);
            }
            emitter.onComplete();
        });
    }

    public Maybe<List<Address>> getAddressesByCustomerId(Long customerId) {
        return Maybe.create(emitter -> {
            List<Address> listAddress = PosDatabase
                    .getInstance(application)
                    .addressDAO()
                    .getAddressesByCustomerId(customerId);
            if (listAddress.size() != 0) { //Or listAddress!=null
                emitter.onSuccess(listAddress);
            }
            emitter.onComplete();
        });
    }

    private Long saveCustomer(Customer customer) {
        if (customer==null||customer.isEmpty())
            return null;
        CustomerDAO customerDAO = PosDatabase.getInstance(application).customerDAO();
        if (customer.getId()==null){
            return customerDAO.insertCustomer(customer);
        }
        else{
            customerDAO.updateCustomer(customer);
            return customer.getId();
        }
    }
    private Long saveAddress(Address address, Long customerId) {
        if (address == null||address.isEmpty())
            return null;
        if (address.getId()==null){
            address.setFkCustomerId(customerId);
            return PosDatabase.getInstance(application)
                    .addressDAO()
                    .insertAddress(address);
        }
        else{
            return address.getId();
        }
    }
    private long saveCustomerOrder(CustomerOrder customerOrder) {
        CustomerOrderDAO dao = PosDatabase.getInstance(application).customerOrderDAO();
        if (customerOrder.getId() == -1) {
            customerOrder.setId(0);
            return dao.insertCustomerOrder(customerOrder);
        } else {
            dao.updateCustomerOrder(customerOrder);
            return customerOrder.getId();
        }
    }
    public Completable saveOrder(Customer customer, Address address, CustomerOrder customerOrder, Receipt receipt, int mode) {
        return Completable.create(emitter -> {
            Long customerId = saveCustomer(customer);
            customerOrder.setFkCustomerId(customerId);
            Long addressId = saveAddress(address, customerId);
            customerOrder.setFkAddressId(addressId);
            long id = saveCustomerOrder(customerOrder);
            receipt.setCustomerOrderId(id);
            if (mode == OrderActivity.MODE_MODIFY_ORDER)
                deleteOrderItems(receipt);
            saveOrderItems(receipt);
            emitter.onComplete();
        });
    }

    public Single<CustomerOrder> getCustomerOrderById(long id) {
        return Single.create(emitter -> {
            CustomerOrder customerOrder = PosDatabase.getInstance(application).customerOrderDAO()
                    .getCustomerOrderById(id);
            emitter.onSuccess(customerOrder);
        });
    }

    public Single<Customer> getCustomerById(Long id) {
        return Single.create(emitter -> {
            Customer customer = PosDatabase.getInstance(application).customerDAO()
                    .getCustomerById(id);
            emitter.onSuccess(customer);
        });
    }
    public Single<Address> getAddressById(Long id) {
        return Single.create(emitter -> {
            Address address = PosDatabase.getInstance(application).addressDAO()
                    .getAddressById(id);
            emitter.onSuccess(address);
        });
    }

    public Single<List<OrderItem>> getOrderItemsByOrderId(long id) {
        return Single.create(emitter -> {
            List<OrderMenuItem> orderMenuItems = PosDatabase.getInstance(application).orderMenuItemDAO()
                    .getOrderMenuItemById(id);
            List<OrderModifierItem> orderModifierItems = PosDatabase.getInstance(application).orderModifierItemDAO()
                    .getItemsByCustomerOrderId(id);
            emitter.onSuccess(mergeOrderItems(orderMenuItems, orderModifierItems));
        });
    }

    public Completable printTicket(Receipt receipt, Staff staff) {
        return Completable.create(emitter -> {
            PrinterService.getInstance(application).printTicket(receipt, staff);
            emitter.onComplete();
        });
    }
    private List<OrderItem> mergeOrderItems(List<OrderMenuItem> orderMenuItems, List<OrderModifierItem> orderModifierItems) {
        List<OrderItem> list = new ArrayList<>(orderMenuItems);
        int size = list.size();
        for (int i = size-1; i >=0; i--) {
            int length = orderModifierItems.size();
            long id = list.get(i).getId();
            for (int j = length-1; j >= 0; j--) {
                if (orderModifierItems.get(j).getFkOrderMenuitemId()==id)
                    list.add(i + 1, orderModifierItems.remove(j));
            }
        }
        return list;
    }
    private void deleteOrderItems(Receipt receipt) {
        OrderMenuItemDAO orderMenuItemDAO = PosDatabase.getInstance(application).orderMenuItemDAO();
        OrderModifierItemDAO orderModifierItemDAO = PosDatabase.getInstance(application).orderModifierItemDAO();
        int size = receipt.getNumOfItems();
        for (int i = size-1; i>=0; i--) {
            OrderItem item = receipt.get(i);
            if (item.getMode() == OrderItem.MODE_DELETED) {
                if (item instanceof OrderMenuItem)
                    orderMenuItemDAO.deleteOrderMenuItem(item.getId());
                else
                    orderModifierItemDAO.deleteOrderModifierItem(item.getId());
            }
        }

    }
    private void saveOrderItems(Receipt receipt) {
        long[] ids = saveOrderMenuItems(receipt.getAddedOrderMenuItems());
        receipt.setFKOrderMenuItemIds(ids);
        saveOrderModifierItems(receipt.getAddedOrderModifierItems());
    }

    private long[] saveOrderMenuItems(OrderMenuItem[] orderMenuItems) {
        return PosDatabase.getInstance(application)
                .orderMenuItemDAO()
                .insertOrderMenuItems(orderMenuItems);
    }

    private void saveOrderModifierItems(OrderModifierItem[] orderModifierItems) {
        if (orderModifierItems.length==0)
            return;
        PosDatabase.getInstance(application)
         .orderModifierItemDAO()
         .insertOrderModifierItems(orderModifierItems);
    }









    /*
    public Completable deleteAddressById(Long addressId) {
        return Completable.create(emitter -> {
            PosDatabase.getInstance(application)
                    .addressDAO()
                    .deleteAddressById(addressId);
            emitter.onComplete();
        });
    }

     */
}
