package com.zac4j.yoda.ui.home.hot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zac4j.yoda.data.DataManager;
import com.zac4j.yoda.data.model.Tag;
import com.zac4j.yoda.data.model.response.Error;
import com.zac4j.yoda.data.remote.RequestState;
import com.zac4j.yoda.di.PerConfig;
import com.zac4j.yoda.ui.base.RxPresenter;
import com.zac4j.yoda.util.RxUtils;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Presenter for Hot Tag
 * Created by zaccc on 6/1/2017.
 */

@PerConfig public class HotTagPresenter extends RxPresenter<HotTagView> {

  private final DataManager mDataManager;
  private CompositeDisposable mDisposable;

  @Inject public HotTagPresenter(DataManager dataManager) {
    mDataManager = dataManager;
  }

  @Override public void attach(HotTagView mvpView) {
    super.attach(mvpView);
    mDisposable = new CompositeDisposable();
  }

  @Override public void detach() {
    super.detach();
    mDisposable.clear();
  }

  public void getHotTags(String token) {
    checkViewAttached();

    mDisposable.add(mDataManager.getHotTags(token)
        .compose(RxUtils.applySchedulers())
        .doOnSubscribe(disposable -> publishRequestState(RequestState.LOADING))
        .doOnError(throwable -> publishRequestState(RequestState.ERROR))
        .doOnSuccess(objectResponse -> publishRequestState(RequestState.COMPLETE))
        .subscribeWith(new DisposableSingleObserver<Response<Object>>() {
          @Override public void onSuccess(@NonNull Response<Object> response) {
            publishResponse(response);
          }

          @Override public void onError(@NonNull Throwable throwable) {
            publishErrors(throwable);
          }
        }));
  }

  @Override protected void publishResponse(Response<Object> response) {
    super.publishResponse(response);

    mDisposable.add(mResponse.subscribe(response1 -> {
      if (response1.isSuccessful()) {
        List<Tag> tagList = null;
        Object data = response1.body();
        ObjectMapper mapper = mDataManager.getObjectMapper();
        try {
          String value = mapper.writeValueAsString(data);
          tagList = mapper.readValue(value, new TypeReference<List<Tag>>() {
          });
        } catch (IOException e) {
          e.printStackTrace();
          getMvpView().showErrorView(e.getMessage());
        }

        if (tagList == null || tagList.isEmpty()) {
          getMvpView().showEmptyView(true);
        } else {
          getMvpView().showHotTags(tagList);
        }
      }
    }));
  }

  @Override protected void publishErrors(Throwable error) {
    super.publishErrors(error);

    mDisposable.add(mErrors.subscribe(throwable -> {
      if (throwable instanceof HttpException) {
        Response response = ((HttpException) throwable).response();
        int responseCode = response.code();
        getMvpView().showErrorView(Error.NETWORK);
      }
    }));
  }
}