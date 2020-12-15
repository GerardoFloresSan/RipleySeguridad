package com.example.myfirstapp.presenter

import com.example.myfirstapp.R
import com.example.myfirstapp.data.request.LoginRequest
import com.example.myfirstapp.data.response.LoginResponse
import com.example.myfirstapp.domain.useCase.GetUser
import com.example.myfirstapp.presenter.base.BasePresenter
import com.example.myfirstapp.utils.Methods
import com.example.myfirstapp.utils.PapersManager
import io.reactivex.observers.DisposableObserver
import retrofit2.HttpException
import java.io.Serializable
import java.net.SocketTimeoutException

class UserPresenter(private var useCase: GetUser, private var methods: Methods) :
    BasePresenter<UserPresenter.View>() {

    fun login(request: LoginRequest) {
        if (!methods.isInternetConnected()) {
            view?.customWifi()
            return
        }

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
                when (e) {
                    is SocketTimeoutException -> {
                        view?.hideLoading()
                        view?.customTimeOut()
                    }
                    is HttpException -> {
                        when {
                            e.code() == 401 -> view?.userSuccessPresenter(401, "error")
                            else -> view?.userSuccessPresenter(203, "error")
                        }
                    }
                    else -> view?.userSuccessPresenter(666, "Error desconocido")
                }
            }

        })
    }

    interface View : BasePresenter.View {
        fun userSuccessPresenter(status: Int, vararg args: Serializable)
    }
}