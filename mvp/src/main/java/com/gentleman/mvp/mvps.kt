package com.gentleman.mvp


interface IPresenter<out View : IView<IPresenter<View>>> : ILifecycle {
    val view: View
}

interface IView<out Presenter : IPresenter<IView<Presenter>>> : ILifecycle {
    val presenter: Presenter
}