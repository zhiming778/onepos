package com.example.onepos.util;

import android.service.autofill.Validators;
import android.text.TextUtils;
import android.widget.EditText;

import com.example.onepos.model.Staff;

public class Validator{
    public static boolean isFieldValid(EditText editText) {
        return !TextUtils.isEmpty(editText.getText());
    }

    public static boolean isTitleValid(int title) {
        return (title >=0 && title < Staff.NUM_OF_TITLES);
    }

    public static boolean isSsnValid(EditText editText) {
        String ssn = editText.getText().toString();
        if (TextUtils.isEmpty(ssn))
            return false;
        else
            return ssn.length() == 9;
    }
    public static boolean isPasswordValid(EditText editText) {
        String password = editText.getText().toString();
        if (TextUtils.isEmpty(password))
            return false;
        else
            return password.length() == 4;
    }
}
