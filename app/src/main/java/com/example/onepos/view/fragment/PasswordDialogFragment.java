package com.example.onepos.view.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import com.example.onepos.R;
import com.example.onepos.model.Action;
import com.example.onepos.view.PasswordView;
import com.example.onepos.viewmodel.BaseViewModel;
import com.example.onepos.viewmodel.MainViewModel;

public class PasswordDialogFragment extends DialogFragment implements View.OnClickListener {
    public static final String VIEWMODEL_CLASS_TAG = "viewmodel_class_tag";
    public static final String TAG_ACTION_TYPE = "tag_action_type";
    public static final String TAG = "PasswordDialogFragment_TAG";
    private BaseViewModel viewModel;
    private int actionType;
    private Class viewmodelClass;
    private OnPasswordListener listener;

    public interface OnPasswordListener {
        void onPassword(int flag);
    }

    private StringBuilder password;
    private PasswordView passwordView;
    private SparseArray<String> keyValues;
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btnBackpack, btnCancel;

    public static PasswordDialogFragment newInstance(Class<? extends BaseViewModel> c, int actionType) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(VIEWMODEL_CLASS_TAG, c);
        bundle.putInt(TAG_ACTION_TYPE, actionType);
        PasswordDialogFragment fragment = new PasswordDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart() {
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        layoutParams.width = (int)getContext().getResources().getDimension(R.dimen.dialog_password_width);
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


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        ContextThemeWrapper context = new ContextThemeWrapper(getActivity(), 0);
        context.applyOverrideConfiguration(getResources().getConfiguration());
        Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_password, null);
        password = new StringBuilder();
        keyValues = new SparseArray<>();
        initLayout(view);
        fillArray();
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
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            actionType = bundle.getInt(TAG_ACTION_TYPE);
            viewmodelClass = (Class)bundle.getSerializable(VIEWMODEL_CLASS_TAG);
            viewModel = (BaseViewModel) ViewModelProviders.of(getActivity()).get(viewmodelClass);
            viewModel.getLiveFlag().observe(this, flag->{
                switch ((int)flag) {
                    case 0:
                        Toast.makeText(getActivity(), R.string.msg_punch_in, Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getActivity(), R.string.msg_punch_in_error, Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getActivity(), R.string.msg_punch_out, Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getActivity(), R.string.msg_punch_out_error, Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        break;
                    case 5:
                        Toast.makeText(getActivity(), R.string.msg_no_permission, Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        throw new IllegalArgumentException("The action doesn't exist.");
                }
                dismiss();
                if ((int)flag!=5 && listener!=null)
                    listener.onPassword((int)flag);
            });
        }
    }

    private void initLayout(View view) {
        btn1 = view.findViewById(R.id.btn_kb_1);
        btn1.setOnClickListener(this);
        btn2 = view.findViewById(R.id.btn_kb_2);
        btn2.setOnClickListener(this);
        btn3 = view.findViewById(R.id.btn_kb_3);
        btn3.setOnClickListener(this);
        btn4 = view.findViewById(R.id.btn_kb_4);
        btn4.setOnClickListener(this);
        btn5 = view.findViewById(R.id.btn_kb_5);
        btn5.setOnClickListener(this);
        btn6 = view.findViewById(R.id.btn_kb_6);
        btn6.setOnClickListener(this);
        btn7 = view.findViewById(R.id.btn_kb_7);
        btn7.setOnClickListener(this);
        btn8 = view.findViewById(R.id.btn_kb_8);
        btn8.setOnClickListener(this);
        btn9 = view.findViewById(R.id.btn_kb_9);
        btn9.setOnClickListener(this);
        btn0 = view.findViewById(R.id.btn_kb_0);
        btn0.setOnClickListener(this);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
        btnBackpack = view.findViewById(R.id.btn_backpack);
        btnBackpack.setOnClickListener(this);
        passwordView = view.findViewById(R.id.view_pw);
    }

    private void fillArray() {
        keyValues.put(R.id.btn_kb_0, "0");
        keyValues.put(R.id.btn_kb_1, "1");
        keyValues.put(R.id.btn_kb_2, "2");
        keyValues.put(R.id.btn_kb_3, "3");
        keyValues.put(R.id.btn_kb_4, "4");
        keyValues.put(R.id.btn_kb_5, "5");
        keyValues.put(R.id.btn_kb_6, "6");
        keyValues.put(R.id.btn_kb_7, "7");
        keyValues.put(R.id.btn_kb_8, "8");
        keyValues.put(R.id.btn_kb_9, "9");
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_backpack) {
            delete();
        } else if (id == R.id.btn_cancel) {
            //TODO
        } else {
            append(id);
        }
    }

    private void append(int id) {
        password.append(keyValues.get(id));
        int length = password.length();
        passwordView.setDigits(length);
        if (length == 4) {
            actions(password.toString());
        }
    }

    private void actions(String password) {
        if (actionType == Action.PUNCH_IN || actionType == Action.PUNCH_OUT) {
            if (viewModel instanceof MainViewModel) {
                if (actionType == Action.PUNCH_IN)
                    ((MainViewModel) viewModel).punchIn(password);
                if (actionType == Action.PUNCH_OUT)
                    ((MainViewModel) viewModel).punchOut(password);
            }
        } else {
            String requiredLevel;
            switch (actionType) {
                case Action.DISCOUNT:
                    requiredLevel = PreferenceManager.getDefaultSharedPreferences(getContext())
                            .getString(getString(R.string.key_permission_discount), getString(R.string.def_permission_level));
                    break;
                case Action.DINE_IN:
                    requiredLevel = PreferenceManager.getDefaultSharedPreferences(getContext())
                            .getString(getString(R.string.key_permission_dine_in), getString(R.string.def_permission_level));
                    break;
                case Action.LOG:
                    requiredLevel = PreferenceManager.getDefaultSharedPreferences(getContext())
                            .getString(getString(R.string.key_permission_log), getString(R.string.def_permission_level));
                    break;
                case Action.SETTING:
                    requiredLevel = PreferenceManager.getDefaultSharedPreferences(getContext())
                            .getString(getString(R.string.key_permission_setting), getString(R.string.def_permission_level));
                    break;
                case Action.MODIFY_ORDER:
                    requiredLevel = PreferenceManager.getDefaultSharedPreferences(getContext())
                            .getString(getString(R.string.key_permission_modify_order), getString(R.string.def_permission_level));
                    break;
                default:
                    throw new IllegalArgumentException("The action doesn't exist.");
            }
            viewModel.isTitleEligible(password, Integer.valueOf(requiredLevel));
        }
    }
    private void delete() {
        password.deleteCharAt(password.length() - 1);
        passwordView.setDigits(password.length());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.getLiveFlag().removeObservers(this);
        keyValues.clear();
        keyValues = null;
        password.setLength(0);
        password = null;
        passwordView = null;
        listener = null;
        btnBackpack.setOnClickListener(null);
        btnCancel.setOnClickListener(null);
        btn1.setOnClickListener(null);
        btn2.setOnClickListener(null);
        btn3.setOnClickListener(null);
        btn4.setOnClickListener(null);
        btn5.setOnClickListener(null);
        btn6.setOnClickListener(null);
        btn7.setOnClickListener(null);
        btn8.setOnClickListener(null);
        btn9.setOnClickListener(null);
        btn0.setOnClickListener(null);
        btn1 = null;
        btn2 = null;
        btn3 = null;
        btn4 = null;
        btn5 = null;
        btn6 = null;
        btn7 = null;
        btn8 = null;
        btn9 = null;
        btn0 = null;
    }

    public StringBuilder getPassword() {
        return password;
    }


    public void setListener(OnPasswordListener listener) {
        this.listener = listener;
    }


}
