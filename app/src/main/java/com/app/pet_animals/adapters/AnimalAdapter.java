package com.app.pet_animals.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pet_animals.R;
import com.app.pet_animals.databinding.AnimalRowBinding;
import com.app.pet_animals.databinding.ServiceRowBinding;
import com.app.pet_animals.models.AnimalModel;
import com.app.pet_animals.models.UserModel;
import com.app.pet_animals.uis.activity_home_user.fragment_profile_user_module.FragmentAnimalsUser;

import java.util.List;

public class AnimalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AnimalModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public AnimalAdapter(Context context, Fragment fragment) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        AnimalRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.animal_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.delete.setOnClickListener(view -> {
            if (fragment instanceof FragmentAnimalsUser){
                FragmentAnimalsUser fragmentAnimalsUser = (FragmentAnimalsUser) fragment;
                fragmentAnimalsUser.delete(list.get(myHolder.getAdapterPosition()),myHolder.getAdapterPosition());
            }
        });

        myHolder.binding.edit.setOnClickListener(view -> {
            if (fragment instanceof FragmentAnimalsUser){
                FragmentAnimalsUser fragmentAnimalsUser = (FragmentAnimalsUser) fragment;
                fragmentAnimalsUser.update(list.get(myHolder.getAdapterPosition()),myHolder.getAdapterPosition());
            }
        });


    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }



    public static class MyHolder extends RecyclerView.ViewHolder {
        private AnimalRowBinding binding;

        public MyHolder(AnimalRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }

    public void updateList(List<AnimalModel> list) {
        if (list != null) {
            this.list = list;
        }
        notifyDataSetChanged();
    }
    public void removeItem(int adapterPosition) {
        if (list != null) {
            list.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
        }
    }

    public List<AnimalModel> getList(){
        return this.list;
    }

}
