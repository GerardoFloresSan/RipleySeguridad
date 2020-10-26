package com.example.myfirstapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myfirstapp.utils.enableError
import com.google.android.material.textfield.TextInputLayout

abstract class BaseFragment : Fragment() {

    private lateinit var parent: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(getFragmentView(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parent = view
        onCreate()
    }

    abstract fun getFragmentView(): Int

    abstract fun onCreate()

    protected fun setTitle(title: String) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).setTitle(title)
        }
    }

    fun activeAllWrappers() {
        activeAllWrappers(parent as ViewGroup)
    }

    fun hideAllWrappers() {
        activeAllWrappers(parent as ViewGroup)
    }

    private fun activeAllWrappers(parent: ViewGroup) {
        for (i in 0..parent.childCount) {

            val view = parent.getChildAt(i)

            if (view is TextInputLayout) {
                view.enableError()
            } else if (view is ViewGroup) {
                activeAllWrappers(parent.getChildAt(i) as ViewGroup)
            }
        }
    }
}