package com.mdcbeta.data.remote.model;

/**
 * Created by Shakil Karim on 5/20/17.
 */

public class SingleResponse<T> {

    public String message;
    public boolean status;
    public T data;


    public String getMessage() {
        return message;
    }

    public boolean getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }
}
