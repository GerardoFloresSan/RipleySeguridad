package com.example.myfirstapp.presenter

import com.example.myfirstapp.data.request.LoginRequest
import com.example.myfirstapp.data.response.LoginResponse
import com.example.myfirstapp.domain.useCase.GetUser
import com.example.myfirstapp.presenter.base.BasePresenter
import com.example.myfirstapp.utils.Methods
import com.example.myfirstapp.utils.PapersManager
import io.reactivex.observers.DisposableObserver
import java.io.Serializable

class UserPresenter (private var useCase: GetUser, private var methods: Methods) : BasePresenter<UserPresenter.View>() {

    fun login(request: LoginRequest) {
        if (!methods.isInternetConnected()) return
        view?.showLoading()

        useCase.request = request
        useCase.execute(object : DisposableObserver<LoginResponse>() {
            override fun onComplete() {
                view?.hideLoading()
            }

            override fun onNext(u: LoginResponse) {
                view?.hideLoading()
                PapersManager.loginAccess = u
                PapersManager.login = true
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