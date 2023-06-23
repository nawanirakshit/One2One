package com.test.papers.kotlin.listeners

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlin.reflect.KClass

interface KotlinBaseListener {
    fun replaceFragment(
        java: KClass<out Fragment>,
        extras: Bundle?,
        userTag: String = "",
        addToBackStack: Boolean = true,
        animation: Boolean,
        container: Int = 0
    )

    fun replaceFragment(
        fragment: Fragment,
        extras: Bundle?,
        userTag: String = "",
        addToBackStack: Boolean = true,
        animation: Boolean,
        container: Int = 0
    )

    fun addFragment(
        fragment: KClass<out Fragment>,
        extras: Bundle?,
        tag: String = "",
        addToBackStack: Boolean = true,
        animation: Boolean = false,
        container: Int = 0
    )

    fun addFragment(
        fragment: Fragment,
        extras: Bundle?,
        tag: String = "",
        addToBackStack: Boolean = true,
        animation: Boolean = false,
        container: Int = 0
    )

    fun addChildFragment(
        childFragmentManager: FragmentManager,
        container: Int,
        kClass: KClass<out Fragment>,
        addToBackStack: Boolean = true,
        bundle: Bundle = Bundle()
        , animation: Boolean = false
    )
    fun addChildFragment(
        childFragmentManager: FragmentManager,
        container: Int,
        fragment: Fragment,
        addToBackStack: Boolean = true,
        bundle: Bundle = Bundle()
        , animation: Boolean = false
    )

    fun replaceChildFragment(
        childFragmentManager: FragmentManager,
        container: Int,
        kClass: KClass<out Fragment>,
        addToBackStack: Boolean = true,
        bundle: Bundle = Bundle()
        , animation: Boolean = false
    )
    fun replaceChildFragment(
        childFragmentManager: FragmentManager,
        container: Int,
        fragment: Fragment,
        addToBackStack: Boolean = true,
        bundle: Bundle = Bundle()
        , animation: Boolean = false
    )

    fun getFragment(kClass: KClass<out Fragment>): Fragment?
    fun getCurrentFragment(): Fragment?
    fun forceHideKeyboard()
    fun showLoading()
    fun hideLoading()
    fun getCurrentFragment(container: Int): Fragment?
    fun getLastAddedFragment(): Fragment?
}

typealias  ClickListener<T> = (T) -> Unit