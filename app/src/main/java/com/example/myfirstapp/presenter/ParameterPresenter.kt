package com.example.myfirstapp.presenter

import com.example.myfirstapp.data.response.Parameter
import com.example.myfirstapp.domain.useCase.GetParameters
import com.example.myfirstapp.presenter.base.BasePresenter
import com.example.myfirstapp.utils.Methods
import io.reactivex.observers.DisposableObserver
import java.io.Serializable

class ParameterPresenter (private var getParameters: GetParameters, private var methods: Methods) : BasePresenter<ParameterPresenter.View>() {

    fun getParameters() {

        getParameters.execute(object : DisposableObserver<List<Parameter>>() {
            override fun onComplete() {
            }

            override fun onNext(t: List<Parameter>) {
                val list :  ArrayList<Parameter> = arrayListOf()
                list.addAll(t)
                view?.parameterSuccessPresenter(200, list)
            }

            override fun onError(e: Throwable) {
                view?.parameterSuccessPresenter(202, "error")
            }

        })
    }

    interface View : BasePresenter.View {
        fun parameterSuccessPresenter(status: Int, vararg args: Serializable)
    }
}