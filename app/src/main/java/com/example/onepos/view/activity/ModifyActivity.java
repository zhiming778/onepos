package com.example.onepos.view.activity;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.onepos.R;
import com.example.onepos.model.CustomerOrder;
import com.example.onepos.util.DateUtil;
import com.example.onepos.view.adapter.ModifierAdapter;
import com.example.onepos.view.adapter.ModifyOrderAdapter;
import com.example.onepos.viewmodel.ModifyViewModel;
import com.example.onepos.viewmodel.ViewModelFactory;
import com.google.android.material.tabs.TabLayout;

import javax.inject.Inject;

public class ModifyActivity extends BaseActivity<ModifyViewModel> implements ModifyOrderAdapter.OnModifyOrderListener {

    private TabLayout tabLayout;
    private ModifyOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, factory).get(ModifyViewModel.class);
        viewModel.getAllCustomerOrder(DateUtil.getMidnightMillis(System.currentTimeMillis()), System.currentTimeMillis());
    }

    @Override
    void initLayout() {
        setContentView(R.layout.activity_modify);
        RecyclerView rvModifyOrder = findViewById(R.id.rv_modify_order);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 5);
        rvModifyOrder.setLayoutManager(layoutManager);
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
                if (newIndex == 0) {
                    viewModel.getAllCustomerOrder(DateUtil.getMidnightMillis(System.currentTimeMillis()), System.currentTimeMillis());
                }
                else
                    viewModel.getCustomerOrderByOrderType(newIndex-1, DateUtil.getMidnightMillis(System.currentTimeMillis()), System.currentTimeMillis());
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
    void bindObserver() {
        viewModel.getLiveListCurstomerOrder().observe(this, adapter::setList);
    }

    @Override
    void removeObserver() {
        viewModel.getLiveListCurstomerOrder().removeObservers(this);
    }

    @Override
    public void onModifyOrder(long id) {
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra(OrderActivity.EXTRA_MODE, OrderActivity.MODE_MODIFY_ORDER);
        intent.putExtra(OrderActivity.EXTRA_ORDER_ID, id);
        startActivity(intent);
    }
}
