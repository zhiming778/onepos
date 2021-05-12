package com.example.onepos.view.activity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.onepos.R;
import com.example.onepos.model.Staff;
import com.example.onepos.util.MLog;
import com.example.onepos.view.adapter.OfficeNaviAdapter;
import com.example.onepos.view.fragment.PermissionFragment;
import com.example.onepos.view.fragment.RestaurantInfoFragment;
import com.example.onepos.view.fragment.StaffFragment;
import com.example.onepos.view.fragment.StaffInfoFragment;
import com.example.onepos.viewmodel.OfficeViewModel;
import com.example.onepos.viewmodel.ViewModelFactory;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class OfficeActivity extends BaseActivity<OfficeViewModel> implements OfficeNaviAdapter.OnItemClickListener{

    private ListView lvNaviPane;
    private OfficeNaviAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, factory).get(OfficeViewModel.class);
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_office);
        setSupportActionBar(findViewById(R.id.toolbar));
        lvNaviPane = findViewById(R.id.lv_navi);
        final String[] naviItems = getResources().getStringArray(R.array.navi_items);
        adapter = new OfficeNaviAdapter(naviItems, this);
        lvNaviPane.setAdapter(adapter);
        initRestaurantInfoFragment();
    }

    @Override
    void bindObserver() {

    }

    @Override
    void removeObserver() {

    }

    private void initRestaurantInfoFragment() {

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, RestaurantInfoFragment.newInstance(), RestaurantInfoFragment.TAG)
                .commit();
    }
    private void loadRestaurantInfoFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, RestaurantInfoFragment.newInstance(), RestaurantInfoFragment.TAG)
                .commit();
    }
    private void loadStaffFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, StaffFragment.newInstance(), StaffFragment.TAG)
                .commit();
    }

    private void loadPermissionFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, PermissionFragment.newInstance(), PermissionFragment.TAG)
                .commit();
    }

    private void clearFragments() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment;
        if ((fragment = fm.findFragmentById(R.id.fragment_container))!=null)
        fm.beginTransaction()
                .remove(fragment)
                .commit();
    }
    @Override
    public void onItemClick(int pos) {
        clearFragments();
        switch (pos) {
            case 0:
                loadRestaurantInfoFragment();
                break;
            case 1:
                loadStaffFragment();
                break;
            case 2:

                break;
            case 3:

                break;
            case 4:

                break;
            case 5:

                break;
            case 6:
                loadPermissionFragment();
                break;
            default:
                throw new IllegalArgumentException("The office navigation button doesn't exist.");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        getResources().updateConfiguration(null, null); //To free all Themekey and LongSparseArray
    }
}
