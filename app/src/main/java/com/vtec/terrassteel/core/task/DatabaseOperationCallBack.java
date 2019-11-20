package com.vtec.terrassteel.core.task;

public abstract class DatabaseOperationCallBack<Result> {

        public abstract void onSuccess(Result result);

        public void onError() { }


}