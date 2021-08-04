package com.mdcbeta.supportservice.Adapter;

import android.content.Context;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.mdcbeta.R;
import com.mdcbeta.util.Normalizer;
import com.mdcbeta.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION;


/**
 * Created by MahaRani on 12/11/2018.
 */

public class PharmacyAdapter extends RecyclerView.Adapter<PharmacyAdapter.MyCasesViewHolder> implements Filterable {
    private final Context context;

    private List<JsonObject> items;
    private PharmacyAdapter.ItemClickListner itemClickListner;
    private PharmacyAdapter.SpinnerClickListner spinnerClickListener;

    private String mQuery;
    private Filter mFilter;

    public PharmacyAdapter(Context context) {
        this.items = new ArrayList<>();
        this.context = context;
    }


    public void setItemClickListner(PharmacyAdapter.ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    public void setSpinnerClickListner(PharmacyAdapter.SpinnerClickListner spinnerClickListner) {
        this.spinnerClickListener = spinnerClickListner;
    }

    public void swap(List<JsonObject> datas) {
        items.clear();
        items.addAll(datas);
        mFilter = new PharmacyAdapter.CaseFilter(datas);
        notifyDataSetChanged();
    }

    public void add(List<JsonObject> datas) {
        items.addAll(datas);
        mFilter = new PharmacyAdapter.CaseFilter(datas);
        notifyDataSetChanged();
    }

    @Override
    public PharmacyAdapter.MyCasesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pharmacy_cases, parent, false);
        return new PharmacyAdapter.MyCasesViewHolder(v);

    }

    @Override
    public void onBindViewHolder(PharmacyAdapter.MyCasesViewHolder holder, int position) {

        try {
            JsonObject item = items.get(position);


            holder.textView14.setText("Case id:");
            holder.textView16.setText("Referred by:");
            holder.label_date_of_origin.setText("Date & Time");
            holder.txt_date_of_review.setText(item.get("status").getAsString());

            holder.txtName.setText(item.get("name").getAsString());
            if (item.get("case_code").getAsString().contains("no data") || TextUtils.isEmpty(item.get("case_code").getAsString())){
                holder.txtCaseId.setText("");
            }else
                holder.txtCaseId.setText(item.get("case_code").getAsString());

            holder.txtTime.setText(Utils.format12HourDateTime(Utils.UTCTolocalDateTime(item.get("created_at").getAsString())));


        } catch (Exception ex) {
            ex.printStackTrace();
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
        if(!(query == mQuery) || (query != null && query.equals(mQuery))) {
            if(getFilter() != null)
                getFilter().filter(query);
        }

    }

    public class MyCasesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_case_id)
        TextView txtCaseId;
        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.txt_time)
        TextView txtTime;
        @BindView(R.id.label_date_of_review)
        TextView label_date_of_review;
        @BindView(R.id.label_date_of_origin)
        TextView label_date_of_origin;
        @BindView(R.id.textView14)
        TextView textView14;
        @BindView(R.id.textView16)
        TextView textView16;
        @BindView(R.id.txt_date_of_review)
        TextView txt_date_of_review;
        @BindView(R.id.btn_view_case)
        TextView btn_view_case;



        public MyCasesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            btn_view_case.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != NO_POSITION) {
                    if (itemClickListner != null)
                        itemClickListner.onItemClick(items.get(pos));
                }
            });

            txt_date_of_review.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != NO_POSITION) {
                    if (spinnerClickListener != null)
                        spinnerClickListener.onSpinnerClick(items.get(pos));
                }
            });

        }

    }

    public interface ItemClickListner {
        public void onItemClick(JsonObject jsonObject);

    }

    public interface SpinnerClickListner {
        public void onSpinnerClick(JsonObject jsonObject);

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
            }
            else {
                list = new ArrayList<>();

                final String normalizedConstraint = Normalizer.forSearch(constraint);

                for (JsonObject item : mItems) {

                    String  recordId = item.get("case_code").getAsString();
                    String  fname = item.get("name").getAsString();


                    if (recordId.startsWith(normalizedConstraint) ||  recordId.contains(" " + normalizedConstraint)
                            ||  fname.startsWith(normalizedConstraint) || fname.contains(" " + normalizedConstraint))
                    {
                        list.add(item);

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

