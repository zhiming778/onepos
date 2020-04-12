package com.example.onepos.view.activity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.onepos.R;
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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.schedulers.SchedulerPoolFactory;
import io.reactivex.schedulers.Schedulers;

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
        String[] naviItems = getResources().getStringArray(R.array.navi_items);
        adapter = new OfficeNaviAdapter(naviItems, this);
        lvNaviPane.setAdapter(adapter);
        initRestaurantInfoFragment(naviItems[0]);
    }

    @Override
    void bindObserver() {

    }

    @Override
    void removeObserver() {

    }

    private void initRestaurantInfoFragment(String title) {

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, RestaurantInfoFragment.newInstance(), RestaurantInfoFragment.TAG)
                .commit();
        getSupportActionBar().setTitle(title);
    }
    private void loadRestaurantInfoFragment(String title) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, RestaurantInfoFragment.newInstance(), RestaurantInfoFragment.TAG)
                .commit();
        getSupportActionBar().setTitle(title);
    }
    private void loadStaffFragment(String title) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, StaffFragment.newInstance(), StaffFragment.TAG)
                .commit();
        getSupportActionBar().setTitle(title);
    }

    private void loadPermissionFragment(String title) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, PermissionFragment.newInstance(), PermissionFragment.TAG)
                .commit();
        getSupportActionBar().setTitle(title);
    }

    private void clearFragments() {
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> listFragment = fm.getFragments();
        if (listFragment.size() > 0) {
            for (Fragment fragment : listFragment) {
                fm.beginTransaction()
                        .remove(fragment)
                        .commit();
            }
        }
        listFragment.clear();
    }
    @Override
    public void onItemClick(int pos, String title) {
        clearFragments();
        switch (pos) {
            case 0:
                loadRestaurantInfoFragment(title);
                break;
            case 1:
                loadStaffFragment(title);
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
                loadPermissionFragment(title);
                break;
            default:
                throw new IllegalArgumentException("The office navigation button doesn't exist.");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        getResources().updateConfiguration(null, null); //To free all Themekey and LongSparseArray
    }
}
