package com.gentleman.mvp;

public class mvps {
    private static final String TAG = mvps.class.getSimpleName();

    interface IPresenter<View extends IView<? extends IPresenter<View>>> {

    }

    interface IView<Presenter extends IPresenter<IView<? extends Presenter>>> {

    }
}
