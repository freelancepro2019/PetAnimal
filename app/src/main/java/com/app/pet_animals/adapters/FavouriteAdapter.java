package com.app.pet_animals.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.app.pet_animals.R;
import com.app.pet_animals.databinding.FavoriteRowBinding;
import com.app.pet_animals.models.FavoriteModel;
import com.app.pet_animals.uis.activity_home_user.fragment_profile_user_module.FragmentFavoriteUser;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<FavoriteModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public FavouriteAdapter(Context context, Fragment fragment) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        FavoriteRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.favorite_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.imageUnFav.setOnClickListener(view -> {
            if (fragment instanceof FragmentFavoriteUser){
                FragmentFavoriteUser fragmentFavorite = (FragmentFavoriteUser) fragment;
                fragmentFavorite.unFavorite(list.get(myHolder.getAdapterPosition()));
            }
        });

        myHolder.binding.call.setOnClickListener(view -> {
            if (fragment instanceof FragmentFavoriteUser){
                FragmentFavoriteUser fragmentFavorite = (FragmentFavoriteUser) fragment;
                String phone = list.get(myHolder.getAdapterPosition()).getService_phone();
                fragmentFavorite.call(phone);
            }
        });

        myHolder.itemView.setOnClickListener(view -> {
            if (fragment instanceof FragmentFavoriteUser){
                FragmentFavoriteUser fragmentFavorite = (FragmentFavoriteUser) fragment;
                fragmentFavorite.navigateToSendPosActivity(list.get(myHolder.getAdapterPosition()));
            }
        });


    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private FavoriteRowBinding binding;

        public MyHolder(FavoriteRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }

    public void updateList(List<FavoriteModel> list){
        if (list!=null){
            this.list = list;
        }
        notifyDataSetChanged();
    }

}
