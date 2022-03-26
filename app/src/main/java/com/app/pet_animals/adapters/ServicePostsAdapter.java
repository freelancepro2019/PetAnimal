package com.app.pet_animals.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pet_animals.R;
import com.app.pet_animals.databinding.ServicePostRowBinding;
import com.app.pet_animals.databinding.UserPostRowBinding;
import com.app.pet_animals.models.PostModel;
import com.app.pet_animals.uis.activity_home_service.fragment_home_service_module.FragmentOrdersService;
import com.app.pet_animals.uis.activity_home_user.fragment_profile_user_module.FragmentOrdersUser;

import java.util.List;

public class ServicePostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PostModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public ServicePostsAdapter(Context context, Fragment fragment) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ServicePostRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.service_post_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.btnAction.setOnClickListener(view -> {
            if (fragment instanceof FragmentOrdersService) {
                FragmentOrdersService fragmentOrdersService = (FragmentOrdersService) fragment;
                fragmentOrdersService.createAppointmentCalender(list.get(myHolder.getAdapterPosition()));
            }
        });

        myHolder.binding.btnRefuse.setOnClickListener(view -> {
            if (fragment instanceof FragmentOrdersService) {
                FragmentOrdersService fragmentOrdersService = (FragmentOrdersService) fragment;
                fragmentOrdersService.refuseOrder(list.get(myHolder.getAdapterPosition()), myHolder.getAdapterPosition());
            }
        });

        myHolder.binding.btnEnd.setOnClickListener(view -> {
            if (fragment instanceof FragmentOrdersService) {
                FragmentOrdersService fragmentOrdersService = (FragmentOrdersService) fragment;
                fragmentOrdersService.endOrder(list.get(myHolder.getAdapterPosition()), myHolder.getAdapterPosition());
            }
        });


        myHolder.binding.call.setOnClickListener(view -> {
            if (fragment instanceof FragmentOrdersService) {
                FragmentOrdersService fragmentOrdersService = (FragmentOrdersService) fragment;
                String phone = list.get(myHolder.getAdapterPosition()).getUser_phone();
                fragmentOrdersService.call(phone);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private ServicePostRowBinding binding;

        public MyHolder(ServicePostRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }

    public void updateList(List<PostModel> list) {
        if (list != null) {
            this.list = list;
        }
        notifyDataSetChanged();
    }

    public void updateItem(PostModel offerModel, int pos) {
        if (list != null) {
            list.set(pos, offerModel);
            notifyItemChanged(pos);
        }
    }

}
