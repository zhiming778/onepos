package com.example.onepos.view.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.onepos.model.ItemTranslation;
import com.example.onepos.model.Staff;
import com.example.onepos.view.fragment.PasswordDialogFragment;
import com.example.onepos.viewmodel.BaseViewModel;
import com.example.onepos.viewmodel.ViewModelFactory;

import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.internal.schedulers.SchedulerPoolFactory;
import io.reactivex.rxjava3.schedulers.Schedulers;

public abstract class BaseActivity<T extends BaseViewModel> extends AppCompatActivity{
    T viewModel;
    static final String EXTRA_STAFF = "extra_staff";

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

    void changeLanguage() {
        final Staff staff = viewModel.getStaff();
        if (staff==null)
            return;
        final int lang = staff.getLang();
        final Configuration config = getResources().getConfiguration();
        if (lang == ItemTranslation.LANG_US)
            config.setLocale(Locale.US);
        if (lang == ItemTranslation.LANG_CN)
            config.setLocale(Locale.CHINA);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

    }
    abstract void bindObserver();
    abstract void removeObserver();
}
