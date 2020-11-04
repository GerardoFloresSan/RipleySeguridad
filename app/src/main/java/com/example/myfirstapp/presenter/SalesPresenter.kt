package com.example.myfirstapp.presenter

import com.example.myfirstapp.data.request.CloseCartRequest
import com.example.myfirstapp.data.request.GetStateByDocRequest
import com.example.myfirstapp.data.request.GetStateByQrRequest
import com.example.myfirstapp.data.response.CloseCartResponse
import com.example.myfirstapp.data.response.SalesGetByResponse
import com.example.myfirstapp.domain.useCase.*
import com.example.myfirstapp.presenter.base.BasePresenter
import com.example.myfirstapp.utils.Methods
import io.reactivex.observers.DisposableObserver
import java.io.Serializable

class SalesPresenter(private var useCase1: GetSalesQr, private var useCase2: GetSalesByDoc, private var useCase3: CloseSales, private var methods: Methods) : BasePresenter<SalesPresenter.View>() {

    fun getUserByQr(request: GetStateByQrRequest) {
        if (!methods.isInternetConnected()) return
        view?.showLoading()

        useCase1.request = request
        useCase1.execute(object : DisposableObserver<SalesGetByResponse>() {
            override fun onComplete() {
                view?.hideLoading()
            }

            override fun onNext(u: SalesGetByResponse) {
                view?.hideLoading()
                view?.salesSuccessPresenter(200, u)
            }

            override fun onError(e: Throwable) {
                view?.hideLoading()
                view?.salesSuccessPresenter(202, "error")
            }

        })
    }
    fun getUserByDoc(request: GetStateByDocRequest, listener: (Int, ArrayList<SalesGetByResponse>) -> Unit) {
        if (!methods.isInternetConnected()) return
        view?.showLoading()

        useCase2.request = request
        useCase2.execute(object : DisposableObserver<List<SalesGetByResponse>>() {
            override fun onComplete() {
                view?.hideLoading()
            }

            override fun onNext(u: List<SalesGetByResponse>) {
                val list :  ArrayList<SalesGetByResponse> = arrayListOf()
                list.addAll(u)
                view?.hideLoading()
                listener(200, list)
            }

            override fun onError(e: Throwable) {
                view?.hideLoading()
                listener(202, arrayListOf())
            }

        })
    }

    fun closeSales(request: CloseCartRequest) {
        if (!methods.isInternetConnected()) return
        view?.showLoading()

        useCase3.request = request
        useCase3.execute(object : DisposableObserver<CloseCartResponse>() {
            override fun onComplete() {
                view?.hideLoading()
            }

            override fun onNext(u: CloseCartResponse) {
                view?.hideLoading()
                view?.salesSuccessPresenter(200, u)
            }

            override fun onError(e: Throwable) {
                view?.hideLoading()
                view?.salesSuccessPresenter(202, "error")
            }

        })
    }

    interface View : BasePresenter.View {
        fun salesSuccessPresenter(status: Int, vararg args: Serializable)
    }
}