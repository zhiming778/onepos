package com.example.onepos.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.onepos.R;

public abstract class DiscountDialog extends Dialog{
    private RadioGroup rgType;
    public DiscountDialog(@NonNull Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_discount, null);
        RadioGroup rg = view.findViewById(R.id.rg_discount_percentage);
        rgType = view.findViewById(R.id.rg_discount_type);
        loadDiscountPercentage(context, rg);
        setContentView(view);
    }
    private void loadDiscountPercentage(Context context, RadioGroup rg) {
        String[] arrPercentage = context.getResources().getStringArray(R.array.discount_percentage);
        for (String percentage : arrPercentage) {
            RadioButton rb = new RadioButton(context, null, 0, R.style.CustomRadioButton);
            rb.setText(percentage);
            rg.addView(rb);
        }
        rg.setOnCheckedChangeListener((radioGroup, checkedId)->{
            int indexType = rgType.indexOfChild(rgType.findViewById(rgType.getCheckedRadioButtonId()));
            int indexPercentage = rg.indexOfChild(rg.findViewById(checkedId));
            callback(indexType, indexPercentage);
            dismiss();
        });
    }
    public abstract void callback(int indexType, int indexPercentage);

    @Override
    public void show() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        super.show();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }
}
