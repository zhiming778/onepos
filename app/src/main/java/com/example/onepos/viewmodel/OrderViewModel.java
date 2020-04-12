package com.example.onepos.viewmodel;

import android.app.Application;
import android.preference.PreferenceManager;

import androidx.lifecycle.MutableLiveData;

import com.example.onepos.R;
import com.example.onepos.model.Address;
import com.example.onepos.model.Customer;
import com.example.onepos.model.CustomerOrder;
import com.example.onepos.model.MenuItem;
import com.example.onepos.model.ModifierItem;
import com.example.onepos.model.Receipt;
import com.example.onepos.model.Staff;
import com.example.onepos.repo.OrderRepository;
import com.example.onepos.util.MLog;
import com.example.onepos.view.activity.OrderActivity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OrderViewModel extends BaseViewModel<OrderRepository> {

    private int indexSelectedCategory;
    private OrderRepository repository;
    private MutableLiveData<List<MenuItem>> liveListCategory;
    private MutableLiveData<List<MenuItem>> liveListMenuItem;
    private MutableLiveData<List<ModifierItem>> liveListModifier;
    private MutableLiveData<List<Address>> liveListAddress;
    private CompositeDisposable disposables;
    private Receipt receipt;
    private Address address;
    private MutableLiveData<Customer> liveCustomer;
    private int orderType;
    private int indexSelectedReceipt;
    private int mode;
    private boolean isMainLevel;

    @Inject
    public OrderViewModel(Application application, OrderRepository repository) {
        super(repository, application);
        double taxRate = Double.parseDouble(PreferenceManager
                .getDefaultSharedPreferences(application)
                .getString(application.getString(R.string.key_tax_rate), application.getString(R.string.def_tax_rate)));
        this.repository = repository;
        indexSelectedCategory = 0;
        indexSelectedReceipt = -1;
        isMainLevel = true;
        disposables = new CompositeDisposable();
        receipt = new Receipt(taxRate);
        liveListCategory = new MutableLiveData<>();
        liveListMenuItem = new MutableLiveData<>();
        liveListModifier = new MutableLiveData<>();
        liveListAddress = new MutableLiveData<>();
        liveCustomer = new MutableLiveData<>();
    }

    private void getOrderItemsByOrderId(long id) {
        Disposable disposable = repository.getOrderItemsByOrderId(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list ->{
                    receipt.setList(list);
                });
        disposables.add(disposable);
    }

    private void getAddressById(Long id) {
        Disposable disposable = repository.getAddressById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(address ->{
                    this.address = address;
                });
        disposables.add(disposable);
    }

    private void getCustomerById(Long id) {
        Disposable disposable = repository.getCustomerById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(customer ->{
                    liveCustomer.setValue(customer);
                });
        disposables.add(disposable);
    }
    public void initData(long id) {
        if (mode == OrderActivity.MODE_MODIFY_ORDER) {
            getCustomerOrderById(id);
        }
        else
            initListCategory();
    }
    private void initListCategory() {
        Disposable disposable = repository.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listCategory->{
                    liveListCategory.setValue(listCategory);
                    setIndexSelectedCategory(0);
                    });
        disposables.add(disposable);
    }


    public void setIndexSelectedCategory(int indexSelectedCategory) {
        this.indexSelectedCategory = indexSelectedCategory;
        long categoryId = liveListCategory.getValue().get(indexSelectedCategory).getId();
        getMenuItems(categoryId);
    }
    public void getMenuItems(long parentId) {
        Disposable disposable = repository.getMenuItems(parentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    liveListMenuItem.setValue(list);
                });
        disposables.add(disposable);
    }

    public void getModifierItems(int type, long fkCategoryId) {
        Disposable disposable = repository.getModifierItems(type, fkCategoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    liveListModifier.setValue(list);
                });
        disposables.add(disposable);
    }


    public MutableLiveData<List<MenuItem>> getLiveListCategory() {
        return liveListCategory;
    }

    public MutableLiveData<List<MenuItem>> getLiveListMenuItem() {
        return liveListMenuItem;
    }

    public MutableLiveData<List<ModifierItem>> getLiveListModifier() {
        return liveListModifier;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void addMenuItemToReceipt(int pos) {
        MenuItem menuItem = liveListMenuItem.getValue().get(pos);
        menuItem.setParentId(liveListCategory.getValue().get(indexSelectedCategory).getId());
        receipt.addMenuItem(menuItem);
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    private void getCustomerOrderById(long id) {
        Disposable disposable = repository.getCustomerOrderById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(customerOrder -> {
                    receipt.setCustomerOrder(customerOrder);
                    if (customerOrder.getFkAddressId()!=null)
                        getAddressById(customerOrder.getFkAddressId());
                    if (customerOrder.getFkCustomerId()!=null)
                        getCustomerById(customerOrder.getFkCustomerId());
                    getOrderItemsByOrderId(customerOrder.getId());
                    initListCategory();
                });
        disposables.add(disposable);
    }

    public void getCustomerByPhoneNumber(String phoneNumber) {
        Disposable disposable = repository.getCustomerByPhoneNumber(phoneNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(customer -> {
                    liveCustomer.setValue(customer);
                    if (orderType == CustomerOrder.ORDER_TYPE_DELIVERY)
                        getAddressesByCustomerId();
                });
        disposables.add(disposable);
    }

    public void getAddressesByCustomerId() {
        if (liveCustomer.getValue()==null)
            return;
        Long customerId = liveCustomer.getValue().getId();
        if (customerId == null)
            return;
        Disposable disposable = repository.getAddressesByCustomerId(customerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    liveListAddress.setValue(list);
                });
        disposables.add(disposable);
    }

    public void setAddress(String streetAddress, String aptRoom, int zipcode, String city, String instruction) {
        if ((!streetAddress.equals(""))) {
            if (address == null)
                address = new Address(streetAddress, aptRoom, zipcode, city, instruction);
            else {
                address.setStreetAddress(streetAddress);
                address.setAptRoom(aptRoom);
                address.setCity(city);
                address.setZipcode(zipcode);
                address.setDeliveryInstruction(instruction);
            }
            List<Address> listAddress = liveListAddress.getValue();
            if (listAddress != null) {
                if (!listAddress.contains(address)) {
                    address.setId(null);
                }
            }
        } else if (address != null)
            address = null;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setCustomer(String phoneNum, String name) {
        if ((!phoneNum.equals("")) || (!name.equals("")))
            if (liveCustomer.getValue() == null) {
                Customer customer = new Customer(phoneNum, name);
                liveCustomer.setValue(customer);
            } else {
                liveCustomer.getValue().setPhoneNumber(phoneNum);
                liveCustomer.getValue().setName(name);
            }
    }

    public void printTicket() {
        Disposable disposable = repository.printTicket(receipt, new Staff(1, "Jimmy", 0, null, 0, null, null, null))
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        disposables.add(disposable);
    }

    public void saveOrder() {
        CustomerOrder customerOrder = receipt.getCustomerOrder();
        customerOrder.setOrderType(orderType);
        customerOrder.setPaymentType(CustomerOrder.PAYMENT_TYPE_NOT_PAID);
        if (mode== OrderActivity.MODE_CREATE_ORDER)
            customerOrder.setDate(System.currentTimeMillis());
        customerOrder.setTotal(customerOrder.getTotal());
        Disposable disposable = repository.saveOrder(liveCustomer.getValue(), address, customerOrder, receipt, mode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        disposables.add(disposable);
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public Address getAddress() {
        return address;
    }

    public MutableLiveData<Customer> getLiveCustomer() {
        return liveCustomer;
    }

    public int getIndexSelectedCategory() {
        return indexSelectedCategory;
    }

    public int getOrderType() {
        return orderType;
    }

    public MutableLiveData<List<Address>> getLiveListAddress() {
        return liveListAddress;
    }

    public int getIndexSelectedReceipt() {
        return indexSelectedReceipt;
    }

    public void setIndexSelectedReceipt(int indexSelectedReceipt) {
        this.indexSelectedReceipt = indexSelectedReceipt;
    }

    public boolean isMainLevel() {
        return isMainLevel;
    }

    public void setMainLevel(boolean mainLevel) {
        isMainLevel = mainLevel;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        MLog.d("onCLeared");
        liveListCategory.setValue(null);
        liveListMenuItem.setValue(null);
        liveListModifier.setValue(null);
        liveCustomer.setValue(null);
        liveListAddress.setValue(null);
    }
}
