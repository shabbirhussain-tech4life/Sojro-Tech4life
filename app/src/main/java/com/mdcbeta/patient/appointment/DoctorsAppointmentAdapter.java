package com.mdcbeta.patient.appointment;

import android.content.Loader;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.mdcbeta.R;
import com.mdcbeta.data.remote.model.AppointmentDoctor;
import com.mdcbeta.di.AppModule;
import com.mdcbeta.healthprovider.appointment.HealthProviderAppointmentAdapter;
import com.mdcbeta.util.Normalizer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;




/**
 * Created by Shakil Karim on 4/2/17.
 */

public class DoctorsAppointmentAdapter
        extends RecyclerView.Adapter<DoctorsAppointmentAdapter.DoctorsAppointmentViewHolder>
        implements Filterable
{

    private ItemClickListener itemClickListener;
    private ItemProfileListener itemProfileListener;
    private ItemReviewerListener itemReviewerListener;
    private List<AppointmentDoctor> doctorList;
    private String mQuery;
    private String mNameQuery;
    private String mLocationQuery;
    private Filter mFilter;
    private Filter mNameFilter;
    private Filter mLocationFilter;

    public DoctorsAppointmentAdapter() {
        doctorList = new ArrayList<>();

    }

    public void swap(List<AppointmentDoctor> datas) {
        doctorList.clear();
        doctorList.addAll(datas);
        mFilter = new SpecialityFilter(doctorList);
        mNameFilter = new DrNameFilter(doctorList);
        mLocationFilter = new DrLocationFilter(doctorList);
        notifyDataSetChanged();
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setItemProfileListener(ItemProfileListener itemClickListener) {
        this.itemProfileListener = itemClickListener;
    }

    public void setItemReviewerListener(ItemReviewerListener itemClickListener) {
        this.itemReviewerListener = itemClickListener;
    }


    @Override
    public DoctorsAppointmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View contactView = inflater.inflate(R.layout.item_consultations, parent, false);
        DoctorsAppointmentViewHolder viewHolder = new DoctorsAppointmentViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DoctorsAppointmentViewHolder holder, int position) {

        AppointmentDoctor doctor = doctorList.get(position);
        holder.name.setText(doctor.name);
        holder.type.setText(doctor.speciality);
        holder.profileImage.setImageResource(R.drawable.placeholder);
        holder.txt_location.setText(doctor.country);
        holder.txt_price.setText(doctor.currency + doctor.slotPrice + "/Appointment");

//        Picasso.with()
//                .load(AppModule.profileimage + doctor.image)
//                .placeholder(R.drawable.image_placeholder).into(holder.profileImage);

        Picasso.with(holder.profileImage.getContext()).load(AppModule.profileimage + doctor.image)
                .placeholder(R.drawable.image_placeholder).into(holder.profileImage);
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public Filter getmNameFilter() {
        return mNameFilter;
    }

    public Filter getmLocationFilter() {
        return mLocationFilter;
    }

    public class DoctorsAppointmentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.profile_image)
        CircleImageView profileImage;
        @BindView(R.id.txt_name)
        TextView name;
        @BindView(R.id.txt_type)
        TextView type;
        @BindView(R.id.view_slots)
        TextView view_slots;
        @BindView(R.id.txt_location)
        TextView txt_location;
        @BindView(R.id.view_profile)
        TextView view_profile;
        @BindView(R.id.txt_price)
        TextView txt_price;
        @BindView(R.id.view_review)
        TextView view_review;

        public DoctorsAppointmentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            view_slots.setOnClickListener(v -> {
                if (itemClickListener != null){}
                   itemClickListener.clickItem(doctorList.get(getAdapterPosition()));



            });

            view_profile.setOnClickListener(v -> {
                if (itemProfileListener != null)
                    itemProfileListener.clickItem(doctorList.get(getAdapterPosition()));
            });

            view_review.setOnClickListener(v -> {
                if (itemReviewerListener != null)
                    itemReviewerListener.clickReviewer(doctorList.get(getAdapterPosition()));
            });



        }
    }

    public interface ItemClickListener {
        public void clickItem(AppointmentDoctor doctor);
    }

    public interface ItemProfileListener {
        public void clickItem(AppointmentDoctor doctor);
    }

    public interface ItemReviewerListener {
        public void clickReviewer(AppointmentDoctor doctor);
    }

    public void setQuery(String query) {
        if (!(query == mQuery) || (query != null && query.equals(mQuery))) {
            getFilter().filter(query);
        }

    }

    public void setNameQuery(String query) {
        if (!(query == mNameQuery) || (query != null && query.equals(mNameQuery))) {
            getmNameFilter().filter(query);
        }

    }

    public void setLocationQuery(String query) {
        if (!(query == mLocationQuery) || (query != null && query.equals(mLocationQuery))) {
            getmLocationFilter().filter(query);
        }

    }


    private final class SpecialityFilter extends Filter {

        private final List<AppointmentDoctor> mItems;

        private SpecialityFilter(List<AppointmentDoctor> items) {
            mItems = new ArrayList<AppointmentDoctor>(items);
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final List<AppointmentDoctor> list;

            if (TextUtils.isEmpty(constraint)) {
                list = mItems;
            } else {
                list = new ArrayList<>();

                final String normalizedConstraint = Normalizer.forSearch(constraint).toLowerCase();

                for (AppointmentDoctor item : mItems) {

                    final String normalizedItem = Normalizer.forSearch(item.speciality).toLowerCase();

                    if (normalizedItem.startsWith(normalizedConstraint) || //
                            normalizedItem.contains(normalizedConstraint)) {
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

            doctorList = (List<AppointmentDoctor>) results.values;
            mQuery = constraint != null ? constraint.toString() : null;
            notifyDataSetChanged();
        }
    }


    private final class DrNameFilter extends Filter {

        private final List<AppointmentDoctor> mItems;

        private DrNameFilter(List<AppointmentDoctor> items) {
            mItems = new ArrayList<AppointmentDoctor>(items);
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final List<AppointmentDoctor> list;

            if (TextUtils.isEmpty(constraint)) {
                list = mItems;
            } else {
                list = new ArrayList<>();

                //final String normalizedConstraint = Normalizer.forSearch(constraint);

                String constrainString = constraint.toString().toLowerCase();


                for (AppointmentDoctor item : mItems) {

                    //final String normalizedItem = Normalizer.forSearch(item.username);
                    String itemDoctor = item.username.toLowerCase();


                    if (itemDoctor.startsWith(constrainString) ||
                            itemDoctor.contains(constrainString)) {
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

            doctorList = (List<AppointmentDoctor>) results.values;
            mNameQuery = constraint != null ? constraint.toString() : null;
            notifyDataSetChanged();
        }
    }


    private final class DrLocationFilter extends Filter {

        private final List<AppointmentDoctor> mItems;

        private DrLocationFilter(List<AppointmentDoctor> items) {
            mItems = new ArrayList<AppointmentDoctor>(items);
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final List<AppointmentDoctor> list;

            if (TextUtils.isEmpty(constraint)) {
                list = mItems;
            } else {
                list = new ArrayList<>();

                String constrainString = constraint.toString().toLowerCase();


                for (AppointmentDoctor item : mItems) {

                    String itemDoctorCountry = item.country.toLowerCase();
                    String itemDoctorCity = item.city.toLowerCase();
                    String itemDoctorState = item.state.toLowerCase();

                    if (itemDoctorCountry.startsWith(constrainString) || itemDoctorCountry.contains(constrainString)
                            || itemDoctorCity.startsWith(constrainString) || itemDoctorCity.contains(constrainString)
                            || itemDoctorState.startsWith(constrainString) || itemDoctorState.contains(constrainString)) {
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

            doctorList = (List<AppointmentDoctor>) results.values;
            mNameQuery = constraint != null ? constraint.toString() : null;
            notifyDataSetChanged();
        }
    }

    public interface VideoClickListener{
        public void onVideoIconClick(JsonObject item);
    }





    }
