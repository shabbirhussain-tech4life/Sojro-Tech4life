package com.mdcbeta.healthprovider;

import android.app.Application;

import android.text.TextUtils;

import androidx.lifecycle.AndroidViewModel;

import com.mdcbeta.app.MyApplication;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.UserNames;
import com.mdcbeta.util.ApiLiveData;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Shakil Karim on 6/26/17.
 */

public class HealthProviderViewModel extends AndroidViewModel {

    @Inject
    ApiFactory apiFactory;

    private final ApiLiveData<List<UserNames>> usernames;

    public HealthProviderViewModel(Application application) {
        super(application);
        ((MyApplication)application).getAppComponent().inject(this);
        usernames = new ApiLiveData<>();

    }

    public ApiLiveData<List<UserNames>> getUserName(String data ,boolean forceUpdate){
        if(usernames.getValue() == null) {
            String licenID = TextUtils.isEmpty(data) ? "-1" : data;
            usernames.call(apiFactory.usersNames(licenID));
        }
        return usernames;
    }



}
