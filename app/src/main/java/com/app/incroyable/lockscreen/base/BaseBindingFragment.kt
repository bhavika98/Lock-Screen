@file:Suppress("unused")

package com.app.incroyable.lockscreen.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.viewbinding.ViewBinding

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseBindingFragment<VB : ViewBinding> : BaseFragment() {

    lateinit var mBinding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        this.mBinding = setBinding(inflater, container)
        return this.mBinding.root
    }

    override fun getLayoutRes(): Int? {
        return null
    }

    @UiThread
    abstract fun setBinding(layoutInflater: LayoutInflater, container: ViewGroup?): VB

    open fun isUserVisible(visible: Boolean) {

    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (this::mBinding.isInitialized) {
            isUserVisible(menuVisible)
        }
    }
}
