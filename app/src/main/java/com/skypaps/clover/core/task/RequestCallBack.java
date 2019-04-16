package com.skypaps.clover.core.task;

import com.skypaps.clover.core.model.ErrorMessage;

public abstract class RequestCallBack<Result> {

    public abstract void onSuccess(Result result);

    public void onError(ErrorMessage errorMessage) { }


}
