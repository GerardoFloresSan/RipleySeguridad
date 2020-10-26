package com.example.myfirstapp.presenter

import com.example.myfirstapp.BuildConfig
import com.example.myfirstapp.data.mapper.DataMapper
import com.example.myfirstapp.data.request.CheckPricesRequest
import com.example.myfirstapp.data.response.CheckPricesResponse
import com.example.myfirstapp.data.response.Subsidiary
import com.example.myfirstapp.domain.useCase.CheckPrice
import com.example.myfirstapp.presenter.base.BasePresenter
import com.example.myfirstapp.utils.Methods
import com.example.myfirstapp.utils.PapersManager
import io.reactivex.observers.DisposableObserver
import java.io.Serializable

class CheckPricePresenter (private var checkPrice: CheckPrice, private var methods: Methods) : BasePresenter<CheckPricePresenter.View>() {

    fun checkPriceComplete(code: String, quantity: Long, byScanOrManual: Boolean) {
        if (!methods.isInternetConnected()) return
        view?.showLoading()
        var skuIsMaxProduct = false

        val maxGlobalProduct = if(BuildConfig.DEBUG) 1 else Methods.getParameter(122).value.toInt()
        val maxByProduct =if(BuildConfig.DEBUG) 3 else  Methods.getParameter(123).value.toInt()
        val check = DataMapper().reverseCheck(PapersManager.shoppingCart)

        /*if(check.products.size >= maxGlobalProduct) {
            var inList = false
            for (p in check.products) {
                if (p.sku == code) {
                    inList = true
                }
            }

            if(!inList) {
                view?.hideLoading()
                view?.checkSuccessPresenter(305)
                return
            }
        }*/

        if(check.subsidiary.isEmpty()) check.subsidiary = PapersManager.subsidiary.code
        if(check.session.isEmpty()) check.session = VALUE_SESSION

        if(check.products.isEmpty()) {
            check.products.add(CheckPricesRequest.Product().apply {
                this.id = 1
                this.sku = code
                this.quantity = quantity.toInt()
            })
        } //
        else {
            if(quantity.toInt() == 0) {
                var position : Int = 0
                check.products.forEachIndexed { i, p ->
                    if (p.sku == code) {
                        position = i
                    }
                }
                check.products.removeAt(position)
            } //
            else {
                var productInList = false
                for (p in check.products) {
                    if (p.sku == code) {
                        if(byScanOrManual) {
                            productInList = true
                            when(p.quantity) {
                                maxByProduct -> {
                                    skuIsMaxProduct = true
                                    p.quantity = maxByProduct
                                }
                                else -> p.quantity = p.quantity + quantity.toInt()
                            }
                        } //
                        else {
                            productInList = true
                            p.quantity = quantity.toInt()
                        }
                        break
                    }
                }
                if(!productInList) {
                    check.products.add(CheckPricesRequest.Product().apply {
                        this.id = 1
                        this.sku = code
                        this.quantity = quantity.toInt()
                    })
                }
            }
        }


        checkPrice.request = check

        checkPrice.execute(object : DisposableObserver<CheckPricesResponse>() {
            override fun onComplete() {
                view?.hideLoading()
            }

            override fun onNext(t: CheckPricesResponse) {
                view?.hideLoading()
                view?.checkSuccessPresenter(200, t, code, skuIsMaxProduct)
            }

            override fun onError(e: Throwable) {
                view?.hideLoading()
                view?.checkSuccessPresenter(202, "error")
            }

        })
    }

    fun checkPriceListener(code: String, quantity: Long, byScanOrManual: Boolean, listener: (Int, CheckPricesResponse, String, Boolean) -> Unit) {
        if (!methods.isInternetConnected()) return
        view?.showLoading()
        var skuIsMaxProduct = false

        val maxGlobalProduct = if(BuildConfig.DEBUG) 1 else Methods.getParameter(122).value.toInt()
        val maxByProduct = if(BuildConfig.DEBUG) 3 else Methods.getParameter(123).value.toInt()
        val check = DataMapper().reverseCheck(PapersManager.shoppingCart)

        /*if(check.products.size >= maxGlobalProduct) {
            var inList = false
            for (p in check.products) {
                if (p.sku == code) {
                    inList = true
                }
            }

            if(!inList) {
                view?.hideLoading()
                listener(305, CheckPricesResponse(), code, skuIsMaxProduct)
                return
            }
        }*/

        if(check.subsidiary.isEmpty()) check.subsidiary = PapersManager.subsidiary.code
        if(check.session.isEmpty()) check.session = VALUE_SESSION

        if(check.products.isEmpty()) {
            check.products.add(CheckPricesRequest.Product().apply {
                this.id = 1
                this.sku = code
                this.quantity = quantity.toInt()
            })
        } else {
            var productInList = false
            for (p in check.products) {
                if (p.sku == code) {
                    if(byScanOrManual) {
                        productInList = true
                        when(p.quantity) {
                            maxByProduct -> {
                                skuIsMaxProduct = true
                                p.quantity = maxByProduct
                            }
                            else -> p.quantity = p.quantity + quantity.toInt()
                        }
                    } else {
                        productInList = true
                        p.quantity = quantity.toInt()
                    }
                    break
                }
            }
            if(!productInList) {
                check.products.add(CheckPricesRequest.Product().apply {
                    this.id = 1
                    this.sku = code
                    this.quantity = quantity.toInt()
                })
            }
        }

        checkPrice.request = check
        checkPrice.execute(object : DisposableObserver<CheckPricesResponse>() {
            override fun onComplete() {
                view?.hideLoading()
            }

            override fun onNext(t: CheckPricesResponse) {
                view?.hideLoading()
                listener(200, t, code, skuIsMaxProduct)
            }

            override fun onError(e: Throwable) {
                view?.hideLoading()
                listener(202, CheckPricesResponse(), "", false)
            }

        })
    }

    fun cuponComplete(code: String, add: Boolean) {
        if (!methods.isInternetConnected()) return
        view?.showLoading()

        val check = DataMapper().reverseCheck(PapersManager.shoppingCart)

        if(check.subsidiary.isEmpty()) check.subsidiary = PapersManager.subsidiary.code
        if(check.session.isEmpty()) check.session = VALUE_SESSION

        if(add) {
            if(!check.coupons.contains(code)) {
                check.coupons.add(code)
            } //
            else {
                view?.hideLoading()
                view?.checkSuccessPresenter(205, CheckPricesResponse(), "")
                return
            }
        } else {
            check.coupons.remove(code)
        }

        checkPrice.request = check

        checkPrice.execute(object : DisposableObserver<CheckPricesResponse>() {
            override fun onComplete() {
                view?.hideLoading()
            }

            override fun onNext(t: CheckPricesResponse) {
                view?.hideLoading()
                view?.checkSuccessPresenter(200, t, code)
            }

            override fun onError(e: Throwable) {
                view?.hideLoading()
                view?.checkSuccessPresenter(202, "error")
            }

        })
    }

    fun cuponListener(code: String, add: Boolean, listener: (Int, CheckPricesResponse, String) -> Unit) {
        if (!methods.isInternetConnected()) return
        view?.showLoading()

        val check = DataMapper().reverseCheck(PapersManager.shoppingCart)

        if(check.subsidiary.isEmpty()) check.subsidiary = PapersManager.subsidiary.code
        if(check.session.isEmpty()) check.session = VALUE_SESSION

        if(add) {
            if(!check.coupons.contains(code)) {
                check.coupons.add(code)
            } //
            else {
                view?.hideLoading()
                listener(205, CheckPricesResponse(), "")
                return
            }
        } else {
            check.coupons.remove(code)
        }

        checkPrice.request = check
        checkPrice.execute(object : DisposableObserver<CheckPricesResponse>() {
            override fun onComplete() {
                view?.hideLoading()
            }

            override fun onNext(t: CheckPricesResponse) {
                view?.hideLoading()
                listener(200, t, code)
            }

            override fun onError(e: Throwable) {
                view?.hideLoading()
                listener(202, CheckPricesResponse(), "")
            }

        })
    }

    interface View : BasePresenter.View {
        fun checkSuccessPresenter(status: Int, vararg args: Serializable)
    }
    companion object {
        private const val VALUE_SESSION = "aaaaaaaaaaaaa"
    }
}