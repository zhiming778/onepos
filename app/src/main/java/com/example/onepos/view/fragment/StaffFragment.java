package com.example.onepos.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onepos.R;
import com.example.onepos.view.adapter.StaffAdapter;
import com.example.onepos.viewmodel.OfficeViewModel;

public class StaffFragment extends Fragment implements StaffAdapter.OnItemClickListener{

    private StaffAdapter adapter;
    public static final String TAG = "StaffFragment";
    private OfficeViewModel viewModel;
    private RecyclerView rvStaff;
    private LinearLayoutManager layoutManager;
    private MenuItem menuItem;

    public static StaffFragment newInstance() {
        StaffFragment fragment = new StaffFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rvStaff = (RecyclerView) inflater.inflate(R.layout.fragment_staff, container, false);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvStaff.setLayoutManager(layoutManager);
        adapter = new StaffAdapter(this);
        rvStaff.setAdapter(adapter);
        setHasOptionsMenu(true);
        return rvStaff;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(OfficeViewModel.class);
        viewModel.getLiveCursorStaff().observe(this, cursor -> {
            adapter.setCursor(cursor);
        });
        viewModel.getAllStaff();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden)
            viewModel.getAllStaff();
    }

    private void loadStaffInfoFragment(long id) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, StaffInfoFragment.newInstance(id), StaffInfoFragment.TAG)
                .hide(this)
                .commit();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_staff, menu);
        menuItem = menu.findItem(R.id.action_new_staff);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_staff:
                loadStaffInfoFragment(-1);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
        menuItem.setIcon(null);
    }

    @Override
    public void onItemClick(long id) {
        loadStaffInfoFragment(id);
    }
}
