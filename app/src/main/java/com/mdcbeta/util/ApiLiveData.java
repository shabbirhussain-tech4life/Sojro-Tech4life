package com.mdcbeta.util;



import androidx.lifecycle.LiveData;

import com.mdcbeta.data.remote.model.Response;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Shakil Karim on 6/26/17.
 */

public class ApiLiveData<T> extends LiveData<Resource<T>> {
    Disposable disposable;

    public void call(Observable<Response<T>> apicall){

            setValue(Resource.loading(null));
            disposable = apicall
                .subscribeOn(Schedulers.io())
                .subscribe(response->{
                if(response.status){
                    postValue(Resource.success(response.data));
                }else {
                    postValue(Resource.error(response.getMessage(),null));
                }
                },throwable -> postValue(Resource.error(throwable.getMessage(),null)));

    }

    @Override
    protected void onInactive() {
        if(disposable!=null)
        disposable.dispose();
    }



}
