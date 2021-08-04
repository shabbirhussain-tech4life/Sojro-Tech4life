package com.mdcbeta.util;



import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.mdcbeta.data.remote.model.Response;

import io.reactivex.Observable;


/**
 * Created by nisha on 6/25/17.
 */

public abstract class NetworkResource<T> {

    private final MediatorLiveData<Resource<T>> result = new MediatorLiveData<>();

    public NetworkResource() {

        result.setValue(Resource.loading(null));

        Observable<Response<T>> apiResponse = createCall();

        apiResponse.subscribe(response->{
            if(response.status){
                result.postValue(Resource.success(response.data));
            }else {
                result.postValue(Resource.error(response.getMessage(),null));
            }


        });


    }

    public LiveData<Resource<T>> asLiveData() {
        return result;
    }

    @NonNull
    @MainThread
    protected abstract Observable<Response<T>> createCall();


}
