package com.example.onepos.view.activity;

import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.onepos.view.fragment.PasswordDialogFragment;
import com.example.onepos.viewmodel.BaseViewModel;
import com.example.onepos.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.schedulers.SchedulerPoolFactory;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseActivity<T extends BaseViewModel> extends AppCompatActivity{
    T viewModel;

    @Inject
    ViewModelFactory factory;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        super.onCreate(savedInstanceState);
        initLayout();
        restartSchedulers();
        bindObserver();
    }

    abstract void initLayout() ;

    void showPasswordDialog(Class viewmodelClass, int actionType, PasswordDialogFragment.OnPasswordListener listener) {
        PasswordDialogFragment fragment = PasswordDialogFragment.newInstance(viewmodelClass, actionType);
        fragment.setListener(listener);
        fragment.show(getSupportFragmentManager(), PasswordDialogFragment.TAG);
    }

    private void restartSchedulers() {
        AndroidSchedulers.mainThread().shutdown();
        Schedulers.io().shutdown();
        SchedulerPoolFactory.shutdown();
        Schedulers.io().start();
        AndroidSchedulers.mainThread().start();
    }

    void dismissPasswordDialog() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(PasswordDialogFragment.TAG);
        if (fragment != null)
            ((PasswordDialogFragment)fragment).dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeObserver();
    }

    abstract void bindObserver();
    abstract void removeObserver();
}
