package com.mdcbeta.util;

import io.reactivex.CompletableTransformer;
import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Shakil Karim on 4/11/17.
 */

public class RxUtil {


    /*.subscribe(model ->{

               if(model.isPrgrogress()){
                        showProgress("Loading");
                    }
                    else {
                        if(model.isSuccess() && model.isEmpty()){
                            hideProgress();
                            //emptyView.setVisibility(View.VISIBLE);
                        }
                        else if(model.isSuccess()){
                            hideProgress();
                            //emptyView.setVisibility(View.GONE);
                            //mainAppointmentAdapter.swap((List<MainAppointment>) model.getData());
                        }
                        else {
                            showError(model.getMessage());
                        }
                    }

    },t-> showError(t.getMessage())));*/



//    public <T> ObservableTransformer<T, T > transform() {
//        return upstream -> upstream.map(response -> {
//                    if(()response.status ){
//                        return SubmitUiModel.success(response.message,response.data);
//                    }else {
//                        return SubmitUiModel.failure(response.message);
//                    }
//                })
//                .onErrorReturn(t -> SubmitUiModel.failure(t.getMessage()))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .startWith(SubmitUiModel.inProgress());
//    }


//    public static  ObservableTransformer<Response<?>,SubmitUiModel> apiTransformer = events -> events
//            .map(response -> {
//                if(response.status ){
//                    return SubmitUiModel.success(response.message,response.data);
//                }else {
//                    return SubmitUiModel.failure(response.message);
//                }
//            })
//            .onErrorReturn(t -> SubmitUiModel.failure(t.getMessage()))
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .startWith(SubmitUiModel.inProgress());


//    public static ObservableTransformer<Response<?>,SubmitUiModel> InsertapiTransformer = events -> events
//            .map(response ->{
//                if(response.status){
//                    return SubmitUiModel.success(response.message,response.data);
//                }else {
//                    return SubmitUiModel.failure(response.message);
//                }
//            })
//            .onErrorReturn(t -> SubmitUiModel.failure(t.getMessage()))
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .startWith(SubmitUiModel.inProgress());



    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> FlowableTransformer<T, T> applySchedulersFlow() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> SingleTransformer<T, T> applySchedulersSingle() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static CompletableTransformer applySchedulersCompletable() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
