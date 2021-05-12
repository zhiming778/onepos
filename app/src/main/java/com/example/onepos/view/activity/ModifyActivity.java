package com.example.onepos.view.activity;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.onepos.R;
import com.example.onepos.model.CustomerOrder;
import com.example.onepos.util.DateUtil;
import com.example.onepos.util.MLog;
import com.example.onepos.view.adapter.ModifierAdapter;
import com.example.onepos.view.adapter.ModifyOrderAdapter;
import com.example.onepos.view.dialog.CustomDatePickerDialog;
import com.example.onepos.viewmodel.ModifyViewModel;
import com.example.onepos.viewmodel.ViewModelFactory;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class ModifyActivity extends BaseActivity<ModifyViewModel> implements ModifyOrderAdapter.OnModifyOrderListener, View.OnClickListener {

    private TabLayout tabLayout;
    private ModifyOrderAdapter adapter;
    private ImageButton btnLeft, btnRight;
    private TextView tvDate;
    public static final int RESULT_UPDATE_ORDER = 1;
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_TOTAL = "extra_total";
    public static final String EXTRA_ORDER_TYPE = "extra_order_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, factory).get(ModifyViewModel.class);
        viewModel.getLiveListCurstomerOrder().observe(this, adapter::setList);
        tabLayout.selectTab(tabLayout.getTabAt(0));
        viewModel.getCustomerOrderByDate(tabLayout.getSelectedTabPosition()-1, System.currentTimeMillis());
    }

    @Override
    void initLayout() {
        setContentView(R.layout.activity_modify);
        RecyclerView rvModifyOrder = findViewById(R.id.rv_modify_order);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 5);
        rvModifyOrder.setLayoutManager(layoutManager);
        btnLeft = findViewById(R.id.btn_arrow_left);
        btnRight = findViewById(R.id.btn_arrow_right);
        tvDate = findViewById(R.id.tv_date);
        tvDate.setOnClickListener(this);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        tvDate.setText(DateUtil.millisToDate(System.currentTimeMillis()));
        final String[] ORDER_TYPES = getResources().getStringArray(R.array.order_types);
        final String[] PAYMENT_TYPES = getResources().getStringArray(R.array.payment_types);
        adapter = new ModifyOrderAdapter(this, ORDER_TYPES, PAYMENT_TYPES);
        rvModifyOrder.setAdapter(adapter);
        tabLayout = findViewById(R.id.tabs);
        loadTabs();

    }
    private void loadTabs() {
        final String[] ORDER_TYPES = getResources().getStringArray(R.array.order_types);
        int length = ORDER_TYPES.length;
        tabLayout.addTab(tabLayout.newTab().setText("ALL"));
        for (int i = 0; i < length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(ORDER_TYPES[i]));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int newIndex = tab.getPosition();
                adapter.setList(viewModel.getCustomerOrderByOrderType(newIndex-1));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_arrow_left: {
                final long newMillis = DateUtil.dateToMillis(tvDate.getText().toString()) - TimeUnit.DAYS.toMillis(1);
                tvDate.setText(DateUtil.millisToDate(newMillis));
                viewModel.getCustomerOrderByDate(tabLayout.getSelectedTabPosition() - 1, newMillis);
            }
                break;
            case R.id.btn_arrow_right: {
                final long newMillis = DateUtil.dateToMillis(tvDate.getText().toString()) + TimeUnit.DAYS.toMillis(1);
                tvDate.setText(DateUtil.millisToDate(newMillis));
                viewModel.getCustomerOrderByDate(tabLayout.getSelectedTabPosition() - 1, newMillis);
            }
                break;
            case R.id.tv_date:
                new CustomDatePickerDialog(this, tvDate, ()->{
                    viewModel.getCustomerOrderByDate(tabLayout.getSelectedTabPosition()-1, DateUtil.dateToMillis(tvDate.getText().toString()));
                }).show();
                break;
            default:
                throw new IllegalArgumentException("The button doesn't exist.");
        }
    }

    @Override
    void bindObserver() {
    }

    @Override
    void removeObserver() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_UPDATE_ORDER && data!=null){
            long id = data.getLongExtra(EXTRA_ID, 0);
            double total = data.getDoubleExtra(EXTRA_TOTAL, 0);
            int orderType = data.getIntExtra(EXTRA_ORDER_TYPE, 0);
            CustomerOrder customerOrder = viewModel.findOrder(id);
            customerOrder.setTotal(total);
            customerOrder.setOrderType(orderType);
            adapter.setList(viewModel.getCustomerOrderByOrderType(tabLayout.getSelectedTabPosition() - 1));
        }
    }

    @Override
    public void onModifyOrder(long id) {
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra(OrderActivity.EXTRA_MODE, OrderActivity.MODE_MODIFY_ORDER);
        intent.putExtra(OrderActivity.EXTRA_ORDER_ID, id);
        startActivityForResult(intent, 0);
    }

}
