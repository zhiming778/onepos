package com.example.onepos.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.onepos.R;

public class CustomDatePickerDialog extends AlertDialog{

    private EditText editText;
    private DatePicker datePicker;

    public CustomDatePickerDialog(Context context, EditText editText) {
        super(context);
        this.editText = editText;
        datePicker = (DatePicker) LayoutInflater.from(context).inflate(R.layout.dialog_datepicker, null);
        updateDate();
        setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.btn_confirm), ((dialogInterface, i) -> {
            editText.setText(String.format("%s/%s/%s", datePicker.getMonth()+1, datePicker.getDayOfMonth(), datePicker.getYear()));
        }));
        setView(datePicker);
    }

    private void updateDate() {
        int year = 0, month = 0, day = 0;
        String oldDate = editText.getText().toString();
        if (oldDate.matches("\\d{1,2}\\/\\d{1,2}\\/\\d{4}"))
        {
            String[] old = editText.getText().toString().split("/");
            month = Integer.parseInt(old[0]);
            day = Integer.parseInt(old[1]);
            year = Integer.parseInt(old[2]);
        }
        datePicker.updateDate(year, month-1, day);
    }

    @Override
    protected void onStop() {
        super.onStop();
        editText.clearFocus();
    }

    @Override
    public void show() {
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        super.show();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }
}
