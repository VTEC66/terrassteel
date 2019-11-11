package com.vtec.terrassteel.core.task;

import com.vtec.terrassteel.core.model.ErrorMessage;

public abstract class RequestCallBack<Result> {

    public abstract void onSuccess(Result result);

    public void onError(ErrorMessage errorMessage) { }


}
