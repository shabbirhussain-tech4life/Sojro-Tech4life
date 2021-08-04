package com.mdcbeta.data.remote;

import java.util.List;

/**
 * Created by Shakil Karim on 4/29/17.
 */

public class SubmitUiModel<T> {

    private final boolean isPrgrogress;
    private final String message;
    private final boolean isSuccess;
    private final boolean isEmpty;
    private List<T> data;



    private SubmitUiModel(boolean isPrgrogress, String message, boolean isSuccess, boolean isEmpty, List<T> data) {
        this.isPrgrogress = isPrgrogress;
        this.message = message;
        this.isSuccess = isSuccess;
        this.isEmpty = isEmpty;
        this.data = data;
    }

    public static <T> SubmitUiModel<T> inProgress() {
           return new SubmitUiModel<T>(true,null,false,false,null);
    }

    public static <T> SubmitUiModel<T> success(String message,List<T> data) {
        return new SubmitUiModel<T>(false,message,true,false,data);
    }



    public static <T> SubmitUiModel<T> failure(String message) {
        return new SubmitUiModel<T> (false,message,false,true,null);
    }


    public boolean isPrgrogress() {
        return isPrgrogress;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public List<T> getData() {
        return data;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public String toString() {
        return "SubmitUiModel{" +
                "isPrgrogress=" + isPrgrogress +
                ", message='" + message + '\'' +
                ", isSuccess=" + isSuccess +
                ", data=" + data +
                '}';
    }
}
