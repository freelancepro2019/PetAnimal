package com.app.pet_animals.models;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.app.pet_animals.BR;
import com.app.pet_animals.R;
import com.app.pet_animals.tags.Tags;

import java.io.Serializable;

public class AddAnimalModel extends BaseObservable implements Serializable {
    private String imagePath;
    private String name;
    private String specialization;
    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_specialization = new ObservableField<>();

    public AddAnimalModel() {
        imagePath = "";
        name = "";
        specialization = "";


    }

    public boolean isDataValid(Context context) {
        if (!imagePath.isEmpty() &&
                !name.isEmpty() &&
                !specialization.isEmpty()


        ) {

            error_name.set(null);
            error_specialization.set(null);

            return true;
        } else {

            if (imagePath.isEmpty()) {
                Toast.makeText(context, R.string.ch_photo, Toast.LENGTH_SHORT).show();
            }

            if (name.isEmpty()) {
                error_name.set(context.getString(R.string.field_required));
            } else {
                error_name.set(null);
            }

            if (specialization.isEmpty()) {
                error_specialization.set(context.getString(R.string.field_required));
            } else {
                error_specialization.set(null);
            }


            return false;
        }

    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getSpecialization() {
        return specialization;

    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
        notifyPropertyChanged(BR.specialization);
    }


}
