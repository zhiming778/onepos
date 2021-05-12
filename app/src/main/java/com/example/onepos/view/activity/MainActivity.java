package com.example.onepos.view.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.example.onepos.R;
import com.example.onepos.model.Action;
import com.example.onepos.model.CustomerOrder;
import com.example.onepos.model.api.PosRetrofit;
import com.example.onepos.model.api.RemoteDataSource;
import com.example.onepos.util.MLog;
import com.example.onepos.viewmodel.MainViewModel;

import java.io.IOException;

public class MainActivity extends BaseActivity<MainViewModel> implements View.OnClickListener {

    private Button btnPickup, btnDelivery, btnWalkin, btnOffice, btnPunchIn, btnPunchOut, btnModify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);
    }

    @Override
    void initLayout() {
        setContentView(R.layout.activity_main);
        btnPickup = findViewById(R.id.btn_pickup);
        btnDelivery = findViewById(R.id.btn_delivery);
        btnWalkin = findViewById(R.id.btn_walk_in);
        btnOffice = findViewById(R.id.btn_office);
        btnPunchIn = findViewById(R.id.btn_punch_in);
        btnPunchOut = findViewById(R.id.btn_punch_out);
        btnModify = findViewById(R.id.btn_modify);
        btnPickup.setOnClickListener(this);
        btnWalkin.setOnClickListener(this);
        btnOffice.setOnClickListener(this);
        btnDelivery.setOnClickListener(this);
        btnPunchIn.setOnClickListener(this);
        btnPunchOut.setOnClickListener(this);
        btnModify.setOnClickListener(this);
    }

    @Override
    void bindObserver() {

    }

    @Override
    void removeObserver() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_pickup: {
                Intent intent = new Intent(this, OrderActivity.class);
                intent.putExtra(CustomerOrder.EXTRA_ORDER_TYPE, CustomerOrder.ORDER_TYPE_PICK_UP);
                startActivity(intent);
                break;
            }
            case R.id.btn_delivery:{
                Intent intent = new Intent(this, OrderActivity.class);
                intent.putExtra(CustomerOrder.EXTRA_ORDER_TYPE, CustomerOrder.ORDER_TYPE_DELIVERY);
                startActivity(intent);
                break;
            }
            case R.id.btn_walk_in:{
                Intent intent = new Intent(this, OrderActivity.class);
                intent.putExtra(CustomerOrder.EXTRA_ORDER_TYPE, CustomerOrder.ORDER_TYPE_WALK_IN);
                startActivity(intent);
                break;
            }
            case R.id.btn_office:{
                showPasswordDialog(MainViewModel.class, Action.SETTING, (flag)->{
                    changeLanguage();
                    Intent intent = new Intent(this, OfficeActivity.class);
                    intent.putExtra(EXTRA_STAFF, viewModel.getStaff());
                    startActivity(intent);
                });
                break;
            }
            case R.id.btn_punch_in:
                showPasswordDialog(MainViewModel.class, Action.PUNCH_IN, null);
                break;
            case R.id.btn_punch_out:{
                showPasswordDialog(MainViewModel.class, Action.PUNCH_OUT, null);
                break;
            }
            case R.id.btn_modify:{
                startActivity(new Intent(this, ModifyActivity.class));
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown button id.");
        }
    }

}
