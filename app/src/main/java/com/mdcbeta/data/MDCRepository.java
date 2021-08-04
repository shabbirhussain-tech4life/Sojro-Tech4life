package com.mdcbeta.data;

import com.mdcbeta.data.local.Doa.MainAppointmentDetailDoa;
import com.mdcbeta.data.local.Doa.MainAppointmentDoa;
import com.mdcbeta.data.local.MDCDatabase;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.model.MainAppointment;
import com.mdcbeta.data.remote.model.MainAppointmentDetail;
import com.mdcbeta.data.remote.model.MainAppointmentDetailImage;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Shakil Karim on 7/1/17.
 */
@Singleton
public class MDCRepository {

    private final ApiFactory apiFactory;
    private  MDCDatabase db;
    private MainAppointmentDoa mainAppointmentDoa;
    private MainAppointmentDetailDoa mainAppointmentDetailDoa;


    private List<MainAppointment> localcache = new ArrayList<>();




    private static final String TAG = "MDCRepository";

    @Inject
    public MDCRepository(ApiFactory apiFactory, MDCDatabase mdcDatabase,
                         MainAppointmentDoa mainAppointmentDoa, MainAppointmentDetailDoa mainAppointmentDetailDoa) {
        this.apiFactory = apiFactory;
        this.db = mdcDatabase;
        this.mainAppointmentDoa = mainAppointmentDoa;
        this.mainAppointmentDetailDoa = mainAppointmentDetailDoa;
    }


    public Flowable<List<MainAppointmentDetail>> getMainAppointmentsDetails(String id){

        Flowable<List<MainAppointmentDetail>> localmodel = Flowable.just(id)
                .flatMap(mainAppointments ->
                        Flowable.just(mainAppointmentDetailDoa.getMainAppointmentDetail(mainAppointments))
                                .subscribeOn(Schedulers.io()));

        final Flowable<List<MainAppointmentDetail>> mainAppointment = apiFactory.getAppointmentDetails(id)
                .map(model -> {
                    if(model.status && !model.data.isEmpty()){
                        return model.data;
                    }else {
                        List<MainAppointmentDetail> main = new ArrayList<>();
                        db.mainAppointmentDetailDoa().delete(id);
                        return main;
                    }
                }).doOnNext(mainAppointments -> {
                    mainAppointmentDetailDoa.insertAll(mainAppointments);
                });

        return Flowable.concat(localmodel,mainAppointment);

    }

    public Flowable<List<MainAppointmentDetailImage>> getMainAppointmentsImageDetails(String record_id){

        Flowable<List<MainAppointmentDetailImage>> localmodel = Flowable.just(record_id)
                .flatMap(id ->
                        Flowable.just(db.mainAppointmentDetailImageDoa()
                                .getImageDetails(id)).subscribeOn(Schedulers.io()));

        final Flowable<List<MainAppointmentDetailImage>> mainAppointment =
                apiFactory.getAppointmentDetailsImages(record_id)
                .map(model -> {
                    if(model.status && !model.data.isEmpty()){
                        return model.data;
                    }else {
                        List<MainAppointmentDetailImage> main = new ArrayList<>();
                        db.mainAppointmentDetailImageDoa().delete(record_id);
                        return main;
                    }
                }).doOnNext(mainAppointments ->
                        db.mainAppointmentDetailImageDoa().insertAll(mainAppointments));





        return Flowable.concat(localmodel,mainAppointment);


    }



}
