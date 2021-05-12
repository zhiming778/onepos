package com.example.onepos.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import androidx.appcompat.widget.AppCompatEditText;

import com.example.onepos.R;
import com.example.onepos.model.Address;
import com.example.onepos.model.PosContract;
import com.example.onepos.util.MLog;
import com.example.onepos.view.fragment.CustomerInfoFragment;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class AddressCompletionEditText extends AppCompatEditText {
    private OnAddressChangeListener listener;
    public interface OnAddressChangeListener{
        void onAddressChanged(String s);
    }

    public AddressCompletionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnAddressChangeListneer(OnAddressChangeListener listneer) {
        this.listener = listneer;
        addTextChangedListener(new AddressTextWatcher(listener));
    }

    static class AddressTextWatcher implements TextWatcher {

        private final Pattern pattern;
        private OnAddressChangeListener listener;

        public AddressTextWatcher(OnAddressChangeListener listener) {
            this.listener = listener;
            pattern = Pattern.compile("\\d{1,5}\\s\\w{2}");
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (pattern.matcher(s).matches()){
                if (listener != null) {
                    listener.onAddressChanged(s.toString());
                }
            }
        }
    }

}
