package com.example.administrator.steelbarattestation.http;

import com.example.administrator.steelbarattestation.exception.ExceptionEngine;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018-01-19.
 */

public class NetTransformer {
    public static <T> ObservableTransformer<NetResponse<T>, T> compose() {
        return observable ->
                observable.subscribeOn(Schedulers.io())//io线程发起请求
                        .observeOn(AndroidSchedulers.mainThread())//UI线程处理响应
                        .map(httpResponse -> {//检验返回的数据
                            if (httpResponse == null) {
                                ExceptionEngine.checkApiException(httpResponse);
                            }
                            return httpResponse.getData();
                        })
                        .onErrorResumeNext(throwable -> {//将异常转成自定义的异常
                                    throwable.printStackTrace();//打印异常
                                    return Observable.error(ExceptionEngine.handleException(throwable));//转换ApiException异常
                                }
                        );
//                        .map(new Function<NetResponse<T>, T>() {//检测返回的数据
//                            @Override
//                            public T apply(NetResponse<T> httpResponse) throws Exception {
//                                if (httpResponse == null) {
//                                    ExceptionEngine.checkApiException(httpResponse);
//                                }
//                                return httpResponse.getData();
//                            }
//                        })
//                        .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends T>>() {//将异常转成自定义的异常
//                            @Override
//                            public ObservableSource<? extends T> apply(Throwable throwable) throws Exception {
//                                throwable.printStackTrace();//打印异常
//                                return Observable.error(ExceptionEngine.handleException(throwable));//转换ApiException异常
//                            }
//                        });
    }
}
