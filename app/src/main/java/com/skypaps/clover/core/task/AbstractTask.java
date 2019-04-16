package com.skypaps.clover.core.task;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.skypaps.clover.common.utils.CommonUtil;
import com.skypaps.clover.core.CloverApplication;
import com.skypaps.clover.core.manager.PreferenceManager;
import com.skypaps.clover.core.manager.SessionManager;
import com.skypaps.clover.core.model.Data;
import com.skypaps.clover.core.model.ErrorMessage;
import com.skypaps.clover.core.model.ErrorType;
import com.skypaps.clover.core.model.exception.BackendException;
import com.skypaps.clover.services.UserService;

import java.io.IOException;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class AbstractTask<R extends Data> {

    private final Single<R> single;

    @Inject
    protected Context context;
    @Inject
    protected Gson gson;

    @Inject
    protected UserService userService;

    @Inject
    protected SessionManager sessionManager;

    @Inject
    protected PreferenceManager preferenceManager;

    @SuppressWarnings("unchecked")
    public AbstractTask() {
        CloverApplication.getComponent().inject((AbstractTask<Data>) this);

        single = Single.create(singleEmitter -> {
            try {
                R result = doCall();
                singleEmitter.onSuccess(result);
            } catch (BackendException backendException) {
                singleEmitter.onError(backendException);
                //singleEmitter.onBackendError(bc.getBackendErrorCode(), bc.getBackendErrorMessage());
            } catch (Exception e) {
                singleEmitter.onError(e);
            }
        });
    }

    public void execute(RequestCallBack<R> requestCallback) {
        single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSubscriber(requestCallback));
    }

    protected abstract R doCall() throws IOException, BackendException, InterruptedException;


    @NonNull
    SingleObserver<R> getSubscriber(final RequestCallBack<R> requestCallback) {
        return new SingleObserver<R>() {

            @Override
            public void onError(Throwable e) {
                Log.e(getClass().getSimpleName(), e.getMessage(), e);

                if (CommonUtil.hasConnectivity(context)) {
                    if (e instanceof BackendException) {
                        try{
                            requestCallback.onError(
                                    gson.fromJson(((BackendException) e).getBackendErrorMessage(), ErrorMessage.class));
                        }catch (Exception exeption){
                            requestCallback.onError(new ErrorMessage(ErrorType.UNEXPECTED_ERROR));
                        }
                    } else {
                        requestCallback.onError(new ErrorMessage(ErrorType.UNEXPECTED_ERROR));
                    }
                } else
                    requestCallback.onError(new ErrorMessage(ErrorType.NO_INTERNET));
            }

            @Override
            public void onSubscribe(Disposable disposable) {
            }

            @Override
            public void onSuccess(R result) {

                requestCallback.onSuccess(result);
            }
        };
    }
}
