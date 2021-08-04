package com.mdcbeta.util;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Shakil Karim on 6/2/17.
 */

public abstract class MyBaseAdapter<T,B extends ViewDataBinding> extends RecyclerView.Adapter<MyBaseAdapter.MyViewHolder> {


    public List<T> data;
    public Context context;
    protected ItemClickListner<T> itemClickListner;

    public MyBaseAdapter(Context context) {
        this.data = new ArrayList<T>();
        this.context = context;
    }

    public MyBaseAdapter(Context context,ItemClickListner<T> itemClickListner) {
        this.data = new ArrayList<T>();
        this.context = context;
        this.itemClickListner = itemClickListner;
    }

    public void setItemClickListner(ItemClickListner<T> itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    public Context getContext() {
        return context;
    }

    public void updateData(List<T> datas) {
        data.clear();
        data.addAll(datas);
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =  LayoutInflater.from(parent.getContext());
        B binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);
        return new MyViewHolder(binding);
    }



    @Override
    public int getItemCount() {
        return (data == null) ? 0 : data.size() ;
    }



    @Override
    public  void onBindViewHolder(MyBaseAdapter.MyViewHolder holder, int position) {
        T obj = data.get(position);
        holder.bind(obj);
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }


    protected abstract int getLayoutIdForPosition(int position);

    protected abstract void bindData(B dataBinding,T item,int position);


     class MyViewHolder extends RecyclerView.ViewHolder {
        private final B binding;

        MyViewHolder(B binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(T obj) {
            bindData(binding,obj,getAdapterPosition());
            binding.executePendingBindings();
        }
    }


    public interface ItemClickListner<T> {
         void onItemClick(T item);
    }

}
