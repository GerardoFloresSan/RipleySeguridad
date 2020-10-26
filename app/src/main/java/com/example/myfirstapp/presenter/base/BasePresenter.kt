package com.example.myfirstapp.presenter.base

import android.content.Context
import androidx.annotation.StringRes
import io.reactivex.disposables.Disposable

open class BasePresenter<V : BasePresenter.View> {

    protected var view: V? = null
    protected var disposable: Disposable? = null

    fun attachView(view: V) {
        this.view = view
    }

    open fun detachView() {
        view = null
        disposable?.dispose()
        disposable = null
        //TODO ADD HIDELOADING???
    }

    interface View {
        fun getContext(): Context

        fun showLoading()

        fun hideLoading()

        fun showError(message: String)

        fun showError(@StringRes message: Int)
    }
}