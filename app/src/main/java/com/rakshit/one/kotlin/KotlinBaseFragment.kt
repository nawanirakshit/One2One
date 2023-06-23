package com.test.papers.kotlin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.test.papers.kotlin.listeners.KotlinBaseListener
import com.test.papers.kotlin.viewmodel.KotlinBaseViewModel
import com.test.papers.kotlin.viewmodel.common.ErrorType
import com.test.papers.utils.extension.showToast

@Keep
abstract class KotlinBaseFragment(@LayoutRes val view: Int = 0) : Fragment() {

    lateinit var baseListener: KotlinBaseListener
    private var baseViewModel: KotlinBaseViewModel? = null

    fun setViewModel(vm: KotlinBaseViewModel) {
        baseViewModel = vm
        lifecycle.addObserver(baseViewModel!!)
        observeProgress()
        listenForError()
    }

    override fun onAttach(context: Context) {

        printLogs("onAttach", javaClass.simpleName)

        super.onAttach(context)
        if (context is KotlinBaseListener) {
            baseListener = context
        } else {
            throw IllegalStateException("You Must have to extends your activity with KotlinBaseActivity")
        }
    }

    private fun printLogs(tag: String, message: String) {
        (activity as KotlinBaseActivity).showLogs(tag, message)
    }

    override fun onResume() {
        super.onResume()
        printLogs("onResume", javaClass.simpleName)
    }

    override fun onStop() {
        super.onStop()
        printLogs("onStop", javaClass.simpleName)
    }

    override fun onStart() {
        super.onStart()
        printLogs("onStart", javaClass.simpleName)
    }

    override fun onPause() {
        super.onPause()
        printLogs("onPause", javaClass.simpleName)
    }

    fun forceHideKeyboard() {
        (activity as KotlinBaseActivity).forceHideKeyboard()
    }

    override fun onDestroyView() {
//        hideLoading()
        printLogs("onDestroyView", javaClass.simpleName)
        super.onDestroyView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        printLogs("onCreateView", javaClass.simpleName)

        requireActivity().onBackPressed {
            (activity as KotlinBaseActivity).checkBackPressEvent()
        }

        return inflater.inflate(view, container, false)
    }


    @DrawableRes
    open fun setBreadCrumbsImage(): Int? {
        return null
    }

    open fun setBreadCrumbsTitle(): String {
        return ""
    }

    fun showLoading() {
        baseListener.showLoading()
    }

    fun hideLoading() {
        baseListener.hideLoading()
    }

    /*  fun onBackPressed() {
          activity?.onBackPressed()
      }*/

    private fun observeProgress() {
        baseViewModel?.progressObserver?.observe(this, androidx.lifecycle.Observer {
            when (it) {
                true -> showLoading()
                false -> hideLoading()
            }
        })
    }

    private fun listenForError() {
        baseViewModel?.errorObserver?.observe(this, androidx.lifecycle.Observer {
            when (it.errorType) {
                ErrorType.TOAST -> {
                    showToast(it.msg ?: "")
                }//snack(it.msg ?: "")
                ErrorType.CENTER_TOAST -> showToast(it.msg ?: "")
            }
        })
    }
}

fun FragmentActivity.onBackPressed(callback: () -> Unit) {
    onBackPressedDispatcher.addCallback(this,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                callback()
            }
        }
    )
}