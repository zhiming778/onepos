package com.example.onepos.view.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onepos.R;
import com.example.onepos.model.CustomerOrder;
import com.example.onepos.util.DateUtil;
import com.example.onepos.util.MLog;
import com.example.onepos.util.OrderListener;
import com.example.onepos.view.adapter.ReceiptAdapter;
import com.example.onepos.viewmodel.OrderViewModel;

public class OrderHistoryDialogFragment extends DialogFragment implements OrderListener, View.OnClickListener {

    public static final String TAG = "OrderHistoryDialogFragment";
    public static final String TAG_position = "tag_position";
    private TextView tvDate, tvTotal, tvOrderType, tvAddress;
    private Button btnReorderItem, btnReorderAll, btnCancel;
    private RecyclerView rvReceipt;
    private OrderViewModel viewModel;
    private ReceiptAdapter adapter;

    public static OrderHistoryDialogFragment newInstance(int pos) {

        Bundle bundle= new Bundle();
        bundle.putInt(TAG_position, pos);
        OrderHistoryDialogFragment fragment = new OrderHistoryDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart() {
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(layoutParams);
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(OrderViewModel.class);


    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        ContextThemeWrapper context = new ContextThemeWrapper(getActivity(), 0);
        context.applyOverrideConfiguration(getResources().getConfiguration());
        Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_order_history, null);
        initlayout(view);
        TextView tvTitle = dialog.findViewById(android.R.id.title);
        tvTitle.setBackgroundColor(context.getColor(R.color.colorMain));
        tvTitle.setTextColor(context.getColor(R.color.colorWhiteText));
        dialog.setContentView(view);
        if (getArguments() != null) {
            int pos = getArguments().getInt(TAG_position);
            final CustomerOrder customerOrder = viewModel.getCustomerOrders().get(pos);
            if (customerOrder.getOrderType() != CustomerOrder.ORDER_TYPE_DELIVERY) {
                tvAddress.setVisibility(View.GONE);
            }
            else if (customerOrder.getFkAddressId() != null) {
                tvAddress.setVisibility(View.VISIBLE);
                viewModel.getPrevAddressById(customerOrder.getFkAddressId());
            }
            viewModel.getLiveFlag().observe(this, flag->{
                viewModel.getLiveFlag().removeObservers(this);
                if (flag == 6) {
                    adapter = new ReceiptAdapter(getContext(),viewModel.getOldReceipt(), this);
                    rvReceipt.setAdapter(adapter);
                }
            });
            viewModel.getLiveAddress().observe(this, address->{
                tvAddress.append(address.toString());

            });
            viewModel.getOldReceiptByOrderId(customerOrder.getId());
            tvOrderType.append(getResources().getStringArray(R.array.order_types)[customerOrder.getOrderType()]);
            tvDate.append(DateUtil.millisToDate(customerOrder.getDate()));
            tvTotal.append(String.format(getString(R.string.format_currency), customerOrder.getTotal()));
        }
        return dialog;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_reorder_all:
                viewModel.oldReceiptToReceipt(-1);
                break;
            case R.id.btn_reorder_item:
                viewModel.oldReceiptToReceipt(viewModel.getIndexSelectedReceipt());
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
            default:
                throw new IllegalArgumentException("The button doesnt exist.");
        }
    }

    @Override
    public void onOrder(int index, int request) {
        viewModel.setIndexSelectedReceipt(index);
    }

    private void initlayout(View rootView) {
        tvDate = rootView.findViewById(R.id.tv_date);
        tvOrderType = rootView.findViewById(R.id.tv_order_type);
        tvAddress = rootView.findViewById(R.id.tv_address);
        tvTotal = rootView.findViewById(R.id.tv_total);
        btnReorderAll = rootView.findViewById(R.id.btn_reorder_all);
        btnReorderItem = rootView.findViewById(R.id.btn_reorder_item);
        btnCancel = rootView.findViewById(R.id.btn_cancel);
        rvReceipt = rootView.findViewById(R.id.rv_receipt);
        rvReceipt.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        btnReorderAll.setOnClickListener(this);
        btnReorderItem.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }


}
