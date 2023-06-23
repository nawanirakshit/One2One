package com.test.papers.kotlin


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    private val fragments: ArrayList<Fragment>,
    private val titles: ArrayList<String> = arrayListOf(),
    private val showTitle: Boolean = false
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (showTitle) {

            titles[position]
        } else
            super.getPageTitle(position)
    }


}