package com.example.onepos.view.fragment;

import android.app.Dialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.onepos.R;
import com.example.onepos.model.PosContract;
import com.example.onepos.viewmodel.OrderViewModel;

public class MapDialogFragment extends DialogFragment implements View.OnClickListener {
    public static final String TAG = "mapdialogfragment_tag";
    public static final String TAG_DESINATION = "tag_destination";

    private OrderViewModel viewModel;
    private TextView tvDistance, tvDuration, tvTraffic;
    private ImageView ivMap;
    private Button btnConfirm;

    public static MapDialogFragment newInstance(String destination) {
        Bundle bundle = new Bundle();
        bundle.putString(TAG_DESINATION, destination);
        MapDialogFragment fragment = new MapDialogFragment();
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
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_map, null);
        initLayout(view);
        dialog.setContentView(view);
        TextView tvTitle = dialog.findViewById(android.R.id.title);
        tvTitle.setBackgroundColor(context.getColor(R.color.colorMain));
        tvTitle.setTextColor(context.getColor(R.color.colorWhiteText));
        int padding = (int)context.getResources().getDimension(R.dimen.distance_med);
        tvTitle.setPadding(padding, padding, padding, padding);
        dialog.setTitle(R.string.title_password_dialog);

        dialog.create();

        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(OrderViewModel.class);
        if (getArguments()!=null) {
            String destination = getArguments().getString(TAG_DESINATION);
            viewModel.getMapData(destination);
            viewModel.getLiveFlag().observe(this, flag -> {
                if (flag == 7) {
                    ivMap.setImageBitmap(viewModel.getMapImage());
                    ContentValues values = viewModel.getRouteData();
                    if (values.getAsString(PosContract.JsonLabel.LABEL_STATUS_DESCRIPTION)!=null) {
                        int min = viewModel.getRouteData().getAsInteger(PosContract.JsonLabel.LABEL_TRAVEL_DURATION);
                        tvDuration.setText(String.format("%d min", min));
                        double distance = viewModel.getRouteData().getAsDouble(PosContract.JsonLabel.LABEL_TRAVEL_DISTANCE);
                        tvDistance.setText(String.format("%.1f mi", distance));
                        tvTraffic.setText(viewModel.getRouteData().getAsString(PosContract.JsonLabel.LABEL_TRAFFIC_CONGESTION));
                    }
                }
                viewModel.getLiveFlag().removeObservers(this);
            });
        }

    }

    private void initLayout(View view) {
        tvDistance = view.findViewById(R.id.tv_distance);
        tvDuration = view.findViewById(R.id.tv_duration);
        tvTraffic = view.findViewById(R.id.tv_traffic);
        ivMap = view.findViewById(R.id.iv_map);
        btnConfirm = view.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        ivMap.setImageBitmap(null);
        viewModel.clearMapData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_confirm)
            dismiss();
    }
}
