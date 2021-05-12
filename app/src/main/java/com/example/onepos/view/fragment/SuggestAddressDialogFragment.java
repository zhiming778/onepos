package com.example.onepos.view.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.onepos.R;
import com.example.onepos.model.Address;
import com.example.onepos.model.PosContract;
import com.example.onepos.view.PasswordView;
import com.example.onepos.viewmodel.BaseViewModel;
import com.example.onepos.viewmodel.OrderViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuggestAddressDialogFragment extends DialogFragment {

    public static final String TAG = "suggest_address_dialog_fragment";
    public static final String TAG_ADDRESS = "address";
    private OrderViewModel viewModel;
    private ListView listView;
    private AddressSelectedListener listener;

    public interface AddressSelectedListener {
        void addressSelected(Address address);
    }

    public static SuggestAddressDialogFragment newInstance(String address) {
        Bundle bundle = new Bundle();
        bundle.putString(TAG_ADDRESS, address);
        SuggestAddressDialogFragment fragment = new SuggestAddressDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart() {
        Window window = getDialog().getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        super.onStart();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        ContextThemeWrapper context = new ContextThemeWrapper(getActivity(), 0);
        context.applyOverrideConfiguration(getResources().getConfiguration());
        Dialog dialog = new Dialog(context);
        TextView tvTitle = dialog.findViewById(android.R.id.title);
        tvTitle.setBackgroundColor(context.getColor(R.color.colorMain));
        tvTitle.setTextColor(context.getColor(R.color.colorWhiteText));
        int padding = (int)context.getResources().getDimension(R.dimen.distance_med);
        tvTitle.setPadding(padding, padding, padding, padding);
        dialog.setTitle(R.string.title_choose_address);
        listView = new ListView(getActivity());
        listView.setOnItemClickListener((parent, view, position, id)->{
            if (listener!=null)
                listener.addressSelected(viewModel.getSuggestAddresses().get(position));
            dismiss();
        });
        dialog.setContentView(listView);
        dialog.create();
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            final String address = getArguments().getString(TAG_ADDRESS);
            viewModel = ViewModelProviders.of(getActivity()).get(OrderViewModel.class);
            viewModel.getLiveFlag().observe(this, flag->{
                viewModel.getLiveFlag().removeObservers(this);
                if (flag == 8) {
                    listView.setAdapter(new SimpleAdapter(getActivity(), getListMapSuggestAddresses(viewModel.getSuggestAddresses()),
                            R.layout.cell_suggest_address,
                            new String[]{PosContract.JsonLabel.LABEL_ADDRESS, PosContract.JsonLabel.LABEL_NAME},
                            new int[]{R.id.tv_address, R.id.tv_business}));
                }
            });
            viewModel.getListSuggestAddresses(address);
        }
    }

    public void setListener(AddressSelectedListener listener) {
        this.listener = listener;
    }
    private List<Map<String, String>> getListMapSuggestAddresses(List<Address> origin) {
        List<Map<String, String>> list = new ArrayList<>();
        for (Address address : origin) {
            Map<String, String> map = new HashMap<>();
            map.put(PosContract.JsonLabel.LABEL_ADDRESS, address.toString());
            map.put(PosContract.JsonLabel.LABEL_NAME, address.getDeliveryInstruction());
            list.add(map);
        }
        return list;
    }
}
