package com.test.papers.kotlin.navigator

import android.os.Bundle
import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.rakshit.one.R
import com.test.papers.kotlin.KotlinBaseFragment
import kotlin.reflect.KClass

@Keep
class Navigator(private val activity: AppCompatActivity, private val container: Int) {

    @AnimatorRes
    @AnimRes
    private var enterAnim = R.anim.slide_in_left

    @AnimatorRes
    @AnimRes
    private var exitAnim = R.anim.slide_out_right

    private val fragmentAdder: FragmentBehaviour by lazy {
        FragmentAdd(container)
    }

    private val fragmentReplacer: FragmentBehaviour by lazy {
        FragmentReplace(container)
    }

    private var lastVisibleFragment: KotlinBaseFragment? = null




    fun replaceFragment(
        clazz: Class<out Fragment>,
        bundle: Bundle?,
        transitionView: View?,
        userTag: String = "",
        addToBackStack: Boolean,
        animation: Boolean = false,
        containerId: Int = 0
    ) {
        executeTransition(
            clazz,
            bundle,
            transitionView,
            fragmentReplacer.apply { setContainer(containerId) },
            activity.supportFragmentManager,
            userTag,
            addToBackStack,
            animation
        )
    }

    fun replaceFragment(
        clazz: Fragment,
        bundle: Bundle?,
        transitionView: View?,
        userTag: String = "",
        addToBackStack: Boolean,
        animation: Boolean = false,
        containerId: Int = 0
    ) {
        executeTransition(
            clazz,
            bundle,
            transitionView,
            fragmentReplacer.apply { setContainer(containerId) },
            activity.supportFragmentManager,
            userTag,
            addToBackStack,
            animation
        )
    }

    private fun executeTransition(
        clazz: Class<out Fragment>,
        bundle: Bundle?,
        transitionView: View?,
        behaviour: FragmentBehaviour,
        manager: FragmentManager,
        userTag: String = "",
        addToBackStack: Boolean,
        animation: Boolean = false
    ) {

        val tag = if (userTag.isEmpty()) clazz.simpleName else userTag
        var fragment = manager.findFragmentByTag(tag)

        if (fragment == null) {
            fragment = makeFragmentAndShowOnScreen(
                fragment,
                clazz,
                manager,
                behaviour,
                addToBackStack,
                tag,
                transitionView,
                animation
            )
        } else {
            manager.popBackStackImmediate(tag, 0)
        }
        fragment?.arguments = bundle

    }

    private fun executeTransition(
        clazz: Fragment,
        bundle: Bundle?,
        transitionView: View?,
        behaviour: FragmentBehaviour,
        manager: FragmentManager,
        userTag: String = "",
        addToBackStack: Boolean,
        animation: Boolean
    ) {


        val tag = if (userTag.isEmpty()) clazz.javaClass.simpleName else userTag
        var fragment = manager.findFragmentByTag(tag)

        if (fragment == null) {
            fragment = makeFragmentAndShowOnScreen(
                clazz,
                clazz.javaClass,
                manager,
                behaviour,
                addToBackStack,
                tag,
                transitionView,
                animation
            )
        } else {
            manager.popBackStackImmediate(tag, 0)
        }
        fragment?.arguments = bundle
    }

    fun setCustomeAnimation(@AnimatorRes @AnimRes enterAnim: Int, @AnimatorRes @AnimRes exitAnim: Int) {
        this.enterAnim = enterAnim
        this.exitAnim = exitAnim
    }

    private fun makeFragmentAndShowOnScreen(
        fragment: Fragment?,
        clazz: Class<out Fragment>,
        manager: FragmentManager,
        behaviour: FragmentBehaviour,
        addToBackStack: Boolean,
        tag: String?, transitionView: View?, animation: Boolean = false
    ): Fragment? {
        var fragment1 = fragment
        if (fragment1 == null)
            fragment1 = clazz.newInstance()
        val transaction = manager.beginTransaction()
//        if (animation) transaction.setCustomAnimations(
//            enterAnim,
//            exitAnim,
//            R.anim.slide_in_right,
//            R.anim.slide_out_left
//        )
        behaviour.execute(transaction, fragment1!!, tag)

//        if (transitionView != null) {
//            transaction.addSharedElement(
//                transitionView,
//                ViewCompat.getTransitionName(transitionView) ?: ""
//            )
//        }
        if (addToBackStack) {
            transaction.addToBackStack(tag)
        } else transaction.addToBackStack(null)
        transaction.commit()
        return fragment1
    }


    fun addFragment(
        clazz: Class<out Fragment>,
        bundle: Bundle? = null,
        transitionView: View? = null,
        tag: String = "",
        addToBackStack: Boolean,
        animation: Boolean = false,
        containerId: Int = 0
    ) {
        executeTransition(
            clazz,
            bundle,
            transitionView,
            fragmentAdder.apply { setContainer(containerId) },
            activity.supportFragmentManager,
            tag,
            addToBackStack,
            animation
        )
    }

    fun addFragment(
        clazz: Fragment,
        bundle: Bundle? = null,
        transitionView: View? = null,
        tag: String = "",
        addToBackStack: Boolean,
        animation: Boolean = false,
        containerId: Int = 0
    ) {
        executeTransition(
            clazz,
            bundle,
            transitionView,
            fragmentAdder.apply { setContainer(containerId) },
            activity.supportFragmentManager,
            tag,
            addToBackStack,
            animation
        )
    }

    fun getCurrentFragmentTag(): String? {
        val findFragmentById =
            activity.supportFragmentManager.findFragmentById(container)
        return findFragmentById?.javaClass?.simpleName
    }

    fun getCurrentFragment() = activity.supportFragmentManager.findFragmentById(container)

    fun getCurrentFragment(containerId: Int) = activity.supportFragmentManager.findFragmentById(containerId)

    fun addChildFragment(
        childFragmentManager: FragmentManager,
        container: Int,
        kClass: KClass<out Fragment>,
        addToBackStack: Boolean,
        bundle: Bundle = Bundle(),
        animation: Boolean
    ) {
        executeTransition(
            kClass.java,
            bundle,
            null,
            FragmentAdd(container),
            childFragmentManager,
            kClass.java.name,
            addToBackStack,
            animation
        )
    }

    fun replaceChildFragment(
        childFragmentManager: FragmentManager,
        container: Int,
        kClass: KClass<out Fragment>,
        addToBackStack: Boolean,
        bundle: Bundle = Bundle(),
        animation: Boolean
    ) {
        executeTransition(
            kClass.java,
            bundle,
            null,
            FragmentReplace(container),
            childFragmentManager,
            kClass.java.name,
            addToBackStack,
            animation
        )
    }

    fun addChildFragment(
        childFragmentManager: FragmentManager,
        container: Int,
        fragment: Fragment,
        addToBackStack: Boolean,
        bundle: Bundle = Bundle(),
        animation: Boolean
    ) {
        executeTransition(
            fragment,
            bundle,
            null,
            FragmentAdd(container),
            childFragmentManager,
            "",
            addToBackStack,
            animation
        )
    }

    fun replaceChildFragment(
        childFragmentManager: FragmentManager,
        container: Int,
        fragment: Fragment,
        addToBackStack: Boolean,
        bundle: Bundle = Bundle(),
        animation: Boolean
    ) {
        executeTransition(
            fragment,
            bundle,
            null,
            FragmentReplace(container),
            childFragmentManager,
            "",
            addToBackStack,
            animation
        )
    }


    fun bringFragmentToFrontIfPresentOrCreate(clazz: Class<out KotlinBaseFragment>) {

        val tag = clazz.simpleName
        val manager = activity.supportFragmentManager
        var fragment = manager.findFragmentByTag(tag)

        val transition = manager.beginTransaction()

        if (fragment == null) {
            fragment = clazz.newInstance()
            fragmentAdder.execute(transition, fragment, tag)
            transition.addToBackStack(tag)
        } else {
            transition.show(fragment)
        }

        lastVisibleFragment?.run {
            if (this != fragment) {
                transition.hide(this)
                userVisibleHint = false
            }
        }

        transition.commitAllowingStateLoss()


        val newFragment = fragment as KotlinBaseFragment
        newFragment.userVisibleHint = true

        lastVisibleFragment = newFragment


    }

    fun getLastAddedFragment(): KotlinBaseFragment? {
        return lastVisibleFragment
    }

    fun lastFragmentChanged(fragment: KotlinBaseFragment) {
        lastVisibleFragment = fragment
    }

}