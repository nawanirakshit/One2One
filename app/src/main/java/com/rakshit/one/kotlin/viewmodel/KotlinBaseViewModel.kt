package com.test.papers.kotlin.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import android.sleek.construction.io.networking.coroutineRequestAdapter.NetworkResponse
import android.sleek.construction.io.networking.coroutineRequestAdapter.responseChecker
import com.test.papers.kotlin.viewmodel.common.CommonError
import com.test.papers.kotlin.viewmodel.common.ErrorType
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class KotlinBaseViewModel : ViewModel(), LifecycleObserver {
    private var disposables: CompositeDisposable = CompositeDisposable()
    private val errorLiveData by lazy { VolatileLiveData<CommonError>() }
    val errorObserver get() = errorLiveData
    private val progressLiveData by lazy { VolatileLiveData<Boolean>() }
    val progressObserver get() = progressLiveData


    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    fun addToDisposable(disposable: Disposable?) {
        disposable?.let { disposables.add(it) }
    }

    fun showError(
        errorMSg: String?,
        errorType: ErrorType = ErrorType.TOAST,
        onBackground: Boolean = false
    ) {
        errorMSg?.let {
            if (onBackground) {
                errorLiveData.postValue(CommonError(it, errorType))
            } else errorLiveData.setValue(CommonError(it, errorType))
        }
    }

    fun showProgress(onBackground: Boolean = false) {
        if (onBackground) {
            progressLiveData.postValue(true)
        } else
            progressLiveData.setValue(true)
    }

    fun hideProgress(onBackground: Boolean = false) {
        if (onBackground) {
            progressLiveData.postValue(false)
        } else
            progressLiveData.setValue(false)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        disposables = CompositeDisposable()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        disposables.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onDestroyView() {
        disposables.clear()
    }

    fun CoroutineScope.launchWithProgress(block: suspend CoroutineScope.() -> Unit) {
        launch {
            showProgress()
            block()
        }.invokeOnCompletion {
            hideProgress()
        }
    }

    fun <T> NetworkResponse<Any, Any>.responseChecker(onSuccess: (T) -> Unit) {
        responseChecker(onSuccess, {
            showError(it)
        }, {
            hideProgress()
        })
    }

}