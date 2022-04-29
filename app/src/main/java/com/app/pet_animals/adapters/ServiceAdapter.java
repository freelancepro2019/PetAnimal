package com.app.pet_animals.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pet_animals.R;
import com.app.pet_animals.databinding.ServiceRowBinding;
import com.app.pet_animals.models.UserModel;
import com.app.pet_animals.uis.activity_home_user.fragment_home_user_module.FragmentDoctorsUser;
import com.app.pet_animals.uis.activity_home_user.fragment_home_user_module.FragmentHomeUser;
import com.app.pet_animals.uis.activity_home_user.fragment_home_user_module.FragmentUserSearch;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UserModel> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private Fragment fragment;
    public ServiceAdapter(Context context, String lang,Fragment fragment) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.lang = lang;
        this.fragment = fragment;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ServiceRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.service_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.setLang(lang);

        myHolder.itemView.setOnClickListener(view -> {
            if (fragment instanceof FragmentHomeUser){
                FragmentHomeUser fragmentHomeUser = (FragmentHomeUser) fragment;
                fragmentHomeUser.navigateToSendPosActivity(list.get(myHolder.getAdapterPosition()));
            }else  if (fragment instanceof FragmentDoctorsUser){
                FragmentDoctorsUser fragmentDoctorsUser = (FragmentDoctorsUser) fragment;
                fragmentDoctorsUser.navigateToSendPosActivity(list.get(myHolder.getAdapterPosition()));
            }else if (fragment instanceof FragmentUserSearch){
                FragmentUserSearch fragmentUserSearch = (FragmentUserSearch) fragment;
                fragmentUserSearch.navigateToSendPosActivity(list.get(myHolder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private ServiceRowBinding binding;

        public MyHolder(ServiceRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }

    public void updateList(List<UserModel> list){
        if (list!=null){
            this.list = list;
        }
        notifyDataSetChanged();
    }

}
