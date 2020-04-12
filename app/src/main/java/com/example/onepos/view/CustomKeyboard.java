package com.example.onepos.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.onepos.R;
import com.example.onepos.util.MLog;

public class CustomKeyboard extends ConstraintLayout implements View.OnClickListener {

    private SparseArray<String> keyValues;
    private InputConnection inputConnection;
    private Button btnQ, btnW, btnE, btnR, btnT, btnY, btnU, btnI, btnO, btnP, btnA, btnS,
            btnD, btnF, btnG, btnH, btnJ, btnK, btnL, btnZ, btnX, btnC, btnV, btnB, btnN, btnM,
            btnBackspace, btnSpace,
            btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0;


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        btnQ.setOnClickListener(null);
        btnW.setOnClickListener(null);
        btnE.setOnClickListener(null);
        btnR.setOnClickListener(null);
        btnT.setOnClickListener(null);
        btnY.setOnClickListener(null);
        btnU.setOnClickListener(null);
        btnI.setOnClickListener(null);
        btnO.setOnClickListener(null);
        btnP.setOnClickListener(null);
        btnA.setOnClickListener(null);
        btnS.setOnClickListener(null);
        btnD.setOnClickListener(null);
        btnF.setOnClickListener(null);
        btnG.setOnClickListener(null);
        btnH.setOnClickListener(null);
        btnJ.setOnClickListener(null);
        btnK.setOnClickListener(null);
        btnL.setOnClickListener(null);
        btnZ.setOnClickListener(null);
        btnX.setOnClickListener(null);
        btnC.setOnClickListener(null);
        btnV.setOnClickListener(null);
        btnB.setOnClickListener(null);
        btnN.setOnClickListener(null);
        btnM.setOnClickListener(null);
        btnBackspace.setOnClickListener(null);
        btnSpace.setOnClickListener(null);
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
        btnQ = null;
        btnW = null;
        btnE = null;
        btnR = null;
        btnT = null;
        btnY = null;
        btnU = null;
        btnI = null;
        btnO = null;
        btnP = null;
        btnA = null;
        btnS = null;
        btnD = null;
        btnF = null;
        btnG = null;
        btnH = null;
        btnJ = null;
        btnK = null;
        btnL = null;
        btnZ = null;
        btnX = null;
        btnC = null;
        btnV = null;
        btnB = null;
        btnN = null;
        btnM = null;
        btnBackspace = null;
        btnSpace = null;
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
        keyValues.clear();
        keyValues = null;
    }

    public CustomKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        keyValues = new SparseArray<>();
        inflate(context, R.layout.custom_keyboard, this);
        init(context);
    }

    private void init(Context context) {
        btnQ = findViewById(R.id.btn_kb_q);
        btnW = findViewById(R.id.btn_kb_w);
        btnE = findViewById(R.id.btn_kb_e);
        btnR = findViewById(R.id.btn_kb_r);
        btnT = findViewById(R.id.btn_kb_t);
        btnY = findViewById(R.id.btn_kb_y);
        btnU = findViewById(R.id.btn_kb_u);
        btnI = findViewById(R.id.btn_kb_i);
        btnO = findViewById(R.id.btn_kb_o);
        btnP = findViewById(R.id.btn_kb_p);
        btnA = findViewById(R.id.btn_kb_a);
        btnS = findViewById(R.id.btn_kb_s);
        btnD = findViewById(R.id.btn_kb_d);
        btnF = findViewById(R.id.btn_kb_f);
        btnG = findViewById(R.id.btn_kb_g);
        btnH = findViewById(R.id.btn_kb_h);
        btnJ = findViewById(R.id.btn_kb_j);
        btnK = findViewById(R.id.btn_kb_k);
        btnL = findViewById(R.id.btn_kb_l);
        btnZ = findViewById(R.id.btn_kb_z);
        btnX = findViewById(R.id.btn_kb_x);
        btnC = findViewById(R.id.btn_kb_c);
        btnV = findViewById(R.id.btn_kb_v);
        btnB = findViewById(R.id.btn_kb_b);
        btnN = findViewById(R.id.btn_kb_n);
        btnM = findViewById(R.id.btn_kb_m);
        btn1 = findViewById(R.id.btn_kb_1);
        btn2 = findViewById(R.id.btn_kb_2);
        btn3 = findViewById(R.id.btn_kb_3);
        btn4 = findViewById(R.id.btn_kb_4);
        btn5 = findViewById(R.id.btn_kb_5);
        btn6 = findViewById(R.id.btn_kb_6);
        btn7 = findViewById(R.id.btn_kb_7);
        btn8 = findViewById(R.id.btn_kb_8);
        btn9 = findViewById(R.id.btn_kb_9);
        btn0 = findViewById(R.id.btn_kb_0);
        btnBackspace = findViewById(R.id.btn_kb_backpack);
        btnSpace = findViewById(R.id.btn_kb_space);

        bindClickListener();

        keyValues.put(R.id.btn_kb_q, context.getString(R.string.keyboard_q));
        keyValues.put(R.id.btn_kb_w, context.getString(R.string.keyboard_w));
        keyValues.put(R.id.btn_kb_e, context.getString(R.string.keyboard_e));
        keyValues.put(R.id.btn_kb_r, context.getString(R.string.keyboard_r));
        keyValues.put(R.id.btn_kb_t, context.getString(R.string.keyboard_t));
        keyValues.put(R.id.btn_kb_y, context.getString(R.string.keyboard_y));
        keyValues.put(R.id.btn_kb_u, context.getString(R.string.keyboard_u));
        keyValues.put(R.id.btn_kb_i, context.getString(R.string.keyboard_i));
        keyValues.put(R.id.btn_kb_o, context.getString(R.string.keyboard_o));
        keyValues.put(R.id.btn_kb_p, context.getString(R.string.keyboard_p));
        keyValues.put(R.id.btn_kb_a, context.getString(R.string.keyboard_a));
        keyValues.put(R.id.btn_kb_s, context.getString(R.string.keyboard_s));
        keyValues.put(R.id.btn_kb_d, context.getString(R.string.keyboard_d));
        keyValues.put(R.id.btn_kb_f, context.getString(R.string.keyboard_f));
        keyValues.put(R.id.btn_kb_g, context.getString(R.string.keyboard_g));
        keyValues.put(R.id.btn_kb_h, context.getString(R.string.keyboard_h));
        keyValues.put(R.id.btn_kb_j, context.getString(R.string.keyboard_j));
        keyValues.put(R.id.btn_kb_k, context.getString(R.string.keyboard_k));
        keyValues.put(R.id.btn_kb_l, context.getString(R.string.keyboard_l));
        keyValues.put(R.id.btn_kb_z, context.getString(R.string.keyboard_z));
        keyValues.put(R.id.btn_kb_x, context.getString(R.string.keyboard_x));
        keyValues.put(R.id.btn_kb_c, context.getString(R.string.keyboard_c));
        keyValues.put(R.id.btn_kb_v, context.getString(R.string.keyboard_v));
        keyValues.put(R.id.btn_kb_b, context.getString(R.string.keyboard_b));
        keyValues.put(R.id.btn_kb_n, context.getString(R.string.keyboard_n));
        keyValues.put(R.id.btn_kb_m, context.getString(R.string.keyboard_m));
        keyValues.put(R.id.btn_kb_backpack, context.getString(R.string.keyboard_backspace));
        keyValues.put(R.id.btn_kb_space, context.getString(R.string.keyboard_space));
        keyValues.put(R.id.btn_kb_1, context.getString(R.string.keyboard_1));
        keyValues.put(R.id.btn_kb_2, context.getString(R.string.keyboard_2));
        keyValues.put(R.id.btn_kb_3, context.getString(R.string.keyboard_3));
        keyValues.put(R.id.btn_kb_4, context.getString(R.string.keyboard_4));
        keyValues.put(R.id.btn_kb_5, context.getString(R.string.keyboard_5));
        keyValues.put(R.id.btn_kb_6, context.getString(R.string.keyboard_6));
        keyValues.put(R.id.btn_kb_7, context.getString(R.string.keyboard_7));
        keyValues.put(R.id.btn_kb_8, context.getString(R.string.keyboard_8));
        keyValues.put(R.id.btn_kb_9, context.getString(R.string.keyboard_9));
        keyValues.put(R.id.btn_kb_0, context.getString(R.string.keyboard_0));
    }

    private void bindClickListener() {
        btnQ.setOnClickListener(this);
        btnW.setOnClickListener(this);
        btnE.setOnClickListener(this);
        btnR.setOnClickListener(this);
        btnT.setOnClickListener(this);
        btnY.setOnClickListener(this);
        btnU.setOnClickListener(this);
        btnI.setOnClickListener(this);
        btnO.setOnClickListener(this);
        btnP.setOnClickListener(this);
        btnA.setOnClickListener(this);
        btnS.setOnClickListener(this);
        btnD.setOnClickListener(this);
        btnF.setOnClickListener(this);
        btnG.setOnClickListener(this);
        btnH.setOnClickListener(this);
        btnJ.setOnClickListener(this);
        btnK.setOnClickListener(this);
        btnL.setOnClickListener(this);
        btnZ.setOnClickListener(this);
        btnX.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnV.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnN.setOnClickListener(this);
        btnM.setOnClickListener(this);
        btnBackspace.setOnClickListener(this);
        btnSpace.setOnClickListener(this);
        btnQ.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn0.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (inputConnection==null)
            return;
        int id = view.getId();
        if (id == R.id.btn_kb_backpack) {
            CharSequence selectedText = inputConnection.getSelectedText(0);

            if (TextUtils.isEmpty(selectedText)) {
                inputConnection.deleteSurroundingText(1, 0);
            } else {
                inputConnection.commitText("", 1);
            }
        } else {
            inputConnection.commitText(keyValues.get(view.getId()), 1);
        }
    }

    public void setInputConnection(InputConnection inputConnection) {
        this.inputConnection = inputConnection;
    }
}
