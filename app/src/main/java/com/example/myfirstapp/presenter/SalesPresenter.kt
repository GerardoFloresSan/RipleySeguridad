package com.example.myfirstapp.presenter

import com.example.myfirstapp.data.request.CloseCartRequest
import com.example.myfirstapp.data.request.GetStateByDocRequest
import com.example.myfirstapp.data.request.GetStateByQrRequest
import com.example.myfirstapp.data.response.CloseCartResponse
import com.example.myfirstapp.data.response.SalesGetByDocResponse
import com.example.myfirstapp.data.response.SalesGetStateResponse
import com.example.myfirstapp.domain.useCase.*
import com.example.myfirstapp.presenter.base.BasePresenter
import com.example.myfirstapp.utils.Methods
import io.reactivex.observers.DisposableObserver
import java.io.Serializable

class SalesPresenter(private var useCase1: GetSalesQr, private var useCase2: GetSalesByDoc, private var useCase3: CloseSales, private var methods: Methods) : BasePresenter<UserPresenter.View>() {

    fun getUserByDoc(request: GetStateByQrRequest) {
        if (!methods.isInternetConnected()) return
        view?.showLoading()

        useCase1.request = request
        useCase1.execute(object : DisposableObserver<SalesGetStateResponse>() {
            override fun onComplete() {
                view?.hideLoading()
            }

            override fun onNext(u: SalesGetStateResponse) {
                view?.hideLoading()
                view?.userSuccessPresenter(200, u)
            }

            override fun onError(e: Throwable) {
                view?.hideLoading()
                view?.userSuccessPresenter(202, "error")
            }

        })
    }
    fun getUserByDoc(request: GetStateByDocRequest) {
        if (!methods.isInternetConnected()) return
        view?.showLoading()

        useCase2.request = request
        useCase2.execute(object : DisposableObserver<List<SalesGetByDocResponse>>() {
            override fun onComplete() {
                view?.hideLoading()
            }

            override fun onNext(u: List<SalesGetByDocResponse>) {
                val list :  ArrayList<SalesGetByDocResponse> = arrayListOf()
                list.addAll(u)
                view?.hideLoading()
                view?.userSuccessPresenter(200, list)
            }

            override fun onError(e: Throwable) {
                view?.hideLoading()
                view?.userSuccessPresenter(202, "error")
            }

        })
    }
    fun getUserByDoc(request: CloseCartRequest) {
        if (!methods.isInternetConnected()) return
        view?.showLoading()

        useCase3.request = request
        useCase3.execute(object : DisposableObserver<CloseCartResponse>() {
            override fun onComplete() {
                view?.hideLoading()
            }

            override fun onNext(u: CloseCartResponse) {
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
        fun salesSuccessPresenter(status: Int, vararg args: Serializable)
    }
}