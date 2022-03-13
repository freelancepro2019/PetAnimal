package com.app.pet_animals.general_ui;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.app.pet_animals.R;
import com.app.pet_animals.tags.Tags;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class GeneralMethod {

    @BindingAdapter("error")
    public static void errorValidation(View view, String error) {
        if (view instanceof EditText) {
            EditText ed = (EditText) view;
            ed.setError(error);
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setError(error);


        }
    }

    @BindingAdapter("userImage")
    public static void user_image(CircleImageView view, String userImage) {
        if (userImage!=null&&!userImage.isEmpty()){
            Picasso.get().load(Uri.parse(userImage)).into(view);

        }else {
            Picasso.get().load(R.drawable.avatar).into(view);

        }
    }

    @BindingAdapter("animalImage")
    public static void animal_image(View view, String animalImage) {
        if (animalImage!=null){
            if (view instanceof CircleImageView){
                CircleImageView circleImageView = (CircleImageView) view;
                Picasso.get().load(Uri.parse(animalImage)).into(circleImageView);

            }else if (view instanceof ImageView){
                ImageView imageView = (ImageView) view;
                Picasso.get().load(Uri.parse(animalImage)).into(imageView);

            }
        }

    }
    @BindingAdapter("userType")
    public static void user_type(TextView view, String userType) {
        if (userType!=null){
            if (userType.equals(Tags.user_animal_owner)){
                view.setText(R.string.animal_owner);
            }else if (userType.equals(Tags.user_housing_owner)){
                view.setText(R.string.housing_owner);
            }else if (userType.equals(Tags.user_doctor)){
                view.setText(R.string.doctor);
            }
        }

    }



}










