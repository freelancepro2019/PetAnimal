package com.app.pet_animals.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pet_animals.R;
import com.app.pet_animals.databinding.ServiceRowBinding;
import com.app.pet_animals.models.UserModel;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UserModel> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    public ServiceAdapter(Context context, String lang) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.lang = lang;
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
