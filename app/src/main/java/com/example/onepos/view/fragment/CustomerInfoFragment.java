package com.example.onepos.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.onepos.R;
import com.example.onepos.model.Address;
import com.example.onepos.model.CustomerOrder;
import com.example.onepos.util.PhoneNumberTextWatcher;
import com.example.onepos.view.CustomKeyboard;
import com.example.onepos.viewmodel.OrderViewModel;

import java.util.List;

public class CustomerInfoFragment extends Fragment implements View.OnClickListener, PhoneNumberTextWatcher.OnNumberCompleteListener, View.OnFocusChangeListener {

    public static final String TAG = "customer_info_fragment";
    private OrderViewModel orderViewModel;
    private Button btnOrderType, btnStreet, btnCancel, btnConfirm;
    private EditText etPhoneNum, etName, etStreetAddress, etCity;
    private EditText etAptRm, etZipcode, etInstruction;
    private InputConnection icPhoneNumber, icName, icStreetAddress, icCity, icAptRom, icZipcode, icInstruction;
    private CustomKeyboard keyboard;
    private View rootView;
    private EditorInfo editorInfo;
    private PhoneNumberTextWatcher textWatcher;
    private String[] ORDER_TYPES;

    public static CustomerInfoFragment newInstance() {
        CustomerInfoFragment customerInfoFragment = new CustomerInfoFragment();
        //customerInfoFragment.setArguments();
        return customerInfoFragment;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        icPhoneNumber.closeConnection();
        icName.closeConnection();
        icStreetAddress.closeConnection();
        icCity.closeConnection();
        icAptRom.closeConnection();
        icZipcode.closeConnection();
        icInstruction.closeConnection();
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        orderViewModel = ViewModelProviders.of(getActivity()).get(OrderViewModel.class);
        ORDER_TYPES = getResources().getStringArray(R.array.order_types);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_customer, container, false);
        initLayout(rootView);
        return rootView;
    }

    private void initLayout(View rootView) {
        keyboard = rootView.findViewById(R.id.keyboard);
        etAptRm = rootView.findViewById(R.id.et_apt_rm);
        etCity = rootView.findViewById(R.id.et_city);
        etZipcode = rootView.findViewById(R.id.et_zipcode);
        etInstruction = rootView.findViewById(R.id.et_instruction);
        etName = rootView.findViewById(R.id.et_name);
        etPhoneNum = rootView.findViewById(R.id.et_phone_num);
        etStreetAddress = rootView.findViewById(R.id.et_street);
        etAptRm.setShowSoftInputOnFocus(false);
        etStreetAddress.setShowSoftInputOnFocus(false);
        etPhoneNum.setShowSoftInputOnFocus(false);
        etName.setShowSoftInputOnFocus(false);
        etCity.setShowSoftInputOnFocus(false);
        etZipcode.setShowSoftInputOnFocus(false);
        etInstruction.setShowSoftInputOnFocus(false);
        setFocusChangeListeners();
        editorInfo = new EditorInfo();
        icAptRom = etAptRm.onCreateInputConnection(editorInfo);
        icZipcode = etZipcode.onCreateInputConnection(editorInfo);
        icCity = etCity.onCreateInputConnection(editorInfo);
        icName = etName.onCreateInputConnection(editorInfo);
        icPhoneNumber = etPhoneNum.onCreateInputConnection(editorInfo);
        icInstruction = etInstruction.onCreateInputConnection(editorInfo);
        icStreetAddress = etStreetAddress.onCreateInputConnection(editorInfo);
        orderViewModel.getLiveCustomer().observe(this, customer -> {
            etName.setText(customer.getName());
        });
        orderViewModel.getLiveListAddress().observe(this, list->{
            loadAddresses(list);
        });
        btnCancel = rootView.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
        btnConfirm = rootView.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);
        btnOrderType = rootView.findViewById(R.id.btn_order_type);
        btnOrderType.setText(ORDER_TYPES[orderViewModel.getOrderType()]);
        btnOrderType.setOnClickListener(this);
        btnStreet = rootView.findViewById(R.id.btn_street);
        btnStreet.setOnClickListener(this);
        textWatcher = new PhoneNumberTextWatcher(this);
        etPhoneNum.addTextChangedListener(textWatcher);
    }

    private void setFocusChangeListeners() {
        etPhoneNum.setRawInputType(InputType.TYPE_CLASS_TEXT);
        etPhoneNum.setTextIsSelectable(true);
        etPhoneNum.setOnFocusChangeListener(this);
        etName.setOnFocusChangeListener(this);
        etStreetAddress.setOnFocusChangeListener(this);
        etCity.setOnFocusChangeListener(this);
        etAptRm.setOnFocusChangeListener(this);
        etZipcode.setOnFocusChangeListener(this);
        etInstruction.setOnFocusChangeListener(this);
    }
    private void loadAddresses(List<Address> list) {
        if (list.size() == 1) {
            Address address = list.get(0);
            setAddressFields(address);
        } else {
            String[] arrAddress = list.stream()
                    .map(address -> address.getStreetAddress()+" "+address.getAptRoom())
                    .toArray(String[]::new);
            Dialog dialog = new AlertDialog.Builder(getActivity())
                    .setItems(arrAddress, (dialogInterface, i) -> {
                        setAddressFields(list.get(i));
                        Address address = list.get(i).clone();
                        orderViewModel.setAddress(address);
                        dialogInterface.dismiss();
                    })
                    .setNegativeButton(R.string.btn_new_address, (dialogInterface, i) -> {
                        orderViewModel.setAddress(null);
                        clearAddressFields();
                    })
                    .setTitle(R.string.title_choose_address)
                    .create();
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            dialog.show();
            dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        }
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_street:
                orderViewModel.getAddressesByCustomerId();
                break;
            case R.id.btn_cancel:
                getActivity().onBackPressed();
                break;
            case R.id.btn_confirm:
                if (orderViewModel.getOrderType() == CustomerOrder.ORDER_TYPE_DELIVERY){
                    String zipcode = etZipcode.getText().toString();
                    orderViewModel.setAddress(etStreetAddress.getText().toString(), etAptRm.getText().toString()
                            , zipcode.equals("")?0:Integer.parseInt(zipcode), etCity.getText().toString(), etInstruction.getText().toString());
                }
                else
                    orderViewModel.setAddress(null);
                orderViewModel.setCustomer(etPhoneNum.getText().toString(), etName.getText().toString());
                loadOrderFragment();
                break;
            case R.id.btn_order_type:
                Dialog dialog = new AlertDialog.Builder(getActivity())
                        .setItems(ORDER_TYPES, (dialogInterface, pos)->{
                            orderViewModel.setOrderType(pos);
                            btnOrderType.setText(ORDER_TYPES[pos]);
                        })
                        .create();
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                dialog.show();
                dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                break;
            default:
                throw new IllegalArgumentException("The id of buttn doesn't exist.");
        }
    }
    private void loadOrderFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(OrderFragment.TAG);
        if (fragment != null && fragment.isHidden()) {
            fm.beginTransaction()
                    .remove(this)
                    .show(fragment)
                    .commit();
        }
        else
            fm.beginTransaction()
                    .replace(android.R.id.content, OrderFragment.newInstance(-1), OrderFragment.TAG)
                    .commit();
    }

    @Override
    public void onNumberCompleted(String phoneNumber) {
        orderViewModel.getCustomerByPhoneNumber(phoneNumber);
    }

    private void setAddressFields(Address address) {
        etStreetAddress.setText(address.getStreetAddress());
        etAptRm.setText(address.getAptRoom());
        etCity.setText(address.getCity());
        etInstruction.setText(address.getDeliveryInstruction());
        etZipcode.setText(String.valueOf(address.getZipcode()));
    }

    private void clearAddressFields() {
        etStreetAddress.setText(null);
        etAptRm.setText(null);
        etCity.setText(null);
        etInstruction.setText(null);
        etZipcode.setText(null);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (view instanceof EditText)
            if (hasFocus){
                int id = view.getId();
                switch (id) {
                    case R.id.et_apt_rm:
                        keyboard.setInputConnection(icAptRom);
                        break;
                    case R.id.et_phone_num:
                        keyboard.setInputConnection(icPhoneNumber);
                        break;
                    case R.id.et_city:
                        keyboard.setInputConnection(icCity);
                        break;
                    case R.id.et_zipcode:
                        keyboard.setInputConnection(icZipcode);
                        break;
                    case R.id.et_name:
                        keyboard.setInputConnection(icName);
                        break;
                    case R.id.et_instruction:
                        keyboard.setInputConnection(icInstruction);
                        break;
                    case R.id.et_street:
                        keyboard.setInputConnection(icStreetAddress);
                        break;
                    default:
                        throw new IllegalArgumentException("The EditText doesn't exist.");
                }
            }
    }


}
