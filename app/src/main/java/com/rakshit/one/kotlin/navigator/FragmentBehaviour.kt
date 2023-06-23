package com.test.papers.kotlin.navigator

import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

@Keep
abstract class FragmentBehaviour {
    abstract fun execute(transaction: FragmentTransaction, fragment: Fragment, tag: String?)
    abstract fun setContainer(container: Int)
}