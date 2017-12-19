package com.wuhenzhizao.adapter.example.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.wuhenzhizao.adapter.example.R
import com.wuhenzhizao.adapter.example.ui.*


/**
 * Created by liufei on 2017/10/26.
 */
class MainTabFragmentAdapter(context: Context, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private var titles: Array<String> = context.resources.getStringArray(R.array.main_tabs)

    override fun getCount(): Int = titles.size

    override fun getPageTitle(position: Int): CharSequence = titles[position]

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> SingleTypeListViewFragment()
        1 -> SingleTypeRecyclerViewFragment()
        2 -> SingleTypeRecyclerViewBindingFragment()

        4 -> MultipleTypeRecyclerViewFragment()
        5 -> StickyRecyclerViewFragment()
        6 -> SwipeMenuRecyclerViewFragment()
        7 -> DragRecyclerViewFragment()
        8 -> SwipeDismissRecyclerViewFragment()
        else -> SingleTypeRecyclerViewFragment()
    }
}