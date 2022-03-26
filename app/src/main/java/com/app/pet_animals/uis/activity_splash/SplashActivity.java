package com.app.pet_animals.uis.activity_splash;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.app.pet_animals.R;
import com.app.pet_animals.databinding.ActivitySplashBinding;
import com.app.pet_animals.tags.Tags;
import com.app.pet_animals.uis.activity_base.ActivityBase;
import com.app.pet_animals.uis.activity_home_service.HomeActivityService;
import com.app.pet_animals.uis.activity_home_user.HomeActivityUser;
import com.app.pet_animals.uis.activity_login.LoginActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends ActivityBase {
    private ActivitySplashBinding binding;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        initView();
    }

    private void initView() {
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        if (getUserModel() == null) {
                            navigateToLoginActivity();
                        } else {
                            navigateToHomeActivity();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void navigateToHomeActivity() {
        String user_type = getUserModel().getUser_type();
        Intent intent = null;
        if (user_type.equals(Tags.user_animal_owner)) {
            intent = new Intent(this, HomeActivityUser.class);
        } else {
            intent = new Intent(this, HomeActivityService.class);
        }
        startActivity(intent);
        finish();
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}