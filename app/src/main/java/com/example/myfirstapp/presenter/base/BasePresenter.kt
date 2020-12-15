package com.example.myfirstapp.presenter.base

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatButton
import com.example.myfirstapp.R
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

        fun tokenExpired()

        fun getContext(): Context

        fun showLoading()

        fun hideLoading()

        fun showError(message: String)

        fun showError(@StringRes message: Int)

        fun customWifi() {
            val dialog = Dialog(getContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_wifi)
            val btnWifi: AppCompatButton =
                dialog.findViewById<android.view.View>(R.id.btn_wifi) as AppCompatButton

            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
            dialog.window?.attributes?.windowAnimations = R.style.AppTheme_Slide
            dialog.setCancelable(false)
            dialog.show()
            btnWifi.setOnClickListener {
                dialog.dismiss()
            }
        }

        fun customTimeOut() {
            val dialog = Dialog(getContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_timeout)
            val btnTimeOut: AppCompatButton =
                dialog.findViewById<android.view.View>(R.id.btn_timeout) as AppCompatButton

            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
            dialog.window?.attributes?.windowAnimations = R.style.AppTheme_Slide
            dialog.setCancelable(false)
            dialog.show()
            btnTimeOut.setOnClickListener {
                dialog.dismiss()
            }
        }

    }
}