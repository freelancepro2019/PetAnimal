package com.app.pet_animals.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pet_animals.R;
import com.app.pet_animals.databinding.UserPostRowBinding;
import com.app.pet_animals.models.PostModel;
import com.app.pet_animals.uis.activity_home_user.fragment_profile_user_module.FragmentOrdersUser;

import java.util.List;

public class UserPostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PostModel> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private Fragment fragment;

    public UserPostsAdapter(Context context, Fragment fragment) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.lang = lang;
        this.fragment = fragment;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        UserPostRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.user_post_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.addRate.setOnClickListener(view -> {
            if (fragment instanceof FragmentOrdersUser) {
                FragmentOrdersUser fragmentOrdersUser = (FragmentOrdersUser) fragment;
                fragmentOrdersUser.createRateSheetDialog(list.get(myHolder.getAdapterPosition()), myHolder.getAdapterPosition());
            }
        });

        myHolder.binding.call.setOnClickListener(view -> {
            if (fragment instanceof FragmentOrdersUser) {
                FragmentOrdersUser fragmentOrdersUser = (FragmentOrdersUser) fragment;
                String phone = list.get(myHolder.getAdapterPosition()).getUser_phone();
                fragmentOrdersUser.call(phone);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private UserPostRowBinding binding;

        public MyHolder(UserPostRowBinding binding) {
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
