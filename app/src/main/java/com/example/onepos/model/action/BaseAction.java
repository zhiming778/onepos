package com.example.onepos.model.action;

import android.content.Context;

public abstract class BaseAction {

    private Context context;
    BaseAction(Context context) {
        this.context = context;
    }

}
