package com.example.myfirstapp.presenter

import com.example.myfirstapp.data.response.Subsidiary
import com.example.myfirstapp.data.response.User
import com.example.myfirstapp.domain.useCase.GetUser
import com.example.myfirstapp.presenter.base.BasePresenter
import com.example.myfirstapp.utils.Methods
import io.reactivex.observers.DisposableObserver
import java.io.Serializable

class UserPresenter (private var getUser: GetUser, private var methods: Methods) : BasePresenter<UserPresenter.View>() {

    fun getUserByDoc(document: String) {
        if (!methods.isInternetConnected()) return
        view?.showLoading()

        getUser.dni = document
        getUser.execute(object : DisposableObserver<User>() {
            override fun onComplete() {
                view?.hideLoading()
            }

            override fun onNext(u: User) {
                view?.hideLoading()
                view?.userSuccessPresenter(200, u)
            }

            override fun onError(e: Throwable) {
                view?.hideLoading()
                view?.userSuccessPresenter(202, "error")
            }

        })
    }

    interface View : BasePresenter.View {
        fun userSuccessPresenter(status: Int, vararg args: Serializable)
    }
}