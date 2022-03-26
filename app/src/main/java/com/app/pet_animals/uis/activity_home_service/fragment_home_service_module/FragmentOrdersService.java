package com.app.pet_animals.uis.activity_home_service.fragment_home_service_module;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.pet_animals.R;
import com.app.pet_animals.adapters.ServicePostsAdapter;
import com.app.pet_animals.adapters.UserPostsAdapter;
import com.app.pet_animals.databinding.BottomSheetRateBinding;
import com.app.pet_animals.databinding.FragmentHomeBaseServiceBinding;
import com.app.pet_animals.databinding.FragmentHomeServiceBinding;
import com.app.pet_animals.databinding.FragmentOrdersUserBinding;
import com.app.pet_animals.models.FavoriteModel;
import com.app.pet_animals.models.PostModel;
import com.app.pet_animals.models.RateModel;
import com.app.pet_animals.tags.Common;
import com.app.pet_animals.tags.Tags;
import com.app.pet_animals.uis.activity_base.FragmentBase;
import com.app.pet_animals.uis.activity_home_service.HomeActivityService;
import com.app.pet_animals.uis.activity_home_user.HomeActivityUser;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class FragmentOrdersService extends FragmentBase implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private HomeActivityService activity;
    private FragmentHomeServiceBinding binding;
    private ServicePostsAdapter adapter;
    private DatabaseReference dRef;
    private PostModel postModel;
    private String appointment_date = "";
    private String appointment_time = "";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivityService) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_service, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        binding.recViewLayout.tvNoData.setText(R.string.no_orders);
        binding.recViewLayout.tvNoData.setVisibility(View.GONE);
        adapter = new ServicePostsAdapter(activity, this);
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewLayout.recView.setAdapter(adapter);
        binding.recViewLayout.swipeRefresh.setOnRefreshListener(this::getPosts);
        getPosts();
    }

    private void createDateDialog() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, 1);
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setAccentColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        datePickerDialog.setTitle(getString(R.string.av_date));
        datePickerDialog.setMinDate(now);
        datePickerDialog.dismissOnPause(true);
        datePickerDialog.setCancelColor(ContextCompat.getColor(activity, R.color.gray4));
        datePickerDialog.setCancelText(getString(R.string.cancel));
        datePickerDialog.setOkColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        datePickerDialog.setOkText(R.string.select);
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        try {
            datePickerDialog.show(activity.getFragmentManager(), "");

        } catch (Exception e) {
        }
    }

    private void createTimeDialog() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false);
        timePickerDialog.setAccentColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        timePickerDialog.setTitle(getString(R.string.av_time));
        timePickerDialog.dismissOnPause(true);
        timePickerDialog.setCancelColor(ContextCompat.getColor(activity, R.color.gray4));
        timePickerDialog.setCancelText(getString(R.string.cancel));
        timePickerDialog.setOkColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        timePickerDialog.setOkText(R.string.select);
        timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_1);
        try {
            timePickerDialog.show(activity.getFragmentManager(), "");

        } catch (Exception e) {
        }
    }

    private void getPosts() {
        binding.recViewLayout.swipeRefresh.setRefreshing(true);
        dRef = FirebaseDatabase.getInstance().getReference();
        Query query = dRef.child(Tags.table_posts)
                .orderByChild("service_id")
                .equalTo(getUserModel().getUser_id());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.recViewLayout.swipeRefresh.setRefreshing(false);
                List<PostModel> list = new ArrayList<>();
                if (snapshot.getValue() != null) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        PostModel postModel = ds.getValue(PostModel.class);
                        if (postModel != null) {
                            list.add(postModel);
                        }
                    }

                    if (list.size() > 0) {
                        Collections.reverse(list);
                        adapter.updateList(list);
                        binding.recViewLayout.tvNoData.setVisibility(View.GONE);
                    } else {
                        binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);

                    }
                } else {
                    binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void createAppointmentCalender(PostModel model) {
        this.postModel = model;
        createDateDialog();
    }

    public void acceptOrder(PostModel postModel) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        postModel.setPost_status(Tags.status_accepted);
        postModel.setPost_available_date(appointment_date);
        postModel.setPost_available_time(appointment_time);
        dRef = FirebaseDatabase.getInstance().getReference();
        dRef.child(Tags.table_posts)
                .child(postModel.getPost_id())
                .setValue(postModel)
                .addOnSuccessListener(unused -> {
                    dialog.dismiss();
                }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }

    public void refuseOrder(PostModel postModel, int adapterPosition) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        postModel.setPost_status(Tags.status_refused);
        dRef = FirebaseDatabase.getInstance().getReference();
        dRef.child(Tags.table_posts)
                .child(postModel.getPost_id())
                .setValue(postModel)
                .addOnSuccessListener(unused -> {
                    dialog.dismiss();
                }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    public void endOrder(PostModel postModel, int adapterPosition) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        postModel.setPost_status(Tags.status_completed);
        dRef = FirebaseDatabase.getInstance().getReference();
        dRef.child(Tags.table_posts)
                .child(postModel.getPost_id())
                .setValue(postModel)
                .addOnSuccessListener(unused -> {
                    dialog.dismiss();
                }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }


    public void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        appointment_date = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH).format(calendar.getTime());
        createTimeDialog();
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        appointment_time = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(calendar.getTime());
        acceptOrder(postModel);
    }

}