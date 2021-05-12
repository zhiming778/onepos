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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onepos.R;
import com.example.onepos.util.MLog;
import com.example.onepos.view.adapter.StaffAdapter;
import com.example.onepos.viewmodel.OfficeViewModel;

public class StaffFragment extends Fragment implements StaffAdapter.OnItemClickListener{

    private StaffAdapter adapter;
    public static final String TAG = "StaffFragment";
    private OfficeViewModel viewModel;
    private RecyclerView rvStaff;
    private LinearLayoutManager layoutManager;

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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Staff");
        rvStaff = (RecyclerView) inflater.inflate(R.layout.fragment_staff, container, false);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvStaff.setLayoutManager(layoutManager);
        final String[] TITLES = getResources().getStringArray(R.array.staff_titles);
        adapter = new StaffAdapter(this, TITLES);
        rvStaff.setAdapter(adapter);
        setHasOptionsMenu(true);
        return rvStaff;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(OfficeViewModel.class);
        viewModel.getLiveListStaff().observe(this, adapter::setList);
        viewModel.getAllStaff();
    }

    private void loadStaffInfoFragment(long id) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, StaffInfoFragment.newInstance(id), StaffInfoFragment.TAG)
                .remove(this)
                .commit();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_staff, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_staff:
                loadStaffInfoFragment(0);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
    }

    @Override
    public void onItemClick(long id) {
        loadStaffInfoFragment(id);
    }
}
