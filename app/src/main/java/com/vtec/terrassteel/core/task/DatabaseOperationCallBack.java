package com.vtec.terrassteel.core.task;

import java.io.IOException;

public abstract class DatabaseOperationCallBack<Result> {

        public abstract void onSuccess(Result result);

        public void onError() { }


}