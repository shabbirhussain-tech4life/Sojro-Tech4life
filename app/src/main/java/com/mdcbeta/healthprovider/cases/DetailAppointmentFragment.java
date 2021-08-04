package com.mdcbeta.healthprovider.cases;

import android.os.Bundle;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.MDCRepository;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.UserNames;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.di.AppModule;
import com.mdcbeta.healthprovider.HealthProviderViewModel;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.Utils;
import com.mdcbeta.widget.ActionBar;
import com.mdcbeta.widget.CustomSpinnerAdapter;
import com.mdcbeta.widget.FlowLayout;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by MahaRani on 28/02/2018.
 */

public class DetailAppointmentFragment extends BaseFragment {

    @Inject
    MDCRepository mdcRepository;
    @Inject
    ApiFactory apiFactory;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_location)
    TextView txtLocation;
    @BindView(R.id.txt_age)
    TextView txtAge;
    @BindView(R.id.txt_datetime)
    TextView txtDatetime;
    @BindView(R.id.txt_medical_complain)
    TextView txt_medical_complain;
    @BindView(R.id.txt_history)
    TextView txtHistory;
    @BindView(R.id.image_container)
    FlowLayout imageContainer;
    @BindView(R.id.label_comment)
    TextView label_comment;
    @BindView(R.id.doctors_spin)
    Spinner doctors_spin;
    @BindView(R.id.txt_book_appointment)
    TextView txt_book_appointment;
    @BindView(R.id.txt_case_code)
    TextView txt_case_code;
    @BindView(R.id.txt_gender_field)
    TextView txt_gender_field;
    @BindView(R.id.send_comment)
    Button send_comment;
    @BindView(R.id.add_comment)
    EditText add_comment;
    @BindView(R.id.comment_container)
    LinearLayout comment_container;

    ArrayList<String> images;

    @BindDimen(R.dimen.dp_150)
    int dp_150;
    @BindDimen(R.dimen.dp_10)
    int dp_10;

    static String mId;
    private CustomSpinnerAdapter<UserNames> adapter;

    private static final String TAG = "CaseDetailFragment";
    private HealthProviderViewModel healthProviderViewModel;
    private LayoutInflater inflater;

    String url;


    @Override
    public int getLayoutID() {
        return R.layout.fragment_case_details;
    }

    public static DetailAppointmentFragment newInstance(String id) {
        DetailAppointmentFragment fragment = new DetailAppointmentFragment();
        mId = id;
        return fragment;
    }


    @Override
    public void getActionBar(ActionBar actionBar) {

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        healthProviderViewModel = ViewModelProviders.of(getActivity()).get(HealthProviderViewModel.class);
        inflater = LayoutInflater.from(getContext());
        getData();



    }

    private void showHideUi() {
        txt_book_appointment.setVisibility(View.GONE);
        doctors_spin.setVisibility(View.GONE);
        label_comment.setVisibility(View.GONE);
        comment_container.setVisibility(View.GONE);
        send_comment.setVisibility(View.GONE);
      //  txtAge.setVisibility(View.GONE);
       // txt_gender_field.setText("");
      //  txtLocation.setVisibility(View.GONE);

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }



   public void getData(){

       images = new ArrayList<>();

      /*  healthProviderViewModel.getUserName(getUser().getLicenses_id(),false).observe(this,data -> {


            if(data.status == Status.SUCCESS && data.data != null) {
                List<UserNames> userNames = new ArrayList<UserNames>();
                userNames.addAll(data.data);

                userNames.add(0,new UserNames("Select a doctor"));
                adapter = new CustomSpinnerAdapter<>(getContext(),userNames);
                doctors_spin.setAdapter(adapter);
            }



        });*/

       if(mId == null)
           return;


       showProgress("Loading");


       mdcRepository.getMainAppointmentsDetails(mId)
               .compose(RxUtil.applySchedulersFlow())
               .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
               .subscribe(response-> {
                   hideProgress();
                   if(response.size() <= 0) {
                       return;

                   }
                   txt_case_code.setText(response.get(0).caseCode);
                   txtDatetime.setText(Utils.format12HourDateTime(Utils.UTCTolocalDateTime(response.get(0).updatedAt)));
                   txtName.setText(response.get(0).fname + " " + response.get(0).lname );
                   txtAge.setText(response.get(0).yAge + " Year");
                   txt_gender_field.setText(response.get(0).gender.equalsIgnoreCase("f") ? "Female" : "Male");
                //   txtLocation.setText(response.get(0).locationId);
                   txt_medical_complain.setText(Html.fromHtml(response.get(0).data));
                   txtHistory.setText(Html.fromHtml(response.get(0).history));


               },t->  showError(t.getMessage()));


       mdcRepository.getMainAppointmentsImageDetails(mId)
               .compose(RxUtil.applySchedulersFlow())
               .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
               .subscribe(response -> {

                   hideProgress();

                   if(response.size() <= 0) {
                       return;
                   }


                   for (int i = 0; i < response.size(); i++) {
                       images.add(AppModule.rootimage+response.get(i).image);
                   }


                   int j = 0;
                   for (String image : images) {

                       ImageView imageView = new ImageView(getContext());
                       imageView.setLayoutParams(new FlowLayout.LayoutParams(dp_150,dp_150));
                       imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                       imageView.setTag(j++);

                       imageView.setOnClickListener(v -> {
                           int pos = (int) v.getTag();

                           Log.d(TAG, "onActivityCreated: "+pos);

//                            new ImageViewer.Builder(getActivity(), images)
//                                    .setStartPosition(pos)
//                                    .show();

                       });

                       Picasso.with(getContext()).load(image)
                               .placeholder(R.drawable.image_placeholder).into(imageView);


                       android.widget.Space space = new android.widget.Space(getContext());
                       space.setLayoutParams(new FlowLayout.LayoutParams(dp_10,dp_10));

                       imageContainer.addView(imageView);
                       imageContainer.addView(space);

                   }


                   Log.d(TAG, "onNext: " + response);

               },t->  showError(t.getMessage()));




 /*      showProgress("Loading...");


        apiFactory.mycasesDetails(mId)
                .compose(RxUtil.applySchedulers())
                .subscribe(resource -> {

                    hideProgress();

                    if(resource.status) {


                        JsonObject data = resource.data.get("details").getAsJsonObject();


                        if (data.get("case_code").getAsString().equalsIgnoreCase("no data") || TextUtils.isEmpty(data.get("case_code").getAsString())) {
                            showHideUi();


                            try {
                                txtDatetime.setText(Utils.format12HourDateTime(Utils.UTCTolocalDateTime(data.get("created_at").getAsString())));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        } else {

                            txt_case_code.setText(data.get("case_code").getAsString());



                            try {
                                txtDatetime.setText(Utils.format12HourDateTime(Utils.UTCTolocalDateTime(data.get("created_at").getAsString())));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }


                            try {
                                txt_gender_field.setText(data.get("gender").getAsString().equalsIgnoreCase("f") ? "Female" : "Male");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            try {
                                txtLocation.setText(data.get("area").getAsString());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }


                            try {
                                txtGender.setText(data.get("fname").getAsString() + " " + data.get("lname").getAsString());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        }


                        try {
                            txtHistory.setText(Html.fromHtml(data.get("history").getAsString()));
                          //  txtHistory.setText(mId);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                        try {
                            txt_medical_complain.setText(Html.fromHtml(data.get("data").getAsString()));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }



                        try {
                            int commemtsSize = resource.data.get("comments").getAsJsonArray().size();

                            comment_container.removeAllViews();

                            for (int i = 0; i < commemtsSize; i++) {

                                View inflatedLayout = inflater.inflate(R.layout.include_comment_layout, comment_container, false);

                                JsonObject comment = resource.data.get("comments").getAsJsonArray().get(i).getAsJsonObject();


                                ((TextView)inflatedLayout.findViewById(R.id.txt_comment))
                                        .setText(comment.get("comment").getAsString());


                                ((TextView)inflatedLayout.findViewById(R.id.txt_date))
                                        .setText(Utils.format12HourDateTime(Utils.UTCTolocalDateTime(comment.get("created_at").getAsString())));


                                ((TextView)inflatedLayout.findViewById(R.id.txt_name))
                                        .setText(comment.get("name").getAsString());

                                ImageView profileImage = (ImageView) inflatedLayout.findViewById(R.id.profile_image);
                                url = AppModule.profileimage+comment.get("image").getAsString();

                                Picasso.with(getContext())
                                        .load(url)
                                        .fit().centerCrop()
                                        .placeholder(R.drawable.image_placeholder).into(profileImage);

                                comment_container.addView(inflatedLayout);


                            }




                        } catch (Exception ex) {
                        }


                        try {
                            int imagesSize = resource.data.get("images").getAsJsonArray().size();

                            imageContainer.removeAllViews();

                            List<String> imageUrls = new ArrayList<String>();
                            for (int i = 0; i < imagesSize; i++) {
                                JsonObject images = resource.data.get("images").getAsJsonArray().get(i).getAsJsonObject();
                                imageUrls.add(AppModule.rootimage + images.get("image").getAsString());

                                Log.d("image url:  " ,images.get("image").getAsString() );

                                View imageViewItem = inflater.inflate(R.layout.include_image,imageContainer,false);
                                ImageView imageView = (ImageView) imageViewItem.findViewById(R.id.image);
                                //   imageView.setTag(j);

                                Picasso.with(getContext())
                                        .load(AppModule.rootimage + images.get("image").getAsString())
                                        //    .load(AppModule.rootimage + "1485512488-1.jpg")
                                        .fit().centerCrop()
                                        .placeholder(R.drawable.image_placeholder).into(imageView);

                                // }





                                imageView.setOnClickListener(v -> {
                                    int pos = (int) v.getTag();
                                    new ImageViewer.Builder(getActivity(), imageUrls)
                                            .setStartPosition(pos)
                                            .show();

                                });

                                Space space = new Space(DetailAppointmentFragment.this.getContext());
                                space.setLayoutParams(new FlowLayout.LayoutParams(dp_10, dp_10));




                                imageContainer.addView(space);
                                imageContainer.addView(imageViewItem);




                            }


                        } catch (Exception ex) {
                        }





                    }else {
                        showError(resource.getMessage());
                    }

                },throwable -> {
                    showError(throwable.getMessage());

                });*/


    }





  /*  @OnItemSelected(R.id.doctors_spin) void onItemSelected(int position) {
        if(position != 0){
            AppPref.getInstance(getActivity()).put(AppPref.Key.STEP_DOC_ID, adapter.getItem(position).id);

            getFragmentHandlingActivity().replaceFragmentWithBackstack(Step4Fragment.newInstance(true,adapter.getItem(position).id,mId));
        }

    }*/


    @OnClick(R.id.send_comment) void sendComment(){
        if(TextUtils.isEmpty(add_comment.getText().toString()))
            return;

        showProgress("Adding comment");
        apiFactory.addCommentsInCase(
                getUser().getId(),
                add_comment.getText().toString(),
                mId,Utils.localToUTCDateTime(Utils.getDatetime()))
                .compose(RxUtil.applySchedulers())
                .subscribe(data->{
                    add_comment.setText("");
                    hideProgress();
                    getData();

                },throwable ->{
                    add_comment.setText("");
                    hideProgress();
                    throwable.printStackTrace();
                });

    }


}
