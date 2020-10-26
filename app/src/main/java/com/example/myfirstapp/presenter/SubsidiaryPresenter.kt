package com.example.myfirstapp.presenter

import com.example.myfirstapp.data.response.Subsidiary
import com.example.myfirstapp.domain.useCase.GetSubsidiary
import com.example.myfirstapp.presenter.base.BasePresenter
import com.example.myfirstapp.utils.Methods
import io.reactivex.observers.DisposableObserver
import java.io.Serializable

class SubsidiaryPresenter (private var getSubsidiary: GetSubsidiary, private var methods: Methods) : BasePresenter<SubsidiaryPresenter.View>() {

    fun getSubsidiary() {
        if (!methods.isInternetConnected()) return

        getSubsidiary.execute(object : DisposableObserver<List<Subsidiary>>() {
            override fun onComplete() {
                view?.hideLoading()
            }

            override fun onNext(t: List<Subsidiary>) {
                view?.hideLoading()
                val list :  ArrayList<Subsidiary> = arrayListOf()
                list.addAll(t)
                view?.subsidiarySuccessPresenter(200, list)
            }

            override fun onError(e: Throwable) {
                view?.hideLoading()
                view?.subsidiarySuccessPresenter(202, "error")
            }

        })
    }

    interface View : BasePresenter.View {
        fun subsidiarySuccessPresenter(status: Int, vararg args: Serializable)
    }
}