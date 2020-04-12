package com.example.onepos.util;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.fragment.app.Fragment;

public class PhoneNumberTextWatcher implements TextWatcher {

    private boolean needFormatted = true;
    private boolean selfTriggered = false;

    private OnNumberCompleteListener onNumberCompleteListener;

    public interface OnNumberCompleteListener {
        void onNumberCompleted(String phoneNumber);
    }

    public PhoneNumberTextWatcher(Fragment fragment) {
        this.onNumberCompleteListener = (OnNumberCompleteListener)fragment;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if (selfTriggered) {
            selfTriggered = false;
            return;
        }
        if (charSequence.length() != 0) {
            needFormatted = true;
        } else
            needFormatted = false;
    }

    @Override
    public void afterTextChanged(Editable editable) {

        if (needFormatted) {
            selfTriggered = true;
            needFormatted = false;
            formatter(editable);
        }
    }


    private void formatter(Editable editable) {
        String oldFormat = editable.toString();
        oldFormat = oldFormat.replaceAll("[^\\d.]", "");
        int length = oldFormat.length();
        if (length < 4) {
            editable.replace(0, editable.length(), oldFormat, 0, oldFormat.length());
        } else if (length < 7) {
            StringBuilder sb = new StringBuilder(oldFormat);
            sb.insert(3, '-');
            editable.replace(0, editable.length(), sb.toString(), 0, sb.length());
        } else {
            StringBuilder sb = new StringBuilder(oldFormat);
            sb.insert(3, '-');
            sb.insert(7, '-');
            editable.replace(0, editable.length(), sb.toString(), 0, sb.length());
            if (length == 10) {
                onNumberCompleteListener.onNumberCompleted(sb.toString());
            }
        }
    }

}
