package com.app.pet_animals.uis.activity_base;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.pet_animals.language.Language;
import com.app.pet_animals.models.UserModel;
import com.app.pet_animals.preferences.Preferences;

import io.paperdb.Paper;

public class ActivityBase extends AppCompatActivity {
    public static final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected String getLang() {
        Paper.init(this);
        String lang = Paper.book().read("lang", "ar");
        return lang;
    }

    protected UserModel getUserModel() {
        Preferences preferences = Preferences.getInstance();
        return preferences.getUserData(this);
    }

    protected void setUserModel(UserModel userModel) {
        Preferences preferences = Preferences.getInstance();
        preferences.createUpdateUserData(this, userModel);
    }

    protected void clearUserModel() {
        Preferences preferences = Preferences.getInstance();
        preferences.clearUserData(this);

    }


}
