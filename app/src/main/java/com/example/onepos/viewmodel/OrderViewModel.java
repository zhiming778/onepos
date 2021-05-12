package com.example.onepos.viewmodel;

import android.app.Application;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;

import androidx.lifecycle.MutableLiveData;

import com.example.onepos.R;
import com.example.onepos.model.Address;
import com.example.onepos.model.Customer;
import com.example.onepos.model.CustomerOrder;
import com.example.onepos.model.ItemTranslation;
import com.example.onepos.model.MenuItem;
import com.example.onepos.model.ModifierItem;
import com.example.onepos.model.OrderItem;
import com.example.onepos.model.Receipt;
import com.example.onepos.repo.OrderRepository;
import com.example.onepos.view.activity.OrderActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrderViewModel extends BaseViewModel<OrderRepository> {

    private int indexSelectedCategory;
    private OrderRepository repository;
    private MutableLiveData<List<MenuItem>> liveListCategory;
    private MutableLiveData<List<MenuItem>> liveSublistMenuItem;
    private MutableLiveData<List<ModifierItem>> liveListModifier;
    private MutableLiveData<List<Address>> liveListAddress;
    private CompositeDisposable disposables;
    private ContentValues routeData;
    private Bitmap mapImage;
    private Receipt receipt;
    private Receipt oldReceipt;
    private Address address;
    private MutableLiveData<Address> liveAddress;
    private List<CustomerOrder> customerOrders;
    private List<MenuItem> allMenuitems;
    private List<Address> suggestAddresses;
    private List<ModifierItem> modifierItems;
    private MutableLiveData<Customer> liveCustomer;
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
        lang = application.getResources().getConfiguration().getLocales().get(0).equals(Locale.US) ? ItemTranslation.LANG_US : ItemTranslation.LANG_CN;
        indexSelectedCategory = 0;
        indexSelectedReceipt = -1;
        isMainLevel = true;
        disposables = new CompositeDisposable();
        receipt = new Receipt(taxRate);
        oldReceipt = new Receipt(taxRate);
        liveListCategory = new MutableLiveData<>();
        liveSublistMenuItem = new MutableLiveData<>();
        liveListModifier = new MutableLiveData<>();
        liveListAddress = new MutableLiveData<>();
        liveCustomer = new MutableLiveData<>();
    }

    private void getOrderItemsByOrderId(long id) {
        Disposable disposable = repository.getOrderItemsByLangAndOrderId(lang, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list ->{
                    receipt.setList(list);
                    liveFlag.setValue(6);
                });
        disposables.add(disposable);
    }

    public void getOldReceiptByOrderId(long id) {
        Disposable disposable = repository.getOrderItemsByLangAndOrderId(lang, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list ->{
                    oldReceipt.setList(list);
                    liveFlag.setValue(6);
                });
        disposables.add(disposable);
    }
    public void getAddressById(Long id) {
        Disposable disposable = repository.getAddressById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(address ->{
                    final List<Address> list = new ArrayList<>();
                    list.add(address);
                    liveListAddress.setValue(list);
                });
        disposables.add(disposable);
    }
    public void getPrevAddressById(Long id) {
        Disposable disposable = repository.getAddressById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(address ->{
                    liveAddress.setValue(address);
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
    public void initData(long id, boolean changeLang) {
        if (changeLang)
            updateOrderItemTitles();
        else
            if (mode == OrderActivity.MODE_MODIFY_ORDER)
                getCustomerOrderById(id);
        initListCategory();
    }

    public void oldReceiptToReceipt(int pos) { //return all if input -1
        List<OrderItem> orderItems = oldReceipt.getOrderItemGroup(pos);
        for (OrderItem orderItem:orderItems)
            orderItem.setMode(OrderItem.MODE_ADDED);
        receipt.addOrderItems(orderItems);
    }

    private void initListCategory() {
        Disposable disposable = repository.getCategories(lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listCategory->{
                    liveListCategory.setValue(listCategory);
                    getAllMenuitems();
                    });
        disposables.add(disposable);
    }


    public void setIndexSelectxedCategory(int indexSelectedCategory) {
        this.indexSelectedCategory = indexSelectedCategory;
        long categoryId = liveListCategory.getValue().get(indexSelectedCategory).getId();
        getMenuItemsByCategory(categoryId);
    }

    public void getMenuItemsByCategory(long categoryId) {
        List<MenuItem> list = allMenuitems.stream()
                .filter(menuItem -> menuItem.getParentId() == categoryId)
                .collect(Collectors.toList());
        liveSublistMenuItem.setValue(list);
    }
    private void updateOrderItemTitles() {
        Disposable disposable = repository.updateOrderItemTitles(lang, receipt)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(()->liveFlag.setValue(6));
        disposables.add(disposable);

    }

    public List<ModifierItem> getModifierItemsByType(int orderType) {
        if (orderType==1)
            modifierItems = liveListModifier.getValue().stream()
                    .filter(modifierItem -> modifierItem.getType() == 0 || modifierItem.getType() == 1)
                    .collect(Collectors.toList());
        else
            modifierItems = liveListModifier.getValue().stream()
                    .filter(modifierItem -> modifierItem.getType()==(orderType))
                    .collect(Collectors.toList());
        return modifierItems;
    }

    public void getModifierItemsByCategory(long fkCategoryId) {
        Disposable disposable = repository.getModifierItemsByCategory(lang, fkCategoryId)
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

    public MutableLiveData<List<MenuItem>> getLiveSublistMenuItem() {
        return liveSublistMenuItem;
    }

    public MutableLiveData<List<ModifierItem>> getLiveListModifier() {
        return liveListModifier;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void addMenuItemToReceipt(int pos) {
        MenuItem menuItem = liveSublistMenuItem.getValue().get(pos).clone();
        menuItem.setParentId(liveListCategory.getValue().get(indexSelectedCategory).getId());
        receipt.addMenuItem(menuItem);
    }

    public void setOrderType(int orderType) {
        receipt.getCustomerOrder().setOrderType(orderType);
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
                });
        disposables.add(disposable);
    }

    public void getCustomerByPhoneNumber(String phoneNumber) {
        Disposable disposable = repository.getCustomerByPhoneNumber(phoneNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(customer -> {
                    liveCustomer.setValue(customer);
                    if (receipt.getCustomerOrder().getOrderType() == CustomerOrder.ORDER_TYPE_DELIVERY)
                        getAddressesByCustomerId();
                });
        disposables.add(disposable);
    }

    private void getAllMenuitems() {
        Disposable disposable = repository.getAllMenuitems(lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list->{
                    allMenuitems = list;
                    setIndexSelectxedCategory(indexSelectedCategory);
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

    public void getMapData(String destination) {
        String startingPoint = PreferenceManager
                .getDefaultSharedPreferences(getApplication())
                .getString(getApplication().getString(R.string.key_restaurant_address), "4610 nicollet ave s");
        getMapImage(startingPoint, destination);
        getRoute(startingPoint, destination);
    }
    public void setAddress(String streetAddress, String aptRoom, int zipcode, String city, String instruction, double deliveryCharge) {
        if ((!streetAddress.equals(""))) {
            if (address == null)
                address = new Address(streetAddress, aptRoom, zipcode, city, instruction, deliveryCharge);
            else {
                address.setStreetAddress(streetAddress);
                address.setAptRoom(aptRoom);
                address.setCity(city);
                address.setZipcode(zipcode);
                address.setDeliveryInstruction(instruction);
            }
            List<Address> listAddress = liveListAddress.getValue();
            if (listAddress != null&&!listAddress.contains(address)) {
                address.setId(null);
            }
        } else
            address = null;
    }

    public void getCustomerOrderByPhoneNumber(String phoneNumber) {
        Disposable disposable = repository.getCustomerOrderByPhoneNumber(phoneNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list->{
                    customerOrders = list;
                    liveFlag.setValue(9);
                });
        disposables.add(disposable);
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setCustomer(String phoneNum, String name) {
        if (!phoneNum.equals("") || !name.equals(""))
            if (liveCustomer.getValue() == null) {
                Customer customer = new Customer(phoneNum, name);
                liveCustomer.setValue(customer);
            } else {
                liveCustomer.getValue().setPhoneNumber(phoneNum);
                liveCustomer.getValue().setName(name);
            }
    }

    public void printTicket() {

        Disposable disposable = repository.printTicket(receipt, staff)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        disposables.add(disposable);
    }

    public void resetLiveListAddress() {
        if (address!=null&&!address.isEmpty()) {
            List<Address> list = new ArrayList<>();
            list.add(address);
            liveListAddress.setValue(list);
        }
    }
    public void saveOrder() {
        final CustomerOrder customerOrder = receipt.getCustomerOrder();
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
    private void getRoute(String startingPoint, String destination) {
        Disposable disposable = repository.getRoute(startingPoint, destination)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contentValues -> {
                    routeData = contentValues;
                    if (mapImage != null) {
                        liveFlag.setValue(7);
                    }
                });
        disposables.add(disposable);
    }
    private void getMapImage(String startingPoint, String destination) {
        Disposable disposable = repository.getMapImage(startingPoint, destination)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> {
                    mapImage = bitmap;
                    if (routeData!=null)
                        liveFlag.setValue(7);
                });
        disposables.add(disposable);
    }
    public void getListSuggestAddresses(String query) {
        Disposable disposable = repository.getSuggestAddresses(query, "44.928198,-93.278491,20")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    suggestAddresses = list;
                    liveFlag.setValue(8);
                });
        disposables.add(disposable);
    }

    public List<Address> getSuggestAddresses() {
        return suggestAddresses;
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
        return receipt.getCustomerOrder().getOrderType();
    }

    public MutableLiveData<List<Address>> getLiveListAddress() {
        return liveListAddress;
    }

    public MutableLiveData<Address> getLiveAddress() {
        if (liveAddress==null||!liveAddress.hasObservers())
            liveAddress = new MutableLiveData<>();
        return liveAddress;
    }

    public int getIndexSelectedReceipt() {
        return indexSelectedReceipt;
    }

    public void setIndexSelectedReceipt(int indexSelectedReceipt) {
        this.indexSelectedReceipt = indexSelectedReceipt;
    }

    public List<CustomerOrder> getCustomerOrders() {
        return customerOrders;
    }

    public Receipt getOldReceipt() {
        return oldReceipt;
    }

    public void setOldReceipt(Receipt oldReceipt) {
        this.oldReceipt = oldReceipt;
    }

    public ContentValues getRouteData() {
        return routeData;
    }

    public Bitmap getMapImage() {
        return mapImage;
    }

    public boolean isMainLevel() {
        return isMainLevel;
    }

    public void setMainLevel(boolean mainLevel) {
        isMainLevel = mainLevel;
    }

    public List<ModifierItem> getModifierItems() {
        return modifierItems;
    }

    public void clearMapData() {
        routeData.clear();
        routeData = null;
        mapImage.recycle();
        mapImage = null;
    }
    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
