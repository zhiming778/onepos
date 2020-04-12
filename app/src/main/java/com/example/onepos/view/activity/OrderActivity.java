package com.example.onepos.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.onepos.model.CustomerOrder;
import com.example.onepos.util.MLog;
import com.example.onepos.view.fragment.CustomerInfoFragment;
import com.example.onepos.view.fragment.OrderFragment;
import com.example.onepos.viewmodel.OrderViewModel;
import com.example.onepos.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.schedulers.SchedulerPoolFactory;
import io.reactivex.schedulers.Schedulers;


//TODO move some listeners to their own fragments
public class OrderActivity extends BaseActivity<OrderViewModel> {

    @Inject
    public ViewModelFactory viewModelFactory;
    private final String ORDER_FRAGMENT_TAG = "order_fragment";
    private final String CUSTOMER_FRAGMENT_TAG = "customer_fragment";
    public static final int MODE_CREATE_ORDER = 0;
    public static final int MODE_MODIFY_ORDER = 1;
    public static final String EXTRA_MODE = "extra_mode";
    public static final String EXTRA_ORDER_ID = "extra_order_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(OrderViewModel.class);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            int orderType = intent.getIntExtra(CustomerOrder.EXTRA_ORDER_TYPE, CustomerOrder.ORDER_TYPE_DINE_IN);
            int mode = intent.getIntExtra(EXTRA_MODE, MODE_CREATE_ORDER);
            viewModel.setOrderType(orderType);
            viewModel.setMode(mode);
            loadFragment(orderType, mode);
        }
    }

    @Override
    void initLayout() {

    }

    @Override
    void bindObserver() {

    }

    @Override
    void removeObserver() {

    }

    private void loadFragment(int orderType, int mode) {
        if (mode == MODE_CREATE_ORDER)
            switch (orderType) {
                case CustomerOrder.ORDER_TYPE_DINE_IN:
                    //TODO load TableFragment
                    break;
                case CustomerOrder.ORDER_TYPE_PICK_UP:
                    loadOrderFragment(-1);
                    break;
                case CustomerOrder.ORDER_TYPE_DELIVERY:
                case CustomerOrder.ORDER_TYPE_WALK_IN:
                    loadCustomerInfoFragment();
                    break;
                default:
                    throw new IllegalArgumentException("The order type doesn't exist.");
            }
        else
            loadOrderFragment(getIntent().getLongExtra(EXTRA_ORDER_ID, -1));
    }
    private void loadOrderFragment(long id) {
        OrderFragment orderFragment = OrderFragment.newInstance(id);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, orderFragment, OrderFragment.TAG)
                .commit();
    }
    private void loadCustomerInfoFragment() {
        CustomerInfoFragment customerInfoFragment = CustomerInfoFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, customerInfoFragment, CustomerInfoFragment.TAG)
                .commit();
    }
    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ORDER_FRAGMENT_TAG);
        if (fragment instanceof OrderFragment) {
            FragmentManager fm = fragment.getChildFragmentManager();
            if (fm.getBackStackEntryCount()>0)
                fm.popBackStack();
        }
        else
            super.onBackPressed();
    }
}
