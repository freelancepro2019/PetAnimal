package com.app.pet_animals.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.app.pet_animals.R;
import com.app.pet_animals.databinding.SpinnerRowBinding;
import com.app.pet_animals.models.AnimalModel;

import java.util.List;

public class AnimalSpinnerAdapter extends BaseAdapter {
    private List<AnimalModel> list;
    private Context context;
    private LayoutInflater inflater;

    public AnimalSpinnerAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list!=null?list.size():0;
    }

    @Override
    public Object getItem(int i) {
        return list!=null?list.get(i):null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void updateList(List<AnimalModel> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") SpinnerRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.spinner_row,viewGroup,false);
        binding.setTitle(list.get(i).getAnimal_name());
        return binding.getRoot();
    }
}
