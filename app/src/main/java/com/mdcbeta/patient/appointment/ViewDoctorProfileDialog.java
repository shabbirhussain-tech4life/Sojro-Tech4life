package com.mdcbeta.patient.appointment;

import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.AppointmentDoctor;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.di.AppModule;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Shakil Karim on 4/8/17.
 */

public class ViewDoctorProfileDialog extends BaseDialogFragment {

    @Inject
    ApiFactory apiFactory;

    @BindView(R.id.docName)
    TextView docName;
    @BindView(R.id.docSpeciality)
    TextView docSpeciality;
    @BindView(R.id.docImage)
    ImageView docImage;
    @BindView(R.id.docDegree)
    TextView docDegree;
    @BindView(R.id.docAffiliation)
    TextView docAffiliation;
    @BindView(R.id.docLocation)
    TextView docLocation;
    @BindView(R.id.docDescription)
    TextView docDescription;
    @BindView(R.id.btnClose)
    ImageView btnClose;

    private AppointmentDoctor doctor;


    public static ViewDoctorProfileDialog newInstance() {
        return new ViewDoctorProfileDialog();
    }

    public void setDoctorData(AppointmentDoctor doctor) {
        this.doctor = doctor;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnClose.setOnClickListener(view1 -> getDialog().dismiss());

        if (doctor != null) {

            Picasso.with(getContext()).load(AppModule.profileimage + doctor.image)
                    .placeholder(R.drawable.image_placeholder).into(docImage);

            docName.setText(doctor.name);
            docSpeciality.setText(doctor.speciality);
            docDegree.setText(doctor.degree);
            docAffiliation.setText(doctor.affiliation);
            docLocation.setText(String.format("%s, %s, %s", doctor.city, doctor.state, doctor.country));
            docDescription.setText(doctor.bio);
        }
    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public int getLayoutID() {
        return R.layout.dialog_doctor_profile;
    }

    @Override
    public void onValidationSucceeded() {

    }




}
