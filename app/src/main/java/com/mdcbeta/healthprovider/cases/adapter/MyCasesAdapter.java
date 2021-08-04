package com.mdcbeta.healthprovider.cases.adapter;

import com.google.gson.JsonObject;

import android.content.Context;
import android.graphics.Color;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.mdcbeta.R;
import com.mdcbeta.healthprovider.MainHealthProviderActivity;
import com.mdcbeta.util.Normalizer;
import com.mdcbeta.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION;


/**
 * Created by Shakil Karim on 4/4/17.
 */
public class MyCasesAdapter extends RecyclerView.Adapter<MyCasesAdapter.MyCasesViewHolder> implements Filterable {
    private final Context context;

    private List<JsonObject> items;
    private ItemClickListner itemClickListner;
    private String mQuery;
    private Filter mFilter;

    public MyCasesAdapter(Context context) {
        this.items = new ArrayList<>();
        this.context = context;
    }


    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;

    }


    public void swap(List<JsonObject> datas) {
        items.clear();
        items.addAll(datas);
        mFilter = new CaseFilter(datas);
        notifyDataSetChanged();
    }

    public void add(List<JsonObject> datas) {
        items.addAll(datas);
        mFilter = new CaseFilter(datas);
        notifyDataSetChanged();
    }

    @Override
    public MyCasesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_cases, parent, false);

return new MyCasesViewHolder(v);

    }

    @Override
    public void onBindViewHolder(MyCasesViewHolder holder, int position) {


        if (MainHealthProviderActivity.isReviewer) {



            try {

                JsonObject item = items.get(position);





                holder.label_date_of_review.setVisibility(View.GONE);
                holder.txt_date_of_review.setVisibility(View.GONE);

                holder.txtCaseId.setText(item.get("case_code").getAsString());
                holder.reffer_name.setText(item.get("name").getAsString());
                holder.txtName.setText(item.get("fname").getAsString() + " " + item.get("lname").getAsString());
                holder.txtLocation.setText(item.get("area").getAsString());
                holder.txtTime.setText(Utils.UTCTolocalDate(item.get("created_at").getAsString()));

//                if (!item.has("reffername")) {
//
//                    holder.reffer_name.setText(item.get("name").getAsString());
//                    holder.txtName.setText(item.get("fname").getAsString() + " " + item.get("lname").getAsString());
//                    if (item.get("case_code").getAsString().contains("no data") || TextUtils.isEmpty(item.get("case_code").getAsString())) {
//                        holder.txtCaseId.setText("");
//                    } else
//                        holder.txtCaseId.setText(item.get("case_code").getAsString());
//
//                    holder.txtLocation.setText(item.get("area").getAsString());
//                    holder.txtTime.setText(Utils.UTCTolocalDate(item.get("created_at").getAsString()));
//
//                } else {
//
//                    holder.group_name.setText(item.get("groupname").getAsString());
//                    holder.group_name.setBackgroundColor(Color.parseColor(item.get("groupcolor").getAsString()));
//                    holder.reffer_name.setText(item.get("reffername").getAsString());
//                    holder.txtName.setText(item.get("fname").getAsString() + " " + item.get("lname").getAsString());
//                    holder.txtLocation.setText(item.get("area").getAsString());
//                    holder.txtTime.setText(Utils.UTCTolocalDate(item.get("created_at").getAsString()));
//
//
//                    if (item.get("case_code").getAsString().contains("no data")) {
//                        holder.txtCaseId.setText("");
//                    } else
//                        holder.txtCaseId.setText(item.get("case_code").getAsString());
//                    holder.txtLocation.setText(item.get("area").getAsString());
//                           holder.txtTime.setText(Utils.format12HourDateTime(Utils.UTCTolocalDateTime(Utils.formatedDate((item.get("created_at").getAsString())))));
//                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            try {

                JsonObject item = items.get(position);


                holder.textView11.setText("Referred to:");
                holder.txtCaseId.setText(item.get("case_code").getAsString());
                holder.txtName.setText(item.get("fname").getAsString() + " " + item.get("lname").getAsString());
                holder.txtLocation.setText(item.get("area").getAsString());
                holder.reffer_name.setText(item.get("name").getAsString());
                //    holder.txtTime.setText(Utils.format12HourDateTime(Utils.UTCTolocalDateTime(Utils.formatedDate((item.get("created_at").getAsString())))));
                holder.txt_date_of_review.setText(Utils.format12HourDate(Utils.UTCTolocalDate(item.get("updated_at").getAsString())));
                holder.txtTime.setText(Utils.UTCTolocalDate(item.get("created_at").getAsString()));
                if (item.get("case_code").getAsString().contains("no data") || TextUtils.isEmpty(item.get("case_code").getAsString())) {
                    holder.txtCaseId.setText("");
                } else
                    holder.txtCaseId.setText(item.get("case_code").getAsString());
                holder.reffer_name.setText(item.get("reffername").getAsString());
                holder.textView18.setText("Address");

                holder.txtLocation.setText(item.get("area").getAsString());
                holder.txt_date_of_review.setText(Utils.UTCTolocalDate(item.get("updated_at").getAsString()));
                holder.txtTime.setText(Utils.UTCTolocalDate(item.get("created_at").getAsString()));

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }


    public void setQuery(String query) {
        if (!(query == mQuery) || (query != null && query.equals(mQuery))) {
            if (getFilter() != null)
                getFilter().filter(query);
        }

    }

    public class MyCasesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_case_id)
        TextView txtCaseId;
        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.txt_location)
        TextView txtLocation;
        @BindView(R.id.txt_time)
        TextView txtTime;
        @BindView(R.id.reffer_name)
        TextView reffer_name;
        @BindView(R.id.textView11)
        TextView textView11;
        @BindView(R.id.group_name)
        TextView group_name;
        @BindView(R.id.label_date_of_review)
        TextView label_date_of_review;
        @BindView(R.id.txt_date_of_review)
        TextView txt_date_of_review;
        @BindView(R.id.textView18)
        TextView textView18;


        public MyCasesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != NO_POSITION) {
                    if (itemClickListner != null)
                        itemClickListner.onItemClick(items.get(pos));

                    //itemView.setBackground(cha);





                }
            });
        }

    }

    public interface ItemClickListner {
        public void onItemClick(JsonObject jsonObject);

    }


    private final class CaseFilter extends Filter {

        private final List<JsonObject> mItems;

        private CaseFilter(List<JsonObject> items) {
            mItems = new ArrayList<JsonObject>(items);
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final List<JsonObject> list;

            if (TextUtils.isEmpty(constraint)) {
                list = mItems;
            } else {
                list = new ArrayList<>();

                String normalizedConstraint = Normalizer.forSearch(constraint);

                for (JsonObject item : mItems) {

                    String recordId = "";
                    String area = "";
                    if (!item.get("case_code").isJsonNull())
                        recordId = item.get("case_code").getAsString();
                    String fname = item.get("fname").getAsString();
                    if (!item.get("area").isJsonNull())
                        area = item.get("area").getAsString();

                    if (recordId.startsWith(normalizedConstraint)) {
                        list.add(item);
                    } else {
                        if (fname.contains(normalizedConstraint) ||
                                area.contains(normalizedConstraint)) {
                            list.add(item);
                        }
                    }
                }

            }

            final FilterResults results = new FilterResults();
            results.values = list;
            results.count = list.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            items = (List<JsonObject>) results.values;
            mQuery = constraint != null ? constraint.toString() : null;
            notifyDataSetChanged();
        }



    }

}
