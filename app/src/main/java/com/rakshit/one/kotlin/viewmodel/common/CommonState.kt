package com.test.papers.kotlin.viewmodel.common

/**
 * Created by Akash Saggu(R4X)
 * akashsaggu@protonmail.com
 */
data class CommonState<T>(val success: Boolean, val data: T? = null, val msg: String = "")


fun <T> CommonState<T>.check(success: (T?) -> Unit, failure: (String) -> Unit) {
    when (this.success) {
        true -> success.invoke(this.data)
        false -> failure.invoke(this.msg)
    }

}