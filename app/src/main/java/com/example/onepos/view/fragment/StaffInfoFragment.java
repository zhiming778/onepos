package com.example.onepos.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.onepos.R;
import com.example.onepos.model.Staff;
import com.example.onepos.util.DateUtil;
import com.example.onepos.util.MLog;
import com.example.onepos.util.Validator;
import com.example.onepos.view.dialog.CustomDatePickerDialog;
import com.example.onepos.viewmodel.OfficeViewModel;

public class StaffInfoFragment extends Fragment implements View.OnClickListener {

    private OfficeViewModel officeViewModel;
    public static final String TAG = "StaffInfoFragment";
    public static final String STAFF_ID = "staff_id";
    private EditText etName, etAddress, etPhoneNum, etSsn, etBirth, etPassword;
    private RadioGroup rgTitle, rgLang;
    private String[] TITLES, LANGUAGES;


    public static StaffInfoFragment newInstance(long id) {
        StaffInfoFragment fragment = new StaffInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(STAFF_ID, id);
        fragment.setArguments(bundle);
        return fragment;
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
        officeViewModel = ViewModelProviders.of(getActivity()).get(OfficeViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_staff_info, container, false);
        TITLES = getContext().getResources().getStringArray(R.array.staff_titles);
        LANGUAGES = getContext().getResources().getStringArray(R.array.staff_langs);
        long id;
        if ((id = getArguments().getLong(StaffInfoFragment.STAFF_ID, 0))!=0)
            setEditStaffView(rootView, id);
        else
            setCreateStaffView(rootView);
        initLayout(rootView);
        return rootView;
    }

    private void setCreateStaffView(View rootView) {
        rootView.findViewById(R.id.btn_delete).setVisibility(View.GONE);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("New Staff");
        Button btnSave = rootView.findViewById(R.id.btn_save);
        btnSave.setText(R.string.btn_create_staff);
        btnSave.setOnClickListener(this);
    }

    private void setEditStaffView(View rootView, long id) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Staff Info");
        Button btnSave = rootView.findViewById(R.id.btn_save);
        btnSave.setText(R.string.btn_edit_staff);
        btnSave.setOnClickListener(this);
        rootView.findViewById(R.id.btn_delete).setOnClickListener(this);
        officeViewModel.getStaffById(id);
        officeViewModel.getLiveStaff().observe(this, staff -> {
            fillFields(staff);
            officeViewModel.getLiveStaff().removeObservers(this);
        });
    }
    private void initLayout(View rootView) {
        etBirth = rootView.findViewById(R.id.et_birth);
        etAddress = rootView.findViewById(R.id.et_address);
        etName = rootView.findViewById(R.id.et_name);
        etPassword = rootView.findViewById(R.id.et_password);
        etSsn = rootView.findViewById(R.id.et_ssn);
        etPhoneNum = rootView.findViewById(R.id.et_phone_num);
        etBirth.setShowSoftInputOnFocus(false);
        etBirth.onCreateInputConnection(new EditorInfo());
        rgTitle = rootView.findViewById(R.id.rg_title);
        rgLang = rootView.findViewById(R.id.rg_lang);
        loadJobTitles(rgTitle);
        loadPreferredLanguages(rgLang);
        etBirth.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                new CustomDatePickerDialog(getActivity(), etBirth,null)
                    .show();
            }
        });
    }

    private void fillFields(Staff staff) {
        etName.setText(staff.getName());
        etPhoneNum.setText(staff.getPhoneNum());
        etAddress.setText(staff.getAddress());
        etPassword.setText(staff.getPassword());
        etSsn.setText(staff.getSsn());
        etBirth.setText(DateUtil.millisToDate(staff.getDateOfBirth()));
        rgTitle.check(rgTitle.getChildAt(staff.getTitle()).getId());
        rgLang.check(rgLang.getChildAt(staff.getLang()).getId());
    }
    private void loadJobTitles(RadioGroup rgTitle) {
        for (String title:TITLES){
            RadioButton button = new RadioButton(getActivity());
            button.setText(title);
            rgTitle.addView(button);
        }
        rgTitle.check(rgTitle.getChildAt(0).getId());
    }
    private void loadPreferredLanguages(RadioGroup rgTitle) {
        for (String lang:LANGUAGES) {
            RadioButton button = new RadioButton(getActivity());
            button.setText(lang);
            rgTitle.addView(button);
        }
        rgTitle.check(rgLang.getChildAt(0).getId());
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_delete:
                officeViewModel.deleteCurrentStaff();
                backToStaffFragment();
                break;
            case R.id.btn_save:
                int title = rgTitle.indexOfChild(rgTitle.findViewById(rgTitle.getCheckedRadioButtonId()));
                int lang = rgLang.indexOfChild(rgLang.findViewById(rgLang.getCheckedRadioButtonId()));
                if (Validator.isFieldValid(etName)&&Validator.isTitleValid(title)&&Validator.isFieldValid(etPhoneNum)&&Validator.isFieldValid(etAddress)
                    &&Validator.isFieldValid(etBirth)&&Validator.isSsnValid(etSsn)&&Validator.isPasswordValid(etPassword)){
                    officeViewModel.saveStaff(etName.getText().toString(), title, lang, DateUtil.dateToMillis(etBirth.getText().toString())
                            , etPhoneNum.getText().toString(), etAddress.getText().toString(), etSsn.getText().toString(), etPassword.getText().toString());
                    backToStaffFragment();
                }
                else{
                    Toast.makeText(getActivity(), R.string.msg_invalid_field, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                throw new IllegalArgumentException("The button doesn't exist.");
        }
    }

    private void saveAndExit() {

    }
    private void backToStaffFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction()
                .remove(this)
                .add(R.id.fragment_container, StaffFragment.newInstance(), StaffFragment.TAG)
                .commit();
    }
}
