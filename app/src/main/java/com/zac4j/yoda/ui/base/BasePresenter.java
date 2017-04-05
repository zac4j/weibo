package com.zac4j.yoda.ui.base;

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 * Created by zac on 16-7-21.
 */

public class BasePresenter<T extends MvpView> implements Presenter<T> {

  private T mMvpView;

  @Override public void attach(T mvpView) {
    mMvpView = mvpView;
  }

  @Override public void detach() {
    mMvpView = null;
  }

  public boolean isViewAttached() {
    return mMvpView != null;
  }

  public T getMvpView() {
    return mMvpView;
  }

  public void showProgress(boolean show) {
    mMvpView.showProgress(show);
  }

  public boolean isProcessing() {
    return mMvpView.isProcessing();
  }

  public void showMainView(boolean show) {
    mMvpView.showMainView(show);
  }

  public void checkViewAttached() {
    if (!isViewAttached()) throw new MvpViewNotAttachedException();
  }

  public static class MvpViewNotAttachedException extends RuntimeException {
    public MvpViewNotAttachedException() {
      super(
          "Please call Presenter.attachView(MvpView) before" + " requesting data to the Presenter");
    }
  }
}
