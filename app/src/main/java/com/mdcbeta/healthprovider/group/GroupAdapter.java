package com.mdcbeta.healthprovider.group;

import android.content.Context;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mdcbeta.R;
import com.mdcbeta.data.remote.model.CreateGroup;
import com.mdcbeta.util.AppPref;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION;


/**
 * Created by Shakil Karim on 4/23/17.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    private final Context context;

    public List<CreateGroup> mlist;
    private ItemClickListner itemClickListner;
    private ItemDelete itemDelete;

    public void setItemDelete(ItemDelete itemDelete) {
        this.itemDelete = itemDelete;
    }

    public GroupAdapter(Context context) {
        this.mlist = new ArrayList<>();
        this.context = context;
    }

    public void swap(List<CreateGroup> datas) {
        mlist.clear();
        mlist.addAll(datas);
        notifyDataSetChanged();
    }


    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        return new GroupViewHolder(v);

    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
       try {
           CreateGroup item = mlist.get(position);
           holder.txtName.setText(item.getName());
           holder.viewColor.setBackgroundColor(Color.parseColor(item.getColor()));
           holder.txtType.setText(item.getPermission());

           if (item.getUserId().equals(AppPref.getInstance(context).getUser().id)){
               holder.btn_delete.setVisibility(View.VISIBLE);
               holder.view_group.setVisibility(View.VISIBLE);
               holder.edit_group.setVisibility(View.INVISIBLE);
               holder.add_member.setVisibility(View.VISIBLE);
           }else {

               if(item.getPermission().equalsIgnoreCase("Public")){
                 //  holder.btn_delete.setVisibility(View.GONE);
                 //  holder.view_group.setVisibility(View.VISIBLE);
                //   holder.edit_group.setVisibility(View.GONE);
                //   holder.add_member.setVisibility(View.VISIBLE);

               }else {
                //   holder.btn_delete.setVisibility(View.GONE);
                //   holder.view_group.setVisibility(View.VISIBLE);
                //   holder.edit_group.setVisibility(View.GONE);
                //   holder.add_member.setVisibility(View.GONE);
               }

           }

       }catch (Exception ex){}

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }


    public class GroupViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.txt_type)
        TextView txtType;
        @BindView(R.id.view_color)
        TextView viewColor;

        @BindView(R.id.btn_delete)
        ImageView btn_delete;
        @BindView(R.id.view_group)
        ImageView view_group;
        @BindView(R.id.edit_group)
        ImageView edit_group;
        @BindView(R.id.add_member)
        ImageView add_member;

        public GroupViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            btn_delete.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != NO_POSITION) {
                    if (itemClickListner != null)
                        itemClickListner.onItemClick(v,mlist.get(pos));

                }
            });
            edit_group.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != NO_POSITION) {
                    if (itemClickListner != null)
                        itemClickListner.onItemClick(v,mlist.get(pos));

                }
            });

            add_member.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != NO_POSITION) {
                    if (itemClickListner != null)
                        itemClickListner.onItemClick(v,mlist.get(pos));

                }
            });

            view_group.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != NO_POSITION) {
                    if (itemClickListner != null)
                        itemClickListner.onItemClick(v,mlist.get(pos));

                }
            });

        }

    }

    public interface ItemClickListner {
        public void onItemClick(View view,CreateGroup item);
    }

    public interface ItemDelete {
        public void onClick(int pos);
    }
}
